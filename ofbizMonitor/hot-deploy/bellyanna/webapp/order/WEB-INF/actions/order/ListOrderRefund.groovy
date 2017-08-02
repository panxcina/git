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

partyId = request.getParameter("partyId");
facilityId = request.getParameter("facilityId");

state = OrderListState.getInstance(request);
state.update(request);
context.state = state;

/* check permission for each order type
hasPermission = false;
if (security.hasEntityPermission("ORDERMGR", "_VIEW", session)) {
    if (state.hasType("view_SALES_ORDER") || (!(state.hasType("view_SALES_ORDER")) && !(state.hasType("view_PURCHASE_ORDER")))) {
        hasPermission = true;
        salesOrdersCondition = EntityCondition.makeCondition("orderTypeId", EntityOperator.EQUALS, "SALES_ORDER");
    }
}
if (security.hasEntityPermission("ORDERMGR", "_PURCHASE_VIEW", session)) {
    if (state.hasType("view_PURCHASE_ORDER") || (!(state.hasType("view_SALES_ORDER")) && !(state.hasType("view_PURCHASE_ORDER")))) {
        hasPermission = true;
        purchaseOrdersCondition = EntityCondition.makeCondition("orderTypeId", EntityOperator.EQUALS, "PURCHASE_ORDER");
    }
}
context.hasPermission = hasPermission;
*/
condition = null;

parameters.statusId = parameters.statusId != null ? parameters.statusId : "REFUND_PENDING"

if(parameters.statusId) {
	condition = EntityCondition.makeCondition([statusId : parameters.statusId]);
}
refundList = delegator.findList("Refund", condition, null, ["createdStamp DESC"], null, false);
context.refundList = refundList;