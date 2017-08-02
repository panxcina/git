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

<#if security.hasEntityPermission("PM", "_UPDATE", session)>

<div id="Main" class="screenlet">
<div class="screenlet-title-bar">
  <ul>
    <li class="h3">Import - Update Product Function</li>
  </ul>
  <br class="clear"/>
</div>
<div class="screenlet-body">
    <table class="basic-table hover-bar" cellspacing='0'>
<#--<tr class="header-row">
        <td width="50%" align="center">Import New Product<br />中文</td>
        <td width="50%" align="center">Update Existing Product</td>
      </tr>-->
      <tr valign="middle" align="center">
        <td width="33%"><a href="<@ofbizUrl>importNewProduct?importFormId=PRODUCT</@ofbizUrl>" class="bigbutton">Import New SKU</a></td>
        <td width="33%"><a href="<@ofbizUrl>bulkUpdate?updateFormId=PRODUCT</@ofbizUrl>" class="bigbutton">Update Existing SKU</a></td>
      </tr>
    </table>
</form>
    
  </div>
</div>
</#if>