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
import javolution.util.FastList;
/*import java.text.DateFormat;*/
import java.text.SimpleDateFormat;


productId = parameters.productId;
productName = parameters.productName;
supplier = parameters.supplier;
platformInput = parameters.platform;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
userLoginPartyGroup = userLogin.getRelatedOne("PartyGroup", false);
ownerGroup = userLoginPartyGroup.officeSiteName;
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

if (UtilValidate.isNotEmpty(userLoginAdmin)) {
    isSbu = true;
}
context.isSbuParameter = isSbu;

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

List<EntityCondition> platformConditionList = FastList.newInstance();

if (UtilValidate.isNotEmpty(platformInput)) {
    platformConditionList.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, platformInput));
}
platformConditionList.add(EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "PLATFORM"));
platformConditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "PARTY_ENABLED"));

platformCondition = EntityCondition.makeCondition(platformConditionList, EntityOperator.AND);

allPlatformList = delegator.findList("PartyRoleDetailAndPartyDetail", platformCondition, null, ["groupName ASC"], null, false);

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
for (productList in productSearchList) {
    data = [:];
    hasViewPermission = false;
    productId = productList.productId;
    if (productList.ownerGroup.equals(ownerGroup)) {
        hasViewPermission = true;
    } else if (UtilValidate.isNotEmpty(userLoginAdmin)) {
        hasViewPermission = true;
    } else if (!isSbu) {
        hasViewPermission = true;
    }

    data.productId = productList.productId;
    data.chineseName = productList.chineseName;
    data.statusId = productList.statusId;
    data.ownerGroup = productList.ownerGroup;
    data.hasViewPermission = hasViewPermission;

    groupList = [];
    for (sbuPartyRole in allPlatformList) {
        groupData = [:];
        partyGroupName = sbuPartyRole.groupName;
        partyGroupId = sbuPartyRole.partyId;
        partyGroupPlatformList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("partyId", partyGroupId, "statusId", "PARTY_ENABLED", "parentTypeId", "PLATFORM"), null, false);
        groupPlatformList = [];
        for (partyGroupPlatform in partyGroupPlatformList) {
            pmStatusData = [:];
            pmStatus = delegator.findOne("ProductMasterStatus", UtilMisc.toMap("productId", productId, "department", partyGroupId, "platform", partyGroupPlatform.roleTypeId), false);
            pmGroup = delegator.findOne("ProductMasterGroup", UtilMisc.toMap("productId", productId, "department", partyGroupId, "platform", partyGroupPlatform.roleTypeId), false);
            pmStatusData.platform = partyGroupPlatform.roleTypeId;
            if (UtilValidate.isNotEmpty(pmStatus)) {
                if (UtilValidate.isNotEmpty(pmGroup)) {
                    pmStatusData.statusId = pmGroup.productGroup + " " + pmStatus.statusId;
                } else {
                    pmStatusData.statusId = pmStatus.statusId;
                }
            }
            groupPlatformList.add(pmStatusData);
        }
        groupData.groupName = partyGroupName;
        groupData.groupPlatformList = groupPlatformList;
        groupList.add(groupData);
    }
    data.groupList = groupList;

    list.add(data);
    rowCount++;
    if (rowCount > 500) {
    break;
    }
}

context.productSearchList = list;
context.productCount = productSearchList.size();


