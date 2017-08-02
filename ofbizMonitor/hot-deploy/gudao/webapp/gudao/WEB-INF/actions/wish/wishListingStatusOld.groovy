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

SimpleDateFormat format = new SimpleDateFormat("mm dd, yyyy");

storeCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("primaryStoreGroupId", EntityOperator.EQUALS, "WISH"),
                                                            EntityCondition.makeCondition("subtitle", EntityOperator.NOT_EQUAL, "0")
                                                            )
                                            , EntityJoinOperator.AND);
productStoreList = delegator.findList("ProductStore", storeCondition, null, ["subtitle ASC"], null, false);

Map accountMap = FastMap.newInstance();
storeList = [];
for (productStore in productStoreList) {
    dateList = [];
    productStoreId = productStore.productStoreId;
    storeList.add(productStoreId);
    for ( i in 0..7 ) {
        int countPromoted = 0;
        disabled = 0;
        Calendar fromDateCal = Calendar.getInstance();
        fromDateCal.add(Calendar.DATE, - i);
        fromDateStr = fromDateCal.get(Calendar.MONTH) + " " + fromDateCal.get(Calendar.DATE) + ", " + fromDateCal.get(Calendar.YEAR);
        java.sql.Date fromDateSql = new java.sql.Date(fromDateCal.getTime().getTime());

        totalCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("date", EntityOperator.EQUALS, fromDateSql),
                                                            EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId)
                                                            )
                                                , EntityJoinOperator.AND);
        totalListingCount = delegator.findCountByCondition("WishListing", totalCondition, null, null);

        pendingCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("date", EntityOperator.EQUALS, fromDateSql),
                                                            EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                            EntityCondition.makeCondition("reviewStatus", EntityOperator.EQUALS, "PENDING")
                                                            )
                                                , EntityJoinOperator.AND);
        pendingListingCount = delegator.findCountByCondition("WishListing", pendingCondition, null, null);

        rejectedCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("date", EntityOperator.EQUALS, fromDateSql),
                                                            EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                            EntityCondition.makeCondition("reviewStatus", EntityOperator.EQUALS, "REJECTED")
                                                            )
                                                , EntityJoinOperator.AND);
        rejectedListingCount = delegator.findCountByCondition("WishListing", rejectedCondition, null, null);

        condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("date", EntityOperator.EQUALS, fromDateSql),
                                                            EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId),
                                                            EntityCondition.makeCondition("reviewStatus", EntityOperator.EQUALS, "APPROVED")
                                                            )
                                                , EntityJoinOperator.AND);
        wishListingByDateList = delegator.findList("WishListing", condition, null, null, null, false);

        data = [:];
        variantNotPromoted = [];
        variantPromoted = [];
        nonPromotedStock = [];
        nonPromotedNoStock = [];
        nonPromotedNoInventory = [];
        promotedStock = [];
        for (wishListingByDate in wishListingByDateList) {
            enabled = true;
            listingId = wishListingByDate.listingId;
            isPromoted = wishListingByDate.isPromoted;

            wishListingVariationList = delegator.findList("WishListingVariation", EntityCondition.makeCondition("listingId", EntityOperator.EQUALS, listingId), null, null, null, false);
            for (wishListingVariation in wishListingVariationList) {
                productId = wishListingVariation.normalizedSku;
                averageWeeklySold = 0;
                qty = 0;
                gudaoInventory = delegator.findOne("GudaoInventory", UtilMisc.toMap("productId", productId, "date", fromDateSql), false);
                if (UtilValidate.isNotEmpty(gudaoInventory)) {
                    qty = gudaoInventory.qty;
                    averageWeeklySold = gudaoInventory.averageWeeklySold;
                    if (UtilValidate.isEmpty(averageWeeklySold)) {
                        pastDay = 1;
                        while (UtilValidate.isEmpty(averageWeeklySold)) {
                            Calendar pastDateCal = Calendar.getInstance();
                            pastDateCal.add(Calendar.DATE, - pastDay);
                            java.sql.Date pastDateSql = new java.sql.Date(pastDateCal.getTime().getTime());
                            GenericValue pastInventory = delegator.findOne("GudaoInventory", UtilMisc.toMap("productId", productId, "date", pastDateSql), false);
                            if (UtilValidate.isNotEmpty(pastInventory)) {
                                averageWeeklySold = pastInventory.averageWeeklySold;
                            }
                            pastDay++;
                        }
                    }
                }


                if (wishListingVariation.enabled.equals("TRUE")) {
                    if (isPromoted.equals("TRUE")) {
                        if (UtilValidate.isNotEmpty(gudaoInventory)) {
                            if (qty >= averageWeeklySold) {
                                promotedStock.add(productId);
                            }
                        }
                        variantPromoted.add(wishListingVariation.normalizedSku);
                    }
                    else {
                        if (UtilValidate.isNotEmpty(gudaoInventory)) {
                            if (qty > 0) {
                                nonPromotedStock.add(productId);
                            }
                            else {
                                nonPromotedNoStock.add(productId);
                            }
                        } else {
                            nonPromotedNoStock.add(productId);
                        }
                        variantNotPromoted.add(wishListingVariation.normalizedSku);
                        if (wishListingVariation.inventory == 0) {
                            nonPromotedNoInventory.add(productId);
                        }
                    }
                }
                else {
                    enabled = false;
                }
            }
            if (!enabled) {
                disabled++;
            } else {
                if (isPromoted.equals("TRUE")) {
                    countPromoted ++;
                }
            }
        }
        data.totalListingCount = totalListingCount - disabled;
        data.pendingListingCount = pendingListingCount;
        data.rejectedListingCount = rejectedListingCount;
        data.promotedListingCount = countPromoted;
        data.listingCount = wishListingByDateList.size() - disabled;
        data.variantNotPromotedCount = variantNotPromoted.size();
        data.variantPromotedCount = variantPromoted.size();
        data.nonPromotedStockCount = nonPromotedStock.size();
        data.nonPromotedNoStockCount = nonPromotedNoStock.size();
        data.promotedStockCount = promotedStock.size();
        data.nonPromotedNoInventory = nonPromotedNoInventory.size();
        data.nonPromotedWithInventory = variantNotPromoted.size() - nonPromotedNoInventory.size();
        data.date = fromDateSql;
        dateList.add(data);
    }
    accountMap.put(productStoreId, dateList);
}
context.accountMap = accountMap;

listOfDate=[];
for ( i in 0..7 ) {
Calendar fromDateCal = Calendar.getInstance();
fromDateCal.add(Calendar.DATE, - i);
fromDateStr = fromDateCal.get(Calendar.MONTH) + " " + fromDateCal.get(Calendar.DATE) + ", " + fromDateCal.get(Calendar.YEAR);
java.sql.Date fromDateSql = new java.sql.Date(fromDateCal.getTime().getTime());
listOfDate.add(fromDateSql);
}
context.listOfDate = listOfDate;
context.storeList = storeList;
