<#-- product eBay Active Listing-->


<div id="productLookupList" class="screenlet">
<#--<div class="screenlet-title-bar">
  <ul>
    <li class="h3">Active Listing - Count: <b>${listingCount}</b></li>
  </ul>
  <br class="clear"/>
</div>-->
<div class="screenlet-body">
    <table class="basic-table hover-bar" cellspacing='0'>
      <tr class="header-row">
        <td width="6%">Item ID</td>
        <td width="6%">Title</td>
        <td width="6%">SKU</td>
        <td width="6%">Price</td>
        <td width="6%">Hit</td>
        <td width="6%">Watch</td>
        <td width="6%">Qty Available<br />(eBay)</td>
        <td width="6%">Qty Sold<br />(eBay)</td>
        <#--<td width="6%">Qty Sold<br />last ${soldInLastXdays} days</td>
        <td width="6%">ATP</td>
        <td width="6%">QOH</td>-->
        <td width="6%">Duration</td>
        <td width="6%">Type</td>
        <td width="6%">Updated Time</td>
      </tr>
      <#assign alt_row = false>
      <#list ebayActiveListings?sort_by("productStoreId") as ebayActiveListing>
      	<tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
            <td>${ebayActiveListing.itemId}</td>
            <td>${ebayActiveListing.title}</td>
            <td>${ebayActiveListing.sku}</td>
            <td>${ebayActiveListing.currencyId} ${ebayActiveListing.startPrice}</td>
            <td>${ebayActiveListing.hitCount}</td>
            <td>${ebayActiveListing.watchCount}</td>
            <td>${ebayActiveListing.quantity}</td>
            <td>
                <#if ebayActiveListing.quantitySold?has_content>
                    ${ebayActiveListing.quantitySold}
                <#else>
                    0
                </#if>
            </td>
            <#--<td>${ebayActiveListing.soldQty}</td>
            <td>${ebayActiveListing.atp}</td>
            <td>${ebayActiveListing.qoh}</td>-->
            <td>${ebayActiveListing.listingDuration}</td>
            <td>${ebayActiveListing.listingType}</td>
            <td>${ebayActiveListing.LastUpdatedStamp?date}</td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>
    
  </div>
</div>