<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Filter</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="left">
        <form action="<@ofbizUrl>projectTask</@ofbizUrl>" method="post" name="project">
            <select name="filter">
                <option value="ALL">All</option>
                <option value="">--------</option>
                <option value="${userLoginId}">${userLoginId}</option>
                <option value="">--------</option>
                <#list userLoginList as userLoginGV>
                    <option value="${userLoginGV.assignedTo!}" <#if filterInput == userLoginGV.assignedTo>SELECTED</#if>>${userLoginGV.assignedTo!}</option>
                </#list>
            </select>
            <input type="submit" value="Apply" class="smallSubmit"/>
        </form>
        </div>
    </div>
</div>

<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3"><b>Pending Task List</b></li>
        </ul>
    </div>
    <div class="screenlet-body" align="left">
        <table class="basic-table hover-bar">
            <tr class="header-row" style="white-space: nowrap;">
                <td align="center">Action</td>
                <td align="center">Project</td>
                <td align="center">Task Title</td>
                <td align="center">Start Date</td>
                <td align="center">ETA</td>
                <td align="center">status</td>
                <td align="center">Notes</td>
                <td align="center">assigned To</td>
            </tr>

            <#list mercuryList as mercuryItem>
                <tr id="displayId_${mercuryItem.mercuryId}_${mercuryItem.sequenceId}">
                    <td align="center">
                        <a style="cursor:hand; cursor:pointer;" class="smallSubmit" onclick="javascript:toggleScreenlet('displayId_${mercuryItem.mercuryId}_${mercuryItem.sequenceId}', 'hideId_${mercuryItem.mercuryId}_${mercuryItem.sequenceId}', 'true', 'Expand', 'Collapse');" title="Edit">Edit</a>
                    </td>
                    <td align="center" style="white-space: nowrap;"><a class="buttontext" href="<@ofbizUrl>editProject?mercuryId=${mercuryItem.mercuryId}</@ofbizUrl>" onclick="window.open(this.href,'','height=768,width=1024,scrollbars=1');return false;">${mercuryItem.projectTitle!}</a></td>
                    <td align="center">${mercuryItem.taskTitle!}</td>
                    <td align="center">${mercuryItem.startDate!}</td>
                    <td align="center"><#if mercuryItem.overdue><font color=red></#if>${mercuryItem.completeDateEta!}<#if mercuryItem.overdue></font></#if></td>
                    <td align="center">${mercuryItem.taskStatus!}</td>
                    <td align="center">${mercuryItem.taskNotes!}</td>
                    <td align="center">${mercuryItem.assignedTo!}</td>
                </tr>
                <tr id="hideId_${mercuryItem.mercuryId}_${mercuryItem.sequenceId}" style="display: none;">
                    <form action="<@ofbizUrl>editMercuryItemTask</@ofbizUrl>" method="post" name="editMercuryItem_${mercuryItem.mercuryId}_${mercuryItem.sequenceId}">
                        <td align="center">
                            <input type="submit" value="Update" class="smallSubmit"/>
                        </td>
                        <input type="hidden" name="mercuryId" value="${mercuryItem.mercuryId}"/>
                        <input type="hidden" name="sequenceId" value="${mercuryItem.sequenceId}"/>
                        <td align="center"><#if mercuryItem.businessTreeParentName?has_content>${mercuryItem.businessTreeParentName} > </#if><#if mercuryItem.businessTreeName?has_content>${mercuryItem.businessTreeName} > </#if>${mercuryItem.projectTitle!}</td>
                        <td><input type="text" name="title" size="50" value="${mercuryItem.taskTitle!}"/></td>
                        <td nowrap="nowrap">
                            <@htmlTemplate.renderDateTimeField name="startDate" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="20" maxlength="25" id="editMercuryItemStartDate_${mercuryItem.mercuryId}_${mercuryItem.sequenceId}" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                        </td>
                        <td nowrap="nowrap">
                            <@htmlTemplate.renderDateTimeField name="completeDateEta" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="20" maxlength="25" id="editMercuryItemCompleteDateEta_${mercuryItem.mercuryId}_${mercuryItem.sequenceId}" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                        </td>
                        <td>
                            <select name="statusId">
                                <option value="PENDING" <#if mercuryItem.taskStatus=="PENDING">SELECTED</#if>>${uiLabelMap.CommonPending}</option>
                                <option value="IN_PROGRESS" <#if mercuryItem.taskStatus=="IN_PROGRESS">SELECTED</#if>>${uiLabelMap.CommonInProgress}</option>
                                <option value="COMPLETED" <#if mercuryItem.taskStatus=="COMPLETED">SELECTED</#if>>${uiLabelMap.CommonCompleted}</option>
                            </select>
                        </td>
                        <td><textarea cols="50" rows="2" maxlength="5000" name="notes">${mercuryItem.taskNotes!}</textarea></td>
                        <td>
                            <select name="assignedTo" id="assignedTo">
                                <option value=""></option>
                                <#assign groupList = delegator.findByAnd("PartyGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("groupNameLocal" , "GUDAO", "comments", "ACTIVE"), null, true)>
                                <#list groupList?sort_by("groupName") as group>
                                    <option value=${group.partyId} <#if mercuryItem.assignedTo = group.partyId>SELECTED</#if>>${group.groupName}</option>
                                    <#assign userLoginList = delegator.findByAnd("UserLogin", Static["org.ofbiz.base.util.UtilMisc"].toMap("partyId" , group.partyId, "enabled", "Y"), null, true)>
                                    <#list userLoginList?sort_by("userLoginId") as userLogin>
                                        <#assign receiverUserLoginId = userLogin.userLoginId>
                                        <option value=${receiverUserLoginId} <#if mercuryItem.assignedTo = receiverUserLoginId>SELECTED</#if>>${receiverUserLoginId}</option>
                                    </#list>
                                    <option value=""></option>
                                </#list>
                            </select>
                        </td>
                    </form>
                </tr>
                <tr><td><br class="clear"/></td></tr>
            </#list>
        </table>
    </div>
</div>
