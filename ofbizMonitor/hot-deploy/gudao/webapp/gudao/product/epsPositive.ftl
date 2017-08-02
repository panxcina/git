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
<li class="h3"> <b><#if productCount?has_content>${productCount}</#if></b></li>
</ul>
<br class="clear"/>
</div>
<div class="screenlet-body">
<table class="basic-table hover-bar" cellspacing='10'>
<tr class="header-row" style="white-space: nowrap;">
<td align="center">SKU</td>
<td align="center">中文名</td>
<td align="center">SBU</td>
<td align="center"> EPS</td>
<td align="center">Ebay链接</td>
<td align="center">分组</td>
<td align="center">状态</td>



</tr>
<#list p as eps >
<tr>
<td align="center"><#if eps.productId?has_content>${eps.productId}</#if></td>
<td align="center"><#if eps.chineseName?has_content>${eps.chineseName}</#if></td>
<td align="center"><#if eps.ownerGroup?has_content>${eps.ownerGroup}</#if></td>
<td align="center"><#if eps.finalEps?has_content>${eps.finalEps}</#if></td>
<td align="center"><#if eps.rivalItemId?has_content>${eps.rivalItemId}</#if></td>
<td align="center"><#if eps.productGroup?has_content>${eps.productGroup}</#if></td>
<td align="center"><#if eps.statusId?has_content>${eps.statusId}</#if></td>
 
</tr>
</#list>

</table>



</div>
</div>
