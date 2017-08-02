<#-- product listing status -->



<div id="productListingStatus" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
<li class="h3">Product Listing Status - Search Result (Count: ${productCount})</b></li>
  </ul>
  <br class="clear"/>
</div><#if sewa?has_content>${sewa}</#if>
<#--<div class="screenlet-body">
<#if productId?has_content>productId : ${productId}</#if><br />
<#if productGroup?has_content>productGroup : ${productGroup}</#if><br />
<#if ownerGroup?has_content>ownerGroup : ${ownerGroup}</#if><br />
<#if platform?has_content>platform : ${platform}</#if><br />
<#if ebayPicture?has_content>ebayPicture : ${ebayPicture}</#if><br />
<#if amazonPicture?has_content>amazonPicture : ${amazonPicture}</#if><br />
<#if ebayUsListing?has_content>ebayUsListing : ${ebayUsListing}</#if><br />
<#if ebayUkListing?has_content>ebayUkListing : ${ebayUkListing}</#if><br />
<#if ebayAuListing?has_content>ebayAuListing : ${ebayAuListing}</#if><br />
<#if ebayCaListing?has_content>ebayCaListing : ${ebayCaListing}</#if><br />
<#if ebayDeListing?has_content>ebayDeListing : ${ebayDeListing}</#if><br />
<#if ebayFrListing?has_content>ebayFrListing : ${ebayFrListing}</#if><br />
<#if ebayEsListing?has_content>ebayEsListing : ${ebayEsListing}</#if><br />-->
<table class="basic-table hover-bar" cellspacing='10'>
<tr class="header-row" style="white-space: nowrap;">
<td align="center">SKU</td>
<td align="center">品名</td>
<td align="center">英文品名</td>
<td align="center">Qty Sold</td>
<td align="center">productGroup</td>
<td align="center">ownerGroup</td>
<td align="center">platform</td>
<td align="center">ebay Picture</td>
<td align="center">amazon Picture</td>
<td align="center">US</td>
<td align="center">UK</td>
<td align="center">AU</td>
<td align="center">CA</td>
<td align="center">DE</td>
<td align="center">FR</td>
<td align="center">ES</td>

</tr>
<#assign alt_row = false>
<#list productList as product>
<tr valign="middle" style="white-space: nowrap;">
<td align="center"><#if product.productIdResult?has_content><a class="buttontext" href="/gudao/control/viewProductImage?productId=${product.productIdResult}" onclick="window.open(this.href,'','height=768,width=1024,scrollbars=1');return false;">${product.productIdResult}</a></#if></td>
<td align="center"><#if product.chineseName?has_content>${product.chineseName}</#if></td>
<td align="center"><#if product.englishName?has_content>${product.englishName}</#if></td>
<td align="center"><#if product.qtySold?has_content>${product.qtySold}</#if></td>
<td align="center"><#if product.productGroupResult?has_content>${product.productGroupResult}</#if></td>
<td align="center"><#if product.ownerGroupResult?has_content>${product.ownerGroupResult}</#if></td>
<td align="center"><#if product.platformResult?has_content>${product.platformResult}</#if></td>
<td align="center"><#if product.ebayPictureResult?has_content>${product.ebayPictureResult}</#if></td>
<td align="center"><#if product.amazonPictureResult?has_content>${product.amazonPictureResult}</#if></td>
<td align="center"><#if product.ebayUsListingResult?has_content>${product.ebayUsListingResult}</#if></td>
<td align="center"><#if product.ebayUkListingResult?has_content>${product.ebayUkListingResult}</#if></td>
<td align="center"><#if product.ebayAuListingResult?has_content>${product.ebayAuListingResult}</#if></td>
<td align="center"><#if product.ebayCaListingResult?has_content>${product.ebayCaListingResult}</#if></td>
<td align="center"><#if product.ebayDeListingResult?has_content>${product.ebayDeListingResult}</#if></td>
<td align="center"><#if product.ebayFrListingResult?has_content>${product.ebayFrListingResult}</#if></td>
<td align="center"><#if product.ebayEsListingResult?has_content>${product.ebayEsListingResult}</#if></td>
</tr>
<#assign alt_row = !alt_row>
</#list>
</table>







</div>
</div>