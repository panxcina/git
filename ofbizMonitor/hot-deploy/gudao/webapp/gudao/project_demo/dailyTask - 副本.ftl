<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>INSPINIA | Dashboard</title>

    <link href="/images/project_demo/css/bootstrap.min.css" rel="stylesheet">
    <link href="/images/project_demo/font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- Morris -->
    <link href="/images/project_demo/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">

    <!-- Gritter -->
    <link href="/images/project_demo/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">

    <link href="/images/project_demo/css/animate.css" rel="stylesheet">
    <link href="/images/project_demo/css/style_project_demo.css" rel="stylesheet">


    <link href="/images/project_demo/css/plugins/iCheck/custom.css" rel="stylesheet">

    <link href="/images/project_demo/css/plugins/fullcalendar/fullcalendar.css" rel="stylesheet">
    <link href="/images/project_demo/css/plugins/fullcalendar/fullcalendar.print.css" rel='stylesheet' media='print'>



    <!-- <script src="/images/project_demo/css/animate.css" type="text/javascript"></script>
    <script src="/images/project_demo/css/plugins/morris/morris-0.4.3.min.css" type="text/javascript"></script> -->
</head>

<body class="pace-done mini-navbar">
    <div id="wrapper">
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">

                        <div class="dropdown profile-element"> 
                            <span>
                                <img alt="image" class="img-circle" src="/images/project_demo/img/<#if '${userLogin.userLoginId}' == '1pan.shi1'>${userLogin.userLoginId}<#elseif '${userLogin.userLoginId}' == 'su.qichen'>${userLogin.userLoginId}<#elseif '${userLogin.userLoginId}' == 'yasin.lyyas'>${userLogin.userLoginId}<#else>pan.shi</#if>.png">
                            </span>

                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear1"> 
                                <!-- <span class="">  -->
                                    <span class="block m-t-xs"> <strong class="font-bold">${userLogin.userLoginId}</strong></span> 
                                    <span class="text-muted text-xs block">User <b class="caret"></b></span> 
                                </span>
                            </a>
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
                                    </#list>

                            </li>

                        </ul>
                    </li>

                </ul>

            </div>
        </nav>

        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
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
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-envelope"></i>  <span class="label label-warning">16</span>
                    </a>
                    <ul class="dropdown-menu dropdown-messages">
                        <li>
                            <div class="dropdown-messages-box">
                                <a href="#" class="pull-left">
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
                                <a href="#" class="pull-left">
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
                                <a href="#" class="pull-left">
                                    <img alt="image" class="img-circle" src="/images/project_demo/img/profile.jpg">
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
                                <a href="#">
                                    <i class="fa fa-envelope"></i> <strong>Read All Messages</strong>
                                </a>
                            </div>
                        </li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="index.html#">
                        <i class="fa fa-bell"></i>  <span class="label label-primary">8</span>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-envelope fa-fw"></i> You have 16 messages
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                    <span class="pull-right text-muted small">12 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <div class="text-center link-block">
                                <a href="#">
                                    <strong>See All Alerts</strong>
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
        <div class="row">
            <div class="col-lg-12">

                <div class="wrapper wrapper-content">
                    <div class="row animated fadeInDown">
                        <div class="col-lg-3">
                            <div class="ibox float-e-margins">
                                <div class="ibox-title">
                                    <h5>Draggable Events</h5>
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
                                    <div id='external-events'>
                                        <p>Drag a event and drop into callendar.</p>
                                        <div class='external-event navy-bg'>Go to shop and buy some products.</div>
                                        <div class='external-event navy-bg'>Check the new CI from Corporation.</div>
                                        <div class='external-event navy-bg'>Send documents to John.</div>
                                        <div class='external-event navy-bg'>Phone to Sandra.</div>
                                        <div class='external-event navy-bg'>Chat with Michael.</div>
                                        <p class="m-t">
                                            <input type='checkbox' id='drop-remove' class="i-checks" checked /> <label for='drop-remove'>remove after drop</label>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="ibox float-e-margins">
                                <div class="ibox-content">
                                    <h2>FullCalendar</h2> is a jQuery plugin that provides a full-sized, drag & drop calendar like the one below. It uses AJAX to fetch events on-the-fly for each month and is
                                    easily configured to use your own feed format (an extension is provided for Google Calendar).
                                    <p>
                                        <a href="http://arshaw.com/fullcalendar/" target="_blank">FullCalendar documentation</a>
                                    </p>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-9">
                            <div class="ibox float-e-margins">
                                <div class="ibox-title">
                                    <h5>Striped Table </h5>
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
                                    <div id="calendar"></div>
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
    </div>

    <!-- Mainly scripts -->
    <script src="/images/project_demo/js/jquery-1.10.2.js"></script>
    <!-- <script src="/images/project_demo/js/jquery-3.1.1.min.js"></script> -->
    <script src="/images/project_demo/js/bootstrap.min.js"></script>
    <script src="/images/project_demo/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/images/project_demo/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

    <!-- Flot -->
    <script src="/images/project_demo/js/plugins/flot/jquery.flot.js"></script>
    <script src="/images/project_demo/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
    <script src="/images/project_demo/js/plugins/flot/jquery.flot.spline.js"></script>
    <script src="/images/project_demo/js/plugins/flot/jquery.flot.resize.js"></script>
    <script src="/images/project_demo/js/plugins/flot/jquery.flot.pie.js"></script>

    <!-- Peity -->
    <script src="/images/project_demo/js/plugins/peity/jquery.peity.min.js"></script>
    <script src="/images/project_demo/js/demo/peity-demo.js"></script>

    <!-- Custom and plugin javascript -->
    <script src="/images/project_demo/js/inspinia.js"></script>
    <script src="/images/project_demo/js/plugins/pace/pace.min.js"></script>

    <!-- jQuery UI -->
    <script src="/images/project_demo/js/plugins/jquery-ui/jquery-ui.min.js"></script>

    <!-- GITTER -->
    <script src="/images/project_demo/js/plugins/gritter/jquery.gritter.min.js"></script>

    <!-- EayPIE -->
    <script src="/images/project_demo/js/plugins/easypiechart/jquery.easypiechart.js"></script>

    <!-- Sparkline -->
    <script src="/images/project_demo/js/plugins/sparkline/jquery.sparkline.min.js"></script>

    <!-- Sparkline demo data  -->
    <script src="/images/project_demo/js/demo/sparkline-demo.js"></script>

    <!-- iCheck -->
    <script src="/images/project_demo/js/plugins/iCheck/icheck.min.js"></script>

    <!-- Full Calendar -->
    <script src="/images/project_demo/js/plugins/fullcalendar/fullcalendar.min.js"></script>

    <script>
        $(document).ready(function() {
            // WinMove();
            // setTimeout(function() {
            //     $.gritter.add({
            //         title: '欢迎来到古道项目管理系统',
            //         text: '这是只是一个展示页，请期待2.0新版本~~',
            //         time: 2000
            //     });
            // }, 2000);

            $('.i-checks').iCheck({
                    checkboxClass: 'icheckbox_square-green',
                    radioClass: 'iradio_square-green'
                });

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


            /* initialize the calendar
             -----------------------------------------------------------------*/
            var date = new Date();
            var d = date.getDate();
            var m = date.getMonth();
            var y = date.getFullYear();

            $('#calendar').fullCalendar({
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                editable: true,
                droppable: true, // this allows things to be dropped onto the calendar
                drop: function() {
                    // is the "remove after drop" checkbox checked?
                    if ($('#drop-remove').is(':checked')) {
                        // if so, remove the element from the "Draggable Events" list
                        $(this).remove();
                    }
                },
                events: [
                    {
                        title: 'All Day Event',
                        start: new Date(y, m, 1)
                    },
                    {
                        title: 'Long Event',
                        start: new Date(y, m, d-5),
                        end: new Date(y, m, d-2)
                    },
                    {
                        id: 999,
                        title: 'Repeating Event',
                        start: new Date(y, m, d-3, 16, 0),
                        allDay: false
                    },
                    {
                        id: 999,
                        title: 'Repeating Event',
                        start: new Date(y, m, d+4, 16, 0),
                        allDay: false
                    },
                    {
                        title: 'Meeting',
                        start: new Date(y, m, d, 10, 30),
                        allDay: false
                    },
                    {
                        title: 'Lunch',
                        start: new Date(y, m, d, 12, 0),
                        end: new Date(y, m, d, 14, 0),
                        allDay: false
                    },
                    {
                        title: 'Birthday Party',
                        start: new Date(y, m, d+1, 19, 0),
                        end: new Date(y, m, d+1, 22, 30),
                        allDay: false
                    },
                    {
                        title: 'Click for Google',
                        start: new Date(y, m, 28),
                        end: new Date(y, m, 29),
                        url: 'http://google.com/'
                    }
                ]
            });

    </script>

</body>
</html>
