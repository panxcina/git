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
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.List;
import java.util.LinkedList;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javolution.util.FastList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
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
import org.ofbiz.service.calendar.RecurrenceRule;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ModelService;
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

public class eBayTradingAPI {
	private static final String module = eBayTradingAPI.class.getName();
    private static final String eol = System.getProperty("line.separator");
    
    public static Map<String, String> ebayProperties ()
    throws IOException {
        
        Map<String, String> mapContent = FastMap.newInstance();
        try {   //main try -- START
            Properties properties = new Properties();
            properties.load(new FileInputStream("hot-deploy/bellyanna/config/eBay.properties"));
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
    
    public static String sendRequestXMLToEbay(Map mapContent, String generatedXmlData)
    throws IOException
    {   //sendRequestXMLToEbay
        Properties properties = new Properties();
        properties.load(new FileInputStream("hot-deploy/bellyanna/config/eBay.properties"));
        String postItemsUrl = properties.getProperty("eBay.url");
        String WSDLVersion = properties.getProperty("eBay.WSDLVersion");
        
        Map<String, String> requestPropertyMap = FastMap.<String, String>newInstance();
        requestPropertyMap.put("X-EBAY-API-COMPATIBILITY-LEVEL", WSDLVersion);
        if (mapContent.get("callName").equals("GetSessionID") ||
			mapContent.get("callName").equals("FetchToken") ||
			mapContent.get("callName").equals("GetTokenStatus") ||
			mapContent.get("callName").equals("RevokeToken")) {
            requestPropertyMap.put("X-EBAY-API-DEV-NAME", mapContent.get("devId").toString());
        }
        if (mapContent.get("callName").equals("FetchToken")) {
            requestPropertyMap.put("X-EBAY-API-APP-NAME", mapContent.get("appId").toString());
            requestPropertyMap.put("X-EBAY-API-CERT-NAME", mapContent.get("certId").toString());
        }
        requestPropertyMap.put("X-EBAY-API-CALL-NAME", mapContent.get("callName").toString());
        requestPropertyMap.put("X-EBAY-API-SITEID", mapContent.get("siteId").toString());
        requestPropertyMap.put("Content-Length", generatedXmlData.getBytes().length + "");
        requestPropertyMap.put("Content-Type", "text/xml");
        
        HttpURLConnection connection = (HttpURLConnection) (new URL(postItemsUrl)).openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(60000);
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
            response = common.inputStreamToString(inputStream);
        } else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
            do {
                Debug.logError("connection Time out, repost HTTP request", module);
                HttpURLConnection newConnection = (HttpURLConnection) (new URL(postItemsUrl)).openConnection();
                newConnection.setRequestMethod("POST");
                newConnection.setConnectTimeout(60000);
                newConnection.setDoInput(true);
                newConnection.setDoOutput(true);
                
                if(requestPropertyMap != null && !requestPropertyMap.isEmpty()) {
                    Iterator<String> keyIt = requestPropertyMap.keySet().iterator();
                    while(keyIt.hasNext()) {
                        String key = keyIt.next();
                        String value = requestPropertyMap.get(key);
                        newConnection.setRequestProperty(key, value);
                    }
                }
                
                OutputStream newOutputStream = newConnection.getOutputStream();
                newOutputStream.write(generatedXmlData.toString().getBytes());
                newOutputStream.flush();
                
                responseCode = newConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = newConnection.getInputStream();
                    response = common.inputStreamToString(inputStream);
                }
                
            }
            while (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT);
        } else {
            inputStream = connection.getErrorStream();
            response = common.inputStreamToString(inputStream);
        }
        
        return (response == null || "".equals(response.trim())) ? String.valueOf(responseCode) : response;
    }   //sendRequestXMLToEbay
	
    public static String getOrdersRequestXML(Map mapContent)
    throws Exception {  //getOrdersRequestXML
        
        String requestXML = null;
        try {   //main try -- START
            //Building XML -- START
            Document rootDoc = UtilXml.makeEmptyXmlDocument("GetOrdersRequest");
            Element rootElem = rootDoc.getDocumentElement();
            rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            
            //RequesterCredentials -- START
            Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
            UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapContent.get("token").toString(), rootDoc);
            //RequesterCredentials -- END
            
            UtilXml.addChildElementValue(rootElem, "IncludeFinalValueFee", "true", rootDoc);
            UtilXml.addChildElementValue(rootElem, "NumberOfDays", ((Integer) mapContent.get("lastXDays")) + 1 + "", rootDoc);
            UtilXml.addChildElementValue(rootElem, "OrderRole", "Seller", rootDoc);
            UtilXml.addChildElementValue(rootElem, "OrderStatus", "Completed", rootDoc);
            UtilXml.addChildElementValue(rootElem, "DetailLevel", "ReturnAll", rootDoc);
            
            Element paginationElem = UtilXml.addChildElement(rootElem, "Pagination", rootDoc);
            UtilXml.addChildElementValue(paginationElem, "EntriesPerPage", "100", rootDoc);
            UtilXml.addChildElementValue(paginationElem, "PageNumber", mapContent.get("pageNumber").toString(), rootDoc);
            //Building XML -- END
            
            requestXML = UtilXml.writeXmlDocument(rootDoc);
        }   //main try -- END
        catch (Exception e) {
            Debug.logError("Exception during read response from Ebay", module);
            e.printStackTrace();
            //return ServiceUtil.returnError(e.getMessage());
        }
        return requestXML;
    }   //getOrdersRequestXML
    
    public static Map<String, Object> updateEbayActiveListing(DispatchContext dctx, Map context)	//Description
    throws GenericEntityException, GenericServiceException {    //updateEbayActiveListing
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        //String productStoreId = (String) context.get("productStoreId");
        String productStoreGroup = (String) context.get("productStoreGroup");
        String productStoreId = (String) context.get("productStoreId");
        String pageNumber = (String) context.get("pageNumber");
        String removeExistingStr = (String) context.get("removeExisting");
        String continuousModeStr = (String) context.get("continuousMode");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        boolean removeExisting = false;
        boolean continuousMode = false;
        if (removeExistingStr.equals("Y") || removeExistingStr.equals("y")) {
            removeExisting = true;
        }
        if (continuousModeStr.equals("Y") || continuousModeStr.equals("y")) {
            continuousMode = true;
        }
        //result.put("productStoreId", productStoreId);
        result.put("productStoreGroup", productStoreGroup);
        result.put("productStoreId", productStoreId);
        try {
            List<GenericValue> productStoreList = null;
            if (productStoreGroup != null) {
                productStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("subtitle", productStoreGroup), null, false);
            }
            else if (productStoreId != null) {
                productStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), null, false);
            }
            for (GenericValue productStore : productStoreList) {
                productStoreId = productStore.getString("productStoreId");
                if (removeExisting) {
                    //delegator.removeByAnd("ActiveListingItemSpecific", UtilMisc.toMap("productStoreId", productStoreId));
                    //purge ListingVariationSpecifics entity by productStoreId
                    delegator.removeByAnd("ListingVariationSpecifics", UtilMisc.toMap("productStoreId", productStoreId));
                    //purge ActiveListingShipping entity by productStoreId
                    delegator.removeByAnd("ActiveListingShipping", UtilMisc.toMap("productStoreId", productStoreId));
                    //purge EbayActiveListingVariation entity by productStoreId
                    delegator.removeByAnd("EbayActiveListingVariation", UtilMisc.toMap("productStoreId", productStoreId));
                    //purge getSellerListData entity by productStoreId
                    delegator.removeByAnd("EbayActiveListing", UtilMisc.toMap("productStoreId", productStoreId));
                    //delegator.removeByAnd("VariationPictureSpecific", UtilMisc.toMap("productStoreId", productStoreId));
                    //delegator.removeByAnd("VariationSpecificsSet", UtilMisc.toMap("productStoreId", productStoreId));
                    
                    //delegator.removeByAnd("ActiveListingPicture", UtilMisc.toMap("productStoreId", productStoreId));
                    //delegator.removeByAnd("ListingCrossBorderTrade", UtilMisc.toMap("productStoreId", productStoreId));
                    //delegator.removeByAnd("ListingEnhancement", UtilMisc.toMap("productStoreId", productStoreId));
                    //delegator.removeByAnd("ListingPaymentMethods", UtilMisc.toMap("productStoreId", productStoreId));
                }
                Map getSellerList = dispatcher.runSync("TradingApiGetSellerList", UtilMisc.toMap("productStoreId", productStoreId, "pageNumber", pageNumber, "userLogin", userLogin));
            }
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }catch (GenericServiceException e) {
            e.printStackTrace();
            Debug.logError(productStoreId + ": return GenericServiceException: " + e.getMessage(), module);
            Map verifyUpdateListing = dispatcher.runSync("TradingApiVerifyUpdateListing", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
            //return ServiceUtil.returnError(e.getMessage());
        }
        if (productStoreGroup != null) {
            Debug.logError(productStoreGroup + ": finished running updateEbayActiveListing", module);
            Map verifyUpdateListing = dispatcher.runSync("TradingApiVerifyUpdateListing", UtilMisc.toMap("productStoreGroup", productStoreGroup, "userLogin", userLogin));
        }
        else {
            Debug.logError(productStoreId + ": finished running updateEbayActiveListing", module);
            Map verifyUpdateListing = dispatcher.runSync("TradingApiVerifyUpdateListing", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
        }
        
        if (continuousMode) {
            dispatcher.runSync("autoScheduleJob", UtilMisc.toMap("serviceName", "updateEbayActiveListing", "userLogin", userLogin));
        }
        
        return result;
    }   //updateEbayActiveListing
    
    public static Map<String, Object> verifyUpdateListing(DispatchContext dctx, Map context)	//Description
    throws GenericEntityException, GenericServiceException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        //String productStoreId = (String) context.get("productStoreId");
        String productStoreGroup = (String) context.get("productStoreGroup");
        String productStoreId = (String) context.get("productStoreId");
        //result.put("productStoreGroup", productStoreGroup);
        
        
        try {
            List<GenericValue> productStoreList = null;
            if (productStoreGroup != null) {
                Debug.logError("Running verifyUpdateListing for " + productStoreGroup, module);
                productStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("subtitle", productStoreGroup), null, false);
            }
            else if (productStoreId != null) {
                Debug.logError("Running verifyUpdateListing for " + productStoreId, module);
                productStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), null, false);
            }

            for (GenericValue productStore : productStoreList) {    //loop productStoreList -- START
                productStoreId = productStore.getString("productStoreId");
                GenericValue updateListingStatus = delegator.findOne("UpdateListingStatus", UtilMisc.toMap("productStoreId", productStoreId), false);
                String hasMoreItems = updateListingStatus.getString("hasMoreItems");
                if (hasMoreItems.toUpperCase().equals("TRUE")) {
                    String pageNumber = (Integer.parseInt(updateListingStatus.getString("lastPageNumber")) + 1) + "";
                    Map getSellerList = dispatcher.runSync("updateEbayActiveListing", UtilMisc.toMap("productStoreId", productStoreId, "pageNumber", pageNumber, "removeExisting", "N", "userLogin", userLogin));
                }
                /*else if (hasMoreItems.toUpperCase().equals("FALSE") && updateListingStatus.getString("lastPageNumber").equals("1")) {
                    List<GenericValue> ebayActiveListing = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("productStoreId", productStoreId), null, false);
                    if (UtilValidate.isEmpty(ebayActiveListing)) {
                        Map getSellerList = dispatcher.runSync("updateEbayActiveListing", UtilMisc.toMap("productStoreId", productStoreId, "pageNumber", "1", "removeExisting", "N", "userLogin", userLogin));
                    }
                }*/
            }   //loop productStoreList -- MAIN

        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }catch (GenericServiceException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        if (productStoreGroup != null) {
            Debug.logError(productStoreGroup + ": finished running verifyUpdateListing", module);
        }
        else {
            Debug.logError(productStoreId + ": finished running verifyUpdateListing", module);
        }
        return result;
        
        
    }   //verifyUpdateListing
    
    public static Map<String, Object> getSellerList(DispatchContext dctx, Map context)
    throws GenericEntityException, IOException {    //newGetSellerList
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        String productStoreId = (String) context.get("productStoreId");
        String pageNumberStr = (String) context.get("pageNumber");
        result.put("productStoreId", productStoreId);
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(now.getTime());
        
        Map<String, String> ebayProps = FastMap.newInstance();
        ebayProps = ebayProperties(); //Load Properties file
        GenericValue productStore = null;
        try {   //try creating requestXML -- START
            productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
        }   //try creating requestXML -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
        int pageNumber = Integer.parseInt(pageNumberStr);
        boolean hasMoreItemCheck = true;
        
        Map mapAccount = FastMap.newInstance();
        mapAccount = common.accountInfo(delegator, productStore);
        mapAccount.put("callName", "GetSellerList");
        mapAccount.put("productStoreId", productStoreId);
        Debug.logError(productStoreId + ": starting getSellerList", module);
        do {    //loop sending per pageNumber -- START
            mapAccount.put("pageNumber", pageNumber);
            boolean loopSendingXML = false;
            String responseXML = null;
            Document docResponse = null;
            Element elemResponse = null;
            String ack = null;
            do {    //loop sendingXML -- START
                //Debug.logError(productStoreId + ": loopSending pageNumber " + pageNumber, module);
                loopSendingXML = false;
                try {   //sending requestXML -- START
                    String requestXMLcode = requestXML.getSellerListRequestXML(mapAccount);
                    //Debug.logError(requestXMLcode,module);
                    responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                    //Debug.logError(responseXML,module);
                    
                    docResponse = UtilXml.readXmlDocument(responseXML, true);
                    elemResponse = docResponse.getDocumentElement();
                    ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                    if (ack.equals("Success") || ack.equals("Warning")) {
                        loopSendingXML = false;
                        try {
                            Debug.logError(productStoreId + ": writing data pageNumber " + pageNumber, module);
                            Map writeIntoGetSellerList = writeIntoGetSellerListData(dctx, mapAccount, responseXML);
                            String writeResult = writeIntoGetSellerList.get("writeResult").toString();
                            if (writeResult.equals("SUCCESS")) {
                                pageNumber++;
                                loopSendingXML = false;
                            } else if (writeResult.equals("ERROR")) {
                                loopSendingXML = true;
                            }
                        }
                        catch (Exception e) {
                            loopSendingXML = true;
                        }
                        
                    } else {
                        Debug.logError(productStoreId + ": loop failed, resending request pageNumber " + pageNumber, module);
                        loopSendingXML = true;
                    }
                }   //sending requestXML -- END
                catch (Exception e) {
                    Debug.logError(productStoreId + ": loop failed, resending request pageNumber " + pageNumber, module);
                    loopSendingXML = true;
                }
                            }   //loop sendingXML -- END
            while (loopSendingXML);
            
            String hasMoreItems = UtilXml.childElementValue(elemResponse, "HasMoreItems", null);
            //result.put("hasMoreItems", hasMoreItems.toUpperCase());
            if (hasMoreItems.toUpperCase().equals("FALSE")) {
                hasMoreItemCheck = false;
            }
            
            
        }   //loop sending per pageNumber -- END
        while (hasMoreItemCheck);
        Debug.logError(productStoreId + ": finished getSellerList", module);
        
        
        return result;
        
    }   //newGetSellerList
	
    public static Map<String, Object> getSellerListOld(DispatchContext dctx, Map context)
    throws GenericEntityException, IOException {    //getSellerList
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        String productStoreId = (String) context.get("productStoreId");
        String pageNumberStr = (String) context.get("pageNumber");
        result.put("productStoreId", productStoreId);
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(now.getTime());
        
        Map<String, String> ebayProps = FastMap.newInstance();
        Map mapAccount = FastMap.newInstance();
        //int pageNumber = 1;
        //if (pageNumberStr != null) {
        int pageNumber = Integer.parseInt(pageNumberStr);
        //}
        
        int totalNumberOfPages = 0;
        Debug.logError(productStoreId + ": starting getSellerList", module);
        try {
            ebayProps = ebayProperties(); //Load Properties file
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "GetSellerList");
            mapAccount.put("pageNumber", pageNumber);
            mapAccount.put("productStoreId", productStoreId);
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        String requestXMLcode = requestXML.getSellerListRequestXML(mapAccount);
        //Debug.logError(requestXMLcode,module);
        String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
        //Debug.logError(responseXML,module);
        
        String writeXMLtoFile = ebayProps.get("WriteXMLtoFile");
        if (writeXMLtoFile.equals("Y")) {
            File f = new File("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/requestXML/GetSellerList-" + productStoreId + "-" + today + ".xml");
            if(f.exists() && f.isFile()) {
                f.delete();
            }
            FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/requestXML/GetSellerList-" + productStoreId + "-" + today + ".xml", true);
            f1.write(requestXMLcode.toString());
            f1.close();
        }
        
        try {   //main try -- START
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
            
            if (ack.equals("Success")) {    //if ack success -- START
                List<? extends Element> paginationResult = UtilXml.childElementList(elemResponse, "PaginationResult");
                Iterator<? extends Element> paginationResultElemIter = paginationResult.iterator();
                while (paginationResultElemIter.hasNext()) {    //loop paginationResultElemIter -- START
                    Element paginationResultElement = paginationResultElemIter.next();
                    totalNumberOfPages = Integer.valueOf(UtilXml.childElementValue(paginationResultElement, "TotalNumberOfPages", "0"));
                    
                    for (pageNumber = pageNumber; pageNumber <= totalNumberOfPages; pageNumber++) {    //loop pageNumber -- START
                        mapAccount.put("pageNumber", pageNumber);
                        mapAccount.put("userLogin", userLogin);
                        Debug.logError(productStoreId + ": processing pageNumber " + pageNumber, module);
                        requestXMLcode = requestXML.getSellerListRequestXML(mapAccount);
                        //Debug.logError(requestXMLcode, module);
                        responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode.toString());
                        writeIntoGetSellerListData(dctx, mapAccount, responseXML);
                        
                        //Write responseXML to directories -- START
                        writeXMLtoFile = ebayProps.get("WriteXMLtoFile");
                        if (writeXMLtoFile.equals("Y")) {
                            File f = new File("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/GetSellerList-" + productStoreId + "-pg" + pageNumber + "-" + today + ".xml");
                            if(f.exists() && f.isFile()) {
                                f.delete();
                            }
                            FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/GetSellerList-" + productStoreId + "-pg" + pageNumber + "-" + today + ".xml", true);
                            f1.write(responseXML.toString());
                            f1.close();
                        }
                        //Write responseXML to directories -- END
                    }   //loop pageNumber -- END
                }   //loop paginationResultElemIter -- END
            }   //if ack success -- END
            else {  //if ack failure -- START
                docResponse = UtilXml.readXmlDocument(responseXML, true);
                elemResponse = docResponse.getDocumentElement();
                ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                
                if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                    List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                    Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                    StringBuffer errorMessage = new StringBuffer();
                    while (errorElementsElemIter.hasNext()) {
                        Element errorElement = errorElementsElemIter.next();
                        String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                        String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                        errorMessage.append(shortMessage + " - " + longMessage);
                        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/GetSellerListError.log", true);
                        f1.write(today + ": product Store ID: " + productStoreId + ", pageNumber " + pageNumber + ": " + errorMessage + "\n");
                        f1.close();
                    }
                    //result = ServiceUtil.returnError(errorMessage.toString());
                    return ServiceUtil.returnError(mapAccount.get("callName")+ " ResponseXML returns Ack Failure: " + errorMessage);
                }   //if ack failure -- END
            }   //if ack failure -- END
        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
            return ServiceUtil.returnError("getSellerList TRY CATCH error");
        }
        Debug.logError(productStoreId + ": finished running getSellerList", module);

        return result;
    }   //getSellerList
    
    public static Map<String, Object> writeIntoGetSellerListData(DispatchContext dctx, Map mapContent, String responseXML)
	throws GenericEntityException, GenericServiceException, SAXException, ParserConfigurationException, IOException { //writeIntoGetSellerListData
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        //formatting the sequence ID
        DecimalFormat df = new DecimalFormat("00000");
        String productStoreId = mapContent.get("productStoreId").toString();
        
        try {   //main try -- START
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
            String hasMoreItems = UtilXml.childElementValue(elemResponse, "HasMoreItems", null);
            String pageNumber = UtilXml.childElementValue(elemResponse, "PageNumber", null);
            
            if (ack.equals("Success") || ack.equals("Warning")) {    //if ack success -- START
                Debug.logError("Actual writeIntoGetSellerListData pageNumber " + pageNumber, module);
                List<? extends Element> itemArray = UtilXml.childElementList(elemResponse, "ItemArray");
                Iterator<? extends Element> itemArrayElemIter = itemArray.iterator();
                if (UtilValidate.isEmpty(itemArray)) {
                    Debug.logError("itemArray is null", module);
                }
                while (itemArrayElemIter.hasNext()) {   //loop itemArrayElemIter -- START
                    Element itemArrayELement = itemArrayElemIter.next();
                    List<? extends Element> items = UtilXml.childElementList(itemArrayELement, "Item");
                    Iterator<? extends Element> itemsElemIter = items.iterator();
                    while (itemsElemIter.hasNext()) {   //loop items Element -- START
                        String ebayActiveListingId = delegator.getNextSeqId("EbayActiveListing");
                        Map itemMap = FastMap.newInstance();
                        Element itemElement = itemsElemIter.next();
                        String itemId = UtilXml.childElementValue(itemElement, "ItemID", null);
                        itemMap.put("buyItNowPrice", UtilXml.childElementValue(itemElement, "BuyItNowPrice", null));
                        itemMap.put("buyItNowPriceCurId", UtilXml.childElementAttribute(itemElement, "BuyItNowPrice", "currencyID", null));
                        itemMap.put("country", UtilXml.childElementValue(itemElement, "Country", null));
                        itemMap.put("currency", UtilXml.childElementValue(itemElement, "Currency", null));
                        itemMap.put("hitCount", UtilXml.childElementValue(itemElement, "HitCount", "0"));
                        itemMap.put("hitCounter", UtilXml.childElementValue(itemElement, "HitCounter", null));
                        itemMap.put("itemId", UtilXml.childElementValue(itemElement, "ItemID", null));
                        
                        //Listing Details -- START
                        Element listingDetailsElement = UtilXml.firstChildElement(itemElement, "ListingDetails");
                        itemMap.put("listDetEndTime", UtilXml.childElementValue(listingDetailsElement, "EndTime", null));
                        itemMap.put("listDetRelistedItemId", UtilXml.childElementValue(listingDetailsElement, "RelistedItemID", null));
                        itemMap.put("listDetStartTime", UtilXml.childElementValue(listingDetailsElement, "StartTime", null));
                        itemMap.put("listDetOriginalItemId", UtilXml.childElementValue(listingDetailsElement, "TCROriginalItemID", null));
                        //Listing Details -- END
                        
                        itemMap.put("listingDuration", UtilXml.childElementValue(itemElement, "ListingDuration", null));
                        itemMap.put("listingSubtype2", UtilXml.childElementValue(itemElement, "ListingSubtype2", null));
                        itemMap.put("listingType", UtilXml.childElementValue(itemElement, "ListingType", null));
                        itemMap.put("location", UtilXml.childElementValue(itemElement, "Location", null));
                        itemMap.put("motorsGerSearch", UtilXml.childElementValue(itemElement, "MotorsGermanySearchable", null));
                        itemMap.put("outOfStockControl", UtilXml.childElementValue(itemElement, "OutOfStockControl", null));
                        
                        //Primary Category -- START
                        Element primaryCategoryElement = UtilXml.firstChildElement(itemElement, "PrimaryCategory");
                        itemMap.put("primaryCategoryId", UtilXml.childElementValue(primaryCategoryElement, "CategoryID", null));
                        itemMap.put("primaryCategoryName", UtilXml.childElementValue(primaryCategoryElement, "CategoryName", null));
                        //Primary Category -- END
                        
                        itemMap.put("quantity", UtilXml.childElementValue(itemElement, "Quantity", null));
                        itemMap.put("qtyAvailableHint", UtilXml.childElementValue(itemElement, "QuantityAvailableHint", null));
                        itemMap.put("qtyThreshold", UtilXml.childElementValue(itemElement, "QuantityThreshold", null));
                        
                        //Revise Status -- START
                        Element reviseStatusElement = UtilXml.firstChildElement(itemElement, "ReviseStatus");
                        itemMap.put("reviseStatus", UtilXml.childElementValue(reviseStatusElement, "ItemRevised", null));
                        //Revise Status -- END
                        
                        //Secondary Category -- START
                        Element secondaryCategoryElement = UtilXml.firstChildElement(itemElement, "SecondaryCategory");
                        itemMap.put("secondaryCategoryId", UtilXml.childElementValue(secondaryCategoryElement, "CategoryID", null));
                        itemMap.put("secondaryCategoryName", UtilXml.childElementValue(secondaryCategoryElement, "CategoryName", null));
                        //Secondary Category -- END
                        
                        //Selling Status -- START
                        Element sellingStatusElement = UtilXml.firstChildElement(itemElement, "SellingStatus");
                        //itemMap.put("sellStatAdminEnded", UtilXml.childElementValue(sellingStatusElement, "AdminEnded", null));
                        itemMap.put("sellStatBidCount", UtilXml.childElementValue(sellingStatusElement, "BidCount", null));
                        itemMap.put("sellStatBidIncrement", UtilXml.childElementValue(sellingStatusElement, "BidIncrement", null));
                        itemMap.put("sellStatBidIncrCurId", UtilXml.childElementAttribute(sellingStatusElement, "BidIncrement", "currencyID", null));
                        itemMap.put("sellStatCurrentPrice", UtilXml.childElementValue(sellingStatusElement, "CurrentPrice", null));
                        itemMap.put("sellStatCurrentPriceCurId", UtilXml.childElementAttribute(sellingStatusElement, "CurrentPrice", "currencyID", null));
                        itemMap.put("sellStatLeadCount", UtilXml.childElementValue(sellingStatusElement, "LeadCount", null));
                        itemMap.put("sellStatListingStatus", UtilXml.childElementValue(sellingStatusElement, "ListingStatus", null));
                        
                        //Selling Status - PromotionalSaleDetails -- START
                        Element promotionalSaleDetails = UtilXml.firstChildElement(sellingStatusElement, "PromotionalSaleDetails");
                        itemMap.put("promoEndTime", UtilXml.childElementValue(promotionalSaleDetails, "EndTime", null));
                        itemMap.put("promoOriginalPrice", UtilXml.childElementValue(promotionalSaleDetails, "OriginalPrice", null));
                        itemMap.put("promoOriginalPriceCurId", UtilXml.childElementAttribute(promotionalSaleDetails, "OriginalPrice", "currencyID", null));
                        itemMap.put("promoStartTime", UtilXml.childElementValue(promotionalSaleDetails, "StartTime", null));
                        //Selling Status - PromotionalSaleDetails -- END
                        
                        itemMap.put("sellStatQuantitySold", UtilXml.childElementValue(sellingStatusElement, "QuantitySold", null));
                        //Selling Status -- END
                        
                        //ShippingDetails -- START
                        Element shippingDetails = UtilXml.firstChildElement(itemElement, "ShippingDetails");
                        itemMap.put("shipDetAllowPaymentEdit", UtilXml.childElementValue(shippingDetails, "AllowPaymentEdit", null));
                        //ShippingDetails>ExcludeShipToLocation -- START
                        //TODO
                        //ShippingDetails>ExcludeShipToLocation -- END
                        itemMap.put("shipDetGlobalShipping", UtilXml.childElementValue(shippingDetails, "GlobalShipping", null));
                        itemMap.put("shipDetInsuranceOption", UtilXml.childElementValue(shippingDetails, "InsuranceOption", null));
                        
                        //ShippingDetails>InternationalShippingServiceOption -- START
                        List<? extends Element> internationalShippingServiceOption = UtilXml.childElementList(shippingDetails, "InternationalShippingServiceOption");
                        Iterator<? extends Element> internationalShippingServiceOptionElemIter = internationalShippingServiceOption.iterator();
                        while (internationalShippingServiceOptionElemIter.hasNext()) {  //loop internationalShippingServiceOptionElemIter -- START
                            Element internationalShippingServiceOptionElement = internationalShippingServiceOptionElemIter.next();
                            BigDecimal additionalCost2 = new BigDecimal(UtilXml.childElementValue(internationalShippingServiceOptionElement, "ShippingServiceAdditionalCost", "0"));
                            String listingShippingId = delegator.getNextSeqId("ActiveListingShipping");
                            GenericValue sdi = delegator.makeValue("ActiveListingShipping", UtilMisc.toMap("listingShippingId", listingShippingId, "activeListingId", ebayActiveListingId));
                            sdi.set("itemId", itemId);
                            sdi.set("productStoreId", productStoreId);
                            sdi.set("shippingServiceName", UtilXml.childElementValue(internationalShippingServiceOptionElement, "ShippingService", null));
                            sdi.set("domestic", "N");
                            sdi.set("shippingServiceCost", new BigDecimal(UtilXml.childElementValue(internationalShippingServiceOptionElement, "ShippingServiceCost", "0")));
                            sdi.set("shippingServiceCurrency", UtilXml.childElementAttribute(internationalShippingServiceOptionElement, "ShippingServiceCost", "currencyID", null));
                            if (additionalCost2 != null) {
                                sdi.set("additionalCost", additionalCost2);
                            }
                            sdi.set("priority", UtilXml.childElementValue(internationalShippingServiceOptionElement, "ShippingServicePriority", null));
                            sdi.set("expeditedService", UtilXml.childElementValue(internationalShippingServiceOptionElement, "ExpeditedService", null));
                            sdi.set("shippingTimeMin", UtilXml.childElementValue(internationalShippingServiceOptionElement, "ShippingTimeMin", null));
                            sdi.set("shippingTimeMax", UtilXml.childElementValue(internationalShippingServiceOptionElement, "ShippingTimeMax", null));
                            delegator.createOrStore(sdi);
                        }   //loop internationalShippingServiceOptionElemIter -- END
                        //ShippingDetails>InternationalShippingServiceOption -- END
                        
                        itemMap.put("shipDetPaymentEdited", UtilXml.childElementValue(shippingDetails, "PaymentEdited", null));
                        itemMap.put("shipDetExclShipToLoc", UtilXml.childElementValue(shippingDetails, "SellerExcludeShipToLocationsPreference", null));
                        
                        //ShippingServiceOptions -- START
                        List<? extends Element> shippingServiceOptions = UtilXml.childElementList(shippingDetails, "ShippingServiceOptions");
                        Iterator<? extends Element> shippingServiceOptionsElemIter = shippingServiceOptions.iterator();
                        while (shippingServiceOptionsElemIter.hasNext()) {  //loop shippingServiceOptionsElemIter -- START
                            Element shippingServiceOptionsElement = shippingServiceOptionsElemIter.next();
                            BigDecimal additionalCost = new BigDecimal(UtilXml.childElementValue(shippingServiceOptionsElement, "ShippingServiceAdditionalCost", "0"));
                            String listingShippingIdDom = delegator.getNextSeqId("ActiveListingShipping");
                            GenericValue sd = delegator.makeValue("ActiveListingShipping", UtilMisc.toMap("listingShippingId", listingShippingIdDom,"activeListingId", ebayActiveListingId));
                            sd.set("itemId", itemId);
                            sd.set("productStoreId", productStoreId);
                            sd.set("shippingServiceName", UtilXml.childElementValue(shippingServiceOptionsElement, "ShippingService", null));
                            sd.set("domestic", "Y");
                            sd.set("shippingServiceCost", new BigDecimal(UtilXml.childElementValue(shippingServiceOptionsElement, "ShippingServiceCost", "0")));
                            sd.set("shippingServiceCurrency", UtilXml.childElementAttribute(shippingServiceOptionsElement, "ShippingServiceCost", "currencyID", null));
                            if (additionalCost != null) {
                                sd.set("additionalCost", additionalCost);
                            }
                            sd.set("priority", UtilXml.childElementValue(shippingServiceOptionsElement, "ShippingServicePriority", null));
                            sd.set("expeditedService", UtilXml.childElementValue(shippingServiceOptionsElement, "ExpeditedService", null));
                            sd.set("shippingTimeMin", UtilXml.childElementValue(shippingServiceOptionsElement, "ShippingTimeMin", null));
                            sd.set("shippingTimeMax", UtilXml.childElementValue(shippingServiceOptionsElement, "ShippingTimeMax", null));
                            delegator.createOrStore(sd);
                        }   //loop shippingServiceOptionsElemIter -- END
                        //ShippingServiceOptions -- END
                        //ShippingDetails -- END
                        
                        itemMap.put("site", UtilXml.childElementValue(itemElement, "Site", null));
                        itemMap.put("sku", UtilXml.childElementValue(itemElement, "SKU", null));
                        itemMap.put("startPrice", UtilXml.childElementValue(itemElement, "StartPrice", null));
                        itemMap.put("startPriceCurId", UtilXml.childElementAttribute(itemElement, "StartPrice", "currencyID", null));
                        
                        //Storefront -- START
                        Element storefront = UtilXml.firstChildElement(itemElement, "Storefront");
                        itemMap.put("storefrontCategoryId", UtilXml.childElementValue(storefront, "StoreCategoryID", null));
                        itemMap.put("storefrontCategory2Id", UtilXml.childElementValue(storefront, "StoreCategory2ID", null));
                        itemMap.put("storefrontUrl", StringEscapeUtils.unescapeHtml(UtilXml.childElementValue(storefront, "StoreURL", null)));
                        //Storefront -- END
                        
                        itemMap.put("timeLeft", UtilXml.childElementValue(itemElement, "TimeLeft", null));
                        itemMap.put("title", UtilXml.childElementValue(itemElement, "Title", null));
                        itemMap.put("totalQuestionCount", UtilXml.childElementValue(itemElement, "TotalQuestionCount", null));
                        
                        //Variations -- START
                        String hasVariation = "N";
                        Element variationsElement = UtilXml.firstChildElement(itemElement, "Variations");
                        if (UtilValidate.isNotEmpty(variationsElement)) {   //if variationsElement is not empty -- START
                            int varSeq = 0;
                            List<? extends Element> variation = UtilXml.childElementList(variationsElement, "Variation");
                            Iterator<? extends Element> variationElemIter = variation.iterator();
                            while (variationElemIter.hasNext()) {
                                hasVariation = "Y";
                                Element variationElement = variationElemIter.next();
                                varSeq++;
                                Map variationMap = FastMap.newInstance();
                                String variationSeqId = df.format(varSeq);
                                String productIdVariation = null;
                                if (UtilValidate.isNotEmpty(UtilXml.childElementValue(variationElement, "SKU", null))) {
                                    productIdVariation = UtilXml.childElementValue(variationElement, "SKU", null);
                                };
                                variationMap.put("variationSeqId", variationSeqId);
                                variationMap.put("productId", productIdVariation);
                                variationMap.put("startPrice", UtilXml.childElementValue(variationElement, "StartPrice", null));
                                variationMap.put("startPriceCurrencyId", UtilXml.childElementAttribute(variationElement, "StartPrice", "currencyID", null));
                                variationMap.put("quantity", UtilXml.childElementValue(variationElement, "Quantity", null));
                                
                                //SellingStatus -- START
                                Element sellingStatusVarElement = UtilXml.firstChildElement(variationElement, "SellingStatus");
                                variationMap.put("quantitySold", UtilXml.childElementValue(sellingStatusVarElement, "QuantitySold", null));
                                //SellingStatus > PromotionalSaleDetails -- START
                                Element promoVarElement = UtilXml.firstChildElement(sellingStatusVarElement, "PromotionalSaleDetails");
                                variationMap.put("originalPrice", UtilXml.childElementValue(promoVarElement, "OriginalPrice", null));
                                variationMap.put("originalPriceCurrencyId", UtilXml.childElementAttribute(promoVarElement, "OriginalPrice", "currencyID", null));
                                //SellingStatus > PromotionalSaleDetails -- END
                                //SellingStatus -- END
                                
                                //VariationSpecifics  -- START
                                List<? extends Element> variationSpecifics = UtilXml.childElementList(variationElement, "VariationSpecifics");
                                Iterator<? extends Element> variationSpecificsElemIter = variationSpecifics.iterator();
                                while (variationSpecificsElemIter.hasNext()) {  //loop variationSpecificsElemIter -- START
                                    Element variationSpecificsElement = variationSpecificsElemIter.next();
                                    List<? extends Element> nameValueList = UtilXml.childElementList(variationSpecificsElement, "NameValueList");
                                    Iterator<? extends Element> nameValueListElemIter = nameValueList.iterator();
                                    while (nameValueListElemIter.hasNext()) {   //loop nameValueListElemIter -- START
                                        Element nameValueListElement = nameValueListElemIter.next();
                                        Map varSpecsMap = FastMap.newInstance();
                                        varSpecsMap.put("varSpecsName", UtilXml.childElementValue(nameValueListElement, "Name", null));
                                        varSpecsMap.put("varSpecsValue", UtilXml.childElementValue(nameValueListElement, "Value", null));
                                        
                                        //Writing to Database (ListingVariationSpecifics) -- START
                                        String varSpecsId = delegator.getNextSeqId("ListingVariationSpecifics");
                                        GenericValue listingVariationSpecifics = delegator.makeValue("ListingVariationSpecifics", UtilMisc.toMap("varSpecsId", varSpecsId, "activeListingId", ebayActiveListingId));
                                        listingVariationSpecifics.set("productStoreId", productStoreId);
                                        listingVariationSpecifics.set("itemId", itemId);
                                        listingVariationSpecifics.set("variationSeqId", variationSeqId);
                                        listingVariationSpecifics.set("productId", productIdVariation);
                                        listingVariationSpecifics.set("varSpecsName", varSpecsMap.get("varSpecsName"));
                                        listingVariationSpecifics.set("varSpecsValue", varSpecsMap.get("varSpecsValue"));
                                        delegator.createOrStore(listingVariationSpecifics);
                                        //Writing to Database (ListingVariationSpecifics) -- END
                                    }   //loop nameValueListElemIter -- END
                                }   //loop variationSpecificsElemIter -- END
                                //VariationSpecifics -- END
                                
                                //Writing to Database (EbayActiveListingVariation) -- START
                                GenericValue ebayActiveListingVariation = delegator.makeValue("EbayActiveListingVariation", UtilMisc.toMap("activeListingId", ebayActiveListingId));
                                ebayActiveListingVariation.set("productStoreId", productStoreId);
                                ebayActiveListingVariation.set("itemId", itemId);
                                ebayActiveListingVariation.set("variationSeqId", variationSeqId);
                                ebayActiveListingVariation.set("productId", productIdVariation);
                                if (UtilValidate.isNotEmpty(productIdVariation)) {
                                    ebayActiveListingVariation.set("normalizedSku", normalizeSku(delegator,productIdVariation));
                                } else {
                                    ebayActiveListingVariation.set("normalizedSku", productIdVariation);
                                }
                                if (variationMap.get("startPriceCurrencyId") != null) { ebayActiveListingVariation.set("startPriceCurrencyId", variationMap.get("startPriceCurrencyId")); }
                                if (variationMap.get("startPrice") != null) { ebayActiveListingVariation.set("startPrice", new BigDecimal(variationMap.get("startPrice").toString())); }
                                if (variationMap.get("originalPriceCurrencyId") != null) { ebayActiveListingVariation.set("originalPriceCurrencyId", variationMap.get("originalPriceCurrencyId")); }
                                if (variationMap.get("originalPrice") != null) { ebayActiveListingVariation.set("originalPrice", new BigDecimal(variationMap.get("originalPrice").toString())); }
                                if (variationMap.get("quantity") != null) { ebayActiveListingVariation.set("quantity", Long.valueOf(variationMap.get("quantity").toString()) - Long.valueOf(variationMap.get("quantitySold").toString())); }
                                if (variationMap.get("quantitySold") != null) { ebayActiveListingVariation.set("quantitySold", Long.valueOf(variationMap.get("quantitySold").toString())); }
                                if (ebayActiveListingVariation.get("quantity") == null) { ebayActiveListingVariation.set("quantity", Long.valueOf(0)); }
                                delegator.createOrStore(ebayActiveListingVariation);
                                //Writing to Database (EbayActiveListingVariation) -- END
                            }   //loop variationElemIter -- END
                        }   //if variationsElement is not empty -- END
                        //Variations -- END
                        
                        itemMap.put("watchCount", UtilXml.childElementValue(itemElement, "WatchCount", "0"));
                        //itemMap.put("uuid", UtilXml.childElementValue(itemElement, "UUID", null));
                        itemMap.put("dispatchTimeMax", UtilXml.childElementValue(itemElement, "DispatchTimeMax", null));
                        itemMap.put("paypalEmailAddress", UtilXml.childElementValue(itemElement, "PayPalEmailAddress", null));
                        
                        //Writing to Database (getSellerListData)-- Start
                        
                        GenericValue getSellerListData = delegator.makeValue("EbayActiveListing", UtilMisc.toMap("activeListingId", ebayActiveListingId));
                        getSellerListData.set("productStoreId", productStoreId);
                        if (itemMap.get("itemId") != null) { getSellerListData.set("itemId", itemMap.get("itemId")); }if (itemMap.get("buyItNowPrice") != null) { getSellerListData.set("buyItNowPrice", new BigDecimal(itemMap.get("buyItNowPrice").toString())); }
                        if (itemMap.get("buyItNowPriceCurId") != null) { getSellerListData.set("buyItNowPriceCurId", itemMap.get("buyItNowPriceCurId")); }
                        if (itemMap.get("country") != null) { getSellerListData.set("country", itemMap.get("country")); }
                        if (itemMap.get("currency") != null) { getSellerListData.set("currency", itemMap.get("currency")); }
                        if (itemMap.get("freeCategoryId") != null) { getSellerListData.set("freeCategoryId", Long.valueOf(itemMap.get("freeCategoryId").toString())); }
                        if (itemMap.get("freeCategoryName") != null) { getSellerListData.set("freeCategoryName", Long.valueOf(itemMap.get("freeCategoryName").toString())); }
                        if (itemMap.get("hitCount") != null) { getSellerListData.set("hitCount", Long.valueOf(itemMap.get("hitCount").toString())); }
                        if (itemMap.get("hitCounter") != null) { getSellerListData.set("hitCounter", itemMap.get("hitCounter")); }
                        if (itemMap.get("listDetEndTime") != null) { getSellerListData.set("listDetEndTime", itemMap.get("listDetEndTime")); }
                        if (itemMap.get("listDetRelistedItemId") != null) { getSellerListData.set("listDetRelistedItemId", itemMap.get("listDetRelistedItemId")); }
                        if (itemMap.get("listDetStartTime") != null) { getSellerListData.set("listDetStartTime", itemMap.get("listDetStartTime")); }
                        if (itemMap.get("listDetOriginalItemId") != null) { getSellerListData.set("listDetOriginalItemId", itemMap.get("listDetOriginalItemId")); }
                        if (itemMap.get("listingDuration") != null) { getSellerListData.set("listingDuration", itemMap.get("listingDuration")); }
                        if (itemMap.get("listingSubtype2") != null) { getSellerListData.set("listingSubtype2", itemMap.get("listingSubtype2")); }
                        if (itemMap.get("listingType") != null) { getSellerListData.set("listingType", itemMap.get("listingType")); }
                        if (itemMap.get("location") != null) { getSellerListData.set("location", itemMap.get("location")); }
                        if (itemMap.get("outOfStockControl") != null) { getSellerListData.set("outOfStockControl", itemMap.get("outOfStockControl")); }
                        if (itemMap.get("primaryCategoryId") != null) { getSellerListData.set("primaryCategoryId", itemMap.get("primaryCategoryId")); }
                        if (itemMap.get("primaryCategoryName") != null) { getSellerListData.set("primaryCategoryName", itemMap.get("primaryCategoryName")); }
                        if (itemMap.get("privateListing") != null) { getSellerListData.set("privateListing", itemMap.get("privateListing")); }
                        if (itemMap.get("quantity") != null) { getSellerListData.set("quantity", Long.valueOf(itemMap.get("quantity").toString()) - Long.valueOf(itemMap.get("sellStatQuantitySold").toString())); }
                        if (itemMap.get("qtyAvailableHint") != null) { getSellerListData.set("qtyAvailableHint", itemMap.get("qtyAvailableHint")); }
                        if (itemMap.get("qtyThreshold") != null) { getSellerListData.set("qtyThreshold", itemMap.get("qtyThreshold")); }if (itemMap.get("warrantyTypeOption") != null) { getSellerListData.set("warrantyTypeOption", itemMap.get("warrantyTypeOption")); }
                        if (itemMap.get("reviseStatus") != null) { getSellerListData.set("reviseStatus", itemMap.get("reviseStatus")); }
                        if (itemMap.get("secondaryCategoryId") != null) { getSellerListData.set("secondaryCategoryId", itemMap.get("secondaryCategoryId")); }
                        if (itemMap.get("secondaryCategoryName") != null) { getSellerListData.set("secondaryCategoryName", itemMap.get("secondaryCategoryName")); }
                        if (itemMap.get("sellStatAdminEnded") != null) { getSellerListData.set("sellStatAdminEnded", itemMap.get("sellStatAdminEnded")); }
                        if (itemMap.get("sellStatBidCount") != null) { getSellerListData.set("sellStatBidCount", Long.valueOf(itemMap.get("sellStatBidCount").toString())); }
                        if (itemMap.get("sellStatBidIncrement") != null) { getSellerListData.set("sellStatBidIncrement", new BigDecimal(itemMap.get("sellStatBidIncrement").toString())); }
                        if (itemMap.get("sellStatBidIncrCurId") != null) { getSellerListData.set("sellStatBidIncrCurId", itemMap.get("sellStatBidIncrCurId")); }
                        if (itemMap.get("sellStatCurrentPrice") != null) { getSellerListData.set("sellStatCurrentPrice", new BigDecimal(itemMap.get("sellStatCurrentPrice").toString())); }
                        if (itemMap.get("sellStatCurrentPriceCurId") != null) { getSellerListData.set("sellStatCurrentPriceCurId", itemMap.get("sellStatCurrentPriceCurId")); }
                        if (itemMap.get("sellStatLeadCount") != null) { getSellerListData.set("sellStatLeadCount", Long.valueOf(itemMap.get("sellStatLeadCount").toString())); }
                        if (itemMap.get("sellStatListingStatus") != null) { getSellerListData.set("sellStatListingStatus", itemMap.get("sellStatListingStatus")); }
                        if (itemMap.get("sellStatMinToBid") != null) { getSellerListData.set("sellStatMinToBid", new BigDecimal(itemMap.get("sellStatMinToBid").toString())); }
                        if (itemMap.get("sellStatMinToBidCurId") != null) { getSellerListData.set("sellStatMinToBidCurId", itemMap.get("sellStatMinToBidCurId")); }
                        if (itemMap.get("promoEndTime") != null) { getSellerListData.set("promoEndTime", itemMap.get("promoEndTime")); }
                        if (itemMap.get("promoOriginalPrice") != null) { getSellerListData.set("promoOriginalPrice", new BigDecimal(itemMap.get("promoOriginalPrice").toString())); }
                        if (itemMap.get("promoOriginalPriceCurId") != null) { getSellerListData.set("promoOriginalPriceCurId", itemMap.get("promoOriginalPriceCurId")); }
                        if (itemMap.get("promoStartTime") != null) { getSellerListData.set("promoStartTime", itemMap.get("promoStartTime")); }
                        if (itemMap.get("sellStatQuantitySold") != null) { getSellerListData.set("sellStatQuantitySold", Long.valueOf(itemMap.get("sellStatQuantitySold").toString())); }
                        if (itemMap.get("shipDetGlobalShipping") != null) { getSellerListData.set("shipDetGlobalShipping", itemMap.get("shipDetGlobalShipping")); }
                        if (itemMap.get("shipDetInsuranceOption") != null) { getSellerListData.set("shipDetInsuranceOption", itemMap.get("shipDetInsuranceOption")); }
                        if (itemMap.get("shipDetPaymentEdited") != null) { getSellerListData.set("shipDetPaymentEdited", itemMap.get("shipDetPaymentEdited")); }
                        if (itemMap.get("shipDetExclShipToLoc") != null) { getSellerListData.set("shipDetExclShipToLoc", itemMap.get("shipDetExclShipToLoc")); }
                        if (itemMap.get("site") != null) { getSellerListData.set("site", itemMap.get("site")); }
                        if (itemMap.get("sku") != null) { getSellerListData.set("sku", itemMap.get("sku")); }
                        if (itemMap.get("sku") != null) { getSellerListData.set("normalizedSku", normalizeSku(delegator, itemMap.get("sku").toString())); }
                        if (itemMap.get("startPrice") != null) { getSellerListData.set("startPrice", new BigDecimal(itemMap.get("startPrice").toString())); }
                        if (itemMap.get("startPriceCurId") != null) { getSellerListData.set("startPriceCurId", itemMap.get("startPriceCurId")); }
                        if (itemMap.get("storefrontCategoryId") != null) { getSellerListData.set("storefrontCategoryId", itemMap.get("storefrontCategoryId")); }
                        if (itemMap.get("storefrontCategory2Id") != null) { getSellerListData.set("storefrontCategory2Id", itemMap.get("storefrontCategory2Id")); }
                        if (itemMap.get("storefrontUrl") != null) { getSellerListData.set("storefrontUrl", itemMap.get("storefrontUrl")); }
                        if (itemMap.get("timeLeft") != null) { getSellerListData.set("timeLeft", itemMap.get("timeLeft")); }
                        if (itemMap.get("title") != null) { getSellerListData.set("title", itemMap.get("title")); }
                        if (itemMap.get("watchCount") != null) { getSellerListData.set("watchCount", Long.valueOf(itemMap.get("watchCount").toString())); }
                        if (itemMap.get("dispatchTimeMax") != null) { getSellerListData.set("dispatchTimeMax", Long.valueOf(itemMap.get("dispatchTimeMax").toString())); }
                        if (itemMap.get("paypalEmailAddress") != null) { getSellerListData.set("paypalEmailAddress", itemMap.get("paypalEmailAddress")); }
                        getSellerListData.set("hasVariation", hasVariation);
                        delegator.createOrStore(getSellerListData);
                        
                        //Writing to Database (getSellerListData) -- END
                    }   //loop items Element -- END
                }   //loop itemArrayElemIter -- END
                result.put("writeResult", "SUCCESS");
            }   //if ack success -- END
            else {  //if ack failure -- START
                List<? extends Element> errorList = UtilXml.childElementList(elemResponse, "Errors");
                Iterator<? extends Element> errorElemIter = errorList.iterator();
                StringBuffer errorMessage = new StringBuffer();
                while (errorElemIter.hasNext()) {   //loop error Iterator -- START
                    Element errorElement = errorElemIter.next();
                    errorMessage.append(UtilXml.childElementValue(errorElement, "ShortMessage", ""));
                }   //loop error Iterator -- START
                Debug.logError(productStoreId + ": pageNumber " + pageNumber + " writeIntoGetSellerListData gives error from ResponseXML: " + errorMessage, module);
                result.put("writeResult", "ERROR");
                //return ServiceUtil.returnError("writeIntoGetSellerListData gives error from ResponseXML: " + errorMessage);
            }   //if ack failure -- END
            GenericValue updateListingStatus = delegator.findOne("UpdateListingStatus", UtilMisc.toMap("productStoreId", productStoreId), false);
            if(UtilValidate.isEmpty(updateListingStatus)) {
                updateListingStatus = delegator.makeValue("UpdateListingStatus", UtilMisc.toMap("productStoreId", productStoreId));
            }
            updateListingStatus.set("hasMoreItems", hasMoreItems);
            if (hasMoreItems.toUpperCase().equals("FALSE")) {
                pageNumber = "1";
            }
            updateListingStatus.set("lastPageNumber", pageNumber);
            delegator.createOrStore(updateListingStatus);
            Debug.logError(productStoreId + ": updating updateListingStatus hasMoreItems is " + hasMoreItems + ", lastPageNumber " + pageNumber, module);
        } //main try -- END
        catch (GenericEntityException e) {
            result.put("writeResult", "ERROR");
            Debug.logError("Yasin: writeIntoGetSellerListData GenericEntityException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (GenericServiceException e) {
            result.put("writeResult", "ERROR");
            Debug.logError("Yasin: writeIntoGetSellerListData GenericServiceException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (SAXException e) {
            result.put("writeResult", "ERROR");
            Debug.logError("Yasin: writeIntoGetSellerListData SAXException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (ParserConfigurationException e) {
            result.put("writeResult", "ERROR");
            Debug.logError("Yasin: writeIntoGetSellerListData ParserConfigurationException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (IOException e) {
            result.put("writeResult", "ERROR");
            Debug.logError("Yasin: writeIntoGetSellerListData IOException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.put("writeResult", "ERROR");
            Debug.logError("Yasin: writeIntoGetSellerListData Exception Error for TRY CATCH: " + e.getMessage(), module);
            //Debug.logError("responseXML writeIntoGetSellerListData function: " + responseXML, module);
            /*try {
                FileWriter f1 = new FileWriter("checkThisResponseXML.xml", true);
                f1.write(responseXML);
                f1.close();
            }
            catch (Exception e2) {
                
            }*/
        }
        
        /*Iterator<String> it = itemMap.keySet().iterator();
         while (it.hasNext()) {
         String key = it.next();
         Object val = itemMap.get(key);
         Debug.logError("Key is " + key + " with value " + val, module);
         }*/
        return result;
        
    }   //writeIntoGetSellerListData
    
    public static Map<String, Object> updateEbayActiveListingItemSpecifics (DispatchContext dctx, Map context)	//Description
    throws GenericEntityException, IOException {    //updateEbayActiveListingItemSpecifics
        
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String productStoreId = (String) context.get("productStoreId");
        Map mapAccount = FastMap.newInstance();
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        
        try {
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "GetItem");
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        try {   //main Try -- START
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("productStoreId",EntityOperator.EQUALS ,productStoreId)
                                                                                      ));
            List<GenericValue> activeListingLists = delegator.findList("EbayActiveListing", condition, null, null, null, false);
            //GenericValue listViewEntityItem = null;
            //int count = 0;
            for (GenericValue activeListing : activeListingLists) { //loop activeListingLists -- START
                String itemId = activeListing.getString("itemId");
                String productId = activeListing.getString("sku");
                
                //Building XML -- START
                Document rootDoc = UtilXml.makeEmptyXmlDocument("GetItemRequest");
                Element rootElem = rootDoc.getDocumentElement();
                rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                
                //RequesterCredentials -- START
                Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                //RequesterCredentials -- END
                
                UtilXml.addChildElementValue(rootElem, "ItemID", itemId, rootDoc);
                UtilXml.addChildElementValue(rootElem, "IncludeItemSpecifics", "true", rootDoc);
                UtilXml.addChildElementValue(rootElem, "DetailLevel", "ReturnAll", rootDoc);
                
                String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                //Debug.logError(responseXML, module);
                
                try {   //try Reading ResponseXML -- START
                    Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                    Element elemResponse = docResponse.getDocumentElement();
                    String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                    StringBuffer errorMessage = new StringBuffer();
                    
                    List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                    Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                    while (errorElementsElemIter.hasNext()) {
                        Element errorElement = errorElementsElemIter.next();
                        String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                        String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                        errorMessage.append(shortMessage + " - " + longMessage);
                        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/GetItemtError(ListingItemSpecific).log", true);
                        f1.write(today + ": productStoreId " + productStoreId + ", itemId " + itemId + ": " + errorMessage + "\n");
                        f1.close();
                    }
                    
                    if (ack != null && ack.equals("Success")) { //if ack is not null -- START
                        Debug.logError("updateEbayActiveListingItemSpecifics itemId: " + itemId, module);
                        List<? extends Element> itemList = UtilXml.childElementList(elemResponse, "Item");
                        Iterator<? extends Element> itemListElemIter = itemList.iterator();
                        while (itemListElemIter.hasNext()) {   //loop itemListElemIter -- START
                            Element itemElement = itemListElemIter.next();
                            
                            //writeItemSpecific -- START
                            List<? extends Element> itemSpecificsList = UtilXml.childElementList(itemElement, "ItemSpecifics");
                            Iterator<? extends Element> itemSpecificsListElemIter = itemSpecificsList.iterator();
                            while (itemSpecificsListElemIter.hasNext()) {    //loop itemSpecificsList -- START
                                Element itemSpecificsELement = itemSpecificsListElemIter.next();
                                List<? extends Element> nameValueListList = UtilXml.childElementList(itemSpecificsELement, "NameValueList");
                                Iterator<? extends Element> nameValueListListElemIter = nameValueListList.iterator();
                                while (nameValueListListElemIter.hasNext()) {   //loop nameValueListListElemIter -- START
                                    Element nameValueListElement = nameValueListListElemIter.next();
                                    String name = UtilXml.childElementValue(nameValueListElement, "Name", null);
                                    List<? extends Element> valueList = UtilXml.childElementList(nameValueListElement, "Value");
                                    Iterator<? extends Element> valueListElemIter = valueList.iterator();
                                    while (valueListElemIter.hasNext()) {   //loop valueListElemIter -- START
                                        Element valueElement = valueListElemIter.next();
                                        String value = UtilXml.elementValue(valueElement);
                                        //Debug.logError("name: " + name + " ; value: " + value, module);
                                        
                                        //Start writing into database -- START
                                        /*GenericValue gv = delegator.makeValue("ProductItemSpecific", UtilMisc.toMap("productId", productId, "ebaySiteId", mapAccount.get("siteId")));
                                         gv.set("name", name);
                                         gv.set("value", value);
                                         delegator.createOrStore(gv);*/
                                        
                                        GenericValue gv2 = delegator.makeValue("ActiveListingItemSpecific", UtilMisc.toMap("itemId", itemId, "productStoreId", productStoreId));
                                        gv2.set("name", name);
                                        gv2.set("value", value);
                                        delegator.createOrStore(gv2);
                                        //Start writing into database -- END
                                    }   //loop valueListElemIter -- END
                                }   //loop nameValueListListElemIter -- END
                            }   //loop itemSpecificsList -- END
                            //writeItemSpecific -- END
                            
                            //loop item>Variations -- START
                            List<? extends Element> variationsList = UtilXml.childElementList(itemElement, "Variations");
                            Iterator<? extends Element> variationsListElemIter = variationsList.iterator();
                            while (variationsListElemIter.hasNext()) {  //loop variationsListElemIter -- START
                                Element variationsElement = variationsListElemIter.next();
                                
                                //writeVariationPictureSpecific -- START
                                List<? extends Element> picturesList = UtilXml.childElementList(variationsElement, "Pictures");
                                Iterator<? extends Element> picturesListElemIter = picturesList.iterator();
                                while (picturesListElemIter.hasNext()) {    //loop picturesListElemIter -- START
                                    Element picturesElement = picturesListElemIter.next();
                                    String variationSpecificName = UtilXml.childElementValue(picturesElement, "VariationSpecificName", null);
                                    List<? extends Element> varSpecPicSetList = UtilXml.childElementList(picturesElement, "VariationSpecificPictureSet");
                                    Iterator<? extends Element> varSpecPicSetListElemIter = varSpecPicSetList.iterator();
                                    while (varSpecPicSetListElemIter.hasNext()) {   //loop varSpecPicSetListElemIter -- START
                                        Element varSpecPicSetElement = varSpecPicSetListElemIter.next();
                                        String variationSpecificValue = UtilXml.childElementValue(varSpecPicSetElement, "VariationSpecificValue", null);
                                        String pictureUrl = StringEscapeUtils.unescapeHtml(UtilXml.childElementValue(varSpecPicSetElement, "PictureURL", null));
                                        String extPictureUrl = StringEscapeUtils.unescapeHtml(UtilXml.childElementValue(varSpecPicSetElement, "ExternalPictureURL", null));
                                        
                                        GenericValue varPicSet = delegator.makeValue("VariationPictureSpecific", UtilMisc.toMap("itemId", itemId, "productStoreId", productStoreId));
                                        varPicSet.set("variationSpecificName", variationSpecificName);
                                        varPicSet.set("variationSpecificValue", variationSpecificValue);
                                        varPicSet.set("pictureUrl", pictureUrl);
                                        varPicSet.set("extPictureUrl", extPictureUrl);
                                        delegator.createOrStore(varPicSet);
                                    }   //loop varSpecPicSetListElemIter -- END
                                }   //loop picturesListElemIter -- END
                                //writeVariationPictureSpecific -- END
                                
                                //writeVariationSpecificSet -- START
                                List<? extends Element> varSpecSetList = UtilXml.childElementList(variationsElement, "VariationSpecificsSet");
                                Iterator<? extends Element> varSpecSetListElemIter = varSpecSetList.iterator();
                                while (varSpecSetListElemIter.hasNext()) {  //loop varSpecSetListElemIter -- START
                                    Element varSpecSetElement = varSpecSetListElemIter.next();
                                    List<? extends Element> varSpecSetNameValueList = UtilXml.childElementList(varSpecSetElement, "NameValueList");
                                    Iterator<? extends Element> varSpecSetNameValueListElemIter = varSpecSetNameValueList.iterator();
                                    while (varSpecSetNameValueListElemIter.hasNext()) { //loop varSpecSetNameValueListElemIter -- START
                                        Element varSpecSetNameValueListElement = varSpecSetNameValueListElemIter.next();
                                        String varSpecSetName = UtilXml.childElementValue(varSpecSetNameValueListElement, "Name", null);
                                        List<? extends Element> varSpecSetValueList = UtilXml.childElementList(varSpecSetNameValueListElement, "Value");
                                        Iterator<? extends Element> varSpecSetValueListElemIter = varSpecSetValueList.iterator();
                                        int i = 0;
                                        while (varSpecSetValueListElemIter.hasNext()) { //loop varSpecSetValueListElemIter -- START
                                            
                                            Element varSpecSetValueElement = varSpecSetValueListElemIter.next();
                                            String varSpecSetValue = UtilXml.elementValue(varSpecSetValueElement);
                                            //Debug.logError("YasinYasinYasinYasinYasinYasinYasinYasinYasinYasinYasin=========================== " + i + ", Name is " + varSpecSetName + " and value is " + varSpecSetValue, module);i++;
                                            GenericValue varSpecSetGV = delegator.makeValue("VariationSpecificsSet", UtilMisc.toMap("itemId", itemId, "productStoreId", productStoreId));
                                            varSpecSetGV.set("name", varSpecSetName);
                                            varSpecSetGV.set("value", varSpecSetValue);
                                            delegator.createOrStore(varSpecSetGV);
                                        }   //loop varSpecSetValueListElemIter  -- END
                                    }   //loop varSpecSetNameValueListElemIter -- END
                                }   //loop varSpecSetListElemIter -- END
                                //writeVariationSpecificSet -- END
                                
                            }   //loop variationsListElemIter -- END
                            //loop item>Variations -- END
                        }   //loop itemListElemIter -- END
                        Debug.logError("updateEbayActiveListingItemSpecifics finished processing itemId " + itemId, module);
                    }   //if ack is not null -- END
                }   //try Reading ResponseXML -- END
                catch (Exception e) {
                    e.printStackTrace();
                }
            }   //loop activeListingLists -- END
        }   //main Try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError("Finished running updateEbayActiveListingItemSpecifics", module);
        return result;
        
    }   //updateEbayActiveListingItemSpecifics
    
    public static Map<String, Object> addOutOfStockControl (DispatchContext dctx, Map context)
    throws GenericEntityException { //addOutOfStockControl
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        
        try {   //main try -- START
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("outOfStockControl", EntityOperator.EQUALS, null),
                                                                                      EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                                                      EntityCondition.makeCondition("listingType", EntityOperator.LIKE, "%FixedPrice%"),
                                                                                      EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active")
                                                                                      ));
            List<GenericValue> activeListingLists = delegator.findList("EbayActiveListing", condition, null, null, null, false);
            
            for (GenericValue activeListingList : activeListingLists) { //loop activeListingLists -- START
                //String productStoreId = activeListingList.getString("productStoreId");
                String itemId = activeListingList.getString("itemId");
                //Debug.logError("itemId: " + itemId + " listingType: " + activeListingList.getString("listingType"), module);
                Map mapAccount = FastMap.newInstance();
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                mapAccount = common.accountInfo(delegator, productStore);
                mapAccount.put("callName", "ReviseFixedPriceItem");
                
                //Building XML -- START
                Document rootDoc = UtilXml.makeEmptyXmlDocument("ReviseFixedPriceItem");
                Element rootElem = rootDoc.getDocumentElement();
                rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                
                //RequesterCredentials -- START
                Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                //RequesterCredentials -- END
                
                //Item -- START
                Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
                UtilXml.addChildElementValue(itemElem, "ItemID", itemId, rootDoc);
                UtilXml.addChildElementValue(itemElem, "OutOfStockControl", "true", rootDoc);
                
                String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                
                Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                Element elemResponse = docResponse.getDocumentElement();
                String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                StringBuffer errorMessage = new StringBuffer();
                
                if (ack.equals("Failure")) {    //if ack failure -- START
                    List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                    Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                    while (errorElementsElemIter.hasNext()) {
                        Element errorElement = errorElementsElemIter.next();
                        String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                        String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                        errorMessage.append(shortMessage + " - " + longMessage);
                        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/AddOutOfStockControl.log", true);
                        f1.write(today + ": productStoreId " + productStoreId + ", itemId " + itemId + ": " + errorMessage + "\n");
                        f1.close();
                    }
                    Debug.logError("ItemId: " + itemId + ", error: " + errorMessage, module);
                }   //if ack failure -- END
            }   //loop activeListingLists -- END
        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError("finished running AddOutOfStockControl", module);
        return ServiceUtil.returnSuccess();
    }   //addOutOfStockControl
    
    public static Map<String, Object> completeSale(DispatchContext dctx, Map context)	//Update Tracking number
    throws GenericEntityException, IOException {    //completeSale
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String orderId = (String) context.get("orderId");
        String eBayItemId = (String) context.get("eBayItemId");
        String eBayTransactionId = (String) context.get("eBayTransactionId");
        String trackingNumber = (String) context.get("trackingNumber");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map mapAccount = FastMap.newInstance();
        try {
            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "CompleteSale");
            mapAccount.put("eBayItemId", eBayItemId);
            mapAccount.put("eBayTransactionId", eBayTransactionId);
            mapAccount.put("eBayOrderId", orderHeader.getString("externalId"));
            mapAccount.put("trackingNumber", trackingNumber);
            mapAccount.put("shippingCarrier", "ChinaPost");
            //mapAccount.put("shippedBoolean", "true");
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        String requestXMLcode = requestXML.completeSaleRequestXML(mapAccount);
        String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode.toString());
        //Debug.logError("ResponseXML: " + responseXML, module);	//DEBUG purpose display the responseXML to console
        
        try {
            
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
            
            if (ack.equals("Success")) {
                return ServiceUtil.returnSuccess("CompleteSaleResponseXML returns success");
            }
            else {
                List<? extends Element> errorList = UtilXml.childElementList(elemResponse, "Errors");
                Iterator<? extends Element> errorElemIter = errorList.iterator();
                StringBuffer errorMessage = new StringBuffer();
                while (errorElemIter.hasNext()) {   //loop error Iterator -- START
                    Element errorElement = errorElemIter.next();
                    errorMessage.append(UtilXml.childElementValue(errorElement, "ShortMessage", ""));
                }   //loop error Iterator -- START
                
                dispatcher.runSync("createOrderNote", UtilMisc.toMap("orderId", orderId, "internalNote", "Y", "note", "Fail to update tracking number to eBay. Error Message: " + errorMessage, "userLogin", userLogin));
                return ServiceUtil.returnError(mapAccount.get("callName")+ "ResponseXML returns Failure message: " + errorMessage);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ServiceUtil.returnSuccess();
    }   //completeSale

    public static Map<String, Object> addMemberMessageAAQToPartnerRequest(DispatchContext dctx, Map context)
	throws GenericEntityException, IOException {    //addMemberMessageAAQToPartnerRequest
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String productStoreId = (String) context.get("productStoreId");
        String eBayItemId = (String) context.get("eBayItemId");
        String eBayUserId = (String) context.get("eBayUserId");
        String questionType = (String) context.get("questionType");
        String messageSubject = (String) context.get("messageSubject");
        String messageBody = (String) context.get("messageBody");
        Map mapAccount = FastMap.newInstance();
        Map result = ServiceUtil.returnSuccess();
        
        try {
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "AddMemberMessageAAQToPartner");
        } catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
        //Populating mapAccount with necessary data -- START
        mapAccount.put("eBayItemId", eBayItemId);
        mapAccount.put("eBayUserId", eBayUserId);
        mapAccount.put("questionType", questionType);
        mapAccount.put("messageSubject", messageSubject);
        mapAccount.put("messageBody", messageBody);
        //Populating mapAccount with necessary data -- END
        
        String requestXMLcode = requestXML.addMemberMessageAAQToPartnerRequestRequestXML(mapAccount);
        //Debug.logError(requestXMLcode, module);
        String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode.toString());
        //Debug.logError(responseXML, module);
        
        try {
            
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
            
            if (ack.equals("Success")) {	//check if the responseXML return Success -- START
                
            }   //check if the responseXML return Success -- END
            else {  //if responseXML return Error -- START
                //Write responseXML to directories -- START
                File f = new File ("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/AddMemberMessageAAQToPartner-" + productStoreId + "-" + eBayItemId + "-ERROR.xml");
                if(f.exists() && f.isFile()){
                    f.delete();
                }
                FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/AddMemberMessageAAQToPartner-" + productStoreId + "-" + eBayItemId + "-ERROR.xml", false);
                f1.write(responseXML);
                f1.close();
                //Write responseXML to directories -- END
                result = ServiceUtil.returnError("AddMemberMessageAAQToPartnerRequestResponseXML returns error");
            }   //if responseXML return Error -- END
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //addMemberMessageAAQToPartnerRequest
    
    public static Map<String, Object> updateEbayListingQuantity (DispatchContext dctx, Map context)
	throws GenericEntityException, GenericServiceException, IOException {   //updateEbayListingQuantity
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        Map result = ServiceUtil.returnSuccess();
        
        try {   //main try -- START
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            if (UtilValidate.isEmpty(productStore)) {
                return ServiceUtil.returnError("ProductStoreId not found");
            }
            GenericValue productStoreEbaySetting = productStore.getRelatedOne("ProductStoreEbaySetting", false);
            if (UtilValidate.isEmpty(productStoreEbaySetting)) {
                return ServiceUtil.returnError("ProductStoreEbaySetting for " + productStoreId +" not found");
            }
            String autoUpdateEbayQuantity = productStoreEbaySetting.getString("autoUpdateEbayQuantity");
            
            if(autoUpdateEbayQuantity.equals("Y")) {    //if autoUpdateEbayQuantity is Y -- START
                List<GenericValue> activeListings = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("productStoreId", productStoreId, "sellStatListingStatus", "Active"), null, false);
                if (UtilValidate.isNotEmpty(activeListings)) {  //if activeListings is not empty -- START
                    for (GenericValue activeListing : activeListings) { //loop activeListings -- START
                        if (!activeListing.getString("listingType").equals("Chinese")) {    //if listingType is not auction -- START
                            String itemId = activeListing.getString("itemId");
                            dispatcher.runSync("updateEbayListingQuantitySingle", UtilMisc.toMap("itemId", itemId, "userLogin", userLogin));
                        }   //if listingType is not auction -- END
                    }   //loop activeListings -- END
                }   //if activeListings is not empty -- END
                else {  //if activeListings is empty -- START
                    result.put("responseMessage", "No eBay Active Listing with Active status found for " + productStoreId);
                }   //if activeListings is empty -- END
                
            }   //if autoUpdateEbayQuantity is Y -- END
            else {  //if autoUpdateEbayQuantity is N -- START
                result.put("responseMessage", "Not updating eBay quantity since autoUpdateEbayQuantity for " + productStoreId + " is set to N");
            }   //if autoUpdateEbayQuantity is N -- END
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
            return result;
        }
        catch (GenericServiceException e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
            return result;
        }
        
        return result;
    }   //updateEbayListingQuantity
    
    public static Map<String, Object> updateEbayListingQuantitySingle (DispatchContext dctx, Map context)
	throws GenericEntityException, GenericServiceException, IOException {   //updateEbayListingQuantitySingle
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String itemId = (String) context.get("itemId");
        Map result = ServiceUtil.returnSuccess();
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        Map mapAccount = FastMap.newInstance();
        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/updateEbayListingQuantity.log", true);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        Timestamp todayTS = Timestamp.valueOf(sdf.format(now.getTime()));

        
        try {   //main try -- START
            //Debug.logError("Processing itemId " + itemId, module);
            GenericValue ebayActiveListing = EntityUtil.getFirst(delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false));
            if (UtilValidate.isEmpty(ebayActiveListing)) {
                return ServiceUtil.returnSuccess("EbayActiveListing for item ID " + itemId + " not found");
            }
            String productStoreId = ebayActiveListing.getString("productStoreId");
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            if (UtilValidate.isEmpty(productStore)) {
                return ServiceUtil.returnSuccess("ProductStoreId not found for active listing itemId " + itemId);
            }
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "ReviseFixedPriceItem");
            GenericValue productStoreEbaySetting = productStore.getRelatedOne("ProductStoreEbaySetting", false);
            if (UtilValidate.isEmpty(productStoreEbaySetting)) {
                return ServiceUtil.returnSuccess("ProductStoreEbaySetting for " + productStoreId +" not found");
            }
            
            long listQuantity = productStoreEbaySetting.getLong("defaultEbayListQty");
            long minQuantity = productStoreEbaySetting.getLong("defaultEbayMinQty");
            boolean adjustQuantity = false;
            
            String productId = null;
            GenericValue product = null;
            Timestamp salesDiscontinuationDate = null;
            boolean active = true;
            long quantity = 0;
            BigDecimal startPrice = BigDecimal.ZERO;
            GenericValue productEbaySetting = null;
            
            //Building XML -- START
            Document rootDoc = UtilXml.makeEmptyXmlDocument("ReviseFixedPriceItemRequest");
            Element rootElem = rootDoc.getDocumentElement();
            rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            
            //RequesterCredentials -- START
            Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
            UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
            //RequesterCredentials -- END
            
            //Item -- START
            Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
            UtilXml.addChildElementValue(itemElem, "ItemID", itemId, rootDoc);
            
            
            List<GenericValue> listingVariations = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("productStoreId", productStoreId, "itemId", itemId), null, false);
            if (UtilValidate.isNotEmpty(listingVariations)) {   //if listingVariations is not empty -- START
                //Debug.logError("Running listingVariations for itemId " + itemId, module);
                Element variationsElem = UtilXml.addChildElement(itemElem, "Variations", rootDoc);
                int i = 0;
                for (GenericValue listingVariation : listingVariations) {   //loop listingVariations -- START
                    String variationSeqId = listingVariation.getString("variationSeqId");
                    productId = listingVariation.getString("productId");
                    product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                    
                    //Debug.logError("ProductId: " + productId, module);
                    if (UtilValidate.isNotEmpty(product)) { //if product exist in database -- START
                        salesDiscontinuationDate = product.getTimestamp("salesDiscontinuationDate");
                        //Debug.logError(productId + " has salesDiscontinuationDate: " + salesDiscontinuationDate, module);
                        
                        if (UtilValidate.isNotEmpty(salesDiscontinuationDate)) { //check discontinued product -- START
                            active = false;
                            if (todayTS.before(salesDiscontinuationDate)) {
                                active = true;
                                //Debug.logError("salesDiscontinuationDate is before today for " + productId, module);
                            }
                        }  //check discontinued product -- END
                        else {  //product is active -- START
                            active = true;
                        }   //product is active -- END
                        
                        quantity = listingVariation.getLong("quantity");
                        startPrice = listingVariation.getBigDecimal("startPrice");
                        
                        productEbaySetting = delegator.findOne("ProductEbaySetting", UtilMisc.toMap("productStoreId", productStoreId, "productId", productId), false);
                        if (UtilValidate.isNotEmpty(productEbaySetting)) {  //if productEbaySetting is not empty -- START
                            if (productEbaySetting.getLong("listQuantity") != null)   {
                                listQuantity = productEbaySetting.getLong("listQuantity");
                            }
                            if (productEbaySetting.getLong("minQuantity") != null) {
                                minQuantity = productEbaySetting.getLong("minQuantity");
                            }
                            if (productEbaySetting.getString("updateQuantity").equals("N")) {
                                active = false;
                            }
                        }   //if productEbaySetting is not empty -- END
                        
                        if ((quantity <= minQuantity) && active)    {   //if need to update quantity -- START
                            adjustQuantity = true;
                            Element variationElem = UtilXml.addChildElement(variationsElem, "Variation", rootDoc);
                            UtilXml.addChildElementValue(variationElem, "Delete", "false", rootDoc);
                            UtilXml.addChildElementValue(variationElem, "Quantity", String.valueOf(listQuantity), rootDoc);
                            UtilXml.addChildElementValue(variationElem, "SKU", productId, rootDoc);
                            UtilXml.addChildElementValue(variationElem, "StartPrice", startPrice.toString(), rootDoc);
                            Element variationSpecificsElem = UtilXml.addChildElement(variationElem, "VariationSpecifics", rootDoc);
                            
                            List<GenericValue> variationSpecifics = delegator.findByAnd("ListingVariationSpecifics", UtilMisc.toMap("productStoreId", productStoreId, "itemId", itemId, "variationSeqId", variationSeqId), null, false);
                            if (variationSpecifics == null) { //if variationSpecifics is empty -- START
                                adjustQuantity = false;
                                f1.write(today + ": productStoreId " + productStoreId + ", itemId " + itemId + ": " + productId + " does not have any data in ListingVariationSpecifics.\n");
                            }   //if variationSpecifics is empty -- END
                            else {  //if variationSpecifics is not empty -- START
                                for (GenericValue variationSpecific : variationSpecifics) { //loop variationSpecifics -- START
                                    String varSpecsName = variationSpecific.getString("varSpecsName");
                                    String varSpecsValue = variationSpecific.getString("varSpecsValue");
                                    Element nameValueListElem = UtilXml.addChildElement(variationSpecificsElem, "NameValueList", rootDoc);
                                    UtilXml.addChildElementValue(nameValueListElem, "Name", varSpecsName, rootDoc);
                                    UtilXml.addChildElementValue(nameValueListElem, "Value", varSpecsValue, rootDoc);
                                }   //loop variationSpecifics -- END
                            }   //if variationSpecifics is not empty -- END
                        }   //if need to update quantity -- END
                        i++;
                    }   //if product exist in database -- END
                    else {  //if product does not exist in database -- START
                        Debug.logError("ProductId: " + productId + " does not exist in database", module);
                        f1.write(today + ": productStoreId " + productStoreId + ", itemId " + itemId + ": " + productId + " does not exist in database.\n");
                    }   //if product does not exist in database -- END
                }   //loop listingVariations -- END
                if (i < 1) {
                    adjustQuantity = false;
                }
            }   //if listingVariations is not empty -- END
            else {  //if listingVariations is empty -- START
                productId = ebayActiveListing.getString("sku");
                product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                
                if (UtilValidate.isNotEmpty(product)) { //if product exist in database -- START
                    salesDiscontinuationDate = product.getTimestamp("salesDiscontinuationDate");
                    //Debug.logError(productId + " has salesDiscontinuationDate: " + salesDiscontinuationDate, module);
                    
                    if (UtilValidate.isNotEmpty(salesDiscontinuationDate)) { //check discontinued product -- START
                        active = false;
                        if (todayTS.before(salesDiscontinuationDate)) {
                            active = true;
                            //Debug.logError("salesDiscontinuationDate is before today for " + productId, module);
                        }
                    }  //check discontinued product -- END
                    else {  //product is active -- START
                        active = true;
                    }   //product is active -- END
                    
                    quantity = ebayActiveListing.getLong("quantity");
                    startPrice = ebayActiveListing.getBigDecimal("startPrice");
                    
                    productEbaySetting = delegator.findOne("ProductEbaySetting", UtilMisc.toMap("productStoreId", productStoreId, "productId", productId), false);
                    if (UtilValidate.isNotEmpty(productEbaySetting)) {  //if productEbaySetting is not empty -- START
                        if (productEbaySetting.getLong("listQuantity") != null)   {
                            listQuantity = productEbaySetting.getLong("listQuantity");
                        }
                        if (productEbaySetting.getLong("minQuantity") != null) {
                            minQuantity = productEbaySetting.getLong("minQuantity");
                        }
                        if (productEbaySetting.getString("updateQuantity").equals("N")) {
                            active = false;
                        }
                    }   //if productEbaySetting is not empty -- END
                    
                    if ((quantity <= minQuantity) && active)    {   //if need to update quantity -- START
                        adjustQuantity = true;
                        UtilXml.addChildElementValue(itemElem, "Quantity", String.valueOf(listQuantity), rootDoc);
                        UtilXml.addChildElementValue(itemElem, "SKU", productId, rootDoc);
                        UtilXml.addChildElementValue(itemElem, "StartPrice", startPrice.toString(), rootDoc);
                    }   //if need to update quantity -- END

                }   //if product exist in database -- END
                else {  //if product does not exist in database -- START
                    adjustQuantity = false;
                    Debug.logError("ProductId: " + productId + " does not exist in database", module);
                    f1.write(today + ": productStoreId " + productStoreId + ", itemId " + itemId + ": " + productId + " does not exist in database.\n");
                }   //if product does not exist in database -- END
                Debug.logError("Not running listingVariations for itemId " + itemId, module);
            }   //if listingVariations is empty -- END
            
            String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
            if (adjustQuantity) {   //if adjustQuantity true -- START
                Debug.logError("Processing itemId " + itemId, module);
                //Debug.logError(requestXMLcode, module);
                String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                
                Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                Element elemResponse = docResponse.getDocumentElement();
                String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                
                if (ack.equals("Success") || ack.equals("Warning")) {	//check if the responseXML return Success -- START
                    
                }   //check if the responseXML return Success -- END
                else {  //if responseXML return Error -- START
                    List<? extends Element> errorList = UtilXml.childElementList(elemResponse, "Errors");
                    Iterator<? extends Element> errorElemIter = errorList.iterator();
                    StringBuffer errorMessage = new StringBuffer();
                    while (errorElemIter.hasNext()) {   //loop error Iterator -- START
                        Element errorElement = errorElemIter.next();
                        errorMessage.append(UtilXml.childElementValue(errorElement, "ShortMessage", ""));
                    }   //loop error Iterator -- START

                    //Write responseXML to directories -- START
                    f1.write(today + ": productStoreId " + productStoreId + ", itemId " + itemId + ": " + errorMessage + "\n");
                    FileWriter errorResponseXML = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/ReviseFixedPriceItem-UpdateQuantity.xml", true);
                    errorResponseXML.write(responseXML);
                    errorResponseXML.close();
                    //Write responseXML to directories -- END
                }   //if responseXML return Error -- END
            }   //if adjustQuantity true -- END
            
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
            return result;
        }
        f1.close();
        return result;
        
    }   //updateEbayListingQuantitySingle
    
    public static Map<String, Object> EndEbayActiveListingSingle(DispatchContext dctx, Map context)
	throws GenericEntityException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        String itemId = (String) context.get("itemId");
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        Timestamp todayTS = Timestamp.valueOf(sdf.format(now.getTime()));
        String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(now.getTime());
        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/endItemsMulti.log", true);

        try {   //main try -- START
            List<GenericValue> ebayActiveListingList = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false);
            if (UtilValidate.isNotEmpty(ebayActiveListingList)) {   //if ebayActiveListingList is not empty -- START
                GenericValue ebayActiveListing = EntityUtil.getFirst(ebayActiveListingList);
                String productStoreId = ebayActiveListing.getString("productStoreId");
                String listingType = ebayActiveListing.getString("listingType");
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                
                //Building XML request -- START
                Map mapAccount = common.accountInfo(delegator, productStore);
                mapAccount.put("callName", "EndItem");
                Document rootDoc = UtilXml.makeEmptyXmlDocument("EndItemRequest");
                Element rootElem = rootDoc.getDocumentElement();
                rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                
                //RequesterCredentials -- START
                Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                //RequesterCredentials -- END
                
                UtilXml.addChildElementValue(rootElem, "EndingReason", "NotAvailable", rootDoc);
                UtilXml.addChildElementValue(rootElem, "ItemID", itemId, rootDoc);
                UtilXml.addChildElementValue(rootElem, "MessageID", itemId, rootDoc);
                
                //Building XML request -- END
                String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                //Debug.logError(requestXMLcode, module);
                String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                
                Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                Element elemResponse = docResponse.getDocumentElement();
                String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                
                if (ack.equals("Success") || ack.equals("Warning")) {	//check if the responseXML return Success -- START
                    GenericValue endedActiveListing = delegator.makeValue("EndedActiveListing", UtilMisc.toMap("itemId", itemId, "productStoreId", productStoreId, "date", todayTS));
                    delegator.create(endedActiveListing);
                }   //check if the responseXML return Success -- END
                else {  //if responseXML return Error -- START
                    List<? extends Element> errorList = UtilXml.childElementList(elemResponse, "Errors");
                    Iterator<? extends Element> errorElemIter = errorList.iterator();
                    StringBuffer errorMessage = new StringBuffer();
                    while (errorElemIter.hasNext()) {   //loop error Iterator -- START
                        Element errorElement = errorElemIter.next();
                        errorMessage.append(UtilXml.childElementValue(errorElement, "ShortMessage", ""));
                    }   //loop error Iterator -- START
                    
                    //Write responseXML to directories -- START
                    f1.write(today + ": productStoreId " + productStoreId + ", itemId " + itemId + ": " + errorMessage + "\n");
                    FileWriter errorResponseXML = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/EndItems-Single.xml", true);
                    errorResponseXML.write(responseXML);
                    errorResponseXML.close();
                    result.put("ebayErrorMessage", errorMessage.toString());
                    //Write responseXML to directories -- END
                }   //if responseXML return Error -- END
                
            }   //if ebayActiveListingList is not empty -- END
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
            return result;
        }
        f1.close();
        return result;
    }   //EndEbayActiveListingSingle
    
    public static Map<String, Object> EndEbayActiveListingMulti(DispatchContext dctx, Map context)
	throws GenericEntityException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        List<String> itemIdList = (List) context.get("itemIdList");
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
        Timestamp todayTS = Timestamp.valueOf(sdf.format(now.getTime()));
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/endItemsMulti.log", true);
        
        try {   //main try -- START
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("itemId", EntityOperator.IN, itemIdList)
                                                                                      ));
            List<GenericValue> activeListingList = delegator.findList("EbayActiveListing", condition, null, null, null, false);
            List<String> productStoreList = new ArrayList<String>();
            for (GenericValue activeListing : activeListingList) {
                productStoreList.add(activeListing.getString("productStoreId"));
            }
            HashSet<String> uniqueProductStoreList = new HashSet<String>(productStoreList);
            
            for (String productStoreId : uniqueProductStoreList) {  //loop productStoreId -- START
                EntityCondition uniqueCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                EntityCondition.makeCondition("itemId", EntityOperator.IN, itemIdList),
                                                                                                EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId)
                                                                                          ));
                List<GenericValue> productStoreActiveListingList = delegator.findList("EbayActiveListing", uniqueCondition, UtilMisc.toSet("itemId"), null, null, false);
                
                List<String> itemIdArray = new ArrayList<String>();
                for (GenericValue activeListingGV : productStoreActiveListingList) {
                    itemIdArray.add(activeListingGV.getString("itemId"));
                }
                
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                Map mapAccount = common.accountInfo(delegator, productStore);
                
                mapAccount.put("callName", "EndItems");
                
                if (productStoreActiveListingList.size() <= 10) {  //itemSize below 10 -- START
                    List<String> successItemIdList = new ArrayList<String>();
                    Document rootDoc = UtilXml.makeEmptyXmlDocument("EndItemsRequest");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    for (GenericValue gv : productStoreActiveListingList) {  //loop itemIdList -- START
                        String itemId = gv.getString("itemId");
                        successItemIdList.add(itemId);
                        Element endItemRequestContainerElem = UtilXml.addChildElement(rootElem, "EndItemRequestContainer", rootDoc);
                        UtilXml.addChildElementValue(endItemRequestContainerElem, "ItemID", itemId, rootDoc);
                        UtilXml.addChildElementValue(endItemRequestContainerElem, "EndingReason", "NotAvailable", rootDoc);
                        UtilXml.addChildElementValue(endItemRequestContainerElem, "MessageID", itemId, rootDoc);
                    }   //loop itemIdList -- END
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    //Debug.logError(requestXMLcode, module);
                    String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                    
                    Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                    Element elemResponse = docResponse.getDocumentElement();
                    String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                    
                    if (ack.equals("Success") || ack.equals("Warning")) {	//check if the responseXML return Success -- START
                        for (String successEndedItemId : successItemIdList) {
                            GenericValue endedActiveListing = delegator.makeValue("EndedActiveListing", UtilMisc.toMap("itemId", successEndedItemId, "productStoreId", productStoreId, "date", todayTS));
                            delegator.create(endedActiveListing);
                        }
                    }   //check if the responseXML return Success -- END
                    else {  //if responseXML return Error -- START
                        List<? extends Element> errorList = UtilXml.childElementList(elemResponse, "Errors");
                        Iterator<? extends Element> errorElemIter = errorList.iterator();
                        StringBuffer errorMessage = new StringBuffer();
                        while (errorElemIter.hasNext()) {   //loop error Iterator -- START
                            Element errorElement = errorElemIter.next();
                            errorMessage.append(UtilXml.childElementValue(errorElement, "ShortMessage", ""));
                        }   //loop error Iterator -- START
                        
                        //Write responseXML to directories -- START
                        f1.write(today + ": productStoreId " + productStoreId + ", itemIdList " + productStoreActiveListingList + ": " + errorMessage + "\n");
                        FileWriter errorRequestXML = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/requestXML/EndItems-Multi.xml", true);
                        errorRequestXML.write(requestXMLcode);
                        errorRequestXML.close();
                        FileWriter errorResponseXML = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/EndItems-Multi.xml", true);
                        errorResponseXML.write(responseXML);
                        errorResponseXML.close();
                        //Write responseXML to directories -- END
                    }   //if responseXML return Error -- END
                }   //itemSize below 10 -- END
                else {  //itemIdSize above 10 -- START
                    int itemIdListSize = productStoreActiveListingList.size();
                    int sentFrequent = itemIdListSize / 10;
                    int itemIdSlot = 0;
                    int sentFrequentCount = 1;
                    int checkLimit = sentFrequent;
                    if ((itemIdListSize % 10) != 0) {
                        checkLimit = checkLimit + 1;
                    }
                    while (sentFrequentCount <= checkLimit) {   //loop the sentFrequent -- START
                        List<String> successItemIdList = new ArrayList<String>();
                        Document rootDoc = UtilXml.makeEmptyXmlDocument("EndItemsRequest");
                        Element rootElem = rootDoc.getDocumentElement();
                        rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                        
                        //RequesterCredentials -- START
                        Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                        UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                        //RequesterCredentials -- END
                        for (int i = 1; i <= 10; i++) { //loop itemIdList to get 10 itemId -- START
                            successItemIdList.add(itemIdArray.get(itemIdSlot));
                            Element endItemRequestContainerElem = UtilXml.addChildElement(rootElem, "EndItemRequestContainer", rootDoc);
                            UtilXml.addChildElementValue(endItemRequestContainerElem, "ItemID", itemIdArray.get(itemIdSlot), rootDoc);
                            UtilXml.addChildElementValue(endItemRequestContainerElem, "EndingReason", "NotAvailable", rootDoc);
                            UtilXml.addChildElementValue(endItemRequestContainerElem, "MessageID", itemIdArray.get(itemIdSlot), rootDoc);
                            itemIdSlot++;
                            if (itemIdSlot == itemIdListSize) {
                                break;
                            }
                        }   //loop itemIdList to get 10 itemId -- END
                        String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                        //Debug.logError(requestXMLcode, module);
                        String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                        
                        Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                        Element elemResponse = docResponse.getDocumentElement();
                        String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                        
                        if (ack.equals("Success") || ack.equals("Warning")) {	//check if the responseXML return Success -- START
                            for (String successEndedItemId : successItemIdList) {
                                GenericValue endedActiveListing = delegator.makeValue("EndedActiveListing", UtilMisc.toMap("itemId", successEndedItemId, "productStoreId", productStoreId, "date", todayTS));
                                delegator.create(endedActiveListing);
                            }
                        }   //check if the responseXML return Success -- END
                        else {  //if responseXML return Error -- START
                            List<? extends Element> errorList = UtilXml.childElementList(elemResponse, "Errors");
                            Iterator<? extends Element> errorElemIter = errorList.iterator();
                            StringBuffer errorMessage = new StringBuffer();
                            while (errorElemIter.hasNext()) {   //loop error Iterator -- START
                                Element errorElement = errorElemIter.next();
                                errorMessage.append(UtilXml.childElementValue(errorElement, "ShortMessage", ""));
                            }   //loop error Iterator -- START
                            
                            //Write responseXML to directories -- START
                            f1.write(today + ": productStoreId " + productStoreId + ", itemIdList " + productStoreActiveListingList + ": " + errorMessage + "\n");
                            FileWriter errorRequestXML = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/requestXML/EndItems-Multi.xml", true);
                            errorRequestXML.write(requestXMLcode);
                            errorRequestXML.close();
                            FileWriter errorResponseXML = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/EndItems-Multi.xml", true);
                            errorResponseXML.write(responseXML);
                            errorResponseXML.close();
                            //Write responseXML to directories -- END
                        }   //if responseXML return Error -- END
                        sentFrequentCount++;
                    }   //loop the sentFrequent -- END
                    
                }   //itemIdSize above 10 -- END

            }   //loop productStoreId -- END
            
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
            return result;
        }
        f1.close();
        Debug.logError("finished running EndEbayActiveListingMulti", module);
        return result;
    }   //EndEbayActiveListingMulti
    
    public static Map<String, Object> relistActiveListingSingle (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String itemId = (String) context.get("itemId");
        Map result = ServiceUtil.returnSuccess();
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
        Timestamp todayTimestamp = Timestamp.valueOf(sdf.format(now.getTime()));
        
        try {   //main Try - START
            Map mapAccount = FastMap.newInstance();
            List<GenericValue> activeListingLists = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false);
            if (!activeListingLists.isEmpty()) {    //if activeListingLists is not empty -- START
                GenericValue activeListing = EntityUtil.getFirst(activeListingLists);
                String productStoreId = activeListing.getString("productStoreId");
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                mapAccount = common.accountInfo(delegator, productStore);
                
                //Relist the listing -- START
                String listingType = activeListing.getString("listingType");
                
                if (!listingType.equals("PersonalOffer")) { //if listingType is not PersonalOffer -- START
                    String callName = null;
                    
                    if (listingType.equals("Chinese")) {
                        callName = "AddItem";
                    }
                    else if (listingType.equals("FixedPriceItem") || listingType.equals("StoresFixedPrice")) {
                        callName = "AddFixedPriceItem";
                    }
                    
                    mapAccount.put("callName", callName);
                    
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    //Item -- START
                    Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ConditionID", activeListing.getString("conditionId"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "Country", activeListing.getString("country"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "DispatchTimeMax", activeListing.getString("dispatchTimeMax"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "GetItFast", activeListing.getString("getItFast"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "GiftIcon", activeListing.getString("giftIcon"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "HitCounter", activeListing.getString("hitCounter"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "IncludeRecommendations", "false", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "Location", activeListing.getString("location"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "PaymentMethods", activeListing.getString("paymentMethods"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "PostCheckoutExperienceEnabled", activeListing.getString("postCheckoutExpEnabled"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "PrivateListing", activeListing.getString("privateListing"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ShippingTermsInDescription", "true", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "Site", activeListing.getString("site"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ListingType", activeListing.getString("listingType"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ListingDuration", activeListing.getString("listingDuration"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "Currency", activeListing.getString("currency"), rootDoc);
                    
                    
                    if (callName.equals("AddItem")) {
                        UtilXml.addChildElementValue(itemElem, "Quantity", "1", rootDoc);
                        if (activeListing.getBigDecimal("buyItNowPrice").longValue() > 0) {
                            UtilXml.addChildElementValue(itemElem, "BuyItNowPrice", activeListing.getBigDecimal("startPrice").toString(), rootDoc);
                        }
                    } else {
                        UtilXml.addChildElementValue(itemElem, "OutOfStockControl", "true", rootDoc);
                    }
                    
                    UtilXml.addChildElementValue(itemElem, "SKU", activeListing.getString("sku"), rootDoc);
                    
                    //Item>ReturnPolicy -- START
                    Element returnPolicyElem = UtilXml.addChildElement(itemElem, "ReturnPolicy", rootDoc);
                    UtilXml.addChildElementCDATAValue(returnPolicyElem, "Description", activeListing.getString("returnsDescription"), rootDoc);
                    if (activeListing.getString("site").equals("US")) {
                        UtilXml.addChildElementValue(returnPolicyElem, "RefundOption", "MoneyBackOrReplacement", rootDoc);
                    } else {
                        UtilXml.addChildElementValue(returnPolicyElem, "RefundOption", "MoneyBackOrExchange", rootDoc);
                    }
                    
                    UtilXml.addChildElementValue(returnPolicyElem, "ReturnsAcceptedOption", activeListing.getString("returnsAcceptedOption"), rootDoc);
                    UtilXml.addChildElementValue(returnPolicyElem, "ReturnsWithinOption", activeListing.getString("returnsWithinOption"), rootDoc);
                    UtilXml.addChildElementValue(returnPolicyElem, "ShippingCostPaidByOption", activeListing.getString("returnsCostPaidBy"), rootDoc);
                    //Item>ReturnPolicy -- END
                    
                    //Item>PayPal Email address -- START
                    /*GenericValue paypalProductStoreRole = EntityUtil.getFirst(delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStoreId, "roleTypeId", "PAYPAL_ACCOUNT", "thruDate", null)));
                     GenericValue paypalPartyGroup = delegator.findByPrimaryKey("PartyGroup", UtilMisc.toMap("partyId", paypalProductStoreRole.getString("partyId")));*/
                    UtilXml.addChildElementValue(itemElem, "PayPalEmailAddress", activeListing.getString("paypalEmailAddress"), rootDoc);
                    //Item>PayPal Email address -- END
                    
                    //Item>PictureDetails -- START
                    /*GenericValue productPictureExternal = delegator.findByPrimaryKey("ProductPictureExternal", UtilMisc.toMap("productId", productId, "pictureType", "GALLERY", "pictureSeqId", "00001"));
                     String imageLink = delegator.findByPrimaryKey("ProductStoreTag", UtilMisc.toMap("productStoreId", productStoreId, "tagName", "imageLink")).getString("tagValue");*/
                    Element pictureDetailsElem = UtilXml.addChildElement(itemElem, "PictureDetails", rootDoc);
                    UtilXml.addChildElementValue(pictureDetailsElem, "GalleryType", activeListing.getString("picDetGalleryType"), rootDoc);
                    UtilXml.addChildElementValue(pictureDetailsElem, "GalleryURL", activeListing.getString("picDetGalleryUrl"), rootDoc);
                    UtilXml.addChildElementValue(pictureDetailsElem, "PhotoDisplay", activeListing.getString("picDetPhotoDisplay"), rootDoc);
                    UtilXml.addChildElementValue(pictureDetailsElem, "PictureSource", "Vendor", rootDoc);
                    UtilXml.addChildElementValue(pictureDetailsElem, "PictureURL", activeListing.getString("picDetPictureUrl"), rootDoc);
                    //Item>PictureDetails -- END
                    
                    //Item>Description -- START
                    /*String dataResourceId = null;
                     List<GenericValue> productContents = delegator.findByAnd("ProductContent", UtilMisc.toMap("productId", productId, "productContentTypeId", "LONG_DESCRIPTION", "thruDate", null));
                     for (GenericValue productContent : productContents) {   //loop productContents -- START
                     GenericValue content = delegator.findByPrimaryKey("Content", UtilMisc.toMap("contentId", productContent.getString("contentId")));
                     if (content.getString("description").equals(productStoreId)) {
                     dataResourceId = content.getString("dataResourceId");
                     }
                     }   //loop productContents -- END
                     if (dataResourceId == null) {
                     return ServiceUtil.returnError("ProductId " + productId + " does not have any description");
                     }
                     else {  //if dataResourceId is not null -- START
                     GenericValue electronicText = delegator.findByPrimaryKey("ElectronicText", UtilMisc.toMap("dataResourceId", dataResourceId));
                     UtilXml.addChildElementCDATAValue(itemElem, "Description", electronicText.getString("textData"), rootDoc);
                     }   //if dataResourceId is not null -- START*/
                    UtilXml.addChildElementCDATAValue(itemElem, "Description", activeListing.getString("description"), rootDoc);
                    //Item>Description -- END
                    
                    //Item>PrimaryCategory -- START
                    Element primaryCategoryElem = UtilXml.addChildElement(itemElem, "PrimaryCategory", rootDoc);
                    UtilXml.addChildElementValue(primaryCategoryElem, "CategoryID", activeListing.getString("primaryCategoryId"), rootDoc);
                    //Item>PrimaryCategory -- END
                    
                    //Item>StorefrontCategory -- START
                    Element storefrontElem = UtilXml.addChildElement(itemElem, "Storefront", rootDoc);
                    UtilXml.addChildElementValue(storefrontElem, "StoreCategoryID", activeListing.getString("storefrontCategoryId"), rootDoc);
                    //Item>StorefrontCategory -- END
                    
                    //Item>Title -- START
                    UtilXml.addChildElementCDATAValue(itemElem, "Title", activeListing.getString("title"), rootDoc);
                    //Item>Title -- END
                    
                    //Item>ShippingDetails -- START
                    List<GenericValue> domesticShippingServiceOptions = delegator.findByAnd("ActiveListingShipping", UtilMisc.toMap("itemId", itemId, "domestic", "Y"), null, false);
                    List<GenericValue> intShippingServiceOptions = delegator.findByAnd("ActiveListingShipping", UtilMisc.toMap("itemId", itemId, "domestic", "N"), null, false);
                    Element shippingDetailsElem = UtilXml.addChildElement(itemElem, "ShippingDetails", rootDoc);
                    UtilXml.addChildElementValue(shippingDetailsElem, "ShippingType", "Flat", rootDoc);
                    
                    
                    //Item>ShippingDetails>InternationalShippingServiceOption -- START
                    for (GenericValue intShippingServiceOption : intShippingServiceOptions) {   //loop intShippingServiceOptions -- START
                        Element intShippingDetailsElem = UtilXml.addChildElement(shippingDetailsElem, "InternationalShippingServiceOption", rootDoc);
                        //UtilXml.addChildElementValue(intShippingDetailsElem, "FreeShipping", intShippingServiceOption.getString("freeShipping"), rootDoc);
                        UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingService", intShippingServiceOption.getString("shippingServiceName"), rootDoc);
                        UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServiceCost", intShippingServiceOption.getBigDecimal("shippingServiceCost").toString(), rootDoc);
                        UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServiceAdditionalCost", intShippingServiceOption.getBigDecimal("additionalCost").toString(), rootDoc);
                        UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServicePriority", intShippingServiceOption.getString("priority"), rootDoc);
                        UtilXml.addChildElementValue(intShippingDetailsElem, "ShipToLocation", "WorldWide", rootDoc);
                    }   //loop intShippingServiceOptions -- END
                    //Item>ShippingDetails>InternationalShippingServiceOption -- END
                    //Item>ShippingDetails>ShippingServiceOptions -- START
                    for (GenericValue domesticShippingServiceOption : domesticShippingServiceOptions) {   //loop intShippingServiceOptions -- START
                        Element shippingServiceOptionsElem = UtilXml.addChildElement(shippingDetailsElem, "ShippingServiceOptions", rootDoc);
                        String freeShipping = "false";
                        if (domesticShippingServiceOption.getBigDecimal("shippingServiceCost").longValue() == 0) {
                            freeShipping = "true";
                        }
                        UtilXml.addChildElementValue(shippingServiceOptionsElem, "FreeShipping", freeShipping, rootDoc);
                        UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingService", domesticShippingServiceOption.getString("shippingServiceName"), rootDoc);
                        UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServiceCost", domesticShippingServiceOption.getBigDecimal("shippingServiceCost").toString(), rootDoc);
                        UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServiceAdditionalCost", domesticShippingServiceOption.getBigDecimal("additionalCost").toString(), rootDoc);
                        UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServicePriority", domesticShippingServiceOption.getString("priority"), rootDoc);
                    }   //loop intShippingServiceOptions -- END
                    //Item>ShippingDetails>ShippingServiceOptions -- END
                    //Item>ShippingDetails -- END
                    
                    //Item>ItemSpecifics -- START
                    boolean colorSpecificExist = false;
                    Element itemSpecificsElem = UtilXml.addChildElement(itemElem, "ItemSpecifics", rootDoc);
                    List<GenericValue> activeListingItemSpecifics = delegator.findByAnd("ActiveListingItemSpecific", UtilMisc.toMap("itemId", itemId), null, false);
                    for (GenericValue activeListingItemSpecific : activeListingItemSpecifics) { //loop activeListingItemSpecifics -- START
                        Element nameValueListElem = UtilXml.addChildElement(itemSpecificsElem, "NameValueList", rootDoc);
                        UtilXml.addChildElementValue(nameValueListElem, "Name", activeListingItemSpecific.getString("name"), rootDoc);
                        UtilXml.addChildElementValue(nameValueListElem, "Value", activeListingItemSpecific.getString("value"), rootDoc);
                    }   //loop activeListingItemSpecifics -- END
                    //Item>ItemSpecifics -- END
                    
                    
                    //variations -- START
                    boolean hasVariation = false;
                    List<GenericValue> variationListings = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("itemId", itemId), null, false);
                    if (!variationListings.isEmpty()) {
                        hasVariation = true;
                    }
                    
                    if (hasVariation) { //if hasVariation is true -- START
                        //Debug.logError("itemId " + itemId  + " has variation", module);
                        Element variationsElem = UtilXml.addChildElement(itemElem, "Variations", rootDoc);
                        
                        //variations>pictures -- START
                        Element varPicturesElem = UtilXml.addChildElement(variationsElem, "Pictures", rootDoc);
                        List<GenericValue> variationPictureSpecificList = delegator.findByAnd("VariationPictureSpecific", UtilMisc.toMap("itemId", itemId), null, false);
                        String varPicSpecName = EntityUtil.getFirst(variationPictureSpecificList).getString("variationSpecificName");
                        UtilXml.addChildElementValue(varPicturesElem, "VariationSpecificName", varPicSpecName, rootDoc);
                        for (GenericValue variationPictureSpecific : variationPictureSpecificList) {    //loop variationPictureSpecificList -- START
                            Element varSpecPicSetElement = UtilXml.addChildElement(varPicturesElem, "VariationSpecificPictureSet", rootDoc);
                            String varPicSpecValue = variationPictureSpecific.getString("variationSpecificValue");
                            String pictureUrl = variationPictureSpecific.getString("pictureUrl");
                            UtilXml.addChildElementValue(varSpecPicSetElement, "VariationSpecificValue", varPicSpecValue, rootDoc);
                            UtilXml.addChildElementValue(varSpecPicSetElement, "PictureURL", pictureUrl, rootDoc);
                        }   //loop variationPictureSpecificList -- END
                        //variations>pictures -- END
                        
                        //variations>VariationSpecificsSet -- START
                        Element varSpecSetElem = UtilXml.addChildElement(variationsElem, "VariationSpecificsSet", rootDoc);
                        List<GenericValue> variationSpecificsSetList = delegator.findByAnd("VariationSpecificsSet", UtilMisc.toMap("itemId", itemId), null, false);
                        
                        List<String> nameCountList = new ArrayList<String>();
                        for (GenericValue variationSpecificsSet : variationSpecificsSetList) {  //loop variationSpecificsSetList -- START
                            nameCountList.add(variationSpecificsSet.getString("name"));
                        }   //loop variationSpecificsSetList -- END
                        HashSet<String> uniqueNameCountList = new HashSet<String>(nameCountList);
                        for (String uniqueName : uniqueNameCountList) { //loop uniqueNameCountList -- START
                            Element varSpecSetNameValueElem = UtilXml.addChildElement(varSpecSetElem, "NameValueList", rootDoc);
                            UtilXml.addChildElementValue(varSpecSetNameValueElem, "Name", uniqueName, rootDoc);
                            List<GenericValue> varSpecSetValueList = delegator.findByAnd("VariationSpecificsSet", UtilMisc.toMap("itemId", itemId, "name", uniqueName), null, false);
                            for (GenericValue varSpecSetValue : varSpecSetValueList) {  //loop varSpecSetValueList -- START
                                UtilXml.addChildElementValue(varSpecSetNameValueElem, "Value", varSpecSetValue.getString("value"), rootDoc);
                            }   //loop varSpecSetValueList -- END
                        }   //loop uniqueNameCountList -- END
                        //variations>VariationSpecificsSet -- END
                        
                        //variations>Variation -- START
                        for (GenericValue ebayActiveListingVariation : variationListings) { //loop variationListings -- START
                            boolean active = true;
                            String skuVariation = ebayActiveListingVariation.getString("productId");
                            GenericValue productVariation = delegator.findOne("Product", UtilMisc.toMap("productId", skuVariation), false);
                            
                            if (productVariation != null) { //if productVariation is not null -- START
                                Timestamp salesDiscontinuationDate = productVariation.getTimestamp("salesDiscontinuationDate");
                                
                                if (salesDiscontinuationDate != null) { //check discontinued product -- START
                                    active = false;
                                    if (todayTimestamp.before(salesDiscontinuationDate)) {
                                        active = true;
                                        //Debug.logError("salesDiscontinuationDate is before today for " + productId, module);
                                    }
                                }  //check discontinued product -- END
                                else {
                                    active = true;
                                }
                                
                                if (active) {   //if product is active -- START
                                    Element variationElem = UtilXml.addChildElement(variationsElem, "Variation", rootDoc);
                                    UtilXml.addChildElementValue(variationElem, "Quantity", "3", rootDoc);
                                    UtilXml.addChildElementValue(variationElem, "SKU", skuVariation, rootDoc);
                                    if (ebayActiveListingVariation.getBigDecimal("originalPrice") != null) {
                                        UtilXml.addChildElementValue(variationElem, "StartPrice", ebayActiveListingVariation.getString("originalPrice"), rootDoc);
                                    } else {
                                        if (activeListing.getBigDecimal("promoOriginalPrice") != null) {
                                            UtilXml.addChildElementValue(variationElem, "StartPrice", activeListing.getBigDecimal("promoOriginalPrice").toString(), rootDoc);
                                        } else {
                                            UtilXml.addChildElementValue(variationElem, "StartPrice", ebayActiveListingVariation.getString("startPrice"), rootDoc);
                                        }
                                    }
                                    
                                    Element varSpecElem = UtilXml.addChildElement(variationElem, "VariationSpecifics", rootDoc);
                                    List<GenericValue> listingVariationSpecifics = delegator.findByAnd("ListingVariationSpecifics", UtilMisc.toMap("itemId", itemId, "variationSeqId", ebayActiveListingVariation.getString("variationSeqId")), null, false);
                                    for (GenericValue listingVariationSpecific : listingVariationSpecifics) {   //loop listingVariationSpecifics -- START
                                        Element varSpecNameValueElem = UtilXml.addChildElement(varSpecElem, "NameValueList", rootDoc);
                                        UtilXml.addChildElementValue(varSpecNameValueElem, "Name", listingVariationSpecific.getString("varSpecsName"), rootDoc);
                                        UtilXml.addChildElementValue(varSpecNameValueElem, "Value", listingVariationSpecific.getString("varSpecsValue"), rootDoc);
                                    }   //loop listingVariationSpecifics -- END
                                }   //if product is active -- END
                            }   //if productVariation is not null -- END
                        }   //loop variationListings -- END
                        //variations>Variation -- END
                    }   //if hasVariation is true -- END
                    else {  //no variation -- START
                        if (activeListing.getBigDecimal("promoOriginalPrice") != null) {
                            UtilXml.addChildElementValue(itemElem, "StartPrice", activeListing.getBigDecimal("promoOriginalPrice").toString(), rootDoc);
                        } else {
                            UtilXml.addChildElementValue(itemElem, "StartPrice", activeListing.getBigDecimal("startPrice").toString(), rootDoc);
                        }
                        UtilXml.addChildElementValue(itemElem, "Quantity", "3", rootDoc);
                    }   //no variation -- END
                    //variations -- END
                    
                    //Item -- END
                    
                    //Building XML -- END
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    Debug.logError(requestXMLcode, module);
                    
                    /*String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                    //Debug.logError(responseXML, module);
                    
                    Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                    Element elemResponse = docResponse.getDocumentElement();
                    String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                    String newItemId = UtilXml.childElementValue(elemResponse, "ItemID", null);
                    
                    if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                        List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                        Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                        StringBuffer errorMessage = new StringBuffer();
                        while (errorElementsElemIter.hasNext()) {
                            Element errorElement = errorElementsElemIter.next();
                            String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                            String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                            errorMessage.append(shortMessage + " - " + longMessage);
                            FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/relistActiveListingSingle-" + itemId + ".xml", true);
                            f1.write(responseXML);
                            f1.close();
                        }
                    }   //if ack failure -- END
                    else {
                        result.put("newItemId", newItemId);
                    }*/
                }   //if listingType is not PersonalOffer -- END
                //Relist the listing -- END
            }   //if activeListingLists is not empty -- START
            else {  //if activeListingLists is empty -- START
                result.put("ebayErrorMessage", "ItemId not found in EbayActiveListing");
            }   //if activeListingLists is empty -- START
            
        }   //main Try - END
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return result;
        
    }   //relistActiveListingSingle
    
    public static Map<String, Object> tempBulkSendEbayMessage(DispatchContext dctx, Map context)
	throws GenericEntityException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        //String eBayItemId = (String) context.get("eBayItemId");
        //String eBayUserId = (String) context.get("eBayUserId");
        String questionType = "General";
        String subjectAcc = productStoreId;
        if (productStoreId.equals("bellyanna-acc")) {
            subjectAcc = "bellyanna";
        }
        String messageSubject = "Important Shipping notification from " + subjectAcc;
        //String messageBody = (String) context.get("messageBody");
        Map mapAccount = FastMap.newInstance();
        Map result = ServiceUtil.returnSuccess();
        
        Calendar fromDay = Calendar.getInstance();
        Calendar toDay = Calendar.getInstance();
        fromDay.set(Calendar.DATE, fromDay.get(Calendar.DATE) - 40);
        toDay.set(Calendar.DATE, toDay.get(Calendar.DATE) - 10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        //Date resultDate = fromDay.getTime();
        //Debug.logError("FromDay is " + sdf.format(fromDay.getTime()).toString(), module);
        //Debug.logError("ToDay is " + sdf.format(toDay.getTime()).toString(), module);
        Timestamp fromDate = Timestamp.valueOf(sdf.format(fromDay.getTime()));
        Timestamp toDate = Timestamp.valueOf(sdf.format(toDay.getTime()));
        
        try {
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "AddMemberMessageAAQToPartner");
            
            GenericValue emailTemplate = delegator.findOne("EbayEmailTemplate", UtilMisc.toMap("emailTemplateId", "9000", "emailTemplateTypeId", "TEMP"), false);
            String messageBody = emailTemplate.getString("content");
            
            
            //Populating mapAccount with necessary data -- START
            
            mapAccount.put("questionType", questionType);
            mapAccount.put("messageSubject", messageSubject);
            mapAccount.put("messageBody", messageBody);
            //Populating mapAccount with necessary data -- END
            Debug.logError("question type" + questionType, module);
            Debug.logError("messageSubject" + messageSubject, module);
            Debug.logError("messageBody" + messageBody, module);
            
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("entryDate",EntityOperator.GREATER_THAN ,fromDate),
                                                                                      EntityCondition.makeCondition("entryDate",EntityOperator.LESS_THAN ,toDate),
                                                                                      EntityCondition.makeCondition("salesChannelEnumId",EntityOperator.EQUALS ,"EBAY_SALES_CHANNEL"),
                                                                                      EntityCondition.makeCondition("productStoreId",EntityOperator.EQUALS , productStoreId),
                                                                                      EntityCondition.makeCondition("orderId",EntityOperator.NOT_LIKE ,"%-BA%"),
                                                                                      EntityCondition.makeCondition("orderId",EntityOperator.NOT_LIKE ,"%R")
                                                                                      ));
            List<GenericValue> orderHeaders = delegator.findList("OrderHeader", condition, null, null, null, false);
            int count = 1;
            for (GenericValue orderHeader : orderHeaders) { //loop orderHeaders -- START
                String orderId = orderHeader.getString("orderId");
                Debug.logError("Process orderID " + orderId, module);
                GenericValue oiaItemNumber = delegator.findOne("OrderItemAttribute", UtilMisc.toMap("orderId", orderId , "attrName", "eBay Item Number", "orderItemSeqId", "00001"), false);
                String ebayItemId = oiaItemNumber.getString("attrValue");
                GenericValue ebayContactMech = EntityUtil.getFirst(delegator.findByAnd("OrderContactMech", UtilMisc.toMap("orderId", orderId, "contactMechPurposeTypeId", "EBAY_USER_ID"), null, false));
                GenericValue eBayUserId = delegator.findOne("ContactMech", UtilMisc.toMap("contactMechId", ebayContactMech.getString("contactMechId")), false);
                String userEmail = eBayUserId.getString("infoString");
                
                if (userEmail != null) {    //if userEmail is not null -- START
                    //mapAccount.put("eBayItemId", ebayItemId);
                    //mapAccount.put("eBayUserId", ebayUserId);
                    
                    //Map sendEmail = dispatcher.runSync("sendMail", UtilMisc.toMap("sendTo", userEmail, "subject", messageSubject, "body", messageBody, "sendFrom", "info@bellyanna.com", "userLogin", userLogin));
                    
                    Map sendMessage = dispatcher.runSync(
                                                     "TradingApiAddMemberMessageAAQToPartnerRequest",
                                                     UtilMisc.toMap(
                                                                    "productStoreId", productStoreId,
                                                                    "eBayItemId", ebayItemId,
                                                                    "eBayUserId", userEmail,
                                                                    "questionType", "Shipping",
                                                                    "messageSubject", messageSubject.toString(),
                                                                    "messageBody", messageBody.toString(),
                                                                    "userLogin", userLogin
                                                                    )
                                                     );

                    if (ServiceUtil.isError(sendMessage)) {
                        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/bulkSendEbayMessageTempError.log", true);
                        f1.write("product Store ID: " + productStoreId + ", failed to send message for orderId " + orderId + "\n");
                        f1.close();
                    }
                }   //if userEmail is not null -- END
                
                
                /*String requestXMLcode = requestXML.addMemberMessageAAQToPartnerRequestRequestXML(mapAccount);
                 //Debug.logError(requestXMLcode, module);
                 String responseXML = sendRequestXMLtoEbay(mapAccount, requestXMLcode.toString());
                 //Debug.logError(responseXML, module);
                 
                 
                 
                 //Building document to read responseXML
                 Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                 Element elemResponse = docResponse.getDocumentElement();
                 String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                 
                 if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                 List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                 Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                 StringBuffer errorMessage = new StringBuffer();
                 while (errorElementsElemIter.hasNext()) {
                 Element errorElement = errorElementsElemIter.next();
                 String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                 String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                 errorMessage.append(shortMessage + " - " + longMessage);
                 FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/eBayResponseXML/bulkSendEbayMessageTempError.log", true);
                 f1.write("product Store ID: " + productStoreId + ", failed to send message for orderId " + orderId + ": " + errorMessage + "\n");
                 f1.close();
                 }
                 Debug.logError(orderId + " failed", module);
                 }   //if ack failure -- END*/
                count++;
            }   //loop orderHeaders -- END
            Debug.logError("Finished processed " + count + " orders", module);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //tempBulkSendEbayMessage
    
    public static Boolean isFeedbackReceived(Delegator delegator, Map mapContent, String responseXML)
	throws GenericEntityException {
        
        boolean isFeedbackReceived = false;
        try {   //main Try -- START
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
            
            if (ack.equals("Success")) {    //if ack success -- START
                List<? extends Element> feedbackDetailArrayElements = UtilXml.childElementList(elemResponse, "FeedbackDetailArray");
                Iterator<? extends Element> feedbackDetailArrayElemIter = feedbackDetailArrayElements.iterator();
                while (feedbackDetailArrayElemIter.hasNext()) { //loop feedbackDetailArray -- START
                    Element feedbackDetailArrayElement = feedbackDetailArrayElemIter.next();
                    List<? extends Element> feedbackDetailElements = UtilXml.childElementList(feedbackDetailArrayElement, "FeedbackDetail");
                    Iterator<? extends Element> feedbackDetailElemIter = feedbackDetailElements.iterator();
                    while (feedbackDetailElemIter.hasNext()) {  //loop feedbackDetail -- START
                        Element feedbackDetailElement = feedbackDetailElemIter.next();
                        String commentingUser= UtilXml.childElementValue(feedbackDetailElement, "CommentingUser", null);
                        if (commentingUser.equals(mapContent.get("eBayUserId"))) {
                            isFeedbackReceived = true;
                        }
                    }   //loop feedbackDetail -- END
                }   //loop feedbackDetailArray -- END
            }   //if ack success -- END
            else {  //if ack failure -- START
                List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                StringBuffer errorMessage = new StringBuffer();
                while (errorElementsElemIter.hasNext()) {   //loop errorMessage -- START
                    Element errorElement = errorElementsElemIter.next();
                    String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                    String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                    errorMessage.append(shortMessage + " - " + longMessage);
                    if (errorMessage.toString().toLowerCase().matches(".*user.* not found.*")) {
                        isFeedbackReceived = true;
                    }
                }   //loop errorMessage -- END
            }   //if ack failure -- END
        }   //main Try -- END
        catch (Exception e) {
            e.printStackTrace();
        }
        return isFeedbackReceived;
    }
    
    public static Map<String, Object> relistActiveListingSingleJacky (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String itemId = (String) context.get("itemId");
        Map result = ServiceUtil.returnSuccess();
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
        Timestamp todayTimestamp = Timestamp.valueOf(sdf.format(now.getTime()));
        
        try {   //main Try - START
            Map mapAccount = FastMap.newInstance();
            List<GenericValue> activeListingLists = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false);
            if (!activeListingLists.isEmpty()) {    //if activeListingLists is not empty -- START
                GenericValue activeListing = EntityUtil.getFirst(activeListingLists);
                String productStoreId = activeListing.getString("productStoreId");
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                GenericValue listingOpt = productStore.getRelatedOne("ProductStoreListingSetting", false);
                mapAccount = common.accountInfo(delegator, productStore);
                
                //Relist the listing -- START
                String listingType = activeListing.getString("listingType");
                
                if (!listingType.equals("PersonalOffer")) { //if listingType is not PersonalOffer -- START
                    String callName = null;
                    
                    if (listingType.equals("Chinese")) {
                        callName = "AddItem";
                    }
                    else if (listingType.equals("FixedPriceItem") || listingType.equals("StoresFixedPrice")) {
                        callName = "AddFixedPriceItem";
                    }
                    
                    mapAccount.put("callName", callName);
                    
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    //Item -- START
                    String getItFast = listingOpt.getString("getItFast");
                    if (!getItFast.isEmpty() && getItFast.equals("Y") ) {
                        getItFast = "true";
                    } else {
                        getItFast = "false";
                    }
                    String postCoExpEnabled = listingOpt.getString("postCoExpEnabled");
                    if (!postCoExpEnabled.isEmpty() && postCoExpEnabled.equals("Y") ) {
                        postCoExpEnabled = "true";
                    } else {
                        postCoExpEnabled = "false";
                    }
                    String privateListing = listingOpt.getString("privateListing");
                    if (!privateListing.isEmpty() && privateListing.equals("Y") ) {
                        privateListing = "true";
                    } else {
                        privateListing = "false";
                    }
                    
                    Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ConditionID", activeListing.getString("conditionId"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "Country", listingOpt.getString("country"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "DispatchTimeMax", listingOpt.getString("dispatchTimeMax"), rootDoc);
                    
                    UtilXml.addChildElementValue(itemElem, "GetItFast", getItFast, rootDoc);
                    UtilXml.addChildElementValue(itemElem, "GiftIcon", listingOpt.getString("giftIcon"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "HitCounter", listingOpt.getString("hitCounter"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "IncludeRecommendations", "false", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "Location", listingOpt.getString("location"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "PaymentMethods", listingOpt.getString("paymentMethods"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "PostCheckoutExperienceEnabled", postCoExpEnabled, rootDoc);
                    UtilXml.addChildElementValue(itemElem, "PrivateListing", privateListing, rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ShippingTermsInDescription", "true", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "Site", listingOpt.getString("site"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ListingType", activeListing.getString("listingType"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ListingDuration", activeListing.getString("listingDuration"), rootDoc);
                    UtilXml.addChildElementValue(itemElem, "Currency", activeListing.getString("currency"), rootDoc);
                    
                    
                    if (callName.equals("AddItem")) {
                        UtilXml.addChildElementValue(itemElem, "Quantity", "1", rootDoc);
                        if (activeListing.getBigDecimal("buyItNowPrice").longValue() > 0) {
                            UtilXml.addChildElementValue(itemElem, "BuyItNowPrice", activeListing.getBigDecimal("startPrice").toString(), rootDoc);
                        }
                    } else {
                        UtilXml.addChildElementValue(itemElem, "OutOfStockControl", "true", rootDoc);
                    }
                    
                    UtilXml.addChildElementValue(itemElem, "SKU", activeListing.getString("sku"), rootDoc);
                    
                    //Item>ReturnPolicy -- START
                    Element returnPolicyElem = UtilXml.addChildElement(itemElem, "ReturnPolicy", rootDoc);
                    UtilXml.addChildElementValue(returnPolicyElem, "Description", listingOpt.getString("returnDescription"), rootDoc);
                    if (activeListing.getString("site").equals("US")) {
                        UtilXml.addChildElementValue(returnPolicyElem, "RefundOption", "MoneyBackOrReplacement", rootDoc);
                    } else {
                        UtilXml.addChildElementValue(returnPolicyElem, "RefundOption", "MoneyBackOrExchange", rootDoc);
                    }
                    
                    UtilXml.addChildElementValue(returnPolicyElem, "ReturnsAcceptedOption", listingOpt.getString("returnsAcceptedOption"), rootDoc);
                    UtilXml.addChildElementValue(returnPolicyElem, "ReturnsWithinOption", listingOpt.getString("returnsWithinOption"), rootDoc);
                    UtilXml.addChildElementValue(returnPolicyElem, "ShippingCostPaidByOption", listingOpt.getString("shipCostPaidBy"), rootDoc);
                    //Item>ReturnPolicy -- END
                    
                    //Item>PayPal Email address -- START
                    /*GenericValue paypalProductStoreRole = EntityUtil.getFirst(delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStoreId, "roleTypeId", "PAYPAL_ACCOUNT", "thruDate", null)));
                     GenericValue paypalPartyGroup = delegator.findByPrimaryKey("PartyGroup", UtilMisc.toMap("partyId", paypalProductStoreRole.getString("partyId")));*/
                    UtilXml.addChildElementValue(itemElem, "PayPalEmailAddress", "dee.pan@live.cn", rootDoc);
                    //Item>PayPal Email address -- END
                    
                    //Item>PictureDetails -- START
                    /*GenericValue productPictureExternal = delegator.findByPrimaryKey("ProductPictureExternal", UtilMisc.toMap("productId", productId, "pictureType", "GALLERY", "pictureSeqId", "00001"));
                     String imageLink = delegator.findByPrimaryKey("ProductStoreTag", UtilMisc.toMap("productStoreId", productStoreId, "tagName", "imageLink")).getString("tagValue");*/
                    Element pictureDetailsElem = UtilXml.addChildElement(itemElem, "PictureDetails", rootDoc);
                    UtilXml.addChildElementValue(pictureDetailsElem, "GalleryType", activeListing.getString("picDetGalleryType"), rootDoc);
                    UtilXml.addChildElementValue(pictureDetailsElem, "GalleryURL", activeListing.getString("picDetGalleryUrl"), rootDoc);
                    UtilXml.addChildElementValue(pictureDetailsElem, "PhotoDisplay", activeListing.getString("picDetPhotoDisplay"), rootDoc);
                    UtilXml.addChildElementValue(pictureDetailsElem, "PictureSource", "Vendor", rootDoc);
                    UtilXml.addChildElementValue(pictureDetailsElem, "PictureURL", activeListing.getString("picDetPictureUrl"), rootDoc);
                    //Item>PictureDetails -- END
                    
                    //Item>Description -- START
                    /*String dataResourceId = null;
                     List<GenericValue> productContents = delegator.findByAnd("ProductContent", UtilMisc.toMap("productId", productId, "productContentTypeId", "LONG_DESCRIPTION", "thruDate", null));
                     for (GenericValue productContent : productContents) {   //loop productContents -- START
                     GenericValue content = delegator.findByPrimaryKey("Content", UtilMisc.toMap("contentId", productContent.getString("contentId")));
                     if (content.getString("description").equals(productStoreId)) {
                     dataResourceId = content.getString("dataResourceId");
                     }
                     }   //loop productContents -- END
                     if (dataResourceId == null) {
                     return ServiceUtil.returnError("ProductId " + productId + " does not have any description");
                     }
                     else {  //if dataResourceId is not null -- START
                     GenericValue electronicText = delegator.findByPrimaryKey("ElectronicText", UtilMisc.toMap("dataResourceId", dataResourceId));
                     UtilXml.addChildElementCDATAValue(itemElem, "Description", electronicText.getString("textData"), rootDoc);
                     }   //if dataResourceId is not null -- START*/
                    UtilXml.addChildElementCDATAValue(itemElem, "Description", activeListing.getString("description"), rootDoc);
                    //Item>Description -- END
                    
                    //Item>PrimaryCategory -- START
                    Element primaryCategoryElem = UtilXml.addChildElement(itemElem, "PrimaryCategory", rootDoc);
                    UtilXml.addChildElementValue(primaryCategoryElem, "CategoryID", activeListing.getString("primaryCategoryId"), rootDoc);
                    //Item>PrimaryCategory -- END
                    
                    //Item>StorefrontCategory -- START
                    Element storefrontElem = UtilXml.addChildElement(itemElem, "Storefront", rootDoc);
                    UtilXml.addChildElementValue(storefrontElem, "StoreCategoryID", "0", rootDoc);
                    //Item>StorefrontCategory -- END
                    
                    //Item>Title -- START
                    UtilXml.addChildElementCDATAValue(itemElem, "Title", activeListing.getString("title"), rootDoc);
                    //Item>Title -- END
                    
                    //Item>ShippingDetails -- START
                    List<GenericValue> domesticShippingServiceOptions = delegator.findByAnd("ActiveListingShipping", UtilMisc.toMap("itemId", itemId, "domestic", "Y"), null, false);
                    List<GenericValue> intShippingServiceOptions = delegator.findByAnd("ActiveListingShipping", UtilMisc.toMap("itemId", itemId, "domestic", "N"), null, false);
                    Element shippingDetailsElem = UtilXml.addChildElement(itemElem, "ShippingDetails", rootDoc);
                    UtilXml.addChildElementValue(shippingDetailsElem, "ShippingType", "Flat", rootDoc);
                    
                    
                    //Item>ShippingDetails>InternationalShippingServiceOption -- START
                    for (GenericValue intShippingServiceOption : intShippingServiceOptions) {   //loop intShippingServiceOptions -- START
                        Element intShippingDetailsElem = UtilXml.addChildElement(shippingDetailsElem, "InternationalShippingServiceOption", rootDoc);
                        //UtilXml.addChildElementValue(intShippingDetailsElem, "FreeShipping", intShippingServiceOption.getString("freeShipping"), rootDoc);
                        UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingService", intShippingServiceOption.getString("shippingServiceName"), rootDoc);
                        UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServiceCost", intShippingServiceOption.getBigDecimal("shippingServiceCost").toString(), rootDoc);
                        UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServiceAdditionalCost", intShippingServiceOption.getBigDecimal("additionalCost").toString(), rootDoc);
                        UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServicePriority", intShippingServiceOption.getString("priority"), rootDoc);
                        UtilXml.addChildElementValue(intShippingDetailsElem, "ShipToLocation", "WorldWide", rootDoc);
                    }   //loop intShippingServiceOptions -- END
                    //Item>ShippingDetails>InternationalShippingServiceOption -- END
                    //Item>ShippingDetails>ShippingServiceOptions -- START
                    for (GenericValue domesticShippingServiceOption : domesticShippingServiceOptions) {   //loop intShippingServiceOptions -- START
                        Element shippingServiceOptionsElem = UtilXml.addChildElement(shippingDetailsElem, "ShippingServiceOptions", rootDoc);
                        String freeShipping = "false";
                        if (domesticShippingServiceOption.getBigDecimal("shippingServiceCost").longValue() == 0) {
                            freeShipping = "true";
                        }
                        UtilXml.addChildElementValue(shippingServiceOptionsElem, "FreeShipping", freeShipping, rootDoc);
                        UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingService", domesticShippingServiceOption.getString("shippingServiceName"), rootDoc);
                        UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServiceCost", domesticShippingServiceOption.getBigDecimal("shippingServiceCost").toString(), rootDoc);
                        UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServiceAdditionalCost", domesticShippingServiceOption.getBigDecimal("additionalCost").toString(), rootDoc);
                        UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServicePriority", domesticShippingServiceOption.getString("priority"), rootDoc);
                    }   //loop intShippingServiceOptions -- END
                    //Item>ShippingDetails>ShippingServiceOptions -- END
                    //Item>ShippingDetails -- END
                    
                    //Item>ItemSpecifics -- START
                    boolean colorSpecificExist = false;
                    Element itemSpecificsElem = UtilXml.addChildElement(itemElem, "ItemSpecifics", rootDoc);
                    List<GenericValue> activeListingItemSpecifics = delegator.findByAnd("ActiveListingItemSpecific", UtilMisc.toMap("itemId", itemId), null, false);
                    for (GenericValue activeListingItemSpecific : activeListingItemSpecifics) { //loop activeListingItemSpecifics -- START
                        Element nameValueListElem = UtilXml.addChildElement(itemSpecificsElem, "NameValueList", rootDoc);
                        UtilXml.addChildElementValue(nameValueListElem, "Name", activeListingItemSpecific.getString("name"), rootDoc);
                        UtilXml.addChildElementValue(nameValueListElem, "Value", activeListingItemSpecific.getString("value"), rootDoc);
                    }   //loop activeListingItemSpecifics -- END
                    //Item>ItemSpecifics -- END
                    
                    
                    //variations -- START
                    boolean hasVariation = false;
                    List<GenericValue> variationListings = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("itemId", itemId), null, false);
                    if (!variationListings.isEmpty()) {
                        hasVariation = true;
                    }
                    
                    if (hasVariation) { //if hasVariation is true -- START
                        //Debug.logError("itemId " + itemId  + " has variation", module);
                        Element variationsElem = UtilXml.addChildElement(itemElem, "Variations", rootDoc);
                        
                        //variations>pictures -- START
                        Element varPicturesElem = UtilXml.addChildElement(variationsElem, "Pictures", rootDoc);
                        List<GenericValue> variationPictureSpecificList = delegator.findByAnd("VariationPictureSpecific", UtilMisc.toMap("itemId", itemId), null, false);
                        String varPicSpecName = EntityUtil.getFirst(variationPictureSpecificList).getString("variationSpecificName");
                        UtilXml.addChildElementValue(varPicturesElem, "VariationSpecificName", varPicSpecName, rootDoc);
                        for (GenericValue variationPictureSpecific : variationPictureSpecificList) {    //loop variationPictureSpecificList -- START
                            Element varSpecPicSetElement = UtilXml.addChildElement(varPicturesElem, "VariationSpecificPictureSet", rootDoc);
                            String varPicSpecValue = variationPictureSpecific.getString("variationSpecificValue");
                            String pictureUrl = variationPictureSpecific.getString("pictureUrl");
                            UtilXml.addChildElementValue(varSpecPicSetElement, "VariationSpecificValue", varPicSpecValue, rootDoc);
                            UtilXml.addChildElementValue(varSpecPicSetElement, "PictureURL", pictureUrl, rootDoc);
                        }   //loop variationPictureSpecificList -- END
                        //variations>pictures -- END
                        
                        //variations>VariationSpecificsSet -- START
                        Element varSpecSetElem = UtilXml.addChildElement(variationsElem, "VariationSpecificsSet", rootDoc);
                        List<GenericValue> variationSpecificsSetList = delegator.findByAnd("VariationSpecificsSet", UtilMisc.toMap("itemId", itemId), null, false);
                        
                        List<String> nameCountList = new ArrayList<String>();
                        for (GenericValue variationSpecificsSet : variationSpecificsSetList) {  //loop variationSpecificsSetList -- START
                            nameCountList.add(variationSpecificsSet.getString("name"));
                        }   //loop variationSpecificsSetList -- END
                        HashSet<String> uniqueNameCountList = new HashSet<String>(nameCountList);
                        for (String uniqueName : uniqueNameCountList) { //loop uniqueNameCountList -- START
                            Element varSpecSetNameValueElem = UtilXml.addChildElement(varSpecSetElem, "NameValueList", rootDoc);
                            UtilXml.addChildElementValue(varSpecSetNameValueElem, "Name", uniqueName, rootDoc);
                            List<GenericValue> varSpecSetValueList = delegator.findByAnd("VariationSpecificsSet", UtilMisc.toMap("itemId", itemId, "name", uniqueName), null, false);
                            for (GenericValue varSpecSetValue : varSpecSetValueList) {  //loop varSpecSetValueList -- START
                                UtilXml.addChildElementValue(varSpecSetNameValueElem, "Value", varSpecSetValue.getString("value"), rootDoc);
                            }   //loop varSpecSetValueList -- END
                        }   //loop uniqueNameCountList -- END
                        //variations>VariationSpecificsSet -- END
                        
                        //variations>Variation -- START
                        for (GenericValue ebayActiveListingVariation : variationListings) { //loop variationListings -- START
                            boolean active = true;
                            String skuVariation = ebayActiveListingVariation.getString("productId");
                            GenericValue productVariation = delegator.findOne("Product", UtilMisc.toMap("productId", skuVariation), false);
                            
                            if (productVariation != null) { //if productVariation is not null -- START
                                Timestamp salesDiscontinuationDate = productVariation.getTimestamp("salesDiscontinuationDate");
                                
                                if (salesDiscontinuationDate != null) { //check discontinued product -- START
                                    active = false;
                                    if (todayTimestamp.before(salesDiscontinuationDate)) {
                                        active = true;
                                        //Debug.logError("salesDiscontinuationDate is before today for " + productId, module);
                                    }
                                }  //check discontinued product -- END
                                else {
                                    active = true;
                                }
                                
                                if (active) {   //if product is active -- START
                                    Element variationElem = UtilXml.addChildElement(variationsElem, "Variation", rootDoc);
                                    UtilXml.addChildElementValue(variationElem, "Quantity", "3", rootDoc);
                                    UtilXml.addChildElementValue(variationElem, "SKU", skuVariation, rootDoc);
                                    UtilXml.addChildElementValue(variationElem, "StartPrice", ebayActiveListingVariation.getString("startPrice"), rootDoc);
                                    
                                    Element varSpecElem = UtilXml.addChildElement(variationElem, "VariationSpecifics", rootDoc);
                                    List<GenericValue> listingVariationSpecifics = delegator.findByAnd("ListingVariationSpecifics", UtilMisc.toMap("itemId", itemId, "variationSeqId", ebayActiveListingVariation.getString("variationSeqId")), null, false);
                                    for (GenericValue listingVariationSpecific : listingVariationSpecifics) {   //loop listingVariationSpecifics -- START
                                        Element varSpecNameValueElem = UtilXml.addChildElement(varSpecElem, "NameValueList", rootDoc);
                                        UtilXml.addChildElementValue(varSpecNameValueElem, "Name", listingVariationSpecific.getString("varSpecsName"), rootDoc);
                                        UtilXml.addChildElementValue(varSpecNameValueElem, "Value", listingVariationSpecific.getString("varSpecsValue"), rootDoc);
                                    }   //loop listingVariationSpecifics -- END
                                }   //if product is active -- END
                            }   //if productVariation is not null -- END
                        }   //loop variationListings -- END
                        //variations>Variation -- END
                    }   //if hasVariation is true -- END
                    else {  //no variation -- START
                        UtilXml.addChildElementValue(itemElem, "StartPrice", activeListing.getBigDecimal("startPrice").toString(), rootDoc);
                        UtilXml.addChildElementValue(itemElem, "Quantity", "3", rootDoc);
                    }   //no variation -- END
                    //variations -- END
                    
                    //Item -- END
                    
                    //Building XML -- END
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    Debug.logError(requestXMLcode, module);
                    
                    String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                    //Debug.logError(responseXML, module);
                    
                    Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                    Element elemResponse = docResponse.getDocumentElement();
                    String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                    String newItemId = UtilXml.childElementValue(elemResponse, "ItemID", null);
                    
                    if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                        List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                        Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                        StringBuffer errorMessage = new StringBuffer();
                        while (errorElementsElemIter.hasNext()) {
                            Element errorElement = errorElementsElemIter.next();
                            String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                            String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                            errorMessage.append(shortMessage + " - " + longMessage);
                            FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/relistActiveListingSingleJacky-" + itemId + ".xml", true);
                            f1.write(responseXML);
                            f1.close();
                        }
                    }   //if ack failure -- END
                    else {
                        result.put("newItemId", newItemId);
                    }
                }   //if listingType is not PersonalOffer -- END
                //Relist the listing -- END
            }   //if activeListingLists is not empty -- START
            else {  //if activeListingLists is empty -- START
                result.put("ebayErrorMessage", "ItemId not found in EbayActiveListing");
            }   //if activeListingLists is empty -- START
            
        }   //main Try - END
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return result;
        
    }   //relistActiveListingSingleJacky
    
    public static Map<String, Object> smartRelistJacky (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String productStoreId = (String) context.get("productStoreId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String today = sdf.format(now.getTime());
        
        Map result = ServiceUtil.returnSuccess();

        try {
            FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/jackyRelist.log", true);
            f1.write(today + " " + productStoreId + ": Start autoRelist" + "\n");
            List<GenericValue> toBeListedList = delegator.findList("ToBeListed", null, null, null, null, false);
            for (GenericValue toBeListed : toBeListedList) {
                String itemId = toBeListed.getString("itemId");
                Map relist = dispatcher.runSync("JackyRelist", UtilMisc.toMap("itemId", itemId, "userLogin", userLogin));
                if (ServiceUtil.isSuccess(relist)) {
                    f1.write("Successfully relist " + itemId + ", new Item ID: " + relist.get("newItemId"));
                } else {
                    f1.write("Failed relist " + itemId + ". " + relist.get("ebayErrorMessage"));
                }
                
            }
            f1.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        return result;
        
    }
    
    public static Map<String, Object> changeEbayShippingBulk (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        int count = 0;
        
        try {   //main try -- START
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "PENDING")
                                                                                      ));
            List<GenericValue> ebayChangeShippingList = delegator.findList("EbayChangeShipping", condition, null, null, null, false);
            for (GenericValue ebayChangeShipping : ebayChangeShippingList) {    //loop ebayChangeShipping -- START
                String itemId = ebayChangeShipping.getString("itemId");
                String productStoreId = ebayChangeShipping.getString("ebayAccount");
                String site = ebayChangeShipping.getString("site");
                String domesticName = ebayChangeShipping.getString("domesticName");
                String domesticPrice = ebayChangeShipping.getString("domesticPrice");
                String domesticAdditional = ebayChangeShipping.getString("domesticAdditional");
                String intName = ebayChangeShipping.getString("intName");
                String intPrice = ebayChangeShipping.getString("intPrice");
                String intAdditional = ebayChangeShipping.getString("intAdditional");
                String handlingTime = ebayChangeShipping.getString("handlingTime");
                
                Map mapAccount = FastMap.newInstance();
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                mapAccount = common.accountInfo(delegator, productStore);
                mapAccount.put("callName", "ReviseFixedPriceItem");
                GenericValue ebaySiteCode = EntityUtil.getFirst(delegator.findByAnd("EbaySiteCode", UtilMisc.toMap("abbreviation", site), null, false));
                mapAccount.put("siteId", ebaySiteCode.getString("ebaySiteId"));
                mapAccount.put("globalId", ebaySiteCode.getString("ebayGlobalId"));
                
                //Building XML -- START
                Document rootDoc = UtilXml.makeEmptyXmlDocument("ReviseFixedPriceItemRequest");
                Element rootElem = rootDoc.getDocumentElement();
                rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                
                //RequesterCredentials -- START
                Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                //RequesterCredentials -- END
                
                //Item -- START
                Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
                UtilXml.addChildElementValue(itemElem, "ItemID", itemId, rootDoc);
                UtilXml.addChildElementValue(itemElem, "OutOfStockControl", "true", rootDoc);
                if (handlingTime != null) {
                    UtilXml.addChildElementValue(itemElem, "DispatchTimeMax", handlingTime, rootDoc);
                }
                
                //Item>ShippingDetails -- START
                Element shippingDetailsElem = UtilXml.addChildElement(itemElem, "ShippingDetails", rootDoc);
                UtilXml.addChildElementValue(shippingDetailsElem, "ShippingType", "Flat", rootDoc);
                
                //Item>ShippingDetails>ShippingServiceOptions -- START
                Element shippingServiceOptionsElem = UtilXml.addChildElement(shippingDetailsElem, "ShippingServiceOptions", rootDoc);
                String freeShipping = "false";
                
                if (Float.valueOf(domesticPrice).floatValue() == 0.00) {
                    freeShipping = "true";
                }
                UtilXml.addChildElementValue(shippingServiceOptionsElem, "FreeShipping", freeShipping, rootDoc);
                UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingService", domesticName, rootDoc);
                UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServiceCost", domesticPrice, rootDoc);
                UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServiceAdditionalCost", domesticAdditional, rootDoc);
                UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServicePriority", "1", rootDoc);
                //Item>ShippingDetails>ShippingServiceOptions -- END
                
                //Item>ShippingDetails>InternationalShippingServiceOption -- START
                Element intShippingDetailsElem = UtilXml.addChildElement(shippingDetailsElem, "InternationalShippingServiceOption", rootDoc);
                UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingService", intName, rootDoc);
                UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServiceCost", intPrice, rootDoc);
                UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServiceAdditionalCost", intAdditional, rootDoc);
                UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServicePriority", "1", rootDoc);
                UtilXml.addChildElementValue(intShippingDetailsElem, "ShipToLocation", "WorldWide", rootDoc);
                //Item>ShippingDetails>InternationalShippingServiceOption -- END
                
                //Item>ShippingDetails -- END
                
                String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                Debug.logError(requestXMLcode, module);
                String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                //Item -- END
                
                Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                Element elemResponse = docResponse.getDocumentElement();
                String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                String newItemId = UtilXml.childElementValue(elemResponse, "ItemID", null);
                
                if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                    List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                    Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                    StringBuffer errorMessage = new StringBuffer();
                    while (errorElementsElemIter.hasNext()) {
                        Element errorElement = errorElementsElemIter.next();
                        String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                        String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                        errorMessage.append(shortMessage + " - " + longMessage);
                    }
                    Debug.logError("change Shipping failed for Item ID " + itemId + ", errorMessage: " + errorMessage, module);
                    ebayChangeShipping.set("statusId", "FAILED");
                    ebayChangeShipping.set("notes", errorMessage);
                    delegator.store(ebayChangeShipping);
                }   //if ack failure -- END
                else {
                    ebayChangeShipping.set("statusId", "COMPLETED");
                    delegator.store(ebayChangeShipping);
                    Debug.logError("finished changing shipping for itemId " + itemId, module);
                }

                count++;
            }   //loop ebayChangeShipping -- END
        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError("finished changeEbayShippingBulk. " + count + " item ID processed", module);
        return result;
        
    }
    
    public static Map<String, Object> changeEbayShippingSingle (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String itemId = (String) context.get("itemId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        
        try {   //main try -- START
            Map mapAccount = FastMap.newInstance();
            List<GenericValue> activeListingLists = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false);
            if (!activeListingLists.isEmpty()) {    //if activeListingLists is not empty -- START
                GenericValue activeListing = EntityUtil.getFirst(activeListingLists);
                String productStoreId = activeListing.getString("productStoreId");
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                GenericValue toBeChanged = delegator.findOne("ToBeListed", UtilMisc.toMap("itemId", itemId), false);
                mapAccount = common.accountInfo(delegator, productStore);
                
                //Relist the listing -- START
                String listingType = activeListing.getString("listingType");
                
                if (!listingType.equals("PersonalOffer")) { //if listingType is not PersonalOffer -- START
                    String callName = null;
                    
                    if (listingType.equals("Chinese")) {
                        callName = "ReviseItem";
                    }
                    else if (listingType.equals("FixedPriceItem") || listingType.equals("StoresFixedPrice")) {
                        callName = "ReviseFixedPriceItem";
                    }
                    
                    mapAccount.put("callName", callName);
                    
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    //Item -- START
                    Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ItemID", itemId, rootDoc);
                    UtilXml.addChildElementValue(itemElem, "OutOfStockControl", "true", rootDoc);
                    
                    //Item>ShippingDetails -- START
                    Element shippingDetailsElem = UtilXml.addChildElement(itemElem, "ShippingDetails", rootDoc);
                    UtilXml.addChildElementValue(shippingDetailsElem, "ShippingType", "Flat", rootDoc);
                    
                    //Item>ShippingDetails>ShippingServiceOptions -- START
                    Element shippingServiceOptionsElem = UtilXml.addChildElement(shippingDetailsElem, "ShippingServiceOptions", rootDoc);
                    String freeShipping = "false";
                    if (toBeChanged.getBigDecimal("shippingPrice").longValue() == 0) {
                        freeShipping = "true";
                    }
                    UtilXml.addChildElementValue(shippingServiceOptionsElem, "FreeShipping", freeShipping, rootDoc);
                    UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingService", "UK_EconomyShippingFromOutside", rootDoc);
                    UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServiceCost", toBeChanged.getBigDecimal("shippingPrice").toString(), rootDoc);
                    UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServiceAdditionalCost", toBeChanged.getBigDecimal("shippingPrice").toString(), rootDoc);
                    UtilXml.addChildElementValue(shippingServiceOptionsElem, "ShippingServicePriority", "1", rootDoc);
                    //Item>ShippingDetails>ShippingServiceOptions -- END
                    
                    //Item>ShippingDetails>InternationalShippingServiceOption -- START
                    Element intShippingDetailsElem = UtilXml.addChildElement(shippingDetailsElem, "InternationalShippingServiceOption", rootDoc);
                    UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingService", "UK_OtherCourierOrDeliveryInternational", rootDoc);
                    UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServiceCost", toBeChanged.getBigDecimal("shippingPrice").toString(), rootDoc);
                    UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServiceAdditionalCost", toBeChanged.getBigDecimal("shippingPrice").toString(), rootDoc);
                    UtilXml.addChildElementValue(intShippingDetailsElem, "ShippingServicePriority", "1", rootDoc);
                    UtilXml.addChildElementValue(intShippingDetailsElem, "ShipToLocation", "WorldWide", rootDoc);
                    //Item>ShippingDetails>InternationalShippingServiceOption -- END
                    
                    //Item>ShippingDetails -- END
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    //Debug.logError(requestXMLcode, module);
                    String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                    //Item -- END
                    
                    Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                    Element elemResponse = docResponse.getDocumentElement();
                    String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                    String newItemId = UtilXml.childElementValue(elemResponse, "ItemID", null);
                    
                    if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                        List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                        Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                        StringBuffer errorMessage = new StringBuffer();
                        while (errorElementsElemIter.hasNext()) {
                            Element errorElement = errorElementsElemIter.next();
                            String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                            String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                            errorMessage.append(shortMessage + " - " + longMessage);
                            Debug.logError("change Shipping failed for Item ID " + itemId + ", errorMessage: " + errorMessage, module);
                        }
                    }   //if ack failure -- END
                    else {
                        Debug.logError("finished changing shipping for itemId " + itemId, module);
                    }
                    
                }   //if listingType is not PersonalOffer -- END
            }   //if activeListingLists is not empty -- END
            
            
        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        return result;
        
    }   //changeEbayShippingSingle
    
    public static Map<String, Object> changeEbayPriceBulk (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        int count = 0;
        
        try {   //main try -- START
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "PENDING")
                                                                                      ));
            List<GenericValue> distinctEbayChangePriceList = delegator.findList("EbayChangePrice", condition,
                                                                                UtilMisc.toSet("itemId"),
                                                                                UtilMisc.toList("itemId"),
                                                                                new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
            
            for (GenericValue distinctEbayChangePrice : distinctEbayChangePriceList) {  //loop distinctEbayChangePrice -- START
                String itemId = distinctEbayChangePrice.getString("itemId");
                Debug.logError("processing itemID " + itemId, module);
                List<GenericValue> ebayChangePrice = delegator.findByAnd("EbayChangePrice", UtilMisc.toMap("itemId", itemId), null, false);
                GenericValue ebayChangePriceFirst = EntityUtil.getFirst(ebayChangePrice);
                List<GenericValue> activeListing = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false);
                if (UtilValidate.isNotEmpty(activeListing)) {   //if activeListing is not empty -- START
                    //Debug.logError("ebayActiveListing for itemId " + itemId + " is not empty, continue to process", module);
                    GenericValue activeListingProductStore = EntityUtil.getFirst(activeListing);
                    String productStoreId = activeListingProductStore.getString("productStoreId");
                    String site = activeListingProductStore.getString("site");
                    String parentProductId = ebayChangePriceFirst.getString("parentProductId");
                    String parentProductPrice = ebayChangePriceFirst.getString("parentProductPrice");
                    String parentQty = ebayChangePriceFirst.getString("parentQty");
                    String childProductId = ebayChangePriceFirst.getString("childProductId");
                    String childProductPrice = ebayChangePriceFirst.getString("childProductPrice");
                    String childQty = ebayChangePriceFirst.getString("childQty");
                    boolean sendRequest = true;
                    //Debug.logError("productStoreId is  " + productStoreId + " and site is " + site, module);
                    Map mapAccount = FastMap.newInstance();
                    GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                    mapAccount = common.accountInfo(delegator, productStore);
                    mapAccount.put("callName", "ReviseFixedPriceItem");
                    GenericValue ebaySiteCode = EntityUtil.getFirst(delegator.findByAnd("EbaySiteCode", UtilMisc.toMap("ebaySite", site), null, false));
                    mapAccount.put("siteId", ebaySiteCode.getString("ebaySiteId"));
                    mapAccount.put("globalId", ebaySiteCode.getString("ebayGlobalId"));
                    
                    //Debug.logError("start building requestXML", module);
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument("ReviseFixedPriceItemRequest");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    //Item -- START
                    Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ItemID", itemId, rootDoc);
                    List<GenericValue> checkVariationExist = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("itemId", itemId), null, false);
                    if (parentProductId == null && UtilValidate.isEmpty(checkVariationExist)) {
                        parentProductId = childProductId;
                        childProductId = null;
                        parentProductPrice = childProductPrice;
                        childProductPrice = null;
                        parentQty = childQty;
                        childQty = null;
                    }
                    
                    if (parentProductId != null) {
                        UtilXml.addChildElementValue(itemElem, "SKU", parentProductId, rootDoc);
                    }
                    if (parentProductPrice != null) {
                        UtilXml.addChildElementValue(itemElem, "StartPrice", parentProductPrice, rootDoc);
                    }
                    if (parentQty != null) {
                        UtilXml.addChildElementValue(itemElem, "Quantity", parentQty, rootDoc);
                    }
                    
                    if (childProductId != null) {   //if childProduct is not empty -- START
                        if (ebayChangePrice.size() > 1) {   //if ebayChangePrice has multi data -- START
                            Element variationsElem = UtilXml.addChildElement(itemElem, "Variations", rootDoc);
                            for (GenericValue ebayChangePriceSingle : ebayChangePrice) {    //loop ebayChangePrice -- START
                                childProductId = ebayChangePriceSingle.getString("childProductId");
                                childProductPrice = ebayChangePriceSingle.getString("childProductPrice");
                                childQty = ebayChangePriceSingle.getString("childQty");
                                
                                Element variationElem = UtilXml.addChildElement(variationsElem, "Variation", rootDoc);
                                UtilXml.addChildElementValue(variationElem, "SKU", childProductId, rootDoc);
                                UtilXml.addChildElementValue(variationElem, "StartPrice", childProductPrice, rootDoc);
                                if (childQty != null) {
                                    UtilXml.addChildElementValue(variationElem, "Quantity", childQty, rootDoc);
                                }
                                List<GenericValue> listingVariationSpecifics = delegator.findByAnd("ListingVariationSpecifics", UtilMisc.toMap("itemId", itemId, "productId", childProductId), null, false);
                                if (UtilValidate.isNotEmpty(listingVariationSpecifics)) {   //if listingVariationSpecifics is not empty -- START
                                    Element variationSpecificElem = UtilXml.addChildElement(variationElem, "VariationSpecifics", rootDoc);
                                    for (GenericValue listingVariationSpecificsGV : listingVariationSpecifics) {    //loop listingVariationSpecifics -- START
                                        Element nameValueListElem = UtilXml.addChildElement(variationSpecificElem, "NameValueList", rootDoc);
                                        UtilXml.addChildElementValue(nameValueListElem, "Name", listingVariationSpecificsGV.getString("varSpecsName"), rootDoc);
                                        UtilXml.addChildElementValue(nameValueListElem, "Value", listingVariationSpecificsGV.getString("varSpecsValue"), rootDoc);
                                    }   //loop listingVariationSpecifics -- END
                                }   //if listingVariationSpecifics is not empty -- END
                                else {  //if listingVariationSpecifics is empty -- START
                                    sendRequest = false;
                                    ebayChangePriceSingle.set("statusId", "FAILED");
                                    ebayChangePriceSingle.set("notes", "Could not find itemId " + itemId + " with productId " + childProductId + " in ListingVariationSpecifics database");
                                    delegator.store(ebayChangePriceSingle);
                                }   //if listingVariationSpecifics is empty -- END
                            }   //loop ebayChangePrice -- END
                        }   //if ebayChangePrice has multi data -- END
                        else {  //if ebayChangePrice has single data -- START
                            Element variationsElem = UtilXml.addChildElement(itemElem, "Variations", rootDoc);
                            Element variationElem = UtilXml.addChildElement(variationsElem, "Variation", rootDoc);
                            UtilXml.addChildElementValue(variationElem, "SKU", childProductId, rootDoc);
                            UtilXml.addChildElementValue(variationElem, "StartPrice", childProductPrice, rootDoc);
                            if (childQty != null) {
                                UtilXml.addChildElementValue(variationElem, "Quantity", childQty, rootDoc);
                            }
                            List<GenericValue> listingVariationSpecifics = delegator.findByAnd("ListingVariationSpecifics", UtilMisc.toMap("itemId", itemId, "productId", childProductId), null, false);
                            if (UtilValidate.isNotEmpty(listingVariationSpecifics)) {   //if listingVariationSpecifics is not empty -- START
                                Element variationSpecificElem = UtilXml.addChildElement(variationElem, "VariationSpecifics", rootDoc);
                                for (GenericValue listingVariationSpecificsGV : listingVariationSpecifics) {    //loop listingVariationSpecifics -- START
                                    Element nameValueListElem = UtilXml.addChildElement(variationSpecificElem, "NameValueList", rootDoc);
                                    UtilXml.addChildElementValue(nameValueListElem, "Name", listingVariationSpecificsGV.getString("varSpecsName"), rootDoc);
                                    UtilXml.addChildElementValue(nameValueListElem, "Value", listingVariationSpecificsGV.getString("varSpecsValue"), rootDoc);
                                }   //loop listingVariationSpecifics -- END
                            }   //if listingVariationSpecifics is not empty -- END
                            else {  //if listingVariationSpecifics is empty -- START
                                sendRequest = false;
                                ebayChangePriceFirst.set("statusId", "FAILED");
                                ebayChangePriceFirst.set("notes", "Could not find itemId " + itemId + " with productId " + childProductId + " in ListingVariationSpecifics database");
                                delegator.store(ebayChangePriceFirst);
                            }   //if listingVariationSpecifics is empty -- END
                        }   //if ebayChangePrice has single data -- END
                    }   //if childProduct is not empty -- END
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    //Debug.logError(requestXMLcode, module);
                    //Debug.logError("send Request is " + sendRequest, module);
                    //Debug.logError("start sending requestXML to eBay API", module);
                    if (sendRequest) {  //if sendRequest is true -- START
                        String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                        //Debug.logError(responseXML, module);
                        Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                        Element elemResponse = docResponse.getDocumentElement();
                        String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                        
                        if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                            List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                            Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                            StringBuffer errorMessage = new StringBuffer();
                            while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                                Element errorElement = errorElementsElemIter.next();
                                String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                                String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                                errorMessage.append(shortMessage + " - " + longMessage);
                            }   //while errorElement -- END
                            Debug.logError("change eBay price failed for Item ID " + itemId + ", errorMessage: " + errorMessage, module);
                            if (ebayChangePrice.size() > 1) {   //if ebayChangePrice has multi data -- START
                                for (GenericValue ebayChangePriceSingle : ebayChangePrice) {    //loop ebayChangePrice -- START
                                    ebayChangePriceSingle.set("statusId", "FAILED");
                                    ebayChangePriceSingle.set("notes", errorMessage.toString());
                                    delegator.store(ebayChangePriceSingle);
                                }   //loop ebayChangePrice -- END
                            }   //if ebayChangePrice has multi data -- END
                            else {  //if ebayChangePrice has single data -- START
                                ebayChangePriceFirst.set("statusId", "FAILED");
                                ebayChangePriceFirst.set("notes", errorMessage.toString());
                                delegator.store(ebayChangePriceFirst);
                            }   //if ebayChangePrice has single data -- END
                        }   //if ack failure -- END
                        else {  //if ack success -- START
                            Debug.logError("finished changing price for itemId " + itemId, module);
                            if (ebayChangePrice.size() > 1) {   //if ebayChangePrice has multi data -- START
                                for (GenericValue ebayChangePriceSingle : ebayChangePrice) {    //loop ebayChangePrice -- START
                                    ebayChangePriceSingle.set("statusId", "COMPLETED");
                                    delegator.store(ebayChangePriceSingle);
                                }   //loop ebayChangePrice -- END
                            }   //if ebayChangePrice has multi data -- END
                            else {  //if ebayChangePrice has single data -- START
                                ebayChangePriceFirst.set("statusId", "COMPLETED");
                                delegator.store(ebayChangePriceFirst);
                            }   //if ebayChangePrice has single data -- END
                        }   //if ack success -- END
                        count++;
                    }   //if sendRequest is true -- END
                }   //if activeListing is not empty -- END
                else {  //if activeListing is empty -- START
                    for (GenericValue ebayChangePriceNoActive : ebayChangePrice) {  //loop ebayChangePriceNoActive -- START
                        ebayChangePriceNoActive.set("statusId", "FAILED");
                        ebayChangePriceNoActive.set("notes", "No eBay active listing found in database");
                        delegator.store(ebayChangePriceNoActive);
                    }   //loop ebayChangePriceNoActive -- START
                }   //if activeListing is empty -- END
            }   //loop distinctEbayChangePrice -- END
        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError("finished changeEbayPriceBulk. " + count + " item ID processed", module);
        return result;
        
    }   //changeEbayPriceBulk
    
    public static Map<String, Object> setPromotionalSaleDetailsOneTime(DispatchContext dctx, Map context)
	throws GenericEntityException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String storeName = (String) context.get("storeName");
        Map result = ServiceUtil.returnSuccess();
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String today = sdfNow.format(now.getTime());
        
        Calendar toDate = Calendar.getInstance();
        toDate.set(Calendar.DATE, toDate.get(Calendar.DATE) + 1);
        String toDateStr = sdfNow.format(toDate.getTime());
        

        
        try {   //main try -- START
            List<GenericValue> productStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("primaryStoreGroupId", "EBAY", "storeName", storeName), null, false);
            for (GenericValue productStore : productStoreList) {    //loop productStoreList -- START
                String productStoreId = productStore.getString("productStoreId");
                GenericValue productStoreEbaySetting = delegator.findOne("ProductStoreEbaySetting", UtilMisc.toMap("productStoreId", productStoreId), false);
                String autoDiscount = productStoreEbaySetting.getString("autoDiscount");
                String maxDiscount = productStoreEbaySetting.getString("maxDiscount");
                String discountGap = productStoreEbaySetting.getString("discountGap");
                String discountDuration = productStoreEbaySetting.getString("discountDuration");
                String discountEndTime = productStoreEbaySetting.getString("discountEndTime");
                
                double maxDiscountInt = Double.parseDouble(maxDiscount);
                double discountGapInt = Double.parseDouble(discountGap);
                
                Map mapAccount = FastMap.newInstance();
                mapAccount = common.accountInfo(delegator, productStore);
                String callName = "SetPromotionalSale";
                mapAccount.put("callName", callName);
                double i = 5.0;
                while (i <= 15.0) { //loop create discount -- START
                    String discountType = "Percentage";
                    String discountValue = i + "";
                    String promotionalSaleName = "Auto markdown " + i + "% off - " + discountDuration + " day(s)";
                    String promotionalSaleType = "PriceDiscountOnly";
                    String promotionalSaleStartTime = today;
                    String promotionalSaleEndTime = toDateStr;
                    
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    UtilXml.addChildElementValue(rootElem, "Action", "Add", rootDoc);
                    Element promotionalSaleDetailsElem = UtilXml.addChildElement(rootElem, "PromotionalSaleDetails", rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "DiscountType", discountType, rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "DiscountValue", discountValue, rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "PromotionalSaleName", promotionalSaleName, rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "PromotionalSaleStartTime", promotionalSaleStartTime, rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "PromotionalSaleEndTime", promotionalSaleEndTime, rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "PromotionalSaleType", promotionalSaleType, rootDoc);
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    Debug.logError(requestXMLcode, module);
                    String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                    //Debug.logError(responseXML, module);
                    Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                    Element elemResponse = docResponse.getDocumentElement();
                    String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                    
                    if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                        List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                        Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                        StringBuffer errorMessage = new StringBuffer();
                        while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                            Element errorElement = errorElementsElemIter.next();
                            String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                            String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                            errorMessage.append(shortMessage + " - " + longMessage);
                        }   //while errorElement -- END
                        Debug.logError(productStoreId + ": Failed set Promotional sale for " + i + "off and errorMessage: " + errorMessage, module);
                    }   //if ack failure -- END
                    else {
                        String promotionalSaleId = UtilXml.childElementValue(elemResponse, "PromotionalSaleID", null);
                        Debug.logError(productStoreId + ": Successfully set Promotional sale for " + i + "off", module);
                        GenericValue ebayPromotionalSale = delegator.makeValue("EbayPromotionalSale", UtilMisc.toMap("promotionalSaleId", promotionalSaleId, "productStoreId", productStoreId));
                        ebayPromotionalSale.set("discountType", discountType);
                        ebayPromotionalSale.set("discountValue", discountValue);
                        ebayPromotionalSale.set("promotionalSaleName", promotionalSaleName);
                        ebayPromotionalSale.set("promotionalSaleType", promotionalSaleType);
                        ebayPromotionalSale.set("promotionalSaleStartTime", promotionalSaleStartTime);
                        ebayPromotionalSale.set("promotionalSaleEndTime", promotionalSaleEndTime);
                        delegator.createOrStore(ebayPromotionalSale);
                    }
                    i = i + discountGapInt;
                }   //loop create discount -- END
            }   //loop productStoreList -- END
        }   //main try -- END
        catch (GenericEntityException e) {
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
    }   //setPromotionalSaleDetailsOneTime
    
    public static Map<String, Object> updateEbayOrderItem(DispatchContext dctx, Map context)
	throws GenericEntityException, GenericServiceException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String pageNumberStr = (String) context.get("pageNumber");
        String numberOfDays = (String) context.get("numberOfDays");
        String continuousModeStr = (String) context.get("continuousMode");
        Map result = ServiceUtil.returnSuccess();
        DecimalFormat df = new DecimalFormat("00000");
        boolean continuousMode = false;
        if (continuousModeStr.equals("Y") || continuousModeStr.equals("y")) {
            continuousMode = true;
        }
        
        try {   //main try -- START
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
            SimpleDateFormat sdfEbay = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'");
            SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
            
            Calendar createFrom = Calendar.getInstance();
            createFrom.set(Calendar.DATE, createFrom.get(Calendar.DATE) - Integer.parseInt(numberOfDays));
            Timestamp createFromDateTS = Timestamp.valueOf(sdf.format(createFrom.getTime()));
            
            Calendar createTo = Calendar.getInstance();
            Timestamp createToDateTS = Timestamp.valueOf(sdf.format(createTo.getTime()));
            String createToDate = sdfEbay.format(createToDateTS.getTime());
            String createFromDate = sdfEbay.format(createFromDateTS.getTime());
            
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = common.accountInfo(delegator, productStore);
            String callName = "GetOrders";
            mapAccount.put("callName", callName);
            
            boolean hasMoreOrders = true;
            int pageNumber = 1;
            if (pageNumberStr != null) {
                pageNumber = Integer.parseInt(pageNumberStr);
            }
            while (hasMoreOrders) { //loop hasMoreOrders is true -- START
                //Building XML -- START
                Debug.logError(productStoreId + ": updateEbayOrderItem processing pageNumber " + pageNumber, module);
                Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                Element rootElem = rootDoc.getDocumentElement();
                rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                
                //RequesterCredentials -- START
                Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                //RequesterCredentials -- END
                
                UtilXml.addChildElementValue(rootElem, "IncludeFinalValueFee", "false", rootDoc);
                UtilXml.addChildElementValue(rootElem, "CreateTimeFrom", createFromDate, rootDoc);
                UtilXml.addChildElementValue(rootElem, "CreateTimeTo", createToDate, rootDoc);
                UtilXml.addChildElementValue(rootElem, "OrderRole", "Seller", rootDoc);
                UtilXml.addChildElementValue(rootElem, "OrderStatus", "All", rootDoc);
                //UtilXml.addChildElementValue(rootElem, "DetailLevel", "ReturnAll", rootDoc);
                
                Element paginationElem = UtilXml.addChildElement(rootElem, "Pagination", rootDoc);
                UtilXml.addChildElementValue(paginationElem, "EntriesPerPage", "100", rootDoc);
                UtilXml.addChildElementValue(paginationElem, "PageNumber", pageNumber + "", rootDoc);
                //Building XML -- END
                
                String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                //Debug.logError(requestXMLcode, module);
                
                String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                //Debug.logError(responseXML, module);
                Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                Element elemResponse = docResponse.getDocumentElement();
                String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                String newHasMoreOrders = UtilXml.childElementValue(elemResponse, "HasMoreOrders", "false");
                Debug.logError(productStoreId + ": updateEbayOrderItem hasMoreOrders is " + newHasMoreOrders, module);
                if (newHasMoreOrders.toUpperCase().equals("TRUE")) {
                    hasMoreOrders = true;
                } else {
                    hasMoreOrders = false;
                }
                
                if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                    List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                    Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                    StringBuffer errorMessage = new StringBuffer();
                    while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                        Element errorElement = errorElementsElemIter.next();
                        String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                        String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                        errorMessage.append(shortMessage + " - " + longMessage);
                    }   //while errorElement -- END
                    Debug.logError(productStoreId + ": Failed running updateEbayOrderItem page: " + pageNumber + ", errorMessage: " + errorMessage, module);
                }   //if ack failure -- END
                else {  //if ack success - starts updating ebayOrderItem -- START
                    Element orderArrayElement = UtilXml.firstChildElement(elemResponse, "OrderArray");
                    List<? extends Element> orderElementList = UtilXml.childElementList(orderArrayElement, "Order");
                    Iterator<? extends Element> orderElemIter = orderElementList.iterator();
                    while (orderElemIter.hasNext()) { //loop Order Element -- START
                        int seqId = 1;
                        Element orderElement = orderElemIter.next();
                        Element transactionArrayElement = UtilXml.firstChildElement(orderElement, "TransactionArray");
                        List<? extends Element> transactionList = UtilXml.childElementList(transactionArrayElement, "Transaction");
                        Iterator<? extends Element> transactionListElemIter = transactionList.iterator();
                        while (transactionListElemIter.hasNext()) { //loop transactionListElemIter -- START
                            String orderItemSeqId = df.format(seqId);
                            Element transactionElement = transactionListElemIter.next();
                            Element shippingDetailsElement = UtilXml.firstChildElement(transactionElement, "ShippingDetails");
                            String salesRecordNumber = UtilXml.childElementValue(shippingDetailsElement, "SellingManagerSalesRecordNumber", null);
                            String createdDate = UtilXml.childElementValue(transactionElement, "CreatedDate");
                            String createdDateStr = createdDate.substring(0, 10);
                            java.sql.Date createdDateSql = new java.sql.Date(formatSql.parse(createdDateStr).getTime());
                            String sku = null;
                            String itemId = null;
                            
                            List<? extends Element> item = UtilXml.childElementList(transactionElement, "Item");
                            Iterator<? extends Element> itemElemIter = item.iterator();
                            while (itemElemIter.hasNext()) {   //loop itemElemIter -- START
                                Element itemElement = itemElemIter.next();
                                itemId = UtilXml.childElementValue(itemElement, "ItemID", null);
                                sku = UtilXml.childElementValue(itemElement, "SKU", null);
                            }   //loop itemElemIter -- END
                            
                            //retrieve Variation
                            List<? extends Element> variation = UtilXml.childElementList(transactionElement, "Variation");
                            if (UtilValidate.isNotEmpty(variation)) {   //if variation is not empty -- START
                                Iterator<? extends Element> variationElemIter = variation.iterator();
                                while (variationElemIter.hasNext()) {   //loop variationElemIter -- START
                                    Element variationElement = variationElemIter.next();
                                    sku = UtilXml.childElementValue(variationElement, "SKU", "");
                                }   //loop variationElemIter -- END
                            }   //if variation is not empty -- END
                            
                            BigDecimal qty = (UtilValidate.isNotEmpty(UtilXml.childElementValue(transactionElement, "QuantityPurchased", "0"))) ? new BigDecimal(UtilXml.childElementValue(transactionElement, "QuantityPurchased", "0")) : BigDecimal.ZERO;
                            String transactionId  = UtilXml.childElementValue(transactionElement, "TransactionID", null);
                            BigDecimal unitPrice = getPrice(UtilXml.childElementValue(transactionElement, "TransactionPrice", null));
                            String currency = UtilXml.childElementAttribute(transactionElement, "TransactionPrice", "currencyID", null);
                            
                            List<GenericValue> checkEbayOrderItem = delegator.findByAnd("EbayOrderItem", UtilMisc.toMap("productStoreId", productStoreId, "salesRecordNumber", salesRecordNumber), null, false);
                            if (UtilValidate.isEmpty(checkEbayOrderItem)) { //if order is not imported yet -- START
                                GenericValue ebayOrderItem = delegator.makeValue("EbayOrderItem", UtilMisc.toMap("productStoreId", productStoreId, "salesRecordNumber", salesRecordNumber));
                                ebayOrderItem.set("orderItemSeqId", orderItemSeqId);
                                ebayOrderItem.set("itemId", itemId);
                                ebayOrderItem.set("transactionId", transactionId);
                                ebayOrderItem.set("createdDate", createdDate);
                                ebayOrderItem.set("createdDateSql", createdDateSql);
                                ebayOrderItem.set("createdDateStamp", convertEbayDateToTimestamp(createdDate));
                                if (UtilValidate.isNotEmpty(sku)) {
                                    ebayOrderItem.set("sku", normalizeSku(delegator, sku));
                                } else {
                                    ebayOrderItem.set("sku", sku);
                                }
                                
                                ebayOrderItem.set("qty", qty.longValue());
                                ebayOrderItem.set("unitPrice", unitPrice);
                                ebayOrderItem.set("currency", currency);
                                delegator.createOrStore(ebayOrderItem);
                            }   //if order is not imported yet -- END
                            seqId++;
                        }   //loop transactionListElemIter -- END
                        
                    }   //loop Order Element -- END
                }   //if ack success - starts updating ebayOrderItem -- END
                
                GenericValue updateEbayOrderItemStatus = delegator.findOne("UpdateEbayOrderItemStatus", UtilMisc.toMap("productStoreId", productStoreId), false);
                if (UtilValidate.isEmpty(updateEbayOrderItemStatus)) {
                    updateEbayOrderItemStatus = delegator.makeValue("UpdateEbayOrderItemStatus", UtilMisc.toMap("productStoreId", productStoreId));
                }
                if (newHasMoreOrders.toUpperCase().equals("FALSE")) {
                    pageNumber = 1;
                }
                updateEbayOrderItemStatus.set("hasMoreOrders", newHasMoreOrders);
                updateEbayOrderItemStatus.set("lastPageNumber", pageNumber + "");
                updateEbayOrderItemStatus.set("numberOfDays", numberOfDays + "");
                delegator.createOrStore(updateEbayOrderItemStatus);
                pageNumber++;
            }   //loop hasMoreOrders is true -- END
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            //return ServiceUtil.returnError(e.getMessage());
            dispatcher.runSync("TradingApiVerifyUpdateEbayOrderItem", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
        }
        try {
            dispatcher.runSync("TradingApiVerifyUpdateEbayOrderItem", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
        }
        catch (GenericServiceException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError(productStoreId + ": finished running updateEbayOrderItem", module);
        if (continuousMode) {
            dispatcher.runSync("autoScheduleJob", UtilMisc.toMap("serviceName", "TradingApiUpdateEbayOrderItem", "userLogin", userLogin));
        }
        
        return result;
        
    }   //updateEbayOrderItem
    
    public static Map<String, Object> updateEbayOrderItemAddQty(DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException, SAXException, ParserConfigurationException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        Map result = ServiceUtil.returnSuccess();
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.sss");
        Timestamp nowTS = Timestamp.valueOf(sdf.format(now.getTime()));
        SimpleDateFormat sdfEbay = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss'Z'");
        SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
        
        DecimalFormat df = new DecimalFormat("00000");
        
        try {   //main try == START
            /*String createFromDate = null;
            GenericValue lastRuntimeGV = delegator.findOne("EbayOrderItemRuntime", UtilMisc.toMap("productStoreId", productStoreId), false);
            if (UtilValidate.isNotEmpty(lastRuntimeGV)) {
                Timestamp lastRuntimeTS = lastRuntimeGV.getTimestamp("lastRuntime");
                createFromDate = sdfEbay.format(lastRuntimeTS.getTime() - 28800000);
            } else {
                lastRuntimeGV = delegator.makeValue("EbayOrderItemRuntime", UtilMisc.toMap("productStoreId", productStoreId));
                createFromDate = sdfEbay.format(nowTS.getTime() - 28800000 - 600000);
            }
            
            String createToDate = sdfEbay.format(nowTS.getTime() - 28800000);
            Debug.logError("createToDate: " + createToDate, module);
            Debug.logError("createFromDate: " + createFromDate, module);*/
            
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = common.accountInfo(delegator, productStore);
            String callName = "GetOrders";
            mapAccount.put("callName", callName);
            
            boolean hasMoreOrders = true;
            int pageNumber = 1;
            while (hasMoreOrders) { //loop hasMoreOrders is true -- START
                //Building XML -- START
                Debug.logError(productStoreId + ": updateEbayOrderItemAddQty processing pageNumber " + pageNumber, module);
                Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                Element rootElem = rootDoc.getDocumentElement();
                rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                
                //RequesterCredentials -- START
                Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                //RequesterCredentials -- END
                
                UtilXml.addChildElementValue(rootElem, "IncludeFinalValueFee", "false", rootDoc);
                UtilXml.addChildElementValue(rootElem, "NumberOfDays", "1", rootDoc);
                /*UtilXml.addChildElementValue(rootElem, "CreateTimeFrom", createFromDate, rootDoc);
                UtilXml.addChildElementValue(rootElem, "CreateTimeTo", createToDate, rootDoc);*/
                UtilXml.addChildElementValue(rootElem, "OrderRole", "Seller", rootDoc);
                UtilXml.addChildElementValue(rootElem, "OrderStatus", "All", rootDoc);
                //UtilXml.addChildElementValue(rootElem, "DetailLevel", "ReturnAll", rootDoc);
                
                Element paginationElem = UtilXml.addChildElement(rootElem, "Pagination", rootDoc);
                UtilXml.addChildElementValue(paginationElem, "EntriesPerPage", "100", rootDoc);
                UtilXml.addChildElementValue(paginationElem, "PageNumber", pageNumber + "", rootDoc);
                //Building XML -- END
                
                String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                //Debug.logError(requestXMLcode, module);
                
                String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                //Debug.logError(responseXML, module);
                Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                Element elemResponse = docResponse.getDocumentElement();
                String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                String newHasMoreOrders = UtilXml.childElementValue(elemResponse, "HasMoreOrders", "false");
                //Debug.logError(productStoreId + ": updateEbayOrderItem hasMoreOrders is " + newHasMoreOrders, module);
                if (newHasMoreOrders.toUpperCase().equals("TRUE")) {
                    hasMoreOrders = true;
                } else {
                    hasMoreOrders = false;
                }
                
                if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                    List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                    Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                    StringBuffer errorMessage = new StringBuffer();
                    while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                        Element errorElement = errorElementsElemIter.next();
                        String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                        String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                        errorMessage.append(shortMessage + " - " + longMessage);
                    }   //while errorElement -- END
                    Debug.logError(productStoreId + ": Failed running updateEbayOrderItem page: " + pageNumber + ", errorMessage: " + errorMessage, module);
                    
                    //lastRuntimeGV.set("notes", "Failed message: " + errorMessage);
                    //delegator.createOrStore(lastRuntimeGV);
                }   //if ack failure -- END
                else {  //if ack success - starts updating ebayOrderItem -- START
                    Element orderArrayElement = UtilXml.firstChildElement(elemResponse, "OrderArray");
                    List<? extends Element> orderElementList = UtilXml.childElementList(orderArrayElement, "Order");
                    Iterator<? extends Element> orderElemIter = orderElementList.iterator();
                    while (orderElemIter.hasNext()) { //loop Order Element -- START
                        int seqId = 1;
                        Element orderElement = orderElemIter.next();
                        Element transactionArrayElement = UtilXml.firstChildElement(orderElement, "TransactionArray");
                        List<? extends Element> transactionList = UtilXml.childElementList(transactionArrayElement, "Transaction");
                        Iterator<? extends Element> transactionListElemIter = transactionList.iterator();
                        while (transactionListElemIter.hasNext()) { //loop transactionListElemIter -- START
                            String orderItemSeqId = df.format(seqId);
                            Element transactionElement = transactionListElemIter.next();
                            Element shippingDetailsElement = UtilXml.firstChildElement(transactionElement, "ShippingDetails");
                            String salesRecordNumber = UtilXml.childElementValue(shippingDetailsElement, "SellingManagerSalesRecordNumber", null);
                            String createdDate = UtilXml.childElementValue(transactionElement, "CreatedDate");
                            String createdDateStr = createdDate.substring(0, 10);
                            java.sql.Date createdDateSql = new java.sql.Date(formatSql.parse(createdDateStr).getTime());
                            String sku = null;
                            String itemId = null;
                            
                            List<? extends Element> item = UtilXml.childElementList(transactionElement, "Item");
                            Iterator<? extends Element> itemElemIter = item.iterator();
                            while (itemElemIter.hasNext()) {   //loop itemElemIter -- START
                                Element itemElement = itemElemIter.next();
                                itemId = UtilXml.childElementValue(itemElement, "ItemID", null);
                                sku = UtilXml.childElementValue(itemElement, "SKU", null);
                            }   //loop itemElemIter -- END
                            
                            //retrieve Variation
                            List<? extends Element> variation = UtilXml.childElementList(transactionElement, "Variation");
                            if (UtilValidate.isNotEmpty(variation)) {   //if variation is not empty -- START
                                Iterator<? extends Element> variationElemIter = variation.iterator();
                                while (variationElemIter.hasNext()) {   //loop variationElemIter -- START
                                    Element variationElement = variationElemIter.next();
                                    sku = UtilXml.childElementValue(variationElement, "SKU", "");
                                }   //loop variationElemIter -- END
                            }   //if variation is not empty -- END
                            
                            BigDecimal qty = (UtilValidate.isNotEmpty(UtilXml.childElementValue(transactionElement, "QuantityPurchased", "0"))) ? new BigDecimal(UtilXml.childElementValue(transactionElement, "QuantityPurchased", "0")) : BigDecimal.ZERO;
                            String transactionId  = UtilXml.childElementValue(transactionElement, "TransactionID", null);
                            BigDecimal unitPrice = getPrice(UtilXml.childElementValue(transactionElement, "TransactionPrice", null));
                            String currency = UtilXml.childElementAttribute(transactionElement, "TransactionPrice", "currencyID", null);
                            String orderLineItemId = UtilXml.childElementValue(transactionElement, "OrderLineItemID", null);
                            
                            List<GenericValue> checkEbayOrderItem = delegator.findByAnd("EbayOrderItem", UtilMisc.toMap("productStoreId", productStoreId, "salesRecordNumber", salesRecordNumber), null, false);
                            if (UtilValidate.isEmpty(checkEbayOrderItem)) { //if order is not imported yet -- START
                                GenericValue ebayOrderItem = delegator.makeValue("EbayOrderItem", UtilMisc.toMap("productStoreId", productStoreId, "salesRecordNumber", salesRecordNumber));
                                ebayOrderItem.set("orderItemSeqId", orderItemSeqId);
                                ebayOrderItem.set("itemId", itemId);
                                ebayOrderItem.set("transactionId", transactionId);
                                ebayOrderItem.set("createdDate", createdDate);
                                ebayOrderItem.set("createdDateSql", createdDateSql);
                                ebayOrderItem.set("createdDateStamp", convertEbayDateToTimestamp(createdDate));
                                if (UtilValidate.isNotEmpty(sku)) {
                                    ebayOrderItem.set("sku", normalizeSku(delegator, sku));
                                }
                                ebayOrderItem.set("originalSku", sku);
                                ebayOrderItem.set("qty", qty.longValue());
                                ebayOrderItem.set("unitPrice", unitPrice);
                                ebayOrderItem.set("currency", currency);
                                delegator.createOrStore(ebayOrderItem);
                            }   //if order is not imported yet -- END
                            seqId++;
                            
                            //add eBay Qty == START
                            GenericValue addQtyHistory = delegator.findOne("AddQtyHistory", UtilMisc.toMap("orderLineItemId", orderLineItemId, "productStoreId", productStoreId), false);
                            if (UtilValidate.isEmpty(addQtyHistory)) {
                                Map addEbayQty = dispatcher.runSync("gudaoAddEbayQuantity", UtilMisc.toMap("productStoreId", productStoreId, "itemId", itemId, "variationSku", sku, "quantity", qty, "userLogin", userLogin));
                                addQtyHistory = delegator.makeValue("AddQtyHistory", UtilMisc.toMap("orderLineItemId", orderLineItemId));
                                addQtyHistory.set("productStoreId", productStoreId);
                                addQtyHistory.set("itemId", itemId);
                                addQtyHistory.set("productId", normalizeSku(delegator, sku));
                                addQtyHistory.set("originalSku", sku);
                                addQtyHistory.set("qty", qty.longValue());
                                if (ServiceUtil.isError(addEbayQty)) {    //if addEbayQty failed == START
                                    addQtyHistory.set("statusId", "FAILED");
                                    addQtyHistory.set("notes", addEbayQty.get("errorMessage"));
                                }   //if addEbayQty failed == END
                                else {
                                    addQtyHistory.set("statusId", "SUCCESS");
                                }
                                delegator.create(addQtyHistory);
                            }
                            //add eBay Qty == END
                        }   //loop transactionListElemIter -- END
                        
                    }   //loop Order Element -- END
                }   //if ack success - starts updating ebayOrderItem -- END
                
                /*GenericValue updateEbayOrderItemStatus = delegator.findOne("UpdateEbayOrderItemStatus", UtilMisc.toMap("productStoreId", productStoreId), false);
                if (UtilValidate.isEmpty(updateEbayOrderItemStatus)) {
                    updateEbayOrderItemStatus = delegator.makeValue("UpdateEbayOrderItemStatus", UtilMisc.toMap("productStoreId", productStoreId));
                }
                if (newHasMoreOrders.toUpperCase().equals("FALSE")) {
                    pageNumber = 1;
                }
                updateEbayOrderItemStatus.set("hasMoreOrders", newHasMoreOrders);
                updateEbayOrderItemStatus.set("lastPageNumber", pageNumber + "");
                updateEbayOrderItemStatus.set("numberOfDays", numberOfDays + "");
                delegator.createOrStore(updateEbayOrderItemStatus);*/
                pageNumber++;
            }   //loop hasMoreOrders is true -- END
            
            //lastRuntimeGV.set("lastRuntime", nowTS);
            //lastRuntimeGV.set("notes", "Success");
            //delegator.createOrStore(lastRuntimeGV);
        }   //main try == END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (SAXException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        return result;
        
    }   //updateEbayOrderItemAddQty
    
    public static Map<String, Object> gudaoAddEbayQuantity (DispatchContext dctx, Map context)
    throws GenericEntityException, IOException, SAXException, ParserConfigurationException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String itemId = (String) context.get("itemId");
        String variationSku = (String) context.get("variationSku");
        BigDecimal quantity = (BigDecimal) context.get("quantity");
        Map result = ServiceUtil.returnSuccess();
        
        try {   //main try == START
            //Initial Calling API credentials == START
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            Map getItemMapAccount = FastMap.newInstance();
            String getItemCallName = "GetItem";
            getItemMapAccount = common.accountInfo(delegator, productStore);
            getItemMapAccount.put("callName", getItemCallName);
            
            Map addQtyMapAccount = FastMap.newInstance();
            String addQtyCallName = "ReviseFixedPriceItem";
            addQtyMapAccount = common.accountInfo(delegator, productStore);
            addQtyMapAccount.put("callName", addQtyCallName);
            //Initial Calling API credentials == END
            
            //ebay API - GetItem == START
            Document rootDoc = UtilXml.makeEmptyXmlDocument(getItemCallName + "Request");
            Element rootElem = rootDoc.getDocumentElement();
            rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            
            Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
            UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", getItemMapAccount.get("token").toString(), rootDoc);
            
            UtilXml.addChildElementValue(rootElem, "ItemID", itemId, rootDoc);
            UtilXml.addChildElementValue(rootElem, "IncludeItemCompatibilityList", "false", rootDoc);
            UtilXml.addChildElementValue(rootElem, "IncludeItemSpecifics", "false", rootDoc);
            UtilXml.addChildElementValue(rootElem, "IncludeTaxTable", "false", rootDoc);
            UtilXml.addChildElementValue(rootElem, "IncludeWatchCount", "false", rootDoc);
            UtilXml.addChildElementValue(rootElem, "DetailLevel", "ItemReturnAttributes", rootDoc);
            
            String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
            //Debug.logError(requestXMLcode, module);
            String responseXML = sendRequestXMLToEbay(getItemMapAccount, requestXMLcode);
            //Debug.logError(responseXML, module);
            //ebay API - GetItem == END
            
            //ebay API - reviseFixedPriceItem == START
            //Building XML -- START
            Document addQtyRootDoc = UtilXml.makeEmptyXmlDocument("ReviseFixedPriceItemRequest");
            Element addQtyRootElem = addQtyRootDoc.getDocumentElement();
            addQtyRootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            
            //RequesterCredentials -- START
            Element addQtyRequesterCredentialsElem = UtilXml.addChildElement(addQtyRootElem, "RequesterCredentials", addQtyRootDoc);
            UtilXml.addChildElementValue(addQtyRequesterCredentialsElem, "eBayAuthToken", addQtyMapAccount.get("token").toString(), addQtyRootDoc);
            //RequesterCredentials -- END
            
            //Item -- START
            Element addQtyItemElem = UtilXml.addChildElement(addQtyRootElem, "Item", addQtyRootDoc);
            UtilXml.addChildElementValue(addQtyItemElem, "ItemID", itemId, addQtyRootDoc);
            
            //ebay API - reviseFixedPriceItem == END
            
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
            String getItemSku = null;
            String getItemQtyStr = null;
            int getItemQty =  5;
            boolean hasVariation = false;
            
            if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                StringBuffer errorMessage = new StringBuffer();
                while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                    Element errorElement = errorElementsElemIter.next();
                    String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                    String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                    errorMessage.append(shortMessage + " - " + longMessage);
                }   //while errorElement -- END
                result = ServiceUtil.returnError(errorMessage.toString());
                return result;
            }   //if ack failure -- END
            else {  //if ack Success -- START
                Element itemElement = UtilXml.firstChildElement(elemResponse, "Item");
                Element variationsElement = UtilXml.firstChildElement(itemElement, "Variations");
                List<? extends Element> variationList = UtilXml.childElementList(variationsElement, "Variation");
                if (UtilValidate.isNotEmpty(variationList)) {   //if has variation == START
                    Iterator<? extends Element> variationListElemIter = variationList.iterator();
                    while (variationListElemIter.hasNext()) {   //loop variationListElemIter == START
                        hasVariation = true;
                        Element variationElement = variationListElemIter.next();
                        String tempSku = UtilXml.childElementValue(variationElement, "SKU", null);
                        if (UtilValidate.isNotEmpty(tempSku)) {
                            if (tempSku.equals(variationSku)) {
                                getItemSku = tempSku;
                                getItemQtyStr = UtilXml.childElementValue(variationElement, "Quantity", "5");
                            }
                        }
                    }   //loop variationListElemIter == END
                }   //if has variation == END
                
                if (!hasVariation) {
                    getItemSku = UtilXml.childElementValue(itemElement, "SKU", null);
                    getItemQtyStr = UtilXml.childElementValue(itemElement, "Quantity", "1");
                }
            }   //if ack Success -- END
            
            Debug.logError("getItemSKU: " + getItemSku, module);
            Debug.logError("getItemQty: " + getItemQtyStr, module);
            int qtyToBeUpdated = quantity.intValue() + Integer.parseInt(getItemQtyStr);
            Debug.logError("Total Qty to be updated: " + qtyToBeUpdated, module);
            
            //update eBay QTY == START
            
            //update eBay QTY == END
        }   //main try == END
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        /*catch (SAXException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }*/
        catch (IOException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        return result;
    }   //gudaoAddEbayQuantity
    
    public static Timestamp convertEbayDateToTimestamp (String date) {
        Timestamp result = null;
        try {
            date = date.replaceAll("T"," ");
            date = date.replaceAll("Z","");
            //Debug.logError("date is " + date, module);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = sdf.parse(date);
            result = new java.sql.Timestamp(parsedDate.getTime());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public static String normalizeSku(Delegator delegator, String sku)	//Description
    throws GenericEntityException, GenericServiceException {
        
        try {
            GenericValue productMaster = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", sku), false);
            if (UtilValidate.isEmpty(productMaster)) {
                int skuLength = sku.length();
                if (sku.contains("|")) {    //new 变体 format -- START
                    String term[]= sku.split("\\|");
                    sku = term[0];
                }   //new 变体 format -- END
                else {  //old 变体 format -- START
                    if (sku.startsWith("AA")) {
                        sku = sku.replaceFirst("AA","");
                    }
                    if (sku.startsWith("BB")) {
                        sku = sku.replaceFirst("BB","");
                    }
                    if (sku.startsWith("CC")) {
                        sku = sku.replaceFirst("CC","");
                    }
                    if (sku.startsWith("DD")) {
                        sku = sku.replaceFirst("DD","");
                    }
                    if (sku.startsWith("EE")) {
                        sku = sku.replaceFirst("EE","");
                    }
                    if (sku.startsWith("FF")) {
                        sku = sku.replaceFirst("FF","");
                    }
                    if (sku.startsWith("GG")) {
                        sku = sku.replaceFirst("GG","");
                    }
                    if (sku.startsWith("HH")) {
                        sku = sku.replaceFirst("HH","");
                    }
                    if (sku.startsWith("II")) {
                        sku = sku.replaceFirst("II","");
                    }
                    if (sku.startsWith("JJ")) {
                        sku = sku.replaceFirst("JJ","");
                    }
                    if (sku.startsWith("KK")) {
                        sku = sku.replaceFirst("KK","");
                    }
                    if (sku.startsWith("LL")) {
                        sku = sku.replaceFirst("LL","");
                    }
                    if (sku.startsWith("MM")) {
                        sku = sku.replaceFirst("MM","");
                    }
                    if (sku.startsWith("NN")) {
                        sku = sku.replaceFirst("NN","");
                    }
                    if (sku.startsWith("OO")) {
                        sku = sku.replaceFirst("OO","");
                    }
                    if (sku.startsWith("QQ")) {
                        sku = sku.replaceFirst("QQ","");
                    }
                    if (sku.startsWith("SS")) {
                        sku = sku.replaceFirst("SS","");
                    }
                    if (sku.startsWith("WW")) {
                        sku = sku.replaceFirst("WW","");
                    }
                    if (sku.startsWith("YY")) {
                        sku = sku.replaceFirst("YY","");
                    }
                    if (sku.startsWith("ZZ")) {
                        sku = sku.replaceFirst("ZZ","");
                    }
                    if (sku.startsWith("FR")) {
                        sku = sku.replaceFirst("FR","");
                    }
                    if (sku.startsWith("LT")) {
                        sku = sku.replaceFirst("LT","");
                    }
                    if (sku.contains("#")) {
                        sku = sku.replaceAll("#","");
                    }
                    if (sku.endsWith("AAAA")) {
                        sku = sku.replaceFirst("AAAA","");
                    }
                    if (sku.endsWith("BB")) {
                        if (sku.endsWith("BBBB")) {
                            sku = sku.replaceFirst("BBBB","");
                        } else {
                            sku = sku.substring(0,skuLength - 2);
                        }
                    }
                    if (sku.endsWith("GG")) {
                        sku = sku.substring(0,skuLength - 2);
                    }
                }   //old 变体 format -- END

                if (sku.contains(" ")) {
                    sku = sku.replaceAll(" ","");
                }
            }
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }
        return sku;
        
    }   //normalizeSku
    
    
    public static Map<String, Object> verifyUpdateEbayOrderItem(DispatchContext dctx, Map context)	//Description
    throws GenericEntityException, GenericServiceException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        //String productStoreId = (String) context.get("productStoreId");
        String productStoreId = (String) context.get("productStoreId");
        //result.put("productStoreGroup", productStoreGroup);
        
        
        try {
            GenericValue updateEbayOrderItemStatus = delegator.findOne("UpdateEbayOrderItemStatus", UtilMisc.toMap("productStoreId", productStoreId), false);
            String hasMoreOrders = updateEbayOrderItemStatus.getString("hasMoreOrders");
            String numberOfDays = updateEbayOrderItemStatus.getString("numberOfDays");
            if (hasMoreOrders.toUpperCase().equals("TRUE")) {
                String pageNumber = (Integer.parseInt(updateEbayOrderItemStatus.getString("lastPageNumber")) + 1) + "";
                Map updateEbayOrderItem = dispatcher.runSync("TradingApiUpdateEbayOrderItem", UtilMisc.toMap("productStoreId", productStoreId, "pageNumber", pageNumber, "numberOfDays", numberOfDays, "userLogin", userLogin));
            }
            else if (updateEbayOrderItemStatus.getString("lastPageNumber").equals("1")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
                Calendar checkday = Calendar.getInstance();
                checkday.set(Calendar.MINUTE, checkday.get(Calendar.MINUTE) - 30);
                Timestamp checkDate = Timestamp.valueOf(sdf.format(checkday.getTime()));
                
                EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                          EntityCondition.makeCondition("createdStamp", EntityOperator.GREATER_THAN, checkDate),
                                                                                          EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId)
                                                                                          ));
                List<GenericValue> ebayOrderItemList = delegator.findList("EbayOrderItem", condition, null, null, null, false);
                if(UtilValidate.isEmpty(ebayOrderItemList)) {
                    Map updateEbayOrderItem = dispatcher.runSync("TradingApiUpdateEbayOrderItem", UtilMisc.toMap("productStoreId", productStoreId, "pageNumber", "1", "numberOfDays", numberOfDays, "userLogin", userLogin));
                }

            }
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }catch (GenericServiceException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        //Debug.logError(productStoreId + ": finished running verifyUpdateEbayOrderItem", module);
        return result;
        
        
    }   //verifyUpdateEbayOrderItem

    
    private static BigDecimal getPrice(String price) {
        BigDecimal result = BigDecimal.ZERO;
        try {
	        price = price.replaceAll("[^0-9,.-]", "");
	        if(!price.contains(".")) { // in latin countries a comma is used to split the integer and the fractional part
				int indexComma = price.lastIndexOf(",");
				if(indexComma >= 0) {
					price = price.substring(0, indexComma) + "." + price.substring(indexComma+1);
				}
			}
	        price = price.replaceAll(",", "");
	        if (price.startsWith("$")) {
	            result = new BigDecimal(price.substring(1));
	        } else if (price.contains("$") && !price.startsWith("$")) {
	            price = price.substring(price.indexOf("$") + 1);
	            result = new BigDecimal(price);
	        } else if (price.contains(" ") && !price.contains("$")) {
	            String[] arr = price.split(" ");
	            result = new BigDecimal(arr[1]);
	        } else {
                result = new BigDecimal(price);
	        }
		} catch (NumberFormatException e) {
			result = BigDecimal.ZERO;
		}
        return result;
    }

    public static Map<String, Object> prepareAutoMarkdownSingle(DispatchContext dctx, Map context)
	throws GenericEntityException, GenericServiceException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        Map result = ServiceUtil.returnSuccess();
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        Timestamp todayTS = Timestamp.valueOf(sdf.format(now.getTime()));
        
        
        try {   //main try --START
            GenericValue productStoreEbaySetting = delegator.findOne("ProductStoreEbaySetting", UtilMisc.toMap("productStoreId", productStoreId), false);
            String autoDiscount = productStoreEbaySetting.getString("autoDiscount");
            String maxDiscount = productStoreEbaySetting.getString("maxDiscount");
            String unsoldDay = productStoreEbaySetting.getString("unsoldDay");
            String discountGap = productStoreEbaySetting.getString("discountGap");
            String discountDuration = productStoreEbaySetting.getString("discountDuration");
            String discountEndTime = productStoreEbaySetting.getString("discountEndTime");
            
            if (autoDiscount.equals("Y")) { //if autoDiscount is set to Y -- START
                //Get sales last X days based on unsoldDay -- START
                Map getListingUnsoldXDays = dispatcher.runSync("TradingApiGetListingSoldUnsoldXDays", UtilMisc.toMap("productStoreId", productStoreId, "unsoldDay", unsoldDay, "userLogin", userLogin));
                List<String> unsoldItemIdList = (List) getListingUnsoldXDays.get("unsoldItemIdList");
                List<String> soldItemIdList = (List) getListingUnsoldXDays.get("soldItemIdList");
                //Get sales last X days based on unsoldDay -- END
                
                for (String itemId : unsoldItemIdList) {    //loop unsoldItemIdList -- START
                    double currentDiscount = 5.0;
                    GenericValue lastMarkdownListing = delegator.findOne("LastMarkdownListing", UtilMisc.toMap("itemId", itemId), false);
                    if (UtilValidate.isNotEmpty(lastMarkdownListing)) { //if lastMarkdownListing is not empty -- START
                        double lastDiscount = lastMarkdownListing.getDouble("lastDiscountValue");
                        currentDiscount = lastDiscount + Double.parseDouble(discountGap);
                        if (currentDiscount > Double.parseDouble(maxDiscount)) {
                            currentDiscount = Double.parseDouble(maxDiscount);
                        }
                    }   //if lastMarkdownListing is not empty -- END
                    GenericValue waitingForMarkdown = delegator.makeValue("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId, "itemId", itemId, "currentDiscount", currentDiscount));
                    delegator.createOrStore(waitingForMarkdown);
                    //Debug.logError("itemId " + itemId + ": discount " + currentDiscount + "% off", module);
                    
                }   //loop unsoldItemIdList -- END
                
                for (String soldItemId : soldItemIdList) {  //loop soldItemIdList -- START
                    double currentDiscount = 5;
                    GenericValue lastMarkdownListing = delegator.findOne("LastMarkdownListing", UtilMisc.toMap("itemId", soldItemId), false);
                    if (UtilValidate.isNotEmpty(lastMarkdownListing)) { //if lastMarkdownListing is not empty -- START
                        double lastDiscount = lastMarkdownListing.getDouble("lastDiscountValue");
                        currentDiscount = lastDiscount - Double.parseDouble(discountGap);
                    }   //if lastMarkdownListing is not empty -- END
                    if (currentDiscount < 5.0) {  //if discount is below 5 - dont markdown -- START
                        delegator.removeValue(lastMarkdownListing);
                    }   //if discount is below 5 - dont markdown -- START
                    else {  //do markdown -- START
                        GenericValue waitingForMarkdown = delegator.makeValue("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId, "itemId", soldItemId, "currentDiscount", currentDiscount));
                        delegator.createOrStore(waitingForMarkdown);
                        //Debug.logError("SOLD: itemId " + soldItemId + ": discount " + currentDiscount + "% off", module);
                    }   //do markdown -- END
                }   //loop soldItemIdList -- END
            }   //if autoDiscount is set to Y -- END
        }   //main try --START
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
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        result.put("productStoreId", productStoreId);
        
        try {   //run autoMarkdownSingle -- START
            dispatcher.runSync("TradingApiAutoMarkdownSingle", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
        }   //run autoMarkdownSingle -- END
        catch (GenericServiceException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        delegator.removeByAnd("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId));
        Debug.logError(productStoreId + ": Finished autoMarkdown", module);
        return result;
        
    }   //prepareAutoMarkdownSingle
    
    public static Map<String, Object> autoMarkdownSingle(DispatchContext dctx, Map context)
	throws GenericEntityException   {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        Map result = ServiceUtil.returnSuccess();
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String today = sdfNow.format(now.getTime());
        
        
        try {   //main try -- START
            FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogInfo/markdownManager/" + productStoreId + ".txt", true);
            
            GenericValue productStoreEbaySetting = delegator.findOne("ProductStoreEbaySetting", UtilMisc.toMap("productStoreId", productStoreId), false);
            String discountDuration = productStoreEbaySetting.getString("discountDuration");
            String discountEndTime = productStoreEbaySetting.getString("discountEndTime");
            String promotionalSaleType = productStoreEbaySetting.getString("promotionalSaleType");
            String discountType = productStoreEbaySetting.getString("discountType");
            String discountGap = productStoreEbaySetting.getString("discountGap");
            double discountGapInt = Double.parseDouble(discountGap);
            f1.write(today + ": Starting markdown with discountDuration " + discountDuration + ", discountEndTime " + discountEndTime + ", promotionalSaleType " + promotionalSaleType + ", discountType " + discountType + "\n");
            
            List<GenericValue> waitingForMarkdownList = delegator.findList("WaitingForMarkdown", null, null, null, null, false);
            if (UtilValidate.isNotEmpty(waitingForMarkdownList)) {  //if waitingForMarkdownList is not empty -- START
                f1.write(today + ": WaitingForMarkdownList is NOT empty, continue with markdown\n");
                
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                Map mapAccount = FastMap.newInstance();
                mapAccount = common.accountInfo(delegator, productStore);

                f1.write(today + ": Starting to remove all eBay promotional Sale\n");
                //==== START delete-recreate ebayPromotionalSale ====
                List<GenericValue> ebayPromotionalSaleAll = delegator.findByAnd("EbayPromotionalSale", UtilMisc.toMap("productStoreId", productStoreId), null, false);
                for (GenericValue ebayPromotionalSaleRemove : ebayPromotionalSaleAll) { //loop ebayPromotionalSaleAll -- START
                    String promotionalSaleIdRemove = ebayPromotionalSaleRemove.getString("promotionalSaleId");
                    //remove the promotion on eBay -- START ====================
                    String callName = "SetPromotionalSale";
                    mapAccount.put("callName", callName);
                    f1.write(today + ": Starting building requestXML to remove the promotion for promotionalSaleId " + promotionalSaleIdRemove + "\n");
                    //Building XML -- START
                    Document rootDocDelete = UtilXml.makeEmptyXmlDocument(callName + "Request");
                    Element rootElemDelete = rootDocDelete.getDocumentElement();
                    rootElemDelete.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElemDelete = UtilXml.addChildElement(rootElemDelete, "RequesterCredentials", rootDocDelete);
                    UtilXml.addChildElementValue(requesterCredentialsElemDelete, "eBayAuthToken", mapAccount.get("token").toString(), rootDocDelete);
                    //RequesterCredentials -- END
                    
                    UtilXml.addChildElementValue(rootElemDelete, "Action", "Delete", rootDocDelete);
                    Element promotionalSaleDetailsElemDelete = UtilXml.addChildElement(rootElemDelete, "PromotionalSaleDetails", rootDocDelete);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElemDelete, "PromotionalSaleID", promotionalSaleIdRemove, rootDocDelete);
                    
                    String requestXMLcodeDelete = UtilXml.writeXmlDocument(rootElemDelete);
                    f1.write(today + ": Finished building requestXML to remove the promotion for promotionalSaleId " + promotionalSaleIdRemove + "\n");
                    f1.write(today + ": Sending requestXML to remove the promotion for promotionalSaleId " + promotionalSaleIdRemove + "\n");
                    String responseXMLDelete = sendRequestXMLToEbay(mapAccount, requestXMLcodeDelete);
                    f1.write(today + ": Retrieved responseXML to remove the promotion for promotionalSaleId " + promotionalSaleIdRemove + "\n");
                    //Debug.logError(responseXML, module);
                    Document docResponseDelete = UtilXml.readXmlDocument(responseXMLDelete, true);
                    Element elemResponseDelete = docResponseDelete.getDocumentElement();
                    String ackDelete = UtilXml.childElementValue(elemResponseDelete, "Ack", "Failure");
                    f1.write(today + ": ResponseXML returns ack " + ackDelete + "\n");
                    
                    if (!ackDelete.equals("Success") && !ackDelete.equals("Warning")) {   //if ack failure -- START
                        List<? extends Element> errorElements = UtilXml.childElementList(elemResponseDelete, "Errors");
                        Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                        StringBuffer errorMessage = new StringBuffer();
                        while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                            Element errorElement = errorElementsElemIter.next();
                            String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                            String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                            errorMessage.append(shortMessage + " - " + longMessage);
                        }   //while errorElement -- END
                        Debug.logError(productStoreId + ": Failed deleting Promotional sale ID " + promotionalSaleIdRemove + ", errorMessage: " + errorMessage, module);
                        f1.write(today + ": Failed deleting Promotional sale for promotionalSaleId " + promotionalSaleIdRemove + ", errorMessage: " + errorMessage + "\n");
                    }   //if ack failure -- END
                    else {
                        delegator.removeByAnd("EbayPromotionalSale", UtilMisc.toMap("promotionalSaleId", promotionalSaleIdRemove, "productStoreId", productStoreId));
                        Debug.logError(productStoreId + ": Successfully delete Promotional sale ID " + promotionalSaleIdRemove, module);
                        f1.write(today + ": Successfully delete promotionalSaleId " + promotionalSaleIdRemove + " from ebayPromotionalSale\n");
                    }
                    //remove the promotion on eBay -- END ====================
                }   //loop ebayPromotionalSaleAll -- END
                f1.write(today + ": Finished removing all eBay promotional Sale\n");
                f1.write(today + ": Starting to recreate eBay promotional Sale\n");
                //recreate the promotion on eBay -- START ====================
                double i = 5.0;
                double maxDiscount = Double.parseDouble(productStoreEbaySetting.getString("discountDuration"));
                while (i <= maxDiscount) { //loop create discount -- START
                    String discountValue = i + "";
                    String promotionalSaleName = "Auto markdown " + i + "% off - " + discountDuration + " day(s)";
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
                    SimpleDateFormat sdfEbay = new SimpleDateFormat("yyyy-MM-dd'T'" + discountEndTime + ":00:00'Z'");
                    SimpleDateFormat sdfEbayStart = new SimpleDateFormat("yyyy-MM-dd'T'" + discountEndTime + ":00:00'Z'");
                    Calendar endDay = Calendar.getInstance();
                    endDay.set(Calendar.DATE, endDay.get(Calendar.DATE) + Integer.parseInt(discountDuration));
                    Timestamp endDate = Timestamp.valueOf(sdf.format(endDay.getTime()));
                    
                    Calendar startDay = Calendar.getInstance();
                    Timestamp startDate = Timestamp.valueOf(sdf.format(startDay.getTime()));
                    String discountStartDate = sdfEbayStart.format(startDate.getTime());
                    String discountEndDate = sdfEbay.format(endDate.getTime());
                    //Debug.logError("discountEndDate : " + discountEndDate, module);
                    //Debug.logError("discountStartDate : " + discountStartDate, module);
                    /*String endHour = promotionalSaleEndTime.substring(11,13);
                     String endMinute = promotionalSaleEndTime.substring(14,16);
                     if (Integer.parseInt(endMinute) >= 55) {
                     endHour = (Integer.parseInt(endHour) + 1) + "";
                     endMinute = "00";
                     } else {
                     endMinute = Integer.parseInt(endMinute) + 5 + "";
                     if (endMinute.length() == 1) {
                     endMinute = "0" + endMinute;
                     }
                     }*/
                    String callName = "SetPromotionalSale";
                    mapAccount.put("callName", callName);
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    UtilXml.addChildElementValue(rootElem, "Action", "Add", rootDoc);
                    Element promotionalSaleDetailsElem = UtilXml.addChildElement(rootElem, "PromotionalSaleDetails", rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "DiscountType", discountType, rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "DiscountValue", discountValue, rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "PromotionalSaleName", promotionalSaleName, rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "PromotionalSaleStartTime", discountStartDate, rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "PromotionalSaleEndTime", discountEndDate, rootDoc);
                    UtilXml.addChildElementValue(promotionalSaleDetailsElem, "PromotionalSaleType", promotionalSaleType, rootDoc);
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    Debug.logError(requestXMLcode, module);
                    String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                    //Debug.logError(responseXML, module);
                    Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                    Element elemResponse = docResponse.getDocumentElement();
                    String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                    
                    if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                        List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                        Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                        StringBuffer errorMessage = new StringBuffer();
                        while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                            Element errorElement = errorElementsElemIter.next();
                            String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                            String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                            errorMessage.append(shortMessage + " - " + longMessage);
                        }   //while errorElement -- END
                        Debug.logError(productStoreId + ": Failed set Promotional sale for " + i + "off and errorMessage: " + errorMessage, module);
                    }   //if ack failure -- END
                    else {
                        String promotionalSaleId = UtilXml.childElementValue(elemResponse, "PromotionalSaleID", null);
                        Debug.logError(productStoreId + ": Successfully set Promotional sale for " + i + "off", module);
                        GenericValue ebayPromotionalSale = delegator.makeValue("EbayPromotionalSale", UtilMisc.toMap("promotionalSaleId", promotionalSaleId, "productStoreId", productStoreId));
                        ebayPromotionalSale.set("discountType", discountType);
                        ebayPromotionalSale.set("discountValue", discountValue);
                        ebayPromotionalSale.set("promotionalSaleName", promotionalSaleName);
                        ebayPromotionalSale.set("promotionalSaleType", promotionalSaleType);
                        ebayPromotionalSale.set("promotionalSaleStartTime", discountStartDate);
                        ebayPromotionalSale.set("promotionalSaleEndTime", discountEndDate);
                        delegator.createOrStore(ebayPromotionalSale);
                    }
                    i = i + discountGapInt;
                }   //loop create discount -- END
                //recreate the promotion on eBay -- END ====================
                f1.write(today + ": Finished recreating all eBay promotional Sale\n");
                //==== END delete-recreate ebayPromotionalSale ====
                
                
                List<String> currentDiscountList = new LinkedList<String>();
                for (GenericValue uniqueDiscount : waitingForMarkdownList) {    //loop waitingForMarkdownList to get unique currentDiscount -- START
                    currentDiscountList.add(uniqueDiscount.getDouble("currentDiscount").toString());
                }   //loop waitingForMarkdownList to get unique currentDiscount -- END
                HashSet<String> uniqueCurrentDiscount = new HashSet<String>(currentDiscountList);
                for (String currentDiscount : uniqueCurrentDiscount) {  //loop uniqueCurrentDiscount -- START
                    f1.write(today + ": ===== START ===== currentDiscount " + currentDiscount + "\n");
                    List<GenericValue> ebayPromotionalSaleList = delegator.findByAnd("EbayPromotionalSale", UtilMisc.toMap("productStoreId", productStoreId, "discountValue", currentDiscount, "promotionalSaleName", "Auto markdown " + currentDiscount + "% off - " + discountDuration + " day(s)"), null, false);
                    if (UtilValidate.isNotEmpty(ebayPromotionalSaleList)) { //if ebayPromotionalSale is not empty -- START
                        GenericValue ebayPromotionalSale = EntityUtil.getFirst(ebayPromotionalSaleList);
                        String promotionalSaleId = ebayPromotionalSale.getString("promotionalSaleId");
                        f1.write(today + ": promotionalSaleId is " + promotionalSaleId + "\n");
                        List<GenericValue> waitingForMarkdownDiscountList = delegator.findByAnd("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId, "currentDiscount", Double.parseDouble(currentDiscount)), null, false);
                        
                        
                        //setPromotionalSaleListings-- START
                        String callName = "SetPromotionalSaleListings";
                        mapAccount.put("callName", callName);
                        
                        //Building XML -- START
                        Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                        Element rootElem = rootDoc.getDocumentElement();
                        rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                        
                        //RequesterCredentials -- START
                        Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                        UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                        //RequesterCredentials -- END
                        
                        UtilXml.addChildElementValue(rootElem, "Action", "Add", rootDoc);
                        UtilXml.addChildElementValue(rootElem, "PromotionalSaleID", promotionalSaleId, rootDoc);
                        
                        Element promotionalSaleItemIDArray = UtilXml.addChildElement(rootElem, "PromotionalSaleItemIDArray", rootDoc);
                        for (GenericValue waitingForMarkdownDiscount: waitingForMarkdownDiscountList) { //loop waitingForMarkdownDiscountList -- START
                            UtilXml.addChildElementValue(promotionalSaleItemIDArray, "ItemID", waitingForMarkdownDiscount.getString("itemId"), rootDoc);
                        }   //loop waitingForMarkdownDiscountList -- END
                        
                        //Building XML -- END
                        
                        String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                        f1.write(today + ": Finished building requestXML to add listings into promotional Sale ID " + promotionalSaleId + "\n");
                        f1.write("===== RequestXML =====\n");
                        f1.write(requestXMLcode);
                        f1.write("===== RequestXML =====\n");
                        /*FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/requestXML/markdown-" + productStoreId + "-" + newPromotionalSaleId + "-" + today + ".xml", true);
                         f1.write(requestXMLcode.toString());
                         f1.close();*/
                        //Debug.logError(requestXMLcode, module);
                        f1.write(today + ": Sending requestXML to add listings into promotional Sale ID " + promotionalSaleId + "\n");
                        String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                        //Debug.logError(responseXML, module);
                        Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                        Element elemResponse = docResponse.getDocumentElement();
                        String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                        f1.write(today + ": Retrieved responseXML to add listings into promotional Sale ID " + promotionalSaleId + ". Ack is " + ack + "\n");
                        if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                            List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                            Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                            StringBuffer errorMessage = new StringBuffer();
                            while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                                Element errorElement = errorElementsElemIter.next();
                                String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                                String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                                errorMessage.append(shortMessage + " - " + longMessage);
                            }   //while errorElement -- END
                            Debug.logError(productStoreId + ": Failed setPromotionalSalesListing for ID " +  promotionalSaleId + " (" + currentDiscount + "% off), errorMessage: " + errorMessage, module);
                            f1.write(today + ": Failed setPromotionalSalesListing for ID " +  promotionalSaleId + " (" + currentDiscount + "% off), errorMessage: " + errorMessage + "\n");
                            delegator.removeByAnd("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId));
                            f1.write(today + ": WaitingForMarkdown list has been cleaned\n");
                        } else {    //if apply markdown to listing successful -- START
                            f1.write(today + ": Successfully add listings into promotional Sale ID " + promotionalSaleId + "\n");
                            delegator.removeByAnd("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId));
                            f1.write(today + ": WaitingForMarkdown list has been cleaned\n");
                        }   //if apply markdown to listing successful -- END
                        
                        //setPromotionalSaleListings-- END
                    }   //if ebayPromotionalSale is not empty -- END
                    else {  //if ebayPromotionalSale is empty -- START
                        Debug.logError(productStoreId + ": does not have promotional sale on eBay with " + currentDiscount + "% off", module);
                        f1.write(today + ": Could not find promotional sale on eBay with " + currentDiscount + "% off\n");
                    }   //if ebayPromotionalSale is empty -- END
                    //Get EbayPromotionalSale -- END
                    f1.write(today + ": ===== END ===== currentDiscount " + currentDiscount + "\n");
                }   //loop uniqueCurrentDiscount -- END
                if (true) {
                    delegator.removeByAnd("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId));
                    f1.write(today + ": WaitingForMarkdown list has been cleaned\n");
                }
            }   //if waitingForMarkdownList is not empty -- END
            else {
                Debug.logError("WaitingForMarkdownList is empty", module);
                f1.write(today + ": WaitingForMarkdownList is empty\n");
            }
            f1.write(today + ": Finished running markdown\n");
            f1.close();
        }  //main try -- END
        catch (GenericEntityException e) {
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
    }   //autoMarkdownSingle
    
    public static Map<String, Object> updateMarkdownHistory(DispatchContext dctx, Map context)
	throws GenericEntityException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        Map result = ServiceUtil.returnSuccess();
        
        try {   //main try -- START
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = common.accountInfo(delegator, productStore);
            
            String callName = "GetPromotionalSaleDetails";
            mapAccount.put("callName", callName);

            Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
            Element rootElem = rootDoc.getDocumentElement();
            rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            
            //RequesterCredentials -- START
            Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
            UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
            //RequesterCredentials -- END
            
            UtilXml.addChildElementValue(rootElem, "PromotionalSaleStatus", "Scheduled", rootDoc);
            UtilXml.addChildElementValue(rootElem, "PromotionalSaleStatus", "Active", rootDoc);
            
            String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
            //Debug.logError(requestXMLcode, module);
            String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
            //Debug.logError(responseXML, module);
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");

            if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                StringBuffer errorMessage = new StringBuffer();
                while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                    Element errorElement = errorElementsElemIter.next();
                    String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                    String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                    errorMessage.append(shortMessage + " - " + longMessage);
                }   //while errorElement -- END
                Debug.logError(productStoreId + ": couldnt get promotionalSaleDetails, errorMessage: " + errorMessage, module);
            } else {    //if apply markdown to listing successful -- START
                Element promotionalSaleDetailsElement = UtilXml.firstChildElement(elemResponse, "PromotionalSaleDetails");
                List<? extends Element> promotionalSaleElementList = UtilXml.childElementList(promotionalSaleDetailsElement, "PromotionalSale");
                Iterator<? extends Element> promotionalSaleIterator = promotionalSaleElementList.iterator();
                while(promotionalSaleIterator.hasNext()) {  //loop promotionalSaleIterator -- START
                    Element promotionalSaleElement = promotionalSaleIterator.next();
                    String promotionalSaleName = UtilXml.childElementValue(promotionalSaleElement, "PromotionalSaleName", null);
                    //Debug.logError("promotionalSaleName = " + promotionalSaleName, module);
                    if (promotionalSaleName.toLowerCase().matches("auto markdown.*")) { //check if promotional is automated setup -- START
                        String discountValue = UtilXml.childElementValue(promotionalSaleElement, "DiscountValue", null);
                        Element promotionalSaleItemIdArray = UtilXml.firstChildElement(promotionalSaleElement, "PromotionalSaleItemIDArray");
                        List<? extends Element> itemIdList = UtilXml.childElementList(promotionalSaleItemIdArray, "ItemID");
                        Iterator<? extends Element> itemIdIterator = itemIdList.iterator();
                        while(itemIdIterator.hasNext()) {   //loop itemIdIterator -- START
                            Element itemIdElement = itemIdIterator.next();
                            String itemId = UtilXml.elementValue(itemIdElement);
                            //Debug.logError("ItemID is " + itemId, module);
                            String markdownHistoryId = delegator.getNextSeqId("MarkdownHistory");
                            GenericValue markdownHistory = delegator.makeValue("MarkdownHistory", UtilMisc.toMap("productStoreId", productStoreId, "markdownHistoryId", markdownHistoryId));
                            markdownHistory.set("itemId", itemId);
                            markdownHistory.set("discountValue", Double.parseDouble(discountValue));
                            delegator.create(markdownHistory);
                            
                            GenericValue lastMarkdownListing = delegator.findOne("LastMarkdownListing", UtilMisc.toMap("itemId", itemId), false);
                            if (UtilValidate.isNotEmpty(lastMarkdownListing)) { //if lastMarkdownListing is not empty -- START
                                lastMarkdownListing.set("lastDiscountValue", Double.parseDouble(discountValue));
                                delegator.store(lastMarkdownListing);
                            }   //if lastMarkdownListing is not empty -- END
                            else {  //if lastMarkdownListing is empty -- START
                                lastMarkdownListing = delegator.makeValue("LastMarkdownListing", UtilMisc.toMap("itemId", itemId));
                                lastMarkdownListing.set("lastDiscountValue", Double.parseDouble(discountValue));
                                delegator.create(lastMarkdownListing);
                            }   //if lastMarkdownListing is empty -- END
                        }   //loop itemIdIterator -- END
                    }   //check if promotional is automated setup -- END
                }   //loop promotionalSaleIterator -- END
            }   //if apply markdown to listing successful -- END

        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError(productStoreId + ": finished updateMarkdownHistory", module);
        return result;
        
    }   //updateMarkdownHistory
    
    public static Map<String, Object> rivalListingMonitor(DispatchContext dctx, Map context)
    throws GenericEntityException, SAXException, ParseException, ParserConfigurationException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String itemIdRequest = (String) context.get("itemId");
        String clearData = (String) context.get("clearData");
        Map result = ServiceUtil.returnSuccess();
        //Debug.logError("start running rivalListingMonitor", module);
        
        try {   //main try -- START
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = new Date();
            Calendar now = Calendar.getInstance();
            String snapshotWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + "";
            //Debug.logError("clearData is " + clearData, module);
            if (UtilValidate.isNotEmpty(clearData)) {
                if (clearData.toUpperCase().equals("Y")) {
                    //Debug.logError("clearData is equals to Y", module);
                    if (UtilValidate.isEmpty(itemIdRequest)) {
                        delegator.removeByAnd("RivalListingResult", UtilMisc.toMap("date", new java.sql.Date(now.getTimeInMillis())));
                    } else {
                        List<GenericValue> removeRivalListingResultList = delegator.findByAnd("RivalListingResult", UtilMisc.toMap("date", new java.sql.Date(now.getTimeInMillis()), "rivalItemId", itemIdRequest), null, false);
                        if (UtilValidate.isNotEmpty(removeRivalListingResultList)) {
                            delegator.removeByAnd("RivalListingResult", UtilMisc.toMap("date", new java.sql.Date(now.getTimeInMillis()), "rivalItemId", itemIdRequest));
                        }
                        //Debug.logError("removing rivalListingResult for : " + itemIdRequest + " and date " + new java.sql.Date(nowDate.getTime()), module);
                    }
                    
                }
            }
            
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", "09lijun"), true);
            
            Collection<GenericValue> totalCrawlList = new ArrayList<GenericValue>();
            
            List<EntityCondition> conditions = FastList.newInstance();
            //conditions.add(EntityCondition.makeCondition("crawl", EntityOperator.EQUALS, "Y"));
            conditions.add(EntityCondition.makeCondition("rivalPlatform", EntityOperator.EQUALS, "EBAY"));
            if (UtilValidate.isNotEmpty(itemIdRequest)) {
                conditions.add(EntityCondition.makeCondition("rivalItemId", EntityOperator.EQUALS, itemIdRequest));
            }
            EntityCondition condition = EntityCondition.makeCondition(conditions, EntityOperator.AND);
            List<GenericValue> crawlList = delegator.findList("ProductMasterRival", condition,
                                                                  UtilMisc.toSet("rivalItemId"),
                                                                  UtilMisc.toList("rivalItemId"),
                                                                  new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), true);
            
            List<EntityCondition> similarConditions = FastList.newInstance();
            //similarConditions.add(EntityCondition.makeCondition("crawl", EntityOperator.EQUALS, "Y"));
            similarConditions.add(EntityCondition.makeCondition("rivalPlatform", EntityOperator.EQUALS, "EBAY_SIMILAR"));
            if (UtilValidate.isNotEmpty(itemIdRequest)) {
                similarConditions.add(EntityCondition.makeCondition("similarItemId", EntityOperator.EQUALS, itemIdRequest));
            }
            EntityCondition similarCondition = EntityCondition.makeCondition(similarConditions, EntityOperator.AND);
            List<GenericValue> similarCrawlList = delegator.findList("ProductMasterRival", similarCondition,
                                                              UtilMisc.toSet("similarItemId"),
                                                              UtilMisc.toList("similarItemId"),
                                                              new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), true);
            totalCrawlList.addAll(crawlList);
            totalCrawlList.addAll(similarCrawlList);
            HashSet<GenericValue> uniqueCrawlList = new HashSet<GenericValue>(totalCrawlList);
            
            //Debug.logError("crawlList size: " + crawlList.size(), module);
            for (GenericValue uniqueCrawl : uniqueCrawlList) {    //loop unique crawlList -- START
                //Debug.logError("new Item ID start" , module);
                String itemId = null;
                String rivalItemId = null;
                String similarItemId = null;
                if (UtilValidate.isNotEmpty(uniqueCrawl.getString("similarItemId"))) {
                    itemId = uniqueCrawl.getString("similarItemId");
                    similarItemId = itemId;
                }
                if (UtilValidate.isNotEmpty(uniqueCrawl.getString("rivalItemId"))) {
                    itemId = uniqueCrawl.getString("rivalItemId");
                    rivalItemId = itemId;
                }
                //Debug.logError("new Item ID is " + itemId + " and productIdCheck is: " + productIdCheck, module);
                List<GenericValue> crawlProductCheckList = delegator.findByAnd("ProductMasterRival", UtilMisc.toMap("rivalItemId", itemId), null, false);
                if (UtilValidate.isEmpty(crawlProductCheckList)) {
                    crawlProductCheckList = delegator.findByAnd("ProductMasterRival", UtilMisc.toMap("similarItemId", itemId), null, false);
                }
                boolean crawlCheck = false;
                for (GenericValue crawlProductCheck : crawlProductCheckList) {  //loop crawlProductCheckList == START
                    String productIdCheck = crawlProductCheck.getString("productId");
                    GenericValue pmCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productIdCheck), true);
                    String productGroup = pmCheck.getString("productGroup");
                    if (!productGroup.equals("G4")) {
                        crawlCheck = true;
                    }
                    else {
                        crawlProductCheck.set("crawl", "N");
                        delegator.store(crawlProductCheck);
                    }
                }   //loop crawlProductCheckList == END
                
                if (crawlCheck) { //if productGroup is G0123 == START
                    //Debug.logError("Unique itemId " + itemId, module);
                    EntityCondition crawlCondition = null;
                    if (UtilValidate.isNotEmpty(similarItemId)) {
                        crawlCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                       //EntityCondition.makeCondition("crawl", EntityOperator.EQUALS, "Y"),
                                                                                       EntityCondition.makeCondition("rivalPlatform", EntityOperator.EQUALS, "EBAY_SIMILAR"),
                                                                                       EntityCondition.makeCondition("similarItemId", EntityOperator.EQUALS, itemId)
                                                                                       ));
                    } else if (UtilValidate.isNotEmpty(rivalItemId)) {
                        crawlCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                       //EntityCondition.makeCondition("crawl", EntityOperator.EQUALS, "Y"),
                                                                                       EntityCondition.makeCondition("rivalPlatform", EntityOperator.EQUALS, "EBAY"),
                                                                                       EntityCondition.makeCondition("rivalItemId", EntityOperator.EQUALS, itemId)
                                                                                       ));
                    }
                    
                    double rivalShipping = 0.0;
                    double rivalCurrentPrice = 0.0;
                    double rivalOriginalPrice = 0.0;
                    double rivalListingLifetime = 0.0;
                    double rivalHistorySold = 0.0;
                    double rivalDailySales = 0.0;
                    double samePriceProfitPercentage = 0.0;
                    double soldPerDay = 0.0;
                    String rivalCurrency = null;
                    String rank = "E";
                    String countryLocation = null;
                    int variationCount = 1;
                    
                    //Debug.logError("Sending Request", module);
                    Map mapAccount = FastMap.newInstance();
                    mapAccount = common.accountInfo(delegator, productStore);
                    
                    //remove the promotion on eBay -- START ====================
                    String callName = "GetItem";
                    mapAccount.put("callName", callName);
                    
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    UtilXml.addChildElementValue(rootElem, "ItemID", itemId, rootDoc);
                    UtilXml.addChildElementValue(rootElem, "IncludeItemSpecifics", "false", rootDoc);
                    UtilXml.addChildElementValue(rootElem, "DetailLevel", "ReturnAll", rootDoc);
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    //Building XML -- END
                    
                    String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                    //Debug.logError(responseXML, module);
                    Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                    Element elemResponse = docResponse.getDocumentElement();
                    String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                    //Debug.logError("run until here", module);
                    if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                        List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                        Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                        StringBuffer errorMessage = new StringBuffer();
                        while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                            Element errorElement = errorElementsElemIter.next();
                            String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                            String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                            errorMessage.append(shortMessage + " - " + longMessage);
                        }   //while errorElement -- END
                        if (errorMessage.toString().matches(".*listing has been deleted.*") || errorMessage.toString().matches(".*Item Not Found.*")) {   //if rival listing does not exist -- START
                            String productIdTask = null;
                            String singleProductIdTask = null;
                            List<GenericValue> ebayRivalToBeRemovedList = null;
                            if (UtilValidate.isNotEmpty(similarItemId)) {
                                ebayRivalToBeRemovedList = delegator.findByAnd("ProductMasterRival", UtilMisc.toMap("similarItemId", itemId, "rivalPlatform", "EBAY_SIMILAR"), null, false);
                            } else if (UtilValidate.isNotEmpty(rivalItemId)) {
                                ebayRivalToBeRemovedList = delegator.findByAnd("ProductMasterRival", UtilMisc.toMap("rivalItemId", itemId, "rivalPlatform", "EBAY"), null, false);
                            }
                            for (GenericValue ebayRivalToBeRemoved: ebayRivalToBeRemovedList) { //loop ebayRivalToBeRemovedList == START
                                String productIdRemove = ebayRivalToBeRemoved.getString("productId");
                                if (UtilValidate.isEmpty(productIdTask)) {
                                    productIdTask = productIdRemove;
                                } else {
                                    productIdTask = productIdTask + eol + productIdRemove;
                                }
                                singleProductIdTask = productIdRemove;
                                GenericValue crawlRemove = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productIdRemove, "rivalPlatform", "REFERENCE"), false);
                                if (UtilValidate.isNotEmpty(crawlRemove)) { //if crawlRemove empty
                                    crawlRemove.set("rivalLink", itemId);
                                    crawlRemove.set("crawl", "N");
                                    delegator.store(crawlRemove);
                                } else {
                                    crawlRemove = delegator.makeValue("ProductMasterRival", UtilMisc.toMap("productId", productIdRemove, "rivalPlatform", "REFERENCE"));
                                    crawlRemove.set("rivalLink", itemId);
                                    crawlRemove.set("crawl", "N");
                                    delegator.create(crawlRemove);
                                }   //if crawlRemove empty
                                delegator.removeValue(ebayRivalToBeRemoved);
                            }   //loop ebayRivalToBeRemovedList == END
                            //Debug.logError("removing " + itemId + " from crawl", module);
                            
                            //creating task
                            /*boolean createVeroTask = false;
                            GenericValue pmTask = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", singleProductIdTask), true);
                            if (pmTask.getString("statusId").equals("DISCONTINUED")) {
                                List<String> gudaoProductStoreList = new ArrayList<String>();
                                List<GenericValue> productStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("storeName", "GUDAO"), null, false);
                                for (GenericValue productStoreGV : productStoreList) {
                                    gudaoProductStoreList.add(productStoreGV.getString("productStoreId"));
                                }
                                
                                EntityCondition veroCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                          EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, pmTask.getString("productId")),
                                                                                                          EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList)
                                                                                                          ));
                                List<GenericValue> veroCheckEbayActiveListingVarList = delegator.findList("EbayActiveListingVariation", veroCondition, null, null, null, false);
                                if (UtilValidate.isNotEmpty(veroCheckEbayActiveListingVarList)) {  //if veroCheckEbayActiveListingVarList is not empty == START
                                    for (GenericValue veroCheckEbayActiveListingVarGV : veroCheckEbayActiveListingVarList) {
                                        List<GenericValue> veroCheckEbayActiveListing = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", veroCheckEbayActiveListingVarGV.getString("itemId")), null, false);
                                        for (GenericValue veroCheckEbayActiveListingGV : veroCheckEbayActiveListing) {
                                            if (veroCheckEbayActiveListingGV.getString("sellStatListingStatus").equals("Active")) {
                                                createVeroTask = true;
                                            }
                                        }
                                    }
                                }   //if veroCheckEbayActiveListingVarList is not empty == END
                                else {
                                    veroCheckEbayActiveListingVarList = delegator.findList("EbayActiveListing", veroCondition, null, null, null, false);
                                    for (GenericValue veroCheckEbayActiveListingGV : veroCheckEbayActiveListingVarList) {
                                        if (veroCheckEbayActiveListingGV.getString("sellStatListingStatus").equals("Active")) {
                                            createVeroTask = true;
                                        }
                                    }
                                }
                            } else {
                                createVeroTask = true;
                            }
                            
                            if (createVeroTask) {   //if createVeroTask is true == START
                                GenericValue taskHeader = delegator.makeValue("GudaoTaskHeader", UtilMisc.toMap("taskId", delegator.getNextSeqId("GudaoTaskHeader")));
                                taskHeader.set("type", "RIVAL_REMOVED");
                                taskHeader.set("priority", "HIGH");
                                taskHeader.set("statusId", "PENDING");
                                taskHeader.set("createdDate", UtilDateTime.nowTimestamp());
                                taskHeader.set("createdBy", "system");
                                taskHeader.set("receiver", pmTask.getString("ownerGroup") + "_GROUP");
                                taskHeader.set("description", "对手链接 " + itemId + " 有可能是VERO. SKU: " + eol + productIdTask);
                                delegator.create(taskHeader);
                            }   //if createVeroTask is true == END*/
                        }   //if rival listing does not exist -- END
                        //Debug.logError("rivalItemID: " + itemId + ", errorMessage: " + errorMessage, module);
                    }   //if ack failure -- END
                    else {  //if ack Success -- START
                        List<? extends Element> itemElements = UtilXml.childElementList(elemResponse, "Item");
                        Iterator<? extends Element> itemsElemIter = itemElements.iterator();
                        while (itemsElemIter.hasNext()) {    //loop itemsElemIter -- START
                            Map itemMap = FastMap.newInstance();
                            Element itemElement = itemsElemIter.next();
                            rivalCurrency = UtilXml.childElementValue(itemElement, "Currency", null);
                            countryLocation = UtilXml.childElementValue(itemElement, "Country", null);
                            //Debug.logError("rivalCurrency: " + rivalCurrency, module);
                            
                            //get prices -- START
                            List<? extends Element> sellingStatusList = UtilXml.childElementList(itemElement, "SellingStatus");
                            Iterator<? extends Element> sellingStatusListElemIter = sellingStatusList.iterator();
                            while (sellingStatusListElemIter.hasNext()) {   //loop sellingStatusListElemIter -- START
                                Element sellingStatusElement = sellingStatusListElemIter.next();
                                rivalCurrentPrice = Double.valueOf(UtilXml.childElementValue(sellingStatusElement, "CurrentPrice", null));
                                //Debug.logError("rivalCurrentPrice: " + rivalCurrentPrice, module);
                                rivalHistorySold = Double.valueOf(UtilXml.childElementValue(sellingStatusElement, "QuantitySold", null));
                                //Debug.logError("rivalHistorySold: " + rivalHistorySold, module);
                                if (UtilValidate.isEmpty(rivalHistorySold)) {
                                    rivalHistorySold = 0;
                                }
                                List<? extends Element> promoList = UtilXml.childElementList(itemElement, "PromotionalSaleDetails");
                                Iterator<? extends Element> promoListElemIter = promoList.iterator();
                                while (promoListElemIter.hasNext()) {   //loop promoListElemIter -- START
                                    Element promoListElement = promoListElemIter.next();
                                    rivalOriginalPrice = Double.valueOf(UtilXml.childElementValue(promoListElement, "OriginalPrice", null));
                                    if (UtilValidate.isEmpty(rivalOriginalPrice)) {
                                        rivalOriginalPrice = 0.0;
                                    }
                                }  //loop promoListElemIter -- END
                            }   //loop sellingStatusListElemIter -- END
                            //get prices -- END
                            
                            //get Shipping -- START
                            List<? extends Element> shippingDetailList = UtilXml.childElementList(itemElement, "ShippingDetails");
                            Iterator<? extends Element> shippingDetailListElemIter = shippingDetailList.iterator();
                            while (shippingDetailListElemIter.hasNext()) {   //loop shippingDetailListElemIter -- START
                                Element shippingDetailElement = shippingDetailListElemIter.next();
                                List<? extends Element> internationalShippingList = UtilXml.childElementList(shippingDetailElement, "InternationalShippingServiceOption");
                                if (UtilValidate.isEmpty(internationalShippingList)) {
                                    internationalShippingList = UtilXml.childElementList(shippingDetailElement, "ShippingServiceOptions");
                                }
                                Iterator<? extends Element> internationalShippingListElemIter = internationalShippingList.iterator();
                                while (internationalShippingListElemIter.hasNext()) {   //loop internationalShippingListElemIter -- START
                                    Element internationalShippingElement = internationalShippingListElemIter.next();
                                    if (UtilXml.childElementValue(internationalShippingElement, "ShippingServicePriority", null).equals("1")) { //if priority is 1 -- START
                                        String shippingServiceCostVar = UtilXml.childElementValue(internationalShippingElement, "ShippingServiceCost", null);
                                        if (UtilValidate.isNotEmpty(shippingServiceCostVar)) {
                                            rivalShipping = Double.valueOf(shippingServiceCostVar);
                                        }
                                    }   //if priority is 1 -- END
                                }   //loop internationalShippingListElemIter -- END
                            }   //loop shippingDetailListElemIter -- END
                            //get Shipping -- END
                            //Debug.logError("rivalShipping " + rivalShipping, module);
                            //get rivalCurrentPrice -- START
                            rivalCurrentPrice += rivalShipping;
                            if (rivalOriginalPrice > 0.0) {
                                rivalOriginalPrice += rivalShipping;
                            }
                            
                            //get rivalCurrentPrice -- END
                            
                            //calculate rivalListingLifetime -- START
                            List<? extends Element> listingDetailList = UtilXml.childElementList(itemElement, "ListingDetails");
                            Iterator<? extends Element> listingDetailListElemIter = listingDetailList.iterator();
                            while (listingDetailListElemIter.hasNext()) {   //loop listingDetailListElemIter -- START
                                Element listingDetailElement = listingDetailListElemIter.next();
                                String rivalListDetStartTime = UtilXml.childElementValue(listingDetailElement, "StartTime", null).substring(0,10);
                                Date rivalListingStartDate = sdf.parse(rivalListDetStartTime);
                                rivalListingLifetime = Double.valueOf((Math.abs(nowDate.getTime() - rivalListingStartDate.getTime()) / (24 * 60 * 60 * 1000)));
                                //Debug.logError("rivalListDetStartTime: " + rivalListDetStartTime + ", rivalListingLifetime: " + rivalListingLifetime, module);
                            }   //loop listingDetailListElemIter -- END
                            //calculate rivalListingLifetime -- END
                            
                            //calculate variations -- START
                            Element variationsElement = UtilXml.firstChildElement(itemElement, "Variations");
                            if (UtilValidate.isNotEmpty(variationsElement)) {   //if variationsElement is not empty -- START
                                List<? extends Element> variation = UtilXml.childElementList(variationsElement, "Variation");
                                Iterator<? extends Element> variationElemIter = variation.iterator();
                                while (variationElemIter.hasNext()) {
                                    Element variationElement = variationElemIter.next();
                                    //Debug.logError("startPrice " + UtilXml.childElementValue(variationElement, "StartPrice", null), module);
                                    if (UtilValidate.isNotEmpty(variationElement)) {
                                        //Debug.logError("variationCount: " + variationCount, module);
                                        variationCount++;
                                    }
                                }
                            }   //if variationsElement is not empty -- END
                            //calculate variations -- END
                            
                            //calculate rivalDailySales -- START
                            //Debug.logError("rivalHistorySold: " + rivalHistorySold + ",rivalCurrentPrice: " + rivalCurrentPrice + ", rivalListingLifetime: " + rivalListingLifetime + ", variationCount: " + variationCount + ", divided: " + (((double)variationCount)/2), module);
                            if (rivalHistorySold > 0.0) {
                                rivalDailySales = (rivalHistorySold * rivalCurrentPrice) / rivalListingLifetime / (((double) variationCount)/2);   //variationCount / 2 is based on Li request for the sake of fairness
                                soldPerDay = rivalHistorySold / rivalListingLifetime / (((double) variationCount)/2);
                                
                                /*List<GenericValue> rivalListingResultList = delegator.findByAnd("RivalListingResult", UtilMisc.toMap("rivalItemId", itemId), "date", false);
                                 if (rivalListingResultList.size() >= 4) {    //if Crawling has run for one month == START
                                 
                                 }   //if Crawling has run for one month == END
                                 else {  //if Crawling has NOT run for one month divides by listing lifetime == START
                                 rivalDailySales = (rivalHistorySold * rivalCurrentPrice) / rivalListingLifetime / (((double) variationCount)/2);   //variationCount / 2 is based on Li request for the sake of fairness
                                 soldPerDay = rivalHistorySold / rivalListingLifetime / (((double) variationCount)/2);
                                 }   //if Crawling has NOT run for one month divides by listing lifetime == END*/
                            } else {
                                rivalDailySales = 0.0;
                            }
                            
                            //calculate rivalDailySales -- END
                            
                        }   //loop itemsElemIter -- END
                    }   //if ack Success -- END
                    
                    
                    GenericValue rivalListingResult = delegator.makeValue("RivalListingResult", UtilMisc.toMap("rivalItemId", itemId));
                    rivalListingResult.set("rivalHistorySold", rivalHistorySold);
                    rivalListingResult.set("rivalCurrentPrice", rivalCurrentPrice);
                    rivalListingResult.set("rivalOriginalPrice", rivalOriginalPrice);
                    rivalListingResult.set("currency", rivalCurrency);
                    rivalListingResult.set("snapshotWeek", snapshotWeek);
                    rivalListingResult.set("variationCount", Long.valueOf(variationCount));
                    rivalListingResult.set("date", new java.sql.Date(nowDate.getTime()));
                    delegator.createOrStore(rivalListingResult);
                    //Debug.logError("run until here", module);
                    List<GenericValue> crawlListDuplicate = delegator.findList("ProductMasterRival", crawlCondition, null, null, null, false);
                    for (GenericValue crawl : crawlListDuplicate) { //loop crawlList -- START
                        String productId = crawl.getString("productId");
                        //Debug.logError("productId: " + productId, module);
                        samePriceProfitPercentage = samePriceProfitPercentageCalculation(delegator, productId, rivalCurrentPrice, rivalCurrency);
                        //Debug.logError("run until here1", module);
                        //Ranking -- START
                        /*
                        if (rivalDailySales >= 5 && samePriceProfitPercentage >= 0.15) {    //S or A ranking -- START
                            if (rivalDailySales * samePriceProfitPercentage >= 5) { // S ranking -- START
                                rank = "S";
                            }   // S ranking -- END
                            else {  //A ranking -- START
                                rank = "A";
                            }   //A ranking -- END
                        }   //S or A ranking -- START
                        else {  //B, C, D ranking -- START
                            if (rivalDailySales <= 0.5 || samePriceProfitPercentage < 0.1) {    //D ranking -- START
                                rank = "D";
                            }   //D ranking -- END
                            else {  //B or C ranking -- START
                                if (rivalDailySales * samePriceProfitPercentage < 0.5) {    //C ranking
                                    rank = "C";
                                }   //C ranking
                                else {  //B ranking
                                    rank = "B";
                                }   //B ranking
                            }   //B or C ranking -- END
                        }   //B, C, D ranking -- START
                        //Ranking -- END
                        */
                        //Debug.logError("run until here2", module);
                        //update database -- START
                        boolean updateDatabase = true;
                        if (countryLocation.equals("US") && rivalCurrency.equals("USD")) {   //US local seller == START
                            updateDatabase = false;
                        }   //US local seller == END
                        if (countryLocation.equals("GB") && rivalCurrency.equals("GBP")) {   //GB local seller == START
                            updateDatabase = false;
                        }   //GB local seller == END
                        if (countryLocation.equals("VG") && rivalCurrency.equals("EUR")) {   //VG local seller == START
                            updateDatabase = false;
                        }   //VG local seller == END
                        if (countryLocation.equals("AU") && rivalCurrency.equals("AUD")) {   //AU local seller == START
                            updateDatabase = false;
                        }   //AU local seller == END
                        if (countryLocation.equals("DE") && rivalCurrency.equals("EUR")) {   //DE local seller == START
                            updateDatabase = false;
                        }   //DE local seller == END
                        if (countryLocation.equals("IT") && rivalCurrency.equals("EUR")) {   //IT local seller == START
                            updateDatabase = false;
                        }   //IT local seller == END
                        if (countryLocation.equals("FR") && rivalCurrency.equals("EUR")) {   //FR local seller == START
                            updateDatabase = false;
                        }   //FR local seller == END
                        if (countryLocation.equals("ES") && rivalCurrency.equals("EUR")) {   //ES local seller == START
                            updateDatabase = false;
                        }   //ES local seller == END
                        //Debug.logError("run until here2: updateDatabase is " + updateDatabase, module);
                        if (updateDatabase) {   //updateDatabase is true == START
                            //Debug.logError("updateDb is true: START", module);
                            crawl.set("rank", rank);
                            crawl.set("samePriceProfitPercentage", samePriceProfitPercentage);
                            crawl.set("dailySales", rivalDailySales);
                            crawl.set("totalPrice", rivalCurrentPrice);
                            crawl.set("historySold", rivalHistorySold);
                            crawl.set("currency", rivalCurrency);
                            crawl.set("soldPerDay", soldPerDay);
                            delegator.store(crawl);
                            //Debug.logError("updateDb is true: END", module);
                        }   //updateDatabase is true == END
                        else {  //updateDatabase is false == START
                            //Debug.logError("updateDb is false: START", module);
                            if (UtilValidate.isNotEmpty(similarItemId)) {
                                delegator.removeByAnd("ProductMasterRival", UtilMisc.toMap("productId", productId, "similarItemId", itemId));
                            } else if (UtilValidate.isNotEmpty(rivalItemId)) {
                                delegator.removeByAnd("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalItemId", itemId));
                            }
                            
                            GenericValue crawlRemove = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalPlatform", "REFERENCE"), false);
                            if (UtilValidate.isNotEmpty(crawlRemove)) { //if crawlRemove empty
                                crawlRemove.set("rivalLink", itemId);
                                crawlRemove.set("crawl", "N");
                                delegator.store(crawlRemove);
                            } else {
                                crawlRemove = delegator.makeValue("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalPlatform", "REFERENCE"));
                                crawlRemove.set("rivalLink", itemId);
                                crawlRemove.set("crawl", "N");
                                delegator.create(crawlRemove);
                            }   //if crawlRemove empty
                            //Debug.logError("updateDb is false: END", module);
                        }   //updateDatabase is false == END
                        //update database -- END
                    }   //loop crawlList -- END
                }   //if productGroup is G0123 == START
                //Debug.logError("Change itemId", module);
            }   //loop unique crawlList -- END
        }   //main try -- END
        catch (GenericEntityException e) {
            Debug.logError("Yasin: rivalListingMonitor GenericEntityException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (SAXException e) {
            Debug.logError("Yasin: rivalListingMonitor SAXException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (ParseException e) {
            Debug.logError("Yasin: rivalListingMonitor ParseException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (ParserConfigurationException e) {
            Debug.logError("Yasin: rivalListingMonitor ParserConfigurationException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (IOException e) {
            Debug.logError("Yasin: rivalListingMonitor IOException Error for TRY CATCH: " + e.getMessage(), module);
        }

        //Debug.logError("Finished running rivalListingMonitor", module);
        return result;
        
    }   //rivalListingMonitor
    
    private static double samePriceProfitPercentageCalculation(Delegator delegator, String productId, double price, String currency)
    throws GenericEntityException   {
        
        double result = 0.0;
        try {
            GenericValue product = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
            
            String productType = product.getString("productType");
            double weightDb = Double.parseDouble(product.getString("weight"));
            if (UtilValidate.isNotEmpty(product.getString("actualWeight"))) {
                weightDb = Double.parseDouble(product.getString("actualWeight"));
            }
            double productCost = product.getBigDecimal("actualPrice").doubleValue();
            double packagingCost = Double.parseDouble(EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "packagingCost"), null, false)).getString("value"));
            double shippingCost = 0.0;
            double usdToRmb = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CNY"), null, false)).getDouble("conversionFactor");
            double currencyRate = 1.0;
            double discountRate = 0.0;
            double pricePerGram = 0.0;
            double additionalShipCost = 0.0;
            double usdToGbp = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "GBP"), null, false)).getDouble("conversionFactor");
            double usdToAud = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "AUD"), null, false)).getDouble("conversionFactor");
            double usdToCad = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CAD"), null, false)).getDouble("conversionFactor");
            double usdToEur = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "EUR"), null, false)).getDouble("conversionFactor");
            double hkdToRmb = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","HKD", "uomIdTo", "CNY"), null, false)).getDouble("conversionFactor");
            double usdToUsd = 1;
            String feeType = null;
            
            if (currency.equals("USD")) {
                currencyRate = 1.0;
                feeType = "ebayFeeBreakUS";
                
                GenericValue productTypeShippingMethod = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productTypeShippingMethod", "name", productType), null, false));
                String carrierId = productTypeShippingMethod.getString("value");
                List<GenericValue> priceBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", carrierId), null, false);
                String priceBreakShippingValue = "REGISTERED";
                
                for (GenericValue priceBreakShipping : priceBreakShippingList) {    //loop priceBreakShippingList -- START
                    BigDecimal priceBreakLoBD = priceBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal priceBreakHiBD = priceBreakShipping.getBigDecimal("priceBreakHi");
                    if (((price * usdToRmb) >= priceBreakLoBD.doubleValue()) && ((price * usdToRmb) < priceBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        priceBreakShippingValue = priceBreakShipping.getString("value");
                        break;
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop priceBreakShippingList -- END
                
                GenericValue shippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", priceBreakShippingValue), false);
                discountRate = Double.valueOf(shippingCostGV.getLong("discountRate"));
                pricePerGram = shippingCostGV.getDouble("pricePerGram");
                if (UtilValidate.isNotEmpty(shippingCostGV.getDouble("additionalCost"))) {
                    additionalShipCost = shippingCostGV.getDouble("additionalCost");
                }
            } else if (currency.equals("GBP")) {
                currencyRate = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "GBP"), null, false)).getDouble("conversionFactor");
                feeType = "ebayFeeBreakUK";
                
                GenericValue productTypeShippingMethod = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productTypeShippingMethod", "name", productType), null, false));
                String carrierId = productTypeShippingMethod.getString("value");
                List<GenericValue> costBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", carrierId), null, false);
                String costBreakShippingValue = "REGISTERED";
                
                GenericValue marketplaceProfitUK = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "UK"), null, false));
                GenericValue lowestPriceUK = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "UK"), null, false));
                String profitPercentUKStr = marketplaceProfitUK.getString("value");
                String ukLowestProfitPercentage = lowestPriceUK.getString("priceBreakLo");
                String lowestPriceUKStr = lowestPriceUK.getString("value");
                
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((price / usdToGbp * usdToRmb) >= costBreakLoBD.doubleValue()) && ((price / usdToGbp * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                GenericValue shippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                discountRate = Double.valueOf(shippingCostGV.getLong("discountRate"));
                pricePerGram = shippingCostGV.getDouble("pricePerGram");
                if (UtilValidate.isNotEmpty(shippingCostGV.getDouble("additionalCost"))) {
                    additionalShipCost = shippingCostGV.getDouble("additionalCost");
                }

            } else if (currency.equals("AUD")) {
                currencyRate = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "AUD"), null, false)).getDouble("conversionFactor");
                feeType = "ebayFeeBreakAU";
                
                GenericValue productTypeShippingMethod = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productTypeShippingMethod", "name", productType), null, false));
                String carrierId = productTypeShippingMethod.getString("value");
                List<GenericValue> costBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", carrierId), null, false);
                String costBreakShippingValue = "REGISTERED";
                
                GenericValue marketplaceProfitAU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "AU"), null, false));
                GenericValue lowestPriceAU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "AU"), null, false));
                String profitPercentAUStr = marketplaceProfitAU.getString("value");
                String auLowestProfitPercentage = lowestPriceAU.getString("priceBreakLo");
                String lowestPriceAUStr = lowestPriceAU.getString("value");
                
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((price / usdToAud * usdToRmb) >= costBreakLoBD.doubleValue()) && ((price / usdToAud * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                GenericValue auShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                discountRate = Double.valueOf(auShippingCostGV.getLong("discountRate"));
                pricePerGram = auShippingCostGV.getDouble("pricePerGram");
                if (UtilValidate.isNotEmpty(auShippingCostGV.getDouble("additionalCost"))) {
                    additionalShipCost = auShippingCostGV.getDouble("additionalCost");
                }

            } else if (currency.equals("EUR")) {
                currencyRate = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "EUR"), null, false)).getDouble("conversionFactor");
                feeType = "ebayFeeBreakEU";
                
                String eurCarrierId = "DEUTSCHEPOST";
                String eurShipMethType = "STANDARD";
                GenericValue shippingCostEurGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", eurCarrierId, "shippingMethodType", eurShipMethType), false);
                discountRate = Double.valueOf(shippingCostEurGV.getLong("discountRate"));
                pricePerGram = shippingCostEurGV.getBigDecimal("pricePerGram").doubleValue();
                if (UtilValidate.isNotEmpty(shippingCostEurGV.getDouble("additionalCost"))) {
                    additionalShipCost = shippingCostEurGV.getDouble("additionalCost");
                }
            } else if (currency.equals("CAD")) {
                currencyRate = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CAD"), null, false)).getDouble("conversionFactor");
                feeType = "ebayFeeBreakCA";
                
                GenericValue productTypeShippingMethod = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productTypeShippingMethod", "name", productType), null, false));
                String carrierId = productTypeShippingMethod.getString("value");
                List<GenericValue> costBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", carrierId), null, false);
                String costBreakShippingValue = "REGISTERED";

                
                GenericValue marketplaceProfitCA = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "CA"), null, false));
                GenericValue lowestPriceCA = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "CA"), null, false));
                String profitPercentCAStr = marketplaceProfitCA.getString("value");
                String caLowestProfitPercentage = lowestPriceCA.getString("priceBreakLo");
                String lowestPriceCAStr = lowestPriceCA.getString("value");
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((price / usdToCad * usdToRmb) >= costBreakLoBD.doubleValue()) && ((price / usdToCad * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                GenericValue caShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                discountRate = Double.valueOf(caShippingCostGV.getLong("discountRate"));
                pricePerGram = caShippingCostGV.getDouble("pricePerGram");
                if (UtilValidate.isNotEmpty(caShippingCostGV.getDouble("additionalCost"))) {
                    additionalShipCost = caShippingCostGV.getDouble("additionalCost");
                }

            }
            
            if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                shippingCost = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
            }   //if weight is more than 2000 - ship by EMS - END
            else {
                shippingCost = (weightDb * pricePerGram + additionalShipCost) * (1.0 - (discountRate/100.0));
            }
            
            double ebayFeePct = getFeePct(delegator, feeType, price);
            
            result = ((price / currencyRate * usdToRmb * (1.0 - (ebayFeePct / 100.0))) - shippingCost - productCost - packagingCost) / (price / currencyRate * usdToRmb);
            
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return 0.0;
        }
        return result;
    }   //samePriceProfitPercentageCalculation
    
    private static double getFeePct(Delegator delegator, String type, double price) {
        
        double feePct = 0;
        try {
            //get ebayFee -- START
            List<GenericValue> feeBreakList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", type), null, false);
            for (GenericValue feeBreak : feeBreakList) {    //loop feeBreakList -- START
                BigDecimal priceBreakLoBD = feeBreak.getBigDecimal("priceBreakLo");
                BigDecimal priceBreakHiBD = feeBreak.getBigDecimal("priceBreakHi");
                String feeBreakStr = feeBreak.getString("value");
                if ((price >= priceBreakLoBD.doubleValue()) && (price < priceBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                    feePct = Double.parseDouble(feeBreakStr);
                }   //if CP is lower than priceBreakLo -- END
            }   //loop feeBreakList -- END
            //get ebayFee -- END
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return 0.0;
        }
        
        return feePct;
    }
    
    public static Map<String, Object> rivalListingMonitorOld(DispatchContext dctx, Map context)
    throws GenericEntityException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        Debug.logError("start running rivalListingMonitor", module);
        
        try {   //main try -- START
            //get snapshotWeek -- START
            String snapshotWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + "";
            //get snapshotWeek -- END
            
            //get RivalListing -- START
            List<GenericValue> rivalListings = delegator.findList("RivalListing", null, null, UtilMisc.toList("sku"),null, false);
            //get RivalListing -- END
            
            //loop the SKU, order by SKU from RivalListing -- START
            for (GenericValue rivalListing : rivalListings) {   //loop rivalListings
                boolean rivalListingResultExist = false;
                String sku = rivalListing.getString("sku");
                String rivalItemId = rivalListing.getString("rivalItemId");
                Debug.logError("Processing sku " + sku + " and rivalItemId " + rivalItemId, module);
                
                //check RivalListingResult if RivalListing has been crawled -- START
                GenericValue checkRivalListingResult = delegator.findOne("RivalListingResult", UtilMisc.toMap("sku", sku, "rivalItemId", rivalItemId, "snapshotWeek", snapshotWeek), false);
                if (UtilValidate.isNotEmpty(checkRivalListingResult)) {
                    rivalListingResultExist = true;
                }
                //check RivalListingResult if RivalListing has been crawled -- END
                
                if (!rivalListingResultExist) { //run only when rivalListingResult is false -- START
                    //get the list of itemId based on SKU, order by original Price desc (gudaoActiveListings) -- START
                    List<GenericValue> gudaoActiveListings = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("sku", sku), null, false);
                    //get the list of itemId based on SKU, order by original Price desc (gudaoActiveListings) -- END
                    
                    double gudaoOriginalPriceFinal = 0.0;
                    double gudaoCurrentPriceFinal = 0.0;
                    double salesTotal = 0.0;
                    double listingLifetimeTotal = 0.0;
                    double gudaoHistorySold = 0.0;
                    double gudaoDailySales = 0.0;
                    double rivalShipping = 0.0;
                    double rivalCurrentPrice = 0.0;
                    double rivalListingLifetime = 0.0;
                    double rivalHistorySold = 0.0;
                    double rivalDailySales = 0.0;
                    double rivalCurrencyRate = 1.0;
                    String rivalListingStatus = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date nowDate = new Date();
                    
                    //loop the gudaoActiveListings -- START
                    if (UtilValidate.isNotEmpty(gudaoActiveListings)) { //if gudaoActiveListings is not empty -- START
                        for (GenericValue gudaoActiveListing : gudaoActiveListings) {   //loop gudaoActiveListings -- START
                            String gudaoItemId = gudaoActiveListing.getString("itemId");
                            double currencyRate = 1.0;
                            double gudaoCurrentPrice = 0.0;
                            BigDecimal shippingCostBD = BigDecimal.ZERO;
                            
                            //get currencyRate -- START
                            List<GenericValue> currencyRates = delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomIdTo", "USD", "uomId", gudaoActiveListing.getString("currency")), null, false);
                            if (UtilValidate.isNotEmpty(currencyRates)) {   //if currencyRates is not empty -- START
                                currencyRate = ((EntityUtil.getFirst(currencyRates)).getDouble("conversionFactor")).doubleValue();
                            }   //if currencyRates is not empty -- END
                            //get currencyRate -- END
                            
                            //get the international first shipping -- START
                            List<GenericValue> gudaoShippings = delegator.findByAnd("ActiveListingShipping", UtilMisc.toMap("itemId", gudaoItemId, "domestic", "N", "priority", "1"), null, false);
                            if (UtilValidate.isNotEmpty(gudaoShippings)) {  //if gudaoShippings is not empty -- START
                                GenericValue gudaoShipping = EntityUtil.getFirst(gudaoShippings);
                                shippingCostBD = gudaoShipping.getBigDecimal("shippingServiceCost");
                                //Debug.logError("shippingcost: " + shippingCostBD, module);
                            }   //if gudaoShippings is not empty -- END
                            //get the international first shipping -- END
                            
                            gudaoCurrentPrice = (gudaoActiveListing.getBigDecimal("sellStatCurrentPrice").doubleValue() + shippingCostBD.doubleValue()) * currencyRate;
                            if (gudaoActiveListing.getString("site").equals("US")) {    //if US site -- START
                                BigDecimal gudaoPromoOriginalPrice = gudaoActiveListing.getBigDecimal("promoOriginalPrice");
                                if (UtilValidate.isEmpty(gudaoPromoOriginalPrice)) {
                                    gudaoPromoOriginalPrice = gudaoActiveListing.getBigDecimal("sellStatCurrentPrice");
                                }
                                double newGudaoOriginalPrice = (gudaoPromoOriginalPrice.doubleValue() + shippingCostBD.doubleValue()) * currencyRate;
                                double newGudaoCurrentPrice = gudaoCurrentPrice;
                                if (gudaoCurrentPriceFinal == 0.0) {
                                    gudaoOriginalPriceFinal = (gudaoPromoOriginalPrice.doubleValue() + shippingCostBD.doubleValue()) * currencyRate;
                                    gudaoCurrentPriceFinal = gudaoCurrentPrice;
                                }
                                else if (newGudaoCurrentPrice <= gudaoCurrentPriceFinal) {
                                    gudaoOriginalPriceFinal = (gudaoPromoOriginalPrice.doubleValue() + shippingCostBD.doubleValue()) * currencyRate;
                                    gudaoCurrentPriceFinal = gudaoCurrentPrice;
                                }
                                
                            }   //if US site -- END
                            
                            //calculate salesTotal -- START
                            gudaoHistorySold += Double.valueOf(gudaoActiveListing.getLong("sellStatQuantitySold"));
                            salesTotal += (gudaoActiveListing.getLong("sellStatQuantitySold").doubleValue()) * gudaoCurrentPrice;
                            //Debug.logError("CurrencyRate: " + currencyRate + ", Gudao current Price: " + gudaoActiveListing.getBigDecimal("sellStatCurrentPrice") + ", shippingCost: " + shippingCostBD + ", gudaoCurrentPrice: " + gudaoCurrentPrice + ", quantitySold: " + gudaoActiveListing.getLong("sellStatQuantitySold") + ", salesTotal: " + salesTotal, module);
                            //calculate salesTotal -- END
                            
                            //calculate listingLifetime -- START
                            String listDetStartTime = gudaoActiveListing.getString("listDetStartTime");
                            String listDetStartDate = listDetStartTime.substring(0,10);
                            //Debug.logError("listDetStartDate: " + listDetStartDate, module);
                            Date listingStartDate = sdf.parse(listDetStartDate);
                            //Debug.logError("daydiff: " + (Math.abs(nowDate.getTime() - listingStartDate.getTime()) / (24 * 60 * 60 * 1000)), module);
                            listingLifetimeTotal += Double.valueOf((Math.abs(nowDate.getTime() - listingStartDate.getTime()) / (24 * 60 * 60 * 1000)));
                            //calculate listingLifetime -- END
                        }   //loop gudaoActiveListings -- END
                        //loop the gudaoActiveListings -- END
                        
                        //calculate gudaoDailySales -- START
                        gudaoDailySales = salesTotal / listingLifetimeTotal;
                        //Debug.logError("gudao salesTotal: " + salesTotal, module);
                        //Debug.logError("gudao listingLifetimeTotal: " + listingLifetimeTotal, module);
                        //calculate gudaoDailySales -- END
                        
                        //build getItem request for rivalItemId -- START
                        
                        
                        //build getItem request for rivalItemId -- END
                        GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", "industryland"), false);
                        Map mapAccount = FastMap.newInstance();
                        mapAccount = common.accountInfo(delegator, productStore);
                        
                        //remove the promotion on eBay -- START ====================
                        String callName = "GetItem";
                        mapAccount.put("callName", callName);
                        
                        //Building XML -- START
                        Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                        Element rootElem = rootDoc.getDocumentElement();
                        rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                        
                        //RequesterCredentials -- START
                        Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                        UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                        //RequesterCredentials -- END
                        
                        UtilXml.addChildElementValue(rootElem, "ItemID", rivalItemId, rootDoc);
                        UtilXml.addChildElementValue(rootElem, "IncludeItemSpecifics", "false", rootDoc);
                        UtilXml.addChildElementValue(rootElem, "DetailLevel", "ReturnAll", rootDoc);
                        
                        String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                        //Building XML -- END
                        
                        String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                        
                        Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                        Element elemResponse = docResponse.getDocumentElement();
                        String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                        
                        if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                            List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                            Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                            StringBuffer errorMessage = new StringBuffer();
                            while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                                Element errorElement = errorElementsElemIter.next();
                                String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                                String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                                errorMessage.append(shortMessage + " - " + longMessage);
                            }   //while errorElement -- END
                            if (errorMessage.toString().matches(".*listing has been deleted.*") || errorMessage.toString().matches(".*Item Not Found.*")) {   //if rival listing does not exist -- START
                                delegator.removeByAnd("RivalListing", UtilMisc.toMap("sku", sku, "rivalItemId", rivalItemId));
                                Debug.logError("Deleting SKU: " + sku + " and rivalItemID: " + rivalItemId, module);
                            }   //if rival listing does not exist -- END
                            Debug.logError("SKU: " + sku + ", rivalItemID: " + rivalItemId + ", errorMessage: " + errorMessage, module);
                        }   //if ack failure -- END
                        else {  //if ack Success -- START
                            List<? extends Element> itemElements = UtilXml.childElementList(elemResponse, "Item");
                            Iterator<? extends Element> itemsElemIter = itemElements.iterator();
                            while (itemsElemIter.hasNext()) {    //loop itemsElemIter -- START
                                Map itemMap = FastMap.newInstance();
                                Element itemElement = itemsElemIter.next();
                                String itemId = UtilXml.childElementValue(itemElement, "ItemID", null);
                                String rivalCurrency = UtilXml.childElementValue(itemElement, "Currency", null);
                                //Debug.logError("rivalCurrency: " + rivalCurrency, module);
                                
                                //get rivalCurrencyRate -- START
                                List<GenericValue> rivalCurrencyRates = delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomIdTo", "USD", "uomId", rivalCurrency), null, false);
                                if (UtilValidate.isNotEmpty(rivalCurrencyRates)) {   //if currencyRates is not empty -- START
                                    rivalCurrencyRate = ((EntityUtil.getFirst(rivalCurrencyRates)).getDouble("conversionFactor"));
                                }   //if currencyRates is not empty -- END
                                //get rivalCurrencyRate -- END
                                
                                //get ListingStatus -- START
                                List<? extends Element> sellingStatusList = UtilXml.childElementList(itemElement, "SellingStatus");
                                Iterator<? extends Element> sellingStatusListElemIter = sellingStatusList.iterator();
                                while (sellingStatusListElemIter.hasNext()) {   //loop sellingStatusListElemIter -- START
                                    Element sellingStatusElement = sellingStatusListElemIter.next();
                                    rivalListingStatus = UtilXml.childElementValue(sellingStatusElement, "ListingStatus", null);
                                    rivalCurrentPrice = Double.valueOf(UtilXml.childElementValue(sellingStatusElement, "CurrentPrice", null));
                                    //Debug.logError("rivalCurrentPrice: " + rivalCurrentPrice, module);
                                    rivalHistorySold = Double.valueOf(UtilXml.childElementValue(sellingStatusElement, "QuantitySold", null));
                                    //Debug.logError("rivalHistorySold: " + rivalHistorySold, module);
                                }   //loop sellingStatusListElemIter -- END
                                //get ListingStatus -- END
                                
                                //get Shipping -- START
                                List<? extends Element> shippingDetailList = UtilXml.childElementList(itemElement, "ShippingDetails");
                                Iterator<? extends Element> shippingDetailListElemIter = shippingDetailList.iterator();
                                while (shippingDetailListElemIter.hasNext()) {   //loop shippingDetailListElemIter -- START
                                    Element shippingDetailElement = shippingDetailListElemIter.next();
                                    List<? extends Element> internationalShippingList = UtilXml.childElementList(shippingDetailElement, "InternationalShippingServiceOption");
                                    Iterator<? extends Element> internationalShippingListElemIter = internationalShippingList.iterator();
                                    while (internationalShippingListElemIter.hasNext()) {   //loop internationalShippingListElemIter -- START
                                        Element internationalShippingElement = internationalShippingListElemIter.next();
                                        if (UtilXml.childElementValue(internationalShippingElement, "ShippingServicePriority", null).equals("1")) { //if priority is 1 -- START
                                            rivalShipping = Double.valueOf(UtilXml.childElementValue(internationalShippingElement, "ShippingServiceCost", null));
                                        }   //if priority is 1 -- END
                                    }   //loop internationalShippingListElemIter -- END
                                }   //loop shippingDetailListElemIter -- END
                                //get Shipping -- END
                                
                                //get rivalCurrentPrice -- START
                                rivalCurrentPrice += rivalShipping;
                                //get rivalCurrentPrice -- END
                                
                                //calculate rivalListingLifetime -- START
                                List<? extends Element> listingDetailList = UtilXml.childElementList(itemElement, "ListingDetails");
                                Iterator<? extends Element> listingDetailListElemIter = listingDetailList.iterator();
                                while (listingDetailListElemIter.hasNext()) {   //loop listingDetailListElemIter -- START
                                    Element listingDetailElement = listingDetailListElemIter.next();
                                    String rivalListDetStartTime = UtilXml.childElementValue(listingDetailElement, "StartTime", null).substring(0,10);
                                    Date rivalListingStartDate = sdf.parse(rivalListDetStartTime);
                                    rivalListingLifetime = Double.valueOf((Math.abs(nowDate.getTime() - rivalListingStartDate.getTime()) / (24 * 60 * 60 * 1000)));
                                    //Debug.logError("rivalListDetStartTime: " + rivalListDetStartTime + ", rivalListingLifetime: " + rivalListingLifetime, module);
                                }   //loop listingDetailListElemIter -- END
                                //calculate rivalListingLifetime -- END
                                
                                //calculate rivalDailySales -- START
                                rivalDailySales = (rivalHistorySold * rivalCurrentPrice * rivalCurrencyRate) / rivalListingLifetime;
                                //calculate rivalDailySales -- END
                                
                                //update result into RivalListingResult -- START
                                GenericValue rivalListingResult = delegator.makeValue("RivalListingResult", UtilMisc.toMap("sku", sku, "rivalItemId", rivalItemId, "snapshotWeek", snapshotWeek));
                                rivalListingResult.set("rivalHistorySold", rivalHistorySold);
                                rivalListingResult.set("gudaoHistorySold", gudaoHistorySold);
                                rivalListingResult.set("rivalDailySales", rivalDailySales);
                                rivalListingResult.set("gudaoDailySales", gudaoDailySales);
                                rivalListingResult.set("rivalCurrentPrice", rivalCurrentPrice);
                                rivalListingResult.set("gudaoOriginalPrice", gudaoOriginalPriceFinal);
                                rivalListingResult.set("gudaoCurrentPrice", gudaoCurrentPriceFinal);
                                rivalListingResult.set("rivalListingStatus", rivalListingStatus);
                                delegator.createOrStore(rivalListingResult);
                                //update result into RivalListingResult -- END
                                
                            }   //loop itemsElemIter -- END
                        }   //if ack Success -- END
                    }   //if gudaoActiveListings is not empty -- END
                    else {  //if gudaoActiveListings is empty -- START
                        Debug.logError("No active Listing found for sku " + sku + " and rivalItemId " + rivalItemId, module);
                    }   //if gudaoActiveListings is empty -- END
                }   //run only when rivalListingResult is false -- END
                else {  //run only when rivalListingResult is true -- START
                    Debug.logError("RivalListingResult found for sku " + sku + " and rivalItemId " + rivalItemId, module);
                }   //run only when rivalListingResult is true -- END
            }   //loop rivalListings
            //loop the SKU, order by SKU from RivalListing -- END
            
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError("finished running rivalListingMonitor", module);
        return result;
        
    }   //rivalListingMonitor
    
    public static Map<String, Object> getListingSoldUnsoldXDays(DispatchContext dctx, Map context)
	throws GenericEntityException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String unsoldDay = (String) context.get("unsoldDay");
        Map result = ServiceUtil.returnSuccess();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        
        try {   //main try -- START
            Calendar fromDay = Calendar.getInstance();
            fromDay.set(Calendar.DATE, fromDay.get(Calendar.DATE) - Integer.parseInt(unsoldDay));
            Timestamp fromDate = Timestamp.valueOf(sdf.format(fromDay.getTime()));
            String fromDateEbayTime = bellyannaService.timestampToEbayDate(fromDate);
            
            Calendar listDay = Calendar.getInstance();
            listDay.set(Calendar.DATE, listDay.get(Calendar.DATE) - 1);
            Timestamp listDate = Timestamp.valueOf(sdf.format(listDay.getTime()));
            String listDateEbayTime = bellyannaService.timestampToEbayDate(listDate);
            
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("createdDate", EntityOperator.GREATER_THAN, fromDateEbayTime),
                                                                                      EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId)
                                                                                      ));
            List<GenericValue> ebayOrderItemList = delegator.findList("EbayOrderItem", condition, null, null, null, false);
            List<String> soldItemIdList = new LinkedList<String>();
            for (GenericValue ebayOrderItem : ebayOrderItemList) {  //loop ebayOrderItemList -- START
                soldItemIdList.add(ebayOrderItem.getString("itemId"));
            }   //loop ebayOrderItemList -- END
            HashSet<String> uniqueSoldItemIdListHS = new HashSet<String>(soldItemIdList);
            List<String> uniqueSoldItemIdList = new ArrayList<String>(uniqueSoldItemIdListHS);
            EntityCondition activeListingCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                   EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                                                                   EntityCondition.makeCondition("listDetStartTime", EntityOperator.LESS_THAN, listDateEbayTime),
                                                                                                   EntityCondition.makeCondition("listingType", EntityOperator.EQUALS, "FixedPriceItem")
                                                                                                   ));
            List<GenericValue> activeListingList = delegator.findList("EbayActiveListing", activeListingCondition, UtilMisc.toSet("itemId"), null, null, false);
            List<String> unsoldItemIdList1 = new LinkedList<String>();
            for (GenericValue activeListing : activeListingList) {  //loop activeListingList -- START
                if (!uniqueSoldItemIdListHS.contains(activeListing.getString("itemId"))) {
                    unsoldItemIdList1.add(activeListing.getString("itemId"));
                }
            }   //loop activeListingList -- END
            HashSet<String> uniqueUnsoldSoldItemIdListHS = new HashSet<String>(unsoldItemIdList1);
            List<String> unsoldItemIdList = new ArrayList<String>(uniqueUnsoldSoldItemIdListHS);
            //Debug.logError("unsoldItemIdList size is " + unsoldItemIdList.size(), module);
            //Debug.logError("soldItemIdList size is " + uniqueSoldItemIdList.size(), module);
            result.put("unsoldItemIdList", unsoldItemIdList);
            result.put("soldItemIdList", uniqueSoldItemIdList);
        }   //main try -- END
        catch (GenericEntityException e) {
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
    }   //getListingUnsoldXDays
    
    public static Map<String, Object> autoMarkdownSingleBACKUP(DispatchContext dctx, Map context)
    throws GenericEntityException   {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        Map result = ServiceUtil.returnSuccess();
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String today = sdfNow.format(now.getTime());
        
        
        try {   //main try -- START
            GenericValue productStoreEbaySetting = delegator.findOne("ProductStoreEbaySetting", UtilMisc.toMap("productStoreId", productStoreId), false);
            String discountDuration = productStoreEbaySetting.getString("discountDuration");
            String discountEndTime = productStoreEbaySetting.getString("discountEndTime");
            String promotionalSaleType = productStoreEbaySetting.getString("promotionalSaleType");
            String discountType = productStoreEbaySetting.getString("discountType");
            
            List<GenericValue> waitingForMarkdownList = delegator.findList("WaitingForMarkdown", null, null, null, null, false);
            if (UtilValidate.isNotEmpty(waitingForMarkdownList)) {  //if waitingForMarkdownList is not empty -- START
                List<String> currentDiscountList = new LinkedList<String>();
                for (GenericValue uniqueDiscount : waitingForMarkdownList) {    //loop waitingForMarkdownList to get unique currentDiscount -- START
                    currentDiscountList.add(uniqueDiscount.getDouble("currentDiscount").toString());
                }   //loop waitingForMarkdownList to get unique currentDiscount -- END
                HashSet<String> uniqueCurrentDiscount = new HashSet<String>(currentDiscountList);
                for (String currentDiscount : uniqueCurrentDiscount) {  //loop uniqueCurrentDiscount -- START
                    //Get EbayPromotionalSale -- START
                    List<GenericValue> ebayPromotionalSaleList = delegator.findByAnd("EbayPromotionalSale", UtilMisc.toMap("productStoreId", productStoreId, "discountValue", currentDiscount, "promotionalSaleName", "Auto markdown " + currentDiscount + "% off - " + discountDuration + " day(s)"), null, false);
                    if (UtilValidate.isNotEmpty(ebayPromotionalSaleList)) { //if ebayPromotionalSale is not empty -- START
                        GenericValue ebayPromotionalSale = EntityUtil.getFirst(ebayPromotionalSaleList);
                        List<GenericValue> waitingForMarkdownDiscountList = delegator.findByAnd("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId, "currentDiscount", Double.parseDouble(currentDiscount)), null, false);
                        String promotionalSaleId = ebayPromotionalSale.getString("promotionalSaleId");
                        
                        GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                        Map mapAccount = FastMap.newInstance();
                        mapAccount = common.accountInfo(delegator, productStore);
                        
                        //remove the promotion on eBay -- START ====================
                        String callName = "SetPromotionalSale";
                        mapAccount.put("callName", callName);
                        
                        //Building XML -- START
                        Document rootDocDelete = UtilXml.makeEmptyXmlDocument(callName + "Request");
                        Element rootElemDelete = rootDocDelete.getDocumentElement();
                        rootElemDelete.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                        
                        //RequesterCredentials -- START
                        Element requesterCredentialsElemDelete = UtilXml.addChildElement(rootElemDelete, "RequesterCredentials", rootDocDelete);
                        UtilXml.addChildElementValue(requesterCredentialsElemDelete, "eBayAuthToken", mapAccount.get("token").toString(), rootDocDelete);
                        //RequesterCredentials -- END
                        
                        UtilXml.addChildElementValue(rootElemDelete, "Action", "Delete", rootDocDelete);
                        Element promotionalSaleDetailsElemDelete = UtilXml.addChildElement(rootElemDelete, "PromotionalSaleDetails", rootDocDelete);
                        UtilXml.addChildElementValue(promotionalSaleDetailsElemDelete, "PromotionalSaleID", promotionalSaleId, rootDocDelete);
                        
                        String requestXMLcodeDelete = UtilXml.writeXmlDocument(rootElemDelete);
                        String responseXMLDelete = sendRequestXMLToEbay(mapAccount, requestXMLcodeDelete);
                        //Debug.logError(responseXML, module);
                        Document docResponseDelete = UtilXml.readXmlDocument(responseXMLDelete, true);
                        Element elemResponseDelete = docResponseDelete.getDocumentElement();
                        String ackDelete = UtilXml.childElementValue(elemResponseDelete, "Ack", "Failure");
                        
                        if (!ackDelete.equals("Success") && !ackDelete.equals("Warning")) {   //if ack failure -- START
                            List<? extends Element> errorElements = UtilXml.childElementList(elemResponseDelete, "Errors");
                            Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                            StringBuffer errorMessage = new StringBuffer();
                            while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                                Element errorElement = errorElementsElemIter.next();
                                String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                                String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                                errorMessage.append(shortMessage + " - " + longMessage);
                            }   //while errorElement -- END
                            Debug.logError(productStoreId + ": Failed deleting Promotional sale for " + currentDiscount + "off and errorMessage: " + errorMessage, module);
                        }   //if ack failure -- END
                        else {
                            delegator.removeByAnd("EbayPromotionalSale", UtilMisc.toMap("promotionalSaleId", promotionalSaleId, "productStoreId", productStoreId));
                            Debug.logError(productStoreId + ": Successfully delete Promotional sale for " + currentDiscount + "off", module);
                        }
                        //remove the promotion on eBay -- END ====================
                        
                        String promotionalSaleName = "Auto markdown " + currentDiscount + "% off - " + discountDuration + " day(s)";
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
                        SimpleDateFormat sdfEbay = new SimpleDateFormat("yyyy-MM-dd'T'" + discountEndTime + ":00:00'Z'");
                        SimpleDateFormat sdfEbayStart = new SimpleDateFormat("yyyy-MM-dd'T'" + discountEndTime + ":15:00'Z'");
                        Calendar endDay = Calendar.getInstance();
                        endDay.set(Calendar.DATE, endDay.get(Calendar.DATE) + Integer.parseInt(discountDuration));
                        Timestamp endDate = Timestamp.valueOf(sdf.format(endDay.getTime()));
                        
                        Calendar startDay = Calendar.getInstance();
                        Timestamp startDate = Timestamp.valueOf(sdf.format(startDay.getTime()));
                        String discountStartDate = sdfEbayStart.format(startDate.getTime());
                        String discountEndDate = sdfEbay.format(endDate.getTime());
                        //Debug.logError("discountEndDate : " + discountEndDate, module);
                        //Debug.logError("discountStartDate : " + discountStartDate, module);
                        /*String endHour = promotionalSaleEndTime.substring(11,13);
                         String endMinute = promotionalSaleEndTime.substring(14,16);
                         if (Integer.parseInt(endMinute) >= 55) {
                         endHour = (Integer.parseInt(endHour) + 1) + "";
                         endMinute = "00";
                         } else {
                         endMinute = Integer.parseInt(endMinute) + 5 + "";
                         if (endMinute.length() == 1) {
                         endMinute = "0" + endMinute;
                         }
                         }*/
                        
                        //create the promotion on eBay -- START ++++++++++++++++++++++++
                        callName = "SetPromotionalSale";
                        mapAccount.put("callName", callName);
                        //Building XML -- START
                        Document rootDocUpdate = UtilXml.makeEmptyXmlDocument(callName + "Request");
                        Element rootElemUpdate = rootDocUpdate.getDocumentElement();
                        rootElemUpdate.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                        
                        //RequesterCredentials -- START
                        Element requesterCredentialsElemUpdate = UtilXml.addChildElement(rootElemUpdate, "RequesterCredentials", rootDocUpdate);
                        UtilXml.addChildElementValue(requesterCredentialsElemUpdate, "eBayAuthToken", mapAccount.get("token").toString(), rootDocUpdate);
                        //RequesterCredentials -- END
                        
                        UtilXml.addChildElementValue(rootElemUpdate, "Action", "Add", rootDocUpdate);
                        Element promotionalSaleDetailsElemUpdate = UtilXml.addChildElement(rootElemUpdate, "PromotionalSaleDetails", rootDocUpdate);
                        
                        UtilXml.addChildElementValue(promotionalSaleDetailsElemUpdate, "DiscountType", discountType, rootDocUpdate);
                        UtilXml.addChildElementValue(promotionalSaleDetailsElemUpdate, "DiscountValue", currentDiscount, rootDocUpdate);
                        UtilXml.addChildElementValue(promotionalSaleDetailsElemUpdate, "PromotionalSaleName", promotionalSaleName, rootDocUpdate);
                        UtilXml.addChildElementValue(promotionalSaleDetailsElemUpdate, "PromotionalSaleType", promotionalSaleType, rootDocUpdate);
                        UtilXml.addChildElementValue(promotionalSaleDetailsElemUpdate, "PromotionalSaleStartTime", discountStartDate, rootDocUpdate);
                        UtilXml.addChildElementValue(promotionalSaleDetailsElemUpdate, "PromotionalSaleEndTime", discountEndDate, rootDocUpdate);
                        
                        String requestXMLcodeUpdate = UtilXml.writeXmlDocument(rootDocUpdate);
                        //Debug.logError(requestXMLcodeUpdate, module);
                        
                        String responseXMLUpdate = sendRequestXMLToEbay(mapAccount, requestXMLcodeUpdate);
                        //create the promotion on eBay -- END ++++++++++++++++++++++++++
                        
                        Document docResponseUpdate = UtilXml.readXmlDocument(responseXMLUpdate, true);
                        Element elemResponseUpdate = docResponseUpdate.getDocumentElement();
                        String ackUpdate = UtilXml.childElementValue(elemResponseUpdate, "Ack", "Failure");
                        
                        if (!ackUpdate.equals("Success") && !ackUpdate.equals("Warning")) {   //if ack failure -- START
                            List<? extends Element> errorElements = UtilXml.childElementList(elemResponseUpdate, "Errors");
                            Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                            StringBuffer errorMessage = new StringBuffer();
                            while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                                Element errorElement = errorElementsElemIter.next();
                                String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                                String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                                errorMessage.append(shortMessage + " - " + longMessage);
                            }   //while errorElement -- END
                            Debug.logError(productStoreId + ": Failed creating ebayPromotionalSale for " + promotionalSaleName + ", errorMessage: " + errorMessage, module);
                            delegator.removeByAnd("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId));
                        }   //if ack failure -- END
                        
                        
                        else {  //if update ebayPromotionalSale success - Start apply the listings -- START
                            String newPromotionalSaleId = UtilXml.childElementValue(elemResponseUpdate, "PromotionalSaleID", null);
                            
                            //update ofbiz EbayPromotionalSale -- START
                            GenericValue newEbayPromotionalSale = delegator.makeValue("EbayPromotionalSale", UtilMisc.toMap("productStoreId", productStoreId, "promotionalSaleId", newPromotionalSaleId));
                            newEbayPromotionalSale.set("promotionalSaleName", promotionalSaleName);
                            newEbayPromotionalSale.set("promotionalSaleType", promotionalSaleType);
                            newEbayPromotionalSale.set("promotionalSaleStartTime", discountStartDate);
                            newEbayPromotionalSale.set("promotionalSaleEndTime", discountEndDate);
                            newEbayPromotionalSale.set("discountType", discountType);
                            newEbayPromotionalSale.set("discountValue", currentDiscount);
                            delegator.createOrStore(newEbayPromotionalSale);
                            //update ofbiz EbayPromotionalSale -- END
                            
                            //setPromotionalSaleListings-- START
                            callName = "SetPromotionalSaleListings";
                            mapAccount.put("callName", callName);
                            
                            //Building XML -- START
                            Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                            Element rootElem = rootDoc.getDocumentElement();
                            rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                            
                            //RequesterCredentials -- START
                            Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                            UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                            //RequesterCredentials -- END
                            
                            UtilXml.addChildElementValue(rootElem, "Action", "Add", rootDoc);
                            UtilXml.addChildElementValue(rootElem, "PromotionalSaleID", newPromotionalSaleId, rootDoc);
                            
                            Element promotionalSaleItemIDArray = UtilXml.addChildElement(rootElem, "PromotionalSaleItemIDArray", rootDoc);
                            for (GenericValue waitingForMarkdownDiscount: waitingForMarkdownDiscountList) { //loop waitingForMarkdownDiscountList -- START
                                UtilXml.addChildElementValue(promotionalSaleItemIDArray, "ItemID", waitingForMarkdownDiscount.getString("itemId"), rootDoc);
                            }   //loop waitingForMarkdownDiscountList -- END
                            
                            //Building XML -- END
                            
                            String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                            /*FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/requestXML/markdown-" + productStoreId + "-" + newPromotionalSaleId + "-" + today + ".xml", true);
                             f1.write(requestXMLcode.toString());
                             f1.close();*/
                            //Debug.logError(requestXMLcode, module);
                            
                            String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                            //Debug.logError(responseXML, module);
                            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                            Element elemResponse = docResponse.getDocumentElement();
                            String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                            
                            if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                                List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                                Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                                StringBuffer errorMessage = new StringBuffer();
                                while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                                    Element errorElement = errorElementsElemIter.next();
                                    String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                                    String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                                    errorMessage.append(shortMessage + " - " + longMessage);
                                }   //while errorElement -- END
                                Debug.logError(productStoreId + ": Failed setPromotionalSalesListing for ID " +  promotionalSaleId + " (" + currentDiscount + "% off), errorMessage: " + errorMessage, module);
                                delegator.removeByAnd("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId));
                            } else {    //if apply markdown to listing successful -- START
                                delegator.removeByAnd("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId));
                            }   //if apply markdown to listing successful -- END
                            
                            
                            //setPromotionalSaleListings-- END
                        }   //if update ebayPromotionalSale success - Start apply the listings -- END
                    }   //if ebayPromotionalSale is not empty -- END
                    else {  //if ebayPromotionalSale is empty -- START
                        Debug.logError(productStoreId + ": does not have promotional sale on eBay with " + currentDiscount + "% off", module);
                    }   //if ebayPromotionalSale is empty -- END
                    //Get EbayPromotionalSale -- END
                }   //loop uniqueCurrentDiscount -- END
                if (true) {
                    delegator.removeByAnd("WaitingForMarkdown", UtilMisc.toMap("productStoreId", productStoreId));
                }
            }   //if waitingForMarkdownList is not empty -- END
            else {
                Debug.logError("WaitingForMarkdownList is empty", module);
            }
            
        }  //main try -- END
        catch (GenericEntityException e) {
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
    }   //autoMarkdownSingle
    
    public static Map<String, Object> autoScheduleJob (DispatchContext dctx, Map context) {   //autoScheduleJob
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String serviceName = (String) context.get("serviceName");
        Map result = ServiceUtil.returnSuccess();
        //String text = (String) context.get("Text");
        try {   //start try block
            
            //Debug.logError("Scheduled Jobs run successfully", module);
            //long startTime = (new Date()).getTime();
            long startTime = (new Date(System.currentTimeMillis()+5*1000)).getTime();
            //Debug.logError(new SimpleDateFormat("yyyy-MM-dd HH:mm:00").format(startTime) + "", module);
            dispatcher.runSync("autoScheduleJobDetail", UtilMisc.toMap("serviceName", serviceName, "startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime) + "", "userLogin", userLogin) );
        }   //end try block
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return ServiceUtil.returnSuccess();
    }   //autoScheduleJob
    
    public static Map<String, Object> autoScheduleJobDetail (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException { //autoScheduleJobDetail
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();

        TimeZone timeZone = UtilDateTime.toTimeZone(null);
        Locale locale = Locale.US;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        String serviceName = (String) context.get("serviceName");
        String serviceTime = (String) context.get("startTime");
        
        try {   //first Try -- START
            
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("serviceName",EntityOperator.EQUALS ,serviceName)
                                                                                      ));
            
            List<GenericValue> autoScheduleJobList = delegator.findList("AutoScheduleJob", condition, null, UtilMisc.toList("priority"),null, false);
            for (GenericValue autoScheduleJob : autoScheduleJobList) {  //loop autoScheduleJobList -- START
                boolean runService = false;
                String productStoreId = autoScheduleJob.getString("productStoreId");
                String autoScheduleJobId = autoScheduleJob.getString("autoScheduleJobId");
                GenericValue autoScheduleJobHistory = delegator.findOne("AutoScheduleJobHistory", UtilMisc.toMap("productStoreId", productStoreId, "serviceName", serviceName), false);
                if (UtilValidate.isEmpty(autoScheduleJobHistory)) { //if autoScheduleJobHistory is null -- START
                    autoScheduleJobHistory = delegator.makeValue("AutoScheduleJobHistory", UtilMisc.toMap("productStoreId", productStoreId, "serviceName", serviceName));
                    runService = true;
                }   //if autoScheduleJobHistory is null -- END
                else {  //if autoScheduleJobHistory is NOT null -- START
                    //java.sql.Timestamp nowTimestamp = UtilDateTime.nowTimestamp();
                    java.sql.Timestamp lastRuntime = autoScheduleJobHistory.getTimestamp("lastRuntime");
                    long checkTime = (new Date(System.currentTimeMillis()-1320*60*1000)).getTime();
                    
                    Timestamp checkTimeTS = Timestamp.valueOf(sdf.format(checkTime));
                    if (lastRuntime.compareTo(checkTimeTS) < 0) {   //check lastRuntime -- START
                        runService = true;
                    }   //check lastRuntime -- END
                }   //if autoScheduleJobHistory is NOT null -- END
                
                if (runService) {   //if runService is TRUE -- START
                    String jobName = "Auto-" + serviceName + "-" + productStoreId;
                    String poolName = "pool";
                    String serviceEndTime = null;
                    String serviceFreq = null;
                    String serviceIntr = null;
                    String serviceCnt = null;
                    String retryCnt = null;
                    
                    // the frequency map
                    Map<String, Integer> freqMap = FastMap.newInstance();
                    
                    freqMap.put("SECONDLY", Integer.valueOf(1));
                    freqMap.put("MINUTELY", Integer.valueOf(2));
                    freqMap.put("HOURLY", Integer.valueOf(3));
                    freqMap.put("DAILY", Integer.valueOf(4));
                    freqMap.put("WEEKLY", Integer.valueOf(5));
                    freqMap.put("MONTHLY", Integer.valueOf(6));
                    freqMap.put("YEARLY", Integer.valueOf(7));
                    
                    long startTime = (new Date()).getTime();
                    long endTime = 0;
                    int maxRetry = -1;
                    int count = 1;
                    int interval = 1;
                    int frequency = RecurrenceRule.DAILY;
                    
                    StringBuilder errorBuf = new StringBuilder();
                    
                    ModelService modelService = null;
                    try {
                        modelService = dispatcher.getDispatchContext().getModelService(serviceName);
                    } catch (GenericServiceException e) {
                        Debug.logError(e, "Error looking up ModelService for serviceName [" + serviceName + "]", module);
                        String errMsg = UtilProperties.getMessage("WebappUiLabels", "coreEvents.error_modelservice_for_srv_name", locale);
                        Debug.logError(errMsg, module);
                        return ServiceUtil.returnError(errMsg);
                    }
                    if (modelService == null) {
                        String errMsg = UtilProperties.getMessage("WebappUiLabels", "coreEvents.service_name_not_find", locale);
                        Debug.logError(errMsg, module);
                        return ServiceUtil.returnError(errMsg);
                    }
                    
                    // make the context valid; using the makeValid method from ModelService
                    Map<String, Object> serviceContext = FastMap.newInstance();
                    
                    List<GenericValue> autoScheduleJobDetailList = delegator.findByAnd("AutoScheduleJobDetail", UtilMisc.toMap("autoScheduleJobId", autoScheduleJobId), null, false);
                    for (GenericValue autoScheduleJobDetail : autoScheduleJobDetailList) {  //loop autoScheduleJobDetailList -- START
                        String gvName = autoScheduleJobDetail.getString("attrName");
                        String gvValue = autoScheduleJobDetail.getString("attrValue");
                        
                        //other value -- START
                        if (gvName.equals("serviceEndTime")) {
                            serviceEndTime = gvValue;
                        } else if (gvName.equals("serviceFreq")) {
                            serviceFreq = gvValue;
                        } else if (gvName.equals("serviceIntr")) {
                            serviceIntr = gvValue;
                        } else if (gvName.equals("serviceCnt")) {
                            serviceCnt = gvValue;
                        } else if (gvName.equals("retryCnt")) {
                            retryCnt = gvValue;
                        }
                        //other value -- END
                        
                    }   //loop autoScheduleJobDetailList -- END
                    if (serviceEndTime == null) {
                        serviceEndTime = "";
                    }
                    if (serviceFreq == null) {
                        serviceFreq = "";
                    }
                    if (serviceIntr == null) {
                        serviceIntr = "1";
                    }
                    if (serviceCnt == null) {
                        serviceCnt = "1";
                    }
                    if (retryCnt == null) {
                        retryCnt = "0";
                    }
                    
                    Iterator<String> ci = modelService.getInParamNames().iterator();
                    while (ci.hasNext()) {  //loop ci to populate service IN Parameters -- START
                        String name = ci.next();
                        // don't include userLogin, that's taken care of below
                        if ("userLogin".equals(name)) continue;
                        // don't include locale, that is also taken care of below
                        if ("locale".equals(name)) continue;
                        Object value = null;
                        for (GenericValue autoScheduleJobDetail : autoScheduleJobDetailList) {  //loop autoScheduleJobDetailList -- START
                            String gvName = autoScheduleJobDetail.getString("attrName");
                            String gvValue = autoScheduleJobDetail.getString("attrValue");
                            //Debug.logError("AutoScheduleJobDetail: " + gvName + " = " + gvValue, module);
                            if (name.equals(gvName)) {  //populate value
                                value = gvValue;
                            }   //populate value
                        }   //loop autoScheduleJobDetailList -- END
                        
                        if (value instanceof String && ((String) value).length() == 0) {
                            // interpreting empty fields as null values for each in back end handling...
                            value = null;
                        }
                        
                        serviceContext.put(name, value);
                    }   //loop ci to populate service IN Parameters -- END

                    serviceContext = modelService.makeValid(serviceContext, ModelService.IN_PARAM, true, null, timeZone, locale);
                    
                    if (userLogin != null) {
                        serviceContext.put("userLogin", userLogin);
                    }
                    
                    if (locale != null) {
                        serviceContext.put("locale", locale);
                    }
                    
                    // some conversions
                    if (UtilValidate.isNotEmpty(serviceTime)) {
                        try {
                            Timestamp ts1 = Timestamp.valueOf(serviceTime);
                            startTime = ts1.getTime();
                        } catch (IllegalArgumentException e) {
                            try {
                                startTime = Long.parseLong(serviceTime);
                            } catch (NumberFormatException nfe) {
                                String errMsg = UtilProperties.getMessage("WebappUiLabels", "coreEvents.invalid_format_time", locale);
                                errorBuf.append(errMsg);
                            }
                        }
                        if (startTime < (new Date()).getTime()) {
                            String errMsg = UtilProperties.getMessage("WebappUiLabels", "coreEvents.service_time_already_passed", locale);
                            errorBuf.append(errMsg);
                        }
                    }
                    if (UtilValidate.isNotEmpty(serviceEndTime)) {
                        try {
                            Timestamp ts1 = Timestamp.valueOf(serviceEndTime);
                            endTime = ts1.getTime();
                        } catch (IllegalArgumentException e) {
                            try {
                                endTime = Long.parseLong(serviceTime);
                            } catch (NumberFormatException nfe) {
                                String errMsg = UtilProperties.getMessage("WebappUiLabels", "coreEvents.invalid_format_time", locale);
                                errorBuf.append(errMsg);
                            }
                        }
                        if (endTime < (new Date()).getTime()) {
                            String errMsg = UtilProperties.getMessage("WebappUiLabels", "coreEvents.service_time_already_passed", locale);
                            errorBuf.append(errMsg);
                        }
                    }
                    if (UtilValidate.isNotEmpty(serviceIntr)) {
                        try {
                            interval = Integer.parseInt(serviceIntr);
                        } catch (NumberFormatException nfe) {
                            String errMsg = UtilProperties.getMessage("WebappUiLabels", "coreEvents.invalid_format_interval", locale);
                            errorBuf.append(errMsg);
                        }
                    }
                    if (UtilValidate.isNotEmpty(serviceCnt)) {
                        try {
                            count = Integer.parseInt(serviceCnt);
                        } catch (NumberFormatException nfe) {
                            String errMsg = UtilProperties.getMessage("WebappUiLabels", "coreEvents.invalid_format_count", locale);
                            errorBuf.append(errMsg);
                        }
                    }
                    if (UtilValidate.isNotEmpty(serviceFreq)) {
                        int parsedValue = 0;
                        
                        try {
                            parsedValue = Integer.parseInt(serviceFreq);
                            if (parsedValue > 0 && parsedValue < 8)
                                frequency = parsedValue;
                        } catch (NumberFormatException nfe) {
                            parsedValue = 0;
                        }
                        if (parsedValue == 0) {
                            if (!freqMap.containsKey(serviceFreq.toUpperCase())) {
                                String errMsg = UtilProperties.getMessage("WebappUiLabels", "coreEvents.invalid_format_frequency", locale);
                                errorBuf.append(errMsg);
                            } else {
                                frequency = freqMap.get(serviceFreq.toUpperCase()).intValue();
                            }
                        }
                    }
                    if (UtilValidate.isNotEmpty(retryCnt)) {
                        int parsedValue = -2;
                        
                        try {
                            parsedValue = Integer.parseInt(retryCnt);
                        } catch (NumberFormatException e) {
                            parsedValue = -2;
                        }
                        if (parsedValue > -2) {
                            maxRetry = parsedValue;
                        } else {
                            maxRetry = modelService.maxRetry;
                        }
                    } else {
                        maxRetry = modelService.maxRetry;
                    }
                    
                    // return the errors
                    if (errorBuf.length() > 0) {
                        Debug.logError(errorBuf.toString(), module);
                        return ServiceUtil.returnError(errorBuf.toString());
                    }
                    
                    dispatcher.schedule(jobName, poolName, serviceName, serviceContext, startTime, frequency, interval, count, endTime, maxRetry);
                    autoScheduleJobHistory.set("lastRuntime", Timestamp.valueOf(sdf.format(startTime)));
                    delegator.createOrStore(autoScheduleJobHistory);
                    break;
                }   //if runService is TRUE -- END
            }   //loop autoScheduleJobList -- END
        }   //first Try -- End
        catch (GenericEntityException e) {
            //Debug.logError(e, "Error looking up ModelService for serviceName [" + serviceName + "]", module);
            //String errMsg = UtilProperties.getMessage("WebappUiLabels", "coreEvents.error_modelservice_for_srv_name", locale);
            //Debug.logError(errMsg, module);
            return ServiceUtil.returnError("error");
        }
        
        return result;
    }   //autoScheduleJobDetail
    
    public static Map<String, Object> changeEbayPaypal (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {   //changeEbayPaypal
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        int count = 0;
        
        try {   //main try -- START
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "PENDING")
                                                                                      ));
            List<GenericValue> distinctEbayChangePaypalList = delegator.findList("EbayChangePaypal", condition,
                                                                                UtilMisc.toSet("itemId"),
                                                                                UtilMisc.toList("itemId"),
                                                                                new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
            
            for (GenericValue distinctEbayChangePaypal : distinctEbayChangePaypalList) {  //loop distinctEbayChangePrice -- START
                String itemId = distinctEbayChangePaypal.getString("itemId");
                Debug.logError("processing itemID " + itemId, module);
                List<GenericValue> ebayChangePaypal = delegator.findByAnd("EbayChangePaypal", UtilMisc.toMap("itemId", itemId), null, false);
                GenericValue ebayChangePaypalFirst = EntityUtil.getFirst(ebayChangePaypal);
                List<GenericValue> activeListing = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false);
                if (UtilValidate.isNotEmpty(activeListing)) {   //if activeListing is not empty -- START
                    //Debug.logError("ebayActiveListing for itemId " + itemId + " is not empty, continue to process", module);
                    GenericValue activeListingProductStore = EntityUtil.getFirst(activeListing);
                    String productStoreId = activeListingProductStore.getString("productStoreId");
                    String listingType = activeListingProductStore.getString("listingType");
                    String site = activeListingProductStore.getString("site");
                    String paypalEmailAddress = ebayChangePaypalFirst.getString("paypalEmailAddress");
                    String callName  = "ReviseFixedPriceItem";
                    if (callName.toUpperCase().equals("CHINESE")) {
                        callName = "ReviseItem";
                    }
                    
                    
                    boolean sendRequest = true;
                    //Debug.logError("productStoreId is  " + productStoreId + " and site is " + site, module);
                    Map mapAccount = FastMap.newInstance();
                    GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                    mapAccount = common.accountInfo(delegator, productStore);
                    mapAccount.put("callName", callName);
                    GenericValue ebaySiteCode = EntityUtil.getFirst(delegator.findByAnd("EbaySiteCode", UtilMisc.toMap("ebaySite", site), null, false));
                    mapAccount.put("siteId", ebaySiteCode.getString("ebaySiteId"));
                    mapAccount.put("globalId", ebaySiteCode.getString("ebayGlobalId"));
                    
                    //Debug.logError("start building requestXML", module);
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    //Item -- START
                    Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ItemID", itemId, rootDoc);
                    UtilXml.addChildElementValue(itemElem, "PayPalEmailAddress", paypalEmailAddress, rootDoc);
             
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    //Debug.logError(requestXMLcode, module);
                    //Debug.logError("send Request is " + sendRequest, module);
                    //Debug.logError("start sending requestXML to eBay API", module);
                    if (sendRequest) {  //if sendRequest is true -- START
                        String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                        //Debug.logError(responseXML, module);
                        Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                        Element elemResponse = docResponse.getDocumentElement();
                        String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                        
                        if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                            List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                            Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                            StringBuffer errorMessage = new StringBuffer();
                            while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                                Element errorElement = errorElementsElemIter.next();
                                String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                                String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                                errorMessage.append(shortMessage + " - " + longMessage);
                            }   //while errorElement -- END
                            Debug.logError("change eBay Paypal failed for Item ID " + itemId + ", errorMessage: " + errorMessage, module);
                            ebayChangePaypalFirst.set("statusId", "FAILED");
                            ebayChangePaypalFirst.set("notes", errorMessage.toString());
                            delegator.store(ebayChangePaypalFirst);
                            
                        }   //if ack failure -- END
                        else {  //if ack success -- START
                            Debug.logError("finished changing paypal for itemId " + itemId, module);
                            ebayChangePaypalFirst.set("statusId", "COMPLETED");
                            delegator.store(ebayChangePaypalFirst);
                        }   //if ack success -- END
                        count++;
                    }   //if sendRequest is true -- END
                }   //if activeListing is not empty -- END
                else {  //if activeListing is empty -- START
                    for (GenericValue ebayChangePaypalNoActive : ebayChangePaypal) {  //loop ebayChangePriceNoActive -- START
                        ebayChangePaypalNoActive.set("statusId", "FAILED");
                        ebayChangePaypalNoActive.set("notes", "No eBay active listing found in database");
                        delegator.store(ebayChangePaypalNoActive);
                    }   //loop ebayChangePriceNoActive -- START
                }   //if activeListing is empty -- END
            }   //loop distinctEbayChangePrice -- END
        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError("finished changeEbayPaypal. " + count + " item ID processed", module);
        return result;
        
    }   //changeEbayPaypal
    
    public static Map<String, Object> changeEbayPriceSingle (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String itemId = (String) context.get("itemId");
        List<String> varSeqList = (List) context.get("varSeq");
        List<String> changePriceList = (List) context.get("price");
        Map result = ServiceUtil.returnSuccess();
        
        Date nowDate = new Date();
        //Debug.logError("itemId: " + itemId, module);
        //Debug.logError("varSeqList: " + varSeqList, module);
        //Debug.logError("changePriceList: " + changePriceList, module);
        double lowestProfitPct = 0.05;
        try {   //main try -- START
            boolean sendRequest = true;
            GenericValue activeListing = delegator.findOne("RivalListingMonitor", UtilMisc.toMap("itemId", itemId, "date", new java.sql.Date(nowDate.getTime())), false);
            if (UtilValidate.isNotEmpty(activeListing)) {   //if activeListing is not empty -- START
                String productStoreId = activeListing.getString("ebayUserId");
                String site = activeListing.getString("site");
                String hasVariation = activeListing.getString("hasVariation");

                Map mapAccount = FastMap.newInstance();
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                if (UtilValidate.isNotEmpty(productStore)) {    //if productStore is not empty == START
                    mapAccount = common.accountInfo(delegator, productStore);
                    mapAccount.put("callName", "ReviseFixedPriceItem");
                    GenericValue ebaySiteCode = EntityUtil.getFirst(delegator.findByAnd("EbaySiteCode", UtilMisc.toMap("abbreviation", site), null, false));
                    mapAccount.put("siteId", ebaySiteCode.getString("ebaySiteId"));
                    mapAccount.put("globalId", ebaySiteCode.getString("ebayGlobalId"));
                    
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument("ReviseFixedPriceItemRequest");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    //Item -- START
                    Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ItemID", itemId, rootDoc);
                    
                    if (hasVariation.equals("N")) { //no variation == START
                        String productId = activeListing.getString("sku");
                        String qtyStr = activeListing.getString("quantity");
                        String qtySold = activeListing.getString("quantitySold");
                        if (UtilValidate.isEmpty(qtyStr)) {
                            qtyStr = "10";
                        }
                        
                        int qty = (int) Math.round((Double.parseDouble(qtyStr) - Double.parseDouble(qtySold)) * 1.25);
                        UtilXml.addChildElementValue(itemElem, "SKU", productId, rootDoc);
                        UtilXml.addChildElementValue(itemElem, "Quantity", qty + "", rootDoc);
                        if (changePriceList.size() > 0) {   //if changePriceList is not empty == START
                            for (String newPrice : changePriceList) {
                                UtilXml.addChildElementValue(itemElem, "StartPrice", newPrice, rootDoc);
                                GenericValue productMasterPrice = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", ebaySiteCode.getString("ebaySiteId")), false);
                                if (UtilValidate.isNotEmpty(productMasterPrice)) {
                                    BigDecimal lowestPriceAllowed = productMasterPrice.getBigDecimal("price").multiply(new BigDecimal(lowestProfitPct / productMasterPrice.getDouble("profitPercentage")));
                                    Debug.logError("productId: " + productId, module);
                                    Debug.logError("newPrice: " + newPrice, module);
                                    Debug.logError("lowestPriceAllowed: " + lowestPriceAllowed, module);
                                    if (lowestPriceAllowed.compareTo(new BigDecimal(newPrice)) <= 0) {
                                        Debug.logError("lowestPriceAllowed compare to new Price is <= 0", module);
                                    }
                                }
                            }
                        }   //if changePriceList is not empty == END
                        else {  //if changePriceList is empty == START
                            sendRequest = false;
                        }   //if changePriceList is empty == END
                    }   //no variation == END
                    else {  //has variation == START
                        Element variationsElem = UtilXml.addChildElement(itemElem, "Variations", rootDoc);
                        if (changePriceList.size() > 0) {   //if changePriceList is not empty == START
                            for (int i = 0; i < changePriceList.size(); i++) { //loop changePriceList == START
                                String varSeqId = varSeqList.get(i);
                                String newPrice = changePriceList.get(i);
                                
                                GenericValue activeListingVariation = delegator.findOne("RivalListingVariation", UtilMisc.toMap("itemId", itemId, "date", new java.sql.Date(nowDate.getTime()), "variationSeqId",varSeqId), false);
                                if (UtilValidate.isNotEmpty(activeListingVariation)) {
                                    String productId = activeListingVariation.getString("sku");
                                    String qtyStr = activeListingVariation.getString("quantity");
                                    String qtySold = activeListingVariation.getString("quantitySold");
                                    if (UtilValidate.isEmpty(qtyStr)) {
                                        qtyStr = "10";
                                    }
                                    int qty = (int) Math.round((Double.parseDouble(qtyStr) - Double.parseDouble(qtySold)) * 1.25);
                                    
                                    Element variationElem = UtilXml.addChildElement(variationsElem, "Variation", rootDoc);
                                    UtilXml.addChildElementValue(variationElem, "SKU", productId, rootDoc);
                                    UtilXml.addChildElementValue(variationElem, "StartPrice", newPrice, rootDoc);
                                    UtilXml.addChildElementValue(variationElem, "Quantity", qty + "", rootDoc);
                                    
                                    List<GenericValue> rivalListingVariationSpecList = delegator.findByAnd("RivalListingVariationSpec", UtilMisc.toMap("itemId", itemId, "date", new java.sql.Date(nowDate.getTime()), "variationSeqId",varSeqId), null, false);
                                    if (UtilValidate.isNotEmpty(rivalListingVariationSpecList)) {   //if rivalListingVariationSpecList is not empty -- START
                                        Element variationSpecificElem = UtilXml.addChildElement(variationElem, "VariationSpecifics", rootDoc);
                                        for (GenericValue listingVariationSpecificsGV : rivalListingVariationSpecList) {    //loop rivalListingVariationSpecList -- START
                                            Element nameValueListElem = UtilXml.addChildElement(variationSpecificElem, "NameValueList", rootDoc);
                                            UtilXml.addChildElementValue(nameValueListElem, "Name", listingVariationSpecificsGV.getString("variationName"), rootDoc);
                                            UtilXml.addChildElementValue(nameValueListElem, "Value", listingVariationSpecificsGV.getString("variationValue"), rootDoc);
                                        }   //loop rivalListingVariationSpecList -- END
                                    }   //if rivalListingVariationSpecList is not empty -- END
                                    else {  //if rivalListingVariationSpecList is empty -- START
                                        sendRequest = false;
                                    }   //if rivalListingVariationSpecList is empty -- END
                                    
                                }   //if activeListingVariation is not empty == END
                                else {  //if activeListingVariation is empty == START
                                    sendRequest = false;
                                }   //if activeListingVariation is empty == END
                            }   //loop changePriceList == END
                        }   //if changePriceList is not empty == END
                        else {  //if changePriceList is empty == START
                            sendRequest = false;
                        }   //if changePriceList is empty == END
                    }   //has variation == END
                    //Building XML -- END
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    Debug.logError(requestXMLcode, module);
                    Debug.logError("sendRequest: " + sendRequest, module);
                    
                    sendRequest = false;
                    if (sendRequest) {  //if sendRequest is true -- START
                        String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                        Debug.logError(responseXML, module);
                        Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                        Element elemResponse = docResponse.getDocumentElement();
                        String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                        
                        if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                            List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                            Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                            StringBuffer errorMessage = new StringBuffer();
                            while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                                Element errorElement = errorElementsElemIter.next();
                                String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                                String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                                errorMessage.append(shortMessage + " - " + longMessage);
                            }   //while errorElement -- END
                        }   //if ack failure -- END
                        else {  //if ack success -- START
                            Map<String, Object> test = dispatcher.runSync("rivalMonitorGetPriceSingle", UtilMisc.toMap("itemId", itemId, "userLogin", userLogin));

                        }   //if ack success -- END
                    }   //if sendRequest is true -- END
                    
                    
                    
                    
                    
                    
                }   //if productStore is not empty == END
            }   //if activeListing is not empty -- END
            
        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        //Debug.logError("finished changeEbayPriceSingle.", module);
        return result;
        
    }   //changeEbayPriceSingle
    
    public static Map<String, Object> changeEbayTemp (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {   //changeEbayPaypal
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        int count = 0;
        
        try {   //main try -- START
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "PENDING")
                                                                                      ));
            List<GenericValue> distinctEbayChangePaypalList = delegator.findList("EbayChangeTemp", condition,
                                                                                 UtilMisc.toSet("itemId"),
                                                                                 UtilMisc.toList("itemId"),
                                                                                 new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
            
            for (GenericValue distinctEbayChangePaypal : distinctEbayChangePaypalList) {  //loop distinctEbayChangePrice -- START
                String itemId = distinctEbayChangePaypal.getString("itemId");
                Debug.logError("processing itemID " + itemId, module);
                List<GenericValue> ebayChangePaypal = delegator.findByAnd("EbayChangeTemp", UtilMisc.toMap("itemId", itemId), null, false);
                GenericValue ebayChangePaypalFirst = EntityUtil.getFirst(ebayChangePaypal);
                
                String firstName = ebayChangePaypalFirst.getString("firstName");
                String firstValue = ebayChangePaypalFirst.getString("firstValue");
                String secondName = ebayChangePaypalFirst.getString("secondName");
                String secondValue = ebayChangePaypalFirst.getString("secondValue");
                String thirdName = ebayChangePaypalFirst.getString("thirdName");
                String thirdValue = ebayChangePaypalFirst.getString("thirdValue");
                String fourthName = ebayChangePaypalFirst.getString("fourthName");
                String fourthValue = ebayChangePaypalFirst.getString("fourthValue");
                
                List<GenericValue> activeListing = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false);
                if (UtilValidate.isNotEmpty(activeListing)) {   //if activeListing is not empty -- START
                    //Debug.logError("ebayActiveListing for itemId " + itemId + " is not empty, continue to process", module);
                    GenericValue activeListingProductStore = EntityUtil.getFirst(activeListing);
                    String productStoreId = activeListingProductStore.getString("productStoreId");
                    String listingType = activeListingProductStore.getString("listingType");
                    String site = activeListingProductStore.getString("site");
                    String callName  = "ReviseFixedPriceItem";
                    if (callName.toUpperCase().equals("CHINESE")) {
                        callName = "ReviseItem";
                    }
                    
                    
                    boolean sendRequest = true;
                    //Debug.logError("productStoreId is  " + productStoreId + " and site is " + site, module);
                    Map mapAccount = FastMap.newInstance();
                    GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                    mapAccount = common.accountInfo(delegator, productStore);
                    mapAccount.put("callName", callName);
                    GenericValue ebaySiteCode = EntityUtil.getFirst(delegator.findByAnd("EbaySiteCode", UtilMisc.toMap("ebaySite", site), null, false));
                    mapAccount.put("siteId", ebaySiteCode.getString("ebaySiteId"));
                    mapAccount.put("globalId", ebaySiteCode.getString("ebayGlobalId"));
                    
                    //Debug.logError("start building requestXML", module);
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument(callName + "Request");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    //Item -- START
                    Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ItemID", itemId, rootDoc);
                    
                    
                    Element returnElement = UtilXml.addChildElement(itemElem, "ReturnPolicy", rootDoc);
                    UtilXml.addChildElementValue(returnElement, "Description", firstValue, rootDoc);
                    UtilXml.addChildElementValue(returnElement, "RefundOption", "MoneyBack", rootDoc);
                    UtilXml.addChildElementValue(returnElement, "RestockingFeeValueOption", "NoRestockingFee", rootDoc);
                    UtilXml.addChildElementValue(returnElement, "ReturnsAcceptedOption", "ReturnsAccepted", rootDoc);
                    UtilXml.addChildElementValue(returnElement, "ReturnsWithinOption", "Days_30", rootDoc);
                    UtilXml.addChildElementValue(returnElement, "ShippingCostPaidByOption", "Buyer", rootDoc);
                    
                    
                    
                    if (UtilValidate.isNotEmpty(secondName)) {
                        UtilXml.addChildElementValue(itemElem, secondName, secondValue, rootDoc);
                    }
                    if (UtilValidate.isNotEmpty(thirdName)) {
                        UtilXml.addChildElementValue(itemElem, thirdName, thirdValue, rootDoc);
                    }
                    if (UtilValidate.isNotEmpty(fourthName)) {
                        UtilXml.addChildElementValue(itemElem, fourthName, fourthValue, rootDoc);
                    }
                    
                    
                    
                    String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    //Debug.logError(requestXMLcode, module);
                    //Debug.logError("send Request is " + sendRequest, module);
                    //Debug.logError("start sending requestXML to eBay API", module);
                    if (sendRequest) {  //if sendRequest is true -- START
                        String responseXML = sendRequestXMLToEbay(mapAccount, requestXMLcode);
                        //Debug.logError(responseXML, module);
                        Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                        Element elemResponse = docResponse.getDocumentElement();
                        String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                        
                        if (!ack.equals("Success") && !ack.equals("Warning")) {   //if ack failure -- START
                            List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                            Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                            StringBuffer errorMessage = new StringBuffer();
                            while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                                Element errorElement = errorElementsElemIter.next();
                                String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                                String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                                errorMessage.append(shortMessage + " - " + longMessage);
                            }   //while errorElement -- END
                            Debug.logError("change eBay temp failed for Item ID " + itemId + ", errorMessage: " + errorMessage, module);
                            ebayChangePaypalFirst.set("statusId", "FAILED");
                            ebayChangePaypalFirst.set("notes", errorMessage.toString());
                            delegator.store(ebayChangePaypalFirst);
                            
                        }   //if ack failure -- END
                        else {  //if ack success -- START
                            Debug.logError("finished changing temp for itemId " + itemId, module);
                            ebayChangePaypalFirst.set("statusId", "COMPLETED");
                            delegator.store(ebayChangePaypalFirst);
                        }   //if ack success -- END
                        count++;
                    }   //if sendRequest is true -- END
                }   //if activeListing is not empty -- END
                else {  //if activeListing is empty -- START
                    for (GenericValue ebayChangePaypalNoActive : ebayChangePaypal) {  //loop ebayChangePriceNoActive -- START
                        ebayChangePaypalNoActive.set("statusId", "FAILED");
                        ebayChangePaypalNoActive.set("notes", "No eBay active listing found in database");
                        delegator.store(ebayChangePaypalNoActive);
                    }   //loop ebayChangePriceNoActive -- START
                }   //if activeListing is empty -- END
            }   //loop distinctEbayChangePrice -- END
        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError("finished changeEbayTemp. " + count + " item ID processed", module);
        return result;
        
    }   //changeEbayTemp


}
