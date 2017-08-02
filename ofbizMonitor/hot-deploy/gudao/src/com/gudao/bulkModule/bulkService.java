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
package com.gudao.bulkModule;

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
import org.apache.commons.lang.StringEscapeUtils;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.bellyanna.ebay.common;
import com.bellyanna.ebay.eBayTradingAPI;


import com.csvreader.CsvReader;


public class bulkService {
	private static final String module = bulkService.class.getName();
    private static final String eol = System.getProperty("line.separator");
    
    public static boolean showPmDebug ()
    throws IOException {
        
        boolean result = false;
        
        /*try {   //main try -- START
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
        */
        
        return result;
    }
    
    public static Map<String, String> pmDebug () {
        
        Map<String, String> mapContent = FastMap.newInstance();
        /*try {   //main try -- START
            Properties properties = new Properties();
            properties.load(new BufferedReader(new InputStreamReader(new FileInputStream("hot-deploy/gudao/config/debug.properties"),
                                                                     "UTF8")));
            Enumeration e = properties.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                mapContent.put(key, properties.getProperty(key).toString());
            }
            mapContent.put("showDebugLog", "N");
            mapContent.put("refreshPmWriteToFile", "N");
            mapContent.put("refreshPmWriteToFileDetail", "N");
        }   //main try -- END
        catch (IOException e) {
            e.printStackTrace();
        }*/
        mapContent.put("showDebugLog", "N");
        mapContent.put("refreshPmWriteToFile", "N");
        mapContent.put("refreshPmWriteToFileDetail", "N");
        return mapContent;
    }
    
    public static Map<String, String> pmDefaultValue ()
    throws IOException {
        
        Map<String, String> mapContent = FastMap.newInstance();
        try {   //main try -- START
            Properties properties = new Properties();
            properties.load(new BufferedReader(new InputStreamReader(new FileInputStream("hot-deploy/gudao/config/pm.properties"),
                                                     "UTF8")));
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


	
    public static Map<String, Object> uploadProductXlsFile(DispatchContext dctx, Map<String, ? extends Object> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
        String uploadFileName = (String) context.get("_uploadedFile_fileName");
        uploadFileName = uploadFileName.replaceAll(" ","");
        String importFormId = (String) context.get("importFormId");
        String updateFormId = (String) context.get("updateFormId");
        
        if (UtilValidate.isNotEmpty(uploadFileName) && UtilValidate.isNotEmpty(imageData)) {
            try {   //main try -- START
                boolean isLimited = false;
                boolean isAdmin = false;
                List<GenericValue> userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLogin.getString("userLoginId"), "groupId", "GUDAO-ADMIN"), null, false);
                for (GenericValue userAdmin : userLoginAdmin) {
                    if (userAdmin.getString("userLoginId").equals(userLogin.getString("userLoginId"))) {
                        isAdmin = true;
                    }
                }
                
                List<GenericValue> limitedAccessList = delegator.findByAnd("GudaoLimitedAccess", UtilMisc.toMap("statusId", "ACTIVE", "type", "CREATE_SKU"), null, false);
                for (GenericValue limitedAccess : limitedAccessList) {
                    if (limitedAccess.getString("userLoginId").equals(userLogin.getString("userLoginId"))) {
                        isLimited = true;
                    }
                }
                
                String fileServerPath = "hot-deploy/gudao/webapp/gudao/bulkModule/upload";
                File rootTargetDir = new File(fileServerPath);
                if (!rootTargetDir.exists()) {
                    boolean created = rootTargetDir.mkdirs();
                    if (!created) {
                        String errMsg = "Not create target directory";
                        Debug.logFatal(errMsg, module);
                        return ServiceUtil.returnError(errMsg);
                    }
                }
                String fileName = null;
                if (importFormId != null) {
                    fileName = "IMPORT-" + importFormId + "-" + new SimpleDateFormat("yyMMdd-HHmm").format(Calendar.getInstance().getTime()) + "-" + userLogin.getString("userLoginId") + "-" + uploadFileName;
                }
                if (updateFormId != null) {
                    fileName = "UPDATE-" + updateFormId + "-" + new SimpleDateFormat("yyMMdd-HHmm").format(Calendar.getInstance().getTime()) + "-" + userLogin.getString("userLoginId") + "-" + uploadFileName;
                }
                
                String filePath = fileServerPath + "/" + fileName;
                File file = new File(filePath);
                try {
                    RandomAccessFile out = new RandomAccessFile(file, "rw");
                    out.write(imageData.array());
                    out.close();
                } catch (FileNotFoundException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError("");
                } catch (IOException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError("");
                }
                if (file.exists()) {    //if file exist -- START
                    Map result = null;
                    if (importFormId != null && importFormId.equals("PRODUCT")) {
                        if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                            boolean isSbu = false;
                            String partyId = userLogin.getString("partyId");
                            List<GenericValue> partyRoleList = delegator.findByAnd("PartyRole", UtilMisc.toMap("partyId", partyId, "roleTypeId", "SBU"), null, false);
                            if (UtilValidate.isNotEmpty(partyRoleList)) {
                                isSbu = true;
                            }
                            
                            if (isSbu || isAdmin) { //check if belongs to ownerGroup == START
                                if (!isLimited || isAdmin) {   //if user is limited from creating SKU == START
                                    result = readProductXls(dispatcher, delegator, userLogin, filePath, uploadFileName);
                                    return result;
                                }   //if user is limited from creating SKU == END
                                else {
                                    return ServiceUtil.returnError("Users are limited - 建新SKU有限");
                                }
                            }   //check if belongs to ownerGroup == START
                            else {  //check if belongs to ownerGroup == END
                                return ServiceUtil.returnError("NO permission to create SKU, You do not belong to SBU");
                            }   //check if belongs to ownerGroup == END
                        }
                        /*else if (fileName.endsWith(".csv")) {
                            result = readProductCsv(dispatcher, delegator, userLogin, filePath);
                        }*/
                        
                    } else if (updateFormId != null && updateFormId.equals("PRODUCT")) {
                        result = readProductUpdateXls(dispatcher, delegator, userLogin, filePath, uploadFileName);
                    }
                }   //if file exist -- END
                
            } catch (Exception e) {
                return ServiceUtil.returnError(e.getMessage());
            }   //main try -- END
        }
        return ServiceUtil.returnSuccess();
    }   //uploadProductXlsFile
    
    private static Map<String, Object> readProductXls(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String filePath, String originalFilename) {   //readProductXls
        Map<String, Object> result = ServiceUtil.returnSuccess();
        try {   //main try -- START
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            java.sql.Date nowTimestamp = new java.sql.Date(now.getTimeInMillis());
            GenericValue partyGroup = userLogin.getRelatedOne("PartyGroup", false);
            String userLoginId = userLogin.getString("userLoginId");
            String ownerGroup = partyGroup.getString("officeSiteName");
            POIFSFileSystem fs = null;
            HSSFWorkbook wb = null;
            try {
                fs = new POIFSFileSystem(new FileInputStream(new File(filePath)));
                
                //filePath.endsWith(".xlsx")
                wb = new HSSFWorkbook(fs);
            } catch (IOException e) {
                Debug.logError("Unable to read workbook from file " + filePath, module);
                return ServiceUtil.returnError("Unable to read workbook from file " + filePath);
            }
            
            HSSFSheet sheet = wb.getSheetAt(0);
            int sheetLastRowNumber = sheet.getLastRowNum();
            HSSFRow firstRow = sheet.getRow(0);
            int minColIx = firstRow.getFirstCellNum();
            int maxColIx = firstRow.getLastCellNum();
            int colProductId = 0;
            int colCustomSku = 0;
            int colMotherSku = 0;
            int colChineseName = 0;
            int colEnglishName = 0;
            int colDeclaredNameCn = 0;
            int colDeclaredNameEn = 0;
            int colWeight = 0;
            int colStatusId = 0;
            int colCategoryIdParent = 0;
            int colCategoryIdChild = 0;
            int colProductType = 0;
            int colImageLink = 0;
            int colSourcingDate = 0;
            int colUsd = 0;
            int colSupplier = 0;
            int colSupplierType = 0;
            int colPrice = 0;
            int colSupplierLink = 0;
            int colEta = 0;
            int colMinimumOrderIdentifierA = 0;
            int colMinimumOrderQtyA = 0;
            int colMinimumOrderPriceA = 0;
            int colMinimumOrderUnitPriceA = 0;
            int colMinimumOrderIdentifierB = 0;
            int colMinimumOrderQtyB = 0;
            int colMinimumOrderPriceB = 0;
            int colMinimumOrderUnitPriceB = 0;
            int colMinimumOrderIdentifierC = 0;
            int colMinimumOrderQtyC = 0;
            int colMinimumOrderPriceC = 0;
            int colMinimumOrderUnitPriceC = 0;
            int colBackupSupplier = 0;
            int colBackupSupplierPrice = 0;
            int colBackupSupplierLink = 0;
            int colBackupSupplierEta = 0;
            int colBackupMinimumOrderIdentifierA = 0;
            int colBackupMinimumOrderQtyA = 0;
            int colBackupMinimumOrderPriceA = 0;
            int colBackupMinimumOrderUnitPriceA = 0;
            int colBackupMinimumOrderIdentifierB = 0;
            int colBackupMinimumOrderQtyB = 0;
            int colBackupMinimumOrderPriceB = 0;
            int colBackupMinimumOrderUnitPriceB = 0;
            int colBackupMinimumOrderIdentifierC = 0;
            int colBackupMinimumOrderQtyC = 0;
            int colBackupMinimumOrderPriceC = 0;
            int colBackupMinimumOrderUnitPriceC = 0;
            int colRivalItemId = 0;
            int colSimilarItemId = 0;
            int colNotes = 0;
            int colListingNotes = 0;
            int colSmtLink = 0;
            int colAmazonLink = 0;
            int colWishLink = 0;
            int colReferenceLink = 0;
            int colPicReady = 0;
            int colOversea = 0;
            int colLength = 0;
            int colWidth = 0;
            int colHeight = 0;
            int colMainSupHolidayStart = 0;
            int colMainSupHolidayEnd = 0;
            int colBackupSupHolidayStart = 0;
            int colBackupSupHolidayEnd = 0;
            
            for(int colHead = minColIx; colHead < maxColIx; colHead++) {  //get column header -- START
                HSSFCell cellHead = firstRow.getCell(colHead);
                if(cellHead == null) {
                    continue;
                }
                cellHead.setCellType(HSSFCell.CELL_TYPE_STRING);
                String colHeader = cellHead.getRichStringCellValue().toString().toUpperCase().trim();
                
                if ("SKU".equals(colHeader) || "PRODUCT ID".equals(colHeader) || "PRODUCTID".equals(colHeader)) { //read column header data -- START
                    colProductId = colHead;
                } else if ("CUSTOM SKU".equals(colHeader) || "CUSTOMSKU".equals(colHeader) || "MY SKU".equals(colHeader) || "MYSKU".equals(colHeader) || "我的SKU".equals(colHeader) || "ALLYSKU".equals(colHeader) || "ALLY SKU".equals(colHeader)) {
                    colCustomSku = colHead;
                } else if ("MOTHER SKU".equals(colHeader) || "MOTHERSKU".equals(colHeader) || "母SKU".equals(colHeader) || "母 SKU".equals(colHeader) || "PARENTSKU".equals(colHeader) || "PARENT SKU".equals(colHeader)) {
                    colMotherSku = colHead;
                } else if ("CHINESE NAME".equals(colHeader) || "CHINESENAME".equals(colHeader) || "品名".equals(colHeader)) {
                    colChineseName = colHead;
                } else if ("ENGLISH NAME".equals(colHeader) || "ENGLISHNAME".equals(colHeader) || "英文品名".equals(colHeader)) {
                    colEnglishName = colHead;
                } else if ("DECLARED NAME CN".equals(colHeader) || "中文报关名".equals(colHeader)) {
                    colDeclaredNameCn = colHead;
                } else if ("DECLARED NAME EN".equals(colHeader) || "英文报关名".equals(colHeader)) {
                    colDeclaredNameEn = colHead;
                } else if ("重量".equals(colHeader) || "WEIGH".equals(colHeader) || "WEIGHT".equals(colHeader) || "估重".equals(colHeader)) {
                    colWeight = colHead;
                } else if ("状态".equals(colHeader) || "STATUS".equals(colHeader) || "STATUS ID".equals(colHeader) || "STATUSID".equals(colHeader)) {
                    colStatusId = colHead;
                } else if ("产品大类".equals(colHeader) || "CATEGORY ID PARENT".equals(colHeader) || "PARENTCATEGORYID".equals(colHeader) || "PARENT CATEGORY ID".equals(colHeader) || "CATEGORYIDPARENT".equals(colHeader)) {
                    colCategoryIdParent = colHead;
                } else if ("产品小类".equals(colHeader) || "CATEGORY ID CHILD".equals(colHeader) || "CHILDCATEGORYID".equals(colHeader) || "CHILD CATEGORY ID".equals(colHeader) || "CATEGORYIDCHILD".equals(colHeader)) {
                    colCategoryIdChild = colHead;
                } else if ("监控3".equals(colHeader) || "产品性质".equals(colHeader) || "TYPE".equals(colHeader) || "PRODUCTTYPE".equals(colHeader) || "PRODUCT TYPE".equals(colHeader) || "产品类型".equals(colHeader) || "类型".equals(colHeader)) {
                    colProductType = colHead;
                } else if ("截图链接".equals(colHeader) || "图片链接".equals(colHeader) || "图片".equals(colHeader) || "IMAGE".equals(colHeader) || "IMAGELINK".equals(colHeader) || "IMAGE LINK".equals(colHeader)) {
                    colImageLink = colHead;
                } else if ("开发日期".equals(colHeader) || "SOURCING DATE".equals(colHeader)|| "SOURCINGDATE".equals(colHeader)) {
                    colSourcingDate = colHead;
                } else if ("USD".equals(colHeader) || "US".equals(colHeader) || "CP".equals(colHeader)) {
                    colUsd = colHead;
                } else if ("供应商".equals(colHeader) || "SUPPLIER".equals(colHeader)) {
                    colSupplier = colHead;
                } else if ("SUPPLIER TYPE".equals(colHeader) || "SUPPLIERTYPE".equals(colHeader)) {
                    colSupplierType = colHead;
                } else if ("原始成本".equals(colHeader)) {
                    colPrice = colHead;
                } else if ("国内".equals(colHeader) || "采购链接".equals(colHeader) || "供应商链接".equals(colHeader) || "SUPPLIER LINK".equals(colHeader) || "SUPPLIERLINK".equals(colHeader)) {
                    colSupplierLink = colHead;
                } else if ("ETA".equals(colHeader)) {
                    colEta = colHead;
                } else if ("起订量1-关联符".equals(colHeader) || "起订量-关联符1".equals(colHeader)) {
                    colMinimumOrderIdentifierA = colHead;
                } else if ("起订量1-数量".equals(colHeader) || "起订量-数量1".equals(colHeader)) {
                    colMinimumOrderQtyA = colHead;
                } else if ("起订量1-金额".equals(colHeader) || "起订量-金额1".equals(colHeader)) {
                    colMinimumOrderPriceA = colHead;
                } else if ("起订量1-成本".equals(colHeader) || "起订量-成本1".equals(colHeader)) {
                    colMinimumOrderUnitPriceA = colHead;
                } else if ("起订量2-关联符".equals(colHeader) || "起订量-关联符2".equals(colHeader)) {
                    colMinimumOrderIdentifierB = colHead;
                } else if ("起订量2-数量".equals(colHeader) || "起订量-数量2".equals(colHeader)) {
                    colMinimumOrderQtyB = colHead;
                } else if ("起订量2-金额".equals(colHeader) || "起订量-金额2".equals(colHeader)) {
                    colMinimumOrderPriceB = colHead;
                } else if ("起订量2-成本".equals(colHeader) || "起订量-成本2".equals(colHeader)) {
                    colMinimumOrderUnitPriceB = colHead;
                } else if ("起订量3-关联符".equals(colHeader) || "起订量-关联符3".equals(colHeader)) {
                    colMinimumOrderIdentifierC = colHead;
                } else if ("起订量3-数量".equals(colHeader) || "起订量-数量3".equals(colHeader)) {
                    colMinimumOrderQtyC = colHead;
                } else if ("起订量3-金额".equals(colHeader) || "起订量-金额3".equals(colHeader)) {
                    colMinimumOrderPriceC = colHead;
                } else if ("起订量3-成本".equals(colHeader) || "起订量-成本3".equals(colHeader)) {
                    colMinimumOrderUnitPriceC = colHead;
                } else if ("备用供应商".equals(colHeader) || "BACKUP SUPPLIER".equals(colHeader)) {
                    colBackupSupplier = colHead;
                } else if ("备用采购链接".equals(colHeader) || "BACKUP SUPPLIER LINK".equals(colHeader)) {
                    colBackupSupplierLink = colHead;
                } else if ("备用原始成本".equals(colHeader) || "BACKUP SUPPLIER PRICE".equals(colHeader)) {
                    colBackupSupplierPrice = colHead;
                } else if ("备用ETA".equals(colHeader) || "BACKUP SUPPLIER ETA".equals(colHeader)) {
                    colBackupSupplierEta = colHead;
                } else if ("备用起订量1-关联符".equals(colHeader) || "备用起订量-关联符1".equals(colHeader)) {
                    colBackupMinimumOrderIdentifierA = colHead;
                } else if ("备用起订量1-数量".equals(colHeader) || "备用起订量-数量1".equals(colHeader)) {
                    colBackupMinimumOrderQtyA = colHead;
                } else if ("备用起订量1-金额".equals(colHeader) || "备用起订量-金额1".equals(colHeader)) {
                    colBackupMinimumOrderPriceA = colHead;
                } else if ("备用起订量1-成本".equals(colHeader) || "备用起订量-成本1".equals(colHeader)) {
                    colBackupMinimumOrderUnitPriceA = colHead;
                } else if ("备用起订量2-关联符".equals(colHeader) || "备用起订量-关联符2".equals(colHeader)) {
                    colBackupMinimumOrderIdentifierB = colHead;
                } else if ("备用起订量2-数量".equals(colHeader) || "备用起订量-数量2".equals(colHeader)) {
                    colBackupMinimumOrderQtyB = colHead;
                } else if ("备用起订量2-金额".equals(colHeader) || "备用起订量-金额2".equals(colHeader)) {
                    colBackupMinimumOrderPriceB = colHead;
                } else if ("备用起订量2-成本".equals(colHeader) || "备用起订量-成本2".equals(colHeader)) {
                    colBackupMinimumOrderUnitPriceB = colHead;
                } else if ("备用起订量3-关联符".equals(colHeader) || "备用起订量-关联符3".equals(colHeader)) {
                    colBackupMinimumOrderIdentifierC = colHead;
                } else if ("备用起订量3-数量".equals(colHeader) || "备用起订量-数量3".equals(colHeader)) {
                    colBackupMinimumOrderQtyC = colHead;
                } else if ("备用起订量3-金额".equals(colHeader) || "备用起订量-金额3".equals(colHeader)) {
                    colBackupMinimumOrderPriceC = colHead;
                } else if ("备用起订量3-成本".equals(colHeader) || "备用起订量-成本3".equals(colHeader)) {
                    colBackupMinimumOrderUnitPriceC = colHead;
                } else if ("IDENTICAL ITEM ID".equals(colHeader) || "相同对手".equals(colHeader) || "EBAY相同对手".equals(colHeader) || "EBAY链接".equals(colHeader)) {
                    colRivalItemId = colHead;
                } else if ("SIMILAR ITEM ID".equals(colHeader) || "相似对手".equals(colHeader) || "EBAY相似对手".equals(colHeader)) {
                    colSimilarItemId = colHead;
                } else if ("采购备注".equals(colHeader) || "开发备注".equals(colHeader) || "PURCHASING NOTES".equals(colHeader) || "PURCHASING NOTES".equals(colHeader)) {
                    colNotes = colHead;
                } else if ("销售备注".equals(colHeader) || "LISTING NOTES".equals(colHeader)) {
                    colListingNotes = colHead;
                } else if ("SMTLINK".equals(colHeader) || "SMT LINK".equals(colHeader) || "SMT链接".equals(colHeader)) {
                    colSmtLink = colHead;
                } else if ("WISHLINK".equals(colHeader) || "WISH LINK".equals(colHeader) || "WISH链接".equals(colHeader)) {
                    colWishLink = colHead;
                } else if ("AMAZONLINK".equals(colHeader) || "AMAZON LINK".equals(colHeader) || "AMAZON链接".equals(colHeader) || "AMA链接".equals(colHeader) || "AMALINK".equals(colHeader) || "AMA LINK".equals(colHeader)) {
                    colAmazonLink = colHead;
                } else if ("REFERENCELINK".equals(colHeader) || "REFERENCE LINK".equals(colHeader) || "参考链接".equals(colHeader) || "REF链接".equals(colHeader)) {
                    colReferenceLink = colHead;
                } else if ("PICREADY".equals(colHeader) || "PIC READY".equals(colHeader) || "PICTURE READY".equals(colHeader) || "PICTUREREADY".equals(colHeader)) {
                    colPicReady = colHead;
                } else if ("OVERSEA".equals(colHeader) || "海外仓".equals(colHeader)) {
                    colOversea = colHead;
                } else if ("LENGTH".equals(colHeader) || "长度(cm)".equals(colHeader)) {
                    colLength = colHead;
                } else if ("WIDTH".equals(colHeader) || "宽度(cm)".equals(colHeader)) {
                    colWidth = colHead;
                } else if ("HEIGHT".equals(colHeader) || "高度(cm)".equals(colHeader)) {
                    colHeight = colHead;
                } else if ("SUPPLIER HOLIDAY START".equals(colHeader) || "供应商放假开始".equals(colHeader)) {
                    colMainSupHolidayStart = colHead;
                } else if ("SUPPLIER HOLIDAY END".equals(colHeader) || "供应商放假结束".equals(colHeader)) {
                    colMainSupHolidayEnd = colHead;
                } else if ("BACKUP SUPPLIER HOLIDAY START".equals(colHeader) || "备用供应商放假开始".equals(colHeader)) {
                    colBackupSupHolidayStart = colHead;
                } else if ("BACKUP SUPPLIER HOLIDAY START".equals(colHeader) || "备用供应商放假开始".equals(colHeader)) {
                    colBackupSupHolidayEnd = colHead;
                }
                //read column header data -- END

            }   //get column header -- END
            
            //get ProductStatusList == START
            List<String> statusList = new ArrayList<String>();
            List<GenericValue> enumerationStatusList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "PM_STATUS"), null, true);
            for (GenericValue pmStatus : enumerationStatusList) {
                statusList.add(pmStatus.getString("enumCode"));
            }
            //get ProductStatusList == END
            
            //get ProductTypeList == START
            List<String> productTypeList = new ArrayList<String>();
            List<GenericValue> enumerationProductTypeList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "GUDAO_PRODUCT_TYPE"), null, true);
            for (GenericValue pmProductType : enumerationProductTypeList) {
                productTypeList.add(pmProductType.getString("enumCode"));
            }
            //get ProductTypeList == END
            
            //Debug.logError("Upload XLS prepare to loop row", module);
            for (int j = 1; j <= sheetLastRowNumber; j++) { //loop rows -- START
                HSSFRow row = sheet.getRow(j);
                if (UtilValidate.isNotEmpty(row)) {  //if row is not empty -- START
                    Map<String, Object> productImportCtx = FastMap.newInstance();
                    boolean updateProductImport = true;
                    StringBuffer errorNotes = new StringBuffer();
                    
                    String productId = null;
                    String customSku = null;
                    String motherSku = null;
                    String chineseName = null;
                    String englishName = null;
                    String overRideDeclaredCn = null;
                    String overRideDeclaredEn = null;
                    double weight = 0.0;
                    String statusId = null;
                    String ebayStatusId = null;
                    String smtStatusId = null;
                    String wishStatusId = null;
                    String amazonStatusId = null;
                    String lazadaStatusId = null;
                    String cdiscountStatusId = null;
                    String parentCategoryName = null;
                    String childrenCategoryName = null;
                    String productType = null;
                    String imageLink = null;
                    BigDecimal priceUsd = new BigDecimal("99.99");
                    String supplier = null;
                    String supplierLink = null;
                    BigDecimal actualPrice = BigDecimal.ZERO;
                    BigDecimal price = BigDecimal.ZERO;
                    long eta = 0;
                    String moIdentifierA = null;
                    long moQtyA = 0;
                    BigDecimal moPriceA = BigDecimal.ZERO;
                    BigDecimal moUnitPriceA = BigDecimal.ZERO;
                    String moIdentifierB = null;
                    long moQtyB = 0;
                    BigDecimal moPriceB = BigDecimal.ZERO;
                    BigDecimal moUnitPriceB = BigDecimal.ZERO;
                    String moIdentifierC = null;
                    long moQtyC = 0;
                    BigDecimal moPriceC = BigDecimal.ZERO;
                    BigDecimal moUnitPriceC = BigDecimal.ZERO;
                    String backupSupplier = null;
                    String backupSupplierLink = null;
                    BigDecimal backupSupplierPrice = BigDecimal.ZERO;
                    long backupSupplierEta = 0;
                    String backupMoIdentifierA = null;
                    long backupMoQtyA = 0;
                    BigDecimal backupMoPriceA = BigDecimal.ZERO;
                    BigDecimal backupMoUnitPriceA = BigDecimal.ZERO;
                    String backupMoIdentifierB = null;
                    long backupMoQtyB = 0;
                    BigDecimal backupMoPriceB = BigDecimal.ZERO;
                    BigDecimal backupMoUnitPriceB = BigDecimal.ZERO;
                    String backupMoIdentifierC = null;
                    long backupMoQtyC = 0;
                    BigDecimal backupMoPriceC = BigDecimal.ZERO;
                    BigDecimal backupMoUnitPriceC = BigDecimal.ZERO;
                    String rivalItemId = null;
                    String similarItemId = null;
                    String notes = null;
                    String listingNotes = null;
                    String smtLink = null;
                    String wishLink = null;
                    String amazonLink = null;
                    String referenceLink = null;
                    String picReady = null;
                    String oversea = null;
                    double length = 0.0;
                    double width = 0.0;
                    double height = 0.0;
                    
                    //Debug.logError("Upload XLS looping row now. Row number: " + j, module);
                    for(int colIx = minColIx; colIx < maxColIx; colIx++) {    //loop cell in a row -- START
                        HSSFCell cell = row.getCell(colIx);
                        if(cell == null) {
                            continue;
                        }
                        //Debug.logError("Upload XLS looping column now. Column number: " + colIx, module);
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        String dataString = cell.getRichStringCellValue().toString().trim();
                        
                        if (colIx == (colProductId)) { //read record -- START
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.contains(" ")) {
                                    dataString = dataString.replaceAll(" ","");
                                }
                                if (dataString.length() > 12) {
                                    errorNotes.append("SKU is too long - SKU太长.不能超过12字.");
                                }
                                if (dataString.length() < 7) {
                                    errorNotes.append("SKU is too short - SKU太短.最短7字.");
                                }
                                productId = dataString.toUpperCase();
                                if (productId.matches(".*[ \t\n\r\f\\&\"].*")) {
                                    errorNotes.append("SKU contains invalid character - SKU包含非法字符.");
                                }
                            } else {   //break when there is no record anymore -- START
                                updateProductImport = false;
                                break;
                            }   //break when there is no record anymore -- END
                        } else if (colIx == (colCustomSku)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("customSku is too long - AllySKU太长.不能超过255字.");
                                } else {
                                    customSku = dataString;
                                    if (customSku.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("customSku contains invalid character - AllySKU包含非法字符.");
                                    }
                                }
                            }
                        } else if (colIx == (colMotherSku)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("motherSku is too long - 母SKU太长.不能超过255字.");
                                } else {
                                    motherSku = dataString.toUpperCase();
                                    if (motherSku.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("motherSku contains invalid character - 母SKU包含非法字符.");
                                    }
                                }
                            } else {
                                motherSku = productId.toUpperCase().substring(0, 7);
                            }
                        } else if (colIx == (colChineseName)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("chineseName is too long - 中文名字太长.不能超过255字.");
                                } else {
                                    chineseName = dataString;
                                }
                            }
                        } else if (colIx == (colEnglishName)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("englishName is too long - 英名字太长.不能超过255字.");
                                } else {
                                    englishName = dataString;
                                }
                            }
                        } else if (colIx == (colDeclaredNameCn)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("DeclaredNameCn is too long - 中文报关名太长.不能超过255字.");
                                } else {
                                    overRideDeclaredCn = dataString;
                                }
                            }
                        } else if (colIx == (colDeclaredNameEn)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("DeclaredNameEn is too long - 英文报关名太长.不能超过255字.");
                                } else {
                                    overRideDeclaredEn = dataString;
                                }
                            }
                        } else if (colIx == (colWeight)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("weight is too long - 估重太长.不能超过255字.");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("weight contains invalid character - 估重包含非法字符.");
                                    } else {
                                        weight = Double.parseDouble(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colStatusId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("statusId is too long - 状态太长.不能超过255字.");
                                } else {
                                    statusId = dataString.toUpperCase();
                                    if (!statusList.contains(dataString.toUpperCase())) {
                                        errorNotes.append("statusId invalid - 状态不真确");
                                    }
                                }
                            }
                        } else if (colIx == (colCategoryIdParent)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("categoryIdParent is too long - 产品大类太长.不能超过255字");
                                } else {
                                    parentCategoryName = dataString;
                                }
                            }
                        } else if (colIx == (colCategoryIdChild)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("categoryIdParent is too long - 产品小类太长.不能超过255字.");
                                } else {
                                    childrenCategoryName = dataString;
                                }
                            }
                        } else if (colIx == (colProductType)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("productType is too long - 产品性质太长.不能超过255字.");
                                } else {
                                    productType = dataString.toUpperCase();
                                    if (!productTypeList.contains(dataString.toUpperCase())) {
                                        errorNotes.append("产品性质不真确");
                                    }
                                }
                            }
                        } else if (colIx == (colUsd)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("priceUsd is too long - USD太长.不能超过255字.");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("priceUsd contains invalid character - USD包含非法字符.");
                                    } else {
                                        priceUsd = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colSupplier)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("supplier is too long - 供应商太长.不能超过255字.");
                                } else {
                                    supplier = dataString;
                                    if (supplier.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("supplier contains invalid character - 供应商包含非法字符.");
                                    }
                                }
                            }
                        } else if (colIx == (colPrice)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("supplierPrice is too long - 原始成本太长.不能超过255字.");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("supplierPrice contains invalid character - 原始成本包含非法字符.");
                                    } else {
                                        price = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colSupplierLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("supplierLink is too long - 采购链接太长.不能超过255字");
                                } else {
                                    supplierLink = dataString;
                                }
                            }
                        } else if (colIx == (colEta)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("eta is too long - ETA太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("eta contains invalid character - ETA包含非法字符.");
                                    } else {
                                        eta = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderIdentifierA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moIdentifierA is too long - '起订量1-关联符'太长.不能超过255字");
                                } else {
                                    moIdentifierA = dataString;
                                    if (moIdentifierA.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("moIdentifierA contains invalid character - '起订量1-关联符'包含非法字符.");
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderQtyA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moQtyA is too long - '起订量1-数量'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("moQtyA contains invalid character - '起订量1-数量'包含非法字符.");
                                    } else {
                                        moQtyA = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderPriceA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moPriceA is too long - '起订量1-金额'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moPriceA contains invalid character - '起订量1-金额'包含非法字符.");
                                    } else {
                                        moPriceA = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderUnitPriceA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moUnitPriceA is too long - '起订量1-成本'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moUnitPriceA contains invalid character - '起订量1-成本'包含非法字符.");
                                    } else {
                                        moUnitPriceA = new BigDecimal(dataString);
                                        actualPrice = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderIdentifierB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moIdentifierB is too long - '起订量2-关联符'太长.不能超过255字");
                                } else {
                                    moIdentifierB = dataString;
                                    if (moIdentifierB.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("moIdentifierB contains invalid character - '起订量2-关联符'包含非法字符.");
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderQtyB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moQtyB is too long - '起订量2-数量'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("moQtyB contains invalid character - '起订量2-数量'包含非法字符.");
                                    } else {
                                        moQtyB = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderPriceB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moPriceB is too long - '起订量2-金额'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moPriceB contains invalid character - '起订量2-金额'包含非法字符.");
                                    } else {
                                        moPriceB = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderUnitPriceB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moUnitPriceB is too long - '起订量2-成本'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moUnitPriceB contains invalid character - '起订量2-成本'包含非法字符.");
                                    } else {
                                        moUnitPriceB = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderIdentifierC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moIdentifierC is too long - '起订量3-关联符'太长.不能超过255字");
                                } else {
                                    moIdentifierC = dataString;
                                    if (moIdentifierC.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("moIdentifierC contains invalid character - '起订量3-关联符'包含非法字符.");
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderQtyC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moQtyC is too long - '起订量3-数量'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("moQtyC contains invalid character - '起订量3-数量'包含非法字符.");
                                    } else {
                                        moQtyC = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderPriceC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moPriceC is too long - '起订量3-金额'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moPriceC contains invalid character - '起订量3-金额'包含非法字符.");
                                    } else {
                                        moPriceC = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderUnitPriceC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moUnitPriceC is too long - '起订量3-成本'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moUnitPriceC contains invalid character - '起订量3-成本'包含非法字符.");
                                    } else {
                                        moUnitPriceC = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupSupplier)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupSupplier is too long - 备用供应商太长.不能超过255字");
                                } else {
                                    backupSupplier = dataString;
                                    if (backupSupplier.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("backupSupplier contains invalid character - 备用供应商包含非法字符.");
                                    }
                                }
                            }
                        } else if (colIx == (colBackupSupplierPrice)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupSupplierPrice is too long - 备用原始成本太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupSupplierPrice contains invalid character - 备用原始成本包含非法字符.");
                                    } else {
                                        backupSupplierPrice = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupSupplierLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupSupplierLink is too long - 备用采购链接太长.不能超过255字");
                                } else {
                                    backupSupplierLink = dataString;
                                }
                            }
                        } else if (colIx == (colBackupSupplierEta)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupSupplierEta is too long - 备用ETA太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("backupSupplierEta contains invalid character - 备用ETA包含非法字符.");
                                    } else {
                                        backupSupplierEta = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderIdentifierA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoIdentifierA is too long - '备用起订量1-关联符'太长.不能超过255字");
                                } else {
                                    backupMoIdentifierA = dataString;
                                    if (backupMoIdentifierA.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("backupMoIdentifierA contains invalid character - '备用起订量1-关联符'包含非法字符.");
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderQtyA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoQtyA is too long - '备用起订量1-数量'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("backupMoQtyA contains invalid character - '备用起订量1-数量'包含非法字符.");
                                    } else {
                                        backupMoQtyA = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderPriceA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoPriceA is too long - '备用起订量1-金额'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoPriceA contains invalid character - '备用起订量1-金额'包含非法字符.");
                                    } else {
                                        backupMoPriceA = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderUnitPriceA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoUnitPriceA is too long - '备用起订量1-成本'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoUnitPriceA contains invalid character - '备用起订量1-成本'包含非法字符.");
                                    } else {
                                        backupMoUnitPriceA = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderIdentifierB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoIdentifierB is too long - '备用起订量2-关联符'太长.不能超过255字");
                                } else {
                                    backupMoIdentifierB = dataString;
                                    if (backupMoIdentifierB.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("backupMoIdentifierB contains invalid character - '备用起订量2-关联符'包含非法字符.");
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderQtyB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoQtyB is too long - '备用起订量2-数量'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("backupMoQtyB contains invalid character - '备用起订量2-数量'包含非法字符.");
                                    } else {
                                        backupMoQtyB = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderPriceB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoPriceB is too long - '备用起订量2-金额'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoPriceB contains invalid character - '备用起订量2-金额'包含非法字符.");
                                    } else {
                                        backupMoPriceB = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderUnitPriceB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoUnitPriceB is too long - '备用起订量2-成本'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoUnitPriceB contains invalid character - '备用起订量2-成本'包含非法字符.");
                                    } else {
                                        backupMoUnitPriceB = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderIdentifierC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoIdentifierC is too long - '备用起订量3-关联符'太长.不能超过255字");
                                } else {
                                    backupMoIdentifierC = dataString;
                                    if (backupMoIdentifierC.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("backupMoIdentifierC contains invalid character - '备用起订量3-关联符'包含非法字符.");
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderQtyC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoQtyC is too long - '备用起订量3-数量'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("backupMoQtyC contains invalid character - '备用起订量3-数量'包含非法字符.");
                                    } else {
                                        backupMoQtyC = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderPriceC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoPriceC is too long - '备用起订量3-金额'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoPriceC contains invalid character - '备用起订量3-金额'包含非法字符.");
                                    } else {
                                        backupMoPriceC = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderUnitPriceC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoUnitPriceC is too long - 备用起订量3-成本'太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoUnitPriceC contains invalid character - '备用起订量3-成本'包含非法字符.");
                                    } else {
                                        backupMoUnitPriceC = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colRivalItemId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("rivalItemId is too long - 相同对手太长.不能超过255字");
                                } else {
                                    if (dataString.length() < 9 || dataString.length() > 12 || dataString.matches(".*000000")) {
                                        rivalItemId = dataString + "E";
                                    } else {
                                        rivalItemId = dataString;
                                    }
                                }
                            }
                        } else if (colIx == (colSimilarItemId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("similarItemId is too long - 相似对手太长.不能超过255字");
                                } else {
                                    if (dataString.length() < 9 || dataString.length() > 12 || dataString.matches(".*000000")) {
                                        similarItemId = dataString + "E";
                                    } else {
                                        similarItemId = dataString;
                                    }
                                }
                            }
                        } else if (colIx == (colNotes)) {
                            notes = dataString;
                        } else if (colIx == (colListingNotes)) {
                            listingNotes = dataString;
                        } else if (colIx == (colSmtLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("smtLink is too long - SMT链接太长.不能超过255字");
                                } else {
                                    smtLink = dataString;
                                }
                            }
                        } else if (colIx == (colWishLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("WishLink is too long - WISH链接太长.不能超过255字");
                                } else {
                                    wishLink = dataString;
                                }
                            }
                        } else if (colIx == (colAmazonLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("amazonLink is too long - Amazon链接太长.不能超过255字");
                                } else {
                                    amazonLink = dataString;
                                }
                            }
                        } else if (colIx == (colReferenceLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("ReferenceLink is too long - Reference链接太长.不能超过255字");
                                } else {
                                    referenceLink = dataString;
                                }
                            }
                        } else if (colIx == (colPicReady)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("PicReady is too long - PicReady太长.不能超过255字");
                                } else {
                                    picReady = dataString.toUpperCase();
                                }
                            }
                        } else if (colIx == (colOversea)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("Oversea is too long - 海外仓太长.不能超过255字");
                                } else {
                                    oversea = dataString.toUpperCase().substring(0,1);
                                }
                            }
                        } else if (colIx == (colLength)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("length is too long - 长度太长.不能超过255字.");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("length contains invalid character - 长度包含非法字符.");
                                    } else {
                                        length = Double.parseDouble(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colWidth)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("width is too long - 宽度太长.不能超过255字.");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("width contains invalid character - 宽度包含非法字符.");
                                    } else {
                                        width = Double.parseDouble(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colHeight)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("height is too long - 高度太长.不能超过255字.");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("height contains invalid character - 高度包含非法字符.");
                                    } else {
                                        height = Double.parseDouble(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMainSupHolidayStart)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("holidayStart is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("supplierHolidayStart", new java.sql.Date(HSSFDateUtil.getJavaDate(Double.parseDouble(dataString)).getTime()));
                                }
                            }
                        } else if (colIx == (colMainSupHolidayEnd)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("sourcingDate is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("supplierHolidayEnd", new java.sql.Date(HSSFDateUtil.getJavaDate(Double.parseDouble(dataString)).getTime()));
                                }
                            }
                        } else if (colIx == (colBackupSupHolidayStart)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("sourcingDate is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("backupHolidayStart", new java.sql.Date(HSSFDateUtil.getJavaDate(Double.parseDouble(dataString)).getTime()));
                                }
                            }
                        } else if (colIx == (colBackupSupHolidayEnd)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("sourcingDate is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("backupHolidayEnd", new java.sql.Date(HSSFDateUtil.getJavaDate(Double.parseDouble(dataString)).getTime()));
                                }
                            }
                        }//read record -- END
                    }   //loop cell in a row -- END
                    
                    if (UtilValidate.isNotEmpty(productId)) {   //if productId is not empty == START
                        //Check mandatory input == START
                        if (UtilValidate.isEmpty(chineseName)) {
                            errorNotes.append("Chinese Name is missing - 品名必填.");
                        }
                        if (UtilValidate.isEmpty(overRideDeclaredCn)) {
                            errorNotes.append("Declared Chinese Name is missing - 中文报关名必填.");
                        }
                        if (UtilValidate.isEmpty(overRideDeclaredEn)) {
                            errorNotes.append("Declared English Name is missing - 英文报关名必填.");
                        }
                        if (UtilValidate.isEmpty(motherSku)) {
                            motherSku = productId.toUpperCase().substring(0, 7);
                        }
                        if (UtilValidate.isEmpty(weight) || weight == 0.0) {
                            errorNotes.append("Weight is missing - 估重必填.");
                        }
                        if (UtilValidate.isEmpty(statusId)) {
                            errorNotes.append("statusId is missing - 状态必填.");
                        }
                        if (UtilValidate.isEmpty(parentCategoryName)) {
                            errorNotes.append("categoryIdParent is missing - 产品大类必填.");
                        }
                        if (UtilValidate.isEmpty(childrenCategoryName)) {
                            errorNotes.append("categoryIdChild is missing - 产品小类必填.");
                        }
                        if (UtilValidate.isEmpty(productType)) {
                            errorNotes.append("productType is missing - 产品性质必填.");
                        }
                        if (UtilValidate.isEmpty(supplier)) {
                            errorNotes.append("supplier is missing - 供应商必填.");
                        }
                        if (UtilValidate.isEmpty(supplierLink)) {
                            errorNotes.append("supplierLink is missing - 采购链接必填.");
                        }
                        if (UtilValidate.isEmpty(eta) || eta == 0) {
                            errorNotes.append("eta is missing - ETA必填.");
                        }
                        if (UtilValidate.isEmpty(actualPrice) || actualPrice.compareTo(BigDecimal.ZERO) == 0) {
                            errorNotes.append("actualPrice is missing - 实际成本必填.");
                        }
                        if (UtilValidate.isNotEmpty(backupSupplier)) {
                            if (UtilValidate.isEmpty(backupSupplierLink)) {
                                errorNotes.append("backupSupplierLink is missing - 备用采购链接必填.");
                            }
                            if (UtilValidate.isEmpty(backupSupplierEta) || backupSupplierEta == 0) {
                                errorNotes.append("backupSupplierEta is missing - 备用ETA必填.");
                            }
                            if (UtilValidate.isEmpty(backupMoUnitPriceA) || backupMoUnitPriceA.compareTo(BigDecimal.ZERO) == 0) {
                                errorNotes.append("backupMoUnitPriceA is missing - 备用起订量1-成本必填.");
                            }
                            if (UtilValidate.isEmpty(backupMoQtyA) || backupMoQtyA == 0) {
                                errorNotes.append("backupMoQtyA is missing - '备用起订量1-数量'必填.");
                            }
                        }
                        //Check mandatory input == END
                    }   //if productId is not empty == START
                    else {
                        updateProductImport = false;
                    }
                    
                    if (updateProductImport) {    //if row indeed has data -- START
                        try {   //try -- second -- START
                            //populating productImportCtx == START
                            productImportCtx.put("productId", productId);
                            productImportCtx.put("customSku", customSku);
                            productImportCtx.put("motherSku", motherSku);
                            productImportCtx.put("chineseName", chineseName);
                            productImportCtx.put("englishName", englishName);
                            if (weight > 0.0) {
                                productImportCtx.put("weight", weight);
                            }
                            if (length > 0.0) {
                                productImportCtx.put("length", length);
                            }
                            if (width > 0.0) {
                                productImportCtx.put("width", width);
                            }
                            if (height > 0.0) {
                                productImportCtx.put("height", height);
                            }
                            productImportCtx.put("statusId", statusId);
                            productImportCtx.put("productType", productType);
                            productImportCtx.put("imageLink", imageLink);
                            if (priceUsd.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("priceUsd", priceUsd);
                            }
                            productImportCtx.put("supplier", supplier);
                            productImportCtx.put("supplierLink", supplierLink);
                            if (price.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("price", price);
                            }
                            if (eta > 0) {
                                productImportCtx.put("eta", eta);
                            }
                            productImportCtx.put("moIdentifierA", moIdentifierA);
                            if (moQtyA > 0) {
                                productImportCtx.put("moQtyA", moQtyA);
                            }
                            if (moPriceA.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moPriceA", moPriceA);
                            }
                            if (moUnitPriceA.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moUnitPriceA", moUnitPriceA);
                                productImportCtx.put("actualPrice", actualPrice);
                            }
                            productImportCtx.put("moIdentifierB", moIdentifierB);
                            if (moQtyB > 0) {
                                productImportCtx.put("moQtyB", moQtyB);
                                if (moQtyA == moQtyB) {
                                    errorNotes.append("MoQtyA is the same with MoQtyB - '起订量1-数量'和'起订量2-数量'不能一样");
                                }
                            }
                            if (moPriceB.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moPriceB", moPriceB);
                                if (moPriceA.compareTo(moPriceB) == 0) {
                                    errorNotes.append("MoPriceA is the same with MoPriceB - '起订量1-金额'和'起订量2-金额'不能一样");
                                }
                            }
                            if (moUnitPriceB.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moUnitPriceB", moUnitPriceB);
                            }
                            productImportCtx.put("moIdentifierC", moIdentifierC);
                            if (moQtyC > 0) {
                                productImportCtx.put("moQtyC", moQtyC);
                                if (moQtyA == moQtyC) {
                                    errorNotes.append("MoQtyA is the same with MoQtyC - '起订量1-数量'和'起订量3-数量'不能一样");
                                }
                                if (moQtyB == moQtyC) {
                                    errorNotes.append("MoQtyB is the same with MoQtyC - '起订量2-数量'和'起订量3-数量'不能一样");
                                }
                            }
                            if (moPriceC.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moPriceC", moPriceC);
                                if (moPriceA.compareTo(moPriceC) == 0) {
                                    errorNotes.append("MoPriceA is the same with MoPriceC - '起订量1-金额'和'起订量3-金额'不能一样");
                                }
                                if (moPriceB.compareTo(moPriceC) == 0) {
                                    errorNotes.append("MoPriceB is the same with MoPriceC - '起订量2-金额'和'起订量3-金额'不能一样");
                                }
                            }
                            if (moUnitPriceC.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moUnitPriceC", moUnitPriceC);
                            }
                            productImportCtx.put("backupSupplier", backupSupplier);
                            productImportCtx.put("backupSupplierLink", backupSupplierLink);
                            if (backupSupplierPrice.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupSupplierPrice", backupSupplierPrice);
                            }
                            if (backupSupplierEta > 0) {
                                productImportCtx.put("backupSupplierEta", backupSupplierEta);
                            }
                            productImportCtx.put("backupMoIdentifierA", backupMoIdentifierA);
                            if (backupMoQtyA > 0) {
                                productImportCtx.put("backupMoQtyA", backupMoQtyA);
                            }
                            if (backupMoPriceA.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoPriceA", backupMoPriceA);
                            }
                            if (backupMoUnitPriceA.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoUnitPriceA", backupMoUnitPriceA);
                            }
                            productImportCtx.put("backupMoIdentifierB", backupMoIdentifierB);
                            if (backupMoQtyB > 0) {
                                productImportCtx.put("backupMoQtyB", backupMoQtyB);
                            }
                            if (backupMoPriceB.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoPriceB", backupMoPriceB);
                            }
                            if (backupMoUnitPriceB.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoUnitPriceB", backupMoUnitPriceB);
                            }
                            productImportCtx.put("backupMoIdentifierC", backupMoIdentifierC);
                            if (backupMoQtyC > 0) {
                                productImportCtx.put("backupMoQtyC", backupMoQtyC);
                            }
                            if (backupMoPriceC.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoPriceC", backupMoPriceC);
                            }
                            if (backupMoUnitPriceC.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoUnitPriceC", backupMoUnitPriceC);
                            }
                            productImportCtx.put("rivalItemId", rivalItemId);
                            productImportCtx.put("similarItemId", similarItemId);
                            productImportCtx.put("notes", notes);
                            productImportCtx.put("listingNotes", listingNotes);
                            productImportCtx.put("smtLink", smtLink);
                            productImportCtx.put("wishLink", wishLink);
                            productImportCtx.put("amazonLink", amazonLink);
                            productImportCtx.put("referenceLink", referenceLink);
                            productImportCtx.put("sourcingDate", nowTimestamp);
                            if (UtilValidate.isEmpty(picReady)) {
                                productImportCtx.put("pictureReady", "N");
                            } else {
                                if (picReady.substring(0,1).equals("Y")) {
                                    productImportCtx.put("pictureReady", "Y");
                                } else {
                                    productImportCtx.put("pictureReady", "N");
                                }
                            }
                            if (UtilValidate.isEmpty(oversea)) {
                                productImportCtx.put("oversea", "N");
                            } else {
                                if (oversea.substring(0,1).equals("Y")) {
                                    productImportCtx.put("oversea", "Y");
                                } else {
                                    productImportCtx.put("oversea", "N");
                                }
                            }
                            //populating productImportCtx == END
                            
                            //default value == START
                            //String childrenCategoryCn = null;
                            //String childrenCategoryEn = null;
                            List<GenericValue> parentCategoryList = delegator.findByAnd("GudaoProductCategory", UtilMisc.toMap("categoryName", parentCategoryName, "type", "PARENT"), null, false);
                            if (UtilValidate.isNotEmpty(parentCategoryList)) {  //check if parentCategoryName exist in database == START
                                String parentCategoryId = EntityUtil.getFirst(parentCategoryList).getString("categoryId");
                                productImportCtx.put("categoryIdParent", parentCategoryName);
                                List<GenericValue> childrenCategoryList = delegator.findByAnd("GudaoProductCategory", UtilMisc.toMap("parentCategoryId", parentCategoryId, "categoryName", childrenCategoryName, "type", "CHILDREN"), null, false);
                                if (UtilValidate.isNotEmpty(childrenCategoryList)) {    //check if childrenCategoryName exist in database == START
                                    GenericValue childrenCategoryGV = EntityUtil.getFirst(childrenCategoryList);
                                    //childrenCategoryCn = childrenCategoryGV.getString("categoryNameCn");
                                    //childrenCategoryEn = childrenCategoryGV.getString("categoryName");
                                    productImportCtx.put("categoryIdChild", childrenCategoryName);
                                } else {
                                    errorNotes.append("Children Category does not exist in database.");
                                }   //check if childrenCategoryName exist in database == END
                            } else {
                                errorNotes.append("Parent Category does not exist in database.");
                            }   //check if parentCategoryName exist in database == END
                            
                            //productImportCtx.put("productGroup", pmDefaultValue().get("defaultProductGroup"));
                            productImportCtx.put("ownerGroup", ownerGroup.toUpperCase());
                            productImportCtx.put("purchaser", pmDefaultValue().get("defaultPurchaser"));
                            productImportCtx.put("extraStockingDay", Long.valueOf(pmDefaultValue().get("defaultExtraStockingDay")));
                            productImportCtx.put("minOrderDays", Long.valueOf(pmDefaultValue().get("defaultMinOrderDays")));
                            
                            if (UtilValidate.isNotEmpty(backupSupplier)) {
                                productImportCtx.put("backupMinOrderDays", Long.valueOf(pmDefaultValue().get("defaultMinOrderDays")));
                            }
                            
                            if (UtilValidate.isNotEmpty(overRideDeclaredCn)) {
                                productImportCtx.put("declaredNameCn", overRideDeclaredCn);
                            }
                            
                            if (UtilValidate.isNotEmpty(overRideDeclaredEn)) {
                                productImportCtx.put("declaredNameEn", overRideDeclaredEn);
                            }
                            //default value == END
                            
                            String errorMessage = null;
                            if (errorNotes.toString().length() >= 253) {
                                errorMessage = errorNotes.toString().substring(1, 253);
                            } else {
                                errorMessage = errorNotes.toString();
                            }
                            
                            if (UtilValidate.isNotEmpty(errorMessage)) {
                                productImportCtx.put("importedDescription", errorMessage);
                            }
                            productImportCtx.put("errorNotes", errorMessage);
                            
                            String newProductImportId = delegator.getNextSeqId("ProductImportGudao");
                            productImportCtx.put("productImportId", newProductImportId);
                            productImportCtx.put("fileName", filePath);
                            productImportCtx.put("fileLineNumber", new BigDecimal(j));
                            productImportCtx.put("importedStatus", "N");
                            productImportCtx.put("userLogin", userLogin);
                            productImportCtx.put("createdBy", userLoginId);
                            productImportCtx.put("originalFilename", originalFilename);
                            //Debug.logError("Upload XLS run until here", module);
                            result = dispatcher.runSync("createProductImportGudao", productImportCtx);
                            if (ServiceUtil.isError(result)) {  //if result gives error -- START
                                result.put("errorMessageList", "ERROR");
                                return result;
                            }   //if result gives error -- END
                        }   //try -- second -- END
                        catch (Exception e) {
                            result = ServiceUtil.returnError(e.getMessage());
                            result.put("errorMessageList", "catch exception ERROR");
                            return result;
                        }
                    }   //if row indeed has data -- END
                }   //if row is not empty -- END
            }   //loop rows -- END
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
            result.put("errorMessageList", "ERROR");
            return result;
        }   //main try -- END
        return ServiceUtil.returnSuccess();
    }   //readProductXls
    
    private static Map<String, Object> readProductUpdateXls(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String filePath, String originalFilename) {   //readProductXls
        Map<String, Object> result = ServiceUtil.returnSuccess();
        List<String> errorList = FastList.newInstance();
        try {   //main try -- START
            String userLoginId = userLogin.getString("userLoginId");
            String userLoginPartyId = userLogin.getString("partyId");
            GenericValue partyGroup = userLogin.getRelatedOne("PartyGroup", false);
            
            String ownerGroupCheck = partyGroup.getString("officeSiteName");
            
            List<GenericValue> userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);
            
            boolean isSbu = false;
            boolean isAdmin = false;
            boolean isPlatform = false;
            List<GenericValue> partyRoleList = delegator.findByAnd("PartyRole", UtilMisc.toMap("partyId", userLoginPartyId, "roleTypeId", "SBU"), null, false);
            if (UtilValidate.isNotEmpty(partyRoleList)) {
                isSbu = true;
            }
            
            List<GenericValue> partyRoleListPlatform = delegator.findByAnd("PartyRole", UtilMisc.toMap("partyId", userLoginPartyId, "roleTypeId", "PLATFORM"), null, false);
            if (UtilValidate.isNotEmpty(partyRoleListPlatform)) {
                isPlatform = true;
            }
            
            if (UtilValidate.isNotEmpty(userLoginAdmin)) {
                isAdmin = true;
            }
            
            POIFSFileSystem fs = null;
            HSSFWorkbook wb = null;
            try {
                fs = new POIFSFileSystem(new FileInputStream(new File(filePath)));
                wb = new HSSFWorkbook(fs);
            } catch (IOException e) {
                Debug.logError("Unable to read workbook from file " + filePath, module);
                return ServiceUtil.returnError("Unable to read workbook from file " + filePath);
            }
            
            HSSFSheet sheet = wb.getSheetAt(0);
            int sheetLastRowNumber = sheet.getLastRowNum();
            HSSFRow firstRow = sheet.getRow(0);
            int minColIx = firstRow.getFirstCellNum();
            int maxColIx = firstRow.getLastCellNum();
            int colProductId = 0;
            int colCustomSku = 0;
            int colMotherSku = 0;
            int colChineseName = 0;
            int colEnglishName = 0;
            int colDeclaredNameCn = 0;
            int colDeclaredNameEn = 0;
            int colWeight = 0;
            int colActualWeight = 0;
            int colProductGroup = 0;
            /*int colEbayPlatformGroup = 0;
            int colWishPlatformGroup = 0;
            int colSmtPlatformGroup = 0;
            int colAmazonPlatformGroup = 0;
            int colLazadaPlatformGroup = 0;
            int colCdiscountPlatformGroup = 0;
            int colTophatterPlatformGroup = 0;*/
            int colStatusId = 0;
            int colEbayStatusId = 0;
            int colWishStatusId = 0;
            int colSmtStatusId = 0;
            int colAmazonStatusId = 0;
            int colLazadaStatusId = 0;
            int colCdiscountStatusId = 0;
            int colTophatterStatusId = 0;
            int colCategoryIdParent = 0;
            int colCategoryIdChild = 0;
            int colProductType = 0;
            int colOwnerGroup = 0;
            int colPurchaser = 0;
            int colImageLink = 0;
            int colExtraStockingDay = 0;
            int colSourcingDate = 0;
            int colUsd = 0;
            int colSupplier = 0;
            int colPrice = 0;
            int colActualPrice = 0;
            int colSupplierLink = 0;
            int colEta = 0;
            int colMinOrderDays = 0;
            int colMinimumOrderIdentifierA = 0;
            int colMinimumOrderQtyA = 0;
            int colMinimumOrderPriceA = 0;
            int colMinimumOrderUnitPriceA = 0;
            int colMinimumOrderIdentifierB = 0;
            int colMinimumOrderQtyB = 0;
            int colMinimumOrderPriceB = 0;
            int colMinimumOrderUnitPriceB = 0;
            int colMinimumOrderIdentifierC = 0;
            int colMinimumOrderQtyC = 0;
            int colMinimumOrderPriceC = 0;
            int colMinimumOrderUnitPriceC = 0;
            int colBackupSupplier = 0;
            int colBackupSupplierPrice = 0;
            int colBackupSupplierLink = 0;
            int colBackupSupplierEta = 0;
            int colBackupMinOrderDays = 0;
            int colBackupMinimumOrderIdentifierA = 0;
            int colBackupMinimumOrderQtyA = 0;
            int colBackupMinimumOrderPriceA = 0;
            int colBackupMinimumOrderUnitPriceA = 0;
            int colBackupMinimumOrderIdentifierB = 0;
            int colBackupMinimumOrderQtyB = 0;
            int colBackupMinimumOrderPriceB = 0;
            int colBackupMinimumOrderUnitPriceB = 0;
            int colBackupMinimumOrderIdentifierC = 0;
            int colBackupMinimumOrderQtyC = 0;
            int colBackupMinimumOrderPriceC = 0;
            int colBackupMinimumOrderUnitPriceC = 0;
            int colRivalItemId = 0;
            int colSimilarItemId = 0;
            int colNotes = 0;
            int colListingNotes = 0;
            int colUpc = 0;
            int colEbayUSListingStatus = 0;
            int colEbayUKListingStatus = 0;
            int colEbayAUListingStatus = 0;
            int colEbayCAListingStatus = 0;
            int colEbayDEListingStatus = 0;
            int colEbayFRListingStatus = 0;
            int colEbayESListingStatus = 0;
            int colSmtListingStatus = 0;
            int colAmaListingStatus = 0;
            int colWishListingStatus = 0;
            int colEbayPicture = 0;
            int colAmazonPicture = 0;
            int colFinePicture = 0;
            int colListingCheck = 0;
            int colSmtLink = 0;
            int colAmazonLink = 0;
            int colWishLink = 0;
            int colReferenceLink = 0;
            int colMotherSkuTemp = 0;
            int colGmsEbayUsAccount = 0;
            int colGmsEbayUkAccount = 0;
            int colGmsEbayAuAccount = 0;
            int colGmsEbayDeAccount = 0;
            int colGmsEbayFrAccount = 0;
            int colGmsWishAccount = 0;
            int colGmsSmtAccount = 0;
            int colGmsAmaUsAccount = 0;
            int colGmsAmaUkAccount = 0;
            int colGmsAmaCaAccount = 0;
            int colGmsAmaDeAccount = 0;
            int colGmsAmaFollow = 0;
            int colSetSku = 0;
            int colSetSkuQty = 0;
            int colSetSkuType = 0;
            int colListerPlatform = 0;
            int colLister = 0;
            int colPicReady = 0;
            int colRelationshipType = 0;
            int colQty = 0;
            int colOversea = 0;
            int colLength = 0;
            int colWidth = 0;
            int colHeight = 0;
            int colMainSupHolidayStart = 0;
            int colMainSupHolidayEnd = 0;
            int colBackupSupHolidayStart = 0;
            int colBackupSupHolidayEnd = 0;
            
            for (int colHead = minColIx; colHead < maxColIx; colHead++) {  //get column header -- START
                HSSFCell cellHead = firstRow.getCell(colHead);
                if(cellHead == null) {
                    continue;
                }
                cellHead.setCellType(HSSFCell.CELL_TYPE_STRING);
                String colHeader = cellHead.getRichStringCellValue().toString().toUpperCase().trim();
                
                if ("SKU".equals(colHeader) || "PRODUCT ID".equals(colHeader) || "PRODUCTID".equals(colHeader)) { //read column header data -- START
                    colProductId = colHead;
                } else if ("CUSTOM SKU".equals(colHeader) || "CUSTOMSKU".equals(colHeader) || "MY SKU".equals(colHeader) || "MYSKU".equals(colHeader) || "我的SKU".equals(colHeader) || "ALLYSKU".equals(colHeader) || "ALLY SKU".equals(colHeader)) {
                    colCustomSku = colHead;
                } else if ("MOTHER SKU".equals(colHeader) || "MOTHERSKU".equals(colHeader) || "母SKU".equals(colHeader) || "母 SKU".equals(colHeader) || "PARENTSKU".equals(colHeader) || "PARENT SKU".equals(colHeader)) {
                    colMotherSku = colHead;
                } else if ("CHINESE NAME".equals(colHeader) || "CHINESENAME".equals(colHeader) || "品名".equals(colHeader)) {
                    colChineseName = colHead;
                } else if ("ENGLISH NAME".equals(colHeader) || "ENGLISHNAME".equals(colHeader) || "英文品名".equals(colHeader)) {
                    colEnglishName = colHead;
                } else if ("DECLARED NAME CN".equals(colHeader) || "中文报关名".equals(colHeader)) {
                    colDeclaredNameCn = colHead;
                } else if ("DECLARED NAME EN".equals(colHeader) || "英文报关名".equals(colHeader)) {
                    colDeclaredNameEn = colHead;
                } else if ("重量".equals(colHeader) || "WEIGH".equals(colHeader) || "WEIGHT".equals(colHeader) || "估重".equals(colHeader)) {
                    colWeight = colHead;
                } else if ("实重OVERRIDE".equals(colHeader) || "ACTUAL WEIGH OVERRIDE".equals(colHeader) || "ACTUAL WEIGHT OVERRIDE".equals(colHeader) || "ACTUALWEIGHTOVERRIDE".equals(colHeader)) {
                    colActualWeight = colHead;
                } else if ("分组OVERRIDE".equals(colHeader) || "PRODUCT GROUP OVERRIDE".equals(colHeader) || "PRODUCTGROUPOVERRIDE".equals(colHeader)) {
                    colProductGroup = colHead;
                }/*  else if ("EBAY分组".equals(colHeader) || "EBAY PLATFORM GROUP".equals(colHeader) || "EBAY PRODUCT GROUP".equals(colHeader) || "EBAYPRODUCTGROUP".equals(colHeader)) {
                    colEbayPlatformGroup = colHead;
                } else if ("WISH分组".equals(colHeader) || "WISH PLATFORM GROUP".equals(colHeader) || "WISH PRODUCT GROUP".equals(colHeader) || "WISHPRODUCTGROUP".equals(colHeader)) {
                    colWishPlatformGroup = colHead;
                } else if ("SMT分组".equals(colHeader) || "SMT PLATFORM GROUP".equals(colHeader) || "SMT PRODUCT GROUP".equals(colHeader) || "SMTPRODUCTGROUP".equals(colHeader)) {
                    colSmtPlatformGroup = colHead;
                } else if ("AMAZON分组".equals(colHeader) || "AMAZON PLATFORM GROUP".equals(colHeader) || "AMAZON PRODUCT GROUP".equals(colHeader) || "AMAZONPRODUCTGROUP".equals(colHeader)) {
                    colAmazonPlatformGroup = colHead;
                } else if ("LAZADA分组".equals(colHeader) || "LAZADA PLATFORM GROUP".equals(colHeader) || "LAZADA PRODUCT GROUP".equals(colHeader) || "LAZADAPRODUCTGROUP".equals(colHeader)) {
                    colLazadaPlatformGroup = colHead;
                } else if ("CDISCOUNT分组".equals(colHeader) || "CDISCOUNT PLATFORM GROUP".equals(colHeader) || "CDISCOUNT PRODUCT GROUP".equals(colHeader) || "CDISCOUNTPRODUCTGROUP".equals(colHeader)) {
                    colCdiscountPlatformGroup = colHead;
                } else if ("TOPHATTER分组".equals(colHeader) || "TOPHATTER PLATFORM GROUP".equals(colHeader) || "TOPHATTER PRODUCT GROUP".equals(colHeader) || "TOPHATTERPRODUCTGROUP".equals(colHeader)) {
                    colTophatterPlatformGroup = colHead;
                }*/ else if ("状态".equals(colHeader) || "STATUS".equals(colHeader) || "STATUS ID".equals(colHeader) || "STATUSID".equals(colHeader)) {
                    colStatusId = colHead;
                } else if ("EBAY状态".equals(colHeader) || "EBAY STATUS".equals(colHeader) || "EBAY STATUS ID".equals(colHeader) || "EBAYSTATUSID".equals(colHeader)) {
                    colEbayStatusId = colHead;
                } else if ("WISH状态".equals(colHeader) || "WISH STATUS".equals(colHeader) || "WISH STATUS ID".equals(colHeader) || "WISHSTATUSID".equals(colHeader)) {
                    colWishStatusId = colHead;
                } else if ("SMT状态".equals(colHeader) || "SMT STATUS".equals(colHeader) || "SMT STATUS ID".equals(colHeader) || "SMTSTATUSID".equals(colHeader)) {
                    colSmtStatusId = colHead;
                } else if ("AMAZON状态".equals(colHeader) || "AMAZON STATUS".equals(colHeader) || "AMAZON STATUS ID".equals(colHeader) || "AMAZONSTATUSID".equals(colHeader)) {
                    colAmazonStatusId = colHead;
                } else if ("LAZADA状态".equals(colHeader) || "LAZADA STATUS".equals(colHeader) || "LAZADA STATUS ID".equals(colHeader) || "LAZADASTATUSID".equals(colHeader)) {
                    colLazadaStatusId = colHead;
                } else if ("CDISCOUNT状态".equals(colHeader) || "CDISCOUNT STATUS".equals(colHeader) || "CDISCOUNT STATUS ID".equals(colHeader) || "CDISCOUNTSTATUSID".equals(colHeader)) {
                    colCdiscountStatusId = colHead;
                } else if ("TOPHATTER状态".equals(colHeader) || "TOPHATTER STATUS".equals(colHeader) || "TOPHATTER STATUS ID".equals(colHeader) || "TOPHATTERSTATUSID".equals(colHeader)) {
                    colTophatterStatusId = colHead;
                } else if ("产品大类".equals(colHeader) || "CATEGORY ID PARENT".equals(colHeader) || "PARENTCATEGORYID".equals(colHeader) || "PARENT CATEGORY ID".equals(colHeader) || "CATEGORYIDPARENT".equals(colHeader)) {
                    colCategoryIdParent = colHead;
                } else if ("产品小类".equals(colHeader) || "CATEGORY ID CHILD".equals(colHeader) || "CHILDCATEGORYID".equals(colHeader) || "CHILD CATEGORY ID".equals(colHeader) || "CATEGORYIDCHILD".equals(colHeader)) {
                    colCategoryIdChild = colHead;
                } else if ("监控3".equals(colHeader) || "产品性质".equals(colHeader) || "TYPE".equals(colHeader) || "PRODUCTTYPE".equals(colHeader) || "PRODUCT TYPE".equals(colHeader) || "产品类型".equals(colHeader) || "类型".equals(colHeader)) {
                    colProductType = colHead;
                } else if ("开发员OVERRIDE".equals(colHeader) || "事业部OVERRIDE".equals(colHeader) || "OWNER GROUP OVERRIDE".equals(colHeader) || "OWNERGROUPOVERRIDE".equals(colHeader)) {
                    colOwnerGroup = colHead;
                } else if ("采购员OVERRIDE".equals(colHeader) || "采购OVERRIDE".equals(colHeader) || "PURCHASER OVERRIDE".equals(colHeader) || "PURCHASEROVERRIDE".equals(colHeader)) {
                    colPurchaser = colHead;
                } else if ("截图链接".equals(colHeader) || "图片链接".equals(colHeader) || "图片".equals(colHeader) || "IMAGE".equals(colHeader) || "IMAGELINK".equals(colHeader) || "IMAGE LINK".equals(colHeader)) {
                    colImageLink = colHead;
                } else if ("备货天数OVERRIDE".equals(colHeader) || "EXTRA STOCKING DAY OVERRIDE".equals(colHeader) || "EXTRASTOCKINGDAYOVERRIDE".equals(colHeader) || "EXTRASTOCKINGDAYSOVERRIDE".equals(colHeader)) {
                    colExtraStockingDay = colHead;
                } else if ("开发日期OVERRIDE".equals(colHeader) || "SOURCING DATE OVERRIDE".equals(colHeader)|| "SOURCINGDATEOVERRIDE".equals(colHeader)) {
                    colSourcingDate = colHead;
                } else if ("USD".equals(colHeader) || "US".equals(colHeader) || "CP".equals(colHeader)) {
                    colUsd = colHead;
                } else if ("供应商".equals(colHeader) || "SUPPLIER".equals(colHeader)) {
                    colSupplier = colHead;
                } else if ("原始成本".equals(colHeader)) {
                    colPrice = colHead;
                } else if ("实际成本OVERRIDE".equals(colHeader)) {
                    colActualPrice = colHead;
                } else if ("国内".equals(colHeader) || "采购链接".equals(colHeader) || "供应商链接".equals(colHeader) || "SUPPLIER LINK".equals(colHeader) || "SUPPLIERLINK".equals(colHeader)) {
                    colSupplierLink = colHead;
                } else if ("ETA".equals(colHeader)) {
                    colEta = colHead;
                } else if ("采购天数OVERRIDE".equals(colHeader) || "MIN ORDER DAYS OVERRIDE".equals(colHeader) || "MINORDERDAYSOVERRIDE".equals(colHeader)) {
                    colMinOrderDays = colHead;
                } else if ("起订量1-关联符".equals(colHeader)) {
                    colMinimumOrderIdentifierA = colHead;
                } else if ("起订量1-数量".equals(colHeader)) {
                    colMinimumOrderQtyA = colHead;
                } else if ("起订量1-金额".equals(colHeader)) {
                    colMinimumOrderPriceA = colHead;
                } else if ("起订量1-成本".equals(colHeader)) {
                    colMinimumOrderUnitPriceA = colHead;
                } else if ("起订量2-关联符".equals(colHeader)) {
                    colMinimumOrderIdentifierB = colHead;
                } else if ("起订量2-数量".equals(colHeader)) {
                    colMinimumOrderQtyB = colHead;
                } else if ("起订量2-金额".equals(colHeader)) {
                    colMinimumOrderPriceB = colHead;
                } else if ("起订量2-成本".equals(colHeader)) {
                    colMinimumOrderUnitPriceB = colHead;
                } else if ("起订量3-关联符".equals(colHeader)) {
                    colMinimumOrderIdentifierC = colHead;
                } else if ("起订量3-数量".equals(colHeader)) {
                    colMinimumOrderQtyC = colHead;
                } else if ("起订量3-金额".equals(colHeader)) {
                    colMinimumOrderPriceC = colHead;
                } else if ("起订量3-成本".equals(colHeader)) {
                    colMinimumOrderUnitPriceC = colHead;
                } else if ("备用供应商".equals(colHeader) || "BACKUP SUPPLIER".equals(colHeader)) {
                    colBackupSupplier = colHead;
                } else if ("备用采购链接".equals(colHeader) || "BACKUP SUPPLIER LINK".equals(colHeader)) {
                    colBackupSupplierLink = colHead;
                } else if ("备用原始成本".equals(colHeader) || "BACKUP SUPPLIER PRICE".equals(colHeader)) {
                    colBackupSupplierPrice = colHead;
                } else if ("备用ETA".equals(colHeader) || "BACKUP SUPPLIER ETA".equals(colHeader)) {
                    colBackupSupplierEta = colHead;
                } else if ("备用采购天数OVERRIDE".equals(colHeader) || "BACKUP SUPPLIER MIN ORDER DAYS OVERRIDE".equals(colHeader)) {
                    colBackupMinOrderDays = colHead;
                } else if ("备用起订量1-关联符".equals(colHeader)) {
                    colBackupMinimumOrderIdentifierA = colHead;
                } else if ("备用起订量1-数量".equals(colHeader)) {
                    colBackupMinimumOrderQtyA = colHead;
                } else if ("备用起订量1-金额".equals(colHeader)) {
                    colBackupMinimumOrderPriceA = colHead;
                } else if ("备用起订量1-成本".equals(colHeader)) {
                    colBackupMinimumOrderUnitPriceA = colHead;
                } else if ("备用起订量2-关联符".equals(colHeader)) {
                    colBackupMinimumOrderIdentifierB = colHead;
                } else if ("备用起订量2-数量".equals(colHeader)) {
                    colBackupMinimumOrderQtyB = colHead;
                } else if ("备用起订量2-金额".equals(colHeader)) {
                    colBackupMinimumOrderPriceB = colHead;
                } else if ("备用起订量2-成本".equals(colHeader)) {
                    colBackupMinimumOrderUnitPriceB = colHead;
                } else if ("备用起订量3-关联符".equals(colHeader)) {
                    colBackupMinimumOrderIdentifierC = colHead;
                } else if ("备用起订量3-数量".equals(colHeader)) {
                    colBackupMinimumOrderQtyC = colHead;
                } else if ("备用起订量3-金额".equals(colHeader)) {
                    colBackupMinimumOrderPriceC = colHead;
                } else if ("备用起订量3-成本".equals(colHeader)) {
                    colBackupMinimumOrderUnitPriceC = colHead;
                } else if ("IDENTICAL ITEM ID".equals(colHeader) || "相同对手".equals(colHeader) || "EBAY相同对手".equals(colHeader) || "EBAY链接".equals(colHeader)) {
                    colRivalItemId = colHead;
                } else if ("SIMILAR ITEM ID".equals(colHeader) || "相似对手".equals(colHeader) || "EBAY相似对手".equals(colHeader)) {
                    colSimilarItemId = colHead;
                } else if ("采购备注".equals(colHeader) || "开发备注".equals(colHeader) || "PURCHASING NOTES".equals(colHeader) || "PURCHASING NOTES".equals(colHeader)) {
                    colNotes = colHead;
                } else if ("销售备注".equals(colHeader) || "LISTING NOTES".equals(colHeader)) {
                    colListingNotes = colHead;
                } else if ("UPC OVERRIDE".equals(colHeader) || "UNIVERSAL PRODUCT CODE OVERRIDE".equals(colHeader)) {
                    colUpc = colHead;
                } else if ("EBAYUSLISTING".equals(colHeader) || "EBAY US LISTING".equals(colHeader) || "US LISTING".equals(colHeader) || "美国LISTING".equals(colHeader) || "美国".equals(colHeader) || "US SITE".equals(colHeader)) {
                    colEbayUSListingStatus = colHead;
                } else if ("EBAYUKLISTING".equals(colHeader) || "EBAY UK LISTING".equals(colHeader) || "UK LISTING".equals(colHeader) || "英国LISTING".equals(colHeader) || "英国".equals(colHeader) || "UK SITE".equals(colHeader)) {
                    colEbayUKListingStatus = colHead;
                } else if ("EBAYAULISTING".equals(colHeader) || "EBAY AU LISTING".equals(colHeader) || "AU LISTING".equals(colHeader) || "澳大利亚LISTING".equals(colHeader) || "澳大利亚".equals(colHeader) || "AU SITE".equals(colHeader)) {
                    colEbayAUListingStatus = colHead;
                } else if ("EBAYCALISTING".equals(colHeader) || "EBAY CA LISTING".equals(colHeader) || "CA LISTING".equals(colHeader) || "加拿大LISTING".equals(colHeader) || "加拿大".equals(colHeader) || "CA SITE".equals(colHeader)) {
                    colEbayCAListingStatus = colHead;
                } else if ("EBAYDELISTING".equals(colHeader) || "EBAY DE LISTING".equals(colHeader) || "DE LISTING".equals(colHeader) || "德国LISTING".equals(colHeader) || "德国".equals(colHeader) || "DE SITE".equals(colHeader)) {
                    colEbayDEListingStatus = colHead;
                } else if ("EBAYFRLISTING".equals(colHeader) || "EBAY FR LISTING".equals(colHeader) || "FR LISTING".equals(colHeader) || "法国LISTING".equals(colHeader) || "法国".equals(colHeader) || "FR SITE".equals(colHeader)) {
                    colEbayFRListingStatus = colHead;
                } else if ("EBAYESLISTING".equals(colHeader) || "EBAY ES LISTING".equals(colHeader) || "ES LISTING".equals(colHeader) || "西班牙LISTING".equals(colHeader) || "西班牙".equals(colHeader) || "ES SITE".equals(colHeader)) {
                    colEbayESListingStatus = colHead;
                } else if ("SMTLISTING".equals(colHeader) || "SMT LISTING".equals(colHeader) || "ALIEXPRESS LISTING".equals(colHeader) || "ALIEXPRESSLISTING".equals(colHeader)) {
                    colSmtListingStatus = colHead;
                } else if ("AMALISTING".equals(colHeader) || "AMA LISTING".equals(colHeader) || "AMAZON LISTING".equals(colHeader) || "AMAZONLISTING".equals(colHeader)) {
                    colAmaListingStatus = colHead;
                } else if ("WISHLISTING".equals(colHeader) || "WISH LISTING".equals(colHeader)) {
                    colWishListingStatus = colHead;
                } else if ("EBAYPICTUREISONLY".equals(colHeader) || "EBAY PICTUREISONLY".equals(colHeader) || "EBAY图片ISONLY".equals(colHeader)) {
                    colEbayPicture = colHead;
                } else if ("AMAZONPICTUREISONLY".equals(colHeader) || "AMAZON PICTUREISONLY".equals(colHeader) || "AMA图片ISONLY".equals(colHeader) || "AMAZON图片ISONLY".equals(colHeader)) {
                    colAmazonPicture = colHead;
                } else if ("FINEPICTUREISONLY".equals(colHeader) || "FINE PICTUREISONLY".equals(colHeader) || "无印良品ISONLY".equals(colHeader)) {
                    colFinePicture = colHead;
                } else if ("LISTINGCHECK".equals(colHeader) || "LISTING CHECK".equals(colHeader) || "CHECK LISTING".equals(colHeader) || "CHECK LISTING".equals(colHeader) || "EBAY母版复查".equals(colHeader) || "母版复查".equals(colHeader)) {
                    colListingCheck = colHead;
                } else if ("SMTLINK".equals(colHeader) || "SMT LINK".equals(colHeader) || "SMT链接".equals(colHeader)) {
                    colSmtLink = colHead;
                } else if ("WISHLINK".equals(colHeader) || "WISH LINK".equals(colHeader) || "WISH链接".equals(colHeader)) {
                    colWishLink = colHead;
                } else if ("AMAZONLINK".equals(colHeader) || "AMAZON LINK".equals(colHeader) || "AMAZON链接".equals(colHeader) || "AMA链接".equals(colHeader) || "AMALINK".equals(colHeader) || "AMA LINK".equals(colHeader)) {
                    colAmazonLink = colHead;
                } else if ("REFERENCELINK".equals(colHeader) || "REFERENCE LINK".equals(colHeader) || "参考链接".equals(colHeader) || "REF链接".equals(colHeader)) {
                    colReferenceLink = colHead;
                } else if ("MOTHERSKUTEMP".equals(colHeader) || "MOTHER SKU TEMP".equals(colHeader) || "母SKU TEMP".equals(colHeader) || "PARENTSKUTEMP".equals(colHeader) || "PARENT SKUTEMP".equals(colHeader)) {
                    colMotherSkuTemp = colHead;
                } else if ("SMT ACCOUNT".equals(colHeader) || "SMTACCOUNT".equals(colHeader) || "SMT账号".equals(colHeader) || "SMT 账号".equals(colHeader)) {
                    colGmsSmtAccount = colHead;
                } else if ("WISH ACCOUNT".equals(colHeader) || "WISHACCOUNT".equals(colHeader) || "WISH账号".equals(colHeader) || "WISH 账号".equals(colHeader)) {
                    colGmsWishAccount = colHead;
                } else if ("EBAY US ACCOUNT".equals(colHeader) || "EBAYUSACCOUNT".equals(colHeader) || "EBAYUS账号".equals(colHeader) || "EBAYUS 账号".equals(colHeader) || "EBAY美国站点".equals(colHeader) || "EBAY US站点".equals(colHeader)) {
                    colGmsEbayUsAccount = colHead;
                } else if ("EBAY UK ACCOUNT".equals(colHeader) || "EBAYUKACCOUNT".equals(colHeader) || "EBAYUK账号".equals(colHeader) || "EBAYUK 账号".equals(colHeader) || "EBAY英国站点".equals(colHeader) || "EBAY UK站点".equals(colHeader)) {
                    colGmsEbayUkAccount = colHead;
                } else if ("EBAY AU ACCOUNT".equals(colHeader) || "EBAYAUACCOUNT".equals(colHeader) || "EBAYAU账号".equals(colHeader) || "EBAYAU 账号".equals(colHeader) || "EBAY澳大利啊站点".equals(colHeader) || "EBAY AU站点".equals(colHeader)) {
                    colGmsEbayAuAccount = colHead;
                } else if ("EBAY DE ACCOUNT".equals(colHeader) || "EBAYDEACCOUNT".equals(colHeader) || "EBAYDE账号".equals(colHeader) || "EBAYDE 账号".equals(colHeader) || "EBAY德国站点".equals(colHeader) || "EBAY DE站点".equals(colHeader)) {
                    colGmsEbayDeAccount = colHead;
                } else if ("EBAY FR ACCOUNT".equals(colHeader) || "EBAYFRACCOUNT".equals(colHeader) || "EBAYFR账号".equals(colHeader) || "EBAYFR 账号".equals(colHeader) || "EBAY法国站点".equals(colHeader) || "EBAY FR站点".equals(colHeader)) {
                    colGmsEbayFrAccount = colHead;
                } else if ("AMAZON US ACCOUNT".equals(colHeader) || "AMAZONUSACCOUNT".equals(colHeader) || "AMAZON US 账号".equals(colHeader) || "AMAZON US账号".equals(colHeader) || "AMAZONUS账号".equals(colHeader) || "AMAZON 美国账号".equals(colHeader) || "AMAZON美国账号".equals(colHeader) || "AMAZON美国".equals(colHeader) || "AMAZON美国站点".equals(colHeader)) {
                    colGmsAmaUsAccount = colHead;
                } else if ("AMAZON UK ACCOUNT".equals(colHeader) || "AMAZONUKACCOUNT".equals(colHeader) || "AMAZON UK 账号".equals(colHeader) || "AMAZON UK账号".equals(colHeader) || "AMAZONUK账号".equals(colHeader) || "AMAZON 英国账号".equals(colHeader) || "AMAZON英国账号".equals(colHeader) || "AMAZON英国".equals(colHeader) || "AMAZON英国站点".equals(colHeader)) {
                    colGmsAmaUkAccount = colHead;
                } else if ("AMAZON CA ACCOUNT".equals(colHeader) || "AMAZONCAACCOUNT".equals(colHeader) || "AMAZON CA 账号".equals(colHeader) || "AMAZON CA账号".equals(colHeader) || "AMAZONCA账号".equals(colHeader) || "AMAZON 加拿大账号".equals(colHeader) || "AMAZON加拿大账号".equals(colHeader) || "AMAZON加拿大".equals(colHeader) || "AMAZON加拿大站点".equals(colHeader)) {
                    colGmsAmaCaAccount = colHead;
                } else if ("AMAZON DE ACCOUNT".equals(colHeader) || "AMAZONDEACCOUNT".equals(colHeader) || "AMAZON DE 账号".equals(colHeader) || "AMAZON DE账号".equals(colHeader) || "AMAZONDE账号".equals(colHeader) || "AMAZON 德国账号".equals(colHeader) || "AMAZON德国账号".equals(colHeader) || "AMAZON德国".equals(colHeader) || "AMAZON德国站点".equals(colHeader)) {
                    colGmsAmaDeAccount = colHead;
                } else if ("AMAZON FOLLOW".equals(colHeader) || "AMAZONFOLLOW".equals(colHeader) || "AMAZON 跟踪".equals(colHeader) || "AMAZON跟踪".equals(colHeader) || "AMAZON跟卖".equals(colHeader)) {
                    colGmsAmaFollow = colHead;
                } else if ("SET SKU".equals(colHeader) || "SETSKU".equals(colHeader) || "绑定SKU".equals(colHeader) || "组合SKU".equals(colHeader)) {
                    colSetSku = colHead;
                } else if ("SET SKU QTY".equals(colHeader) || "SETSKUQTY".equals(colHeader) || "绑定SKU数量".equals(colHeader)) {
                    colSetSkuQty = colHead;
                } else if ("SET SKU TYPE".equals(colHeader) || "SETSKUTYPE".equals(colHeader) || "变体".equals(colHeader) || "变体SKU".equals(colHeader)) {
                    colSetSkuType = colHead;
                } else if ("LISTERPLATFORM".equals(colHeader) || "LISTER PLATFORM".equals(colHeader) || "上架平台".equals(colHeader)) {
                    colListerPlatform = colHead;
                } else if ("LISTER".equals(colHeader) || "上架员".equals(colHeader)) {
                    colLister = colHead;
                } else if ("PICREADY".equals(colHeader) || "PIC READY".equals(colHeader) || "PICTURE READY".equals(colHeader) || "PICTUREREADY".equals(colHeader)) {
                    colPicReady = colHead;
                } else if ("RELATIONSHIP".equals(colHeader) || "从属类型".equals(colHeader)) {
                    colRelationshipType = colHead;
                } else if ("QUANTITY".equals(colHeader) || "QTY".equals(colHeader) || "数量".equals(colHeader) || "组合SKU数量".equals(colHeader)) {
                    colQty = colHead;
                } else if ("OVERSEA".equals(colHeader) || "海外仓".equals(colHeader)) {
                    colOversea = colHead;
                } else if ("LENGTH".equals(colHeader) || "长度(cm)".equals(colHeader)) {
                    colLength = colHead;
                } else if ("WIDTH".equals(colHeader) || "宽度(cm)".equals(colHeader)) {
                    colWidth = colHead;
                } else if ("HEIGHT".equals(colHeader) || "高度(cm)".equals(colHeader)) {
                    colHeight = colHead;
                } else if ("SUPPLIER HOLIDAY START".equals(colHeader) || "供应商放假开始".equals(colHeader)) {
                    colMainSupHolidayStart = colHead;
                } else if ("SUPPLIER HOLIDAY END".equals(colHeader) || "供应商放假结束".equals(colHeader)) {
                    colMainSupHolidayEnd = colHead;
                } else if ("BACKUP SUPPLIER HOLIDAY START".equals(colHeader) || "备用供应商放假开始".equals(colHeader)) {
                    colBackupSupHolidayStart = colHead;
                } else if ("BACKUP SUPPLIER HOLIDAY START".equals(colHeader) || "备用供应商放假开始".equals(colHeader)) {
                    colBackupSupHolidayEnd = colHead;
                }
                //read column header data -- END
            }   //get column header -- END
            
            //get ProductStatusList == START
            List<String> statusList = new ArrayList<String>();
            List<GenericValue> enumerationStatusList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "PM_STATUS"), null, true);
            for (GenericValue pmStatus : enumerationStatusList) {
                statusList.add(pmStatus.getString("enumCode"));
            }
            //get ProductStatusList == END
            
            //get PlatformStatusList == START
            List<String> platformStatusList = new ArrayList<String>();
            List<GenericValue> enumerationPlatformStatusList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "PLATFORM_STATUS"), null, true);
            for (GenericValue platformStatus : enumerationPlatformStatusList) {
                platformStatusList.add(platformStatus.getString("enumCode"));
            }
            //get PlatformStatusList == END
            
            //get ProductGroupList == START
            List<String> productGroupList = new ArrayList<String>();
            List<GenericValue> enumerationProductGroupList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "GUDAO_PRODUCT_GROUP"), null, true);
            for (GenericValue pmProductGroup : enumerationProductGroupList) {
                productGroupList.add(pmProductGroup.getString("enumCode"));
            }
            //get ProductStatusList == END
            
            //get productTypeList == START
            List<String> productTypeList = new ArrayList<String>();
            List<GenericValue> enumerationProductTypeList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "GUDAO_PRODUCT_TYPE"), null, true);
            for (GenericValue pmProductType : enumerationProductTypeList) {
                productTypeList.add(pmProductType.getString("enumCode"));
            }
            //get productTypeList == END
            
            //get ownerGroupList == START
            List<String> ownerGroupList = new ArrayList<String>();
            List<GenericValue> enumerationOwnerGroupList = delegator.findByAnd("PartyRole", UtilMisc.toMap("roleTypeId", "SBU"), null, true);
            for (GenericValue pmOwnerGroup : enumerationOwnerGroupList) {
                GenericValue partyGroupOwner = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", pmOwnerGroup.getString("partyId")), false);
                ownerGroupList.add(partyGroupOwner.getString("officeSiteName"));
            }
            //get ownerGroupList == END
            
            //get platform Status LISTED == START
            List<String> g3PlatformStatusList = new ArrayList<String>();
            g3PlatformStatusList.add("重点维护");
            g3PlatformStatusList.add("正常在售");
            g3PlatformStatusList.add("EN不上");
            g3PlatformStatusList.add("EU不上");
            g3PlatformStatusList.add("AU不上");
            g3PlatformStatusList.add("UK不上");
            g3PlatformStatusList.add("US不上");
            g3PlatformStatusList.add("KA不上");
            //get platform Status LISTED == END
            
            //get platform Status LISTED == START
            List<String> g4PlatformStatusList = new ArrayList<String>();
            g4PlatformStatusList.add("侵权下架");
            g4PlatformStatusList.add("断货下架");
            g4PlatformStatusList.add("滞销下架");
            g4PlatformStatusList.add("问题下架");
            g4PlatformStatusList.add("风险下架");
            //get platform Status LISTED == END
            
            for (int j = 1; j <= sheetLastRowNumber; j++) { //loop rows -- START
                HSSFRow row = sheet.getRow(j);
                if (UtilValidate.isNotEmpty(row)) {  //if row is not empty -- START
                    Map<String, Object> productImportCtx = FastMap.newInstance();
                    boolean updateProductImport = true;
                    boolean updateGms = false;
                    boolean updateSetSku = false;
                    boolean updateProductRelationship = false;
                    String productId = null;
                    String motherSkuTemp = null;
                    String ebayPicture = null;
                    String amazonPicture = null;
                    String statusId = null;
                    String ebayStatusId = null;
                    String wishStatusId = null;
                    String smtStatusId = null;
                    String amazonStatusId = null;
                    String lazadaStatusId = null;
                    String cdiscountStatusId = null;
                    String tophatterStatusId = null;
                    String productGroup = null;
                    /*String ebayPlatformGroup = null;
                    String wishPlatformGroup = null;
                    String smtPlatformGroup = null;
                    String amazonPlatformGroup = null;
                    String lazadaPlatformGroup = null;
                    String cdiscountPlatformGroup = null;
                    String tophatterPlatformGroup = null;*/
                    String gmsEbayUsAccount = null;
                    String gmsEbayUkAccount = null;
                    String gmsEbayAuAccount = null;
                    String gmsEbayDeAccount = null;
                    String gmsEbayFrAccount = null;
                    String gmsSmtAccount = null;
                    String gmsWishAccount = null;
                    String gmsAmaUsAccount = null;
                    String gmsAmaUkAccount = null;
                    String gmsAmaCaAccount = null;
                    String gmsAmaDeAccount = null;
                    long gmsAmaFollow = 0;
                    StringBuffer errorNotes = new StringBuffer();
                    String setSku = null;
                    long setSkuQty = 1;
                    String setSkuType = "SET";
                    String parentCategoryName = null;
                    String childrenCategoryName = null;
                    String overRideDeclaredCn = null;
                    String overRideDeclaredEn = null;
                    String customSku = null;
                    String motherSku = null;
                    String chineseName = null;
                    String englishName = null;
                    double weight = 0.0;
                    double actualWeight = 0.0;
                    String productType = null;
                    String ownerGroup = null;
                    String imageLink = null;
                    BigDecimal priceUsd = BigDecimal.ZERO;
                    String supplier = null;
                    String supplierLink = null;
                    BigDecimal price = BigDecimal.ZERO;
                    BigDecimal actualPrice = BigDecimal.ZERO;
                    long eta = 0;
                    long extraStockingDay = 0;
                    long minOrderDays = 0;
                    String moIdentifierA = null;
                    long moQtyA = 0;
                    BigDecimal moPriceA = BigDecimal.ZERO;
                    BigDecimal moUnitPriceA = BigDecimal.ZERO;
                    String moIdentifierB = null;
                    long moQtyB = 0;
                    BigDecimal moPriceB = BigDecimal.ZERO;
                    BigDecimal moUnitPriceB = BigDecimal.ZERO;
                    String moIdentifierC = null;
                    long moQtyC = 0;
                    BigDecimal moPriceC = BigDecimal.ZERO;
                    BigDecimal moUnitPriceC = BigDecimal.ZERO;
                    String backupSupplier = null;
                    String backupSupplierLink = null;
                    BigDecimal backupSupplierPrice = BigDecimal.ZERO;
                    long backupSupplierEta = 0;
                    long backupMinOrderDays = 0;
                    String backupMoIdentifierA = null;
                    long backupMoQtyA = 0;
                    BigDecimal backupMoPriceA = BigDecimal.ZERO;
                    BigDecimal backupMoUnitPriceA = BigDecimal.ZERO;
                    String backupMoIdentifierB = null;
                    long backupMoQtyB = 0;
                    BigDecimal backupMoPriceB = BigDecimal.ZERO;
                    BigDecimal backupMoUnitPriceB = BigDecimal.ZERO;
                    String backupMoIdentifierC = null;
                    long backupMoQtyC = 0;
                    BigDecimal backupMoPriceC = BigDecimal.ZERO;
                    BigDecimal backupMoUnitPriceC = BigDecimal.ZERO;
                    String rivalItemId = null;
                    String similarItemId = null;
                    String notes = null;
                    String listingNotes = null;
                    String smtLink = null;
                    String wishLink = null;
                    String amazonLink = null;
                    String referenceLink = null;
                    String picReady = null;
                    boolean changePlatformStatus = false;
                    String relationshipType = null;
                    long quantity = 0;
                    String oversea = null;
                    double length = 0.0;
                    double width = 0.0;
                    double height = 0.0;
                    
                    for(int colIx = minColIx; colIx < maxColIx; colIx++) {    //loop cell in a row -- START
                        HSSFCell cell = row.getCell(colIx);
                        if (cell == null) {
                            continue;
                        }
                        
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        String dataString = cell.getRichStringCellValue().toString().trim();
                        
                        if (colIx == (colProductId)) { //read record -- START
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (UtilValidate.isNotEmpty(dataString)) {
                                    if (dataString.contains(" ")) {
                                        dataString = dataString.replaceAll(" ","");
                                    }
                                    if (dataString.length() > 12) {
                                        errorNotes.append("SKU is too long - SKU太长.不能超过12字.");
                                    }
                                    if (dataString.length() < 7) {
                                        errorNotes.append("SKU is too short - SKU太短.最短7字.");
                                    }
                                    productId = dataString;
                                    if (productId.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("SKU contains invalid character - SKU包含非法字符;");
                                    }
                                } else {   //break when there is no record anymore -- START
                                    updateProductImport = false;
                                    break;
                                }   //break when there is no record anymore -- END
                            } else {   //break when there is no record anymore -- START
                                updateProductImport = false;
                                break;
                            }   //break when there is no record anymore -- END
                        } else if (colIx == (colCustomSku)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("customSku is too long - AllySKU太长.不能超过255字;");
                                } else {
                                    customSku = dataString;
                                    if (customSku.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("customSku contains invalid character - AllySKU包含非法字符;");
                                    }
                                }
                            }
                        } else if (colIx == (colMotherSku)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("motherSku is too long - 母SKU太长.不能超过255字;");
                                } else {
                                    motherSku = dataString;
                                    if (motherSku.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("motherSku contains invalid character - 母SKU包含非法字符;");
                                    }
                                }
                            }
                        } else if (colIx == (colChineseName)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("chineseName is too long - 中文名字太长.不能超过255字;");
                                } else {
                                    chineseName = dataString;
                                }
                            }
                        } else if (colIx == (colEnglishName)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("englishName is too long - 英文名字太长.不能超过255字;");
                                } else {
                                    englishName = dataString;
                                }
                            }
                        } else if (colIx == (colDeclaredNameCn)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("DeclaredCn is too long - DeclaredCn太长.不能超过255字;");
                                } else {
                                    overRideDeclaredCn = dataString;
                                }
                            }
                        } else if (colIx == (colDeclaredNameEn)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("DeclaredEn is too long - DeclaredEn太长.不能超过255字;");
                                } else {
                                    overRideDeclaredEn = dataString;
                                }
                            }
                        } else if (colIx == (colWeight)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("weight is too long - 估重太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("weight contains invalid character - 估重包含非法字符;");
                                    } else {
                                        weight = Double.parseDouble(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colActualWeight)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("actual weight is too long - 实重太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("actual Weight contains invalid character - 实重包含非法字符;");
                                    } else {
                                        actualWeight = Double.parseDouble(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colProductGroup)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("productGroup is too long - 分组太长.不能超过255字;");
                                } else {
                                    productGroup = dataString.toUpperCase();
                                    if (!productGroupList.contains(productGroup)) {
                                        errorNotes.append("productGroup contains invalid character - 分组不正确;");
                                    }
                                }
                            }
                        } else if (colIx == (colStatusId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("statusId is too long - 状态太长.不能超过255字;");
                                } else {
                                    statusId = dataString.toUpperCase();
                                    if (!statusList.contains(statusId)) {
                                        errorNotes.append("statusId contains invalid character - 状态不正确;");
                                    }
                                }
                            }
                        } else if (colIx == (colEbayStatusId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("ebayStatusId is too long - ebay状态太长.不能超过255字;");
                                } else {
                                    ebayStatusId = dataString.toUpperCase();
                                    if (!platformStatusList.contains(ebayStatusId)) {
                                        errorNotes.append("ebayStatusId contains invalid character - ebay状态不正确;");
                                    } else {
                                        changePlatformStatus = true;
                                    }
                                }
                            }
                        } else if (colIx == (colWishStatusId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("wishStatusId is too long - WISH状态太长.不能超过255字;");
                                } else {
                                    wishStatusId = dataString.toUpperCase();
                                    if (!platformStatusList.contains(wishStatusId)) {
                                        errorNotes.append("wishStatusId contains invalid character - WISH状态不正确;");
                                    } else {
                                        changePlatformStatus = true;
                                    }
                                }
                            }
                        } else if (colIx == (colSmtStatusId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("smtStatusId is too long - SMT状态太长.不能超过255字;");
                                } else {
                                    smtStatusId = dataString.toUpperCase();
                                    if (!platformStatusList.contains(smtStatusId)) {
                                        errorNotes.append("smtStatusId contains invalid character - SMT状态不正确;");
                                    } else {
                                        changePlatformStatus = true;
                                    }
                                }
                            }
                        } else if (colIx == (colAmazonStatusId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("amazonStatusId is too long - Amazon状态太长.不能超过255字;");
                                } else {
                                    amazonStatusId = dataString.toUpperCase();
                                    if (!platformStatusList.contains(amazonStatusId)) {
                                        errorNotes.append("amazonStatusId contains invalid character - Amazon状态不正确;");
                                    } else {
                                        changePlatformStatus = true;
                                    }
                                }
                            }
                        } else if (colIx == (colLazadaStatusId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("lazadaStatusId is too long - Lazada状态太长.不能超过255字;");
                                } else {
                                    lazadaStatusId = dataString.toUpperCase();
                                    if (!platformStatusList.contains(lazadaStatusId)) {
                                        errorNotes.append("lazadaStatusId contains invalid character - Lazada状态不正确;");
                                    } else {
                                        changePlatformStatus = true;
                                    }
                                }
                            }
                        } else if (colIx == (colCdiscountStatusId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("cdiscountStatusId is too long - WISH状态太长.不能超过255字;");
                                } else {
                                    cdiscountStatusId = dataString.toUpperCase();
                                    if (!platformStatusList.contains(cdiscountStatusId)) {
                                        errorNotes.append("cdiscountStatusId contains invalid character - Cdiscount状态不正确;");
                                    } else {
                                        changePlatformStatus = true;
                                    }
                                }
                            }
                        } else if (colIx == (colTophatterStatusId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("tophatterStatusId is too long - Tophatter状态太长.不能超过255字;");
                                } else {
                                    tophatterStatusId = dataString.toUpperCase();
                                    if (!platformStatusList.contains(tophatterStatusId)) {
                                        errorNotes.append("tophatterStatusId contains invalid character - Tophatter状态不正确;");
                                    } else {
                                        changePlatformStatus = true;
                                    }
                                }
                            }
                        } else if (colIx == (colCategoryIdParent)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("parentCategoryName is too long - 大目录太长.不能超过255字;");
                                } else {
                                    parentCategoryName = dataString;
                                }
                            }
                        } else if (colIx == (colCategoryIdChild)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("childrenCategoryName is too long - 小目录太长.不能超过255字;");
                                } else {
                                    childrenCategoryName = dataString;
                                }
                            }
                        } else if (colIx == (colProductType)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("productType is too long - 产品性质太长.不能超过255字;");
                                } else {
                                    productType = dataString.toUpperCase();
                                    if (!productTypeList.contains(dataString.toUpperCase())) {
                                        errorNotes.append("productType is invalid - 产品性质不正确;");
                                    }
                                }
                            }
                        } else if (colIx == (colOwnerGroup)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("ownerGroup is too long - 事业部太长.不能超过255字;");
                                } else {
                                    ownerGroup = dataString.toUpperCase();
                                    if (!ownerGroupList.contains(dataString.toUpperCase())) {
                                        errorNotes.append("ownerGroup is invalid - 事业部不正确;");
                                    }
                                }
                            }
                        } else if (colIx == (colPurchaser)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("purchaser is too long - purchaser太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("purchaser", dataString);
                                }
                            }
                        } else if (colIx == (colExtraStockingDay)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("extraStockingDay is too long - 太长.不能超过255字;");
                                } else {
                                    extraStockingDay = Long.valueOf(dataString);
                                }
                            }
                        } else if (colIx == (colSourcingDate)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("sourcingDate is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("sourcingDate", new java.sql.Date(HSSFDateUtil.getJavaDate(Double.parseDouble(dataString)).getTime()));
                                }
                            }
                        } else if (colIx == (colUsd)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("priceUsd is too long - USD太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("priceUsd contains invalid character - USD包含非法字符;");
                                    } else {
                                        priceUsd = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colSupplier)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("supplier is too long - 供应商太长.不能超过255字;");
                                } else {
                                    supplier = dataString;
                                    if (supplier.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("supplier contains invalid character - 供应商包含非法字符;");
                                    }
                                }
                            }
                        }/* else if (colIx == (colPrice)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("supplierPrice is too long - 原始成本太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("supplierPrice contains invalid character - 原始成本包含非法字符.");
                                    } else {
                                        price = new BigDecimal(dataString);
                                    }
                                }
                            }
                        }*/ else if (colIx == (colActualPrice)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("actualPrice is too long - 太长.不能超过255字");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("actualPrice contains invalid character - 实成本包含非法字符.");
                                    } else {
                                        actualPrice = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colSupplierLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("supplierLink is too long - 采购链接太长.不能超过255字;");
                                } else {
                                    supplierLink = dataString;
                                }
                            }
                        } else if (colIx == (colEta)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("eta is too long - ETA太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("eta contains invalid character - ETA包含非法字符;");
                                    } else {
                                        eta = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinOrderDays)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("minOrderDays is too long - 采购天数太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("minOrderDays contains invalid character - 采购天数包含非法字符;");
                                    } else {
                                        minOrderDays = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderIdentifierA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moIdentifierA is too long - '起订量1-关联符'太长.不能超过255字;");
                                } else {
                                    moIdentifierA = dataString;
                                    if (moIdentifierA.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("moIdentifierA contains invalid character - '起订量1-关联符'包含非法字符;");
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderQtyA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moQtyA is too long - '起订量1-数量'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("moQtyA contains invalid character - '起订量1-数量'包含非法字符;");
                                    } else {
                                        moQtyA = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderPriceA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moPriceA is too long - '起订量1-金额'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moPriceA contains invalid character - '起订量1-金额'包含非法字符;");
                                    } else {
                                        moPriceA = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderUnitPriceA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moUnitPriceA is too long - '起订量1-成本'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moUnitPriceA contains invalid character - '起订量1-成本'包含非法字符;");
                                    } else {
                                        moUnitPriceA = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderIdentifierB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moIdentifierB is too long - '起订量2-关联符'太长.不能超过255字;");
                                } else {
                                    moIdentifierB = dataString;
                                    if (moIdentifierB.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("moIdentifierB contains invalid character - '起订量2-关联符'包含非法字符;");
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderQtyB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moQtyB is too long - '起订量2-数量'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("moQtyB contains invalid character - '起订量2-数量'包含非法字符;");
                                    } else {
                                        moQtyB = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderPriceB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moPriceB is too long - '起订量2-金额'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moPriceB contains invalid character - '起订量2-金额'包含非法字符;");
                                    } else {
                                        moPriceB = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderUnitPriceB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moUnitPriceB is too long - '起订量2-成本'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moUnitPriceB contains invalid character - '起订量2-成本'包含非法字符;");
                                    } else {
                                        moUnitPriceB = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderIdentifierC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moIdentifierC is too long - '起订量3-关联符'太长.不能超过255字;");
                                } else {
                                    moIdentifierC = dataString;
                                    if (moIdentifierC.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("moIdentifierC contains invalid character - '起订量3-关联符'包含非法字符;");
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderQtyC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moQtyC is too long - '起订量3-数量'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("moQtyC contains invalid character - '起订量3-数量'包含非法字符;");
                                    } else {
                                        moQtyC = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderPriceC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moPriceC is too long - '起订量3-金额'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moPriceC contains invalid character - '起订量3-金额'包含非法字符;");
                                    } else {
                                        moPriceC = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMinimumOrderUnitPriceC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("moUnitPriceC is too long - '起订量3-成本'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("moUnitPriceC contains invalid character - '起订量3-成本'包含非法字符;");
                                    } else {
                                        moUnitPriceC = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupSupplier)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupSupplier is too long - 备用供应商太长.不能超过255字;");
                                } else {
                                    backupSupplier = dataString;
                                    if (backupSupplier.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("backupSupplier contains invalid character - 备用供应商包含非法字符;");
                                    }
                                }
                            }
                        } else if (colIx == (colBackupSupplierPrice)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupSupplierPrice is too long - 备用原始成本太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupSupplierPrice contains invalid character - 备用原始成本包含非法字符;");
                                    } else {
                                        backupSupplierPrice = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupSupplierLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupSupplierLink is too long - 备用采购链接太长.不能超过255字;");
                                } else {
                                    backupSupplierLink = dataString;
                                }
                            }
                        } else if (colIx == (colBackupSupplierEta)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupSupplierEta is too long - 备用ETA太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("backupSupplierEta contains invalid character - 备用ETA包含非法字符;");
                                    } else {
                                        backupSupplierEta = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinOrderDays)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMinOrderDays is too long - 备用采购天数太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("backupMinOrderDays contains invalid character - 备用采购天数包含非法字符;");
                                    } else {
                                        backupMinOrderDays = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderIdentifierA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoIdentifierA is too long - '备用起订量1-关联符'太长.不能超过255字;");
                                } else {
                                    backupMoIdentifierA = dataString;
                                    if (backupMoIdentifierA.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("backupMoIdentifierA contains invalid character - '备用起订量1-关联符'包含非法字符;");
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderQtyA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoQtyA is too long - '备用起订量1-数量'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("backupMoQtyA contains invalid character - '备用起订量1-数量'包含非法字符;");
                                    } else {
                                        backupMoQtyA = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderPriceA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoPriceA is too long - '备用起订量1-金额'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoPriceA contains invalid character - '备用起订量1-金额'包含非法字符;");
                                    } else {
                                        backupMoPriceA = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderUnitPriceA)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoUnitPriceA is too long - '备用起订量1-成本'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoUnitPriceA contains invalid character - '备用起订量1-成本'包含非法字符;");
                                    } else {
                                        backupMoUnitPriceA = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderIdentifierB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoIdentifierB is too long - '备用起订量2-关联符'太长.不能超过255字;");
                                } else {
                                    backupMoIdentifierB = dataString;
                                    if (backupMoIdentifierB.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("backupMoIdentifierB contains invalid character - '备用起订量2-关联符'包含非法字符;");
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderQtyB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoQtyB is too long - '备用起订量2-数量'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("backupMoQtyB contains invalid character - '备用起订量2-数量'包含非法字符;");
                                    } else {
                                        backupMoQtyB = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderPriceB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoPriceB is too long - '备用起订量2-金额'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoPriceB contains invalid character - '备用起订量2-金额'包含非法字符;");
                                    } else {
                                        backupMoPriceB = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderUnitPriceB)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoUnitPriceB is too long - '备用起订量2-成本'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoUnitPriceB contains invalid character - '备用起订量2-成本'包含非法字符;");
                                    } else {
                                        backupMoUnitPriceB = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderIdentifierC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoIdentifierC is too long - '备用起订量3-关联符'太长.不能超过255字;");
                                } else {
                                    backupMoIdentifierC = dataString;
                                    if (backupMoIdentifierC.matches(".*[ \t\n\r\f\\&\"].*")) {
                                        errorNotes.append("backupMoIdentifierC contains invalid character - '备用起订量3-关联符'包含非法字符;");
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderQtyC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoQtyC is too long - '备用起订量3-数量'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].*")) {
                                        errorNotes.append("backupMoQtyC contains invalid character - '备用起订量3-数量'包含非法字符;");
                                    } else {
                                        backupMoQtyC = Long.valueOf(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderPriceC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoPriceC is too long - '备用起订量3-金额'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoPriceC contains invalid character - '备用起订量3-金额'包含非法字符;");
                                    } else {
                                        backupMoPriceC = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colBackupMinimumOrderUnitPriceC)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("backupMoUnitPriceC is too long - '备用起订量3-成本'太长.不能超过255字;");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("backupMoUnitPriceC contains invalid character - '备用起订量3-成本'包含非法字符;");
                                    } else {
                                        backupMoUnitPriceC = new BigDecimal(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colRivalItemId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("identicalItemId is too long - 太长.不能超过255字;");
                                } else {
                                    if ((dataString.length() < 9 && dataString.length() > 12) || dataString.matches(".*000000")) {
                                        rivalItemId = dataString + "E";
                                    } else {
                                        rivalItemId = dataString;
                                    }
                                }
                            }
                        } else if (colIx == (colSimilarItemId)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("similarItemId is too long - 太长.不能超过255字;");
                                } else {
                                    if ((dataString.length() < 9 && dataString.length() > 12) || dataString.matches(".*000000")) {
                                        similarItemId = dataString + "E";
                                    } else {
                                        similarItemId = dataString;
                                    }
                                }
                            }
                        } else if (colIx == (colNotes)) {
                            productImportCtx.put("notes", dataString);
                        } else if (colIx == (colListingNotes)) {
                            productImportCtx.put("listingNotes", dataString);
                        } else if (colIx == (colUpc)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("upc is too long - upc太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("upc", dataString);
                                }
                            }
                        } else if (colIx == (colEbayPicture)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("ebayPicture is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("ebayPicture", dataString.toUpperCase().substring(0,1));
                                    ebayPicture = dataString.toUpperCase().substring(0,1);
                                }
                            }
                        } else if (colIx == (colAmazonPicture)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("amazonPicture is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("amazonPicture", dataString.toUpperCase().substring(0,1));
                                    amazonPicture = dataString.toUpperCase().substring(0,1);
                                }
                            }
                        } else if (colIx == (colFinePicture)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("finePicture is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("finePicture", dataString.toUpperCase().substring(0,1));
                                }
                            }
                        } else if (colIx == (colSmtLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (UtilValidate.isNotEmpty(dataString)) {
                                    if (dataString.length() >= 255) {
                                        errorNotes.append("smtLink too long - SMT链接太长.不能超过255字;");
                                    } else {
                                        smtLink = dataString;
                                    }
                                }
                            }
                        } else if (colIx == (colWishLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("wishLink is too long - Wish链接太长.不能超过255字;");
                                } else {
                                    wishLink = dataString;
                                }
                            }
                        } else if (colIx == (colAmazonLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("amazonLink too long - Amazon链接太长.不能超过255字;");
                                } else {
                                    amazonLink = dataString;
                                }
                            }
                        } else if (colIx == (colReferenceLink)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("referenceLink too long - Reference链接太长.不能超过255字;");
                                } else {
                                    referenceLink = dataString;
                                }
                            }
                        } else if (colIx == (colMotherSkuTemp)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("motherSkuTemp is too long - 太长.不能超过255字;");
                                } else {
                                    motherSkuTemp = dataString.toUpperCase();
                                }
                            }
                        } else if (colIx == (colGmsEbayUsAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsEbayUsAccount = dataString.toLowerCase();
                                gmsEbayUkAccount = dataString.toLowerCase();
                                gmsEbayAuAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsEbayUkAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsEbayUkAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsEbayAuAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsEbayAuAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsEbayDeAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsEbayDeAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsEbayFrAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsEbayFrAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsSmtAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsSmtAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsWishAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsWishAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsAmaUsAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsAmaUsAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsAmaUkAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsAmaUkAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsAmaCaAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsAmaCaAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsAmaDeAccount)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsAmaDeAccount = dataString.toLowerCase();
                            }
                        } else if (colIx == (colGmsAmaFollow)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateGms = true;
                                gmsAmaFollow = Long.valueOf(dataString);
                            }
                        }/* else if (colIx == (colSetSku)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateSetSku = true;
                                if (dataString.contains(" ")) {
                                    dataString = dataString.replaceAll(" ","");
                                }
                                setSku = dataString;
                            }
                        } else if (colIx == (colSetSkuQty)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateSetSku = true;
                                setSkuQty = Long.valueOf(dataString);
                            }
                        } else if (colIx == (colSetSkuType)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                updateSetSku = true;
                                setSkuType = dataString.trim().toUpperCase();
                                if (setSkuType.equals("Y")) {
                                    setSkuType = "ALIAS";
                                } else if (setSkuType.equals("ALIAS")) {
                                    setSkuType = "ALIAS";
                                } else if (setSkuType.equals("变体")) {
                                    setSkuType = "ALIAS";
                                }
                            }
                        }*/ else if (colIx == (colListerPlatform)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("listerPlatform is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("listerPlatform", dataString.toUpperCase());
                                }
                            }
                        } else if (colIx == (colLister)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("lister is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("lister", dataString.toLowerCase());
                                }
                                
                            }
                        } else if (colIx == (colPicReady)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("PicReady is too long - PicReady太长.不能超过255字");
                                } else {
                                    picReady = dataString.toUpperCase();
                                }
                            }
                        } else if (colIx == (colSetSku)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("setSku is too long - 组合SKU太长.不能超过255字");
                                } else {
                                    setSku = dataString;
                                    updateProductRelationship = true;
                                }
                            }
                        } else if (colIx == (colRelationshipType)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("RelationshipType is too long - 从属类型太长.不能超过255字");
                                } else {
                                    relationshipType = dataString.toUpperCase();
                                    updateProductRelationship = true;
                                }
                            }
                        } else if (colIx == (colQty)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("Quantity is too long - 数量太长.不能超过255字");
                                } else {
                                    quantity = Long.valueOf(dataString);
                                }
                            }
                        } else if (colIx == (colOversea)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("Oversea is too long - 海外仓太长.不能超过255字");
                                } else {
                                    oversea = dataString.toUpperCase().substring(0,1);
                                }
                            }
                        } else if (colIx == (colLength)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("length is too long - 长度太长.不能超过255字.");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("length contains invalid character - 长度包含非法字符.");
                                    } else {
                                        length = Double.parseDouble(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colWidth)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("width is too long - 宽度太长.不能超过255字.");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("width contains invalid character - 宽度包含非法字符.");
                                    } else {
                                        width = Double.parseDouble(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colHeight)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() > 255) {
                                    errorNotes.append("height is too long - 高度太长.不能超过255字.");
                                } else {
                                    if (dataString.matches(".*[ \t\n\r\f\\&\"].*") || dataString.matches(".*[^0-9].[^0-9].*")) {
                                        errorNotes.append("height contains invalid character - 高度包含非法字符.");
                                    } else {
                                        height = Double.parseDouble(dataString);
                                    }
                                }
                            }
                        } else if (colIx == (colMainSupHolidayStart)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("holidayStart is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("supplierHolidayStart", new java.sql.Date(HSSFDateUtil.getJavaDate(Double.parseDouble(dataString)).getTime()));
                                }
                            }
                        } else if (colIx == (colMainSupHolidayEnd)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("sourcingDate is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("supplierHolidayEnd", new java.sql.Date(HSSFDateUtil.getJavaDate(Double.parseDouble(dataString)).getTime()));
                                }
                            }
                        } else if (colIx == (colBackupSupHolidayStart)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("sourcingDate is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("backupHolidayStart", new java.sql.Date(HSSFDateUtil.getJavaDate(Double.parseDouble(dataString)).getTime()));
                                }
                            }
                        } else if (colIx == (colBackupSupHolidayEnd)) {
                            if (UtilValidate.isNotEmpty(dataString)) {
                                if (dataString.length() >= 255) {
                                    errorNotes.append("sourcingDate is too long - 太长.不能超过255字;");
                                } else {
                                    productImportCtx.put("backupHolidayEnd", new java.sql.Date(HSSFDateUtil.getJavaDate(Double.parseDouble(dataString)).getTime()));
                                }
                            }
                        }  //read record -- END
                    }   //loop cell in a row -- END
                    
                    
                    if (updateProductImport) {    //if row indeed has data -- START
                        GenericValue productCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
                        try {   //try -- second -- START
                            //populating productImportCtx == START
                            productImportCtx.put("productId", productId);
                            productImportCtx.put("customSku", customSku);
                            productImportCtx.put("motherSku", motherSku);
                            productImportCtx.put("productGroup", productGroup);
                            
                            productImportCtx.put("chineseName", chineseName);
                            productImportCtx.put("englishName", englishName);
                            if (weight > 0.0) {
                                productImportCtx.put("weight", weight);
                            }
                            if (actualWeight > 0.0) {
                                productImportCtx.put("actualWeight", actualWeight);
                            }
                            if (length > 0.0) {
                                productImportCtx.put("length", length);
                            }
                            if (width > 0.0) {
                                productImportCtx.put("width", width);
                            }
                            if (height > 0.0) {
                                productImportCtx.put("height", height);
                            }
                            
                            if (UtilValidate.isNotEmpty(statusId)) {    //check statusId is not empty == START
                                if (isSbu || isAdmin) {    //has rights to update statusId == START
                                    productImportCtx.put("statusId", statusId);
                                }   //has rights to update statusId == START
                                else {
                                    errorNotes.append("No rights to update product status - 没有权限更新产品状态;");
                                }
                            }   //check statusId is not empty == END
                            
                            if (changePlatformStatus) { //changePlatformStatus is true - update into Product Import map == START
                                boolean productDiscontinued = false;
                                if (UtilValidate.isNotEmpty(productCheck)) {
                                    String productStatusCheck = productCheck.getString("statusId");
                                    if (productStatusCheck.equals("DISCONTINUED")) {
                                        productDiscontinued = true;
                                    }
                                } else {
                                    errorNotes.append("SKU " + productId + " does not exist - SKU不存在;");
                                }
                                
                                if (UtilValidate.isNotEmpty(ebayStatusId)) {    //if ebayStatusId is not empty == START
                                    List<GenericValue> platformRoleCheckList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("statusId", "PARTY_ENABLED", "partyId", userLoginPartyId, "parentTypeId", "PLATFORM", "roleTypeId", "EBAY"), null, false);
                                    if (UtilValidate.isNotEmpty(platformRoleCheckList) || isAdmin) {   //has rights to update ebayStatusId == START
                                        if (productDiscontinued) {  //if product is discontinued == START
                                            errorNotes.append("Unable to update platform status for discontinued product - 无法更新已停产产品的平台状态;");
                                        } else {
                                            productImportCtx.put("ebayStatusId", ebayStatusId);
                                        }   //if product is discontinued == END
                                    }   //has rights to update ebayStatusId == END
                                    else {
                                        errorNotes.append("No rights to update eBay status - 没有权限更新eBay状态;");
                                    }
                                }   //if ebayStatusId is not empty == END
                                
                                if (UtilValidate.isNotEmpty(wishStatusId)) {    //if wishStatusId is not empty == START
                                    List<GenericValue> platformRoleCheckList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("statusId", "PARTY_ENABLED", "partyId", userLoginPartyId, "parentTypeId", "PLATFORM", "roleTypeId", "WISH"), null, false);
                                    if (UtilValidate.isNotEmpty(platformRoleCheckList) || isAdmin) {   //has rights to update wishStatusId == START
                                        if (productDiscontinued) {  //if product is discontinued == START
                                            errorNotes.append("Unable to update platform status for discontinued product - 无法更新已停产产品的平台状态;");
                                        } else {
                                            productImportCtx.put("wishStatusId", wishStatusId);
                                        }   //if product is discontinued == END
                                    }   //has rights to update wishStatusId == END
                                    else {
                                        errorNotes.append("No rights to update WISH status - 没有权限更新WISH状态;");
                                    }
                                }   //if wishStatusId is not empty == END
                                
                                if (UtilValidate.isNotEmpty(smtStatusId)) {    //if smtStatusId is not empty == START
                                    List<GenericValue> platformRoleCheckList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("statusId", "PARTY_ENABLED", "partyId", userLoginPartyId, "parentTypeId", "PLATFORM", "roleTypeId", "SMT"), null, false);
                                    if (UtilValidate.isNotEmpty(platformRoleCheckList) || isAdmin) {   //has rights to update smtStatusId == START
                                        if (productDiscontinued) {  //if product is discontinued == START
                                            errorNotes.append("Unable to update platform status for discontinued product - 无法更新已停产产品的平台状态;");
                                        } else {
                                            productImportCtx.put("smtStatusId", smtStatusId);
                                        }   //if product is discontinued == END
                                    }   //has rights to update smtStatusId == END
                                    else {
                                        errorNotes.append("No rights to update SMT status - 没有权限更新SMT状态;");
                                    }
                                }   //if smtStatusId is not empty == END
                                
                                if (UtilValidate.isNotEmpty(amazonStatusId)) {    //if amazonStatusId is not empty == START
                                    List<GenericValue> platformRoleCheckList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("statusId", "PARTY_ENABLED", "partyId", userLoginPartyId, "parentTypeId", "PLATFORM", "roleTypeId", "AMAZON"), null, false);
                                    if (UtilValidate.isNotEmpty(platformRoleCheckList) || isAdmin) {   //has rights to update amazonStatusId == START
                                        if (productDiscontinued) {  //if product is discontinued == START
                                            errorNotes.append("Unable to update platform status for discontinued product - 无法更新已停产产品的平台状态;");
                                        } else {
                                            productImportCtx.put("amazonStatusId", amazonStatusId);
                                        }   //if product is discontinued == END
                                    }   //has rights to update amazonStatusId == END
                                    else {
                                        errorNotes.append("No rights to update Amazon status - 没有权限更新Amazon状态;");
                                    }
                                }   //if amazonStatusId is not empty == END
                                
                                if (UtilValidate.isNotEmpty(lazadaStatusId)) {    //if lazadaStatusId is not empty == START
                                    List<GenericValue> platformRoleCheckList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("statusId", "PARTY_ENABLED", "partyId", userLoginPartyId, "parentTypeId", "PLATFORM", "roleTypeId", "LAZADA"), null, false);
                                    if (UtilValidate.isNotEmpty(platformRoleCheckList) || isAdmin) {   //has rights to update lazadaStatusId == START
                                        if (productDiscontinued) {  //if product is discontinued == START
                                            errorNotes.append("Unable to update platform status for discontinued product - 无法更新已停产产品的平台状态;");
                                        } else {
                                            productImportCtx.put("lazadaStatusId", lazadaStatusId);
                                        }   //if product is discontinued == END
                                    }   //has rights to update lazadaStatusId == END
                                    else {
                                        errorNotes.append("No rights to update Lazada status - 没有权限更新Lazada状态;");
                                    }
                                }   //if lazadaStatusId is not empty == END
                                
                                if (UtilValidate.isNotEmpty(cdiscountStatusId)) {    //if cdiscountStatusId is not empty == START
                                    List<GenericValue> platformRoleCheckList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("statusId", "PARTY_ENABLED", "partyId", userLoginPartyId, "parentTypeId", "PLATFORM", "roleTypeId", "CDISCOUNT"), null, false);
                                    if (UtilValidate.isNotEmpty(platformRoleCheckList) || isAdmin) {   //has rights to update cdiscountStatusId == START
                                        if (productDiscontinued) {  //if product is discontinued == START
                                            errorNotes.append("Unable to update platform status for discontinued product - 无法更新已停产产品的平台状态;");
                                        } else {
                                            productImportCtx.put("cdiscountStatusId", cdiscountStatusId);
                                        }   //if product is discontinued == END
                                    }   //has rights to update cdiscountStatusId == END
                                    else {
                                        errorNotes.append("No rights to update Cdiscount status - 没有权限更新Cdiscount状态;");
                                    }
                                }   //if cdiscountStatusId is not empty == END
                                
                                if (UtilValidate.isNotEmpty(tophatterStatusId)) {    //if tophatterStatusId is not empty == START
                                    List<GenericValue> platformRoleCheckList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("statusId", "PARTY_ENABLED", "partyId", userLoginPartyId, "parentTypeId", "PLATFORM", "roleTypeId", "TOPHATTER"), null, false);
                                    if (UtilValidate.isNotEmpty(platformRoleCheckList) || isAdmin) {   //has rights to update tophatterStatusId == START
                                        if (productDiscontinued) {  //if product is discontinued == START
                                            errorNotes.append("Unable to update platform status for discontinued product - 无法更新已停产产品的平台状态;");
                                        } else {
                                            productImportCtx.put("tophatterStatusId", tophatterStatusId);
                                        }   //if product is discontinued == END
                                    }   //has rights to update tophatterStatusId == END
                                    else {
                                        errorNotes.append("No rights to update Tophatter status - 没有权限更新Tophatter状态;");
                                    }
                                }   //if tophatterStatusId is not empty == END
                            }   //changePlatformStatus is true - update into Product Import map == END
                            
                            
                            productImportCtx.put("productType", productType);
                            if (UtilValidate.isNotEmpty(ownerGroup)) {
                                productImportCtx.put("ownerGroup", ownerGroup);
                            }
                            productImportCtx.put("imageLink", imageLink);
                            if (priceUsd.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("priceUsd", priceUsd);
                            }
                            productImportCtx.put("supplier", supplier);
                            productImportCtx.put("supplierLink", supplierLink);
                            /*if (price.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("price", price);
                            }*/
                            if (actualPrice.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("actualPrice", actualPrice);
                            }
                            /*if (actualPrice.compareTo(BigDecimal.ZERO) == 0) {
                                productImportCtx.put("actualPrice", moUnitPriceA);
                            }*/
                            if (eta > 0) {
                                productImportCtx.put("eta", eta);
                            }
                            if (extraStockingDay > 0) {
                                productImportCtx.put("extraStockingDay", extraStockingDay);
                            }
                            if (minOrderDays > 0) {
                                productImportCtx.put("minOrderDays", minOrderDays);
                            }
                            productImportCtx.put("moIdentifierA", moIdentifierA);
                            if (moQtyA > 0) {
                                productImportCtx.put("moQtyA", moQtyA);
                            }
                            if (moPriceA.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moPriceA", moPriceA);
                            }
                            if (moUnitPriceA.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moUnitPriceA", moUnitPriceA);
                            }
                            productImportCtx.put("moIdentifierB", moIdentifierB);
                            if (moQtyB > 0) {
                                productImportCtx.put("moQtyB", moQtyB);
                            }
                            if (moPriceB.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moPriceB", moPriceB);
                            }
                            if (moUnitPriceB.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moUnitPriceB", moUnitPriceB);
                            }
                            productImportCtx.put("moIdentifierC", moIdentifierC);
                            if (moQtyC > 0) {
                                productImportCtx.put("moQtyC", moQtyC);
                            }
                            if (moPriceC.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moPriceC", moPriceC);
                            }
                            if (moUnitPriceC.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("moUnitPriceC", moUnitPriceC);
                            }
                            productImportCtx.put("backupSupplier", backupSupplier);
                            productImportCtx.put("backupSupplierLink", backupSupplierLink);
                            if (backupSupplierPrice.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupSupplierPrice", backupSupplierPrice);
                            }
                            if (backupSupplierEta > 0) {
                                productImportCtx.put("backupSupplierEta", backupSupplierEta);
                            }
                            if (backupMinOrderDays > 0) {
                                productImportCtx.put("backupMinOrderDays", backupMinOrderDays);
                            }
                            productImportCtx.put("backupMoIdentifierA", backupMoIdentifierA);
                            if (backupMoQtyA > 0) {
                                productImportCtx.put("backupMoQtyA", backupMoQtyA);
                            }
                            if (backupMoPriceA.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoPriceA", backupMoPriceA);
                            }
                            if (backupMoUnitPriceA.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoUnitPriceA", backupMoUnitPriceA);
                            }
                            productImportCtx.put("backupMoIdentifierB", backupMoIdentifierB);
                            if (backupMoQtyB > 0) {
                                productImportCtx.put("backupMoQtyB", backupMoQtyB);
                            }
                            if (backupMoPriceB.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoPriceB", backupMoPriceB);
                            }
                            if (backupMoUnitPriceB.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoUnitPriceB", backupMoUnitPriceB);
                            }
                            productImportCtx.put("backupMoIdentifierC", backupMoIdentifierC);
                            if (backupMoQtyC > 0) {
                                productImportCtx.put("backupMoQtyC", backupMoQtyC);
                            }
                            if (backupMoPriceC.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoPriceC", backupMoPriceC);
                            }
                            if (backupMoUnitPriceC.compareTo(BigDecimal.ZERO) > 0) {
                                productImportCtx.put("backupMoUnitPriceC", backupMoUnitPriceC);
                            }
                            productImportCtx.put("rivalItemId", rivalItemId);
                            productImportCtx.put("similarItemId", similarItemId);
                            productImportCtx.put("smtLink", smtLink);
                            productImportCtx.put("wishLink", wishLink);
                            productImportCtx.put("amazonLink", amazonLink);
                            productImportCtx.put("referenceLink", referenceLink);
                            if (UtilValidate.isNotEmpty(picReady)) {
                                if (picReady.substring(0,1).equals("Y")) {
                                    productImportCtx.put("pictureReady", "Y");
                                }
                            }
                            if (UtilValidate.isNotEmpty(oversea)) {
                                if (oversea.substring(0,1).equals("Y")) {
                                    productImportCtx.put("oversea", "Y");
                                } else {
                                    productImportCtx.put("oversea", "N");
                                }
                            }
                            //populating productImportCtx == END
                            
                            //Debug.logError("MotherSKU: " + motherSku, module);
                            if (UtilValidate.isNotEmpty(motherSkuTemp)) {  //if motherSku is not empty -- START
                                EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                          EntityCondition.makeCondition("productId",EntityOperator.LIKE , motherSkuTemp + "%")
                                                                                          ));
                                List<GenericValue> childrenSkuList = delegator.findList("ProductMaster", condition, null, null, null, false);
                                for (GenericValue childrenSku : childrenSkuList) {  //loop childrenSkuList == START
                                    Map<String, Object> productChildImportCtx = FastMap.newInstance();
                                    String childSku = childrenSku.getString("productId");
                                    productChildImportCtx.put("productId", childSku);
                                    productChildImportCtx.put("ebayPicture", ebayPicture);
                                    productChildImportCtx.put("amazonPicture", amazonPicture);
                                    
                                    String newProductImportId = delegator.getNextSeqId("ProductImportGudao");
                                    productChildImportCtx.put("productImportId", newProductImportId);
                                    productChildImportCtx.put("fileName", filePath);
                                    productChildImportCtx.put("fileLineNumber", new BigDecimal(j));
                                    productChildImportCtx.put("importedStatus", "N");
                                    productChildImportCtx.put("userLogin", userLogin);
                                    productChildImportCtx.put("originalFilename", originalFilename);
                                    
                                    result = dispatcher.runSync("createProductImportGudao", productChildImportCtx);
                                    if (ServiceUtil.isError(result)) {  //if result gives error -- START
                                        errorList.add(ServiceUtil.getErrorMessage(result));
                                    }   //if result gives error -- END

                                }   //loop childrenSkuList == END
                            }   //if motherSku is not empty -- END
                            else if (updateGms) {   //if updateGms is true == START
                                Map<String, Object> updateGmsImportCtx = FastMap.newInstance();
                                updateGmsImportCtx.put("productId", productId);
                                updateGmsImportCtx.put("updateGms", "Y");
                                if (UtilValidate.isNotEmpty(gmsEbayUsAccount)) {
                                    updateGmsImportCtx.put("ebayUsAccount", gmsEbayUsAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsEbayUkAccount)) {
                                    updateGmsImportCtx.put("ebayUkAccount", gmsEbayUkAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsEbayAuAccount)) {
                                    updateGmsImportCtx.put("ebayAuAccount", gmsEbayAuAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsEbayDeAccount)) {
                                    updateGmsImportCtx.put("ebayDeAccount", gmsEbayDeAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsEbayFrAccount)) {
                                    updateGmsImportCtx.put("ebayFrAccount", gmsEbayFrAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsSmtAccount)) {
                                    updateGmsImportCtx.put("smtAccount", gmsSmtAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsWishAccount)) {
                                    updateGmsImportCtx.put("wishAccount", gmsWishAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsAmaUsAccount)) {
                                    updateGmsImportCtx.put("amaUsAccount", gmsAmaUsAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsAmaUkAccount)) {
                                    updateGmsImportCtx.put("amaUkAccount", gmsAmaUkAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsAmaCaAccount)) {
                                    updateGmsImportCtx.put("amaCaAccount", gmsAmaCaAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsAmaDeAccount)) {
                                    updateGmsImportCtx.put("amaDeAccount", gmsAmaDeAccount);
                                }
                                if (UtilValidate.isNotEmpty(gmsAmaFollow)) {
                                    updateGmsImportCtx.put("amaFollow", gmsAmaFollow);
                                }
                                
                                String newProductImportId = delegator.getNextSeqId("ProductImportGudao");
                                updateGmsImportCtx.put("productImportId", newProductImportId);
                                updateGmsImportCtx.put("fileName", filePath);
                                updateGmsImportCtx.put("fileLineNumber", new BigDecimal(j));
                                updateGmsImportCtx.put("importedStatus", "N");
                                updateGmsImportCtx.put("userLogin", userLogin);
                                updateGmsImportCtx.put("originalFilename", originalFilename);
                                
                                result = dispatcher.runSync("createProductImportGudao", updateGmsImportCtx);
                                if (ServiceUtil.isError(result)) {  //if result gives error -- START
                                    errorList.add(ServiceUtil.getErrorMessage(result));
                                }   //if result gives error -- END
                            }   //if updateGms is true == END
                            /*else if (updateSetSku) {    //if updateSetSku is true == START
                                
                                GenericValue setSkuGV = delegator.findOne("GudaoSetSku", UtilMisc.toMap("setSku", setSku, "productId", productId), false);
                                if (UtilValidate.isNotEmpty(setSkuGV)) {
                                    if (UtilValidate.isNotEmpty(setSkuQty)) {
                                        setSkuGV.set("qty", setSkuQty);
                                    } else if (setSkuType.equals("SET")) {
                                        setSkuGV.set("qty", 1);
                                    }
                                    setSkuGV.set("type", setSkuType);
                                    delegator.store(setSkuGV);
                                } else {
                                    setSkuGV = delegator.makeValue("GudaoSetSku", UtilMisc.toMap("setSku", setSku, "productId", productId));
                                    if (UtilValidate.isNotEmpty(setSkuQty)) {
                                        setSkuGV.set("qty", setSkuQty);
                                    } else if (setSkuType.equals("SET")) {
                                        setSkuGV.set("qty", 1);
                                    }
                                    setSkuGV.set("type", setSkuType);
                                    delegator.create(setSkuGV);
                                }
                            } */  //if updateSetSku is true == END
                            else if (updateProductRelationship) {   //if updateProductRelationship is true == START
                                Map<String, Object> updateProductRelationshipImportCtx = FastMap.newInstance();
                                if (relationshipType.contains("SET")) {
                                    relationshipType = "SETSKU";
                                } else {
                                    relationshipType = "VARIATION";
                                }
                                
                                GenericValue motherSkuCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", setSku), false);
                                if (UtilValidate.isEmpty(motherSkuCheck)) {
                                    updateProductRelationshipImportCtx.put("errorNotes", "mother setSKU does not exist - 组合SKU不存在");
                                } else {
                                    if (!motherSkuCheck.getString("statusId").equals("SETSKU")) {
                                        updateProductRelationshipImportCtx.put("errorNotes", "mother setSKU is not SETSKU - 组合SKU不是setSku");
                                    }
                                }
                                
                                GenericValue childrenSkuCheck = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
                                if (UtilValidate.isEmpty(childrenSkuCheck)) {
                                    updateProductRelationshipImportCtx.put("errorNotes", "children SKU does not exist - SKU不存在");
                                } else {
                                    if (childrenSkuCheck.getString("statusId").equals("SETSKU")) {
                                        updateProductRelationshipImportCtx.put("errorNotes", "Children SKU is SETSKU - 子SKU是setSku");
                                    }
                                }
                                
                                updateProductRelationshipImportCtx.put("productId", productId);
                                updateProductRelationshipImportCtx.put("motherSku", setSku);
                                updateProductRelationshipImportCtx.put("relationshipType", relationshipType);
                                updateProductRelationshipImportCtx.put("quantity", quantity);
                                updateProductRelationshipImportCtx.put("updateProductRelationship", "Y");
                                
                                String newProductImportId = delegator.getNextSeqId("ProductImportGudao");
                                updateProductRelationshipImportCtx.put("productImportId", newProductImportId);
                                updateProductRelationshipImportCtx.put("fileName", filePath);
                                updateProductRelationshipImportCtx.put("fileLineNumber", new BigDecimal(j));
                                updateProductRelationshipImportCtx.put("importedStatus", "N");
                                updateProductRelationshipImportCtx.put("userLogin", userLogin);
                                updateProductRelationshipImportCtx.put("originalFilename", originalFilename);
                                
                                result = dispatcher.runSync("createProductImportGudao", updateProductRelationshipImportCtx);
                                if (ServiceUtil.isError(result)) {  //if result gives error -- START
                                    errorList.add(ServiceUtil.getErrorMessage(result));
                                }
                            }   //if updateProductRelationship is true == END
                            else {  //if motherSku is empty -- START
                                //default value == START
                                String childrenCategoryCn = null;
                                String childrenCategoryEn = null;
                                if (UtilValidate.isNotEmpty(parentCategoryName)) {
                                    List<GenericValue> parentCategoryList = delegator.findByAnd("GudaoProductCategory", UtilMisc.toMap("categoryName", parentCategoryName, "type", "PARENT"), null, false);
                                    if (UtilValidate.isEmpty(parentCategoryList)) {  //check if parentCategoryName exist in database == START
                                        errorNotes.append("Parent Category does not exist in database;");
                                    }  //check if parentCategoryName exist in database == END
                                    else {
                                        productImportCtx.put("categoryIdParent", parentCategoryName);
                                    }
                                }
                                
                                if (UtilValidate.isNotEmpty(childrenCategoryName)) {
                                    List<GenericValue> childrenCategoryList = delegator.findByAnd("GudaoProductCategory", UtilMisc.toMap("categoryName", childrenCategoryName, "type", "CHILDREN"), null, false);
                                    if (UtilValidate.isEmpty(childrenCategoryList)) {  //check if parentCategoryName exist in database == START
                                        errorNotes.append("Children Category does not exist in database;");
                                    }
                                    else {
                                        productImportCtx.put("categoryIdChild", childrenCategoryName);
                                    }
                                }
                                
                                if (UtilValidate.isNotEmpty(overRideDeclaredCn)) {
                                    productImportCtx.put("declaredNameCn", overRideDeclaredCn);
                                }
                                
                                if (UtilValidate.isNotEmpty(overRideDeclaredEn)) {
                                    productImportCtx.put("declaredNameEn", overRideDeclaredEn);
                                }
                                //default value == END
                                
                                //Check if has permission to update SKU == START
                                if (UtilValidate.isNotEmpty(productCheck)) {    //if productCheck is not empty == START
                                    String ownerGroupCheckUpdate = productCheck.getString("ownerGroup");
                                    if (!isAdmin) { //if not admin == START
                                        if (!isPlatform) {  //if not a platform == START
                                            if (!ownerGroupCheck.equals(ownerGroupCheckUpdate)) {
                                                errorNotes.append("no permission to update this SKU (Wrong SBU);");
                                            }
                                        }   //if not a platform == END
                                    }   //if not admin == END
                                }   //if productCheck is not empty == END
                                //Check if has permission to update SKU == END
                                
                                
                                String errorMessage = null;
                                if (errorNotes.toString().length() >= 253) {
                                    errorMessage = errorNotes.toString().substring(1, 253);
                                } else {
                                    errorMessage = errorNotes.toString();
                                }
                                
                                if (UtilValidate.isNotEmpty(errorMessage)) {
                                    productImportCtx.put("importedDescription", errorMessage);
                                }
                                
                                String newProductImportId = delegator.getNextSeqId("ProductImportGudao");
                                productImportCtx.put("errorNotes", errorMessage);
                                productImportCtx.put("productImportId", newProductImportId);
                                productImportCtx.put("fileName", filePath);
                                productImportCtx.put("fileLineNumber", new BigDecimal(j));
                                productImportCtx.put("importedStatus", "N");
                                productImportCtx.put("userLogin", userLogin);
                                productImportCtx.put("originalFilename", originalFilename);
                                
                                result = dispatcher.runSync("createProductImportGudao", productImportCtx);
                                if (ServiceUtil.isError(result)) {  //if result gives error -- START
                                    errorList.add(ServiceUtil.getErrorMessage(result));
                                }   //if result gives error -- END
                            }   //if motherSku is empty -- END
                        }   //try -- second -- END
                        catch (Exception e) {
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    }   //if row indeed has data -- END
                    
                }   //if row is not empty -- END
            }   //loop rows -- END
        } catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }   //main try -- END
        
        return ServiceUtil.returnSuccess();
    }   //readProductUpdateXls
    
    private static Map<String, Object> readProductCsv(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String filePath) {   //readProductXls
        Map<String, Object> result = ServiceUtil.returnSuccess();
        List<String> errorList = FastList.newInstance();
        try {   //main try -- START
            CsvReader csv = new CsvReader(new FileInputStream(new File(filePath)), Charset.forName("WINDOWS-1252"));
            if (UtilValidate.isNotEmpty(csv)) { //if csv is not empty -- START
                csv.readHeaders();
                while (csv.readRecord()) {  //While loop csv.readRecord -- START
                    Map<String, Object> productImportCtx = FastMap.newInstance();
                    String newProductImportId = delegator.getNextSeqId("ProductImport");
                    productImportCtx.put("productImportId", newProductImportId);
                    productImportCtx.put("fileName", filePath);
                    productImportCtx.put("fileLineNumber", new BigDecimal(csv.getCurrentRecord() + 1));
                    productImportCtx.put("importedStatus", "N");
                    productImportCtx.put("userLogin", userLogin);
                    
                    for (int columnCount = 0; columnCount < csv.getHeaderCount(); columnCount++) {  //For loop columnCount -- START
                        String data = csv.get(columnCount);
                        String header = csv.getHeader(columnCount).toUpperCase().trim();
                        
                        if ("SKU".equals(header)) { //read record -- START
                            productImportCtx.put("productId", data);
                        } else if ("INTERNAL NAME".equals(header)) {
                            productImportCtx.put("internalName", data);
                        } else if ("EN NAME".equals(header)) {
                            productImportCtx.put("declaredNameEn", data);
                        } else if ("CN NAME".equals(header)) {
                            productImportCtx.put("declaredNameCn", data);
                        } else if ("LOCATION".equals(header)) {
                            productImportCtx.put("locationCode", data);
                        } else if ("WEIGHT".equals(header)) {
                            productImportCtx.put("weight", data);
                        } else if ("PRODUCT TYPE".equals(header) || "TYPE".equals(header)) {
                            productImportCtx.put("productType", data);
                        } else if ("VIRTUAL".equals(header) || "IS VIRTUAL".equals(header)) {
                            productImportCtx.put("isVirtual", data);
                        } else if ("VARIANT".equals(header) || "IS VARIANT".equals(header)) {
                            productImportCtx.put("isVariant", data);
                        } else if ("VARIANT METHOD".equals(header)) {
                            productImportCtx.put("virtualVariantMethodEnum", data);
                        } else if ("REQUIREMENT METHOD".equals(header)) {
                            productImportCtx.put("requirementMethodEnumId", data);
                        } else if ("SUPPLIER".equals(header) || "SUPPLIER ID".equals(header)) {
                            productImportCtx.put("supplierPartyId", data);
                        } else if ("FACILITY".equals(header) || "WAREHOUSE".equals(header) || "FACILITY ID".equals(header)) {
                            productImportCtx.put("facilityId", data);
                        } else if ("USD".equals(header)) {
                            productImportCtx.put("priceUSD", data);
                        } else if ("CAD".equals(header)) {
                            productImportCtx.put("priceCAD", data);
                        } else if ("GBP".equals(header)) {
                            productImportCtx.put("priceGBP", data);
                        } else if ("AUD".equals(header)) {
                            productImportCtx.put("priceAUD", data);
                        } else if ("EUR".equals(header)) {
                            productImportCtx.put("priceEUR", data);
                        }   //read record -- END
                    }   //For loop columnCount -- END
                    
                    try {   //try -- second -- START
                        String checkDataExist = csv.get(1);
                        if (UtilValidate.isNotEmpty(checkDataExist) && UtilValidate.isNotEmpty(csv.get(0).trim())) {    //if data exist -- START
                            result = dispatcher.runSync("createProductImport", productImportCtx);
                            if (ServiceUtil.isError(result)) {  //if result gives error -- START
                                errorList.add(ServiceUtil.getErrorMessage(result));
                            }   //if result gives error -- END
                        }   //if data exist -- END
                    }   //try -- second -- END
                    catch (Exception e) {
                        return ServiceUtil.returnError(e.getMessage());
                    }
                }   //While loop csv.readRecord -- END
            }   //if csv is not empty -- END
        } catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }   //main try -- END
        return ServiceUtil.returnSuccess();
    }   //readProductCsv
    
    public static String convertMessage(LinkedList list) {
        
        Debug.logError("running convertMessage", module);
        String result = null;
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            if (result == null) {
                result = (String) iterator.next();
            } else {
                result += ", " + iterator.next();
            }
        }
        Debug.logError("convertMessage result is: " + result, module);
        return result;
    }
    
    public static Map<String, Object> deleteFileBulkModule(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String fileName = (String) context.get("fileName");
        String importFormId = (String) context.get("importFormId");
        String updateFormId = (String) context.get("updateFormId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        if (UtilValidate.isEmpty(fileName)) {
            result = ServiceUtil.returnError("Required fileName parameter.");
            result.put("importFormId", importFormId);
            result.put("updateFormId", updateFormId);
            return result;
        }
        try {   //main try -- START
            if (UtilValidate.isNotEmpty(importFormId)) {    //Execute deleteProductImport -- START
                if (importFormId.equals("PRODUCT")) {
                    List<GenericValue> productImportList = delegator.findByAnd("ProductImportGudao", UtilMisc.toMap("fileName", (String) context.get("fileName")), null, false);
                    if (productImportList.size() > 0) { //if productImportList is not empty -- START
                        for (GenericValue productImport : productImportList) {
                            dispatcher.runSync("deleteProductImportGudao", UtilMisc.toMap("productImportId", productImport.getString("productImportId"), "userLogin", userLogin));
                        }
                        long productImportCount = delegator.findCountByCondition("ProductImportGudao", EntityCondition.makeCondition(UtilMisc.toMap("fileName", fileName)), null, null);
                        if (productImportCount == 0) {
                            File file = new File(fileName);
                            if (!file.exists()) {
                                result = ServiceUtil.returnError("This file doesn't exist.");
                                result.put("importFormId", importFormId);
                                return result;
                            }
                            file.delete();
                        }
                    }   //if productImportList is not empty -- END

                } else if (importFormId.equals("PROFIT")) {
                    List<GenericValue> reportImportList = delegator.findByAnd("ReportImport", UtilMisc.toMap("fileName", (String) context.get("fileName")), null, false);
                    if (reportImportList.size() > 0) { //if productImportList is not empty -- START
                        for (GenericValue reportImport : reportImportList) {
                            dispatcher.runSync("deleteReportImportGudao", UtilMisc.toMap("reportImportId", reportImport.getString("reportImportId"), "userLogin", userLogin));
                        }
                        long reportImportCount = delegator.findCountByCondition("ReportImport", EntityCondition.makeCondition(UtilMisc.toMap("fileName", fileName)), null, null);
                        if (reportImportCount == 0) {
                            File file = new File(fileName);
                            if (!file.exists()) {
                                result = ServiceUtil.returnError("This file doesn't exist.");
                                result.put("importFormId", importFormId);
                                return result;
                            }
                            file.delete();
                        }
                    }   //if productImportList is not empty -- END
                }
                
            }   //Execute deleteProductImport -- END
            
            if (UtilValidate.isNotEmpty(updateFormId)) {    //Execute deleteProductUpdate -- START
                List<GenericValue> bulkProductUpdateList = delegator.findByAnd("ProductImportGudao", UtilMisc.toMap("fileName", (String) context.get("fileName")), null, false);
                if (bulkProductUpdateList.size() > 0) { //if bulkProductUpdateList is not empty -- START
                    for (GenericValue bulkProductUpdate : bulkProductUpdateList) {
                        dispatcher.runSync("deleteBulkProductUpdateGudao", UtilMisc.toMap("productImportId", bulkProductUpdate.getString("productImportId"), "userLogin", userLogin));
                    }
                    long bulkProductUpdateCount = delegator.findCountByCondition("ProductImportGudao", EntityCondition.makeCondition(UtilMisc.toMap("fileName", fileName)), null, null);
                    if (bulkProductUpdateCount == 0) {
                        File file = new File(fileName);
                        if (!file.exists()) {
                            result = ServiceUtil.returnError("This file doesn't exist.");
                            result.put("updateFormId", updateFormId);
                            return result;
                        }
                        file.delete();
                    }
                }   //if bulkProductUpdateList is not empty -- END
            }   //Execute deleteProductUpdate -- END
            
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
            result.put("importFormId", importFormId);
            result.put("updateFormId", updateFormId);
            return result;
        }   //main try -- END
        result = ServiceUtil.returnSuccess("Delete ProductImport and file successful.");
        result.put("importFormId", importFormId);
        result.put("updateFormId", updateFormId);
        return result;
    }   //deleteFileProductImport
    
    public static java.sql.Date convertExcelDateToJSDate(int date) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Debug.logError("Date is " + date, module);
        return new java.sql.Date((date - 25569)*86400*1000);
    }
    
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
    
    public static Map<String, Object> gudaoRefreshProductMaster (DispatchContext dctx, Map context) {   //gudaoGenerateProductMaster
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productIdQuery = (String) context.get("productId");
        String productGroupQuery = (String) context.get("productGroupQuery");
        List<String> productGroupList = (List) context.get("productGroupList");
        List<String> productIdList = (List) context.get("productIdList");
        Map result = ServiceUtil.returnSuccess();
        //Debug.logError("Start refreshing PM, productID: " + productIdQuery, module);
        //String text = (String) context.get("Text");
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
        
        try {   //main try == START
            boolean writeLogFile = false;
            boolean writeLogFileDetail = false;
            if (pmDebug().get("refreshPmWriteToFile").toUpperCase().equals("Y")) {
                writeLogFile = true;
            }
            if (pmDebug().get("refreshPmWriteToFileDetail").toUpperCase().equals("Y")) {
                writeLogFileDetail = true;
            }
            FileWriter f1 = null;
            if (writeLogFile) {
                f1 = new FileWriter("hot-deploy/gudao/webapp/gudao/error/refreshPm-" + "-" + today + ".log", true);
            }
            
            EntityCondition condition = null;
            if (UtilValidate.isNotEmpty(productIdQuery)) {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("productId",EntityOperator.EQUALS ,productIdQuery)
                                                                          ));
            } else if (UtilValidate.isNotEmpty(productGroupQuery)) {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("productGroup",EntityOperator.EQUALS ,productGroupQuery)
                                                                          ));
            } else if (UtilValidate.isNotEmpty(productIdList)) {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("productId",EntityOperator.IN ,productIdList)
                                                                          ));
            } else if (UtilValidate.isNotEmpty(productGroupList)) {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("productGroup",EntityOperator.IN ,productGroupList)
                                                                          ));
            }
            List<GenericValue> productMasterList = delegator.findList("ProductMaster", condition, null, UtilMisc.toList("productId"), null, false);
            
            for (GenericValue productMaster : productMasterList) {  //loop productMasterList -- START
                String productId = productMaster.getString("productId");
                BigDecimal price = productMaster.getBigDecimal("actualPrice");
                String weight = productMaster.getString("weight");
                String actualWeight = productMaster.getString("actualWeight");
                String productType = productMaster.getString("productType");
                
                //get CP -- START
                if (writeLogFile) {
                    f1.write("Start CP calculation" + eol);
                }
                double usPriceDb = 0;
                GenericValue usPriceGV = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "MANUAL", "site", "US"), false);
                BigDecimal usPriceBD = BigDecimal.ZERO;
                if (UtilValidate.isNotEmpty(usPriceGV)) {
                    usPriceBD = usPriceGV.getBigDecimal("price");
                }
                usPriceDb = usPriceBD.doubleValue();
                if (writeLogFile) {
                    f1.write("CP price: " + usPriceDb + eol);
                }
                //get CP -- END
                
                //CALCULATION PART -- START ---------------------------
                double usdToRmb = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CNY"), null, false)).getDouble("conversionFactor");
                double usdToGbp = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "GBP"), null, false)).getDouble("conversionFactor");
                double usdToAud = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "AUD"), null, false)).getDouble("conversionFactor");
                double usdToCad = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CAD"), null, false)).getDouble("conversionFactor");
                double usdToEur = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "EUR"), null, false)).getDouble("conversionFactor");
                double hkdToRmb = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","HKD", "uomIdTo", "CNY"), null, false)).getDouble("conversionFactor");
                double usdToUsd = 1;
                //Debug.logError("price: " + price, module);
                double productCost = price.doubleValue();
                //Debug.logError("productCost: " + productCost, module);
                double packagingCost = Double.parseDouble(EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "packagingCost"), null, false)).getString("value"));
                //Debug.logError("packagingCost: " + packagingCost, module);
                double ebayFeePct = getFeePct(delegator, "ebayFeeBreakUS", usPriceDb);
                //Debug.logError("ebayFeePct: " + ebayFeePct, module);
                //get CP percentage -- START
                double weightDb = Double.parseDouble(weight);
                if (UtilValidate.isNotEmpty(actualWeight)) {
                    weightDb = Double.parseDouble(actualWeight);
                }
                //Debug.logError("WeightDb: " + weightDb, module);
                double shippingCostUS = 0.0;
                double discountRateUS = 0.0;
                double pricePerGramUS = 0.0;
                double additionalShipCostUS = 0.0;
                
                if (writeLogFile) {
                    f1.write("Calculating CP percentage" + eol);
                }
                
                //get SwissPost == START
                double swissPostWeightBreak = 35.0;
                List<GenericValue> pmVariableSwissPostWeightBreak = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "weightBreakShipping", "name", "CHINAPOST", "value", "SWISSPOST"), null, false);
                if (UtilValidate.isNotEmpty(pmVariableSwissPostWeightBreak)) {
                    swissPostWeightBreak = EntityUtil.getFirst(pmVariableSwissPostWeightBreak).getDouble("weightBreakHi");
                }
                //get SwissPost == END
                
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostUS = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                else {  //if weight is less than 2000 - ship by normal shipment - START
                    //Debug.logError("WeightDb is lower than 2KG", module);
                    //Debug.logError("productType is " + productType, module);
                    GenericValue productTypeShippingMethod = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productTypeShippingMethod", "name", productType), null, false));
                    String carrierId = productTypeShippingMethod.getString("value");
                    //Debug.logError("carrierId is " + carrierId, module);
                    List<GenericValue> priceBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", carrierId), null, false);
                    String priceBreakShippingValue = "REGISTERED";
                    
                    for (GenericValue priceBreakShipping : priceBreakShippingList) {    //loop priceBreakShippingList -- START
                        BigDecimal priceBreakLoBD = priceBreakShipping.getBigDecimal("priceBreakLo");
                        BigDecimal priceBreakHiBD = priceBreakShipping.getBigDecimal("priceBreakHi");
                        if (((usPriceDb * usdToRmb) >= priceBreakLoBD.doubleValue()) && ((usPriceDb * usdToRmb) < priceBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                            priceBreakShippingValue = priceBreakShipping.getString("value");
                            break;
                        }   //if CP is lower than priceBreakLo -- END
                    }   //loop priceBreakShippingList -- END
                    //Debug.logError("priceBreakShippingValue is " + priceBreakShippingValue, module);
                    //calculate Swiss Post == START
                    if (carrierId.equals("CHINAPOST")) {
                        if (weightDb <= swissPostWeightBreak) {
                            carrierId = "SWISSPOST";
                            priceBreakShippingValue = "STANDARD";
                        }
                    }
                    //calculate Swiss Post == END
                    //Debug.logError("Final priceBreakShippingValue is " + priceBreakShippingValue, module);
                    GenericValue shippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", priceBreakShippingValue), false);
                    discountRateUS = Double.valueOf(shippingCostGV.getLong("discountRate"));
                    pricePerGramUS = shippingCostGV.getDouble("pricePerGram");
                    if (UtilValidate.isNotEmpty(shippingCostGV.getDouble("additionalCost"))) {
                        additionalShipCostUS = shippingCostGV.getDouble("additionalCost");
                    }
                    shippingCostUS = (weightDb * pricePerGramUS + additionalShipCostUS) * (1.0 - (discountRateUS/100.0));
                }   //if weight is less than 2000 - ship by normal shipment - END
                
                double cpPercentageDb = ((usPriceDb * usdToRmb * (1.0 - (ebayFeePct / 100.0))) - shippingCostUS - productCost - packagingCost) / (usPriceDb * usdToRmb);
                //get CP percentage -- START
                
                if(showPmDebug()) {
                    Debug.logError("pricePerGram: " + pricePerGramUS, module);
                    Debug.logError("additionalShipCost: " + additionalShipCostUS, module);
                    Debug.logError("shippingCost: " + shippingCostUS, module);
                    Debug.logError("ebayFeePct: " + ebayFeePct, module);
                    Debug.logError("cpPercentageDb: " + cpPercentageDb, module);
                    Debug.logError("usPriceDb: " + usPriceDb, module);
                }
                
                if (writeLogFile) {
                    f1.write("Get CP Price done" + eol);
                    f1.write("Start calculating UK price" + eol);
                }
                
                //get UK -- START
                GenericValue productTypeShippingMethod = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productTypeShippingMethod", "name", productType), null, false));
                String carrierId = productTypeShippingMethod.getString("value");
                List<GenericValue> costBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", carrierId), null, false);
                String costBreakShippingValue = "REGISTERED";
                
                GenericValue marketplaceProfitUK = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "UK"), null, false));
                GenericValue lowestPriceUK = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "UK"), null, false));
                String profitPercentUKStr = marketplaceProfitUK.getString("value");
                String ukLowestProfitPercentage = lowestPriceUK.getString("priceBreakLo");
                String lowestPriceUKStr = lowestPriceUK.getString("value");
                double ukPriceDb = usPriceDb * usdToGbp * (1.0 + (Double.parseDouble(profitPercentUKStr)/100.0));
                //Debug.logError("First price ukPriceDb: " + ukPriceDb, module);
                if (usPriceDb == 0.99 && cpPercentageDb > 0.3) {
                    ukPriceDb = 0.99;
                }
                if (ukPriceDb < Double.parseDouble(lowestPriceUKStr)) {
                    ukPriceDb = Double.parseDouble(lowestPriceUKStr);
                }
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((ukPriceDb / usdToGbp * usdToRmb) >= costBreakLoBD.doubleValue()) && ((ukPriceDb / usdToGbp * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                
                //calculate Swiss Post == START
                if (carrierId.equals("CHINAPOST")) {
                    if (weightDb <= swissPostWeightBreak) {
                        carrierId = "SWISSPOST";
                        costBreakShippingValue = "STANDARD";
                    }
                }
                //calculate Swiss Post == END
                //Debug.logError("UK carrierId: " + carrierId + ", method: " + costBreakShippingValue , module);
                GenericValue shippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                double discountRate = Double.valueOf(shippingCostGV.getLong("discountRate"));
                double pricePerGram = shippingCostGV.getDouble("pricePerGram");
                double additionalShipCost = 0.0;
                if (UtilValidate.isNotEmpty(shippingCostGV.getDouble("additionalCost"))) {
                    additionalShipCost = shippingCostGV.getDouble("additionalCost");
                }
                double shippingCost = (weightDb * pricePerGram + additionalShipCost) * (1.0 - (discountRate/100.0));
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCost = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                
                double ebayFeePctGbp = getFeePct(delegator, "ebayFeeBreakUK", ukPriceDb);
                double gbpPercentageDb = ((ukPriceDb / usdToGbp * usdToRmb * (1.0 - (ebayFeePctGbp / 100.0))) - shippingCost - productCost - packagingCost) / (ukPriceDb / usdToGbp * usdToRmb);
                
                if(showPmDebug()) {
                    Debug.logError("pricePerGram: " + pricePerGram, module);
                    Debug.logError("additionalShipCost: " + additionalShipCost, module);
                    Debug.logError("shippingCost: " + shippingCost, module);
                    Debug.logError("ebayFeePctGbp: " + ebayFeePctGbp, module);
                    Debug.logError("gbpPercentageDb: " + gbpPercentageDb, module);
                    Debug.logError("ukPriceDb: " + ukPriceDb, module);
                }
                //get UK -- END
                
                if (writeLogFile) {
                    f1.write("Get UK Price done" + eol);
                    f1.write("Start calculating AU price" + eol);
                }
                
                //get AU -- START
                GenericValue marketplaceProfitAU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "AU"), null, false));
                GenericValue lowestPriceAU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "AU"), null, false));
                String profitPercentAUStr = marketplaceProfitAU.getString("value");
                String auLowestProfitPercentage = lowestPriceAU.getString("priceBreakLo");
                String lowestPriceAUStr = lowestPriceAU.getString("value");
                double auPriceDb = usPriceDb * usdToAud * (1.0 + (Double.parseDouble(profitPercentAUStr)/100.0));
                if (usPriceDb == 0.99 && cpPercentageDb > 0.3) {
                    auPriceDb = 0.99;
                }
                if (auPriceDb < Double.parseDouble(lowestPriceAUStr)) {
                    auPriceDb = Double.parseDouble(lowestPriceAUStr);
                }
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((auPriceDb / usdToAud * usdToRmb) >= costBreakLoBD.doubleValue()) && ((auPriceDb / usdToAud * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                
                //calculate Swiss Post == START
                if (carrierId.equals("CHINAPOST")) {
                    if (weightDb <= swissPostWeightBreak) {
                        carrierId = "SWISSPOST";
                        costBreakShippingValue = "STANDARD";
                    }
                } else if (carrierId.equals("SWISSPOST")) {
                    costBreakShippingValue = "STANDARD";
                }
                //calculate Swiss Post == END
                //Debug.logError("Finish AU calculate swiss post", module);
                GenericValue auShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                //Debug.logError("Start AU calculate costBreakShippingValue: " + costBreakShippingValue, module);
                //Debug.logError("Start AU calculate carrierId: " + carrierId, module);
                //Debug.logError("Start AU calculate auShippingCostGV: " + auShippingCostGV, module);
                double auDiscountRate = Double.valueOf(auShippingCostGV.getLong("discountRate"));
                //Debug.logError("Start AU calculate auDiscountRate: " + auDiscountRate, module);
                double auPricePerGram = auShippingCostGV.getDouble("pricePerGram");
                //Debug.logError("Start AU calculate auPricePerGram: " + auPricePerGram, module);
                double auAdditionalShipCost = 0.0;
                if (UtilValidate.isNotEmpty(auShippingCostGV.getDouble("additionalCost"))) {
                    auAdditionalShipCost = auShippingCostGV.getDouble("additionalCost");
                }
                //Debug.logError("Start AU calculate auShippingCost", module);
                double auShippingCost = (weightDb * auPricePerGram + auAdditionalShipCost) * (1.0 - (auDiscountRate/100.0));
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    auShippingCost = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                //Debug.logError("Start AU calculate ebayFee", module);
                double ebayFeePctAud = getFeePct(delegator, "ebayFeeBreakAU", auPriceDb);
                //Debug.logError("finish AU calculate ebayFee", module);
                double audPercentageDb = ((auPriceDb / usdToAud * usdToRmb * (1.0 - (ebayFeePctAud / 100.0))) - auShippingCost - productCost - packagingCost) / (auPriceDb / usdToAud * usdToRmb);
                //get AU -- END
                
                if (writeLogFile) {
                    f1.write("Get AU Price done" + eol);
                    f1.write("Start calculating CA price" + eol);
                }
                
                //get CA -- START
                GenericValue marketplaceProfitCA = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "CA"), null, false));
                GenericValue lowestPriceCA = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "CA"), null, false));
                String profitPercentCAStr = marketplaceProfitCA.getString("value");
                String caLowestProfitPercentage = lowestPriceCA.getString("priceBreakLo");
                String lowestPriceCAStr = lowestPriceCA.getString("value");
                double caPriceDb = usPriceDb * usdToCad * (1.0 + (Double.parseDouble(profitPercentCAStr)/100.0));
                if (usPriceDb == 0.99 && cpPercentageDb > 0.3) {
                    caPriceDb = 0.99;
                }
                if (caPriceDb < Double.parseDouble(lowestPriceCAStr)) {
                    caPriceDb = Double.parseDouble(lowestPriceCAStr);
                }
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((caPriceDb / usdToCad * usdToRmb) >= costBreakLoBD.doubleValue()) && ((caPriceDb / usdToCad * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                
                //calculate Swiss Post == START
                if (carrierId.equals("CHINAPOST")) {
                    if (weightDb <= swissPostWeightBreak) {
                        carrierId = "SWISSPOST";
                        costBreakShippingValue = "STANDARD";
                    }
                } else if (carrierId.equals("SWISSPOST")) {
                    costBreakShippingValue = "STANDARD";
                }
                //calculate Swiss Post == END
                
                GenericValue caShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                double caDiscountRate = Double.valueOf(caShippingCostGV.getLong("discountRate"));
                double caPricePerGram = caShippingCostGV.getDouble("pricePerGram");
                double caAdditionalShipCost = 0.0;
                if (UtilValidate.isNotEmpty(caShippingCostGV.getDouble("additionalCost"))) {
                    caAdditionalShipCost = caShippingCostGV.getDouble("additionalCost");
                }
                double caShippingCost = (weightDb * caPricePerGram + caAdditionalShipCost) * (1.0 - (caDiscountRate/100.0));
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    caShippingCost = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                
                double ebayFeePctCad = getFeePct(delegator, "ebayFeeBreakCA", caPriceDb);
                double cadPercentageDb = ((caPriceDb / usdToCad * usdToRmb * (1.0 - (ebayFeePctCad / 100.0))) - caShippingCost - productCost - packagingCost) / (caPriceDb / usdToCad * usdToRmb);
                //get CA -- END
                
                if (writeLogFile) {
                    f1.write("Get CA Price done" + eol);
                    f1.write("Start calculating EU price" + eol);
                }
                
                //get Eur -- START
                GenericValue marketplaceProfitEU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "LT"), null, false));
                String profitPercentEUStr = marketplaceProfitEU.getString("value");
                double eurPercentageDb = Double.parseDouble(profitPercentEUStr) / 100.0;
                double euPriceDb = 0.0;
                String eurCarrierId = "DEUTSCHEPOST";
                String eurShipMethType = "STANDARD";
                /*if (carrierId.equals("YANWEN")) {
                 eurCarrierId = carrierId;
                 eurShipMethType = costBreakShippingValue;
                 }*/
                
                //calculate Swiss Post == START
                if (carrierId.equals("CHINAPOST")) {
                    if (weightDb <= swissPostWeightBreak) {
                        eurCarrierId = "SWISSPOST";
                        eurShipMethType = "STANDARD";
                    }
                } else if (carrierId.equals("SWISSPOST")) {
                    costBreakShippingValue = "STANDARD";
                }
                //calculate Swiss Post == END
                
                GenericValue shippingCostEurGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", eurCarrierId, "shippingMethodType", eurShipMethType), false);
                double discountRateEur = Double.valueOf(shippingCostEurGV.getLong("discountRate"));
                double pricePerGramEur = shippingCostEurGV.getBigDecimal("pricePerGram").doubleValue();
                double additionalShipCostEur = 0.0;
                if (UtilValidate.isNotEmpty(shippingCostEurGV.getDouble("additionalCost"))) {
                    additionalShipCostEur = shippingCostEurGV.getDouble("additionalCost");
                }
                
                double shippingCostEur = (weightDb * pricePerGramEur + additionalShipCostEur) * (1.0 - (discountRateEur/100.0)) * hkdToRmb;
                /*if (carrierId.equals("YANWEN")) {
                 shippingCostEur = (weightDb * pricePerGramEur + additionalShipCostEur) * (1.0 - (discountRateEur/100.0));
                 }*/
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostEur = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                List<GenericValue> ebayFeeBreakList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "ebayFeeBreak"), null, false);
                double ebayFeePctEur = 0.0;
                String ebayFeeBreakId = null;
                boolean breakForLoop = false;
                for (GenericValue ebayFeeBreak : ebayFeeBreakList) {    //loop ebayFeeBreakList -- START
                    BigDecimal priceBreakLoBD = ebayFeeBreak.getBigDecimal("priceBreakLo");
                    BigDecimal priceBreakHiBD = ebayFeeBreak.getBigDecimal("priceBreakHi");
                    String ebayFeeBreakStr = ebayFeeBreak.getString("value");
                    ebayFeePctEur = Double.parseDouble(ebayFeeBreakStr);
                    euPriceDb = usdToEur / usdToRmb * ((shippingCostEur + productCost + packagingCost) / (1.0 - eurPercentageDb - (ebayFeePctEur/100.0)));
                    
                    for (GenericValue ebayFeeBreakInner : ebayFeeBreakList) {   //inner loop -- START
                        BigDecimal priceBreakLoBD2 = ebayFeeBreakInner.getBigDecimal("priceBreakLo");
                        BigDecimal priceBreakHiBD2 = ebayFeeBreakInner.getBigDecimal("priceBreakHi");
                        if ((euPriceDb >= priceBreakLoBD2.doubleValue()) && (euPriceDb < priceBreakHiBD2.doubleValue())) {  //if euPriceDb is within range -- START
                            ebayFeeBreakId = ebayFeeBreakInner.getString("pmVariableId");
                            breakForLoop = true;
                            break;
                        }   //if euPriceDb is within range -- END
                    }   //inner loop -- END
                    if (breakForLoop) {
                        break;
                    }
                }   //loop ebayFeeBreakList -- END
                GenericValue finalEbayFeePctEurGV = delegator.findOne("ProductMasterVariable", UtilMisc.toMap("pmVariableId", ebayFeeBreakId), false);
                String finalEbayFeeStr = finalEbayFeePctEurGV.getString("value");
                ebayFeePctEur = Double.parseDouble(finalEbayFeeStr);
                euPriceDb = usdToEur / usdToRmb * ((shippingCostEur + productCost + packagingCost) / (1.0 - eurPercentageDb - (ebayFeePctEur/100.0)));
                eurPercentageDb = ((euPriceDb / usdToEur * usdToRmb * (1.0 - (ebayFeePctEur / 100.0))) - shippingCostEur - productCost - packagingCost) / (euPriceDb / usdToEur * usdToRmb);
                
                GenericValue lowestPriceEU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "LT"), null, false));
                String lowestPriceEUStr = lowestPriceEU.getString("value");
                if (euPriceDb < Double.parseDouble(lowestPriceEUStr)) {
                    euPriceDb = Double.parseDouble(lowestPriceEUStr);
                    eurPercentageDb = ((euPriceDb / usdToEur * usdToRmb * (1.0 - (ebayFeePctEur / 100.0))) - shippingCostEur - productCost - packagingCost) / (euPriceDb / usdToEur * usdToRmb);
                }
                
                //get Eur -- END
                
                if (writeLogFile) {
                    f1.write("Get EU Price done" + eol);
                    f1.write("Start calculating SMT price" + eol);
                }
                
                //get SMT -- START
                double smtPriceDb = 0.0;
                double smtPercentageDb = 0.0;
                String smtShippingMethodType = null;
                double shippingCostSmt = 0.0;
                double smtFeePct = getFeePct(delegator, "smtFeeBreak", smtPriceDb);
                double priceBreakRangeDb = 0.0;
                GenericValue marketplaceProfitSmt = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "SMT"), null, false));
                String profitPercentSmtStr = marketplaceProfitSmt.getString("value");
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostSmt = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                    smtPercentageDb = Double.parseDouble(profitPercentSmtStr) / 100.0;
                    smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                    smtShippingMethodType = "EMS";
                    smtFeePct = getFeePct(delegator, "smtFeeBreak", smtPriceDb);
                    smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                }   //if weight is more than 2000 - ship by EMS - END
                else {  //weight is less than 2000 -- START
                    List<GenericValue> smtShippingList = delegator.findByAnd("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT"), null, false);
                    for (GenericValue smtShippingGV : smtShippingList) {    //loop smtShippingList -- START
                        smtShippingMethodType = smtShippingGV.getString("shippingMethodType");
                        double discountRateSmt = Double.valueOf(smtShippingGV.getLong("discountRate"));
                        double pricePerGramSmt = smtShippingGV.getDouble("pricePerGram");
                        double additionalShipCostSmt = 0.0;
                        if (UtilValidate.isNotEmpty(smtShippingGV.getDouble("additionalCost"))) {
                            additionalShipCostSmt = smtShippingGV.getDouble("additionalCost");
                        }
                        if (carrierId.equals("YANWEN") && smtShippingMethodType.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                            discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", smtShippingMethodType), false).getLong("discountRate"));
                        }   //if shipped by Yanwen -- END
                        shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                        smtPercentageDb = Double.parseDouble(profitPercentSmtStr) / 100.0;
                        smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                        
                        GenericValue priceBreakShippingSmt = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "SMT", "value", smtShippingMethodType), null, false));
                        BigDecimal smtPriceBreakLoBD = priceBreakShippingSmt.getBigDecimal("priceBreakLo");
                        BigDecimal smtPriceBreakHiBD = priceBreakShippingSmt.getBigDecimal("priceBreakHi");
                        priceBreakRangeDb = smtPriceBreakLoBD.doubleValue();
                        if ((smtPriceDb >= smtPriceBreakLoBD.doubleValue()) && (smtPriceDb < smtPriceBreakHiBD.doubleValue())) { //if -- START
                            break;
                        }   //if  -- END
                    }   //loop smtShippingList -- END
                    smtFeePct = getFeePct(delegator, "smtFeeBreak", smtPriceDb);
                    smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                    //Debug.logError("===========before priceBreakRange============", module);
                    //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                    //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                    //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                    //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                    
                    GenericValue priceBreakRangeGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakRange", "name", "SMT"), null, false));
                    String priceBreakRangeStr = priceBreakRangeGV.getString("value");
                    if (smtPriceDb <= (priceBreakRangeDb + Double.parseDouble(priceBreakRangeStr))) {   //if price is within price break range -- START
                        List<GenericValue> priceBreakShippingSmtList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "SMT"), null, false);
                        for (GenericValue priceBreakRangeSmt : priceBreakShippingSmtList) { //loop priceBreakShippingSmtList -- START
                            BigDecimal smtPriceBreakLoBD = priceBreakRangeSmt.getBigDecimal("priceBreakLo");
                            BigDecimal smtPriceBreakHiBD = priceBreakRangeSmt.getBigDecimal("priceBreakHi");
                            if (((priceBreakRangeDb - 0.1) >= smtPriceBreakLoBD.doubleValue()) && ((priceBreakRangeDb - 0.1) < smtPriceBreakHiBD.doubleValue())) { //if -- START
                                String finalShippingMethodSmt = priceBreakRangeSmt.getString("value");
                                GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", finalShippingMethodSmt), false);
                                double discountRateSmt = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                                if (carrierId.equals("YANWEN") && finalShippingMethodSmt.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                                    discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodSmt), false).getLong("discountRate"));
                                }   //if shipped by Yanwen -- END
                                
                                double pricePerGramSmt = finalShippingCostGV.getDouble("pricePerGram");
                                double additionalShipCostSmt = 0.0;
                                if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                                    additionalShipCostSmt = finalShippingCostGV.getDouble("additionalCost");
                                }
                                shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                                smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                                smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                                smtShippingMethodType = finalShippingMethodSmt;
                                break;
                            }   //if  -- END
                        }   //loop priceBreakShippingSmtList -- END
                    }   //if price is within price break range -- END
                    //Debug.logError("===========First LOOP ============", module);
                    //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                    //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                    //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                    //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                    
                    //this loop is to change the price which falls down above 7 USD, but still ship by standard, so change it back to registered, and then change the price again to still get the 15% profit
                    List<GenericValue> finalPriceBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "SMT"), null, false);
                    for (GenericValue finalPriceBreakShipping : finalPriceBreakShippingList) { //final loop finalPriceBreakShippingList -- START
                        BigDecimal finalPriceBreakLoBD = finalPriceBreakShipping.getBigDecimal("priceBreakLo");
                        BigDecimal finalPriceBreakHiBD = finalPriceBreakShipping.getBigDecimal("priceBreakHi");
                        if ((smtPriceDb >= finalPriceBreakLoBD.doubleValue()) && (smtPriceDb < finalPriceBreakHiBD.doubleValue())) {
                            //Debug.logError("finalPriceBreakLoBD: " + finalPriceBreakLoBD, module);
                            //Debug.logError("finalPriceBreakHiBD: " + finalPriceBreakHiBD, module);
                            //Debug.logError("smtPriceDb: " + smtPriceDb, module);
                            String finalShippingMethodSmt = finalPriceBreakShipping.getString("value");
                            if (!smtShippingMethodType.equals(finalShippingMethodSmt)) {
                                GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", finalShippingMethodSmt), false);
                                double discountRateSmt = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                                if (carrierId.equals("YANWEN") && finalShippingMethodSmt.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                                    discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodSmt), false).getLong("discountRate"));
                                }   //if shipped by Yanwen -- END
                                
                                double pricePerGramSmt = finalShippingCostGV.getDouble("pricePerGram");
                                double additionalShipCostSmt = 0.0;
                                if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                                    additionalShipCostSmt = finalShippingCostGV.getDouble("additionalCost");
                                }
                                shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                                smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                                smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                                smtShippingMethodType = finalShippingMethodSmt;
                                //Debug.logError("===========Second LOOP ============", module);
                                //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                                //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                                //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                                //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                                break;
                            }
                        }
                    }   //final loop priceBreakShippingSmtList -- END
                    
                    if (carrierId.equals("YANWEN") && smtShippingMethodType.equals("STANDARD")) {
                        //Debug.logError("==========Third loop to check if shipped by Yanwen and standard, change to SMT standard=============", module);
                        GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", smtShippingMethodType), false);
                        double discountRateSmt = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                        double pricePerGramSmt = finalShippingCostGV.getDouble("pricePerGram");
                        double additionalShipCostSmt = 0.0;
                        if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                            additionalShipCostSmt = finalShippingCostGV.getDouble("additionalCost");
                        }
                        shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                        smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                        smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                        //Debug.logError("===========Yanwen-STANDARD LOOP - change to SMT STANDARD ============", module);
                        //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                        //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                        //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                        //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                        
                        //this loop is to change the price which falls down above 7 USD, but still ship by standard, so change it back to registered, and then change the price again to still get the 15% profit
                        for (GenericValue finalPriceBreakShipping : finalPriceBreakShippingList) { //final loop finalPriceBreakShippingList -- START
                            BigDecimal finalPriceBreakLoBD = finalPriceBreakShipping.getBigDecimal("priceBreakLo");
                            BigDecimal finalPriceBreakHiBD = finalPriceBreakShipping.getBigDecimal("priceBreakHi");
                            if ((smtPriceDb >= finalPriceBreakLoBD.doubleValue()) && (smtPriceDb < finalPriceBreakHiBD.doubleValue())) {
                                //Debug.logError("smtPriceDb: " + smtPriceDb, module);
                                String finalShippingMethodSmt = finalPriceBreakShipping.getString("value");
                                if (!smtShippingMethodType.equals(finalShippingMethodSmt)) {
                                    GenericValue finalShippingCostGV2 = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", finalShippingMethodSmt), false);
                                    discountRateSmt = Double.valueOf(finalShippingCostGV2.getLong("discountRate"));
                                    if (carrierId.equals("YANWEN") && finalShippingMethodSmt.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                                        discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodSmt), false).getLong("discountRate"));
                                    }   //if shipped by Yanwen -- END
                                    
                                    pricePerGramSmt = finalShippingCostGV2.getDouble("pricePerGram");
                                    additionalShipCostSmt = 0.0;
                                    if (UtilValidate.isNotEmpty(finalShippingCostGV2.getDouble("additionalCost"))) {
                                        additionalShipCostSmt = finalShippingCostGV2.getDouble("additionalCost");
                                    }
                                    shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                                    smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                                    smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                                    smtShippingMethodType = finalShippingMethodSmt;
                                    //Debug.logError("===========Yanwen-STANDARD LOOP - change to SMT STANDARD ---  THIRD LOOP ============", module);
                                    //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                                    //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                                    //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                                    //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                                    break;
                                }
                            }
                        }   //final loop priceBreakShippingSmtList -- END
                    }
                    
                }   //weight is less than 2000 -- END
                
                if(showPmDebug()) {
                    Debug.logError("===========Final ============", module);
                    Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                    Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                    Debug.logError("smtPriceDb : " + smtPriceDb, module);
                    Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                }
                
                //get SMT -- END
                
                if (writeLogFile) {
                    f1.write("Get SMT Price done" + eol);
                    f1.write("Start calculating WISH price" + eol);
                }
                
                //get WISH -- START
                double shippingCostWish = 0.0;
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostWish = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                else { //weight is less than 2000 -- START
                    GenericValue wishShippingGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST", "shippingMethodType", "REGISTERED"), false);
                    double discountRateWish = Double.valueOf(wishShippingGV.getLong("discountRate"));
                    if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                        discountRateWish = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", "REGISTERED"), false).getLong("discountRate"));
                    }   //if shipped by Yanwen -- END
                    double pricePerGramWish = wishShippingGV.getDouble("pricePerGram");
                    double additionalShipCostWish = 0.0;
                    if (UtilValidate.isNotEmpty(wishShippingGV.getDouble("additionalCost"))) {
                        additionalShipCostWish = wishShippingGV.getDouble("additionalCost");
                    }
                    shippingCostWish = (weightDb * pricePerGramWish + additionalShipCostWish) * (1.0 - (discountRateWish/100.0));
                } //weight is less than 2000 -- END
                
                
                double wishFeePct = getFeePct(delegator, "wishFeeBreak", 0);
                GenericValue marketplaceProfitWish = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "WISH"), null, false));
                String profitPercentWishStr = marketplaceProfitWish.getString("value");
                double wishPercentageDb = Double.parseDouble(profitPercentWishStr) / 100.0;
                double wishPriceDb = 1.0 / usdToRmb * ((shippingCostWish + productCost + packagingCost) / (1.0 - (wishFeePct/100.0) - wishPercentageDb));
                GenericValue productShippingRatioGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productShippingRatio", "name", "WISH"), null, false));
                String productShippingRatioWish = productShippingRatioGV.getString("value");
                double wishFinalProductPrice = Math.ceil(wishPriceDb * Double.parseDouble(productShippingRatioWish) / 100.0);
                double wishFinalShippingPrice = Math.ceil(wishPriceDb * (1.0 - (Double.parseDouble(productShippingRatioWish) / 100.0)));
                double wishFinalPrice = wishFinalProductPrice + wishFinalShippingPrice;
                double wishProfitPercentage = ((wishFinalPrice * usdToRmb * (1.0 - (wishFeePct / 100.0))) - shippingCostWish - productCost - packagingCost) / (wishFinalPrice * usdToRmb);
                
                GenericValue overPriceCorrection = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "overPriceCorrection", "name", "WISH"), null, false));
                double overPricePct = (overPriceCorrection.getDouble("priceBreakHi")) / 100.0;
                double overPriceLoPct = (overPriceCorrection.getDouble("priceBreakLo")) / 100.0;
                double overPriceDeduction = Double.parseDouble(overPriceCorrection.getString("value"));
                if (wishProfitPercentage > overPricePct) {
                    wishFinalProductPrice = wishFinalProductPrice - overPriceDeduction;
                    wishProfitPercentage = (((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb * (1.0 - (wishFeePct / 100.0))) - shippingCostWish - productCost - packagingCost) / ((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb);
                    if (wishProfitPercentage < overPriceLoPct) {
                        wishFinalProductPrice = wishFinalProductPrice + overPriceDeduction;
                        wishProfitPercentage = (((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb * (1.0 - (wishFeePct / 100.0))) - shippingCostWish - productCost - packagingCost) / ((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb);
                    }
                }
                
                //get WISH -- END
                
                if (writeLogFile) {
                    f1.write("Get WISH Price done" + eol);
                    f1.write("Start calculating AMAZON price" + eol);
                }
                
                //get AMAZON -- START
                double shippingCostAma = 0.0;
                double amaPriceDb = 0.0;
                double amaPercentageDb = 0.0;
                double amaFeePct = getFeePct(delegator, "amaFeeBreak", 0);
                double amaPriceBreakRangeDb = 0.0;
                String amaShippingMethodTypeFinal = null;
                GenericValue marketplaceProfitAma = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "AMA"), null, false));
                String profitPercentAmaStr = marketplaceProfitAma.getString("value");
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostAma = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                    amaPercentageDb = Double.parseDouble(profitPercentAmaStr) / 100.0;
                    amaPriceDb = 1.0 / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                    
                }   //if weight is more than 2000 - ship by EMS - END
                else { //weight is less than 2000 -- START
                    
                    List<GenericValue> amaShippingList = delegator.findByAnd("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST"), null, false);
                    for (GenericValue amaShippingGV : amaShippingList) {    //loop amaShippingList -- START
                        String amaShippingMethodType = amaShippingGV.getString("shippingMethodType");
                        double discountRateAma = Double.valueOf(amaShippingGV.getLong("discountRate"));
                        double pricePerGramAma = amaShippingGV.getDouble("pricePerGram");
                        double additionalShipCostAma = 0.0;
                        if (UtilValidate.isNotEmpty(amaShippingGV.getDouble("additionalCost"))) {
                            additionalShipCostAma = amaShippingGV.getDouble("additionalCost");
                        }
                        if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                            discountRateAma = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", amaShippingMethodType), false).getLong("discountRate"));
                        }   //if shipped by Yanwen -- END
                        shippingCostAma = (weightDb * pricePerGramAma + additionalShipCostAma) * (1.0 - (discountRateAma/100.0));
                        amaPercentageDb = Double.parseDouble(profitPercentAmaStr) / 100.0;
                        amaPriceDb = 1.0 / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                        
                        GenericValue priceBreakShippingAma = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "AMA", "value", amaShippingMethodType), null, false));
                        BigDecimal amaPriceBreakLoBD = priceBreakShippingAma.getBigDecimal("priceBreakLo");
                        BigDecimal amaPriceBreakHiBD = priceBreakShippingAma.getBigDecimal("priceBreakHi");
                        String priceBreakShippingAmaValue = priceBreakShippingAma.getString("value");
                        if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                            amaPriceDb = (amaPriceDb * ( 1 - amaFeePct / 100.0)) + 1;
                        }
                        
                        if (((amaPriceDb * usdToRmb) >= amaPriceBreakLoBD.doubleValue()) && ((amaPriceDb * usdToRmb) < amaPriceBreakHiBD.doubleValue())) { //if -- START
                            if (priceBreakShippingAmaValue.equals(amaShippingMethodType)) {
                                amaShippingMethodTypeFinal = amaShippingMethodType;
                                amaPriceBreakRangeDb = amaPriceBreakLoBD.doubleValue();
                                break;
                            }
                        }   //if  -- END
                    }   //loop amaShippingList -- END
                }   //weight is less than 2000 -- END
                
                //recalculate the amaPercentageDb instead of using a fixed value from database
                amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                
                //if fvf is lower than 1USD, recalculate the amaPercentageDb using fvf 1USD
                if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                }
                
                //if amaPercentageDb is lower than the marketplaceprofit sets in database, recalculate the amaPriceDb so that it can reach the marketplaceprofit% from database and then recalculate the profit%
                if (amaPercentageDb < (Double.parseDouble(profitPercentAmaStr) / 100.0)) {
                    amaPriceDb = (1 - amaPercentageDb) / (1 - Double.parseDouble(profitPercentAmaStr) / 100.0) * amaPriceDb;
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    
                    //always check if the fee is below 1USD or not
                    if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                        amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    }
                }
                
                GenericValue amaPriceBreakRangeGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakRange", "name", "AMA"), null, false));
                String amaPriceBreakRangeStr = amaPriceBreakRangeGV.getString("value");
                if (amaPriceDb <= (amaPriceBreakRangeDb + Double.parseDouble(amaPriceBreakRangeStr))) {   //if price is within price break range -- START
                    List<GenericValue> priceBreakShippingAmaList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "AMA"), null, false);
                    for (GenericValue priceBreakRangeAma : priceBreakShippingAmaList) { //loop priceBreakShippingAmaList -- START
                        BigDecimal amaPriceBreakLoBD = priceBreakRangeAma.getBigDecimal("priceBreakLo");
                        BigDecimal amaPriceBreakHiBD = priceBreakRangeAma.getBigDecimal("priceBreakHi");
                        if (((amaPriceBreakRangeDb - 0.1) >= amaPriceBreakLoBD.doubleValue()) && ((amaPriceBreakRangeDb - 0.1) < amaPriceBreakHiBD.doubleValue())) { //if -- START
                            String finalShippingMethodAma = priceBreakRangeAma.getString("value");
                            GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST", "shippingMethodType", finalShippingMethodAma), false);
                            double discountRateAma = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                            if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                                discountRateAma = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodAma), false).getLong("discountRate"));
                            }   //if shipped by Yanwen -- END
                            double pricePerGramAma = finalShippingCostGV.getDouble("pricePerGram");
                            double additionalShipCostAma = 0.0;
                            if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                                additionalShipCostAma = finalShippingCostGV.getDouble("additionalCost");
                            }
                            shippingCostAma = (weightDb * pricePerGramAma + additionalShipCostAma) * (1.0 - (discountRateAma / 100.0));
                            amaPriceDb = 1.0 / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                            amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCostAma - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                            break;
                        }   //if  -- END
                    }   //loop priceBreakShippingAmaList -- END
                }   //if price is within price break range -- END
                
                
                //this loop is to change the price which falls down above 30 USD, but still ship by standard, so change it back to registered, and then change the price again to still get the 15% profit
                List<GenericValue> amaFinalPriceBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "AMA"), null, false);
                for (GenericValue amaFinalPriceBreakShipping : amaFinalPriceBreakShippingList) { //final loop amaFinalPriceBreakShippingList -- START
                    BigDecimal finalPriceBreakLoBD = amaFinalPriceBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal finalPriceBreakHiBD = amaFinalPriceBreakShipping.getBigDecimal("priceBreakHi");
                    if ((amaPriceDb >= finalPriceBreakLoBD.doubleValue()) && (amaPriceDb < finalPriceBreakHiBD.doubleValue())) {
                        String finalShippingMethodAma = amaFinalPriceBreakShipping.getString("value");
                        GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST", "shippingMethodType", finalShippingMethodAma), false);
                        double discountRateAma = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                        if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                            discountRateAma = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodAma), false).getLong("discountRate"));
                        }   //if shipped by Yanwen -- END
                        double pricePerGramAma = finalShippingCostGV.getDouble("pricePerGram");
                        double additionalShipCostAma = 0.0;
                        if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                            additionalShipCostAma = finalShippingCostGV.getDouble("additionalCost");
                        }
                        shippingCostAma = (weightDb * pricePerGramAma + additionalShipCostAma) * (1.0 - (discountRateAma/100.0));
                        amaPercentageDb = Double.parseDouble(profitPercentAmaStr) / 100.0;
                        amaPriceDb = usdToUsd / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                        amaShippingMethodTypeFinal = finalShippingMethodAma;
                        break;
                    }
                }   //final loop amaFinalPriceBreakShippingList -- END
                
                //if fvf is lower than 1USD, recalculate the amaPercentageDb using fvf 1USD
                if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                }
                
                //if amaPercentageDb is lower than the marketplaceprofit sets in database, recalculate the amaPriceDb so that it can reach the marketplaceprofit% from database and then recalculate the profit%
                if (amaPercentageDb < (Double.parseDouble(profitPercentAmaStr) / 100.0)) {
                    amaPriceDb = (1 - amaPercentageDb) / (1 - Double.parseDouble(profitPercentAmaStr) / 100.0) * amaPriceDb;
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    
                    //always check if the fee is below 1USD or not
                    if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                        amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    }
                }
                //get AMAZON -- END
                
                //CALCULATION PART -- END ---------------------------
                
                
                //Update EBAY Prices == START
                //US site
                GenericValue priceEbayUS = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "US"), false);
                if (UtilValidate.isEmpty(priceEbayUS)) {
                    priceEbayUS = delegator.makeValue("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "US"));
                }
                priceEbayUS.set("price", new BigDecimal(usPriceDb));
                priceEbayUS.set("profitPercentage", cpPercentageDb);
                priceEbayUS.set("currencyId", "USD");
                delegator.createOrStore(priceEbayUS);
                
                //UK site
                GenericValue priceEbayUK = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "UK"), false);
                if (UtilValidate.isEmpty(priceEbayUK)) {
                    priceEbayUK = delegator.makeValue("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "UK"));
                }
                priceEbayUK.set("price", new BigDecimal(ukPriceDb));
                priceEbayUK.set("profitPercentage", gbpPercentageDb);
                priceEbayUK.set("currencyId", "GBP");
                delegator.createOrStore(priceEbayUK);
                
                //AU site
                GenericValue priceEbayAU = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "AU"), false);
                if (UtilValidate.isEmpty(priceEbayAU)) {
                    priceEbayAU = delegator.makeValue("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "AU"));
                }
                priceEbayAU.set("price", new BigDecimal(auPriceDb));
                priceEbayAU.set("profitPercentage", audPercentageDb);
                priceEbayAU.set("currencyId", "AUD");
                delegator.createOrStore(priceEbayAU);
                
                //CA site
                GenericValue priceEbayCA = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "CA"), false);
                if (UtilValidate.isEmpty(priceEbayCA)) {
                    priceEbayCA = delegator.makeValue("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "CA"));
                }
                priceEbayCA.set("price", new BigDecimal(caPriceDb));
                priceEbayCA.set("profitPercentage", cadPercentageDb);
                priceEbayCA.set("currencyId", "CAD");
                delegator.createOrStore(priceEbayCA);
                
                //DE site
                GenericValue priceEbayDE = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "DE"), false);
                if (UtilValidate.isEmpty(priceEbayDE)) {
                    priceEbayDE = delegator.makeValue("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "DE"));
                }
                priceEbayDE.set("price", new BigDecimal(euPriceDb));
                priceEbayDE.set("profitPercentage", eurPercentageDb);
                priceEbayDE.set("currencyId", "EUR");
                delegator.createOrStore(priceEbayDE);
                
                //FR site
                GenericValue priceEbayFR = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "FR"), false);
                if (UtilValidate.isEmpty(priceEbayFR)) {
                    priceEbayFR = delegator.makeValue("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "EBAY", "type", "CALCULATED", "site", "FR"));
                }
                priceEbayFR.set("price", new BigDecimal(euPriceDb));
                priceEbayFR.set("profitPercentage", eurPercentageDb);
                priceEbayFR.set("currencyId", "EUR");
                delegator.createOrStore(priceEbayFR);
                
                //Update EBAY Prices == END
                
                //Update SMT Prices == START
                GenericValue priceSmtUS = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "SMT", "type", "CALCULATED", "site", "US"), false);
                if (UtilValidate.isEmpty(priceSmtUS)) {
                    priceSmtUS = delegator.makeValue("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "SMT", "type", "CALCULATED", "site", "US"));
                }
                priceSmtUS.set("price", new BigDecimal(smtPriceDb));
                priceSmtUS.set("profitPercentage", smtPercentageDb);
                priceSmtUS.set("currencyId", "USD");
                delegator.createOrStore(priceSmtUS);
                //Update SMT Prices == END
                
                //Update WISH Prices == START
                GenericValue priceWishUS = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "WISH", "type", "CALCULATED", "site", "US"), false);
                if (UtilValidate.isEmpty(priceWishUS)) {
                    priceWishUS = delegator.makeValue("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "WISH", "type", "CALCULATED", "site", "US"));
                }
                priceWishUS.set("price", new BigDecimal(wishFinalProductPrice));
                priceWishUS.set("shippingPrice", new BigDecimal(wishFinalShippingPrice));
                priceWishUS.set("profitPercentage", wishProfitPercentage);
                priceWishUS.set("currencyId", "USD");
                delegator.createOrStore(priceWishUS);
                //Update WISH Prices == END
                
                //Update AMAZON Prices == START
                GenericValue priceAmazonUS = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "AMAZON", "type", "CALCULATED", "site", "US"), false);
                if (UtilValidate.isEmpty(priceAmazonUS)) {
                    priceAmazonUS = delegator.makeValue("ProductMasterPrice", UtilMisc.toMap("productId", productId, "platform", "AMAZON", "type", "CALCULATED", "site", "US"));
                }
                priceAmazonUS.set("price", new BigDecimal(amaPriceDb));
                priceAmazonUS.set("profitPercentage", amaPercentageDb);
                priceAmazonUS.set("currencyId", "USD");
                delegator.createOrStore(priceAmazonUS);
                //Update AMAZON Prices == END
                
                
            }   //loop productMasterList -- END
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return result;
        
    }   //gudaoRefreshProductMaster
    
    public static Map<String, Object> gudaoRefreshProductMasterBackup (DispatchContext dctx, Map context) {   //gudaoGenerateProductMaster
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productIdQuery = (String) context.get("productId");
        String productGroupQuery = (String) context.get("productGroupQuery");
        List<String> productGroupList = (List) context.get("productGroupList");
        List<String> productIdList = (List) context.get("productIdList");
        Map result = ServiceUtil.returnSuccess();
        //Debug.logError("Start refreshing PM, productID: " + productIdQuery, module);
        //String text = (String) context.get("Text");
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(now.getTime());
        
        try {   //start try block
            boolean writeLogFile = false;
            boolean writeLogFileDetail = false;
            if (pmDebug().get("refreshPmWriteToFile").toUpperCase().equals("Y")) {
                writeLogFile = true;
            }
            if (pmDebug().get("refreshPmWriteToFileDetail").toUpperCase().equals("Y")) {
                writeLogFileDetail = true;
            }
            FileWriter f1 = null;
            if (writeLogFile) {
                f1 = new FileWriter("hot-deploy/gudao/webapp/gudao/error/refreshPm-" + "-" + today + ".log", true);
            }
            
            EntityCondition condition = null;
            if (UtilValidate.isNotEmpty(productIdQuery)) {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("productId",EntityOperator.EQUALS ,productIdQuery)
                                                                          ));
            } else if (UtilValidate.isNotEmpty(productGroupQuery)) {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("productGroup",EntityOperator.EQUALS ,productGroupQuery)
                                                                          ));
            } else if (UtilValidate.isNotEmpty(productIdList)) {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("productId",EntityOperator.IN ,productIdList)
                                                                          ));
            } else if (UtilValidate.isNotEmpty(productGroupList)) {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("productGroup",EntityOperator.IN ,productGroupList)
                                                                          ));
            }

            /*List<GenericValue> productMasterList = delegator.findList("ProductMaster", EntityCondition.makeCondition(UtilMisc.toList(
             EntityCondition.makeCondition("productGroup",EntityOperator.NOT_LIKE ,"G5%")
             ))
             , null, UtilMisc.toList("productId"), null, false);*/
            
            /*List<GenericValue> productMasterList = delegator.findList("ProductMaster", EntityCondition.makeCondition(UtilMisc.toList(
             EntityCondition.makeCondition("productId",EntityOperator.EQUALS ,"05-0005")
             ))
             , null, UtilMisc.toList("productId"), null, false);*/
            List<GenericValue> productMasterList = delegator.findList("ProductMaster", condition, null, UtilMisc.toList("productId"), null, false);
            //GenericValue productMaster = EntityUtil.getFirst(productMasterList);
            //Debug.logError("run untilhere", module);
            for (GenericValue productMaster : productMasterList) {  //loop productMasterList -- START
                if (writeLogFile) {
                    f1.write("===================================================================================" + eol);
                    f1.write("ProductId: " + productMaster.getString("productId") + eol);
                }
                Map<String, Object> content = FastMap.newInstance();
                
                String productId = productMaster.getString("productId");
                String customSku = productMaster.getString("customSku");
                String motherSku = productMaster.getString("motherSku");
                String setSku = productMaster.getString("setSku");
                String chineseName = productMaster.getString("chineseName");
                String englishName = productMaster.getString("englishName");
                String declaredNameCn = productMaster.getString("declaredNameCn");
                String declaredNameEn = productMaster.getString("declaredNameEn");
                String weight = productMaster.getString("weight");
                String actualWeight = productMaster.getString("actualWeight");
                String productGroup = productMaster.getString("productGroup");
                String statusId = productMaster.getString("statusId");
                String categoryId = productMaster.getString("categoryId");
                String categoryIdParent = productMaster.getString("categoryIdParent");
                String categoryIdChild = productMaster.getString("categoryIdChild");
                String productType = productMaster.getString("productType");
                String ownerGroup = productMaster.getString("ownerGroup");
                String purchaser = productMaster.getString("purchaser");
                String imageLink = productMaster.getString("imageLink");
                String extraStockingDay = productMaster.getString("extraStockingDay");
                Date sourcingDate = productMaster.getDate("sourcingDate");
                String notes = productMaster.getString("notes");
                String listingNotes = productMaster.getString("listingNotes");
                String upc = productMaster.getString("upc");
                String pictureReady = productMaster.getString("pictureReady");
                String createdBy = productMaster.getString("createdBy");
                
                //get CP -- START
                if (writeLogFile) {
                    f1.write("Start CP calculation" + eol);
                }
                double usPriceDb = 0;
                GenericValue usPriceGV = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "currencyId", "USD"), false);
                BigDecimal usPriceBD = BigDecimal.ZERO;
                if (UtilValidate.isNotEmpty(usPriceGV)) {
                    usPriceBD = usPriceGV.getBigDecimal("price");
                }
                usPriceDb = usPriceBD.doubleValue();
                if (writeLogFile) {
                    f1.write("CP price: " + usPriceDb + eol);
                }
                //get CP -- END
                
                //get supplier -- START
                long eta = 0;
                long minOrderDays = 0;
                long minOrderQuantity = 0;
                
                if (writeLogFile) {
                    f1.write("Getting Main Supplier" + eol);
                }
                
                //get Supplier price == START
                GenericValue mainSupplierGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterSupplier", UtilMisc.toMap("productId", productId, "supplierType", "MAIN"), null, false));
                if (writeLogFileDetail) {
                    f1.write("MainSupplierGV: " + mainSupplierGV + eol);
                }
                String supplier = mainSupplierGV.getString("supplier");
                String supplierType = mainSupplierGV.getString("supplierType");
                String supplierLink = mainSupplierGV.getString("supplierLink");
                
                Date priceLastUpdate = mainSupplierGV.getDate("priceLastUpdate");
                BigDecimal price = mainSupplierGV.getBigDecimal("actualPrice");
                
                if (UtilValidate.isNotEmpty(mainSupplierGV.getLong("eta"))) {
                    eta = mainSupplierGV.getLong("eta");
                }
                if (UtilValidate.isNotEmpty(mainSupplierGV.getLong("minOrderDays"))) {
                    minOrderDays = mainSupplierGV.getLong("minOrderDays");
                }
                
                if (writeLogFile) {
                    f1.write("Getting Main Supplier Price" + eol);
                }
                List<GenericValue> supplierPriceGVList = delegator.findByAnd("SupplierPrice", UtilMisc.toMap("productId", productId, "supplier", supplier, "type", "MIN_QUANTITY"), null, false);
                GenericValue supplierPriceGV = null;
                if (UtilValidate.isNotEmpty(supplierPriceGVList)) {
                    supplierPriceGV = EntityUtil.getFirst(supplierPriceGVList);
                    //price = supplierPriceGV.getBigDecimal("unitPrice");
                    if (UtilValidate.isNotEmpty(supplierPriceGV.getBigDecimal("minimumOrderValue"))) {
                        minOrderQuantity = supplierPriceGV.getBigDecimal("minimumOrderValue").longValue();
                    } else {
                        minOrderQuantity = 1;
                    }
                    
                } else {
                    //price = mainSupplierGV.getBigDecimal("price");
                    if (UtilValidate.isNotEmpty(mainSupplierGV.getLong("minOrderQuantity"))) {
                        minOrderQuantity = mainSupplierGV.getLong("minOrderQuantity");
                    } else {
                        minOrderQuantity = 1;
                    }
                    
                }
                
                //get Supplier price == END
                
                if (writeLogFile) {
                    f1.write("Getting Backup Supplier" + eol);
                }
                List<GenericValue> backupSupplierList = delegator.findByAnd("ProductMasterSupplier", UtilMisc.toMap("productId", productId, "supplierType", "BACKUP"), null, false);
                String backupSupplier = null;
                String backupSupplierType = null;
                String backupSupplierLink = null;
                BigDecimal backupSupplierPrice = BigDecimal.ZERO;
                long backupSupplierEta = 0;
                long backupMinOrderDays = 0;
                long backupMinOrderQuantity = 0;
                Date backupPriceLastUpdate = null;
                if (UtilValidate.isNotEmpty(backupSupplierList)) { //if backupSupplierList is not empty -- START
                    GenericValue backupSupplierGV = EntityUtil.getFirst(backupSupplierList);
                    backupSupplier = backupSupplierGV.getString("supplier");
                    backupSupplierType = backupSupplierGV.getString("supplierType");
                    backupSupplierLink = backupSupplierGV.getString("supplierLink");
                    backupSupplierPrice = backupSupplierGV.getBigDecimal("actualPrice");
                    
                    List<GenericValue> backupSupplierPriceGVList = delegator.findByAnd("SupplierPrice", UtilMisc.toMap("productId", productId, "supplier", supplier, "type", "MIN_QUANTITY"), null, false);
                    GenericValue backupSupplierPriceGV = null;
                    if (UtilValidate.isNotEmpty(backupSupplierPriceGVList)) {
                        backupSupplierPriceGV = EntityUtil.getFirst(backupSupplierPriceGVList);
                        //backupSupplierPrice = backupSupplierPriceGV.getBigDecimal("unitPrice");
                        if (UtilValidate.isNotEmpty(backupSupplierPriceGV.getBigDecimal("minimumOrderValue"))) {
                            backupMinOrderQuantity = backupSupplierPriceGV.getBigDecimal("minimumOrderValue").longValue();
                        } else {
                            backupMinOrderQuantity = 1;
                        }
                        
                    } else {
                        //backupSupplierPrice = backupSupplierGV.getBigDecimal("price");
                        if (UtilValidate.isNotEmpty(backupSupplierGV.getLong("minOrderQuantity"))) {
                            backupMinOrderQuantity = backupSupplierGV.getLong("minOrderQuantity");
                        }
                    }
                    
                    if (UtilValidate.isNotEmpty(backupSupplierGV.getDate("priceLastUpdate"))) {
                        backupPriceLastUpdate = backupSupplierGV.getDate("priceLastUpdate");
                    }
                    if (UtilValidate.isNotEmpty(backupSupplierGV.getLong("eta"))) {
                        backupSupplierEta = backupSupplierGV.getLong("eta");
                    }
                    if (UtilValidate.isNotEmpty(backupSupplierGV.getLong("minOrderDays"))) {
                        backupMinOrderDays = backupSupplierGV.getLong("minOrderDays");
                    }
                }   //if backupSupplierList is not empty -- END
                
                
                //get supplier -- END
                
                if (writeLogFile) {
                    f1.write("Getting rival item" + eol);
                }
                
                //get rival -- START
                String rivalItemId = null;
                String rivalLink = null;
                String crawl = null;
                double soldPerDay = 0.0;
                double samePriceProfitPercentage = 0.0;
                
                List<GenericValue> rivalList = null;
                EntityCondition identicalConditionList = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                       EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId),
                                                                                                       EntityCondition.makeCondition("rivalItemId", EntityOperator.NOT_EQUAL, null)
                                                                                                       ));
                
                List<GenericValue> identicalRivalList = delegator.findList("ProductMasterRival", identicalConditionList, null, null, null, false);
                if (UtilValidate.isNotEmpty(identicalRivalList)) {
                    rivalList = identicalRivalList;
                } else {
                    rivalList = delegator.findList("ProductMasterRival",
                                                   EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                 EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId),
                                                                                                 EntityCondition.makeCondition("similarItemId", EntityOperator.NOT_EQUAL, null)
                                                                                                 ))
                                                   , null, null, null, false);
                }
                
                if (UtilValidate.isNotEmpty(rivalList)) {   //if rivalList is not empty -- START
                    GenericValue rivalGV = EntityUtil.getFirst(rivalList);
                    
                    if (UtilValidate.isNotEmpty(rivalGV.getString("similarItemId"))) {
                        rivalItemId = rivalGV.getString("similarItemId");
                    }
                    Debug.logError("Rival item ID: " + rivalItemId, module);
                    if (UtilValidate.isNotEmpty(rivalGV.getString("rivalItemId"))) {
                        rivalItemId = rivalGV.getString("rivalItemId");
                    }
                    Debug.logError("Rival item ID: " + rivalItemId, module);
                    if (UtilValidate.isNotEmpty(rivalGV.getString("rivalLink"))) {
                        rivalLink = rivalGV.getString("rivalLink");
                    }
                    if (UtilValidate.isNotEmpty(rivalGV.getString("soldPerDay"))) {
                        soldPerDay = rivalGV.getDouble("soldPerDay");
                    }
                    if (UtilValidate.isNotEmpty(rivalGV.getString("samePriceProfitPercentage"))) {
                        samePriceProfitPercentage = rivalGV.getDouble("samePriceProfitPercentage");
                    }
                    if (UtilValidate.isNotEmpty(rivalGV.getString("crawl"))) {
                        crawl = rivalGV.getString("crawl");
                    }
                }   //if rivalList is not empty -- END
                //get rival -- END
                
                if (writeLogFile) {
                    f1.write("rivalItemId: " + rivalItemId + eol);
                    f1.write("Start calculation part" + eol);
                }
                
                //CALCULATION PART -- START ---------------------------
                double usdToRmb = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CNY"), null, false)).getDouble("conversionFactor");
                double usdToGbp = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "GBP"), null, false)).getDouble("conversionFactor");
                double usdToAud = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "AUD"), null, false)).getDouble("conversionFactor");
                double usdToCad = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CAD"), null, false)).getDouble("conversionFactor");
                double usdToEur = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "EUR"), null, false)).getDouble("conversionFactor");
                double hkdToRmb = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","HKD", "uomIdTo", "CNY"), null, false)).getDouble("conversionFactor");
                double usdToUsd = 1;
                //Debug.logError("price: " + price, module);
                double productCost = price.doubleValue();
                //Debug.logError("productCost: " + productCost, module);
                double packagingCost = Double.parseDouble(EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "packagingCost"), null, false)).getString("value"));
                //Debug.logError("packagingCost: " + packagingCost, module);
                double ebayFeePct = getFeePct(delegator, "ebayFeeBreakUS", usPriceDb);
                //Debug.logError("ebayFeePct: " + ebayFeePct, module);
                //get CP percentage -- START
                double weightDb = Double.parseDouble(weight);
                if (UtilValidate.isNotEmpty(actualWeight)) {
                    weightDb = Double.parseDouble(actualWeight);
                }
                //Debug.logError("WeightDb: " + weightDb, module);
                double shippingCostUS = 0.0;
                double discountRateUS = 0.0;
                double pricePerGramUS = 0.0;
                double additionalShipCostUS = 0.0;
                
                if (writeLogFile) {
                    f1.write("Calculating CP percentage" + eol);
                }
                
                //get SwissPost == START
                double swissPostWeightBreak = 35.0;
                List<GenericValue> pmVariableSwissPostWeightBreak = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "weightBreakShipping", "name", "CHINAPOST", "value", "SWISSPOST"), null, false);
                if (UtilValidate.isNotEmpty(pmVariableSwissPostWeightBreak)) {
                    swissPostWeightBreak = EntityUtil.getFirst(pmVariableSwissPostWeightBreak).getDouble("weightBreakHi");
                }
                //get SwissPost == END
                
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostUS = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                else {  //if weight is less than 2000 - ship by normal shipment - START
                    //Debug.logError("WeightDb is lower than 2KG", module);
                    //Debug.logError("productType is " + productType, module);
                    GenericValue productTypeShippingMethod = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productTypeShippingMethod", "name", productType), null, false));
                    String carrierId = productTypeShippingMethod.getString("value");
                    //Debug.logError("carrierId is " + carrierId, module);
                    List<GenericValue> priceBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", carrierId), null, false);
                    String priceBreakShippingValue = "REGISTERED";
                    
                    for (GenericValue priceBreakShipping : priceBreakShippingList) {    //loop priceBreakShippingList -- START
                        BigDecimal priceBreakLoBD = priceBreakShipping.getBigDecimal("priceBreakLo");
                        BigDecimal priceBreakHiBD = priceBreakShipping.getBigDecimal("priceBreakHi");
                        if (((usPriceDb * usdToRmb) >= priceBreakLoBD.doubleValue()) && ((usPriceDb * usdToRmb) < priceBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                            priceBreakShippingValue = priceBreakShipping.getString("value");
                            break;
                        }   //if CP is lower than priceBreakLo -- END
                    }   //loop priceBreakShippingList -- END
                    //Debug.logError("priceBreakShippingValue is " + priceBreakShippingValue, module);
                    //calculate Swiss Post == START
                    if (carrierId.equals("CHINAPOST")) {
                        if (weightDb <= swissPostWeightBreak) {
                            carrierId = "SWISSPOST";
                            priceBreakShippingValue = "STANDARD";
                        }
                    }
                    //calculate Swiss Post == END
                    //Debug.logError("Final priceBreakShippingValue is " + priceBreakShippingValue, module);
                    GenericValue shippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", priceBreakShippingValue), false);
                    discountRateUS = Double.valueOf(shippingCostGV.getLong("discountRate"));
                    pricePerGramUS = shippingCostGV.getDouble("pricePerGram");
                    if (UtilValidate.isNotEmpty(shippingCostGV.getDouble("additionalCost"))) {
                        additionalShipCostUS = shippingCostGV.getDouble("additionalCost");
                    }
                    shippingCostUS = (weightDb * pricePerGramUS + additionalShipCostUS) * (1.0 - (discountRateUS/100.0));
                }   //if weight is less than 2000 - ship by normal shipment - END
                
                double cpPercentageDb = ((usPriceDb * usdToRmb * (1.0 - (ebayFeePct / 100.0))) - shippingCostUS - productCost - packagingCost) / (usPriceDb * usdToRmb);
                //get CP percentage -- START
                
                if(showPmDebug()) {
                    Debug.logError("pricePerGram: " + pricePerGramUS, module);
                    Debug.logError("additionalShipCost: " + additionalShipCostUS, module);
                    Debug.logError("shippingCost: " + shippingCostUS, module);
                    Debug.logError("ebayFeePct: " + ebayFeePct, module);
                    Debug.logError("cpPercentageDb: " + cpPercentageDb, module);
                    Debug.logError("usPriceDb: " + usPriceDb, module);
                }
                
                if (writeLogFile) {
                    f1.write("Get CP Price done" + eol);
                    f1.write("Start calculating UK price" + eol);
                }
                
                //get UK -- START
                GenericValue productTypeShippingMethod = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productTypeShippingMethod", "name", productType), null, false));
                String carrierId = productTypeShippingMethod.getString("value");
                List<GenericValue> costBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", carrierId), null, false);
                String costBreakShippingValue = "REGISTERED";
                
                GenericValue marketplaceProfitUK = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "UK"), null, false));
                GenericValue lowestPriceUK = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "UK"), null, false));
                String profitPercentUKStr = marketplaceProfitUK.getString("value");
                String ukLowestProfitPercentage = lowestPriceUK.getString("priceBreakLo");
                String lowestPriceUKStr = lowestPriceUK.getString("value");
                double ukPriceDb = usPriceDb * usdToGbp * (1.0 + (Double.parseDouble(profitPercentUKStr)/100.0));
                //Debug.logError("First price ukPriceDb: " + ukPriceDb, module);
                if (usPriceDb == 0.99 && cpPercentageDb > 0.3) {
                    ukPriceDb = 0.99;
                }
                if (ukPriceDb < Double.parseDouble(lowestPriceUKStr)) {
                    ukPriceDb = Double.parseDouble(lowestPriceUKStr);
                }
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((ukPriceDb / usdToGbp * usdToRmb) >= costBreakLoBD.doubleValue()) && ((ukPriceDb / usdToGbp * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                
                //calculate Swiss Post == START
                if (carrierId.equals("CHINAPOST")) {
                    if (weightDb <= swissPostWeightBreak) {
                        carrierId = "SWISSPOST";
                        costBreakShippingValue = "STANDARD";
                    }
                }
                //calculate Swiss Post == END
                //Debug.logError("UK carrierId: " + carrierId + ", method: " + costBreakShippingValue , module);
                GenericValue shippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                double discountRate = Double.valueOf(shippingCostGV.getLong("discountRate"));
                double pricePerGram = shippingCostGV.getDouble("pricePerGram");
                double additionalShipCost = 0.0;
                if (UtilValidate.isNotEmpty(shippingCostGV.getDouble("additionalCost"))) {
                    additionalShipCost = shippingCostGV.getDouble("additionalCost");
                }
                double shippingCost = (weightDb * pricePerGram + additionalShipCost) * (1.0 - (discountRate/100.0));
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCost = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END

                double ebayFeePctGbp = getFeePct(delegator, "ebayFeeBreakUK", ukPriceDb);
                double gbpPercentageDb = ((ukPriceDb / usdToGbp * usdToRmb * (1.0 - (ebayFeePctGbp / 100.0))) - shippingCost - productCost - packagingCost) / (ukPriceDb / usdToGbp * usdToRmb);
                
                if(showPmDebug()) {
                    Debug.logError("pricePerGram: " + pricePerGram, module);
                    Debug.logError("additionalShipCost: " + additionalShipCost, module);
                    Debug.logError("shippingCost: " + shippingCost, module);
                    Debug.logError("ebayFeePctGbp: " + ebayFeePctGbp, module);
                    Debug.logError("gbpPercentageDb: " + gbpPercentageDb, module);
                    Debug.logError("ukPriceDb: " + ukPriceDb, module);
                }
                //get UK -- END
                
                if (writeLogFile) {
                    f1.write("Get UK Price done" + eol);
                    f1.write("Start calculating AU price" + eol);
                }
                
                //get AU -- START
                GenericValue marketplaceProfitAU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "AU"), null, false));
                GenericValue lowestPriceAU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "AU"), null, false));
                String profitPercentAUStr = marketplaceProfitAU.getString("value");
                String auLowestProfitPercentage = lowestPriceAU.getString("priceBreakLo");
                String lowestPriceAUStr = lowestPriceAU.getString("value");
                double auPriceDb = usPriceDb * usdToAud * (1.0 + (Double.parseDouble(profitPercentAUStr)/100.0));
                if (usPriceDb == 0.99 && cpPercentageDb > 0.3) {
                    auPriceDb = 0.99;
                }
                if (auPriceDb < Double.parseDouble(lowestPriceAUStr)) {
                    auPriceDb = Double.parseDouble(lowestPriceAUStr);
                }
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((auPriceDb / usdToAud * usdToRmb) >= costBreakLoBD.doubleValue()) && ((auPriceDb / usdToAud * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                
                //calculate Swiss Post == START
                if (carrierId.equals("CHINAPOST")) {
                    if (weightDb <= swissPostWeightBreak) {
                        carrierId = "SWISSPOST";
                        costBreakShippingValue = "STANDARD";
                    }
                } else if (carrierId.equals("SWISSPOST")) {
                    costBreakShippingValue = "STANDARD";
                }
                //calculate Swiss Post == END
                //Debug.logError("Finish AU calculate swiss post", module);
                GenericValue auShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                //Debug.logError("Start AU calculate costBreakShippingValue: " + costBreakShippingValue, module);
                //Debug.logError("Start AU calculate carrierId: " + carrierId, module);
                //Debug.logError("Start AU calculate auShippingCostGV: " + auShippingCostGV, module);
                double auDiscountRate = Double.valueOf(auShippingCostGV.getLong("discountRate"));
                //Debug.logError("Start AU calculate auDiscountRate: " + auDiscountRate, module);
                double auPricePerGram = auShippingCostGV.getDouble("pricePerGram");
                //Debug.logError("Start AU calculate auPricePerGram: " + auPricePerGram, module);
                double auAdditionalShipCost = 0.0;
                if (UtilValidate.isNotEmpty(auShippingCostGV.getDouble("additionalCost"))) {
                    auAdditionalShipCost = auShippingCostGV.getDouble("additionalCost");
                }
                //Debug.logError("Start AU calculate auShippingCost", module);
                double auShippingCost = (weightDb * auPricePerGram + auAdditionalShipCost) * (1.0 - (auDiscountRate/100.0));
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    auShippingCost = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                //Debug.logError("Start AU calculate ebayFee", module);
                double ebayFeePctAud = getFeePct(delegator, "ebayFeeBreakAU", auPriceDb);
                //Debug.logError("finish AU calculate ebayFee", module);
                double audPercentageDb = ((auPriceDb / usdToAud * usdToRmb * (1.0 - (ebayFeePctAud / 100.0))) - auShippingCost - productCost - packagingCost) / (auPriceDb / usdToAud * usdToRmb);
                //get AU -- END
                
                if (writeLogFile) {
                    f1.write("Get AU Price done" + eol);
                    f1.write("Start calculating CA price" + eol);
                }
                
                //get CA -- START
                GenericValue marketplaceProfitCA = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "CA"), null, false));
                GenericValue lowestPriceCA = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "CA"), null, false));
                String profitPercentCAStr = marketplaceProfitCA.getString("value");
                String caLowestProfitPercentage = lowestPriceCA.getString("priceBreakLo");
                String lowestPriceCAStr = lowestPriceCA.getString("value");
                double caPriceDb = usPriceDb * usdToCad * (1.0 + (Double.parseDouble(profitPercentCAStr)/100.0));
                if (usPriceDb == 0.99 && cpPercentageDb > 0.3) {
                    caPriceDb = 0.99;
                }
                if (caPriceDb < Double.parseDouble(lowestPriceCAStr)) {
                    caPriceDb = Double.parseDouble(lowestPriceCAStr);
                }
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((caPriceDb / usdToCad * usdToRmb) >= costBreakLoBD.doubleValue()) && ((caPriceDb / usdToCad * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                
                //calculate Swiss Post == START
                if (carrierId.equals("CHINAPOST")) {
                    if (weightDb <= swissPostWeightBreak) {
                        carrierId = "SWISSPOST";
                        costBreakShippingValue = "STANDARD";
                    }
                } else if (carrierId.equals("SWISSPOST")) {
                    costBreakShippingValue = "STANDARD";
                }
                //calculate Swiss Post == END
                
                GenericValue caShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                double caDiscountRate = Double.valueOf(caShippingCostGV.getLong("discountRate"));
                double caPricePerGram = caShippingCostGV.getDouble("pricePerGram");
                double caAdditionalShipCost = 0.0;
                if (UtilValidate.isNotEmpty(caShippingCostGV.getDouble("additionalCost"))) {
                    caAdditionalShipCost = caShippingCostGV.getDouble("additionalCost");
                }
                double caShippingCost = (weightDb * caPricePerGram + caAdditionalShipCost) * (1.0 - (caDiscountRate/100.0));
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    caShippingCost = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                
                double ebayFeePctCad = getFeePct(delegator, "ebayFeeBreakCA", caPriceDb);
                double cadPercentageDb = ((caPriceDb / usdToCad * usdToRmb * (1.0 - (ebayFeePctCad / 100.0))) - caShippingCost - productCost - packagingCost) / (caPriceDb / usdToCad * usdToRmb);
                //get CA -- END
                
                if (writeLogFile) {
                    f1.write("Get CA Price done" + eol);
                    f1.write("Start calculating EU price" + eol);
                }
                
                //get Eur -- START
                GenericValue marketplaceProfitEU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "LT"), null, false));
                String profitPercentEUStr = marketplaceProfitEU.getString("value");
                double eurPercentageDb = Double.parseDouble(profitPercentEUStr) / 100.0;
                double euPriceDb = 0.0;
                String eurCarrierId = "DEUTSCHEPOST";
                String eurShipMethType = "STANDARD";
                /*if (carrierId.equals("YANWEN")) {
                 eurCarrierId = carrierId;
                 eurShipMethType = costBreakShippingValue;
                 }*/
                
                //calculate Swiss Post == START
                if (carrierId.equals("CHINAPOST")) {
                    if (weightDb <= swissPostWeightBreak) {
                        eurCarrierId = "SWISSPOST";
                        eurShipMethType = "STANDARD";
                    }
                } else if (carrierId.equals("SWISSPOST")) {
                    costBreakShippingValue = "STANDARD";
                }
                //calculate Swiss Post == END
                
                GenericValue shippingCostEurGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", eurCarrierId, "shippingMethodType", eurShipMethType), false);
                double discountRateEur = Double.valueOf(shippingCostEurGV.getLong("discountRate"));
                double pricePerGramEur = shippingCostEurGV.getBigDecimal("pricePerGram").doubleValue();
                double additionalShipCostEur = 0.0;
                if (UtilValidate.isNotEmpty(shippingCostEurGV.getDouble("additionalCost"))) {
                    additionalShipCostEur = shippingCostEurGV.getDouble("additionalCost");
                }
                
                double shippingCostEur = (weightDb * pricePerGramEur + additionalShipCostEur) * (1.0 - (discountRateEur/100.0)) * hkdToRmb;
                /*if (carrierId.equals("YANWEN")) {
                 shippingCostEur = (weightDb * pricePerGramEur + additionalShipCostEur) * (1.0 - (discountRateEur/100.0));
                 }*/
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostEur = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                List<GenericValue> ebayFeeBreakList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "ebayFeeBreak"), null, false);
                double ebayFeePctEur = 0.0;
                String ebayFeeBreakId = null;
                boolean breakForLoop = false;
                for (GenericValue ebayFeeBreak : ebayFeeBreakList) {    //loop ebayFeeBreakList -- START
                    BigDecimal priceBreakLoBD = ebayFeeBreak.getBigDecimal("priceBreakLo");
                    BigDecimal priceBreakHiBD = ebayFeeBreak.getBigDecimal("priceBreakHi");
                    String ebayFeeBreakStr = ebayFeeBreak.getString("value");
                    ebayFeePctEur = Double.parseDouble(ebayFeeBreakStr);
                    euPriceDb = usdToEur / usdToRmb * ((shippingCostEur + productCost + packagingCost) / (1.0 - eurPercentageDb - (ebayFeePctEur/100.0)));
                    
                    for (GenericValue ebayFeeBreakInner : ebayFeeBreakList) {   //inner loop -- START
                        BigDecimal priceBreakLoBD2 = ebayFeeBreakInner.getBigDecimal("priceBreakLo");
                        BigDecimal priceBreakHiBD2 = ebayFeeBreakInner.getBigDecimal("priceBreakHi");
                        if ((euPriceDb >= priceBreakLoBD2.doubleValue()) && (euPriceDb < priceBreakHiBD2.doubleValue())) {  //if euPriceDb is within range -- START
                            ebayFeeBreakId = ebayFeeBreakInner.getString("pmVariableId");
                            breakForLoop = true;
                            break;
                        }   //if euPriceDb is within range -- END
                    }   //inner loop -- END
                    if (breakForLoop) {
                        break;
                    }
                }   //loop ebayFeeBreakList -- END
                GenericValue finalEbayFeePctEurGV = delegator.findOne("ProductMasterVariable", UtilMisc.toMap("pmVariableId", ebayFeeBreakId), false);
                String finalEbayFeeStr = finalEbayFeePctEurGV.getString("value");
                ebayFeePctEur = Double.parseDouble(finalEbayFeeStr);
                euPriceDb = usdToEur / usdToRmb * ((shippingCostEur + productCost + packagingCost) / (1.0 - eurPercentageDb - (ebayFeePctEur/100.0)));
                eurPercentageDb = ((euPriceDb / usdToEur * usdToRmb * (1.0 - (ebayFeePctEur / 100.0))) - shippingCostEur - productCost - packagingCost) / (euPriceDb / usdToEur * usdToRmb);
                
                GenericValue lowestPriceEU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "LT"), null, false));
                String lowestPriceEUStr = lowestPriceEU.getString("value");
                if (euPriceDb < Double.parseDouble(lowestPriceEUStr)) {
                    euPriceDb = Double.parseDouble(lowestPriceEUStr);
                    eurPercentageDb = ((euPriceDb / usdToEur * usdToRmb * (1.0 - (ebayFeePctEur / 100.0))) - shippingCostEur - productCost - packagingCost) / (euPriceDb / usdToEur * usdToRmb);
                }
                
                //get Eur -- END
                
                if (writeLogFile) {
                    f1.write("Get EU Price done" + eol);
                    f1.write("Start calculating SMT price" + eol);
                }
                
                //get SMT -- START
                double smtPriceDb = 0.0;
                double smtPercentageDb = 0.0;
                String smtShippingMethodType = null;
                double shippingCostSmt = 0.0;
                double smtFeePct = getFeePct(delegator, "smtFeeBreak", smtPriceDb);
                double priceBreakRangeDb = 0.0;
                GenericValue marketplaceProfitSmt = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "SMT"), null, false));
                String profitPercentSmtStr = marketplaceProfitSmt.getString("value");
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostSmt = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                    smtPercentageDb = Double.parseDouble(profitPercentSmtStr) / 100.0;
                    smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                    smtShippingMethodType = "EMS";
                    smtFeePct = getFeePct(delegator, "smtFeeBreak", smtPriceDb);
                    smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                }   //if weight is more than 2000 - ship by EMS - END
                else {  //weight is less than 2000 -- START
                    List<GenericValue> smtShippingList = delegator.findByAnd("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT"), null, false);
                    for (GenericValue smtShippingGV : smtShippingList) {    //loop smtShippingList -- START
                        smtShippingMethodType = smtShippingGV.getString("shippingMethodType");
                        double discountRateSmt = Double.valueOf(smtShippingGV.getLong("discountRate"));
                        double pricePerGramSmt = smtShippingGV.getDouble("pricePerGram");
                        double additionalShipCostSmt = 0.0;
                        if (UtilValidate.isNotEmpty(smtShippingGV.getDouble("additionalCost"))) {
                            additionalShipCostSmt = smtShippingGV.getDouble("additionalCost");
                        }
                        if (carrierId.equals("YANWEN") && smtShippingMethodType.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                            discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", smtShippingMethodType), false).getLong("discountRate"));
                        }   //if shipped by Yanwen -- END
                        shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                        smtPercentageDb = Double.parseDouble(profitPercentSmtStr) / 100.0;
                        smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                        
                        GenericValue priceBreakShippingSmt = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "SMT", "value", smtShippingMethodType), null, false));
                        BigDecimal smtPriceBreakLoBD = priceBreakShippingSmt.getBigDecimal("priceBreakLo");
                        BigDecimal smtPriceBreakHiBD = priceBreakShippingSmt.getBigDecimal("priceBreakHi");
                        priceBreakRangeDb = smtPriceBreakLoBD.doubleValue();
                        if ((smtPriceDb >= smtPriceBreakLoBD.doubleValue()) && (smtPriceDb < smtPriceBreakHiBD.doubleValue())) { //if -- START
                            break;
                        }   //if  -- END
                    }   //loop smtShippingList -- END
                    smtFeePct = getFeePct(delegator, "smtFeeBreak", smtPriceDb);
                    smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                    //Debug.logError("===========before priceBreakRange============", module);
                    //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                    //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                    //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                    //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                    
                    GenericValue priceBreakRangeGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakRange", "name", "SMT"), null, false));
                    String priceBreakRangeStr = priceBreakRangeGV.getString("value");
                    if (smtPriceDb <= (priceBreakRangeDb + Double.parseDouble(priceBreakRangeStr))) {   //if price is within price break range -- START
                        List<GenericValue> priceBreakShippingSmtList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "SMT"), null, false);
                        for (GenericValue priceBreakRangeSmt : priceBreakShippingSmtList) { //loop priceBreakShippingSmtList -- START
                            BigDecimal smtPriceBreakLoBD = priceBreakRangeSmt.getBigDecimal("priceBreakLo");
                            BigDecimal smtPriceBreakHiBD = priceBreakRangeSmt.getBigDecimal("priceBreakHi");
                            if (((priceBreakRangeDb - 0.1) >= smtPriceBreakLoBD.doubleValue()) && ((priceBreakRangeDb - 0.1) < smtPriceBreakHiBD.doubleValue())) { //if -- START
                                String finalShippingMethodSmt = priceBreakRangeSmt.getString("value");
                                GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", finalShippingMethodSmt), false);
                                double discountRateSmt = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                                if (carrierId.equals("YANWEN") && finalShippingMethodSmt.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                                    discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodSmt), false).getLong("discountRate"));
                                }   //if shipped by Yanwen -- END
                                
                                double pricePerGramSmt = finalShippingCostGV.getDouble("pricePerGram");
                                double additionalShipCostSmt = 0.0;
                                if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                                    additionalShipCostSmt = finalShippingCostGV.getDouble("additionalCost");
                                }
                                shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                                smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                                smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                                smtShippingMethodType = finalShippingMethodSmt;
                                break;
                            }   //if  -- END
                        }   //loop priceBreakShippingSmtList -- END
                    }   //if price is within price break range -- END
                    //Debug.logError("===========First LOOP ============", module);
                    //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                    //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                    //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                    //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                    
                    //this loop is to change the price which falls down above 7 USD, but still ship by standard, so change it back to registered, and then change the price again to still get the 15% profit
                    List<GenericValue> finalPriceBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "SMT"), null, false);
                    for (GenericValue finalPriceBreakShipping : finalPriceBreakShippingList) { //final loop finalPriceBreakShippingList -- START
                        BigDecimal finalPriceBreakLoBD = finalPriceBreakShipping.getBigDecimal("priceBreakLo");
                        BigDecimal finalPriceBreakHiBD = finalPriceBreakShipping.getBigDecimal("priceBreakHi");
                        if ((smtPriceDb >= finalPriceBreakLoBD.doubleValue()) && (smtPriceDb < finalPriceBreakHiBD.doubleValue())) {
                            //Debug.logError("finalPriceBreakLoBD: " + finalPriceBreakLoBD, module);
                            //Debug.logError("finalPriceBreakHiBD: " + finalPriceBreakHiBD, module);
                            //Debug.logError("smtPriceDb: " + smtPriceDb, module);
                            String finalShippingMethodSmt = finalPriceBreakShipping.getString("value");
                            if (!smtShippingMethodType.equals(finalShippingMethodSmt)) {
                                GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", finalShippingMethodSmt), false);
                                double discountRateSmt = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                                if (carrierId.equals("YANWEN") && finalShippingMethodSmt.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                                    discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodSmt), false).getLong("discountRate"));
                                }   //if shipped by Yanwen -- END
                                
                                double pricePerGramSmt = finalShippingCostGV.getDouble("pricePerGram");
                                double additionalShipCostSmt = 0.0;
                                if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                                    additionalShipCostSmt = finalShippingCostGV.getDouble("additionalCost");
                                }
                                shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                                smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                                smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                                smtShippingMethodType = finalShippingMethodSmt;
                                //Debug.logError("===========Second LOOP ============", module);
                                //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                                //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                                //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                                //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                                break;
                            }
                        }
                    }   //final loop priceBreakShippingSmtList -- END
                    
                    if (carrierId.equals("YANWEN") && smtShippingMethodType.equals("STANDARD")) {
                        //Debug.logError("==========Third loop to check if shipped by Yanwen and standard, change to SMT standard=============", module);
                        GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", smtShippingMethodType), false);
                        double discountRateSmt = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                        double pricePerGramSmt = finalShippingCostGV.getDouble("pricePerGram");
                        double additionalShipCostSmt = 0.0;
                        if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                            additionalShipCostSmt = finalShippingCostGV.getDouble("additionalCost");
                        }
                        shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                        smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                        smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                        //Debug.logError("===========Yanwen-STANDARD LOOP - change to SMT STANDARD ============", module);
                        //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                        //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                        //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                        //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                        
                        //this loop is to change the price which falls down above 7 USD, but still ship by standard, so change it back to registered, and then change the price again to still get the 15% profit
                        for (GenericValue finalPriceBreakShipping : finalPriceBreakShippingList) { //final loop finalPriceBreakShippingList -- START
                            BigDecimal finalPriceBreakLoBD = finalPriceBreakShipping.getBigDecimal("priceBreakLo");
                            BigDecimal finalPriceBreakHiBD = finalPriceBreakShipping.getBigDecimal("priceBreakHi");
                            if ((smtPriceDb >= finalPriceBreakLoBD.doubleValue()) && (smtPriceDb < finalPriceBreakHiBD.doubleValue())) {
                                //Debug.logError("smtPriceDb: " + smtPriceDb, module);
                                String finalShippingMethodSmt = finalPriceBreakShipping.getString("value");
                                if (!smtShippingMethodType.equals(finalShippingMethodSmt)) {
                                    GenericValue finalShippingCostGV2 = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", finalShippingMethodSmt), false);
                                    discountRateSmt = Double.valueOf(finalShippingCostGV2.getLong("discountRate"));
                                    if (carrierId.equals("YANWEN") && finalShippingMethodSmt.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                                        discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodSmt), false).getLong("discountRate"));
                                    }   //if shipped by Yanwen -- END
                                    
                                    pricePerGramSmt = finalShippingCostGV2.getDouble("pricePerGram");
                                    additionalShipCostSmt = 0.0;
                                    if (UtilValidate.isNotEmpty(finalShippingCostGV2.getDouble("additionalCost"))) {
                                        additionalShipCostSmt = finalShippingCostGV2.getDouble("additionalCost");
                                    }
                                    shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                                    smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                                    smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                                    smtShippingMethodType = finalShippingMethodSmt;
                                    //Debug.logError("===========Yanwen-STANDARD LOOP - change to SMT STANDARD ---  THIRD LOOP ============", module);
                                    //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                                    //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                                    //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                                    //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                                    break;
                                }
                            }
                        }   //final loop priceBreakShippingSmtList -- END
                    }
                    
                }   //weight is less than 2000 -- END
                
                if(showPmDebug()) {
                    Debug.logError("===========Final ============", module);
                    Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                    Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                    Debug.logError("smtPriceDb : " + smtPriceDb, module);
                    Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                }
                
                //get SMT -- END
                
                if (writeLogFile) {
                    f1.write("Get SMT Price done" + eol);
                    f1.write("Start calculating WISH price" + eol);
                }
                
                //get WISH -- START
                double shippingCostWish = 0.0;
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostWish = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                else { //weight is less than 2000 -- START
                    GenericValue wishShippingGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST", "shippingMethodType", "REGISTERED"), false);
                    double discountRateWish = Double.valueOf(wishShippingGV.getLong("discountRate"));
                    if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                        discountRateWish = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", "REGISTERED"), false).getLong("discountRate"));
                    }   //if shipped by Yanwen -- END
                    double pricePerGramWish = wishShippingGV.getDouble("pricePerGram");
                    double additionalShipCostWish = 0.0;
                    if (UtilValidate.isNotEmpty(wishShippingGV.getDouble("additionalCost"))) {
                        additionalShipCostWish = wishShippingGV.getDouble("additionalCost");
                    }
                    shippingCostWish = (weightDb * pricePerGramWish + additionalShipCostWish) * (1.0 - (discountRateWish/100.0));
                } //weight is less than 2000 -- END
                
                
                double wishFeePct = getFeePct(delegator, "wishFeeBreak", 0);
                GenericValue marketplaceProfitWish = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "WISH"), null, false));
                String profitPercentWishStr = marketplaceProfitWish.getString("value");
                double wishPercentageDb = Double.parseDouble(profitPercentWishStr) / 100.0;
                double wishPriceDb = 1.0 / usdToRmb * ((shippingCostWish + productCost + packagingCost) / (1.0 - (wishFeePct/100.0) - wishPercentageDb));
                GenericValue productShippingRatioGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productShippingRatio", "name", "WISH"), null, false));
                String productShippingRatioWish = productShippingRatioGV.getString("value");
                double wishFinalProductPrice = Math.ceil(wishPriceDb * Double.parseDouble(productShippingRatioWish) / 100.0);
                double wishFinalShippingPrice = Math.ceil(wishPriceDb * (1.0 - (Double.parseDouble(productShippingRatioWish) / 100.0)));
                double wishFinalPrice = wishFinalProductPrice + wishFinalShippingPrice;
                double wishProfitPercentage = ((wishFinalPrice * usdToRmb * (1.0 - (wishFeePct / 100.0))) - shippingCostWish - productCost - packagingCost) / (wishFinalPrice * usdToRmb);
                
                GenericValue overPriceCorrection = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "overPriceCorrection", "name", "WISH"), null, false));
                double overPricePct = (overPriceCorrection.getDouble("priceBreakHi")) / 100.0;
                double overPriceLoPct = (overPriceCorrection.getDouble("priceBreakLo")) / 100.0;
                double overPriceDeduction = Double.parseDouble(overPriceCorrection.getString("value"));
                if (wishProfitPercentage > overPricePct) {
                    wishFinalProductPrice = wishFinalProductPrice - overPriceDeduction;
                    wishProfitPercentage = (((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb * (1.0 - (wishFeePct / 100.0))) - shippingCostWish - productCost - packagingCost) / ((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb);
                    if (wishProfitPercentage < overPriceLoPct) {
                        wishFinalProductPrice = wishFinalProductPrice + overPriceDeduction;
                        wishProfitPercentage = (((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb * (1.0 - (wishFeePct / 100.0))) - shippingCostWish - productCost - packagingCost) / ((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb);
                    }
                }
                
                //get WISH -- END
                
                if (writeLogFile) {
                    f1.write("Get WISH Price done" + eol);
                    f1.write("Start calculating AMAZON price" + eol);
                }
                
                //get AMAZON -- START
                double shippingCostAma = 0.0;
                double amaPriceDb = 0.0;
                double amaPercentageDb = 0.0;
                double amaFeePct = getFeePct(delegator, "amaFeeBreak", 0);
                double amaPriceBreakRangeDb = 0.0;
                String amaShippingMethodTypeFinal = null;
                GenericValue marketplaceProfitAma = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "AMA"), null, false));
                String profitPercentAmaStr = marketplaceProfitAma.getString("value");
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostAma = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                    amaPercentageDb = Double.parseDouble(profitPercentAmaStr) / 100.0;
                    amaPriceDb = 1.0 / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                    
                }   //if weight is more than 2000 - ship by EMS - END
                else { //weight is less than 2000 -- START
                    
                    List<GenericValue> amaShippingList = delegator.findByAnd("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST"), null, false);
                    for (GenericValue amaShippingGV : amaShippingList) {    //loop amaShippingList -- START
                        String amaShippingMethodType = amaShippingGV.getString("shippingMethodType");
                        double discountRateAma = Double.valueOf(amaShippingGV.getLong("discountRate"));
                        double pricePerGramAma = amaShippingGV.getDouble("pricePerGram");
                        double additionalShipCostAma = 0.0;
                        if (UtilValidate.isNotEmpty(amaShippingGV.getDouble("additionalCost"))) {
                            additionalShipCostAma = amaShippingGV.getDouble("additionalCost");
                        }
                        if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                            discountRateAma = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", amaShippingMethodType), false).getLong("discountRate"));
                        }   //if shipped by Yanwen -- END
                        shippingCostAma = (weightDb * pricePerGramAma + additionalShipCostAma) * (1.0 - (discountRateAma/100.0));
                        amaPercentageDb = Double.parseDouble(profitPercentAmaStr) / 100.0;
                        amaPriceDb = 1.0 / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                        
                        GenericValue priceBreakShippingAma = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "AMA", "value", amaShippingMethodType), null, false));
                        BigDecimal amaPriceBreakLoBD = priceBreakShippingAma.getBigDecimal("priceBreakLo");
                        BigDecimal amaPriceBreakHiBD = priceBreakShippingAma.getBigDecimal("priceBreakHi");
                        String priceBreakShippingAmaValue = priceBreakShippingAma.getString("value");
                        if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                            amaPriceDb = (amaPriceDb * ( 1 - amaFeePct / 100.0)) + 1;
                        }
                        
                        if (((amaPriceDb * usdToRmb) >= amaPriceBreakLoBD.doubleValue()) && ((amaPriceDb * usdToRmb) < amaPriceBreakHiBD.doubleValue())) { //if -- START
                            if (priceBreakShippingAmaValue.equals(amaShippingMethodType)) {
                                amaShippingMethodTypeFinal = amaShippingMethodType;
                                amaPriceBreakRangeDb = amaPriceBreakLoBD.doubleValue();
                                break;
                            }
                        }   //if  -- END
                    }   //loop amaShippingList -- END
                }   //weight is less than 2000 -- END
                
                //recalculate the amaPercentageDb instead of using a fixed value from database
                amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                
                //if fvf is lower than 1USD, recalculate the amaPercentageDb using fvf 1USD
                if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                }
                
                //if amaPercentageDb is lower than the marketplaceprofit sets in database, recalculate the amaPriceDb so that it can reach the marketplaceprofit% from database and then recalculate the profit%
                if (amaPercentageDb < (Double.parseDouble(profitPercentAmaStr) / 100.0)) {
                    amaPriceDb = (1 - amaPercentageDb) / (1 - Double.parseDouble(profitPercentAmaStr) / 100.0) * amaPriceDb;
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    
                    //always check if the fee is below 1USD or not
                    if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                        amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    }
                }
                
                GenericValue amaPriceBreakRangeGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakRange", "name", "AMA"), null, false));
                String amaPriceBreakRangeStr = amaPriceBreakRangeGV.getString("value");
                if (amaPriceDb <= (amaPriceBreakRangeDb + Double.parseDouble(amaPriceBreakRangeStr))) {   //if price is within price break range -- START
                    List<GenericValue> priceBreakShippingAmaList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "AMA"), null, false);
                    for (GenericValue priceBreakRangeAma : priceBreakShippingAmaList) { //loop priceBreakShippingAmaList -- START
                        BigDecimal amaPriceBreakLoBD = priceBreakRangeAma.getBigDecimal("priceBreakLo");
                        BigDecimal amaPriceBreakHiBD = priceBreakRangeAma.getBigDecimal("priceBreakHi");
                        if (((amaPriceBreakRangeDb - 0.1) >= amaPriceBreakLoBD.doubleValue()) && ((amaPriceBreakRangeDb - 0.1) < amaPriceBreakHiBD.doubleValue())) { //if -- START
                            String finalShippingMethodAma = priceBreakRangeAma.getString("value");
                            GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST", "shippingMethodType", finalShippingMethodAma), false);
                            double discountRateAma = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                            if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                                discountRateAma = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodAma), false).getLong("discountRate"));
                            }   //if shipped by Yanwen -- END
                            double pricePerGramAma = finalShippingCostGV.getDouble("pricePerGram");
                            double additionalShipCostAma = 0.0;
                            if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                                additionalShipCostAma = finalShippingCostGV.getDouble("additionalCost");
                            }
                            shippingCostAma = (weightDb * pricePerGramAma + additionalShipCostAma) * (1.0 - (discountRateAma / 100.0));
                            amaPriceDb = 1.0 / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                            amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCostAma - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                            break;
                        }   //if  -- END
                    }   //loop priceBreakShippingAmaList -- END
                }   //if price is within price break range -- END
                
                
                //this loop is to change the price which falls down above 30 USD, but still ship by standard, so change it back to registered, and then change the price again to still get the 15% profit
                List<GenericValue> amaFinalPriceBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "AMA"), null, false);
                for (GenericValue amaFinalPriceBreakShipping : amaFinalPriceBreakShippingList) { //final loop amaFinalPriceBreakShippingList -- START
                    BigDecimal finalPriceBreakLoBD = amaFinalPriceBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal finalPriceBreakHiBD = amaFinalPriceBreakShipping.getBigDecimal("priceBreakHi");
                    if ((amaPriceDb >= finalPriceBreakLoBD.doubleValue()) && (amaPriceDb < finalPriceBreakHiBD.doubleValue())) {
                        String finalShippingMethodAma = amaFinalPriceBreakShipping.getString("value");
                        GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST", "shippingMethodType", finalShippingMethodAma), false);
                        double discountRateAma = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                        if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                            discountRateAma = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodAma), false).getLong("discountRate"));
                        }   //if shipped by Yanwen -- END
                        double pricePerGramAma = finalShippingCostGV.getDouble("pricePerGram");
                        double additionalShipCostAma = 0.0;
                        if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                            additionalShipCostAma = finalShippingCostGV.getDouble("additionalCost");
                        }
                        shippingCostAma = (weightDb * pricePerGramAma + additionalShipCostAma) * (1.0 - (discountRateAma/100.0));
                        amaPercentageDb = Double.parseDouble(profitPercentAmaStr) / 100.0;
                        amaPriceDb = usdToUsd / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                        amaShippingMethodTypeFinal = finalShippingMethodAma;
                        break;
                    }
                }   //final loop amaFinalPriceBreakShippingList -- END
                
                //if fvf is lower than 1USD, recalculate the amaPercentageDb using fvf 1USD
                if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                }
                
                //if amaPercentageDb is lower than the marketplaceprofit sets in database, recalculate the amaPriceDb so that it can reach the marketplaceprofit% from database and then recalculate the profit%
                if (amaPercentageDb < (Double.parseDouble(profitPercentAmaStr) / 100.0)) {
                    amaPriceDb = (1 - amaPercentageDb) / (1 - Double.parseDouble(profitPercentAmaStr) / 100.0) * amaPriceDb;
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    
                    //always check if the fee is below 1USD or not
                    if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                        amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    }
                }
                //get AMAZON -- END
                
                //CALCULATION PART -- END ---------------------------
                
                if (writeLogFile) {
                    f1.write("Calculation part done" + eol);
                }
                
                //Get Product Listing Status -- START
                String ebayUSListing = null;
                String ebayUKListing = null;
                String ebayAUListing = null;
                String ebayCAListing = null;
                String ebayDEListing = null;
                String ebayFRListing = null;
                String ebayESListing = null;
                String smtListing = null;
                String amaListing = null;
                String wishListing = null;
                String ebayPicture = null;
                String amazonPicture = null;
                String listingCheck = null;
                String finePicture = null;
                List<GenericValue> productListingStatusList = delegator.findByAnd("ProductListingStatus", UtilMisc.toMap("productId", productId), null, false);
                if (UtilValidate.isNotEmpty(productListingStatusList)) {    // if productListingStatusList is not null -- START
                    for (GenericValue productListingStatus : productListingStatusList) {    //loop productListingStatusList -- START
                        String listingStatusTypeId = productListingStatus.getString("listingStatusTypeId");
                        if (listingStatusTypeId.equals("EBAY_US_LISTING")) {
                            ebayUSListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_UK_LISTING")) {
                            ebayUKListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_AU_LISTING")) {
                            ebayAUListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_CA_LISTING")) {
                            ebayCAListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_DE_LISTING")) {
                            ebayDEListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_FR_LISTING")) {
                            ebayFRListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_ES_LISTING")) {
                            ebayESListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("SMT_LISTING")) {
                            smtListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("AMA_LISTING")) {
                            amaListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("WISH_LISTING")) {
                            wishListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_PICTURE")) {
                            ebayPicture = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("AMAZON_PICTURE")) {
                            amazonPicture = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("LISTING_CHECK")) {
                            listingCheck = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("FINE_PICTURE")) {
                            finePicture = productListingStatus.getString("value");
                        }
                    }   //loop productListingStatusList -- END
                }   // if productListingStatusList is not null -- END
                //Get Product Listing Status -- END
                
                GenericValue productMasterResult = delegator.findOne("ProductMasterResult", UtilMisc.toMap("productId", productId), false);
                if (UtilValidate.isEmpty(productMasterResult)) {
                    productMasterResult = delegator.makeValue("ProductMasterResult", UtilMisc.toMap("productId", productId));
                }
                productMasterResult.set("productId", productId);
                productMasterResult.set("customSku", customSku);
                productMasterResult.set("motherSku", motherSku);
                productMasterResult.set("setSku", setSku);
                productMasterResult.set("chineseName", chineseName);
                productMasterResult.set("englishName", englishName);
                productMasterResult.set("declaredNameCn", declaredNameCn);
                productMasterResult.set("declaredNameEn", declaredNameEn);
                productMasterResult.set("weight", Double.parseDouble(weight));
                if (UtilValidate.isNotEmpty(actualWeight)) {
                    productMasterResult.set("actualWeight", Double.parseDouble(actualWeight));
                }
                productMasterResult.set("productGroup", productGroup);
                productMasterResult.set("statusId", statusId);
                productMasterResult.set("categoryId", categoryId);
                productMasterResult.set("categoryIdParent", categoryIdParent);
                productMasterResult.set("categoryIdChild", categoryIdChild);
                productMasterResult.set("productType", productType);
                productMasterResult.set("ownerGroup", ownerGroup);
                productMasterResult.set("purchaser", purchaser);
                productMasterResult.set("imageLink", imageLink);
                productMasterResult.set("extraStockingDay", Long.parseLong(extraStockingDay));
                productMasterResult.set("sourcingDate", sourcingDate);
                productMasterResult.set("supplier", supplier);
                productMasterResult.set("supplierType", supplierType);
                productMasterResult.set("price", price);
                productMasterResult.set("supplierLink", supplierLink);
                productMasterResult.set("eta", eta);
                productMasterResult.set("minOrderDays", minOrderDays);
                productMasterResult.set("minOrderQuantity", minOrderQuantity);
                productMasterResult.set("priceLastUpdate", priceLastUpdate);Debug.logError("rivalItemId : " + rivalItemId, module);
                productMasterResult.set("rivalItemId", rivalItemId);
                productMasterResult.set("rivalLink", rivalLink);
                productMasterResult.set("soldPerDay", soldPerDay);
                productMasterResult.set("samePriceProfitPercentage", samePriceProfitPercentage);
                productMasterResult.set("crawl", crawl);
                productMasterResult.set("priceUsd", new BigDecimal(usPriceDb));
                productMasterResult.set("priceGbp", new BigDecimal(ukPriceDb));
                productMasterResult.set("priceAud", new BigDecimal(auPriceDb));
                productMasterResult.set("priceEur", new BigDecimal(euPriceDb));
                productMasterResult.set("priceCad", new BigDecimal(caPriceDb));
                productMasterResult.set("cpPercentage", cpPercentageDb);
                productMasterResult.set("gbpPercentage", gbpPercentageDb);
                productMasterResult.set("audPercentage", audPercentageDb);
                productMasterResult.set("eurPercentage", eurPercentageDb);
                productMasterResult.set("cadPercentage", cadPercentageDb);
                productMasterResult.set("smtPrice", new BigDecimal(smtPriceDb));
                productMasterResult.set("smtPercentage", smtPercentageDb);
                productMasterResult.set("smtShippingMethod", smtShippingMethodType);
                productMasterResult.set("wishProductPrice", new BigDecimal(wishFinalProductPrice));
                productMasterResult.set("wishShippingPrice", new BigDecimal(wishFinalShippingPrice));
                productMasterResult.set("wishPercentage", wishProfitPercentage);
                productMasterResult.set("amaPrice", new BigDecimal(amaPriceDb));
                productMasterResult.set("amaPercentage", amaPercentageDb);
                productMasterResult.set("backupSupplier", backupSupplier);
                productMasterResult.set("backupSupplierPrice", backupSupplierPrice);
                productMasterResult.set("backupSupplierLink", backupSupplierLink);
                productMasterResult.set("backupSupplierEta", backupSupplierEta);
                productMasterResult.set("backupMinOrderDays", backupMinOrderDays);
                productMasterResult.set("backupMinOrderQty", backupMinOrderQuantity);
                productMasterResult.set("backupPriceLastUpdate", backupPriceLastUpdate);
                productMasterResult.set("notes", notes);
                productMasterResult.set("listingNotes", listingNotes);
                productMasterResult.set("upc", upc);
                productMasterResult.set("pictureReady", pictureReady);
                productMasterResult.set("createdBy", createdBy);
                productMasterResult.set("ebayUsListing", ebayUSListing);
                productMasterResult.set("ebayUkListing", ebayUKListing);
                productMasterResult.set("ebayAuListing", ebayAUListing);
                productMasterResult.set("ebayCaListing", ebayCAListing);
                productMasterResult.set("ebayDeListing", ebayDEListing);
                productMasterResult.set("ebayFrListing", ebayFRListing);
                productMasterResult.set("ebayEsListing", ebayESListing);
                productMasterResult.set("smtListing", smtListing);
                productMasterResult.set("amaListing", amaListing);
                productMasterResult.set("wishListing", wishListing);
                productMasterResult.set("ebayPicture", ebayPicture);
                productMasterResult.set("amazonPicture", amazonPicture);
                productMasterResult.set("listingCheck", listingCheck);
                productMasterResult.set("finePicture", finePicture);
                delegator.createOrStore(productMasterResult);
            }   //loop productMasterList -- END
            
            Debug.logError("Finished refreshing PM", module);
            if (writeLogFile) {
                f1.write("Finished refreshing PM" + eol);
                f1.close();
            }
        }   //end try block
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return ServiceUtil.returnSuccess();
    }   //gudaoRefreshProductMaster
    
    public static Map<String, Object> gudaoGenerateProductMasterSpreadsheet (DispatchContext dctx, Map context) {   //gudaoGenerateProductMasterSpreadsheet
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        Debug.logError("Start generating PM", module);
        //String text = (String) context.get("Text");
        try {   //start try block
            String fileServerPath = "hot-deploy/gudao/webapp/gudao/bulkModule/export/";
            FileOutputStream fileOut = new FileOutputStream(fileServerPath + "PM-ALL-" + new SimpleDateFormat("MMMdd-HH_mm").format(Calendar.getInstance().getTime()) + ".xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook();
            CreationHelper createHelper = workbook.getCreationHelper();
            DataFormat format = workbook.createDataFormat();
            Sheet sheet = workbook.createSheet("PM");
            
            //CellStyle percentIntFormat = workbook.createCellStyle().setDataFormat(format.getFormat("0%"));
            //CellStyle priceFormat = workbook.createCellStyle().setDataFormat(format.getFormat("#,##0.00"));
            //CellStyle dateFormat = workbook.createCellStyle().setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
            XSSFCellStyle percentIntFormat = workbook.createCellStyle();
            percentIntFormat.setDataFormat((short)BuiltinFormats.getBuiltinFormat("0%"));
            XSSFCellStyle priceFormat = workbook.createCellStyle();
            priceFormat.setDataFormat((short)BuiltinFormats.getBuiltinFormat("#,##0.00"));
            XSSFCellStyle dateFormat = workbook.createCellStyle();
            dateFormat.setDataFormat((short)BuiltinFormats.getBuiltinFormat("d-mmm-yy"));
            
            //Filling column header -- START
            
            String titleProductId = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productId"), true).getString("columnTitle");
            String titleChineseName = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "chineseName"), true).getString("columnTitle");
            String titleEnglishName = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "englishName"), true).getString("columnTitle");
            String titleWeight = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "weight"), true).getString("columnTitle");
            String titleProductGroup = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productGroup"), true).getString("columnTitle");
            String titleStatusId = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "statusId"), true).getString("columnTitle");
            String titleCategoryId = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "categoryId"), true).getString("columnTitle");
            String titleProductType = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productType"), true).getString("columnTitle");
            String titleOwnerGroup = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ownerGroup"), true).getString("columnTitle");
            String titlePurchaser = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "purchaser"), true).getString("columnTitle");
            String titleImageLink = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "imageLink"), true).getString("columnTitle");
            String titleExtraStockingDay = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "extraStockingDay"), true).getString("columnTitle");
            String titleSourcingDate = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "sourcingDate"), true).getString("columnTitle");
            String titleSupplier = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplier"), true).getString("columnTitle");
            String titleSupplierType = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplierType"), true).getString("columnTitle");
            String titlePrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "price"), true).getString("columnTitle");
            String titlePriceLastUpdate = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "priceLastUpdate"), true).getString("columnTitle");
            String titleSupplierLink = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplierLink"), true).getString("columnTitle");
            String titleDiscountRate = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "discountRate"), true).getString("columnTitle");
            String titleEta = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eta"), true).getString("columnTitle");
            String titleMinOrderDays = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "minOrderDays"), true).getString("columnTitle");
            String titleMinOrderQuantity = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "minOrderQuantity"), true).getString("columnTitle");
            String titleRivalItemId = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "rivalItemId"), true).getString("columnTitle");
            String titleRivalLink = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "rivalLink"), true).getString("columnTitle");
            String titleSoldPerDay = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "soldPerDay"), true).getString("columnTitle");
            String titleSamePriceProfitPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "samePriceProfitPercentage"), true).getString("columnTitle");
            String titleUsd = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "usd"), true).getString("columnTitle");
            String titleGbp = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "gbp"), true).getString("columnTitle");
            String titleAud = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "aud"), true).getString("columnTitle");
            String titleEur = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eur"), true).getString("columnTitle");
            String titleCad = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cad"), true).getString("columnTitle");
            String titleCpPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cpPercentage"), true).getString("columnTitle");
            String titleGbpPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "gbpPercentage"), true).getString("columnTitle");
            String titleAudPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "audPercentage"), true).getString("columnTitle");
            String titleEurPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eurPercentage"), true).getString("columnTitle");
            String titleCadPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cadPercentage"), true).getString("columnTitle");
            String titleSmtPrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtPrice"), true).getString("columnTitle");
            String titleSmtPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtPercentage"), true).getString("columnTitle");
            String titleSmtShippingMethod = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtShippingMethod"), true).getString("columnTitle");
            String titleWishProductPrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishProductPrice"), true).getString("columnTitle");
            String titleWishShippingPrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishShippingPrice"), true).getString("columnTitle");
            String titleWishPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishPercentage"), true).getString("columnTitle");
            String titleAmaPrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amaPrice"), true).getString("columnTitle");
            String titleAmaPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amaPercentage"), true).getString("columnTitle");
            String titleProductRivalRank = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productRivalRank"), true).getString("columnTitle");
            String titleBackupSupplier = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplier"), true).getString("columnTitle");
            String titleBackupSupplierPrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierPrice"), true).getString("columnTitle");
            String titleBackupPriceLastUpdate = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupPriceLastUpdate"), true).getString("columnTitle");
            String titleBackupSupplierLink = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierLink"), true).getString("columnTitle");
            String titleBackupSupplierEta = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierEta"), true).getString("columnTitle");
            String titleBackupMinOrderDays = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupMinOrderDays"), true).getString("columnTitle");
            String titleBackupMinOrderQuantity = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupMinOrderQuantity"), true).getString("columnTitle");
            String titleNotes = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "notes"), true).getString("columnTitle");
            String titleUpc = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "upc"), true).getString("columnTitle");
            String titleListingStatus = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "listingStatus"), true).getString("columnTitle");
            String titleEbayPicture = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayPicture"), true).getString("columnTitle");
            String titleAmazonPicture = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amazonPicture"), true).getString("columnTitle");
            
            int colProductId = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productId"), true).getString("sequenceId")) -1;
            int colChineseName = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "chineseName"), true).getString("sequenceId")) -1;
            int colEnglishName = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "englishName"), true).getString("sequenceId")) -1;
            int colWeight = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "weight"), true).getString("sequenceId")) -1;
            int colProductGroup = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productGroup"), true).getString("sequenceId")) -1;
            int colStatusId = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "statusId"), true).getString("sequenceId")) -1;
            int colCategoryId = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "categoryId"), true).getString("sequenceId")) -1;
            int colProductType = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productType"), true).getString("sequenceId")) -1;
            int colOwnerGroup = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ownerGroup"), true).getString("sequenceId")) -1;
            int colPurchaser = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "purchaser"), true).getString("sequenceId")) -1;
            int colImageLink = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "imageLink"), true).getString("sequenceId")) -1;
            int colExtraStockingDay = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "extraStockingDay"), true).getString("sequenceId")) -1;
            int colSourcingDate = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "sourcingDate"), true).getString("sequenceId")) -1;
            int colSupplier = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplier"), true).getString("sequenceId")) -1;
            int colSupplierType = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplierType"), true).getString("sequenceId")) -1;
            int colPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "price"), true).getString("sequenceId")) -1;
            int colPriceLastUpdate = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "priceLastUpdate"), true).getString("sequenceId")) -1;
            int colSupplierLink = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplierLink"), true).getString("sequenceId")) -1;
            int colDiscountRate = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "discountRate"), true).getString("sequenceId")) -1;
            int colEta = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eta"), true).getString("sequenceId")) -1;
            int colMinOrderDays = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "minOrderDays"), true).getString("sequenceId")) -1;
            int colMinOrderQuantity = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "minOrderQuantity"), true).getString("sequenceId")) -1;
            int colRivalItemId = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "rivalItemId"), true).getString("sequenceId")) -1;
            int colRivalLink = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "rivalLink"), true).getString("sequenceId")) -1;
            int colSoldPerDay = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "soldPerDay"), true).getString("sequenceId")) -1;
            int colSamePriceProfitPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "samePriceProfitPercentage"), true).getString("sequenceId")) -1;
            int colUsd = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "usd"), true).getString("sequenceId")) -1;
            int colGbp = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "gbp"), true).getString("sequenceId")) -1;
            int colAud = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "aud"), true).getString("sequenceId")) -1;
            int colEur = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eur"), true).getString("sequenceId")) -1;
            int colCad = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cad"), true).getString("sequenceId")) -1;
            int colCpPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cpPercentage"), true).getString("sequenceId")) -1;
            int colGbpPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "gbpPercentage"), true).getString("sequenceId")) -1;
            int colAudPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "audPercentage"), true).getString("sequenceId")) -1;
            int colEurPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eurPercentage"), true).getString("sequenceId")) -1;
            int colCadPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cadPercentage"), true).getString("sequenceId")) -1;
            int colSmtPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtPrice"), true).getString("sequenceId")) -1;
            int colSmtPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtPercentage"), true).getString("sequenceId")) -1;
            int colSmtShippingMethod = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtShippingMethod"), true).getString("sequenceId")) -1;
            int colWishProductPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishProductPrice"), true).getString("sequenceId")) -1;
            int colWishShippingPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishShippingPrice"), true).getString("sequenceId")) -1;
            int colWishPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishPercentage"), true).getString("sequenceId")) -1;
            int colAmaPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amaPrice"), true).getString("sequenceId")) -1;
            int colAmaPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amaPercentage"), true).getString("sequenceId")) -1;
            int colProductRivalRank = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productRivalRank"), true).getString("sequenceId")) -1;
            int colBackupSupplier = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplier"), true).getString("sequenceId")) -1;
            int colBackupSupplierPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierPrice"), true).getString("sequenceId")) -1;
            int colBackupSupplierLink = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierLink"), true).getString("sequenceId")) -1;
            int colBackupSupplierEta = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierEta"), true).getString("sequenceId")) -1;
            int colBackupMinOrderDays = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupMinOrderDays"), true).getString("sequenceId")) -1;
            int colBackupMinOrderQuantity = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupMinOrderQuantity"), true).getString("sequenceId")) -1;
            int colBackupPriceLastUpdate = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupPriceLastUpdate"), true).getString("sequenceId")) -1;
            int colNotes = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "notes"), true).getString("sequenceId")) -1;
            int colUpc = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "upc"), true).getString("sequenceId")) -1;
            int colListingStatus = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "listingStatus"), true).getString("sequenceId")) -1;
            int colEbayPicture = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayPicture"), true).getString("sequenceId")) -1;
            int colAmazonPicture = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amazonPicture"), true).getString("sequenceId")) -1;
                                                                                                                    
            Row rowHeader = sheet.createRow((short) 0);
            Cell labelProductId = rowHeader.createCell(colProductId);
            Cell labelChineseName = rowHeader.createCell(colChineseName);
            Cell labelEnglishName = rowHeader.createCell(colEnglishName);
            Cell labelWeight = rowHeader.createCell(colWeight);
            Cell labelProductGroup = rowHeader.createCell(colProductGroup);
            Cell labelStatusId = rowHeader.createCell(colStatusId);
            Cell labelCategoryId = rowHeader.createCell(colCategoryId);
            Cell labelProductType = rowHeader.createCell(colProductType);
            Cell labelOwnerGroup = rowHeader.createCell(colOwnerGroup);
            Cell labelPurchaser = rowHeader.createCell(colPurchaser);
            Cell labelImageLink = rowHeader.createCell(colImageLink);
            Cell labelExtraStockingDay = rowHeader.createCell(colExtraStockingDay);
            Cell labelSourcingDate = rowHeader.createCell(colSourcingDate);
            Cell labelSupplier = rowHeader.createCell(colSupplier);
            Cell labelSupplierType = rowHeader.createCell(colSupplierType);
            Cell labelPrice = rowHeader.createCell(colPrice);
            Cell labelPriceLastUpdate = rowHeader.createCell(colPriceLastUpdate);
            Cell labelSupplierLink = rowHeader.createCell(colSupplierLink);
            Cell labelDiscountRate = rowHeader.createCell(colDiscountRate);
            Cell labelEta = rowHeader.createCell(colEta);
            Cell labelMinOrderDays = rowHeader.createCell(colMinOrderDays);
            Cell labelMinOrderQuantity = rowHeader.createCell(colMinOrderQuantity);
            Cell labelRivalItemId = rowHeader.createCell(colRivalItemId);
            Cell labelRivalLink = rowHeader.createCell(colRivalLink);
            Cell labelSoldPerDay = rowHeader.createCell(colSoldPerDay);
            Cell labelSamePriceProfitPercentage = rowHeader.createCell(colSamePriceProfitPercentage);
            Cell labelUsd = rowHeader.createCell(colUsd);
            Cell labelGbp = rowHeader.createCell(colGbp);
            Cell labelAud = rowHeader.createCell(colAud);
            Cell labelEur = rowHeader.createCell(colEur);
            Cell labelCad = rowHeader.createCell(colCad);
            Cell labelCpPercentage = rowHeader.createCell(colCpPercentage);
            Cell labelGbpPercentage = rowHeader.createCell(colGbpPercentage);
            Cell labelAudPercentage = rowHeader.createCell(colAudPercentage);
            Cell labelEurPercentage = rowHeader.createCell(colEurPercentage);
            Cell labelCadPercentage = rowHeader.createCell(colCadPercentage);
            Cell labelSmtPrice = rowHeader.createCell(colSmtPrice);
            Cell labelSmtPercentage = rowHeader.createCell(colSmtPercentage);
            Cell labelSmtShippingMethod = rowHeader.createCell(colSmtShippingMethod);
            Cell labelWishProductPrice = rowHeader.createCell(colWishProductPrice);
            Cell labelWishShippingPrice = rowHeader.createCell(colWishShippingPrice);
            Cell labelWishPercentage = rowHeader.createCell(colWishPercentage);
            Cell labelAmaPrice = rowHeader.createCell(colAmaPrice);
            Cell labelAmaPercentage = rowHeader.createCell(colAmaPercentage);
            Cell labelProductRivalRank = rowHeader.createCell(colProductRivalRank);
            Cell labelBackupSupplier = rowHeader.createCell(colBackupSupplier);
            Cell labelBackupSupplierPrice = rowHeader.createCell(colBackupSupplierPrice);
            Cell labelBackupSupplierLink = rowHeader.createCell(colBackupSupplierLink);
            Cell labelBackupSupplierEta = rowHeader.createCell(colBackupSupplierEta);
            Cell labelBackupMinOrderDays = rowHeader.createCell(colBackupMinOrderDays);
            Cell labelBackupMinOrderQuantity = rowHeader.createCell(colBackupMinOrderQuantity);
            Cell labelBackupPriceLastUpdate = rowHeader.createCell(colBackupPriceLastUpdate);
            Cell labelNotes = rowHeader.createCell(colNotes);
            Cell labelUpc = rowHeader.createCell(colUpc);
            Cell labelListingStatus = rowHeader.createCell(colListingStatus);
            Cell labelEbayPicture = rowHeader.createCell(colEbayPicture);
            Cell labelAmazonPicture = rowHeader.createCell(colAmazonPicture);
            
            labelProductId.setCellValue(createHelper.createRichTextString(titleProductId));
            labelChineseName.setCellValue(createHelper.createRichTextString(titleChineseName));
            labelEnglishName.setCellValue(createHelper.createRichTextString(titleEnglishName));
            labelWeight.setCellValue(createHelper.createRichTextString(titleWeight));
            labelProductGroup.setCellValue(createHelper.createRichTextString(titleProductGroup));
            labelStatusId.setCellValue(createHelper.createRichTextString(titleStatusId));
            labelCategoryId.setCellValue(createHelper.createRichTextString(titleCategoryId));
            labelProductType.setCellValue(createHelper.createRichTextString(titleProductType));
            labelOwnerGroup.setCellValue(createHelper.createRichTextString(titleOwnerGroup));
            labelPurchaser.setCellValue(createHelper.createRichTextString(titlePurchaser));
            labelImageLink.setCellValue(createHelper.createRichTextString(titleImageLink));
            labelExtraStockingDay.setCellValue(createHelper.createRichTextString(titleExtraStockingDay));
            labelSourcingDate.setCellValue(createHelper.createRichTextString(titleSourcingDate));
            labelSupplier.setCellValue(createHelper.createRichTextString(titleSupplier));
            labelSupplierType.setCellValue(createHelper.createRichTextString(titleSupplierType));
            labelPrice.setCellValue(createHelper.createRichTextString(titlePrice));
            labelPriceLastUpdate.setCellValue(createHelper.createRichTextString(titlePriceLastUpdate));
            labelSupplierLink.setCellValue(createHelper.createRichTextString(titleSupplierLink));
            labelDiscountRate.setCellValue(createHelper.createRichTextString(titleDiscountRate));
            labelEta.setCellValue(createHelper.createRichTextString(titleEta));
            labelMinOrderDays.setCellValue(createHelper.createRichTextString(titleMinOrderDays));
            labelMinOrderQuantity.setCellValue(createHelper.createRichTextString(titleMinOrderQuantity));
            labelRivalItemId.setCellValue(createHelper.createRichTextString(titleRivalItemId));
            labelRivalLink.setCellValue(createHelper.createRichTextString(titleRivalLink));
            labelSoldPerDay.setCellValue(createHelper.createRichTextString(titleSoldPerDay));
            labelSamePriceProfitPercentage.setCellValue(createHelper.createRichTextString(titleSamePriceProfitPercentage));
            labelUsd.setCellValue(createHelper.createRichTextString(titleUsd));
            labelGbp.setCellValue(createHelper.createRichTextString(titleGbp));
            labelAud.setCellValue(createHelper.createRichTextString(titleAud));
            labelEur.setCellValue(createHelper.createRichTextString(titleEur));
            labelCad.setCellValue(createHelper.createRichTextString(titleCad));
            labelCpPercentage.setCellValue(createHelper.createRichTextString(titleCpPercentage));
            labelGbpPercentage.setCellValue(createHelper.createRichTextString(titleGbpPercentage));
            labelAudPercentage.setCellValue(createHelper.createRichTextString(titleAudPercentage));
            labelEurPercentage.setCellValue(createHelper.createRichTextString(titleEurPercentage));
            labelCadPercentage.setCellValue(createHelper.createRichTextString(titleCadPercentage));
            labelSmtPrice.setCellValue(createHelper.createRichTextString(titleSmtPrice));
            labelSmtPercentage.setCellValue(createHelper.createRichTextString(titleSmtPercentage));
            labelSmtShippingMethod.setCellValue(createHelper.createRichTextString(titleSmtShippingMethod));
            labelWishProductPrice.setCellValue(createHelper.createRichTextString(titleWishProductPrice));
            labelWishShippingPrice.setCellValue(createHelper.createRichTextString(titleWishShippingPrice));
            labelWishPercentage.setCellValue(createHelper.createRichTextString(titleWishPercentage));
            labelAmaPrice.setCellValue(createHelper.createRichTextString(titleAmaPrice));
            labelAmaPercentage.setCellValue(createHelper.createRichTextString(titleAmaPercentage));
            labelProductRivalRank.setCellValue(createHelper.createRichTextString(titleProductRivalRank));
            labelBackupSupplier.setCellValue(createHelper.createRichTextString(titleBackupSupplier));
            labelBackupSupplierPrice.setCellValue(createHelper.createRichTextString(titleBackupSupplierPrice));
            labelBackupSupplierLink.setCellValue(createHelper.createRichTextString(titleBackupSupplierLink));
            labelBackupSupplierEta.setCellValue(createHelper.createRichTextString(titleBackupSupplierEta));
            labelBackupMinOrderDays.setCellValue(createHelper.createRichTextString(titleBackupMinOrderDays));
            labelBackupMinOrderQuantity.setCellValue(createHelper.createRichTextString(titleBackupMinOrderQuantity));
            labelBackupPriceLastUpdate.setCellValue(createHelper.createRichTextString(titleBackupPriceLastUpdate));
            labelNotes.setCellValue(createHelper.createRichTextString(titleNotes));
            labelUpc.setCellValue(createHelper.createRichTextString(titleUpc));
            labelListingStatus.setCellValue(createHelper.createRichTextString(titleListingStatus));
            labelEbayPicture.setCellValue(createHelper.createRichTextString(titleEbayPicture));
            labelAmazonPicture.setCellValue(createHelper.createRichTextString(titleAmazonPicture));
            
            //Filling column header -- END
            int maxCol = 0;
            List<GenericValue> pmColTitles = delegator.findList("ProductMasterColumnTitle", null, null, null, null, false);
            for (GenericValue pmColTitle : pmColTitles) {   //loop pmColTitles -- START
                if (Integer.parseInt(pmColTitle.getString("sequenceId")) >= maxCol) {
                    maxCol = Integer.parseInt(pmColTitle.getString("sequenceId")) - 1;
                }
            }   //loop pmColTitles -- END
            
            
            
            /*List<GenericValue> productMasterList = delegator.findList("ProductMaster", EntityCondition.makeCondition(UtilMisc.toList(
             EntityCondition.makeCondition("productGroup",EntityOperator.NOT_LIKE ,"G5%")
             ))
             , null, UtilMisc.toList("productId"), null, false);*/
            
            /*List<GenericValue> productMasterList = delegator.findList("ProductMaster", EntityCondition.makeCondition(UtilMisc.toList(
             EntityCondition.makeCondition("productId",EntityOperator.EQUALS ,"05-0005")
             ))
             , null, UtilMisc.toList("productId"), null, false);*/
            List<GenericValue> productMasterList = delegator.findList("ProductMasterResult", null, null, UtilMisc.toList("productId"), null, false);
            //GenericValue productMaster = EntityUtil.getFirst(productMasterList);
            int row = 1;
            for (GenericValue productMaster : productMasterList) {  //loop productMasterList -- START
                Row rowValue = sheet.createRow(row);
                
                Cell valueProductId = rowValue.createCell(colProductId);
                Cell valueChineseName = rowValue.createCell(colChineseName);
                Cell valueEnglishName = rowValue.createCell(colEnglishName);
                Cell valueWeight = rowValue.createCell(colWeight);
                Cell valueProductGroup = rowValue.createCell(colProductGroup);
                Cell valueStatusId = rowValue.createCell(colStatusId);
                Cell valueCategoryId = rowValue.createCell(colCategoryId);
                Cell valueProductType = rowValue.createCell(colProductType);
                Cell valueOwnerGroup = rowValue.createCell(colOwnerGroup);
                Cell valuePurchaser = rowValue.createCell(colPurchaser);
                Cell valueImageLink = rowValue.createCell(colImageLink);
                Cell valueExtraStockingDay = rowValue.createCell(colExtraStockingDay);
                Cell valueSourcingDate = rowValue.createCell(colSourcingDate);
                Cell valueSupplier = rowValue.createCell(colSupplier);
                Cell valueSupplierType = rowValue.createCell(colSupplierType);
                Cell valuePrice = rowValue.createCell(colPrice);
                Cell valuePriceLastUpdate = rowValue.createCell(colPriceLastUpdate);
                Cell valueSupplierLink = rowValue.createCell(colSupplierLink);
                Cell valueDiscountRate = rowValue.createCell(colDiscountRate);
                Cell valueEta = rowValue.createCell(colEta);
                Cell valueMinOrderDays = rowValue.createCell(colMinOrderDays);
                Cell valueMinOrderQuantity = rowValue.createCell(colMinOrderQuantity);
                Cell valueRivalItemId = rowValue.createCell(colRivalItemId);
                Cell valueRivalLink = rowValue.createCell(colRivalLink);
                Cell valueSoldPerDay = rowValue.createCell(colSoldPerDay);
                Cell valueSamePriceProfitPercentage = rowValue.createCell(colSamePriceProfitPercentage);
                Cell valuePriceUsd = rowValue.createCell(colUsd);
                Cell valuePriceGbp = rowValue.createCell(colGbp);
                Cell valuePriceAud = rowValue.createCell(colAud);
                Cell valuePriceEur = rowValue.createCell(colEur);
                Cell valuePriceCad = rowValue.createCell(colCad);
                Cell valueCpPercentage = rowValue.createCell(colCpPercentage);
                Cell valueGbpPercentage = rowValue.createCell(colGbpPercentage);
                Cell valueAudPercentage = rowValue.createCell(colAudPercentage);
                Cell valueEurPercentage = rowValue.createCell(colEurPercentage);
                Cell valueCadPercentage = rowValue.createCell(colCadPercentage);
                Cell valueSmtPrice = rowValue.createCell(colSmtPrice);
                Cell valueSmtPercentage = rowValue.createCell(colSmtPercentage);
                Cell valueSmtShippingMethod = rowValue.createCell(colSmtShippingMethod);
                Cell valueWishProductPrice = rowValue.createCell(colWishProductPrice);
                Cell valueWishShippingPrice = rowValue.createCell(colWishShippingPrice);
                Cell valueWishPercentage = rowValue.createCell(colWishPercentage);
                Cell valueAmaPrice = rowValue.createCell(colAmaPrice);
                Cell valueAmaPercentage = rowValue.createCell(colAmaPercentage);
                Cell valueProductRivalRank = rowValue.createCell(colProductRivalRank);
                Cell valueBackupSupplier = rowValue.createCell(colBackupSupplier);
                Cell valueBackupSupplierLink = rowValue.createCell(colBackupSupplierLink);
                Cell valueBackupSupplierPrice = rowValue.createCell(colBackupSupplierPrice);
                Cell valueBackupSupplierEta = rowValue.createCell(colBackupSupplierEta);
                Cell valueBackupMinOrderDays = rowValue.createCell(colBackupMinOrderDays);
                Cell valueBackupMinOrderQuantity = rowValue.createCell(colBackupMinOrderQuantity);
                Cell valueBackupPriceLastUpdate = rowValue.createCell(colBackupPriceLastUpdate);
                Cell valueNotes = rowValue.createCell(colNotes);
                Cell valueUpc = rowValue.createCell(colUpc);
                Cell valueListingStatus = rowValue.createCell(colListingStatus);
                Cell valueEbayPicture = rowValue.createCell(colEbayPicture);
                Cell valueAmazonPicture = rowValue.createCell(colAmazonPicture);
                
                String productId = productMaster.getString("productId");
                String chineseName = productMaster.getString("chineseName");
                String englishName = productMaster.getString("englishName");
                double weight = productMaster.getDouble("weight");
                String productGroup = productMaster.getString("productGroup");
                String statusId = productMaster.getString("statusId");
                String categoryId = productMaster.getString("categoryId");
                String productType = productMaster.getString("productType");
                String ownerGroup = productMaster.getString("ownerGroup");
                String purchaser = productMaster.getString("purchaser");
                String imageLink = productMaster.getString("imageLink");
                long extraStockingDay = productMaster.getLong("extraStockingDay");
                String sourcingDate = productMaster.getString("sourcingDate");
                BigDecimal priceUsd = productMaster.getBigDecimal("priceUsd");
                BigDecimal priceAud = productMaster.getBigDecimal("priceAud");
                BigDecimal priceCad = productMaster.getBigDecimal("priceCad");
                BigDecimal priceGbp = productMaster.getBigDecimal("priceGbp");
                BigDecimal priceEur = productMaster.getBigDecimal("priceEur");
                double cpPercentage = productMaster.getDouble("cpPercentage");
                double gbpPercentage = productMaster.getDouble("gbpPercentage");
                double audPercentage = productMaster.getDouble("audPercentage");
                double eurPercentage = productMaster.getDouble("eurPercentage");
                double cadPercentage = productMaster.getDouble("cadPercentage");
                BigDecimal smtPrice = productMaster.getBigDecimal("smtPrice");
                double smtPercentage = productMaster.getDouble("smtPercentage");
                String smtShippingMethod = productMaster.getString("smtShippingMethod");
                BigDecimal wishProductPrice = productMaster.getBigDecimal("wishProductPrice");
                BigDecimal wishShippingPrice = productMaster.getBigDecimal("wishShippingPrice");
                double wishPercentage = productMaster.getDouble("wishPercentage");
                BigDecimal amaPrice = productMaster.getBigDecimal("amaPrice");
                double amaPercentage = productMaster.getDouble("amaPercentage");
                String supplier = productMaster.getString("supplier");
                String supplierType = productMaster.getString("supplierType");
                BigDecimal price = productMaster.getBigDecimal("price");
                String priceLastUpdate = productMaster.getString("priceLastUpdate");
                String supplierLink = productMaster.getString("supplierLink");
                long discountRate = productMaster.getLong("discountRate");
                long eta = productMaster.getLong("eta");
                long minOrderDays = productMaster.getLong("minOrderDays");
                long minOrderQuantity = productMaster.getLong("minOrderQuantity");
                String backupSupplier = productMaster.getString("backupSupplier");
                String backupSupplierLink = productMaster.getString("backupSupplierLink");
                BigDecimal backupSupplierPrice = productMaster.getBigDecimal("backupSupplierPrice");
                long backupSupplierEta = productMaster.getLong("backupSupplierEta");
                long backupMinOrderDays = productMaster.getLong("backupMinOrderDays");
                long backupMinOrderQuantity = productMaster.getLong("backupMinOrderQty");
                String backupPriceLastUpdate = productMaster.getString("backupPriceLastUpdate");
                String rivalItemId = productMaster.getString("rivalItemId");
                String rivalLink = productMaster.getString("rivalLink");
                double soldPerDay = productMaster.getDouble("soldPerDay");
                double samePriceProfitPercentage = productMaster.getDouble("samePriceProfitPercentage");
                String notes = productMaster.getString("notes");
                String upc = productMaster.getString("upc");
                String listingStatus = productMaster.getString("listingStatus");
                String ebayPicture = productMaster.getString("ebayPicture");
                String amazonPicture = productMaster.getString("amazonPicture");
                
                valueProductId.setCellValue(createHelper.createRichTextString(productId));
                valueChineseName.setCellValue(createHelper.createRichTextString(chineseName));
                valueEnglishName.setCellValue(createHelper.createRichTextString(englishName));
                valueWeight.setCellValue((weight));
                //valueWeight.setCellStyle(priceFormat);
                valueProductGroup.setCellValue(createHelper.createRichTextString(productGroup));
                valueStatusId.setCellValue(createHelper.createRichTextString(statusId));
                valueCategoryId.setCellValue(createHelper.createRichTextString(categoryId));
                valueProductType.setCellValue(createHelper.createRichTextString(productType));
                valueOwnerGroup.setCellValue(createHelper.createRichTextString(ownerGroup));
                valuePurchaser.setCellValue(createHelper.createRichTextString(purchaser));
                valueImageLink.setCellValue(createHelper.createRichTextString(imageLink));
                valueExtraStockingDay.setCellValue(extraStockingDay);
                valueSourcingDate.setCellValue(createHelper.createRichTextString(sourcingDate));
                valuePriceUsd.setCellValue(priceUsd.doubleValue());
                valuePriceUsd.setCellStyle(priceFormat);
                valuePriceAud.setCellValue(priceAud.doubleValue());
                valuePriceAud.setCellStyle(priceFormat);
                valuePriceCad.setCellValue(priceCad.doubleValue());
                valuePriceCad.setCellStyle(priceFormat);
                valuePriceGbp.setCellValue(priceGbp.doubleValue());
                valuePriceGbp.setCellStyle(priceFormat);
                valuePriceEur.setCellValue(priceEur.doubleValue());
                valuePriceEur.setCellStyle(priceFormat);
                valueCpPercentage.setCellValue(cpPercentage);
                valueCpPercentage.setCellStyle(percentIntFormat);
                valueGbpPercentage.setCellValue(gbpPercentage);
                valueGbpPercentage.setCellStyle(percentIntFormat);
                valueAudPercentage.setCellValue(audPercentage);
                valueAudPercentage.setCellStyle(percentIntFormat);
                valueEurPercentage.setCellValue(eurPercentage);
                valueEurPercentage.setCellStyle(percentIntFormat);
                valueCadPercentage.setCellValue(cadPercentage);
                valueCadPercentage.setCellStyle(percentIntFormat);
                valueSmtPrice.setCellValue(smtPrice.doubleValue());
                valueSmtPrice.setCellStyle(priceFormat);
                valueSmtPercentage.setCellValue(smtPercentage);
                valueSmtPercentage.setCellStyle(percentIntFormat);
                valueSmtShippingMethod.setCellValue(createHelper.createRichTextString(smtShippingMethod));
                valueWishProductPrice.setCellValue(wishProductPrice.doubleValue());
                valueWishProductPrice.setCellStyle(priceFormat);
                valueWishShippingPrice.setCellValue(wishShippingPrice.doubleValue());
                valueWishShippingPrice.setCellStyle(priceFormat);
                valueWishPercentage.setCellValue(wishPercentage);
                valueWishPercentage.setCellStyle(percentIntFormat);
                valueAmaPrice.setCellValue(amaPrice.doubleValue());
                valueAmaPrice.setCellStyle(priceFormat);
                valueAmaPercentage.setCellValue(amaPercentage);
                valueAmaPercentage.setCellStyle(percentIntFormat);
                valueSupplier.setCellValue(createHelper.createRichTextString(supplier));
                valueSupplierType.setCellValue(createHelper.createRichTextString(supplierType));
                valuePrice.setCellValue(price.doubleValue());
                valuePrice.setCellStyle(priceFormat);
                valuePriceLastUpdate.setCellValue(createHelper.createRichTextString(priceLastUpdate));
                valueSupplierLink.setCellValue(createHelper.createRichTextString(supplierLink));
                valueDiscountRate.setCellValue(discountRate);
                valueDiscountRate.setCellStyle(percentIntFormat);
                valueEta.setCellValue(eta);
                valueMinOrderDays.setCellValue(minOrderDays);
                valueMinOrderQuantity.setCellValue(minOrderQuantity);
                valueBackupSupplier.setCellValue(createHelper.createRichTextString(backupSupplier));
                valueBackupSupplierLink.setCellValue(createHelper.createRichTextString(backupSupplierLink));
                valueBackupSupplierPrice.setCellValue(backupSupplierPrice.doubleValue());
                valueBackupSupplierPrice.setCellStyle(priceFormat);
                valueBackupSupplierEta.setCellValue(backupSupplierEta);
                valueBackupMinOrderDays.setCellValue(backupMinOrderDays);
                valueBackupMinOrderQuantity.setCellValue(backupMinOrderQuantity);
                valueBackupPriceLastUpdate.setCellValue(createHelper.createRichTextString(backupPriceLastUpdate));
                valueRivalItemId.setCellValue(createHelper.createRichTextString(rivalItemId));
                valueRivalLink.setCellValue(createHelper.createRichTextString(rivalLink));
                valueSoldPerDay.setCellValue(soldPerDay);
                valueSamePriceProfitPercentage.setCellValue(samePriceProfitPercentage);
                valueNotes.setCellValue(createHelper.createRichTextString(notes));
                valueUpc.setCellValue(createHelper.createRichTextString(upc));
                valueListingStatus.setCellValue(createHelper.createRichTextString(listingStatus));
                valueEbayPicture.setCellValue(createHelper.createRichTextString(ebayPicture));
                valueAmazonPicture.setCellValue(createHelper.createRichTextString(amazonPicture));
                
                row++;
            }   //loop productMasterList -- END
            
            workbook.write(fileOut);
            fileOut.close();
            Debug.logError("Finished generating PM", module);
        }   //end try block
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return ServiceUtil.returnSuccess();
    }   //gudaoGenerateProductMasterSpreadsheet
    
    public static Map<String, Object> gudaoGenerateProductMaster (DispatchContext dctx, Map context) {   //gudaoGenerateProductMaster
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();
        Debug.logError("Start generating PM", module);
        //String text = (String) context.get("Text");
        try {   //start try block
            String fileServerPath = "hot-deploy/gudao/webapp/gudao/bulkModule/export/";
            
            WritableWorkbook workbook = jxl.Workbook.createWorkbook(new File(fileServerPath + "PM-ALL-" + new SimpleDateFormat("MMMdd-HH_mm").format(Calendar.getInstance().getTime()) + ".xls"));
            WritableSheet sheet = workbook.createSheet("PM", 0);
            
            WritableCellFormat integerFormat = new WritableCellFormat (NumberFormats.INTEGER);
            WritableCellFormat percentIntFormat = new WritableCellFormat (NumberFormats.PERCENT_INTEGER);
            WritableCellFormat priceFormat = new WritableCellFormat (NumberFormats.FLOAT);
            //Filling column header -- START
            
            String titleProductId = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productId"), true).getString("columnTitle");
            String titleChineseName = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "chineseName"), true).getString("columnTitle");
            String titleEnglishName = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "englishName"), true).getString("columnTitle");
            String titleWeight = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "weight"), true).getString("columnTitle");
            String titleProductGroup = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productGroup"), true).getString("columnTitle");
            String titleStatusId = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "statusId"), true).getString("columnTitle");
            String titleCategoryId = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "categoryId"), true).getString("columnTitle");
            String titleProductType = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productType"), true).getString("columnTitle");
            String titleOwnerGroup = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ownerGroup"), true).getString("columnTitle");
            String titlePurchaser = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "purchaser"), true).getString("columnTitle");
            String titleImageLink = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "imageLink"), true).getString("columnTitle");
            String titleExtraStockingDay = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "extraStockingDay"), true).getString("columnTitle");
            String titleSourcingDate = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "sourcingDate"), true).getString("columnTitle");
            String titleSupplier = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplier"), true).getString("columnTitle");
            String titleSupplierType = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplierType"), true).getString("columnTitle");
            String titlePrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "price"), true).getString("columnTitle");
            String titlePriceLastUpdate = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "priceLastUpdate"), true).getString("columnTitle");
            String titleSupplierLink = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplierLink"), true).getString("columnTitle");
            String titleDiscountRate = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "discountRate"), true).getString("columnTitle");
            String titleEta = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eta"), true).getString("columnTitle");
            String titleMinOrderDays = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "minOrderDays"), true).getString("columnTitle");
            String titleMinOrderQuantity = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "minOrderQuantity"), true).getString("columnTitle");
            String titleRivalItemId = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "rivalItemId"), true).getString("columnTitle");
            String titleRivalLink = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "rivalLink"), true).getString("columnTitle");
            String titleSoldPerDay = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "soldPerDay"), true).getString("columnTitle");
            String titleSamePriceProfitPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "samePriceProfitPercentage"), true).getString("columnTitle");
            String titleUsd = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "usd"), true).getString("columnTitle");
            String titleGbp = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "gbp"), true).getString("columnTitle");
            String titleAud = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "aud"), true).getString("columnTitle");
            String titleEur = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eur"), true).getString("columnTitle");
            String titleCad = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cad"), true).getString("columnTitle");
            String titleCpPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cpPercentage"), true).getString("columnTitle");
            String titleGbpPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "gbpPercentage"), true).getString("columnTitle");
            String titleAudPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "audPercentage"), true).getString("columnTitle");
            String titleEurPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eurPercentage"), true).getString("columnTitle");
            String titleCadPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cadPercentage"), true).getString("columnTitle");
            String titleSmtPrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtPrice"), true).getString("columnTitle");
            String titleSmtPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtPercentage"), true).getString("columnTitle");
            String titleSmtShippingMethod = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtShippingMethod"), true).getString("columnTitle");
            String titleWishProductPrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishProductPrice"), true).getString("columnTitle");
            String titleWishShippingPrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishShippingPrice"), true).getString("columnTitle");
            String titleWishPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishPercentage"), true).getString("columnTitle");
            String titleAmaPrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amaPrice"), true).getString("columnTitle");
            String titleAmaPercentage = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amaPercentage"), true).getString("columnTitle");
            String titleProductRivalRank = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productRivalRank"), true).getString("columnTitle");
            String titleBackupSupplier = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplier"), true).getString("columnTitle");
            String titleBackupSupplierPrice = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierPrice"), true).getString("columnTitle");
            String titleBackupSupplierLink = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierLink"), true).getString("columnTitle");
            String titleBackupSupplierEta = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierEta"), true).getString("columnTitle");
            String titleBackupMinOrderDays = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupMinOrderDays"), true).getString("columnTitle");
            String titleBackupMinOrderQuantity = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupMinOrderQuantity"), true).getString("columnTitle");
            String titleBackupPriceLastUpdate = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupPriceLastUpdate"), true).getString("columnTitle");
            String titleNotes = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "notes"), true).getString("columnTitle");
            String titleUpc = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "upc"), true).getString("columnTitle");
            String titleEbayUSListing = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayUsListing"), true).getString("columnTitle");
            String titleEbayUKListing = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayUkListing"), true).getString("columnTitle");
            String titleEbayAUListing = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayAuListing"), true).getString("columnTitle");
            String titleEbayCAListing = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayCaListing"), true).getString("columnTitle");
            String titleEbayDEListing = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayDeListing"), true).getString("columnTitle");
            String titleEbayFRListing = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayFrListing"), true).getString("columnTitle");
            String titleEbayESListing = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayEsListing"), true).getString("columnTitle");
            String titleSmtListing = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtListing"), true).getString("columnTitle");
            String titleAmaListing = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amaListing"), true).getString("columnTitle");
            String titleWishListing = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishListing"), true).getString("columnTitle");
            String titleEbayPicture = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayPicture"), true).getString("columnTitle");
            String titleAmazonPicture = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amazonPicture"), true).getString("columnTitle");
            String titlePlatform = delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "platform"), true).getString("columnTitle");
            
            int colProductId = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productId"), true).getString("sequenceId")) -1;
            int colChineseName = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "chineseName"), true).getString("sequenceId")) -1;
            int colEnglishName = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "englishName"), true).getString("sequenceId")) -1;
            int colWeight = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "weight"), true).getString("sequenceId")) -1;
            int colProductGroup = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productGroup"), true).getString("sequenceId")) -1;
            int colStatusId = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "statusId"), true).getString("sequenceId")) -1;
            int colCategoryId = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "categoryId"), true).getString("sequenceId")) -1;
            int colProductType = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productType"), true).getString("sequenceId")) -1;
            int colOwnerGroup = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ownerGroup"), true).getString("sequenceId")) -1;
            int colPurchaser = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "purchaser"), true).getString("sequenceId")) -1;
            int colImageLink = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "imageLink"), true).getString("sequenceId")) -1;
            int colExtraStockingDay = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "extraStockingDay"), true).getString("sequenceId")) -1;
            int colSourcingDate = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "sourcingDate"), true).getString("sequenceId")) -1;
            int colSupplier = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplier"), true).getString("sequenceId")) -1;
            int colSupplierType = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplierType"), true).getString("sequenceId")) -1;
            int colPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "price"), true).getString("sequenceId")) -1;
            int colPriceLastUpdate = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "priceLastUpdate"), true).getString("sequenceId")) -1;
            int colSupplierLink = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "supplierLink"), true).getString("sequenceId")) -1;
            int colDiscountRate = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "discountRate"), true).getString("sequenceId")) -1;
            int colEta = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eta"), true).getString("sequenceId")) -1;
            int colMinOrderDays = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "minOrderDays"), true).getString("sequenceId")) -1;
            int colMinOrderQuantity = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "minOrderQuantity"), true).getString("sequenceId")) -1;
            int colRivalItemId = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "rivalItemId"), true).getString("sequenceId")) -1;
            int colRivalLink = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "rivalLink"), true).getString("sequenceId")) -1;
            int colSoldPerDay = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "soldPerDay"), true).getString("sequenceId")) -1;
            int colSamePriceProfitPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "samePriceProfitPercentage"), true).getString("sequenceId")) -1;
            int colUsd = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "usd"), true).getString("sequenceId")) -1;
            int colGbp = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "gbp"), true).getString("sequenceId")) -1;
            int colAud = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "aud"), true).getString("sequenceId")) -1;
            int colEur = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eur"), true).getString("sequenceId")) -1;
            int colCad = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cad"), true).getString("sequenceId")) -1;
            int colCpPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cpPercentage"), true).getString("sequenceId")) -1;
            int colGbpPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "gbpPercentage"), true).getString("sequenceId")) -1;
            int colAudPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "audPercentage"), true).getString("sequenceId")) -1;
            int colEurPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "eurPercentage"), true).getString("sequenceId")) -1;
            int colCadPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "cadPercentage"), true).getString("sequenceId")) -1;
            int colSmtPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtPrice"), true).getString("sequenceId")) -1;
            int colSmtPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtPercentage"), true).getString("sequenceId")) -1;
            int colSmtShippingMethod = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtShippingMethod"), true).getString("sequenceId")) -1;
            int colWishProductPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishProductPrice"), true).getString("sequenceId")) -1;
            int colWishShippingPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishShippingPrice"), true).getString("sequenceId")) -1;
            int colWishPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishPercentage"), true).getString("sequenceId")) -1;
            int colAmaPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amaPrice"), true).getString("sequenceId")) -1;
            int colAmaPercentage = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amaPercentage"), true).getString("sequenceId")) -1;
            int colProductRivalRank = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "productRivalRank"), true).getString("sequenceId")) -1;
            int colBackupSupplier = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplier"), true).getString("sequenceId")) -1;
            int colBackupSupplierPrice = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierPrice"), true).getString("sequenceId")) -1;
            int colBackupSupplierLink = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierLink"), true).getString("sequenceId")) -1;
            int colBackupSupplierEta = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupSupplierEta"), true).getString("sequenceId")) -1;
            int colBackupMinOrderDays = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupMinOrderDays"), true).getString("sequenceId")) -1;
            int colBackupMinOrderQuantity = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupMinOrderQuantity"), true).getString("sequenceId")) -1;
            int colBackupPriceLastUpdate = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "backupPriceLastUpdate"), true).getString("sequenceId")) -1;
            int colNotes = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "notes"), true).getString("sequenceId")) -1;
            int colUpc = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "upc"), true).getString("sequenceId")) -1;
            int colEbayUSListing = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayUsListing"), true).getString("sequenceId")) -1;
            int colEbayUKListing = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayUkListing"), true).getString("sequenceId")) -1;
            int colEbayAUListing = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayCaListing"), true).getString("sequenceId")) -1;
            int colEbayCAListing = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayDeListing"), true).getString("sequenceId")) -1;
            int colEbayDEListing = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayFrListing"), true).getString("sequenceId")) -1;
            int colEbayFRListing = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayEsListing"), true).getString("sequenceId")) -1;
            int colEbayESListing = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayListing"), true).getString("sequenceId")) -1;
            int colSmtListing = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "smtListing"), true).getString("sequenceId")) -1;
            int colAmaListing = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amaListing"), true).getString("sequenceId")) -1;
            int colWishListing = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "wishListing"), true).getString("sequenceId")) -1;
            int colEbayPicture = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "ebayPicture"), true).getString("sequenceId")) -1;
            int colAmazonPicture = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "amazonPicture"), true).getString("sequenceId")) -1;
            int colPlatform = Integer.parseInt(delegator.findOne("ProductMasterColumnTitle", UtilMisc.toMap("columnId", "platform"), true).getString("sequenceId")) -1;
            
            Label labelProductId = new Label(colProductId, 0, titleProductId);
            Label labelChineseName = new Label(colChineseName, 0, titleChineseName);
            Label labelEnglishName = new Label(colEnglishName, 0, titleEnglishName);
            Label labelWeight = new Label(colWeight, 0, titleWeight);
            Label labelProductGroup = new Label(colProductGroup, 0, titleProductGroup);
            Label labelStatusId = new Label(colStatusId, 0, titleStatusId);
            Label labelCategoryId = new Label(colCategoryId, 0, titleCategoryId);
            Label labelProductType = new Label(colProductType, 0, titleProductType);
            Label labelOwnerGroup = new Label(colOwnerGroup, 0, titleOwnerGroup);
            Label labelPurchaser = new Label(colPurchaser, 0, titlePurchaser);
            Label labelImageLink = new Label(colImageLink, 0, titleImageLink);
            Label labelExtraStockingDay = new Label(colExtraStockingDay, 0, titleExtraStockingDay);
            Label labelSourcingDate = new Label(colSourcingDate, 0, titleSourcingDate);
            Label labelSupplier = new Label(colSupplier, 0, titleSupplier);
            Label labelSupplierType = new Label(colSupplierType, 0, titleSupplierType);
            Label labelPrice = new Label(colPrice, 0, titlePrice);
            Label labelPriceLastUpdate = new Label(colPriceLastUpdate, 0, titlePriceLastUpdate);
            Label labelSupplierLink = new Label(colSupplierLink, 0, titleSupplierLink);
            Label labelDiscountRate = new Label(colDiscountRate, 0, titleDiscountRate);
            Label labelEta = new Label(colEta, 0, titleEta);
            Label labelMinOrderDays = new Label(colMinOrderDays, 0, titleMinOrderDays);
            Label labelMinOrderQuantity = new Label(colMinOrderQuantity, 0, titleMinOrderQuantity);
            Label labelRivalItemId = new Label(colRivalItemId, 0, titleRivalItemId);
            Label labelRivalLink = new Label(colRivalLink, 0, titleRivalLink);
            Label labelSoldPerDay = new Label(colSoldPerDay, 0, titleSoldPerDay);
            Label labelSamePriceProfitPercentage = new Label(colSamePriceProfitPercentage, 0, titleSamePriceProfitPercentage);
            Label labelUsd = new Label(colUsd, 0, titleUsd);
            Label labelGbp = new Label(colGbp, 0, titleGbp);
            Label labelAud = new Label(colAud, 0, titleAud);
            Label labelEur = new Label(colEur, 0, titleEur);
            Label labelCad = new Label(colCad, 0, titleCad);
            Label labelCpPercentage = new Label(colCpPercentage, 0, titleCpPercentage);
            Label labelGbpPercentage = new Label(colGbpPercentage, 0, titleGbpPercentage);
            Label labelAudPercentage = new Label(colAudPercentage, 0, titleAudPercentage);
            Label labelEurPercentage = new Label(colEurPercentage, 0, titleEurPercentage);
            Label labelCadPercentage = new Label(colCadPercentage, 0, titleCadPercentage);
            Label labelSmtPrice = new Label(colSmtPrice, 0, titleSmtPrice);
            Label labelSmtPercentage = new Label(colSmtPercentage, 0, titleSmtPercentage);
            Label labelSmtShippingMethod = new Label(colSmtShippingMethod, 0, titleSmtShippingMethod);
            Label labelWishProductPrice = new Label(colWishProductPrice, 0, titleWishProductPrice);
            Label labelWishShippingPrice = new Label(colWishShippingPrice, 0, titleWishShippingPrice);
            Label labelWishPercentage = new Label(colWishPercentage, 0, titleWishPercentage);
            Label labelAmaPrice = new Label(colAmaPrice, 0, titleAmaPrice);
            Label labelAmaPercentage = new Label(colAmaPercentage, 0, titleAmaPercentage);
            Label labelProductRivalRank = new Label(colProductRivalRank, 0, titleProductRivalRank);
            Label labelBackupSupplier = new Label(colBackupSupplier, 0, titleBackupSupplier);
            Label labelBackupSupplierPrice = new Label(colBackupSupplierPrice, 0, titleBackupSupplierPrice);
            Label labelBackupSupplierLink = new Label(colBackupSupplierLink, 0, titleBackupSupplierLink);
            Label labelBackupSupplierEta = new Label(colBackupSupplierEta, 0, titleBackupSupplierEta);
            Label labelBackupMinOrderDays = new Label(colBackupMinOrderDays, 0, titleBackupMinOrderDays);
            Label labelBackupMinOrderQuantity = new Label(colBackupMinOrderQuantity, 0, titleBackupMinOrderQuantity);
            Label labelBackupPriceLastUpdate = new Label(colPrice, 0, titleBackupPriceLastUpdate);
            Label labelNotes = new Label(colNotes, 0, titleNotes);
            Label labelUpc = new Label(colUpc, 0, titleUpc);
            Label labelEbayUSListing = new Label(colEbayUSListing, 0, titleEbayUSListing);
            Label labelEbayUKListing = new Label(colEbayUKListing, 0, titleEbayUKListing);
            Label labelEbayAUListing = new Label(colEbayAUListing, 0, titleEbayAUListing);
            Label labelEbayCAListing = new Label(colEbayCAListing, 0, titleEbayCAListing);
            Label labelEbayDEListing = new Label(colEbayDEListing, 0, titleEbayDEListing);
            Label labelEbayFRListing = new Label(colEbayFRListing, 0, titleEbayFRListing);
            Label labelEbayESListing = new Label(colEbayESListing, 0, titleEbayESListing);
            Label labelSmtListing = new Label(colSmtListing, 0, titleSmtListing);
            Label labelAmaListing = new Label(colAmaListing, 0, titleAmaListing);
            Label labelWishListing = new Label(colWishListing, 0, titleWishListing);
            Label labelEbayPicture = new Label(colEbayPicture, 0, titleEbayPicture);
            Label labelAmazonPicture = new Label(colAmazonPicture, 0, titleAmazonPicture);
            Label labelPlatform = new Label(colPlatform, 0, titlePlatform);
            
            sheet.addCell(labelProductId);
            sheet.addCell(labelChineseName);
            sheet.addCell(labelEnglishName);
            sheet.addCell(labelWeight);
            sheet.addCell(labelProductGroup);
            sheet.addCell(labelStatusId);
            sheet.addCell(labelCategoryId);
            sheet.addCell(labelProductType);
            sheet.addCell(labelOwnerGroup);
            sheet.addCell(labelPurchaser);
            sheet.addCell(labelImageLink);
            sheet.addCell(labelExtraStockingDay);
            sheet.addCell(labelSourcingDate);
            sheet.addCell(labelSupplier);
            sheet.addCell(labelSupplierType);
            sheet.addCell(labelPrice);
            sheet.addCell(labelPriceLastUpdate);
            sheet.addCell(labelSupplierLink);
            sheet.addCell(labelDiscountRate);
            sheet.addCell(labelEta);
            sheet.addCell(labelMinOrderDays);
            sheet.addCell(labelMinOrderQuantity);
            sheet.addCell(labelRivalItemId);
            sheet.addCell(labelRivalLink);
            sheet.addCell(labelSoldPerDay);
            sheet.addCell(labelSamePriceProfitPercentage);
            sheet.addCell(labelUsd);
            sheet.addCell(labelGbp);
            sheet.addCell(labelAud);
            sheet.addCell(labelEur);
            sheet.addCell(labelCad);
            sheet.addCell(labelCpPercentage);
            sheet.addCell(labelGbpPercentage);
            sheet.addCell(labelAudPercentage);
            sheet.addCell(labelEurPercentage);
            sheet.addCell(labelCadPercentage);
            sheet.addCell(labelSmtPrice);
            sheet.addCell(labelSmtPercentage);
            sheet.addCell(labelSmtShippingMethod);
            sheet.addCell(labelWishProductPrice);
            sheet.addCell(labelWishShippingPrice);
            sheet.addCell(labelWishPercentage);
            sheet.addCell(labelAmaPrice);
            sheet.addCell(labelAmaPercentage);
            sheet.addCell(labelProductRivalRank);
            sheet.addCell(labelBackupSupplier);
            sheet.addCell(labelBackupSupplierPrice);
            sheet.addCell(labelBackupSupplierLink);
            sheet.addCell(labelBackupSupplierEta);
            sheet.addCell(labelBackupMinOrderDays);
            sheet.addCell(labelBackupMinOrderQuantity);
            sheet.addCell(labelBackupPriceLastUpdate);
            sheet.addCell(labelNotes);
            sheet.addCell(labelUpc);
            sheet.addCell(labelEbayUSListing);
            sheet.addCell(labelEbayUKListing);
            sheet.addCell(labelEbayAUListing);
            sheet.addCell(labelEbayCAListing);
            sheet.addCell(labelEbayDEListing);
            sheet.addCell(labelEbayFRListing);
            sheet.addCell(labelEbayESListing);
            sheet.addCell(labelSmtListing);
            sheet.addCell(labelAmaListing);
            sheet.addCell(labelWishListing);
            sheet.addCell(labelEbayPicture);
            sheet.addCell(labelAmazonPicture);
            sheet.addCell(labelPlatform);
            
            //Filling column header -- END
            int maxCol = 0;
            List<GenericValue> pmColTitles = delegator.findList("ProductMasterColumnTitle", null, null, null, null, false);
            for (GenericValue pmColTitle : pmColTitles) {   //loop pmColTitles -- START
                if (Integer.parseInt(pmColTitle.getString("sequenceId")) >= maxCol) {
                    maxCol = Integer.parseInt(pmColTitle.getString("sequenceId")) - 1;
                }
            }   //loop pmColTitles -- END
            
            
            
            List<GenericValue> productMasterList = delegator.findList("ProductMaster", EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                                                     EntityCondition.makeCondition("productGroup",EntityOperator.NOT_LIKE ,"G5%")
                                                                                                                                     ))
                                                                      , null, UtilMisc.toList("productId"), null, false);
            
            /*List<GenericValue> productMasterList = delegator.findList("ProductMaster", EntityCondition.makeCondition(UtilMisc.toList(
             EntityCondition.makeCondition("productId",EntityOperator.EQUALS ,"61-1043_1-50")
             ))
             , null, UtilMisc.toList("productId"), null, false);*/
            //GenericValue productMaster = EntityUtil.getFirst(productMasterList);
            int row = 1;
            for (GenericValue productMaster : productMasterList) {  //loop productMasterList -- START
                
                String productId = productMaster.getString("productId");
                String chineseName = productMaster.getString("chineseName");
                String englishName = productMaster.getString("englishName");
                String weight = productMaster.getString("weight");
                String productGroup = productMaster.getString("productGroup");
                String statusId = productMaster.getString("statusId");
                String categoryId = productMaster.getString("categoryId");
                String productType = productMaster.getString("productType");
                String ownerGroup = productMaster.getString("ownerGroup");
                String purchaser = productMaster.getString("purchaser");
                String imageLink = productMaster.getString("imageLink");
                String extraStockingDay = productMaster.getString("extraStockingDay");
                String sourcingDate = productMaster.getString("sourcingDate");
                String notes = productMaster.getString("notes");
                String upc = productMaster.getString("upc");
                String platform = productMaster.getString("platform");
                
                Label valueProductId = new Label(colProductId, row, productId);
                Label valueChineseName = new Label(colChineseName, row, chineseName);
                Label valueEnglishName = new Label(colEnglishName, row, englishName);
                jxl.write.Number valueWeight = new jxl.write.Number(colWeight, row, Double.parseDouble(weight), priceFormat);
                Label valueProductGroup = new Label(colProductGroup, row, productGroup);
                Label valueStatusId = new Label(colStatusId, row, statusId);
                Label valueCategoryId = new Label(colCategoryId, row, categoryId);
                Label valueProductType = new Label(colProductType, row, productType);
                Label valueOwnerGroup = new Label(colOwnerGroup, row, ownerGroup);
                Label valuePurchaser = new Label(colPurchaser, row, purchaser);
                Label valueImageLink = new Label(colImageLink, row, imageLink);
                Label valueExtraStockingDay = new Label(colExtraStockingDay, row, extraStockingDay);
                Label valueSourcingDate = new Label(colSourcingDate, row, sourcingDate);
                Label valueNotes = new Label(colNotes, row, notes);
                Label valueUpc = new Label(colUpc, row, upc);
                Label valuePlatform = new Label(colPlatform, row, platform);
                
                sheet.addCell(valueProductId);
                sheet.addCell(valueChineseName);
                sheet.addCell(valueEnglishName);
                sheet.addCell(valueWeight);
                sheet.addCell(valueProductGroup);
                sheet.addCell(valueStatusId);
                sheet.addCell(valueCategoryId);
                sheet.addCell(valueProductType);
                sheet.addCell(valueOwnerGroup);
                sheet.addCell(valuePurchaser);
                sheet.addCell(valueImageLink);
                sheet.addCell(valueExtraStockingDay);
                sheet.addCell(valueSourcingDate);
                sheet.addCell(valueNotes);
                sheet.addCell(valueUpc);
                sheet.addCell(valuePlatform);
                
                //get CP -- START
                double usPriceDb = 0;
                GenericValue usPriceGV = delegator.findOne("ProductMasterPrice", UtilMisc.toMap("productId", productId, "currencyId", "USD"), false);
                BigDecimal usPriceBD = BigDecimal.ZERO;
                if (UtilValidate.isNotEmpty(usPriceGV)) {
                    usPriceBD = usPriceGV.getBigDecimal("price");
                    jxl.write.Number usd = new jxl.write.Number(colUsd, row, usPriceBD.doubleValue());
                    sheet.addCell(usd);
                } else {
                    Label usd = new Label(colUsd, row, "No US PRICE");
                    sheet.addCell(usd);
                }
                usPriceDb = usPriceBD.doubleValue();
                //get CP -- END
                
                //get supplier -- START
                GenericValue mainSupplierGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterSupplier", UtilMisc.toMap("productId", productId, "supplierType", "MAIN"), null, false));
                Label mainSupplier = new Label(colSupplier, row, mainSupplierGV.getString("supplier"));
                Label mainSupplierType = new Label(colSupplierType, row, mainSupplierGV.getString("supplierType"));
                Label mainSupplierLink = new Label(colSupplierLink, row, mainSupplierGV.getString("supplierLink"));
                jxl.write.Number price = new jxl.write.Number(colPrice, row, mainSupplierGV.getBigDecimal("price").doubleValue());
                sheet.addCell(mainSupplier);
                sheet.addCell(mainSupplierType);
                sheet.addCell(mainSupplierLink);
                sheet.addCell(price);
                
                if (UtilValidate.isNotEmpty(mainSupplierGV.getString("priceLastUpdate"))) {
                    Label valuePriceLastUpdate = new Label(colPriceLastUpdate, row, mainSupplierGV.getString("priceLastUpdate"));
                    sheet.addCell(valuePriceLastUpdate);
                }
                if (UtilValidate.isNotEmpty(mainSupplierGV.getLong("discountRate"))) {
                    jxl.write.Number discountRate = new jxl.write.Number(colDiscountRate, row, mainSupplierGV.getLong("discountRate"), percentIntFormat);
                    sheet.addCell(discountRate);
                }
                if (UtilValidate.isNotEmpty(mainSupplierGV.getLong("eta"))) {
                    jxl.write.Number eta = new jxl.write.Number(colEta, row, mainSupplierGV.getLong("eta"));
                    sheet.addCell(eta);
                }
                if (UtilValidate.isNotEmpty(mainSupplierGV.getLong("minOrderDays"))) {
                    jxl.write.Number minOrderDays = new jxl.write.Number(colMinOrderDays, row, mainSupplierGV.getLong("minOrderDays"));
                    sheet.addCell(minOrderDays);
                }
                if (UtilValidate.isNotEmpty(mainSupplierGV.getLong("minOrderQuantity"))) {
                    jxl.write.Number minOrderQuantity = new jxl.write.Number(colMinOrderQuantity, row, mainSupplierGV.getLong("minOrderQuantity"));
                    sheet.addCell(minOrderQuantity);
                }
                
                List<GenericValue> backupSupplierList = delegator.findByAnd("ProductMasterSupplier", UtilMisc.toMap("productId", productId, "supplierType", "BACKUP"), null, false);
                if (UtilValidate.isNotEmpty(backupSupplierList)) {  //if backupSupplierList is not empty -- START
                    GenericValue backupSupplierGV =  EntityUtil.getFirst(backupSupplierList);
                    
                    Label backupSupplier = new Label(maxCol + 1, row, backupSupplierGV.getString("supplier"));
                    Label backupSupplierLink = new Label(maxCol + 2, row, backupSupplierGV.getString("supplierLink"));
                    jxl.write.Number backupSupplierPrice = new jxl.write.Number(maxCol + 3, row, backupSupplierGV.getBigDecimal("price").doubleValue());
                    sheet.addCell(backupSupplier);
                    sheet.addCell(backupSupplierLink);
                    sheet.addCell(backupSupplierPrice);
                    maxCol = maxCol + 3;

                    if (UtilValidate.isNotEmpty(backupSupplierGV.getString("priceLastUpdate"))) {
                        Label valueBackupPriceLastUpdate = new Label(colBackupPriceLastUpdate, row, backupSupplierGV.getString("priceLastUpdate"));
                        sheet.addCell(valueBackupPriceLastUpdate);
                    }
                    if (UtilValidate.isNotEmpty(backupSupplierGV.getLong("eta"))) {
                        jxl.write.Number backupSupplierEta = new jxl.write.Number(maxCol + 1, row, backupSupplierGV.getLong("eta"));
                        sheet.addCell(backupSupplierEta);
                        maxCol = maxCol + 1;
                    }
                    if (UtilValidate.isNotEmpty(backupSupplierGV.getLong("minOrderDays"))) {
                        jxl.write.Number backupMinOrderDays = new jxl.write.Number(maxCol + 1, row, backupSupplierGV.getLong("minOrderDays"));
                        sheet.addCell(backupMinOrderDays);
                        maxCol = maxCol + 1;
                    }
                    if (UtilValidate.isNotEmpty(backupSupplierGV.getLong("minOrderQuantity"))) {
                        jxl.write.Number backupMinOrderQuantity = new jxl.write.Number(maxCol + 1, row, backupSupplierGV.getLong("minOrderQuantity"));
                        sheet.addCell(backupMinOrderQuantity);
                        maxCol = maxCol + 1;
                    }

                }   //if backupSupplierList is not empty -- END
                /*List<GenericValue> backupSupplierList = delegator.findByAnd("ProductMasterSupplier", UtilMisc.toMap("productId", productId, "supplierType", "BACKUP"), null, false);
                int backup = 1;
                for (GenericValue backupSupplierGV : backupSupplierList) {  //loop backupSupplierGV -- START
                    Label labelBackupSupplier = new Label(maxCol + 1, 0, titleSupplier + "-" + backup);
                    Label labelBackupSupplierType = new Label(maxCol + 2, 0, titleSupplierType + "-" + backup);
                    Label labelBackupLink = new Label(maxCol + 3, 0, titleSupplierLink + "-" + backup);
                    Label labelBackupPrice = new Label(maxCol + 4, 0, titlePrice + "-" + backup);
                    sheet.addCell(labelBackupSupplier);
                    sheet.addCell(labelBackupSupplierType);
                    sheet.addCell(labelBackupLink);
                    sheet.addCell(labelBackupPrice);
                    
                    Label backupSupplier = new Label(maxCol + 1, row, backupSupplierGV.getString("supplier"));
                    Label backupSupplierType = new Label(maxCol + 2, row, backupSupplierGV.getString("supplierType"));
                    Label backupSupplierLink = new Label(maxCol + 3, row, backupSupplierGV.getString("supplierLink"));
                    jxl.write.Number backupPrice = new jxl.write.Number(maxCol + 4, row, backupSupplierGV.getBigDecimal("price").doubleValue());
                    sheet.addCell(backupSupplier);
                    sheet.addCell(backupSupplierType);
                    sheet.addCell(backupSupplierLink);
                    sheet.addCell(backupPrice);
                    maxCol = maxCol + 4;
                    
                    if (UtilValidate.isNotEmpty(backupSupplierGV.getLong("discountRate"))) {
                        Label labelBackupDiscountRate = new Label(maxCol + 1, 0, titleDiscountRate + "-" + backup);
                        jxl.write.Number backupDiscountRate = new jxl.write.Number(maxCol + 1, row, backupSupplierGV.getLong("discountRate"));
                        sheet.addCell(labelBackupDiscountRate);
                        sheet.addCell(backupDiscountRate);
                        maxCol = maxCol + 1;
                    }
                    if (UtilValidate.isNotEmpty(backupSupplierGV.getLong("eta"))) {
                        Label labelBackupEta = new Label(maxCol + 1, 0, titleEta + "-" + backup);
                        jxl.write.Number backupEta = new jxl.write.Number(maxCol + 1, row, backupSupplierGV.getLong("eta"));
                        sheet.addCell(labelBackupEta);
                        sheet.addCell(backupEta);
                        maxCol = maxCol + 1;
                    }
                    if (UtilValidate.isNotEmpty(backupSupplierGV.getLong("minOrderDays"))) {
                        Label labelBackupMinOrderDays = new Label(maxCol + 1, 0, titleMinOrderDays + "-" + backup);
                        jxl.write.Number backupMinOrderDays = new jxl.write.Number(maxCol + 1, row, backupSupplierGV.getLong("minOrderDays"));
                        sheet.addCell(labelBackupMinOrderDays);
                        sheet.addCell(backupMinOrderDays);
                        maxCol = maxCol + 1;
                    }
                    backup++;
                }   //loop backupSupplierGV -- END*/
                //get supplier -- END
                
                //get rival -- START
                List<GenericValue> rivalList = delegator.findByAnd("ProductMasterRival", UtilMisc.toMap("productId", productId), null, false);
                int rivalCount = 0;
                if (UtilValidate.isNotEmpty(rivalList)) {   //if rivalList is not empty -- START
                    for (GenericValue rivalGV : rivalList) {    //loop rivalList -- START
                        if (rivalCount < 1) {   //first rival -- START
                            if (UtilValidate.isNotEmpty(rivalGV.getString("rivalItemId"))) {
                                Label mainRival = new Label(colRivalItemId, row, rivalGV.getString("rivalItemId"));
                                sheet.addCell(mainRival);
                            }
                            if (UtilValidate.isNotEmpty(rivalGV.getString("rivalLink"))) {
                                Label mainRivalLink = new Label(colRivalLink, row, rivalGV.getString("rivalLink"));
                                sheet.addCell(mainRivalLink);
                            }
                            if (UtilValidate.isNotEmpty(rivalGV.getString("soldPerDay"))) {
                                jxl.write.Number mainSoldPerDay = new jxl.write.Number(colSoldPerDay, row, Double.parseDouble(rivalGV.getString("soldPerDay")), priceFormat);
                                sheet.addCell(mainSoldPerDay);
                            }
                            if (UtilValidate.isNotEmpty(rivalGV.getString("samePriceProfitPercentage"))) {
                                jxl.write.Number mainSamePriceProfitPercentage = new jxl.write.Number(colSamePriceProfitPercentage, row, Double.parseDouble(rivalGV.getString("samePriceProfitPercentage")), percentIntFormat);
                                sheet.addCell(mainSamePriceProfitPercentage);
                            }
                        }   //first rival -- END
                        /*else {  //rest of next rival -- START
                            if (UtilValidate.isNotEmpty(rivalGV.getString("rivalItemId"))) {
                                Label secondRivalTitle = new Label(maxCol + 1, row, titleRivalItemId + "-" + backup);
                                sheet.addCell(secondRivalTitle);
                                Label secondRival = new Label(maxCol + 1, row, rivalGV.getString("rivalItemId"));
                                sheet.addCell(secondRival);
                                maxCol = maxCol + 1;
                            }
                            if (UtilValidate.isNotEmpty(rivalGV.getString("rivalLink"))) {
                                Label secondRivalLinkTitle = new Label(maxCol + 1, row, titleRivalLink + "-" + backup);
                                sheet.addCell(secondRivalLinkTitle);
                                Label secondRivalLink = new Label(maxCol + 1, row, rivalGV.getString("rivalLink"));
                                sheet.addCell(secondRivalLink);
                                maxCol = maxCol + 1;
                            }
                            if (UtilValidate.isNotEmpty(rivalGV.getString("soldPerDay"))) {
                                Label secondSoldPerDayTitle = new Label(maxCol + 1, row, titleSoldPerDay + "-" + backup);
                                sheet.addCell(secondSoldPerDayTitle);
                                jxl.write.Number secondSoldPerDay = new jxl.write.Number(maxCol + 1, row, Double.parseDouble(rivalGV.getString("soldPerDay")), priceFormat);
                                sheet.addCell(secondSoldPerDay);
                                maxCol = maxCol + 1;
                            }
                            if (UtilValidate.isNotEmpty(rivalGV.getString("samePriceProfitPercentage"))) {
                                Label secondSamePriceProfitPercentageTitle = new Label(maxCol + 1, row, titleSamePriceProfitPercentage + "-" + backup);
                                sheet.addCell(secondSamePriceProfitPercentageTitle);
                                jxl.write.Number secondSamePriceProfitPercentage = new jxl.write.Number(maxCol + 1, row, Double.parseDouble(rivalGV.getString("samePriceProfitPercentage")), percentIntFormat);
                                sheet.addCell(secondSamePriceProfitPercentage);
                                maxCol = maxCol + 1;
                            }
                        }   //rest of next rival -- END*/
                        rivalCount++;
                    }   //loop rivalList -- END
                }   //if rivalList is not empty -- END
                //get rival -- END
                Debug.logError("Processing calculation for SKU " + productId, module);
                //CALCULATION PART -- START ---------------------------
                double usdToRmb = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CNY"), null, false)).getDouble("conversionFactor");
                double usdToGbp = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "GBP"), null, false)).getDouble("conversionFactor");
                double usdToAud = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "AUD"), null, false)).getDouble("conversionFactor");
                double usdToCad = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "CAD"), null, false)).getDouble("conversionFactor");
                double usdToEur = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","USD", "uomIdTo", "EUR"), null, false)).getDouble("conversionFactor");
                double hkdToRmb = EntityUtil.getFirst(delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId","HKD", "uomIdTo", "CNY"), null, false)).getDouble("conversionFactor");
                double usdToUsd = 1;
                double productCost = mainSupplierGV.getBigDecimal("price").doubleValue();
                double packagingCost = Double.parseDouble(EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "packagingCost"), null, false)).getString("value"));
                double ebayFeePct = getFeePct(delegator, "ebayFeeBreakUS", usPriceDb);
                
                //get CP percentage -- START
                double weightDb = Double.parseDouble(weight);
                double shippingCostUS = 0.0;
                double discountRateUS = 0.0;
                double pricePerGramUS = 0.0;
                double additionalShipCostUS = 0.0;
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostUS = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                else {  //if weight is less than 2000 - ship by normal shipment - START
                    GenericValue productTypeShippingMethod = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productTypeShippingMethod", "name", productType), null, false));
                    String carrierId = productTypeShippingMethod.getString("value");
                    List<GenericValue> priceBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", carrierId), null, false);
                    String priceBreakShippingValue = "REGISTERED";
                    for (GenericValue priceBreakShipping : priceBreakShippingList) {    //loop priceBreakShippingList -- START
                        BigDecimal priceBreakLoBD = priceBreakShipping.getBigDecimal("priceBreakLo");
                        BigDecimal priceBreakHiBD = priceBreakShipping.getBigDecimal("priceBreakHi");
                        if (((usPriceDb * usdToRmb) >= priceBreakLoBD.doubleValue()) && ((usPriceDb * usdToRmb) < priceBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                            priceBreakShippingValue = priceBreakShipping.getString("value");
                            break;
                        }   //if CP is lower than priceBreakLo -- END
                    }   //loop priceBreakShippingList -- END
                    GenericValue shippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", priceBreakShippingValue), false);
                    discountRateUS = Double.valueOf(shippingCostGV.getLong("discountRate"));
                    pricePerGramUS = shippingCostGV.getDouble("pricePerGram");
                    if (UtilValidate.isNotEmpty(shippingCostGV.getDouble("additionalCost"))) {
                        additionalShipCostUS = shippingCostGV.getDouble("additionalCost");
                    }
                    shippingCostUS = (weightDb * pricePerGramUS + additionalShipCostUS) * (1.0 - (discountRateUS/100.0));
                }   //if weight is less than 2000 - ship by normal shipment - END
                
                double cpPercentageDb = ((usPriceDb * usdToRmb * (1.0 - (ebayFeePct / 100.0))) - shippingCostUS - productCost - packagingCost) / (usPriceDb * usdToRmb);
                jxl.write.Number cpPercentage = new jxl.write.Number(colCpPercentage, row, cpPercentageDb, percentIntFormat);
                sheet.addCell (cpPercentage);
                //get CP percentage -- START
                
                //get UK -- START
                GenericValue productTypeShippingMethod = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productTypeShippingMethod", "name", productType), null, false));
                String carrierId = productTypeShippingMethod.getString("value");
                List<GenericValue> costBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", carrierId), null, false);
                String costBreakShippingValue = "REGISTERED";
                
                GenericValue marketplaceProfitUK = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "UK"), null, false));
                GenericValue lowestPriceUK = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "UK"), null, false));
                String profitPercentUKStr = marketplaceProfitUK.getString("value");
                String ukLowestProfitPercentage = lowestPriceUK.getString("priceBreakLo");
                String lowestPriceUKStr = lowestPriceUK.getString("value");
                double ukPriceDb = usPriceDb * usdToGbp * (1.0 + (Double.parseDouble(profitPercentUKStr)/100.0));
                if (usPriceDb == 0.99 && cpPercentageDb > 0.3) {
                    ukPriceDb = 0.99;
                }
                if (ukPriceDb < Double.parseDouble(lowestPriceUKStr)) {
                    ukPriceDb = Double.parseDouble(lowestPriceUKStr);
                }
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((ukPriceDb / usdToGbp * usdToRmb) >= costBreakLoBD.doubleValue()) && ((ukPriceDb / usdToGbp * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                GenericValue shippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                double discountRate = Double.valueOf(shippingCostGV.getLong("discountRate"));
                double pricePerGram = shippingCostGV.getDouble("pricePerGram");
                double additionalShipCost = 0.0;
                if (UtilValidate.isNotEmpty(shippingCostGV.getDouble("additionalCost"))) {
                    additionalShipCost = shippingCostGV.getDouble("additionalCost");
                }
                double shippingCost = (weightDb * pricePerGram + additionalShipCost) * (1.0 - (discountRate/100.0));
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCost = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                
                double ebayFeePctGbp = getFeePct(delegator, "ebayFeeBreakUK", ukPriceDb);
                double gbpPercentageDb = ((ukPriceDb / usdToGbp * usdToRmb * (1.0 - (ebayFeePctGbp / 100.0))) - shippingCost - productCost - packagingCost) / (ukPriceDb / usdToGbp * usdToRmb);
                
                jxl.write.Number gbp = new jxl.write.Number(colGbp, row, ukPriceDb, priceFormat);
                sheet.addCell (gbp);
                jxl.write.Number gbpPercentage = new jxl.write.Number(colGbpPercentage, row, gbpPercentageDb, percentIntFormat);
                sheet.addCell (gbpPercentage);
                //get UK -- END
                
                //get AU -- START
                GenericValue marketplaceProfitAU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "AU"), null, false));
                GenericValue lowestPriceAU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "AU"), null, false));
                String profitPercentAUStr = marketplaceProfitAU.getString("value");
                String auLowestProfitPercentage = lowestPriceAU.getString("priceBreakLo");
                String lowestPriceAUStr = lowestPriceAU.getString("value");
                double auPriceDb = usPriceDb * usdToAud * (1.0 + (Double.parseDouble(profitPercentAUStr)/100.0));
                if (usPriceDb == 0.99 && cpPercentageDb > 0.3) {
                    auPriceDb = 0.99;
                }
                if (auPriceDb < Double.parseDouble(lowestPriceAUStr)) {
                    auPriceDb = Double.parseDouble(lowestPriceAUStr);
                }
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((auPriceDb / usdToAud * usdToRmb) >= costBreakLoBD.doubleValue()) && ((auPriceDb / usdToAud * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                GenericValue auShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                double auDiscountRate = Double.valueOf(auShippingCostGV.getLong("discountRate"));
                double auPricePerGram = auShippingCostGV.getDouble("pricePerGram");
                double auAdditionalShipCost = 0.0;
                if (UtilValidate.isNotEmpty(auShippingCostGV.getDouble("additionalCost"))) {
                    auAdditionalShipCost = auShippingCostGV.getDouble("additionalCost");
                }
                double auShippingCost = (weightDb * auPricePerGram + auAdditionalShipCost) * (1.0 - (auDiscountRate/100.0));
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    auShippingCost = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                
                double ebayFeePctAud = getFeePct(delegator, "ebayFeeBreakAU", auPriceDb);
                double audPercentageDb = ((auPriceDb / usdToAud * usdToRmb * (1.0 - (ebayFeePctAud / 100.0))) - auShippingCost - productCost - packagingCost) / (auPriceDb / usdToAud * usdToRmb);
                jxl.write.Number aud = new jxl.write.Number(colAud, row, auPriceDb, priceFormat);
                sheet.addCell (aud);
                jxl.write.Number audPercentage = new jxl.write.Number(colAudPercentage, row, audPercentageDb, percentIntFormat);
                sheet.addCell (audPercentage);
                //get AU -- END
                
                //get CA -- START
                GenericValue marketplaceProfitCA = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "CA"), null, false));
                GenericValue lowestPriceCA = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "CA"), null, false));
                String profitPercentCAStr = marketplaceProfitCA.getString("value");
                String caLowestProfitPercentage = lowestPriceCA.getString("priceBreakLo");
                String lowestPriceCAStr = lowestPriceCA.getString("value");
                double caPriceDb = usPriceDb * usdToCad * (1.0 + (Double.parseDouble(profitPercentCAStr)/100.0));
                if (usPriceDb == 0.99 && cpPercentageDb > 0.3) {
                    caPriceDb = 0.99;
                }
                if (caPriceDb < Double.parseDouble(lowestPriceCAStr)) {
                    caPriceDb = Double.parseDouble(lowestPriceCAStr);
                }
                for (GenericValue costBreakShipping : costBreakShippingList) {    //loop costBreakShippingList -- START
                    BigDecimal costBreakLoBD = costBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal costBreakHiBD = costBreakShipping.getBigDecimal("priceBreakHi");
                    if (((caPriceDb / usdToCad * usdToRmb) >= costBreakLoBD.doubleValue()) && ((caPriceDb / usdToCad * usdToRmb) < costBreakHiBD.doubleValue())) { //if CP is lower than priceBreakLo -- START
                        costBreakShippingValue = costBreakShipping.getString("value");
                    }   //if CP is lower than priceBreakLo -- END
                }   //loop costBreakShippingList -- END
                GenericValue caShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", carrierId, "shippingMethodType", costBreakShippingValue), false);
                double caDiscountRate = Double.valueOf(caShippingCostGV.getLong("discountRate"));
                double caPricePerGram = caShippingCostGV.getDouble("pricePerGram");
                double caAdditionalShipCost = 0.0;
                if (UtilValidate.isNotEmpty(caShippingCostGV.getDouble("additionalCost"))) {
                    caAdditionalShipCost = caShippingCostGV.getDouble("additionalCost");
                }
                double caShippingCost = (weightDb * caPricePerGram + caAdditionalShipCost) * (1.0 - (caDiscountRate/100.0));
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    caShippingCost = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                
                double ebayFeePctCad = getFeePct(delegator, "ebayFeeBreakCA", caPriceDb);
                double cadPercentageDb = ((caPriceDb / usdToCad * usdToRmb * (1.0 - (ebayFeePctCad / 100.0))) - caShippingCost - productCost - packagingCost) / (caPriceDb / usdToCad * usdToRmb);
                
                jxl.write.Number cad = new jxl.write.Number(colCad, row, caPriceDb, priceFormat);
                sheet.addCell (cad);
                jxl.write.Number cadPercentage = new jxl.write.Number(colCadPercentage, row, cadPercentageDb, percentIntFormat);
                sheet.addCell (cadPercentage);
                //get CA -- END
                
                //get Eur -- START
                GenericValue marketplaceProfitEU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "LT"), null, false));
                String profitPercentEUStr = marketplaceProfitEU.getString("value");
                double eurPercentageDb = Double.parseDouble(profitPercentEUStr) / 100.0;
                double euPriceDb = 0.0;
                String eurCarrierId = "DEUTSCHEPOST";
                String eurShipMethType = "STANDARD";
                /*if (carrierId.equals("YANWEN")) {
                 eurCarrierId = carrierId;
                 eurShipMethType = costBreakShippingValue;
                 }*/
                GenericValue shippingCostEurGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", eurCarrierId, "shippingMethodType", eurShipMethType), false);
                double discountRateEur = Double.valueOf(shippingCostEurGV.getLong("discountRate"));
                double pricePerGramEur = shippingCostEurGV.getBigDecimal("pricePerGram").doubleValue();
                double additionalShipCostEur = 0.0;
                if (UtilValidate.isNotEmpty(shippingCostEurGV.getDouble("additionalCost"))) {
                    additionalShipCostEur = shippingCostEurGV.getDouble("additionalCost");
                }
                
                double shippingCostEur = (weightDb * pricePerGramEur + additionalShipCostEur) * (1.0 - (discountRateEur/100.0)) * hkdToRmb;
                /*if (carrierId.equals("YANWEN")) {
                 shippingCostEur = (weightDb * pricePerGramEur + additionalShipCostEur) * (1.0 - (discountRateEur/100.0));
                 }*/
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostEur = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                List<GenericValue> ebayFeeBreakList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "ebayFeeBreak"), null, false);
                double ebayFeePctEur = 0.0;
                String ebayFeeBreakId = null;
                boolean breakForLoop = false;
                for (GenericValue ebayFeeBreak : ebayFeeBreakList) {    //loop ebayFeeBreakList -- START
                    BigDecimal priceBreakLoBD = ebayFeeBreak.getBigDecimal("priceBreakLo");
                    BigDecimal priceBreakHiBD = ebayFeeBreak.getBigDecimal("priceBreakHi");
                    String ebayFeeBreakStr = ebayFeeBreak.getString("value");
                    ebayFeePctEur = Double.parseDouble(ebayFeeBreakStr);
                    euPriceDb = usdToEur / usdToRmb * ((shippingCostEur + productCost + packagingCost) / (1.0 - eurPercentageDb - (ebayFeePctEur/100.0)));
                    
                    for (GenericValue ebayFeeBreakInner : ebayFeeBreakList) {   //inner loop -- START
                        BigDecimal priceBreakLoBD2 = ebayFeeBreakInner.getBigDecimal("priceBreakLo");
                        BigDecimal priceBreakHiBD2 = ebayFeeBreakInner.getBigDecimal("priceBreakHi");
                        if ((euPriceDb >= priceBreakLoBD2.doubleValue()) && (euPriceDb < priceBreakHiBD2.doubleValue())) {  //if euPriceDb is within range -- START
                            ebayFeeBreakId = ebayFeeBreakInner.getString("pmVariableId");
                            breakForLoop = true;
                            break;
                        }   //if euPriceDb is within range -- END
                    }   //inner loop -- END
                    if (breakForLoop) {
                        break;
                    }
                }   //loop ebayFeeBreakList -- END
                GenericValue finalEbayFeePctEurGV = delegator.findOne("ProductMasterVariable", UtilMisc.toMap("pmVariableId", ebayFeeBreakId), false);
                String finalEbayFeeStr = finalEbayFeePctEurGV.getString("value");
                ebayFeePctEur = Double.parseDouble(finalEbayFeeStr);
                euPriceDb = usdToEur / usdToRmb * ((shippingCostEur + productCost + packagingCost) / (1.0 - eurPercentageDb - (ebayFeePctEur/100.0)));
                eurPercentageDb = ((euPriceDb / usdToEur * usdToRmb * (1.0 - (ebayFeePctEur / 100.0))) - shippingCostEur - productCost - packagingCost) / (euPriceDb / usdToEur * usdToRmb);
                
                GenericValue lowestPriceEU = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "lowestPrice", "name", "LT"), null, false));
                String lowestPriceEUStr = lowestPriceEU.getString("value");
                if (euPriceDb < Double.parseDouble(lowestPriceEUStr)) {
                    euPriceDb = Double.parseDouble(lowestPriceEUStr);
                    eurPercentageDb = ((euPriceDb / usdToEur * usdToRmb * (1.0 - (ebayFeePctEur / 100.0))) - shippingCostEur - productCost - packagingCost) / (euPriceDb / usdToEur * usdToRmb);
                }
                
                jxl.write.Number eur = new jxl.write.Number(colEur, row, euPriceDb, priceFormat);
                sheet.addCell (eur);
                jxl.write.Number eurPercentage = new jxl.write.Number(colEurPercentage, row, eurPercentageDb, percentIntFormat);
                sheet.addCell (eurPercentage);
                //get Eur -- END
                
                //get SMT -- START
                double smtPriceDb = 0.0;
                double smtPercentageDb = 0.0;
                String smtShippingMethodType = null;
                double shippingCostSmt = 0.0;
                double smtFeePct = getFeePct(delegator, "smtFeeBreak", smtPriceDb);
                double priceBreakRangeDb = 0.0;
                GenericValue marketplaceProfitSmt = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "SMT"), null, false));
                String profitPercentSmtStr = marketplaceProfitSmt.getString("value");
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostSmt = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                    smtPercentageDb = Double.parseDouble(profitPercentSmtStr) / 100.0;
                    smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                    smtShippingMethodType = "EMS";
                    smtFeePct = getFeePct(delegator, "smtFeeBreak", smtPriceDb);
                    smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                }   //if weight is more than 2000 - ship by EMS - END
                else {  //weight is less than 2000 -- START
                    List<GenericValue> smtShippingList = delegator.findByAnd("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT"), null, false);
                    for (GenericValue smtShippingGV : smtShippingList) {    //loop smtShippingList -- START
                        smtShippingMethodType = smtShippingGV.getString("shippingMethodType");
                        double discountRateSmt = Double.valueOf(smtShippingGV.getLong("discountRate"));
                        double pricePerGramSmt = smtShippingGV.getDouble("pricePerGram");
                        double additionalShipCostSmt = 0.0;
                        if (UtilValidate.isNotEmpty(smtShippingGV.getDouble("additionalCost"))) {
                            additionalShipCostSmt = smtShippingGV.getDouble("additionalCost");
                        }
                        if (carrierId.equals("YANWEN") && smtShippingMethodType.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                            discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", smtShippingMethodType), false).getLong("discountRate"));
                        }   //if shipped by Yanwen -- END
                        shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                        smtPercentageDb = Double.parseDouble(profitPercentSmtStr) / 100.0;
                        smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                        
                        GenericValue priceBreakShippingSmt = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "SMT", "value", smtShippingMethodType), null, false));
                        BigDecimal smtPriceBreakLoBD = priceBreakShippingSmt.getBigDecimal("priceBreakLo");
                        BigDecimal smtPriceBreakHiBD = priceBreakShippingSmt.getBigDecimal("priceBreakHi");
                        priceBreakRangeDb = smtPriceBreakLoBD.doubleValue();
                        if ((smtPriceDb >= smtPriceBreakLoBD.doubleValue()) && (smtPriceDb < smtPriceBreakHiBD.doubleValue())) { //if -- START
                            break;
                        }   //if  -- END
                    }   //loop smtShippingList -- END
                    smtFeePct = getFeePct(delegator, "smtFeeBreak", smtPriceDb);
                    smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                    //Debug.logError("===========before priceBreakRange============", module);
                    //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                    //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                    //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                    //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                    
                    GenericValue priceBreakRangeGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakRange", "name", "SMT"), null, false));
                    String priceBreakRangeStr = priceBreakRangeGV.getString("value");
                    if (smtPriceDb <= (priceBreakRangeDb + Double.parseDouble(priceBreakRangeStr))) {   //if price is within price break range -- START
                        List<GenericValue> priceBreakShippingSmtList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "SMT"), null, false);
                        for (GenericValue priceBreakRangeSmt : priceBreakShippingSmtList) { //loop priceBreakShippingSmtList -- START
                            BigDecimal smtPriceBreakLoBD = priceBreakRangeSmt.getBigDecimal("priceBreakLo");
                            BigDecimal smtPriceBreakHiBD = priceBreakRangeSmt.getBigDecimal("priceBreakHi");
                            if (((priceBreakRangeDb - 0.1) >= smtPriceBreakLoBD.doubleValue()) && ((priceBreakRangeDb - 0.1) < smtPriceBreakHiBD.doubleValue())) { //if -- START
                                String finalShippingMethodSmt = priceBreakRangeSmt.getString("value");
                                GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", finalShippingMethodSmt), false);
                                double discountRateSmt = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                                if (carrierId.equals("YANWEN") && finalShippingMethodSmt.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                                    discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodSmt), false).getLong("discountRate"));
                                }   //if shipped by Yanwen -- END
                                
                                double pricePerGramSmt = finalShippingCostGV.getDouble("pricePerGram");
                                double additionalShipCostSmt = 0.0;
                                if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                                    additionalShipCostSmt = finalShippingCostGV.getDouble("additionalCost");
                                }
                                shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                                smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                                smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                                smtShippingMethodType = finalShippingMethodSmt;
                                break;
                            }   //if  -- END
                        }   //loop priceBreakShippingSmtList -- END
                    }   //if price is within price break range -- END
                    //Debug.logError("===========First LOOP ============", module);
                    //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                    //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                    //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                    //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                    
                    //this loop is to change the price which falls down above 7 USD, but still ship by standard, so change it back to registered, and then change the price again to still get the 15% profit
                    List<GenericValue> finalPriceBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "SMT"), null, false);
                    for (GenericValue finalPriceBreakShipping : finalPriceBreakShippingList) { //final loop finalPriceBreakShippingList -- START
                        BigDecimal finalPriceBreakLoBD = finalPriceBreakShipping.getBigDecimal("priceBreakLo");
                        BigDecimal finalPriceBreakHiBD = finalPriceBreakShipping.getBigDecimal("priceBreakHi");
                        if ((smtPriceDb >= finalPriceBreakLoBD.doubleValue()) && (smtPriceDb < finalPriceBreakHiBD.doubleValue())) {
                            //Debug.logError("finalPriceBreakLoBD: " + finalPriceBreakLoBD, module);
                            //Debug.logError("finalPriceBreakHiBD: " + finalPriceBreakHiBD, module);
                            //Debug.logError("smtPriceDb: " + smtPriceDb, module);
                            String finalShippingMethodSmt = finalPriceBreakShipping.getString("value");
                            if (!smtShippingMethodType.equals(finalShippingMethodSmt)) {
                                GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", finalShippingMethodSmt), false);
                                double discountRateSmt = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                                if (carrierId.equals("YANWEN") && finalShippingMethodSmt.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                                    discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodSmt), false).getLong("discountRate"));
                                }   //if shipped by Yanwen -- END
                                
                                double pricePerGramSmt = finalShippingCostGV.getDouble("pricePerGram");
                                double additionalShipCostSmt = 0.0;
                                if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                                    additionalShipCostSmt = finalShippingCostGV.getDouble("additionalCost");
                                }
                                shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                                smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                                smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                                smtShippingMethodType = finalShippingMethodSmt;
                                //Debug.logError("===========Second LOOP ============", module);
                                //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                                //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                                //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                                //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                                break;
                            }
                        }
                    }   //final loop priceBreakShippingSmtList -- END
                    
                    if (carrierId.equals("YANWEN") && smtShippingMethodType.equals("STANDARD")) {
                        //Debug.logError("==========Third loop to check if shipped by Yanwen and standard, change to SMT standard=============", module);
                        GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", smtShippingMethodType), false);
                        double discountRateSmt = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                        double pricePerGramSmt = finalShippingCostGV.getDouble("pricePerGram");
                        double additionalShipCostSmt = 0.0;
                        if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                            additionalShipCostSmt = finalShippingCostGV.getDouble("additionalCost");
                        }
                        shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                        smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                        smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                        //Debug.logError("===========Yanwen-STANDARD LOOP - change to SMT STANDARD ============", module);
                        //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                        //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                        //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                        //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                        
                        //this loop is to change the price which falls down above 7 USD, but still ship by standard, so change it back to registered, and then change the price again to still get the 15% profit
                        for (GenericValue finalPriceBreakShipping : finalPriceBreakShippingList) { //final loop finalPriceBreakShippingList -- START
                            BigDecimal finalPriceBreakLoBD = finalPriceBreakShipping.getBigDecimal("priceBreakLo");
                            BigDecimal finalPriceBreakHiBD = finalPriceBreakShipping.getBigDecimal("priceBreakHi");
                            if ((smtPriceDb >= finalPriceBreakLoBD.doubleValue()) && (smtPriceDb < finalPriceBreakHiBD.doubleValue())) {
                                //Debug.logError("smtPriceDb: " + smtPriceDb, module);
                                String finalShippingMethodSmt = finalPriceBreakShipping.getString("value");
                                if (!smtShippingMethodType.equals(finalShippingMethodSmt)) {
                                    GenericValue finalShippingCostGV2 = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "SMT", "shippingMethodType", finalShippingMethodSmt), false);
                                    discountRateSmt = Double.valueOf(finalShippingCostGV2.getLong("discountRate"));
                                    if (carrierId.equals("YANWEN") && finalShippingMethodSmt.equals("REGISTERED")) {   //if shipped by Yanwen -- START
                                        discountRateSmt = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodSmt), false).getLong("discountRate"));
                                    }   //if shipped by Yanwen -- END
                                    
                                    pricePerGramSmt = finalShippingCostGV2.getDouble("pricePerGram");
                                    additionalShipCostSmt = 0.0;
                                    if (UtilValidate.isNotEmpty(finalShippingCostGV2.getDouble("additionalCost"))) {
                                        additionalShipCostSmt = finalShippingCostGV2.getDouble("additionalCost");
                                    }
                                    shippingCostSmt = (weightDb * pricePerGramSmt + additionalShipCostSmt) * (1.0 - (discountRateSmt/100.0));
                                    smtPriceDb = 1.0 / usdToRmb * ((shippingCostSmt + productCost + packagingCost) / (1.0 - (smtFeePct/100.0) - smtPercentageDb));
                                    smtPercentageDb = ((smtPriceDb / usdToUsd * usdToRmb * (1.0 - (smtFeePct / 100.0))) - shippingCostSmt - productCost - packagingCost) / (smtPriceDb / usdToUsd * usdToRmb);
                                    smtShippingMethodType = finalShippingMethodSmt;
                                    //Debug.logError("===========Yanwen-STANDARD LOOP - change to SMT STANDARD ---  THIRD LOOP ============", module);
                                    //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                                    //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                                    //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                                    //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                                    break;
                                }
                            }
                        }   //final loop priceBreakShippingSmtList -- END
                    }
                    
                }   //weight is less than 2000 -- END
                //Debug.logError("===========Final ============", module);
                //Debug.logError("smtShippingMethodType : " + smtShippingMethodType, module);
                //Debug.logError("shippingCostSmt : " + shippingCostSmt, module);
                //Debug.logError("smtPriceDb : " + smtPriceDb, module);
                //Debug.logError("smtPercentageDb : " + smtPercentageDb, module);
                
                jxl.write.Number smtPrice = new jxl.write.Number(colSmtPrice, row, smtPriceDb, priceFormat);
                sheet.addCell (smtPrice);
                jxl.write.Number smtPercentage = new jxl.write.Number(colSmtPercentage, row, smtPercentageDb, percentIntFormat);
                sheet.addCell (smtPercentage);
                Label smtShippingMethod = new Label(colSmtShippingMethod, row, smtShippingMethodType);
                sheet.addCell(smtShippingMethod);
                //get SMT -- END
                
                //get WISH -- START
                double shippingCostWish = 0.0;
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostWish = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                }   //if weight is more than 2000 - ship by EMS - END
                else { //weight is less than 2000 -- START
                    GenericValue wishShippingGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST", "shippingMethodType", "REGISTERED"), false);
                    double discountRateWish = Double.valueOf(wishShippingGV.getLong("discountRate"));
                    if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                        discountRateWish = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", "REGISTERED"), false).getLong("discountRate"));
                    }   //if shipped by Yanwen -- END
                    double pricePerGramWish = wishShippingGV.getDouble("pricePerGram");
                    double additionalShipCostWish = 0.0;
                    if (UtilValidate.isNotEmpty(wishShippingGV.getDouble("additionalCost"))) {
                        additionalShipCostWish = wishShippingGV.getDouble("additionalCost");
                    }
                    shippingCostWish = (weightDb * pricePerGramWish + additionalShipCostWish) * (1.0 - (discountRateWish/100.0));
                } //weight is less than 2000 -- END
                
                
                double wishFeePct = getFeePct(delegator, "wishFeeBreak", 0);
                GenericValue marketplaceProfitWish = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "WISH"), null, false));
                String profitPercentWishStr = marketplaceProfitWish.getString("value");
                double wishPercentageDb = Double.parseDouble(profitPercentWishStr) / 100.0;
                double wishPriceDb = 1.0 / usdToRmb * ((shippingCostWish + productCost + packagingCost) / (1.0 - (wishFeePct/100.0) - wishPercentageDb));
                GenericValue productShippingRatioGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "productShippingRatio", "name", "WISH"), null, false));
                String productShippingRatioWish = productShippingRatioGV.getString("value");
                double wishFinalProductPrice = Math.ceil(wishPriceDb * Double.parseDouble(productShippingRatioWish) / 100.0);
                double wishFinalShippingPrice = Math.ceil(wishPriceDb * (1.0 - (Double.parseDouble(productShippingRatioWish) / 100.0)));
                double wishFinalPrice = wishFinalProductPrice + wishFinalShippingPrice;
                double wishProfitPercentage = ((wishFinalPrice * usdToRmb * (1.0 - (wishFeePct / 100.0))) - shippingCostWish - productCost - packagingCost) / (wishFinalPrice * usdToRmb);
                
                GenericValue overPriceCorrection = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "overPriceCorrection", "name", "WISH"), null, false));
                double overPricePct = (overPriceCorrection.getDouble("priceBreakHi")) / 100.0;
                double overPriceLoPct = (overPriceCorrection.getDouble("priceBreakLo")) / 100.0;
                double overPriceDeduction = Double.parseDouble(overPriceCorrection.getString("value"));
                if (wishProfitPercentage > overPricePct) {
                    wishFinalProductPrice = wishFinalProductPrice - overPriceDeduction;
                    wishProfitPercentage = (((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb * (1.0 - (wishFeePct / 100.0))) - shippingCostWish - productCost - packagingCost) / ((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb);
                    if (wishProfitPercentage < overPriceLoPct) {
                        wishFinalProductPrice = wishFinalProductPrice + overPriceDeduction;
                        wishProfitPercentage = (((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb * (1.0 - (wishFeePct / 100.0))) - shippingCostWish - productCost - packagingCost) / ((wishFinalProductPrice + wishFinalShippingPrice) * usdToRmb);
                    }
                }
                
                jxl.write.Number wishProductPrice = new jxl.write.Number(colWishProductPrice, row, wishFinalProductPrice, priceFormat);
                sheet.addCell (wishProductPrice);
                jxl.write.Number wishShippingPrice = new jxl.write.Number(colWishShippingPrice, row, wishFinalShippingPrice, priceFormat);
                sheet.addCell (wishShippingPrice);
                jxl.write.Number wishPercentage = new jxl.write.Number(colWishPercentage, row, wishProfitPercentage, percentIntFormat);
                sheet.addCell (wishPercentage);
                //get WISH -- END
                
                //get AMAZON -- START
                double shippingCostAma = 0.0;
                double amaPriceDb = 0.0;
                double amaPercentageDb = 0.0;
                double amaFeePct = getFeePct(delegator, "amaFeeBreak", 0);
                double amaPriceBreakRangeDb = 0.0;
                String amaShippingMethodTypeFinal = null;
                GenericValue marketplaceProfitAma = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "marketplaceProfit", "name", "AMA"), null, false));
                String profitPercentAmaStr = marketplaceProfitAma.getString("value");
                if (weightDb > 2000) {  //if weight is more than 2000 - ship by EMS - START
                    shippingCostAma = (150.0 + (Math.ceil(weightDb/500.0) - 1.0) * 45.0);
                    amaPercentageDb = Double.parseDouble(profitPercentAmaStr) / 100.0;
                    amaPriceDb = 1.0 / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                    
                }   //if weight is more than 2000 - ship by EMS - END
                else { //weight is less than 2000 -- START
                    
                    List<GenericValue> amaShippingList = delegator.findByAnd("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST"), null, false);
                    for (GenericValue amaShippingGV : amaShippingList) {    //loop amaShippingList -- START
                        String amaShippingMethodType = amaShippingGV.getString("shippingMethodType");
                        double discountRateAma = Double.valueOf(amaShippingGV.getLong("discountRate"));
                        double pricePerGramAma = amaShippingGV.getDouble("pricePerGram");
                        double additionalShipCostAma = 0.0;
                        if (UtilValidate.isNotEmpty(amaShippingGV.getDouble("additionalCost"))) {
                            additionalShipCostAma = amaShippingGV.getDouble("additionalCost");
                        }
                        if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                            discountRateAma = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", amaShippingMethodType), false).getLong("discountRate"));
                        }   //if shipped by Yanwen -- END
                        shippingCostAma = (weightDb * pricePerGramAma + additionalShipCostAma) * (1.0 - (discountRateAma/100.0));
                        amaPercentageDb = Double.parseDouble(profitPercentAmaStr) / 100.0;
                        amaPriceDb = 1.0 / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                        
                        GenericValue priceBreakShippingAma = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "AMA", "value", amaShippingMethodType), null, false));
                        BigDecimal amaPriceBreakLoBD = priceBreakShippingAma.getBigDecimal("priceBreakLo");
                        BigDecimal amaPriceBreakHiBD = priceBreakShippingAma.getBigDecimal("priceBreakHi");
                        String priceBreakShippingAmaValue = priceBreakShippingAma.getString("value");
                        if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                            amaPriceDb = (amaPriceDb * ( 1 - amaFeePct / 100.0)) + 1;
                        }
                        
                        if (((amaPriceDb * usdToRmb) >= amaPriceBreakLoBD.doubleValue()) && ((amaPriceDb * usdToRmb) < amaPriceBreakHiBD.doubleValue())) { //if -- START
                            if (priceBreakShippingAmaValue.equals(amaShippingMethodType)) {
                                amaShippingMethodTypeFinal = amaShippingMethodType;
                                amaPriceBreakRangeDb = amaPriceBreakLoBD.doubleValue();
                                break;
                            }
                        }   //if  -- END
                    }   //loop amaShippingList -- END
                }   //weight is less than 2000 -- END
                
                //recalculate the amaPercentageDb instead of using a fixed value from database
                amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                
                //if fvf is lower than 1USD, recalculate the amaPercentageDb using fvf 1USD
                if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                }
                
                //if amaPercentageDb is lower than the marketplaceprofit sets in database, recalculate the amaPriceDb so that it can reach the marketplaceprofit% from database and then recalculate the profit%
                if (amaPercentageDb < (Double.parseDouble(profitPercentAmaStr) / 100.0)) {
                    amaPriceDb = (1 - amaPercentageDb) / (1 - Double.parseDouble(profitPercentAmaStr) / 100.0) * amaPriceDb;
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    
                    //always check if the fee is below 1USD or not
                    if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                        amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    }
                }
                
                GenericValue amaPriceBreakRangeGV = EntityUtil.getFirst(delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakRange", "name", "AMA"), null, false));
                String amaPriceBreakRangeStr = amaPriceBreakRangeGV.getString("value");
                if (amaPriceDb <= (amaPriceBreakRangeDb + Double.parseDouble(amaPriceBreakRangeStr))) {   //if price is within price break range -- START
                    List<GenericValue> priceBreakShippingAmaList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "AMA"), null, false);
                    for (GenericValue priceBreakRangeAma : priceBreakShippingAmaList) { //loop priceBreakShippingAmaList -- START
                        BigDecimal amaPriceBreakLoBD = priceBreakRangeAma.getBigDecimal("priceBreakLo");
                        BigDecimal amaPriceBreakHiBD = priceBreakRangeAma.getBigDecimal("priceBreakHi");
                        if (((amaPriceBreakRangeDb - 0.1) >= amaPriceBreakLoBD.doubleValue()) && ((amaPriceBreakRangeDb - 0.1) < amaPriceBreakHiBD.doubleValue())) { //if -- START
                            String finalShippingMethodAma = priceBreakRangeAma.getString("value");
                            GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST", "shippingMethodType", finalShippingMethodAma), false);
                            double discountRateAma = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                            if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                                discountRateAma = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodAma), false).getLong("discountRate"));
                            }   //if shipped by Yanwen -- END
                            double pricePerGramAma = finalShippingCostGV.getDouble("pricePerGram");
                            double additionalShipCostAma = 0.0;
                            if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                                additionalShipCostAma = finalShippingCostGV.getDouble("additionalCost");
                            }
                            shippingCostAma = (weightDb * pricePerGramAma + additionalShipCostAma) * (1.0 - (discountRateAma / 100.0));
                            amaPriceDb = 1.0 / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                            amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCostAma - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                            break;
                        }   //if  -- END
                    }   //loop priceBreakShippingAmaList -- END
                }   //if price is within price break range -- END
                
                
                //this loop is to change the price which falls down above 30 USD, but still ship by standard, so change it back to registered, and then change the price again to still get the 15% profit
                List<GenericValue> amaFinalPriceBreakShippingList = delegator.findByAnd("ProductMasterVariable", UtilMisc.toMap("type", "priceBreakShipping", "name", "AMA"), null, false);
                for (GenericValue amaFinalPriceBreakShipping : amaFinalPriceBreakShippingList) { //final loop amaFinalPriceBreakShippingList -- START
                    BigDecimal finalPriceBreakLoBD = amaFinalPriceBreakShipping.getBigDecimal("priceBreakLo");
                    BigDecimal finalPriceBreakHiBD = amaFinalPriceBreakShipping.getBigDecimal("priceBreakHi");
                    if ((amaPriceDb >= finalPriceBreakLoBD.doubleValue()) && (amaPriceDb < finalPriceBreakHiBD.doubleValue())) {
                        String finalShippingMethodAma = amaFinalPriceBreakShipping.getString("value");
                        GenericValue finalShippingCostGV = delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "CHINAPOST", "shippingMethodType", finalShippingMethodAma), false);
                        double discountRateAma = Double.valueOf(finalShippingCostGV.getLong("discountRate"));
                        if (carrierId.equals("YANWEN")) {   //if shipped by Yanwen -- START
                            discountRateAma = Double.valueOf(delegator.findOne("GudaoShippingCost", UtilMisc.toMap("carrierId", "YANWEN", "shippingMethodType", finalShippingMethodAma), false).getLong("discountRate"));
                        }   //if shipped by Yanwen -- END
                        double pricePerGramAma = finalShippingCostGV.getDouble("pricePerGram");
                        double additionalShipCostAma = 0.0;
                        if (UtilValidate.isNotEmpty(finalShippingCostGV.getDouble("additionalCost"))) {
                            additionalShipCostAma = finalShippingCostGV.getDouble("additionalCost");
                        }
                        shippingCostAma = (weightDb * pricePerGramAma + additionalShipCostAma) * (1.0 - (discountRateAma/100.0));
                        amaPercentageDb = Double.parseDouble(profitPercentAmaStr) / 100.0;
                        amaPriceDb = usdToUsd / usdToRmb * ((shippingCostAma + productCost + packagingCost) / (1.0 - (amaFeePct/100.0) - amaPercentageDb));
                        amaShippingMethodTypeFinal = finalShippingMethodAma;
                        break;
                    }
                }   //final loop amaFinalPriceBreakShippingList -- END
                
                //if fvf is lower than 1USD, recalculate the amaPercentageDb using fvf 1USD
                if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                }
                
                //if amaPercentageDb is lower than the marketplaceprofit sets in database, recalculate the amaPriceDb so that it can reach the marketplaceprofit% from database and then recalculate the profit%
                if (amaPercentageDb < (Double.parseDouble(profitPercentAmaStr) / 100.0)) {
                    amaPriceDb = (1 - amaPercentageDb) / (1 - Double.parseDouble(profitPercentAmaStr) / 100.0) * amaPriceDb;
                    amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb * (1.0 - (amaFeePct / 100.0))) - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    
                    //always check if the fee is below 1USD or not
                    if (amaFeePct / 100.0 * amaPriceDb <= 1) {
                        amaPercentageDb = ((amaPriceDb / usdToUsd * usdToRmb ) - usdToRmb - shippingCost - productCost - packagingCost) / (amaPriceDb / usdToUsd * usdToRmb);
                    }
                }
                
                jxl.write.Number amaPrice = new jxl.write.Number(colAmaPrice, row, amaPriceDb, priceFormat);
                sheet.addCell (amaPrice);
                jxl.write.Number amaPercentage = new jxl.write.Number(colAmaPercentage, row, amaPercentageDb, percentIntFormat);
                sheet.addCell (amaPercentage);
                //get AMAZON -- END
                
                //CALCULATION PART -- END ---------------------------
                
                //Get Product Listing Status -- START
                String ebayUSListing = null;
                String ebayUKListing = null;
                String ebayAUListing = null;
                String ebayCAListing = null;
                String ebayDEListing = null;
                String ebayFRListing = null;
                String ebayESListing = null;
                String smtListing = null;
                String amaListing = null;
                String wishListing = null;
                String ebayPicture = null;
                String amazonPicture = null;
                List<GenericValue> productListingStatusList = delegator.findByAnd("ProductListingStatus", UtilMisc.toMap("productId", productId), null, false);
                if (UtilValidate.isNotEmpty(productListingStatusList)) {    // if productListingStatusList is not null -- START
                    for (GenericValue productListingStatus : productListingStatusList) {    //loop productListingStatusList -- START
                        String listingStatusTypeId = productListingStatus.getString("listingStatusTypeId");
                        if (listingStatusTypeId.equals("EBAY_US_LISTING")) {
                            ebayUSListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_UK_LISTING")) {
                            ebayUKListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_AU_LISTING")) {
                            ebayAUListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_CA_LISTING")) {
                            ebayCAListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_DE_LISTING")) {
                            ebayDEListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_FR_LISTING")) {
                            ebayFRListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_ES_LISTING")) {
                            ebayESListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("SMT_LISTING")) {
                            smtListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("AMA_LISTING")) {
                            amaListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("WISH_LISTING")) {
                            wishListing = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("EBAY_PICTURE")) {
                            ebayPicture = productListingStatus.getString("value");
                        } else if (listingStatusTypeId.equals("AMAZON_PICTURE")) {
                            amazonPicture = productListingStatus.getString("value");
                        }
                    }   //loop productListingStatusList -- END
                }   // if productListingStatusList is not null -- END
                
                Label valueEbayUSListing = new Label(colEbayUSListing, row, ebayUSListing);
                Label valueEbayUKListing = new Label(colEbayUKListing, row, ebayUKListing);
                Label valueEbayAUListing = new Label(colEbayAUListing, row, ebayAUListing);
                Label valueEbayCAListing = new Label(colEbayCAListing, row, ebayCAListing);
                Label valueEbayDEListing = new Label(colEbayDEListing, row, ebayDEListing);
                Label valueEbayFRListing = new Label(colEbayFRListing, row, ebayFRListing);
                Label valueEbayESListing = new Label(colEbayESListing, row, ebayESListing);
                Label valueSmtListing = new Label(colSmtListing, row, smtListing);
                Label valueAmaListing = new Label(colAmaListing, row, amaListing);
                Label valueWishListing = new Label(colWishListing, row, wishListing);
                Label valueEbayPicture = new Label(colEbayPicture, row, ebayPicture);
                Label valueAmazonPicture = new Label(colAmazonPicture, row, amazonPicture);
                sheet.addCell(valueEbayUSListing);
                sheet.addCell(valueEbayUKListing);
                sheet.addCell(valueEbayAUListing);
                sheet.addCell(valueEbayCAListing);
                sheet.addCell(valueEbayDEListing);
                sheet.addCell(valueEbayFRListing);
                sheet.addCell(valueEbayESListing);
                sheet.addCell(valueSmtListing);
                sheet.addCell(valueAmaListing);
                sheet.addCell(valueWishListing);
                sheet.addCell(valueEbayPicture);
                sheet.addCell(valueAmazonPicture);

                //Get Product Listing Status -- END
                
                
                
                row++;
            }   //loop productMasterList -- END
            
            workbook.write();
            workbook.close();
            Debug.logError("Finished generating PM", module);
        }   //end try block
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return ServiceUtil.returnSuccess();
    }   //gudaoGenerateProductMaster
    
    public static Map<String, Object> uploadReportXlsFile(DispatchContext dctx, Map<String, ? extends Object> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
        String uploadFileName = (String) context.get("_uploadedFile_fileName");
        uploadFileName = uploadFileName.replaceAll(" ","");
        String importFormId = (String) context.get("importFormId");
        
        if (UtilValidate.isNotEmpty(uploadFileName) && UtilValidate.isNotEmpty(imageData)) {
            try {   //main try -- START
                String fileServerPath = "hot-deploy/gudao/webapp/gudao/bulkModule/upload/report";
                File rootTargetDir = new File(fileServerPath);
                if (!rootTargetDir.exists()) {
                    boolean created = rootTargetDir.mkdirs();
                    if (!created) {
                        String errMsg = "Not create target directory";
                        Debug.logFatal(errMsg, module);
                        return ServiceUtil.returnError(errMsg);
                    }
                }
                String fileName = importFormId + "-" + new SimpleDateFormat("MMdd-HHmm").format(Calendar.getInstance().getTime()) + "-" + userLogin.getString("userLoginId") + "-" + uploadFileName;
                
                String filePath = fileServerPath + "/" + fileName;
                File file = new File(filePath);
                try {
                    RandomAccessFile out = new RandomAccessFile(file, "rw");
                    out.write(imageData.array());
                    out.close();
                } catch (FileNotFoundException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError("");
                } catch (IOException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError("");
                }
                if (file.exists()) {    //if file exist -- START
                    Map result = readReportXls(dispatcher, delegator, userLogin, filePath, uploadFileName);
                }   //if file exist -- END
            } catch (Exception e) {
                return ServiceUtil.returnError(e.getMessage());
            }   //main try -- END
        }
        return ServiceUtil.returnSuccess();
    }   //uploadReportXlsFile
    
    private static Map<String, Object> readReportXls(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String filePath, String originalFilename) {   //readReportXls
        Map<String, Object> result = ServiceUtil.returnSuccess();
        List<String> errorList = FastList.newInstance();
        try {   //main try -- START
            POIFSFileSystem fs = null;
            HSSFWorkbook wb = null;
            
            try {
                fs = new POIFSFileSystem(new FileInputStream(new File(filePath)));
                
                //filePath.endsWith(".xlsx")
                wb = new HSSFWorkbook(fs);
            } catch (IOException e) {
                Debug.logError("Unable to read workbook from file " + filePath, module);
                return ServiceUtil.returnError("Unable to read workbook from file " + filePath);
            }
            
            HSSFSheet sheet = wb.getSheetAt(0);
            int sheetLastRowNumber = sheet.getLastRowNum();
            HSSFRow firstRow = sheet.getRow(0);
            int minColIx = firstRow.getFirstCellNum();
            int maxColIx = firstRow.getLastCellNum();
            int colOrderId = 0;
            int colOrderDate = 0;
            int colCurrencyRate = 0;
            int colPlatform = 0;
            int colSalesRmb = 0;
            int colSalesOriginal = 0;
            int colShippingRmb = 0;
            int colShippingOriginal = 0;
            int colTotalSales = 0;
            int colProductCost = 0;
            int colShippingCost = 0;
            int colPaypalRmb = 0;
            int colPaypalOriginal = 0;
            int colEbayFeeRmb = 0;
            int colEbayFeeOriginal = 0;
            int colPackaging = 0;
            int colTotalCost = 0;
            int colItemQty = 0;
            int colOrderQty = 0;
            int colProfit = 0;
            int colProfitPct = 0;
            int colOwnerGroup = 0;
            int colDuplicate = 0;
            
            for(int colHead = minColIx; colHead < maxColIx; colHead++) {  //get column header -- START
                HSSFCell cellHead = firstRow.getCell(colHead);
                if(cellHead == null) {
                    continue;
                }
                cellHead.setCellType(HSSFCell.CELL_TYPE_STRING);
                String colHeader = cellHead.getRichStringCellValue().toString().toUpperCase().trim();
                //read column header data -- START
                if ("订单编号".equals(colHeader)) {
                    colOrderId = colHead;
                } else if ("订单日期".equals(colHeader)) {
                    colOrderDate = colHead;
                } else if ("汇率".equals(colHeader)) {
                    colCurrencyRate = colHead;
                } else if ("平台".equals(colHeader)) {
                    colPlatform = colHead;
                } else if ("交易额".equals(colHeader)) {
                    colSalesRmb = colHead;
                } else if ("原金额".equals(colHeader)) {
                    colSalesOriginal = colHead;
                } else if ("运费".equals(colHeader)) {
                    colShippingRmb = colHead;
                } else if ("原运费".equals(colHeader)) {
                    colShippingOriginal = colHead;
                } else if ("小计".equals(colHeader)) {
                    colTotalSales = colHead;
                } else if ("成本".equals(colHeader)) {
                    colProductCost = colHead;
                } else if ("运费".equals(colHeader)) {
                    colShippingCost = colHead;
                } else if ("paypal".equals(colHeader)) {
                    colPaypalRmb = colHead;
                } else if ("paypal".equals(colHeader)) {
                    colPaypalOriginal = colHead;
                } else if ("ebay费".equals(colHeader)) {
                    colEbayFeeRmb = colHead;
                } else if ("ebay费".equals(colHeader)) {
                    colEbayFeeOriginal = colHead;
                } else if ("包材".equals(colHeader)) {
                    colPackaging = colHead;
                } else if ("小计".equals(colHeader)) {
                    colTotalCost = colHead;
                } else if ("销量".equals(colHeader)) {
                    colItemQty = colHead;
                } else if ("订单量".equals(colHeader)) {
                    colOrderQty = colHead;
                } else if ("毛利".equals(colHeader)) {
                    colProfit = colHead;
                } else if ("毛利率%".equals(colHeader)) {
                    colProfitPct = colHead;
                } else if ("事业部".equals(colHeader)) {
                    colOwnerGroup = colHead;
                } else if ("DUPLICATE".equals(colHeader)) {
                    colDuplicate = colHead;
                }   //read column header data -- END
                
            }   //get column header -- END
            
            for (int j = 1; j <= sheetLastRowNumber; j++) { //loop rows -- START
                HSSFRow row = sheet.getRow(j);
                if (row != null) {  //if row is not empty -- START
                    Map<String, Object> importReportCtx = FastMap.newInstance();
                    boolean uploadReportData = true;
                    
                    for(int colIx = minColIx; colIx < maxColIx; colIx++) {    //loop cell in a row -- START
                        HSSFCell cell = row.getCell(colIx);
                        if(cell == null) {
                            continue;
                        }
                        
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        String dataString = cell.getRichStringCellValue().toString().trim();
                        
                        if (colIx == (colOrderId)) {
                            importReportCtx.put("orderId", dataString);
                        } else if (colIx == (colOrderDate)) {
                            importReportCtx.put("orderDate", new java.sql.Date(HSSFDateUtil.getJavaDate(Double.parseDouble(dataString)).getTime()));
                        } else if (colIx == (colCurrencyRate)) {
                            importReportCtx.put("currencyRate", Double.valueOf(dataString));
                        } else if (colIx == (colPlatform)) {
                            importReportCtx.put("platform", (dataString));
                        } else if (colIx == (colSalesRmb)) {
                            importReportCtx.put("salesRmb", Double.valueOf(dataString));
                        } else if (colIx == (colSalesOriginal)) {
                            importReportCtx.put("salesOriginal", Double.valueOf(dataString));
                        } else if (colIx == (colShippingRmb)) {
                            importReportCtx.put("shippingRmb", Double.valueOf(dataString));
                        } else if (colIx == (colShippingOriginal)) {
                            importReportCtx.put("shippingOriginal", Double.valueOf(dataString));
                        } else if (colIx == (colTotalSales)) {
                            importReportCtx.put("totalSales", Double.valueOf(dataString));
                        } else if (colIx == (colProductCost)) {
                            importReportCtx.put("productCost", Double.valueOf(dataString));
                        } else if (colIx == (colShippingCost)) {
                            importReportCtx.put("shippingCost", Double.valueOf(dataString));
                        } else if (colIx == (colPaypalRmb)) {
                            importReportCtx.put("paypalRmb", Double.valueOf(dataString));
                        } else if (colIx == (colPaypalOriginal)) {
                            importReportCtx.put("paypalOriginal", Double.valueOf(dataString));
                        } else if (colIx == (colEbayFeeRmb)) {
                            importReportCtx.put("ebayFeeRmb", Double.valueOf(dataString));
                        } else if (colIx == (colEbayFeeOriginal)) {
                            importReportCtx.put("ebayFeeOriginal", Double.valueOf(dataString));
                        } else if (colIx == (colPackaging)) {
                            importReportCtx.put("packaging", Double.valueOf(dataString));
                        } else if (colIx == (colTotalCost)) {
                            importReportCtx.put("totalCost", Double.valueOf(dataString));
                        } else if (colIx == (colItemQty)) {
                            importReportCtx.put("itemQty", Double.valueOf(dataString));
                        } else if (colIx == (colOrderQty)) {
                            importReportCtx.put("orderQty", Double.valueOf(dataString));
                        } else if (colIx == (colProfit)) {
                            importReportCtx.put("profit", Double.valueOf(dataString));
                        } else if (colIx == (colProfitPct)) {
                            importReportCtx.put("profitPct", Double.valueOf(dataString));
                        } else if (colIx == (colOwnerGroup)) {
                            importReportCtx.put("ownerGroup", (dataString));
                        } else if (colIx == (colDuplicate)) {
                            importReportCtx.put("duplicate", Double.valueOf(dataString));
                        }//read record -- END
                    }   //loop cell in a row -- END
                    
                    importReportCtx.put("normalizeTotalSales", Double.valueOf(importReportCtx.get("totalSales") + "") / Double.valueOf(importReportCtx.get("duplicate") + ""));
                    importReportCtx.put("normalizeProductCost", Double.valueOf(importReportCtx.get("productCost") + "") / Double.valueOf(importReportCtx.get("duplicate") + ""));
                    importReportCtx.put("normalizeShippingCost", Double.valueOf(importReportCtx.get("shippingCost") + "") / Double.valueOf(importReportCtx.get("duplicate") + ""));
                    importReportCtx.put("normalizePaypal", Double.valueOf(importReportCtx.get("paypalRmb") + "") / Double.valueOf(importReportCtx.get("duplicate") + ""));
                    importReportCtx.put("rormalizeEbayFee", Double.valueOf(importReportCtx.get("ebayFeeRmb") + "") / Double.valueOf(importReportCtx.get("duplicate") + ""));
                    importReportCtx.put("normalizeTotalFee", Double.valueOf(importReportCtx.get("normalizePaypal") + "") + Double.valueOf(importReportCtx.get("normalizeEbayFee") + ""));
                    importReportCtx.put("normalizeProfit", Double.valueOf(importReportCtx.get("normalizeTotalSales") + "") - Double.valueOf(importReportCtx.get("normalizeProductCost") + "") - Double.valueOf(importReportCtx.get("normalizeShippingCost") + "") - Double.valueOf(importReportCtx.get("normalizePaypal") + "") - Double.valueOf(importReportCtx.get("normalizeEbayFee") + ""));
                    
                    if (uploadReportData) {    //if row indeed has data -- START
                        try {   //try -- second -- START
                            String newReportUploadId = delegator.getNextSeqId("ReportUpload");
                            importReportCtx.put("reportUploadId", newReportUploadId);
                            importReportCtx.put("fileName", filePath);
                            importReportCtx.put("fileLineNumber", new BigDecimal(j));
                            importReportCtx.put("importedStatus", "N");
                            importReportCtx.put("userLogin", userLogin);
                            
                            result = dispatcher.runSync("createReportImport", importReportCtx);
                            if (ServiceUtil.isError(result)) {  //if result gives error -- START
                                errorList.add(ServiceUtil.getErrorMessage(result));
                            }   //if result gives error -- END
                        }   //try -- second -- END
                        catch (Exception e) {
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    }   //if row indeed has data -- END
                    
                }   //if row is not empty -- END
            }   //loop rows -- END
        } catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }   //main try -- END
        return ServiceUtil.returnSuccess();
    }   //readReportXls
    
    public static Map<String, Object> importMotherVersion (DispatchContext dctx, Map<String, ? extends Object> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
        String uploadFileName = (String) context.get("_uploadedFile_fileName");
        uploadFileName = uploadFileName.replaceAll(" ","");
        String marketplace = (String) context.get("marketplace");
        String motherVersionType = (String) context.get("motherVersionType");
        String deleteData = (String) context.get("deleteData");
        
        if (UtilValidate.isNotEmpty(uploadFileName) && UtilValidate.isNotEmpty(imageData)) {
            try {   //main try -- START
                String fileServerPath = "hot-deploy/gudao/webapp/gudao/bulkModule/upload/motherVersion";
                File rootTargetDir = new File(fileServerPath);
                if (!rootTargetDir.exists()) {
                    boolean created = rootTargetDir.mkdirs();
                    if (!created) {
                        String errMsg = "Failed creating target directory";
                        Debug.logFatal(errMsg, module);
                        return ServiceUtil.returnError(errMsg);
                    }
                }
                String fileName = new SimpleDateFormat("MMdd-HHmm").format(Calendar.getInstance().getTime()) + "-" + uploadFileName;
                
                String filePath = fileServerPath + "/" + fileName;
                File file = new File(filePath);
                try {
                    RandomAccessFile out = new RandomAccessFile(file, "rw");
                    out.write(imageData.array());
                    out.close();
                } catch (FileNotFoundException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError("");
                } catch (IOException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError("");
                }
                if (file.exists()) {    //if file exist -- START
                    if (deleteData.equals("Y")) {
                        delegator.removeByAnd("MotherVersion", UtilMisc.toMap("motherVersionType", motherVersionType, "marketplace", marketplace));
                    }
                    
                    List<String> errorList = FastList.newInstance();
                    try {   //main2 try -- START
                        POIFSFileSystem fs = null;
                        HSSFWorkbook wb = null;
                        
                        try {
                            fs = new POIFSFileSystem(new FileInputStream(new File(filePath)));
                            
                            //filePath.endsWith(".xlsx")
                            wb = new HSSFWorkbook(fs);
                        } catch (IOException e) {
                            Debug.logError("Unable to read workbook from file " + filePath, module);
                            return ServiceUtil.returnError("Unable to read workbook from file " + filePath);
                        }
                        
                        HSSFSheet sheet = wb.getSheetAt(0);
                        int sheetLastRowNumber = sheet.getLastRowNum();
                        HSSFRow firstRow = sheet.getRow(0);
                        int minColIx = firstRow.getFirstCellNum();
                        int maxColIx = firstRow.getLastCellNum();
                        
                        int colId = 0;
                        int colYunqudaoListingId = 0;
                        int colYunqudaoListingIdVar = 0;
                        int colYunqudaoFolderId = 0;
                        int colEbayAccountName = 0;
                        int colSku = 0;
                        int colSiteId = 0;
                        int colFormat = 0;
                        int colTitle = 0;
                        int colSubtitle = 0;
                        int colTags = 0;
                        int colRelationship = 0;
                        int colRelationshipDetails = 0;
                        int colQuantity = 0;
                        int colQtyRestrictionPerBuyerMax = 0;
                        int colCurrency = 0;
                        int colStartPrice = 0;
                        int colReservePrice = 0;
                        int colBuyItNowPrice = 0;
                        int colDuration = 0;
                        int colPrivateListing = 0;
                        int colBestOfferAutoAcceptPrice = 0;
                        int colMinBestOfferPrice = 0;
                        int colOutOfStockControl = 0;
                        int colPicUrl = 0;
                        int colYunqudaoImageLayout = 0;
                        int colUploadImageEps = 0;
                        int colShowImageInDesc = 0;
                        int colItemSpecific = 0;
                        int colCondition = 0;
                        int colConditionDescription = 0;
                        int colCategory = 0;
                        int colCategory2 = 0;
                        int colStoreCategory = 0;
                        int colStoreCategory2 = 0;
                        int colUpc = 0;
                        int colEan = 0;
                        int colIsbn = 0;
                        int colBrandMpnBrand = 0;
                        int colBrandMpnMpn = 0;
                        int colDescriptionFile = 0;
                        int colDescription = 0;
                        int colDescription1 = 0;
                        int colDescription2 = 0;
                        int colDescription3 = 0;
                        int colYunqudaoTemplateId = 0;
                        int colYunqudaoSellerDetailId = 0;
                        int colYunqudaoCounter = 0;
                        int colListingEnhancement = 0;
                        int colCountry = 0;
                        int colLocation = 0;
                        int colDispatchTimeMax = 0;
                        int colGetItFast = 0;
                        int colShippingService1Option = 0;
                        int colShippingService1Cost = 0;
                        int colShippingService1AddCost = 0;
                        int colShippingService2Option = 0;
                        int colShippingService2Cost = 0;
                        int colShippingService2AddCost = 0;
                        int colShippingService3Option = 0;
                        int colShippingService3Cost = 0;
                        int colShippingService3AddCost = 0;
                        int colShippingService4Option = 0;
                        int colShippingService4Cost = 0;
                        int colShippingService4AddCost = 0;
                        int colShippingRateTable = 0;
                        int colIntShippingService1Option = 0;
                        int colIntShippingService1Cost = 0;
                        int colIntShippingService1AddCost = 0;
                        int colIntShippingService1Locations = 0;
                        int colIntShippingService2Option = 0;
                        int colIntShippingService2Cost = 0;
                        int colIntShippingService2AddCost = 0;
                        int colIntShippingService2Locations = 0;
                        int colIntShippingService3Option = 0;
                        int colIntShippingService3Cost = 0;
                        int colIntShippingService3AddCost = 0;
                        int colIntShippingService3Locations = 0;
                        int colIntShippingService4Option = 0;
                        int colIntShippingService4Cost = 0;
                        int colIntShippingService4AddCost = 0;
                        int colIntShippingService4Locations = 0;
                        int colIntShippingService5Option = 0;
                        int colIntShippingService5Cost = 0;
                        int colIntShippingService5AddCost = 0;
                        int colIntShippingService5Locations = 0;
                        int colIntShippingRateTable = 0;
                        int colExcludeShipToLocation = 0;
                        int colPayPalEmailAddress = 0;
                        int colImmediatePayRequired = 0;
                        int colPayMoneyXferAccInCheckout = 0;
                        int colPaymentInstructions = 0;
                        int colReturnsAcceptedOption = 0;
                        int colReturnsWithinOption = 0;
                        int colRefundOption = 0;
                        int colShippingCostPaidByOption = 0;
                        int colReturnPolicyDescription = 0;
                        int colExtendedHolidayReturns = 0;
                        int colBRLinkedPayPalAccount = 0;
                        int colBRMaxBuyerPolicyVioCount = 0;
                        int colBRMaxBuyerPolicyVioPeriod = 0;
                        int colBRMaxItemReqMaxItemCount = 0;
                        int colBRMaxItemReqMinFeedbackScore = 0;
                        int colBRMaxUnpaidStrikesInfoCount = 0;
                        int colBRMaxUnpaidStrikesInfoPeriod = 0;
                        int colBRMinFeedbackScore = 0;
                        int colBRShipToRegistrationCountry = 0;
                        int colYunqudaoAutoRelistId = 0;
                        int colYunqudaoAutoReplenishId = 0;
                        int colYunqudaoShowcaseId = 0;
                        
                        for (int colHead = minColIx; colHead < maxColIx; colHead++) {  //get column header -- START
                            HSSFCell cellHead = firstRow.getCell(colHead);
                            if(cellHead == null) {
                                continue;
                            }
                            cellHead.setCellType(HSSFCell.CELL_TYPE_STRING);
                            String colHeader = cellHead.getRichStringCellValue().toString().trim();
                            //read column header data -- START
                            if ("ID".equals(colHeader)) {
                                colId = colHead;
                                
                            } else if ("Yunqudao_ListingID".equals(colHeader)) {
                                colYunqudaoListingId = colHead;
                                
                            } else if ("Yunqudao_ListingIDVar".equals(colHeader)) {
                                colYunqudaoListingIdVar = colHead;
                                
                            } else if ("Yunqudao_FolderID".equals(colHeader)) {
                                colYunqudaoFolderId = colHead;
                                
                            } else if ("ebayAccountName".equals(colHeader)) {
                                colEbayAccountName = colHead;
                                
                            } else if ("SKU".equals(colHeader)) {
                                colSku = colHead;
                                
                            } else if ("SiteID".equals(colHeader)) {
                                colSiteId = colHead;
                                
                            } else if ("Format".equals(colHeader)) {
                                colFormat = colHead;
                                
                            } else if ("Title".equals(colHeader)) {
                                colTitle = colHead;
                                
                            } else if ("Subtitle".equals(colHeader)) {
                                colSubtitle = colHead;
                                
                            } else if ("Tags".equals(colHeader)) {
                                colTags = colHead;
                                
                            } else if ("Relationship".equals(colHeader)) {
                                colRelationship = colHead;
                                
                            } else if ("RelationshipDetails".equals(colHeader)) {
                                colRelationshipDetails = colHead;
                                
                            } else if ("Quantity".equals(colHeader)) {
                                colQuantity = colHead;
                                
                            } else if ("QuantityRestrictionPerBuyerMax".equals(colHeader)) {
                                colQtyRestrictionPerBuyerMax = colHead;
                                
                            } else if ("Currency".equals(colHeader)) {
                                colCurrency = colHead;
                                
                            } else if ("StartPrice".equals(colHeader)) {
                                colStartPrice = colHead;
                                
                            } else if ("ReservePrice".equals(colHeader)) {
                                colReservePrice = colHead;
                                
                            } else if ("BuyItNowPrice".equals(colHeader)) {
                                colBuyItNowPrice = colHead;
                                
                            } else if ("Duration".equals(colHeader)) {
                                colDuration = colHead;
                                
                            } else if ("PrivateListing".equals(colHeader)) {
                                colPrivateListing = colHead;
                                
                            } else if ("BestOfferAutoAcceptPrice".equals(colHeader)) {
                                colBestOfferAutoAcceptPrice = colHead;
                                
                            } else if ("MinimumBestOfferPrice".equals(colHeader)) {
                                colMinBestOfferPrice = colHead;
                                
                            } else if ("OutOfStockControl".equals(colHeader)) {
                                colOutOfStockControl = colHead;
                                
                            } else if ("PicURL".equals(colHeader)) {
                                colPicUrl = colHead;
                                
                            } else if ("Yunqudao_ImageLayout".equals(colHeader)) {
                                colYunqudaoImageLayout = colHead;
                                
                            } else if ("UploadImageEPS".equals(colHeader)) {
                                colUploadImageEps = colHead;
                                
                            } else if ("ShowImageInDesc".equals(colHeader)) {
                                colShowImageInDesc = colHead;
                                
                            } else if ("ItemSpecific".equals(colHeader)) {
                                colItemSpecific = colHead;
                                
                            } else if ("Condition".equals(colHeader)) {
                                colCondition = colHead;
                                
                            } else if ("ConditionDescription".equals(colHeader)) {
                                colConditionDescription = colHead;
                                
                            } else if ("Category".equals(colHeader)) {
                                colCategory = colHead;
                                
                            } else if ("Category2".equals(colHeader)) {
                                colCategory2 = colHead;
                                
                            } else if ("StoreCategory".equals(colHeader)) {
                                colStoreCategory = colHead;
                                
                            } else if ("StoreCategory2".equals(colHeader)) {
                                colStoreCategory2 = colHead;
                                
                            } else if ("UPC".equals(colHeader)) {
                                colUpc = colHead;
                                
                            } else if ("EAN".equals(colHeader)) {
                                colEan = colHead;
                                
                            } else if ("ISBN".equals(colHeader)) {
                                colIsbn = colHead;
                                
                            } else if ("BrandMPN_Brand".equals(colHeader)) {
                                colBrandMpnBrand = colHead;
                                
                            } else if ("BrandMPN_MPN".equals(colHeader)) {
                                colBrandMpnMpn = colHead;
                                
                            } else if ("Description File".equals(colHeader)) {
                                colDescriptionFile = colHead;
                                
                            } else if ("Description".equals(colHeader)) {
                                colDescription = colHead;
                                
                            } else if ("Description1".equals(colHeader)) {
                                colDescription1 = colHead;
                                
                            } else if ("Description2".equals(colHeader)) {
                                colDescription2 = colHead;
                                
                            } else if ("Description3".equals(colHeader)) {
                                colDescription3 = colHead;
                                
                            } else if ("Yunqudao_TemplateID".equals(colHeader)) {
                                colYunqudaoTemplateId = colHead;
                                
                            } else if ("Yunqudao_SellerDetailID".equals(colHeader)) {
                                colYunqudaoSellerDetailId = colHead;
                                
                            } else if ("Yunqudao_Counter".equals(colHeader)) {
                                colYunqudaoCounter = colHead;
                                
                            } else if ("ListingEnhancement".equals(colHeader)) {
                                colListingEnhancement = colHead;
                                
                            } else if ("Country".equals(colHeader)) {
                                colCountry = colHead;
                                
                            } else if ("Location".equals(colHeader)) {
                                colLocation = colHead;
                                
                            } else if ("DispatchTimeMax".equals(colHeader)) {
                                colDispatchTimeMax = colHead;
                                
                            } else if ("GetItFast".equals(colHeader)) {
                                colGetItFast = colHead;
                                
                            } else if ("ShippingService-1:Option".equals(colHeader)) {
                                colShippingService1Option = colHead;
                                
                            } else if ("ShippingService-1:Cost".equals(colHeader)) {
                                colShippingService1Cost = colHead;
                                
                            } else if ("ShippingService-1:AdditionalCost".equals(colHeader)) {
                                colShippingService1AddCost = colHead;
                                
                            } else if ("ShippingService-2:Option".equals(colHeader)) {
                                colShippingService2Option = colHead;
                                
                            } else if ("ShippingService-2:Cost".equals(colHeader)) {
                                colShippingService2Cost = colHead;
                                
                            } else if ("ShippingService-2:AdditionalCost".equals(colHeader)) {
                                colShippingService2AddCost = colHead;
                                
                            } else if ("ShippingService-3:Option".equals(colHeader)) {
                                colShippingService3Option = colHead;
                                
                            } else if ("ShippingService-3:Cost".equals(colHeader)) {
                                colShippingService3Cost = colHead;
                                
                            } else if ("ShippingService-3:AdditionalCost".equals(colHeader)) {
                                colShippingService3AddCost = colHead;
                                
                            } else if ("ShippingService-4:Option".equals(colHeader)) {
                                colShippingService4Option = colHead;
                                
                            } else if ("ShippingService-4:Cost".equals(colHeader)) {
                                colShippingService4Cost = colHead;
                                
                            } else if ("ShippingService-4:AdditionalCost".equals(colHeader)) {
                                colShippingService4AddCost = colHead;
                                
                            } else if ("ShippingRateTable".equals(colHeader)) {
                                colShippingRateTable = colHead;
                                
                            } else if ("IntShippingService-1:Option".equals(colHeader)) {
                                colIntShippingService1Option = colHead;
                                
                            } else if ("IntShippingService-1:Cost".equals(colHeader)) {
                                colIntShippingService1Cost = colHead;
                                
                            } else if ("IntShippingService-1:AdditionalCost".equals(colHeader)) {
                                colIntShippingService1AddCost = colHead;
                                
                            } else if ("IntShippingService-1:Locations".equals(colHeader)) {
                                colIntShippingService1Locations = colHead;
                                
                            } else if ("IntShippingService-2:Option".equals(colHeader)) {
                                colIntShippingService2Option = colHead;
                                
                            } else if ("IntShippingService-2:Cost".equals(colHeader)) {
                                colIntShippingService2Cost = colHead;
                                
                            } else if ("IntShippingService-2:AdditionalCost".equals(colHeader)) {
                                colIntShippingService2AddCost = colHead;
                                
                            } else if ("IntShippingService-2:Locations".equals(colHeader)) {
                                colIntShippingService2Locations = colHead;
                                
                            } else if ("IntShippingService-3:Option".equals(colHeader)) {
                                colIntShippingService3Option = colHead;
                                
                            } else if ("IntShippingService-3:Cost".equals(colHeader)) {
                                colIntShippingService3Cost = colHead;
                                
                            } else if ("IntShippingService-3:AdditionalCost".equals(colHeader)) {
                                colIntShippingService3AddCost = colHead;
                                
                            } else if ("IntShippingService-3:Locations".equals(colHeader)) {
                                colIntShippingService3Locations = colHead;
                                
                            } else if ("IntShippingService-4:Option".equals(colHeader)) {
                                colIntShippingService4Option = colHead;
                                
                            } else if ("IntShippingService-4:Cost".equals(colHeader)) {
                                colIntShippingService4Cost = colHead;
                                
                            } else if ("IntShippingService-4:AdditionalCost".equals(colHeader)) {
                                colIntShippingService4AddCost = colHead;
                                
                            } else if ("IntShippingService-4:Locations".equals(colHeader)) {
                                colIntShippingService4Locations = colHead;
                                
                            } else if ("IntShippingService-5:Option".equals(colHeader)) {
                                colIntShippingService5Option = colHead;
                                
                            } else if ("IntShippingService-5:Cost".equals(colHeader)) {
                                colIntShippingService5Cost = colHead;
                                
                            } else if ("IntShippingService-5:AdditionalCost".equals(colHeader)) {
                                colIntShippingService5AddCost = colHead;
                                
                            } else if ("IntShippingService-5:Locations".equals(colHeader)) {
                                colIntShippingService5Locations = colHead;
                                
                            } else if ("IntShippingRateTable".equals(colHeader)) {
                                colIntShippingRateTable = colHead;
                                
                            } else if ("ExcludeShipToLocation".equals(colHeader)) {
                                colExcludeShipToLocation = colHead;
                                
                            } else if ("PayPalEmailAddress".equals(colHeader)) {
                                colPayPalEmailAddress = colHead;
                                
                            } else if ("ImmediatePayRequired".equals(colHeader)) {
                                colImmediatePayRequired = colHead;
                                
                            } else if ("Payment_MoneyXferAcceptedInCheckout".equals(colHeader)) {
                                colPayMoneyXferAccInCheckout = colHead;
                                
                            } else if ("PaymentInstructions".equals(colHeader)) {
                                colPaymentInstructions = colHead;
                                
                            } else if ("ReturnsAcceptedOption".equals(colHeader)) {
                                colReturnsAcceptedOption = colHead;
                                
                            } else if ("ReturnsWithinOption".equals(colHeader)) {
                                colReturnsWithinOption = colHead;
                                
                            } else if ("RefundOption".equals(colHeader)) {
                                colRefundOption = colHead;
                                
                            } else if ("ShippingCostPaidByOption".equals(colHeader)) {
                                colShippingCostPaidByOption = colHead;
                                
                            } else if ("ReturnPolicyDescription".equals(colHeader)) {
                                colReturnPolicyDescription = colHead;
                                
                            } else if ("ExtendedHolidayReturns".equals(colHeader)) {
                                colExtendedHolidayReturns = colHead;
                                
                            } else if ("BR:LinkedPayPalAccount".equals(colHeader)) {
                                colBRLinkedPayPalAccount = colHead;
                                
                            } else if ("BR:MaximumBuyerPolicyViolations:Count".equals(colHeader)) {
                                colBRMaxBuyerPolicyVioCount = colHead;
                                
                            } else if ("BR:MaximumBuyerPolicyViolations:Period".equals(colHeader)) {
                                colBRMaxBuyerPolicyVioPeriod = colHead;
                                
                            } else if ("BR:MaximumItemRequirements:MaximumItemCount".equals(colHeader)) {
                                colBRMaxItemReqMaxItemCount = colHead;
                                
                            } else if ("BR:MaximumItemRequirements:MinimumFeedbackScore".equals(colHeader)) {
                                colBRMaxItemReqMinFeedbackScore = colHead;
                                
                            } else if ("BR:MaximumUnpaidItemStrikesInfo:Count".equals(colHeader)) {
                                colBRMaxUnpaidStrikesInfoCount = colHead;
                                
                            } else if ("BR:MaximumUnpaidItemStrikesInfo:Period".equals(colHeader)) {
                                colBRMaxUnpaidStrikesInfoPeriod = colHead;
                                
                            } else if ("BR:MinimumFeedbackScore".equals(colHeader)) {
                                colBRMinFeedbackScore = colHead;
                                
                            } else if ("BR:ShipToRegistrationCountry".equals(colHeader)) {
                                colBRShipToRegistrationCountry = colHead;
                                
                            } else if ("Yunqudao_AutoRelistID".equals(colHeader)) {
                                colYunqudaoAutoRelistId = colHead;
                                
                            } else if ("Yunqudao_AutoReplenishID".equals(colHeader)) {
                                colYunqudaoAutoReplenishId = colHead;
                                
                            } else if ("Yunqudao_ShowcaseID".equals(colHeader)) {
                                colYunqudaoShowcaseId = colHead;
                            }//read column header data -- END
                            
                        }   //get column header -- END

                        
                        for (int j = 1; j <= sheetLastRowNumber; j++) { //loop rows -- START
                            HSSFRow row = sheet.getRow(j);
                            if (row != null) {  //if row is not empty -- START
                                Map<String, Object> motherVersionImportCtx = FastMap.newInstance();
                                boolean doImport = true;
                                
                                for(int colIx = minColIx; colIx < maxColIx; colIx++) {    //loop cell in a row -- START
                                    HSSFCell cell = row.getCell(colIx);
                                    if(cell == null) {
                                        continue;
                                    }
                                    
                                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                    String dataString = cell.getRichStringCellValue().toString().trim();
                                    
                                    if (colIx == (colId)) {
                                        motherVersionImportCtx.put("id", dataString);
                                    } else if (colIx == (colYunqudaoListingId)) {
                                        motherVersionImportCtx.put("yunqudaoListingId", dataString);
                                    } else if (colIx == (colYunqudaoListingIdVar)) {
                                        motherVersionImportCtx.put("yunqudaoListingIdVar", dataString);
                                    } else if (colIx == (colYunqudaoFolderId)) {
                                        motherVersionImportCtx.put("yunqudaoFolderId", dataString);
                                    } else if (colIx == (colEbayAccountName)) {
                                        motherVersionImportCtx.put("ebayAccountName", dataString);
                                    } else if (colIx == (colSku)) {
                                        motherVersionImportCtx.put("sku", dataString);
                                    } else if (colIx == (colSiteId)) {
                                        motherVersionImportCtx.put("siteId", dataString);
                                    } else if (colIx == (colFormat)) {
                                        motherVersionImportCtx.put("format", dataString);
                                    } else if (colIx == (colTitle)) {
                                        motherVersionImportCtx.put("title", dataString);
                                    } else if (colIx == (colSubtitle)) {
                                        motherVersionImportCtx.put("subtitle", dataString);
                                    } else if (colIx == (colRelationship)) {
                                        motherVersionImportCtx.put("relationship", dataString);
                                    } else if (colIx == (colRelationshipDetails)) {
                                        motherVersionImportCtx.put("relationshipDetails", dataString);
                                    } else if (colIx == (colQuantity)) {
                                        motherVersionImportCtx.put("quantity", dataString);
                                    } else if (colIx == (colCurrency)) {
                                        motherVersionImportCtx.put("currency", dataString);
                                    } else if (colIx == (colStartPrice)) {
                                        motherVersionImportCtx.put("startPrice", dataString);
                                    } else if (colIx == (colReservePrice)) {
                                        motherVersionImportCtx.put("reservePrice", dataString);
                                    } else if (colIx == (colBuyItNowPrice)) {
                                        motherVersionImportCtx.put("buyItNowPrice", dataString);
                                    } else if (colIx == (colDuration)) {
                                        motherVersionImportCtx.put("duration", dataString);
                                    } else if (colIx == (colPrivateListing)) {
                                        motherVersionImportCtx.put("privateListing", dataString);
                                    } else if (colIx == (colBestOfferAutoAcceptPrice)) {
                                        motherVersionImportCtx.put("bestOfferAutoAcceptPrice", dataString);
                                    } else if (colIx == (colMinBestOfferPrice)) {
                                        motherVersionImportCtx.put("minBestOfferPrice", dataString);
                                    } else if (colIx == (colOutOfStockControl)) {
                                        motherVersionImportCtx.put("outOfStockControl", dataString);
                                    } else if (colIx == (colPicUrl)) {
                                        motherVersionImportCtx.put("picUrl", dataString);
                                    } else if (colIx == (colYunqudaoImageLayout)) {
                                        motherVersionImportCtx.put("yunqudaoImageLayout", dataString);
                                    } else if (colIx == (colUploadImageEps)) {
                                        motherVersionImportCtx.put("uploadImageEps", dataString);
                                    } else if (colIx == (colShowImageInDesc)) {
                                        motherVersionImportCtx.put("showImageInDesc", dataString);
                                    } else if (colIx == (colItemSpecific)) {
                                        motherVersionImportCtx.put("itemSpecific", dataString);
                                    } else if (colIx == (colCondition)) {
                                        motherVersionImportCtx.put("condition", dataString);
                                    } else if (colIx == (colCategory)) {
                                        motherVersionImportCtx.put("category", dataString);
                                    } else if (colIx == (colCategory2)) {
                                        motherVersionImportCtx.put("category2", dataString);
                                    } else if (colIx == (colStoreCategory)) {
                                        motherVersionImportCtx.put("storeCategory", dataString);
                                    } else if (colIx == (colStoreCategory2)) {
                                        motherVersionImportCtx.put("storeCategory2", dataString);
                                    } else if (colIx == (colUpc)) {
                                        motherVersionImportCtx.put("upc", dataString);
                                    } else if (colIx == (colEan)) {
                                        motherVersionImportCtx.put("ean", dataString);
                                    } else if (colIx == (colIsbn)) {
                                        motherVersionImportCtx.put("isbn", dataString);
                                    } else if (colIx == (colBrandMpnBrand)) {
                                        motherVersionImportCtx.put("brandMpnBrand", dataString);
                                    } else if (colIx == (colBrandMpnMpn)) {
                                        motherVersionImportCtx.put("brandMpnMpn", dataString);
                                    } else if (colIx == (colDescriptionFile)) {
                                        motherVersionImportCtx.put("descriptionFile", dataString);
                                    } else if (colIx == (colDescription)) {
                                        motherVersionImportCtx.put("description", dataString);
                                    } else if (colIx == (colDescription1)) {
                                        motherVersionImportCtx.put("description1", dataString);
                                    } else if (colIx == (colDescription2)) {
                                        motherVersionImportCtx.put("description2", dataString);
                                    } else if (colIx == (colDescription3)) {
                                        motherVersionImportCtx.put("description3", dataString);
                                    } else if (colIx == (colYunqudaoTemplateId)) {
                                        motherVersionImportCtx.put("yunqudaoTemplateId", dataString);
                                    } else if (colIx == (colYunqudaoSellerDetailId)) {
                                        motherVersionImportCtx.put("yunqudaoSellerDetailId", dataString);
                                    } else if (colIx == (colYunqudaoCounter)) {
                                        motherVersionImportCtx.put("yunqudaoCounter", dataString);
                                    } else if (colIx == (colListingEnhancement)) {
                                        motherVersionImportCtx.put("listingEnhancement", dataString);
                                    } else if (colIx == (colCountry)) {
                                        motherVersionImportCtx.put("country", dataString);
                                    } else if (colIx == (colLocation)) {
                                        motherVersionImportCtx.put("location", dataString);
                                    } else if (colIx == (colDispatchTimeMax)) {
                                        motherVersionImportCtx.put("dispatchTimeMax", dataString);
                                    } else if (colIx == (colGetItFast)) {
                                        motherVersionImportCtx.put("getItFast", dataString);
                                    } else if (colIx == (colShippingService1Option)) {
                                        motherVersionImportCtx.put("shippingService1Option", dataString);
                                    } else if (colIx == (colShippingService1Cost)) {
                                        motherVersionImportCtx.put("shippingService1Cost", dataString);
                                    } else if (colIx == (colShippingService1AddCost)) {
                                        motherVersionImportCtx.put("shippingService1AddCost", dataString);
                                    } else if (colIx == (colShippingService2Option)) {
                                        motherVersionImportCtx.put("shippingService2Option", dataString);
                                    } else if (colIx == (colShippingService2Cost)) {
                                        motherVersionImportCtx.put("shippingService2Cost", dataString);
                                    } else if (colIx == (colShippingService2AddCost)) {
                                        motherVersionImportCtx.put("shippingService2AddCost", dataString);
                                    } else if (colIx == (colShippingService3Option)) {
                                        motherVersionImportCtx.put("shippingService3Option", dataString);
                                    } else if (colIx == (colShippingService3Cost)) {
                                        motherVersionImportCtx.put("shippingService3Cost", dataString);
                                    } else if (colIx == (colShippingService3AddCost)) {
                                        motherVersionImportCtx.put("shippingService3AddCost", dataString);
                                    } else if (colIx == (colShippingService4Option)) {
                                        motherVersionImportCtx.put("shippingService4Option", dataString);
                                    } else if (colIx == (colShippingService4Cost)) {
                                        motherVersionImportCtx.put("shippingService4Cost", dataString);
                                    } else if (colIx == (colShippingService4AddCost)) {
                                        motherVersionImportCtx.put("shippingService4AddCost", dataString);
                                    } else if (colIx == (colShippingRateTable)) {
                                        motherVersionImportCtx.put("shippingRateTable", dataString);
                                    } else if (colIx == (colIntShippingService1Option)) {
                                        motherVersionImportCtx.put("intShippingService1Option", dataString);
                                    } else if (colIx == (colIntShippingService1Cost)) {
                                        motherVersionImportCtx.put("intShippingService1Cost", dataString);
                                    } else if (colIx == (colIntShippingService1AddCost)) {
                                        motherVersionImportCtx.put("intShippingService1AddCost", dataString);
                                    } else if (colIx == (colIntShippingService1Locations)) {
                                        motherVersionImportCtx.put("intShippingService1Locations", dataString);
                                    } else if (colIx == (colIntShippingService2Option)) {
                                        motherVersionImportCtx.put("intShippingService2Option", dataString);
                                    } else if (colIx == (colIntShippingService2Cost)) {
                                        motherVersionImportCtx.put("intShippingService2Cost", dataString);
                                    } else if (colIx == (colIntShippingService2AddCost)) {
                                        motherVersionImportCtx.put("intShippingService2AddCost", dataString);
                                    } else if (colIx == (colIntShippingService2Locations)) {
                                        motherVersionImportCtx.put("intShippingService2Locations", dataString);
                                    } else if (colIx == (colIntShippingService3Option)) {
                                        motherVersionImportCtx.put("intShippingService3Option", dataString);
                                    } else if (colIx == (colIntShippingService3Cost)) {
                                        motherVersionImportCtx.put("intShippingService3Cost", dataString);
                                    } else if (colIx == (colIntShippingService3AddCost)) {
                                        motherVersionImportCtx.put("intShippingService3AddCost", dataString);
                                    } else if (colIx == (colIntShippingService3Locations)) {
                                        motherVersionImportCtx.put("intShippingService3Locations", dataString);
                                    } else if (colIx == (colIntShippingService4Option)) {
                                        motherVersionImportCtx.put("intShippingService4Option", dataString);
                                    } else if (colIx == (colIntShippingService4Cost)) {
                                        motherVersionImportCtx.put("intShippingService4Cost", dataString);
                                    } else if (colIx == (colIntShippingService4AddCost)) {
                                        motherVersionImportCtx.put("intShippingService4AddCost", dataString);
                                    } else if (colIx == (colIntShippingService4Locations)) {
                                        motherVersionImportCtx.put("intShippingService4Locations", dataString);
                                    } else if (colIx == (colIntShippingService5Option)) {
                                        motherVersionImportCtx.put("intShippingService5Option", dataString);
                                    } else if (colIx == (colIntShippingService5Cost)) {
                                        motherVersionImportCtx.put("intShippingService5Cost", dataString);
                                    } else if (colIx == (colIntShippingService5AddCost)) {
                                        motherVersionImportCtx.put("intShippingService5AddCost", dataString);
                                    } else if (colIx == (colIntShippingService5Locations)) {
                                        motherVersionImportCtx.put("intShippingService5Locations", dataString);
                                    } else if (colIx == (colIntShippingRateTable)) {
                                        motherVersionImportCtx.put("intShippingRateTable", dataString);
                                    } else if (colIx == (colExcludeShipToLocation)) {
                                        motherVersionImportCtx.put("excludeShipToLocation", dataString);
                                    } else if (colIx == (colPayPalEmailAddress)) {
                                        motherVersionImportCtx.put("payPalEmailAddress", dataString);
                                    } else if (colIx == (colImmediatePayRequired)) {
                                        motherVersionImportCtx.put("immediatePayRequired", dataString);
                                    } else if (colIx == (colPayMoneyXferAccInCheckout)) {
                                        motherVersionImportCtx.put("payMoneyXferAccInCheckout", dataString);
                                    } else if (colIx == (colPaymentInstructions)) {
                                        motherVersionImportCtx.put("paymentInstructions", dataString);
                                    } else if (colIx == (colReturnsAcceptedOption)) {
                                        motherVersionImportCtx.put("returnsAcceptedOption", dataString);
                                    } else if (colIx == (colReturnsWithinOption)) {
                                        motherVersionImportCtx.put("returnsWithinOption", dataString);
                                    } else if (colIx == (colRefundOption)) {
                                        motherVersionImportCtx.put("refundOption", dataString);
                                    } else if (colIx == (colShippingCostPaidByOption)) {
                                        motherVersionImportCtx.put("shippingCostPaidByOption", dataString);
                                    } else if (colIx == (colReturnPolicyDescription)) {
                                        motherVersionImportCtx.put("returnPolicyDescription", dataString);
                                    } else if (colIx == (colBRLinkedPayPalAccount)) {
                                        motherVersionImportCtx.put("bRLinkedPayPalAccount", dataString);
                                    } else if (colIx == (colBRMaxBuyerPolicyVioCount)) {
                                        motherVersionImportCtx.put("bRMaxBuyerPolicyVioCount", dataString);
                                    } else if (colIx == (colBRMaxBuyerPolicyVioPeriod)) {
                                        motherVersionImportCtx.put("bRMaxBuyerPolicyVioPeriod", dataString);
                                    } else if (colIx == (colBRMaxItemReqMaxItemCount)) {
                                        motherVersionImportCtx.put("bRMaxItemReqMaxItemCount", dataString);
                                    } else if (colIx == (colBRMaxItemReqMinFeedbackScore)) {
                                        motherVersionImportCtx.put("bRMaxItemReqMinFeedbackScore", dataString);
                                    } else if (colIx == (colBRMaxUnpaidStrikesInfoCount)) {
                                        motherVersionImportCtx.put("bRMaxUnpaidStrikesInfoCount", dataString);
                                    } else if (colIx == (colBRMaxUnpaidStrikesInfoPeriod)) {
                                        motherVersionImportCtx.put("bRMaxUnpaidStrikesInfoPeriod", dataString);
                                    } else if (colIx == (colBRMinFeedbackScore)) {
                                        motherVersionImportCtx.put("bRMinFeedbackScore", dataString);
                                    } else if (colIx == (colBRShipToRegistrationCountry)) {
                                        motherVersionImportCtx.put("bRShipToRegistrationCountry", dataString);
                                    } else if (colIx == (colYunqudaoAutoRelistId)) {
                                        motherVersionImportCtx.put("yunqudaoAutoRelistId", dataString);
                                    } else if (colIx == (colYunqudaoAutoReplenishId)) {
                                        motherVersionImportCtx.put("yunqudaoAutoReplenishId", dataString);
                                    } else if (colIx == (colYunqudaoShowcaseId)) {
                                        motherVersionImportCtx.put("yunqudaoShowcaseId", dataString);
                                    }//read record -- END
                                }   //loop cell in a row -- END
                        
                                if (doImport) {    //if row indeed has data -- START
                                    try {   //try -- second -- START
                                        motherVersionImportCtx.put("motherVersionType", motherVersionType);
                                        motherVersionImportCtx.put("marketplace", marketplace);
                                        motherVersionImportCtx.put("userLogin", userLogin);
                                        Map result = dispatcher.runSync("createMotherVersion", motherVersionImportCtx);
                                        if (ServiceUtil.isError(result)) {  //if result gives error -- START
                                            errorList.add(ServiceUtil.getErrorMessage(result));
                                        }   //if result gives error -- END
                                        /*else {
                                            Debug.logError("finished creating ID " + motherVersionImportCtx.get("id"), module);
                                        }*/
                                        
                                    }   //try -- second -- END
                                    catch (Exception e) {
                                        return ServiceUtil.returnError(e.getMessage());
                                    }
                                }   //if row indeed has data -- END
                                
                            }   //if row is not empty -- END
                        }   //loop rows -- END
                    } catch (Exception e) {
                        return ServiceUtil.returnError(e.getMessage());
                    }   //main2 try -- END
                    
                    
                    
                    
                }   //if file exist -- END
            } catch (Exception e) {
                return ServiceUtil.returnError(e.getMessage());
            }   //main try -- END
        }
        return ServiceUtil.returnSuccess();
    }   //importMotherVersion
    
    public static Map<String, Object> calculateMotherSkuWeight(DispatchContext dctx, Map<String, ? extends Object> context) {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productIdInput = (String) context.get("productId");
        String motherSkuInput = (String) context.get("motherSku");
        
        try {
            List<EntityCondition> cond = FastList.newInstance();
            if (UtilValidate.isNotEmpty(productIdInput)) {
                cond.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productIdInput));
            }
            
            if (UtilValidate.isNotEmpty(motherSkuInput)) {
                cond.add(EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, motherSkuInput));
            }
            
            EntityCondition pmCond = null;
            if (cond.size() > 0) {
                pmCond = EntityCondition.makeCondition(cond, EntityOperator.AND);
            }
            
            List<GenericValue> motherSkuList = delegator.findList( "ProductMaster", pmCond, UtilMisc.toSet("motherSku"),
                                                                  UtilMisc.toList("motherSku"),
                                                                  new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                  false);
            
            for (GenericValue motherSkuGV : motherSkuList) {	//loop motherSkuList == START
                String motherSku = motherSkuGV.getString("motherSku");
                if (UtilValidate.isNotEmpty(motherSku)) {
                    List<GenericValue> childrenSkuList = delegator.findByAnd("ProductMaster", UtilMisc.toMap("motherSku",motherSku), null, false);
                    
                    int childrenSkuSize = childrenSkuList.size();
                    double childrenSkuCount = (childrenSkuSize - 1) * 0.15 + 1;
                    //Debug.logError("motherSku: " + motherSku + ", childrenSkuCount: " + childrenSkuCount, module);
                    for (GenericValue childrenSku : childrenSkuList) {
                        childrenSku.set("motherSkuWeight", childrenSkuCount);
                        delegator.store(childrenSku);
                    }
                }
            }	//loop motherSkuList == END
            
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            //return ServiceUtil.returnError("");
        }
        return ServiceUtil.returnSuccess(); 
    }
    
    public static Map<String, Object> uploadSalesXlsGudao(DispatchContext dctx, Map<String, ? extends Object> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
        String uploadFileName = (String) context.get("_uploadedFile_fileName");
        uploadFileName = uploadFileName.replaceAll(" ","");
        
        if (UtilValidate.isNotEmpty(uploadFileName) && UtilValidate.isNotEmpty(imageData)) {
            try {   //main try -- START
                String fileServerPath = "hot-deploy/gudao/webapp/gudao/bulkModule/upload/sales";
                File rootTargetDir = new File(fileServerPath);
                if (!rootTargetDir.exists()) {
                    boolean created = rootTargetDir.mkdirs();
                    if (!created) {
                        String errMsg = "Not create target directory";
                        Debug.logFatal(errMsg, module);
                        return ServiceUtil.returnError(errMsg);
                    }
                }
                String fileName = new SimpleDateFormat("yyMMdd-HHmm").format(Calendar.getInstance().getTime()) + "-" + userLogin.getString("userLoginId") + "-" + uploadFileName;
                
                
                String filePath = fileServerPath + "/" + fileName;
                File file = new File(filePath);
                try {
                    RandomAccessFile out = new RandomAccessFile(file, "rw");
                    out.write(imageData.array());
                    out.close();
                } catch (FileNotFoundException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.toString());
                } catch (IOException e) {
                    Debug.logError(e, module);
                    return ServiceUtil.returnError(e.toString());
                }
                
                if (file.exists()) {    //if file exist -- START
                    Map result = null;
                    if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                        result = readSalesXls(dispatcher, delegator, userLogin, filePath, uploadFileName);
                        
                    }
                    
                    /*GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                     String productStoreGroup = productStore.getString("primaryStoreGroupId");
                     if(productStoreGroup.equals("EBAY")) {
                     result = readDataFromCsv(dispatcher, delegator, userLogin, productStoreId, filePath);
                     } else if (productStoreGroup.equals("MAGENTO")) {
                     result = readDataFromMagentoCsv(dispatcher, delegator, userLogin, productStoreId, filePath);
                     } else if (productStoreGroup.equals("ALIEXPRESS")) {
                     result = readDataFromAliXls(dispatcher, delegator, userLogin, productStoreId, filePath);
                     }
                     if (ServiceUtil.isError(result)) {
                     return result;
                     }*/
                }   //if file exist -- END
            } catch (Exception e) {
                return ServiceUtil.returnError(e.getMessage());
            }   //main try -- END
        }
        return ServiceUtil.returnSuccess();
    }   //uploadSalesXlsGudao
    
    private static Map<String, Object> readSalesXls(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String filePath, String originalFilename) {   //readSalesXls
        Map<String, Object> result = ServiceUtil.returnSuccess();
        List<String> errorList = FastList.newInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {   //main try -- START
            
            String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
            FileInputStream is = new FileInputStream(new File(filePath));
            
            Workbook wb = null;
            if (fileType.equals("xls")) {
                wb = new HSSFWorkbook(is);
            } else if (fileType.equals("xlsx")) {
                wb = new XSSFWorkbook(is);
            } else {
                Debug.logError("您输入的excel格式不正确", module);
                return  ServiceUtil.returnError("您输入的excel格式不正确");
            }
            Sheet sheet ;
            Row row ;
            Cell cell;
            int numSheet = 0;
            String map[]={"platform","ownerGroup","title","date","puyuanOrderId","platformOrderId","sellerName","productId","orderTotalQuantity","orderItemQuantity",
                "productPrice","shippingFee","ebayFee","paypalFee","productCost","shippingCost","itemProfit","profitRate","currency","exchangeRate","transactionId","salesReportId","shippingMethod"};
            
            
            /*
             // 循环工作表Sheet
             for(int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++){
             sheet = wb.getSheetAt( numSheet);
             if(sheet == null){
             continue;
             }
             */
            sheet = wb.getSheetAt( numSheet);
            String[][] arrayResult = new String[sheet.getLastRowNum()][22];
            int name=0;
            int column=0;
            int hang=0;
            String value="";
            // 循环行Row
            //Debug.logError("total Row: " + sheet.getLastRowNum(), module);
            //Debug.logError("total Col: " + sheet.getRow(1).getLastCellNum(), module);
            
            for(int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++){
                row = sheet.getRow( rowNum);
                if(row == null){
                    break;
                }
                //Debug.logError(" Row: " + rowNum, module);
                Map<String,Object> salesImportCtx = FastMap.newInstance();
                Object check = null;
                for(int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++){
                    cell = row.getCell( cellNum);
                    value = cell.getRichStringCellValue().getString().trim();
                    
                    if(map[cellNum]=="platform"){
                        String platformName = null;
                        if (value.equals("amazon") || value.equals("amazon11")) {
                            platformName = "AMAZON";
                        } else if (value.equals("aliexpress")|| value.equals("smt")) {
                            platformName = "SMT";
                        } else if (value.equals("")) {
                            platformName = "OTHER";
                        } else if (value.equals("ebay")) {
                            platformName = "EBAY";
                        } else if (value.equals("cdis")) {
                            platformName = "CDISCOUNT";
                        } else if (value.equals("lazada")) {
                            platformName = "LAZADA";
                        } else if (value.equals("wish")) {
                            platformName = "WISH";
                        } else if (value.equals("tophatter")) {
                            platformName = "TOPHATTER";
                        } else if (value.equals("99buy")) {
                            platformName = "OTHER";
                        } else if (value.equals("s")) {
                            platformName = "OTHER";
                        } else if (UtilValidate.isEmpty(value)) {
                            platformName = "OTHER";
                        }
                        
                        salesImportCtx.put("platform", platformName);
                        //Debug.logError("check value: " + value.toUpperCase() + ", property: " + salesReportProperties().get(value.toUpperCase()), module);
                    }
                    else if(map[cellNum]=="ownerGroup"){
                        GenericValue puyuanSbuMap = delegator.findOne("PuyuanSbuMap", UtilMisc.toMap("puyuanName", value), false);
                        if (UtilValidate.isNotEmpty(puyuanSbuMap)){
                            salesImportCtx.put("ownerGroup", puyuanSbuMap.getString("ownerGroup"));
                            salesImportCtx.put("createdBy", puyuanSbuMap.getString("createdBy"));
                        }else{
                            if (UtilValidate.isEmpty(value)) {
                                salesImportCtx.put("ownerGroup", "FAKE");
                            }else {
                                salesImportCtx.put("ownerGroup", value.toUpperCase());
                            }
                        }
                        
                        //check = value;
                    }
                    else if(map[cellNum]=="title"){
                        salesImportCtx.put("title", value);
                        //check = value;
                    }
                    else if(map[cellNum]=="date"){
                        salesImportCtx.put("date", new java.sql.Timestamp(sdf.parse(row.getCell(3).getRichStringCellValue().toString()).getTime()));
                        //check = value;
                    }
                    else if(map[cellNum]=="puyuanOrderId"){
                        salesImportCtx.put("puyuanOrderId", value.toString());
                        //check = value;
                    }
                    else if(map[cellNum]=="platformOrderId"){
                        salesImportCtx.put("platformOrderId",value.toString());
                    }
                    else if(map[cellNum]=="sellerName"){
                        salesImportCtx.put("sellerName", value.toString().toUpperCase());
                    }
                    else if(map[cellNum]=="productId"){
                        salesImportCtx.put("productId",value.toString());
                    }
                    else if(map[cellNum]=="orderTotalQuantity"){
                        salesImportCtx.put("orderTotalQuantity", Long.valueOf(value));
                        
                    }
                    else if(map[cellNum]=="orderItemQuantity"){
                        salesImportCtx.put("orderItemQuantity",Long.valueOf(value));
                    }
                    else if(map[cellNum]=="productPrice"){
                        salesImportCtx.put("productPrice", new BigDecimal(value));
                    }
                    else if(map[cellNum]=="shippingFee"){
                        salesImportCtx.put("shippingFee", new BigDecimal(value));
                    }
                    else if(map[cellNum]=="ebayFee"){
                        salesImportCtx.put("ebayFee", new BigDecimal(value));
                        
                    }else if(map[cellNum]=="paypalFee"){
                        salesImportCtx.put("paypalFee", new BigDecimal(value));
                    }
                    else if(map[cellNum]=="productCost"){
                        salesImportCtx.put("productCost", new BigDecimal(value));
                    }
                    else if(map[cellNum]=="shippingCost"){
                        salesImportCtx.put("shippingCost", new BigDecimal(value));
                    }
                    else if(map[cellNum]=="itemProfit"){
                        salesImportCtx.put("itemProfit", new BigDecimal(value));
                    }
                    else if(map[cellNum]=="profitRate"){
                        salesImportCtx.put("profitRate", new BigDecimal(value));
                    }
                    else if(map[cellNum]=="currency"){
                        salesImportCtx.put("currency", value.toUpperCase());
                    }
                    else if(map[cellNum]=="exchangeRate"){
                        salesImportCtx.put("exchangeRate", new BigDecimal(value));
                    }
                    else if(map[cellNum]=="transactionId"){
                        salesImportCtx.put("transactionId", value.toString());
                    }
                    else if(map[cellNum]=="salesReportId"){
                        salesImportCtx.put("salesReportId", value.toString());
                    }
                    else if(map[cellNum]=="shippingMethod"){
                        salesImportCtx.put("shippingMethod", value.toString());
                    }
                    
                    
                }
                //if (UtilValidate.isEmpty(check)) {break;}
                //Debug.logError(salesImportCtx.toString(),module);
                //if(UtilValidate.isEmpty(salesImportCtx.get("transactionId"))){salesImportCtx.put("transactionId","_NA_");}
                //if(UtilValidate.isEmpty(salesImportCtx.get("platform"))){salesImportCtx.put("platform","_NA_");}
                //if(UtilValidate.isEmpty(salesImportCtx.get("sellerName"))){salesImportCtx.put("sellerName","_NA_");}
                if(UtilValidate.isEmpty(salesImportCtx.get("salesReportId"))){salesImportCtx.put("salesReportId","_NA_");}
                salesImportCtx.put("userLogin", userLogin);
                result = dispatcher.runSync("createSalesImportGudao", salesImportCtx);
                if (ServiceUtil.isSuccess(result)) {
                    //Debug.logError("rowNum: " + rowNum + "createSalesImportGudao success for " + salesImportCtx.get("salesReportId"), module);
                }
                
                /*                      GenericValue gv = delegator.makeValue("SalesReportImport", UtilMisc.toMap("puyuanOrderId", salesImportCtx.get("puyuanOrderId")));
                 gv.set("sellerName", salesImportCtx.get("sellerName"));
                 gv.set("productId", salesImportCtx.get("productId"));
                 gv.set("transactionId", salesImportCtx.get("transactionId"));
                 delegator.createOrStore(gv);*/
            }
            
        }
        catch (Exception e){
            return ServiceUtil.returnError(e.getMessage());
        }
        return ServiceUtil.returnSuccess();
        //main try -- END
    }   //readSalesXls
    
    private static String getValue(Cell cell){
        if(cell.getCellType() == cell.CELL_TYPE_BOOLEAN){
            return String.valueOf( cell.getBooleanCellValue());
            //    }else if(cell.getCellType() == cell.CELL_TYPE_NUMERIC){
            //      return String.valueOf( cell.getNumericCellValue());
            //    }else if (cell.getCellType() ==cell.CELL_TYPE_STRING){
            //   	return String.valueOf(cell.getStringCellValue());
        }else{
            return String.valueOf( cell.getStringCellValue());
        }  
    }
    
    public static Map<String, Object> scheduledRefreshProductMaster(DispatchContext dctx, Map<String, ? extends Object> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        
        try {   //main try == START
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            now.add(Calendar.MINUTE, -6);
            //java.sql.Date nowTimestamp = new java.sql.Date(now.getTimeInMillis());
            Timestamp nowTimestamp = Timestamp.valueOf(sdf.format(now.getTime()));
            //Debug.logError(nowTimestamp + "", module);
            
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("lastUpdatedStamp",EntityOperator.GREATER_THAN , nowTimestamp),
                                                                                      EntityCondition.makeCondition("importedStatus",EntityOperator.EQUALS , "Y"),
                                                                                      EntityCondition.makeCondition("updateGms",EntityOperator.EQUALS , null)
                                                                                      ));

            List<GenericValue> productImportGudaoList = delegator.findList("ProductImportGudao", condition, null, null, null, false);
            //Debug.logError(productImportGudaoList.size() + "", module);
            List<String> productIdListAll = new ArrayList<String>();
            for (GenericValue productImportGudao : productImportGudaoList) {    //loop productImportGudaoList == START
                productIdListAll.add(productImportGudao.getString("productId"));
            }   //loop productImportGudaoList == END
            HashSet<String> uniqueProductIdList = new HashSet<String>(productIdListAll);
            
            List<String> productIdList = new ArrayList<String>();
            for (String uniqueProductId : uniqueProductIdList) {
                productIdList.add(uniqueProductId);
            }
            //Debug.logError(productIdList.size() + "", module);
            if (productIdList.size() > 0) {
                Map<String,Object> refreshPmMap = FastMap.newInstance();
                refreshPmMap.put("productIdList", productIdList);
                refreshPmMap.put("userLogin", userLogin);
                dispatcher.runSync("gudaoRefreshProductMaster", refreshPmMap);
            }
        }   //main try == END
        catch (Exception e){
            Debug.logError(e.toString(), module);
        }
        
        return result;
        
    }   //scheduledRefreshProductMaster
    
    public static Map<String, Object> calculateSalesReportImport(DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat sdfTest = new SimpleDateFormat("2010-01-01 hh:mm:ss");
        Timestamp nowTimestamp = Timestamp.valueOf(sdfTo.format(now.getTime()));
        Timestamp fromTimestamp = Timestamp.valueOf(sdf.format(now.getTime()));
        java.sql.Timestamp fromDate = UtilDateTime.getMonthStart(fromTimestamp);
        /*Calendar calendar = Calendar.getInstance();
         calendar.set(Calendar.DAY_OF_MONTH, 1);
         calendar.add(Calendar.MONTH, -1);
         Timestamp fromDate = Timestamp.valueOf(sdf.format(calendar.getTime()));
         /*SimpleDateFormat sdfJune = new SimpleDateFormat("2016-06-01 hh:mm:ss");
         Timestamp juneTimestamp = Timestamp.valueOf(sdfJune.format(now.getTime()));
         fromDate = UtilDateTime.getMonthStart(juneTimestamp);*/
        Debug.logError("fromDate: " + fromDate, module);
        Debug.logError("nowTimestamp: " + nowTimestamp, module);
        
        try {
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate),
                                                                                      EntityCondition.makeCondition("date", EntityOperator.LESS_THAN_EQUAL_TO, nowTimestamp)
                                                                                      ));
            
            List<GenericValue> salesReportImportList = delegator.findList("SalesReportImport", condition, null, null, null, false);
            BigDecimal sales = BigDecimal.ZERO;
            BigDecimal refund = BigDecimal.ZERO;
            BigDecimal domesticFee = BigDecimal.ZERO;
            BigDecimal platformFee = BigDecimal.ZERO;
            BigDecimal materialFee1 = BigDecimal.ZERO;
            BigDecimal materialFee2 = BigDecimal.ZERO;
            BigDecimal materialFee = BigDecimal.ZERO;
            BigDecimal internationalFee = BigDecimal.ZERO;
            BigDecimal productFee = BigDecimal.ZERO;
            BigDecimal itemProfit = BigDecimal.ZERO;
            List<GenericValue> findOverseaSbuList =null;
            List<GenericValue> hasOverseaList = delegator.findByAnd("GudaoShippingMethod", UtilMisc.toMap("oversea","Y"),null,false);
            for(GenericValue hasOversea : hasOverseaList){
                findOverseaSbuList = delegator.findByAnd("SalesReportImport", UtilMisc.toMap("shippingMethod",hasOversea.getString("shippingMethod")),null,false);
            }
            /*
             List<GenericValue> distinctOrderCountList = delegator.findList( "SalesReportImport",
             condition,
             UtilMisc.toSet("puyuanOrderId"),
             UtilMisc.toList("puyuanOrderId"),
             new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
             false);
             
             int orderCount = distinctOrderCountList.size();
             Debug.logError("orderCount: " + orderCount, module);
             */
            
            for(GenericValue salesList : salesReportImportList){
                String hasOverseaSbu="N";
                String platform = salesList.getString("platform");
                String ownerGroup =  salesList.getString("ownerGroup");
                for(GenericValue findOverseaSbu : findOverseaSbuList){
                    if(findOverseaSbu.getString("ownerGroup").equals(ownerGroup)){
                        hasOverseaSbu="Y";
                    }
                }
                String puyuanName =  salesList.getString("puyuanName");
                Timestamp date = salesList.getTimestamp("date");
                BigDecimal productPrice = salesList.getBigDecimal("productPrice");
                BigDecimal shippingFee = salesList.getBigDecimal("shippingFee");
                BigDecimal productCost = salesList.getBigDecimal("productCost");
                BigDecimal ebayFee = salesList.getBigDecimal("ebayFee");
                BigDecimal paypalFee = salesList.getBigDecimal("paypalFee");
                String puyuanOrderId = salesList.getString("puyuanOrderId");
                String sku = salesList.getString("productId");
                String shippingMethod = salesList.getString("shippingMethod");
                BigDecimal shippingCost = salesList.getBigDecimal("shippingCost");
                String sellerName =  salesList.getString("sellerName");
                sales = productPrice.add(shippingFee);
                productFee = productCost;
                
                List<GenericValue> partyIdList = delegator.findByAnd("PlatformAccount", UtilMisc.toMap("ownerGroup",ownerGroup,"sellerName",sellerName),null, false);
                String partyId =null;
                String platformAccountPlatform = null;
                if(UtilValidate.isNotEmpty(partyIdList)){
                    partyId = EntityUtil.getFirst(partyIdList).getString("platformPartyId");
                    platformAccountPlatform = EntityUtil.getFirst(partyIdList).getString("platform");
                } else {
                    partyIdList = delegator.findByAnd("PlatformAccount", UtilMisc.toMap("ownerGroup","ALL","sellerName",sellerName),null, false);
                    if (UtilValidate.isNotEmpty(partyIdList)) {
                        partyId = EntityUtil.getFirst(partyIdList).getString("platformPartyId");
                        platformAccountPlatform = EntityUtil.getFirst(partyIdList).getString("platform");
                    }
                }
                //Debug.logError("platformPartyId: " + partyId, module);
                if (UtilValidate.isEmpty(platform)) {
                    platform = platformAccountPlatform;
                }
                
                List<GenericValue> distinctOrderCountList = delegator.findByAnd( "SalesReportImport", UtilMisc.toMap("puyuanOrderId",puyuanOrderId),null,false);
                int orderCount = distinctOrderCountList.size();
                String 	oversea = "N";
                List<GenericValue> overseaList = delegator.findByAnd("GudaoShippingMethod", UtilMisc.toMap("shippingMethod",shippingMethod,"oversea","Y"),null,false);
                if(UtilValidate.isNotEmpty(overseaList)){
                    oversea = "Y";
                }
                
                //Debug.logError("orderCount: " + orderCount, module);
                Timestamp previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime()));
                List<GenericValue> variableListRefund = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","REFUND"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableListDomesticFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","DOMESTICFEE"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableListListingFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","LISTINGFEE"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableListMaterialFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","MATERIALFEE"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableMaterialOrderCount = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","MATERIALORDERCOUNT"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableListInterFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","INTERNATIONALFEE"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableListOverseaFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","OVERSEAFEE"), UtilMisc.toList("fromDate ASC"),false);
                
                BigDecimal refundVariableValue = BigDecimal.ZERO;
                for (GenericValue refundVar : variableListRefund) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (refundVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (refundVar.getTimestamp("fromDate"))>=0){refundVariableValue = refundVar.getBigDecimal("value");}
                    previousFromDate = refundVar.getTimestamp("fromDate");
                }
                //Debug.logError("refundVariableValue: " + refundVariableValue , module);
                refund = sales.multiply(refundVariableValue);
                BigDecimal domesticVariableValue = BigDecimal.ZERO;
                for (GenericValue domesticVar : variableListDomesticFee) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (domesticVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (domesticVar.getTimestamp("fromDate"))>=0){domesticVariableValue = domesticVar.getBigDecimal("value");}
                    previousFromDate = domesticVar.getTimestamp("fromDate");
                }
                //Debug.logError("domesticVariableValue: " + domesticVariableValue , module);
                domesticFee = salesList.getBigDecimal("productCost").multiply(domesticVariableValue);
                BigDecimal platformVariableValue = BigDecimal.ZERO;
                for (GenericValue platformVar : variableListListingFee) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (platformVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (platformVar.getTimestamp("fromDate"))>=0){platformVariableValue = platformVar.getBigDecimal("value");}
                    previousFromDate = platformVar.getTimestamp("fromDate");
                }
                //Debug.logError("platformVariableValue: " + platformVariableValue , module);
                platformFee =  sales.multiply(platformVariableValue);
                if(platform.equals("EBAY")){
                    platformFee =  sales.multiply(platformVariableValue).add(ebayFee.add(paypalFee));
                }
                BigDecimal materialFeeVariableValue = BigDecimal.ZERO;
                for (GenericValue materialVar : variableListMaterialFee) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (materialVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (materialVar.getTimestamp("fromDate"))>=0){materialFeeVariableValue = materialVar.getBigDecimal("value");}
                    previousFromDate = materialVar.getTimestamp("fromDate");
                }
                materialFee1 = sales.multiply(materialFeeVariableValue);
                //Debug.logError("materialFeeVariableValue: " + materialFeeVariableValue , module);
                BigDecimal materialOrderCountValue = BigDecimal.ZERO;
                for (GenericValue materialOrderCountVar : variableMaterialOrderCount) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (materialOrderCountVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (materialOrderCountVar.getTimestamp("fromDate"))>=0){materialOrderCountValue = materialOrderCountVar.getBigDecimal("value");}
                    previousFromDate = materialOrderCountVar.getTimestamp("fromDate");
                }
                //Debug.logError("materialOrderCountValue: " + materialOrderCountValue , module);
                //Debug.logError("orderCount: " + orderCount , module);
                materialFee2 = materialOrderCountValue.divide(new BigDecimal(orderCount), 6);
                //Debug.logError("materialOrderCountValue: " + materialOrderCountValue, module);
                materialFee = materialFee1.add(materialFee2);
                
                BigDecimal internationalFeeVariableValue = BigDecimal.ZERO;
                for (GenericValue internationalVar : variableListInterFee) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (internationalVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (internationalVar.getTimestamp("fromDate"))>=0){internationalFeeVariableValue = internationalVar.getBigDecimal("value");}
                    previousFromDate = internationalVar.getTimestamp("fromDate");
                }
                //Debug.logError("internationalFeeVariableValue: " + internationalFeeVariableValue , module);
                internationalFee = shippingCost.add(sales.multiply(internationalFeeVariableValue));
                if(UtilValidate.isNotEmpty(oversea)){
                    if(oversea.equals("Y")){
                        BigDecimal overseaVariableValue = BigDecimal.ZERO;
                        for (GenericValue overseaVar : variableListOverseaFee) {
                            if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (overseaVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                            if(salesList.getTimestamp("date") .compareTo (overseaVar.getTimestamp("fromDate"))>=0){overseaVariableValue = overseaVar.getBigDecimal("value");}
                            previousFromDate = overseaVar.getTimestamp("fromDate");
                        }
                        if(platform.equals("AMAZON")){
                            internationalFee = sales.multiply(overseaVariableValue);
                        } else if(platform.equals("EBAY")){
                            internationalFee = sales.multiply(overseaVariableValue);
                        }
                    }else if(oversea.equals("N")) {
                        if(platform.equals("EBAY") && hasOverseaSbu.equals("Y")){
                            internationalFee = shippingCost;
                        }
                    }
                    
                }
                itemProfit = sales.subtract(refund);
                itemProfit = itemProfit.subtract(platformFee);
                itemProfit = itemProfit.subtract(productFee);
                itemProfit = itemProfit.subtract(domesticFee);
                itemProfit = itemProfit.subtract(internationalFee);
                itemProfit = itemProfit.subtract(materialFee);
                
                salesList.set("platform", platform);
                salesList.set("sales",sales);
                salesList.set("refund",refund);
                salesList.set("platformFee",platformFee);
                salesList.set("productFee",productFee);
                salesList.set("domesticFee",domesticFee);
                salesList.set("internationalFee",internationalFee);
                salesList.set("materialFee",materialFee);
                salesList.set("profit",itemProfit);
                salesList.set("platformPartyId",partyId);
                delegator.store(salesList);
                
                
                //Debug.logError("salesreportId: " + salesList.getString("salesReportId"), module);
                /*
                 Debug.logError("sales: " + sales, module);
                 itemProfit = sales.subtract(refund);
                 Debug.logError("refund: " + refund + "; refundVar: " + refundVariableValue, module);
                 Debug.logError("itemProfit min refund: " + itemProfit, module);
                 itemProfit = itemProfit.subtract(platformFee);
                 Debug.logError("platformFee: " + platformFee + "; platformVar: " + platformVariableValue, module);
                 Debug.logError("itemProfit min platformfee: " + itemProfit, module);
                 itemProfit = itemProfit.subtract(productFee);
                 Debug.logError("productFee: " + productFee , module);
                 Debug.logError("itemProfit min productFee: " + itemProfit, module);
                 itemProfit = itemProfit.subtract(domesticFee);
                 Debug.logError("domesticFee: " + domesticFee + "; domesticVariableValue: " + domesticVariableValue, module);
                 Debug.logError("itemProfit min domesticFee: " + itemProfit, module);
                 itemProfit = itemProfit.subtract(internationalFee);
                 Debug.logError("internationalFee: " + internationalFee + "; internationalFeeVariableValue: " + internationalFeeVariableValue, module);
                 Debug.logError("itemProfit min internationalFee: " + itemProfit, module);
                 itemProfit = itemProfit.subtract(materialFee);
                 Debug.logError("materialFee: " + materialFee + "; materialFeeVariableValue: " + materialFeeVariableValue, module);
                 Debug.logError("itemProfit min materialFee: " + itemProfit, module);
                 
                 Debug.logError("salesListDate: " + salesList.getTimestamp("date"), module);
                 Debug.logError("previousFromDate: " + previousFromDate, module);
                 Debug.logError("result: " + salesList.getTimestamp("date").compareTo(previousFromDate), module);
                 Debug.logError("platform: " + platform, module);
                 Debug.logError("ownerGroup: " + ownerGroup, module);
                 Debug.logError("puyuanName: " + puyuanName, module);
                 Debug.logError("date: " + date, module);
                 Debug.logError("salesreportId: " + salesList.getString("salesReportId"), module);
                 */
            }
        }catch (GenericEntityException e) {
            e.printStackTrace();
        }
        Debug.logError("finished running calculateSalesReportImport", module);
        return ServiceUtil.returnSuccess();
    }   //calculateSalesReportImport
    
    public static Map<String, Object> calculateSalesReportImportWeekInput(DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException {
        
        Debug.logError("Starting running calculateSalesReportImport", module);
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String yearNumber = (String) context.get("yearNumber");
        String splitMonth = (String) context.get("weekNumber");
        String monthNumber = (String) context.get("monthNumber");
        
        
        Calendar now = Calendar.getInstance();
        Calendar monthStart = Calendar.getInstance();
        monthStart.set(Calendar.YEAR,  Integer.parseInt(yearNumber));
        monthStart.set(Calendar.MONTH, Integer.parseInt(monthNumber)-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat sdfEnd= new SimpleDateFormat("yyyy-MM-dd 23:59:59.999");
        SimpleDateFormat sdfTest = new SimpleDateFormat("2010-01-01 hh:mm:ss");
        Timestamp nowTimestamp = Timestamp.valueOf(sdfTo.format(now.getTime()));
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,  Integer.parseInt(yearNumber));
        calendar.set(Calendar.MONTH, Integer.parseInt(monthNumber)-1);
        Timestamp fromTimestamp = Timestamp.valueOf(sdf.format(monthStart.getTime()));
        java.sql.Timestamp fromDate = UtilDateTime.getMonthStart(fromTimestamp);
        Timestamp toDate = nowTimestamp;
        if(splitMonth.equals("1")){
            calendar.set(Calendar.DAY_OF_MONTH, 8);
            toDate = Timestamp.valueOf(sdf.format(calendar.getTime()));
            if(nowTimestamp.compareTo(toDate) < 0){
                toDate = nowTimestamp;
            }
        }
        if(splitMonth.equals("2")) {
            calendar.set(Calendar.DAY_OF_MONTH, 8);
            fromDate = Timestamp.valueOf(sdf.format(calendar.getTime()));
            calendar.set(Calendar.DAY_OF_MONTH, 15);
            toDate = Timestamp.valueOf(sdf.format(calendar.getTime()));
            if(nowTimestamp.compareTo(toDate) < 0){
                toDate = nowTimestamp;
            }
        }
        if(splitMonth.equals("3")) {
            calendar.set(Calendar.DAY_OF_MONTH, 15);
            fromDate = Timestamp.valueOf(sdf.format(calendar.getTime()));
            calendar.set(Calendar.DAY_OF_MONTH, 22);
            toDate = Timestamp.valueOf(sdf.format(calendar.getTime()));
            if(nowTimestamp.compareTo(toDate) < 0){
                toDate = nowTimestamp;
            }
            
        }
        if(splitMonth.equals("4")) {
            calendar.set(Calendar.DAY_OF_MONTH, 22);
            fromDate = Timestamp.valueOf(sdf.format(calendar.getTime()));
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
            toDate = Timestamp.valueOf(sdfEnd.format(calendar.getTime()));
            if(nowTimestamp.compareTo(toDate) <= 0){
                toDate = nowTimestamp;
            }
        }
        
        
        try {
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate),
                                                                                      EntityCondition.makeCondition("date", EntityOperator.LESS_THAN, toDate)
                                                                                      ));
            List<GenericValue> salesReportImportList = delegator.findList("SalesReportImport", condition, null, null, null, false);
            BigDecimal sales = BigDecimal.ZERO;
            BigDecimal refund = BigDecimal.ZERO;
            BigDecimal domesticFee = BigDecimal.ZERO;
            BigDecimal platformFee = BigDecimal.ZERO;
            BigDecimal materialFee1 = BigDecimal.ZERO;
            BigDecimal materialFee2 = BigDecimal.ZERO;
            BigDecimal materialFee = BigDecimal.ZERO;
            BigDecimal internationalFee = BigDecimal.ZERO;
            BigDecimal productFee = BigDecimal.ZERO;
            BigDecimal itemProfit = BigDecimal.ZERO;
            
            for(GenericValue salesList : salesReportImportList){
                String platform=salesList.getString("platform");
                String ownerGroup =  salesList.getString("ownerGroup");
                
                String puyuanName =  salesList.getString("puyuanName");
                Timestamp date = salesList.getTimestamp("date");
                BigDecimal productPrice = salesList.getBigDecimal("productPrice");
                BigDecimal shippingFee = salesList.getBigDecimal("shippingFee");
                BigDecimal productCost = salesList.getBigDecimal("productCost");
                BigDecimal ebayFee = salesList.getBigDecimal("ebayFee");
                BigDecimal paypalFee = salesList.getBigDecimal("paypalFee");
                BigDecimal platformFeeManual = salesList.getBigDecimal("platformFeeManual");
                long dailyOrderCount = salesList.getLong("dailyOrderCount");
                String puyuanOrderId = salesList.getString("puyuanOrderId");
                String sku = salesList.getString("productId");
                String shippingMethod = salesList.getString("shippingMethod");
                BigDecimal shippingCost = salesList.getBigDecimal("shippingCost");
                String sellerName =  salesList.getString("sellerName");
                sales = productPrice.add(shippingFee);
                productFee = productCost;
                
                List<GenericValue> partyIdList = delegator.findByAnd("PlatformAccount", UtilMisc.toMap("ownerGroup",ownerGroup,"sellerName",sellerName),null, false);
                String partyId =null;
                String platformAccountPlatform = null;
                if(UtilValidate.isNotEmpty(partyIdList)){
                    partyId = EntityUtil.getFirst(partyIdList).getString("platformPartyId");
                    platformAccountPlatform = EntityUtil.getFirst(partyIdList).getString("platform");
                } else {
                    partyIdList = delegator.findByAnd("PlatformAccount", UtilMisc.toMap("ownerGroup","ALL","sellerName",sellerName),null, false);
                    if (UtilValidate.isNotEmpty(partyIdList)) {
                        partyId = EntityUtil.getFirst(partyIdList).getString("platformPartyId");
                        platformAccountPlatform = EntityUtil.getFirst(partyIdList).getString("platform");
                    }
                }
                //Debug.logError("platformAccountPlatform: " + platformAccountPlatform, module);
                if (UtilValidate.isEmpty(platform)) {
                    platform = platformAccountPlatform;
                }
                //Debug.logError("platform:*******-------> " + platform, module);
                List<GenericValue> distinctOrderCountList = delegator.findByAnd( "SalesReportImport", UtilMisc.toMap("puyuanOrderId",puyuanOrderId),null,false);
                int orderCount = distinctOrderCountList.size();
                
                Timestamp previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime()));
                List<GenericValue> variableListRefund = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","REFUND"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableListDomesticFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","DOMESTICFEE"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableListListingFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","LISTINGFEE"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableListMaterialFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","MATERIALFEE"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableMaterialOrderCount = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","MATERIALORDERCOUNT"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableListInterFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","INTERNATIONALFEE"), UtilMisc.toList("fromDate ASC"),false);
                List<GenericValue> variableListOverseaFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",platform,"ownerGroup",puyuanName,"type","OVERSEAFEE"), UtilMisc.toList("fromDate ASC"),false);
                //refund
                BigDecimal refundVariableValue = BigDecimal.ZERO;
                for (GenericValue refundVar : variableListRefund) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (refundVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (refundVar.getTimestamp("fromDate"))>=0){refundVariableValue = refundVar.getBigDecimal("value");}
                    previousFromDate = refundVar.getTimestamp("fromDate");
                }
                refund = sales.multiply(refundVariableValue);
                //domesticfee
                BigDecimal domesticVariableValue = BigDecimal.ZERO;
                for (GenericValue domesticVar : variableListDomesticFee) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (domesticVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (domesticVar.getTimestamp("fromDate"))>=0){domesticVariableValue = domesticVar.getBigDecimal("value");}
                    previousFromDate = domesticVar.getTimestamp("fromDate");
                }
                domesticFee = salesList.getBigDecimal("productCost").multiply(domesticVariableValue);
                //platformfee
                BigDecimal platformVariableValue = BigDecimal.ZERO;
                for (GenericValue platformVar : variableListListingFee) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (platformVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (platformVar.getTimestamp("fromDate"))>=0){platformVariableValue = platformVar.getBigDecimal("value");}
                    previousFromDate = platformVar.getTimestamp("fromDate");
                }
                platformFee =  sales.multiply(platformVariableValue);
                if(platform.equals("EBAY")){
                    platformFee =  sales.multiply(platformVariableValue).add(platformFeeManual);
                }
                if(platform.equals("SMT")){
                    platformFee =  platformFeeManual;
                }
                
                BigDecimal materialFeeVariableValue = BigDecimal.ZERO;
                for (GenericValue materialVar : variableListMaterialFee) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (materialVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (materialVar.getTimestamp("fromDate"))>=0){materialFeeVariableValue = materialVar.getBigDecimal("value");}
                    previousFromDate = materialVar.getTimestamp("fromDate");
                }
                materialFee1 = sales.multiply(materialFeeVariableValue);
                BigDecimal materialOrderCountValue = BigDecimal.ZERO;
                for (GenericValue materialOrderCountVar : variableMaterialOrderCount) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (materialOrderCountVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (materialOrderCountVar.getTimestamp("fromDate"))>=0){materialOrderCountValue = materialOrderCountVar.getBigDecimal("value");}
                    previousFromDate = materialOrderCountVar.getTimestamp("fromDate");
                }
                materialFee2 = materialOrderCountValue.multiply(BigDecimal.valueOf(dailyOrderCount));
                materialFee = materialFee1.add(materialFee2);
                
                
                BigDecimal internationalFeeVariableValue = BigDecimal.ZERO;
                for (GenericValue internationalVar : variableListInterFee) {
                    if(salesList.getTimestamp("date").compareTo(previousFromDate)>=0 && salesList.getTimestamp("date").compareTo  (internationalVar.getTimestamp("fromDate"))<0){ previousFromDate = Timestamp.valueOf(sdfTest.format(now.getTime())); break;}
                    if(salesList.getTimestamp("date") .compareTo (internationalVar.getTimestamp("fromDate"))>=0){internationalFeeVariableValue = internationalVar.getBigDecimal("value");}
                    previousFromDate = internationalVar.getTimestamp("fromDate");
                }
                internationalFee = shippingCost.add(sales.multiply(internationalFeeVariableValue));
                
                itemProfit = sales.subtract(refund);
                itemProfit = itemProfit.subtract(platformFee);
                itemProfit = itemProfit.subtract(productFee);
                itemProfit = itemProfit.subtract(domesticFee);
                itemProfit = itemProfit.subtract(internationalFee);
                itemProfit = itemProfit.subtract(materialFee);
                
                salesList.set("platform",platform);
                salesList.set("sales",sales);
                salesList.set("refund",refund);
                salesList.set("platformFee",platformFee);
                salesList.set("productFee",productFee);
                salesList.set("domesticFee",domesticFee);
                salesList.set("internationalFee",internationalFee);
                salesList.set("materialFee",materialFee);
                salesList.set("profit",itemProfit);
                salesList.set("platformPartyId",partyId);
                delegator.store(salesList);
                
            }
        }catch (GenericEntityException e) {
            e.printStackTrace();
        }
        Debug.logError("finished running calculateSalesReportImport", module);
        return ServiceUtil.returnSuccess();
    }   //calculateSalesReportImportWeekInput
    
    public static Map<String, Object> calculateSalesReportImportMonthly(DispatchContext dctx, Map<String, ? extends Object> context)
    throws GenericEntityException {
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat sdfTo= new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, -1);
        calendar2.set(Calendar.DATE, calendar2.getActualMaximum(Calendar.DATE));
        Timestamp fromDate = Timestamp.valueOf(sdf.format(calendar.getTime()));
        Debug.logError("fromDate: " + fromDate, module);
        Timestamp toDate = Timestamp.valueOf(sdfTo.format(calendar2.getTime()));
        Debug.logError("toDate: " + toDate, module);
        long daily = 0;
        if (UtilValidate.isNotEmpty(fromDate) && UtilValidate.isNotEmpty(toDate)) {
            daily = Math.abs(toDate.getTime() - fromDate.getTime()) / (24*60*60*1000) + 1;
        }
        Debug.logError("daily: " + daily, module);
        try {
            
            List<GenericValue> platformList = delegator.findByAnd("RoleType", UtilMisc.toMap("parentTypeId","PLATFORM"), UtilMisc.toList("description ASC"),false);
            
            for(GenericValue pfl : platformList){
                List<GenericValue> ownerGroupList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("partyGroupComments","ACTIVE","roleTypeId","SBU"), UtilMisc.toList("officeSiteName ASC"),false);
                String platform=pfl.getString("roleTypeId");
                for(GenericValue owner : ownerGroupList){
                    String ownerGroup =  owner.getString("officeSiteName");
                    List<GenericValue> platformDepartmentList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("roleTypeId",platform), UtilMisc.toList("partyId ASC"),false);
                    for(GenericValue pfdl : platformDepartmentList){
                        String departmentId =  pfdl.getString("partyId");
                        EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                  EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate),
                                                                                                  EntityCondition.makeCondition("date", EntityOperator.LESS_THAN_EQUAL_TO, toDate),
                                                                                                  EntityCondition.makeCondition("platform", EntityOperator.EQUALS, platform),
                                                                                                  EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroup),
                                                                                                  EntityCondition.makeCondition("platformPartyId", EntityOperator.EQUALS, departmentId)
                                                                                                  
                                                                                                  ));
                        GenericValue sriMonthly = delegator.makeValue("SalesReportImportMonthly", UtilMisc.toMap("date",fromDate,"platform",platform,"ownerGroup",ownerGroup,"platformPartyId",departmentId));
                        
                        List<GenericValue> salesReportImportMonthlyList = delegator.findList("SalesReportImport", condition, null, null, null, false);
                        BigDecimal sumSales = BigDecimal.ZERO;
                        BigDecimal sumProfit = BigDecimal.ZERO;
                        BigDecimal dailySumSales = BigDecimal.ZERO;
                        BigDecimal dailySumProfit = BigDecimal.ZERO;
                        Debug.logError("salesReportImportMonthlyList: " + salesReportImportMonthlyList.size(), module);
                        for(GenericValue salesList : salesReportImportMonthlyList){
                            //Debug.logError("lll"+salesList.getString("salesReportId"), module);
                            BigDecimal sales = salesList.getBigDecimal("sales");
                            BigDecimal profit = salesList.getBigDecimal("profit");
                            sumSales = sumSales.add(sales);
                            sumProfit = sumProfit.add(profit);
                        }
                        dailySumSales = sumSales.divide(new BigDecimal(daily), 6);
                        dailySumProfit = sumProfit.divide(new BigDecimal(daily), 6);
                        sriMonthly.set("totalSales",dailySumSales);
                        sriMonthly.set("totalProfit",dailySumProfit);
                        delegator.createOrStore(sriMonthly);
                    }
                }
            }
        } catch (GenericEntityException e) {
            e.printStackTrace();
        }
        
        return ServiceUtil.returnSuccess();
    }   //calculateSalesReportImportMonthly
    
    public static Map<String, Object> calculateSalesSummary(DispatchContext dctx, Map<String, ? extends Object> context)
    throws Exception {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Timestamp nowTimestamp = Timestamp.valueOf(sdf.format(now.getTime()));
        Calendar calen = Calendar.getInstance();
        Timestamp lastMonth = Timestamp.valueOf(sdf.format(calen.getTime()));
        
        try{
            
            List<GenericValue> platformDepartmentList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap( "roleTypeId", "PLATFORM"), null, false);
            //Debug.logError("platformDepartmentList.Size: " + platformDepartmentList.size(), module);
            for (GenericValue depart : platformDepartmentList){//loop distinct depart group--start
                //Debug.logError("start loop depart group", module);
                String oneDepartment = depart.getString("partyId");
                //Debug.logError("oneDepartment: " + oneDepartment, module);
                List<GenericValue> findDepartOnePlatformList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("parentTypeId","PLATFORM","partyId",oneDepartment), null, false);
                
                //Debug.logError("findDepartOnePlatformList.Size: " + findDepartOnePlatformList.size(), module);
                for (GenericValue onePlatformList : findDepartOnePlatformList) {//loop distinct depart platform group -- start
                    
                    String onePlatform = onePlatformList.getString("roleTypeId");
                    //Debug.logError("onePlatform: " + onePlatform, module);
                    EntityCondition skuCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                 EntityCondition.makeCondition("department", EntityOperator.EQUALS, oneDepartment),
                                                                                                 EntityCondition.makeCondition("platform", EntityOperator.EQUALS, onePlatform)
                                                                                                 ));
                    List<GenericValue> findProductIdList =delegator.findList("PMGroupAndPM", skuCondition, null, null, null, false);
                    //Debug.logError("findProductIdList size: " + findProductIdList.size(), module);
                    for (GenericValue productList : findProductIdList) {// loop skuList--start
                        Long skuSalesCountList[] = new Long[31];
                        BigDecimal salesList[] = new BigDecimal[31];
                        for( int  count = 1;count <= 31; count++){
                            skuSalesCountList[count] = 0L;
                            salesList[count]=BigDecimal.ZERO;
                        }
                        
                        String oneSku = productList.getString("productId");
                        GenericValue salesSumTable = delegator.makeValue("SalesSummary", UtilMisc.toMap("productId",oneSku));
                        
                        String statusId = productList.getString("statusId");
                        salesSumTable.set("statusId",statusId);
                        salesSumTable.set("department",oneDepartment);
                        
                        salesSumTable.set("platform",onePlatform);
                        
                        String platformProductGroup = productList.getString("productGroup");
                        salesSumTable.set("platformProductGroup",platformProductGroup);
                        String chineseName = productList.getString("chineseName");
                        salesSumTable.set("chineseName",chineseName);
                        String ownerGroup = productList.getString("ownerGroup");
                        salesSumTable.set("ownerGroup",ownerGroup);
                        String mainSupplier = productList.getString("mainSupplier");
                        salesSumTable.set("mainSupplier",mainSupplier);
                        BigDecimal productCost = productList.getBigDecimal("actualPrice");
                        salesSumTable.set("productCost",productCost);
                        EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                  EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN, lastMonth),
                                                                                                  EntityCondition.makeCondition("date", EntityOperator.LESS_THAN_EQUAL_TO, nowTimestamp),
                                                                                                  EntityCondition.makeCondition("platformPartyId", EntityOperator.EQUALS, oneDepartment),
                                                                                                  EntityCondition.makeCondition("platform", EntityOperator.EQUALS, onePlatform),
                                                                                                  EntityCondition.makeCondition("productId", EntityOperator.EQUALS, oneSku)
                                                                                                  ));
                        
                        List<GenericValue> findsriDataList = delegator.findList("SalesReportImport", condition, null, null, null, false);
                        
                        /*
                         Debug.logError("nowTimestamp: " + nowTimestamp, module);
                         Debug.logError("oneDepartment: " + oneDepartment, module);
                         Debug.logError("onePlatform: " + onePlatform, module);
                         Debug.logError("oneSku: " + oneSku, module);
                         Debug.logError("Size: " + findsriDataList.size(), module);*/
                        for(GenericValue sriDataList : findsriDataList){//loop 7,15,30 days salesList--start
                            Long orderItemQuantity = sriDataList.getLong("orderItemQuantity");
                            BigDecimal sales = sriDataList.getBigDecimal("sales");
                            Calendar calendar = Calendar.getInstance();
                            for(int i = 1; i <= 31; i++){
                                calendar.add(Calendar.DAY_OF_MONTH, -i);
                                Timestamp sortDate = Timestamp.valueOf(sdf.format(calendar.getTime()));
                                if(sriDataList.getTimestamp("date").compareTo(sortDate)>0 ){
                                    skuSalesCountList[i] += orderItemQuantity;
                                    salesList[i]=salesList[i].add(sales);
                                }
                            }
                        }//loop 7,15,30 days salesList--end
                        
                        Long qty = 0L;
                        List<GenericValue> findInventoryDataList = delegator.findByAnd("GudaoInventory", UtilMisc.toMap("productId",oneSku), null,false);
                        for(GenericValue inventory : findInventoryDataList){
                            qty += inventory.getLong("qty");
                        }
                        salesSumTable.set("qty",qty);
                        BigDecimal qtyCount = new BigDecimal(qty);
                        BigDecimal inventoryAmount = BigDecimal.ZERO;
                        inventoryAmount = qtyCount.multiply(productCost);
                        salesSumTable.set("inventoryAmount",inventoryAmount);
                        
                        Timestamp productCreatedTime = productList.getTimestamp("pmCreatedStamp");
                        salesSumTable.set("productCreatedTime",productCreatedTime);
                        
                        delegator.createOrStore(salesSumTable);
                        
                        for(int k = 1;k <= 31; k++){
                            
                            GenericValue salesSumDetailTable = delegator.makeValue("SalesSummaryDetail", UtilMisc.toMap("productId",oneSku));
                            salesSumDetailTable.set("department",oneDepartment);
                            salesSumDetailTable.set("platform",onePlatform);
                            salesSumDetailTable.set("salesDays",Long.valueOf(k));
                            salesSumDetailTable.set("qtySold",skuSalesCountList[k]);
                            salesSumDetailTable.set("sales",salesList[k]);
                            
                            long qtyConvertDayCount=0;
                            if(skuSalesCountList[k] !=0){
                                qtyConvertDayCount = (long)qty *Long.valueOf(k) / skuSalesCountList[k];
                            }else{
                                qtyConvertDayCount = (long)qty * Long.valueOf(k);
                            }
                            salesSumDetailTable.set("qtyConvertDayCount",qtyConvertDayCount);
                            delegator.createOrStore(salesSumDetailTable);
                        }
                        
                    }//if pm statsId!= discontinued--end
                }//loop distinct depart platform group -- end
            }//loop distinct depart group --end
            
            
        } catch (GenericEntityException e) {
            e.printStackTrace();
        }
        
        return ServiceUtil.returnSuccess();
    }   //calculateSalesSummary
    
    public static Map<String, Object> rivalMonitorGetPriceSingle (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String itemId = (String) context.get("itemId");
        
        Map result = ServiceUtil.returnSuccess();
        
        try {
            
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", "09lijun"), false);
            Map mapAccount = FastMap.newInstance();
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "GetItem");
            mapAccount.put("productStoreId", productStore.getString("productStoreId"));
            
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
            UtilXml.addChildElementValue(rootElem, "IncludeItemSpecifics", "true", rootDoc);
            //Building XML -- END
            
            //Write REQUEST == END
            
            String requestXML = UtilXml.writeXmlDocument(rootDoc);
            //Debug.logError(requestXML, module);
            
            String responseXML = eBayTradingAPI.sendRequestXMLToEbay(mapAccount, requestXML);
            
            //Debug.logError(responseXML, module);
            Map<String, Object> getRivalListingData = rivalMonitorGetPriceDatabaseWrite(dctx, mapAccount, responseXML);
            //Debug.logError("Finished writing database", module);
            result.put("site", getRivalListingData.get("site"));
            result.put("ack",getRivalListingData.get("ack"));
            result.put("shortMessage",getRivalListingData.get("shortMessage"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }   //rivalMonitorGetPriceSingle
    
    public static Map<String, Object> rivalMonitorGetPriceDatabaseWrite(DispatchContext dctx, Map mapContent, String responseXML)
    throws GenericEntityException, GenericServiceException, SAXException, ParserConfigurationException, IOException { //writeIntoGetSellerListData
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        //formatting the sequence ID
        DecimalFormat df = new DecimalFormat("00000");
        String productStoreId = mapContent.get("productStoreId").toString();
        Date nowDate = new Date();
        String site = "US";
        String ack ="Failure";
        try {   //main try -- START
            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
            Element elemResponse = docResponse.getDocumentElement();
            ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
            //Debug.logError("ack " + ack, module);
            String hasMoreItems = UtilXml.childElementValue(elemResponse, "HasMoreItems", null);
            String pageNumber = UtilXml.childElementValue(elemResponse, "PageNumber", null);
            
            if (ack.equals("Success") || ack.equals("Warning")) {    //if ack success -- START
                //Debug.logError("Actual writeIntoGetSellerListData pageNumber " + pageNumber, module);
                List<? extends Element> itemArray = UtilXml.childElementList(elemResponse, "Item");
                Iterator<? extends Element> itemArrayElemIter = itemArray.iterator();
                if (UtilValidate.isEmpty(itemArray)) {
                    Debug.logError("itemArray is null", module);
                }
                while (itemArrayElemIter.hasNext()) {   //loop items Element -- START
                    Map itemMap = FastMap.newInstance();
                    Element itemElement = itemArrayElemIter.next();
                    String itemId = UtilXml.childElementValue(itemElement, "ItemID", null);
                    itemMap.put("itemId", UtilXml.childElementValue(itemElement, "ItemID", null));
                    itemMap.put("date",new java.sql.Date(nowDate.getTime()));
                    itemMap.put("country", UtilXml.childElementValue(itemElement, "Country", null));
                    itemMap.put("hitCount", UtilXml.childElementValue(itemElement, "HitCount", "0"));
                    
                    //Listing Details -- START
                    Element listingDetailsElement = UtilXml.firstChildElement(itemElement, "ListingDetails");
                    itemMap.put("listDetStartTime", UtilXml.childElementValue(listingDetailsElement, "StartTime", null));
                    //Listing Details -- END
                    
                    itemMap.put("listingDuration", UtilXml.childElementValue(itemElement, "ListingDuration", null));
                    itemMap.put("listingType", UtilXml.childElementValue(itemElement, "ListingType", null));
                    itemMap.put("location", UtilXml.childElementValue(itemElement, "Location", null));
                    
                    //Primary Category -- START
                    Element primaryCategoryElement = UtilXml.firstChildElement(itemElement, "PrimaryCategory");
                    itemMap.put("primaryCategoryId", UtilXml.childElementValue(primaryCategoryElement, "CategoryID", null));
                    itemMap.put("primaryCategoryName", UtilXml.childElementValue(primaryCategoryElement, "CategoryName", null));
                    //Primary Category -- END
                    
                    itemMap.put("quantity", UtilXml.childElementValue(itemElement, "Quantity", null));
                    
                    //Secondary Category -- START
                    Element secondaryCategoryElement = UtilXml.firstChildElement(itemElement, "SecondaryCategory");
                    itemMap.put("secondaryCategoryId", UtilXml.childElementValue(secondaryCategoryElement, "CategoryID", null));
                    itemMap.put("secondaryCategoryName", UtilXml.childElementValue(secondaryCategoryElement, "CategoryName", null));
                    //Secondary Category -- END
                    //Seller-- START
                    Element sellerElement = UtilXml.firstChildElement(itemElement, "Seller");
                    itemMap.put("ebayUserId", UtilXml.childElementValue(sellerElement, "UserID", null));
                    
                    //Seller -- START
                    //Selling Status -- START
                    Element sellingStatusElement = UtilXml.firstChildElement(itemElement, "SellingStatus");
                    String ebayListingStatus = UtilXml.childElementValue(sellingStatusElement, "ListingStatus", null);
                    itemMap.put("listingStatus", ebayListingStatus);
                    if (ebayListingStatus.equals("Completed")) {
                        ack = "COMPLETED";
                    }
                    
                    //Selling Status - PromotionalSaleDetails -- START
                    Element promotionalSaleDetails = UtilXml.firstChildElement(sellingStatusElement, "PromotionalSaleDetails");
                    itemMap.put("promoEndTime", UtilXml.childElementValue(promotionalSaleDetails, "EndTime", null));
                    itemMap.put("promoOriginalPrice", UtilXml.childElementValue(promotionalSaleDetails, "OriginalPrice", null));
                    itemMap.put("promoOriginalPriceCurId", UtilXml.childElementAttribute(promotionalSaleDetails, "OriginalPrice", "currencyID", null));
                    itemMap.put("promoStartTime", UtilXml.childElementValue(promotionalSaleDetails, "StartTime", null));
                    //Selling Status - PromotionalSaleDetails -- END
                    
                    itemMap.put("quantitySold", UtilXml.childElementValue(sellingStatusElement, "QuantitySold", null));
                    //Selling Status -- END
                    
                    
                    Element shippingDetails = UtilXml.firstChildElement(itemElement, "ShippingDetails");
                    //ShippingDetails>InternationalShippingServiceOption -- START
                    Element internationalShippingServiceOptionElement = UtilXml.firstChildElement(shippingDetails, "InternationalShippingServiceOption");
                    itemMap.put("shipping",UtilXml.childElementValue(internationalShippingServiceOptionElement, "ShippingServiceCost", "0"));
                    //ShippingDetails>InternationalShippingServiceOption -- END
                    
                    itemMap.put("sku", UtilXml.childElementValue(itemElement, "SKU", null));
                    itemMap.put("quantity", UtilXml.childElementValue(itemElement, "Quantity", null));
                    itemMap.put("price", UtilXml.childElementValue(itemElement, "StartPrice", null));
                    itemMap.put("currency", UtilXml.childElementAttribute(itemElement, "StartPrice", "currencyID", null));
                    
                    //Storefront -- START
                    Element storefront = UtilXml.firstChildElement(itemElement, "Storefront");
                    itemMap.put("storefrontUrl", StringEscapeUtils.unescapeHtml(UtilXml.childElementValue(storefront, "StoreURL", null)));
                    //Storefront -- END
                    itemMap.put("title", UtilXml.childElementValue(itemElement, "Title", null));
                    String ebaySite = UtilXml.childElementValue(itemElement, "Site", null);
                    List<GenericValue> ebaySiteGV = delegator.findByAnd("EbaySiteCode", UtilMisc.toMap("ebaySite", ebaySite),null, false);
                    if (UtilValidate.isNotEmpty(ebaySiteGV)) {
                        site = EntityUtil.getFirst(ebaySiteGV).getString("abbreviation");
                    }
                    itemMap.put("site", site);
                    
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
                            variationMap.put("sku", productIdVariation);
                            variationMap.put("startPrice", UtilXml.childElementValue(variationElement, "StartPrice", null));
                            variationMap.put("startPriceCurrencyId", UtilXml.childElementAttribute(variationElement, "StartPrice", "currencyID", null));
                            variationMap.put("quantity", UtilXml.childElementValue(variationElement, "Quantity", null));
                            //SellingStatus -- START
                            Element sellingStatusVarElement = UtilXml.firstChildElement(variationElement, "SellingStatus");
                            variationMap.put("quantitySold", UtilXml.childElementValue(sellingStatusVarElement, "QuantitySold", null));
                            //SellingStatus > PromotionalSaleDetails -- START
                            Element promoVarElement = UtilXml.firstChildElement(sellingStatusVarElement, "PromotionalSaleDetails");
                            
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
                                    varSpecsMap.put("variationName", UtilXml.childElementValue(nameValueListElement, "Name", null));
                                    varSpecsMap.put("variationValue", UtilXml.childElementValue(nameValueListElement, "Value", null));
                                    
                                    //Writing to Database (ListingVariationSpecifics) -- START
                                    GenericValue listingVariationSpecifics = delegator.makeValue("RivalListingVariationSpec", UtilMisc.toMap("variationSeqId", variationSeqId));
                                    listingVariationSpecifics.set("itemId", itemId);
                                    listingVariationSpecifics.set("date", new java.sql.Date(nowDate.getTime()));
                                    listingVariationSpecifics.set("variationName", varSpecsMap.get("variationName"));
                                    listingVariationSpecifics.set("variationValue", varSpecsMap.get("variationValue"));
                                    delegator.createOrStore(listingVariationSpecifics);
                                    //Writing to Database (ListingVariationSpecifics) -- END
                                }   //loop nameValueListElemIter -- END
                            }   //loop variationSpecificsElemIter -- END
                            //VariationSpecifics -- END
                            
                            //Writing to Database (EbayActiveListingVariation) -- START
                            GenericValue ebayActiveListingVariation = delegator.makeValue("RivalListingVariation", UtilMisc.toMap("itemId", itemId));
                            ebayActiveListingVariation.set("date", new java.sql.Date(nowDate.getTime()));
                            ebayActiveListingVariation.set("variationSeqId", variationSeqId);
                            ebayActiveListingVariation.set("sku", productIdVariation);
                            if (variationMap.get("startPriceCurrencyId") != null) { ebayActiveListingVariation.set("startPriceCurrencyId", variationMap.get("startPriceCurrencyId")); }
                            if (variationMap.get("startPrice") != null) { ebayActiveListingVariation.set("startPrice", new BigDecimal(variationMap.get("startPrice").toString())); }
                            
                            if (variationMap.get("quantity") != null) { ebayActiveListingVariation.set("quantity", Long.valueOf(variationMap.get("quantity").toString()) - Long.valueOf(variationMap.get("quantitySold").toString())); }
                            if (variationMap.get("quantitySold") != null) { ebayActiveListingVariation.set("quantitySold", Long.valueOf(variationMap.get("quantitySold").toString())); }
                            if (ebayActiveListingVariation.get("quantity") == null) { ebayActiveListingVariation.set("quantity", Long.valueOf(0)); }
                            delegator.createOrStore(ebayActiveListingVariation);
                            //Writing to Database (EbayActiveListingVariation) -- END
                        }   //loop variationElemIter -- END
                    }   //if variationsElement is not empty -- END
                    //Variations -- END
                    
                    itemMap.put("dispatchTimeMax", UtilXml.childElementValue(itemElement, "DispatchTimeMax", null));
                    
                    //Writing to Database (getSellerListData)-- Start
                    
                    GenericValue getSellerListData = delegator.makeValue("RivalListingMonitor", UtilMisc.toMap("itemId", itemMap.get("itemId")));
                    getSellerListData.set("date", new java.sql.Date(nowDate.getTime()));
                    if (itemMap.get("price") != null) { getSellerListData.set("price", new BigDecimal(itemMap.get("price").toString())); }
                    if (itemMap.get("country") != null) { getSellerListData.set("country", itemMap.get("country")); }
                    if (itemMap.get("currency") != null) { getSellerListData.set("currency", itemMap.get("currency")); }
                    if (itemMap.get("hitCount") != null) { getSellerListData.set("hitCount", Long.valueOf(itemMap.get("hitCount").toString())); }
                    if (itemMap.get("listDetStartTime") != null) { getSellerListData.set("listDetStartTime", itemMap.get("listDetStartTime")); }
                    if (itemMap.get("listingDuration") != null) { getSellerListData.set("listingDuration", itemMap.get("listingDuration")); }
                    if (itemMap.get("listingType") != null) { getSellerListData.set("listingType", itemMap.get("listingType")); }
                    if (itemMap.get("location") != null) { getSellerListData.set("location", itemMap.get("location")); }
                    if (itemMap.get("primaryCategoryId") != null) { getSellerListData.set("primaryCategoryId", itemMap.get("primaryCategoryId")); }
                    if (itemMap.get("primaryCategoryName") != null) { getSellerListData.set("primaryCategoryName", itemMap.get("primaryCategoryName")); }
                    if (itemMap.get("secondaryCategoryId") != null) { getSellerListData.set("secondaryCategoryId", itemMap.get("secondaryCategoryId")); }
                    if (itemMap.get("secondaryCategoryName") != null) { getSellerListData.set("secondaryCategoryName", itemMap.get("secondaryCategoryName")); }
                    if (itemMap.get("listingStatus") != null) { getSellerListData.set("listingStatus", itemMap.get("listingStatus")); }
                    if (itemMap.get("promoEndTime") != null) { getSellerListData.set("promoEndTime", itemMap.get("promoEndTime")); }
                    if (itemMap.get("promoStartTime") != null) { getSellerListData.set("promoStartTime", itemMap.get("promoStartTime")); }
                    if (itemMap.get("sku") != null) { getSellerListData.set("sku", itemMap.get("sku")); }
                    if (itemMap.get("storefrontUrl") != null) { getSellerListData.set("storefrontUrl", itemMap.get("storefrontUrl")); }
                    if (itemMap.get("title") != null) { getSellerListData.set("title", itemMap.get("title")); }
                    if (itemMap.get("site") != null) { getSellerListData.set("site", itemMap.get("site")); }
                    if (itemMap.get("dispatchTimeMax") != null) { getSellerListData.set("dispatchTimeMax", Long.valueOf(itemMap.get("dispatchTimeMax").toString())); }
                    if (itemMap.get("originalPriceCurId") != null) { getSellerListData.set("originalPriceCurrencyId", itemMap.get("originalPriceCurrencyId")); }
                    if (itemMap.get("originalPrice") != null) { getSellerListData.set("originalPrice", new BigDecimal(itemMap.get("originalPrice").toString())); }
                    if (itemMap.get("ebayUserId") != null) { getSellerListData.set("ebayUserId", itemMap.get("ebayUserId").toString()); }
                    if (itemMap.get("quantitySold") != null) { getSellerListData.set("quantitySold", Long.valueOf(itemMap.get("quantitySold").toString())); }
                    if (itemMap.get("quantity") != null) { getSellerListData.set("quantity", Long.valueOf(itemMap.get("quantity").toString())); }
                    if (itemMap.get("shipping") != null) { getSellerListData.set("shipping", new BigDecimal(itemMap.get("shipping").toString())); }
                    getSellerListData.set("hasVariation", hasVariation);
                    delegator.createOrStore(getSellerListData);
                    
                    //Writing to Database (getSellerListData) -- END
                }   //loop items Element -- END
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
                result.put("shortMessage",errorMessage.toString());
                //return ServiceUtil.returnError("writeIntoGetSellerListData gives error from ResponseXML: " + errorMessage);
            }   //if ack failure -- END
        } //main try -- END
        catch (GenericEntityException e) {
            Debug.logError("Yasin: writeIntoGetSellerListData GenericEntityException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (SAXException e) {
            Debug.logError("Yasin: writeIntoGetSellerListData SAXException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (ParserConfigurationException e) {
            Debug.logError("Yasin: writeIntoGetSellerListData ParserConfigurationException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (IOException e) {
            Debug.logError("Yasin: writeIntoGetSellerListData IOException Error for TRY CATCH: " + e.getMessage(), module);
        }
        catch (Exception e) {
            e.printStackTrace();
            Debug.logError("Yasin: writeIntoGetSellerListData Exception Error for TRY CATCH: " + e.getMessage(), module);
        }
        //Debug.logError("ack:"+ack,module);
        result.put("site", site);
        result.put("ack",ack);
        return result;
    }   //rivalMonitorGetPriceDatabaseWrite
    
    public static Map<String, Object> rivalMonitorGetPriceAll (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String motherSku = (String) context.get("motherSku");
        Map result = ServiceUtil.returnSuccess();
        
        try {
            List<EntityCondition> conditionList = FastList.newInstance();
            if(UtilValidate.isNotEmpty(motherSku)){
                conditionList.add(EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, motherSku));
            }
            conditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
            conditionList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"));
            conditionList.add(EntityCondition.makeCondition("type", EntityOperator.EQUALS, "RIVAL"));
            EntityCondition condition = null;
            if (conditionList.size() > 0) {
                condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
            }
            List<GenericValue> distinctItemIdList = delegator.findList("ProductMonitoringList",
                                                                       condition,
                                                                       UtilMisc.toSet("itemId"),
                                                                       UtilMisc.toList("itemId"),
                                                                       new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),  false);
            
            for(GenericValue itemIdList : distinctItemIdList){
                String itemId = itemIdList.getString("itemId");
                List<GenericValue> findUserLoginIdList = delegator.findByAnd("ProductMonitoringList", UtilMisc.toMap("itemId",itemId), null,  false);
                for(GenericValue userList : findUserLoginIdList){
                    String oneUserLogin = userList.getString("userLoginId");
                    String site = userList.getString("site");
                    Map<String, Object> test = dispatcher.runSync("rivalMonitorGetPriceSingle", UtilMisc.toMap("itemId", itemId, "userLogin", userLogin));
                    if (ServiceUtil.isError(test)) {
                        Debug.logError("asd",module);
                    }
                    String ack = (String) test.get("ack");
                    if(ack.equals("Failure")){
                        String shortMessage = (String) test.get("shortMessage");
                        
                        if(shortMessage.contains("Item cannot be accessed.")){
                            List<GenericValue> infringementList = delegator.findByAnd("ProductMonitoringList", UtilMisc.toMap("itemId",itemId), null,false);
                            for(GenericValue infringement : infringementList){
                                infringement.set("statusId","VERO");
                                delegator.store(infringement);
                            }
                        }else{
                            List<GenericValue> failureDataList = delegator.findByAnd("ProductMonitoringList", UtilMisc.toMap("itemId",itemId), null,false);
                            for(GenericValue failure : failureDataList){
                                failure.set("statusId","INVALID");
                                delegator.store(failure);	
                            }
                        }
                    } else if (ack.equals("COMPLETED")) {
                        List<GenericValue> completedList = delegator.findByAnd("ProductMonitoringList", UtilMisc.toMap("itemId",itemId), null,false);
                        for(GenericValue completed : completedList){
                            completed.set("statusId","COMPLETED");
                            delegator.store(completed);
                        }
                    }/* else {    //if ack is SUCCESS OR WARNING == START
                        String resultSite = (String) test.get("site");
                        if (!site.equals(resultSite)) {  //if input site is not equal result site == START
                            userList.set("site", resultSite);
                            delegator.store(userList);
                        }   //if input site is not equal result site == END
                    }   //if ack is SUCCESS OR WARNING == END*/
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }   //rivalMonitorGetPriceAll
    
    public static Map<String, Object> gudaoMonitorGetPriceAll (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String motherSkuInput = (String) context.get("motherSku");
        Map result = ServiceUtil.returnSuccess();
        
        try {
            List<EntityCondition> conditionList = FastList.newInstance();
            if(UtilValidate.isNotEmpty(motherSkuInput)){
                motherSkuInput = motherSkuInput.trim();
                conditionList.add(EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, motherSkuInput));
            }
            conditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
            conditionList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"));
            conditionList.add(EntityCondition.makeCondition("type", EntityOperator.EQUALS, "GUDAO"));
            EntityCondition condition = null;
            if (conditionList.size() > 0) {
                condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
            }
            
            List<GenericValue> distinctMotherSkuList = delegator.findList("ProductMonitoringList", condition,
                                                                          UtilMisc.toSet("motherSku"),
                                                                          UtilMisc.toList("motherSku"),
                                                                          new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),  false);
            
            
            for (GenericValue distinctMotherSku : distinctMotherSkuList) {  //loop distinctMotherSkuList == START
                String motherSku = distinctMotherSku.getString("motherSku");
                
                List<GenericValue> pmList = delegator.findByAnd("ProductMaster", UtilMisc.toMap("motherSku",motherSku), null, false);
                List<String> childrenSkuList = new ArrayList<String>();
                for(GenericValue pm : pmList){
                    childrenSkuList.add(pm.getString("productId"));
                }
                
                List<GenericValue> indoEbayAccountListGV = delegator.findList("IndoEbayAccountList", null, null, null, null, false);
                List<String> indoEbayAccountList = new ArrayList<String>();
                for (GenericValue indoEbayAccountGV : indoEbayAccountListGV) {  //loop indoEbayAccountListGV == START
                    indoEbayAccountList.add(indoEbayAccountGV.getString("accountName"));
                }   //loop indoEbayAccountListGV == END
                
                List<String> itemIdListArray = new ArrayList<String>();
                EntityCondition itemIdCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                EntityCondition.makeCondition("normalizedSku", EntityOperator.IN, childrenSkuList),
                                                                                                EntityCondition.makeCondition("productStoreId", EntityOperator.IN, indoEbayAccountList)
                                                                                                ));
                List<GenericValue> itemIdList = delegator.findList("EbayActiveListing", itemIdCondition, null, null, null, false);
                for (GenericValue item : itemIdList) {
                    itemIdListArray.add(item.getString("itemId"));
                }
                itemIdList = delegator.findList("EbayActiveListingVariation", itemIdCondition, null, null, null, false);
                for(GenericValue item : itemIdList){
                    itemIdListArray.add(item.getString("itemId"));
                }
                HashSet<String> uniqueItemIdList = new HashSet<String>(itemIdListArray);
                Debug.logError("uniqueItemIdList:"+uniqueItemIdList,module);
                
                EntityCondition userLoginCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                   EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, motherSku),
                                                                                                   EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"),
                                                                                                   EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"),
                                                                                                   EntityCondition.makeCondition("type", EntityOperator.EQUALS, "GUDAO")
                                                                                                   ));
                List<GenericValue> prodMonDistinctUserLoginList = delegator.findList("ProductMonitoringList", userLoginCondition,
                                                                                     UtilMisc.toSet("userLoginId"),
                                                                                     UtilMisc.toList("userLoginId"),
                                                                                     new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),  false);
                List<String> userLoginList = new ArrayList<String>();
                for (GenericValue prodMonDistinctUserLogin : prodMonDistinctUserLoginList) { //loop prodMonDistinctUserLoginList == START
                    userLoginList.add(prodMonDistinctUserLogin.getString("userLoginId"));
                }   //loop prodMonDistinctUserLoginList == END
                
                for (String uniqueItemId : uniqueItemIdList) {  //loop uniqueItemIdList == START
                    Map<String, Object> rivalMonitorGetPriceSingle = dispatcher.runSync("rivalMonitorGetPriceSingle", UtilMisc.toMap("itemId", uniqueItemId, "userLogin", userLogin));
                    String crawlSite = (String) rivalMonitorGetPriceSingle.get("site");
                    String ack = (String) rivalMonitorGetPriceSingle.get("ack");
                    String statusId = "ACTIVE";
                    if (ack.equals("COMPLETED")) {
                        statusId = "COMPLETED";
                    }
                    
                    List<GenericValue> newProductMonitoringList = delegator.findByAnd("ProductMonitoringList", UtilMisc.toMap("itemId","NEW","motherSku",motherSku, "site", crawlSite), null, false);
                    if(UtilValidate.isNotEmpty(newProductMonitoringList)){
                        //Debug.logError("newProductMonitoringList is not empty, deleting the NEW itemID", module);
                        delegator.removeByAnd("ProductMonitoringList", UtilMisc.toMap("itemId","NEW","motherSku", motherSku, "site", crawlSite));
                    }
                    
                    for (String userLoginStr : userLoginList) { //loop userLoginList == START
                        GenericValue userLoginGV = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", userLoginStr), false);
                        String userLoginSite = userLoginGV.getString("externalAuthId");
                        if (userLoginSite.equals(crawlSite)) {  //if site matches == START
                            GenericValue updateItemId = delegator.makeValue("ProductMonitoringList", UtilMisc.toMap("motherSku", motherSku, "userLoginId", userLoginStr, "itemId", uniqueItemId));
                            updateItemId.set("site", crawlSite);
                            updateItemId.set("platform", "EBAY");
                            updateItemId.set("type", "GUDAO");
                            updateItemId.set("statusId", statusId);
                            delegator.createOrStore(updateItemId);
                        }   //if site matches == END
                    }   //loop userLoginList == END
                }   //loop uniqueItemIdList == END
            }   //loop distinctMotherSkuList == END
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }   //gudaoMonitorGetPriceAll
    
    public static Map<String, Object> calculatePriceMonitorView (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String groupInput = (String) context.get("group");
        String motherSkuInput = (String) context.get("motherSku");
        Map result = ServiceUtil.returnSuccess();
        
        Date nowDate = new Date();
        java.sql.Date today = new java.sql.Date(nowDate.getTime());
        java.sql.Date yesterday = new java.sql.Date(nowDate.getTime() - 1 * 24 * 3600 * 1000);
        java.sql.Date weekAgo = new java.sql.Date(nowDate.getTime() - 7 * 24 * 3600 * 1000);
        
        try {   //main try == START
            if (UtilValidate.isNotEmpty(groupInput)) {
                groupInput = groupInput.trim().toUpperCase();
                if (!groupInput.contains("GROUP")) {
                    groupInput = groupInput + "_GROUP";
                }
            }

            GenericValue partyGroupGV = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", groupInput), false);
            if (UtilValidate.isNotEmpty(partyGroupGV)) {    //if partyGroupGV is not empty == START
                List<GenericValue> userLoginList = delegator.findList("UserLogin",
                                                                      EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                                    EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, groupInput),
                                                                                                                    EntityCondition.makeCondition("externalAuthId", EntityOperator.NOT_EQUAL, "ADMIN")
                                                                                                                 )
                                                                                                    ),
                                                                      null, null, null, false);
                
                //Debug.logError("UserLoginList: " + userLoginList, module);
                for (GenericValue userLoginGV : userLoginList) {  //loop userLoginList == START
                    String site = userLoginGV.getString("externalAuthId");
                    String userLoginId = userLoginGV.getString("userLoginId");
                    //Debug.logError("Start looping userLoginId: " + userLoginId + " with site: " + site, module);
                    
                    //clearing productMonViewHeader == START
                    delegator.removeByAnd("ProductMonViewHeader", UtilMisc.toMap("userLoginId", userLoginId));
                    //clearing productMonViewHeader == END
                    
                    //clearing productMonViewItem == START
                    delegator.removeByAnd("ProductMonViewItem", UtilMisc.toMap("userLoginId", userLoginId));
                    //clearing productMonViewItem == END
                    
                    List<EntityCondition> distinctMotherSkuConditionList = FastList.newInstance();
                    distinctMotherSkuConditionList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginId));
                    distinctMotherSkuConditionList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"));
                    distinctMotherSkuConditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
                    
                    if (UtilValidate.isNotEmpty(motherSkuInput)) {
                        distinctMotherSkuConditionList.add(EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, motherSkuInput));
                    }
                    
                    List<GenericValue> distinctMotherSkuList = delegator.findList("ProductMonitoringList",
                                                                                  EntityCondition.makeCondition(distinctMotherSkuConditionList),
                                                                                  UtilMisc.toSet("motherSku"),
                                                                                  UtilMisc.toList("motherSku"),
                                                                                  new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                                                                  false);
                    //Debug.logError("distinctMotherSkuList: " + distinctMotherSkuList, module);
                    for (GenericValue distinctMotherSku : distinctMotherSkuList) {  //loop distinctMotherSkuList == START
                        String motherSku = distinctMotherSku.getString("motherSku");
                        boolean mainHasAction = false;
                        //Debug.logError("Start looping motherSku: " + motherSku, module);
                        String lowestItemId = null;
                        BigDecimal lowestPrice = new BigDecimal("10000000");
                        List<GenericValue> rivalList = delegator.findList("ProductMonitoringList",
                                                                          EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                                        EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, motherSku),
                                                                                                                        EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"),
                                                                                                                        EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"),
                                                                                                                        EntityCondition.makeCondition("site", EntityOperator.EQUALS, site),
                                                                                                                        EntityCondition.makeCondition("type", EntityOperator.EQUALS, "RIVAL")
                                                                                                                        )
                                                                                                        ),
                                                                          null, null, null,false);
                        List<GenericValue> gudaoList = delegator.findList("ProductMonitoringList",
                                                                          EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                                        EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, motherSku),
                                                                                                                        EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"),
                                                                                                                        EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"),
                                                                                                                        EntityCondition.makeCondition("site", EntityOperator.EQUALS, site),
                                                                                                                        EntityCondition.makeCondition("type", EntityOperator.EQUALS, "GUDAO"),
                                                                                                                        EntityCondition.makeCondition("itemId", EntityOperator.NOT_EQUAL, "NEW")
                                                                                                                        )
                                                                                                        ),
                                                                          null, null, null,false);
                        //Debug.logError("RivalList: " + rivalList, module);
                        //Debug.logError("GudaoList: " + gudaoList, module);
                        //rival == START
                        for (GenericValue rival : rivalList) {  //loop rivalList == START
                            String rivalItemId = rival.getString("itemId");
                            BigDecimal minPrice = new BigDecimal("1000000");
                            int todayQty = 0;
                            int yesterdayQty = 0;
                            int weekQty = 0;
                            //Debug.logError("RivalList itemId: " + rivalItemId, module);
                            List<GenericValue> rivalListToday = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",rivalItemId,"date",today,"hasVariation","N"), null,false);
                            List<GenericValue> rivalListingYesterday = delegator.findByAnd("RivalListingMonitor",UtilMisc.toMap("itemId",rivalItemId,"date",yesterday,"hasVariation","N"),null,false);
                            List<GenericValue> rivalListingWeek = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",rivalItemId,"date",weekAgo,"hasVariation","N"),null, false);
                            
                            if (UtilValidate.isNotEmpty(rivalListToday)) {  //rival does not have variation == START
                                GenericValue rivalListTodayGV = EntityUtil.getFirst(rivalListToday);
                                todayQty = rivalListTodayGV.getLong("quantitySold").intValue();
                                BigDecimal productPrice = rivalListTodayGV.getBigDecimal("price");
                                BigDecimal shippingPrice = rivalListTodayGV.getBigDecimal("shipping");
                                minPrice = productPrice.add(shippingPrice);
                            }   //rival does not have variation == END
                            else {  //rival has variations == START
                                rivalListToday = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",rivalItemId,"date",today), null, false);
                                for(GenericValue rivalListGV : rivalListToday) {  //loop rivalListToday == START
                                    todayQty += rivalListGV.getLong("quantitySold").intValue();
                                    BigDecimal startPrice = rivalListGV.getBigDecimal("startPrice");
                                    if (minPrice.compareTo(startPrice) >= 0) {
                                        minPrice = startPrice;
                                    }
                                }   //loop rivalListToday == END
                                GenericValue rivalListHeader = delegator.findOne("RivalListingMonitor", UtilMisc.toMap("itemId",rivalItemId,"date",today), false);
                                minPrice = minPrice.add(rivalListHeader.getBigDecimal("shipping"));
                            }   //rival has variations == START
                            
                            if (UtilValidate.isNotEmpty(rivalListingYesterday)) {   //if rivalListingYesterday is not empty == START
                                GenericValue rivalListingYesterdayGV = EntityUtil.getFirst(rivalListingYesterday);
                                yesterdayQty = rivalListingYesterdayGV.getLong("quantitySold").intValue();
                            }   //if rivalListingYesterday is not empty == END
                            else {  //if rivalListingYesterday is empty (has variation) == START
                                rivalListingYesterday = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",rivalItemId,"date",yesterday), null, false);
                                for(GenericValue rivalListGV : rivalListingYesterday) {  //loop rivalListingYesterday == START
                                    yesterdayQty += rivalListGV.getLong("quantitySold").intValue();
                                }   //loop rivalListingYesterday == END
                            }   //if rivalListingYesterday is empty (has variation) == END
                            
                            if (UtilValidate.isNotEmpty(rivalListingWeek)) {   //if rivalListingWeek is not empty == START
                                GenericValue rivalListingWeekGV = EntityUtil.getFirst(rivalListingWeek);
                                weekQty = rivalListingWeekGV.getLong("quantitySold").intValue();
                            }   //if rivalListingWeek is not empty == END
                            else {  //if rivalListingWeek is empty (has variation) == START
                                rivalListingWeek = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",rivalItemId,"date",weekAgo), null, false);
                                for(GenericValue rivalListGV : rivalListingWeek) {  //loop rivalListingWeek == START
                                    weekQty += rivalListGV.getLong("quantitySold").intValue();
                                }   //loop rivalListingWeek == END
                            }   //if rivalListingWeek is empty (has variation) == END
                            
                            if (lowestPrice.compareTo(minPrice) >= 0) { //if rival item is lowest price == START
                                lowestPrice = minPrice;
                                lowestItemId = rivalItemId;
                            }   //if rival item is lowest price == END
                            
                            //update ProductMonViewItem == START
                            GenericValue productMonViewItem = delegator.makeValue("ProductMonViewItem", UtilMisc.toMap("userLoginId", userLoginId, "motherSku", motherSku, "itemId", rivalItemId));
                            productMonViewItem.set("site", site);
                            productMonViewItem.set("type", "RIVAL");
                            productMonViewItem.set("oneDaySold", Long.valueOf(todayQty - yesterdayQty));
                            productMonViewItem.set("sevenDaySold", Long.valueOf(todayQty - weekQty));
                            productMonViewItem.set("startPrice", minPrice);
                            productMonViewItem.set("statusId", "ACTIVE");
                            delegator.createOrStore(productMonViewItem);
                            //update ProductMonViewItem == END
                        }   //loop rivalList == END
                        //rival == END
                        
                        //gudao == START
                        for (GenericValue gudao : gudaoList) {  //loop gudaoList == START
                            String gudaoItemId = gudao.getString("itemId");
                            BigDecimal minPrice = new BigDecimal("1000000");
                            boolean hasAction = false;
                            int todayQty = 0;
                            int yesterdayQty = 0;
                            int weekQty = 0;
                            //Debug.logError("GudaoList itemId: " + gudaoItemId, module);
                            List<GenericValue> gudaoListToday = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",gudaoItemId,"date",today,"hasVariation","N"), null,false);
                            List<GenericValue> gudaoListYesterday = delegator.findByAnd("RivalListingMonitor",UtilMisc.toMap("itemId",gudaoItemId,"date",yesterday,"hasVariation","N"),null,false);
                            List<GenericValue> gudaoListWeek = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",gudaoItemId,"date",weekAgo,"hasVariation","N"),null, false);
                            
                            if (UtilValidate.isNotEmpty(gudaoListToday)) {  //gudao does not have variation == START
                                GenericValue gudaoListTodayGV = EntityUtil.getFirst(gudaoListToday);
                                todayQty = gudaoListTodayGV.getLong("quantitySold").intValue();
                                BigDecimal productPrice = gudaoListTodayGV.getBigDecimal("price");
                                BigDecimal shippingPrice = gudaoListTodayGV.getBigDecimal("shipping");
                                minPrice = productPrice.add(shippingPrice);
                            }   //gudao does not have variation == END
                            else {  //gudao has variations == START
                                gudaoListToday = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",gudaoItemId,"date",today), null, false);
                                for(GenericValue gudaoListGV : gudaoListToday) {  //loop gudaoListToday == START
                                    todayQty += gudaoListGV.getLong("quantitySold").intValue();
                                    BigDecimal startPrice = gudaoListGV.getBigDecimal("startPrice");
                                    if (minPrice.compareTo(startPrice) >= 0) {
                                        minPrice = startPrice;
                                    }
                                }   //loop gudaoListToday == END
                                GenericValue gudaoListHeader = delegator.findOne("RivalListingMonitor", UtilMisc.toMap("itemId",gudaoItemId,"date",today), false);
                                minPrice = minPrice.add(gudaoListHeader.getBigDecimal("shipping"));
                            }   //gudao has variations == START
                            
                            if (UtilValidate.isNotEmpty(gudaoListYesterday)) {   //if gudaoListYesterday is not empty == START
                                GenericValue gudaoListYesterdayGV = EntityUtil.getFirst(gudaoListYesterday);
                                yesterdayQty = gudaoListYesterdayGV.getLong("quantitySold").intValue();
                            }   //if gudaoListYesterday is not empty == END
                            else {  //if gudaoListYesterday is empty (has variation) == START
                                gudaoListYesterday = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId", gudaoItemId, "date", yesterday), null, false);
                                for(GenericValue gudaoListGV : gudaoListYesterday) {  //loop gudaoListYesterday == START
                                    yesterdayQty += gudaoListGV.getLong("quantitySold").intValue();
                                }   //loop gudaoListYesterday == END
                            }   //if gudaoListYesterday is empty (has variation) == END
                            
                            if (UtilValidate.isNotEmpty(gudaoListWeek)) {   //if gudaoListWeek is not empty == START
                                GenericValue gudaoListWeekGV = EntityUtil.getFirst(gudaoListWeek);
                                weekQty = gudaoListWeekGV.getLong("quantitySold").intValue();
                            }   //if gudaoListWeek is not empty == END
                            else {  //if gudaoListWeek is empty (has variation) == START
                                gudaoListWeek = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId", gudaoItemId, "date", weekAgo), null, false);
                                for(GenericValue gudaoListGV : gudaoListWeek) {  //loop gudaoListWeek == START
                                    weekQty += gudaoListGV.getLong("quantitySold").intValue();
                                }   //loop gudaoListWeek == END
                            }   //if gudaoListWeek is empty (has variation) == END
                            
                            if (lowestPrice.compareTo(minPrice) < 0) { //if need to take action == START
                                hasAction = true;
                                mainHasAction = true;
                            }   ////if need to take action == END
                            
                            //update ProductMonViewItem == START
                            GenericValue productMonViewItem = delegator.makeValue("ProductMonViewItem", UtilMisc.toMap("userLoginId", userLoginId, "motherSku", motherSku, "itemId", gudaoItemId));
                            productMonViewItem.set("site", site);
                            productMonViewItem.set("type", "GUDAO");
                            productMonViewItem.set("oneDaySold", Long.valueOf(todayQty - yesterdayQty));
                            productMonViewItem.set("sevenDaySold", Long.valueOf(todayQty - weekQty));
                            productMonViewItem.set("startPrice", minPrice);
                            productMonViewItem.set("lowestItemId", lowestItemId);
                            productMonViewItem.set("lowestRivalPrice", lowestPrice);
                            if (hasAction) {
                                productMonViewItem.set("hasAction", "Y");
                            } else {
                                productMonViewItem.set("hasAction", "N");
                            }
                            productMonViewItem.set("statusId", "ACTIVE");
                            delegator.createOrStore(productMonViewItem);
                            //update ProductMonViewItem == END
                        }   //loop gudaoList == END
                        //gudao == END
                        
                        GenericValue productMonViewHeader = delegator.makeValue("ProductMonViewHeader", UtilMisc.toMap("userLoginId", userLoginId));
                        productMonViewHeader.set("motherSku", motherSku);
                        productMonViewHeader.set("hasAction", mainHasAction);
                        delegator.createOrStore(productMonViewHeader);
                        
                    }   //loop distinctMotherSkuList == END
                }   //loop userLoginList == END
            }   //if partyGroupGV is not empty == END
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }   //calculatePriceMonitorView
    
    public static Map<String, Object> createGudaoProject (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String filter = userLogin.getString("userLoginId");
        Map result = ServiceUtil.returnSuccess();
        
        String title = (String) context.get("title");
        String businessTreeId = (String) context.get("businessTreeId");
        // String businessTreeType = (String) context.get("businessTreeType");

        String description = (String) context.get("description");
        String priority = (String) context.get("priority");
        
        try {   //main try == START
            String mercuryHeaderSeqId = delegator.getNextSeqId("MercuryHeader");
            GenericValue mercuryHeader = delegator.makeValue("MercuryHeader", UtilMisc.toMap("mercuryId", mercuryHeaderSeqId));
            mercuryHeader.set("title", title);
            // if (UtilValidate.isNotEmpty(context.get("businessTreeId"))) {  //if businessTreeId not empty == START
            //     if (businessTreeId.contains("\\|")) {
            //         String[] businessTreeArr = businessTreeId.split("\\|");
            //         mercuryHeader.set("businessTreeId", businessTreeArr[0]);
            //         mercuryHeader.set("businessTreeType", businessTreeArr[1]);
            //     }
            // }   //if businessTreeId not empty == END

            if (UtilValidate.isNotEmpty(businessTreeId)) {  //if businessTreeId not empty == START
                if (businessTreeId.contains("|")) {
                    String[] businessTreeArr = businessTreeId.split("\\|");
                    mercuryHeader.set("businessTreeId", businessTreeArr[0]);
                    mercuryHeader.set("businessTreeType", businessTreeArr[1]);
                }
            }   //if businessTreeId not empty == END
            
            if (UtilValidate.isNotEmpty(context.get("startDate"))) {
                Date startDateDt = (Date) context.get("startDate");
                java.sql.Date startDate = new java.sql.Date(startDateDt.getTime());
                mercuryHeader.set("startDate", startDate);
            }
            
            if (UtilValidate.isNotEmpty(context.get("completeDateEta"))) {
                Date completeDateEtaDt = (Date) context.get("completeDateEta");
                java.sql.Date completeDateEta = new java.sql.Date(completeDateEtaDt.getTime());
                mercuryHeader.set("completeDateEta", completeDateEta);
            }
            
            if (UtilValidate.isEmpty(priority)) {
                priority = "5";
            }
            
            mercuryHeader.set("priority", priority);
            mercuryHeader.set("description", description);
            mercuryHeader.set("createdBy", filter);
            mercuryHeader.set("statusId", "ACTIVE");
            // mercuryHeader.set("businessTreeId", businessTreeId);
            // mercuryHeader.set("businessTreeType", businessTreeType);
            delegator.create(mercuryHeader);
            
            result.put("mercuryId", mercuryHeaderSeqId);
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //createGudaoProject
    
    public static Map<String, Object> updateGudaoProject (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String filter = userLogin.getString("userLoginId");
        Map result = ServiceUtil.returnSuccess();
        
        String mercuryId = (String) context.get("mercuryId");
        String title = (String) context.get("title");
        String businessTreeId = (String) context.get("businessTreeId");
        String description = (String) context.get("description");
        String priority = (String) context.get("priority");
        result.put("mercuryId", mercuryId);
        
        try {   //main try == START
            
            GenericValue mercuryHeader = delegator.findOne("MercuryHeader", UtilMisc.toMap("mercuryId", mercuryId), false);

            if (UtilValidate.isNotEmpty(title)) {   //if title not empty == START
                mercuryHeader.set("title", title);
            }   //if title not empty == END
            
            if (UtilValidate.isNotEmpty(description)) { //if description not empty == START
                mercuryHeader.set("description", description);
            }   //if description not empty == END
            
            if (UtilValidate.isNotEmpty(context.get("businessTreeId"))) {  //if businessTreeId not empty == START
                String[] businessTreeArr = businessTreeId.split("\\|");
                mercuryHeader.set("businessTreeId", businessTreeArr[0]);
                mercuryHeader.set("businessTreeType", businessTreeArr[1]);
            }   //if businessTreeId not empty == END
            
            if (UtilValidate.isNotEmpty(context.get("startDate"))) {
                Date startDateDt = (Date) context.get("startDate");
                java.sql.Date startDate = new java.sql.Date(startDateDt.getTime());
                mercuryHeader.set("startDate", startDate);
            }
            
            if (UtilValidate.isNotEmpty(context.get("completeDateEta"))) {
                Date completeDateEtaDt = (Date) context.get("completeDateEta");
                java.sql.Date completeDateEta = new java.sql.Date(completeDateEtaDt.getTime());
                mercuryHeader.set("completeDateEta", completeDateEta);
            }
            
            if (UtilValidate.isNotEmpty(priority)) {
                mercuryHeader.set("priority", priority);
            }
            //mercuryHeader.set("statusId", "PENDING");
            delegator.store(mercuryHeader);
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //updateGudaoProject
    
    public static Map<String, Object> deleteGudaoProject (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String filter = userLogin.getString("userLoginId");
        Map result = ServiceUtil.returnSuccess();
        
        String mercuryId = (String) context.get("mercuryId");
        result.put("mercuryId", mercuryId);
        
        try {   //main try == START
            //delegator.removeByAnd("MercuryItem", UtilMisc.toMap("mercuryId", mercuryId));
            GenericValue mercuryHeader = delegator.findOne("MercuryHeader", UtilMisc.toMap("mercuryId", mercuryId), false);
            mercuryHeader.set("statusId", "DELETED");
            delegator.store(mercuryHeader);
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //deleteGudaoProject
    
    public static Map<String, Object> addMercuryItem (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String filter = userLogin.getString("userLoginId");
        Map result = ServiceUtil.returnSuccess();
        
        String mercuryId = (String) context.get("mercuryId");
        String title = (String) context.get("title");
        String notes = (String) context.get("notes");
        String statusId = (String) context.get("statusId");
        String assignedTo = (String) context.get("assignedTo");
        result.put("mercuryId", mercuryId);
        
        try {   //main try == START
            GenericValue mercuryItem = delegator.makeValue("MercuryItem", UtilMisc.toMap("mercuryId", mercuryId));
            mercuryItem.set("title", title);
            mercuryItem.set("notes", notes);
            mercuryItem.set("statusId", statusId);
            mercuryItem.set("assignedTo", assignedTo);
            if (UtilValidate.isNotEmpty(context.get("startDate"))) {
                Date startDateDt = (Date) context.get("startDate");
                java.sql.Date startDate = new java.sql.Date(startDateDt.getTime());
                mercuryItem.set("startDate", startDate);
            }
            if (UtilValidate.isNotEmpty(context.get("completeDateEta"))) {
                Date completeDateEtaDt = (Date) context.get("completeDateEta");
                java.sql.Date completeDateEta = new java.sql.Date(completeDateEtaDt.getTime());
                mercuryItem.set("completeDateEta", completeDateEta);
            }
            delegator.setNextSubSeqId(mercuryItem, "sequenceId", 3, 1);
            delegator.create(mercuryItem);
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //addMercuryItem
    
    public static Map<String, Object> updateMercuryItemStatus (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String filter = userLogin.getString("userLoginId");
        Map result = ServiceUtil.returnSuccess();
        
        String mercuryId = (String) context.get("mercuryId");
        String sequenceId = (String) context.get("sequenceId");
        String statusId = (String) context.get("statusId");
        result.put("mercuryId", mercuryId);
        
        try {   //main try == START
            Date nowDate = new Date();
            GenericValue mercuryItem = delegator.findOne("MercuryItem", UtilMisc.toMap("mercuryId", mercuryId, "sequenceId", sequenceId), false);
            
            if (statusId.equals("PENDING")) {
                mercuryItem.set("statusId", "IN_PROGRESS");
                mercuryItem.set("startDate", new java.sql.Date(nowDate.getTime()));
            } else if (statusId.equals("IN_PROGRESS")) {
                mercuryItem.set("statusId", "COMPLETED");
                mercuryItem.set("completedDate", new java.sql.Date(nowDate.getTime()));
            }
            
            delegator.store(mercuryItem);
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //updateMercuryItemStatus
    
    public static Map<String, Object> editMercuryItem (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String filter = userLogin.getString("userLoginId");
        Map result = ServiceUtil.returnSuccess();
        
        String mercuryId = (String) context.get("mercuryId");
        String sequenceId = (String) context.get("sequenceId");
        String title = (String) context.get("title");
        String notes = (String) context.get("notes");
        String statusId = (String) context.get("statusId");
        String assignedTo = (String) context.get("assignedTo");
        result.put("filter", filter);
        result.put("mercuryId", mercuryId);
        
        try {   //main try == START
            Date nowDate = new Date();
            GenericValue mercuryItem = delegator.findOne("MercuryItem", UtilMisc.toMap("mercuryId", mercuryId, "sequenceId", sequenceId), false);
            String oldStatusId = mercuryItem.getString("statusId");
            if (UtilValidate.isNotEmpty(context.get("startDate"))) {
                Date startDateDt = (Date) context.get("startDate");
                java.sql.Date startDate = new java.sql.Date(startDateDt.getTime());
                mercuryItem.set("startDate", startDate);
            }
            if (UtilValidate.isNotEmpty(context.get("completeDateEta"))) {
                Date completeDateEtaDt = (Date) context.get("completeDateEta");
                java.sql.Date completeDateEta = new java.sql.Date(completeDateEtaDt.getTime());
                mercuryItem.set("completeDateEta", completeDateEta);
            }
            if (UtilValidate.isNotEmpty(statusId)) {
                mercuryItem.set("statusId", statusId);
                if (statusId.equals("COMPLETED") && !oldStatusId.equals("COMPLETED")) {
                    mercuryItem.set("completedDate", new java.sql.Date(nowDate.getTime()));
                }
            }
            if (UtilValidate.isNotEmpty(title)) {
                mercuryItem.set("title", title);
            }
            if (UtilValidate.isNotEmpty(notes)) {
                mercuryItem.set("notes", notes);
            }
            if (UtilValidate.isNotEmpty(assignedTo)) {
                mercuryItem.set("assignedTo", assignedTo);
            }
            delegator.store(mercuryItem);
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //editMercuryItem
    
    public static Map<String, Object> deleteMercuryItem (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String filter = userLogin.getString("userLoginId");
        Map result = ServiceUtil.returnSuccess();
        
        String mercuryId = (String) context.get("mercuryId");
        String sequenceId = (String) context.get("sequenceId");
        result.put("mercuryId", mercuryId);
        
        try {   //main try == START
            GenericValue mercuryItem = delegator.findOne("MercuryItem", UtilMisc.toMap("mercuryId", mercuryId, "sequenceId", sequenceId), false);
            delegator.removeValue(mercuryItem);
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //deleteMercuryItem
    
    //UpdateGudaoProjectStatusCheck
    public static Map<String, Object> gudaoProjectStatusCheck (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String filter = userLogin.getString("userLoginId");
        Map result = ServiceUtil.returnSuccess();

        String mercuryId = (String) context.get("mercuryId");
        try {   //main try == START
            String statusId = "PENDING";
            boolean isActive = false;
            boolean isCompleted = true;
            GenericValue mercuryHeader = delegator.findOne("MercuryHeader", UtilMisc.toMap("mercuryId", mercuryId), false);
            if (UtilValidate.isEmpty(mercuryHeader)) {
                return ServiceUtil.returnError("MercuryHeader " + mercuryId + " does not exist");
            }
            
            List<GenericValue> mercuryItemList = delegator.findByAnd("MercuryItem", UtilMisc.toMap("mercuryId", mercuryId), null, false);
            java.sql.Date nowDate = new java.sql.Date((new Date()).getTime());
            if (UtilValidate.isNotEmpty(mercuryItemList)) {    //if mercuryItemList not empty == START
                for (GenericValue mercuryItem : mercuryItemList) {  //loop mercuryItemList == START
                    java.sql.Date startDate = mercuryItem.getDate("startDate");
                    if (UtilValidate.isNotEmpty(startDate)) {   //if startDate is not empty == START
                        if (nowDate.compareTo(startDate) >= 0) {  //if mercuryItem task has been started == START
                            isActive = true;
                        }   //if mercuryItem task has been started == END
                    }   //if startDate is not empty == END
                    
                    String mercuryItemStatus = mercuryItem.getString("statusId");
                    if (!mercuryItemStatus.equals("COMPLETED")) {
                        isCompleted = false;
                    }
                }   //loop mercuryItemList == END
            }   //if mercuryItemList not empty == END
            else {
                isCompleted = false;
            }
            
            if (isActive) { //if project is active == START
                if (isCompleted) {  //if project is completed == START
                    statusId = "COMPLETED";
                }   //if project is completed == END
                else {  //if project not completed == START
                    statusId = "ACTIVE";
                }   //if project not completed == END
            }   //if project is active == END
            
            Map updateMercuryHeaderStatus = dispatcher.runSync("updateMercuryHeaderStatus", UtilMisc.toMap("mercuryId", mercuryId, "statusId", statusId, "userLogin", userLogin));
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //gudaoProjectStatusCheck
    
    public static Map<String, Object> updateMercuryHeaderStatus (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String filter = userLogin.getString("userLoginId");
        Map result = ServiceUtil.returnSuccess();
        
        String mercuryId = (String) context.get("mercuryId");
        String statusId = (String) context.get("statusId");
        statusId = statusId.toUpperCase();
        
        try {   //main try == START
            GenericValue mercuryHeader = delegator.findOne("MercuryHeader", UtilMisc.toMap("mercuryId", mercuryId), false);
            if (UtilValidate.isEmpty(mercuryHeader)) {
                return ServiceUtil.returnError("MercuryHeader " + mercuryId + " does not exist");
            }
            mercuryHeader.set("statusId", statusId);
            delegator.store(mercuryHeader);
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //updateMercuryHeaderStatus
    

    public static Map<String, Object> updatetestyaya (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map result = ServiceUtil.returnSuccess();

        String testyayaId = (String) context.get("testyayaId");
        String text = (String) context.get("text");

        String browser = (String) context.get("browser");
        String cSSGrade = (String) context.get("cSSGrade");
        String engineVersion = (String) context.get("engineVersion");
        String platform = (String) context.get("platform");
        String renderingEngine = (String) context.get("renderingEngine");

        // 
        String brr_testyayaId = testyayaId.substring(1,testyayaId.length()-1);
        String brr_text = text.substring(1,text.length()-1);

        String brr_browser = browser.substring(1,browser.length()-1);
        String brr_cSSGrade = cSSGrade.substring(1,cSSGrade.length()-1);
        String brr_engineVersion = engineVersion.substring(1,engineVersion.length()-1);
        String brr_platform = platform.substring(1,platform.length()-1);
        String brr_renderingEngine = renderingEngine.substring(1,renderingEngine.length()-1);



        // char[] a = browser.toCharArray();
        // String b = new String(a);
        // String[] browser2 =browser3.split(",");
        String[] br_testyayaId =brr_testyayaId.split(", ");
        String[] br_text =brr_text.split(", ");

        String[] br_browser =brr_browser.split(", ");
        String[] br_cSSGrade =brr_cSSGrade.split(", ");
        String[] br_engineVersion =brr_engineVersion.split(", ");
        String[] br_platform =brr_platform.split(", ");
        String[] br_renderingEngine =brr_renderingEngine.split(", ");

        int size = br_testyayaId.length;
        int sizes = size;
        int size_browser =br_browser.length;

        String[] x = new String[sizes];
        for(int i=0;i<br_browser.length;i++){
            x[i] = br_browser[i];
        }


        // String[] sa=browser.split("\\s*");

        // int size = browserList.size();
        // String[] array = (String[])browserList1.toArray(new String[size]); 

        // try {   //main try == START
        //     GenericValue testyaya = delegator.findOne("testyaya", UtilMisc.toMap("testyayaId", testyayaId), false);

        //         // GenericValue testyaya = delegator.makeValue("testyaya", UtilMisc.toMap("testyayaId", testyayaId,"text","text", "browser","browser","cSSGrade","cSSGrade","engineVersion","engineVersion","platform","platform","renderingEngine","renderingEngine"));
            
        //         if (UtilValidate.isEmpty(testyaya)) {
        //             return ServiceUtil.returnError("testyaya " + testyayaId + " does not exist");
        //         }
        //         // testyaya.set("text", text);
        //         testyaya.set("browser", browser);
        //         testyaya.set("cSSGrade", cSSGrade);
        //         testyaya.set("engineVersion", engineVersion);
        //         testyaya.set("platform", platform);
        //         testyaya.set("renderingEngine", renderingEngine);


        //         delegator.store(testyaya);
        //         // delegator.storeAll(java.util.List<GenericValue> testyaya);
        //         // delegator.createOrStore(testyaya);

            
        // }   //main try == END
        // catch (Exception e) {
        //     e.printStackTrace();
        // }

        List<GenericValue> testyayaList = null;
        try {


                // for (GenericValue testyaya : testyayaList) {

                    // if (UtilValidate.isEmpty(testyaya)) {
                    //     return ServiceUtil.returnError("testyaya " + testyayaId + " does not exist");
                    // }
                    // testyaya.set("browser", browser);
                    // List<GenericValue> recipes = null;
                    // List<GenericValue> browserList = delegator.findList("testyaya", null, null, null, null, false);
                    // GenericValue testyaya1 = delegator.findOne("testyaya", UtilMisc.toMap("testyayaId", testyayaId), false);
                try {
                        // List<GenericValue> testyaya = UtilMisc.toList(testyaya, testyaya);
                    for (int i=0; i< sizes; i++) {
                        // GenericValue testyaya = delegator.findOne("testyaya", UtilMisc.toMap("testyayaId", i+""), false);
                        GenericValue testyaya = delegator.findOne("testyaya", UtilMisc.toMap("testyayaId", br_testyayaId[i]), false);
                            // testyaya.set("browser", br_browser1[i]);
                            testyaya.set("browser", x[i]);
                            testyaya.set("cSSGrade", br_cSSGrade[i]);
                            // testyaya.set("cSSGrade", cSSGrade);
                            testyaya.set("engineVersion", br_engineVersion[i]);
                            testyaya.set("platform", br_platform[i]);
                            testyaya.set("renderingEngine", br_renderingEngine[i]);
                            try{
                                // delegator.store(recipes);
                                delegator.store(testyaya);
                            }
                            catch(GenericEntityException e) {
                                Debug.logError(e, module);
                            }
                        // GenericValue testyaya1 = delegator.findOne("testyaya", UtilMisc.toMap("testyayaId", "2"), false);
                        //     testyaya1.set("browser", br[1]);
                        //     // testyaya.set("cSSGrade", cSSGrade);
                        //     // testyaya.set("engineVersion", engineVersion);
                        //     // testyaya.set("platform", platform);
                        //     // testyaya.set("renderingEngine", renderingEngine);
                        //     try{
                        //         // delegator.store(recipes);
                        //         delegator.store(testyaya1);
                        //     }
                        //     catch(GenericEntityException e) {
                        //         Debug.logError(e, module);
                        //     }
                            

                    }
                                            

                    // for (int i=0; i < browserList.size(); i++) {
                    //     String browser_1 = (List) browserList.get(i);
                    //     testyaya.set("browser", browser_1);
                    // }

                    // testyaya.set("cSSGrade", cSSGrade);
                    // testyaya.set("engineVersion", engineVersion);
                    // testyaya.set("platform", platform);
                    // testyaya.set("renderingEngine", renderingEngine);

                    // delegator.store(recipes);

                // }
                            // delegator.storeAll(testyaya);

                // testyayaList.browser = node;
                // testyayaList.set("browser", browser);


                    } catch (GenericEntityException e) {
                        Debug.logError(e, module);
                        // return ServiceUtil.returnError(UtilProperties.getMessage(resource_error,
                                // "OrderUnableToSetCancelQuantity", UtilMisc.toMap("itemMsgInfo",itemMsgInfo), locale));
                    }

        }
        catch (Exception e) {
            e.printStackTrace();
        }



        
        return result;
    }   //Updatetestyaya


        public static Map<String, Object> createDailyTaskRecord (DispatchContext dctx, Map context) {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String filter = userLogin.getString("userLoginId");
        Map result = ServiceUtil.returnSuccess();
        
        String owner = (String) context.get("owner");
        String taskName = (String) context.get("taskName");


        
        try {   //main try == START
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Timestamp nowTimestamp = Timestamp.valueOf(sdf.format(now.getTime()));
            // java.sql.Date nowTimestamp = new java.sql.Date(now.getTime().getTime());
            // String mercuryHeaderSeqId = delegator.getNextSeqId("DailyTaskRecord");
            GenericValue DailyTaskRecord = delegator.makeValue("DailyTaskRecord", UtilMisc.toMap("completedDate", nowTimestamp));
            DailyTaskRecord.set("owner", owner);
            DailyTaskRecord.set("taskName", taskName);
            // if (UtilValidate.isNotEmpty(context.get("businessTreeId"))) {  //if businessTreeId not empty == START
            //     if (businessTreeId.contains("\\|")) {
            //         String[] businessTreeArr = businessTreeId.split("\\|");
            //         mercuryHeader.set("businessTreeId", businessTreeArr[0]);
            //         mercuryHeader.set("businessTreeType", businessTreeArr[1]);
            //     }
            // }   //if businessTreeId not empty == END


            delegator.create(DailyTaskRecord);
            
            // result.put("completedDate", nowTimestamp);
            
        }   //main try == END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }   //createDailyTaskRecord


}	//END class