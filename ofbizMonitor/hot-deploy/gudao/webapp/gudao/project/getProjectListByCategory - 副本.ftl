<#if mercuryList?has_content>
    <table>
        <tr>
            <td width="7%">&nbsp;</td>
            <td width="10%" class="border">Progress</td>
            <td width="1%">&nbsp;</td>
            <td width="70%" class="border">Title</td>
            <td width="1%">&nbsp;</td>
            <td width="7%" class="border">By</td>
            <td width="1%">&nbsp;</td>
            <td width="3%" class="border">Overdue</td>
            <td width="1%">&nbsp;</td>
            <td width="3%" class="border">Last ETA</td>
        </tr>
        <#list mercuryList as mercuryHeader>
            <tr>
                <td width="5%">
                    <#if mercuryHeader.hasPermission><a class="buttontext" href="<@ofbizUrl>editProject?mercuryId=${mercuryHeader.mercuryId}</@ofbizUrl>" onclick="window.open(this.href,'','height=768,width=1024,scrollbars=1');return false;">Edit</a></#if>
                </td>
                <td width="5%">${mercuryHeader.completeCount} / ${mercuryHeader.totalCount}</td>
                <td width="1%">&nbsp;</td>
                <td align="left" style="cursor:hand; cursor:pointer;white-space: nowrap;" onclick="displayTaskList('${mercuryHeader.mercuryId}')" onmouseover="this.setAttribute('class', 'borderOnly');this.style.backgroundColor='lightgray'" onmouseout="this.setAttribute('class', 'none');this.style.backgroundColor='#dfdfdf'">${mercuryHeader.title}</td>
                <td width="1%">&nbsp;</td>
                <td align="left">${mercuryHeader.createdBy!}</td>
                <td width="1%">&nbsp;</td>
                <td align="center"><#if mercuryHeader.overdue><font color="red">YES</font></#if></td>
                <td width="1%">&nbsp;</td>
                <td align="center" style="white-space: nowrap;"><#if mercuryHeader.hasEta>${mercuryHeader.lastEta!?string["yyyy-MM-dd"]}</#if></td>
            </tr>
        </#list>
    </table>
<#else>
    No Ongoing Project Found!
</#if>