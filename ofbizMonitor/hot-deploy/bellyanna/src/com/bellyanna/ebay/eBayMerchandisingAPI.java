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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.List;
import java.text.SimpleDateFormat;
import org.dom4j.io.SAXReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
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

import javolution.util.FastMap;

public class eBayMerchandisingAPI {
	private static final String module = eBayMerchandisingAPI.class.getName();
	
    public static Map<String, Object> getMostWatchedItems (DispatchContext dctx, Map context)
    throws GenericEntityException, IOException {
        
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String categoryId = (String) context.get("categoryId");
        String productStoreId = (String) context.get("productStoreId");
        Map mapAccount = FastMap.newInstance();
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        
        try {
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "getMostWatchedItems");
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        //Building XML -- START
        Document rootDoc = UtilXml.makeEmptyXmlDocument("getMostWatchedItemsRequest");
        Element rootElem = rootDoc.getDocumentElement();
        rootElem.setAttribute("xmlns", "http://www.ebay.com/marketplace/services");
        
        /*//RequesterCredentials -- START
        Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
        UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
        //RequesterCredentials -- END*/
        UtilXml.addChildElementValue(rootElem, "categoryId", categoryId, rootDoc);
        UtilXml.addChildElementValue(rootElem, "maxResults", "50", rootDoc);
        //Building XML -- END
        
        String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
        String responseXML = sendRequestXMLtoEbay(mapAccount, requestXMLcode.toString());
        
        //Reading responseXML -- STAR
        try {   //try reading responseXML -- START
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            String ack = UtilXml.childElementValue(elemResponse, "ack", "Failure");
            
            if (ack != null && "Success".equals(ack))   {   //if ack success -- START
                delegator.removeByAnd("EbayMostWatchedItem", UtilMisc.toMap("categoryId", categoryId));
                List<? extends Element> itemRecommendations = UtilXml.childElementList(elemResponse, "itemRecommendations");
                Iterator<? extends Element> itemRecommendationsElemIter = itemRecommendations.iterator();
                while (itemRecommendationsElemIter.hasNext())    {  //loop itemRecommendationsElemIter -- START
                    Element itemRecommendationsElement = itemRecommendationsElemIter.next();
                    List<? extends Element> item = UtilXml.childElementList(itemRecommendationsElement, "item");
                    Iterator<? extends Element> itemElemIter = item.iterator();
                    while (itemElemIter.hasNext())  {   //loop item -- START
                        Element itemElement = itemElemIter.next();
                        String itemId = UtilXml.childElementValue(itemElement, "itemId", null);
                        String title = UtilXml.childElementValue(itemElement, "title", null);
                        String viewItemURL = UtilXml.childElementValue(itemElement, "viewItemURL", null);
                        String globalId = UtilXml.childElementValue(itemElement, "globalId", null);
                        String primaryCategoryId = UtilXml.childElementValue(itemElement, "primaryCategoryId", null);
                        String primaryCategoryName = UtilXml.childElementValue(itemElement, "primaryCategoryName", null);
                        String buyItNowPrice = UtilXml.childElementValue(itemElement, "buyItNowPrice", null);
                        String currencyId = UtilXml.childElementAttribute(itemElement, "buyItNowPrice", "currencyId", null);
                        String country = UtilXml.childElementValue(itemElement, "country", null);
                        String imageURL = UtilXml.childElementValue(itemElement, "imageURL", null);
                        String shippingCost = UtilXml.childElementValue(itemElement, "shippingCost", null);
                        String shippingType = UtilXml.childElementValue(itemElement, "shippingType", null);
                        String watchCount = UtilXml.childElementValue(itemElement, "watchCount", null);
                        
                        GenericValue gv = delegator.makeValue("EbayMostWatchedItem", UtilMisc.toMap("categoryId", categoryId, "itemId", itemId));
                        gv.set("title", title);
                        gv.set("viewItemUrl", viewItemURL);
                        gv.set("globalId", globalId);
                        gv.set("primaryCategoryId", primaryCategoryId);
                        gv.set("primaryCategoryName", primaryCategoryName);
                        gv.set("buyItNowPrice", new BigDecimal(buyItNowPrice));
                        gv.set("currencyId", currencyId);
                        gv.set("country", country);
                        gv.set("imageUrl", imageURL);
                        gv.set("shippingCost", new BigDecimal(shippingCost));
                        gv.set("shippingType", shippingType);
                        gv.set("watchCount", Long.valueOf(watchCount));
                        delegator.createOrStore(gv);
                    }   //loop item -- END
                }   //loop itemRecommendationsElemIter -- END
            }   //if ack success -- END
            else {  //else -- START
                List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                while (errorElementsElemIter.hasNext()) {
                    StringBuffer errorMessage = new StringBuffer();
                    Element errorElement = errorElementsElemIter.next();
                    String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                    String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                    errorMessage.append(shortMessage + " - " + longMessage);
                    FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/GetMostWatchedItemError.log", true);
                    f1.write(today + ": productStoreId " + productStoreId + ", categoryId " + categoryId + ": " + errorMessage + "\n");
                    f1.close();
                }
                //return ServiceUtil.returnError(errorMessage.toString());
            }   //else -- END
            
            
        }   //try reading responseXML -- END
        catch (Exception e) {
            Debug.logError("Exception during read response from Ebay", module);
            e.printStackTrace();
            //return ServiceUtil.returnError(e.getMessage());
        }

        
        //Reading responseXML -- END        

        return ServiceUtil.returnSuccess();
    }
    
            
    public static String sendRequestXMLtoEbay(Map mapContent, String generatedXmlData) throws IOException
    {
        Properties properties = new Properties();
        properties.load(new FileInputStream("hot-deploy/bellyanna/config/eBay.properties"));
        String postItemsUrl = properties.getProperty("MerchandisingApi.url");
        String version = properties.getProperty("MerchandisingApi.version");
        
        Map<String, String> requestPropertyMap = FastMap.<String, String>newInstance();
        requestPropertyMap.put("X-EBAY-SOA-OPERATION-NAME", mapContent.get("callName").toString());
        requestPropertyMap.put("X-EBAY-SOA-SERVICE-VERSION", version);
        requestPropertyMap.put("EBAY-SOA-CONSUMER-ID", mapContent.get("appId").toString());
        requestPropertyMap.put("X-EBAY-SOA-GLOBAL-ID", mapContent.get("globalId").toString());
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
        
        if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = connection.getInputStream();
            response = inputStreamToString(inputStream);
        } else {
            inputStream = connection.getErrorStream();
            response = inputStreamToString(inputStream);
        }
        
        return (response == null || "".equals(response.trim())) ? String.valueOf(responseCode) : response;
    }
    
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
    
    
    
    
}
