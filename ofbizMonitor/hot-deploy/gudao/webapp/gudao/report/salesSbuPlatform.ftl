
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
width:auto;
height:auto;
border: 1px solid #696969;
display:table-cell; 
empty-cells:show;
}
#table0 tr  td {
width:168px;
height:auto;
border: 0px solid #696969;
display:table-cell; 
empty-cells:show;
bgcolor:#828372}
#table1 tr  td {
width:160px;
height:auto;
border: 0px solid #696969;
display:table-cell; 
empty-cells:show;
}
#table2 tr  td {
width:28px;
height:27px;
border: 1px solid #696969;
display:table-cell; 
empty-cells:show;
}

#table3 tr  td {
width:80px;
height:32px;
border: 2px solid #000000;
display:table-cell; 
empty-cells:show;
}
#table4 tr  td {
width:51px;
height:25px;
border: 1px solid #000000;
display:table-cell; 
empty-cells:show;
}

</style>
<div id="productLookupList" class="screenlet">
<div class="screenlet-title-bar">
 
<br class="clear"/>
</div>
<div class="screenlet-body">


<form action="<@ofbizUrl>salesSbuPlatform</@ofbizUrl>" method="post" name="form1" onsubmit="return checkscript()">
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
	     <td></td>
	     <td>
		<input type="submit"  value="search"  class="smallSubmit" />
	     </td>
	  </tr>
	</table>
</form>
<br class="clear"/>

<table class="basic-table hover-bar" cellspacing='0' id ="table">
<tr class="header-row" >
<td align="center">事业部</td>
<td>
	<table id="table0">
	<tr >
	<td align="center" >AMAZON</td>
	<td align="center" >CDISCOUNT</td>
	<td align="center">EBAY</td>
	<td align="center">LAZADA</td>
	<td align="center">OTHER</td>
	<td align="center">SMT</td>
	<td align="center">TOPHATTER</td>
	<td align="center">WISH</td>
	</tr>

	<tr >
	<td align="center">
		<table><tr><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
	</td>
	<td align="center">
		<table><tr><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
	</td>
	<td align="center">
		<table><tr><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
	</td>
	<td align="center">
		<table><tr><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
	</td>
	<td align="center">
		<table><tr><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
	</td>
	<td align="center">
		<table><tr><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
	</td>
	<td align="center">
		<table><tr><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
	</td>
	<td align="center">
		<table><tr><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
	</td>
	</tr>
	</table>
</td>
</tr>


<tr>
	<td>		
		<table id ="table3">			
			<tr><td>AMY</td></tr>
			<tr><td>BECKY</td></tr>
			<tr><td>CHENWEI</td></tr>
			<tr><td>CINDY</td></tr>
			<tr><td>DADA</td></tr>
			<tr><td>GARY</td></tr>
			<tr><td>LEMON</td></tr>
			<tr><td>LUCY</td></tr>
			<tr><td>MATTY</td></tr>
			<tr><td>NICK</td></tr>
			<tr><td>NIKI</td></tr>
			<tr><td>SELINA</td></tr>
			<tr><td>SISSI</td></tr>	
			<tr><td>YANGYANG</td></tr>
			
		</table>
	</td>


<td>
	<table>		 
	  <tr>
	    <#list s as plf>
	    <td align="center">
		<table id="table1" >
			<#list plf.sbuList as sbu>	
			<tr >
				<td align="center" >
					<table  id="table2" class="basic-table hover-bar">
					<#list sbu.plist as data>
					<tr>						
					<td align="center"><#if data.dailySumSales?has_content>${data.dailySumSales?string["0"]}</#if></td>
					<td align="center"><#if data.dailyItemProfit?has_content>${data.dailyItemProfit?string["0"]}</#if></td>
					<td align="center"><#if data.dailyprofitRatePercent?has_content>${data.dailyprofitRatePercent?string["0%"]}</#if></td>			
					</tr>
					</#list>
					</table>
				</td>
			</tr>
			</#list>				
		</table>
	    </td>
	    </#list>
	  </tr>
	  
	</table>

</td>





</tr>


<tr>
	<td align="center" style="color:black font-size: 14pt">Total</td>
	<td>
		
		<table id="table4" >
			<tr>
			<#list a as total>					
				<td align="center"><#if total.totalSales?has_content>${total.totalSales?string["0"]}</#if></td>
				<td align="center"><#if total.totalProfit?has_content>${total.totalProfit?string["0"]}</#if></td>
				<td align="center"><#if total.totalPercent?has_content>${total.totalPercent?string["0%"]}</#if></td>
			</#list> 			
			</tr>
					
		</table>
				
	</td>
</tr>
</table>

</div>
</div>
