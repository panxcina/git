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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javolution.util.FastMap;
import javolution.util.FastList;

userLoginIdInput = parameters.userLoginIdInput;
periodInput = parameters.period;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
context.userLoginId = userLoginId;
if (UtilValidate.isEmpty(userLoginIdInput)) {
    userLoginIdInput = "ALL";
}
context.userLoginIdInput = userLoginIdInput;

isAdmin = false;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);
if (UtilValidate.isNotEmpty(userLoginAdmin)) {
isAdmin = true;
}
context.isAdmin = isAdmin;

cal = Calendar.getInstance();
monthName = ["JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY",
"AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"];
currentMonth = monthName[cal.get(Calendar.MONTH)];

if (UtilValidate.isNotEmpty(periodInput)) {
currentMonth = periodInput;
}
context.currentMonth = currentMonth;


EntityCondition condition = null;
List<EntityCondition> conditionList = FastList.newInstance();

if (!currentMonth.equals("ALL")) {
    conditionList.add(EntityCondition.makeCondition("period", EntityOperator.EQUALS, currentMonth));
}
if (isAdmin) {
    if (UtilValidate.isNotEmpty(userLoginIdInput)) {
        if (!userLoginIdInput.equals("ALL")) {
            conditionList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginIdInput));
        }
    } else {
        conditionList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginId));
    }
    userLoginList = delegator.findByAnd("UserLoginAndPartyDetails", ["enabled" : "Y"], ["userLoginId"], false);
    context.userLoginList = userLoginList;
} else {
    conditionList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginId));
}

if (conditionList.size() > 0) {
    condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
}

gudaoTargetList = delegator.findList("GudaoTargetList", condition, null, ["period", "createdStamp DESC"], null, false);
context.gudaoTargetSize = gudaoTargetList.size() + 1;
context.gudaoTargetList = gudaoTargetList;

list =[];
for (gudaoTarget in gudaoTargetList) {
    targetList = [:];
    targetId = gudaoTarget.targetId;
    target = gudaoTarget.target;
    gudaoActionList = delegator.findByAnd("GudaoTargetAction", UtilMisc.toMap("targetId", targetId), UtilMisc.toList("dueDate"), false);
    actionList = [];
    for (gudaoAction in gudaoActionList) {
        data =[:];
        data.target = target;
        data.dueDate = gudaoAction.dueDate;
        data.actionBy = gudaoAction.actionBy;
        data.description = gudaoAction.description;
        data.statusId = gudaoAction.statusId;
        data.priority = gudaoAction.priority;
        actionList.add(data);
    }
    targetList.target = gudaoTarget.target;
    targetList.actionList = actionList;
    targetList.actionListSize = actionList.size() + 1;
    list.add(targetList);
}

context.gudaoActionList = list;
