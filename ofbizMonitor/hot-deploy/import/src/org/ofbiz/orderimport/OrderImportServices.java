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

package org.ofbiz.orderimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.lang.Character;
import jxl.*;
import jxl.read.biff.BiffException;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.ofbiz.base.util.string.FlexibleStringExpander;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.model.DynamicViewEntity;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.util.EntityListIterator;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.csvreader.CsvReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.bellyanna.ebay.common;
import com.bellyanna.ebay.eBayTradingAPI;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.CompleteSaleCall;
import com.ebay.soap.eBLBaseComponents.ShipmentTrackingDetailsType;
import com.ebay.soap.eBLBaseComponents.ShipmentType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

public class OrderImportServices {
    public static final String module = OrderImportServices.class.getName();
    public static Map<String, Object> uploadOrderCsvFile(DispatchContext dctx, Map<String, ? extends Object> context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
        String uploadFileName = (String) context.get("_uploadedFile_fileName");
        String productStoreId = (String) context.get("productStoreId");
        String csvType = (String) context.get("csvType");
        boolean isEbay = "ebay".equals(csvType);
        if (UtilValidate.isNotEmpty(uploadFileName) && UtilValidate.isNotEmpty(imageData)) {
            try {
                String fileServerPath = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("orderImport", "orderimport.server.path"), context);
                File rootTargetDir = new File(fileServerPath);
                if (!rootTargetDir.exists()) {
                    boolean created = rootTargetDir.mkdirs();
                    if (!created) {
                        String errMsg = "Not create target directory";
                        Debug.logFatal(errMsg, module);
                        return ServiceUtil.returnError(errMsg);
                    }
                }
                String fileName = uploadFileName.substring(0, uploadFileName.indexOf(".")) + "_" +UtilDateTime.nowDateString() + ".csv";
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
                if (file.exists()) {
                	Map result = null;
                    GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                    String productStoreGroup = productStore.getString("primaryStoreGroupId");
                	if(productStoreGroup.equals("EBAY")) {
                		result = readDataFromCsv(dispatcher, delegator, userLogin, productStoreId, filePath);
                	} else if (productStoreGroup.equals("MAGENTO")) {
                		result = readDataFromMagentoCsv(dispatcher, delegator, userLogin, productStoreId, filePath);
                	} else if (productStoreGroup.equals("ALIEXPRESS")) {
                        result = readDataFromAliXls(dispatcher, delegator, userLogin, productStoreId, filePath);
                    }
                	if(ServiceUtil.isError(result)) {
                		return result;
                	}
                }
            } catch (Exception e) {
                return ServiceUtil.returnError(e.getMessage());
            }
        }
        return ServiceUtil.returnSuccess();
    }

    private static Map<String, Object> readDataFromCsv(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String productStoreId, String filePath) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        List<String> errorList = FastList.newInstance();
        try {
            String orderNumberProfix = null;
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            if (UtilValidate.isNotEmpty(productStore.get("orderNumberPrefix"))) {
                orderNumberProfix = productStore.getString("orderNumberPrefix");
            }
            CsvReader csv = new CsvReader(new FileInputStream(new File(filePath)), Charset.forName("WINDOWS-1252"));
            if (UtilValidate.isNotEmpty(csv)) {
                csv.readHeaders();
                while (csv.readRecord()) {
                    List<GenericValue> orderImports = delegator.findByAnd("OrderImport", UtilMisc.toMap("orderId", orderNumberProfix + csv.get(0), "fileName", filePath), null, false);
                    if (orderImports.size() > 0) {
                        GenericValue orderImport = EntityUtil.getFirst(orderImports);
                        Map<String, Object> orderItemImport = FastMap.newInstance();
                        orderItemImport.put("orderImportId", orderImport.getString("orderImportId"));
                        orderItemImport.put("fileLineNumber", new BigDecimal(csv.getCurrentRecord() + 3));
                        orderItemImport.put("userLogin", userLogin);
                        for (int columnCount = 0; columnCount < csv.getHeaderCount(); columnCount++) {
                            String data = csv.get(columnCount);
                            String header = csv.getHeader(columnCount);
                            if ("Item Number".equals(header)) {
                                orderItemImport.put("orderItemNumber", data);
                            } else if ("Item Title".equals(header)) {
                                orderItemImport.put("productName", data);
                            } else if ("Custom Label".equals(header)) {
                                orderItemImport.put("productId", data);
                            } else if ("Quantity".equals(header)) {
                                orderItemImport.put("quantity", (UtilValidate.isNotEmpty(data)) ? new BigDecimal(data) : BigDecimal.ZERO);
                            } else if ("Transaction ID".equals(header)) {
                                orderItemImport.put("transactionId", data);
                                if(data != null && data.contains("+")) {
                                	return ServiceUtil.returnError("Please check the column 'Transaction ID' and make sure that its value is pure number without '+'.");
                                }
                            } else if ("Sale Price".equals(header)) {
                                orderItemImport.put("unitPrice", getPrice(data));
                            } else if ("Sale Date".equals(header)) {
                                orderItemImport.put("saleDate", getTimestamp(data));
                            }
                        }
                        result = dispatcher.runSync("createOrderItemImport", orderItemImport);
                    } else {
                        Map<String, Object> orderImportCtx = FastMap.newInstance();
                        Map<String, Object> orderItemImport = FastMap.newInstance();
                        String newOrderImportId = delegator.getNextSeqId("OrderImport");
                        orderImportCtx.put("orderImportId", newOrderImportId);
                        orderImportCtx.put("productStoreId", productStoreId);
                        orderImportCtx.put("fileName", filePath);
                        orderImportCtx.put("fileLineNumber", new BigDecimal(csv.getCurrentRecord() + 3));
                        orderImportCtx.put("importedStatus", "N");
                        orderImportCtx.put("userLogin", userLogin);
                        orderImportCtx.put("stateProvinceGeoId", "_NA_"); // set _NA_ as default state
                        for (int columnCount = 0; columnCount < csv.getHeaderCount(); columnCount++) {
                            String data = csv.get(columnCount);
                            String header = csv.getHeader(columnCount);

                            if ("Sales Record Number".equals(header)) {
                                orderImportCtx.put("orderId", orderNumberProfix + data);
                            } else if ("User Id".equals(header)) {
                                orderImportCtx.put("userId", data);
                            } else if ("Buyer Fullname".equals(header) || "buyer full name".equals(header.toLowerCase())) {
                                Map<String, Object> nameMap = FastMap.newInstance();
                                if (UtilValidate.isNotEmpty(data)) {
                                    nameMap = splitBuyerFullName(data);
                                }
                                orderImportCtx.put("toName", data);
                                orderImportCtx.put("firstName", (nameMap.get("firstName") != null) ? nameMap.get("firstName").toString() : null);
                                orderImportCtx.put("middleName", (nameMap.get("middleName") != null) ? nameMap.get("middleName").toString() : null);
                                orderImportCtx.put("lastName", (nameMap.get("lastName") != null) ? nameMap.get("lastName").toString() : null);
                            } else if ("Buyer Phone Number".equals(header)) {
                                Map<String, Object> telecomNumber = splitPhoneNumber(data);
                                orderImportCtx.put("countryCode", (telecomNumber.get("countryCode") != null) ? telecomNumber.get("countryCode") : null);
                                orderImportCtx.put("areaCode", (telecomNumber.get("areaCode") != null) ? telecomNumber.get("areaCode").toString() : null);
                                orderImportCtx.put("contactNumber", (telecomNumber.get("contactNumber") != null) ? telecomNumber.get("contactNumber").toString() : null);
                            } else if ("Buyer Email".equals(header)) {
                                orderImportCtx.put("emailAddress", data);
                            } else if ("Buyer Address 1".equals(header)) {
                                orderImportCtx.put("address1", data);
                            } else if ("Buyer Address 2".equals(header)) {
                                orderImportCtx.put("address2", data);
                            } else if ("Buyer City".equals(header) || "Buyer Town/City".equals(header)) {
                                orderImportCtx.put("city", data);
                            } else if ("Buyer State".equals(header) || "Buyer County".equals(header)) {
                                /*String stateOrProvince = checkStateProvinceGeoId(delegator, data);
                                if (UtilValidate.isNotEmpty(stateOrProvince)) {
                                    orderImportCtx.put("stateProvinceGeoId", stateOrProvince);
                                } else {
                                    orderImportCtx.put("city", (String) orderImportCtx.get("city") + " " + data);
                                }*/	orderImportCtx.put("stateProvinceGeoId", data);

                            } else if ("Buyer Zip".equals(header) || "Buyer Postcode".equals(header)) {
                                orderImportCtx.put("postalCode", data);
                            } else if ("Buyer Country".equals(header)) {
                                orderImportCtx.put("countryGeoId", getGeoId(delegator, data, "COUNTRY"));
                            } else if ("Sale Price".equals(header)) {
                            	String currencyUom = getCurrencyUom(delegator, data);
                            	if(currencyUom == null || "".equals(currencyUom.trim())) {
                            		currencyUom = productStore.getString("defaultCurrencyUomId");
                            	}
                                orderImportCtx.put("currencyUom", currencyUom);
                                orderImportCtx.put("remainingSubTotal", getPrice(data));
                                orderItemImport.put("unitPrice", getPrice(data));
                            } else if ("Shipping and Handling".equals(header) || "Postage and Handling".equals(header) || "Postage and Packaging".equals(header)) {
                                orderImportCtx.put("shippingAmount", getPrice(data));
                            } else if ("US Tax".equals(header)) {
                                orderImportCtx.put("taxAmount", getPrice(data));
                            } else if ("Insurance".equals(header)) {
                                orderImportCtx.put("warrantyAmount", getPrice(data));
                            } else if ("Cash on delivery fee".equals(header)) {
                                orderImportCtx.put("taxAdjustmentAmount", getPrice(data));
                            } else if ("Total Price".equals(header)) {
                                orderImportCtx.put("grandTotalAmount", getPrice(data));
                            } else if ("Payment Method".equals(header)) {
                                orderImportCtx.put("paymentMethodTypeId", getPaymentMethodTypeId(delegator, data));
                            } else if ("Sale Date".equals(header)) {
                                orderImportCtx.put("createDate", getTimestamp(data));
                                orderItemImport.put("saleDate", getTimestamp(data));
                            } else if ("Checkout Date".equals(header)) {
                                orderImportCtx.put("checkoutDate", getTimestamp(data));
                            } else if ("Paid on Date".equals(header)) {
                                orderImportCtx.put("paidOnDate", getTimestamp(data));
                            } else if ("Shipped on Date".equals(header)) {
                                orderImportCtx.put("shippedOnDate", getTimestamp(data));
                            } else if ("Notes to yourself".equals(header)) {
                                orderImportCtx.put("noteInfo", data);
                            } else if ("PayPal Transaction ID".equals(header)) {
                                orderImportCtx.put("paypalTransactionId", data);
                            } else if ("Shipping Service".equals(header) || "Postage Service".equals(header)) {
                                orderImportCtx.put("productStoreShipMethId", getShipmentMethodId(delegator, data, productStoreId, (String) orderImportCtx.get("countryGeoId"), (BigDecimal) orderImportCtx.get("grandTotalAmount")));
                            } else if ("Cash on delivery option".equals(header)) {
                                orderImportCtx.put("cashOnDeliveryOption", data);
                            } else if ("Order ID".equals(header)) {
                                orderImportCtx.put("externalOrderId", data);
                            } else if ("Variation Details".equals(header)) {
                                orderImportCtx.put("variationDetails", data);
                            } else if ("Item Number".equals(header)) {
                                orderItemImport.put("orderItemNumber", data);
                            } else if ("Item Title".equals(header)) {
                                orderItemImport.put("productName", data);
                            } else if ("Custom Label".equals(header)) {
                                orderItemImport.put("productId", data);
                            } else if ("Quantity".equals(header)) {
                                orderItemImport.put("quantity", (UtilValidate.isNotEmpty(data)) ? new BigDecimal(data) : BigDecimal.ZERO);
                            } else if ("Transaction ID".equals(header)) {
                                if(data != null && data.contains("+")) {
                                	return ServiceUtil.returnError("Please check the column 'Transaction ID' and make sure that its value is pure number without '+'.");
                                }
                                orderItemImport.put("transactionId", data);
                            }
                        }
                        //Yasin - Fix stateProvinceGeoId - Start - 6 Oct 2012
                        String stateOrProvince = checkStateProvinceGeoId(delegator, (String) orderImportCtx.get("stateProvinceGeoId"), (String) orderImportCtx.get("countryGeoId"));
                        if (UtilValidate.isNotEmpty(stateOrProvince)) {
                            orderImportCtx.put("stateProvinceGeoId", stateOrProvince);
                        } else {
                            orderImportCtx.put("city", (String) orderImportCtx.get("city") + " " + orderImportCtx.get("stateProvinceGeoId"));
                            orderImportCtx.put("stateProvinceGeoId", "_NA_");
                        }
                        //Yasin - Fix stateProvinceGeoId - End
                        try {
                            String checkNoOrderValue = csv.get(0);
                            if (UtilValidate.isNotEmpty(checkNoOrderValue) && !"record(s) downloaded".equals(csv.get(1).trim()) && !checkNoOrderValue.startsWith("Seller ID") && UtilValidate.isNotEmpty(csv.get(0).trim())) {
                                result = dispatcher.runSync("createOrderImport", orderImportCtx);
                                if(ServiceUtil.isError(result)) {
                                    errorList.add(ServiceUtil.getErrorMessage(result));
                                }
                                if (UtilValidate.isNotEmpty(orderItemImport.get("productId"))) {
                                    orderItemImport.put("orderImportId", newOrderImportId);
                                    orderItemImport.put("fileLineNumber", new BigDecimal(csv.getCurrentRecord() + 3));
                                    orderItemImport.put("userLogin", userLogin);
                                    Map<String, Object> orderItemResult = dispatcher.runSync("createOrderItemImport", orderItemImport);
                                    if(ServiceUtil.isError(result)) {
                                        errorList.add(ServiceUtil.getErrorMessage(orderItemResult));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        return ServiceUtil.returnSuccess();
    }

    private static Map<String, Object> splitBuyerFullName(String buyerFullName){
        String[] name = buyerFullName.split(" ");
        Map<String, Object> nameMap =  FastMap.newInstance();
        if (name.length == 2) {
            nameMap.put("firstName", name[0].trim());
            nameMap.put("middleName", null);
            nameMap.put("lastName", name[1].trim());
        } else if (name.length == 3) {
            nameMap.put("firstName", name[0].trim());
            nameMap.put("middleName", name[1].trim());
            nameMap.put("lastName", name[2].trim());
        } else if (name.length == 4) {
            nameMap.put("firstName", name[0].trim());
            nameMap.put("middleName", name[1].trim() + " " + name[2].trim());
            nameMap.put("lastName", name[3].trim());
        } else {
            nameMap.put("firstName", buyerFullName);
            nameMap.put("middleName", null);
            nameMap.put("lastName", null);
        }
        return nameMap;
    }

    private static Timestamp getTimestamp(String dateStr) {
        if (UtilValidate.isEmpty(dateStr)) {
            return new Timestamp(System.currentTimeMillis());
        }
        String datePattern = "MM-dd-yy";
        if (dateStr.contains("/")) {
            dateStr = dateStr.replace("/", "-");
            datePattern = "MM-dd-yy";
        } else if (dateStr.contains(".") && dateStr.indexOf(".") > 2) {
            datePattern = "MM-dd-yy HH:mm";
            String[] aliDate = dateStr.split(" ");
            String date = aliDate[0].substring(aliDate[0].lastIndexOf(".") + 1).trim();
            String month = aliDate[0].substring(aliDate[0].indexOf(".") + 1, aliDate[0].lastIndexOf(".")).trim();
            String year = aliDate[0].substring(0, aliDate[0].indexOf(".")).trim();
            if (aliDate[1] != null) {
                String hour = aliDate[1].substring(0, aliDate[1].indexOf(":")).trim();
                String minute = aliDate[1].substring(aliDate[1].lastIndexOf(":") + 1).trim();
                dateStr = month + "-" + date + "-" + year + " " + hour + ":" + minute;
            } else {
                dateStr = month + "-" + date + "-" + year;
            }
        }
        Timestamp result = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            Date toDate = dateFormat.parse(dateStr);
            result = new Timestamp(toDate.getTime());
        } catch (Exception e) {
            return new Timestamp(System.currentTimeMillis());
        }
        return result != null ? result : new Timestamp(System.currentTimeMillis());
    }

    private static String getShipmentMethodId(Delegator delegator, String shipmentName, String productStoreId, String countryGeoId, BigDecimal grandTotalAmount) {

        String productStoreShipMethId = shipmentName;
        String enumTypeId = null;
        
        try {   //main try -- START
            GenericValue productStoreEbaySetting = delegator.findOne("ProductStoreEbaySetting", UtilMisc.toMap("productStoreId", productStoreId), false);
            BigDecimal lowPriceThreshold = productStoreEbaySetting.getBigDecimal("lowPriceThreshold");
            
            if (grandTotalAmount.compareTo(lowPriceThreshold) <= 0) {   //if grandTotalAmount is <= 5 -- START
                Debug.logError("Yasin check this: grandTotalAmount is " + grandTotalAmount, module);
                List<GenericValue> productStoreShipmentMethList = delegator.findByAnd("ProductStoreShipmentMeth", UtilMisc.toMap("productStoreId", productStoreId, "shipmentMethodTypeId", "STANDARD"), null, false);
                GenericValue productStoreShipmentMeth = EntityUtil.getFirst(productStoreShipmentMethList);
                productStoreShipMethId = productStoreShipmentMeth.getString("productStoreShipMethId");
            }   //if grandTotalAmount is <= 5 -- END
            else {  //if grandTotalAmount is > 5 -- START
                List<GenericValue> enumerationTypes= delegator.findByAnd("EnumerationType", UtilMisc.toMap("description", productStoreId, "parentTypeId", "PROSTORE_EXSHIP"), null, false);
                if (enumerationTypes.size() > 0) {
                    GenericValue enumerationType = EntityUtil.getFirst(enumerationTypes);
                    enumTypeId = enumerationType.getString("enumTypeId");
                }
                
                List<GenericValue> enumerations = delegator.findByAnd("Enumeration", UtilMisc.toMap("description", shipmentName, "enumTypeId", enumTypeId), null, false);
                if (enumerations.size() > 0 && UtilValidate.isNotEmpty(enumTypeId)) {   //if enumeration is not empty -- START
                    // to check if there is a country set in the shipping mapping
                    boolean isCountryGeoSet = false;
                    for(GenericValue enumeration : enumerations) {
                        if(enumeration.getString("sequenceId") != null) {
                            isCountryGeoSet = true;
                            break;
                        }
                    }
                    
                    // use the first mapping if there is no country set there
                    if(!isCountryGeoSet) {
                        GenericValue enumeration = EntityUtil.getFirst(enumerations);
                        String enumCode = enumeration.getString("enumCode");
                        if (UtilValidate.isNotEmpty(enumCode)) {
                            productStoreShipMethId = enumCode;
                        }
                    } else {
                        for(GenericValue enumeration : enumerations) {
                            String geoId = enumeration.getString("sequenceId");
                            String enumCode = enumeration.getString("enumCode"); // product store shipment method id
                            if(geoId == null) {
                                productStoreShipMethId = enumCode; // this will be overwrite if there is a geo-matched mapping after this one
                                continue;
                            }
                            
                            if (!UtilValidate.isNotEmpty(enumCode)) {
                                continue;
                            }
                            
                            GenericValue countryGeo = delegator.findOne("Geo", UtilMisc.toMap("geoId", geoId), false);
                            if(!"GROUP".equals(countryGeo.getString("geoTypeId"))) { // check if the country of order
                                //is same with the one in the rule
                                if(geoId.equals(countryGeoId)) {
                                    productStoreShipMethId = enumCode;
                                    break;
                                }
                            } else { //check if the country in the order matches the geo set in the mapping matches
                                List<GenericValue> countriesInGroup = expandGeoGroup(countryGeo);
                                if(containsGeo(countriesInGroup, countryGeoId, delegator)) {
                                    productStoreShipMethId = enumCode;
                                    break;
                                }
                            }
                        }
                    }
                }   //if enumeration is not empty -- END
            }   //if grandTotalAmount is > 5 -- END
        }   //main try -- END
        catch (Exception e) {
            return productStoreShipMethId;
        }
        return productStoreShipMethId;
    }

    public static boolean containsGeo(List<GenericValue> geoList, String geoId, Delegator delegator) {
        GenericValue geo = null;
        try {
            geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", geoId), false);
        } catch (GenericEntityException e) {
            Debug.logError(e, "Unable to look up Geo from geoId : " + geoId, module);
        }
        return containsGeo(geoList, geo);
    }

    public static boolean containsGeo(List<GenericValue> geoList, GenericValue geo) {
        if (geoList == null || geo == null) {
            return false;
        }
        //Debug.log("Contains Geo : " + geoList.contains(geo));
        return geoList.contains(geo);
    }

    public static List<GenericValue> expandGeoGroup(GenericValue geo) {
        if (geo == null) {
            return FastList.newInstance();
        }
        if (!"GROUP".equals(geo.getString("geoTypeId"))) {
            return UtilMisc.toList(geo);
        }

        //Debug.log("Expanding geo : " + geo, module);

        List<GenericValue> geoList = FastList.newInstance();
        List<GenericValue> thisGeoAssoc = null;
        try {
            thisGeoAssoc = geo.getRelated("AssocGeoAssoc", UtilMisc.toMap("geoAssocTypeId", "GROUP_MEMBER"), null, false);
        } catch (GenericEntityException e) {
            Debug.logError(e, "Unable to get associated Geo GROUP_MEMBER relationship(s)", module);
        }
        if (UtilValidate.isNotEmpty(thisGeoAssoc)) {
            for (GenericValue nextGeoAssoc: thisGeoAssoc) {
                GenericValue nextGeo = null;
                try {
                    nextGeo = nextGeoAssoc.getRelatedOne("MainGeo", false);
                } catch (GenericEntityException e) {
                    Debug.logError(e, "Unable to get related Geo", module);
                }
                geoList.addAll(expandGeoGroup(nextGeo));
            }
        } else {
            //Debug.log("No associated geos with this group", module);
        }

        //Debug.log("Expanded to : " + geoList, module);

        return geoList;
    }

    private static String getPaymentMethodTypeId(Delegator delegator, String paymentMethodStr) throws GenericEntityException {
        String paymentMethodTypeId = paymentMethodStr;
        try {
            List<GenericValue> enumerations = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumCode", paymentMethodStr, "enumTypeId", "PAYMENT_MAP"), null, false);
            if (enumerations.size() > 0) {
                GenericValue enumeration = EntityUtil.getFirst(enumerations);
                paymentMethodTypeId = enumeration.getString("enumId");
            }
        } catch (Exception e) {
            return "error";
        }
        return paymentMethodTypeId;
    }

    private static String getCurrencyUom(Delegator delegator, String priceStr) throws GenericEntityException {
        String currency = null;
        if (priceStr.startsWith("$")) {
            currency = "USD";
        } else if (priceStr.startsWith("GBP")) {
            currency = "GBP";
        } else if (priceStr.startsWith("C $")) {
            currency = "CAD";
        } else if (priceStr.startsWith("AU $")) {
            currency = "AUD";
        } else if (priceStr.startsWith("EUR")) {
            currency = "EUR";
        } else if (!priceStr.startsWith("$") && !priceStr.contains("$")) {
            String geoCode =priceStr.replaceAll("[0-9,.]", "");
            GenericValue enumeration = delegator.findOne("Enumeration", UtilMisc.toMap("enumId", geoCode), false);
            if (UtilValidate.isNotEmpty(enumeration)) {
                currency = enumeration.getString("enumCode");
            }
        } else if (priceStr.contains(" ") && !priceStr.contains("$")) {
            String[] arr = priceStr.split(" ");
            currency = arr[0];
        } else {
            currency = null;
        }
        return currency;
    }

    private static BigDecimal getPrice(String price) {
        BigDecimal result = BigDecimal.ZERO;
        try {
	        price = price.replaceAll("[^0-9,.-]", "");
	        if(!price.contains(".")) { // in latin countries a comma is used to split the integer and the fractional part
				int indexComma = price.lastIndexOf(",");
				if(indexComma >= 0) {
					price = price.substring(0, indexComma) + "." + price.substring(indexComma+1);
				}
			}
	        price = price.replaceAll(",", "");
	        if (price.startsWith("$")) {
	            result = new BigDecimal(price.substring(1));
	        } else if (price.contains("$") && !price.startsWith("$")) {
	            price = price.substring(price.indexOf("$") + 1);
	            result = new BigDecimal(price);
	        } else if (price.contains(" ") && !price.contains("$")) {
	            String[] arr = price.split(" ");
	            result = new BigDecimal(arr[1]);
	        } else {
	    			result = new BigDecimal(price);
	        }
		} catch (NumberFormatException e) {
			result = BigDecimal.ZERO;
		}
        return result;
    }

    private static String getGeoId(Delegator delegator, String countryName, String geoType) {
    	countryName = countryName.trim();
            String countryGeoId = countryName;
            try {
    	countryName = UtilProperties.getPropertyValue("countryMapping.properties", countryName.replaceAll(" ", "_"), countryName);
                List<GenericValue> countryGeoList = delegator.findByAnd("Geo", UtilMisc.toMap("geoName", countryName.trim(), "geoTypeId", geoType), null, false);
                if (countryGeoList.size() > 0) {
                    GenericValue geo = EntityUtil.getFirst(countryGeoList);
                    countryGeoId = geo.getString("geoId");
                }
            } catch (Exception e) {
                return countryGeoId;
            }
            return countryGeoId;
        }

    private static String getGeoIdFromGeoCode(Delegator delegator, String countryName, String geoType) {
    	countryName = countryName.trim();
            String countryGeoId = countryName;
            try {
    	countryName = UtilProperties.getPropertyValue("countryMapping.properties", countryName.replaceAll(" ", "_"), countryName);
                List<GenericValue> countryGeoList = delegator.findByAnd("Geo", UtilMisc.toMap("geoCode", countryName.trim(), "geoTypeId", geoType), null, false);
                if (countryGeoList.size() > 0) {
                    GenericValue geo = EntityUtil.getFirst(countryGeoList);
                    countryGeoId = geo.getString("geoId");
                }
            } catch (Exception e) {
                return countryGeoId;
            }
            return countryGeoId;
        }

    private static Map<String, Object> splitPhoneNumber(String phoneNumber) {
        Map<String, Object> phoneMap =  FastMap.newInstance();
        String countryCode = null;
        String areaCode = null;
        String contactNumber = null;
        if (phoneNumber.startsWith("+") && phoneNumber.contains("(") && phoneNumber.contains(")")) {
            countryCode = phoneNumber.substring(0, phoneNumber.indexOf("(")).trim();
            areaCode = phoneNumber.substring(phoneNumber.indexOf("("), phoneNumber.indexOf(")") + 1).trim();
            contactNumber = phoneNumber.substring(phoneNumber.lastIndexOf(")") + 1).trim();
        } else if (!phoneNumber.contains("+") && phoneNumber.startsWith("(") && phoneNumber.contains(")")) {
            areaCode = phoneNumber.substring(0, phoneNumber.indexOf(")") + 1).trim();
            contactNumber = phoneNumber.substring(phoneNumber.lastIndexOf(")") +1).trim();
        } else if (phoneNumber.contains("-") && !phoneNumber.contains("(") && !phoneNumber.contains("+")) {
            areaCode = phoneNumber.substring(0, phoneNumber.indexOf("-")).trim();
            contactNumber = phoneNumber.substring(phoneNumber.indexOf("-") + 1, phoneNumber.length()).trim();
        } else {
            contactNumber = phoneNumber.trim();
        }
        if (countryCode != null) { phoneMap.put("countryCode", countryCode); }
        if (areaCode != null) { phoneMap.put("areaCode", areaCode); }
        phoneMap.put("contactNumber", contactNumber);
        return phoneMap;
    }

    private static String checkStateProvinceGeoId(Delegator delegator, String state) {
        return checkStateProvinceGeoId(delegator, state, null);
    }

    private static String toTitleCase(String input) {
	StringBuilder titleCase = new StringBuilder();
	boolean nextTitleCase = true;

	for (char c : input.toCharArray()) {
		if (Character.isSpaceChar(c)) {
			nextTitleCase = true;
		} else if (nextTitleCase) {
			c = Character.toTitleCase(c);
			nextTitleCase = false;
		}
		titleCase.append(c);
	}
	return titleCase.toString();
    }

    private static String checkStateProvinceGeoId(Delegator delegator, String state, String countryId) {
        String geoId = null;
	try {
	    GenericValue geo = EntityUtil.getFirst(delegator.findByAnd("GeoAssocAndGeoTo", UtilMisc.toMap("geoIdFrom", countryId, "geoCode", state.toUpperCase()), null, false));
	    if (UtilValidate.isNotEmpty(geo)) {
		geoId = geo.getString("geoId");
	    } else {
		state = toTitleCase(state.toLowerCase());
		geo = EntityUtil.getFirst(delegator.findByAnd("GeoAssocAndGeoTo", UtilMisc.toMap("geoIdFrom", countryId, "geoName", state), null, false));
		if (UtilValidate.isNotEmpty(geo)) {
		    geoId = geo.getString("geoId");
		}
	    }
        /*try { // geo id
            GenericValue geo = delegator.findOne("Geo", UtilMisc.toMap("geoId", state.toUpperCase()), false);
            if (UtilValidate.isNotEmpty(geo)) {
                geoId = geo.getString("geoId");
            } else if(countryId != null){ // geo id = country + state
            	geo = EntityUtil.getFirst(delegator.findByAnd("Geo", UtilMisc.toMap("geoId", countryId + "-" + state.toUpperCase()), null, false));
            	if (UtilValidate.isNotEmpty(geo)) {
                    geoId = geo.getString("geoId");
            	}
            }

            if(geoId == null) {
            	List<GenericValue> nextGeos = delegator.findByAnd("Geo", UtilMisc.toMap("geoName", state), null, false);
                if (UtilValidate.isNotEmpty(nextGeos)) {
                    GenericValue nextGeo = EntityUtil.getFirst(nextGeos);
                    geoId = nextGeo.getString("geoId");
                }
            }*/
        } catch (Exception e) {
            return geoId;
        }

        return geoId;
    }

    public static Map<String, Object> deleteFileOrderImport(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        if (UtilValidate.isEmpty(context.get("fileName"))) {
            result = ServiceUtil.returnError("Required fileName parameter.");
            result.put("productStoreId", (String) context.get("productStoreId"));
            return result;
        }
        try {
            List<GenericValue> orderImportList = delegator.findByAnd("OrderImport", UtilMisc.toMap("fileName", (String) context.get("fileName")), null, false);
            if (orderImportList.size() > 0) {
                for (GenericValue orderImport : orderImportList) {
                    dispatcher.runSync("deleteOrderImport", UtilMisc.toMap("orderImportId", orderImport.getString("orderImportId"), "userLogin", userLogin));
                }
                long orderImportCount = delegator.findCountByCondition("OrderImport", EntityCondition.makeCondition(UtilMisc.toMap("fileName", (String) context.get("fileName"))), null, null);
                if (orderImportCount == 0) {
                    File file = new File((String) context.get("fileName"));
                    if (!file.exists()) {
                        result = ServiceUtil.returnError("This file doesn't exist.");
                        result.put("productStoreId", (String) context.get("productStoreId"));
                        return result;
                    }
                    file.delete();
                }
            }
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
            result.put("productStoreId", (String) context.get("productStoreId"));
            return result;
        }
        result = ServiceUtil.returnSuccess("Delete OrderImport and file successful.");
        result.put("productStoreId", (String)context.get("productStoreId"));
        return result;
    }

    public static Map<String, Object> doUploadInternalOrderCsvFile(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = FastMap.newInstance();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        ByteBuffer imageData = (ByteBuffer) context.get("uploadedFile");
        String uploadFileName = (String) context.get("_uploadedFile_fileName");
        String fileType = (String) context.get("_uploadedFile_contentType");
        if (!"text/csv".equals(fileType)) {
            return ServiceUtil.returnError("Incorrect file format .. CSV needed");
        }
        try {
            String fileServerPath = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("orderImport", "orderimport.server.path"), context);
            File rootTargetDir = new File(fileServerPath + "/Internal Order");
            if (!rootTargetDir.exists()) {
                boolean created = rootTargetDir.mkdirs();
                if (!created) {
                    String errMsg = "Not create target directory";
                    Debug.logFatal(errMsg, module);
                    return ServiceUtil.returnError(errMsg);
                }
            }
            String fileName = uploadFileName.substring(0, uploadFileName.indexOf(".")) + "_" +UtilDateTime.nowDateString() + ".csv";
            String filePath = rootTargetDir + "/" + fileName;
            File file = new File(filePath);
            try {
                RandomAccessFile out = new RandomAccessFile(file, "rw");
                out.write(imageData.array());
                out.close();
            } catch (FileNotFoundException e) {
                Debug.logError(e, module);
                return ServiceUtil.returnError(e.getMessage());
            } catch (IOException e) {
                Debug.logError(e, module);
                return ServiceUtil.returnError(e.getMessage());
            }
            if (file.exists()) {
                readDataFromCsvAndImport(dispatcher, delegator, userLogin, filePath);
            }
        } catch (Exception e) {
        	return ServiceUtil.returnError(e.getMessage());
        }
        return result;
    }
    private static Map<String, Object> readDataFromCsvAndImport(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String filePath) {
        Map<String, Object> result = FastMap.newInstance();
        try {
            if (UtilValidate.isEmpty(filePath)) {
                return ServiceUtil.returnError("This file is not exist.");
            }
            CsvReader csv = new CsvReader(new FileInputStream(new File(filePath)), Charset.forName("UTF-8"));
            if (UtilValidate.isNotEmpty(csv)) {
                csv.readHeaders();
                while (csv.readRecord()) {
                    List<GenericValue> orderImports = delegator.findByAnd("OrderImport", UtilMisc.toMap("orderId", csv.get("orderId"), "fileName", filePath), null, false);
                    if (orderImports.size() > 0) {
                        GenericValue orderImport = EntityUtil.getFirst(orderImports);
                        setOrderItemImport(dispatcher, userLogin, csv, orderImport.getString("orderImportId"));
                    }else {
                        Map<String, Object> orderImportCtx = FastMap.newInstance();
                        String newOrderImportId = delegator.getNextSeqId("OrderImport");
                        for (int columnCount = 0; columnCount < csv.getHeaderCount(); columnCount++) {
                            String header = csv.getHeader(columnCount);
                            String data = csv.get(columnCount);
                            if ("shipmentMethodTypeId".equals(header)) {
                                String partyId = csv.get("carrierPartyId");
                                List<GenericValue> productStoreShipmentMethes = delegator.findByAnd("ProductStoreShipmentMeth", UtilMisc.toMap("shipmentMethodTypeId", csv.get("shipmentMethodTypeId"), "partyId", partyId), null, false);
                                if (productStoreShipmentMethes.size() > 0) {
                                    GenericValue productStoreShipmentMeth = EntityUtil.getFirst(productStoreShipmentMethes);
                                    data = productStoreShipmentMeth.getString("productStoreShipMethId");
                                    orderImportCtx.put("productStoreShipMethId", data);
                                }
                            } else if ("carrierPartyId".equals(header)) {
                                continue;
                            } else if ("createDate".equals(header) || "checkoutDate".equals(header) || "paidOnDate".equals(header) || "shippedOnDate".equals(header)) {
                                orderImportCtx.put(header, toTimestamp(data));
                            } else if ("orderItemNumber".equals(header) || "productId".equals(header) || "productName".equals(header) || "quantity".equals(header) ||  "transactionId".equals(header) || "unitPrice".equals(header) || "saleDate".equals(header)) {
                                continue;
                            } else if ("remainingSubTotal".equals(header) || "shippingAmount".equals(header) || "taxAdjustmentAmount".equals(header) || "warrantyAmount".equals(header) || "grandTotalAmount".equals(header) || "quantity".equals(header) || "taxAmount".equals(header)) {
                                orderImportCtx.put(header, (UtilValidate.isNotEmpty(data)) ? new BigDecimal(data) : BigDecimal.ZERO);
                            } else {
                                orderImportCtx.put(header, data);
                            }
                        }
                        orderImportCtx.put("orderImportId", newOrderImportId);
                        orderImportCtx.put("productStoreId", csv.get("productStoreId"));
                        orderImportCtx.put("fileName", filePath);
                        orderImportCtx.put("fileLineNumber", new BigDecimal(csv.getCurrentRecord() + 1));
                        orderImportCtx.put("importedStatus", "N");
                        orderImportCtx.put("userLogin", userLogin);
                        result = dispatcher.runSync("createOrderImport", orderImportCtx);
                        if (UtilValidate.isNotEmpty(csv.get("productId"))) {
                            setOrderItemImport(dispatcher, userLogin, csv, newOrderImportId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
        }
        return result;
    }

    private static Timestamp toTimestamp (String dateStr) {
        Timestamp result = null;
        if (UtilValidate.isEmail(dateStr)) {
            return null;
        }
        if (dateStr.contains("/")) {
            dateStr = dateStr.replace("/", "-");
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date toDate = dateFormat.parse(dateStr);
            result = new Timestamp(toDate.getTime());
        } catch (Exception e) {
            return null;
        }
        return result;
    }
    private static Map<String, Object> setOrderItemImport(LocalDispatcher dispatcher, GenericValue userLogin, CsvReader csv, String orderImportId) {
        Map<String, Object> result = FastMap.newInstance();
        try {
            Map<String, Object> orderItemImport = FastMap.newInstance();
            orderItemImport.put("orderItemNumber", csv.get("orderItemNumber"));
            orderItemImport.put("productName", csv.get("productName"));
            orderItemImport.put("productId", csv.get("productId"));
            orderItemImport.put("quantity", (UtilValidate.isNotEmpty(csv.get("quantity"))) ? new BigDecimal(csv.get("quantity")) : BigDecimal.ZERO);
            orderItemImport.put("transactionId", csv.get("transactionId"));
            orderItemImport.put("unitPrice", (UtilValidate.isNotEmpty(csv.get("unitPrice"))) ? new BigDecimal(csv.get("unitPrice")) : BigDecimal.ZERO);
            orderItemImport.put("saleDate", toTimestamp(csv.get("saleDate")));
            orderItemImport.put("orderImportId", orderImportId);
            orderItemImport.put("fileLineNumber", new BigDecimal(csv.getCurrentRecord() + 1));
            orderItemImport.put("userLogin", userLogin);
            result = dispatcher.runSync("createOrderItemImport", orderItemImport);
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
        }
        return result;
    }

    public static Map<String, Object> doDeleteOrderImportAndFile(DispatchContext dctx, Map<String, ? extends Object> context) {
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        LocalDispatcher dispatcher = dctx.getDispatcher();
        try {
            List<GenericValue> orderImportList = delegator.findByAnd("OrderImport", UtilMisc.toMap("fileName", (String) context.get("filePath")), null, false);
            if (orderImportList.size() > 0) {
                for (GenericValue orderImport : orderImportList) {
                    dispatcher.runSync("deleteOrderImport", UtilMisc.toMap("orderImportId", orderImport.getString("orderImportId"), "userLogin", userLogin));
                }
                long orderImportCount = delegator.findCountByCondition("OrderImport", EntityCondition.makeCondition(UtilMisc.toMap("fileName", (String) context.get("filePath"))), null, null);
                if (orderImportCount == 0) {
                    File file = new File((String) context.get("filePath"));
                    if (!file.exists()) {
                        return ServiceUtil.returnError("This file doesn't exist.");
                    }
                    file.delete();
                }
            }
        } catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        return ServiceUtil.returnSuccess("Delete OrderImport and file successful.");
    }

    private static Map<String, Object> readDataFromMagentoCsv(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String productStoreId, String filePath) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        List<String> errorList = FastList.newInstance();
        try {
            String orderNumberProfix = null;
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            if (UtilValidate.isNotEmpty(productStore.get("orderNumberPrefix"))) {
                orderNumberProfix = productStore.getString("orderNumberPrefix");
            }
            CsvReader csv = new CsvReader(new FileInputStream(new File(filePath)), Charset.forName("WINDOWS-1252"));
            if (UtilValidate.isNotEmpty(csv)) {
                csv.readHeaders();
                Timestamp today = new Timestamp(System.currentTimeMillis());
                while (csv.readRecord()) {
                    List<GenericValue> orderImports = delegator.findByAnd("OrderImport", UtilMisc.toMap("orderId", orderNumberProfix + csv.get(0), "fileName", filePath), null, false);
                    String shippingMthodFromMagento = null;
                    if (orderImports.size() > 0) {
                        GenericValue orderImport = EntityUtil.getFirst(orderImports);
                        Map<String, Object> orderItemImport = FastMap.newInstance();
                        orderItemImport.put("orderImportId", orderImport.getString("orderImportId"));
                        orderItemImport.put("fileLineNumber", new BigDecimal(csv.getCurrentRecord() + 3));
                        orderItemImport.put("userLogin", userLogin);
                        orderItemImport.put("saleDate", today);
                        for (int columnCount = 0; columnCount < csv.getHeaderCount(); columnCount++) {
                            String data = csv.get(columnCount);
                            String header = csv.getHeader(columnCount);
                            if ("Order Item Increment".equals(header)) {
                                orderItemImport.put("orderItemNumber", data);
                            } else if ("Item Name".equals(header)) {
                                orderItemImport.put("productName", data);
                            } else if ("Item SKU".equals(header)) {
                                orderItemImport.put("productId", data);
                            } else if ("Item Qty Ordered".equals(header)) {
                                orderItemImport.put("quantity", (UtilValidate.isNotEmpty(data)) ? new BigDecimal(data) : BigDecimal.ZERO);
                            } else if ("Transaction ID".equals(header)) {
                                orderItemImport.put("transactionId", data);
                            } else if ("Item Price".equals(header)) {
                                orderItemImport.put("unitPrice", getPrice(data));
                            }
                        }
                        result = dispatcher.runSync("createOrderItemImport", orderItemImport);
                    } else {
                        Map<String, Object> orderImportCtx = FastMap.newInstance();
                        Map<String, Object> orderItemImport = FastMap.newInstance();
                        String newOrderImportId = delegator.getNextSeqId("OrderImport");
                        orderImportCtx.put("orderImportId", newOrderImportId);
                        orderImportCtx.put("productStoreId", productStoreId);
                        orderImportCtx.put("fileName", filePath);
                        orderImportCtx.put("fileLineNumber", new BigDecimal(csv.getCurrentRecord() + 3));
                        orderImportCtx.put("importedStatus", "N");
                        orderImportCtx.put("userLogin", userLogin);
                        orderImportCtx.put("stateProvinceGeoId", "_NA_"); // set _NA_ as default state
                        String state = null; // sometimes need COUNTRY_CODE + STATE_CODE to search from the Geo because the state may be a geo code from Mangento
                        for (int columnCount = 0; columnCount < csv.getHeaderCount(); columnCount++) {
                            String data = csv.get(columnCount);
                            String header = csv.getHeader(columnCount);

                            orderImportCtx.put("createDate", today);
                            orderItemImport.put("saleDate", today);
                            orderImportCtx.put("checkoutDate", today);
                            orderImportCtx.put("paidOnDate", today);

                            if ("Order Number".equals(header)) {
                                orderImportCtx.put("orderId", orderNumberProfix + data);
                                orderImportCtx.put("externalOrderId", data);
                            } else if ("Customer Email".equals(header)) {
                                orderImportCtx.put("userId", data);
                                orderImportCtx.put("emailAddress", data);
                            } else if ("Shipping Name".equals(header)) {
                                Map<String, Object> nameMap = FastMap.newInstance();
                                if (UtilValidate.isNotEmpty(data)) {
                                    nameMap = splitBuyerFullName(data);
                                }
                                orderImportCtx.put("toName", data);
                                orderImportCtx.put("firstName", (nameMap.get("firstName") != null) ? nameMap.get("firstName").toString() : null);
                                orderImportCtx.put("middleName", (nameMap.get("middleName") != null) ? nameMap.get("middleName").toString() : null);
                                orderImportCtx.put("lastName", (nameMap.get("lastName") != null) ? nameMap.get("lastName").toString() : null);
                            } else if ("Shipping Phone Number".equals(header)) {
                                Map<String, Object> telecomNumber = splitPhoneNumber(data);
                                orderImportCtx.put("countryCode", (telecomNumber.get("countryCode") != null) ? telecomNumber.get("countryCode") : null);
                                orderImportCtx.put("areaCode", (telecomNumber.get("areaCode") != null) ? telecomNumber.get("areaCode").toString() : null);
                                orderImportCtx.put("contactNumber", (telecomNumber.get("contactNumber") != null) ? telecomNumber.get("contactNumber").toString() : null);
                            }  else if ("Shipping Street".equals(header)) {
                                orderImportCtx.put("address1", data);
//                            }
//                            else if ("Buyer Address 2".equals(header)) {
//                                orderImportCtx.put("address2", data);
                            } else if ("Shipping City".equals(header)) {
                                orderImportCtx.put("city", data);
                            }  else if ("Shipping State Name".equals(header)) {
                            	state = data;
                                String stateOrProvince = checkStateProvinceGeoId(delegator, data);
                                if (UtilValidate.isNotEmpty(stateOrProvince)) { // set the state id
                                    orderImportCtx.put("stateProvinceGeoId", stateOrProvince);
                                } else { // append state name after the city
                                	orderImportCtx.put("stateProvinceGeoId", "_NA_");
                                	orderImportCtx.put("city", (String) orderImportCtx.get("city") + " " + data);
                                }
                            }  else if ("Shipping Zip".equals(header) || "Buyer Postcode".equals(header)) {
                                orderImportCtx.put("postalCode", data);
                            } else if ("Shipping Country".equals(header)) {
                            	String countryGeoId = getGeoIdFromGeoCode(delegator, data, "COUNTRY");
                                orderImportCtx.put("countryGeoId", countryGeoId);
                                if(shippingMthodFromMagento != null) {
                                	orderImportCtx.put("productStoreShipMethId", getShipmentMethodId(delegator, shippingMthodFromMagento, productStoreId,
                                			(String) orderImportCtx.get("countryGeoId"), (BigDecimal) orderImportCtx.get("grandTotalAmount")));
                                }

                                // set the state
                                if("_NA_".equals(orderImportCtx.get("stateProvinceGeoId")) && (countryGeoId != null && countryGeoId.length() >= 2)) {
                                	String stateOrProvince = checkStateProvinceGeoId(delegator, state, countryGeoId.substring(0, 2));
                                	if(stateOrProvince != null) {
                                		orderImportCtx.put("stateProvinceGeoId", stateOrProvince);
                                	}
                                }

                            } else if ("Item Price".equals(header)) {
                                orderImportCtx.put("currencyUom", productStore.getString("defaultCurrencyUomId"));
                                orderImportCtx.put("remainingSubTotal", getPrice(data));
                                orderItemImport.put("unitPrice", getPrice(data));
                            } else if ("Order Shipping".equals(header) || "Postage and Handling".equals(header)) {
                                orderImportCtx.put("shippingAmount", getPrice(data));
                            } else if ("Order Tax".equals(header)) {
                                orderImportCtx.put("taxAmount", getPrice(data));
                            } else if ("Insurance".equals(header)) {
                                orderImportCtx.put("warrantyAmount", getPrice(data));
                            } else if ("Cash on delivery fee".equals(header)) {
                                orderImportCtx.put("taxAdjustmentAmount", getPrice(data));
                            } else if ("Order Grand Total".equals(header)) {
                                orderImportCtx.put("grandTotalAmount", getPrice(data));
                            } else if ("Order Payment Method".equals(header)) {
                            	String paymentMethodInOFBiz = null;
                            		if("paypal_express".equals(data)) {
                            			paymentMethodInOFBiz = "EXT_PAYPAL";
                            		} else if ("bankpayment".equals(data)) {
                            			paymentMethodInOFBiz = "EXT_WIRE_TRANSFER";
                            		} else if("sagepayserver".equals(data)) {
                            			paymentMethodInOFBiz = "CREDIT_CARD";
                            		}
                                orderImportCtx.put("paymentMethodTypeId", paymentMethodInOFBiz);
                            } else if ("Notes to yourself".equals(header)) {
                                orderImportCtx.put("noteInfo", data);
                            } else if ("PayPal Transaction ID".equals(header)) {
                                orderImportCtx.put("paypalTransactionId", data);
                            } else if ("Order Shipping Method".equals(header) || "Postage Service".equals(header)) {
                            	shippingMthodFromMagento = data;
                                orderImportCtx.put("productStoreShipMethId", getShipmentMethodId(delegator, data, productStoreId, (String) orderImportCtx.get("countryGeoId"), (BigDecimal) orderImportCtx.get("grandTotalAmount")));
                            } else if ("Cash on delivery option".equals(header)) {
                                orderImportCtx.put("cashOnDeliveryOption", data);
                            } else if ("Variation Details".equals(header)) {
                                orderImportCtx.put("variationDetails", data);
                            } else if ("Order Item Increment".equals(header)) {
                                orderItemImport.put("orderItemNumber", data);
                            } else if ("Item Name".equals(header)) {
                                orderItemImport.put("productName", data);
                            } else if ("Item SKU".equals(header)) {
                                orderItemImport.put("productId", data);
                            } else if ("Item Qty Ordered".equals(header)) {
                                orderItemImport.put("quantity", (UtilValidate.isNotEmpty(data)) ? new BigDecimal(data) : BigDecimal.ZERO);
                            } else if ("Transaction ID".equals(header)) {
                                orderItemImport.put("transactionId", data);
                            }
                        }
                        try {
                            String checkNoOrderValue = csv.get(0);
                            if (UtilValidate.isNotEmpty(checkNoOrderValue) && !"record(s) downloaded".equals(csv.get(1).trim()) && !checkNoOrderValue.startsWith("Seller ID") && UtilValidate.isNotEmpty(csv.get(0).trim())) {
                                result = dispatcher.runSync("createOrderImport", orderImportCtx);
                                if(ServiceUtil.isError(result)) {
                                    errorList.add(ServiceUtil.getErrorMessage(result));
                                }
                                if (UtilValidate.isNotEmpty(orderItemImport.get("productId"))) {
                                    orderItemImport.put("orderImportId", newOrderImportId);
                                    orderItemImport.put("fileLineNumber", new BigDecimal(csv.getCurrentRecord() + 3));
                                    orderItemImport.put("userLogin", userLogin);
                                    Map<String, Object> orderItemResult = dispatcher.runSync("createOrderItemImport", orderItemImport);
                                    if(ServiceUtil.isError(result)) {
                                        errorList.add(ServiceUtil.getErrorMessage(orderItemResult));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        return ServiceUtil.returnSuccess();
    }
    
    private static Map<String, Object> readDataFromAliXls(LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String productStoreId, String filePath) {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        List<String> errorList = FastList.newInstance();
        try {
            String orderNumberProfix = null;
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            if (UtilValidate.isNotEmpty(productStore.get("orderNumberPrefix"))) {
                orderNumberProfix = productStore.getString("orderNumberPrefix");
            }
            
            WorkbookSettings ws = new WorkbookSettings();
            ws.getCharacterSet();
            ws.getEncoding();
            Workbook workbook = Workbook.getWorkbook(new FileInputStream(new File(filePath)), ws);
            Sheet sheet = workbook.getSheet(0);
            
            //String message = null;
            if (UtilValidate.isNotEmpty(sheet)) {   //if sheet is not empty -- START
                for (int row = 1; row < sheet.getRows(); row++)  { //loop rows -- START
                    //String letter = "A";
                    Map<String, Object> orderImportCtx = FastMap.newInstance();
                    Map<String, Object> orderItemImport = FastMap.newInstance();
                    String newOrderImportId = delegator.getNextSeqId("OrderImport");
                    int totalQuantity = 0;
                    orderImportCtx.put("orderImportId", newOrderImportId);
                    orderImportCtx.put("productStoreId", productStoreId);
                    orderImportCtx.put("fileName", filePath);
                    orderImportCtx.put("fileLineNumber", new BigDecimal(row+1));
                    orderImportCtx.put("importedStatus", "N");
                    orderImportCtx.put("userLogin", userLogin);
                    orderImportCtx.put("stateProvinceGeoId", "_NA_"); // set _NA_ as default state
                    for (int column = 0; column < sheet.getColumns(); column++)    {   //loop columns -- START
                        /*int charValue = letter.charAt(0);
                         message += "Row " + (row + 1) + ", Column " + letter + " is " + sheet.getCell(column, row).getContents() + eol;
                         letter = String.valueOf( (char) (charValue + 1));
                         result = ServiceUtil.returnSuccess(message);*/
                        String data = sheet.getCell(column, row).getContents().trim();
                        String header = sheet.getCell(column, 0).getContents().trim();
                        Map<String, Object> telecomNumber = FastMap.newInstance();
                        
                        if ("订单号".equals(header)) { //populating orderImportCtx -- START
                            orderImportCtx.put("orderId", orderNumberProfix + data);
                            orderImportCtx.put("externalOrderId", data);
                        } else if ("买家邮箱".equals(header)) {
                            orderImportCtx.put("userId", data);
                            orderImportCtx.put("emailAddress", data);
                        } else if ("买家名称".equals(header)) {
                            Map<String, Object> nameMap = FastMap.newInstance();
                            //Debug.logError("buyer Name is " + data, module);
                            if (UtilValidate.isNotEmpty(data)) {
                                nameMap = splitBuyerFullName(data);
                            }
                            orderImportCtx.put("firstName", (nameMap.get("firstName") != null) ? nameMap.get("firstName").toString() : null);
                            if (nameMap.get("middleName") != null) { orderImportCtx.put("middleName", nameMap.get("middleName")); }
                            if (nameMap.get("lastName") != null) { orderImportCtx.put("lastName", nameMap.get("lastName")); }
                        } else if ("联系电话".equals(header)) {
                            telecomNumber = splitPhoneNumber(data);
                            if (telecomNumber.get("countryCode") != null) { orderImportCtx.put("countryCode", telecomNumber.get("countryCode")); }
                            if (telecomNumber.get("areaCode") != null) { orderImportCtx.put("areaCode", telecomNumber.get("areaCode")); }
                            orderImportCtx.put("contactNumber", (telecomNumber.get("contactNumber") != null) ? telecomNumber.get("contactNumber").toString() : null);
                        } else if ("手机".equals(header)) {
                            if (telecomNumber.size() < 1) {
                                telecomNumber = splitPhoneNumber(data);
                                if (telecomNumber.get("countryCode") != null) { orderImportCtx.put("countryCode", telecomNumber.get("countryCode")); }
                                if (telecomNumber.get("areaCode") != null) { orderImportCtx.put("areaCode", telecomNumber.get("areaCode")); }
                                orderImportCtx.put("contactNumber", (telecomNumber.get("contactNumber") != null) ? telecomNumber.get("contactNumber").toString() : null);
                            } 
                        } else if ("收货人名称".equals(header)) {
                            orderImportCtx.put("toName", data);
                        } else if ("地址".equals(header)) {   //TODO , split it
                            orderImportCtx.put("address1", data);
                        //} else if ("Buyer Address 2".equals(header)) {
                            //orderImportCtx.put("address2", data);
                        } else if ("城市".equals(header)) {
                            orderImportCtx.put("city", data);
                        } else if ("州/省".equals(header)) {
                            orderImportCtx.put("stateProvinceGeoId", data);
                        } else if ("邮编".equals(header)) {
                            orderImportCtx.put("postalCode", data);
                        } else if ("收货国家".equals(header)) {
                            orderImportCtx.put("countryGeoId", getGeoId(delegator, data, "COUNTRY"));
                        } else if ("产品总金额".equals(header)) {
                            String currencyUom = getCurrencyUom(delegator, data);
                            if(currencyUom == null || "".equals(currencyUom.trim())) {
                                currencyUom = productStore.getString("defaultCurrencyUomId");
                            }
                            orderImportCtx.put("currencyUom", currencyUom);
                            orderImportCtx.put("remainingSubTotal", getPrice(data));
                            //orderItemImport.put("unitPrice", getPrice(data));
                        } else if ("物流费用".equals(header)) {
                            orderImportCtx.put("shippingAmount", getPrice(data));
                        /*} else if ("US Tax".equals(header)) {
                            orderImportCtx.put("taxAmount", getPrice(data));
                        } else if ("Insurance".equals(header)) {
                            orderImportCtx.put("warrantyAmount", getPrice(data));
                        } else if ("Cash on delivery fee".equals(header)) {
                            orderImportCtx.put("taxAdjustmentAmount", getPrice(data));*/
                        } else if ("订单金额".equals(header)) {
                            orderImportCtx.put("grandTotalAmount", getPrice(data));
                            orderImportCtx.put("paymentMethodTypeId", getPaymentMethodTypeId(delegator, "Alipay"));
                        } else if ("下单时间".equals(header)) {
                            orderImportCtx.put("createDate", getTimestamp(data));
                            //orderItemImport.put("saleDate", getTimestamp(data));
                            orderImportCtx.put("checkoutDate", getTimestamp(data));
                        } else if ("付款时间".equals(header)) {
                            orderImportCtx.put("paidOnDate", getTimestamp(data));
                        /*} else if ("发货时间".equals(header)) {
                            //orderImportCtx.put("shippedOnDate", getTimestamp(data.trim()));
                            orderImportCtx.put("shippedOnDate", data);*/
                        } else if ("订单备注".equals(header)) {
                            orderImportCtx.put("noteInfo", data.trim());
                        /*} else if ("PayPal Transaction ID".equals(header)) {
                            orderImportCtx.put("paypalTransactionId", data);*/
                        } else if ("买家选择物流".equals(header)) {
                            String[] shipmeths = data.split("\\n");
                            boolean same = true;
                            String shipCheck = shipmeths[0].trim();
                            for (int k = 0; k < shipmeths.length; k++) {
                                //Debug.logError("shipmeths: " + shipmeths[k].trim(), module);
                                if (!shipmeths[k].trim().equals(shipCheck)) {
                                    same = false;
                                }
                            }
                            if (same) {
                                orderImportCtx.put("productStoreShipMethId", getShipmentMethodId(delegator, shipCheck, productStoreId, (String) orderImportCtx.get("countryGeoId"), (BigDecimal) orderImportCtx.get("grandTotalAmount")));
                            } else {
                                return ServiceUtil.returnError("Shipping method for row " + (row + 1) + " has different shipping method for each SKU");
                            }
                            
                        /*} else if ("Cash on delivery option".equals(header)) {
                            orderImportCtx.put("cashOnDeliveryOption", data);
                        } else if ("Order ID".equals(header)) {
                            orderImportCtx.put("externalOrderId", data);
                        } else if ("Variation Details".equals(header)) {
                            orderImportCtx.put("variationDetails", data);
                        } else if ("Item Number".equals(header)) {
                            orderItemImport.put("orderItemNumber", data);
                        } else if ("Item Title".equals(header)) {
                            orderItemImport.put("productName", data);
                        } else if ("Custom Label".equals(header)) {
                            orderItemImport.put("productId", data);
                        } else if ("Quantity".equals(header)) {
                            orderItemImport.put("quantity", (UtilValidate.isNotEmpty(data)) ? new BigDecimal(data) : BigDecimal.ZERO);
                        } else if ("Transaction ID".equals(header)) {
                            if(data != null && data.contains("+")) {
                                return ServiceUtil.returnError("Please check the column 'Transaction ID' and make sure that its value is pure number without '+'.");
                            }
                            orderItemImport.put("transactionId", data);
                        }   //populating orderImportCtx -- START*/
                        } else if (header.contains("产品信息")) {   //products cell -- START
                            String[] items = data.split("【.*】");
                            for (int i = 1; i < items.length; i++) {    //loop items -- START
                                //Debug.logError(i + " = " + items[i], module);
                                String[] orderItems = items[i].split("\\n");
                                String itemTitle = orderItems[0].trim();
                                int skuSeq = 1;
                                for (int j = 1; j < orderItems.length; j++) {
                                    if (orderItems[j].trim().contains("商家编码")) {
                                        skuSeq = j;
                                    }
                                }
                                String productId = orderItems[skuSeq].trim().substring(orderItems[skuSeq].indexOf(":") + 1, orderItems[skuSeq].indexOf(")"));
                                String quantity = orderItems[skuSeq + 1].trim().substring(orderItems[skuSeq + 1].indexOf(":") + 1, orderItems[skuSeq + 1].indexOf(" piece"));
                                totalQuantity += Integer.parseInt(quantity);
                                
                                orderItemImport.put("orderImportId", newOrderImportId);
                                orderItemImport.put("fileLineNumber", new BigDecimal(row+1));
                                orderItemImport.put("userLogin", userLogin);
                                orderItemImport.put("productId", productId);
                                orderItemImport.put("productName", itemTitle);
                                orderItemImport.put("quantity", (UtilValidate.isNotEmpty(quantity)) ? new BigDecimal(quantity) : BigDecimal.ZERO);
                                //orderItemImport.put("unitPrice", getPrice("1"));
                                orderItemImport.put("saleDate", orderImportCtx.get("createDate"));
                                //Debug.logError("totalQuantity is " + totalQuantity, module);
                                if (UtilValidate.isNotEmpty(orderItemImport.get("productId"))) {    //if orderItemImport.productId exist -- START
                                    Map<String, Object> orderItemResult = dispatcher.runSync("createOrderItemImport", orderItemImport);
                                    if(ServiceUtil.isError(result)) {
                                        errorList.add(ServiceUtil.getErrorMessage(orderItemResult));
                                    }
                                }   //if orderItemImport.productId exist -- END
                            }   //loop items -- END
                        }   //products cell -- END
                        
                    }   //loop columns -- END
                    //Yasin - Fix stateProvinceGeoId - Start - 6 Oct 2012
                    String stateOrProvince = checkStateProvinceGeoId(delegator, (String) orderImportCtx.get("stateProvinceGeoId"), (String) orderImportCtx.get("countryGeoId"));
                    if (UtilValidate.isNotEmpty(stateOrProvince)) {
                        orderImportCtx.put("stateProvinceGeoId", stateOrProvince);
                    } else {
                        orderImportCtx.put("city", (String) orderImportCtx.get("city") + " " + orderImportCtx.get("stateProvinceGeoId"));
                        orderImportCtx.put("stateProvinceGeoId", "_NA_");
                    }
                    //Yasin - Fix stateProvinceGeoId - End
                    
                    //update the product unitPrice -- START
                    //Debug.logError("Final totalQuantity is " + totalQuantity, module);
                    double unitPrice = Double.valueOf(((BigDecimal)orderImportCtx.get("remainingSubTotal")).doubleValue()) / (double) totalQuantity;
                    
                    //Debug.logError("unitPrice is " + unitPrice, module);
                    List<GenericValue> orderItemImports = delegator.findByAnd("OrderItemImport", UtilMisc.toMap("orderImportId", newOrderImportId), null, false);
                    for (GenericValue gv : orderItemImports) { //update unitPrice -- START
                        gv.put("unitPrice", new BigDecimal(unitPrice));
                        delegator.store(gv);
                    }   //update unitPrice -- END
                    //update the product unitPrice -- END
                    
                    /*Iterator<String> keyIt = orderImportCtx.keySet().iterator();
                     while(keyIt.hasNext()) {
                     String key = keyIt.next();
                     String value = orderImportCtx.get(key).toString();
                     Debug.logError("Key is " + key + " and value is " + value, module);
                     }*/
                    try {   //try createOrderImport -- START
                        if (sheet.getRows() > 1 && sheet.getColumns() > 20) {
                            result = dispatcher.runSync("createOrderImport", orderImportCtx);
                            if(ServiceUtil.isError(result)) {
                                errorList.add(ServiceUtil.getErrorMessage(result));
                            }
                        }
                    } catch (Exception e) {
                        return ServiceUtil.returnError(e.getMessage());
                    }   //try createOrderImport -- END
                }   //loop rows -- END
            }   //if sheet is not empty -- END
        } catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        return ServiceUtil.returnSuccess();
    }
    
    private static Map<String, Object> readGetOrdersResponseXml (LocalDispatcher dispatcher, Delegator delegator, GenericValue userLogin, String productStoreId, String filePath)
    throws IOException, GenericEntityException {
        
        Map<String, Object> result = ServiceUtil.returnSuccess();
        List<String> errorList = FastList.newInstance();
        String responseXml = null;
        List<Map<String, Object>> orders = null;
        StringBuffer errorMessage = new StringBuffer();
        String orderNumberProfix = null;
        int fileLineNumber = 0;
        
        try {
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            if (UtilValidate.isNotEmpty(productStore.get("orderNumberPrefix"))) {
                orderNumberProfix = productStore.getString("orderNumberPrefix");
            }
            responseXml = inputStreamToString(new FileInputStream(new File(filePath)));
            //Debug.logError(responseXml, module);
        } catch (Exception e) {
            return ServiceUtil.returnError(e.getMessage());
        }
        
        try {   //main try to read responseXml -- START
            Document docResponse = UtilXml.readXmlDocument(responseXml, true);
            Element elemResponse = docResponse.getDocumentElement();
            String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
            List<? extends Element> paginationList = UtilXml.childElementList(elemResponse, "PaginationResult");
            
            int totalOrders = 0;
            String returnedOrderCountActual = UtilXml.childElementValue(elemResponse, "ReturnedOrderCountActual", "0");
            totalOrders = Integer.valueOf(returnedOrderCountActual);
            
            if (ack != null && "Success".equals(ack)) { //if ack returns success -- START
                orders = FastList.newInstance();
                if (totalOrders > 0) {  //if totalOrders is more than 0 -- START
                    // retrieve OrderArray
                    List<? extends Element> orderArrays = UtilXml.childElementList(elemResponse, "OrderArray");
                    Iterator<? extends Element> orderArraysElemIter = orderArrays.iterator();
                    while (orderArraysElemIter.hasNext()) {    //loop orderArray -- START
                        Element orderArraysElement = orderArraysElemIter.next();
                        
                        // retrieve Order
                        List<? extends Element> orderElementList = UtilXml.childElementList(orderArraysElement, "Order");
                        Iterator<? extends Element> orderElemIter = orderElementList.iterator();
                        while (orderElemIter.hasNext()) { //loop orderElemIter -- START
                            ++fileLineNumber;
                            Element orderElement = orderElemIter.next();
                            String orderStatus = UtilXml.childElementValue(orderElement, "OrderStatus", "");
                            String shippedTime = UtilXml.childElementValue(orderElement, "ShippedTime", "");
                            String ebayPaymentStatus = null;
                            String checkoutStatus = null;
                            String salesRecordNumber = null;
                            
                            // retrieve checkoutStatus
                            List<? extends Element> checkoutStatusList = UtilXml.childElementList(orderElement, "CheckoutStatus");
                            Iterator<? extends Element> checkoutStatusElemIter = checkoutStatusList.iterator();
                            while (checkoutStatusElemIter.hasNext()) {   //loop checkoutStatus -- START
                                Element checkoutStatusElement = checkoutStatusElemIter.next();
                                ebayPaymentStatus = UtilXml.childElementValue(checkoutStatusElement, "eBayPaymentStatus", "");
                                checkoutStatus = UtilXml.childElementValue(checkoutStatusElement, "Status", "");
                            }   //loop checkoutStatus -- END
                            
                            // retrieve shipping details
                            List<? extends Element> shippingDetails = UtilXml.childElementList(orderElement, "ShippingDetails");
                            Iterator<? extends Element> shippingDetailsElemIter = shippingDetails.iterator();
                            while (shippingDetailsElemIter.hasNext()) { //loop shippingDetailsElemIter -- START
                                Element shippingDetailsElement = shippingDetailsElemIter.next();
                                salesRecordNumber = UtilXml.childElementValue(shippingDetailsElement, "SellingManagerSalesRecordNumber", "");
                            }   //loop shippingDetailsElemIter -- END
                            
                            if (orderStatus.equals("Completed") && ebayPaymentStatus.equals("NoPaymentFailure") && checkoutStatus.equals("Complete") ) {//&& UtilValidate.isEmpty(shippedTime)) {  //if orderStatus is completed -- START
                                Map<String, Object> orderImportCtx = FastMap.newInstance();
                                Map<String, Object> orderItemImport = FastMap.newInstance();
                                String newOrderImportId = delegator.getNextSeqId("OrderImport");
                                
                                orderImportCtx.put("orderImportId", newOrderImportId);
                                orderImportCtx.put("productStoreId", productStoreId);
                                orderImportCtx.put("fileName", filePath);
                                orderImportCtx.put("fileLineNumber", new BigDecimal(fileLineNumber));
                                orderImportCtx.put("importedStatus", "N");
                                orderImportCtx.put("userLogin", userLogin);
                                //orderImportCtx.put("userLogin", "userLogin");
                                orderImportCtx.put("stateProvinceGeoId", "_NA_"); // set _NA_ as default state
                                
                                orderImportCtx.put("externalOrderId", UtilXml.childElementValue(orderElement, "OrderID", ""));
                                //orderImportCtx.put("orderStatus", UtilXml.childElementValue(orderElement, "OrderStatus", ""));
                                //orderImportCtx.put("adjustmentAmount", UtilXml.childElementValue(orderElement, "AdjustmentAmount", ""));
                                //orderImportCtx.put("amountPaid", UtilXml.childElementValue(orderElement, "AmountPaid", ""));
                                //orderImportCtx.put("amountSaved", UtilXml.childElementValue(orderElement, "AmountSaved", ""));
                                orderImportCtx.put("orderId", orderNumberProfix + salesRecordNumber);
                                orderImportCtx.put("createDate", toTimestamp(UtilXml.childElementValue(orderElement, "CreatedTime", "").replaceAll("[T,Z]", " ")));
                                orderImportCtx.put("checkoutDate", toTimestamp(UtilXml.childElementValue(orderElement, "CreatedTime", "").replaceAll("[T,Z]", " ")));
                                orderImportCtx.put("paymentMethodTypeId", getPaymentMethodTypeId(delegator, UtilXml.childElementValue(orderElement, "PaymentMethods", "")));
                                orderImportCtx.put("noteInfo", StringEscapeUtils.escapeXml(UtilXml.childElementValue(orderElement, "BuyerCheckoutMessage", null)));
                                
                                
                                // retrieve ShippingAddress
                                List<? extends Element> shippingAddress = UtilXml.childElementList(orderElement, "ShippingAddress");
                                Iterator<? extends Element> shippingAddressElemIter = shippingAddress.iterator();
                                while (shippingAddressElemIter.hasNext()) {    //loop ShippingAddress -- START
                                    Element shippingAddressElement = shippingAddressElemIter.next();
                                    Map<String, Object> nameMap = FastMap.newInstance();
                                    if (UtilValidate.isNotEmpty(UtilXml.childElementValue(shippingAddressElement, "Name", ""))) {
                                        nameMap = splitBuyerFullName(StringEscapeUtils.escapeXml(UtilXml.childElementValue(shippingAddressElement, "Name", "")));
                                    }
                                    orderImportCtx.put("toName", StringEscapeUtils.escapeXml(UtilXml.childElementValue(shippingAddressElement, "Name", "")));
                                    orderImportCtx.put("firstName", (nameMap.get("firstName") != null) ? nameMap.get("firstName").toString() : null);
                                    if (nameMap.get("middleName") != null) {orderImportCtx.put("middleName", nameMap.get("middleName"));}
                                    if (nameMap.get("lastName") != null) {orderImportCtx.put("lastName", nameMap.get("lastName"));}
                                    orderImportCtx.put("address1", StringEscapeUtils.escapeXml(UtilXml.childElementValue(shippingAddressElement, "Street1", "")));
                                    orderImportCtx.put("address2", StringEscapeUtils.escapeXml(UtilXml.childElementValue(shippingAddressElement, "Street2", "").replaceAll("[\n\r]","").trim()));
                                    orderImportCtx.put("city", StringEscapeUtils.escapeXml(UtilXml.childElementValue(shippingAddressElement, "CityName", "")));
                                    orderImportCtx.put("stateProvinceGeoId", StringEscapeUtils.escapeXml(UtilXml.childElementValue(shippingAddressElement, "StateOrProvince", "")));
                                    orderImportCtx.put("countryGeoId", getGeoId(delegator, UtilXml.childElementValue(shippingAddressElement, "CountryName", ""), "COUNTRY"));
                                    //orderImportCtx.put("countryName", UtilXml.childElementValue(shippingAddressElement, "CountryName", ""));
                                    orderImportCtx.put("postalCode", UtilXml.childElementValue(shippingAddressElement, "PostalCode", ""));
                                    Map<String, Object> telecomNumber = splitPhoneNumber(UtilXml.childElementValue(shippingAddressElement, "Phone", ""));
                                    if (telecomNumber.get("countryCode") != null) {orderImportCtx.put("countryCode", telecomNumber.get("countryCode"));}
                                    if (telecomNumber.get("areaCode") != null) {orderImportCtx.put("areaCode", telecomNumber.get("areaCode"));}
                                    orderImportCtx.put("contactNumber", (telecomNumber.get("contactNumber") != null) ? telecomNumber.get("contactNumber").toString() : null);
                                }   //loop ShippingAddress -- END
                                
                                // retrieve shippingServiceSelected
                                List<? extends Element> shippingServiceSelected = UtilXml.childElementList(orderElement, "ShippingServiceSelected");
                                Iterator<? extends Element> shippingServiceSelectedElemIter = shippingServiceSelected.iterator();
                                while (shippingServiceSelectedElemIter.hasNext()) {  //loop shippingServiceSelectedElemiter -- START
                                    Element shippingServiceSelectedElement = shippingServiceSelectedElemIter.next();
                                    orderImportCtx.put("productStoreShipMethId", getShipmentMethodId(delegator, UtilXml.childElementValue(shippingServiceSelectedElement, "ShippingService", ""), productStoreId, (String) orderImportCtx.get("countryGeoId"), (BigDecimal) orderImportCtx.get("grandTotalAmount")));
                                    orderImportCtx.put("shippingAmount", getPrice(UtilXml.childElementValue(shippingServiceSelectedElement, "ShippingServiceCost", "")));
                                }   //loop shippingServiceSelectedElemiter -- END
                                
                                orderImportCtx.put("remainingSubTotal", getPrice(UtilXml.childElementValue(orderElement, "Subtotal", "")));
                                orderImportCtx.put("currencyUom", UtilXml.childElementAttribute(orderElement, "Subtotal", "currencyID", ""));
                                orderImportCtx.put("grandTotalAmount", getPrice(UtilXml.childElementValue(orderElement, "Total", "")));
                                
                                // retrieve ExternalTransaction
                                List<? extends Element> externalTransaction = UtilXml.childElementList(orderElement, "ExternalTransaction");
                                Iterator<? extends Element> externalTransactionElemIter = externalTransaction.iterator();
                                while (externalTransactionElemIter.hasNext()) {  //loop externalTransactionElemiter -- START
                                    Element externalTransactionElement = externalTransactionElemIter.next();
                                    orderImportCtx.put("paypalTransactionId", UtilXml.childElementValue(externalTransactionElement, "ExternalTransactionID", ""));
                                    //orderImportCtx.put("feeOrCreditAmount", UtilXml.childElementValue(externalTransactionElement, "FeeOrCreditAmount", ""));
                                    //orderImportCtx.put("paymentOrRefundAmount", UtilXml.childElementValue(externalTransactionElement, "PaymentOrRefundAmount", ""));
                                }   //loop externalTransactionElemiter -- END
                                
                                orderImportCtx.put("userId", UtilXml.childElementValue(orderElement, "BuyerUserID", ""));
                                orderImportCtx.put("paidOnDate", toTimestamp(UtilXml.childElementValue(orderElement, "PaidTime", "").replaceAll("[T,Z]", " ")));
                                
                                //if (UtilValidate.isNotEmpty(shippedTime)) {orderImportCtx.put("shippedOnDate", toTimestamp(shippedTime.replaceAll("[T,Z]", " ")));}
                                //orderImportCtx.put("EIASToken", UtilXml.childElementValue(orderElement, "EIASToken", ""));
                                //orderImportCtx.put("paymentHoldStatus", UtilXml.childElementValue(orderElement, "PaymentHoldStatus", ""));
                                //orderImportCtx.put("isMultiLegShipping", UtilXml.childElementValue(orderElement, "IsMultiLegShipping", ""));
                                
                                //Yasin - Fix stateProvinceGeoId - Start - 6 Oct 2012
                                String stateOrProvince = checkStateProvinceGeoId(delegator, (String) orderImportCtx.get("stateProvinceGeoId"), (String) orderImportCtx.get("countryGeoId"));
                                if (UtilValidate.isNotEmpty(stateOrProvince)) {
                                    orderImportCtx.put("stateProvinceGeoId", stateOrProvince);
                                } else {
                                    orderImportCtx.put("city", (String) orderImportCtx.get("city") + " " + orderImportCtx.get("stateProvinceGeoId"));
                                    orderImportCtx.put("stateProvinceGeoId", "_NA_");
                                }
                                //Yasin - Fix stateProvinceGeoId - End
                                
                                //looping transactionArray -- START
                                List<? extends Element> transactionArray = UtilXml.childElementList(orderElement, "TransactionArray");
                                Iterator<? extends Element> transactionArrayElemIter = transactionArray.iterator();
                                while (transactionArrayElemIter.hasNext()) {  //loop transactionArrayElemIter -- START
                                    Element transactionArrayElement = transactionArrayElemIter.next();
                                    //loop transaction -- START
                                    List<? extends Element> transaction = UtilXml.childElementList(transactionArrayElement, "Transaction");
                                    Iterator<? extends Element> transactionElemIter = transaction.iterator();
                                    while (transactionElemIter.hasNext()) {  //loop transactionElemIter -- START
                                        Element transactionElement = transactionElemIter.next();
                                        orderItemImport.put("userLogin", userLogin);
                                        orderItemImport.put("orderImportId", newOrderImportId);
                                        orderItemImport.put("fileLineNumber", new BigDecimal(fileLineNumber));
                                        //retrieve buyer
                                        List<? extends Element> buyer = UtilXml.childElementList(transactionElement, "Buyer");
                                        Iterator<? extends Element> buyerElemIter = buyer.iterator();
                                        while (buyerElemIter.hasNext()) {   //loop buyerElemIter -- START
                                            Element buyerElement = buyerElemIter.next();
                                            if (UtilXml.childElementValue(buyerElement, "Email", "").contains("Invalid")) {
                                                orderImportCtx.put("emailAddress", "email@email.com");
                                            }
                                            else {
                                               orderImportCtx.put("emailAddress", UtilXml.childElementValue(buyerElement, "Email", ""));
                                            }
                                            
                                        }   //loop buyerElemIter -- END
                                        orderItemImport.put("saleDate", toTimestamp(UtilXml.childElementValue(transactionElement, "CreatedDate", "").replaceAll("[T,Z]", " ")));
                                        
                                        //retrieve Item
                                        List<? extends Element> item = UtilXml.childElementList(transactionElement, "Item");
                                        Iterator<? extends Element> itemElemIter = item.iterator();
                                        while (itemElemIter.hasNext()) {   //loop itemElemIter -- START
                                            Element itemElement = itemElemIter.next();
                                            orderItemImport.put("orderItemNumber", UtilXml.childElementValue(itemElement, "ItemID", ""));
                                            orderItemImport.put("productName", UtilXml.childElementValue(itemElement, "Title", ""));
                                            orderItemImport.put("productId", UtilXml.childElementValue(itemElement, "SKU", ""));
                                        }   //loop itemElemIter -- END
                                        
                                        orderItemImport.put("quantity", (UtilValidate.isNotEmpty(UtilXml.childElementValue(transactionElement, "QuantityPurchased", "0"))) ? new BigDecimal(UtilXml.childElementValue(transactionElement, "QuantityPurchased", "0")) : BigDecimal.ZERO);
                                        orderItemImport.put("transactionId", UtilXml.childElementValue(transactionElement, "TransactionID", ""));
                                        orderItemImport.put("unitPrice", getPrice(UtilXml.childElementValue(transactionElement, "TransactionPrice", "")));
                                        //orderItemImport.put("actualShippingCost", UtilXml.childElementValue(transactionElement, "ActualShippingCost", ""));
                                        //orderItemImport.put("orderLineItemID", UtilXml.childElementValue(transactionElement, "OrderLineItemID", ""));
                                        
                                        //retrieve Variation
                                        List<? extends Element> variation = UtilXml.childElementList(transactionElement, "Variation");
                                        if (UtilValidate.isNotEmpty(variation)) {   //if variation is not empty -- START
                                            Iterator<? extends Element> variationElemIter = variation.iterator();
                                            while (variationElemIter.hasNext()) {   //loop variationElemIter -- START
                                                Element variationElement = variationElemIter.next();
                                                orderItemImport.put("productId", UtilXml.childElementValue(variationElement, "SKU", ""));
                                                orderItemImport.put("productName", UtilXml.childElementValue(variationElement, "VariationTitle", ""));
                                            }   //loop variationElemIter -- END
                                        }   //if variation is not empty -- END
                                        
                                        //execute createOrderItemImport -- START
                                        if (UtilValidate.isNotEmpty(orderItemImport.get("productId"))) {    //if orderItemImport.productId exist -- START
                                            //Debug.logError("this runs the createOrderItemImport for\r\n" + orderItemImport, module);
                                            Map<String, Object> orderItemResult = dispatcher.runSync("createOrderItemImport", orderItemImport);
                                            if(ServiceUtil.isError(result)) {
                                                errorList.add(ServiceUtil.getErrorMessage(orderItemResult));
                                            }
                                        }   //if orderItemImport.productId exist -- END
                                        //execute createOrderItemImport -- END
                                        
                                        /*Iterator<String> keyIt = orderItemImport.keySet().iterator();
                                        while(keyIt.hasNext()) {
                                            String key = keyIt.next();
                                            String value = orderItemImport.get(key).toString();
                                            Debug.logError("Key is " + key + " = " + value, module);
                                        }*/
                                        
                                    }   //loop transactionElemIter -- END
                                    //loop transaction -- END
                                }   //loop transactionArrayElemIter -- END
                                //looping transactionArray -- END
                                
                                /*Iterator<String> keyIt = orderImportCtx.keySet().iterator();
                                while(keyIt.hasNext()) {
                                    String key = keyIt.next();
                                    String value = orderImportCtx.get(key).toString();
                                    Debug.logError("Key is " + key + " = " + value, module);
                                }*/
                                
                                //execute createOrderImport -- START
                                try {   //try createOrderImport -- START
                                    //Debug.logError("this runs createOrderImport for " + orderImportCtx, module);
                                    result = dispatcher.runSync("createOrderImport", orderImportCtx);
                                    if(ServiceUtil.isError(result)) {
                                        errorList.add(ServiceUtil.getErrorMessage(result));
                                    }
                                } catch (Exception e) {
                                    return ServiceUtil.returnError(e.getMessage());
                                }   //try createOrderImport -- END
                                //execute createOrderImport -- END
                                
                            }   //if orderStatus is completed -- END
                            /*else {
                                Debug.logError("ProductStoreId: " + productStoreId + ", SalesRecordNumber: " + salesRecordNumber + " has orderStatus: " + orderStatus + ", ebayPaymentStatus: " + ebayPaymentStatus + ", checkoutStatus: " + checkoutStatus + ". Not running importation!", module);
                            }*/
                        }   ////loop orderElemIter -- START -- END
                    }   //loop orderArray -- END
                }   //if totalOrders is more than 0 -- END
            } //if ack returns success -- END
            else {  //if ack returns other than success -- START
                List<? extends Element> errorListXml = UtilXml.childElementList(elemResponse, "Errors");
                Iterator<? extends Element> errorElemIter = errorListXml.iterator();
                while (errorElemIter.hasNext()) {   //loop errorElemIter -- START
                    Element errorElement = errorElemIter.next();
                    Debug.logError("Ack: " + ack + " with message: " + UtilXml.childElementValue(errorElement, "ShortMessage", ""), module);
                    errorMessage.append(UtilXml.childElementValue(errorElement, "ShortMessage", ""));
                }   //loop errorElemIter -- START
            }   //if ack returns other than success -- END
        } //main try to read responseXml -- END
        catch (Exception e) {
            Debug.logError("Exception during read response from Ebay", module);
            return ServiceUtil.returnError("Exception during read response from Ebay: " + e.getMessage());
        }
        return ServiceUtil.returnSuccess();
    }
    
    public static Map<String, Object> importOrderFromEbayApi(DispatchContext dctx, Map context) {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        int lastXDays = (Integer) context.get("lastXDays");
        if (UtilValidate.isEmpty(lastXDays)) {
            lastXDays = 2;
        }
        Map mapAccount = FastMap.newInstance();
        int pageNumber = 1;
        int totalNumberOfPages = 0;
        String ack = null;
        boolean hasMoreOrders = true;
        String fileName = null;
        
        try {
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
            String productStoreGroup = productStore.getString("primaryStoreGroupId");
            if(!productStoreGroup.equals("EBAY")) {
                Debug.logError("ProductStoreId " + productStoreId + " is not part of eBay productStoreGroup", module);
                return ServiceUtil.returnError("ProductStoreId " + productStoreId + " is not part of eBay productStoreGroup");
            }
            mapAccount = common.accountInfo(delegator, productStore);
            mapAccount.put("callName", "GetOrders");
            mapAccount.put("pageNumber", pageNumber);
            mapAccount.put("lastXDays", lastXDays);
        } catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
        while (hasMoreOrders) { //loop while HasMoreOrders is true -- START
            String requestXMLcode = null;
            //Debug.logError(requestXMLcode, module);
            String responseXML = null;
            try {
                requestXMLcode = eBayTradingAPI.getOrdersRequestXML(mapAccount);
                responseXML = eBayTradingAPI.sendRequestXMLToEbay(mapAccount, requestXMLcode);
                Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                Element elemResponse = docResponse.getDocumentElement();
                ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                hasMoreOrders = Boolean.parseBoolean(UtilXml.childElementValue(elemResponse, "HasMoreOrders", "false"));
            }
            catch (Exception e) {
                Debug.logError("Exception during read response-ack from Ebay", module);
                return ServiceUtil.returnError("Exception during read response-ack from Ebay: " + e.getMessage());
            }
            
            if (UtilValidate.isNotEmpty(responseXML) && ack.equals("Success")) {    //if ack is success -- START
                try {   //try saving file and run service -- START
                    String fileServerPath = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("orderImport", "orderimport.server.path"), context);
                    String fileServerPath2 = FlexibleStringExpander.expandString(UtilProperties.getPropertyValue("orderImport", "orderimportXML.server.path"), context);
                    File rootTargetDir = new File(fileServerPath);
                    if (!rootTargetDir.exists()) {
                        boolean created = rootTargetDir.mkdirs();
                        if (!created) {
                            String errMsg = "Not create target directory";
                            Debug.logFatal(errMsg, module);
                            return ServiceUtil.returnError(errMsg);
                        }
                    }
                    fileName = productStoreId + "_" + UtilDateTime.nowDateString("yyyyMMddHH") + ".xml";
                    String filePath = fileServerPath + "/" + fileName;
                    File file = new File(filePath);
                    File file2 = new File(fileServerPath2 + "/" + UtilDateTime.nowDateString("yyyyMMddHH") + "PG-" + pageNumber + "_(" + UtilDateTime.nowDateString() + ").xml");
                    //Debug.logError(fileServerPath2 + "/PG-" + pageNumber + "_" + UtilDateTime.nowDateString() + ".xml", module);
                    /*if(file2.exists()) throw new IOException("file exists");

                    if(file.exists() && file.isFile()){
                        FileUtils.copyFile(file, file2);
                        //file.delete();
                    }
                    else {
                        FileWriter f2 = new FileWriter(fileServerPath2 + "/" + UtilDateTime.nowDateString("yyyyMMddHH") + "PG-" + pageNumber + "_(" + UtilDateTime.nowDateString() + ").xml", false);
                        f2.write(responseXML);
                        f2.close();
                    }*/
                    try {   //try write file -- START
                        FileWriter f1 = new FileWriter(filePath, false);
                        f1.write(responseXML);
                        f1.close();
                    } catch (FileNotFoundException e) {
                        Debug.logError(e, module);
                        return ServiceUtil.returnError("");
                    } catch (IOException e) {
                        Debug.logError(e, module);
                        return ServiceUtil.returnError("");
                    }   //try write file -- END
                    if (file.exists()) {    //if file exists -- START
                        Map result = null;
                        result = readGetOrdersResponseXml(dispatcher, delegator, userLogin, productStoreId, filePath);
                        FileUtils.copyFile(file, file2);
                        if(ServiceUtil.isError(result)) {
                            return result;
                        }
                    }   //if file exists -- END
                }   //try saving file and run service -- END
                catch (Exception e) {
                    return ServiceUtil.returnError(e.getMessage());
                }
            }   //if ack is success -- END
            else {  //if ack is not success -- START
                try {   //try write file -- START
                    //Write responseXML to directories -- START
                    File f = new File ("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/GetOrders/" + fileName);
                    if(f.exists() && f.isFile()){
                        f.delete();
                    }
                    FileWriter f2 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/GetOrders/" + fileName, false);
                    f2.write(responseXML);
                    f2.close();
                    //Write responseXML to directories -- END
                } catch (FileNotFoundException e) {
                    Debug.logError(e, module);
                } catch (IOException e) {
                    Debug.logError(e, module);
                }   //try write file -- END
                
            }   //if ack is not success -- END
            mapAccount.put("pageNumber", ++pageNumber);
        }   //loop while HasMoreOrders is true -- END
        
        return ServiceUtil.returnSuccess();
    }
    
    public static Map<String, Object> autoImportOrderFromEbayApi(DispatchContext dctx, Map context)
    throws Exception {
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        try {
            List<GenericValue> productStores = delegator.findByAnd("ProductStore", UtilMisc.toMap("primaryStoreGroupId", "EBAY"), null, false);
            for (GenericValue productStore : productStores) {   //loop productStores -- START
                String productStoreId = productStore.getString("productStoreId");
                GenericValue productEbayStoreSetting = delegator.findOne("ProductStoreEbaySetting", UtilMisc.toMap("productStoreId", productStoreId), false);
                if (productEbayStoreSetting.getString("autoImportEbayApi").equals("Y")) {   //if autoImport is Y -- START
                    Map result = dispatcher.runSync("importOrderFromEbayApi", UtilMisc.toMap("productStoreId", productStoreId, "userLogin", userLogin));
                }   //if autoImport is Y -- END
            }   //loop productStores -- END
        }
        catch (Exception e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return ServiceUtil.returnSuccess();
    }
    
    public static Map<String, Object> setEbayStatusShipped(DispatchContext dctx, Map context) throws ApiException, SdkException, Exception {
        //String apiToken, String ebayItemId, String transactionId, String trackingCode, String carrierCode
        //single item: itemId + transactionId
        //multiple item: external Order ID
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        //GenericValue userLogin = (GenericValue) context.get("userLogin");
        String orderId = (String) context.get("orderId");
        
        GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
        
        if (orderHeader.getString("salesChannelEnumId").equals("EBAY_SALES_CHANNEL")) { //if ebay sales order -- START
            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", orderHeader.getString("productStoreId")), false);
            GenericValue ebayConfig = delegator.findOne("EbayConfig", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId")), false);
            
            String apiToken = ebayConfig.getString("token");
            String apiServerUrl = ebayConfig.getString("apiServerUrl");
            String globalId = ebayConfig.getString("globalId");
            String externalOrderId = orderHeader.getString("externalId");
            
            
            /*ApiContext apiContext = new ApiContext();
             
             ApiCredential cred = apiContext.getApiCredential();
             cred.seteBayToken(apiToken);
             
             apiContext.setApiServerUrl(apiServerUrl);
             apiContext.setSite(SiteCodeType.US); //may need changes
             
             CompleteSaleCall completeSaleApi = new CompleteSaleCall(apiContext);
             
             if (externalOrderId == null) {
             String itemId = delegator.findOne("OrderItemAttribute", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", "00001", "attrName", "eBay Item Number"), false).getString("attrValue");
             String transactionId = delegator.findOne("OrderItemAttribute", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", "00001", "attrName", "EBAY_TRAN_ID"), false).getString("attrValue");
             externalOrderId = itemId + "-" + transactionId;
             Debug.logError("externalOrderId: " + externalOrderId, module);
             completeSaleApi.setOrderLineItemID(externalOrderId);
             }
             else {
             completeSaleApi.setOrderID(externalOrderId);
             }
             
             completeSaleApi.setShipped(true);
             completeSaleApi.completeSale();*/
        }   //if ebay sales order -- END
        
        
        return ServiceUtil.returnSuccess();
        
    }
    
    private static String inputStreamToString(InputStream inputStream) throws IOException
    {
        String string;
        StringBuilder outputBuilder = new StringBuilder();
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while (null != (string = reader.readLine())) {
                outputBuilder.append(string).append('\n');
            }
        }
        return outputBuilder.toString();
    }
    
    public static Map<String, Object> deleteFileOrderImportBulk(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        try {
            DynamicViewEntity importDynamicView = new DynamicViewEntity();
            // Construct a dynamic view entity
            importDynamicView.addMemberEntity("OI", "OrderImport");
            importDynamicView.addAlias("OI", "fileName");
            EntityCondition condition2 = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                       //EntityCondition.makeCondition("quantityNotAvailable",EntityOperator.GREATER_THAN ,zero),
                                                                                       EntityCondition.makeCondition("fileName",EntityOperator.NOT_EQUAL ,null)
                                                                                       )
                                                                       );
            //get Distinct EntityLustIterator
            EntityListIterator orderImports = delegator.findListIteratorByCondition(importDynamicView,
                                                                                      condition2,
                                                                                      null,
                                                                                      UtilMisc.toList("fileName"),
                                                                                      UtilMisc.toList("fileName"),
                                                                                      new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true));
            
            GenericValue orderImport = null;
            while ((orderImport = orderImports.next()) != null) {   //loop orderImports -- START
                String fileName = orderImport.getString("fileName");
                GenericValue orderImportFull = EntityUtil.getFirst(delegator.findByAnd("OrderImport", UtilMisc.toMap("fileName", fileName), null, false));
                String productStoreId = orderImportFull.getString("productStoreId");
                Debug.logError("Deleting " + productStoreId + ": " + fileName, module);
                Map tempResult = dispatcher.runSync("deleteFileOrderImport", UtilMisc.toMap("productStoreId", productStoreId, "fileName", fileName, "userLogin", userLogin));
                if (ServiceUtil.isError(tempResult)) {
                    return ServiceUtil.returnError("Failed delete fileName " + fileName);
                }
            }   //loop orderImports -- END
            
            orderImports.close();
        } catch (Exception e) {
            result = ServiceUtil.returnError(e.getMessage());
            result.put("productStoreId", (String) context.get("productStoreId"));
            return result;
        }
        result = ServiceUtil.returnSuccess("Delete OrderImport and file successful.");
        result.put("productStoreId", (String)context.get("productStoreId"));
        return result;
        
    }
    
    public static String convertMessage (LinkedList list) {
        
        String result = null;
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            if (result == null) {
                result = (String) iterator.next();
            } else {
                result += ", " + iterator.next();
            }
        }
        return result;
    }

}

