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





<div id="taskDetailCondition2" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Condition 2</b></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="center">
<form name='taskDetailCondition2' method="post" action="<@ofbizUrl>taskDetailCondition2</@ofbizUrl>">
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
</div>
<div class="screenlet-body" align="left">
<h3>Result List - ${count}</h3>
<table class="basic-table hover-bar" cellspacing='10'>
<tr class="header-row" style="white-space: nowrap;">
<td align="center">SKU</td>
<td align="center">Name</td>
<td align="center">Group</td>
<td align="center">Rank</td>
<td align="center">Currency</td>
<td align="center">PM Price</td>
<td align="center">PM Profit Percentage</td>
<td align="center">Rival Total Price</td>
<td align="center">Rival Profit Percentage</td>
<td align="center">Rival Daily Sales</td>
<#--<td align="center">Rival Sold Per Day</td>-->
<td align="center">Rival History Sold</td>
</tr>

<#list productIdList as product>
<tr>
<td align="center">${product.productId}</td>
<td align="center">${product.productName}</td>
<td align="center">${product.productGroup}</td>
<td align="center"><#if product.rank?has_content>${product.rank}</#if></td>
<td align="center">${product.currency}</td>
<td align="center">${product.pmPrice?string["0.##"]}</td>
<td align="center">${product.profitPercentage?string["0.##"]}</td>
<td align="center">${product.rivalTotalPrice?string["0.##"]}</td>
<td align="center">${product.rivalProfitPercentage?string["0.##"]}</td>
<td align="center">${product.rivalDailySales?string["0.##"]}</td>
<#--<td align="center">${product.rivalSoldPerDay}</td>-->
<td align="center">${product.rivalHistorySold}</td>
</tr>
</#list>


</table>




    </div>
</div>