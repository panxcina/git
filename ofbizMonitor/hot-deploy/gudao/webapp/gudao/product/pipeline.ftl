<style type="text/css">
table tr td   {
font-size:12px;
text-align:center;
border: 1px solid #333333;
border-collapse:collapse;
padding: 0px 0px 0px 0px;
line-height: 2.9em;
margin-left:  0.0em;
padding: 0.4em 0.6em 0.4em 0.6em;
font-weight: bold;
}

</style>

<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Pipeline Header</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="center">
        <table cellpadding="0" cellspacing="0">
            <tr>
                <#if pipelineId??>
                    <td>Action</td>
                </#if>
                <td>夫妻档</td>
                <td>负责人</td>
                <td>开发</td>
                <td>销售</td>
                <td>平台</td>
                <td>特色</td>
                <td>目标毛利</td>
                <td>预估上新</td>
            </tr>
            <#list pipelineHeaderList as pipelineHeader>
                <tr>
                    <#if pipelineId??>
                        <td>
                            <form action="<@ofbizUrl>refreshPipeline</@ofbizUrl>" method="post" name="refreshPipeline">
                                <input type="hidden" name="pipelineId" value="${pipelineHeader.pipelineId}"/>
                                <input type="submit" value="Refresh" class="smallSubmit"/>
                            </form>
                        </td>
                    </#if>
                    <td>
                        <form action="<@ofbizUrl>pipeline</@ofbizUrl>" method="get" name="pipelineHeader">
                            <input type="hidden" name="pipelineId" value="${pipelineHeader.pipelineId}"/>
                            <input type="submit" value="${pipelineHeader.pipelineId}" class="smallSubmit"/>
                        </form>
                    </td>
                    <td>${pipelineHeader.personInCharge?if_exists}</td>
                    <td>${pipelineHeader.sourcer?if_exists}</td>
                    <td>${pipelineHeader.salesman?if_exists}</td>
                    <td>${pipelineHeader.platform?if_exists}</td>
                    <td>${pipelineHeader.notes?if_exists}</td>
                    <td>${pipelineHeader.targetProfit?if_exists}</td>
                    <td>${pipelineHeader.estimatedNew?if_exists}</td>
                </tr>
            </#list>
        </table>
        <br class="clear"/>
        <form action="<@ofbizUrl>pipeline</@ofbizUrl>" method="post" name="pipelineShowAll">
            <input type="submit" value="Show All 夫妻档" class="smallSubmit"/>
        </form>
    </div>
</div>

<#if pipelineId??>
    <div class="screenlet">
        <div class="screenlet-title-bar">
            <ul>
                <li class="h3">Pipeline Item</li>
            </ul>
            <br class="clear"/>
        </div>


        <div class="screenlet-body" align="center">
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td>周次</td>
                    <td>sourced</td>
                    <td>PicReady</td>
                    <td>PicDone</td>
                    <td>CopyDone</td>
                    <td>Listed</td>
                    <td>Active</td>
<#--<td>G0数</td>
                    <td>G0库存</td>
                    <td>G4数</td>
                    <td>销量</td>
                    <td>毛利</td>
<td>毛利率</td>-->
                </tr>
                <#list pipelineItemList as pipelineItem>
                    <tr>
                        <td>${pipelineItem.weekNumber} (${pipelineItem.startDate?string["MMM.d"]} - ${pipelineItem.endDate?string["MMM.d"]})</td>
                        <td>${pipelineItem.productSourced?if_exists}</td>
                        <td>${pipelineItem.pictureReady?if_exists}</td>
                        <td>${pipelineItem.pictureDone?if_exists}</td>
                        <td>${pipelineItem.copyDone?if_exists}</td>
                        <td>${pipelineItem.listed?if_exists}</td>
                        <td>${pipelineItem.active?if_exists}</td>
<#--<td>${pipelineItem.group0Qty?if_exists}</td>
                        <td>${pipelineItem.group0Stock?if_exists}</td>
                        <td>${pipelineItem.group4Qty?if_exists}</td>
                        <td>${pipelineItem.sales?if_exists}</td>
                        <td>${pipelineItem.grossProfit?if_exists}</td>
<td>${pipelineItem.grossProfitPct?if_exists}</td>-->
                    </tr>
                </#list>
            </table>
        </div>
    </div>
</#if>