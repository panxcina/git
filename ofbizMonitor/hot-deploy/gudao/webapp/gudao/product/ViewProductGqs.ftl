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


<form action="<@ofbizUrl>viewProductGqs</@ofbizUrl>" method="post" name="gqsFilter" id="gqsFilter">
<table cellspacing="0" class="basic-table">

<tbody>



<tr>
<td class="label">Platform</td>
<td>
<select name="platform">
<option value="ALL">All</option>
<option value="EBAY" <#if platformFilter?? && platformFilter = "EBAY">SELECTED</#if>>eBay</option>
<option value="SMT" <#if platformFilter?? && platformFilter = "SMT">SELECTED</#if>>SMT</option>
<option value="WISH" <#if platformFilter?? && platformFilter = "WISH">SELECTED</#if>>Wish</option>
<option value="AMAZON" <#if platformFilter?? && platformFilter = "AMAZON">SELECTED</#if>>Amazon</option>
</select>
</td>
</tr>


<tr>
<td class="label">Channel</td>
<td>
<select name="channel">
<option value="ALL">All</option>
<option value="CASE" <#if channelFilter?? && channelFilter = "CASE">SELECTED</#if>>Case</option>
<option value="FEEDBACK" <#if channelFilter?? && channelFilter = "FEEDBACK">SELECTED</#if>>Feedback</option>
<option value="MESSAGE" <#if channelFilter?? && channelFilter = "MESSAGE">SELECTED</#if>>Message</option>
<option value="RETURN_REQUEST" <#if channelFilter?? && channelFilter = "RETURN_REQUEST">SELECTED</#if>>Return Request</option>
</select>
</td>
</tr>

<tr>
<td class="label" >Type</td>
<td>
<select name="type">
<option value="ALL">All</option>
<#assign gqsTypeList = delegator.findByAnd("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumTypeId" , "GQS_TYPE"), null, true)>
<#list gqsTypeList?sort_by("description") as gqsType>
<option value=${gqsType.enumId} <#if typeFilter?? && typeFilter = gqsType.enumId>SELECTED</#if>>${gqsType.description}</option>
</#list>
</select>
</td>
</tr>

<tr>
<td class="label" >Shipping Method</td>
<td>
<select name="shipType">
<option value="ALL">All</option>
<#assign shipTypeList = delegator.findByAnd("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumTypeId" , "GUDAO_SHIP_TYPE"), null, true)>
<#list shipTypeList?sort_by("sequenceId") as shipType>
<option value=${shipType.enumId} <#if shipTypeFilter?? && shipTypeFilter = shipType.enumId>SELECTED</#if>>${shipType.description}</option>
</#list>
</select>
</td>
</tr>

<#--<tr>
<td class="label">Owner Group</td>
<td>
<select name="ownerGroup">
<option value="ALL">All</option>
<#assign ownerGroupList = delegator.findByAnd("OwnerGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("statusId" , "ACTIVE"), null, true)>
<#list ownerGroupList?sort_by("ownerGroupName") as ownerGrp>
<option value=${ownerGrp.ownerGroupId} <#if productGqsDetail?? && productGqsDetail.ownerGroup = ownerGrp.ownerGroupId>SELECTED</#if>>${ownerGrp.ownerGroupName}</option>
</#list>
</select>
</td>
</tr>-->

<tr>
<td class="label">Created By</td>
<td>
<select name="createdBy">
<option value="ALL">All</option>
<#assign createdByList = delegator.findByAnd("UserLogin", Static["org.ofbiz.base.util.UtilMisc"].toMap("partyId" , "CUSTOMER_SERVICE_GROUP"), null, true)>
<#list createdByList?sort_by("userLoginId") as createdBy>
<option value=${createdBy.userLoginId} <#if createdByFilter?? && createdByFilter = createdBy.userLoginId>SELECTED</#if>>${createdBy.userLoginId}</option>
</#list>
</select>
</td>
</tr>

<tr>
<td class="label">Store Name</td>
<td>
<input type="text" name="account" size="40" <#if accountFilter??>value="${accountFilter}"</#if>/>
</td>
</tr>

<tr>
<td class="label">Order ID</td>
<td>
<input type="text" name="orderId" size="40" <#if orderIdFilter??>value="${orderIdFilter}"</#if>/>
</td>
</tr>

<tr>
<td class="label">Customer ID</td>
<td>
<input type="text" name="customerId" size="40" <#if customerIdFilter??>value="${customerIdFilter}"</#if>/>
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
<a class="buttontext" href="<@ofbizUrl>editProductGqsDetail</@ofbizUrl>" onclick="window.open(this.href,'','height=750,width=750,scrollbars=1');return false;">
Create New Entry
</a>
<table class="basic-table hover-bar" cellspacing='10'>
    <tr class="header-row" style="white-space: nowrap;">
        <td align="center">Order Id</td>
        <td align="center">Type</td>
        <td align="center">Created By</td>
        <td align="center">Created Date</td>
        <td align="center">Platform</td>
        <td align="center">Channel</td>
<td align="center">Store Name</td>

        <td align="center">Customer Id</td>
<#--<td align="center">Owner Group</td>-->
        <td align="center">Description</td>
    </tr>

    <#assign alt_row = false>
    <#list (productGqsDetailList?sort_by("createdStamp"))?reverse as productGqsDetail>
        <tr valign="middle">
<#--<td align="center" width = "100px">
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
            </td>-->
<td align="center" width ="100px"><#if productGqsDetail.orderId?has_content>
<a class="buttontext" href="<@ofbizUrl>editProductGqsDetail?orderId=${productGqsDetail.orderId}</@ofbizUrl>" onclick="window.open(this.href,'','height=750,width=700,scrollbars=1');return false;">
${productGqsDetail.orderId}
</a>



</#if></td>
<td align="center" width ="150px" style="word-wrap: break-word;">
<#if productGqsDetail.type?has_content>
<#assign typeResult = delegator.findOne("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumId", productGqsDetail.type), true)>
${typeResult.description}

</#if></td>
            <td align="center" width="100px">
                <#if productGqsDetail.createdBy?has_content>${productGqsDetail.createdBy}</#if>
            </td>
            <td align="center" width="100px"><#if productGqsDetail.createdStamp?has_content>${productGqsDetail.createdStamp}</#if></td>
            <td align="center" width="50px"><#if productGqsDetail.platform?has_content>${productGqsDetail.platform}</#if></td>
            <td align="center" width ="80px"><#if productGqsDetail.channel?has_content>${productGqsDetail.channel}</#if></td>
<td align="center" width ="80px"><#if productGqsDetail.account?has_content>${productGqsDetail.account}</#if></td>

<td align="center" width ="100px"><#if productGqsDetail.customerId?has_content>${productGqsDetail.customerId}</#if></td>
<#--<td align="center" width ="80px"><#if productGqsDetail.ownerGroup?has_content>${productGqsDetail.ownerGroup}</#if></td>-->
            <td align="left" width="400px" style="word-wrap:break-word;"><#if productGqsDetail.description?has_content>${productGqsDetail.description}</#if></td>

        </tr>
        <#assign alt_row = !alt_row>
    </#list>
</table>
    </div>
</div>