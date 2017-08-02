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
import javolution.util.FastList;

userLogin = parameters.userLogin;
userLoginId = userLogin.getString("userLoginId");
userLoginPartyId = userLogin.getString("partyId");
site = userLogin.getString("externalAuthId");
context.userLoginId = userLoginId;
filter = parameters.filterInput;
if (UtilValidate.isEmpty(filter)) {
    filter = "ACTION";
}
context.filter = filter;

isAdmin = false;
userLoginSelect = parameters.userLoginSelect;
context.userLoginSelect= userLoginSelect;
if(site.equals("ADMIN")){
	isAdmin=true;
} else{
	userLoginSelect = userLoginId;
	
}

context.isAdmin = isAdmin;
distinctUserLoginIdList = delegator.findList("UserLogin", 
													EntityCondition.makeCondition(UtilMisc.toList(
														EntityCondition.makeCondition("partyId",EntityOperator.EQUALS,userLoginPartyId ),
														EntityCondition.makeCondition("enabled",EntityOperator.EQUALS,"Y" ),
														EntityCondition.makeCondition("externalAuthId",EntityOperator.NOT_EQUAL,"ADMIN" )
		)),null,null,null,false);
context.userLoginList = distinctUserLoginIdList;
motherSkuInput = parameters.motherSku;
context.motherSkuInput = motherSkuInput;

List<EntityCondition> productMonViewCondList = FastList.newInstance();
if (filter.equals("ACTION")) {
    productMonViewCondList.add(EntityCondition.makeCondition("hasAction", EntityOperator.EQUALS, "Y"));
}

if(UtilValidate.isNotEmpty(motherSkuInput)){
    motherSkuInput = motherSkuInput.trim();
    productMonViewCondList.add(EntityCondition.makeCondition("motherSku", EntityOperator.LIKE, "%" + motherSkuInput + "%"));
}

if(UtilValidate.isNotEmpty(userLoginSelect)){
    userLoginSelect = userLoginSelect.trim();
    productMonViewCondList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginSelect));
}

productMonViewCond = null;
if (productMonViewCondList.size() > 0) {
    productMonViewCond = EntityCondition.makeCondition(productMonViewCondList, EntityOperator.AND);
}

productMonViewHeaderList = delegator.findList("ProductMonViewHeader", productMonViewCond, null, null, null, false);
allMotherSkuList=[];
for (productMonViewHeader in productMonViewHeaderList) {    //loop productMonViewHeaderList == START
    allMotherSkuData=[:];
    motherSku = productMonViewHeader.motherSku;
    userLoginIdStr = productMonViewHeader.userLoginId;
    hasActionStr = productMonViewHeader.hasAction;

    allMotherSkuData.motherSku = motherSku;
    siteDataList=[];

    List<EntityCondition> distinctSiteCondList = FastList.newInstance();
    distinctSiteCondList.add(EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, motherSku));
    if(UtilValidate.isNotEmpty(userLoginSelect)){
        distinctSiteCondList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginSelect));
    }

    distinctSiteList = delegator.findList("ProductMonitoringList",
                                            EntityCondition.makeCondition(distinctSiteCondList),
                                            UtilMisc.toSet("site"),
                                            UtilMisc.toList("site"),
                                            new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                            false);
    for (siteList in distinctSiteList) {    //loop distinctSiteList == START
        siteData=[:];
        site = siteList.site;
        siteData.site = site;
        lowestRivalItemId = null;

        productMonViewItemGudaoList = delegator.findByAnd("ProductMonViewItem", UtilMisc.toMap("motherSku", motherSku, "site", site, "type", "GUDAO", "statusId", "ACTIVE", "userLoginId", userLoginSelect), null, false);
        productMonViewItemRivalList = delegator.findByAnd("ProductMonViewItem", UtilMisc.toMap("motherSku", motherSku, "site", site, "type", "RIVAL", "statusId", "ACTIVE", "userLoginId", userLoginSelect), null, false);

        rivalItemIdList=[];
        gudaoItemIdList=[];

        for (productMonViewItemRival in productMonViewItemRivalList) {
            rivalData=[:];
            rivalData.rivalItemId = productMonViewItemRival.itemId;
            rivalData.rivalPrice = productMonViewItemRival.startPrice;
            rivalData.rivalOneDayQty = productMonViewItemRival.oneDaySold;
            rivalData.rivalSevenDaysQty = productMonViewItemRival.sevenDaySold;
            rivalItemIdList.add(rivalData);
        }

        for (productMonViewItemGudao in productMonViewItemGudaoList) {
            lowestRivalItemId = productMonViewItemGudao.lowestItemId;
            gudaoData = [:];
            gudaoData.ourItemId = productMonViewItemGudao.itemId;
            gudaoData.ourPrice = productMonViewItemGudao.startPrice;
            gudaoData.ourOneDayQty = productMonViewItemGudao.oneDaySold;
            gudaoData.ourSevenDaysQty = productMonViewItemGudao.sevenDaySold;
            gudaoData.miniMinPrice = productMonViewItemGudao.lowestRivalPrice;
            if (productMonViewItemGudao.hasAction.equals("Y")) {
                gudaoData.isHigher = true;
            } else {
                gudaoData.isHigher = false;
            }
            gudaoItemIdList.add(gudaoData);
        }

        siteData.ourItemIdList = gudaoItemIdList;
        siteData.rivalItemIdList = rivalItemIdList;
        siteData.miniRivalItemId = lowestRivalItemId;
        if (productMonViewItemGudaoList.size() > 0 || productMonViewItemRivalList.size() > 0) {
            siteDataList.add(siteData);
        }


    }   //loop distinctSiteList == END

    allMotherSkuData.siteDataList = siteDataList;
    if (filter.equals("ALL")) {
        allMotherSkuList.add(allMotherSkuData);
    } else if (hasActionStr.equals("Y")) {
        allMotherSkuList.add(allMotherSkuData);
    }
}   //loop productMonViewHeaderList == END

context.allMotherSkuList = allMotherSkuList;