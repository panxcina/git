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

context.platform="EBAY";

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
context.ownerGroup = ownerGroup;

targetList = delegator.findByAnd("GudaoTargetList", UtilMisc.toMap("platform", "EBAY"), null, false);

context.gudaoTargetList = targetList;

sopList = EntityUtil.getFirst(delegator.findByAnd("StandardOperationalProcedure", UtilMisc.toMap("platform", "EBAY"), null, false));
context.sopList = sopList;

hasEditPermission = false;
externalAuthId = userLogin.getString("externalAuthId");
if (UtilValidate.isNotEmpty(externalAuthId)) {
    if (externalAuthId.equals("LEADER")) {
        hasEditPermission = true;
    }
}

userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);
if (UtilValidate.isNotEmpty(userLoginAdmin)) {
hasEditPermission = true;
}

context.hasEditPermission = hasEditPermission;



