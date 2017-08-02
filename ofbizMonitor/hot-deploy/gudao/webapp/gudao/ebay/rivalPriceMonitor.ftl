<style type="text/css">
#table-out{
font-size:12px;
text-align:center;
border: 1px solid #696969;
border-collapse:collapse;
padding:0px 0px 0px 0px;
cellpadding:0;
 cellspacing:0;

}
#table-out tr td{
text-align:center;
vertical-align:middle;
cellpadding:0;
cellspacing:0;
}
#table-single{
font-size:12px;
text-align:center;
empty-cells:show;
display:table-cell;
border-collapse:collapse;
border: 0px solid #696969;
padding:0px 0px 0px 0px;
cellpadding:0;
cellspacing:0;
}
#table-single tr td{
font-size:12px;
text-align:center;
border: 1px solid #696969;
empty-cells:show;
border-collapse:collapse;
cellpadding:0;
cellspacing:0;
}
#itemId-list {
text-align:center;
display:table-cell; 
empty-cells:show;
border-collapse:collapse;
}
#itemId-list tr td{
empty-cells:show;
width:auto;
height:auto;
}
.css{
font-weight: bold;
color:#000000;
}
.header{
text-transform: uppercase;
}
</style>
<script>
function myFunction()
{
	   var check=false;
       if (confirm("Are you sure you want to remove?")) {  
            return true; 
        }  
        else {  
            check=false;
        }  
		return check;
}
function endListing()
{
	   var check=false;
       if (confirm("Are you sure you want to end?")) {  
            return true; 
        }  
        else {  
            check=false;
        }  
		return check;
}
</script>

<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">motherSku detail</li>
        </ul>
        <br class="clear"/>
    </div>
  	<div class = "screenlet-body">
		<table>
			<form action="<@ofbizUrl>rivalPriceMonitor</@ofbizUrl>" method="post" >
<#if isAdmin>
			<tr>

				<td text-align="left" class="label" >userLogin &nbsp;&nbsp;&nbsp;</td>
				<td>
				<select name="userLoginSelect">
				<option value=""></option>
				<#list userLoginList?sort_by("userLoginId") as user>
				<option value="${user.userLoginId}" <#if userLoginSelect?has_content && userLoginSelect==user.userLoginId >SELECTED</#if>>${user.userLoginId} - ${user.externalAuthId}</option>
				</#list>
				</select>
				</td>
			</tr>
</#if>
			<tr>
				<td  class='label' >MotherSku:</td>
				<td><input type="text" name="motherSku" size="20" value="${motherSkuInput!}"/></td>
			</tr>
            <tr>
                <td  class='label' >Filter:</td>
                <td>
                    <select name="filterInput">
                        <option value="ACTION" SELECTED>Action Required Only</option>
                        <option value="ALL">All Records</option>
                    </select>

                </td>
            </tr>
			<tr>
				<td></td>
				<td align ="left"><input type="submit" value="search" class="smallSubmit"/></td>
			</tr>
			</form>
        </table> 
	</div>
</div>
<br class="clear"/>	

<div class="screenlet">
	<div class="screenlet-title-bar">
		<ul>
           	<li class="h3">sku detail</li>
        </ul>
	</div>
	<div class = "screenlet-body">	

	
		
		<table id ="table-out">
		<#list allMotherSkuList as mother>
		<tr class="header">
		<td>
			<table id="table-single" >
			<tr class="header">
				<td  style="width:90px;font-weight:bold;color:000000;border:1px solid #696969;">SKU</td>
				<td>
					<table  class="css hover-bar" >
					<tr>
						<td  style="width:70px;border:0">action</td>
						<td align="center" style="width:30px;border:0">site</td>
						<td style="border:0">
							<table id= "itemId-list" >
								<tr ><td style="width:120px; border:0">ourItemId</td> 
									 <td style="width:58px; border:0">1天卖出</td> 
									 <td style="width:58px; border:0">7天卖出</td> 
									 <td style="width:58px; border:0">价格</td> 
									 <td style="width:147px; border:0" >ACTION</td></tr>
							</table>
						</td>
						<td style="border:0">
							<table id = "itemId-list">
								<tr >
									<td align="center" style="width:120px;border:0">rivalItemId</td> 
									<td align="center" style="width:60px;border:0">1天卖出</td> 
									<td align="center" style="width:60px;border:0">7天卖出</td> 
									<td align="center" style="width:60px;border:0">价格</td>
									<td align="center" style="width:160px;border:0">ACTION</td>
								</tr>
								<tr></tr>
							</table>
						</td>
					</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="center" style="width:90px;font-weight:bold;color:#0000EE;border:1px solid #000000"><#if mother.motherSku?has_content>${mother.motherSku}</#if></td>
				<td>
					<table >
						<#list mother.siteDataList as siteList>
						<tr>
							<td align="center"  style="width:70px; empty-cells:show;">
							<form action="<@ofbizUrl>addRivalItemId</@ofbizUrl>" method="post" name="addRival_${mother.motherSku}-${siteList.site}" id="addRival_${mother.motherSku}-${siteList.site}" onclick="window.open('about:blank','addRival_${mother.motherSku}-${siteList.site}','height=250,width=500,scrollbars=1');document.addRival_${mother.motherSku}-${siteList.site}.submit();return false;" target="addRival_${mother.motherSku}-${siteList.site}"  >			
							<input type="hidden" size="20" name="motherSku" value="${mother.motherSku}"/>
							<input type="hidden" size="20" name="site" value="${siteList.site}"/>
							<input type="submit" size="20" value="add" class="smallSubmit"/>
							</form>
							</td>
							<td align="center" style="width:30px;"><#if siteList.site?has_content>${siteList.site}</#if></td>
							<td>
								<table id= "itemId-list" >
									<#list siteList.ourItemIdList as our >
									<tr >	
										<td style="width:120px;"><#if our.ourItemId?has_content><a href="http://www.ebay.com/itm/${our.ourItemId}" title="${our.ourItemId}" target="_blank" class="buttontext">${our.ourItemId}</a></#if></td>
										<td align="center" style="width:55px;"><#if our.ourOneDayQty?has_content>${our.ourOneDayQty}</#if></td>
										<td align="center" style="width:55px;"><#if our.ourSevenDaysQty?has_content>${our.ourSevenDaysQty}</#if></td>
										<td align="center" style="width:55px;"><#if our.ourPrice?has_content>${our.ourPrice}</#if></td>
										<td align="center"  style="width:146px;">
											<table >
												<tr >
												<td align="center"  style="width:73px;border:0">
												<#if our.isHigher>
												<form action="<@ofbizUrl>editEbayListingPrice</@ofbizUrl>" method="post" name="edit_${our.ourItemId}" id="edit_${our.ourItemId}" onclick="window.open('about:blank','edit_${our.ourItemId}','height=700,width=700,scrollbars=1');document.edit_${our.ourItemId}.submit();return false;" target="edit_${our.ourItemId}">
												<input type="hidden" name="ourItemId" value="${our.ourItemId}"/>
												<input type="hidden" name="rivalItemId" value="${siteList.miniRivalItemId!}"/>
												<input type="hidden" name="site" value="${siteList.site}"/>
												<input type="submit" value="price" class="smallSubmit"/>
												</form></#if>
												</td> 
												<td align="center" style="width:73px;border:0">
												<form action="<@ofbizUrl>endOurListing</@ofbizUrl>" method="post" name="end_${our.ourItemId}" id="end_${our.ourItemId}" onsubmit="return endListing()">
													<input type="hidden"  name="userLoginId" value="${userLoginId}"/>
													<input type="hidden"  name="motherSku" value="${mother.motherSku}"/>
													<input type="hidden"  name="site" value="${siteList.site}"/>
													<input type="hidden" name="itemId" value="${our.ourItemId}"/>
													<input type="submit" value="end" class="smallSubmit"/>
												</form>
												</td> 
												</tr>
											</table>
										</td>
									</tr>	
									</#list>
								</table>
							</td>

							<td>
								<table id= "itemId-list">			
									<#list siteList.rivalItemIdList as rival >
									<tr>
										<td style="width:120px;"><#if rival.rivalItemId?has_content><a href="http://www.ebay.com/itm/${rival.rivalItemId}" title="${rival.rivalItemId}" target="_blank" class="buttontext">${rival.rivalItemId}</a></#if></td>
										<td align="center" style="width:55px;"><#if rival.rivalOneDayQty?has_content>${rival.rivalOneDayQty}</#if></td>
										<td align="center" style="width:55px;"><#if rival.rivalSevenDaysQty?has_content>${rival.rivalSevenDaysQty}</#if></td>
										<td align="center" style="width:55px;"><#if rival.rivalPrice?has_content>${rival.rivalPrice}</#if></td>
										<td align="center" style="width:160px;">
											<table align="center" >
												<tr >
												<td align="center" style="width:80px;border:0">
												<form action="<@ofbizUrl>deleteRivalItemId</@ofbizUrl>" method="post" name="${mother.motherSku}_${siteList.site}_${rival.rivalItemId}" onsubmit="return myFunction()">
													<input type="hidden" name="userLoginId" value="${userLoginId}"/>
													<input type="hidden" name="motherSku" value="${mother.motherSku}"/>
													<input type="hidden" name="site" value="${siteList.site}"/>
													<input type="hidden" name="itemId" value="${rival.rivalItemId}"/> 

													<input type="submit"  value="removed" class="smallSubmit" /> 	
												</form>
												</td>
												<td align="center"style="width:80px;border:0"><input type="submit" value="history" class="smallSubmit"/></td>
												</tr>
											</table>
										</td>
									</tr>
									</#list>
								</table>
							</td>
						</tr>
						</#list>
					</table>
				</td>
			</tr>
			</table>
		</td>
		</tr>
		</#list>
		</table>
	</div>
</div>                         
<br class="clear"/>


