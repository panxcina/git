

<div id="ExpressPicklist" class="screenlet">

<div class="screenlet-body">
  <#if picklistInfo??>
    <#list picklistInfo.picklistBinInfoList as picklistBinInfo>
      <#assign picklistBin = picklistBinInfo.picklistBin>
      <#assign orderId = picklistBinInfo.primaryOrderHeader.orderId>
      <h3>${orderId}</h3>
    </#list>
  </#if>
  </div>
</div>