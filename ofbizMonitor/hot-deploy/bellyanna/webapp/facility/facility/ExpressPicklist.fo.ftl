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
<#-- Express pick/pack report -->
<fo:layout-master-set>
    <fo:simple-page-master master-name="main" page-height="11in" page-width="8.5in"
            margin-top="10mm" margin-bottom="10mm" margin-left="10mm" margin-right="10mm">
        <fo:region-body margin-top="10mm"/>
        <fo:region-before extent="10mm"/>
        <fo:region-after extent="10mm"/>
    </fo:simple-page-master>
</fo:layout-master-set>

<fo:page-sequence master-reference="main">
    <#-- Header Start -->
    <fo:static-content flow-name="xsl-region-before">
    <#if picklistInfo?has_content>
        <fo:block font-size="12pt">${uiLabelMap.ProductPickList} ${picklistInfo.picklist.picklistId} ${uiLabelMap.CommonIn} ${picklistInfo.facility.facilityName} <fo:inline font-size="8pt">[${picklistInfo.facility.facilityId}]</fo:inline></fo:block>
        <#if picklistInfo.shipmentMethodType?has_content>
            <fo:block font-size="10pt">${uiLabelMap.CommonFor} ${uiLabelMap.ProductShipmentMethodType} ${picklistInfo.shipmentMethodType.description?default(picklistInfo.shipmentMethodType.shipmentMethodTypeId)}</fo:block>
        </#if>
    </#if>
    </fo:static-content>
    <#-- Header End -->

    <#-- Body Start -->
    <fo:flow flow-name="xsl-region-body" font-family="Helvetica">
    <#if security.hasEntityPermission("FACILITY", "_VIEW", session)>
    <#if picklistInfo?has_content>
    <fo:table>
        <fo:table-column column-width="250pt"/>
        <fo:table-header>
            <fo:table-row font-weight="bold">
                <fo:table-cell border-bottom="thin solid grey"><fo:block>${uiLabelMap.ProductOrderId}</fo:block></fo:table-cell>
            </fo:table-row>
        </fo:table-header>
        <fo:table-body>	
            	<#list picklistInfo.picklistBinInfoList as picklistBinInfo>
        	<#assign picklistBin = picklistBinInfo.picklistBin>
		<#assign orderId = picklistBinInfo.primaryOrderHeader.orderId>
		<fo:table-row>
		<fo:table-cell>
        	<fo:block-container>
            	<fo:block font-size="7pt">
		<fo:instream-foreign-object>
                    <barcode:barcode xmlns:barcode="http://barcode4j.krysalis.org/ns"
                            message="${orderId}">
                        <barcode:code39>
                            <barcode:height>15mm</barcode:height>
			    <barcode:module-width>0.4mm</barcode:module-width>
                        </barcode:code39>
                    </barcode:barcode>
                </fo:instream-foreign-object>
		</fo:block>
		</fo:block-container>
		</fo:table-cell>
		</fo:table-row>
		</#list>
	</fo:table-body>
    </fo:table>
    </#if>
    <#else>
        <fo:block font-size="14pt">
            ${uiLabelMap.ProductFacilityViewPermissionError}
        </fo:block>
    </#if>
    </fo:flow>
    <#-- Body End -->
</fo:page-sequence>
</fo:root>
</#escape>
