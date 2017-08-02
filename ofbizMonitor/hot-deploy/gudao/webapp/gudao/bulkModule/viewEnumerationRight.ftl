<style type="text/css">
table.withBorder {
font-size:12px;
text-align:center;
border: 1px solid #333333;
border-collapse:collapse;
padding: 0px 0px 0px 0px;
line-height: 2.9em;
margin-left:  0.0em;
padding: 0.4em 0.6em 0.4em 0.6em;
font-weight: bold;
}
tr.withBorder  td.withBorder {
border: 1px solid #333333;
display:table-cell;
padding: 0px 10px 0px 10px;
}
</style>


<div id="Main" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Category List</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
        <table cellspacing="0" class="hover-bar">
            <tbody>
                <#list categoryList as category>
                    <tr class="withBorder">
                        <td class="label withBorder">
                            ${category.parentCategoryName}
                        </td>
                        <td class="withBorder">
                            <#list category.childrenCategoryList as childrenCategory>
                                ${childrenCategory.categoryName} <br />
                            </#list>
                        </td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>