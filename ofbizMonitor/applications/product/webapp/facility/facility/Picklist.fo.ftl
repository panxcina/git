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

<#escape x as x?xml>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

<#-- It is important to set defaults for facilityLocation and facilityLocationInfo in case the picklisted item has no location defined in facility.
  Because these defaults are scalars, we must then use the ?is_hash check directive as well before trying to access them -->
<#macro pickInfoDetail pickQuantity picklistBinInfoList product facilityLocation="" facilityLocationInfo="">
    <fo:table-row>
        <fo:table-cell padding="2pt" background-color="${rowColor}">
            <#if (facilityLocation?has_content) && (facilityLocation?is_hash)>
                <fo:block>${facilityLocation.areaId?if_exists}-${facilityLocation.aisleId?if_exists}-${facilityLocation.sectionId?if_exists}-${facilityLocation.levelId?if_exists}-${facilityLocation.positionId?if_exists}</fo:block>
            <#else>
                <fo:block>[${uiLabelMap.ProductNoLocation}]</fo:block>
            </#if>
        </fo:table-cell>
        <fo:table-cell padding="2pt" background-color="${rowColor}">
            <fo:block>${pickQuantity}</fo:block>
        </fo:table-cell>
        <fo:table-cell padding="2pt" background-color="${rowColor}">
            <#if product?has_content>
                <fo:block>${product.productId}</fo:block>
            <#else>
                <fo:block>&#xa0;</fo:block>
            </#if>
            <#if (facilityLocationInfo?has_content) && (facilityLocationInfo?is_hash) && (facilityLocationInfo.message)?has_content>
                <fo:block>${product.productId},&#xa0;&#xa0;&#xa0;&#xa0;${facilityLocationInfo.message?if_exists}</fo:block>
            </#if>
        </fo:table-cell>
        <#--<fo:table-cell padding="2pt" background-color="${rowColor}">
            <#list picklistBinInfoList as picklistBinInfo>
                <fo:block>${picklistBinInfo.quantity} ${uiLabelMap.CommonTo} #${picklistBinInfo.picklistBin.binLocationNumber}</fo:block>
            </#list>
        </fo:table-cell>
        <fo:table-cell padding="2pt" background-color="${rowColor}">
            <#list picklistBinInfoList as picklistBinInfo>
                <fo:block>${picklistBinInfo.picklistBin.primaryOrderId?if_exists}</fo:block>
            </#list>
        </fo:table-cell>-->
    </fo:table-row>
    <#-- toggle the row color -->
    <#if rowColor == "white">
        <#assign rowColor = "#D4D0C8">
    <#else>
        <#assign rowColor = "white">
    </#if>
</#macro>

<#macro picklistItemInfoDetail picklistItemInfo product facilityLocation>
    <#local picklistItem = picklistItemInfo.picklistItem>
    <#local orderItem = picklistItemInfo.orderItem>
    <fo:table-row>
        <fo:table-cell padding="2pt" background-color="${rowColor}">
            <#if facilityLocation?has_content>
                <fo:block>${facilityLocation.areaId?if_exists}-${facilityLocation.aisleId?if_exists}-${facilityLocation.sectionId?if_exists}-${facilityLocation.levelId?if_exists}-${facilityLocation.positionId?if_exists}</fo:block>
            <#else>
                <fo:block>[${uiLabelMap.ProductNoLocation}]</fo:block>
            </#if>
        </fo:table-cell>
        <fo:table-cell padding="2pt" background-color="${rowColor}">
            <fo:block>${picklistItem.quantity}</fo:block>
        </fo:table-cell>
        <fo:table-cell padding="2pt" background-color="${rowColor}">
            <#if product?has_content>
                <fo:block>${product.productId}</fo:block>
            <#else>
                <fo:block>&#xa0;</fo:block>
            </#if>
        </fo:table-cell>
        <#--<fo:table-cell padding="2pt" background-color="${rowColor}">
            <fo:block>${picklistItem.quantity}</fo:block>
        </fo:table-cell>
        <fo:table-cell padding="2pt" background-color="${rowColor}">
            <fo:block>${orderItemShipGrpInvRes.orderId}:${orderItem.orderItemSeqId}</fo:block>
        </fo:table-cell>
        <fo:table-cell padding="2pt" background-color="${rowColor}">
            <fo:block>
                ${picklistItemInfo.inventoryItemAndLocation.inventoryItemId}<#if picklistItemInfo.inventoryItemAndLocation.binNumber?exists>:${picklistItemInfo.inventoryItemAndLocation.binNumber}</#if>
            </fo:block>
        </fo:table-cell>-->
    </fo:table-row>
    <#-- toggle the row color -->
    <#if rowColor == "white">
        <#assign rowColor = "#D4D0C8">
    <#else>
        <#assign rowColor = "white">
    </#if>
</#macro>

<!--
     - picklist
     - facility
     - statusItem
     - statusValidChangeToDetailList
     - picklistRoleInfoList (picklistRole, partyNameView, roleType)
     - picklistStatusHistoryInfoList (picklistStatusHistory, statusItem, statusItemTo)
     - picklistBinInfoList
       - picklistBin
       - primaryOrderHeader
       - primaryOrderItemShipGroup
       - picklistItemInfoList (picklistItem, picklistBin, orderItem, product, inventoryItemAndLocation, orderItemShipGrpInvRes, itemIssuanceList)
-->
<fo:layout-master-set>
    <fo:simple-page-master master-name="main" page-height="11in" page-width="8.5in"
            margin-top="0.05in" margin-bottom="0.05in" margin-left="0.5in" margin-right="0.5in">
        <fo:region-body margin-top="0.5in"/>
        <fo:region-before extent="0.5in"/>
        <fo:region-after extent="0.5in"/>
    </fo:simple-page-master>
</fo:layout-master-set>

<fo:page-sequence master-reference="main">
    <fo:static-content flow-name="xsl-region-before">
    <#if picklistInfo?has_content>
        <fo:block font-size="12pt">${uiLabelMap.ProductPickList} ${picklistInfo.picklist.picklistId} ${uiLabelMap.CommonIn} ${uiLabelMap.ProductFacility} ${picklistInfo.facility.facilityName} <fo:inline font-size="8pt">[${picklistInfo.facility.facilityId}]</fo:inline></fo:block>
        <#if picklistInfo.shipmentMethodType?has_content>
            <fo:block font-size="10pt">${uiLabelMap.CommonFor} ${uiLabelMap.ProductShipmentMethodType} ${picklistInfo.shipmentMethodType.description?default(picklistInfo.shipmentMethodType.shipmentMethodTypeId)}</fo:block>
        </#if>
        <fo:block><fo:leader/></fo:block>
    </#if>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body" font-family="Helvetica">
    <#if security.hasEntityPermission("FACILITY", "_VIEW", session)>
    <fo:block space-after.optimum="10pt" font-size="10pt">
    <fo:table>
        <fo:table-column column-width="90pt"/>
        <fo:table-column column-width="50pt"/>
        <fo:table-column column-width="380pt"/>
        <fo:table-header>
            <fo:table-row font-weight="bold">
                <fo:table-cell border-bottom="thin solid grey"><fo:block>${uiLabelMap.ProductLocation}</fo:block></fo:table-cell>
                <fo:table-cell border-bottom="thin solid grey"><fo:block>${uiLabelMap.CommonQuantity}</fo:block></fo:table-cell>
                <fo:table-cell border-bottom="thin solid grey"><fo:block>${uiLabelMap.ProductProductId}</fo:block></fo:table-cell>
                <#--<fo:table-cell border-bottom="thin solid grey"><fo:block>${uiLabelMap.ProductToPick}</fo:block></fo:table-cell>
                <fo:table-cell border-bottom="thin solid grey"><fo:block>${uiLabelMap.ProductQuantityToBin} ${uiLabelMap.CommonNbr}</fo:block></fo:table-cell>
                <fo:table-cell border-bottom="thin solid grey"><fo:block>${uiLabelMap.ProductOrderId}</fo:block></fo:table-cell>-->

              <#-- Not display details here, just the summary info for the bins
                <fo:table-cell border-bottom="thin solid grey"><fo:block>${uiLabelMap.ProductOrderItems}</fo:block></fo:table-cell>
                <fo:table-cell border-bottom="thin solid grey"><fo:block>${uiLabelMap.ProductInventoryItems}</fo:block></fo:table-cell>
              -->
            </fo:table-row>
        </fo:table-header>
        <fo:table-body>
            <#--
              2. facilityLocationInfoList (facilityLocation, product, pickQuantity, picklistBinInfoList (picklistBin, quantity), picklistItemInfoList (picklistItem, picklistBin, orderItem, product, inventoryItemAndLocation, orderItemShipGrpInvRes, itemIssuanceList))
              3. noLocationProductInfoList (product, pickQuantity, picklistBinInfoList (picklistBin, quantity), picklistItemInfoList (picklistItem, picklistBin, orderItem, product, inventoryItemAndLocation, orderItemShipGrpInvRes, itemIssuanceList))
            -->
            <#if facilityLocationInfoList?has_content || noLocationProductInfoList?has_content>
                <#assign rowColor = "white">
                <#if facilityLocationInfoList?has_content>
                <#list facilityLocationInfoList as facilityLocationInfo>
                    <@pickInfoDetail pickQuantity=facilityLocationInfo.pickQuantity picklistBinInfoList=facilityLocationInfo.picklistBinInfoList product=facilityLocationInfo.product facilityLocation=facilityLocationInfo.facilityLocation facilityLocationInfo=facilityLocationInfo/>
                  <#-- Not display details here, just the summary info for the bins
                    <#list facilityLocationInfo.picklistItemInfoList as picklistItemInfo>
                        <@picklistItemInfoDetail picklistItemInfo=picklistItemInfo product=picklistItemInfo.product facilityLocation=facilityLocationInfo.facilityLocation/>
                    </#list>
                  -->
                </#list>
                </#if>

                <#if noLocationProductInfoList?has_content>
                <#list noLocationProductInfoList as noLocationProductInfo>
                    <@pickInfoDetail pickQuantity=noLocationProductInfo.pickQuantity picklistBinInfoList=noLocationProductInfo.picklistBinInfoList product=noLocationProductInfo.product facilityLocation=null facilityLocationInfo=null/>
                  <#-- Not display details here, just the summary info for the bins
                    <#list noLocationProductInfo.picklistItemInfoList as picklistItemInfo>
                        <@picklistItemInfoDetail picklistItemInfo=picklistItemInfo product=noLocationProductInfo.product facilityLocation=null/>
                    </#list>
                  -->
                </#list>
                </#if>
            <#else>
                <fo:table-row font-weight="bold">
                    <fo:table-cell><fo:block>${uiLabelMap.ProductNoInventoryFoundToPick}.</fo:block></fo:table-cell>
                </fo:table-row>
            </#if>
        </fo:table-body>
    </fo:table>
    </fo:block>
</fo:flow>
</fo:page-sequence>


    <#else>
        <fo:block font-size="14pt">
            ${uiLabelMap.ProductFacilityViewPermissionError}
        </fo:block>
    </#if>
</fo:root>
</#escape>
