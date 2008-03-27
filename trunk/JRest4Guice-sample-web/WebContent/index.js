var tEngine = new TemplateEngine();

var contact,_contactList,_editArea,isPost=true;

$(document).ready(function(){
	_editArea = $.elem("editArea");
});

function clear(elem){
	elem = $(elem);
	elem.find("input").val("");
	elem.find("textarea").val("");
	elem.find(".tagsTr").hide();
}


function getContactList(){
	clear(_editArea);
	isPost=true;
	_contactList = $.elem("contactList");
	var data = {first:0,max:100};
	$.ajax({
		url:"contacts",
		type:"GET",
		dataType:'json',
		data:data,
		ifModified:true,
		success:function(json){
			json = json.content;
			if(json.length){
				$(_contactList).find("tbody").show();
				tEngine.bind(_contactList,json);
			}
		}
	})	
}

function getContactDetail(id){
	isPost = false;
	$.ajax({
		url:"contact/"+id,
		type:"GET",
		dataType:'json',
		ifModified:true,
		success:function(json){
			contact = json.content;
			tEngine.fillData("#editArea",contact);
		}
	})	
}

function deleteContact(id,name){
	if(!window.confirm("你确定要删除\""+name+"\"?"))
		return;
	$.ajax({
		url:"contact/"+id,
		type:"DELETE",
		dataType:'json',
		success:function(json){
			if(!json.errorType)
				getContactList();
		}
	})	
}

function doCancel(){
	clear(_editArea);
}
function doSave(){
	var _data = gatherData(_editArea[0]);
	var url = "contact";
	if(!isPost)
		url += "/"+contact.id;
	else
		delete(_data.id);
	$.ajax({
		url:url,
		type:isPost?"POST":"PUT",
		dataType:'json',
		data:_data,
		ifModified:true,
		success:function(json){
			if(!json.errorType)
				getContactList();
			else
				alert(json.errorMessage)
		}
	})	
}

function createContact(){
	clear(_editArea);
}
