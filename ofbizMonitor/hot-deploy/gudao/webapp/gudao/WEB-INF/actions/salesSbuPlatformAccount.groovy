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

if (UtilValidate.isEmpty(parameters.ownerGroupInput)) {
    ownerGroupInput = "JUSTRANDOMONE";
    context.ownerGroupInput = ownerGroupInput;
}

fromDateInput = parameters.minDate;
toDate  = parameters.maxDate;
isMultiple = false;
if (StringUtils.countMatches(parameters.ownerGroupInput.toString(), ",") > 0) {
	isMultiple = true;
}

ownerVar =[];
if (isMultiple) {
	for (test in parameters.ownerGroupInput) {
		ownerVar.add(test);
	}
} else {
	ownerVar.add(parameters.ownerGroupInput);
}

platformVar = parameters.platformInput;
context.minDate = fromDateInput;
context.maxDate = toDate;
//context.ownerVar = ownerVar;
now = Calendar.getInstance();
sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
nowTimestamp = Timestamp.valueOf(sdf.format(now.getTime()));

if (UtilValidate.isEmpty(fromDateInput)) { 
	fromDateInput = UtilDateTime.getMonthStart(nowTimestamp);
}
if (UtilValidate.isEmpty(toDate)) { 
	toDate = UtilDateTime.getXMonthFromToday(nowTimestamp,0);
}
if (UtilValidate.isEmpty(ownerVar)) { 
	ownerVar = "";
} else {
    context.ownerGroupInput = ownerVar;
}
Date fromDateDt= ObjectType.simpleTypeConvert(fromDateInput, "Date", null, null);	
Date toDateDt= ObjectType.simpleTypeConvert(toDate, "Date", null, null);	

view = parameters.detail;
if (UtilValidate.isEmpty(view)) {
	view = false;
}
else {
	view = true;
}
context.detail = view;

long daily =0;
if (UtilValidate.isNotEmpty(fromDateInput) && UtilValidate.isNotEmpty(toDate)) { 	
	daily = Math.abs(toDateDt.getTime() - fromDateDt.getTime()) / (24*60*60*1000) + 1;
}
context.dailyas =daily;
//java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd ");
//String date = formatter.format(new Date());//格式化数据


java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
Calendar calendar = Calendar.getInstance();
calendar.set(Calendar.DAY_OF_MONTH, 1);
calendar.add(Calendar.MONTH, -1);
Timestamp lastMonthDate = Timestamp.valueOf(sdf.format(calendar.getTime()));

pfList=[];
allList=[];
rightAllList=[];

finalTotalSales=0;
finalTotalProfit=0;
finalPercent=0;
finalLastMonthSales=0;
finalLastMonthProfit=0;
finalLastMonthPercent=0;

List<String> list = new ArrayList<String>();
List<BigDecimal> rightTotalSalesList = new ArrayList<BigDecimal>();
List<BigDecimal> rightTotalProfitList = new ArrayList<BigDecimal>();
List<BigDecimal> rightTotalPercentList = new ArrayList<BigDecimal>();
List<BigDecimal> rightLastMonthSalesList = new ArrayList<BigDecimal>();
List<BigDecimal> rightLastMonthProfitList = new ArrayList<BigDecimal>();

//platformList = delegator.findByAnd("RoleType", UtilMisc.toMap("parentTypeId","PLATFORM"), ["description ASC"],false);
EntityCondition platformConds = EntityCondition.makeCondition(UtilMisc.toList(
                                                                        EntityCondition.makeCondition("parentTypeId", EntityOperator.EQUALS, "PLATFORM")
                                                                        //EntityCondition.makeCondition("roleTypeId", EntityOperator.NOT_IN, ["CDISCOUNT", "TOPHATTER"])
                                                                        ));
platformList = delegator.findList("RoleType", platformConds, null, ["description ASC"], null,false);
context.platformList = platformList;

ownerGroupAllList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("partyGroupComments","ACTIVE","roleTypeId","SBU"), ["officeSiteName ASC"],false);
context.ownerGroupAllList = ownerGroupAllList;
for(pfl in platformList){
	List<EntityCondition> findSbuList = FastList.newInstance();
	findSbuList.add(EntityCondition.makeCondition("partyGroupComments", EntityOperator.EQUALS, "ACTIVE"));
	findSbuList.add(EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "SBU"));
	findSbuList.add(EntityCondition.makeCondition("officeSiteName", EntityOperator.IN, ownerVar));
	EntityCondition listerConds = null;
	 if (findSbuList.size() > 0)	{
	    	listerConds = EntityCondition.makeCondition(findSbuList, EntityOperator.AND);
	}
	List<GenericValue> ownerGroupList = delegator.findList("PartyRoleDetailAndPartyDetail", listerConds, null,["officeSiteName ASC"],null,false);
//	ownerGroupList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("partyGroupComments","ACTIVE","roleTypeId","SBU"), ["officeSiteName ASC"],false);
	context.ownerGroupList = ownerGroupList;
	allData=[:];	
 	pfListData=[:];
	downTotalSales=0;
	downTotalProfit=0;
	totalPercent=0;
	downTotalLastMonthSales=0;
	downTotalLastMonthProfit=0;
	downTotalLastMonthPercent=0;
	sbuList=[];
	
	for(owner in ownerGroupList){
		if(!list.contains(owner.officeSiteName)){
			list.add(owner.officeSiteName);
		}
	}
	for(ow in ownerGroupList){
		sbuData=[:];
		sbuDepartmentSales=0;
		sbuDepartmentProfit=0;
		sbuDepartmentPercent=0;

		plist=[];
		
		dailySumSales=0;
		dailyItemProfit=0;
		dailyprofitRatePercent=0;
		lastMonthDepartSales=0;
		lastMonthDepartProfit=0;
		lastMonthDepartPercent=0;		
		lastMonthSales=0;
		lastMonthProfit=0;
		lastMonthPercent=0;
		platformDepartmentList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("roleTypeId",pfl.roleTypeId), ["partyId ASC"],false);
		context.platformDepartmentList = platformDepartmentList;
		data=[:];
		platformDepartList=[];
		
		for(pfdl in platformDepartmentList){
			sumSales=0;	
			sumItemProfit=0;
			departData=[:];
			hasSalesData=false;
			departData.departmentid = pfdl.partyId;

			List<EntityCondition> salesReportList = FastList.newInstance();
			if (UtilValidate.isNotEmpty(fromDateInput)){ 
			  	Timestamp fromDateTS = ObjectType.simpleTypeConvert(fromDateInput, "Timestamp", null, null);	
		  		
		   	 	salesReportList.add(EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, fromDateTS));
			}
		 	if (UtilValidate.isNotEmpty(toDate)) { 
			  	Timestamp toDateTS = ObjectType.simpleTypeConvert(toDate, "Timestamp", null, null);
				context.toDateTS = toDateTS;
		   	  	salesReportList.add(EntityCondition.makeCondition("date", EntityOperator.LESS_THAN, toDateTS));
			}
			salesReportList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, pfl.roleTypeId));
			salesReportList.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ow.officeSiteName));
			salesReportList.add(EntityCondition.makeCondition("platformPartyId",EntityOperator.EQUALS,pfdl.partyId))
		 	EntityCondition listerCond = null;
		 	if (salesReportList.size() > 0)	{
		    		listerCond = EntityCondition.makeCondition(salesReportList, EntityOperator.AND);
			}
	 		platformSalesList = delegator.findList("SalesReportImport", listerCond, null, null, null, false); 
			for(ps in platformSalesList){
				if(ps.platformPartyId.equals(pfdl.partyId)){
					hasSalesData=true;
				}
				sales = ps.sales;	
				itemProfit = ps.profit;
				sumSales += sales;	
				sumItemProfit += itemProfit;		
			}

			sbuDepartmentSales = sumSales / daily;	
			sbuDepartmentProfit = sumItemProfit / daily;
			departData.sbuDepartmentSales = sbuDepartmentSales;
			departData.sbuDepartmentProfit = sbuDepartmentProfit;
			departData.hasSalesData = hasSalesData;
			if (sbuDepartmentSales == 0) {
				departData.sbuDepartmentPercent = 0;
			} else {
				departData.sbuDepartmentPercent = sbuDepartmentProfit / sbuDepartmentSales ;
			}
			
			dailySumSales += sbuDepartmentSales;	
			dailyItemProfit += sbuDepartmentProfit;
			data.dailySumSales = dailySumSales;
			data.dailyItemProfit = dailyItemProfit;
			
		
			if (dailySumSales == 0) {
				data.dailyprofitRatePercent = 0;
			} else {
				data.dailyprofitRatePercent = dailyItemProfit / dailySumSales ;
			}

			lastMonthSalesList = delegator.findByAnd("SalesReportImportMonthly",UtilMisc.toMap("date",lastMonthDate,"platform",pfl.roleTypeId,"ownerGroup",ow.officeSiteName,"platformPartyId",pfdl.partyId),["platformPartyId ASC"],false);
			for(last in lastMonthSalesList){
				if (UtilValidate.isEmpty(last.totalSales)) {
							last.totalSales = BigDecimal.ZERO;
						}
				if (UtilValidate.isEmpty(last.totalProfit)) {
							last.totalProfit = BigDecimal.ZERO;
						}
				lastMonthDepartSales = last.totalSales;
				lastMonthDepartProfit = last.totalProfit;
				departData.lastMonthDepartSales = lastMonthDepartSales;
				departData.lastMonthDepartProfit = lastMonthDepartProfit;
				if (lastMonthDepartSales == 0) {
					departData.lastMonthDepartPercent = 0;
				} else {
					departData.lastMonthDepartPercent = lastMonthDepartProfit / lastMonthDepartSales ;
				}
				lastMonthSales += lastMonthDepartSales;
				lastMonthProfit += lastMonthDepartProfit;
				data.lastMonthDepart = last.platformPartyId;
				data.lastMonthSales = lastMonthSales;
				data.lastMonthProfit = lastMonthProfit;
				if (lastMonthSales == 0) {
					data.lastMonthPercent = 0;
				} else {
					data.lastMonthPercent = lastMonthProfit / lastMonthSales ;
				}
				
			}
			
			platformDepartList.add(departData);
			data.platformDepartList = platformDepartList;
			
			
		}
		plist.add(data);

		for(int i=0; i<list.size();i++){
			if(ow.officeSiteName==list[i]){
				if (UtilValidate.isEmpty(rightTotalSalesList[i])) {
					rightTotalSalesList[i] = BigDecimal.ZERO;
				}
				if (UtilValidate.isEmpty(rightTotalProfitList[i])) {
					rightTotalProfitList[i] = BigDecimal.ZERO;
				}
				rightTotalSalesList[i] += dailySumSales;
				rightTotalProfitList[i] += dailyItemProfit;	

				if (UtilValidate.isEmpty(rightLastMonthSalesList[i])) {
					rightLastMonthSalesList[i] = BigDecimal.ZERO;
				}
				if (UtilValidate.isEmpty(rightLastMonthProfitList[i])) {
					rightLastMonthProfitList[i] = BigDecimal.ZERO;
				}
				rightLastMonthSalesList[i] += lastMonthSales;
				rightLastMonthProfitList[i] += lastMonthProfit;	
			}
		}
		downTotalSales += dailySumSales;
		downTotalProfit += dailyItemProfit;	
		if (downTotalSales == 0) {
			totalPercent = 0;
		} else {
			totalPercent = downTotalProfit / downTotalSales ;
		}		
		downTotalLastMonthSales += lastMonthSales;
		downTotalLastMonthProfit += lastMonthProfit;
		if (downTotalLastMonthSales == 0) {
			downTotalLastMonthPercent = 0;
		} else {
			downTotalLastMonthPercent = downTotalLastMonthProfit / downTotalLastMonthSales ;
		}
	
		sbuData.ownerGroup = ow.officeSiteName;	
		sbuData.plist = plist;		
		sbuList.add(sbuData);
	}

	pfListData.platform =  pfl.roleTypeId;
	pfListData.sbuList = sbuList;
	pfList.add(pfListData);	
	
	allData.downTotalSales = downTotalSales;
	allData.downTotalProfit = downTotalProfit;
	allData.totalPercent = totalPercent;
	allData.downTotalLastMonthSales = downTotalLastMonthSales;
	allData.downTotalLastMonthProfit = downTotalLastMonthProfit;
	allData.downTotalLastMonthPercent = downTotalLastMonthPercent;
	allList.add(allData);

    finalTotalSales += downTotalSales;
    finalTotalProfit += downTotalProfit;
    if (finalTotalSales == 0) {
        finalPercent = 0;
    } else {
        finalPercent = finalTotalProfit / finalTotalSales ;
    }
    finalLastMonthSales += downTotalLastMonthSales;
    finalLastMonthProfit += downTotalLastMonthProfit;
    if (finalLastMonthSales == 0) {
        finalLastMonthPercent = 0;
    } else {
        finalLastMonthPercent = finalLastMonthProfit / finalLastMonthSales ;
    }
    context.finalTotalSales = finalTotalSales;
    context.finalTotalProfit = finalTotalProfit;
    context.finalPercent = finalPercent;
    context.finalLastMonthSales = finalLastMonthSales;
    context.finalLastMonthProfit = finalLastMonthProfit;
    context.finalLastMonthPercent = finalLastMonthPercent;

}
for(int j=0 ;j<list.size();j++){
	rightAllData=[:];
	rightAllData.sbu = list[j];
	rightAllData.rightTotalSales = rightTotalSalesList[j];
	rightAllData.rightTotalProfit = rightTotalProfitList[j];
	if (rightTotalSalesList[j] == 0) {
		rightTotalPercent = 0;
	} else {
		rightTotalPercent = rightTotalProfitList[j] / rightTotalSalesList[j] ;
	}
	rightAllData.rightTotalPercent = rightTotalPercent;

	rightAllData.rightLastMonthSales = rightLastMonthSalesList[j];
	rightAllData.rightLastMonthProfit = rightLastMonthProfitList[j];

	if (rightLastMonthSalesList[j] == 0) {
		rightLastMonthPercent = 0;
	} else {
		rightLastMonthPercent = rightLastMonthProfitList[j] / rightLastMonthSalesList[j] ;
	}
	rightAllData.rightLastMonthPercent = rightLastMonthPercent;
	rightAllList.add(rightAllData);

}
context.list=list;
context.rightTotalSalesList=rightTotalSalesList;
context.a =allList;
context.s = pfList;
context.rightAllList = rightAllList;







