<#-- product GPS MINI VIEW -->

<style type="text/css">
A:linkCustom  {color: #FFFF00; text-decoration: none}
A:hover   {cursor: pointer; color: #C0FFC0; background-color: lightslategray; text-decoration: none}
}
</style>

<div class="screenlet-body" align="center">
<div align="center">
<b>SKU: ${productId}</b>
</div>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>eBay US</td>
</tr>
<#list ebayUsHistory as ebayUs>
<tr>
<td>
${ebayUs.account} : ${ebayUs.count} : ${ebayUs.createdBy}
</td>
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>eBay UK</td>
</tr>
<#list ebayUkHistory as ebayUk>
<tr>
<td>
${ebayUk.account} : ${ebayUk.count} : ${ebayUk.createdBy}
</td>
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>eBay AU</td>
</tr>
<#list ebayAuHistory as ebayAu>
<tr>
<td>
${ebayAu.account} : ${ebayAu.count} : ${ebayAu.createdBy}
</td>
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>eBay DE</td>
</tr>
<#list ebayDeHistory as ebayDe>
<tr>
<td>
${ebayDe.account} : ${ebayDe.count} : ${ebayDe.createdBy}
</td>
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>eBay FR</td>
</tr>
<#list ebayFrHistory as ebayFr>
<tr>
<td>
${ebayFr.account} : ${ebayFr.count} : ${ebayFr.createdBy}
</td>
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>SMT US</td>
</tr>
<#list smtUsHistory as smtUs>
<tr>
<td>
${smtUs.account} : ${smtUs.count} : ${smtUs.createdBy}
</td>
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>Wish US</td>
</tr>
<#list wishUsHistory as wishUs>
<tr>
<td>
${wishUs.account} : ${wishUs.count} : ${wishUs.createdBy}
</td>
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>Amazon US</td>
</tr>
<#list amaUsHistory as amaUs>
<tr>
<td>
${amaUs.account} : ${amaUs.count} : ${amaUs.createdBy}
</td>
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>Amazon UK</td>
</tr>
<#list amaUkHistory as amaUk>
<tr>
<td>
${amaUk.account} : ${amaUk.count} : ${amaUk.createdBy}
</td>
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>Amazon CA</td>
</tr>
<#list amaCaHistory as amaCa>
<tr>
<td>
${amaCa.account} : ${amaCa.count} : ${amaCa.createdBy}
</td>
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>Amazon DE</td>
</tr>
<#list amaDeHistory as amaDe>
<tr>
<td>
${amaDe.account} : ${amaDe.count} : ${amaDe.createdBy}
</td>https://detail.tmall.com/item.htm?spm=a1z10.3-b.w4011-8437734912.116.N7SfHw&id=40895535600&rn=947ff6a92948dd8fcbaeb7b50a031ad4&abbucket=19
</tr>
</#list>
</table>

<table class="hover-bar" style="border:1px solid black;">
<tr class="row-header">
<td>Amazon Follow</td>
</tr>
<#list amaFollowHistory as amaFollow>
<tr>
<td>
${amaFollow.account} : ${amaFollow.count} : ${amaFollow.createdBy}
</td>
</tr>
</#list>
</table>

<div align="center">
<a class="buttontext" href="<@ofbizUrl>viewProductGmsMini?productId=${productId}</@ofbizUrl>">Back</a>
&nbsp;
<a href="javascript:void(0)" onclick="window.close();" class="buttontext">Close</a>
</div>

</div>
