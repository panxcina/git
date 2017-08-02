
<#-- product master -->
<#-- <#if alt_row> class="alternate-row"</#if> -->

<style type="text/css">
#table{
font-size:12px;
text-align:center;
border: 1px solid #333333;
border-collapse:collapse;
padding:0px 0px 0px 0px;
line-height: 2.1em;
line-width: 10em;
margin-left:  0.0em;
padding: 0.4em 0.6em 0.4em 0.6em;
width:1000px;
}
#table tr  td {
width:200px;
border: 1px solid #333333;
display:table-cell; 
}

</style>
<div id="productLookupList" class="screenlet">
<div class="screenlet-title-bar">
 
<br class="clear"/>
</div>
<div class="screenlet-body">
<form action="<@ofbizUrl>sbuReport</@ofbizUrl>" method="post" name="actionForm" id="actionForm" onsubmit="return checkscript()">

<table  border="1" cellpadding="0" cellspacing="0" >
<tr>
<td class="label">Year</td>
<td>
<select name="year"style="width:80px">
<option value=""SELECTED></option>
<option value="${currentYear}"SELECTED >${currentYear}</option>
<option value="${currentYear-1}">${currentYear-1}</option>
<option value="${currentYear-2}">${currentYear-2}</option>
<option value="${currentYear-3}">${currentYear-3}</option>
<option value="${currentYear-4}">${currentYear-4}</option>
</td>
</tr>
<tr>
<td class="label">Period</td>
<td>
<select name="period"style="width:80px">
<option value=""></option>
<#assign monthList = delegator.findByAnd("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumTypeId" , "PERIOD"), Static["org.ofbiz.base.util.UtilMisc"].toList("sequenceId"), false)>
${monthList}
<#list monthList?sort_by("sequenceId") as singleMonthList>
<option value=${singleMonthList.enumCode} <#if parameters.period??><#if singleMonthList.enumCode=parameters.period>SELECTED</#if><#else><#if singleMonthList.enumCode = currentMonth >SELECTED</#if></#if>>${singleMonthList.description}</option>
</#list>
</select>
</td>
</tr>
<tr>
<td></td>
<td>
<input type="submit"  value="search"  class="smallSubmit" />
</td>
</tr>
</table>
</form>


<br class="clear"/>

<table class="basic-table hover-bar" cellspacing='0' id="table">
<tr class="header-row" style="white-space:">
<td align="center">事业部</td>
<td align="center">总母Sku数量</td>
<td align="center">Ｇ１母Sku数量</td>
<td align="center">Ｇ１占比</td>
<td align="center">总综合款数量</td>
<td align="center">Ｇ１综合款数量</td>
<td align="center">Ｇ１占比</td>
</tr>

<tr >
<#list p as newProductlist>
<td align="center"><#if newProductlist.ownerGroup?has_content>${newProductlist.ownerGroup}</#if></td>
<td align="center"><#if newProductlist.motherSkuCount?has_content>${newProductlist.motherSkuCount}</#if></td>
<td align="center"><#if newProductlist.g1Count?has_content>${newProductlist.g1Count}</#if></td>
<td align="center"><#if newProductlist.percentage?has_content>${newProductlist.percentage?string["0.00%"]}</#if></td>
<td align="center"><#if newProductlist.totalCount?has_content>${newProductlist.totalCount}</#if></td>
<td align="center"><#if newProductlist.g1MotherSkuTotal?has_content>${newProductlist.g1MotherSkuTotal}</#if></td>
<td align="center"><#if newProductlist.g1Percent?has_content>${newProductlist.g1Percent?string["0.00%"]}</#if></td>
</tr>
</#list>
<tr class="header-row" style="width:200px;white-space: ">
<#list a as allTotal>
<td align="center">Total</td>
<td align="center">${allTotal.allMotherSkuCount}</td>
<td align="center">${allTotal.allG1Count}</td>
<td align="center">${allTotal.allPercentage?string["0.00%"]}</td>
<td align="center">${allTotal.allTotalCount}</td>
<td align="center">${allTotal.allG1MotherSkuTotal}</td>
<td align="center">${allTotal.allG1Percent?string["0.00%"]}</td>
</tr>
</#list>
</table>



</div>
</div>
