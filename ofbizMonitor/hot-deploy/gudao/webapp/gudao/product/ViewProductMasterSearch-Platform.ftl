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
        <td align="center"></td>
        <td align="center">SKU</td>
        <td align="center">${uiLabelMap.motherSku}</td>
        <td align="center">${uiLabelMap.productNameCn}</td>
        <td align="center">${uiLabelMap.actualPrice}</td>
        <td align="center">${uiLabelMap.estimatedWeight}</td>
        <td align="center">${uiLabelMap.actualWeight}</td>
        <td align="center">${uiLabelMap.productGroup}</td>
        <td align="center">${uiLabelMap.eps}</td>
        <td align="center">${uiLabelMap.ownerGroup}</td>
        <td align="center">${uiLabelMap.statusId}</td>
        <td align="center">${uiLabelMap.supplierId}</td>
        <td align="center">${uiLabelMap.categoryIdParent}</td>
        <td align="center">${uiLabelMap.ebayUsPrice}</td>
        <td align="center">${uiLabelMap.ebayUsPercentage}</td>
        <td align="center">${uiLabelMap.purchasingNotes}</td>
        <td align="center">${uiLabelMap.listingNotes}</td>
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
            <td align="center">
                <#if productList.chineseName?has_content>
                    <#if productList.declaredNameCn?has_content>
                        <span title="${productList.declaredNameCn}<#if productList.declaredNameEn?has_content> - ${productList.declaredNameEn}</#if>">${productList.chineseName}</span>
                    <#else>${productList.chineseName}
                    </#if>
                </#if>
            </td>
            <td align="center"><#if hasViewPermission><#if productList.actualPrice?has_content>${productList.actualPrice?string["0.##"]}</#if><#else>无</#if></td>
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
            <td align="center"><#if hasViewPermission><#if productList.categoryIdParent?has_content>${productList.categoryIdParent}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayUsPrice?has_content>${productList.ebayUsPrice?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.ebayUsPercentage?has_content>${productList.ebayUsPercentage?string["0%"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.notes?has_content>${productList.notes}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.listingNotes?has_content>${productList.listingNotes}</#if><#else>无</#if></td>
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