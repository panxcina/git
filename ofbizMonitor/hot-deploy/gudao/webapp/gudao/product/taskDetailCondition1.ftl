<#-- task View -->

<style type="text/css">
A.bigbutton,.bigbuttondisabled {
border-bottom: #999999 solid 0.1em;
border-top: #f7e893 solid 0.1em;
border-left: #f7e893 solid 0.1em;
border-right: #999999 solid 0.1em;
background-image: url(../images/button_whitegray.jpg);
background-repeat:repeat-x;
font-weight: bold;
font-size: 20px;
line-height: 2.9em;
margin-left:  0.0em;
padding: 0.4em 0.6em 0.4em 0.6em;
/*white-space: nowrap;*/
}

</style>

<div id="taskDetailCondition1" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
<li class="h3"><b>G1 products to be checked for eBay listings &nbsp; - &nbsp; Condition: G1 + eBayCheck is FALSE</b></li>
        </ul>
        <br class="clear"/>
    </div>
<#--<div class="screenlet-body" align="center">
<form name='taskDetailCondition1' method="post" action="<@ofbizUrl>taskDetailCondition1</@ofbizUrl>">
<table class="basic-table" cellspacing='0'>
    <tr>
        <td align='right' width='5%' class='label'>SBU</td>
        <td align='left'>
<select id="ownerGroup" name="ownerGroup">
<option value="DADA">Dada</option>
<option value="FANG">Fang</option>
<option value="FANGXI">FANGXI</option>
<option value="G5">G5</option>
<option value="G6">G6</option>
<option value="GARY">Gary</option>
<option value="JERRY">Jerry</option>
<option value="MATTY">Matty</option>
<option value="NICK">NICK</option>
<option value="NIKI">Niki</option>
</select></td>
    </tr>
    <tr>
        <td align='right' width='5%'>&nbsp;</td>
        <td align='left'>
            <input type='submit' value='SUBMIT'/>
        </td>
    </tr>
</table>
</form>
</div>-->
<div class="screenlet-body" align="left">
<h3>Result List - ${count} / ${countAll}</h3>
<table class="basic-table hover-bar" cellspacing='10'>
<tr class="header-row" style="white-space: nowrap;">
<td align="center">SKU</td>
<td align="center">品名</td>
<td align="center">成本</td>
<td align="center">重量</td>
<td align="center">分组</td>
<td align="center">状态</td>
<td align="center">产品目录</td>
<td align="center">产品性质</td>
<td align="center">Platform</td>
<td align="center">开发员</td>
<td align="center">供应商</td>
<td align="center">USD</td>
<td align="center">PROFIT%</td>
<td align="center">eBay母版复查</td>
<td align="center">UPC</td>
<td align="center">7天日销</td>
<td align="center">30天日销</td>
<td align="center">7天日利</td>
<td align="center">30天日利</td>

</tr>
<#list (productIdList?sort_by("lastWeekDailyProfit"))?reverse as product>
<tr>
<td align="center">${product.productId}</td>
<td align="center">${product.chineseName}</td>
<td align="center">${product.price?string["0.##"]}</td>
<td align="center">${product.weight?string["0.##"]}</td>
<td align="center">${product.productGroup}</td>
<td align="center">${product.statusId}</td>
<td align="center"><#if product.categoryId?has_content>${product.categoryId}</#if></td>
<td align="center">${product.productType}</td>
<td align="center"><#if product.platform?has_content>${product.platform}</#if></td>
<td align="center">${product.ownerGroup}</td>
<td align="center"><#if product.supplier?has_content>${product.supplier}</#if></td>
<td align="center">${product.priceUsd?string["0.##"]}</td>
<td align="center">${product.cpPercentage?string["0.##"]}</td>
<td align="center"><#if product.listingCheck?has_content>${product.listingCheck}</#if></td>
<td align="center"><#if product.upc?has_content>${product.upc}</#if></td>
<td align="center"><#if product.lastWeekSales?has_content>${product.lastWeekSales?string["0.##"]}</#if></td>
<td align="center"><#if product.lastMonthSales?has_content>${product.lastMonthSales?string["0.##"]}</#if></td>
<td align="center"><#if product.lastWeekDailyProfit?has_content>${product.lastWeekDailyProfit?string["0.##"]}</#if></td>
<td align="center"><#if product.lastMonthDailyProfit?has_content>${product.lastMonthDailyProfit?string["0.##"]}</#if></td>
</tr>
</#list>


</table>




    </div>
</div>