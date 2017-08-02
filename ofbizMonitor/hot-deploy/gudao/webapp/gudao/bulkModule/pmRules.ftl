<div id="Main" class="screenlet">
    <div class="screenlet-title-bar">
        <ul>
            <li class="h3">${uiLabelMap.pmRules}</li>
        </ul>
        <br class="clear"/>
    </div>
    <div class="screenlet-body">
Platform Group Rules:<br />
1. Default is G4 when SKU is newly created<br />
2. Platform department can only change platform product group to G1 or G2<br />
3. If there is not any G1 platform product group, all platform product group will be changed to G0 (except G3 and G4 stays the same)<br />
4. If Platform department update platform status to G3 status list<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if no platform product group data update in PM template, will be automatically changed to G3<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if there is some data in PM template, will be updated according to PM update template<br />
5. If Platform department update platform status to G4 status list:<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if platform product group = G4, nothing changed<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if platform product group = G3, will be automatically changed to G4<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if platform product group = G2, nothing changed<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if platform product group = G1, will be automatically changed to G2<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if platform product group = G0, nothing changed<br />
6. If product general status is set to DISCONTINUED:<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if platform product group = G4, nothing changed<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if platform product group = G3, will be automatically changed to G4<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if platform product group = G2, will be automatically changed to G0<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if platform product group = G1, will be automatically changed to G0<br />
&nbsp;&nbsp;&nbsp;&nbsp;- if platform product group = G0, nothing changed<br />
<br />
<br />
G3 status list:<br />
<ul>
<li>&nbsp;&nbsp;重点维护</li>
<li>&nbsp;&nbsp;正常在售</li>
<li>&nbsp;&nbsp;EN不上</li>
<li>&nbsp;&nbsp;EU不上</li>
<li>&nbsp;&nbsp;AU不上</li>
<li>&nbsp;&nbsp;UK不上</li>
<li>&nbsp;&nbsp;US不上</li>
<li>&nbsp;&nbsp;KA不上</li>
</ul>
<br />
G4 status list:<br />
<ul>
<li>&nbsp;&nbsp;新品待上</li>
<li>&nbsp;&nbsp;侵权下架</li>
<li>&nbsp;&nbsp;断货下架</li>
<li>&nbsp;&nbsp;滞销下架</li>
<li>&nbsp;&nbsp;问题下架</li>
<li>&nbsp;&nbsp;风险下架</li>
</ul>

    </div>
    <div align=center>
        <a href="javascript:void(0)" onclick="window.close();" class="smallSubmit">Cancel/Close</a>
    </div>
</div