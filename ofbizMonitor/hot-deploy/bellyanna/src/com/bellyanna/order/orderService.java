package com.bellyanna.order;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.lang.Math;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import javax.servlet.http.*;

import javolution.util.FastMap;

import org.ofbiz.base.util.UtilMisc;

import org.ofbiz.base.util.UtilXml;
import org.ofbiz.base.util.ObjectType;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilValidate;
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

import com.bellyanna.common.bellyannaService;
import com.bellyanna.ebay.common;
import com.bellyanna.ebay.requestXML;
import com.bellyanna.ebay.eBayTradingAPI;

public class orderService {
	private static final String module = orderService.class.getName();
    private static final String eol = System.getProperty("line.separator");
	
public static Map<String, Object> createOrderRefund (DispatchContext dctx, Map context)
	throws GenericEntityException, GenericServiceException {    //createOrderRefund
	
        Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String orderId = (String) context.get("orderId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        try {
            String refundId = delegator.getNextSeqId("Refund");
            GenericValue refundData = delegator.makeValue("Refund", UtilMisc.toMap("refundId", refundId));
            
        }
        catch (Exception e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
    return result;
}   //createOrderRefund

    public static Map<String, Object> sendToBeShippedEbayMessage (DispatchContext dctx, Map context)
	throws GenericEntityException, GenericServiceException {    //sendToBeShippedEbayMessage
        
        Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String orderId = (String) context.get("orderId");
        String ebayUserId = (String) context.get("ebayUserId");
        String productStoreId = (String) context.get("productStoreId");
        String emailTemplateName = (String) context.get("emailTemplateName");
        String ebayItemId = (String) context.get("ebayItemId");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map sendMessage = null;
        
        if (ebayItemId == null) {
            result = ServiceUtil.returnError("No eBay Item Id found!");
            return result;
        }
        
        /*Debug.logError("order ID is " + orderId, module);
        Debug.logError("ebay User ID is " + ebayUserId, module);
        Debug.logError("Product Store ID is " + productStoreId, module);
        Debug.logError("Email Template Name is " + emailTemplateName, module);
        Debug.logError("ebay item id is " + ebayItemId, module);*/
        
        GenericValue ebayEmailTemplate = EntityUtil.getFirst(delegator.findByAnd("EbayEmailTemplate", UtilMisc.toMap("emailTemplateName", emailTemplateName), null, false));
        GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
        GenericValue productStoreRole = EntityUtil.getFirst(delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"), "roleTypeId", "EBAY_ACCOUNT"), null, false));
        GenericValue partyGroup = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", productStoreRole.getString("partyId")), false);
        String sellerUserId = partyGroup.getString("groupName");
        
        try {
            //building subject part -- START
            String subjectTemplate = ebayEmailTemplate.getString("subject");
            VelocityContext subjectVC = new VelocityContext();
            subjectVC.put("sellerUserId", sellerUserId);
            StringWriter subjectBody = new StringWriter();
            Velocity.evaluate(subjectVC, subjectBody, "subjectTemplate", subjectTemplate);
            //building subject part -- END
            
            //Building message body -- START
            Velocity.init();
            
            String mainTemplate = ebayEmailTemplate.getString("content");   //building main part -- START
            VelocityContext mainVC = new VelocityContext();
            mainVC.put("sellerUserId", sellerUserId);
            mainVC.put("ebayItemId", ebayItemId);
            StringWriter contentBody = new StringWriter();
            Velocity.evaluate(mainVC, contentBody, "mainTemplate", mainTemplate);   //building main part -- END
            //Building message body -- END
            
            
            //Debug.logError("Email content is " + contentBody, module);
            //Debug.logError("email subject is  " + subjectBody, module);
            
            try {   //send message to customer via eBay message -- START
                sendMessage = dispatcher.runSync(
                                                     "TradingApiAddMemberMessageAAQToPartnerRequest",
                                                     UtilMisc.toMap(
                                                                    "productStoreId", productStoreId,
                                                                    "eBayItemId", ebayItemId,
                                                                    "eBayUserId", ebayUserId,
                                                                    "questionType", "Shipping",
                                                                    "messageSubject", subjectBody.toString(),
                                                                    "messageBody", contentBody.toString(),
                                                                    "userLogin", userLogin
                                                                    )
                                                     );
            } catch (GenericServiceException e) {
                Debug.logError(e, module);
                return ServiceUtil.returnError(e.getMessage());
            }   //send message to customer via eBay message -- END
         }
         catch (Exception e) {
         e.printStackTrace();
         return ServiceUtil.returnError(e.getMessage());
         }
        
        if (ServiceUtil.isSuccess(sendMessage)) {   //if SendMessage success -- START
            try {   //update orderAttribute, increase OSA_EMAIL_SENT by 1 -- START*/
                GenericValue orderAttribute = delegator.findOne("OrderAttribute", UtilMisc.toMap("orderId", orderId, "attrName", "OSA_EMAIL_SENT"), false);
                int freq = Integer.parseInt(orderAttribute.getString("attrValue"));
                GenericValue gv = delegator.makeValue("OrderAttribute");
                gv.put("orderId", orderId);
                gv.put("attrName", "OSA_EMAIL_SENT");
                gv.put("attrValue", (freq + 1) + "");
                
                delegator.createOrStore(gv);
            } catch (GenericEntityException e) {
                Debug.logError(e, module);
                return ServiceUtil.returnError(e.getMessage());
            }   //update orderAttribute, increase OSA_EMAIL_SENT by 1 -- END
            
            try {   //update order notes -- START
                String note = "Successfully sent email template: " + emailTemplateName;
                dispatcher.runSync(
                                   "createOrderNote",
                                   UtilMisc.toMap(
                                                  "orderId", orderId,
                                                  "internalNote", "Y",
                                                  "note", note,
                                                  "userLogin", userLogin
                                                  )
                                   );
            } catch (GenericServiceException e) {
                Debug.logError(e, module);
                return ServiceUtil.returnError(e.getMessage());
            }   //update order notes -- END
         }  //if SendMessage success -- END
        
        return result;
    }   //sendToBeShippedEbayMessage

    public static Map<String, Object> copyOrderItemAttribute (DispatchContext dctx, Map context)
	throws GenericEntityException, GenericServiceException {    //copyOrderItemAttribute
        
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String orderIdTo = (String) context.get("orderIdTo");
        String orderIdFrom = (String) context.get("orderIdFrom");
        String orderItemSeqIdFrom = (String) context.get("orderItemSeqIdFrom");
        String orderItemSeqIdTo = (String) context.get("orderItemSeqIdTo");
        GenericValue checkOrderItem = null;
        GenericValue checkOrderItemAttribute = null;
        
        if (orderIdTo.equals(orderIdFrom)) {    //if orderIdFrom is equal to orderIdTo -- START
            //Debug.logError("orderIdFrom is equal to orderIdTo, not running service", module);
            result= ServiceUtil.returnError("orderIdFrom is equal to orderIdTo");
            return result;
        }   //if orderIdFrom is equal to orderIdTo -- END
        
        if (orderItemSeqIdTo != null) { //if orderItemSeqIdTo is not null -- START
            if (!orderItemSeqIdTo.matches("[0-9]{5}")) {    //if check orderItemSeqIdTo format -- START
                //Debug.logError("orderItemSeqIdTo does not have valid format. Has to be in DecimalFormat 00000", module);
                return ServiceUtil.returnError("orderItemSeqIdTo does not have valid format. Has to be in DecimalFormat 00000");
            }   //if check orderItemSeqIdTo format -- END
            else {  //if check orderItemSeqId exist in orderItem -- START
                checkOrderItem = delegator.findOne("OrderItem", UtilMisc.toMap("orderId", orderIdTo, "orderItemSeqId", orderItemSeqIdTo), false);
                checkOrderItemAttribute = EntityUtil.getFirst(delegator.findByAnd("OrderItemAttribute", UtilMisc.toMap("orderId", orderIdTo, "orderItemSeqId", orderItemSeqIdTo), null, false));
                if (UtilValidate.isEmpty(checkOrderItem)) {
                    //Debug.logError("Order ID " + orderIdTo + " does not have orderItem with orderItemSeqId " + orderItemSeqIdTo, module);
                    return ServiceUtil.returnError("Order ID " + orderIdTo + " does not have orderItem with orderItemSeqId " + orderItemSeqIdTo);
                }
                else if (UtilValidate.isNotEmpty(checkOrderItemAttribute)) {
                    //Debug.logError("Order ID " + orderIdTo + " with orderItemSeqId " + orderItemSeqIdTo + " already has some orderItemAttribute data", module);
                    return ServiceUtil.returnError("Order ID " + orderIdTo + " with orderItemSeqId " + orderItemSeqIdTo + " already has some orderItemAttribute data");
                }
            } //if check orderItemSeqId exist in orderItem -- END
        }   //if orderItemSeqIdTo is not null -- END
        
        List<GenericValue> orderItemAttributesFromList = null;
        try {
            if (orderItemSeqIdFrom != null) {   //if orderItemSeqIdFrom is not null -- START
                if (orderItemSeqIdFrom.matches("[0-9]{5}")) {   //if orderItemSeqIdFrom has correct format -- START
                    orderItemAttributesFromList = delegator.findByAnd("OrderItemAttribute", UtilMisc.toMap("orderId", orderIdFrom, "orderItemSeqId", orderItemSeqIdFrom),UtilMisc.toList("orderItemSeqId"), false);
                }   //if orderItemSeqIdFrom has correct format -- END
                else {  //if orderItemSeqIdFrom has wrong format -- START
                    //Debug.logError("orderItemSeqIdFrom does not have valid format. Has to be in DecimalFormat 00000", module);
                    return ServiceUtil.returnError("orderItemSeqIdFrom does not have valid format. Has to be in DecimalFormat 00000");
                }   //if orderItemSeqIdFrom has wrong format -- END
                
            }   //if orderItemSeqIdFrom is not null -- START
            else {  //if orderItemSeqIdFrom is null -- START
                if (orderItemSeqIdTo != null) { //if orderItemSeqIdTo is not null and orderItemSeqIdFrom is null -- START
                    //Debug.logError("orderItemSeqIdFrom is null, while orderItemSeqIdTo is not null. Not running the service due to possible data error!", module);
                    return ServiceUtil.returnError("orderItemSeqIdFrom is null, while orderItemSeqIdTo is not null. Not running the service due to possible data error!");
                }   //if orderItemSeqIdTo is not null and orderItemSeqIdFrom is null -- END
                orderItemAttributesFromList = delegator.findByAnd("OrderItemAttribute", UtilMisc.toMap("orderId", orderIdFrom),UtilMisc.toList("orderItemSeqId"), false);
            }   //if orderItemSeqIdFrom is null -- END
            
            String nextOrderItemSeqIdTo = null;
            DecimalFormat df = new DecimalFormat("00000");
            if (orderItemSeqIdTo != null) { //if orderItemSeqIdTo is not null -- START
                nextOrderItemSeqIdTo = orderItemSeqIdTo;
            }   //if orderItemSeqIdTo is not null -- END
            else {  //if orderItemSeqIdTo is null -- START
                GenericValue lastOrderItemAttrTo = EntityUtil.getFirst(delegator.findByAnd("OrderItemAttribute", UtilMisc.toMap("orderId", orderIdTo),UtilMisc.toList("-orderItemSeqId"), false));
                nextOrderItemSeqIdTo = df.format(Integer.parseInt(lastOrderItemAttrTo.getString("orderItemSeqId").replace("0","")) + 1);
            }   //if orderItemSeqIdTo is null -- END
            
            //Debug.logError("Next orderItemSeqID is "  + nextOrderItemSeqIdTo, module);
            GenericValue testOrderItemTo = delegator.findOne("OrderItem", UtilMisc.toMap("orderId", orderIdTo, "orderItemSeqId", nextOrderItemSeqIdTo), false);
            if (UtilValidate.isNotEmpty(testOrderItemTo)) {  //If orderItemAttribute has not been created yet -- START
                if (UtilValidate.isNotEmpty(orderItemAttributesFromList)) { //if orderItemAttributesList is not empty -- START
                    String NextOrderItemSeqIdFrom = null;
                    String currentOrderItemSeqIdFrom = null;
                    String oldOrderItemSeqIdFrom = null;
                    for (GenericValue orderItemAttrFrom : orderItemAttributesFromList) {    //loop orderItemAttr -- START
                        currentOrderItemSeqIdFrom = orderItemAttrFrom.getString("orderItemSeqId");
                        if (oldOrderItemSeqIdFrom != null && !oldOrderItemSeqIdFrom.equals(currentOrderItemSeqIdFrom)) {    //if orderItemSeqId has changed 
                            nextOrderItemSeqIdTo = df.format(Integer.parseInt(nextOrderItemSeqIdTo.replace("0","")) + 1);
                            checkOrderItem = delegator.findOne("OrderItem", UtilMisc.toMap("orderId", orderIdTo, "orderItemSeqId", nextOrderItemSeqIdTo), false);
                            if (UtilValidate.isEmpty(checkOrderItem)) {
                                //Debug.logError("Order ID " + orderIdTo + " does not have orderItem with orderItemSeqId " + nextOrderItemSeqIdTo, module);
                                return ServiceUtil.returnError("Order ID " + orderIdTo + " does not have orderItem with orderItemSeqId " + nextOrderItemSeqIdTo);
                            }
                        }
                        
                        String name = orderItemAttrFrom.getString("attrName");
                        String value = orderItemAttrFrom.getString("attrValue");
                        //Debug.logError(orderIdFrom + "-" + currentOrderItemSeqIdFrom + " copied to " + orderIdTo + "-" + nextOrderItemSeqIdTo + " : " + name, module);
                        GenericValue gv = delegator.makeValue("OrderItemAttribute");
                         gv.put("orderId", orderIdTo);
                         gv.put("attrName", name);
                         gv.put("attrValue", value);
                         gv.put("orderItemSeqId", nextOrderItemSeqIdTo);
                         delegator.create(gv);
                        //Debug.logError("orderItemAttr Created", module);
                        oldOrderItemSeqIdFrom = currentOrderItemSeqIdFrom;
                    }   //loop orderItemAttr -- END
                }   //if orderItemAttributesList is not empty -- END
                else {  //if orderItemAttributesList is empty -- START
                    //Debug.logError("orderItemAttributesList is empty, possible wrong orderItemSeqIdFrom", module);
                    return ServiceUtil.returnError("orderItemAttributesList is empty, possible wrong orderItemSeqIdFrom");
                }   //if orderItemAttributesList is empty -- END
            }   //If orderItemAttribute has not been created yet -- END
            else {  //If orderItemAttribute has been created yet -- START
                //Debug.logError("Order ID " + orderIdTo + " does not have orderItem with orderItemSeqId " + nextOrderItemSeqIdTo + ". Have you added the new product?", module);
                return ServiceUtil.returnError("Order ID " + orderIdTo + " does not have orderItem with orderItemSeqId " + nextOrderItemSeqIdTo + ". Have you added the new product?");
            }   //If orderItemAttribute has been created yet -- END
        } catch (GenericEntityException e) {
            Debug.logError(e, module);
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return result;
    }   //copyOrderItemAttribute
    
    public static Map<String, Object> updateEpacketTrackingStatus (DispatchContext dctx, Map context)
	throws GenericServiceException, GenericEntityException {    //updateEpacketTrackingStatus
        
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        int lastXDays = (Integer) context.get("lastXDays");
        int toLastXDays = (Integer)  context.get("toLastXDays");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        try {   //main try -- START
            if (UtilValidate.isNotEmpty(lastXDays)) {
                if (lastXDays <= 5) {
                    lastXDays = 5;
                }
                
            }
            Calendar fromDay = Calendar.getInstance();
            Calendar toDay = Calendar.getInstance();
            fromDay.set(Calendar.DATE, fromDay.get(Calendar.DATE) - lastXDays);
            toDay.set(Calendar.DATE, toDay.get(Calendar.DATE) - toLastXDays);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
            //Date resultDate = fromDay.getTime();
            Debug.logError("FromDay is " + sdf.format(fromDay.getTime()).toString(), module);
            Debug.logError("ToDay is " + sdf.format(toDay.getTime()).toString(), module);
            Timestamp fromDate = Timestamp.valueOf(sdf.format(fromDay.getTime()));
            Timestamp toDate = Timestamp.valueOf(sdf.format(toDay.getTime()));
            
            DynamicViewEntity sprsDynamicView = new DynamicViewEntity();
            // Construct a dynamic view entity
            sprsDynamicView.addMemberEntity("sprs", "ShipmentPackageRouteSeg");
            sprsDynamicView.addAlias("sprs", "trackingCode");
            sprsDynamicView.addAlias("sprs", "trackingStatus");
            sprsDynamicView.addAlias("sprs", "shipmentId");
            sprsDynamicView.addAlias("sprs", "shipmentPackageSeqId");
            sprsDynamicView.addAlias("sprs", "shipmentRouteSegmentId");
            sprsDynamicView.addAlias("sprs", "createdStamp");
            
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                            EntityCondition.makeCondition("createdStamp",EntityOperator.GREATER_THAN_EQUAL_TO ,fromDate),
                                                                            EntityCondition.makeCondition("createdStamp",EntityOperator.LESS_THAN ,toDate),
                                                                            EntityCondition.makeCondition(
                                                                                        UtilMisc.toList(
                                                                                                EntityCondition.makeCondition("trackingStatus",EntityOperator.EQUALS ,null),
                                                                                                EntityCondition.makeCondition("trackingStatus",EntityOperator.EQUALS ,"PENDING"),
                                                                                                EntityCondition.makeCondition("trackingStatus",EntityOperator.EQUALS ,"DELIVERY"),
                                                                                                EntityCondition.makeCondition("trackingStatus",EntityOperator.EQUALS ,"PICKUP"),
                                                                                                EntityCondition.makeCondition("trackingStatus",EntityOperator.EQUALS ,"TRANSPORT")
                                                                                                ),
                                                                                       EntityOperator.OR),
                                                                            EntityCondition.makeCondition("trackingCode",EntityOperator.LIKE ,"LK%")
                                                                            ),
                                                                       EntityOperator.AND);
            EntityListIterator sprsELI = delegator.findListIteratorByCondition(sprsDynamicView,
                                                                                condition,
                                                                                null,
                                                                                UtilMisc.toList("trackingCode", "trackingStatus", "createdStamp", "shipmentId", "shipmentPackageSeqId", "shipmentRouteSegmentId"),
                                                                                UtilMisc.toList("createdStamp"),
                                                                                //new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true));
                                                                               null);
            GenericValue sprs = null;
            int i = 0;
            while ((sprs = sprsELI.next()) != null) {   //loop sprs -- START
                i++;
                String trackingCode = sprs.getString("trackingCode");
                String shipmentId = sprs.getString("shipmentId"); 
                String shipmentPackageSeqId = sprs.getString("shipmentPackageSeqId");
                String shipmentRouteSegmentId = sprs.getString("shipmentRouteSegmentId");
                Debug.logError(i + "= trackingCode: " + trackingCode + ", trackingStatus: " + sprs.getString("trackingStatus") + ", createdStamp: " + sprs.getTimestamp("createdStamp"), module);
                
                Map getAPACShippingPackageStatus = dispatcher.runSync("getAPACShippingPackageStatus",
                                                                      UtilMisc.toMap(
                                                                                     "trackingNumber", trackingCode,
                                                                                     "userLogin", userLogin
                                                                                     )
                                                                      );
                if (ServiceUtil.isSuccess(getAPACShippingPackageStatus)) {  //if get status service success -- START
                    String status = getAPACShippingPackageStatus.get("status").toString();
                    if (status.length() > 2) {  //if ACK status is success -- START
                        Debug.logError(i + " TrackingCode " + trackingCode + " returned status " + status, module);
                        status = "99";
                    }   //if ACK status is success -- END
                    GenericValue enumeration = EntityUtil.getFirst(delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "PCKG_STATUS", "sequenceId", status), null, false));
                    GenericValue gv = delegator.findOne("ShipmentPackageRouteSeg", UtilMisc.toMap("shipmentId", shipmentId, "shipmentPackageSeqId", shipmentPackageSeqId, "shipmentRouteSegmentId", shipmentRouteSegmentId), false);
                    gv.put("trackingStatus", enumeration.getString("enumCode"));
                    delegator.store(gv);
                }   //if get status service success -- END
            }   //loop sprs -- END
            sprsELI.close();
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return ServiceUtil.returnSuccess();
        
    }   //updateEpacketTrackingStatus
    
    public static Map<String, Object> sendEpacketFeedbackMessage (DispatchContext dctx, Map context)
	throws GenericServiceException, GenericEntityException {    //sendEpacketFeedbackMessage
        
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        try {   //main try -- START
            DynamicViewEntity sprsDynamicView = new DynamicViewEntity();
            // Construct a dynamic view entity
            sprsDynamicView.addMemberEntity("sprs", "ShipmentPackageRouteSeg");
            sprsDynamicView.addMemberEntity("shipment", "Shipment");
            sprsDynamicView.addMemberEntity("OH", "OrderHeader");
            sprsDynamicView.addMemberEntity("OA", "OrderAttribute");
            sprsDynamicView.addAlias("sprs", "trackingCode");
            sprsDynamicView.addAlias("sprs", "trackingStatus");
            sprsDynamicView.addAlias("sprs", "shipmentId");
            sprsDynamicView.addAlias("sprs", "shipmentPackageSeqId");
            sprsDynamicView.addAlias("sprs", "shipmentRouteSegmentId");
            sprsDynamicView.addAlias("sprs", "createdStamp");
            sprsDynamicView.addAlias("OH", "orderId");
            sprsDynamicView.addAlias("OH", "salesChannelEnumId");
            sprsDynamicView.addAlias("OH", "productStoreId");
            sprsDynamicView.addAlias("OA", "attrName");
            sprsDynamicView.addAlias("OA", "attrValue");
            sprsDynamicView.addAlias("OA", "lastUpdatedStamp");
            sprsDynamicView.addViewLink("sprs", "shipment", Boolean.TRUE, ModelKeyMap.makeKeyMapList("shipmentId"));
            sprsDynamicView.addViewLink("shipment", "OH", Boolean.TRUE, ModelKeyMap.makeKeyMapList("primaryOrderId", "orderId"));
            sprsDynamicView.addViewLink("shipment", "OA", Boolean.TRUE, ModelKeyMap.makeKeyMapList("primaryOrderId", "orderId"));
            
            EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      EntityCondition.makeCondition("trackingStatus",EntityOperator.EQUALS ,"SUCCESS"),
                                                                                      EntityCondition.makeCondition("attrName",EntityOperator.EQUALS , "FEEDBACK_RECEIVED"),
                                                                                      EntityCondition.makeCondition("attrValue",EntityOperator.EQUALS , "N"),
                                                                                      EntityCondition.makeCondition("salesChannelEnumId",EntityOperator.EQUALS ,"EBAY_SALES_CHANNEL"),
                                                                                      EntityCondition.makeCondition("orderId",EntityOperator.NOT_LIKE ,"%-BA%"),
                                                                                      EntityCondition.makeCondition("orderId",EntityOperator.NOT_LIKE ,"%R")
                                                                                      ),
                                                                      EntityOperator.AND);
            EntityListIterator sprsELI = delegator.findListIteratorByCondition(sprsDynamicView,
                                                                               condition,
                                                                               null,
                                                                               //UtilMisc.toList("trackingCode", "trackingStatus", "createdStamp", "shipmentId", "shipmentPackageSeqId", "shipmentRouteSegmentId"),
                                                                               null,
                                                                               //UtilMisc.toList("createdStamp"),
                                                                               null,
                                                                               //new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true));
                                                                               null);
            GenericValue sprs = null;
            while ((sprs = sprsELI.next()) != null) {   //loop sprs -- START
                //Debug.logError("orderId: " + sprs.getString("orderId") + ", trackingStatus: " + sprs.getString("trackingStatus") + ", attrName: " + sprs.getString("attrName") + ", attrValue: " + sprs.getString("attrValue") + ", salesChannelEnumId: " + sprs.getString("salesChannelEnumId"), module);
                String orderId = sprs.getString("orderId");
                String productStoreId = sprs.getString("productStoreId");
                String emailTemplateName = "Asking for feedback (Shipped)";
                int feedbackSent = 0;
                GenericValue productStoreEbaySetting = delegator.findOne("ProductStoreEbaySetting", UtilMisc.toMap("productStoreId", productStoreId), false);
                int daysAskFeedback = Integer.parseInt(productStoreEbaySetting.getLong("daysAskFeedback").toString());
                int askFeedbackMaxAttempt = Integer.parseInt(productStoreEbaySetting.getLong("askFeedbackMaxAttempt").toString());
                GenericValue orderAttribute = delegator.findOne("OrderAttribute", UtilMisc.toMap("orderId", orderId, "attrName", "ASK_FEEDBACK_SENT"), false);
                feedbackSent = Integer.parseInt(orderAttribute.getString("attrValue"));
                Timestamp lastUpdatedStamp = orderAttribute.getTimestamp("lastUpdatedStamp");
                Date lastUpdatedDate = new Date(lastUpdatedStamp.getTime());
                Long dayDiff = bellyannaService.dayDifference(lastUpdatedDate);
                boolean askFeedback = false;
                
                if(feedbackSent < askFeedbackMaxAttempt) { //if feedbackSent is within MaxAttempt -- START
                    if (dayDiff >= daysAskFeedback) {  //if send feedback reminder after dayDiff -- START
                        emailTemplateName = "Asking for feedback (Shipped) - Reminder";
                    }   //if send feedback reminder after dayDiff -- END
                    
                    GenericValue ebayEmailTemplate = EntityUtil.getFirst(delegator.findByAnd("EbayEmailTemplate", UtilMisc.toMap("emailTemplateName", emailTemplateName), null, false));
                    GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                    GenericValue productStoreRole = EntityUtil.getFirst(delegator.findByAnd("ProductStoreRole", UtilMisc.toMap("productStoreId", productStore.getString("productStoreId"), "roleTypeId", "EBAY_ACCOUNT"), null, false));
                    GenericValue partyGroup = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", productStoreRole.getString("partyId")), false);
                    String sellerUserId = partyGroup.getString("groupName");
                    GenericValue ocm = EntityUtil.getFirst(delegator.findByAnd("OrderContactMech", UtilMisc.toMap("orderId", orderId, "contactMechPurposeTypeId", "EBAY_USER_ID"), null, false));
                    String ebayUserId = delegator.findOne("ContactMech", UtilMisc.toMap("contactMechId", ocm.getString("contactMechId")), false).getString("infoString");
                    String ebayItemId = delegator.findOne("OrderItemAttribute", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", "00001", "attrName", "eBay Item Number"), false).getString("attrValue");
                    
                    try {   //try building message -- START
                        //building subject part -- START
                        String subjectTemplate = ebayEmailTemplate.getString("subject");
                        VelocityContext subjectVC = new VelocityContext();
                        subjectVC.put("sellerUserId", sellerUserId);
                        StringWriter subjectBody = new StringWriter();
                        Velocity.evaluate(subjectVC, subjectBody, "subjectTemplate", subjectTemplate);
                        //building subject part -- END
                        
                        //Building message body -- START
                        Velocity.init();
                        
                        String mainTemplate = ebayEmailTemplate.getString("content");   //building main part -- START
                        VelocityContext mainVC = new VelocityContext();
                        mainVC.put("sellerUserId", sellerUserId);
                        mainVC.put("ebayItemId", ebayItemId);
                        StringWriter contentBody = new StringWriter();
                        Velocity.evaluate(mainVC, contentBody, "mainTemplate", mainTemplate);   //building main part -- END
                        //Building message body -- END
                        
                        try {   //send message to customer via eBay message -- START
                            Map sendMessage = dispatcher.runSync(
                                                                 "TradingApiAddMemberMessageAAQToPartnerRequest",
                                                                 UtilMisc.toMap(
                                                                                "productStoreId", productStoreId,
                                                                                "eBayItemId", ebayItemId,
                                                                                "eBayUserId", ebayUserId,
                                                                                "questionType", "General",
                                                                                "messageSubject", subjectBody.toString(),
                                                                                "messageBody", contentBody.toString(),
                                                                                "userLogin", userLogin
                                                                                )
                                                                 );
                            if (ServiceUtil.isFailure(sendMessage)) {
                                Debug.logError("Error running TradingApiAddMemberMessageAAQToPartnerRequest for order ID " + orderId, module);
                            } else {
                                GenericValue orderAttributeGV = delegator.makeValue("OrderAttribute", UtilMisc.toMap("orderId", orderId, "attrName", "ASK_FEEDBACK_SENT"));
                                orderAttributeGV.set("attrValue", (feedbackSent + 1) + "");
                                delegator.createOrStore(orderAttributeGV);
                            }
                        }   //send message to customer via eBay message -- END
                        catch (GenericServiceException e) {
                            Debug.logError(e, module);
                            return ServiceUtil.returnError(e.getMessage());
                        }
                    }   //try building message -- END
                    catch (Exception e) {
                        e.printStackTrace();
                        return ServiceUtil.returnError(e.getMessage());
                    }
                    
                }   //if feedbackSent is within MaxAttempt -- END
            }   //loop sprs -- END
            sprsELI.close();
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        return result;
        
    }   //sendEpacketFeedbackMessage
    
    public static Map<String, Object> updateOrderAttribute (DispatchContext dctx, Map context)
    throws GenericEntityException { //updateOrderAttribute
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        
        String orderId = (String) context.get("orderId");
        String attrName = (String) context.get("attrName");
        String attrValue = (String) context.get("attrValue");
        
        try {   //main try -- START
            GenericValue orderAttribute = delegator.findOne("OrderAttribute", UtilMisc.toMap("orderId", orderId, "attrName", attrName), false);
            if(UtilValidate.isEmpty(orderAttribute)) {  //if orderAttribute isEmpty -- START
                return ServiceUtil.returnError("Order ID " + orderId + " does not have attrName " + attrName + " in Order Attribute");
            }   //if orderAttribute isEmpty -- END
            else {  //if orderAttribute isNotEmpty -- START
                orderAttribute.set("attrValue", attrValue);
                delegator.store(orderAttribute);
            }   //if orderAttribute isNotEmpty -- END
        }   //main try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
        }
        return ServiceUtil.returnSuccess();
    }   //updateOrderAttribute
    
    public static Map<String, Object> updateEbayOrderFeedback (DispatchContext dctx, Map context)
	throws IOException, GenericEntityException, GenericServiceException {
        
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        int lastXDays = (Integer) context.get("lastXDays");
        int toLastXDays = (Integer)  context.get("toLastXDays");
        String productStoreId = (String) context.get("productStoreId");
        
        Calendar fromDay = Calendar.getInstance();
        Calendar toDay = Calendar.getInstance();
        fromDay.set(Calendar.DATE, fromDay.get(Calendar.DATE) - lastXDays);
        toDay.set(Calendar.DATE, toDay.get(Calendar.DATE) - toLastXDays);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
        //Date resultDate = fromDay.getTime();
        //Debug.logError("FromDay is " + sdf.format(fromDay.getTime()).toString(), module);
        //Debug.logError("ToDay is " + sdf.format(toDay.getTime()).toString(), module);
        Timestamp fromDate = Timestamp.valueOf(sdf.format(fromDay.getTime()));
        Timestamp toDate = Timestamp.valueOf(sdf.format(toDay.getTime()));
        
        try {
            EntityCondition condition = null;
            if (UtilValidate.isEmpty(productStoreId)) {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("statusDatetime",EntityOperator.GREATER_THAN ,fromDate),
                                                                          EntityCondition.makeCondition("statusDatetime",EntityOperator.LESS_THAN ,toDate)
                                                                          ));
            } else {
                condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                          EntityCondition.makeCondition("statusDatetime",EntityOperator.GREATER_THAN ,fromDate),
                                                                          EntityCondition.makeCondition("statusDatetime",EntityOperator.LESS_THAN ,toDate),
                                                                          EntityCondition.makeCondition("productStoreId",EntityOperator.EQUALS , productStoreId)
                                                                          ));
            }
            
            List<GenericValue> listViewEntityIt = delegator.findList("OrderHeaderFeedback", condition, null, null, null, false);
            //GenericValue listViewEntityItem = null;
            //int count = 0;
            for (GenericValue listViewEntityItem : listViewEntityIt) {    // While listViewEntityItem is not null -- START
                //Convert entity date to TimeStamp -- START
                /*Object statusDateTime = null;
                 try {
                 statusDateTime = ObjectType.simpleTypeConvert(listViewEntityItem.get("statusDatetime"), "Timestamp", null, null);
                 } catch (GeneralException e) {
                 Debug.logError(e.getMessage(), module);
                 }*/
                //Convert entity date to TimeStamp -- END
                if (listViewEntityItem.get("orderId") != null) {    // If listViewEntityItem.orderId is not null -- START
                    String orderId = listViewEntityItem.get("orderId").toString();
                    Debug.logError("Updating Feedback for order ID " + listViewEntityItem.get("orderId"), module);
                    //count++;
                    Map tradingApiGetFeedbackResult = dispatcher.runSync("updateEbayOrderItemFeedback", UtilMisc.toMap("orderId", orderId, "userLogin", userLogin));
					if (ServiceUtil.isFailure(tradingApiGetFeedbackResult)) {
	            		Debug.logError("Error running TradingApiGetFeedback for order ID " + orderId, module);
	            	} else {	//	update orderAttribute with FEEDBACK_RECEIVED = "Y" -- START
						List<GenericValue> orderItemAttributes = delegator.findByAnd("OrderItemAttribute", UtilMisc.toMap("orderId", orderId, "attrName", "FEEDBACK_RECEIVED"), null, false);
						for (GenericValue orderItemAttribute : orderItemAttributes) {	// Loop for each orderItemAttributes -- START
							if("Y".equals(orderItemAttribute.getString("attrValue"))) {	//if orderItemAttribute attrValue = "Y" -- START
								GenericValue orderAttribute = delegator.makeValue("OrderAttribute", UtilMisc.toMap("orderId", orderId, "attrName", "FEEDBACK_RECEIVED"));
								orderAttribute.set("attrValue", "Y");
								delegator.createOrStore(orderAttribute);
							}	//if orderItemAttribute attrValue = "Y" -- END
						}	// Loop for each orderItemAttributes -- END
					}	//	update orderAttribute with FEEDBACK_RECEIVED = "Y" -- END
                    //checking if orderAttribute.feedbackReceived is "Y" -- START
                    //GenericValue orderAttributeFeedback = delegator.findByPrimaryKey("OrderAttribute", UtilMisc.toMap("orderId", orderId, "attrName", "FEEDBACK_RECEIVED"));
                    //if ("N".equals(orderAttributeFeedback.getString("attrValue"))) {    //  send message to customer via eBay API -- START
                    
                    //}   //  send message to customer via eBay API -- END
                    //checking if orderAttribute.feedbackReceived is "Y" -- END
                }   // If listViewEntityItem.orderId is not null -- END
            }   // While listViewEntityItem is not null -- END
            //listViewEntityIt.close();
            //Debug.logError("total order is " + count, module);
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
        }
        return ServiceUtil.returnSuccess();
    }   //updateEbayOrderFeedback

    
    public static Map<String, Object> updateEbayOrderItemFeedback(DispatchContext dctx, Map context)
	throws GenericEntityException, IOException {
        
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        String orderId = (String) context.get("orderId");
        Map mapAccount = FastMap.newInstance();
        
        
        try {
            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            String productStoreId = orderHeader.getString("productStoreId");
            
            if ((orderHeader.getString("salesChannelEnumId")).equals("EBAY_SALES_CHANNEL") && !orderId.matches(".*R")) { //if salesChannelEnumId = EBAY_SALES_CHANNEL -- START
                //get customer eBayUserId -- START
                GenericValue orderContactMech = EntityUtil.getFirst(delegator.findByAnd("OrderContactMech", UtilMisc.toMap("orderId", orderId, "contactMechPurposeTypeId", "EBAY_USER_ID"), null, false));
                String eBayUserId = delegator.findOne("ContactMech", UtilMisc.toMap("contactMechId", orderContactMech.getString("contactMechId")), false).getString("infoString");
                //get customer eBayUserId -- END
                
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                mapAccount = common.accountInfo(delegator, productStore);
                mapAccount.put("callName", "GetFeedback");
                mapAccount.put("eBayUserId",eBayUserId);
                
                Calendar now = Calendar.getInstance();
                String today = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
                
                List<GenericValue> orderItems = delegator.findByAnd("OrderItem", UtilMisc.toMap("orderId", orderId), null, false);
                //Debug.logError("OrderHeader is " + orderHeader, module);
                //Debug.logError("orderItems is " + orderItems, module);
                for (GenericValue orderItem : orderItems) {	// loop orderItems -- START
                    String orderItemSeqId = orderItem.getString("orderItemSeqId");
                    boolean oiaIsFeedbackReceived = false;
                    List<GenericValue> orderItemAttributes = delegator.findByAnd("OrderItemAttribute", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId), null, false);
                    for (GenericValue orderItemAttribute : orderItemAttributes) {	// loop orderItemAttributes -- START
                        //Debug.logError("OA " + orderItemAttribute.getString("attrValue"), module);
                        if("EBAY_TRAN_ID".equals(orderItemAttribute.getString("attrName"))) {
                            mapAccount.put("eBayTransactionId",orderItemAttribute.getString("attrValue"));
                        } else if ("eBay Item Number".equals(orderItemAttribute.getString("attrName"))) {
                            mapAccount.put("eBayItemId",orderItemAttribute.getString("attrValue"));
                        } else if ("FEEDBACK_RECEIVED".equals(orderItemAttribute.getString("attrName"))) {
                            if (orderItemAttribute.getString("attrValue").equals("Y")) {
                                oiaIsFeedbackReceived = true;
                            }
                        }
                    }	// loop orderItemAttributes -- END
                    
                    if (!oiaIsFeedbackReceived) {   //if order item attribute FEEDBACK_RECEIVED is N -- START
                        String requestXMLcode = requestXML.getFeedbackRequestXML(mapAccount);
                        //Debug.logError("Sending getFeedbackRequestXML for eBayUserID " + mapAccount.get("eBayUserId") + ", eBayTransactionID " + mapAccount.get("eBayTransactionId") + ", eBayItemID " + mapAccount.get("eBayItemId"), module);
                        //Debug.logError(requestXMLcode, module);
                        String responseXML = eBayTradingAPI.sendRequestXMLToEbay(mapAccount, requestXMLcode.toString());
                        //Debug.logError(responseXML, module);
                        
                        
                        try {   //try Reading ResponseXML -- START
                            Document docResponse = UtilXml.readXmlDocument(responseXML, true);
                            Element elemResponse = docResponse.getDocumentElement();
                            String ack = UtilXml.childElementValue(elemResponse, "Ack", "Failure");
                            StringBuffer errorMessage = new StringBuffer();
                            
                            List<? extends Element> errorElements = UtilXml.childElementList(elemResponse, "Errors");
                            Iterator<? extends Element> errorElementsElemIter = errorElements.iterator();
                            while (errorElementsElemIter.hasNext()) {
                                Element errorElement = errorElementsElemIter.next();
                                String shortMessage = UtilXml.childElementValue(errorElement, "ShortMessage", null);
                                String longMessage = UtilXml.childElementValue(errorElement, "LongMessage", null);
                                errorMessage.append(shortMessage + " - " + longMessage);
                                FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/LogError/ebay/GetFeedbackError.log", true);
                                f1.write(today + ": productStoreId " + productStoreId + ", eBayUserId " + eBayUserId + ": " + errorMessage + "\n");
                                f1.close();
                            }
                            
                            if (ack != null) { //if ack is not null -- START
                                GenericValue orderItemAttributeFeedback = delegator.makeValue("OrderItemAttribute", UtilMisc.toMap("orderId", orderId, "orderItemSeqId", orderItemSeqId));
                                orderItemAttributeFeedback.set("attrName", "FEEDBACK_RECEIVED");
                                if (eBayTradingAPI.isFeedbackReceived(delegator, mapAccount, responseXML)) {
                                    orderItemAttributeFeedback.set("attrValue", "Y");
                                } else {
                                    orderItemAttributeFeedback.set("attrValue", "N");
                                }
                                delegator.createOrStore(orderItemAttributeFeedback);
                            }   //if ack is not null -- END
                        }   //try Reading ResponseXML -- END
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }   //if order item attribute FEEDBACK_RECEIVED is N -- END
                }	// loop for orderItems -- END
            }   //if salesChannelEnumId = EBAY_SALES_CHANNEL -- END
        } catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return ServiceUtil.returnSuccess();
    }   //updateEbayOrderItemFeedback

    
}	//END class
