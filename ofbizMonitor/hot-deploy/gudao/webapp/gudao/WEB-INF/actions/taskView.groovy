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


sbuGVlist = delegator.findList("ProductMasterResult", null, UtilMisc.toSet("ownerGroup"), UtilMisc.toList("ownerGroup"), new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);

sbuList = [];
Map condition1Map = FastMap.newInstance();
Map condition1G2Map = FastMap.newInstance();
Map condition1G3Map = FastMap.newInstance();
Map condition2Map = FastMap.newInstance();
Map condition3Map = FastMap.newInstance();
Map condition4Map = FastMap.newInstance();

for (sbuGV in sbuGVlist) {
    sbu = sbuGV.getString("ownerGroup");
if (!sbu.equals("G5")) {


    sbuList.add(sbu);

/* TASK 1 - START */
pmCondition1 = EntityCondition.makeCondition(UtilMisc.toList(
                                                    EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G1"),
                                                    EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, sbu),
                                                    EntityCondition.makeCondition("platform", EntityOperator.NOT_EQUAL, "S")
                                                    )
                                            , EntityOperator.AND);
pmList1 = delegator.findList("ProductMasterResult", pmCondition1, UtilMisc.toSet("productId"), ["productId ASC"], null, false);
pmStringList = [];
missingRivalSkuStringList = [];
for (pmList in pmList1) {
    pmStringList.add(pmList.productId);
    missingRivalSkuStringList.add(pmList.productId);
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
condition1Map.put(sbu, pmStringList.size() + " / " + pmList1.size());


/* G2 - condition1 - START */
G2condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                    EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G2"),
                                                    EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, sbu),
                                                    EntityCondition.makeCondition("platform", EntityOperator.NOT_EQUAL, "S")
                                                    )
                                            , EntityOperator.AND);
G2List = delegator.findList("ProductMasterResult", G2condition, UtilMisc.toSet("productId"), ["productId ASC"], null, false);
G2StringList = [];
for (pmG2List in G2List) {
    G2StringList.add(pmG2List.productId);
}
listingConditionG2 = EntityCondition.makeCondition(UtilMisc.toList(
                                                    EntityCondition.makeCondition("listingStatusTypeId", EntityOperator.EQUALS, "LISTING_CHECK"),
                                                    EntityCondition.makeCondition("value", EntityOperator.EQUALS, "Y"),
                                                    EntityCondition.makeCondition("productId", EntityOperator.IN, G2StringList)
                                                    )
                                            , EntityOperator.AND);

listingStatusListG2 = delegator.findList("ProductListingStatus", listingConditionG2, UtilMisc.toSet("productId"), ["productId ASC"], null, false);
pmG2ListingStatusStringList = [];
for (listingStatusG2List in listingStatusListG2) {
    pmG2ListingStatusStringList.add(listingStatusG2List.productId);
}

G2StringList.removeAll(pmG2ListingStatusStringList);
condition1G2Map.put(sbu, G2StringList.size() + " / " + G2List.size());
/* G2 - condition1 - END */

/* G3 - condition1 - START */
G3condition = EntityCondition.makeCondition(UtilMisc.toList(
                                                    EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G3"),
                                                    EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, sbu),
                                                    EntityCondition.makeCondition("platform", EntityOperator.NOT_EQUAL, "S")
                                                    )
                                            , EntityOperator.AND);
G3List = delegator.findList("ProductMasterResult", G3condition, UtilMisc.toSet("productId"), ["productId ASC"], null, false);
G3StringList = [];
for (pmG3List in G3List) {
    G3StringList.add(pmG3List.productId);
}
listingConditionG3 = EntityCondition.makeCondition(UtilMisc.toList(
                                                    EntityCondition.makeCondition("listingStatusTypeId", EntityOperator.EQUALS, "LISTING_CHECK"),
                                                    EntityCondition.makeCondition("value", EntityOperator.EQUALS, "Y"),
                                                    EntityCondition.makeCondition("productId", EntityOperator.IN, G3StringList)
                                                    )
                                            , EntityOperator.AND);

listingStatusListG3 = delegator.findList("ProductListingStatus", listingConditionG3, UtilMisc.toSet("productId"), ["productId ASC"], null, false);
pmG3ListingStatusStringList = [];
for (listingStatusG3List in listingStatusListG3) {
    pmG3ListingStatusStringList.add(listingStatusG3List.productId);
}

G3StringList.removeAll(pmG3ListingStatusStringList);
condition1G3Map.put(sbu, G3StringList.size() + " / " + G3List.size());
/* G3 - condition1 - END */
/* TASK 1 - END */



/* TASK 2 - START */
condition2List = [];
pmCondition2 = EntityCondition.makeCondition(UtilMisc.toList(
                                                    EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, sbu),
                                                    EntityCondition.makeCondition("platform", EntityOperator.NOT_EQUAL, "S")
                                                    )
                                            , EntityOperator.AND);
pmList2 = delegator.findList("ProductMasterResult", pmCondition2, null, null, null, false);

for (pmSingle2 in pmList2) {
    pmPrice = pmSingle2.priceUsd;
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
        } else if (rivalList.currency.equals("AUD")) {
            pmPrice = pmSingle2.priceAud;
        } else if (rivalList.currency.equals("CAD")) {
            pmPrice = pmSingle2.priceCad;
        } else if (rivalList.currency.equals("EUR")) {
            pmPrice = pmSingle2.priceEur;
        }
        if (rivalTotalPrice < pmPrice) {
            condition2List.add(pmSingle2.productId);
        }
    }
}
condition2Map.put(sbu, condition2List.size());
/* TASK 2 - END */

/* TASK 3 - START */
/*Map siteMap = FastMap.newInstance();
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

usTotal = 0;
ukTotal = 0;
auTotal = 0;
caTotal = 0;
deTotal = 0;
frTotal = 0;
esTotal = 0;

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
    usTotal += us;
    ukTotal += uk;
    auTotal += au;
    caTotal += ca;
    deTotal += de;
    frTotal += fr;
    esTotal += es;
}
siteMap.put("US", usTotal);
siteMap.put("UK", ukTotal);
siteMap.put("AU", auTotal);
siteMap.put("CA", caTotal);
siteMap.put("DE", deTotal);
siteMap.put("FR", frTotal);
siteMap.put("ES", esTotal);

condition3Map.put(sbu, siteMap);*/
/* TASK 3 - END */


/* TASK 4 - START */
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
missingRivalSkuStringList.removeAll(crawledItemList)
condition4Map.put(sbu, missingRivalSkuStringList.size());
/* TASK 4 - END */

}
}
context.sbuList = sbuList;
context.condition1Map = condition1Map;
context.condition1G2Map = condition1G2Map;
context.condition1G3Map = condition1G3Map;
context.condition2Map = condition2Map;
context.condition3Map = condition3Map;
context.condition4Map = condition4Map;









