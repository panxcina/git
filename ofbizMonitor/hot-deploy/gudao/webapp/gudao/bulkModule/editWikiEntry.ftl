<div id="editWikiEntry" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Edit Wiki Entry</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
        <form action="<@ofbizUrl><#if wikiEntry??>updateWikiEntry<#else>createWikiEntry</#if></@ofbizUrl>" method="post" name="editWikiEntry" id="editWikiEntry">
            <input type="hidden" name="wikiId" value="${wikiId!}">
            <input type="hidden" name="parentId" value="${parentId!}">
            <table cellspacing="0">
                <tr>
                    <td class="label" >Wiki Id</td>
                    <td>
                        ${wikiId!}
                    </td>
                </tr>
                <tr>
                    <td class="label">Category</td>
                    <td colspan=2>
                        <input type="text" name="category" size="120" maxlength="255" value="${category!}"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Title</td>
                    <td colspan=2>
                        <input type="text" name="title" size="120" maxlength="255" value="<#if wikiEntry??>${wikiEntry.title!}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Short Description</td>
                    <td>
                        <textarea cols="120" rows="3" maxlength="5000" name="shortDescription" id="shortDescription"><#if wikiEntry??>${wikiEntry.shortDescription?if_exists}</#if></textarea>
                    </td>
                </tr>
                <tr>
                    <td class="label">Description</td>
                    <td>
                        <textarea cols="120" rows="3" maxlength="5000" name="description" id="description"><#if wikiEntry??>${wikiEntry.description?if_exists}</#if></textarea>
                    </td>
                </tr>
                <tr>
                    <td class="label" >Sequence</td>
                    <td>
                        <input type="number" name="sequence" size="15" maxlength="255" value="<#if wikiEntry??>${wikiEntry.sequence!}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td class="label" >Status</td>
                    <td>
                        <select name="statusId">
                            <option value="ACTIVE">ACTIVE</option>
                            <option value="INACTIVE">INACTIVE</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="label" ></td>
                    <td><input type="submit" value="<#if wikiEntry??>Update<#else>Create</#if>" class="smallSubmit"/></td>
                </tr>
            </table>
        </form>
        <#if wikiEntry??>
            <table>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <form action="<@ofbizUrl>deleteWikiEntry</@ofbizUrl>" method="post" name="deleteWikiEntry" id="deleteWikiEntry" onsubmit="return checkscript()">
                            <input type="hidden" name="wikiId" value="${wikiId!}">
                            <input type="submit" value="Delete" class="smallSubmit"/>
                        </form>
                    </td>
                </tr>
            </table>
        </#if>
    </div>
</div>

<script language="JavaScript" type="text/javascript">
    function checkscript() {
        var checkProcess = false;
        if(confirm('Are you sure you want to delete this entry?')) {
            checkProcess = true;
        } else {
            checkProcess = false;
        }
        return checkProcess;
    }
</script>