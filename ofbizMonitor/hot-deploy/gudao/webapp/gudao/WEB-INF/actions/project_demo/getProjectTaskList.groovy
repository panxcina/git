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
    if (UtilValidate.isEmpty(parameters.treeType))  {
        treeType = "DEPARTMENT";
    }
}
context.isHeadquarter = isHeadquarter;

mercuryId = parameters.mercuryId;

mercuryHeader = delegator.findOne("MercuryHeader", ["mercuryId" : mercuryId], false);
context.mercuryHeader = mercuryHeader;

EntityCondition condition = null;
List<EntityCondition> conditionList = FastList.newInstance();

conditionList.add(EntityCondition.makeCondition("mercuryId", EntityOperator.EQUALS, mercuryId));
conditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "COMPLETED"));
condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);

mercuryItemList = delegator.findList("MercuryItem", condition, null, ["statusId", "completeDateEta"], null, false);
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
    data.completeDateEta = mercuryItem.completeDateEta;
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

mercuryItemListCompleted = delegator.findByAnd("MercuryItem", ["mercuryId" : mercuryId, "statusId" : "COMPLETED"], ["completeDateEta ASC"], false);
context.mercuryItemListCompleted = mercuryItemListCompleted;

hasPermission = false;
if (mercuryHeader.createdBy.equals(userLoginId)) {
    hasPermission = true;
}
context.hasPermission = hasPermission;