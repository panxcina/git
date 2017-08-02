package com.gudao.amazon;
//https://developer.amazonservices.com/gp/mws/api.html?ie=UTF8&group=sellers&section=sellers&version=latest

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
import org.apache.commons.codec.binary.Base64;

import javolution.util.FastMap;

import com.amazonaws.mws.*;

public class amazon {
	private static final String module = amazon.class.getName();
    private static final String CHARACTER_ENCODING = "UTF-8";
    final static String ALGORITHM = "HmacSHA256";
    
    public static Map<String, String> amazonProperties ()
    throws IOException {
        
        Map<String, String> mapContent = FastMap.newInstance();
        try {   //main try -- START
            Properties properties = new Properties();
            properties.load(new FileInputStream("hot-deploy/gudao/config/amazon.properties"));
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
    
    public static Map<String, Object> createNewAccount (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("accountName");
        String accessKeyId = (String) context.get("accessKeyId");
        String secretAccessKey = (String) context.get("secretAccessKey");
        String appName = (String) context.get("appName");
        String appVersion = (String) context.get("appVersion");
        String serviceUrl = (String) context.get("serviceURL");
        String merchantId = (String) context.get("merchantId");
        String marketplaceId = (String) context.get("marketplaceId");
        String sellerDevAuthToken = (String) context.get("sellerDevAuthToken");
        
        productStoreId = productStoreId.trim();
        accessKeyId = accessKeyId.trim();
        secretAccessKey =  secretAccessKey.trim();
        merchantId = merchantId.trim();
        if (UtilValidate.isNotEmpty(appName)) {
            appName = appName.trim();
        } else {
            appName = "BELLYANNA";
        }
        if (UtilValidate.isNotEmpty(appVersion)) {
            appVersion = appVersion.trim();
        } else {
            appVersion = "1.0";
        }
        if (UtilValidate.isNotEmpty(serviceUrl)) {
            serviceUrl = serviceUrl.trim();
        } else {
            serviceUrl = "https://mws.amazonservices.com";
        }
        if (UtilValidate.isNotEmpty(marketplaceId)) {
            marketplaceId = marketplaceId.trim();
        } else {
            marketplaceId = "ATVPDKIKX0DER";
        }
        if (UtilValidate.isNotEmpty(sellerDevAuthToken)) {
            sellerDevAuthToken = sellerDevAuthToken.trim();
        }
        
        try {   //main try == START
            List<GenericValue> existingProductStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("primaryStoreGroupId", "AMAZON"), null, false);
            int amazonSeq = 1;
            if (UtilValidate.isNotEmpty(existingProductStoreList)) {
                for (GenericValue existingProductStore : existingProductStoreList) {    //loop existingProductStoreList == START
                    String existingSubtitle = existingProductStore.getString("subtitle");
                    int currentStoreSeq = Integer.parseInt(existingSubtitle);
                    if (amazonSeq <= currentStoreSeq) {
                        amazonSeq = currentStoreSeq;
                    }
                }   //loop existingProductStoreList == END
                amazonSeq = amazonSeq + 1;
            }
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            if (UtilValidate.isEmpty(productStore)) {   //if productStore is not empty == START
                productStore = delegator.makeValue("ProductStore", UtilMisc.toMap("productStoreId", productStoreId));
                productStore.set("storeName", productStoreId);
                productStore.set("title", productStoreId);
                productStore.set("subtitle", amazonSeq + "");
                productStore.set("companyName", "Gudao");
                productStore.set("primaryStoreGroupId", "AMAZON");
                productStore.set("isDemoStore", "N");
                productStore.set("visualThemeId", "EC_DEFAULT");
                productStore.set("inventoryFacilityId", "PDWarehouse");
                productStore.set("defaultSalesChannelEnumId", "AMAZON_SALES_CHANNEL");
                delegator.create(productStore);
                
                GenericValue party = delegator.makeValue("Party", UtilMisc.toMap("partyId", productStoreId, "partyTypeId", "PARTY_GROUP", "preferredCurrencyUomId", "USD", "statusId", "PARTY_ENABLED"));
                delegator.create(party);
                
                GenericValue partyGroup = delegator.makeValue("PartyGroup", UtilMisc.toMap("partyId", productStoreId, "groupName", productStoreId));
                delegator.create(partyGroup);
                
                GenericValue partyRole = delegator.makeValue("PartyRole", UtilMisc.toMap("partyId", productStoreId, "roleTypeId", "AMAZON_ACCOUNT"));
                delegator.create(partyRole);
                
                Calendar now = Calendar.getInstance();
                String fixedFromDate = new SimpleDateFormat("2010-01-01 HH:mm:ss.SSS").format(now.getTime());
                GenericValue productStoreRole = delegator.makeValue("ProductStoreRole", UtilMisc.toMap("partyId", productStoreId, "roleTypeId", "AMAZON_ACCOUNT", "productStoreId", productStoreId, "fromDate", Timestamp.valueOf(fixedFromDate)));
                delegator.create(productStoreRole);
                
                GenericValue accessKeyIdGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                accessKeyIdGV.set("partyIdentificationTypeId", "AMAZON_ACCESS_KEY_ID");
                accessKeyIdGV.set("idValue", accessKeyId);
                delegator.create(accessKeyIdGV);
                
                GenericValue secretAccessKeyGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                secretAccessKeyGV.set("partyIdentificationTypeId", "AMAZON_SECRET_ACCESS_KEY");
                secretAccessKeyGV.set("idValue", secretAccessKey);
                delegator.create(secretAccessKeyGV);
                
                GenericValue appNameGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                appNameGV.set("partyIdentificationTypeId", "AMAZON_APP_NAME");
                appNameGV.set("idValue", appName);
                delegator.create(appNameGV);
                
                GenericValue appVersionGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                appVersionGV.set("partyIdentificationTypeId", "AMAZON_APP_VERSION");
                appVersionGV.set("idValue", appVersion);
                delegator.create(appVersionGV);
                
                GenericValue serviceUrlGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                serviceUrlGV.set("partyIdentificationTypeId", "AMAZON_SERVICE_URL");
                serviceUrlGV.set("idValue", serviceUrl);
                delegator.create(serviceUrlGV);
                
                GenericValue merchantIdGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                merchantIdGV.set("partyIdentificationTypeId", "AMAZON_MERCHANT_ID");
                merchantIdGV.set("idValue", merchantId);
                delegator.create(merchantIdGV);
                
                GenericValue marketplaceIdGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                marketplaceIdGV.set("partyIdentificationTypeId", "AMAZON_MARKETPLACE_ID");
                marketplaceIdGV.set("idValue", marketplaceId);
                delegator.create(marketplaceIdGV);
                
                if (UtilValidate.isNotEmpty(sellerDevAuthToken)) {
                    GenericValue sellerDevAuthTokenGV = delegator.makeValue("PartyIdentification", UtilMisc.toMap("partyId", productStoreId));
                    sellerDevAuthTokenGV.set("partyIdentificationTypeId", "AMAZON_SELLER_DEV_AUTH_TOKEN");
                    sellerDevAuthTokenGV.set("idValue", sellerDevAuthToken);
                    delegator.create(sellerDevAuthTokenGV);
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
        
        return result;
    }   //createNewAccount
    
    public static Map<String, Object> getAmazonApiInfo (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("accountName");
        productStoreId = productStoreId.trim();
        
        try {
            Map<String, Object> apiKeyMap = FastMap.newInstance();
            String accessKeyId = null;
            String secretAccessKey = null;
            String appName = null;
            String appVersion = null;
            String serviceUrl = null;
            String merchantId = null;
            String sellerDevAuthToken = null;
            String marketplaceId = null;
            
            List<GenericValue> partyIdentificationList = delegator.findByAnd("PartyIdentification", UtilMisc.toMap("partyId", productStoreId), null, false);
            for (GenericValue partyIdentification : partyIdentificationList) {  //loop partyIdentificationList == START
                String partyIdentificationTypeId = partyIdentification.getString("partyIdentificationTypeId");
                String idValue = partyIdentification.getString("idValue");
                if (partyIdentificationTypeId.equals("AMAZON_ACCESS_KEY_ID")) {
                    accessKeyId =  idValue;
                } else if (partyIdentificationTypeId.equals("AMAZON_SECRET_ACCESS_KEY")) {
                    secretAccessKey =  idValue;
                } else if (partyIdentificationTypeId.equals("AMAZON_APP_NAME")) {
                    appName =  idValue;
                } else if (partyIdentificationTypeId.equals("AMAZON_APP_VERSION")) {
                    appVersion =  idValue;
                } else if (partyIdentificationTypeId.equals("AMAZON_SERVICE_URL")) {
                    serviceUrl =  idValue;
                } else if (partyIdentificationTypeId.equals("AMAZON_MERCHANT_ID")) {
                    merchantId =  idValue;
                } else if (partyIdentificationTypeId.equals("AMAZON_MARKETPLACE_ID")) {
                    marketplaceId =  idValue;
                } else if (partyIdentificationTypeId.equals("AMAZON_SELLER_DEV_AUTH_TOKEN")) {
                    sellerDevAuthToken =  idValue;
                }
            }   //loop partyIdentificationList == END
            
            apiKeyMap.put("accessKeyId", accessKeyId);
            apiKeyMap.put("secretAccessKey", secretAccessKey);
            apiKeyMap.put("appName", appName);
            apiKeyMap.put("appVersion", appVersion);
            apiKeyMap.put("serviceUrl", serviceUrl);
            apiKeyMap.put("merchantId", merchantId);
            apiKeyMap.put("sellerDevAuthToken", sellerDevAuthToken);
            apiKeyMap.put("marketplaceId", marketplaceId);
            
            MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();
            config.setServiceURL(serviceUrl);
            
            MarketplaceWebService service = new MarketplaceWebServiceClient(
                                                                            accessKeyId, secretAccessKey, appName, appVersion, config);
            apiKeyMap.put("marketplaceWebService", service);
            result.put("apiKeyMap", apiKeyMap);
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
        }
        return result;
        
    }   //getAmazonApiInfo
    
}
