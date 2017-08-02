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

import javolution.util.FastMap;
import javolution.util.FastList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.ComparatorUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
/*import java.text.DateFormat;*/
import java.text.SimpleDateFormat;

productId = parameters.productId;
productName = parameters.productName;
ownerGroupInput = parameters.ownerGroupInput;
listerInput = parameters.lister;
listerPlatformInput = parameters.listerPlatform;
viewSize = parameters.viewSize;
if (viewSize.equals("ALL")) {
viewSize = "99999";
}

if (UtilValidate.isEmpty(productId) && UtilValidate.isEmpty(productName) && UtilValidate.isEmpty(ownerGroupInput) && UtilValidate.isEmpty(listerInput) && UtilValidate.isEmpty(listerPlatformInput) ) {
productId = "impossible";
}

userLogin = parameters.userLogin;
userLoginIdDefault = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
context.ownerGroup = ownerGroup;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginIdDefault, "groupId", "GUDAO-ADMIN"), null, false);

List<EntityCondition> listerCondsList = FastList.newInstance();

if (UtilValidate.isNotEmpty(listerInput)) {
    listerCondsList.add(EntityCondition.makeCondition("lister", EntityOperator.EQUALS, listerInput));
}

if (UtilValidate.isNotEmpty(listerPlatformInput)) {
    listerCondsList.add(EntityCondition.makeCondition("listerPlatform", EntityOperator.EQUALS, listerPlatformInput));
}

if (UtilValidate.isNotEmpty(productId)) {
    listerCondsList.add(EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%" + productId.trim() + "%"));
}

EntityCondition listerCond = null;
if (listerCondsList.size() > 0) {
    listerCond = EntityCondition.makeCondition(listerCondsList, EntityOperator.AND);
}

productListers = delegator.findList("ProductLister", listerCond, null, UtilMisc.toList("productId"), null, false);

list=[];
count = 0;

for (productLister in productListers) {

    productIdVar = productLister.productId;
    List<EntityCondition> pmCondsList = FastList.newInstance();

    pmCondsList.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productIdVar));
    if (UtilValidate.isNotEmpty(ownerGroupInput)) {
        pmCondsList.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroupInput));
    }

    if (UtilValidate.isNotEmpty(productName)) {
        pmCondsList.add(EntityCondition.makeCondition("chineseName", EntityOperator.LIKE, "%" + productName.trim() + "%"));
    }

    EntityCondition pmCond = null;
    if (pmCondsList.size() > 0) {
        pmCond = EntityCondition.makeCondition(pmCondsList, EntityOperator.AND);
    }

    pmList = delegator.findList("ProductMaster", pmCond, null, UtilMisc.toList("productId"), null, false);
    for (pm in pmList) {
        if (count >= Integer.parseInt(viewSize)) {
            break;
        }
        data = [:];
        statusId = pm.statusId;
        productListingStatus = delegator.findOne("ProductListingStatus", UtilMisc.toMap("productId", productIdVar, "listingStatusTypeId", "EBAY_PICTURE"), false);
        readyToList = productListingStatus.value;
        containsEbay = false;
        pmPlatform = pm.platform;
        if (pmPlatform.contains("E")) {
            containsEbay = true;
        }
        if ((statusId.equals("ACTIVE") || statusId.equals("SETSKU")) && containsEbay && readyToList.equals("Y")) {
            productGmsList = null;
            if (UtilValidate.isNotEmpty(listerPlatformInput)) {
                listerPlatformQuery = "EBAY";
                platformSite = "US";
                if (listerPlatformInput.startsWith("AMAZON")) {
                    listerPlatformQuery= "AMAZON";
                } else if (listerPlatformInput.startsWith("WISH")) {
                    listerPlatformQuery= "WISH";
                } else if (listerPlatformInput.startsWith("SMT")) {
                    listerPlatformQuery= "SMT";
                } else if (listerPlatformInput.startsWith("EBAY")) {
                    listerPlatformQuery= "EBAY";
                    if (listerPlatformInput.endsWith("DE")) {
                        platformSite = "DE";
                    } else if (listerPlatformInput.endsWith("FR")) {
                        platformSite = "FR";
                    }
                }

                productGmsList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productIdVar, "platform", listerPlatformQuery, "site", platformSite), null, false);
            } else {
                productGmsList = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productIdVar, "platform", "EBAY", "site", "US"), null, false);
            }

            if (UtilValidate.isEmpty(productGmsList)) {
                count++;
                ebayUsPrice = delegator.findOne("ProductMasterPrice", ["productId" : productIdVar, "platform" : "EBAY", "type" : "CALCULATED", "site" : "US"], false);
                data.productId = productIdVar;
                data.chineseName = pm.chineseName;
                data.weight = pm.weight;
                data.productGroup = pm.productGroup;
                data.statusId = pm.statusId;
                data.categoryIdParent = pm.categoryIdParent;
                data.categoryIdChild = pm.categoryIdChild;
                data.ownerGroup = pm.ownerGroup;
                data.notes = pm.notes;
                data.risk = pm.risk;
                data.priceUsd = ebayUsPrice.price;
                data.cpPercentage = ebayUsPrice.profitPercentage;
                data.readyToList = readyToList;
                data.supplier = pm.mainSupplier;
                data.supplierLink = pm.mainSupplierLink;
                data.price = pm.actualPrice;
                data.finalGps = pm.finalGps;
                rivalListing = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productIdVar, "rivalPlatform", "EBAY"), false);
                smtLink = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productIdVar, "rivalPlatform", "SMT"), false);
                referenceLink = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productIdVar, "rivalPlatform", "REFERENCE"), false);
                amazonLink = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productIdVar, "rivalPlatform", "AMAZON"), false);
                if (UtilValidate.isNotEmpty(rivalListing)) {
                    data.rivalItemId = rivalListing.rivalItemId;
                }
                if (UtilValidate.isNotEmpty(smtLink)) {
                    data.smtLink = smtLink.rivalLink;
                }
                if (UtilValidate.isNotEmpty(referenceLink)) {
                    data.referenceLink = referenceLink.rivalLink;
                }
                if (UtilValidate.isNotEmpty(amazonLink)) {
                    data.amazonLink = amazonLink.rivalLink;
                }
                data.lister = productLister.lister;
                data.listerPlatform = productLister.listerPlatform;
                list.add(data);
            }
        }
    }

}

//Collections.sort(list, ComparatorUtils.chainedComparator([list.productId, list.categoryIdChild, list.categoryIdParent]));


list.sort{it.productId};
context.productSearchList = list;
context.productCount = count;







