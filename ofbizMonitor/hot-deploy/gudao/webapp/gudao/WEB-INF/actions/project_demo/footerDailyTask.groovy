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
Calendar now3 = Calendar.getInstance();
Calendar now4 = Calendar.getInstance();
Calendar now5 = Calendar.getInstance();
Calendar now6 = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
// 当前时间
Timestamp nowTimestamp = Timestamp.valueOf(sdf.format(now3.getTime()));
Timestamp nowTimestamp1 = Timestamp.valueOf(sdf.format(now6.getTime()));

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




dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
dayOfWeek4 = now4.get(Calendar.DAY_OF_WEEK);
dayOfWeekList = ["EMPTY", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"];
context.today = dayOfWeekList[dayOfWeek];
// context.dayOfWeek4 = dayOfWeekList[dayOfWeek4];
context.dayOfWeek4 = dayOfWeek4;
context.dayOfWeek1 = thisMonday;
context.nowTimestamp = nowTimestamp;
context.nowTimestamp1 = nowTimestamp1;


// 左下角 DailyTaskList 当天显示列表
list4 = [];
EntityCondition condition4 = null;
List<EntityCondition> conditionList4 = FastList.newInstance();

conditionList4.add(EntityCondition.makeCondition("owner", EntityOperator.EQUALS, userLoginId));
conditionList4.add(EntityCondition.makeCondition(dayOfWeekList[dayOfWeek], EntityOperator.EQUALS, "1"));

condition4 = EntityCondition.makeCondition(conditionList4, EntityOperator.AND);
dailyTaskList4 = delegator.findList("DailyTaskList", condition4, null, ["priority DESC", "taskName"], null, false);
context.list4 = dailyTaskList4;
for (dailyTask in dailyTaskList4) {
    data = [:];
    data.owner = userLoginId;
    data.owner = dailyTask.owner;
    data.taskName = dailyTask.taskName;
    data.description = dailyTask.description;
    data.notes = dailyTask.notes;
    data.monday = dailyTask.monday;
    data.tuesday = dailyTask.tuesday;
    data.wednesday = dailyTask.wednesday;
    data.thursday = dailyTask.thursday;
    data.friday = dailyTask.friday;
    data.createdBy = dailyTask.createdBy;
    data.priority = dailyTask.priority;
    data.occurrenceValue = dailyTask.occurrenceValue;

        list4.add(data);
    
}
context.list4 = list4;


// 当天 DailyTaskRecord 表记录数据
list6 = [];
EntityCondition condition6 = null;
List<EntityCondition> conditionList6 = FastList.newInstance();

// 时间区间
List criteria1 = new LinkedList();
 criteria1.add(thisToday);
 criteria1.add(nowTimestamp1);
 conditionList6.add(EntityCondition.makeCondition("completedDate", EntityOperator.BETWEEN, criteria1));
 conditionList6.add(EntityCondition.makeCondition("owner", EntityOperator.EQUALS, userLoginId));

 condition6 = EntityCondition.makeCondition(conditionList6, EntityOperator.AND);

dailyTaskRecord6 = delegator.findList("DailyTaskRecord", condition6, null, ["completedDate DESC"], null, false);
context.dailyTaskRecord6 = dailyTaskRecord6;
for (dailyTaskRecord in dailyTaskRecord6) {
    data = [:];
    //data.owner = userLoginId;
    data.owner = dailyTaskRecord.owner;
    data.taskName = dailyTaskRecord.taskName;
    data.completedDate = dailyTaskRecord.completedDate;

    list6.add(data);
}
context.dailyTaskRecord6 = dailyTaskRecord6;
context.thisToday = thisToday;




