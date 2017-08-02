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
import org.ofbiz.order.order.OrderListState;
import org.ofbiz.entity.model.DynamicViewEntity;


productStore = request.getParameter("productStore");
orderStatus = request.getParameter("orderStatus");
shipToCountry = request.getParameter("shipToCountry");
defaultEmailTemplate = request.getParameter("defaultEmailTemplate");

//get eBay account
ebayAccounts = delegator.findList("ProductStore", null, null, ["storeName ASC"], null, false);
context.ebayAccounts = ebayAccounts;

//get ToBeShipped
list = UtilMisc.toList(
                    EntityCondition.makeCondition("orderTypeId", EntityOperator.EQUALS, "SALES_ORDER")
                    );
if (!(productStore == null)) {
    if (!(ebayAccount.equals("All"))) {
        list.add(EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStore));
    }
}
/*if (!(productStore == null)) {
    if (!(productStore("All"))) {
        cond = EntityCondition.makeCondition(UtilMisc.toList(
                                                    EntityCondition.makeCondition("statusId", EntityOperator.IN, UtilMisc.toList("ORDER_APPROVED", "ORDER_HOLD")),
                                                    EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, productStore)
                                                    )
                                            ,EntityOperator.AND);
    }
}*/

if (!(orderStatus == null) && !orderStatus.equals("All")) {
    if (!(orderStatus.equals("All"))) {
        list.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, orderStatus));
    }
} else {
    list.add(EntityCondition.makeCondition("statusId", EntityOperator.IN, UtilMisc.toList("ORDER_APPROVED", "ORDER_HOLD")));
}

if (!(shipToCountry == null) && !shipToCountry.equals("All")) {
    if (!(shipToCountry.equals("All"))) {
        list.add(EntityCondition.makeCondition("countryGeoId", EntityOperator.EQUALS, shipToCountry));
    }
}

cond = EntityCondition.makeCondition(list, EntityOperator.AND);
orderLists = delegator.findList("ToBeShipped", cond, null, ["createdStamp ASC"], null, false);
//context.orderLists = orderLists;

expressLists = [];
epacketLists = [];
standardLists = [];
for (orderList in orderLists) {
    
    if (orderList.shipmentMethodTypeId.equals("EXPRESS") || orderList.shipmentMethodTypeId.equals("EMS") || orderList.shipmentMethodTypeId.equals("YW_EXPRESS"))    {
        expressLists.add(orderList);
    }
    
    else if (orderList.shipmentMethodTypeId.equals("EPACKET") || orderList.shipmentMethodTypeId.equals("NO_SHIPPING"))    {
        epacketLists.add(orderList);
    }
    else {
        standardLists.add(orderList);
    }
    
}

context.orderCount = orderLists.size();
context.expressLists = expressLists;
context.epacketLists = epacketLists;
context.standardLists = standardLists;

//get EbayEmailTemplate
emailTemplates = delegator.findList("EbayEmailTemplate", null, null, null, null, false);
context.emailTemplates = emailTemplates;
context.defaultEmailTemplate = defaultEmailTemplate;