
<div id="wishListingCheck" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Wish Listing Check</b></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="left">
        <form action="<@ofbizUrl>wishListingCheck</@ofbizUrl>" method="post" name="wishListingCheckForm" id="wishListingCheckForm">
            <table>
                <tr>
                    <td>Wish Id</td>
                    <td>
                        <input type="text" name="wishId" size="32" value="${wishId!}"/>
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

<#if result?has_content>
<div id="${result.productStoreId}" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Crawl Result</b></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="left">
        <table class="hover-bar" style="border:1px solid black;">
            <tr class="header-row" style="white-space: nowrap;">
                <td align="left" style="border:1px solid black;"><b>Wish ID: ${result.wishId}</b></td>
            </tr>
            <tr>
                <td align="left" style="border:1px solid black;">
                    <table>
                        <tr>
                            <td align="right">
                                <b>Merchant Name:</b>
                            </td>
                            <td align="left">
                                <b>${result.merchantName}</b>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Merchant Rating Count:
                            </td>
                            <td align="left">
                                ${result.merchantRatingCount}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Merchant Rating:
                            </td>
                            <td align="left">
                                ${result.merchantRating}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Merchant Rating Class:
                            </td>
                            <td align="left">
                                ${result.merchantRatingClass}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                From Trusted Store:
                            </td>
                            <td align="left">
                                ${result.fromTrustedStore}
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td align="left" style="border:1px solid black;">
                    <table>
                        <tr>
                            <td align="right">
                                <b>Product Name:</b>
                            </td>
                            <td align="left">
                                <b>${result.name!}</b>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Product Price:
                            </td>
                            <td align="left">
                                <#if result.minOriginalPrice == result.maxOriginalPrice>$${result.minOriginalPrice}<#else>from $${result.minOriginalPrice} to $${result.maxOriginalPrice}</#if>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Shipping Price:
                            </td>
                            <td align="left">
                                <#if result.minOriginalShipping == result.maxOriginalShipping>$${result.minOriginalShipping}<#else>from $${result.minOriginalShipping} to $${result.maxOriginalShipping}</#if>
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Num Bought:
                            </td>
                            <td align="left">
                                ${result.numBought}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Num Wishes:
                            </td>
                            <td align="left">
                                ${result.numWishes}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Num Entered:
                            </td>
                            <td align="left">
                                ${result.numEntered}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Num Won:
                            </td>
                            <td align="left">
                                ${result.numWon}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Product Rating Count:
                            </td>
                            <td align="left">
                                ${result.productRatingCount}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Product Rating:
                            </td>
                            <td align="left">
                                ${result.productRating}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Product Rating Class:
                            </td>
                            <td align="left">
                                ${result.productRatingClass}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Gender:
                            </td>
                            <td align="left">
                                ${result.gender}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Generation Time:
                            </td>
                            <td align="left">
                                ${result.generationTime}
                            </td>
                        </tr>
                        <tr>
                            <td align="right">
                                Ships From:
                            </td>
                            <td align="left">
                                ${result.shipsFrom!}
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td align="left" style="border:1px solid black;">
                    <table>
                        <tr>
                            <td align="right">Tags : </td>
                            <td align="left">
                                <#list result.tagsList as tags>
                                    <span class="smallSubmit">${tags}</span>
                                </#list>
                            </td>
                        </tr>
                        <tr>
                            <td><br class="clear"/></td>
                        </tr>
                        <tr>
                            <td align="right">Merchant Tags : </td>
                            <td align="left">
                                <#list result.merchantTagsList as merchantTags>
                                    <span class="smallSubmit">${merchantTags}</span>
                                </#list>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td align="left" style="border:1px solid black;">
                    <table>
                        <tr>
                            <td>
                                <table>
                                    <tr>
                                        <td align="right">aspectRatio: </td>
                                        <td align="left">${result.aspectRatio!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">brand: </td>
                                        <td align="left">${result.brand!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">canGift: </td>
                                        <td align="left">${result.canGift!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">canPromo: </td>
                                        <td align="left">${result.canPromo!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">commentCount: </td>
                                        <td align="left">${result.commentCount!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">fbwActive: </td>
                                        <td align="left">${result.fbwActive!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">fbwPending: </td>
                                        <td align="left">${result.fbwPending!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">firstRatingImageIndex: </td>
                                        <td align="left">${result.firstRatingImageIndex!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">hasReward: </td>
                                        <td align="left">${result.hasReward!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">isActive: </td>
                                        <td align="left">${result.isActive!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">isActiveFbwInUs: </td>
                                        <td align="left">${result.isActiveFbwInUs!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">isAdminUser: </td>
                                        <td align="left">${result.isAdminUser!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">isClean: </td>
                                        <td align="left">${result.isClean!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">isConcept: </td>
                                        <td align="left">${result.isConcept!}</td>
                                    </tr>
                                </table>
                            </td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td>
                                <table>
                                    <tr>
                                        <td align="right">isContentReviewer: </td>
                                        <td align="left">${result.isContentReviewer!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">isDeleted: </td>
                                        <td align="left">${result.isDeleted!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">isDirty: </td>
                                        <td align="left">${result.isDirty!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">isExpired: </td>
                                        <td align="left">${result.isExpired!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">isFulfillByWish: </td>
                                        <td align="left">${result.isFulfillByWish!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">isVerified: </td>
                                        <td align="left">${result.isVerified!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">numContestPhotos: </td>
                                        <td align="left">${result.numContestPhotos!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">numExtraPhotos: </td>
                                        <td align="left">${result.numExtraPhotos!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">removed: </td>
                                        <td align="left">${result.removed!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">requiresReview: </td>
                                        <td align="left">${result.requiresReview!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">sourceCountry: </td>
                                        <td align="left">${result.sourceCountry!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">uploadSource: </td>
                                        <td align="left">${result.uploadSource!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">userInActiveSweep: </td>
                                        <td align="left">${result.userInActiveSweep!}</td>
                                    </tr>
                                    <tr>
                                        <td align="right">value: </td>
                                        <td align="left">${result.value!}</td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    Main Image:<br/>
                    <a href="${result.contestPagePicture!}" onclick="window.open(this.href,'','height=480,width=640');return false;"><img height="150" src="${result.contestPagePicture!}"/></a><br /><br />
                </td>
            </tr>
        </table>
    </div>
</div>
</#if>


