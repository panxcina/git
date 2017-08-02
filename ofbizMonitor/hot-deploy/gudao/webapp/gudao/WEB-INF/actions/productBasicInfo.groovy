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
import com.gudao.product.productService;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
boolean hasUpdatePermission = false;
if (ownerGroup.equals("MARKETING")) {
    hasUpdatePermission = true;
}
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);
if (UtilValidate.isNotEmpty(userLoginAdmin)) {
    hasUpdatePermission = true;
}
context.hasUpdatePermission = hasUpdatePermission;

productId = parameters.productId;
language = parameters.language;
if (UtilValidate.isEmpty(language)) {
language = "en";
}

feature = delegator.findOne("ProductDescription", UtilMisc.toMap("productId", productId, "type", "FEATURE", "language", language), false);
if (UtilValidate.isEmpty(feature)) {
    product = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
    if (UtilValidate.isNotEmpty(product)) {
        if (UtilValidate.isNotEmpty(product.motherSku)) {
            productId = product.motherSku;
        }
    }
}

context.productId = productId;
context.language = language;

feature = delegator.findOne("ProductDescription", UtilMisc.toMap("productId", productId, "type", "FEATURE", "language", language), false);
context.feature = feature;

specification = delegator.findOne("ProductDescription", UtilMisc.toMap("productId", productId, "type", "SPECIFICATION", "language", language), false);
context.specification = specification;

packageIncludes = delegator.findOne("ProductDescription", UtilMisc.toMap("productId", productId, "type", "PACKAGE", "language", language), false);
context.packageIncludes = packageIncludes;


titleResult = dispatcher.runSync("gudaoTitleGenerator", UtilMisc.toMap("productId", productId, "language", language, "platform", "EBAY", "userLogin", userLogin));
context.title = titleResult.title;
context.titleLength = titleResult.titleLength;

smtTitleResult = dispatcher.runSync("gudaoTitleGenerator", UtilMisc.toMap("productId", productId, "language", language, "platform", "SMT", "userLogin", userLogin));
context.smtTitle = smtTitleResult.title;
context.smtTitleLength = smtTitleResult.titleLength;

mainKeywordList = delegator.findByAnd("ProductKeywordSeed", UtilMisc.toMap("productId", productId, "type", "MAIN", "language", language), UtilMisc.toList("keyword"), false);
context.mainKeywordList = mainKeywordList;

secondaryKeywordList = delegator.findByAnd("ProductKeywordSeed", UtilMisc.toMap("productId", productId, "type", "SECONDARY", "language", language), UtilMisc.toList("keyword"), false);
context.secondaryKeywordList = secondaryKeywordList;

tertiaryKeywordList = delegator.findByAnd("ProductKeywordSeed", UtilMisc.toMap("productId", productId, "type", "TERTIARY", "language", language), UtilMisc.toList("keyword"), false);
context.tertiaryKeywordList = tertiaryKeywordList;

if (UtilValidate.isNotEmpty(feature)) {
featureArray = feature.content.split("\\n");
featureData=[];
for (int k = 0; k < featureArray.length; k++) {
    if (UtilValidate.isNotEmpty(featureArray[k])) {
        featureData.add(featureArray[k].trim());
    }
}
featureData.removeAll(Collections.singleton(""));
context.featureArray = featureData;

specificationArray = specification.content.split("\\n");
specificationData=[];
for (int k = 0; k < specificationArray.length; k++) {
if (UtilValidate.isNotEmpty(specificationArray[k])) {
specificationData.add(specificationArray[k].trim());
}
}
specificationData.removeAll(Collections.singleton(""));
context.specificationArray = specificationData;

packageArray = packageIncludes.content.split("\\n");
packageData=[];
for (int k = 0; k < packageArray.length; k++) {
if (UtilValidate.isNotEmpty(packageArray[k])) {
packageData.add(packageArray[k].trim());
}
}
packageData.removeAll(Collections.singleton(""));
context.packageArray = packageData;
}
