
<style type="text/css">

#singleTable{
width:60px;
height:auto;
font-size:14px;
text-align:center;
border: 1px solid #333333;
border-collapse:collapse;
padding:0px 0px 0px 0px;
}
#singleTable tr td{
width:30px;
height:15px;
border:1px solid #333333;
empty-cells:show;
}
#variationTable{
width:200px;
height:auto;
font-size:14px;
text-align:center;
border: 1px solid #333333;
border-collapse:collapse;
padding:0px 0px 0px 0px;
}
#variationTable tr td{
width:50px;
height:15px;
border:1px solid #333333;
empty-cells:show;
}
#specTable{
width:50px;
height:auto;
font-size:12px;
text-align:center;
border: 1px solid #333333;
border-collapse:collapse;
padding:0px 10px 0px 10px;
}
#specTable tr td{
width:25px;
height:15px;
border:1px solid #333333;
empty-cells:show;
padding:0px 10px 0px 10px;
}
</style>

<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">edit price </li>
        </ul>
        <br class="clear"/>
    </div>
  	<div class = "screenlet-body">
		<table>
			<form action="<@ofbizUrl>updateEbayListingPrice</@ofbizUrl>" method="post">
			
			<tr>
				<#if isSingle>
					<#list ourItemIdList as our>
						<td>
							<table id="singleTable">
								<tr><td class="label">ourItemId:</td><td><#if our.ourItemId?has_content>${our.ourItemId}</#if></td></tr>
								<tr><td class="label">sku:</td>	<td><#if our.ourSku?has_content>${our.ourSku}</#if></td></tr>
								<tr><td class="label">price:</td>	<td><#if our.ourPrice?has_content><input type="text" size="8" value="${our.ourPrice}" name="price" id ="priceFill"/></#if></td></tr>
							</table>
						</td>
						<input type="hidden" name="varSeq" value="00001"/>
						<input type="hidden" name="priceInput" value="${our.priceInput}" id="priceInput"/>
					</#list>
				<#else>
					<#list ourItemIdList as our>
						<td>
							<table id="variationTable">
								<tr><td >ourItemId:</td>	<td><#if our.ourItemId?has_content>${our.ourItemId}</#if></td>  <td>action</td></tr>
								<tr>			
									<td class="label" >sku</td>
									<td class="label" >price</td>
									<td>
										<table id="specTable">
											<tr>
												<td class="label" >name</td>
												<td class="label" >value</td>
											</tr>
										</table>
									</td>

								</tr>
								<#list our.ourVarList as ourVar>
								<input type="hidden" name="varSeq" value="${ourVar.ourSeqId}"/>
                                <tr>
									<td  ><#if ourVar.ourSku?has_content>${ourVar.ourSku}</#if></td>
									<td  ><#if ourVar.ourPrice?has_content><input type="text" size="8" value="${ourVar.ourPrice}" name="price" id="price"/></#if></td>
									<input type="hidden" name="priceInput" value="${ourVar.priceInput}" id="priceInput"/>
									<td>
										<table id="specTable">
											<#list ourVar.ourSpecList as spec>
												<tr>

													<td align="center"><#if spec.ourName?has_content>${spec.ourName}</#if></td>
													<td align="center"><#if spec.ourValue?has_content>${spec.ourValue}</#if></td>
												</tr>
											</#list>
										</table>
									</td>
								</tr>
								</#list>
							</table>
						</td>
					</#list>				
				</#if>
			

				<#if isRivalSingle>
					<#list rivalItemIdList as rival>

						<td>
							<table id="singleTable">
								<tr><td class="label" >rivalItemId:</td>	<td><#if rival.rivalItemId?has_content>${rival.rivalItemId}</#if></td></tr>
								<tr><td class="label" >sku:</td>	<td><#if rival.rivalSku?has_content>${rival.rivalSku}</#if></td></tr>
								<tr><td class="label" >price:</td>	<td><#if rival.rivalPrice?has_content>${rival.rivalPrice}</#if></td></tr>
							</table>
						</td>
					</#list>
				
				<#else>
					<#list rivalItemIdList as rival>
						<td>
							<table id="variationTable">
								<tr><td class="label" >rivalItemId:</td>	<td><#if rival.rivalItemId?has_content>${rival.rivalItemId}</#if></td><td>action</td></tr>
								
									<tr>
										<td class="label" >sku</td>
										<td class="label" >price</td>
										<td>
											<table id="specTable">
												<tr>
													<td class="label" >name</td>
													<td class="label" >value</td>
												</tr>
											</table>
										</td>
									</tr>
									<#list rival.variationList as var >
									<tr>
										<td  ><#if var.rivalSku?has_content>${var.rivalSku}</#if></td>
										<td  ><#if var.rivalPrice?has_content>${var.rivalPrice}</#if></td>
										<td>
											<table id="specTable">
												<#list var.rivalSpecList as rivalSpec>
													<tr>
														<td align="center"><#if rivalSpec.rivalName?has_content>${rivalSpec.rivalName}</#if></td>
														<td align="center"><#if rivalSpec.rivalValue?has_content>${rivalSpec.rivalValue}</#if></td>
													</tr>
												</#list>
											</table>
										</td>
									
									</tr>
									</#list>
							</table>
						</td>
					</#list>
				</#if>
			</tr>
			<tr>
				<td align="right">
				<input type="submit" value="update" class="smallSubmit"/></td>
				<input type="hidden" name="itemId" value="${ourItemIdInput}"/>
				<td align="left"><a href="javascript:void(0)" onclick="window.close();" class="smallSubmit">Close</a></td>
			</tr>
			</form>
        </table> 
	</div>
</div>

<br class="clear"/>


