<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

<#assign nowTimestamp = Static["org.ofbiz.base.util.UtilDateTime"].nowTimestamp()>
<head>
    <!-- 

    <link href="/images/project_demo/css/bootstrap.min.css" rel="stylesheet">
     -->
    <link href="/images/project_demo/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/images/project_demo/css/animate.css" rel="stylesheet">
    <link href="/images/project_demo/css/style2_7_1.css" rel="stylesheet">



</head>
<div id="footer">

${userLoginId}

<#list list4 as list>
<#if '${list.owner!}' == '${userLoginId}'>
  <#assign owner = '${userLoginId}'>
<#else>
</#if>
</#list>


<#if '${owner!}' == '${userLoginId}'>

<div  id="footer_show">
        <div class="small-chat-box fadeInRight animated wrapper wrapper-content">
            <div class="heading" draggable="true">
                <small class="chat-date pull-right">
                    ${nowTimestamp?string("YYYY/MM/dd")}
                </small>
                今日任务便签
            </div>
                <div class="row">

                <a id="footer_show_a" href="\gudao\control\dailyTask" style="text-decoration:underline;">
                   <p id="footer_show_a_p">任务详情页</p>
                </a>

                        <div class="ibox-content1">

                            <div class="chat-users-for">


                                    <div class="users-list">
                                        <!-- ${today}
                                        ${thisToday} -->
                                        <br />
                                        <#assign seq>
                                        <#list dailyTaskRecord6 as dailyTask6>
                                        ${dailyTask6.taskName}
                                        </#list>
                                        </#assign>
                                        <!-- ${seq} -->
                                        <#list list4 as a>

                                             <!-- ${a.taskName}
                                             ${a_index}
                                             ${list4?size} -->


                                            <#if '${seq?contains(a.taskName)?string}' == 'true'>
                                            <#else>
                                            <div class="chat-user-for animated" id="animation_box_${a_index}">
                                                <span class="pull-right label label_footer label-primary" id="upload_task_${a_index}" >完成
                                                </span>

                                                <div class="chat-user-name">
                                                    <a href="#"  id="footer_show_a">${a.taskName}</a>
                                                </div>
                                                <div style="display: none">
                                                    <form type="hidden" action="<@ofbizUrl>createDailyTaskRecord</@ofbizUrl>" method="post" name="createDailyTaskRecord" id="createDailyTaskRecordForm" id="saveReportForm" onsubmit="return saveReport();" target="targetIfr">
                                                        <input type="text" name="owner" value="${a.owner}"/>
                                                        <input type="text" name="taskName" value="${a.taskName}"/>
                                                    <button id="online_${a_index}" type="submit">完成</button>
                                                    </form>
                                                </div>
                                            </div>
                                            </#if>


                                        </#list>


                                        <iframe name="targetIfr" style="display:none"></iframe> 


                                    </div>

                                </div>
                        </div>

                </div>
    </div>
    <div id="small-chat">

            <span class="badge badge-warning pull-right">${list4?size - dailyTaskRecord6?size}</span>

            </a>
            <button class="btn btn-info btn-circle btn-lg" type="button"><i class="fa fa-check"></i>
                            </button>
    </div>
</div>


<#else>
</#if>




  <ul>
    <li>
      ${uiLabelMap.CommonCopyright} (c) 2001-${nowTimestamp?string("yyyy")} The Apache Software Foundation - <a href="http://www.apache.org" target="_blank">www.apache.org</a><br/>
      ${uiLabelMap.CommonPoweredBy} <a href="http://ofbiz.apache.org" target="_blank">Apache OFBiz</a> <#include "ofbizhome://runtime/svninfo.ftl" />
    </li>
    <li class="opposed">${nowTimestamp?datetime?string.short} -
  <a href="<@ofbizUrl>ListTimezones</@ofbizUrl>">${timeZone.getDisplayName(timeZone.useDaylightTime(), Static["java.util.TimeZone"].LONG, locale)}</a>
    </li>
  </ul>
</div>

<#if layoutSettings.VT_FTR_JAVASCRIPT?has_content>
  <#list layoutSettings.VT_FTR_JAVASCRIPT as javaScript>
    <script src="<@ofbizContentUrl>${StringUtil.wrapString(javaScript)}</@ofbizContentUrl>" type="text/javascript"></script>
  </#list>
</#if>

</div>




<script type="text/javascript">
$(document).ready(function(){
  $("button").click(function(){
  $(".small-chat-box").toggle();
  });
    $("#upload_task_0").click(function(){
    // $("#animation_box").parents("li:first").addClass("bounceOutLeft").fadeOut("slow");
    $("#animation_box_0").addClass("bounceOutLeft").fadeOut("slow");
    setTimeout("$('#online_0').click().myrefresh()",2000);
  });
  $("#upload_task_1").click(function(){
    // $("#animation_box").parents("li:first").addClass("bounceOutLeft").fadeOut("slow");
    $("#animation_box_1").addClass("bounceOutLeft").fadeOut("slow");
    setTimeout("$('#online_1').click().myrefresh()",2000);

  });
  $("#upload_task_2").click(function(){
    // $("#animation_box").parents("li:first").addClass("bounceOutLeft").fadeOut("slow");
    $("#animation_box_2").addClass("bounceOutLeft").fadeOut("slow");
    setTimeout("$('#online_2').click()",2000);

  });
    $("#upload_task_3").click(function(){
    // $("#animation_box").parents("li:first").addClass("bounceOutLeft").fadeOut("slow");
    $("#animation_box_3").addClass("bounceOutLeft").fadeOut("slow");
    setTimeout("$('#online_3').click()",2000);

  });
    $("#upload_task_4").click(function(){
    // $("#animation_box").parents("li:first").addClass("bounceOutLeft").fadeOut("slow");
    $("#animation_box_4").addClass("bounceOutLeft").fadeOut("slow");
    setTimeout("$('#online_4').click()",2000);

  });
    $("#upload_task_5").click(function(){
    // $("#animation_box").parents("li:first").addClass("bounceOutLeft").fadeOut("slow");
    $("#animation_box_5").addClass("bounceOutLeft").fadeOut("slow");
    setTimeout("$('#online_5').click()",2000);

  });
    $("#upload_task_6").click(function(){
    // $("#animation_box").parents("li:first").addClass("bounceOutLeft").fadeOut("slow");
    $("#animation_box_6").addClass("bounceOutLeft").fadeOut("slow");
    setTimeout("$('#online_6').click()",2000);

  });
    $("#upload_task_7").click(function(){
    // $("#animation_box").parents("li:first").addClass("bounceOutLeft").fadeOut("slow");
    $("#animation_box_7").addClass("bounceOutLeft").fadeOut("slow");
    setTimeout("$('#online_7').click()",2000);

  });

  function myrefresh(){ 
    window.location.reload();
    // self.opener.location.reload();
    } 
    
  function saveReport() { 


    // jquery 表单提交 
    $("#saveReportForm").ajaxSubmit(function(message) { 
    // 对于表单提交成功后处理，message为提交页面saveReport.htm的返回内容 
    }); 


    return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转 
    } 
});
</script>

</body>
</html>
