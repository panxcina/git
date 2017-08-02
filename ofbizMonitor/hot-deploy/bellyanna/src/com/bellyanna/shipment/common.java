package com.bellyanna.shipment;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.math.BigDecimal;
import java.util.List;
import java.util.LinkedList;
import java.util.Locale;
import java.text.SimpleDateFormat;
import javax.servlet.http.*;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.dom4j.io.SAXReader;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
//import org.ofbiz.content.content.ContentWorker;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityConditionList;
import org.ofbiz.entity.condition.EntityExpr;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.service.ServiceAuthException;
import org.ofbiz.service.ServiceValidationException;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.call.CompleteSaleCall;
import com.ebay.soap.eBLBaseComponents.ShipmentTrackingDetailsType;
import com.ebay.soap.eBLBaseComponents.ShipmentType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

import freemarker.template.TemplateException;

public class common {
    
    private static final String module= common.class.getName();
    
    public static Map<String, Object> testJava (DispatchContext dctx, Map serviceContext)
    throws Exception {
        
        Map mapContent = FastMap.newInstance();
        try {   //main try -- START
            
            
            
        }   //main try -- END
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return ServiceUtil.returnSuccess();
    }
    
    public static Map<String, Object> getApiShipmentInfo (DispatchContext dctx, Map serviceContext)
	throws GenericEntityException, IOException {    //getApiShipmentInfo
        
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Map mapContent = FastMap.newInstance();
        String orderId = (String) serviceContext.get("orderId");
        Map result = FastMap.newInstance();
        
        Calendar now = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
        
        FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/shipment/errorLog/getApiShipmentInfo.log", true);
        try {		//try block start
            boolean processSendRequest = true;
            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            GenericValue productStore = orderHeader.getRelatedOne("ProductStore", false);
            String facilityId = orderHeader.getString("originFacilityId");
            Map mapAccount = FastMap.newInstance();
            //mapAccount = accountInfo(delegator, productStore);	//get account information
            
            //Get Pickup information -- START
            //Get Pickup information > Pickup address -- START
            GenericValue pickupAddress = null;
            GenericValue facilityContactMechPurposeLocation = EntityUtil.getFirst(delegator.findByAnd("FacilityContactMechPurpose", UtilMisc.toMap("facilityId", facilityId, "contactMechPurposeTypeId", "PICKUP_LOCATION"), null, false));
			if (UtilValidate.isNotEmpty(facilityContactMechPurposeLocation)) {
				GenericValue facilityContactMechPostalAddress = delegator.findOne("ContactMech", UtilMisc.toMap("contactMechId",facilityContactMechPurposeLocation.getString("contactMechId")), false);
				pickupAddress = facilityContactMechPostalAddress.getRelatedOne("PostalAddress", false);
			}
			else {
				f1.write(today + ": orderId: " + orderId + ", could not find Pickup Postal Address for facility Id " + facilityId + "\n");
				processSendRequest = false;
			}
            //Get Pickup information > Pickup address -- END
            //Get Pickup information > Pickup Phone number -- START
            String pickupPhone = null;
			GenericValue facilityContactMechPurposePhone = EntityUtil.getFirst(delegator.findByAnd("FacilityContactMechPurpose", UtilMisc.toMap("facilityId", facilityId, "contactMechPurposeTypeId", "PICKUP_PHONE"), null, false));
			if (UtilValidate.isNotEmpty(facilityContactMechPurposePhone)) {  //if facilityContactMechPurposePhone is not null -- START
				GenericValue facilityContactMechPhone = delegator.findOne("ContactMech", UtilMisc.toMap("contactMechId",facilityContactMechPurposePhone.getString("contactMechId")), false);
				GenericValue facilityPickupTelecomNumber = facilityContactMechPhone.getRelatedOne("TelecomNumber", false);
				if (UtilValidate.isNotEmpty(facilityPickupTelecomNumber.getString("areaCode"))) {
					pickupPhone = facilityPickupTelecomNumber.getString("areaCode") + facilityPickupTelecomNumber.getString("contactNumber");
				}
				else {
					pickupPhone = facilityPickupTelecomNumber.getString("contactNumber");
				}
			}   //if facilityContactMechPurposePhone is not null -- END
			else {
                f1.write(today + ": orderId: " + orderId + ", could not find Pickup Phone/Mobile for facility Id " + facilityId + "\n");
				processSendRequest = false;
			}
            //Get Pickup information > Pickup Phone number -- END
            //Get Pickup information > Pickup email -- START
            GenericValue pickupEmailEntity = null;
			GenericValue facilityContactMechPurposeEmail = EntityUtil.getFirst(delegator.findByAnd("FacilityContactMechPurpose", UtilMisc.toMap("facilityId", facilityId, "contactMechPurposeTypeId", "PRIMARY_EMAIL"), null, false));
			if (UtilValidate.isNotEmpty(facilityContactMechPurposeEmail)) {
				pickupEmailEntity = delegator.findOne("ContactMech", UtilMisc.toMap("contactMechId",facilityContactMechPurposeEmail.getString("contactMechId")), false);
			}
			else {
                f1.write(today + ": orderId: " + orderId + ", could not find Pickup Email for facility Id " + facilityId + "\n");
				processSendRequest = false;
			}
            //Get Pickup information > Pickup email -- END
            //Get Pickup information -- END
            
            //Get ShipFrom information -- START
            //Get ShipFrom information > Address -- START
            GenericValue shipfromAddress = null;
			GenericValue facilityContactMechPurposeShipFromLocation = EntityUtil.getFirst(delegator.findByAnd("FacilityContactMechPurpose", UtilMisc.toMap("facilityId", facilityId, "contactMechPurposeTypeId", "SHIP_ORIG_LOCATION"), null, false));
			if (UtilValidate.isNotEmpty(facilityContactMechPurposeShipFromLocation)) {
				GenericValue facilityContactMechShipFromPostalAddress = delegator.findOne("ContactMech", UtilMisc.toMap("contactMechId",facilityContactMechPurposeShipFromLocation.getString("contactMechId")), false);
				shipfromAddress = facilityContactMechShipFromPostalAddress.getRelatedOne("PostalAddress", false);
			}
			else {
                f1.write(today + ": orderId: " + orderId + ", could not find ShipFrom Postal Address for facility Id " + facilityId + "\n");
				processSendRequest = false;
			}
            //Get ShipFrom information > Address -- END
            //Get ShipFrom information > Phone number -- START
            String shipfromPhone = null;
			GenericValue facilityContactMechPurposeShipFromPhone = EntityUtil.getFirst(delegator.findByAnd("FacilityContactMechPurpose", UtilMisc.toMap("facilityId", facilityId, "contactMechPurposeTypeId", "PHONE_SHIP_ORIG"), null, false));
			if (UtilValidate.isNotEmpty(facilityContactMechPurposeShipFromPhone)) {
				GenericValue facilityContactMechShipFromPhone = delegator.findOne("ContactMech", UtilMisc.toMap("contactMechId",facilityContactMechPurposeShipFromPhone.getString("contactMechId")), false);
				GenericValue facilityShipFromTelecomNumber = facilityContactMechShipFromPhone.getRelatedOne("TelecomNumber", false);
				if (UtilValidate.isNotEmpty(facilityShipFromTelecomNumber.getString("areaCode"))) {
					shipfromPhone = facilityShipFromTelecomNumber.getString("areaCode") + facilityShipFromTelecomNumber.getString("contactNumber");
				}
				else {
					shipfromPhone = facilityShipFromTelecomNumber.getString("contactNumber");
				}
			}
			else {
                f1.write(today + ": orderId: " + orderId + ", could not find ShipFrom Phone/Mobile for facility Id " + facilityId + "\n");
				processSendRequest = false;
			}
            //Get ShipFrom information > Phone number -- END
            //Get ShipFrom information -- END
            
            //Get ShipTo information -- START
            //Get ShipTo > Address -- START
            GenericValue shipToAddressOrderContactMech = EntityUtil.getFirst(delegator.findByAnd("OrderContactMech", UtilMisc.toMap("orderId", orderId, "contactMechPurposeTypeId", "SHIPPING_LOCATION"), null, false));
            GenericValue shiptoAddress = delegator.findOne("PostalAddress", UtilMisc.toMap("contactMechId", shipToAddressOrderContactMech.getString("contactMechId")), false);
            //Get ShipTo > Address -- END
            //Get ShipTo > Phone -- START
            GenericValue shiptoPhoneEntity = null;
            List<GenericValue> shiptoPhoneOrderContactMechs = delegator.findByAnd("OrderContactMech", UtilMisc.toMap("orderId", orderId, "contactMechPurposeTypeId", "PRIMARY_PHONE"), null, false);
            if (UtilValidate.isNotEmpty(shiptoPhoneOrderContactMechs)) {
                GenericValue shiptoPhoneOrderContactMech = EntityUtil.getFirst(shiptoPhoneOrderContactMechs);
                shiptoPhoneEntity = delegator.findOne("TelecomNumber", UtilMisc.toMap("contactMechId", shiptoPhoneOrderContactMech.getString("contactMechId")), false);
            }
            
            //Get ShipTo > Phone -- END
            //Get shipTo > Email -- START
            GenericValue shiptoEmailOrderContactMech = EntityUtil.getFirst(delegator.findByAnd("OrderContactMech", UtilMisc.toMap("orderId", orderId, "contactMechPurposeTypeId", "ORDER_EMAIL"), null, false));
            GenericValue shiptoEmailContactMech = delegator.findOne("ContactMech", UtilMisc.toMap("contactMechId", shiptoEmailOrderContactMech.getString("contactMechId")), false);
            String shiptoEmail = shiptoEmailContactMech.getString("infoString");
            //Get shipTo > Email -- END
            //Get ShipTo information -- END
            
            //Mapping variables for pickup information -- START
            String pickupEmail = pickupEmailEntity.getString("infoString");
            String pickupCountry = delegator.findOne("Geo",UtilMisc.toMap("geoId",pickupAddress.getString("countryGeoId")), false).getString("wellKnownText");
            String pickupProvince = delegator.findOne("Geo",UtilMisc.toMap("geoId",pickupAddress.getString("stateProvinceGeoId")), false).getString("geoCode") + "0000";
            String pickupCity = EntityUtil.getFirst(delegator.findByAnd("Geo", UtilMisc.toMap("geoName", pickupAddress.getString("city")), null, false)).getString("geoCode");
            String returnCity = pickupAddress.getString("city");
            String returnProvince = delegator.findOne("Geo",UtilMisc.toMap("geoId",pickupAddress.getString("stateProvinceGeoId")), false).getString("geoName");
            String pickupDistrict = delegator.findOne("Geo",UtilMisc.toMap("geoId",pickupAddress.getString("countyGeoId")), false).getString("geoCode");
            String pickupStreet = null;
            if (UtilValidate.isNotEmpty(pickupAddress.getString("address2"))) {
                pickupStreet = pickupAddress.getString("address1") + ", " + pickupAddress.getString("address2");
            }
            else {
                pickupStreet = pickupAddress.getString("address1");
            }
            String pickupPostcode = pickupAddress.getString("postalCode");
            String pickupContact = pickupAddress.getString("toName");
            //Mapping variables for pickup information -- END
            
            //Mapping variables for ShipFrom information -- START
            String shipfromContact = shipfromAddress.getString("toName");
            String shipfromStreet = null;
            if (UtilValidate.isNotEmpty(shipfromAddress.getString("address2"))) {
                shipfromStreet = shipfromAddress.getString("address1") + ", " + shipfromAddress.getString("address2");
            }
            else {
                shipfromStreet = shipfromAddress.getString("address1");
            }
            String shipfromDistrict = delegator.findOne("Geo",UtilMisc.toMap("geoId",shipfromAddress.getString("countyGeoId")), false).getString("geoCode");
            String shipfromCity = shipfromAddress.getString("city");
            //String shipfromProvince = delegator.findOne("Geo",UtilMisc.toMap("geoId",shipfromAddress.getString("stateProvinceGeoId"))).getString("geoCode") + "0000";
            String shipfromProvince = delegator.findOne("Geo",UtilMisc.toMap("geoId",shipfromAddress.getString("stateProvinceGeoId")), false).getString("wellKnownText");
            String shipfromPostcode = shipfromAddress.getString("postalCode");
            String shipfromCountry = delegator.findOne("Geo",UtilMisc.toMap("geoId",shipfromAddress.getString("countryGeoId")), false).getString("geoName");
            String shipfromEmail = pickupEmail;
            //Mapping variables for ShipFrom information -- END
            
            //Mapping variables for ShipTo information -- START
            String shiptoContact = shiptoAddress.getString("toName");
            String shiptoPhone = null;
            
            if (UtilValidate.isNotEmpty(shiptoPhoneEntity)) {   //if shiptoPhoneEntity is not null -- START
                if (shiptoPhoneEntity.getString("areaCode") != null) {
                    shiptoPhone = shiptoPhoneEntity.getString("areaCode") + shiptoPhoneEntity.getString("contactNumber");
                }
                else {
                    shiptoPhone = shiptoPhoneEntity.getString("contactNumber");
                }
            }   //if shiptoPhoneEntity is not null -- END
            else {  //if shiptoPhoneEntity is null -- START
                shiptoPhone = "0";
            }   //if shiptoPhoneEntity is null -- END
            
            String shiptoStreet = null;
            if (UtilValidate.isNotEmpty(shiptoAddress.getString("address2")))
            {
                shiptoStreet = shiptoAddress.getString("address1") + " " + shiptoAddress.getString("address2");
            }
            else {
                shiptoStreet = shiptoAddress.getString("address1");
            }
            String shiptoCity = shiptoAddress.getString("city");
            String shiptoProvince = delegator.findOne("Geo",UtilMisc.toMap("geoId",shiptoAddress.getString("stateProvinceGeoId")), false).getString("geoName");
            String shiptoPostcode = shiptoAddress.getString("postalCode");
            String shiptoCountry = delegator.findOne("Geo",UtilMisc.toMap("geoId",shiptoAddress.getString("countryGeoId")), false).getString("geoName");
            String shiptoCountryCode = delegator.findOne("Geo",UtilMisc.toMap("geoId",shiptoAddress.getString("countryGeoId")), false).getString("geoCode");
            //Mapping variables for ShipTo information -- END
            
            //Mapping -- START
            mapContent.put("OrderId", orderId);
            mapContent.put("PickUpEmail", pickupEmail);
            mapContent.put("PickUpCompany", "Bellyanna");
            mapContent.put("PickUpCountry", pickupCountry);
            mapContent.put("PickUpProvince", pickupProvince);
            mapContent.put("PickUpCity", pickupCity);
            mapContent.put("PickUpDistrict", pickupDistrict);
            mapContent.put("PickUpStreet", pickupStreet);
            mapContent.put("PickUpPostcode", pickupPostcode);
            mapContent.put("PickUpContact", pickupContact);
            mapContent.put("PickUpMobile", pickupPhone);
            mapContent.put("PickUpPhone", pickupPhone);
            mapContent.put("ReturnCity", returnCity);
            mapContent.put("ReturnProvince", returnProvince);
            mapContent.put("ShipFromContact", shipfromContact);
            mapContent.put("ShipFromCompany", "Bellyanna");
            mapContent.put("ShipFromStreet", shipfromStreet);
            mapContent.put("ShipFromDistrict", shipfromDistrict);
            mapContent.put("ShipFromCity", shipfromCity);
            mapContent.put("ShipFromProvince", shipfromProvince);
            mapContent.put("ShipFromPostcode", shipfromPostcode);
            mapContent.put("ShipFromCountry", shipfromCountry);
            mapContent.put("ShipFromEmail", shipfromEmail);
            mapContent.put("ShipFromMobile", shipfromPhone);
            mapContent.put("ShipToEmail", shiptoEmail);
            //mapContent.put("ShipToCompany", "");	//Optional
            mapContent.put("ShipToContact", shiptoContact);
            mapContent.put("ShipToPhone", shiptoPhone);	//what if customer didnt leave any phone number?
            mapContent.put("ShipToStreet", shiptoStreet);
            mapContent.put("ShipToCity", shiptoCity);
            if (shiptoProvince != null && shiptoProvince != "_NA_") {
                mapContent.put("ShipToProvince", shiptoProvince);
            }
            mapContent.put("ShipToPostcode", shiptoPostcode);
            mapContent.put("ShipToCountry", shiptoCountry);
            mapContent.put("ShipToCountryCode", shiptoCountryCode);
            mapContent.put("processSendRequest", processSendRequest);
            //Mapping -- END
            
            //Get orderItem -- START
            List<Map> orderItemMapList = new LinkedList<Map>();
            List<GenericValue> orderItems = orderHeader.getRelated("OrderItem", UtilMisc.toMap("statusId", "ITEM_COMPLETED"), null, false);
            
            if (UtilValidate.isEmpty(orderItems)) {
                orderItems = orderHeader.getRelated("OrderItem", UtilMisc.toMap("statusId", "ITEM_APPROVED"), null, false);
            }
            
            double totalShippedQty = 0;
            if (UtilValidate.isNotEmpty(orderItems)) {    //if orderItems is not empty -- START
                for (int i = 0; i < orderItems.size(); i++) {
                    totalShippedQty += orderItems.get(i).getBigDecimal("quantity").doubleValue();
                }
                for (GenericValue orderItem : orderItems) { //loop orderItems -- START
                    
                    Map mapItem = FastMap.newInstance();
                    String orderItemSeqId = orderItem.getString("orderItemSeqId");
                    BigDecimal orderItemQuantity = orderItem.getBigDecimal("quantity");	//Qty Shipped
                    
                    GenericValue orderItemProduct = delegator.findOne("Product",UtilMisc.toMap("productId", orderItem.getString("productId")), false);
                    String orderItemEbayItemNumber = delegator.findOne("OrderItemAttribute",UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId, "attrName", "eBay Item Number"), false).getString("attrValue");
                    String orderItemEbayTransactionId = delegator.findOne("OrderItemAttribute",UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId, "attrName", "EBAY_TRAN_ID"), false).getString("attrValue");
                    String orderItemEbaySellerUserName = delegator.findOne("OrderItemAttribute",UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId, "attrName", "SELLER_EBAY_USERNAME"), false).getString("attrValue");
                    String orderItemCustomsTitleCN = orderItemProduct.getString("declaredNameCn");
                    String orderItemCustomsTitleEN = orderItemProduct.getString("declaredNameEn");
                    //BigDecimal orderShipmentProductWeightBigDecimal = delegator.findOne("ShipmentPackage",UtilMisc.toMap("shipmentId",shipmentId, "shipmentPackageSeqId","00001"), false).getBigDecimal("weight");
                    double orderItemProductWeight = 50.0;
                    /*if (orderShipmentProductWeightBigDecimal != null) {
                     orderShipmentProductWeight = orderShipmentProductWeightBigDecimal.doubleValue() / totalShippedQty;
                     }
                     else {
                     Debug.logError("EPACKET-LOG: could not find weight for shipment ID " + shipmentId, module);
                     return null;
                     }*/
                    mapItem.put("productId", orderItem.getString("productId"));
                    mapItem.put("eBayItemID", orderItemEbayItemNumber);
                    mapItem.put("eBayTransactionID", orderItemEbayTransactionId);
                    mapItem.put("eBayBuyerID", orderItemEbaySellerUserName);
                    mapItem.put("PostedQTY", orderItemQuantity.intValue());
                    mapItem.put("DeclaredValue", (getDeclaredValue(totalShippedQty) / totalShippedQty) * orderItemQuantity.doubleValue());		//Declared product value
                    mapItem.put("Weight", orderItemProductWeight * orderItemQuantity.doubleValue() / 1000);		//weight is in KG
                    mapItem.put("CustomsTitleCN", orderItemCustomsTitleCN);
                    mapItem.put("CustomsTitleEN", orderItemCustomsTitleEN);
                    orderItemMapList.add(mapItem);
                }   //loop orderItems -- END
            }   //if orderItems is not empty -- END
            else {
                f1.write(today + ": orderId: " + orderId + ", could not find any order item\n");
				processSendRequest = false;
            }
            //Get orderItem -- END
            
            f1.close();
            
            result.put("mapContent", mapContent);
            result.put("mapItem", orderItemMapList);
            
        }		//try block end
        catch (GenericEntityException e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
            result = ServiceUtil.returnError(e.getMessage());
            return result;
        }
        
        return result;
        
    }   //getApiShipmentInfo
    
    private static double getDeclaredValue(double totalShippedQty) {    //getDeclaredValue
        
        double result = 0.0;
        if (totalShippedQty == 1d) {
            result = 7;
        }
        else if (totalShippedQty == 2d) {
            result = 10;
        }
        else if (totalShippedQty == 3d) {
            result = 12.5;
        }
        else if (totalShippedQty == 4d) {
            result = 14;
        }
        else if (totalShippedQty > 4d) {
            result = 17;
        }
        /*double arr [] = new double [] {8d, 8.5d, 9d, 9.5d, 10d, 10.5d, 11d};
         int length = arr.length;
         double r = Math.random();
         int s = Double.valueOf(Math.floor(r *   Double.valueOf(length))).intValue(); */
        return result;
    }   //getDeclaredValue
    
    public static Map uploadTrackingCodeToEbay(DispatchContext dctx, Map context)
    throws GenericEntityException, ServiceAuthException, ServiceValidationException,
    GenericServiceException {   //uploadTrackingCodeToEbay
        
		Delegator delegator = dctx.getDelegator();
		LocalDispatcher dispatcher = dctx.getDispatcher();
		String shipmentId = (String) context.get("shipmentId");
        Debug.logError("Yasin DEBUG: ShipmentId : " + shipmentId, module);
        
		StringBuffer returnMessage = new StringBuffer();
        try {   //try -- START
            GenericValue shipment = delegator.findOne("Shipment", UtilMisc.toMap("shipmentId", shipmentId), false);
            Debug.logError("Yasin DEBUG: shipmentId " + shipmentId + ": " + shipment, module);
            String orderId = shipment.getString("primaryOrderId");
            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            String salesChannel = orderHeader.getString("salesChannelEnumId");
            String productStoreId = orderHeader.getString("productStoreId");
            GenericValue ebayConfig = delegator.findOne("EbayConfig", UtilMisc.toMap("productStoreId", productStoreId), false);
            GenericValue productStoreEbaySetting = delegator.findOne("ProductStoreEbaySetting", UtilMisc.toMap("productStoreId",productStoreId), false);
            String packUpdateShipmentStatus = productStoreEbaySetting.getString("packUpdateShipmentStatus");
            GenericValue userLogin = (GenericValue) context.get("userLogin");
            String eol = System.getProperty("line.separator");
            String noteMsg = "eBay shipment status update: " + eol;
            
            if(packUpdateShipmentStatus.equals("Y") && salesChannel.equals("EBAY_SALES_CHANNEL")) { //IF clause to run main code -- START
                List<GenericValue> shipmentItemList = shipment.getRelated("ShipmentItem",null, null, false);
                //		String shipGroupSeqId = shipment.getString("primaryShipGroupSeqId");
                String carrierCode = null;
                String trackingCode = null;
                
                GenericValue orderItemShipGroup = shipment.getRelatedOne("PrimaryOrderItemShipGroup", false);
                String shipmentMethod = orderItemShipGroup.getString("shipmentMethodTypeId");
                if(!shipmentMethod.equals("EPACKET") && !shipmentMethod.startsWith("STANDARD")) { //IF shipment is not equal to EPACKET -- START
                    if(orderItemShipGroup != null) {
                        carrierCode = orderItemShipGroup.getString("carrierPartyId");
                    }
                    
                    List<GenericValue> shipmentPackageRouteSegList = shipment.getRelated("ShipmentPackageRouteSeg", null, null, false);
                    for(GenericValue shipmentpackageRouteSeq : shipmentPackageRouteSegList) {
                        trackingCode = shipmentpackageRouteSeq.getString("trackingCode");
                        if(trackingCode != null) {
                            break;
                        }
                    }
                    if(trackingCode == null) {
                        returnMessage.append("\r\nOrder [" + orderId + "] : No tracking code.");
                    }
                    
                    for(GenericValue shipmentItem : shipmentItemList) { //Loop for each ShipmentItemList -- START
                        String productId = shipmentItem.getString("productId");
                        GenericValue orderItem = EntityUtil.getFirst(delegator.findByAnd("OrderItem", UtilMisc.toMap("orderId", orderId, "productId", productId), null, false));
                        if(orderItem == null) {
                            returnMessage.append("\r\nOrder [" + orderId + "] : No order item [" + productId + "] found.");
                            continue;
                        }
                        
                        String itemSeqId = orderItem.getString("orderItemSeqId");
                        String accountName = getAttrValueFromOrderItem(orderItem, "SELLER_EBAY_USERNAME");
                        if(accountName == null) {
                            returnMessage.append("\r\nOrder [" + orderId + "] : No eBay account found.");
                            continue;
                        }
                        
                        String ebayItemId = getAttrValueFromOrderItem(orderItem, "eBay Item Number");
                        if(ebayItemId == null) {
                            returnMessage.append("\r\nOrder [" + orderId + "] : No eBay item id found.");
                            continue;
                        }
                        
                        String transactionId = getAttrValueFromOrderItem(orderItem, "EBAY_TRAN_ID");
                        if(transactionId == null) {
                            returnMessage.append("\r\nOrder [" + orderId + "] : No eBay transaction id found.");
                            continue;
                        }
                        
                        /*String apiToken = getEbayApiToken(accountName);*/
                        String apiToken = ebayConfig.getString("token");
                        if(apiToken == null) {
                            returnMessage.append("\r\nOrder [" + orderId + "] : No eBay api token found for [" + accountName + "].");
                            continue;
                        }
                        
                        try {
                            completeSale(apiToken, ebayItemId, transactionId, trackingCode, carrierCode);
                            noteMsg += "- Update complete for Item " + itemSeqId + " : " + ebayItemId + eol;
                        } catch (Exception e) {
                            e.printStackTrace();
                            noteMsg += "- Update failed for Item " + itemSeqId + " : " + ebayItemId + eol;
                        }
                    }   //Loop for each ShipmentItemList -- END
                    
                    dispatcher.runSync("createOrderNote", UtilMisc.<String, Object>toMap("orderId", orderId, "note", noteMsg, "internalNote", "Y", "userLogin", userLogin));
                    dispatcher.runSync("updateOrderAttribute", UtilMisc.toMap("orderId", orderId, "attrName", "TRACKING_UPDATE", "attrValue", "Y", "userLogin", userLogin));
                }   //IF clause to run main code -- END
                else {
                    noteMsg = "Auto Update shipment status to eBay is disabled. Not Updating shipment Status on eBay after packing order";
                    if(packUpdateShipmentStatus.equals("Y")) {
                        noteMsg = "Not Updating shipment status on eBay after packing order. Order Sales Channel is not eBay Or Shipping method is EPACKET";
                    }
                    dispatcher.runSync("createOrderNote", UtilMisc.<String, Object>toMap("orderId", orderId, "note", noteMsg, "internalNote", "Y", "userLogin", userLogin));
                }
            }   //IF shipment is not equal to EPACKET -- END
        }   //try -- END
        catch (GenericEntityException e) {
			e.printStackTrace();
		}
        catch (GenericServiceException e) {
			e.printStackTrace();
		}
		//Debug.logInfo(returnMessage.toString(), module);
		return ServiceUtil.returnSuccess(returnMessage.toString());
	}   //uploadTrackingCodeToEbay
    
    private static String getAttrValueFromOrderItem(GenericValue orderItem, String attrName) {  //getAttrValueFromOrderItem
		String attrValue = null;
		try {
			GenericValue value = EntityUtil.getFirst(orderItem.getRelated("OrderItemAttribute", UtilMisc.toMap("attrName", attrName), null, false));
			if(value != null) {
				attrValue = value.getString("attrValue");
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return attrValue;
	}   //getAttrValueFromOrderItem
    
    public static void completeSale(String apiToken, String ebayItemId, String transactionId, String trackingCode, String carrierCode)
    throws ApiException, SdkException, Exception {  //completeSale
        ApiContext apiContext = new ApiContext();
        // set API Token to access eBay API Server
        ApiCredential cred = apiContext.getApiCredential();
        //Set Auth Token
        //cred.seteBayToken("YourToken");
        cred.seteBayToken(apiToken);
        
        apiContext.setApiServerUrl("https://api.ebay.com/wsapi");// Pointing to sandbox for testing.
        
        //apiContext.getApiLogging().setLogSOAPMessages(true);// This will log SOAP requests and responses
        
        apiContext.setSite(SiteCodeType.US); // Set site to UK
        
        CompleteSaleCall completeSaleApi = new CompleteSaleCall(apiContext);
        
        completeSaleApi.setItemID(ebayItemId);
        completeSaleApi.setTransactionID(transactionId);
        completeSaleApi.setShipped(true);
        
        ShipmentType shipType = new ShipmentType();
        
        ShipmentTrackingDetailsType shpmnt = new ShipmentTrackingDetailsType();
        shpmnt.setShipmentTrackingNumber(trackingCode);
        shpmnt.setShippingCarrierUsed(carrierCode != null ? carrierCode : "CHINAPOST");
        shipType.setShipmentTrackingDetails(new ShipmentTrackingDetailsType[]{shpmnt});
        completeSaleApi.setShipment(shipType);
        completeSaleApi.completeSale();
    }   //completeSale
    
} //END class
