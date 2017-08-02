<#if mainSupplierList??>
    <div class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3">Main Supplier</b></li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body">
            <table class="basic-table hover-bar" cellspacing='10'>
                <tr class="header-row" style="white-space: nowrap;">
                    <td align="center">${uiLabelMap.supplierId}</td>
                    <td align="center">${uiLabelMap.moqIdentifier}</td>
                    <td align="center">${uiLabelMap.moqType}</td>
                    <td align="center">${uiLabelMap.moQty}</td>
                    <td align="center">${uiLabelMap.moUnitPrice}</td>
                    <td align="center">${uiLabelMap.action}</td>
                </tr>
                <#assign alt_row = false>
                <#list mainSupplierList as mainSupplier>
                    <tr valign="middle" style="white-space: nowrap;">
                        <form method="post" name="updateSupplierPrice_${productId}_${mainSupplier.supplierId}" action="<@ofbizUrl>updateSupplierPrice</@ofbizUrl>">
                            <input type="hidden" name="productId" value="${productId!}">
                            <input type="hidden" name="supplier" value="${mainSupplier.supplierId!}">
                            <input type="hidden" name="type" value="${mainSupplier.type!}">
                            <input type="hidden" name="minimumOrderValue" value="${mainSupplier.minimumOrderValue!}">
                            <td align="center">
                                ${mainSupplier.supplierId}
                            </td>
                            <td align="center">
                                <input type="text" size="20" maxlength="50" name="identifier" value="${mainSupplier.identifier!}"/>
                            </td>
                            <td align="center">
                                <#if mainSupplier.type == "MIN_QUANTITY">${uiLabelMap.moqTypeMinQty}<#else>${uiLabelMap.moqTypeMinPrice}</#if>
                            </td>
                            <td align="center">
                                ${mainSupplier.minimumOrderValue!}
                            </td>
                            <td align="center">
                                <input type="text" size="8" maxlength="20" name="unitPrice" value="${mainSupplier.unitPrice!}"/>
                            </td>
                            <td align="center">
                                <input type="submit" value="Update" class="smallSubmit"/>
                        </form>
                        <form method="post" name="deleteSupplierPrice_${productId}_${mainSupplier.supplierId}" action="<@ofbizUrl>deleteSupplierPrice</@ofbizUrl>">
                            <input type="hidden" name="productId" value="${productId!}">
                            <input type="hidden" name="supplier" value="${mainSupplier.supplierId!}">
                            <input type="hidden" name="type" value="${mainSupplier.type!}">
                            <input type="hidden" name="minimumOrderValue" value="${mainSupplier.minimumOrderValue!}">
                            <input type="submit" value="Remove" class="smallSubmit"/>
                        </form>
                        </td>
                    </tr>
                    <#assign alt_row = !alt_row>
                </#list>
            </table>
        </div>
    </div>
</#if>


<br /><br />


<#if backupSupplierList??>
<div class="screenlet">
<div class="screenlet-title-bar">
<ul>
<li class="h3">Backup Supplier</b></li>
</ul>
<br class="clear"/>
</div>
<div class="screenlet-body">
<table class="basic-table hover-bar" cellspacing='10'>
<tr class="header-row" style="white-space: nowrap;">
<td align="center">${uiLabelMap.supplierId}</td>
<td align="center">${uiLabelMap.moqIdentifier}</td>
<td align="center">${uiLabelMap.moqType}</td>
<td align="center">${uiLabelMap.moQty}</td>
<td align="center">${uiLabelMap.moUnitPrice}</td>
<td align="center">${uiLabelMap.action}</td>
</tr>
<#assign alt_row = false>
<#list backupSupplierList as backupSupplier>
<tr valign="middle" style="white-space: nowrap;">
<form method="post" name="updateSupplierPrice_${productId}_${backupSupplier.supplierId}" action="<@ofbizUrl>updateSupplierPrice</@ofbizUrl>">
<input type="hidden" name="productId" value="${productId!}">
<input type="hidden" name="supplier" value="${backupSupplier.supplierId!}">
<input type="hidden" name="type" value="${backupSupplier.type!}">
<input type="hidden" name="minimumOrderValue" value="${backupSupplier.minimumOrderValue!}">
<td align="center">
${backupSupplier.supplierId}
</td>
<td align="center">
<input type="text" size="20" maxlength="50" name="identifier" value="${backupSupplier.identifier!}"/>
</td>
<td align="center">
<#if backupSupplier.type == "MIN_QUANTITY">${uiLabelMap.moqTypeMinQty}<#else>${uiLabelMap.moqTypeMinPrice}</#if>
</td>
<td align="center">
${backupSupplier.minimumOrderValue!}
</td>
<td align="center">
<input type="text" size="8" maxlength="20" name="unitPrice" value="${backupSupplier.unitPrice!}"/>
</td>
<td align="center">
<input type="submit" value="Update" class="smallSubmit"/>
</form>
<form method="post" name="deleteSupplierPrice_${productId}_${backupSupplier.supplierId}" action="<@ofbizUrl>deleteSupplierPrice</@ofbizUrl>">
<input type="hidden" name="productId" value="${productId!}">
<input type="hidden" name="supplier" value="${backupSupplier.supplierId!}">
<input type="hidden" name="type" value="${backupSupplier.type!}">
<input type="hidden" name="minimumOrderValue" value="${backupSupplier.minimumOrderValue!}">
<input type="submit" value="Remove" class="smallSubmit"/>
</form>
</td>
</tr>
<#assign alt_row = !alt_row>
</#list>
</table>
</div>
</div>
</#if>


<#if security.hasEntityPermission("GUDAO", "_ADMIN", session)>
<br /><br />

<#if emptyList?? && emptyList?has_content>
<div class="screenlet">
<div class="screenlet-title-bar">
<ul>
<li class="h3">Empty Supplier</b></li>
</ul>
<br class="clear"/>
</div>
<div class="screenlet-body">
<table class="basic-table hover-bar" cellspacing='10'>
<tr class="header-row" style="white-space: nowrap;">
<td align="center">Supplier</td>
<td align="center">Identifier</td>
<td align="center">Type</td>
<td align="center">minimumOrderValue</td>
<td align="center">unitPrice</td>
<td align="center">action</td>
</tr>
<#assign alt_row = false>
<#list emptyList as emptySupplier>
<tr valign="middle" style="white-space: nowrap;">
<form method="post" name="updateSupplierPrice_${productId}_${emptySupplier.supplierId}" action="<@ofbizUrl>updateSupplierPrice</@ofbizUrl>">
<input type="hidden" name="productId" value="${productId!}">
<input type="hidden" name="supplier" value="${emptySupplier.supplierId!}">
<input type="hidden" name="type" value="${emptySupplier.type!}">
<input type="hidden" name="minimumOrderValue" value="${emptySupplier.minimumOrderValue!}">
<td align="center">
${emptySupplier.supplierId}
</td>
<td align="center">
<input type="text" size="20" maxlength="50" name="identifier" value="${emptySupplier.identifier!}"/>
</td>
<td align="center">
${emptySupplier.type}
</td>
<td align="center">
${emptySupplier.minimumOrderValue!}
</td>
<td align="center">
<input type="text" size="8" maxlength="20" name="unitPrice" value="${emptySupplier.unitPrice!}"/>
</td>
<td align="center">
<input type="submit" value="Update" class="smallSubmit"/>
</form>
<form method="post" name="deleteSupplierPrice_${productId}_${emptySupplier.supplierId}" action="<@ofbizUrl>deleteSupplierPrice</@ofbizUrl>">
<input type="hidden" name="productId" value="${productId!}">
<input type="hidden" name="supplier" value="${emptySupplier.supplierId!}">
<input type="hidden" name="type" value="${emptySupplier.type!}">
<input type="hidden" name="minimumOrderValue" value="${emptySupplier.minimumOrderValue!}">
<input type="submit" value="Remove" class="smallSubmit"/>
</form>
</td>
</tr>
<#assign alt_row = !alt_row>
</#list>
</table>
</div>
</div>
</#if>
</#if>


        <div align="center">
            <a href="javascript:void(0)" onclick="window.close();" class="buttontext">Close</a>
        </div>
