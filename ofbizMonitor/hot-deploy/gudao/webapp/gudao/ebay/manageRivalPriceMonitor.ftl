<style type="text/css">
#table{
font-size:12px;
text-align:center;
border: 0px solid #696969;
padding:0px 10px 0px 10px;
width:auto;
height:auto;
white-space: nowrap;
}
#table tr td{
text-align:center;
border: 1px solid #696969;
width:120px;
}
</style>
<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">sku detail</li>
        </ul>
        <br class="clear"/>
    </div>
  	<div class = "screenlet-body">
		<table>
			<tr>
				
			
					<td>
					<form action="<@ofbizUrl>editRivalPriceMonitor</@ofbizUrl>" method="post" name="create_new" id="create_new" onclick="window.open('about:blank','create_new','height=500,width=500,scrollbars=1');document.create_new.submit(); return false;" target="create_new">	
			   			<input type="submit" value="Create New" class="smallSubmit"/>
					</form>
					</td>
			</tr>
			
			<form action="<@ofbizUrl>manageRivalPriceMonitor</@ofbizUrl>" method="post" name="adminCreate" id="adminCreate">
			<tr>
				<td text-align="left" class="label" >userLogin &nbsp;&nbsp;&nbsp;</td>
				<td>
				<select name="userLoginIdInput">
				<option value=""></option>
				<#list userLoginList?sort_by("userLoginId") as user>
				<option value="${user.userLoginId}" <#if userLoginIdVar?has_content && userLoginIdVar==user.userLoginId >SELECTED</#if>>${user.userLoginId} - ${user.externalAuthId}</option>
				</#list>
				</select>
				</td>
			</tr>
			<tr><td></td><td><input type="submit" value="search" class="smallSubmit"/></td></tr>
			</form>
        </table> 
	</div>
</div>

<br class="clear"/>
<div class="screenlet">
		<div class="screenlet-title-bar">
		    <ul>
		        <li class="h3">motherSku data</li>
		    </ul>
		    <br class="clear"/>
		</div>
	  	<div class = "screenlet-body">
			<table class="basic-table hover-bar" cellspacing='0'id ="table">
			<tr class="header-row" >
			<td align="center">motherSku</td>
			<td>edit</td>
			</tr>
			<#if motherSkuList?has_content>
			<#list motherSkuList as mother>
				
				<tr >
					<td align="center">
						${mother.motherSku}
					</td>
					<td><#if mother.isNew>
						<form action="<@ofbizUrl>deleteMotherSku</@ofbizUrl>" method="post" name="delete_${mother.motherSku}" id="delete_${mother.motherSku}">
							<input type="hidden" name="motherSku" value="${mother.motherSku}" />
							<input type="hidden" name="userLoginId" value="${mother.userLoginId}" />
							<input type="hidden" name="itemId" value="NEW"/>
							<input type="submit" value="delete" class="smallSubmit"/>
						</form></#if>
					</td>
				</tr>
				
			</#list> 
			</#if> 
			</table>
	
		</div>
</div>

