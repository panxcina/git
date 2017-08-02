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

orderId = parameters.orderId;
userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");

if (UtilValidate.isNotEmpty(orderId)) {
productGqsDetail = delegator.findOne("ProductGqsDetail", UtilMisc.toMap("orderId", orderId), false);
    productGqsDetailProduct = delegator.findByAnd("ProductGqsDetailProduct", UtilMisc.toMap("orderId", orderId), null, false);
    if (productGqsDetail.createdBy.equals(userLoginId)) {
        context.action = "EDIT";
    } else {
        context.action = "VIEW";
    }
    context.productGqsDetail = productGqsDetail;
    context.productGqsDetailProduct = productGqsDetailProduct;
    context.skuSeq = productGqsDetailProduct.size();
}
else {
    context.action = "CREATE";
    context.skuSeq = 1;
}


