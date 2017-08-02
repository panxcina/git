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

orderId = parameters.orderId;
userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");

statusList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "PM_STATUS"), null, false);
platformStatusList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "PLATFORM_STATUS"), null, false);
productGroupList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "GUDAO_PRODUCT_GROUP"), null, false);
productTypeList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId", "GUDAO_PRODUCT_TYPE"), null, false);

parentCategoryList = delegator.findByAnd("GudaoProductCategory", UtilMisc.toMap("type", "PARENT"), UtilMisc.toList("categoryName"), false);

categoryList = [];
for (parentCategory in parentCategoryList) {
parentData = [:];
    parentCategoryId = parentCategory.categoryId;
    childrenCategoryGV = delegator.findByAnd("GudaoProductCategory", UtilMisc.toMap("type", "CHILDREN", "parentCategoryId", parentCategoryId), UtilMisc.toList("categoryName"), false);
    childrenCategoryList = [];
    for (childrenCategory in childrenCategoryGV) {
        data = [:];
        data.displayName = parentCategory.categoryName + " - " + childrenCategory.categoryName;
        data.categoryName = childrenCategory.categoryName;
        childrenCategoryList.add(data);
    }
    parentData.parentCategoryName = parentCategory.categoryName;
    parentData.childrenSize = childrenCategoryList.size();
    parentData.childrenCategoryList = childrenCategoryList;
    categoryList.add(parentData);
}

context.statusList = statusList;
context.platformStatusList = platformStatusList;
context.productGroupList = productGroupList;
context.productTypeList = productTypeList;
context.categoryList = categoryList;

