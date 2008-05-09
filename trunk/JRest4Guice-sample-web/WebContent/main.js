var currentContact;
var currentRowNo = 0;
var restMethod = "POST";
var validationHelper;
var contacts_ds = new Spry.Data.JSONDataSet(null,{sortOnLoad:"name"});
var contact_detail_ds = new Spry.Data.JSONDataSet();

window.onload = function(){
	validationHelper = new SpryExt.validationHelper();
	init();
}

/**
 * 清输入框
 */
function clear(elem){
	elem = $(elem);
	elem.find("input[@type=text]").val("");
	elem.find("textarea").val("");
	
	validationHelper.reset();
}

function createContact(){
	restMethod = "POST";
	clear($("#editArea"));
	$("#name").click();
}

/**
 * 保存联系人
 */
function saveOrUpdateContact(){
	if(!validationHelper.validate())
		return;
	
	var form = Spry.$("editArea");
	//收集数据
	var postData = SpryExt.gatherData(form);
	currentContact = contacts_ds.getCurrentRow();
	var url = "resource/contact";
	if(restMethod == "PUT")
		url += "/"+currentContact.id;

	//保存联系人
	SpryExt.rest._call(restMethod,url,function(result){
		if(!result.errorMessage){
			contacts_ds.loadData();
		}else
			alert(result.errorMessage);
	},{postData:postData});
}

/**
 * 删除联系人
 */
function deleteContact(id,nme){
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
	
	//删除联系人
	SpryExt.rest.doDelete("resource/contact/"+id,function(result){
		if(!result.errorMessage){
			contacts_ds.loadData();
			currentContact = null;
		}else
			alert(result.errorMessage);
	});
}

var detailObserver = {
	onPostUpdate: function(notifier, data) { 
		restMethod = "PUT";
		validationHelper.removeAll();
		validationHelper.createTextFieldValidation("theName", "none", {useCharacterMasking:true, regExpFilter:/^[^\'"\*]{0,15}$/, validateOn:["blur"]});
		validationHelper.createTextFieldValidation("theMobilePhone", "none", {isRequired:false,useCharacterMasking:true, validateOn:["blur"]});
		validationHelper.createTextFieldValidation("theEmail", "email", {isRequired:false,useCharacterMasking:true,  validateOn:["blur"]});
		validationHelper.createTextAreaValidation("theAddress", {isRequired:false,useCharacterMasking:true, maxChars:40, validateOn:["blur"]});
	}
};

Spry.Data.Region.addObserver("editArea", detailObserver);

/**
 * 初始化
 */
function init(){
	contacts_ds.setURL("resource/contacts?first=0&max=100");
	contacts_ds.useCache = false;
	contacts_ds.setPath("content");
	contacts_ds.setRequestInfo({headers:{"Accept":"application/json"}},true);
	
	contacts_ds.addObserver({
		onCurrentRowChanged:function(dataSet,rowInfo){
			currentRowNo = contacts_ds.getCurrentRowNumber();
		},
		onDataChanged:function(dataSet, type) {
			if(currentRowNo != null){
				contacts_ds.setCurrentRowNumber(currentRowNo);
			}
		}
	});

	contact_detail_ds.setURL("resource/contact/{contacts_ds::id}");
	contact_detail_ds.setPath("content");
	contact_detail_ds.useCache = false;
	contact_detail_ds.setRequestInfo({headers:{"Accept":"application/json"}},true);

	new SpryExt.DataSetDecorator().decorateAjaxLoading(contacts_ds).decorateAjaxLoading(contact_detail_ds);

	contacts_ds.loadData();
}