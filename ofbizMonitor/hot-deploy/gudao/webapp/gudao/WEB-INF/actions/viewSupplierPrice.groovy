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
import javolution.util.FastList;

productId = parameters.productId
context.productId=productId

product = delegator.findOne("ProductMaster", ["productId" : productId], false);
mainSupplierId = null;
backupSupplierId = null;

if (UtilValidate.isNotEmpty(product)) {
    mainSupplierList = [];
    mainSupplierId = product.mainSupplier;
    context.mainSupplierId = mainSupplierId;
    mainSupplierPriceList = delegator.findByAnd("SupplierPrice", UtilMisc.toMap("productId", productId, "supplier", mainSupplierId), UtilMisc.toList("type", "minimumOrderValue"), false);
    for (mainSupplierPrice in mainSupplierPriceList) {
        data = [:];
        data.supplierId = mainSupplierPrice.supplier;
        data.identifier = mainSupplierPrice.identifier;
        data.type = mainSupplierPrice.type;
        data.minimumOrderValue = mainSupplierPrice.minimumOrderValue;
        data.unitPrice = mainSupplierPrice.unitPrice;
        mainSupplierList.add(data);
    }
    context.mainSupplierList = mainSupplierList;
}

if (UtilValidate.isNotEmpty(product) && UtilValidate.isNotEmpty(product.backupSupplier)) {
    backupSupplierList = [];
    backupSupplierId = product.backupSupplier;
    context.backupSupplierId = backupSupplierId;
    backupSupplierPriceList = delegator.findByAnd("SupplierPrice", UtilMisc.toMap("productId", productId, "supplier", backupSupplierId), UtilMisc.toList("type", "minimumOrderValue"), false);
    for (backupSupplierPrice in backupSupplierPriceList) {
        data = [:];
        data.supplierId = backupSupplierPrice.supplier;
        data.identifier = backupSupplierPrice.identifier;
        data.type = backupSupplierPrice.type;
        data.minimumOrderValue = backupSupplierPrice.minimumOrderValue;
        data.unitPrice = backupSupplierPrice.unitPrice;
        backupSupplierList.add(data);
    }
    context.backupSupplierList = backupSupplierList;
}

List<EntityCondition> supCondList = FastList.newInstance();
supCondList.add(EntityCondition.makeCondition("supplier", EntityOperator.NOT_EQUAL, mainSupplierId));
supCondList.add(EntityCondition.makeCondition("supplier", EntityOperator.NOT_EQUAL, backupSupplierId));

condList = EntityCondition.makeCondition(supCondList, EntityOperator.AND);

List<EntityCondition> supplierPriceCondList = FastList.newInstance();
supplierPriceCondList.add(EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId));
supplierPriceCondList.add(condList);

EntityCondition cond = EntityCondition.makeCondition(supplierPriceCondList, EntityOperator.AND);


emptySupplierPriceList = delegator.findList("SupplierPrice", cond, null, null, null, false);
emptyList = [];
for (emptySupplierPrice in emptySupplierPriceList) {
    data= [:];
    data.supplierId = emptySupplierPrice.supplier;
    data.identifier = emptySupplierPrice.identifier;
    data.type = emptySupplierPrice.type;
    data.minimumOrderValue = emptySupplierPrice.minimumOrderValue;
    data.unitPrice = emptySupplierPrice.unitPrice;
    emptyList.add(data);
}
context.emptyList = emptyList;