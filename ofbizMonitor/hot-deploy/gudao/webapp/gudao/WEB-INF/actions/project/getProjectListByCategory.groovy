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

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
context.userLoginId = userLoginId;

Calendar now = Calendar.getInstance();
java.sql.Date nowTs = new java.sql.Date(now.getTimeInMillis());

businessTreeId = parameters.businessTreeId;
businessTreeType = parameters.businessTreeType;
statusId = parameters.statusId;

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
otherConditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, statusId));
otherConditionList.add(EntityCondition.makeCondition("createdBy", EntityOperator.NOT_EQUAL, userLoginId));
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