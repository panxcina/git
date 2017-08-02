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
import com.gudao.product.productService;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");

userLoginAdmin = delegator.findByAnd("UserLoginSecurityGroup", UtilMisc.toMap("userLoginId", userLoginId, "groupId", "GUDAO-ADMIN"), null, false);

isAdmin = false;
if (UtilValidate.isNotEmpty(userLoginAdmin)) {
    isAdmin = true;
}
context.isAdmin = isAdmin;

wikiId = parameters.wikiId;
if (UtilValidate.isNotEmpty(wikiId)) {
    context.wikiId = wikiId;
}
parentId = parameters.parentId;
if (UtilValidate.isNotEmpty(parentId)) {
    context.parentId = parentId;
}
category = parameters.category;
if (UtilValidate.isNotEmpty(category)) {
    context.category = category;
}

if (UtilValidate.isNotEmpty(wikiId)) {
    wikiEntry = delegator.findOne("WikiEntry", ["wikiId" : wikiId], false);
    parentId = wikiEntry.parentId;
    context.parentId = parentId;
    category = wikiEntry.category;
    context.category = category;
    context.wikiEntry = wikiEntry;
}
