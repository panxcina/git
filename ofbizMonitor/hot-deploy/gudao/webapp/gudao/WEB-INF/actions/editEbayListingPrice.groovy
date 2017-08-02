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

ourItemIdInput = parameters.ourItemId;
rivalItemIdInput = parameters.rivalItemId;
siteVar = parameters.site;
context.ourItemIdInput = ourItemIdInput;
context.rivalItemIdInput = rivalItemIdInput;
Date nowDate = new Date();
today = new java.sql.Date(nowDate.getTime());
//our data
ourItemIdList=[];
data=[:];
boolean isSingle = true;
priceInput = 0;
priceTestProfit =0;
ourListingList = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",ourItemIdInput,"date",today,"hasVariation","N"), null,false);
if(UtilValidate.isNotEmpty(ourListingList)){
	ourTitle = EntityUtil.getFirst(ourListingList).title;
	ourPrice = EntityUtil.getFirst(ourListingList).price;
	ourSku =  EntityUtil.getFirst(ourListingList).sku;
	ourSite = EntityUtil.getFirst(ourListingList).site;
    ourSkuVar = EntityUtil.getFirst(ourListingList).sku;
    if (ourSku.contains("|")) {
        term = ourSku.split("\\|");
        ourSkuVar = term[0];
    }
	findProfitList = delegator.findOne("ProductMasterPrice",UtilMisc.toMap("productId",ourSkuVar,"type","CALCULATED","platform","EBAY", "site",ourSite),false);
	priceTest = findProfitList.price;
	priceTestProfit = findProfitList.profitPercentage;
	if(priceTest != 0){
		priceInput = priceTest * 0.05 / priceTestProfit;
	}
	data.priceInput = priceInput;
	data.ourItemId = ourItemIdInput;
	data.ourPrice = ourPrice;
	data.ourSku = ourSku;
	ourItemIdList.add(data);
}
else{
	isSingle = false;
	ourVariationList = delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",ourItemIdInput,"date",today), null, false);
	data.ourItemId = ourItemIdInput;
    findSite = delegator.findOne("RivalListingMonitor", UtilMisc.toMap("itemId", ourItemIdInput, "date", today), false);
    ourSite = findSite.site;
	ourVarList=[];
	for(ourVar in ourVariationList){
		ourVarListData=[:];
		ourPrice = ourVar.startPrice;
		ourSku = ourVar.sku;
        ourSkuVar = ourVar.sku;
		if (ourSku.contains("|")) {    
         	term = ourSku.split("\\|");
         	ourSkuVar = term[0];
        } 
		findProfitList = delegator.findOne("ProductMasterPrice",UtilMisc.toMap("productId",ourSkuVar,"type","CALCULATED","platform","EBAY", "site",ourSite),false);
		priceTest = findProfitList.price;
		priceTestProfit = findProfitList.profitPercentage;
		if(priceTest != 0){
			priceInput = priceTest * 0.05 / priceTestProfit;
		}
		ourSeqId = ourVar.variationSeqId;
		ourVarListData.ourPrice = ourPrice;
		ourVarListData.ourSku = ourSku;
		ourVarListData.priceInput = priceInput;
		ourVarListData.ourSeqId = ourSeqId;
		ourVariationSeqList = delegator.findByAnd("RivalListingVariationSpec", UtilMisc.toMap("itemId",ourItemIdInput,"date",today,"variationSeqId",ourSeqId), ["variationName"], false);
		ourSpecList=[];
		for(ourSpec in ourVariationSeqList){
			ourSpecData=[:];
			ourName =  ourSpec.variationName;
			ourValue = ourSpec.variationValue;	
			ourSpecData.ourName = ourName;
			ourSpecData.ourValue = ourValue;
			ourSpecList.add(ourSpecData);
		}
		ourVarListData.ourSpecList = ourSpecList;
		ourVarList.add(ourVarListData);
	}
	data.ourVarList = ourVarList;
	ourItemIdList.add(data);
}
context.ourItemIdList= ourItemIdList;
//rival data
rivalItemIdList=[];
rivalData=[:];
boolean isRivalSingle = true;
rivalListingList = delegator.findByAnd("RivalListingMonitor", UtilMisc.toMap("itemId",rivalItemIdInput,"date",today,"hasVariation","N"), null,false);
if(UtilValidate.isNotEmpty(rivalListingList)){
	rivalTitle = EntityUtil.getFirst(rivalListingList).title;
	rivalPrice = EntityUtil.getFirst(rivalListingList).price;
	rivalSku = EntityUtil.getFirst(rivalListingList).sku;
	rivalData.rivalItemId = rivalItemIdInput;
	rivalData.rivalPrice = rivalPrice;
	rivalData.rivalSku = rivalSku;
	rivalItemIdList.add(rivalData);
}
else{
	isRivalSingle = false;
	rivalVariationList= delegator.findByAnd("RivalListingVariation", UtilMisc.toMap("itemId",rivalItemIdInput,"date",today), null, false);
	rivalData.rivalItemId = rivalItemIdInput;
	variationList=[];
	for(rivalVar in rivalVariationList){
		variationData=[:];
		rivalPrice = rivalVar.startPrice;
		rivalSku = rivalVar.sku;
		rivalSeqId = rivalVar.variationSeqId;
		variationData.rivalPrice = rivalPrice;
		variationData.rivalSku = rivalSku;
		variationData.rivalSeqId = rivalSeqId;
		rivalVariationSeqList = delegator.findByAnd("RivalListingVariationSpec", UtilMisc.toMap("itemId",rivalItemIdInput,"date",today,"variationSeqId",rivalSeqId), ["variationName"], false);
		rivalSpecList=[];
		for(rivalSpec in rivalVariationSeqList){
			rivalSpecData=[:];
			rivalName =  rivalSpec.variationName;
			rivalValue = rivalSpec.variationValue;	
			rivalSpecData.rivalName = rivalName;
			rivalSpecData.rivalValue = rivalValue;
			rivalSpecList.add(rivalSpecData);
		}
		variationData.rivalSpecList = rivalSpecList;
		variationList.add(variationData);
	}
	rivalData.variationList = variationList;
	rivalItemIdList.add(rivalData);
}
context.rivalItemIdList = rivalItemIdList;
context.isSingle = isSingle;
context.isRivalSingle = isRivalSingle;







