<#-- product master -->
<#-- <#if alt_row> class="alternate-row"</#if> -->
<#if security.hasEntityPermission("PM", "_VIEW", session)>

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
        <td align="center"></td>
        <td align="center">SKU</td>
        <td align="center">${uiLabelMap.motherSku}</td>
        <#if hasCustomSku?has_content><#if hasCustomSku == true><td align="center">custom SKU</td></#if></#if>
        <td align="center">${uiLabelMap.productNameCn}</td>
        <td align="center">${uiLabelMap.productName}</td>
        <td align="center">${uiLabelMap.actualPrice}</td>
        <td align="center">${uiLabelMap.moQty1}</td>
        <td align="center">${uiLabelMap.moUnitPrice1}</td>
        <td align="center">${uiLabelMap.estimatedWeight}</td>
        <td align="center">${uiLabelMap.actualWeight}</td>
        <td align="center">${uiLabelMap.productGroup}</td>
        <td align="center">${uiLabelMap.eps}</td>
        <td align="center">${uiLabelMap.gms}</td>
        <td align="center">${uiLabelMap.ownerGroup}</td>
        <td align="center">${uiLabelMap.statusId}</td>
        <td align="center">${uiLabelMap.supplierId}</td>
        <td align="center">${uiLabelMap.supplierRank}</td>
        <td align="center">${uiLabelMap.categoryIdParent}</td>
        <td align="center">${uiLabelMap.categoryIdChild}</td>
        <td align="center">${uiLabelMap.productType}</td>
        <td align="center">${uiLabelMap.ebayUsPrice}</td>
        <td align="center">${uiLabelMap.ebayUsPercentage}</td>
        <td align="center">${uiLabelMap.ebayUkPrice}</td>
        <td align="center">${uiLabelMap.ebayUkPercentage}</td>
        <td align="center">${uiLabelMap.ebayAuPrice}</td>
        <td align="center">${uiLabelMap.ebayAuPercentage}</td>
        <td align="center">${uiLabelMap.ebayCaPrice}</td>
        <td align="center">${uiLabelMap.ebayCaPercentage}</td>
        <td align="center">${uiLabelMap.ebayDePrice}</td>
        <td align="center">${uiLabelMap.ebayDePercentage}</td>
        <td align="center">${uiLabelMap.ebayFrPrice}</td>
        <td align="center">${uiLabelMap.ebayFrPercentage}</td>
        <td align="center">${uiLabelMap.cdiscountUsPrice}</td>
        <td align="center">${uiLabelMap.smtUsPrice}</td>
        <td align="center">${uiLabelMap.smtUsPercentage}</td>
        <td align="center">${uiLabelMap.smtUsShippingMethod}</td>
        <td align="center">${uiLabelMap.wishUsPrice}</td>
        <td align="center">${uiLabelMap.wishUsShippingPrice}</td>
        <td align="center">${uiLabelMap.wishUsPercentage}</td>
        <td align="center">${uiLabelMap.amazonUsPrice}</td>
        <td align="center">${uiLabelMap.amazonUsPercentage}</td>
        <td align="center">${uiLabelMap.purchaser}</td>
        <td align="center">${uiLabelMap.eta}</td>
        <td align="center">${uiLabelMap.minOrderDays}</td>
        <td align="center">${uiLabelMap.extraStockingDays}</td>
        <td align="center">${uiLabelMap.sourcingDate}</td>
        <td align="center">${uiLabelMap.purchasingNotes}</td>
        <td align="center">${uiLabelMap.listingNotes}</td>
        <td align="center">${uiLabelMap.ebayPicture}</td>
        <td align="center">${uiLabelMap.amazonPicture}</td>
        <td align="center">${uiLabelMap.finePicture}</td>
        <td align="center">${uiLabelMap.backupSupplierId}</td>
        <td align="center">${uiLabelMap.ebayLink}</td>
        <td align="center">${uiLabelMap.smtLink}</td>
        <td align="center">${uiLabelMap.wishLink}</td>
        <td align="center">${uiLabelMap.amazonLink}</td>
        <td align="center">${uiLabelMap.referenceLink}</td>
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
      	<tr valign="middle" style="white-space: nowrap;">
            <td align="center">
                <form method="post" name="syncProductMaster_${productList.productId}" action="<@ofbizUrl>syncProductMaster</@ofbizUrl>">
                    <input type="hidden" name="productId" value="${productList.productId!}">
                    <input type="submit" value="Sync" class="smallSubmit"/>
                </form>
            </td>
            <td align="center">
                <a class="buttontext" href="<@ofbizUrl>productBasicInfoMini?productId=${productList.productId}</@ofbizUrl>" onclick="window.open(this.href,'','height=768,width=1024,scrollbars=1');return false;">${uiLabelMap.listingInfo}</a>
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
<td align="center">
<#if productList.englishName?has_content>
${productList.englishName}
</#if>
</td>
            <td align="center"><#if hasViewPermission><#if productList.actualPrice?has_content>${productList.actualPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.moq1?has_content>${productList.moq1}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission>
                <#if productList.moq1Price?has_content>
                    <a class="buttontext" href="/gudao/control/viewSupplierPrice?productId=${productList.productId}" onclick="window.open(this.href,'','height=756,width=1024,scrollbars=1');return false;">${productList.moq1Price?string["0.##"]}</a>
                </#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.weight?has_content>${productList.weight?string["0"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.actualWeight?has_content>${productList.actualWeight?string["0"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.productGroup?has_content>${productList.productGroup}</#if><#else>无</#if></td>
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.finalEps?has_content>
                        <a class="buttontext" href="/gudao/control/viewProductGpsMini?productId=${productList.productId}" onclick="window.open(this.href,'','height=200,width=400,scrollbars=1');return false;">${productList.finalEps?string["0"]}</a>
                    </#if>
                <#else>无</#if>
            </td>
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.finalGms?has_content>
                        <a class="buttontext" href="/gudao/control/viewProductGmsMini?productId=${productList.productId}" onclick="window.open(this.href,'','height=400,width=600,scrollbars=1');return false;">${productList.finalGms}</a>
                    </#if>
                <#else>无</#if>
            </td>
            <td align="center"><#if hasViewPermission><#if productList.ownerGroup?has_content>${productList.ownerGroup}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.statusId?has_content>${productList.statusId}</#if><#else>无</#if></td>
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.mainSupplierLink?has_content>
                        <#if productList.mainSupplier?has_content><a href="${productList.mainSupplierLink}" title="${productList.mainSupplier}" target="_blank" class="buttontext">${productList.mainSupplier}</a></#if>
                    <#else>
                        <#if productList.mainSupplier?has_content>${productList.mainSupplier}</#if>
                    </#if>
                <#else>无</#if>
            </td>
            <td align="center"><#if hasViewPermission><#if productList.supplierRank?has_content>${productList.supplierRank}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.categoryIdParent?has_content>${productList.categoryIdParent}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.categoryIdChild?has_content>${productList.categoryIdChild}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.productType?has_content>${productList.productType}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayUsPrice?has_content>${productList.ebayUsPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayUsPercentage?has_content>${productList.ebayUsPercentage?string["0%"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayUkPrice?has_content>${productList.ebayUkPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayUkPercentage?has_content>${productList.ebayUkPercentage?string["0.0%"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayAuPrice?has_content>${productList.ebayAuPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayAuPercentage?has_content>${productList.ebayAuPercentage?string["0%"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayCaPrice?has_content>${productList.ebayCaPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayCaPercentage?has_content>${productList.ebayCaPercentage?string["0%"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayDePrice?has_content>${productList.ebayDePrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayDePercentage?has_content>${productList.ebayDePercentage?string["0%"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayFrPrice?has_content>${productList.ebayFrPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayFrPercentage?has_content>${productList.ebayFrPercentage?string["0%"]}</#if><#else>无</#if></td>
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.ebayDePrice?has_content>
                        <#assign newEurPrice = productList.ebayDePrice * 1.25>
                        ${newEurPrice?string["0.##"]}
                    </#if>
                <#else>无</#if>
            </td>
            <td align="center"><#if hasViewPermission><#if productList.smtUsPrice?has_content>${productList.smtUsPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.smtUsPercentage?has_content>${productList.smtUsPercentage?string["0%"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.smtUsShippingMethod?has_content>${productList.smtUsShippingMethod}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.wishUsPrice?has_content>${productList.wishUsPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.wishUsShippingPrice?has_content>${productList.wishUsShippingPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.wishUsPercentage?has_content>${productList.wishUsPercentage?string["0%"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.amazonUsPrice?has_content>${productList.amazonUsPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.amazonUsPercentage?has_content>${productList.amazonUsPercentage?string["0%"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.purchaser?has_content>${productList.purchaser}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.mainSupplierEta?has_content>${productList.mainSupplierEta}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.minOrderDays?has_content>${productList.minOrderDays}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.extraStockingDay?has_content>${productList.extraStockingDay}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.sourcingDate?has_content>${productList.sourcingDate}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.notes?has_content>${productList.notes}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.listingNotes?has_content>${productList.listingNotes}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayPicture?has_content>${productList.ebayPicture}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.amazonPicture?has_content>${productList.amazonPicture}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.finePicture?has_content>${productList.finePicture}</#if><#else>无</#if></td>
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
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.rivalItemId?has_content>
                        <a href="http://www.ebay.com/itm/ws/eBayISAPI.dll?ViewItem&item=${productList.rivalItemId}" title="${productList.rivalItemId}" target="_blank" class="buttontext">${productList.rivalItemId}</a>
                    <#elseif productList.similarItemId?has_content>
                        (相似) <a href="http://www.ebay.com/itm/ws/eBayISAPI.dll?ViewItem&item=${productList.similarItemId}" title="${productList.similarItemId}" target="_blank" class="buttontext">${productList.similarItemId}</a>
                    </#if>
                <#else>无</#if>
            </td>
            <td align="center"><#if hasViewPermission><#if productList.smtLink?has_content>${productList.smtLink}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.wishLink?has_content>${productList.wishLink}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.amazonLink?has_content>${productList.amazonLink}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.referenceLink?has_content>${productList.referenceLink}</#if><#else>无</#if></td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>
  </div>
</div>
</#if>