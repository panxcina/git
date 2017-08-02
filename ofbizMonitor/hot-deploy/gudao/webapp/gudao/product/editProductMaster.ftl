<#if security.hasEntityPermission("PM", "_UPDATE", session)>
<#if product??>
<div class="screenlet">
<form action="<@ofbizUrl>updateProductMaster</@ofbizUrl>" method="post" name="productMasterForm" id="productMasterForm">
    <table cellspacing="0" class="basic-table">
        <tbody>
            <tr>
                <td class="label">Product ID</td>
                <td>
                    <input type="hidden" name="productId" size="32" value="${product.productId!}"/>
                    <span>${product.productId!}</span>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.motherSku}</td>
                <td>
                    <#if isAdmin || isSbu>
                        <input type="text" name="motherSku" size="32" value="${product.motherSku!}"><display/></input>
                    <#else>
                        <span>${product.motherSku!}</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.productNameCn}</td>
                <td>
                    <#if isAdmin || isSbu>
                        <input type="text" name="chineseName" size="80" value="${product.chineseName!}"><display/></input>
                    <#else>
                        <span>${product.chineseName!}</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.estimatedWeight}</td>
                <td>
                    <#if isAdmin || isSbu>
                        <input type="text" name="weight" size="8"value="${product.weight!}"><display/></input>g
                    <#else>
                        <span>${product.weight!} g</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.actualWeight}</td>
                <td>
                    <#if isAdmin || isSbu>
                        <input type="text" name="actualWeight" size="8" value="${product.actualWeight!}"><display/></input>g
                    <#else>
                        <span>${product.actualWeight!} g</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.productGroup}</td>
                <td>
                    <#if isAdmin>
                        <select name="productGroup">
                            <option value=""></option>
                            <#list productGroupList as productGroup>
                                <option value="${productGroup.enumCode}" <#if product?? && product.productGroup?has_content && product.productGroup = productGroup.enumCode>SELECTED</#if>>${productGroup.enumCode}</option>
                            </#list>
                        </select>
                    <#else>
                        <span>${product.productGroup!}</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.productType}</td>
                <td>
                    <#if isAdmin || isSbu>
                        <select name="productType">
                            <option value=""></option>
                            <#list productTypeList as productType>
                                <option value="${productType.enumCode}" <#if product?? && product.productType?has_content && product.productType = productType.enumCode>SELECTED</#if>>${productType.enumCode}</option>
                            </#list>
                        </select>
                    <#else>
                        <span>${product.productType!}</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.statusId}</td>
                <td>
                    <#if isAdmin || isSbu>
                        <select name="statusId">
                            <option value=""></option>
                            <#list statusList as status>
                                <option value="${status.enumCode}" <#if product?? && product.statusId?has_content && product.statusId = status.enumCode>SELECTED</#if>>${status.enumCode}</option>
                            </#list>
                        </select>
                    <#else>
                        <span>${product.statusId!}</span>
                    </#if>
                </td>
            </tr>
<#--<tr>
                <td class="label">${uiLabelMap.ebayStatusId}</td>
                <td>
                    <#if isAdmin || isEbayPlatform>
                        <select name="ebayStatusId">
                            <option value=""></option>
                            <#list platformStatusList as status>
                                <option value="${status.enumCode}" <#if product?? && ebayStatus?has_content><#if ebayStatus.statusId = status.enumCode>SELECTED</#if></#if>>${status.enumCode}</option>
                            </#list>
                        </select>
                    <#else>
                        <span><#if ebayStatus?has_content>${ebayStatus.statusId!}</#if></span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.smtStatusId}</td>
                <td>
                    <#if isAdmin || isSmtPlatform>
                        <select name="smtStatusId">
                            <option value=""></option>
                            <#list platformStatusList as status>
                                <option value="${status.enumCode}" <#if product?? && smtStatus?has_content><#if smtStatus.statusId = status.enumCode>SELECTED</#if></#if>>${status.enumCode}</option>
                            </#list>
                        </select>
                    <#else>
                        <span><#if smtStatus?has_content>${smtStatus.statusId!}</#if></span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.wishStatusId}</td>
                <td>
                    <#if isAdmin || isWishPlatform>
                        <select name="wishStatusId">
                            <option value=""></option>
                            <#list platformStatusList as status>
                                <option value="${status.enumCode}" <#if product?? && wishStatus?has_content><#if wishStatus.statusId = status.enumCode>SELECTED</#if></#if>>${status.enumCode}</option>
                            </#list>
                        </select>
                    <#else>
                        <span><#if wishStatus?has_content>${wishStatus.statusId!}</#if></span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.amazonStatusId}</td>
                <td>
                    <#if isAdmin || isAmazonPlatform>
                        <select name="amazonStatusId">
                            <option value=""></option>
                            <#list platformStatusList as status>
                                <option value="${status.enumCode}" <#if product?? && amazonStatus?has_content><#if amazonStatus.statusId = status.enumCode>SELECTED</#if></#if>>${status.enumCode}</option>
                            </#list>
                        </select>
                    <#else>
                        <span><#if amazonStatus?has_content>${amazonStatus.statusId!}</#if></span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.lazadaStatusId}</td>
                <td>
                    <#if isAdmin || isLazadaPlatform>
                        <select name="lazadaStatusId">
                            <option value=""></option>
                            <#list platformStatusList as status>
                                <option value="${status.enumCode}" <#if product?? && lazadaStatus?has_content><#if lazadaStatus.statusId = status.enumCode>SELECTED</#if></#if>>${status.enumCode}</option>
                            </#list>
                        </select>
                    <#else>
                        <span><#if lazadaStatus?has_content>${lazadaStatus.statusId!}</#if></span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.cdiscountStatusId}</td>
                <td>
                    <#if isAdmin || isCdiscountPlatform>
                        <select name="cdiscountStatusId">
                            <option value=""></option>
                            <#list platformStatusList as status>
                                <option value="${status.enumCode}" <#if product?? && cdiscountStatus?has_content><#if cdiscountStatus.statusId = status.enumCode>SELECTED</#if></#if>>${status.enumCode}</option>
                            </#list>
                        </select>
                    <#else>
                        <span><#if cdiscountStatus?has_content>${cdiscountStatus.statusId!}</#if></span>
                    </#if>
                </td>
            </tr>-->
            <tr>
                <td class="label">${uiLabelMap.categoryIdParent}</td>
                <td>
                    <#if isAdmin || isSbu>
                        <select name="categoryIdParent">
                            <option value=""></option>
                            <#list parentCategoryList as parentCategory>
                                <option value="${parentCategory.categoryName}" <#if product?? && product.categoryIdParent?has_content && product.categoryIdParent = parentCategory.categoryName>SELECTED</#if>>${parentCategory.categoryName}</option>
                            </#list>
                        </select>
                    <#else>
                        <span>${product.categoryIdParent!}</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.categoryIdChild}</td>
                <td>
                    <#if isAdmin || isSbu>
                        <select name="categoryIdChild">
                            <option value=""></option>
                            <#list childrenCategoryList as childrenCategory>
                                <option value="${childrenCategory.categoryName}" <#if product?? && product.categoryIdChild?has_content && product.categoryIdChild = childrenCategory.categoryName>SELECTED</#if>>${childrenCategory.displayName}</option>
                            </#list>
                        </select>
                    <#else>
                        <span>${product.categoryIdChild!}</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.supplierId}</td>
                <td>
                    <input type="hidden" name="mainSupplier" size="50" value="${product.mainSupplier!}"/>
                    <span>${product.mainSupplier!}</span>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.actualPrice}</td>
                <td>
                    <#if isAdmin>
                        <input type="text" name="actualPrice" size="8" value="${product.actualPrice!}"><display/></input>RMB
                    <#else>
                        <span>${product.actualPrice!} RMB</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.mainSupplierLink}</td>
                <td>
                    <input type="text" name="mainSupplierLink" size="80" value="${product.mainSupplierLink!}"><display/></input>
            </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.eta}</td>
                <td>
                    <input type="text" name="mainSupplierEta" size="8" value="${product.mainSupplierEta!}"><display/></input>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.minOrderDays}</td>
                <td>
                    <input type="text" name="mainMinOrderDays" size="8" value="${product.mainMinOrderDays!}"><display/></input>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.ebayUsPrice}</td>
                <td>
                    <#if isAdmin || isPlatform>
                        <input type="text" name="ebayUsPrice" size="8" value="${ebayUsPrice!}"><display/></input>USD
                    <#else>
                        <span>${ebayUsPrice!} USD</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.listingNotes}</td>
                <td>
                    <textarea cols="50" rows="5" maxlength="5000" name="listingNotes">${product.listingNotes!}</textarea>
                </td>
            </tr>
            <tr>
                <td class="label">${uiLabelMap.purchasingNotes}</td>
                <td>
                    <textarea cols="50" rows="5" maxlength="5000" name="notes">${product.notes!}</textarea>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>
                    <input type="submit" value="Update" class="smallSubmit"/>
                    <a href="javascript:void(0)" onclick="window.close();" class="smallSubmit">Cancel/Close</a>
                </td>
            </tr>
        </tbody>
    </table>
</form>
</div>
<#else>
    Product Not found
</#if>
</#if>