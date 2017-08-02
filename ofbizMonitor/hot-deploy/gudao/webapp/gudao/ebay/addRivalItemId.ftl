<style type="text/css">
#table{
width:100px;
font-size:12px;
text-align:center;
border: 0px solid #333333;
border-collapse:collapse;
padding: 0px 0px 0px 0px;
line-height: 2.9em;
margin-left:  0.0em;
padding: 0.4em 0.6em 0.4em 0.6em;
font-weight: bold;
}
#table tr td {
width:50px;
border: 0px solid #333333;
display:table-cell;
padding: 0px 10px 0px 10px;
}
</style>
<script>
function myFunction()
{
	   var check=false;
       if (document.getElementById("itemId").value) {  
			check=true;
             
        }  
        else {  
			alert("ItemId could not be empty!");
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
            <li class="h3">add Rival ItemId</li>
        </ul>
        <br class="clear"/>
    </div>

  	<div class = "screenlet-body">
		<form action="<@ofbizUrl>updateAddRivalItemId</@ofbizUrl>" method="post" name="updateAddRivalItemId" id="updateAddRivalItemId" onsubmit="return myFunction()">
			<input type="hidden" name="userLoginId" value="${userLogin.userLoginId}" />
			<input type="hidden" name="motherSku" value="${motherSku}" />
			<input type="hidden" name="site" value="${site}" />
			<table id ="table">
				<tr><td  class='label'>MotherSku:</td><td align="center">${motherSku}</td></tr>
				<tr><td  class='label'>site:</td><td align="center">${site}</td></tr>
				<tr>
					<td  class='label'>rivalItemId:</td>
					<td><input type="text" name="itemId" size="20" id="itemId" /></td>				
				</tr>
				<tr></tr>
			 	<tr>
					<td  ></td>
					<td align="left" ><input type="submit" value="Add" class="smallSubmit"/></td>
				</tr>
				<tr><td></td><td align="left" ><a href="javascript:void(0)" onclick="window.close();" class="smallSubmit">Close</a></td></tr>
		   </table>
		</form>
	</div>
</div>
<br class="clear"/>	

