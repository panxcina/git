<#-- Gudao Main page -->
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

<div id="Target" class="screenlet">
<div class="screenlet-title-bar">
<ul>
<li class="h3">Target</li>
</ul>
<br class="clear"/>
</div>
<div class="screenlet-body" align="left">
<table class="hover-bar" cellspacing="10" style="border-width: 1px;border-style: dotted;border-color: black;border-spacing: 20px 0;">
<tr class="header-row" style="white-space: nowrap;">
<td align="center">TYPE</td>
<td align="center">TARGET</td>
<td align="center">CURRENT</td>
<td align="center">COMPLETE %</td>
</tr>
<#list gudaoTargetList as target>
<tr>
<#assign targetTypeEnum = delegator.findOne("Enumeration", {"enumId" : target.type}, false).description/>
<td align="left">${targetTypeEnum}</td>
<td align="center">${target.value}</td>
</tr>


</#list>
<#if hasEditPermission>
<tr><td>
<a class="buttontext" href="/gudao/control/editEbay" onclick="window.open(this.href,'','height=512,width=512,scrollbars=1');return false;">Edit</a>
</td></tr>
</#if>
</table>
</div>
</div>


<div id="Main" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
<li class="h3">SOP</li>
  </ul>
  <br class="clear"/>
</div>
<div class="screenlet-body" align="left">
<#if sopList?has_content>
<#assign valueXmlDoc = Static["org.ofbiz.entity.GenericValue"].makeXmlDocument([sopList]) />
${Static["org.ofbiz.base.util.UtilXml"].writeXmlDocument(valueXmlDoc)?replace("\n", "<br />")?replace("    ", "&nbsp;&nbsp;&nbsp;&nbsp;")}

</#if>
    
  </div>
</div>