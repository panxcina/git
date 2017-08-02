
<div id="wishBestSellingListMgr" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Wish Best Selling List Manager</b></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="left">
        <form action="<@ofbizUrl>bestSellingMonitorListMgr</@ofbizUrl>" method="post" name="bestSellingMonitorListMgr" id="bestSellingMonitorListMgr">
            <table>
                <tr>
                    <td>Account Name</td>
                    <td>
                        <select name="productStoreId">
                            <option value="">Choose Account</option>
                            <option value=""></option>
                            <#list productStoreList?sort_by("productStoreId") as productStoreGv>
                                <option value="${productStoreGv.productStoreId}" <#if productStoreId = productStoreGv.productStoreId>SELECTED</#if>>${productStoreGv.productStoreId?capitalize}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <input type="submit" value="Submit" class="smallSubmit"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<br class="clear"/>



    </div>
</div>
