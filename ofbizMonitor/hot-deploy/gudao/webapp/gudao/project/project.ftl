<style type="text/css">
table .border{
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
tr  td .border{
border: 1px solid #333333;
display:table-cell;
padding: 0px 10px 0px 10px;
}
tr  td .borderOnly{
border: 1px solid #333333;
display:table-cell;
}
</style>

<script type="text/javascript">
    var prevId= null;
    var prevColor = null;
    function getTaskTab(id, childId, treeType) {
        if (prevId != null) {
            document.getElementById(prevId).style.backgroundColor=prevColor;
        }

        prevColor=document.getElementById(id).style.backgroundColor;
        document.getElementById(id).style.backgroundColor='#dfdfdf';
        <!--document.getElementById("TASKRESULT").innerHTML=str;-->
        prevId = id;

        var xmlhttp;

        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp=new XMLHttpRequest();
        } else {// code for IE6, IE5
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                document.getElementById("TASKRESULT").innerHTML=xmlhttp.responseText;
                document.getElementById("TASKRESULT").style.backgroundColor='#dfdfdf';
            }
        }
        xmlhttp.open("GET","<@ofbizUrl>getProjectListTab?businessTreeId="+childId+"&businessTreeType="+treeType+"</@ofbizUrl>",true);
        xmlhttp.send();
        document.getElementById("DIV_TASK_RESULT_LIST").style.display = 'none';
    }

    

    function displayTaskList(mercuryId) {
        document.getElementById("DIV_TASK_RESULT_LIST").style.display = 'block';
        var xmlhttp;

        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp=new XMLHttpRequest();
        } else {// code for IE6, IE5
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                document.getElementById("TASKRESULTLIST").innerHTML=xmlhttp.responseText;
                document.getElementById("TASKRESULTLIST").style.backgroundColor='#dfdfdf';
            }
        }
    xmlhttp.open("GET","<@ofbizUrl>getProjectTaskList?mercuryId="+mercuryId+"</@ofbizUrl>",true);
    xmlhttp.send();
    }

    function updateMercuryItemStatus(idForm) {
        document.getElementById(idForm).submit(function() {
            $.ajax({
                type: document.getElementById(idForm).attr('method'),
                url: document.getElementById(idForm).attr('action'),
                data: document.getElementById(idForm).serialize(), // serializes the form's elements.
                success: function(data) {
                }
            });

            event.preventDefault(); // avoid to execute the actual submit of the form.
            return false;
        });
    }

</script>

<#if isAdmin>
    <div class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3">Filter</li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body" align="left">
            <form action="<@ofbizUrl>project</@ofbizUrl>" method="post" name="project">
                <select name="filter">
                    <option value="ALL">All</option>
                    <option value="">--------</option>
                    <option value="${userLoginId}">${userLoginId}</option>
                    <option value="">--------</option>
                    <#list userLoginList as userLoginGV>
                        <option value="${userLoginGV.createdBy}" <#if filterInput == userLoginGV.createdBy>SELECTED</#if>>${userLoginGV.createdBy}</option>
                    </#list>
                </select>
                <br class="clear"/>
                <#if isHeadquarter || isAdmin>
                    <select name = "treeType">
                        <option value="PROFILE" <#if treeType == "PROFILE">SELECTED</#if>>Profile</option>
                        <option value="DEPARTMENT" <#if treeType == "DEPARTMENT">SELECTED</#if>>Department</option>
                    </select>
                </#if>
                <input type="submit" value="Apply" class="smallSubmit"/>
            </form>
        </div>
    </div>
</#if>

<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Project Task</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="left">
    <#--
        <a class="buttontext" href="<@ofbizUrl>editProject</@ofbizUrl>" onclick="window.open(this.href,'','height=768,width=1024,scrollbars=1');return false;">Create New</a>
        -->
        <a class="buttontext" href="<@ofbizUrl>createGudaoProject</@ofbizUrl>" onclick="window.open(this.href,'','height=768,width=1024,scrollbars=1');return false;">Create New</a>
        
        <table class="border">
            <tr class="border">
                <td class="border">Category</td>
                <td class="border">Sub Category</td>
                <td class="border">Task</td>
            </tr>
            <#assign taskCol = true>
            <#list businessList as businessList>
                <#assign parentRow = true>
                <#list businessList.childList as childList>
                    <tr class="border">
                        <#if parentRow>
                            <td class="border" rowspan="${businessList.childSize}" style="vertical-align:middle">${businessList.parent.title}</td>
                            <#assign parentRow = !parentRow>
                        </#if>
                        <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="${childList.childId}" onclick="getTaskTab(this.id, '${childList.childId}' ,'${treeType}')">${childList.title}</td>
                        <#if taskCol>
                            <td class="border" rowspan="${allChildListSize}" style="vertical-align:top;" id="TASKRESULT">Click the Sub Category to show Project List here</td>
                            <#assign taskCol = !taskCol>
                        </#if>
                    </tr>
                </#list>
            </#list>
        </table>
    <div>
</div>

<br class="clear"/>
<div class="screenlet" style="display: none;" id="DIV_TASK_RESULT_LIST">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Project Task Detail</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="left" id="TASKRESULTLIST">
Project Task List Here
    </div>
</div>


<br/>
${parameters}
<br/>
<br/>

<div>this is businessList</div>
${businessList}
<br/>
${userLoginList}
<br/>


<#list userLoginList as test>

${test}
<br />
<br />

</#list>