<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>INSPINIA | Data Tables</title>

    <link href="/images/project_demo/css/bootstrap.min.css" rel="stylesheet">
    <link href="/images/project_demo/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="/images/project_demo/css/plugins/dataTables/datatables.min.css" rel="stylesheet">

    <link href="/images/project_demo/css/plugins/bootstrap-tagsinput/bootstrap-tagsinput.css" rel="stylesheet">

    <link href="/images/project_demo/css/plugins/chosen/bootstrap-chosen.css" rel="stylesheet">

    <!-- datetimepicker -->
    <link rel="stylesheet" type="text/css" href="/images/project_demo/css/jquery.datetimepicker.css"/>

    <link href="/images/project_demo/css/plugins/select2/select2.min.css" rel="stylesheet">


    <link href="/images/project_demo/css/animate.css" rel="stylesheet">
    <link href="/images/project_demo/css/style_project_demo.css" rel="stylesheet">
    <!-- <link href="/images/project_demo/css/style.css" rel="stylesheet"> -->

</head>

<body class="pace-done mini-navbar">

    <div id="wrapper">

        <div id="page-wrapper_1" class="gray-bg">
        <div class="row border-bottom">
        <!--
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
                    <span class="m-r-sm text-muted welcome-message">Welcome to GuDao+ Admin Theme.</span>
                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-envelope"></i>  <span class="label label-warning">16</span>
                    </a>
                    <ul class="dropdown-menu dropdown-messages">
                        
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
                    <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                        <i class="fa fa-bell"></i>  <span class="label label-primary">8</span>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        
                    </ul>
                </li>


                <li>
                    <a href="/gudao/control/logout">
                        <i class="fa fa-sign-out"></i> Log out
                    </a>
                </li>
            </ul>

        </nav>
        -->
        </div>
            <div class="row wrapper border-bottom white-bg page-heading">
                <div class="col-lg-12">
                    <h2>${uiLabelMap.OrderStatusTracking}</h2>
                    <div>
                        <!-- <div class="ibox-content"> -->
                        <div class="">
                            <form role="form" action="<@ofbizUrl>table_data_tables</@ofbizUrl>" class="form-inline" id="saveReportForm" target="targetIfr">
                               <!--  <ul>
                                    <li>
                                        <div class="form-group">
                                            <label for="StartDate">开始时间</label>
                                            <input type="text" class="form-control datetimepicker3" name="startDate" value="" id="" placeholder=""/>
                                        </div>
                                        <div class="form-group">
                                            <label for="StartDate">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;结束时间</label>
                                            <input type="text" class="form-control datetimepicker3" name="startDate" value="" id="" placeholder=""/>
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputEmail2">买家平台ID</label>
                                            <input type="text" placeholder="" id="exampleInputEmail2"
                                                   class="form-control">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputEmail2">&nbsp;PP交易ID</label>
                                            <input type="text" placeholder="" id="exampleInputEmail2"
                                                               class="form-control">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputEmail2">卖家简称</label>
                                            <input type="text" placeholder="" id="exampleInputEmail2"
                                                   class="form-control">
                                        </div>
                                        <div class="form-group">
                                                <select class="select2_demo_1 form-control">
                                                    <option value="1">店铺SKU</option>
                                                    <option value="2">商品SKU</option>
                                                </select>
                                                <input class="tagsinput form-control" type="text" placeholder="多个SKU请用逗号隔开" value=""/>
                                                <div class="checkbox m-r-xs">
                                                    <input type="checkbox" id="checkbox1">
                                                    <label for="checkbox1">
                                                        模糊
                                                    </label>
                                                </div>
                                        </div>
                                        
                                        <div class="form-group">
                                            <label for="exampleInputEmail2">&nbsp;&nbsp;&nbsp;&nbsp;跟踪号</label>
                                            <input type="text" placeholder="多个请用逗号隔开" id="exampleInputEmail2"
                                                   class="tagsinput form-control">
                                        </div>
                                        
                                    </li>
                                    <li>
                                        <div class="form-group">
                                            <label for="exampleInputEmail2">&nbsp;&nbsp;&nbsp;&nbsp;收货人</label>
                                            <input type="text" placeholder="" id="exampleInputEmail2"
                                                               class="form-control">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputEmail2">&nbsp;收款人邮箱</label>
                                            <input type="text" placeholder="" id="exampleInputEmail2"
                                                               class="form-control">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputEmail2">&nbsp;&nbsp;&nbsp;&nbsp;电话号码</label>
                                            <input type="text" placeholder="" id="exampleInputEmail2"
                                                                   class="form-control">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputEmail2">&nbsp;&nbsp;订单编号</label>
                                            <input type="text" placeholder="" id="exampleInputEmail2"
                                                               class="form-control">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputEmail2">&nbsp;物流方式</label>
                                            <select class="select2_demo_1 form-control" id="exampleInputEmail2">
                                                <option value="1">Option 1</option>
                                                <option value="2">Option 2</option>
                                                <option value="3">Option 3</option>
                                                <option value="4">Option 4</option>
                                                <option value="5">Option 5</option>
                                            </select>
                                        </div>

                                        <div class="form-group">
                                            <label for="exampleInputEmail2">订单编号</label>
                                            <input type="text" placeholder="多个用逗号隔开" id="exampleInputEmail2"
                                                   class="form-control tagsinput">
                                        </div>
                                        <div class="form-group">
                                            <label for="exampleInputEmail2">店铺单号</label>
                                            <input type="text" placeholder="多个请用逗号隔开" id="exampleInputEmail2"
                                                   class="tagsinput form-control">
                                        </div>

                                    </li>
                                    <li>
                                        <div>
                                            <div id="flip">
                                                <strong class="text-navy">其它查询条件</strong>
                                            </div>
                                            <div id="panel">
                                                <ul>
                                                    <li>
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2">快递公司</label>
                                                        <select class="select2_demo_1 form-control">
                                                            <option value="1">Option 1</option>
                                                            <option value="2">Option 2</option>
                                                            <option value="3">Option 3</option>
                                                            <option value="4">Option 4</option>
                                                            <option value="5">Option 5</option>
                                                        </select>
                                                    </div>
                                                    
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2">收件国家</label>
                                                        <select class="select2_demo_1 form-control">
                                                            <option value="1">Option 1</option>
                                                            <option value="2">Option 2</option>
                                                            <option value="3">Option 3</option>
                                                            <option value="4">Option 4</option>
                                                            <option value="5">Option 5</option>
                                                        </select>
                                                    </div>
                                                    
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2">自定义标签</label>
                                                        <select class="select2_demo_1 form-control">
                                                            <option value="1">Option 1</option>
                                                            <option value="2">Option 2</option>
                                                            <option value="3">Option 3</option>
                                                            <option value="4">Option 4</option>
                                                            <option value="5">Option 5</option>
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2">平台交易ID</label>
                                                        <input type="text" placeholder="" id="exampleInputEmail2"
                                                               class="form-control">
                                                    </div>
                                                    </li>
                                                    <li>
                                                        
                                                        <div class="form-group">
                                                            <label for="exampleInputEmail2">客户PP邮箱</label>
                                                            <input type="text" placeholder="" id="exampleInputEmail2"
                                                               class="form-control">
                                                        </div>
                                                        
                                                        <div class="form-group">
                                                            <label for="exampleInputEmail2">邮编</label>
                                                            <input type="text" placeholder="" id="exampleInputEmail2"
                                                               class="form-control">
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="exampleInputEmail2">仓库</label>
                                                            <select class="select2_demo_1 form-control">
                                                                <option value="1">Option 1</option>
                                                                <option value="2">Option 2</option>
                                                                <option value="3">Option 3</option>
                                                                <option value="4">Option 4</option>
                                                                <option value="5">Option 5</option>
                                                            </select>
                                                        </div>
                                                        <div class="form-group" id="data_5">
                                                            <label>库位</label>
                                                            <div class="input-daterange input-group" id="">
                                                                <input type="text" class="input-sm form-control" name="start" value=""/>
                                                                <span class="input-group-addon">to</span>
                                                                <input type="text" class="input-sm form-control" name="end" value="" />
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <label for="exampleInputEmail2">地址</label>
                                                            <input type="text" placeholder="" id="exampleInputEmail2"
                                                               class="form-control">
                                                        </div>
                                                        <div class="form-group" id="data_5">
                                                            <label>订单重量</label>
                                                            <div class="input-daterange input-group" id="">
                                                                <input type="text" class="input-sm form-control" name="start" value=""/>
                                                                <span class="input-group-addon">to</span>
                                                                <input type="text" class="input-sm form-control" name="end" value="" />
                                                            </div>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </li>
                                </ul> -->
                                <!-- <div class="ibox-content"> -->
                                <div class="">
                                    <table class="" id="table_search" width="100%" style="border-collapse:separate; border-spacing:0px 10px;">
                                        <tbody>
                                            <tr>
                                                <td>
                                                    <div class="form-group">
                                                        <label for="StartDate" class="text-right">${uiLabelMap.startTime}</label>
                                                        <input type="text" class="form-control datetimepicker3 table_data_search" name="startDate" value="" id="" placeholder=""/>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="form-group">
                                                        <!-- <label for="EndDate" class="text-right">${uiLabelMap.endTime}</label> -->
                                                            <label for="StartDate" class="text-right">${uiLabelMap.endTime}</label>
                                                            <input type="text" class="form-control datetimepicker3 table_data_search" name="startDate" value="" id="" placeholder=""/>
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="form-group">
                                                        <label for="" class="text-right">${uiLabelMap.BuyerPlatformID}</label>
                                                        <input type="text" placeholder="" id="exampleInputEmail2"
                                                               class="form-control table_data_search">
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2" class="text-right">${uiLabelMap.PPTransactionID}</label>
                                                        <input type="text" placeholder="" id="exampleInputEmail2"
                                                                           class="form-control table_data_search">
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2" class="text-right">${uiLabelMap.RecipientMailbox}</label>
                                                        <input id="email" name="email" type="email" placeholder=""
                                                                           class="form-control table_data_search">
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2" class="text-right">${uiLabelMap.consignee}</label>
                                                        <input type="text" placeholder="" id="exampleInputEmail2"
                                                                           class="form-control table_data_search">
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2" class="text-right">${uiLabelMap.orderNumber}</label>
                                                        <input type="text" placeholder="" id="exampleInputEmail2"
                                                                           class="form-control table_data_search">
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2" class="text-right">${uiLabelMap.phoneNumber}</label>
                                                        <input type="text" placeholder="" id="exampleInputEmail2"
                                                                               class="form-control table_data_search">
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2" class="text-right">${uiLabelMap.TrackingNumber}</label>
                                                        <input type="text" placeholder="" id="exampleInputEmail2"
                                                                           class="form-control table_data_search">
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="form-group">
                                                        <label for="exampleInputEmail2" class="text-right">${uiLabelMap.LogisticsModel}</label>
                                                        <select class="select2_demo_1 form-control table_data_search" id="exampleInputEmail2">
                                                            <option value="1">Option 1</option>
                                                            <option value="2">Option 2</option>
                                                            <option value="3">Option 3</option>
                                                            <option value="4">Option 4</option>
                                                            <option value="5">Option 5</option>
                                                        </select>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <div class="ibox-content">
                                        <div class="text-right">
                                            <button type="submit" class="btn btn-sm btn-primary m-t-n-xs"><strong>${uiLabelMap.search}</strong></button>
                                            <button type="reset" class="btn btn-sm btn-warning m-t-n-xs"><strong>&nbsp;Clear&nbsp;&nbsp;</strong></button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                            <iframe name="targetIfr" style="display:none"></iframe>
                        </div>
                    </div>
                </div>
            </div>
        <div class="wrapper wrapper-content animated fadeInRight">
            <div class="row">
                <div class="col-lg-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>${uiLabelMap.OrderStatusTable}</h5>
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

                    <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover dataTables-example" style="width: 100%;" width="100%">
                    <thead>
                    <tr>
                        <th>${uiLabelMap.PPTransactionID}</th>
                        <th>${uiLabelMap.BuyerPlatformID}</th>
                        <th>${uiLabelMap.consignee}</th>
                        <th>${uiLabelMap.TransactionTime}</th>
                        <th>${uiLabelMap.deliveryTime}</th>
                        <th>${uiLabelMap.TrackingNumber}</th>
                        <th>${uiLabelMap.orderNumber}</th>
                        <th>${uiLabelMap.TotalWeight}</th>
                        <th>${uiLabelMap.CourierCompany}</th>
                        <th>${uiLabelMap.Delay}</th>
                        <th>${uiLabelMap.sellerReferredToAs}</th>
                        <th>${uiLabelMap.LogisticsModel}</th>
                        <th>${uiLabelMap.RecipientMailbox}</th>
                        <th>${uiLabelMap.ConsigneeAddress}</th>
                        <th>${uiLabelMap.ReceivingCity}</th>
                        <th>${uiLabelMap.StateOrProvince}</th>
                        <th>${uiLabelMap.CollectionPostcode}</th>
                        <th>${uiLabelMap.CollectTheCountryCode}</th>
                        <th>${uiLabelMap.receivingCountry}</th>
                        <th>${uiLabelMap.ConsigneePhone}</th>
                        <th>${uiLabelMap.PaymentStatus}</th>
                        <th>${uiLabelMap.ShopOrder}</th>
                        <th>${uiLabelMap.DespatchingPlatform}</th>
                        <th>${uiLabelMap.SellerPlatformID}</th>
                        <th>${uiLabelMap.ConsigneeCountryCn}</th>
                        <th>${uiLabelMap.deliveryStatus}</th>
                        <th>${uiLabelMap.orderStatus}</th>
                        <th>${uiLabelMap.PayerMailbox}</th>
                        <th>${uiLabelMap.PaymentFirstName}</th>
                        <th>${uiLabelMap.PaymentLastName}</th>
                        <th>${uiLabelMap.CommodityInformation}</th>
                        <th>${uiLabelMap.PickingPerson}</th>
                        <th>${uiLabelMap.OriginalPickingPerson}</th>
                        <th>${uiLabelMap.ReviewPerson}</th>
                        <th>${uiLabelMap.ReviewOrderTime}</th>
                        <th>${uiLabelMap.WeighingTime}</th>
                        <th>${uiLabelMap.refundAmount}</th>
                        <th>${uiLabelMap.RedirectionNumber}</th>
                        <th>${uiLabelMap.CustomTag}</th>
                        <th>${uiLabelMap.TypeOfPayment}</th>
                        <th>${uiLabelMap.orderAmount}</th>
                        <th>${uiLabelMap.TradingCurrencies}</th>
                        <th>${uiLabelMap.StoreMode}</th>
                        <th>${uiLabelMap.CourierFees}</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="gradeX">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>

                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeA">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>
                    <tr class="gradeC">
                        <td>${uiLabelMap.PPTransactionID}</td>
                        <td>${uiLabelMap.BuyerPlatformID}</td>
                        <td>${uiLabelMap.consignee}</td>
                        <td>${uiLabelMap.TransactionTime}</td>
                        <td>${uiLabelMap.deliveryTime}</td>
                        <td>${uiLabelMap.TrackingNumber}</td>
                        <td>${uiLabelMap.orderNumber}</td>
                        <td>${uiLabelMap.TotalWeight}</td>
                        <td>${uiLabelMap.CourierCompany}</td>
                        <td>${uiLabelMap.Delay}</td>
                        <td>${uiLabelMap.sellerReferredToAs}</td>
                        <td>${uiLabelMap.LogisticsModel}</td>
                        <td>${uiLabelMap.RecipientMailbox}</td>
                        <td>${uiLabelMap.ConsigneeAddress}</td>
                        <td>${uiLabelMap.ReceivingCity}</td>
                        <td>${uiLabelMap.StateOrProvince}</td>
                        <td>${uiLabelMap.CollectionPostcode}</td>
                        <td>${uiLabelMap.CollectTheCountryCode}</td>
                        <td>${uiLabelMap.receivingCountry}</td>
                        <td>${uiLabelMap.ConsigneePhone}</td>
                        <td>${uiLabelMap.PaymentStatus}</td>
                        <td>${uiLabelMap.ShopOrder}</td>
                        <td>${uiLabelMap.DespatchingPlatform}</td>
                        <td>${uiLabelMap.SellerPlatformID}</td>
                        <td>${uiLabelMap.ConsigneeCountryCn}</td>
                        <td>${uiLabelMap.deliveryStatus}</td>
                        <td>${uiLabelMap.orderStatus}</td>
                        <td>${uiLabelMap.PayerMailbox}</td>
                        <td>${uiLabelMap.PaymentFirstName}</td>
                        <td>${uiLabelMap.PaymentLastName}</td>
                        <td>${uiLabelMap.CommodityInformation}</td>
                        <td>${uiLabelMap.PickingPerson}</td>
                        <td>${uiLabelMap.OriginalPickingPerson}</td>
                        <td>${uiLabelMap.ReviewPerson}</td>
                        <td>${uiLabelMap.ReviewOrderTime}</td>
                        <td>${uiLabelMap.WeighingTime}</td>
                        <td>${uiLabelMap.refundAmount}</td>
                        <td>${uiLabelMap.RedirectionNumber}</td>
                        <td>${uiLabelMap.CustomTag}</td>
                        <td>${uiLabelMap.TypeOfPayment}</td>
                        <td>${uiLabelMap.orderAmount}</td>
                        <td>${uiLabelMap.TradingCurrencies}</td>
                        <td>${uiLabelMap.StoreMode}</td>
                        <td>${uiLabelMap.CourierFees}</td>
                    </tr>

                    </tbody>
                    <tfoot>
                    <tr>
                        <th>${uiLabelMap.PPTransactionID}</th>
                        <th>${uiLabelMap.BuyerPlatformID}</th>
                        <th>${uiLabelMap.consignee}</th>
                        <th>${uiLabelMap.TransactionTime}</th>
                        <th>${uiLabelMap.deliveryTime}</th>
                        <th>${uiLabelMap.TrackingNumber}</th>
                        <th>${uiLabelMap.orderNumber}</th>
                        <th>${uiLabelMap.TotalWeight}</th>
                        <th>${uiLabelMap.CourierCompany}</th>
                        <th>${uiLabelMap.Delay}</th>
                        <th>${uiLabelMap.sellerReferredToAs}</th>
                        <th>${uiLabelMap.LogisticsModel}</th>
                        <th>${uiLabelMap.RecipientMailbox}</th>
                        <th>${uiLabelMap.ConsigneeAddress}</th>
                        <th>${uiLabelMap.ReceivingCity}</th>
                        <th>${uiLabelMap.StateOrProvince}</th>
                        <th>${uiLabelMap.CollectionPostcode}</th>
                        <th>${uiLabelMap.CollectTheCountryCode}</th>
                        <th>${uiLabelMap.receivingCountry}</th>
                        <th>${uiLabelMap.ConsigneePhone}</th>
                        <th>${uiLabelMap.PaymentStatus}</th>
                        <th>${uiLabelMap.ShopOrder}</th>
                        <th>${uiLabelMap.DespatchingPlatform}</th>
                        <th>${uiLabelMap.SellerPlatformID}</th>
                        <th>${uiLabelMap.ConsigneeCountryCn}</th>
                        <th>${uiLabelMap.deliveryStatus}</th>
                        <th>${uiLabelMap.orderStatus}</th>
                        <th>${uiLabelMap.PayerMailbox}</th>
                        <th>${uiLabelMap.PaymentFirstName}</th>
                        <th>${uiLabelMap.PaymentLastName}</th>
                        <th>${uiLabelMap.CommodityInformation}</th>
                        <th>${uiLabelMap.PickingPerson}</th>
                        <th>${uiLabelMap.OriginalPickingPerson}</th>
                        <th>${uiLabelMap.ReviewPerson}</th>
                        <th>${uiLabelMap.ReviewOrderTime}</th>
                        <th>${uiLabelMap.WeighingTime}</th>
                        <th>${uiLabelMap.refundAmount}</th>
                        <th>${uiLabelMap.RedirectionNumber}</th>
                        <th>${uiLabelMap.CustomTag}</th>
                        <th>${uiLabelMap.TypeOfPayment}</th>
                        <th>${uiLabelMap.orderAmount}</th>
                        <th>${uiLabelMap.TradingCurrencies}</th>
                        <th>${uiLabelMap.StoreMode}</th>
                        <th>${uiLabelMap.CourierFees}</th>
                    </tr>
                    </tfoot>
                    </table>
                    </div>

                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="tabs-container">
                        <ul class="nav nav-tabs">
                            <li class=""><a data-toggle="tab" href="#tab-1">${uiLabelMap.orderStatus}</a></li>
                            <li class=""><a data-toggle="tab" href="#tab-2">${uiLabelMap.CommodityInformation}</a></li>
                            <li class="active"><a data-toggle="tab" href="#tab-3">${uiLabelMap.OperationLog}</a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="tab-1" class="tab-pane">
                                <div class="panel-body">
                                    <strong>${uiLabelMap.orderStatus}</strong>

                                    <p>null</p>
                                </div>
                            </div>
                            <div id="tab-2" class="tab-pane">
                                <div class="panel-body">
                                    <strong>${uiLabelMap.CommodityInformation}</strong>

                                    <p>null</p>
                                </div>
                            </div>
                            <div id="tab-3" class="tab-pane active">
                                <div class="panel-body">
                                    <strong>${uiLabelMap.OperationLog}</strong>
                                    <pre>
朱一鸣 2017-06-23 12:11:32.510 导入运费：116.23,通过单号/跟踪号："83173833780",ACK:""
LG007 2017-06-21 16:43:58.440 待发货订单转至已发货.
LG004 2017-06-21 11:29:29.480 订单捆绑、核对完成;拣货人：LG004, 包装人: LG004
包裹"4566983"图片上传失败!
LG004 2017-06-21 11:29:29.480 捆单称重，更改：跟踪号旧：83173833780新：83173833780,重量:1.1072,运费:127.68
LG004 2017-06-21 11:29:28.620 捆绑称重重量检测:exec P_Fr_BoundCheckWeight 479,1.1072,'4566983',0
LG007 2017-06-20 15:44:44.090 批量核对订单->未包装
LG007 2017-06-20 15:35:35.950 未包装订单->调用打印"拣货单—按箱拣货"
LG007 2017-06-20 15:35:34.053 未拣货->未核单
LG007 2017-06-20 15:33:23.837 待处理订单->按物流方式打印"C:\Users\GD\Desktop\普源\report\106\万色\wish邮平邮上海仓 (2).fr3"
LG007 2017-06-19 19:35:42.720 将订单转至未拣货！
ADMIN 2017-06-19 18:55:37.773 合并订单编号:4566310***Wish Auto 发货成功!
ADMIN 2017-06-19 18:55:35.500 合并订单编号:4566309***Wish Auto 发货成功!
LG007 2017-06-19 18:37:21.657 WanSeWishYou订单提交成功! 跟踪号:83173833780,运单号:4566983,其他信息:83173833780
LG007 2017-06-19 17:27:14.690 自动:[万色物流][011Wish邮上海平邮];仓库:古道浦江5楼（直邮）数:5;kc:6;zy:1;qz:0;SKUID:230629仓库:古道浦江5楼（直邮）数:4;kc:7;zy:3;qz:0;SKUID:230680,并占用
张祥 2017-06-19 09:50:55.883 合并为新订单,合并订单号：4566310,4566309--Wish-maomao226,--5946a383e8c5231a87abc636,5946a383e8c5231a87abc637,
                                    </pre>
                                </div>
                            </div>
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
                <strong></strong> GuDao Company &copy; 2010-2017
            </div>
        </div>

        </div>
        </div>



    <!-- Mainly scripts -->
    <script src="/images/project_demo/js/jquery-3.1.1.min.js"></script>
    <script src="/images/project_demo/js/bootstrap.min.js"></script>
    <script src="/images/project_demo/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="/images/project_demo/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

    <script src="/images/project_demo/js/plugins/dataTables/datatables.min.js"></script>

        <!-- Chosen -->
    <script src="/images/project_demo/js/plugins/chosen/chosen.jquery.js"></script>

    <!-- Custom and plugin javascript -->
    <script src="/images/project_demo/js/inspinia.js"></script>
    <script src="/images/project_demo/js/plugins/pace/pace.min.js"></script>

        <!-- Tags Input -->
    <script src="/images/project_demo/js/plugins/bootstrap-tagsinput/bootstrap-tagsinput.js"></script>

    <!-- datetimepicker -->
    <script src="/images/project_demo/js/jquery.datetimepicker.full.js"></script>

    <!-- Select2 -->
    <script src="/images/project_demo/js/plugins/select2/select2.full.min.js"></script>

    <!-- <script src="/images/project_demo/js/plugins/bootstrap-select/js/bootstrap-select.min.js"></script> -->

    <!-- Page-Level Scripts -->
    <script>
        $(document).ready(function(){
           var oTable = $('.dataTables-example').DataTable({
                "scrollY": 300,
                "sScrollX": "100%",
                "sScrollXInner": "110%",
                "bScrollCollapse": true,
                "pageLength": 25,
                "responsive": true,
                "dom": '<"html5buttons"B>lTfgitp',
                "buttons": [
                    { extend: ''},
                    // { extend: 'copy'},
                    // {extend: 'csv'},
                    // {extend: 'excel', title: 'ExampleFile'},
                    // {extend: 'pdf', title: 'ExampleFile'},

                    // {extend: 'print',
                    //  customize: function (win){
                    //         $(win.document.body).addClass('white-bg');
                    //         $(win.document.body).css('font-size', '10px');

                    //         $(win.document.body).find('table')
                    //                 .addClass('compact')
                    //                 .css('font-size', 'inherit');
                    // }
                    // }
                ],
                "bAutoWidth": true,//自动宽度
                "language": {
                    "emptyTable":     "Custom Search Message Result Is Empty"
                }

            });

            $('.tagsinput').tagsinput({
                tagClass: 'label label-primary'
            });

            $('.chosen-select').chosen({width: "100%"});

            $('.datetimepicker3').datetimepicker({
            format: 'y-m-d h:i:s a'
            });

            $(".select2_demo_1").select2();

            $("#flip").click(function(){
                $("#panel").slideToggle("slow");
            });

            //然后通过trigger来触发reset按钮 
            $("button[type='reset']").trigger("click");//触发reset按钮 

            //通过form表单的dom对象的reset方法来清空
            $('form')[0].reset();

                
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
