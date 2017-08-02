
<div id="Main" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">${uiLabelMap.pmAvailableValueList}</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
        <table cellspacing="10" border="0">
            <tbody>
                <tr>
                    <td>
                        <table class="hover-bar withBorder" cellspacing="10">
                            <tbody>
                                <tr align=center><td class="label">${uiLabelMap.productStatusList}</td></tr>
                                <#list statusList as status>
                                    <tr align=center class="withBorder">
                                        <td class="withBorder">
                                            ${status.enumCode}
                                        </td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    </td>
                    <td>&nbsp;</td>
                    <td>
                        <table class="hover-bar withBorder" cellspacing="10">
                            <tbody>
                                <tr align=center><td class="label">${uiLabelMap.platformStatusList}</td></tr>
                                <#list platformStatusList as status>
                                    <tr align=center class="withBorder">
                                        <td class="withBorder">
                                            ${status.enumCode}
                                        </td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    </td>
                    <td>&nbsp;</td>
                    <td>
                        <table class="hover-bar withBorder" cellspacing="10">
                            <tbody>
                                <tr align=center><td class="label">${uiLabelMap.productGroupList}</td></tr>
                                <#list productGroupList as productGroup>
                                    <tr align=center class="withBorder">
                                        <td class="withBorder">
                                            ${productGroup.enumCode}
                                        </td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    </td>
                    <td>&nbsp;</td>
                    <td>
                        <table class="hover-bar withBorder" cellspacing="10">
                            <tbody>
                                <tr align=center><td class="label">${uiLabelMap.productTypeList}</td></tr>
                                <#list productTypeList as productType>
                                    <tr align=center class="withBorder">
                                        <td class="withBorder">
                                            ${productType.enumCode}
                                        </td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>