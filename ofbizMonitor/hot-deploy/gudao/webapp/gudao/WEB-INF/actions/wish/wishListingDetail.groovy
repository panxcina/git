/*
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
 */

import org.ofbiz.base.util.*;
import org.ofbiz.entity.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.entity.condition.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
/*import java.sql.Date;
import java.text.DateFormat;*/
import java.text.SimpleDateFormat;
import javolution.util.FastMap;
import javolution.util.FastList;


productStoreIdInput = parameters.productStoreId;
hasStockInput = parameters.hasStock;
isPromotedInput = parameters.isPromoted;
reviewStatusInput = parameters.reviewStatus;
wishInventoryInput = parameters.wishInventory;
enabledInput = parameters.listingStatus;
sevenDayStockInput = parameters.sevenDayStock;

if (UtilValidate.isEmpty(productStoreIdInput)) {
    productStoreIdInput = "initialLoading";
}

Calendar fromDateCal = Calendar.getInstance();
java.sql.Date fromDateSql = new java.sql.Date(fromDateCal.getTime().getTime());


List<EntityCondition> conditions = FastList.newInstance();
conditions.add(EntityCondition.makeCondition("date", EntityOperator.EQUALS, fromDateSql));
if (UtilValidate.isNotEmpty(productStoreIdInput)) {
    if (!productStoreIdInput.equals("ALL")) {
        conditions.add(EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreIdInput));
    }
}

if (UtilValidate.isNotEmpty(isPromotedInput)) {
    if (isPromotedInput.equals("YES")) {
        conditions.add(EntityCondition.makeCondition("isPromoted", EntityOperator.EQUALS, "TRUE"));
    } else if (isPromotedInput.equals("NO")) {
        conditions.add(EntityCondition.makeCondition("isPromoted", EntityOperator.EQUALS, "FALSE"));
    }
}

if (UtilValidate.isNotEmpty(reviewStatusInput)) {
    if (reviewStatusInput.equals("APPROVED")) {
        conditions.add(EntityCondition.makeCondition("reviewStatus", EntityOperator.EQUALS, "APPROVED"));
    } else if (reviewStatusInput.equals("PENDING")) {
        conditions.add(EntityCondition.makeCondition("reviewStatus", EntityOperator.EQUALS, "PENDING"));
    } else if (reviewStatusInput.equals("REJECTED")) {
        conditions.add(EntityCondition.makeCondition("reviewStatus", EntityOperator.EQUALS, "REJECTED"));
    }
}

EntityCondition cond = null;
if (conditions.size() > 0) {
    cond = EntityCondition.makeCondition(conditions, EntityOperator.AND);
}

wishListingList = delegator.findList("WishListing", cond, null, null, null, false);

resultList = [];
for (wishListing in wishListingList) {
    enabledDisplay = true;
    wishInventoryDisplay = true;
    sevenDayStockDisplay = true;
    hasStockDisplay = true;
    listingId = wishListing.listingId;
    productStoreId = wishListing.productStoreId;
    isPromoted = wishListing.isPromoted;
    reviewStatus = wishListing.reviewStatus;
    numberSaves = wishListing.numberSaves;
    numberSold = wishListing.numberSold;
    List<GenericValue> wishListingVariations = delegator.findByAnd("WishListingVariation", UtilMisc.toMap("listingId", listingId), null, false);
    for (wishListingVariation in wishListingVariations) {
        data = [:];
        sku = wishListingVariation.sku;
        normalizedSku = wishListingVariation.normalizedSku;
        price = wishListingVariation.price;
        enabled = wishListingVariation.enabled;
        shipping = wishListingVariation.shipping;
        inventory = wishListingVariation.inventory;
        msrp = wishListingVariation.msrp;
        shippingTime = wishListingVariation.shippingTime;
        GenericValue gudaoInventory = delegator.findOne("GudaoInventory", UtilMisc.toMap("productId", normalizedSku, "date", fromDateSql), false);
        qoh = 0;
        averageWeeklySold = 0;
        if (UtilValidate.isNotEmpty(gudaoInventory)) {
            qoh = gudaoInventory.qty;
            averageWeeklySold = gudaoInventory.averageWeeklySold;
            if (UtilValidate.isEmpty(averageWeeklySold)) {
                pastDay = 1;
                while (UtilValidate.isEmpty(averageWeeklySold)) {
                    Calendar pastDateCal = Calendar.getInstance();
                    pastDateCal.add(Calendar.DATE, - pastDay);
                    java.sql.Date pastDateSql = new java.sql.Date(pastDateCal.getTime().getTime());
                    GenericValue pastInventory = delegator.findOne("GudaoInventory", UtilMisc.toMap("productId", normalizedSku, "date", pastDateSql), false);
                    averageWeeklySold = pastInventory.averageWeeklySold;
                    pastDay++;
                }
            }
        }

        data.productStoreId = productStoreId;
        data.isPromoted = isPromoted;
        data.reviewStatus = reviewStatus;
        data.numberSaves = numberSaves;
        data.numberSold = numberSold;
        data.sku = sku;
        data.normalizedSku = normalizedSku;
        data.price = price;
        data.enabled = enabled;
        data.shipping = shipping;
        data.inventory = inventory;
        data.msrp = msrp;
        data.shippingTime = shippingTime;
        data.qoh = qoh;
        data.averageWeeklySold = averageWeeklySold;

        if (UtilValidate.isNotEmpty(wishInventoryInput)) {
            if (wishInventoryInput.equals("YES")) {
                if (inventory > 0) {
                    wishInventoryDisplay = true;
                } else if (inventory == 0) {
                    wishInventoryDisplay = false;
                }
            } else if (wishInventoryInput.equals("NO")) {
                if (inventory > 0) {
                    wishInventoryDisplay = false;
                } else if (inventory == 0) {
                    wishInventoryDisplay = true;
                }
            }
        }

        if (UtilValidate.isNotEmpty(enabledInput)) {
            if (enabledInput.equals("ENABLED")) {
                if (enabled.equals("TRUE")) {
                    enabledDisplay = true;
                } else if (enabled.equals("FALSE")) {
                    enabledDisplay = false;
                }
            } else if (enabledInput.equals("DISABLED")) {
                if (enabled.equals("TRUE")) {
                    enabledDisplay = false;
                } else if (enabled.equals("FALSE")) {
                    enabledDisplay = true;
                }
            }
        }

        if (UtilValidate.isNotEmpty(hasStockInput)) {
            if (hasStockInput.equals("YES")) {
                if (qoh > 0) {
                    hasStockDisplay = true;
                } else if (qoh == 0) {
                    hasStockDisplay = false;
                }
            } else if (hasStockInput.equals("NO")) {
                if (qoh > 0) {
                    hasStockDisplay = false;
                } else if (qoh == 0) {
                    hasStockDisplay = true;
                }
            }
        }

        if (UtilValidate.isNotEmpty(sevenDayStockInput)) {
            if (sevenDayStockInput.equals("YES")) {
                if (qoh >= averageWeeklySold) {
                    sevenDayStockDisplay = true;
                } else if (qoh < averageWeeklySold) {
                    sevenDayStockDisplay = false;
                }
            } else if (sevenDayStockInput.equals("NO")) {
                if (qoh >= averageWeeklySold) {
                    sevenDayStockDisplay = false;
                } else if (qoh < averageWeeklySold) {
                    sevenDayStockDisplay = true;
                }
            }
        }

        if (wishInventoryDisplay && enabledDisplay && hasStockDisplay && sevenDayStockDisplay) {
            resultList.add(data);
        }
    }
}
context.resultList = resultList;
context.rowCount = resultList.size();















