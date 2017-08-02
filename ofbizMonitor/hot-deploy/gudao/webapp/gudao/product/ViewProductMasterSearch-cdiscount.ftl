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
        <td align="center">${uiLabelMap.ownerGroup}</td>
        <td align="center">${uiLabelMap.statusId}</td>
        <td align="center">${uiLabelMap.supplierId}</td>
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
            <td align="center"><#if hasViewPermission><#if productList.ownerGroup?has_content>${productList.ownerGroup}</#if><#else>无</#if></td>
            <td align="center"><#if hasViewPermission><#if productList.statusId?has_content>${productList.statusId}</#if><#else>无</#if></td>
            <td align="center">
                <#if hasViewPermission>
                    <#if productList.supplierLink?has_content>
                        <#if productList.supplier?has_content><a href="${productList.supplierLink}" title="${productList.supplier}" target="_blank" class="buttontext">${productList.supplier}</a></#if>
                    <#else>
                        <#if productList.supplier?has_content>${productList.supplier}</#if>
                    </#if>
                <#else>无</#if>
            </td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>

  </div>
</div>