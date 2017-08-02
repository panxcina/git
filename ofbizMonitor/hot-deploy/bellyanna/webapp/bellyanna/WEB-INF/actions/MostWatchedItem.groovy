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
categoryId = parameters.categoryId;
soldInLastXdays = parameters.soldInLastXdays;
list = [];

if(categoryId==null) {
    categoryId = "63861";
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



condition = EntityCondition.makeCondition("categoryId", EntityOperator.EQUALS, categoryId);
mostWatchedLists = delegator.findList("EbayMostWatchedItem", condition, null, ["watchCount DESC"], null, false);

for (mostWatchedList in mostWatchedLists) {
    list.add(mostWatchedList);
    
}
context.mostWatchedLists = list;
context.mostWatchedCount = mostWatchedLists.size();