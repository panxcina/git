<#-- Most Watched Item -->


<script language="JavaScript" type="text/javascript">
<!--//
document.EbayMostWatchedItem.categoryId.focus();
//-->
</script>

<div id="MostWatchedItemList" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
    <li class="h3">Most Watched Item - Count: <b>${mostWatchedCount}</b></li>
  </ul>
  <br class="clear"/>
</div>
<div class="screenlet-body">
    <table class="basic-table hover-bar" cellspacing='0'>
      <tr class="header-row">
        <td width="6%">Image</td>
        <td width="5%">Watch Count</td>
        <td width="5%">BIN Price</td>
        <td width="5%">Shipping Price (type)</td>
        <td width="5%">Item ID</td>
        <td width="10%">Primary Category</td>
        <td width="10%">Title</td>
        <td width="5%">Site (Location)</td>
      </tr>
      <#assign alt_row = false>
      <#list mostWatchedLists as mostWatchedList>
      	<tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
            <td><img src="${mostWatchedList.imageUrl}"</td>
            <td>${mostWatchedList.watchCount}</td>
            <td>${mostWatchedList.currencyId} ${mostWatchedList.buyItNowPrice}</td>
            <td>${mostWatchedList.shippingCost} (${mostWatchedList.shippingType})</td>
            <td><a href="${mostWatchedList.viewItemUrl}" target="_blank">${mostWatchedList.itemId}</a></td>
            <td>${mostWatchedList.primaryCategoryName} (${mostWatchedList.primaryCategoryId})</td>
            <td>${mostWatchedList.title}</td>
            <td>${mostWatchedList.globalId} (from ${mostWatchedList.country})</td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>
    
  </div>
</div>