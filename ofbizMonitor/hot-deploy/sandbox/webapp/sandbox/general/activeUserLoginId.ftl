<#if userLoginList??>
    <#list userLoginList as userLoginGV>
        ${userLoginGV.userLoginId}
        <br class="clear"/>
    </#list>
</#if>