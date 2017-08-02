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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
/*import java.text.DateFormat;*/
import java.text.SimpleDateFormat;

productId = parameters.productId;
productName = parameters.productName;
supplier = parameters.supplier;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");

userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);

isSbu = false;
sbuPartyRoleList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("partyId", userLoginPartyId, "roleTypeId", "SBU", "statusId", "PARTY_ENABLED"), null, false);
if (UtilValidate.isNotEmpty(sbuPartyRoleList)) {
    isSbu = true;
    ownerGroup = EntityUtil.getFirst(sbuPartyRoleList).officeSiteName;
    context.ownerGroup = ownerGroup;
} else {
    ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
    context.ownerGroup = ownerGroup;
}
isPlatform = false;
platformPartyRoleList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("partyId", userLoginPartyId, "roleTypeId", "PLATFORM", "statusId", "PARTY_ENABLED"), null, false);
if (UtilValidate.isNotEmpty(platformPartyRoleList)) {
    isPlatform = true;
}

isAdmin = false;
if (UtilValidate.isNotEmpty(userLoginAdmin)) {
    isAdmin = true;
}
context.isSbuParameter = isSbu;
context.isAdmin = isAdmin;

pageViewNameInput = parameters.pageViewName;
if (UtilValidate.isEmpty(pageViewNameInput)) {
    if (isSbu) {
        pageViewNameInput = "SBU";
    } else if (isPlatform) {
        pageViewNameInput = "PLATFORM";
    } else {
        pageViewNameInput = "DEFAULT";
    }
}
context.pageViewName = pageViewNameInput;

list = [];
EntityCondition condition = null;
List<EntityCondition> conditionList = FastList.newInstance();

if(UtilValidate.isNotEmpty(productId)) {
    productId =  productId.trim();
    conditionList.add(EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%" + productId + "%"));
}

if (UtilValidate.isNotEmpty(productName)) {
    productName = productName.trim();
    conditionList.add(EntityCondition.makeCondition(UtilMisc.toList(
                                                                    EntityCondition.makeCondition("chineseName", EntityOperator.LIKE, "%" + productName + "%"),
                                                                    EntityCondition.makeCondition("englishName", EntityOperator.LIKE, "%" + productName + "%")
                                                                    )
                                                    , EntityJoinOperator.OR)
                                                    );
}

if (UtilValidate.isNotEmpty(supplier)) {
    supplier = supplier.trim();
    conditionList.add(EntityCondition.makeCondition(UtilMisc.toList(
                                                                    EntityCondition.makeCondition("mainSupplier", EntityOperator.LIKE, "%" + supplier + "%"),
                                                                    EntityCondition.makeCondition("backupSupplier", EntityOperator.LIKE, "%" + supplier + "%")
                                                                    )
                                                    , EntityJoinOperator.OR)
                                                    );
}

if (conditionList.size() > 0) {
    condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
} else {
    condition = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, "IMPOSSIBLEONE");
}

productSearchList = delegator.findList("ProductMaster", condition, null, ["productId ASC"], null, false);

rowCount = 1;
hasCustomSku = false;
for (productList in productSearchList) {
    hasViewPermission = false;
    productId = productList.productId;
    ebayPicture = delegator.findOne("ProductListingStatus", UtilMisc.toMap("productId", productId, "listingStatusTypeId", "EBAY_PICTURE"), false);
    amazonPicture = delegator.findOne("ProductListingStatus", UtilMisc.toMap("productId", productId, "listingStatusTypeId", "AMAZON_PICTURE"), false);
    finePicture = delegator.findOne("ProductListingStatus", UtilMisc.toMap("productId", productId, "listingStatusTypeId", "FINE_PICTURE"), false);
    rivalListing = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalPlatform", "EBAY"), false);
    similarListing = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalPlatform", "EBAY_SIMILAR"), false);
    smtLink = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalPlatform", "SMT"), false);
    wishLink = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalPlatform", "WISH"), false);
    amazonLink = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalPlatform", "AMAZON"), false);
    referenceLink = delegator.findOne("ProductMasterRival", UtilMisc.toMap("productId", productId, "rivalPlatform", "REFERENCE"), false);
    supplierRank = delegator.findOne("SupplierRank", UtilMisc.toMap("supplier", productList.mainSupplier), false);
    supplierPriceList = delegator.findByAnd("SupplierPrice", UtilMisc.toMap("productId", productId, "supplier", productList.mainSupplier, "type", "MIN_QUANTITY"), UtilMisc.toList("minimumOrderValue"), false);

    if (productList.ownerGroup.equals(ownerGroup)) {
        hasViewPermission = true;
    } else if (UtilValidate.isNotEmpty(userLoginAdmin)) {
        hasViewPermission = true;
    } else if (!isSbu) {
        hasViewPermission = true;
    }
    data = [:];
    data.hasViewPermission = hasViewPermission;
    data.productId = productList.productId;
    data.actualPrice = productList.actualPrice;
    data.actualWeight = productList.actualWeight;
    data.backupMinOrderDays = productList.backupMinOrderDays;
    data.backupSupplier = productList.backupSupplier;
    data.backupSupplierEta = productList.backupSupplierEta;
    data.backupSupplierLink = productList.backupSupplierLink;
    data.categoryIdChild = productList.categoryIdChild;
    data.categoryIdParent = productList.categoryIdParent;
    data.chineseName = productList.chineseName;
    data.createdBy = productList.createdBy;
    data.customSku = productList.customSku;
    data.declaredNameCn = productList.declaredNameCn;
    data.declaredNameEn = productList.declaredNameEn;
    data.englishName = productList.englishName;
    data.extraStockingDay = productList.extraStockingDay;
    data.finalEps = productList.finalEps;
    data.finalGms = productList.finalGms;
    data.finalGps = productList.finalGps;
    data.imageLink = productList.imageLink;
    data.listingNotes = productList.listingNotes;
    data.mainMinOrderDays = productList.mainMinOrderDays;
    data.mainSupplier = productList.mainSupplier;
    data.mainSupplierEta = productList.mainSupplierEta;
    data.mainSupplierLink = productList.mainSupplierLink;
    data.motherSku = productList.motherSku;
    data.motherSkuWeight = productList.motherSkuWeight;
    data.notes = productList.notes;
    data.ownerGroup = productList.ownerGroup;
    data.packagingCost = productList.packagingCost;
    data.pictureReady = productList.pictureReady;
    data.productGroup = productList.productGroup;
    data.productType = productList.productType;
    data.purchaser = productList.purchaser;
    data.qcCost = productList.qcCost;
    data.sourcingDate = productList.sourcingDate;
    data.statusId = productList.statusId;
    data.weight = productList.weight;

    priceEbayUs = delegator.findOne("ProductMasterPrice", ["productId" : productId, "platform" : "EBAY", "type" : "CALCULATED", "site" : "US"], false);
    priceEbayUk = delegator.findOne("ProductMasterPrice", ["productId" : productId, "platform" : "EBAY", "type" : "CALCULATED", "site" : "UK"], false);
    priceEbayAu = delegator.findOne("ProductMasterPrice", ["productId" : productId, "platform" : "EBAY", "type" : "CALCULATED", "site" : "AU"], false);
    priceEbayCa = delegator.findOne("ProductMasterPrice", ["productId" : productId, "platform" : "EBAY", "type" : "CALCULATED", "site" : "CA"], false);
    priceEbayDe = delegator.findOne("ProductMasterPrice", ["productId" : productId, "platform" : "EBAY", "type" : "CALCULATED", "site" : "DE"], false);
    priceEbayFr = delegator.findOne("ProductMasterPrice", ["productId" : productId, "platform" : "EBAY", "type" : "CALCULATED", "site" : "FR"], false);
    priceSmtUs = delegator.findOne("ProductMasterPrice", ["productId" : productId, "platform" : "SMT", "type" : "CALCULATED", "site" : "US"], false);
    priceWishUs = delegator.findOne("ProductMasterPrice", ["productId" : productId, "platform" : "WISH", "type" : "CALCULATED", "site" : "US"], false);
    priceAmazonUs = delegator.findOne("ProductMasterPrice", ["productId" : productId, "platform" : "AMAZON", "type" : "CALCULATED", "site" : "US"], false);

    data.ebayUsPrice = priceEbayUs.price;
    data.ebayUkPrice = priceEbayUk.price;
    data.ebayAuPrice = priceEbayAu.price;
    data.ebayCaPrice = priceEbayCa.price;
    data.ebayDePrice = priceEbayDe.price;
    data.ebayFrPrice = priceEbayFr.price;
    data.ebayUsPercentage = priceEbayUs.profitPercentage;
    data.ebayUkPercentage = priceEbayUk.profitPercentage;
    data.ebayAuPercentage = priceEbayAu.profitPercentage;
    data.ebayCaPercentage = priceEbayCa.profitPercentage;
    data.ebayDePercentage = priceEbayDe.profitPercentage;
    data.ebayFrPercentage = priceEbayFr.profitPercentage;
    data.smtUsPrice = priceSmtUs.price;
    data.smtUsPercentage = priceSmtUs.profitPercentage;
    data.wishUsPrice = priceWishUs.price;
    data.wishUsShippingPrice = priceWishUs.shippingPrice;
    data.wishUsPercentage = priceWishUs.profitPercentage;
    data.amazonUsPrice = priceAmazonUs.price;
    data.amazonUsPercentage = priceAmazonUs.profitPercentage;

    if (UtilValidate.isNotEmpty(productList.backupSupplier)) {
        backupSupplierPriceList = delegator.findByAnd("SupplierPrice", UtilMisc.toMap("productId", productId, "supplier", productList.backupSupplier, "type", "MIN_QUANTITY"), UtilMisc.toList("minimumOrderValue"), false);
        if (UtilValidate.isNotEmpty(backupSupplierPriceList)) {
            backupSupplierPriceCount = 1;
            for (backupSupplierPrice in backupSupplierPriceList) {
                if (backupSupplierPriceCount == 1)   {
                    data.backupMoq1 = backupSupplierPrice.minimumOrderValue;
                    data.backupMoq1Price = backupSupplierPrice.unitPrice;
                    data.backupMoIdentifier1 = backupSupplierPrice.identifier;
                } else if (backupSupplierPriceCount == 2){
                    data.backupMoq2 = backupSupplierPrice.minimumOrderValue;
                    data.backupMoq2Price = backupSupplierPrice.unitPrice;
                    data.backupMoIdentifier2 = backupSupplierPrice.identifier;
                } else if (backupSupplierPriceCount == 3){
                    data.backupMoq3 = backupSupplierPrice.minimumOrderValue;
                    data.backupMoq3Price = backupSupplierPrice.unitPrice;
                    data.backupMoIdentifier3 = backupSupplierPrice.identifier;
                }
                backupSupplierPriceCount++;
            }
        }
    }

    if (UtilValidate.isNotEmpty(supplierPriceList)) {
        supplierPriceCount = 1;
        for (supplierPrice in supplierPriceList) {
            if (supplierPriceCount == 1) {
                data.moq1 = supplierPrice.minimumOrderValue;
                data.moq1Price = supplierPrice.unitPrice;
                data.moIdentifier1 = supplierPrice.identifier;
            } else if (supplierPriceCount == 2) {
                data.moq2 = supplierPrice.minimumOrderValue;
                data.moq2Price = supplierPrice.unitPrice;
                data.moIdentifier2 = supplierPrice.identifier;
            } else if (supplierPriceCount == 3) {
                data.moq3 = supplierPrice.minimumOrderValue;
                data.moq3Price = supplierPrice.unitPrice;
                data.moIdentifier3 = supplierPrice.identifier;
            }
            supplierPriceCount++;
        }
    }

    if (UtilValidate.isNotEmpty(ebayPicture)) {
        data.ebayPicture = ebayPicture.value;
    }
    if (UtilValidate.isNotEmpty(amazonPicture)) {
        data.amazonPicture = amazonPicture.value;
    }
    if (UtilValidate.isNotEmpty(finePicture)) {
        data.finePicture = finePicture.value;
    }
    if (UtilValidate.isNotEmpty(rivalListing)) {
        data.rivalItemId = rivalListing.rivalItemId;
    } else {
        if (UtilValidate.isNotEmpty(similarListing)) {
            data.similarItemId = similarListing.similarItemId;
        }
    }
    if (UtilValidate.isNotEmpty(smtLink)) {
        data.smtLink = smtLink.rivalLink;
    }
    if (UtilValidate.isNotEmpty(wishLink)) {
        data.wishLink = wishLink.rivalLink;
    }
    if (UtilValidate.isNotEmpty(amazonLink)) {
        data.amazonLink = amazonLink.rivalLink;
    }
    if (UtilValidate.isNotEmpty(referenceLink)) {
        data.referenceLink = referenceLink.rivalLink;
    }
    if (UtilValidate.isNotEmpty(supplierRank)) {
        data.supplierRank = supplierRank.rank;
    }
    if (UtilValidate.isNotEmpty(productList.customSku)) {
        hasCustomSku = true;
    }

    hasListingInfo = false;
    productKeywordSeed = delegator.findByAnd("ProductKeywordSeed", UtilMisc.toMap("productId", productId), null, false);
    productDescription = delegator.findByAnd("ProductDescription", UtilMisc.toMap("productId", productId), null, false);

    if (UtilValidate.isNotEmpty(productKeywordSeed)) {
        hasListingInfo = false;
    }

    if (UtilValidate.isNotEmpty(productDescription)) {
        hasListingInfo = false;
    }
    data.hasListingInfo = hasListingInfo;

    list.add(data);
    rowCount++;
    if (rowCount > 500) {
    break;
    }
}

context.productSearchList = list;
context.productCount = productSearchList.size();
context.hasCustomSku = hasCustomSku;


