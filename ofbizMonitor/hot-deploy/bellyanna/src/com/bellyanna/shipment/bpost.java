package com.bellyanna.shipment;

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
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.math.BigDecimal;
import java.util.List;
import java.util.LinkedList;
import java.util.Locale;
import java.text.SimpleDateFormat;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.dom4j.io.SAXReader;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.ofbiz.base.json.JSON;
import org.ofbiz.base.json.JSONConstants;
//import org.ofbiz.base.json.JSONObject;
import org.ofbiz.base.json.JSONWriter;
import org.ofbiz.base.json.ParseException;
import org.ofbiz.base.json.Token;
import org.ofbiz.base.json.TokenMgrError;
import org.ofbiz.base.lang.SourceMonitored;
import org.ofbiz.base.test.GenericTestCaseBase;
import org.ofbiz.base.util.IndentingWriter;
//import org.ofbiz.content.content.ContentWorker;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.serialize.XmlSerializer;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import freemarker.template.TemplateException;

import org.apache.commons.codec.binary.Base64;

import com.bellyanna.shipment.common;

public class bpost {

private static final String module= bpost.class.getName();
    
    public static Map<String, String> accountInfo () throws IOException {
        
        Map mapAccount = FastMap.newInstance();
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("hot-deploy/bellyanna/config/bpost.properties"));
            
            String serviceEndPoint = properties.getProperty("bpost.serviceEndPoint");
            String contractId = properties.getProperty("bpost.contractId");
            String username = properties.getProperty("bpost.username");
            String password = properties.getProperty("bpost.password");
            
            String authString = username + ":" + password;
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(authEncBytes);
            
            
            mapAccount.put("serviceEndPoint", serviceEndPoint);
            mapAccount.put("contractId", contractId);
            mapAccount.put("apiToken", authStringEnc);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return mapAccount;
    }

    public static Map<String, Object> createLvsParcel (DispatchContext dctx, Map context)
	throws GenericServiceException, IOException {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String orderId = (String) context.get("orderId");
        
        Map mapAccount = FastMap.newInstance();
        Map mapContent = FastMap.newInstance();
        List<Map> orderItemMapList = new LinkedList<Map>();
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        
        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/shipment/errorLog/bpost.log", true);
        try {		//try block start
            Map<String, Object> getApiShipmentInfo = dispatcher.runSync("getApiShipmentInfo", UtilMisc.toMap("orderId", orderId, "userLogin", userLogin));
            
            if (ServiceUtil.isSuccess(getApiShipmentInfo)) {    //if getApiShipmentInfo success -- START
                mapContent = (Map) getApiShipmentInfo.get("mapContent");
                orderItemMapList = (List) getApiShipmentInfo.get("mapItem");
            }   //if getApiShipmentInfo success -- START
            
            mapAccount = accountInfo();
            Iterator it2 = mapAccount.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry pairs = (Map.Entry)it2.next();
                Debug.logError(pairs.getKey() + " = " + pairs.getValue(), module);
            }
            
            Iterator it = mapContent.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                Debug.logError(pairs.getKey() + " = " + pairs.getValue(), module);
            }
            
            for (Map itemMapContent : orderItemMapList) {
                Iterator itemIt = itemMapContent.entrySet().iterator();
                while (itemIt.hasNext()) {
                    Map.Entry pairs = (Map.Entry)itemIt.next();
                    Debug.logError(pairs.getKey() + " = " + pairs.getValue(), module);
                }
                
            }
            
            //building request XML -- START
            /*Document rootDoc = UtilXml.makeEmptyXmlDocument("LvsParcelJsonModel");
            Element rootElem = rootDoc.getDocumentElement();
            //rootElem.createElementNS("http://schemas.datacontract.org/2004/07/Cnzilla.Bpost.Common.Models", "LvsParcelJsonModel");
            //rootElem.setAttributeNS(null, "xmlns", "http://schemas.datacontract.org/2004/07/Cnzilla.Bpost.Common.Models");
            rootElem.setAttributeNS("i", "xmlns", "http://www.w3.org/2001/XMLSchema-instance");
            rootElem.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xmlns", "http://schemas.datacontract.org/2004/07/Cnzilla.Bpost.Common.Models");
            
            //rootElem.setAttribute("xmlns:i", "http://www.w3.org/2001/XMLSchema-instance");
            //rootElem.setAttribute("xmlns:i", "http://www.w3.org/2001/XMLSchema-instance");
            //rootElem.setAttribute("xmlns", "http://schemas.datacontract.org/2004/07/Cnzilla.Bpost.Common.Models");
            
            UtilXml.addChildElementValue(rootElem, "ContractId", mapAccount.get("contractId").toString(), rootDoc);
            UtilXml.addChildElementValue(rootElem, "OrderNumber", mapContent.get("OrderId").toString(), rootDoc);
            UtilXml.addChildElementValue(rootElem, "SenderName", mapContent.get("ShipFromContact").toString(), rootDoc);
            UtilXml.addChildElementValue(rootElem, "SenderAddress", mapContent.get("ShipFromStreet").toString(), rootDoc);
            UtilXml.addChildElementValue(rootElem, "SenderSequence", "1", rootDoc);
            UtilXml.addChildElementValue(rootElem, "RecipientName", mapContent.get("ShipToContact").toString(), rootDoc);
            UtilXml.addChildElementValue(rootElem, "RecipientStreet", mapContent.get("ShipToStreet").toString(), rootDoc);
            UtilXml.addChildElementValue(rootElem, "RecipientHouseNumber", "", rootDoc);
            UtilXml.addChildElementValue(rootElem, "RecipientBusnumber", "", rootDoc);
            UtilXml.addChildElementValue(rootElem, "RecipientZipCode", mapContent.get("ShipToPostcode").toString(), rootDoc);
            UtilXml.addChildElementValue(rootElem, "RecipientCity", mapContent.get("ShipToCity").toString(), rootDoc);
            if (UtilValidate.isNotEmpty(mapContent.get("ShipToProvince"))) {
                if (!mapContent.get("ShipToProvince").equals("Not Applicable")) {
                    UtilXml.addChildElementValue(rootElem, "RecipientState", mapContent.get("ShipToProvince").toString(), rootDoc);
                }
            }
            UtilXml.addChildElementValue(rootElem, "RecipientCountry", mapContent.get("ShipToCountryCode").toString(), rootDoc);
            UtilXml.addChildElementValue(rootElem, "PhoneNumber", mapContent.get("ShipToPhone").toString(), rootDoc);
            UtilXml.addChildElementValue(rootElem, "Email", mapContent.get("ShipToEmail").toString(), rootDoc);
            //UtilXml.addChildElementValue(rootElem, "SenderName", mapContent.get("").toString(), rootDoc);
            
            Element customs = UtilXml.addChildElement(rootElem, "Customs", rootDoc);
            //loop orderItem -- START
            for (Map itemMapContent : orderItemMapList) {   //loop orderItemMapList -- START
                Element LvsCustomJsonModel = UtilXml.addChildElement(customs, "LvsCustomJsonModel", rootDoc);
                UtilXml.addChildElementValue(LvsCustomJsonModel, "Sku", itemMapContent.get("productId").toString(), rootDoc);
                UtilXml.addChildElementValue(LvsCustomJsonModel, "SkuInInvoice", itemMapContent.get("productId").toString(), rootDoc);
                UtilXml.addChildElementValue(LvsCustomJsonModel, "ChineseContentDescription", itemMapContent.get("CustomsTitleCN").toString(), rootDoc);
                UtilXml.addChildElementValue(LvsCustomJsonModel, "ItemContent", itemMapContent.get("CustomsTitleEN").toString(), rootDoc);
                UtilXml.addChildElementValue(LvsCustomJsonModel, "ItemCount", itemMapContent.get("PostedQTY").toString(), rootDoc);
                UtilXml.addChildElementValue(LvsCustomJsonModel, "Value", itemMapContent.get("DeclaredValue").toString(), rootDoc);
                UtilXml.addChildElementValue(LvsCustomJsonModel, "Currency", "USD", rootDoc);
                UtilXml.addChildElementValue(LvsCustomJsonModel, "Weight", Math.round(Double.parseDouble(itemMapContent.get("Weight").toString()) * 1000) + "", rootDoc);
            }   //loop orderItemMapList -- END
            //loop orderItem -- END
            //building request XML -- END
            
            String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
            Debug.logError(requestXMLcode, module);*/
            
            /*//build HTTP connection -- START
            String postItemsUrl = mapAccount.get("serviceEndPoint") + "api/LvsParcels";
            String apiToken = mapAccount.get("apiToken").toString();
            Map<String, String> requestPropertyMap = FastMap.<String, String>newInstance();
            requestPropertyMap.put("Authorization", "Basic " + apiToken);
            requestPropertyMap.put("Content-Type", "text/xml; charset=utf-8");
            
            HttpURLConnection connection = (HttpURLConnection) (new URL(postItemsUrl)).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            if(requestPropertyMap != null && !requestPropertyMap.isEmpty()) {
                Iterator<String> keyIt = requestPropertyMap.keySet().iterator();
                while(keyIt.hasNext()) {
                    String key = keyIt.next();
                    String value = requestPropertyMap.get(key);
                    Debug.logError("Key: " + key + ", value: " + value, module);
                    connection.setRequestProperty(key, value);
                }
            }
            
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestXMLcode.toString().getBytes());
            outputStream.flush();
            
            int responseCode = connection.getResponseCode();
            Debug.logError("responseCode: " + responseCode, module);
            InputStream inputStream = null;
            String response = null;
            
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                response = inputStreamToString(inputStream);
            } else {
                inputStream = connection.getErrorStream();
                response = inputStreamToString(inputStream);
            }
            //build HTTP connection -- END
            
            Debug.logError(response, module);*/
            
        }		//try block end
        catch (GenericServiceException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return ServiceUtil.returnSuccess();
        
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
} //END class
