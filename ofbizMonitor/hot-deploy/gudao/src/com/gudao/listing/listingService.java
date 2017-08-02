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
package com.gudao.listing;

import java.sql.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Properties;
//import java.sql.Date;

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
import java.util.LinkedList;
import java.util.Locale;
import java.util.Iterator;
import java.net.URLDecoder;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import jxl.*;
import jxl.write.*;
import jxl.read.biff.BiffException;
import org.jsoup.*;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.commons.lang.StringEscapeUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.ofbiz.base.util.ObjectType;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.string.FlexibleStringExpander;
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
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceAuthException;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.service.ServiceValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bellyanna.common.bellyannaService;
import com.bellyanna.ebay.common;
import com.bellyanna.ebay.requestXML;

public class listingService {
	private static final String module = listingService.class.getName();
    private static final String eol = System.getProperty("line.separator");
	
    public static Map<String, Object> gudaoAutoEndListing (DispatchContext dctx, Map<String, ? extends Object> context) {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreIdInput = (String) context.get("productStoreId");
        Map result = ServiceUtil.returnSuccess();
        SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
        
        try {   //main try -- START
            EntityCondition storeCondition = null;
            if (UtilValidate.isNotEmpty(productStoreIdInput)) {
                storeCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                               EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreIdInput)
                                                                               ));
            }

            List<GenericValue> productStoreList = delegator.findList("EndActiveListingRule", storeCondition, null, null, null, false);
            for (GenericValue productStore : productStoreList) {    //loop account rule - START
                String productStoreId = productStore.getString("productStoreId");
                long unsoldDay = productStore.getLong("unsoldDay");
                long historicalSoldLessThan = productStore.getLong("historicalSoldLessThan");
                long startDateMoreThanXDay = productStore.getLong("startDateMoreThanXDay");
                
                Calendar fromDay = Calendar.getInstance();
                fromDay.set(Calendar.DATE, fromDay.get(Calendar.DATE) - Integer.parseInt(unsoldDay + ""));
                Timestamp fromDate = Timestamp.valueOf(sdf.format(fromDay.getTime()));
                String fromDateEbayTime = bellyannaService.timestampToEbayDate(fromDate);

                
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
                
                List<GenericValue> activeListingList = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("productStoreId", productStoreId, "sellStatListingStatus", "Active"), UtilMisc.toList("listDetStartTime"), false);
                
                List<String> unsoldItemIdList1 = new LinkedList<String>();
                for (GenericValue activeListing : activeListingList) {  //loop activeListingList -- START
                    if (!uniqueSoldItemIdListHS.contains(activeListing.getString("itemId"))) {
                        unsoldItemIdList1.add(activeListing.getString("itemId"));
                    }
                }   //loop activeListingList -- END
                HashSet<String> uniqueUnsoldSoldItemIdListHS = new HashSet<String>(unsoldItemIdList1);
                List<String> unsoldItemIdList = new ArrayList<String>(uniqueUnsoldSoldItemIdListHS);
                
                List<String> endItemIdList = new ArrayList<String>();
                double endItemCount = 0.0;
                for (String itemId : unsoldItemIdList) {    //loop unsoldItemIdList -- START
                    boolean endItem = false;
                    List<GenericValue> activeLists = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false);
                    
                    if(UtilValidate.isNotEmpty(activeLists)) {  //if activeLists is not empty -- START
                        GenericValue activeList = EntityUtil.getFirst(activeLists);
                        long sellStatQuantitySold = activeList.getLong("sellStatQuantitySold");
                        String listDetStartTime = activeList.getString("listDetStartTime").substring(0, 10);
                        
                        java.sql.Date createdDateSql = new java.sql.Date(formatSql.parse(listDetStartTime).getTime());
                        long diffDay = Math.abs(new Date().getTime() - createdDateSql.getTime()) / (24 * 60 * 60 * 1000);
                        
                        if ((sellStatQuantitySold < historicalSoldLessThan) && (diffDay > startDateMoreThanXDay)) {
                            endItem = true;
                        }
                    }   //if activeLists is not empty -- END
                    
                    if (endItem) {
                        endItemIdList.add(itemId);
                        endItemCount++;
                    }
                    if (endItemCount >= (activeListingList.size() * 0.15)) {
                        break;
                    }
                }   //loop unsoldItemIdList -- END
                
                FileWriter f1 = new FileWriter("hot-deploy/gudao/webapp/gudao/error/" + productStoreId + ".txt", true);
                for (String test : endItemIdList) {
                    f1.write(test + eol);
                }
                
                f1.close();

            }   //loop account rule - END
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
        }   //main try -- END

        
        return result;
    }   //gudaoAutoEndListing
    
    public static Map<String, Object> gudaoGenerateMotherVersion (DispatchContext dctx, Map<String, ? extends Object> context) {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String site = (String) context.get("site");
        Map result = ServiceUtil.returnSuccess();
        SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
        Debug.logError("Starting gudaoGenerateMotherVersion", module);
        productStoreId = productStoreId.trim();
        site = site.trim();
        int seqId = 1;

        try {   //main try -- START
            //FileWriter f1 = new FileWriter("yunqudaoListingIdVar.txt", true);
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), true);
            boolean dangerAccount = false;
            if (UtilValidate.isNotEmpty(productStore.getString("companyName"))) {   //if companyName is not empty -- START
                if (productStore.getString("companyName").equals("DANGEROUS")) {
                    dangerAccount = true;
                }
            }   //if companyName is not empty -- START
            //f1.write("Starting account " + productStoreId + "; dangerAccount : " + dangerAccount + eol);
            
            double siteLowestPrice = 1.0;
            List<GenericValue> siteLowestList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", site), null, true);
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
            
            
            delegator.removeByAnd("MotherVersionResult", UtilMisc.toMap("account", productStoreId, "marketplace", site));
            
            List<String> activeSkuAll = new LinkedList<String>();
            //for (String activeSiteLoop : siteList) {
            EntityCondition activeListingCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                   EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                                                                   EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                   EntityCondition.makeCondition("site", EntityOperator.IN, siteList)
                                                                                                   ));
            List<GenericValue> activeListingList = delegator.findList("EbayActiveListing", activeListingCondition, UtilMisc.toSet("itemId", "normalizedSku", "hasVariation"), null, null, true);
            for (GenericValue activeListing : activeListingList) {  //loop activeListingList == START ==
                String itemId =  activeListing.getString("itemId");
                if (activeListing.getString("hasVariation").equals("N")) {
                    activeSkuAll.add(activeListing.getString("normalizedSku"));
                }
                List<GenericValue> variationList = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("itemId", itemId), null, true);
                for (GenericValue variation : variationList) {  //loop variationList == START ==
                    activeSkuAll.add(variation.getString("normalizedSku"));
                }   //loop variationList == END ==
            }   //loop activeListingList == END ==
            //}
            
            
            HashSet<String> activeSkuAllHS = new HashSet<String>(activeSkuAll);
            List<String> activeSkuUnique = new ArrayList<String>(activeSkuAllHS);
            
            EntityCondition ruleCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                          EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                                                          EntityCondition.makeCondition("site", EntityOperator.EQUALS, site),
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
                Debug.logError("generate MV for " + productStoreId + ", site: " + site + ", foldername: " + folderName, module);
                //f1.write("Account " + productStoreId + ", ownerGroup: " + ownerGroup + ", site: " + site + ", foldername: " + folderName + ", skuPrefix: " + skuPrefix + ", skuSuffix: " + skuSuffix + ", titleSuffix: " + titleSuffix + eol);
                if (UtilValidate.isEmpty(skuPrefix)) {
                    skuPrefix = "";
                }
                if (UtilValidate.isEmpty(skuSuffix)) {
                    skuSuffix = "";
                }
                
                List<GenericValue> accountListingRuleCategoryList = delegator.findByAnd("AccountListingRuleCategory", UtilMisc.toMap("productStoreId", productStoreId, "statusId", "ACTIVE", "site", site, "folderName", folderName), null, false);
                List<String> categoryList = new LinkedList<String>();
                for (GenericValue accountListingRuleCategory: accountListingRuleCategoryList) { //loop accountListingRuleCategoryList -- START
                    String categoryId = accountListingRuleCategory.getString("categoryId");
                    if (UtilValidate.isNotEmpty(categoryId)) {
                        categoryList.add(categoryId);
                        //f1.write("Special Requirement: " + specialRequirement + eol);
                    }
                }   //loop accountListingRuleCategoryList -- END
                
                //List<String> toBeListedListingIdListAll = new LinkedList<String>();
                EntityCondition checkMotherVersionCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                            EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site),
                                                                                                            EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName),
                                                                                                            EntityCondition.makeCondition("format", EntityOperator.EQUALS, "固定价格"),
                                                                                                            EntityCondition.makeCondition("sku", EntityOperator.NOT_IN, activeSkuUnique)
                                                                                                            ));
                List<GenericValue> checkMotherVersionSingleList = delegator.findList("MotherVersion", checkMotherVersionCondition, null, UtilMisc.toList("id"), null, false);
                Debug.logError("checkMotherVersionSingleList size: " + checkMotherVersionSingleList.size(), module);
                for (GenericValue mv : checkMotherVersionSingleList) {  //loop checkMotherVersionSingleList == START ==
                    String productId = mv.getString("sku");
                    String yunqudaoListingIdVar = mv.getString("yunqudaoListingIdVar");
                    String yunqudaoListingId = mv.getString("yunqudaoListingId");
                    boolean addListingId = false;
                    
                    GenericValue pmSingleCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
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
                        String id = seqId + "";
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
                        seqId++;
                    }   //addListingId for single listing == END
                }   //loop checkMotherVersionSingleList == END ==
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                //Multivariation listing == START
                EntityCondition multivariationCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                            EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site),
                                                                                                            EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName),
                                                                                                            EntityCondition.makeCondition("format", EntityOperator.EQUALS, "多属性"),
                                                                                                            EntityCondition.makeCondition("sku", EntityOperator.NOT_IN, activeSkuUnique)
                                                                                                            ));
                List<GenericValue> multiVariationList = delegator.findList("MotherVersion", multivariationCondition, null, UtilMisc.toList("yunqudaoListingIdVar"), null, false);
                Debug.logError("multiVariationList size: " + multiVariationList.size(), module);
                for (GenericValue multiVariation : multiVariationList) {    //loop multiVariationList == START
                    String storeCategory = null;
                    String yunqudaoListingIdVar = multiVariation.getString("yunqudaoListingIdVar");
                    double lowestChildPrice = 10000000000.0;
                    //Debug.logError("MotherListing: " + yunqudaoListingIdVar + " - START", module);
                    List<Map> childMapList = new LinkedList<Map>();
                    EntityCondition childCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                   EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site),
                                                                                                   EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName),
                                                                                                   EntityCondition.makeCondition("yunqudaoListingIdVar", EntityOperator.EQUALS, yunqudaoListingIdVar)
                                                                                                   ));
                    List<GenericValue> childList = delegator.findList("MotherVersion", childCondition, null, UtilMisc.toList("id"), null, false);
                    for (GenericValue child : childList) {  //loop childList == START
                        if (UtilValidate.isEmpty(child.getString("yunqudaoListingId"))) {   //if yungudaoListingId is empty == START
                            Map<String, String> childMap = FastMap.newInstance();
                            boolean addToChildMap = false;
                            
                            if (!activeSkuUnique.contains(child.getString("sku"))) {    //if child SKU not in activeSkuUnique == START
                                GenericValue pmChildrenCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", child.getString("sku")), false);

                                if (UtilValidate.isNotEmpty(pmChildrenCheck)) {   //if pmChildrenCheck is not empty == START
                                    if (UtilValidate.isEmpty(storeCategory)) {
                                        GenericValue gudaoCategoryMap = delegator.findOne("GudaoCategoryMap", UtilMisc.toMap("productCategory", pmChildrenCheck.getString("categoryIdParent"), "platform", "EBAY", "account", productStoreId), true);
                                        if (UtilValidate.isNotEmpty(gudaoCategoryMap)) {
                                            storeCategory = gudaoCategoryMap.getString("categoryId");
                                        }
                                    }
                                    
                                    String pmChildrenCheckProductGroup = pmChildrenCheck.getString("productGroup");
                                    String pmChildrenCheckRisk = pmChildrenCheck.getString("risk");
                                    String pmChildrenCheckStatusId = pmChildrenCheck.getString("statusId");
                                    if (UtilValidate.isNotEmpty(categoryList)) {    //if categoryList is not empty == START
                                        String singleCategoryIdParent = pmChildrenCheck.getString("categoryIdParent");
                                        if (UtilValidate.isNotEmpty(singleCategoryIdParent)) { // if singleCategoryIdParent is NOT EMPTY == START
                                            if(categoryList.contains(singleCategoryIdParent)) {    //category matched categoryList == START
                                                if (!pmChildrenCheckProductGroup.equals("G4") && !pmChildrenCheckProductGroup.equals("G5") && !pmChildrenCheckRisk.equals("高") && safeStatusList.contains(pmChildrenCheckStatusId)) {   //general safe check == START
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
                                
                                //Debug.logError("this RUN", module);
                                if (addToChildMap) {    //if addToChildMap TRUE == START
                                    //Debug.logError("Product Id " + child.getString("sku") + " start", module);
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
                                }   //if addToChildMap TRUE == END
                            }   //if child SKU not in activeSkuUnique == END
                        }   //if yungudaoListingId is empty == END
                        
                    }   //loop childList == END
                    
                    //Debug.logError("yunqudaoListingIdVar: " + yunqudaoListingIdVar + " - childMapList size: " + childMapList.size(), module);
                    
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
                        String id = seqId + "";
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
                        seqId++;
                        
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
                            resultMotherVersionChildMap.put("id", seqId + "");
                            resultMotherVersionChildMap.put("ebayAccountName", ebayAccountName);
                            resultMotherVersionChildMap.put("sku", childProductId);
                            resultMotherVersionChildMap.put("relationship", childResultMap.get("relationship"));
                            resultMotherVersionChildMap.put("relationshipDetails", childResultMap.get("relationshipDetails"));
                            resultMotherVersionChildMap.put("quantity", quantity);
                            resultMotherVersionChildMap.put("buyItNowPrice", childBuyItNowPrice);
                            resultMotherVersionChildMap.put("picUrl", childResultMap.get("picUrl"));
                            resultMotherVersionChildMap.put("userLogin", userLogin);
                            
                            Map createMotherVersionChildResult = dispatcher.runSync("createMotherVersionResult", resultMotherVersionChildMap);
                            seqId++;
                            
                        }   //loop childMapList == END
                        
                        //create Children row == END
                        
                    }   //Create motherVersionResult == END
                    //Debug.logError("MotherListing: " + yunqudaoListingIdVar + " - END", module);
                }   //loop multiVariationList == END

                //Multivariation listing == END
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                //Debug.logError(ownerGroup + " - changing ownerGroup", module);
            }   //loop accountListingRuleList -- END
            
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
        }   //main try -- END
        Debug.logError(productStoreId + " finished generating motherVersionResult", module);
        return ServiceUtil.returnSuccess();
        
    }
    
    
    public static Map<String, Object> gudaoGenerateMotherVersionTEST (DispatchContext dctx, Map<String, ? extends Object> context) {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String site = (String) context.get("site");
        Map result = ServiceUtil.returnSuccess();
        SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
        Debug.logError("Starting gudaoGenerateMotherVersion", module);
        productStoreId = productStoreId.trim();
        site = site.trim();
        
        try {   //main try -- START
            FileWriter f1 = new FileWriter("yunqudaoListingIdVar.txt", true);
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), true);
            boolean dangerAccount = false;
            if (UtilValidate.isNotEmpty(productStore.getString("companyName"))) {   //if companyName is not empty -- START
                if (productStore.getString("companyName").equals("DANGEROUS")) {
                    dangerAccount = true;
                }
            }   //if companyName is not empty -- START
            //f1.write("Starting account " + productStoreId + "; dangerAccount : " + dangerAccount + eol);
            
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
            
            delegator.removeByAnd("MotherVersionResult", UtilMisc.toMap("account", productStoreId, "marketplace", site));
            
            List<String> activeSkuAll = new LinkedList<String>();
            //for (String activeSiteLoop : siteList) {
                EntityCondition activeListingCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                       EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                                                                       EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                       EntityCondition.makeCondition("site", EntityOperator.IN, siteList)
                                                                                                       ));
                List<GenericValue> activeListingList = delegator.findList("EbayActiveListing", activeListingCondition, UtilMisc.toSet("itemId", "normalizedSku"), null, null, true);
                for (GenericValue activeListing : activeListingList) {  //loop activeListingList == START ==
                    String itemId =  activeListing.getString("itemId");
                    activeSkuAll.add(activeListing.getString("normalizedSku"));
                    List<GenericValue> variationList = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("itemId", itemId), null, true);
                    for (GenericValue variation : variationList) {  //loop variationList == START ==
                        activeSkuAll.add(variation.getString("normalizedSku"));
                    }   //loop variationList == END ==
                }   //loop activeListingList == END ==
            //}
            
            
            HashSet<String> activeSkuAllHS = new HashSet<String>(activeSkuAll);
            List<String> activeSkuUnique = new ArrayList<String>(activeSkuAllHS);
            
            EntityCondition ruleCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                          EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                                                          EntityCondition.makeCondition("site", EntityOperator.EQUALS, site),
                                                                                          EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE")
                                                                                          ));
            
            List<GenericValue> accountListingRuleList = delegator.findList("AccountListingRule", ruleCondition, null, null, null, true);
            Debug.logError("accountListingRuleList size: " + accountListingRuleList.size(), module);
            //f1.write("Start looping accountListingRuleList" + eol);
            for (GenericValue accountListingRule : accountListingRuleList) {    //loop accountListingRuleList -- START
                String folderName = accountListingRule.getString("folderName");
                String skuPrefix = accountListingRule.getString("skuPrefix");
                String skuSuffix = accountListingRule.getString("skuSuffix");
                String titleSuffix = accountListingRule.getString("titleSuffix");
                String ownerGroup = accountListingRule.getString("ownerGroup");
                Debug.logError("generate MV for " + productStoreId + ", ownerGroup: " + ownerGroup + ", site: " + site + ", foldername: " + folderName, module);
                //f1.write("Account " + productStoreId + ", ownerGroup: " + ownerGroup + ", site: " + site + ", foldername: " + folderName + ", skuPrefix: " + skuPrefix + ", skuSuffix: " + skuSuffix + ", titleSuffix: " + titleSuffix + eol);
                if (UtilValidate.isEmpty(skuPrefix)) {
                    skuPrefix = "";
                }
                if (UtilValidate.isEmpty(skuSuffix)) {
                    skuSuffix = "";
                }
                
                List<GenericValue> accountListingRuleCategoryList = delegator.findByAnd("AccountListingRuleCategory", UtilMisc.toMap("productStoreId", productStoreId, "statusId", "ACTIVE", "site", site, "ownerGroup", ownerGroup, "folderName", folderName), null, true);
                List<String> specialReqAll = new LinkedList<String>();
                for (GenericValue accountListingRuleCategory: accountListingRuleCategoryList) { //loop accountListingRuleCategoryList -- START
                    String specialRequirement = accountListingRuleCategory.getString("specialRequirement");
                    if (UtilValidate.isNotEmpty(specialRequirement)) {
                        specialReqAll.add(specialRequirement);
                        //f1.write("Special Requirement: " + specialRequirement + eol);
                    }
                }   //loop accountListingRuleCategoryList -- END
                
                List<String> toBeListedListingIdListAll = new LinkedList<String>();
                EntityCondition checkMotherVersionCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                             EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site),
                                                                                                             EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName),
                                                                                                             EntityCondition.makeCondition("sku", EntityOperator.NOT_IN, activeSkuUnique)
                                                                                                             ));
                List<GenericValue> checkMotherVersionList = delegator.findList("MotherVersion", checkMotherVersionCondition, null, UtilMisc.toList("yunqudaoListingIdVar", "id", "yunqudaoListingId"), null, true);
                for (GenericValue mv : checkMotherVersionList) {  //loop activeMotherVersionList == START ==
                    String productId = mv.getString("sku");
                    String yunqudaoListingIdVar = mv.getString("yunqudaoListingIdVar");
                    String yunqudaoListingId = mv.getString("yunqudaoListingId");
                    boolean addListingId = true;
//Debug.logError(yunqudaoListingIdVar + " - " + yunqudaoListingId + " - " + productId, module);
                    
                    if (UtilValidate.isNotEmpty(yunqudaoListingId)) {    //motherListing == START
                        EntityCondition motherListingCheckChildrenCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                                    EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site),
                                                                                                                    EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName),
                                                                                                                    EntityCondition.makeCondition("yunqudaoListingIdVar", EntityOperator.EQUALS, yunqudaoListingIdVar),
                                                                                                                    EntityCondition.makeCondition("sku", EntityOperator.NOT_IN, activeSkuUnique)
                                                                                                                    ));
                        
                        List<GenericValue> motherListingCheckChildrenList = delegator.findList("MotherVersion", motherListingCheckChildrenCondition, null, null, null, true);
//Debug.logError("MotherListing : motherListingCheckChildrenList size: " + motherListingCheckChildrenList.size(), module);
                        for (GenericValue motherListingCheckChildren : motherListingCheckChildrenList) {    //loop motherListingCheckChildrenList == START
                            String yunqudaoListingIdChildren = motherListingCheckChildren.getString("yunqudaoListingId");
                            String productIdChildren = motherListingCheckChildren.getString("sku");
//Debug.logError(yunqudaoListingIdChildren + " - " + productIdChildren, module);
                            if (UtilValidate.isEmpty(yunqudaoListingIdChildren)) {  //if yunqudaoListingIdChildren is empty == START
//Debug.logError("yunqudaoListingIdChildren is empty", module);
                                GenericValue pmChildrenCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productIdChildren), true);
                                if (UtilValidate.isNotEmpty(pmChildrenCheck)) { //if pmChildrenCheck is not empty == START
                                    if (pmChildrenCheck.getString("ownerGroup").equals(ownerGroup)) {    //if OwnerGroup is correct == START
                                        addListingId = true;
                                        //Checking G4 or G5 -- START
                                        int checkSize = 0;
                                        List<GenericValue> checkProductGroupList = delegator.findByAnd("MotherVersion", UtilMisc.toMap("yunqudaoListingIdVar", yunqudaoListingIdVar, "motherVersionType", folderName, "marketplace", site), null, true);
                                        for (GenericValue checkProductGroup : checkProductGroupList) {  //loop checkProductGroupList == START ==
                                            if (UtilValidate.isEmpty(checkProductGroup.getString("yunqudaoListingId"))) {
                                                GenericValue productCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", checkProductGroup.getString("sku")), true);
                                                if (UtilValidate.isNotEmpty(productCheck)) {
                                                    if (productCheck.getString("productGroup").equals("G4") || productCheck.getString("productGroup").equals("G5")) {
                                                        checkSize++;
                                                    }
                                                }
                                            }
                                        }   //loop checkProductGroupList == END ==
                                        if (checkProductGroupList.size() > 1) { //if has variation -- START
                                            if ((checkProductGroupList.size() - 1) == checkSize) {
                                                addListingId = false;
                                            }
                                        }   //if has variation -- END
                                        else {  //if no variation -- START
                                            if (checkProductGroupList.size() == checkSize) {
                                                addListingId = false;
                                            }
                                        }   //if no variation -- END
                                        //Checking G4 or G5 -- END
                                        
                                        if (dangerAccount) {    //dangerous account == START
                                            String platform = pmChildrenCheck.getString("platform");
                                            if (UtilValidate.isEmpty(platform)) {   //check platform == START
                                                addListingId = false;
                                            } else {
                                                if (platform.equals("S") || platform.equals("SE")) {
                                                    addListingId = false;
                                                }
                                            }   //check platform == END
                                        }   //dangerous account == END
                                        
                                        if (UtilValidate.isNotEmpty(specialReqAll)) {   //special Requirement (category) == START
                                            //Debug.logError("specialReqAll is not null", module);
                                            String categoryIdParent = pmChildrenCheck.getString("categoryIdParent");
                                            if (UtilValidate.isEmpty(categoryIdParent)) {   //if categoryIdParent EMPTY == START
                                                addListingId = false;
                                            }   //if categoryIdParent EMPTY == END
                                            else {
                                                if(!specialReqAll.contains(categoryIdParent)) { //if categoryIdParent NOT EMPTY == START
                                                    addListingId = false;
                                                }
                                            }   //if categoryIdParent NOT EMPTY == END
                                        }   //special Requirement (category) == END
                                    }   //if OwnerGroup is correct == END
                                }   //if pmChildrenCheck is not empty == END
                            }   //if yunqudaoListingIdChildren is empty == END
                            else {  //if yunqudaoListingIdChildren is NOT empty == START
//Debug.logError("yunqudaoListingIdChildren is not empty", module);
                                GenericValue pmMotherCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productIdChildren), true);
                                //Debug.logError("pmMotherCheck: " + pmMotherCheck, module);
                                if (UtilValidate.isNotEmpty(pmMotherCheck)) {   //if pmMotherCheck is not empty == START
                                    if (pmMotherCheck.getString("ownerGroup").equals(ownerGroup)) {
                                        addListingId = true;
                                    }
                                    if (dangerAccount) {    //dangerous account == START
                                        String platform = pmMotherCheck.getString("platform");
                                        if (UtilValidate.isEmpty(platform)) {   //check platform == START
                                            addListingId = false;
                                        } else {
                                            if (platform.equals("S") || platform.equals("SE")) {
                                                addListingId = false;
                                            }
                                        }   //check platform == END
                                    }   //dangerous account == END
                                    
                                    if (UtilValidate.isNotEmpty(specialReqAll)) {   //special Requirement (category) == START
                                        //Debug.logError("specialReqAll is not null", module);
                                        String categoryIdParent = pmMotherCheck.getString("categoryIdParent");
                                        if (UtilValidate.isEmpty(categoryIdParent)) {   //if categoryIdParent EMPTY == START
                                            addListingId = false;
                                        }   //if categoryIdParent EMPTY == END
                                        else {
                                            if(!specialReqAll.contains(categoryIdParent)) { //if categoryIdParent NOT EMPTY == START
                                                addListingId = false;
                                            }
                                        }   //if categoryIdParent NOT EMPTY == END
                                    }   //special Requirement (category) == END
                                }   //if pmMotherCheck is not empty == END
                            }   //if yunqudaoListingIdChildren is NOT empty == END
                        }   //loop motherListingCheckChildrenList == END
//Debug.logError("MotherListing: end here" , module);
                    }   //motherListing == END
                    else {  //childrenListing == START
                        GenericValue pmCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), true);
                        if (UtilValidate.isNotEmpty(pmCheck)) {  //if PM is not null -- START
                            if (pmCheck.getString("ownerGroup").equals(ownerGroup)) {    //if OwnerGroup is correct == START
                                addListingId = true;
                                //Checking G4 or G5 -- START
                                int checkSize = 0;
                                List<GenericValue> checkProductGroupList = delegator.findByAnd("MotherVersion", UtilMisc.toMap("yunqudaoListingIdVar", yunqudaoListingIdVar, "motherVersionType", folderName), null, true);
                                for (GenericValue checkProductGroup : checkProductGroupList) {  //loop checkProductGroupList == START ==
                                    if (UtilValidate.isEmpty(checkProductGroup.getString("yunqudaoListingId"))) {
                                        GenericValue productCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", checkProductGroup.getString("sku")), true);
                                        if (UtilValidate.isNotEmpty(productCheck)) {
                                            if (productCheck.getString("productGroup").equals("G4") || productCheck.getString("productGroup").equals("G5")) {
                                                checkSize++;
                                            }
                                        }
                                    }
                                }   //loop checkProductGroupList == END ==
                                if (checkProductGroupList.size() > 1) { //if has variation -- START
                                    if ((checkProductGroupList.size() - 1) == checkSize) {
                                        addListingId = false;
                                    }
                                }   //if has variation -- END
                                else {  //if no variation -- START
                                    if (checkProductGroupList.size() == checkSize) {
                                        addListingId = false;
                                    }
                                }   //if no variation -- END
                                //Checking G4 or G5 -- END
                                
                                if (dangerAccount) {    //dangerous account == START
                                    String platform = pmCheck.getString("platform");
                                    if (UtilValidate.isEmpty(platform)) {   //check platform == START
                                        addListingId = false;
                                    } else {
                                        if (platform.equals("S") || platform.equals("SE")) {
                                            addListingId = false;
                                        }
                                    }   //check platform == END
                                }   //dangerous account == END
                                
                                if (UtilValidate.isNotEmpty(specialReqAll)) {   //special Requirement (category) == START
                                    //Debug.logError("specialReqAll is not null", module);
                                    String categoryIdParent = pmCheck.getString("categoryIdParent");
                                    if (UtilValidate.isEmpty(categoryIdParent)) {   //if categoryIdParent EMPTY == START
                                        addListingId = false;
                                    }   //if categoryIdParent EMPTY == END
                                    else {
                                        if(!specialReqAll.contains(categoryIdParent)) { //if categoryIdParent NOT EMPTY == START
                                            addListingId = false;
                                        }
                                    }   //if categoryIdParent NOT EMPTY == END
                                }   //special Requirement (category) == END
                                
                            }   //if OwnerGroup is correct == END
                        }   //if PM is not null -- END
                    }   //childrenListing == END
                    
                    if (addListingId) {
                        //Debug.logError("yunqudaoListingIdVar " + yunqudaoListingIdVar, module);
                        f1.write(yunqudaoListingIdVar + eol);
                        f1.close();
                    }

                    /*
                    if (addListingId) { //generate motherVersionResult == START
                        String quantity = mv.getString("quantity");
                        String currency = mv.getString("currency");
                        String sku = mv.getString("sku");
                        String buyItNowPrice = mv.getString("buyItNowPrice");
                        String ebayAccountName = productStoreId;
                        String siteId = mv.getString("siteId");
                        String title = null;
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
                        String dispatchTimeMax = mv.getString("dispatchTimeMax");
                        
                        //Shipping -- START
                        double productPrice = 100000.0;
                        if (UtilValidate.isEmpty(yunqudaoListingId)) {  //loop empty yunqudaoListingId (children Listing) -- START
                            EntityCondition childrenCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                              EntityCondition.makeCondition("yunqudaoListingIdVar", EntityOperator.EQUALS, yunqudaoListingIdVar),
                                                                                                              EntityCondition.makeCondition("yunqudaoListingId", EntityOperator.EQUALS, null),
                                                                                                              EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName),
                                                                                                              EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site)
                                                                                                              ));
                            List<GenericValue> childrenList = delegator.findList("MotherVersion", childrenCondition, null, null, null, true);
                            
                            for (GenericValue childrenGv : childrenList) {  //loop childrenList -- START
                                String childSku = childrenGv.getString("sku");
                                double totalPrice = 0.0;
                                GenericValue pm = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", childSku), true);
                                if (UtilValidate.isNotEmpty(pm)) {  //if pm is not empty -- START
                                    if (site.equals("US")) {
                                        totalPrice = pm.getBigDecimal("priceUsd").doubleValue();
                                    } else if (site.equals("AU")) {
                                        totalPrice = pm.getBigDecimal("priceAud").doubleValue();
                                    } else if (site.equals("CA")) {
                                        totalPrice = pm.getBigDecimal("priceCad").doubleValue();
                                    } else if (site.equals("UK")) {
                                        totalPrice = pm.getBigDecimal("priceGbp").doubleValue();
                                    } else if (site.equals("DE")) {
                                        totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                    } else if (site.equals("FR")) {
                                        totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                    } else if (site.equals("ES")) {
                                        totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                    } else if (site.equals("IT")) {
                                        totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                    }
                                }   //if pm is not empty -- END
                                if (totalPrice <= productPrice) {
                                    productPrice = totalPrice;
                                }
                            }   //loop childrenList -- END
                        }   //loop empty yunqudaoListingId (children Listing) -- END
                        else {  //loop motherlisting - get product price == START
                            GenericValue pm = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", sku), true);
                            double totalPrice = 0.0;
                            if (UtilValidate.isNotEmpty(pm)) {  //if pm is not empty -- START
                                if (site.equals("US")) {
                                    totalPrice = pm.getBigDecimal("priceUsd").doubleValue();
                                } else if (site.equals("AU")) {
                                    totalPrice = pm.getBigDecimal("priceAud").doubleValue();
                                } else if (site.equals("CA")) {
                                    totalPrice = pm.getBigDecimal("priceCad").doubleValue();
                                } else if (site.equals("UK")) {
                                    totalPrice = pm.getBigDecimal("priceGbp").doubleValue();
                                } else if (site.equals("DE")) {
                                    totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                } else if (site.equals("FR")) {
                                    totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                } else if (site.equals("ES")) {
                                    totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                } else if (site.equals("IT")) {
                                    totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                }
                            }   //if pm is not empty -- END
                            productPrice = totalPrice;
                        }   //loop motherlisting - get product price == END
                        
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
                        List<GenericValue> domesticList = delegator.findByAnd("AccountListingRuleShipping", UtilMisc.toMap("site", site, "shippingType", "DOMESTIC", "filter", filter), null, true);
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
                        List<GenericValue> intList = delegator.findByAnd("AccountListingRuleShipping", UtilMisc.toMap("site", site, "shippingType", "INTERNATIONAL", "filter", intFilter), null, true);
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
                        }   ////loop intList -- END
                        
                        GenericValue productResult = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", sku), true);
                        double totalPrice = 0.0;
                        if (UtilValidate.isNotEmpty(productResult)) {
                            if (site.equals("US")) {
                                totalPrice = productResult.getBigDecimal("priceUsd").doubleValue();
                            } else if (site.equals("AU")) {
                                totalPrice = productResult.getBigDecimal("priceAud").doubleValue();
                            } else if (site.equals("CA")) {
                                totalPrice = productResult.getBigDecimal("priceCad").doubleValue();
                            } else if (site.equals("UK")) {
                                totalPrice = productResult.getBigDecimal("priceGbp").doubleValue();
                            } else if (site.equals("DE")) {
                                totalPrice = productResult.getBigDecimal("priceEur").doubleValue();
                            } else if (site.equals("FR")) {
                                totalPrice = productResult.getBigDecimal("priceEur").doubleValue();
                            } else if (site.equals("ES")) {
                                totalPrice = productResult.getBigDecimal("priceEur").doubleValue();
                            } else if (site.equals("IT")) {
                                totalPrice = productResult.getBigDecimal("priceEur").doubleValue();
                            }
                            
                            if (totalPrice < 5) {
                                quantity = "20";
                            } else if (totalPrice >= 5 && totalPrice < 30) {
                                quantity = "5";
                            } else if (totalPrice >= 30) {
                                quantity = "3";
                            } else {
                                quantity = "5";
                            }
                            
                            if (productResult.getString("productGroup").equals("G4") || productResult.getString("productGroup").equals("G5")) {
                                quantity = "0";
                            }
                        }
                        
                        buyItNowPrice = totalPrice + "";
                        
                        
                        if (UtilValidate.isEmpty(yunqudaoListingId)) {  //if children line -- START
                            ebayAccountName = productStoreId;
                            siteId = "";
                            title = "";
                            duration = "";
                            privateListing = "";
                            uploadImageEps = "";
                            showImageInDesc = "";
                            location = "";
                            getItFast = "";
                            extendedHolidayReturns = "";
                            bRLinkedPayPalAccount = "";
                            shippingService1Option = "";
                            shippingService1Cost = "";
                            shippingService1AddCost = "";
                            shippingService2Option = "";
                            shippingService2Cost = "";
                            shippingService2AddCost = "";
                            shippingService3Option = "";
                            shippingService3Cost = "";
                            shippingService3AddCost = "";
                            shippingService4Option = "";
                            shippingService4Cost = "";
                            shippingService4AddCost = "";
                            intShippingService1Option = "";
                            intShippingService1Cost = "";
                            intShippingService1AddCost = "";
                            intShippingService1Locations = "";
                            intShippingService2Option = "";
                            intShippingService2Cost = "";
                            intShippingService2AddCost = "";
                            intShippingService2Locations = "";
                            intShippingService3Option = "";
                            intShippingService3Cost = "";
                            intShippingService3AddCost = "";
                            intShippingService3Locations = "";
                            intShippingService4Option = "";
                            intShippingService4Cost = "";
                            intShippingService4AddCost = "";
                            intShippingService4Locations = "";
                            intShippingService5Option = "";
                            intShippingService5Cost = "";
                            intShippingService5AddCost = "";
                            intShippingService5Locations = "";
                        }   //if children line -- END
                        else {  //if mother line -- START
                            title = mv.getString("title").substring(0, mv.getString("title").length() - 4) + " " + titleSuffix;
                            upc = "Does not apply";
                            if (UtilValidate.isNotEmpty(productResult)) {
                                if (UtilValidate.isNotEmpty(productResult.getString("upc"))) {
                                    upc = productResult.getString("upc");
                                }
                            }
                            
                            ean = "Does not apply";
                            isbn = "Does not apply";
                            brandMpnBrand = "WHATWEARS";
                            brandMpnMpn = "Does not apply";
                            
                        }   //if mother line -- END
                        
                        //Debug.logError("first shippingService1Cost is : " + shippingService1Cost, module);
                        
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
                        //Shipping -- END
                        //Debug.logError("after foler shippingService1Cost is : " + shippingService1Cost, module);
                        
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
                        String storeCategory = mv.getString("storeCategory");
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
                    }   //generate motherVersionResult == END
                    */
                }   //loop activeMotherVersionList == END ==
                Debug.logError("changing ownerGroup", module);
            }   //loop accountListingRuleList -- END
            
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
        }   //main try -- END
        Debug.logError(productStoreId + " finished", module);
        return ServiceUtil.returnSuccess();
        
    }   //gudaoGenerateMotherVersion
    
    public static Map<String, Object> gudaoGenerateMotherVersionBACKUP (DispatchContext dctx, Map<String, ? extends Object> context) {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String site = (String) context.get("site");
        Map result = ServiceUtil.returnSuccess();
        SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.000");
        Debug.logError("Starting gudaoGenerateMotherVersion", module);
        productStoreId = productStoreId.trim();
        site = site.trim();
        
        try {   //main try -- START
            //FileWriter f1 = new FileWriter("hot-deploy/gudao/webapp/gudao/error/" + productStoreId + "-MV.txt", true);
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            boolean dangerAccount = false;
            if (UtilValidate.isNotEmpty(productStore.getString("companyName"))) {   //if companyName is not empty -- START
                if (productStore.getString("companyName").equals("DANGEROUS")) {
                    dangerAccount = true;
                }
            }   //if companyName is not empty -- START
            //f1.write("Starting account " + productStoreId + "; dangerAccount : " + dangerAccount + eol);
            
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
            
            delegator.removeByAnd("MotherVersionResult", UtilMisc.toMap("account", productStoreId, "marketplace", site));
            
            List<String> activeSkuAll = new LinkedList<String>();
            for (String activeSiteLoop : siteList) {
                EntityCondition activeListingCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                       EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                                                                       EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                       EntityCondition.makeCondition("site", EntityOperator.EQUALS, activeSiteLoop)
                                                                                                       ));
                List<GenericValue> activeListingList = delegator.findList("EbayActiveListing", activeListingCondition, UtilMisc.toSet("itemId", "normalizedSku"), null, null, false);
                for (GenericValue activeListing : activeListingList) {  //loop activeListingList == START ==
                    String itemId =  activeListing.getString("itemId");
                    activeSkuAll.add(activeListing.getString("normalizedSku"));
                    List<GenericValue> variationList = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("itemId", itemId), null, false);
                    for (GenericValue variation : variationList) {  //loop variationList == START ==
                        activeSkuAll.add(variation.getString("normalizedSku"));
                    }   //loop variationList == END ==
                }   //loop activeListingList == END ==
            }
            
            HashSet<String> activeSkuAllHS = new HashSet<String>(activeSkuAll);
            List<String> activeSkuUnique = new ArrayList<String>(activeSkuAllHS);
            /*f1.write("Unique SKU on active listing loop START: " + eol);
             for (String tempSku : activeSkuUnique) {
             f1.write(tempSku + eol);
             }
             f1.write("Unique SKU on active listing loop END" + eol);*/
            
            EntityCondition ruleCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                          EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                                                          EntityCondition.makeCondition("site", EntityOperator.EQUALS, site),
                                                                                          EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE")
                                                                                          ));
            
            List<GenericValue> accountListingRuleList = delegator.findList("AccountListingRule", ruleCondition, null, null, null, false);
            //f1.write("Start looping accountListingRuleList" + eol);
            for (GenericValue accountListingRule : accountListingRuleList) {    //loop accountListingRuleList -- START
                String folderName = accountListingRule.getString("folderName");
                String skuPrefix = accountListingRule.getString("skuPrefix");
                String skuSuffix = accountListingRule.getString("skuSuffix");
                String titleSuffix = accountListingRule.getString("titleSuffix");
                String ownerGroup = accountListingRule.getString("ownerGroup");
                Debug.logError("generate MV for " + productStoreId + ", ownerGroup: " + ownerGroup + ", site: " + site + ", foldername: " + folderName, module);
                //f1.write("Account " + productStoreId + ", ownerGroup: " + ownerGroup + ", site: " + site + ", foldername: " + folderName + ", skuPrefix: " + skuPrefix + ", skuSuffix: " + skuSuffix + ", titleSuffix: " + titleSuffix + eol);
                if (UtilValidate.isEmpty(skuPrefix)) {
                    skuPrefix = "";
                }
                if (UtilValidate.isEmpty(skuSuffix)) {
                    skuSuffix = "";
                }
                
                List<GenericValue> accountListingRuleCategoryList = delegator.findByAnd("AccountListingRuleCategory", UtilMisc.toMap("productStoreId", productStoreId, "statusId", "ACTIVE", "site", site, "ownerGroup", ownerGroup, "folderName", folderName), null, false);
                List<String> specialReqAll = new LinkedList<String>();
                for (GenericValue accountListingRuleCategory: accountListingRuleCategoryList) { //loop accountListingRuleCategoryList -- START
                    String specialRequirement = accountListingRuleCategory.getString("specialRequirement");
                    if (UtilValidate.isNotEmpty(specialRequirement)) {
                        specialReqAll.add(specialRequirement);
                        //f1.write("Special Requirement: " + specialRequirement + eol);
                    }
                }   //loop accountListingRuleCategoryList -- END
                
                List<String> activeListingIdAll = new LinkedList<String>();
                /*for (String siteLoop : siteList) {
                 Debug.logError("SiteLoop is " + siteLoop, module);
                 EntityCondition activeMotherVersionCondition = EntityCondition.makeCondition(UtilMisc.toList(
                 EntityCondition.makeCondition("siteId", EntityOperator.EQUALS, siteLoop),
                 EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName),
                 EntityCondition.makeCondition("sku", EntityOperator.IN, activeSkuUnique)
                 ));
                 List<GenericValue> activeMotherVersionList = delegator.findList("MotherVersion", activeMotherVersionCondition, null, null, null, false);
                 for (GenericValue activeMotherVersion : activeMotherVersionList) {  //loop activeMotherVersionList == START ==
                 activeListingIdAll.add(activeMotherVersion.getString("yunqudaoListingIdVar"));
                 }   //loop activeMotherVersionList == END ==
                 }*/
                
                EntityCondition activeMotherVersionCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                             EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site),
                                                                                                             EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName)
                                                                                                             ));
                List<GenericValue> activeMotherVersionList = delegator.findList("MotherVersion", activeMotherVersionCondition, null, null, null, false);
                for (GenericValue activeMotherVersion : activeMotherVersionList) {  //loop activeMotherVersionList == START ==
                    if (activeSkuUnique.contains(activeMotherVersion.getString("sku"))) {
                        activeListingIdAll.add(activeMotherVersion.getString("yunqudaoListingIdVar"));
                    }
                    List<GenericValue> activeMotherVersionVariationList = delegator.findByAnd("MotherVersion", UtilMisc.toMap("yunqudaoListingIdVar", activeMotherVersion.getString("yunqudaoListingIdVar")), null, false);
                    for (GenericValue activeMotherVersionVariation : activeMotherVersionVariationList) {
                        if (activeSkuUnique.contains(activeMotherVersionVariation.getString("sku"))) {
                            activeListingIdAll.add(activeMotherVersionVariation.getString("yunqudaoListingIdVar"));
                        }
                    }
                    
                }   //loop activeMotherVersionList == END ==
                
                HashSet<String> activeListingIdAllHS = new HashSet<String>(activeListingIdAll);
                List<String> activeListingIdUnique = new ArrayList<String>(activeListingIdAllHS);
                Debug.logError("activeListingIdUnique size is " + activeListingIdUnique.size(), module);
                /*f1.write("All YQDListing ID that are on active listing loop START: " + eol);
                 for (String tempListingId : activeListingIdAll) {
                 f1.write(tempListingId + eol);
                 }
                 f1.write("All YQDListing ID that are on active listing loop END" + eol);*/
                EntityCondition checkMotherVersionCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                            EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site),
                                                                                                            EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName)
                                                                                                            ));
                
                List<GenericValue> checkMotherVersionList = delegator.findList("MotherVersion", checkMotherVersionCondition, null, UtilMisc.toList("id"), null, false);
                
                List<String> toBeListedListingIdList = new LinkedList<String>();
                for (GenericValue checkMV : checkMotherVersionList) {   //loop checkMotherVersionList == START ==
                    String productId = checkMV.getString("sku");
                    String yunqudaoListingIdVar = checkMV.getString("yunqudaoListingIdVar");
                    boolean addListingId = false;
                    
                    if (!activeListingIdUnique.contains(yunqudaoListingIdVar)) {
                        GenericValue pm = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
                        if (UtilValidate.isNotEmpty(pm)) {  //if PM is not null -- START
                            if (pm.getString("ownerGroup").equals(ownerGroup)) {
                                addListingId = true;
                                //Checking G4 or G5 -- START
                                int checkSize = 0;
                                List<GenericValue> checkProductGroupList = delegator.findByAnd("MotherVersion", UtilMisc.toMap("yunqudaoListingIdVar", yunqudaoListingIdVar, "motherVersionType", folderName), null, false);
                                for (GenericValue checkProductGroup : checkProductGroupList) {  //loop checkProductGroupList == START ==
                                    GenericValue productCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", checkProductGroup.getString("sku")), false);
                                    if (UtilValidate.isNotEmpty(productCheck)) {
                                        if (productCheck.getString("productGroup").equals("G4") || productCheck.getString("productGroup").equals("G5")) {
                                            checkSize++;
                                        }
                                    }
                                }   //loop checkProductGroupList == END ==
                                if (checkProductGroupList.size() > 1) { //if has variation -- START
                                    if ((checkProductGroupList.size() - 1) == checkSize) {
                                        addListingId = false;
                                    }
                                }   //if has variation -- END
                                else {  //if no variation -- START
                                    if (checkProductGroupList.size() == checkSize) {
                                        addListingId = false;
                                    }
                                }   //if no variation -- END
                                //Checking G4 or G5 -- END
                                
                                if (dangerAccount) {
                                    if (pm.getString("platform").equals("S") || pm.getString("platform").equals("SE")) {
                                        addListingId = false;
                                    }
                                }
                                
                                if (UtilValidate.isNotEmpty(specialReqAll)) {
                                    if(!specialReqAll.contains(pm.getString("categoryIdParent"))) {
                                        addListingId = false;
                                    }
                                }
                                
                            }
                        }   //if PM is not null -- END
                    }
                    
                    if (addListingId) {
                        toBeListedListingIdList.add(yunqudaoListingIdVar);
                    }
                    
                }   //loop checkMotherVersionList == END ==
                HashSet<String> toBeListedListingIdListHS = new HashSet<String>(toBeListedListingIdList);
                List<String> toBeListedListingIdListUnique = new ArrayList<String>(toBeListedListingIdListHS);
                
                Debug.logError("toBeListedListingIdListUnique size is " + toBeListedListingIdListUnique.size(), module);
                /*f1.write("To be Listed YQDListing ID loop START: " + eol);
                 for (String tempTobeListed : toBeListedListingIdListUnique) {
                 f1.write(tempTobeListed + eol);
                 }
                 f1.write("To be Listed YQDListing ID loop END" + eol);*/
                
                
                EntityCondition resultMotherVersionCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                             EntityCondition.makeCondition("yunqudaoListingIdVar", EntityOperator.IN, toBeListedListingIdListUnique),
                                                                                                             EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName),
                                                                                                             EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site)
                                                                                                             ));
                
                List<GenericValue> resultMotherVersionList = delegator.findList("MotherVersion", resultMotherVersionCondition, null, UtilMisc.toList("id"), null, false);
                
                
                for (GenericValue mv : resultMotherVersionList) {   //loop resultMotherVersionList -- START
                    String yunqudaoListingIdVar = mv.getString("yunqudaoListingIdVar");
                    String yunqudaoListingId = mv.getString("yunqudaoListingId");
                    String quantity = mv.getString("quantity");
                    String currency = mv.getString("currency");
                    String sku = mv.getString("sku");
                    String buyItNowPrice = mv.getString("buyItNowPrice");
                    String ebayAccountName = productStoreId;
                    String siteId = mv.getString("siteId");
                    String title = null;
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
                    String dispatchTimeMax = mv.getString("dispatchTimeMax");
                    
                    //Shipping -- START
                    double productPrice = 100000.0;
                    if (UtilValidate.isEmpty(yunqudaoListingId)) {  //loop empty yunqudaoListingId -- START
                        EntityCondition childrenCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                          EntityCondition.makeCondition("yunqudaoListingIdVar", EntityOperator.EQUALS, yunqudaoListingIdVar),
                                                                                                          EntityCondition.makeCondition("yunqudaoListingId", EntityOperator.EQUALS, null),
                                                                                                          EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, folderName),
                                                                                                          EntityCondition.makeCondition("marketplace", EntityOperator.EQUALS, site)
                                                                                                          ));
                        List<GenericValue> childrenList = delegator.findList("MotherVersion", childrenCondition, null, null, null, false);
                        
                        for (GenericValue childrenGv : childrenList) {  //loop childrenList -- START
                            String childSku = childrenGv.getString("sku");
                            double totalPrice = 0.0;
                            GenericValue pm = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", childSku), false);
                            if (UtilValidate.isNotEmpty(pm)) {  //if pm is not empty -- START
                                if (site.equals("US")) {
                                    totalPrice = pm.getBigDecimal("priceUsd").doubleValue();
                                } else if (site.equals("AU")) {
                                    totalPrice = pm.getBigDecimal("priceAud").doubleValue();
                                } else if (site.equals("CA")) {
                                    totalPrice = pm.getBigDecimal("priceCad").doubleValue();
                                } else if (site.equals("UK")) {
                                    totalPrice = pm.getBigDecimal("priceGbp").doubleValue();
                                } else if (site.equals("DE")) {
                                    totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                } else if (site.equals("FR")) {
                                    totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                } else if (site.equals("ES")) {
                                    totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                } else if (site.equals("IT")) {
                                    totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                                }
                            }   //if pm is not empty -- END
                            if (totalPrice <= productPrice) {
                                productPrice = totalPrice;
                            }
                        }   //loop childrenList -- END
                    }   //loop empty yunqudaoListingId -- END
                    else {
                        GenericValue pm = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", sku), false);
                        double totalPrice = 0.0;
                        if (UtilValidate.isNotEmpty(pm)) {  //if pm is not empty -- START
                            if (site.equals("US")) {
                                totalPrice = pm.getBigDecimal("priceUsd").doubleValue();
                            } else if (site.equals("AU")) {
                                totalPrice = pm.getBigDecimal("priceAud").doubleValue();
                            } else if (site.equals("CA")) {
                                totalPrice = pm.getBigDecimal("priceCad").doubleValue();
                            } else if (site.equals("UK")) {
                                totalPrice = pm.getBigDecimal("priceGbp").doubleValue();
                            } else if (site.equals("DE")) {
                                totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                            } else if (site.equals("FR")) {
                                totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                            } else if (site.equals("ES")) {
                                totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                            } else if (site.equals("IT")) {
                                totalPrice = pm.getBigDecimal("priceEur").doubleValue();
                            }
                        }   //if pm is not empty -- END
                        productPrice = totalPrice;
                    }
                    
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
                    }   ////loop intList -- END
                    
                    GenericValue productResult = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", sku), false);
                    double totalPrice = 0.0;
                    if (UtilValidate.isNotEmpty(productResult)) {
                        if (site.equals("US")) {
                            totalPrice = productResult.getBigDecimal("priceUsd").doubleValue();
                        } else if (site.equals("AU")) {
                            totalPrice = productResult.getBigDecimal("priceAud").doubleValue();
                        } else if (site.equals("CA")) {
                            totalPrice = productResult.getBigDecimal("priceCad").doubleValue();
                        } else if (site.equals("UK")) {
                            totalPrice = productResult.getBigDecimal("priceGbp").doubleValue();
                        } else if (site.equals("DE")) {
                            totalPrice = productResult.getBigDecimal("priceEur").doubleValue();
                        } else if (site.equals("FR")) {
                            totalPrice = productResult.getBigDecimal("priceEur").doubleValue();
                        } else if (site.equals("ES")) {
                            totalPrice = productResult.getBigDecimal("priceEur").doubleValue();
                        } else if (site.equals("IT")) {
                            totalPrice = productResult.getBigDecimal("priceEur").doubleValue();
                        }
                        
                        if (totalPrice < 5) {
                            quantity = "20";
                        } else if (totalPrice >= 5 && totalPrice < 30) {
                            quantity = "5";
                        } else if (totalPrice >= 30) {
                            quantity = "3";
                        } else {
                            quantity = "5";
                        }
                        
                        if (productResult.getString("productGroup").equals("G4") || productResult.getString("productGroup").equals("G5")) {
                            quantity = "0";
                        }
                    }
                    
                    buyItNowPrice = totalPrice + "";
                    
                    
                    if (UtilValidate.isEmpty(yunqudaoListingId)) {  //if children line -- START
                        ebayAccountName = productStoreId;
                        siteId = "";
                        title = "";
                        duration = "";
                        privateListing = "";
                        uploadImageEps = "";
                        showImageInDesc = "";
                        location = "";
                        getItFast = "";
                        extendedHolidayReturns = "";
                        bRLinkedPayPalAccount = "";
                        shippingService1Option = "";
                        shippingService1Cost = "";
                        shippingService1AddCost = "";
                        shippingService2Option = "";
                        shippingService2Cost = "";
                        shippingService2AddCost = "";
                        shippingService3Option = "";
                        shippingService3Cost = "";
                        shippingService3AddCost = "";
                        shippingService4Option = "";
                        shippingService4Cost = "";
                        shippingService4AddCost = "";
                        intShippingService1Option = "";
                        intShippingService1Cost = "";
                        intShippingService1AddCost = "";
                        intShippingService1Locations = "";
                        intShippingService2Option = "";
                        intShippingService2Cost = "";
                        intShippingService2AddCost = "";
                        intShippingService2Locations = "";
                        intShippingService3Option = "";
                        intShippingService3Cost = "";
                        intShippingService3AddCost = "";
                        intShippingService3Locations = "";
                        intShippingService4Option = "";
                        intShippingService4Cost = "";
                        intShippingService4AddCost = "";
                        intShippingService4Locations = "";
                        intShippingService5Option = "";
                        intShippingService5Cost = "";
                        intShippingService5AddCost = "";
                        intShippingService5Locations = "";
                    }   //if children line -- END
                    else {  //if mother line -- START
                        title = mv.getString("title").substring(0, mv.getString("title").length() - 4) + " " + titleSuffix;
                        upc = "Does not apply";
                        if (UtilValidate.isNotEmpty(productResult)) {
                            if (UtilValidate.isNotEmpty(productResult.getString("upc"))) {
                                upc = productResult.getString("upc");
                            }
                        }
                        
                        ean = "Does not apply";
                        isbn = "Does not apply";
                        brandMpnBrand = "WHATWEARS";
                        brandMpnMpn = "Does not apply";
                        
                    }   //if mother line -- END
                    
                    //Debug.logError("first shippingService1Cost is : " + shippingService1Cost, module);
                    
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
                    //Shipping -- END
                    //Debug.logError("after foler shippingService1Cost is : " + shippingService1Cost, module);
                    
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
                    String storeCategory = mv.getString("storeCategory");
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
                    /*if (ServiceUtil.isError(createMotherVersionResult)) {  //if result gives error -- START
                     result = ServiceUtil.returnError(ServiceUtil.getErrorMessage(createMotherVersionResult));
                     }   //if result gives error -- END */
                    
                    //f1.write("folderName: " + folderName + "; skuPrefix: " + skuPrefix + "; skuSuffix: " + skuSuffix + "; ownerGroup: " + ownerGroup + "; sku: " + sku + "; productPrice: " + productPrice + "; filter: " + filter + "; buyItNowPrice: " + buyItNowPrice + eol);
                    
                }   //loop resultMotherVersionList -- END
                
                //f1.close();
            }   //loop accountListingRuleList -- END
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
        }   //main try -- END
        Debug.logError(productStoreId + " finished", module);
        return ServiceUtil.returnSuccess();
        
    }   //gudaoGenerateMotherVersion
    
    public static Map<String, Object> gudaoGenerateChildrenSku (DispatchContext dctx, Map<String, ? extends Object> context) {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        List<String> motherSkuList = (List) context.get("motherSkuList");
        
        try {   //main try -- START
            List<String> siteList = new LinkedList<String>();
            siteList.add("US");
            siteList.add("eBayMotors");
            
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                    EntityCondition.makeCondition("marketplace", EntityOperator.IN, siteList),
                                                                                    EntityCondition.makeCondition("motherVersionType", EntityOperator.EQUALS, "ST1FREE"),
                                                                                    EntityCondition.makeCondition("sku", EntityOperator.IN, motherSkuList)
                                                                                    ));
            List<GenericValue> motherVersionAll = delegator.findList("MotherVersion", condition, null, null, null, false);
            List<String> listingIdList = new LinkedList<String>();
            for (GenericValue mvAll : motherVersionAll) {   //loop motherVersionAll -- START
                listingIdList.add(mvAll.getString("yunqudaoListingIdVar"));
            }   //loop motherVersionAll -- END
            HashSet<String> listingIdHS = new HashSet<String>(listingIdList);
            List<String> listingIdUnique = new ArrayList<String>(listingIdHS);
            
            for (String listingId : listingIdUnique) {  //loop resultMv -- START
                String childrenSku = null;
                String motherSku = null;
                List<GenericValue> mvList = delegator.findByAnd("MotherVersion", UtilMisc.toMap("yunqudaoListingIdVar", listingId), null, false);
                for (GenericValue mv : mvList) {    //loop mvList -- START
                    String productId = mv.getString("sku");
                    GenericValue product = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
                    if (UtilValidate.isNotEmpty(product)) {
                        childrenSku += "," + productId;
                    } else {
                        motherSku = productId;
                    }
                }   //loop mvList -- END
                childrenSku = childrenSku.substring(6,childrenSku.length()).trim();
                //Debug.logError("motherSku: " + motherSku + ", childrenSku: " + childrenSku, module);
                GenericValue gv = delegator.makeValue("GudaoMotherChildrenSku", UtilMisc.toMap("motherSku", motherSku, "childrenSku", childrenSku));
                delegator.createOrStore(gv);
                
            }   //loop resultMv -- END
            
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
        }   //main try -- END
        
        return result;
        
    }
    
    public static Map<String, Object> rivalCrawl(DispatchContext dctx, Map context)	//Description
    throws GenericEntityException, GenericServiceException {    //updateEbayActiveListing
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        //String productStoreId = (String) context.get("productStoreId");
        String productStoreId = (String) context.get("ebayUserId");
        String pageNumber = (String) context.get("pageNumber");
        String continuousModeStr = (String) context.get("continuousMode");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        boolean continuousMode = false;
        if (continuousModeStr.equals("Y") || continuousModeStr.equals("y")) {
            continuousMode = true;
        }
        result.put("productStoreId", productStoreId);
        if(UtilValidate.isEmpty(pageNumber)) {
            pageNumber = "1";
        }
        try {
            Map getSellerList = dispatcher.runSync("TradingApiGetSellerListRival", UtilMisc.toMap("productStoreId", productStoreId, "pageNumber", pageNumber, "userLogin", userLogin));
        }
        catch (GenericServiceException e) {
            e.printStackTrace();
            Debug.logError(productStoreId + ": return GenericServiceException: " + e.getMessage(), module);
            Map verifyUpdateListing = dispatcher.runSync("TradingApiVerifyUpdateListing", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
            //return ServiceUtil.returnError(e.getMessage());
        }
        
        Debug.logError(productStoreId + ": finished running rivalCrawl", module);
        Map verifyUpdateListing = dispatcher.runSync("TradingApiVerifyUpdateListing", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
        
        
        if (continuousMode) {
            dispatcher.runSync("autoScheduleJob", UtilMisc.toMap("serviceName", "rivalCrawl", "userLogin", userLogin));
        }
        
        return result;
    }   //rivalCrawl
    
    public static Map<String, Object> getSellerListRival(DispatchContext dctx, Map context)
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
                            Map writeIntoGetSellerList = writeIntoGetSellerListDataRival(dctx, mapAccount, responseXML);
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
        Debug.logError(productStoreId + ": finished getSellerListRival", module);
        
        
        return result;
        
    }   //getSellerListRival
    
    
    public static Map<String, Object> writeIntoGetSellerListDataRival(DispatchContext dctx, Map mapContent, String responseXML)
    throws GenericEntityException, GenericServiceException, SAXException, ParserConfigurationException, IOException { //writeIntoGetSellerListData
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        //formatting the sequence ID
        DecimalFormat df = new DecimalFormat("00000");
        String productStoreId = mapContent.get("productStoreId").toString();
        Calendar now = Calendar.getInstance();
        Date nowDate = new Date();
        java.sql.Date crawlDate = new java.sql.Date(nowDate.getTime());
        
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
                            GenericValue sdi = delegator.makeValue("RivalActiveListingShipping", UtilMisc.toMap("listingShippingId", listingShippingId, "activeListingId", ebayActiveListingId));
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
                            sdi.set("crawlDate", crawlDate);
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
                            String listingShippingIdDom = delegator.getNextSeqId("RivalActiveListingShipping");
                            GenericValue sd = delegator.makeValue("RivalActiveListingShipping", UtilMisc.toMap("listingShippingId", listingShippingIdDom,"activeListingId", ebayActiveListingId));
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
                            sd.set("crawlDate", crawlDate);
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
                                        String varSpecsId = delegator.getNextSeqId("RivalVariationSpecifics");
                                        GenericValue listingVariationSpecifics = delegator.makeValue("RivalVariationSpecifics", UtilMisc.toMap("varSpecsId", varSpecsId, "activeListingId", ebayActiveListingId));
                                        listingVariationSpecifics.set("productStoreId", productStoreId);
                                        listingVariationSpecifics.set("itemId", itemId);
                                        listingVariationSpecifics.set("variationSeqId", variationSeqId);
                                        listingVariationSpecifics.set("productId", productIdVariation);
                                        listingVariationSpecifics.set("varSpecsName", varSpecsMap.get("varSpecsName"));
                                        listingVariationSpecifics.set("varSpecsValue", varSpecsMap.get("varSpecsValue"));
                                        listingVariationSpecifics.set("crawlDate", crawlDate);
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
                                ebayActiveListingVariation.set("crawlDate", crawlDate);
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
                        getSellerListData.set("crawlDate", crawlDate);
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
                Debug.logError(productStoreId + ": pageNumber " + pageNumber + " writeIntoGetSellerListDataRival gives error from ResponseXML: " + errorMessage, module);
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
        
    }   //writeIntoGetSellerListDataRival

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



    
}	//END class