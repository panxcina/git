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
package com.bellyanna.bulk;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.List;
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
import java.util.LinkedList;
import java.util.Locale;
import java.util.Iterator;
import java.net.URLDecoder;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import jxl.*;
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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.csvreader.CsvReader;


public class bulkService {
	private static final String module = bulkService.class.getName();
    private static final String eol = System.getProperty("line.separator");
	
    public static Map<String, Object> uploadProductXlsFile(DispatchContext dctx, Map<String, ? extends Object> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
        String uploadFileName = (String) context.get("_uploadedFile_fileName");
        String importFormId = (String) context.get("importFormId");
        String updateFormId = (String) context.get("updateFormId");
        if (UtilValidate.isNotEmpty(uploadFileName) && UtilValidate.isNotEmpty(imageData)) {
            try {   //main try -- START
                String fileServerPath = "hot-deploy/bellyanna/webapp/bellyanna/bulkModule/upload";
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
                    fileName = "IMPORT-" + importFormId + "_" + UtilDateTime.nowDateString() + uploadFileName;
                } else if (updateFormId != null) {
                    fileName = "UPDATE-" + updateFormId + "_" + UtilDateTime.nowDateString() + uploadFileName;
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
                            result = readProductXls(dispatcher, delegator, userLogin, filePath);
                        }
                        else if (fileName.endsWith(".csv")) {
                            result = readProductCsv(dispatcher, delegator, userLogin, filePath);
                        }
                        
                    } else if (updateFormId != null && updateFormId.equals("PRODUCT")) {
                        result = readProductUpdateXls(dispatcher, delegator, userLogin, filePath);
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
    }   //uploadProductXlsFile
    
    private static Map<String, Object> readProductXls(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String filePath) {   //readProductXls
        Map<String, Object> result = ServiceUtil.returnSuccess();
        List<String> errorList = FastList.newInstance();
        try {   //main try -- START
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
            int colInternalName = 0;
            int colDeclaredNameEn = 0;
            int colDeclaredNameCn = 0;
            int colLocationCode = 0;
            int colWeight = 0;
            int colProductType = 0;
            int colIsVirtual = 0;
            int colIsVariant = 0;
            int colVariantMethod = 0;
            int colRequirementMethod = 0;
            int colSupplierPartyId = 0;
            int colFacilityId = 0;
            int colUsd = 0;
            int colCad = 0;
            int colGbp = 0;
            int colAud = 0;
            int colEur = 0;
            
            for(int colHead = minColIx; colHead < maxColIx; colHead++) {  //get column header -- START
                HSSFCell cellHead = firstRow.getCell(colHead);
                if(cellHead == null) {
                    continue;
                }
                cellHead.setCellType(HSSFCell.CELL_TYPE_STRING);
                String colHeader = cellHead.getRichStringCellValue().toString().toUpperCase().trim();
                
                if ("SKU".equals(colHeader) || "PRODUCT ID".equals(colHeader)) { //read column header data -- START
                    colProductId = colHead;
                } else if ("INTERNAL NAME".equals(colHeader)) {
                    colInternalName = colHead;
                } else if ("EN NAME".equals(colHeader)) {
                    colDeclaredNameEn = colHead;
                } else if ("CN NAME".equals(colHeader)) {
                    colDeclaredNameCn = colHead;
                } else if ("LOCATION".equals(colHeader)) {
                    colLocationCode = colHead;
                } else if ("WEIGHT".equals(colHeader)) {
                    colWeight = colHead;
                } else if ("PRODUCT TYPE".equals(colHeader) || "TYPE".equals(colHeader)) {
                    colProductType = colHead;
                } else if ("VIRTUAL".equals(colHeader) || "IS VIRTUAL".equals(colHeader)) {
                    colIsVirtual = colHead;
                } else if ("VARIANT".equals(colHeader) || "IS VARIANT".equals(colHeader)) {
                    colIsVariant = colHead;
                } else if ("VARIANT METHOD".equals(colHeader)) {
                    colVariantMethod = colHead;
                } else if ("REQUIREMENT METHOD".equals(colHeader)) {
                    colRequirementMethod = colHead;
                } else if ("SUPPLIER".equals(colHeader) || "SUPPLIER ID".equals(colHeader)) {
                    colSupplierPartyId = colHead;
                } else if ("FACILITY".equals(colHeader) || "WAREHOUSE".equals(colHeader) || "FACILITY ID".equals(colHeader)) {
                    colFacilityId = colHead;
                } else if ("USD".equals(colHeader)) {
                    colUsd = colHead;
                } else if ("CAD".equals(colHeader)) {
                    colCad = colHead;
                } else if ("GBP".equals(colHeader)) {
                    colGbp = colHead;
                } else if ("AUD".equals(colHeader)) {
                    colAud = colHead;
                } else if ("EUR".equals(colHeader)) {
                    colEur = colHead;
                }   //read column header data -- END

            }   //get column header -- END
            
            for (int j = 1; j <= sheetLastRowNumber; j++) { //loop rows -- START
                HSSFRow row = sheet.getRow(j);
                if (row != null) {  //if row is not empty -- START
                    Map<String, Object> productImportCtx = FastMap.newInstance();
                    boolean updateProductImport = true;
                    
                    for(int colIx = minColIx; colIx < maxColIx; colIx++) {    //loop cell in a row -- START
                        HSSFCell cell = row.getCell(colIx);
                        if(cell == null) {
                            continue;
                        }
                        
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        String dataString = cell.getRichStringCellValue().toString().trim();
                        
                        if (colIx == (colProductId)) { //read record -- START
                            productImportCtx.put("productId", dataString.toUpperCase());
                            if (UtilValidate.isEmpty(dataString)) {   //break when there is no record anymore -- START
                                updateProductImport = false;
                                continue;
                            }   //break when there is no record anymore -- END
                        } else if (colIx == (colInternalName)) {
                            productImportCtx.put("internalName", dataString);
                        } else if (colIx == (colDeclaredNameEn)) {
                            productImportCtx.put("declaredNameEn", dataString);
                        } else if (colIx == (colDeclaredNameCn)) {
                            productImportCtx.put("declaredNameCn", dataString);
                        } else if (colIx == (colLocationCode)) {
                            productImportCtx.put("locationCode", dataString);
                        } else if (colIx == (colWeight)) {
                            productImportCtx.put("weight", new BigDecimal(dataString));
                        } else if (colIx == (colProductType)) {
                            productImportCtx.put("productType", dataString);
                        } else if (colIx == (colIsVirtual)) {
                            productImportCtx.put("isVirtual", dataString);
                        } else if (colIx == (colIsVariant)) {
                            productImportCtx.put("isVariant", dataString);
                        } else if (colIx == (colVariantMethod)) {
                            productImportCtx.put("virtualVariantMethodEnum", dataString);
                        } else if (colIx == (colRequirementMethod)) {
                            productImportCtx.put("requirementMethodEnumId", dataString);
                        } else if (colIx == (colSupplierPartyId)) {
                            productImportCtx.put("supplierPartyId", dataString);
                        } else if (colIx == (colFacilityId)) {
                            productImportCtx.put("facilityId", dataString);
                        } else if (colIx == (colUsd)) {
                            productImportCtx.put("priceUSD", dataString);
                        } else if (colIx == (colCad)) {
                            productImportCtx.put("priceCAD", dataString);
                        } else if (colIx == (colGbp)) {
                            productImportCtx.put("priceGBP", dataString);
                        } else if (colIx == (colAud)) {
                            productImportCtx.put("priceAUD", dataString);
                        } else if (colIx == (colEur)) {
                            productImportCtx.put("priceEUR", dataString);
                        }   //read record -- END
                    }   //loop cell in a row -- END
                    if (updateProductImport) {    //if row indeed has data -- START
                        try {   //try -- second -- START
                            String newProductImportId = delegator.getNextSeqId("ProductImport");
                            productImportCtx.put("productImportId", newProductImportId);
                            productImportCtx.put("fileName", filePath);
                            productImportCtx.put("fileLineNumber", new BigDecimal(j));
                            productImportCtx.put("importedStatus", "N");
                            productImportCtx.put("userLogin", userLogin);

                            result = dispatcher.runSync("createProductImport", productImportCtx);
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
    }   //readProductXls
    
    private static Map<String, Object> readProductUpdateXls(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String filePath) {   //readProductXls
        Map<String, Object> result = ServiceUtil.returnSuccess();
        List<String> errorList = FastList.newInstance();
        try {   //main try -- START
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
            int colParentSku = 0;
            int colInternalName = 0;
            int colDeclaredNameEn = 0;
            int colDeclaredNameCn = 0;
            int colLocationCode = 0;
            int colWeight = 0;
            int colProductType = 0;
            int colIsVirtual = 0;
            int colIsVariant = 0;
            int colVariantMethod = 0;
            int colRequirementMethod = 0;
            int colSupplierPartyId = 0;
            int colFacilityId = 0;
            int colUsd = 0;
            int colCad = 0;
            int colGbp = 0;
            int colAud = 0;
            int colEur = 0;
            int colSetDiscontinue = 0;
            int colSetActive = 0;
            int colEbayUpdateQuantity = 0;
            int colEbayListQuantity = 0;
            int colEbayTriggerQuantity = 0;
            boolean parentSku = false;
            
            for(int colHead = minColIx; colHead < maxColIx; colHead++) {  //get column header -- START
                HSSFCell cellHead = firstRow.getCell(colHead);
                if(cellHead == null) {
                    continue;
                }
                cellHead.setCellType(HSSFCell.CELL_TYPE_STRING);
                String colHeader = cellHead.getRichStringCellValue().toString().toUpperCase().trim();
                
                if ("SKU".equals(colHeader) || "PRODUCT ID".equals(colHeader)) { //read column header data -- START
                    colProductId = colHead;
                } else if ("PARENT SKU".equals(colHeader)) {
                    colParentSku = colHead;
                } else if ("INTERNAL NAME".equals(colHeader)) {
                    colInternalName = colHead;
                } else if ("EN NAME".equals(colHeader)) {
                    colDeclaredNameEn = colHead;
                } else if ("CN NAME".equals(colHeader)) {
                    colDeclaredNameCn = colHead;
                } else if ("LOCATION".equals(colHeader)) {
                    colLocationCode = colHead;
                } else if ("WEIGHT".equals(colHeader)) {
                    colWeight = colHead;
                } else if ("PRODUCT TYPE".equals(colHeader) || "TYPE".equals(colHeader)) {
                    colProductType = colHead;
                } else if ("VIRTUAL".equals(colHeader) || "IS VIRTUAL".equals(colHeader)) {
                    colIsVirtual = colHead;
                } else if ("VARIANT".equals(colHeader) || "IS VARIANT".equals(colHeader)) {
                    colIsVariant = colHead;
                } else if ("VARIANT METHOD".equals(colHeader)) {
                    colVariantMethod = colHead;
                } else if ("REQUIREMENT METHOD".equals(colHeader)) {
                    colRequirementMethod = colHead;
                } else if ("SUPPLIER".equals(colHeader) || "SUPPLIER ID".equals(colHeader)) {
                    colSupplierPartyId = colHead;
                } else if ("FACILITY".equals(colHeader) || "WAREHOUSE".equals(colHeader) || "FACILITY ID".equals(colHeader)) {
                    colFacilityId = colHead;
                } else if ("USD".equals(colHeader)) {
                    colUsd = colHead;
                } else if ("CAD".equals(colHeader)) {
                    colCad = colHead;
                } else if ("GBP".equals(colHeader)) {
                    colGbp = colHead;
                } else if ("AUD".equals(colHeader)) {
                    colAud = colHead;
                } else if ("EUR".equals(colHeader)) {
                    colEur = colHead;
                } else if ("DISCONTINUE".equals(colHeader) || "SET DISCONTINUE".equals(colHeader)) {
                    colSetDiscontinue = colHead;
                } else if ("UPDATE QUANTITY".equals(colHeader) || "EBAY UPDATE QUANTITY".equals(colHeader) || "UPDATE EBAY QUANTITY".equals(colHeader)) {
                    colEbayUpdateQuantity = colHead;
                } else if ("LIST QUANTITY".equals(colHeader) || "EBAY LIST QUANTITY".equals(colHeader)) {
                    colEbayListQuantity = colHead;
                } else if ("TRIGGER QUANTITY".equals(colHeader) ||"TRIGGER UPDATE QUANTITY".equals(colHeader) || "EBAY TRIGGER QUANTITY".equals(colHeader)) {
                    colEbayTriggerQuantity = colHead;
                }   //read column header data -- END
                
            }   //get column header -- END
            
            for (int j = 1; j <= sheetLastRowNumber; j++) { //loop rows -- START
                HSSFRow row = sheet.getRow(j);
                if (row != null) {  //if row is not empty -- START
                    Map<String, Object> productUpdateCtx = FastMap.newInstance();
                    boolean updateProductImport = true;
                    
                    for(int colIx = minColIx; colIx < maxColIx; colIx++) {    //loop cell in a row -- START
                        HSSFCell cell = row.getCell(colIx);
                        if(cell == null) {
                            continue;
                        }
                        
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        String dataString = cell.getRichStringCellValue().toString().trim();
                        
                        if (colIx == (colProductId)) { //read record -- START
                            productUpdateCtx.put("productId", dataString.toUpperCase());
                            if (UtilValidate.isEmpty(dataString)) {   //break when there is no record anymore -- START
                                updateProductImport = false;
                                continue;
                            }   //break when there is no record anymore -- END
                        } else if (colIx == (colParentSku)) {
                            if (dataString.equals("Y")) {
                                parentSku = true;
                            }
                        } else if (colIx == (colInternalName)) {
                            productUpdateCtx.put("internalName", dataString);
                        } else if (colIx == (colDeclaredNameEn)) {
                            productUpdateCtx.put("declaredNameEn", dataString);
                        } else if (colIx == (colDeclaredNameCn)) {
                            productUpdateCtx.put("declaredNameCn", dataString);
                        } else if (colIx == (colLocationCode)) {
                            productUpdateCtx.put("locationCode", dataString);
                        } else if (colIx == (colWeight)) {
                            productUpdateCtx.put("weight", new BigDecimal(dataString));
                        } else if (colIx == (colProductType)) {
                            productUpdateCtx.put("productType", dataString);
                        } else if (colIx == (colIsVirtual)) {
                            productUpdateCtx.put("isVirtual", dataString);
                        } else if (colIx == (colIsVariant)) {
                            productUpdateCtx.put("isVariant", dataString);
                        } else if (colIx == (colVariantMethod)) {
                            productUpdateCtx.put("virtualVariantMethodEnum", dataString);
                        } else if (colIx == (colRequirementMethod)) {
                            productUpdateCtx.put("requirementMethodEnumId", dataString);
                        } else if (colIx == (colSupplierPartyId)) {
                            productUpdateCtx.put("supplierPartyId", dataString);
                        } else if (colIx == (colFacilityId)) {
                            productUpdateCtx.put("facilityId", dataString);
                        } else if (colIx == (colUsd)) {
                            productUpdateCtx.put("priceUSD", dataString);
                        } else if (colIx == (colCad)) {
                            productUpdateCtx.put("priceCAD", dataString);
                        } else if (colIx == (colGbp)) {
                            productUpdateCtx.put("priceGBP", dataString);
                        } else if (colIx == (colAud)) {
                            productUpdateCtx.put("priceAUD", dataString);
                        } else if (colIx == (colEur)) {
                            productUpdateCtx.put("priceEUR", dataString);
                        } else if (colIx == (colSetDiscontinue)) {
                            productUpdateCtx.put("setDiscontinue", dataString);
                        } else if (colIx == (colEbayUpdateQuantity)) {
                            productUpdateCtx.put("ebayUpdateQuantity", dataString);
                        } else if (colIx == (colEbayListQuantity)) {
                            productUpdateCtx.put("ebayListQuantity", dataString);
                        } else if (colIx == (colEbayTriggerQuantity)) {
                            productUpdateCtx.put("ebayTriggerQuantity", dataString);
                        } //read record -- END
                    }   //loop cell in a row -- END
                    if (updateProductImport) {    //if row indeed has data -- START
                        try {   //try -- second -- START
                            if (parentSku && productUpdateCtx.get("setDiscontinue") != null && productUpdateCtx.get("setDiscontinue").equals("Y")) {   //if SKU is parent -- START
                                List<GenericValue> childrenProducts = delegator.findList("Product",
                                                                                        EntityCondition.makeCondition("productId", EntityOperator.LIKE, productUpdateCtx.get("productId").toString().toUpperCase() + "%"),
                                                                                        null, null, null, false);
                                for (GenericValue childrenProduct : childrenProducts) { //loop childrenProducts -- START
                                    String childrenProductId = childrenProduct.getString("productId");
                                    String childrenNextProductUpdateId = delegator.getNextSeqId("BulkProductUpdate");
                                    result = dispatcher.runSync("createBulkProductUpdate", UtilMisc.toMap(
                                                                                                          "productUpdateId", childrenNextProductUpdateId,
                                                                                                          "fileName", filePath,
                                                                                                          "fileLineNumber", new BigDecimal(j),
                                                                                                          "updatedStatus", "N",
                                                                                                          "productId", childrenProductId,
                                                                                                          "setDiscontinue", productUpdateCtx.get("setDiscontinue"),
                                                                                                          "userLogin", userLogin)
                                                                );
                                }   //loop childrenProducts -- END
                                

                            }   //if SKU is parent -- END
                            else {  //if SKU is children -- START
                                String newProductUpdateId = delegator.getNextSeqId("BulkProductUpdate");
                                productUpdateCtx.put("productUpdateId", newProductUpdateId);
                                productUpdateCtx.put("fileName", filePath);
                                productUpdateCtx.put("fileLineNumber", new BigDecimal(j));
                                productUpdateCtx.put("updatedStatus", "N");
                                productUpdateCtx.put("userLogin", userLogin);
                                
                                result = dispatcher.runSync("createBulkProductUpdate", productUpdateCtx);
                                if (ServiceUtil.isError(result)) {  //if result gives error -- START
                                    errorList.add(ServiceUtil.getErrorMessage(result));
                                }   //if result gives error -- END
                            }   //if SKU is children -- END
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
                List<GenericValue> productImportList = delegator.findByAnd("ProductImport", UtilMisc.toMap("fileName", (String) context.get("fileName")), null, false);
                if (productImportList.size() > 0) { //if productImportList is not empty -- START
                    for (GenericValue productImport : productImportList) {
                        dispatcher.runSync("deleteProductImport", UtilMisc.toMap("productImportId", productImport.getString("productImportId"), "userLogin", userLogin));
                    }
                    long productImportCount = delegator.findCountByCondition("ProductImport", EntityCondition.makeCondition(UtilMisc.toMap("fileName", fileName)), null, null);
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
            }   //Execute deleteProductImport -- END
            
            if (UtilValidate.isNotEmpty(updateFormId)) {    //Execute deleteProductUpdate -- START
                List<GenericValue> bulkProductUpdateList = delegator.findByAnd("BulkProductUpdate", UtilMisc.toMap("fileName", (String) context.get("fileName")), null, false);
                if (bulkProductUpdateList.size() > 0) { //if bulkProductUpdateList is not empty -- START
                    for (GenericValue bulkProductUpdate : bulkProductUpdateList) {
                        dispatcher.runSync("deleteBulkProductUpdate", UtilMisc.toMap("productUpdateId", bulkProductUpdate.getString("productUpdateId"), "userLogin", userLogin));
                    }
                    long bulkProductUpdateCount = delegator.findCountByCondition("BulkProductUpdate", EntityCondition.makeCondition(UtilMisc.toMap("fileName", fileName)), null, null);
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



    
    
}	//END class