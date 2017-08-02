<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>INSPINIA | Morris.js Charts</title>

    <link href="/images/project_demo/css/bootstrap.min.css" rel="stylesheet">
    <link href="/images/project_demo/font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- orris -->
    <link href="/images/project_demo/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">

    <!-- FooTable -->
    <link href="/images/project_demo/css/plugins/footable/footable.core.css" rel="stylesheet">

        <!-- Gritter -->
    <link href="/images/project_demo/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">


    <link href="/images/project_demo/css/animate.css" rel="stylesheet">
    <link href="/images/project_demo/css/plugins/iCheck/custom.css" rel="stylesheet">
    <!-- <link href="/images/project_demo/css/style_1.css" rel="stylesheet"> -->
    <link href="/images/project_demo/css/style_project_demo.css" rel="stylesheet">


</head>

<body>

    <div id="wrapper">

    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav metismenu" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element"> <span>
                            <img alt="image" class="img-circle" src="/images/project_demo/img/<#if '${userLogin.userLoginId}' == '1pan.shi1'>${userLogin.userLoginId}<#elseif '${userLogin.userLoginId}' == 'su.qichen'>${userLogin.userLoginId}<#elseif '${userLogin.userLoginId}' == 'yasin.lyyas'>${userLogin.userLoginId}<#else>pan.shi</#if>.png">  </span>
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="clear_1"> <span class="block m-t-xs"> <strong class="font-bold">David Williams</strong>
                             </span> <span class="text-muted text-xs block">Art Director <b class="caret"></b></span> </span> </a>
                        <ul class="dropdown-menu animated fadeInRight m-t-xs">
                            <li><a href="profile.html">Profile</a></li>
                            <li><a href="contacts.html">Contacts</a></li>
                            <li><a href="mailbox.html">Mailbox</a></li>
                            <li class="divider"></li>
                            <li><a href="login.html">Logout</a></li>
                        </ul>
                    </div>
                    <div class="logo-element">
                        IN+
                    </div>
                </li>
                <li class="active">
                    <a href="project_demo#"><i class="fa fa-sitemap"></i> <span class="nav-label">Category</span><span class="fa arrow"></span></a>

                    <#assign taskCol = true>
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
                        </li>
                    </#list>

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
                    <span class="m-r-sm text-muted welcome-message">Welcome to INSPINIA+ Admin Theme.</span>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-envelope"></i>  <span class="label label-warning">16</span>
                    </a>
                    <ul class="dropdown-menu dropdown-messages">
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="profile.html" class="pull-left">
                                    <img alt="image" class="img-circle" src="img/a7.jpg">
                                </a>
                                <div class="media-body">
                                    <small class="pull-right">46h ago</small>
                                    <strong>Mike Loreipsum</strong> started following <strong>Monica Smith</strong>. <br>
                                    <small class="text-muted">3 days ago at 7:58 pm - 10.06.2014</small>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="profile.html" class="pull-left">
                                    <img alt="image" class="img-circle" src="img/a4.jpg">
                                </a>
                                <div class="media-body ">
                                    <small class="pull-right text-navy">5h ago</small>
                                    <strong>Chris Johnatan Overtunk</strong> started following <strong>Monica Smith</strong>. <br>
                                    <small class="text-muted">Yesterday 1:21 pm - 11.06.2014</small>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="profile.html" class="pull-left">
                                    <img alt="image" class="img-circle" src="img/profile.jpg">
                                </a>
                                <div class="media-body ">
                                    <small class="pull-right">23h ago</small>
                                    <strong>Monica Smith</strong> love <strong>Kim Smith</strong>. <br>
                                    <small class="text-muted">2 days ago at 2:30 am - 11.06.2014</small>
                                </div>
                            </div>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="mailbox.html">
                                    <i class="fa fa-envelope"></i> <strong>Read All Messages</strong>
                                </a>
                            </div>
                        </li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-bell"></i>  <span class="label label-primary">8</span>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        <li>
                            <a href="mailbox.html">
                                <div>
                                    <i class="fa fa-envelope fa-fw"></i> You have 16 messages
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="profile.html">
                                <div>
                                    <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                    <span class="pull-right text-muted small">12 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="grid_options.html">
                                <div>
                                    <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="notifications.html">
                                    <strong>See All Alerts</strong>
                                    <i class="fa fa-angle-right"></i>
                                </a>
                            </div>
                        </li>
                    </ul>
                </li>


                <li>
                    <a href="login.html">
                        <i class="fa fa-sign-out"></i> Log out
                    </a>
                </li>
            </ul>

        </nav>
        </div>
            <!-- <div class="row wrapper border-bottom white-bg page-heading"> -->
            <div class="row wrapper border-bottom page-heading">
                <div class="row">
                            <div class="col-lg-3">
                                <div class="ibox float-e-margins">
                                    <div class="ibox-title">
                                        <span class="label label-success pull-right">Monthly</span>
                                        <h5>Income</h5>
                                    </div>
                                    <div class="ibox-content">
                                        <h1 class="no-margins">16</h1>
                                        <!-- <div class="stat-percent font-bold text-success">98% <i class="fa fa-bolt"></i></div> -->
                                        <small>全部任务</small>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3">
                                <div class="ibox float-e-margins">
                                    <div class="ibox-title">
                                        <span class="label label-info pull-right">Annual</span>
                                        <h5>Orders</h5>
                                    </div>
                                    <div class="ibox-content">
                                        <h1 class="no-margins">6</h1>
                                        <div class="stat-percent font-bold text-info">20% <i class="fa fa-level-up"></i></div>
                                        <small>处理中</small>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3">
                                <div class="ibox float-e-margins">
                                    <div class="ibox-title">
                                        <span class="label label-primary pull-right">Today</span>
                                        <h5>Vistits</h5>
                                    </div>
                                    <div class="ibox-content">
                                        <h1 class="no-margins">5</h1>
                                        <div class="stat-percent font-bold text-navy">44% <i class="fa fa-level-up"></i></div>
                                        <small>已完成</small>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3">
                                <div class="ibox float-e-margins">
                                    <div class="ibox-title">
                                        <span class="label label-danger pull-right">Low value</span>
                                        <h5>User activity</h5>
                                    </div>
                                    <div class="ibox-content">
                                        <h1 class="no-margins">5</h1>
                                        <!-- <div class="stat-percent font-bold text-danger">38% <i class="fa fa-level-down"></i></div> -->
                                        <small>待处理</small>
                                    </div>
                                </div>
                            </div>
                            <!-- <div>THIS IS TEST</div> -->
                            ${businessList}
                            <br />
                            <br />
                            ${allChildListSize}
                            <br />
                            <br />
                            ${parameters}
                </div>
            </div>
        <div class="wrapper wrapper-content animated fadeInRight" >
            <div class="row">
            <div class="col-lg-6">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>任务统计 <small></small></h5>
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
                    <div class="ibox-content" style="position: relative">
                        <div id="morris-bar-chart"></div>
                    </div>
                </div>
            </div>
            <div class="col-lg-6">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>今天计划完成  </h5>
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

                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>项目名称</th>
                                    <th>优先级</th>
                                    <th>开始时间</th>
                                    <th>进度</th>
                                    <th>状态</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>Example product 1</td>
                                    <td style="text-align:center;">A</td>
                                    <td>2017-06-06</td>
                                    <td><span class="pie">0.52,1.041</span></td>
                                    <td class="text-navy">Pending</td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>Example product 2</td>
                                    <td style="text-align:center;">A</td>
                                    <td>2017-06-06</td>
                                    <td><span class="pie">226,134</span></td>
                                    <td class="text-warning">Pending</td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td>Example product 3</td>
                                    <td style="text-align:center;">A</td>
                                    <td>2017-06-06</td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-danger">Pending</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
            </div>
            </div>


            <div class="row">
                <div class="col-lg-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>进行中的项目 </h5>
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
                        <div class="ibox">
                        <div class="ibox-content">

                            <table class="footable table table-stripped toggle-arrow-tiny" data-page-size="15">
                                <thead>
                                <tr>

                                    <th data-toggle="true">项目名称</th>
                                    <th data-hide="phone">优先级</th>
                                    <th data-hide="phone">开始时间</th>
                                    <th data-hide="phone" >预计完成时间</th>
                                    <th data-hide="phone">状态</th>
                                    <th data-hide="phone">进度</th>
                                    <th class="text-right" data-sort-ignore="true">操作</th>

                                </tr>
                                </thead>
                                <tbody>
                                <tr class="header expand">
                                    <td>
                                       Example product 1
                                    </td>
                                    <td style="text-align:center;">
                                        <span class="text-danger">A</span>
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        2017-06-20
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="text-info">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Task1 </span>Step1
                                    </td>
                                    <td style="text-align:center;">
                                        <span class="text-danger">A</span>
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="text-info">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Task2 </span>Step2
                                    </td>
                                    <td style="text-align:center;">
                                        <span class="text-warning">B</span>
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        <span class="label label-danger">Disabled</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="header expand">
                                    <td>
                                        Example product 2
                                    </td>
                                    <td style="text-align:center;">
                                        <span class="text-warning">B</span>
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="text-info">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Task1 </span>Step 1
                                    </td>
                                    <td style="text-align:center;">
                                        <span class="text-danger">A</span>
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        <span class="label label-warning">Low stock</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="text-info">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Task2 </span>Step 2
                                    </td>
                                    <td style="text-align:center;">
                                        <span class="text-warning">B</span>
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        <span class="label label-danger">Disabled</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="text-info">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Task3 </span>Step 3
                                    </td>
                                    <td style="text-align:center;">
                                        <span class="text-info">C</span>
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        <span class="label label-danger">Disabled</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="text-info">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Task4 </span>Step 4
                                    </td>
                                    <td style="text-align:center;">
                                        <span class="text-success">D</span>
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        2017-06-06
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="header expand">
                                    <td>
                                        Example product 9
                                    </td>
                                    <td>
                                        Model 9
                                    </td>
                                    <td>
                                        $97.00
                                    </td>
                                    <td>
                                        450
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 10
                                    </td>
                                    <td>
                                        Model 10
                                    </td>
                                    <td>
                                        $43.00
                                    </td>
                                    <td>
                                        7600
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 11
                                    </td>
                                    <td>
                                        Model 1
                                    </td>
                                    <td>
                                        $50.00
                                    </td>
                                    <td>
                                        1000
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 12
                                    </td>
                                    <td>
                                        Model 2
                                    </td>
                                    <td>
                                        $40.00
                                    </td>
                                    <td>
                                        4300
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 13
                                    </td>
                                    <td>
                                        Model 3
                                    </td>
                                    <td>
                                        $22.00
                                    </td>
                                    <td>
                                        300
                                    </td>
                                    <td>
                                        <span class="label label-warning">Low stock</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 14
                                    </td>
                                    <td>
                                        Model 4
                                    </td>
                                    <td>
                                        $67.00
                                    </td>
                                    <td>
                                        2300
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 15
                                    </td>
                                    <td>
                                        Model 5
                                    </td>
                                    <td>
                                        $76.00
                                    </td>
                                    <td>
                                        800
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 16
                                    </td>
                                    <td>
                                        Model 6
                                    </td>
                                    <td>
                                        $60.00
                                    </td>
                                    <td>
                                        6000
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 17
                                    </td>
                                    <td>
                                        Model 7
                                    </td>
                                    <td>
                                        $32.00
                                    </td>
                                    <td>
                                        700
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 18
                                    </td>
                                    <td>
                                        Model 8
                                    </td>
                                    <td>
                                        $86.00
                                    </td>
                                    <td>
                                        5180
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 19
                                    </td>
                                    <td>
                                        Model 9
                                    </td>
                                    <td>
                                        $97.00
                                    </td>
                                    <td>
                                        450
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Example product 20
                                    </td>
                                    <td>
                                        Model 10
                                    </td>
                                    <td>
                                        $43.00
                                    </td>
                                    <td>
                                        7600
                                    </td>
                                    <td>
                                        <span class="label label-primary">Enable</span>
                                    </td>
                                    <td><span class="pie">0.52/1.561</span></td>
                                    <td class="text-right">
                                        <div class="btn-group">
                                            <button class="btn-white btn btn-xs">View</button>
                                            <button class="btn-white btn btn-xs">Edit</button>
                                        </div>
                                    </td>
                                </tr>


                                </tbody>
                                <tfoot>
                                <tr>
                                    <td colspan="6">
                                        <ul class="pagination pull-right"></ul>
                                    </td>
                                </tr>
                                </tfoot>
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
                <strong>Copyright</strong> Example Company &copy; 2014-2015
            </div>
        </div>

        </div>
        </div>



    <!-- Mainly scripts -->
    <script src="/images/project_demo/js/jquery-2.1.1.js"></script>
    <script src="/images/project_demo/js/bootstrap.min.js"></script>
    <script src="/images/project_demo/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/images/project_demo/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

    <!-- Morris -->
    <script src="/images/project_demo/js/plugins/morris/raphael-2.1.0.min.js"></script>
    <script src="/images/project_demo/js/plugins/morris/morris.js"></script>

    
    <!-- Peity -->
    <script src="/images/project_demo/js/plugins/peity/jquery.peity.min.js"></script>

    <!-- Custom and plugin javascript -->
    <script src="/images/project_demo/js/inspinia.js"></script>
    <script src="/images/project_demo/js/plugins/pace/pace.min.js"></script>

    <!-- Morris demo data-->
    <script src="/images/project_demo/js/demo/morris-demo.js"></script>

    <!-- jQuery UI -->
    <script src="/images/project_demo/js/plugins/jquery-ui/jquery-ui.min.js"></script>


    <!-- FooTable -->
    <script src="/images/project_demo/js/plugins/footable/footable.all.min.js"></script>

    <!-- iCheck -->
    <script src="/images/project_demo/js/plugins/iCheck/icheck.min.js"></script>

    <!-- Peity -->
    <script src="/images/project_demo/js/demo/peity-demo.js"></script>

    <!-- Page-Level Scripts -->
    <script>
        $(document).ready(function() {

            $('.footable').footable();

        });
        $('.header').click(function(){
             $(this).toggleClass('expand').nextUntil('tr.header').slideToggle(100);
        });

    </script>


</body>

</html>
