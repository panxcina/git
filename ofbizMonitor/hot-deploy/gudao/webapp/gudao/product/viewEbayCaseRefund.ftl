<#-- task View -->

<style type="text/css">
A.bigbutton,.bigbuttondisabled {
border-bottom: #999999 solid 0.1em;
border-top: #f7e893 solid 0.1em;
border-left: #f7e893 solid 0.1em;
border-right: #999999 solid 0.1em;
background-image: url(../images/button_whitegray.jpg);
background-repeat:repeat-x;
font-weight: bold;
font-size: 20px;
line-height: 2.9em;
margin-left:  0.0em;
padding: 0.4em 0.6em 0.4em 0.6em;
/*white-space: nowrap;*/
}

</style>





<div id="taskView" class="screenlet">
<div class="screenlet-title-bar">
<ul>
<li class="h3">eBay Case Refund</b> - ${count}</li>
</ul>
<br class="clear"/>
</div>
<!--<div class="screenlet-body" align="center">

<form action="<@ofbizUrl>viewEbayCaseRefund</@ofbizUrl>" method="post" name="viewEbayCaseRefund" id="viewEbayCaseRefund">
<select name="statusId">
<option value="ALL">All</option>
</select>
<input type="submit" value="Search" class="smallSubmit"/>
</form>
</div>-->

<div class="screenlet-body" align="center">
<table class="basic-table hover-bar" cellspacing='10'>
<tr class="header-row" style="white-space: nowrap;">
<td>Case ID</td>
<td>Account</td>
<td>Type</td>
<td>Status</td>
<td>eBay User ID</td>
<td>Item Id</td>
<td>Amount</td>
<td>Case Creation Date</td>
<td>Refund Status</td>
<td>Refund Date</td>
<td>Refund Notes</td>
</tr>

<#list ebayCaseRefundList as ebayCaseRefund>
<tr>
<td>${ebayCaseRefund.caseId}</td>
<td>${ebayCaseRefund.productStoreId}</td>
<td>${ebayCaseRefund.caseType}</td>
<td>${ebayCaseRefund.caseStatus}</td>
<td>${ebayCaseRefund.ebayUserId}</td>
<td>${ebayCaseRefund.itemId}</td>
<td>${ebayCaseRefund.caseCurrency} ${ebayCaseRefund.caseAmount}</td>
<td>${ebayCaseRefund.creationDate}</td>
<td>${ebayCaseRefund.refundStatus}</td>
<td><#if ebayCaseRefund.refundDate?has_content>${ebayCaseRefund.refundDate}</#if></td>
<td><#if ebayCaseRefund.notes?has_content>${ebayCaseRefund.notes}</#if></td>
</tr>

</#list>
</table>

</div>
</div>