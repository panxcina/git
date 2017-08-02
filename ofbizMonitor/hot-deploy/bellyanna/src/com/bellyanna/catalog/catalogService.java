package com.bellyanna.catalog;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
import java.lang.Thread;
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
import jxl.*;
import jxl.read.biff.BiffException;
import org.jsoup.*;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.ofbiz.base.util.UtilDateTime;
import org.ofbiz.base.util.UtilMisc;
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
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.service.ServiceAuthException;
import org.ofbiz.service.ServiceUtil;
import org.ofbiz.service.ServiceValidationException;
import org.ofbiz.service.ModelService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.commons.lang.StringUtils;

public class catalogService {
	private static final String module = catalogService.class.getName();
    private static final String eol = System.getProperty("line.separator");
	
public static Map<String, Object> catalogTest (DispatchContext dctx, Map context)
	throws GenericEntityException, GenericServiceException {
	
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String facilityId = (String) context.get("facilityId");
        
        String orderId = null;
        String productId = null;
        String priority = null;
        BigDecimal zero = BigDecimal.ZERO;
        List<String> productIdCheckList = new LinkedList<String>();
        
        //check inventory for backordered but has positive ATP -- START
        EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                  EntityCondition.makeCondition("quantityNotAvailable",EntityOperator.GREATER_THAN ,zero),
                                                                                  EntityCondition.makeCondition("facilityId",EntityOperator.EQUALS ,facilityId)
                                                                                  ),
                                                                  EntityOperator.AND);
        List<GenericValue> oisgirs = delegator.findList("OrderItemShipGrpInvResAndItem", condition, null, null, null, false);
        
        for (GenericValue oisgir : oisgirs) {   //loop oisgir -- START
            orderId = oisgir.getString("orderId");
            productId = oisgir.getString("productId");
            priority = oisgir.getString("priority");
            
            try {   //run inventoryCheck -- START
                Map<String, Object> inventoryCheck = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("facilityId", facilityId, "productId", productId, "userLogin", userLogin));
                BigDecimal atpBD = (BigDecimal)inventoryCheck.get("availableToPromiseTotal");
                int atp = Integer.valueOf(atpBD.intValue());
                if (atp > 0) {  //if atp is positive -- START
                    Map<String, Object> fixReservation = dispatcher.runSync("setOrderReservationPriority", UtilMisc.toMap("orderId", orderId, "priority", priority, "userLogin", userLogin));
                }   //if atp is positive -- END
            }   //run inventoryCheck -- END
            catch (GenericServiceException e) {
                e.printStackTrace();
                return ServiceUtil.returnError(e.getMessage());
            }
        }   //loop oisgir -- END
        //check inventory for backordered but has positive ATP -- END
        
        
        //Check Inventory Reservations -- START
        //List<GenericValue> oisgirs2 = delegator.findList("OrderItemShipGrpInvResAndItem", condition, UtilMisc.toList("productId"), UtilMisc.toList("productId"), new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
        try {   //try -- START
            DynamicViewEntity oisgirs2DynamicView = new DynamicViewEntity();
            // Construct a dynamic view entity
            oisgirs2DynamicView.addMemberEntity("oisgir", "OrderItemShipGrpInvResAndItem");
            oisgirs2DynamicView.addAlias("oisgir", "productId");
            oisgirs2DynamicView.addAlias("oisgir", "facilityId");
            EntityCondition condition2 = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                      //EntityCondition.makeCondition("quantityNotAvailable",EntityOperator.GREATER_THAN ,zero),
                                                                                      EntityCondition.makeCondition("facilityId",EntityOperator.EQUALS ,facilityId)
                                                                                      )
                                                                      );
            EntityListIterator oisgirs2 = delegator.findListIteratorByCondition(oisgirs2DynamicView,
                                                                                condition2,
                                                                                null,
                                                                                UtilMisc.toList("productId"),
                                                                                UtilMisc.toList("productId"),
                                                                                new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true));
            
            GenericValue oisgir2 = null;
            while ((oisgir2 = oisgirs2.next()) != null) {   //while loop oisgir2 -- START
                productId = oisgir2.getString("productId");
                List<GenericValue> oisgirProductIds = delegator.findByAnd("OrderItemShipGrpInvResAndItem", UtilMisc.toMap("productId", productId), null, false);
                //Debug.logError("ProductId is " + productId, module);
                BigDecimal oisgirReserve = BigDecimal.ZERO;
                BigDecimal oisgirOsa = BigDecimal.ZERO;
                int oisgirReserveTotal = 0;
                int oisgirOsaTotal = 0;
                for (GenericValue oisgirProductId : oisgirProductIds) { //loop oisgirProductId -- START
                    //Debug.logError("test", module);
                    oisgirReserve = (BigDecimal) oisgirProductId.getBigDecimal("quantity");
                    oisgirOsa = (BigDecimal) oisgirProductId.getBigDecimal("quantityNotAvailable");
                    if (oisgirOsa == null) {
                        oisgirOsa = BigDecimal.ZERO;
                    }
                    //Debug.logError("For ProductID: " + productId + " and orderID: " + oisgirProductId.getString("orderId") + " with res qty = " + oisgirReserve, module);
                    oisgirReserveTotal += oisgirReserve.intValue();
                    oisgirOsaTotal += oisgirOsa.intValue();
                }   //loop oisgirProductId -- END
                //Debug.logError("ProductID is " + productId + " and oisgirReserveTotal is " + oisgirReserveTotal + ", oisgriOSA is " + oisgirOsaTotal, module);
                
                
                Map<String, Object> inventoryCheck2 = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("facilityId", facilityId, "productId", productId, "userLogin", userLogin));
                BigDecimal atpBD2 = (BigDecimal)inventoryCheck2.get("availableToPromiseTotal");
                BigDecimal qohBD = (BigDecimal)inventoryCheck2.get("quantityOnHandTotal");
                int atp2 = Integer.valueOf(atpBD2.intValue());
                int qoh = Integer.valueOf(qohBD.intValue());
                
                //Debug.logError("ProductId: " + productId + ", oisgirReserveTotal: " + oisgirReserveTotal + " and QOH - ATP ( " + qoh + " - " + atp2 + " = " + (qoh - atp2) + "(" + oisgirReserveTotal + ")", module);
                //Debug.logError("oisgirOsa: " + oisgirOsaTotal + " atp2: " + atp2, module);
                
                if ((qoh - atp2) != oisgirReserveTotal) {   //if qoh-atp2 is not equal to oisgirReserveTotal -- START
                    productIdCheckList.add(productId);
                }   //if qoh-atp2 is not equal to oisgirReserveTotal -- END
                
                if (oisgirOsaTotal > 0) {   //if oisgirOsaTotal > 0 -- START
                    if ((atp2 * (-1)) != oisgirOsaTotal) {  //if oisgirOsaTotal is not the same with atp2 -- START
                        productIdCheckList.add(productId);
                    }   //if oisgirOsaTotal is not the same with atp2 -- START
                }   //if oisgirOsaTotal > 0 -- END
            
            }   //while loop oisgir2 -- END
            oisgirs2.close();
            
            List<String> productIdCheckListDistinct = new LinkedList<String>();
            for (String temp : productIdCheckList) {    //get distinct productIdCheckList -- START
                if (!productIdCheckListDistinct.contains(temp)) {
                    productIdCheckListDistinct.add(temp);
                }
            }//get distinct productIdCheckList -- END
            
            Debug.logError("ProductIdCheckListDistinct is: " + productIdCheckListDistinct, module);
            for (String productIdCheck : productIdCheckListDistinct) {  //loop productIdCheck -- START
                List<GenericValue> oisgirProductIds = delegator.findByAnd("OrderItemShipGrpInvResAndItem", UtilMisc.toMap("productId", productIdCheck), null, false);
                GenericValue orderIdCheck = EntityUtil.getFirst(oisgirProductIds);
                try {   //run inventoryCheck2 -- START
                    Map<String, Object> fixReservation2 = dispatcher.runSync("setOrderReservationPriority", UtilMisc.toMap("orderId", orderIdCheck.getString("orderId"), "priority", orderIdCheck.getString("priority"), "userLogin", userLogin));
                }   //run inventoryCheck2 -- END
                catch (GenericServiceException e) {
                    e.printStackTrace();
                    return ServiceUtil.returnError(e.getMessage());
                }   
            }   //loop productIdCheck -- END
            
        }   //try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        /*finally {
            Debug.logError("test", module);
            try {
                oisgirs2.close();
            }
            catch (GenericEntityException e) {
                e.printStackTrace();
                return ServiceUtil.returnError(e.getMessage());
            }
            Debug.logError("test2", module);
            //return ServiceUtil.returnSuccess();
        }*/
        

        return ServiceUtil.returnSuccess();
}
   
    public static Map<String, Object> fixInventoryReservation (DispatchContext dctx, Map context)
	throws GenericEntityException, GenericServiceException {
        
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String facilityId = (String) context.get("facilityId");
        
        String orderId = null;
        String productId = null;
        String priority = null;
        BigDecimal zero = BigDecimal.ZERO;
        List<String> productIdCheckList = new LinkedList<String>();
        
        //check inventory for backordered but has positive ATP -- START
        EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                  EntityCondition.makeCondition("quantityNotAvailable",EntityOperator.GREATER_THAN ,zero),
                                                                                  EntityCondition.makeCondition("facilityId",EntityOperator.EQUALS ,facilityId)
                                                                                  ),
                                                                  EntityOperator.AND);
        List<GenericValue> oisgirs = delegator.findList("OrderItemShipGrpInvResAndItem", condition, null, null, null, false);
        
        for (GenericValue oisgir : oisgirs) {   //loop oisgir -- START
            orderId = oisgir.getString("orderId");
            productId = oisgir.getString("productId");
            priority = oisgir.getString("priority");
            
            try {   //run inventoryCheck -- START
                Map<String, Object> inventoryCheck = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("facilityId", facilityId, "productId", productId, "userLogin", userLogin));
                BigDecimal atpBD = (BigDecimal)inventoryCheck.get("availableToPromiseTotal");
                int atp = Integer.valueOf(atpBD.intValue());
                if (atp > 0) {  //if atp is positive -- START
                    Map<String, Object> fixReservation = dispatcher.runSync("setOrderReservationPriority", UtilMisc.toMap("orderId", orderId, "priority", priority, "userLogin", userLogin));
                }   //if atp is positive -- END
            }   //run inventoryCheck -- END
            catch (GenericServiceException e) {
                e.printStackTrace();
                return ServiceUtil.returnError(e.getMessage());
            }
        }   //loop oisgir -- END
        //check inventory for backordered but has positive ATP -- END
        
        
        //Check Inventory Reservations -- START
        //List<GenericValue> oisgirs2 = delegator.findList("OrderItemShipGrpInvResAndItem", condition, UtilMisc.toList("productId"), UtilMisc.toList("productId"), new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
        try {   //try -- START
            DynamicViewEntity oisgirs2DynamicView = new DynamicViewEntity();
            // Construct a dynamic view entity
            oisgirs2DynamicView.addMemberEntity("oisgir", "OrderItemShipGrpInvResAndItem");
            oisgirs2DynamicView.addAlias("oisgir", "productId");
            oisgirs2DynamicView.addAlias("oisgir", "facilityId");
            EntityCondition condition2 = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                       //EntityCondition.makeCondition("quantityNotAvailable",EntityOperator.GREATER_THAN ,zero),
                                                                                       EntityCondition.makeCondition("facilityId",EntityOperator.EQUALS ,facilityId)
                                                                                       )
                                                                       );
            //get Distinct EntityLustIterator
            EntityListIterator oisgirs2 = delegator.findListIteratorByCondition(oisgirs2DynamicView,
                                                                                condition2,
                                                                                null,
                                                                                UtilMisc.toList("productId"),
                                                                                UtilMisc.toList("productId"),
                                                                                new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true));
            
            GenericValue oisgir2 = null;
            while ((oisgir2 = oisgirs2.next()) != null) {   //while loop oisgir2 -- START
                productId = oisgir2.getString("productId");
                List<GenericValue> oisgirProductIds = delegator.findByAnd("OrderItemShipGrpInvResAndItem", UtilMisc.toMap("productId", productId), null, false);
                //Debug.logError("ProductId is " + productId, module);
                BigDecimal oisgirReserve = BigDecimal.ZERO;
                BigDecimal oisgirOsa = BigDecimal.ZERO;
                int oisgirReserveTotal = 0;
                int oisgirOsaTotal = 0;
                for (GenericValue oisgirProductId : oisgirProductIds) { //loop oisgirProductId -- START
                    //Debug.logError("test", module);
                    oisgirReserve = (BigDecimal) oisgirProductId.getBigDecimal("quantity");
                    oisgirOsa = (BigDecimal) oisgirProductId.getBigDecimal("quantityNotAvailable");
                    if (oisgirOsa == null) {
                        oisgirOsa = BigDecimal.ZERO;
                    }
                    //Debug.logError("For ProductID: " + productId + " and orderID: " + oisgirProductId.getString("orderId") + " with res qty = " + oisgirReserve, module);
                    oisgirReserveTotal += oisgirReserve.intValue();
                    oisgirOsaTotal += oisgirOsa.intValue();
                }   //loop oisgirProductId -- END
                //Debug.logError("ProductID is " + productId + " and oisgirReserveTotal is " + oisgirReserveTotal + ", oisgriOSA is " + oisgirOsaTotal, module);
                
                
                Map<String, Object> inventoryCheck2 = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("facilityId", facilityId, "productId", productId, "userLogin", userLogin));
                BigDecimal atpBD2 = (BigDecimal)inventoryCheck2.get("availableToPromiseTotal");
                BigDecimal qohBD = (BigDecimal)inventoryCheck2.get("quantityOnHandTotal");
                int atp2 = Integer.valueOf(atpBD2.intValue());
                int qoh = Integer.valueOf(qohBD.intValue());
                
                //Debug.logError("ProductId: " + productId + ", oisgirReserveTotal: " + oisgirReserveTotal + " and QOH - ATP ( " + qoh + " - " + atp2 + " = " + (qoh - atp2) + "(" + oisgirReserveTotal + ")", module);
                //Debug.logError("oisgirOsa: " + oisgirOsaTotal + " atp2: " + atp2, module);
                
                if ((qoh - atp2) != oisgirReserveTotal) {   //if qoh-atp2 is not equal to oisgirReserveTotal -- START
                    productIdCheckList.add(productId);
                }   //if qoh-atp2 is not equal to oisgirReserveTotal -- END
                
                if (oisgirOsaTotal > 0) {   //if oisgirOsaTotal > 0 -- START
                    if ((atp2 * (-1)) != oisgirOsaTotal) {  //if oisgirOsaTotal is not the same with atp2 -- START
                        productIdCheckList.add(productId);
                    }   //if oisgirOsaTotal is not the same with atp2 -- START
                }   //if oisgirOsaTotal > 0 -- END
                
            }   //while loop oisgir2 -- END
            oisgirs2.close();
            
            List<String> productIdCheckListDistinct = new LinkedList<String>();
            for (String temp : productIdCheckList) {    //get distinct productIdCheckList -- START
                if (!productIdCheckListDistinct.contains(temp)) {
                    productIdCheckListDistinct.add(temp);
                }
            }//get distinct productIdCheckList -- END
            
            Debug.logError("ProductIdCheckListDistinct is: " + productIdCheckListDistinct, module);
            for (String productIdCheck : productIdCheckListDistinct) {  //loop productIdCheck -- START
                List<GenericValue> oisgirProductIds = delegator.findByAnd("OrderItemShipGrpInvResAndItem", UtilMisc.toMap("productId", productIdCheck), null, false);
                GenericValue orderIdCheck = EntityUtil.getFirst(oisgirProductIds);
                try {   //run inventoryCheck2 -- START
                    Map<String, Object> fixReservation2 = dispatcher.runSync("setOrderReservationPriority", UtilMisc.toMap("orderId", orderIdCheck.getString("orderId"), "priority", orderIdCheck.getString("priority"), "userLogin", userLogin));
                }   //run inventoryCheck2 -- END
                catch (GenericServiceException e) {
                    e.printStackTrace();
                    return ServiceUtil.returnError(e.getMessage());
                }
            }   //loop productIdCheck -- END
            
        }   //try -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        /*finally {
         Debug.logError("test", module);
         try {
         oisgirs2.close();
         }
         catch (GenericEntityException e) {
         e.printStackTrace();
         return ServiceUtil.returnError(e.getMessage());
         }
         Debug.logError("test2", module);
         //return ServiceUtil.returnSuccess();
         }*/
        
        
        return ServiceUtil.returnSuccess();
    }
    
    public static Map<String, Object> updateProductPictureExternalBulk (DispatchContext dctx, Map context)
	throws GenericServiceException, GenericEntityException, IOException {
        
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String removeExisting = (String) context.get("removeExisting");
        if (removeExisting == null) {
            removeExisting = "N";
        }
        int countProduct = 1;
        
        DynamicViewEntity productDve = new DynamicViewEntity();
        productDve.addMemberEntity("product", "Product");
        productDve.addAlias("product", "productId");
        productDve.addAlias("product", "salesDiscontinuationDate");
        EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                   //EntityCondition.makeCondition("quantityNotAvailable",EntityOperator.GREATER_THAN ,zero),
                                                                                   EntityCondition.makeCondition("salesDiscontinuationDate",EntityOperator.EQUALS ,null)
                                                                                   )
                                                                   );

        EntityListIterator productsELI = delegator.findListIteratorByCondition(productDve, condition, null, UtilMisc.toList("productId"), UtilMisc.toList("productId"), null);
        
        DynamicViewEntity productPictureDve = new DynamicViewEntity();
        productPictureDve.addMemberEntity("productPicture", "ProductPictureExternal");
        productPictureDve.addAlias("productPicture", "productId");
        EntityCondition condition2 = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                  //EntityCondition.makeCondition("quantityNotAvailable",EntityOperator.GREATER_THAN ,zero),
                                                                                  EntityCondition.makeCondition("productId",EntityOperator.NOT_EQUAL ,null)
                                                                                  )
                                                                  );
        
        EntityListIterator productPicturesELI = delegator.findListIteratorByCondition(productPictureDve, condition2, null, UtilMisc.toList("productId"), null, new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true));
        
                
        GenericValue productELI = null;
        GenericValue productPictureELI = null;
        List<String> activeProductId = new ArrayList<String>();
        //int count = 0;
        while ((productELI = productsELI.next()) != null) { //loop productsELI -- START
            //count++;
            activeProductId.add(productELI.getString("productId"));
        }   //loop productsELI -- END
        //Debug.logError("check this " + productPicturesELI.size(), module);
        
        //if (productPicturesELI != null) {   //if productPicturesELI is not null -- START
            List<String> productPictureProductId = new ArrayList<String>();
            while ((productPictureELI = productPicturesELI.next()) != null) {   //loop productPicturesELI -- START
                productPictureProductId.add(productPictureELI.getString("productId"));
            }   //loop productPicturesELI -- END
            activeProductId.removeAll(productPictureProductId);
        //}   //if productPicturesELI is not null -- END
        
        productsELI.close();
        productPicturesELI.close();
        
        //Running the updateProductPictureExternal -- START
        //writing errorProductId to file -- START
        try {   //try writing errorProductId to file -- START
            File f = new File ("hot-deploy/bellyanna/webapp/bellyanna/eBayResponseXML/errorProductId.txt");
            if(f.exists() && f.isFile()){
                f.delete();
            }
            FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/eBayResponseXML/errorProductId.txt", true);
            
            for (String productId : activeProductId) {  //loop activeProductId -- START
                Debug.logError("Processing productId " + productId + "...(" + countProduct + ")", module);
                Map<String, Object> updateProductPictureExternalSingle = dispatcher.runSync("updateProductPictureExternalSingle", UtilMisc.toMap("productId", productId, "userLogin", userLogin, "removeExisting", removeExisting));
                Debug.logError("result is " + updateProductPictureExternalSingle, module);
                if (ServiceUtil.isSuccess(updateProductPictureExternalSingle)) {    //if service success -- START
                    if (updateProductPictureExternalSingle.get("successMessage").equals("URL does not exist")) {    //if URL does not exist -- START
                        f1.write(productId + "\n");
                    }   //if URL does not exist -- END
                    //Debug.logError("result is " + updateProductPictureExternalSingle.get("successMessage"), module);
                }   //if service success -- END
                if (countProduct % 100 == 0) {  //mod 100 -- START
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }   //mod 100 -- END
                countProduct++;
            }   //loop activeProductId -- END
            
            f1.close();
        }   //try writing errorProductId to file -- END
        catch (IOException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        //writing errorProductId to file -- END
        
        //Running the updateProductPictureExternal -- END
        
        return result;
        
    }
    
    public static Map<String, Object> updateProductPictureExternal (DispatchContext dctx, Map context)
	throws GenericServiceException, GenericEntityException, IOException {   //updateProductPictureExternal
        
        Map<String, Object> result = ServiceUtil.returnSuccess("Success");
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productId = (String) context.get("productId");
        String removeExisting = (String) context.get("removeExisting");
        
        GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
        
        if (UtilValidate.isNotEmpty(product)) { //if productId exists in ofbiz -- START
            if (UtilValidate.isNotEmpty(removeExisting) && removeExisting.toUpperCase().equals("Y")) {
                Map<String, Object> removeProductPictureExternal = dispatcher.runSync("removeProductPictureExternal", UtilMisc.toMap("productId", productId, "userLogin", userLogin));
            }
            
            DecimalFormat df = new DecimalFormat("00000");
            String parentSku = null;
            String childSku = null;
            int count = StringUtils.countMatches(productId, "-");
            
            if (count == 3) {
                childSku = "/" + productId.substring(0, productId.indexOf("-", productId.indexOf("-", productId.indexOf("-") + 1) + 1)) + "/";
                parentSku = productId.substring(0, productId.indexOf("-", productId.indexOf("-") + 1));
            }
            else if (count == 2) {
                childSku = "/" + productId + "/";
                parentSku = productId.substring(0, productId.indexOf("-", productId.indexOf("-") + 1));
            }
            else if (count == 1) {
                parentSku = productId;
                childSku = "/";
            }
            
            try {   //try -- START
                org.jsoup.Connection.Response res = Jsoup.connect("http://images.bellyanna.com" + "/" + parentSku + childSku).timeout(100000).ignoreHttpErrors(true).followRedirects(true).execute();
                //Debug.logError("Response code is " + res.statusCode(), module);
                if (res.statusCode() == 200) { //if URL exist -- START
                    Debug.logError("Updating ProductPictureExternal " + productId + ": parentSku is " + parentSku + " and childSku is " + childSku + " and count of \"-\" is " + count, module);
                    org.jsoup.nodes.Document doc = Jsoup.connect("http://images.bellyanna.com" + "/" + parentSku + childSku).timeout(100000).ignoreHttpErrors(true).get();
                    //Debug.logError("Html" + doc, module);
                    
                    org.jsoup.nodes.Element ul = doc.select("ul").first();
                    //Debug.logError("Ul is " + ul, module);
                    //Debug.logError("UL childnodes: " + ul.childNodes(), module);
                    int standardCount = 1;
                    int sizeCount = 1;
                    int galleryCount = 1;
                    int descCount = 1;
                    
                    List<org.jsoup.nodes.Node> ulChildNodes = ul.childNodes();
                    for ( org.jsoup.nodes.Node ulChildNode : ulChildNodes) {    //loop ulChildNodes -- START
                        //Debug.logError("ulChildNode: " + ulChildNode.toString(), module);
                        //Debug.logError("ulChildNode size is : " + ulChildNode.toString().length(), module);
                        
                        if (ulChildNode.toString().length() > 1) {  //if ulChildNode string is more than 1 -- START
                            String[] items = ulChildNode.toString().split("\"");
                            String filename = items[1];
                            
                            if(filename.toLowerCase().contains(".jpg")) {     //if filename contains JPG -- START
                                //Debug.logError("test check this: " + filename, module);
                                GenericValue pictureGV = delegator.makeValue("ProductPictureExternal");
                                pictureGV.put("productId", productId);
                                
                                if (filename.contains("Gallery")) { //gallery -- START
                                    Debug.logError("http://images.bellyanna.com" + "/" + parentSku + childSku + filename, module);
                                    pictureGV.put("pictureType", "GALLERY");
                                    pictureGV.put("pictureSeqId", df.format(galleryCount));
                                    galleryCount++;
                                }   //gallery -- END
                                else if (filename.contains("Size")) {   //size -- START
                                    Debug.logError("http://images.bellyanna.com" + "/" + parentSku + childSku + filename, module);
                                    pictureGV.put("pictureType", "SIZE");
                                    pictureGV.put("pictureSeqId", df.format(sizeCount));
                                    sizeCount++;
                                }   //size -- END
                                else if (filename.contains("Desc")) {   //desc -- START
                                    Debug.logError("http://images.bellyanna.com" + "/" + parentSku + childSku + filename, module);
                                    pictureGV.put("pictureType", "DESCRIPTION");
                                    pictureGV.put("pictureSeqId", df.format(descCount));
                                    descCount++;
                                }   //desc -- END
                                else {  //standard -- START
                                    Debug.logError("http://images.bellyanna.com" + "/" + parentSku + childSku + filename, module);
                                    pictureGV.put("pictureType", "STANDARD");
                                    pictureGV.put("pictureSeqId", df.format(standardCount));
                                    standardCount++;
                                }   //standard -- END
                                
                                pictureGV.put("pictureUrl", parentSku + childSku + filename);
                                delegator.createOrStore(pictureGV);
                                
                            }   //if filename contains JPG  -- END
                        }   //if ulChildNode string is more than 1  -- END
                    }   //loop ulChildNodes - END
                    
                }   //if URL exist -- END
                else {
                    result = ServiceUtil.returnSuccess("URL does not exist, not updating anything");
                }
            }   //try -- END
            catch (IOException e) {
                e.printStackTrace();
                return ServiceUtil.returnError(e.getMessage());
            }
        }   //if productId exists in ofbiz -- END
        else {
            result = ServiceUtil.returnSuccess("productId does not exist in Product database, not updating anything");
        }
        return result;
    }   //updateProductPictureExternal
    
    /** Service to remove Product Picture External */
    public static Map<String, Object> removeProductPictureExternal(DispatchContext ctx, Map<String, ? extends Object> context) {
        Map<String, Object> result = new HashMap<String, Object>();
        Delegator delegator = ctx.getDelegator();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productId = (String) context.get("productId");
        Map<String, String> fields = UtilMisc.<String, String>toMap("productId", productId);
        //Locale locale = (Locale) context.get("locale");
        
        List<GenericValue> testValue = null;
        
        try {
            testValue = delegator.findByAnd("ProductPictureExternal", fields, null, false);
        } catch (GenericEntityException e) {
            result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_ERROR);
            result.put(ModelService.ERROR_MESSAGE, "ERROR: Could not add into Product Picture External (" + e.getMessage() + ").");
            return result;
        }
        
        if (testValue == null) {
            result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_SUCCESS);
            return result;
        }
        
        try {
            List<GenericValue> values = delegator.findByAnd("ProductPictureExternal", fields, null, false);
            for (GenericValue value : values) {
                value.remove();
            }
        } catch (GenericEntityException e) {
            result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_ERROR);
            result.put(ModelService.ERROR_MESSAGE, "ERROR: Could not remove Product Picture External (" + e.getMessage() + ").");
            return result;
        }
        result.put(ModelService.RESPONSE_MESSAGE, ModelService.RESPOND_SUCCESS);
        return result;
    }
    
    
    public static Map<String, Object> updateProductEbayDescription (DispatchContext dctx, Map context)
	throws IOException, GenericEntityException, GenericServiceException {
        
        Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String productId = (String) context.get("productId");
        String altenate = (String) context.get("altenate");
        Map mapAccount = FastMap.newInstance();
        
        String alt = "";
        if (altenate != null) { //if altenate is not null -- START
            if (altenate.toUpperCase().equals("Y")) {
                alt = "_ALT";
            }
        }   //if altenate is not null -- END
        Debug.logError("productStoreId: " + productStoreId + ", productId: " + productId, module);
        
        try {
            Velocity.init();
            VelocityContext listingTemplateVC = new VelocityContext();
            Debug.logError("this run GV productId " + productId, module);
            GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
            Debug.logError("this run after GV productId " + productId, module);
            String description = product.getString("longDescription");
            boolean hasDescription = false;
            if (description != null) {
                hasDescription = true;
            }
            else {
                description = "";
            }
            
            String pictureTemplateTemp = "";
            List<GenericValue> productStoreTags = delegator.findByAnd("ProductStoreTag", UtilMisc.toMap("productStoreId", productStoreId), null, false);
            
            //Getting listing template -- START
            String listingTemplate = null;
            GenericValue productContentListingTemplate = EntityUtil.getFirst(delegator.findByAnd("ProductContent", UtilMisc.toMap("productId", productId, "productContentTypeId", "EBAY_LISTING_TEMPLATE" + alt), null, false));
            if (productContentListingTemplate != null) {    //if product has listingTemplate -- START
                GenericValue contentListingTemplate = productContentListingTemplate.getRelatedOne("Content", false);
                GenericValue dataResourceListingTemplate = contentListingTemplate.getRelatedOne("DataResource", false);
                GenericValue electronicTextListingTemplate = dataResourceListingTemplate.getRelatedOne("ElectronicText", false);
                listingTemplate = electronicTextListingTemplate.getString("textData");
            }   //if product has listingTemplate -- END
            else {  //if product does not have listingTemplate, take default from productStoreContentTemplate -- START
                GenericValue productStoreContentListingTemplate = delegator.findOne("ProductStoreContentTemplate", UtilMisc.toMap("productStoreId", productStoreId, "productStoreContentTypeId", "EBAY_LISTING_TEMPLATE" + alt), false);
                listingTemplate = productStoreContentListingTemplate.getString("textData");
            }   //if product does not have listingTemplate, take default from productStoreContentTemplate -- END
            //Getting listing template -- END
            
            //Getting picture template -- START
            String pictureTemplateOrig = null;
            GenericValue productContentPictureTemplate = EntityUtil.getFirst(delegator.findByAnd("ProductContent", UtilMisc.toMap("productId", productId, "productContentTypeId", "EBAY_PICTURE_TEMPLATE" + alt), null, false));
            if (productContentPictureTemplate != null) { //if product has pictureTemplate -- START
                GenericValue contentPictureTemplate = productContentPictureTemplate.getRelatedOne("Content", false);
                GenericValue dataResourcePictureTemplate = contentPictureTemplate.getRelatedOne("DataResource", false);
                GenericValue electronicTextPictureTemplate = dataResourcePictureTemplate.getRelatedOne("ElectronicText", false);
                pictureTemplateOrig = electronicTextPictureTemplate.getString("textData");
            }   //if product has pictureTemplate -- END
            else {  //if product does not have pictureTemplate, take default from productStoreContentTemplate -- START
                GenericValue productStoreContentListingTemplate = delegator.findOne("ProductStoreContentTemplate", UtilMisc.toMap("productStoreId", productStoreId, "productStoreContentTypeId", "EBAY_PICTURE_TEMPLATE" + alt), false);
                pictureTemplateOrig = productStoreContentListingTemplate.getString("textData");
            }   //if product does not have pictureTemplate, take default from productStoreContentTemplate -- END
            //Getting picture template -- END
            
            //Getting product picture external -- START
            String[] parentProductArray = productId.split("-");
            String parentProductId = parentProductArray[0] + "-" + parentProductArray[1];
            
            GenericValue productPictureExternalDesc = delegator.findOne("ProductPictureExternal", UtilMisc.toMap("productId", productId, "pictureType", "DESCRIPTION", "pictureSeqId", "00001"), false);
            if (productPictureExternalDesc == null) {  //if productPictureExternalDesc is null -- START
                dispatcher.runSync("updateProductPictureExternalSingle", UtilMisc.toMap("productId", productId,
                                                                                       "removeExisting", "Y",
                                                                                       "userLogin", userLogin
                                                                                       )
                                   );
                productPictureExternalDesc = delegator.findOne("ProductPictureExternal", UtilMisc.toMap("productId", productId, "pictureType", "DESCRIPTION", "pictureSeqId", "00001"), false);
                if (productPictureExternalDesc == null) {
                    productPictureExternalDesc = delegator.findOne("ProductPictureExternal", UtilMisc.toMap("productId", parentProductId, "pictureType", "DESCRIPTION", "pictureSeqId", "00001"), false);
                    if (productPictureExternalDesc == null) {
                        dispatcher.runSync("updateProductPictureExternalSingle", UtilMisc.toMap("productId", parentProductId,
                                                                                                "removeExisting", "Y",
                                                                                                "userLogin", userLogin
                                                                                                )
                                           );
                        productPictureExternalDesc = delegator.findOne("ProductPictureExternal", UtilMisc.toMap("productId", parentProductId, "pictureType", "DESCRIPTION", "pictureSeqId", "00001"), false);
                    }
                }
            }   //if productPictureExternalDesc is null -- END
            if (!hasDescription) {
                if (productPictureExternalDesc != null) {
                    listingTemplateVC.put("pictureDesc", productPictureExternalDesc.getString("pictureUrl"));
                }
                else {
                    listingTemplateVC.put("pictureDesc", "blank.jpg");
                }
            }
            
            GenericValue productPictureExternalSize = delegator.findOne("ProductPictureExternal", UtilMisc.toMap("productId", productId, "pictureType", "SIZE", "pictureSeqId", "00001"), false);
            if (productPictureExternalSize == null) {  //if productPictureExternalSize is null -- START
                dispatcher.runSync("updateProductPictureExternalSingle", UtilMisc.toMap("productId", productId,
                                                                                        "removeExisting", "Y",
                                                                                        "userLogin", userLogin
                                                                                        )
                                   );
                productPictureExternalSize = delegator.findOne("ProductPictureExternal", UtilMisc.toMap("productId", productId, "pictureType", "SIZE", "pictureSeqId", "00001"), false);
                if (productPictureExternalSize == null) {
                    productPictureExternalSize = delegator.findOne("ProductPictureExternal", UtilMisc.toMap("productId", parentProductId, "pictureType", "SIZE", "pictureSeqId", "00001"), false);
                    if (productPictureExternalSize == null) {
                        dispatcher.runSync("updateProductPictureExternalSingle", UtilMisc.toMap("productId", parentProductId,
                                                                                                "removeExisting", "Y",
                                                                                                "userLogin", userLogin
                                                                                                )
                                           );
                        productPictureExternalSize = delegator.findOne("ProductPictureExternal", UtilMisc.toMap("productId", parentProductId, "pictureType", "SIZE", "pictureSeqId", "00001"), false);
                    }
                }
            }   //if productPictureExternalDesc is null -- END
            if (!hasDescription) {
                if (productPictureExternalSize != null) {
                    listingTemplateVC.put("pictureSize", productPictureExternalSize.getString("pictureUrl"));
                }
                else {
                    listingTemplateVC.put("pictureSize", "blank.jpg");
                }
                
            }
            
            
            List<String> orderByPictureUrl = new ArrayList<String>();
            orderByPictureUrl.add("pictureUrl");
            List<GenericValue> productPictureExternals = delegator.findByAnd("ProductPictureExternal", UtilMisc.toMap("productId", productId, "pictureType", "STANDARD"), orderByPictureUrl, false);
            for (GenericValue productPictureExternal : productPictureExternals) {    //loop productPictureExternals -- START
                VelocityContext pictureTemplateVC = new VelocityContext();
                String pictureUrl = productPictureExternal.getString("pictureUrl");
                String pictureTemplate = pictureTemplateOrig;
                for (GenericValue productStoreTag : productStoreTags) { //loop productStoreTags -- START
                    pictureTemplateVC.put(productStoreTag.getString("tagName"), productStoreTag.getString("tagValue"));
                    //Debug.logError("TagName is " + productStoreTag.getString("tagName") + " with value " + productStoreTag.getString("tagValue"), module);
                }   //loop productStoreTags -- END
                
                pictureTemplateVC.put("pictureUrl", pictureUrl);
                
                StringWriter pictureTemplateBody = new StringWriter();
                Velocity.evaluate(pictureTemplateVC, pictureTemplateBody, "pictureTemplate", pictureTemplate);
                //Debug.logError("pictureType " + pictureType + " is : " + pictureTemplateBody, module);
                pictureTemplateTemp = pictureTemplateTemp + pictureTemplateBody + "\n";
            }   //loop productPictureExternals -- END
            
            //Getting product picture external -- END
            listingTemplateVC.put("pictureTemplate", pictureTemplateTemp);
            listingTemplateVC.put("descriptionTemplate", description);
            
            for (GenericValue productStoreTag : productStoreTags) { //loop productStoreTags -- START
                listingTemplateVC.put(productStoreTag.getString("tagName"), productStoreTag.getString("tagValue"));
                //Debug.logError("TagName is " + productStoreTag.getString("tagName") + " with value " + productStoreTag.getString("tagValue"), module);
            }   //loop productStoreTags -- END
            
            StringWriter listingTemplateBody = new StringWriter();
            Velocity.evaluate(listingTemplateVC, listingTemplateBody, "listingTemplate", listingTemplate);
            
            //Debug.logError(listingTemplateBody.toString(), module);
            GenericValue productContentEbayDescription = EntityUtil.getFirst(delegator.findByAnd("ProductContentAndInfo", UtilMisc.toMap("productId", productId, "productContentTypeId", "LONG_DESCRIPTION", "description", productStoreId + alt), null, false));
            
            Map<String, Object> updateDescription = FastMap.newInstance();
            if (productContentEbayDescription != null) {    //if eBay description exists, update it -- START
                updateDescription = dispatcher.runSync("updateSimpleTextContentForProduct", UtilMisc.toMap("productId", productId,
                                                                                                           "productContentTypeId", "LONG_DESCRIPTION",
                                                                                                           "contentId", productContentEbayDescription.getString("contentId"),
                                                                                                           "textDataResourceId", productContentEbayDescription.getString("dataResourceId"),
                                                                                                           "description", productStoreId + alt,
                                                                                                           "fromDate", productContentEbayDescription.getTimestamp("fromDate"),
                                                                                                           "text", listingTemplateBody.toString(),
                                                                                                           "userLogin", userLogin
                                                                                                           )
                                                       );
            }   //if eBay description exists, update it -- END
            else {  //if eBay description is blank, create it -- START
                updateDescription = dispatcher.runSync("createSimpleTextContentForProduct", UtilMisc.toMap("productId", productId,
                                                                                                           "productContentTypeId", "LONG_DESCRIPTION",
                                                                                                           "description", productStoreId + alt,
                                                                                                           "text", listingTemplateBody.toString(),
                                                                                                           "userLogin", userLogin
                                                                                                           )
                                                       );
            }   //if eBay description is blank, create it -- END
            
            if (ServiceUtil.isSuccess(updateDescription)) {   //if updateDescription returns success -- START
                result = ServiceUtil.returnSuccess();
            }   //if updateDescription returns success -- END
            else {
                result = ServiceUtil.returnError("createSimpleTextContentForProduct has failed");
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
        return result;
    }
    
    public static Map<String, Object> updateProductEbayDescriptionBulk (DispatchContext dctx, Map context)
	throws GenericServiceException, GenericEntityException, IOException {
        
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String productStoreId = (String) context.get("productStoreId");
        String removeExisting = (String) context.get("removeExisting");
        if (removeExisting == null) {
            removeExisting = "N";
        }
        int countProduct = 1;
        
        DynamicViewEntity productDve = new DynamicViewEntity();
        productDve.addMemberEntity("product", "Product");
        productDve.addAlias("product", "productId");
        productDve.addAlias("product", "salesDiscontinuationDate");
        EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                  //EntityCondition.makeCondition("quantityNotAvailable",EntityOperator.GREATER_THAN ,zero),
                                                                                  EntityCondition.makeCondition("salesDiscontinuationDate",EntityOperator.EQUALS ,null)
                                                                                  )
                                                                  );
        
        EntityListIterator productsELI = delegator.findListIteratorByCondition(productDve, condition, null, UtilMisc.toList("productId"), UtilMisc.toList("productId"), null);
        
        /*DynamicViewEntity productPictureDve = new DynamicViewEntity();
        productPictureDve.addMemberEntity("productPicture", "ProductPictureExternal");
        productPictureDve.addAlias("productPicture", "productId");
        EntityCondition condition2 = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                   //EntityCondition.makeCondition("quantityNotAvailable",EntityOperator.GREATER_THAN ,zero),
                                                                                   EntityCondition.makeCondition("productId",EntityOperator.NOT_EQUAL ,null)
                                                                                   )
                                                                   );
        
        EntityListIterator productPicturesELI = delegator.findListIteratorByCondition(productPictureDve, condition2, null, UtilMisc.toList("productId"), null, new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true));*/
        
        
        GenericValue productELI = null;
        //GenericValue productPictureELI = null;
        List<String> activeProductId = new ArrayList<String>();
        //int count = 0;
        while ((productELI = productsELI.next()) != null) { //loop productsELI -- START
            //count++;
            activeProductId.add(productELI.getString("productId"));
        }   //loop productsELI -- END
        //Debug.logError("check this " + productPicturesELI.size(), module);
        
        //if (productPicturesELI != null) {   //if productPicturesELI is not null -- START
        /*List<String> productPictureProductId = new ArrayList<String>();
        while ((productPictureELI = productPicturesELI.next()) != null) {   //loop productPicturesELI -- START
            productPictureProductId.add(productPictureELI.getString("productId"));
        }   //loop productPicturesELI -- END
        activeProductId.removeAll(productPictureProductId);*/
        //}   //if productPicturesELI is not null -- END
        
        productsELI.close();
        //productPicturesELI.close();
        
        //Running the updateProductPictureExternal -- START
        //writing errorProductId to file -- START
        try {   //try writing errorProductId to file -- START
            File f = new File ("hot-deploy/bellyanna/webapp/bellyanna/eBayResponseXML/errorProductId.txt");
            if(f.exists() && f.isFile()){
                f.delete();
            }
            FileWriter f1 = new FileWriter("hot-deploy/bellyanna/webapp/bellyanna/eBayResponseXML/errorProductId.txt", true);
            
            for (String productId : activeProductId) {  //loop activeProductId -- START
                Debug.logError("Processing productId " + productId + "...(" + countProduct + ")", module);
                Map<String, Object> updateProductEbayDescription = dispatcher.runSync("updateProductEbayDescription", UtilMisc.toMap("productId", productId, "productStoreId", productStoreId, "userLogin", userLogin));
                //Debug.logError("result is " + updateProductEbayDescription, module);
                if (ServiceUtil.isError(updateProductEbayDescription)) {    //if service success -- START
                    //if (updateProductEbayDescription.get("successMessage").equals("URL does not exist")) {    //if URL does not exist -- START
                        f1.write(productId + "\n");
                    //}   //if URL does not exist -- END
                    //Debug.logError("result is " + updateProductPictureExternalSingle.get("successMessage"), module);
                }   //if service success -- END
                
                countProduct++;
            }   //loop activeProductId -- END
            
            f1.close();
        }   //try writing errorProductId to file -- END
        catch (IOException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        //writing errorProductId to file -- END
        
        //Running the updateProductPictureExternal -- END
        
        return result;
        
    }
    
}	//END class
