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

SimpleDateFormat formatTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

productId = parameters.productId;
productName = parameters.productName;
productGroupG0 = parameters.productGroupG0;
productGroupG1 = parameters.productGroupG1;
productGroupG2 = parameters.productGroupG2;
productGroupG3 = parameters.productGroupG3;
productGroupG4 = parameters.productGroupG4;
productGroupG4a = parameters.productGroupG4a;
productGroupG5 = parameters.productGroupG5;
productGroupG6 = parameters.productGroupG6;
ownerGroupAdmin = parameters.ownerGroupAdmin;
ownerGroupDada = parameters.ownerGroupDada;
ownerGroupFangxi = parameters.ownerGroupFangxi;
ownerGroupGege = parameters.ownerGroupGege;
ownerGroupJerry = parameters.ownerGroupJerry;
ownerGroupMatty = parameters.ownerGroupMatty;
ownerGroupMina = parameters.ownerGroupMina;
ownerGroupNick = parameters.ownerGroupNick;
ownerGroupNiki = parameters.ownerGroupNiki;
platformS = parameters.platformS;
platformSe= parameters.platformSe;
platformSew = parameters.platformSew;
platformSewa = parameters.platformSewa;
platformSeok = parameters.platformSeok;
ebayPicture = parameters.ebayPicture;
amazonPicture = parameters.amazonPicture;
ebayUsListing = parameters.ebayUsListing;
ebayUkListing = parameters.ebayUkListing;
ebayAuListing = parameters.ebayAuListing;
ebayCaListing = parameters.ebayCaListing;
ebayDeListing = parameters.ebayDeListing;
ebayFrListing = parameters.ebayFrListing;
ebayEsListing = parameters.ebayEsListing;
fromDateStr = parameters.fromDate;
toDateStr = parameters.toDate;

Calendar today = Calendar.getInstance();
Timestamp todayTS = Timestamp.valueOf(formatTimestamp.format(today.getTime()));

Calendar yesterdayCal = Calendar.getInstance();
yesterdayCal.add(Calendar.DATE, -1);
Timestamp yesterdayTS = Timestamp.valueOf(formatTimestamp.format(yesterdayCal.getTime()));

if (UtilValidate.isNotEmpty(fromDateStr)) {
    fromDateTS = new java.sql.Timestamp(formatTimestamp.parse(fromDateStr).getTime());
} else {
    fromDateTS = yesterdayTS;
}

if (UtilValidate.isNotEmpty(toDateStr)) {
    toDateTS = new java.sql.Timestamp(formatTimestamp.parse(toDateStr).getTime());
} else {
    toDateTS = todayTS;
}

List<EntityCondition> conditions = FastList.newInstance();

if (UtilValidate.isNotEmpty(productId)) {
    productId =  productId.trim();
    conditions.add(EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%" + productId + "%"));
}

if (UtilValidate.isNotEmpty(productName)) {
    productName =  productName.trim();
    nameCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                                EntityCondition.makeCondition("chineseName", EntityOperator.LIKE, "%" + productName + "%"),
                                                                EntityCondition.makeCondition("englishName", EntityOperator.LIKE, "%" + productName + "%")
                                                                )
                                    , EntityOperator.OR);
    conditions.add(nameCondition);
}

List<EntityCondition> productGroupConditions = FastList.newInstance();

if (UtilValidate.isNotEmpty(productGroupG0)) {
    if (productGroupG0.equals("Y")) {
        productGroupConditions.add(EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G0"));
    }
}

if (UtilValidate.isNotEmpty(productGroupG1)) {
    if (productGroupG1.equals("Y")) {
        productGroupConditions.add(EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G1"));
    }
}

if (UtilValidate.isNotEmpty(productGroupG2)) {
    if (productGroupG2.equals("Y")) {
        productGroupConditions.add(EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G2"));
    }
}

if (UtilValidate.isNotEmpty(productGroupG3)) {
    if (productGroupG3.equals("Y")) {
        productGroupConditions.add(EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G3"));
    }
}

if (UtilValidate.isNotEmpty(productGroupG4)) {
    if (productGroupG4.equals("Y")) {
        productGroupConditions.add(EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G4"));
    }
}

if (UtilValidate.isNotEmpty(productGroupG4a)) {
    if (productGroupG4a.equals("Y")) {
        productGroupConditions.add(EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "准G4"));
    }
}

if (UtilValidate.isNotEmpty(productGroupG5)) {
    if (productGroupG5.equals("Y")) {
        productGroupConditions.add(EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G5"));
    }
}

if (UtilValidate.isNotEmpty(productGroupG6)) {
    if (productGroupG6.equals("Y")) {
        productGroupConditions.add(EntityCondition.makeCondition("productGroup", EntityOperator.EQUALS, "G6"));
    }
}

if (productGroupConditions.size() > 0) {
    conditions.add(EntityCondition.makeCondition(productGroupConditions, EntityOperator.OR));
}

List<EntityCondition> ownerGroupConditions = FastList.newInstance();

if (UtilValidate.isNotEmpty(ownerGroupAdmin)) {
    if (ownerGroupAdmin.equals("Y")) {
        ownerGroupConditions.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, "ADMIN"));
    }
}

if (UtilValidate.isNotEmpty(ownerGroupDada)) {
    if (ownerGroupDada.equals("Y")) {
        ownerGroupConditions.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, "DADA"));
    }
}

if (UtilValidate.isNotEmpty(ownerGroupFangxi)) {
    if (ownerGroupFangxi.equals("Y")) {
        ownerGroupConditions.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, "FANGXI"));
    }
}

if (UtilValidate.isNotEmpty(ownerGroupGege)) {
    if (ownerGroupGege.equals("Y")) {
    ownerGroupConditions.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, "GEGE"));
    }
}

if (UtilValidate.isNotEmpty(ownerGroupJerry)) {
    if (ownerGroupJerry.equals("Y")) {
    ownerGroupConditions.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, "JERRY"));
    }
}

if (UtilValidate.isNotEmpty(ownerGroupMatty)) {
    if (ownerGroupMatty.equals("Y")) {
        ownerGroupConditions.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, "MATTY"));
    }
}

if (UtilValidate.isNotEmpty(ownerGroupMina)) {
    if (ownerGroupMina.equals("Y")) {
    ownerGroupConditions.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, "MINA"));
    }
}

if (UtilValidate.isNotEmpty(ownerGroupNick)) {
    if (ownerGroupNick.equals("Y")) {
        ownerGroupConditions.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, "NICK"));
    }
}

if (UtilValidate.isNotEmpty(ownerGroupNiki)) {
    if (ownerGroupNiki.equals("Y")) {
        ownerGroupConditions.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, "NIKI"));
    }
}

if (ownerGroupConditions.size() > 0) {
    conditions.add(EntityCondition.makeCondition(ownerGroupConditions, EntityOperator.OR));
}

List<EntityCondition> platformConditions = FastList.newInstance();

if (UtilValidate.isNotEmpty(platformS)) {
    if (platformS.equals("Y")) {
        platformConditions.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "S"));
    }
}

if (UtilValidate.isNotEmpty(platformSe)) {
    if (platformSe.equals("Y")) {
        seCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "SE"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "ES")
                                                                )
                                                , EntityOperator.OR);
        platformConditions.add(seCondition);
    }
}

if (UtilValidate.isNotEmpty(platformSew)) {
    if (platformSew.equals("Y")) {
        sewCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "SEW"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "SWE"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "WSE"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "WES"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "ESW"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EWS")
                                                                )
                                                , EntityOperator.OR);
        platformConditions.add(sewCondition);
    }
}

if (UtilValidate.isNotEmpty(platformSewa)) {
    if (platformSewa.equals("Y")) {
        sewaCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "SEWA"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "SWEA"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "WSEA"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "WESA"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "ESWA"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EWSA")
                                                                )
                                                , EntityOperator.OR);
        platformConditions.add(sewaCondition);
    }
}

if (UtilValidate.isNotEmpty(platformSeok)) {
    if (platformSeok.equals("Y")) {
        seokCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "SEOK"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "ESOK"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "OKSE"),
                                                            EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "OKES")
                                                                )
                                                , EntityOperator.OR);
        platformConditions.add(seokCondition);
    }
}

if (platformConditions.size() > 0) {
    conditions.add(EntityCondition.makeCondition(platformConditions, EntityOperator.OR));
}

if (UtilValidate.isNotEmpty(ebayPicture)) {
    if (ebayPicture.equals("Y")) {
        conditions.add(EntityCondition.makeCondition("ebayPicture", EntityOperator.EQUALS, ebayPicture));
    }
}

if (UtilValidate.isNotEmpty(amazonPicture)) {
    if (ebayPicture.equals("Y")) {
        conditions.add(EntityCondition.makeCondition("amazonPicture", EntityOperator.EQUALS, amazonPicture));
    }
}

if (UtilValidate.isNotEmpty(ebayUsListing)) {
    if (ebayPicture.equals("Y")) {
        conditions.add(EntityCondition.makeCondition("ebayUsListing", EntityOperator.EQUALS, ebayUsListing));
    }
}

if (UtilValidate.isNotEmpty(ebayUkListing)) {
    if (ebayPicture.equals("Y")) {
        conditions.add(EntityCondition.makeCondition("ebayUkListing", EntityOperator.EQUALS, ebayUkListing));
    }
}

if (UtilValidate.isNotEmpty(ebayAuListing)) {
    if (ebayPicture.equals("Y")) {
        conditions.add(EntityCondition.makeCondition("ebayAuListing", EntityOperator.EQUALS, ebayAuListing));
    }
}

if (UtilValidate.isNotEmpty(ebayCaListing)) {
    if (ebayPicture.equals("Y")) {
        conditions.add(EntityCondition.makeCondition("ebayCaListing", EntityOperator.EQUALS, ebayCaListing));
    }
}

if (UtilValidate.isNotEmpty(ebayDeListing)) {
    if (ebayPicture.equals("Y")) {
        conditions.add(EntityCondition.makeCondition("ebayDeListing", EntityOperator.EQUALS, ebayDeListing));
    }
}

if (UtilValidate.isNotEmpty(ebayFrListing)) {
    if (ebayPicture.equals("Y")) {
        conditions.add(EntityCondition.makeCondition("ebayFrListing", EntityOperator.EQUALS, ebayFrListing));
    }
}

if (UtilValidate.isNotEmpty(ebayEsListing)) {
    if (ebayPicture.equals("Y")) {
        conditions.add(EntityCondition.makeCondition("ebayEsListing", EntityOperator.EQUALS, ebayEsListing));
    }
}

EntityCondition cond = null;
if (conditions.size() > 0) {
    cond = EntityCondition.makeCondition(conditions, EntityOperator.AND);
} else {
    cond = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, "Impossibleone");
}

productSearchList = delegator.findList("ProductMasterResult", cond, null, ["productId ASC"], null, false);

rowCount = 1;
list = [];
for (productList in productSearchList) {
data = [:];
productIdResult = productList.productId;
chineseName = productList.chineseName;
englishName = productList.englishName;
productGroupResult = productList.productGroup;
ownerGroupResult = productList.ownerGroup;
platformResult = productList.platform;
ebayPictureResult = productList.ebayPicture;
amazonPictureResult = productList.amazonPicture;
ebayUsListingResult = productList.ebayUsListing;
ebayUkListingResult = productList.ebayUkListing;
ebayAuListingResult = productList.ebayAuListing;
ebayCaListingResult = productList.ebayCaListing;
ebayDeListingResult = productList.ebayDeListing;
ebayFrListingResult = productList.ebayFrListing;
ebayEsListingResult = productList.ebayEsListing;

data.productIdResult = productIdResult;
data.chineseName = chineseName;
data.englishName = englishName;
data.productGroupResult = productGroupResult;
data.ownerGroupResult = ownerGroupResult;
data.platformResult = platformResult;
data.ebayPictureResult = ebayPictureResult;
data.amazonPictureResult = amazonPictureResult;
data.ebayUsListingResult = ebayUsListingResult;
data.ebayUkListingResult = ebayUkListingResult;
data.ebayAuListingResult = ebayAuListingResult;
data.ebayCaListingResult = ebayCaListingResult;
data.ebayDeListingResult = ebayDeListingResult;
data.ebayFrListingResult = ebayFrListingResult;
data.ebayEsListingResult = ebayEsListingResult;


ebayOrderItemCond = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("sku", EntityOperator.EQUALS, productIdResult),
                                                            EntityCondition.makeCondition("createdDateStamp", EntityOperator.GREATER_THAN_EQUAL_TO, fromDateTS),
                                                            EntityCondition.makeCondition("createdDateStamp", EntityOperator.LESS_THAN_EQUAL_TO, toDateTS)
                                                                )
                                                , EntityOperator.AND);
ebayOrderItemList = delegator.findList("EbayOrderItem", ebayOrderItemCond, null, null, null, false);
qtySold = 0;
for (ebayOrderItem in ebayOrderItemList) {
    qtySold += ebayOrderItem.qty;
}
data.qtySold = qtySold;
list.add(data);
rowCount++;
if (rowCount > 500) {
break;
}
}

context.productList = list;
context.productCount = productSearchList.size();

context.productId = productId;

context.ebayPicture = ebayPicture;
context.amazonPicture = amazonPicture;
context.ebayUsListing = ebayUsListing;
context.ebayUkListing = ebayUkListing;
context.ebayAuListing = ebayAuListing;
context.ebayCaListing = ebayCaListing;
context.ebayDeListing = ebayDeListing;
context.ebayFrListing = ebayFrListing;
context.ebayEsListing = ebayEsListing;


