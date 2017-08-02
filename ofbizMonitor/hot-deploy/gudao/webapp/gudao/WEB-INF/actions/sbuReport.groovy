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

import com.gudao.bulkModule.bulkService;


userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
context.ownerGroup = ownerGroup;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);


yearInput = parameters.year;
monthInput = parameters.period;
context.monthInput = monthInput;

cal = Calendar.getInstance();
currentMonth = cal.get(Calendar.MONTH);
currentYear = cal.get(Calendar.YEAR);
context.currentMonth = currentMonth + "";
context.currentYear = currentYear;

if (UtilValidate.isEmpty(monthInput)) {




	monthInt = currentMonth;
} else {
	monthInt = Integer.valueOf(monthInput) ;
}

if (UtilValidate.isEmpty(yearInput)) {
	yearInt = currentYear;
} else {
	yearInt = Integer.valueOf(yearInput);
}

fromDateCal = Calendar.getInstance();
fromDateCal.set(Calendar.DAY_OF_MONTH, 1);
fromDateCal.set(Calendar.MONTH, monthInt);
fromDateCal.set(Calendar.YEAR, yearInt);
fromDateCal.set(Calendar.MILLISECOND, 0);
fromDate = new java.sql.Timestamp(fromDateCal.getTimeInMillis());
context.fromDate = fromDate;

toDateCal = Calendar.getInstance();
toDateCal.set(Calendar.DAY_OF_MONTH, 1);
toDateCal.set(Calendar.MONTH, monthInt + 1);
toDateCal.set(Calendar.YEAR, yearInt);
toDateCal.set(Calendar.MILLISECOND, 0);
toDate = new java.sql.Timestamp(toDateCal.getTimeInMillis());
context.toDate = toDate;




plist=[];
allList=[];
allData=[:];
allMotherSkuCount=0;
allG1Count=0;
allPercentage=0.0;
allTotalCount=0;
allG1MotherSkuTotal=0;
allG1Percent=0.0;
ownerGroupList = delegator.findByAnd("OwnerGroup", UtilMisc.toMap("statusId","ACTIVE"), ["ownerGroupId ASC"],false);
for (ownerGroupGV in ownerGroupList) {
	totalList=[];
	totalListG1Count=[];
	data=[:];
	count=0;
  	g1Count = 0;
	g1Percent = 0;
	totalCount = 0.0;
	g1MotherSkuTotal = 0.0;
	data.ownerGroup = ownerGroupGV.ownerGroupId;
	List<EntityCondition> newProductList = FastList.newInstance();
	
	newProductList.add(EntityCondition.makeCondition("createdStamp", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate));
	newProductList.add(EntityCondition.makeCondition("createdStamp", EntityOperator.LESS_THAN,toDate));
	newProductList.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroupGV.ownerGroupId));
	
	EntityCondition listerCond = null;
	if (newProductList.size() > 0) {
    		listerCond = EntityCondition.makeCondition(newProductList, EntityOperator.AND);
	}
	pmList = delegator.findList("ProductMaster", listerCond, null, null, null, false);
	newProductList.add(EntityCondition.makeCondition("motherSku", EntityOperator.NOT_EQUAL,null));
        EntityCondition sonCond = null;
	if (newProductList.size() > 0) {
    		sonCond = EntityCondition.makeCondition(newProductList, EntityOperator.AND);
	}
	
       
	motherSkuList = delegator.findList( "ProductMaster", sonCond, UtilMisc.toSet("motherSku"),
                                        UtilMisc.toList("motherSku"),
                                        new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                        false);	
	motherSkuCount = motherSkuList.size();
	data.motherSkuCount = motherSkuCount;
    allMotherSkuCount += motherSkuCount;
    allData.allMotherSkuCount = allMotherSkuCount;
    for (pm in pmList) {
		if(UtilValidate.isNotEmpty(pm.motherSku)){
		        singleSku = pm.motherSku;			
			if(!(totalList.contains(singleSku))){
				totalCount += pm.motherSkuWeight;
				totalList.add(singleSku);
			}
			if(!(totalListG1Count.contains(singleSku))){
	   			if(pm.productGroup.equals("G1")||pm.productGroup.equals("准G1")){
					g1Count++;
					g1MotherSkuTotal  += pm.motherSkuWeight;
					totalListG1Count.add(singleSku);
				}
				
			}
		}
			
	}
	if (motherSkuCount == 0) {
		percentage = 0;
	} else {
		percentage = g1Count / motherSkuCount;
	}
	data.percentage = percentage;

    	if (totalCount == 0) {
		g1Percent = 0;
	} else {
		g1Percent = g1MotherSkuTotal / totalCount;
	}
    data.totalCount = totalCount;
    allTotalCount += totalCount;
    allData.allTotalCount = allTotalCount;
	data.g1Percent = g1Percent;
    data.g1MotherSkuTotal = g1MotherSkuTotal;
	allG1MotherSkuTotal += g1MotherSkuTotal;
	allData.allG1MotherSkuTotal = allG1MotherSkuTotal;
if (allTotalCount > 0) {
    allG1Percent = allG1MotherSkuTotal / allTotalCount;
    allData.allG1Percent = allG1Percent;
} else {
    allData.allG1Percent = 0;
}


	data.g1Count = g1Count;
	allG1Count += g1Count;
	allData.allG1Count = allG1Count;
if (allMotherSkuCount > 0) {
    allPercentage = allG1Count / allMotherSkuCount;
    allData.allPercentage = allPercentage;
} else {
    allData.allPercentage = 0;
}


	plist.add(data);
	
}
allList.add(allData);
context.p = plist;
context.a = allList;


