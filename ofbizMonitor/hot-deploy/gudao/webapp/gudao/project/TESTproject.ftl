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

 #percent
    {
        border-top-width:thin;
        border-right-width:thin;
        border-bottom-width:thin;
        border-left-width:thin;
        border-top-style:solid;
        border-right-style:solid;
        border-bottom-style:solid;
        border-left-style:solid;
        height:7px;
        width:300px;
        vertical-align:middle;
    }
    #percent #in
    {
        background-color:#ffddff;
        height:7px;
        width:0px;
    }
    #percent #in_font
    {
        height:7px;
        width:300px;
        text-align:center;
    }

/*绝对定位 + z-index */ 
.progressbar_3{ 
    background-color:#eee; 
    color:#222; 
    height:16px; 
    width:150px; 
    border:1px solid #bbb; 
    text-align:center; 
    position:relative; 
} 
.progressbar_3 .bar { 
    background-color:#6CAF00; 
    height:16px; 
    width:0; 
    position:absolute; 
    left:0; 
    top:0; 
    z-index:10; 
} 
.progressbar_3 .text { 
    height:16px; 
    position:absolute; 
    left:0; 
    top:0; 
    width:100%; 
    line-height:16px; 
     
    z-index:100; 
} 

        .progress {
          width: 150px;
          background: #ddd;
        }
        .curRate {
          width: 75%;
          background: #f30;
        }
        .round-conner {
          height: 10px;
          border-radius: 15px;
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

    var xmlhttp;
    function loadXMLDoc(url)
    {
    xmlhttp=null;
    if (window.XMLHttpRequest)
      {// code for Firefox, Opera, IE7, etc.
      xmlhttp=new XMLHttpRequest();
      }
    else if (window.ActiveXObject)
      {// code for IE6, IE5
      xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
    if (xmlhttp!=null)
      {
      xmlhttp.onreadystatechange=state_Change;
      xmlhttp.open("GET",url,true);
      xmlhttp.send(null);
      }
    else
      {
      alert("Your browser does not support XMLHTTP.");
      }
    }


    function state_Change()
    {
    if (xmlhttp.readyState==4)
      {// 4 = "loaded"
      if (xmlhttp.status==200)
        {// 200 = "OK"
        document.getElementById('TASKRESULT').innerHTML=xmlhttp.responseText;
        }
      else
        {
        alert("Problem retrieving data:" + xmlhttp.statusText);
        }
      }
    }

     i = 0;
                function start() {
                    tm = setInterval("begin()",100);
                }
                function begin() {
                    i += 1;
                    if (i <= 100) {
                        document.getElementById("in").style.width = i + "%";
                        document.getElementById("in").innerHTML = i + "%";
                    }
                    else {
                        clearInterval(tm);
                        document.getElementById("in").style.width = 100 + "%";
                        document.getElementById("in").innerHTML = 100 + "%";
                        }
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
        <a class="buttontext" href="<@ofbizUrl>editProject</@ofbizUrl>" onclick="window.open(this.href,'','height=768,width=1024,scrollbars=1');return false;">Create New</a>
        <table class="border">
            <tr class="border">
                <td class="border">Category</td>
                <td class="border">Sub Category</td>
                <td class="border">Task</td>
            </tr>
            <tr class="border">
                <td class="border" rowspan="3" style="vertical-align:middle">人事行政部</td>
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="11000" onclick="changeTaskResult(this.id, '11000' ,'DEPARTMENT')">招商部</td>
                <td align="left" class="border" rowspan="11" style="vertical-align:top" id="TASKRESULT">Click the Sub Category to show Project List here
                
            <div>
            <body onload="start();">
            <div id="percent">
            <div id="in" style="width:10%">10%</div>
            </div>
            </body>
            </div>
            <br />
    
            <br />
            <div class="progressbar_3"> 
                <div class="text">50%</div> 
                <div class="bar" style="width: 50%;"></div> 
            </div> 
            <br />
            <div class="progressbar_3"> 
                <div class="text">80%</div> 
                <div class="bar" style="width: 80%;"></div> 
            </div> 
            <br />
            <div class="progressbar_3"> 
                <div class="text">100%</div> 
                <div class="bar" style="width: 100%;"></div> 
            </div> 
            <br />
            <div class="progress round-conner">
                <div class="curRate round-conner"></div>
            </div>

            <iframe src="" style="width: 500px; height: 500px"></iframe>






                </iframe>
                </td>
            </tr>
            <tr class="border">
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="12000" onclick="changeTaskResult(this.id, '12000' ,'DEPARTMENT')">人事部</td>
            </tr>
            <tr class="border">
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="13000" onclick="changeTaskResult(this.id, '13000' ,'DEPARTMENT')">行政部</td>
            </tr>
            <tr class="border">
                <td class="border" rowspan="3" style="vertical-align:middle">客户服务部</td>
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="21000" onclick="loadXMLDoc('/images/jquery/plugins/validate/new_file1.txt')">青铜部</td>
            </tr>
            <tr class="border">
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="22000" onclick="loadXMLDoc('/images/jquery/plugins/validate/new_file.txt')">白银部</td>
            </tr>
            <tr class="border">
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="23000" onclick="loadXMLDoc('../target/TESTtarget.ftl')">黄金部</td>
            </tr>
            <tr class="border">
                <td class="border" rowspan="3" style="vertical-align:middle">系统数据部</td>
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="31000" onclick="changeTaskResult(this.id, '31000' ,'DEPARTMENT')">技术部</td>
            </tr>
            <tr class="border">
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="32000" onclick="changeTaskResult(this.id, '32000' ,'DEPARTMENT')">数据部</td>
            </tr>
            <tr class="border">
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="33000" onclick="changeTaskResult(this.id, '33000' ,'DEPARTMENT')">财务部</td>
            </tr>
            <tr class="border">
                <td class="border" rowspan="2" style="vertical-align:middle">业务支持部</td>
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="41000" onclick="changeTaskResult(this.id, '41000' ,'DEPARTMENT')">开发部</td>
            </tr>
            <tr class="border">
                <td class="border" style="vertical-align:middle;cursor:hand; cursor:pointer;" id="42000" onclick="changeTaskResult(this.id, '42000' ,'DEPARTMENT')">销售部</td>
            </tr>
        </table>    
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
                            <td class="border" rowspan="${allChildListSize}" style="vertical-align:top;" id="TASKRESULT">Click the Sub Category to show Project List qhere1
                            <iframe  class="border" rowspan="${allChildListSize} src="test.html" width="200" height="200"></iframe>
                        
                            </td>

                        <iframe src="test.html" width="200" height="200"></iframe>
                            <#assign taskCol = !taskCol>
                        </#if>
                    </tr>
                </#list>
            </#list>
        </table>
    </div>
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