
<div id="targetList" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Target Count: <b><#if targetCount?has_content>${targetCount}</#if></b></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
        <form action="<@ofbizUrl>target</@ofbizUrl>" method="post" name="adminTargetForm" id="adminTargetForm">
            <#if isAdmin>
                <select name="userLoginIdInput">
                    <option value="ALL">All</option>
                    <option value=""></option>
                    <#list userLoginList?sort_by("groupName") as userLoginGv>
                        <option value="${userLoginGv.userLoginId}" <#if userLoginIdInput = userLoginGv.userLoginId>SELECTED</#if>>${userLoginGv.groupName?if_exists} - ${userLoginGv.userLoginId?capitalize}</option>
                    </#list>
                </select>
                <br />
            </#if>
            <select name="period">
                <option value="ALL">All</option>
                <option value=""></option>
                <#assign monthList = delegator.findByAnd("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumTypeId" , "PERIOD"), Static["org.ofbiz.base.util.UtilMisc"].toList("sequenceId"), false)>
                <#list monthList?sort_by("sequenceId") as singleMonthList>
                    <option value="${singleMonthList.enumId}" <#if singleMonthList.enumId = currentMonth>SELECTED</#if>>${singleMonthList.description}</option>
                </#list>
            </select>
            <br />
            <input type="submit" value="Search" class="smallSubmit"/>
        </form>
        <br />
        <a class="buttontext" href="<@ofbizUrl>editTarget</@ofbizUrl>" onclick="window.open(this.href,'','height=750,width=1024,scrollbars=1');return false;">
            Create New Target
        </a>

        <table class="basic-table hover-bar" cellspacing='10'>
            <tr class="header-row" style="white-space: nowrap;">
                <td>Month</td>
                <td>UserLogin</td>
                <td>Target</td>
                <td>Actions</td>
            </tr>

            <#if gudaoTargetList?has_content>
                <#list gudaoTargetList as gudaoTarget>
                    <tr>
                        <td>
                            <a class="buttontext" href="<@ofbizUrl>editTarget?targetId=${gudaoTarget.targetId}</@ofbizUrl>" onclick="window.open(this.href,'','height=750,width=1024,scrollbars=1');return false;">
                                Edit
                            </a>&nbsp;
                            ${gudaoTarget.period}
                        </td>
                        <td>${gudaoTarget.userLoginId!}</td>
                        <td>${gudaoTarget.target!}</td>
                        <td>
                            <#if gudaoTarget.hasAction>
                                <#list gudaoTarget.actionLists as action>
                                    <table>
                                        <tr>
                                            <td>
                                                <table class="hover-bar" cellspacing="10" style="<#if action.priority = "HIGH">border-width: 2px;border-style: dotted;border-color: red;<#else>border-width: 1px;border-style: dotted;border-color: black;</#if>border-spacing: 20px 0;">
                                                    <tr class="header-row">
                                                        <td><span>${action.actionBy}</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span>${action.statusId}</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span><#if action.dueDate?has_content>${action.dueDate}</#if></span></td>
                                                    </tr>
                                                    <#if action.description?has_content><tr style="word-break : break-all;"><td>${action.description}</td></tr></#if>
                                                    <#if action.notes?has_content><tr><td>${action.notes}</td></tr></#if>
                                                </table>
                                            </td>
                                            <td>
                                                <form action="<@ofbizUrl>deleteGudaoAction</@ofbizUrl>" method="post" name="deleteGudaoAction" id="deleteGudaoAction">
                                                    <input type="hidden" name="targetId" value="${action.targetId!}">
                                                    <input type="hidden" name="seqId" value="${action.seqId!}">
                                                    <input type="hidden" name="userLoginIdInput" value="${userLoginIdInput!}">
                                                    <input type="hidden" name="period" value="${currentMonth!}">
                                                    <input type="submit" value="Delete" class="smallSubmit"/>
                                                </form>
                                            </td>
                                        </tr>
                                    </table>
                                    <br />
                                </#list>
                            <#else>
                                <form action="<@ofbizUrl>deleteGudaoTarget</@ofbizUrl>" method="post" name="deleteGudaoTarget" id="deleteGudaoTarget">
                                    <input type="hidden" name="targetId" value="${gudaoTarget.targetId!}">
                                    <input type="hidden" name="userLoginIdInput" value="${userLoginIdInput!}">
                                    <input type="hidden" name="period" value="${currentMonth!}">
                                    <input type="submit" value="Delete" class="smallSubmit"/>
                                </form>
                            </#if>
                        </td>
                    </tr>
                </#list>
            </#if>
        </table>
    </div>
</div>



