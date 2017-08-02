<SCRIPT language="javascript">
//function getSkuSeq(formSkuSeq) {
//var skuSeqElement = document.getElementById(skuSeqForm);
//skuSeq = formSkuSeq.name;
//return skuSeq;
//}


this.seq = ${skuSeq} + 1;
function addMoreProductId() {

//Create an input type dynamically.
var element = document.createElement("input");

//Assign different attributes to the element.
element.setAttribute("type", "text");
element.setAttribute("size", "30");
element.setAttribute("name", "productId[" + seq + "]");
element.setAttribute("id", "productId[" + seq + "]");
element.setAttribute("value", "SKU " + seq);
element.setAttribute("onClick", "this.select();");

var elementQty = document.createElement("input");

//Assign different attributes to the element.
elementQty.setAttribute("type", "text");
elementQty.setAttribute("size", "5");
elementQty.setAttribute("name", "qty[" + seq + "]");
elementQty.setAttribute("value", "1");
elementQty.setAttribute("onClick", "this.select();");

var elementRefund = document.createElement("input");
elementRefund.setAttribute("type", "currency");
elementRefund.setAttribute("size", "6");
elementRefund.setAttribute("name", "refundAmount[" + seq + "]");

var elementReplacementProduct = document.createElement("input");
elementReplacementProduct.setAttribute("type", "currency");
elementReplacementProduct.setAttribute("size", "6");
elementReplacementProduct.setAttribute("name", "replacementProductCost[" + seq + "]");

var elementReplacementShip = document.createElement("input");
elementReplacementShip.setAttribute("type", "currency");
elementReplacementShip.setAttribute("size", "6");
elementReplacementShip.setAttribute("name", "replacementShippingCost[" + seq + "]");


var secondTd = document.getElementById("secondTd");

//Append the element in page (in span).
secondTd.appendChild(element);
secondTd.innerHTML += "&nbsp;<b>Qty</b>&nbsp;";
secondTd.appendChild(elementQty);
secondTd.innerHTML += "<br />&nbsp;<b>Refund " + seq + "</b>&nbsp;";
secondTd.appendChild(elementRefund);
secondTd.innerHTML += "&nbsp;&nbsp;<b>Replacement Product " + seq + "</b>&nbsp;";
secondTd.appendChild(elementReplacementProduct);
secondTd.innerHTML += "&nbsp;&nbsp;<b>Replacement Ship " + seq + "</b>&nbsp;";
secondTd.appendChild(elementReplacementShip);
secondTd.appendChild(document.createElement("br"));



seq++;


}
</SCRIPT>



<form action="<@ofbizUrl><#if productGqsDetail??>updateProductGqsDetail<#else>createProductGqsDetail</#if></@ofbizUrl>" method="post" name="gqsForm" id="gqsForm" onsubmit="return checkscript()">

<table cellspacing="0">
<tr><td>

<table cellspacing="0" class="basic-table">
<tbody>


<tr>
<td class="label">Platform</td>
<td>
<select name="platform">
<option value="EBAY" <#if productGqsDetail?? && productGqsDetail.platform?has_content && productGqsDetail.platform = "EBAY">SELECTED</#if>>eBay</option>
<option value="SMT" <#if productGqsDetail?? && productGqsDetail.platform?has_content && productGqsDetail.platform = "SMT">SELECTED</#if>>SMT</option>
<option value="WISH" <#if productGqsDetail?? && productGqsDetail.platform?has_content && productGqsDetail.platform = "WISH">SELECTED</#if>>Wish</option>
<option value="AMAZON" <#if productGqsDetail?? && productGqsDetail.platform?has_content && productGqsDetail.platform = "AMAZON">SELECTED</#if>>Amazon</option>
</select>
</td>
</tr>

<tr>
<td class="label">Channel</td>
<td>
<select name="channel">
<option value="CASE" <#if productGqsDetail?? && productGqsDetail.channel?has_content && productGqsDetail.channel = "CASE">SELECTED</#if>>Case</option>
<option value="FEEDBACK" <#if productGqsDetail?? && productGqsDetail.channel?has_content && productGqsDetail.channel = "FEEDBACK">SELECTED</#if>>Feedback</option>
<option value="MESSAGE" <#if productGqsDetail?? && productGqsDetail.channel?has_content && productGqsDetail.channel = "MESSAGE">SELECTED</#if>>Message</option>
<option value="RETURN_REQUEST" <#if productGqsDetail?? && productGqsDetail.channel?has_content && productGqsDetail.channel = "RETURN_REQUEST">SELECTED</#if>>Return Request</option>
</select>
</td>
</tr>

<tr>
<td class="label" >Type</td>
<td>
<select name="type">
<option value=""></option>
<#assign gqsTypeList = delegator.findByAnd("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumTypeId" , "GQS_TYPE"), null, true)>
<#list gqsTypeList?sort_by("description") as gqsType>
<option value=${gqsType.enumId} <#if productGqsDetail?? && productGqsDetail.type?has_content && productGqsDetail.type = gqsType.enumId>SELECTED</#if>>${gqsType.description}</option>
</#list>
</select>
</td>
</tr>



<tr>
<td class="label">Account Name</td>
<td>
<input type="text" name="account" size="40" <#if productGqsDetail?? && productGqsDetail.account?has_content>value="${productGqsDetail.account!}"</#if>/>
</td>
</tr>

<tr>
<td class="label">Order ID</td>
<td>
<input type="text" name="orderId" size="40" <#if productGqsDetail?? && productGqsDetail.orderId?has_content>value="${productGqsDetail.orderId!}" readonly</#if>/>
</td>
</tr>

<tr>
<td class="label">Customer ID</td>
<td>
<input type="text" name="customerId" size="40" <#if productGqsDetail?? && productGqsDetail.customerId?has_content>value="${productGqsDetail.customerId!}"</#if>/>
</td>
</tr>

<#--<tr>
<td class="label">Owner Group</td>
<td>
<select name="ownerGroup">
<option value=""></option>
<#assign ownerGroupList = delegator.findByAnd("OwnerGroup", Static["org.ofbiz.base.util.UtilMisc"].toMap("statusId" , "ACTIVE"), null, true)>
<#list ownerGroupList?sort_by("ownerGroupName") as ownerGrp>
<option value=${ownerGrp.ownerGroupId} <#if productGqsDetail?? && productGqsDetail.ownerGroup?has_content && productGqsDetail.ownerGroup = ownerGrp.ownerGroupId>SELECTED</#if>>${ownerGrp.ownerGroupName}</option>
</#list>
</select>
</td>
</tr>-->

<tr>
<td class="label">Description</td>
<td>
<textarea cols="60" rows="5" maxlength="5000" name="description" id="description" <#if productGqsDetail?? && productGqsDetail.description?has_content>>${productGqsDetail.description!}</textarea><#else>></textarea></#if>
</td>
</tr>

<tr>
<td class="label">Solution</td>
<td>
<INPUT TYPE="checkbox" NAME="fullRefund" VALUE="Y" <#if productGqsDetail?? && productGqsDetail.fullRefund?has_content && productGqsDetail.fullRefund == 'Y'>checked</#if>>Full Refund<BR>
<INPUT TYPE="checkbox" NAME="partialRefund" VALUE="Y" <#if productGqsDetail?? && productGqsDetail.partialRefund?has_content && productGqsDetail.partialRefund == 'Y'>checked</#if>>Partial Refund<BR>
<INPUT TYPE="checkbox" NAME="replacement" VALUE="Y" <#if productGqsDetail?? && productGqsDetail.replacement?has_content && productGqsDetail.replacement == 'Y'>checked</#if>>Replacement<BR>
<INPUT TYPE="checkbox" NAME="noAction" VALUE="Y" <#if productGqsDetail?? && productGqsDetail.noAction?has_content && productGqsDetail.noAction == 'Y'>checked</#if>>No Action Needed<BR>
</td>
</tr>

<tr>
<td class="label">Refund Currency</td>
<td>
<select name="currencyId">
<option value="USD" <#if productGqsDetail?? && productGqsDetail.currency?has_content && productGqsDetail.currency == "USD">SELECTED</#if>>USD</option>
<option value="AUD" <#if productGqsDetail?? && productGqsDetail.currency?has_content && productGqsDetail.currency == "AUD">SELECTED</#if>>AUD</option>
<option value="EUR" <#if productGqsDetail?? && productGqsDetail.currency?has_content && productGqsDetail.currency == "EUR">SELECTED</#if>>EUR</option>
<option value="GBP" <#if productGqsDetail?? && productGqsDetail.currency?has_content && productGqsDetail.currency == "GBP">SELECTED</#if>>GBP</option>
<option value="CAD" <#if productGqsDetail?? && productGqsDetail.currency?has_content && productGqsDetail.currency == "CAD">SELECTED</#if>>CAD</option>
<option value="RMB" <#if productGqsDetail?? && productGqsDetail.currency?has_content && productGqsDetail.currency == "RMB">SELECTED</#if>>RMB</option>
</select>
</td>
</tr>


<#--
<tr>
<td class="label">Refund Amount</td>
<td>
<input type="currency" name="refundAmount" size="10" <#if productGqsDetail?? && productGqsDetail.refundAmount?has_content>value="${productGqsDetail.refundAmount!}"</#if>/>
</td>
</tr>

<tr>
<td class="label">Replacement Product Cost (RMB)</td>
<td>
<input type="currency" name="replacementProductCost" size="10" <#if productGqsDetail?? && productGqsDetail.replacementProductCost?has_content>value="${productGqsDetail.replacementProductCost!}"</#if>/>
</td>
</tr>

<tr>
<td class="label">Replacement Shipping Cost (RMB)</td>
<td>
<input type="currency" name="replacementShippingCost" size="10" <#if productGqsDetail?? && productGqsDetail.replacementShippingCost?has_content>value="${productGqsDetail.replacementShippingCost!}"</#if>/>
</td>
</tr>-->

<tr>
<td class="label">Shipping Method</td>
<td>
<select name="shipType">
<option value=""></option>
<#assign shipTypeList = delegator.findByAnd("Enumeration", Static["org.ofbiz.base.util.UtilMisc"].toMap("enumTypeId" , "GUDAO_SHIP_TYPE"), null, true)>
<#list shipTypeList?sort_by("sequenceId") as shipType>
<option value=${shipType.enumId} <#if productGqsDetail?? && productGqsDetail.shipType?has_content && productGqsDetail.shipType = shipType.enumId>SELECTED</#if>>${shipType.description}</option>
</#list>
</select>
</td>
</tr>

<tr>
<td class="label">Country Destination</td>
<td>
<select name="countryDestination">
<option value=""></option>
<#assign countryDestinationList = delegator.findByAnd("Geo", Static["org.ofbiz.base.util.UtilMisc"].toMap("geoTypeId" , "COUNTRY"), null, true)>
<#list countryDestinationList?sort_by("geoName") as countryDest>
<option value=${countryDest.geoId} <#if productGqsDetail?? && productGqsDetail.countryDestination?has_content && productGqsDetail.countryDestination == countryDest.geoId>SELECTED</#if>>${countryDest.geoName} (${countryDest.geoId})<#if countryDest.wellKnownText?has_content> - ${countryDest.wellKnownText}</#if></option>
</#list>
</select>
</td>
</tr>


<#if productGqsDetail?? && productGqsDetailProduct??>

<tr>
<td class="label">SKU</td>
<td>
<#assign skuCount = 1>
<#list productGqsDetailProduct?sort_by("seqId") as skuList>
<input type="text" name="productId[${skuCount}]" size="30" value="${skuList.productId}" onClick="this.select();"/>
<b>Qty</b>
<input type="text" name="qty[${skuCount}]" size="5" value="${skuList.quantity}" onClick="this.select();"/><br />
<#assign skuCount = skuCount + 1>
</#list>
</td>
</tr>


<#if action=="VIEW">
<#else>
<tr><td></td>
<td><span id="secondTd"></span></td>
</tr>

<tr><td></td>
<td><input type="button" value="Add More SKU" onclick="addMoreProductId()"/></td>
</tr>
</#if>







<#else>
<tr>
<td class="label">SKU</td>
<td>
<input type="text" name="productId[1]" size="30" value="SKU 1" onClick="this.select();"/>
 <b>Qty</b>
<input type="text" name="qty[1]" size="5" value="1" onClick="this.select();"/>
<br><b>Refund</b>
<input type="currency" name="refundAmount[1]" size="6"/>
&nbsp;&nbsp;<b>Replacement Product(rmb)</b>
<input type="currency" name="replacementProductCost[1]" size="6"/>
&nbsp;&nbsp;<b>Replacement Ship(rmb)</b>
<input type="currency" name="replacementShippingCost[1]" size="6"/>
</td>

</tr>
<tr><td></td>
<td><span id="secondTd"></span></td>
</tr>

<tr><td></td>
<td><input type="button" value="Add More SKU" onclick="addMoreProductId()"/></td>
</tr>
</#if>





</tbody></table>



</td>
<td width=50px>&nbsp;</td>
<td valign=top>
<table cellspacing="0">
<tbody>

















</tbody>
</table>

</td>
</tr>
<tr><td align=center>
<#if action=="VIEW">
<#else>
<input type="submit" value="<#if action=="CREATE">Create<#else>Update</#if>" class="smallSubmit"/>
</#if>
</td><td></td>
</tr>
</table>
</form>




<#--<table cellspacing="0" class="basic-table">
<tbody>
<tr>
<td style="padding-left:10em;">
<form><input type="button" value="Add More SKU" onclick="addMoreProductId()"/></form>
</td>
</tr>

</tbody>
</table>-->

<div align="center">
<a href="javascript:void(0)" onclick="window.close();" class="buttontext">Close</a>
</div>


<script language="JavaScript" type="text/javascript">
function checkscript() {
var checkProcess = false;
if(document.getElementById('type').value) {
checkProcess = true;
} else {
alert('"请求类型" 不能空白');
//return false;
}

if(document.getElementById('orderId').value) {
checkProcess = true;
} else {
alert('"orderId" 不能空白');
//return false;
}

if(document.getElementById('receiver').value) {
checkProcess = true;
} else {
alert('"接收人" 不能空白');
//return false;
}

if(document.getElementById('description').value) {
checkProcess = true;
} else {
alert('“请求内容” 不能空白');
//return false;
}

return checkProcess;
}
</script>
