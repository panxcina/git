<script>
function myFunction()
{
	   var check=false;
       if (document.getElementById("motherSku").value) {  
			check=true;
             
        }  
        else {  
			alert("motherSku could not be empty!");
            check=false;
        }  
		if (check) {
		window.opener.location.reload(); window.close();
		}
		return check;

}
</script>
<div class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">sku detail</li>
        </ul>
        <br class="clear"/>
    </div>
  	<div class = "screenlet-body">

		

		<table>
			
			<form action="<@ofbizUrl>updateRivalPriceMonitor</@ofbizUrl>" method="post" name="updateRivalPriceMonitor" id="updateRivalPriceMonitor" onsubmit="return myFunction()">
			<tr>
				<td text-align="left" class="label" >userLogin &nbsp;&nbsp;&nbsp;</td>
				<td>
				<select name="userLoginId">
				<option value=""></option>
				<#list userLoginList?sort_by("userLoginId") as user>
				<option value="${user.userLoginId}" <#if userLoginIdVar?has_content >SELECTED</#if>>${user.userLoginId} - ${user.externalAuthId}</option>
				</#list>
				</select>
				</td>
			</tr>			
			<tr>
				<td  class='label' >motherSku :</td><td><input type="text" name="motherSku" size="20" id="motherSku"/></td>
			</tr>

			<tr>
				<td align="right"><input type="submit" value="Create" class="smallSubmit"/></td>
				<td align="left"><a href="javascript:void(0)" onclick="window.close();" class="smallSubmit">Close</a></td>
			</tr>
			</form>
        </table> 

<br class="clear"/>
	</div>
                     
<br class="clear"/>
</div>

