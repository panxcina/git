<#-- task View -->

<style type="text/css">
A.bigbutton,.bigbuttondisabled {
border-bottom: #999999 solid 0.1em;
border-top: #f7e893 solid 0.1em;
border-left: #f7e893 solid 0.1em;
border-right: #999999 solid 0.1em;
background-image: url(../images/button_whitegray.jpg);
background-repeat:repeat-x;
font-weight: bold;
font-size: 20px;
line-height: 2.9em;
margin-left:  0.0em;
padding: 0.4em 0.6em 0.4em 0.6em;
/*white-space: nowrap;*/
}

</style>


<div id="taskView" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Filter</b></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="center">


<form action="<@ofbizUrl>operationalTaskView</@ofbizUrl>" method="post" name="taskFilter" id="taskFilter">
<table cellspacing="0" class="basic-table">

<tbody>



<tr>
<td class="label">请求类型</td>
<td>
<select name="type">
<option value="ALL" <#if type?has_content && type = "ALL">SELECTED</#if>>All</option>
<option value="SOURCING_PROBLEM" <#if type?has_content && type = "SOURCING_PROBLEM">SELECTED</#if>>无法采购</option>
<option value="COST_UPDATE" <#if type?has_content && type = "COST_UPDATE">SELECTED</#if>>成本更新</option>
<option value="MOQ_UPDATE" <#if type?has_content && type = "MOQ_UPDATE">SELECTED</#if>>起订量更新</option>
<option value="WEIGHT_UPDATE" <#if type?has_content && type = "WEIGHT_UPDATE">SELECTED</#if>>重量更新</option>
<option value="LISTING_ERROR" <#if type?has_content && type = "LISTING_ERROR">SELECTED</#if>>刊登错误</option>
<option value="EDIT_ORDER" <#if type?has_content && type = "EDIT_ORDER">SELECTED</#if>>订单修改</option>
<option value="EXCESSIVE_DOMESTIC_POSTAGE" <#if type?has_content && type = "EXCESSIVE_DOMESTIC_POSTAGE">SELECTED</#if>>国内运费过高</option>
<option value="EXCESSIVE_RETURNS" <#if type?has_content && type = "EXCESSIVE_RETURNS">SELECTED</#if>>退包比例过高</option>
<option value="EXCESSIVE_QC" <#if type?has_content && type = "EXCESSIVE_QC">SELECTED</#if>>质量投诉过多</option>
<option value="HIGH_VERO_RISK" <#if type?has_content && type = "HIGH_VERO_RISK">SELECTED</#if>>侵权风险过高</option>
<option value="RIVAL_REMOVED" <#if type?has_content && type = "RIVAL_REMOVED">SELECTED</#if>>对手链接失效</option>
<option value="OTHERS" <#if type?has_content && type = "OTHERS">SELECTED</#if>>其他请求</option>
</select>
</td>
</tr>


<tr>
<td class="label">请求状态</td>
<td>
<select name="statusId">
<option value="ALL" <#if statusId?has_content && statusId = "ALL">SELECTED</#if>>All</option>
<option value="PENDING" <#if statusId?has_content && statusId = "PENDING">SELECTED</#if>>待处理</option>
<option value="IN_PROGRESS" <#if statusId?has_content && statusId = "IN_PROGRESS">SELECTED</#if>>处理中</option>
<option value="COMPLETED" <#if statusId?has_content && statusId = "COMPLETED">SELECTED</#if>>已完成</option>
</select>
</td>
</tr>


<tr>
<td class="label">紧急程度</td>
<td>
<select name="priority">
<option value="ALL" <#if priority?has_content && priority = "ALL">SELECTED</#if>>All</option>
<option value="NORMAL" <#if priority?has_content && priority = "NORMAL">SELECTED</#if>>一般</option>
<option value="HIGH" <#if priority?has_content && priority = "HIGH">SELECTED</#if>>紧急</option>
</select>
</td>
</tr>



<tr>
<td class="label">接收人</td>
<td>
<select name="receiver">
<option value="">Choose one</option>
<option value="ALL" <#if receiver?has_content && receiver = "ALL">SELECTED</#if>>All</option>
<option value=""></option>
<#assign groupList = delegator.findByAnd("PartyGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("groupNameLocal" , "GUDAO", "comments", "ACTIVE"), null, true)>
<#list groupList?sort_by("groupName") as group>
<option value=${group.partyId}>${group.groupName}</option>
<#assign userLoginList = delegator.findByAnd("UserLogin", Static["org.ofbiz.base.util.UtilMisc"].toMap("partyId" , group.partyId, "enabled", "Y"), null, true)>
<#list userLoginList?sort_by("userLoginId") as userLogin>
<#assign receiverUserLoginId = userLogin.userLoginId>
<option value=${receiverUserLoginId}>${receiverUserLoginId}</option>
</#list>
<option value=""></option>
</#list>
</select>
</td>
</tr>






<tr>
<td>&nbsp;</td>
<td>
<input type="submit" value="Search" class="smallSubmit"/>
</td>
</tr>



</tbody></table>
</form>




    </div>
</div>

</div>


<div id="taskView" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Task</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
<a class="buttontext" href="<@ofbizUrl>editOperationalTask</@ofbizUrl>" onclick="window.open(this.href,'','height=450,width=650,scrollbars=1');return false;">
Create New task
</a>
<table class="basic-table hover-bar" cellspacing='10'>
    <tr class="header-row" style="white-space: nowrap;">
        <td align="center">Action</td>
        <td align="center">Type</td>
        <td align="center">Priority</td>
        <td align="center">Status</td>
        <td align="center">Created By</td>
        <td align="center">Receiver</td>
        <td align="center">ETA</td>
        <td align="center">Created Date</td>
        <td align="center">Description</td>
        <td align="center">Remark</td>
    </tr>

    <#assign alt_row = false>
    <#list (taskHeaderList?sort_by("createdDate"))?reverse as taskHeader>
        <tr valign="middle">
            <td align="center" width = "100px">
                <#if userLoginId = taskHeader.createdBy>
                    <table class="basic-table hover-bar">
                        <tr>
                            <td width =50px>
                                <a class="buttontext" href="<@ofbizUrl>editOperationalTask?taskId=${taskHeader.taskId}</@ofbizUrl>" onclick="window.open(this.href,'','height=450,width=650,scrollbars=1');return false;">
                                Edit
                                </a>
                            </td>
                            <td widh = 50px>
                                <form action="<@ofbizUrl>deleteOperationalTask</@ofbizUrl>" method="post" name="taskDelete_${taskHeader.taskId}" id="taskDelete_${taskHeader.taskId}">
                                    <input type="hidden" name="taskId" value="${taskHeader.taskId!}">
                                    <input type="submit" value="Delete" class="smallSubmit"/>
                                </form>
                            </td>
                        </tr>
                    </table>
                <#elseif taskHeader.action ="EDIT">
                    <a class="buttontext" href="<@ofbizUrl>editOperationalTask?taskId=${taskHeader.taskId}</@ofbizUrl>" onclick="window.open(this.href,'','height=450,width=650,scrollbars=1');return false;">
                        Edit
                    </a>
                <#elseif taskHeader.action ="ACCEPT">
                    <a class="buttontext" href="<@ofbizUrl>editOperationalTask?taskId=${taskHeader.taskId}</@ofbizUrl>" onclick="window.open(this.href,'','height=450,width=650,scrollbars=1');return false;">
                        Accept
                    </a>
                <#elseif taskHeader.action ="NONE">
                    Not Available
                </#if>
            </td>
            <td align="center" width="100px">
                <#if taskHeader.type?has_content>
                    <#if taskHeader.type = "SOURCING_PROBLEM">
                        无法采购
                    <#elseif taskHeader.type = "COST_UPDATE">
                        成本更新
                    <#elseif taskHeader.type = "MOQ_UPDATE">
                        起订量更新
                    <#elseif taskHeader.type = "WEIGHT_UPDATE">
                        重量更新
                    <#elseif taskHeader.type = "LISTING_ERROR">
                        刊登错误
                    <#elseif taskHeader.type = "EXCESSIVE_DOMESTIC_POSTAGE">
                        国内运费过高
                    <#elseif taskHeader.type = "EXCESSIVE_RETURNS">
                        退包比例过高
                    <#elseif taskHeader.type = "EXCESSIVE_QC">
                        质量投诉过多
                    <#elseif taskHeader.type = "RIVAL_REMOVED">
                        对手链接失效
                    <#elseif taskHeader.type = "OTHERS">
                        其他请求
                    <#elseif taskHeader.type = "HIGH_VERO_RISK">
                        侵权风险过高
                    </#if>
                </#if>
            </td>
            <td align="center" width="80px">
                <#if taskHeader.priority?has_content>
                    <#if taskHeader.priority = "NORMAL">
                        一般
                    <#elseif taskHeader.priority = "HIGH">
                        <font color="red"><b>紧急</b></font>
                    </#if>
                </#if>
            </td>
            <td align="center" width="80px">
                <#if taskHeader.statusId?has_content>
                    <#if taskHeader.statusId = "PENDING">
                        <font color="brown"><b>待处理</b></font>
                    <#elseif taskHeader.statusId = "COMPLETED">
                        已完成
                    <#elseif taskHeader.statusId = "IN_PROGRESS">
                        <font color="blue"><b>处理中</b></font>
                    </#if>
                </#if>
            </td>
            <td align="center" width="100px"><#if taskHeader.createdBy?has_content>${taskHeader.createdBy}</#if></td>
            <td align="center" width="100px"><#if taskHeader.receiver?has_content>${taskHeader.receiver}</#if></td>
            <td align="center" width ="100px"><#if taskHeader.eta?has_content>${taskHeader.eta}</#if></td>
            <td align="center" width ="100px"><#if taskHeader.createdDate?has_content>${taskHeader.createdDate}</#if></td>
<td align="left" width="500px" style="word-wrap:break-word;"><#if taskHeader.description?has_content>${taskHeader.description}</#if></td>
            <td align="center" width="500px" style="word-wrap:break-word;"><#if taskHeader.remark?has_content>${taskHeader.remark}</#if></td>

        </tr>
        <#assign alt_row = !alt_row>
    </#list>
</table>
    </div>
</div>