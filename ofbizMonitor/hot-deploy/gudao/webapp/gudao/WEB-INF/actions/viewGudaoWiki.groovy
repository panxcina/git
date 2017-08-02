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

import javolution.util.FastMap;
import javolution.util.FastList;

import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
/*import java.text.DateFormat;*/
import java.text.SimpleDateFormat;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");

userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);

isAdmin = false;
if (UtilValidate.isNotEmpty(userLoginAdmin)) {
    isAdmin = true;
}
context.isAdmin = isAdmin;


EntityCondition wikiConds = EntityCondition.makeCondition(UtilMisc.toList(
                                                                EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"),
                                                                EntityCondition.makeCondition("parentId", EntityOperator.EQUALS, null)
                                                            ));
wikiList = delegator.findList("WikiEntry", wikiConds, null, ["sequence ASC"], null,false);
resultList = [];
for (wikiEntry in wikiList) {
    data = [:];
    data.wikiEntry = wikiEntry;
    wikiId = wikiEntry.wikiId;
    wikiChildList = delegator.findByAnd("WikiEntry", ["parentId" : wikiId], ["sequence"], false);
    data.wikiChildList = wikiChildList;
    resultList.add(data);
}




context.wikiList = resultList;