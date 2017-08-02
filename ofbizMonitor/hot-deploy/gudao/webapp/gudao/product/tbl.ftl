<#-- product master -->
<#-- <#if alt_row> class="alternate-row"</#if> -->

<script language="JavaScript" type="text/javascript">
<!--//
document.findProduct.productId.focus();
//-->
</script>

<div id="productLookupList" class="screenlet">
<div class="screenlet-title-bar">
<ul>
<li class="h3">SKU Count: <b>${productCount}</b></li>
<li><a class="text" href="/gudao/control/tbl-skuList?<#if parameters.viewSize?has_content>viewSize=${parameters.viewSize}</#if><#if parameters.productId?has_content>&productId=${parameters.productId}</#if><#if parameters.productName?has_content>&productName=${parameters.productName}</#if><#if parameters.ownerGroupInput?has_content>&ownerGroupInput=${parameters.ownerGroupInput}</#if><#if parameters.lister?has_content>&lister=${parameters.lister}</#if><#if parameters.listerPlatform?has_content>&listerPlatform=${parameters.listerPlatform}</#if>" onclick="window.open(this.href,'','height=300,width=500,scrollbars=1');return false;">Export</a></li>
</ul>
<br class="clear"/>
</div>
<div class="screenlet-body">
<table class="basic-table hover-bar" cellspacing='10'>
<tr class="header-row" style="white-space">
<td align="center">图</td>
<td align="center">SKU</td>

<td align="center">分组</td>
<td align="center">GPS</td>
<td align="center">开发员</td>
<td align="center">状态</td>
<td align="center">侵权风险</td>
<td align="center">供应商</td>
<td align="center">产品大类</td>
<td align="center">CP</td>
<td align="center">CP%</td>
<td align="center">开发备注</td>
<td align="center">eBay链接</td>
<td align="center">SMT链接</td>
<td align="center">REF链接</td>
<td align="center">Amazon链接</td>
<td align="center">品名</td>
<td align="center">成本</td>
<td align="center">重量</td>
<td align="center">平台</td>
<td align="center">上架员</td>

</tr>
<#assign alt_row = false>
<#list productSearchList as productList>
<tr valign="middle" style="white-space">

<td align="center"><#if productList.productId?has_content><a class="buttontext" href="/gudao/control/viewProductImage?productId=${productList.productId}" onclick="window.open(this.href,'','height=768,width=1350,scrollbars=1');return false;">图</a></#if></td>
<td align="center"><#if productList.productId?has_content><span>${productList.productId}</span>
</#if>
</td>


<td align="center"><#if productList.productGroup?has_content>${productList.productGroup}</#if></td>
<td align="center">
<#if productList.finalGps?has_content>
<a class="buttontext" href="/gudao/control/viewProductGpsMini?productId=${productList.productId}" onclick="window.open(this.href,'','height=300,width=500,scrollbars=1');return false;">${productList.finalGps}</a>
</#if>
</td>
<td align="center"><#if productList.ownerGroup?has_content>${productList.ownerGroup}</#if></td>
<td align="center"><#if productList.statusId?has_content>${productList.statusId}</#if></td>
<td align="center"><#if productList.risk?has_content>${productList.risk}</#if></td>
<td align="center">

<#if productList.supplierLink?has_content>
<#if productList.supplier?has_content><a href="${productList.supplierLink}" title="${productList.supplier}" target="_blank" class="buttontext">${productList.supplier}</a></#if>
<#else>
<#if productList.supplier?has_content>${productList.supplier}</#if>
</#if>

</td>

<td align="center"><#if productList.categoryIdParent?has_content>${productList.categoryIdParent}</#if></td>
<td align="center"><#if productList.priceUsd?has_content>${productList.priceUsd?string["0.##"]}</#if></td>
<td align="center"><#if productList.cpPercentage?has_content>${productList.cpPercentage?string["0%"]}</#if></td>

<td align="center"><#if productList.notes?has_content>${productList.notes}</#if></td>

<td align="center">
<#if productList.rivalItemId?has_content>
<a href="http://www.ebay.com/itm/ws/eBayISAPI.dll?ViewItem&item=${productList.rivalItemId}" title="${productList.rivalItemId}" target="_blank" class="buttontext">Link</a>
</#if>
</td>
<td align="center"><#if productList.smtLink?has_content>${productList.smtLink}</#if></td>
<td align="center"><#if productList.referenceLink?has_content>${productList.referenceLink}</#if></td>
<td align="center"><#if productList.amazonLink?has_content>${productList.amazonLink}</#if></td>
<td align="center" style="word-break : break-word;"><#if productList.chineseName?has_content>
<#if productList.declaredNameCn?has_content>
<span title="${productList.declaredNameCn}<#if productList.declaredNameEn?has_content> - ${productList.declaredNameEn}</#if>">${productList.chineseName}</span>
<#else>${productList.chineseName}
</#if>


</#if></td>
<td align="center"><#if productList.price?has_content>${productList.price?string["0.##"]}</#if></td>
<td align="center"><#if productList.weight?has_content>${productList.weight?string["0"]}</#if></td>
<td align="center"><#if productList.listerPlatform?has_content>${productList.listerPlatform}</#if></td>
<td align="center"><#if productList.lister?has_content>${productList.lister}</#if></td>
</tr>
<#assign alt_row = !alt_row>
</#list>
</table>

</div>
</div>