<#-- product master -->
<#-- <#if alt_row> class="alternate-row"</#if> -->

<script language="JavaScript" type="text/javascript">
<!--//
document.findProduct.productId.focus();
//-->
</script>
<div id="productLookupList" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">SKU Count: <b>${productCount}</b></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
        <table class="basic-table hover-bar" cellspacing='10'>
            <tr class="header-row" style="white-space:">
                <td align="center">SKU</td>
                <td align="center" width="150px" style="word-break:break-word;">${uiLabelMap.productNameCn}</td>
                <td align="center">${uiLabelMap.productGroup}</td>
                <td align="center">${uiLabelMap.ownerGroup}</td>
                <td align="center">${uiLabelMap.statusId}</td>
                <td align="center">${uiLabelMap.platformStatus}</td>
                <td align="center">${uiLabelMap.categoryIdParent}</td>
                <td align="center">${uiLabelMap.categoryIdChild}</td>
            </tr>
            <#list veroList as vero  >
                <tr valign="middle" style="white-space:">
                    <td align="center">
                        <#if vero.productId?has_content>
                            <a class="buttontext" href="/gudao/control/viewProductImage?productId=${vero.productId}" onclick="window.open(this.href,'','height=768,width=1350,scrollbars=1');return false;">${vero.productId}</a>
                        </#if>
                    </td>
                    <td align="center"width="10">
                        <#if vero.chineseName?has_content>
                            ${vero.chineseName}
                        <#else>
                            ${vero.englishName}
                        </#if>
                    </td>
                    <td align="center"><#if vero.productGroup?has_content>${vero.productGroup}</#if></td>
                    <td align="center"><#if vero.ownerGroup?has_content>${vero.ownerGroup}</#if></td>
                    <td align="center"><#if vero.statusId?has_content>${vero.statusId}</#if></td>
                    <td align="center"><#if vero.platform?has_content>${vero.platformStatus}</#if></td>
                    <td align="center"><#if vero.categoryIdParent?has_content>${vero.categoryIdParent}</#if></td>
                    <td align="center"><#if vero.categoryIdChild?has_content>${vero.categoryIdChild}</#if></td>
                </tr>
            </#list>
        </table>
    </div>
</div>
