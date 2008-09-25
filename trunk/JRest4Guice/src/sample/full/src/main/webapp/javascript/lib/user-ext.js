/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */

//===============================================================================
// JS增强
//===============================================================================
/**
* 通用比较方法(基本类型与对象类型)
*/
function equals(v1,v2){
	if(typeof(v1) != typeof(v2))
		return false;

	if(typeof(v1) == "object"){
		for(var p in v1){
			if(!equals(v1[p],v2[p]))
				return false;
		}
		
		return true;
	}else
		return v1 == v2;
}

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

//===========================================================
// Map对象
//===========================================================
var Map = function(){
   this.elements=new Array();
}
Map.prototype ={
  size:function(){
   	return this.elements.length;
  },
  put:function(_key,_value){
	   _key = _key + "";
	   if(this.containsKey(_key))
	   	this.remove(_key);
	   this.elements.push({key:_key,value:_value});
   },
   clear:function(){
   	 var vkeys = this.keys();
   	 if(vkeys != null){
	   	 for(var i=0;i<vkeys.length;i++){
	   	 	this.remove(vkeys[i]);
	   	 }
   	 }
   },
   remove:function(_key){
	   _key = _key + "";
  	 var bln=false;
     try{   
    	for (i=0;i<this.elements.length;i++){
	    	if (equals(this.elements[i].key,_key)){
	      	this.elements.splice(i,1);
  			 	return true;
	  	  }
  	 	}
  	 }catch(e){
     	bln=false;    
   	 }
	   return bln;
   },
   
   keys:function(){
  	 var result = new Array();
     try{   
    	for (i=0;i<this.elements.length;i++){  
    		result[i] = this.elements[i].key;
  	 	}
  	 }catch(e){
   	 }
	   return result;
   },

   values:function(){
  	 var result = new Array();
     try{   
    	for (i=0;i<this.elements.length;i++){  
    		result[i] = this.elements[i].value;
  	 	}
  	 }catch(e){
   	 }
	   return result;
   },

   containsKey:function(_key){
    _key = _key + "";
    var bln=false;
    try{   
    	for (i=0;i<this.elements.length;i++){  
	    	if (equals(this.elements[i].key,_key)){
      		bln=true;
    		}
    	}
   	}catch(e){
    	bln=false;    
   	}
   	return bln;
   },
    
   get:function(_key){    
      _key = _key + "";
      try{   
		    for (i=0;i<this.elements.length;i++){  
		    	if (equals(this.elements[i].key,_key)){
			      return this.elements[i].value;
			    }
		    }
	   }catch(e){
  	   return null;   
	   }
   },
   toObjectArray : function(){
   	var result = {};
   	var keys = this.keys();
   	for(var i=0;i<keys.length;i++){
   		result[keys[i]] = this.get(keys[i]);
   	}
   	return result;
   }
};

var Thread = function(runable,pTime){
	pTime = pTime || 10;
	this.start = function(){
		window.setTimeout(runable,pTime);
	}
}

/**
 * 事件的处理句柄
 */
var EventHandler = function(win,handler){
	if(win == null)
		return;
	this.win = win;
	if(!$.isFunction(handler)){
		this.handler = function(){
			return eval("win."+handler+".apply(this,arguments);")
		};
	}else
		this.handler = handler;
}

/**
 * 消息主题,用来处理系统消息
 */
var Topic = function(){
	this.eventHandlers = new Map();
	this.publications = new Map();
}
Topic.instance = null;//主题的单例对象
Topic.getInstance = function(){//主题的单例访问方法
	if(Topic.instance == null)
		Topic.instance = new Topic();
	return Topic.instance;
};

Topic.prototype = {
	/**
	 * 发布消息主题
	 * topic:主题名
	 * param:与主题的绑定的参数
	 */
	publish :function(topic,param){
		this.publications.put(topic,param);

		var handlers = this.eventHandlers.get(topic);
		if(handlers == null)
			return;
		
		for(var i=0;i<handlers.length;i++){
			window.setTimeout(function(){
				try{
					this(param);
				}catch(e){
				}
			}.bind(handlers[i].handler),10);
			
		}
	},
	/**
	 * 订阅消息主题
	 * topic:主题名
	 * eventHandler:用来处理消息的方法,它的参数来自于发布主题方
	 */
	subscribe:function(topic,eventHandler){
		var handlers = this.eventHandlers.get(topic);
		if(handlers == null)
			handlers = [];
		
		var _index = -1;
		for(var i=0;i<handlers.length;i++){
			if(handlers[i].win == eventHandler.win){
				_index = i;
				break;
			}
		}
		
		if(_index != -1){
			handlers[_index] = eventHandler;
		}else{
			handlers.push(eventHandler);
		}
		this.eventHandlers.put(topic,handlers);
		
		if(this.publications.containsKey(topic))
			eventHandler.handler(this.publications.get(topic));
		
	},
	/**
	 * 取消对指定主题的订阅
	 * topic:主题名
	 * sWindow:用来区分消息订阅者
	 */
	unSubscribe :function(topic,sWindow){
		if(sWindow == null)
			return;
		var handlers = this.eventHandlers.get(topic);
		if(handlers == null)
			return;
		for(var i=0;i<handlers.length;i++){
			if(handlers[i].win == sWindow){
				handlers.splice(i,1);
			}
		}
	},
	clearAll : function(){
		this.clearSubscriptions();
		this.clearPublications();
	},
	/**
	 * 清除所有的订阅者
	 */
	clearSubscriptions:function(){
		this.eventHandlers.clear();
	},
	/**
	 * 清除所有的发布物
	 */
	clearPublications:function(){
		this.publications.clear();
	}
}

var IFrameUtil = new function(){
	this.findIframeNode = function(sWindow){
		if(!sWindow)
			sWindow = window;
		var iframes = sWindow.parent.document.getElementsByTagName("IFRAME");
		var iframe = null;
		for(var i=0;i<iframes.length;i++){
			var elem = iframes[i];
			if(sWindow == elem.contentWindow){
				iframe = elem;
				break;
			}
		}
		return iframe;
	}
	
	this.makeResizeAble = function(iframe,isTop,callBack){
		var cWindow = iframe.contentWindow;
		window.setInterval(function(){
			try{
				var h = Number(cWindow.document.body.scrollHeight);
				if(!isIE() && !isTop)
					h = Number(cWindow.document.body.offsetHeight);
				iframe.style.height = h;
				
				if(callBack) callBack;
			}catch(e){}
		},50);
	}
	
	this.resize = function(sWindow){
		if(!sWindow)
			sWindow = window;
		var iframe = this.findIframeNode(sWindow);
		if(iframe != null && sWindow.document.body != null){
			var h = Number(sWindow.document.body.scrollHeight);
			iframe.style.height = h;
		}
	}

	this.publishEvent = function(topic,param,_parent){
		_parent = _parent || window;
		_parent.Topic.getInstance().publish(topic,param);
	}
	this.subscribeEvent = function(topic,win,fun,_parent){
		_parent = _parent || window;
		_parent.Topic.getInstance().subscribe(topic,new _parent.EventHandler(win,fun));
	}
	this.unSubscribeEvent = function(topic,win,_parent){
		_parent = _parent || window;
		_parent.Topic.getInstance().unSubscribe(topic,win);
	}
}

position=function(elem,pos) {
	var wnd = $(window), doc = $(document),_top=0, minTop = _top = doc.scrollTop(), left = doc.scrollLeft();
	if ($.inArray(pos, ['center','top','right','bottom','left']) >= 0) {
		pos = [pos == 'right' || pos == 'left' ? pos : 'center', pos == 'top' || pos == 'bottom' ? pos : 'middle'];
	}
	if (pos.constructor != Array) {
		pos == ['center', 'middle']
	}
	if (pos[0].constructor == Number) {
		left += pos[0];
	} else {
		switch (pos[0]) {
			case 'left':
				left += 0;
				break;
			case 'right':
				left += (wnd.width()) - (this.uiDialog.width());
				break;
			case 'center':
			default:
				left += (wnd.width() / 2) - (elem.width() / 2);
		}
	}
	if (pos[1].constructor == Number) {
		_top += pos[1];
	} else {
		switch (pos[1]) {
			case 'top':
				_top += 0;
				break;
			case 'bottom':
				_top += (wnd.height()) - (this.uiDialog.height());
				break;
			case 'middle':
			default:
				_top += (wnd.height() / 2) - (elem.height() / 2);
		}
	}
	_top = _top < minTop ? minTop : _top;
	elem.css({top: _top, left: left});
};



var Spry;
if(Spry){
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
					if(result.errorType == "org.jrest4guice.commons.exception.UserNotLoginException"){//用户没有登录
						//alert("由于您还没有登录本系统,所以当前的操作被拒绝!");
					}else if(result.errorType == "org.jrest4guice.commons.exception.AccessDeniedException"){//用户的操作权限受限
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
			option = option || {};
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
			
			var td = $("<td style=\"width: 24px;\" class='tdCkb_"+rowId+"'><input id='tdCkb_"+rowId+"' type=\"checkbox\" style=\"display: none;width: 24px;\"/></td>");
			tr[0].insertBefore(td[0],tr[0].firstChild);
		
			if(!option.onKeyDown){
				SpryExt.DomHelper.disableSelection(tr[0]);
				tr.find("td").each(function(){
					SpryExt.DomHelper.disableSelection(this);
				});
			}else{
				SpryExt.DomHelper.enableSelection(tr[0]);
				tr.find("td").each(function(){
					SpryExt.DomHelper.enableSelection(this);
				});
			}
	
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
			}).click(function(evt){//鼠标单击
				if(!(evt.ctrlKey || evt.shiftKey))
					_self.uncheckAll(true);
					
				fun.call(this,evt);
				if(option.onclick){
					option.onclick.call(this,evt);
				}
			}).keydown(function(evt){
				if(option.onKeyDown){
					option.onKeyDown.call(this,evt);
				}
			});
	
			var _find = false;
			for(var i =0;i<checkedIds.length;i++){
				if(rowId==checkedIds[i]){
					_find = true;
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
		checkById:function(id){
			var ckb = this.table.tbody.find("td.tdCkb_"+id+" input:first");
			if(ckb.length != 0){
				ckb = ckb[0];
				ckb.checked = true;
				this._checkCurrent(ckb,true);
			}
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
				loadingBar = $("<div id=\"loading\" class=\"loadingBar\" style=\"display: none;\">&nbsp;加载数据...</div>").appendTo("body");
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
	
	SpryExt.PageInfoBar.maxPageCount = 3;
	SpryExt.PageInfoBar.pageSize = 12;
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
		document.getElementById(infoBar).innerHTML = "&nbsp;&nbsp;"+pageInfo.resultCount + " "+msg+"，共 " + pageCount + " 页";
		var navigation = document.getElementById(navigations);
		
		navigation.innerHTML = "";
		
		//定制上一页
		var textPerPage = document.createElement("span"); 
		textPerPage.style.width = "50px";
		textPerPage.innerHTML = "上一页";
		textPerPage.className="pageClass";
		if(pageInfo.pageIndex > 1) {
			textPerPage.style.visibility="visible";
			textPerPage.onclick =  function(){goPage(parseInt(this.toString()));}.bind(pageInfo.pageIndex -1); 
		}else{
			textPerPage.style.visibility="hidden";
		}
		navigation.appendChild(textPerPage);
		var pageSpan = document.createElement("span");
		pageSpan.className="pageClass";
		var _index = 1;
		if(pageInfo.pageIndex>maxPageCount)
			_index = pageInfo.pageIndex-maxPageCount+1;
			
		for(var i=_index;i<maxPageCount+_index;i++){
			if(i>pageCount) break;
			pageSpanClone = pageSpan.cloneNode(true);
			pageSpanClone.innerHTML = " "+(i)+" ";
			if(i == pageInfo.pageIndex)
				pageSpanClone.className="pageClassd";	
			else
				pageSpanClone.onclick = function(){
					goPage(parseInt(this.toString()));
				}.bind(i);
			navigation.appendChild(pageSpanClone);
		}
		//定制下一页
		var textNextPage = document.createElement("span");
		textNextPage.style.width = "50px";
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
	
	SpryExt.security = {}
	
	SpryExt.security.currentUser = null;
	SpryExt.security.currentUserRoles = null;
	
	SpryExt.security.clear = function(){
		spry.security.currentUser = null;
		spry.security.currentUserRoles = null;
	}
	
	SpryExt.security.doFilter = function(){
		
	}
}