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
<#-- Bellyanna OrderShippingLabel -->
<fo:layout-master-set>
    <fo:simple-page-master master-name="main" page-height="50mm" page-width="100mm"
            margin-top="0mm" margin-bottom="0mm" margin-left="1mm" margin-right="1mm">
        <fo:region-body region-name="body" margin-top="9mm"/>
        <fo:region-before region-name ="before" extent="0in"/>
        <#--<fo:region-after region-name="after" extent="0in"/> -->
        <fo:region-start region-name="start"/>
    </fo:simple-page-master>
</fo:layout-master-set>

<#-- <#if displayParty?has_content || orderContactMechValueMaps?has_content> -->
<#list orderContactMechValueMaps as orderContactMechValueMap>
	<#assign contactMech = orderContactMechValueMap.contactMech>
	<#if contactMech.contactMechTypeId == "POSTAL_ADDRESS">
        <#assign postalAddress = orderContactMechValueMap.postalAddress>
	<#assign countryState = delegator.findByPrimaryKey("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId", postalAddress.stateProvinceGeoId))>
        <#assign country = delegator.findByPrimaryKey("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId", postalAddress.countryGeoId))>
	</#if>
</#list>
<#assign phoneContactMechs = delegator.findByAnd("OrderContactMech", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId", orderId, "contactMechPurposeTypeId", "PRIMARY_PHONE"))>
<#list phoneContactMechs as phoneContactMech>
    <#assign phone = delegator.findByPrimaryKey("TelecomNumber", Static["org.ofbiz.base.util.UtilMisc"].toMap("contactMechId", phoneContactMech.contactMechId))>
</#list>
<fo:page-sequence master-reference="main">
	<#-- Barcode region -->
        <fo:static-content flow-name="before">
        <fo:block-container reference-orientation="0">
            <fo:block font-size="7pt" text-align="center">
                <fo:instream-foreign-object>
                    <barcode:barcode xmlns:barcode="http://barcode4j.krysalis.org/ns"
                            message="${orderId}">
                        <barcode:code39>
                        <barcode:height>8mm</barcode:height>
			<barcode:module-width>0.3mm</barcode:module-width>
			<barcode:wide-factor>2</barcode:wide-factor>
                        <barcode:interchar-gap-width>0.5mm</barcode:interchar-gap-width>
                        </barcode:code39>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>
        </fo:block-container>
        </fo:static-content>

	<fo:static-content flow-name="start">
        <fo:block-container reference-orientation="90">
        <#if postalAddress.countryGeoId == "AUS">
        <fo:table margin-left="3.0em" margin-top="0.2em">
            <fo:table-column column-width="80pt"/>
            <fo:table-header>
                <fo:table-row margin-left="-3.0em">
                    <fo:table-cell border="thin solid grey">
					<fo:block font-size="12pt">Made in China</fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-header>
            <fo:table-body>
            <fo:table-row >
                    <fo:table-cell></fo:table-cell>
            </fo:table-row>
            </fo:table-body>
        </fo:table>
        <#else>
        <fo:block font-size="12pt"></fo:block>
        </#if>
        </fo:block-container>
        </fo:static-content>

	<#-- Shipping Label Body region -->
        <fo:flow flow-name="body">
            <#--<fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block>-->
            <#if postalAddress.toName?has_content>
                 <fo:block font-family="Quivira" font-size="12pt" margin-left="3.5em">${postalAddress.toName}</fo:block>
            </#if>
            <fo:block font-family="Quivira" font-size="12pt" margin-left="3.5em">${postalAddress.address1}</fo:block>
            <#if postalAddress.address2?has_content>
                 <fo:block font-family="Quivira" font-size="12pt" margin-left="3.5em">${postalAddress.address2}</fo:block>
            </#if>
            <fo:block font-family="Quivira" font-size="12pt" margin-left="3.5em"><#if postalAddress.city != "default ">${postalAddress.city}</#if> <#if postalAddress.postalCode != "default">${postalAddress.postalCode}</#if></fo:block>
            <#if countryState.geoName?has_content>
				<#if countryState.geoId != "_NA_">
					<fo:block font-family="Quivira" font-size="12pt" margin-left="3.5em">${countryState.geoName}</fo:block>
				</#if>
            </#if>
            <fo:block font-family="SimHei" font-size="12pt" margin-left="3.5em">${country.geoName} (${postalAddress.countryGeoId}<#if country.wellKnownText?has_content> / ${country.wellKnownText}</#if>)</fo:block>
	    <#if phoneContactMechs?has_content>
	    <fo:block font-size="12pt" margin-left="3.5em">Tel: <#if phone.areaCode?has_content>${phone.areaCode} - </#if>${phone.contactNumber}</fo:block>
	    </#if>
        </fo:flow>
        </fo:page-sequence>
    <#-- </#list> -->
<#-- </#if> -->

</fo:root>
</#escape>
