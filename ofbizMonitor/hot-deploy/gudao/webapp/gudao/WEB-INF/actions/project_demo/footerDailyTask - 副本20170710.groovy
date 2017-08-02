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

import java.sql.Timestamp;
import java.util.Calendar;
// import java.util.Date;
// import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javolution.util.FastMap;
import javolution.util.FastList;


userLogin = parameters.userLogin;
userLoginId = null;
    if(UtilValidate.isNotEmpty(userLogin)) {
        userLoginId = userLogin.userLoginId;
        context.userLoginId = userLoginId;
    }












now = Calendar.getInstance();
now4 = Calendar.getInstance();
now5 = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
dayOfWeekList = ["EMPTY", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"];
context.today = dayOfWeekList[dayOfWeek];

//获取本周一日期
now4.add(Calendar.DAY_OF_MONTH, -1); //解决周日会出现 并到下一周的情况
now4.set(now4.get(Calendar.YEAR),now4.get(Calendar.MONDAY),now4.get(Calendar.DAY_OF_MONTH), 1, 0, 0);
now4.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
Timestamp thisMonday = Timestamp.valueOf(sdf.format(now4.getTime()));


now5.set(Calendar.HOUR_OF_DAY, 1);
now5.set(Calendar.SECOND, 0);
now5.set(Calendar.MINUTE, 0);
now5.set(Calendar.MILLISECOND, 001);
Timestamp thisToday = Timestamp.valueOf(sdf.format(now5.getTime()));
Timestamp nowTimestamp1 = Timestamp.valueOf(sdf.format(now.getTime()));

context.dayOfWeek = nowTimestamp1;
context.dayOfWeek1 = thisMonday;
context.thisToday = thisToday;

list = [];
EntityCondition condition = null;
List<EntityCondition> conditionList = FastList.newInstance();

conditionList.add(EntityCondition.makeCondition("owner", EntityOperator.EQUALS, userLoginId));
conditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
conditionList.add(EntityCondition.makeCondition(dayOfWeekList[dayOfWeek], EntityOperator.EQUALS, "1"));

condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
dailyTaskList = delegator.findList("DailyTaskList", condition, null, ["priority DESC", "taskName"], null, false);
context.list = dailyTaskList;
for (dailyTask in dailyTaskList) {
    data = [:];
    data.owner = userLoginId;
    data.taskName = dailyTask.taskName;
    data.description = dailyTask.description;
    data.notes = dailyTask.notes;
    data.createdBy = dailyTask.createdBy;
    data.priority = dailyTask.priority;

    EntityCondition condition6 = null;
    List<EntityCondition> conditionList6 = FastList.newInstance();
    List criteria1 = new LinkedList();
    criteria1.add(thisToday);
    criteria1.add(nowTimestamp1);
    conditionList6.add(EntityCondition.makeCondition("completedDate", EntityOperator.BETWEEN, criteria1));
    conditionList6.add(EntityCondition.makeCondition("owner", EntityOperator.EQUALS, "userLoginId"));
    conditionList6.add(EntityCondition.makeCondition("taskName", EntityOperator.EQUALS, dailyTask.taskName));

    condition6 = EntityCondition.makeCondition(conditionList6, EntityOperator.AND);

    dailyTaskRecord6 = delegator.findList("DailyTaskRecord", condition6, null, ["completedDate DESC"], null, false);


    if(UtilValidate.isEmpty(dailyTaskRecord6)) {
        list.add(data);
    }

}

context.dailyTaskRecord6 = dailyTaskRecord6;


dayOfMonth = now.get(Calendar.DAY_OF_MONTH);
context.dayOfMonth = dayOfMonth;
EntityCondition condition2 = null;
List<EntityCondition> conditionList2 = FastList.newInstance();

conditionList2.add(EntityCondition.makeCondition("owner", EntityOperator.EQUALS, userLoginId));
conditionList2.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
conditionList2.add(EntityCondition.makeCondition("occurrence", EntityOperator.EQUALS, "DATE"));

condition2 = EntityCondition.makeCondition(conditionList2, EntityOperator.AND);
dailyTaskList2 = delegator.findList("DailyTaskList", condition2, null, ["priority DESC", "taskName"], null, false);
for (dailyTask in dailyTaskList2) {
    data = [:];
    data.owner = userLoginId;
    data.taskName = dailyTask.taskName;
    data.description = dailyTask.description;
    data.notes = dailyTask.notes;
    data.createdBy = dailyTask.createdBy;
    data.priority = dailyTask.priority;
    addRecord = false;
    if (dailyTask.occurrenceType.equals("BEFORE")) {
        if (dayOfMonth < Integer.valueOf(dailyTask.occurrenceValue)) {
            addRecord = true;
        }
    } else if (dailyTask.occurrenceType.equals("AFTER")) {
        if (dayOfMonth > Integer.valueOf(dailyTask.occurrenceValue)) {
            addRecord = true;
        }
    } else if (dailyTask.occurrenceType.equals("EXACT")) {
        if (dayOfMonth == Integer.valueOf(dailyTask.occurrenceValue)) {
            addRecord = true;
        }
    }
    if (addRecord) {
        list.add(data);
    }
}



weekOfMonth = now.get(Calendar.WEEK_OF_MONTH);
context.weekOfMonth = weekOfMonth;
EntityCondition condition3 = null;
List<EntityCondition> conditionList3 = FastList.newInstance();

conditionList3.add(EntityCondition.makeCondition("owner", EntityOperator.EQUALS, userLoginId));
conditionList3.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
conditionList3.add(EntityCondition.makeCondition("occurrence", EntityOperator.EQUALS, "WEEK"));

condition3 = EntityCondition.makeCondition(conditionList3, EntityOperator.AND);
dailyTaskList3 = delegator.findList("DailyTaskList", condition3, null, ["priority DESC", "taskName"], null, false);
for (dailyTask in dailyTaskList3) {
    data = [:];
    data.owner = userLoginId;
    data.taskName = dailyTask.taskName;
    data.description = dailyTask.description;
    data.notes = dailyTask.notes;
    data.createdBy = dailyTask.createdBy;
    data.priority = dailyTask.priority;
    addRecord = false;
    if (dailyTask.occurrenceType.equals("BEFORE")) {
        if (weekOfMonth < Integer.valueOf(dailyTask.occurrenceValue)) {
            addRecord = true;
        }
    } else if (dailyTask.occurrenceType.equals("AFTER")) {
        if (weekOfMonth > Integer.valueOf(dailyTask.occurrenceValue)) {
            addRecord = true;
        }
    } else if (dailyTask.occurrenceType.equals("EXACT")) {
        if (weekOfMonth == Integer.valueOf(dailyTask.occurrenceValue)) {
            addRecord = true;
        }
    }
    if (addRecord) {
        list.add(data);
    }
}
context.list = list;
