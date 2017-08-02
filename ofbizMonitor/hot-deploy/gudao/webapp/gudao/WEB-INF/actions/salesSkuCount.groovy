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

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
context.ownerGroup = ownerGroup;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);




//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//fromDateTS = new java.sql.Timestamp(sdf.parse(fromDate));
fromDate = parameters.minDate;
toDate  = parameters.maxDate;
productId = parameters.productId;


context.minDate = fromDate;
context.maxDate = toDate;
//context.productId = productId;



plist=[];
skuList=[];

	if (UtilValidate.isEmpty(productId)) {
       		productId="随便写";    
        }
 	List<EntityCondition> salesSkuList = FastList.newInstance();
		
        if (UtilValidate.isNotEmpty(fromDate)){ 
	  	Timestamp fromDateTS = ObjectType.simpleTypeConvert(fromDate, "Timestamp", null, null);	  		
   	 	salesSkuList.add(EntityCondition.makeCondition("date", EntityOperator.GREATER_THAN_EQUAL_TO, fromDateTS));
	}
 	if (UtilValidate.isNotEmpty(toDate)) { 
	  	Timestamp toDateTS = ObjectType.simpleTypeConvert(toDate, "Timestamp", null, null);
   	  	salesSkuList.add(EntityCondition.makeCondition("date", EntityOperator.LESS_THAN_EQUAL_TO, toDateTS));
		
	}
	if (UtilValidate.isNotEmpty(productId)) {
       		 salesSkuList.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
    	}
 	EntityCondition listerCond = null;
 	if (salesSkuList.size() > 0)	{
    		listerCond = EntityCondition.makeCondition(salesSkuList, EntityOperator.AND);
	}
 	platformSalesList = delegator.findList("SalesReportImport", listerCond, null, null, null, false); 
	distinctDateList = delegator.findList( "SalesReportImport",
                                            listerCond,
                                            UtilMisc.toSet("date"),
                                            UtilMisc.toList("date"),
                                            new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                        false);
	context.distinctDateList =distinctDateList;

for(ds in distinctDateList){
	length=0;
	data=[:];
	
	aliexCount=0;
	amzonCount=0;
	cdisCount=0;
	ebayCount=0;
	lazadaCount=0;
	wishCount=0;
	singleCount = platformSalesList.orderItemQuantity;
	singleDate = platformSalesList.date;
	sgPlatform = platformSalesList.platform;

		for(length=0;length< platformSalesList.size();length++){		
			if(singleDate[length]==ds.date){
								
				if(sgPlatform[length].equals("ALIEXPRESS")){aliexCount += singleCount[length];data.aliexCount = aliexCount;} 
				else if(sgPlatform[length].equals("AMAZON")){amzonCount += singleCount[length];data.amzonCount = amzonCount;}
				else if(sgPlatform[length].equals("CDIS")){cdisCount += singleCount[length];data.cdisCount = cdisCount;}  
				else if(sgPlatform[length].equals("EBAY")){ebayCount +=singleCount[length];data.ebayCount = ebayCount;}  
				else if(sgPlatform[length].equals("LAZADA")){lazadaCount += singleCount[length];data.lazadaCount = lazadaCount;} 
				else if(sgPlatform[length].equals("WISH")){wishCount += singleCount[length];data.wishCount = wishCount;}  
			}
						
		} 
	
	data.date = ds.date;	
	plist.add(data);

}
context.p=plist;
context.count = distinctDateList.size();


