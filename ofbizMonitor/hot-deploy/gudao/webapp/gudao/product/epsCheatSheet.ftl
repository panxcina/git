<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">EPS Cheat Sheet</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
        <form action="<@ofbizUrl>epsCheatSheet</@ofbizUrl>" method="post" name="epsCheatSheet" id="epsCheatSheet">
            <b>eBay Item ID</b> <input type="text" name="itemId" size="32" value="${itemId?if_exists}"/>
            <input type="submit" value="Calculate" class="smallSubmit"/>
        </form>
        <br />

        <#if itemId?has_content>


                <table cellspacing="0" class="basic-table">
                    <tbody>
                        <#if epsCheatSheetResult.notes??>
                            <tr>
                                <td>
                                    Notes
                                </td>
                                <td>
                                    <font color=red><b>${epsCheatSheetResult.notes}</b></font>
                                </td>
                            </tr>
<#else>
                        <tr>
                            <td >
                                <table cellspacing="0" class="basic-table">
                                    <tbody>
<tr>
<td class="label">Item ID</td>
<td><a href="http://www.ebay.com/itm/ws/eBayISAPI.dll?ViewItem&item=${epsCheatSheetResult.itemId}" title="${epsCheatSheetResult.itemId}" target="_blank" class="buttontext">${epsCheatSheetResult.itemId}</a></td>
</tr>

<tr>
<td class="label">Listing Type</td>
<td>${epsCheatSheetResult.listingType}</td>
</tr>

<tr>
<td class="label">Listing Status</td>
<td>${epsCheatSheetResult.sellingStatus} <#if epsCheatSheetResult.sellingStatus == "Completed"><font color=red>(已下架)</font></#if></td>
</tr>

<tr>
<td class="label">History Sold</td>
<td>${epsCheatSheetResult.rivalHistorySold}</td>
</tr>

<tr>
<td class="label">Current Price</td>
<td>${epsCheatSheetResult.rivalCurrency} ${epsCheatSheetResult.rivalCurrentPrice}</td>
</tr>

<tr>
<td class="label">Variation</td>
<td>${epsCheatSheetResult.variationCount}</td>
</tr>

<tr>
<td class="label">Listing Start Date</td>
<td>${epsCheatSheetResult.rivalListDetStartTime}</td>
</tr>

<tr>
<td class="label">Listing Lifetime</td>
<td>${epsCheatSheetResult.rivalListingLifetime} Day(s)</td>
</tr>

<tr>
<td class="label">Country Location</td>
<td>${epsCheatSheetResult.countryLocation}</td>
</tr>

<tr>
<td class="label">Daily Sales</td>
<td>${epsCheatSheetResult.rivalCurrency} ${epsCheatSheetResult.rivalDailySales?string["0.##"]}</td>
</tr>

<tr>
<td class="label">Sold Per Day</td>
<td>${epsCheatSheetResult.soldPerDay?string["0.##"]}</td>
</tr>

<tr>
<td class="label">EPS</td>
<td><#if epsCheatSheetResult.localSeller><font color=red></#if><b>${epsCheatSheetResult.eps?string["0.##"]}</b><#if epsCheatSheetResult.localSeller> <b>(LOCAL SELLER)</b></font></#if></td>
</tr>

</#if>

                                    </tbody>
                                </table>
                            </td>
                        <tr>
                            <td>

                            </td>
                            <td>

                            </td>
                        </tr>
                    </tbody>
                </table>

        </#if>
    </div>
</div>