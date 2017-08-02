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
        <td align="center">Mother SKU</td>
        <td align="center">品名</td>
        <td align="center">实际成本</td>
        <td align="center">估重</td>
        <td align="center">实重</td>
        <td align="center">分组</td>
        <td align="center">EPS</td>
        <td align="center">开发员</td>
        <td align="center">状态</td>
        <td align="center">侵权风险</td>
        <td align="center">供应商</td>
        <td align="center">产品大类</td>
        <td align="center">CP</td>
        <td align="center">CP%</td>
        <td align="center">采购备注</td>
        <td align="center">销售备注</td>
        <td align="center">eBay链接</td>
        <td align="center">SMT链接</td>
        <td align="center">REF链接</td>

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
            <td align="center"><#if hasViewPermission><#if productList.risk?has_content>${productList.risk}</#if><#else>无</#if></td>
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.supplierLink?has_content>
                        <#if productList.supplier?has_content><a href="${productList.supplierLink}" title="${productList.supplier}" target="_blank" class="buttontext">${productList.supplier}</a></#if>
                    <#else>
                        <#if productList.supplier?has_content>${productList.supplier}</#if>
                    </#if>
                <#else>无</#if>
            </td>
            <td align="center"><#if hasViewPermission><#if productList.categoryIdParent?has_content>${productList.categoryIdParent}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.priceUsd?has_content>${productList.priceUsd?string["0.##"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.cpPercentage?has_content>${productList.cpPercentage?string["0%"]}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.notes?has_content>${productList.notes}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.listingNotes?has_content>${productList.listingNotes}</#if><#else>无</#if></td>
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.rivalItemId?has_content>
                        <a href="http://www.ebay.com/itm/ws/eBayISAPI.dll?ViewItem&item=${productList.rivalItemId}" title="${productList.rivalItemId}" target="_blank" class="buttontext">${productList.rivalItemId}</a>
                    </#if>
                <#else>无</#if>
            </td>
            <td align="center"><#if hasViewPermission><#if productList.smtLink?has_content>${productList.smtLink}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.referenceLink?has_content>${productList.referenceLink}</#if><#else>无</#if></td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>

  </div>
</div>