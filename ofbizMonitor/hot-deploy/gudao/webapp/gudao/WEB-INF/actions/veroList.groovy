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
productName = parameters.productName;
ownerGroupInput = parameters.ownerGroupInput;

resultList=[];
count = 0;
EntityCondition pmVeroCond = EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "侵权下架");

productPlatformVeroList = delegator.findList("ProductMasterStatus",
                                                    pmVeroCond,
                                                    UtilMisc.toSet("productId"),
                                                    UtilMisc.toList("productId"),
                                                    new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);

for (productPlatformVero in productPlatformVeroList) {
    showResult = false;
    data = [:];
    productMaster = delegator.findOne("ProductMaster", UtilMisc.toMap("productId", productPlatformVero.productId), false);
    data.productId = productMaster.productId;
    data.chineseName = productMaster.chineseName;
    data.englishName = productMaster.englishName;
    data.productGroup = productMaster.productGroup;
    data.ownerGroup = productMaster.ownerGroup;
    data.statusId = productMaster.statusId;
    data.categoryIdParent = productMaster.categoryIdParent;
    data.categoryIdChild = productMaster.categoryIdChild;
    productPlatformList = delegator.findByAnd("ProductMasterStatus", UtilMisc.toMap("productId", productId), null, false);
    data.productPlatformList = productPlatformList;
    if (UtilValidate.isNotEmpty(productId)) {
        if (productMaster.productId.contains(productId)) {
            showResult = true;
        }
    }

    if (UtilValidate.isNotEmpty(productName)) {
        if (productMaster.chineseName.contains(productName) || productMaster.englishName.contains(productName)) {
            showResult = true;
        }
    }

    if (UtilValidate.isNotEmpty(ownerGroupInput)) {
        if (productMaster.ownerGroup.contains(ownerGroupInput)) {
            showResult = true;
        }
    }

    if (UtilValidate.isEmpty(productId) && UtilValidate.isEmpty(productName) && UtilValidate.isEmpty(ownerGroupInput)) {
        showResult = true;
    }
    if (showResult) {
        resultList.add(data);
        count++;
    }

}


context.productCount = count;
context.veroList = resultList;


