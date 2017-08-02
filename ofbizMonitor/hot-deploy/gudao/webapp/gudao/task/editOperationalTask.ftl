<form action="<#if gudaoTaskHeader??><@ofbizUrl>updateOperationalTask</@ofbizUrl><#else><@ofbizUrl>createOperationalTask</@ofbizUrl></#if>" method="post" name="taskForm" id="taskForm" onsubmit="return checkscript()">
<table cellspacing="0" class="basic-table">

<tbody>

<#if gudaoTaskHeader??>
<input type="hidden" name="taskId" value="${gudaoTaskHeader.taskId!}">
</#if>

<tr>
<td class="label">请求类型&nbsp;&nbsp;&nbsp;</td>
<td>
<select name="type" id ="type">
<option value=""></option>
<option value="SOURCING_PROBLEM" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "SOURCING_PROBLEM">SELECTED</#if>>无法采购</option>
<option value="COST_UPDATE" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "COST_UPDATE">SELECTED</#if>>成本更新</option>
<option value="MOQ_UPDATE" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "MOQ_UPDATE">SELECTED</#if>>起订量更新</option>
<option value="WEIGHT_UPDATE" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "WEIGHT_UPDATE">SELECTED</#if>>重量更新</option>
<option value="LISTING_ERROR" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "LISTING_ERROR">SELECTED</#if>>刊登错误</option>
<option value="EDIT_ORDER" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "EDIT_ORDER">SELECTED</#if>>订单修改</option>
<option value="EXCESSIVE_DOMESTIC_POSTAGE" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "EXCESSIVE_DOMESTIC_POSTAGE">SELECTED</#if>>国内运费过高</option>
<option value="EXCESSIVE_RETURNS" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "EXCESSIVE_RETURNS">SELECTED</#if>>退包比例过高</option>
<option value="EXCESSIVE_QC" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "EXCESSIVE_QC">SELECTED</#if>>质量投诉过多</option>
<option value="HIGH_VERO_RISK" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "HIGH_VERO_RISK">SELECTED</#if>>侵权风险过高</option>
<option value="RIVAL_REMOVED" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "RIVAL_REMOVED">SELECTED</#if>>对手链接失效</option>
<option value="OTHERS" <#if gudaoTaskHeader?? && gudaoTaskHeader.type = "OTHERS">SELECTED</#if>>其他请求</option>
</select>
</td>
</tr>


<tr>
<td class="label">请求状态</td>
<td>
<select name="statusId">
<option value="PENDING" <#if gudaoTaskHeader?? && gudaoTaskHeader.statusId = "PENDING">SELECTED</#if>>待处理</option>
<option value="COMPLETED" <#if gudaoTaskHeader?? && gudaoTaskHeader.statusId = "COMPLETED">SELECTED</#if>>已完成</option>
<option value="IN_PROGRESS" <#if gudaoTaskHeader?? && (gudaoTaskHeader.statusId = "IN_PROGRESS" || !gudaoTaskHeader.lastUpdatedBy?has_content)>SELECTED</#if>>处理中</option>
</select>
</td>
</tr>


<tr>
<td class="label">紧急程度</td>
<td>
<select name="priority">
<option value="NORMAL" <#if gudaoTaskHeader?? && gudaoTaskHeader.priority = "NORMAL">SELECTED</#if>>一般</option>
<option value="HIGH" <#if gudaoTaskHeader?? && gudaoTaskHeader.priority = "HIGH">SELECTED</#if>>紧急</option>
</select>
</td>
</tr>


<#if gudaoTaskHeader??>
<input type="hidden" name="createdBy" value="${gudaoTaskHeader.createdBy!}">
<#else>
<tr>
<td class="label">Created By</td>
<td>
<select name="createdBy" id="createdBy">
<option value=""></option>
<#assign groupList = delegator.findByAnd("PartyGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("groupNameLocal" , "GUDAO"), null, true)>
<#list groupList?sort_by("groupName") as group>
<option value=${group.partyId} <#if gudaoTaskHeader?? && gudaoTaskHeader.createdBy = group.partyId>SELECTED</#if>>${group.groupName}</option>
<#assign userLoginList = delegator.findByAnd("UserLogin", Static["org.ofbiz.base.util.UtilMisc"].toMap("partyId" , group.partyId, "enabled", "Y"), null, true)>
<#list userLoginList?sort_by("userLoginId") as userLogin>
<#assign receiverUserLoginId = userLogin.userLoginId>
<option value=${receiverUserLoginId} <#if gudaoTaskHeader?? && gudaoTaskHeader.createdBy = receiverUserLoginId>SELECTED</#if>>${receiverUserLoginId}</option>
</#list>
<option value=""></option>
</#list>
</select>
</td>
</tr>
</#if>


<tr>
<td class="label">接收人</td>
<td>
<select name="receiver" id="receiver">
<option value=""></option>
<#assign groupList = delegator.findByAnd("PartyGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("groupNameLocal" , "GUDAO"), null, true)>
<#list groupList?sort_by("groupName") as group>
<option value=${group.partyId} <#if gudaoTaskHeader?? && gudaoTaskHeader.receiver = group.partyId>SELECTED</#if>>${group.groupName}</option>
<#assign userLoginList = delegator.findByAnd("UserLogin", Static["org.ofbiz.base.util.UtilMisc"].toMap("partyId" , group.partyId, "enabled", "Y"), null, true)>
<#list userLoginList?sort_by("userLoginId") as userLogin>
<#assign receiverUserLoginId = userLogin.userLoginId>
<option value=${receiverUserLoginId} <#if gudaoTaskHeader?? && gudaoTaskHeader.receiver = receiverUserLoginId>SELECTED</#if>>${receiverUserLoginId}</option>
</#list>
<option value=""></option>
</#list>
</select>
</td>
</tr>

<tr>
<td class="label">ETA</td>
<td>
<@htmlTemplate.renderDateTimeField name="eta" event="" action="" value="" className="" alert="" title="Format: yyyy-MM-dd" size="25" maxlength="30" id="maxDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/>
</td>
</tr>

<tr>
<td class="label">请求内容</td>
<td>

<textarea cols="60" rows="5" maxlength="5000" name="description" id="description"><#if gudaoTaskHeader??>${gudaoTaskHeader.description!}</#if></textarea>
</td>
</tr>

<tr>
<td class="label">处理结果</td>
<td>
<textarea cols="60" rows="5" maxlength="5000" name="remark"><#if gudaoTaskHeader??>${gudaoTaskHeader.remark!}</#if></textarea>
</td>
</tr>




<tr>
<td>&nbsp;</td>
<td>
<input type="submit" value="<#if gudaoTaskHeader??>Update<#else>Create</#if>" class="smallSubmit"/>
<a href="javascript:void(0)" onclick="window.close();" class="smallSubmit">Cancel</a>
</td>
</tr>


</tbody></table>
</form>


<script language="JavaScript" type="text/javascript">
    function checkscript() {
        var checkProcess = false;
        if(document.getElementById('type').value) {
            checkProcess = true;
        } else {
            alert('"请求类型" 不能空白');
            return false;
        }

        if(document.getElementById('receiver').value) {
            checkProcess = true;
        } else {
            alert('"接收人" 不能空白');
            return false;
        }

        if(document.getElementById('description').value) {
            checkProcess = true;
        } else {
            alert('“请求内容” 不能空白');
            return false;
        }

        return checkProcess;
    }
</script>