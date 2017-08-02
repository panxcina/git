
<#-- product master -->
<#-- <#if alt_row> class="alternate-row"</#if> -->



</style>
<div id="productLookupList" class="screenlet">
<div class="screenlet-title-bar">
 
<br class="clear"/>
</div>
<div class="screenlet-body">
<script></script>

<form action="<@ofbizUrl>inventoryform</@ofbizUrl>" method="post" name="form1">
      <table  border="0" cellspacing="0" cellpadding="0">

<tr>
    		<td text-align="left" class="label" >平台 &nbsp;&nbsp;&nbsp;</td>
    		<td>
		<select name="platform">
		<option value=""></option>
		<#list findPlatformList?sort_by("description") as platform>
		<option value="${platform.roleTypeId}" <#if parameters.platform?has_content && parameters.platform = platform.roleTypeId>SELECTED</#if>>${platform.roleTypeId}</option>
		</#list>
		</select>
		</td>

</tr>
<tr>
    		<td text-align="left" class="label" >部门 &nbsp;&nbsp;&nbsp;</td>
    		<td>
		<select name="department">
		<option value=""></option>
		<#list findDepartmentList?sort_by("partyId") as depart>
		<option value="${depart.partyId}" <#if parameters.department?has_content && parameters.department = depart.partyId>SELECTED</#if>>${depart.groupName}</option>
		</#list>
		</select>
		</td>
</tr>

 <tr>
    		<td text-align="left" class="label" >事业部 &nbsp;&nbsp;&nbsp;</td>
    		<td>
		<select name="ownerGroup">
		<option value=""></option>
		<#list findOwnerGroupList?sort_by("officeSiteName") as ownerGrp>
		<option value="${ownerGrp.officeSiteName}" <#if parameters.ownerGroup?has_content && parameters.ownerGroup = ownerGrp.officeSiteName>SELECTED</#if>>${ownerGrp.officeSiteName}</option>
		</#list>
		</select>
		</td>

</tr>
<tr>
    		<td text-align="left" class="label" >天数 &nbsp;&nbsp;&nbsp;</td>
    		<td>
		<select name="days">
		<option value="">1</option>
		<option value="">2</option>
		<option value="">3</option>
		<option value="">4</option>
		<option value="">5</option>
		<option value="">6</option>
		<option value="">7</option>
		<option value="">8</option>
		<option value="">9</option>
		<option value="">10</option>
		<option value="">11</option>
		<option value="">12</option>
		<option value="">13</option>		
		<option value="">14</option>
		<option value="">15</option>
		<option value="">16</option>
		<option value="">17</option>
		<option value="">18</option>
		<option value="">19</option>
		<option value="">20</option>
		<option value="">21</option>
		<option value="">22</option>
		<option value="">23</option>
		<option value="">24</option>
		<option value="">25</option>
		<option value="">26</option>
		<option value="">27</option>
		<option value="">28</option>
		<option value="">29</option>
		<option value="">30</option>
		<option value="">31</option>
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
<td align="center">sku</td>
<td align="center">总状态</td>
<td align="center">部门</td>
<td align="center">平台</td>
<td align="center">平台产品分组</td>
<td align="center">产品名称</td>
<td align="center">事业部</td>
<td align="center">供应商</td>
<td align="center">库位</td>
<td align="center">商品成本</td>
<td align="center">库存数量</td>
<td align="center">库存金额</td>
<td align="center">商品创建时间</td>
<td align="center">天数范围</td>
<td align="center">销量金额</td>
<td align="center">库存销量</td>
<td align="center">库存周转天数</td>
</tr>
<tr class="header-row" style="width:200px;white-space: ">
<#list p as inventoryList>
<td align="center"><#if inventoryList.productId?has_content>${inventoryList.productId}</#if></td>
<td align="center"><#if inventoryList.statusId?has_content>${inventoryList.statusId}</#if></td>
<td align="center"><#if inventoryList.department?has_content>${inventoryList.department}</#if></td>
<td align="center"><#if inventoryList.platform?has_content>${inventoryList.platform}</#if></td>
<td align="center"><#if inventoryList.platformProductGroup?has_content>${inventoryList.platformProductGroup}</#if></td>
<td align="center"><#if inventoryList.chineseName?has_content>${inventoryList.chineseName}</#if></td>
<td align="center"><#if inventoryList.ownerGroup?has_content>${inventoryList.ownerGroup}</#if></td>
<td align="center"><#if inventoryList.supplier?has_content>${inventoryList.supplier}</#if></td>
<td align="center">kuwei</td>
<td align="center"><#if inventoryList.productCost?has_content>${inventoryList.productCost}</#if></td>
<td align="center"><#if inventoryList.qty?has_content>${inventoryList.qty}</#if></td>
<td align="center"><#if inventoryList.inventoryAmount?has_content>${inventoryList.inventoryAmount}</#if></td>
<td align="center"><#if inventoryList.productCreatedTime?has_content>${inventoryList.productCreatedTime}</#if></td>
<td align="center"><#if inventoryList.salesDays?has_content>${inventoryList.salesDays}</#if></td>
<td align="center"><#if inventoryList.sales?has_content>${inventoryList.sales}</#if></td>
<td align="center"><#if inventoryList.qtySold?has_content>${inventoryList.qtySold}</#if></td>
<td align="center"><#if inventoryList.qtyConvertDayCount?has_content>${inventoryList.qtyConvertDayCount}</#if></td>
</tr>
</#list>
</table>
</div>
</div>
