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
import com.gudao.product.productService;
import javolution.util.FastList;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");

userLoginIdInput = parameters.userLoginIdInput;
context.userLoginIdVar = userLoginIdInput;

distinctUserLoginIdList = delegator.findList("UserLogin", 
													EntityCondition.makeCondition(UtilMisc.toList(

														EntityCondition.makeCondition("partyId",EntityOperator.EQUALS,userLoginPartyId ),
														EntityCondition.makeCondition("enabled",EntityOperator.EQUALS,"Y" ),
														EntityCondition.makeCondition("externalAuthId",EntityOperator.NOT_EQUAL,"ADMIN" )
		)),null,null,null,false);
context.userLoginList = distinctUserLoginIdList;
allMotherSkuList=[];
if(UtilValidate.isNotEmpty(userLoginIdInput)){

	List<EntityCondition> findSbuList = FastList.newInstance();
	if(UtilValidate.isNotEmpty(userLoginIdInput)){
		findSbuList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginIdInput));
	}
	
	findSbuList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
	findSbuList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"));
	
	EntityCondition listerConds = null;
	if (findSbuList.size() > 0)	{
		listerConds = EntityCondition.makeCondition(findSbuList, EntityOperator.AND);
	}
	allMotherSkuListGV =  delegator.findList("ProductMonitoringList", listerConds, UtilMisc.toSet("motherSku"), UtilMisc.toList("motherSku"),new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),false);
	for(allMother in allMotherSkuListGV){
		data=[:];
		isNew = false;
		oneMotherSku = allMother.motherSku;
		userLoginIdName = null; 
		findItemIdList = delegator.findByAnd("ProductMonitoringList",UtilMisc.toMap("motherSku",oneMotherSku),null,false);
		if (findItemIdList.size() == 1) {
			checkItemId = EntityUtil.getFirst(findItemIdList).getString("itemId");
			userLoginIdName = EntityUtil.getFirst(findItemIdList).getString("userLoginId");
			if (checkItemId.equals("NEW")) {
				isNew = true;
			}
		}
		data.motherSku=oneMotherSku;
		data.userLoginId= userLoginIdName;
		data.isNew=isNew;
		allMotherSkuList.add(data);
	}
	

}

context.motherSkuList = allMotherSkuList;







