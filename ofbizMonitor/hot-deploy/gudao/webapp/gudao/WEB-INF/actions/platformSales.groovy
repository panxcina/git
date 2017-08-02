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
import java.util.HashSet;
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
ownerInput = parameters.ownerGroup;
context.ownerInput= ownerInput;
context.fromDate=fromDateInput;
context.toDate= toDate;
Date fromDateDt= ObjectType.simpleTypeConvert(fromDateInput, "Date", null, null);	
Date toDateDt= ObjectType.simpleTypeConvert(toDate, "Date", null, null);		
context.fromDateDt = fromDateDt;
context.toDateDt = toDateDt;

plist=[];
allData=[:];
allList=[];

allSumSales = 0;
allSumRefund = 0;
allSumDomesticFee = 0;
allSumMaterialFee = 0;
allSumItemProfit = 0;
allprofitRatePercent = 0;
alldailySumSales = 0;
alldailyItemProfit = 0;
alldailyprofitRatePercent = 0;	

platformList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId","GUDAO_PLATFORM"), ["description ASC"],false);
for(pfl in platformList ){
	orderIdList=[];
 	data = [:];
        long daily =0;	
	sales=0;
	refund=0;
	platformFee=0;
	productFee=0;
	domesticFee=0;
	internationalFee=0;
	itemProfit=0;

	sumSales=0;	
	sumRefund=0;
	sumPlatformFee=0;
	sumProductFee=0;
	sumDomesticFee=0;
	sumMaterialFee=0;
	sumInternationalFee=0;
	sumItemProfit=0;
 	List<EntityCondition> salesReportList = FastList.newInstance();
	data.platform = pfl.enumCode;
	if (UtilValidate.isEmpty(ownerInput)){
	ownerInput=" ";
	} 
        if (UtilValidate.isNotEmpty(fromDateInput)){ 
	  	Timestamp fromDateTS = ObjectType.simpleTypeConvert(fromDateInput, "Timestamp", null, null);	
		fromMonth = fromDateTS.getMonth() + 1;
   	 	salesReportList.add(EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, fromDateTS));
	}
 	if (UtilValidate.isNotEmpty(toDate)) { 
	  	Timestamp toDateTS = ObjectType.simpleTypeConvert(toDate, "Timestamp", null, null);
   	  	salesReportList.add(EntityCondition.makeCondition("date", EntityOperator.LESS_THAN_EQUAL_TO, toDateTS));		
	}	
	if (UtilValidate.isNotEmpty(ownerInput)) { 	  	
   	  	salesReportList.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerInput));		
	}
 	salesReportList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, pfl.enumCode));
 	EntityCondition listerCond = null;
 	if (salesReportList.size() > 0)	{
    		listerCond = EntityCondition.makeCondition(salesReportList, EntityOperator.AND);
	}
	platformSalesList = delegator.findList("SalesReportImport", listerCond, null, null,null, false);
	
	if (UtilValidate.isNotEmpty(fromDateInput) && UtilValidate.isNotEmpty(toDate)){ 	
	daily = Math.abs(toDateDt.getTime() - fromDateDt.getTime()) / (24*60*60*1000) + 1;
	context.daily = daily;
	}

	for(ps in platformSalesList){
		sales = ps.productPrice + ps.shippingFee;	
		productFee =ps.productCost;	
		orderIdCount = ps.dailyOrderIdCount;
		Timestamp previousFromDate = ObjectType.simpleTypeConvert("2010-01-01 00:00:00", "Timestamp", null, null);
		variableListRefund = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.ownerGroup,"type","REFUND"), ["fromDate ASC"],false);
		variableListDomesticFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.ownerGroup,"type","DOMESTICFEE"), ["fromDate ASC"],false);
		variableListListingFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.ownerGroup,"type","LISTINGFEE"), ["fromDate ASC"],false);
		variableListMaterialFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.ownerGroup,"type","MATERIALFEE"), ["fromDate ASC"],false);
		variableMaterialOrderCount = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.ownerGroup,"type","MATERIALORDERCOUNT"), ["fromDate ASC"],false);			
		variableListInterFee = delegator.findByAnd("SalesReportVariable", UtilMisc.toMap("platform",ps.platform,"ownerGroup",ps.ownerGroup,"type","INTERNATIONALFEE"), ["fromDate ASC"],false);
					
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

		orderCountVariableValue = 0;
		for (orderCountVar in variableMaterialOrderCount) {
			if(ps.date >= previousFromDate && ps.date < orderCountVar.fromDate){previousFromDate = ObjectType.simpleTypeConvert("2010-01-01 00:00:00", "Timestamp", null, null);  break;}
			if(ps.date >= orderCountVar.fromDate){orderCountVariableValue = orderCountVar.value;}
			previousFromDate = orderCountVar.fromDate;		
		}		
		materialFee = materialFeeVariableValue;
	
		internationalFeeVariableValue = 0;
		for (internationalVar in variableListInterFee) {
			if(ps.date >= previousFromDate && ps.date < internationalVar.fromDate){previousFromDate = ObjectType.simpleTypeConvert("2010-01-01 00:00:00", "Timestamp", null, null);  break;}
			if(ps.date >= internationalVar.fromDate){internationalFeeVariableValue = internationalVar.value;}
			previousFromDate = internationalVar.fromDate;		
		}
		internationalFee = ps.shippingCost +sales * internationalFeeVariableValue;		
		itemProfit =sales - refund - platformFee - productFee - domesticFee - internationalFee - materialFee;
		
		sumSales += sales;	
		sumRefund += refund;
		sumPlatformFee += platformFee;
		sumProductFee += productFee;
		sumDomesticFee += domesticFee;
		sumInternationalFee += internationalFee;
		sumMaterialFee += materialFee;
		sumItemProfit += itemProfit;	
	}		

	data.sumSales = sumSales;
	data.sumRefund = sumRefund;
	data.sumProductFee = sumProductFee;
	data.sumDomesticFee = sumDomesticFee;
	data.sumInternationalFee = sumInternationalFee;
	data.sumDomesticFee = sumDomesticFee;
	data.sumMaterialFee = sumMaterialFee;	
	data.sumItemProfit = sumItemProfit;
	if(sumSales == 0) {profitRatePercent = 0;}
	else{profitRatePercent = sumItemProfit / sumSales;}		
	data.profitRatePercent = profitRatePercent;
	
	if(daily ==0) { daily=1 ;}
        dailySumSales = sumSales / daily;
        data.dailySumSales = dailySumSales;
	dailyItemProfit = sumItemProfit / daily;
	data.dailyItemProfit = dailyItemProfit;
	if(dailySumSales == 0) {dailyprofitRatePercent = 0;}
	else{dailyprofitRatePercent = dailyItemProfit / dailySumSales ;}	
	data.dailyprofitRatePercent = dailyprofitRatePercent;			
	plist.add(data); 

	allSumSales += sumSales;
	allSumRefund += sumRefund;
	allSumDomesticFee += sumDomesticFee;
	allSumMaterialFee += sumMaterialFee;
	allSumItemProfit += sumItemProfit;	
	if(allSumSales == 0) {allprofitRatePercent =0;}
	else{allprofitRatePercent = allSumItemProfit /allSumSales;}
		
	allData.allSumSales = allSumSales;
	allData.allSumRefund = allSumRefund;
	allData.allSumDomesticFee = allSumDomesticFee;
	allData.allSumMaterialFee = allSumMaterialFee;
	allData.allSumItemProfit = allSumItemProfit;
	allData.allprofitRatePercent = allprofitRatePercent;

	alldailySumSales +=  dailySumSales;
	alldailyItemProfit += dailyItemProfit;	
	if(alldailySumSales == 0) {alldailyprofitRatePercent =0;}
	else{alldailyprofitRatePercent = alldailyItemProfit /alldailySumSales;}		
	allData.alldailySumSales = alldailySumSales;
	allData.alldailyItemProfit = alldailyItemProfit;
	allData.alldailyprofitRatePercent = alldailyprofitRatePercent;		
}	
allList.add(allData);	
context.p = plist;
context.a = allList;

