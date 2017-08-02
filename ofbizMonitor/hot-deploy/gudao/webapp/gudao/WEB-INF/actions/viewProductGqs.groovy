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

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");

platform = parameters.platform;
channel = parameters.channel;
type = parameters.type;
shipType = parameters.shipType;
ownerGroup = parameters.ownerGroup;
account = parameters.account;
orderId = parameters.orderId;
customerId = parameters.customerId;
createdBy = parameters.createdBy;



List<EntityCondition> condsList = FastList.newInstance();

if (UtilValidate.isNotEmpty(type)) {
    if (!type.equals("ALL")) {
        condsList.add(EntityCondition.makeCondition("type", EntityOperator.EQUALS, type));
    }
}

if (UtilValidate.isNotEmpty(shipType)) {
    if (!shipType.equals("ALL")) {
        condsList.add(EntityCondition.makeCondition("shipType", EntityOperator.EQUALS, shipType));
    }
}

if (UtilValidate.isNotEmpty(platform)) {
    if (!platform.equals("ALL")) {
        condsList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, platform));
    }
}

if (UtilValidate.isNotEmpty(channel)) {
    if (!channel.equals("ALL")) {
        condsList.add(EntityCondition.makeCondition("channel", EntityOperator.EQUALS, channel));
    }
}

if (UtilValidate.isNotEmpty(ownerGroup)) {
    if (!ownerGroup.equals("ALL")) {
        condsList.add(EntityCondition.makeCondition("ownerGroup", EntityOperator.EQUALS, ownerGroup));
    }
}

if (UtilValidate.isNotEmpty(account)) {
    condsList.add(EntityCondition.makeCondition("account", EntityOperator.EQUALS, account));
}

if (UtilValidate.isNotEmpty(orderId)) {
    condsList.add(EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId));
}

if (UtilValidate.isNotEmpty(customerId)) {
    condsList.add(EntityCondition.makeCondition("customerId", EntityOperator.EQUALS, customerId));
}

if (UtilValidate.isNotEmpty(createdBy)) {
    if (!createdBy.equals("ALL")) {
        condsList.add(EntityCondition.makeCondition("createdBy", EntityOperator.EQUALS, createdBy));
    }
}

EntityCondition cond = null;
if (condsList.size() > 0) {
    cond = EntityCondition.makeCondition(condsList, EntityOperator.AND);
}


resultList=[];
productGqsDetailList = delegator.findList("ProductGqsDetail", cond, null, UtilMisc.toList("createdStamp DESC"), null, false);
rowCount = 1;
for (productGqsDetail in productGqsDetailList) {
    data = [:];
    data.platform = productGqsDetail.platform;
    data.channel = productGqsDetail.channel;
    data.type = productGqsDetail.type;
    data.shipType = productGqsDetail.shipType;
    data.account = productGqsDetail.account;
    data.orderId = productGqsDetail.orderId;
    data.customerId = productGqsDetail.customerId;
    data.ownerGroup = productGqsDetail.ownerGroup;
    data.description = productGqsDetail.description;
    data.currency = productGqsDetail.currency;
    data.refundAmount = productGqsDetail.refundAmount;
    data.replacementProductCost = productGqsDetail.replacementProductCost;
    data.replacementShippingCost = productGqsDetail.replacementShippingCost;
    data.countryDestination = productGqsDetail.countryDestination;
    data.fullRefund = productGqsDetail.fullRefund;
    data.partialRefund = productGqsDetail.partialRefund;
    data.replacement = productGqsDetail.replacement;
    data.noAction = productGqsDetail.noAction;
    data.createdBy = productGqsDetail.createdBy;
    data.createdStamp = productGqsDetail.createdStamp;
    data.action = "NONE";
    if (productGqsDetail.createdBy.equals(userLoginId)) {
        data.action = "EDIT";
    }
    resultList.add(data);
    rowCount++;
    if (rowCount > 500) {
        break;
    }

}


context.productGqsDetailList = resultList;
