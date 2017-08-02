<#--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

<script language="JavaScript" type="text/javascript">
    function clearLine(facilityId, orderId, orderItemSeqId, productId, shipGroupSeqId, inventoryItemId, packageSeqId) {
        document.clearPackLineForm.facilityId.value = facilityId;
        document.clearPackLineForm.orderId.value = orderId;
        document.clearPackLineForm.orderItemSeqId.value = orderItemSeqId;
        document.clearPackLineForm.productId.value = productId;
        document.clearPackLineForm.shipGroupSeqId.value = shipGroupSeqId;
        document.clearPackLineForm.inventoryItemId.value = inventoryItemId;
        document.clearPackLineForm.packageSeqId.value = packageSeqId;
        document.clearPackLineForm.submit();
    }
</script>

<#if security.hasEntityPermission("FACILITY", "_VIEW", session)>
    <#assign showInput = requestParameters.showInput?default("Y")>
    <#assign hideGrid = requestParameters.hideGrid?default("N")>

    <#if (requestParameters.forceComplete?has_content && !invoiceIds?has_content)>
        <#assign forceComplete = "true">
        <#assign showInput = "Y">
    </#if>

    <div class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3">${uiLabelMap.ProductPackOrder}&nbsp;in&nbsp;${facility.facilityName?if_exists} [${facilityId?if_exists}]</li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body">
            <#if shipmentId?has_content>
                <#assign orderIdPacked = delegator.findOne("Shipment",Static["org.ofbiz.base.util.UtilMisc"].toMap("shipmentId",shipmentId), false)>
                <div>
                ${uiLabelMap.CommonView} <a href="<@ofbizUrl>/PackingSlip.pdf?shipmentId=${shipmentId}</@ofbizUrl>" target="_blank" class="buttontext">${uiLabelMap.ProductPackingSlip}</a> ${uiLabelMap.CommonOr}
                ${uiLabelMap.CommonView} <a href="<@ofbizUrl>/ShipmentBarCode.pdf?shipmentId=${shipmentId}</@ofbizUrl>" target="_blank" class="buttontext">${uiLabelMap.ProductBarcode}</a> ${uiLabelMap.CommonFor} ${uiLabelMap.ProductShipmentId} <a href="<@ofbizUrl>/ViewShipment?shipmentId=${shipmentId}</@ofbizUrl>" class="buttontext">${shipmentId}</a>
                </div>
                <#if shipmentId?exists && shipmentId?has_content>
                <div>
                    <p>${uiLabelMap.AccountingInvoices}:</p>
                    <ul>
                    <#--<#list invoiceIds as invoiceId>
                      <li>
                        ${uiLabelMap.CommonNbr}<a href="/accounting/control/invoiceOverview?invoiceId=${invoiceId}${externalKeyParam}" target="_blank" class="buttontext">${invoiceId}</a>
                        (<a href="/accounting/control/invoice.pdf?invoiceId=${invoiceId}${externalKeyParam}" target="_blank" class="buttontext">PDF</a>)
                      </li>-->
                      <#-- Bellyanna - START -->
                      <#assign trackingNumber = delegator.findOne("ShipmentPackageRouteSeg",Static["org.ofbiz.base.util.UtilMisc"].toMap("shipmentId",shipmentId, "shipmentPackageSeqId", "00001", "shipmentRouteSegmentId", "00001"), false)>
                      <#--<#if !trackingNumber?has_content>
                        <#assign trackingNumber = delegator.findOne("ShipmentPackageRouteSeg",Static["org.ofbiz.base.util.UtilMisc"].toMap("shipmentId",shipmentId, "shipmentPackageSeqId", "BA00001", "shipmentRouteSegmentId", "BA00001"), false)>
                      </#if>-->
                      <#assign shipment = delegator.findOne("Shipment", Static["org.ofbiz.base.util.UtilMisc"].toMap("shipmentId", shipmentId), false)>
                      <#assign orderItemShipGroup = shipment.getRelatedOne("PrimaryOrderItemShipGroup", false)>
                      <#if orderItemShipGroup.shipmentMethodTypeId == "EPACKET">
                      <li>
                        <script language="JavaScript" type="text/javascript">
                          window.open('<@ofbizUrl>printAPACShippingLabel?trackingNumber=${trackingNumber.trackingCode?if_exists}</@ofbizUrl>');
                        </script>
                        <a href="<@ofbizUrl>printAPACShippingLabel?trackingNumber=${trackingNumber.trackingCode?if_exists}</@ofbizUrl>" class="buttontext">${uiLabelMap.Print} EUB ${uiLabelMap.ShippingLabel}</a>
                      </li>
                      <#elseif (orderItemShipGroup.shipmentMethodTypeId == "REGISTERED" || orderItemShipGroup.shipmentMethodTypeId == "STANDARD")>
                        <script language="JavaScript" type="text/javascript">
                          <#--window.open('<@ofbizUrl>CN23OrderCustomsDeclarationFormNoTrack.pdf?orderId=${orderIdPacked.primaryOrderId}&shipmentId=${shipmentId}</@ofbizUrl>');-->
                          window.open('<@ofbizUrl>orderShippingLabel.pdf?orderId=${orderIdPacked.primaryOrderId}</@ofbizUrl>');
                        </script>
                      </#if>
                      <li>
                        <a href="<@ofbizUrl>CN23OrderCustomsDeclarationForm.pdf?orderId=${orderIdPacked.primaryOrderId}&shipmentId=${shipmentId}</@ofbizUrl>" target="_blank" class="buttontext">${uiLabelMap.Print} CN23 form</a>
                      </li>
                      <li>
                        <a href="<@ofbizUrl>CN23OrderCustomsDeclarationFormNoTrack.pdf?orderId=${orderIdPacked.primaryOrderId}&shipmentId=${shipmentId}</@ofbizUrl>" target="_blank" class="buttontext">${uiLabelMap.Print} CN23 form (without Tracking Number)</a>
                      </li>
                      <li>
                        <a href="<@ofbizUrl>orderShippingLabel.pdf?orderId=${orderIdPacked.primaryOrderId}</@ofbizUrl>" target="_blank" class="buttontext">${uiLabelMap.Print} ${uiLabelMap.ShippingLabel}</a>
                      </li>
                      <li>
                        <a href="<@ofbizUrl>EMSorderShippingLabel.pdf?orderId=${orderIdPacked.primaryOrderId}</@ofbizUrl>" target="_blank" class="buttontext">${uiLabelMap.Print} EMS ${uiLabelMap.ShippingLabel}</a>
                      </li>
                      <#-- Bellyanna - END -->
                    <#--</#list>-->
                    </ul>
                </div>
                </#if>
            </#if>
            <br />

            <!-- select order form -->
            <form name="selectOrderForm" method="post" action="<@ofbizUrl>PackOrder</@ofbizUrl>">
              <input type="hidden" name="facilityId" value="${facilityId?if_exists}" />
              <table cellspacing="0" class="basic-table">
                <tr>
                  <td width="25%" align="right"><span class="label">${uiLabelMap.ProductOrderId}</span></td>
                  <td width="1">&nbsp;</td>
                  <td width="25%">
                    <input type="text" name="orderId" size="20" maxlength="20" value="${orderId?if_exists}"/>
                    /
                    <input type="text" name="shipGroupSeqId" size="6" maxlength="6" value="${shipGroupSeqId?default("00001")}"/>
                  </td>
                  <td><span class="label">${uiLabelMap.ProductHideGrid}</span>&nbsp;<input type="checkbox" name="hideGrid" value="Y" <#if (hideGrid == "Y")>checked=""</#if> /></td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="2">&nbsp;</td>
                  <td colspan="2">
                    <input type="image" src="<@ofbizContentUrl>/images/spacer.gif</@ofbizContentUrl>" onclick="javascript:document.selectOrderForm.submit();" />
                    <a href="javascript:document.selectOrderForm.submit();" class="buttontext">${uiLabelMap.ProductPackOrder}</a>
                    <a href="javascript:document.selectOrderForm.action='<@ofbizUrl>WeightPackageOnly</@ofbizUrl>';document.selectOrderForm.submit();" class="buttontext">${uiLabelMap.ProductWeighPackageOnly}</a>
                  </td>
                </tr>
              </table>
            </form>

            <!-- select picklist bin form -->
            <!--<form name="selectPicklistBinForm" method="post" action="<@ofbizUrl>PackOrder</@ofbizUrl>" style="margin: 0;">
              <input type="hidden" name="facilityId" value="${facilityId?if_exists}" />
              <table cellspacing="0" class="basic-table">
                <tr>
                  <td width="25%" align='right'><span class="label">${uiLabelMap.FormFieldTitle_picklistBinId}</span></td>
                  <td width="1">&nbsp;</td>
                  <td width="25%">
                    <input type="text" name="picklistBinId" size="29" maxlength="60" value="${picklistBinId?if_exists}"/>
                  </td>
                  <td><span class="label">${uiLabelMap.ProductHideGrid}</span>&nbsp;<input type="checkbox" name="hideGrid" value="Y" <#if (hideGrid == "Y")>checked=""</#if> /></td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="2">&nbsp;</td>
                  <td colspan="1">
                    <input type="image" src="<@ofbizContentUrl>/images/spacer.gif</@ofbizContentUrl>" onclick="javascript:document.selectPicklistBinForm.submit();" />
                    <a href="javascript:document.selectPicklistBinForm.submit();" class="buttontext">${uiLabelMap.ProductPackOrder}</a>
                    <a href="javascript:document.selectPicklistBinForm.action='<@ofbizUrl>WeightPackageOnly</@ofbizUrl>';document.selectPicklistBinForm.submit();" class="buttontext">${uiLabelMap.ProductWeighPackageOnly}</a>
                  </td>
                </tr>
              </table>
            </form>-->
            <form name="clearPackForm" method="post" action="<@ofbizUrl>ClearPackAll</@ofbizUrl>">
              <input type="hidden" name="orderId" value="${orderId?if_exists}"/>
              <input type="hidden" name="shipGroupSeqId" value="${shipGroupSeqId?if_exists}"/>
              <input type="hidden" name="facilityId" value="${facilityId?if_exists}"/>
            </form>
            <form name="incPkgSeq" method="post" action="<@ofbizUrl>SetNextPackageSeq</@ofbizUrl>">
              <input type="hidden" name="orderId" value="${orderId?if_exists}"/>
              <input type="hidden" name="shipGroupSeqId" value="${shipGroupSeqId?if_exists}"/>
              <input type="hidden" name="facilityId" value="${facilityId?if_exists}"/>
            </form>
            <form name="clearPackLineForm" method="post" action="<@ofbizUrl>ClearPackLine</@ofbizUrl>">
                <input type="hidden" name="facilityId"/>
                <input type="hidden" name="orderId"/>
                <input type="hidden" name="orderItemSeqId"/>
                <input type="hidden" name="productId"/>
                <input type="hidden" name="shipGroupSeqId"/>
                <input type="hidden" name="inventoryItemId"/>
                <input type="hidden" name="packageSeqId"/>
            </form>
        </div>
    </div>

    <#-- Bellyanna - START -->
    <#if !(invoiceIds?has_content)>
      <#if orderHeader?has_content>
        <div class="screenlet">
    	  <div class="screenlet-title-bar">
    	    <ul>
    		  <li class="h3">&nbsp;${uiLabelMap.OrderNotes}</li>
              <#if security.hasEntityPermission("ORDERMGR", "_NOTE", session)>
                <li><a href="/ordermgr/control/createnewnote?orderId=${orderId}">${uiLabelMap.OrderNotesCreateNew}</a></li>
              </#if>
    	    </ul>
    	    <br class="clear"/>
          </div>
    	  <div class="screenlet-body">
            <table class="basic-table" cellspacing='0'>
              <tr>
                <td>
                  <#if orderNotes?has_content>
                    <table class="basic-table" cellspacing='0'>
                      <#list orderNotes as note>
                        <tr>
                          <td valign="top" width="35%">
                            <#if note.noteParty?has_content>
                              <div>&nbsp;<span class="label">${uiLabelMap.CommonBy}</span>&nbsp;${Static["org.ofbiz.party.party.PartyHelper"].getPartyName(delegator, note.noteParty, true)}</div>
                            </#if>
                            <div>&nbsp;<span class="label">${uiLabelMap.CommonAt}</span>&nbsp;${note.noteDateTime?string?if_exists}</div>
                          </td>
                          <td valign="top" width="50%">
                            <font color='red'><B>${note.noteInfo?replace("\n", "<br/>")}</B></font>
                          </td>
                          <td align="right" valign="top" width="15%">
                        	<#if note.internalNote?if_exists == "N">
                              ${uiLabelMap.OrderPrintableNote}
                              <form name="privateNotesForm_${note_index}" method="post" action="/ordermgr/control/updateOrderNote">
                                <input type="hidden" name="orderId" value="${orderId}"/>
                                <input type="hidden" name="noteId" value="${note.noteId}"/>
                                <input type="hidden" name="internalNote" value="Y"/>
                                <a href="javascript:document.privateNotesForm_${note_index}.submit()" class="buttontext">${uiLabelMap.OrderNotesPrivate}</a>
                              </form>
                            </#if>
                            <#if note.internalNote?if_exists == "Y">
                              ${uiLabelMap.OrderNotPrintableNote}
                              <form name="publicNotesForm_${note_index}" method="post" action="/ordermgr/control/updateOrderNote">
                                <input type="hidden" name="orderId" value="${orderId}"/>
                                <input type="hidden" name="noteId" value="${note.noteId}"/>
                                <input type="hidden" name="internalNote" value="N"/>
                                <a href="javascript:document.publicNotesForm_${note_index}.submit()" class="buttontext">${uiLabelMap.OrderNotesPublic}</a>
                              </form>
                            </#if>
                          </td>
                        </tr>
                        <#if note_has_next>
                          <tr><td colspan="3"><hr/></td></tr>
                        </#if>
                      </#list>
                    </table>
                  <#else>
                    <span class="label">&nbsp;${uiLabelMap.OrderNoNotes}.</span>
                  </#if>
             	</td>
              </tr>
            </table>
          </div>
        </div>
      </#if>
    </#if>
    <#-- Bellyanna - END -->

    <#if showInput != "N" && ((orderHeader?exists && orderHeader?has_content))>
    <div class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3">${uiLabelMap.ProductOrderId} <a href="/ordermgr/control/orderview?orderId=${orderId}">${orderId}</a> / ${uiLabelMap.ProductOrderShipGroupId} #${shipGroupSeqId}</li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body">
              <#if orderItemShipGroup?has_content>
                <#assign postalAddress = orderItemShipGroup.getRelatedOne("PostalAddress", false)>
                <#-- Bellyanna - START -->
                <#assign country = delegator.findOne("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId", postalAddress.countryGeoId), false)>
                <#assign province = delegator.findOne("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId", postalAddress.stateProvinceGeoId), false)>
                <#assign carrier = orderItemShipGroup.carrierPartyId?default("N/A")>
                <#assign phoneNumberTemp = delegator.findByAnd("OrderContactMech", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderId, "contactMechPurposeTypeId", "PRIMARY_PHONE"), null, false)>
                <#if phoneNumberTemp?has_content><#assign phoneNumberContactMech = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(phoneNumberTemp)>
                <#assign phoneNumber = delegator.findOne("TelecomNumber", Static["org.ofbiz.base.util.UtilMisc"].toMap("contactMechId", phoneNumberContactMech.contactMechId), false)></#if>
                <#assign orderStatusCreated = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(delegator.findByAnd("OrderStatus", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId",orderHeader.orderId, "statusId","ORDER_CREATED"), null, false))>
                <#-- Bellyanna - END -->
                <table cellpadding="4" cellspacing="4" class="basic-table">
                  <tr>
                    <td valign="top">
                      <span class="label">${uiLabelMap.ProductShipToAddress}</span>
                      <br />
                      ${uiLabelMap.CommonTo}: ${postalAddress.toName?default("")}
                      <br />
                      <#if postalAddress.attnName?has_content>
                          ${uiLabelMap.CommonAttn}: ${postalAddress.attnName}
                          <br />
                      </#if>
                      ${postalAddress.address1}
                      &nbsp;&nbsp;
                      <#if postalAddress.address2?has_content>
                          ${postalAddress.address2}
                          <br />
                      </#if>
                      ${postalAddress.postalCode?if_exists}<br />${postalAddress.city?if_exists}, ${province.geoName} (${postalAddress.stateProvinceGeoId?if_exists})
                      <br />
                      ${country.geoName} - ${postalAddress.countryGeoId}<#if country.wellKnownText?has_content> (${country.wellKnownText})</#if>
                      <br />
                      <#if phoneNumber?has_content>
                        Phone: <#if phoneNumber.countryCode?has_content>${phoneNumber.countryCode}</#if> <#if phoneNumber.areaCode?has_content>${phoneNumber.areaCode}</#if> ${phoneNumber.contactNumber}
                      </#if>
                    </td>
                    <td>&nbsp;</td>
                    <td valign="top">
                      <span class="label">${uiLabelMap.ProductCarrierShipmentMethod}</span>
                      <br />
                      <#if carrier == "USPS">
                        <#assign color = "red">
                      <#elseif carrier == "UPS">
                        <#assign color = "green">
                      <#else>
                        <#assign color = "black">
                      </#if>
                      <#if carrier != "_NA_">
                        <font color="${color}">&nbsp;&nbsp;${carrier}</font>
                      </#if>
                      ${orderItemShipGroup.shipmentMethodTypeId?default("??")}
                      <br />
                      <#-- Bellyanna - START -->
                      <#if orderId?has_content>
                        <#list shipGroups as shipGroup>
                          <#assign shipmentMethodType = shipGroup.getRelatedOne("ShipmentMethodType", false)?if_exists>
                          <form name="updateOrderItemShipGroup" method="post" action="<@ofbizUrl>updateOrderItemShipGroup</@ofbizUrl>">
                            <input type="hidden" name="orderId" value="${orderId?if_exists}"/>
                            <input type="hidden" name="shipGroupSeqId" value="${shipGroup.shipGroupSeqId?if_exists}"/>
                            <input type="hidden" name="contactMechPurposeTypeId" value="SHIPPING_LOCATION"/>
                            <input type="hidden" name="oldContactMechId" value="${shipGroup.contactMechId?if_exists}"/>
                            <input type="hidden" name="facilityId" value="${facilityId?if_exists}"/>
                            <#if orderHeader?has_content && orderHeader.statusId != "ORDER_CANCELLED" && orderHeader.statusId != "ORDER_COMPLETED" && orderHeader.statusId != "ORDER_REJECTED">
                              <#-- passing the shipmentMethod value as the combination of two fields value
                                i.e shipmentMethodTypeId & carrierPartyId and this two field values are separated bye
                                "@" symbol.
                                -->
                              <select name="shipmentMethod">
                                <#if shipGroup.shipmentMethodTypeId?has_content>
                                  <option value="${shipGroup.shipmentMethodTypeId}@${shipGroup.carrierPartyId?if_exists}"><#if shipGroup.carrierPartyId != "_NA_">${shipGroup.carrierPartyId?if_exists}</#if>&nbsp;${shipmentMethodType.get("description",locale)?default("")}</option>
                                <#else>
                                  <option value=""/>
                                </#if>
                                <#list productStoreShipmentMethList as productStoreShipmentMethod>
                                  <#assign shipmentMethodTypeAndParty = productStoreShipmentMethod.shipmentMethodTypeId + "@" + productStoreShipmentMethod.partyId + "@" + productStoreShipmentMethod.roleTypeId>
                                  <#if productStoreShipmentMethod.partyId?has_content || productStoreShipmentMethod?has_content>
                                    <option value="${shipmentMethodTypeAndParty?if_exists}"><#if productStoreShipmentMethod.partyId != "_NA_">${productStoreShipmentMethod.partyId?if_exists}</#if>&nbsp;${productStoreShipmentMethod.get("description",locale)?default("")}</option>
                                  </#if>
                                </#list>
                              </select>
                            <#else>
                              <#if shipGroup.carrierPartyId != "_NA_">
                                ${shipGroup.carrierPartyId?if_exists}
                              </#if>
                              ${shipmentMethodType?if_exists.get("description",locale)?default("")}
                            </#if>
			                <input type="submit" value="${uiLabelMap.CommonUpdate}" class="smallSubmit"/>
                          </form>
                        </#list>
                      </#if>
                      </td><td>
                      <#if orderHeader?has_content>
                        <#if (Static["com.bellyanna.common.bellyannaService"].dayDifference(orderStatusCreated.statusDatetime) > 5)><font color="red"><span class="label">${uiLabelMap.CommonPaidDate}</span>${orderStatusCreated.statusDatetime?string("yyyy-MM-dd")}</font>
                        <#else>
                          <span class="label">${uiLabelMap.CommonPaidDate}</span>${orderStatusCreated.statusDatetime?string("yyyy-MM-dd")}
                        </#if><br />
                        <span class="label">${uiLabelMap.CommonProductTotal}</span>${orderHeader.currencyUom} ${orderHeader.remainingSubTotal}<br />
                        <#assign shippingPrice = orderHeader.grandTotal - orderHeader.remainingSubTotal>
                        <span class="label">${uiLabelMap.CommonShippingPrice}</span>${orderHeader.currencyUom} ${shippingPrice}<br />
                        <span class="label">${uiLabelMap.ProductOrderTotal}</span><!--${uiLabelMap.ProductEstimatedShipCostForShipGroup}</span>-->
                        <#--<#if shipmentCostEstimateForShipGroup?exists>-->
                        ${orderHeader.currencyUom} ${orderHeader.grandTotal}<#--<@ofbizCurrency amount=shipmentCostEstimateForShipGroup isoCode=orderReadHelper.getCurrency()?if_exists/>-->
                      <#-- Bellyanna - END -->
                      </#if>
                    </td>
                    <td>&nbsp;</td>
                    <td valign="top">
                      <span class="label">${uiLabelMap.OrderInstructions}</span>
                      <br />
                      ${orderItemShipGroup.shippingInstructions?default("(${uiLabelMap.CommonNone})")}
                    </td>
                  </tr>
                </table>
              </#if>

              <!-- manual per item form -->
              <#if showInput != "N">
                <hr />
                <form name="singlePackForm" method="post" action="<@ofbizUrl>ProcessPackOrder</@ofbizUrl>">
                  <input type="hidden" name="packageSeq" value="${packingSession.getCurrentPackageSeq()}"/>
                  <input type="hidden" name="orderId" value="${orderId}"/>
                  <input type="hidden" name="shipGroupSeqId" value="${shipGroupSeqId}"/>
                  <input type="hidden" name="facilityId" value="${facilityId?if_exists}"/>
                  <input type="hidden" name="hideGrid" value="${hideGrid}"/>
                  <table cellpadding="2" cellspacing="0">
                    <tr>
                      <td>
                        <div>
                            <span class="label">${uiLabelMap.ProductProductNumber}</span>
                            <input type="text" name="productId" size="20" maxlength="20" value=""/>
                            <input type="hidden" name="quantity" size="6" maxlength="6" value="1"/>
                            <a href="javascript:document.singlePackForm.submit();" class="buttontext">${uiLabelMap.ProductPackItem}</a>
                        </div>
                      </td>
                      <td>
                          <!--<span class="label">${uiLabelMap.ProductCurrentPackageSequence}</span>
                          ${packingSession.getCurrentPackageSeq()}-->
                          <input type="hidden" value="${uiLabelMap.ProductNextPackage}" onclick="javascript:document.incPkgSeq.submit();" />
                      </td>
                    </tr>
                    <#-- Bellyanna - START -->
                </form>
                <!-- complete form -->
                <#if showInput != "N">
                <tr>
                  <td>
                    <form name="completePackForm" method="post" action="<@ofbizUrl>CompletePack</@ofbizUrl>">
                      <input type="hidden" name="orderId" value="${orderId?if_exists}"/>
                      <input type="hidden" name="shipGroupSeqId" value="${shipGroupSeqId?if_exists}"/>
                      <input type="hidden" name="facilityId" value="${facilityId?if_exists}"/>
                      <input type="hidden" name="forceComplete" value="${forceComplete?default('false')}"/>
                      <input type="hidden" name="weightUomId" value="${defaultWeightUomId}"/>
                      <input type="hidden" name="showInput" value="N"/>
                      <input type="hidden" name="carrier" value="${carrier}"/>
                      <input type="hidden" name="shipmentMethodType" value="${orderItemShipGroup.shipmentMethodTypeId?default("??")}"/>
                      <table class="basic-table" cellpadding="2" cellspacing='0'>
                        <tr>
                          <#assign packageSeqIds = packingSession.getPackageSeqIds()/>
                          <#if packageSeqIds?has_content>
                            <td>
                              <span class="label">${uiLabelMap.ProductPackedWeight} (${("uiLabelMap.ProductShipmentUomAbbreviation_" + defaultWeightUomId)?eval}):</span>
                              <br />
                                <#list packageSeqIds as packageSeqId>
                                    ${uiLabelMap.ProductPackage} ${packageSeqId}
                                    <input type="text" size="7" name="packageWeight_${packageSeqId}" value="${packingSession.getPackageWeight(packageSeqId?int)?if_exists}" />
                                    <br />
                                </#list>
                                <#if orderItemShipGroup?has_content>
                                    <input type="hidden" name="shippingContactMechId" value="${orderItemShipGroup.contactMechId?if_exists}"/>
                                    <input type="hidden" name="shipmentMethodTypeId" value="${orderItemShipGroup.shipmentMethodTypeId?if_exists}"/>
                                    <input type="hidden" name="carrierPartyId" value="${orderItemShipGroup.carrierPartyId?if_exists}"/>
                                    <input type="hidden" name="carrierRoleTypeId" value="${orderItemShipGroup.carrierRoleTypeId?if_exists}"/>
                                    <input type="hidden" name="productStoreId" value="${productStoreId?if_exists}"/>
                                </#if>
                            </td>
                            <td>
                                <#list packageSeqIds as packageSeqId>
                                    <span class="label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tracking No ${packageSeqId}:</span><br />
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" class="inputBox packageTrackingCode" size="15" name="packageTrackingCode_${packageSeqId}" value="">
				    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </#list>
                            </td>
                          </#if>
                          <!--<td nowrap="nowrap">
                            <span class="label">${uiLabelMap.ProductAdditionalShippingCharge}:</span>
                            <input type="text" name="additionalShippingCharge" value="${packingSession.getAdditionalShippingCharge()?if_exists}" size="20"/>
                            <#if packageSeqIds?has_content>
                                <a href="javascript:document.completePackForm.action='<@ofbizUrl>calcPackSessionAdditionalShippingCharge</@ofbizUrl>';document.completePackForm.submit();" class="buttontext">${uiLabelMap.ProductEstimateShipCost}</a>
                                <br />
                            </#if>
                          </td>
                          <td>
                            <span class="label">${uiLabelMap.ProductHandlingInstructions}:</span>
                            <br />
                            <textarea name="handlingInstructions" rows="2" cols="30">${packingSession.getHandlingInstructions()?if_exists}</textarea>
                          </td>-->
                          <td align="left">
                            <div>
                              <#assign buttonName = "${uiLabelMap.ProductComplete}">
                              <#if forceComplete?default("false") == "true">
                                <#assign buttonName = "${uiLabelMap.ProductCompleteForce}">
                              </#if>
                              <input type="button" value="${buttonName}" onclick="javascript:document.completePackForm.submit();"/>
                            </div>
                          </td>
                        </tr>
                    <#-- Bellyanna - END -->
                  </table>
                  <br />
                </form>
                </td></tr>
              </#if>
              </table>
              </#if>

              <!-- auto grid form -->
              <#assign itemInfos = packingSession.getItemInfos()?if_exists>
              <#if showInput != "N" && hideGrid != "Y" && itemInfos?has_content>
                <br />
                <form name="multiPackForm" method="post" action="<@ofbizUrl>ProcessBulkPackOrder</@ofbizUrl>">
                  <input type="hidden" name="facilityId" value="${facilityId?if_exists}" />
                  <input type="hidden" name="orderId" value="${orderId?if_exists}" />
                  <input type="hidden" name="shipGroupSeqId" value="${shipGroupSeqId?if_exists}" />
                  <input type="hidden" name="originFacilityId" value="${facilityId?if_exists}" />
                  <input type="hidden" name="hideGrid" value="${hideGrid}"/>

                  <table class="basic-table" cellspacing='0'>
                    <tr class="header-row">
                      <td>&nbsp;</td>
                      <td>${uiLabelMap.ProductItem} ${uiLabelMap.CommonNbr}</td>
                      <td>${uiLabelMap.ProductProductId}</td>
                      <td>${uiLabelMap.ProductPicture}</td>
                      <td align="right">${uiLabelMap.ProductOrderedQuantity}</td>
                      <td align="right">${uiLabelMap.ProductQuantityShipped}</td>
                      <td align="right">${uiLabelMap.ProductPackedQty}</td>
                      <td>&nbsp;</td>
                      <td align="center">${uiLabelMap.ProductPackQty}</td>
                      <td align="center">${uiLabelMap.ProductPackedWeight}&nbsp;(${("uiLabelMap.ProductShipmentUomAbbreviation_" + defaultWeightUomId)?eval})</td>
                      <#if carrierShipmentBoxTypes?has_content>
                        <td align="center">${uiLabelMap.ProductShipmentBoxType}</td>
                      </#if>
                      <td align="center">${uiLabelMap.ProductPackage}</td>
                      <td align="right">&nbsp;<b>*</b>&nbsp;${uiLabelMap.ProductPackages}</td>
                    </tr>

                    <#if (itemInfos?has_content)>
                      <#assign rowKey = 1>
                      <#list itemInfos as itemInfo>
                      <#-- <#list itemInfos as orderItem>  -->
                        <#assign orderItem = itemInfo.orderItem/>
                        <#assign shippedQuantity = orderReadHelper.getItemShippedQuantity(orderItem)?if_exists>
                        <#assign orderItemQuantity = itemInfo.quantity/>
                        <#assign orderProduct = orderItem.getRelatedOne("Product", false)?if_exists/>
                        <#assign product = Static["org.ofbiz.product.product.ProductWorker"].findProduct(delegator, itemInfo.productId)?if_exists/>
                        <#--
                        <#if orderItem.cancelQuantity?exists>
                          <#assign orderItemQuantity = orderItem.quantity - orderItem.cancelQuantity>
                        <#else>
                          <#assign orderItemQuantity = orderItem.quantity>
                        </#if>
                        -->
                        <#assign inputQty = orderItemQuantity - packingSession.getPackedQuantity(orderId, orderItem.orderItemSeqId, shipGroupSeqId, itemInfo.productId)>

                        <tr>
                          <td><input type="checkbox" name="sel_${rowKey}" value="Y" <#if (inputQty >0)>checked=""</#if>/></td>
                          <td><font <#if packingSession.getPackedQuantity(orderId, orderItem.orderItemSeqId, shipGroupSeqId, itemInfo.productId) == orderItemQuantity>color="green"><#else>color="red"></#if>${orderItem.orderItemSeqId}</td>
                          <td>
                            <font <#if packingSession.getPackedQuantity(orderId, orderItem.orderItemSeqId, shipGroupSeqId, itemInfo.productId) == orderItemQuantity>color="green"><#else>color="red"></#if>
                              ${orderProduct.productId?default("N/A")}
                              <#if orderProduct.productId != product.productId>
                                  &nbsp;${product.productId?default("N/A")}
                              </#if>
                            </font>
                          </td>
                          <#-- Bellyanna - START -->
                          <#if product.originalImageUrl?has_content><td>
                            <a href="${product.originalImageUrl}" onclick="window.open(this.href,'','height=480,width=640');return false;"><img alt="${orderProduct.productId?if_exists}" height="160" src="${product.largeImageUrl}"/></a>
                            </td>
                          <#else>
                            <td>${uiLabelMap.CommonNo} ${uiLabelMap.ProductImage}</td>
                          </#if>
                          <td align="right">
                          <#if (orderItemQuantity > 1)>
                            <font color="red"><span class="label">${orderItemQuantity}</span></font><#else>${orderItemQuantity}
                          </#if>
                          </td>
                          <#-- Bellyanna - END -->
                          <td align="right">${shippedQuantity?default(0)}</td>
                          <td align="right">${packingSession.getPackedQuantity(orderId, orderItem.orderItemSeqId, shipGroupSeqId, itemInfo.productId)}</td>
                          <td>&nbsp;</td>
                          <td align="center">
                            <input type="text" size="7" name="qty_${rowKey}" value="${inputQty}" />
                          </td>
                          <td align="center">
                            <input type="text" size="7" name="wgt_${rowKey}" value="" />
                          </td>
                          <#if carrierShipmentBoxTypes?has_content>
                            <td align="center">
                              <select name="boxType_${rowKey}">
                                <option value=""></option>
                                <#list carrierShipmentBoxTypes as carrierShipmentBoxType>
                                  <#assign shipmentBoxType = carrierShipmentBoxType.getRelatedOne("ShipmentBoxType", false) />
                                  <option value="${shipmentBoxType.shipmentBoxTypeId}">${shipmentBoxType.description?default(shipmentBoxType.shipmentBoxTypeId)}</option>
                                </#list>
                              </select>
                            </td>
                          </#if>
                          <td align="center">
                            <select name="pkg_${rowKey}">
                              <#if packingSession.getPackageSeqIds()?exists>
                                <#list packingSession.getPackageSeqIds() as packageSeqId>
                                  <option value="${packageSeqId}">${uiLabelMap.ProductPackage} ${packageSeqId}</option>
                                </#list>
                                <#assign nextPackageSeqId = packingSession.getPackageSeqIds().size() + 1>
                                <option value="${nextPackageSeqId}">${uiLabelMap.ProductNextPackage}</option>
                              <#else>
                                <option value="1">${uiLabelMap.ProductPackage} 1</option>
                                <option value="2">${uiLabelMap.ProductPackage} 2</option>
                                <option value="3">${uiLabelMap.ProductPackage} 3</option>
                                <option value="4">${uiLabelMap.ProductPackage} 4</option>
                                <option value="5">${uiLabelMap.ProductPackage} 5</option>
                              </#if>
                            </select>
                          </td>
                          <td align="right">
                            <input type="text" size="7" name="numPackages_${rowKey}" value="1" />
                          </td>
                          <input type="hidden" name="prd_${rowKey}" value="${itemInfo.productId?if_exists}"/>
                          <input type="hidden" name="ite_${rowKey}" value="${orderItem.orderItemSeqId}"/>
                        </tr>
                        <#assign rowKey = rowKey + 1>
                      </#list>
                    </#if>
                    <tr><td colspan="10">&nbsp;</td></tr>
                    <tr>
                      <td colspan="12" align="right">
                        <input type="submit" value="${uiLabelMap.ProductPackItem}" />
                        &nbsp;
                        <input type="button" value="${uiLabelMap.CommonClear} (${uiLabelMap.CommonAll})" onclick="javascript:document.clearPackForm.submit();"/>
                      </td>
                    </tr>
                  </table>
                </form>
                <br />
              </#if>

              <!-- complete form moved to above -->
              <!--<#if showInput != "N">
                <form name="completePackForm" method="post" action="<@ofbizUrl>CompletePack</@ofbizUrl>">
                  <input type="hidden" name="orderId" value="${orderId?if_exists}"/>
                  <input type="hidden" name="shipGroupSeqId" value="${shipGroupSeqId?if_exists}"/>
                  <input type="hidden" name="facilityId" value="${facilityId?if_exists}"/>
                  <input type="hidden" name="forceComplete" value="${forceComplete?default('false')}"/>
                  <input type="hidden" name="weightUomId" value="${defaultWeightUomId}"/>
                  <input type="hidden" name="showInput" value="N"/>
                  <input type="hidden" name="carrier" value="${carrier}"/>
                  <input type="hidden" name="shipmentMethodType" value="${orderItemShipGroup.shipmentMethodTypeId?default("??")}"/>
                  <hr/>
                  <table class="basic-table" cellpadding="2" cellspacing='0'>
                    <tr>
                        <#assign packageSeqIds = packingSession.getPackageSeqIds()/>
                        <#if packageSeqIds?has_content>
                            <td>
                                <span class="label">${uiLabelMap.ProductPackedWeight} (${("uiLabelMap.ProductShipmentUomAbbreviation_" + defaultWeightUomId)?eval}):</span>
                                <br />
                                <#list packageSeqIds as packageSeqId>
                                    ${uiLabelMap.ProductPackage} ${packageSeqId}
                                    <input type="text" size="7" name="packageWeight_${packageSeqId}" value="${packingSession.getPackageWeight(packageSeqId?int)?if_exists}" />
                                    <br />
                                </#list>
                                <#if orderItemShipGroup?has_content>
                                    <input type="hidden" name="shippingContactMechId" value="${orderItemShipGroup.contactMechId?if_exists}"/>
                                    <input type="hidden" name="shipmentMethodTypeId" value="${orderItemShipGroup.shipmentMethodTypeId?if_exists}"/>
                                    <input type="hidden" name="carrierPartyId" value="${orderItemShipGroup.carrierPartyId?if_exists}"/>
                                    <input type="hidden" name="carrierRoleTypeId" value="${orderItemShipGroup.carrierRoleTypeId?if_exists}"/>
                                    <input type="hidden" name="productStoreId" value="${productStoreId?if_exists}"/>
                                </#if>
                            </td>
                            <td>
                              <#list packageSeqIds as packageSeqId>
                                <span class="label">Tracking No ${packageSeqId}:</span>
                                <input type="text" class="inputBox packageTrackingCode" size="15" name="packageTrackingCode_${packageSeqId}" value="">
                              </#list>
                            </td>
                          </#if>-->
                          <!--<td nowrap="nowrap">
                            <span class="label">${uiLabelMap.ProductAdditionalShippingCharge}:</span>

                            <input type="text" name="additionalShippingCharge" value="${packingSession.getAdditionalShippingCharge()?if_exists}" size="20"/>
                            <#if packageSeqIds?has_content>
                                <a href="javascript:document.completePackForm.action='<@ofbizUrl>calcPackSessionAdditionalShippingCharge</@ofbizUrl>';document.completePackForm.submit();" class="buttontext">${uiLabelMap.ProductEstimateShipCost}</a>
                                <br />
                            </#if>
                        </td>
                      <td>
                        <span class="label">${uiLabelMap.ProductHandlingInstructions}:</span>
                        <br />
                        <textarea name="handlingInstructions" rows="2" cols="30">${packingSession.getHandlingInstructions()?if_exists}</textarea>
                      </td>-->
                      <!--<td align="right">
                        <div>
                          <#assign buttonName = "${uiLabelMap.ProductComplete}">
                          <#if forceComplete?default("false") == "true">
                            <#assign buttonName = "${uiLabelMap.ProductCompleteForce}">
                          </#if>
                          <input type="button" value="${buttonName}" onclick="javascript:document.completePackForm.submit();"/>
                        </div>
                      </td>
                    </tr>
                  </table>
                  <br />
                </form>
              </#if>-->
        </div>
    </div>

    <!-- display items in packages, per packed package and in order -->
    <#--<#assign linesByPackageResultMap = packingSession.getPackingSessionLinesByPackage()?if_exists>
    <#assign packageMap = linesByPackageResultMap.get("packageMap")?if_exists>
    <#assign sortedKeys = linesByPackageResultMap.get("sortedKeys")?if_exists>
    <#if ((packageMap?has_content) && (sortedKeys?has_content))>
      <div class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3">${uiLabelMap.ProductPackages} : ${sortedKeys.size()?if_exists}</li>
            </ul>
            <br class="clear"/>
        </div>
          <div class="screenlet-body">
            <#list sortedKeys as key>
              <#assign packedLines = packageMap.get(key)>
              <#if packedLines?has_content>
                <br />
                <#assign packedLine = packedLines.get(0)?if_exists>
                <span class="label" style="font-size:1.2em">${uiLabelMap.ProductPackage}&nbsp;${packedLine.getPackageSeq()?if_exists}</span>
                <br />
                <table class="basic-table" cellspacing='0'>
                  <tr class="header-row">
                    <td>${uiLabelMap.ProductItem} ${uiLabelMap.CommonNbr}</td>
                    <td>${uiLabelMap.ProductProductId}</td>
                    <td>${uiLabelMap.ProductProductDescription}</td>
                    <td>${uiLabelMap.ProductInventoryItem} ${uiLabelMap.CommonNbr}</td>
                    <td align="right">${uiLabelMap.ProductPackedQty}</td>
                    <td align="right">${uiLabelMap.ProductPackedWeight}&nbsp;(${("uiLabelMap.ProductShipmentUomAbbreviation_" + defaultWeightUomId)?eval})&nbsp;(${uiLabelMap.ProductPackage})</td>
                    <td align="right">${uiLabelMap.ProductPackage} ${uiLabelMap.CommonNbr}</td>
                    <td>&nbsp;</td>
                  </tr>
                  <#list packedLines as line>
                    <#assign product = Static["org.ofbiz.product.product.ProductWorker"].findProduct(delegator, line.getProductId())/>
                    <tr>
                      <td>${line.getOrderItemSeqId()}</td>
                      <td>${line.getProductId()?default("N/A")}</td>
                      <td>
                          <a href="/catalog/control/EditProduct?productId=${line.getProductId()?if_exists}${externalKeyParam}" class="buttontext" target="_blank">${product.internalName?if_exists?default("[N/A]")}</a>
                      </td>
                      <td>${line.getInventoryItemId()}</td>
                      <td align="right">${line.getQuantity()}</td>
                      <td align="right">${line.getWeight()} (${packingSession.getPackageWeight(line.getPackageSeq()?int)?if_exists})</td>
                      <td align="right">${line.getPackageSeq()}</td>
                      <td align="right"><a href="javascript:clearLine('${facilityId}', '${line.getOrderId()}', '${line.getOrderItemSeqId()}', '${line.getProductId()?default("")}', '${line.getShipGroupSeqId()}', '${line.getInventoryItemId()}', '${line.getPackageSeq()}')" class="buttontext">${uiLabelMap.CommonClear}</a></td>
                    </tr>
                  </#list>
                </table>
              </#if>
            </#list>
          </div>
      </div>
    </#if>-->

    <!-- packed items display -->
    <#--<#assign packedLines = packingSession.getLines()?if_exists>
    <#if packedLines?has_content>
      <div class="screenlet">
          <div class="screenlet-title-bar">
              <ul>
                  <li class="h3">${uiLabelMap.ProductItems} (${uiLabelMap.ProductPackages}): ${packedLines.size()?if_exists}</li>
              </ul>
              <br class="clear"/>
          </div>
          <div class="screenlet-body">
            <table class="basic-table" cellspacing='0'>
              <tr class="header-row">
                  <td>${uiLabelMap.ProductItem} ${uiLabelMap.CommonNbr}</td>
                  <td>${uiLabelMap.ProductProductId}</td>
                  <td>${uiLabelMap.ProductProductDescription}</td>
                  <td>${uiLabelMap.ProductInventoryItem} ${uiLabelMap.CommonNbr}</td>
                  <td align="right">${uiLabelMap.ProductPackedQty}</td>
                  <td align="right">${uiLabelMap.ProductPackedWeight}&nbsp;(${("uiLabelMap.ProductShipmentUomAbbreviation_" + defaultWeightUomId)?eval})&nbsp;(${uiLabelMap.ProductPackage})</td>
                  <td align="right">${uiLabelMap.ProductPackage} ${uiLabelMap.CommonNbr}</td>
                  <td>&nbsp;</td>
              </tr>
              <#list packedLines as line>
                  <#assign product = Static["org.ofbiz.product.product.ProductWorker"].findProduct(delegator, line.getProductId())/>
                  <tr>
                      <td>${line.getOrderItemSeqId()}</td>
                      <td>${line.getProductId()?default("N/A")}</td>
                      <td>
                          <a href="/catalog/control/EditProduct?productId=${line.getProductId()?if_exists}${externalKeyParam}" class="buttontext" target="_blank">${product.internalName?if_exists?default("[N/A]")}</a>
                      </td>
                      <td>${line.getInventoryItemId()}</td>
                      <td align="right">${line.getQuantity()}</td>
                      <td align="right">${line.getWeight()} (${packingSession.getPackageWeight(line.getPackageSeq()?int)?if_exists})</td>
                      <td align="right">${line.getPackageSeq()}</td>
                      <td align="right"><a href="javascript:clearLine('${facilityId}', '${line.getOrderId()}', '${line.getOrderItemSeqId()}', '${line.getProductId()?default("")}', '${line.getShipGroupSeqId()}', '${line.getInventoryItemId()}', '${line.getPackageSeq()}')" class="buttontext">${uiLabelMap.CommonClear}</a></td>
                  </tr>
              </#list>
            </table>
          </div>
      </div>
    </#if>-->
  </#if>

  <#if orderId?has_content>
    <script language="javascript" type="text/javascript">
      document.singlePackForm.productId.focus();
    </script>
  <#else>
    <script language="javascript" type="text/javascript">
      document.selectOrderForm.orderId.focus();
    </script>
  </#if>
<#else>
  <h3>${uiLabelMap.ProductFacilityViewPermissionError}</h3>
</#if>
