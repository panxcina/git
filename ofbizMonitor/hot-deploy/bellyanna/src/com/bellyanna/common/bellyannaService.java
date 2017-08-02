/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.bellyanna.common;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Iterator;

import java.net.URLDecoder;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
//import jxl.*;
//import jxl.read.biff.BiffException;
//import jxl.write.*;
import org.jsoup.*;

import java.util.TimeZone;
import javolution.util.FastList;
import javolution.util.FastMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ofbiz.base.util.UtilHttp;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.ofbiz.base.util.ObjectType;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.DynamicViewEntity;
import org.ofbiz.entity.model.ModelKeyMap;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.entity.util.EntityUtil;
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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bellyanna.ebay.eBayTradingAPI;
import com.bellyanna.ebay.common;

import org.json.JSONObject;
import org.json.XML;


public class bellyannaService {
	private static final String module = bellyannaService.class.getName();
    private static final String eol = System.getProperty("line.separator");
    
    public static Map<String, Object> testForVlax (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        String productStoreId = (String) context.get("productStoreId");
        String itemId = (String) context.get("itemId");
        
        Map result = ServiceUtil.returnSuccess();
        
        
        try {
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "GetItem");
            mapAccount.put("productStoreId", productStoreId);
            
            //Write REQUEST == START
            
            //Building XML -- START
            Document rootDoc = UtilXml.makeEmptyXmlDocument("GetItemRequest");
            Element rootElem = rootDoc.getDocumentElement();
            rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
            
            //RequesterCredentials -- START
            Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
            UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
            //RequesterCredentials -- END
            
            UtilXml.addChildElementValue(rootElem, "ItemID", itemId, rootDoc);
            //Building XML -- END
            
            //Write REQUEST == END
            
            String requestXML = UtilXml.writeXmlDocument(rootDoc);
            Debug.logError(requestXML, module);
            
            String responseXML = eBayTradingAPI.sendRequestXMLToEbay(mapAccount, requestXML);
            
            Debug.logError(responseXML, module);
            
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
        
        
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

	
    public static Map<String, Object> YasinTestJava (DispatchContext dctx, Map context) {   //YasinTestJava
        //Debug.logError("start", module);
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        Debug.logError("Start", module);
        //String text = (String) context.get("Text");
        try {   //start try block
            String productStoreId = "09lijun";
            String site = "US";
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", "09lijun"), true);
            boolean dangerAccount = false;
            if (UtilValidate.isNotEmpty(productStore.getString("companyName"))) {   //if companyName is not empty -- START
                if (productStore.getString("companyName").equals("DANGEROUS")) {
                    dangerAccount = true;
                }
            }   //if companyName is not empty -- START
            //f1.write("Starting account " + productStoreId + "; dangerAccount : " + dangerAccount + eol);
            
            double siteLowestPrice = 1.0;
            List<GenericValue> siteLowestList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "US"), null, true);
            if (UtilValidate.isNotEmpty(siteLowestList)) {
                siteLowestPrice = Double.parseDouble(EntityUtil.getFirst(siteLowestList).getString("value"));
            }
            List<String> siteList = new LinkedList<String>();
            if (site.equals("US")) {
                siteList.add("US");
                siteList.add("eBayMotors");
            } else if (site.equals("UK")) {
                siteList.add("UK");
            } else if (site.equals("AU")) {
                siteList.add("Australia");
            } else if (site.equals("CA")) {
                siteList.add("Canada");
            } else if (site.equals("DE")) {
                siteList.add("Germany");
            } else if (site.equals("FR")) {
                siteList.add("France");
            } else if (site.equals("IT")) {
                siteList.add("Italy");
            } else if (site.equals("ES")) {
                siteList.add("Spain");
            }
            List<String> safeStatusList = new ArrayList<String>();
            safeStatusList.add("ACTIVE");
            safeStatusList.add("SETSKU");
            safeStatusList.add("DUPLICATED");
            safeStatusList.add("AU不上");
            safeStatusList.add("EU不上");
            safeStatusList.add("UK不上");
            safeStatusList.add("US不上");
            safeStatusList.add("只上EU");

            
            
            EntityCondition ruleCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                          EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, "09lijun"),
                                                                                          EntityCondition.makeCondition("site", EntityOperator.EQUALS, "US"),
                                                                                          EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE")
                                                                                          ));
            
            List<GenericValue> accountListingRuleList = delegator.findList("AccountListingRule", ruleCondition, null, null, null, true);
            Debug.logError("accountListingRuleList size: " + accountListingRuleList.size(), module);
            //f1.write("Start looping accountListingRuleList" + eol);
            int countTest = 1;
            for (GenericValue accountListingRule : accountListingRuleList) {    //loop accountListingRuleList -- START
                Debug.logError("CountTest: " + countTest, module); countTest++;
                String folderName = accountListingRule.getString("folderName");
                String skuPrefix = accountListingRule.getString("skuPrefix");
                String skuSuffix = accountListingRule.getString("skuSuffix");
                String titleSuffix = accountListingRule.getString("titleSuffix");
                //String ownerGroup = accountListingRule.getString("ownerGroup");
                //Debug.logError("generate MV for " + productStoreId + ", site: " + site + ", foldername: " + folderName, module);
                //f1.write("Account " + productStoreId + ", ownerGroup: " + ownerGroup + ", site: " + site + ", foldername: " + folderName + ", skuPrefix: " + skuPrefix + ", skuSuffix: " + skuSuffix + ", titleSuffix: " + titleSuffix + eol);
                if (UtilValidate.isEmpty(skuPrefix)) {
                    skuPrefix = "";
                }
                if (UtilValidate.isEmpty(skuSuffix)) {
                    skuSuffix = "";
                }
                
                List<GenericValue> accountListingRuleCategoryList = delegator.findByAnd("AccountListingRuleCategory", UtilMisc.toMap("productStoreId", productStoreId, "statusId", "ACTIVE", "site", "US", "folderName", folderName), null, false);
                List<String> categoryList = new LinkedList<String>();
                for (GenericValue accountListingRuleCategory: accountListingRuleCategoryList) { //loop accountListingRuleCategoryList -- START
                    String categoryId = accountListingRuleCategory.getString("categoryId");
                    if (UtilValidate.isNotEmpty(categoryId)) {
                        categoryList.add(categoryId);
                        //f1.write("Special Requirement: " + specialRequirement + eol);
                    }
                }   //loop accountListingRuleCategoryList -- END
                
                
                EntityCondition checkMotherVersionCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                            EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, "US"),
                                                                                                            EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, "ST1FREE"),
                                                                                                            EntityCondition.makeCondition("format", EntityOperator.EQUALS, "固定价格"),
                                                                                                            EntityCondition.makeCondition("yunqudaoListingIdVar", EntityOperator.EQUALS, "7ff3b039-a653-4163-810f-c395bf2a844e")
                                                                                                            ));
                List<GenericValue> checkMotherVersionSingleList = delegator.findList("MotherVersion", checkMotherVersionCondition, null, UtilMisc.toList("id"), null, false);
                Debug.logError("checkMotherVersionSingleList size: " + checkMotherVersionSingleList.size(), module);
                for (GenericValue mv : checkMotherVersionSingleList) {  //loop checkMotherVersionSingleList == START ==
                    String productId = mv.getString("sku");
                    String yunqudaoListingIdVar = mv.getString("yunqudaoListingIdVar");
                    String yunqudaoListingId = mv.getString("yunqudaoListingId");
                    boolean addListingId = false;
                    
                    GenericValue pmSingleCheck = delegator.findOne("ProductMasterResult", UtilMisc.toMap("productId", productId), false);
                    String pmSingleCheckProductGroup = pmSingleCheck.getString("productGroup");
                    String pmSingleCheckRisk = pmSingleCheck.getString("risk");
                    String pmSingleCheckStatusId = pmSingleCheck.getString("statusId");
                    //Debug.logError("pmMotherCheck: " + pmSingleCheck, module);
                    if (UtilValidate.isNotEmpty(pmSingleCheck)) {   //if pmSingleCheck is not empty == START
                        if (UtilValidate.isNotEmpty(categoryList)) {    //if categoryList is not empty == START
                            String singleCategoryIdParent = pmSingleCheck.getString("categoryIdParent");
                            if (UtilValidate.isNotEmpty(singleCategoryIdParent)) { // if singleCategoryIdParent is NOT EMPTY == START
                                if(categoryList.contains(singleCategoryIdParent)) {    //category matched categoryList == START
                                    if (!pmSingleCheckProductGroup.equals("G4") && !pmSingleCheckProductGroup.equals("G5") && !pmSingleCheckRisk.equals("高") && safeStatusList.contains(pmSingleCheckStatusId)) {   //general safe check == START
                                        addListingId = true;
                                        if (site.equals("US")) {
                                            if (pmSingleCheckStatusId.equals("US不上") || pmSingleCheckStatusId.equals("只上EU")) {
                                                addListingId = false;
                                            }
                                        } else if (site.equals("UK")) {
                                            if (pmSingleCheckStatusId.equals("UK不上") || pmSingleCheckStatusId.equals("只上EU")) {
                                                addListingId = false;
                                            }
                                        } else if (site.equals("AU")) {
                                            if (pmSingleCheckStatusId.equals("AU不上") || pmSingleCheckStatusId.equals("只上EU")) {
                                                addListingId = false;
                                            }
                                        } else if (site.equals("CA")) {
                                            continue;
                                        } else {
                                            if (pmSingleCheckStatusId.equals("EU不上")) {
                                                addListingId = false;
                                            }
                                        }
                                        
                                        if (dangerAccount) {    //dangerous account == START
                                            if (!pmSingleCheckRisk.equals("低")) {   //check risk == START
                                                addListingId = false;
                                            }  //check risk == END
                                        }   //dangerous account == END
                                    }   //general safe check == END
                                }   //category matched categoryList == END
                            }   // if singleCategoryIdParent is NOT EMPTY == END
                        }   //if categoryList is not empty == END
                    }   //if pmSingleCheck is not empty == END
                    //Debug.logError("ProductId: " + productId + ", addListingId is " + addListingId, module);
                    if (addListingId) { //addListingId for single listing == START
                        String quantity = mv.getString("quantity");
                        String currency = mv.getString("currency");
                        String sku = mv.getString("sku");
                        String buyItNowPrice = mv.getString("buyItNowPrice");
                        String ebayAccountName = productStoreId;
                        String siteId = mv.getString("siteId");
                        String title = mv.getString("title").substring(0, mv.getString("title").length() - 4) + " " + titleSuffix;
                        String duration = "GTC";
                        String privateListing = "FALSE";
                        String uploadImageEps = "TRUE";
                        String showImageInDesc = "TRUE";
                        String upc = mv.getString("upc");
                        if (UtilValidate.isEmpty(upc)) {
                            upc = "Does not apply";
                            if (UtilValidate.isNotEmpty(pmSingleCheck)) {
                                if (UtilValidate.isNotEmpty(pmSingleCheck.getString("upc"))) {
                                    upc = pmSingleCheck.getString("upc");
                                }
                            }
                        }
                        String ean = mv.getString("ean");
                        if (UtilValidate.isEmpty(ean)) {
                            ean = "Does not apply";
                        }
                        String isbn = mv.getString("isbn");
                        if (UtilValidate.isEmpty(isbn)) {
                            isbn = "Does not apply";
                        }
                        String brandMpnBrand = mv.getString("brandMpnBrand");
                        if (UtilValidate.isEmpty(brandMpnBrand)) {
                            brandMpnBrand = "WHATWEARS";
                        }
                        String brandMpnMpn = mv.getString("brandMpnMpn");
                        if (UtilValidate.isEmpty(brandMpnMpn)) {
                            brandMpnMpn = "Does not apply";
                        }
                        String location = "China";
                        String getItFast = "FALSE";
                        String extendedHolidayReturns = "TRUE";
                        String bRLinkedPayPalAccount = "FALSE";
                        String storeCategory = mv.getString("storeCategory");
                        GenericValue gudaoCategoryMap = delegator.findOne("GudaoCategoryMap", UtilMisc.toMap("productCategory", pmSingleCheck.getString("categoryIdParent"), "platform", "EBAY", "account", productStoreId), true);
                        if (UtilValidate.isNotEmpty(gudaoCategoryMap)) {
                            storeCategory = gudaoCategoryMap.getString("categoryId");
                        }
                        String shippingService1Option = null;
                        String shippingService1Cost = null;
                        String shippingService1AddCost = null;
                        String shippingService2Option = null;
                        String shippingService2Cost = null;
                        String shippingService2AddCost = null;
                        String shippingService3Option = null;
                        String shippingService3Cost = null;
                        String shippingService3AddCost = null;
                        String shippingService4Option = null;
                        String shippingService4Cost = null;
                        String shippingService4AddCost = null;
                        String intShippingService1Option = null;
                        String intShippingService1Cost = null;
                        String intShippingService1AddCost = null;
                        String intShippingService1Locations = null;
                        String intShippingService2Option = null;
                        String intShippingService2Cost = null;
                        String intShippingService2AddCost = null;
                        String intShippingService2Locations = null;
                        String intShippingService3Option = null;
                        String intShippingService3Cost = null;
                        String intShippingService3AddCost = null;
                        String intShippingService3Locations = null;
                        String intShippingService4Option = null;
                        String intShippingService4Cost = null;
                        String intShippingService4AddCost = null;
                        String intShippingService4Locations = null;
                        String intShippingService5Option = null;
                        String intShippingService5Cost = null;
                        String intShippingService5AddCost = null;
                        String intShippingService5Locations = null;
                        String dispatchTimeMax = mv.getString("dispatchTimeMax");
                        
                        //Shipping == START
                        double productPrice = 0.0;
                        if (UtilValidate.isNotEmpty(pmSingleCheck)) {  //if pm is not empty -- START
                            if (site.equals("US")) {
                                productPrice = pmSingleCheck.getBigDecimal("priceUsd").doubleValue();
                            } else if (site.equals("AU")) {
                                productPrice = pmSingleCheck.getBigDecimal("priceAud").doubleValue();
                            } else if (site.equals("CA")) {
                                productPrice = pmSingleCheck.getBigDecimal("priceCad").doubleValue();
                            } else if (site.equals("UK")) {
                                productPrice = pmSingleCheck.getBigDecimal("priceGbp").doubleValue();
                            } else if (site.equals("DE")) {
                                productPrice = pmSingleCheck.getBigDecimal("priceEur").doubleValue();
                            } else if (site.equals("FR")) {
                                productPrice = pmSingleCheck.getBigDecimal("priceEur").doubleValue();
                            } else if (site.equals("ES")) {
                                productPrice = pmSingleCheck.getBigDecimal("priceEur").doubleValue();
                            } else if (site.equals("IT")) {
                                productPrice = pmSingleCheck.getBigDecimal("priceEur").doubleValue();
                            }
                            
                            if (productPrice < 5) {
                                quantity = "20";
                            } else if (productPrice >= 5 && productPrice < 30) {
                                quantity = "5";
                            } else if (productPrice >= 30) {
                                quantity = "3";
                            } else {
                                quantity = "5";
                            }
                            
                            if (pmSingleCheck.getString("productGroup").equals("G4") || pmSingleCheck.getString("productGroup").equals("G5")) {
                                quantity = "0";
                            }
                            
                            if (productPrice <= siteLowestPrice) {
                                productPrice = siteLowestPrice;
                            }
                            buyItNowPrice = productPrice + "";
                            
                        }   //if pm is not empty -- END
                        
                        String filter = "ALL";
                        if (site.equals("US")) {
                            if (productPrice < 5 || productPrice > 20) {
                                filter = "LESS5GREAT20";
                            } else {
                                filter = "5AND20";
                            }
                        }
                        String intFilter = "ALL";
                        if (site.equals("CA")) {
                            if(productPrice < 6 || productPrice > 25) {
                                filter = "LESS6GREAT25";
                            } else {
                                filter = "6AND25";
                            }
                        }
                        
                        //Debug.logError("Filter is " + filter, module);
                        List<GenericValue> domesticList = delegator.findByAnd("AccountListingRuleShipping", UtilMisc.toMap("site", site, "shippingType", "DOMESTIC", "filter", filter), null, false);
                        //Debug.logError("domesticList size is " + domesticList.size(), module);
                        for (GenericValue domestic : domesticList) {    //loop domesticList -- START
                            String priority = domestic.getString("priority");
                            if (priority.equals("1")) {
                                shippingService1Option = domestic.getString("shippingServiceName");
                                shippingService1Cost = domestic.getString("shippingServiceCost");
                                shippingService1AddCost = domestic.getString("additionalCost");
                                dispatchTimeMax = domestic.getString("eta");
                            } else if (priority.equals("2")) {
                                shippingService2Option = domestic.getString("shippingServiceName");
                                shippingService2Cost = domestic.getString("shippingServiceCost");
                                shippingService2AddCost = domestic.getString("additionalCost");
                            } else if (priority.equals("3")) {
                                shippingService3Option = domestic.getString("shippingServiceName");
                                shippingService3Cost = domestic.getString("shippingServiceCost");
                                shippingService3AddCost = domestic.getString("additionalCost");
                            } else if (priority.equals("4")) {
                                shippingService4Option = domestic.getString("shippingServiceName");
                                shippingService4Cost = domestic.getString("shippingServiceCost");
                                shippingService4AddCost = domestic.getString("additionalCost");
                            }
                        }   //loop domesticList -- END
                        //Debug.logError("initial shippingService1Cost " + shippingService1Cost, module);
                        List<GenericValue> intList = delegator.findByAnd("AccountListingRuleShipping", UtilMisc.toMap("site", site, "shippingType", "INTERNATIONAL", "filter", intFilter), null, false);
                        for (GenericValue intern : intList) {   //loop intList -- START
                            String priority = intern.getString("priority");
                            if (priority.equals("1")) {
                                intShippingService1Option = intern.getString("shippingServiceName");
                                intShippingService1Cost = intern.getString("shippingServiceCost");
                                intShippingService1AddCost = intern.getString("additionalCost");
                                intShippingService1Locations = intern.getString("destination");
                            } else if (priority.equals("2")) {
                                intShippingService2Option = intern.getString("shippingServiceName");
                                intShippingService2Cost = intern.getString("shippingServiceCost");
                                intShippingService2AddCost = intern.getString("additionalCost");
                                intShippingService2Locations = intern.getString("destination");
                            } else if (priority.equals("3")) {
                                intShippingService3Option = intern.getString("shippingServiceName");
                                intShippingService3Cost = intern.getString("shippingServiceCost");
                                intShippingService3AddCost = intern.getString("additionalCost");
                                intShippingService3Locations = intern.getString("destination");
                            } else if (priority.equals("4")) {
                                intShippingService4Option = intern.getString("shippingServiceName");
                                intShippingService4Cost = intern.getString("shippingServiceCost");
                                intShippingService4AddCost = intern.getString("additionalCost");
                                intShippingService4Locations = intern.getString("destination");
                            } else if (priority.equals("5")) {
                                intShippingService5Option = intern.getString("shippingServiceName");
                                intShippingService5Cost = intern.getString("shippingServiceCost");
                                intShippingService5AddCost = intern.getString("additionalCost");
                                intShippingService5Locations = intern.getString("destination");
                            }
                        }   //loop intList -- END
                        
                        if (!folderName.equals("ST1FREE") && !folderName.equals("ST2FREE")) { //if NON FREE folder -- START
                            if (productPrice > 1 && productPrice < 2) {
                                buyItNowPrice = "1";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + productPrice  - 1 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + productPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + productPrice  - 1 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + productPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + productPrice  - 1 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + productPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + productPrice  - 1 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + productPrice  - 1 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + productPrice  - 1 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + productPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + productPrice  - 1 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + productPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + productPrice  - 1 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + productPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + productPrice  - 1 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + productPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + productPrice  - 1 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + productPrice  - 1 + "";
                                }
                            } else if (productPrice >= 2 && productPrice < 5) {
                                buyItNowPrice = (Double.parseDouble(buyItNowPrice) - 1)  + "";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + 1 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + 1 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + 1 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + 1 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + 1 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + 1 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + 1 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + 1 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + 1 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + 1 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + 1 + "";
                                }
                                
                            } else if (productPrice >= 5 && productPrice < 10) {
                                buyItNowPrice = (Double.parseDouble(buyItNowPrice) - 3)  + "";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + 3 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + 3 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + 3 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + 3 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + 3 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + 3 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + 3 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + 3 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + 3 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + 3 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + 3 + "";
                                }
                            } else if (productPrice >= 10 && productPrice < 20) {
                                buyItNowPrice = (Double.parseDouble(buyItNowPrice) - 5)  + "";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + 5 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + 5 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + 5 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + 5 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + 5 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + 5 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + 5 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + 5 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + 5 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + 5 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + 5 + "";
                                }
                            } else if (productPrice >= 20 && productPrice < 40) {
                                buyItNowPrice = (Double.parseDouble(buyItNowPrice) - 10)  + "";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + 10 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + 10 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + 10 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + 10 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + 10 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + 10 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + 10 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + 10 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + 10 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + 10 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + 10 + "";
                                }
                            } else if (productPrice >= 40) {
                                buyItNowPrice = (Double.parseDouble(buyItNowPrice) - 20)  + "";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + 20 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + 20 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + 20 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + 20 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + 20 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + 20 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + 20 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + 20 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + 20 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + 20 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + 20 + "";
                                }
                            }
                        }   //if NON FREE folder -- END
                        
                        //Shipping == END
                        
                        sku = skuPrefix + mv.getString("sku") + skuSuffix;
                        String id = mv.getString("id");
                        String yunqudaoFolderId = mv.getString("yunqudaoFolderId");
                        String format = mv.getString("format");
                        String subtitle = mv.getString("subtitle");
                        String tags = mv.getString("tags");
                        String relationship = mv.getString("relationship");
                        String relationshipDetails = mv.getString("relationshipDetails");
                        String qtyRestrictionPerBuyerMax = mv.getString("qtyRestrictionPerBuyerMax");
                        String startPrice = mv.getString("startPrice");
                        String reservePrice = mv.getString("reservePrice");
                        String bestOfferAutoAcceptPrice = mv.getString("bestOfferAutoAcceptPrice");
                        String minBestOfferPrice = mv.getString("minBestOfferPrice");
                        String picUrl = mv.getString("picUrl");
                        String yunqudaoImageLayout = mv.getString("yunqudaoImageLayout");
                        String itemSpecific = mv.getString("itemSpecific");
                        String condition = mv.getString("condition");
                        String conditionDescription = mv.getString("conditionDescription");
                        String category = mv.getString("category");
                        String category2 = mv.getString("category2");
                        
                        String storeCategory2 = mv.getString("storeCategory2");
                        String descriptionFile = mv.getString("descriptionFile");
                        String description = mv.getString("description");
                        String description1 = mv.getString("description1");
                        String description2 = mv.getString("description2");
                        String description3 = mv.getString("description3");
                        String yunqudaoTemplateId = mv.getString("yunqudaoTemplateId");
                        String yunqudaoSellerDetailId = mv.getString("yunqudaoSellerDetailId");
                        String yunqudaoCounter = mv.getString("yunqudaoCounter");
                        String listingEnhancement = mv.getString("listingEnhancement");
                        String country = mv.getString("country");
                        String shippingRateTable = mv.getString("shippingRateTable");
                        String intShippingRateTable = mv.getString("intShippingRateTable");
                        String excludeShipToLocation = mv.getString("excludeShipToLocation");
                        String payPalEmailAddress = mv.getString("payPalEmailAddress");
                        String immediatePayRequired = mv.getString("immediatePayRequired");
                        String payMoneyXferAccInCheckout = mv.getString("payMoneyXferAccInCheckout");
                        String paymentInstructions = mv.getString("paymentInstructions");
                        String returnsAcceptedOption = mv.getString("returnsAcceptedOption");
                        String returnsWithinOption = mv.getString("returnsWithinOption");
                        String refundOption = mv.getString("refundOption");
                        String shippingCostPaidByOption = mv.getString("shippingCostPaidByOption");
                        String returnPolicyDescription = mv.getString("returnPolicyDescription");
                        String bRMaxBuyerPolicyVioCount = mv.getString("bRMaxBuyerPolicyVioCount");
                        String bRMaxBuyerPolicyVioPeriod = mv.getString("bRMaxBuyerPolicyVioPeriod");
                        String bRMaxItemReqMaxItemCount = mv.getString("bRMaxItemReqMaxItemCount");
                        String bRMaxItemReqMinFeedbackScore = mv.getString("bRMaxItemReqMinFeedbackScore");
                        String bRMaxUnpaidStrikesInfoCount = mv.getString("bRMaxUnpaidStrikesInfoCount");
                        String bRMaxUnpaidStrikesInfoPeriod = mv.getString("bRMaxUnpaidStrikesInfoPeriod");
                        String bRMinFeedbackScore = mv.getString("bRMinFeedbackScore");
                        String bRShipToRegistrationCountry = "TRUE";
                        String yunqudaoAutoRelistId = mv.getString("yunqudaoAutoRelistId");
                        String yunqudaoAutoReplenishId = mv.getString("yunqudaoAutoReplenishId");
                        String yunqudaoShowcaseId = mv.getString("yunqudaoShowcaseId");
                        
                        Map<String, Object> resultMotherVersionMap = FastMap.newInstance();
                        resultMotherVersionMap.put("account", productStoreId);
                        resultMotherVersionMap.put("marketplace", site);
                        resultMotherVersionMap.put("id", id);
                        resultMotherVersionMap.put("yunqudaoListingId", yunqudaoListingId);
                        resultMotherVersionMap.put("yunqudaoFolderId", yunqudaoFolderId);
                        resultMotherVersionMap.put("ebayAccountName", ebayAccountName);
                        resultMotherVersionMap.put("sku", sku);
                        resultMotherVersionMap.put("siteId", siteId);
                        resultMotherVersionMap.put("format", format);
                        resultMotherVersionMap.put("title", title);
                        resultMotherVersionMap.put("subtitle", subtitle);
                        resultMotherVersionMap.put("tags", tags);
                        resultMotherVersionMap.put("relationship", relationship);
                        resultMotherVersionMap.put("relationshipDetails", relationshipDetails);
                        resultMotherVersionMap.put("quantity", quantity);
                        resultMotherVersionMap.put("qtyRestrictionPerBuyerMax", qtyRestrictionPerBuyerMax);
                        resultMotherVersionMap.put("currency", currency);
                        resultMotherVersionMap.put("startPrice", startPrice);
                        resultMotherVersionMap.put("reservePrice", reservePrice);
                        resultMotherVersionMap.put("buyItNowPrice", buyItNowPrice);
                        resultMotherVersionMap.put("duration", duration);
                        resultMotherVersionMap.put("privateListing", privateListing);
                        resultMotherVersionMap.put("bestOfferAutoAcceptPrice", bestOfferAutoAcceptPrice);
                        resultMotherVersionMap.put("minBestOfferPrice", minBestOfferPrice);
                        resultMotherVersionMap.put("picUrl", picUrl);
                        resultMotherVersionMap.put("yunqudaoImageLayout", yunqudaoImageLayout);
                        resultMotherVersionMap.put("uploadImageEps", uploadImageEps);
                        resultMotherVersionMap.put("showImageInDesc", showImageInDesc);
                        resultMotherVersionMap.put("itemSpecific", itemSpecific);
                        resultMotherVersionMap.put("condition", condition);
                        resultMotherVersionMap.put("conditionDescription", conditionDescription);
                        resultMotherVersionMap.put("category", category);
                        resultMotherVersionMap.put("category2", category2);
                        resultMotherVersionMap.put("storeCategory", storeCategory);
                        resultMotherVersionMap.put("storeCategory2", storeCategory2);
                        resultMotherVersionMap.put("upc", upc);
                        resultMotherVersionMap.put("ean", ean);
                        resultMotherVersionMap.put("isbn", isbn);
                        resultMotherVersionMap.put("brandMpnBrand", brandMpnBrand);
                        resultMotherVersionMap.put("brandMpnMpn", brandMpnMpn);
                        resultMotherVersionMap.put("descriptionFile", descriptionFile);
                        resultMotherVersionMap.put("description", description);
                        resultMotherVersionMap.put("description1", description1);
                        resultMotherVersionMap.put("description2", description2);
                        resultMotherVersionMap.put("description3", description3);
                        resultMotherVersionMap.put("yunqudaoTemplateId", yunqudaoTemplateId);
                        resultMotherVersionMap.put("yunqudaoSellerDetailId", yunqudaoSellerDetailId);
                        resultMotherVersionMap.put("yunqudaoCounter", yunqudaoCounter);
                        resultMotherVersionMap.put("listingEnhancement", listingEnhancement);
                        resultMotherVersionMap.put("country", country);
                        resultMotherVersionMap.put("location", location);
                        resultMotherVersionMap.put("dispatchTimeMax", dispatchTimeMax);
                        resultMotherVersionMap.put("getItFast", getItFast);
                        resultMotherVersionMap.put("shippingService1Option", shippingService1Option);
                        resultMotherVersionMap.put("shippingService1Cost", shippingService1Cost);
                        resultMotherVersionMap.put("shippingService1AddCost", shippingService1AddCost);
                        resultMotherVersionMap.put("shippingService2Option", shippingService2Option);
                        resultMotherVersionMap.put("shippingService2Cost", shippingService2Cost);
                        resultMotherVersionMap.put("shippingService2AddCost", shippingService2AddCost);
                        resultMotherVersionMap.put("shippingService3Option", shippingService3Option);
                        resultMotherVersionMap.put("shippingService3Cost", shippingService3Cost);
                        resultMotherVersionMap.put("shippingService3AddCost", shippingService3AddCost);
                        resultMotherVersionMap.put("shippingService4Option", shippingService4Option);
                        resultMotherVersionMap.put("shippingService4Cost", shippingService4Cost);
                        resultMotherVersionMap.put("shippingService4AddCost", shippingService4AddCost);
                        resultMotherVersionMap.put("shippingRateTable", shippingRateTable);
                        resultMotherVersionMap.put("intShippingService1Option", intShippingService1Option);
                        resultMotherVersionMap.put("intShippingService1Cost", intShippingService1Cost);
                        resultMotherVersionMap.put("intShippingService1AddCost", intShippingService1AddCost);
                        resultMotherVersionMap.put("intShippingService1Locations", intShippingService1Locations);
                        resultMotherVersionMap.put("intShippingService2Option", intShippingService2Option);
                        resultMotherVersionMap.put("intShippingService2Cost", intShippingService2Cost);
                        resultMotherVersionMap.put("intShippingService2AddCost", intShippingService2AddCost);
                        resultMotherVersionMap.put("intShippingService2Locations", intShippingService2Locations);
                        resultMotherVersionMap.put("intShippingService3Option", intShippingService3Option);
                        resultMotherVersionMap.put("intShippingService3Cost", intShippingService3Cost);
                        resultMotherVersionMap.put("intShippingService3AddCost", intShippingService3AddCost);
                        resultMotherVersionMap.put("intShippingService3Locations", intShippingService3Locations);
                        resultMotherVersionMap.put("intShippingService4Option", intShippingService4Option);
                        resultMotherVersionMap.put("intShippingService4Cost", intShippingService4Cost);
                        resultMotherVersionMap.put("intShippingService4AddCost", intShippingService4AddCost);
                        resultMotherVersionMap.put("intShippingService4Locations", intShippingService4Locations);
                        resultMotherVersionMap.put("intShippingService5Option", intShippingService5Option);
                        resultMotherVersionMap.put("intShippingService5Cost", intShippingService5Cost);
                        resultMotherVersionMap.put("intShippingService5AddCost", intShippingService5AddCost);
                        resultMotherVersionMap.put("intShippingService5Locations", intShippingService5Locations);
                        resultMotherVersionMap.put("intShippingRateTable", intShippingRateTable);
                        resultMotherVersionMap.put("excludeShipToLocation", excludeShipToLocation);
                        resultMotherVersionMap.put("payPalEmailAddress", payPalEmailAddress);
                        resultMotherVersionMap.put("immediatePayRequired", immediatePayRequired);
                        resultMotherVersionMap.put("payMoneyXferAccInCheckout", payMoneyXferAccInCheckout);
                        resultMotherVersionMap.put("paymentInstructions", paymentInstructions);
                        resultMotherVersionMap.put("returnsAcceptedOption", returnsAcceptedOption);
                        resultMotherVersionMap.put("returnsWithinOption", returnsWithinOption);
                        resultMotherVersionMap.put("refundOption", refundOption);
                        resultMotherVersionMap.put("shippingCostPaidByOption", shippingCostPaidByOption);
                        resultMotherVersionMap.put("returnPolicyDescription", returnPolicyDescription);
                        resultMotherVersionMap.put("extendedHolidayReturns", extendedHolidayReturns);
                        resultMotherVersionMap.put("bRLinkedPayPalAccount", bRLinkedPayPalAccount);
                        resultMotherVersionMap.put("bRMaxBuyerPolicyVioCount", bRMaxBuyerPolicyVioCount);
                        resultMotherVersionMap.put("bRMaxBuyerPolicyVioPeriod", bRMaxBuyerPolicyVioPeriod);
                        resultMotherVersionMap.put("bRMaxItemReqMaxItemCount", bRMaxItemReqMaxItemCount);
                        resultMotherVersionMap.put("bRMaxItemReqMinFeedbackScore", bRMaxItemReqMinFeedbackScore);
                        resultMotherVersionMap.put("bRMaxUnpaidStrikesInfoCount", bRMaxUnpaidStrikesInfoCount);
                        resultMotherVersionMap.put("bRMaxUnpaidStrikesInfoPeriod", bRMaxUnpaidStrikesInfoPeriod);
                        resultMotherVersionMap.put("bRMinFeedbackScore", bRMinFeedbackScore);
                        resultMotherVersionMap.put("bRShipToRegistrationCountry", bRShipToRegistrationCountry);
                        resultMotherVersionMap.put("yunqudaoAutoRelistId", yunqudaoAutoRelistId);
                        resultMotherVersionMap.put("yunqudaoAutoReplenishId", yunqudaoAutoReplenishId);
                        resultMotherVersionMap.put("yunqudaoShowcaseId", yunqudaoShowcaseId);
                        resultMotherVersionMap.put("userLogin", userLogin);
                        
                        Map createMotherVersionResult = dispatcher.runSync("createMotherVersionResult", resultMotherVersionMap);
                    }   //addListingId for single listing == END
                }   //loop checkMotherVersionSingleList == END ==
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                //Multivariation listing == START
                EntityCondition multivariationCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                        EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, "US"),
                                                                                                        EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, "ST1FREE"),
                                                                                                        EntityCondition.makeCondition("format", EntityOperator.EQUALS, "多属性"),
                                                                                                        EntityCondition.makeCondition("yunqudaoListingIdVar", EntityOperator.EQUALS, "7ff3b039-a653-4163-810f-c395bf2a844e")
                                                                                                        ));
                List<GenericValue> multiVariationList = delegator.findList("MotherVersion", multivariationCondition, null, UtilMisc.toList("yunqudaoListingIdVar"), null, false);
                Debug.logError("multiVariationList size: " + multiVariationList.size(), module);
                for (GenericValue multiVariation : multiVariationList) {    //loop multiVariationList == START
                    String storeCategory = null;
                    String yunqudaoListingIdVar = multiVariation.getString("yunqudaoListingIdVar");
                    double lowestChildPrice = 10000000000.0;
                    Debug.logError("MotherListing: " + yunqudaoListingIdVar + " - START", module);
                    List<Map> childMapList = new LinkedList<Map>();
                    EntityCondition childCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                   EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site),
                                                                                                   EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName),
                                                                                                   EntityCondition.makeCondition("yunqudaoListingIdVar", EntityOperator.EQUALS, yunqudaoListingIdVar)
                                                                                                   ));
                    List<GenericValue> childList = delegator.findList("MotherVersion", childCondition, null, UtilMisc.toList("id"), null, false);
                    Debug.logError("run until here", module);
                    for (GenericValue child : childList) {  //loop childList == START
                        Debug.logError("Start loop childlist", module);
                        String childYunqudaoListingId = child.getString("yunqudaoListingId");
                        Debug.logError("Start loop childlist 1", module);
                        if (UtilValidate.isEmpty(childYunqudaoListingId)) {   //if yungudaoListingId is empty == START
                            Map<String, String> childMap = FastMap.newInstance();
                            boolean addToChildMap = false;
                            Debug.logError("Start loop childlist 2", module);
                            if (true) {    //if child SKU not in activeSkuUnique == START
                                Debug.logError("Start loop childlist 3, " + child.getString("sku"), module);
                                GenericValue pmChildrenCheck = delegator.findOne("ProductMasterResult", UtilMisc.toMap("productId", child.getString("sku")), false);
                                
                                
                                Debug.logError("run until here 3", module);
                                if (UtilValidate.isNotEmpty(pmChildrenCheck)) {   //if pmChildrenCheck is not empty == START
                                    Debug.logError("run until here 4", module);
                                    String pmChildrenCheckProductGroup = pmChildrenCheck.getString("productGroup");
                                    String pmChildrenCheckRisk = pmChildrenCheck.getString("risk");
                                    String pmChildrenCheckStatusId = pmChildrenCheck.getString("statusId");
                                    if (UtilValidate.isEmpty(storeCategory)) {
                                        GenericValue gudaoCategoryMap = delegator.findOne("GudaoCategoryMap", UtilMisc.toMap("productCategory", pmChildrenCheck.getString("categoryIdParent"), "platform", "EBAY", "account", productStoreId), true);
                                        if (UtilValidate.isNotEmpty(gudaoCategoryMap)) {
                                            storeCategory = gudaoCategoryMap.getString("categoryId");
                                        }
                                    }

                                    if (UtilValidate.isNotEmpty(categoryList)) {    //if categoryList is not empty == START
                                        Debug.logError("run until here 5", module);
                                        String singleCategoryIdParent = pmChildrenCheck.getString("categoryIdParent");
                                        Debug.logError("run until here 6: " + singleCategoryIdParent, module);
                                        if (UtilValidate.isNotEmpty(singleCategoryIdParent)) { // if singleCategoryIdParent is NOT EMPTY == START
                                            Debug.logError("run until here 7", module);
                                            if(categoryList.contains(singleCategoryIdParent)) {    //category matched categoryList == START
                                                Debug.logError("run until here 8", module);
                                                if (!pmChildrenCheckProductGroup.equals("G4") && !pmChildrenCheckProductGroup.equals("G5") && !pmChildrenCheckRisk.equals("高") && safeStatusList.contains(pmChildrenCheckStatusId)) {   //general safe check == START
                                                    Debug.logError("run until here 9", module);
                                                    addToChildMap = true;
                                                    if (site.equals("US")) {
                                                        if (pmChildrenCheckStatusId.equals("US不上") || pmChildrenCheckStatusId.equals("只上EU")) {
                                                            addToChildMap = false;
                                                        }
                                                    } else if (site.equals("UK")) {
                                                        if (pmChildrenCheckStatusId.equals("UK不上") || pmChildrenCheckStatusId.equals("只上EU")) {
                                                            addToChildMap = false;
                                                        }
                                                    } else if (site.equals("AU")) {
                                                        if (pmChildrenCheckStatusId.equals("AU不上") || pmChildrenCheckStatusId.equals("只上EU")) {
                                                            addToChildMap = false;
                                                        }
                                                    } else if (site.equals("CA")) {
                                                        continue;
                                                    } else {
                                                        if (pmChildrenCheckStatusId.equals("EU不上")) {
                                                            addToChildMap = false;
                                                        }
                                                    }
                                                    
                                                    if (dangerAccount) {    //dangerous account == START
                                                        if (!pmChildrenCheckRisk.equals("低")) {   //check risk == START
                                                            addToChildMap = false;
                                                        }  //check risk == END
                                                    }   //dangerous account == END
                                                }   //general safe check == END
                                            }   //category matched categoryList == END
                                        }   // if singleCategoryIdParent is NOT EMPTY == END
                                    }   //if categoryList is not empty == END
                                }   //if pmChildrenCheck is not empty == END
                                
                                Debug.logError("this RUN and addToChildMap is " + addToChildMap, module);
                                if (addToChildMap) {    //if addToChildMap TRUE == START
                                    Debug.logError("Product Id " + child.getString("sku") + " start add", module);
                                    double childProductPrice = 0.0;
                                    if (site.equals("US")) {
                                        childProductPrice = pmChildrenCheck.getBigDecimal("priceUsd").doubleValue();
                                    } else if (site.equals("AU")) {
                                        childProductPrice = pmChildrenCheck.getBigDecimal("priceAud").doubleValue();
                                    } else if (site.equals("CA")) {
                                        childProductPrice = pmChildrenCheck.getBigDecimal("priceCad").doubleValue();
                                    } else if (site.equals("UK")) {
                                        childProductPrice = pmChildrenCheck.getBigDecimal("priceGbp").doubleValue();
                                    } else if (site.equals("DE")) {
                                        childProductPrice = pmChildrenCheck.getBigDecimal("priceEur").doubleValue();
                                    } else if (site.equals("FR")) {
                                        childProductPrice = pmChildrenCheck.getBigDecimal("priceEur").doubleValue();
                                    } else if (site.equals("ES")) {
                                        childProductPrice = pmChildrenCheck.getBigDecimal("priceEur").doubleValue();
                                    } else if (site.equals("IT")) {
                                        childProductPrice = pmChildrenCheck.getBigDecimal("priceEur").doubleValue();
                                    }
                                    //Debug.logError("childProductPrice: " + childProductPrice, module);
                                    if (lowestChildPrice >= childProductPrice) {
                                        lowestChildPrice = childProductPrice;
                                    }
                                    
                                    if (lowestChildPrice <= siteLowestPrice) {
                                        lowestChildPrice = siteLowestPrice;
                                    }
                                    childMap.put("productId", pmChildrenCheck.getString("productId"));
                                    childMap.put("childProductPrice", childProductPrice + "");
                                    childMap.put("id", child.getString("id"));
                                    childMap.put("relationship", child.getString("relationship"));
                                    childMap.put("relationshipDetails", child.getString("relationshipDetails"));
                                    childMap.put("picUrl", child.getString("picUrl"));
                                    childMapList.add(childMap);
                                    Debug.logError("Product Id " + child.getString("sku") + " END add", module);
                                }   //if addToChildMap TRUE == END
                                Debug.logError("this RUN : end of child SKU not in activeSKUUnique", module);
                            }   //if child SKU not in activeSkuUnique == END
                            Debug.logError("this RUN : end of yunqudaoListingId is empty", module);
                        }   //if yungudaoListingId is empty == END
                        Debug.logError("this RUN : End of childList loop", module);
                    }   //loop childList == END
                    
                    //Debug.logError("yunqudaoListingIdVar: " + yunqudaoListingIdVar + " - childMapList size: " + childMapList.size(), module);
                    Debug.logError("this RUN after addToChildMap", module);
                    if (childMapList.size() > 0) {  //Create motherVersionResult == START
                        String quantity = multiVariation.getString("quantity");
                        String currency = multiVariation.getString("currency");
                        String sku = multiVariation.getString("sku");
                        String buyItNowPrice = multiVariation.getString("buyItNowPrice");
                        String ebayAccountName = productStoreId;
                        String siteId = multiVariation.getString("siteId");
                        String title = multiVariation.getString("title").substring(0, multiVariation.getString("title").length() - 4) + " " + titleSuffix;
                        String duration = "GTC";
                        String privateListing = "FALSE";
                        String uploadImageEps = "TRUE";
                        String showImageInDesc = "TRUE";
                        String upc = "";
                        String ean = "";
                        String isbn = "";
                        String brandMpnBrand = "";
                        String brandMpnMpn = "";
                        String location = "China";
                        String getItFast = "FALSE";
                        String extendedHolidayReturns = "TRUE";
                        String bRLinkedPayPalAccount = "FALSE";
                        String shippingService1Option = null;
                        String shippingService1Cost = null;
                        String shippingService1AddCost = null;
                        String shippingService2Option = null;
                        String shippingService2Cost = null;
                        String shippingService2AddCost = null;
                        String shippingService3Option = null;
                        String shippingService3Cost = null;
                        String shippingService3AddCost = null;
                        String shippingService4Option = null;
                        String shippingService4Cost = null;
                        String shippingService4AddCost = null;
                        String intShippingService1Option = null;
                        String intShippingService1Cost = null;
                        String intShippingService1AddCost = null;
                        String intShippingService1Locations = null;
                        String intShippingService2Option = null;
                        String intShippingService2Cost = null;
                        String intShippingService2AddCost = null;
                        String intShippingService2Locations = null;
                        String intShippingService3Option = null;
                        String intShippingService3Cost = null;
                        String intShippingService3AddCost = null;
                        String intShippingService3Locations = null;
                        String intShippingService4Option = null;
                        String intShippingService4Cost = null;
                        String intShippingService4AddCost = null;
                        String intShippingService4Locations = null;
                        String intShippingService5Option = null;
                        String intShippingService5Cost = null;
                        String intShippingService5AddCost = null;
                        String intShippingService5Locations = null;
                        String dispatchTimeMax = multiVariation.getString("dispatchTimeMax");
                        
                        //Debug.logError("lowestChildPrice: " + lowestChildPrice, module);
                        String filter = "ALL";
                        if (site.equals("US")) {
                            if (lowestChildPrice < 5 || lowestChildPrice > 20) {
                                filter = "LESS5GREAT20";
                            } else {
                                filter = "5AND20";
                            }
                        }
                        String intFilter = "ALL";
                        if (site.equals("CA")) {
                            if(lowestChildPrice < 6 || lowestChildPrice > 25) {
                                filter = "LESS6GREAT25";
                            } else {
                                filter = "6AND25";
                            }
                        }
                        
                        List<GenericValue> domesticList = delegator.findByAnd("AccountListingRuleShipping", UtilMisc.toMap("site", site, "shippingType", "DOMESTIC", "filter", filter), null, false);
                        //Debug.logError("domesticList size is " + domesticList.size(), module);
                        for (GenericValue domestic : domesticList) {    //loop domesticList -- START
                            String priority = domestic.getString("priority");
                            if (priority.equals("1")) {
                                shippingService1Option = domestic.getString("shippingServiceName");
                                shippingService1Cost = domestic.getString("shippingServiceCost");
                                shippingService1AddCost = domestic.getString("additionalCost");
                                dispatchTimeMax = domestic.getString("eta");
                            } else if (priority.equals("2")) {
                                shippingService2Option = domestic.getString("shippingServiceName");
                                shippingService2Cost = domestic.getString("shippingServiceCost");
                                shippingService2AddCost = domestic.getString("additionalCost");
                            } else if (priority.equals("3")) {
                                shippingService3Option = domestic.getString("shippingServiceName");
                                shippingService3Cost = domestic.getString("shippingServiceCost");
                                shippingService3AddCost = domestic.getString("additionalCost");
                            } else if (priority.equals("4")) {
                                shippingService4Option = domestic.getString("shippingServiceName");
                                shippingService4Cost = domestic.getString("shippingServiceCost");
                                shippingService4AddCost = domestic.getString("additionalCost");
                            }
                        }   //loop domesticList -- END
                        //Debug.logError("initial shippingService1Cost " + shippingService1Cost, module);
                        List<GenericValue> intList = delegator.findByAnd("AccountListingRuleShipping", UtilMisc.toMap("site", site, "shippingType", "INTERNATIONAL", "filter", intFilter), null, false);
                        for (GenericValue intern : intList) {   //loop intList -- START
                            String priority = intern.getString("priority");
                            if (priority.equals("1")) {
                                intShippingService1Option = intern.getString("shippingServiceName");
                                intShippingService1Cost = intern.getString("shippingServiceCost");
                                intShippingService1AddCost = intern.getString("additionalCost");
                                intShippingService1Locations = intern.getString("destination");
                            } else if (priority.equals("2")) {
                                intShippingService2Option = intern.getString("shippingServiceName");
                                intShippingService2Cost = intern.getString("shippingServiceCost");
                                intShippingService2AddCost = intern.getString("additionalCost");
                                intShippingService2Locations = intern.getString("destination");
                            } else if (priority.equals("3")) {
                                intShippingService3Option = intern.getString("shippingServiceName");
                                intShippingService3Cost = intern.getString("shippingServiceCost");
                                intShippingService3AddCost = intern.getString("additionalCost");
                                intShippingService3Locations = intern.getString("destination");
                            } else if (priority.equals("4")) {
                                intShippingService4Option = intern.getString("shippingServiceName");
                                intShippingService4Cost = intern.getString("shippingServiceCost");
                                intShippingService4AddCost = intern.getString("additionalCost");
                                intShippingService4Locations = intern.getString("destination");
                            } else if (priority.equals("5")) {
                                intShippingService5Option = intern.getString("shippingServiceName");
                                intShippingService5Cost = intern.getString("shippingServiceCost");
                                intShippingService5AddCost = intern.getString("additionalCost");
                                intShippingService5Locations = intern.getString("destination");
                            }
                        }   //loop intList -- END
                        
                        buyItNowPrice = lowestChildPrice + "";
                        
                        if (!folderName.equals("ST1FREE") && !folderName.equals("ST2FREE")) { //if NON FREE folder -- START
                            //Debug.logError("lowestChildPrice: " + lowestChildPrice, module);
                            if (lowestChildPrice > 1 && lowestChildPrice < 2) {
                                buyItNowPrice = "1";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + lowestChildPrice  - 1 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + lowestChildPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + lowestChildPrice  - 1 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + lowestChildPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + lowestChildPrice  - 1 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + lowestChildPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + lowestChildPrice  - 1 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + lowestChildPrice  - 1 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + lowestChildPrice  - 1 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + lowestChildPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + lowestChildPrice  - 1 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + lowestChildPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + lowestChildPrice  - 1 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + lowestChildPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + lowestChildPrice  - 1 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + lowestChildPrice  - 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + lowestChildPrice  - 1 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + lowestChildPrice  - 1 + "";
                                }
                            } else if (lowestChildPrice >= 2 && lowestChildPrice < 5) {
                                buyItNowPrice = (Double.parseDouble(buyItNowPrice) - 1)  + "";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + 1 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + 1 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + 1 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + 1 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + 1 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + 1 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + 1 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + 1 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + 1 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + 1 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + 1 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + 1 + "";
                                }
                                
                            } else if (lowestChildPrice >= 5 && lowestChildPrice < 10) {
                                buyItNowPrice = (Double.parseDouble(buyItNowPrice) - 3)  + "";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + 3 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + 3 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + 3 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + 3 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + 3 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + 3 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + 3 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + 3 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + 3 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + 3 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + 3 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + 3 + "";
                                }
                            } else if (lowestChildPrice >= 10 && lowestChildPrice < 20) {
                                buyItNowPrice = (Double.parseDouble(buyItNowPrice) - 5)  + "";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + 5 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + 5 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + 5 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + 5 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + 5 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + 5 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + 5 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + 5 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + 5 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + 5 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + 5 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + 5 + "";
                                }
                            } else if (lowestChildPrice >= 20 && lowestChildPrice < 40) {
                                buyItNowPrice = (Double.parseDouble(buyItNowPrice) - 10)  + "";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + 10 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + 10 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + 10 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + 10 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + 10 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + 10 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + 10 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + 10 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + 10 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + 10 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + 10 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + 10 + "";
                                }
                            } else if (lowestChildPrice >= 40) {
                                buyItNowPrice = (Double.parseDouble(buyItNowPrice) - 20)  + "";
                                if (UtilValidate.isNotEmpty(shippingService1Option)) {
                                    shippingService1Cost = Double.parseDouble(shippingService1Cost) + 20 + "";
                                    shippingService1AddCost = Double.parseDouble(shippingService1AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService2Option)) {
                                    shippingService2Cost = Double.parseDouble(shippingService2Cost) + 20 + "";
                                    shippingService2AddCost = Double.parseDouble(shippingService2AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService3Option)) {
                                    shippingService3Cost = Double.parseDouble(shippingService3Cost) + 20 + "";
                                    shippingService3AddCost = Double.parseDouble(shippingService3AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(shippingService4Option)) {
                                    shippingService4Cost = Double.parseDouble(shippingService4Cost) + 20 + "";
                                    shippingService4AddCost = Double.parseDouble(shippingService4AddCost) + 20 + "";
                                }
                                
                                if (UtilValidate.isNotEmpty(intShippingService1Option)) {
                                    intShippingService1Cost = Double.parseDouble(intShippingService1Cost) + 20 + "";
                                    intShippingService1AddCost = Double.parseDouble(intShippingService1AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService2Option)) {
                                    intShippingService2Cost = Double.parseDouble(intShippingService2Cost) + 20 + "";
                                    intShippingService2AddCost = Double.parseDouble(intShippingService2AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService3Option)) {
                                    intShippingService3Cost = Double.parseDouble(intShippingService3Cost) + 20 + "";
                                    intShippingService3AddCost = Double.parseDouble(intShippingService3AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService4Option)) {
                                    intShippingService4Cost = Double.parseDouble(intShippingService4Cost) + 20 + "";
                                    intShippingService4AddCost = Double.parseDouble(intShippingService4AddCost) + 20 + "";
                                }
                                if (UtilValidate.isNotEmpty(intShippingService5Option)) {
                                    intShippingService5Cost = Double.parseDouble(intShippingService5Cost) + 20 + "";
                                    intShippingService5AddCost = Double.parseDouble(intShippingService5AddCost) + 20 + "";
                                }
                            }
                        }   //if NON FREE folder -- END
                        //Debug.logError("BUYITNOWPRICE: " + buyItNowPrice, module);
                        
                        sku = skuPrefix + multiVariation.getString("sku") + skuSuffix;
                        String id = multiVariation.getString("id");
                        String yunqudaoFolderId = multiVariation.getString("yunqudaoFolderId");
                        String format = multiVariation.getString("format");
                        String subtitle = multiVariation.getString("subtitle");
                        String tags = multiVariation.getString("tags");
                        String relationship = multiVariation.getString("relationship");
                        String relationshipDetails = multiVariation.getString("relationshipDetails");
                        String qtyRestrictionPerBuyerMax = multiVariation.getString("qtyRestrictionPerBuyerMax");
                        String startPrice = multiVariation.getString("startPrice");
                        String reservePrice = multiVariation.getString("reservePrice");
                        String bestOfferAutoAcceptPrice = multiVariation.getString("bestOfferAutoAcceptPrice");
                        String minBestOfferPrice = multiVariation.getString("minBestOfferPrice");
                        String picUrl = multiVariation.getString("picUrl");
                        String yunqudaoImageLayout = multiVariation.getString("yunqudaoImageLayout");
                        String itemSpecific = multiVariation.getString("itemSpecific");
                        String condition = multiVariation.getString("condition");
                        String conditionDescription = multiVariation.getString("conditionDescription");
                        String category = multiVariation.getString("category");
                        String category2 = multiVariation.getString("category2");
                        String storeCategory2 = multiVariation.getString("storeCategory2");
                        String descriptionFile = multiVariation.getString("descriptionFile");
                        String description = multiVariation.getString("description");
                        String description1 = multiVariation.getString("description1");
                        String description2 = multiVariation.getString("description2");
                        String description3 = multiVariation.getString("description3");
                        String yunqudaoTemplateId = multiVariation.getString("yunqudaoTemplateId");
                        String yunqudaoSellerDetailId = multiVariation.getString("yunqudaoSellerDetailId");
                        String yunqudaoCounter = multiVariation.getString("yunqudaoCounter");
                        String listingEnhancement = multiVariation.getString("listingEnhancement");
                        String country = multiVariation.getString("country");
                        String shippingRateTable = multiVariation.getString("shippingRateTable");
                        String intShippingRateTable = multiVariation.getString("intShippingRateTable");
                        String excludeShipToLocation = multiVariation.getString("excludeShipToLocation");
                        String payPalEmailAddress = multiVariation.getString("payPalEmailAddress");
                        String immediatePayRequired = multiVariation.getString("immediatePayRequired");
                        String payMoneyXferAccInCheckout = multiVariation.getString("payMoneyXferAccInCheckout");
                        String paymentInstructions = multiVariation.getString("paymentInstructions");
                        String returnsAcceptedOption = multiVariation.getString("returnsAcceptedOption");
                        String returnsWithinOption = multiVariation.getString("returnsWithinOption");
                        String refundOption = multiVariation.getString("refundOption");
                        String shippingCostPaidByOption = multiVariation.getString("shippingCostPaidByOption");
                        String returnPolicyDescription = multiVariation.getString("returnPolicyDescription");
                        String bRMaxBuyerPolicyVioCount = multiVariation.getString("bRMaxBuyerPolicyVioCount");
                        String bRMaxBuyerPolicyVioPeriod = multiVariation.getString("bRMaxBuyerPolicyVioPeriod");
                        String bRMaxItemReqMaxItemCount = multiVariation.getString("bRMaxItemReqMaxItemCount");
                        String bRMaxItemReqMinFeedbackScore = multiVariation.getString("bRMaxItemReqMinFeedbackScore");
                        String bRMaxUnpaidStrikesInfoCount = multiVariation.getString("bRMaxUnpaidStrikesInfoCount");
                        String bRMaxUnpaidStrikesInfoPeriod = multiVariation.getString("bRMaxUnpaidStrikesInfoPeriod");
                        String bRMinFeedbackScore = multiVariation.getString("bRMinFeedbackScore");
                        String bRShipToRegistrationCountry = "TRUE";
                        String yunqudaoAutoRelistId = multiVariation.getString("yunqudaoAutoRelistId");
                        String yunqudaoAutoReplenishId = multiVariation.getString("yunqudaoAutoReplenishId");
                        String yunqudaoShowcaseId = multiVariation.getString("yunqudaoShowcaseId");
                        
                        Map<String, Object> resultMotherVersionMap = FastMap.newInstance();
                        resultMotherVersionMap.put("account", productStoreId);
                        resultMotherVersionMap.put("marketplace", site);
                        resultMotherVersionMap.put("id", id);
                        resultMotherVersionMap.put("yunqudaoListingId", yunqudaoListingIdVar);
                        resultMotherVersionMap.put("yunqudaoFolderId", yunqudaoFolderId);
                        resultMotherVersionMap.put("ebayAccountName", ebayAccountName);
                        resultMotherVersionMap.put("sku", sku);
                        resultMotherVersionMap.put("siteId", siteId);
                        resultMotherVersionMap.put("format", format);
                        resultMotherVersionMap.put("title", title);
                        resultMotherVersionMap.put("subtitle", subtitle);
                        resultMotherVersionMap.put("tags", tags);
                        resultMotherVersionMap.put("relationship", relationship);
                        resultMotherVersionMap.put("relationshipDetails", relationshipDetails);
                        resultMotherVersionMap.put("quantity", quantity);
                        resultMotherVersionMap.put("qtyRestrictionPerBuyerMax", qtyRestrictionPerBuyerMax);
                        resultMotherVersionMap.put("currency", currency);
                        resultMotherVersionMap.put("startPrice", startPrice);
                        resultMotherVersionMap.put("reservePrice", reservePrice);
                        resultMotherVersionMap.put("buyItNowPrice", buyItNowPrice);
                        resultMotherVersionMap.put("duration", duration);
                        resultMotherVersionMap.put("privateListing", privateListing);
                        resultMotherVersionMap.put("bestOfferAutoAcceptPrice", bestOfferAutoAcceptPrice);
                        resultMotherVersionMap.put("minBestOfferPrice", minBestOfferPrice);
                        resultMotherVersionMap.put("picUrl", picUrl);
                        resultMotherVersionMap.put("yunqudaoImageLayout", yunqudaoImageLayout);
                        resultMotherVersionMap.put("uploadImageEps", uploadImageEps);
                        resultMotherVersionMap.put("showImageInDesc", showImageInDesc);
                        resultMotherVersionMap.put("itemSpecific", itemSpecific);
                        resultMotherVersionMap.put("condition", condition);
                        resultMotherVersionMap.put("conditionDescription", conditionDescription);
                        resultMotherVersionMap.put("category", category);
                        resultMotherVersionMap.put("category2", category2);
                        resultMotherVersionMap.put("storeCategory", storeCategory);
                        resultMotherVersionMap.put("storeCategory2", storeCategory2);
                        resultMotherVersionMap.put("upc", upc);
                        resultMotherVersionMap.put("ean", ean);
                        resultMotherVersionMap.put("isbn", isbn);
                        resultMotherVersionMap.put("brandMpnBrand", brandMpnBrand);
                        resultMotherVersionMap.put("brandMpnMpn", brandMpnMpn);
                        resultMotherVersionMap.put("descriptionFile", descriptionFile);
                        resultMotherVersionMap.put("description", description);
                        resultMotherVersionMap.put("description1", description1);
                        resultMotherVersionMap.put("description2", description2);
                        resultMotherVersionMap.put("description3", description3);
                        resultMotherVersionMap.put("yunqudaoTemplateId", yunqudaoTemplateId);
                        resultMotherVersionMap.put("yunqudaoSellerDetailId", yunqudaoSellerDetailId);
                        resultMotherVersionMap.put("yunqudaoCounter", yunqudaoCounter);
                        resultMotherVersionMap.put("listingEnhancement", listingEnhancement);
                        resultMotherVersionMap.put("country", country);
                        resultMotherVersionMap.put("location", location);
                        resultMotherVersionMap.put("dispatchTimeMax", dispatchTimeMax);
                        resultMotherVersionMap.put("getItFast", getItFast);
                        resultMotherVersionMap.put("shippingService1Option", shippingService1Option);
                        resultMotherVersionMap.put("shippingService1Cost", shippingService1Cost);
                        resultMotherVersionMap.put("shippingService1AddCost", shippingService1AddCost);
                        resultMotherVersionMap.put("shippingService2Option", shippingService2Option);
                        resultMotherVersionMap.put("shippingService2Cost", shippingService2Cost);
                        resultMotherVersionMap.put("shippingService2AddCost", shippingService2AddCost);
                        resultMotherVersionMap.put("shippingService3Option", shippingService3Option);
                        resultMotherVersionMap.put("shippingService3Cost", shippingService3Cost);
                        resultMotherVersionMap.put("shippingService3AddCost", shippingService3AddCost);
                        resultMotherVersionMap.put("shippingService4Option", shippingService4Option);
                        resultMotherVersionMap.put("shippingService4Cost", shippingService4Cost);
                        resultMotherVersionMap.put("shippingService4AddCost", shippingService4AddCost);
                        resultMotherVersionMap.put("shippingRateTable", shippingRateTable);
                        resultMotherVersionMap.put("intShippingService1Option", intShippingService1Option);
                        resultMotherVersionMap.put("intShippingService1Cost", intShippingService1Cost);
                        resultMotherVersionMap.put("intShippingService1AddCost", intShippingService1AddCost);
                        resultMotherVersionMap.put("intShippingService1Locations", intShippingService1Locations);
                        resultMotherVersionMap.put("intShippingService2Option", intShippingService2Option);
                        resultMotherVersionMap.put("intShippingService2Cost", intShippingService2Cost);
                        resultMotherVersionMap.put("intShippingService2AddCost", intShippingService2AddCost);
                        resultMotherVersionMap.put("intShippingService2Locations", intShippingService2Locations);
                        resultMotherVersionMap.put("intShippingService3Option", intShippingService3Option);
                        resultMotherVersionMap.put("intShippingService3Cost", intShippingService3Cost);
                        resultMotherVersionMap.put("intShippingService3AddCost", intShippingService3AddCost);
                        resultMotherVersionMap.put("intShippingService3Locations", intShippingService3Locations);
                        resultMotherVersionMap.put("intShippingService4Option", intShippingService4Option);
                        resultMotherVersionMap.put("intShippingService4Cost", intShippingService4Cost);
                        resultMotherVersionMap.put("intShippingService4AddCost", intShippingService4AddCost);
                        resultMotherVersionMap.put("intShippingService4Locations", intShippingService4Locations);
                        resultMotherVersionMap.put("intShippingService5Option", intShippingService5Option);
                        resultMotherVersionMap.put("intShippingService5Cost", intShippingService5Cost);
                        resultMotherVersionMap.put("intShippingService5AddCost", intShippingService5AddCost);
                        resultMotherVersionMap.put("intShippingService5Locations", intShippingService5Locations);
                        resultMotherVersionMap.put("intShippingRateTable", intShippingRateTable);
                        resultMotherVersionMap.put("excludeShipToLocation", excludeShipToLocation);
                        resultMotherVersionMap.put("payPalEmailAddress", payPalEmailAddress);
                        resultMotherVersionMap.put("immediatePayRequired", immediatePayRequired);
                        resultMotherVersionMap.put("payMoneyXferAccInCheckout", payMoneyXferAccInCheckout);
                        resultMotherVersionMap.put("paymentInstructions", paymentInstructions);
                        resultMotherVersionMap.put("returnsAcceptedOption", returnsAcceptedOption);
                        resultMotherVersionMap.put("returnsWithinOption", returnsWithinOption);
                        resultMotherVersionMap.put("refundOption", refundOption);
                        resultMotherVersionMap.put("shippingCostPaidByOption", shippingCostPaidByOption);
                        resultMotherVersionMap.put("returnPolicyDescription", returnPolicyDescription);
                        resultMotherVersionMap.put("extendedHolidayReturns", extendedHolidayReturns);
                        resultMotherVersionMap.put("bRLinkedPayPalAccount", bRLinkedPayPalAccount);
                        resultMotherVersionMap.put("bRMaxBuyerPolicyVioCount", bRMaxBuyerPolicyVioCount);
                        resultMotherVersionMap.put("bRMaxBuyerPolicyVioPeriod", bRMaxBuyerPolicyVioPeriod);
                        resultMotherVersionMap.put("bRMaxItemReqMaxItemCount", bRMaxItemReqMaxItemCount);
                        resultMotherVersionMap.put("bRMaxItemReqMinFeedbackScore", bRMaxItemReqMinFeedbackScore);
                        resultMotherVersionMap.put("bRMaxUnpaidStrikesInfoCount", bRMaxUnpaidStrikesInfoCount);
                        resultMotherVersionMap.put("bRMaxUnpaidStrikesInfoPeriod", bRMaxUnpaidStrikesInfoPeriod);
                        resultMotherVersionMap.put("bRMinFeedbackScore", bRMinFeedbackScore);
                        resultMotherVersionMap.put("bRShipToRegistrationCountry", bRShipToRegistrationCountry);
                        resultMotherVersionMap.put("yunqudaoAutoRelistId", yunqudaoAutoRelistId);
                        resultMotherVersionMap.put("yunqudaoAutoReplenishId", yunqudaoAutoReplenishId);
                        resultMotherVersionMap.put("yunqudaoShowcaseId", yunqudaoShowcaseId);
                        resultMotherVersionMap.put("userLogin", userLogin);
                        
                        Map createMotherVersionResult = dispatcher.runSync("createMotherVersionResult", resultMotherVersionMap);
                        
                        
                        //create Children row == START
                        for (Map childResultMap : childMapList) {   //loop childMapList == START
                            String childProductId = skuPrefix + childResultMap.get("productId") + skuSuffix;
                            String childProductPriceStr = childResultMap.get("childProductPrice").toString();
                            String childBuyItNowPrice = childProductPriceStr;
                            double childProductPrice = Double.parseDouble(childProductPriceStr);
                            
                            if (childProductPrice < 5) {
                                quantity = "20";
                            } else if (childProductPrice >= 5 && childProductPrice < 30) {
                                quantity = "5";
                            } else if (childProductPrice >= 30) {
                                quantity = "3";
                            } else {
                                quantity = "5";
                            }
                            
                            if (!folderName.equals("ST1FREE") && !folderName.equals("ST2FREE")) {
                                if (lowestChildPrice > 1 && lowestChildPrice < 2) {
                                    childBuyItNowPrice = "1";
                                } else if (lowestChildPrice >= 2 && lowestChildPrice < 5) {
                                    childBuyItNowPrice = (childProductPrice - 1)  + "";
                                } else if (lowestChildPrice >= 5 && lowestChildPrice < 10) {
                                    childBuyItNowPrice = (childProductPrice - 3)  + "";
                                } else if (lowestChildPrice >= 10 && lowestChildPrice < 20) {
                                    childBuyItNowPrice = (childProductPrice - 5)  + "";
                                } else if (lowestChildPrice >= 20 && lowestChildPrice < 40) {
                                    childBuyItNowPrice = (childProductPrice - 10)  + "";
                                } else if (lowestChildPrice >= 40) {
                                    childBuyItNowPrice = (childProductPrice - 20)  + "";
                                } else {
                                    childBuyItNowPrice = childProductPrice + "";
                                    if (childProductPrice <= siteLowestPrice) {
                                        childBuyItNowPrice = siteLowestPrice + "";
                                    }
                                }
                            }
                            
                            Map<String, Object> resultMotherVersionChildMap = FastMap.newInstance();
                            resultMotherVersionChildMap.put("account", productStoreId);
                            resultMotherVersionChildMap.put("marketplace", site);
                            resultMotherVersionChildMap.put("id", childResultMap.get("id"));
                            resultMotherVersionChildMap.put("ebayAccountName", ebayAccountName);
                            resultMotherVersionChildMap.put("sku", childProductId);
                            resultMotherVersionChildMap.put("relationship", childResultMap.get("relationship"));
                            resultMotherVersionChildMap.put("relationshipDetails", childResultMap.get("relationshipDetails"));
                            resultMotherVersionChildMap.put("quantity", quantity);
                            resultMotherVersionChildMap.put("buyItNowPrice", childBuyItNowPrice);
                            resultMotherVersionChildMap.put("picUrl", childResultMap.get("picUrl"));
                            resultMotherVersionChildMap.put("userLogin", userLogin);
                            
                            Map createMotherVersionChildResult = dispatcher.runSync("createMotherVersionResult", resultMotherVersionChildMap);
                            
                            
                        }   //loop childMapList == END
                        
                        //create Children row == END
                        
                    }   //Create motherVersionResult == END
                    Debug.logError("MotherListing: " + yunqudaoListingIdVar + " - END", module);
                }   //loop multiVariationList == END
                
                //Multivariation listing == END


            
            
            }
        
        
        
        }   //end try block
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError("finish", module);
        return ServiceUtil.returnSuccess();
    }   //YasinTestJava
    
    
    public static Map<String, Object> YasinTestJava2 (DispatchContext dctx, Map context)
    throws Exception{
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String productStoreId = (String) context.get("parameter1");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        Map mapAccount = FastMap.newInstance();
        Calendar fromDay = Calendar.getInstance();
        fromDay.add(Calendar.DATE, -30);
        java.sql.Date fromDate = new java.sql.Date(fromDay.getTimeInMillis());
        
        try {
            
            String sku = "XXXXX-XXXX|ABCD";
            Debug.logError("SKU: " + sku, module);
            if (sku.contains("|")) {    //new 变体 format -- START
                Debug.logError("SKU has |", module);
                String term[]= sku.split("\\|");
                Debug.logError("term : " + term.length, module);
                sku = term[0];
            }   //new 变体 format -- END
            Debug.logError("SKU: " + sku, module);
        
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public static String getXMonthFromToday(Timestamp today, int x) {   //getXMonthFromToday
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(today.getTime());
		cal.add(Calendar.MONTH, -x);
        
		return new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(cal.getTime());
	}   //getXMonthFromToday
    
    public static long dayDifference (Date date2) { //dayDifference
        
        long diffDay = 0;
        Date initialDate = null;
        Date checkDate = date2;
        if (initialDate == null) {
            initialDate = new Date();
        }
        diffDay = Math.abs(initialDate.getTime() - checkDate.getTime()) / (24 * 60 * 60 * 1000); // convert the millis back to day
        return diffDay;
    }   //dayDifference
    
    public static String ebayToXDay( int diff)
    {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.DATE, today.get(Calendar.DATE) + diff);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'");
        Date resultDate = today.getTime();
        return sdf.format(resultDate);
    }
    
    public static String ebayToday()
    {
        Calendar today = Calendar.getInstance();
        //today.set(Calendar.DATE, today.get(Calendar.DATE) - diff);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'");
        Date resultDate = today.getTime();
        return sdf.format(resultDate);
    }
    
    public static String timestampToEbayDate(Timestamp ts)
    {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000'Z'");
        //Date resultDate = ts.getTime();
        return sdf.format(ts.getTime());
    }
    
    public static Map<String, Object> smartRelistEbay (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String productStoreId = (String) context.get("productStoreId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String today = sdf.format(now.getTime());
        Timestamp todayTS = Timestamp.valueOf(today);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + 2);
        Date triggerDate = now.getTime();
        Timestamp triggerDateTS = Timestamp.valueOf(sdf.format(triggerDate));
        
        Map result = ServiceUtil.returnSuccess();
        int count = 0;
        int successCount = 0;
        int failCount = 0;
        
        try {   //main try -- START
            FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/smartRelist.log", true);
            f1.write(today + " " + productStoreId + ": Start autoRelist" + "\n");
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            GenericValue productStoreEbaySetting = productStore.getRelatedOne("ProductStoreEbaySetting", false);
            if (!productStore.getString("primaryStoreGroupId").equals("EBAY")) {    //check if productStore belongs to ebay group -- START
                f1.write(today + " " + productStoreId + ": does not belong to eBay group. Not running auto relist!" + "\n");
                return result;
            }   //check if productStore belongs to ebay group -- END
            boolean smartRelist = false;
            if (UtilValidate.isNotEmpty(productStoreEbaySetting)) { //if productStoreEbaySetting not empty -- START
                f1.write(today + " " + productStoreId + ": getting productStoreEbaySetting" + "\n");
                if (productStoreEbaySetting.getString("smartRelist").equals("Y")) {
                    smartRelist = true;
                    f1.write(today + " " + productStoreId + ": smartRelist from productStoreEbaySetting is " + smartRelist + "\n");
                }
            }   //if productStoreEbaySetting not empty -- END
            else {  //if productStoreEbaySetting empty -- START
                f1.write(today + " " + productStoreId + ": does not have any productStoreEbaySetting. Not running auto relist!" + "\n");
                return result;
            }   //if productStoreEbaySetting empty -- END
            
            if (smartRelist) {  //if smartRelist is true -- START
                EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                          EntityCondition.makeCondition("productStoreId",EntityOperator.EQUALS ,productStoreId),
                                                                                          EntityCondition.makeCondition("listingDuration",EntityOperator.EQUALS , "GTC")
                                                                                          ));
                List<GenericValue> activeListingLists = delegator.findList("EbayActiveListing", condition, null, null, null, false);
                
                for (GenericValue activeListing : activeListingLists) { //loop activeListingLists -- START
                    String itemId = activeListing.getString("itemId");
                    long qtySold = activeListing.getLong("sellStatQuantitySold");
                    long watchCount = activeListing.getLong("watchCount");
                    long hitCount = activeListing.getLong("hitCount");
                    String endTime = activeListing.getString("listDetEndTime").replaceAll("[T,Z]"," ").trim();
                    Timestamp endTimeTS = Timestamp.valueOf(endTime);
                    
                    if (qtySold == 0 && triggerDateTS.compareTo(endTimeTS) >= 0) {  //run the relist -- START
                        Map endListing = dispatcher.runSync("TradingApiEndEbayActiveListingSingle", UtilMisc.toMap("itemId", itemId, "userLogin", userLogin));
                        if (ServiceUtil.isSuccess(endListing)) {    //if endListing success -- START
                            String endListingErrorMessage = (String) endListing.get("ebayErrorMessage");
                            if (endListingErrorMessage == null) {   //endListingErrorMessage is null -- START
                                Map relist = dispatcher.runSync("TradingApiRelistEbayActiveListingSingle", UtilMisc.toMap("itemId", itemId, "userLogin", userLogin));
                                if (ServiceUtil.isSuccess(relist)) {    //if relist success -- START
                                    String relistErrorMessage = (String) relist.get("ebayErrorMessage");
                                    if (relistErrorMessage == null) {   //if relistErrorMessage is null -- START
                                        successCount++;
                                        f1.write(today + " " + productStoreId + ": " + itemId + ", Relist Successful" + "\n");
                                        String relistHistoryId = delegator.getNextSeqId("RelistHistory");
                                        GenericValue relistHistory = delegator.makeValue("RelistHistory", UtilMisc.toMap("relistHistoryId", relistHistoryId));
                                        relistHistory.set("productStoreId", productStoreId);
                                        relistHistory.set("oldItemId", itemId);
                                        relistHistory.set("hitCount", hitCount);
                                        relistHistory.set("watchCount", watchCount);
                                        relistHistory.set("sku", activeListing.getString("sku"));
                                        relistHistory.set("newItemId", relist.get("newItemId"));
                                        delegator.create(relistHistory);
                                    }   //if relistErrorMessage is null -- START
                                    else {  //if relistErrorMessage is not null -- START
                                        failCount++;
                                        f1.write(today + " " + productStoreId + ": " + itemId + ", Relist Failed. " + relist.get("ebayErrorMessage") + "\n");
                                    }   //if relistErrorMessage is not null -- END
                                }   //if relist success -- END
                                else {  //if relist failed -- START
                                    failCount++;
                                    f1.write(today + " " + productStoreId + ": " + itemId + ", Relist Service failed" + "\n");
                                }   //if relist failed -- END
                            }   //endListingErrorMessage is null -- END
                            else {  //endListingErrorMessage is not null -- START
                                failCount++;
                                f1.write(today + " " + productStoreId + ": " + itemId + ", failed to end Listing. " + endListing.get("ebayErrorMessage") + "\n");
                            }   //endListingErrorMessage is not null -- END
                            
                        }   //if endListing success -- END
                        else {  //if endListing error -- START
                            failCount++;
                            f1.write(today + " " + productStoreId + ": " + itemId + ", End listing service failed" + "\n");
                        }   //if endListing error -- START
                        count++;
                    }   //run the relist -- END
                    if (count == 100) {  //temporary
                        break;
                    }
                }   //loop activeListingLists -- END
            }   //if smartRelist is true -- END
            f1.write(today + " " + productStoreId + ": " + count + " listings have been processed. " + successCount + " success and " + failCount + " fail" + "\n");
            f1.write(today + " " + productStoreId + ": End autoRelist" + "\n");
            f1.close();
        }   //main try -- START
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
        
        return result;
    }   //smartRelistEbay
    
}	//END class