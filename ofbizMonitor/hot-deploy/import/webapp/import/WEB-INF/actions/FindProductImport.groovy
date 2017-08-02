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
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import java.sql.Date;

def module = "FindProductImport.groovy";

//find product
def performFindInMap = [:];
performFindInMap.entityName = "ProductCategorySupplier";
def inputFields = [:];
inputFields.putAll(parameters);
inputFields.internalName = parameters.internalName ?: " ";
performFindInMap.inputFields = inputFields;
performFindInMap.orderBy = "productId";
def performFindResults = dispatcher.runSync("performFind", performFindInMap);
def productLists = performFindResults.listIt.getCompleteList();
performFindResults.listIt.close();

category = parameters.category ?: "Any";
picture = parameters.picture ?: "Any";
discontinue = parameters.discontinue ?: "Any";

conds = [];

productId = parameters.productId;
internalName = parameters.internalName;
partyId = parameters.partyId;

if (productId) {
    conds.add(EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%" + parameters.productId + "%"));
}
if (internalName) {
    conds.add(EntityCondition.makeCondition("internalName", EntityOperator.LIKE, "%" + parameters.internalName + "%"));
}
if (partyId) {
    conds.add(EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, parameters.partyId));
}

if (category == "N") {
    conds.add(EntityCondition.makeCondition("primaryProductCategoryId", null));
    cateProdList = delegator.findList("ProductCategorySupplier", EntityCondition.makeCondition(conds), null, null, null, false);
    productLists = cateProdList;
}

if (picture == "N") {
//  conds.add(EntityCondition.makeCondition("smallImageUrl", null));
//	conds.add(EntityCondition.makeCondition("mediumImageUrl", null));
//	conds.add(EntityCondition.makeCondition("largeImageUrl", null));
//	conds.add(EntityCondition.makeCondition("detailImageUrl", null));
    conds.add(EntityCondition.makeCondition("originalImageUrl", null));
    picProdList = delegator.findList("ProductCategorySupplier", EntityCondition.makeCondition(conds), null, null, null, false);
    productLists = picProdList;
}

if (discontinue == "N") {
	conds.add(EntityUtil.getFilterByDateExpr("availableFromDate", "availableThruDate"));
    discontinueProdList = delegator.findList("ProductCategorySupplier", EntityCondition.makeCondition(conds), null, null, null, false);
    productLists = discontinueProdList;
}

context.productLists = productLists;
