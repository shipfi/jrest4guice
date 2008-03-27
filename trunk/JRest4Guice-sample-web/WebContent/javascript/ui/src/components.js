TabPanel = function(id,tEngine){
	this.tabId = id;
	this.tEngine = tEngine;
	this.templateBody = null;
	this.csTab = null;
	
	this.element = $.elem(this.tabId);

	//保存当前ID的实例引用到JS全局上下文
	eval("_"+id+"=this");
}

TabPanel.prototype = {
	init:function(_ds,callBack){
		var _self = this;
		var _callBack = callBack || (function(){
			_self.selectTabByIndex(1);
		});
		callBack = function(){
			_callBack();
			IFrameUtil.resize();
		}.bind(this);
		this.tEngine.bind($.elem(this.tabId),_ds,this.extensions,callBack);
		this.templateBody = this.tEngine.tempateBodies.get(this.tabId);
		if(this.templateBody == null)
			throw "没有初始化TabPanel\""+this.tabId+"\"对象";
	},
	/**
	 * 根据序号选择指定的tab页
	 * index
	 * 	序号
	 * param
	 * 	页的附加参数,可为NULL,通常在内容区为IFrame时才用到
	 */
	selectTabByIndex:function(index,param){
		index = Number(index)-1;
		var elem = this.element.childNodes[index];
		this.selectTab(elem,param);
	},
	/**
	 * 选择指定的tab页
	 * tab
	 * 	tab页
	 * param
	 * 	页的附加参数,可为NULL,通常在内容区为IFrame时才用到
	 */
	selectTab:function(tab,param){
		if(!tab)
			return;
		if(this.csTab != null){
			this.csTab.className = "tab_menu_style";
			this.csTab.onmouseover =this.csTab.onmouseover_bak;
			this.csTab.onmouseout = this.csTab.onmouseout_bak;
			this._updateControlButton(this.csTab,false);
		}

		this.csTab = tab;

		this.csTab.className = "tab_menu_mouseclick";		
		if(!this.csTab.onmouseover_bak){
			this.csTab.onmouseover_bak = this.csTab.onmouseover;
			this.csTab.onmouseout_bak = this.csTab.onmouseout;
		}
		this.csTab.onmouseover = this.csTab.onmouseout = null;
		this._updateControlButton(this.csTab,true);
		
		//触发用户自定义的Tab选择事件
		this.onTabSelected(this.csTab,param);
	},
	/**
	 * 更新与Tab控制相关按钮的状态
	 */
	_updateControlButton:function(tab,visible){
		var removeButton = document.getElementsByUIName("removeButton",tab);
		if(removeButton){
			removeButton = removeButton[0];
			removeButton.style.display = visible?"":"none";
		}
	},
	/**
	 * 删除指定的tab页
	 */
	removeTab:function(tab){
		if(!tab)
			return;
		
		var tabId = tab.getAttribute("tabId");
		//获取前一个tab页
		var preNode = tab.previousSibling;
		
		//删除当前tab页
		this.element.removeChild(tab);
		//触发用户自定义的Tab删除事件
		this.onTabRemoved(tabId);
		//选择前一个tab页
		if(preNode && preNode.getAttribute("tabId")!=null)
			this.selectTab(preNode);
	},
	onTabSelected:function(tab,param){
	},
	onTabRemoved:function(tabId){
	}
}

//=================================================
// 折叠工具条
//=================================================
Accordion = function(id,tEngine){
	this.barId = id;
	this.tEngine = tEngine;
	this.templateBody = null;
	this.element = $.elem(this.barId);

	//保存当前ID的实例引用到JS全局上下文
	eval("_"+id+"=this");
}

Accordion.prototype = {
	init:function(_ds,callBack){
		var _self = this;
		var _callBack = callBack || (function(){
			_self.selectBarByIndex(1);
		});
		callBack = function(){
			_self.element = $.elem(_self.barId);
			_callBack();
			IFrameUtil.resize();
		}.bind(this);
		this.tEngine.bind($.elem(this.barId),_ds,this.extensions,callBack);
		this.templateBody = this.tEngine.tempateBodies.get(this.barId);
		if(this.templateBody == null)
			throw "没有初始化Accordion\""+id+"\"对象";
	},
	/**
	 * 根据序号选择指定的bar项
	 * index
	 * 	序号
	 * param
	 * 	bar的附加参数,可为NULL,通常在内容区为IFrame时才用到
	 */
	selectBarByIndex:function(index,param){
		index = Number(index)*2-2;
		var bar = this.element.tBodies[0].rows[index];
		this.selectBar(bar,param);
	},
	/**
	 * 选择指定的bar项
	 * bar
	 * 	bar项
	 * param
	 * 	bar的附加参数,可为NULL,通常在内容区为IFrame时才用到
	 */
	selectBar:function(bar,param){
		if(this.csBar){
			this.csBar.parentNode.style.display = "none";	
		}
		
		var detail = bar.getAttribute("detail");
		if(!detail)
			return;
		
		this.csBar = $.elem(detail);
		this.csBar.parentNode.style.display = "";

		//触发用户自定义的Bar选择事件
		this.onBarSelected(this.csBar,param);
	},
	onBarSelected:function(bar,param){
	}
}

/**
 * 模拟下拉列表
 * 此控件基于表格控件
 */
var SelectList = function(dom,dom2,ds,index,callback){
	this.init(dom,dom2,ds,index,callback);
}

//=================================================
// 下拉列表控件
//=================================================
SelectList.prototype = {
	init:function(dom,dom2,ds,index,callback){
		this.rowIndex = 0;
		dom.className = "select";
		this.createList(dom,dom2,ds,index,callback);//创建一个下拉列表
	},
	createList:function(dom,dom2,ds,index,callback){
//		var div = document.createElement("DIV");
//		div.appendChild($.elem(dom2));
//		dom.parentNode.appendChild(div);
		new TableView(dom2,tEngine);
		eval("_"+dom2).init(ds,function(){
			this._self._selectIt(dom2);
			var trs = $("#"+dom2 + " tr");
			var trLan = trs.length;
			for(var i=0;i<trLan;i++){
				trs[i].onmouseover = function(){
					this._self.rowIndex = this.obj.rowIndex;
					this._self._selectIt(dom2);
				}.bind({_self:this._self,obj:trs[i]})
				trs[i].onmousedown = function(){
					this._self._inset(dom,this.obj,callback);
				}.bind({_self:this._self,obj:trs[i]})
			}
			dom.style.cursor = "default";
			div = $.elem(dom2);
			div.className = "SelectList";
			div.style.display = "none";
			dom.onfocus = function(){
				doms = dom;
				var offsetLeft = doms.offsetLeft;
				var offsetTop = doms.offsetTop;
				
				while( doms = doms.offsetParent) 
			    { 
			        offsetTop += doms.offsetTop; 
			        offsetLeft += doms.offsetLeft; 
			    } 
				
				this.style.left = offsetLeft;
				this.style.top = offsetTop + dom.offsetHeight;
				this.style.width = dom.offsetWidth;
				this.style.display = "";
			}.bind(div);
			dom.onblur = function(){
				this.style.display = "none";
			}.bind(div);
		}.bind({_self:this}));
		
	},
	_selectIt:function(dom){
		var row = $('#'+dom+' tr')
		for(var i=0;i<row.length;i++){
			if(row[i].rowIndex == this.rowIndex+""){
				row[i].className = "selectAutocompleter";
			}else{
				row[i].className = "selectAuto";
			}
		}
	},
	_inset:function(dom,dom2,callback){
		dom.value = isIE()?dom2.innerText:dom2.textContent.replace(/\s/g,"");
		if(callback)
			callback(isIE()?dom2.innerText:dom2.textContent.replace(/\s/g,""),dom);
	}
}

//=================================================
// 表格控件
//=================================================
TableView = function(id,tEngine,extensions){
	this.tableId = id;
	this.tEngine = tEngine;
	this.templateBody = null;
	this.selectedRow =null;
	
	
	this.extensions = extensions||{};
	if(this.extensions.sortAble==null)
		this.extensions.sortAble = true;

	//初始化表格装饰器
	new TableDecorator(this.tableId);
	
	//保存当前ID的实例引用到JS全局上下文
	eval("_"+id+"=this");
}

TableView.prototype = {
	init:function(jsonDS,callBack){
		var tableElem = $.elem(this.tableId);
		tableElem.style.display = "";
		var _callBack = callBack || (function(){});
		callBack = function(){
			this.templateBody = this.tEngine.tempateBodies.get(this.tableId);
			if(tableElem.tagName == "TABLE"){
				$("#"+this.tableId)
				.css({borderLeft:"1px solid #ededed",borderRight:"1px solid #ededed"})
				.find("tbody").show().find("tr").each(function(){
					$(this).find("td:last").css({backgroundImage:"url('')"});
				});
				$("#"+this.tableId).find("thead tr td:last").css({backgroundPositionX:"-1px"});
			}
			
			IFrameUtil.resize();
			this.datas = this.templateBody.jsonXPath.data;
			if(!lang.isArray(this.datas))
				this.datas = [this.datas];
			//装饰表格的复选功能
			if(this.extensions.checkAbleOption)
				eval(this.tableId+"_decorator.decorateRow(this.extensions.checkAbleOption)");

			//装饰头部的排序功能
			eval(this.tableId+"_decorator.decorateHead(this.extensions)");

			//执行用户的回调方法
			_callBack();
		}.bind(this);
		
		this.callBack = callBack;
		this.tEngine.bind(tableElem,jsonDS,this.extensions,callBack);
	},
	/**
	 * 返回field值等于value的用户数据对象
	 * field
	 * 	数据对象的属性名称
	 * value
	 * 	属性值
	 */
	getUserObject:function(field,value){
		if(value.tagName)
			value = value.getAttribute("rowId");
		var len = this.datas.length;
		var result = null;
		var _v;
		for(var i=0;i<len;i++){
			result = this.datas[i];
			_v = eval("result."+field);
			if(value == _v){
				break;
			}
		}
		return result;
	},
	/**
	 * 修改field值等于value的用户数据对象为obj
	 * field
	 * 	数据对象的属性名称
	 * value
	 * 	属性值
	 * obj
	 * 	新的数据对象
	 * elem
	 *  需要刷新数据的节点
	 */
	updateUserObject:function(field,value,obj,elem){
		var len = this.datas.length;
		var tObj = null;
		for(var i=0;i<len;i++){
			tObj = this.datas[i];
			if(value == tObj[field]){
				this.datas[i] = obj;
				this.updateRow(obj,elem);
				break;
			}
		}
	},
	
	/**
	 * 选择指定行
	 */
	selectRow:function(elem){
		this.selectedRow = elem;
	},
	getSelectRowUserObject:function(key){
		if(!this.selectedRow)
			return null;
		if(!key)
			key = "id";
		return this.getUserObject(key,this.selectedRow);
		
	},
	/**
	 * 删除指定行
	 */
	removeRow:function(elem,callBack){
		var tb = $.elem(this.tableId).tBodies[0];
		tb.removeChild(elem);
		if(callBack){
			callBack(tb,document.getElementById(elem.getAttribute("detail")));
		}
		this.selectedRow = null;
	},
	appendRow:function(data){
		return this._addOrReplaceRow(data,true);
	},
	insertRow:function(data,elem){
		if(elem == null)
			elem = this.selectedRow;		
		return this._addOrReplaceRow(data,true,elem);
	},
	updateRow:function(data,elem){
		elem = elem || this.selectedRow;
		if(elem){
			var newRow = this._addOrReplaceRow(data,false,elem);
			//装饰表格的复选功能
			if(this.extensions.checkAbleOption){
				var tr = $(newRow);
				var checkedIds = [tr.attr("rowId")];
				eval(this.tableId+"_decorator.checkAbleOption.checkedIds = checkedIds;");
				//重新装饰当前行
				eval(this.tableId+"_decorator.decorateSingleRow(tr,true)");
			}
			return newRow;
		}
	},
	_addOrReplaceRow:function(data,add,elem){
		if(add)
			data["userDataRowIndex"] = $.elem(this.tableId).rows.length;
		else
			data["userDataRowIndex"] = this.selectedRow.rowIndex;

		this.datas[data["userDataRowIndex"]-1] = data;

		var datas = [];
		this.tEngine._defaultBuildItems(this.templateBody,datas,[data]);
		var tmpDiv = document.createElement("DIV");
		tmpDiv.innerHTML = "<table id='tmpTable'>"+datas.join("")+"</table>";
		document.body.appendChild(tmpDiv);
		var rows = $.elem("tmpTable").rows;
		var newRow;
		var cRow = this.selectedRow;
		var sibling = this.selectedRow?this.selectedRow.nextSibling:null;
		for(var i=0;i<rows.length;i++){
			newRow = rows[i].cloneNode(true);
			try{
				if(elem){
					if(add){
						$.elem(this.tableId).tBodies[0].insertBefore(newRow,cRow);
					}else{
						$.elem(this.tableId).tBodies[0].replaceChild(newRow,cRow);
						cRow = sibling;
						if(cRow != null)
							sibling = sibling.nextSibling;						
					}

				}else{
					$.elem(this.tableId).tBodies[0].appendChild(newRow);
				}
			}catch(e){
//				alert("请选择要处理的行");
			}
		}
		document.body.removeChild(tmpDiv);
		
		return newRow;
	}
}

var treeImage = {
	setRootPath:function(path){
		this.blank =  path+"blank.gif";
		this.plus = path+"plus.gif";
		this.plus1 = path+"plus1.gif";
		this.plus2 = path+"plus2.gif";
		this.plus3 = path+"plus3.gif";
		this.plus4 = path+"plus4.gif";
		this.plus5 = path+"plus5.gif";
		
		this.minus = path+"minus.gif";
		this.minus1 = path+"minus1.gif";
		this.minus2 = path+"minus2.gif";
		this.minus3 = path+"minus3.gif";
		this.minus4 = path+"minus4.gif";
		this.minus5 = path+"minus5.gif";

		this.line = path+"line.gif";
		this.line1 = path+"line1.gif";
		this.line2 = path+"line2.gif";
		this.line3 = path+"line3.gif";
		this.line4 = path+"line4.gif";
		this.folder = path+"vcard.png";
		this.folderOpen = path+"vcard.png";
		this.leafe = path+"leaf.gif";
		this.waitting = path+"waitting.gif";
	}
}
treeImage.setRootPath(sys.imagePath);

//=================================================
// 树控件
//=================================================
TreeView = function(id,tEngine,extensions){
	this.treeId = id;
	this.tEngine = tEngine;
	this.currentSelectedTreeNode = null;
	this._treeNodeClass = "_treeNode";
	extensions = extensions || {};

	this.extensions = {};
	this.extensions.pKey = extensions.pKey || "id";
	this.extensions.enableSort = extensions.enableSort != null?extensions.enableSort:true;//排序开关，缺省为true	
	this.extensions.childrenKey = extensions.childrenKey || "children";
	this.extensions.leafKey = extensions.leafKey || "isLeafe";
	this.extensions.showGrid = extensions.showGrid || false;//是否显示连线
	this.extensions.buildItems = this.tEngine._buildTreeItems = (extensions.buildItems || this.__buildTreeItems.bind(this.tEngine));
	this.tEngine._generateTreeNodeDecorator = this._generateTreeNodeDecorator.bind(this.tEngine);
	
	this.templateBody = null;

	//保存当前ID的实例引用到JS全局上下文
	eval("_"+id+"=this;");

	sys.imagePath = window.top.themes_path+"/image/tree/";
	
	this.datas = new Map();
}

TreeView.prototype = {
	init:function(jsonDS,callBack){
		//设置树的图标路径
		treeImage.setRootPath(sys.imagePath);

		var _callBack = callBack;
		callBack = function(){
			$.elem(this.treeId).style.display = "";
			IFrameUtil.resize();
			this.templateBody = this.tEngine.tempateBodies.get(this.treeId+"");
			this.rootNode = this.templateBody.loopBody.firstChild;
			this.expandTreeNodeByPath("");
			_callBack();
		}.bind(this);

		this.tEngine.bind($.elem(this.treeId),jsonDS,this.extensions,callBack);
	},
	/**
	 * 装载子节点(通常用在对树的懒装载方面)
	 */
	loadChild:function(node,srcNodeId){
		node = node || this.currentSelectedTreeNode;
		if(node == null)
			return;

		var isLoaded = node.getAttribute("isLoaded")=="true"?true:false;
		if(!isLoaded){
			var _self = this;
			//根据用户改写的doLoadChild方法,获取当前节点下的子节点
			var callBack = function(children){
				if(children != null){
					var old_children = _self.getChildTreeNodes(node);
					var len = old_children.length;
					for(var i=0;i<len;i++){
						node.removeChild(old_children[i]);
					}
					//设置当前的节点已经被装载
					node.setAttribute("isLoaded","true");
					var lastChild = node.childNodes[1];
					//显示提示信息
					sys.innerMesg(_self.treeId,"请稍候,正在加载子节点....",document.body,lastChild);
					window.setTimeout(function(){
						var nodes = [];
						if(!lang.isArray(children))
							children = [children];
						var len = children.length;
						for(var i=0;i<len;i++){
							_self.addChild(children[i],node,(i==len-1));
						}
						//关闭提示信息
						sys.finish(_self.treeId);
						
						if(srcNodeId)
							_self.expandTreeNode(_self.getNodeById(srcNodeId),null,true,null,false,true);
					},1);
				}else
					//设置当前的节点已经被装载
					node.setAttribute("isLoaded","true");
			};
			if(this.doLoadChildEx)
				this.doLoadChildEx(node,callBack);
			else
				this.doLoadChild(node.getAttribute("nodeId"),callBack);
		}
	},
	/**
	 * 重新加载子节点
	 */
	reloadChild:function(node,srcNodeId){
		node = node || this.currentSelectedTreeNode;
		if(node == null)
			return;

		node.setAttribute("isLoaded","false");
		this.loadChild(node,srcNodeId);
	},
	/**
	 * 在当前事件触发源所在的树节点下添加一个新节点
	 * data
	 * 	节点数据
	 * event
	 * 	事件的触发源
	 */
	addChildAtCP:function(data,event){
		if(!event)
			return;
		var pNode = htmlDom.parentElemByCssName(EventUtil.getEventTarget(event),this._treeNodeClass);
		if(pNode)
			this.addChild(data,pNode);
	},
	/**
	 * 添加子节点
	 */
	addChild:function(data,pNode,isLast){
		if(isLast==null)
			isLast = true;

		pNode = pNode || this.currentSelectedTreeNode;
		var isLeaf = this._isLeaf(pNode);
		
		//展开当前节点
		this.expandTreeNode(pNode,null,true);
		//构造层次信息
		var levelInfo = pNode.getAttribute("levelInfo");
		if(levelInfo != null){
			levelInfo = levelInfo.split("/");
			var len = levelInfo.length;
			for(var i=0;i<len;i++){
				levelInfo[i] = levelInfo[i]=="true"?true:false;
			}
		}else
			levelInfo = [true];

		//返塑treePath
		var treePath = pNode.getAttribute("treePath");
		if(treePath)
			treePath = treePath.split("/");
		else
			 treePath = [data[this.templateBody.extensions.childrenKey]];
		
		//构造父节点的信息
		var parentData = {_treeLevel:Number(pNode.getAttribute("level"))};
		data._levelInfo = levelInfo;
		data._isLast = isLast;
		data._treePath = treePath;
		
		//从模板中生成新节点的字符串
		var nodes = [];
		this.extensions.buildItems(this.templateBody,nodes,data,parentData,true);
		//构造新节点
		var newNode = document.createElement("DIV");
		newNode.innerHTML = nodes.join("");
		//插入新节点
		pNode.appendChild(newNode.firstChild);
		//变更节点的连线
		this._changeTreeNodeLineImg(pNode.lastChild.previousSibling,false,false);
		
		if(isLeaf){//如果是叶子节点,则设置父节点为目录类型,并替换连线
			pNode.childNodes[1].firstChild.setAttribute("isLeaf","false");
			this.changeTreeNodeState(pNode,true);
			this.refreshNode();
		}
	},
	/**
	 * 刷新当前选择的节点
	 */
	refreshNode:function(node){
		node = node || this.currentSelectedTreeNode;
		if(!node)
			return;
		
		var data = this.datas.get(this.getNodeId(node));
		if(data != null){
			data[this.extensions.leafKey] = false;
			this.updateTreeNode(data,node);
		}
		
	},


	/**
	 * 修改当前事件触发源所在的树节点
	 * data
	 * 	节点数据
	 * event
	 * 	事件的触发源
	 */
	updateTreeNodeAtCP:function(data,event){
		if(!event)
			return;
		var cNode = htmlDom.parentElemByCssName(EventUtil.getEventTarget(event),this._treeNodeClass);
		if(cNode)
			this.updateTreeNode(data,cNode);
	},
	/**
	 * 修改指定的子节点
	 */
	updateTreeNode:function(data,node){
		node = node || this.currentSelectedTreeNode;
		if(node == null)
			return;
		data._MODIFIERS = customModifiers;
		data._self = data;
		data.isLeafe = data.isLeaf = this._isLeaf(node);
		//获取节点的模板
		var template = this.tEngine._getTemplate(this.templateBody,data)[0];
		var html = template.process(data);
		//重新构造节点
		node.childNodes[1].innerHTML = html;
		this.expandTreeNode(node,null,this.isExpanded(node));
	},
	/**
	 * 删除当前事件触发源所在的树节点
	 * data
	 * 	节点数据
	 * event
	 * 	事件的触发源
	 */
	removeTreeNodeAtCP:function(data,event){
		if(!event)
			return;
		var cNode = htmlDom.parentElemByCssName(EventUtil.getEventTarget(event),this._treeNodeClass);
		if(cNode)
			this.removeTreeNode(data,cNode);
	},
	/**
	 * 删除子当前选择的节点
	 */
	removeTreeNode:function(node){
		node = node || this.currentSelectedTreeNode;
		if(node == null)
			return;
		var pNode = node.parentNode;
		if(pNode.className!=this._treeNodeClass)
			return;
		
		var cNode = node;
		//保存前一个兄弟节点
		var previousNode = this.getPreviousSiblingNode(cNode);
		//选择上一个节点
		this.goPreviousNode();
		//删除当前节点
		pNode.removeChild(cNode);

		//设置父节点状态(是否是叶子)
		this._setIsLeaf(pNode);

		//检查父节点下是否没有子节点
		if(pNode.lastChild.className!=this._treeNodeClass){
			var nodeImagElem = document.getElementsByUIName("nodeImag",pNode.childNodes[1]);
			if(nodeImagElem){
				nodeImagElem = nodeImagElem[0];
				if(changeImage)
					nodeImagElem.src = changeImage(true);
				else
					nodeImagElem.src = treeImage.leafe;
			}else{
				//调用用户的自定义处理函数
				this.onStateChange(node,false,true);
			}
			//更新节点的连线
			this._changeTreeNodeLineImg(pNode,this._isLastTreeNode(pNode),true);
		}else{
			//更新节点的连线
			this._changeTreeNodeLineImg(pNode.lastChild,true,true);
		}
		//变更节点的打开状态图标信息
		this._changeTreeNodeExpandImg(previousNode);
	},
	/**
	 * 获取指定树节点下的子节点，不级联
	 */
	getChildTreeNodes:function(node){
		var children = $A(node.childNodes);
		children.splice(0,2);
		return children;
	},
	/**
	 * 选择指定的节点
	 */
	selectTreeNode:function(event,node,fireEvent){
		if(fireEvent == null)
			fireEvent = true;

		if(this.currentSelectedTreeNode != null){
			this._changeTreeNodeStyle(true);
		}
		this.currentSelectedTreeNode = node;
		this._changeTreeNodeStyle(false);
		
		//触发节点被选中的事件
		if(fireEvent)
			this.onTreeNodeSelected(this.currentSelectedTreeNode);
		
		EventUtil.cancelBubble(event);
	},
	/**
	 * 获取相对当前节点的下一个节点
	 */
	getNextNode:function(){
		if(this.currentSelectedTreeNode == null)
			this.currentSelectedTreeNode = this.rootNode;

		var nextNode;
		var children = this.getChildTreeNodes(this.currentSelectedTreeNode);
		//取第一个子节点
		if(children != null && children.length>0)
			nextNode = children[0];
		//取下一个同级节点
		if(nextNode == null){
			nextNode = this.currentSelectedTreeNode.nextSibling;
		}
		//取父节点的下一个同级节点
		if(nextNode == null){
			var pNode = this.currentSelectedTreeNode.parentNode;
			while(nextNode==null){
				if(pNode==null || pNode.className!=this._treeNodeClass)
					break;
				nextNode = pNode.nextSibling;
				pNode = pNode.parentNode;
			}
		}
		return nextNode;		
	},
	/**
	 * 转向到下一个节点
	 */
	goNextNode:function(){
		var nextNode = this.getNextNode();
		if(nextNode != null){
			this.expandTreeNode(this.currentSelectedTreeNode,null,true);
			this.expandTreeNode(nextNode,null,true);
		}
	},
	/**
	 * 获取前一个同胞节点
	 */
	getPreviousSiblingNode:function(node){
		return this._getSiblingNode(node.previousSibling);
	},
	/**
	 * 获取下一个同胞节点
	 */
	getNextSiblingNode:function(node){
		return this._getSiblingNode(node.nextSibling);
	},
	/**
	 * 获取相对当前节点的上一个节点
	 */
	getPreviousNode:function(){
		if(this.currentSelectedTreeNode == null)
			this.currentSelectedTreeNode = this.rootNode;

		var previousNode = this.getPreviousSiblingNode(this.currentSelectedTreeNode);
		
		if(previousNode && this._isLeaf(previousNode))
			return previousNode;

		//取父节点
		if(previousNode == null || previousNode.className!=this._treeNodeClass){
			var pNode = this.currentSelectedTreeNode.parentNode;
			if(pNode != null && pNode.className ==this._treeNodeClass)
				previousNode = pNode;
		}else{//如果同级节点存在,则尝试转向到前一个同级节点的最后一个节点
			if(previousNode!=null && previousNode != previousNode.parentNode.firstChild){
				if(!this.isExpanded(previousNode))
					return previousNode;
				var lChild = previousNode.lastChild;
				while(lChild != null && lChild.className ==this._treeNodeClass){
					if(lChild.lastChild.className!=this._treeNodeClass || !this.isExpanded(lChild)){
						break;
					}
					lChild = lChild.lastChild;
				}
				
				if(lChild.className ==this._treeNodeClass 
				&& lChild.getAttribute("level")!=this.currentSelectedTreeNode.getAttribute("level")
				&&lChild!=this.currentSelectedTreeNode.lastChild)
					previousNode = lChild;
			}
		}
		return previousNode;
	},
	/**
	 * 转向到上一个节点
	 */
	goPreviousNode:function(){
		var previousNode = this.getPreviousNode();
		if(previousNode != null){
			this.selectTreeNode(null,previousNode);
		}
	},
	/**
	 * 根据路径信息,展开指定的节点
	 * path:路径(如: 1/2 :表示要展开第一层的第一个子节点下的第二个子节点)
	 * all:表示是否要展开所有的子节点
	 */
	expandTreeNodeByPath:function(path,all){
		var levels;
		if(path.indexOf("/")!=-1)
			levels = path.split("/");
		else
			levels = [];
		var level;
		var node;
		try{
			node = this.rootNode;
			var len = levels.length;
			this.expandTreeNode(node,all,true,null,len<1);
			for(var i=0;i<len;i++){
				level = Number(levels[i]);
				node = node.childNodes[level+1];
				this.expandTreeNode(node,all,true,null,i==len-1);
			}
		}catch(e){}
	},
	/**
	 * 展开treeNode
	 */
	expandTreeNode:function(node,all,state,event,fireEvent,cascadParent){
		if(node == null)
			return;

		if(!node.tagName){
			node = this.treeId+"_"+node;
			node = $.elem(node).parentNode.parentNode;
		}
		
		if(cascadParent){
			//检查父节点有没有展开
			if(!this.isExpanded(node))
				this.expandTreeNode(this._getParentTreeNode(node),all,true,event,fireEvent);
		}

		if(fireEvent==null)
			fireEvent = true;
		//装载子节点(如果当前节点还没有被装载
		this.loadChild(node);

		var expanded;
		if(state != null){
			expanded = state;
		}else{
			var expanded　=　!this.isExpanded(node);
		}
		
		var children;
		if(all){
			children = document.getElementsByClassName(this._treeNodeClass,node);
		}else{
			children = this.getChildTreeNodes(node);
		}
		
		for(var i=0;i<children.length;i++){
			children[i].style.display = expanded?"":"none";
			if(all)
				this.changeTreeNodeState(children[i],expanded,null,fireEvent);
		}
	
		this.changeTreeNodeState(node,expanded,event,fireEvent);
		//触发节点被展开的事件
		this.onTreeNodeExpanded(node);
	},
	/**
	 * 变更树节点的状态及图标
	 */
	changeTreeNodeState:function(node,expanded,event,fireEvent){
		var isLeaf = this._isLeaf(node);
	
		node.setAttribute("expanded",expanded?"true":"false");
	
		var imgElem = node.firstChild.lastChild;
	
		if(imgElem && !isLeaf){
			imgElem.src = expanded?imgElem.getAttribute("close_src"):imgElem.getAttribute("expand_src");
		}
		
		if(!isLeaf){
			var nodeImagElem = document.getElementsByUIName("nodeImag",node.childNodes[1]);
			if(nodeImagElem){
				nodeImagElem = nodeImagElem[0];
				if(nodeImagElem.getAttribute("oldSrc")==null){
					nodeImagElem.setAttribute("oldSrc",nodeImagElem.src);
				}
				nodeImagElem.src = expanded?treeImage.folderOpen:treeImage.folder;
			}else{
				//调用用户的自定义处理函数
				this.onStateChange(node,expanded,isLeaf);
			}
		}
		if(!event)
			this.selectTreeNode(null,node,fireEvent);
		else{
			var tPath = this.currentSelectedTreeNode.getAttribute("treePath");
			if(tPath && tPath.indexOf(node.getAttribute("treePath"))!=-1){
				this.selectTreeNode(null,node,fireEvent);
			}
			EventUtil.cancelBubble(event);
		}
	},
	/**
	 * 展开所有节点
	 */
	expandAll:function(){
		DurationTimeDebug.start();
		this.expandTreeNode(this.rootNode,true,true);
		DurationTimeDebug.finish("展开所有节点耗时",$.elem("infoArea"));
	},
	/**
	 * 关闭所有节点
	 */
	closeAll:function(){
		DurationTimeDebug.start();
		this.expandTreeNode(this.rootNode,true,false);
		DurationTimeDebug.finish("关闭所有节点耗时",$.elem("infoArea"));
	},

	_getSiblingNode:function(node){
		if(this._isTreeNode(node))
			return node;
		return null;
	},
	_isTreeNode:function(elem){
		return elem != null && elem.className==this._treeNodeClass;
	},

	/**
	 * 变更新增树节点所导致的前一兄弟节点下的所有子节点的连线
	 */
	_changeTreeNodeLineImg:function(node,isLast,clearPrevious){
		if(node != null && node.className == this._treeNodeClass){
			if(this._isLeaf(node)){
				node.firstChild.lastChild.src = isLast?treeImage.line2:treeImage.line3;
			}
			
			var _self = this;
			var children = document.getElementsByClassName(this._treeNodeClass,node,function(child){
				var level = Number(node.getAttribute("level"));
				child.firstChild.childNodes[level].src = clearPrevious?treeImage.blank:treeImage.line1;
				var levelInfo = _self._getLevelInfo(child);
				levelInfo[level] = clearPrevious?"true":"false";
				child.setAttribute("levelInfo",levelInfo.join("/"));
			});
			
			var levelInfo = this._getLevelInfo(node);
			try{
				levelInfo[Number(node.getAttribute("level"))]="false";
				node.setAttribute("levelInfo",levelInfo.join("/"));
			}catch(e){}
		}
		
	},
	/**
	 * 获取节点的层次信息
	 */
	_getLevelInfo:function(node){
		var levelInfo = node.getAttribute("levelInfo");
		return levelInfo.split("/");
	},
	/**
	 * 改变节点的显示样式
	 */
	_changeTreeNodeStyle:function(clear){
		var nodeText = document.getElementsByUIName("nodeText",this.currentSelectedTreeNode.childNodes[1]);
		if(nodeText){
			nodeText = nodeText[0];
			nodeText.className = clear?"treeNodeUnSelected":"treeNodeSelected";
		}else{
			//调用用户自定义的样式变更函数
			this.onStyleChange(this.currentSelectedTreeNode,clear);
		}
	},
	/**
	 * 检测节点是否是叶子
	 */
	_isLeaf:function(node){
		var isleaf = node.childNodes[1].firstChild.getAttribute("isleaf");
		if(isleaf != null)
			return (isleaf=="true"||isleaf==true)?true:false;
		else
			return node.childNodes.length==2;
	},
	_setIsLeaf:function(node){
		var isleaf = node.childNodes.length==2?"true":"false";
		node.childNodes[1].firstChild.setAttribute("isleaf",isleaf);
		if(isleaf)
			this.refreshNode(node);
	},
	/**
	 * 获取父节点
	 */
	_getParentTreeNode:function(node){
		var pNode = node.parentNode;
		if(pNode != null && pNode.className == this._treeNodeClass)
			return pNode;
		return null;
	},
	/**
	 * 检测节点是否是最后一个子节点(同级目录下)
	 */
	_isLastTreeNode:function(node){
		var pNode = this._getParentTreeNode(node);
		return (pNode != null)&&pNode.lastChild==node;
	},
	/**
	 * 检测节点是否展开
	 */
	isExpanded:function(node){
		var expanded = node.getAttribute("expanded");
		return expanded=="true"?true:false;
	},
	/**
	 * 更改节点的展开状态图标
	 */
	_changeTreeNodeExpandImg:function(node){
		if(node == null || this._isLeaf(node))
			return;
		var imgElem = node.firstChild.lastChild;
	
		if(imgElem){
			var isLast = this._isLastTreeNode(node);
			imgElem.setAttribute("expand_src",isLast?treeImage.plus2:treeImage.plus3)
			imgElem.setAttribute("close_src",isLast?treeImage.minus2:treeImage.minus3)
			imgElem.src = this.isExpanded(node)?imgElem.getAttribute("close_src"):imgElem.getAttribute("expand_src");
		}
	},
	/**
	 * 生成树节点前面部分的Html字符（包括空图标及＋/－）
	 */
	_generateTreeNodeDecorator:function(treeId,level,levelInfo,hasChildren,isLast,showGrid){
		if(showGrid == null)
			showGrid = true;
		var size = 20;
		var img_pre = "<img align='absmiddle' width=\""+size+"\" height=\""+size+"\"";
		var img_str = img_pre+"src=\""+treeImage.blank+"\">";
		
		var str = "";
		for(var i=0;i<level;i++){
			if(i>0){
				if(!levelInfo[i])
					str += img_pre+"src=\""+treeImage.line1+"\">";
				else
					str += img_str;
			}else{
				str += img_str;
			}
		}
		var expand_src,close_src;
		expand_src = (level==0?treeImage.plus5:((isLast?treeImage.plus2:treeImage.plus3)));
		close_src = (level==0?treeImage.minus5:((isLast?treeImage.minus2:treeImage.minus3)));
		if(hasChildren){
			str += img_pre+"class='_expandImg' src=\""+expand_src+"\"";
		}else
			str += img_pre+"src=\""+(isLast?treeImage.line2:treeImage.line3)+"\"";

		str +=" expand_src=\""+expand_src+"\""+
			" close_src=\""+close_src+"\""+
			" style=\"cursor: pointer;\" onclick='_"+treeId+".expandTreeNode(this.parentNode.parentNode,null,null,event);'>";

		return "<span style='float: left;clear: both;'>"+str+"</span>";
	},
	/**
	 * 构造树节点
	 */
	__buildTreeItems : function(templateBody,nodes,data,parentData,visible){
		var innerBuilder = function(templateBody,nodes,data,parentData,visible){
			if(visible != null)
				visible = visible;
			else
				visible = false;

			var pKey = eval("data."+templateBody.extensions.pKey);
				
			eval("_"+templateBody.id+".datas.put("+pKey+",data);");

			templateBody.itemCount ++;
			data._MODIFIERS = customModifiers;
			data._self = data;
			var children = eval("data."+templateBody.extensions.childrenKey);
			if(children != null && children.list)
				children = children.list;
			if(children != null && !lang.isArray(children))
				children = [children];

			var leafKey = eval("data."+templateBody.extensions.leafKey);
			var hasChildren = (children != null && children.length>0) || (leafKey=="false" || leafKey==false);
	
			if(parentData){
				data._treeLevel = parentData._treeLevel + 1;
				data._levelInfo[data._treeLevel] = data._isLast;
				data._treePath[data._treeLevel] = [pKey+""];
			}else{
				data._treeLevel = 0;
				data._levelInfo = [false];
				data._treePath = [pKey+""];
			}
	
			var template = this._getTemplate(templateBody,data)[0];
			var treeSpan = "<span id='"+templateBody.id+"_"+pKey+"' nodeId='"+pKey+"' class='_treeNode' "+
			" levelInfo='"+data._levelInfo.join("/")+"'"+
			" treePath='"+data._treePath.join("/")+"'"+
			" onclick='_"+templateBody.id+".selectTreeNode(event,this);' level='"+data._treeLevel+"' expanded='false'";
			if(data._treeLevel == 0)
				nodes.push(treeSpan+">");
			else
				nodes.push(treeSpan+(visible?">":" style=\"display:none;\">"));
			nodes.push(this._generateTreeNodeDecorator(templateBody.id,data._treeLevel,data._levelInfo,hasChildren,data._isLast,templateBody.extensions.showGrid));
			nodes.push("<span class=\"_treeNodeInfo\">");
			nodes.push(template.process(data));
			nodes.push("</span>");
	
			if(children != null){
				if(children.length){
					//如果开关为真，排序
					if(templateBody.extensions.enableSort){
						children = lang.sortEntity(children,true,"id");
					}
					var len = children.length;
					for(var i=0;i<len;i++){
						children[i]._isLast = (i==len-1);
						children[i]._levelInfo = data._levelInfo;
						children[i]._treePath = data._treePath;
						this._buildTreeItems(templateBody,nodes,children[i],data);
					}
				}else{
					if(!children.toArray){//解决由于Prototype导致的空数组,但类型又不是数组问题
						children._isLast = true;
						children._levelInfo = data._levelInfo;
						children._treePath = data._treePath;
						this._buildTreeItems(templateBody,nodes,children,data);
					}
				}
			}
			nodes.push("</span>");
		}.bind(this);
		
		if(data.length){
			for(var i=0;i<data.length;i++)
				innerBuilder(templateBody,nodes,data[i],parentData,visible);
		}else{
			innerBuilder(templateBody,nodes,data,parentData,visible);
		}
	},
	/**
	 * 返回当前树的根节点
	 */
	getRootNode:function(){
		return this.rootNode;
	},
	/**
	 * 根据当前事件的发生源,获取当前所在的树节点
	 */
	getNodeAtCP:function(event){
		if(!event)
			return null;
		return htmlDom.parentElemByCssName(EventUtil.getEventTarget(event),this._treeNodeClass);
	},
	/**
	 * 返回节点的ID
	 */
	getNodeId:function(node){
		return node.getAttribute("nodeId");
	},
	/**
	 * 根据节点ID获取节点对象
	 */
	getNodeById:function(id){
		return $.elem(this.templateBody.id+"_"+id);
	},
	/**
	 * 返回模板节点中的所有用户自定义属性
	 */
	getNodeAttributes:function(node){
		var result = {};
		var attrs = node.childNodes[1].firstChild.attributes;
		for(var i=0;i<attrs.length;i++){
			attr = attrs[i];
			if(attr.specified)
				result[attr.nodeName.toLowerCase()] = attrs[i].nodeValue;
		}
		return result;
	},
	/**
	 * 返回当前事件源所在的节点的路径
	 */
	getTreePathInfoAtCP:function(event){
		if(!event)
			return null;
		var pNode = htmlDom.parentElemByCssName(EventUtil.getEventTarget(event),this._treeNodeClass);
		return pNode.getAttribute("treePath");
	},
	/**
	 * 返回当前节点的路径
	 */
	getTreePath:function(node,interceptor){
		interceptor = interceptor || function(node,path){//装饰节点路径的回调函数
			return "<a href='#' onclick='_bookTree.expandTreeNode("+_bookTree.getNodeId(node)+",null,true);'>"+path+"</a>";
		};
		
		var strs = new StringBuffer();
		var _slef = this;
		var initTreePath = function(node,interceptor){
			if(!node)
				return;
	
			var nodeText = document.getElementsByUIName("nodeText",node.childNodes[1]);
			if(nodeText){
				if(interceptor)
					strs.append(interceptor(node,nodeText[0].innerHTML));
				else
					strs.append(nodeText[0].innerHTML);
			}
			
			initTreePath(_slef._getParentTreeNode(node),interceptor);
		}

		initTreePath(node,interceptor);
		
		return strs.toString("/",true);;
	},
	/**
	 * 用户需要实现的装载子节点的处理方法,返回值是符合parentId的所有子节点
	 */
	doLoadChild:function(parentId,callBack){
		return null;
	},
	/**
	 * 用户需要实现的当节点被选中时要实现的操作(如果用户对节点的选中事件感兴趣)
	 */
	onTreeNodeSelected:function(node){
	},
	/**
	 * 用户需要实现的当节点被展开时要实现的操作(如果用户对节点的展开事件感兴趣)
	 */
	onTreeNodeExpanded:function(node){
	},
	/**
	 * 用户自定义的节点状态变更处理函数
	 * node
	 * 	当前节点
	 * expanded
	 * 	是否已经展开
	 * isLeaf
	 * 	是否是叶子节点
	 */
	onStateChange:function(node,expanded,isLeaf){
	},
	/**
	 * 用户自定义的节点样式变更处理函数
	 * node
	 * 	当前节点
	 * clear
	 * 	样式的变更结果:为true表示清除,否则表示要设置新样式
	 */
	onStyleChange:function(node,clear){
	}
}

var XWindow = function(){};
XWindow.WIN_NOR = 1;//正常
XWindow.WIN_MIN = 2;//最小化
XWindow.WIN_MAX = 3;//最大化
XWindow.PortletZindex = 10;

var WorkSpace = new function(){
	this.portlets = new Map();
	this.windows = new Map();
	
	this.init = function(){
	},
	/**
* 加Portlet
	 */
	this.addPortlet = function(portlet){
		this.portlets.put(portlet.widgetId,portlet);
	}
	/**
	 * 设置非当前Portlet的显示状态
	 */
	this._changeOtherPortletVisible = function(target,portlet,visible){
		var elems = $ela("isPortlet",target);
		var _portlet;
		if(elems != null){
			for(var i =0;i<elems.length;i ++){
				elem = elems[i];
				_portlet = this.getPortlet($a(elem,"widgetId"));
				if(_portlet == null)
					continue;
				if(visible){
					if(portlet == null ||(portlet != null && portlet.widgetId == _portlet.widgetId))
						_portlet.domNode.style.display = "";
				}else
					_portlet.domNode.style.display = "none";
			}
		}
	}
	/**
	 * 打开一个新的内部窗口
	 * url : 要打开的URL
	 * title : 标题
	 * win : 当前触发事件所在的window对象
	 * window_id : 新打开窗口的ID,今后可以通过这个ID来关闭这个window
	 */
	this.openInnerWindow = function(url,title,win,window_id){
		win = win || window;
		//保存Top window
		if(win.windowId == null){
			win.windowId = "top";
			this.windows.put(win.windowId,win);
		}

		var currentPortlet = this.getPortlet(win);
		var currentTargetContainer = this.getPortletOuterContainer(win);
		if(currentTargetContainer == null)
			return;
		//隐藏其它portlets
		this._changeOtherPortletVisible(currentTargetContainer,null,false);

		var protletCmp = new Portlet({title:title,width:currentTargetContainer.width,noTitle:currentPortlet.extension.noTitle,noBorder:currentPortlet.extension.noBorder,hasDecorate:false});
		protletCmp.setUrl(url);
		//设置当前的portlet的下一个portlet为新建的这个portlet
		currentPortlet.nextPortlet = protletCmp;
		//设置新portlet的前面的portlet为当前所发出指定所有的portlet
		protletCmp.forwardPortlet = currentPortlet;
		protletCmp.setParentNode(currentTargetContainer);
		
		this.addPortlet(protletCmp);
		if(window_id){
			protletCmp.iframe.windowId = window_id;
			this.windows.put(window_id,protletCmp.iframe.contentWindow);
		}
	},
	this.closeInnerWindow = function(win){
		sys.finishAll();
		this._closeInnerWindow(win);
	},
	/**
	 * 关闭指定的内部窗口
	 * win :window对象,或者是window对象的ID
	 */
	this._closeInnerWindow = function(win){
		if(!lang.isString(win))
			win = win || window;
		else //从缓存中获取window
			win = this.windows.get(win);
		
		if(win == null)
			return;
			
		var topics = win._topics;
		if(topics != null){
			for(var i=0;i<topics.length;i++){
				unSubscribe(topics[i],win);
			}
		}
			
		var portlet = this.getPortlet(win);
		var currentTargetContainer = this.getPortletOuterContainer(win);
		if(currentTargetContainer == null)
			return;

		var forwardPortlet = portlet.forwardPortlet;
		var nextPortlet = portlet.nextPortlet;
		
		while(nextPortlet != null){
			this._destroyPortlet(nextPortlet);
			nextPortlet = nextPortlet.nextPortlet;
		}
		
		if(forwardPortlet!=null)
			this._destroyPortlet(portlet);
		
		this._changeOtherPortletVisible(currentTargetContainer,forwardPortlet,true);
	},
	/**
	 * 销毁portlet
	 */
	this._destroyPortlet = function(portlet){
		if(portlet == null) return;
		
		if(portlet.iframe.windowId != null)
			this.windows.remove(portlet.iframe.windowId);
			
		this.removePortlet(portlet.widgetId);
		
		if(portlet.forwardPortlet != null)
			portlet.forwardPortlet.nextPortlet = null;
		
		if(portlet.iframe.onunload)	
			portlet.iframe.onunload();
		
		portlet.onDestroy();
	},
	
	/**
	 * 删除Portlet
	 */
	this.removePortlet = function(id){
		this.portlets.remove(id);
	}
	/**
	 * 获取Portlet
	 * id:portlet的ID或者是当前所在的window
	 */
	this.getPortlet = function(id){
		if(id==null || !lang.isString(id))
			id = IFrameUtil.findIframeNode(id).getAttribute("portletId");

		return this.portlets.get(id);
	}
	/**
	 * 返回指定Portlet所在的父容器
	 */
	this.getPortletOuterContainer = function(id){
		try{
			return this.getPortlet(id).parentNode;
		}catch(e){return null}
	}
};

var Portlet = function(extension){
	
	this.widgetName = "Portlet";
	this.widgetId = "portlet_"+new Date().getTime()+"";
	this.windowState = XWindow.WIN_NOR;

	if(extension == null)
		extension = {};
	else{
		this.extension = extension;
	}
	
	this.extension.width = this.extension.width || "100%";
	this.extension.height = this.extension.height || "";
	this.extension.hasDecorate = this.extension.hasDecorate!=null?this.extension.hasDecorate:true;
	this.extension.noTitle = !this.extension.hasDecorate;
	this.extension.noBorder = !this.extension.hasDecorate;
	this.extension.isIframe = this.extension.isIframe!=null?this.extension.isIframe:true;
	this.extension.resizeable = this.extension.resizeable!=null?this.extension.resizeable:false;
	this.extension.cssName = this.extension.cssName!=null?this.extension.cssName:null;
	

	this.id = this.extension.id;
	this.url = this.extension.url;
	this.title = "";
	this.leftTopPoint = {x:0,y:0};
	this.rightBottomPoint = {x:0,y:0};

	this.htmlTemplate = window.top.context_root+"javascript/ui/portletTemplate.html";
	this.cssTemplate = window.top.context_root+"/riap/theme/default/css/portletView.css";
	
	this.imagePath = window.top.context_root+"/riap/theme/default/image/portal/";
	this.hideActionIcon = this.imagePath+"expand.gif";
	this.hideActionIcon2 = this.imagePath+"expand_hover.gif";
	this.refreshActionIcon = this.imagePath+"glyph_refresh.gif";
	this.refreshActionIcon2 = this.imagePath+"glyph_refresh_hover.gif";
	this.editActionIcon = this.imagePath+"BtnSettings.gif";
	this.editActionIcon2 = this.imagePath+"BtnSettings_hover.gif";
	this.closeActionIcon = this.imagePath+"close.gif";
	this.closeActionIcon2 = this.imagePath+"close_hover.gif";
	this.resizeWindowActionIcon= this.imagePath+"big.gif";
	this.resizeWindowActionIcon2= this.imagePath+"big_hover.gif";
	this.resizeActionRestoreIcon= this.imagePath+"restore.gif";
	this.resizeActionRestoreIcon2= this.imagePath+"restore_hover.gif";
	this.loadingActionIcon= this.imagePath+"exten.gif";

	if(Portlet.portletDomNodeTemplate == null){
		var _self = this;
		Portlet.portletDomNodeTemplate = $.ajax({
				url:_self.htmlTemplate,
				dataType:'html',
				async:false
		}).responseText;
	}

	this._init();
}

Portlet.portletDomNodeTemplate=null;
Portlet.prototype = {
	_init : function(){
		this.domNode = WidgetUtils.createDomFromTemplate(Portlet.portletDomNodeTemplate,this);
		this.domNode.setAttribute("isPortlet",true);
		this.domNode.setAttribute("widgetId",this.widgetId);
		this.domNode.setAttribute("portletRlId",this.id);
		if(this.titleContainer)
			this.titleContainer.style.backgroundImage = "url('"+this.imagePath+"biaotuo2.jpg')"
		if(this.extension.resizeable){
			this.floatingdiv = document.createElement("div");
			this.floatingimg = document.createElement("img");
			this.floatingdiv.appendChild(this.floatingimg);
			this.floatingimg.src = this.imagePath+"ResizeGripper.gif";
			this.floatingdiv.style.position = "relative";
			this.floatingdiv.style.zIndex = "9999";
			this.floatingdiv.style.cursor = "se-resize";
			this.domNode.appendChild(this.floatingdiv);
		
			var _self = this;
			this.floatingdiv.onDrag = function(x,y){
				if(htmlDom.style.toNumber(this.style.left)<100)
					this.style.left = 100;
				if(htmlDom.style.toNumber(_self.domNode.style.height)<30)
					_self.domNode.style.height = 30;
				else
					_self.domNode.style.height = htmlDom.style.toNumber(_self.domNode.style.height)+htmlDom.style.toNumber(this.style.top)+24;
				
				_self.domNode.style.width = htmlDom.style.toNumber(this.style.left)+24;
				
				this.style.top = "-24"
			}
			Drag.init(this.floatingdiv);
		}
		
		//设置Title及边框的显示属性
		if(this.titleContainer)
			this.titleContainer.style.display = this.extension.noTitle?"none":"";
		if(this.contentContainer)
			this.contentContainer.style.borderTop = this.extension.noBorder?"0":"1px solid #CACFD3";

		if(this.extension.title)
			this.setTitle(this.extension.title);

		//设置Portlet的自定义边框
		if(this.portletContainer){
			if(this.extension.cssName != null)
				this.portletContainer.className = this.extension.cssName;
			else
				this.portletContainer.style.border = this.extension.noBorder?"0":"1px solid #CACFD3";
		}
		
		if(this.hideAction && window.top.env.runtime){
			this.hideAction.style.display = "none";
			this.closeAction.style.display = "none";
			this.loadingAction.style.display = "none";
		}else{
			this.hideAction.style.display = "";
			this.loadingAction.style.display = "";
			this.closeAction.style.display = "";
		}
		
		//将当前的Portlet加入到工作区
		WorkSpace.addPortlet(this);
	},
	setWindowState : function(state){
		switch (state) {
			case XWindow.WIN_MIN:
				
				break;
			case XWindow.WIN_MAX:
				
				break;
			default:
				break;
		}
	},
	setTitle : function(title){
		this.title = title;
		this.activeTitileEditAction.innerHTML = title;
		this.titileEditAction.innerHTML = title;
	},
	setParentNode : function(pNode,lazy){
		this.parentNode = pNode;
		pNode.appendChild(this.domNode);
		if(lazy){
			var _self = this;
			window.setTimeout(function(){
				_self.setUrl();
			},200);
		}else{
			this.setUrl();
		}
	},
	setUrl : function(url){
		url = url || this.url;
		this.url = url;
		
		var width = parent.$("iframe")[0].offsetWidth; //检查是否为网页固定宽度780，如果不是就是自适应 //2007-09-03 by zdh
		if(width != "780")
			width = "100%"
		
		if(this.extension.isIframe){
			if(this.iframe == null){
				this.iframe = document.createElement("iframe");
				this.iframe.setAttribute("marginheight","0");
				this.iframe.setAttribute("marginwidth","0");
				this.iframe.setAttribute("width",width);
				this.iframe.setAttribute("framespacing","0");
				this.iframe.setAttribute("frameBorder","0");
				this.iframe.scrolling = "no";
				//添加当前的IFrame到Dom
				this.contentContainer.innerHTML = "";
				this.contentContainer.appendChild(this.iframe);
			}
			
			//设置当前的IFrame对应的Portlet对象的组件ID
			this.iframe.setAttribute("portletId",this.widgetId);

			//绑定IFrame的事件
			this._bindIFrameEvent();
			
			//加载IFrame里的内容
			this.iframe.setAttribute("src",window.top.current_context+this.url);
		}else{
			new Ajax.Request(this.url,{method: 'get',onComplete:function(transport, json){
				this.contentContainer.innerHTML = transport.responseText;
			}.bind(this)});
		}
		
		if(this.extension.height != "")
			this.domNode.style.height = this.extension.height+24;
		
		this.resize();
	},
	_bindIFrameEvent : function(){
		var _self = this;
		//window的改变大小事件
		var resize_func = function(){
			window.top.IFrameUtil.resize(this.contentWindow);
		}.bind(this.iframe);

		//window的加载事件
		var load_func = function(){

			if(_self.extension.hasDecorate && !window.top.env.runtime){
				_self.titleContainer.style.cursor = "move";
				//dndMgr.registerDraggable(new Rico.Draggable("dragSource",_self.domNode,_self.iframe));
			}else{
				_self.titleContainer.style.cursor = "default";
			}

			//订阅window的onresize消息
			window.top.Topic.getInstance().subscribe("window_onresize",new window.top.EventHandler(this.contentWindow,function(){
				_self.resize();
				window.top.IFrameUtil.resize(this.contentWindow);
			}.bind(this)));
			
			//订阅Portlet的ondrop消息
			window.top.Topic.getInstance().subscribe("element_ondrop",new window.top.EventHandler(this.contentWindow,function(param){
				//处理Firefox每次在托动完成后会自动Reload的问题,避免事件被重复的订阅
				try{
					if((param.iframe==null ||(param.iframe != null && param.iframe.contentWindow == this.contentWindow))){
						if(!window.top.isIE()){
							window.top.Topic.getInstance().unSubscribe("window_onresize",this.contentWindow);
							window.top.Topic.getInstance().unSubscribe("element_ondrop",this.contentWindow);
						}
						_self.onDrop(param.source,param.target);
					}
				}catch(e){
				}
			}.bind(this)));
			
			//绑定父窗体的onkeydown事件到当前的IFrame
			if(window.top.isIE()){
				this.contentWindow.document.body.onkeydown = doOnKeyDown.bindAsEventListener(this.contentWindow);
			}else{
				this.contentWindow.document.onkeydown = doOnKeyDown.bindAsEventListener(this.contentWindow);
			}

			if(window.top.isIE()){
				this.contentWindow.attachEvent('onresize',resize_func);
			}else{
				this.contentWindow.onresize = resize_func;
				this.contentWindow.onscroll = resize_func;
			}
			
			this.contentWindow.setInterval(function(){
				try{
					if(_self.contentContainer.offsetHeight != this.contentWindow.document.body.scrollHeight){
						//重新计算窗口大小
						window.top.IFrameUtil.resize(this.contentWindow);
						_self.resize();
					}
				}catch(e){}
			}.bind(this),50);
			
			if(this.contentWindow.document.body){
				//附加父窗体的onclick事件到当前的IFrame
				if(window.top.isIE()){
					this.contentWindow.document.body.attachEvent("onclick",document.body.onclick);
				}else{
					this.contentWindow.document.onclick = document.body.onclick;
				}
			}
			
		}.bind(this.iframe);
		
		//window的卸载事件
		var onunload_func = function(){
			window.top.Topic.getInstance().unSubscribe("window_onresize",this.contentWindow);
			window.top.Topic.getInstance().unSubscribe("element_ondrop",this.contentWindow);
		}.bind(this.iframe);

		if(window.top.isIE()){
			this.iframe.attachEvent('onload',load_func);
			this.iframe.attachEvent('onunload',onunload_func);
		}else{
			this.iframe.onload = load_func;
			this.iframe.onunload = onunload_func;
		}
	},
	resize : function(){
		//设置宽度
		var thisDomWidth = window.top.htmlDom.style.toNumber(this.extension.width)
		this.domNode.style.width = thisDomWidth;
	},
	reload : function(){
		if(this.iframe != null){
			this.iframe.contentWindow.document.location.reload();
		}else
			this.setUrl(this.url);
	},
	/**
	 * 开始拖拽
	 */
	onDragStart : function(x,y){
		if(!this.domNode.style.zIndex || this.domNode.style.zIndex<XWindow.PortletZindex){
			XWindow.PortletZindex++;
			this.domNode.style.zIndex = XWindow.PortletZindex;
		}
	},
	/**
	 * 结束拖拽
	 */
	onDragEnd : function(x,y){
	},
	onDrop : function(src,target){
		this.extension.width = target.getAttribute("originalWidth");
		this.resize();
		if(this.iframe != null)
			IFrameUtil.resize(this.iframe.contentWindow);
		
		var topic = window.top.Topic;
		//发布布局发生变换的消息主题
		topic.getInstance().publish("portlet_onPositionChanged",{src:src,target:target,portlet:this});
	},
	onTitleDbclick:function(e){
		this.onResize(e);
	},
	onTitleBarMouseOver:function(e){
		this.disActiveTitleBar.style.display = "none";
		this.activeTitleBar.style.display = "";
		this.titleContainer.style.backgroundImage = "url('"+this.imagePath+"biaotuo2_over.jpg')"
	},
	onTtitleBarMouseOut:function(e){
		this.activeTitleBar.style.display = "none";
		this.disActiveTitleBar.style.display = "";
		this.titleContainer.style.backgroundImage = "url('"+this.imagePath+"biaotuo2.jpg')"
	},
	changeIcon	:	function(e){
		var evSrc = e.srcElement||e.target;
		var appid=evSrc.getAttribute("uiAttachPoint");
		if (e.type=="mouseover") {
			var oldSrc = "this."+appid+"Icon2";
			oldSrc = eval(oldSrc);
			evSrc.src = oldSrc;
		} else if (e.type=="mouseout") {
			var newSrc = "this."+appid+"Icon";
			newSrc = eval(newSrc);
			evSrc.src = newSrc;
		}
	},
	onTitleEdit:function(e){
		return true;
	},
	onHide:function(e){
		this.active = !this.active;
		if(this.active){
			this.contentContainerTr.style.display = "none";
			if(this.extension.resizeable)
				this.floatingdiv.style.display = "none";
		}else{
			this.contentContainerTr.style.display = "";
			if(this.extension.resizeable)
				this.floatingdiv.style.display = "";
		}
	},
	onRefresh:function(e){
		this.reload();
	},
	onResize:function(e){
		if(!this.isMax){
			this._maxWindow();
		}else{
			this._restoreWindow();
		}
	},
	_maxWindow :function(){
		if(window.currentMaxPortlet != null)
			window.currentMaxPortlet._restoreWindow();
		window.currentMaxPortlet = this;
		this.resizeWindowAction.src = this.resizeActionRestoreIcon;	// ??
		this.isMax = true;
	},
	_restoreWindow :function(){
		window.currentMaxPortlet = null;
		this.resizeWindowAction.src = this.resizeWindowActionIcon;	// ??
		this.resizeWindowAction.title = "还原";
		this.isMax = false;
	},
	onCloseWindow:function(e){
		if(!window.confirm("您确认要删除 \""+this.title+"\" 吗？"))
			return false;
		this.destroy();
	},
	destroy : function(){
		//发布布局发生变换的消息主题
		window.top.Topic.getInstance().publish("portlet_onRemoved",{id:this.id,callBack:function(){
			if(this.iframe!= null && window.top.isIE()){
				if(this.iframe.contentWindow.doOnunload)
					this.iframe.contentWindow.doOnunload();
			}	
			this.onDestroy.apply(this,arguments);
		}.bind(this)});
	},
	onPortletEdit:function(e){
		alert("非常抱歉,功能还没有实现！");
	},
	/**
	 * 执行销毁前的数据清除工作
	 */
	onDestroy : function(){
		//从工作区删除当前Portlet
		WorkSpace.removePortlet(this.widgetId);
		this._restoreWindow();
		htmlDom.removeNode(this.iframe);
		htmlDom.removeNode(this.domNode);
	}
}

Portal = function(mainTabId,subTabId,titleEditorId,tEngine,extensions){
	this.mainTabId = mainTabId;
	this.subTabId = subTabId;

	this.mainTabElem = $.elem(mainTabId);
	this.subTabElem = $.elem(subTabId).parentNode;
	this.titleEditor = $.elem(titleEditorId);

	this.tEngine = tEngine;
	this.cMPage = null;//当前正在编辑的一级导航页
	this.cSPage = null;//当前正在编辑的子页
	
	this.cmTabIndex = -1;
	this.editMainPage = false;
	
	extensions = extensions || {};

	this.extensions = {};
	this.extensions.pKey = extensions.pKey || "id";
	this.extensions.childrenKey = extensions.childrenKey || "children";
	this.extensions.buildItems = this.tEngine._buildPageItems = (extensions._buildPageItems || this.__buildPageItems.bind(this.tEngine));
	

	//保存当前ID的实例引用到JS全局上下文
	eval("_"+mainTabId+"=this;");
}
Portal.prototype = {
	init:function(jsonDS,callBack){
		//构造回调函数
		var _callBack = callBack || (function(){});
		callBack = function(){
			_callBack();
			IFrameUtil.resize();
			this.mIterEntity = this.tEngine.tempateBodies.get(this.mainTabId+"");
			var tab;
			if(this.cmTabIndex != -1)
				tab = this.mIterEntity.loopBody.childNodes[this.cmTabIndex];
			else{
				tab = this.mIterEntity.loopBody.firstChild;
			}
			
			//设置每个Tab页的index,并选择当前页
			var loopBody = this.mIterEntity.loopBody;
			window.setTimeout(function(){
				var children = loopBody.childNodes;
				var len = children.length;
				for(var i=0;i<len;i++){
					children[i].setAttribute("rowIndex",i);
				}
				//选择当前的Tab页
				if(tab){
					EventUtil.fireEvent("onclick",tab);
				}
			},5);
		}.bind(this);
		
		//进行数据迭代
		this.tEngine.bind($.elem(this.mainTabId),jsonDS,this.extensions,callBack);
	},
	/**
	 * 添加新页
	 */
	addPage:function(parentId,pageInfo){
		var _self = this;
		protalProxy.createPage(env.currentUser.id,env.currentUser.id,pageInfo,parentId,function(json){
			if(json.error){
				alert("添加页面出错！"+json.error.msg);
			}else{
				//将当前新页添加到导航栏
				var newElem = _self._createPage(json.result,_self.mIterEntity);
				_self.selectMainPage(newElem);
			}
		});
	},
	/**
	 * 添加二级页面
	 */
	addSubPage:function(pageInfo){
		if(this.cMPage == null)
			return;

		var _self = this;
		protalProxy.createPage(env.currentUser.id,env.currentUser.id,pageInfo,this.cMPage.getAttribute("pageId"),function(json){
			if(json.error){
				alert("添加页面出错！"+json.error.msg);
			}else{
				//将当前新页添加到导航栏
				var newElem = _self._createPage(json.result,_self.sIterEntity);
				_self.selectSubPage(newElem);
			}
		});
	},
	_createPage:function(pageInfo,iterEntity){
		var nodes = [];
		this.tEngine._buildPageItems(iterEntity,nodes,[pageInfo]);
		var tmpDiv = document.createElement("DIV");
		tmpDiv.innerHTML = nodes.join("");
		var newElem = tmpDiv.firstChild;
		iterEntity.loopBody.appendChild(newElem);
		return newElem;
	},
	/**
	 * 修改指定页的信息
	 */
	updatePage:function(pageInfo){
	},
	/**
	 * 完成标题栏的编辑事件
	 */
	finishEdit:function(e){
		if(e.keyCode !=13 && e.keyCode !=0 && !lang.isUndefined(e.keyCode) || !this.titleEditor) return;
		//取得当前正在编辑的Tab页
		var elem = this.editMainPage?this.cMPage:this.cSPage;
		
		var pageInfo;
		var _self = this;
		if(this.editMainPage)
			pageInfo = this.mIterEntity.pages.get(elem.getAttribute("pageId"));
		else
			pageInfo = this.sIterEntity.pages.get(elem.getAttribute("pageId"));

		//备份子页
		var bak_children = pageInfo.children;
		pageInfo.children = null;
		
		//修改名称
		pageInfo.name = this.titleEditor.value;
		//向后台更新当前页面信息
		protalProxy.updatePageInfo(pageInfo,function(json){
			if(json.error){
				alert("添加页面出错！"+json.error.msg);
			}else{
				var title = document.getElementsByUIName("title",elem);
				if(title){
					title = title[0];
					title.innerHTML = pageInfo.name;
				}
			}
			pageInfo.children = bak_children;
		});

		this.titleEditor.style.display = "none";
	},
	/**
	 * 删除指定一级页
	 * pageElemId
	 * 	当前激活状态中的Page 元素的id
	 */
	removeMainPage:function(event,pageElemId,id){
		this._removePage(event,pageElemId,id,true);
	},
	/**
	 * 删除指定二级页
	 * pageElemId
	 * 	当前激活状态中的Page 元素的id
	 */
	removeSubPage:function(event,pageElemId,id){
		this._removePage(event,pageElemId,id,false);
	},
	_removePage:function(event,pageElemId,id,isMain){
		if(!window.confirm("您确定要删除当前页吗？"))
			return;
		
		var pageElem = document.getElementById(pageElemId);
		var preElem = pageElem.previousSibling;
		id = new JLong(id);
		protalProxy.deletePage(id,function(json){
			if(json.error){
				alert("删除页面出错！"+json.error.msg);
			}
			EventUtil.cancelBubble(event);
			if(pageElem != null){
				pageElem.parentNode.removeChild(pageElem);
				if(preElem){
					if(isMain)
						this.selectMainPage(preElem);
					else
						this.selectSubPage(preElem);
				}
			}
		});
	},
	/**
	 * 监测标题的编辑事件
	 */
	_notifyEdit:function(elem,isMainPage){
		if(!this.titleEditor)
			return;
		var title = document.getElementsByUIName("title",elem);
		if(title){
			title = title[0];
			this.titleEditor.style.display = "";
			this.titleEditor.style.left = htmlDom.style.getAbsoluteX(elem,true)+1;
			this.titleEditor.style.top = htmlDom.style.getAbsoluteY(elem,true)+1;
			this.titleEditor.style.width = elem.offsetWidth -2;
			this.titleEditor.style.height = elem.offsetHeight-2;
			this.titleEditor.value = title.innerHTML.trim();
			this.titleEditor.select();
		}
		this.editMainPage = isMainPage;
	},
	/**
	 * 根据序号,选择指定的一级页
	 */
	selectMainPageByIndex:function(index){
		var mp = this.mainTabElem.childNodes[index-1];
		if(mp)
			this.selectMainPage(mp);
	},
	/**
	 * 选择一级页
	 */
	selectMainPage:function(elem,id){
		id = id || elem.getAttribute("pageId");
		if(this.titleEditor)
			this.titleEditor.style.display = "none";
		if(!env.runtime && this.cMPage == elem && this.titleEditor != null){
			this._notifyEdit(elem,true);
			return;
		}

		if(this.cMPage !=null){
			this.cMPage.style.background="url(/riap/theme/default/images/navBackground.jpg)";
			this.cMPage.className = "inactiveTab";
			this.cMPage.firstChild.className = "inactiveTableft";
			this.cMPage.firstChild.firstChild.className = "inactiveTabright";
		}
		if(!env.runtime){
			if(this.mCimg)
				this.mCimg.style.display = "none";
	
			var mCimg = document.getElementsByUIName("closeTabImg",elem);
			if(mCimg)
				this.mCimg = mCimg[0];
			this.mCimg.style.display = "";
		}
			
		this.cMPage = elem;
		//保存当前页的index
		this.cmTabIndex = Number(this.cMPage.getAttribute("rowIndex"));

		this.cMPage.style.background="url(/riap/theme/default/images/nav01.jpg)";
		this.cMPage.className = "activeTab";
		this.cMPage.firstChild.className = "activeTableft";
		this.cMPage.firstChild.firstChild.className = "activeTabright";
		
		var pageInfo = this.mIterEntity.pages.get(id+"");
		//构造回调方法
		var callBack = function(){
			this.subTabElem.style.display = env.runtime?"none":"";

			IFrameUtil.resize();
			this.sIterEntity = this.tEngine.tempateBodies.get(this.subTabId+"");
			var tab = this.sIterEntity.loopBody.firstChild;

			if(tab != null){
				EventUtil.fireEvent("onclick",tab);
			}else{
				//触发页面被选择的事件
				this.onPageSelect(elem,id,this.mIterEntity.pages.get(id+""));
			}
		}.bind(this);
		//进行数据迭代
		this.tEngine.bind(document.getElementById(this.subTabId),pageInfo,this.extensions,callBack);
	},
	/**
	 * 根据序号,选择指定的二级页
	 */
	selectSubPageByIndex:function(index){
		var sp = this.subTabElem.childNodes[index-1];
		if(sp)
			this.selectSubPage(sp);
	},
	/**
	 * 选择二级页
	 */
	selectSubPage:function(elem,id){
		id = id || elem.getAttribute("pageId");

		if(this.titleEditor)
			this.titleEditor.style.display = "none";
		if(!env.runtime && this.cSPage == elem && this.titleEditor != null){
			this._notifyEdit(elem,false);
			return;
		}

		this.subTabElem.style.display = "";
		if(!env.runtime){
			if(this.sCimg)
				this.sCimg.style.display = "none";
	
			var sCimg = document.getElementsByUIName("closeTabImg",elem);
			if(sCimg)
				this.sCimg = sCimg[0];
			this.sCimg.style.display = "";
		}
		
		if(this.cSPage !=null)
			this.cSPage.className = "unsubTabMenuItem";
			
		this.cSPage = elem;
		this.cSPage.className = "onsubTabMenuItem";

		//触发页面被选择的事件
		this.onPageSelect(elem,id,this.sIterEntity.pages.get(id+""));
	},
	/**
	 * 装载指定页下的portlets
	 */
	loadPortlets:function(pageId,callBack){
		if(!env.isStatic)
			protalProxy.getPortlets(pageId,callBack);
		else
			loadStaticPortlets(pageId,callBack);
	},
	/**
	 * 构造页面信息，则TemplateEngine调用
	 */
	__buildPageItems:function(iteratorEntity,nodes,datas){
		if(datas.list)
			datas = datas.list;

		if(!lang.isArray(datas))
			datas = [datas];

		if(!iteratorEntity.pages)
			iteratorEntity.pages = new Map();
		//创建页面的内部函数
		function buildPageItem(data){
			data._MODIFIERS = customModifiers;
			var leafKey = data[iteratorEntity.extensions.leafKey];
			var pKey = data[iteratorEntity.extensions.pKey];
			iteratorEntity.pages.put(pKey+"",data);
			
			var template = this._getTemplate(iteratorEntity,data)[0];
			nodes.push(template.process(data));
			iteratorEntity.itemCount ++;
		};
		
		for(var i=0;i<datas.length;i++){
			buildPageItem.call(this,datas[i]);
		}
	},
	/**
	 * 由对象的实例override,用来响应页面切换的事件
	 */
	onPageSelect:function(elem,id,pageInfo){
	}
}


PortalProxy = function(){
}
PortalProxy.prototype={
	/**
	 * 获取指定套餐下的缺省页面，用在设计时的页面管理
	 */
	getServiceBundleDefaultPages : function(serviceBundleId,callBack){
	  env.remoteJsonrpc.callRemote("PortalService.getServiceBundleDefaultPages",[serviceBundleId],callBack);
	},
	/**
	 * 获取当前用户在目标用户下的portal页面
	 */
	getPages : function(userId,targetUserId,callBack){
	  env.remoteJsonrpc.callRemote("PortalService.getUserPages",[userId,targetUserId],callBack);
	},
	/**
	 * 获取指定页面下的所有portlets
	 */
	getPortlets : function(pageId,callBack){
	  env.remoteJsonrpc.callRemote("PortalService.getPagePortlets",[pageId],callBack);
	},
	/**
	 * 在当前页添加一个portlet
	 */
	addPortletToUserPage : function(pageId,portlet,callBack){
	  env.remoteJsonrpc.callRemote("PortalService.addPortletToUserPage",[pageId,portlet],callBack);
	},
	/**
	 * 创建新页面
	 */
	createPage : function(userId,targetUserId,page,parentId,callBack){
		//如果是管理状态,则加载当前服务套餐下的所有缺省的页
		if(sys.onManageState)
		  env.remoteJsonrpc.callRemote("PortalService.createServiceBundlePage",[env.currentServiceBundleId,page,parentId],callBack);
		else
		  env.remoteJsonrpc.callRemote("PortalService.createUserPage",[userId,targetUserId,page,parentId],callBack);
	},
	/**
	 * 创建子页面
	 */
	createChildPage : function(page,parentId,callBack){
		env.remoteJsonrpc.callRemote("PortalService.createUserChildPage",[page,parentId],callBack);
	},
	/**
	 * 删除页面
	 */
	deletePage : function(pageId,callBack){
		env.remoteJsonrpc.callRemote("PortalService.removeUserPage",[pageId],callBack);
	},
	/**
	 * 更新页面信息
	 */
	updatePageInfo : function(page,callBack){
		env.remoteJsonrpc.callRemote("PortalService.updatePageInfo",[page],callBack);
	},
	/**
	 * 更改页面布局
	 */
	changePagePortletLayout : function(portlets,callBack){
		env.remoteJsonrpc.callRemote("PortalService.changeUserPagePortletLayout",[portlets],callBack);
	},
	/**
	 * 从当前页删除portlet
	 */
	removePortlet : function(portletId,callBack){
		env.remoteJsonrpc.callRemote("PortalService.removePortletFromUserPage",[portletId],callBack);
	}
}

//全局的portal服务代理对象
var protalProxy = new PortalProxy();


//====================================================================
// 表格装饰器
//====================================================================
TableDecorator=function(tableId,selectedClass,mouseoverClass){
	this.tableId = tableId;
	this.selectedClass = selectedClass || "ListClassBacks";
	this.mouseoverClass = mouseoverClass || "ListClassBack";
	eval(this.tableId+"_decorator=this");
}

TableDecorator.prototype = {
	decorateHead : function(option){
		this._initTable();
		if(!this.sortAbleUtil)
			this.sortAbleUtil = new SortAbleUtil(this.table);
		else
			this.sortAbleUtil.table = this.table;

		if(option.sortAble){
			var _self = this;
			this.table.thead.each(
				function(){
					$(this).find("td").each(function(index){
						$(this).attr("columnIndex",index);
						htmlDom.disableSelection(this);
					}).not(".ckbHeadTd").each(function(){
						$(this).find(".sortIcon").each(function(){
							this.parentNode.removeChild(this);
						});
						var img = $("<img class='sortIcon' style='margin-left: 4px;margin-top: 4px;' src='/riap/theme/default/image/portal/expand_hover.gif'>")[0];
						this.appendChild(img);
						this.onclick = function(){
							_self._onSortIconClick(this);
						}
						this.style.cursor = "pointer";
						$(img).hide();
					});
					
					var _index = _self.sortAbleUtil.sortCol;
					if(_index != null){
						var sortIconTd = this.cells[_index];
						if(sortIconTd){
							_self.sortAbleUtil.sortCol = null;
							if(_self.sortAbleUtil.isDesc != null)
								$(sortIconTd).find(".sortIcon").attr("isDesc",_self.sortAbleUtil.isDesc=="true"?"false":"true").hide();
							_self._onSortIconClick(sortIconTd);
						}
					}
				}
			);
		}
	},
	/**
	 * 为表格Body的所有行增加复选框的功能
	 * checkedIds : 初始要被选中的ID数组
	 */
	decorateRow : function(option){
		this.checkAbleOption = option;
		this._initTable();
		var _self = this;
		//初始化表格头的全选Checkbox
		this.table.thead.each(
			function(){
				var ckb = $(this).find("input.selectAllCkb:first");
				if(ckb.length==0)
					this.insertBefore($("<td class='ckbHeadTd' style=\"width: 24px;\"><input class='selectAllCkb' id=\""+_self.tableId+"_allCkb\" type=\"checkbox\" onclick=\""+_self.tableId+"_decorator._checkAll(this);\"/></td>")[0],this.firstChild);
				else
					ckb[0].checked = false;
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

		var td = $("<td class='tdCkb_"+rowId+"'><input type=\"checkbox\" style=\"display: none;\"/>&nbsp;</td>")[0];
		tr[0].insertBefore(td,tr[0].firstChild);
		
		var fun = function(event){
			var ckb = tr.find("input:first")[0];
			if((EventUtil.getEventTarget(event).tagName)!="INPUT")
				ckb.checked = !ckb.checked;
			_self._checkCurrent(ckb,true);
		};

		tr.mouseover(function(){//鼠标经过
			tr.addClass(_self.mouseoverClass);
			tr.find("input:first").show();
		}).mouseout(function(){//鼠标离开
			tr.removeClass(_self.mouseoverClass);
			_self._showCheckBox(tr);
		}).click(function(event){//鼠标单击
			fun.call(this,event);
			if(option.onclick){
				option.onclick.call(this,event);
			}
		}).dblclick(function(event){//鼠标双击
			fun.call(this,event);
			if(option.dblclick){
				option.dblclick.call(this,event);
			}
			fun.call(this,event);
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
			var ckb = $("input:first",td)[0];
			ckb.checked = true;
			_self._checkCurrent(ckb);
		}
	},
	uncheckAll:function(){
		var _self = this;
		this.table.tbody.find("input:checkbox").each(function(){
			this.checked = false;
			_self._checkCurrent(this,false);
		});
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
		var rows = this.getCheckedRows();
		var ids = [];
		rows.each(function(){
			ids.push($(this).attr("rowId"));
		});
		return ids;
	},
	/**
	 * 获取当前选择的TR数组
	 */
	getCheckedRows:function(){
		return this.table.table.find("tbody tr."+this.selectedClass);
	},
	/**
	 * tr:当前所在的TR
	 * checked ：是否被选中
	 */
	onCheckedFunction:function(tr,checked){
	},
	reInitTable:function(){//初始化表格对象
		this._initTable();
	},
	_onSortIconClick:function(td,key){
		var cellIndex = parseInt(td.getAttribute("columnIndex"));
		var dataType = $(td).attr("dataType")||"string";
		
		elem = $(td).find(".sortIcon")[0];

		var isDesc = $(elem).attr("isDesc");
		
		if(isDesc==null)
			isDesc = false;
		else
			isDesc = isDesc=="false"?false:true;

		isDesc = !isDesc;
		
		if(isDesc){
			elem.src = "/riap/theme/default/image/portal/expand_hover.gif";
		}else{
			elem.src = "/riap/theme/default/image/portal/inpand_hover.gif";
		}
		
		this.table.thead.find("td img.sortIcon").hide().removeAttr("isDesc");
		
		isDesc = isDesc?"true":"false";
		$(elem).attr("isDesc",isDesc);
		
		$(elem).show();
		
		this.sortAbleUtil.isDesc = isDesc;

		this.sortAbleUtil.sortColumn(cellIndex,dataType,isDesc=="true"?true:false);
	},
	_checkAll:function(elem){//选择所有
		var checked = $(elem).attr("checked");
		var _self = this;
		this.table.tbody.find("input:checkbox").each(function(){
			this.checked = checked;
			_self._checkCurrent(this,false);
		});
	},
	_checkCurrent:function(elem,changeTop){//选择当前
		if(elem.checked){
			$(elem).show();
		}else{
			$.elem(this.tableId+"_allCkb").checked = false;
			$(elem).hide();
		}
		if(elem.checked && changeTop){
			this._changeCheckedAllCkbState();
		}
	 	var tr = $(htmlDom.parentElemByTagName(elem,"tr"));
		if(elem.checked){
			tr.addClass(this.selectedClass);
		}else{
			tr.removeClass(this.selectedClass);
		}

		//触发用户定义的选中事件
		this.onCheckedFunction(tr[0],elem.checked);
	},
	_showCheckBox:function(elem){//显示Checkbox
		var ckb = elem.find("input:first");
		if(!ckb[0].checked)
			ckb.hide();
	},
	_changeCheckedAllCkbState:function(){//改变全选Checkbox的状态
		var len = this.table.tbody.find("input:checkbox[@checked]").length;
		$.elem(this.tableId+"_allCkb").checked = len==0?false:(len == this.table.tbody.find("input:checkbox").length);
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

SortAbleUtil = function(table){
	this.table = table;
}
SortAbleUtil.prototype = {
	getCellContent:function(ob){
	    if (ob.textContent != null)
	    return ob.textContent;
	    var s = ob.innerText;
	    return s.substring(0, s.length);
	},
	//排序 tableId: 表的id,iCol:第几列 ；dataType：iCol对应的列显示数据的数据类型
	sortColumn:function(iCol, dataType,isDesc) {
	    var tbody = this.table.dom.tBodies[0];
	    var colRows = tbody.rows;
	    var aTrs = new Array;
	    //将将得到的列放入数组，备用
	    for (var i=0; i < colRows.length; i++) {
	        aTrs[i] = colRows[i];
	    }

        //使用数组的sort方法，传进排序函数
        aTrs.sort(this.compareEle(iCol, dataType,isDesc));

	    var oFragment = document.createDocumentFragment();
	    for (var i=0; i < aTrs.length; i++) {
	        oFragment.appendChild(aTrs[i]);
	    } 
	    tbody.appendChild(oFragment);
	    //记录最后一次排序的列索引
	    this.sortCol = iCol;
	},
	//将列的类型转化成相应的可以排列的数据类型
	convert:function(sValue, dataType) {
	    switch(dataType) {
	    case "int":
	        return parseInt(sValue);
	    case "float":
	        return parseFloat(sValue);
	    case "date":
	        return new Date(Date.parse(sValue));
	    default:
	        return sValue.toString().trim();
	    }
	},
	//排序函数，iCol表示列索引，dataType表示该列的数据类型
	compareEle:function(iCol, dataType,isDesc) {
		var _self = this;
	    return  function (oTR1, oTR2) {
	        var vValue1 = _self.convert(_self.getCellContent(oTR1.cells[iCol]), dataType);
	        var vValue2 = _self.convert(_self.getCellContent(oTR2.cells[iCol]), dataType);

			if (vValue1 < vValue2) return isDesc ? 1 : -1;
			if (vValue1 > vValue2) return isDesc ? -1 : 1;
			return 0;
	     };   
	}
}
