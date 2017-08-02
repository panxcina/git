<#-- wish listing status -->



<div id="productListingStatus" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
<li class="h3">Wish Listing Status - ${rowCount}</b></li>
  </ul>
  <br class="clear"/>
</div>
<div class="screenlet-body"">

<table class="basic-table hover-bar">
<tr class="header-row" style="white-space: nowrap;">

<td align="center">账号名称</td>
<td align="center">SKU</td>
<td align="center">原始SKU</td>
<td align="center">实际库存</td>
<td align="center">在线库存</td>
<td align="center">平均周销</td>
<td align="center">收藏总数</td>
<td align="center">已售总数</td>
<td align="center">产品价格</td>
<td align="center">运费价格</td>
<td align="center">是否黄钻</td>
<td align="center">是否审核</td>
<td align="center">是否在线</td>
</tr>
<#assign alt_row = false>
<#list resultList?sort_by("sku") as result>
<tr valign="middle" >
<td align="center">${result.productStoreId}</td>
<td align="center">${result.normalizedSku}</td>
<td align="center">${result.sku}</td>
<td align="center">${result.qoh}</td>
<td align="center">${result.inventory}</td>
<td align="center">${result.averageWeeklySold}</td>
<td align="center">${result.numberSaves}</td>
<td align="center">${result.numberSold}</td>
<td align="center">${result.price}</td>
<td align="center">${result.shipping}</td>
<td align="center">${result.isPromoted}</td>
<td align="center">${result.reviewStatus}</td>
<td align="center">${result.enabled}</td>
</tr>
<#assign alt_row = !alt_row>




</#list>
</table>
</div>
</div>