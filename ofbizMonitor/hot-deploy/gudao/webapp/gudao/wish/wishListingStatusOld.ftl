<#-- wish listing status -->

<style type="text/css">
A.bigbutton,.bigbuttondisabled {
border-bottom: #999999 solid 0.1em;
border-top: #f7e893 solid 0.1em;
border-left: #f7e893 solid 0.1em;
border-right: #999999 solid 0.1em;
background-image: url(../images/button_whitegray.jpg);
background-repeat:repeat-x;
font-weight: bold;
font-size: 20px;
line-height: 2.9em;
margin-left:  0.0em;
padding: 0.4em 0.6em 0.4em 0.6em;
/*white-space: nowrap;*/
}

</style>





<div id="productListingStatus" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
<li class="h3">Wish Listing Status</b></li>
  </ul>
  <br class="clear"/>
</div>
<div class="screenlet-body" align="center">
<a href="<@ofbizUrl>wishListingDetail</@ofbizUrl>" class="bigbutton">Listing Detail Click here</a>
<#list storeList as store>
<#assign wsom = accountMap.get("${store}")>

<form target="_blank" name="variantNotPromotedCountForm${store}" id="variantNotPromotedCountForm${store}" method="post" action="<@ofbizUrl>wishListingDetail</@ofbizUrl>">
<input type="hidden" name="productStoreId" value="${store}"/>
<input type="hidden" name="isPromoted" value="NO"/>
<input type="hidden" name="reviewStatus" value="APPROVED"/>
<input type="hidden" name="listingStatus" value="ENABLED"/>
</form>
<form target="_blank" name="nonPromotedWithInventoryForm${store}" id="nonPromotedWithInventoryForm${store}" method="post" action="<@ofbizUrl>wishListingDetail</@ofbizUrl>">
<input type="hidden" name="productStoreId" value="${store}"/>
<input type="hidden" name="isPromoted" value="NO"/>
<input type="hidden" name="wishInventory" value="YES"/>
<input type="hidden" name="reviewStatus" value="APPROVED"/>
<input type="hidden" name="listingStatus" value="ENABLED"/>
</form>
<form target="_blank" name="nonPromotedNoInventoryForm${store}" id="nonPromotedNoInventoryForm${store}" method="post" action="<@ofbizUrl>wishListingDetail</@ofbizUrl>">
<input type="hidden" name="productStoreId" value="${store}"/>
<input type="hidden" name="isPromoted" value="NO"/>
<input type="hidden" name="wishInventory" value="NO"/>
<input type="hidden" name="reviewStatus" value="APPROVED"/>
<input type="hidden" name="listingStatus" value="ENABLED"/>
</form>
<form target="_blank" name="nonPromotedStockCountForm${store}" id="nonPromotedStockCountForm${store}" method="post" action="<@ofbizUrl>wishListingDetail</@ofbizUrl>">
<input type="hidden" name="productStoreId" value="${store}"/>
<input type="hidden" name="isPromoted" value="NO"/>
<input type="hidden" name="hasStock" value="YES"/>
<input type="hidden" name="reviewStatus" value="APPROVED"/>
<input type="hidden" name="listingStatus" value="ENABLED"/>
</form>
<form target="_blank" name="nonPromotedNoStockCountForm${store}" id="nonPromotedNoStockCountForm${store}" method="post" action="<@ofbizUrl>wishListingDetail</@ofbizUrl>">
<input type="hidden" name="productStoreId" value="${store}"/>
<input type="hidden" name="isPromoted" value="NO"/>
<input type="hidden" name="hasStock" value="NO"/>
<input type="hidden" name="reviewStatus" value="APPROVED"/>
<input type="hidden" name="listingStatus" value="ENABLED"/>
</form>

<form target="_blank" name="variantPromotedCountForm${store}" id="variantPromotedCountForm${store}" method="post" action="<@ofbizUrl>wishListingDetail</@ofbizUrl>">
<input type="hidden" name="productStoreId" value="${store}"/>
<input type="hidden" name="isPromoted" value="YES"/>
<input type="hidden" name="reviewStatus" value="APPROVED"/>
<input type="hidden" name="listingStatus" value="ENABLED"/>
</form>
<form target="_blank" name="promotedStockCountForm${store}" id="promotedStockCountForm${store}" method="post" action="<@ofbizUrl>wishListingDetail</@ofbizUrl>">
<input type="hidden" name="productStoreId" value="${store}"/>
<input type="hidden" name="sevenDayStock" value="YES"/>
<input type="hidden" name="isPromoted" value="YES"/>
<input type="hidden" name="reviewStatus" value="APPROVED"/>
<input type="hidden" name="listingStatus" value="ENABLED"/>
</form>

<table class="hover-bar" cellspacing="10" style="border-width: 1px;border-style: dotted;border-color: black;border-spacing: 20px 0;">
<tr class="header-row" style="white-space: nowrap;">
<td align="center"><form method="post" name="refreshWishListing" action="<@ofbizUrl>refreshWishListing</@ofbizUrl>">
<input type="hidden" name="productStoreId" value="${store}"></hidden></input>
<input type="submit" value="${store}"/>
</form></td>
<td align="center">母SKU</td>
<td align="center">子SKU</td>
<td align="center">库存充足</td>
<td align="center">黄钻母SKU</td>
<td align="center">黄钻子SKU</td>
<td align="center">黄钻库存充足</td>
</tr>
<#assign alt_row = false>
<#assign i = 0>
<#list wsom?sort_by("date") as dateSingle>
<tr valign="middle" >
<td align="center">${dateSingle.date?string["MMM dd"]}</td>
<td align="center">
${dateSingle.totalListingCount} ( <font color="blue"><b>${dateSingle.listingCount}</b></font> / <font color="yellow">${dateSingle.pendingListingCount}</font> / <font color="red">${dateSingle.rejectedListingCount}</font> )
</td>
<td align="center">
<#if i == 7>
<a href="javascript:document.getElementById('variantNotPromotedCountForm${store}').submit()">
${dateSingle.variantNotPromotedCount}</a>
<#else>
${dateSingle.variantNotPromotedCount}
</#if>
(
<#if i == 7>
<a href="javascript:document.getElementById('nonPromotedWithInventoryForm${store}').submit()">
<font color="blue"><b>${dateSingle.nonPromotedWithInventory}</b></font></a>
<#else>
<font color="blue"><b>${dateSingle.nonPromotedWithInventory}</b></font>
</#if>
 /
<#if i == 7>
<a href="javascript:document.getElementById('nonPromotedNoInventoryForm${store}').submit()">
<font color="red">${dateSingle.nonPromotedNoInventory}</font></a>
<#else>
<font color="red">${dateSingle.nonPromotedNoInventory}</font>
</#if>
)
</td>
<td align="center">
<#if i == 7>
<a href="javascript:document.getElementById('nonPromotedStockCountForm${store}').submit()">
${dateSingle.nonPromotedStockCount}</a>
<#else>
${dateSingle.nonPromotedStockCount}
</#if>
/
<#if i == 7>
<a href="javascript:document.getElementById('nonPromotedNoStockCountForm${store}').submit()">
${dateSingle.nonPromotedNoStockCount}</a>
<#else>
${dateSingle.nonPromotedNoStockCount}
</#if></td>
<td align="center">${dateSingle.promotedListingCount}</td>
<td align="center">
<#if i == 7>
<a href="javascript:document.getElementById('variantPromotedCountForm${store}').submit()">
${dateSingle.variantPromotedCount}</a>
<#else>
${dateSingle.variantPromotedCount}
</#if>
</td>
<td align="center">
<#if i == 7>
<a href="javascript:document.getElementById('promotedStockCountForm${store}').submit()">
${dateSingle.promotedStockCount}</a>
<#else>
${dateSingle.promotedStockCount}
</#if>


</td>
</tr>
<#assign alt_row = !alt_row>
<#assign i = i + 1>
</#list>
</table>
<br />

</#list>
</div>
</div>