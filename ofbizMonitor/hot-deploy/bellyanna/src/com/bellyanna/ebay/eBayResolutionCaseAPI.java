package com.bellyanna.ebay;
//http://developer.ebay.com/DevZone/XML/docs/WebHelp/wwhelp/wwhimpl/js/html/wwhelp.htm?context=eBay_XML_API&topic=StandardData
//Reference: http://developer.ebay.com/DevZone/XML/docs/Reference/ebay/GetItem.html

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.List;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import org.dom4j.io.SAXReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.DynamicViewEntity;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceAuthException;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.service.ServiceValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.apache.commons.lang.StringEscapeUtils;

import com.bellyanna.ebay.common;
import com.bellyanna.ebay.requestXML;
import com.bellyanna.common.bellyannaService;

import javolution.util.FastMap;

public class eBayResolutionCaseAPI {
	private static final String module = eBayMerchandisingAPI.class.getName();
    private static final String eol = System.getProperty("line.separator");

    public static String sendRequestXMLToEbay(Map mapContent, String generatedXmlData) throws IOException
    {
        Properties properties = new Properties();
        properties.load(new FileInputStream("hot-deploy/bellyanna/config/eBay.properties"));
        String postItemsUrl = properties.getProperty("ResolutionCaseApi.url");
        String version = properties.getProperty("ResolutionCaseApi.version");
        
        Map<String, String> requestPropertyMap = FastMap.<String, String>newInstance();
        requestPropertyMap.put("X-EBAY-SOA-SERVICE-NAME", "ResolutionCaseManagementService");
        requestPropertyMap.put("X-EBAY-SOA-OPERATION-NAME", mapContent.get("callName").toString());
        requestPropertyMap.put("X-EBAY-SOA-SERVICE-VERSION", version);
        requestPropertyMap.put("X-EBAY-SOA-SECURITY-TOKEN", mapContent.get("token").toString());
        //requestPropertyMap.put("X-EBAY-SOA-GLOBAL-ID", mapContent.get("globalId").toString());
        requestPropertyMap.put("X-EBAY-SOA-REQUEST-DATA-FORMAT", "XML");
        
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
        //Debug.logError("connection response code: " + responseCode, module);
        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = connection.getInputStream();
            response = inputStreamToString(inputStream);
        } else {
            inputStream = connection.getErrorStream();
            response = inputStreamToString(inputStream);
        }
        
        return (response == null || "".equals(response.trim())) ? String.valueOf(responseCode) : response;
    }   //sendRequestXMLToEbay
    
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
    }
    
    public static String convertToXML (String inputXML)
    {
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
    }
    
    public static Map<String, Object> getUserCases (DispatchContext dctx, Map context)
    throws GenericEntityException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        //Map disputeResult = FastMap.newInstance();
        
        String productStoreId = (String) context.get("productStoreId");
        int lastXDays = (Integer) context.get("lastXDays");
        if (UtilValidate.isEmpty(lastXDays) || lastXDays == 0) {
            lastXDays = 60;
        }
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
        Timestamp todayTimestamp = Timestamp.valueOf(sdf.format(now.getTime()));
        
        Calendar fromDate = Calendar.getInstance();
        fromDate.set(Calendar.DATE, fromDate.get(Calendar.DATE) - lastXDays);
        Timestamp modTimeFrom = Timestamp.valueOf(sdf.format(fromDate.getTime()));
        //Debug.logError("Starts running getUserCases", module);
        try {   //main try -- START
            EntityCondition condition = null;
            if (UtilValidate.isNotEmpty(productStoreId) || productStoreId != null) {    //populate productStoreList -- START
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("productStoreId",EntityOperator.EQUALS ,productStoreId),
                                                                          EntityCondition.makeCondition("primaryStoreGroupId",EntityOperator.EQUALS ,"EBAY"),
                                                                          EntityCondition.makeCondition("storeName",EntityOperator.EQUALS ,"GUDAO")
                                                                          ));
            } else {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("primaryStoreGroupId",EntityOperator.EQUALS ,"EBAY"),
                                                                          EntityCondition.makeCondition("storeName",EntityOperator.EQUALS ,"GUDAO")
                                                                          ));
            }//populate productStoreList -- END
            
            List<GenericValue> productStoreList = delegator.findList("ProductStore", condition, null, null, null, false);
            for (GenericValue productStore : productStoreList) {    //loop productStoreList -- START
                int pageNumber = 1;
                Map mapAccount = FastMap.newInstance();
                
                String responseXML = getUserCasesResponseXML(delegator, productStore, modTimeFrom, pageNumber);
                //Debug.logError(responseXML, module);
                //Reading responseXML -- START
                Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                Element elemResponse = docResponse.getDocumentElement();
                String ack = UtilXml.childElementValue(elemResponse, "ack", "Failure");
                
                if (ack.equals("Success") || ack.equals("Warning")) {   //if ack success -- START
                    writeEbayCaseIntoDatabase(dctx, productStore, responseXML, userLogin);
                    
                    pageNumber++;
                    Element paginationOutput = UtilXml.firstChildElement(elemResponse, "paginationOutput");
                    int totalPages = Integer.parseInt(UtilXml.childElementValue(paginationOutput, "totalPages", "1").toString());
                    //Debug.logError("total pages is " + totalPages, module);
                    while (pageNumber <= totalPages) {  //loop totalPages -- START
                        responseXML = getUserCasesResponseXML(delegator, productStore, modTimeFrom, pageNumber);
                        Document responseDoc = UtilXml.readXmlDocument(responseXML, true);
                        Element responseElem = responseDoc.getDocumentElement();
                        String ack2 = UtilXml.childElementValue(responseElem, "ack", "Failure");
                        if (ack2.equals("Success") || ack2.equals("Warning")) { //if ack2 success -- START
                            writeEbayCaseIntoDatabase(dctx, productStore, responseXML, userLogin);
                            pageNumber++;
                        }   //if ack2 success -- END
                    }   //loop totalPages -- END
                }   //if ack success -- END
                else {  //if ack failure -- START
                    //disputeResult.put("hasDispute", "N");
                    List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                    Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                    StringBuffer errorMessage = new StringBuffer();
                    while (errorElementsElemIter.hasNext()) {   //loop errorMessage -- START
                        Element errorElement = errorElementsElemIter.next();
                        String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                        String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                        errorMessage.append(shortMessage + " - " + longMessage);
                        Debug.logError("GetEbayUserCases ResponseXML returns error: " + errorMessage, module);
                    }   //loop errorMessage -- END
                }   //if ack failure -- END
                //Reading responseXML -- END
            }   //loop productStoreList -- END
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //Debug.logError("Finished running getUserCases", module);
        return result;
    }   //getUserCases

    public static String getUserCasesResponseXML (Delegator delegator, GenericValue productStore, Timestamp modTimeFrom, int pageNumber)
    throws IOException {
        
        String responseXML = null;
        try {   //try -- START
            //Debug.logError("Processing page " + pageNumber, module);
            Map mapAccount = FastMap.newInstance();
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "getUserCases");
            
            //Building XML -- START
            Document rootDoc = UtilXml.makeEmptyXmlDocument("getUserCasesRequest");
            Element rootElem = rootDoc.getDocumentElement();
            rootElem.setAttribute("xmlns", "http://www.ebay.com/marketplace/resolution/v1/services");
            
            //RequesterCredentials -- START
            Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
            UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
            //RequesterCredentials -- END
            
            //Pagination -- START
            Element paginationElem = UtilXml.addChildElement(rootElem, "paginationInput", rootDoc);
            UtilXml.addChildElementValue(paginationElem, "entriesPerPage", "200", rootDoc);
            UtilXml.addChildElementValue(paginationElem, "pageNumber", pageNumber + "", rootDoc);
            //Pagination -- END
            
            //CaseTypeFilter -- START
            Element caseTypeFilterElem = UtilXml.addChildElement(rootElem, "caseTypeFilter", rootDoc);
            UtilXml.addChildElementValue(caseTypeFilterElem, "caseType", "EBP_INR", rootDoc);
            UtilXml.addChildElementValue(caseTypeFilterElem, "caseType", "EBP_SNAD", rootDoc);
            UtilXml.addChildElementValue(caseTypeFilterElem, "caseType", "INR", rootDoc);
            UtilXml.addChildElementValue(caseTypeFilterElem, "caseType", "PAYPAL_INR", rootDoc);
            UtilXml.addChildElementValue(caseTypeFilterElem, "caseType", "PAYPAL_SNAD", rootDoc);
            UtilXml.addChildElementValue(caseTypeFilterElem, "caseType", "RETURN", rootDoc);
            UtilXml.addChildElementValue(caseTypeFilterElem, "caseType", "SNAD", rootDoc);
            //CaseTypeFilter -- END
            
            //CreationDateRangeFilter -- START
            Element creationDateRangeFilterElem = UtilXml.addChildElement(rootElem, "creationDateRangeFilter", rootDoc);
            UtilXml.addChildElementValue(creationDateRangeFilterElem, "fromDate", bellyannaService.timestampToEbayDate(modTimeFrom), rootDoc);
            //CreationDateRangeFilter -- END
            
            //Building XML -- END
            String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
            //Debug.logError(requestXMLcode, module);
            
            responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode.toString());
            //Debug.logError(responseXML, module);
        }   //try -- END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return responseXML;
    }   //getUserCasesResponseXML
    
    public static Map<String, Object> writeEbayCaseIntoDatabase(DispatchContext dctx, GenericValue productStore, String responseXML, GenericValue userLogin) {
        
        Map result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        
        try {   //main try == START
            Document responseDoc = UtilXml.readXmlDocument(responseXML, true);
            Element responseElem = responseDoc.getDocumentElement();
            String ack = UtilXml.childElementValue(responseElem, "ack", "Failure");
            if (ack.equals("Success") || ack.equals("Warning")) { //if ack success -- START
                Element cases = UtilXml.firstChildElement(responseElem, "cases");
                List<? extends Element> caseSummaries = UtilXml.childElementList(cases, "caseSummary");
                Iterator<? extends Element> caseSummariesIterator = caseSummaries.iterator();
                while (caseSummariesIterator.hasNext()) {  //loop caseSummariesIterator -- START
                    Element caseSummary = caseSummariesIterator.next();
                    
                    Element caseIdElem = UtilXml.firstChildElement(caseSummary, "caseId");
                    String caseId = UtilXml.childElementValue(caseIdElem, "id");
                    String caseType = UtilXml.childElementValue(caseIdElem, "type");
                    
                    Element otherParty = UtilXml.firstChildElement(caseSummary, "otherParty");
                    String buyerId = UtilXml.childElementValue(otherParty, "userId");
                    
                    BigDecimal caseAmount = new BigDecimal(UtilXml.childElementValue(caseSummary, "caseAmount", null));
                    String caseCurrency = UtilXml.childElementAttribute(caseSummary, "caseAmount", "currencyId", null);
                    long caseQuantity = Long.parseLong(UtilXml.childElementValue(caseSummary, "caseQuantity", null));
                    String creationDate = UtilXml.childElementValue(caseSummary, "creationDate", null);
                    String lastModifiedDate = UtilXml.childElementValue(caseSummary, "lastModifiedDate", null);
                    String respondByDate = UtilXml.childElementValue(caseSummary, "respondByDate", null);
                    
                    Element itemElem = UtilXml.firstChildElement(caseSummary, "item");
                    String itemId = UtilXml.childElementValue(itemElem, "itemId");
                    String itemTitle = UtilXml.childElementValue(itemElem, "itemTitle");
                    String transactionId = UtilXml.childElementValue(itemElem, "transactionId");
                    
                    Element statusElem = UtilXml.firstChildElement(caseSummary, "status");
                    Element statusChildElem = UtilXml.firstChildElement(statusElem);
                    String statusId = UtilXml.elementValue(statusChildElem);
                    //Debug.logError("caseId: " + caseId + ", caseType: " + caseType + ", buyerId: " + buyerId, module);
                    GenericValue caseGV = delegator.makeValue("EbayCases", UtilMisc.toMap("caseId", caseId, "productStoreId", productStore.getString("productStoreId")));
                    caseGV.set("ebayUserId", buyerId);
                    caseGV.set("caseType", caseType);
                    caseGV.set("caseQuantity", caseQuantity);
                    caseGV.set("caseAmount", caseAmount);
                    caseGV.set("caseCurrency", caseCurrency);
                    caseGV.set("creationDate", creationDate);
                    caseGV.set("itemId", itemId);
                    caseGV.set("itemTitle", itemTitle);
                    caseGV.set("transactionId", transactionId);
                    caseGV.set("lastModifiedDate", lastModifiedDate);
                    caseGV.set("respondByDate", respondByDate);
                    caseGV.set("statusId", statusId);
                    delegator.createOrStore(caseGV);
                    
                    //autoRefund == START
                    /*double usdToGbp = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "GBP"), null, false)).getDouble("conversionFactor");
                    double usdToAud = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "AUD"), null, false)).getDouble("conversionFactor");
                    double usdToCad = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CAD"), null, false)).getDouble("conversionFactor");
                    double usdToEur = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "EUR"), null, false)).getDouble("conversionFactor");
                    */
                    /*String site = null;
                    List<GenericValue> ebayActiveListing = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"), "itemId", itemId), null, false);
                    if (UtilValidate.isNotEmpty(ebayActiveListing)) {
                        site = EntityUtil.getFirst(ebayActiveListing).getString("site");
                    }*/
                    
                    boolean refunded = false;
                    GenericValue ebayCaseRefund = delegator.findOne("EbayCaseRefund", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"), "caseId", caseId), false);
                    if (UtilValidate.isNotEmpty(ebayCaseRefund)) {
                        refunded = true;
                    }
                    
                    if (!refunded) { //check if refund has been tried before == START
                        if (statusId.equals("OTHER_PARTY_CONTACTED_CS_AWAITING_RESPONSE")) {    //check if case has been escalated == START
                            /*if (!site.equals("Germany")) {  //dont run if case is from DE listing == START
                                double caseAmountDB = caseAmount.doubleValue();
                                double caseAmountComparator = 0.0;
                                if (caseCurrency.equals("USD")) {
                                    caseAmountComparator = 30.0;
                                } else if (caseCurrency.equals("GBP")) {
                                    caseAmountComparator = 30.0 * usdToGbp;
                                } else if (caseCurrency.equals("AUD")) {
                                    caseAmountComparator = 30.0 * usdToAud;
                                } else if (caseCurrency.equals("CAD")) {
                                    caseAmountComparator = 30.0 * usdToCad;
                                } else if (caseCurrency.equals("EUR")) {
                                    caseAmountComparator = 30.0 * usdToEur;
                                }
                                
                                if (caseAmountDB <= caseAmountComparator) {  //if caseAmount is below 30 USD or equivalent == START
                                    Map issueRefund = dispatcher.runSync("ResolutionCaseApiIssueRefund", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"), "caseId", caseId, "userLogin", userLogin));
                                }   //if caseAmount is below 30 USD or equivalent == END
                            }   //dont run if case is from DE listing == END
                            */
                            Map issueRefund = dispatcher.runSync("ResolutionCaseApiIssueRefund", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"), "caseId", caseId, "userLogin", userLogin));
                        }   //check if case has been escalated == END
                    }   //check if refund has been tried before == END
                    //autoRefund == END
                }   //loop caseSummariesIterator -- END
            }   //if ack success -- END
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
        
    }
    
    public static Map<String, Object> issueRefund (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        
        String productStoreId = (String) context.get("productStoreId");
        String caseId = (String) context.get("caseId");
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Timestamp todayTimestamp = Timestamp.valueOf(sdf.format(now.getTime()));

        try {   //main try == START
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            GenericValue ebayCase = delegator.findOne("EbayCases", UtilMisc.toMap("productStoreId", productStoreId, "caseId", caseId), false);
            String caseType = ebayCase.getString("caseType");
            String caseCurrency = ebayCase.getString("caseCurrency");
            String caseAmount = ebayCase.getString("caseAmount");
            String ebayUserId = ebayCase.getString("ebayUserId");
            String itemId = ebayCase.getString("itemId");
            
            if(caseType.equals("EBP_INR") || caseType.equals("EBP_SNAD")) { //ebay resolution case API only support this case type == START
                /*Map mapAccount = FastMap.newInstance();
                mapAccount = common.accountInfo(delegator, productStore);
                mapAccount.put("callName", "issueFullRefund");
                
                //Building XML -- START
                Document rootDoc = UtilXml.makeEmptyXmlDocument("issueFullRefundRequest");
                Element rootElem = rootDoc.getDocumentElement();
                rootElem.setAttribute("xmlns", "http://www.ebay.com/marketplace/resolution/v1/services");
                
                //RequesterCredentials -- START
                Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                //RequesterCredentials -- END
                
                
                Element caseIdElem = UtilXml.addChildElement(rootElem, "caseId", rootDoc);
                UtilXml.addChildElementValue(caseIdElem, "id", caseId, rootDoc);
                UtilXml.addChildElementValue(caseIdElem, "type", caseType, rootDoc);
                
                UtilXml.addChildElementValue(rootElem, "comments", "Your refund is on the way. Please check your PayPal account", rootDoc);
                
                //Building XML -- END
                String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                //Debug.logError(requestXMLcode, module);
                
                String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode.toString());
                //Debug.logError(responseXML, module);
                
                //Read responseXML == START
                Document responseDoc = UtilXml.readXmlDocument(responseXML, true);
                Element responseElem = responseDoc.getDocumentElement();
                String ack = UtilXml.childElementValue(responseElem, "ack", "Failure");
                */
                String ack = "Success";
                GenericValue ebayCaseRefund = delegator.makeValue("EbayCaseRefund", UtilMisc.toMap("productStoreId", productStoreId, "caseId", caseId));
                
                if (ack.equals("Success") || ack.equals("Warning")) {   //if ack success == START
                    //Debug.logError("this runs 1", module);
                    //String fullRefundStatus = UtilXml.childElementValue(responseElem, "fullRefundStatus", null);
                    String fullRefundStatus = "AUTO_REFUND_IS_OFF";
                    ebayCaseRefund.set("statusId", fullRefundStatus);
                    ebayCaseRefund.set("refundDate", todayTimestamp);
                    delegator.createOrStore(ebayCaseRefund);
                }   //if ack success == END
                /*else {
                    //Debug.logError("this runs 2", module);
                    StringBuffer errorNotes = new StringBuffer();
                    Element errorMessage = UtilXml.firstChildElement(responseElem, "errorMessage");
                    List<? extends Element> errorList = UtilXml.childElementList(errorMessage, "error");
                    Iterator<? extends Element> errorIterator = errorList.iterator();
                    while (errorIterator.hasNext()) {   //while errorElement -- START
                        Element errorElement = errorIterator.next();
                        String shortMessage = UtilXml.childElementValue(errorElement, "message", null);
                        //Debug.logError("message: " + shortMessage, module);
                        errorNotes.append(shortMessage + ". ");
                    }   //while errorElement -- END
                    ebayCaseRefund.set("statusId", "ACK_FAILURE");
                    ebayCaseRefund.set("notes", errorNotes.toString());
                    delegator.createOrStore(ebayCaseRefund);
                }*/
                //Read responseXML == END
                
                //send email == START
                Properties properties = new Properties();
                properties.load(new FileInputStream("hot-deploy/gudao/config/case.properties"));
                String sendEmailToProp = properties.getProperty("sendEmailTo");
                String sendCc = properties.getProperty("sendCc");
                String sendBcc = properties.getProperty("sendBcc");
                String subject = productStoreId + " - " + caseId + " has been Escalated";
                String body = "Account : " + productStoreId + "   " + "case ID : " + caseId + "   " + "TYPE : " + caseType + "   " + "Amount : " + caseCurrency + " " + caseAmount + "   " + "eBay user ID: " + ebayUserId + "   " + "Item ID: " + itemId;
                boolean startTlsEnabled = true;
                
                String[] sendEmailTo = sendEmailToProp.split(",");
                for (int k = 0; k < sendEmailTo.length; k++) {   //loop sending email == START
                    Map sendEmail = dispatcher.runSync("sendMail", UtilMisc.toMap("sendFrom", "898515425@qq.com",
                                                                                  "sendTo", sendEmailTo[k].trim(),
                                                                                  "sendCc", sendCc,
                                                                                  "sendBcc", sendBcc,
                                                                                  "socketFactoryPort", "465",
                                                                                  "startTLSEnabled", startTlsEnabled,
                                                                                  "subject", subject,
                                                                                  "body", body,
                                                                                  "userLogin", userLogin));
                }   //loop sending email == END
                //send email == END
            }   //ebay resolution case API only support this case type == END
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
}
