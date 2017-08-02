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
        <tr class="header-row" style="white-space: nowrap;">
            <td align="center" width="160px" style="border:1px solid black;">Factor</td>
            <td align="center" width="100px" style="border:1px solid black;">Listing Count</td>
            <td align="center" width="100px" style="border:1px solid black;">GMS</td>
            <td align="center" width="100px" style="border:1px solid black;">Uploaded By</td>
        </tr>

        <tr>
            <td align="center" width="160px" style="border:1px solid black;">
                <div id="USListingCountFactor">
                    <a class="linkCustom" onclick="javascript:toggleScreenlet('USListingCountFactor', 'USListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
                        <div>
                            <ul>
                                <li class="collapsed">eBay US</li>
                            </ul>
                        </div>
                    </a>
                    <div id="USListingCountHistory" style="display: none;">
                        <li>
                            <#list ebayUsDetail?sort_by("account") as usDetail>
                                <ul>
                                    ${usDetail.account} = ${usDetail.totalCount}
                                </u>
                            </#list>
                        </li>
                    </div>
                </div>
            </td>
            <td align="center" width="100px" style="border:1px solid black;">
                <a class="linkCustom" onclick="javascript:toggleScreenlet('USListingCountFactor', 'USListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
                    <div>
                        <ul>
                            <li class="collapsed"><#if ebayUs?has_content>${ebayUs.value}</#if></li>
                        </ul>
                    </div>
                </a>
            </td>
            <td align="center" width="100px" style="border:1px solid black;">
                <a class="linkCustom" onclick="javascript:toggleScreenlet('USListingCountFactor', 'USListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
                    <div>
                        <ul>
                            <li class="collapsed"><#if ebayUs?has_content>${ebayUs.gms}</#if></li>
                        </ul>
                    </div>
                </a>
            </td>
            <td align="center" width="100px" style="border:1px solid black;">
                <a class="linkCustom" onclick="javascript:toggleScreenlet('USListingCountFactor', 'USListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
                    <div>
                        <ul>
                            <li class="collapsed"><#if ebayUsUser?has_content>${ebayUsUser}<#else>&nbsp;</#if></li>
                        </ul>
                    </div>
                </a>
            </td>
        </tr>


<tr>
<td align="center" width="160px" style="border:1px solid black;">
<div id="DEListingCountFactor">
<a class="linkCustom" onclick="javascript:toggleScreenlet('DEListingCountFactor', 'DEListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed">eBay DE</li>
</ul>
</div>
</a>
<div id="DEListingCountHistory" style="display: none;">
<li>
<#list ebayDeDetail?sort_by("account") as deDetail>
<ul>
${deDetail.account} = ${deDetail.totalCount}
</u>
</#list>
</li>
</div>
</div>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('DEListingCountFactor', 'DEListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if ebayDe?has_content>${ebayDe.value}</#if></li>
</ul>
</div>
</a>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('DEListingCountFactor', 'DEListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if ebayDe?has_content>${ebayDe.gms}</#if></li>
</ul>
</div>
</a></td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('DEListingCountFactor', 'DEListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if ebayDeUser?has_content>${ebayDeUser}<#else>&nbsp;</#if></li>
</ul>
</div>
</a></td>
</tr>

<tr>
<td align="center" width="160px" style="border:1px solid black;">
<div id="FRListingCountFactor">
<a class="linkCustom" onclick="javascript:toggleScreenlet('FRListingCountFactor', 'FRListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed">eBay FR</li>
</ul>
</div>
</a>
<div id="FRListingCountHistory" style="display: none;">
<li>
<#list ebayFrDetail?sort_by("account") as frDetail>
<ul>
${frDetail.account} = ${frDetail.totalCount}
</u>
</#list>
</li>
</div>
</div>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('FRListingCountFactor', 'FRListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if ebayFr?has_content>${ebayFr.value}</#if></li>
</ul>
</div>
</a>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('FRListingCountFactor', 'FRListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if ebayFr?has_content>${ebayFr.gms}</#if></li>
</ul>
</div>
</a></td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('FRListingCountFactor', 'FRListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if ebayFrUser?has_content>${ebayFrUser}<#else>&nbsp;</#if></li>
</ul>
</div>
</a></td>
</tr>

<tr>
<td align="center" width="160px" style="border:1px solid black;">
<div id="smtUsListingCountFactor">
<a class="linkCustom" onclick="javascript:toggleScreenlet('smtUsListingCountFactor', 'smtUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed">SMT US</li>
</ul>
</div>
</a>
<div id="smtUsListingCountHistory" style="display: none;">
<li>
<#list smtUsDetail?sort_by("account") as smtDetail>
<ul>
${smtDetail.account} = ${smtDetail.totalCount}
</u>
</#list>
</li>
</div>
</div>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('smtUsListingCountFactor', 'smtUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if smtUs?has_content>${smtUs.value}</#if></li>
</ul>
</div>
</a>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('smtUsListingCountFactor', 'smtUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if smtUs?has_content>${smtUs.gms}</#if></li>
</ul>
</div>
</a></td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('smtUsListingCountFactor', 'smtUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if smtUsUser?has_content>${smtUsUser}<#else>&nbsp;</#if></li>
</ul>
</div>
</a></td>
</tr>

<tr>
<td align="center" width="160px" style="border:1px solid black;">
<div id="wishUsListingCountFactor">
<a class="linkCustom" onclick="javascript:toggleScreenlet('wishUsListingCountFactor', 'wishUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed">Wish US</li>
</ul>
</div>
</a>
<div id="wishUsListingCountHistory" style="display: none;">
<li>
<#list wishUsDetail?sort_by("account") as wishDetail>
<ul>
${wishDetail.account} = ${wishDetail.totalCount}
</u>
</#list>
</li>
</div>
</div>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('wishUsListingCountFactor', 'wishUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if wishUs?has_content>${wishUs.value}</#if></li>
</ul>
</div>
</a>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('wishUsListingCountFactor', 'wishUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if wishUs?has_content>${wishUs.gms}</#if></li>
</ul>
</div>
</a></td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('wishUsListingCountFactor', 'wishUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if wishUsUser?has_content>${wishUsUser}<#else>&nbsp;</#if></li>
</ul>
</div>
</a></td>
</tr>

<tr>
<td align="center" width="160px" style="border:1px solid black;">
<div id="amaUsListingCountFactor">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaUsListingCountFactor', 'amaUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed">Amazon US</li>
</ul>
</div>
</a>
<div id="amaUsListingCountHistory" style="display: none;">
<li>
<#list amaUsDetail?sort_by("account") as amUsDetail>
<ul>
${amUsDetail.account} = ${amUsDetail.totalCount}
</u>
</#list>
</li>
</div>
</div>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaUsListingCountFactor', 'amaUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaUs?has_content>${amaUs.value}</#if></li>
</ul>
</div>
</a>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaUsListingCountFactor', 'amaUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaUs?has_content>${amaUs.gms}</#if></li>
</ul>
</div>
</a></td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaUsListingCountFactor', 'amaUsListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaUsUser?has_content>${amaUsUser}<#else>&nbsp;</#if></li>
</ul>
</div>
</a></td>
</tr>

<tr>
<td align="center" width="160px" style="border:1px solid black;">
<div id="amaUkListingCountFactor">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaUkListingCountFactor', 'amaUkListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed">Amazon UK</li>
</ul>
</div>
</a>
<div id="amaUkListingCountHistory" style="display: none;">
<li>
<#list amaUkDetail?sort_by("account") as amUkDetail>
<ul>
${amUkDetail.account} = ${amUkDetail.totalCount}
</u>
</#list>
</li>
</div>
</div>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaUkListingCountFactor', 'amaUkListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaUk?has_content>${amaUk.value}</#if></li>
</ul>
</div>
</a>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaUkListingCountFactor', 'amaUkListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaUk?has_content>${amaUk.gms}</#if></li>
</ul>
</div>
</a></td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaUkListingCountFactor', 'amaUkListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaUkUser?has_content>${amaUkUser}<#else>&nbsp;</#if></li>
</ul>
</div>
</a></td>
</tr>

<tr>
<td align="center" width="160px" style="border:1px solid black;">
<div id="amaCaListingCountFactor">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaCaListingCountFactor', 'amaCaListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed">Amazon CA</li>
</ul>
</div>
</a>
<div id="amaCaListingCountHistory" style="display: none;">
<li>
<#list amaCaDetail?sort_by("account") as amCaDetail>
<ul>
${amCaDetail.account} = ${amCaDetail.totalCount}
</u>
</#list>
</li>
</div>
</div>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaCaListingCountFactor', 'amaCaListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaCa?has_content>${amaCa.value}</#if></li>
</ul>
</div>
</a>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaCaListingCountFactor', 'amaCaListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaCa?has_content>${amaCa.gms}</#if></li>
</ul>
</div>
</a></td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaCaListingCountFactor', 'amaCaListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaCaUser?has_content>${amaCaUser}<#else>&nbsp;</#if></li>
</ul>
</div>
</a></td>
</tr>

<tr>
<td align="center" width="160px" style="border:1px solid black;">
<div id="amaDeListingCountFactor">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaDeListingCountFactor', 'amaDeListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed">Amazon DE</li>
</ul>
</div>
</a>
<div id="amaDeListingCountHistory" style="display: none;">
<li>
<#list amaDeDetail?sort_by("account") as amDeDetail>
<ul>
${amDeDetail.account} = ${amDeDetail.totalCount}
</u>
</#list>
</li>
</div>
</div>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaDeListingCountFactor', 'amaDeListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaDe?has_content>${amaDe.value}</#if></li>
</ul>
</div>
</a>
</td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaDeListingCountFactor', 'amaDeListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaDe?has_content>${amaDe.gms}</#if></li>
</ul>
</div>
</a></td>
<td align="center" width="100px" style="border:1px solid black;">
<a class="linkCustom" onclick="javascript:toggleScreenlet('amaDeListingCountFactor', 'amaDeListingCountHistory', 'true', 'Expand', 'Collapse');" title="Expand">
<div>
<ul>
<li class="collapsed"><#if amaDeUser?has_content>${amaDeUser}<#else>&nbsp;</#if></li>
</ul>
</div>
</a></td>
</tr>

<tr style="white-space: nowrap;">
<td align="center" width="160px" style="border:1px solid black;">AMAZON Follow Count</td>
<td align="center" width="100px" style="border:1px solid black;"><#if amaFollow?has_content>${amaFollow.value}</#if></td>
<td align="center" width="100px" style="border:1px solid black;"><#if amaFollow?has_content>${amaFollow.gms}</#if></td>
<td align="center" width="100px" style="border:1px solid black;"><#if amaFollowUser?has_content>${amaFollowUser}</#if></td>
</tr>



    </table>

<div align="center">
<a href="javascript:void(0)" onclick="window.close();" class="buttontext">Close</a>
</div>

</div>
