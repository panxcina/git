<div id="targetDetail" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Target Detail</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
        <form action="<@ofbizUrl><#if gudaoTarget??>updateTarget<#else>createTarget</#if></@ofbizUrl>" method="post" name="editTargetForm" id="editTargetForm" onsubmit="return checkscript()">
            <input type="hidden" name="targetId" value="${targetId!}">
            <input type="hidden" name="userLoginId" value="${userLoginId!}">
            <table cellspacing="0">
                <tr>
                    <td class="label" >Period</td>
                    <td>
                        <select name="period">
                            <option value=""></option>
                            <#assign monthList = delegator.findByAnd("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumTypeId" , "PERIOD"), Static["org.ofbiz.base.util.UtilMisc"].toList("sequenceId"), false)>
                            <#list monthList?sort_by("sequenceId") as singleMonthList>
                                <option value="${singleMonthList.enumId}" <#if gudaoTarget?? && gudaoTarget.period?has_content && gudaoTarget.period = singleMonthList.enumId>SELECTED<#elseif !gudaoTarget?? &&singleMonthList.enumId = currentMonth>SELECTED</#if>>${singleMonthList.description}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td class="label">Target</td>
                    <td colspan=2>
                        <input type="text" name="target" size="120" maxlength="255" value="<#if gudaoTarget??>${gudaoTarget.target!}</#if>"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">Obstacles</td>
                    <td>
                        <textarea cols="120" rows="3" maxlength="5000" name="obstacles" id="obstacles"><#if gudaoTarget??>${gudaoTarget.obstacles?if_exists}</#if></textarea>
                    </td>
                </tr>
                <tr>
                    <td class="label">Notes</td>
                    <td>
                        <textarea cols="120" rows="3" maxlength="5000" name="notes" id="notes"><#if gudaoTarget??>${gudaoTarget.notes?if_exists}</#if></textarea>
                    </td>
                </tr>
                <tr>
                    <td><input type="submit" value="<#if gudaoTarget??>Update<#else>Create</#if>" class="smallSubmit"/></td>
                </tr>
            </table>
        </form>
    </div>
</div>

<#if actionList?has_content>
    <div id="actionList" class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3">Action List</li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body">
            <table class="basic-table hover-bar" cellspacing='10'>
                <tr class="header-row">
                    <td>Description</td>
                    <td>Action By</td>
                    <td>Due Date</td>
                    <td>Status</td>
                    <td>Priority</td>
                    <td>Created By</td>
                    <td>Created Date</td>
                    <td>Notes</td>
                </tr>
                <#list actionList as actionSingle>
                    <tr>
                        <td style="word-break : break-word;">
                            <a class="buttontext" href="<@ofbizUrl>editTarget?editAction=TRUE&targetId=${gudaoTarget.targetId}&seqId=${actionSingle.seqId}</@ofbizUrl>">
                                Edit
                            </a>&nbsp;
                            <#if actionSingle.description?has_content>${actionSingle.description}</#if>
                        </td>
                        <td style="white-space: nowrap;"><#if actionSingle.actionBy?has_content>${actionSingle.actionBy}</#if></td>
                        <td style="white-space: nowrap;"><#if actionSingle.dueDate?has_content>${actionSingle.dueDate}</#if></td>
                        <td style="white-space: nowrap;"><#if actionSingle.statusId?has_content>${actionSingle.statusId}</#if></td>
                        <td style="white-space: nowrap;"><#if actionSingle.priority?has_content>${actionSingle.priority}</#if></td>
                        <td style="white-space: nowrap;"><#if actionSingle.createdBy?has_content>${actionSingle.createdBy}</#if></td>
                        <td style="white-space: nowrap;"><#if actionSingle.createdDate?has_content>${actionSingle.createdDate}</#if></td>
                        <td style="word-break : break-word;"><#if actionSingle.notes?has_content>${actionSingle.notes}</#if></td>
                    </tr>
                </#list>
            </table>
        </div>
    </div>
</#if>




<#if gudaoTarget??>
    <div id="createNewAction" class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3">Create New Action</li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body">
            <form action="<@ofbizUrl><#if editAction??>updateAction<#else>createAction</#if></@ofbizUrl>" method="post" name="editActionForm" id="editActionForm" onsubmit="return checkscript()">
                <input type="hidden" name="targetId" value="<#if editAction??>${editActionTargetId}<#else>${gudaoTarget.targetId!}</#if>"/>
                <input type="hidden" name="seqId" value="<#if editAction??>${editActionSeqId}<#else>${nextSeqId!}</#if>"/>
                <input type="hidden" name="createdBy" value="<#if editAction??>${editActionCreatedBy}<#else>${userLoginId!}</#if>"/>
                <table>
                    <tr>
                        <td class="label">Action By</td>
                        <td>
                            <select name="actionBy" id="actionBy">
                                <option value=""></option>
                                <#assign groupList = delegator.findByAnd("PartyGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("groupNameLocal" , "GUDAO"), null, true)>
                                <#list groupList?sort_by("groupName") as group>
                                    <option value=${group.partyId} <#if editAction?? && editActionActionBy = group.partyId>SELECTED</#if>>${group.groupName}</option>
                                    <#assign userLoginList = delegator.findByAnd("UserLogin", Static["org.ofbiz.base.util.UtilMisc"].toMap("partyId" , group.partyId, "enabled", "Y"), null, true)>
                                    <#list userLoginList?sort_by("userLoginId") as userLogin>
                                        <#assign receiverUserLoginId = userLogin.userLoginId>
                                        <option value=${receiverUserLoginId} <#if editAction?? && editActionActionBy = receiverUserLoginId>SELECTED</#if>>${receiverUserLoginId}</option>
                                    </#list>
                                    <option value=""></option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Due Date</td>
                        <td nowrap="nowrap">
                            <@htmlTemplate.renderDateTimeField name="dueDate" event="" action="" value="${editActionDueDate?if_exists}" className="" alert="" title="Format: yyyy-MM-dd" size="25" maxlength="30" id="dueDate" dateType="date" shortDateInput=true timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">status</td>
                        <td>
                            <select name="statusId">
                                <option value="PENDING" <#if editAction?? && editActionStatusId = "PENDING">SELECTED</#if>>Pending</option>
                                <option value="IN_PROGRESS" <#if editAction?? && editActionStatusId = "IN_PROGRESS">SELECTED</#if>>In Progress</option>
                                <option value="COMPLETED" <#if editAction?? && editActionStatusId = "COMPLETED">SELECTED</#if>>Completed</option>
                                <option value="ABORTED" <#if editAction?? && editActionStatusId = "ABORTED">SELECTED</#if>>Aborted</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">priority</td>
                        <td>
                            <select name="priority">
                                <option value=""></option>
                                <option value="HIGH" <#if editAction?? && editActionPriority = "HIGH">SELECTED</#if>>High</option>
                                <option value="NORMAL" <#if editAction?? && editActionPriority = "NORMAL">SELECTED</#if>>Normal</option>
                                <option value="LOW" <#if editAction?? && editActionPriority = "LOW">SELECTED</#if>>Low</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Description</td>
                        <td>
                            <textarea cols="60" rows="5" maxlength="5000" name="description" id="description">${editActionDescription?if_exists}</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">Notes</td>
                        <td>
                            <textarea cols="60" rows="5" maxlength="5000" name="notes" id="description">${editActionNotes?if_exists}</textarea>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="<#if editAction??>Update<#else>Create</#if>" class="smallSubmit"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</#if>



<div align="center">
    <a href="javascript:void(0)" onclick="window.close();" class="buttontext">Close</a>
</div>


<script language="JavaScript" type="text/javascript">
function checkscript() {
var checkProcess = false;


if(document.getElementById('receiver').value) {
checkProcess = true;
} else {
alert('"接收人" 不能空白');
//return false;
}

if(document.getElementById('description').value) {
checkProcess = true;
} else {
alert('“请求内容” 不能空白');
//return false;
}

return checkProcess;
}
</script>
