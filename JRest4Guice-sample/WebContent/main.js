/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
var currentContact;
var restMethod = "POST";
var validationHelper;
var contacts_ds = new Spry.Data.JSONDataSet(null,{sortOnLoad:"name"});
var contact_detail_ds = new Spry.Data.JSONDataSet();

window.onload = function(){
	validationHelper = new SpryExt.ValidationHelper();
	init();
	IFrameUtil.subscribeEvent("onLogin",window,function(param){
		onTimeout();
	});
	if(jQuery.browser.mozilla){
		$("#logoDiv").css("top","12px");
	}
}

/**
 * 当网页超时、登录时所执行的权限过滤
 */
function onTimeout(){
	new Thread(function(){
		SpryExt.rest.doGet("resource/security/userRoles",function(json){
			if(json.content != ""){
				SpryExt.security.currentUserName = json.content.userName;
				SpryExt.security.currentUserRoles = json.content.userRoles;
				doSecurityCheck();
			}
		});
	},100).start();
}

/**
 * 页面权限过滤
 */
function doSecurityCheck(){
	$("#securityIframe").attr("src","");
	$.unblockUI();
	$("#securityDiv").hide();
	$("#logoutButton").show();
	$("#loginButton").hide();
	$("#welcomeDiv").show();
	$("#userInfoSpan").html(SpryExt.security.currentUserName);
}

/**
 * 初始化
 */
function init(){
	contacts_ds.setURL("resource/contacts");
	contacts_ds.useCache = false;
	contacts_ds.setPath("content");
	contacts_ds.setRequestInfo({headers:{"Accept":"application/json"}},true);
	
	contacts_ds.addObserver({
		onCurrentRowChanged:function(dataSet,rowInfo){
			$("#cContactSpan").show();
		},
		onDataChanged:function(dataSet, type) {
			$("#cContactSpan").hide();
			$("#editArea").hide("slow");
		}
	});

	contact_detail_ds.setURL("resource/contact/{contacts_ds::id}");
	contact_detail_ds.useCache = false;
	contact_detail_ds.setPath("content");
	contact_detail_ds.setRequestInfo({headers:{"Accept":"application/json"}},true);
	
	//装饰AJAX请求的Loading条
	new SpryExt.DataSetDecorator().decorateAjaxLoading(contacts_ds).decorateAjaxLoading(contact_detail_ds);
	
	//增强表格区域的功能（多选、分页）
	SpryExt.TableRegionDecorator.decorate("contactListRegion",contacts_ds,"contactTable",{
		//监听表格的选择事件
		onChecked:function(){
			var names = getCheckedContactNames();
			//处理已经选择联系人的显示（对应于controlBar)
			var len = names.length;
			if(len>0){
				var _name = " "+names.join(",")+" ";
				var _detail = _name;
				if(len>5){
					names = names.slice(0,5);
					_name = " "+names.join(",")+" ......";
				}
				
				$("#cContact").html("<span title=\""+_detail+"\"> "+_name+" </span>");
				$("#cContactSpan").show();
			}else{
				$("#cContactSpan").hide();
			}
	
			if(len==1){
				$("#infoArea").hide("slow");
				$("#editArea").show("slow");
			}else{
				$("#editArea").hide("slow");
				$("#infoArea").show("slow");
			}
		},
		recordTypeName:"个联系人",
		onPaged:function(index){
			contacts_ds.loadPageData({pageIndex:index,pageSize:SpryExt.PageInfoBar.pageSize});
		},
		onPostUpdate:function(){
			//构建拖放操作
			$(".dragRow").draggable({
		    	helper: function(ev){
					var names = getCheckedContactNames();
					var info,len;
					len = names.length;
					if(len>3){
						info = names.slice(0,3).join(",")+" ...";
					}else{
						info = names.join(",");
					}
					if(len>0)
	    				return $("<div class='dragRow dragElement' style='font-weight: bold;cursor: move;'><img src='images/user.gif'> "+info+"  &nbsp;&nbsp;</div>").appendTo("body");
	    			else
	    				return $("<div></div>");
		    	},
		   	    cursorAt: { 
			        top: 2, 
			        right:2  
			    } 
			});
		}
	});
	
	//装载数据
	contacts_ds.loadPageData();

	$(".recyle").droppable({ 
	    accept: ".dragRow",
	    hoverClass: "selectedClass",
	    drop: function(ev, ui) {
	    	//ui.element.fadeOut("fast",function() {$(this).fadeIn("fast")});
	        deleteContact(); 
	    } 
	});
	
	onTimeout();
}

function getCheckedContactNames(){
	var rows = contactTable_decorator.getCheckedRows();
	var names = [];
	for(var i=0;i<rows.length;i++){
		names.push(rows[i].name);
	}
	
	return names;
}

var detailObserver = {
	onPostUpdate: function(notifier, data) { 
		restMethod = "PUT";
		validationHelper.removeAll();
		//绑定验证逻辑
		validationHelper.createTextFieldValidation("theName", "none", {useCharacterMasking:true, regExpFilter:/^[^\'"\*]{0,15}$/, validateOn:["blur"]});
		validationHelper.createTextFieldValidation("theMobilePhone", "mobile", {isRequired:false,useCharacterMasking:true, validateOn:["blur"]});
		validationHelper.createTextFieldValidation("theEmail", "email", {isRequired:false,useCharacterMasking:true,  validateOn:["blur"]});
		validationHelper.createTextAreaValidation("theAddress", {isRequired:false,useCharacterMasking:true, maxChars:40, validateOn:["blur"]});

		$("#uploadFrame").attr("src","upload.html?fileUrl='"+$("#headPic").val()+"'");
	}
};

Spry.Data.Region.addObserver("editRegion", detailObserver);


function doCancel(){
	if(restMethod == "POST")
		clear($("#editArea"));
	else
		$('#editForm')[0].reset();
}
/**
 * 清输入框
 */
function clear(elem){
	elem = $(elem);
	elem.find("input[@type!=button]").val("");
	elem.find("textarea").val("");
	//清除图片
	$("#uploadFrame").attr("src","upload.html?fileUrl='"+$("#headPic").val()+"'");
	
	validationHelper.reset();
}

function createContact(){
	restMethod = "POST";
	//取消所有选择的行
	contactTable_decorator.uncheckAll();
	clear($("#editArea"));
	$("#infoArea").hide("slow");
	$("#editArea").show("slow");
}

/**
 * 保存联系人
 */
function saveOrUpdateContact(){
	if(!validationHelper.validate())
		return;
	
	var form = $("#editArea")[0];
	//收集数据
	var postData = SpryExt.DataHelper.gatherData(form);
	currentContact = contacts_ds.getCurrentRow();
	var url = "resource/contact";
	if(restMethod == "PUT")
		url += "/"+currentContact.id;

	//保存联系人
	SpryExt.rest._call(restMethod,url,function(result){
		if(result.errorType == ""){
			contacts_ds.loadData();
		}else
			alert(result.errorMessage);
	},{postData:postData});
}

/**
 * 删除联系人
 */
function deleteContact(id,nme){
	var rows = contactTable_decorator.getCheckedRows();
	if(rows.length<1){
		alert("请选择您要删除的联系人！");
		return;
	}
	
	var ids = [];
	var names = [];
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].id);
		names.push(rows[i].name);
	}
	
	if(!window.confirm("您确定要删除 \""+names.join(",")+"\" 吗?"))
		return;
	
	//删除联系人
	SpryExt.rest.doDelete("resource/contact/"+ids,function(result){
		if(result.errorType == ""){
			contacts_ds.loadData();
			currentContact = null;
			
			$("#recyle").attr("src","images/recycle_full.png")
		}else
			alert(result.errorMessage);
	});
}

function doLogout(){
	if(!window.confirm("您确定要注销当前的登录状态吗?"))
		return;
	window.location.href = "logout.jsp"
}

function doLogin(){
	$.blockUI({message: $('#securityDiv'), css: { width: '400px',height:'206px'}}); 
	$("#securityIframe").attr("src","login.jsp");
}

window.onUpload = function(fileUrl){
	$("#headPic",this.document).val(fileUrl);
}