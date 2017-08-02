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
productGroup = parameters.productGroup;
if (UtilValidate.isEmpty(sbu)) {
sbu = "ALL";
}
if (UtilValidate.isEmpty(productGroup)) {
productGroup = "ALL";
}
context.ownerGroup = sbu;
context.productGroup = productGroup;

SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
Calendar today = Calendar.getInstance();
lastWeek = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + (today.get(Calendar.DATE) - 7);
java.sql.Date lastWeekSql = new java.sql.Date(format.parse(lastWeek).getTime());
lastMonth = today.get(Calendar.YEAR) + "-" + today.get(Calendar.MONTH) + "-" + today.get(Calendar.DATE);
java.sql.Date lastMonthSql = new java.sql.Date(format.parse(lastMonth).getTime());

/* TASK 1 - START */
pmCondition1 = EntityCondition.makeCondition(UtilMisc.toList(
                                                    EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, productGroup),
                                                    EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, sbu),
                                                    EntityCondition.makeCondition("platform", EntityOperator.NOT_EQUAL, "S")
                                                    )
                                            , EntityOperator.AND);
pmList1 = delegator.findList("ProductMasterResult", pmCondition1, UtilMisc.toSet("productId"), ["productId ASC"], null, false);
pmStringList = [];
for (pmList in pmList1) {
    pmStringList.add(pmList.productId);
}
listingCondition1 = EntityCondition.makeCondition(UtilMisc.toList(
                                                    EntityCondition.makeCondition("listingStatusTypeId", EntityOperator.EQUALS, "LISTING_CHECK"),
                                                    EntityCondition.makeCondition("value", EntityOperator.EQUALS, "Y"),
                                                    EntityCondition.makeCondition("productId", EntityOperator.IN, pmStringList)
                                                    )
                                            , EntityOperator.AND);

listingStatusList1 = delegator.findList("ProductListingStatus", listingCondition1, UtilMisc.toSet("productId"), ["productId ASC"], null, false);
pmListingStatusStringList = [];
for (listingStatusList in listingStatusList1) {
pmListingStatusStringList.add(listingStatusList.productId);
}

pmStringList.removeAll(pmListingStatusStringList);
condition1List = [];
rowCount = 1;
for (productId in pmStringList) {
    lastWeekSales = 0;
    lastMonthSales = 0;
    salesConditionLastWeek = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("sku", EntityOperator.EQUALS, productId),
                                                            EntityCondition.makeCondition("createdDateSql", EntityOperator.GREATER_THAN_EQUAL_TO, lastWeekSql)
                                                            )
                                                    , EntityOperator.AND);
    salesConditionLastMonth = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("sku", EntityOperator.EQUALS, productId),
                                                            EntityCondition.makeCondition("createdDateSql", EntityOperator.GREATER_THAN_EQUAL_TO, lastMonthSql)
                                                            )
                                                    , EntityOperator.AND);
    eoiLastWeekList = delegator.findList("EbayOrderItem", salesConditionLastWeek, null, null, null, false);
    eoiLastMonthList = delegator.findList("EbayOrderItem", salesConditionLastMonth, null, null, null, false);

    for (eoiLastWeek in eoiLastWeekList) {
        currencyConversionWeek = delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId", "USD", "uomIdTo", eoiLastWeek.currency), null, false);
        conversionFactorWeek = EntityUtil.getFirst(currencyConversionWeek).conversionFactor;
        lastWeekSales = lastWeekSales + (eoiLastWeek.qty * eoiLastWeek.unitPrice * conversionFactorWeek);
    }

    for (eoiLastMonth in eoiLastMonthList) {
        currencyConversionMonth = delegator.findByAnd("UomConversionDated", UtilMisc.toMap("uomId", "USD", "uomIdTo", eoiLastMonth.currency), null, false);
        conversionFactorMonth = EntityUtil.getFirst(currencyConversionMonth).conversionFactor;
        lastMonthSales = lastMonthSales + (eoiLastMonth.qty * eoiLastMonth.unitPrice * conversionFactorMonth);
    }

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
    data.lastWeekSales = lastWeekSales;
    data.lastMonthSales = lastMonthSales;
    data.lastWeekDailyProfit = lastWeekSales * productMaster.cpPercentage;
    data.lastMonthDailyProfit = lastMonthSales * productMaster.cpPercentage;
    condition1List.add(data);
    rowCount++;
    if (rowCount > 500) {
        break;
    }

}

/* TASK 1 - END */


context.count = pmStringList.size();
context.countAll = pmList1.size();
context.productIdList = condition1List;








