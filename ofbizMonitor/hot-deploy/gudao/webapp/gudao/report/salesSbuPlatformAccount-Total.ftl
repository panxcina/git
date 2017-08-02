<#macro renderDateTimeFieldGudao name className alert title value size maxlength id dateType shortDateInput timeDropdownParamName defaultDateTimeString localizedIconTitle timeDropdown timeHourName classString hour1 hour2 timeMinutesName minutes isTwelveHour ampmName amSelected pmSelected compositeType formName mask="" event="" action="" step="" timeValues="">
<span class="view-calendar">
<#if dateType!="time" >
<input type="text" name="${name}_i18n" <@renderClass className alert /><#rt/>
<#if title?has_content> title="${title}"</#if>
<#if value?has_content> value="${value}"</#if>
<#if size?has_content> size="${size}"</#if><#rt/>
<#if maxlength?has_content>  maxlength="${maxlength}"</#if>
<#if id?has_content> id="${id}_i18n"</#if>/><#rt/>
</#if>
<#-- the style attribute is a little bit messy but when using disply:none the timepicker is shown on a wrong place -->
<input type="text" name="${name}" style="height:1px;width:1px;border:none;background-color:transparent" <#if event?has_content && action?has_content> ${event}="${action}"</#if> <@renderClass className alert /><#rt/>
<#if title?has_content> title="${title}"</#if>
<#if value?has_content> value="${value}"</#if>
<#if size?has_content> size="${size}"</#if><#rt/>
<#if maxlength?has_content>  maxlength="${maxlength}"</#if>
<#if id?has_content> id="${id}"</#if>/><#rt/>
<#if dateType!="time" >
<script type="text/javascript">
<#-- If language specific lib is found, use date / time converter else just copy the value fields -->
if (Date.CultureInfo != undefined) {
var initDate = <#if value?has_content>jQuery("#${id}_i18n").val()<#else>""</#if>;
if (initDate != "") {
var dateFormat = Date.CultureInfo.formatPatterns.shortDate<#if shortDateInput?exists && !shortDateInput> + " 23:59:59"</#if>;
<#-- bad hack because the JS date parser doesn't understand dots in the date / time string -->
if (initDate.indexOf('.') != -1) {
initDate = initDate.substring(0, initDate.indexOf('.'));
}
var ofbizTime = "<#if shortDateInput?exists && shortDateInput>yyyy-MM-dd<#else>yyyy-MM-dd HH:mm:ss</#if>";
var dateObj = Date.parseExact(initDate, ofbizTime);
var formatedObj = dateObj.toString(dateFormat);
jQuery("#${id}_i18n").val(formatedObj);
}

jQuery("#${id}").change(function() {
var ofbizTime = "<#if shortDateInput?exists && shortDateInput>yyyy-MM-dd<#else>yyyy-MM-dd HH:mm:ss</#if>";
var newValue = ""
if (this.value != "") {
var dateObj = Date.parseExact(this.value, ofbizTime);
var dateFormat = Date.CultureInfo.formatPatterns.shortDate<#if shortDateInput?exists && !shortDateInput> + " 23:59:59"</#if>;
newValue = dateObj.toString(dateFormat);
}
jQuery("#${id}_i18n").val(newValue);
});
jQuery("#${id}_i18n").change(function() {
var dateFormat = Date.CultureInfo.formatPatterns.shortDate<#if shortDateInput?exists && !shortDateInput> + " 23:59:59"</#if>,
newValue = "",
dateObj = Date.parseExact(this.value, dateFormat),
ofbizTime;
if (this.value != "" && dateObj !== null) {
ofbizTime = "<#if shortDateInput?exists && shortDateInput>yyyy-MM-dd<#else>yyyy-MM-dd HH:mm:ss</#if>";
newValue = dateObj.toString(ofbizTime);
}
else { // invalid input
jQuery("#${id}_i18n").val("");
}
jQuery("#${id}").val(newValue);
});
} else {
<#-- fallback if no language specific js date file is found -->
jQuery("#${id}").change(function() {
jQuery("#${id}_i18n").val(this.value);
});
jQuery("#${id}_i18n").change(function() {
jQuery("#${id}").val(this.value);
});
}

<#if shortDateInput?exists && shortDateInput>
jQuery("#${id}").datepicker({
<#else>
jQuery("#${id}").datetimepicker({
showSecond: true,
<#-- showMillisec: true, -->
timeFormat: 'hh:mm:ss',
stepHour: 1,
stepMinute: 1,
stepSecond: 1,
</#if>
showOn: 'button',
buttonImage: '',
buttonText: '',
buttonImageOnly: false,
dateFormat: 'yy-mm-dd'
})
<#if mask?has_content>.mask("${mask}")</#if>
;
</script>
</#if>
<#if timeDropdown?has_content && timeDropdown=="time-dropdown">
<select name="${timeHourName}" <#if classString?has_content>class="${classString}"</#if>><#rt/>
<#if isTwelveHour>
<#assign x=11>
<#list 0..x as i>
<option value="${i}"<#if hour1?has_content><#if i=hour1> selected="selected"</#if></#if>>${i}</option><#rt/>
</#list>
<#else>
<#assign x=23>
<#list 0..x as i>
<option value="${i}"<#if hour2?has_content><#if i=hour2> selected="selected"</#if></#if>>${i}</option><#rt/>
</#list>
</#if>
</select>:<select name="${timeMinutesName}" <#if classString?has_content>class="${classString}"</#if>><#rt/>
<#assign values = Static["org.ofbiz.base.util.StringUtil"].toList(timeValues)>
<#list values as i>
<option value="${i}"<#if minutes?has_content><#if i?number== minutes ||((i?number==(60 -step?number)) && (minutes &gt; 60 - (step?number/2))) || ((minutes &gt; i?number )&& (minutes &lt; i?number+(step?number/2))) || ((minutes &lt; i?number )&& (minutes &gt; i?number-(step?number/2)))> selected="selected"</#if></#if>>${i}</option><#rt/>
</#list>
</select>
<#rt/>
<#if isTwelveHour>
<select name="${ampmName}" <#if classString?has_content>class="${classString}"</#if>><#rt/>
<option value="AM" <#if amSelected == "selected">selected="selected"</#if> >AM</option><#rt/>
<option value="PM" <#if pmSelected == "selected">selected="selected"</#if>>PM</option><#rt/>
</select>
<#rt/>
</#if>
</#if>
<input type="hidden" name="${compositeType}" value="Timestamp"/>
</span>
</#macro>



<#-- product master -->
<#-- <#if alt_row> class="alternate-row"</#if> -->

<style type="text/css">
#table{
font-size:12px;
text-align:center;
border: 1px solid #696969;
border-collapse:separate;
padding:0px 0px 0px 0px;
width:auto;
height:auto;
}
#table tr  td {
width:auto;
height:auto;
border: 1px solid #696969;
display:table-cell;
empty-cells:show;
}
#table0 tr  td {
width:135px;
height:auto;
border: 0px solid #696969;
display:table-cell;
empty-cells:show;
bgcolor:#828372}
#table1 tr  td {
width:135px;
height:auto;
border: 0px solid #696969;
display:table-cell;
empty-cells:show;
}
#table2 tr  td {
width:35px;
height:25px;
border: 1px solid #696969;
display:table-cell;
empty-cells:show;
}

#table3 tr  td {
width:60px;
height:24.4px;
border: 1px solid #000000;
display:table-cell;
empty-cells:show;
}
#table4 tr  td {
width:42px;
height:28.5px;
border: 1px solid #000000;
display:table-cell;
empty-cells:show;
}

</style>
<div id="productLookupList" class="screenlet">
<div class="screenlet-title-bar">

<br class="clear"/>
</div>
<div class="screenlet-body">
<script type="text/javascript">
    $(document).ready(function() {
        $('#a').multiselect();
    });
</script>

<form action="<@ofbizUrl>salesSbuPlatformAccount</@ofbizUrl>" method="post" name="form1" onsubmit="return checkscript()">
    <table  border="0" cellspacing="0" cellpadding="0">
        <tr>
            <td width='25%' align='right' class='label'>${uiLabelMap.CommonDateFilter}</td>
            <td align='left'>
                <table class="basic-table" cellspacing='0'>
                    <tr>
                        <td nowrap="nowrap">
                            <@htmlTemplate.renderDateTimeField name="minDate" event="" action="" value="${requestParameters.minDate?default(Static['org.ofbiz.base.util.UtilDateTime'].getMonthStart(nowTimestamp))}" className="" alert="" title="Format: yyyy-MM-dd" size="25" maxlength="30" id="minDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/><span class='label'>${uiLabelMap.CommonFrom}</span>
                        </td>
                    </tr>
                    <tr>                
                        <td nowrap="nowrap">
                            <@renderDateTimeFieldGudao name="maxDate" event="" action="" value="${requestParameters.maxDate?default(Static['org.ofbiz.base.util.UtilDateTime'].nowTimestamp())}" className="" alert="" title="Format: yyyy-MM-dd" size="25" maxlength="30" id="maxDate1" dateType="date" shortDateInput=false timeDropdownParamName="" defaultDateTimeString="" localizedIconTitle="" timeDropdown="" timeHourName="" classString="" hour1="" hour2="23" timeMinutesName="" minutes="" isTwelveHour="" ampmName="" amSelected="" pmSelected="" compositeType="" formName=""/><span class='label'>${uiLabelMap.CommonThru}</span>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td text-align="left" class="label" >sbu &nbsp;&nbsp;&nbsp;</td>
            <td>
                <div style=" overflow-y:scroll; width:120px; height:80px;”>
                    <select  id="sbuSelect" multiple="multiple">
                        <#list ownerGroupAllList as ownerGroup>
                            <option>
                                <input  name="ownerGroupInput" type="checkbox"   value="${ownerGroup.officeSiteName}" <#if ownerGroupInput.contains(ownerGroup.officeSiteName)> CHECKED</#if>/>${ownerGroup.officeSiteName}<br/>
                            </option>
                        </#list>
                    </select>
                </div>
            </td>
        </tr>
        <tr>
            <td align="center" class="label">Detail &nbsp;&nbsp;</td>
            <td>
                <input type="checkbox" name="detail" <#if detail>checked</#if>/>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <input type="submit"  value="search"  class="smallSubmit" />
            </td>
        </tr>
    </table>
</form>

<br class="clear"/>

<table class="basic-table hover-bar" cellspacing='0' id ="table">
    <tr class="header-row" >
        <td align="center">事业部</td>
            <#list platformList as platform>
                <td>
                    <table id="table0">
                        <tr >
                            <td align="center"><#if platform.roleTypeId?has_content>${platform.roleTypeId}</#if></td>
                        </tr>
                        <tr >
                            <td align="center">
                                <table><tr><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
                            </td>
                        </tr>
                    </table>
                </td>
            </#list>
        <td >
            <table id="table0">
                <tr ><td align="center">Total</td></tr>
                <tr >
                    <td align="center">
                        <table><tr><td align="center">销量</td><td align="center">毛利</td><td align="center">毛利率</td></tr></table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <#list ownerGroupList as ownerGroup>
                <table id="table3">
                    <tr>
                        <td align="center"><#if ownerGroup.officeSiteName?has_content>${ownerGroup.officeSiteName}</#if></td>
                        <td><table id="table3"><tr><td align="center">日均</td></tr><tr><td align="center">上月</td></tr></table></td>
                    </tr>
                </table>
            </#list>
        </td>
        <#list s as plf>
            <td>
                <table>
                    <tr>
                        <td align="center">
                            <#list plf.sbuList as sbu>
                                <table id="table1" >
                                    <tr>
                                        <td align="center" >
                                            <#list sbu.plist as data>
                                                <table id="table2" class="basic-table hover-bar">
                                                    <tr>
                                                        <td align="center"><#if data.dailySumSales?has_content>${data.dailySumSales?string["0"]}</#if></td>
                                                        <td align="center"><#if data.dailyItemProfit?has_content>${data.dailyItemProfit?string["0"]}</#if></td>
                                                        <td align="center"><#if data.dailyprofitRatePercent?has_content>${data.dailyprofitRatePercent?string["0%"]}</#if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="center"><#if data.lastMonthSales?has_content>${data.lastMonthSales?string["0"]}</#if></td>
                                                        <td align="center"><#if data.lastMonthProfit?has_content>${data.lastMonthProfit?string["0"]}</#if></td>
                                                        <td align="center"><#if data.lastMonthPercent?has_content>${data.lastMonthPercent?string["0%"]}</#if></td>
                                                    </tr>
                                                </table>
                                            </#list>
                                        </td>
                                    </tr>
                                </table>
                            </#list>
                        </td>
                    </tr>
                </table>
            </td>
        </#list>
        <td>
            <#list rightAllList as right>
                <table id="table4">
                    <tr>
                        <td align="center"><#if right.rightTotalSales?has_content>${right.rightTotalSales?string["0"]}</#if></td>
                        <td align="center"><#if right.rightTotalProfit?has_content>${right.rightTotalProfit?string["0"]}</#if></td>
                        <td align="center"><#if right.rightTotalPercent?has_content>${right.rightTotalPercent?string["0%"]}</#if></td>
                    </tr>
                    <tr>
                        <td align="center"><#if right.rightLastMonthSales?has_content>${right.rightLastMonthSales?string["0"]}</#if></td>
                        <td align="center"><#if right.rightLastMonthProfit?has_content>${right.rightLastMonthProfit?string["0"]}</#if></td>
                        <td align="center"><#if right.rightLastMonthPercent?has_content>${right.rightLastMonthPercent?string["0%"]}</#if></td>
                    </tr>
                </table>
            </#list>
        </td>
    </tr>
    <tr>
        <td>
            <table id="table3">
                <tr>
                    <td align="center" style="color:black font-size: 14pt">Total</td>
                    <td><table id="table3"><tr><td align="center">日均</td></tr><tr><td align="center">上月</td></tr></table></td>
                </tr>
            </table>
        </td>
        <#list a as total>
            <td>
                <table id="table4" >
                    <tr>
                        <td align="center"><#if total.downTotalSales?has_content>${total.downTotalSales?string["0"]}</#if></td>
                        <td align="center"><#if total.downTotalProfit?has_content>${total.downTotalProfit?string["0"]}</#if></td>
                        <td align="center"><#if total.totalPercent?has_content>${total.totalPercent?string["0%"]}</#if></td>
                    </tr>
                    <tr>
                        <td align="center"><#if total.downTotalLastMonthSales?has_content>${total.downTotalLastMonthSales?string["0"]}</#if></td>
                        <td align="center"><#if total.downTotalLastMonthProfit?has_content>${total.downTotalLastMonthProfit?string["0"]}</#if></td>
                        <td align="center"><#if total.downTotalLastMonthPercent?has_content>${total.downTotalLastMonthPercent?string["0%"]}</#if></td>
                    </tr>
                </table>
            </td>
        </#list>
        <td align="center" >
            <table id="table4" >
                <tr>
                    <td align="center"><#if finalTotalSales?has_content>${finalTotalSales?string["0"]}</#if></td>
                    <td align="center"><#if finalTotalProfit?has_content>${finalTotalProfit?string["0"]}</#if></td>
                    <td align="center"><#if finalPercent?has_content>${finalPercent?string["0%"]}</#if></td>
                </tr>
                <tr>
                    <td align="center"><#if finalLastMonthSales?has_content>${finalLastMonthSales?string["0"]}</#if></td>
                    <td align="center"><#if finalLastMonthProfit?has_content>${finalLastMonthProfit?string["0"]}</#if></td>
                    <td align="center"><#if finalLastMonthPercent?has_content>${finalLastMonthPercent?string["0%"]}</#if></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</div>
</div>
