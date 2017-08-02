
<#-- product master -->
<#-- <#if alt_row> class="alternate-row"</#if> -->

<style type="text/css">
#table{
font-size:12px;
text-align:center;
border: 1px solid #696969;
border-collapse:separate;
padding:0px 0px 0px 0px;
width:1451px;
height:auto;
}
#table tr  td {
width:200px;
height:18px;
border: 1px solid #696969;
display:table-cell; 
empty-cells:show;
}

</style>
<div id="productLookupList" class="screenlet">
    <div class="screenlet-title-bar">
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
<form action="<@ofbizUrl>salesSkuCount</@ofbizUrl>" method="post" name="form1" onsubmit="return checkscript()">
<table  border="0" cellspacing="0" cellpadding="0">
<tr>
<td width='25%' align='right' class='label'>${uiLabelMap.CommonDateFilter}</td>
<td align='left'>
<table class="basic-table" cellspacing='0'>
<tr>
<td nowrap="nowrap">
<@htmlTemplate.renderDateTimeField name="minDate" event="" action="" value="${minDate?if_exists}" className="" alert="" title="Format: yyyy-MM-dd" size="25" maxlength="30" id="minDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/><span class='label'>${uiLabelMap.CommonFrom}</span>
</td>

</tr>
<tr>
<td nowrap="nowrap">
<@htmlTemplate.renderDateTimeField name="maxDate" event="" action="" value="${maxDate?if_exists}" className="" alert="" title="Format: yyyy-MM-dd" size="25" maxlength="30" id="maxDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/><span class='label'>${uiLabelMap.CommonThru}</span>
</td>
</tr>
</table>
</td>
</tr>

<tr>
<tr>
<td  class='label' >SKU</td>
<td ><input type="text"  size="25" maxlength="255" name="productId" value="${productId?if_exists}" /></td>
</tr>
<td ></td>
<td>
<input type="submit"  value="search"  class="smallSubmit" />
</td>
</tr>
</table>

</form>
<br class="clear"/>


<table class="basic-table hover-bar" cellspacing='0' id="table">
<tr class="header-row" >
<td align="center">日期</td>
<td align="center">ALIEXPRESS数量</td>
<td align="center">AMAZON数量</td>
<td align="center">CDIS数量</td>
<td align="center">EBAY数量</td>
<td align="center">LAZADA数量</td>
<td align="center">WISH数量</td>
</tr>


<tr>
<#list p as slist>
<td align="center"><#if slist.date?has_content>${slist.date}</#if></td>
<td align="center"><#if slist.aliexCount?has_content>${slist.aliexCount}</#if></td>
<td align="center"><#if slist.amzonCount?has_content>${slist.amzonCount}</#if></td>
<td align="center"><#if slist.cdisCount?has_content>${slist.cdisCount}</#if></td>
<td align="center"><#if slist.ebayCount?has_content>${slist.ebayCount}</#if></td>
<td align="center"><#if slist.lazadaCount?has_content>${slist.lazadaCount}</#if></td>
<td align="center"><#if slist.wishCount?has_content>${slist.wishCount}</#if></td>
</tr>
</#list>
</table>
</div>
</div>

