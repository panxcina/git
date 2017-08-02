
<#-- product master -->
<#-- <#if alt_row> class="alternate-row"</#if> -->

<style type="text/css">
#table{
font-size:12px;
text-align:center;
border: 1px solid #696969;
border-collapse:separate;
padding:0px 0px 0px 0px;
width:1200px;
height:auto;
}
#table tr  td {
width:120px;
height:20px;
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

<form action="<@ofbizUrl>platformSales</@ofbizUrl>" method="post" name="form1" onsubmit="return checkscript()">
      <table  border="0" cellspacing="0" cellpadding="0">
         <tr>
            <td width='25%' align='right' class='label'>${uiLabelMap.CommonDateFilter}</td>
            <td align='left'>
                 <table class="basic-table" cellspacing='0'>
       		    <tr>
                       <td nowrap="nowrap">
                       <@htmlTemplate.renderDateTimeField name="minDate" event="" action="" value="${requestParameters.minDate?default(Static['org.ofbiz.base.util.UtilDateTime'].getXMonthFromToday(nowTimestamp,3))}" className="" alert="" title="Format: yyyy-MM-dd" size="25" maxlength="30" id="minDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/><span class='label'>${uiLabelMap.CommonFrom}</span>
			</td>
			
                    </tr>
                    <tr>                
			<td nowrap="nowrap">
                        <@htmlTemplate.renderDateTimeField name="maxDate" event="" action="" value="${requestParameters.maxDate?default(Static['org.ofbiz.base.util.UtilDateTime'].getXMonthFromToday(nowTimestamp,0))}" className="" alert="" title="Format: yyyy-MM-dd" size="25" maxlength="30" id="maxDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/><span class='label'>${uiLabelMap.CommonThru}</span>
			</td>
                    </tr>
                  </table>
             </td>
	  </tr>
	  <tr>
	 <tr>
		<td class="label">Owner Group</td>
		<td>
		<select name="ownerGroup">
		<option value=""></option>
		<#assign ownerGroupList = delegator.findByAnd("OwnerGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("statusId" , "ACTIVE"), null, true)>
		<#list ownerGroupList?sort_by("ownerGroupName") as ownerGrp>
		<option value="${ownerGrp.ownerGroupName}" <#if parameters.ownerGroup?has_content && parameters.ownerGroup = ownerGrp.ownerGroupName>SELECTED</#if>>${ownerGrp.ownerGroupName}</option>
		</#list>
		</select>
		</td>
		</td>
	  </tr>
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
<td align="center">平台</td>
<td align="center">总销售额</td>
<td align="center">总退款</td>
<td align="center">总国内运费</td>
<td align="center">总包材费</td>
<td align="center">总利润</td>
<td align="center">总利润率</td>
<td align="center">日均销售额</td>
<td align="center">每日利润</td>
<td align="center">每日利润率</td>
</tr>
		
<tr>
<#list p as slist>
<td align="center"><#if slist.platform?has_content>${slist.platform}</#if></td>
<td align="center"><#if slist.sumSales?has_content>${slist.sumSales?string["0"]}</#if></td>
<td align="center"><#if slist.sumRefund?has_content>${slist.sumRefund?string["0"]}</#if></td>
<td align="center"><#if slist.sumDomesticFee?has_content>${slist.sumDomesticFee?string["0"]}</#if></td>
<td align="center"><#if slist.sumMaterialFee?has_content>${slist.sumMaterialFee?string["0"]}</#if></td>
<td align="center"><#if slist.sumItemProfit?has_content>${slist.sumItemProfit?string["0"]}</#if></td>
<td align="center"><#if slist.profitRatePercent?has_content>${slist.profitRatePercent?string["0%"]}</#if></td>
<td align="center"><#if slist.dailySumSales?has_content>${slist.dailySumSales?string["0"]}</#if></td>
<td align="center"><#if slist.dailyItemProfit?has_content>${slist.dailyItemProfit?string["0"]}</#if></td>
<td align="center"><#if slist.dailyprofitRatePercent?has_content>${slist.dailyprofitRatePercent?string["0%"]}</#if></td>
</tr>
</#list>
<tr class="header-row" style="width:200px;white-space: ">
<#list a as Total>
<td align="center">Total</td>
<td align="center">${Total.allSumSales?string["0"]}</td>
<td align="center">${Total.allSumRefund?string["0"]}</td>
<td align="center">${Total.allSumDomesticFee?string["0"]}</td>
<td align="center">${Total.allSumMaterialFee?string["0"]}</td>
<td align="center">${Total.allSumItemProfit?string["0"]}</td>
<td align="center">${Total.allprofitRatePercent?string["0%"]}</td>
<td align="center">${Total.alldailySumSales?string["0"]}</td>
<td align="center">${Total.alldailyItemProfit?string["0"]}</td>
<td align="center">${Total.alldailyprofitRatePercent?string["0%"]}</td>
</tr>
</#list>
</table>
</div>
</div>
