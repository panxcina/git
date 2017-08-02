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

userLoginList = delegator.findList("MercuryHeader", null, null, null, null, false);
//userLoginList = delegator.findList("MercuryHeader", null, UtilMisc.toSet("createdBy"), UtilMisc.toList("createdBy"), new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
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

