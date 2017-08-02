<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>INSPINIA | Calendar</title>

    <link href="/images/project_demo/css/bootstrap.min.css" rel="stylesheet">
    <link href="/images/project_demo/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/images/project_demo/css/plugins/iCheck/custom.css" rel="stylesheet">

<!--     <link href="/images/project_demo/css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="/images/project_demo/css/plugins/fullcalendar/fullcalendar.print.css" rel='stylesheet' media='print'> -->

        <!-- Toastr style -->
    <!-- <link href="/images/project_demo/css/plugins/toastr/toastr.min.css" rel="stylesheet"> -->

        <!-- Gritter -->
    <!-- <link href="/images/project_demo/js/plugins/gritter/jquery.gritter.css" rel="stylesheet"> -->


    <link href="/images/project_demo/css/plugins/basictable-master/basictable.css" rel="stylesheet">
    <link href="/images/project_demo/css/plugins/basictable-master/basictable_style.css" rel="stylesheet">

    <link href="/images/project_demo/css/animate.css" rel="stylesheet">
    <!-- <link href="/images/project_demo/css/style.css" rel="stylesheet"> -->
    <link href="/images/project_demo/css/style2_7_1.css" rel="stylesheet">
    <!-- <link href="/images/project_demo/css/style_project_demo.css" rel="stylesheet"> -->

</head>

<body class="mini-navbar">
<!-- <body> -->

<div id="wrapper">

    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav metismenu" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element"> <span>
                                <img alt="image" class="img-circle" src="/images/project_demo/img/pan.shi.jpg" />
                                 </span>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear"> <span class="block m-t-xs"> <strong class="font-bold">${userLoginId}</strong>
                                 </span> <span class="text-muted text-xs block">Art Director <b class="caret"></b></span> </span> </a>
                        <ul class="dropdown-menu animated fadeInRight m-t-xs">
                            <li><a href="#">Profile</a></li>
                            <li><a href="#">Contacts</a></li>
                            <li><a href="#">Mailbox</a></li>
                            <li class="divider"></li>
                            <li><a href="/gudao/control/logout">Logout</a></li>
                        </ul>
                    </div>
                    <div class="logo-element">
                        IN+
                    </div>
                </li>
                <!-- 20170405 公司架构 -->
                <li class="active">
                    <a href="project_demo#"><i class="fa fa-sitemap"></i> <span class="nav-label">Category</span><span class="fa arrow"></span></a>

                    <ul class="nav nav-second-level">
                        <#list businessList as businessList>
                        <li>
                            <a href="#">${businessList.parent.title}<span class="fa arrow"></span></a>

                            <ul class="nav nav-third-level">
                                <#list businessList.childList as childList>
                                    <li >
                                        <a id="${childList.childId}"  onclick="getTaskTab(this.id, '${childList.childId}' ,'${treeType}')" href='<@ofbizUrl>projects_Technical_Department_1?businessTreeId=${childList.childId}&businessTreeType=${treeType}</@ofbizUrl>#'>${childList.title}</a>
                                    </li>
                                </#list>
                            </ul>
                        </#list>
                        </li>
                    </ul>
                </li>

            </ul>

        </div>
    </nav>

    <div id="page-wrapper" class="gray-bg">
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">
                    <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
                    <form role="search" class="navbar-form-custom" action="search_results.html">
                        <div class="form-group">
                            <input type="text" placeholder="Search for something..." class="form-control" name="top-search" id="top-search">
                        </div>
                    </form>
                </div>
                <ul class="nav navbar-top-links navbar-right">
                    <li>
                        <span class="m-r-sm text-muted welcome-message">Welcome to GUDAO+ Admin Theme.</span>
                    </li>
                    <li class="dropdown">
                        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                            <i class="fa fa-envelope"></i>  <span class="label label-warning"></span>
                        </a>
                        <ul class="dropdown-menu dropdown-messages">
                            
                            <li class="divider"></li>
                            <li>
                                <div class="text-center link-block">
                                    <a href="#">
                                        <i class="fa fa-envelope"></i> <strong>邮箱开发中..</strong>
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                            <i class="fa fa-bell"></i>  <span class="label label-primary"></span>
                        </a>
                        <ul class="dropdown-menu dropdown-alerts">
                            
                            <li class="divider"></li>
                            <li>
                                <div class="text-center link-block">
                                    <a href="#">
                                        <strong>消息</strong>
                                        <i class="fa fa-angle-right"></i>
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </li>


                    <li>
                        <a href="/gudao/control/logout">
                            <i class="fa fa-sign-out"></i> Log out
                        </a>
                    </li>
                </ul>

            </nav>
        </div>
        <!-- <div class="row wrapper border-bottom white-bg page-heading">
            <div class="col-lg-8">
                <h2>Calendar</h2>
                <ol class="breadcrumb">
                    <li>
                        <a href="index.html">Home</a>
                    </li>
                    <li>
                        Extra pages
                    </li>
                    <li class="active">
                        <strong>Calendar</strong>
                    </li>
                </ol>
            </div>
        </div> -->
        <div class="wrapper wrapper-content">
            <div class="row animated fadeInDown">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>日常任务表</h5>
                            <div class="ibox-tools">
                                <a class="collapse-link">
                                    <i class="fa fa-chevron-up"></i>
                                </a>
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    <i class="fa fa-wrench"></i>
                                </a>
                                <ul class="dropdown-menu dropdown-user">
                                    <li><a href="#">Config option 1</a>
                                    </li>
                                    <li><a href="#">Config option 2</a>
                                    </li>
                                </ul>
                                <a class="close-link">
                                    <i class="fa fa-times"></i>
                                </a>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div id="calendar">
                                  <table id="table-two-axis" class="two-axis">
                                    <thead>
                                      <tr>
                                        <th>Name</th>
                                        <th>Monday</th>
                                        <th>Tuesday</th>
                                        <th>Wednesday</th>
                                        <th>Thursday</th>
                                        <th>Friday</th>
                                      </tr>
                                    </thead>
                                    <tbody>
                                    <#list dailyTaskRecord5 as dailyTask>
                                    <!-- ${dailyTask.completedDate?string("EEEE")}
                                    ${dailyTask} -->
                                    </#list>
                                    <!-- ${userLoginId} -->
                                    <!-- ${criteria} -->
<!-- 
                                    ${dayOfWeek}
                                    ${now}
                                    ${thisMonday}
                                    <br />
                                    <br />
                                    ${thisMonday?string("EEEE")}
                                    ${dayOfWeek4} -->

                                    <#assign userpro="TOP利润表 ">
                                    <!-- ${userpro} -->

                                    <#list list as list>

                                    <!-- ${list} -->
                                      <tr>
                                        
                                        <td><strong>${list.owner}</strong></td>
                                        <td style="display:none;"></td>
                                        
                                        <td>
                                        <!-- <#if '${list.monday!}' == '1'> -->
                                        <#list dailyTaskRecord5 as dailyTask>
                                        <!-- <#if '${dailyTask.taskName!}' == "TOP&#21033;&#28070;&#34920;"> -->
                                                <!-- <#if '${dailyTask.taskName!}' == '${list.taskName!}'> -->
                                                <i class="fa fa-check"></i>
                                                <!-- </#if> -->
                                        <!-- <#else> -->
                                        <!-- ${dailyTask.completedDate?string("EEEE")}
                                        ${dailyTask.completedDate} -->


                                            <!-- <#if '${dailyTask.completedDate?string("EEEE")}' == '${thisMonday?string("EEEE")}'> -->
                                                <!-- <#if '${dailyTask.taskName!}' == '${list.taskName!}'> -->
                                                <i class="fa fa-check"></i>
                                                <!-- </#if> -->
                                            <!-- </#if> -->
                                        <!-- </#if> -->
                                        </#list>
                                        ${list.description}

                                        <a class="status_tr_button">
                                        <!-- <#if '${list.occurrenceValue!}' == '12345'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1-5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '135'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1 3 5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '145'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1 4 5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '1'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button"> 1 </button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == 'week'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button"> 周 </button>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </a>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </td>
                                        <td>
                                        <!-- <#if '${list.tuesday!}' == '1'> -->
                                        <#list dailyTaskRecord5 as dailyTask>
                                        <!-- <#if '${dailyTask.taskName!}' == "TOP&#21033;&#28070;&#34920;"> -->
                                                <!-- <#if '${dailyTask.taskName!}' == '${list.taskName!}'> -->
                                                <i class="fa fa-check"></i>
                                                <!-- </#if> -->
                                        <!-- <#else> -->

                                            <!-- <#if '${dailyTask.completedDate?string("EEEE")}' == '${thisTuesday?string("EEEE")}'> -->
                                                <!-- <#if '${dailyTask.taskName}' == '${list.taskName}'> -->
                                                <i class="fa fa-check"></i>
                                                <!-- </#if> -->
                                            <!-- </#if> -->
                                        <!-- </#if> -->
                                        </#list>
                                        ${list.description}
                                        <a class="status_tr_button">
                                        <!-- <#if '${list.occurrenceValue!}' == '12345'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1-5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '25'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">2 5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '2'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button"> 2 </button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == 'week'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button"> 周 </button>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </a>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </td>
                                        <td>
                                        <!-- <#if '${list.wednesday!}' == '1'> -->
                                        <#list dailyTaskRecord5 as dailyTask>
                                        <!-- <#if '${dailyTask.taskName!}' == "TOP&#21033;&#28070;&#34920;"> -->
                                                <!-- <#if '${dailyTask.taskName!}' == '${list.taskName!}'> -->
                                                <i class="fa fa-check"></i>
                                                <!-- </#if> -->
                                        <!-- <#else> -->

                                            <!-- <#if '${dailyTask.completedDate?string("EEEE")}' == '${thisWednesday?string("EEEE")}'> -->
                                                <!-- <#if '${dailyTask.taskName}' == '${list.taskName}'> -->
                                                <i class="fa fa-check"></i>
                                                <!-- </#if> -->
                                            <!-- </#if> -->
                                        <!-- </#if> -->
                                        </#list>
                                        ${list.description}

                                        <a class="status_tr_button">
                                        <!-- <#if '${list.occurrenceValue!}' == '12345'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1-5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '135'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1 3 5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '3'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button"> 3 </button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == 'week'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button"> 周 </button>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </a>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </td>
                                        <td>
                                        <!-- <#if '${list.thursday!}' == '1'> -->

                                        <#list dailyTaskRecord5 as dailyTask>
                                        <!-- <#if '${dailyTask.taskName!}' == "TOP&#21033;&#28070;&#34920;"> -->
                                                <!-- <#if '${dailyTask.taskName!}' == '${list.taskName!}'> -->
                                                <i class="fa fa-check"></i>
                                                <!-- </#if> -->
                                        <!-- <#else> -->

                                            <!-- <#if '${dailyTask.completedDate?string("EEEE")}' == '${thisThursday?string("EEEE")}'> -->
                                                <!-- <#if '${dailyTask.taskName}' == '${list.taskName}'> -->
                                                <i class="fa fa-check"></i>
                                                <!-- </#if> -->
                                            <!-- </#if> -->
                                        <!-- </#if> -->
                                        </#list>
                                        ${list.description}

                                        <a class="status_tr_button">
                                        <!-- <#if '${list.occurrenceValue!}' == '12345'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1-5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '145'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1 4 5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '4'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button"> 4 </button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == 'week'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button"> 周 </button>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </a>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </td>
                                        <td>
                                        <!-- <#if '${list.friday!}' == '1'> -->
                                        <#list dailyTaskRecord5 as dailyTask>
                                        <!-- <#if '${dailyTask.taskName!}' == "TOP&#21033;&#28070;&#34920;"> -->
                                                <!-- <#if '${dailyTask.taskName!}' == '${list.taskName!}'> -->
                                                <i class="fa fa-check"></i>
                                                <!-- </#if> -->
                                        <!-- <#else> -->

                                            <!-- <#if '${dailyTask.completedDate?string("EEEE")}' == '${thisFriday?string("EEEE")}'> -->
                                                <!-- <#if '${dailyTask.taskName}' == '${list.taskName}'> -->
                                                <i class="fa fa-check"></i>
                                                <!-- </#if> -->
                                            <!-- </#if> -->
                                        <!-- </#if> -->
                                        </#list>
                                        ${list.description}
                                        <a class="status_tr_button">
                                        <!-- <#if '${list.occurrenceValue!}' == '12345'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1-5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '135'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1 3 5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '145'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">1 4 5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == '25'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button">2 5</button>
                                        <!-- <#elseif '${list.occurrenceValue!}' == 'week'> -->
                                            <button data-toggle="button" class="btn btn-white btn-xs pull-right" disabled="disabled" id="button_disabled" type="button"> 周 </button>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </a>
                                        <!-- <#else> -->
                                          
                                        <!-- </#if> -->
                                        </td>
                                      </tr>

                                    </#list>


                                    </tbody>
                                  </table>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div class="footer">
            <div class="pull-right">
                10GB of <strong>250GB</strong> Free.
            </div>
            <div>
                <strong>Copyright</strong> GUDAO Company &copy; 2010-2017
            </div>
        </div>
    </div>

</div>



<!-- Mainly scripts -->
<!-- <script src="/images/project_demo/js/plugins/fullcalendar/moment.min.js"></script> -->
<script src="/images/project_demo/js/jquery-3.1.1.min.js"></script>
<script src="/images/project_demo/js/bootstrap.min.js"></script>
<script src="/images/project_demo/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="/images/project_demo/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

<!-- Flot -->
<script src="/images/project_demo/js/plugins/flot/jquery.flot.js"></script>
<script src="/images/project_demo/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
<script src="/images/project_demo/js/plugins/flot/jquery.flot.spline.js"></script>
<script src="/images/project_demo/js/plugins/flot/jquery.flot.resize.js"></script>
<script src="/images/project_demo/js/plugins/flot/jquery.flot.pie.js"></script>

<!-- Custom and plugin javascript -->
<script src="/images/project_demo/js/inspinia.js"></script>
<script src="/images/project_demo/js/plugins/pace/pace.min.js"></script>

<!-- jQuery UI  -->
<script src="/images/project_demo/js/plugins/jquery-ui/jquery-ui.min.js"></script>

<!-- iCheck -->
<script src="/images/project_demo/js/plugins/iCheck/icheck.min.js"></script>

<!-- Full Calendar -->
<!-- <script src="/images/project_demo/js/plugins/fullcalendar/fullcalendar.min.js"></script> -->
<!-- <script src="/images/project_demo/js/plugins/fullcalendar/fullcalendar.js"></script> -->

<!-- GITTER -->
<!-- <script src="/images/project_demo/js/plugins/gritter/jquery.gritter.min.js"></script> -->

<!-- Sparkline -->
<script src="/images/project_demo/js/plugins/sparkline/jquery.sparkline.min.js"></script>

<!-- Sparkline demo data  -->
<!-- <script src="/images/project_demo/js/demo/sparkline-demo.js"></script> -->

<!-- ChartJS-->
<!-- <script src="/images/project_demo/js/plugins/chartJs/Chart.min.js"></script> -->

<!-- Toastr -->
<script src="/images/project_demo/js/plugins/toastr/toastr.min.js"></script>

<!-- Peity -->
<script src="/images/project_demo/js/plugins/peity/jquery.peity.min.js"></script>

<!-- Rickshaw -->
<script src="/images/project_demo/js/plugins/rickshaw/vendor/d3.v3.js"></script>
<script src="/images/project_demo/js/plugins/rickshaw/rickshaw.min.js"></script>

<!-- Touch Punch - Touch Event Support for jQuery UI -->
    <script src="/images/project_demo/js/plugins/touchpunch/jquery.ui.touch-punch.min.js"></script>


<script src="/images/project_demo/js/plugins/basictable-master/jquery.basictable.min.js"></script>

<div class="theme-config">
    <div class="theme-config-box">
        <div class="spin-icon">
            <i class="fa fa-cogs fa-spin"></i>
        </div>
        <div class="skin-settings">
            <div class="title">界面设置<br>
            <small style="text-transform: none;font-weight: 400">
                设置界面
            </small></div>
            <div class="setings-item">
                    <span>
                        菜单栏
                    </span>

                <div class="switch">
                    <div class="onoffswitch">
                        <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="collapsemenu">
                        <label class="onoffswitch-label" for="collapsemenu">
                            <span class="onoffswitch-inner"></span>
                            <span class="onoffswitch-switch"></span>
                        </label>
                    </div>
                </div>
            </div>
            <div class="setings-item">
                    <span>
                        隐藏左侧菜单栏
                    </span>

                <div class="switch">
                    <div class="onoffswitch">
                        <input type="checkbox" name="fixedsidebar" class="onoffswitch-checkbox" id="fixedsidebar">
                        <label class="onoffswitch-label" for="fixedsidebar">
                            <span class="onoffswitch-inner"></span>
                            <span class="onoffswitch-switch"></span>
                        </label>
                    </div>
                </div>
            </div>
            <div class="setings-item">
                    <span>
                        Top navbar
                    </span>

                <div class="switch">
                    <div class="onoffswitch">
                        <input type="checkbox" name="fixednavbar" class="onoffswitch-checkbox" id="fixednavbar" disabled="disabled">
                        <label class="onoffswitch-label" for="fixednavbar">
                            <span class="onoffswitch-inner"></span>
                            <span class="onoffswitch-switch"></span>
                        </label>
                    </div>
                </div>
            </div>
            <div class="setings-item">
                    <span>
                        Top navbar v.2
                        <br>
                        <small>*Primary layout</small>
                    </span>

                <div class="switch">
                    <div class="onoffswitch">
                        <input type="checkbox" name="fixednavbar2" class="onoffswitch-checkbox" id="fixednavbar2" disabled="disabled">
                        <label class="onoffswitch-label" for="fixednavbar2">
                            <span class="onoffswitch-inner"></span>
                            <span class="onoffswitch-switch"></span>
                        </label>
                    </div>
                </div>
            </div>
            <div class="setings-item">
                    <span>
                        Boxed layout
                    </span>

                <div class="switch">
                    <div class="onoffswitch">
                        <input type="checkbox" name="boxedlayout" class="onoffswitch-checkbox" id="boxedlayout">
                        <label class="onoffswitch-label" for="boxedlayout">
                            <span class="onoffswitch-inner"></span>
                            <span class="onoffswitch-switch"></span>
                        </label>
                    </div>
                </div>
            </div>
            <div class="setings-item">
                    <span>
                        Fixed footer
                    </span>

                <div class="switch">
                    <div class="onoffswitch">
                        <input type="checkbox" name="fixedfooter" class="onoffswitch-checkbox" id="fixedfooter">
                        <label class="onoffswitch-label" for="fixedfooter">
                            <span class="onoffswitch-inner"></span>
                            <span class="onoffswitch-switch"></span>
                        </label>
                    </div>
                </div>
            </div>

            <div class="setings-item">
                <div>
                <ul>
                    <li id="right-bar_description">
                        <button data-toggle="button" class="btn btn-white btn-xs" disabled="disabled" id="button_disabled" type="button">周</button> 每周一次
                    </li>
                    <li id="right-bar_description">
                        <button data-toggle="button" class="btn btn-white btn-xs" disabled="disabled" id="button_disabled" type="button">月</button> 每月一次
                    </li>
                    <li id="right-bar_description">
                        <button data-toggle="button" class="btn btn-white btn-xs" disabled="disabled" id="button_disabled" type="button">1-5</button> 周一至周五
                    </li>
                    <li id="right-bar_description">
                        <button data-toggle="button" class="btn btn-white btn-xs" disabled="disabled" id="button_disabled" type="button">1 3</button> 周一、周三
                    </li>
                    <li id="right-bar_description">
                        <button data-toggle="button" class="btn btn-white btn-xs" disabled="disabled" id="button_disabled" type="button">二</button> 周二
                    </li>
                </ul>

                </div>
            </div>


        </div>
    </div>
</div>
<script>
    // Config box

    // Enable/disable fixed top navbar
    $('#fixednavbar').click(function (){
        if ($('#fixednavbar').is(':checked')){
            $(".navbar-static-top").removeClass('navbar-static-top').addClass('navbar-fixed-top');
            $("body").removeClass('boxed-layout');
            $("body").addClass('fixed-nav');
            $('#boxedlayout').prop('checked', false);

            if (localStorageSupport){
                localStorage.setItem("boxedlayout",'off');
            }

            if (localStorageSupport){
                localStorage.setItem("fixednavbar",'on');
            }
        } else{
            $(".navbar-fixed-top").removeClass('navbar-fixed-top').addClass('navbar-static-top');
            $("body").removeClass('fixed-nav');
            $("body").removeClass('fixed-nav-basic');
            $('#fixednavbar2').prop('checked', false);

            if (localStorageSupport){
                localStorage.setItem("fixednavbar",'off');
            }

            if (localStorageSupport){
                localStorage.setItem("fixednavbar2",'off');
            }
        }
    });

    // Enable/disable fixed top navbar
    $('#fixednavbar2').click(function (){
        if ($('#fixednavbar2').is(':checked')){
            $(".navbar-static-top").removeClass('navbar-static-top').addClass('navbar-fixed-top');
            $("body").removeClass('boxed-layout');
            $("body").addClass('fixed-nav').addClass('fixed-nav-basic');
            $('#boxedlayout').prop('checked', false);

            if (localStorageSupport){
                localStorage.setItem("boxedlayout",'off');
            }

            if (localStorageSupport){
                localStorage.setItem("fixednavbar2",'on');
            }
        } else {
            $(".navbar-fixed-top").removeClass('navbar-fixed-top').addClass('navbar-static-top');
            $("body").removeClass('fixed-nav').removeClass('fixed-nav-basic');
            $('#fixednavbar').prop('checked', false);

            if (localStorageSupport){
                localStorage.setItem("fixednavbar2",'off');
            }
            if (localStorageSupport){
                localStorage.setItem("fixednavbar",'off');
            }
        }
    });

    // Enable/disable fixed sidebar
    $('#fixedsidebar').click(function (){
        if ($('#fixedsidebar').is(':checked')){
            $("body").addClass('fixed-sidebar');
            $('.sidebar-collapse').slimScroll({
                height: '100%',
                railOpacity: 0.9
            });

            if (localStorageSupport){
                localStorage.setItem("fixedsidebar",'on');
            }
        } else{
            $('.sidebar-collapse').slimscroll({destroy: true});
            $('.sidebar-collapse').attr('style', '');
            $("body").removeClass('fixed-sidebar');

            if (localStorageSupport){
                localStorage.setItem("fixedsidebar",'off');
            }
        }
    });

    // Enable/disable collapse menu
    $('#collapsemenu').click(function (){
        if ($('#collapsemenu').is(':checked')){
            $("body").addClass('mini-navbar');
            SmoothlyMenu();

            if (localStorageSupport){
                localStorage.setItem("collapse_menu",'on');
            }

        } else{
            $("body").removeClass('mini-navbar');
            SmoothlyMenu();

            if (localStorageSupport){
                localStorage.setItem("collapse_menu",'off');
            }
        }
    });

    // Enable/disable boxed layout
    $('#boxedlayout').click(function (){
        if ($('#boxedlayout').is(':checked')){
            $("body").addClass('boxed-layout');
            $('#fixednavbar').prop('checked', false);
            $('#fixednavbar2').prop('checked', false);
            $(".navbar-fixed-top").removeClass('navbar-fixed-top').addClass('navbar-static-top');
            $("body").removeClass('fixed-nav');
            $("body").removeClass('fixed-nav-basic');
            $(".footer").removeClass('fixed');
            $('#fixedfooter').prop('checked', false);

            if (localStorageSupport){
                localStorage.setItem("fixednavbar",'off');
            }

            if (localStorageSupport){
                localStorage.setItem("fixednavbar2",'off');
            }

            if (localStorageSupport){
                localStorage.setItem("fixedfooter",'off');
            }


            if (localStorageSupport){
                localStorage.setItem("boxedlayout",'on');
            }
        } else{
            $("body").removeClass('boxed-layout');

            if (localStorageSupport){
                localStorage.setItem("boxedlayout",'off');
            }
        }
    });

    // Enable/disable fixed footer
    $('#fixedfooter').click(function (){
        if ($('#fixedfooter').is(':checked')){
            $('#boxedlayout').prop('checked', false);
            $("body").removeClass('boxed-layout');
            $(".footer").addClass('fixed');

            if (localStorageSupport){
                localStorage.setItem("boxedlayout",'off');
            }

            if (localStorageSupport){
                localStorage.setItem("fixedfooter",'on');
            }
        } else{
            $(".footer").removeClass('fixed');

            if (localStorageSupport){
                localStorage.setItem("fixedfooter",'off');
            }
        }
    });

    // SKIN Select
    $('.spin-icon').click(function (){
        $(".theme-config-box").toggleClass("show");
    });

    // Default skin
    $('.s-skin-0').click(function (){
        $("body").removeClass("skin-1");
        $("body").removeClass("skin-2");
        $("body").removeClass("skin-3");
    });

    // Blue skin
    $('.s-skin-1').click(function (){
        $("body").removeClass("skin-2");
        $("body").removeClass("skin-3");
        $("body").addClass("skin-1");
    });

    // Inspinia ultra skin
    $('.s-skin-2').click(function (){
        $("body").removeClass("skin-1");
        $("body").removeClass("skin-3");
        $("body").addClass("skin-2");
    });

    // Yellow skin
    $('.s-skin-3').click(function (){
        $("body").removeClass("skin-1");
        $("body").removeClass("skin-2");
        $("body").addClass("skin-3");
    });

    if (localStorageSupport){
        var collapse = localStorage.getItem("collapse_menu");
        var fixedsidebar = localStorage.getItem("fixedsidebar");
        var fixednavbar = localStorage.getItem("fixednavbar");
        var fixednavbar2 = localStorage.getItem("fixednavbar2");
        var boxedlayout = localStorage.getItem("boxedlayout");
        var fixedfooter = localStorage.getItem("fixedfooter");

        if (collapse == 'on'){
            $('#collapsemenu').prop('checked','checked')
        }
        if (fixedsidebar == 'on'){
            $('#fixedsidebar').prop('checked','checked')
        }
        if (fixednavbar == 'on'){
            $('#fixednavbar').prop('checked','checked')
        }
        if (fixednavbar2 == 'on'){
            $('#fixednavbar2').prop('checked','checked')
        }
        if (boxedlayout == 'on'){
            $('#boxedlayout').prop('checked','checked')
        }
        if (fixedfooter == 'on') {
            $('#fixedfooter').prop('checked','checked')
        }
    }
</script>


<script>
$(function(){
    hebingRows(0);
})
function hebingRows(col){
    var trs = $("table tr");
    var rows = 1;
    for(var i=trs.length;i>0;i--){
        var cur = $($(trs[i]).find("td")[col]).text();
        var next = $($(trs[i-1]).find("td")[col]).text();
        if(cur==next){
            rows++;
            $($(trs[i]).find("td")[col]).remove();
        } else {
            $($(trs[i]).find("td")[col]).attr("rowspan",rows);
            rows=1;
        }
    }
}
</script>


</body>

</html>
