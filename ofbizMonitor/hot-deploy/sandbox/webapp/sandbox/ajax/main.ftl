<style type="text/css">
table {
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
tr  td {
border: 1px solid #333333;
display:table-cell;
padding: 0px 10px 0px 10px;
}
</style>

<script type="text/javascript">
    var prevId= null;
    var prevColor = null;
    function changeValue(id,str) {
        if (prevId != null) {
            document.getElementById(prevId).style.backgroundColor=prevColor;
        }

        prevColor =document.getElementById(id).style.backgroundColor;
        document.getElementById(id).style.backgroundColor='white';
        <!--document.getElementById("resultHere").innerHTML=str;-->
        prevId = id;

        var xmlhttp;

        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp=new XMLHttpRequest();
        } else {// code for IE6, IE5
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange=function() {
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                document.getElementById("resultHere").innerHTML=xmlhttp.responseText;
                document.getElementById("resultHere").style.backgroundColor='white';
            }
        }
        xmlhttp.open("GET","<@ofbizUrl>pmInfo?productId="+str+"</@ofbizUrl>",true);
        xmlhttp.send();
    }

    function showUserLoginIdList(str) {
        var xmlhttp;
        if (str.length==0) {
            document.getElementById("txtHint").innerHTML="";
            return;
        }
        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp=new XMLHttpRequest();
        } else {// code for IE6, IE5
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange=function() {
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                document.getElementById("txtHint").innerHTML=xmlhttp.responseText;
            }
        }
        xmlhttp.open("GET","<@ofbizUrl>activeUserLoginId?userLoginId="+str+"</@ofbizUrl>",true);
        xmlhttp.send();
    }
</script>

<div id="companyMission" class="screenlet">
    <div class="screenlet-title-bar">
        AJAX
    </div>
    <div class="screenlet-body" align="center">
        AJAX TESTING FIELD
    </div>
</div>

<div id="Main" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Ajax</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="center">

<table id="table">
    <tr class="rowHeader">
        <td colspan="2">Title 1</td>
        <td>Title 2</td>
    </tr>


    <tr>
        <td rowspan="2" id="1" onclick="changeValue(this.id, 'DA00002-1')">DA00002-1</td>
        <tr>
            <td>row 1 column 2</td>
            <td>row 1 column 2</td>
        </tr>
        <td rowspan="3" id="resultHere" >row 1 column 3</td>
    </tr>
    <tr>
        <td id="2" onclick="changeValue(this.id, 'DA00002-2')">DA00002-2</td>
        <td>row 2 column 2</td>
    </tr>


</table>


    
    </div>
</div>


<div id="Main" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">Ajax - UserLogin</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body" align="center">
<form action="">
UserLoginId：<input type="text" id="userLoginIdInput" onkeyup="showUserLoginIdList(this.value)" />
</form>
<br class="clear"/>
<span id="txtHint"></span>

    </div>
</div>