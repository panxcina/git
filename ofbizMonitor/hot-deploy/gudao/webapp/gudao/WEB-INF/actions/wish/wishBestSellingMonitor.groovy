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

import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
/*import java.sql.Date;
import java.text.DateFormat;*/
import java.text.SimpleDateFormat;
import javolution.util.FastMap;
import javolution.util.FastList;

Calendar nowCal = Calendar.getInstance();
java.sql.Date nowDateSql = new java.sql.Date(nowCal.getTime().getTime());
defaultTotalMonitorDay = 7;
totalMonitorDay = parameters.totalMonitorDay;
if (UtilValidate.isEmpty(totalMonitorDay)) {
    totalMonitorDay = defaultTotalMonitorDay;
}
context.totalMonitorDay = totalMonitorDay;

productStoreIdInput = parameters.productStoreIdInput;
if (UtilValidate.isEmpty(productStoreIdInput)) {
    productStoreIdInput = "INITIALLOAD";
}
context.productStoreIdInput = productStoreIdInput;

productStoreList = delegator.findByAnd("ProductStore", ["companyName" : "Gudao", "primaryStoreGroupId" : "WISH", "isDemoStore" : "N"], null, false);
context.productStoreList = productStoreList;

EntityCondition condition = null;
List<EntityCondition> conditionList = FastList.newInstance();

conditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));

if (UtilValidate.isNotEmpty(productStoreIdInput)) {
    if (!productStoreIdInput.equals("ALL")) {
        conditionList.add(EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStoreIdInput));
    }
} else  {
    conditionList.add(EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, "EMPTYPRODUCTSTORE"));
}

condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);

productStoreResultList = delegator.findList("WishBestSellingList", condition, UtilMisc.toSet("productStoreId"), ["productStoreId"], new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);

resultList = [];
for (productStoreResult in productStoreResultList) {
    productStoreResultData = [:];
    productStoreId = productStoreResult.productStoreId;
    wishBestSellingList = delegator.findByAnd("WishBestSellingList", ["productStoreId" : productStoreId], null, false);
    bestSellingList = [];
    for (wishBestSelling in wishBestSellingList) {
        bestSellingData = [:];
        wishId = wishBestSelling.wishId;
        sellingDataList = [];

        distinctBestSellingHeaderDate = delegator.findList("WishBestSellingHeader", EntityCondition.makeCondition("wishId", EntityOperator.EQUALS, wishId), UtilMisc.toSet("date"), ["date DESC"], new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
        queryDateList = [];
        i = 1;
        for (queryDateSingle in distinctBestSellingHeaderDate) {
            queryDateList.add(queryDateSingle);
            if (i >= Integer.parseInt(totalMonitorDay)) {
                break;
            }
            i++;
        }
        queryDateList.sort{it.date};

        previousSold=0;
        previousSave=0;
        name = null;
        parentSku = null;
        mainImage = null;
        extraImages = null;
        reviewStatus = null;
        upc = null;
        dateUploaded = null;
        activeSweep = null;
        aspectRatio = null;
        canGift = null;
        canPromo = null;
        fromTrustedStore = null;
        gender = null;
        hasReward = null;
        isClean = null;
        isDirty = null;
        isFulfillByWish = null;
        isVerified = null;
        merchantRating = null;
        merchantRatingClass = null;
        merchantRatingCount = null;
        numBought = null;
        numSaves = null;
        numSold = null;
        productRating = null;
        productRatingClass = null;
        productRatingCount = null;
        shipsFrom = null;
        lastUpdatedStamp = null;
        minOriginalPrice = 0.0;
        maxOriginalPrice = 0.0;
        minOriginalShipping = 0.0;
        maxOriginalShipping = 0.0;

        for (bestSellingHeaderDate in queryDateList) {
            queryDate = bestSellingHeaderDate.date;
            sellingData = [:];
            sellingData.date = queryDate;

            sellingHeaderData = [:];
            bestSellingHeader = delegator.findOne("WishBestSellingHeader", ["productStoreId" : productStoreId, "wishId" : wishId, "date" : queryDate], false);
            if (UtilValidate.isNotEmpty(bestSellingHeader)) {
                if (previousSold != 0) {
                    dailySold = bestSellingHeader.numberSold - previousSold;
                } else {
                    dailySold = 0;
                }
                if (previousSave != 0) {
                    dailySaves = bestSellingHeader.numberSaves - previousSave;
                } else {
                    dailySaves = 0;
                }

                sellingHeaderData.numberSold = bestSellingHeader.numberSold;
                sellingHeaderData.numberSaves = bestSellingHeader.numberSaves;
                sellingHeaderData.isPromoted = bestSellingHeader.isPromoted;
                sellingHeaderData.dailySold = dailySold;
                sellingHeaderData.dailySaves = dailySaves;
                previousSold = bestSellingHeader.numberSold;
                previousSave = bestSellingHeader.numberSaves;
                if (bestSellingHeader.numberSold > 0) {
                    conversionRate = bestSellingHeader.numberSold / bestSellingHeader.numberSaves;
                } else {
                    conversionRate = 0;
                }
                if (dailySaves > 0) {
                    dailyConversionRate = dailySold / dailySaves;
                } else {
                    dailyConversionRate = 0;
                }
                name = bestSellingHeader.name;
                parentSku = bestSellingHeader.parentSku;
                mainImage = bestSellingHeader.mainImage;
                extraImages = bestSellingHeader.extraImages;
                reviewStatus = bestSellingHeader.reviewStatus;
                upc = bestSellingHeader.upc;
                dateUploaded = bestSellingHeader.dateUploaded;
                activeSweep = bestSellingHeader.activeSweep;
                aspectRatio = bestSellingHeader.aspectRatio;
                canGift = bestSellingHeader.canGift;
                canPromo = bestSellingHeader.canPromo;
                fromTrustedStore = bestSellingHeader.fromTrustedStore;
                gender = bestSellingHeader.gender;
                hasReward = bestSellingHeader.hasReward;
                isClean = bestSellingHeader.isClean;
                isDirty = bestSellingHeader.isDirty;
                isFulfillByWish = bestSellingHeader.isFulfillByWish;
                isVerified = bestSellingHeader.isVerified;
                merchantRating = bestSellingHeader.merchantRating;
                merchantRatingClass = bestSellingHeader.merchantRatingClass;
                merchantRatingCount = bestSellingHeader.merchantRatingCount;
                numBought = bestSellingHeader.numBought;
                numSaves = bestSellingHeader.numberSaves;
                numSold = bestSellingHeader.numberSold;
                productRating = bestSellingHeader.productRating;
                productRatingClass = bestSellingHeader.productRatingClass;
                productRatingCount = bestSellingHeader.productRatingCount;
                shipsFrom = bestSellingHeader.shipsFrom;
                lastUpdatedStamp = bestSellingHeader.lastUpdatedStamp;
                minOriginalPrice = bestSellingHeader.minOriginalPrice;
                maxOriginalPrice = bestSellingHeader.maxOriginalPrice;
                minOriginalShipping = bestSellingHeader.minOriginalShipping;
                maxOriginalShipping = bestSellingHeader.maxOriginalShipping;

                sellingHeaderData.conversionRate = conversionRate;
                sellingHeaderData.dailyConversionRate = dailyConversionRate;

                sellingHeaderData.activeSweep = bestSellingHeader.activeSweep;
                sellingHeaderData.aspectRatio = bestSellingHeader.aspectRatio;
                sellingHeaderData.brand = bestSellingHeader.brand;
                sellingHeaderData.canGift = bestSellingHeader.canGift;
                sellingHeaderData.canPromo = bestSellingHeader.canPromo;
                sellingHeaderData.commentCount = bestSellingHeader.commentCount;
                sellingHeaderData.fbwActive = bestSellingHeader.fbwActive;
                sellingHeaderData.fbwPending = bestSellingHeader.fbwPending;
                sellingHeaderData.firstRatingImageIndex = bestSellingHeader.firstRatingImageIndex;
                sellingHeaderData.fromTrustedStore = bestSellingHeader.fromTrustedStore;
                sellingHeaderData.gender = bestSellingHeader.gender;
                sellingHeaderData.hasReward = bestSellingHeader.hasReward;
                sellingHeaderData.isActive = bestSellingHeader.isActive;
                sellingHeaderData.isActiveFbwInUs = bestSellingHeader.isActiveFbwInUs;
                sellingHeaderData.isAdminUser = bestSellingHeader.isAdminUser;
                sellingHeaderData.isClean = bestSellingHeader.isClean;
                sellingHeaderData.isConcept = bestSellingHeader.isConcept;
                sellingHeaderData.isContentReviewer = bestSellingHeader.isContentReviewer;
                sellingHeaderData.isDeleted = bestSellingHeader.isDeleted;
                sellingHeaderData.isDirty = bestSellingHeader.isDirty;
                sellingHeaderData.isExpired = bestSellingHeader.isExpired;
                sellingHeaderData.isFulfillByWish = bestSellingHeader.isFulfillByWish;
                sellingHeaderData.isVerified = bestSellingHeader.isVerified;
                sellingHeaderData.numBought = bestSellingHeader.numBought;
                sellingHeaderData.numEntered = bestSellingHeader.numEntered;
                sellingHeaderData.numWishes = bestSellingHeader.numWishes;
                sellingHeaderData.numWon = bestSellingHeader.numWon;
                sellingHeaderData.numContestPhotos = bestSellingHeader.numContestPhotos;
                sellingHeaderData.numExtraPhotos = bestSellingHeader.numExtraPhotos;
                sellingHeaderData.productRating = bestSellingHeader.productRating;
                sellingHeaderData.productRatingClass = bestSellingHeader.productRatingClass;
                sellingHeaderData.productRatingCount = bestSellingHeader.productRatingCount;
                sellingHeaderData.removed = bestSellingHeader.removed;
                sellingHeaderData.requiresReview = bestSellingHeader.requiresReview;
                sellingHeaderData.shipsFrom = bestSellingHeader.shipsFrom;
                sellingHeaderData.sourceCountry = bestSellingHeader.sourceCountry;
                sellingHeaderData.uploadSource = bestSellingHeader.uploadSource;
                sellingHeaderData.userInActiveSweep = bestSellingHeader.userInActiveSweep;
                sellingHeaderData.value = bestSellingHeader.value;
                sellingHeaderData.merchantRating = bestSellingHeader.merchantRating;
                sellingHeaderData.merchantRatingClass = bestSellingHeader.merchantRatingClass;
                sellingHeaderData.merchantRatingCount = bestSellingHeader.merchantRatingCount;
                sellingHeaderData.minOriginalPrice = bestSellingHeader.minOriginalPrice;
                sellingHeaderData.maxOriginalPrice = bestSellingHeader.maxOriginalPrice;
                sellingHeaderData.minOriginalShipping = bestSellingHeader.minOriginalShipping;
                sellingHeaderData.maxOriginalShipping = bestSellingHeader.maxOriginalShipping;

            }
            sellingData.headerData = sellingHeaderData;
            sellingDataList.add(sellingData);
        }

        extraImagesList = [];
        if (UtilValidate.isNotEmpty(extraImages)) {
            extraImagesArray= extraImages.split("\\|");
            for (extraImage in extraImagesArray) {
                extraImagesList.add(extraImage);
            }
        }

        bestSellingTags = delegator.findByAnd("WishBestSellingTags", ["wishId" : wishId, "productStoreId" : productStoreId], null, false);

        bestSellingData.wishId = wishId;
        bestSellingData.name = name;
        bestSellingData.parentSku = parentSku;
        bestSellingData.mainImage = mainImage;
        bestSellingData.extraImages = extraImagesList;
        bestSellingData.reviewStatus = reviewStatus;
        bestSellingData.upc = upc;
        bestSellingData.dateUploaded = dateUploaded;
        bestSellingData.sellingDataList = sellingDataList;
        bestSellingData.tagsList = bestSellingTags;
        bestSellingData.tagsCount = bestSellingTags.size();

        bestSellingData.activeSweep = activeSweep;
        bestSellingData.aspectRatio = aspectRatio;
        bestSellingData.canGift = canGift;
        bestSellingData.canPromo = canPromo;
        bestSellingData.fromTrustedStore = fromTrustedStore;
        bestSellingData.gender = gender;
        bestSellingData.hasReward = hasReward;
        bestSellingData.isClean = isClean;
        bestSellingData.isDirty = isDirty;
        bestSellingData.isFulfillByWish = isFulfillByWish;
        bestSellingData.isVerified = isVerified;
        bestSellingData.merchantRating = merchantRating;
        bestSellingData.merchantRatingClass = merchantRatingClass;
        bestSellingData.merchantRatingCount = merchantRatingCount;
        bestSellingData.numBought = numBought;
        bestSellingData.numSaves = numSaves;
        bestSellingData.numSold = numSold;
        bestSellingData.productRating = productRating;
        bestSellingData.productRatingClass = productRatingClass;
        bestSellingData.productRatingCount = productRatingCount;
        bestSellingData.shipsFrom = shipsFrom;
        bestSellingData.lastUpdatedStamp = lastUpdatedStamp;
        bestSellingData.minOriginalPrice = minOriginalPrice;
        bestSellingData.maxOriginalPrice = maxOriginalPrice;
        bestSellingData.minOriginalShipping = minOriginalShipping;
        bestSellingData.maxOriginalShipping = maxOriginalShipping;

        bestSellingList.add(bestSellingData);
    }

    productStoreResultData.productStoreId = productStoreId;
    productStoreResultData.bestSellingList = bestSellingList;
    resultList.add(productStoreResultData);
}
context.resultList = resultList;