<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="/images/project_demo/css/bootstrap.min.css" rel="stylesheet">
    <link href="/images/project_demo/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/images/project_demo/css/animate.css" rel="stylesheet">
    <link href="/images/project_demo/css/style_project_demo.css" rel="stylesheet">

</head>

<script language="JavaScript" type="text/javascript">
    function confirmDelete() {
        var checkProcess = false;
        if(confirm('Are you sure you want to delete project?')) {
            window.close();
            return true;

        } else {
            checkProcess = false;
        }
        return checkProcess;
    }

    function validateEta(mercuryId) {
        var etaValue = document.getElementById("add_completeDateEta_" + mercuryId).value;
        if (etaValue == null || etaValue == "") {
            alert("Please fill in ETA");
            return false;
        }
    }
</script>

<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3"><#if mercuryHeader??>Update Project ID ${mercuryHeader.mercuryId}<#else>Create New Project</#if></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="left">
        <form action="<@ofbizUrl><#if mercuryHeader??>updateGudaoProject<#else>createGudaoProject</#if></@ofbizUrl>" method="post" name="editGudaoProject" id="editGudaoProjectForm">
            <input type="hidden" name="mercuryId" value="<#if mercuryHeader??>${mercuryHeader.mercuryId}</#if>"/>
            <table>
                <tr><td>Project Name</td><td><input type="text" name="title" size="120" value="<#if mercuryHeader??>${mercuryHeader.title!}</#if>"/></td></tr>
                <tr>
                    <td style="vertical-align:middle">Description</td>
                    <td>
                        <textarea cols="120" rows="1" maxlength="5000" name="description"><#if mercuryHeader??>${mercuryHeader.description!}</#if></textarea>
                    </td>
                </tr>
                <tr>
                    <td>Category</td>
                    <td>
                        <select name="businessTreeId">
                            <option value="">Choose One</option>
                            <!--<#list profileList as profileList>
                                <option value="${profileList.parent.businessTreeId}|PROFILE" 
                                    <#if mercuryHeader??>
                                        <#if 
                                            mercuryHeader.businessTreeId==profileList.parent.businessTreeId && mercuryHeader.businessTreeType=="PROFILE">SELECTED
                                        </#if>
                                    </#if>
                                >${profileList.parent.title}</option>
                                <#list profileList.childList as childList>
                                    <option value="${childList.businessTreeId}|PROFILE" <#if mercuryHeader??><#if mercuryHeader.businessTreeId==childList.businessTreeId && mercuryHeader.businessTreeType=="PROFILE">SELECTED</#if></#if>>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${childList.title}</option>
                                </#list>
                                <option value=""></option>
                            </#list>-->
                            <#if isHeadquarter || isAdmin>
                                <option value="">===DEPARTMENT===</option>
                                <#list businessList as businessList>
                                    <option value="${businessList.parent.businessTreeId}|DEPARTMENT" <#if businessTreeId==businessList.parent.businessTreeId && businessTreeType=="DEPARTMENT">SELECTED</#if>>${businessList.parent.title}</option>
                                    <#list businessList.childList as childList>
                                        <option value="${childList.businessTreeId}|DEPARTMENT" <#if businessTreeId==childList.businessTreeId && businessTreeType=="DEPARTMENT">SELECTED</#if>>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${childList.title}</option>
                                    </#list>
                                </#list>
                            </#if>
                        </select>
                    </td>
                </tr>
                <!--<tr>
                    <td>Priority</td>
                    <td>
                        <select name="priority">
                            <option value="1">1 (High)</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5" SELECTED>5 (Normal)</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                            <option value="9">9 (Low)</option>
                        </select>
                    </td>
                </tr>-->
            </table>
            <input type="submit" value="<#if mercuryHeader??>Update<#else>Create</#if>" class="smallSubmit"/>
        </form>
        <form action="<@ofbizUrl>deleteGudaoProject</@ofbizUrl>" method="post" name="deleteGudaoProject" id="deleteGudaoProjectForm" onsubmit="return confirmDelete()">
            <input type="hidden" name="mercuryId" value="<#if mercuryHeader??>${mercuryHeader.mercuryId}</#if>"/>
            <input type="submit" value="Delete" class="smallSubmit"/>
        </form>
    </div>
</div>

<#if mercuryHeader??>
    <div class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3">Task List</li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body" align="left">
            <table class="basic-table hover-bar">
                <tr class="header-row">
                    <td width="10%">&nbsp;</td>
                    <td width="1%">step</td>
                    <td width="1%">&nbsp;</td>
                    <td width="5%">status</td>
                    <td width="1%">&nbsp;</td>
                    <td width="7%">start Date</td>
                    <td width="1%">&nbsp;</td>
                    <td width="7%">ETA</td>
                    <td width="1%">&nbsp;</td>
                    <td width="30%">Task Name</td>
                    <td width="1%">&nbsp;</td>
                    <td width="30%">notes</td>
                    <td width="1%">&nbsp;</td>
                    <td width="7%">Assigned To</td>
                </tr>
                    ${mercuryItemList}
                    <#list (((mercuryItemList?sort_by("sequenceId"))?reverse)?sort_by("completeDateEta"))?reverse as mercuryItem>
                    
                    <br />
                    <tr id="displayId_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}">
                        <td>
                            <#if (hasPermission || isAdmin)>
                                <table>
                                    <tr>
                                        <td>
                                            <a class="smallSubmit" onclick="javascript:toggleScreenlet('displayId_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}', 'hideId_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}', 'true', 'Expand', 'Collapse');" title="Edit">Edit</a>
                                        </td>
                                        <td>
                                            <#if mercuryItem.statusId == "COMPLETED">
                                                &nbsp;
                                            <#else>
                                                <form action="<@ofbizUrl>updateMercuryItemStatus</@ofbizUrl>" method="post" name="updateMercuryItemStatus_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}">
                                                    <input type="hidden" name="mercuryId" value="${mercuryHeader.mercuryId}"/>
                                                    <input type="hidden" name="sequenceId" value="${mercuryItem.sequenceId}"/>
                                                    <input type="hidden" name="statusId" value="${mercuryItem.statusId}"/>
                                                    <input type="submit" value="<#if mercuryItem.statusId == "IN_PROGRESS">Complete<#else>Work On it</#if>" class="smallSubmit"/>
                                                </form>
                                            </#if>
                                        </td>
                                    </tr>
                                </table>
                            </#if>
                        </td>
                        <td>${mercuryItem.sequenceId}</td>
                        <td>&nbsp;</td>
                        <td>${mercuryItem.statusId!}</td>
                        <td>&nbsp;</td>
                        <td>${mercuryItem.startDate!}</td>
                        <td>&nbsp;</td>
                        <td><#if mercuryItem.statusId == "COMPLETED">${mercuryItem.completedDate!}<#else><#if mercuryItem.overdue><font color=red></#if><#if mercuryItem.completeDateEta?date!=dummyDate?date>${mercuryItem.completeDateEta!?date}</#if><#if mercuryItem.overdue></font></#if></#if></td>
                        <td>&nbsp;</td>
                        <td>${mercuryItem.title!}</td>
                        <td>&nbsp;</td>
                        <td>${mercuryItem.notes!}</td>
                        <td>&nbsp;</td>
                        <td>${mercuryItem.assignedTo!}</td>
                    </tr>
                    <tr id="hideId_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}" style="display: none;">
                        <td colspan=13>
                            <form action="<@ofbizUrl>editMercuryItem</@ofbizUrl>" method="post" name="editMercuryItem_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}">
                                <input type="hidden" name="mercuryId" value="${mercuryHeader.mercuryId}"/>
                                <input type="hidden" name="sequenceId" value="${mercuryItem.sequenceId}"/>
                                <table>
                                    <tr><td>Task Name</td><td><input type="text" name="title" size="120" value="${mercuryItem.title!}"/></td></tr>
                                    <tr><td>Notes</td><td><textarea cols="120" rows="1" maxlength="5000" name="notes">${mercuryItem.notes!}</textarea></td></tr>
                                    <tr>
                                        <td>StartDate</td>
                                        <td nowrap="nowrap">
                                            <@htmlTemplate.renderDateTimeField name="startDate" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="20" maxlength="25" id="edit_startDate_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>ETA</td>
                                        <td nowrap="nowrap">
                                            <@htmlTemplate.renderDateTimeField name="completeDateEta" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="20" maxlength="25" id="edit_completeDateEta_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Status</td>
                                        <td>
                                            <select name="statusId">
                                                <option value="PENDING" <#if mercuryItem.statusId=="PENDING">SELECTED</#if>>${uiLabelMap.CommonPending}</option>
                                                <option value="IN_PROGRESS" <#if mercuryItem.statusId=="IN_PROGRESS">SELECTED</#if>>${uiLabelMap.CommonInProgress}</option>
                                                <option value="COMPLETED" <#if mercuryItem.statusId=="COMPLETED">SELECTED</#if>>${uiLabelMap.CommonCompleted}</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Assign To</td>
                                        <td>
                                            <select name="assignedTo" id="assignedTo">
                                                <option value=""></option>
                                                <#assign groupList = delegator.findByAnd("PartyGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("groupNameLocal" , "GUDAO", "comments", "ACTIVE"), null, true)>
                                                <#list groupList?sort_by("groupName") as group>
                                                    <option value="${group.partyId}" <#if userLoginId = group.partyId>SELECTED</#if>>${group.groupName}</option>
                                                    <#assign userLoginList = delegator.findByAnd("UserLogin", Static["org.ofbiz.base.util.UtilMisc"].toMap("partyId" , group.partyId, "enabled", "Y"), null, true)>
                                                    <#list userLoginList?sort_by("userLoginId") as userLogin>
                                                        <#assign receiverUserLoginId = userLogin.userLoginId>
                                                        <option value="${receiverUserLoginId}" <#if userLoginId = receiverUserLoginId>SELECTED</#if>>${receiverUserLoginId}</option>
                                                    </#list>
                                                    <option value=""></option>
                                                </#list>
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                                <input type="submit" value="Update" class="smallSubmit"/>
                            </form>
                            <#if hasPermission || isAdmin>
                                <form action="<@ofbizUrl>deleteMercuryItem</@ofbizUrl>" method="post" name="deleteMercuryItem_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}" id="deleteMercuryItem_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}">
                                    <input type="hidden" name="mercuryId" value="${mercuryHeader.mercuryId}"/>
                                    <input type="hidden" name="sequenceId" value="${mercuryItem.sequenceId}"/>
                                    <input type="submit" value="Delete" class="smallSubmit"/>
                                </form>
                            </#if>
                        </td>
                    </tr>
                </#list>
            </table>
            <div id="${mercuryHeader.mercuryId}_addNew">
                <#if hasPermission || isAdmin>
                    <a class="smallSubmit" onclick="javascript:toggleScreenlet('${mercuryHeader.mercuryId}_addNew', '${mercuryHeader.mercuryId}_addNewDetail', 'true', 'Expand', 'Collapse');" title="AddNew">Add New Task</a>
                </#if>
                <div id="${mercuryHeader.mercuryId}_addNewDetail" style="display: none;">
                    <form action="<@ofbizUrl>addMercuryItem</@ofbizUrl>" method="post" name="addMercuryItem_${mercuryHeader.mercuryId}" id="addMercuryItem_${mercuryHeader.mercuryId}" onsubmit="return validateEta(${mercuryHeader.mercuryId})">
                        <input type="hidden" name="mercuryId" value="${mercuryHeader.mercuryId}"/>
                        <table>
                            <tr><td>Task Name</td><td><input type="text" name="title" size="120"/></td></tr>
                            <tr><td>Notes</td><td><textarea cols="120" rows="1" maxlength="5000" name="notes"></textarea></td></tr>
                            <tr>
                                <td>StartDate</td>
                                <td nowrap="nowrap">
                                    <@htmlTemplate.renderDateTimeField name="startDate" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="20" maxlength="25" id="add_startDate_${mercuryHeader.mercuryId}" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                </td>
                            </tr>
                            <tr>
                                <td>ETA</td>
                                <td nowrap="nowrap">
                                    <@htmlTemplate.renderDateTimeField name="completeDateEta" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd HH:mm:ss.SSS" size="20" maxlength="25" id="add_completeDateEta_${mercuryHeader.mercuryId}" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
                                </td>
                            </tr>
                            <tr>
                                <td>Status</td>
                                <td>
                                    <select name="statusId">
                                        <option value="PENDING" SELECTED>${uiLabelMap.CommonPending}</option>
                                        <option value="IN_PROGRESS">${uiLabelMap.CommonInProgress}</option>
                                        <option value="COMPLETED">${uiLabelMap.CommonCompleted}</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td>Assign To</td>
                                <td>
                                    <select name="assignedTo" id="assignedTo">
                                        <option value=""></option>
                                        <#assign groupList = delegator.findByAnd("PartyGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("groupNameLocal" , "GUDAO", "comments", "ACTIVE"), null, true)>
                                        <#list groupList?sort_by("groupName") as group>
                                            <option value="${group.partyId}" <#if userLoginId = group.partyId>SELECTED</#if>>${group.groupName}</option>
                                            <#assign userLoginList = delegator.findByAnd("UserLogin", Static["org.ofbiz.base.util.UtilMisc"].toMap("partyId" , group.partyId, "enabled", "Y"), null, true)>
                                            <#list userLoginList?sort_by("userLoginId") as userLogin>
                                                <#assign receiverUserLoginId = userLogin.userLoginId>
                                                <option value="${receiverUserLoginId}" <#if userLoginId = receiverUserLoginId>SELECTED</#if>>${receiverUserLoginId}</option>
                                            </#list>
                                            <option value=""></option>
                                        </#list>
                                    </select>
                                </td>
                            </tr>
                        </table>
                        <input type="submit" value="Create" class="smallSubmit"/>
                    </form>
                </div>
            </div>
        </div>
    </div>
</#if>
<br class="clear"/>
<div align="center">
    <a href="javascript:void(0)" onclick="window.close();" class="buttontext">Close</a>
</div>

<br/>

<br/>
<br/>

<#--
${mercuryItemList}
-->
<#--
${parameters}
<br />

${userLoginList}
<br />

<br/>
-->

