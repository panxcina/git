<div>
<h1>this is testyaya</h1>

${testyaya.text}
<br />
<br />
<br />
<#list all as t>
<form action="<@ofbizUrl>updatetestyaya</@ofbizUrl>" method="post" name="updatetestyaya_${t.testyayaId}" id="updatetestyaya_${t.testyayaId}">
 <input type="text" name="testyayaId" size="32" value="${t.testyayaId}"/>
 <input type="text" name="text" size="32" value="${t.text}"/>
<input type="submit" value="Update" class="smallSubmit"/>
 </form>
 ${t}
${t.testyayaId} , ${t.text}, ${t.createdStamp}, ${t.Browser}, ${t.CSSGrade}, ${t.EngineVersion}, ${t.Platform}, ${t.RenderingEngine}
<br />
<br />

</#list>

${test}
</div>