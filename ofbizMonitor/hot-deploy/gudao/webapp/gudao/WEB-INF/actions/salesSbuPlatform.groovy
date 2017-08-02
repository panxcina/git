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

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
context.ownerGroup = ownerGroup;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);




fromDateInput = parameters.minDate;
toDate  = parameters.maxDate;
ownerVar = parameters.ownerGroupInput;
context.minDate = fromDateInput;
context.maxDate = toDate;

if (UtilValidate.isEmpty(fromDateInput)) { 
	fromDateInput = UtilDateTime.monthBegin();
}
if (UtilValidate.isEmpty(toDate)) { 
	toDate = UtilDateTime.getXMonthFromToday(nowTimestamp,0);
}	
Date fromDateDt= ObjectType.simpleTypeConvert(fromDateInput, "Date", null, null);	
Date toDateDt= ObjectType.simpleTypeConvert(toDate, "Date", null, null);	

long daily =0;
if (UtilValidate.isNotEmpty(fromDateInput) && UtilValidate.isNotEmpty(toDate)) { 	
	daily = Math.abs(toDateDt.getTime() - fromDateDt.getTime()) / (24*60*60*1000) + 1;
}
context.dailyas =daily;
pfList=[];
allList=[];

platformList = delegator.findByAnd("RoleType", UtilMisc.toMap("parentTypeId","PLATFORM"), ["description ASC"],false);

for(pfl in platformList){
	ownerGroupList = delegator.findByAnd("PartyRoleDetailAndPartyDetail", UtilMisc.toMap("partyGroupComments","ACTIVE","roleTypeId","SBU"), ["officeSiteName ASC"],false);
	allData=[:];	
 	pfListData = [:];
	totalSales = 0;
	totalProfit = 0;
	totalPercent = 0;
	sbuList=[];
	
	for(ow in ownerGroupList){
		plist=[];
		data=[:];
		sbuData=[:];
		sumSales=0;	
		sumItemProfit=0;
		sbuData.ownerGroup = ow.officeSiteName;	
		dailySumSales=0;
		dailyItemProfit=0;
		dailyprofitRatePercent=0;
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
			
	 	EntityCondition listerCond = null;
	 	if (salesReportList.size() > 0)	{
	    		listerCond = EntityCondition.makeCondition(salesReportList, EntityOperator.AND);
		}
	 	platformSalesList = delegator.findList("SalesReportImport", listerCond, null, null, null, false); 
	
				
		for(ps in platformSalesList){		
			sales = ps.productPrice + ps.shippingFee;	
			productFee =ps.productCost;	
			Timestamp previousFromDate = ObjectType.simpleTypeConvert("2010-01-01 00:00:00", "Timestamp", null, null);
			variableListRefund = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.puyuanName,"type","REFUND"), ["fromDate ASC"],false);
			variableListDomesticFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.puyuanName,"type","DOMESTICFEE"), ["fromDate ASC"],false);
			variableListListingFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.puyuanName,"type","LISTINGFEE"), ["fromDate ASC"],false);
			variableListMaterialFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.puyuanName,"type","MATERIALFEE"), ["fromDate ASC"],false);
			variableMaterialOrderCount = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.puyuanName,"type","MATERIALORDERCOUNT"), ["fromDate ASC"],false);
			variableListInterFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.puyuanName,"type","INTERNATIONALFEE"), ["fromDate ASC"],false);
					
			refundVariableValue = 0;
			for (refundVar in variableListRefund) {
				if(ps.date >= previousFromDate && ps.date < refundVar.fromDate){ previousFromDate = ObjectType.simpleTypeConvert("2010-01-01 00:00:00", "Timestamp", null, null); break;}
				if(ps.date >= refundVar.fromDate){refundVariableValue = refundVar.value;}
				previousFromDate = refundVar.fromDate;		
			}
			refund = sales * refundVariableValue;

			domesticVariableValue = 0;
			for (domesticVar in variableListDomesticFee) {
				if(ps.date >= previousFromDate && ps.date < domesticVar.fromDate){ previousFromDate = ObjectType.simpleTypeConvert("2010-01-01 00:00:00", "Timestamp", null, null); break;}
				if(ps.date >= domesticVar.fromDate){domesticVariableValue = domesticVar.value;}
				previousFromDate = domesticVar.fromDate;		
			}
			domesticFee = ps.productCost * domesticVariableValue;
		
			platformVariableValue = 0;
			for (platformVar in variableListListingFee) {
				if(ps.date >= previousFromDate && ps.date < platformVar.fromDate){previousFromDate = ObjectType.simpleTypeConvert("2010-01-01 00:00:00", "Timestamp", null, null);  break;}
				if(ps.date >= platformVar.fromDate){platformVariableValue = platformVar.value;}
				previousFromDate = platformVar.fromDate;		
			}
			platformFee =  sales * platformVariableValue;
					
			materialFeeVariableValue = 0;
			for (materialVar in variableListMaterialFee) {
				if(ps.date >= previousFromDate && ps.date < materialVar.fromDate){previousFromDate = ObjectType.simpleTypeConvert("2010-01-01 00:00:00", "Timestamp", null, null);  break;}
				if(ps.date >= materialVar.fromDate){materialFeeVariableValue = materialVar.value;}
				previousFromDate = materialVar.fromDate;		
			}
			materialFee = materialFeeVariableValue;
		
			internationalFeeVariableValue = 0;
			for (internationalVar in variableListInterFee) {
				if(ps.date >= previousFromDate && ps.date < internationalVar.fromDate){previousFromDate = ObjectType.simpleTypeConvert("2010-01-01 00:00:00", "Timestamp", null, null);  break;}
				if(ps.date >= internationalVar.fromDate){internationalFeeVariableValue = internationalVar.value;}
				previousFromDate = internationalVar.fromDate;		
			}
			internationalFee = ps.shippingCost +sales * internationalFeeVariableValue;
			itemProfit = sales - refund - platformFee - productFee - domesticFee - internationalFee - materialFee;
			sumSales += sales;	
			sumItemProfit += itemProfit;		
		}
		dailySumSales = sumSales / daily;
		
		dailyItemProfit = sumItemProfit / daily;
		data.dailySumSales = dailySumSales;
		data.dailyItemProfit = dailyItemProfit;
		if (dailySumSales == 0) {
			data.dailyprofitRatePercent = 0;
		} else {
			data.dailyprofitRatePercent = dailyItemProfit / dailySumSales ;
		}
		plist.add(data);
		
		if (totalSales == 0) {
			totalPercent = 0;
		} else {
			totalPercent = totalProfit / totalSales ;
		}
		totalSales += dailySumSales;
		totalProfit += dailyItemProfit;			
		
			
		sbuData.plist = plist;		
		sbuList.add(sbuData);
	}
		

	pfListData.platform =  pfl.roleTypeId;
	pfListData.sbuList = sbuList;
	pfList.add(pfListData);	
	
	allData.totalSales = totalSales;
	allData.totalProfit = totalProfit;
	allData.totalPercent = totalPercent;
	allList.add(allData);
}


context.a =allList;
context.s = pfList;








