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
<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">SKU Search</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
        <form action="<@ofbizUrl>productBasicInfoMini</@ofbizUrl>" method="post" name="productBasicInfoForm" id="productBasicInfoForm">
            <input type="hidden" name="language" value="en"/>
            <input type="hidden" name="platform" value="EBAY"/>
            <table cellspacing="0" class="basic-table">
                <tbody>
                    <tr>
                        <td class="label">Product ID</td>
                        <td>
                            <input type="text" name="productId" size="32" value="${productId?if_exists}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td>
                            <input type="submit" value="Search" class="smallSubmit"/>
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
        <br />

        <#if productId?has_content>
            <form action="<@ofbizUrl>updateProductBasicInfo</@ofbizUrl>" method="post" name="updateProductBasicInfoForm" id="updateProductBasicInfoForm">
                <input type="hidden" name="language" value="${language}"/>
                <table cellspacing="0" class="basic-table">
                    <tbody>
                        <tr>
                            <td colspan=2>
                                <table cellspacing="0" class="basic-table">
                                    <tbody>
                                        <tr>
                                            <td class="label">Title1 (${titleLength})</td>
                                            <td>
                                                <input type="text" size="80" value="${title}"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="label">Title2 (${smtTitleLength})</td>
                                            <td>
                                                <input type="text" size="130" value="${smtTitle}"/>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        <tr>
                            <td>
                                <table cellspacing="0" class="basic-table">
                                    <tbody>
                                        <tr>
                                            <td class="label">Content</td>
                                            <td>
                                                <textarea cols="80" rows="15" maxlength="5000" name="descriptionContent"><#if featureArray??>
Features:
<#list featureArray as feature>
<#assign featureStr = Static["org.apache.commons.lang.StringEscapeUtils"].unescapeXml(feature)>
  - ${featureStr}
</#list>

Specifications:
<#list specificationArray as specification>
<#assign specificationStr = Static["org.apache.commons.lang.StringEscapeUtils"].unescapeXml(specification)>
  - ${specificationStr}
</#list>

Package Includes:
<#list packageArray as package>
<#assign packageStr = Static["org.apache.commons.lang.StringEscapeUtils"].unescapeXml(package)>
  - ${packageStr}
</#list></#if></textarea>
                                            </td>
                                        </tr>
                                        <#if hasUpdatePermission>
                                            <tr>
                                                <td class="label">Feature</td>
                                                <td>
                                                    <textarea cols="80" rows="5" maxlength="5000" name="feature"><#if feature??>${feature.content!}</#if></textarea>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="label">Specification</td>
                                                <td>
                                                    <textarea cols="80" rows="5" maxlength="5000" name="specification"><#if specification??>${specification.content!}</#if></textarea>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="label">Package Includes</td>
                                                <td>
                                                    <textarea cols="80" rows="3" maxlength="5000" name="packageIncludes"><#if packageIncludes??>${packageIncludes.content!}</#if></textarea>
                                                </td>
                                            </tr>
                                        </#if>
                                    </tbody>
                                </table>
                            </td>
                            <td>
                                <table cellspacing="0" class="hover-bar">
                                    <tbody>
                                        <tr class="withBorder">
                                            <td class="label withBorder">Main Keyword</td>
                                            <td class="withBorder">
                                                <#list mainKeywordList as mainKeyword>
                                                    <#if title?contains(mainKeyword.keyword)><font color="red"><i>${mainKeyword.keyword}</i></font><#else>${mainKeyword.keyword}</#if> <br />
                                                </#list>
                                            </td>
                                        </tr>
                                        <tr class="withBorder">
                                            <td class="label withBorder">Secondary Keyword</td>
                                            <td class="withBorder">
                                                <#list secondaryKeywordList as secondaryKeyword>
                                                    <#if title?contains(secondaryKeyword.keyword)><font color="red"><i>${secondaryKeyword.keyword}</i></font><#else>${secondaryKeyword.keyword}</#if> <br />
                                                </#list>
                                            </td>
                                        </tr>
                                        <tr class="withBorder">
                                            <td class="label withBorder">Tertiary Keyword</td>
                                            <td class="withBorder">
                                                <#list tertiaryKeywordList as tertiaryKeyword>
                                                    <#if title?contains(tertiaryKeyword.keyword)><font color="red"><i>${tertiaryKeyword.keyword}</i></font><#else>${tertiaryKeyword.keyword}</#if> <br />
                                                </#list>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align=center>
                                <#if hasUpdatePermission>
                                    <input type="submit" value="Update" class="smallSubmit"/>
                                </#if>
                                <a href="javascript:void(0)" onclick="window.close();" class="smallSubmit">Cancel/Close</a>
                            </td>
                            <td>
                                &nbsp;
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </#if>
    </div>
</div>