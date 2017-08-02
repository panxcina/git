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

orderHeader = null;
refund = null;
orderId = request.getParameter("orderId");
refundId = request.getParameter("refundId");

if(!orderId && !refundId) {return}
//println("orderId" + orderId);

if(refundId) {
	// query by refundId
	refund = delegator.findOne("Refund", [refundId : refundId], false);
	if(refund && !orderId) {
		orderId = refund.orderId;
	} 
}

if(orderId) {
	orderHeader = delegator.findOne("OrderHeader", [orderId : orderId], false);
}

if(!orderHeader) {
	return;
}

if(!(orderHeader || refund)) {
	//println('No record!');
	return;
}

if(!refund) {
	
	refund = [:];
	
	refund.orderId = orderId;
	
	orderPaymentPreference = EntityUtil.getFirst(delegator.findByAnd("OrderPaymentPreference", [orderId : orderId], null, false));
	if(orderPaymentPreference) {
		paypalGatewayResponse = EntityUtil.getFirst(orderPaymentPreference.getRelated("PaymentGatewayResponse", null, null, false));
		refund.paymentMethodTypeId = orderPaymentPreference.paymentMethodTypeId;
		if(paypalGatewayResponse) {
			refund.paypalTransactionId = paypalGatewayResponse.referenceNum;
		}
	}
    
}

context.orderHeader = orderHeader;
context.refund = refund;