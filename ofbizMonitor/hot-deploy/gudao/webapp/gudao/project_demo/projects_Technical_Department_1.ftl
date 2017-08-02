<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>INSPINIA | Projects list</title>

    <link href="/images/project_demo/css/bootstrap.min.css" rel="stylesheet">
    <link href="/images/project_demo/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/images/project_demo/css/animate.css" rel="stylesheet">
    <link href="/images/project_demo/css/style_project_demo.css" rel="stylesheet">

    <!-- <style>

    /* hide content from view but not from searching */
    .hidden { display: none; }

    /* filter table specific styling */
    .filter-table .quick { margin-left: 1em; font-size: 0.8em; text-decoration: none; }
    .fitler-table .quick:hover { text-decoration: underline; }
    td.alt { background-color: #ffc; background-color: rgba(255, 255, 0, 0.2); }
    </style> -->

</head>

<body>

    <div id="wrapper">

        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">

                        <div class="dropdown profile-element"> 
                            <span>
                              <img alt="image" class="img-circle" src="/images/project_demo/img/<#if '${userLogin.userLoginId}' == 'pan.shi'>${userLogin.userLoginId}<#elseif '${userLogin.userLoginId}' == 'su.qichen'>${userLogin.userLoginId}<#elseif '${userLogin.userLoginId}' == 'yasin.lyyas'>${userLogin.userLoginId}<#else>pan.shi</#if>.png">
                             </span>
                            <a data-toggle="dropdown" class="dropdown-toggle" href="project_detail_Technical_Department_1#">
                            <span class=""> <span class="block m-t-xs"> <strong class="font-bold">${userLogin.userLoginId}</strong>
                             </span> <span class="text-muted text-xs block">User <b class="caret"></b></span> </span> </a>
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
                    <!-- 
                    <li>
                        <a href="index.html"><i class="fa fa-th-large"></i> <span class="nav-label">Dashboards</span> <span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="index.html">Dashboard v.1</a></li>
                            <li><a href="dashboard_2.html">Dashboard v.2</a></li>
                            <li><a href="dashboard_3.html">Dashboard v.3</a></li>
                        </ul>
                    </li>

                    <li>
                        <a href="project_detail_Technical_Department#"><i class="fa fa-bar-chart-o"></i> <span class="nav-label">Graphs</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="graph_flot.html">Flot Charts</a></li>
                            <li><a href="graph_morris.html">Morris.js Charts</a></li>
                            <li><a href="graph_rickshaw.html">Rickshaw Charts</a></li>
                            <li><a href="graph_peity.html">Peity Charts</a></li>
                            <li><a href="graph_sparkline.html">Sparkline Charts</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="mailbox.html"><i class="fa fa-envelope"></i> <span class="nav-label">Mailbox </span><span class="label label-warning pull-right">16/24</span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="mailbox.html">Inbox</a></li>
                            <li><a href="mail_detail.html">Email view</a></li>
                            <li><a href="mail_compose.html">Compose email</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="widgets.html"><i class="fa fa-flask"></i> <span class="nav-label">Widgets</span> <span class="label label-info pull-right">NEW</span></a>
                    </li>
                    <li>
                        <a href="project_detail_Technical_Department#"><i class="fa fa-edit"></i> <span class="nav-label">Forms</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="form_basic.html">Basic form</a></li>
                            <li><a href="form_advanced.html">Advanced Plugins</a></li>
                            <li><a href="form_wizard.html">Wizard</a></li>
                            <li><a href="form_file_upload.html">File Upload</a></li>
                            <li><a href="form_editors.html">Text Editor</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="project_detail_Technical_Department#"><i class="fa fa-desktop"></i> <span class="nav-label">App Views</span>  <span class="pull-right label label-primary">SPECIAL</span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="contacts.html">Contacts</a></li>
                            <li><a href="profile.html">Profile</a></li>
                            <li class="active"><a href="project_detail_Technical_Department">Projects</a></li>
                            <li><a href="project_detail.html">Project detail</a></li>
                            <li><a href="file_manager.html">File manager</a></li>
                            <li><a href="calendar.html">Calendar</a></li>
                            <li><a href="faq.html">FAQ</a></li>
                            <li><a href="timeline.html">Timeline</a></li>
                            <li><a href="pin_board.html">Pin board</a></li>
                            <li><a href="invoice.html">Invoice</a></li>
                            <li><a href="login.html">Login</a></li>
                            <li><a href="register.html">Register</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="project_detail_Technical_Department#"><i class="fa fa-files-o"></i> <span class="nav-label">Other Pages</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="search_results.html">Search results</a></li>
                            <li><a href="lockscreen.html">Lockscreen</a></li>
                            <li><a href="404.html">404 Page</a></li>
                            <li><a href="500.html">500 Page</a></li>
                            <li><a href="empty_page.html">Empty page</a></li>
                        </ul>
                    </li>

                    <li >
                        <a href="project_detail_Technical_Department#"><i class="fa fa-flask"></i> <span class="nav-label">UI Elements</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="typography.html">Typography</a></li>
                            <li><a href="icons.html">Icons</a></li>
                            <li><a href="draggable_panels.html">Draggable Panels</a></li>
                            <li><a href="buttons.html">Buttons</a></li>
                            <li><a href="tabs_panels.html">Tabs & Panels</a></li>
                            <li><a href="notifications.html">Notifications & Tooltips</a></li>
                            <li><a href="badges_labels.html">Badges, Labels, Progress</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="grid_options.html"><i class="fa fa-laptop"></i> <span class="nav-label">Grid options</span></a>
                    </li>
                    <li>
                        <a href="project_detail_Technical_Department#"><i class="fa fa-table"></i> <span class="nav-label">Tables</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="table_basic.html">Static Tables</a></li>
                            <li><a href="table_data_tables.html">Data Tables</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="project_detail_Technical_Department#"><i class="fa fa-picture-o"></i> <span class="nav-label">Gallery</span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="basic_gallery.html">Basic Gallery</a></li>
                            <li><a href="carousel.html">Bootstrap Carusela</a></li>

                        </ul>
                    </li>
                    <li>
                        <a href="project_detail_Technical_Department#"><i class="fa fa-sitemap"></i> <span class="nav-label">Menu Levels </span><span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li>
                                <a href="project_detail_Technical_Department#">Third Level <span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li>
                                        <a href="project_detail_Technical_Department#">Third Level Item</a>
                                    </li>
                                    <li>
                                        <a href="project_detail_Technical_Department#">Third Level Item</a>
                                    </li>
                                    <li>
                                        <a href="project_detail_Technical_Department#">Third Level Item</a>
                                    </li>

                                </ul>
                            </li>
                            <li><a href="project_detail_Technical_Department#">Second Level Item</a></li>
                            <li>
                                <a href="project_detail_Technical_Department#">Second Level Item</a></li>
                            <li>
                                <a href="project_detail_Technical_Department#">Second Level Item</a></li>
                        </ul>
                    </li>
                     -->
                    <!-- 20170405 公司架构 -->
                    <li class="active">
                        <a href="index.html#"><i class="fa fa-sitemap"></i> <span class="nav-label">Category</span><span class="fa arrow"></span></a>

                        <#assign taskCol = true>
                        <ul class="nav nav-second-level">
                        <#list businessList as businessList>

                            <#if businessList.parent.businessTreeId?substring(0,1) = parameters.businessTreeId?substring(0,1)>
                                <li class="active">
                            <#else>
                                <li>
                            </#if> 
                            <!-- ${businessList.parent.businessTreeId?number+2000}
                            ${parameters.businessTreeId} -->
                                <a href="#">${businessList.parent.title}<span class="fa arrow"></span></a>
                            
                                <ul class="nav nav-third-level">
                                <#list businessList.childList as childList>
                                    <li >
                                        <a id="${childList.childId}"  onclick="getTaskTab(this.id, '${childList.childId}' ,'${treeType}')" href='<@ofbizUrl>projects_Technical_Department_1?businessTreeId=${childList.childId}&businessTreeType=${treeType}</@ofbizUrl>#'>${childList.title}</a>
                                    </li>
                                    <!-- <li>
                                        <a href="projects#">人事部</a>
                                    </li>
                                    <li>
                                        <a href="/projects_Administration_Department.ftl#">行政部</a>
                                    </li> -->
                                    </#list>

                                </ul>
                                    </#list>

                            </li> 



                       <!--  <ul class="nav nav-second-level">
                            <li>
                                <a href="index.html#">人事行政部<span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li>
                                        <a href="projects_Merchants_Department.html#">招商部</a>
                                    </li>
                                    <li>
                                        <a href="projects_Personnel_department.html#">人事部</a>
                                    </li>
                                    <li>
                                        <a href="projects_Administration_Department.html#">行政部</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="index.html#">客户服务部<span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li>
                                        <a href="index.html#">青铜部</a>
                                    </li>
                                    <li>
                                        <a href="index.html#">白银部</a>
                                    </li>
                                    <li>
                                        <a href="index.html#">黄金部</a>
                                    </li>
                                </ul>
                            </li>
                            <li  class="active">
                                <a href="projects_Technical_Department#">系统数据部<span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li>
                                        <a href="projects_Technical_Department#">技术部</a>
                                    </li>
                                    <li>
                                        <a href="projects_Technical_Department_1#">数据部</a>
                                    </li>
                                    <li>
                                        <a href="projects_Technical_Department_2#" onclick="getTaskTab(this.id, '31000' ,'DEPARTMENT')">财务部</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="index.html#">业务支持部<span class="fa arrow"></span></a>
                                <ul class="nav nav-third-level">
                                    <li>
                                        <a href="index.html#">开发部</a>
                                    </li>
                                    <li>
                                        <a href="index.html#">销售部</a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </li> -->
                    <!-- 
                    <li>
                        <a href="css_animation.html"><i class="fa fa-magic"></i> <span class="nav-label">CSS Animations </span><span class="label label-info pull-right">62</span></a>
                    </li>
                      -->
                </ul>

            </div>
        </nav>

        <div id="page-wrapper" class="gray-bg">
        <div class="row border-bottom">
        <nav class="navbar navbar-static-top  " role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
            <form role="search" class="navbar-form-custom" method="post" action="#">
                <div class="form-group">
                    <input type="text" placeholder="Search for something..." class="form-control" name="top-search" id="top-search">
                </div>
            </form>
        </div>
            <ul class="nav navbar-top-links navbar-right">
                <li>
                    <span class="m-r-sm text-muted welcome-message">Welcome to GuDao+ Admin Theme.</span>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="project_detail_Technical_Department_1#">
                        <i class="fa fa-envelope"></i>  <span class="label label-warning"></span>
                    </a>
                    <ul class="dropdown-menu dropdown-messages">
                        
<!-- 
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="profile.html" class="pull-left">
                                    <img alt="image" class="img-circle" src="/images/project_demo/img/a7.jpg">
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
                                    <img alt="image" class="img-circle" src="/images/project_demo/img/a4.jpg">
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
                                    <img alt="image" class="img-circle" src="/images/project_demo/img/profile.jpg">
                                </a>
                                <div class="media-body ">
                                    <small class="pull-right">23h ago</small>
                                    <strong>Monica Smith</strong> love <strong>Kim Smith</strong>. <br>
                                    <small class="text-muted">2 days ago at 2:30 am - 11.06.2014</small>
                                </div>
                            </div>
                        </li>
 -->


                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="#">
                                    <i class="fa fa-envelope"></i> <strong>邮箱开发中</strong>
                                </a>
                            </div>
                        </li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="project_detail_Technical_Department_1#">
                        <i class="fa fa-bell"></i>  <span class="label label-primary"></span>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        
                        <!-- <li>
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
                        </li> -->
                        

                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="#">
                                    <strong>See All Alerts...(开发中)</strong>
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
            <div class="row wrapper border-bottom white-bg page-heading">
                <div class="col-sm-4">
                    <h2  class="textss">Project list</h2>
                    <ol class="breadcrumb">
                        <li>
                            <a href="project_demo#">Home</a>
                        </li>
                        <li>
                            <a href="project_demo">Category</a>
                        </li>
                        <li class="active">
                            <strong>Project list</strong>
                        </li>
                    </ol>
                </div>
            </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="wrapper wrapper-content animated fadeInUp">

                    <div class="ibox">
                        <div class="ibox-title">
                            <h5>All projects assigned to this account</h5>
                            <div class="ibox-tools">
                                <a  target="_black" href='<@ofbizUrl>edit_project_form_basic_1?businessTreeId=${parameters.businessTreeId}&businessTreeType=${parameters.businessTreeType}</@ofbizUrl>' class="btn btn-primary btn-xs">Create new project</a>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div class="row m-b-sm m-t-sm">
                                <div class="col-md-1">
                                    <button type="button" id="loading-example-btn" class="btn btn-white btn-sm" onclick="location.reload()" ><i class="fa fa-refresh"></i> Refresh</button>
                                </div>
                                <div class="col-md-11">
                                    <div class="input-group_1">
                                        <input type="text" placeholder="Search" class="input-sm form-control" id="input-filter"></input>
                                        <!-- <span class="input-group-btn">
                                            <button type="button" class="btn btn-sm btn-primary"> Go!</button> 
                                        </span> -->
                                    </div>
                                </div>
                                <!-- <div class="col-md-11">
                                    <div class="input-group">
                                        <input type="text" placeholder="Search" class="input-sm form-control" id="input-filter1"></input>
                                        <span class="input-group-btn">
                                            <button type="button" class="btn btn-sm btn-primary"> Go!</button> 
                                        </span>
                                    </div>
                                </div> -->
                            </div>

                            


                            <div class="project-list">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th class="first-name filter-select" data-placeholder="Select a name">STATUS</th>
                                            <th data-placeholder="Exact matches only">Title</th>
                                            <th data-placeholder="Choose a city">Completion with</th>
                                            <th data-value="<30">CreatBy </th>
                                            <th data-placeholder="Select a filter"> </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    
                                    
                                    <#-- ${parameters}
                                    <br />
                                    <br />

                                    ${mercuryList}
                                    <br />
                                    <br />


                                    ${businessList}
                                    <br />
                                    <br /> -->



                                    <#list mercuryList as test>
                                   

                                    <tr>
                                        <td class="project-status">
                                      
                                            <!-- <#if '${test.statusId}' = 'ACTIVE'> -->
                                                <span class="label  label-primary">${test.statusId}</span>
                                                <!-- <#else> -->
                                                <span class="label label-default project-status-span">${test.statusId}</span>
                                           <!-- </#if> -->
                                        </td>
                                        <td class="project-title">
                                            <!-- <a class="project-title-a" href="project_detail_Technical_Department_1">${test.title}</a> -->
                                            <a class="project-title-a" target="_black" href="<@ofbizUrl>edit_project_form_basic_1?mercuryId=${test.mercuryId}</@ofbizUrl>"><#if !test.title?has_content>kong<#else>${test.title}</#if></a>

                                            
                                            <br/>
                                            
                                            <!-- <small class="project-title-small">Created</small> -->
                                            <small   class="project-title-small">lastEta: <#if !test.lastEta?has_content><#else>${test.lastEta}</#if></small>
                                        </td>
                                        <td class="project-completion">
                                                <!-- <small>Completion with: 50%</small> -->
                                                <small  id="demo_${test.mercuryId}">Completion with: <#if test.completeCount == 0>
                                                                          0
                                                                        <#elseif test.totalCount == 0>
                                                                          0
                                                                        <#else>
                                                                          ${((test.completeCount*100)/test.totalCount)?int}
                                                                        </#if>
                                                %</small>
                                                <div class="progress progress-mini">
                                                    <div style="width: <#if test.completeCount == 0>
                                                                          0%
                                                                        <#elseif test.totalCount == 0>
                                                                          0%
                                                                        <#else>
                                                                          ${((test.completeCount*100)/test.totalCount)?int}%
                                                                        </#if>;" class="progress-bar"></div>
                                                </div>
                                                
                                        </td>
                                        <td class="project-people">
                                        <!-- <a href="#">Created</a> -->
                                            <small   class="project-title-small">${test.createdBy}</small>
                                        </td>
                                        <td class="project-actions">
                                            <a target="view_window" href="<@ofbizUrl>edit_project_form_basic_1?mercuryId=${test.mercuryId}</@ofbizUrl>" class="btn btn-white btn-sm"><i class="fa fa-folder"></i> View </a>
                                            <#if "${test.createdBy}" == "${userLogin.userLoginId}">
                                            <a target="view_window" href="<@ofbizUrl>edit_project_form_basic_1?mercuryId=${test.mercuryId}</@ofbizUrl>" class="btn btn-white btn-sm"><i class="fa fa-pencil"></i> Edit </a>
                                           <#else>
                                           
                                           </#if>
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
                <strong>Copyright</strong> Gudao Company &copy; 2017-2010
            </div>
        </div>

        </div>
        </div>
    </div>

    <!-- Mainly scripts -->
    <script src="/images/project_demo/js/jquery-1.10.2.js"></script>
    <script src="/images/project_demo/js/bootstrap.min.js"></script>
    <script src="/images/project_demo/js/plugins/metisMenu/jquery.metisMenu.js"></script>

    <!-- Custom and plugin javascript -->
    <script src="/images/project_demo/js/inspinia.js"></script>
    <script src="/images/project_demo/js/plugins/pace/pace.min.js"></script>
<!-- 20170407 add search function start -->
    <!-- <script src="/images/project_demo/js/jquery-1.11.0.min.js"></script> -->
    <!-- <script>window.jQuery || document.write('<script src="/images/project_demo/js/jquery-1.11.0.min.js"><\/script>')</script> -->
    <!-- <script src="/images/project_demo/js/jquery.filtertable.min.js"></script> -->
    <script src="/images/project_demo/js/jquery.filtertable.js"></script>
        <script>
        $(document).ready(function() {
            $('table').filterTable({ // apply filterTable to all tables on this page
                inputSelector: '#input-filter', // use the existing input instead of creating a new one
                quickList: [
                    'ACTIVE',
                    'PENDING',
                    'COMPLETED',
                ],
                quickListClear: '× clear filter'
            });

        });
        </script>

<!-- add search function end -->
    <script>
        $(document).ready(function(){

            $('#loading-example-btn').click(function () {
                btn = $(this);
                simpleLoad(btn, true)

                // Ajax example
//                $.ajax().always(function () {
//                    simpleLoad($(this), false)
//                });

                simpleLoad(btn, false)
            });
        });

        function simpleLoad(btn, state) {
            if (state) {
                btn.children().addClass('fa-spin');
                btn.contents().last().replaceWith(" Loading");
            } else {
                setTimeout(function () {
                    btn.children().removeClass('fa-spin');
                    btn.contents().last().replaceWith(" Refresh");
                }, 2000);
            }
        }
    </script>


<script type="text/javascript">
    var prevId= null;
    var prevColor = null;
    function getTaskTab(id, childId, treeType) {
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
                document.getElementById("TASKRESULT").innerHTML=xmlhttp.responseText;
                document.getElementById("TASKRESULT").style.backgroundColor='#dfdfdf';
            }
        }
        xmlhttp.open("GET","<@ofbizUrl>getProjectListTab?businessTreeId="+childId+"&businessTreeType="+treeType+"</@ofbizUrl>",true);
        xmlhttp.send();
        document.getElementById("DIV_TASK_RESULT_LIST").style.display = 'none';
    }



</script>


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

    <!-- Tablesorter: required -->
<!--     <script src="/images/project_demo/js/jquery.tablesorter.js"></script>
    <script src="/images/project_demo/js/widget-storage.js"></script>
    <script src="/images/project_demo/js/widget-filter.js"></script> -->



</body>

</html>