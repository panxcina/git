<#-- order list -->
<div id="orderLookup" class="screenlet">
    <div class="screenlet-title-bar">
      <ul>
        <li class="h3">Lookup refunds	</li>
      </ul>
      <br class="clear"/>
    </div>
    <div class="screenlet-body">
      <form method="get" name="findorder" action="<@ofbizUrl>refundList</@ofbizUrl>">
        <table class="basic-table" cellspacing='0'>
          <tr>
            <td align="right" class="label">${uiLabelMap.CommonStatus}</td>
            <td nowrap="nowrap">
                <div>
                        <input type="radio" name="statusId" value="REFUND_PENDING" <#if parameters.statusId! == "REFUND_PENDING">checked </#if> /> PENDING
                        <input type="radio" name="statusId" value="REFUND_COMPLETED" <#if parameters.statusId! == "REFUND_COMPLETED">checked </#if> /> COMPLETED
                        <input type="radio" name="statusId" value="REFUND_REJECTED" <#if parameters.statusId! == "REFUND_REJECTED">checked </#if> /> REJECTED
            <br/><input type="submit"/></div></td>
          </tr>
        </table>
      </form>
    </div>
</div>


<div id="refundList" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
    <li class="h3">Refund List</li>
  </ul>
  <br class="clear"/>
</div>
<div class="screenlet-body">
    <table class="basic-table hover-bar" cellspacing='0'>
      <tr class="header-row">
        <td width="2%"><input type="checkbox" id="selectAllBtn"/></td>
        <td width="6%">refund ID</td>
        <td width="8%">order ID</td>
        <td width="8%">created date</td>
        <td width="6%">created by</td>
        <td width="10%">eBay User ID</td>
        <td width="10%">PayPal Email Address</td>
        <td width="10%">PayPal Trans ID</td>
        <td width="10%">refund PayPal Trans ID</td>
        <!--<td width="10%">Payment method type ID</td>-->
        <td width="6%">Is full refund?</td>
        <td width="6%">Amount</td>
        <td width="6%">Currency</td>
        <td width="25%">Reason</td>
        <td width="7%">Status</td>
        <td width="7%">Operation</td>
      </tr>
      <#assign alt_row = false>
      <#list refundList as refund>
      <form action="<@ofbizUrl>processOrderRefund</@ofbizUrl>" method="post" name="refund">
      	<tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
            <input type="hidden" value="${refund.refundId!}" name="refundId"/>
            <td><input type="checkbox" name="refundId" value="${refund.refundId}"/></td>
      		<td><a class="buttontext" href="/ordermgr/control/editOrderRefund?orderId=${refund.orderId?if_exists}&refundId=${refund.refundId}" target="refund">${refund.refundId}</A></td>
      		<td><a class="buttontext" href="/ordermgr/control/orderview?orderId=${refund.orderId?if_exists}" target="_blank">${refund.orderId!}</a></td>
            <td>${refund.createdStamp?date}</td>
            <td>${refund.createdByUserLogin?if_exists}</td>
            <td>
                <#assign orderContactMech = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("OrderContactMech", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", refund.orderId, "contactMechPurposeTypeId", "EBAY_USER_ID"), null, false))>
                <#assign eBayUserId = delegator.findOne("ContactMech", Static["org.ofbiz.base.util.UtilMisc"].toMap("contactMechId", orderContactMech.contactMechId), false)>
                ${eBayUserId.infoString}
            </td>
            <td>${refund.paypalEmailAddress?if_exists}</td>
      		<td>${refund.paypalTransactionId?if_exists}</td>
      		<td>${refund.refundPaypalTransId?if_exists}</td>
      		<!--<td>${refund.paymentMethodTypeId?if_exists}</td>-->
      		<td>${refund.fullRefund?if_exists}</td>
      		<td>${refund.amount?if_exists}</td>
      		<td>${refund.currencyUomId?if_exists}</td>
      		<td>${refund.reason?if_exists}</td>
      		<td>${refund.getRelatedOne("StatusItem").get("description",locale)?if_exists}</td>
            <td><input type="submit" value="Process" class="smallSubmit"/></td>
      		<!--<td><a class="buttontext" onclick="javascript:alert('Not implement yet! ${refund.refundId}')">Process</a></td>-->
        </tr>
        <#assign alt_row = !alt_row>
        </form>
      </#list>

          <tr>
            <td colspan="123"><a class="buttontext" onclick="javascript:void(0)" id="bulkProcessBtn">Bulk Process</a></td>
          </tr>
    </table>
    
  </div>
</div>

<script>

  $('#bulkProcessBtn').click(function() {
    var ids = [];
    $('input:checked[name=refundId]').each(function(i,e) {
      ids[ids.length] = e.value;
    });
    alert("Selected id is " + ids);
  });

  $('#selectAllBtn').click(function() {
    var result = this.checked ? true : null;
    $('input[name=refundId]').each(function(i, e) {
     e.checked=result;
    });
  });

  
</script>

 