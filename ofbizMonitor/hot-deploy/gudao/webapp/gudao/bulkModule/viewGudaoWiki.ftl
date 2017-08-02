<style type="text/css">

.screenlet-title-bar {
background:url(../images/bkgd_header.gif) repeat;
min-height: 25px;
padding-top: 1px;
}
</style>

<#if isAdmin><form name="createWikiEntryNew" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
    <input type="submit" value="Create New" class="smallSubmit"/>
</form></#if>
<br class="clear"/>
<#list wikiList as wiki>
    <div id="Main" class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3"><#if wiki.wikiEntry.category?has_content>${wiki.wikiEntry.category!} | </#if>${wiki.wikiEntry.title}</li>
                <li>
                    <form name="createWikiEntryNew_${wiki.wikiEntry.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                        <input type="hidden" name="parentId" value="${wiki.wikiEntry.wikiId}"/>
                        <input type="hidden" name="category" value="${wiki.wikiEntry.category!}"/>
                        <input type="submit" value="Create New" class="smallSubmit"/>
                    </form>
                </li>
                <li>
                    <form name="editWikiEntry_${wiki.wikiEntry.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                        <input type="hidden" name="wikiId" value="${wiki.wikiEntry.wikiId}"/>
                        <input type="submit" value="Edit" class="smallSubmit"/>
                    </form>
                </li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body">
            <#if wiki.wikiEntry.shortDescription?has_content>
                <textarea cols="120" rows="2" readonly="readonly">${wiki.wikiEntry.shortDescription}</textarea>
                <br />
            </#if>
            <#if wiki.wikiEntry.description?has_content>
                <textarea cols="120" rows="5" readonly="readonly">${wiki.wikiEntry.description}</textarea>
                <br />
            </#if>
            <br />
            <table>
                <tr>
                    <td width=50>
                        &nbsp;
                    </td>
                    <td>
                        <#list wiki.wikiChildList as wikiChild>
                            <div id="Main" class="screenlet">
                                <div class="screenlet-title-bar">
                                    <ul>
                                        <li class="h3"><#if wikiChild.category?has_content>${wikiChild.category!} | </#if>${wikiChild.title}</li>
                                        <li>
                                            <form name="createWikiEntryNew_${wikiChild.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                                                <input type="hidden" name="parentId" value="${wikiChild.wikiId}"/>
                                                <input type="hidden" name="category" value="${wikiChild.category!}"/>
                                                <input type="submit" value="Create New" class="smallSubmit"/>
                                            </form>
                                        </li>
                                        <li>
                                            <form name="editWikiEntry_${wikiChild.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                                                <input type="hidden" name="wikiId" value="${wikiChild.wikiId}"/>
                                                <input type="submit" value="Edit" class="smallSubmit"/>
                                            </form>
                                        </li>

                                    </ul>
                                    <br class="clear"/>
                                </div>
                                <div class="screenlet-body">
                                    <#if wikiChild.shortDescription?has_content>
                                        <textarea cols="120" rows="2" readonly="readonly">${wikiChild.shortDescription}</textarea>
                                        <br />
                                    </#if>
                                    <#if wikiChild.description?has_content>
                                        <textarea cols="120" rows="5" readonly="readonly">${wikiChild.description}</textarea>
                                    </#if>
<!-- LOOP THIS -->
                                    <#assign wikiChildLoopList = delegator.findByAnd("WikiEntry",Static["org.ofbiz.base.util.UtilMisc"].toMap("parentId",wikiChild.wikiId), Static["org.ofbiz.base.util.UtilMisc"].toList("sequence"), false)>
                                    <#if wikiChildLoopList?has_content>
                                        <br />
                                        <br />
                                        <table>
                                            <tr>
                                                <td width=50>
                                                    &nbsp;
                                                </td>
                                                <td>
                                                    <#list wikiChildLoopList as wikiChildLoop>
                                                        <div id="Main" class="screenlet">
                                                            <div class="screenlet-title-bar">
                                                                <ul>
                                                                    <li class="h3"><#if wikiChildLoop.category?has_content>${wikiChildLoop.category!} | </#if>${wikiChildLoop.title}</li>
                                                                    <li>
                                                                        <form name="createWikiEntryNew_${wikiChildLoop.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                                                                            <input type="hidden" name="parentId" value="${wikiChildLoop.wikiId}"/>
                                                                            <input type="hidden" name="category" value="${wikiChildLoop.category!}"/>
                                                                            <input type="submit" value="Create New" class="smallSubmit"/>
                                                                        </form>
                                                                    </li>
                                                                    <li>
                                                                        <form name="editWikiEntry_${wikiChildLoop.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                                                                            <input type="hidden" name="wikiId" value="${wikiChildLoop.wikiId}"/>
                                                                            <input type="submit" value="Edit" class="smallSubmit"/>
                                                                        </form>
                                                                    </li>
                                                                </ul>
                                                                <br class="clear"/>
                                                            </div>
                                                            <div class="screenlet-body">
                                                                <#if wikiChildLoop.shortDescription?has_content>
                                                                    <textarea cols="120" rows="2" readonly="readonly">${wikiChildLoop.shortDescription}</textarea>
                                                                    <br />
                                                                </#if>
                                                                <#if wikiChildLoop.description?has_content>
                                                                    <textarea cols="120" rows="5" readonly="readonly">${wikiChildLoop.description}</textarea>
                                                                </#if>
                                                                <#assign wikiChildLoopList2 = delegator.findByAnd("WikiEntry",Static["org.ofbiz.base.util.UtilMisc"].toMap("parentId",wikiChildLoop.wikiId), Static["org.ofbiz.base.util.UtilMisc"].toList("sequence"), false)>
                                                                <#if wikiChildLoopList2?has_content>
                                                                    <br />
                                                                    <br />
                                                                    <table>
                                                                        <tr>
                                                                            <td width=50>
                                                                                &nbsp;
                                                                            </td>
                                                                            <td>
                                                                                <#list wikiChildLoopList2 as wikiChildLoop2>
                                                                                    <div id="Main" class="screenlet">
                                                                                        <div class="screenlet-title-bar">
                                                                                            <ul>
                                                                                                <li class="h3"><#if wikiChildLoop2.category?has_content>${wikiChildLoop2.category!} | </#if>${wikiChildLoop2.title}</li>
                                                                                                <li>
                                                                                                    <form name="createWikiEntryNew_${wikiChildLoop2.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                                                                                                        <input type="hidden" name="parentId" value="${wikiChildLoop2.wikiId}"/>
                                                                                                        <input type="hidden" name="category" value="${wikiChildLoop2.category!}"/>
                                                                                                        <input type="submit" value="Create New" class="smallSubmit"/>
                                                                                                    </form>
                                                                                                </li>
                                                                                                <li>
                                                                                                    <form name="editWikiEntry_${wikiChildLoop2.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                                                                                                        <input type="hidden" name="wikiId" value="${wikiChildLoop2.wikiId}"/>
                                                                                                        <input type="submit" value="Edit" class="smallSubmit"/>
                                                                                                    </form>
                                                                                                </li>
                                                                                            </ul>
                                                                                            <br class="clear"/>
                                                                                        </div>
                                                                                        <div class="screenlet-body">
                                                                                            <#if wikiChildLoop2.shortDescription?has_content>
                                                                                                <textarea cols="120" rows="2" readonly="readonly">${wikiChildLoop2.shortDescription}</textarea>
                                                                                                <br />
                                                                                            </#if>
                                                                                            <#if wikiChildLoop2.description?has_content>
                                                                                                <textarea cols="120" rows="5" readonly="readonly">${wikiChildLoop2.description}</textarea>
                                                                                            </#if>
                                                                                            <#assign wikiChildLoopList3 = delegator.findByAnd("WikiEntry",Static["org.ofbiz.base.util.UtilMisc"].toMap("parentId",wikiChildLoop2.wikiId), Static["org.ofbiz.base.util.UtilMisc"].toList("sequence"), false)>
                                                                                            <#if wikiChildLoopList3?has_content>
                                                                                                <br />
                                                                                                <br />
                                                                                                <table>
                                                                                                    <tr>
                                                                                                        <td width=50>
                                                                                                            &nbsp;
                                                                                                        </td>
                                                                                                        <td>
                                                                                                            <#list wikiChildLoopList3 as wikiChildLoop3>
                                                                                                                <div id="Main" class="screenlet">
                                                                                                                    <div class="screenlet-title-bar">
                                                                                                                        <ul>
                                                                                                                            <li class="h3"><#if wikiChildLoop3.category?has_content>${wikiChildLoop3.category!} | </#if>${wikiChildLoop3.title}</li>
                                                                                                                            <li>
                                                                                                                                <form name="createWikiEntryNew_${wikiChildLoop3.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                                                                                                                                    <input type="hidden" name="parentId" value="${wikiChildLoop3.wikiId}"/>
                                                                                                                                    <input type="hidden" name="category" value="${wikiChildLoop3.category!}"/>
                                                                                                                                    <input type="submit" value="Create New" class="smallSubmit"/>
                                                                                                                                </form>
                                                                                                                            </li>
                                                                                                                            <li>
                                                                                                                                <form name="editWikiEntry_${wikiChildLoop3.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                                                                                                                                    <input type="hidden" name="wikiId" value="${wikiChildLoop3.wikiId}"/>
                                                                                                                                    <input type="submit" value="Edit" class="smallSubmit"/>
                                                                                                                                </form>
                                                                                                                            </li>
                                                                                                                        </ul>
                                                                                                                        <br class="clear"/>
                                                                                                                    </div>
                                                                                                                    <div class="screenlet-body">
                                                                                                                        <#if wikiChildLoop3.shortDescription?has_content>
                                                                                                                            <textarea cols="120" rows="2" readonly="readonly">${wikiChildLoop3.shortDescription}</textarea>
                                                                                                                            <br />
                                                                                                                        </#if>
                                                                                                                        <#if wikiChildLoop3.description?has_content>
                                                                                                                            <textarea cols="120" rows="5" readonly="readonly">${wikiChildLoop3.description}</textarea>
                                                                                                                        </#if>
                                                                                                                        <#assign wikiChildLoopList4 = delegator.findByAnd("WikiEntry",Static["org.ofbiz.base.util.UtilMisc"].toMap("parentId",wikiChildLoop3.wikiId), Static["org.ofbiz.base.util.UtilMisc"].toList("sequence"), false)>
                                                                                                                        <#if wikiChildLoopList4?has_content>
                                                                                                                            <br />
                                                                                                                            <br />
                                                                                                                            <table>
                                                                                                                                <tr>
                                                                                                                                    <td width=50>
                                                                                                                                        &nbsp;
                                                                                                                                    </td>
                                                                                                                                    <td>
                                                                                                                                        <#list wikiChildLoopList4 as wikiChildLoop4>
                                                                                                                                            <div id="Main" class="screenlet">
                                                                                                                                                <div class="screenlet-title-bar">
                                                                                                                                                    <ul>
                                                                                                                                                        <li class="h3"><#if wikiChildLoop4.category?has_content>${wikiChildLoop4.category!} | </#if>${wikiChildLoop4.title}</li>
                                                                                                                                                        <li>
                                                                                                                                                            <form name="createWikiEntryNew_${wikiChildLoop4.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                                                                                                                                                                <input type="hidden" name="parentId" value="${wikiChildLoop4.wikiId}"/>
                                                                                                                                                                <input type="hidden" name="category" value="${wikiChildLoop4.category!}"/>
                                                                                                                                                                <input type="submit" value="Create New" class="smallSubmit"/>
                                                                                                                                                            </form>
                                                                                                                                                        </li>
                                                                                                                                                        <li>
                                                                                                                                                            <form name="editWikiEntry_${wikiChildLoop4.wikiId}" method="post" action="<@ofbizUrl>editWikiEntry</@ofbizUrl>">
                                                                                                                                                                <input type="hidden" name="wikiId" value="${wikiChildLoop4.wikiId}"/>
                                                                                                                                                                <input type="submit" value="Edit" class="smallSubmit"/>
                                                                                                                                                            </form>
                                                                                                                                                        </li>
                                                                                                                                                    </ul>
                                                                                                                                                    <br class="clear"/>
                                                                                                                                                </div>
                                                                                                                                                <div class="screenlet-body">
                                                                                                                                                    <#if wikiChildLoop4.shortDescription?has_content>
                                                                                                                                                        <textarea cols="120" rows="2" readonly="readonly">${wikiChildLoop4.shortDescription}</textarea>
                                                                                                                                                        <br />
                                                                                                                                                    </#if>
                                                                                                                                                    <#if wikiChildLoop4.description?has_content>
                                                                                                                                                        <textarea cols="120" rows="5" readonly="readonly">${wikiChildLoop4.description}</textarea>
                                                                                                                                                    </#if>
                                                                                                                                                </div>
                                                                                                                                            </div>
                                                                                                                                        </#list>
                                                                                                                                    </td>
                                                                                                                                </tr>
                                                                                                                            </table>
                                                                                                                        </#if>
                                                                                                                    </div>
                                                                                                                </div>
                                                                                                            </#list>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                                                                                            </#if>
                                                                                        </div>
                                                                                    </div>
                                                                                </#list>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </#if>
                                                            </div>
                                                        </div>
                                                    </#list>
                                                </td>
                                            </tr>
                                        </table>
                                    </#if>
<!-- LOOP THIS -->
                                </div>
                            </div>
                        </#list>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <br class="clear"/>
</#list>
