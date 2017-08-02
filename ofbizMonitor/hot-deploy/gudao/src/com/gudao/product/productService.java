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
package com.gudao.product;

import java.sql.Timestamp;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Properties;
import java.lang.Math;
import java.util.Random;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilHttp;
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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.commons.lang.StringUtils;

import com.gudao.bulkModule.bulkService;

import com.csvreader.CsvReader;


public class productService {
	private static final String module = productService.class.getName();
    private static final String eol = System.getProperty("line.separator");
    
    public static boolean showPmDebug ()
    throws IOException {
        
        boolean result = false;
        
        try {   //main try -- START
            Properties properties = new Properties();
            properties.load(new BufferedReader(new InputStreamReader(new FileInputStream("hot-deploy/gudao/config/debug.properties"),
                                                                     "UTF8")));
            Enumeration e = properties.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                if (key.equals("showDebugLog")) {
                    if (properties.getProperty(key).toString().equals("Y")) {
                        result = true;
                    }
                }
            }
        }   //main try -- END
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public static Map<String, Object> gudaoSetAllDiscontinue(DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException   {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productId = (String) context.get("productId");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        try {   //main try == START
            GenericValue productMaster = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
            productMaster.set("statusId", "DISCONTINUED");
            delegator.store(productMaster);
            
            List<GenericValue> productMasterStatusList = delegator.findByAnd("ProductMasterStatus", UtilMisc.toMap("productId", productId), null, false);
            if (UtilValidate.isNotEmpty(productMasterStatusList)) { //if productMasterStatusList is not empty == START
                for (GenericValue productMasterStatus : productMasterStatusList) {  //loop productMasterStatusList == START
                    productMasterStatus.set("statusId", "断货下架");
                    delegator.store(productMasterStatus);
                }   //loop productMasterStatusList == END
            }   //if productMasterStatusList is not empty == END
            else {  //if productMasterStatusList is empty == START
                List<GenericValue> platformList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "GUDAO_PLATFORM"), null, false);
                for (GenericValue platform : platformList) {
                    String platformId = platform.getString("enumCode");
                    GenericValue newProductMasterStatus = delegator.makeValue("ProductMasterStatus", UtilMisc.toMap("productId", productId, "platform", platformId));
                    newProductMasterStatus.set("statusId", "断货下架");
                    delegator.create(newProductMasterStatus);
                }
            }   //if productMasterStatusList is empty == END
        }   //main try == END
        catch (GenericEntityException e){
            Debug.logError(e.toString(), module);
        }
        
        return result;
        
    }   //gudaoSetAllDiscontinue
    
    public static Map<String, Object> gudaoTitleGenerator(DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException   {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productId = (String) context.get("productId");
        String platform = (String) context.get("platform");
        String language = (String) context.get("language");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        if (UtilValidate.isEmpty(language)) {
            language = "en";
        }
        if (UtilValidate.isNotEmpty(platform)) {
            platform = platform.toUpperCase();
        }
        
        if (UtilValidate.isNotEmpty(productId)) {   //if productId is not empty == START
            try {   //main try == START
                GenericValue titleConfigTitleLength = delegator.findOne("ProductTitleConfig", UtilMisc.toMap("type", "LENGTH", "platform", platform), false);
                int configTitleLength = Integer.parseInt(titleConfigTitleLength.getString("value"));
                GenericValue titleConfigTitleSuffix = delegator.findOne("ProductTitleConfig", UtilMisc.toMap("type", "SUFFIX", "platform", platform), false);
                int configTitleSuffix = Integer.parseInt(titleConfigTitleSuffix.getString("value"));
                
                List<GenericValue> mainList = delegator.findByAnd("ProductKeywordSeed", UtilMisc.toMap("productId", productId, "type", "MAIN", "statusId", "ACTIVE", "language", language), null, false);
                List<GenericValue> secondaryList = delegator.findByAnd("ProductKeywordSeed", UtilMisc.toMap("productId", productId, "type", "SECONDARY", "statusId", "ACTIVE", "language", language), null, false);
                List<GenericValue> tertiaryList = delegator.findByAnd("ProductKeywordSeed", UtilMisc.toMap("productId", productId, "type", "TERTIARY", "statusId", "ACTIVE", "language", language), null, false);
                if (UtilValidate.isNotEmpty(mainList)) {    //if keywords has data == START
                    String[] mainKeywordList = new String[mainList.size()];
                    String[] secondaryKeywordList = new String[secondaryList.size()];
                    String[]tertiaryKeywordList = new String[tertiaryList.size()];
                    
                    String[] secondaryKeywordLoopCheck = new String[secondaryList.size()];
                    String[] tertiaryKeywordLoopCheck = new String[tertiaryList.size()];
                    
                    int mainCount = 0;
                    for (GenericValue main : mainList) {
                        mainKeywordList[mainCount] = main.getString("keyword").trim();
                        mainCount++;
                    }
                    
                    int secondaryCount = 0;
                    int secondaryShortestKeywordLength = 100;
                    for (GenericValue secondary : secondaryList) {
                        int secondaryKeywordLength = secondary.getString("keyword").length();
                        secondaryKeywordList[secondaryCount] = secondary.getString("keyword").trim();
                        if (secondaryShortestKeywordLength >= secondaryKeywordLength) {
                            secondaryShortestKeywordLength = secondaryKeywordLength;
                        }
                        secondaryCount++;
                    }
                    
                    int tertiaryCount = 0;
                    int tertiaryShortestKeywordLength = 100;
                    for (GenericValue tertiary : tertiaryList) {
                        int tertiaryKeywordLength = tertiary.getString("keyword").length();
                        tertiaryKeywordList[tertiaryCount] = tertiary.getString("keyword").trim();
                        if (tertiaryShortestKeywordLength >= tertiaryKeywordLength) {
                            tertiaryShortestKeywordLength = tertiaryKeywordLength;
                        }
                        tertiaryCount++;
                    }
                    
                    int availableUrlLength = configTitleLength - configTitleSuffix;
                    int mainListSize = mainList.size();
                    int secondaryListSize = secondaryList.size();
                    int tertiaryListSize = tertiaryList.size();
                    
                    int mainRandom = randomWithRange(1, mainListSize);
                    int secondaryRandom = randomWithRange(1, secondaryListSize);
                    int tertiaryRandom = randomWithRange(1, tertiaryListSize);
                    
                    //Debug.logError("availableUrlLength " + availableUrlLength, module);
                    
                    String mainKeyword = mainKeywordList[mainRandom - 1] + " ";
                    String secondaryKeyword = "";
                    String tertiaryKeyword = tertiaryKeywordList[tertiaryRandom - 1] + " ";
                    
                    boolean keepLoopSecondary = true;
                    boolean keepLoopTertiary = true;
                    boolean randomIncludeTertiary = new Random().nextBoolean();
                    
                    if (randomIncludeTertiary) {
                        tertiaryKeyword = "";
                    }
                    
                    int availableSecondaryLength = availableUrlLength - mainKeyword.length() - tertiaryKeyword.length();
                    
                    StringBuffer secondaryKeywordBuffer = new StringBuffer();
                    int secondaryKeywordLoopCheckCount = 0;
                    do {    //DO loop == START
                        String randomSecondaryKeyword = randomKeyword(secondaryKeywordList);
                        if (UtilValidate.isEmpty(secondaryKeywordBuffer)) {
                            secondaryKeywordBuffer.append(randomSecondaryKeyword);
                            secondaryKeywordLoopCheck[secondaryKeywordLoopCheckCount] = randomSecondaryKeyword;
                            secondaryKeywordLoopCheckCount++;
                            secondaryKeyword = secondaryKeywordBuffer.toString();
                        } else {    //second secondaryKeyword == START
                            if (!secondaryKeywordBuffer.toString().contains(randomSecondaryKeyword)) {    //if secondaryKeyword is not used yet == START
                                if (availableSecondaryLength - secondaryKeywordBuffer.toString().length() >= secondaryShortestKeywordLength) {  //if secondaryKeyword still more than secondaryShortestKeywordLength == START
                                    if ((secondaryKeywordBuffer.toString().length() + randomSecondaryKeyword.length()) <= availableSecondaryLength) {
                                        secondaryKeywordBuffer.append(randomSecondaryKeyword);
                                        secondaryKeyword = secondaryKeywordBuffer.toString();
                                    }
                                    //availableSecondaryLength = availableSecondaryLength - secondaryKeyword.length();
                                }   //if secondaryKeyword still more than secondaryShortestKeywordLength == END
                                secondaryKeywordLoopCheck[secondaryKeywordLoopCheckCount] = randomSecondaryKeyword;
                                secondaryKeywordLoopCheckCount++;
                                
                                if ((secondaryKeywordLoopCheckCount) >= secondaryListSize - 1) {   //break the loop == START
                                    keepLoopSecondary = false;
                                }   //break the loop == END
                            }   //if secondaryKeyword is not used yet == END
                        }   //second secondaryKeyword == END
                        //Debug.logError("secondaryKeywordLoopCheckCount: " + secondaryKeywordLoopCheckCount, module);
                    }   //DO loop == END
                    while (keepLoopSecondary);
                    
                    String resultKeyword = mainKeyword + secondaryKeyword + tertiaryKeyword;
                    //Debug.logError("resultKeyword: " + resultKeyword, module);
                    if (availableUrlLength - resultKeyword.length() >= tertiaryShortestKeywordLength) { //adding tertiary Keyword if still space available == START
                        int availableTertiaryLength = availableUrlLength - resultKeyword.length();
                        StringBuffer tertiaryKeywordBuffer = new StringBuffer();
                        int tertiaryKeywordLoopCheckCount = 0;
                        do {    //DO loop == START
                            String randomTertiaryKeyword = randomKeyword(tertiaryKeywordList);
                            if (UtilValidate.isEmpty(tertiaryKeywordBuffer)) {
                                tertiaryKeywordBuffer.append(randomTertiaryKeyword);
                                tertiaryKeywordLoopCheck[tertiaryKeywordLoopCheckCount] = randomTertiaryKeyword;
                                tertiaryKeywordLoopCheckCount++;
                                tertiaryKeyword = tertiaryKeywordBuffer.toString();
                            } else {    //second tertiaryKeyword == START
                                if (!tertiaryKeywordBuffer.toString().contains(randomTertiaryKeyword)) {    //if tertiaryKeyword is not used yet == START
                                    if (availableTertiaryLength - tertiaryKeywordBuffer.toString().length() >= tertiaryShortestKeywordLength) {  //if tertiaryKeyword still more than tertiaryShortestKeywordLength == START
                                        if ((tertiaryKeywordBuffer.toString().length() + randomTertiaryKeyword.length()) <= availableTertiaryLength) {
                                            tertiaryKeywordBuffer.append(randomTertiaryKeyword);
                                            tertiaryKeyword = tertiaryKeywordBuffer.toString();
                                        }
                                    }   //if tertiaryKeyword still more than tertiaryShortestKeywordLength == END
                                    tertiaryKeywordLoopCheck[tertiaryKeywordLoopCheckCount] = randomTertiaryKeyword;
                                    tertiaryKeywordLoopCheckCount++;
                                    
                                    if ((tertiaryKeywordLoopCheckCount) >= tertiaryListSize - 1) {   //break the loop == START
                                        keepLoopTertiary = false;
                                    }   //break the loop == END
                                }   //if tertiaryKeyword is not used yet == END
                            }   //second tertiaryKeyword == END
                        }   //DO loop == END
                        while (keepLoopTertiary);
                     }   //adding tertiary Keyword if still space available == END
                     
                     if ((resultKeyword + tertiaryKeyword).length() <= availableUrlLength) {
                     resultKeyword = resultKeyword + tertiaryKeyword;
                     }
                    
                    
                    //Debug.logError("resultKeywordLength: " + resultKeyword.length(), module);
                    result.put("title", resultKeyword);
                    result.put("titleLength", resultKeyword.length() + "");
                }   //if keywords has data == END
                else {
                    result.put("title", "");
                    result.put("titleLength", 0 + "");
                }
                
                
            }   //main try == END
            catch (GenericEntityException e){
                Debug.logError(e.toString(), module);
            }
        }   //if productId is not empty == END
        else {
            result.put("title", "");
            result.put("titleLength", 0 + "");
        }
        
        return result;
        
    }   //gudaoTitleGenerator
    
    private static String randomKeyword(String[] keywordList) {
        
        int randomNumber = randomWithRange(1, keywordList.length);
        String result = keywordList[randomNumber - 1] + " ";
        
        return result;
    }   //randomKeyword
    
    public static int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
    
    public static Map<String, Object> gudaoTitleGeneratorBulk (DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException, GenericServiceException {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        String language = (String) context.get("language");
        String productIdInput = (String) context.get("productId");
        
        if (UtilValidate.isEmpty(language)) {
            language = "en";
        }
        
        try {   //main try == START
            List<GenericValue> platformArrayList = delegator.findByAnd("ProductTitleConfig", UtilMisc.toMap("type", "PLATFORM"), null, false);
            GenericValue titleConfigTitleSuffix = EntityUtil.getFirst(delegator.findByAnd("ProductTitleConfig", UtilMisc.toMap("type", "QUANTITY"), null, false));
            int titleQty = Integer.parseInt(titleConfigTitleSuffix.getString("value"));
            //List<GenericValue> productTitleList = null;
            List<String> productIdKeywordList = FastList.newInstance();
            if (UtilValidate.isNotEmpty(productIdInput)) {
                List<GenericValue> productTitleList = delegator.findList("ProductMaster", EntityCondition.makeCondition("productId",EntityOperator.EQUALS , productIdInput), null, null, null, false);
                if (UtilValidate.isEmpty(productTitleList)) {
                    productTitleList = delegator.findList("ProductMaster", EntityCondition.makeCondition("motherSku",EntityOperator.EQUALS , productIdInput), null, null, null, false);
                }
                for (GenericValue productTitle : productTitleList) {
                    productIdKeywordList.add(productTitle.getString("productId"));
                }
            } else {
                /*productTitleList = delegator.findList("ProductTitleList", null,
                                                      UtilMisc.toSet("productId"),
                                                      UtilMisc.toList("productId"),
                                                      new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
                */
                
                List<GenericValue> productKeywordSeedList = delegator.findList("ProductKeywordSeed", null,
                                                                               UtilMisc.toSet("productId"),
                                                                               UtilMisc.toList("productId"),
                                                                               new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
                for (GenericValue productKeywordSeed : productKeywordSeedList) {    //loop productKeywordSeedList == START
                    productIdKeywordList.add(productKeywordSeed.getString("productId"));
                }   //loop productKeywordSeedList == START
                
                List<GenericValue> productTitleRandomList = delegator.findList("ProductTitleRandom", null,
                                                                               UtilMisc.toSet("productId"),
                                                                               UtilMisc.toList("productId"),
                                                                               new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
                for (GenericValue productTitleRandom : productTitleRandomList) {    //loop productTitleRandomList == START
                    productIdKeywordList.remove(productTitleRandom.getString("productId"));
                }   //loop productTitleRandomList == END
            }
            //Debug.logError("Start the loop soon: " + productTitleList.size(), module);
            for (String productId : productIdKeywordList) {    //loop ProductTitleList == START
                //String productId = productTitle.getString("productId");
                Debug.logError("Processing title for " + productId, module);
                //List<String> productIdList = new ArrayList<String>();
                //check if should use motherSku or ChildSku == START
                /*List<GenericValue> mainList = delegator.findByAnd("ProductKeywordSeed", UtilMisc.toMap("productId", productIdCheck, "type", "MAIN", "statusId", "ACTIVE", "language", language), null, false);
                if (UtilValidate.isEmpty(mainList)) {   //if mainList empty == START
                    GenericValue product = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productIdCheck), false);
                    if (UtilValidate.isEmpty(product)) {
                        List<GenericValue> productList = delegator.findByAnd("ProductMaster", UtilMisc.toMap("motherSku", productIdCheck), null, false);
                        for (GenericValue productLoop : productList) {
                            productIdList.add(productLoop.getString("productId"));
                        }
                    } else {
                        productIdList.add(product.getString("motherSku"));
                    }
                }   //if mainList empty == END
                else {
                    productIdList.add(productIdCheck);
                }*/
                //productIdList.add(productIdCheck);
                //check if should use motherSku or ChildSku == END
                
                //run title generator == START
                //for (String productId : productIdList) {    //loop productIdList == START
                    for (GenericValue platformArray : platformArrayList) {    //loop platformArray == START
                        String platform = platformArray.getString("value").toUpperCase();
                        //generating random title X times == START
                        for (int i = 0; i <= titleQty; i++) {   //loop titleQty == START
                            Map gudaoTitleGenerator = dispatcher.runSync("gudaoTitleGenerator", UtilMisc.toMap("productId", productId, "platform", platform, "language", language, "userLogin", userLogin));
                            if (ServiceUtil.isSuccess(gudaoTitleGenerator)) {   //if gudaoTitleGenerator function success == START
                                String title = (String) gudaoTitleGenerator.get("title");
                                if (UtilValidate.isNotEmpty(title)) {   //if title is not empty == START
                                    GenericValue newTitleRandom = delegator.makeValue("ProductTitleRandom", UtilMisc.toMap("productId", productId,
                                                                                                                           "platform", platform,
                                                                                                                           "language", language,
                                                                                                                           "title", title));
                                    delegator.createOrStore(newTitleRandom);
                                }   //if title is not empty == END
                            }   //if gudaoTitleGenerator function success == END
                        }   //loop titleQty == END
                        //generating random title X times == END
                    }   //loop platformArray == END
                    List<GenericValue> removeProductTitleList = delegator.findByAnd("ProductTitleList", UtilMisc.toMap("productId", productId), null, false);
                    for (GenericValue removeProductTitle : removeProductTitleList) {    //loop removeProductTitleList == START
                        delegator.removeValue(removeProductTitle);
                        //Debug.logError("Done and removing from table: " + productId, module);
                    }   //loop removeProductTitleList == END
                //}   //loop productIdList == END
                //run title generator == END
            }   //loop ProductTitleList == END
        }   //main try == END
        catch (GenericEntityException e){
            Debug.logError(e.toString(), module);
        }
        catch (GenericServiceException e){
            Debug.logError(e.toString(), module);
        }
        return result;
    }   //gudaoTitleGeneratorBulk
    
    public static Map<String, Object> populatePipeline (DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException   {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String pipelineId = (String) context.get("pipelineId");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        try {   //main try == START
            GenericValue pipelineHeader = delegator.findOne("PipelineHeader", UtilMisc.toMap("pipelineId", pipelineId), false);
            if (UtilValidate.isEmpty(pipelineHeader)) {
                return ServiceUtil.returnError("pipelineId " + pipelineId + " does not exist");
            }
            String sourcer = pipelineHeader.getString("sourcer");
            String salesman = pipelineHeader.getString("salesman");
            String platform = pipelineHeader.getString("platform");
            boolean isAllSbu = false;
            if (sourcer.toUpperCase().equals("ALL")) {
                isAllSbu = true;
            }
            
            String salesmanPartyId = null;
            //get salesman partyGroup == START
            List<GenericValue> salesmanUserLoginList = delegator.findByAnd("UserLoginAndPartyDetails", UtilMisc.toMap("userLoginId", salesman), null, false);
            if (UtilValidate.isNotEmpty(salesmanUserLoginList)) {
                GenericValue salesmanUserLogin = EntityUtil.getFirst(salesmanUserLoginList);
                salesmanPartyId =  salesmanUserLogin.getString("partyId");
            }
            //get salesman partyGroup == END
            
            List<GenericValue> pipelineWeekList = delegator.findList("PipelineWeek", EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"), null, UtilMisc.toList("weekNumber"), null, false);
            for (GenericValue pipelineWeek : pipelineWeekList) {    //loop pipelineWeekList == START
                long weekNumber = pipelineWeek.getLong("weekNumber");
                Timestamp startDate = pipelineWeek.getTimestamp("startDate");
                Timestamp endDate = pipelineWeek.getTimestamp("endDate");
                
                //List<String> g0List = FastList.newInstance();
                //List<String> g4List = FastList.newInstance();
                List<String> pictureReadyList = FastList.newInstance();
                List<String> picList = FastList.newInstance();
                List<String> copyList = FastList.newInstance();
                List<String> listedList = FastList.newInstance();
                List<String> activeList = FastList.newInstance();
                
                List<EntityCondition> pmConditionList = FastList.newInstance();
                if (!isAllSbu) {
                    pmConditionList.add(EntityCondition.makeCondition("createdBy", EntityOperator.EQUALS, sourcer));
                }
                
                pmConditionList.add(EntityCondition.makeCondition("createdStamp", EntityOperator.GREATER_THAN_EQUAL_TO, startDate));
                pmConditionList.add(EntityCondition.makeCondition("createdStamp", EntityOperator.LESS_THAN_EQUAL_TO, endDate));
                EntityCondition condition = EntityCondition.makeCondition(pmConditionList, EntityOperator.AND);
                
                List<GenericValue> productMotherList = delegator.findList("ProductMaster", condition,
                                                                    UtilMisc.toSet("motherSku"),
                                                                    UtilMisc.toList("motherSku"),
                                                                    new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                    true);
                
                for (GenericValue productMother : productMotherList) {  //loop productMotherList == START
                    List<String> productIdList = FastList.newInstance();
                    String motherSku = productMother.getString("motherSku");
                    List<GenericValue> productList = delegator.findByAnd("ProductMaster", UtilMisc.toMap("motherSku", motherSku), null, false);
                    
                    //Description == START
                    boolean descriptionDone = false;
                    boolean pictureReady = false;
                    boolean pictureDone = false;
                    boolean isListed = false;
                    boolean isActive = false;
                    
                    GenericValue description = delegator.findOne("ProductDescription", UtilMisc.toMap("productId", motherSku, "language", "en", "type", "SPECIFICATION"), false);
                    if (UtilValidate.isNotEmpty(description)) {
                        descriptionDone = true;
                    }
                    //Description == END
                    
                    for (GenericValue product : productList) {  //loop productList == START
                        String productId = product.getString("productId");
                        String productGroup = product.getString("productGroup");
                        String pictureReadyString = product.getString("pictureReady");
                        
                        productIdList.add(productId);
                        
                        //PictureReady == START
                        if (UtilValidate.isNotEmpty(pictureReadyString)) {
                            if (pictureReadyString.equals("Y")) {
                                pictureReady = true;
                            }
                        }
                        //PictureReady == END
                        
                        //PictureDone == START
                        GenericValue ebayPictureGV = delegator.findOne("ProductListingStatus", UtilMisc.toMap("productId", productId, "listingStatusTypeId", "EBAY_PICTURE"), false);
                        if (UtilValidate.isNotEmpty(ebayPictureGV)) {
                            if (ebayPictureGV.getString("value").equals("Y")) {
                                pictureDone = true;
                            }
                        }
                        //PictureDone == END
                        
                        //Children Description == START
                        if (!descriptionDone) {
                            GenericValue childrenDescription = delegator.findOne("ProductDescription", UtilMisc.toMap("productId", productId, "language", "en", "type", "SPECIFICATION"), false);
                            if (UtilValidate.isNotEmpty(childrenDescription)) {
                                descriptionDone = true;
                            }
                        }
                        //Children Description == END
                    }   //loop productList == END
                    
                    //Listed == START
                    List<EntityCondition> listedConditionList = FastList.newInstance();
                    listedConditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "新品待上"));
                    listedConditionList.add(EntityCondition.makeCondition("department", EntityOperator.EQUALS, salesmanPartyId));
                    listedConditionList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, platform));
                    listedConditionList.add(EntityCondition.makeCondition("productId", EntityOperator.IN, productIdList));
                    EntityCondition listedCondition = EntityCondition.makeCondition(listedConditionList, EntityOperator.AND);
                    
                    List<GenericValue> listedListGV = delegator.findList("ProductMasterStatus", listedCondition,
                                                                       UtilMisc.toSet("productId"),
                                                                       UtilMisc.toList("productId"),
                                                                       new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                       true);
                    if (UtilValidate.isNotEmpty(listedListGV)) {
                        isListed = true;
                    }
                    //Listed == END
                    
                    //ACTIVE == START
                    List<EntityCondition> activeConditionList = FastList.newInstance();
                    activeConditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "新品待上"));
                    activeConditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_LIKE, "%下架"));
                    activeConditionList.add(EntityCondition.makeCondition("department", EntityOperator.EQUALS, salesmanPartyId));
                    listedConditionList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, platform));
                    activeConditionList.add(EntityCondition.makeCondition("productId", EntityOperator.IN, productIdList));
                    EntityCondition activeCondition = EntityCondition.makeCondition(activeConditionList, EntityOperator.AND);
                    
                    List<GenericValue> activeListGV = delegator.findList("ProductMasterStatus", activeCondition,
                                                                       UtilMisc.toSet("productId"),
                                                                       UtilMisc.toList("productId"),
                                                                       new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                       true);
                    if (UtilValidate.isNotEmpty(activeListGV)) {
                        isActive = true;
                    }
                    //ACTIVE == END
                    
                    if (descriptionDone) {
                        copyList.add(motherSku);
                    }
                    
                    if (pictureReady) {
                        pictureReadyList.add(motherSku);
                    }
                    
                    if (pictureDone) {
                        picList.add(motherSku);
                    }
                    
                    if (isListed) {
                        listedList.add(motherSku);
                    }
                    
                    if (isActive) {
                        activeList.add(motherSku);
                    }
                }   //loop productMotherList == END
                
                //update pipelineItem == START
                GenericValue pipelineItem = delegator.makeValue("PipelineItem", UtilMisc.toMap("pipelineId", pipelineId, "weekNumber", weekNumber));
                pipelineItem.set("productSourced", Double.valueOf(productMotherList.size()));
                pipelineItem.set("pictureReady", Double.valueOf(pictureReadyList.size()));
                pipelineItem.set("pictureDone", Double.valueOf(picList.size()));
                pipelineItem.set("copyDone", Double.valueOf(copyList.size()));
                pipelineItem.set("listed", Double.valueOf(listedList.size()));
                pipelineItem.set("active", Double.valueOf(activeList.size()));
                //pipelineItem.set("group0Qty", Double.valueOf(g0List.size()));
                //pipelineItem.set("group0Stock", Double.valueOf());
                //pipelineItem.set("group4Qty", Double.valueOf(g4List.size()));
                //pipelineItem.set("sales", Double.valueOf());
                //pipelineItem.set("grossProfit", Double.valueOf());
                //pipelineItem.set("grossProfitPct", Double.valueOf());
                delegator.createOrStore(pipelineItem);
                //update pipelineItem == END
                
            }   //loop pipelineWeek == END
        }   //main try == END
        catch (GenericEntityException e){
            Debug.logError(e.toString(), module);
        }
        
        return result;
        
    }   //populatePipeline
    
    public static Map<String, Object> populatePipelineAuto (DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException, GenericServiceException {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        try {   //main try == START
            List<GenericValue> pipelineHeaderList = delegator.findByAnd("PipelineHeader", UtilMisc.toMap("statusId", "ACTIVE"), null, false);
            for (GenericValue pipelineHeader : pipelineHeaderList) {
                String pipelineId = pipelineHeader.getString("pipelineId");
                Map populatePipeline = dispatcher.runSync("gudaoPopulatePipeline", UtilMisc.toMap("pipelineId", pipelineId, "userLogin", userLogin));
            }
        }   //main try == END
        catch (GenericEntityException e){
            Debug.logError(e.toString(), module);
        }
        catch (GenericServiceException e){
            Debug.logError(e.toString(), module);
        }
        
        return result;
        
    }   //populatePipelineAuto
    
    public static Map<String, Object> gudaoDeleteProduct(DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productId = (String) context.get("productId");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        if (UtilValidate.isNotEmpty(productId)) {
            productId = productId.trim();
        }
        
        try {   //main try == START
            GenericValue productMaster = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
            delegator.removeValue(productMaster);
            
            List<GenericValue> productMasterPriceList = delegator.findByAnd("ProductMasterPrice", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productMasterPrice : productMasterPriceList) {    //loop remove productMasterPriceList == START
                delegator.removeValue(productMasterPrice);
            }   //loop remove productMasterPriceList == END
            
            List<GenericValue> productMasterRivalList = delegator.findByAnd("ProductMasterRival", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productMasterRival : productMasterRivalList) {    //loop remove productMasterRivalList == START
                delegator.removeValue(productMasterRival);
            }   //loop remove productMasterRivalList == END
            
            List<GenericValue> productMasterStatusList = delegator.findByAnd("ProductMasterStatus", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productMasterStatus : productMasterStatusList) {    //loop remove productMasterStatusList == START
                delegator.removeValue(productMasterStatus);
            }   //loop remove productMasterStatusList == END
            
            List<GenericValue> productMasterGroupList = delegator.findByAnd("ProductMasterGroup", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productMasterGroup : productMasterGroupList) {    //loop remove productMasterGroupList == START
                delegator.removeValue(productMasterGroup);
            }   //loop remove productMasterGroupList == END
            
            List<GenericValue> productGpsList = delegator.findByAnd("ProductGps", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productGps : productGpsList) {    //loop remove productGpsList == START
                delegator.removeValue(productGps);
            }   //loop remove productGpsList == END
            
            List<GenericValue> productGmsList = delegator.findByAnd("ProductGms", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productGms : productGmsList) {    //loop remove productGmsList == START
                delegator.removeValue(productGms);
            }   //loop remove productGmsList == END
            
            List<GenericValue> productGmsDetailList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productGmsDetail : productGmsDetailList) {    //loop remove productGmsDetailList == START
                delegator.removeValue(productGmsDetail);
            }   //loop remove productGmsDetailList == END
            
            List<GenericValue> productGmsDetailHistoryList = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productGmsDetailHistory : productGmsDetailHistoryList) {    //loop remove productGmsDetailHistoryList == START
                delegator.removeValue(productGmsDetailHistory);
            }   //loop remove productGmsDetailHistoryList == END
            
            List<GenericValue> productGqsList = delegator.findByAnd("ProductGqs", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productGqs : productGqsList) {    //loop remove productGqsList == START
                delegator.removeValue(productGqs);
            }   //loop remove productGqsList == END
            
            List<GenericValue> productEpsList = delegator.findByAnd("ProductEps", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productEps : productEpsList) {    //loop remove productEpsList == START
                delegator.removeValue(productEps);
            }   //loop remove productEpsList == END
            
            List<GenericValue> productKeywordSeedList = delegator.findByAnd("ProductKeywordSeed", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productKeywordSeed : productKeywordSeedList) {    //loop remove productKeywordSeedList == START
                delegator.removeValue(productKeywordSeed);
            }   //loop remove productKeywordSeedList == END
            
            List<GenericValue> productDescriptionList = delegator.findByAnd("ProductDescription", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productDescription : productDescriptionList) {    //loop remove productDescriptionList == START
                delegator.removeValue(productDescription);
            }   //loop remove productDescriptionList == END
            
            List<GenericValue> productTitleRandomList = delegator.findByAnd("ProductTitleRandom", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productTitleRandom : productTitleRandomList) {    //loop remove productTitleRandomList == START
                delegator.removeValue(productTitleRandom);
            }   //loop remove productTitleRandomList == END
            
            List<GenericValue> productListingStatusList = delegator.findByAnd("ProductListingStatus", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue productListingStatus : productListingStatusList) {    //loop remove productListingStatusList == START
                delegator.removeValue(productListingStatus);
            }   //loop remove productListingStatusList == END
            
            List<GenericValue> supplierPriceList = delegator.findByAnd("SupplierPrice", UtilMisc.toMap("productId", productId), null, false);
            for (GenericValue supplierPrice : supplierPriceList) {    //loop remove supplierPriceList == START
                delegator.removeValue(supplierPrice);
            }   //loop remove supplierPriceList == END
        }   //main try == END
        catch (GenericEntityException e){
            Debug.logError(e.toString(), module);
        }
        return result;
    }   //gudaoDeleteProduct
    
    public static Map<String, Object> gudaoDeleteProductAuto(DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException, GenericServiceException {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        try {   //main try == START
            List<GenericValue> productMasterList = delegator.findByAnd("ProductMaster", UtilMisc.toMap("statusId", "DELETE"), null, false);
            for (GenericValue pm : productMasterList) {
                String productId = pm.getString("productId");
                Map gudaoDeleteProduct = dispatcher.runSync("gudaoDeleteProduct", UtilMisc.toMap("productId", productId, "userLogin", userLogin));
                if (ServiceUtil.isError(gudaoDeleteProduct)) {
                    return ServiceUtil.returnError("Error in deleting product " + productId);
                }
            }
        }   //main try == END
        catch (GenericEntityException e){
            Debug.logError(e.toString(), module);
        }
        catch (GenericServiceException e){
            Debug.logError(e.toString(), module);
        }
        return result;
    }   //gudaoDeleteProductAuto
    
    public static Map<String, Object> syncProductMaster(DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericServiceException   {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productId = (String) context.get("productId");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        try {   //main try == START
            Map gudaoRefreshProductMaster = dispatcher.runSync("gudaoRefreshProductMaster", UtilMisc.toMap("productId", productId, "userLogin", userLogin));
            if (ServiceUtil.isError(gudaoRefreshProductMaster)) {
                return ServiceUtil.returnError("Error in gudaoRefreshProductMaster product " + productId);
            }
            
            Map updateProductEps = dispatcher.runSync("updateProductEps", UtilMisc.toMap("productIdInput", productId, "userLogin", userLogin));
            if (ServiceUtil.isError(updateProductEps)) {
                return ServiceUtil.returnError("Error in updateProductEps product " + productId);
            }
        }   //main try == END
        catch (GenericServiceException e){
            Debug.logError(e.toString(), module);
        }
        
        return result;
        
    }   //syncProductMaster
    
    public static Map<String, Object> gudaoCheckPlatformGroup (DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException   {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productId = (String) context.get("productId");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        try {   //main try == START
            List<EntityCondition> pmConditionList = FastList.newInstance();
            pmConditionList.add(EntityCondition.makeCondition("productGroup", EntityOperator.NOT_EQUAL, "G3"));
            pmConditionList.add(EntityCondition.makeCondition("productGroup", EntityOperator.NOT_EQUAL, "G4"));
            pmConditionList.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
            EntityCondition condition = EntityCondition.makeCondition(pmConditionList, EntityOperator.AND);
            
            List<GenericValue> productMasterGroupList = delegator.findList("ProductMasterGroup", condition, null, null, null, false);
            boolean hasG1Group = false;
            if (UtilValidate.isNotEmpty(productMasterGroupList)) { //if productMasterGroupList is not empty == START
                for (GenericValue productMasterGroup : productMasterGroupList) {  //loop productMasterGroupList == START
                    String platformProductGroup = productMasterGroup.getString("productGroup");
                    if (platformProductGroup.equals("G1")) {
                        hasG1Group = true;
                    }
                }   //loop productMasterGroupList == END
                
                if (hasG1Group) {  //if there is at least one G1 Group == START
                    for (GenericValue productMasterGroup : productMasterGroupList) {    //loop productMasterGroupList == START
                        String platformProductGroup = productMasterGroup.getString("productGroup");
                        if (platformProductGroup.equals("G0")) {
                            productMasterGroup.set("productGroup", "G2");
                            delegator.store(productMasterGroup);
                        }
                    }   //loop productMasterGroupList == END
                }   //if there is at least one G1 Group == END
                else {  //if there is no G1 Group == START
                    for (GenericValue productMasterGroup : productMasterGroupList) {    //loop productMasterGroupList == START
                        String platformProductGroup = productMasterGroup.getString("productGroup");
                        if (platformProductGroup.equals("G2")) {
                            productMasterGroup.set("productGroup", "G0");
                            delegator.store(productMasterGroup);
                        }
                    }   //loop productMasterGroupList == END
                }   //if there is no G1 Group == END
            }   //if productMasterGroupList is not empty == END
        }   //main try == END
        catch (GenericEntityException e){
            Debug.logError(e.toString(), module);
        }
        
        return result;
        
    }   //gudaoCheckPlatformGroup
    
    public static Map<String, Object> calculatePlatformProductGroup (DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String motherSkuInput = (String) context.get("motherSku");
        String departmentInput = (String) context.get("department");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        
        Calendar threeDaysAgoCal = Calendar.getInstance();
        threeDaysAgoCal.set(Calendar.DATE, threeDaysAgoCal.get(Calendar.DATE) - 7);
        Timestamp threeDaysAgoTS = Timestamp.valueOf(sdf.format(threeDaysAgoCal.getTime()));
        Debug.logError("start calculatePlatformProductGroupG1", module);
        try {   //main try == START
            List<GenericValue> platformList = delegator.findByAnd("RoleType", UtilMisc.toMap("parentTypeId", "PLATFORM"), null, false);
            
            List<EntityCondition> platformGroupConditionList = FastList.newInstance();
            if (UtilValidate.isNotEmpty(departmentInput)) { //if departmentInput is not empty == START
                departmentInput = departmentInput.trim();
                platformGroupConditionList.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, departmentInput));
            }   //if departmentInput is not empty == END
            platformGroupConditionList.add(EntityCondition.makeCondition("partyGroupComments", EntityOperator.EQUALS, "ACTIVE"));
            platformGroupConditionList.add(EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "PLATFORM"));
            EntityCondition platformGroupCondition = EntityCondition.makeCondition(platformGroupConditionList, EntityOperator.AND);
            
            List<GenericValue> platformGroupList = delegator.findList("PartyRoleDetailAndPartyDetail", platformGroupCondition, null, null, null,false);
            
            List<EntityCondition> pmConditionList = FastList.newInstance();
            if (UtilValidate.isNotEmpty(motherSkuInput)) {
                motherSkuInput = motherSkuInput.trim();
                pmConditionList.add(EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, motherSkuInput));
            }
            pmConditionList.add(EntityCondition.makeCondition("productGroup", EntityOperator.NOT_EQUAL, "GL"));
            EntityCondition pmCond = EntityCondition.makeCondition(pmConditionList, EntityOperator.AND);
            
            List<String> distinctMotherSkuList = FastList.newInstance();
            List<GenericValue> distinctMotherSkuListGV = delegator.findList("ProductMasterGroupAndPm", pmCond,
                                                                            UtilMisc.toSet("motherSku"),
                                                                            UtilMisc.toList("motherSku"),
                                                                            new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
            Debug.logError("distinctMotherSkuListGV size: " + distinctMotherSkuListGV.size(), module);
            for (GenericValue distinctMotherSkuGV : distinctMotherSkuListGV) {  //loop distinctMotherSkuListGV == START
                if (UtilValidate.isNotEmpty(distinctMotherSkuGV.getString("motherSku"))) {  //if motherSku is not empty == START
                    String motherSku = distinctMotherSkuGV.getString("motherSku");
                    distinctMotherSkuList.add(motherSku);
                    
                    List<String> childrenSkuList = FastList.newInstance();
                    List<GenericValue> childrenSkuListGV = delegator.findByAnd("ProductMaster", UtilMisc.toMap("motherSku", motherSku), null, false);
                    for (GenericValue childrenSkuGV : childrenSkuListGV) {  //loop childrenSkuListGV == START
                        childrenSkuList.add(childrenSkuGV.getString("productId"));
                    }   //loop childrenSkuListGV == END
                    
                    for (GenericValue platformGroupGV : platformGroupList) {  //loop platformGroupList == START
                        String department = platformGroupGV.getString("partyId");
                        boolean noG0product = true;
                        
                        List<GenericValue> currentProductMasterGroupList = delegator.findList("ProductMasterGroup",
                                                                                              EntityCondition.makeCondition(
                                                                                              UtilMisc.toList(
                                                                                                              EntityCondition.makeCondition("productId", EntityOperator.IN, childrenSkuList),
                                                                                                              EntityCondition.makeCondition("department", EntityOperator.EQUALS, department)
                                                                                              )),
                                                                                              null, null, null, false);
                        for (GenericValue currentProductMasterGroup : currentProductMasterGroupList) {  //loop currentProductMasterGroupList == START
                            if (currentProductMasterGroup.getString("productGroup").equals("G0")) {
                                noG0product = false;
                            }
                        }   //currentProductMasterGroupList == END
                        
                        if (noG0product) {   //if there is no G0 == START
                            for (GenericValue platformGV : platformList) {    //loop platformList == START
                                String platform = platformGV.getString("roleTypeId");
                                EntityCondition sriConditionList = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                                 EntityCondition.makeCondition("platform", EntityOperator.EQUALS, platform),
                                                                                                                 EntityCondition.makeCondition("productId", EntityOperator.IN, childrenSkuList),
                                                                                                                 EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, threeDaysAgoTS)
                                                                                                                 ));
                                List<GenericValue> checkSkuSoldOrNot = delegator.findList("SalesReportImport", sriConditionList, null, null, null, false);
                                if (UtilValidate.isNotEmpty(checkSkuSoldOrNot)) {   //change to G1 == START
                                    List<GenericValue> changeProductMasterGroupList = delegator.findList("ProductMasterGroup",
                                                                                                          EntityCondition.makeCondition(
                                                                                                                                        UtilMisc.toList(
                                                                                                                                                        EntityCondition.makeCondition("productId", EntityOperator.IN, childrenSkuList),
                                                                                                                                                        EntityCondition.makeCondition("department", EntityOperator.EQUALS, department),
                                                                                                                                                        EntityCondition.makeCondition("platform", EntityOperator.EQUALS, platform)
                                                                                                                                                        )),
                                                                                                          null, null, null, false);
                                    for (GenericValue changeGroup : changeProductMasterGroupList) { //loop changeProductMasterGroupList == START
                                        changeGroup.set("productGroup", "G1");
                                        delegator.store(changeGroup);
                                        //Debug.logError("productId: " + changeGroup.getString("productId") + "; platform: " + changeGroup.getString("platform") + "; department: " + changeGroup.getString("department"), module);
                                        
                                        //update ProductMasterGroupHistory
                                        Date nowDate = new Date();
                                        GenericValue productMasterGroupHistory = delegator.makeValue("ProductMasterGroupHistory", UtilMisc.toMap("productId", changeGroup.getString("productId")));
                                        productMasterGroupHistory.set("platform", changeGroup.getString("platform"));
                                        productMasterGroupHistory.set("department", changeGroup.getString("department"));
                                        productMasterGroupHistory.set("date", new java.sql.Date(nowDate.getTime()));
                                        productMasterGroupHistory.set("productGroup", "G1");
                                        delegator.create(productMasterGroupHistory);
                                        
                                    }   //loop changeProductMasterGroupList == END
                                }   //change to G1 == END
                            }   //loop platformList == END
                        }   //if there is no G0 == END
                    }   //platformGroupList == END
                }   //if motherSku is not empty == END
            }   //loop distinctMotherSkuListGV == END
        }   //main try == END
        catch (GenericEntityException e){
            Debug.logError(e.toString(), module);
        }
        Debug.logError("finished calculatePlatformProductGroupG1", module);
        return result;
        
    }   //calculatePlatformProductGroup
    
    public static Map<String, Object> calculateProductGroup (DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String motherSkuInput = (String) context.get("motherSku");
        String departmentInput = (String) context.get("department");
        String lastXDaysInput = (String) context.get("lastXDays");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        if (UtilValidate.isEmpty(lastXDaysInput)) {
            lastXDaysInput = "3";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        
        Calendar lastXDaysAgoCal = Calendar.getInstance();
        lastXDaysAgoCal.set(Calendar.DATE, lastXDaysAgoCal.get(Calendar.DATE) - Integer.parseInt(lastXDaysInput.trim()));
        Timestamp lastXDaysAgoTS = Timestamp.valueOf(sdf.format(lastXDaysAgoCal.getTime()));
        Debug.logError("start calculateProductGroup", module);
        
        if (UtilValidate.isNotEmpty(departmentInput)) {
            departmentInput = departmentInput.trim().toUpperCase();
            if (!departmentInput.contains("GROUP")) {
                departmentInput = departmentInput + "_GROUP";
            }
        }
        
        try {   //main try == START
            List<EntityCondition> sriConditionList = FastList.newInstance();
            if (UtilValidate.isNotEmpty(departmentInput)) { //if departmentInput is not empty == START
                sriConditionList.add(EntityCondition.makeCondition("platformPartyId", EntityOperator.EQUALS, departmentInput));
            }   //if departmentInput is not empty == END
            
            if (UtilValidate.isNotEmpty(motherSkuInput)) {    //if motherSkuInput is not empty == START
                motherSkuInput = motherSkuInput.trim();
                List<String> childrenSkuList = FastList.newInstance();
                List<GenericValue> childrenSkuListGV = delegator.findByAnd("ProductMaster", UtilMisc.toMap("motherSku", motherSkuInput), null, false);
                for (GenericValue childrenSkuGV : childrenSkuListGV) {  //loop childrenSkuListGV == START
                    childrenSkuList.add(childrenSkuGV.getString("productId"));
                }   //loop childrenSkuListGV == END
                sriConditionList.add(EntityCondition.makeCondition("productId", EntityOperator.IN, childrenSkuList));
            }   //if motherSkuInput is not empty == START
            
            sriConditionList.add(EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, lastXDaysAgoTS));
            EntityCondition sriCondition = EntityCondition.makeCondition(sriConditionList, EntityOperator.AND);
            
            List<GenericValue> salesReportImportList = delegator.findList("SalesDataPmGroup", sriCondition, null, null, null, false);
            Debug.logError("SRI size: " + salesReportImportList.size(), module);
            
            for (GenericValue salesReportImport : salesReportImportList) {  //loop salesReportImportList == START
                String productId = salesReportImport.getString("productId");
                String department = salesReportImport.getString("platformPartyId");
                String platform = salesReportImport.getString("platform");
                boolean noG0product = true;
                
                List<String> childrenSkuList = FastList.newInstance();
                GenericValue productGV = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
                if (UtilValidate.isNotEmpty(productGV)) {   //if product definitely exist in PM == START
                    String motherSku = productGV.getString("motherSku");
                    List<GenericValue> childrenSkuListGV = delegator.findByAnd("ProductMaster", UtilMisc.toMap("motherSku", motherSku), null, false);
                    for (GenericValue childrenSkuGV : childrenSkuListGV) {  //loop childrenSkuListGV == START
                        childrenSkuList.add(childrenSkuGV.getString("productId"));
                    }   //loop childrenSkuListGV == END
                    
                    List<GenericValue> currentProductMasterGroupList = delegator.findList("ProductMasterGroup",
                                                                                          EntityCondition.makeCondition(
                                                                                                                        UtilMisc.toList(
                                                                                                                                        EntityCondition.makeCondition("productId", EntityOperator.IN, childrenSkuList),
                                                                                                                                        EntityCondition.makeCondition("department", EntityOperator.EQUALS, department)
                                                                                                                                        )),
                                                                                          null, null, null, false);
                    for (GenericValue currentProductMasterGroup : currentProductMasterGroupList) {  //loop currentProductMasterGroupList == START
                        if (currentProductMasterGroup.getString("productGroup").equals("G0")) {
                            noG0product = false;
                        }
                    }   //loop currentProductMasterGroupList == END
                    
                    if (noG0product) {  //no G0 product == START
                        for (String childrenSku : childrenSkuList) {    //loop childrenSkuList == START
                            GenericValue productMasterGroup = delegator.makeValue("ProductMasterGroup", UtilMisc.toMap("productId", childrenSku));
                            productMasterGroup.set("department", department);
                            productMasterGroup.set("platform", platform);
                            productMasterGroup.set("productGroup", "G1");
                            delegator.createOrStore(productMasterGroup);
                            
                            //update ProductMasterGroupHistory
                            Date nowDate = new Date();
                            GenericValue productMasterGroupHistory = delegator.makeValue("ProductMasterGroupHistory", UtilMisc.toMap("productId", childrenSku));
                            productMasterGroupHistory.set("platform", platform);
                            productMasterGroupHistory.set("department", department);
                            productMasterGroupHistory.set("date", new java.sql.Date(nowDate.getTime()));
                            productMasterGroupHistory.set("productGroup", "G1");
                            delegator.createOrStore(productMasterGroupHistory);
                        }   //loop childrenSkuList == END
                    }   //no G0 product == END
                }   //if product definitely exist in PM == END
            }   //loop salesReportImportList == END
        }   //main try == END
        catch (GenericEntityException e){
            Debug.logError(e.toString(), module);
        }

        /*try {   //main try == START
            List<EntityCondition> sriConditionList = FastList.newInstance();
            if (UtilValidate.isNotEmpty(departmentInput)) { //if departmentInput is not empty == START
                sriConditionList.add(EntityCondition.makeCondition("platformPartyId", EntityOperator.EQUALS, departmentInput));
            }   //if departmentInput is not empty == END
            
            if (UtilValidate.isNotEmpty(motherSkuInput)) {    //if motherSkuInput is not empty == START
                motherSkuInput = motherSkuInput.trim();
                List<String> childrenSkuList = FastList.newInstance();
                List<GenericValue> childrenSkuListGV = delegator.findByAnd("ProductMaster", UtilMisc.toMap("motherSku", motherSkuInput), null, false);
                for (GenericValue childrenSkuGV : childrenSkuListGV) {  //loop childrenSkuListGV == START
                    childrenSkuList.add(childrenSkuGV.getString("productId"));
                }   //loop childrenSkuListGV == END
                sriConditionList.add(EntityCondition.makeCondition("productId", EntityOperator.IN, childrenSkuList));
            }   //if motherSkuInput is not empty == START
            
            sriConditionList.add(EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, threeDaysAgoTS));
            EntityCondition sriCondition = EntityCondition.makeCondition(sriConditionList, EntityOperator.AND);

            List<GenericValue> salesReportImportList = delegator.findList("SalesReportImport", sriCondition, null, null, null, false);
            Debug.logError("SRI size: " + salesReportImportList.size(), module);
            
            for (GenericValue salesReportImport : salesReportImportList) {  //loop salesReportImportList == START
                String productId = salesReportImport.getString("productId");
                String department = salesReportImport.getString("platformPartyId");
                //DEL THIS
                if (UtilValidate.isEmpty(department)) {
                    department = "EMPTYDEPARTMENT";
                }
                //DEL THIS
                String platform = salesReportImport.getString("platform");
                boolean noG0product = true;
                
                List<String> childrenSkuList = FastList.newInstance();
                GenericValue productGV = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
                if (UtilValidate.isNotEmpty(productGV)) {   //if product definitely exist in PM == START
                    String motherSku = productGV.getString("motherSku");
                    List<GenericValue> childrenSkuListGV = delegator.findByAnd("ProductMaster", UtilMisc.toMap("motherSku", motherSku), null, false);
                    for (GenericValue childrenSkuGV : childrenSkuListGV) {  //loop childrenSkuListGV == START
                        childrenSkuList.add(childrenSkuGV.getString("productId"));
                    }   //loop childrenSkuListGV == END
                    
                    List<GenericValue> currentProductMasterGroupList = delegator.findList("ProductMasterGroup",
                                                                                          EntityCondition.makeCondition(
                                                                                                                        UtilMisc.toList(
                                                                                                                                        EntityCondition.makeCondition("productId", EntityOperator.IN, childrenSkuList),
                                                                                                                                        EntityCondition.makeCondition("department", EntityOperator.EQUALS, department)
                                                                                                                                        )),
                                                                                          null, null, null, false);
                    for (GenericValue currentProductMasterGroup : currentProductMasterGroupList) {  //loop currentProductMasterGroupList == START
                        if (currentProductMasterGroup.getString("productGroup").equals("G0")) {
                            noG0product = false;
                        }
                    }   //currentProductMasterGroupList == END
                    
                    if (noG0product) {  //no G0 product == START
                        for (String childrenSku : childrenSkuList) {    //loop childrenSkuList == START
                            GenericValue productMasterGroup = delegator.makeValue("ProductMasterGroup", UtilMisc.toMap("productId", childrenSku));
                            productMasterGroup.set("department", department);
                            productMasterGroup.set("platform", platform);
                            productMasterGroup.set("productGroup", "G1");
                            delegator.createOrStore(productMasterGroup);
                            
                            //update ProductMasterGroupHistory
                            Date nowDate = new Date();
                            GenericValue productMasterGroupHistory = delegator.makeValue("ProductMasterGroupHistory", UtilMisc.toMap("productId", childrenSku));
                            productMasterGroupHistory.set("platform", platform);
                            productMasterGroupHistory.set("department", department);
                            productMasterGroupHistory.set("date", new java.sql.Date(nowDate.getTime()));
                            productMasterGroupHistory.set("productGroup", "G1");
                            delegator.createOrStore(productMasterGroupHistory);
                        }   //loop childrenSkuList == END
                    }   //no G0 product == END
                }   //if product definitely exist in PM == END
            }   //loop salesReportImportList == END
        }   //main try == END
        catch (GenericEntityException e){
            Debug.logError(e.toString(), module);
        }*/
        Debug.logError("finished calculateProductGroup", module);
        return result;
        
    }   //calculateProductGroup
    
}	//END class