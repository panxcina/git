<#-- product master -->
<style type="text/css">
table .border   {
    text-align:center;
    border: 1px solid #333333;
    padding: 0px 0px 0px 0px;
    margin-left:  0.0em;
    padding: 0.4em 0.6em 0.4em 0.6em;
}

tr  td .cellborder  {
    border: 1px solid #333333;
    line-height: 2.9em;
    display:table-cell;
    padding: 0px 10px 0px 10px;
}
</style>

<#list productSearchList as productSearch>
    <div id="productLookupList" class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3"><b>SKU: ${productSearch.productId}</b></li>
                <li class="h3">&nbsp;</li>
                <li class="h3"><b>${productSearch.chineseName}</b></li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body">
            <table>
                <tr>
                    <td><b>${uiLabelMap.ownerGroup}: ${productSearch.ownerGroup}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${uiLabelMap.statusId}: ${productSearch.statusId}<b></td>
                </tr>
                <tr>
                    <td>
                        <table>
                            <tr>
                                <#list productSearch.groupList as groupList>
                                    <td>
                                        <table>
                                            <tr><td class="border"><b>${groupList.groupName}</b></td></tr>
                                            <tr>
                                                <td>
                                                    <table>
                                                        <#list groupList.groupPlatformList as platform>
                                                            <tr>
                                                                <td class="border">${platform.platform?if_exists}</td>
                                                                <td class="border">${platform.statusId?if_exists}</td>
                                                            </tr>
                                                        </#list>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>&nbsp;</td>
                                </#list>
                            </td>
                        </table>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <br class="clear"/>
</#list>