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

smtRefundPct = 0.05;
wishRefundPct = 0.1;
packagingCost = 0.5;

SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
fromDateStr = parameters.fromDate;
toDateStr = parameters.toDate;


Calendar today = Calendar.getInstance();
todayStr = today.get(Calendar.YEAR) + "-" + (today.get(Calendar.MONTH) + 1) + "-" + today.get(Calendar.DATE);
java.sql.Date todaySql = new java.sql.Date(format.parse(todayStr).getTime());

Calendar yesterdayCal = Calendar.getInstance();
yesterdayCal.add(Calendar.DATE, -1);
yesterdayStr = yesterdayCal.get(Calendar.YEAR) + "-" + (yesterdayCal.get(Calendar.MONTH) + 1) + "-" + yesterdayCal.get(Calendar.DATE);
java.sql.Date yesterdaySql = new java.sql.Date(format.parse(yesterdayStr).getTime());

java.sql.Date fromDate = yesterdaySql;
java.sql.Date toDate = todaySql;
Timestamp fromDateTs = new Timestamp ((fromDate).getTime());
Timestamp toDateTs = new Timestamp ((toDate).getTime());

if (UtilValidate.isNotEmpty(fromDateStr)) {
    Date fromDateParse = format.parse(fromDateStr.substring(0,10));
    fromDate = new java.sql.Date(fromDateParse.getTime());
}

if (UtilValidate.isNotEmpty(toDateStr)) {
    Date toDateParse = format.parse(toDateStr.substring(0,10));
    toDate = new java.sql.Date(toDateParse.getTime());
}

dayCount = toDate - fromDate + 1;
context.dayCount=dayCount;
Map allRefundList = FastMap.newInstance();
Map tableList = FastMap.newInstance();

platformList = [];
platformList.add("ebay");
platformList.add("aliexpres");
platformList.add("Wish");
platformList.add("Amazon");


for (platform in platformList) {
platformSalesList = [];
platformCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                    EntityCondition.makeCondition("orderDate", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate),
                                                    EntityCondition.makeCondition("orderDate", EntityOperator.LESS_THAN_EQUAL_TO, toDate),
                                                    EntityCondition.makeCondition("platform", EntityOperator.EQUALS, platform)
                                                    )
                                        , EntityJoinOperator.AND);

ebayList = delegator.findList("ReportProfit", platformCondition, null, ["ownerGroup ASC"], null, false);

groupList=[];
for (ebay in ebayList) {
groupList.add(ebay.ownerGroup);
}
HashSet<String> uniqueGroupList = new HashSet<String>(groupList);

for (ownerGroup in uniqueGroupList) {   /*ownerGroup*/
    data = [:];
    data.ownerGroup = ownerGroup;
    salesTotal = 0;
    feeTotal = 0;
    shippingCostTotal = 0;
    productCostTotal = 0;
    ownerGroupRefundTotal = 0;
    domesticDelivery = 0;
    domesticProductCostTotal = 1;

    ownerListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                    platformCondition,
                                                                    EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                    EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroup)
                                                                                                ))
                                                                    )
                                        , EntityJoinOperator.AND);
    ownerList = delegator.findList("ReportProfit", ownerListCondition, null, ["ownerGroup ASC"], null, false);
    for (owner in ownerList) {
        salesTotal += owner.normalizeTotalSales;
        feeTotal += owner.normalizeTotalFee;
        shippingCostTotal += owner.normalizeShippingCost;
        productCostTotal += owner.normalizeProductCost;
    }
    if (salesTotal < 1) {
    salesTotal = 1;
    }
    refundListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                        EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate),
                                                        EntityCondition.makeCondition("date", EntityOperator.LESS_THAN_EQUAL_TO, toDate),
                                                        EntityCondition.makeCondition("platform", EntityOperator.EQUALS, platform),
                                                        EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroup)
                                                        )
                                            , EntityJoinOperator.AND);
    refundList = delegator.findList("GudaoRefund", refundListCondition, null, null, null, false);
    for (refund in refundList) {
        ownerGroupRefundTotal += refund.amount;
    }
    allRefundList.put(ownerGroup, ownerGroupRefundTotal);

    costListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate),
                                                            EntityCondition.makeCondition("date", EntityOperator.LESS_THAN_EQUAL_TO, toDate),
                                                            EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroup)
                                                            )
                                            , EntityJoinOperator.AND);
    costList = delegator.findList("GudaoOperationCost", costListCondition, null, null, null, false);
    for (cost in costList) {
        domesticDelivery += cost.domesticDelivery;
        domesticProductCostTotal += cost.totalProductCost;
    }
    domesticDeliveryFee = productCostTotal * domesticDelivery / domesticProductCostTotal;

    data.domesticDeliveryFee = domesticDeliveryFee;
    data.salesTotal = salesTotal/dayCount;
    data.feeTotal = feeTotal/dayCount;
    data.shippingCostTotal = shippingCostTotal/dayCount;
    data.productCostTotal = productCostTotal/dayCount;
    data.totalPackaging = ownerList.size()/dayCount*packagingCost;
    platformSalesList.add(data);


}   /*ownerGroup*/

tableList.put(platform, platformSalesList);

context.tableList = tableList;
context.allRefundList = allRefundList;

}



allPlatformCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                EntityCondition.makeCondition("orderDate", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate),
                                                                EntityCondition.makeCondition("orderDate", EntityOperator.LESS_THAN_EQUAL_TO, toDate)
                                                                    )
                                                , EntityJoinOperator.AND);

allList = delegator.findList("ReportProfit", allPlatformCondition, null, ["ownerGroup ASC"], null, false);

allPlatformGroupList=[];
for (allSales in allList) {
    allPlatformGroupList.add(allSales.ownerGroup);
}
HashSet<String> uniqueAllPlatformGroupList = new HashSet<String>(allPlatformGroupList);

allPlatformSalesList = [];
Map allPlatformRefundList = FastMap.newInstance();
for (allPlatformOwnerGroup in uniqueAllPlatformGroupList) {
    dataAllPlatform = [:];
    dataAllPlatform.ownerGroup = allPlatformOwnerGroup;
    salesTotalAll = 0;
    feeTotalAll = 0;
    shippingCostTotalAll = 0;
    productCostTotalAll = 0;
    ownerGroupRefundTotalAll = 0;
    domesticDeliveryAll = 0;
    domesticProductCostTotalAll = 1;

    ownerAllListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                    allPlatformCondition,
                                                                    EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                            EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, allPlatformOwnerGroup)
                                                                                                            )
                                                                                                )
                                                                )
                                                , EntityJoinOperator.AND);
    ownerAllList = delegator.findList("ReportProfit", ownerAllListCondition, null, ["ownerGroup ASC"], null, false);
    for (ownerAll in ownerAllList) {
        salesTotalAll += ownerAll.normalizeTotalSales;
        feeTotalAll += ownerAll.normalizeTotalFee;
        shippingCostTotalAll += ownerAll.normalizeShippingCost;
        productCostTotalAll += ownerAll.normalizeProductCost;
    }

    refundAllListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                        EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate),
                                                                        EntityCondition.makeCondition("date", EntityOperator.LESS_THAN_EQUAL_TO, toDate),
                                                                        EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, allPlatformOwnerGroup)
                                                                        )
                                                        , EntityJoinOperator.AND);
    refundAllList = delegator.findList("GudaoRefund", refundAllListCondition, null, null, null, false);
    for (refundAll in refundAllList) {
        ownerGroupRefundTotalAll += refundAll.amount;
    }
    allPlatformRefundList.put(allPlatformOwnerGroup, ownerGroupRefundTotalAll);

    costAllListCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                    EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate),
                                                                    EntityCondition.makeCondition("date", EntityOperator.LESS_THAN_EQUAL_TO, toDate),
                                                                    EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, allPlatformOwnerGroup)
                                                                    )
                                                    , EntityJoinOperator.AND);
    costAllList = delegator.findList("GudaoOperationCost", costAllListCondition, null, null, null, false);
    for (costAll in costAllList) {
        domesticDeliveryAll += costAll.domesticDelivery;
        domesticProductCostTotalAll += costAll.totalProductCost;
    }
    domesticDeliveryFeeAll = productCostTotal * domesticDelivery / domesticProductCostTotal;

dataAllPlatform.domesticDeliveryFee = domesticDeliveryFeeAll;
dataAllPlatform.salesTotal = salesTotalAll/dayCount;
dataAllPlatform.feeTotal = feeTotalAll/dayCount;
dataAllPlatform.shippingCostTotal = shippingCostTotalAll/dayCount;
dataAllPlatform.productCostTotal = productCostTotalAll/dayCount;
dataAllPlatform.totalPackaging = ownerAllList.size()/dayCount*packagingCost;
allPlatformSalesList.add(dataAllPlatform);


}
context.allPlatformSalesList = allPlatformSalesList;