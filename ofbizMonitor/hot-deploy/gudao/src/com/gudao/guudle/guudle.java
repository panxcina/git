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
package com.gudao.guudle;

import java.sql.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.ofbiz.entity.condition.EntityExpr;
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
import com.bellyanna.ebay.eBayTradingAPI;

public class guudle {
	private static final String module = guudle.class.getName();
    private static final String eol = System.getProperty("line.separator");
	
    public static Map<String, Object> updateProductGps (DispatchContext dctx, Map<String, ? extends Object> context) {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Map result = ServiceUtil.returnSuccess();
        Calendar fromDay = Calendar.getInstance();
        fromDay.add(Calendar.DATE, -30);
        java.sql.Date fromDate = new java.sql.Date(fromDay.getTimeInMillis());
        
        String productIdInput = (String) context.get("productIdInput");
        String ownerGroupInput = (String) context.get("ownerGroupInput");
        String productGroupInput = (String) context.get("productGroupInput");

        //Debug.logError("Running updateProductGps", module);
        
        try {   //main try -- START
            EntityCondition productListCondition = null;
            
            if (UtilValidate.isNotEmpty(productGroupInput)) {   //productListCondition == START
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, productGroupInput)
                                                                                     ));
            } else if (UtilValidate.isNotEmpty(ownerGroupInput)) {
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.NOT_IN, UtilMisc.toList("G4", "G5")),
                                                                                     EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroupInput)
                                                                                     ));
            } else if (UtilValidate.isNotEmpty(productIdInput)) {
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.NOT_IN, UtilMisc.toList("G4", "G5")),
                                                                                     EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productIdInput)
                                                                                     ));
            } else {
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.NOT_IN, UtilMisc.toList("G4", "G5"))
                                                                                     ));
            }//productListCondition == END
            List<GenericValue> productList = delegator.findList("ProductMaster", productListCondition, null, null, null, false);
            //Debug.logError("productList size is " + productList.size(), module);
            for (GenericValue product : productList) {  //loop productList == START
                String productId = product.getString("productId");
                String ownerGroup = product.getString("ownerGroup");
                GenericValue ebayUsPrice = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "US"), false);
                //Debug.logError("productId " + productId, module);
                
                double rivalDailySoldValue = 0.0;
                long rivalDailySoldScore = 0;
                long rivalDailySoldWeight = 0;
                long rivalDailySoldGps = 0;
                
                double rivalDailySalesValue = 0.0;
                long rivalDailySalesScore = 0;
                long rivalDailySalesWeight = 0;
                long rivalDailySalesGps = 0;
                
                double samePriceProfitPctValue = 0.0;
                long samePriceProfitPctScore = 0;
                long samePriceProfitPctWeight = 0;
                long samePriceProfitPctGps = 0;
                
                long supplierRankValue = 99999;
                long supplierRankScore = 0;
                long supplierRankWeight = 0;
                long supplierRankGps = 0;
                
                String sbuParentCategoryValue = "N";
                long sbuParentCategoryScore = 0;
                long sbuParentCategoryWeight = 0;
                long sbuParentCategoryGps = 0;
                
                String sbuChildrenCategoryValue = "N";
                long sbuChildrenCategoryScore = 0;
                long sbuChildrenCategoryWeight = 0;
                long sbuChildrenCategoryGps = 0;
                
                String amazonPictureValue = "N";
                long amazonPictureScore = 0;
                long amazonPictureWeight = 0;
                long amazonPictureGps = 0;
                
                String multiPlatformRivalValue = "N";
                long multiPlatformRivalScore = 0;
                long multiPlatformRivalWeight = 0;
                long multiPlatformRivalGps = 0;
                
                //get Rival data == START
                long variationCount = 1;
                List<GenericValue> rivalList = delegator.findByAnd("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalPlatform", "EBAY"), null, false);
                if (UtilValidate.isNotEmpty(rivalList)) {   //rivalList is not Empty == START
                    String rivalItemId = EntityUtil.getFirst(rivalList).getString("rivalItemId");
                    EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                              EntityCondition.makeCondition("rivalItemId",EntityOperator.EQUALS , rivalItemId),
                                                                                              EntityCondition.makeCondition("date",EntityOperator.GREATER_THAN , fromDate)
                                                                                              ));
                    List<GenericValue> rivalListingResultList = delegator.findList("RivalListingResult", condition, null, UtilMisc.toList("date ASC"), null, false);
                    if (rivalListingResultList.size() > 1) {    //if Crawling has run for one month == START
                        java.sql.Date firstSnapshotDate = EntityUtil.getFirst(rivalListingResultList).getDate("date");
                        //Debug.logError(fromDate + "", module);
                        java.sql.Date lastSnapshotDate = null;
                        
                        double firstHistorySold = EntityUtil.getFirst(rivalListingResultList).getDouble("rivalHistorySold");
                        double lastHistorySold = 0.0;
                        double listingResultHistorySales = 0.0;
                        double previousHistorySold = firstHistorySold;
                        
                        for (GenericValue rivalListingResult : rivalListingResultList) {    // loop rivalListingResultList == START
                            if (UtilValidate.isNotEmpty(rivalListingResult.getLong("variationCount"))) {
                                variationCount = rivalListingResult.getLong("variationCount");
                            }
                            lastSnapshotDate = rivalListingResult.getDate("date");
                            lastHistorySold = rivalListingResult.getDouble("rivalHistorySold");
                            double rivalCurrentPrice = rivalListingResult.getDouble("rivalCurrentPrice");
                            listingResultHistorySales = listingResultHistorySales + ((lastHistorySold - previousHistorySold) * rivalCurrentPrice);
                            //Debug.logError("listingResultHistorySales: " + listingResultHistorySales + ", lastHistorySold: " + lastHistorySold + ", previousHistorySold: " + previousHistorySold, module);
                            previousHistorySold = lastHistorySold;
                        }   // loop rivalListingResultList == START
                        double dayDiff = (double) Math.abs(lastSnapshotDate.getTime() - firstSnapshotDate.getTime()) / (24 * 60 * 60 * 1000);
                        //Debug.logError("listingResultHistorySales: " + listingResultHistorySales + ", firstHistorySold: "+ firstHistorySold + ", lastHistorySold: " + lastHistorySold + ", dayDiff: " + dayDiff, module);
                        
                        if (dayDiff >= 6) { //if gap calculation day between first and second crawl more than equals to 6 days == START
                            rivalDailySoldValue = (lastHistorySold - firstHistorySold) / dayDiff;
                            rivalDailySalesValue = listingResultHistorySales / dayDiff;
                        }   //if gap calculation day between first and second crawl more than equals to 6 days == END
                        else {
                            if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("soldPerDay"))) {
                                rivalDailySoldValue = EntityUtil.getFirst(rivalList).getDouble("soldPerDay");
                            } else {
                                rivalDailySoldValue = 0.0;
                            }
                            if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("dailySales"))) {
                                rivalDailySalesValue = EntityUtil.getFirst(rivalList).getDouble("dailySales");
                            } else {
                                rivalDailySalesValue = 0.0;
                            }
                        }
                    }   //if Crawling has run for one month == END
                    else {  //if Crawling has NOT run for one month divides by listing lifetime == START
                        if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getLong("variationCount"))) {
                            variationCount = EntityUtil.getFirst(rivalList).getLong("variationCount");
                        }
                        if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("soldPerDay"))) {
                            rivalDailySoldValue = EntityUtil.getFirst(rivalList).getDouble("soldPerDay");
                        } else {
                            rivalDailySoldValue = 0.0;
                        }
                        if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("dailySales"))) {
                            rivalDailySalesValue = EntityUtil.getFirst(rivalList).getDouble("dailySales");
                        } else {
                            rivalDailySalesValue = 0.0;
                        }
                    }   //if Crawling has NOT run for one month divides by listing lifetime == END

                    if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("samePriceProfitPercentage"))) {
                        samePriceProfitPctValue = EntityUtil.getFirst(rivalList).getDouble("samePriceProfitPercentage");
                    } else {
                        samePriceProfitPctValue = ebayUsPrice.getDouble("profitPercentage");
                    }
                }   //rivalList is not Empty == END
                else {  //rivalList is Empty == START
                    rivalDailySoldValue = 0.0;
                    rivalDailySalesValue = 0.0;
                    samePriceProfitPctValue = ebayUsPrice.getDouble("profitPercentage");
                }   //rivalList is Empty == END
                rivalDailySoldValue = rivalDailySoldValue / (double) variationCount / 2;
                rivalDailySalesValue = rivalDailySalesValue / (double) variationCount / 2;
                //get Rival data == END
                
                //get supplier rank data == START
                GenericValue supplierRank = delegator.findOne("SupplierRank", UtilMisc.toMap("supplier", product.getString("mainSupplier")), false);
                supplierRankValue = supplierRank.getLong("rank");
                //get supplier rank data == END
                
                //get category data == START
                if (UtilValidate.isNotEmpty(product.getString("categoryIdParent"))) {   //if PM categoryIdParent is not empty == START
                    List<GenericValue> parentCategoryList = delegator.findByAnd("SbuCategoryParent", UtilMisc.toMap("sbu", ownerGroup), null, false);
                    if (UtilValidate.isNotEmpty(parentCategoryList)) {  //loop parentCategoryList == START
                        String parentCategoryMatch = EntityUtil.getFirst(parentCategoryList).getString("categoryIdParent");
                        //Debug.logError("PM category: " + product.getString("categoryIdParent") + ", GPS category: " + parentCategoryMatch, module);
                        if (product.getString("categoryIdParent").equals(parentCategoryMatch)) {
                            sbuParentCategoryValue = "Y";
                        }
                    }   //loop parentCategoryList == END
                }   //if PM categoryIdParent is not empty == END
                
                if (UtilValidate.isNotEmpty(product.getString("categoryIdChild"))) {    //if PM categoryIdChild is not empty == START
                    List<GenericValue> childrenCategoryList = delegator.findByAnd("SbuCategoryChildren", UtilMisc.toMap("sbu", ownerGroup), null, false);
                    if (UtilValidate.isNotEmpty(childrenCategoryList)) {    //if childrenCategoryList is not empty == START
                        for (GenericValue childrenCategory : childrenCategoryList) {    //loop childrenCategoryList == START
                            String childrenCategoryMatch = childrenCategory.getString("categoryIdChild");
                            if (product.getString("categoryIdChild").equals(childrenCategoryMatch)) {
                                sbuChildrenCategoryValue = "Y";
                            }
                        }   //loop childrenCategoryList == END
                    }   //if childrenCategoryList is not empty == END
                }   //if PM categoryIdChild is not empty == END
                //get category data == END
                
                
                //get amazonPicture data == START
                if (UtilValidate.isNotEmpty(product.getString("amazonPicture"))) {
                    if (product.getString("amazonPicture").substring(0,1).toUpperCase().equals("Y")) {
                        amazonPictureValue = "Y";
                    }
                }
                //get amazonPicture data == END
                
                //get multiplatform rival == START
                for (String rivalPlatform : Arrays.asList("SMT","WISH", "AMAZON")) {    //loop ProductMasterRival == START
                    GenericValue rivalGV = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalPlatform", rivalPlatform), false);
                    if (UtilValidate.isNotEmpty(rivalGV)) {
                        multiPlatformRivalValue ="Y";
                    }
                }   //loop ProductMasterRival == END
                //get multiplatform rival == END
                //Debug.logError("safely run until here", module);
                List<String> factorList = new ArrayList<String>();
                factorList.add("RIVAL_DAILY_SOLD");
                factorList.add("RIVAL_DAILY_SALES");
                factorList.add("SAME_PRICE_PROFIT_PCT");
                factorList.add("SUPPLIER_RANK");
                factorList.add("SBU_PARENT_CATEGORY");
                factorList.add("SBU_CHILDREN_CATEGORY");
                factorList.add("AMAZON_PICTURE");
                factorList.add("MULTI_PLATFORM_RIVAL");
                
                //Debug.logError("looping gpsAlgorithm", module);
                for (String factor : factorList) {  //loop factorList == START
                    List<GenericValue> gpsAlgorithmList = delegator.findByAnd("GpsAlgorithm", UtilMisc.toMap("factor", factor), null, false);
                    for (GenericValue gpsAlgorithm : gpsAlgorithmList) {    //loop gpsAlgorithmRivalDailySoldList == START
                        boolean useRivalDailySold = false;
                        boolean useRivalDailySales = false;
                        boolean useSamePriceProfitPct = false;
                        boolean useSupplierRank = false;
                        boolean useSbuParentCategory = false;
                        boolean useSbuChildrenCategory = false;
                        boolean useAmazonPicture = false;
                        boolean useMultiPlatformRival = false;
                        
                        long score = gpsAlgorithm.getLong("score");
                        long weight = gpsAlgorithm.getLong("weight");
                        String comparator1 = gpsAlgorithm.getString("comparator1");
                        double value1 = 0.0;
                        long value1Long = 0;
                        if (UtilValidate.isNotEmpty(gpsAlgorithm.getDouble("value1"))) {
                            value1 = gpsAlgorithm.getDouble("value1");
                            value1Long = Math.round(value1);
                        }

                        String value1Str = gpsAlgorithm.getString("value1Str");
                        String joinOperator = gpsAlgorithm.getString("joinOperator");
                        /*if (UtilValidate.isNotEmpty(gpsAlgorithm.getString("joinOperator"))) {
                            joinOperator = gpsAlgorithm.getString("joinOperator");
                        }*/
                        String comparator2 = gpsAlgorithm.getString("comparator2");
                        double value2 = 0.0;
                        long value2Long = 0;
                        if (UtilValidate.isNotEmpty(gpsAlgorithm.getDouble("value2"))) {
                            value2 = gpsAlgorithm.getDouble("value2");
                            value2Long = Math.round(value2);
                        }
                        String value2Str = gpsAlgorithm.getString("value2Str");
                        //Debug.logError("factor: " + factor + ", value1: " + value1 + ", value2: " + value2 + ", value1Str:  " + value1Str + ", joinOperator: " + joinOperator, module);
                        
                        if (UtilValidate.isEmpty(joinOperator)) {   //joinOperator EMPTY == START
                            if (comparator1.equals("EQUALS")) { //EQUALS
                                if (factor.equals("RIVAL_DAILY_SOLD")) {
                                    if (rivalDailySoldValue == value1) {
                                        useRivalDailySold = true;
                                    }
                                } else if (factor.equals("RIVAL_DAILY_SALES")) {
                                    if (rivalDailySalesValue == value1) {
                                        useRivalDailySales = true;
                                    }
                                } else if (factor.equals("SAME_PRICE_PROFIT_PCT")) {
                                    if (samePriceProfitPctValue == value1) {
                                        useSamePriceProfitPct = true;
                                    }
                                } else if (factor.equals("SUPPLIER_RANK")) {
                                    if (supplierRankValue == value1Long) {
                                        useSupplierRank = true;
                                    }
                                }  else if (factor.equals("SBU_PARENT_CATEGORY")) {
                                    if (sbuParentCategoryValue.equals(value1Str)) {
                                        useSbuParentCategory = true;
                                    }
                                } else if (factor.equals("SBU_CHILDREN_CATEGORY")) {
                                    if (sbuChildrenCategoryValue.equals(value1Str)) {
                                        useSbuChildrenCategory = true;
                                    }
                                } else if (factor.equals("AMAZON_PICTURE")) {
                                    if (amazonPictureValue.equals(value1Str)) {
                                        useAmazonPicture = true;
                                    }
                                } else if (factor.equals("MULTI_PLATFORM_RIVAL")) {
                                    if (multiPlatformRivalValue.equals(value1Str)) {
                                        useMultiPlatformRival = true;
                                    }
                                }
                            }   //EQUALS
                            else if (comparator1.equals("LESS_THAN")) { //LESS_THAN
                                if (factor.equals("RIVAL_DAILY_SOLD")) {
                                    if (rivalDailySoldValue < value1) {
                                        useRivalDailySold = true;
                                    }
                                } else if (factor.equals("RIVAL_DAILY_SALES")) {
                                    if (rivalDailySalesValue < value1) {
                                        useRivalDailySales = true;
                                    }
                                } else if (factor.equals("SAME_PRICE_PROFIT_PCT")) {
                                    if (samePriceProfitPctValue < value1) {
                                        useSamePriceProfitPct = true;
                                    }
                                } else if (factor.equals("SUPPLIER_RANK")) {
                                    if (supplierRankValue < value1) {
                                        useSupplierRank = true;
                                    }
                                }
                            }   //LESS_THAN
                            else if (comparator1.equals("LESS_THAN_EQUAL_TO")) {    //LESS_THAN_EQUAL_TO
                                if (factor.equals("RIVAL_DAILY_SOLD")) {
                                    if (rivalDailySoldValue <= value1) {
                                        useRivalDailySold = true;
                                    }
                                } else if (factor.equals("RIVAL_DAILY_SALES")) {
                                    if (rivalDailySalesValue <= value1) {
                                        useRivalDailySales = true;
                                    }
                                } else if (factor.equals("SAME_PRICE_PROFIT_PCT")) {
                                    if (samePriceProfitPctValue <= value1) {
                                        useSamePriceProfitPct = true;
                                    }
                                } else if (factor.equals("SUPPLIER_RANK")) {
                                    if (supplierRankValue <= value1) {
                                        useSupplierRank = true;
                                    }
                                }
                            }   //LESS_THAN_EQUAL_TO
                            else if (comparator1.equals("GREATER_THAN")) {  //GREATER_THAN
                                if (factor.equals("RIVAL_DAILY_SOLD")) {
                                    if (rivalDailySoldValue > value1) {
                                        useRivalDailySold = true;
                                    }
                                } else if (factor.equals("RIVAL_DAILY_SALES")) {
                                    if (rivalDailySalesValue > value1) {
                                        useRivalDailySales = true;
                                    }
                                } else if (factor.equals("SAME_PRICE_PROFIT_PCT")) {
                                    if (samePriceProfitPctValue > value1) {
                                        useSamePriceProfitPct = true;
                                    }
                                } else if (factor.equals("SUPPLIER_RANK")) {
                                    if (supplierRankValue > value1) {
                                        useSupplierRank = true;
                                    }
                                }
                            }   //GREATER_THAN
                            else if (comparator1.equals("GREATER_THAN_EQUAL_TO")) { //GREATER_THAN_EQUAL_TO
                                if (factor.equals("RIVAL_DAILY_SOLD")) {
                                    if (rivalDailySoldValue >= value1) {
                                        useRivalDailySold = true;
                                    }
                                } else if (factor.equals("RIVAL_DAILY_SALES")) {
                                    if (rivalDailySalesValue >= value1) {
                                        useRivalDailySales = true;
                                    }
                                } else if (factor.equals("SAME_PRICE_PROFIT_PCT")) {
                                    if (samePriceProfitPctValue >= value1) {
                                        useSamePriceProfitPct = true;
                                    }
                                } else if (factor.equals("SUPPLIER_RANK")) {
                                    if (supplierRankValue >= value1) {
                                        useSupplierRank = true;
                                    }
                                }
                            }   //GREATER_THAN_EQUAL_TO
                            else if (comparator1.equals("NOT_EQUAL")) { //NOT_EQUAL
                                if (factor.equals("RIVAL_DAILY_SOLD")) {
                                    if (rivalDailySoldValue != value1) {
                                        useRivalDailySold = true;
                                    }
                                } else if (factor.equals("RIVAL_DAILY_SALES")) {
                                    if (rivalDailySalesValue != value1) {
                                        useRivalDailySales = true;
                                    }
                                } else if (factor.equals("SAME_PRICE_PROFIT_PCT")) {
                                    if (samePriceProfitPctValue != value1) {
                                        useSamePriceProfitPct = true;
                                    }
                                } else if (factor.equals("SUPPLIER_RANK")) {
                                    if (supplierRankValue != value1Long) {
                                        useSupplierRank = true;
                                    }
                                } else if (factor.equals("SBU_PARENT_CATEGORY")) {
                                    if (!sbuParentCategoryValue.equals(value1Str)) {
                                        useSbuParentCategory = true;
                                    }
                                } else if (factor.equals("SBU_CHILDREN_CATEGORY")) {
                                    if (!sbuChildrenCategoryValue.equals(value1Str)) {
                                        useSbuChildrenCategory = true;
                                    }
                                } else if (factor.equals("AMAZON_PICTURE")) {
                                    if (!amazonPictureValue.equals(value1Str)) {
                                        useAmazonPicture = true;
                                    }
                                } else if (factor.equals("MULTI_PLATFORM_RIVAL")) {
                                    if (!multiPlatformRivalValue.equals(value1Str)) {
                                        useMultiPlatformRival = true;
                                    }
                                }
                            }   //NOT_EQUAL
                        }   //joinOperator EMPTY == END
                        else {  //joinOperator NOT EMPTY == START
                            if (joinOperator.equals("AND")) {   //joinOperator AND == START
                                if (comparator1.equals("GREATER_THAN") && comparator2.equals("LESS_THAN")) { //GREATER_THAN - LESS_THAN == START
                                    if (factor.equals("RIVAL_DAILY_SOLD")) {
                                        if (rivalDailySoldValue > value1 && rivalDailySoldValue < value2) {
                                            useRivalDailySold = true;
                                        }
                                    } else if (factor.equals("RIVAL_DAILY_SALES")) {
                                        if (rivalDailySalesValue > value1 && rivalDailySalesValue < value2) {
                                            useRivalDailySales = true;
                                        }
                                    } else if (factor.equals("SAME_PRICE_PROFIT_PCT")) {
                                        if (samePriceProfitPctValue > value1 && samePriceProfitPctValue < value2) {
                                            useSamePriceProfitPct = true;
                                        }
                                    } else if (factor.equals("SUPPLIER_RANK")) {
                                        if (supplierRankValue > value1 && supplierRankValue < value2) {
                                            useSupplierRank = true;
                                        }
                                    }
                                }   //GREATER_THAN - LESS_THAN == END
                                else if (comparator1.equals("GREATER_THAN") && comparator2.equals("LESS_THAN_EQUAL_TO")) { //GREATER_THAN - LESS_THAN_EQUAL_TO == START
                                    if (factor.equals("RIVAL_DAILY_SOLD")) {
                                        if (rivalDailySoldValue > value1 && rivalDailySoldValue <= value2) {
                                            useRivalDailySold = true;
                                        }
                                    } else if (factor.equals("RIVAL_DAILY_SALES")) {
                                        if (rivalDailySalesValue > value1 && rivalDailySalesValue <= value2) {
                                            useRivalDailySales = true;
                                        }
                                    } else if (factor.equals("SAME_PRICE_PROFIT_PCT")) {
                                        if (samePriceProfitPctValue > value1 && samePriceProfitPctValue <= value2) {
                                            useSamePriceProfitPct = true;
                                        }
                                    } else if (factor.equals("SUPPLIER_RANK")) {
                                        if (supplierRankValue > value1 && supplierRankValue <= value2) {
                                            useSupplierRank = true;
                                        }
                                    }
                                }   //GREATER_THAN - LESS_THAN_EQUAL_TO == END
                                else if (comparator1.equals("GREATER_THAN_EQUAL_TO") && comparator2.equals("LESS_THAN")) { //GREATER_THAN_EQUAL_TO - LESS_THAN == START
                                    if (factor.equals("RIVAL_DAILY_SOLD")) {
                                        if (rivalDailySoldValue >= value1 && rivalDailySoldValue < value2) {
                                            useRivalDailySold = true;
                                        }
                                    } else if (factor.equals("RIVAL_DAILY_SALES")) {
                                        if (rivalDailySalesValue >= value1 && rivalDailySalesValue < value2) {
                                            useRivalDailySales = true;
                                        }
                                    } else if (factor.equals("SAME_PRICE_PROFIT_PCT")) {
                                        if (samePriceProfitPctValue >= value1 && samePriceProfitPctValue < value2) {
                                            useSamePriceProfitPct = true;
                                        }
                                    } else if (factor.equals("SUPPLIER_RANK")) {
                                        if (supplierRankValue >= value1 && supplierRankValue < value2) {
                                            useSupplierRank = true;
                                        }
                                    }
                                }   //GREATER_THAN_EQUAL_TO - LESS_THAN == END
                                else if (comparator1.equals("GREATER_THAN_EQUAL_TO") && comparator2.equals("LESS_THAN_EQUAL_TO")) { //GREATER_THAN_EQUAL_TO - LESS_THAN_EQUAL_TO == START
                                    if (factor.equals("RIVAL_DAILY_SOLD")) {
                                        if (rivalDailySoldValue >= value1 && rivalDailySoldValue <= value2) {
                                            useRivalDailySold = true;
                                        }
                                    } else if (factor.equals("RIVAL_DAILY_SALES")) {
                                        if (rivalDailySalesValue >= value1 && rivalDailySalesValue <= value2) {
                                            useRivalDailySales = true;
                                        }
                                    } else if (factor.equals("SAME_PRICE_PROFIT_PCT")) {
                                        if (samePriceProfitPctValue >= value1 && samePriceProfitPctValue <= value2) {
                                            useSamePriceProfitPct = true;
                                        }
                                    } else if (factor.equals("SUPPLIER_RANK")) {
                                        if (supplierRankValue >= value1 && supplierRankValue <= value2) {
                                            useSupplierRank = true;
                                        }
                                    }
                                }   //GREATER_THAN_EQUAL_TO - LESS_THAN_EQUAL_TO == END
                            }   //joinOperator AND == END
                            else if (joinOperator.equals("OR")) {   //joinOperator OR == START
                                //TODO
                            }   //joinOperator OR == START
                        }   //joinOperator NOT EMPTY == END
                        
                        if (useRivalDailySold) {
                            rivalDailySoldScore = score;
                            rivalDailySoldWeight = weight;
                            //rivalDailySoldGps = Long.valueOf(Math.round(rivalDailySoldScore * rivalDailySoldWeight / 100));
                            rivalDailySoldGps = rivalDailySoldScore * rivalDailySoldWeight / 100;
                        }
                        if (useRivalDailySales) {
                            rivalDailySalesScore = score;
                            rivalDailySalesWeight = weight;
                            //rivalDailySalesGps = Long.valueOf(Math.round(rivalDailySalesScore * rivalDailySalesWeight / 100));
                            rivalDailySalesGps = rivalDailySalesScore * rivalDailySalesWeight / 100;
                        }
                        if (useSamePriceProfitPct) {
                            samePriceProfitPctScore = score;
                            samePriceProfitPctWeight = weight;
                            //samePriceProfitPctGps = Long.valueOf(Math.round(samePriceProfitPctScore * samePriceProfitPctWeight / 100));
                            samePriceProfitPctGps = samePriceProfitPctScore * samePriceProfitPctWeight / 100;
                        }
                        if (useSupplierRank) {
                            supplierRankScore = score;
                            supplierRankWeight = weight;
                            //supplierRankGps = Long.valueOf(Math.round(supplierRankScore * supplierRankWeight / 100));
                            supplierRankGps = supplierRankScore * supplierRankWeight / 100;
                        }
                        if (useSbuParentCategory) {
                            sbuParentCategoryScore = score;
                            sbuParentCategoryWeight = weight;
                            //sbuParentCategoryGps = Long.valueOf(Math.round(sbuParentCategoryScore * sbuParentCategoryWeight / 100));
                            sbuParentCategoryGps = sbuParentCategoryScore * sbuParentCategoryWeight / 100;
                        }
                        if (useSbuChildrenCategory) {
                            sbuChildrenCategoryScore = score;
                            sbuChildrenCategoryWeight = weight;
                            //sbuChildrenCategoryGps = Long.valueOf(Math.round(sbuChildrenCategoryScore * sbuChildrenCategoryWeight / 100));
                            sbuChildrenCategoryGps = sbuChildrenCategoryScore * sbuChildrenCategoryWeight / 100;
                        }
                        if (useAmazonPicture) {
                            amazonPictureScore = score;
                            amazonPictureWeight = weight;
                            //amazonPictureGps = Long.valueOf(Math.round(amazonPictureScore * amazonPictureWeight / 100));
                            amazonPictureGps = amazonPictureScore * amazonPictureWeight / 100;
                        }
                        if (useMultiPlatformRival) {
                            multiPlatformRivalScore = score;
                            multiPlatformRivalWeight = weight;
                            //multiPlatformRivalGps = Long.valueOf(Math.round(multiPlatformRivalScore * multiPlatformRivalWeight / 100));
                            multiPlatformRivalGps = multiPlatformRivalScore * multiPlatformRivalWeight / 100;
                        }
                    }   //loop gpsAlgorithmRivalDailySoldList == END
                }   //loop factorList == END
                
                GenericValue rivalDailySoldGV = delegator.makeValue("ProductGps", UtilMisc.toMap("productId", productId, "factor", "RIVAL_DAILY_SOLD"));
                rivalDailySoldGV.set("value", rivalDailySoldValue + "");
                rivalDailySoldGV.set("score", rivalDailySoldScore);
                rivalDailySoldGV.set("weight", rivalDailySoldWeight);
                rivalDailySoldGV.set("gps", rivalDailySoldGps);
                delegator.createOrStore(rivalDailySoldGV);
                
                GenericValue rivalDailySalesGV = delegator.makeValue("ProductGps", UtilMisc.toMap("productId", productId, "factor", "RIVAL_DAILY_SALES"));
                rivalDailySalesGV.set("value", rivalDailySalesValue + "");
                rivalDailySalesGV.set("score", rivalDailySalesScore);
                rivalDailySalesGV.set("weight", rivalDailySalesWeight);
                rivalDailySalesGV.set("gps", rivalDailySalesGps);
                delegator.createOrStore(rivalDailySalesGV);
                
                GenericValue samePriceProfitPctGV = delegator.makeValue("ProductGps", UtilMisc.toMap("productId", productId, "factor", "SAME_PRICE_PROFIT_PCT"));
                samePriceProfitPctGV.set("value", samePriceProfitPctValue + "");
                samePriceProfitPctGV.set("score", samePriceProfitPctScore);
                samePriceProfitPctGV.set("weight", samePriceProfitPctWeight);
                samePriceProfitPctGV.set("gps", samePriceProfitPctGps);
                delegator.createOrStore(samePriceProfitPctGV);
                
                GenericValue supplierRankGV = delegator.makeValue("ProductGps", UtilMisc.toMap("productId", productId, "factor", "SUPPLIER_RANK"));
                supplierRankGV.set("value", supplierRankValue + "");
                supplierRankGV.set("score", supplierRankScore);
                supplierRankGV.set("weight", supplierRankWeight);
                supplierRankGV.set("gps", supplierRankGps);
                delegator.createOrStore(supplierRankGV);
                
                GenericValue sbuParentCategoryGV = delegator.makeValue("ProductGps", UtilMisc.toMap("productId", productId, "factor", "SBU_PARENT_CATEGORY"));
                sbuParentCategoryGV.set("value", sbuParentCategoryValue + "");
                sbuParentCategoryGV.set("score", sbuParentCategoryScore);
                sbuParentCategoryGV.set("weight", sbuParentCategoryWeight);
                sbuParentCategoryGV.set("gps", sbuParentCategoryGps);
                delegator.createOrStore(sbuParentCategoryGV);
                
                GenericValue sbuChildrenCategoryGV = delegator.makeValue("ProductGps", UtilMisc.toMap("productId", productId, "factor", "SBU_CHILDREN_CATEGORY"));
                sbuChildrenCategoryGV.set("value", sbuChildrenCategoryValue + "");
                sbuChildrenCategoryGV.set("score", sbuChildrenCategoryScore);
                sbuChildrenCategoryGV.set("weight", sbuChildrenCategoryWeight);
                sbuChildrenCategoryGV.set("gps", sbuChildrenCategoryGps);
                delegator.createOrStore(sbuChildrenCategoryGV);
                
                GenericValue amazonPictureGV = delegator.makeValue("ProductGps", UtilMisc.toMap("productId", productId, "factor", "AMAZON_PICTURE"));
                amazonPictureGV.set("value", amazonPictureValue + "");
                amazonPictureGV.set("score", amazonPictureScore);
                amazonPictureGV.set("weight", amazonPictureWeight);
                amazonPictureGV.set("gps", amazonPictureGps);
                delegator.createOrStore(amazonPictureGV);
                
                GenericValue multiPlatformRivalGV = delegator.makeValue("ProductGps", UtilMisc.toMap("productId", productId, "factor", "MULTI_PLATFORM_RIVAL"));
                multiPlatformRivalGV.set("value", multiPlatformRivalValue + "");
                multiPlatformRivalGV.set("score", multiPlatformRivalScore);
                multiPlatformRivalGV.set("weight", multiPlatformRivalWeight);
                multiPlatformRivalGV.set("gps", multiPlatformRivalGps);
                delegator.createOrStore(multiPlatformRivalGV);
                
                
                //long totalGps = Long.valueOf(Math.round(rivalDailySoldGps + rivalDailySalesGps + samePriceProfitPctGps + supplierRankGps + sbuParentCategoryGps + sbuChildrenCategoryGps + amazonPictureGps + multiPlatformRivalGps));
                long totalGps = rivalDailySoldGps + rivalDailySalesGps + samePriceProfitPctGps + supplierRankGps + sbuParentCategoryGps + sbuChildrenCategoryGps + amazonPictureGps + multiPlatformRivalGps;
                long finalGps = 0;
                //Debug.logError("Total GPS: " + totalGps, module);
                
                //get Final GPS algorithm == START
                List<GenericValue> finalGpsAlgorithmList = delegator.findList("FinalGpsAlgorithm", null, null, null, null, false);
                for (GenericValue finalGpsAlgorithm : finalGpsAlgorithmList) {  //loop finalGpsAlgorithmList == START
                    String comparator1 = finalGpsAlgorithm.getString("comparator1");
                    double value1 = 0.0;
                    long value1Long = 0;
                    if (UtilValidate.isNotEmpty(finalGpsAlgorithm.getDouble("value1"))) {
                        value1 = finalGpsAlgorithm.getDouble("value1");
                        value1Long = Math.round(value1);
                    }
                    String value1Str = finalGpsAlgorithm.getString("value1Str");
                    String joinOperator = finalGpsAlgorithm.getString("joinOperator");
                    String comparator2 = finalGpsAlgorithm.getString("comparator2");
                    double value2 = 0.0;
                    long value2Long = 0;
                    if (UtilValidate.isNotEmpty(finalGpsAlgorithm.getDouble("value2"))) {
                        value2 = finalGpsAlgorithm.getDouble("value2");
                        value2Long = Math.round(value2);
                    }
                    String value2Str = finalGpsAlgorithm.getString("value2Str");
                    String operator = finalGpsAlgorithm.getString("operator");
                    double operatorFactor = finalGpsAlgorithm.getDouble("operatorFactor");
                    long operatorFactorLong = Math.round(operatorFactor);
                    //Debug.logError("comparator1: " + comparator1 + ", value1: " + value1 + ", joinOperator: " + joinOperator + ", value2: " + value2 + ", operator: " + operator + ", operatorFactor: " + operatorFactor, module);
                    
                    if (UtilValidate.isEmpty(joinOperator)) {   //joinOperator EMPTY == START
                        if (comparator1.equals("EQUALS")) { //EQUALS
                            if (totalGps == value1Long) {
                                if (operator.equals("ADD")) {
                                    finalGps = totalGps + operatorFactorLong;
                                } else if (operator.equals("SUBSTRACT")) {
                                    finalGps = totalGps - operatorFactorLong;
                                } else if (operator.equals("MULTIPLY")) {
                                    finalGps = totalGps * operatorFactorLong;
                                } else if (operator.equals("DIVIDE")) {
                                    finalGps = totalGps / operatorFactorLong;
                                }
                            }
                        }   //EQUALS
                        else if (comparator1.equals("LESS_THAN")) { //LESS_THAN
                            if (totalGps < value1Long) {
                                if (operator.equals("ADD")) {
                                    finalGps = totalGps + operatorFactorLong;
                                } else if (operator.equals("SUBSTRACT")) {
                                    finalGps = totalGps - operatorFactorLong;
                                } else if (operator.equals("MULTIPLY")) {
                                    finalGps = totalGps * operatorFactorLong;
                                } else if (operator.equals("DIVIDE")) {
                                    finalGps = totalGps / operatorFactorLong;
                                }
                            }
                        }   //LESS_THAN
                        else if (comparator1.equals("LESS_THAN_EQUAL_TO")) { //LESS_THAN_EQUAL_TO
                            if (totalGps <= value1Long) {
                                if (operator.equals("ADD")) {
                                    finalGps = totalGps + operatorFactorLong;
                                } else if (operator.equals("SUBSTRACT")) {
                                    finalGps = totalGps - operatorFactorLong;
                                } else if (operator.equals("MULTIPLY")) {
                                    finalGps = totalGps * operatorFactorLong;
                                } else if (operator.equals("DIVIDE")) {
                                    finalGps = totalGps / operatorFactorLong;
                                }
                            }
                        }   //LESS_THAN_EQUAL_TO
                        else if (comparator1.equals("GREATER_THAN")) { //GREATER_THAN
                            if (totalGps > value1Long) {
                                if (operator.equals("ADD")) {
                                    finalGps = totalGps + operatorFactorLong;
                                } else if (operator.equals("SUBSTRACT")) {
                                    finalGps = totalGps - operatorFactorLong;
                                } else if (operator.equals("MULTIPLY")) {
                                    finalGps = totalGps * operatorFactorLong;
                                } else if (operator.equals("DIVIDE")) {
                                    finalGps = totalGps / operatorFactorLong;
                                }
                            }
                        }   //GREATER_THAN
                        else if (comparator1.equals("GREATER_THAN_EQUAL_TO")) { //GREATER_THAN_EQUAL_TO
                            if (totalGps >= value1Long) {
                                if (operator.equals("ADD")) {
                                    finalGps = totalGps + operatorFactorLong;
                                } else if (operator.equals("SUBSTRACT")) {
                                    finalGps = totalGps - operatorFactorLong;
                                } else if (operator.equals("MULTIPLY")) {
                                    finalGps = totalGps * operatorFactorLong;
                                } else if (operator.equals("DIVIDE")) {
                                    finalGps = totalGps / operatorFactorLong;
                                }
                            }
                        }   //GREATER_THAN_EQUAL_TO
                        else if (comparator1.equals("NOT_EQUALS")) { //NOT_EQUALS
                            if (totalGps != value1Long) {
                                if (operator.equals("ADD")) {
                                    finalGps = totalGps + operatorFactorLong;
                                } else if (operator.equals("SUBSTRACT")) {
                                    finalGps = totalGps - operatorFactorLong;
                                } else if (operator.equals("MULTIPLY")) {
                                    finalGps = totalGps * operatorFactorLong;
                                } else if (operator.equals("DIVIDE")) {
                                    finalGps = totalGps / operatorFactorLong;
                                }
                            }
                        }   //NOT_EQUALS
                    }   //joinOperator EMPTY == END
                    else {  //joinOperator not EMPTY == START
                        if (joinOperator.equals("AND")) {  //joinOperator AND == START
                            if (comparator1.equals("GREATER_THAN") && comparator2.equals("LESS_THAN")) { //GREATER_THAN - LESS_THAN == START
                                if (totalGps > value1Long && totalGps < value2Long) {
                                    if (operator.equals("ADD")) {
                                        finalGps = totalGps + operatorFactorLong;
                                    } else if (operator.equals("SUBSTRACT")) {
                                        finalGps = totalGps - operatorFactorLong;
                                    } else if (operator.equals("MULTIPLY")) {
                                        finalGps = totalGps * operatorFactorLong;
                                    } else if (operator.equals("DIVIDE")) {
                                        finalGps = totalGps / operatorFactorLong;
                                    }
                                }
                            }   //GREATER_THAN - LESS_THAN == END
                            else if (comparator1.equals("GREATER_THAN") && comparator2.equals("LESS_THAN_EQUAL_TO")) { //GREATER_THAN - LESS_THAN_EQUAL_TO == START
                                if (totalGps > value1Long && totalGps <= value2Long) {
                                    if (operator.equals("ADD")) {
                                        finalGps = totalGps + operatorFactorLong;
                                    } else if (operator.equals("SUBSTRACT")) {
                                        finalGps = totalGps - operatorFactorLong;
                                    } else if (operator.equals("MULTIPLY")) {
                                        finalGps = totalGps * operatorFactorLong;
                                    } else if (operator.equals("DIVIDE")) {
                                        finalGps = totalGps / operatorFactorLong;
                                    }
                                }
                            }   //GREATER_THAN - LESS_THAN_EQUAL_TO == END
                            else if (comparator1.equals("GREATER_THAN_EQUAL_TO") && comparator2.equals("LESS_THAN")) { //GREATER_THAN_EQUAL_TO - LESS_THAN == START
                                if (totalGps >= value1Long && totalGps < value2Long) {
                                    if (operator.equals("ADD")) {
                                        finalGps = totalGps + operatorFactorLong;
                                    } else if (operator.equals("SUBSTRACT")) {
                                        finalGps = totalGps - operatorFactorLong;
                                    } else if (operator.equals("MULTIPLY")) {
                                        finalGps = totalGps * operatorFactorLong;
                                    } else if (operator.equals("DIVIDE")) {
                                        finalGps = totalGps / operatorFactorLong;
                                    }
                                }
                            }   //GREATER_THAN_EQUAL_TO - LESS_THAN == END
                            else if (comparator1.equals("GREATER_THAN_EQUAL_TO") && comparator2.equals("LESS_THAN_EQUAL_TO")) { //GREATER_THAN_EQUAL_TO - LESS_THAN_EQUAL_TO == START
                                if (totalGps >= value1Long && totalGps <= value2Long) {
                                    if (operator.equals("ADD")) {
                                        finalGps = totalGps + operatorFactorLong;
                                    } else if (operator.equals("SUBSTRACT")) {
                                        finalGps = totalGps - operatorFactorLong;
                                    } else if (operator.equals("MULTIPLY")) {
                                        finalGps = totalGps * operatorFactorLong;
                                    } else if (operator.equals("DIVIDE")) {
                                        finalGps = totalGps / operatorFactorLong;
                                    }
                                }
                            }   //GREATER_THAN_EQUAL_TO - LESS_THAN_EQUAL_TO == END
                        }   //joinOperator AND == END
                        else if (joinOperator.equals("OR")) { //joinOperator OR == START
                            //TODO
                        }   //joinOperator OR == END
                    }   //joinOperator not EMPTY == END
                }   //loop finalGpsAlgorithmList == END
                //get Final GPS algorithm == END
                
                //FINAL - update productMaster == START
                finalGps = Long.valueOf(Math.round(finalGps));
                //Debug.logError("finalGps: " + finalGps, module);
                GenericValue pm = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
                pm.set("finalGps", finalGps);
                delegator.store(pm);
                //FINAL - update productMaster == END
            }   //loop productList == END
        } catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }   //main try -- END
        
        //Debug.logError("Finished Running updateProductGps", module);
        return result;
    }   //updateProductGps
    
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }  
        return true;  
    }
    
    public static Map<String, Object> updateSupplierRank (DispatchContext dctx, Map<String, ? extends Object> context) {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        Calendar fromDay = Calendar.getInstance();
        fromDay.add(Calendar.DATE, -3);
        java.sql.Date fromDate = new java.sql.Date(fromDay.getTimeInMillis());
        
        try {
            
            delegator.removeAll("SupplierRank");
            final Map<String, Double> supplierMap = new HashMap<String, Double>();
            java.sql.Date latestDate = null;
            List<GenericValue> latestDateGVList = delegator.findList("GudaoInventory", EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN, fromDate), UtilMisc.toSet("date", "averageWeeklySold"), UtilMisc.toList("date DESC"), null, true);
            for (GenericValue latestDateGV: latestDateGVList) { //loop latestDateGVList == START
                //Debug.logError(latestDateGV.getDouble("averageWeeklySold") + "", module);
                if (UtilValidate.isNotEmpty(latestDateGV.getDouble("averageWeeklySold"))) {
                    latestDate = latestDateGV.getDate("date");
                    break;
                }
            }   //loop latestDateGVList == END
            
            List<GenericValue> supplierList = delegator.findByAnd("ProductMasterSupplier", UtilMisc.toMap("supplierType", "MAIN"), null, true);
            int i = 0;
            for (GenericValue supplierGV : supplierList) {    //loop supplierList == START
                String productId = supplierGV.getString("productId");
                String supplier = supplierGV.getString("supplier");
                double qtySold = 0.0;
                
                GenericValue pm = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), true);
                BigDecimal cPriceBD = pm.getBigDecimal("priceUsd");
                double cPrice = cPriceBD.doubleValue();
                
                GenericValue gudaoInventory = delegator.findOne("GudaoInventory", UtilMisc.toMap("productId", productId, "date", latestDate), true);
                if (UtilValidate.isNotEmpty(gudaoInventory)) {  //if gudaoInventory is NOT EMPTY == START
                    qtySold = gudaoInventory.getDouble("averageWeeklySold");
                }   //if gudaoInventory is NOT EMPTY == START
                double totalSales = cPrice * qtySold;
                
                if (UtilValidate.isEmpty(supplierMap.get(supplier))) {  //first time putting supplier into supplierMap == START
                    supplierMap.put(supplier, totalSales);
                }   //first time putting supplier into supplierMap == END
                else {  //adding up the supplier totalSales == START
                    totalSales = totalSales + supplierMap.get(supplier);
                    supplierMap.put(supplier, totalSales);
                    //Debug.logError("supplier: " + supplier + ", totalSales: " + totalSales, module);
                }   //adding up the supplier totalSales == START
                
            }   //loop supplierList == END
            
            List<String> list = new LinkedList<String>();
            for (String supplier : supplierMap.keySet()) {
                list.add(supplier);
            }
            
            Collections.sort(list, new Comparator<String> () {
                @Override
                public int compare(String supplier1, String supplier2) {
                    return supplierMap.get(supplier2).compareTo(supplierMap.get(supplier1));
                }
            });
            
            long rank = 1;
            for(String supplier : list) {
                GenericValue supplierRank = delegator.makeValue("SupplierRank", UtilMisc.toMap("supplier", supplier));
                if (supplierMap.get(supplier).equals(0d)) {
                    supplierRank.set("rank", Long.valueOf(99999));
                } else {
                    supplierRank.set("rank", rank);
                }
                delegator.create(supplierRank);
                rank++;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }
        
        return result;
        
    }   //updateSupplierRank
    
    public static Map<String, Object> updateProductGms (DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException  {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        
        String productIdInput = (String) context.get("productIdInput");
        String ownerGroupInput = (String) context.get("ownerGroupInput");
        String productGroupInput = (String) context.get("productGroupInput");

        try {   //main try == START
            EntityCondition cond = null;
            List<EntityCondition> conditions = FastList.newInstance();
            
            if (UtilValidate.isNotEmpty(productGroupInput)) {   //productListCondition == START
                conditions.add(EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, productGroupInput));
            }
            
            if (UtilValidate.isNotEmpty(ownerGroupInput)) {
                conditions.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroupInput));
            }
            
            if (UtilValidate.isNotEmpty(productIdInput)) {
                conditions.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productIdInput));
            }//productListCondition == END
            
            //GET distinct product ID from product GMS Detail == START
            
            List<GenericValue> productIdList = delegator.findList("ProductGmsDetail", null,
                                                                  UtilMisc.toSet("productId"),
                                                                  UtilMisc.toList("productId"),
                                                                  new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                  true);
            List<String> distinctProductIdList = new ArrayList<String>();
            for (GenericValue gmsDetailProductId : productIdList) { //loop productIdList == START
                distinctProductIdList.add(gmsDetailProductId.getString("productId"));
                //Debug.logError("productId " + gmsDetailProductId.getString("productId"), module);
            }   //loop productIdList == START
            //GET distinct product ID from product GMS Detail == END
            
            //conditions.add(EntityCondition.makeCondition("productId", EntityOperator.IN, distinctProductIdList));
            cond = EntityCondition.makeCondition(conditions, EntityOperator.AND);
            
            List<GenericValue> productList = delegator.findList("ProductMaster", cond, null, null, null, false);
            //Debug.logError("size: " + productList.size(), module);
            for (GenericValue product : productList) {  //loop productList == START
                String productId = product.getString("productId");
                String productGroup = product.getString("productGroup");
                //Debug.logError("productId " + productId, module);
                if (UtilValidate.isNotEmpty(productGroup) && !productGroup.equals("G4") && !productGroup.equals("G5")) {    //if not G4 and G5 == START
                    long ebayUsValue = 0;
                    long ebayUsScore = 0;
                    long ebayUsWeight = 0;
                    long ebayUsGms = 0;
                    
                    long ebayUkValue = 0;
                    long ebayUkScore = 0;
                    long ebayUkWeight = 0;
                    long ebayUkGms = 0;
                    
                    long ebayAuValue = 0;
                    long ebayAuScore = 0;
                    long ebayAuWeight = 0;
                    long ebayAuGms = 0;
                    
                    long ebayDeValue = 0;
                    long ebayDeScore = 0;
                    long ebayDeWeight = 0;
                    long ebayDeGms = 0;
                    
                    long ebayFrValue = 0;
                    long ebayFrScore = 0;
                    long ebayFrWeight = 0;
                    long ebayFrGms = 0;
                    
                    long smtUsValue = 0;
                    long smtUsScore = 0;
                    long smtUsWeight = 0;
                    long smtUsGms = 0;
                    
                    long wishUsValue = 0;
                    long wishUsScore = 0;
                    long wishUsWeight = 0;
                    long wishUsGms = 0;
                    
                    long amaUsValue = 0;
                    long amaUsScore = 0;
                    long amaUsWeight = 0;
                    long amaUsGms = 0;
                    
                    long amaUkValue = 0;
                    long amaUkScore = 0;
                    long amaUkWeight = 0;
                    long amaUkGms = 0;
                    
                    long amaCaValue = 0;
                    long amaCaScore = 0;
                    long amaCaWeight = 0;
                    long amaCaGms = 0;
                    
                    long amaDeValue = 0;
                    long amaDeScore = 0;
                    long amaDeWeight = 0;
                    long amaDeGms = 0;
                    
                    long amaFollowValue = 0;
                    long amaFollowScore = 0;
                    long amaFollowWeight = 0;
                    long amaFollowGms = 0;
                    
                    //get ebay US GMS == START
                    List<GenericValue> ebayUsGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "US"), null, true);
                    for (GenericValue ebayUsGmsDetail : ebayUsGmsDetailList) {  //loop ebayUsGmsDetailList == START
                        ebayUsValue = ebayUsValue + ebayUsGmsDetail.getLong("totalCount");
                    }   //loop ebayUsGmsDetailList == END
                    //get ebay US GMS == END
                    
                    //get ebay UK GMS == START
                    List<GenericValue> ebayUkGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "UK"), null, true);
                    for (GenericValue ebayUkGmsDetail : ebayUkGmsDetailList) {  //loop ebayUkGmsDetailList == START
                        ebayUkValue = ebayUkValue + ebayUkGmsDetail.getLong("totalCount");
                    }   //loop ebayUkGmsDetailList == END
                    //get ebay UK GMS == END
                    
                    //get ebay AU GMS == START
                    List<GenericValue> ebayAuGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "AU"), null, true);
                    for (GenericValue ebayAuGmsDetail : ebayAuGmsDetailList) {  //loop ebayAuGmsDetailList == START
                        ebayAuValue = ebayAuValue + ebayAuGmsDetail.getLong("totalCount");
                    }   //loop ebayAuGmsDetailList == END
                    //get ebay AU GMS == END
                    
                    //get ebay DE GMS == START
                    List<GenericValue> ebayDeGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "DE"), null, true);
                    for (GenericValue ebayDeGmsDetail : ebayDeGmsDetailList) {  //loop ebayDeGmsDetailList == START
                        ebayDeValue = ebayDeValue + ebayDeGmsDetail.getLong("totalCount");
                    }   //loop ebayDeGmsDetailList == END
                    //get ebay DE GMS == END
                    
                    //get ebay FR GMS == START
                    List<GenericValue> ebayFrGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "FR"), null, true);
                    for (GenericValue ebayFrGmsDetail : ebayFrGmsDetailList) {  //loop ebayFrGmsDetailList == START
                        ebayFrValue = ebayFrValue + ebayFrGmsDetail.getLong("totalCount");
                    }   //loop ebayFrGmsDetailList == END
                    //get ebay FR GMS == END
                    
                    //get SMT US GMS == START
                    List<GenericValue> smtUsGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "SMT", "site", "US"), null, true);
                    for (GenericValue smtUsGmsDetail : smtUsGmsDetailList) {  //loop smtUsGmsDetailList == START
                        smtUsValue = smtUsValue + smtUsGmsDetail.getLong("totalCount");
                    }   //loop smtUsGmsDetailList == END
                    //get SMT US GMS == END
                    
                    //get WISH US GMS == START
                    List<GenericValue> wishUsGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "WISH", "site", "US"), null, true);
                    for (GenericValue wishUsGmsDetail : wishUsGmsDetailList) {  //loop wishUsGmsDetailList == START
                        wishUsValue = wishUsValue + wishUsGmsDetail.getLong("totalCount");
                    }   //loop wishUsGmsDetailList == END
                    //get WISH US GMS == END
                    
                    //get AMA US GMS == START
                    List<GenericValue> amaUsGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "US"), null, true);
                    for (GenericValue amaUsGmsDetail : amaUsGmsDetailList) {  //loop amaUsGmsDetailList == START
                        amaUsValue = amaUsValue + amaUsGmsDetail.getLong("totalCount");
                    }   //loop amaUsGmsDetailList == END
                    //get AMA US GMS == END
                    
                    //get AMA UK GMS == START
                    List<GenericValue> amaUkGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "UK"), null, true);
                    for (GenericValue amaUkGmsDetail : amaUkGmsDetailList) {  //loop amaUkGmsDetailList == START
                        amaUkValue = amaUkValue + amaUkGmsDetail.getLong("totalCount");
                    }   //loop amaUkGmsDetailList == END
                    //get AMA UK GMS == END
                    
                    //get AMA CA GMS == START
                    List<GenericValue> amaCaGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "CA"), null, true);
                    for (GenericValue amaCaGmsDetail : amaCaGmsDetailList) {  //loop amaCaGmsDetailList == START
                        amaCaValue = amaCaValue + amaCaGmsDetail.getLong("totalCount");
                    }   //loop amaCaGmsDetailList == END
                    //get AMA CA GMS == END
                    
                    //get AMA DE GMS == START
                    List<GenericValue> amaDeGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "DE"), null, true);
                    for (GenericValue amaDeGmsDetail : amaDeGmsDetailList) {  //loop amaDeGmsDetailList == START
                        amaDeValue = amaDeValue + amaDeGmsDetail.getLong("totalCount");
                    }   //loop amaDeGmsDetailList == END
                    //get AMA DE GMS == END
                    
                    //get AMA FOLLOW GMS == START
                    List<GenericValue> amaFollowGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "FOLLOW_COUNT", "platform", "AMAZON", "site", "US"), null, true);
                    for (GenericValue amaFollowGmsDetail : amaFollowGmsDetailList) {  //loop amaFollowGmsDetailList == START
                        amaFollowValue = amaFollowValue + amaFollowGmsDetail.getLong("totalCount");
                    }   //loop amaFollowGmsDetailList == END
                    //get AMA FOLLOW GMS == END
                    
                    
                    List<String> factorList = new ArrayList<String>();
                    factorList.add("EBAY_US_LISTING_COUNT");
                    factorList.add("EBAY_UK_LISTING_COUNT");
                    factorList.add("EBAY_AU_LISTING_COUNT");
                    factorList.add("EBAY_DE_LISTING_COUNT");
                    factorList.add("EBAY_FR_LISTING_COUNT");
                    factorList.add("SMT_US_LISTING_COUNT");
                    factorList.add("WISH_US_LISTING_COUNT");
                    factorList.add("AMA_US_LISTING_COUNT");
                    factorList.add("AMA_UK_LISTING_COUNT");
                    factorList.add("AMA_CA_LISTING_COUNT");
                    factorList.add("AMA_DE_LISTING_COUNT");
                    factorList.add("AMA_US_FOLLOW_COUNT");
                    
                    for (String factor : factorList) {  //loop factorList == START
                        List<GenericValue> gmsAlgorithmList = delegator.findByAnd("GmsAlgorithm", UtilMisc.toMap("factor", factor), null, false);
                        for (GenericValue gmsAlgorithm : gmsAlgorithmList) {    //loop gmsAlgorithmList == START
                            boolean useEbayUsGms = false;
                            boolean useEbayUkGms = false;
                            boolean useEbayAuGms = false;
                            boolean useEbayDeGms = false;
                            boolean useEbayFrGms = false;
                            boolean useSmtUsGms = false;
                            boolean useWishUsGms = false;
                            boolean useAmaUsGms = false;
                            boolean useAmaUkGms = false;
                            boolean useAmaCaGms = false;
                            boolean useAmaDeGms = false;
                            boolean useAmaFollowGms = false;
                            
                            long score = gmsAlgorithm.getLong("score");
                            long weight = gmsAlgorithm.getLong("weight");
                            String comparator1 = gmsAlgorithm.getString("comparator1");
                            long value1 = gmsAlgorithm.getLong("value1");
                            String value1Str = gmsAlgorithm.getString("value1Str");
                            String joinOperator = gmsAlgorithm.getString("joinOperator");
                            String comparator2 = gmsAlgorithm.getString("comparator2");
                            long value2 = 0;
                            if (UtilValidate.isNotEmpty(gmsAlgorithm.getLong("value2"))) {
                                value2 = gmsAlgorithm.getLong("value2");
                            }
                            
                            String value2Str = gmsAlgorithm.getString("value2Str");
                            //Debug.logError("factor: " + factor + ", value1: " + value1 + ", value2: " + value2 + ", value1Str:  " + value1Str + ", joinOperator: " + joinOperator, module);
                            
                            if (UtilValidate.isEmpty(joinOperator)) {   //joinOperator EMPTY == START
                                if (comparator1.equals("EQUALS")) { //EQUALS
                                    if (factor.equals("EBAY_US_LISTING_COUNT")) {
                                        if (ebayUsValue == value1) {
                                            useEbayUsGms = true;
                                        }
                                    } else if (factor.equals("EBAY_UK_LISTING_COUNT")) {
                                        if (ebayUkValue == value1) {
                                            useEbayUkGms = true;
                                        }
                                    } else if (factor.equals("EBAY_AU_LISTING_COUNT")) {
                                        if (ebayAuValue == value1) {
                                            useEbayAuGms = true;
                                        }
                                    } else if (factor.equals("EBAY_DE_LISTING_COUNT")) {
                                        if (ebayDeValue == value1) {
                                            useEbayDeGms = true;
                                        }
                                    }  else if (factor.equals("EBAY_FR_LISTING_COUNT")) {
                                        if (ebayFrValue == value1) {
                                            useEbayFrGms = true;
                                        }
                                    } else if (factor.equals("SMT_US_LISTING_COUNT")) {
                                        if (smtUsValue == value1) {
                                            useSmtUsGms = true;
                                        }
                                    } else if (factor.equals("WISH_US_LISTING_COUNT")) {
                                        if (wishUsValue == value1) {
                                            useWishUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_LISTING_COUNT")) {
                                        if (amaUsValue == value1) {
                                            useAmaUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_UK_LISTING_COUNT")) {
                                        if (amaUkValue == value1) {
                                            useAmaUkGms = true;
                                        }
                                    } else if (factor.equals("AMA_CA_LISTING_COUNT")) {
                                        if (amaCaValue == value1) {
                                            useAmaCaGms = true;
                                        }
                                    } else if (factor.equals("AMA_DE_LISTING_COUNT")) {
                                        if (amaDeValue == value1) {
                                            useAmaDeGms = true;
                                        }
                                    }else if (factor.equals("AMA_US_FOLLOW_COUNT")) {
                                        if (amaFollowValue == value1) {
                                            useAmaFollowGms = true;
                                        }
                                    }
                                }   //EQUALS
                                else if (comparator1.equals("LESS_THAN")) { //LESS_THAN
                                    if (factor.equals("EBAY_US_LISTING_COUNT")) {
                                        if (ebayUsValue < value1) {
                                            useEbayUsGms = true;
                                        }
                                    } else if (factor.equals("EBAY_UK_LISTING_COUNT")) {
                                        if (ebayUkValue < value1) {
                                            useEbayUkGms = true;
                                        }
                                    } else if (factor.equals("EBAY_AU_LISTING_COUNT")) {
                                        if (ebayAuValue < value1) {
                                            useEbayAuGms = true;
                                        }
                                    } else if (factor.equals("EBAY_DE_LISTING_COUNT")) {
                                        if (ebayDeValue < value1) {
                                            useEbayDeGms = true;
                                        }
                                    }  else if (factor.equals("EBAY_FR_LISTING_COUNT")) {
                                        if (ebayFrValue < value1) {
                                            useEbayFrGms = true;
                                        }
                                    } else if (factor.equals("SMT_US_LISTING_COUNT")) {
                                        if (smtUsValue < value1) {
                                            useSmtUsGms = true;
                                        }
                                    } else if (factor.equals("WISH_US_LISTING_COUNT")) {
                                        if (wishUsValue < value1) {
                                            useWishUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_LISTING_COUNT")) {
                                        if (amaUsValue < value1) {
                                            useAmaUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_UK_LISTING_COUNT")) {
                                        if (amaUkValue < value1) {
                                            useAmaUkGms = true;
                                        }
                                    } else if (factor.equals("AMA_CA_LISTING_COUNT")) {
                                        if (amaCaValue < value1) {
                                            useAmaCaGms = true;
                                        }
                                    } else if (factor.equals("AMA_DE_LISTING_COUNT")) {
                                        if (amaDeValue < value1) {
                                            useAmaDeGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_FOLLOW_COUNT")) {
                                        if (amaFollowValue < value1) {
                                            useAmaFollowGms = true;
                                        }
                                    }
                                }   //LESS_THAN
                                else if (comparator1.equals("LESS_THAN_EQUAL_TO")) {    //LESS_THAN_EQUAL_TO
                                    if (factor.equals("EBAY_US_LISTING_COUNT")) {
                                        if (ebayUsValue <= value1) {
                                            useEbayUsGms = true;
                                        }
                                    } else if (factor.equals("EBAY_UK_LISTING_COUNT")) {
                                        if (ebayUkValue <= value1) {
                                            useEbayUkGms = true;
                                        }
                                    } else if (factor.equals("EBAY_AU_LISTING_COUNT")) {
                                        if (ebayAuValue <= value1) {
                                            useEbayAuGms = true;
                                        }
                                    } else if (factor.equals("EBAY_DE_LISTING_COUNT")) {
                                        if (ebayDeValue <= value1) {
                                            useEbayDeGms = true;
                                        }
                                    }  else if (factor.equals("EBAY_FR_LISTING_COUNT")) {
                                        if (ebayFrValue <= value1) {
                                            useEbayFrGms = true;
                                        }
                                    } else if (factor.equals("SMT_US_LISTING_COUNT")) {
                                        if (smtUsValue <= value1) {
                                            useSmtUsGms = true;
                                        }
                                    } else if (factor.equals("WISH_US_LISTING_COUNT")) {
                                        if (wishUsValue <= value1) {
                                            useWishUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_LISTING_COUNT")) {
                                        if (amaUsValue <= value1) {
                                            useAmaUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_UK_LISTING_COUNT")) {
                                        if (amaUkValue <= value1) {
                                            useAmaUkGms = true;
                                        }
                                    } else if (factor.equals("AMA_CA_LISTING_COUNT")) {
                                        if (amaCaValue <= value1) {
                                            useAmaCaGms = true;
                                        }
                                    } else if (factor.equals("AMA_DE_LISTING_COUNT")) {
                                        if (amaDeValue <= value1) {
                                            useAmaDeGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_FOLLOW_COUNT")) {
                                        if (amaFollowValue <= value1) {
                                            useAmaFollowGms = true;
                                        }
                                    }
                                }   //LESS_THAN_EQUAL_TO
                                else if (comparator1.equals("GREATER_THAN")) {  //GREATER_THAN
                                    if (factor.equals("EBAY_US_LISTING_COUNT")) {
                                        if (ebayUsValue > value1) {
                                            useEbayUsGms = true;
                                        }
                                    } else if (factor.equals("EBAY_UK_LISTING_COUNT")) {
                                        if (ebayUkValue > value1) {
                                            useEbayUkGms = true;
                                        }
                                    } else if (factor.equals("EBAY_AU_LISTING_COUNT")) {
                                        if (ebayAuValue > value1) {
                                            useEbayAuGms = true;
                                        }
                                    } else if (factor.equals("EBAY_DE_LISTING_COUNT")) {
                                        if (ebayDeValue > value1) {
                                            useEbayDeGms = true;
                                        }
                                    }  else if (factor.equals("EBAY_FR_LISTING_COUNT")) {
                                        if (ebayFrValue > value1) {
                                            useEbayFrGms = true;
                                        }
                                    } else if (factor.equals("SMT_US_LISTING_COUNT")) {
                                        if (smtUsValue > value1) {
                                            useSmtUsGms = true;
                                        }
                                    } else if (factor.equals("WISH_US_LISTING_COUNT")) {
                                        if (wishUsValue > value1) {
                                            useWishUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_LISTING_COUNT")) {
                                        if (amaUsValue > value1) {
                                            useAmaUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_UK_LISTING_COUNT")) {
                                        if (amaUkValue > value1) {
                                            useAmaUkGms = true;
                                        }
                                    } else if (factor.equals("AMA_CA_LISTING_COUNT")) {
                                        if (amaCaValue > value1) {
                                            useAmaCaGms = true;
                                        }
                                    } else if (factor.equals("AMA_DE_LISTING_COUNT")) {
                                        if (amaDeValue > value1) {
                                            useAmaDeGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_FOLLOW_COUNT")) {
                                        if (amaFollowValue > value1) {
                                            useAmaFollowGms = true;
                                        }
                                    }
                                }   //GREATER_THAN
                                else if (comparator1.equals("GREATER_THAN_EQUAL_TO")) { //GREATER_THAN_EQUAL_TO
                                    if (factor.equals("EBAY_US_LISTING_COUNT")) {
                                        if (ebayUsValue >= value1) {
                                            useEbayUsGms = true;
                                        }
                                    } else if (factor.equals("EBAY_UK_LISTING_COUNT")) {
                                        if (ebayUkValue >= value1) {
                                            useEbayUkGms = true;
                                        }
                                    } else if (factor.equals("EBAY_AU_LISTING_COUNT")) {
                                        if (ebayAuValue >= value1) {
                                            useEbayAuGms = true;
                                        }
                                    } else if (factor.equals("EBAY_DE_LISTING_COUNT")) {
                                        if (ebayDeValue >= value1) {
                                            useEbayDeGms = true;
                                        }
                                    }  else if (factor.equals("EBAY_FR_LISTING_COUNT")) {
                                        if (ebayFrValue >= value1) {
                                            useEbayFrGms = true;
                                        }
                                    } else if (factor.equals("SMT_US_LISTING_COUNT")) {
                                        if (smtUsValue >= value1) {
                                            useSmtUsGms = true;
                                        }
                                    } else if (factor.equals("WISH_US_LISTING_COUNT")) {
                                        if (wishUsValue >= value1) {
                                            useWishUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_LISTING_COUNT")) {
                                        if (amaUsValue >= value1) {
                                            useAmaUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_UK_LISTING_COUNT")) {
                                        if (amaUkValue >= value1) {
                                            useAmaUkGms = true;
                                        }
                                    } else if (factor.equals("AMA_CA_LISTING_COUNT")) {
                                        if (amaCaValue >= value1) {
                                            useAmaCaGms = true;
                                        }
                                    } else if (factor.equals("AMA_DE_LISTING_COUNT")) {
                                        if (amaDeValue >= value1) {
                                            useAmaDeGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_FOLLOW_COUNT")) {
                                        if (amaFollowValue >= value1) {
                                            useAmaFollowGms = true;
                                        }
                                    }                            }   //GREATER_THAN_EQUAL_TO
                                else if (comparator1.equals("NOT_EQUAL")) { //NOT_EQUAL
                                    if (factor.equals("EBAY_US_LISTING_COUNT")) {
                                        if (ebayUsValue != value1) {
                                            useEbayUsGms = true;
                                        }
                                    } else if (factor.equals("EBAY_UK_LISTING_COUNT")) {
                                        if (ebayUkValue != value1) {
                                            useEbayUkGms = true;
                                        }
                                    } else if (factor.equals("EBAY_AU_LISTING_COUNT")) {
                                        if (ebayAuValue != value1) {
                                            useEbayAuGms = true;
                                        }
                                    } else if (factor.equals("EBAY_DE_LISTING_COUNT")) {
                                        if (ebayDeValue != value1) {
                                            useEbayDeGms = true;
                                        }
                                    }  else if (factor.equals("EBAY_FR_LISTING_COUNT")) {
                                        if (ebayFrValue != value1) {
                                            useEbayFrGms = true;
                                        }
                                    } else if (factor.equals("SMT_US_LISTING_COUNT")) {
                                        if (smtUsValue != value1) {
                                            useSmtUsGms = true;
                                        }
                                    } else if (factor.equals("WISH_US_LISTING_COUNT")) {
                                        if (wishUsValue != value1) {
                                            useWishUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_LISTING_COUNT")) {
                                        if (amaUsValue != value1) {
                                            useAmaUsGms = true;
                                        }
                                    } else if (factor.equals("AMA_UK_LISTING_COUNT")) {
                                        if (amaUkValue != value1) {
                                            useAmaUkGms = true;
                                        }
                                    } else if (factor.equals("AMA_CA_LISTING_COUNT")) {
                                        if (amaCaValue != value1) {
                                            useAmaCaGms = true;
                                        }
                                    } else if (factor.equals("AMA_DE_LISTING_COUNT")) {
                                        if (amaDeValue != value1) {
                                            useAmaDeGms = true;
                                        }
                                    } else if (factor.equals("AMA_US_FOLLOW_COUNT")) {
                                        if (amaFollowValue != value1) {
                                            useAmaFollowGms = true;
                                        }
                                    }
                                }   //NOT_EQUAL
                            }   //joinOperator EMPTY == END
                            else {  //joinOperator NOT EMPTY == START
                                if (joinOperator.equals("AND")) {   //joinOperator AND == START
                                    if (comparator1.equals("GREATER_THAN") && comparator2.equals("LESS_THAN")) { //GREATER_THAN - LESS_THAN == START
                                        if (factor.equals("EBAY_US_LISTING_COUNT")) {
                                            if (ebayUsValue > value1 && ebayUsValue < value2) {
                                                useEbayUsGms = true;
                                            }
                                        } else if (factor.equals("EBAY_UK_LISTING_COUNT")) {
                                            if (ebayUkValue > value1 && ebayUkValue < value2) {
                                                useEbayUkGms = true;
                                            }
                                        } else if (factor.equals("EBAY_AU_LISTING_COUNT")) {
                                            if (ebayAuValue > value1 && ebayAuValue < value2) {
                                                useEbayAuGms = true;
                                            }
                                        } else if (factor.equals("EBAY_DE_LISTING_COUNT")) {
                                            if (ebayDeValue > value1 && ebayDeValue < value2) {
                                                useEbayDeGms = true;
                                            }
                                        } else if (factor.equals("EBAY_FR_LISTING_COUNT")) {
                                            if (ebayFrValue > value1 && ebayFrValue < value2) {
                                                useEbayFrGms = true;
                                            }
                                        } else if (factor.equals("SMT_US_LISTING_COUNT")) {
                                            if (smtUsValue > value1 && smtUsValue < value2) {
                                                useSmtUsGms = true;
                                            }
                                        } else if (factor.equals("WISH_US_LISTING_COUNT")) {
                                            if (wishUsValue > value1 && wishUsValue < value2) {
                                                useWishUsGms = true;
                                            }
                                        } else if (factor.equals("AMA_US_LISTING_COUNT")) {
                                            if (amaUsValue > value1 && amaUsValue < value2) {
                                                useAmaUsGms = true;
                                            }
                                        } else if (factor.equals("AMA_UK_LISTING_COUNT")) {
                                            if (amaUkValue > value1 && amaUkValue < value2) {
                                                useAmaUkGms = true;
                                            }
                                        } else if (factor.equals("AMA_CA_LISTING_COUNT")) {
                                            if (amaCaValue > value1 && amaCaValue < value2) {
                                                useAmaCaGms = true;
                                            }
                                        } else if (factor.equals("AMA_DE_LISTING_COUNT")) {
                                            if (amaDeValue > value1 && amaDeValue < value2) {
                                                useAmaDeGms = true;
                                            }
                                        } else if (factor.equals("AMA_US_FOLLOW_COUNT")) {
                                            if (amaFollowValue > value1 && amaFollowValue < value2) {
                                                useAmaFollowGms = true;
                                            }
                                        }
                                    }   //GREATER_THAN - LESS_THAN == END
                                    else if (comparator1.equals("GREATER_THAN") && comparator2.equals("LESS_THAN_EQUAL_TO")) { //GREATER_THAN - LESS_THAN_EQUAL_TO == START
                                        if (factor.equals("EBAY_US_LISTING_COUNT")) {
                                            if (ebayUsValue > value1 && ebayUsValue <= value2) {
                                                useEbayUsGms = true;
                                            }
                                        } else if (factor.equals("EBAY_UK_LISTING_COUNT")) {
                                            if (ebayUkValue > value1 && ebayUkValue <= value2) {
                                                useEbayUkGms = true;
                                            }
                                        } else if (factor.equals("EBAY_AU_LISTING_COUNT")) {
                                            if (ebayAuValue > value1 && ebayAuValue <= value2) {
                                                useEbayAuGms = true;
                                            }
                                        } else if (factor.equals("EBAY_DE_LISTING_COUNT")) {
                                            if (ebayDeValue > value1 && ebayDeValue <= value2) {
                                                useEbayDeGms = true;
                                            }
                                        } else if (factor.equals("EBAY_FR_LISTING_COUNT")) {
                                            if (ebayFrValue > value1 && ebayFrValue <= value2) {
                                                useEbayFrGms = true;
                                            }
                                        } else if (factor.equals("SMT_US_LISTING_COUNT")) {
                                            if (smtUsValue > value1 && smtUsValue <= value2) {
                                                useSmtUsGms = true;
                                            }
                                        } else if (factor.equals("WISH_US_LISTING_COUNT")) {
                                            if (wishUsValue > value1 && wishUsValue <= value2) {
                                                useWishUsGms = true;
                                            }
                                        } else if (factor.equals("AMA_US_LISTING_COUNT")) {
                                            if (amaUsValue > value1 && amaUsValue <= value2) {
                                                useAmaUsGms = true;
                                            }
                                        } else if (factor.equals("AMA_UK_LISTING_COUNT")) {
                                            if (amaUkValue > value1 && amaUkValue <= value2) {
                                                useAmaUkGms = true;
                                            }
                                        } else if (factor.equals("AMA_CA_LISTING_COUNT")) {
                                            if (amaCaValue > value1 && amaCaValue <= value2) {
                                                useAmaCaGms = true;
                                            }
                                        } else if (factor.equals("AMA_DE_LISTING_COUNT")) {
                                            if (amaDeValue > value1 && amaDeValue <= value2) {
                                                useAmaDeGms = true;
                                            }
                                        } else if (factor.equals("AMA_US_FOLLOW_COUNT")) {
                                            if (amaFollowValue > value1 && amaFollowValue <= value2) {
                                                useAmaFollowGms = true;
                                            }
                                        }
                                    }   //GREATER_THAN - LESS_THAN_EQUAL_TO == END
                                    else if (comparator1.equals("GREATER_THAN_EQUAL_TO") && comparator2.equals("LESS_THAN")) { //GREATER_THAN_EQUAL_TO - LESS_THAN == START
                                        if (factor.equals("EBAY_US_LISTING_COUNT")) {
                                            if (ebayUsValue >= value1 && ebayUsValue < value2) {
                                                useEbayUsGms = true;
                                            }
                                        } else if (factor.equals("EBAY_UK_LISTING_COUNT")) {
                                            if (ebayUkValue >= value1 && ebayUkValue < value2) {
                                                useEbayUkGms = true;
                                            }
                                        } else if (factor.equals("EBAY_AU_LISTING_COUNT")) {
                                            if (ebayAuValue >= value1 && ebayAuValue < value2) {
                                                useEbayAuGms = true;
                                            }
                                        } else if (factor.equals("EBAY_DE_LISTING_COUNT")) {
                                            if (ebayDeValue >= value1 && ebayDeValue < value2) {
                                                useEbayDeGms = true;
                                            }
                                        } else if (factor.equals("EBAY_FR_LISTING_COUNT")) {
                                            if (ebayFrValue >= value1 && ebayFrValue < value2) {
                                                useEbayFrGms = true;
                                            }
                                        } else if (factor.equals("SMT_US_LISTING_COUNT")) {
                                            if (smtUsValue >= value1 && smtUsValue < value2) {
                                                useSmtUsGms = true;
                                            }
                                        } else if (factor.equals("WISH_US_LISTING_COUNT")) {
                                            if (wishUsValue >= value1 && wishUsValue < value2) {
                                                useWishUsGms = true;
                                            }
                                        } else if (factor.equals("AMA_US_LISTING_COUNT")) {
                                            if (amaUsValue >= value1 && amaUsValue < value2) {
                                                useAmaUsGms = true;
                                            }
                                        } else if (factor.equals("AMA_UK_LISTING_COUNT")) {
                                            if (amaUkValue >= value1 && amaUkValue < value2) {
                                                useAmaUkGms = true;
                                            }
                                        } else if (factor.equals("AMA_CA_LISTING_COUNT")) {
                                            if (amaCaValue >= value1 && amaCaValue < value2) {
                                                useAmaCaGms = true;
                                            }
                                        } else if (factor.equals("AMA_DE_LISTING_COUNT")) {
                                            if (amaDeValue >= value1 && amaDeValue < value2) {
                                                useAmaDeGms = true;
                                            }
                                        } else if (factor.equals("AMA_US_FOLLOW_COUNT")) {
                                            if (amaFollowValue >= value1 && amaFollowValue < value2) {
                                                useAmaFollowGms = true;
                                            }
                                        }
                                    }   //GREATER_THAN_EQUAL_TO - LESS_THAN == END
                                    else if (comparator1.equals("GREATER_THAN_EQUAL_TO") && comparator2.equals("LESS_THAN_EQUAL_TO")) { //GREATER_THAN_EQUAL_TO - LESS_THAN_EQUAL_TO == START
                                        if (factor.equals("EBAY_US_LISTING_COUNT")) {
                                            if (ebayUsValue >= value1 && ebayUsValue <= value2) {
                                                useEbayUsGms = true;
                                            }
                                        } else if (factor.equals("EBAY_UK_LISTING_COUNT")) {
                                            if (ebayUkValue >= value1 && ebayUkValue <= value2) {
                                                useEbayUkGms = true;
                                            }
                                        } else if (factor.equals("EBAY_AU_LISTING_COUNT")) {
                                            if (ebayAuValue >= value1 && ebayAuValue <= value2) {
                                                useEbayAuGms = true;
                                            }
                                        } else if (factor.equals("EBAY_DE_LISTING_COUNT")) {
                                            if (ebayDeValue >= value1 && ebayDeValue <= value2) {
                                                useEbayDeGms = true;
                                            }
                                        } else if (factor.equals("EBAY_FR_LISTING_COUNT")) {
                                            if (ebayFrValue >= value1 && ebayFrValue <= value2) {
                                                useEbayFrGms = true;
                                            }
                                        } else if (factor.equals("SMT_US_LISTING_COUNT")) {
                                            if (smtUsValue >= value1 && smtUsValue <= value2) {
                                                useSmtUsGms = true;
                                            }
                                        } else if (factor.equals("WISH_US_LISTING_COUNT")) {
                                            if (wishUsValue >= value1 && wishUsValue <= value2) {
                                                useWishUsGms = true;
                                            }
                                        } else if (factor.equals("AMA_US_LISTING_COUNT")) {
                                            if (amaUsValue >= value1 && amaUsValue <= value2) {
                                                useAmaUsGms = true;
                                            }
                                        } else if (factor.equals("AMA_UK_LISTING_COUNT")) {
                                            if (amaUkValue >= value1 && amaUkValue <= value2) {
                                                useAmaUkGms = true;
                                            }
                                        } else if (factor.equals("AMA_CA_LISTING_COUNT")) {
                                            if (amaCaValue >= value1 && amaCaValue <= value2) {
                                                useAmaCaGms = true;
                                            }
                                        } else if (factor.equals("AMA_DE_LISTING_COUNT")) {
                                            if (amaDeValue >= value1 && amaDeValue <= value2) {
                                                useAmaDeGms = true;
                                            }
                                        } else if (factor.equals("AMA_US_FOLLOW_COUNT")) {
                                            if (amaFollowValue >= value1 && amaFollowValue <= value2) {
                                                useAmaFollowGms = true;
                                            }
                                        }
                                    }   //GREATER_THAN_EQUAL_TO - LESS_THAN_EQUAL_TO == END
                                }   //joinOperator AND == END
                                else if (joinOperator.equals("OR")) {   //joinOperator OR == START
                                    //TODO
                                }   //joinOperator OR == START
                            }   //joinOperator NOT EMPTY == END
                            
                            if (useEbayUsGms) {
                                ebayUsScore = score;
                                ebayUsWeight = weight;
                                ebayUsGms = ebayUsScore * ebayUsWeight / 100;
                            }
                            if (useEbayUkGms) {
                                ebayUkScore = score;
                                ebayUkWeight = weight;
                                ebayUkGms = ebayUkScore * ebayUkWeight / 100;
                            }
                            if (useEbayAuGms) {
                                ebayAuScore = score;
                                ebayAuWeight = weight;
                                ebayAuGms = ebayAuScore * ebayAuWeight / 100;
                            }
                            if (useEbayDeGms) {
                                ebayDeScore = score;
                                ebayDeWeight = weight;
                                ebayDeGms = ebayDeScore * ebayDeWeight / 100;
                            }
                            if (useEbayFrGms) {
                                ebayFrScore = score;
                                ebayFrWeight = weight;
                                ebayFrGms = ebayFrScore * ebayFrWeight / 100;
                            }
                            if (useSmtUsGms) {
                                smtUsScore = score;
                                smtUsWeight = weight;
                                smtUsGms = smtUsScore * smtUsWeight / 100;
                            }
                            if (useWishUsGms) {
                                wishUsScore = score;
                                wishUsWeight = weight;
                                wishUsGms = wishUsScore * wishUsWeight / 100;
                            }
                            if (useAmaUsGms) {
                                amaUsScore = score;
                                amaUsWeight = weight;
                                amaUsGms = amaUsScore * amaUsWeight / 100;
                            }
                            if (useAmaUkGms) {
                                amaUkScore = score;
                                amaUkWeight = weight;
                                amaUkGms = amaUkScore * amaUkWeight / 100;
                            }
                            if (useAmaCaGms) {
                                amaCaScore = score;
                                amaCaWeight = weight;
                                amaCaGms = amaCaScore * amaCaWeight / 100;
                            }
                            if (useAmaDeGms) {
                                amaDeScore = score;
                                amaDeWeight = weight;
                                amaDeGms = amaDeScore * amaDeWeight / 100;
                            }
                            if (useAmaFollowGms) {
                                amaFollowScore = score;
                                amaFollowWeight = weight;
                                amaFollowGms = amaFollowScore * amaFollowWeight / 100;
                            }
                        }   //loop gmsAlgorithmList == END
                    }   //loop factorList == END
                    
                    GenericValue ebayUsGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "EBAY_US_LISTING_COUNT"));
                    ebayUsGmsGV.set("value", ebayUsValue + "");
                    ebayUsGmsGV.set("score", ebayUsScore);
                    ebayUsGmsGV.set("weight", ebayUsWeight);
                    ebayUsGmsGV.set("gms", ebayUsGms);
                    delegator.createOrStore(ebayUsGmsGV);
                    
                    GenericValue ebayUkGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "EBAY_UK_LISTING_COUNT"));
                    ebayUkGmsGV.set("value", ebayUkValue + "");
                    ebayUkGmsGV.set("score", ebayUkScore);
                    ebayUkGmsGV.set("weight", ebayUkWeight);
                    ebayUkGmsGV.set("gms", ebayUkGms);
                    delegator.createOrStore(ebayUkGmsGV);
                    
                    GenericValue ebayAuGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "EBAY_AU_LISTING_COUNT"));
                    ebayAuGmsGV.set("value", ebayAuValue + "");
                    ebayAuGmsGV.set("score", ebayAuScore);
                    ebayAuGmsGV.set("weight", ebayAuWeight);
                    ebayAuGmsGV.set("gms", ebayAuGms);
                    delegator.createOrStore(ebayAuGmsGV);
                    
                    GenericValue ebayDeGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "EBAY_DE_LISTING_COUNT"));
                    ebayDeGmsGV.set("value", ebayDeValue + "");
                    ebayDeGmsGV.set("score", ebayDeScore);
                    ebayDeGmsGV.set("weight", ebayDeWeight);
                    ebayDeGmsGV.set("gms", ebayDeGms);
                    delegator.createOrStore(ebayDeGmsGV);
                    
                    GenericValue ebayFrGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "EBAY_FR_LISTING_COUNT"));
                    ebayFrGmsGV.set("value", ebayFrValue + "");
                    ebayFrGmsGV.set("score", ebayFrScore);
                    ebayFrGmsGV.set("weight", ebayFrWeight);
                    ebayFrGmsGV.set("gms", ebayFrGms);
                    delegator.createOrStore(ebayFrGmsGV);
                    
                    GenericValue smtUsGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "SMT_US_LISTING_COUNT"));
                    smtUsGmsGV.set("value", smtUsValue + "");
                    smtUsGmsGV.set("score", smtUsScore);
                    smtUsGmsGV.set("weight", smtUsWeight);
                    smtUsGmsGV.set("gms", smtUsGms);
                    delegator.createOrStore(smtUsGmsGV);
                    
                    GenericValue wishUsGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "WISH_US_LISTING_COUNT"));
                    wishUsGmsGV.set("value", wishUsValue + "");
                    wishUsGmsGV.set("score", wishUsScore);
                    wishUsGmsGV.set("weight", wishUsWeight);
                    wishUsGmsGV.set("gms", wishUsGms);
                    delegator.createOrStore(wishUsGmsGV);
                    
                    GenericValue amaUsGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "AMA_US_LISTING_COUNT"));
                    amaUsGmsGV.set("value", amaUsValue + "");
                    amaUsGmsGV.set("score", amaUsScore);
                    amaUsGmsGV.set("weight", amaUsWeight);
                    amaUsGmsGV.set("gms", amaUsGms);
                    delegator.createOrStore(amaUsGmsGV);
                    
                    GenericValue amaUkGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "AMA_UK_LISTING_COUNT"));
                    amaUkGmsGV.set("value", amaUkValue + "");
                    amaUkGmsGV.set("score", amaUkScore);
                    amaUkGmsGV.set("weight", amaUkWeight);
                    amaUkGmsGV.set("gms", amaUkGms);
                    delegator.createOrStore(amaUkGmsGV);
                    
                    GenericValue amaCaGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "AMA_CA_LISTING_COUNT"));
                    amaCaGmsGV.set("value", amaCaValue + "");
                    amaCaGmsGV.set("score", amaCaScore);
                    amaCaGmsGV.set("weight", amaCaWeight);
                    amaCaGmsGV.set("gms", amaCaGms);
                    delegator.createOrStore(amaCaGmsGV);
                    
                    GenericValue amaDeGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "AMA_DE_LISTING_COUNT"));
                    amaDeGmsGV.set("value", amaDeValue + "");
                    amaDeGmsGV.set("score", amaDeScore);
                    amaDeGmsGV.set("weight", amaDeWeight);
                    amaDeGmsGV.set("gms", amaDeGms);
                    delegator.createOrStore(amaDeGmsGV);
                    
                    GenericValue amaFollowGmsGV = delegator.makeValue("ProductGms", UtilMisc.toMap("productId", productId, "factor", "AMA_US_FOLLOW_COUNT"));
                    amaFollowGmsGV.set("value", amaFollowValue + "");
                    amaFollowGmsGV.set("score", amaFollowScore);
                    amaFollowGmsGV.set("weight", amaFollowWeight);
                    amaFollowGmsGV.set("gms", amaFollowGms);
                    delegator.createOrStore(amaFollowGmsGV);
                    
                    long totalGms = ebayUsGms +
                    ebayUkGms +
                    ebayAuGms +
                    ebayDeGms +
                    ebayFrGms +
                    smtUsGms +
                    wishUsGms +
                    amaUsGms +
                    amaUkGms +
                    amaCaGms +
                    amaDeGms +
                    amaFollowGms;
                    
                    //FINAL - update productMaster == START
                    //finalGps = Long.valueOf(Math.round(finalGps));
                    //Debug.logError("finalGps: " + finalGps, module);
                    GenericValue pm = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
                    pm.set("finalGms", totalGms);
                    delegator.store(pm);
                    //FINAL - update productMaster == END
                }   //if not G4 and G5 == END
            }   //loop productList == END
        }   //main try == END
        catch (GenericEntityException e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }
    
        return result;
    }   //updateProductGms
    
    public static Map<String, Object> updateProductGmsEbayDetail (DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        
        String productIdInput = (String) context.get("productIdInput");
        String ownerGroupInput = (String) context.get("ownerGroupInput");
        String productGroupInput = (String) context.get("productGroupInput");
        Debug.logError("Start Running updateProductGmsEbayDetail", module);

        try {   //main try == START
            //get Gudao productStoreId == START
            List<String> gudaoProductStoreList = new ArrayList<String>();
            
            List<GenericValue> gudaoProductStoreListGV = delegator.findByAnd("ProductStore", UtilMisc.toMap("storeName", "GUDAO"), null, true);
            for (GenericValue gudaoProductStore : gudaoProductStoreListGV) {  //loop gudaoProductStoreList == START
                gudaoProductStoreList.add(gudaoProductStore.getString("productStoreId"));
            }   //loop gudaoProductStoreList == END
            //get Gudao productStoreId == END
            
            EntityCondition productListCondition = null;
            if (UtilValidate.isNotEmpty(productGroupInput)) {   //productListCondition == START
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, productGroupInput)
                                                                                     ));
            } else if (UtilValidate.isNotEmpty(ownerGroupInput)) {
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.NOT_IN, UtilMisc.toList("G4", "G5")),
                                                                                     EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroupInput)
                                                                                     ));
            } else if (UtilValidate.isNotEmpty(productIdInput)) {
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.NOT_IN, UtilMisc.toList("G4", "G5")),
                                                                                     EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productIdInput)
                                                                                     ));
            } else {
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.NOT_IN, UtilMisc.toList("G4", "G5"))
                                                                                     ));
            }//productListCondition == END
            List<GenericValue> productList = delegator.findList("ProductMaster", productListCondition, null, null, null, true);
            
            for (GenericValue product : productList) {  //loop productList == START
                String productId = product.getString("productId");
                //Debug.logError("GMS Detail: " + productId, module);
                //update ebay US GMS == START
                int usCount = 0;
                //NO Variation listing == START
                EntityCondition ebayUsCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                EntityCondition.makeCondition("site", EntityOperator.IN, UtilMisc.toList("US", "eBayMotors")),
                                                                                                EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                EntityCondition.makeCondition("hasVariation", EntityOperator.EQUALS, "N"),
                                                                                                EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                ));
                List<GenericValue> ebayUsListing = delegator.findList("EbayActiveListing", ebayUsCondition,
                                                                      UtilMisc.toSet("itemId"),
                                                                      UtilMisc.toList("itemId"),
                                                                      new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                      true);
                //NO Variation listing == END
                //Has Variation listing == START
                List<String> usActiveItemIdList = new ArrayList<String>();
                EntityCondition ebayUsConditionMain = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                EntityCondition.makeCondition("site", EntityOperator.IN, UtilMisc.toList("US", "eBayMotors")),
                                                                                                EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                EntityCondition.makeCondition("hasVariation", EntityOperator.EQUALS, "Y")
                                                                                                ));
                List<GenericValue> ebayUsListingMain = delegator.findList("EbayActiveListing", ebayUsConditionMain, null, null, null, true);
                for (GenericValue ebayUs : ebayUsListingMain) { //loop ebayUsListingMain == START
                    usActiveItemIdList.add(ebayUs.getString("itemId"));
                }   //loop ebayUsListingMain == END
                
                EntityCondition ebayUsConditionVar = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                EntityCondition.makeCondition("itemId", EntityOperator.IN, usActiveItemIdList),
                                                                                                EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                ));
                List<GenericValue> ebayUsListingVar = delegator.findList("EbayActiveListingVariation",
                                                                      ebayUsConditionVar,
                                                                      UtilMisc.toSet("itemId"),
                                                                      UtilMisc.toList("itemId"),
                                                                      new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                      true);
                //Has Variation listing == END
                //Debug.logError("ebayUsListing: " + ebayUsListing.size() + ", ebayUsListingVar: " +  ebayUsListingVar.size(), module);
                usCount = ebayUsListing.size() + ebayUsListingVar.size();
                //Debug.logError("USCOUNT: " + usCount, module);
                List<GenericValue> ebayUsGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "US"), null, false);
                GenericValue ebayUsGmsDetail = null;
                if (UtilValidate.isEmpty(ebayUsGmsDetailList)) {
                    String newSeqId = delegator.getNextSeqId("ProductGmsDetail");
                    ebayUsGmsDetail = delegator.makeValue("ProductGmsDetail", UtilMisc.toMap("id", newSeqId, "productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "US"));
                } else {
                    ebayUsGmsDetail = EntityUtil.getFirst(ebayUsGmsDetailList);
                }
                ebayUsGmsDetail.set("totalCount", Long.valueOf(usCount));
                delegator.createOrStore(ebayUsGmsDetail);
                //update ebay US GMS == END
                
                //update ebay UK GMS == START
                int ukCount = 0;
                //NO Variation listing == START
                EntityCondition ebayUkCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                EntityCondition.makeCondition("site", EntityOperator.EQUALS, "UK"),
                                                                                                EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                EntityCondition.makeCondition("hasVariation", EntityOperator.EQUALS, "N"),
                                                                                                EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                ));
                List<GenericValue> ebayUkListing = delegator.findList("EbayActiveListing", ebayUkCondition,
                                                                      UtilMisc.toSet("itemId"),
                                                                      UtilMisc.toList("itemId"),
                                                                      new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                      true);
                //NO Variation listing == END
                //Has Variation listing == START
                List<String> ukActiveItemIdList = new ArrayList<String>();
                EntityCondition ebayUkConditionMain = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                    EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                    EntityCondition.makeCondition("site", EntityOperator.EQUALS, "UK"),
                                                                                                    EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                    EntityCondition.makeCondition("hasVariation", EntityOperator.EQUALS, "Y")
                                                                                                    ));
                List<GenericValue> ebayUkListingMain = delegator.findList("EbayActiveListing", ebayUkConditionMain, null, null, null, true);
                for (GenericValue ebayUk : ebayUkListingMain) { //loop ebayUkListingMain == START
                    ukActiveItemIdList.add(ebayUk.getString("itemId"));
                }   //loop ebayUkListingMain == END
                
                EntityCondition ebayUkConditionVar = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                   EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                   EntityCondition.makeCondition("itemId", EntityOperator.IN, ukActiveItemIdList),
                                                                                                   EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                   ));
                List<GenericValue> ebayUkListingVar = delegator.findList("EbayActiveListingVariation",
                                                                         ebayUkConditionVar,
                                                                         UtilMisc.toSet("itemId"),
                                                                         UtilMisc.toList("itemId"),
                                                                         new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                         true);
                //Has Variation listing == END
                //Debug.logError("ebayUkListing: " + ebayUkListing.size() + ", ebayUkListingVar: " +  ebayUkListingVar.size(), module);
                ukCount = ebayUkListing.size() + ebayUkListingVar.size();
                //Debug.logError("UKCOUNT: " + ukCount, module);
                List<GenericValue> ebayUkGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "UK"), null, false);
                GenericValue ebayUkGmsDetail = null;
                if (UtilValidate.isEmpty(ebayUkGmsDetailList)) {
                    String newSeqId = delegator.getNextSeqId("ProductGmsDetail");
                    ebayUkGmsDetail = delegator.makeValue("ProductGmsDetail", UtilMisc.toMap("id", newSeqId, "productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "UK"));
                } else {
                    ebayUkGmsDetail = EntityUtil.getFirst(ebayUkGmsDetailList);
                }
                ebayUkGmsDetail.set("totalCount", Long.valueOf(ukCount));
                delegator.createOrStore(ebayUkGmsDetail);
                //update ebay UK GMS == END
                
                //update ebay AU GMS == START
                int auCount = 0;
                //NO Variation listing == START
                EntityCondition ebayAuCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                EntityCondition.makeCondition("site", EntityOperator.EQUALS, "Australia"),
                                                                                                EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                EntityCondition.makeCondition("hasVariation", EntityOperator.EQUALS, "N"),
                                                                                                EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                ));
                List<GenericValue> ebayAuListing = delegator.findList("EbayActiveListing", ebayAuCondition,
                                                                      UtilMisc.toSet("itemId"),
                                                                      UtilMisc.toList("itemId"),
                                                                      new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                      true);
                //NO Variation listing == END
                //Has Variation listing == START
                List<String> auActiveItemIdList = new ArrayList<String>();
                EntityCondition ebayAuConditionMain = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                    EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                    EntityCondition.makeCondition("site", EntityOperator.EQUALS, "Australia"),
                                                                                                    EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                    EntityCondition.makeCondition("hasVariation", EntityOperator.EQUALS, "Y")
                                                                                                    ));
                List<GenericValue> ebayAuListingMain = delegator.findList("EbayActiveListing", ebayAuConditionMain, null, null, null, true);
                for (GenericValue ebayAu : ebayAuListingMain) { //loop ebayAuListingMain == START
                    auActiveItemIdList.add(ebayAu.getString("itemId"));
                }   //loop ebayAuListingMain == END
                
                EntityCondition ebayAuConditionVar = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                   EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                   EntityCondition.makeCondition("itemId", EntityOperator.IN, auActiveItemIdList),
                                                                                                   EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                   ));
                List<GenericValue> ebayAuListingVar = delegator.findList("EbayActiveListingVariation",
                                                                         ebayAuConditionVar,
                                                                         UtilMisc.toSet("itemId"),
                                                                         UtilMisc.toList("itemId"),
                                                                         new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                         true);
                //Has Variation listing == END
                //Debug.logError("ebayUkListing: " + ebayUkListing.size() + ", ebayUkListingVar: " +  ebayUkListingVar.size(), module);
                auCount = ebayAuListing.size() + ebayAuListingVar.size();
                //Debug.logError("UKCOUNT: " + ukCount, module);
                List<GenericValue> ebayAuGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "AU"), null, false);
                GenericValue ebayAuGmsDetail = null;
                if (UtilValidate.isEmpty(ebayAuGmsDetailList)) {
                    String newSeqId = delegator.getNextSeqId("ProductGmsDetail");
                    ebayAuGmsDetail = delegator.makeValue("ProductGmsDetail", UtilMisc.toMap("id", newSeqId, "productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "AU"));
                } else {
                    ebayAuGmsDetail = EntityUtil.getFirst(ebayAuGmsDetailList);
                }
                ebayAuGmsDetail.set("totalCount", Long.valueOf(auCount));
                delegator.createOrStore(ebayAuGmsDetail);
                //update ebay AU GMS == END
                
                //update ebay DE GMS == START
                int deCount = 0;
                //NO Variation listing == START
                EntityCondition ebayDeCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                EntityCondition.makeCondition("site", EntityOperator.EQUALS, "Germany"),
                                                                                                EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                EntityCondition.makeCondition("hasVariation", EntityOperator.EQUALS, "N"),
                                                                                                EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                ));
                List<GenericValue> ebayDeListing = delegator.findList("EbayActiveListing", ebayDeCondition,
                                                                      UtilMisc.toSet("itemId"),
                                                                      UtilMisc.toList("itemId"),
                                                                      new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                      true);
                //NO Variation listing == END
                //Has Variation listing == START
                List<String> deActiveItemIdList = new ArrayList<String>();
                EntityCondition ebayDeConditionMain = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                    EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                    EntityCondition.makeCondition("site", EntityOperator.EQUALS, "Germany"),
                                                                                                    EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                    EntityCondition.makeCondition("hasVariation", EntityOperator.EQUALS, "Y")
                                                                                                    ));
                List<GenericValue> ebayDeListingMain = delegator.findList("EbayActiveListing", ebayDeConditionMain, null, null, null, true);
                for (GenericValue ebayDe : ebayDeListingMain) { //loop ebayDeListingMain == START
                    deActiveItemIdList.add(ebayDe.getString("itemId"));
                }   //loop ebayDeListingMain == END
                
                EntityCondition ebayDeConditionVar = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                   EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                   EntityCondition.makeCondition("itemId", EntityOperator.IN, deActiveItemIdList),
                                                                                                   EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                   ));
                List<GenericValue> ebayDeListingVar = delegator.findList("EbayActiveListingVariation",
                                                                         ebayDeConditionVar,
                                                                         UtilMisc.toSet("itemId"),
                                                                         UtilMisc.toList("itemId"),
                                                                         new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                         true);
                //Has Variation listing == END
                //Debug.logError("ebayUkListing: " + ebayUkListing.size() + ", ebayUkListingVar: " +  ebayUkListingVar.size(), module);
                deCount = ebayDeListing.size() + ebayDeListingVar.size();
                //Debug.logError("UKCOUNT: " + ukCount, module);
                List<GenericValue> ebayDeGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "DE"), null, false);
                GenericValue ebayDeGmsDetail = null;
                if (UtilValidate.isEmpty(ebayDeGmsDetailList)) {
                    String newSeqId = delegator.getNextSeqId("ProductGmsDetail");
                    ebayDeGmsDetail = delegator.makeValue("ProductGmsDetail", UtilMisc.toMap("id", newSeqId, "productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "DE"));
                } else {
                    ebayDeGmsDetail = EntityUtil.getFirst(ebayDeGmsDetailList);
                }
                ebayDeGmsDetail.set("totalCount", Long.valueOf(deCount));
                delegator.createOrStore(ebayDeGmsDetail);
                //update ebay DE GMS == END
                
                //update ebay FR GMS == START
                int frCount = 0;
                //NO Variation listing == START
                EntityCondition ebayFrCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                EntityCondition.makeCondition("site", EntityOperator.EQUALS, "France"),
                                                                                                EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                EntityCondition.makeCondition("hasVariation", EntityOperator.EQUALS, "N"),
                                                                                                EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                ));
                List<GenericValue> ebayFrListing = delegator.findList("EbayActiveListing", ebayFrCondition,
                                                                      UtilMisc.toSet("itemId"),
                                                                      UtilMisc.toList("itemId"),
                                                                      new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                      true);
                //NO Variation listing == END
                //Has Variation listing == START
                List<String> frActiveItemIdList = new ArrayList<String>();
                EntityCondition ebayFrConditionMain = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                    EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                    EntityCondition.makeCondition("site", EntityOperator.EQUALS, "France"),
                                                                                                    EntityCondition.makeCondition("sellStatListingStatus", EntityOperator.EQUALS, "Active"),
                                                                                                    EntityCondition.makeCondition("hasVariation", EntityOperator.EQUALS, "Y")
                                                                                                    ));
                List<GenericValue> ebayFrListingMain = delegator.findList("EbayActiveListing", ebayFrConditionMain, null, null, null, true);
                for (GenericValue ebayFr : ebayFrListingMain) { //loop ebayFrListingMain == START
                    frActiveItemIdList.add(ebayFr.getString("itemId"));
                }   //loop ebayFrListingMain == END
                
                EntityCondition ebayFrConditionVar = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                   EntityCondition.makeCondition("productStoreId", EntityOperator.IN, gudaoProductStoreList),
                                                                                                   EntityCondition.makeCondition("itemId", EntityOperator.IN, frActiveItemIdList),
                                                                                                   EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                   ));
                List<GenericValue> ebayFrListingVar = delegator.findList("EbayActiveListingVariation",
                                                                         ebayFrConditionVar,
                                                                         UtilMisc.toSet("itemId"),
                                                                         UtilMisc.toList("itemId"),
                                                                         new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                         true);
                //Has Variation listing == END
                //Debug.logError("ebayUkListing: " + ebayUkListing.size() + ", ebayUkListingVar: " +  ebayUkListingVar.size(), module);
                frCount = ebayFrListing.size() + ebayFrListingVar.size();
                //Debug.logError("UKCOUNT: " + ukCount, module);
                List<GenericValue> ebayFrGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "FR"), null, false);
                GenericValue ebayFrGmsDetail = null;
                if (UtilValidate.isEmpty(ebayFrGmsDetailList)) {
                    String newSeqId = delegator.getNextSeqId("ProductGmsDetail");
                    ebayFrGmsDetail = delegator.makeValue("ProductGmsDetail", UtilMisc.toMap("id", newSeqId, "productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "FR"));
                } else {
                    ebayFrGmsDetail = EntityUtil.getFirst(ebayFrGmsDetailList);
                }
                ebayFrGmsDetail.set("totalCount", Long.valueOf(frCount));
                delegator.createOrStore(ebayFrGmsDetail);
                //update ebay FR GMS == END
                
                
                //update wish == START
                int wishCount = 0;
                List<GenericValue> lastWishListing = delegator.findByAnd("WishListing", UtilMisc.toMap("reviewStatus", "APPROVED"), UtilMisc.toList("date DESC"), true);
                if (UtilValidate.isNotEmpty(lastWishListing)) { //if wish has listing == START
                    java.sql.Date wishLastDate = EntityUtil.getFirst(lastWishListing).getDate("date");
                    List<String> wishApprovedListingIdList = new ArrayList<String>();
                    List<GenericValue> wishListingList = delegator.findByAnd("WishListing", UtilMisc.toMap("reviewStatus", "APPROVED", "date", wishLastDate), null, true);
                    for (GenericValue wishListing : wishListingList) { //loop wishListing == START
                        wishApprovedListingIdList.add(wishListing.getString("listingId"));
                    }   //loop wishListing == END
                    
                    EntityCondition wishConditionVar = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                    EntityCondition.makeCondition("listingId", EntityOperator.IN, wishApprovedListingIdList),
                                                                                                    EntityCondition.makeCondition("enabled", EntityOperator.EQUALS, "TRUE"),
                                                                                                    EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId)
                                                                                                    ));
                    List<GenericValue> wishListingVar = delegator.findList("WishListingVariation", wishConditionVar, null, null, null, true);
                    wishCount = wishListingVar.size();
                    
                }   //if wish has listing == END
                List<GenericValue> wishGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "WISH", "site", "US"), null, false);
                GenericValue wishGmsDetail = null;
                if (UtilValidate.isEmpty(wishGmsDetailList)) {
                    String newSeqId = delegator.getNextSeqId("ProductGmsDetail");
                    wishGmsDetail = delegator.makeValue("ProductGmsDetail", UtilMisc.toMap("id", newSeqId, "productId", productId, "type", "LISTING_COUNT", "platform", "WISH", "site", "US"));
                } else {
                    wishGmsDetail = EntityUtil.getFirst(wishGmsDetailList);
                }
                wishGmsDetail.set("totalCount", Long.valueOf(wishCount));
                delegator.createOrStore(wishGmsDetail);
                //update wish == END
                
            }   //loop productList == END
            
            
        }   //main try == END
        catch (GenericEntityException e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }
        Debug.logError("Finished Running updateProductGmsEbayDetail", module);
        return result;

        
    }   //updateProductGmsEbayDetail
    
    public static Map<String, Object> createProductGqs (DispatchContext dctx, Map<String, ? extends Object> context) {  //createProductGqs
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Map result = ServiceUtil.returnSuccess();
        
        String platform = (String) context.get("platform");
        String channel = (String) context.get("channel");
        String type = (String) context.get("type");
        String shipType = (String) context.get("shipType");
        String account = (String) context.get("account");
        String orderId = (String) context.get("orderId");
        String customerId = (String) context.get("customerId");
        String ownerGroup = (String) context.get("ownerGroup");
        String description = (String) context.get("description");
        String fullRefund = (String) context.get("fullRefund");
        String partialRefund = (String) context.get("partialRefund");
        String replacement = (String) context.get("replacement");
        String noAction = (String) context.get("noAction");
        String currency = (String) context.get("currency");
        BigDecimal refundAmount = (BigDecimal) context.get("refundAmount");
        BigDecimal replacementProductCost = (BigDecimal) context.get("replacementProductCost");
        BigDecimal replacementShippingCost = (BigDecimal) context.get("replacementShippingCost");
        String countryDestination = (String) context.get("countryDestination");
        
        DecimalFormat df = new DecimalFormat("00000");
        
        try {   //main try == START
            //updating ProductGqsDetail == START
            GenericValue productGqsDetail = delegator.makeValue("ProductGqsDetail", UtilMisc.toMap("orderId", orderId));
            productGqsDetail.set("platform", platform);
            productGqsDetail.set("channel", channel);
            productGqsDetail.set("type", type);
            productGqsDetail.set("shipType", shipType);
            productGqsDetail.set("account", account.trim());
            productGqsDetail.set("orderId", orderId.trim());
            productGqsDetail.set("customerId", customerId.trim());
            productGqsDetail.set("ownerGroup", ownerGroup);
            productGqsDetail.set("description", description);
            productGqsDetail.set("currencyId", currency);
            
            if(UtilValidate.isNotEmpty(refundAmount)) {
                productGqsDetail.set("refundAmount", refundAmount);
            }
            if(UtilValidate.isNotEmpty(replacementProductCost)) {
                productGqsDetail.set("replacementProductCost", replacementProductCost);
            }
            if(UtilValidate.isNotEmpty(replacementShippingCost)) {
                productGqsDetail.set("replacementShippingCost", replacementShippingCost);
            }
            
            productGqsDetail.set("countryDestination", countryDestination);
            productGqsDetail.set("fullRefund", fullRefund);
            productGqsDetail.set("partialRefund", partialRefund);
            productGqsDetail.set("replacement", replacement);
            productGqsDetail.set("noAction", noAction);
            productGqsDetail.set("createdBy", userLogin.getString("userLoginId"));
            delegator.create(productGqsDetail);
            
            //updating ProductGqsDetail == END
            int maxSku = 100;
            for (int i = 1; i <= maxSku; i++) {
                String productId = (String) context.get("productId[" + i + "]");
                
                if (UtilValidate.isEmpty(productId)) {
                    break;
                }
                int qty = (Integer) context.get("qty[" + i + "]");
                String seqId = df.format(i);
                //Debug.logError("run until here", module);
                BigDecimal refundAmountChild = (BigDecimal) context.get("refundAmount[" + i + "]");
                BigDecimal replacementProductCostChild = (BigDecimal) context.get("replacementProductCost[" + i + "]");
                BigDecimal replacementShippingCostChild = (BigDecimal) context.get("replacementShippingCost[" + i + "]");
                
                //Debug.logError("SKU " + i + " = " + productId + ", and qty: " + qty + "refund is " + refundAmountChild.toString(), module);
                GenericValue productGqsDetailProduct = delegator.makeValue("ProductGqsDetailProduct", UtilMisc.toMap("orderId", orderId, "seqId", seqId));
                productGqsDetailProduct.set("productId", productId);
                productGqsDetailProduct.set("quantity", Long.valueOf(qty));
                if(UtilValidate.isNotEmpty(refundAmountChild)) {
                    productGqsDetailProduct.set("refundAmount", refundAmountChild);
                }
                if(UtilValidate.isNotEmpty(replacementProductCostChild)) {
                    productGqsDetailProduct.set("replacementProductCost", replacementProductCostChild);
                }
                if(UtilValidate.isNotEmpty(replacementShippingCostChild)) {
                    productGqsDetailProduct.set("replacementShippingCost", replacementShippingCostChild);
                }
                delegator.create(productGqsDetailProduct);
            }
            
        }   //main try == END
        /*catch (GenericEntityException e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }*/
        catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }
        
        return result;

        
    }   //createProductGqs
    
    public static Map<String, Object> updateProductGqs (DispatchContext dctx, Map<String, ? extends Object> context) {  //createProductGqs
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Map result = ServiceUtil.returnSuccess();
        
        String platform = (String) context.get("platform");
        String channel = (String) context.get("channel");
        String type = (String) context.get("type");
        String shipType = (String) context.get("shipType");
        String account = (String) context.get("account");
        String orderId = (String) context.get("orderId");
        String customerId = (String) context.get("customerId");
        String ownerGroup = (String) context.get("ownerGroup");
        String description = (String) context.get("description");
        String fullRefund = (String) context.get("fullRefund");
        String partialRefund = (String) context.get("partialRefund");
        String replacement = (String) context.get("replacement");
        String noAction = (String) context.get("noAction");
        String currency = (String) context.get("currencyId");
        BigDecimal refundAmount = (BigDecimal) context.get("refundAmount");
        BigDecimal replacementProductCost = (BigDecimal) context.get("replacementProductCost");
        BigDecimal replacementShippingCost = (BigDecimal) context.get("replacementShippingCost");
        String countryDestination = (String) context.get("countryDestination");
        
        DecimalFormat df = new DecimalFormat("00000");
        
        try {   //main try == START
            //updating ProductGqsDetail == START
            GenericValue productGqsDetail = delegator.findOne("ProductGqsDetail", UtilMisc.toMap("orderId", orderId), false);
            productGqsDetail.set("platform", platform);
            productGqsDetail.set("channel", channel);
            productGqsDetail.set("type", type);
            productGqsDetail.set("shipType", shipType);
            productGqsDetail.set("account", account.trim());
            productGqsDetail.set("orderId", orderId.trim());
            productGqsDetail.set("customerId", customerId.trim());
            productGqsDetail.set("ownerGroup", ownerGroup);
            productGqsDetail.set("description", description);
            productGqsDetail.set("currencyId", currency);
            
            if(UtilValidate.isNotEmpty(refundAmount)) {
                productGqsDetail.set("refundAmount", refundAmount);
            }
            if(UtilValidate.isNotEmpty(replacementProductCost)) {
                productGqsDetail.set("replacementProductCost", replacementProductCost);
            }
            if(UtilValidate.isNotEmpty(replacementShippingCost)) {
                productGqsDetail.set("replacementShippingCost", replacementShippingCost);
            }
            
            productGqsDetail.set("countryDestination", countryDestination);
            productGqsDetail.set("fullRefund", fullRefund);
            productGqsDetail.set("partialRefund", partialRefund);
            productGqsDetail.set("replacement", replacement);
            productGqsDetail.set("noAction", noAction);
            productGqsDetail.set("createdBy", userLogin.getString("userLoginId"));
            delegator.store(productGqsDetail);
            
            //updating ProductGqsDetail == END
            delegator.removeByAnd("ProductGqsDetailProduct", UtilMisc.toMap("orderId", orderId));
            int maxSku = 100;
            for (int i = 1; i <= maxSku; i++) {
                String productId = (String) context.get("productId[" + i + "]");
                
                if (UtilValidate.isEmpty(productId)) {
                    break;
                }
                int qty = (Integer) context.get("qty[" + i + "]");
                String seqId = df.format(i);
                BigDecimal refundAmountChild = (BigDecimal) context.get("refundAmount[" + i + "]");
                BigDecimal replacementProductCostChild = (BigDecimal) context.get("replacementProductCost[" + i + "]");
                BigDecimal replacementShippingCostChild = (BigDecimal) context.get("replacementShippingCost[" + i + "]");
                
                //Debug.logError("SKU " + i + " = " + productId + ", and qty: " + qty, module);
                GenericValue productGqsDetailProduct = delegator.makeValue("ProductGqsDetailProduct", UtilMisc.toMap("orderId", orderId, "seqId", seqId));
                productGqsDetailProduct.set("productId", productId);
                productGqsDetailProduct.set("quantity", Long.valueOf(qty));
                if(UtilValidate.isNotEmpty(refundAmountChild)) {
                    productGqsDetailProduct.set("refundAmount", refundAmountChild);
                }
                if(UtilValidate.isNotEmpty(replacementProductCostChild)) {
                    productGqsDetailProduct.set("replacementProductCost", replacementProductCostChild);
                }
                if(UtilValidate.isNotEmpty(replacementShippingCostChild)) {
                    productGqsDetailProduct.set("replacementShippingCost", replacementShippingCostChild);
                }
                delegator.create(productGqsDetailProduct);
                
            }
            
        }   //main try == END
        /*catch (GenericEntityException e) {
         e.printStackTrace();
         result = ServiceUtil.returnError(e.getMessage());
         }*/
        catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }
        
        return result;
        
        
    }   //updateProductGqs
    
    public static Map<String, Object> updateProductEps (DispatchContext dctx, Map<String, ? extends Object> context) {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Map result = ServiceUtil.returnSuccess();
        Calendar fromDay = Calendar.getInstance();
        fromDay.add(Calendar.DATE, -30);
        java.sql.Date fromDate = new java.sql.Date(fromDay.getTimeInMillis());
        
        String productIdInput = (String) context.get("productIdInput");
        String ownerGroupInput = (String) context.get("ownerGroupInput");
        String productGroupInput = (String) context.get("productGroupInput");
        
        //Debug.logError("Running updateProductGps", module);
        
        try {   //main try -- START
            EntityCondition productListCondition = null;
            
            if (UtilValidate.isNotEmpty(productGroupInput)) {   //productListCondition == START
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, productGroupInput)
                                                                                     ));
            } else if (UtilValidate.isNotEmpty(ownerGroupInput)) {
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.NOT_IN, UtilMisc.toList("G4")),
                                                                                     EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroupInput)
                                                                                     ));
            } else if (UtilValidate.isNotEmpty(productIdInput)) {
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.NOT_IN, UtilMisc.toList("G4")),
                                                                                     EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productIdInput)
                                                                                     ));
            } else {
                productListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                     EntityCondition.makeCondition("productGroup", EntityOperator.NOT_IN, UtilMisc.toList("G4"))
                                                                                     ));
            }//productListCondition == END
            List<GenericValue> productList = delegator.findList("ProductMaster", productListCondition, null, null, null, false);
            //Debug.logError("productList size is " + productList.size(), module);
            for (GenericValue product : productList) {  //loop productList == START
                String productId = product.getString("productId");
                String ownerGroup = product.getString("ownerGroup");
                GenericValue ebayUsPrice = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "US"), false);
                boolean hasIdentical = false;
                //Debug.logError("productId " + productId, module);
                
                double rivalDailySoldValue = 0.0;
                long rivalDailySoldScore = 0;
                long rivalDailySoldWeight = 0;
                long rivalDailySoldGps = 0;
                
                double rivalDailySalesValue = 0.0;
                long rivalDailySalesScore = 0;
                long rivalDailySalesWeight = 0;
                long rivalDailySalesGps = 0;
                
                double samePriceProfitPctValue = ebayUsPrice.getDouble("profitPercentage");
                long samePriceProfitPctScore = 0;
                long samePriceProfitPctWeight = 0;
                long samePriceProfitPctGps = 0;
                
                //get Rival data == START
                String rivalCurrency = "USD";
                long variationCount = 1;
                
                List<EntityCondition> conditions = FastList.newInstance();
                conditions.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
                conditions.add(EntityCondition.makeCondition("rivalPlatform", EntityOperator.EQUALS, "EBAY"));
                conditions.add(EntityCondition.makeCondition("rivalItemId", EntityOperator.NOT_EQUAL, null));
                EntityCondition condition = EntityCondition.makeCondition(conditions, EntityOperator.AND);
                
                List<GenericValue> rivalList = delegator.findList("ProductMasterRival", condition, null, null, null, false);
                //Debug.logError("rivalList size: " + rivalList.size(), module);
                if (UtilValidate.isNotEmpty(rivalList)) {   //rivalList is not Empty == START
                    
                    hasIdentical = true;
                    String rivalItemId = EntityUtil.getFirst(rivalList).getString("rivalItemId");
                    rivalCurrency = EntityUtil.getFirst(rivalList).getString("currency");
                    EntityCondition rivalListingResultCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                              EntityCondition.makeCondition("rivalItemId",EntityOperator.EQUALS , rivalItemId),
                                                                                              EntityCondition.makeCondition("date",EntityOperator.GREATER_THAN , fromDate)
                                                                                              ));
                    List<GenericValue> rivalListingResultList = delegator.findList("RivalListingResult", rivalListingResultCondition, null, UtilMisc.toList("date ASC"), null, false);
                    //Debug.logError("rivalListingResultList size: " + rivalListingResultList.size(), module);
                    if (rivalListingResultList.size() > 1) {    //if Crawling has run for one month == START
                        java.sql.Date firstSnapshotDate = EntityUtil.getFirst(rivalListingResultList).getDate("date");
                        //Debug.logError(fromDate + "", module);
                        java.sql.Date lastSnapshotDate = null;
                        
                        double firstHistorySold = EntityUtil.getFirst(rivalListingResultList).getDouble("rivalHistorySold");
                        double lastHistorySold = 0.0;
                        double listingResultHistorySales = 0.0;
                        double previousHistorySold = firstHistorySold;
                        
                        for (GenericValue rivalListingResult : rivalListingResultList) {    // loop rivalListingResultList == START
                            if (UtilValidate.isNotEmpty(rivalListingResult.getLong("variationCount"))) {
                                variationCount = rivalListingResult.getLong("variationCount");
                            }
                            lastSnapshotDate = rivalListingResult.getDate("date");
                            lastHistorySold = rivalListingResult.getDouble("rivalHistorySold");
                            double rivalCurrentPrice = rivalListingResult.getDouble("rivalCurrentPrice");
                            listingResultHistorySales = listingResultHistorySales + ((lastHistorySold - previousHistorySold) * rivalCurrentPrice);
                            //Debug.logError("listingResultHistorySales: " + listingResultHistorySales + ", lastHistorySold: " + lastHistorySold + ", previousHistorySold: " + previousHistorySold, module);
                            previousHistorySold = lastHistorySold;
                        }   // loop rivalListingResultList == START
                        double dayDiff = (double) Math.abs(lastSnapshotDate.getTime() - firstSnapshotDate.getTime()) / (24 * 60 * 60 * 1000);
                        //Debug.logError("listingResultHistorySales: " + listingResultHistorySales + ", firstHistorySold: "+ firstHistorySold + ", lastHistorySold: " + lastHistorySold + ", dayDiff: " + dayDiff, module);
                        
                        if (dayDiff >= 6) { //if gap calculation day between first and second crawl more than equals to 6 days == START
                            rivalDailySoldValue = (lastHistorySold - firstHistorySold) / dayDiff;
                            rivalDailySalesValue = listingResultHistorySales / dayDiff;
                        }   //if gap calculation day between first and second crawl more than equals to 6 days == END
                        else {
                            if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("soldPerDay"))) {
                                rivalDailySoldValue = EntityUtil.getFirst(rivalList).getDouble("soldPerDay");
                            } else {
                                rivalDailySoldValue = 0.0;
                            }
                            if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("dailySales"))) {
                                rivalDailySalesValue = EntityUtil.getFirst(rivalList).getDouble("dailySales");
                            } else {
                                rivalDailySalesValue = 0.0;
                            }
                        }
                        
                    }   //if Crawling has run for one month == END
                    else {  //if Crawling has NOT run for one month divides by listing lifetime == START
                        if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getLong("variationCount"))) {
                            variationCount = EntityUtil.getFirst(rivalList).getLong("variationCount");
                        }
                        if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("soldPerDay"))) {
                            rivalDailySoldValue = EntityUtil.getFirst(rivalList).getDouble("soldPerDay");
                        } else {
                            rivalDailySoldValue = 0.0;
                        }
                        if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("dailySales"))) {
                            rivalDailySalesValue = EntityUtil.getFirst(rivalList).getDouble("dailySales");
                        } else {
                            rivalDailySalesValue = 0.0;
                        }
                    }   //if Crawling has NOT run for one month divides by listing lifetime == END
                    
                    if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("samePriceProfitPercentage"))) {
                        samePriceProfitPctValue = EntityUtil.getFirst(rivalList).getDouble("samePriceProfitPercentage");
                    } else {
                        samePriceProfitPctValue = ebayUsPrice.getDouble("profitPercentage");
                    }
                    
                }   //rivalList is not Empty == END
                else {  //rivalList is Empty == START
                    List<EntityCondition> similarConditions = FastList.newInstance();
                    similarConditions.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
                    similarConditions.add(EntityCondition.makeCondition("rivalPlatform", EntityOperator.EQUALS, "EBAY_SIMILAR"));
                    similarConditions.add(EntityCondition.makeCondition("rivalItemId", EntityOperator.NOT_EQUAL, null));
                    EntityCondition similarCondition = EntityCondition.makeCondition(similarConditions, EntityOperator.AND);
                    rivalList = delegator.findList("ProductMasterRival", similarCondition, null, null, null, false);
                    //Debug.logError("empty Rival List, else block: rivalList size: " + rivalList.size(), module);
                    if (UtilValidate.isNotEmpty(rivalList)) {    //if rivalList for similar item ID is not empty == START
                        String rivalItemId = EntityUtil.getFirst(rivalList).getString("rivalItemId");
                        rivalCurrency = EntityUtil.getFirst(rivalList).getString("currency");
                        EntityCondition rivalListingResultListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                  EntityCondition.makeCondition("rivalItemId",EntityOperator.EQUALS , rivalItemId),
                                                                                                  EntityCondition.makeCondition("date",EntityOperator.GREATER_THAN , fromDate)
                                                                                                  ));
                        List<GenericValue> rivalListingResultList = delegator.findList("RivalListingResult", rivalListingResultListCondition, null, UtilMisc.toList("date ASC"), null, false);
                        if (rivalListingResultList.size() > 1) {    //if Crawling has run for one month == START
                            java.sql.Date firstSnapshotDate = EntityUtil.getFirst(rivalListingResultList).getDate("date");
                            //Debug.logError(fromDate + "", module);
                            java.sql.Date lastSnapshotDate = null;
                            
                            double firstHistorySold = EntityUtil.getFirst(rivalListingResultList).getDouble("rivalHistorySold");
                            double lastHistorySold = 0.0;
                            double listingResultHistorySales = 0.0;
                            double previousHistorySold = firstHistorySold;
                            
                            for (GenericValue rivalListingResult : rivalListingResultList) {    // loop rivalListingResultList == START
                                if (UtilValidate.isNotEmpty(rivalListingResult.getLong("variationCount"))) {
                                    variationCount = rivalListingResult.getLong("variationCount");
                                }
                                lastSnapshotDate = rivalListingResult.getDate("date");
                                lastHistorySold = rivalListingResult.getDouble("rivalHistorySold");
                                double rivalCurrentPrice = rivalListingResult.getDouble("rivalCurrentPrice");
                                listingResultHistorySales = listingResultHistorySales + ((lastHistorySold - previousHistorySold) * rivalCurrentPrice);
                                //Debug.logError("listingResultHistorySales: " + listingResultHistorySales + ", lastHistorySold: " + lastHistorySold + ", previousHistorySold: " + previousHistorySold, module);
                                previousHistorySold = lastHistorySold;
                            }   // loop rivalListingResultList == START
                            double dayDiff = (double) Math.abs(lastSnapshotDate.getTime() - firstSnapshotDate.getTime()) / (24 * 60 * 60 * 1000);
                            //Debug.logError("listingResultHistorySales: " + listingResultHistorySales + ", firstHistorySold: "+ firstHistorySold + ", lastHistorySold: " + lastHistorySold + ", dayDiff: " + dayDiff, module);
                            
                            if (dayDiff >= 6) { //if gap calculation day between first and second crawl more than equals to 6 days == START
                                rivalDailySoldValue = (lastHistorySold - firstHistorySold) / dayDiff;
                                rivalDailySalesValue = listingResultHistorySales / dayDiff;
                            }   //if gap calculation day between first and second crawl more than equals to 6 days == END
                            else {
                                if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("soldPerDay"))) {
                                    rivalDailySoldValue = EntityUtil.getFirst(rivalList).getDouble("soldPerDay");
                                } else {
                                    rivalDailySoldValue = 0.0;
                                }
                                if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("dailySales"))) {
                                    rivalDailySalesValue = EntityUtil.getFirst(rivalList).getDouble("dailySales");
                                } else {
                                    rivalDailySalesValue = 0.0;
                                }
                            }
                        }   //if Crawling has run for one month == END
                        else {  //if Crawling has NOT run for one month divides by listing lifetime == START
                            if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getLong("variationCount"))) {
                                variationCount = EntityUtil.getFirst(rivalList).getLong("variationCount");
                            }
                            if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("soldPerDay"))) {
                                rivalDailySoldValue = EntityUtil.getFirst(rivalList).getDouble("soldPerDay");
                            } else {
                                rivalDailySoldValue = 0.0;
                            }
                            if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("dailySales"))) {
                                rivalDailySalesValue = EntityUtil.getFirst(rivalList).getDouble("dailySales");
                            } else {
                                rivalDailySalesValue = 0.0;
                            }
                        }   //if Crawling has NOT run for one month divides by listing lifetime == END
                        
                        if (UtilValidate.isNotEmpty(EntityUtil.getFirst(rivalList).getDouble("samePriceProfitPercentage"))) {
                            samePriceProfitPctValue = EntityUtil.getFirst(rivalList).getDouble("samePriceProfitPercentage");
                        } else {
                            samePriceProfitPctValue = ebayUsPrice.getDouble("profitPercentage");
                        }
                    }   //if rivalList for similar item ID is not empty == END
                    else {
                        rivalDailySoldValue = 0.0;
                        rivalDailySalesValue = 0.0;
                        samePriceProfitPctValue = ebayUsPrice.getDouble("profitPercentage");
                    }
                }   //rivalList is Empty == END
                //Debug.logError("run until here:", module);
                //Debug.logError("rivalCurrency:" + rivalCurrency, module);
                double conversionFactor = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", rivalCurrency), null, false)).getDouble("conversionFactor");
                //Debug.logError("run until here 1:", module);
                //rivalDailySoldValue = rivalDailySoldValue / (double) variationCount / 2;
                //rivalDailySalesValue = rivalDailySalesValue / (double) variationCount / 2;
                rivalDailySoldValue = rivalDailySoldValue / (double) (((variationCount - 1) * 0.15) + 1);
                rivalDailySalesValue = rivalDailySalesValue / (double) (((variationCount - 1) * 0.15) + 1) / conversionFactor;
                //get Rival data == END
                //Debug.logError("run until here 2:", module);
                double rivalProfitPct = 0.15;
                double weightFactor = 1.0;
                double usdToRmb = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CNY"), null, false)).getDouble("conversionFactor");
                //Debug.logError("run until here 3:", module);
                double eps = 0.0;
                if (hasIdentical) {
                    weightFactor = 3.0;
                } else {
                    weightFactor = 1.0;
                }
                eps = rivalDailySalesValue * rivalProfitPct * usdToRmb * weightFactor;
                
                if (samePriceProfitPctValue < 0.15) {
                    eps = eps * (-1);
                }
                //Debug.logError("run until here 4:", module);
                GenericValue productEpsGV = delegator.makeValue("ProductEps", UtilMisc.toMap("productId", productId));
                productEpsGV.set("rivalDailySold", rivalDailySoldValue);
                productEpsGV.set("rivalDailySales", rivalDailySalesValue);
                productEpsGV.set("samePriceProfitPct", samePriceProfitPctValue);
                productEpsGV.set("eps", eps);
                delegator.createOrStore(productEpsGV);
                
                
                
                //FINAL - update productMaster == START
                //long finalEps = Long.valueOf(Math.round(eps));
                //Debug.logError("finalGps: " + finalGps, module);
                GenericValue pm = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
                pm.set("finalEps", eps);
                delegator.store(pm);
                //FINAL - update productMaster == END
            }   //loop productList == END
        } catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }   //main try -- END
        
        //Debug.logError("Finished Running updateProductGps", module);
        return result;
    }   //updateProductEps
    
    public static Map<String, Object> epsCheatSheet (DispatchContext dctx, Map<String, ? extends Object> context) {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String itemId = (String) context.get("itemId");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = new Date();
        
        Map result = ServiceUtil.returnSuccess();
        Map resultMap = FastMap.newInstance();
        
        try {   //main try == START
            boolean crawl = true;
            if (UtilValidate.isEmpty(itemId)) {
                crawl = false;
            } else {
                if (itemId.length() < 9 || itemId.length() > 12 || itemId.matches(".*000000")) {
                    result.put("notes", "ItemID format is not correct or invalid");
                    crawl = false;
                }
                itemId = itemId.trim();
            }
            
            if (crawl) {    //crawl == START
                double rivalShipping = 0.0;
                double rivalCurrentPrice = 0.0;
                double rivalOriginalPrice = 0.0;
                double rivalListingLifetime = 0.0;
                double rivalHistorySold = 0.0;
                double rivalDailySales = 0.0;
                double samePriceProfitPercentage = 0.0;
                double soldPerDay = 0.0;
                String rivalCurrency = null;
                String countryLocation = null;
                String rivalListDetStartTime = null;
                String listingType = null;
                String sellingStatus = null;
                int variationCount = 1;
                
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
                
                UtilXml.addChildElementValue(rootElem, "ItemID", itemId, rootDoc);
                UtilXml.addChildElementValue(rootElem, "IncludeItemSpecifics", "false", rootDoc);
                UtilXml.addChildElementValue(rootElem, "DetailLevel", "ReturnAll", rootDoc);
                
                String requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                //Building XML -- END
                
                String responseXML = eBayTradingAPI.sendRequestXMLToEbay(mapAccount, requestXMLcode);
                //Debug.logError(responseXML, module);
                Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                Element elemResponse = docResponse.getDocumentElement();
                String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                
                if (!ack.equals("Success") && !ack.equals("Warning")) { //if ack failed == START
                    List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                    Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                    StringBuffer errorMessage = new StringBuffer();
                    while (errorElementsElemIter.hasNext()) {   //while errorElement -- START
                        Element errorElement = errorElementsElemIter.next();
                        String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                        String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                        errorMessage.append(shortMessage + " - " + longMessage);
                    }
                    result.put("notes", errorMessage.toString());
                }   //if ack failed == END
                else {  //if ack success == START
                    List<? extends Element> itemElements = UtilXml.childElementList(elemResponse, "Item");
                    Iterator<? extends Element> itemsElemIter = itemElements.iterator();
                    while (itemsElemIter.hasNext()) {    //loop itemsElemIter -- START
                        Map itemMap = FastMap.newInstance();
                        Element itemElement = itemsElemIter.next();
                        rivalCurrency = UtilXml.childElementValue(itemElement, "Currency", null);
                        countryLocation = UtilXml.childElementValue(itemElement, "Country", null);
                        listingType = UtilXml.childElementValue(itemElement, "ListingType", null);
                        
                        //get prices -- START
                        List<? extends Element> sellingStatusList = UtilXml.childElementList(itemElement, "SellingStatus");
                        Iterator<? extends Element> sellingStatusListElemIter = sellingStatusList.iterator();
                        while (sellingStatusListElemIter.hasNext()) {   //loop sellingStatusListElemIter -- START
                            Element sellingStatusElement = sellingStatusListElemIter.next();
                            sellingStatus = UtilXml.childElementValue(sellingStatusElement, "ListingStatus", null);
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
                            rivalListDetStartTime = UtilXml.childElementValue(listingDetailElement, "StartTime", null).substring(0,10);
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
                            
                        } else {
                            rivalDailySales = 0.0;
                        }
                        //calculate rivalDailySales -- END
                        
                    }   //loop itemsElemIter -- END
                    
                    double conversionFactor = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", rivalCurrency), null, false)).getDouble("conversionFactor");
                    
                    soldPerDay = soldPerDay / (double) (((variationCount - 1) * 0.15) + 1);
                    rivalDailySales = rivalDailySales / (double) (((variationCount - 1) * 0.15) + 1) / conversionFactor;
                    //get Rival data == END
                    
                    double rivalProfitPct = 0.15;
                    double weightFactor = 1.0;
                    double usdToRmb = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CNY"), null, false)).getDouble("conversionFactor");
                    //Debug.logError("run until here 3:", module);
                    double eps = 0.0;
                    eps = rivalDailySales * rivalProfitPct * usdToRmb * weightFactor;
                    
                    boolean localSeller = false;
                    if (countryLocation.equals("US") && rivalCurrency.equals("USD")) {   //US local seller == START
                        localSeller = true;
                    }   //US local seller == END
                    if (countryLocation.equals("GB") && rivalCurrency.equals("GBP")) {   //GB local seller == START
                        localSeller = true;
                    }   //GB local seller == END
                    if (countryLocation.equals("VG") && rivalCurrency.equals("EUR")) {   //VG local seller == START
                        localSeller = true;
                    }   //VG local seller == END
                    if (countryLocation.equals("AU") && rivalCurrency.equals("AUD")) {   //AU local seller == START
                        localSeller = true;
                    }   //AU local seller == END
                    if (countryLocation.equals("DE") && rivalCurrency.equals("EUR")) {   //DE local seller == START
                        localSeller = true;
                    }   //DE local seller == END
                    if (countryLocation.equals("IT") && rivalCurrency.equals("EUR")) {   //IT local seller == START
                        localSeller = true;
                    }   //IT local seller == END
                    if (countryLocation.equals("FR") && rivalCurrency.equals("EUR")) {   //FR local seller == START
                        localSeller = true;
                    }   //FR local seller == END
                    if (countryLocation.equals("ES") && rivalCurrency.equals("EUR")) {   //ES local seller == START
                        localSeller = true;
                    }   //ES local seller == END

                    if (listingType.equals("Chinese")) {
                        listingType = "Auction";
                    }
                    result.put("localSeller", localSeller);
                    result.put("rivalDailySales", rivalDailySales);
                    result.put("soldPerDay", soldPerDay);
                    result.put("variationCount", variationCount);
                    result.put("rivalListingLifetime", rivalListingLifetime);
                    result.put("eps", eps);
                    result.put("itemId", itemId);
                    result.put("countryLocation", countryLocation);
                    result.put("rivalCurrency", rivalCurrency);
                    result.put("rivalCurrentPrice", rivalCurrentPrice);
                    result.put("rivalHistorySold", rivalHistorySold);
                    result.put("rivalListDetStartTime", rivalListDetStartTime);
                    result.put("listingType", listingType);
                    result.put("sellingStatus", sellingStatus);
                    
                    
                    
                    
                }   //if ack success == END
            }   //crawl == END
        }  catch (Exception e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
        }   //main try == END

        return result;
    }
    
}	//END class