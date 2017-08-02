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





<div id="taskView" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Task</b></li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="center">

<table class="hover-bar" cellspacing="50" style="border-width: 1px;border-style: dotted;border-color: black;border-spacing: 50px 0;">
<tr class="header-row" style="white-space: nowrap;">
<td align="center">Task List</td>
<#list sbuList?sort as sbu>
<td align="center">${sbu}</td>
</#list>
</tr>
<tr valign="middle" style="white-space: nowrap;">
<td align="center">G1产品母版待复查</td>
<#list sbuList?sort as sbu>
<td align="center">
<form target="_blank" name="taskDetailCondition1${sbu}" id="taskDetailCondition1${sbu}" method="post" action="<@ofbizUrl>taskDetailCondition1</@ofbizUrl>">
<input type="hidden" name="ownerGroup" value="${sbu}"/>
<input type="hidden" name="productGroup" value="G1"/>
</form>
<a href="javascript:document.getElementById('taskDetailCondition1${sbu}').submit()">
${condition1Map.get(sbu)}</a>
</td>
</#list>
</tr>

<tr><td>&nbsp;</td></tr>





<tr valign="middle" style="white-space: nowrap;">
<td align="center">G2产品母版待复查</td>
<#list sbuList?sort as sbu>
<td align="center">
<form target="_blank" name="taskDetailCondition1${sbu}" id="taskDetailCondition1${sbu}" method="post" action="<@ofbizUrl>taskDetailCondition1</@ofbizUrl>">
<input type="hidden" name="ownerGroup" value="${sbu}"/>
<input type="hidden" name="productGroup" value="G2"/>
</form>
<a href="javascript:document.getElementById('taskDetailCondition1${sbu}').submit()">
${condition1G2Map.get(sbu)}</a>
</td>
</#list>
</tr>

<tr><td>&nbsp;</td></tr>


<tr valign="middle" style="white-space: nowrap;">
<td align="center">G3产品母版待复查</td>
<#list sbuList?sort as sbu>
<td align="center">
<form target="_blank" name="taskDetailCondition1${sbu}" id="taskDetailCondition1${sbu}" method="post" action="<@ofbizUrl>taskDetailCondition1</@ofbizUrl>">
<input type="hidden" name="ownerGroup" value="${sbu}"/>
<input type="hidden" name="productGroup" value="G3"/>
</form>
<a href="javascript:document.getElementById('taskDetailCondition1${sbu}').submit()">
${condition1G3Map.get(sbu)}</a>
</td>
</#list>
</tr>

<tr><td>&nbsp;</td></tr>












<tr valign="middle" style="white-space: nowrap;">
<td align="center">G1产品对手待更新</td>
<#list sbuList?sort as sbu>
<td align="center">
<form target="_blank" name="taskDetailCondition4${sbu}" id="taskDetailCondition4${sbu}" method="post" action="<@ofbizUrl>taskDetailCondition4</@ofbizUrl>">
<input type="hidden" name="ownerGroup" value="${sbu}"/>
</form>
<a href="javascript:document.getElementById('taskDetailCondition4${sbu}').submit()">
${condition4Map.get(sbu)}</a>
</td>
</#list>
</tr>

<tr><td>&nbsp;</td></tr>

<tr valign="middle" style="white-space: nowrap;">
<td align="center">对手价格偏低待复查</td>
<#list sbuList?sort as sbu>
<td align="center">
<form target="_blank" name="taskDetailCondition2${sbu}" id="taskDetailCondition2${sbu}" method="post" action="<@ofbizUrl>taskDetailCondition2</@ofbizUrl>">
<input type="hidden" name="ownerGroup" value="${sbu}"/>
</form>
<a href="javascript:document.getElementById('taskDetailCondition2${sbu}').submit()">
${condition2Map.get(sbu)}</a>
</td>
</#list>
</tr>

<tr><td>&nbsp;</td></tr>

<#--<tr valign="middle" style="white-space: nowrap;">
<td align="center">美国站点在线待补齐<br />英国站点在线待补齐<br />澳洲站点在线待补齐<br />加拿大站点在线待补齐</td>
<#list sbuList?sort as sbu>
<td align="center">
<#assign siteMap = condition3Map.get(sbu)>

<table cellspacing="5">
<tr>
<td align="center">US</td>
<td align="center">UK</td>
<td align="center">AU</td>
<td align="center">CA</td>
<td align="center">FR</td>
<td align="center">DE</td>
<td align="center">ES</td>
</tr>
<tr>
<td align="center">${siteMap.get("US")}</td>
<td align="center">${siteMap.get("UK")}</td>
<td align="center">${siteMap.get("AU")}</td>
<td align="center">${siteMap.get("CA")}</td>
<td align="center">${siteMap.get("FR")}</td>
<td align="center">${siteMap.get("DE")}</td>
<td align="center">${siteMap.get("ES")}</td>
</tr>
</table>
</td>
</#list>
</tr>-->





</table>




    </div>
</div>