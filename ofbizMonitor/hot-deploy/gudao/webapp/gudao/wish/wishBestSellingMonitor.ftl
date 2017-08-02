
<div id="wishBestSellingMonitor" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Wish Best Selling Monitor</b></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="left">
        <form action="<@ofbizUrl>bestSellingMonitor</@ofbizUrl>" method="post" name="bestSellingMonitorForm" id="bestSellingMonitorForm">
            <table>
                <tr>
                    <td>Account Name</td>
                    <td>
                        <select name="productStoreIdInput">
                            <option value="">Choose Account</option>
                            <option value="ALL" <#if productStoreIdInput = "ALL">SELECTED</#if>>All</option>
                            <option value=""></option>
                            <#list productStoreList?sort_by("productStoreId") as productStoreGv>
                                <option value="${productStoreGv.productStoreId}" <#if productStoreIdInput = productStoreGv.productStoreId>SELECTED</#if>>${productStoreGv.productStoreId?capitalize}</option>
                            </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Monitor Days</td>
                    <td>
                        <input type="text" name="totalMonitorDay" size="5" value="${totalMonitorDay}"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <input type="submit" value="Submit" class="smallSubmit"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>

</div>
<br class="clear"/>

<#list resultList as result>
    <div id="${result.productStoreId}" class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3"><b>${result.productStoreId}</b></li>
                <li>
                    <form name="bestSellingMonitorMgr" method="post" action="<@ofbizUrl>bestSellingMonitorListMgr</@ofbizUrl>" target="_blank">
                        <input type="hidden" name="productStoreId" value="${result.productStoreId}"/>
                        <input type="submit" value="Manage List" class="smallSubmit"/>
                    </form>
                </li>
            </ul>
            <br class="clear"/>
        </div>
        <div class="screenlet-body" align="left">
            <#list result.bestSellingList as wishList>
                <table class="hover-bar" style="border:1px solid black;">
                    <tr class="header-row" style="white-space: nowrap;">
                        <td align="left" style="border:1px solid black;"><b>Wish ID: ${wishList.wishId}</b></td>
                        <td align="right" style="border:1px solid black;"><b>Last Updated Time: ${wishList.lastUpdatedStamp!}</b></td>
                    </tr>
                    <tr>
                        <td align="left" colspan=2>
                            <table>
                                <tr>
                                    <td align="right">Merchant Rating: </td>
                                    <td align="left">${wishList.merchantRating!}</td>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                    <td align="right">Product Rating: </td>
                                    <td align="left">${wishList.productRating!}</td>
                                </tr>
                                <tr>
                                    <td align="right">Merchant Rating Count: </td>
                                    <td align="left">${wishList.merchantRatingCount!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right">Product Rating Count: </td>
                                    <td align="left">${wishList.productRatingCount!}</td>
                                </tr>
                                <tr>
                                    <td align="right">Merchant Rating Class: </td>
                                    <td align="left">${wishList.merchantRatingClass!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right">Product Rating Class: </td>
                                    <td align="left">${wishList.productRatingClass!}</td>
                                </tr>
                                <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
                                <tr>
                                    <td align="right">Date Uploaded: </td>
                                    <td align="left">${wishList.dateUploaded!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right"></td>
                                    <td align="left"></td>
                                </tr>
                                <tr>
                                    <td align="right">Parent SKU: </td>
                                    <td align="left">${wishList.parentSku!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right"></td>
                                    <td align="left"></td>
                                </tr>
                                <tr>
                                    <td align="right">Name: </td>
                                    <td align="left">${wishList.name!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right">activeSweep: </td>
                                    <td align="left">${wishList.activeSweep!}</td>
                                </tr>
                                <tr>
                                    <td align="right">Original Price: </td>
                                    <td align="left"><#if wishList.minOriginalPrice == wishList.maxOriginalPrice>$${wishList.minOriginalPrice}<#else>from $${wishList.minOriginalPrice} to $${wishList.maxOriginalPrice}</#if></td>
                                    <td>&nbsp;</td>
                                    <td align="right">aspectRatio: </td>
                                    <td align="left">${wishList.aspectRatio!}</td>
                                </tr>
                                <tr>
                                    <td align="right">Original Shipping: </td>
                                    <td align="left"><#if wishList.minOriginalShipping == wishList.maxOriginalShipping>$${wishList.minOriginalShipping}<#else>from $${wishList.minOriginalShipping} to $${wishList.maxOriginalShipping}</#if></td>
                                    <td>&nbsp;</td>
                                    <td align="right">canGift: </td>
                                    <td align="left">${wishList.canGift!}</td>
                                </tr>
                                <tr>
                                    <td align="right">Review: </td>
                                    <td align="left">${wishList.reviewStatus!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right">canPromo: </td>
                                    <td align="left">${wishList.canPromo!}</td>
                                </tr>
                                <tr>
                                    <td align="right">UPC: </td>
                                    <td align="left">${wishList.upc!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right">fromTrustedStore: </td>
                                    <td align="left">${wishList.fromTrustedStore!}</td>
                                </tr>
                                <tr>
                                    <td align="right">numBought (Crawl): </td>
                                    <td align="left">${wishList.numBought!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right">hasReward: </td>
                                    <td align="left">${wishList.hasReward!}</td>
                                </tr>
                                <tr>
                                    <td align="right">numSold (API): </td>
                                    <td align="left">${wishList.numSold!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right">isClean: </td>
                                    <td align="left">${wishList.isClean!}</td>
                                </tr>
                                <tr>
                                    <td align="right">numSaves: </td>
                                    <td align="left">${wishList.numSaves!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right">isDirty: </td>
                                    <td align="left">${wishList.isDirty!}</td>
                                </tr>
                                <tr>
                                    <td align="right">gender: </td>
                                    <td align="left">${wishList.gender!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right">isFulfillByWish: </td>
                                    <td align="left">${wishList.isFulfillByWish!}</td>
                                </tr>
                                <tr>
                                    <td align="right">shipsFrom: </td>
                                    <td align="left">${wishList.shipsFrom!}</td>
                                    <td>&nbsp;</td>
                                    <td align="right">isVerified: </td>
                                    <td align="left">${wishList.isVerified!}</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan=2>
                            <table>
                                <tr>
                                    <td align="right">Tags (${wishList.tagsCount}): </td>
                                    <td align="left">
                                        <#list wishList.tagsList as tags>
                                            <span class="smallSubmit">${tags.tagName}</span>
                                        </#list>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td align="left" colspan=2>
                            <table class="hover_bar">
                                <tr>
                                    <#list wishList.sellingDataList?sort_by("date") as sellingDataList>
                                        <td align="center" style="border:1px solid black;">
                                            <table class="hover-bar" style="border:1px solid black;">
                                                <tr class="header-row" style="white-space: nowrap;">
                                                    <td align="center" style="border:1px solid black;"><b>${sellingDataList.date}</b></td>
                                                </tr>
                                                <tr style="white-space: nowrap;">
                                                    <td align="center" style="border:1px solid black;">
                                                        <table>
                                                            <tr style="white-space: nowrap;">
                                                                <td align="center">Promoted: </td><td align="left">
                                                                    <b><font color=<#if sellingDataList.headerData.isPromoted == "TRUE">green<#else>red</#if>>${sellingDataList.headerData.isPromoted}</font></b>
                                                                </td>
                                                            </tr>
                                                            <tr><td align="center">Total Saves: </td><td align="left">${sellingDataList.headerData.numberSaves}</td></tr>
                                                            <tr><td align="center">Total Sold: </td><td align="left">${sellingDataList.headerData.numberSold}</td></tr>
                                                            <tr><td align="center">Conversion Rate: </td><td align="left">${sellingDataList.headerData.conversionRate?string["0.00%"]}</td></tr>
                                                            <tr><td>&nbsp;</td></tr>
                                                            <tr><td align="center">Daily Save: </td><td align="left">${sellingDataList.headerData.dailySaves}</td></tr>
                                                            <tr><td align="center">Daily Sold: </td><td align="left">${sellingDataList.headerData.dailySold}</td></tr>
                                                            <tr><td align="center">Daily Conversion Rate: </td><td align="left">${sellingDataList.headerData.dailyConversionRate?string["0.00%"]}</td></tr>
                                                            <tr><td>&nbsp;</td></tr>
                                                            <tr><td align="right">productRating: </td><td align="left">${sellingDataList.headerData.productRating?string["0.#####"]!}</td></tr>
                                                            <tr><td align="right">productRatingCount: </td><td align="left">${sellingDataList.headerData.productRatingCount!}</td></tr>
                                                            <tr><td align="right">productRatingClass: </td><td align="left">${sellingDataList.headerData.productRatingClass!}</td></tr>
                                                            <tr><td>&nbsp;</td></tr>
                                                            <tr>
                                                                <td colspan=2>
                                                                    <div id="${wishList.wishId}_${sellingDataList.date?string["ddMM"]}_dynamicDetail">
                                                                        <a class="linkCustom" onclick="javascript:toggleScreenlet('${wishList.wishId}_${sellingDataList.date?string["ddMM"]}_dynamicDetail', '${wishList.wishId}_${sellingDataList.date?string["ddMM"]}_dynamicDetailHidden', 'true', 'Expand', 'Collapse');" title="Detail">Detail</a>
                                                                        <div id="${wishList.wishId}_${sellingDataList.date?string["ddMM"]}_dynamicDetailHidden" style="display: none;">
                                                                            <table>
                                                                                <tr>
                                                                                    <td align="right">merchantRating: </td>
                                                                                    <td align="left">${sellingDataList.headerData.merchantRating!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">merchantRatingCount: </td>
                                                                                    <td align="left">${sellingDataList.headerData.merchantRatingCount!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">merchantRatingClass: </td>
                                                                                    <td align="left">${sellingDataList.headerData.merchantRatingClass!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>&nbsp;</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">activeSweep: </td>
                                                                                    <td align="left">${sellingDataList.headerData.activeSweep!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">aspectRatio: </td>
                                                                                    <td align="left">${sellingDataList.headerData.aspectRatio!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">brand: </td>
                                                                                    <td align="left">${sellingDataList.headerData.brand!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">canGift: </td>
                                                                                    <td align="left">${sellingDataList.headerData.canGift!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">canPromo: </td>
                                                                                    <td align="left">${sellingDataList.headerData.canPromo!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">commentCount: </td>
                                                                                    <td align="left">${sellingDataList.headerData.commentCount!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">fbwActive: </td>
                                                                                    <td align="left">${sellingDataList.headerData.fbwActive!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">fbwPending: </td>
                                                                                    <td align="left">${sellingDataList.headerData.fbwPending!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">firstRatingImageIndex: </td>
                                                                                    <td align="left">${sellingDataList.headerData.firstRatingImageIndex!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">fromTrustedStore: </td>
                                                                                    <td align="left">${sellingDataList.headerData.fromTrustedStore!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">gender: </td>
                                                                                    <td align="left">${sellingDataList.headerData.gender!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">hasReward: </td>
                                                                                    <td align="left">${sellingDataList.headerData.hasReward!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isActive: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isActive!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isActiveFbwInUs: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isActiveFbwInUs!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isAdminUser: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isAdminUser!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isClean: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isClean!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isConcept: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isConcept!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isContentReviewer: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isContentReviewer!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isDeleted: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isDeleted!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isDirty: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isDirty!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isExpired: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isExpired!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isFulfillByWish: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isFulfillByWish!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">isVerified: </td>
                                                                                    <td align="left">${sellingDataList.headerData.isVerified!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">numBought: </td>
                                                                                    <td align="left">${sellingDataList.headerData.numBought!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">numEntered: </td>
                                                                                    <td align="left">${sellingDataList.headerData.numEntered!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">numWishes: </td>
                                                                                    <td align="left">${sellingDataList.headerData.numWishes!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">numWon: </td>
                                                                                    <td align="left">${sellingDataList.headerData.numWon!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">numContestPhotos: </td>
                                                                                    <td align="left">${sellingDataList.headerData.numContestPhotos!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">numExtraPhotos: </td>
                                                                                    <td align="left">${sellingDataList.headerData.numExtraPhotos!}</td>
                                                                                </tr>

                                                                                <tr>
                                                                                    <td align="right">removed: </td>
                                                                                    <td align="left">${sellingDataList.headerData.removed!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">requiresReview: </td>
                                                                                    <td align="left">${sellingDataList.headerData.requiresReview!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">shipsFrom: </td>
                                                                                    <td align="left">${sellingDataList.headerData.shipsFrom!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">sourceCountry: </td>
                                                                                    <td align="left">${sellingDataList.headerData.sourceCountry!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">uploadSource: </td>
                                                                                    <td align="left">${sellingDataList.headerData.uploadSource!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">userInActiveSweep: </td>
                                                                                    <td align="left">${sellingDataList.headerData.userInActiveSweep!}</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right">value: </td>
                                                                                    <td align="left">${sellingDataList.headerData.value!}</td>
                                                                                </tr>
                                                                            </table>
                                                                        </div>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </#list>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td colspan=2>
                            <div id="${wishList.wishId}_dynamicImage">
                                <a class="linkCustom" onclick="javascript:toggleScreenlet('${wishList.wishId}_dynamicImage', '${wishList.wishId}_dynamicImageHidden', 'true', 'Expand', 'Collapse');" title="Images">Images</a>
                                <div id="${wishList.wishId}_dynamicImageHidden" style="display: none;">
                                    Main Image:<br/>
                                    <a href="${wishList.mainImage!}" onclick="window.open(this.href,'','height=480,width=640');return false;"><img height="150" src="${wishList.mainImage!}"/></a><br /><br />
                                    Extra Images:<br>
                                    <#list wishList.extraImages as extraImage>
                                        <a href="${extraImage!}" onclick="window.open(this.href,'','height=480,width=640');return false;"><img height="150" src="${extraImage!}"/></a>&nbsp;
                                    </#list>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
                <br class="clear"/>
            </#list>
        </div>
    </div>
    <br class="clear"/>
</#list>

