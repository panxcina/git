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

productStoreId = parameters.productStoreId;
productId = parameters.productId;
listingType = parameters.listingType;

condition = null;
productCondition = null;
listingTypeCondition = null;

productStoreList = [];
list = [];
activeList = [];

if (productStoreId != null) {
if (!productStoreId.equals("All")) {
    productStoreList = delegator.findList("ProductStore", EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId), null, ["productStoreId ASC"], null, false);
}
else {
    productStoreList = delegator.findList("ProductStore", EntityCondition.makeCondition("primaryStoreGroupId", EntityOperator.EQUALS, "EBAY"), null, ["productStoreId ASC"], null, false);
}

if (productId == null) {
    productCondition = EntityCondition.makeCondition("sku", EntityOperator.NOT_EQUAL, null);
} else {
    productCondition = EntityCondition.makeCondition("sku", EntityOperator.LIKE, "%" + productId.toUpperCase() + "%");
}

if (listingType == null || listingType.equals("All")) {
    listingTypeCondition = EntityCondition.makeCondition("listingType", EntityOperator.NOT_EQUAL, null);
} else {
    listingTypeCondition = EntityCondition.makeCondition("listingType", EntityOperator.EQUALS, listingType);
}
    
for (productStoreSingle in productStoreList) {
    productStoreId = productStoreSingle.productStoreId;

    fileMap = [:];
    fileMap.put("productStoreId", productStoreId);
    condition = EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId);
    
    fixedPriceExp = [];
    fixedPriceExp.add(condition);
    fixedPriceExp.add(productCondition);
    fixedPriceExp.add(EntityCondition.makeCondition("listingType", EntityOperator.EQUALS, "FixedPriceItem"));
    fixedPriceListing = delegator.findCountByCondition("EbayActiveListing", EntityCondition.makeCondition(fixedPriceExp, EntityOperator.AND), null, null);
    fileMap.put("fixedPriceListing", fixedPriceListing);

    auctionExp = [];
    auctionExp.add(condition);
    auctionExp.add(productCondition);
    auctionExp.add(EntityCondition.makeCondition("listingType", EntityOperator.EQUALS, "Chinese"));
    auctionListing = delegator.findCountByCondition("EbayActiveListing", EntityCondition.makeCondition(auctionExp, EntityOperator.AND), null, null);
    fileMap.put("auctionListing", auctionListing);

    totalExp = [];
    totalExp.add(condition);
    totalExp.add(productCondition);
    totalListing = delegator.findCountByCondition("EbayActiveListing", EntityCondition.makeCondition(totalExp, EntityOperator.AND), null, null);
    fileMap.put("totalListing", totalListing);
    list.add(fileMap);

    
    psExp =[];
    psExp.add(condition);
    psExp.add(productCondition);
    psExp.add(listingTypeCondition);
    productStoreActiveListing = delegator.findList("EbayActiveListing", EntityCondition.makeCondition(psExp, EntityOperator.AND), null, ["productStoreId ASC"], null, false);

}
context.listIt = list;
context.listActiveListing = productStoreActiveListing;


}


