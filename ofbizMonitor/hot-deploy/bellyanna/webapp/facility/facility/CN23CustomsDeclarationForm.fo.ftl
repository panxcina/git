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
<#-- Bellyanna Shipping Label -->
<fo:layout-master-set>
    <fo:simple-page-master master-name="main" page-height="144mm" page-width="210mm"
            margin-top="1mm" margin-bottom="1mm" margin-left="1mm" margin-right="1mm">
        <fo:region-body region-name="body" margin-top="0in"/>
        <fo:region-before extent="0in"/>
        <fo:region-after region-name="after" extent="0in"/>
        <fo:region-start region-name="start" extent="0in"/>
    </fo:simple-page-master>
</fo:layout-master-set>

    <#if security.hasEntityPermission("FACILITY", "_VIEW", session)>
    <#if picklistInfo?has_content>
    <#list picklistInfo.picklistBinInfoList as picklistBinInfo>
        <#assign picklistBin = picklistBinInfo.picklistBin>
	<#assign orderId = picklistBinInfo.primaryOrderHeader.orderId>
	<#assign orderHeader = delegator.findByPrimaryKey("OrderHeader", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId",orderId))>
        <#assign contactMechId = picklistBinInfo.primaryOrderItemShipGroup.contactMechId>
        <#assign postalAddress = delegator.findByPrimaryKey("PostalAddress", Static["org.ofbiz.base.util.UtilMisc"].toMap("contactMechId",contactMechId))>
        <#assign country = delegator.findByPrimaryKey("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId", postalAddress.countryGeoId))>
	<#assign orderContactMech = orderHeader.getRelated("OrderContactMech")>
	<#list orderContactMech as orderContactMechChildren>
	<#if orderContactMechChildren.contactMechPurposeTypeId == "PRIMARY_PHONE">
	<#assign phoneContactMechId = orderContactMechChildren.contactMechId>
	<#assign phone = delegator.findByPrimaryKey("TelecomNumber", Static["org.ofbiz.base.util.UtilMisc"].toMap("contactMechId",phoneContactMechId))>
	</#if></#list>
        <fo:page-sequence master-reference="main">
	<#-- Barcode region -->
        <#-- <fo:static-content flow-name="start">
        <fo:block-container reference-orientation="90">
            <fo:block font-size="7pt" text-align="center">
                <fo:instream-foreign-object>
                    <barcode:barcode xmlns:barcode="http://barcode4j.krysalis.org/ns"
                            message="${picklistBinInfo.primaryOrderHeader.orderId}"> /${picklistBinInfo.primaryOrderItemShipGroup.shipGroupSeqId}">
                        <barcode:code39>
                            <barcode:height>15mm</barcode:height>
			    <barcode:module-width>0.375mm</barcode:module-width>
                        </barcode:code39>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>
        </fo:block-container>
        </fo:static-content> -->
	<#-- Footer region
        <fo:static-content flow-name="after">
            <fo:block font-size="10pt" margin-left="5em">${picklistInfo.picklist.picklistId}-${picklistBin.binLocationNumber}</fo:block>
        </fo:static-content> -->
	<#-- CN23 Customs Declaration Body region -->
        <fo:flow flow-name="body" font-family="Arial">
            <#-- <fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block> -->
            <#if postalAddress.toName?has_content>
                <fo:block font-size="9pt" margin-left="9em" margin-top="11.5em">${postalAddress.toName}</fo:block>
	    <#else>
		<fo:block font-size="9pt" margin-left="9em" margin-top="11.5em">&#xa0;</fo:block>
            </#if>
		<fo:block font-size="9pt" margin-left="9em">${postalAddress.address1}</fo:block>
            <#if postalAddress.address2?has_content>
                 <fo:block font-size="9pt" margin-left="9em">${postalAddress.address2}</fo:block>
	    <#else>
		<fo:block font-size="9pt" margin-left="9em">&#xa0;</fo:block>
            </#if>
            <fo:block font-size="9pt" margin-left="9em">${postalAddress.postalCode} ${postalAddress.city}, ${country.geoName}.
	    <#if (orderContactMech.size() > 3) >
	       Tel: <#if phone.areaCode?has_content>${phone.areaCode} - </#if>${phone.contactNumber}</fo:block>
	    </#if>
	    
	    <fo:table margin-left="3.75em" margin-top="3em">
	    <fo:table-column column-width="44mm"/>
            <fo:table-column column-width="13mm"/>
            <fo:table-column column-width="23mm"/>
            <fo:table-column column-width="20mm"/>
            <fo:table-column column-width="34mm"/>
            <fo:table-column column-width="37mm"/>
		<fo:table-header>
		    <fo:table-row>
			<fo:table-cell><fo:block text-align="center">Gift&#xa0;-&#xa0;Dress</fo:block>
			</fo:table-cell>
                        <fo:table-cell><fo:block text-align="center">1</fo:block>
                        </fo:table-cell>
                        <fo:table-cell><fo:block text-align="center">450g</fo:block>
                        </fo:table-cell>
                        <fo:table-cell><fo:block text-align="center">$5</fo:block>
                        </fo:table-cell>
                        <fo:table-cell><fo:block text-align="center"></fo:block>
                        </fo:table-cell>
                        <fo:table-cell><fo:block text-align="center">China</fo:block>
                        </fo:table-cell>
		    </fo:table-row>
		</fo:table-header>
		<fo:table-body>
		    <fo:table-row>
                        <fo:table-cell><fo:block>&#xa0;</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell><fo:block>&#xa0;</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell><fo:block>&#xa0;</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell><fo:block>&#xa0;</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell><fo:block>&#xa0;</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell><fo:block font-size="17pt">x</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
		</fo:table-body>
	    </fo:table>
	    
        </fo:flow>
        </fo:page-sequence>
    </#list>
    </#if>
    <#else>
        <fo:block font-size="14pt">
            ${uiLabelMap.ProductFacilityViewPermissionError}
        </fo:block>
    </#if>
</fo:root>
</#escape>
