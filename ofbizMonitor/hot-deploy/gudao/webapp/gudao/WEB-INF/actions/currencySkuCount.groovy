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
import org.ofbiz.base.util.ObjectType;
import org.ofbiz.base.util.UtilDateTime;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
context.ownerGroup = ownerGroup;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);

productId = parameters.productId;
currencyInput = parameters.currency;
context.productId =productId;

plist=[];
allList=[];
allData=[:];
tOneCount=0;
tTwoCount=0;
tThreeCount=0;
tFourCount=0;
tFiveCount=0;
tSixCount=0;
tSevenCount=0;	
platformList = delegator.findByAnd("Enumeration", UtilMisc.toMap("enumTypeId","GUDAO_PLATFORM"), ["description ASC"],false);
for(pfl in platformList ){
	oneCount=0;
	twoCount=0;
	threeCount=0;
	fourCount=0;
	fiveCount=0;
	sixCount=0;
	sevenCount=0;
	data=[:];
	if (UtilValidate.isEmpty(productId)) {
       		productId="随便写";    
        }
 	List<EntityCondition> salesSkuList = FastList.newInstance();
	  
	if (UtilValidate.isNotEmpty(productId)) {
       		 salesSkuList.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
    	}
	if (UtilValidate.isNotEmpty(currencyInput)) {
       		 salesSkuList.add(EntityCondition.makeCondition("currency", EntityOperator.EQUALS, currencyInput));
    	}
	salesSkuList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, pfl.enumCode));
 	
 	EntityCondition listerCond = null;
 	if (salesSkuList.size() > 0)	{
    		listerCond = EntityCondition.makeCondition(salesSkuList, EntityOperator.AND);

	}
	platformSalesList = delegator.findList("SalesReportImport", listerCond, null, null, null, false); 	
	Timestamp today = ObjectType.simpleTypeConvert(UtilDateTime.getXDayFromToday(nowTimestamp,0), "Timestamp", null, null);
	Timestamp yesterday = ObjectType.simpleTypeConvert(UtilDateTime.getXDayFromToday(nowTimestamp,1), "Timestamp", null, null);
	Timestamp beforeTwoDay = ObjectType.simpleTypeConvert(UtilDateTime.getXDayFromToday(nowTimestamp,2), "Timestamp", null, null);
	Timestamp beforeThreeDay = ObjectType.simpleTypeConvert(UtilDateTime.getXDayFromToday(nowTimestamp,3), "Timestamp", null, null);
	Timestamp beforeFourDay = ObjectType.simpleTypeConvert(UtilDateTime.getXDayFromToday(nowTimestamp,4), "Timestamp", null, null);
	Timestamp beforeFiveDay = ObjectType.simpleTypeConvert(UtilDateTime.getXDayFromToday(nowTimestamp,5), "Timestamp", null, null);
	Timestamp beforeSixDay = ObjectType.simpleTypeConvert(UtilDateTime.getXDayFromToday(nowTimestamp,6), "Timestamp", null, null);	
	for(ps in platformSalesList){				
		if(ps.date==today){oneCount += ps.orderItemQuantity;}
		else if(ps.date==yesterday){twoCount += ps.orderItemQuantity;}
		else if(ps.date==beforeTwoDay){threeCount += ps.orderItemQuantity;}
		else if(ps.date==beforeThreeDay){fourCount += ps.orderItemQuantity;}
		else if(ps.date==beforeFourDay){fiveCount += ps.orderItemQuantity;}
		else if(ps.date==beforeFiveDay){sixCount += ps.orderItemQuantity;}
		else if(ps.date==beforeSixDay){sevenCount += ps.orderItemQuantity;}
	}
	data.platform = pfl.enumCode;
	data.oneCount = oneCount;
	data.twoCount = twoCount;
	data.threeCount = threeCount;
	data.fourCount = fourCount;
	data.fiveCount = fiveCount;
	data.sixCount = sixCount;
	data.sevenCount = sevenCount;
	plist.add(data);
	
	tOneCount+=oneCount;
	tTwoCount+=twoCount;
	tThreeCount+=threeCount;
	tFourCount+=fourCount;
	tFiveCount+=fiveCount;
	tSixCount+=sixCount;
	tSevenCount+=sevenCount;	
}
	allData.tOneCount=tOneCount;
	allData.tTwoCount=tTwoCount;
	allData.tThreeCount=tThreeCount;
	allData.tFourCount=tFourCount;
	allData.tFiveCount=tFiveCount;
	allData.tSixCount=tSixCount;
	allData.tSevenCount=tSevenCount;	

allList.add(allData);
context.p=plist;
context.a=allList;

