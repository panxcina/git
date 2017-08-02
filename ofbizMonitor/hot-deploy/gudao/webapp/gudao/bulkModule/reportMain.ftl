<#-- Gudao Main page -->
<style type="text/css">
A.bigbutton,.bigbuttondisabled {
border-bottom: #999999 solid 0.1em;
border-top: #f7e893 solid 0.1em;
border-left: #f7e893 solid 0.1em;
border-right: #999999 solid 0.1em;
background-image: url(../images/button_whitegray.jpg);
background-repeat:repeat-x;
font-weight: bold;
font-size: 20px;
line-height: 2.9em;
margin-left:  0.0em;
padding: 0.4em 0.6em 0.4em 0.6em;
/*white-space: nowrap;*/
}

</style>

<div id="Main" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
<li class="h3">Report - Profit | Day Count: ${dayCount}</li>
  </ul>
  <br class="clear"/>
</div>
<div class="screenlet-body">

<#-- ==================================== All Platform ========================================= -->
<table class="basic-table" cellspacing='0' border='10'>
<tr>
<th>All Platform</th>
<th>销量</th>
<th>退款</th>
<th>平台费</th>
<th>产品成本</th>
<th>国内运费</th>
<th>国际运费</th>
<th>包材费</th>
<th>毛利</th>
</tr>
<#assign allGroupSalesTotal = 1>
<#assign allGroupRefund = 0>
<#assign allGroupPlatformFee = 0>
<#assign allGroupProductCostTotal = 0>
<#assign allGroupDomesticDeliveryFee = 0>
<#assign allGroupShippingCostTotal = 0>
<#assign allGroupTotalPackaging = 0>


<#list allPlatformSalesList?sort_by("ownerGroup") as platformSales>
<tr>
<td>${platformSales.ownerGroup}</td>
<td>${platformSales.salesTotal?string["0"]}</td>
<#assign allGroupSalesTotal = allGroupSalesTotal + platformSales.salesTotal>
<#assign refundPct = allRefundList.get(platformSales.ownerGroup) / platformSales.salesTotal * 100>
<td>${allRefundList.get(platformSales.ownerGroup)?string["0"]} (${refundPct?string["0"]}%)</td>
<#assign allGroupRefund = allGroupRefund + allRefundList.get(platformSales.ownerGroup)>
<#assign platformFee = platformSales.feeTotal * 1>
<#assign feePct = platformFee / platformSales.salesTotal * 100>
<td>${platformFee?string["0"]} (${feePct?string["0"]}%)</td>
<#assign allGroupPlatformFee = allGroupPlatformFee + platformFee>
<#assign productCostPct = platformSales.productCostTotal / platformSales.salesTotal * 100>
<td>${platformSales.productCostTotal?string["0"]} (${productCostPct?string["0"]}%)</td>
<#assign allGroupProductCostTotal = allGroupProductCostTotal + platformSales.productCostTotal>
<#assign domesticDeliveryFeePct = platformSales.domesticDeliveryFee / platformSales.salesTotal * 100>
<td>${platformSales.domesticDeliveryFee?string["0"]} (${domesticDeliveryFeePct?string["0"]}%)</td>
<#assign allGroupDomesticDeliveryFee = allGroupDomesticDeliveryFee + platformSales.domesticDeliveryFee>
<#assign shippingCostPct = platformSales.shippingCostTotal / platformSales.salesTotal * 100>
<td>${platformSales.shippingCostTotal?string["0"]} (${shippingCostPct?string["0"]}%)</td>
<#assign allGroupShippingCostTotal = allGroupShippingCostTotal + platformSales.shippingCostTotal>
<#assign totalPackagingPct = platformSales.totalPackaging / platformSales.salesTotal * 100>
<td>${platformSales.totalPackaging?string["0"]} (${totalPackagingPct?string["0"]}%)</td>
<#assign allGroupTotalPackaging = allGroupTotalPackaging + platformSales.totalPackaging>
<#assign profit = platformSales.salesTotal - allRefundList.get(platformSales.ownerGroup) - platformSales.feeTotal - platformSales.productCostTotal - platformSales.domesticDeliveryFee - platformSales.shippingCostTotal - platformSales.totalPackaging>
<#assign profitPct = 100 - refundPct - feePct - productCostPct - domesticDeliveryFeePct - shippingCostPct - totalPackagingPct>
<td>${profit?string["0"]} (${profitPct?string["0"]}%)</td>
</tr>
</#list>

<tr>
<td><b>TOTAL</b></td>
<td><b>${allGroupSalesTotal?string["0"]}</b></td>
<#assign allGroupRefundPct = allGroupRefund / allGroupSalesTotal * 100>
<td><b>${allGroupRefund?string["0"]} (${allGroupRefundPct?string["0"]}%)</b></td>
<#assign allGroupPlatformFeePct = allGroupPlatformFee / allGroupSalesTotal * 100>
<td><b>${allGroupPlatformFee?string["0"]} (${allGroupPlatformFeePct?string["0"]}%)</b></td>
<#assign allGroupProductCostTotalPct = allGroupProductCostTotal / allGroupSalesTotal * 100>
<td><b>${allGroupProductCostTotal?string["0"]} (${allGroupProductCostTotalPct?string["0"]}%)</b></td>
<#assign allGroupDomesticDeliveryFeePct = allGroupDomesticDeliveryFee / allGroupSalesTotal * 100>
<td><b>${allGroupDomesticDeliveryFee?string["0"]} (${allGroupDomesticDeliveryFeePct?string["0"]}%)</b></td>
<#assign allGroupShippingCostTotalPct = allGroupShippingCostTotal / allGroupSalesTotal * 100>
<td><b>${allGroupShippingCostTotal?string["0"]} (${allGroupShippingCostTotalPct?string["0"]}%)</b></td>
<#assign allGroupTotalPackagingPct = allGroupTotalPackaging / allGroupSalesTotal * 100>
<td><b>${allGroupTotalPackaging?string["0"]} (${allGroupTotalPackagingPct?string["0"]}%)</b></td>
<#assign allGroupProfit = allGroupSalesTotal - allGroupRefund - allGroupPlatformFee - allGroupProductCostTotal - allGroupDomesticDeliveryFee - allGroupShippingCostTotal - allGroupTotalPackaging>
<#assign allGroupProfitPct = 100 - allGroupRefundPct - allGroupPlatformFeePct - allGroupProductCostTotalPct - allGroupDomesticDeliveryFeePct - allGroupShippingCostTotalPct - allGroupTotalPackagingPct>
<td><b>${allGroupProfit?string["0"]} (${allGroupProfitPct?string["0"]}%)</b></td>
</tr>


</table>
<br />
<br />
<br />

<#-- ==================================== eBay ========================================= -->
<#assign platformSalesList = tableList.get("ebay")>
<table class="basic-table" cellspacing='0' border='10'>
<tr>
<th>eBay</th>
<th>销量</th>
<th>退款</th>
<th>平台费</th>
<th>产品成本</th>
<th>国内运费</th>
<th>国际运费</th>
<th>包材费</th>
<th>毛利</th>
</tr>
<#assign allGroupSalesTotal = 1>
<#assign allGroupRefund = 0>
<#assign allGroupPlatformFee = 0>
<#assign allGroupProductCostTotal = 0>
<#assign allGroupDomesticDeliveryFee = 0>
<#assign allGroupShippingCostTotal = 0>
<#assign allGroupTotalPackaging = 0>

<#list platformSalesList?sort_by("ownerGroup") as platformSales>
<tr>
<td>${platformSales.ownerGroup}</td>
<td>${platformSales.salesTotal?string["0"]}</td>
<#assign allGroupSalesTotal = allGroupSalesTotal + platformSales.salesTotal>
<#assign refundPct = allRefundList.get(platformSales.ownerGroup) / platformSales.salesTotal?default(1) * 100>
<td>${allRefundList.get(platformSales.ownerGroup)?string["0"]} (${refundPct?string["0"]}%)</td>
<#assign allGroupRefund = allGroupRefund + allRefundList.get(platformSales.ownerGroup)>
<#assign platformFee = platformSales.feeTotal * 1.01>
<#assign feePct = platformFee / platformSales.salesTotal?default(1) * 100>
<td>${platformFee?string["0"]} (${feePct?string["0"]}%)</td>
<#assign allGroupPlatformFee = allGroupPlatformFee + platformFee>
<#assign productCostPct = platformSales.productCostTotal / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.productCostTotal?string["0"]} (${productCostPct?string["0"]}%)</td>
<#assign allGroupProductCostTotal = allGroupProductCostTotal + platformSales.productCostTotal>
<#assign domesticDeliveryFeePct = platformSales.domesticDeliveryFee / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.domesticDeliveryFee?string["0"]} (${domesticDeliveryFeePct?string["0"]}%)</td>
<#assign allGroupDomesticDeliveryFee = allGroupDomesticDeliveryFee + platformSales.domesticDeliveryFee>
<#assign shippingCostPct = platformSales.shippingCostTotal / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.shippingCostTotal?string["0"]} (${shippingCostPct?string["0"]}%)</td>
<#assign allGroupShippingCostTotal = allGroupShippingCostTotal + platformSales.shippingCostTotal>
<#assign totalPackagingPct = platformSales.totalPackaging / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.totalPackaging?string["0"]} (${totalPackagingPct?string["0"]}%)</td>
<#assign allGroupTotalPackaging = allGroupTotalPackaging + platformSales.totalPackaging>
<#assign profit = platformSales.salesTotal - allRefundList.get(platformSales.ownerGroup) - platformSales.feeTotal - platformSales.productCostTotal - platformSales.domesticDeliveryFee - platformSales.shippingCostTotal - platformSales.totalPackaging>
<#assign profitPct = 100 - refundPct - feePct - productCostPct - domesticDeliveryFeePct - shippingCostPct - totalPackagingPct>
<td>${profit?string["0"]} (${profitPct?string["0"]}%)</td>
</tr>
</#list>

<tr>
<td><b>TOTAL</b></td>
<td><b>${allGroupSalesTotal?string["0"]}</b></td>
<#assign allGroupRefundPct = allGroupRefund / allGroupSalesTotal * 100>
<td><b>${allGroupRefund?string["0"]} (${allGroupRefundPct?string["0"]}%)</b></td>
<#assign allGroupPlatformFeePct = allGroupPlatformFee / allGroupSalesTotal * 100>
<td><b>${allGroupPlatformFee?string["0"]} (${allGroupPlatformFeePct?string["0"]}%)</b></td>
<#assign allGroupProductCostTotalPct = allGroupProductCostTotal / allGroupSalesTotal * 100>
<td><b>${allGroupProductCostTotal?string["0"]} (${allGroupProductCostTotalPct?string["0"]}%)</b></td>
<#assign allGroupDomesticDeliveryFeePct = allGroupDomesticDeliveryFee / allGroupSalesTotal * 100>
<td><b>${allGroupDomesticDeliveryFee?string["0"]} (${allGroupDomesticDeliveryFeePct?string["0"]}%)</b></td>
<#assign allGroupShippingCostTotalPct = allGroupShippingCostTotal / allGroupSalesTotal * 100>
<td><b>${allGroupShippingCostTotal?string["0"]} (${allGroupShippingCostTotalPct?string["0"]}%)</b></td>
<#assign allGroupTotalPackagingPct = allGroupTotalPackaging / allGroupSalesTotal * 100>
<td><b>${allGroupTotalPackaging?string["0"]} (${allGroupTotalPackagingPct?string["0"]}%)</b></td>
<#assign allGroupProfit = allGroupSalesTotal - allGroupRefund - allGroupPlatformFee - allGroupProductCostTotal - allGroupDomesticDeliveryFee - allGroupShippingCostTotal - allGroupTotalPackaging>
<#assign allGroupProfitPct = 100 - allGroupRefundPct - allGroupPlatformFeePct - allGroupProductCostTotalPct - allGroupDomesticDeliveryFeePct - allGroupShippingCostTotalPct - allGroupTotalPackagingPct>
<td><b>${allGroupProfit?string["0"]} (${allGroupProfitPct?string["0"]}%)</b></td>
</tr>
</table>
<br />
<br />
<br />



<#-- ==================================== SMT ========================================= -->
<#assign platformSalesList = tableList.get("aliexpres")>
<table class="basic-table" cellspacing='0' border='10'>
<tr>
<th>SMT</th>
<th>销量</th>
<th>退款</th>
<th>平台费</th>
<th>产品成本</th>
<th>国内运费</th>
<th>国际运费</th>
<th>包材费</th>
<th>毛利</th>
</tr>
<#assign allGroupSalesTotal = 1>
<#assign allGroupRefund = 0>
<#assign allGroupPlatformFee = 0>
<#assign allGroupProductCostTotal = 0>
<#assign allGroupDomesticDeliveryFee = 0>
<#assign allGroupShippingCostTotal = 0>
<#assign allGroupTotalPackaging = 0>

<#list platformSalesList?sort_by("ownerGroup") as platformSales>
<tr>
<td>${platformSales.ownerGroup}</td>
<td>${platformSales.salesTotal?string["0"]}</td>
<#assign allGroupSalesTotal = allGroupSalesTotal + platformSales.salesTotal>
<#assign refundPct = allRefundList.get(platformSales.ownerGroup) / platformSales.salesTotal?default(1) * 100>
<td>${allRefundList.get(platformSales.ownerGroup)?string["0"]} (${refundPct?string["0"]}%)</td>
<#assign allGroupRefund = allGroupRefund + allRefundList.get(platformSales.ownerGroup)>
<#assign platformFee = platformSales.feeTotal * 1.01>
<#assign feePct = platformFee / platformSales.salesTotal?default(1) * 100>
<td>${platformFee?string["0"]} (${feePct?string["0"]}%)</td>
<#assign allGroupPlatformFee = allGroupPlatformFee + platformFee>
<#assign productCostPct = platformSales.productCostTotal / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.productCostTotal?string["0"]} (${productCostPct?string["0"]}%)</td>
<#assign allGroupProductCostTotal = allGroupProductCostTotal + platformSales.productCostTotal>
<#assign domesticDeliveryFeePct = platformSales.domesticDeliveryFee / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.domesticDeliveryFee?string["0"]} (${domesticDeliveryFeePct?string["0"]}%)</td>
<#assign allGroupDomesticDeliveryFee = allGroupDomesticDeliveryFee + platformSales.domesticDeliveryFee>
<#assign shippingCostPct = platformSales.shippingCostTotal / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.shippingCostTotal?string["0"]} (${shippingCostPct?string["0"]}%)</td>
<#assign allGroupShippingCostTotal = allGroupShippingCostTotal + platformSales.shippingCostTotal>
<#assign totalPackagingPct = platformSales.totalPackaging / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.totalPackaging?string["0"]} (${totalPackagingPct?string["0"]}%)</td>
<#assign allGroupTotalPackaging = allGroupTotalPackaging + platformSales.totalPackaging>
<#assign profit = platformSales.salesTotal - allRefundList.get(platformSales.ownerGroup) - platformSales.feeTotal - platformSales.productCostTotal - platformSales.domesticDeliveryFee - platformSales.shippingCostTotal - platformSales.totalPackaging>
<#assign profitPct = 100 - refundPct - feePct - productCostPct - domesticDeliveryFeePct - shippingCostPct - totalPackagingPct>
<td>${profit?string["0"]} (${profitPct?string["0"]}%)</td>
</tr>
</#list>

<tr>
<td><b>TOTAL</b></td>
<td><b>${allGroupSalesTotal?string["0"]}</b></td>
<#assign allGroupRefundPct = allGroupRefund / allGroupSalesTotal * 100>
<td><b>${allGroupRefund?string["0"]} (${allGroupRefundPct?string["0"]}%)</b></td>
<#assign allGroupPlatformFeePct = allGroupPlatformFee / allGroupSalesTotal * 100>
<td><b>${allGroupPlatformFee?string["0"]} (${allGroupPlatformFeePct?string["0"]}%)</b></td>
<#assign allGroupProductCostTotalPct = allGroupProductCostTotal / allGroupSalesTotal * 100>
<td><b>${allGroupProductCostTotal?string["0"]} (${allGroupProductCostTotalPct?string["0"]}%)</b></td>
<#assign allGroupDomesticDeliveryFeePct = allGroupDomesticDeliveryFee / allGroupSalesTotal * 100>
<td><b>${allGroupDomesticDeliveryFee?string["0"]} (${allGroupDomesticDeliveryFeePct?string["0"]}%)</b></td>
<#assign allGroupShippingCostTotalPct = allGroupShippingCostTotal / allGroupSalesTotal * 100>
<td><b>${allGroupShippingCostTotal?string["0"]} (${allGroupShippingCostTotalPct?string["0"]}%)</b></td>
<#assign allGroupTotalPackagingPct = allGroupTotalPackaging / allGroupSalesTotal * 100>
<td><b>${allGroupTotalPackaging?string["0"]} (${allGroupTotalPackagingPct?string["0"]}%)</b></td>
<#assign allGroupProfit = allGroupSalesTotal - allGroupRefund - allGroupPlatformFee - allGroupProductCostTotal - allGroupDomesticDeliveryFee - allGroupShippingCostTotal - allGroupTotalPackaging>
<#assign allGroupProfitPct = 100 - allGroupRefundPct - allGroupPlatformFeePct - allGroupProductCostTotalPct - allGroupDomesticDeliveryFeePct - allGroupShippingCostTotalPct - allGroupTotalPackagingPct>
<td><b>${allGroupProfit?string["0"]} (${allGroupProfitPct?string["0"]}%)</b></td>
</tr>
</table>
<br />
<br />
<br />

<#-- ==================================== WISH ========================================= -->
<#assign platformSalesList = tableList.get("Wish")>
<table class="basic-table" cellspacing='0' border='10'>
<tr>
<th>Wish</th>
<th>销量</th>
<th>退款</th>
<th>平台费</th>
<th>产品成本</th>
<th>国内运费</th>
<th>国际运费</th>
<th>包材费</th>
<th>毛利</th>
</tr>
<#assign allGroupSalesTotal = 1>
<#assign allGroupRefund = 0>
<#assign allGroupPlatformFee = 0>
<#assign allGroupProductCostTotal = 0>
<#assign allGroupDomesticDeliveryFee = 0>
<#assign allGroupShippingCostTotal = 0>
<#assign allGroupTotalPackaging = 0>

<#list platformSalesList?sort_by("ownerGroup") as platformSales>
<tr>
<td>${platformSales.ownerGroup}</td>
<td>${platformSales.salesTotal?string["0"]}</td>
<#assign allGroupSalesTotal = allGroupSalesTotal + platformSales.salesTotal>
<#assign refundPct = allRefundList.get(platformSales.ownerGroup) / platformSales.salesTotal?default(1) * 100>
<td>${allRefundList.get(platformSales.ownerGroup)?string["0"]} (${refundPct?string["0"]}%)</td>
<#assign allGroupRefund = allGroupRefund + allRefundList.get(platformSales.ownerGroup)>
<#assign platformFee = platformSales.feeTotal * 1.01>
<#assign feePct = platformFee / platformSales.salesTotal?default(1) * 100>
<td>${platformFee?string["0"]} (${feePct?string["0"]}%)</td>
<#assign allGroupPlatformFee = allGroupPlatformFee + platformFee>
<#assign productCostPct = platformSales.productCostTotal / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.productCostTotal?string["0"]} (${productCostPct?string["0"]}%)</td>
<#assign allGroupProductCostTotal = allGroupProductCostTotal + platformSales.productCostTotal>
<#assign domesticDeliveryFeePct = platformSales.domesticDeliveryFee / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.domesticDeliveryFee?string["0"]} (${domesticDeliveryFeePct?string["0"]}%)</td>
<#assign allGroupDomesticDeliveryFee = allGroupDomesticDeliveryFee + platformSales.domesticDeliveryFee>
<#assign shippingCostPct = platformSales.shippingCostTotal / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.shippingCostTotal?string["0"]} (${shippingCostPct?string["0"]}%)</td>
<#assign allGroupShippingCostTotal = allGroupShippingCostTotal + platformSales.shippingCostTotal>
<#assign totalPackagingPct = platformSales.totalPackaging / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.totalPackaging?string["0"]} (${totalPackagingPct?string["0"]}%)</td>
<#assign allGroupTotalPackaging = allGroupTotalPackaging + platformSales.totalPackaging>
<#assign profit = platformSales.salesTotal - allRefundList.get(platformSales.ownerGroup) - platformSales.feeTotal - platformSales.productCostTotal - platformSales.domesticDeliveryFee - platformSales.shippingCostTotal - platformSales.totalPackaging>
<#assign profitPct = 100 - refundPct - feePct - productCostPct - domesticDeliveryFeePct - shippingCostPct - totalPackagingPct>
<td>${profit?string["0"]} (${profitPct?string["0"]}%)</td>
</tr>
</#list>

<tr>
<td><b>TOTAL</b></td>
<td><b>${allGroupSalesTotal?string["0"]}</b></td>
<#assign allGroupRefundPct = allGroupRefund / allGroupSalesTotal * 100>
<td><b>${allGroupRefund?string["0"]} (${allGroupRefundPct?string["0"]}%)</b></td>
<#assign allGroupPlatformFeePct = allGroupPlatformFee / allGroupSalesTotal * 100>
<td><b>${allGroupPlatformFee?string["0"]} (${allGroupPlatformFeePct?string["0"]}%)</b></td>
<#assign allGroupProductCostTotalPct = allGroupProductCostTotal / allGroupSalesTotal * 100>
<td><b>${allGroupProductCostTotal?string["0"]} (${allGroupProductCostTotalPct?string["0"]}%)</b></td>
<#assign allGroupDomesticDeliveryFeePct = allGroupDomesticDeliveryFee / allGroupSalesTotal * 100>
<td><b>${allGroupDomesticDeliveryFee?string["0"]} (${allGroupDomesticDeliveryFeePct?string["0"]}%)</b></td>
<#assign allGroupShippingCostTotalPct = allGroupShippingCostTotal / allGroupSalesTotal * 100>
<td><b>${allGroupShippingCostTotal?string["0"]} (${allGroupShippingCostTotalPct?string["0"]}%)</b></td>
<#assign allGroupTotalPackagingPct = allGroupTotalPackaging / allGroupSalesTotal * 100>
<td><b>${allGroupTotalPackaging?string["0"]} (${allGroupTotalPackagingPct?string["0"]}%)</b></td>
<#assign allGroupProfit = allGroupSalesTotal - allGroupRefund - allGroupPlatformFee - allGroupProductCostTotal - allGroupDomesticDeliveryFee - allGroupShippingCostTotal - allGroupTotalPackaging>
<#assign allGroupProfitPct = 100 - allGroupRefundPct - allGroupPlatformFeePct - allGroupProductCostTotalPct - allGroupDomesticDeliveryFeePct - allGroupShippingCostTotalPct - allGroupTotalPackagingPct>
<td><b>${allGroupProfit?string["0"]} (${allGroupProfitPct?string["0"]}%)</b></td>
</tr>
</table>
<br />
<br />
<br />


<#-- ==================================== Amazon ========================================= -->
<#assign platformSalesList = tableList.get("Amazon")>
<table class="basic-table" cellspacing='0' border='10'>
<tr>
<th>Amazon</th>
<th>销量</th>
<th>退款</th>
<th>平台费</th>
<th>产品成本</th>
<th>国内运费</th>
<th>国际运费</th>
<th>包材费</th>
<th>毛利</th>
</tr>
<#assign allGroupSalesTotal = 1>
<#assign allGroupRefund = 0>
<#assign allGroupPlatformFee = 0>
<#assign allGroupProductCostTotal = 0>
<#assign allGroupDomesticDeliveryFee = 0>
<#assign allGroupShippingCostTotal = 0>
<#assign allGroupTotalPackaging = 0>

<#list platformSalesList?sort_by("ownerGroup") as platformSales>
<tr>
<td>${platformSales.ownerGroup}</td>
<td>${platformSales.salesTotal?string["0"]}</td>
<#assign allGroupSalesTotal = allGroupSalesTotal + platformSales.salesTotal>
<#assign refundPct = allRefundList.get(platformSales.ownerGroup) / platformSales.salesTotal?default(1) * 100>
<td>${allRefundList.get(platformSales.ownerGroup)?string["0"]} (${refundPct?string["0"]}%)</td>
<#assign allGroupRefund = allGroupRefund + allRefundList.get(platformSales.ownerGroup)>
<#assign platformFee = platformSales.feeTotal * 1.01>
<#assign feePct = platformFee / platformSales.salesTotal?default(1) * 100>
<td>${platformFee?string["0"]} (${feePct?string["0"]}%)</td>
<#assign allGroupPlatformFee = allGroupPlatformFee + platformFee>
<#assign productCostPct = platformSales.productCostTotal / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.productCostTotal?string["0"]} (${productCostPct?string["0"]}%)</td>
<#assign allGroupProductCostTotal = allGroupProductCostTotal + platformSales.productCostTotal>
<#assign domesticDeliveryFeePct = platformSales.domesticDeliveryFee / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.domesticDeliveryFee?string["0"]} (${domesticDeliveryFeePct?string["0"]}%)</td>
<#assign allGroupDomesticDeliveryFee = allGroupDomesticDeliveryFee + platformSales.domesticDeliveryFee>
<#assign shippingCostPct = platformSales.shippingCostTotal / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.shippingCostTotal?string["0"]} (${shippingCostPct?string["0"]}%)</td>
<#assign allGroupShippingCostTotal = allGroupShippingCostTotal + platformSales.shippingCostTotal>
<#assign totalPackagingPct = platformSales.totalPackaging / platformSales.salesTotal?default(1) * 100>
<td>${platformSales.totalPackaging?string["0"]} (${totalPackagingPct?string["0"]}%)</td>
<#assign allGroupTotalPackaging = allGroupTotalPackaging + platformSales.totalPackaging>
<#assign profit = platformSales.salesTotal - allRefundList.get(platformSales.ownerGroup) - platformSales.feeTotal - platformSales.productCostTotal - platformSales.domesticDeliveryFee - platformSales.shippingCostTotal - platformSales.totalPackaging>
<#assign profitPct = 100 - refundPct - feePct - productCostPct - domesticDeliveryFeePct - shippingCostPct - totalPackagingPct>
<td>${profit?string["0"]} (${profitPct?string["0"]}%)</td>
</tr>
</#list>

<tr>
<td><b>TOTAL</b></td>
<td><b>${allGroupSalesTotal?string["0"]}</b></td>
<#assign allGroupRefundPct = allGroupRefund / allGroupSalesTotal * 100>
<td><b>${allGroupRefund?string["0"]} (${allGroupRefundPct?string["0"]}%)</b></td>
<#assign allGroupPlatformFeePct = allGroupPlatformFee / allGroupSalesTotal * 100>
<td><b>${allGroupPlatformFee?string["0"]} (${allGroupPlatformFeePct?string["0"]}%)</b></td>
<#assign allGroupProductCostTotalPct = allGroupProductCostTotal / allGroupSalesTotal * 100>
<td><b>${allGroupProductCostTotal?string["0"]} (${allGroupProductCostTotalPct?string["0"]}%)</b></td>
<#assign allGroupDomesticDeliveryFeePct = allGroupDomesticDeliveryFee / allGroupSalesTotal * 100>
<td><b>${allGroupDomesticDeliveryFee?string["0"]} (${allGroupDomesticDeliveryFeePct?string["0"]}%)</b></td>
<#assign allGroupShippingCostTotalPct = allGroupShippingCostTotal / allGroupSalesTotal * 100>
<td><b>${allGroupShippingCostTotal?string["0"]} (${allGroupShippingCostTotalPct?string["0"]}%)</b></td>
<#assign allGroupTotalPackagingPct = allGroupTotalPackaging / allGroupSalesTotal * 100>
<td><b>${allGroupTotalPackaging?string["0"]} (${allGroupTotalPackagingPct?string["0"]}%)</b></td>
<#assign allGroupProfit = allGroupSalesTotal - allGroupRefund - allGroupPlatformFee - allGroupProductCostTotal - allGroupDomesticDeliveryFee - allGroupShippingCostTotal - allGroupTotalPackaging>
<#assign allGroupProfitPct = 100 - allGroupRefundPct - allGroupPlatformFeePct - allGroupProductCostTotalPct - allGroupDomesticDeliveryFeePct - allGroupShippingCostTotalPct - allGroupTotalPackagingPct>
<td><b>${allGroupProfit?string["0"]} (${allGroupProfitPct?string["0"]}%)</b></td>
</tr>
</table>
<br />
<br />
<br />





