
<#-- product master -->
<#-- <#if alt_row> class="alternate-row"</#if> -->

<style type="text/css">
#table{
font-size:12px;
text-align:center;
border: 1px solid #696969;
border-collapse:separate;
padding:0px 0px 0px 0px;
width:auto;
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
<div id="productLookupList" class="screenlet">
<div class="screenlet-title-bar">
 
<br class="clear"/>
</div>
<div class="screenlet-body">

<table class="basic-table hover-bar" cellspacing='0' id="table">
<tr class="header-row" style="width:200px;white-space: ">
<td align="center">平台</td>
<td align="center">${Static['org.ofbiz.base.util.UtilDateTime'].getXDayFromToday(nowTimestamp,0)}</td>
<td align="center">${Static['org.ofbiz.base.util.UtilDateTime'].getXDayFromToday(nowTimestamp,1)}</td>
<td align="center">${Static['org.ofbiz.base.util.UtilDateTime'].getXDayFromToday(nowTimestamp,2)}</td>
<td align="center">${Static['org.ofbiz.base.util.UtilDateTime'].getXDayFromToday(nowTimestamp,3)}</td>
<td align="center">${Static['org.ofbiz.base.util.UtilDateTime'].getXDayFromToday(nowTimestamp,4)}</td>
<td align="center">${Static['org.ofbiz.base.util.UtilDateTime'].getXDayFromToday(nowTimestamp,5)}</td>
<td align="center">${Static['org.ofbiz.base.util.UtilDateTime'].getXDayFromToday(nowTimestamp,6)}</td>
</tr>

<tr>
<#list p as slist>
<td align="center"><#if slist.platform?has_content>${slist.platform}</#if></td>
<td align="center"><#if slist.oneCount?has_content>${slist.oneCount}</#if></td>
<td align="center"><#if slist.twoCount?has_content>${slist.twoCount}</#if></td>
<td align="center"><#if slist.threeCount?has_content>${slist.threeCount}</#if></td>
<td align="center"><#if slist.fourCount?has_content>${slist.fourCount}</#if></td>
<td align="center"><#if slist.fiveCount?has_content>${slist.fiveCount}</#if></td>
<td align="center"><#if slist.sixCount?has_content>${slist.sixCount}</#if></td>
<td align="center"><#if slist.sevenCount?has_content>${slist.sevenCount}</#if></td>
</tr>
</#list>

<tr class="header-row" style="width:200px;white-space: ">
<#list a as Total>
<td align="center">Total</td>
<td align="center">${Total.tOneCount}</td>
<td align="center">${Total.tTwoCount}</td>
<td align="center">${Total.tThreeCount}</td>
<td align="center">${Total.tFourCount}</td>
<td align="center">${Total.tFiveCount}</td>
<td align="center">${Total.tSixCount}</td>
<td align="center">${Total.tSevenCount}</td>
</tr>
</#list>

</table>
</div>
</div>

