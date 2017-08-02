<script type="text/javascript">
function getTaskResult(id,childId, treeType, statusId) {
if (prevId != null) {
document.getElementById(prevId).style.backgroundColor=prevColor;
}

prevColor=document.getElementById(id).style.backgroundColor;
document.getElementById(id).style.backgroundColor='#dfdfdf';
prevId = id;

var xmlhttp;

if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
xmlhttp=new XMLHttpRequest();
} else {// code for IE6, IE5
xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
}
xmlhttp.onreadystatechange = function() {
if (xmlhttp.readyState==4 && xmlhttp.status==200) {
document.getElementById("TABRESULT").innerHTML=xmlhttp.responseText;
document.getElementById("TABRESULT").style.backgroundColor='#dfdfdf';
}
}
xmlhttp.open("GET","<@ofbizUrl>getProjectListByCategory?businessTreeId="+childId+"&businessTreeType="+treeType+"&statusId="+statusId+"</@ofbizUrl>",true);
xmlhttp.send();
document.getElementById("DIV_TASK_RESULT_LIST").style.display = 'none';
}

</script>

    <table>
        <tr>
            <td colspan="2" width="30%" class="border" style="cursor:hand; cursor:pointer;white-space: nowrap;<#if statusId=="ACTIVE">background-color:white;</#if>" onmouseover="this.style.backgroundColor='white'" <#if statusId!="ACTIVE">onmouseout="this.style.backgroundColor='#dfdfdf'"</#if> onclick="getTaskResult(this.id, '${businessTreeId}' ,'${businessTreeType}', 'ACTIVE')">Active</td>
            <td width="1%">&nbsp;</td>
            <!--<td width="30%" class="border" style="cursor:hand; cursor:pointer;white-space: nowrap;<#if statusId=="PENDING">background-color:white;</#if>" onmouseover="this.style.backgroundColor='white'" <#if statusId!="PENDING">onmouseout="this.style.backgroundColor='#dfdfdf'"</#if> onclick="getTaskResult(this.id, '${businessTreeId}' ,'${businessTreeType}', 'PENDING')">Pending</td>
            <td width="1%">&nbsp;</td>-->
            <td colspan="2" width="30%" class="border" style="cursor:hand; cursor:pointer;white-space: nowrap;<#if statusId=="COMPLETED">background-color:white;</#if>" onmouseover="this.style.backgroundColor='white'" <#if statusId!="COMPLETED">onmouseout="this.style.backgroundColor='#dfdfdf'"</#if> onclick="getTaskResult(this.id, '${businessTreeId}' ,'${businessTreeType}', 'COMPLETED')">Completed</td>
        </tr>
<tr>
<td colspan="5"><hr></td>
</tr>
<tr>
<td id="TABRESULT" colspan="5">
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
<td width="3%" class="border">ETA</td>
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


</td>
</tr>

    </table>
