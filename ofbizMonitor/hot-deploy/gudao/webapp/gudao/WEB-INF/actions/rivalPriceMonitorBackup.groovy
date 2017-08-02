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

productMonViewHeaderList = delegator.findList("ProductMonViewHeader", EntityCondition.makeCondition(productMonViewCond), null, null, null, false);
allMotherSkuList=[];
for (productMonViewHeader in productMonViewHeaderList) {
    allMotherSkuData=[:];
    motherSku = productMonViewHeader.motherSku;
    userLoginIdStr = productMonViewHeader.userLoginId;
    hasActionStr = productMonViewHeader.hasAction;

    allMotherSkuData.motherSku = motherSku;
    siteDataList=[];
    distinctSiteList = delegator.findList("ProductMonitoringList",
                                            EntityCondition.makeCondition(UtilMisc.toList(
                                                                                EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginIdStr),
                                                                                EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, motherSku))),
                                            UtilMisc.toSet("site"),
                                            UtilMisc.toList("site"),
                                            new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),
                                            false);
    for (siteList in distinctSiteList) {
        siteData=[:];
        site = siteList.site;
        siteData.site = site;

        productMonViewItemGudaoList = delegator.findByAnd("ProductMonViewItem", UtilMisc.toMap("motherSku", motherSku, "site", site, "type", "GUDAO"), null, false);
        productMonViewItemRivalList = delegator.findByAnd("ProductMonViewItem", UtilMisc.toMap("motherSku", motherSku, "site", site, "type", "RIVAL"), null, false);

        rivalItemIdList=[];
        gudaoItemIdList=[];

        for (productMonViewItemRival in productMonViewItemRivalList) {

        }

        for (productMonViewItemGudao in productMonViewItemGudaoList) {

        }

    }
}










List<EntityCondition> findSbuList = FastList.newInstance();
List<EntityCondition>  gudaoItemListCond = FastList.newInstance();
List<EntityCondition> rivalItemListCond = FastList.newInstance();
if(UtilValidate.isNotEmpty(motherSkuInput)){
	motherSkuInput = motherSkuInput.trim();
	findSbuList.add(EntityCondition.makeCondition("motherSku", EntityOperator.LIKE, "%" + motherSkuInput + "%"));
}
findSbuList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
if(UtilValidate.isNotEmpty(userLoginSelect)){
	userLoginSelect = userLoginSelect.trim();
	findSbuList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginSelect));
}

findSbuList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"));
EntityCondition listerConds = null;
if (findSbuList.size() > 0)	{
	listerConds = EntityCondition.makeCondition(findSbuList, EntityOperator.AND);
}

distinctMotherSkuList = delegator.findList("ProductMonitoringList", listerConds, UtilMisc.toSet("motherSku"), UtilMisc.toList("motherSku"),new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),false);
int count = 0;
limitMotherSkuList=[];
for(motherSkuList in distinctMotherSkuList){
	count++;
	if(count > 500){ break;}
	limitMotherSkuList.add(motherSkuList);
}
context.limitSkuList = limitMotherSkuList;
Date nowDate = new Date();
today = new java.sql.Date(nowDate.getTime());
yesterday = new java.sql.Date(nowDate.getTime() - 1 * 24 * 3600 * 1000);
context.yesterday = yesterday;
weekAgo = new java.sql.Date(nowDate.getTime() - 7 * 24 * 3600 * 1000);
allMotherSkuList=[];
for(limitSku in limitMotherSkuList){//loop motherSku --start
	allMotherSkuData=[:];
	oneSku = limitSku.motherSku;
    hasAction = false;
	
	allMotherSkuData.motherSku = oneSku;
	List<EntityCondition> conditionList = FastList.newInstance();
	conditionList.add(EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, oneSku));
	conditionList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
	conditionList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"));
	if (UtilValidate.isNotEmpty(userLoginSelect)) {
		conditionList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginSelect));
	}
	if (!site.equals("ADMIN")) {
		conditionList.add(EntityCondition.makeCondition("site", EntityOperator.EQUALS, site));
	}
	

	EntityCondition condition = EntityCondition.makeCondition(conditionList);
	distinctSiteList = delegator.findList("ProductMonitoringList", condition, UtilMisc.toSet("site"), UtilMisc.toList("site"),new EntityFindOptions(true, EntityFindOptions.TYPE_SCROLL_INSENSITIVE, EntityFindOptions.CONCUR_READ_ONLY, true),false);
	siteDataList=[];
	for(siteList in distinctSiteList){//loop site --start
		miniRivalItemId = null;
		siteData=[:];
		oneSite = siteList.site;
		siteData.site = oneSite;
		List<EntityCondition> ebayCondsList = FastList.newInstance();
		ebayCondsList.add(EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, oneSku));
		ebayCondsList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
		ebayCondsList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"));
		ebayCondsList.add(EntityCondition.makeCondition("site", EntityOperator.EQUALS, oneSite));
		ebayCondsList.add(EntityCondition.makeCondition("type", EntityOperator.EQUALS, "GUDAO"));
		if (UtilValidate.isNotEmpty(userLoginSelect)) {
			ebayCondsList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginSelect));
		}
		EntityCondition ebayConds = EntityCondition.makeCondition(ebayCondsList);

		List<EntityCondition> rivalCondsList = FastList.newInstance();
		rivalCondsList.add(EntityCondition.makeCondition("motherSku", EntityOperator.EQUALS, oneSku));
		rivalCondsList.add(EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ACTIVE"));
		rivalCondsList.add(EntityCondition.makeCondition("platform", EntityOperator.EQUALS, "EBAY"));
		rivalCondsList.add(EntityCondition.makeCondition("site", EntityOperator.EQUALS, oneSite));
		rivalCondsList.add(EntityCondition.makeCondition("type", EntityOperator.EQUALS, "RIVAL"));
		if (UtilValidate.isNotEmpty(userLoginSelect)) {
			rivalCondsList.add(EntityCondition.makeCondition("userLoginId", EntityOperator.EQUALS, userLoginSelect));
		}
		EntityCondition rivalConds = EntityCondition.makeCondition(rivalCondsList);
		ebayVarList = delegator.findList("ProductMonitoringList",ebayConds, null, null, null,false);
		rivalVarList = delegator.findList("ProductMonitoringList",rivalConds, null, null, null,false);

			
			// rival data
			rivalItemIdList=[];
			rivalAttributeList=[];
			rivalOneDayQty=0;
			rivalSevenDaysQty=0;
			for(rivalList in rivalVarList){//loop rivalListing -- start
				todayQty =0;
				yesterdayQty=0;
				weekAgoQty=0;
				rivalData=[:];
				rivalAttributeData=[:];
				oneRivalItemId = rivalList.itemId;
				rivalData.rivalItemId = oneRivalItemId;
				rivalListingList1 = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",oneRivalItemId,"date",today,"hasVariation","N"), null,false);
				rivalListingList2 = delegator.findByAnd("RivalListingMonitor",UtilMisc.toMap("itemId",oneRivalItemId,"date",yesterday,"hasVariation","N"),null,false);
				rivalListingList3 = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",oneRivalItemId,"date",weekAgo,"hasVariation","N"),null, false);
				if(UtilValidate.isNotEmpty(rivalListingList1)){
					todayQty = EntityUtil.getFirst(rivalListingList1).quantitySold;
					rivalPrice = EntityUtil.getFirst(rivalListingList1).price;
					rivalLocation = EntityUtil.getFirst(rivalListingList1).location;
					rivalCategoryName = EntityUtil.getFirst(rivalListingList1).secondaryCategoryName;
					rivalSbuTitle = EntityUtil.getFirst(rivalListingList1).subTitle;
					rivalBestOffer = EntityUtil.getFirst(rivalListingList1).bestOffer;
					rivalFeedBackScore = EntityUtil.getFirst(rivalListingList1).feedBackScore;
					rivalFeedBackPercent = EntityUtil.getFirst(rivalListingList1).feedBackPercent;
					rivalData.rivalPrice = rivalPrice;
					rivalData.rivalLocation = rivalLocation;
					rivalData.rivalCategoryName = rivalCategoryName;
					rivalData.rivalSbuTitle = rivalSbuTitle;
					rivalData.rivalBestOffer = rivalBestOffer;
					rivalData.rivalFeedBackScore = rivalFeedBackScore;
					rivalData.rivalFeedBackPercent = rivalFeedBackPercent;
					rivalAttributeData.rivalLocation = rivalLocation;
					rivalAttributeData.rivalCategoryName = rivalCategoryName;
					rivalAttributeData.rivalSbuTitle = rivalSbuTitle;
					rivalAttributeData.rivalBestOffer = rivalBestOffer;
					rivalAttributeData.rivalFeedBackScore = rivalFeedBackScore;
					rivalAttributeData.rivalFeedBackPercent = rivalFeedBackPercent;
					
					
				}	
				rivalAttributeList.add(rivalAttributeData);	
				if(UtilValidate.isNotEmpty(rivalListingList2)){
					yesterdayQty = EntityUtil.getFirst(rivalListingList2).quantitySold;
				}
				if(UtilValidate.isNotEmpty(rivalListingList3)){	
					weekAgoQty = EntityUtil.getFirst(rivalListingList3).quantitySold;
				}

				if(UtilValidate.isEmpty(rivalListingList1)){
					rivalVariationList1 = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",oneRivalItemId,"date",today), null, false);
					//minPrice = EntityUtil.getFirst(rivalVariationList1).startPrice;
					minPrice= 1000000;
					for(variationVar1 in rivalVariationList1){
						todayQty += variationVar1.quantitySold;
						if (variationVar1.startPrice <= minPrice) { minPrice = variationVar1.startPrice; }
					}
					rivalData.rivalPrice = minPrice;
					
				}
				if(UtilValidate.isEmpty(rivalListingList2)){
					rivalVariationList2 = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",oneRivalItemId,"date",yesterday), null, false);
					for(variationVar2 in rivalVariationList2){
						yesterdayQty += variationVar2.quantitySold;
					}
				}
				if(UtilValidate.isEmpty(rivalListingList3)){
					rivalVariationList3 = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",oneRivalItemId,"date",weekAgo), null, false);
					for(variationVar3 in rivalVariationList3){
						weekAgoQty += variationVar3.quantitySold;
					}
				}
				
				rivalOneDayQty = todayQty - yesterdayQty;
				rivalSevenDaysQty = todayQty - weekAgoQty;
				rivalData.rivalOneDayQty = rivalOneDayQty;
		 		rivalData.rivalSevenDaysQty = rivalSevenDaysQty;
				rivalData.rivalLocation = rivalAttributeList.rivalLocation;
				rivalData.rivalCategoryName = rivalAttributeList.rivalCategoryName;
				rivalData.rivalSbuTitle = rivalAttributeList.rivalSbuTitle;
				rivalData.rivalBestOffer = rivalAttributeList.rivalBestOffer;
				rivalData.rivalFeedBackScore = rivalAttributeList.rivalFeedBackScore;
				rivalData.rivalFeedBackPercent = rivalAttributeList.rivalFeedBackPercent;
				rivalItemIdList.add(rivalData);
			}//loop rivalListing -- end
			miniMinPrice = 1000000;
			
			for(miniRival in rivalItemIdList){
				if (miniRival.rivalPrice <= miniMinPrice) { miniMinPrice = miniRival.rivalPrice; miniRivalItemId = miniRival.rivalItemId;}
			}

		//our data
		
		ourItemIdList=[];
		ourPriceList=[];
		for(ebayList in ebayVarList){//loop ourListing -- start
			ourOneDayQty = 0;
			ourSevenDaysQty = 0;
			todayQty =0;
			yesterdayQty=0;
			weekAgoQty=0;
			data=[:];
			isHigher = false;
			oneEbayItemId = ebayList.itemId;
			data.ourItemId = oneEbayItemId;
			context.oneEbayItemId = oneEbayItemId;
			ourListingDate = ebayList.date;
			ourListingList1 = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",oneEbayItemId,"date",today,"hasVariation","N"), null,false);
			ourListingList2 = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",oneEbayItemId,"date",yesterday,"hasVariation","N"), null,false);
			ourListingList3 = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",oneEbayItemId,"date",weekAgo,"hasVariation","N"), null,false);
			if(UtilValidate.isNotEmpty(ourListingList1)){
				todayQty = EntityUtil.getFirst(ourListingList1).quantitySold;
				ourPrice = EntityUtil.getFirst(ourListingList1).price;
				data.ourPrice = ourPrice;
                if (ourPrice >= miniMinPrice) { isHigher = true; hasAction = true;}
				data.isHigher = isHigher;
				data.miniMinPrice = miniMinPrice;
			}		
			if(UtilValidate.isNotEmpty(ourListingList2)){
				yesterdayQty = EntityUtil.getFirst(ourListingList2).quantitySold;
			}
			if(UtilValidate.isNotEmpty(ourListingList3)){	
				weekAgoQty = EntityUtil.getFirst(ourListingList3).quantitySold;
			}
			if(UtilValidate.isEmpty(ourListingList1)){
				ourVariationList1 = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",oneEbayItemId,"date",today), null, false);
				//minPrice = EntityUtil.getFirst(ourVariationList1).startPrice;
				minPrice  =1000000;
				for(ourVar1 in ourVariationList1){
					todayQty += ourVar1.quantitySold;
					if (ourVar1.startPrice <= minPrice) { minPrice = ourVar1.startPrice; }
				}
				data.ourPrice = minPrice;
				if (minPrice>= miniMinPrice) { isHigher = true; }
				data.isHigher = isHigher;
				data.miniMinPrice= miniMinPrice;
			}
			if(UtilValidate.isEmpty(ourListingList2)){
				ourVariationList2 = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",oneEbayItemId,"date",yesterday), null, false);
				for(ourVar2 in ourVariationList2){
					yesterdayQty += ourVar2.quantitySold;
				}
			}
			if(UtilValidate.isEmpty(ourListingList3)){
				ourVariationList3 = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",oneEbayItemId,"date",weekAgo), null, false);
				for(ourVar3 in ourVariationList3){
					weekAgoQty += ourVar3.quantitySold;
				}
			}
			ourOneDayQty = todayQty - yesterdayQty;
			ourSevenDaysQty = todayQty - weekAgoQty;
			data.ourOneDayQty = ourOneDayQty;
	 		data.ourSevenDaysQty = ourSevenDaysQty;
			ourItemIdList.add(data);	
		}//loop ourListing --end
		siteData.ourItemIdList = ourItemIdList;
		siteData.rivalItemIdList = rivalItemIdList;
		siteData.miniRivalItemId = miniRivalItemId;
        siteDataList.add(siteData);

	}//loop site --end
	allMotherSkuData.siteDataList = siteDataList;
    if (filter.equals("ALL")) {
        allMotherSkuList.add(allMotherSkuData);
    } else if (hasAction) {
        allMotherSkuList.add(allMotherSkuData);
    }
}//loop motherSku --end
context.allMotherSkuList = allMotherSkuList;








