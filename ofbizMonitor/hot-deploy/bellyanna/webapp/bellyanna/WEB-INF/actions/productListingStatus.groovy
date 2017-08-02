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

productStoreId = parameters.productStoreId;
quantity = parameters.quantity;
list = [];


if (!productStoreId.equals("All")) {
    condition = EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreId);
}

activeListings = delegator.findList("EbayActiveListing", condition, null, ["productStoreId ASC"], null, false);

for (activeListing in activeListings) {
    exprList = [];
    exprList.add(EntityCondition.makeCondition("itemId", EntityOperator.EQUALS, activeListing.itemId));
    exprList.add(EntityCondition.makeCondition("quantity", EntityOperator.LESS_THAN_EQUAL_TO, quantity.toLong()));
    listingVariations = delegator.findList("EbayActiveListingVariation", EntityCondition.makeCondition(exprList, EntityOperator.AND), null, null, null, false);
    for (listingVariation in listingVariations) {
        list.add(listingVariation);
    }
}
context.variationLists = list;
context.variationCount = list.size();