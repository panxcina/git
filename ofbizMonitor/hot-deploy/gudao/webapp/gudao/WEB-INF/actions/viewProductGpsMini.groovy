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

productId = parameters.productId
context.productId=productId

/*rivalDailySold = delegator.findOne("ProductGps", UtilMisc.toMap("productId", productId, "factor", "RIVAL_DAILY_SOLD"), false);
rivalDailySales = delegator.findOne("ProductGps", UtilMisc.toMap("productId", productId, "factor", "RIVAL_DAILY_SALES"), false);
samePriceProfitPct = delegator.findOne("ProductGps", UtilMisc.toMap("productId", productId, "factor", "SAME_PRICE_PROFIT_PCT"), false);
supplierRank = delegator.findOne("ProductGps", UtilMisc.toMap("productId", productId, "factor", "SUPPLIER_RANK"), false);
sbuParentCategory = delegator.findOne("ProductGps", UtilMisc.toMap("productId", productId, "factor", "SBU_PARENT_CATEGORY"), false);
sbuChildrenCategory = delegator.findOne("ProductGps", UtilMisc.toMap("productId", productId, "factor", "SBU_CHILDREN_CATEGORY"), false);
amazonPicture = delegator.findOne("ProductGps", UtilMisc.toMap("productId", productId, "factor", "AMAZON_PICTURE"), false);
multiPlatformRival = delegator.findOne("ProductGps", UtilMisc.toMap("productId", productId, "factor", "MULTI_PLATFORM_RIVAL"), false);

context.rivalDailySoldValue = Double.parseDouble(rivalDailySold.value);
context.rivalDailySalesValue = Double.parseDouble(rivalDailySales.value);
context.samePriceProfitPctValue = Double.parseDouble(samePriceProfitPct.value);

context.rivalDailySold = rivalDailySold;
context.rivalDailySales = rivalDailySales;
context.samePriceProfitPct = samePriceProfitPct;
context.supplierRank = supplierRank;
context.sbuParentCategory = sbuParentCategory;
context.sbuChildrenCategory = sbuChildrenCategory;
context.amazonPicture = amazonPicture;
context.multiPlatformRival = multiPlatformRival;*/

productEps = delegator.findOne("ProductEps", UtilMisc.toMap("productId", productId), false);
context.productEps = productEps;