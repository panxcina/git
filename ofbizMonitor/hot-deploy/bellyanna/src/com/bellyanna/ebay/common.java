package com.bellyanna.ebay;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.Map;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.base.util.UtilXml;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.apache.commons.lang.StringEscapeUtils;

import com.bellyanna.ebay.eBayTradingAPI;
import com.bellyanna.ebay.common;

public class common {
	private static final String module = common.class.getName();
	
	public static Map<String, Object> accountInfo (Delegator delegator, GenericValue productStore)
    throws GenericEntityException {
        
        Map mapAccount = FastMap.newInstance();
        
        try {   //main TRY -- START
            String productStoreId = productStore.getString("productStoreId");
            GenericValue ebayConfig = delegator.findOne("EbayConfig", UtilMisc.toMap("productStoreId", productStoreId), false);
            
            //Get productStore eBay Config
            mapAccount.put("devId", ebayConfig.getString("devId"));
            mapAccount.put("appId", ebayConfig.getString("appId"));
            mapAccount.put("certId", ebayConfig.getString("certId"));
            mapAccount.put("token", ebayConfig.getString("token"));
            mapAccount.put("compatibilityLevel", ebayConfig.getString("compatibilityLevel"));
            mapAccount.put("siteId", ebayConfig.getString("siteId"));
            mapAccount.put("globalId", ebayConfig.getString("globalId"));
            
            //Get eBay Account information
            List<GenericValue> productStoreRole = delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStoreId, "roleTypeId", "EBAY_ACCOUNT"), null, false);
            if(UtilValidate.isNotEmpty(productStoreRole)) { //if productStoreRole is not empty -- START
                GenericValue ebayAccountPartyGroup = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", EntityUtil.getFirst(productStoreRole).getString("partyId")),false);
                mapAccount.put("ebaySellerId", ebayAccountPartyGroup.getString("groupName"));
            }   //if productStoreRole is not empty -- END
            else {
                Debug.logError("empty ProductStoreRole", module);
                return ServiceUtil.returnError("No ProductStoreRole found");
            }
            
        }   //main TRY -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        return mapAccount;
	}   //accountInfo
    
    public static String inputStreamToString(InputStream inputStream) throws IOException
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
    }   //inputStreamToString
    
    public static Map<String, Object> discontinueAdjustEbayQuantity (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String fileName = (String) context.get("fileName");
        String productUpdateId = (String) context.get("productUpdateId");
        Map result = ServiceUtil.returnSuccess();
        List<String> itemIdList = new ArrayList<String>();
        List<String> productIdList = new ArrayList<String>();
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/discontinueAdjustEbayQuantity.log", true);
        
        //getting ItemIdList and productIdList -- START
        try {   //main try -- START
            if (fileName != null && productUpdateId == null) { //filename is not null -- START
                List<GenericValue> bulkProductUpdates = delegator.findByAnd("BulkProductUpdate", UtilMisc.toMap("fileName", fileName, "setDiscontinue", "Y", "updatedStatus", "Y"),null, false);
                for (GenericValue bulkProductUpdate : bulkProductUpdates) { //loop bulkProductUpdate -- START
                    String productId =  bulkProductUpdate.getString("productId");
                    productIdList.add(productId);
                    List<GenericValue> ebayActiveListings = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("sku", productId), null, false);
                    List<GenericValue> ebayActiveListingVariations = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("productId", productId), null, false);
                    if (UtilValidate.isNotEmpty(ebayActiveListings)) {  //if ebayActiveListing is not empty -- START
                        for (GenericValue ebayActiveListing : ebayActiveListings) { //loop ebayActiveListings -- START
                            //Debug.logError("Discontinue: Item ID: " + ebayActiveListing.getString("itemId"), module);
                            itemIdList.add(ebayActiveListing.getString("itemId"));
                        }   //loop ebayActiveListings -- END
                    }   //if ebayActiveListing is not empty -- END
                    
                    if (UtilValidate.isNotEmpty(ebayActiveListingVariations)) {  //if ebayActiveListing is not empty -- START
                        for (GenericValue ebayActiveListingVariation : ebayActiveListingVariations) { //loop ebayActiveListingVariations -- START
                            //Debug.logError("Discontinue: Item ID: " + ebayActiveListingVariation.getString("itemId"), module);
                            itemIdList.add(ebayActiveListingVariation.getString("itemId"));
                        }   //loop ebayActiveListingVariations -- END
                    }   //if ebayActiveListingVariations is not empty -- END

                }   //loop bulkProductUpdate -- END
            }   //filename is not null -- END
            else if (fileName == null && productUpdateId != null) { //if productUpdateId is not null -- START
                GenericValue bulkProductUpdate = delegator.findOne("BulkProductUpdate", UtilMisc.toMap("productUpdateId", productUpdateId), false);
                String productId =  bulkProductUpdate.getString("productId");
                productIdList.add(productId);
                List<GenericValue> ebayActiveListings = delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("sku", productId), null, false);
                List<GenericValue> ebayActiveListingVariations = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("productId", productId), null, false);
                if (UtilValidate.isNotEmpty(ebayActiveListings)) {  //if ebayActiveListing is not empty -- START
                    for (GenericValue ebayActiveListing : ebayActiveListings) { //loop ebayActiveListings -- START
                        //Debug.logError("Discontinue: Item ID: " + ebayActiveListing.getString("itemId"), module);
                        itemIdList.add(ebayActiveListing.getString("itemId"));
                    }   //loop ebayActiveListings -- END
                }   //if ebayActiveListing is not empty -- END
                
                if (UtilValidate.isNotEmpty(ebayActiveListingVariations)) {  //if ebayActiveListing is not empty -- START
                    for (GenericValue ebayActiveListingVariation : ebayActiveListingVariations) { //loop ebayActiveListingVariations -- START
                        //Debug.logError("Discontinue: Item ID: " + ebayActiveListingVariation.getString("itemId"), module);
                        itemIdList.add(ebayActiveListingVariation.getString("itemId"));
                    }   //loop ebayActiveListingVariations -- END
                }   //if ebayActiveListingVariations is not empty -- END
            }   //if productUpdateId is not null -- END
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        //getting ItemIdList and productIdList -- END
        
        Set itemIdListSet = new LinkedHashSet(itemIdList);
        itemIdList.clear();
        itemIdList.addAll(itemIdListSet);
        
        for (String test : itemIdList) {
            Debug.logError("itemId is: " + test, module);
        }
        
        Set productIdListSet = new LinkedHashSet(productIdList);
        productIdList.clear();
        productIdList.addAll(productIdListSet);
        
        for (String test : productIdList) {
            Debug.logError("productId is: " + test, module);
        }
        
        try {   //try AdjustEbayQuantity -- START
            for (String itemId : itemIdList) {  //loop itemIdList -- START
                boolean sendRequest = false;
                boolean hasVariation = false;
                String requestXMLcode = null;
                GenericValue discontinueListing = EntityUtil.getFirst(delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false));
                List<GenericValue> discontinueListingVariationList = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("itemId", itemId), null, false);
                if (UtilValidate.isNotEmpty(discontinueListingVariationList)) {
                    hasVariation = true;
                }
                String productStoreId = discontinueListing.getString("productStoreId");
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                String listingType = discontinueListing.getString("listingType");
                if (listingType.equals("FixedPriceItem")) { //listingType fixedPriceItem -- START
                    Map mapAccount = common.accountInfo(delegator, productStore);
                    mapAccount.put("callName", "ReviseFixedPriceItem");
                    
                    //Building XML -- START
                    Document rootDoc = UtilXml.makeEmptyXmlDocument("ReviseFixedPriceItemRequest");
                    Element rootElem = rootDoc.getDocumentElement();
                    rootElem.setAttribute("xmlns", "urn:ebay:apis:eBLBaseComponents");
                    
                    //RequesterCredentials -- START
                    Element requesterCredentialsElem = UtilXml.addChildElement(rootElem, "RequesterCredentials", rootDoc);
                    UtilXml.addChildElementValue(requesterCredentialsElem, "eBayAuthToken", mapAccount.get("token").toString(), rootDoc);
                    //RequesterCredentials -- END
                    
                    //Item -- START
                    Element itemElem = UtilXml.addChildElement(rootElem, "Item", rootDoc);
                    UtilXml.addChildElementValue(itemElem, "ItemID", itemId, rootDoc);
                    
                    if (hasVariation) { //hasVariation true -- START
                        Element variationsElem = UtilXml.addChildElement(itemElem, "Variations", rootDoc);
                        for (GenericValue discontinueListingVariation : discontinueListingVariationList) {  //loop discontinueListingVariationList -- START
                            String variationSeqId = discontinueListingVariation.getString("variationSeqId");
                            String variationProductId = discontinueListingVariation.getString("productId");
                            String startPrice = discontinueListingVariation.getBigDecimal("startPrice").toString();
                            if (productIdList.contains(variationProductId)) { //if variationProductId is in productIdList -- START
                                Element variationElem = UtilXml.addChildElement(variationsElem, "Variation", rootDoc);
                                UtilXml.addChildElementValue(variationElem, "Delete", "false", rootDoc);
                                UtilXml.addChildElementValue(variationElem, "Quantity", "0", rootDoc);
                                UtilXml.addChildElementValue(variationElem, "SKU", variationProductId, rootDoc);
                                UtilXml.addChildElementValue(variationElem, "StartPrice", startPrice, rootDoc);
                                Element variationSpecificsElem = UtilXml.addChildElement(variationElem, "VariationSpecifics", rootDoc);
                                
                                List<GenericValue> variationSpecifics = delegator.findByAnd("ListingVariationSpecifics", UtilMisc.toMap("productStoreId", productStoreId, "itemId", itemId, "variationSeqId", variationSeqId), null, false);
                                if (UtilValidate.isNotEmpty(variationSpecifics)) {  //if variationSpecifics is not null -- START
                                    for (GenericValue variationSpecific : variationSpecifics) { //loop variationSpecifics -- START
                                        String varSpecsName = variationSpecific.getString("varSpecsName");
                                        String varSpecsValue = variationSpecific.getString("varSpecsValue");
                                        Element nameValueListElem = UtilXml.addChildElement(variationSpecificsElem, "NameValueList", rootDoc);
                                        UtilXml.addChildElementValue(nameValueListElem, "Name", varSpecsName, rootDoc);
                                        UtilXml.addChildElementValue(nameValueListElem, "Value", varSpecsValue, rootDoc);
                                        
                                        sendRequest = true;
                                    }   //loop variationSpecifics -- END
                                }   //if variationSpecifics is not null -- END
                            }   //if variationProductId is in productIdList -- END
                        }   //loop discontinueListingVariationList -- END
                    }   //hasVariation true -- END
                    else {  //hasVariation false -- START
                        String sku = discontinueListing.getString("sku");
                        if (productIdList.contains(sku)) { //if sku is in productIdList -- START
                            UtilXml.addChildElementValue(itemElem, "SKU", sku, rootDoc);
                            UtilXml.addChildElementValue(itemElem, "Quantity", "0", rootDoc);
                            sendRequest = true;
                        }   //if sku is in productIdList -- END
                    }   //hasVariation false -- END
                    requestXMLcode = UtilXml.writeXmlDocument(rootDoc);
                    
                    if (sendRequest) {  //sendRequest to eBay -- START
                        //Debug.logError(requestXML, module);
                        String responseXML = eBayTradingAPI.sendRequestXMLToEbay(mapAccount, requestXMLcode);
                        
                        Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                        Element elemResponse = docResponse.getDocumentElement();
                        String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                        
                        if (!(ack.equals("Success") || ack.equals("Warning"))) {    //if responseXML returns error -- START
                            List<? extends Element> errorList = UtilXml.childElementList(elemResponse, "Errors");
                            Iterator<? extends Element> errorElemIter = errorList.iterator();
                            StringBuffer errorMessage = new StringBuffer();
                            while (errorElemIter.hasNext()) {   //loop error Iterator -- START
                                Element errorElement = errorElemIter.next();
                                errorMessage.append(UtilXml.childElementValue(errorElement, "ShortMessage", ""));
                            }   //loop error Iterator -- START
                            
                            //Write responseXML to directories -- START
                            f1.write(today + ": productStoreId " + productStoreId + ", failed to end eBay Active Listing for itemId " + itemId + ": " + errorMessage + "\n");
                            FileWriter errorResponseXML = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogXML/ebay/responseXML/discontinueAdjustEbayQuantity.xml", true);
                            errorResponseXML.write(responseXML);
                            errorResponseXML.close();
                            //Write responseXML to directories -- END
                        }   //if responseXML returns error -- END
                    }   //sendRequest to eBay -- END
                }   //listingType fixedPriceItem -- END
                else if (listingType.equals("Chinese")) {    //listingType Auction -- START
                    Debug.logError("ItemId: " + itemId + ", SKU: " + discontinueListing.getString("sku") + ", ListingType: " + listingType + ", and hasVariation is " + hasVariation, module);
                    
                    Map endEbayListing = dispatcher.runSync(
                                                            "TradingApiEndEbayActiveListingSingle",
                                                            UtilMisc.toMap(
                                                                           "itemId", itemId,
                                                                           "userLogin", userLogin
                                                                           )
                                                            );
                    
                    if (ServiceUtil.isError(endEbayListing)) {
                        f1.write(today + ": productStoreId " + productStoreId + ", failed to end eBay Active Listing for itemId " + itemId + "\n");
                    }
                }   //listingType Auction -- END
            }   //loop itemIdList -- END

        }   //try AdjustEbayQuantity -- END
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
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        f1.close();
        return result;
        
    }   //discontinueAdjustEbayQuantity
    
    public static Map<String, Object> checkDiscontinuedListingOnEbay (DispatchContext dctx, Map context)
    throws GenericEntityException, GenericServiceException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        Map result = ServiceUtil.returnSuccess();
        
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        Timestamp todayTS = Timestamp.valueOf(sdf.format(now.getTime()));
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/checkDiscontinuedListing.log", true);
        
        try {   //main try -- START
            
            List<String> productStoreIdList = new ArrayList<String>();
            if (UtilValidate.isNotEmpty(productStoreId)) {  //if productStoreId is not empty -- START
                productStoreIdList.add(productStoreId);
            }   //if productStoreId is not empty -- END
            else {  //if productStoreId is empty -- START
                List<GenericValue> productStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("primaryStoreGroupId", "EBAY"), null, false);
                for (GenericValue productStore : productStoreList) {    //loop productStoreList -- START
                    String productStoreIdEbay = productStore.getString("productStoreId");
                    productStoreIdList.add(productStoreIdEbay);
                }   //loop productStoreList -- END
            }   //if productStoreId is empty -- END
            
            for (String activeListingProductStoreId : productStoreIdList) { //loop productStoreId -- START
                int count = 0;
                EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(EntityCondition.makeCondition("productStoreId",EntityOperator.EQUALS , activeListingProductStoreId)));
                
                List<String> endListingList = new ArrayList<String>();
                List<GenericValue> ebayActiveListingList = delegator.findList("EbayActiveListing", condition, null, null, null, false);
                for (GenericValue ebayActiveListing : ebayActiveListingList) {  //loop ebayActiveListingList -- START
                    boolean endListing = true;
                    String itemId = ebayActiveListing.getString("itemId");
                    
                    List<GenericValue> ebayActiveListingVariationList = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("itemId", itemId), null, false);
                    if (UtilValidate.isNotEmpty(ebayActiveListingVariationList)) {  //if hasVariation is true -- START
                        for (GenericValue ebayActiveListingVariation : ebayActiveListingVariationList) {    //loop ebayActiveListingVariationList -- START
                            String productId = ebayActiveListingVariation.getString("productId");
                            GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                            if (UtilValidate.isNotEmpty(product)) { //if product exist in database -- START
                                Timestamp salesDiscontinuationDate = product.getTimestamp("salesDiscontinuationDate");
                                if (UtilValidate.isNotEmpty(salesDiscontinuationDate)) { //if salesDiscontinuationDate is not empty -- START
                                    if (todayTS.before(salesDiscontinuationDate)) { //if discontinue date is before today -- START
                                        endListing = false;
                                    }   //if discontinue date is before today -- END
                                    
                                    Map checkInv = dispatcher.runSync("getProductInventoryAvailable", UtilMisc.toMap("productId", productId));
                                    BigDecimal atp = (BigDecimal) checkInv.get("availableToPromiseTotal");
                                    if (Integer.valueOf(atp.intValue()) > 0) {
                                        endListing = false;
                                    }
                                    //Debug.logError("productId " + productId + ": QOH " + checkInv.get("quantityOnHandTotal") + ", " + checkInv.get("availableToPromiseTotal") + "  ATP", module);
                                }  //if salesDiscontinuationDate is not empty -- END
                                else {  //if salesDiscontinuationDate is empty -- START
                                    endListing = false;
                                }   //if salesDiscontinuationDate is empty -- END
                            }   //if product exist in database -- END
                            else {  //if product does not exist in database -- START
                                endListing = false;
                                f1.write(today + ": productStoreId " + activeListingProductStoreId + ", itemId " + itemId + ": " + productId + " does not exist in database.\n");
                            }   //if product does not exist in database -- END
                        }   //loop ebayActiveListingVariationList -- END
                    }   //if hasVariation is true -- END
                    else {  //if hasVariation is false -- START
                        String productId = ebayActiveListing.getString("sku");
                        GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                        if (UtilValidate.isNotEmpty(product)) { //if product exist in database -- START
                            Timestamp salesDiscontinuationDate = product.getTimestamp("salesDiscontinuationDate");
                            if (UtilValidate.isNotEmpty(salesDiscontinuationDate)) { //check discontinued product -- START
                                if (todayTS.before(salesDiscontinuationDate)) {
                                    endListing = false;
                                }
                                Map checkInv = dispatcher.runSync("getProductInventoryAvailable", UtilMisc.toMap("productId", productId));
                                BigDecimal atp = (BigDecimal) checkInv.get("availableToPromiseTotal");
                                if (Integer.valueOf(atp.intValue()) > 0) {
                                    endListing = false;
                                }
                            }  //check discontinued product -- END
                            else {
                                endListing = false;
                            }
                        }   //if product exist in database -- END
                        else {  //if product does not exist in database -- START
                            endListing = false;
                            f1.write(today + ": productStoreId " + activeListingProductStoreId + ", itemId " + itemId + ": " + productId + " does not exist in database.\n");
                        }   //if product does not exist in database -- END
                    }   //if hasVariation is false -- END
                    
                    
                    if (endListing) {   //if endListing is true -- START
                        count++;
                        
                        endListingList.add(itemId);
                        if (count % 10 == 0) {  //if endListingList has 10 members -- START
                            Debug.logError(activeListingProductStoreId + ": EndListingList: " + endListingList, module);
                            Map endEbayListingList = dispatcher.runSync(
                                                                        "TradingApiEndEbayActiveListingMulti",
                                                                        UtilMisc.toMap(
                                                                                       "itemIdList", endListingList,
                                                                                       "productStoreId", activeListingProductStoreId,
                                                                                       "userLogin", userLogin
                                                                                       )
                                                                        );
                            
                            if (ServiceUtil.isError(endEbayListingList)) {  //if endEbayListingList returns Error -- START
                                f1.write(today + ": productStoreId " + activeListingProductStoreId + ", failed to end eBay Active Listing for item ID List " + endListingList + "\n");
                            }   //if endEbayListingList returns Error -- END
                            else {  //if endEbayListingList returns Success -- START
                                endListingList.clear();
                            }   //if endEbayListingList returns Success -- END
                        }   //if endListingList has 10 members -- END
                    }   //if endListing is true -- END
                }   //loop ebayActiveListingList -- END
                if (UtilValidate.isNotEmpty(endListingList)) {  //run rest of endListingList -- START
                    Debug.logError(activeListingProductStoreId + ": EndListingList: " + endListingList, module);
                    Map endEbayListingList = dispatcher.runSync(
                                                                "TradingApiEndEbayActiveListingMulti",
                                                                UtilMisc.toMap(
                                                                               "itemIdList", endListingList,
                                                                               "productStoreId", activeListingProductStoreId,
                                                                               "userLogin", userLogin
                                                                               )
                                                                );
                    
                    if (ServiceUtil.isError(endEbayListingList)) {  //if endEbayListingList returns Error -- START
                        f1.write(today + ": productStoreId " + activeListingProductStoreId + ", failed to end eBay Active Listing for item ID List " + endListingList + "\n");
                    }   //if endEbayListingList returns Error -- END
                }   //run rest of endListingList -- END
                
            }   //loop productStoreId -- END
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        /*catch (GenericServiceException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }*/
        catch (IOException e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            //Debug.logError(e.getMessage(), module);
            return ServiceUtil.returnError(e.getMessage());
        }
        f1.close();
        return result;
    }   //checkDiscontinuedListingOnEbay
}