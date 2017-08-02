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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
/*import java.sql.Date;
import java.text.DateFormat;*/
import java.text.SimpleDateFormat;
import javolution.util.FastMap;
import javolution.util.FastList;


type = parameters.type;
statusId = parameters.statusId;
receiver = parameters.receiver;
priority = parameters.priority;
userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");

hasEditPermission = false;
/*externalAuthId = userLogin.getString("externalAuthId");
if (UtilValidate.isNotEmpty(externalAuthId)) {
if (externalAuthId.equals("LEADER")) {
hasEditPermission = true;
}
}*/

userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);
if (UtilValidate.isNotEmpty(userLoginAdmin)) {
hasEditPermission = true;
}
if (userLoginId.equals("simon.shen")) {
hasEditPermission = true;
}


List<EntityCondition> conditions = FastList.newInstance();

if (UtilValidate.isNotEmpty(type)) {
    if (!type.equals("ALL")) {
        conditions.add(EntityCondition.makeCondition("type", EntityOperator.EQUALS, type));
    }
}

if (UtilValidate.isNotEmpty(statusId)) {
    if (!statusId.equals("ALL")) {
        conditions.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, statusId));
    }
}

if (UtilValidate.isNotEmpty(receiver)) {
    if (!receiver.equals("ALL")) {
        conditions.add(EntityCondition.makeCondition("receiver", EntityOperator.EQUALS, receiver));
    }
} else {
    List<EntityCondition> receiverCond = FastList.newInstance();
    receiverCond.add(EntityCondition.makeCondition("createdBy", EntityOperator.EQUALS, userLoginId));
    receiverCond.add(EntityCondition.makeCondition("receiver", EntityOperator.EQUALS, userLoginId));
    receiverCond.add(EntityCondition.makeCondition("receiver", EntityOperator.EQUALS, userLoginPartyId));
    conditions.add(EntityCondition.makeCondition(receiverCond, EntityOperator.OR));
}

if (UtilValidate.isNotEmpty(priority)) {
    if (!priority.equals("ALL")) {
        conditions.add(EntityCondition.makeCondition("priority", EntityOperator.EQUALS, priority));
    }
}

EntityCondition cond = null;
if (conditions.size() > 0) {
    cond = EntityCondition.makeCondition(conditions, EntityOperator.AND);
}

resultList=[];
taskHeaderList = delegator.findList("GudaoTaskHeader", cond, null, null, null, false);
for (taskHeader in taskHeaderList) {
    data = [:];
    data.taskId = taskHeader.taskId;
    data.type= taskHeader.type;
    data.priority= taskHeader.priority;
    data.statusId = taskHeader.statusId;
    data.createdBy = taskHeader.createdBy;
    data.receiver = taskHeader.receiver;
    data.createdDate = taskHeader.createdDate;
    data.description = taskHeader.description;
    data.remark = taskHeader.remark;
    data.lastUpdatedBy = taskHeader.lastUpdatedBy;
    data.action = "NONE";
    if (taskHeader.createdBy.equals(userLoginId)) {
        data.action = "EDIT";
    }

    if (UtilValidate.isEmpty(taskHeader.lastUpdatedBy)) {
        if (taskHeader.receiver.equals(userLoginId) || taskHeader.receiver.equals(userLoginPartyId)) {
            data.action = "ACCEPT";
        }
    } else {
        if (taskHeader.receiver.equals(userLoginId) || taskHeader.receiver.equals(userLoginPartyId)) {
            data.action = "EDIT";
        }
    }
    if (hasEditPermission) {
        data.action = "EDIT";
    }
    resultList.add(data);
}

if (userLoginPartyId.equals("EBAY_GROUP")) {
    rivalRemovedList = delegator.findByAnd("GudaoTaskHeader", UtilMisc.toMap("type", "RIVAL_REMOVED", "statusId", "PENDING"), null, false);
    for (rivalRemoved in rivalRemovedList) {
        data = [:];
        data.taskId = rivalRemoved.taskId;
        data.type= rivalRemoved.type;
        data.priority= rivalRemoved.priority;
        data.statusId = rivalRemoved.statusId;
        data.createdBy = rivalRemoved.createdBy;
        data.receiver = rivalRemoved.receiver;
        data.createdDate = rivalRemoved.createdDate;
        data.description = rivalRemoved.description;
        data.remark = rivalRemoved.remark;
        data.lastUpdatedBy = rivalRemoved.lastUpdatedBy;
        data.action = "EDIT";
        if (UtilValidate.isEmpty(rivalRemoved.lastUpdatedBy)) {
            if (rivalRemoved.receiver.equals(userLoginId) || rivalRemoved.receiver.equals(userLoginPartyId)) {
                data.action = "ACCEPT";
            }
        }
        resultList.add(data);
    }
}

context.taskHeaderList = resultList;
context.userLoginId=userLoginId;
context.userLoginPartyId = userLoginPartyId;





