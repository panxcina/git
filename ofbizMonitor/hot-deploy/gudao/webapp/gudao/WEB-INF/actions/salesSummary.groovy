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
import org.ofbiz.base.util.UtilDateTime;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
/*import java.text.DateFormat;*/
import java.text.SimpleDateFormat;
import com.gudao.bulkModule.bulkService;
import org.ofbiz.base.util.ObjectType;
import java.math.BigDecimal;
import java.util.ArrayList;
userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
context.ownerGroup = ownerGroup;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);



daysVar = parameters.days;
departmentVar = parameters.department;
platformVar =  parameters.platform;
ownerInput = parameters.ownerGroup;
context.departmentVar=departmentVar;
context.platformVar = platformVar;
context.ownerInput= ownerInput;
context.daysVar = daysVar;

	


if (UtilValidate.isEmpty(daysVar)) {
    daysVar="1";
}
long dayRange = Long.parseLong(daysVar);
if (UtilValidate.isEmpty(departmentVar)) {
    departmentVar = "";
}
if (UtilValidate.isEmpty(platformVar)) {
    platformVar = "";
}
if (UtilValidate.isEmpty(ownerInput)){
    ownerInput="";
} 
plist=[];
findAllDepartmentList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap( "roleTypeId", "PLATFORM"), null, false);
context.findDepartmentList = findAllDepartmentList;
distinctPlatformList = delegator.findByAnd("RoleType", UtilMisc.toMap("parentTypeId","PLATFORM"), ["description ASC"],false);
context.findPlatformList = distinctPlatformList;
ownerGroupAllList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("partyGroupComments","ACTIVE","roleTypeId","SBU"), ["officeSiteName ASC"],false);
context.findOwnerGroupList = ownerGroupAllList;

List<EntityCondition> findDepartmentCondList = FastList.newInstance();
if (UtilValidate.isNotEmpty(departmentVar)){ 
	findDepartmentCondList.add(EntityCondition.makeCondition("department", EntityOperator.EQUALS, departmentVar));
}
if (UtilValidate.isNotEmpty(platformVar)){ 
	findDepartmentCondList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, platformVar));
}
if (UtilValidate.isNotEmpty(ownerInput)){ 
	findDepartmentCondList.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerInput));
}
EntityCondition findDepartmentCond = null;
if (findDepartmentCondList.size() > 0)	{
	findDepartmentCond = EntityCondition.makeCondition(findDepartmentCondList, EntityOperator.AND);
}
salesSummaryList = delegator.findList("SalesSummary", findDepartmentCond, null, null, null, false);
plist=[];
for (salesSummary in salesSummaryList) {
	productId = salesSummary.productId;
	department = departmentVar;
	platform = salesSummary.platform;
	salesSummaryDetail = delegator.findOne("SalesSummaryDetail",UtilMisc.toMap("department",department,"platform",platform,"productId",productId,"salesDays",dayRange), false);
	data = [:];
	data.productId = productId;
	data.statusId = skuList.statusId;
	data.department = oneDeparment;
	data.platform = onePlatform;
	data.platformProductGroup = salesSummary.platformProductGroup;
	data.chineseName = salesSummary.chineseName;
	data.ownerGroup = ownerInput;
	data.supplier = salesSummary.mainsupplier;
	data.productCost = salesSummary.productCost;
	data.qty = salesSummary.qty;
	data.inventoryAmount = salesSummary.inventoryAmount;
	data.productCreatedTime = salesSummary.productCreatedTime;
	data.salesDays = salesSummaryDetail.salesDays;
	data.sales = salesSummaryDetail.sales;
	data.qtySold = salesSummaryDetail.qtySold;
	data.qtyConvertDayCount = salesSummaryDetail.qtyConvertDayCount;
	plist.add(data);
}

context.p =plist;







