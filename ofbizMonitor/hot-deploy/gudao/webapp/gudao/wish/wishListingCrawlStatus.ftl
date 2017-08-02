
<style type="text/css">
#table{
font-size:12px;
text-align:center;
border: 1px solid #696969;
border-collapse:separate;
padding:0px 0px 0px 0px;
width:600px;
height:auto;
}
#table tr  td {
width:150px;
height:18px;
border: 1px solid #696969;
display:table-cell; 
empty-cells:show;
}
</style>
<script language="JavaScript">
function autoRefresh(){
window.location.reload();
}
setTimeout('autoRefresh()',20000); 
</script> 


<div id="productLookupList" class="screenlet">
    <div class="screenlet-title-bar">
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
	

<table class="basic-table hover-bar" cellspacing='0' id="table">
<tr class="header-row" >
<td align="center">账号</td>
<td align="center">最后获取时间</td>
<td align="center">总数</td>
<td align="center">状态</td>
<td align="center">重新获取</td>
</tr>

<tr>

<#list wish?sort_by("account") as store>
<form action="<@ofbizUrl>triggerWishCrawlListing</@ofbizUrl>" method ="post" onclick="autoRefresh()">
<td align="center"><#if store.account?has_content>${store.account}</#if></td>
<td align="center"><#if store.date?has_content>${store.date}</#if></td>
<td align="center"><#if store.totalCount?has_content>${store.totalCount}</#if></td>
<td align="center"><#if store.status?has_content>${store.status}</#if></td>
<td align="center"><input type="submit" value="crawl" class="smallSubmit"/></td>
<input type="hidden" name="productStoreId" value="${store.account}"/>
<input type="hidden" name="status" value="CRAWL_PENDING"/>
<input type="hidden" name="platform" value="WISH"/>
</form>
</tr>
</#list>

</table>
</div>
</div>

