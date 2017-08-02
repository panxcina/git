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


userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
context.ownerGroup = ownerGroup;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);
isSbu = false;
sbuPartyRoleList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("partyId", userLoginPartyId, "roleTypeId", "SBU", "statusId", "PARTY_ENABLED"), null, false);
if (UtilValidate.isNotEmpty(sbuPartyRoleList)) {
    isSbu = true;
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

context.isSbu = isSbu;
context.isAdmin = isAdmin;
context.isPlatform = isPlatform;


productId = parameters.productId;

product = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productId), false);
context.product = product;

statusList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "PM_STATUS"), null, false);
context.statusList = statusList;

platformStatusList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "PLATFORM_STATUS"), null, false);
context.platformStatusList = platformStatusList;

productTypeList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "GUDAO_PRODUCT_TYPE"), UtilMisc.toList("enumCode"), false);
context.productTypeList = productTypeList;

productGroupList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "GUDAO_PRODUCT_GROUP"), UtilMisc.toList("enumCode"), false);
context.productGroupList = productGroupList;

parentCategoryList = delegator.findByAnd("GudaoProductCategory", UtilMisc.toMap("type", "PARENT"), UtilMisc.toList("categoryName"), false);
context.parentCategoryList = parentCategoryList;

childrenCategoryList = [];
for (parentCategory in parentCategoryList) {
    parentCategoryId = parentCategory.categoryId;
    childrenCategoryGV = delegator.findByAnd("GudaoProductCategory", UtilMisc.toMap("type", "CHILDREN", "parentCategoryId", parentCategoryId), UtilMisc.toList("categoryName"), false);
    for (childrenCategory in childrenCategoryGV) {
        data = [:];
        data.displayName = parentCategory.categoryName + " - " + childrenCategory.categoryName;
        data.categoryName = childrenCategory.categoryName;
        childrenCategoryList.add(data);
    }

}
context.childrenCategoryList =  childrenCategoryList;

ebayUsPrice = delegator.findOne("ProductMasterPrice", ["productId" : productId, "platform" : "EBAY", "type" : "CALCULATED", "site" : "US"], false);
context.ebayUsPrice = ebayUsPrice.price;