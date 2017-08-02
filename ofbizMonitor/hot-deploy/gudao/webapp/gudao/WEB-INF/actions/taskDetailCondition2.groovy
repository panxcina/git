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

/* TASK 2 - START */
condition2List = [];
pmCondition2 = EntityCondition.makeCondition(UtilMisc.toList(
                                                        EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, sbu),
                                                        EntityCondition.makeCondition("platform", EntityOperator.NOT_EQUAL, "S")
                                                        )
                                                , EntityOperator.AND);
pmList2 = delegator.findList("ProductMasterResult", pmCondition2, null, null, null, false);
rowCount = 1;
for (pmSingle2 in pmList2) {
    data = [:];
    pmPrice = pmSingle2.priceUsd;
    profitPercentage = pmSingle2.cpPercentage;
    rivalCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                        EntityCondition.makeCondition("productId", EntityOperator.EQUALS, pmSingle2.productId),
                                                        EntityCondition.makeCondition("crawl", EntityOperator.LIKE, "Y%")
                                                        )
                                                , EntityOperator.AND);
    rivalListGv = delegator.findList("ProductMasterRival", rivalCondition, null, null, null, false);
    if(UtilValidate.isNotEmpty(rivalListGv)) {
        rivalList = EntityUtil.getFirst(rivalListGv);
        rivalTotalPrice = rivalList.totalPrice;
        if (rivalList.currency.equals("GBP")) {
            pmPrice = pmSingle2.priceGbp;
            profitPercentage = pmSingle2.gbpPercentage;
        } else if (rivalList.currency.equals("AUD")) {
            pmPrice = pmSingle2.priceAud;
            profitPercentage = pmSingle2.audPercentage;
        } else if (rivalList.currency.equals("CAD")) {
            pmPrice = pmSingle2.priceCad;
            profitPercentage = pmSingle2.cadPercentage;
        } else if (rivalList.currency.equals("EUR")) {
            pmPrice = pmSingle2.priceEur;
            profitPercentage = pmSingle2.eurPercentage;
        }
        if (rivalTotalPrice < pmPrice) {
            data.productId = pmSingle2.productId;
            data.productName = pmSingle2.chineseName;
            data.productGroup = pmSingle2.productGroup;
            data.pmPrice = pmPrice;
            data.profitPercentage = profitPercentage;
            data.currency = rivalList.currency;
            data.rivalTotalPrice = rivalTotalPrice;
            /*data.rivalSoldPerDay = rivalList.soldPerDay;*/
            data.rivalProfitPercentage = rivalList.samePriceProfitPercentage;
            data.rivalHistorySold = rivalList.historySold;
            data.rivalDailySales = rivalList.dailySales;
            data.rank = rivalList.rank;
            condition2List.add(data);
            rowCount++;
            if (rowCount > 500) {
                break;
            }
        }
    }
}

/* TASK 2 - END */


context.count = condition2List.size();
context.productIdList = condition2List;








