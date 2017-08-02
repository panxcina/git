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

productStoreStrList = [];
productStoreList = delegator.findByAnd("ProductStore", UtilMisc.toMap("storeName", "GUDAO"), null, false);
for (productStore in productStoreList) {
productStoreStrList.add(productStore.productStoreId);
}

pmCondition3 = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                        EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G0"),
                                                                                                        EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G1"),
                                                                                                        EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G2"),
                                                                                                        EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G3")
                                                                                                        )
                                                                                            , EntityOperator.OR),
                                                            EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, sbu),
                                                            EntityCondition.makeCondition("platform", EntityOperator.NOT_EQUAL, "S"),
                                                            EntityCondition.makeCondition(UtilMisc.toList(
                                                                                                        EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "AU不上"),
                                                                                                        EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "UK不上"),
                                                                                                        EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "US不上")
                                                                                                        )
                                                                                            , EntityOperator.OR)
                                                            )
                                            , EntityOperator.AND);
pmList3 = delegator.findList("ProductMasterResult", pmCondition3, UtilMisc.toSet("productId"), ["productId ASC"], null, false);

pmStringList3 = [];
for (pmList in pmList3) {
    pmStringList3.add(pmList.productId);
}

for (productId in pmStringList3) {
    us = 1;
    uk = 1;
    au = 1;
    ca = 1;
    de = 1;
    fr = 1;
    es = 1;

    condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                EntityCondition.makeCondition("normalizedSku", EntityOperator.EQUALS, productId),
                                                                EntityCondition.makeCondition("productStoreId", EntityOperator.IN, productStoreStrList)
                                                                )
, EntityOperator.AND);

    ealList = delegator.findList("EbayActiveListing", condition, null, null, null, false);
    if (UtilValidate.isNotEmpty(ealList)) {
        for (eal in ealList) {
            if (eal.site.equals("US")) {
                us = 0;
            } else if (eal.site.equals("US")) {
                us = 0;
            } else if (eal.site.equals("UK")) {
                uk = 0;
            } else if (eal.site.equals("Australia")) {
                au = 0;
            } else if (eal.site.equals("Canada")) {
                ca = 0;
            } else if (eal.site.equals("Germany")) {
                de = 0;
            } else if (eal.site.equals("France")) {
                fr = 0;
            } else if (eal.site.equals("Spain")) {
                es = 0;
            }
        }
    }

    ealvList = delegator.findList("EbayActiveListingVariation", condition, null, null, null, false);
    if (UtilValidate.isNotEmpty(ealvList)) {
        for (ealv in ealvList) {
            mainListing = EntityUtil.getFirst(delegator.findByAnd("EbayActiveListing", UtilMisc.toMap("itemId", ealv.itemId), null, false));
            site = mainListing.site;
            if (site.equals("US")) {
                us = 0;
            } else if (site.equals("US")) {
                us = 0;
            } else if (site.equals("UK")) {
                uk = 0;
            } else if (site.equals("Australia")) {
                au = 0;
            } else if (site.equals("Canada")) {
                ca = 0;
            } else if (site.equals("Germany")) {
                de = 0;
            } else if (site.equals("France")) {
                fr = 0;
            } else if (site.equals("Spain")) {
                es = 0;
            }
        }
    }


}








