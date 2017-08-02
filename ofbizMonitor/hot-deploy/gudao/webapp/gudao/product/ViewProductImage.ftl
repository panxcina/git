<#-- product master -->
<#-- <#if alt_row> class="alternate-row"</#if> -->
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
table.image {
border-width: 1px;
border-spacing: 2px;
border-style: outset;
border-color: black;
border-collapse: separate;
}


</style>


<div id="productImageList" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
    <li class="h3">Images for ${productId}</b></li>
  </ul>
  <br class="clear"/>
</div>
<div class="screenlet-body">
<div align="center">
<a href="javascript:void(0)" onclick="window.close();" class="bigbutton">Close</a>
</div>




<table>
<tr>
<#-- AMAZON == START -->
<td>
<table>
<tr align="center"><td><h2><b>AMAZON</b></h2></td></tr>
<tr>
<td>
<#list imageListAma?sort as image>
<table class="image">
<tr>
<td>
<a href="..${image}"><img src="..${image}" width="300px"></a>
<br />
https://img.factory-kiss.com${image?substring(11)}
<br />
<a href="https://img.factory-kiss.com${image?substring(11)}" class = "smallSubmit">Link</a>
</td>
</tr>
<tr><td>&nbsp;</td></tr>
</table>
</#list>
</td>
</tr>
</table>
</td>
<#-- AMAZON == END -->

<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
<#-- EBAY == START -->
<td>
<table>
<tr align="center"><td><h2><b>EBAY</b></h2></td></tr>
<tr>
<td>
<#list imageListEbay?sort as image>
<table class="image">
<tr>
<td>
<a href="..${image}"><img src="..${image}" width="300px"></a>
<br />
https://img.factory-kiss.com${image?substring(11)}
<br />
<a href="https://img.factory-kiss.com${image?substring(11)}" class = "smallSubmit">Link</a>
</td>
</tr>
<tr><td>&nbsp;</td></tr>
</table>
</#list>
</td>
</tr>
</table>
</td>
<#-- EBAY == END -->

<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
<#-- SMT == START -->
<td>
<table>
<tr align="center"><td><h2><b>SMT</b></h2></td></tr>
<tr>
<td>
<#list imageListSmt?sort as image>
<table class="image">
<tr>
<td>
<a href="..${image}"><img src="..${image}" width="300px"></a>
<br />
https://img.factory-kiss.com${image?substring(11)}
<br />
<a href="https://img.factory-kiss.com${image?substring(11)}" class = "smallSubmit">Link</a>
</td>
</tr>
<tr><td>&nbsp;</td></tr>
</table>
</#list>
</td>
</tr>
</table>
</td>
<#-- SMT == END -->

<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
<#-- WISH == START -->
<td>
<table>
<tr align="center"><td><h2><b>WISH</b></h2></td></tr>
<tr>
<td>
<#list imageListWish?sort as image>
<table class="image">
<tr>
<td>
<a href="..${image}"><img src="..${image}" width="300px"></a>
<br />
https://img.factory-kiss.com${image?substring(11)}
<br />
<a href="https://img.factory-kiss.com${image?substring(11)}" class = "smallSubmit">Link</a>
</td>
</tr>
<tr><td>&nbsp;</td></tr>
</table>
</#list>
</td>
</tr>
</table>
</td>
<#-- WISH == END -->

<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
<#-- OLD == START -->
<td>
<table>
<tr align="center"><td><h2><b>OLD</b></h2></td></tr>
<tr>
<td>
<#list imageListOld?sort as image>
<table class="image">
<tr>
<td>
<a href="..${image}"><img src="..${image}" width="300px"></a>
<br />
https://img.factory-kiss.com${image?substring(11)}
<br />
<a href="https://img.factory-kiss.com${image?substring(11)}" class = "smallSubmit">Link</a>
</td>
</tr>
<tr><td>&nbsp;</td></tr>
</table>
</#list>
</td>
</tr>
</table>
</td>
<#-- EBAY == END -->

</tr>
</table>




<div align="center">
<a href="javascript:void(0)" onclick="window.close();" class="bigbutton">Close</a>
</div>
  </div>
</div>