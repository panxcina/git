<#if mercuryHeader?has_content>
    <#if mercuryHeader.description?has_content>
        <table class="basic-table hover-bar">
            <tr class="header-row"><td>Description</td></tr>
            <tr>
                <td style="white-space:pre;">${mercuryHeader.description}<hr></td>
            </tr>
        </table>
    </#if>
    <table class="basic-table hover-bar">
        <tr class="header-row">
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
            <td width="7%">Assigned to</td>
        </tr>
        <#list mercuryItemList as mercuryItem>
            <tr id="displayId_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}">
                <td>${mercuryItem.statusId!}</td>
                <td>&nbsp;</td>
                <td>${mercuryItem.startDate!}</td>
                <td>&nbsp;</td>
                <td><#if mercuryItem.overdue><font color=red></#if>${mercuryItem.completeDateEta!}<#if mercuryItem.overdue></font></#if></td>
                <td>&nbsp;</td>
                <td>${mercuryItem.title!}</td>
                <td>&nbsp;</td>
                <td>${mercuryItem.notes!}</td>
                <td>&nbsp;</td>
                <td>${mercuryItem.assignedTo!}</td>
            </tr>
        </#list>
    </table>

    <br class="clear"/>
    <hr>
    <br class="clear"/>

    <table class="basic-table hover-bar">
        <tr class="header-row">
            <td width="5%">Status</td>
            <td width="1%">&nbsp;</td>
            <td width="7%">start Date</td>
            <td width="1%">&nbsp;</td>
            <td width="7%">Completed Date</td>
            <td width="1%">&nbsp;</td>
            <td width="30%">Task Name</td>
            <td width="1%">&nbsp;</td>
            <td width="30%">notes</td>
            <td width="1%">&nbsp;</td>
            <td width="7%">Assigned to</td>
        </tr>
        <#list mercuryItemListCompleted?sort_by("completedDate") as mercuryItem>
            <tr id="displayId_${mercuryHeader.mercuryId}_${mercuryItem.sequenceId}">
                <td>${mercuryItem.statusId!}</td>
                <td>&nbsp;</td>
                <td>${mercuryItem.startDate!}</td>
                <td>&nbsp;</td>
                <td>${mercuryItem.completedDate!}</td>
                <td>&nbsp;</td>
                <td>${mercuryItem.title!}</td>
                <td>&nbsp;</td>
                <td>${mercuryItem.notes!}</td>
                <td>&nbsp;</td>
                <td>${mercuryItem.assignedTo!}</td>
            </tr>
        </#list>
    </table>
<#else>
    No Ongoing Project Task Found!
</#if>