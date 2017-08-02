<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>INSPINIA | Calendar</title>

    <link href="/images/project_demo/css/bootstrap.min.css" rel="stylesheet">
    <link href="/images/project_demo/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/images/project_demo/css/plugins/iCheck/custom.css" rel="stylesheet">

    <link href="/images/project_demo/css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="/images/project_demo/css/plugins/fullcalendar/fullcalendar.print.css" rel='stylesheet' media='print'>

        <!-- Toastr style -->
    <link href="/images/project_demo/css/plugins/toastr/toastr.min.css" rel="stylesheet">

        <!-- Gritter -->
    <link href="/images/project_demo/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">


    <link href="/images/project_demo/css/plugins/basictable-master/basictable.css" rel="stylesheet">
    <link href="/images/project_demo/css/plugins/basictable-master/basictable_style.css" rel="stylesheet">

    <link href="/images/project_demo/css/animate.css" rel="stylesheet">
    <!-- <link href="/images/project_demo/css/style.css" rel="stylesheet"> -->
    <link href="/images/project_demo/css/style2_7_1.css" rel="stylesheet">
    <!-- <link href="/images/project_demo/css/style_project_demo.css" rel="stylesheet"> -->

</head>

<!-- <body class="mini-navbar"> -->
<body>

<div id="wrapper">

    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav metismenu" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element"> <span>
                                <img alt="image" class="img-circle" src="/images/project_demo/img/profile_small.jpg" />
                                 </span>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear"> <span class="block m-t-xs"> <strong class="font-bold">David Williams</strong>
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
                                    <!-- <#list dailyTaskRecord5 as dailyTask>
                                    ${dailyTask.completedDate?string("EEEE")}
                                    qqqqq
                                    </#list> -->
                                    <!-- ${criteria} -->

                                    <!-- ${dayOfWeek}
                                    ${dayOfWeek1}
                                    ${dayOfWeek4} -->
                                    <!-- ${dayOfWeek1?string.full} -->

                                    <#list list as list>

                                    <!-- ${list} -->
                                      <tr>
                                        
                                        <td><strong>${list.owner}</strong></td>
                                        <td style="display:none;"></td>
                                        
                                        <td>
                                        <!-- <#if '${list.monday!}' == '1'> -->
                                        <#list dailyTaskRecord5 as dailyTask>

                                            <!-- <#if '${dailyTask.completedDate?string("EEEE")}' == 'Monday'> -->
                                                <!-- <#if '${dailyTask.taskName}' == '${list.taskName}'> -->
                                                <i class="fa fa-check"></i>
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
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </a>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </td>
                                        <td>
                                        <!-- <#if '${list.tuesday!}' == '1'> -->
                                        <#list dailyTaskRecord5 as dailyTask>
                                            <!-- <#if '${dailyTask.completedDate?string("EEEE")}' == 'Tuesday'> -->
                                                <!-- <#if '${dailyTask.taskName}' == '${list.taskName}'> -->
                                                <i class="fa fa-check"></i>
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
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </a>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </td>
                                        <td>
                                        <!-- <#if '${list.wednesday!}' == '1'> -->
                                        <#list dailyTaskRecord5 as dailyTask>
                                            <!-- <#if '${dailyTask.completedDate?string("EEEE")}' == 'Wednesday'> -->
                                                <!-- <#if '${dailyTask.taskName}' == '${list.taskName}'> -->
                                                <i class="fa fa-check"></i>
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
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </a>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </td>
                                        <td>
                                        <!-- <#if '${list.thursday!}' == '1'> -->

                                        <#list dailyTaskRecord5 as dailyTask>
                                            <!-- <#if '${dailyTask.completedDate?string("EEEE")}' == 'Thursday'> -->
                                                <!-- <#if '${dailyTask.taskName}' == '${list.taskName}'> -->
                                                <i class="fa fa-check"></i>
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
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </a>
                                        <!-- <#else> -->
                                        <!-- </#if> -->
                                        </td>
                                        <td>
                                        <!-- <#if '${list.friday!}' == '1'> -->
                                        <#list dailyTaskRecord5 as dailyTask>
                                            <!-- <#if '${dailyTask.completedDate?string("EEEE")}' == 'Friday'> -->
                                                <!-- <#if '${dailyTask.taskName}' == '${list.taskName}'> -->
                                                <i class="fa fa-check"></i>
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

    <div class="small-chat-box fadeInRight animated wrapper wrapper-content">
            <div class="heading" draggable="true">
                <small class="chat-date pull-right">
                    ${dayOfWeek?string("YYYY/MM/dd")}
                </small>
                今日任务便签
            </div>
            <!-- <div class="content"> -->
                <div class="row">
                <!-- <div class="col-lg-12"> -->
                    <!-- <div class="ibox"> -->
                        <div class="ibox-content1">
                            <!-- <h3>To-do</h3> -->
                            <!-- <p class="small"><i class="fa fa-hand-o-up"></i> Drag task between list</p> -->

                            <!-- <div class="input-group">
                                <input type="text" placeholder="Add new task. " class="input input-sm form-control">
                                <span class="input-group-btn">
                                        <button type="button" class="btn btn-sm btn-white"> <i class="fa fa-plus"></i> Add task</button>
                                </span>
                            </div> -->





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


                                            <!-- <#if '${seq?contains(a.taskName)?string}' == 'true'> -->
                                            <!-- <#else> -->
                                            <div class="chat-user-for animated" id="animation_box_${a_index}">
                                                <span class="pull-right label label-primary" id="upload_task_${a_index}" >完成
                                                </span>

                                                <div class="chat-user-name">
                                                    <a href="#">${a.taskName}</a>
                                                </div>
                                                <div style="display: none">
                                                    <form type="hidden" action="<@ofbizUrl>createDailyTaskRecord</@ofbizUrl>" method="post" name="createDailyTaskRecord" id="createDailyTaskRecordForm" id="saveReportForm" onsubmit="return saveReport();" target="targetIfr">
                                                        <input type="text" name="owner" value="${a.owner}"/>
                                                        <input type="text" name="taskName" value="${a.taskName}"/>
                                                    <button id="online_${a_index}" type="submit">完成</button>
                                                    </form>
                                                </div>
                                            </div>
                                            <!-- </#if> -->


                                        </#list>


                                        <iframe name="targetIfr" style="display:none"></iframe> 


                                    </div>

                                </div>
                        </div>
                    <!-- </div> -->
                </div>
                <!-- </div> -->
            <!-- </div> -->
    </div>
    <div id="small-chat">

            <span class="badge badge-warning pull-right">${list4?size}</span>
            <!-- <a class="open-small-chat">
                <i class="fa fa-comments"></i>

            </a> -->
            <!-- <button type="button"><i class="fa fa-comments"></i></button> -->
            <button class="btn btn-info btn-circle btn-lg" type="button"><i class="fa fa-check"></i>
                            </button>
    </div>

    <div id="right-sidebar" class="animated">
        <div class="sidebar-container">

                <ul class="nav nav-tabs navs-3">

                    <li class="active"><a data-toggle="tab" href="#tab-1">
                        Notes
                    </a></li>
                    <li><a data-toggle="tab" href="#tab-2">
                        Projects
                    </a></li>
                    <li class=""><a data-toggle="tab" href="#tab-3">
                        <i class="fa fa-gear"></i>
                    </a></li>
                </ul>

                <div class="tab-content">


                    <div id="tab-1" class="tab-pane active">

                        <div class="sidebar-title">
                            <h3> <i class="fa fa-comments-o"></i> Latest Notes</h3>
                            <small><i class="fa fa-tim"></i> You have 10 new message.</small>
                        </div>

                        <div>

                            <div class="sidebar-message">
                                <a href="#">
                                    <div class="pull-left text-center">
                                        <img alt="image" class="img-circle message-avatar" src="img/a1.jpg">

                                        <div class="m-t-xs">
                                            <i class="fa fa-star text-warning"></i>
                                            <i class="fa fa-star text-warning"></i>
                                        </div>
                                    </div>
                                    <div class="media-body">

                                        There are many variations of passages of Lorem Ipsum available.
                                        <br>
                                        <small class="text-muted">Today 4:21 pm</small>
                                    </div>
                                </a>
                            </div>
                            <div class="sidebar-message">
                                <a href="#">
                                    <div class="pull-left text-center">
                                        <img alt="image" class="img-circle message-avatar" src="img/a2.jpg">
                                    </div>
                                    <div class="media-body">
                                        The point of using Lorem Ipsum is that it has a more-or-less normal.
                                        <br>
                                        <small class="text-muted">Yesterday 2:45 pm</small>
                                    </div>
                                </a>
                            </div>
                            <div class="sidebar-message">
                                <a href="#">
                                    <div class="pull-left text-center">
                                        <img alt="image" class="img-circle message-avatar" src="img/a3.jpg">

                                        <div class="m-t-xs">
                                            <i class="fa fa-star text-warning"></i>
                                            <i class="fa fa-star text-warning"></i>
                                            <i class="fa fa-star text-warning"></i>
                                        </div>
                                    </div>
                                    <div class="media-body">
                                        Mevolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).
                                        <br>
                                        <small class="text-muted">Yesterday 1:10 pm</small>
                                    </div>
                                </a>
                            </div>
                            <div class="sidebar-message">
                                <a href="#">
                                    <div class="pull-left text-center">
                                        <img alt="image" class="img-circle message-avatar" src="img/a4.jpg">
                                    </div>

                                    <div class="media-body">
                                        Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the
                                        <br>
                                        <small class="text-muted">Monday 8:37 pm</small>
                                    </div>
                                </a>
                            </div>
                            <div class="sidebar-message">
                                <a href="#">
                                    <div class="pull-left text-center">
                                        <img alt="image" class="img-circle message-avatar" src="img/a8.jpg">
                                    </div>
                                    <div class="media-body">

                                        All the Lorem Ipsum generators on the Internet tend to repeat.
                                        <br>
                                        <small class="text-muted">Today 4:21 pm</small>
                                    </div>
                                </a>
                            </div>
                            <div class="sidebar-message">
                                <a href="#">
                                    <div class="pull-left text-center">
                                        <img alt="image" class="img-circle message-avatar" src="img/a7.jpg">
                                    </div>
                                    <div class="media-body">
                                        Renaissance. The first line of Lorem Ipsum, "Lorem ipsum dolor sit amet..", comes from a line in section 1.10.32.
                                        <br>
                                        <small class="text-muted">Yesterday 2:45 pm</small>
                                    </div>
                                </a>
                            </div>
                            <div class="sidebar-message">
                                <a href="#">
                                    <div class="pull-left text-center">
                                        <img alt="image" class="img-circle message-avatar" src="img/a3.jpg">

                                        <div class="m-t-xs">
                                            <i class="fa fa-star text-warning"></i>
                                            <i class="fa fa-star text-warning"></i>
                                            <i class="fa fa-star text-warning"></i>
                                        </div>
                                    </div>
                                    <div class="media-body">
                                        The standard chunk of Lorem Ipsum used since the 1500s is reproduced below.
                                        <br>
                                        <small class="text-muted">Yesterday 1:10 pm</small>
                                    </div>
                                </a>
                            </div>
                            <div class="sidebar-message">
                                <a href="#">
                                    <div class="pull-left text-center">
                                        <img alt="image" class="img-circle message-avatar" src="img/a4.jpg">
                                    </div>
                                    <div class="media-body">
                                        Uncover many web sites still in their infancy. Various versions have.
                                        <br>
                                        <small class="text-muted">Monday 8:37 pm</small>
                                    </div>
                                </a>
                            </div>
                        </div>

                    </div>

                    <div id="tab-2" class="tab-pane">

                        <div class="sidebar-title">
                            <h3> <i class="fa fa-cube"></i> Latest projects</h3>
                            <small><i class="fa fa-tim"></i> You have 14 projects. 10 not completed.</small>
                        </div>

                        <ul class="sidebar-list">
                            <li>
                                <a href="#">
                                    <div class="small pull-right m-t-xs">9 hours ago</div>
                                    <h4>Business valuation</h4>
                                    It is a long established fact that a reader will be distracted.

                                    <div class="small">Completion with: 22%</div>
                                    <div class="progress progress-mini">
                                        <div style="width: 22%;" class="progress-bar progress-bar-warning"></div>
                                    </div>
                                    <div class="small text-muted m-t-xs">Project end: 4:00 pm - 12.06.2014</div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="small pull-right m-t-xs">9 hours ago</div>
                                    <h4>Contract with Company </h4>
                                    Many desktop publishing packages and web page editors.

                                    <div class="small">Completion with: 48%</div>
                                    <div class="progress progress-mini">
                                        <div style="width: 48%;" class="progress-bar"></div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="small pull-right m-t-xs">9 hours ago</div>
                                    <h4>Meeting</h4>
                                    By the readable content of a page when looking at its layout.

                                    <div class="small">Completion with: 14%</div>
                                    <div class="progress progress-mini">
                                        <div style="width: 14%;" class="progress-bar progress-bar-info"></div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="label label-primary pull-right">NEW</span>
                                    <h4>The generated</h4>
                                    There are many variations of passages of Lorem Ipsum available.
                                    <div class="small">Completion with: 22%</div>
                                    <div class="small text-muted m-t-xs">Project end: 4:00 pm - 12.06.2014</div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="small pull-right m-t-xs">9 hours ago</div>
                                    <h4>Business valuation</h4>
                                    It is a long established fact that a reader will be distracted.

                                    <div class="small">Completion with: 22%</div>
                                    <div class="progress progress-mini">
                                        <div style="width: 22%;" class="progress-bar progress-bar-warning"></div>
                                    </div>
                                    <div class="small text-muted m-t-xs">Project end: 4:00 pm - 12.06.2014</div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="small pull-right m-t-xs">9 hours ago</div>
                                    <h4>Contract with Company </h4>
                                    Many desktop publishing packages and web page editors.

                                    <div class="small">Completion with: 48%</div>
                                    <div class="progress progress-mini">
                                        <div style="width: 48%;" class="progress-bar"></div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <div class="small pull-right m-t-xs">9 hours ago</div>
                                    <h4>Meeting</h4>
                                    By the readable content of a page when looking at its layout.

                                    <div class="small">Completion with: 14%</div>
                                    <div class="progress progress-mini">
                                        <div style="width: 14%;" class="progress-bar progress-bar-info"></div>
                                    </div>
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <span class="label label-primary pull-right">NEW</span>
                                    <h4>The generated</h4>
                                    <div class="small pull-right m-t-xs">9 hours ago</div>
                                    There are many variations of passages of Lorem Ipsum available.
                                    <div class="small">Completion with: 22%</div>
                                    <div class="small text-muted m-t-xs">Project end: 4:00 pm - 12.06.2014</div>
                                </a>
                            </li>

                        </ul>

                    </div>

                    <div id="tab-3" class="tab-pane">

                        <div class="sidebar-title">
                            <h3><i class="fa fa-gears"></i> Settings</h3>
                            <small><i class="fa fa-tim"></i> You have 14 projects. 10 not completed.</small>
                        </div>

                        <div class="setings-item">
                    <span>
                        Show notifications
                    </span>
                            <div class="switch">
                                <div class="onoffswitch">
                                    <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="example">
                                    <label class="onoffswitch-label" for="example">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="setings-item">
                    <span>
                        Disable Chat
                    </span>
                            <div class="switch">
                                <div class="onoffswitch">
                                    <input type="checkbox" name="collapsemenu" checked class="onoffswitch-checkbox" id="example2">
                                    <label class="onoffswitch-label" for="example2">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="setings-item">
                    <span>
                        Enable history
                    </span>
                            <div class="switch">
                                <div class="onoffswitch">
                                    <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="example3">
                                    <label class="onoffswitch-label" for="example3">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="setings-item">
                    <span>
                        Show charts
                    </span>
                            <div class="switch">
                                <div class="onoffswitch">
                                    <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="example4">
                                    <label class="onoffswitch-label" for="example4">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="setings-item">
                    <span>
                        Offline users
                    </span>
                            <div class="switch">
                                <div class="onoffswitch">
                                    <input type="checkbox" checked name="collapsemenu" class="onoffswitch-checkbox" id="example5">
                                    <label class="onoffswitch-label" for="example5">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="setings-item">
                    <span>
                        Global search
                    </span>
                            <div class="switch">
                                <div class="onoffswitch">
                                    <input type="checkbox" checked name="collapsemenu" class="onoffswitch-checkbox" id="example6">
                                    <label class="onoffswitch-label" for="example6">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="setings-item">
                    <span>
                        Update everyday
                    </span>
                            <div class="switch">
                                <div class="onoffswitch">
                                    <input type="checkbox" name="collapsemenu" class="onoffswitch-checkbox" id="example7">
                                    <label class="onoffswitch-label" for="example7">
                                        <span class="onoffswitch-inner"></span>
                                        <span class="onoffswitch-switch"></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="sidebar-content">
                            <h4>Settings</h4>
                            <div class="small">
                                I belive that. Lorem Ipsum is simply dummy text of the printing and typesetting industry.
                                And typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.
                                Over the years, sometimes by accident, sometimes on purpose (injected humour and the like).
                            </div>
                        </div>

                    </div>
                </div>
        </div>
    </div> 

</div>



<!-- Mainly scripts -->
<script src="/images/project_demo/js/plugins/fullcalendar/moment.min.js"></script>
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
<script src="/images/project_demo/js/plugins/fullcalendar/fullcalendar.js"></script>

<!-- GITTER -->
<script src="/images/project_demo/js/plugins/gritter/jquery.gritter.min.js"></script>

<!-- Sparkline -->
<script src="/images/project_demo/js/plugins/sparkline/jquery.sparkline.min.js"></script>

<!-- Sparkline demo data  -->
<script src="/images/project_demo/js/demo/sparkline-demo.js"></script>

<!-- ChartJS-->
<script src="/images/project_demo/js/plugins/chartJs/Chart.min.js"></script>

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

<script>

    $(document).ready(function() {


        /* initialize the external events
         -----------------------------------------------------------------*/


        $('#external-events div.external-event').each(function() {

            // store data so the calendar knows to render an event upon drop
            $(this).data('event', {
                title: $.trim($(this).text()), // use the element's text as the event title
                stick: true // maintain when user navigates (see docs on the renderEvent method)
            });

            // make the event draggable using jQuery UI
            $(this).draggable({
                zIndex: 1111999,
                revert: true,      // will cause the event to go back to its
                revertDuration: 0  //  original position after the drag
            });

        });

        $('#table-two-axis').basictable();



    });

</script>


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
