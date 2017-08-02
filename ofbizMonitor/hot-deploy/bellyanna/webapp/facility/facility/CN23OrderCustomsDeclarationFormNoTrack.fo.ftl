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
<#-- Bellyanna CN23 Order Custom Declaration Form -->
<fo:layout-master-set>
    <fo:simple-page-master master-name="main" page-height="146mm" page-width="210mm"
            margin-top="1mm" margin-bottom="1mm" margin-left="1mm" margin-right="1mm">
        <fo:region-body region-name="body" margin-top="0in"/>
        <fo:region-before page-width="0mm" page-height="0mm" extent="0in"/>
        <fo:region-after region-name="after" extent="0in"/>
        <fo:region-start region-name="start" extent="0in"/>
    </fo:simple-page-master>
</fo:layout-master-set>

    <#if security.hasEntityPermission("FACILITY", "_VIEW", session)>
	<#assign orderHeader = delegator.findByPrimaryKey("OrderHeader", Static["org.ofbiz.base.util.UtilMisc"].toMap("orderId",orderId))>
        
	<#assign orderContactMech = orderHeader.getRelated("OrderContactMech")>
	<#list orderContactMech as orderContactMechChildren>
	<#if orderContactMechChildren.contactMechPurposeTypeId == "SHIPPING_LOCATION">
	<#assign PostalAddressContactMechId = orderContactMechChildren.contactMechId>
        <#assign postalAddress = delegator.findByPrimaryKey("PostalAddress", Static["org.ofbiz.base.util.UtilMisc"].toMap("contactMechId",PostalAddressContactMechId))>
        <#assign country = delegator.findByPrimaryKey("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId", postalAddress.countryGeoId))>
	<#elseif orderContactMechChildren.contactMechPurposeTypeId == "PRIMARY_PHONE">
	<#assign phoneContactMechId = orderContactMechChildren.contactMechId>
	<#assign phone = delegator.findByPrimaryKey("TelecomNumber", Static["org.ofbiz.base.util.UtilMisc"].toMap("contactMechId",phoneContactMechId))>
	</#if></#list>
	<#assign shipmentItems = delegator.findByAnd("ShipmentItem", Static["org.ofbiz.base.util.UtilMisc"].toMap("shipmentId", shipmentId))>		
	<#assign shipmentPackageRouteSegs = delegator.findByAnd("ShipmentPackageRouteSeg", Static["org.ofbiz.base.util.UtilMisc"].toMap("shipmentId", shipmentId))>
	<#assign shipmentPackages = delegator.findByAnd("ShipmentPackage", Static["org.ofbiz.base.util.UtilMisc"].toMap("shipmentId", shipmentId))>
	<#list shipmentPackages as shipmentPackage>
	<#assign weight = shipmentPackage.weight>
	</#list>
	<#list shipmentPackageRouteSegs as shipmentPackageRouteSeg>
	<#assign trackingNumber = shipmentPackageRouteSeg.trackingCode>
	</#list>
	<fo:page-sequence master-reference="main">
	<#-- CN23 Customs Declaration Body region -->
        <fo:flow flow-name="body" font-family="Arial">
            <#-- <fo:block><fo:leader/></fo:block>
            <fo:block>firstRow</fo:block>
            <fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block>-->
            <fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block>
            <fo:block><fo:leader/></fo:block>
            <fo:block>WANSE E-COMMERCE co.LTD</fo:block>
	    <fo:block>No 439 Guangyue Rd. Shanghai, China<#--<#if trackingNumber?has_content>&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;${trackingNumber}</#if>--></fo:block>
            <fo:block><fo:leader/></fo:block>
	    <#--<fo:block><fo:leader/></fo:block>-->
	    <#if postalAddress.toName?has_content>
                <fo:block font-size="9pt">${postalAddress.toName}</fo:block>
	    <#else>
		<fo:block font-size="9pt">&#xa0;</fo:block>
            </#if>
	    <fo:block font-size="9pt">${postalAddress.address1}
	    <#if postalAddress.address2?has_content>
                 &#xa0;${postalAddress.address2}
	    <#else>
		&#xa0;
            </#if></fo:block>
            <fo:block font-family="SimHei" font-size="9pt">${postalAddress.postalCode} ${postalAddress.city}, ${country.geoName} (${country.wellKnownText})</fo:block>
	    <fo:block font-size="9pt"><#if (orderContactMech.size() > 3) >
	       Tel: <#if phone.areaCode?has_content>${phone.areaCode} - </#if>${phone.contactNumber}
	    </#if>&#xa0;</fo:block>
	    <#-- TODO -->
	    <#assign productQuantity = 0>
	    <#list shipmentItems as shipmentItem>
		<#assign totalProductQuantity = productQuantity + shipmentItem.quantity>
		<#assign productQuantity = totalProductQuantity>
		<#assign product = delegator.findByPrimaryKey("Product", Static["org.ofbiz.base.util.UtilMisc"].toMap("productId", shipmentItem.productId))>
	    </#list>
	    <fo:table margin-top="3em">
	    <fo:table-column column-width="30mm"/>
            <fo:table-column column-width="13mm"/>
            <fo:table-column column-width="23mm"/>
            <fo:table-column column-width="20mm"/>
            <fo:table-column column-width="34mm"/>
            <fo:table-column column-width="37mm"/>
		<fo:table-header>
		<#--<#list shipmentItems as shipmentItem>
		    <#assign product = delegator.findByPrimaryKey("Product", Static["org.ofbiz.base.util.UtilMisc"].toMap("productId", shipmentItem.productId))>-->
		    <fo:table-row>
			<fo:table-cell><fo:block text-align="center">${product.declaredNameEn}</fo:block>
			</fo:table-cell>
                        <fo:table-cell><fo:block text-align="center">${productQuantity}</fo:block>
                        </fo:table-cell>
			<fo:table-cell><fo:block text-align="center">${weight}</fo:block>
			</fo:table-cell>
			<#if (productQuantity < 2)> 
                        <fo:table-cell><fo:block text-align="center">$6</fo:block>
                        </fo:table-cell>
			<#elseif (productQuantity < 4)>
			<fo:table-cell><fo:block text-align="center">$8</fo:block>
			</fo:table-cell>
			<#else>
			<fo:table-cell><fo:block text-align="center">$10</fo:block>
			</fo:table-cell>
			</#if> 
                        <fo:table-cell><fo:block text-align="center"></fo:block>
                        </fo:table-cell>
                        <fo:table-cell><fo:block text-align="center">China</fo:block>
                        </fo:table-cell>
		    </fo:table-row>
		<#--</#list>-->
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
	<fo:block><fo:leader/></fo:block>
	<fo:block><fo:leader/></fo:block>
	<fo:block><fo:leader/></fo:block>
	<fo:block><fo:leader/></fo:block>
	<fo:block><fo:leader/></fo:block>
	<fo:block margin-left="26em">WANSE</fo:block>	    
        </fo:flow>
	</fo:page-sequence>


    <#else>
        <fo:block font-size="14pt">
            ${uiLabelMap.ProductFacilityViewPermissionError}
        </fo:block>
    </#if>
</fo:root>
</#escape>
