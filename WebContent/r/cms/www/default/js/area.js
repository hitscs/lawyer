
/** 
* ������ 
* 
*/ 
function loadCity() { 
var provinceId = $("#provinceSelect option:selected").val(); 
if(provinceId == null || provinceId == ""){ 
//alert("�Ҳ���ʡ"); 
}else{ 
$.post(rootPath+"/loadCity", { 
"q" : provinceId 
}, function(data, result) { 
if(data == "noId"){ 
alert("�������"); 
}else if(data == "null"){ 
alert("ϵͳ�Ҳ������ڸ�ʡ����"); 
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
* ������ 
* 
*/ 
function loadRegion() { 
var cityId = $("#citySelect option:selected").val(); 
if(cityId == null || cityId == "" || cityId < 1){ 
alert("�Ҳ�����"); 
}else{ 
$.post(rootPath+"/loadRegion", { 
"q" : cityId 
}, function(data, result) { 
if(data == "noId"){ 
alert("�������"); 
}else if(data == "null"){ 
alert("ϵͳ�Ҳ������ڸ��е���"); 
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
* ʡ�ı��¼� 
* 
*/ 
$("#provinceSelect").change(loadCity); 
/** 
* �иı��¼� 
* 
*/ 
$("#citySelect").change(loadRegion); 

$(function() { 
loadCity(); 
}); 