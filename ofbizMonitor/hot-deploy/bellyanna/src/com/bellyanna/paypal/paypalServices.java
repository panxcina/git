package com.bellyanna.paypal;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javolution.util.FastMap;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;

public class paypalServices {
	private static final String module = paypalServices.class.getName();
    private static final String eol = System.getProperty("line.separator");
    
    public static Map<String, Object> doPaypalRefund(DispatchContext dctx, Map context)
    throws GenericEntityException { //doPaypalRefund
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String refundId = (String) context.get("refundId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map mapAccount = FastMap.newInstance();
        Map result = ServiceUtil.returnSuccess();
        String ack = null;
        boolean ackSuccess = false;
        String shortMessage = null;
        String longMessage = null;
        String refundTransactionId = null;
        String refundReason = null;
        
        GenericValue refund = delegator.findOne("Refund", UtilMisc.toMap("refundId", refundId), false);
        GenericValue orderHeader = refund.getRelatedOne("OrderHeader", false);
        String orderId = orderHeader.getString("orderId");
        String refundStatus = refund.getString("statusId").toString();
        if (refund.getString("reason") == null) {
            refundReason = eol;
        }
        else {
            refundReason = refund.getString("reason").toString();
        }
        
        if ("REFUND_PENDING".equals(refundStatus)) {   //if refundStatus = Pending -- START
            mapAccount = accountInfo(delegator, refund);
            mapAccount.put("paypalTransactionId", refund.getString("paypalTransactionId"));
            if ("Y".equals(refund.getString("fullRefund")))  {
                mapAccount.put("refundType", "Full");
            } else {
                mapAccount.put("refundType", "Partial");
            }
            mapAccount.put("amount", refund.getString("amount"));
            mapAccount.put("currencyCode", refund.getString("currencyUomId"));
            mapAccount.put("method", "RefundTransaction");
            
            Iterator<String> keyIt = mapAccount.keySet().iterator();
            while(keyIt.hasNext()) {    //loop while mapAccount key pair value -- START
                String key = keyIt.next();
                String value = mapAccount.get(key).toString();
                //Debug.logError("key: " + key + ", value: " + value, module);
            }   //loop while mapAccount key pair value -- END
            
            try {   //try -- START
                String responseHttpCode = sendHttpRequest(mapAccount);
                //Debug.logError("requestHTTPCODE: " + responseHttpCode, module);
                //if responseHttpCode is null -- START
                if (responseHttpCode == null) {
                    //Debug.logError("ResponseHttpCode returns null", module);
                    refundReason = normalizeRefundReason(refundReason);
                    refund.set("reason", refundReason + eol + "API error: Could not connect to server, retry again");
                    refund.store();
                }
                //if responseHttpCode is null -- END
                else {  //if responseHttpCode is not null -- Start
                    responseHttpCode = URLDecoder.decode(responseHttpCode, "UTF-8");
                    String[] params = responseHttpCode.split("&");
                    Map<String, String> map = FastMap.newInstance();
                    for (String param : params) {   //for loop URL split -- START
                        String name = param.split("=")[0];
                        String value = param.split("=")[1];
                        map.put(name, value);
                    }   //for loop URL split -- END
                    
                    Set<String> keys = map.keySet();
                    for (String key : keys) {   //for loop URL parameter -- START
                        /*Debug.logError("Name=" + key, module);
                         Debug.logError("Value=" + map.get(key), module);*/
                        //check ACK -- START
                        if (key.toUpperCase().equals("ACK")) {
                            //Debug.logError("ACK returns " + map.get(key), module);
                            ack = map.get(key);
                        }
                        //check ACK -- END
                        
                        //check ShortMessage -- START
                        if (key.toUpperCase().matches(".*SHORTMESSAGE.*")) {
                            //Debug.logError("SHORTMESSAGE returns " + map.get(key), module);
                            shortMessage = map.get(key);
                        }
                        //check ShortMessage -- END
                        
                        //check LongMessage -- START
                        if (key.toUpperCase().matches(".*LONGMESSAGE.*")) {
                            //Debug.logError("LONGMESSAGE returns " + map.get(key), module);
                            longMessage = map.get(key);
                        }
                        //check LongMessage -- END
                        
                        //check refundTransactionId -- START
                        if (key.toUpperCase().matches(".*REFUNDTRANSACTIONID.*")) {
                            //Debug.logError("REFUNDTRANSACTIONID returns " + map.get(key), module);
                            refundTransactionId = map.get(key);
                        }
                        //check refundTransactionId -- END
                        
                        //check FEEREFUNDAMT -- START
                        if (key.toUpperCase().matches(".*FEEREFUNDAMT.*")) {
                            //Debug.logError("FEEREFUNDAMT returns " + map.get(key), module);
                        }
                        //check FEEREFUNDAMT -- END
                        
                        //check GROSSREFUNDAMT -- START
                        if (key.toUpperCase().matches(".*GROSSREFUNDAMT.*")) {
                            //Debug.logError("GROSSREFUNDAMT returns " + map.get(key), module);
                        }
                        //check GROSSREFUNDAMT -- END
                        
                        //check NETREFUNDAMT -- START
                        if (key.toUpperCase().matches(".*NETREFUNDAMT.*")) {
                            //Debug.logError("NETREFUNDAMT returns " + map.get(key), module);
                        }
                        //check NETREFUNDAMT -- END
                        
                        //check CURRENCYCODE -- START
                        if (key.toUpperCase().matches(".*CURRENCYCODE.*")) {
                            //Debug.logError("CURRENCYCODE returns " + map.get(key), module);
                        }
                        //check CURRENCYCODE -- END
                        
                        //check TOTALREFUNDEDAMOUNT -- START
                        if (key.toUpperCase().matches(".*TOTALREFUNDEDAMOUNT.*")) {
                            //Debug.logError("TOTALREFUNDEDAMOUNT returns " + map.get(key), module);
                        }
                        //check TOTALREFUNDEDAMOUNT -- END
                        
                        //check TIMESTAMP -- START
                        if (key.toUpperCase().matches(".*TIMESTAMP.*")) {
                            //Debug.logError("TIMESTAMP returns " + map.get(key), module);
                        }
                        //check TIMESTAMP -- END
                        
                        //check CORRELATIONID -- START
                        if (key.toUpperCase().matches(".*CORRELATIONID.*")) {
                            //Debug.logError("CORRELATIONID returns " + map.get(key), module);
                        }
                        //check CORRELATIONID -- END
                        
                        //check REFUNDSTATUS -- START
                        if (key.toUpperCase().matches(".*REFUNDSTATUS.*")) {
                            //Debug.logError("REFUNDSTATUS returns " + map.get(key), module);
                        }
                        //check REFUNDSTATUS -- END
                        
                        //check PENDINGREASON -- START
                        if (key.toUpperCase().matches(".*PENDINGREASON.*")) {
                            //Debug.logError("PENDINGREASON returns " + map.get(key), module);
                        }
                        //check PENDINGREASON -- END
                    }   //for loop URL parameter -- END
                    
                    if (ack.equals("Success")) {    //if ack success -- START
                        ackSuccess = true;
                        refundReason = normalizeRefundReason(refundReason);
                        refund.set("reason", refundReason);
                        refund.set("refundPaypalTransId", refundTransactionId);
                        refund.set("statusId", "REFUND_COMPLETED");
                        refund.store();
                        
                        //send message to customer via eBay message -- START
                        Map<String, Object> emailContent = FastMap.newInstance();
                        emailContent = getRefundPaypalEmailContent(dctx, orderId);
                        //Debug.logError(emailContent.toString(), module);
                        

                        Map sendMessage = dispatcher.runSync(
                                           "TradingApiAddMemberMessageAAQToPartnerRequest",
                                           UtilMisc.toMap(
                                                          "productStoreId", emailContent.get("productStoreId").toString(),
                                                          "eBayItemId", emailContent.get("eBayItemId").toString(),
                                                          "eBayUserId", emailContent.get("eBayUserId").toString(),
                                                          "questionType", "Payment",
                                                          "messageSubject", emailContent.get("subject").toString(),
                                                          "messageBody", emailContent.get("emailContent").toString(),
                                                          "userLogin", userLogin
                                                          )
                                           );
                        //send message to customer via eBay message -- END
                        
                        //create Order Note -- START
                        String note = null;
                        if ("Y".equals(refund.getString("fullRefund")))  {
                            note = "Fully";
                        } else {
                            note = "Partial";
                        }
                        note = note + " refunded " + refund.getString("currencyUomId") + " " + refund.getString("amount") + " (" + refundTransactionId + ")";
                        
                        if (ServiceUtil.isSuccess(sendMessage)) {
                            note = note + eol + "Refund confirmation message has been successfully sent to customer via eBay message";                        }
                        else {
                            note = note + eol + "Failed to send refund confirmation message to customer via eBay message";
                        }
                        
                        dispatcher.runSync(
                                           "createOrderNote",
                                           UtilMisc.toMap(
                                                          "orderId", orderId,
                                                          "internalNote", "Y",
                                                          "note", note,
                                                          "userLogin", userLogin
                                                          )
                                           );
                        //create Order Note -- END
                    }   //if ack success -- END
                    else if (ack.equals("Failure")) {  //if ack failure -- START
                        ackSuccess = false;
                        refundReason = normalizeRefundReason(refundReason);
                        refund.set("reason", refundReason + eol + "API ShortMessage: " + shortMessage + eol + "API LongMessage: " + longMessage);
                        refund.store();
                    }   //if ack failure -- END
                    else {  //if ack success/failure with warning -- START
                        
                    }   //if ack success/failure with warning -- END
                }   //if responseHttpCode is not null -- END
            }   //try -- END
            catch (Exception e) { //catch -- START
                e.printStackTrace();
                //Debug.logError(e.getMessage(), module);
                result = ServiceUtil.returnError(e.getMessage());
            }   //catch -- END
            
        }   //if refundStatus = Pending -- END
        else {
            //Debug.logError("PayPal-LOG: Refund Status is not equal to REFUND_PENDING", module);
            result = ServiceUtil.returnError("Refund Status is not equal to REFUND_PENDING");
        }
        
        return result;
        
    }   //End of doPaypalRefund
	
    public static Map<String, Object> accountInfo (Delegator delegator, GenericValue refund)
    throws GenericEntityException { //accountInfo
		
		try {
            String orderId = refund.getString("orderId");
            String paypalEmailAddress = refund.getString("paypalEmailAddress");
            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", orderHeader.getString("productStoreId")), false);
			String productStoreId = productStore.getString("productStoreId");
			Map mapAccount = FastMap.newInstance();
			
			//Get PayPal partyId information
			List<GenericValue> productStoreRole = delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStoreId, "roleTypeId", "PAYPAL_ACCOUNT"), null, false);
			if(productStoreRole != null && !productStoreRole.isEmpty()) {
				GenericValue paypalPartyGroup = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", EntityUtil.getFirst(productStoreRole).getString("partyId")), false);
				
                //checking PayPal Email Address -- START
                if(refund.getString("paypalEmailAddress").equals(paypalPartyGroup.getString("groupName"))) {
                    mapAccount.put("paypalEmailAddress", paypalPartyGroup.getString("groupName"));
                    mapAccount.put("paypalPartyId", paypalPartyGroup.getString("partyId"));
                } else {
                    GenericValue partyGroup = EntityUtil.getFirst(delegator.findByAnd("PartyGroup", UtilMisc.toMap("groupName", paypalEmailAddress), null, false));
                    mapAccount.put("paypalPartyId", partyGroup.getString("partyId"));
                }
                //checking PayPal Email Address -- END
                
            }
			else {
				Debug.logError("empty ProductStoreRole", module);
				return ServiceUtil.returnError("No ProductStoreRole found");
            }
            
            //Get eBay Account
            List<GenericValue> productStoreRoleEbay = delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStoreId, "roleTypeId", "EBAY_ACCOUNT"), null, false);
			if(productStoreRoleEbay != null && !productStoreRoleEbay.isEmpty()) {
				GenericValue paypalPartyGroup = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", EntityUtil.getFirst(productStoreRoleEbay).getString("partyId")), false);
				mapAccount.put("ebayAccount", paypalPartyGroup.getString("groupName"));
            }
			else {
				Debug.logError("empty ProductStoreRole", module);
				return ServiceUtil.returnError("No ProductStoreRole found");
            }
            
            //Get PayPal API Credential
            List<GenericValue> partyIdentificationList = delegator.findByAnd("PartyIdentification", UtilMisc.toMap("partyId",mapAccount.get("paypalPartyId")), null, false);
            if(partyIdentificationList != null && partyIdentificationList.size()>0) {   //if partyIdentificationList is not null -- START
                for(int i = 0; i < partyIdentificationList.size(); i++) {   //Loop partyIdentificationList -- START
                    if(partyIdentificationList.get(i).getString("partyIdentificationTypeId").equals("PP_ACCOUNT")) {
                        mapAccount.put("APIAccount", partyIdentificationList.get(i).getString("idValue"));
                    }
                    else if (partyIdentificationList.get(i).getString("partyIdentificationTypeId").equals("PP_USERNAME")) {
                        mapAccount.put("APIUsername", partyIdentificationList.get(i).getString("idValue"));
                    }
                    else if (partyIdentificationList.get(i).getString("partyIdentificationTypeId").equals("PP_PASSWORD")) {
                        mapAccount.put("APIPassword", partyIdentificationList.get(i).getString("idValue"));
                    }
                    else if (partyIdentificationList.get(i).getString("partyIdentificationTypeId").equals("PP_SIGNATURE")) {
                        mapAccount.put("APISignature", partyIdentificationList.get(i).getString("idValue"));
                    }
                }   //Loop partyIdentificationList -- END
            }   //if partyIdentificationList is not null -- END
            else {
                Debug.logError("PAYPAL-LOG: PayPal party ID " + mapAccount.get("paypalPartyId") + " does not have PayPal API Credential in PartyIdentification", module);
                return ServiceUtil.returnError("No PayPal API Credential (PartyIdentification) found");
            }
            
			return mapAccount;
		}		//try block end
		catch (GenericEntityException e) {
			e.printStackTrace();
			//Debug.logError(e.getMessage(), module);
			return ServiceUtil.returnError(e.getMessage());
		}
	}   //End of accountInfo
    
    public static String sendHttpRequest(Map mapContent) throws IOException
    {   //sendHttpRequest
        Properties properties = new Properties();
        properties.load(new FileInputStream("hot-deploy/bellyanna/config/PayPal.properties"));
        String postItemsUrl = properties.getProperty("PayPal.url");
        String version = properties.getProperty("PayPal.version");
        String response = null;
                
        Map<String, String> requestPropertyMap = FastMap.<String, String>newInstance();
        //requestPropertyMap.put("Content-Length", generatedXmlData.getBytes().length + "");
        requestPropertyMap.put("USER", mapContent.get("APIUsername").toString());
        requestPropertyMap.put("PWD", mapContent.get("APIPassword").toString());
        requestPropertyMap.put("SIGNATURE", mapContent.get("APISignature").toString());
        requestPropertyMap.put("METHOD", mapContent.get("method").toString());
        requestPropertyMap.put("VERSION", version);
        requestPropertyMap.put("TRANSACTIONID", mapContent.get("paypalTransactionId").toString());
        
        /*String refundType = mapContent.get("refundType").toString();
        if ("Full".equals(refundType))  {
            requestPropertyMap.put("REFUNDTYPE", "Full");
        } else {
            requestPropertyMap.put("REFUNDTYPE", "Partial");
            requestPropertyMap.put("AMT", mapContent.get("amount").toString());
            requestPropertyMap.put("CURRENCYCODE", mapContent.get("currencyCode").toString());
            requestPropertyMap.put("NOTE", "Refund from " + mapContent.get("ebayAccount").toString());
        }*/
        
        //new code
        try {
            String myUrl = postItemsUrl
            + "USER=" + mapContent.get("APIUsername")
            + "&PWD=" + mapContent.get("APIPassword")
            + "&SIGNATURE=" + mapContent.get("APISignature")
            + "&METHOD=" + mapContent.get("method")
            + "&VERSION=" + version
            + "&TRANSACTIONID=" + mapContent.get("paypalTransactionId");
            
            String refundType = mapContent.get("refundType").toString();
            if ("Full".equals(refundType))  {
                requestPropertyMap.put("REFUNDTYPE", "Full");
                myUrl += "&REFUNDTYPE=Full";
            } else {
                requestPropertyMap.put("REFUNDTYPE", "Partial");
                requestPropertyMap.put("AMT", mapContent.get("amount").toString());
                requestPropertyMap.put("CURRENCYCODE", mapContent.get("currencyCode").toString());
                requestPropertyMap.put("NOTE", "Refund from " + mapContent.get("ebayAccount").toString());
                myUrl += "&REFUNDTYPE=Partial";
                myUrl += "&AMT=" + mapContent.get("amount");
                myUrl += "&CURRENCYCODE=" + mapContent.get("currencyCode");
                myUrl += "&NOTE=Refund%20from%20" + mapContent.get("ebayAccount");
            }
            //Debug.logError("URL is : " + myUrl, module);
            
            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(60*1000);
            connection.connect();
            //xmlstream = connection.getInputStream();
		/*}
        catch (Exception e) {
	    	e.printStackTrace();
		} //new code*/
        
        /*HttpURLConnection connection = (HttpURLConnection) (new URL(postItemsUrl)).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        
        if(requestPropertyMap != null && !requestPropertyMap.isEmpty()) {
            Iterator<String> keyIt = requestPropertyMap.keySet().iterator();
            while(keyIt.hasNext()) {
                String key = keyIt.next();
                String value = requestPropertyMap.get(key);
                Debug.logError("URL Key: " + key + " value is " + value, module);
                connection.setRequestProperty(key, value);
            }
        }*/
        
        /*OutputStream outputStream = connection.getOutputStream();
        outputStream.write(generatedXmlData.toString().getBytes());
        outputStream.flush();*/
        
        int responseCode = connection.getResponseCode();
        InputStream inputStream = null;
        
        
        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = connection.getInputStream();
            response = inputStreamToString(inputStream);
        } else {
            inputStream = connection.getErrorStream();
            response = inputStreamToString(inputStream);
        }
    
    
        return (response == null || "".equals(response.trim())) ? String.valueOf(responseCode) : response;
        }//new code
        catch (Exception e) {
            e.printStackTrace();
        }//new code
        return response;
        
    }   //End of sendHttpRequest
    
    private static String normalizeRefundReason(String refundReason) {  //normalizeRefundReason
        if (refundReason.contains("ShortMessage: Transaction refused") && refundReason.contains("LongMessage: You are over the time limit")) {
            refundReason = refundReason.replaceAll(eol + "API ShortMessage: Transaction refused", "");
            refundReason = refundReason.replaceAll(eol + "API LongMessage: You are over the time limit to perform a refund on this transaction", "");
        }
        if (refundReason.contains("error: Could not connect to server")) {
            refundReason = refundReason.replaceAll(eol + "API error: Could not connect to server, retry again", "");
        }
        if (refundReason.contains("ShortMessage: Permission denied")) {
            refundReason = refundReason.replaceAll(eol + "API ShortMessage: Permission denied", "");
            refundReason = refundReason.replaceAll(eol + "API LongMessage: You do not have permission to refund this transaction", "");
        }
        return refundReason;
    }   //normalizeRefundReason
    
    private static String inputStreamToString(InputStream inputStream) throws IOException
    {
        String string;
        StringBuilder outputBuilder = new StringBuilder();
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (null != (string = reader.readLine())) {
                outputBuilder.append(string).append('\n');
            }
        }
        return outputBuilder.toString();
    }   //End of inputStreamToString
    
    public static Map<String, Object> getRefundPaypalEmailContent (DispatchContext dctx, String orderId)
	throws IOException, GenericEntityException, GenericServiceException {   //getRefundPaypalEmailContent -- START
        
        Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();

        String sellerUserId = null;
        
        try {   //try -- START
            GenericValue ebayEmailTemplate = delegator.findOne("EbayEmailTemplate", UtilMisc.toMap("emailTemplateId", "10020", "emailTemplateTypeId", "Refund"), false);
            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            GenericValue orderItem = EntityUtil.getFirst(delegator.findByAnd("OrderItem", UtilMisc.toMap("orderId", orderId), null, false));
            GenericValue orderItemAttribute = delegator.findOne("OrderItemAttribute", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItem.getString("orderItemSeqId"), "attrName", "eBay Item Number"), false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            GenericValue productStoreRole = EntityUtil.getFirst(delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"), "roleTypeId", "EBAY_ACCOUNT"), null, false));
            GenericValue partyGroup = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", productStoreRole.getString("partyId")), false);
            GenericValue ebayContactMech = EntityUtil.getFirst(delegator.findByAnd("OrderContactMech", UtilMisc.toMap("orderId", orderId, "contactMechPurposeTypeId", "EBAY_USER_ID"), null, false));
            GenericValue eBayUserId = delegator.findOne("ContactMech", UtilMisc.toMap("contactMechId", ebayContactMech.getString("contactMechId")), false);
            GenericValue refund = EntityUtil.getFirst(delegator.findByAnd("Refund", UtilMisc.toMap("orderId", orderId), null, false));
            sellerUserId = partyGroup.getString("groupName");
            
            String subjectTemplate = ebayEmailTemplate.getString("subject");    //building subject part -- START
            VelocityContext subjectVC = new VelocityContext();
            subjectVC.put("sellerUserId", sellerUserId);
            StringWriter subjectBody = new StringWriter();
            Velocity.evaluate(subjectVC, subjectBody, "subjectTemplate", subjectTemplate);  //building subject part -- END
            
            String mainTemplate = ebayEmailTemplate.getString("content");   //building main part -- START
            VelocityContext mainVC = new VelocityContext();
            mainVC.put("currencyCode", refund.getString("currencyUomId"));
            mainVC.put("amount", refund.getString("amount"));
            mainVC.put("refundTransactionId", refund.getString("refundPaypalTransId"));
            mainVC.put("sellerUserId", sellerUserId);
            StringWriter contentBody = new StringWriter();
            Velocity.evaluate(mainVC, contentBody, "mainTemplate", mainTemplate);   //building main part -- END
            
            result.put("productStoreId", productStore.getString("productStoreId"));
            result.put("eBayItemId", orderItemAttribute.getString("attrValue"));
            result.put("eBayUserId", eBayUserId.getString("infoString"));
            result.put("subject", subjectBody);
            result.put("emailContent", contentBody);
            
        }   //try -- END
        catch (Exception e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return result;
    }   //getRefundPaypalEmailContent -- END
    
}	//END class
