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
      <tr class="header-row" style="white-space: nowrap;">
        <td align="center">&nbsp;</td>
        <td align="center">Action</td>
        <td align="center">SKU</td>
        <td align="center">${uiLabelMap.motherSku}</td>
        <#if hasCustomSku?has_content><#if hasCustomSku == true><td align="center">custom SKU</td></#if></#if>
        <td align="center">${uiLabelMap.productNameCn}</td>
        <td align="center">${uiLabelMap.estimatedWeight}</td>
        <td align="center">${uiLabelMap.actualWeight}</td>
        <td align="center">${uiLabelMap.statusId}</td>
        <td align="center">${uiLabelMap.productType}</td>
        <td align="center">${uiLabelMap.productGroup}</td>
        <td align="center">${uiLabelMap.supplierId}</td>
        <td align="center">${uiLabelMap.actualPrice}</td>
        <td align="center">${uiLabelMap.moQty1}</td>
        <td align="center">${uiLabelMap.moUnitPrice1}</td>
        <td align="center">${uiLabelMap.backupSupplierId}</td>
        <td align="center">${uiLabelMap.backupMoQty1}</td>
        <td align="center">${uiLabelMap.backupMoUnitPrice1}</td>
        <td align="center">${uiLabelMap.categoryIdParent}</td>
        <td align="center">${uiLabelMap.categoryIdChild}</td>
        <td align="center">${uiLabelMap.listingNotes}</td>
        <td align="center">${uiLabelMap.purchasingNotes}</td>
        <td align="center">${uiLabelMap.identicalItemId}</td>
        <td align="center">${uiLabelMap.similarItemId}</td>
        <td align="center">${uiLabelMap.smtLink}</td>
        <td align="center">${uiLabelMap.amazonLink}</td>
        <td align="center">${uiLabelMap.wishLink}</td>
        <td align="center">${uiLabelMap.referenceLink}</td>
        <td align="center">${uiLabelMap.priceUsd}</td>
        <td align="center">${uiLabelMap.moQty2}</td>
        <td align="center">${uiLabelMap.moUnitPrice2}</td>
        <td align="center">${uiLabelMap.moQty3}</td>
        <td align="center">${uiLabelMap.moUnitPrice3}</td>
        <td align="center">${uiLabelMap.backupMoQty2}</td>
        <td align="center">${uiLabelMap.backupMoUnitPrice2}</td>
        <td align="center">${uiLabelMap.backupMoQty3}</td>
        <td align="center">${uiLabelMap.backupMoUnitPrice3}</td>
        <td align="center">${uiLabelMap.eta}</td>
        <td align="center">${uiLabelMap.backupEta}</td>
      </tr>
      <#assign alt_row = false>
      <#list productSearchList as productList>
        <#if productList.hasViewPermission?has_content>
            <#if productList.hasViewPermission>
                <#assign hasViewPermission = true>
            <#else>
                <#assign hasViewPermission = false>
            </#if>
        <#else>
            <#assign hasViewPermission = false>
        </#if>
        <#if isSbuParameter>
            <#assign isSbu = true>
        <#else>
            <#assign isSbu = false>
        </#if>
      	<tr valign="middle" style="white-space: nowrap;">
            <td align="center">
                <form method="post" name="syncProductMaster_${productList.productId}" action="<@ofbizUrl>syncProductMaster</@ofbizUrl>">
                    <input type="hidden" name="productId" value="${productList.productId!}">
                    <input type="submit" value="Sync" class="smallSubmit"/>
                </form>
            </td>
            <td>
                <#if hasViewPermission>
                    <#if isSbu || isAdmin>
                        <a class="buttontext" href="/gudao/control/editProductMaster?productId=${productList.productId}" onclick="window.open(this.href,'','height=768,width=768,scrollbars=1');return false;">edit</a>&nbsp;
                    </#if>
                    <a class="buttontext" href="<@ofbizUrl>productBasicInfoMini?productId=${productList.productId}</@ofbizUrl>" onclick="window.open(this.href,'','height=768,width=1024,scrollbars=1');return false;">${uiLabelMap.listingInfo}</a>
                </#if>
            </td>
            <td align="center">
                <#if productList.productId?has_content>
                    <#if hasViewPermission>
                        <a class="buttontext" href="/gudao/control/viewProductImage?productId=${productList.productId}" onclick="window.open(this.href,'','height=768,width=1350,scrollbars=1');return false;">${productList.productId}</a>
                    <#else>${productList.productId}
                    </#if>
                </#if>
            </td>
            <td align="center"><#if productList.motherSku?has_content>${productList.motherSku}</#if></td>
            <#if hasCustomSku?has_content>
                <#if hasCustomSku == true>
                    <td align="center"><#if productList.customSku?has_content>${productList.customSku}</#if></td>
                </#if>
            </#if>
            <td align="center">
                <#if productList.chineseName?has_content>
                    <#if productList.declaredNameCn?has_content>
                        <span title="${productList.declaredNameCn}<#if productList.declaredNameEn?has_content> - ${productList.declaredNameEn}</#if>">${productList.chineseName}</span>
                    <#else>${productList.chineseName}
                    </#if>
                </#if>
            </td>
            <td align="center"><#if hasViewPermission><#if productList.weight?has_content>${productList.weight?string["0"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.actualWeight?has_content>${productList.actualWeight?string["0"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.statusId?has_content>${productList.statusId}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.productType?has_content>${productList.productType}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.productGroup?has_content>${productList.productGroup}</#if><#else>无</#if></td>
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.mainSupplierLink?has_content>
                        <#if productList.mainSupplier?has_content><a href="${productList.mainSupplierLink}" title="${productList.mainSupplier}" target="_blank" class="buttontext">${productList.mainSupplier}</a></#if>
                    <#else>
                        <#if productList.mainSupplier?has_content>${productList.mainSupplier}</#if>
                    </#if>
                <#else>无</#if>
            </td>
            <td align="center"><#if hasViewPermission><#if productList.actualPrice?has_content>${productList.actualPrice}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.moq1?has_content>${productList.moq1}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.moq1Price?has_content>
                <a class="buttontext" href="/gudao/control/viewSupplierPrice?productId=${productList.productId}" onclick="window.open(this.href,'','height=756,width=1024,scrollbars=1');return false;">${productList.moq1Price?string["0.##"]}</a>
                </#if><#else>无</#if>
            </td>
            <#if productList.backupSupplierLink?has_content>
                <td align="center">
                    <#if hasViewPermission>
                        <#if productList.backupSupplier?has_content>
                            <a href="${productList.backupSupplierLink}" title="${productList.backupSupplier}" target="_blank" class="buttontext">${productList.backupSupplier}</a>
                        </#if>
                    <#else>无</#if>
                </td>
            <#else>
                <td align="center">
                    <#if hasViewPermission>
                        <#if productList.backupSupplier?has_content>${productList.backupSupplier}</#if>
                    <#else>无</#if>
                </td>
            </#if>
            <td align="center"><#if hasViewPermission><#if productList.backupMoq1?has_content>${productList.backupMoq1}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.backupMoq1Price?has_content>${productList.backupMoq1Price?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.categoryIdParent?has_content>${productList.categoryIdParent}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.categoryIdChild?has_content>${productList.categoryIdChild}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.listingNotes?has_content>${productList.listingNotes}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.notes?has_content>${productList.notes}</#if><#else>无</#if></td>
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.rivalItemId?has_content>
                        <a href="http://www.ebay.com/itm/ws/eBayISAPI.dll?ViewItem&item=${productList.rivalItemId}" title="${productList.rivalItemId}" target="_blank" class="buttontext">${productList.rivalItemId}</a>
                    </#if>
                <#else>无</#if>
            </td>
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.similarItemId?has_content>
                        <a href="http://www.ebay.com/itm/ws/eBayISAPI.dll?ViewItem&item=${productList.similarItemId}" title="${productList.similarItemId}" target="_blank" class="buttontext">${productList.similarItemId}</a>
                    </#if>
                <#else>无</#if>
            </td>
            <td align="center"><#if hasViewPermission><#if productList.smtLink?has_content>${productList.smtLink}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.amazonLink?has_content>${productList.amazonLink}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.wishLink?has_content>${productList.wishLink}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.referenceLink?has_content>${productList.referenceLink}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayUsPrice?has_content>${productList.ebayUsPrice?string["0.##"]}</#if><#else>无</#if></td>

            <td align="center"><#if hasViewPermission><#if productList.moq2?has_content>${productList.moq2}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.moq2Price?has_content>${productList.moq2Price?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.moq3?has_content>${productList.moq3}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.moq3Price?has_content>${productList.moq3Price?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.backupMoq2?has_content>${productList.backupMoq2}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.backupMoq2Price?has_content>${productList.backupMoq2Price?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.backupMoq3?has_content>${productList.backupMoq3}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.backupMoq3Price?has_content>${productList.backupMoq3Price?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.mainSupplierEta?has_content>${productList.mainSupplierEta}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.backupSupplierEta?has_content>${productList.backupSupplierEta}</#if><#else>无</#if></td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>

  </div>
</div>