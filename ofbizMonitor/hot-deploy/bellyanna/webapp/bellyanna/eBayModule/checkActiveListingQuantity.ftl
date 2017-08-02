<#-- Check Active Listing Quantity -->




<div id="CheckActiveListingQuantity" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
    <li class="h3">Variation - Count: <b>${variationCount}</b></li>
  </ul>
  <br class="clear"/>
</div>
<div class="screenlet-body">
    <table class="basic-table hover-bar" cellspacing='0'>
      <tr class="header-row">
        <td width="6%">Product Store Id</td>
        <td width="5%">Item Id</td>
        <td width="5%">Product Id</td>
        <td width="5%">Active Listing Quantity</td>
        <td width="5%">Quantity Sold (eBay)</td>
        <td width="10%">Price</td>
      </tr>
      <#assign alt_row = false>
      <#list variationLists as variationList>
      	<tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
            <td>${variationList.productStoreId}</td>
            <td>${variationList.itemId}</td>
            <td><a href="/catalog/control/EditProduct?productId=${variationList.productId}" target="_blank" class="buttontext">${variationList.productId}</a></td>
            <td>${variationList.quantity}</td>
            <td>${variationList.quantitySold}</td>
            <td>${variationList.startPriceCurrencyId} ${variationList.startPrice}</td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>
    
  </div>
</div>