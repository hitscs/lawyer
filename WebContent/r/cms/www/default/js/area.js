
/** 
* 加载市 
* 
*/ 
function loadCity() { 
var provinceId = $("#provinceSelect option:selected").val(); 
if(provinceId == null || provinceId == ""){ 
//alert("找不到省"); 
}else{ 
$.post(rootPath+"/loadCity", { 
"q" : provinceId 
}, function(data, result) { 
if(data == "noId"){ 
alert("请求错误"); 
}else if(data == "null"){ 
alert("系统找不到属于该省的市"); 
}else{ 
data = eval("{" + data + "}"); 
var citySelect = $("#citySelect"); 
var myCity = $("#myCity").val(); 
citySelect.html(""); 
for ( var i = 0; i < data.length; i++) { 
if(myCity != null && myCity != "" && myCity > 0 && myCity == data[i].id){ 
citySelect.append("<option selected='selected' value='" + data[i].id + "'>" 
+ data[i].name + "</option>"); 
}else{ 
citySelect.append("<option value='" + data[i].id + "'>" 
+ data[i].name + "</option>"); 
} 
} 
loadRegion(); 
} 
}); 
} 
}; 
/** 
* 加载区 
* 
*/ 
function loadRegion() { 
var cityId = $("#citySelect option:selected").val(); 
if(cityId == null || cityId == "" || cityId < 1){ 
alert("找不到市"); 
}else{ 
$.post(rootPath+"/loadRegion", { 
"q" : cityId 
}, function(data, result) { 
if(data == "noId"){ 
alert("请求错误"); 
}else if(data == "null"){ 
alert("系统找不到属于该市的区"); 
}else{ 
data = eval("{" + data + "}"); 
var regionSelect = $("#regionSelect"); 
var myRegion = $("#myRegion").val(); 
regionSelect.html(""); 
for ( var i = 0; i < data.length; i++) { 
if(myRegion != null && myRegion != "" && myRegion > 0 && myRegion == data[i].id){ 
regionSelect.append("<option selected='selected' value='" + data[i].id + "'>" 
+ data[i].name + "</option>"); 
}else{ 
regionSelect.append("<option value='" + data[i].id + "'>" 
+ data[i].name + "</option>"); 
} 
} 
} 
}); 
} 
}; 
/** 
* 省改变事件 
* 
*/ 
$("#provinceSelect").change(loadCity); 
/** 
* 市改变事件 
* 
*/ 
$("#citySelect").change(loadRegion); 

$(function() { 
loadCity(); 
}); 