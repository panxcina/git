<#-- product search list -->
<div id="ResultList" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
    <li class="h3">Express Out Of Stock List (${expressLists.size()})</li><li class="collapsed"><a onclick="javascript:toggleCollapsiblePanel(this, 'express_result_body', 'Expand', 'Collapse');">&nbsp;</a></li>
  </ul>
  <br class="clear"/>
</div>
<div id="express_result_body" class="fieldgroup-body">
<div class="screenlet-body">
<#if expressLists?has_content>
    <table class="basic-table hover-bar" cellspacing='0' id="dataTable">
      <tr class="header-row">
        <td width="2%"><input type="checkbox" id="selectAll"/></td>
        <td width="5%">Order ID</td>
        <td width="5%">eBay User ID</td>
        <td width="5%">Status ID</td>
        <td width="5%">Shipping Method</td>
        <td width="5%">Order Date</td>
        <td width="8%">Ship To Country</td>
        <td width="10%">Approved SKU</td>
        <td width="6%">Ordered QTY</td>
        <td width="6%">Backorder</td>
        <td width="6%">Email Template</td>
        <td width="3%">OSA sent</td>
        <td width="6%">Actions</td>
      </tr>
      <#assign alt_row = false>
      <#list expressLists as expressList>
      <form action="<@ofbizUrl>sendToBeShippedEbayMessage</@ofbizUrl>" method="post" name="expressList">
        <input type=hidden name="orderId" value="${expressList.orderId}"/>
        <input type=hidden name="productStoreId" value="${expressList.productStoreId}"/>
      	<tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
            <td><input type="checkbox" name="isSelected"/></td>
            <td><a href="<@ofbizUrl>orderview?orderId=${expressList.orderId}</@ofbizUrl>" target="_blank" class="buttontext">${expressList.orderId}</a></td>
            <td>
                <#assign ebayContactMech = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("OrderContactMech", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId" , expressList.orderId, "contactMechPurposeTypeId", "EBAY_USER_ID"), null, false))>
                <#assign ebayUserId = delegator.findOne("ContactMech", Static["org.ofbiz.base.util.UtilMisc"].toMap("contactMechId", ebayContactMech.contactMechId), false)>
                ${ebayUserId.infoString}
                <input type=hidden name="ebayUserId" value="${ebayUserId.infoString}"/>
            </td>
            <td>
                <#assign currentStatus = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("StatusItem", Static["org.ofbiz.base.util.UtilMisc"].toMap("statusId" , expressList.statusId), null, false))>
                ${currentStatus.get("description",locale)}
            </td>
            <td>${expressList.shipmentMethodTypeId}</td>
            <td>
                <#if (Static["com.bellyanna.common.bellyannaService"].dayDifference(expressList.createdStamp) > 5)>
                    <font color=red><b>${expressList.createdStamp?date}</b></font>
                <#else>${expressList.createdStamp?date}
                </#if>
            </td>
            <td><#assign geo = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId" , expressList.countryGeoId), null, false))>
            ${geo.geoName} (${expressList.countryGeoId}) - ${geo.wellKnownText}
            </td>
            <td>
                <#assign orderItems = delegator.findByAnd("OrderItem", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", expressList.orderId), null, false)>
                <#list orderItems?sort_by("orderItemSeqId") as orderItem>
                    <#if orderItem.statusId.equals("ITEM_APPROVED")>
                        ${orderItem.orderItemSeqId.substring(3)} - ${orderItem.productId}<br />
                    </#if>
                </#list>
            </td>
            <td>
                <#list orderItems?sort_by("orderItemSeqId") as orderItem>
                    <#if orderItem.statusId.equals("ITEM_APPROVED")>
                        ${orderItem.quantity}<br />
                    </#if>
                </#list>
            </td>
            <td>
                <#list orderItems?sort_by("orderItemSeqId") as orderItem>
                    <#if orderItem.statusId.equals("ITEM_APPROVED")>
                        <#assign oisgirs = delegator.findByAnd("OrderItemShipGrpInvRes", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderItem.orderId, "orderItemSeqId", orderItem.orderItemSeqId), null, false)>
                        <#assign quantityNotAvailable = 0>
                        <#list oisgirs as oisgir>
                            <#if oisgir.quantityNotAvailable?has_content>
                                <#assign quantityNotAvailable = quantityNotAvailable + oisgir.quantityNotAvailable>
                            </#if>
                            <!--${oisgir.orderItemSeqId} - ${oisgir.inventoryItemId} - ${oisgir.quantity}<#if oisgir.quantityNotAvailable?has_content> - ${oisgir.quantityNotAvailable}</#if><br />-->
                        </#list>
                        <#if delegator.findOne("OrderItemAttribute", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderItem.orderId, "orderItemSeqId", orderItem.orderItemSeqId, "attrName", "eBay Item Number"), false)?has_content>
                            <#assign orderItemAttribute = delegator.findOne("OrderItemAttribute", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderItem.orderId, "orderItemSeqId", orderItem.orderItemSeqId, "attrName", "eBay Item Number"), false)>
                            <input type=hidden name="ebayItemId" value="${orderItemAttribute.attrValue}"/>
                        <#else><font color=red>ERROR! No ItemId!</font>
                        </#if>
                        <#if (quantityNotAvailable > 0)>
                            <font color=red>${orderItem.orderItemSeqId.substring(3)} - ${quantityNotAvailable} out of stock</font><br />
                        <#else><br />
                        </#if>
                    </#if>
                </#list>
            </td>
            <td>
            <div>
                <select id="emailTemplateName" name="emailTemplateName">
                    <#list emailTemplates as emailTemplate>
                        <option value="${emailTemplate.emailTemplateName}"
                        <#if defaultEmailTemplate?has_content>
                            <#if emailTemplate.emailTemplateId.equals(defaultEmailTemplate)> selected="selected"</#if>
                        <#else>
                            <#if emailTemplate.emailTemplateName.equals("OSA - General")> selected="selected"</#if>
                        </#if>>${emailTemplate.emailTemplateName}</option>
                    </#list>
                    <option value="custom">Custom</option>
                </select>
            </div>
            <td>
                <#if (expressList.attrValue?number > 0)>Sent - ${expressList.attrValue}</#if>
            </td>
            <td><input type="submit" value="Process" class="smallSubmit"/></td>
        </tr>
        <#assign alt_row = !alt_row>
        </form>
      </#list>
      <tr>
        <td colspan="123"><input type="submit" class="buttontext" value="Submit All" id="allBtn"/></td>
      </tr>
    </table>
</#if>
  </div>
  </div>










<br />









<div class="screenlet-title-bar">
  <ul>
    <li class="h3">Epacket Out Of Stock List (${epacketLists.size()})</li><li class="collapsed"><a onclick="javascript:toggleCollapsiblePanel(this, 'epacket_result_body', 'Expand', 'Collapse');">&nbsp;</a></li>
  </ul>
  <br class="clear"/>
</div>
<div id="epacket_result_body" class="fieldgroup-body">
<div class="screenlet-body">
<#if epacketLists?has_content>
    <table class="basic-table hover-bar" cellspacing='0'>
      <tr class="header-row">
        <td width="2%"><input type="checkbox" id="selectAllBtn"/></td>
        <td width="5%">Order ID</td>
        <td width="5%">eBay User ID</td>
        <td width="5%">Status ID</td>
        <td width="5%">Shipping Method</td>
        <td width="5%">Order Date</td>
        <td width="8%">Ship To Country</td>
        <td width="10%">Approved SKU</td>
        <td width="6%">Ordered QTY</td>
        <td width="6%">Backorder</td>
        <td width="6%">Email Template</td>
        <td width="3%">OSA sent</td>
        <td width="6%">Actions</td>
      </tr>
      <#assign alt_row = false>
      <#list epacketLists as epacketList>
      <form action="<@ofbizUrl>sendToBeShippedEbayMessage</@ofbizUrl>" method="post" name="epacketList">
        <input type=hidden name="orderId" value="${epacketList.orderId}"/>
        <input type=hidden name="productStoreId" value="${epacketList.productStoreId}"/>
      	<tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
            <td><input type="checkbox" name="orderId" value="${epacketList.orderId}"/></td>
            <td><a href="<@ofbizUrl>orderview?orderId=${epacketList.orderId}</@ofbizUrl>" target="_blank" class="buttontext">${epacketList.orderId}</a></td>
            <td>
                <#assign ebayContactMech = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("OrderContactMech", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId" , epacketList.orderId, "contactMechPurposeTypeId", "EBAY_USER_ID"), null, false))>
                <#assign ebayUserId = delegator.findOne("ContactMech", Static["org.ofbiz.base.util.UtilMisc"].toMap("contactMechId", ebayContactMech.contactMechId), false)>
                ${ebayUserId.infoString}
                <input type=hidden name="ebayUserId" value="${ebayUserId.infoString}"/>
            </td>
            <td>
                <#assign currentStatus = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("StatusItem", Static["org.ofbiz.base.util.UtilMisc"].toMap("statusId" , epacketList.statusId), null, false))>
                ${currentStatus.get("description",locale)}
            </td>
            <td>${epacketList.shipmentMethodTypeId}</td>
            <td>
                <#if (Static["com.bellyanna.common.bellyannaService"].dayDifference(epacketList.createdStamp) > 5)>
                    <font color=red><b>${epacketList.createdStamp?date}</b></font>
                <#else>${epacketList.createdStamp?date}
                </#if>
            </td>
            <td><#assign geo = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId" , epacketList.countryGeoId), null, false))>
            ${geo.geoName} (${epacketList.countryGeoId}) - ${geo.wellKnownText}
            </td>
            <td>
                <#assign orderItems = delegator.findByAnd("OrderItem", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", epacketList.orderId), null, false)>
                <#list orderItems?sort_by("orderItemSeqId") as orderItem>
                    <#if orderItem.statusId.equals("ITEM_APPROVED")>
                        ${orderItem.orderItemSeqId.substring(3)} - ${orderItem.productId}<br />
                    </#if>
                </#list>
            </td>
            <td>
                <#list orderItems?sort_by("orderItemSeqId") as orderItem>
                    <#if orderItem.statusId.equals("ITEM_APPROVED")>
                        ${orderItem.quantity}<br />
                    </#if>
                </#list>
            </td>
            <td>
                <#list orderItems?sort_by("orderItemSeqId") as orderItem>
                    <#if orderItem.statusId.equals("ITEM_APPROVED")>
                        <#assign oisgirs = delegator.findByAnd("OrderItemShipGrpInvRes", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderItem.orderId, "orderItemSeqId", orderItem.orderItemSeqId), null, false)>
                        <#assign quantityNotAvailable = 0>
                        <#list oisgirs as oisgir>
                            <#if oisgir.quantityNotAvailable?has_content>
                                <#assign quantityNotAvailable = quantityNotAvailable + oisgir.quantityNotAvailable>
                            </#if>
                            <!--${oisgir.orderItemSeqId} - ${oisgir.inventoryItemId} - ${oisgir.quantity}<#if oisgir.quantityNotAvailable?has_content> - ${oisgir.quantityNotAvailable}</#if><br />-->
                        </#list>
                        <#if delegator.findOne("OrderItemAttribute", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderItem.orderId, "orderItemSeqId", orderItem.orderItemSeqId, "attrName", "eBay Item Number"), false)?has_content>
                            <#assign orderItemAttribute = delegator.findOne("OrderItemAttribute", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderItem.orderId, "orderItemSeqId", orderItem.orderItemSeqId, "attrName", "eBay Item Number"), false)>
                            <input type=hidden name="ebayItemId" value="${orderItemAttribute.attrValue}"/>
                        <#else><font color=red>ERROR! No ItemId!</font>
                        </#if>
                        <#if (quantityNotAvailable > 0)>
                            <font color=red>${orderItem.orderItemSeqId.substring(3)} - ${quantityNotAvailable} out of stock</font><br />
                        <#else><br />
                        </#if>
                    </#if>
                </#list>
            </td>
            <td>
            <div>
                <select id="emailTemplateName" name="emailTemplateName">
                    <#list emailTemplates as emailTemplate>
                        <option value="${emailTemplate.emailTemplateName}"
                        <#if defaultEmailTemplate?has_content>
                            <#if emailTemplate.emailTemplateId.equals(defaultEmailTemplate)> selected="selected"</#if>
                        <#else>
                            <#if emailTemplate.emailTemplateName.equals("OSA - General")> selected="selected"</#if>
                        </#if>>${emailTemplate.emailTemplateName}</option>
                    </#list>
                    <option value="custom">Custom</option>
                </select>
            </div>
            <td>
                <#if (epacketList.attrValue?number > 0)>Sent - ${epacketList.attrValue}</#if>
            </td>
            <td><input type="submit" value="Process" class="smallSubmit"/></td>
        </tr>
        <#assign alt_row = !alt_row>
        </form>
      </#list>
      <tr>
        <td colspan="123"><a class="buttontext" onclick="javascript:void(0)" id="bulkProcessBtn">Bulk Process</a></td>
      </tr>
    </table>
</#if>
  </div>
  </div>
  
  
  
  

<br />





<div class="screenlet-title-bar">
  <ul>
    <li class="h3">Standard Out Of Stock List (${standardLists.size()})</li><li class="collapsed"><a onclick="javascript:toggleCollapsiblePanel(this, 'standard_result_body', 'Expand', 'Collapse');">&nbsp;</a></li>
  </ul>
  <br class="clear"/>
</div>
<div id="standard_result_body" class="fieldgroup-body">
<div class="screenlet-body">
<#if standardLists?has_content>
    <table class="basic-table hover-bar" cellspacing='0'>
      <tr class="header-row">
        <td width="2%"><input type="checkbox" id="selectAllBtn"/></td>
        <td width="5%">Order ID</td>
        <td width="5%">eBay User ID</td>
        <td width="5%">Status ID</td>
        <td width="5%">Shipping Method</td>
        <td width="5%">Order Date</td>
        <td width="8%">Ship To Country</td>
        <td width="10%">Approved SKU</td>
        <td width="6%">Ordered QTY</td>
        <td width="6%">Backorder</td>
        <td width="6%">Email Template</td>
        <td width="3%">OSA sent</td>
        <td width="6%">Actions</td>
      </tr>
      <#assign alt_row = false>
      <#list standardLists as standardList>
      <form action="<@ofbizUrl>sendToBeShippedEbayMessage</@ofbizUrl>" method="post" name="standardList">
        <input type=hidden name="orderId" value="${standardList.orderId}"/>
        <input type=hidden name="productStoreId" value="${standardList.productStoreId}"/>
      	<tr valign="middle"<#if alt_row> class="alternate-row"</#if>>
            <td><input type="checkbox" name="orderId" value="${standardList.orderId}"/></td>
            <td><a href="<@ofbizUrl>orderview?orderId=${standardList.orderId}</@ofbizUrl>" target="_blank" class="buttontext">${standardList.orderId}</a></td>
            <td>
                <#assign ebayContactMech = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("OrderContactMech", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId" , standardList.orderId, "contactMechPurposeTypeId", "EBAY_USER_ID"), null, false))>
                <#assign ebayUserId = delegator.findOne("ContactMech", Static["org.ofbiz.base.util.UtilMisc"].toMap("contactMechId", ebayContactMech.contactMechId), false)>
                ${ebayUserId.infoString}
                <input type=hidden name="ebayUserId" value="${ebayUserId.infoString}"/>
            </td>
            <td>
                <#assign currentStatus = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("StatusItem", Static["org.ofbiz.base.util.UtilMisc"].toMap("statusId" , standardList.statusId), null, false))>
                ${currentStatus.get("description",locale)}
            </td>
            <td>${standardList.shipmentMethodTypeId}</td>
            <td>
                <#if (Static["com.bellyanna.common.bellyannaService"].dayDifference(standardList.createdStamp) > 5)>
                    <font color=red><b>${standardList.createdStamp?date}</b></font>
                <#else>${standardList.createdStamp?date}
                </#if>
            </td>
            <td><#assign geo = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId" , standardList.countryGeoId), null, false))>
            ${geo.geoName} (${standardList.countryGeoId}) - ${geo.wellKnownText}
            </td>
            <td>
                <#assign orderItems = delegator.findByAnd("OrderItem", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", standardList.orderId), null, false)>
                <#list orderItems?sort_by("orderItemSeqId") as orderItem>
                    <#if orderItem.statusId.equals("ITEM_APPROVED")>
                        ${orderItem.orderItemSeqId.substring(3)} - ${orderItem.productId}<br />
                    </#if>
                </#list>
            </td>
            <td>
                <#list orderItems?sort_by("orderItemSeqId") as orderItem>
                    <#if orderItem.statusId.equals("ITEM_APPROVED")>
                        ${orderItem.quantity}<br />
                    </#if>
                </#list>
            </td>
            <td>
                <#list orderItems?sort_by("orderItemSeqId") as orderItem>
                    <#if orderItem.statusId.equals("ITEM_APPROVED")>
                        <#assign oisgirs = delegator.findByAnd("OrderItemShipGrpInvRes", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderItem.orderId, "orderItemSeqId", orderItem.orderItemSeqId), null, false)>
                        <#assign quantityNotAvailable = 0>
                        <#list oisgirs as oisgir>
                            <#if oisgir.quantityNotAvailable?has_content>
                                <#assign quantityNotAvailable = quantityNotAvailable + oisgir.quantityNotAvailable>
                            </#if>
                            <!--${oisgir.orderItemSeqId} - ${oisgir.inventoryItemId} - ${oisgir.quantity}<#if oisgir.quantityNotAvailable?has_content> - ${oisgir.quantityNotAvailable}</#if><br />-->
                        </#list>
                        <#if delegator.findOne("OrderItemAttribute", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderItem.orderId, "orderItemSeqId", orderItem.orderItemSeqId, "attrName", "eBay Item Number"), false)?has_content>
                            <#assign orderItemAttribute = delegator.findOne("OrderItemAttribute", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderItem.orderId, "orderItemSeqId", orderItem.orderItemSeqId, "attrName", "eBay Item Number"), false)>
                            <input type=hidden name="ebayItemId" value="${orderItemAttribute.attrValue}"/>
                        <#else><font color=red>ERROR! No ItemId!</font>
                        </#if>
                        <#if (quantityNotAvailable > 0)>
                            <font color=red>${orderItem.orderItemSeqId.substring(3)} - ${quantityNotAvailable} out of stock</font><br />
                        <#else><br />
                        </#if>
                    </#if>
                </#list>
            </td>
            <td>
            <div>
                <select id="emailTemplateName" name="emailTemplateName">
                    <#list emailTemplates as emailTemplate>
                        <option value="${emailTemplate.emailTemplateName}"
                        <#if defaultEmailTemplate?has_content>
                            <#if emailTemplate.emailTemplateId.equals(defaultEmailTemplate)> selected="selected"</#if>
                        <#else>
                            <#if emailTemplate.emailTemplateName.equals("OSA - General")> selected="selected"</#if>
                        </#if>>${emailTemplate.emailTemplateName}</option>
                    </#list>
                    <option value="custom">Custom</option>
                </select>
            </div>
            <td>
                <#if (standardList.attrValue?number > 0)>Sent - ${standardList.attrValue}</#if>
            </td>
            <td><input type="submit" value="Process" class="smallSubmit"/></td>
        </tr>
        <#assign alt_row = !alt_row>
        </form>
      </#list>
      <tr>
        <td colspan="123"><a class="buttontext" onclick="javascript:void(0)" id="bulkProcessBtn">Bulk Process</a></td>
      </tr>
    </table>
</#if>
  </div>
  </div>









</div>

<script>
		var mainForm = null;
		$(function() {
			$('#selectAll').change(function() {
				if (this.checked) { // select all
					$('input[name=isSelected]').each(function() {this.checked=true});
				} else { // unselect all
					$('input[name=isSelected]').removeAttr('checked');
				}	
			});

			
			$('#allBtn').click(function() {
				// check if there are no rows selected
				if($('input:checked[name=isSelected]').length == 0) {
					alert('No selected rows found!');
					return false; // prevent from submitting the form
				}

				// create a new form to wrap all the data
				var mainForm = $('<form action="<@ofbizUrl>sendToBeShippedEbayMessage</@ofbizUrl>" method="POST"/>');
				// delete original forms since forms cannot be wrapped within a form
				$('#dataTable form').remove();
				// move the table into the newly created form
				$('#dataTable').wrap(mainForm);
				// remove unchecked rows to not submit them
				$('input:not(:checked)[name=isSelected]').closest('tr').each(function(i,e) {
					$(e).remove();
				})
				// go go go submitting!
				return true;
			});
		});
	</script>
