package com.gudao.wish;
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
import java.net.URLEncoder;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import java.util.Random;
import java.text.SimpleDateFormat;

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

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.commons.lang.StringEscapeUtils;

import org.json.JSONObject;
import org.json.XML;


import javolution.util.FastMap;

public class wish {
	private static final String module = wish.class.getName();
    
    public static Map<String, String> wishProperties ()
    throws IOException {
        
        Map<String, String> mapContent = FastMap.newInstance();
        try {   //main try -- START
            Properties properties = new Properties();
            properties.load(new FileInputStream("hot-deploy/gudao/config/wish.properties"));
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
    
    public static int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
    
    public static String sendHttpRequest(String myUrl) throws IOException
    
    {   //sendHttpRequest
        String response = null;
        
        //new code
        try {
            
            URL url = new URL(myUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(60000);
            connection.connect();
            
            int responseCode = connection.getResponseCode();
            InputStream inputStream = null;
            
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                response = inputStreamToString(inputStream);
            } else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                do {
                    Debug.logError("connection Time out, repost HTTP request", module);
                    HttpURLConnection newConnection = (HttpURLConnection) url.openConnection();;
                    newConnection.setRequestMethod("GET");
                    newConnection.setReadTimeout(60000);
                    newConnection.connect();
                    
                    responseCode = newConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                        inputStream = newConnection.getInputStream();
                        response = inputStreamToString(inputStream);
                    }
                    
                }
                while (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT);
            }
            else {
                inputStream = connection.getErrorStream();
                response = inputStreamToString(inputStream);
            }
            

            //return (response == null || "".equals(response.trim())) ? String.valueOf(responseCode) : response;
        }//new code
        catch (Exception e) {
            e.printStackTrace();
        }//new code
        return response;
        
    }   //End of sendHttpRequest
    
    public static Map<String, Object> wishGetRefreshCode(DispatchContext dctx, Map context)
    throws IOException, GenericEntityException, SAXException, ParserConfigurationException, GenericServiceException
    {   //wishGetRefreshCode
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String getAccessCode = (String) context.get("getAccessCode");
        
        boolean getAccessCodeBoolean = false;
        if (UtilValidate.isNotEmpty(getAccessCode) && getAccessCode.toUpperCase().equals("Y")) {
            getAccessCodeBoolean = true;
        }

        String response = null;
        Properties properties = new Properties();
        properties.load(new FileInputStream("hot-deploy/gudao/config/wish.properties"));
        
        String urlFromProperty = "refreshCodeUrl";
        if (getAccessCodeBoolean) {
            urlFromProperty = "accessCodeUrl";
        }
        String postItemsUrl = properties.getProperty(urlFromProperty);
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(now.getTime());
        
        StringBuffer propertyUrl = new StringBuffer("?&format=xml");
        try {   //main TRY == START
            String clientId = null;
            String clientSecret = null;
            String redirectUri = null;
            String authorizationCode = null;
            String accessCode = null;
            String refreshCode = null;
            
            GenericValue clientIdGV = delegator.findOne("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_CLIENT_ID"), false);
            if (UtilValidate.isNotEmpty(clientIdGV)) {
                clientId = URLEncoder.encode(clientIdGV.getString("idValue"), "UTF-8");
                propertyUrl.append("&client_id=" + clientId);
            }
            
            GenericValue clientSecretGV = delegator.findOne("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_CLIENT_SECRET"), false);
            if (UtilValidate.isNotEmpty(clientSecretGV)) {
                clientSecret = URLEncoder.encode(clientSecretGV.getString("idValue"), "UTF-8");
                propertyUrl.append("&client_secret=" + clientSecret);
            }
            
            if (getAccessCodeBoolean) {
                GenericValue redirectUriGV = delegator.findOne("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_REDIRECT_URI"), false);
                if (UtilValidate.isNotEmpty(redirectUriGV)) {
                    redirectUri = URLEncoder.encode(redirectUriGV.getString("idValue"), "UTF-8");
                    propertyUrl.append("&redirect_uri=" + redirectUri);
                }
                
                GenericValue authorizationCodeGV = delegator.findOne("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_AUTHORIZATION_CODE"), false);
                if (UtilValidate.isNotEmpty(authorizationCodeGV)) {
                    authorizationCode = URLEncoder.encode(authorizationCodeGV.getString("idValue"), "UTF-8");
                    propertyUrl.append("&code=" + authorizationCode);
                    propertyUrl.append("&grant_type=authorization_code");
                }
            } else {
                GenericValue refreshCodeGV = delegator.findOne("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_REFRESH_CODE"), false);
                if (UtilValidate.isNotEmpty(refreshCodeGV)) {
                    refreshCode = URLEncoder.encode(refreshCodeGV.getString("idValue"), "UTF-8");
                    propertyUrl.append("&refresh_token=" + refreshCode);
                    propertyUrl.append("&grant_type=refresh_token");
                }
            }
            
        }   //main TRY == END
        catch (Exception e) {
            e.printStackTrace();
        }
        //Debug.logError(postItemsUrl + propertyUrl.toString(), module);
        URL url = new URL(postItemsUrl + propertyUrl.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(60000);
        connection.connect();
        
        int responseCode = connection.getResponseCode();
        InputStream inputStream = null;
        
        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = connection.getInputStream();
            response = inputStreamToString(inputStream);
        } else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
            do {
                Debug.logError("connection Time out, repost HTTP request", module);
                HttpURLConnection newConnection = (HttpURLConnection) url.openConnection();;
                newConnection.setRequestMethod("GET");
                newConnection.setReadTimeout(60000);
                newConnection.connect();
                
                responseCode = newConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = newConnection.getInputStream();
                    response = inputStreamToString(inputStream);
                }
                
            }
            while (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT);
        }
        else {
            inputStream = connection.getErrorStream();
            response = inputStreamToString(inputStream);
        }
        
        String responseXML = response;
        //Debug.logError(responseXML, module);
        Document docResponse = UtilXml.readXmlDocument(responseXML, true);
        Element elemResponse = docResponse.getDocumentElement();
        String ack = UtilXml.childElementValue(elemResponse, "Code", null);
        //Debug.logError("Code: " + ack, module);
        if (ack.equals("0")) {    //if ack success -- START
            
            String jsonCode = UtilXml.childElementValue(elemResponse, "Data", null);
            if (UtilValidate.isNotEmpty(jsonCode)) {    //if jsonCode is not empty == START
                JSONObject json = new JSONObject(jsonCode);
                String xml = XML.toString(json);
                StringBuffer responseXmlBuffer = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?><Response>");
                responseXmlBuffer.append(xml);
                responseXmlBuffer.append("</Response>");
                String responseXml = responseXmlBuffer.toString();
                //Debug.logError(responseXml, module);
                Document rootDoc = UtilXml.readXmlDocument(responseXml, true);
                Element rootElement = rootDoc.getDocumentElement();
                
                
                String resultAccessCode = UtilXml.childElementValue(rootElement, "access_token", null);
                if (UtilValidate.isNotEmpty(resultAccessCode)) {
                    resultAccessCode = resultAccessCode.replaceAll("u&apos;","");
                    resultAccessCode = resultAccessCode.replaceAll("&apos;","");
                    resultAccessCode = resultAccessCode.replaceAll("u'","");
                    resultAccessCode = resultAccessCode.replaceAll("'","");
                }
                
                String refreshCode = UtilXml.childElementValue(rootElement, "refresh_token", null);
                refreshCode = refreshCode.replaceAll("u&apos;","");
                refreshCode = refreshCode.replaceAll("&apos;","");
                refreshCode = refreshCode.replaceAll("u'","");
                refreshCode = refreshCode.replaceAll("'","");
                String merchantUser = UtilXml.childElementValue(rootElement, "merchant_user_id", null);
                
                GenericValue accessCodeGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_ACCESS_CODE"));
                accessCodeGV.set("idValue", resultAccessCode);
                delegator.createOrStore(accessCodeGV);
                
                GenericValue refreshCodeGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_REFRESH_CODE"));
                refreshCodeGV.set("idValue", refreshCode);
                delegator.createOrStore(refreshCodeGV);
                
                GenericValue merchantUserGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_MERCHANT_USER_ID"));
                merchantUserGV.set("idValue", merchantUser);
                delegator.createOrStore(merchantUserGV);
            }   //if jsonCode is not empty == END
            
            //writing data into database -- START
            /*
            int accessCodeStart = responseXML.indexOf("'access_token':");
            int accessCodeEnd = responseXML.indexOf("', '", accessCodeStart);
            
            String resultAccessCode = responseXML.substring(accessCodeStart + 17, accessCodeEnd);
            
            if (UtilValidate.isNotEmpty(resultAccessCode)) {
                resultAccessCode = resultAccessCode.replaceAll("'","");
                
                int refreshCodeStart = responseXML.indexOf("'refresh_token':");
                int refreshCodeEnd = responseXML.indexOf("'}", refreshCodeStart);
                String refreshCode = responseXML.substring(refreshCodeStart + 18, refreshCodeEnd);
                refreshCode = refreshCode.replaceAll("'","");
                
                int merchantUserStart = responseXML.indexOf("'merchant_user_id':");
                int merchantUserEnd = responseXML.indexOf("', '", merchantUserStart);
                String merchantUser = responseXML.substring(merchantUserStart + 21, merchantUserEnd);
                merchantUser = merchantUser.replaceAll("'","");
                
                GenericValue accessCodeGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_ACCESS_CODE"));
                accessCodeGV.set("idValue", resultAccessCode);
                delegator.createOrStore(accessCodeGV);
                
                GenericValue refreshCodeGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_REFRESH_CODE"));
                refreshCodeGV.set("idValue", refreshCode);
                delegator.createOrStore(refreshCodeGV);
                
                GenericValue merchantUserGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_MERCHANT_USER_ID"));
                merchantUserGV.set("idValue", merchantUser);
                delegator.createOrStore(merchantUserGV);
            }*/
            //writing data into database -- END
            
        }   //if ack success -- END
        else {  //if ack failure -- START
            String errorMessage = UtilXml.childElementValue(elemResponse, "Message", null);
            FileWriter f1 = new FileWriter("hot-deploy/gudao/webapp/gudao/wish/logError/oauth.log", true);
            f1.write(today + ": product Store ID: " + productStoreId + ", Codes : " + ack + ": " + errorMessage + "\n");
            f1.close();
        }   //if ack failure -- END
        //Reading XML -- END
        
        return result;
    }   //wishGetRefreshCode
 
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
    
    public static Map<String, Object> crawlAllAccount (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        try {   //main try -- START
            List<GenericValue> productStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("primaryStoreGroupId", "WISH", "isDemoStore", "N"), null, false);
            for (GenericValue productStore : productStoreList) {        //loop productStoreList -- START
                String productStoreId = productStore.getString("productStoreId");
                Map getAllProducts = dispatcher.runSync("wishGetAllProducts", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
            }   //loop productStoreList -- START
        }   //main try -- END
        catch (GenericEntityException e) {
            Debug.logError("crawlAllAccount GenericEntityException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (GenericServiceException e) {
            Debug.logError("crawlAllAccount GenericEntityException Error for TRY CATCH: " + e.getMessage(), module);
        }
        return result;
        
    }
    
    public static Map<String, Object> getAllProducts (DispatchContext dctx, Map context)
    throws GenericEntityException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String url = "https://china-merchant.wish.com/api/v2/product/multi-get";
        
        if (UtilValidate.isNotEmpty(productStoreId)) {
            productStoreId = productStoreId.trim();
        }
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(now.getTime());
        java.sql.Date dateSql = new java.sql.Date(now.getTime().getTime());
        int productCount = 0;
        
        try {   //main try -- START
            Debug.logError("Wish crawling " + productStoreId, module);
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            Map removeListing = dispatcher.runSync("wishRemoveListing", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
            String productStoreGroup = productStore.getString("primaryStoreGroupId");
            if (!productStoreGroup.equals("WISH")) {
                return ServiceUtil.returnSuccess("productStoreId " + productStoreId + " is not a wish account");
            }
            List<GenericValue> productStoreRoleList = delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"), "roleTypeId", "WISH_ACCOUNT"), null, false);
            if (UtilValidate.isEmpty(productStoreRoleList)) {   //if productStoreRoleList is empty -- START
                Debug.logError("ProductStoreId " + productStoreId + " does not have Wish account role", module);
                return ServiceUtil.returnError("ProductStoreId " + productStoreId + " does not have Wish account role");
            }   //if productStoreRoleList is empty -- END
            GenericValue productStoreRole = EntityUtil.getFirst(productStoreRoleList);
            GenericValue wishAccountPartyGroup = productStoreRole.getRelatedOne("PartyGroup", false);
            GenericValue wishAccountParty = wishAccountPartyGroup.getRelatedOne("Party", false);
            GenericValue partyIdentification = delegator.findOne("PartyIdentification", UtilMisc.toMap("partyId",wishAccountParty.getString("partyId"), "partyIdentificationTypeId", "WISH_ACCESS_CODE"), false);
            String apiKey = URLEncoder.encode(partyIdentification.getString("idValue"), "UTF-8");
            
            String requestUrl = url + "?format=xml&limit=250&access_token=" + apiKey;
            boolean keepLooping = false;
            int loopCount = 0;
            //Debug.logError("URL: " + requestUrl, module);
            do {    //do loop -- START
                //Debug.logError("run Check DO", module);
                keepLooping = false;
                //Debug.logError("URL: " + requestUrl, module);
                String responseXML = sendHttpRequest(requestUrl);
                //Debug.logError(responseXML, module);
                //Reading XML -- START
                Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                Element elemResponse = docResponse.getDocumentElement();
                String ack = UtilXml.childElementValue(elemResponse, "Code", null);
                //Debug.logError("Code: " + ack, module);
                if (ack.equals("0")) {    //if ack success -- START
                    //writing data into database -- START
                    Map writeResult = writeWishProductIntoDatabase(dctx, context, responseXML);
                    productCount += (Integer) writeResult.get("productCount");
                    //writing data into database -- END
                    //FileWriter f1 = new FileWriter("hot-deploy/gudao/webapp/gudao/wish/logError/responseXML" + loopCount + ".xml", true);
                    //f1.write(responseXML);
                    //f1.close();
                    
                    Element pagingElement = UtilXml.firstChildElement(elemResponse, "Paging");
                    List<? extends Element> nextElements = UtilXml.childElementList(pagingElement, "Next");
                    Iterator<? extends Element> nextElementsElemIter = nextElements.iterator();
                    while (nextElementsElemIter.hasNext()) {    //check Next Element -- START
                        Element nextElement = nextElementsElemIter.next();
                        String nextUrl = UtilXml.elementValue(nextElement);
                        if (UtilValidate.isNotEmpty(nextUrl)) {
                            keepLooping = true;
                            requestUrl = nextUrl;
                        }
                    }   //check Next Element -- END
                }   //if ack success -- END
                else if (ack.equals("1016") || ack.equals("1015")) {  //refresh token == START
                    Map refreshWishToken = dispatcher.runSync("wishGetRefreshCode", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
                    if (ServiceUtil.isSuccess(refreshWishToken)) {
                        Map rerunThisFunction = dispatcher.runSync("wishGetAllProducts", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
                    }
                    break;
                }   //refresh token == END
                else {  //if ack failure -- START
                    String errorMessage = UtilXml.childElementValue(elemResponse, "Message", null);
                    FileWriter f1 = new FileWriter("hot-deploy/gudao/webapp/gudao/wish/logError/product-multi-get.log", true);
                    f1.write(today + ": product Store ID: " + productStoreId + ", Codes : " + ack + ": " + errorMessage + "\n");
                    f1.close();
                }   //if ack failure -- END
                //Reading XML -- END
                loopCount++;
            }   //do loop -- END
            while (keepLooping);
            //Debug.logError("run Check End", module);
        }   //main try -- END
        catch (GenericEntityException e) {
            Debug.logError("Yasin: getAllProducts GenericEntityException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (SAXException e) {
            Debug.logError("Yasin: getAllProducts SAXException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (ParserConfigurationException e) {
            Debug.logError("Yasin: getAllProducts ParserConfigurationException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (IOException e) {
            Debug.logError("Yasin: getAllProducts IOException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (Exception e) {
            e.printStackTrace();
            Debug.logError("Yasin: getAllProducts Exception Error for TRY CATCH: " + e.getMessage(), module);
            try {
                Map rerunGetAllProducts = dispatcher.runSync("wishGetAllProducts", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
            }
            catch (Exception ee) {
                ee.printStackTrace();
                Debug.logError("Rerun exception also return another exception", module);
                
                GenericValue updateActiveListingStatusFailed = delegator.makeValue("ActiveListingStatus", UtilMisc.toMap("productStoreId", productStoreId, "platform", "WISH", "date", Timestamp.valueOf(today)));
                updateActiveListingStatusFailed.set("totalListingCount", Long.valueOf(productCount));
                updateActiveListingStatusFailed.set("status", "CRAWL_FAILED");
                delegator.createOrStore(updateActiveListingStatusFailed);
                
                return ServiceUtil.returnError("ProductStoreId " + productStoreId + " failed Crawling WISH allhash");
            }
        }
        
        GenericValue updateLastRuntime = delegator.makeValue("AutoScheduleJobHistory", UtilMisc.toMap("productStoreId", productStoreId, "serviceName", "wishGetAllProducts", "lastRuntime", Timestamp.valueOf(today)));
        delegator.createOrStore(updateLastRuntime);
        
        GenericValue updateActiveListingStatus = delegator.makeValue("ActiveListingStatus", UtilMisc.toMap("productStoreId", productStoreId, "platform", "WISH", "date", Timestamp.valueOf(today)));
        updateActiveListingStatus.set("totalListingCount", Long.valueOf(productCount));
        updateActiveListingStatus.set("status", "CRAWL_FINISHED");
        delegator.createOrStore(updateActiveListingStatus);
        return result;
    }   //getAllProducts
    
    public static Map<String, Object> writeWishProductIntoDatabase(DispatchContext dctx, Map context, String responseXML)
    throws GenericEntityException, GenericServiceException, SAXException, ParserConfigurationException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        //formatting the sequence ID
        DecimalFormat df = new DecimalFormat("00000");
        String productStoreId = (String) context.get("productStoreId");
        Calendar now = Calendar.getInstance();
        Date nowDate = new Date();
        int productCount = 0;
        
        try {   //main try -- START
            //Debug.logError("run Check Write", module);
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            String code = UtilXml.childElementValue(elemResponse, "Code", null);
            
            if (code.equals("0")) { //if response OK -- START
                
                Element dataElement = UtilXml.firstChildElement(elemResponse, "Data");
                List<? extends Element> productElements = UtilXml.childElementList(dataElement, "Product");
                Iterator<? extends Element> productElementsElemIter = productElements.iterator();
                while (productElementsElemIter.hasNext()) { //loop Product -- START
                    
                    Element productElement = productElementsElemIter.next();
                    String mainImage = UtilXml.childElementValue(productElement, "main_image", null);
                    String isPromoted = UtilXml.childElementValue(productElement, "is_promoted", null);
                    String description = UtilXml.childElementValue(productElement, "description", null);
                    String reviewStatus = UtilXml.childElementValue(productElement, "review_status", null);
                    String upc = UtilXml.childElementValue(productElement, "upc", null);
                    String extraImages = UtilXml.childElementValue(productElement, "extra_images", null);
                    String numberSaves = UtilXml.childElementValue(productElement, "number_saves", null);
                    String numberSold = UtilXml.childElementValue(productElement, "number_sold", null);
                    String parentSku = UtilXml.childElementValue(productElement, "parent_sku", null);
                    String wishId = UtilXml.childElementValue(productElement, "id", null);
                    String name = UtilXml.childElementValue(productElement, "name", null);
                    String dateUploaded = UtilXml.childElementValue(productElement, "date_uploaded", null);
                    
                    GenericValue wishListing = delegator.findOne("WishListing", UtilMisc.toMap("wishId", wishId), false);
                    if (UtilValidate.isEmpty(wishListing)) {
                        wishListing = delegator.makeValue("WishListing", UtilMisc.toMap("wishId", wishId));
                    }
                    
                    wishListing.set("parentSku", parentSku);
                    wishListing.set("productStoreId", productStoreId);
                    wishListing.set("mainImage", mainImage);
                    wishListing.set("name", name);
                    wishListing.set("isPromoted", isPromoted.toUpperCase());
                    wishListing.set("description", description);
                    wishListing.set("reviewStatus", reviewStatus.toUpperCase());
                    wishListing.set("extraImages", extraImages);
                    if (UtilValidate.isEmpty(numberSaves)) {
                        numberSaves = "0";
                    }
                    if (UtilValidate.isEmpty(numberSold)) {
                        numberSold = "0";
                    }
                    wishListing.set("numberSaves", Long.valueOf(numberSaves));
                    wishListing.set("numberSold", Long.valueOf(numberSold));
                    wishListing.set("dateUploaded", dateUploaded);
                    
                    Element wishExpressElement = UtilXml.firstChildElement(productElement, "wish_express_country_codes");
                    String wishExpress = UtilXml.childElementValue(wishExpressElement, "wish_express_country_codes", null);
                    wishListing.set("wishExpress", wishExpress);
                    
                    delegator.createOrStore(wishListing);
                    
                    
                    Element tagsElement = UtilXml.firstChildElement(productElement, "tags");
                    List<? extends Element> tagsList = UtilXml.childElementList(tagsElement, "Tag");
                    Iterator<? extends Element> tagsListElemIter = tagsList.iterator();
                    int tagSeq = 0;
                    while (tagsListElemIter.hasNext()) {    //loop tags -- START
                        
                        tagSeq++;
                        String tagSeqId = df.format(tagSeq);
                        Element tagElement = tagsListElemIter.next();
                        String tagId = UtilXml.childElementValue(tagElement, "id", null);
                        String tagName = UtilXml.childElementValue(tagElement, "name", null);
                        GenericValue wishTag = delegator.findOne("WishTags", UtilMisc.toMap("wishId", wishId, "tagSeqId", tagSeqId, "tagType", "INPUT_TAG"), false);
                        if (UtilValidate.isEmpty(wishTag)) {
                            wishTag = delegator.makeValue("WishTags", UtilMisc.toMap("wishId", wishId, "tagSeqId", tagSeqId, "tagType", "INPUT_TAG"));
                        }
                        wishTag.set("tagId", tagId);
                        wishTag.set("tagName", tagName);
                        delegator.createOrStore(wishTag);
                    }   //loop tags -- START
                    
                    Element autoTagsElement = UtilXml.firstChildElement(productElement, "auto_tags");
                    if (UtilValidate.isNotEmpty(autoTagsElement)) { //if auto_tags not empty == START
                        List<? extends Element> autoTagsList = UtilXml.childElementList(autoTagsElement, "Tag");
                        Iterator<? extends Element> autoTagsListElemIter = autoTagsList.iterator();
                        int autoTagSeq = 0;
                        while (autoTagsListElemIter.hasNext()) {    //loop auto tags -- START
                            autoTagSeq++;
                            String autoTagSeqId = df.format(autoTagSeq);
                            Element tagElement = autoTagsListElemIter.next();
                            String tagId = UtilXml.childElementValue(tagElement, "id", null);
                            String tagName = UtilXml.childElementValue(tagElement, "name", null);
                            GenericValue wishTag = delegator.findOne("WishTags", UtilMisc.toMap("wishId", wishId, "tagSeqId", autoTagSeqId, "tagType", "AUTO_TAG"), false);
                            if (UtilValidate.isEmpty(wishTag)) {
                                wishTag = delegator.makeValue("WishTags", UtilMisc.toMap("wishId", wishId, "tagSeqId", autoTagSeqId, "tagType", "AUTO_TAG"));
                            }
                            wishTag.set("tagId", tagId);
                            wishTag.set("tagName", tagName);
                            delegator.createOrStore(wishTag);
                        }   //loop auto tags -- START
                    }   //if auto_tags not empty == END
                    
                    
                    //Variations -- START
                    Element variantsElement = UtilXml.firstChildElement(productElement, "variants");
                    List<? extends Element> variantList = UtilXml.childElementList(variantsElement, "Variant");
                    Iterator<? extends Element> variantListElemIter = variantList.iterator();
                    int varSeq = 0;
                    while (variantListElemIter.hasNext()) { //loop variants -- START
                        //Debug.logError("variation runs", module);
                        varSeq++;
                        String varSeqId = df.format(varSeq);
                        Element variantElement = variantListElemIter.next();
                        String sku = UtilXml.childElementValue(variantElement, "sku", null);
                        String productId = UtilXml.childElementValue(variantElement, "product_id", null);
                        String size = UtilXml.childElementValue(variantElement, "size", null);
                        String color = UtilXml.childElementValue(variantElement, "color", null);
                        String price = UtilXml.childElementValue(variantElement, "price", null);
                        String enabled = UtilXml.childElementValue(variantElement, "enabled", null);
                        String shipping = UtilXml.childElementValue(variantElement, "shipping", null);
                        String allImages = UtilXml.childElementValue(variantElement, "all_images", null);
                        String inventory = UtilXml.childElementValue(variantElement, "inventory", null);
                        String variationId = UtilXml.childElementValue(variantElement, "id", null);
                        String msrp = UtilXml.childElementValue(variantElement, "msrp", null);
                        String shippingTime = UtilXml.childElementValue(variantElement, "shipping_time", null);
                        
                        GenericValue wishListingVariation = delegator.findOne("WishListingVariation", UtilMisc.toMap("wishId", wishId, "varSeqId", varSeqId), false);
                        if (UtilValidate.isEmpty(wishListingVariation)) {
                            wishListingVariation = delegator.makeValue("WishListingVariation", UtilMisc.toMap("wishId", wishId, "varSeqId", varSeqId));
                        }
                        wishListingVariation.set("sku", sku);
                        wishListingVariation.set("normalizedSku", normalizeSkuWish(delegator,sku));
                        wishListingVariation.set("productStoreId", productStoreId);
                        wishListingVariation.set("productId", productId);
                        wishListingVariation.set("size", size);
                        wishListingVariation.set("color", color);
                        if (UtilValidate.isEmpty(price)) {
                            price = "0";
                        }
                        wishListingVariation.set("price", Double.valueOf(price));
                        wishListingVariation.set("enabled", enabled.toUpperCase());
                        if (UtilValidate.isEmpty(shipping)) {
                            shipping = "0";
                        }
                        wishListingVariation.set("shipping", Double.valueOf(shipping));
                        wishListingVariation.set("allImages", allImages);
                        if (UtilValidate.isEmpty(inventory)) {
                            inventory = "0";
                        }
                        wishListingVariation.set("inventory", Long.valueOf(inventory));
                        wishListingVariation.set("variationId", variationId);
                        if (UtilValidate.isEmpty(msrp)) {
                            msrp = "0";
                        }
                        wishListingVariation.set("msrp", Double.valueOf(msrp));
                        wishListingVariation.set("shippingTime", shippingTime);
                        delegator.createOrStore(wishListingVariation);
                    }   //loop variants -- END
                    //Variations -- END
                    productCount++;
                }   //loop Product -- END
            }   //if response OK -- END
        }   //main try -- END
        catch (GenericEntityException e) {
            Debug.logError("Yasin: writeWishProductIntoDatabase GenericEntityException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (SAXException e) {
            Debug.logError("Yasin: writeWishProductIntoDatabase SAXException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (ParserConfigurationException e) {
            Debug.logError("Yasin: writeWishProductIntoDatabase ParserConfigurationException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (IOException e) {
            Debug.logError("Yasin: writeWishProductIntoDatabase IOException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (Exception e) {
            e.printStackTrace();
            Debug.logError("Yasin: writeWishProductIntoDatabase Exception Error for TRY CATCH: " + e.getMessage(), module);
        }
        result.put("productCount", productCount);
        return result;
    }   //writeWishProductIntoDatabase
    
    public static Map<String, Object> removeListing (DispatchContext dctx, Map context)
    throws GenericEntityException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        String productStoreId = (String) context.get("productStoreId");
        Calendar now = Calendar.getInstance();
        java.sql.Date date = new java.sql.Date(now.getTime().getTime());
        
        try {
            delegator.removeByAnd("WishTags", UtilMisc.toMap("productStoreId", productStoreId));
            delegator.removeByAnd("WishListingVariation", UtilMisc.toMap("productStoreId", productStoreId));
            delegator.removeByAnd("WishListing", UtilMisc.toMap("productStoreId", productStoreId));
        }
        catch (GenericEntityException e) {
            Debug.logError("Yasin: removeWishListing GenericEntityException Error for TRY CATCH: " + e.getMessage(), module);
        }
        return result;
    }
    
    public static String normalizeSkuWish(Delegator delegator, String sku)	//Description
    throws GenericEntityException, GenericServiceException {
        
        try {
            GenericValue productMaster = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", sku), false);
            if (UtilValidate.isEmpty(productMaster)) {
                int skuLength = sku.length();
                if (sku.contains("|")) {    //new 变体 format -- START
                    String term[]= sku.split("\\|");
                    sku = term[0];
                }
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
                    if (sku.startsWith("WSGD-")) {
                        sku = sku.replaceFirst("WSGD-","");
                    }
                    if (sku.startsWith("WSGDPL-")) {
                        sku = sku.replaceFirst("WSGDPL-","");
                    }
                    if (sku.startsWith("WSDE-")) {
                        sku = sku.replaceFirst("WSDE-","");
                    }
                    if (sku.startsWith("WSDEPL-")) {
                        sku = sku.replaceFirst("WSDEPL-","");
                    }
                    if (sku.startsWith("WSWA-")) {
                        sku = sku.replaceFirst("WSWA-","");
                    }
                    if (sku.startsWith("WSWAPL-")) {
                        sku = sku.replaceFirst("WSWAPL-","");
                    }
                    if (sku.startsWith("WSOM-")) {
                        sku = sku.replaceFirst("WSOM-","");
                    }
                    if (sku.startsWith("WSOMPL-")) {
                        sku = sku.replaceFirst("WSOMPL-","");
                    }
                    if (sku.startsWith("WSHG-")) {
                        sku = sku.replaceFirst("WSHG-","");
                    }
                    if (sku.startsWith("WSHGPL-")) {
                        sku = sku.replaceFirst("WSHGPL-","");
                    }
                    if (sku.startsWith("WSWF-")) {
                        sku = sku.replaceFirst("WSWF-","");
                    }
                    if (sku.startsWith("WSHB-")) {
                        sku = sku.replaceFirst("WSHB-","");
                    }
                    if (sku.startsWith("WSSB-")) {
                        sku = sku.replaceFirst("WSSB-","");
                    }
                    if (sku.startsWith("WSMF-")) {
                        sku = sku.replaceFirst("WSMF-","");
                    }
                    if (sku.startsWith("WSJW-")) {
                        sku = sku.replaceFirst("WSJW-","");
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
    
    public static Map<String, Object> createNewAccount (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("accountName");
        String clientId = (String) context.get("clientId");
        String clientSecret = (String) context.get("clientSecret");
        String authorizationCode = (String) context.get("authorizationCode");
        String redirectUri = (String) context.get("redirectUri");
        productStoreId = productStoreId.trim();
        clientId = clientId.trim();
        clientSecret =  clientSecret.trim();
        authorizationCode = authorizationCode.trim();
        if (UtilValidate.isNotEmpty(redirectUri)) {
            redirectUri = redirectUri.trim();
        } else {
            redirectUri = "https://merchant.wish.com";
        }
        
        try {   //main try == START
            List<GenericValue> existingProductStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("primaryStoreGroupId", "WISH"), null, false);
            int wishSeq = 1;
            if (UtilValidate.isNotEmpty(existingProductStoreList)) {
                for (GenericValue existingProductStore : existingProductStoreList) {    //loop existingProductStoreList == START
                    String existingSubtitle = existingProductStore.getString("subtitle");
                    int currentStoreSeq = Integer.parseInt(existingSubtitle);
                    if (wishSeq <= currentStoreSeq) {
                        wishSeq = currentStoreSeq;
                    }
                }   //loop existingProductStoreList == END
                wishSeq = wishSeq + 1;
            }
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            if (UtilValidate.isEmpty(productStore)) {   //if productStore is not empty == START
                productStore = delegator.makeValue("ProductStore", UtilMisc.toMap("productStoreId", productStoreId));
                productStore.set("storeName", productStoreId);
                productStore.set("title", productStoreId);
                productStore.set("subtitle", wishSeq + "");
                productStore.set("companyName", "Gudao");
                productStore.set("primaryStoreGroupId", "WISH");
                productStore.set("isDemoStore", "N");
                productStore.set("visualThemeId", "EC_DEFAULT");
                productStore.set("inventoryFacilityId", "PDWarehouse");
                productStore.set("defaultSalesChannelEnumId", "WISH_SALES_CHANNEL");
                delegator.create(productStore);
                
                GenericValue party = delegator.makeValue("Party", UtilMisc.toMap("partyId", productStoreId, "partyTypeId", "PARTY_GROUP", "preferredCurrencyUomId", "USD", "statusId", "PARTY_ENABLED"));
                delegator.create(party);
                
                GenericValue partyGroup = delegator.makeValue("PartyGroup", UtilMisc.toMap("partyId", productStoreId, "groupName", productStoreId));
                delegator.create(partyGroup);
                
                GenericValue partyRole = delegator.makeValue("PartyRole", UtilMisc.toMap("partyId", productStoreId, "roleTypeId", "WISH_ACCOUNT"));
                delegator.create(partyRole);
                
                Calendar now = Calendar.getInstance();
                String fixedFromDate = new SimpleDateFormat("2010-01-01 HH:mm:ss.SSS").format(now.getTime());
                GenericValue productStoreRole = delegator.makeValue("ProductStoreRole", UtilMisc.toMap("partyId", productStoreId, "roleTypeId", "WISH_ACCOUNT", "productStoreId", productStoreId, "fromDate", Timestamp.valueOf(fixedFromDate)));
                delegator.create(productStoreRole);
                
                GenericValue clientIdGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                clientIdGV.set("partyIdentificationTypeId", "WISH_CLIENT_ID");
                clientIdGV.set("idValue", clientId);
                delegator.create(clientIdGV);
                
                GenericValue clientSecretGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                clientSecretGV.set("partyIdentificationTypeId", "WISH_CLIENT_SECRET");
                clientSecretGV.set("idValue", clientSecret);
                delegator.create(clientSecretGV);
                
                GenericValue redirectUriGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                redirectUriGV.set("partyIdentificationTypeId", "WISH_REDIRECT_URI");
                redirectUriGV.set("idValue", redirectUri);
                delegator.create(redirectUriGV);
                
                GenericValue authorizationCodeGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                authorizationCodeGV.set("partyIdentificationTypeId", "WISH_AUTHORIZATION_CODE");
                authorizationCodeGV.set("idValue", authorizationCode);
                delegator.create(authorizationCodeGV);
                
                Map wishGetRefreshCode = dispatcher.runSync("wishGetRefreshCode", UtilMisc.toMap("productStoreId", productStoreId, "getAccessCode", "Y", "userLogin", userLogin));
                if (ServiceUtil.isSuccess(wishGetRefreshCode)) {
                    result = ServiceUtil.returnSuccess();
                } else {
                    result = ServiceUtil.returnError("wishGetRefreshCode returns error: " + wishGetRefreshCode.get("errorMessage"));
                }
            }   //if productStore is not empty == END
            else {  //if productStore is empty == START
                result = ServiceUtil.returnSuccess("AccountName " + productStoreId + " has been created before. Not doing anything");
            }   //if productStore is empty == END
        }   //main try == END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }
        catch (GenericServiceException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }
        
        return result;

        
    }   //createNewAccount
    
    public static Map<String, Object> getAccountStatistic (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, SAXException, ParserConfigurationException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("accountName");
        
        if (UtilValidate.isNotEmpty(productStoreId)) {
            productStoreId = productStoreId.trim();
        }
        
        try {   //main try == START
            List<GenericValue> wishListingVariationList = delegator.findByAnd("WishListingVariation", UtilMisc.toMap("productStoreId", productStoreId, "enabled", "TRUE"), UtilMisc.toList("lastUpdatedStamp DESC"), true);
            if (UtilValidate.isNotEmpty(wishListingVariationList)) {    //if wishListingVariationList is not empty == START
                GenericValue wishListingVariation = EntityUtil.getFirst(wishListingVariationList);
                GenericValue wishListing = delegator.findOne("WishListing", UtilMisc.toMap("listingId", wishListingVariation.getString("listingId")), false);
                String wishId = wishListing.getString("wishId");
                
                Map wishCrawlPageSingle = dispatcher.runSync("wishCrawlPageSingle", UtilMisc.toMap("wishId", wishId, "userLogin", userLogin));
                if (ServiceUtil.isSuccess(wishCrawlPageSingle)) {   //if wishCrawlPageSingle success == START
                    String responseXml = wishCrawlPageSingle.get("xml").toString();
                
                    if (UtilValidate.isNotEmpty(responseXml)) { //if responseXml not empty == START
                        Document docResponse = UtilXml.readXmlDocument(responseXml, true);
                        Element elemResponse = docResponse.getDocumentElement();
                        
                        GenericValue wishAccountStatistic = delegator.makeValue("WishAccountStatistic", UtilMisc.toMap("productStoreId", productStoreId));
                        String trustedStore = UtilXml.childElementValue(elemResponse, "from_trusted_store", null);
                        if (UtilValidate.isNotEmpty(trustedStore)) {    //update trustedStore == START
                            if (trustedStore.toUpperCase().equals(trustedStore)) {
                                wishAccountStatistic.set("trustedStore", "Y");
                            } else {
                                wishAccountStatistic.set("trustedStore", "N");
                            }
                        } else {
                            wishAccountStatistic.set("trustedStore", "N");
                        }   //update trustedStore == END
                        
                        Element dataElement = UtilXml.firstChildElement(elemResponse, "commerce_product_info");
                        List<? extends Element> variationsElements = UtilXml.childElementList(dataElement, "variations");
                        Iterator<? extends Element> variationsElementsElemIter = variationsElements.iterator();
                        while (variationsElementsElemIter.hasNext()) {  //loop variationsElementsElemIter == START
                            Element variationsElement = variationsElementsElemIter.next();
                            String merchantName = UtilXml.childElementValue(variationsElement, "merchant_name", null);
                            String merchantId = UtilXml.childElementValue(variationsElement, "merchant_id", null);
                            String merchantRatingCount = UtilXml.childElementValue(variationsElement, "merchant_rating_count", null);
                            String merchantRating = UtilXml.childElementValue(variationsElement, "merchant_rating", null);
                            
                            if (UtilValidate.isNotEmpty(merchantName)) {
                                wishAccountStatistic.set("merchantName", merchantName);
                            }
                            if (UtilValidate.isNotEmpty(merchantId)) {
                                wishAccountStatistic.set("merchantId", merchantId);
                            }
                            if (UtilValidate.isNotEmpty(merchantRatingCount)) {
                                wishAccountStatistic.set("merchantRatingCount", Long.valueOf(merchantRatingCount));
                            }
                            if (UtilValidate.isNotEmpty(merchantRating)) {
                                wishAccountStatistic.set("merchantRating", Double.valueOf(merchantRating));
                            }
                        }   //loop variationsElementsElemIter == END
                        delegator.createOrStore(wishAccountStatistic);
                    }   //if responseXml not empty == END
                }   //if wishCrawlPageSingle success == END
            }   //if wishListingVariationList is not empty == END
        }   //main try == END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }
        /*catch (GenericServiceException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }*/
        catch (SAXException e) {
            Debug.logError("Yasin: getAccountStatistic SAXException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (ParserConfigurationException e) {
            Debug.logError("Yasin: getAccountStatistic ParserConfigurationException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (IOException e) {
            Debug.logError("Yasin: getAccountStatistic IOException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (Exception e) {
            e.printStackTrace();
            Debug.logError("Yasin: getAccountStatistic Exception Error for TRY CATCH: " + e.getMessage(), module);
        }
        return result;
    }   //getAccountStatistic
    
    public static Map<String, Object> crawlPageSingle (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String wishId = (String) context.get("wishId");
        
        if (UtilValidate.isNotEmpty(wishId)) {
            wishId = wishId.trim();
        }
        
        try {   //main try == START
            long requestHeaderCount = delegator.findCountByCondition("WishHtmlRequestHeader", null, null, null);
            int randomWishHtmlRequestHeader = randomWithRange(1, new Long(requestHeaderCount).intValue());
            GenericValue wishHtmlRequestHeader = delegator.findOne("WishHtmlRequestHeader", UtilMisc.toMap("sequence", Long.valueOf(randomWishHtmlRequestHeader)), false);
            
            String userAgent = wishHtmlRequestHeader.getString("userAgent");
            String bsid = null;
            String xsrf = null;
            String cookieExpires = null;
            boolean newRequestHeader = false;
            
            if (UtilValidate.isEmpty(wishHtmlRequestHeader.getString("bsid"))) { //if bsid empty == START
                URL obj = new URL("http://www.wish.com");
                URLConnection conn = obj.openConnection();
                Map<String, List<String>> initialWishCon = conn.getHeaderFields();
                String cookieOriginal = initialWishCon.get("Set-Cookie").toString();
                String[] cookieArray = cookieOriginal.split(";");
                for (int i = 0; i < cookieArray.length; i++) {  //loop cookieArray == START
                    String cookie = cookieArray[i];
                    if (UtilValidate.isNotEmpty(cookie)) {  //if cookie is not empty == START
                        if (cookie.matches(".*bsid.*")) {
                            int bsidStart = cookie.indexOf("bsid=") + 5;
                            bsid = cookie.substring(bsidStart).trim();
                        }
                        if (cookie.matches(".*_xsrf.*")) {
                            int xsrfStart = cookie.indexOf("_xsrf=") + 6;
                            xsrf = cookie.substring(xsrfStart).trim();
                        }
                        if (cookie.matches(".*expires.*")) {
                            int expiresStart = cookie.indexOf("expires=") + 8;
                            cookieExpires = cookie.substring(expiresStart).trim();
                        }
                    }   //if cookie is not empty == END
                }   //loop cookieArray == END
                wishHtmlRequestHeader.set("bsid", bsid);
                wishHtmlRequestHeader.set("xsrf", xsrf);
                wishHtmlRequestHeader.set("cookieExpires", cookieExpires);
                delegator.store(wishHtmlRequestHeader);
                newRequestHeader = true;
            }   //if bsid empty == END
            
            if (UtilValidate.isEmpty(bsid)) {
                bsid = wishHtmlRequestHeader.getString("bsid");
            }
            
            if (UtilValidate.isEmpty(xsrf)) {
                xsrf = wishHtmlRequestHeader.getString("xsrf");
            }
            
            if (!newRequestHeader) { //if newRequestHeader is TRUE == START
                URL obj = new URL("http://www.wish.com");
                URLConnection conn = obj.openConnection();
                
                Map<String, List<String>> initialWishCon = conn.getHeaderFields();
                
                String cookieOriginal = initialWishCon.get("Set-Cookie").toString();
                String[] cookieArray = cookieOriginal.split(";");
                for (int i = 0; i < cookieArray.length; i++) {  //loop cookieArray == START
                    String cookie = cookieArray[i];
                    if (UtilValidate.isNotEmpty(cookie)) {  //if cookie is not empty == START
                        if (cookie.matches(".*bsid.*")) {
                            int bsidStart = cookie.indexOf("bsid=") + 5;
                            bsid = cookie.substring(bsidStart).trim();
                        }
                        if (cookie.matches(".*_xsrf.*")) {
                            int xsrfStart = cookie.indexOf("_xsrf=") + 6;
                            xsrf = cookie.substring(xsrfStart).trim();
                        }
                        if (cookie.matches(".*expires.*")) {
                            int expiresStart = cookie.indexOf("expires=") + 8;
                            cookieExpires = cookie.substring(expiresStart).trim();
                        }
                    }   //if cookie is not empty == END
                }   //loop cookieArray == END
                wishHtmlRequestHeader.set("bsid", bsid);
                wishHtmlRequestHeader.set("xsrf", xsrf);
                wishHtmlRequestHeader.set("cookieExpires", cookieExpires);
                delegator.store(wishHtmlRequestHeader);
            }   //if newRequestHeader is TRUE == END
            
            String requestCookie = "bsid=" + bsid + "; _xsrf=" + xsrf;
            String postItemsUrl = "https://www.wish.com/c/" + wishId;
            String response = null;
            
            Map<String, String> requestPropertyMap = FastMap.<String, String>newInstance();
            requestPropertyMap.put("Host", "www.wish.com");
            requestPropertyMap.put("User-Agent", userAgent);
            requestPropertyMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            requestPropertyMap.put("Accept-Language", "en-US,en;q=0.5");
            requestPropertyMap.put("Accept-Encoding", "gzip, deflate, br");
            requestPropertyMap.put("DNT", "1");
            requestPropertyMap.put("Cookie", requestCookie);
            requestPropertyMap.put("Connection", "keep-alive");
            requestPropertyMap.put("Upgrade-Insecure-Requests", "1");
            
            URL url = new URL(postItemsUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(60*1000);
            connection.connect();
            
            int responseCode = connection.getResponseCode();
            InputStream inputStream = null;
            
            
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                response = inputStreamToString(inputStream);
            } else {
                inputStream = connection.getErrorStream();
                response = inputStreamToString(inputStream);
            }
            
            int jsonStart = response.indexOf("pageParams['mainContestObj'] =");
            int jsonEnd = response.indexOf("pageParams", jsonStart + 1);
            
            String jsonCode = response.substring(jsonStart + 30, jsonEnd - 2);
            
            JSONObject json = new JSONObject(jsonCode);
            String xml = XML.toString(json);
            
            StringBuffer responseXmlBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><CrawlResponse xmlns=\"urn:ebay:apis:eBLBaseComponents\">");
            responseXmlBuffer.append(xml);
            responseXmlBuffer.append("</CrawlResponse>");
            String responseXml = responseXmlBuffer.toString();
            
            int extraPhotoStart = responseXml.indexOf("<extra_photo_urls>");
            int extraPhotoEnd = responseXml.indexOf("</extra_photo_urls>", extraPhotoStart);
            StringBuffer responseBuffer = new StringBuffer(responseXml);
            responseBuffer.replace(extraPhotoStart, extraPhotoEnd + 19, "");
            responseXml = responseBuffer.toString();
            
            int extraPhotoDetailsStart = responseXml.indexOf("<extra_photo_details>");
            int extraPhotoDetailsEnd = responseXml.indexOf("</extra_photo_details>", extraPhotoDetailsStart);
            StringBuffer responseBuffer2 = new StringBuffer(responseXml);
            responseBuffer2.replace(extraPhotoDetailsStart, extraPhotoDetailsEnd + 22, "");
            responseXml = responseBuffer2.toString();
            
            int sizingChartStart = responseXml.indexOf("<sizing_chart_data>");
            int sizingChartEnd = responseXml.indexOf("</sizing_chart_data>", sizingChartStart);
            StringBuffer responseBuffer3 = new StringBuffer(responseXml);
            responseBuffer3.replace(sizingChartStart, sizingChartEnd + 10, "");
            responseXml = responseBuffer3.toString();
            
            //Debug.logError(xml, module);
            result.put("xml", responseXml);
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
            Debug.logError("Yasin: crawlPageSingle Exception Error for TRY CATCH: " + e.getMessage(), module);
        }
        return result;
    }   //crawlPageSingle
    
    public static Map<String, Object> crawlPageSingleMap (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String, Object> xmlMapResult = FastMap.<String, Object>newInstance();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String wishId = (String) context.get("wishId");
        
        if (UtilValidate.isNotEmpty(wishId)) {
            wishId = wishId.trim();
        }
        
        try {   //main try == START
            long requestHeaderCount = delegator.findCountByCondition("WishHtmlRequestHeader", null, null, null);
            int randomWishHtmlRequestHeader = randomWithRange(1, new Long(requestHeaderCount).intValue());
            GenericValue wishHtmlRequestHeader = delegator.findOne("WishHtmlRequestHeader", UtilMisc.toMap("sequence", Long.valueOf(randomWishHtmlRequestHeader)), false);
            
            String userAgent = wishHtmlRequestHeader.getString("userAgent");
            String bsid = null;
            String xsrf = null;
            String cookieExpires = null;
            boolean newRequestHeader = false;
            
            if (UtilValidate.isEmpty(wishHtmlRequestHeader.getString("bsid"))) { //if bsid empty == START
                URL obj = new URL("http://www.wish.com");
                URLConnection conn = obj.openConnection();
                Map<String, List<String>> initialWishCon = conn.getHeaderFields();
                String cookieOriginal = initialWishCon.get("Set-Cookie").toString();
                String[] cookieArray = cookieOriginal.split(";");
                for (int i = 0; i < cookieArray.length; i++) {  //loop cookieArray == START
                    String cookie = cookieArray[i];
                    if (UtilValidate.isNotEmpty(cookie)) {  //if cookie is not empty == START
                        if (cookie.matches(".*bsid.*")) {
                            int bsidStart = cookie.indexOf("bsid=") + 5;
                            bsid = cookie.substring(bsidStart).trim();
                        }
                        if (cookie.matches(".*_xsrf.*")) {
                            int xsrfStart = cookie.indexOf("_xsrf=") + 6;
                            xsrf = cookie.substring(xsrfStart).trim();
                        }
                        if (cookie.matches(".*expires.*")) {
                            int expiresStart = cookie.indexOf("expires=") + 8;
                            cookieExpires = cookie.substring(expiresStart).trim();
                        }
                    }   //if cookie is not empty == END
                }   //loop cookieArray == END
                wishHtmlRequestHeader.set("bsid", bsid);
                wishHtmlRequestHeader.set("xsrf", xsrf);
                wishHtmlRequestHeader.set("cookieExpires", cookieExpires);
                delegator.store(wishHtmlRequestHeader);
                newRequestHeader = true;
            }   //if bsid empty == END
            
            if (UtilValidate.isEmpty(bsid)) {
                bsid = wishHtmlRequestHeader.getString("bsid");
            }
            
            if (UtilValidate.isEmpty(xsrf)) {
                xsrf = wishHtmlRequestHeader.getString("xsrf");
            }
            
            if (!newRequestHeader) { //if newRequestHeader is TRUE == START
                URL obj = new URL("http://www.wish.com");
                URLConnection conn = obj.openConnection();
                
                Map<String, List<String>> initialWishCon = conn.getHeaderFields();
                
                String cookieOriginal = initialWishCon.get("Set-Cookie").toString();
                String[] cookieArray = cookieOriginal.split(";");
                for (int i = 0; i < cookieArray.length; i++) {  //loop cookieArray == START
                    String cookie = cookieArray[i];
                    if (UtilValidate.isNotEmpty(cookie)) {  //if cookie is not empty == START
                        if (cookie.matches(".*bsid.*")) {
                            int bsidStart = cookie.indexOf("bsid=") + 5;
                            bsid = cookie.substring(bsidStart).trim();
                        }
                        if (cookie.matches(".*_xsrf.*")) {
                            int xsrfStart = cookie.indexOf("_xsrf=") + 6;
                            xsrf = cookie.substring(xsrfStart).trim();
                        }
                        if (cookie.matches(".*expires.*")) {
                            int expiresStart = cookie.indexOf("expires=") + 8;
                            cookieExpires = cookie.substring(expiresStart).trim();
                        }
                    }   //if cookie is not empty == END
                }   //loop cookieArray == END
                wishHtmlRequestHeader.set("bsid", bsid);
                wishHtmlRequestHeader.set("xsrf", xsrf);
                wishHtmlRequestHeader.set("cookieExpires", cookieExpires);
                delegator.store(wishHtmlRequestHeader);
            }   //if newRequestHeader is TRUE == END
            
            String requestCookie = "bsid=" + bsid + "; _xsrf=" + xsrf;
            String postItemsUrl = "https://www.wish.com/c/" + wishId;
            String response = null;
            
            Map<String, String> requestPropertyMap = FastMap.<String, String>newInstance();
            requestPropertyMap.put("Host", "www.wish.com");
            requestPropertyMap.put("User-Agent", userAgent);
            requestPropertyMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            requestPropertyMap.put("Accept-Language", "en-US,en;q=0.5");
            requestPropertyMap.put("Accept-Encoding", "gzip, deflate, br");
            requestPropertyMap.put("DNT", "1");
            requestPropertyMap.put("Cookie", requestCookie);
            requestPropertyMap.put("Connection", "keep-alive");
            requestPropertyMap.put("Upgrade-Insecure-Requests", "1");
            
            URL url = new URL(postItemsUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(60*1000);
            connection.connect();
            
            int responseCode = connection.getResponseCode();
            InputStream inputStream = null;
            
            
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                response = inputStreamToString(inputStream);
            } else {
                inputStream = connection.getErrorStream();
                response = inputStreamToString(inputStream);
            }
            
            int jsonStart = response.indexOf("pageParams['mainContestObj'] =");
            int jsonEnd = response.indexOf("pageParams", jsonStart + 1);
            
            String jsonCode = response.substring(jsonStart + 30, jsonEnd - 2);
            
            JSONObject json = new JSONObject(jsonCode);
            String xml = XML.toString(json);
            
            StringBuffer responseXmlBuffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><CrawlResponse xmlns=\"urn:ebay:apis:eBLBaseComponents\">");
            responseXmlBuffer.append(xml);
            responseXmlBuffer.append("</CrawlResponse>");
            String responseXml = responseXmlBuffer.toString();
            
            int extraPhotoStart = responseXml.indexOf("<extra_photo_urls>");
            int extraPhotoEnd = responseXml.indexOf("</extra_photo_urls>", extraPhotoStart);
            StringBuffer responseBuffer = new StringBuffer(responseXml);
            responseBuffer.replace(extraPhotoStart, extraPhotoEnd + 19, "");
            responseXml = responseBuffer.toString();
            
            int extraPhotoDetailsStart = responseXml.indexOf("<extra_photo_details>");
            int extraPhotoDetailsEnd = responseXml.indexOf("</extra_photo_details>", extraPhotoDetailsStart);
            StringBuffer responseBuffer2 = new StringBuffer(responseXml);
            responseBuffer2.replace(extraPhotoDetailsStart, extraPhotoDetailsEnd + 22, "");
            responseXml = responseBuffer2.toString();
            
            int sizingChartStart = responseXml.indexOf("<sizing_chart_data>");
            int sizingChartEnd = responseXml.indexOf("</sizing_chart_data>", sizingChartStart);
            StringBuffer responseBuffer3 = new StringBuffer(responseXml);
            responseBuffer3.replace(sizingChartStart, sizingChartEnd + 10, "");
            responseXml = responseBuffer3.toString();
            
            Debug.logError(responseXml, module);
            
            Document rootDoc = UtilXml.readXmlDocument(responseXml, true);
            Element rootElement = rootDoc.getDocumentElement();
            
            String activeSweep = UtilXml.childElementValue(rootElement, "active_sweep", "FALSE");
            String aspectRatio = UtilXml.childElementValue(rootElement, "aspect_ratio", "0.0");
            String brand = UtilXml.childElementValue(rootElement, "brand", null);
            String canGift = UtilXml.childElementValue(rootElement, "can_gift", "FALSE");
            String canPromo = UtilXml.childElementValue(rootElement, "can_promo", "FALSE");
            String commentCount = UtilXml.childElementValue(rootElement, "comment_count", "0");
            String contestPagePicture = UtilXml.childElementValue(rootElement, "contest_page_picture", null);
            String firstRatingImageIndex = UtilXml.childElementValue(rootElement, "first_rating_image_index", "0");
            String fromTrustedStore = UtilXml.childElementValue(rootElement, "from_trusted_store", "FALSE");
            String gender = UtilXml.childElementValue(rootElement, "gender", null);
            String generationTime = UtilXml.childElementValue(rootElement, "generation_time", null);
            String hasReward = UtilXml.childElementValue(rootElement, "has_reward", "FALSE");
            String isActive = UtilXml.childElementValue(rootElement, "is_active", "FALSE");
            String isAdminUser = UtilXml.childElementValue(rootElement, "is_admin_user", "FALSE");
            String isClean = UtilXml.childElementValue(rootElement, "is_clean", "FALSE");
            String isConcept = UtilXml.childElementValue(rootElement, "is_concept", "FALSE");
            String isContentReviewer = UtilXml.childElementValue(rootElement, "is_content_reviewer", "FALSE");
            String isDeleted = UtilXml.childElementValue(rootElement, "is_deleted", "FALSE");
            String isDirty = UtilXml.childElementValue(rootElement, "is_dirty", "FALSE");
            String isExpired = UtilXml.childElementValue(rootElement, "is_expired", "FALSE");
            String isVerified = UtilXml.childElementValue(rootElement, "is_verified", "FALSE");
            String name = UtilXml.childElementValue(rootElement, "name", null);
            String numBought = UtilXml.childElementValue(rootElement, "num_bought", "0");
            String numEntered = UtilXml.childElementValue(rootElement, "num_entered", "0");
            String numWishes = UtilXml.childElementValue(rootElement, "num_wishes", "0");
            String numWon = UtilXml.childElementValue(rootElement, "num_won", "0");
            String numContestPhotos = UtilXml.childElementValue(rootElement, "num_contest_photos", "0");
            String numExtraPhotos = UtilXml.childElementValue(rootElement, "num_extra_photos", "0");
            
            String requiresReview = UtilXml.childElementValue(rootElement, "requires_review", "FALSE");
            String sourceCountry = UtilXml.childElementValue(rootElement, "source_country", null);
            String uploadSource = UtilXml.childElementValue(rootElement, "upload_source", null);
            String userInActiveSweep = UtilXml.childElementValue(rootElement, "user_in_active_sweep", "FALSE");
            String value = UtilXml.childElementValue(rootElement, "value", null);
            
            Element commerceProductInfoElement = UtilXml.firstChildElement(rootElement, "commerce_product_info");
            String fbwActive = UtilXml.childElementValue(commerceProductInfoElement, "fbw_active", "0");
            String fbwPending = UtilXml.childElementValue(commerceProductInfoElement, "fbw_pending", "0");
            String isActiveFbwInUs = UtilXml.childElementValue(commerceProductInfoElement, "is_active_fbw_in_us", "FALSE");
            String isFulfillByWish = UtilXml.childElementValue(commerceProductInfoElement, "is_fulfill_by_wish", "FALSE");
            String removed = UtilXml.childElementValue(commerceProductInfoElement, "removed", "FALSE");
            
            Double minOriginalPrice = 10000.0;
            Double maxOriginalPrice = 0.0;
            Double minOriginalShipping = 10000.0;
            Double maxOriginalShipping = 0.0;
            
            List<? extends Element> variationsElements = UtilXml.childElementList(commerceProductInfoElement, "variations");
            Iterator<? extends Element> variationsElementsElemIter = variationsElements.iterator();
            while (variationsElementsElemIter.hasNext()) {  //loop variationsElementsElemIter == START
                Element variationsElement = variationsElementsElemIter.next();
                String merchantName = UtilXml.childElementValue(variationsElement, "merchant_name", null);
                String merchantRatingClass = UtilXml.childElementValue(variationsElement, "merchant_rating_class", null);
                String merchantRatingCount = UtilXml.childElementValue(variationsElement, "merchant_rating_count", null);
                String merchantRating = UtilXml.childElementValue(variationsElement, "merchant_rating", null);
                String shipsFrom = UtilXml.childElementValue(variationsElement, "ships_from", null);
                
                Double originalPrice = Double.valueOf(UtilXml.childElementValue(variationsElement, "original_price", "0"));
                Double originalShipping = Double.valueOf(UtilXml.childElementValue(variationsElement, "original_shipping", "0"));
                
                if (minOriginalPrice >= originalPrice) {
                    minOriginalPrice = originalPrice;
                }
                
                if (maxOriginalPrice <= originalPrice) {
                    maxOriginalPrice = originalPrice;
                }
                
                if (minOriginalShipping >= originalShipping) {
                    minOriginalShipping = originalShipping;
                }
                
                if (maxOriginalShipping <= originalShipping) {
                    maxOriginalShipping = originalShipping;
                }
                
                if (UtilValidate.isNotEmpty(merchantName)) {
                    xmlMapResult.put("merchantName", merchantName);
                }
                if (UtilValidate.isNotEmpty(merchantRatingClass)) {
                    xmlMapResult.put("merchantRatingClass", merchantRatingClass);
                }
                if (UtilValidate.isNotEmpty(merchantRatingCount)) {
                    xmlMapResult.put("merchantRatingCount", Double.valueOf(merchantRatingCount));
                }
                if (UtilValidate.isNotEmpty(merchantRating)) {
                    xmlMapResult.put("merchantRating", Double.valueOf(merchantRating));
                }
                if (UtilValidate.isNotEmpty(shipsFrom)) {
                    xmlMapResult.put("shipsFrom", shipsFrom);
                }
            }   //loop variationsElementsElemIter == END
            
            Element productRatingElement = UtilXml.firstChildElement(rootElement, "product_rating");
            String productRating = UtilXml.childElementValue(productRatingElement, "rating", "0.0");
            String productRatingClass = UtilXml.childElementValue(productRatingElement, "rating_class", null);
            String productRatingCount = UtilXml.childElementValue(productRatingElement, "rating_count", "0");
            
            List<String> tagsList= new ArrayList<String>();
            List<? extends Element> tagsElementList = UtilXml.childElementList(rootElement, "tags");
            Iterator<? extends Element> tagsListElemIter = tagsElementList.iterator();
            while (tagsListElemIter.hasNext()) {    //loop tags -- START
                Element tagElement = tagsListElemIter.next();
                String tagName = UtilXml.childElementValue(tagElement, "name", null);
                tagsList.add(tagName);
            }   //loop tags -- START
            
            List<String> merchantTagsList= new ArrayList<String>();
            List<? extends Element> merchantTagsElementList = UtilXml.childElementList(rootElement, "merchant_tags");
            Iterator<? extends Element> merchantTagsListElemIter = merchantTagsElementList.iterator();
            while (merchantTagsListElemIter.hasNext()) {    //loop tags -- START
                Element tagElement = merchantTagsListElemIter.next();
                String tagName = UtilXml.childElementValue(tagElement, "name", null);
                merchantTagsList.add(tagName);
            }   //loop tags -- START
            
            xmlMapResult.put("wishId", wishId);
            xmlMapResult.put("tagsList", tagsList);
            xmlMapResult.put("merchantTagsList", merchantTagsList);
            xmlMapResult.put("activeSweep", activeSweep.toUpperCase());
            xmlMapResult.put("aspectRatio", Double.valueOf(aspectRatio));
            xmlMapResult.put("brand", brand);
            xmlMapResult.put("canGift", canGift.toUpperCase());
            xmlMapResult.put("canPromo", canPromo.toUpperCase());
            xmlMapResult.put("commentCount", Long.valueOf(commentCount));
            xmlMapResult.put("contestPagePicture", contestPagePicture);
            xmlMapResult.put("fbwActive", Long.valueOf(fbwActive));
            xmlMapResult.put("fbwPending", Long.valueOf(fbwPending));
            xmlMapResult.put("firstRatingImageIndex", Long.valueOf(firstRatingImageIndex));
            xmlMapResult.put("fromTrustedStore", fromTrustedStore.toUpperCase());
            xmlMapResult.put("gender", gender);
            xmlMapResult.put("generationTime", generationTime);
            xmlMapResult.put("hasReward", hasReward.toUpperCase());
            xmlMapResult.put("isActive", isActive.toUpperCase());
            xmlMapResult.put("isActiveFbwInUs", isActiveFbwInUs.toUpperCase());
            xmlMapResult.put("isAdminUser", isAdminUser.toUpperCase());
            xmlMapResult.put("isClean", isClean.toUpperCase());
            xmlMapResult.put("isConcept", isConcept.toUpperCase());
            xmlMapResult.put("isContentReviewer", isContentReviewer.toUpperCase());
            xmlMapResult.put("isDeleted", isDeleted.toUpperCase());
            xmlMapResult.put("isDirty", isDirty.toUpperCase());
            xmlMapResult.put("isExpired", isExpired.toUpperCase());
            xmlMapResult.put("isFulfillByWish", isFulfillByWish.toUpperCase());
            xmlMapResult.put("isVerified", isVerified.toUpperCase());
            xmlMapResult.put("name", name);
            xmlMapResult.put("numBought", Long.valueOf(numBought));
            xmlMapResult.put("numEntered", Long.valueOf(numEntered));
            xmlMapResult.put("numWishes", Long.valueOf(numWishes));
            xmlMapResult.put("numWon", Long.valueOf(numWon));
            xmlMapResult.put("numContestPhotos", Long.valueOf(numContestPhotos));
            xmlMapResult.put("numExtraPhotos", Long.valueOf(numExtraPhotos));
            xmlMapResult.put("productRating", Double.valueOf(productRating));
            xmlMapResult.put("productRatingClass", productRatingClass);
            xmlMapResult.put("productRatingCount", Double.valueOf(productRatingCount));
            xmlMapResult.put("removed", removed.toUpperCase());
            xmlMapResult.put("requiresReview", requiresReview.toUpperCase());
            xmlMapResult.put("sourceCountry", sourceCountry);
            xmlMapResult.put("uploadSource", uploadSource);
            xmlMapResult.put("userInActiveSweep", userInActiveSweep.toUpperCase());
            xmlMapResult.put("value", value);
            xmlMapResult.put("wishId", wishId);
            xmlMapResult.put("minOriginalPrice", minOriginalPrice);
            xmlMapResult.put("maxOriginalPrice", maxOriginalPrice);
            xmlMapResult.put("minOriginalShipping", minOriginalShipping);
            xmlMapResult.put("maxOriginalShipping", maxOriginalShipping);
            
            //Debug.logError(xml, module);
            result.put("xmlMapResult", xmlMapResult);
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
            Debug.logError("Yasin: crawlPageSingle Exception Error for TRY CATCH: " + e.getMessage(), module);
        }
        return result;
    }   //crawlPageSingleMap

    
    public static Map<String, Object> updateBestSellingSingle (DispatchContext dctx, Map context)
    throws GenericEntityException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String wishId = (String) context.get("wishId");
        String url = "https://china-merchant.wish.com/api/v2/product";
        
        if (UtilValidate.isNotEmpty(productStoreId)) {
            productStoreId = productStoreId.trim();
        }
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(now.getTime());
        java.sql.Date dateSql = new java.sql.Date(now.getTime().getTime());
        DecimalFormat df = new DecimalFormat("00000");
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        
        try {   //main try -- START
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            String productStoreGroup = productStore.getString("primaryStoreGroupId");
            if (!productStoreGroup.equals("WISH")) {
                return ServiceUtil.returnSuccess("productStoreId " + productStoreId + " is not a wish account");
            }
            List<GenericValue> productStoreRoleList = delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"), "roleTypeId", "WISH_ACCOUNT"), null, false);
            if (UtilValidate.isEmpty(productStoreRoleList)) {   //if productStoreRoleList is empty -- START
                Debug.logError("ProductStoreId " + productStoreId + " does not have Wish account role", module);
                return ServiceUtil.returnError("ProductStoreId " + productStoreId + " does not have Wish account role");
            }   //if productStoreRoleList is empty -- END
            GenericValue productStoreRole = EntityUtil.getFirst(productStoreRoleList);
            GenericValue wishAccountPartyGroup = productStoreRole.getRelatedOne("PartyGroup", false);
            GenericValue wishAccountParty = wishAccountPartyGroup.getRelatedOne("Party", false);
            GenericValue partyIdentification = delegator.findOne("PartyIdentification", UtilMisc.toMap("partyId",wishAccountParty.getString("partyId"), "partyIdentificationTypeId", "WISH_ACCESS_CODE"), false);
            String apiKey = URLEncoder.encode(partyIdentification.getString("idValue"), "UTF-8");
            
            String requestUrl = url + "?format=xml&access_token=" + apiKey + "&id=" + wishId;
            //Debug.logError(requestUrl, module);
            String responseXML = sendHttpRequest(requestUrl);
            //Debug.logError(responseXML, module);
            
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            String ack = UtilXml.childElementValue(elemResponse, "Code", null);
            if (ack.equals("0")) {    //if ack success -- START
                //writing data into database -- START
                Element dataElement = UtilXml.firstChildElement(elemResponse, "Data");
                List<? extends Element> productElements = UtilXml.childElementList(dataElement, "Product");
                Iterator<? extends Element> productElementsElemIter = productElements.iterator();
                while (productElementsElemIter.hasNext()) { //loop Product -- START
                    GenericValue wishListing = delegator.findOne("WishBestSellingHeader", UtilMisc.toMap("productStoreId", productStoreId, "wishId", wishId, "date", new java.sql.Date(nowDate.getTime())), false);
                    if (UtilValidate.isEmpty(wishListing)) {
                        wishListing = delegator.makeValue("WishBestSellingHeader", UtilMisc.toMap("productStoreId", productStoreId, "wishId", wishId, "date", new java.sql.Date(nowDate.getTime())));
                    }
                    
                    Map wishCrawlPageSingle = dispatcher.runSync("wishCrawlPageSingle", UtilMisc.toMap("wishId", wishId, "userLogin", userLogin));
                    if (ServiceUtil.isSuccess(wishCrawlPageSingle)) {   //if wishCrawlPageSingle successfull == START
                        //Debug.logError(wishCrawlPageSingle.get("xml").toString(), module);
                        Document rootDoc = UtilXml.readXmlDocument(wishCrawlPageSingle.get("xml").toString(), true);
                        Element rootElement = rootDoc.getDocumentElement();
                        
                        String activeSweep = UtilXml.childElementValue(rootElement, "active_sweep", "FALSE");
                        String aspectRatio = UtilXml.childElementValue(rootElement, "aspect_ratio", "0.0");
                        String brand = UtilXml.childElementValue(rootElement, "brand", null);
                        String canGift = UtilXml.childElementValue(rootElement, "can_gift", "FALSE");
                        String canPromo = UtilXml.childElementValue(rootElement, "can_promo", "FALSE");
                        String commentCount = UtilXml.childElementValue(rootElement, "comment_count", "0");
                        String firstRatingImageIndex = UtilXml.childElementValue(rootElement, "first_rating_image_index", "0");
                        String fromTrustedStore = UtilXml.childElementValue(rootElement, "from_trusted_store", "FALSE");
                        String gender = UtilXml.childElementValue(rootElement, "gender", null);
                        String generationTime = UtilXml.childElementValue(rootElement, "generation_time", null);
                        String hasReward = UtilXml.childElementValue(rootElement, "has_reward", "FALSE");
                        String isActive = UtilXml.childElementValue(rootElement, "is_active", "FALSE");
                        String isAdminUser = UtilXml.childElementValue(rootElement, "is_admin_user", "FALSE");
                        String isClean = UtilXml.childElementValue(rootElement, "is_clean", "FALSE");
                        String isConcept = UtilXml.childElementValue(rootElement, "is_concept", "FALSE");
                        String isContentReviewer = UtilXml.childElementValue(rootElement, "is_content_reviewer", "FALSE");
                        String isDeleted = UtilXml.childElementValue(rootElement, "is_deleted", "FALSE");
                        String isDirty = UtilXml.childElementValue(rootElement, "is_dirty", "FALSE");
                        String isExpired = UtilXml.childElementValue(rootElement, "is_expired", "FALSE");
                        String isVerified = UtilXml.childElementValue(rootElement, "is_verified", "FALSE");
                        String numBought = UtilXml.childElementValue(rootElement, "num_bought", "0");
                        String numEntered = UtilXml.childElementValue(rootElement, "num_entered", "0");
                        String numWishes = UtilXml.childElementValue(rootElement, "num_wishes", "0");
                        String numWon = UtilXml.childElementValue(rootElement, "num_won", "0");
                        String numContestPhotos = UtilXml.childElementValue(rootElement, "num_contest_photos", "0");
                        String numExtraPhotos = UtilXml.childElementValue(rootElement, "num_extra_photos", "0");
                        
                        String requiresReview = UtilXml.childElementValue(rootElement, "requires_review", "FALSE");
                        String sourceCountry = UtilXml.childElementValue(rootElement, "source_country", null);
                        String uploadSource = UtilXml.childElementValue(rootElement, "upload_source", null);
                        String userInActiveSweep = UtilXml.childElementValue(rootElement, "user_in_active_sweep", "FALSE");
                        String value = UtilXml.childElementValue(rootElement, "value", null);
                        
                        Element commerceProductInfoElement = UtilXml.firstChildElement(rootElement, "commerce_product_info");
                        String fbwActive = UtilXml.childElementValue(commerceProductInfoElement, "fbw_active", "0");
                        String fbwPending = UtilXml.childElementValue(commerceProductInfoElement, "fbw_pending", "0");
                        String isActiveFbwInUs = UtilXml.childElementValue(commerceProductInfoElement, "is_active_fbw_in_us", "FALSE");
                        String isFulfillByWish = UtilXml.childElementValue(commerceProductInfoElement, "is_fulfill_by_wish", "FALSE");
                        String removed = UtilXml.childElementValue(commerceProductInfoElement, "removed", "FALSE");
                        
                        Double minOriginalPrice = 10000.0;
                        Double maxOriginalPrice = 0.0;
                        Double minOriginalShipping = 10000.0;
                        Double maxOriginalShipping = 0.0;
                        
                        List<? extends Element> variationsElements = UtilXml.childElementList(commerceProductInfoElement, "variations");
                        Iterator<? extends Element> variationsElementsElemIter = variationsElements.iterator();
                        while (variationsElementsElemIter.hasNext()) {  //loop variationsElementsElemIter == START
                            Element variationsElement = variationsElementsElemIter.next();
                            String merchantRatingClass = UtilXml.childElementValue(variationsElement, "merchant_rating_class", null);
                            String merchantRatingCount = UtilXml.childElementValue(variationsElement, "merchant_rating_count", null);
                            String merchantRating = UtilXml.childElementValue(variationsElement, "merchant_rating", null);
                            String shipsFrom = UtilXml.childElementValue(variationsElement, "ships_from", null);
                            
                            Double originalPrice = Double.valueOf(UtilXml.childElementValue(variationsElement, "original_price", "0"));
                            Double originalShipping = Double.valueOf(UtilXml.childElementValue(variationsElement, "original_shipping", "0"));
                            
                            if (minOriginalPrice >= originalPrice) {
                                minOriginalPrice = originalPrice;
                            }
                            
                            if (maxOriginalPrice <= originalPrice) {
                                maxOriginalPrice = originalPrice;
                            }
                            
                            if (minOriginalShipping >= originalShipping) {
                                minOriginalShipping = originalShipping;
                            }
                            
                            if (maxOriginalShipping <= originalShipping) {
                                maxOriginalShipping = originalShipping;
                            }
                            
                            if (UtilValidate.isNotEmpty(merchantRatingClass)) {
                                wishListing.set("merchantRatingClass", merchantRatingClass);
                            }
                            if (UtilValidate.isNotEmpty(merchantRatingCount)) {
                                wishListing.set("merchantRatingCount", Double.valueOf(merchantRatingCount));
                            }
                            if (UtilValidate.isNotEmpty(merchantRating)) {
                                wishListing.set("merchantRating", Double.valueOf(merchantRating));
                            }
                            if (UtilValidate.isNotEmpty(shipsFrom)) {
                                wishListing.set("shipsFrom", shipsFrom);
                            }
                        }   //loop variationsElementsElemIter == END

                        Element productRatingElement = UtilXml.firstChildElement(rootElement, "product_rating");
                        String productRating = UtilXml.childElementValue(productRatingElement, "rating", "0.0");
                        String productRatingClass = UtilXml.childElementValue(productRatingElement, "rating_class", null);
                        String productRatingCount = UtilXml.childElementValue(productRatingElement, "rating_count", "0");
                        
                        wishListing.set("activeSweep", activeSweep.toUpperCase());
                        wishListing.set("aspectRatio", Double.valueOf(aspectRatio));
                        wishListing.set("brand", brand);
                        wishListing.set("canGift", canGift.toUpperCase());
                        wishListing.set("canPromo", canPromo.toUpperCase());
                        wishListing.set("commentCount", Long.valueOf(commentCount));
                        wishListing.set("fbwActive", Long.valueOf(fbwActive));
                        wishListing.set("fbwPending", Long.valueOf(fbwPending));
                        wishListing.set("firstRatingImageIndex", Long.valueOf(firstRatingImageIndex));
                        wishListing.set("fromTrustedStore", fromTrustedStore.toUpperCase());
                        wishListing.set("gender", gender);
                        wishListing.set("generationTime", generationTime);
                        wishListing.set("hasReward", hasReward.toUpperCase());
                        wishListing.set("isActive", isActive.toUpperCase());
                        wishListing.set("isActiveFbwInUs", isActiveFbwInUs.toUpperCase());
                        wishListing.set("isAdminUser", isAdminUser.toUpperCase());
                        wishListing.set("isClean", isClean.toUpperCase());
                        wishListing.set("isConcept", isConcept.toUpperCase());
                        wishListing.set("isContentReviewer", isContentReviewer.toUpperCase());
                        wishListing.set("isDeleted", isDeleted.toUpperCase());
                        wishListing.set("isDirty", isDirty.toUpperCase());
                        wishListing.set("isExpired", isExpired.toUpperCase());
                        wishListing.set("isFulfillByWish", isFulfillByWish.toUpperCase());
                        wishListing.set("isVerified", isVerified.toUpperCase());
                        wishListing.set("numBought", Long.valueOf(numBought));
                        wishListing.set("numEntered", Long.valueOf(numEntered));
                        wishListing.set("numWishes", Long.valueOf(numWishes));
                        wishListing.set("numWon", Long.valueOf(numWon));
                        wishListing.set("numContestPhotos", Long.valueOf(numContestPhotos));
                        wishListing.set("numExtraPhotos", Long.valueOf(numExtraPhotos));
                        wishListing.set("productRating", Double.valueOf(productRating));
                        wishListing.set("productRatingClass", productRatingClass);
                        wishListing.set("productRatingCount", Double.valueOf(productRatingCount));
                        wishListing.set("removed", removed.toUpperCase());
                        wishListing.set("requiresReview", requiresReview.toUpperCase());
                        wishListing.set("sourceCountry", sourceCountry);
                        wishListing.set("uploadSource", uploadSource);
                        wishListing.set("userInActiveSweep", userInActiveSweep.toUpperCase());
                        wishListing.set("value", value);
                        
                        wishListing.set("minOriginalPrice", minOriginalPrice);
                        wishListing.set("maxOriginalPrice", maxOriginalPrice);
                        wishListing.set("minOriginalShipping", minOriginalShipping);
                        wishListing.set("maxOriginalShipping", maxOriginalShipping);
                    }   //if wishCrawlPageSingle successfull == END
                    
                    Element productElement = productElementsElemIter.next();
                    String mainImage = UtilXml.childElementValue(productElement, "main_image", null);
                    String isPromoted = UtilXml.childElementValue(productElement, "is_promoted", null);
                    String description = UtilXml.childElementValue(productElement, "description", null);
                    String reviewStatus = UtilXml.childElementValue(productElement, "review_status", null);
                    String upc = UtilXml.childElementValue(productElement, "upc", null);
                    String extraImages = UtilXml.childElementValue(productElement, "extra_images", null);
                    String numberSaves = UtilXml.childElementValue(productElement, "number_saves", null);
                    String numberSold = UtilXml.childElementValue(productElement, "number_sold", null);
                    String parentSku = UtilXml.childElementValue(productElement, "parent_sku", null);
                    String name = UtilXml.childElementValue(productElement, "name", null);
                    String originalImageUrl = UtilXml.childElementValue(productElement, "original_image_url", null);
                    String dateUploaded = UtilXml.childElementValue(productElement, "date_uploaded", null);
                    
                    wishListing.set("parentSku", parentSku);
                    wishListing.set("mainImage", mainImage);
                    wishListing.set("name", name);
                    wishListing.set("upc", upc);
                    wishListing.set("isPromoted", isPromoted.toUpperCase());
                    wishListing.set("description", description);
                    wishListing.set("reviewStatus", reviewStatus.toUpperCase());
                    wishListing.set("extraImages", extraImages);
                    wishListing.set("originalImageUrl", originalImageUrl);
                    if (UtilValidate.isEmpty(numberSaves)) {
                        numberSaves = "0";
                    }
                    if (UtilValidate.isEmpty(numberSold)) {
                        numberSold = "0";
                    }
                    wishListing.set("numberSaves", Long.valueOf(numberSaves));
                    wishListing.set("numberSold", Long.valueOf(numberSold));
                    wishListing.set("dateUploaded", new java.sql.Date(sdf.parse(dateUploaded).getTime()));
                    delegator.createOrStore(wishListing);
                    
                    delegator.removeByAnd("WishBestSellingTags", UtilMisc.toMap("wishId", wishId));
                    
                    Element tagsElement = UtilXml.firstChildElement(productElement, "tags");
                    List<? extends Element> tagsList = UtilXml.childElementList(tagsElement, "Tag");
                    Iterator<? extends Element> tagsListElemIter = tagsList.iterator();
                    int tagSeq = 0;
                    while (tagsListElemIter.hasNext()) {    //loop tags -- START
                        
                        tagSeq++;
                        String tagSeqId = df.format(tagSeq);
                        Element tagElement = tagsListElemIter.next();
                        String tagId = UtilXml.childElementValue(tagElement, "id", null);
                        String tagName = UtilXml.childElementValue(tagElement, "name", null);
                        GenericValue wishTag = delegator.findOne("WishBestSellingTags", UtilMisc.toMap("wishId", wishId, "productStoreId", productStoreId, "tagSeqId", tagSeqId, "tagType", "INPUT_TAG"), false);
                        if (UtilValidate.isEmpty(wishTag)) {
                            wishTag = delegator.makeValue("WishBestSellingTags", UtilMisc.toMap("wishId", wishId, "productStoreId", productStoreId, "tagSeqId", tagSeqId, "tagType", "INPUT_TAG"));
                        }
                        wishTag.set("tagId", tagId);
                        wishTag.set("tagName", tagName);
                        delegator.createOrStore(wishTag);
                    }   //loop tags -- START
                    
                    Element autoTagsElement = UtilXml.firstChildElement(productElement, "auto_tags");
                    if (UtilValidate.isNotEmpty(autoTagsElement)) { //if auto_tags not empty == START
                        List<? extends Element> autoTagsList = UtilXml.childElementList(autoTagsElement, "Tag");
                        Iterator<? extends Element> autoTagsListElemIter = autoTagsList.iterator();
                        int autoTagSeq = 0;
                        while (autoTagsListElemIter.hasNext()) {    //loop auto tags -- START
                            autoTagSeq++;
                            String autoTagSeqId = df.format(autoTagSeq);
                            Element tagElement = autoTagsListElemIter.next();
                            String tagId = UtilXml.childElementValue(tagElement, "id", null);
                            String tagName = UtilXml.childElementValue(tagElement, "name", null);
                            GenericValue wishTag = delegator.findOne("WishBestSellingTags", UtilMisc.toMap("wishId", wishId, "productStoreId", productStoreId, "tagSeqId", autoTagSeqId, "tagType", "AUTO_TAG"), false);
                            if (UtilValidate.isEmpty(wishTag)) {
                                wishTag = delegator.makeValue("WishBestSellingTags", UtilMisc.toMap("wishId", wishId, "productStoreId", productStoreId, "tagSeqId", autoTagSeqId, "tagType", "AUTO_TAG"));
                            }
                            wishTag.set("tagId", tagId);
                            wishTag.set("tagName", tagName);
                            delegator.createOrStore(wishTag);
                        }   //loop auto tags -- START
                    }   //if auto_tags not empty == END
                    
                    
                    //Variations -- START
                    Element variantsElement = UtilXml.firstChildElement(productElement, "variants");
                    List<? extends Element> variantList = UtilXml.childElementList(variantsElement, "Variant");
                    Iterator<? extends Element> variantListElemIter = variantList.iterator();
                    int varSeq = 0;
                    while (variantListElemIter.hasNext()) { //loop variants -- START
                        //Debug.logError("variation runs", module);
                        varSeq++;
                        String varSeqId = df.format(varSeq);
                        Element variantElement = variantListElemIter.next();
                        String sku = UtilXml.childElementValue(variantElement, "sku", null);
                        String productId = UtilXml.childElementValue(variantElement, "product_id", null);
                        String size = UtilXml.childElementValue(variantElement, "size", null);
                        String color = UtilXml.childElementValue(variantElement, "color", null);
                        String price = UtilXml.childElementValue(variantElement, "price", null);
                        String enabled = UtilXml.childElementValue(variantElement, "enabled", null);
                        String shipping = UtilXml.childElementValue(variantElement, "shipping", null);
                        String allImages = UtilXml.childElementValue(variantElement, "all_images", null);
                        String inventory = UtilXml.childElementValue(variantElement, "inventory", null);
                        String variationId = UtilXml.childElementValue(variantElement, "id", null);
                        String msrp = UtilXml.childElementValue(variantElement, "msrp", null);
                        String shippingTime = UtilXml.childElementValue(variantElement, "shipping_time", null);
                        
                        GenericValue wishListingVariation = delegator.findOne("WishBestSellingVariant", UtilMisc.toMap("wishId", wishId, "productStoreId", productStoreId, "varSeqId", varSeqId, "date", new java.sql.Date(nowDate.getTime())), false);
                        if (UtilValidate.isEmpty(wishListingVariation)) {
                            wishListingVariation = delegator.makeValue("WishBestSellingVariant", UtilMisc.toMap("wishId", wishId, "productStoreId", productStoreId, "varSeqId", varSeqId, "date", new java.sql.Date(nowDate.getTime())));
                        }
                        wishListingVariation.set("sku", sku);
                        wishListingVariation.set("normalizedSku", normalizeSkuWish(delegator,sku));
                        wishListingVariation.set("productStoreId", productStoreId);
                        wishListingVariation.set("productId", productId);
                        wishListingVariation.set("size", size);
                        wishListingVariation.set("color", color);
                        if (UtilValidate.isEmpty(price)) {
                            price = "0";
                        }
                        wishListingVariation.set("price", Double.valueOf(price));
                        wishListingVariation.set("enabled", enabled.toUpperCase());
                        if (UtilValidate.isEmpty(shipping)) {
                            shipping = "0";
                        }
                        wishListingVariation.set("shipping", Double.valueOf(shipping));
                        wishListingVariation.set("allImages", allImages);
                        if (UtilValidate.isEmpty(inventory)) {
                            inventory = "0";
                        }
                        wishListingVariation.set("inventory", Long.valueOf(inventory));
                        wishListingVariation.set("variationId", variationId);
                        if (UtilValidate.isEmpty(msrp)) {
                            msrp = "0";
                        }
                        wishListingVariation.set("msrp", Double.valueOf(msrp));
                        wishListingVariation.set("shippingTime", shippingTime);
                        delegator.createOrStore(wishListingVariation);
                    }   //loop variants -- END
                    //Variations -- END
                }   //loop Product -- END
                //writing data into database -- END
            }   //if ack success -- END
            else if (ack.equals("1016")) {  //refresh token == START
                Map refreshWishToken = dispatcher.runSync("wishGetRefreshCode", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
                if (ServiceUtil.isSuccess(refreshWishToken)) {
                    Map rerunThisFunction = dispatcher.runSync("wishUpdateBestSellingSingle", UtilMisc.toMap("productStoreId", productStoreId, "wishId", wishId, "userLogin", userLogin));
                }
            }   //refresh token == END
            else {  //if ack failure -- START
                String errorMessage = UtilXml.childElementValue(elemResponse, "Message", null);
                FileWriter f1 = new FileWriter("hot-deploy/gudao/webapp/gudao/wish/logError/updateBestSelling.log", true);
                f1.write(today + ": product Store ID: " + productStoreId + ", Codes : " + ack + ": " + errorMessage + "\n");
                f1.close();
            }   //if ack failure -- END
        }   //main try -- END
        catch (GenericEntityException e) {
            Debug.logError("Yasin: updateBestSellingSingle GenericEntityException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (SAXException e) {
            Debug.logError("Yasin: updateBestSellingSingle SAXException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (ParserConfigurationException e) {
            Debug.logError("Yasin: updateBestSellingSingle ParserConfigurationException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (IOException e) {
            Debug.logError("Yasin: updateBestSellingSingle IOException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (Exception e) {
            e.printStackTrace();
            Debug.logError("Yasin: updateBestSellingSingle Exception Error for TRY CATCH: " + e.getMessage(), module);
        }
        return result;
    }   //updateBestSellingSingle
    
    public static Map<String, Object> updateBestSellingAuto (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreIdInput = (String) context.get("productStoreId");
        
        if (UtilValidate.isNotEmpty(productStoreIdInput)) {
            productStoreIdInput = productStoreIdInput.trim();
        }

        try {   //main try == START
            EntityCondition cond = null;
            List<EntityCondition> conditions = FastList.newInstance();
            conditions.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
            if (UtilValidate.isNotEmpty(productStoreIdInput)) {   //productListCondition == START
                conditions.add(EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreIdInput));
            }
            cond = EntityCondition.makeCondition(conditions, EntityOperator.AND);
            List<GenericValue> wishBestSellingList = delegator.findList("WishBestSellingList", cond, null, null, null, false);
            for (GenericValue wishBestSelling : wishBestSellingList) {  //loop wishBestSellingList == START
                String productStoreId = wishBestSelling.getString("productStoreId");
                String wishId = wishBestSelling.getString("wishId");
                Map wishUpdateBestSellingSingle = dispatcher.runSync("wishUpdateBestSellingSingle", UtilMisc.toMap("productStoreId", productStoreId, "wishId", wishId, "userLogin", userLogin));
            }   //loop wishBestSellingList == END
        }   //main try == END
        catch (GenericEntityException e) {
            Debug.logError("Yasin: updateBestSellingAuto GenericEntityException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (GenericServiceException e) {
            Debug.logError("Yasin: updateBestSellingAuto GenericServiceException Error for TRY CATCH: " + e.getMessage(), module);
        }
        return result;
    }   //updateBestSellingAuto
    
    public static Map<String, Object> updateWishListingInventory (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreIdInput = (String) context.get("productStoreId");
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(now.getTime());
        
        Debug.logError("Starting updateWishListingInventory" , module);
        try {   //main try -- START
            String url = "https://china-merchant.wish.com/api/v2/";
            EntityCondition cond = null;
            List<EntityCondition> conditions = FastList.newInstance();
            if (UtilValidate.isNotEmpty(productStoreIdInput)) {
                productStoreIdInput = productStoreIdInput.trim();
                cond = EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreIdInput);
            }
            List<GenericValue> wishListingLowQuantityList = delegator.findList("WishListingLowQuantity", cond, null, null, null, false);
            for (GenericValue wishListingLowQuantity : wishListingLowQuantityList)   {   //loop wishListingLowQuantityList == START
                String normalizedSku = wishListingLowQuantity.getString("normalizedSku");
                GenericValue product = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", normalizedSku), false);
                
                if (UtilValidate.isNotEmpty(product)){  //if product is not empty == START
                    String productStoreId = wishListingLowQuantity.getString("productStoreId");
                    GenericValue partyIdentification = delegator.findOne("PartyIdentification", UtilMisc.toMap("partyId", productStoreId, "partyIdentificationTypeId", "WISH_ACCESS_CODE"), false);
                    String accessCode = partyIdentification.getString("idValue");
                    String sku = wishListingLowQuantity.getString("sku");
                    String requestUrl = url + "variant/update-inventory?format=xml&access_token=" + accessCode + "&sku=" + sku + "&inventory=5000";
                    String responseXML = sendHttpRequest(requestUrl);
                    Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                    Element elemResponse = docResponse.getDocumentElement();
                    String ack = UtilXml.childElementValue(elemResponse, "Code", null);
                    if(!ack.equals("0")) {    //if ack failure -- start
                        String errorMessage = UtilXml.childElementValue(elemResponse, "Message", null);
                        FileWriter f1 = new FileWriter("hot-deploy/gudao/webapp/gudao/wish/logError/updateInventory.log", true);
                        f1.write(today + ": product Store ID: " + productStoreId + ", Codes : " + ack + ": " + errorMessage + "\n");
                        f1.close();
                    }					
                } //if product is not empty == START
            }   //loop wishListingLowQuantityList == END
        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError("Finished updateWishListingInventory" , module);
        return result;
    }   //updateWishListingInventory
}
