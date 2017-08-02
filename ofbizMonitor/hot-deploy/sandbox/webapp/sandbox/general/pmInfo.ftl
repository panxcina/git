<#if product??>
<table>
    <tr>
        <td>SKU</td>
<td>${product.productId}</td>
</tr>
<tr>
<td>Status</td>
<td>${product.statusId}</td>
</tr>
</table>
<#else>
No Product Found
</#if>