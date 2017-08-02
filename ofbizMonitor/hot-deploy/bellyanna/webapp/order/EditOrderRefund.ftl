<#if orderHeader??>
	<form action="<#if refund.refundId??><@ofbizUrl>updateOrderRefund</@ofbizUrl><#else><@ofbizUrl>createOrderRefund</@ofbizUrl></#if>" method="post" name="refund" id="refund" onsubmit="return checkscript()">
            <table cellspacing="0" class="basic-table">
                    <#if refund??>
                    <input type="hidden" name="refundId" value="${refund.refundId!}">
                    <input type="hidden" name="createdByUserLogin" value="${refund.createdByUserLogin!}">
                    </#if>
                      <tbody>
                    <tr>
                      <td class="label">orderId</td>
                      <td>
                          <input type="text" size="20" maxlength="50" name="orderId" value="${refund.orderId}" readonly="readonly"/>
                      </td>
                    </tr>
                    <!-- paypal email address -->
                    <#assign productStore = orderHeader.getRelatedOne("ProductStore")>
                    <#assign paypalParty = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("ProductStoreRole", Static["org.ofbiz.base.util.UtilMisc"].toMap("productStoreId" , productStore.productStoreId, "roleTypeId", "PAYPAL_ACCOUNT"), null, false))>
                    <#assign paypalEmailAddress = delegator.findOne("PartyGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("partyId", paypalParty.partyId), false)>
                    <tr>
                      <td class="label">paypalEmailAddress</td>
                      <td>
                          <input type="text" size="50" name="paypalEmailAddress" value="${paypalEmailAddress.groupName?if_exists}"/>
                      </td>
                    </tr>
                    <tr>
                      <td class="label">paypalTransactionId</td>
                      <td>
                          <input type="text" size="50" maxlength="50" name="paypalTransactionId" value="${refund.paypalTransactionId!}" readonly="readonly"/>
                      </td>
                    </tr>
                    <tr>
                      <td class="label">refundPaypalTransId</td>
                      <td>
                          <input type="text" size="50" maxlength="50" name="refundPaypalTransId" value="${refund.refundPaypalTransId!""}"/>
                      </td>
                    </tr>
                    <tr>
                      <td class="label">paymentMethodTypeId</td>
                      <td>
                          <input type="text" size="20" maxlength="20" name="paymentMethodTypeId" value="${refund.paymentMethodTypeId!}"  readonly="readonly"/>
                      </td>
                    </tr>
                    <tr>
                      <td class="label">fullRefund</td>
                      <td>
                          <input type="radio" name="fullRefund" id ="doFullRefund" value="Y" <#if refund.fullRefund! == "Y" || !refund.fullRefund??>checked </#if> /> Y
                          <input type="radio" name="fullRefund" value="N" <#if refund.fullRefund! == "N">checked</#if>/> N
                      </td>
                    </tr>
                    <tr>
                      <td class="label">amount</td>
                      <td>
                          <input type="text" size="20" name="amount" value="<#if refund.amount??>${refund.amount}<#else>${orderHeader?if_exists.grandTotal?if_exists}</#if>">
                      </td>
                    </tr>
                    <tr>
                      <td class="label">currencyUomId</td>
                      <td>
                          <input type="text" size="10" maxlength="10" name="currencyUomId" value="${orderHeader?if_exists.currencyUom?if_exists}" readonly="readonly"/>
                      </td>
                    </tr>
                    <tr>
                      <td class="label">Status</td>
                      <td>
                        <select name="statusId">
                            <option value="REFUND_PENDING">Pending</option>
                            <option value="REFUND_REJECTED">Rejected</option>
                            <option value="REFUND_COMPLETED">Completed</option>
                        </select>
                      </td>
                    </tr>
                    <tr>
                      <td class="label">Reason</td>
                        <td>
                            <select id="reasonCode" name="reasonCode">
                                <#if refund.reasonCode??>
                                    <#assign reasonDesc = delegator.findOne("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumId", refund.reasonCode), false)>
                                    <option value="${refund.reasonCode!}">${reasonDesc.get("description")}</option>
                                <#else>
                                    <option value="">Choose one</option>
                                </#if>
                                <option value=""></option>
                                <#assign refundReasonEnumList = (delegator.findByAnd("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumTypeId", "REFUND_REASON_CODE"), sequenceId, false))?if_exists/>
                                <#list refundReasonEnumList as refundReasonEnum>
                                    <option value="${refundReasonEnum.get("enumId")}">${refundReasonEnum.get("description")}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                      <td class="label">Reason Description</td>
                      <td>
                          <textarea cols="60" rows="5" maxlength="5000" name="reason">${refund.reason!}</textarea>
                      </td>
                      </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td>
                      <input type="submit" value="<#if refund.refundId??>Update<#else>Create</#if>" class="smallSubmit"/>
                      <a href="javascript:void(0)" onclick="window.close();" class="smallSubmit">Cancel</a>
                    </td>
                  </tr>
            </tbody></table>
          </form>

          <!--<script>
          	var amountInput = $('form input[name=amount]');
          	var originalAmount = amountInput.attr('data-original-amount');
            var yes = $('form input[name=fullRefund][value=Y]');
            var no = $('form input[name=fullRefund][value=N]');
          	amountInput.blur(function(){
          		var value = $(this).val();
              if(parseInt(amountInput.attr('data-original-amount')) <= parseInt(value)) {
                amountInput.val(amountInput.attr('data-original-amount'));
                value = amountInput.attr('data-original-amount');
              }
          		if(value == originalAmount) {
          			yes.click();
          		}  else {
                no.click();
              }
          	});

            yes.click(function(){
              amountInput.val(amountInput.attr('data-original-amount'));
            });
            no.click(function(){
              if(amountInput.attr('data-original-amount') == amountInput.val()) {
                yes.click();
              }
            });
          </script>-->
          <script language="JavaScript" type="text/javascript">
          function checkscript() {
          var checkProcess = false;
          if(document.getElementById('reasonCode').value) {
                checkProcess = true;
            } else {
                alert('Please select a Reason for this refund');
                return false;
          }

          if(document.getElementById('doFullRefund').checked == true)
          { 

            if(confirm('Are you sure to process FULL refund?')) {
            return true;
            } else {
            checkProcess = false;
            }
          checkProcess = false;
          }
          return checkProcess;
          }
          </script>
          
          <#else>

          <h1>Order Not Found!</h1>

</#if>
