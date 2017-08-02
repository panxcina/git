package com.bellyanna.facility;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.lang.Math;
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
import javax.servlet.http.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.net.URLDecoder;

import javolution.util.FastMap;

import org.ofbiz.base.util.UtilMisc;
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

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class facilityService {
	private static final String module = facilityService.class.getName();
    private static final String eol = System.getProperty("line.separator");
	
public static Map<String, Object> freeProductFacilityLocation (DispatchContext dctx, Map context)
	throws IOException, GenericEntityException, GenericServiceException {
	
    Map<String, Object> result = FastMap.newInstance();
	Delegator delegator = dctx.getDelegator();
	LocalDispatcher dispatcher = dctx.getDispatcher();
    result = ServiceUtil.returnSuccess();
    String facilityId = (String) context.get("facilityId");
    
        try {   //main Try block -- START
            List<GenericValue> productFacilityLocations = delegator.findByAnd("ProductFacilityLocation", UtilMisc.toMap("facilityId", facilityId), null, false);
            for (GenericValue productFacilityLocation : productFacilityLocations) { //Loop productFacilityLocation -- START
                
                String productId = productFacilityLocation.getString("productId");
                String locationSeqId = productFacilityLocation.getString("locationSeqId");
                //Debug.logError("Processing ProductId " + productId + " with locationSeqId " + locationSeqId, module);
                GenericValue facilityLocation = delegator.findOne("FacilityLocation", UtilMisc.toMap("facilityId", facilityId, "locationSeqId", locationSeqId), false);
                if ((facilityLocation.getString("areaId") != null) && (facilityLocation.getString("areaId").equals("00") || facilityLocation.getString("areaId").equals("01"))) {    //If facilityLocation areaId = 00 -- START
                    int atp = 0;
                    int qoh = 0;
                    List<GenericValue> inventoryItems = delegator.findByAnd("InventoryItem", UtilMisc.toMap("productId", productId, "locationSeqId", locationSeqId, "facilityId", facilityId), null, false);
                    for (GenericValue inventoryItem : inventoryItems) { //Loop inventoryItem -- START
                        //Debug.logError(inventoryItem.toString(), module);
                        atp = inventoryItem.getBigDecimal("availableToPromiseTotal").intValueExact() + atp;
                        qoh = inventoryItem.getBigDecimal("quantityOnHandTotal").intValueExact() + qoh;
                    }   //Loop inventoryItem -- END
                    //Debug.logError("product ID " + productId + " with location " + locationSeqId + " has ATP " + atp + " and qoh " + qoh, module);
                    if (qoh == 0) { //if atp and qoh is 0 -- START
                        //Debug.logError("product ID " + productId + " with location " + locationSeqId + " can be cleared", module);
                        delegator.removeByAnd("ProductFacilityLocation", UtilMisc.toMap("facilityId", facilityId, "productId", productId, "locationSeqId", locationSeqId));
                        GenericValue updateProductFacilityLocation = delegator.makeValue("ProductFacilityLocation", UtilMisc.toMap("facilityId", facilityId, "productId", productId, "locationSeqId", "NoLocation"));
                        delegator.createOrStore(updateProductFacilityLocation);
                        EntityCondition condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                  EntityCondition.makeCondition("productId",EntityOperator.EQUALS ,productId),
                                                                                                  EntityCondition.makeCondition("locationSeqId",EntityOperator.EQUALS ,locationSeqId),
                                                                                                  EntityCondition.makeCondition("quantityOnHandTotal",EntityOperator.NOT_EQUAL ,BigDecimal.ZERO),
                                                                                                  EntityCondition.makeCondition("availableToPromiseTotal",EntityOperator.NOT_EQUAL ,BigDecimal.ZERO)
                                                                                                  ),
                                                                                  EntityOperator.AND
                                                                                  );
                        List<GenericValue> inventoryLists = delegator.findList("InventoryItem", condition, null, null, null, false);
                        for (GenericValue inventoryList : inventoryLists) { //loop inventoryLists -- START
                            GenericValue updateInvLocation = delegator.makeValue("InventoryItem", UtilMisc.toMap("inventoryItemId", inventoryList.getString("inventoryItemId"), "locationSeqId", "NoLocation"));
                            delegator.store(updateInvLocation);
                        }   //loop inventoryLists -- END
                            
                        
                    }   //if atp and qoh is 0 -- START
                    /*if (atp == 0 && qoh == 0) { //if atp and qoh is 0 -- START
                        //Debug.logError("product ID " + productId + " with location " + locationSeqId + " can be cleared", module);
                        List<GenericValue> noLocation = delegator.findByAnd("ProductFacilityLocation", UtilMisc.toMap("productId", productId, "locationSeqId", "NoLocation"));
                        if (!(noLocation.size() > 0)) { //if NoLocation not exist -- START
                            //Debug.logError("product ID " + productId + " with location " + locationSeqId + " and NoLocation Does NOT exist", module);
                            delegator.removeByAnd("ProductFacilityLocation", UtilMisc.toMap("facilityId", facilityId, "productId", productId, "locationSeqId", locationSeqId));
                            GenericValue updateProductFacilityLocation = delegator.makeValue("ProductFacilityLocation", UtilMisc.toMap("facilityId", facilityId, "productId", productId, "locationSeqId", "NoLocation"));
                            delegator.create(updateProductFacilityLocation);
                        } else {
                            //Debug.logError("product ID " + productId + " with location " + locationSeqId + " and NoLocation Does exist", module);
                            delegator.removeByAnd("ProductFacilityLocation", UtilMisc.toMap("facilityId", facilityId, "productId", productId, "locationSeqId", locationSeqId));
                        }   //if NoLocation not exist -- END
                    }   //if atp and qoh is 0 -- START*/
                }   //If facilityLocation areaId = 00 -- END
            }   //Loop productFacilityLocation -- END
        }   //main Try block -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        
    return result;
}
	
    public static Map<String, Object> adjustProductFacilityLocation (DispatchContext dctx, Map context)
	throws IOException, GenericEntityException, GenericServiceException {
        
        Map<String, Object> result = FastMap.newInstance();
        Delegator delegator = dctx.getDelegator();
        LocalDispatcher dispatcher = dctx.getDispatcher();
        result = ServiceUtil.returnSuccess();
        String facilityId = (String) context.get("facilityId");
        String productId = (String) context.get("productId");
        String locationSeqId = (String) context.get("locationSeqId");
        
        GenericValue userLogin = null;
        try {
            userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "system"), false);
        }
        catch (GenericEntityException e) {
            e.printStackTrace();
        }
        
        //Debug.logError("facilityId is " + facilityId + ", product ID is " + productId +", locationSeqId is " + locationSeqId, module);
        
        try {   //main Try block -- START
            boolean noLocation = false;
            boolean diffLocation = false;
            List<GenericValue> productFacilityLocations = delegator.findByAnd("ProductFacilityLocation", UtilMisc.toMap("facilityId", facilityId, "productId", productId), null, false);
            if (productFacilityLocations.size() > 0) {  //if productFacilityLocations has data -- START
                for (GenericValue productFacilityLocation : productFacilityLocations) { //loop productFacilityLocation -- START
                    String location = productFacilityLocation.getString("locationSeqId");
                    if (location.equals("NoLocation")) {    //If location is NoLocation -- START
                        noLocation = true;
                    }   //If location is NoLocation -- END
                    else if (!location.equals(locationSeqId)) {  //If location is not the same -- START
                        diffLocation = true;
                    }   //If location is not the same -- END
                }   //loop productFacilityLocation -- END
                
                if (noLocation) {   //If noLocation is TRUE -- START
                    try {   //remove NoLocation and create a new location -- START
                        dispatcher.runSync("deleteProductFacilityLocation",
                                           UtilMisc.toMap("facilityId", facilityId,
                                                          "productId", productId,
                                                          "locationSeqId", "NoLocation",
                                                          "userLogin", userLogin
                                                          )
                                           );
                        dispatcher.runSync("createProductFacilityLocation",
                                           UtilMisc.toMap("facilityId", facilityId,
                                                          "productId", productId,
                                                          "locationSeqId", locationSeqId,
                                                          "userLogin", userLogin
                                                          )
                                           );
                    }   //remove NoLocation and create a new location -- END
                    catch (GenericServiceException e) {
                        e.printStackTrace();
                        return ServiceUtil.returnError(e.getMessage());
                    }
                }   //If noLocation is TRUE -- END
                
                if (diffLocation) {   //If diffLocation is TRUE -- START
                    try {   //create a new location -- START
                        dispatcher.runSync("createProductFacilityLocation",
                                           UtilMisc.toMap("facilityId", facilityId,
                                                          "productId", productId,
                                                          "locationSeqId", locationSeqId,
                                                          "userLogin", userLogin
                                                          )
                                           );
                    }   //create a new location -- END
                    catch (GenericServiceException e) {
                        e.printStackTrace();
                        return ServiceUtil.returnError(e.getMessage());
                    }
                }   //If diffLocation is TRUE -- END
            }   //if productFacilityLocations has data -- END
            else {  //if productFacilityLocations is blank -- START
                try {   //create a new location -- START
                    dispatcher.runSync("createProductFacilityLocation",
                                       UtilMisc.toMap("facilityId", facilityId,
                                                      "productId", productId,
                                                      "locationSeqId", locationSeqId,
                                                      "userLogin", userLogin
                                                      )
                                       );
                }   //create a new location -- END
                catch (GenericServiceException e) {
                    e.printStackTrace();
                    return ServiceUtil.returnError(e.getMessage());
                }
            }   //if productFacilityLocations is blank -- END
        }   //main Try block -- END
        catch (GenericEntityException e) {
            e.printStackTrace();
            return ServiceUtil.returnError(e.getMessage());
        }
        return result;
    }
    
}	//END class
