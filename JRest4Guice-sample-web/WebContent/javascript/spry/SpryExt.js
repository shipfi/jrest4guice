SpryExt = {};

//===============================================================================
// Restful web service client
//===============================================================================
SpryExt.rest = {};
SpryExt.rest.doGet = function(url,callBack,options){
	SpryExt.rest._call("GET",url,callBack,options);
}
SpryExt.rest.doPut = function(url,callBack,options){
	SpryExt.rest._call("PUT",url,callBack,options);
}
SpryExt.rest.doPost = function(url,callBack,options){
	SpryExt.rest._call("POST",url,callBack,options);
}
SpryExt.rest.doDelete = function(url,callBack,options){
	SpryExt.rest._call("DELETE",url,callBack,options);
}

SpryExt.rest._call = function(method,url,callBack,options){
	options = options || {postData:null};
	
	if(!options.headers)
		options.headers = {Accept:"application/json"};
	
	Spry.Utils.loadURL(method, url, true, function(request){
		var result = eval("("+request.xhRequest.responseText+")");
		callBack(result);
	},options);
}

SpryExt.DataSetDecorator = function(){
	if(SpryExt.DataSetDecorator.loadingObserver == null){
		var loadingBar = $("#loading");
		if(loadingBar.length==0)
			loadingBar = $("<div id=\"loading\" class=\"loadingBar\" style=\"display: none;\">正在加载数据...</div>").appendTo("body");
		SpryExt.DataSetDecorator.loadingObserver = {
				onPreLoad:function(){
					loadingBar.show();
				},
				onPostLoad:function(){
					loadingBar.hide();
				}
		};
	}
};

SpryExt.DataSetDecorator.prototype.decorateAjaxLoading = function(dataSet){
	dataSet.addObserver(SpryExt.DataSetDecorator.loadingObserver);
	return this;
}


//===============================================================================
// 通用的数据收集
//===============================================================================
SpryExt.gatherData = function(containerElm,isJson){
	var nodeslist = {};
	var elements = containerElm.getElementsByTagName("*");
	return _doGather(elements,null,isJson);
}

//===============================================================================
// 表单数据收集
//===============================================================================
SpryExt.gatherFormData = function (form, elements,isJson){
	if (!form)
		return '';

	if ( typeof form == 'string' )
		form = document.getElementById(form);

	var formElements;
	if (elements)
		formElements = ',' + elements.join(',') + ',';

	return _doGather(form.elements,formElements,isJson);
};

//===============================================================================
// 数据收集的私有方法
//===============================================================================
function _doGather(nodes,formElements,isJson){
	var compStack = new Array();
	var el;
	var mark = isJson?":":"="
	for (var i = 0; i < nodes.length; i++ ){
		el = nodes[i];
		if (el.disabled || !el.name){
			continue;
		}

		if (!el.type){
			continue;
		}

		if (formElements && formElements.indexOf(',' + el.name + ',')==-1)
			continue;

		switch(el.type.toLowerCase()){
			case 'text':
			case 'password':
			case 'textarea':
			case 'hidden':
			case 'submit':
				compStack.push(encodeComponent(el.name,isJson) + mark + encodeComponent(el.value,isJson));
				break;
			case 'select-one':
				var value = '';
				var opt;
				if (el.selectedIndex >= 0) {
					opt = el.options[el.selectedIndex];
					value = opt.value || opt.text;
				}
				compStack.push(encodeComponent(el.name,isJson) + mark + encodeComponent(value,isJson));
				break;
			case 'select-multiple':
				for (var j = 0; j < el.length; j++){
					if (el.options[j].selected){
						value = el.options[j].value || el.options[j].text;
						compStack.push(encodeComponent(el.name,isJson) + mark + encodeComponent(value,isJson));
					}
				}
				break;
			case 'checkbox':
			case 'radio':
				if (el.checked)
					compStack.push(encodeComponent(el.name,isJson) + mark + encodeComponent(el.value,isJson));
				break;
			default:
			// file, button, reset
			break;
		}
	}
	
	if(!isJson)
		return compStack.join('&');
	else{
		return "{"+compStack.join(',')+"}";
	}
}

function encodeComponent(cmp,isJson){
	return isJson?("'"+cmp+"'"):encodeURIComponent(cmp);
}
