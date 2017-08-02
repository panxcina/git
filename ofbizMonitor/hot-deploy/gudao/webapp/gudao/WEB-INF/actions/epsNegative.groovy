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
ownerGroup = StringUtils.remove(userLoginPartyId, "_GROUP");
context.ownerGroup = ownerGroup;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);

productId = parameters.productId;
ownerGroupInput = parameters.ownerGroupInput;
    if (UtilValidate.isEmpty(productId) && UtilValidate.isEmpty(ownerGroupInput)) {
       productId=" ";   
    }
plist=[];
count=0;

List<EntityCondition> pmCondsname = FastList.newInstance();
    if (UtilValidate.isNotEmpty(productId)) {
        pmCondsname.add(EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%" + productId + "%"));
    }

   
    if (UtilValidate.isNotEmpty(ownerGroupInput)) {
        pmCondsname.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroupInput));
    }
    pmCondsname.add(EntityCondition.makeCondition("statusId", EntityOperator.NOT_LIKE,  "%停产"));

    EntityCondition pmCond = null;
    if (pmCondsname.size() > 0) {
        pmCond = EntityCondition.makeCondition(pmCondsname, EntityOperator.AND);
    }
   
    pmList = delegator.findList("ProductMasterResult", pmCond, null, ["finalEps"], null, false); 
     for (pm in pmList) {
   	
   	if(pm.finalEps < 0){
	    count++;
            plist.add(pm); 
		if(count>=500){
			break;
		}      
	}
    }
context.productCount = count;    	
context.p = plist;
   
       	           




