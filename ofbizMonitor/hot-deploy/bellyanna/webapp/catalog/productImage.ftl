

<div id="productImage" class="screenlet">

<div class="screenlet-body">
  <#if product??>
    <#assign hyphenCount = Static["org.apache.commons.lang.StringUtils"].countMatches(product.productId, "-")>
    <#assign parentSku = product.productId?substring(0,product.productId?index_of("-", product.productId?index_of("-") + 1))>
    <#if hyphenCount == 3>
      <#assign childSku = product.productId?substring(0,product.productId?index_of("-",product.productId?index_of("-",product.productId?index_of("-") + 1)+1))>
    <#elseif hyphenCount == 2>
      <#assign childSku = product.productId>
    <#else>
      <#assign childSku = product.productId>
    </#if>
    <a href="http://images.bellyanna.com/${parentSku}/${childSku}/${childSku}.jpg" onclick="window.open(this.href,'','height=480,width=640');return false;"><img alt="${product.productId?if_exists}" height="160px" src="http://images.bellyanna.com/${parentSku}/${childSku}/${childSku}.jpg"/></a>
  </#if>
    
  </div>
</div>