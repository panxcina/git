<script type="text/javascript">
   $(function () { $('#collapseFour').collapse({
      toggle: false
   })});
   $(function () { $('#collapseTwo').collapse('show')});
   $(function () { $('#collapseThree').collapse('toggle')});
   $(function () { $('#collapseOne').collapse('hide')});
</script> 
<div>
<ul id="myTab" class="nav nav-tabs">
   
   <li class="active dropdown">
      <a href="#" id="myTabDrop1" class="dropdown-toggle" 
         data-toggle="dropdown">人事行政部
         <b class="caret"></b>
      </a>
      <ul class="dropdown-menu" role="menu" aria-labelledby="myTabDrop1">
         <li><a href="#zsb" tabindex="-1" data-toggle="tab">招商部</a></li>
         <li><a href="#rsb" tabindex="-1" data-toggle="tab">人事部</a></li>
         <li><a href="#xzb" tabindex="-1" data-toggle="tab">行政部</a></li>
      </ul>
   </li>
   
   
   <li class="dropdown">
      <a href="#" id="myTabDrop1" class="dropdown-toggle" 
         data-toggle="dropdown">客户服务部
         <b class="caret"></b>
      </a>
      <ul class="dropdown-menu" role="menu" aria-labelledby="myTabDrop1">
         <li><a href="#qtb" tabindex="-1" data-toggle="tab">青铜部</a></li>
         <li><a href="#byb" tabindex="-1" data-toggle="tab">白银部</a></li>
         <li><a href="#hjb" tabindex="-1" data-toggle="tab">黄金部</a></li>
      </ul>
   </li>
   
   
   
   <li class="dropdown">
      <a href="#" id="myTabDrop1" class="dropdown-toggle" 
         data-toggle="dropdown">系统数据部
         <b class="caret"></b>
      </a>
      <ul class="dropdown-menu" role="menu" aria-labelledby="myTabDrop1">
         <li><a href="#jmeter" tabindex="-1" data-toggle="tab">技术部</a></li>
         <li><a href="#sjb" tabindex="-1" data-toggle="tab">数据部</a></li>
         <li><a href="#cwb" tabindex="-1" data-toggle="tab">财务部</a></li>
      </ul>
   </li>
   <li class="dropdown">
      <a href="#" id="myTabDrop1" class="dropdown-toggle" 
         data-toggle="dropdown">业务支持部
         <b class="caret"></b>
      </a>
      <ul class="dropdown-menu" role="menu" aria-labelledby="myTabDrop1">
         <li><a href="#jmeter" tabindex="-1" data-toggle="tab">开发部</a></li>
         <li><a href="#ejb" tabindex="-1" data-toggle="tab">销售部</a></li>
      </ul>
   </li>

</ul>
<div id="myTabContent" class="tab-content">
   <div class="tab-pane fade in active" id="zsb">
      <p>这是招商部</p>
   </div>
   <div class="tab-pane fade" id="rsb">
      <p>这是人事部</p>
   </div>
   <div class="tab-pane fade" id="xzb">
      <p>这是行政部</p>
   </div>
   <div class="tab-pane fade" id="jmeter">
      <p>jMeter 是一款开源的测试软件。它是 100% 纯 Java 应用程序，用于负载和性能测试。</p>
   </div>
   
   <div class="tab-pane fade" id="sjb">
        <div class="panel-group" id="accordion">
           <div class="panel panel-default">
              <div class="panel-heading">
                 <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" 
                       href="#collapseOne">
                       项目未完成
                    </a>
                 </h4>
              </div>
              <div id="collapseOne" class="panel-collapse collapse in">
                 <div class="panel-body">
                    Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred 
                    nesciunt sapiente ea proident. Ad vegan excepteur butcher vice 
                    lomo.
                 </div>
              </div>
           </div>
           <div class="panel panel-success">
              <div class="panel-heading">
                 <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" 
                       href="#collapseTwo">
                        项目进行中
                    </a>
                 </h4>
              </div>
              <div id="collapseTwo" class="panel-collapse collapse">
                 <div class="panel-body">
                    <div id="">
                        <table class="table">
                           <caption>财务部</caption>
        
                           <thead>
                              <tr>
                                 <th>产品</th>
                                 <th>付款日期</th>
                                 <th>状态</th>
                                 <th>进度</th>
                              </tr>
                           </thead>
                           <tbody>
                              <tr class="active">
                                 <td>产品1</td>
                                 <td>23/11/2013</td>
                                 <td>待发货</td>
                                 <td>
                                    
                                    <div class="progress">
                                       <div class="progress-bar progress-bar-info" role="progressbar" 
                                          aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                          style="width: 30%;">
                                          <span class="sr-only">30% 完成（信息）</span>
                                       </div>
                                    </div>
                             
                                 </td>
                              </tr>
                              <tr class="success">
                                 <td>产品2</td>
                                 <td>10/11/2013</td>
                                 <td>发货中</td>
                                 <td>
                                    <div class="progress">
                                       <div class="progress-bar progress-bar-info" role="progressbar" 
                                          aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                          style="width: 30%;">
                                          <span class="sr-only">30% 完成（信息）</span>
                                       </div>
                                    </div>
                                 </td>
                              </tr>
                              <tr  class="warning">
                                 <td>产品3</td>
                                 <td>20/10/2013</td>
                                 <td>待确认</td>
                                 <td>
                                    <div class="progress">
                                       <div class="progress-bar progress-bar-info" role="progressbar" 
                                          aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                          style="width: 30%;">
                                          <span class="sr-only">30% 完成（信息）</span>
                                       </div>
                                    </div>
                                 </td>
                              </tr>
                              <tr  class="danger">
                                 <td>产品4</td>
                                 <td>20/10/2013</td>
                                 <td>已退货</td>
                                 <td>
                                    <div class="progress">
                                       <div class="progress-bar progress-bar-info" role="progressbar" 
                                          aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                          style="width: 30%;">
                                          <span class="sr-only">30% 完成（信息）</span>
                                       </div>
                                    </div>
                                 </td>
                              </tr>
                           </tbody>
                        </table>
                    </div>
                 </div>
              </div>
           </div>
           <div class="panel panel-info">
              <div class="panel-heading">
                 <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" 
                       href="#collapseThree">
                       项目已完成
                    </a>
                 </h4>
              </div>
              <div id="collapseThree" class="panel-collapse collapse">
                 <div class="panel-body">
                    <div>
                        <table class="table">
                           <caption>财务部</caption>
        
                           <thead>
                              <tr>
                                 <th>产品</th>
                                 <th>付款日期</th>
                                 <th>状态</th>
                                 <th>进度</th>
                              </tr>
                           </thead>
                           <tbody>
                              <tr class="active">
                                 <td>产品1</td>
                                 <td>23/11/2013</td>
                                 <td>待发货</td>
                                 <td>
                                    
                                        <div class="progress">
                                           <div class="progress-bar progress-bar-success" role="progressbar" 
                                              aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                              style="width: 90%;">
                                              <span class="sr-only">90% 完成（成功）</span>
                                           </div>
                                        </div>
                             
                                 </td>
                              </tr>
                              <tr class="success">
                                 <td>产品2</td>
                                 <td>10/11/2013</td>
                                 <td>发货中</td>
                                 <td>
                                        <div class="progress">
                                           <div class="progress-bar progress-bar-success" role="progressbar" 
                                              aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                              style="width: 90%;">
                                              <span class="sr-only">90% 完成（成功）</span>
                                           </div>
                                        </div>
                                 </td>
                              </tr>
                              <tr  class="warning">
                                 <td>产品3</td>
                                 <td>20/10/2013</td>
                                 <td>待确认</td>
                                 <td>
                                        <div class="progress">
                                           <div class="progress-bar progress-bar-success" role="progressbar" 
                                              aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                              style="width: 90%;">
                                              <span class="sr-only">90% 完成（成功）</span>
                                           </div>
                                        </div>
                                 </td>
                              </tr>
                              <tr  class="danger">
                                 <td>产品4</td>
                                 <td>20/10/2013</td>
                                 <td>已退货</td>
                                 <td>
                                        <div class="progress">
                                           <div class="progress-bar progress-bar-success" role="progressbar" 
                                              aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                              style="width: 90%;">
                                              <span class="sr-only">90% 完成（成功）</span>
                                           </div>
                                        </div>
                                 </td>
                              </tr>
                           </tbody>
                        </table>
                    </div>
                 </div>
              </div>
           </div>
           
           <div class="panel panel-warning">
              <div class="panel-heading">
                 <h4 class="panel-title">
                    <a data-toggle="collapse" data-parent="#accordion" 
                       href="#collapseFour">
                        测试
                    </a>
                 </h4>
              </div>
              <div id="collapseFour" class="panel-collapse collapse">
                 <div class="panel-body">
                    <div id="">
                        <table class="table">
                           <caption>财务部</caption>
        
                           <thead>
                              <tr>
                                 <th>产品</th>
                                 <th>付款日期</th>
                                 <th>状态</th>
                                 <th>进度</th>
                              </tr>
                           </thead>
                           <tbody>
                              <tr class="active">
                                 <td>产品1</td>
                                 <td>23/11/2013</td>
                                 <td>待发货</td>
                                 <td>
                                    
                                        <div class="progress">
                                           <div class="progress-bar progress-bar-success" role="progressbar" 
                                              aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                              style="width: 90%;">
                                              <span class="sr-only">90% 完成（成功）</span>
                                           </div>
                                        </div>
                             
                                 </td>
                              </tr>
                              <tr class="success">
                                 <td>产品2</td>
                                 <td>10/11/2013</td>
                                 <td>发货中</td>
                                 <td>
                                    <div class="progress">
                                       <div class="progress-bar progress-bar-info" role="progressbar" 
                                          aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                          style="width: 30%;">
                                          <span class="sr-only">30% 完成（信息）</span>
                                       </div>
                                    </div>
                                 </td>
                              </tr>
                              <tr  class="warning">
                                 <td>产品3</td>
                                 <td>20/10/2013</td>
                                 <td>待确认</td>
                                 <td>
                                    <div class="progress">
                                       <div class="progress-bar progress-bar-warning" role="progressbar" 
                                          aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" 
                                          style="width: 20%;">
                                          <span class="sr-only">20% 完成（警告）</span>
                                       </div>
                                    </div>
                                 </td>
                              </tr>
                              <tr  class="danger">
                                 <td>产品4</td>
                                 <td>20/10/2013</td>
                                 <td>已退货</td>
                                 <td>
                                    <div class="progress">
                                       <div class="progress-bar progress-bar-danger" role="progressbar" 
                                          aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                                          style="width: 10%;">
                                          <span class="sr-only">10% 完成（危险）</span>
                                       </div>
                                    </div>
                                 </td>
                              </tr>
                           </tbody>
                        </table>
                    </div>
                 </div>
              </div>
           </div>
        </div>
   </div>
   
   <!--财务部-->
   <div class="tab-pane fade" id="cwb">
      <div>
        <table class="table">
           <caption>财务部</caption>
           <div>
            
            
            <!-- 表示一个成功的或积极的动作 -->
            <button type="button" class="btn btn-success">代发货</button>
            
            <!-- 信息警告消息的上下文按钮 -->
            <button type="button" class="btn btn-info">发货中</button>
            
            <!-- 表示应谨慎采取的动作 -->
            <button type="button" class="btn btn-warning">待确认</button>
            
            <!-- 表示一个危险的或潜在的负面动作 -->
            <button type="button" class="btn btn-danger">已退货</button>

           </div>
           <thead>
              <tr>
                 <th>产品</th>
                 <th>付款日期</th>
                 <th>状态</th>
                 <th>进度</th>
              </tr>
           </thead>
           <tbody>
              <tr class="active">
                 <td>产品1</td>
                 <td>23/11/2013</td>
                 <td>待发货</td>
                 <td>
                    
                        <div class="progress">
                           <div class="progress-bar progress-bar-success" role="progressbar" 
                              aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                              style="width: 90%;">
                              <span class="sr-only">90% 完成（成功）</span>
                           </div>
                        </div>
             
                 </td>
              </tr>
              <tr class="success">
                 <td>产品2</td>
                 <td>10/11/2013</td>
                 <td>发货中</td>
                 <td>
                    <div class="progress">
                       <div class="progress-bar progress-bar-info" role="progressbar" 
                          aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                          style="width: 30%;">
                          <span class="sr-only">30% 完成（信息）</span>
                       </div>
                    </div>
                 </td>
              </tr>
              <tr  class="warning">
                 <td>产品3</td>
                 <td>20/10/2013</td>
                 <td>待确认</td>
                 <td>
                    <div class="progress">
                       <div class="progress-bar progress-bar-warning" role="progressbar" 
                          aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" 
                          style="width: 20%;">
                          <span class="sr-only">20% 完成（警告）</span>
                       </div>
                    </div>
                 </td>
              </tr>
              <tr  class="danger">
                 <td>产品4</td>
                 <td>20/10/2013</td>
                 <td>已退货</td>
                 <td>
                    <div class="progress">
                       <div class="progress-bar progress-bar-danger" role="progressbar" 
                          aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"  
                          style="width: 10%;">
                          <span class="sr-only">10% 完成（危险）</span>
                       </div>
                    </div>
                 </td>
              </tr>
           </tbody>
        </table>
        
      </div>
   </div>
   
</div>

</div>



