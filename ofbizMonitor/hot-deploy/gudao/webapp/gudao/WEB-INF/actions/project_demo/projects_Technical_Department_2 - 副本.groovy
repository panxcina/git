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

treeType = parameters.treeType;
if (UtilValidate.isEmpty(parameters.treeType)) {
    treeType = "DEPARTMENT";
}

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
context.userLoginId = userLoginId;

Calendar now = Calendar.getInstance();
java.sql.Date nowTs = new java.sql.Date(now.getTimeInMillis());

businessTreeId = parameters.businessTreeId;
businessTreeType = parameters.businessTreeType;
statusId = parameters.statusId;


if (UtilValidate.isEmpty(statusId)) {
    statusId = "ACTIVE";
}
context.statusId = statusId;
context.businessTreeId = businessTreeId;
context.businessTreeType = businessTreeType;
// mercuryList = delegator.findByAnd("MercuryHeader", ["businessTreeId" : businessTreeId, "businessTreeType" : businessTreeType, "createdBy" : userLoginId, "statusId" : statusId], ["title"], false);
mercuryList = delegator.findByAnd("MercuryHeader", ["businessTreeId" : businessTreeId, "businessTreeType" : businessTreeType, "createdBy" : userLoginId, "statusId" : statusId], ["title"], false);

list=[];
for (mercuryHeader in mercuryList) {
    data=[:];
    data.mercuryId = mercuryHeader.mercuryId;
    data.businessTreeId = mercuryHeader.businessTreeId;
    data.businessTreeType = mercuryHeader.businessTreeType;
    data.title = mercuryHeader.title;
    data.createdBy = mercuryHeader.createdBy;
    data.statusId = mercuryHeader.statusId;
    data.startDate = mercuryHeader.startDate;
    data.completeDateEta = mercuryHeader.completeDateEta;
    data.completedDate = mercuryHeader.completedDate;
    data.description = mercuryHeader.description;

    hasPermission = false;
    if (mercuryHeader.createdBy.equals(userLoginId)) {
        hasPermission = true;
    }
    data.hasPermission = hasPermission;

    EntityCondition condition = null;
    List<EntityCondition> conditionList = FastList.newInstance();
    conditionList.add(EntityCondition.makeCondition("mercuryId", EntityOperator.EQUALS, mercuryHeader.mercuryId));
    conditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "COMPLETED"));
    condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
    mercuryItemListCompleteCount = delegator.findCountByCondition("MercuryItem", condition, null, null);

    mercuryItemList = delegator.findByAnd("MercuryItem", ["mercuryId" : mercuryHeader.mercuryId], null,false);
    data.completeCount = mercuryItemListCompleteCount;
    data.totalCount = mercuryItemList.size();

    overdue = false;
    lastEta = null;
    for (mercuryItem in mercuryItemList) {
        if (UtilValidate.isNotEmpty(mercuryItem.completeDateEta)) {
            if (mercuryItem.completeDateEta < nowTs) {
                if (!mercuryItem.statusId.equals("COMPLETED")) {
                    overdue = true;
                }
            }
        }
        if (lastEta <= mercuryItem.completeDateEta) {
            lastEta = mercuryItem.completeDateEta;
        }
    }

    hasEta = true;
    if (UtilValidate.isEmpty(lastEta)) {
        hasEta = false;
    }

    data.hasEta = hasEta;
    data.lastEta = lastEta;
    data.overdue = overdue;

    list.add(data);
}

EntityCondition otherCondition = null;
List<EntityCondition> otherConditionList = FastList.newInstance();

otherConditionList.add(EntityCondition.makeCondition("businessTreeId", EntityOperator.EQUALS, businessTreeId));
otherConditionList.add(EntityCondition.makeCondition("businessTreeType", EntityOperator.EQUALS, businessTreeType));
otherConditionList.add(EntityCondition.makeCondition("createdBy", EntityOperator.NOT_EQUAL, userLoginId));
otherConditionList.add(EntityCondition.makeCondition("createdBy", EntityOperator.EQUALS, statusId));
otherCondition = EntityCondition.makeCondition(otherConditionList, EntityOperator.AND);

mercuryHeaderList = delegator.findList("MercuryHeader", otherCondition, null, ["title"], null, false);

for (mercuryHeader in mercuryHeaderList) {
    data=[:];
    data.mercuryId = mercuryHeader.mercuryId;
    data.businessTreeId = mercuryHeader.businessTreeId;
    data.businessTreeType = mercuryHeader.businessTreeType;
    data.title = mercuryHeader.title;
    data.createdBy = mercuryHeader.createdBy;
    data.statusId = mercuryHeader.statusId;
    data.startDate = mercuryHeader.startDate;
    data.completeDateEta = mercuryHeader.completeDateEta;
    data.completedDate = mercuryHeader.completedDate;
    data.description = mercuryHeader.description;

    hasPermission = false;
    if (mercuryHeader.createdBy.equals(userLoginId)) {
        hasPermission = true;
    }
    data.hasPermission = hasPermission;

    EntityCondition condition = null;
    List<EntityCondition> conditionList = FastList.newInstance();
    conditionList.add(EntityCondition.makeCondition("mercuryId", EntityOperator.EQUALS, mercuryHeader.mercuryId));
    conditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "COMPLETED"));
    condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
    mercuryItemListCompleteCount = delegator.findCountByCondition("MercuryItem", condition, null, null);

    mercuryItemList = delegator.findByAnd("MercuryItem", ["mercuryId" : mercuryHeader.mercuryId], null,false);
    data.completeCount = mercuryItemListCompleteCount;
    data.totalCount = mercuryItemList.size();

    overdue = false;
    lastEta = null;
    for (mercuryItem in mercuryItemList) {
        if (UtilValidate.isNotEmpty(mercuryItem.completeDateEta)) {
            if (mercuryItem.completeDateEta < nowTs) {
                if (!mercuryItem.statusId.equals("COMPLETED")) {
                    overdue = true;
                }
            }
        }
        if (lastEta <= mercuryItem.completeDateEta) {
            lastEta = mercuryItem.completeDateEta;
        }
    }

    hasEta = true;
    if (UtilValidate.isEmpty(lastEta)) {
        hasEta = false;
    }

    data.hasEta = hasEta;
    data.lastEta = lastEta;
    data.overdue = overdue;

    list.add(data);
}

context.mercuryList = list;

userLoginList = delegator.findList("MercuryHeader", null, null, null, null, false);
// userLoginList = delegator.findList("MercuryHeader", null, UtilMisc.toSet("createdBy"), UtilMisc.toList("createdBy"), new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
context.userLoginList = userLoginList;

isAdmin = false;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);
if (UtilValidate.isNotEmpty(userLoginAdmin)) {
    isAdmin = true;
}
context.isAdmin = isAdmin;

isHeadquarter = false;
if (userLoginPartyId.equals("HEADQUARTER")) {
    isHeadquarter = true;
    if (UtilValidate.isEmpty(parameters.treeType))  {
        treeType = "DEPARTMENT";
    }
}
context.isHeadquarter = isHeadquarter;
context.treeType = treeType;

filterInput = parameters.filter;
if (UtilValidate.isEmpty(filterInput)) {
    filterInput = userLoginId;
}
context.filterInput = filterInput;

List<EntityCondition> businessTreeCondList = FastList.newInstance();
businessTreeCondList.add(EntityCondition.makeCondition("parentId", EntityOperator.EQUALS, null));
businessTreeCondList.add(EntityCondition.makeCondition("type", EntityOperator.EQUALS, treeType));
EntityCondition businessTreeCond = EntityCondition.makeCondition(businessTreeCondList, EntityOperator.AND);


businessTreeList = delegator.findList("BusinessTree", businessTreeCond, null, ["businessTreeId"], null, false);
businessList = [];
allChildListSize = 0;
for (businessTreeParent in businessTreeList) {
    data = [:];
    parentId = businessTreeParent.businessTreeId;
    businessTreeChild = delegator.findByAnd("BusinessTree", ["parentId" : parentId, "type" : treeType], null, false);
    data.parent = businessTreeParent;

    childList=[];
    for (businessChild in businessTreeChild) {
        childData = [:];
        childData.title = businessChild.title;
        childData.childId = businessChild.businessTreeId;
        childList.add(childData);
    }

    data.childList = childList;
    childSize = 0;
    if (UtilValidate.isNotEmpty(businessTreeChild)) {
        childSize = businessTreeChild.size();
        allChildListSize += childSize;
    }
    data.childSize = childSize;
    businessList.add(data);
}
context.businessList = businessList;
context.allChildListSize = allChildListSize;



isAdmin = false;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);
if (UtilValidate.isNotEmpty(userLoginAdmin)) {
    isAdmin = true;
}
context.isAdmin = isAdmin;

isHeadquarter = false;
if (userLoginPartyId.equals("HEADQUARTER")) {
    isHeadquarter = true;
    treeType = "DEPARTMENT";
}
context.isHeadquarter = isHeadquarter;

mercuryId = parameters.mercuryId;

if (UtilValidate.isNotEmpty(mercuryId)) {
    mercuryHeader = delegator.findOne("MercuryHeader", ["mercuryId" : mercuryId], false);
    mercuryItemList = delegator.findByAnd("MercuryItem", ["mercuryId" : mercuryId], ["statusId", "sequenceId"], false);
    hasPermission = false;
    if (mercuryHeader.createdBy.equals(userLoginId)) {
        hasPermission = true;
    }
    context.hasPermission = hasPermission;
    context.mercuryHeader = mercuryHeader;

    list=[];
    for (mercuryItem in mercuryItemList) {
        data=[:];
        data.mercuryId = mercuryItem.mercuryId;
        data.sequenceId = mercuryItem.sequenceId;
        data.sortId = mercuryItem.sortId;
        data.title = mercuryItem.title;
        data.notes = mercuryItem.notes;
        data.statusId = mercuryItem.statusId;
        data.startDate = mercuryItem.startDate;
        if (UtilValidate.isNotEmpty(mercuryItem.completedDate)) {
            data.completeDateEta = mercuryItem.completedDate;
        } else {
            data.completeDateEta = mercuryItem.completeDateEta;
        }
        if (UtilValidate.isEmpty(data.completeDateEta)) {
            data.completeDateEta = dummyDate;
        }
        data.completedDate = mercuryItem.completedDate;
        data.assignedTo = mercuryItem.assignedTo;

        overdue = false;
        if (mercuryItem.completeDateEta < nowTs && UtilValidate.isNotEmpty(mercuryItem.completeDateEta)) {
            overdue = true;
        }
        data.overdue = overdue;
        list.add(data);
    }
    context.mercuryItemList = list;
}
