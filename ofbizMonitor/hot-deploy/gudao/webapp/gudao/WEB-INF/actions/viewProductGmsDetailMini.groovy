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


ebayUs = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "EBAY_US_LISTING_COUNT"), false);
ebayUk = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "EBAY_UK_LISTING_COUNT"), false);
ebayAu = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "EBAY_AU_LISTING_COUNT"), false);
ebayDe = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "EBAY_DE_LISTING_COUNT"), false);
ebayFr = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "EBAY_FR_LISTING_COUNT"), false);
smtUs = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "SMT_US_LISTING_COUNT"), false);
wishUs = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "WISH_US_LISTING_COUNT"), false);
amaUs = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "AMA_US_LISTING_COUNT"), false);
amaUk = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "AMA_UK_LISTING_COUNT"), false);
amaCa = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "AMA_CA_LISTING_COUNT"), false);
amaDe = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "AMA_DE_LISTING_COUNT"), false);
amaFollow = delegator.findOne("ProductGms", UtilMisc.toMap("productId", productId, "factor", "AMA_US_FOLLOW_COUNT"), false);

ebayUsDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "US"), null, false);
ebayUkDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "UK"), null, false);
ebayAuDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "AU"), null, false);
ebayDeDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "DE"), null, false);
ebayFrDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "FR"), null, false);
smtUsDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "SMT", "site", "US"), null, false);
wishUsDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "WISH", "site", "US"), null, false);
amaUsDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "US"), null, false);
amaUkDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "UK"), null, false);
amaCaDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "CA"), null, false);
amaDeDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "DE"), null, false);
amaFollowDetail = delegator.findByAnd("ProductGmsDetail", UtilMisc.toMap("productId", productId, "type", "FOLLOW_COUNT", "platform", "AMAZON", "site", "US"), null, false);

ebayUsHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "US"), null, false);
ebayUkHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "UK"), null, false);
ebayAuHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "AU"), null, false);
ebayDeHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "DE"), null, false);
ebayFrHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "EBAY", "site", "FR"), null, false);
smtUsHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "SMT", "site", "US"), null, false);
wishUsHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "WISH", "site", "US"), null, false);
amaUsHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "US"), null, false);
amaUkHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "UK"), null, false);
amaCaHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "CA"), null, false);
amaDeHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "LISTING_COUNT", "platform", "AMAZON", "site", "DE"), null, false);
amaFollowHistory = delegator.findByAnd("ProductGmsDetailHistory", UtilMisc.toMap("productId", productId, "type", "FOLLOW_COUNT", "platform", "AMAZON", "site", "US"), null, false);

context.ebayUs = ebayUs;
context.ebayUk = ebayUk;
context.ebayAu = ebayAu;
context.ebayDe = ebayDe;
context.ebayFr = ebayFr;
context.smtUs = smtUs;
context.wishUs = wishUs;
context.amaUs = amaUs;
context.amaUk = amaUk;
context.amaCa = amaCa;
context.amaDe = amaDe;
context.amaFollow = amaFollow;

context.ebayUsDetail = ebayUsDetail;
context.ebayUkDetail = ebayUkDetail;
context.ebayAuDetail = ebayAuDetail;
context.ebayDeDetail = ebayDeDetail;
context.ebayFrDetail = ebayFrDetail;
context.smtUsDetail = smtUsDetail;
context.wishUsDetail = wishUsDetail;
context.amaUsDetail = amaUsDetail;
context.amaUkDetail = amaUkDetail;
context.amaCaDetail = amaCaDetail;
context.amaDeDetail = amaDeDetail;
context.amaFollowDetail = amaFollowDetail;

context.ebayUsHistory = ebayUsHistory;
context.ebayUkHistory = ebayUkHistory;
context.ebayAuHistory = ebayAuHistory;
context.ebayDeHistory = ebayDeHistory;
context.ebayFrHistory = ebayFrHistory;
context.smtUsHistory = smtUsHistory;
context.wishUsHistory = wishUsHistory;
context.amaUsHistory = amaUsHistory;
context.amaUkHistory = amaUkHistory;
context.amaCaHistory = amaCaHistory;
context.amaDeHistory = amaDeHistory;
context.amaFollowHistory = amaFollowHistory;