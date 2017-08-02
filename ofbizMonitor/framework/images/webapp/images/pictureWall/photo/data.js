var data = [];

var dataStr = "1.DeathNote<br>\
<br>\
Japanese Cartoon<br>\
<br>\
<br>\
2.FateUBW<br>\
<br>\
Japanese Cartoon<br>\
<br>\
<br>\
3.FateZero<br>\
<br>\
Japanese Cartoon<br>\
<br>\
<br>\
4.GoneGirl<br>\
<br>\
American Movie<br>\
<br>\
<br>\
5.Shameless<br>\
<br>\
American TV Series<br>\
<br>\
<br>\
6.theDarkKnight<br>\
<br>\
American Movie<br>\
<br>\
<br>\
7.ToyStory<br>\
<br>\
American Movie<br>\
"
var d = dataStr.split("<br><br><br>");
for(var i = 0; i<d.length; i++){
  var c = d[i].split("<br><br>");
  data.push({
    img: c[0]+ ".jpg",
    caption: c[0].split(".")[1],
    desc: c[1]
  });
}

