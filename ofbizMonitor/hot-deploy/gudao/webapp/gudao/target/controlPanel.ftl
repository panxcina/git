<style type="text/css">
table {
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
tr  td {
border: 1px solid #333333;
display:table-cell;
padding: 0px 10px 0px 10px;
}
</style>


<div id="targetList" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Filter</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="left">
        <form action="<@ofbizUrl>controlPanel</@ofbizUrl>" method="post" name="controlPanelForm" id="controlPanelForm" onsubmit="return checkscript()">
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
                <option value=""></option>
                <#assign monthList = delegator.findByAnd("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumTypeId" , "PERIOD"), Static["org.ofbiz.base.util.UtilMisc"].toList("sequenceId"), false)>
                <#list monthList?sort_by("sequenceId") as singleMonthList>
                    <option value="${singleMonthList.enumId}" <#if singleMonthList.enumId = currentMonth>SELECTED</#if>>${singleMonthList.description}</option>
                </#list>
            </select>
            <br />
        <input type="submit" value="Submit" class="smallSubmit"/>
    </form>
</div>


<div id="targetList" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Control Panel</li>
        </ul>
        <br class="clear"/>
    </div>


    <div class="screenlet-body" align="center">
        <table  style="line-height: 1.0em;" border="1">
            <tr>
                <td style="background-color: rgba(255,0,0,0.6)" width ="10px"></td>
                <td><font style="color:rgba(255,0,0,0.6);">High Priority</font></td>
            </tr>
            <tr>
                <td style="background-color: green;"></td>
                <td><font color=green>Action Completed</font></td>
            </tr>
            <tr>
                <td style="background-color: blue;"></td>
                <td><font color=blue>Action In Progress</font></td>
            </tr>
            <tr>
                <td style="background-color: brown;"></td>
                <td><font color=brown>Action Pending</font></td>
            </tr>
            <tr>
                <td style="background-color: gray;"></td>
                <td><font color=gray>Action Aborted</font></td>
            </tr>
        </table>
        <br />
        <table  cellpadding="0" cellspacing="0">
            <!--<tr>
                <td  rowspan="2" style="vertical-align:middle" >Mission</td>
                <td  colspan="4" >Company Mission </td>
            </tr>
            <tr>
                <td colspan="4">Value Proposition </td>
            </tr>-->

            <tr>
                <td>Target</td>
                <td>When</td>
                <td>Who</td>
                <td>What</td>

            </tr>

            <#list gudaoActionList as gudaoAction>
                <tr>
                    <td rowspan="${gudaoAction.actionListSize}" style="vertical-align:middle">${gudaoAction.target}</td>
                    <#list gudaoAction.actionList as gudaoActionChild>
                        <tr>
                        <td>${gudaoActionChild.dueDate}</td>
                        <td>${gudaoActionChild.actionBy}</td>
                        <td align="left" <#if gudaoActionChild.priority = "HIGH">style="background-color: rgba(255,0,0,0.6);"</#if>><font color=<#if gudaoActionChild.statusId = "COMPLETED">green<#elseif gudaoActionChild.statusId = "IN_PROGRESS">blue<#elseif gudaoActionChild.statusId = "PENDING">brown<#elseif gudaoActionChild.statusId = "ABORTED">grey</#if>>${gudaoActionChild.description}</font></td>
                        </tr>
                    </#list>
                </tr>
            </#list>
        </table>
    </div>
</div>
<!--<tr>
<td rowspan="10" style="vertical-align:middle">Support</td>
<td rowspan="3" style="vertical-align:middle">SOP</td>
<td>&nbsp;</td>
<td>Update Date </td>
<td>Update Member </td>
</tr>
<tr>
<td>Posting</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>Pictures</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td rowspan="4"style="vertical-align:middle">Criteria</td>
<td>New Products </td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>Top Accounts </td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>Top Products </td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>Top Listings </td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td rowspan="3" style="vertical-align:middle">Requests</td>
<td>&nbsp;</td>
<td>No. of Requests </td>
<td>No. of Urgents </td>
</tr>
<tr>
<td>Outstanding</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>In Progress </td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td rowspan="14" style="vertical-align:middle">Report</td>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td>Last Week </td>
<td>Last Month </td>
</tr>
<tr>
<td rowspan="2" style="vertical-align:middle">Sales</td>
<td>Daily GP </td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>Daily Orders </td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td rowspan="3" style="vertical-align:middle">Accounts</td>
<td>Accounts</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>Top Accounts </td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>GP/Accounts</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td rowspan="3" style="vertical-align:middle">Products</td>
<td>Products</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>Top Products </td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>GP/Products</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td rowspan="3" style="vertical-align:middle">Listings</td>
<td>Listings</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>Top Listings </td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>GP/Listings</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td rowspan="2" style="vertical-align:middle">Members</td>
<td>Members</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>
<tr>
<td>GP/Member</td>
<td><input type="text"/></td>
<td><input type="text"/></td>
</tr>-->





