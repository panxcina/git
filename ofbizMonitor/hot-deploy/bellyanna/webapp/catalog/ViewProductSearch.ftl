<#-- product search list -->
<#--<div id="productLookup" class="screenlet">
    <div class="screenlet-title-bar">
      <ul>
        <li class="h3">Product Search</li>
      </ul>
      <br class="clear"/>
    </div>
    <div class="screenlet-body">
      <form method="get" name="findProduct" action="<@ofbizUrl>productSearchList</@ofbizUrl>">
        <table class="basic-table" cellspacing='0'>
          <tr>
            <td align="right" class="label">${uiLabelMap.ProductProductId} / SKU</td>
            <td nowrap="nowrap">
                <div>
                        <input type="text" name="productId" size="40" maxlength="50"/>
                        <input type="submit" value="Search"/>
                </div>
            </td>
          </tr>
          <tr><td><br /></td></tr>
          <tr><td colspan=2>Product Count: <b>${productCount}</b></td></tr>
        </table>
      </form>
    </div>
</div>-->

<script language="JavaScript" type="text/javascript">
<!--//
document.findProduct.productId.focus();
//-->
</script>

<div id="productLookupList" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
    <li class="h3">Product List - Count: <b>${productCount}</b></li>
  </ul>
  <br class="clear"/>
</div>
<div class="screenlet-body">
    <table class="basic-table hover-bar" cellspacing='0'>
      <tr class="header-row">
        <td width="10%">Product Image</td>
        <td width="10%">Product ID</td>
        <td width="10%">Status</td>
        <td width="6%">Quantity On Hand (QOH)</td>
        <td width="6%">Available to Promise (ATP)</td>
        <td width="6%">Sold Qty</td>
        <td width="6%">Location</td>
        <td width="6%">Active Listing</td>
      </tr>
      <#assign alt_row = false>
      <#list productSearchList as productList>
      	<tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
            <td>
                <#assign hyphenCount = Static["org.apache.commons.lang.StringUtils"].countMatches(productList.productId, "-")>
                    <#assign parentSku = productList.productId?substring(0,productList.productId?index_of("-", productList.productId?index_of("-") + 1))>
                    <#if hyphenCount == 3>
                        <#assign childSku = productList.productId?substring(0,productList.productId?index_of("-",productList.productId?index_of("-",productList.productId?index_of("-") + 1)+1))>
                    <#elseif hyphenCount == 2>
                        <#assign childSku = productList.productId>
                    <#else>
                        <#assign childSku = productList.productId>
                    </#if>
                    <a href="http://images.bellyanna.com/${parentSku}/${childSku}/${childSku}.jpg" onclick="window.open(this.href,'','height=480,width=640');return false;"><img alt="${productList.productId?if_exists}" height="160px" src="http://images.bellyanna.com/${parentSku}/${childSku}/${childSku}.jpg"/></a>
            </td>
            <td><a href="<@ofbizUrl>EditProduct?productId=${productList.productId}</@ofbizUrl>" target="_blank" class="buttontext">${productList.productId}</a></td>
            <td>
                <#if productList.discontinueDate?has_content>
                    <font color=red>Discontinued on ${productList.discontinueDate?date}</font>
                <#else>
                    Active
                </#if>
            </td>
            <td>${productList.qoh}</td>
            <td>${productList.atp}</td>
            <td>${productList.soldQty}</td>
            <td>
                <#assign locations = delegator.findByAnd("ProductFacilityLocation", Static["org.ofbiz.base.util.UtilMisc"].toMap("productId", productList.productId), null, false)>
                <#if locations?has_content>
                    <#list locations?sort_by("locationSeqId") as location>
                        ${location.locationSeqId} <br />
                    </#list>
                <#else><font color=red>No Locations set!</font>
                </#if>
            </td>
            <td><a href="<@ofbizUrl>productEbayActiveListing?productId=${productList.productId}</@ofbizUrl>" target="_blank" class="buttontext">eBay Listing</a></td>
        </tr>
        <#assign alt_row = !alt_row>
      </#list>
    </table>
    
  </div>
</div>