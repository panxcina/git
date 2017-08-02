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
/*import java.text.DateFormat;*/
import java.text.SimpleDateFormat;
import javolution.util.FastMap;
import javolution.util.FastList;

pipelineId = parameters.pipelineId;
context.pipelineId = pipelineId;

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
context.isSbuParameter = isSbu;
context.isAdmin = isAdmin;

list = [];

List<EntityCondition> conditions = FastList.newInstance();

if (UtilValidate.isNotEmpty(pipelineId)) {
    conditions.add(EntityCondition.makeCondition("pipelineId", EntityOperator.EQUALS, pipelineId));
}
conditions.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
condition = EntityCondition.makeCondition(conditions, EntityOperator.AND);

pipelineHeaderList = delegator.findList("PipelineHeader", condition, null, UtilMisc.toList("pipelineId"), null, false);

context.pipelineHeaderList = pipelineHeaderList;

itemCondition = null;
if (UtilValidate.isNotEmpty(pipelineId)) {
    itemCondition = EntityCondition.makeCondition("pipelineId", EntityOperator.EQUALS, pipelineId);
}

pipelineItemList = [];
pipelineItemListGV = delegator.findList("PipelineItem", itemCondition, null, UtilMisc.toList("weekNumber"), null, false);
for (pipelineItem in pipelineItemListGV) {
    data = [:];
    data.weekNumber = pipelineItem.weekNumber;
    data.productSourced = pipelineItem.productSourced;
    data.pictureReady = pipelineItem.pictureReady;
    data.pictureDone = pipelineItem.pictureDone;
    data.copyDone = pipelineItem.copyDone;
    data.listed = pipelineItem.listed;
    data.active = pipelineItem.active;
    pipelineWeekList = delegator.findByAnd("PipelineWeek", UtilMisc.toMap("weekNumber", pipelineItem.weekNumber, "statusId", "ACTIVE"), null, false);
    if (UtilValidate.isNotEmpty(pipelineWeekList)) {
        pipelineWeek = EntityUtil.getFirst(pipelineWeekList);
        data.startDate = pipelineWeek.startDate;
        data.endDate = pipelineWeek.endDate;
    }
    pipelineItemList.add(data);

}
context.pipelineItemList = pipelineItemList;


