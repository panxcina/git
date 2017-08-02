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

sbu = parameters.ownerGroup;

/* TASK 4 - START */
pmCondition1 = EntityCondition.makeCondition(UtilMisc.toList(
EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G1"),
EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, sbu),
EntityCondition.makeCondition("platform", EntityOperator.NOT_EQUAL, "S")
)
, EntityOperator.AND);
pmList1 = delegator.findList("ProductMasterResult", pmCondition1, UtilMisc.toSet("productId"), ["productId ASC"], null, false);
missingRivalSkuStringList = [];
for (pmList in pmList1) {
missingRivalSkuStringList.add(pmList.productId);
}

rivalItemCondition = EntityCondition.makeCondition(UtilMisc.toList(
EntityCondition.makeCondition("productId", EntityOperator.IN, missingRivalSkuStringList),
EntityCondition.makeCondition(UtilMisc.toList(
EntityCondition.makeCondition("crawl", EntityOperator.LIKE, "Y%"),
EntityCondition.makeCondition("crawl", EntityOperator.LIKE, "N%")
)
, EntityOperator.OR)
)
, EntityOperator.AND);

rivalItemList = delegator.findList("ProductMasterRival", rivalItemCondition, UtilMisc.toSet("productId"), ["productId ASC"], null, false);
crawledItemList = [];
for (rivalItem in rivalItemList) {
crawledItemList.add(rivalItem.productId);
}
missingRivalSkuStringList.removeAll(crawledItemList);

condition4List = [];
rowCount = 1;
for (productId in missingRivalSkuStringList) {
    data = [:];
    productMaster = delegator.findOne("ProductMasterResult", UtilMisc.toMap("productId", productId), false);
    mainSupplier = EntityUtil.getFirst(delegator.findByAnd("ProductMasterSupplier", UtilMisc.toMap("productId", productId, "supplierType", "MAIN"), null, false));
    listingCheck = delegator.findOne("ProductListingStatus", UtilMisc.toMap("productId", productId, "listingStatusTypeId", "LISTING_CHECK"), false);
    data.productId = productMaster.productId;
    data.chineseName = productMaster.chineseName;
    data.weight = productMaster.weight;
    data.productGroup = productMaster.productGroup;
    data.statusId = productMaster.statusId;
    data.categoryId = productMaster.categoryId;
    data.productType = productMaster.productType;
    data.ownerGroup = productMaster.ownerGroup;
    data.upc = productMaster.upc;
    data.platform = productMaster.platform;
    data.priceUsd = productMaster.priceUsd;
    data.cpPercentage = productMaster.cpPercentage;
    data.price = mainSupplier.price;
    data.supplier = mainSupplier.supplier;
    data.listingCheck = listingCheck.value;
    condition4List.add(data);
    rowCount++;
    if (rowCount > 500) {
        break;
    }

}

/* TASK 4 - END */


context.count = missingRivalSkuStringList.size();
context.productIdList = condition4List;








