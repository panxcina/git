/* Step 1: addAPACShippingPackageRequestXML
 * Step 2: getAPACShippingLabelRequestXML
 * Step 3: confirmAPACShippingPackageRequestXML
 * Step 4: getAPACShippingPackageStatusRequestXML - This one still needs to be confirmed if it needs to be run or not

Calls list:
1. AddAPACShippingPackage
2. CancelAPACShippingPackage
3. ConfirmAPACShippingPackage
4. GetAPACShippingLabel
5. GetAPACShippingLabels
6. GetAPACShippingPackage
7. GetAPACShippingPackageStatus
8. GetAPACShippingRate
9. GetAPACShippingTrackCode
10. RecreateAPACShippingPackage
11. VerifyAPACShippingUser

@see http://shippingapi.ebay.cn/production/v2/orderservice.asmx
*/
package com.bellyanna.shipment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.math.BigDecimal;
import java.util.List;
import java.util.LinkedList;
import java.util.Locale;
import javax.servlet.http.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


import javolution.util.FastList;
import javolution.util.FastMap;

import org.dom4j.io.SAXReader;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilXml;
import org.ofbiz.base.util.UtilValidate;
//import org.ofbiz.content.content.ContentWorker;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import freemarker.template.TemplateException;

import com.bellyanna.shipment.common;

public class ePacket {

private static final String module= ePacket.class.getName();
    private static boolean displayXmlLog () {
        //Load Properties file
        boolean displayXmlLog = true;
        try {
            String displayXmlLogProps = (String) epacketProperties().get("DisplayXmlLog");
            if (("N").equals(displayXmlLogProps)) {
                displayXmlLog = false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return displayXmlLog;
    }
    
    public static Map<String, String> epacketProperties ()
    throws IOException {
        
        Map<String, String> mapContent = FastMap.newInstance();
        try {   //main try -- START
            Properties properties = new Properties();
            properties.load(new FileInputStream("hot-deploy/bellyanna/config/ePacket.properties"));
            Enumeration e = properties.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                mapContent.put(key, properties.getProperty(key).toString());
            }
        }   //main try -- END
        catch (IOException e) {
            e.printStackTrace();
        }
        return mapContent;
    }
    
    public static Map<String, Object> accountInfo (Delegator delegator, GenericValue productStore)
    throws GenericEntityException, IOException { //accountInfo
        
        Map mapAccount = FastMap.newInstance();
        try {   //try block -- START
            List<GenericValue> productStoreRoleList = delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"), "roleTypeId", "EBAY_ACCOUNT"), null, false);
            if (productStoreRoleList.isEmpty()) {   //if productStoreRoleList is empty -- START
                Debug.logError("ProductStoreId " + productStore.getString("productStoreId") + " does not have eBay account role", module);
                return ServiceUtil.returnError("ProductStoreId " + productStore.getString("productStoreId") + " does not have eBay account role");
            }   //if productStoreRoleList is empty -- END
            GenericValue productStoreRole = EntityUtil.getFirst(productStoreRoleList);
            Map<String, String> epacketProps = epacketProperties(); //Load Properties file
            
            //Get eBay Account Party Group and Party Identification for EUB
            GenericValue ebayAccountPartyGroup = productStoreRole.getRelatedOne("PartyGroup", false);
            GenericValue ebayAccountParty = ebayAccountPartyGroup.getRelatedOne("Party", false);
            List<GenericValue> partyIdentificationList = delegator.findByAnd("PartyIdentification", UtilMisc.toMap("partyId",ebayAccountParty.getString("partyId")), null, false);
            
            if(!partyIdentificationList.isEmpty()) {    //if partyIdentificationList is not null -- START
                for (GenericValue partyIdentification : partyIdentificationList) {  //loop partyIdentificationList -- START
                    if (partyIdentification.getString("partyIdentificationTypeId").equals("EUB_DEV_ID")) {
                        mapAccount.put("APIDevUserId", partyIdentification.getString("idValue"));
                    }
                    else if (partyIdentification.getString("partyIdentificationTypeId").equals("EUB_API_CODE")) {
                        mapAccount.put("APIPassword", partyIdentification.getString("idValue"));
                    }
                    else if (partyIdentification.getString("partyIdentificationTypeId").equals("EUB_ACCOUNT")) {
                        mapAccount.put("APISellerUserId", partyIdentification.getString("idValue"));
                    }
                }   //loop partyIdentificationList -- END
                mapAccount.put("APIVersion", epacketProps.get("Version"));
            }   //if partyIdentificationList is not null -- END
            else {
                Debug.logError("EPACKET-LOG: eBay account party ID " + ebayAccountParty.getString("partyId") + " does not have EUB identification", module);
                return ServiceUtil.returnError("ebay Account " + ebayAccountParty.getString("partyId") + " does not have EUB identification");
            }
        }		//try block - END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        return mapAccount;
    }   //accountInfo
    
    public static String sendRequestXMLtoEpacket(String generatedXmlData)
    throws IOException {    //sendRequestXMLtoEpacket
        
        String postItemsUrl = null;
        String host = null;
        try {
            Map epacketProps = epacketProperties(); //load Properties file
            postItemsUrl = epacketProps.get("Url").toString();
            host = epacketProps.get("Host").toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return e.getMessage().toString();
        }
        
        Map<String, String> requestPropertyMap = FastMap.<String, String>newInstance();
        requestPropertyMap.put("Content-Length", generatedXmlData.getBytes().length + "");
        requestPropertyMap.put("Host", host);
        requestPropertyMap.put("Content-Type", "application/soap+xml; charset=utf-8");
        
        HttpURLConnection connection = (HttpURLConnection) (new URL(postItemsUrl)).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        
        if(requestPropertyMap != null && !requestPropertyMap.isEmpty()) {
            Iterator<String> keyIt = requestPropertyMap.keySet().iterator();
            while(keyIt.hasNext()) {
                String key = keyIt.next();
                String value = requestPropertyMap.get(key);
                connection.setRequestProperty(key, value);
            }
        }
        
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(generatedXmlData.toString().getBytes());
        outputStream.flush();
        
        int responseCode = connection.getResponseCode();
        InputStream inputStream = null;
        String response = null;
        
        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = connection.getInputStream();
            response = inputStreamToString(inputStream);
        } else {
            inputStream = connection.getErrorStream();
            response = inputStreamToString(inputStream);
        }
        
        return (response == null || "".equals(response.trim())) ? String.valueOf(responseCode) : response;
    }   //sendRequestXMLtoEpacket

    public static Map<String, Object> addAPACShippingPackage (DispatchContext dctx, Map serviceContext)
	throws GenericEntityException, GenericServiceException, IOException {   //addAPACShippingPackage
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Map result = ServiceUtil.returnSuccess();
        Map mapContent = FastMap.newInstance();
        String orderId = (String) serviceContext.get("orderId");
        //String shipmentId = (String) serviceContext.get("shipmentId");
        GenericValue userLogin = (GenericValue) serviceContext.get("userLogin");
        
        try {		//try block start
            Map<String, String> epacketProps = epacketProperties(); //Load Properties file
            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = accountInfo(delegator, productStore);	//get account information
            Map getApiShipmentInfo = dispatcher.runSync("getApiShipmentInfo", UtilMisc.toMap("orderId", orderId, "userLogin", userLogin));
            
            List<Map> orderItemMapList = new LinkedList<Map>();
            if (ServiceUtil.isSuccess(getApiShipmentInfo)) {    //if getApiShipmentInfo success -- START
                mapContent = (Map) getApiShipmentInfo.get("mapContent");
                orderItemMapList = (List) getApiShipmentInfo.get("mapItem");
            }   //if getApiShipmentInfo success -- END
            
            mapContent.put("EMSPickUpType", epacketProps.get("EMSPickupType"));	//Set to 0 for carrier to come pickup the parcels. Set to 1 for seller to delivery it to carrier
            mapContent.put("LabelPageSize", epacketProps.get("LabelPageSize"));	//Set to 1 for 4inch thermal printer label. Set to 0 for A4 paper
            mapContent.put("APIDevUserId", mapAccount.get("APIDevUserId"));
            mapContent.put("APIPassword", mapAccount.get("APIPassword"));
            mapContent.put("APIVersion", epacketProps.get("Version"));
            mapContent.put("APISellerUserId", mapAccount.get("APISellerUserId"));
            
            //addAPACShippingPackageRequestXML -- START
            StringBuffer addShippingPackageRequestXML = new StringBuffer();
            addShippingPackageRequestXML.append(ePacketXML.addAPACShippingPackageRequestXML(mapContent));
            
            //Get Item Shipped -- START
            //loop orderItem -- START
            StringBuffer itemListXML = new StringBuffer();
            for (Map itemMapContent : orderItemMapList) {
                itemListXML.append(ePacketXML.AddAPACShippingPackageItemList(itemMapContent));
            } //loop orderItemMapList -- END
            //Get Item Shipped -- END
            //addAPACShippingPackageRequestXML -- END
            
            addShippingPackageRequestXML.append(itemListXML);
            addShippingPackageRequestXML.append("          </ItemList>\r\n");
            addShippingPackageRequestXML.append("          <EMSPickUpType>" + mapContent.get("EMSPickUpType") + "</EMSPickUpType>\r\n");	//Required; 0 - Door pickup; 1 - Seller send to carrier
            addShippingPackageRequestXML.append("        </OrderDetail>\r\n");
            addShippingPackageRequestXML.append("      </AddAPACShippingPackageRequest>\r\n");
            addShippingPackageRequestXML.append("    </AddAPACShippingPackage>\r\n");
            addShippingPackageRequestXML.append("  </soap12:Body>\r\n");
            addShippingPackageRequestXML.append("</soap12:Envelope>");
            
            if (displayXmlLog()) {
                Debug.logError("addAPACShippingPackageRequestXML == START ==", module);
                Debug.logError(addShippingPackageRequestXML.toString(), module);	//Print Request XML to console.log
                Debug.logError("addAPACShippingPackageRequestXML == END ==", module);
            }
            
            //Send request XML to EUB server
            String responseXML = sendRequestXMLtoEpacket(addShippingPackageRequestXML.toString());
            
            if (displayXmlLog()) {
                Debug.logError("addAPACShippingPackageResponseXML == START ==", module);
                Debug.logError(responseXML, module);	//Print XML to console.log
                Debug.logError("addAPACShippingPackageResponseXML == END ==", module);
            }
            
            String writeXMLtoFile = epacketProps.get("WriteXMLtoFile");
            if (writeXMLtoFile.equals("Y")) {
                File f = new File("hot-deploy/bellyanna/webapp/bellyanna/LogXML/Shipment/ePacket-" + orderId + "-RequestXML.xml");
                if(f.exists() && f.isFile()) {
                    f.delete();
                }
                FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/Shipment/ePacket-" + orderId + "-RequestXML.xml", true);
                f1.write(addShippingPackageRequestXML.toString());
                f1.close();
            }
            
            Map testResponseXMLmap = FastMap.newInstance();
            testResponseXMLmap = testResponseXML(responseXML);
            String trackingNumber = null;
            if (testResponseXMLmap.get("Ack") == null || !testResponseXMLmap.get("Ack").equals("Success")) {
                result.put("trackingNumber", "ERROR");
            }
            else {	//TODO
                trackingNumber = responseXML.substring(responseXML.indexOf("<TrackCode>") + "<TrackCode>".length(), responseXML.indexOf("</TrackCode>"));
                GenericValue orderItemShipGroup = EntityUtil.getFirst(delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("orderId", orderId), null, false));
                orderItemShipGroup.put("trackingNumber", trackingNumber);
                delegator.store(orderItemShipGroup);
                result.put("trackingNumber", trackingNumber);
            }
            
            
        }		//try block end
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (GenericServiceException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return result;
        
    }   //addAPACShippingPackage
    
    public static Map<String, Object> getAPACShippingLabel (DispatchContext dctx, Map serviceContext)
	throws GenericEntityException, IOException {    //getAPACShippingLabel
        
        Delegator delegator = dctx.getDelegator();
        String trackingNumber = (String) serviceContext.get("trackingNumber");
        
        try { //start try block
            GenericValue orderItemShipGroup = EntityUtil.getFirst(delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("trackingNumber", trackingNumber), null, false));
            GenericValue orderHeader = orderItemShipGroup.getRelatedOne("OrderHeader", false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = accountInfo(delegator, productStore);	//call function to get account information
            
            Map<String, String> epacketProps = epacketProperties(); //Load Properties file
            mapAccount.put("LabelPageSize", epacketProps.get("LabelPageSize"));
            
            StringBuffer getAPACShippingLabelXML = new StringBuffer();	//building XML Request for getAPACShippingLabel
            getAPACShippingLabelXML.append(ePacketXML.getAPACShippingLabelRequestXML(mapAccount, trackingNumber));
            
            if (displayXmlLog()) {
                Debug.logError("getAPACShippingLabelRequestXML == START ==", module);
                Debug.logError(getAPACShippingLabelXML.toString(), module);	//Print XML to console.log
                Debug.logError("getAPACShippingLabelRequestXML == END ==", module);
            }
            
            //Send request XML to EUB server
            String responseXML = sendRequestXMLtoEpacket(getAPACShippingLabelXML.toString());
            
            if (displayXmlLog()) {
             Debug.logError("getAPACShippingLabelResponseXML == START ==", module);
             Debug.logError(responseXML, module);	//Print XML to console.log
             Debug.logError("getAPACShippingLabelResponseXML == END ==", module);
             }
            
            java.io.ByteArrayInputStream shippingLabelByte = null;
            Map testResponseXMLmap = FastMap.newInstance();
            testResponseXMLmap = testResponseXML(responseXML);
            if (testResponseXMLmap.get("Ack") == null || !testResponseXMLmap.get("Ack").equals("Success")) {
                //Debug.logError("Ack: " + testResponseXMLmap.get("Ack").toString() + " with message: " + testResponseXMLmap.get("Message").toString(), module);
                return ServiceUtil.returnError("ePacket Ack Response: \"" + testResponseXMLmap.get("Ack").toString() + "\" with message \"" + testResponseXMLmap.get("Message").toString() + "\"");
            }
            else {	//TODO
                responseXML = convertToXML(responseXML);
                String content = null;
                int startIndex = responseXML.indexOf("<Label>");
                int endIndex = responseXML.indexOf("</Label>");
                content = responseXML.substring(startIndex + "<Label>".length(), endIndex);
                byte[] bytes = new org.apache.commons.codec.binary.Base64().decode(content.replaceAll("\\r|\\n", "").getBytes());
                shippingLabelByte = new java.io.ByteArrayInputStream(bytes);
            }
            Map result = ServiceUtil.returnSuccess();
            result.put("shippingLabelByte", shippingLabelByte);
            return result;
        }		//try block end
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
    }   //getAPACShippingLabel
    
    public static Map<String, Object> confirmAPACShippingPackage (DispatchContext dctx, Map serviceContext)
    throws GenericEntityException, IOException {    //confirmAPACShippingPackage
        
        Delegator delegator = dctx.getDelegator();
        String trackingNumber = (String) serviceContext.get("trackingNumber");
        
        try { //start try block
            GenericValue orderItemShipGroup = EntityUtil.getFirst(delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("trackingNumber", trackingNumber), null, false));
            GenericValue orderHeader = orderItemShipGroup.getRelatedOne("OrderHeader", false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = accountInfo(delegator, productStore);	//call function to get account information
            
            StringBuffer confirmAPACShippingPackageXML = new StringBuffer();	//building XML Request for confirmAPACShippingPackage
            confirmAPACShippingPackageXML.append(ePacketXML.confirmAPACShippingPackageRequestXML(mapAccount, trackingNumber));
            
            if (displayXmlLog()) {
                Debug.logError("confirmAPACShippingPackageRequestXML == START ==", module);
                Debug.logError(confirmAPACShippingPackageXML.toString(), module);	//Print XML to console.log
                Debug.logError("confirmAPACShippingPackageRequestXML == END ==", module);
            }
            
            //Send request XML to EUB server
            String responseXML = sendRequestXMLtoEpacket(confirmAPACShippingPackageXML.toString());
            
            if (displayXmlLog()) {
                Debug.logError("confirmAPACShippingPackageResponseXML == START ==", module);
                Debug.logError(responseXML, module);	//Print XML to console.log
                Debug.logError("confirmAPACShippingPackageResponseXML == END ==", module);
            }
            
            Map testResponseXMLmap = FastMap.newInstance();
            testResponseXMLmap = testResponseXML(responseXML);
            if (testResponseXMLmap.get("Ack") == null || !testResponseXMLmap.get("Ack").equals("Success")) {
                //Debug.logError("Ack: " + testResponseXMLmap.get("Ack").toString() + " with message: " + testResponseXMLmap.get("Message").toString(), module);
                return ServiceUtil.returnError("ePacket Tracking Number " + trackingNumber + " returns Ack Response: \"" + testResponseXMLmap.get("Ack").toString() + "\" with message \"" + testResponseXMLmap.get("Message").toString() + "\"");
            }
            else {
                return ServiceUtil.returnSuccess("ePacket Tracking Number " + trackingNumber + " returns Ack Response: \"" + testResponseXMLmap.get("Ack").toString() + "\" with message \"" + testResponseXMLmap.get("Message").toString() + "\"");
            }
            
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
    }   //confirmAPACShippingPackage
    
    public static Map<String, Object> getAPACShippingPackageStatus (DispatchContext dctx, Map serviceContext)
    throws GenericEntityException { //getAPACShippingPackageStatus
        
        Delegator delegator = dctx.getDelegator();
        String trackingNumber = (String) serviceContext.get("trackingNumber");
        Map result = ServiceUtil.returnSuccess();
        
        try { //start try block
            GenericValue orderItemShipGroup = EntityUtil.getFirst(delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("trackingNumber", trackingNumber), null, false));
            GenericValue orderHeader = orderItemShipGroup.getRelatedOne("OrderHeader", false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = accountInfo(delegator, productStore);	//call function to get account information
            
            StringBuffer getAPACShippingPackageStatusXML = new StringBuffer();	//building XML Request for confirmAPACShippingPackage
            getAPACShippingPackageStatusXML.append(ePacketXML.getAPACShippingPackageStatusRequestXML(mapAccount, trackingNumber));
            
            if (displayXmlLog()) {
                Debug.logError("getAPACShippingPackageStatusRequestXML == START ==", module);
                Debug.logError(getAPACShippingPackageStatusXML.toString(), module);	//Print XML to console.log
                Debug.logError("getAPACShippingPackageStatusRequestXML == END ==", module);
            }
            
            //Send request XML to EUB server
            String responseXML = sendRequestXMLtoEpacket(getAPACShippingPackageStatusXML.toString());
            
            if (displayXmlLog()) {
                Debug.logError("getAPACShippingPackageStatusResponseXML == START ==", module);
                Debug.logError(responseXML, module);	//Print XML to console.log
                Debug.logError("getAPACShippingPackageStatusResponseXML == END ==", module);
            }
            
            Map testResponseXMLmap = FastMap.newInstance();
            testResponseXMLmap = testResponseXML(responseXML);
            if (testResponseXMLmap.get("Ack") == null || !testResponseXMLmap.get("Ack").equals("Success")) {
                result.put("status", testResponseXMLmap.get("Message").toString());
            }
            else {
                result.put("status", responseXML.substring(responseXML.indexOf("<Status>") + "<Status>".length(), responseXML.indexOf("</Status>")));
                
            }
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        return result;
    }   //getAPACShippingPackageStatus
    
    public static Map<String, Object> recreateAPACShippingPackage (DispatchContext dctx, Map serviceContext)
    throws GenericEntityException, IOException {    //recreateAPACShippingPackage
        
        Delegator delegator = dctx.getDelegator();
        String trackingNumber = (String) serviceContext.get("trackingNumber");
        Map result = ServiceUtil.returnSuccess();
        
        try { //start try block
            GenericValue orderItemShipGroup = EntityUtil.getFirst(delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("trackingNumber", trackingNumber), null, false));
            GenericValue orderHeader = orderItemShipGroup.getRelatedOne("OrderHeader", false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = accountInfo(delegator, productStore);	//call function to get account information
            
            StringBuffer recreateAPACShippingPackageXML = new StringBuffer();	//building XML Request for confirmAPACShippingPackage
            recreateAPACShippingPackageXML.append(ePacketXML.recreateAPACShippingPackageRequestXML(mapAccount, trackingNumber));
            
            if (displayXmlLog()) {
                Debug.logError("recreateAPACShippingPackageRequestXML == START ==", module);
                Debug.logError(recreateAPACShippingPackageXML.toString(), module);	//Print XML to console.log
                Debug.logError("recreateAPACShippingPackageRequestXML == END ==", module);
            }
            
            //Send request XML to EUB server
            String responseXML = sendRequestXMLtoEpacket(recreateAPACShippingPackageXML.toString());
            
            if (displayXmlLog()) {
                Debug.logError("recreateAPACShippingPackageResponseXML == START ==", module);
                Debug.logError(responseXML, module);	//Print XML to console.log
                Debug.logError("recreateAPACShippingPackageResponseXML == END ==", module);
            }
            
            Map testResponseXMLmap = FastMap.newInstance();
            testResponseXMLmap = testResponseXML(responseXML);
            //String trackingNumber = null;
            if (testResponseXMLmap.get("Ack") == null || !testResponseXMLmap.get("Ack").equals("Success")) {
                //Debug.logError("Ack: " + testResponseXMLmap.get("Ack").toString() + " with message: " + testResponseXMLmap.get("Message").toString(), module);
                return ServiceUtil.returnError("ePacket Ack Response: \"" + testResponseXMLmap.get("Ack").toString() + "\" with message \"" + testResponseXMLmap.get("Message").toString() + "\"");
            }
            else {
                trackingNumber = responseXML.substring(responseXML.indexOf("<TrackCode>") + "<TrackCode>".length(), responseXML.indexOf("</TrackCode>"));
                result.put("trackingNumber", trackingNumber);
                
                GenericValue replacementOrder = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderHeader.getString("orderId") + "R"), false);
                if (UtilValidate.isNotEmpty(replacementOrder)) {
                    GenericValue orderItemShipGroupR = EntityUtil.getFirst(delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("orderId", orderHeader.getString("orderId") + "R"), null, false));
                    orderItemShipGroupR.put("trackingNumber", trackingNumber);
                    delegator.store(orderItemShipGroupR);
                }
            }   //if ack failure
        }   //end try block
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        return result;
    }   //recreateAPACShippingPackage
    
    public static Map<String, Object> cancelAPACShippingPackage (DispatchContext dctx, Map serviceContext)
    throws GenericEntityException, IOException {    //cancelAPACShippingPackage
        
        Delegator delegator = dctx.getDelegator();
        String trackingNumber = (String) serviceContext.get("trackingNumber");
        Map result = ServiceUtil.returnSuccess();
        
        try { //start try block
            GenericValue orderItemShipGroup = EntityUtil.getFirst(delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("trackingNumber", trackingNumber), null, false));
            GenericValue orderHeader = orderItemShipGroup.getRelatedOne("OrderHeader", false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = accountInfo(delegator, productStore);	//call function to get account information
            
            StringBuffer cancelAPACShippingPackageXML = new StringBuffer();	//building XML Request for confirmAPACShippingPackage
            cancelAPACShippingPackageXML.append(ePacketXML.cancelAPACShippingPackageRequestXML(mapAccount, trackingNumber));
            
            if (displayXmlLog()) {
                Debug.logError("cancelAPACShippingPackageRequestXML == START ==", module);
                Debug.logError(cancelAPACShippingPackageXML.toString(), module);	//Print XML to console.log
                Debug.logError("cancelAPACShippingPackageRequestXML == END ==", module);
            }
            
            //Send request XML to EUB server
            String responseXML = sendRequestXMLtoEpacket(cancelAPACShippingPackageXML.toString());
            
            if (displayXmlLog()) {
                Debug.logError("cancelAPACShippingPackageResponseXML == START ==", module);
                Debug.logError(responseXML, module);	//Print XML to console.log
                Debug.logError("cancelAPACShippingPackageResponseXML == END ==", module);
            }
            
            Map testResponseXMLmap = FastMap.newInstance();
            testResponseXMLmap = testResponseXML(responseXML);
            if (testResponseXMLmap.get("Ack") == null || !testResponseXMLmap.get("Ack").equals("Success")) {
                //Debug.logError("Ack: " + testResponseXMLmap.get("Ack").toString() + " with message: " + testResponseXMLmap.get("Message").toString(), module);
                result = ServiceUtil.returnError("ePacket Tracking Number " + trackingNumber + " returns Ack Response: \"" + testResponseXMLmap.get("Ack").toString() + "\" with message \"" + testResponseXMLmap.get("Message").toString() + "\"");
            }
            else {
                result = ServiceUtil.returnSuccess("ePacket Tracking Number " + trackingNumber + " returns Ack Response: \"" + testResponseXMLmap.get("Ack").toString() + "\" with message \"" + testResponseXMLmap.get("Message").toString() + "\"");
            }
            
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        return result;
    }   //cancelAPACShippingPackage
    
    public static Map<String, Object> getAPACShippingTrackCode (DispatchContext dctx, Map serviceContext)
    throws GenericEntityException, IOException {    //getAPACShippingTrackCode
        
        Delegator delegator = dctx.getDelegator();
        String orderId = (String) serviceContext.get("orderId");
        Map result = ServiceUtil.returnSuccess();
        String eBayItemId= null;
        String eBayTransactionId = null;
        
        try { //start try block
            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = accountInfo(delegator, productStore);	//call function to get account information
            
            List<GenericValue> orderItemAttributes = delegator.findByAnd("OrderItemAttribute", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", "00001"), null, false);
            for (GenericValue orderItemAttribute : orderItemAttributes) {
                if (orderItemAttribute.getString("attrName").equals("eBay Item Number")) {
                    eBayItemId = orderItemAttribute.getString("attrValue");
                }
                if (orderItemAttribute.getString("attrName").equals("EBAY_TRAN_ID")) {
                    eBayTransactionId = orderItemAttribute.getString("attrValue");
                }
            }
            
            StringBuffer getAPACShippingTrackCodeXML = new StringBuffer();	//building XML Request for confirmAPACShippingPackage
            getAPACShippingTrackCodeXML.append(ePacketXML.getAPACShippingTrackCodeRequestXML(mapAccount, eBayItemId, eBayTransactionId));
            
            if (displayXmlLog()) {
                Debug.logError("getAPACShippingTrackCodeRequestXML == START ==", module);
                Debug.logError(getAPACShippingTrackCodeXML.toString(), module);	//Print XML to console.log
                Debug.logError("getAPACShippingTrackCodeRequestXML == END ==", module);
            }
            
            //Send request XML to EUB server
            String responseXML = sendRequestXMLtoEpacket(getAPACShippingTrackCodeXML.toString());
            
            if (displayXmlLog()) {
                Debug.logError("getAPACShippingTrackCodeResponseXML == START ==", module);
                Debug.logError(responseXML, module);	//Print XML to console.log
                Debug.logError("getAPACShippingTrackCodeResponseXML == END ==", module);
            }
            
            Map testResponseXMLmap = FastMap.newInstance();
            testResponseXMLmap = testResponseXML(responseXML);
            String trackingNumber = null;
            if (testResponseXMLmap.get("Ack") == null || !testResponseXMLmap.get("Ack").equals("Success")) {
                //Debug.logError("Ack: " + testResponseXMLmap.get("Ack").toString() + " with message: " + testResponseXMLmap.get("Message").toString(), module);
                return ServiceUtil.returnError("ePacket Ack Response: \"" + testResponseXMLmap.get("Ack").toString() + "\" with message \"" + testResponseXMLmap.get("Message").toString() + "\"");
            }
            else {
                trackingNumber = responseXML.substring(responseXML.indexOf("<TrackCode>") + "<TrackCode>".length(), responseXML.indexOf("</TrackCode>"));
                result.put("trackingNumber", trackingNumber);
            }
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        return result;
    }   //getAPACShippingTrackCode


    private static String inputStreamToString(InputStream inputStream) throws IOException
    {   //inputStreamToString
        String string;
        StringBuilder outputBuilder = new StringBuilder();
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (null != (string = reader.readLine())) {
                outputBuilder.append(string).append('\n');
            }
        }
        return outputBuilder.toString();
    }   //inputStreamToString
    
    public static String convertToXML (String inputXML)
    {   //convertToXML
        SAXReader reader = new SAXReader();
        String xml = null;
        try {
            org.dom4j.Document document = reader.read(new StringReader(inputXML));
            xml = document.asXML();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }   //convertToXML
    
    public static Map<String, Object> testResponseXML (String inputXML)
    {   //testResponseXML
        Map result = FastMap.newInstance();
        String ack = inputXML.substring(inputXML.indexOf("<Ack>") + "<Ack>".length(), inputXML.indexOf("</Ack>"));
        result.put("Ack", ack);
        if (inputXML.contains("<Message>")) {
            String message = inputXML.substring(inputXML.indexOf("<Message>") + "<Message>".length(), inputXML.indexOf("</Message>"));
            result.put("Message", message);
        }
        else {
            result.put("Message", "No message");
        }	
        return result;
    }   //testResponseXML
    
    private static double getDeclaredValue(double totalShippedQty) {    //getDeclaredValue
        
        double result = 0.0;
        if (totalShippedQty == 1d) {
            result = 7;
        }
        else if (totalShippedQty == 2d) {
            result = 10;
        }
        else if (totalShippedQty == 3d) {
            result = 12.5;
        }
        else if (totalShippedQty == 4d) {
            result = 14;
        }
        else if (totalShippedQty > 4d) {
            result = 17;
        }
        /*double arr [] = new double [] {8d, 8.5d, 9d, 9.5d, 10d, 10.5d, 11d};
         int length = arr.length;
         double r = Math.random();
         int s = Double.valueOf(Math.floor(r *   Double.valueOf(length))).intValue(); */
        return result;
    }   //getDeclaredValue
    
    public static void printAPACShippingLabel (HttpServletRequest request, HttpServletResponse response)
	throws GenericEntityException, GenericServiceException, IOException {   //printAPACShippingLabel
        
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        Delegator delegator = (Delegator) request.getAttribute("delegator");
        String trackingNumber = (String) request.getParameter("trackingNumber");
        Map result = ServiceUtil.returnSuccess();
        
        try {
            GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "system"), false);
            result = dispatcher.runSync("getAPACShippingLabel", UtilMisc.toMap("trackingNumber", trackingNumber, "userLogin", userLogin));
        }
        catch (GenericServiceException e) {
            e.printStackTrace();
        }
        
        if(ServiceUtil.isSuccess(result)) { //if service success - START
            response.reset();
            response.setContentType("application/pdf");
            //response.setHeader("content-disposition", "attachment;filename=" + shipmentId + ".pdf");
            
            java.io.ByteArrayInputStream inputStream = (ByteArrayInputStream) result.get("shippingLabelByte");
            
            byte[]  buffer =new  byte[1444];
            OutputStream fs = null;
            try {
               	fs = response.getOutputStream();
                int bytesum=0;
                int byteread=0;
                while ((byteread=inputStream.read(buffer))!=-1)  {
                    bytesum+=byteread;
                    fs.write(buffer,0,byteread);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fs != null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }   //if service success - END
    }   //printAPACShippingLabel
    
    public static Map<String, Object> completePackEpacket (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {   //completePackEpacket
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String shipmentId = (String) context.get("shipmentId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map addAPACShippingPackageResult = FastMap.newInstance();
        
        try {   //main try -- START
            
            GenericValue shipment = delegator.findOne("Shipment", UtilMisc.toMap("shipmentId", shipmentId), false);
            String orderId = shipment.getString("primaryOrderId");
            String shipGroupSeqId = shipment.getString("primaryShipGroupSeqId");
            GenericValue orderItemShipGroup = delegator.findOne("OrderItemShipGroup", UtilMisc.toMap("orderId", orderId, "shipGroupSeqId", shipGroupSeqId), false);
            String shipmentMethodType = orderItemShipGroup.getString("shipmentMethodTypeId");
            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            String trackingNumber = orderItemShipGroup.getString("trackingNumber");
            
            if (shipmentMethodType.equals("EPACKET")) {   //run main code -- START
                if (UtilValidate.isEmpty(trackingNumber)) {  //if tracking number is empty -- START
                    addAPACShippingPackageResult = dispatcher.runSync("addAPACShippingPackage", UtilMisc.toMap("orderId", orderId, "userLogin", userLogin));
                    if (ServiceUtil.isSuccess(addAPACShippingPackageResult)) {
                        //Debug.logError(addAPACShippingPackageResult.get("trackingNumber").toString(), module);
                        trackingNumber = addAPACShippingPackageResult.get("trackingNumber").toString();
                    }
                    else {
                        Debug.logError("No tracking number in OrderItemShipGroup, addAPACShippingPackage service failed", module);
                        return ServiceUtil.returnError("No tracking number in OrderItemShipGroup, addAPACShippingPackage service failed");
                    }
                }   //if tracking number is empty -- END
                Map confirmAPACShippingPackageResult = dispatcher.runSync("confirmAPACShippingPackage", UtilMisc.toMap("trackingNumber", trackingNumber, "userLogin", userLogin));
                if (ServiceUtil.isSuccess(confirmAPACShippingPackageResult)) {	//if confirmAPACShipping package success -- START
                    //Debug.logError("confirmAPACShippingPackage successfully run", module);
                    Map saveTrackingNumberResult = dispatcher.runSync("saveTrackingNumber", UtilMisc.toMap("shipmentId", shipmentId, "trackingNumber", trackingNumber, "userLogin", userLogin));
                    if (ServiceUtil.isSuccess(saveTrackingNumberResult)) {
                        //Debug.logError("SaveTrackingNumber successfully run", module);
                        return ServiceUtil.returnSuccess();
                    }
                    else {
                        Debug.logError("SaveTrackingNumber failed", module);
                        return ServiceUtil.returnError("SaveTrackingNumber service failed");
                    }
                }   //if confirmAPACShipping package success -- END
                else {
                    Debug.logError("confirmAPACShippingPackage failed", module);
                    return ServiceUtil.returnError("confirmAPACShippingPackage service failed");
                }
            }   //run main code -- END
            return ServiceUtil.returnSuccess();
        }   //main try -- START
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (GenericServiceException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
    }   //completePackEpacket
    
    public static Map<String, Object> saveTrackingNumber(DispatchContext dctx, Map serviceContext) throws GenericEntityException
    {   //saveTrackingNumber
        
        Delegator delegator = dctx.getDelegator();
        String shipmentId = (String) serviceContext.get("shipmentId");
        String orderId = (String) serviceContext.get("orderId");
        String trackingNumber = (String) serviceContext.get("trackingNumber");
        
        if (UtilValidate.isEmpty(shipmentId) && UtilValidate.isEmpty(orderId)) {    //if both input is empty -- START
            return ServiceUtil.returnError("No ShipmentId or OrderId input found");
        }   //if both input is empty -- END
        else if (UtilValidate.isEmpty(shipmentId) && UtilValidate.isNotEmpty(orderId)) {    //if shipmentId is empty -- START
            try {
                List<GenericValue> orderItemShipGroups = delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("orderId", orderId), null, false);
                for(GenericValue orderItemShipGroup : orderItemShipGroups) {
                    String trackingNumberTemp = orderItemShipGroup.getString("trackingNumber");
                    if(trackingNumberTemp == null || "".equals(trackingNumberTemp.trim())) {
                        orderItemShipGroup.set("trackingNumber", trackingNumber);
                        orderItemShipGroup.store();
                    }
                }
            }
            catch (GenericEntityException e) {
                e.printStackTrace();
                //Debug.logError(e.getMessage(), module);
            }
        }   //if shipmentId is empty -- END
        else if (UtilValidate.isEmpty(orderId) && UtilValidate.isNotEmpty(shipmentId)) {    //if orderId is empty -- START
            try {
                List<GenericValue> shipmentPackageRouteSegments = delegator.findByAnd("ShipmentPackageRouteSeg", UtilMisc.toMap("shipmentId", shipmentId), null, false);
                for(GenericValue shipmentPackageRouteSegment : shipmentPackageRouteSegments) {
                    String trackingNumberTemp = shipmentPackageRouteSegment.getString("trackingCode");
                    if(trackingNumberTemp == null || "".equals(trackingNumberTemp.trim())) {
                        shipmentPackageRouteSegment.set("trackingCode", trackingNumber);
                        shipmentPackageRouteSegment.store();
                    }
                }
            }
            catch (GenericEntityException e) {
                e.printStackTrace();
            }
        }   //if orderId is empty -- END
        
        return ServiceUtil.returnSuccess();
    }   //saveTrackingNumber
    
    public static Map<String, Object> updateEpacketTrackingNumberToEbay (DispatchContext dctx, Map context)
    throws GenericEntityException { //updateEpacketTrackingNumberToEbay
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        
        /*Calendar fromDay = Calendar.getInstance();
        Calendar toDay = Calendar.getInstance();
        fromDay.set(Calendar.DATE, fromDay.get(Calendar.DATE) - 30);
        toDay.set(Calendar.DATE, toDay.get(Calendar.DATE));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        //Date resultDate = fromDay.getTime();
        //Debug.logError("FromDay is " + sdf.format(fromDay.getTime()).toString(), module);
        //Debug.logError("ToDay is " + sdf.format(toDay.getTime()).toString(), module);
        Timestamp fromDate = Timestamp.valueOf(sdf.format(fromDay.getTime()));
        Timestamp toDate = Timestamp.valueOf(sdf.format(toDay.getTime()));*/
        
        List<GenericValue> listViewEntityIt = delegator.findList("OrderShipEpacket", null, null, null, null, false);
        for (GenericValue listViewEntityItem : listViewEntityIt) {   //loop listViewEntityItem -- START
            if (UtilValidate.isNotEmpty(listViewEntityItem.get("orderId")) && (("EPACKET").equals(listViewEntityItem.get("shipmentMethodTypeId"))) && UtilValidate.isEmpty(listViewEntityItem.get("trackingNumber"))) {// || ("EMS").equals(listViewEntityItem.get("shipmentMethodTypeId")))) {
                try {
                    //main code -- START
                    String trackingNumber = null;
                    String orderId = listViewEntityItem.get("orderId").toString();
                    String maySplit = listViewEntityItem.get("maySplit").toString();
                    GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
                    Timestamp orderDate = orderHeader.getTimestamp("orderDate");
                    GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
                    GenericValue productStoreEbaySetting = productStore.getRelatedOne("ProductStoreEbaySetting", false);
                    int daysUpdateEpacket = Integer.valueOf(productStoreEbaySetting.getLong("daysUpdateEpacket").intValue());
                    String productStoreId = productStore.getString("productStoreId");
                    String earlyEpacketTrackNum = productStoreEbaySetting.getString("earlyEpacketTrackNum");
                    
                    Calendar fromDay = Calendar.getInstance();
                    fromDay.set(Calendar.DATE, fromDay.get(Calendar.DATE) - (daysUpdateEpacket - 2));
                    Timestamp fromDate = Timestamp.valueOf(sdf.format(fromDay.getTime()));
                    //Debug.logError("OrderID: "+ orderId + ", OrderDate is " + orderDate + ", fromDate is " + fromDate, module);
                    if (orderDate.before(fromDate)) {   //if orderDate before FromDate -- START
                        //GenericValue orderItemShipGroup = EntityUtil.getFirst(delegator.findByAnd("OrderItemShipGroup", UtilMisc.toMap("orderId", orderId)));
                        //String trackingNumberTemp = orderItemShipGroup.getString("TrackingNumber");
                        
                        if (earlyEpacketTrackNum.equals("Y")) { // && (trackingNumberTemp == null || "".equals(trackingNumberTemp.trim()))
                            //Get EUB Tracking number -- START
                            Map trackingNumberResult = dispatcher.runSync("addAPACShippingPackage", UtilMisc.toMap("orderId", orderId, "userLogin", userLogin));
                            if (ServiceUtil.isSuccess(trackingNumberResult) && !trackingNumberResult.get("trackingNumber").equals("ERROR")) {
                                trackingNumber = trackingNumberResult.get("trackingNumber").toString();
                                Debug.logError("Success, " + orderId + " : " + trackingNumber, module);
                            }
                            else {
                                Debug.logError("Error running addAPACShippingPackage for order ID " + orderId, module);
                                //check if orderID already has ePacket tracking number
                                
                                Map altResult = dispatcher.runSync("getAPACShippingTrackCode", UtilMisc.toMap("orderId", orderId, "userLogin", userLogin));
                                trackingNumber = altResult.get("trackingNumber").toString();
                                Debug.logError("Fail, trackingNumber is " + trackingNumber, module);
                            }
                            
                            //Get EUB Tracking number -- END
                            
                            Map saveTrackingNumberResult = dispatcher.runSync("saveTrackingNumber", UtilMisc.toMap("orderId", orderId, "trackingNumber", trackingNumber, "userLogin", userLogin));
                            if (ServiceUtil.isFailure(saveTrackingNumberResult)) {
                                Debug.logError("SaveTrackingNumber for orderID " + orderId + " failed", module);
                            }
                            
                            String eBayItemId = null;
                            String eBayTransactionId = null;
                            
                            List<GenericValue> orderItems = delegator.findByAnd("OrderItem", UtilMisc.toMap("orderId", orderId), null, false);
                            for (GenericValue orderItem : orderItems) { //loop orderItem -- START
                                String orderItemSeqId = orderItem.getString("orderItemSeqId");
                                List<GenericValue> orderItemAttributes = delegator.findByAnd("OrderItemAttribute", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId), null, false);
                                for (GenericValue orderItemAttribute : orderItemAttributes) { //loop orderItemAttribute -- START
                                    if (("eBay Item Number").equals(orderItemAttribute.getString("attrName"))) {
                                        eBayItemId = orderItemAttribute.getString("attrValue");
                                    }
                                    else if (("EBAY_TRAN_ID").equals(orderItemAttribute.getString("attrName"))) {
                                        eBayTransactionId = orderItemAttribute.getString("attrValue");
                                    }
                                }//loop orderItemAttribute -- END
                                //Run the completeSale to update Tracking number to eBay -- START
                                if (UtilValidate.isNotEmpty(orderItemAttributes)) { //if orderItemAttributes is not empty -- START
                                    Map completeSaleResult = dispatcher.runSync("TradingApiCompleteSale", UtilMisc.toMap(
                                                                                                                         "orderId", orderId,
                                                                                                                         "trackingNumber", trackingNumber,
                                                                                                                         "eBayItemId", eBayItemId,
                                                                                                                         "eBayTransactionId", eBayTransactionId,
                                                                                                                         "userLogin", userLogin));
                                    
                                    if (ServiceUtil.isFailure(completeSaleResult)) {    //if completeSaleResult failed -- START
                                        Debug.logError("Error running completeSale for order ID " + orderId + ", not updating tracking number to eBay", module);
                                    }   //if completeSaleResult failed -- END
                                    else {  //if completeSaleResult success -- START
                                        Map updateOrderAttribute = dispatcher.runSync("updateOrderAttribute", UtilMisc.toMap("orderId", orderId, "attrName", "TRACKING_UPDATE", "attrValue", "Y", "userLogin", userLogin));
                                    }   //if completeSaleResult success -- END
                                }   //if orderItemAttributes is not empty -- END
                                //Run the completeSale to update Tracking number to eBay -- END
                            }   //loop orderItem -- END
                        } //if earlyEpacketTrackNum block
                    }   //if orderDate before FromDate -- END
                    
                    //main code -- END
                } catch (Exception e) {
                    // Ignore
                }   //end of Try
            }
        }   //loop listViewEntityItem -- END
        return ServiceUtil.returnSuccess();
    }   //updateEpacketTrackingNumberToEbay
    
    
    
} //END class
