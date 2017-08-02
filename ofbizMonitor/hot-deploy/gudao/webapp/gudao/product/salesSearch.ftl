
<#if security.hasEntityPermission("PM", "_VIEW", session)>

    <div id="salesSearchLookup" class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3">SKU Count: <b>${productCount}</b></li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body">






<form action="<@ofbizUrl>salesSearch</@ofbizUrl>" method="post" name="salesSearchForm" id="salesSearchFormProduct">
<input type="hidden" name="mercuryId" value="<#if mercuryHeader??>${mercuryHeader.mercuryId}</#if>"/>
<input type="text" name="productId" size="80"/>
<select name="filter">
<option value="ALL">All</option>
<option value="">--------</option>
<option value="${userLoginId}">${userLoginId}</option>
<option value="">--------</option>
<#list userLoginList as userLoginGV>
<option value="${userLoginGV.createdBy}" <#if filterInput == userLoginGV.createdBy>SELECTED</#if>>${userLoginGV.createdBy}</option>
</#list>
</select>
<br class="clear"/>
<#if isHeadquarter || isAdmin>
<select name = "treeType">
<option value="PROFILE" <#if treeType == "PROFILE">SELECTED</#if>>Profile</option>
<option value="DEPARTMENT" <#if treeType == "DEPARTMENT">SELECTED</#if>>Department</option>
</select>
</#if>
<input type="submit" value="Apply" class="smallSubmit"/>
</form>





        </div>
    </div>







<#else>
No Permission - 没有权限
</#if>