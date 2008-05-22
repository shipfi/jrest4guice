/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */

Function.prototype.bind = function() {
  var __method = this, args = arguments, object = args.length>1?args:(args.length>0?args[0]:null);
  return function() {
  	try{
	    return __method.apply(object, arguments);
  	}catch(e){
  	}
  }
}

Function.prototype.bindAsEventListener = function(object) {
  var __method = this;
  return function(event) {
    return __method.call(object, event || window.event);
  }
}

//===============================================================================
// Spry 修订与增强
//===============================================================================
Spry.Data.DataSet.prototype.setCurrentRow = function(rowID){
	var nData = { oldRowID: this.curRowID, newRowID: rowID };
	this.curRowID = rowID;
	this.notifyObservers("onCurrentRowChanged", nData);
};

//===============================================================================
// 重载callback,实现结果集的拦截
//===============================================================================
Spry.Utils.loadURL.callback = function(req){
	if (!req || req.xhRequest.readyState != 4)
		return;
	if (req.successCallback && (req.xhRequest.status == 200 || req.xhRequest.status == 0)){
		var text = req.xhRequest.responseText;
		if(text != null){
			try{
				var result = eval("(" + text + ")");	
				if(result.errorType == "org.jrest4guice.core.exception.UserNotLoginException"){//用户没有登录
					//alert("由于您还没有登录本系统,所以当前的操作被拒绝!");
				}else if(result.errorType == "org.jrest4guice.core.exception.AccessDeniedException"){//用户的操作权限受限
					//alert("您的访问权限不足,请及时与管理员联系,谢谢!");
				}
			}catch(e){}
		}
		req.successCallback(req);
	}else if (req.errorCallback)
		req.errorCallback(req);
};


//===============================================================================
// 扩展验证方式
//===============================================================================
if(Spry.Widget){
	Spry.Widget.ValidationTextField.ValidationDescriptors.phone={
		validation: function (value, options) {
			var regExp = new RegExp("(^\\d{3}(\\-)?(\\d{8})$)|(^\\d{4}(\\-)?(\\d{7,8})$)");
			if (!regExp.test(value)) {
				return false;
			}
			return true;
		}
	}
	Spry.Widget.ValidationTextField.ValidationDescriptors.mobile={
		validation: function (value, options) {
			var regExp = new RegExp("^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(13|15)\\d{9}$");
			if (!regExp.test(value)) {
				return false;
			}
			return true;
		}
	}
}

//===============================================================================
// 扩展数据加载的方式,增加对分页的处理
//===============================================================================
if(Spry.Data.JSONDataSet){
	Spry.Data.JSONDataSet.prototype.loadPageData = function(param){
		param = param || {pageIndex:1,pageSize:SpryExt.PageInfoBar.pageSize};
		if(this.oldUrl==null)
			this.oldUrl = this.url;
		this.url = this.oldUrl+"?"+jQuery.param(param);
		this.loadData();
	};
}

//===============================================================================
// 拦截排序的声明,添加额外的'sortColumn'属性来实现排序图标的支持
//===============================================================================
var spry_sort_attach = Spry.Data.Region.behaviorAttrs["spry:sort"].attach;
Spry.Data.Region.behaviorAttrs["spry:sort"].attach = function(rgn, node, value){
	spry_sort_attach(rgn,node,value);
	//增加排序标识
	$(node).attr("sortColumn",value);
}

//===============================================================================
// Spry 的自定义扩展
//===============================================================================
SpryExt = {};

//===============================================================================
// Dom操作助手
//===============================================================================
SpryExt.DomHelper = {};
SpryExt.DomHelper.isIE = function(){
  return navigator.appName.indexOf("Microsoft")!=-1;
}
SpryExt.DomHelper.cancelBubble = function(event){
	if(SpryExt.DomHelper.isIE()){
		event.cancelBubble=true;
		window.event.cancelBubble=true;
	}else{
		event.stopPropagation();
	}
}
SpryExt.DomHelper.disableSelection = function(element){
	element = element||document.body;
	if(SpryExt.DomHelper.isIE()){
		element.UNSELECTABLE = "on";
	}else{
		element.style.MozUserSelect = "none";
	}
}

SpryExt.DomHelper.enableSelection = function(element){
	element = element||document.body;
	if(SpryExt.DomHelper.isIE()){
		element.UNSELECTABLE = "off";
	}else{
		element.style.MozUserSelect = "";
	}
}

SpryExt.DomHelper.parentElemByTagName = function(node, tagName){
	if(!node) { return null; }
	if(tagName) { tagName = tagName.toLowerCase(); }
	do {
		node = node.parentNode;
	} while(node && node.nodeType != 1);

	if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
		return SpryExt.DomHelper.parentElemByTagName(node, tagName);
	}
	return node;
}

//===============================================================================
// 数据显示区域的装饰器
//===============================================================================
SpryExt.TableRegionDecorator = function(){
}

/**
 * dataRegionId : 数据显示区域的ID
 * dataSet	:数据集
 * tableId :数据显示所在的表格ID
 * showCheckBox :是否显示复选框
 */
SpryExt.TableRegionDecorator.decorate = function(dataRegionId,dataSet,tableId,option){
	option = option ||{};
	Spry.Data.Region.addObserver(dataRegionId, {
		onPreUpdate: function(){
			//删除为了提高用户体验而产生的临时显示区域
			$("#"+tableId+"_temp").remove();
			try{
				var ids = eval(tableId+"_decorator.getCheckedIds();");
				eval(tableId+"_decorator.ids=ids;");
			}catch(e){}
		},
		onPostUpdate: function(notifier, data) {
			//处理排序的图标
			var sortColumn = dataSet.getSortColumn();
			var sortOrder = dataSet.getSortOrder();
			$("#"+tableId).find("td[sortColumn]").each(function(){
				var display = $(this).attr("sortColumn") == sortColumn?"":"none";
				var imgSrc = sortOrder=="ascending"?"sort_asc.gif":"sort_desc.gif";
				$(this).append("<img class='sortOrderImg' src='images/"+imgSrc+"' style='margin-left:8px;display:"+display+";'>");
			});
			
			//获取上次已经选择的行IDS
			try{
				var ids = eval(tableId+"_decorator.ids;");
			}catch(e){
				ids = [];
			}
			option.checkedIds = ids;
			var tableDecorator = new TableDecorator(dataSet,tableId);
			var onChecked = function(tr,checked){
				if(option.onChecked)
					option.onChecked.call(tableDecorator,tr,checked);
				var ids = this.getCheckedIds();
				if(ids.length == 1){
					dataSet.setCurrentRow(ids[0]);
				}					
			}

			//设置表格选择的相关事件
			tableDecorator.onChecked = onChecked;
			tableDecorator.onCheckedAll = onChecked;
			tableDecorator.onUnCheckedAll = onChecked;

			if(option.onCheckedAll)
				tableDecorator.onCheckedAll = option.onCheckedAll;
			if(option.onUnCheckedAll)
				tableDecorator.onUnCheckedAll = option.onUnCheckedAll;
			tableDecorator.decorateRow(option);
			
			SpryExt.PageInfoBar.build(dataSet.docObj,option.recordTypeName || "条记录","infoBar","navigation",option.onPaged);
			
			if(option.onPostUpdate)
				option.onPostUpdate(notifier, data);
		}
	});
	
	dataSet.addObserver({
		onDataChanged: function(){
			try{
				eval(tableId+"_decorator.uncheckAll();");
			}catch(e){
			}
		}
	});
}

//====================================================================
// 表格装饰器
//====================================================================
TableDecorator=function(dataSet,tableId,selectedClass,mouseoverClass){
	this.dataSet = dataSet;
	this.tableId = tableId;
	this.table = $("#"+this.tableId);
	this.selectedClass = selectedClass || "selectedClass";
	this.mouseoverClass = mouseoverClass || "mouseoverClass";
	try{
		eval(this.tableId+"_decorator=this");
	}catch(e){}
}

TableDecorator.prototype = {
	/**
	 * 为表格Body的所有行增加复选框的功能
	 * checkedIds : 初始要被选中的ID数组
	 */
	decorateRow : function(option){
		this.checkAbleOption = option;
		this.showCheckBox = option.showCheckBox==null?true:option.showCheckBox;
		this._initTable();
		var _self = this;
		//初始化表格头的全选Checkbox
		this.table.thead.each(
			function(){
				SpryExt.DomHelper.disableSelection(this);
				$(this).find("td").each(function(){
					SpryExt.DomHelper.disableSelection(this);
				});
				var ckb = $(this).find("input.selectAllCkb:first");
				if(ckb.length==0){
					this.insertBefore($("<td class='ckbHeadTd' style=\"width: 24px;\"><input style=\"width: 24px;\" class='selectAllCkb' id=\""+_self.tableId+"_allCkb\" type=\"checkbox\" onclick=\""+_self.tableId+"_decorator._checkAll(this);\"/></td>")[0],this.firstChild);
				}else
					ckb[0].checked = false;
				
				if(!_self.showCheckBox){
					ckb = $(this).find("input.selectAllCkb:first");
					ckb.hide();
				}
			}
		);

		//初始化表格体的复选Checkbox
		this.table.tbody.each(
			function(){
				_self.decorateSingleRow($(this));
			}
		);
	},
	/**
	 * 装饰单行
	 */
	decorateSingleRow : function(tr,reInit){
		if(reInit)
			this.reInitTable();
		
		var _self = this;
		var option = this.checkAbleOption;
		var checkedIds = option.checkedIds || [];
		var rowId = tr.attr("rowId");
		
		var td = $("<td style=\"width: 24px;\" class='tdCkb_"+rowId+"'><input type=\"checkbox\" style=\"display: none;width: 24px;\"/></td>");
		tr[0].insertBefore(td[0],tr[0].firstChild);

		SpryExt.DomHelper.disableSelection(tr[0]);
		tr.find("td").each(function(){
			SpryExt.DomHelper.disableSelection(this);
		});

		var ckb = tr.find("input:first");
		ckb.click(function(event){//鼠标单击
			SpryExt.DomHelper.cancelBubble(event);
			_self._checkCurrent(this,true);
			$(this).show();
			return true;
		});
		
		var fun = function(event){
			var ckb = tr.find("input:first")[0];
			if(((event.target || event.srcElement).tagName)!="INPUT")
				ckb.checked = !ckb.checked;
			_self._checkCurrent(ckb,true);
		};
		
		tr.attr("oldClass",tr.attr("class"));

		tr.mouseover(function(){//鼠标经过
			tr.removeClass(tr.attr("oldClass"));
			tr.addClass(_self.mouseoverClass);
			if(_self.showCheckBox)
				tr.find("input:first").show();
		}).mouseout(function(){//鼠标离开
			tr.removeClass(_self.mouseoverClass);
			if(tr.attr("class")!=_self.selectedClass)
				tr.addClass(tr.attr("oldClass"));
			_self._showCheckBox(tr);
		}).click(function(event){//鼠标单击
			if(!(event.ctrlKey || event.shiftKey))
				_self.uncheckAll(true);
				
			fun.call(this,event);
			if(option.onclick){
				option.onclick.call(this,event);
			}
		});
		
		var _find = false;
		for(var i =0;i<checkedIds.length;i++){
			if(rowId==checkedIds[i]){
				_find = true;
				checkedIds.slice(i,1);
				break;
			}
		}
		if(_find){
			var ckb = $("input:first",td[0])[0];
			ckb.checked = true;
			_self._checkCurrent(ckb);
		}
	},
	uncheckAll:function(notAllowFireEvent){
		var _self = this;
		this.table.tbody.find("input:checkbox").each(function(){
			this.checked = false;
			_self._checkCurrent(this,false,true);
		});
		
		if(!notAllowFireEvent)
			this.onUnCheckedAll();
	},
	/**
	 * 根据ID取消选择
	 */
	uncheckById:function(id){
		var ckb = this.table.tbody.find("td.tdCkb_"+id+" input:first");
		if(ckb.length != 0){
			ckb = ckb[0];
			ckb.checked = false;
			this._checkCurrent(ckb,true);
		}
	},
	/**
	 * 获取当前选择的ID数组
	 */
	getCheckedIds:function(){
		var rows = this.getCheckedTrs();
		var ids = new Array;
		rows.each(function(){
			ids.push($(this).attr("rowId"));
		});
		return ids;
	},
	/**
	 * 获取当前选择的数据对象数组
	 */
	getCheckedRows:function(){
		var ids = this.getCheckedIds();
		var rows = new Array;
		for(var i=0;i<ids.length;i++)
			rows.push(this.dataSet.getRowByID(ids[i]));
		return rows;
	},
	getCheckedTrs:function(){
		return this.table.table.find("tbody tr."+this.selectedClass);
	},
	/**
	 * tr:当前所在的TR
	 * checked ：是否被选中
	 */
	onChecked:function(tr,checked){
	},
	onCheckedAll:function(){
	},
	onUnCheckedAll:function(){
	},
	reInitTable:function(){//初始化表格对象
		this._initTable();
	},
	_checkAll:function(elem){//选择所有
		var checked = $(elem).attr("checked");
		var _self = this;
		this.table.tbody.find("input:checkbox").each(function(){
			this.checked = checked;
			_self._checkCurrent(this,false,true);
		});

		this.onCheckedAll();
	},
	_checkCurrent:function(elem,changeTop,notAlloFireEvent){//选择当前
		if(elem.checked){
			if(this.showCheckBox)
				$(elem).show();
		}else{
			$("#"+this.tableId+"_allCkb")[0].checked = false;
			$(elem).hide();
		}
		if(elem.checked && changeTop){
			this._changeCheckedAllCkbState();
		}
	 	var tr = $(SpryExt.DomHelper.parentElemByTagName(elem,"tr"));

		if(elem.checked){
			tr.removeClass(tr.attr("oldClass"));
			tr.addClass(this.selectedClass);
		}else{
			tr.removeClass(this.selectedClass);
			tr.addClass(tr.attr("oldClass"));
		}
		
		//触发用户定义的选中事件
		if(!notAlloFireEvent)
			this.onChecked(tr[0],elem.checked);
	},
	_showCheckBox:function(elem){//显示Checkbox
		var ckb = elem.find("input:first");
		if(!ckb[0].checked)
			ckb.hide();
	},
	_changeCheckedAllCkbState:function(){//改变全选Checkbox的状态
		var len = this.table.tbody.find("input:checkbox[@checked]").length;
		$("#"+this.tableId+"_allCkb")[0].checked = len==0?false:(len == this.table.tbody.find("input:checkbox").length);
	},
	_initTable:function(){//初始化表格对象
		var _table = $("#"+this.tableId);
		this.table = {
			table : _table,
			dom : _table[0],
			thead : $("thead tr",_table),
			tbody : $("tbody tr",_table)
		};
		return this.table;
	}
}


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

//===============================================================================
// 数据集的装饰器
//===============================================================================
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
// 数据验证助手
//===============================================================================
SpryExt.ValidationHelper = function(){
	this.validations = [];
};

SpryExt.ValidationHelper.prototype.createTextFieldValidation = function(element, type, options){
	this.validations.push(new Spry.Widget.ValidationTextField(element,type,options));
}

SpryExt.ValidationHelper.prototype.createTextAreaValidation = function(element, type, options){
	this.validations.push(new Spry.Widget.ValidationTextarea(element,type,options));
}

SpryExt.ValidationHelper.prototype.removeAll = function(){
	for(var i=0;i<this.validations.length;i++){
		this.validations[i].destroy();
	}
	this.validations.splice(0,this.validations.length);
}

SpryExt.ValidationHelper.prototype.validate = function(){
	var result = true;
	for(var i=0;i<this.validations.length;i++){
		result = this.validations[i].validate();
		if(!result)
			break;
	}
	
	return result;
}
SpryExt.ValidationHelper.prototype.reset = function(){
	for(var i=0;i<this.validations.length;i++)
		this.validations[i].reset();
}

//===============================================================================
// 数据助手
//===============================================================================
SpryExt.DataHelper = {}

/**
 * 任意区域的数据收集
 */
SpryExt.DataHelper.gatherData = function(containerElm,isJson){
	var nodeslist = {};
	var elements = containerElm.getElementsByTagName("*");
	return SpryExt.DataHelper._doGather(elements,isJson);
}

SpryExt.DataHelper._doGather = function(nodes,isJson){
	var compStack = new Array();
	var el;
	var mark = isJson?":":"=";
	var key;
	for (var i = 0; i < nodes.length; i++ ){
		el = nodes[i];
		key = el.name || el.id;
		if (el.disabled || !key){
			continue;
		}

		if (!el.type){
			continue;
		}

		switch(el.type.toLowerCase()){
			case 'text':
			case 'password':
			case 'textarea':
			case 'hidden':
			case 'submit':
				compStack.push(SpryExt.DataHelper._encodeComponent(key,isJson) + mark + SpryExt.DataHelper._encodeComponent(el.value,isJson));
				break;
			case 'select-one':
				var value = '';
				var opt;
				if (el.selectedIndex >= 0) {
					opt = el.options[el.selectedIndex];
					value = opt.value || opt.text;
				}
				compStack.push(SpryExt.DataHelper._encodeComponent(key,isJson) + mark + SpryExt.DataHelper._encodeComponent(value,isJson));
				break;
			case 'select-multiple':
				for (var j = 0; j < el.length; j++){
					if (el.options[j].selected){
						value = el.options[j].value || el.options[j].text;
						compStack.push(SpryExt.DataHelper._encodeComponent(key,isJson) + mark + SpryExt.DataHelper._encodeComponent(value,isJson));
					}
				}
				break;
			case 'checkbox':
			case 'radio':
				if (el.checked)
					compStack.push(SpryExt.DataHelper._encodeComponent(key,isJson) + mark + SpryExt.DataHelper._encodeComponent(el.value,isJson));
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

SpryExt.DataHelper._encodeComponent = function(cmp,isJson){
	return isJson?("'"+cmp+"'"):encodeURIComponent(cmp);
}


SpryExt.PageInfoBar = function(){}

SpryExt.PageInfoBar.maxPageCount = 9;
SpryExt.PageInfoBar.pageSize = 15;
/**
 * 构造分页信息条
 * pageInfo 分页信息 {resultCount:总数,pageSize:每页条数,index:当前页码}
 * var pageInfo = new Object();
 * pageInfo.resultCount = 100;//符合查询条件的记录总数
 * pageInfo.pageSize = 20; //每页显示记录的条数
 * pageInfo.index = 1; //当前页
 *
 * msg 提示信息(如:'个联系人');
 * infoBar 分页信息的显示区域(html元素的ID)
 * navigations 分页的导航区域(html元素的ID)
 * goPage 执行分页操作的函数
 * 
 * 用法 SpryExt.PageInfoBar.build(pageInfo, "条记录", "infoBar", "navigation", loadData);
 **/
SpryExt.PageInfoBar.build = function(pageInfo, msg, infoBar, navigations, goPage){
	var maxPageCount = SpryExt.PageInfoBar.maxPageCount;
	if(pageInfo.pageSize == null) 
		 pageInfo.pageSize = SpryExt.PageInfoBar.pageSize;  
	if(pageInfo.pageIndex == null) 
		 pageInfo.pageIndex = 1;  
	try{
		pageInfo.resultCount = parseInt(pageInfo.resultCount);
	}catch(exception){
		pageInfo.resultCount = 0;	
	}
	var pageCount = pageInfo.pageCount;
	document.getElementById(infoBar).innerHTML = "&nbsp;&nbsp;检索到 "+pageInfo.resultCount + " "+msg+"，共 " + pageCount + " 页";
	var navigation = document.getElementById(navigations);
	
	navigation.innerHTML = "";
	
	//  定制导航信息 ：上一页 1 2 3 下一页
	//定制上一页
	var textPerPage = document.createElement("span"); 
	textPerPage.style.width = "45px";
	textPerPage.innerHTML = "上一页";
	textPerPage.className="pageClass";
	if(pageInfo.pageIndex > 1) {
		textPerPage.style.visibility="visible";
		textPerPage.onclick =  function(){goPage(parseInt(this.toString()));}.bind(pageInfo.pageIndex -1); 
	}else{
		textPerPage.style.visibility="hidden";
	}
	navigation.appendChild(textPerPage);
	//定制 1 2 3 4 5 6
	var pageSpan = document.createElement("span");
	pageSpan.className="pageClass";
	for(var i=0;i<maxPageCount;i++){
		if(i>=pageCount) break;
		if(pageInfo.pageIndex<5 || pageCount<=maxPageCount)
			pageIndex = i+1;
		else if(pageInfo.pageIndex >=5 && pageInfo.pageIndex<pageCount-4 && pageCount>maxPageCount)
			pageIndex = pageInfo.pageIndex + i - 4;
		else if(pageInfo.pageIndex>=pageCount-4 && pageInfo.pageIndex >=5 && pageCount>maxPageCount)
			pageIndex = pageInfo.pageIndex + i -4 -(pageInfo.pageIndex-pageCount+4);
		pageSpanClone = pageSpan.cloneNode(true);
		pageSpanClone.innerHTML = pageIndex;
		if(pageIndex == pageInfo.pageIndex)
			pageSpanClone.className="pageClassd";	
		else
			pageSpanClone.onclick = function(){
				goPage(parseInt(this.toString()));
			}.bind(pageIndex);
		navigation.appendChild(pageSpanClone);
	}
	//定制下一页
	var textNextPage = document.createElement("span");
	textNextPage.style.width = "45px";
	textNextPage.innerHTML = "下一页";
	textNextPage.className="pageClass";
	if(pageInfo.pageIndex < pageCount ){
		textNextPage.style.visibility="visible";
		textNextPage.onclick = function(){
			goPage(parseInt(this.toString()));
		}.bind(pageInfo.pageIndex + 1);
	}else{
		textNextPage.style.visibility="hidden";
	}
	navigation.appendChild(textNextPage);
}