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

Calendar now = Calendar.getInstance();
java.sql.Date nowTs = new java.sql.Date(now.getTimeInMillis());
Date dummyDate = new SimpleDateFormat("dd-MM-yyyy").parse("1-1-2000");
context.dummyDate = dummyDate;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
context.userLoginId = userLoginId;

userLoginList = delegator.findList("MercuryHeader", null, UtilMisc.toSet("createdBy"), UtilMisc.toList("createdBy"), new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
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

List<EntityCondition> profileTreeCondList = FastList.newInstance();
profileTreeCondList.add(EntityCondition.makeCondition("parentId", EntityOperator.EQUALS, null));
profileTreeCondList.add(EntityCondition.makeCondition("type", EntityOperator.EQUALS, "PROFILE"));
EntityCondition profileTreeCond = EntityCondition.makeCondition(profileTreeCondList, EntityOperator.AND);

profileTreeList = delegator.findList("BusinessTree", profileTreeCond, null, ["businessTreeId"], null, false);
profileList = [];
for (profileTreeParent in profileTreeList) {
    data = [:];
    parentId = profileTreeParent.businessTreeId;
    profileTreeChild = delegator.findByAnd("BusinessTree", ["parentId" : parentId, "type" : "PROFILE"], null, false);
    data.parent = profileTreeParent;
    data.childList = profileTreeChild;
    profileList.add(data);
}
context.profileList = profileList;

List<EntityCondition> businessTreeCondList = FastList.newInstance();
businessTreeCondList.add(EntityCondition.makeCondition("parentId", EntityOperator.EQUALS, null));
businessTreeCondList.add(EntityCondition.makeCondition("type", EntityOperator.EQUALS, "DEPARTMENT"));
EntityCondition businessTreeCond = EntityCondition.makeCondition(businessTreeCondList, EntityOperator.AND);

businessTreeList = delegator.findList("BusinessTree", businessTreeCond, null, ["businessTreeId"], null, false);
businessList = [];
for (businessTreeParent in businessTreeList) {
    data = [:];
    parentId = businessTreeParent.businessTreeId;
    businessTreeChild = delegator.findByAnd("BusinessTree", ["parentId" : parentId, "type" : "DEPARTMENT"], null, false);
    data.parent = businessTreeParent;
    data.childList = businessTreeChild;
    businessList.add(data);
}
context.businessList = businessList;

