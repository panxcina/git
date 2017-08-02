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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
/*import java.text.DateFormat;*/
import java.text.SimpleDateFormat;

condition = null;
productId = parameters.productId;
soldInLastXdays = parameters.soldInLastXdays;
list = [];

if(productId==null) {
    productId = "impossibleone";
}

if (soldInLastXdays == null) {
    soldInLastXdays = 30;
}

fromDay = Calendar.getInstance();
toDay = Calendar.getInstance();
fromDay.set(Calendar.DATE, fromDay.get(Calendar.DATE) - soldInLastXdays.toInteger());
toDay.set(Calendar.DATE, toDay.get(Calendar.DATE));
sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
fromDate = Timestamp.valueOf(sdf.format(fromDay.getTime()));
toDate = Timestamp.valueOf(sdf.format(toDay.getTime()));



condition = EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%" + productId.toUpperCase() + "%");
productSearchList = delegator.findList("Product", condition, null, ["productId ASC"], null, false);

for (productList in productSearchList) {
    atp = 0;
    qoh = 0;
    discontinueDate = null;
    productId = productList.productId;
    if (productList.salesDiscontinuationDate != null) {
        discontinueDate = productList.salesDiscontinuationDate;
    }
    inventoryItemList = delegator.findByAnd("InventoryItem", UtilMisc.toMap("productId", productId), null, false);
    for (inventoryItem in inventoryItemList) {
        atp += inventoryItem.availableToPromiseTotal;
        qoh += inventoryItem.quantityOnHandTotal;
    }
    
    soldCondition = EntityCondition.makeCondition(UtilMisc.toList(
                                                            EntityCondition.makeCondition("productId",EntityOperator.EQUALS ,productId),
                                                            EntityCondition.makeCondition("createdStamp",EntityOperator.GREATER_THAN ,fromDate),
                                                            EntityCondition.makeCondition("createdStamp",EntityOperator.LESS_THAN ,toDate)
                                                            )
                                            );
    soldQty = 0;
    orderItemList = delegator.findList("OrderItem", soldCondition, null, ["productId ASC"], null, false);
    for (orderItem in orderItemList) {
        soldQty += orderItem.quantity;
    }
    
    data = [:];
    data.atp = atp;
    data.qoh = qoh;
    data.productId = productId;
    data.discontinueDate = discontinueDate;
    data.soldQty = soldQty;
    list.add(data);
}
context.productSearchList = list;
context.productCount = productSearchList.size();