var currentContact;
var contacts_ds = new Spry.Data.JSONDataSet(null,{sortOnLoad:"name"});
var contact_detail_ds = new Spry.Data.JSONDataSet();


function deleteContact(id,name){
	currentContact = contacts_ds.getCurrentRow();
	if(currentContact != null){
		id = currentContact.id;
		name = currentContact.name;
	}
	
	if(id == null){
		alert("请选择您要删除的联系人！");
		return;
	}
	if(!window.confirm("您确定要删除\""+name+"\"?"))
		return;
	$.ajax({
		url:"resource/contact/"+id,
		type:"DELETE",
		dataType:'json',
		success:function(json){
			if(!json.errorMessage){
				contacts_ds.loadData();
				currentContact = null;
			}else
				alert(json.errorMessage);
		}
	})	
}
var detailObserver = { onPostUpdate: function(notifier, data) { 
		new Spry.Widget.ValidationTextField("theName", "none", {useCharacterMasking:true, regExpFilter:/^[^\'"\*]{0,15}$/, validateOn:["change","blur"]});
		new Spry.Widget.ValidationTextField("theMobilePhone", "none", {useCharacterMasking:true, validateOn:["change","blur"]});
		new Spry.Widget.ValidationTextField("theEmail", "email", {useCharacterMasking:true,  validateOn:["change","blur"]});
		new Spry.Widget.ValidationTextarea("theAddress", {useCharacterMasking:true, maxChars:40, counterType:"chars_count", counterId:"address", validateOn:["change","blur"]});
	}
};
Spry.Data.Region.addObserver("editArea", detailObserver);

function init(){
	contacts_ds.setURL("resource/contacts?first=1&max=100");
	contacts_ds.useCache = false;
	contacts_ds.setPath("content");
	contacts_ds.setRequestInfo({headers:{"Accept":"application/json"}},true);

	contact_detail_ds.setURL("resource/contact/{contacts_ds::id}");
	contact_detail_ds.setPath("content");
	contact_detail_ds.useCache = false;
	contact_detail_ds.setRequestInfo({headers:{"Accept":"application/json"}},true);

	contacts_ds.loadData();
}

window.onload = function(){
	init();
}
