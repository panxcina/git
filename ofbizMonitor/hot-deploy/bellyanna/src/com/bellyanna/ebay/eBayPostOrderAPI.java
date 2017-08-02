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

public class eBayPostOrderAPI {
	private static final String module = eBayPostOrderAPI.class.getName();

    public static String sendHTTPRequestToEbay(Map mapContent, String generatedXmlData) throws IOException
    {
        Properties properties = new Properties();
        properties.load(new FileInputStream("hot-deploy/bellyanna/config/eBay.properties"));
        String caseId = mapContent.get("caseId").toString();
        String postItemsUrl = properties.getProperty("PostOrderAPI.issueRefund");
        postItemsUrl.replace("{caseId}", caseId);
        
        Map<String, String> requestPropertyMap = FastMap.<String, String>newInstance();
        requestPropertyMap.put("Accept", "application/json");
        requestPropertyMap.put("Authorization", mapContent.get("token").toString());
        requestPropertyMap.put("X-EBAY-C-MARKETPLACE-ID", mapContent.get("globalId").toString());
        requestPropertyMap.put("Content-Type", "application/json");
        
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
    
    public static Map<String, Object> issueRefund (DispatchContext dctx, Map context)
    throws GenericEntityException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String caseId = (String) context.get("caseId");
        Map result = ServiceUtil.returnSuccess();
        //Map disputeResult = FastMap.newInstance();
        Debug.logError("Start issuing refund for account: " + productStoreId + ", caseId: " + caseId, module);
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
        Timestamp todayTimestamp = Timestamp.valueOf(sdf.format(now.getTime()));
        
        try {   //main try -- START
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            Map mapAccount = common.accountInfo(delegator, productStore);
            Properties properties = new Properties();
            properties.load(new FileInputStream("hot-deploy/bellyanna/config/eBay.properties"));
            String postItemsUrl = properties.getProperty("PostOrderAPI.issueRefund");
            postItemsUrl = postItemsUrl.replace("caseId", caseId);
            
            Debug.logError("PostItemsUrl: " + postItemsUrl, module);
            
            Map<String, String> requestPropertyMap = FastMap.<String, String>newInstance();
            requestPropertyMap.put("Accept", "application/json");
            requestPropertyMap.put("Authorization", "TOKEN <" + mapAccount.get("token").toString() + ">");
            requestPropertyMap.put("X-EBAY-C-MARKETPLACE-ID", mapAccount.get("globalId").toString());
            requestPropertyMap.put("Content-Type", "application/json");
            Debug.logError("start sending http connection", module);
            HttpURLConnection connection = (HttpURLConnection) (new URL(postItemsUrl)).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            
            //HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestMethod("GET");
            //connection.setReadTimeout(60*1000);
            //connection.connect();

            
            if(requestPropertyMap != null && !requestPropertyMap.isEmpty()) {
                Iterator<String> keyIt = requestPropertyMap.keySet().iterator();
                while(keyIt.hasNext()) {
                    String key = keyIt.next();
                    String value = requestPropertyMap.get(key);
                    connection.setRequestProperty(key, value);
                }
            }
            
            //connection.connect();
            String jsonRequest = "{ /* CaseVoluntaryRefundRequest */"
            + "\"comments\":"
            + "{"
            + "    \"content\": \"Your refund is on the way\", "
            + "    \"language\": \"en\", "
            //+ "    \"translatedFromContent\": string, "
            //+ "    \"translatedFromLanguage\": LanguageEnum "
            + "}"
            + "}";
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonRequest.getBytes());
            outputStream.flush();
            
            int responseCode = connection.getResponseCode();
            InputStream inputStream = null;
            String response = null;
            Debug.logError("connection response code: " + responseCode, module);
            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                response = inputStreamToString(inputStream);
            } else {
                inputStream = connection.getErrorStream();
                response = inputStreamToString(inputStream);
            }
            
            Debug.logError(response, module);

        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }   //getUserCases

    
}
