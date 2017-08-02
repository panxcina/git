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
import org.ofbiz.order.order.OrderListState;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
/*import java.text.DateFormat;*/
import java.text.SimpleDateFormat;

condition = null;
productId = parameters.productId;
productStoreId = parameters.productStoreId;
soldInLastXdays = parameters.soldInLastXdays;
ebayActiveListings = [];
multiVarListing = false;

if (soldInLastXdays == null) {
    soldInLastXdays = 30;
}

product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);

fromDay = Calendar.getInstance();
toDay = Calendar.getInstance();
fromDay.set(Calendar.DATE, fromDay.get(Calendar.DATE) - soldInLastXdays.toInteger());
toDay.set(Calendar.DATE, toDay.get(Calendar.DATE));
sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
fromDate = Timestamp.valueOf(sdf.format(fromDay.getTime()));
toDate = Timestamp.valueOf(sdf.format(toDay.getTime()));

if (productStoreId.equals("All") || productStoreId == null) {
    variations = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("productId", productId), null, false);
}
else {
    variations = delegator.findByAnd("EbayActiveListingVariation", UtilMisc.toMap("productId", productId, "productStoreId", productStoreId), null, false);
}
//println(variations);

if (variations.size() > 0) {
//println("this if works");
    for (variation in variations) {
        multiVarListing = true;
        atp = 0;
        qoh = 0;
        discontinueDate = null;
        if (product.salesDiscontinuationDate != null) {
            discontinueDate = product.salesDiscontinuationDate;
        }
        inventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("productId", productId), null, false);
        for (inventoryItem in inventoryItemList) {
            atp += inventoryItem.availableToPromiseTotal;
            qoh += inventoryItem.quantityOnHandTotal;
        }
    
        soldCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("productId",EntityOperator.EQUALS ,productId),
                                                            EntityCondition.makeCondition("createdStamp",EntityOperator.GREATER_THAN ,fromDate),
                                                            EntityCondition.makeCondition("createdStamp",EntityOperator.LESS_THAN ,toDate)
                                                            )
                                            );
        soldQty = 0;
        orderItemList = delegator.findList("OrderItem", soldCondition, null, ["productId ASC"], null, false);
        for (orderItem in orderItemList) {
            soldQty += orderItem.quantity;
        }
    
        data = [:];
        data.multiVarListing = multiVarListing;
        data.atp = atp;
        data.qoh = qoh;
        data.productId = productId;
        data.discontinueDate = discontinueDate;
        data.soldQty = soldQty;
        
        itemId = variation.itemId;
        varSeqId = variation.variationSeqId;
        ebayActiveListing = EntityUtil.getFirst(delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", itemId), null, false));
        
        data.productStoreId = variation.productStoreId;
        data.itemId = variation.itemId;
        data.sku = variation.productId;
        data.startPrice = variation.startPrice;
        data.currencyId = variation.startPriceCurrencyId;
        data.hitCount = ebayActiveListing.hitCount;
        data.watchCount = ebayActiveListing.watchCount;
        data.listingDuration = ebayActiveListing.listingDuration;
        data.listingType = ebayActiveListing.listingType;
        data.quantity = variation.quantity;
        data.quantitySold = variation.quantitySold;
        data.updatedStamp = variation.lastUpdatedStamp;
        ebayActiveListings.add(data);
    }
}
else {
    multiVarListing = false;
    //println("this else works");
    atp = 0;
    qoh = 0;
    discontinueDate = null;
    if (product.salesDiscontinuationDate != null) {
        discontinueDate = product.salesDiscontinuationDate;
    }
    inventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("productId", productId), null, false);
    for (inventoryItem in inventoryItemList) {
        atp += inventoryItem.availableToPromiseTotal;
        qoh += inventoryItem.quantityOnHandTotal;
    }
    
    soldCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                        EntityCondition.makeCondition("productId",EntityOperator.EQUALS ,productId),
                                                        EntityCondition.makeCondition("createdStamp",EntityOperator.GREATER_THAN ,fromDate),
                                                        EntityCondition.makeCondition("createdStamp",EntityOperator.LESS_THAN ,toDate)
                                                        )
                                        );
    soldQty = 0;
    orderItemList = delegator.findList("OrderItem", soldCondition, null, ["productId ASC"], null, false);
    for (orderItem in orderItemList) {
        soldQty += orderItem.quantity;
    }
    
    data2 = [:];
    data2.multiVarListing = multiVarListing;
    data2.atp = atp;
    data2.qoh = qoh;
    data2.productId = productId;
    data2.discontinueDate = discontinueDate;
    data2.soldQty = soldQty;
    
    ebayActiveListing = EntityUtil.getFirst(delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("sku", productId), null, false));
    
    data2.productStoreId = ebayActiveListing.productStoreId;
    data2.itemId = ebayActiveListing.itemId;
    data2.sku = productId;
    data2.startPrice = ebayActiveListing.sellStatCurrentPrice;
    data2.currencyId = ebayActiveListing.sellStatCurrentPriceCurId;
    data2.hitCount = ebayActiveListing.hitCount;
    data2.watchCount = ebayActiveListing.watchCount;
    data2.listingDuration = ebayActiveListing.listingDuration;
    data2.listingType = ebayActiveListing.listingType;
    data2.quantity = ebayActiveListing.quantity;
    data2.quantitySold = ebayActiveListing.sellStatQuantitySold;
    data2.updatedStamp = ebayActiveListing.lastUpdatedStamp;
    ebayActiveListings.add(data2);
}



context.ebayActiveListings = ebayActiveListings;
context.soldInLastXdays = soldInLastXdays;