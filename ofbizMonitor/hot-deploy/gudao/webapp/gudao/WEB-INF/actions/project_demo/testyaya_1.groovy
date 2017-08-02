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
import java.util.Date;
/*import java.sql.Date;
import java.text.DateFormat;*/
import java.text.SimpleDateFormat;
import javolution.util.FastMap;
import javolution.util.FastList;


GenericValue testyaya = delegator.findOne("testyaya" , UtilMisc.toMap("testyayaId", "test3"), false);
context.testyaya = testyaya;

testyayalist = delegator.findByAnd("testyaya", ["text" : "text3"], null, false);
// 排序 逆序
// testyayalist = delegator.findByAnd("testyaya", ["text" : "text3"], ["testyayaId DESC"], false);
context.testyayalist = testyayalist;

allList = delegator.findList("testyaya", null, null, null, null, false);
context.all = allList;

context.test = parameters;