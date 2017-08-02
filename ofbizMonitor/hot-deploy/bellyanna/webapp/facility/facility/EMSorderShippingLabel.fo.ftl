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
<#-- EMS Shipping Label -->
<fo:layout-master-set>
    <fo:simple-page-master master-name="main" page-height="153mm" page-width="216mm"
            margin-top="0" margin-bottom="0" margin-left="0" margin-right="0">
        <fo:region-body region-name="body"/>
        <#--<fo:region-before extent="0in"/>
        <fo:region-after region-name="after" extent="0in"/>
        <fo:region-start region-name="start" extent="0in"/>-->
    </fo:simple-page-master>
</fo:layout-master-set>
    <#list orderContactMechValueMaps as orderContactMechValueMap>
	<#assign orderContactMech = orderContactMechValueMap>
	<#assign contactMech = orderContactMechValueMap.contactMech>
	<#if contactMech.contactMechTypeId == "POSTAL_ADDRESS">
	    <#assign postalAddress = orderContactMechValueMap.postalAddress>
	    <#assign country = delegator.findByPrimaryKey("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoId", postalAddress.countryGeoId))>
	</#if>
	<#if contactMech.contactMechTypeId == "TELECOM_NUMBER">
	    <#assign phone = orderContactMechValueMap.telecomNumber>
	</#if>
    </#list>
        <fo:page-sequence master-reference="main">
	<#-- Barcode region -->
	<#-- EMS Shipping Label Body region -->
        <fo:flow flow-name="body">
		<fo:table margin-left="42mm" table-layout="fixed">
		<fo:table-column column-width="130mm"/>
		<fo:table-header>
                    <fo:table-row height="10mm">
                        <fo:table-cell><fo:block>&#xa0;</fo:block></fo:table-cell>
                    </fo:table-row>
		</fo:table-header>
		<fo:table-body>
                    <fo:table-row height="10mm">
                        <fo:table-cell><fo:block>&#xa0;</fo:block></fo:table-cell>
                    </fo:table-row>
                    <fo:table-row height="10mm">
                        <fo:table-cell>
			    <#if postalAddress.toName?has_content>
				<fo:block>${postalAddress.toName}</fo:block>
			    <#else>
				<fo:block>&#xa0;</fo:block>
			    </#if>
                        </fo:table-cell>
                    </fo:table-row>
		</fo:table-body>
		</fo:table>
                <fo:table margin-left="42mm" table-layout="fixed">
                <fo:table-column column-width="45mm"/>
		<fo:table-column column-width="85mm"/>
                <fo:table-header>
                    <fo:table-row height="10mm">
                        <fo:table-cell><fo:block>&#xa0;</fo:block></fo:table-cell>
			<fo:table-cell><fo:block>${country.geoName}</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-header>
                <fo:table-body>
                    <fo:table-row height="10mm">
                        <fo:table-cell><fo:block>&#xa0;</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
                </fo:table>
                <fo:table margin-left="42mm"  table-layout="fixed">
                <fo:table-column column-width="130mm"/>
                <fo:table-header>
                    <fo:table-row height="5mm">
                        <fo:table-cell><fo:block>${postalAddress.address1}</fo:block></fo:table-cell>
                    </fo:table-row>
                </fo:table-header>
                <fo:table-body>
		    <fo:table-row height="5mm">
			<fo:table-cell>
                            <#if postalAddress.address2?has_content>
                                <fo:block>${postalAddress.address2}</fo:block>
                            <#else>
                                <fo:block>&#xa0;</fo:block>
                            </#if>
			</fo:table-cell>
		    </fo:table-row>
		    <fo:table-row height="5mm">
			<fo:table-cell>
			    <fo:block>${postalAddress.city}</fo:block>
			</fo:table-cell>
		    </fo:table-row>
		    <fo:table-row height="5mm">
			<fo:table-cell>
			    <fo:block>&#xa0;</fo:block>
			</fo:table-cell>
		    </fo:table-row>
                </fo:table-body>
                </fo:table>
                <fo:table margin-left="42mm" table-layout="fixed">
                <fo:table-column column-width="45mm"/>
                <fo:table-column column-width="85mm"/>
                <fo:table-header>
                    <fo:table-row height="10mm">
                        <fo:table-cell><fo:block>${postalAddress.postalCode}</fo:block></fo:table-cell>
                        <fo:table-cell>
			    <#if (orderContactMechValueMaps.size() > 3) >
				<fo:block><#if phone.areaCode?has_content>${phone.areaCode} - </#if>${phone.contactNumber}</fo:block>
			    <#else>
				<fo:block>&#xa0;</fo:block>
			    </#if>
			</fo:table-cell>
                    </fo:table-row>
		</fo:table-header>
		<fo:table-body>
		    <fo:table-row>
			<fo:table-cell><fo:block></fo:block></fo:table-cell>
		    </fo:table-row>
		</fo:table-body>
		</fo:table>
	    
	    <fo:table margin-left="-4mm" margin-top="3mm" table-layout="fixed">
	    <fo:table-column column-width="48mm"/>
            <fo:table-column column-width="10mm"/>
            <fo:table-column column-width="9.5mm"/>
            <fo:table-column column-width="17.8mm"/>
            <fo:table-column column-width="18.5mm"/>
		<fo:table-header>
		    <fo:table-row height="5.5mm">
			<fo:table-cell width="48mm"><fo:block font-size="15pt" text-align="right">x&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;</fo:block>
			</fo:table-cell>
		    </fo:table-row>
		</fo:table-header>
		<fo:table-body>
                    <#--<fo:table-row height="2mm">
                        <fo:table-cell><fo:block text-align="right"></fo:block>
                        </fo:table-cell>
                    </fo:table-row> -->
		    <fo:table-row height="10mm">
                        <fo:table-cell><fo:block font-size="15pt" text-align="right">x&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row height="7mm">
                        <fo:table-cell><fo:block>&#xa0;</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row height="5mm">
                        <fo:table-cell><fo:block>Gift-Dress</fo:block>
                        </fo:table-cell>
                        <fo:table-cell><fo:block>&#xa0;</fo:block>
                        </fo:table-cell>
                        <fo:table-cell><fo:block>&#xa0;</fo:block>
                        </fo:table-cell>
                        <fo:table-cell><fo:block>$5</fo:block>
                        </fo:table-cell>
                        <fo:table-cell><fo:block>China</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
		</fo:table-body>
	    </fo:table>
	    
        </fo:flow>
        </fo:page-sequence>
</fo:root>
</#escape>
