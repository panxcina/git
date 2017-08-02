
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
white-space:nowrap;
}
#table0 tr  td {
width:auto;
height:auto;
border: 0px solid #696969;
display:table-cell; 
empty-cells:show;
bgcolor:#828372}
#table1 tr  td {
width:135px;
height:auto;
border: 0px solid #696969;
display:table-cell; 
empty-cells:show;
}
#table2 tr  td {
width:30px;
height:auto;
border: 1px solid #696969;
display:table-cell; 
empty-cells:show;
valign:top;
}
#table3 tr  td {
width:60px;
height:24.4px;
border: 1px solid #000000;
display:table-cell; 
empty-cells:show;
}
#table4 tr  td {
width:42px;
height:28.5px;
border: 1px solid #000000;
display:table-cell; 
empty-cells:show;
}
#table5 tr  td {
width:50px;
height:28.5px;
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

<form action="<@ofbizUrl>salesSbuPlatformAccount</@ofbizUrl>" method="post" name="form1" onsubmit="return checkscript()">
      <table  border="0" cellspacing="0" cellpadding="0">
         <tr>
            <td width='25%' align='right' class='label'>${uiLabelMap.CommonDateFilter}</td>
            <td align='left'>
                 <table class="basic-table" cellspacing='0'>
       		    <tr>
                       <td nowrap="nowrap">
                       <@htmlTemplate.renderDateTimeField name="minDate" event="" action="" value="${requestParameters.minDate?default(Static['org.ofbiz.base.util.UtilDateTime'].getMonthStart(nowTimestamp))}" className="" alert="" title="Format: yyyy-MM-dd" size="25" maxlength="30" id="minDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/><span class='label'>${uiLabelMap.CommonFrom}</span>
			</td>
			
                    </tr>
                    <tr>                
			<td nowrap="nowrap">
                        <@htmlTemplate.renderDateTimeField name="maxDate" event="" action="" value="${requestParameters.maxDate?default(Static['org.ofbiz.base.util.UtilDateTime'].nowTimestamp())}" className="" alert="" title="Format: yyyy-MM-dd" size="25" maxlength="30" id="maxDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/><span class='label'>${uiLabelMap.CommonThru}</span>
			</td>
                    </tr>
                  </table>
             </td>
	  </tr>

 <tr>
    <td text-align="left" class="label" >sbu &nbsp;&nbsp;&nbsp;</td>
	
    <td>
	<div style=" overflow-y:scroll; width:120px; height:80px;”>
     <select  id="sbuSelect" multiple="multiple">
	
       <#list ownerGroupAllList as ownerGroup>
	<option>
       <input  name="ownerGroupInput" type="checkbox"   value="${ownerGroup.officeSiteName}" <#if parameters.ownerGroupInput.contains(ownerGroup.officeSiteName)> CHECKED</#if>/>${ownerGroup.officeSiteName}<br/>
	</option>
       </#list>
    </select>
	</div>
    </td>

</tr>

<tr>	     
	     <td align="center" class="label">Detail &nbsp;&nbsp;</td>
	     <td>
		<input type="checkbox" name="detail" <#if detail>checked</#if>/>
	     </td>
</tr>
<tr></tr>

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
<#list platformList as platform>
<td>
	
	<table id="table0">
	<tr >
	    <td align="center"><#if platform.roleTypeId?has_content>${platform.roleTypeId}</#if></td>
	</tr>
	<tr>
	    <td align="center">
	    <table ><tr><td align="center">种类&nbsp;</td><td align="center">部门&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td align="center">销量&nbsp;&nbsp;</td><td align="center">毛利&nbsp;&nbsp;</td><td align="center">毛利率</td></tr></table>
	    </td>
	</tr>
	</table>
	
</td>
</#list>
<td >
	<table id="table0">
	<tr >
	    <td align="center">Total</td>
	</tr>
	<tr >
	    <td align="center">
	    <table><tr><td align="center">种类</td><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
	    </td>
	</tr>
	</table>
</td>
</tr>

<tr>
   <td>
      <#list ownerGroupList as ownerGroup>
	<div style="margin:0px 0px 100px 0px;width:auto;height:180px">
	     <table id="table3">
		    <tr>
		       <td align="center" ><#if ownerGroup.officeSiteName?has_content>${ownerGroup.officeSiteName}</#if></td>
		    </tr>		
	     </table>
</div>
     </#list>
   </td>

<#list s as plf>
<td>
	    <#list plf.sbuList as sbu>
	     <div style="margin:0px 0px 100px 0px;width:auto;height:180px">
			<#list sbu.plist as party>
				
			    <#list party.platformDepartList as data>
				<#if data.hasSalesData>
				<table id="table2">
				 <tr>
				    <td align="center">日均</td>
				    <td align="center"><#if data.departmentid?has_content>${data.departmentid}</#if></td>
				    <td align="center"><#if data.sbuDepartmentSales?has_content>${data.sbuDepartmentSales?string["0"]}</#if></td>
				    <td align="center"><#if data.sbuDepartmentProfit?has_content>${data.sbuDepartmentProfit?string["0"]}</#if></td>
				    <td align="center"><#if data.sbuDepartmentPercent?has_content>${data.sbuDepartmentPercent?string["0%"]}</#if></td>		
				 </tr>
				
				 <tr>
				    <td align="center">上月</td>
				    <td align="center"><#if data.departmentid?has_content>${data.departmentid}</#if></td>
				    <td align="center"><#if data.lastMonthDepartSales?has_content>${data.lastMonthDepartSales?string["0"]}</#if></td>
			   	    <td align="center"><#if data.lastMonthDepartProfit?has_content>${data.lastMonthDepartProfit?string["0"]}</#if></td>
				    <td align="center"><#if data.lastMonthDepartPercent?has_content>${data.lastMonthDepartPercent?string["0%"]}</#if></td>
				 </tr>
				</table>
			 	</#if>
			    </#list>
				
			</#list>
		</div>					
		</#list>

</td>
</#list>

<td>
      <#list rightAllList as right>
		<div style="margin:0px 0px 100px 0px;width:auto;height:180px">
	     <table id="table4">
		   <tr>
		       <td align="center">种类</td>				
		       <td align="center">销量</td>
		       <td align="center">毛利</td>
		       <td align="center">毛利率</td>
		    </tr>
		   <tr>	
			<td align="center">日均</td>			
	       		<td align="center"><#if right.rightTotalSales?has_content>${right.rightTotalSales?string["0"]}</#if></td>
	       		<td align="center"><#if right.rightTotalProfit?has_content>${right.rightTotalProfit?string["0"]}</#if></td>
	       		<td align="center"><#if right.rightTotalPercent?has_content>${right.rightTotalPercent?string["0%"]}</#if></td>
	    	   </tr>
	           <tr>	
			<td align="center">上月</td>			
	       		<td align="center"><#if right.rightLastMonthSales?has_content>${right.rightLastMonthSales?string["0"]}</#if></td>
	       		<td align="center"><#if right.rightLastMonthProfit?has_content>${right.rightLastMonthProfit?string["0"]}</#if></td>
	       		<td align="center"><#if right.rightLastMonthPercent?has_content>${right.rightLastMonthPercent?string["0%"]}</#if></td>
	    	   </tr>		
	     </table>
	</div>
     </#list>
   </td>
</tr>
<tr>
   <td>
      <table id="table3">
        <tr>
           <td align="center" style="color:black font-size: 14pt">Total</td>
        </tr>		
      </table>
   </td>

   <#list a as total>
      <td>
       	 <table id="table5" >
	   <tr>
	       <td align="center">种类</td>				
	       <td align="center">销量</td>
	       <td align="center">毛利</td>
	       <td align="center">毛利率</td>
	    </tr>
	    <tr>
	       <td align="center">日均</td>				
	       <td align="center"><#if total.downTotalSales?has_content>${total.downTotalSales?string["0"]}</#if></td>
	       <td align="center"><#if total.downTotalProfit?has_content>${total.downTotalProfit?string["0"]}</#if></td>
	       <td align="center"><#if total.totalPercent?has_content>${total.totalPercent?string["0%"]}</#if></td>
	    </tr>
	    <tr>
	       <td align="center">上月</td>					
	       <td align="center"><#if total.downTotalLastMonthSales?has_content>${total.downTotalLastMonthSales?string["0"]}</#if></td>
	       <td align="center"><#if total.downTotalLastMonthProfit?has_content>${total.downTotalLastMonthProfit?string["0"]}</#if></td>
	       <td align="center"><#if total.downTotalLastMonthPercent?has_content>${total.downTotalLastMonthPercent?string["0%"]}</#if></td>
	    </tr>
	 </table>
       </td>
    </#list> 
 
<td align="center" >又赚啦！</td>
</tr>

</table>





</div>
</div>
