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


targetId = parameters.targetId;
if (UtilValidate.isNotEmpty(targetId)) {
context.targetId = targetId;
}
editAction = parameters.editAction;
seqId = parameters.seqId;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
context.userLoginId = userLoginId;

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
context.currentMonth = currentMonth;


if (UtilValidate.isNotEmpty(targetId)) {
    gudaoTarget = delegator.findOne("GudaoTargetList", UtilMisc.toMap("targetId", targetId), false);
    context.gudaoTarget = gudaoTarget;

    df = new DecimalFormat("00000");
    nextSeqIdInt = 1;
    actionList = delegator.findByAnd("GudaoTargetAction", UtilMisc.toMap("targetId", targetId), UtilMisc.toList("dueDate"), false);
    for (action in actionList) {
        seqIdInt = Integer.parseInt(action.seqId);
        if (seqIdInt > nextSeqIdInt) {
            nextSeqIdInt = seqIdInt;
        }
    }
    nextSeqId = df.format(nextSeqIdInt+1);
    context.actionList = actionList;
    context.nextSeqId = nextSeqId;
}

if (UtilValidate.isNotEmpty(editAction) && editAction.equals("TRUE")) {
    context.editAction = true;
    editActionGV = delegator.findOne("GudaoTargetAction", UtilMisc.toMap("targetId", targetId, "seqId", seqId), false);
    context.editActionTargetId = editActionGV.targetId;
    context.editActionSeqId = editActionGV.seqId;
    context.editActionCreatedBy = editActionGV.createdBy;
    context.editActionActionBy = editActionGV.actionBy;
    context.editActionCreatedDate = editActionGV.createdDate;
    context.editActionDueDate = editActionGV.dueDate;
    context.editActionStatusId = editActionGV.statusId;
    context.editActionPriority = editActionGV.priority;
    context.editActionDescription = editActionGV.description;
    context.editActionNotes = editActionGV.notes;
}

