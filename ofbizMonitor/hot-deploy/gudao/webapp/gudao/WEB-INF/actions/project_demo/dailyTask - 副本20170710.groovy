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



// this is just category
// ***************start**********************
treeType = parameters.treeType;
if (UtilValidate.isEmpty(parameters.treeType)) {
    treeType = "DEPARTMENT";
}

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
context.userLoginId = userLoginId;

userLoginList = delegator.findList("MercuryHeader", null, null, null, null, false);
//userLoginList = delegator.findList("MercuryHeader", null, UtilMisc.toSet("createdBy"), UtilMisc.toList("createdBy"), new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true), false);
context.userLoginList = userLoginList;

isAdmin = false;
userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);
if (UtilValidate.isNotEmpty(userLoginAdmin)) {
    isAdmin = true;
}
context.isAdmin = isAdmin;

isHeadquarter = false;
if (userLoginPartyId.equals("HEADQUARTER")) {
    isHeadquarter = true;
    if (UtilValidate.isEmpty(parameters.treeType))  {
        treeType = "DEPARTMENT";
    }
}
context.isHeadquarter = isHeadquarter;
context.treeType = treeType;

filterInput = parameters.filter;
if (UtilValidate.isEmpty(filterInput)) {
    filterInput = userLoginId;
}
context.filterInput = filterInput;

List<EntityCondition> businessTreeCondList = FastList.newInstance();
businessTreeCondList.add(EntityCondition.makeCondition("parentId", EntityOperator.EQUALS, null));
businessTreeCondList.add(EntityCondition.makeCondition("type", EntityOperator.EQUALS, treeType));
EntityCondition businessTreeCond = EntityCondition.makeCondition(businessTreeCondList, EntityOperator.AND);


businessTreeList = delegator.findList("BusinessTree", businessTreeCond, null, ["businessTreeId"], null, false);
businessList = [];
allChildListSize = 0;
for (businessTreeParent in businessTreeList) {
    data = [:];
    parentId = businessTreeParent.businessTreeId;
    businessTreeChild = delegator.findByAnd("BusinessTree", ["parentId" : parentId, "type" : treeType], null, false);
    data.parent = businessTreeParent;

    childList=[];
    for (businessChild in businessTreeChild) {
        childData = [:];
        childData.title = businessChild.title;
        childData.childId = businessChild.businessTreeId;
        childList.add(childData);
    }

    data.childList = childList;
    childSize = 0;
    if (UtilValidate.isNotEmpty(businessTreeChild)) {
        childSize = businessTreeChild.size();
        allChildListSize += childSize;
    }
    data.childSize = childSize;
    businessList.add(data);
}
context.businessList = businessList;
context.allChildListSize = allChildListSize;

//*****************end************************


userLogin = parameters.userLogin;
userLoginId = userLogin.userLoginId;

now = Calendar.getInstance();
Calendar now3 = Calendar.getInstance();
Calendar now4 = Calendar.getInstance();
Calendar now5 = Calendar.getInstance();
Calendar now6 = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
context.dayOfWeek = nowTimestamp;
context.dayOfWeek1 = thisMonday;

list = [];
EntityCondition condition = null;
List<EntityCondition> conditionList = FastList.newInstance();

conditionList.add(EntityCondition.makeCondition("owner", EntityOperator.EQUALS, userLoginId));
conditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
conditionList.add(EntityCondition.makeCondition(dayOfWeekList[dayOfWeek], EntityOperator.EQUALS, "1"));

condition = EntityCondition.makeCondition(conditionList, EntityOperator.AND);
// dailyTaskList = delegator.findList("DailyTaskList", condition, null, ["priority DESC", "taskName"], null, false);
// dailyTaskList = delegator.findList("DailyTaskList", null, null, ["priority DESC", "taskName"], null, false);
dailyTaskList = delegator.findList("DailyTaskList", null, null, ["priority DESC", "owner"], null, false);
context.list = dailyTaskList;
for (dailyTask in dailyTaskList) {
    data = [:];
    // data.owner = userLoginId;
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
    list.add(data);
}


// 左下角 DailyTaskList 当天显示列表
list4 = [];
EntityCondition condition4 = null;
List<EntityCondition> conditionList4 = FastList.newInstance();

// conditionList4.add(EntityCondition.makeCondition("owner", EntityOperator.EQUALS, userLoginId));
conditionList4.add(EntityCondition.makeCondition("owner", EntityOperator.EQUALS, "carl.xia"));
// conditionList4.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
conditionList4.add(EntityCondition.makeCondition(dayOfWeekList[dayOfWeek], EntityOperator.EQUALS, "1"));
// conditionList4.add(EntityCondition.makeCondition("taskName", EntityOperator.NOT_IN, "dailyTaskRecord6.taskName"));

condition4 = EntityCondition.makeCondition(conditionList4, EntityOperator.AND);
// dailyTaskList = delegator.findList("DailyTaskList", condition, null, ["priority DESC", "taskName"], null, false);
// dailyTaskList = delegator.findList("DailyTaskList", null, null, ["priority DESC", "taskName"], null, false);
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



    // if(UtilValidate.isEmpty(dailyTaskRecord6)) {
    //     list4.add(data);
    // }
        list4.add(data);
    
}
context.list4 = list4;



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




// 本周 DailyTaskRecord 表记录数据
list5 = [];
EntityCondition condition5 = null;
List<EntityCondition> conditionList5 = FastList.newInstance();
// 时间区间
List criteria = new LinkedList();
 criteria.add(thisMonday);
 criteria.add(nowTimestamp);

 condition5 = EntityCondition.makeCondition("completedDate", EntityOperator.BETWEEN, criteria);

dailyTaskRecord5 = delegator.findList("DailyTaskRecord", condition5, null, ["completedDate DESC"], null, false);
// dailyTaskRecord5 = delegator.findList("DailyTaskRecord", null, null, null, null, false);
context.dailyTaskRecord5 = dailyTaskRecord5;
for (dailyTaskRecord in dailyTaskRecord5) {
    data = [:];
    data.owner = userLoginId;
    data.owner = dailyTaskRecord.owner;
    data.taskName = dailyTaskRecord.taskName;
    data.completedDate = dailyTaskRecord.completedDate;
    // completedDate = dailyTaskRecord.completedDate;
    // dayOfWeek = data.completedDate.get(Calendar.DAY_OF_WEEK);
    // dayOfWeekList = ["EMPTY", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"];
    // data.completedDate1 = dayOfWeek;
    list5.add(data);
}
context.dailyTaskRecord5 = dailyTaskRecord5;
context.criteria = criteria;


// 当天 DailyTaskRecord 表记录数据
list6 = [];
EntityCondition condition6 = null;
List<EntityCondition> conditionList6 = FastList.newInstance();




// 时间区间
List criteria1 = new LinkedList();
 criteria1.add(thisToday);
 criteria1.add(nowTimestamp1);


 condition6 = EntityCondition.makeCondition("completedDate", EntityOperator.BETWEEN, criteria1);

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




