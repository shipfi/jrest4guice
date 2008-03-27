var themes_path = "../theme/default";
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


Iterator=function(/* array */arr){
	var a=arr;
	var position=0;
	this.element=a[position]||null;
	this.atEnd=function(){
		return (position>=a.length);	//	bool
	};
	this.get=function(){
		if(this.atEnd()){
			return null;		//	object
		}
		this.element=a[position++];
		return this.element;	//	object
	};
	this.map=function(/* function */fn, /* object? */scope){
		var s=scope||dj_global;
		if(Array.map){
			return Array.map(a,fn,s);	//	array
		}else{
			var arr=[];
			for(var i=0; i<a.length; i++){
				arr.push(fn.call(s,a[i]));
			}
			return arr;		//	array
		}
	};
	this.reset=function(){
		position=0;
		this.element=a[position];
	};
}

ArrayList=function(/* array? */arr){
	var items=[];
	if(arr) items=items.concat(arr);
	this.count=items.length;
	this.add=function(/* object */obj){
		items.push(obj);
		this.count=items.length;
	};
	this.addRange=function(/* array */a){
		if(a.getIterator){
			var e=a.getIterator();
			while(!e.atEnd()){
				this.add(e.get());
			}
			this.count=items.length;
		}else{
			for(var i=0; i<a.length; i++){
				items.push(a[i]);
			}
			this.count=items.length;
		}
	};
	this.clear=function(){
		items.splice(0, items.length);
		this.count=0;
	};
	this.clone=function(){
		return new ArrayList(items);	//	ArrayList
	};
	this.contains=function(/* object */obj){
		for(var i=0; i < items.length; i++){
			if(items[i] == obj) {
				return true;	//	bool
			}
		}
		return false;	//	bool
	};
	this.forEach=function(/* function */ fn, /* object? */ scope){
		var s=scope||dj_global;
		if(Array.forEach){
			Array.forEach(items, fn, s);
		}else{
			for(var i=0; i<items.length; i++){
				fn.call(s, items[i], i, items);
			}
		}
	};
	this.getIterator=function(){
		return new Iterator(items);	//	Iterator
	};
	this.indexOf=function(/* object */obj){
		for(var i=0; i < items.length; i++){
			if(items[i] == obj) {
				return i;	//	int
			}
		}
		return -1;	// int
	};
	this.insert=function(/* int */ i, /* object */ obj){
		items.splice(i,0,obj);
		this.count=items.length;
	};
	this.item=this.get=function(/* int */ i){
		return items[i];	//	object
	};
	this.remove=function(/* object */obj){
		var i=this.indexOf(obj);
		if(i >=0) {
			items.splice(i,1);
		}
		this.count=items.length;
	};
	this.removeAt=function(/* int */ i){
		items.splice(i,1);
		this.count=items.length;
	};
	this.reverse=function(){
		items.reverse();
	};
	this.sort=function(/* function? */ fn){
		if(fn){
			items.sort(fn);
		}else{
			items.sort();
		}
	};
	this.setByIndex=function(/* int */ i, /* object */ obj){
		items[i]=obj;
		this.count=items.length;
	};
	this.toArray=function(){
		return [].concat(items);
	}
	this.toString=function(/* string */ delim){
		return items.join((delim||","));
	};
};

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

$.elem = function(){
  var elements = new Array();

  for (var i = 0; i < arguments.length; i++) {
    var element = arguments[i];
    if (typeof element == 'string')
      element = document.getElementById(element);

    if (arguments.length == 1)
      return element;

    elements.push(element);
  }

  return elements;
}

var $A = Array.from = function(iterable) {
  if (!iterable) return [];
  if (iterable.toArray) {
    return iterable.toArray();
  } else {
    var results = [];
    for (var i = 0; i < iterable.length; i++)
      results.push(iterable[i]);
    return results;
  }
}

document.getElementsByClassName = function(className, parentElement,callBack) {
  var children = ($.elem(parentElement) || document.body).getElementsByTagName('*');
  var child,result=[];  
  for(var i=0;i<children.length;i++){
  	child = children[i];
	if((child.nodeType == 3 || child.nodeType == 4 ||child.nodeType == 8))
		continue;
    if (child.className==className){
      result.push(child);
      if(callBack)
      	callBack(child);
    }
  }
  if(result.length==0)
  	result = null;

  return result;

}
document.getElementsByUIName = function(uiName, parentElement,callBack) {
  var children = ($.elem(parentElement) || document.body).getElementsByTagName('*');
  var attr,child,result=[];  
  for(var i=0;i<children.length;i++){
  	child = children[i];
	if((child.nodeType == 3 || child.nodeType == 4 ||child.nodeType == 8))
		continue;
	attr = child.getAttribute("ui");
    if (attr==uiName){
      result.push(child);
      if(callBack)
      	callBack(child);
    }
  }
  if(result.length==0)
  	result = null;

  return result;
}

var DataParser = {
	/**
	 * 解析指定对象层次结构的数据
	 * obj:要解析的目标对象
	 * oPath:obj中的对象层次路径
	 */
	doParse : function(obj,oPath){
		try{
			if(oPath.indexOf("/")!=-1 || oPath.indexOf(".")!=-1){
				oPath = oPath.replace(/\/+/g,".");
				return eval("obj."+oPath);
			}else{
				return obj[oPath];
			}
		}catch(e){return null}
	}
}

/**
 * 事件的工具类
 */
var EventUtil = {
	cancelBubble: function(event){
		try{
			if(event){
			  //event.returnValue = false;
			  event.cancelBubble = true;
			}
		}catch(e){};
	},
	fireEvent: function(eName,elem){
		if(isIE())
			elem.fireEvent(eName);
		else{
			var event = elem.getAttribute(eName);
			var fun = function(){
				eval(event);
			}.bind(elem);
			fun();
		}
	},
	getEventTarget : function(event){
		return event.target || event.srcElement;
	},
	attchEvent : function(obj,evt,func){
		if(isIE())
			obj.attachEvent(evt,func);
		else
			obj.addEventListener(evt, func, false);
	}
}

/**
 * 事件的处理句柄
 */
var EventHandler = function(win,handler,replyIntime){
	if(win == null)
		return;
	this.win = win;
	this.replyIntime = replyIntime;

	this.widgetId = this.win.widgetId;

	if(lang.isString(handler))
		this.handler = function(){
			return eval("win."+handler+".apply(this,arguments);")
		};
	else
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
			if(handlers[i].widgetId == eventHandler.widgetId){
				_index = i;
				break;
			}
		}
		
		if(_index != -1){
			handlers[_index] = eventHandler;
			//注册当前的消息主题到当前窗口
			if(!eventHandler.win._topics)
				eventHandler.win._topics = [topic];
			else
				eventHandler.win._topics.push(topic);
		}else{
			//初始化当前窗口的消息主题集合
			eventHandler.win._topics = [topic];
			handlers.push(eventHandler);
		}
		this.eventHandlers.put(topic,handlers);
		
		if(this.publications.containsKey(topic) && eventHandler.replyIntime)
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
			if(handlers[i].widgetId == sWindow.widgetId){
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

function subscribe(topic,eventHandler){
	unSubscribe(topic,eventHandler.win);
	window.top.Topic.getInstance().subscribe(topic,eventHandler);
}

function publish(topic,param){
	window.top.Topic.getInstance().publish(topic,param);
}

function unSubscribe(topic,sWindow){
	window.top.Topic.getInstance().unSubscribe(topic,sWindow);
}

function isIE(){
  return navigator.appName.indexOf("Microsoft")!=-1;
}

function getParameter(para) {
    var retval = "", s = location.search.replace("?", "");
    if (s == "") {
        return "";
    }
    s = s.split("&");
    for (var i = 0; i < s.length; i=i+1) {
        if (s[i].toUpperCase().indexOf(para.toUpperCase() + "=") == 0) {
            retval += ((retval == "" ? "" : ", ") + s[i].substr(s[i].indexOf("=") + 1, s[i].length));
        }
    }
    return decodeURI(retval);
} 

if(!isIE()){
	XMLDocument.prototype.loadXML = function(xmlString){
		var parser = new DOMParser();
		var doc = parser.parseFromString(xmlString, "text/xml");
	  this.appendChild(doc.documentElement);
	};
}

var StringBuffer = function(){
	this.strs = [];
	
	this.append = function(str)	{
		this.strs.push(str);
	};
	this.toString = function(mark,reverse){
		mark = mark || "";
		if(reverse)
			this.strs = this.strs.reverse();
		return this.strs.join(mark);
	}
}

/**
 * 获取所有包含attrName属性的元素
 * 如果指定了filter,则数据结果会被这个指定的过滤器过滤
 */
$ela = function(attrName, parentElement,filter,single,tagName) {
	parentElement = $.elem(parentElement);
	tagName = tagName || "*";

	var children = $A((parentElement || document.body).getElementsByTagName(tagName));
	
	if(parentElement.getAttribute(attrName) != null)
		children.push(parentElement);
	
	var result = [],child = null;
	for(var i=0;i<children.length;i++){
		child = children[i];
		var attr = child.getAttribute(attrName);
		if (attr!=null && attr!= ""){
			if(!filter ||(filter.call(child,attr))){
				result.push(child);
				if(single) break;
			}
		}
	}
	
	if(single){
		if(result.length>0)
	 		return result[0];
	  	else
	  		return null;
	}
	return result;
}

/**
 * 获取所有包含tagName属性的元素
 * 如果指定了filter,则数据结果会被这个指定的过滤器过滤
 */
$elt = function(tagName, parentElement,filter,single) {
	parentElement = $.elem(parentElement);
	var children = $A((parentElement || document.body).getElementsByTagName(tagName));

	if(parentElement.tagName == tagName)
	  	children.push(parentElement);

	var result = [],child = null;
	for(var i=0;i<children.length;i++){
		child = children[i];
    	if(!filter ||(filter && filter.call(child))){
			result.push(child);
			if(single) break;
    	}
	}
  
	if(single){
		if(result.length>0)
	 		return result[0];
	  	else
	  		return null;
	}
	return result;
}

/**
 * 根据属性名称,获取指定的属性的值
 */
var $a = function(elem,attrN) {
	try{
		return elem.getAttribute(attrN);
	}catch(e){return null;}
}

/**
 * 获取除了attr外的所有Attrbite对象
 */
var $aw = function(elem,attr) {
  return $A(elem.attributes).inject([], function(attrs, pattr) {
    if (pattr.specified && (!attr || pattr.nodeName != attr)){
      attrs.push(pattr);
    }
    return attrs;
  });
}

var $atrrbitesExist = function(elem,attrs,both) {
	var eAttrs = elem.attributes;
	var count = 0;
	var isOk = false;
	for(var i=0;i<eAttrs.length;i++){
		for(var j=0;j<attrs.length;j++){
			if(eAttrs[i].nodeName == attrs[j]){
				count ++;
			}
			
			if(count>0){
				if(both){
					if(count >= attrs.length){
						isOk = true;
						break;
					}
				}else{
					isOk = true;
					break;
				}
			}
		}
	}
	
	return isOk;
}

json2array = function(json,attrs){
	var a = new Array();
	var attrsa = null;
	if(attrs != null)
		attrsa = $A(attrs);
	for(var p in json){
		if(attrsa==null || (attrsa != null && attrsa.include(p)))
			a.push(json[p]);
	}
	return a;
}

elementAttr2json = function(elem){
	var obj = {};
  $A(elem.attributes).each(function(attr){
  	if(attr.specified)
	  	obj[attr.nodeName] = attr.nodeValue;
  });
  return obj;
}


/**
 * 时间串转换
 */
parseDateTime = function(time)
{
	var pdate;
	var hh;
	var dd = time.split(" ");
	var ss = dd[0].split("-");
	ss[1] = ss[1] -1;
	if(dd.length == 1)
	{
		pdate = new Date(ss[0],ss[1],ss[2]);
	}
	else
	{
		hh = dd[1].split(":");
		if(hh.length == 3)
			pdate = new Date(ss[0],ss[1],ss[2],hh[0],hh[1],hh[2]);
		else
			pdate = new Date(ss[0],ss[1],ss[2],hh[0],hh[1],0);
    }
   return pdate.getTime();
}

/**
 * 将数据提交到服务
 */
submitToServer = function(dom,callBack){
	dom = $.elem(dom);
	var form = $.elem(dom.getAttribute("form"));
	var datas = {};
	var elems = form.getElementsByTagName("*");
	var len = elems.length;
	var elem,value;
	for(var i=0;i<len;i++){
		elem = elems[i];
		value = null;
		if((elem.tagName == "INPUT" && elem.type =="text") || elem.tagName =="TEXTAREA")
			value = elem.value;
		
		if(value != null)
			datas[elem.getAttribute("field")] = value;
	}
	
	var param = [datas];
	
	var rpc = dom.getAttribute("rpc");
	//如果没有申明RPC服务,则使用系统的缺省RPC远程服务
	rpc = rpc || "window.top.env.remoteJsonrpc";

	if(rpc != null){
		var service = dom.getAttribute("service");
		var exp = rpc+".callRemote('"+service+"',param,callBack);";
		eval(exp);
	}
}

//======================================================================================
// Json相关的数据类型
//======================================================================================
JArrayList = function(){
	this.javaClass = "java.util.ArrayList";
	this.list = [];
}

JHashSet = function(){
	this.javaClass = "java.util.HashSet";
	this.list = [];
}

JHashMap = function(){
	this.javaClass = "java.util.HashMap";
	this.map = {};
}

JDate = function(date){
	this.javaClass = "java.util.Date";
	if(date != null)
		this.time = date;
	else
		this.time = new Date();
	this.time = this.time.getTime();
}

JTimestamp = function(date){
	this.javaClass = "java.sql.Timestamp";
	if(date != null)
		this.time = date;
	else
		this.time = new Date();
}

JLong = function(_value){
	this.javaClass = "java.lang.Long";
	this.value = _value;
}


//======================================================================================
// Json处理对象
//======================================================================================
var $json= function(){};

/**
 * 串行化Javascript对象到Json方式
 */
$json.serialize = function(o){
		var objtype = typeof(o);
		if(objtype == "undefined"){
			return "undefined";
		}else if((objtype == "number")||(objtype == "boolean")){
			return o + "";
		}else if(o === null){
			return "null";
		}
		if (objtype == "string") { return o.escapeString(); }
		var me = arguments.callee;
		var newObj;
		if(typeof(o.__json__) == "function"){
			newObj = o.__json__();
			if(o !== newObj){
				return me(newObj);
			}
		}
		if(typeof(o.json) == "function"){
			newObj = o.json();
			if (o !== newObj) {
				return me(newObj);
			}
		}
		// array
		if(objtype != "function" && typeof(o.length) == "number"){
			var res = [];
			for(var i = 0; i < o.length; i++){
				var val = me(o[i]);
				if(typeof(val) != "string"){
					val = "undefined";
				}
				res.push(val);
			}
			return "[" + res.join(",") + "]";
		}

		// it's a function with no adapter, bad
		if(objtype == "function"){
			return null;
		}
		// generic object code path
		res = [];
		for (var k in o){
			var useKey;
			if (typeof(k) == "number"){
				useKey = '"' + k + '"';
			}else if (typeof(k) == "string"){
				useKey = k.escapeString();
			}else{
				// skip non-string or number keys
				continue;
			}
			val = me(o[k]);
			if(typeof(val) != "string"){
				// skip non-serializable values
				continue;
			}
			res.push(useKey + ":" + val);
		}
		return "{" + res.join(",") + "}";
};
/**
 * 将Json字符串转换成Javascript对象
 */
$json.evalJson = function(json){
	try {
		var result = eval("(" + json + ")");
		$json.pareseReference(result,new Map());
		return result;
	}catch(e){
		return json;
	}
}

$json.pareseArrayReference = function(json_array,referenceMap){
	if(typeof json_array == "string")
		$json._pareseReference(json_array,referenceMap);
	else{
		for(var i=0;i<json_array.length;i++){
			$json.pareseReference(json_array[i],referenceMap);
		}
	}
}

$json.pareseReference = function(obj,referenceMap){
	if (obj instanceof Array)
		$json.pareseArrayReference(obj,referenceMap);
	else if(obj instanceof Object)
		$json._pareseReference(obj,referenceMap);
}

$json._pareseReference = function(json_obj,referenceMap){
	var type = json_obj.JSONRPCType;
	var objectID = json_obj.objectID;
	if(type == null || type!="Reference")
		referenceMap.put(objectID,json_obj);

	var element,key,ref_element,list;
	for(var key in json_obj){
		if(key=="extend")
			continue;
		element = json_obj[key];
		if(element == null)
			continue;
		if(element instanceof Object){
			if(element.list != null && element.list instanceof Array){
				$json.pareseArrayReference(element.list,referenceMap);
			}else{
				objectID = element.objectID;
				if(element["referenceClass"] != null){
					var ref = referenceMap.get(objectID);
					if(ref != null)
						json_obj[key] = ref;
				}else{
					//分析成员对象的引用关系
					$json.pareseReference(element,referenceMap);
				}
			}
		}
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
	
	this.makeResizeAble = function(iframe){
		var cWindow = iframe.contentWindow;
		window.setInterval(function(){
			try{
				if(iframe.offsetHeight != cWindow.document.body.offsetHeight){
					//重新计算窗口大小
					IFrameUtil.resize(cWindow);
				}
			}catch(e){}
		},500);
	}
	
	this.resize = function(sWindow){
		if(!sWindow)
			sWindow = window;
		var iframe = this.findIframeNode(sWindow);
		if(iframe != null && sWindow.document.body != null){
			h = Number(sWindow.document.body.offsetHeight);
			iframe.style.height = h;
			h = Number(sWindow.document.body.scrollHeight);
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


//===================================================================================
// sys
//===================================================================================
var sys={}

sys.clone = function(source){
	var newObj = {};
	for(var p in source){
		newObj[p] = source[p];
	}
	
	return newObj;
}

sys.msgArea = document.createElement("DIV");
with(sys.msgArea){
	style.position = "absolute";
	style.background = "#d2fb8f";
	style.border = "1px solid #6FBEFF";
	style.color = "#333";
	style.fontSize = "12";
	style.padding = "2px";
	style.opacity = "0.8";
	style.filter = "Alpha(opacity=80)";
}

sys.debug = false;
sys.imagePath = "../../../"+themes_path+"/image/tree/";
sys.info = function(msg){
	if(sys.debug) alert(msg);
};
sys.raise = function(msg){
	if(sys.debug) {
		alert(msg);
	}
	throw msg
};

sys.waittingBars = new Map();

/**
 * 关闭所有进度条
 */
sys.finishAll = function(){
	var bars = sys.waittingBars.values();
	if(bars != null && bars != ""){
		for(var i=0;i<bars.length;i++){
			try{
				bars[i].destroy();
			}catch(e){}
		}
	}
	
	sys.waittingBars.clear();
	var win = window.top;
	var progressBarContainer = win.document.getElementById("progressBarContainer");
	if(progressBarContainer != null && progressBarContainer!= "")
		progressBarContainer.innerHTML = "";
}
/**
 * 关闭指定ID的进度条信息
 */
sys.finish = function(id){
	try{
	  var bar = sys.waittingBars.get(id);
	  if(bar != null){
	  	if(bar.tagName != null)
			bar.parentNode.removeChild(bar);
		else
			bar.destroy();
	  }
	}catch(e){}
};

sys.logout = function(url,msg){
	if(!msg)
		msg = '你确定要注销当前会话吗?';
	if(window.confirm(msg))
		location.href=url;
}

/**
 * 显示进度处理信息条
 */
sys.waitting = function(id,msg){
	var win = window.top;
	var loadingDiv = win.$.elem("loading");
	if(loadingDiv == null){
		loadingDiv = win.document.createElement("div");
		loadingDiv.style.display = "none";
		loadingDiv.innerHTML = 
			'	<div ui=\"Progresstitle\" class=\"Progresstitle\" ></div>'+
			'	<div class=\"Progressdiv\">'+
			'		<div class=\"Progress\">'+
			'			<div ui=\"ProgressBar\" class=\"ProgressBar\"></div>'+
			'		</div>'+
			'	</div>';
	}
	var bar = window.top.createProgressBar(loadingDiv,"数据加载中，请稍候...",true,10,false);
	
	if(sys.waittingBars.containsKey(id))
	  sys.finish(id);
	
	sys.waittingBars.put(id,bar);
};

/**
 * 在指定位置显示提示信息
 */
sys.innerMesg = function(id,msg,pNode,target){
	sys.finish(id);
	var msgArea = sys.msgArea.cloneNode(true);
	msgArea.style.display = "";
	msgArea.innerHTML = msg;
	msgArea.style.top = 0;//htmlDom.style.getAbsoluteY(target);
	msgArea.style.right = 10;
//	msgArea.style.left = htmlDom.style.getAbsoluteX(target);

	pNode.appendChild(msgArea);
	sys.waittingBars.put(id,msgArea);
}

sys.gc = function(){
	if(isIE()){
		CollectGarbage();
		window.setTimeout("CollectGarbage();",1);
	}
}


/**
 * 持续时间调试器
 */
var DurationTimeDebug= new function(){
	this.s_time = null;
	this.start = function(){
		if(sys.debug)
			this.s_time = new Date().getTime();
	};
	this.finish = function(msg,target){
		if(sys.debug){
			msg = msg+": "+((new Date().getTime()-this.s_time)/1000)+"秒";
			if(!target)
				target = $.elem("debugArea");
			if(target){
				target.innerHTML = msg;
			}
			else{
				alert(msg);
			}
		}
	}
}

/**
 * 数据收集
 */
gatherData = function(parentElement,type){
	var nodeslist = {};
	
	/* 
	 * 基于JQUERY的each迭代只能为jquery对象
	 * 使用方法不变!parentElement依旧为数据区域的对象
	 * 增加省略参数,可以收集全文的数据表单,
	 * 收集的类型有input,select,textarea
	 */
	parentElement = parentElement || document;
	var nodes = $(parentElement).find("input,select,textarea");
	
	var value = null;
	nodes.each(function(node){
		if(type != null && $a(this,type) == null)
			return;
		if((nodes[node].tagName == "INPUT" && nodes[node].type != "button" && nodes[node].type != "reset" && nodes[node].type != "submit" && nodes[node].type != "image") || nodes[node].tagName == "SELECT" || nodes[node].tagName == "TEXTAREA"){
			var key = nodes[node].getAttribute("field") ||nodes[node].name||nodes[node].id;
			if(key == null || key=="")
				return;
			value = null;
			if(nodes[node].type!="checkbox" && nodes[node].value!=null && nodes[node].type != "radio" || (nodes[node].type=="radio" && nodes[node].checked  == true)){
				value = nodes[node].value;
			}else if(nodes[node].type=="checkbox"){
				if(nodes[node].checked  == true)
					value = true;
				else
					value = false;
			}
			if(!nodeslist[key])
				nodeslist[key]=value;
		}
	});
	return nodeslist;
}

var Thread = function(runable,pTime){
	pTime = pTime || 10;
	this.run = function(){
		window.setTimeout(runable,pTime);
	}
}

//===================================================================================
// string
//===================================================================================
String.prototype.trimStart = function(){
	this.trim(1);
}
String.prototype.trimEnd = function(){
	this.trim(-1);
}
String.prototype.trim = function(wh){
	if(!this.replace){ return this; }
	if(!this.length){ return this; }
	var re = (wh > 0) ? (/^\s+/) : (wh < 0) ? (/\s+$/) : (/^\s+|\s+$/g);
	return this.replace(re, "");
}
String.prototype.wholeLength = function(){
	var cArr = this.match(/[^\x00-\xff]/ig);
  return this.length + (cArr == null ? 0 : cArr.length);	
}

String.prototype.escapeString = function(){ 
	return ('"' + this.replace(/(["\\])/g, '\\$1') + '"'
		).replace(/[\f]/g, "\\f"
		).replace(/[\b]/g, "\\b"
		).replace(/[\n]/g, "\\n"
		).replace(/[\t]/g, "\\t"
		).replace(/[\r]/g, "\\r");
}

var StringUtils = {
	/**
	 * Returns true if 'str' ends with 'end'
	 */
	endsWith : function(str, end, ignoreCase) {
		if(ignoreCase) {
			str = str.toLowerCase();
			end = end.toLowerCase();
		}
		if((str.length - end.length) < 0){
			return false;
		}
		return str.lastIndexOf(end) == str.length - end.length;
	},
	
	/**
	 * Returns true if 'str' ends with any of the arguments[2 -> n]
	 */
	endsWithAny : function(str /* , ... */) {
		for(var i = 1; i < arguments.length; i++) {
			if(this.endsWith(str, arguments[i])) {
				return true;
			}
		}
		return false;
	},
	
	/**
	 * Returns true if 'str' starts with 'start'
	 */
	startsWith : function(str, start, ignoreCase) {
		if(ignoreCase) {
			str = str.toLowerCase();
			start = start.toLowerCase();
		}
		return str.indexOf(start) == 0;
	},
	/**
	 * Returns true if 'str' starts with any of the arguments[2 -> n]
	 */
	startsWithAny : function(str /* , ... */) {
		for(var i = 1; i < arguments.length; i++) {
			if(this.startsWith(str, arguments[i])) {
				return true;
			}
		}
		return false;
	}
}


//===================================================================================
// lang
//===================================================================================
var lang = new function(){};
lang.isObject = function(wh) {
	return typeof wh == "object" || lang.isArray(wh) || lang.isFunction(wh);
}

lang.isArray = function(wh) {
	return (typeof (wh.length) == "number" || wh instanceof Array || typeof wh == "array");
}

lang.isArrayLike = function(wh) {
	if(lang.isString(wh)){ return false; }
	if(lang.isFunction(wh)){ return false; }
	if(lang.isArray(wh)){ return true; }
	if(typeof wh != "undefined" && wh
		&& lang.isNumber(wh.length) && isFinite(wh.length)){ return true; }
	return false;
}

lang.isFunction = function(wh) {
	return (wh instanceof Function || typeof wh == "function");
}

lang.isString = function(wh) {
	return (wh instanceof String || typeof wh == "string");
}

lang.isAlien = function(wh) {
	return !lang.isFunction() && /\{\s*\[native code\]\s*\}/.test(String(wh));
}

lang.isBoolean = function(wh) {
	return (wh instanceof Boolean || typeof wh == "boolean");
}

lang.isNumber = function(wh) {
	return (wh instanceof Number || typeof wh == "number");
}

lang.isUndefined = function(wh) {
	return ((wh == undefined)&&(typeof wh == "undefined"));
}

lang.sortEntity = function(data,isDesc,key){
	key = key || "id";
	try{
		data.sort(function (n1, n2){
			if (Number(n1[key]) < Number(n2[key])) return isDesc ? 1 : -1;
			if (Number(n1[key]) > Number(n2[key])) return isDesc ? -1 : 1;
			return 0;
		});
	}catch(e){alert(e)};
	return data;
}

var WidgetUtils = function(){};
WidgetUtils.createDomFromTemplate = function(htmlTemplate,widget){
	var htmlTemplate = TrimPath.parseTemplate(htmlTemplate).process(widget);
	var dom = document.createElement("div");
	dom.innerHTML = htmlTemplate;
	
	//处理attachPoint
	var attachPoints = $A($ela("uiAttachPoint",dom));
	$.each(attachPoints,function(){
		var key = $a(this,"uiAttachPoint");
		widget[key] = this;
	});

	//处理attachEvent
	var attachEvents = $A($ela("uiAttachEvent",dom));
	$.each(attachEvents,function(){
		var _event = $a(this,"uiAttachEvent");
		var event_pairs = _event.split(";");
		var event_pair = "";
		var event_key_value = [];
		for(var i=0;i<event_pairs.length;i++){
			event_pair = event_pairs[i];
			event_key_value = event_pair.split(":");
			if(event_key_value.length>1){
				widget[event_key_value[0]] = widget[event_key_value[1]].bindAsEventListener(widget);
			}
		}
	});
	return dom;
}


var Expression = function(){
}
Expression.process = function(exp,data){
	if(lang.isUndefined(data) || exp.indexOf("$") == -1){
		return eval("with(data){"+exp+"}");
	}

	var exps = exp.split("|");
	var data;
	var exp_str;
	for(var i=0;i<exps.length;i++){
		exp_str = exps[i]+"";
		exp_str = exp_str.replace(/\$/g,"data.");
		if(i == 0){
			try{
				data = eval(exp_str);
			}catch(e){data = exp_str};
		}else{
			exp_str = exp_str.replace(/\;/g,"");
			data = eval(exp_str+"(data);");
		}
	}
	
	return data;
}

//===================================================================================
// Json xpath解释工具类
//===================================================================================
var JsonDataSource = function(json,base){
	this.json = json;
	if(base != null){
		this.base = base.replace(/\/+/g,".");
	}
	this.data = this._initData();
}
JsonDataSource.prototype = {
	evaluate:function(xpath){
		var value = "";
		try{
			var exp = xpath.replace(/\/+/g,".");
			var p_exp = "this.json";
			if(this.base != null && this.base != ""){
				p_exp = "this.json."+this.base;
			}
			if(xpath.indexOf("[")==0)
				exp = p_exp+exp;
			else
				exp = p_exp+"."+exp;
			
			value = eval(exp);
		}catch(e){}

		if(typeof value == "undefined")
			value = "&nbsp;";
		return value;
	},
	_initData :function(){
		var data = null;
		var exp = "this.json";
		if(this.base != null && this.base !=""){
			exp = "this.json."+this.base;
		}
		data = eval(exp);
		if(data == null)
			data = [];
		return data;
	}
}


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


// check for XPath implementation
if( document.implementation.hasFeature("XPath", "3.0") ){
   // prototying the XMLDocument
   XMLDocument.prototype.selectNodes = function(cXPathString, xNode)
   {
      if( !xNode ) {  xNode = this;  }
      var oNSResolver = this.createNSResolver(this.documentElement)
      var aItems = this.evaluate(cXPathString, xNode, oNSResolver,
                   XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null)
      var aResult = [];
      for( var i = 0; i < aItems.snapshotLength; i++)
      {
         aResult[i] =  aItems.snapshotItem(i);
       }
      return aResult;
    }

   // prototying the Element
   Element.prototype.selectNodes = function(cXPathString)
   {
      if(this.ownerDocument.selectNodes)
      {
       return this.ownerDocument.selectNodes(cXPathString, this);
       }
      else{ throw "For XML Elements Only"; }
    }
}


// check for XPath implementation
if( document.implementation.hasFeature("XPath", "3.0") ){
   // prototying the XMLDocument
   XMLDocument.prototype.selectSingleNode = function(cXPathString, xNode)
   {
      if( !xNode ) {  xNode = this;  }
      var xItems = this.selectNodes(cXPathString, xNode);
      if( xItems.length > 0 )
      {
         return xItems[0];
       }
      else
      {
         return null;
       }
    }

   // prototying the Element
   Element.prototype.selectSingleNode = function(cXPathString)
   {
      if(this.ownerDocument.selectSingleNode)
      {
         return this.ownerDocument.selectSingleNode(cXPathString, this);
       }
      else{ throw "For XML Elements Only"; }
    }
}

function changeBackground(elem,color,isplay){
	elem.style.backgroundColor = color;
	var imgs = elem.getElementsByTagName("img");
	if(isplay){
		imgs[0].style.display = "";
		imgs[1].style.display = "";
	}else{
		imgs[0].style.display = "none";
		imgs[1].style.display = "none";
	}
}


var htmlDom = {};

htmlDom.ELEMENT_NODE                  = 1;
htmlDom.ATTRIBUTE_NODE                = 2;
htmlDom.TEXT_NODE                     = 3;
htmlDom.CDATA_SECTION_NODE            = 4;
htmlDom.ENTITY_REFERENCE_NODE         = 5;
htmlDom.ENTITY_NODE                   = 6;
htmlDom.PROCESSING_INSTRUCTION_NODE   = 7;
htmlDom.COMMENT_NODE                  = 8;
htmlDom.DOCUMENT_NODE                 = 9;
htmlDom.DOCUMENT_TYPE_NODE            = 10;
htmlDom.DOCUMENT_FRAGMENT_NODE        = 11;
htmlDom.NOTATION_NODE                 = 12;
htmlDom.jsRootPath	= "";

/**
 * 设置指定父节点下的所有TextArea对象支持插入操作
 */
htmlDom.makeInsertAbles = function(parentNode){
	//设置所有的TextArea可以支持插入
	window.setTimeout(function(){
		var textAreas = parentNode.getElementsByTagName("TEXTAREA");
		for(var i=0;i<textAreas.length;i++)
			htmlDom.makeInsertAble(textAreas[i]);
	},10);
}
/*
 * 设置textObj对象支持插入操作
 */
htmlDom.makeInsertAble = function(textObj){
	if(!textObj.createTextRange){
		return;
	}
	var fun = function(){htmlDom.setCaret(textObj);};
	textObj.onselect = textObj.onclick = textObj.onkeyup = fun;
}

htmlDom.setCaret = function(textObj){
	if(textObj.createTextRange){
		textObj.caretPos = document.selection.createRange().duplicate();
	}
}
/**
 * 将指定textValue插入到textObj的选择区域
 */
htmlDom.insertAtCaret = function (textObj, textValue){
	if(isIE()){
		var sAll = document.selection.createRange().duplicate().text == textObj.value;
		if(textObj.createTextRange && textObj.caretPos){
			var caretPos = textObj.caretPos;
			caretPos.text = textValue;
		}else{
			textObj.value += textValue;
		}
		
		if(sAll){
			var r = document.body.createControlRange();
			r.add(textObj);
			r.select();
			r.remove(0);
			r.select();
		}
	}else{
		if(textObj.setSelectionRange){
			var rangeStart = textObj.selectionStart;
			var rangeEnd = textObj.selectionEnd;
			var tempStr1 = textObj.value.substring(0,rangeStart);
			var tempStr2 = textObj.value.substring(rangeEnd);
			textObj.value = tempStr1 + textValue + tempStr2;
		}else{
			alert("浏览器不支持\"setSelectionRange\"方法");
		}  
	}  
}  

htmlDom.isNode = function(wh){
	if(typeof Element == "object") {
		try {
			return wh instanceof Element;
		} catch(E) {}
	} else {
		// best-guess
		return wh && !isNaN(wh.nodeType);
	}
}

htmlDom.getUniqueId = function(){
	do {
		var id = "dj_unique_" + (++arguments.callee._idIncrement);
	}while(document.getElementById(id));
	return id;
}
htmlDom.getUniqueId._idIncrement = 0;

htmlDom.firstElement = htmlDom.getFirstChildElement = function(parentNode, tagName){
	var node = parentNode.firstChild;
	while(node && node.nodeType != htmlDom.ELEMENT_NODE){
		node = node.nextSibling;
	}
	if(tagName && node && node.tagName && node.tagName.toLowerCase() != tagName.toLowerCase()) {
		node = htmlDom.nextElement(node, tagName);
	}
	return node;
}

htmlDom.lastElement = htmlDom.getLastChildElement = function(parentNode, tagName){
	var node = parentNode.lastChild;
	while(node && node.nodeType != htmlDom.ELEMENT_NODE) {
		node = node.previousSibling;
	}
	if(tagName && node && node.tagName && node.tagName.toLowerCase() != tagName.toLowerCase()) {
		node = htmlDom.prevElement(node, tagName);
	}
	return node;
}

htmlDom.parentElemByCssName = function(node, cssName){
	if(!node) { return null; }
	if(cssName) { cssName = cssName.toLowerCase(); }
	do {
		node = node.parentNode;
	} while(node && node.nodeType != htmlDom.ELEMENT_NODE);

	if(node && cssName && cssName.toLowerCase() != node.className.toLowerCase()) {
		return htmlDom.parentElemByCssName(node, cssName);
	}
	return node;
}

htmlDom.parentElemByTagName = function(node, tagName){
	if(!node) { return null; }
	if(tagName) { tagName = tagName.toLowerCase(); }
	do {
		node = node.parentNode;
	} while(node && node.nodeType != htmlDom.ELEMENT_NODE);

	if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
		return htmlDom.parentElemByTagName(node, tagName);
	}
	return node;
}

htmlDom.nextElement = htmlDom.getNextSiblingElement = function(node, tagName){
	return htmlDom._findSiblingByTagName(node,tagName,false);
}

htmlDom.prevElement = htmlDom.getPreviousSiblingElement = function(node, tagName){
	return htmlDom._findSiblingByTagName(node,tagName,true);
}

htmlDom._findSiblingByTagName = function(node, tagName,isPre){
	if(!node) { return null; }
	if(tagName) { tagName = tagName.toLowerCase(); }
	do {
		node = isPre?node.previousSibling:node.nextSibling;
	} while(node && node.nodeType != htmlDom.ELEMENT_NODE);

	if(node && tagName && tagName.toLowerCase() != node.tagName.toLowerCase()) {
		return htmlDom._findSiblingByTagName(node, tagName,isPre);
	}
	return node;
}


htmlDom.nextElemByCssName = function(node, cssName,isPre){
	return htmlDom._findSiblingByCssName(node,cssName,false);
}

htmlDom.prevElemByCssName = function(node, cssName){
	return htmlDom._findSiblingByCssName(node,cssName,true);
}

htmlDom._findSiblingByCssName = function(node, cssName,isPre){
	if(!node) { return null; }
	if(cssName) { cssName = cssName.toLowerCase(); }
	do {
		node = isPre?node.previousSibling:node.nextSibling;
	} while(node && node.nodeType != htmlDom.ELEMENT_NODE);

	if(node && cssName && cssName.toLowerCase() != node.className.toLowerCase()) {
		return htmlDom._findSiblingByCssName(node, cssName,isPre);
	}
	return node;
}

htmlDom.moveChildren = function(srcNode, destNode, trim){
	var count = 0;
	if(trim) {
		while(srcNode.hasChildNodes() &&
			srcNode.firstChild.nodeType == htmlDom.TEXT_NODE) {
			srcNode.removeChild(srcNode.firstChild);
		}
		while(srcNode.hasChildNodes() &&
			srcNode.lastChild.nodeType == htmlDom.TEXT_NODE) {
			srcNode.removeChild(srcNode.lastChild);
		}
	}
	while(srcNode.hasChildNodes()){
		destNode.appendChild(srcNode.firstChild);
		count++;
	}
	return count;
}

htmlDom.copyChildren = function(srcNode, destNode, trim){
	var clonedNode = srcNode.cloneNode(true);
	return this.moveChildren(clonedNode, destNode, trim);
}

htmlDom.removeChildren = function(node){
	var count = node.childNodes.length;
	while(node.hasChildNodes()){ node.removeChild(node.firstChild); }
	return count;
}

htmlDom.replaceChildren = function(node, newChild){
	htmlDom.removeChildren(node);
	node.appendChild(newChild);
}

htmlDom.removeNode = function(node){
	if(node && node.parentNode){
		return node.parentNode.removeChild(node);
	}
}


htmlDom.getScrollTop = function(){
	return window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
}

htmlDom.getScrollLeft = function(){
	return window.pageXOffset || document.documentElement.scrollLeft || document.body.scrollLeft || 0;
}

htmlDom.getScrollOffset = function(){
	var off = [htmlDom.getScrollLeft(), htmlDom.getScrollTop()];
	off.x = off[0];
	off.y = off[1];
	return off;
}

htmlDom.getNodeUnderMouse = function(e,children){
	var mouse = htmlDom.getCursorPosition(e);
	// find the child
	for (var i = 0, child; i < children.length; i++) {
		with (children[i]) {
			if (mouse.x >= left && mouse.x <= right &&
				mouse.y >= top && mouse.y <= bottom) { return i; }
		}
	}
	return -1;
}

htmlDom.getCursorPosition = function(e){
	e = e || window.event;
	var cursor = {x:0, y:0};
	if(e.pageX || e.pageY){
		cursor.x = e.pageX;
		cursor.y = e.pageY;
	}else{
		var de = document.documentElement;
		var db = document.body;
		cursor.x = e.clientX + ((de||db)["scrollLeft"]) - ((de||db)["clientLeft"]);
		cursor.y = e.clientY + ((de||db)["scrollTop"]) - ((de||db)["clientTop"]);
	}
	return cursor;
}

htmlDom.getAncestors = function(node, filterFunction, returnFirstHit) {
	var ancestors = [];
	var isFunction = lang.isFunction(filterFunction);
	while(node) {
		if (!isFunction || filterFunction(node)) {
			ancestors.push(node);
		}
		if (returnFirstHit && ancestors.length > 0) { return ancestors[0]; }
		node = node.parentNode;
	}
	if (returnFirstHit) { return null; }
	return ancestors;
}

htmlDom.getAncestorsByTag = function(node, tag, returnFirstHit) {
	tag = tag.toLowerCase();
	return htmlDom.getAncestors(node, function(el){
		return ((el.tagName)&&(el.tagName.toLowerCase() == tag));
	}, returnFirstHit);
}

htmlDom.getFirstAncestorByTag = function(node, tag) {
	return htmlDom.getAncestorsByTag(node, tag, true);
}

htmlDom.isDescendantOf = function(node, ancestor, guaranteeDescendant){
	if(guaranteeDescendant && node) { node = node.parentNode; }
	while(node) {
		if(node == ancestor){ return true; }
		node = node.parentNode;
	}
	return false;
}

htmlDom.innerXML = function(node){
	if(node.innerXML){
		return node.innerXML;
	}else if(typeof XMLSerializer != "undefined"){
		return (new XMLSerializer()).serializeToString(node);
	}
}

htmlDom.createDocumentFromText = function(str, mimetype){
	if(!mimetype) { mimetype = "text/xml"; }
	if(typeof DOMParser != "undefined") {
		var parser = new DOMParser();
		return parser.parseFromString(str, mimetype);
	}else if(typeof ActiveXObject != "undefined"){
		var domDoc = new ActiveXObject("Microsoft.XMLDOM");
		if(domDoc) {
			domDoc.async = false;
			domDoc.loadXML(str);
			return domDoc;
		}else{
			sys.debug("toXml didn't work?");
		}
	}else if(document.createElement){
		var tmp = document.createElement("xml");
		tmp.innerHTML = str;
		if(document.implementation && document.implementation.createDocument) {
			var xmlDoc = document.implementation.createDocument("foo", "", null);
			for(var i = 0; i < tmp.childNodes.length; i++) {
				xmlDoc.importNode(tmp.childNodes.item(i), true);
			}
			return xmlDoc;
		}
		return tmp.document && tmp.document.firstChild ?
			tmp.document.firstChild : tmp;
	}
	return null;
}

htmlDom.prependChild = function(node, parent) {
	if(parent.firstChild) {
		parent.insertBefore(node, parent.firstChild);
	} else {
		parent.appendChild(node);
	}
	return true;
}

htmlDom.insertBefore = function(node, ref, force){
	if (force != true &&
		(node === ref || node.nextSibling === ref)){ return false; }
	var parent = ref.parentNode;
	parent.insertBefore(node, ref);
	return true;
}

htmlDom.insertAfter = function(node, ref, force){
	var pn = ref.parentNode;
	if(ref == pn.lastChild){
		if((force != true)&&(node === ref)){
			return false;
		}
		pn.appendChild(node);
	}else{
		return this.insertBefore(node, ref.nextSibling, force);
	}
	return true;
}

htmlDom.insertAtPosition = function(node, ref, position){
	if((!node)||(!ref)||(!position)){ return false; }
	switch(position.toLowerCase()){
		case "before":
			return htmlDom.insertBefore(node, ref);
		case "after":
			return htmlDom.insertAfter(node, ref);
		case "first":
			if(ref.firstChild){
				return htmlDom.insertBefore(node, ref.firstChild);
			}else{
				ref.appendChild(node);
				return true;
			}
			break;
		default: // aka: last
			ref.appendChild(node);
			return true;
	}
}

htmlDom.insertAtIndex = function(node, containingNode, insertionIndex){
	var siblingNodes = containingNode.childNodes;
	if (!siblingNodes.length){
		containingNode.appendChild(node);
		return true;
	}

	var after = null;

	for(var i=0; i<siblingNodes.length; i++){
		var sibling_index = -1;
		if (sibling_index < insertionIndex){
			after = siblingNodes.item(i);
		}
	}

	if (after){
		// add it after the node in {after}
		return htmlDom.insertAfter(node, after);
	}else{
		// add it to the start
		return htmlDom.insertBefore(node, siblingNodes.item(0));
	}
}
	
/**
 * implementation of the DOM Level 3 attribute.
 * 
 * @param node The node to scan for text
 * @param text Optional, set the text to this value.
 */
htmlDom.textContent = function(node, text){
	if (text) {
		htmlDom.replaceChildren(node, document.createTextNode(text));
		return text;
	} else {
		var _result = "";
		if (node == null) { return _result; }
		for (var i = 0; i < node.childNodes.length; i++) {
			switch (node.childNodes[i].nodeType) {
				case 1: // ELEMENT_NODE
				case 5: // ENTITY_REFERENCE_NODE
					_result += htmlDom.textContent(node.childNodes[i]);
					break;
				case 3: // TEXT_NODE
				case 2: // ATTRIBUTE_NODE
				case 4: // CDATA_SECTION_NODE
					_result += node.childNodes[i].nodeValue;
					break;
				default:
					break;
			}
		}
		return _result;
	}
}

htmlDom.disableSelections = function(elements){
	for(var i=0;i<elements.length;i++)
		htmlDom.disableSelection(elements[i]);
}
htmlDom.enableSelections = function(elements){
	for(var i=0;i<elements.length;i++)
		htmlDom.enableSelection(elements[i]);
}

htmlDom.disableSelection = function(element){
	element = element||document.body;
	if(isIE()){
		element.unselectable = "on";
	}else{
		element.style.MozUserSelect = "none";
	}
}

htmlDom.enableSelection = function(element){
	element = element||document.body;
	if(isIE()){
		element.unselectable = "off";
	}else{
		element.style.MozUserSelect = "";
	}
}

htmlDom.hasParent = function (node) {
	return node && node.parentNode && htmlDom.isNode(node.parentNode);
}


//================================================
//实现浮点数的截取小数位数
//================================================
function adv_format(value,size){ //四舍五入
	var a_str = formatNumber(value,size);
	var a_int = parseFloat(a_str);
	if (value.toString().length>a_str.length){
		var b_str = value.toString().substring(a_str.length,a_str.length+1)
		var b_int = parseFloat(b_str);
		if (b_int<5){
			return a_str
		}else{
			var bonus_str,bonus_int;
			if (size==0){
				bonus_int = 1;
			}else{
				bonus_str = "0."
				for (var i=1; i<size; i++)
					bonus_str+="0";
				bonus_str+="1";
				bonus_int = parseFloat(bonus_str);
			}
			a_str = formatNumber(a_int + bonus_int, size)
		}
	}
	return a_str
}

function formatNumber(value,size) {//直接去尾
	var a,b,c,i
	a = value.toString();
	b = a.indexOf('.');
	c = a.length;
	if (size==0){
	if (b!=-1)
		a = a.substring(0,b);
	}else{
		if (b==-1){
			a = a + ".";
			for (i=1;i<=size;i++)
			a = a + "0";
		}else{
			a = a.substring(0,b+size+1);
			for (i=c;i<=b+size;i++)
				a = a + "0";
		}
	}
	return a
}
//================================================
createRpc = function(context,isRemote){
	var rpc = null;
	if(isRemote){
		rpc = new jQuery.rpc(context+"/remote");
		window[context+"_remote"] = rpc;
	}else{
		rpc = new jQuery.rpc(context+"/local");
		window[context+"_local"] = rpc;
	}
	return rpc;
};

$.rpc = function(url){
	this.url = url;
	/**
	 * 调用远程的业务方法
	 */
	this.invork = function(method,param,callBack,async){
		_self = this;
		if(async == null)
			async = true;
		var postBody = this.serialize({"params": param, "method": method});
		
		if(!async)
			return eval("result = " + $.ajax({
				url:_self.url,
				data:postBody,
				dataType:'json',
				type:'post',
				async:false
			}).responseText);
		else
			$.ajax({
				url:_self.url,
				data:postBody,
				dataType:'json',
				type:'post',
				success:function(json){
					if(json.error){//会话超时或者没有登录
						if(json.error.code==800)
							window.top.location.href = window.top.loginUrl+"?isRelogin=true";
						if(json.error.code==595){
							//window.location.href = json.error.msg;
							alert("拒绝访问!!!");
						}
					}else{
						$json.pareseReference(json,new Map());
						callBack(json);
					}
				}
			});
	};
	/**
	 * 串行化参数
	 */
	this.serialize = function(o){
		var objtype = typeof(o);
		if(objtype == "undefined"){
			return "undefined";
		}else if((objtype == "number")||(objtype == "boolean")){
			return o + "";
		}else if(o === null){
			return "null";
		}
		if (objtype == "string") { return o.escapeString(); }
		var me = arguments.callee;
		var newObj;
		if(typeof(o.__json__) == "function"){
			newObj = o.__json__();
			if(o !== newObj){
				return me(newObj);
			}
		}
		if(typeof(o.json) == "function"){
			newObj = o.json();
			if (o !== newObj) {
				return me(newObj);
			}
		}
		// array
		if(objtype != "function" && typeof(o.length) == "number"){
			var res = [];
			for(var i = 0; i < o.length; i++){
				var val = me(o[i]);
				if(typeof(val) != "string"){
					val = "undefined";
				}
				res.push(val);
			}
			return "[" + res.join(",") + "]";
		}

		// it's a function with no adapter, bad
		if(objtype == "function"){
			return null;
		}
		// generic object code path
		res = [];
		for (var k in o){
			var useKey;
			if (typeof(k) == "number"){
				useKey = '"' + k + '"';
			}else if (typeof(k) == "string"){
				useKey = k.escapeString();
			}else{
				// skip non-string or number keys
				continue;
			}
			val = me(o[k]);
			if(typeof(val) != "string"){
				// skip non-serializable values
				continue;
			}
			res.push(useKey + ":" + val);
		}
		return "{" + res.join(",") + "}";
	};
};


var $json= function(){};
/**
 * 将Json字符串转换成Javascript对象
 */
$json.evalJson = function(json){
	try {
		var result = eval("(" + json + ")");
		$json.pareseReference(result,new Map());
		return result;
	}catch(e){
		return json;
	}
}

$json.pareseArrayReference = function(json_array,referenceMap){
	if(typeof json_array == "string")
		$json._pareseReference(json_array,referenceMap);
	else{
		for(var i=0;i<json_array.length;i++){
			$json.pareseReference(json_array[i],referenceMap);
		}
	}
}

$json.pareseReference = function(obj,referenceMap){
	if (obj instanceof Array)
		$json.pareseArrayReference(obj,referenceMap);
	else if(obj instanceof Object)
		$json._pareseReference(obj,referenceMap);
}

$json._pareseReference = function(json_obj,referenceMap){
	var type = json_obj.JSONRPCType;
	var objectID = json_obj.objectID;
	if(type == null || type!="Reference")
		referenceMap.put(objectID,json_obj);

	var element,key,ref_element,list;
	for(var key in json_obj){
		if(key=="extend")
			continue;
		element = json_obj[key];
		if(element == null)
			continue;
		if(element instanceof Object){
			if(element.list != null && element.list instanceof Array){
				$json.pareseArrayReference(element.list,referenceMap);
			}else{
				objectID = element.objectID;
				if(element["referenceClass"] != null){
					var ref = referenceMap.get(objectID);
					if(ref != null)
						json_obj[key] = ref;
				}else{
					//分析成员对象的引用关系
					$json.pareseReference(element,referenceMap);
				}
			}
		}
	}
}

methodHistory = new Map();//历史请求

invorkLocal = function(context,method,param,callBack,async){
	if(async!=null && async==false)
		return _invork(context,method,param,null,null,false);
	else
		_invork(context,method,param,callBack,null,false);
}
invorkRemote = function(context,method,param,callBack,async){
	if(async!=null && async==false)
		return _invork(context,method,param,null,null,true);
	else
		_invork(context,method,param,callBack,null,true);
}

_invork = function(context,method,param,callBack,async,isRemote){
	var _param = methodHistory.get(method);

	if(_param != null && equals(_param,param)){
		if(!_param.finish)//如果上次调用还没有结束,则直接返回
			return;
	}

	methodHistory.put(method,param);
	
	var rpc = null;
	if(isRemote)
		rpc = window[context+"_remote"];
	else
		rpc = window[context+"_local"];

	if(!rpc)
		rpc = window.createRpc(context,isRemote);
	
	if(async!=null && async==false)
		return rpc.invork(method,param);
	else{
		callBack = callBack || function(){};
		var _callBack = function(json){
			param.finish = true;
			methodHistory.put(method,param);
			callBack(json);
		}
		rpc.invork(method,param,_callBack);
	}
}


var TrimPath;
(function() {               // Using a closure to keep global namespace clean.
     TrimPath = new Object();
     TrimPath.evalEx = function(src) { return eval(src); };

    var UNDEFINED;
    if (Array.prototype.pop == null)  // IE 5.x fix from Igor Poteryaev.
        Array.prototype.pop = function() {
            if (this.length === 0) {return UNDEFINED;}
            return this[--this.length];
        };
    if (Array.prototype.push == null) // IE 5.x fix from Igor Poteryaev.
        Array.prototype.push = function() {
            for (var i = 0; i < arguments.length; ++i) {this[this.length] = arguments[i];}
            return this.length;
        };

    TrimPath.parseTemplate = function(tmplContent, optTmplName, optEtc) {
        if (optEtc == null)
            optEtc = TrimPath.parseTemplate_etc;
        var funcSrc = parse(tmplContent, optTmplName, optEtc);
        var func = TrimPath.evalEx(funcSrc, optTmplName, 1);
        if (func != null)
            return new optEtc.Template(optTmplName, tmplContent, funcSrc, func, optEtc);
        return null;
    }
    
    TrimPath.parseTemplate_etc = {};            // Exposed for extensibility.
    // Lookup table for statement tags.
    TrimPath.parseTemplate_etc.statementDef = {}
    TrimPath.parseTemplate_etc.modifierDef = {
        "escape"     : function(s)    { return String(s).replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;"); },
        "capitalize" : function(s)    { return String(s).toUpperCase(); },
        "substring" : function(s,len)    {
        	var _len = s.replace(/[^\x00-\xff]/g,'11').length;
        	var tmp_str;
        	var index = len-1;
			if(_len>(len*2)){
				tmp_str=s.substring(0,index);
				while(tmp_str.replace(/[^\x00-\xff]/g,'11').length<len*2-2){
					index ++;
					tmp_str=s.substring(0,index);
				}
				s = tmp_str+"...";
			}
			return s;
        },
        "default": function(s, d) { return s != null ? s : d; }
    }

    TrimPath.parseTemplate_etc.modifierDef.h = TrimPath.parseTemplate_etc.modifierDef.escape;
    TrimPath.parseTemplate_etc.Template = function(tmplName, tmplContent, funcSrc, func, etc) {
        this.process = function(context, flags) {
            if (context == null)
                context = {};
            if (context._MODIFIERS == null)
                context._MODIFIERS = {};
            if (context.defined == null)
                context.defined = function(str) { return (context[str] != undefined); };
            for (var k in etc.modifierDef) {
                if (context._MODIFIERS[k] == null)
                    context._MODIFIERS[k] = etc.modifierDef[k];
            }
            if (flags == null)
                flags = {};
            var resultArr = [];
            var resultOut = {
            	write:function(m){
	           		resultArr.push(m); 
            	} 
            };
            try {
                func(resultOut, context, flags);
            } catch (e) {
                if (flags.throwExceptions == true)
                    throw e;
                //var result = new String(resultArr.join("") + "[ERROR: " + e.toString() + (e.message ? '; ' + e.message : '') + "]");
                //result["exception"] = e;
                result = null;
                return result;
            }
            return resultArr.join("");
        }
        this.name       = tmplName;
        this.source     = tmplContent; 
        this.sourceFunc = funcSrc;
        this.toString   = function() { return "TrimPath.Template [" + tmplName + "]"; }
    }
    TrimPath.parseTemplate_etc.ParseError = function(name, line, message) {
        this.name    = name;
        this.line    = line;
        this.message = message;
    }
    TrimPath.parseTemplate_etc.ParseError.prototype.toString = function() { 
        return ("TrimPath template ParseError in " + this.name + ": line " + this.line + ", " + this.message);
    }
    
    var parse = function(body, tmplName, etc) {
        body = cleanWhiteSpace(body);
        var funcText = [ "var TrimPath_Template_TEMP = function(_OUT, _CONTEXT, _FLAGS) { with (_CONTEXT) {" ];
        var state    = { stack: [], line: 1 };                              // TODO: Fix line number counting.
        var endStmtPrev = -1;
        while (endStmtPrev + 1 < body.length) {
            var begStmt = endStmtPrev;
            // Scan until we find some statement markup.
            begStmt = body.indexOf("{", begStmt + 1);
            while (begStmt >= 0) {
                var endStmt = body.indexOf('}', begStmt + 1);
                var stmt = body.substring(begStmt, endStmt);
                if (body.charAt(begStmt - 1) != '$' &&               // Not an expression or backslashed,
                           body.charAt(begStmt - 1) != '\\') {              // so check if it is a statement tag.
                    var offset = (body.charAt(begStmt + 1) == '/' ? 2 : 1); // Close tags offset of 2 skips '/'.
                                                                            // 10 is larger than maximum statement tag length.
                }
                begStmt = body.indexOf("{", begStmt + 1);
            }
            if (begStmt < 0)                              // In "a{for}c", begStmt will be 1.
                break;
            var endStmt = body.indexOf("}", begStmt + 1); // In "a{for}c", endStmt will be 5.
            if (endStmt < 0)
                break;
            emitSectionText(body.substring(endStmtPrev + 1, begStmt), funcText);
            emitStatement(body.substring(begStmt, endStmt + 1), state, funcText, tmplName, etc);
            endStmtPrev = endStmt;
        }
        emitSectionText(body.substring(endStmtPrev + 1), funcText);
        if (state.stack.length != 0)
            throw new etc.ParseError(tmplName, state.line, "unclosed, unmatched statement(s): " + state.stack.join(","));
        funcText.push("}}; TrimPath_Template_TEMP");
        return funcText.join("");
    }
    
    var emitStatement = function(stmtStr, state, funcText, tmplName, etc) {
        var parts = stmtStr.slice(1, -1).split(' ');
        emitSectionText(stmtStr, funcText);
        return;
    }

    var emitSectionText = function(text, funcText) {
        if (text.length <= 0)
            return;
        var nlPrefix = 0;               // Index to first non-newline in prefix.
        var nlSuffix = text.length - 1; // Index to first non-space/tab in suffix.
        while (nlPrefix < text.length && (text.charAt(nlPrefix) == '\n'))
            nlPrefix++;
        while (nlSuffix >= 0 && (text.charAt(nlSuffix) == ' ' || text.charAt(nlSuffix) == '\t'))
            nlSuffix--;
        if (nlSuffix < nlPrefix)
            nlSuffix = nlPrefix;
        var lines = text.substring(nlPrefix, nlSuffix + 1).split('\n');
        for (var i = 0; i < lines.length; i++) {
            emitSectionTextLine(lines[i], funcText);
            if (i < lines.length - 1)
                funcText.push('_OUT.write("\\n");\n');
        }
    }
    
    var emitSectionTextLine = function(line, funcText) {
        var endMarkPrev = '}';
        var endExprPrev = -1;
        while (endExprPrev + endMarkPrev.length < line.length) {
            var begMark = "#{", endMark = "}";
            var begExpr = line.indexOf(begMark, endExprPrev + endMarkPrev.length); // In "a#{b}c", begExpr == 1
            if (begExpr < 0)
                break;
            if (line.charAt(begExpr + 2) == '%') {
                begMark = "#{%";
                endMark = "%}";
            }
            var endExpr = line.indexOf(endMark, begExpr + begMark.length);         // In "a#{b}c", endExpr == 4;
            if (endExpr < 0)
                break;
            emitText(line.substring(endExprPrev + endMarkPrev.length, begExpr), funcText);                
            // Example: exprs == 'firstName|default:"John Doe"|capitalize'.split('|')
            var exprArr = line.substring(begExpr + begMark.length, endExpr).replace(/\|\|/g, "#@@#").split('|');
            for (var k in exprArr) {
                if (exprArr[k].replace) // IE 5.x fix from Igor Poteryaev.
                    exprArr[k] = exprArr[k].replace(/#@@#/g, '||');
            }
            funcText.push('try{_OUT.write(');
            emitExpression(exprArr, exprArr.length - 1, funcText); 
            funcText.push(');}catch(e){_OUT.write("");}');

//            funcText.push('_OUT.write(');
//            emitExpression(exprArr, exprArr.length - 1, funcText); 
//            funcText.push(');');

            endExprPrev = endExpr;
            endMarkPrev = endMark;
        }
        emitText(line.substring(endExprPrev + endMarkPrev.length), funcText); 
    }
    
    var emitText = function(text, funcText) {
        if (text == null ||
            text.length <= 0)
            return;
        text = text.replace(/\\/g, '\\\\');
        text = text.replace(/\n/g, '\\n');
        text = text.replace(/"/g,  '\\"');
        funcText.push('_OUT.write("');
        funcText.push(text);
        funcText.push('");');
    }
    
    var emitExpression = function(exprArr, index, funcText) {
        // Ex: foo|a:x|b:y1,y2|c:z1,z2 is emitted as c(b(a(foo,x),y1,y2),z1,z2)
        var expr = exprArr[index]; // Ex: exprArr == [firstName,capitalize,default:"John Doe"]
        if (index <= 0) {          // Ex: expr    == 'default:"John Doe"'
            funcText.push(expr);
            return;
        }
        var parts = expr.split(':');
        funcText.push('_MODIFIERS["');
        funcText.push(parts[0]); // The parts[0] is a modifier function name, like capitalize.
        funcText.push('"](');
        emitExpression(exprArr, index - 1, funcText);
        if (parts.length > 1) {
            funcText.push(',');
            funcText.push(parts[1]);
        }
        funcText.push(')');
    }

    var cleanWhiteSpace = function(result) {
        result = result.replace(/\t/g,   "    ");
        result = result.replace(/\r\n/g, "\n");
        result = result.replace(/\r/g,   "\n");
        result = result.replace(/^(\s*\S*(\s+\S+)*)\s*$/, '$1'); // Right trim by Igor Poteryaev.
        return result;
    }

    var scrubWhiteSpace = function(result) {
        result = result.replace(/^\s+/g,   "");
        result = result.replace(/\s+$/g,   "");
        result = result.replace(/\s+/g,   " ");
        result = result.replace(/^(\s*\S*(\s+\S+)*)\s*$/, '$1'); // Right trim by Igor Poteryaev.
        return result;
    }

    TrimPath.parseDOMTemplate = function(elementId, optDocument, optEtc) {
        if (optDocument == null)
            optDocument = document;
        var element = optDocument.getElementById(elementId);
        var content = element.value;     // Like textarea.value.
        if (content == null)
            content = element.innerHTML; // Like textarea.innerHTML.
        content = content.replace(/&lt;/g, "<").replace(/&gt;/g, ">");
        return TrimPath.parseTemplate(content, elementId, optEtc);
    }

    TrimPath.processDOMTemplate = function(elementId, context, optFlags, optDocument, optEtc) {
        return TrimPath.parseDOMTemplate(elementId, optDocument, optEtc).process(context, optFlags);
    }
}) ();

// ========================================================================
//  XML to Json
// ========================================================================
if ( typeof(XML) == 'undefined' ) XML = function() {};
XML.ObjTree = function () {
    return this;
};
XML.ObjTree.VERSION = "0.24";
XML.ObjTree.prototype.xmlDecl = '<?xml version="1.0" encoding="UTF-8" ?>\n';
XML.ObjTree.prototype.attr_prefix = '';
XML.ObjTree.prototype.overrideMimeType = 'text/xml';
XML.ObjTree.prototype.parseXML = function ( xml ) {
    var root;
    if ( window.DOMParser ) {
        var xmldom = new DOMParser();
//      xmldom.async = false;           // DOMParser is always sync-mode
        var dom = xmldom.parseFromString( xml, "application/xml" );
        if ( ! dom ) return;
        root = dom.documentElement;
    } else if ( window.ActiveXObject ) {
        xmldom = new ActiveXObject('Microsoft.XMLDOM');
        xmldom.async = false;
        xmldom.loadXML( xml );
        root = xmldom.documentElement;
    }
    if ( ! root ) return;
    
    return this.parseDOM( root );
};
XML.ObjTree.prototype.parseHTTP = function ( url, options, callback ) {
	var trans = $.ajax({
				url:url,
				data:options,
				dataType:'xml',
				type:'post',
				async:false
	});
    if ( trans && trans.responseXML && trans.responseXML.documentElement ) {
        return this.parseDOM( trans.responseXML.documentElement );
    } else if ( trans && trans.responseText ) {
        return this.parseXML( trans.responseText );
    }
}
XML.ObjTree.prototype.parseDOM = function ( root ) {
    if ( ! root ) return;

    this.__force_array = {};
    if ( this.force_array ) {
        for( var i=0; i<this.force_array.length; i++ ) {
            this.__force_array[this.force_array[i]] = 1;
        }
    }

    var json = this.parseElement( root );   // parse root node
    if ( this.__force_array[root.nodeName] ) {
        json = [ json ];
    }
    if ( root.nodeType != 11 ) {            // DOCUMENT_FRAGMENT_NODE
        var tmp = {};
        tmp[root.nodeName] = json;          // root nodeName
        json = tmp;
    }
    return json;
};
XML.ObjTree.prototype.parseElement = function ( elem ) {
    //  COMMENT_NODE
    if ( elem.nodeType == 7 ) {
        return;
    }
    //  TEXT_NODE CDATA_SECTION_NODE
    if ( elem.nodeType == 3 || elem.nodeType == 4 ) {
        var bool = elem.nodeValue.match( /[^\x00-\x20]/ );
        if ( bool == null ) return;     // ignore white spaces
        return elem.nodeValue;
    }
    var retval;
    var cnt = {};
    //  parse attributes
    if ( elem.attributes && elem.attributes.length ) {
        retval = {};
        for ( var i=0; i<elem.attributes.length; i++ ) {
            var key = elem.attributes[i].nodeName;
            if ( typeof(key) != "string" ) continue;
            var val = elem.attributes[i].nodeValue;
            if ( ! val ) continue;
            key = this.attr_prefix + key;
            if ( typeof(cnt[key]) == "undefined" ) cnt[key] = 0;
            cnt[key] ++;
            this.addNode( retval, key, cnt[key], val );
        }
    }

    //  parse child nodes (recursive)
    if ( elem.childNodes && elem.childNodes.length ) {
        var textonly = true;
        if ( retval ) textonly = false;        // some attributes exists
        for ( var i=0; i<elem.childNodes.length && textonly; i++ ) {
            var ntype = elem.childNodes[i].nodeType;
            if ( ntype == 3 || ntype == 4 ) continue;
            textonly = false;
        }
        if ( textonly ) {
            if ( ! retval ) retval = "";
            for ( var i=0; i<elem.childNodes.length; i++ ) {
                retval += elem.childNodes[i].nodeValue;
            }
        } else {
            if ( ! retval ) retval = {};
            for ( var i=0; i<elem.childNodes.length; i++ ) {
                var key = elem.childNodes[i].nodeName;
                if ( typeof(key) != "string" ) continue;
                var val = this.parseElement( elem.childNodes[i] );
                if ( ! val ) continue;
                if ( typeof(cnt[key]) == "undefined" ) cnt[key] = 0;
                cnt[key] ++;
                this.addNode( retval, key, cnt[key], val );
            }
        }
    }
    return retval;
};
XML.ObjTree.prototype.addNode = function ( hash, key, cnts, val ) {
    if ( this.__force_array[key] ) {
        if ( cnts == 1 ) hash[key] = [];
        hash[key][hash[key].length] = val;      // push
    } else if ( cnts == 1 ) {                   // 1st sibling
        hash[key] = val;
    } else if ( cnts == 2 ) {                   // 2nd sibling
        hash[key] = [ hash[key], val ];
    } else {                                    // 3rd sibling and more
        hash[key][hash[key].length] = val;
    }
};




//=========================================================
// 验证处理
//=========================================================
var validate = function(oForm,callBack){
	this.chChecked = true;
	this.callBacks = callBack;
	this.sRegcode = {
		//日期格式2004-08-10 date
		date:"^(?:([0-9]{4}-(?:(?:0?[1,3-9]|1[0-2])-(?:29|30)|((?:0?[13578]|1[02])-31)))|([0-9]{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(((?:(\\d\\d(?:0[48]|[2468][048]|[13579][26]))|(?:0[48]00|[2468][048]00|[13579][26]00))-0?2-29)))$", 
		//双字节 doub
		doub:"^[^\\x00-\\xff]+$",
		//Double
		Double:"^(\\d)+\\.+(\\d{2})$",
		//中文 chinese
		chinese:"^[\\u4e00-\\u9fa5]+$",
		//中文、字母、数字、下划线、点、空格，不能以数字与符号开头 userName
		userName:"^([A-Za-z\\u4e00-\\u9fa5]{1,})([A-Za-z0-9\\_\\.\\s\\u4e00-\\u9fa5]|)+$",
		//字母 chr
		chr:"^[A-Za-z]+$",
		//字母与数字 charNum
		charNum:"^[A-Za-z0-9]+$",
		//字母与数字与下划线 charNumUline 
		charNumUline:"[A-Za-z]{1}\\w{5}",
		//数字 numb
		numb:"^\\d+$",
		numb6:"\\d{6}",
		//中国电话号码 phone 020-12345678|02012345678|0212-1234567|0212-12345678|021212345678
		phone:"(^\\d{3}(\\-)?(\\d{8})$)|(^\\d{4}(\\-)?(\\d{7,8})$)",
		//手机 Mobile
		Mobile:"^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(13|15)\\d{9}$",
		//中国邮政编码 postcode
		postcode:"^[1-9]\\d{5}(?!\\d)$",
		//URL地址 Url 格式http://aaa.bbb?cc=xx a只能是字母、数字、中线，b、c、x的都为任意字符
		Url :"^http:\\\/\\\/[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+[\\\/=\\?%\\-&_~`@[\\]\\':+!]*([^<>\\\"\\\"])*$",
		//URL地址2 Url2 地址格式只能以数字和字母开头，必须包含一个点，不能以第一个点结尾，第一个点前只能是数字、字母、中线、斜线/、与冒号、点后为任意字符
		Url2:"^([A-Za-z0-9]{1,})(([A-Za-z0-9\\-\\\/\\:]|)+\\.[A-Za-z0-9\\-]+[\\\/=\\?%\\-&_~`@[\\]\\':+!]*([^<>\\\"\\\"])*)$",
		//身份证号 IDCard
		IDCard:"^\\d{15}$|^\\d{17}[\\d{1}|x]$",
		//电子邮件 Email
		//Email:"^([a-zA-Z0-9_\\-])([a-zA-Z0-9_\\-\\.]*)@(\\[((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}|((([a-zA-Z0-9\\-]+)\\.)+))([a-zA-Z]{2,}|(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\])$",
		Email:"^([a-zA-Z0-9_\\-])([a-zA-Z0-9_\\-\\.]*)@([a-zA-Z0-9_\\-])([a-zA-Z0-9_\\-\\.]*)$",
		//时间23:59 time
		time:"^(1|01|2|02|3|03|4|04|5|05|6|06|7|07|8|08|9|09|10|11|12|13|14|15|16|17|18|19|20|21|22|23|00{1,2}):(([0-5]{1}[0-9]{1}\\s{0,1}))$"
	}
	this.els = oForm.getElementsByTagName("*");
	this.init();
}

validate.prototype = {
	init:function(){
		//遍历所有元素
		for(var i=0;i<this.els.length;i++){
			//是否需要验证
			if(this.els[i].getAttribute("check") && this.els[i].getAttribute("check") != "false"){
				if(!this.callBacks){
					var div = document.createElement("div");
					this.els[i].parentNode.parentNode.appendChild(div);
				}
				
				$(this.els[i]).keyup(function(e){
					this._self.checked(this._self,this.src);
				}.bind({src:this.els[i],_self:this})).change(function(e){
					this._self.checked(this._self,this.src);
				}.bind({src:this.els[i],_self:this})).focus(function(e){
					this._self.checked(this._self,this.src);
				}.bind({src:this.els[i],_self:this}))
			}
		}
	},
	Checkover:function(obj){
		if(this.chChecked == true)
			this.checked(this,obj);
	},
	chCheck:function(){
		this.chChecked = true;
		for(var i=0;i<this.els.length;i++){
			if(this.els[i].getAttribute("check") && this.els[i].getAttribute("check") != "false"){
				//var div = document.createElement("div");
				this.checked(this,this.els[i]);
				if(this.chChecked == false)
					return false;
			}
		}
	  return true;
	},
	GoBack:function(el){
		//取得表单元素的类型
    var sType = el.type;
    try {
    	 switch(sType){
	        case "text":
	        case "hidden":
	        case "password":
	        case "file":
	        case "textarea": el.focus();
	        break;
	        case "checkbox":
	        case "radio": var els = document.getElementsByName(el.name);els[0].focus();
	        break;
	        case "select-one":
	        case "select-multiple"://el.focus();
	        break;
	        default:break;
		  }
		  return true;
    } catch (e) {
    	return false;//在IE下被隐藏的表单元素不能focus；利用这个特性进行判断表单是否可见。Firefox暂无解决方案；
    }
	},
	callBack : function (warning,src){
		if(this.callBacks){this.callBacks(warning,src);return;}
    	if(warning == "ok"){
    		src.parentNode.parentNode.lastChild.className = "validateok";
	        src.parentNode.parentNode.lastChild.innerHTML = "";//"填写正确";
	        src.parentNode.parentNode.lastChild.style.display = "";
	        src.style.backgroundColor = "#a9fea9";
	        src.style.color = "";
	        src.style.border = "1px solid #aaa";
    	}else if(warning == "pass"){
	    	src.parentNode.parentNode.lastChild.className = "validatepass";
			src.parentNode.parentNode.lastChild.style.display = "none";
	      	src.parentNode.parentNode.lastChild.innerHTML=""//src.getAttribute("warning");
	      	src.style.backgroundColor = "";
	      	src.style.color = "";
	      	 src.style.border = "1px solid #aaa";
    	}else{
    		src.parentNode.parentNode.lastChild.className = "validate";
	        src.parentNode.parentNode.lastChild.innerHTML = warning;
	        src.parentNode.parentNode.lastChild.style.display = "";
	        src.style.backgroundColor = "#fff4f4";
	        src.style.color = "red";
	         src.style.border = "1px solid #aaa";
    	}
    },
	checked:function(_self,src){
		//取得验证的正则字符串
		var warning ="";
		if (_self.sRegcode[src.getAttribute("check")])
			_self.sReg = _self.sRegcode[src.getAttribute("check")]
		else
			_self.sReg = src.getAttribute("check");
			
		//取得表单的值,用通用取值函数
		_self.sVal = _self.GetValue(src);
		
	  	required = src.getAttribute("required");
	  	EmaxLength = src.getAttribute("EmaxLength");
	  	warning = src.getAttribute("warning");
	  	formID = src.getAttribute("for");
		if(_self.sVal || required || formID || src.tagName == "SELECT"){//判断不为空或不允许为空
	     	//字符串->正则表达式,不区分大小写
	       	var checked=true;
	       	if ((EmaxLength && EmaxLength!="") && _self.sVal !=""){
	       		if(EmaxLength<_self.sVal.replace(/[^\x00-\xff]/g,'11').length){
			        warning = "长度不能超过"+EmaxLength+"个英文字符或" + EmaxLength/2+"个中文字符";
			        checked =false;
	       		}else{
	       			if(_self.sReg != "true"){
				      _self.reg = new RegExp(_self.sReg);
				      checked = _self.reg.test(_self.sVal)
				    }
	       		}
	       	}else{
					if(_self.sReg != "true"){
				      _self.reg = new RegExp(_self.sReg);
				      checked = _self.reg.test(_self.sVal)
				    }       		
	       	}
		    if(!_self.sVal && required){
		    	warning = required;
				  checked =false;
		    }
		    if(formID && $.elem(formID).value !=_self.sVal){
		    	checked =false;
		    }
	      if(!checked)
	      {
	        //验证不通过,提示warning
	        this.callBack(warning,src)
	        //alert(src.getAttribute("warning"));
	        //该表单元素取得焦点,用通用返回函数
	        if(_self.GoBack(src))
		        _self.chChecked = false;
	        //return false;
	      }else{
	        this.callBack("ok",src)
	      }
		}else{
			this.callBack("pass",src)
		}
	},
	GetValue:function(el){
		//取得表单元素的类型
    var sType = el.type;
    switch(sType)
    {
        case "text":
        case "hidden":
        case "password":
        case "file":
        case "textarea": return el.value;
        break;
        case "checkbox":
        case "radio": return GetValueChoose(el);
        break;
        case "select-one":
        case "select-multiple": return GetValueSel(el);
        break;
        default:break;
    }
    
    
    //取得radio,checkbox的选中数,用"0"来表示选中的个数,我们写正则的时候就可以通过0{1,}来表示选中个数
    function GetValueChoose(el)
    {
        var sValue = "";
        //取得第一个元素的name,搜索这个元素组
        var tmpels = document.getElementsByName(el.name);
        for(var i=0;i<tmpels.length;i++)
        {
            if(tmpels[i].checked)
            {
                sValue += "0";
            }
        }
        return sValue;
    }
    //取得select的选中数,用"0"来表示选中的个数,我们写正则的时候就可以通过0{1,}来表示选中个数
    function GetValueSel(el)
    {
        var sValue = "";
        for(var i=0;i<el.options.length;i++)
        {
            //单选下拉框提示选项设置为value=""
            if(el.options[i].selected && el.options[i].value!="")
            {
                sValue += "0";
            }
        }
        return sValue;
    }
	}
}


//扩展TrimPath的自定义的数据限定器
var customModifiers = {}

function TemplateBody(){
	this.extensions = null;
	this.templateElems = null;
	this.templates = null;
	this.itemCount = 0;
}

//====================================================================
// 模块解析引擎 
//====================================================================
/**
 * 负责文档解析及模板迭代的处理类
 */
function TemplateEngine(){
	this.dsPools = new Map();
	this.tempateBodies = new Map();
};
TemplateEngine.prototype = {
	/**
	 * 初始化JsonDataSource处理器
	 */
	initJsonDataSource:function(elem,_ds,itrProcessor,extensions){
		var select = elem.getAttribute("select");
		if(_ds!= null && typeof _ds == "object"){
			if(extensions && extensions.dataFilter)
				_ds = extensions.dataFilter(_ds);
			//根据数据进行绑定与迭代
			itrProcessor(new JsonDataSource(_ds,select));
		}else{
			var ds = elem.getAttribute("ds");
			if(ds != null){
				var dsItem = $.elem(ds);
				if(dsItem == null)
					throw {message:"数据源 \""+ds+"\" 定义无效"};
				this._doInitDS(dsItem,_ds,select,itrProcessor,extensions);
			}else{
				this._doInitDS(elem,_ds,select,itrProcessor,extensions);
			}
		}
	},
	/**
	 * 初始化数据源
	 */
	_doInitDS:function(elem,_ds,select,itrProcessor,extensions){
		var error = null;
		if(elem == null){
			sys.raise("指定的数据源不存在!");
		}

		var service = elem.getAttribute("service");
		//处理动态数据源
		if(service != null){
			var rpc = elem.getAttribute("context");
			//如果没有申明RPC服务,则使用系统的缺省RPC远程服务
			rpc = rpc || "/portal";
			var params = elem.getAttribute("params");
			if(!eval(params))
				params = "[]";
			var callBack = function(json){
				itrProcessor(new JsonDataSource(json.result,select));
			}
			var exp = "invorkRemote('"+rpc+"','"+service+"',"+params+",callBack);";
			eval(exp);
		}else{//静态数据源的处理
			var json = null;
			var json_url;
			if(_ds!=null){
				json_url = _ds;
			}else{
				json_url = elem.getAttribute("json");
			}
			if(json_url != null){
				DurationTimeDebug.start();
				var key1 = json_url || elem.id; 
				var key2 = elem.id || json_url;
				var key;
				if(this.dsPools.containsKey(key1)){//查询是否已经存在于缓存中
					json = this.dsPools.get(key1);
					key =key1;
				}else if(this.dsPools.containsKey(key2)){
					json = this.dsPools.get(key2);
					key =key2;
				}else{
					var jsonUtils = new XML.ObjTree();
					json = jsonUtils.parseHTTP(json_url);
					key = json_url;
				}

				this.dsPools.put(key,json);

				if(extensions && extensions.dataFilter){
					json = extensions.dataFilter(json);
					select = null;
				}
				DurationTimeDebug.finish("构造XML数据的持续时间: ");
				//根据数据进行绑定与迭代
				itrProcessor(new JsonDataSource(json,select));
			}
		}
	},
	fillData:function(elem,context){
		var area = $(elem);
		area.show();
		area.find("input").each(function(){
			this.value = context[this.name];
		});
		area.find("textarea").each(function(){
			this.value = context[this.name];
		});
	},
	/**
	 * 绑定明细信息
	 * detailDom
	 * 	要绑定的明细区域元素
	 * _ds
	 * 	数据源
	 * callBack
	 * 	绑定完成后的回调方法
	 */
	bindDetail:function(detailDom,_ds,extensions,callBack){
		var isLoaded = detailDom.getAttribute("isLoaded");
		if(isLoaded == null || isLoaded=="false"){
			//如果没有定义数据过滤器,则使用系统缺省提供的
			extensions = extensions || {dataFilter:function(ds){
				var rfInfo = detailDom.getAttribute("rfInfo");
				if(rfInfo){
					rfInfo = rfInfo.split("=");
				}
				//获取数据路径
				var select = detailDom.getAttribute("select");
				if(select){
					select = select.replace(/\//g,".");
					var datas = eval("ds."+select);
					if(!lang.isArray(datas))
						datas = [datas];
					var len = datas.length;
					//进行数据过滤
					for(var i=0;i<len;i++){
						if(datas[i][rfInfo[0]]==rfInfo[1]){
							ds = datas[i];
							break;
						}
					}
				}
				return ds;
			}};

			this.bind(detailDom,_ds,extensions,callBack);
			//设置加载完毕的标志
			detailDom.setAttribute("isLoaded","true");
		}
	},
	
	/**
	 * 迭代当前元素中的数据
	 * callBack: 迭代完成后的回调方法
	 */
	bind:function(dom,_ds,extensions,callBack){
		sys.innerMesg(dom.id,"请稍候,正在处理数据....",document.body,dom);
		var _self = this;
		//迭代处理器
		var itrProcessor = function(jsonXpath){
			//构造循环体
			var loopEntity = _self._initLoopInfo(dom);
			//初始化或者从缓存里获取当前dom所对应的Itertor实体
			var templateBody = null;
			if(_self.tempateBodies.containsKey(loopEntity.id)){
				templateBody = _self.tempateBodies.get(loopEntity.id);
			}else{
				//构造模板实体
				templateBody = new TemplateBody();
				templateBody.id = loopEntity.id;
				//分析与构造模板中的元素
				templateBody.templateElems = loopEntity.loopBody.cloneNode(true);

				//迭代实体的扩展项
				templateBody.extensions = extensions != null?extensions:{};

				var rows = [];
				if(dom.tagName == "TABLE" && !isIE()){
					rows = templateBody.templateElems.rows;
				}else{
					rows = templateBody.templateElems.childNodes;
				}
				
				templateBody.templateElems = [];
				templateBody.templates = [];
				var elem;
				var len = rows.length;
				var tmpDiv;
				for(var i=0;i<len;i++){//构造模板信息
					elem = rows[i];
					if(!(elem.nodeType == 3 || elem.nodeType == 4 ||elem.nodeType == 8)){
						templateBody.templateElems.push(elem);
						
						var tmp = elem.cloneNode(true);
						if(templateBody.extensions.pKey)
							tmp.setAttribute("id",dom.id+"_#{"+templateBody.extensions.pKey+"}");

						//保存模板
						tmpDiv = document.createElement("DIV");
						tmpDiv.appendChild(tmp);
						var tmpStr = tmpDiv.innerHTML;
						//处理模板字符
						tmpStr = tmpStr.
						replace(/%7B/g,"{").
						replace(/%7C/g,"|").
						replace(/%7D/g,"}").
						replace(/value=""/g,"").
						replace(/pwd=/g,"value=").
						replace(/visible=/g,"style=");
						
						if(templateBody.extensions.embedMark){
							eval("tmpStr = tmpStr.replace(/"+extensions.embedMark+"/g,\"#\");");
						}
						templateBody.templates.push(TrimPath.parseTemplate(tmpStr));
					}
				}
			}
			
			templateBody.jsonXPath = jsonXpath;//保存数据源
			templateBody.dom = dom;//保存迭代体的在DOM元素
			templateBody.loopBody = loopEntity.loopBody;//保存迭代体
			templateBody.ui = loopEntity.ui;//保存UI类型

			//保存当前的迭代实体,当下次调用时,可以直接从缓存里获取
			_self.tempateBodies.put(loopEntity.id,templateBody);
			//形如处理迭代体
			window.setTimeout(function(){
				DurationTimeDebug.start();
				_self._bindItems(templateBody);
				DurationTimeDebug.finish("处理的记录个数："+templateBody.itemCount+";&nbsp;页面处理的持续时间");
	
				//执行解析完成后的回调方法
				if(callBack != null && lang.isFunction(callBack)){
					callBack();
				}
				sys.finish(dom.id);
			}.bind(this),2);
		};

		//初始化数据源,并根据迭代处理器进行数据迭代
		this.initJsonDataSource(dom,_ds,itrProcessor,extensions);
	},
	getUserObject:function(domId,field,value){
		var templateBody = this.tempateBodies.get(domId);
		if(templateBody == null)
			return null;

		if(value.tagName)
			value = value.getAttribute("rowId");
		var datas = templateBody.jsonXPath.data;
		var len = datas.length;
		var result = null;
		var _v;
		for(var i=0;i<len;i++){
			result = datas[i];
			_v = eval("result."+field);
			if(value == _v){
				break;
			}
		}
		return result;
	},
	/**
	 * 处理循环体
	 */
	_bindItems:function(templateBody){
		var nodes = [];
		templateBody.itemCount = 0;
		//利用构造器生成DOM元素
		var data = templateBody.jsonXPath.data;
		data._self = data;
		if(templateBody.extensions.buildItems)
			templateBody.extensions.buildItems(templateBody,nodes,data);
		else
			this._defaultBuildItems(templateBody,nodes,data);
		
		//将DOM元素的字符串添加到DOM容器
		if(templateBody.ui =="tree"){//Firefox表格的InnerHTML是可读写的
			templateBody.loopBody.innerHTML = nodes.join("");
		}else{//因为IE里的表格对象及TR/TBody对象的InnerHTML是只读的，所以在内在中先模拟生成一个表，然后将基注入到原始表格对象的父容器中
//			if(nodes.length>50){
//				this._generateTable(templateBody,nodes);
//			}else{
				if(!isIE() || templateBody.dom.tagName !="TABLE"){
					templateBody.loopBody.innerHTML = nodes.join("");
					return;
				}
				var tbBuffer = new StringBuffer();
				tbBuffer.append("<table");
				var attrs = templateBody.dom.attributes;
				var attr;
				for(var i=0;i<attrs.length;i++){
					attr = attrs[i];
					if(attr.specified)
						tbBuffer.append(" "+attr.nodeName+"=\""+attrs[i].nodeValue+"\"");
				}
				tbBuffer.append(">")
				//添加THead
				tbBuffer.append(templateBody.dom.tHead?templateBody.dom.tHead.outerHTML:"");
				//添加TBody
				tbBuffer.append(nodes.join(""));
				//添加TFoot
				tbBuffer.append(templateBody.dom.tFoot?templateBody.dom.tFoot.outerHTML:"");
				tbBuffer.append("</table>");
				
				templateBody.dom.outerHTML = tbBuffer.toString();
//			}
			sys.gc();
		}
	},
	/**
	 * 异步添加记录行
	 */
	_generateTable:function(templateBody,nodes){
		if(templateBody.timer){
			window.clearInterval(templateBody.timer);
		}
		
		var len = templateBody.loopBody.childNodes.length;
		for(var i=0;i<len;i++){
			templateBody.loopBody.removeChild(templateBody.loopBody.childNodes[0]);
		}
		var len = nodes.length;
		var steps = Math.floor(len/50);
		if(len %50!=0)
			steps ++;
		var tmpDiv = document.createElement("div");
		templateBody.timer = window.setInterval(function(){
			var tBody,r;
			if(nodes.length<=0){
				sys.finish(templateBody.dom.id);
				window.clearInterval(templateBody.timer);
				return;
			}
			var tbBuffer = new StringBuffer();
			tbBuffer.append("<table>");
			//添加TBody
			tbBuffer.append(nodes.splice(0,50).join(""));
			tbBuffer.append("</table>");
			tmpDiv.innerHTML = tbBuffer.toString();
			tBody = tmpDiv.firstChild.tBodies[0];
			for(r=0;r<tBody.rows.length;r++){
				templateBody.loopBody.appendChild(tBody.rows[r].cloneNode(true));
			}
		},500);
	},
	/**
	 * 获取当前要进行渲染的模板
	 */
	_getTemplate:function(templateBody,data){
		var template = [];
		var when;
		var elem,value;
		var len = templateBody.templateElems.length;
		for(var i=0;i<len;i++){
			elem = templateBody.templateElems[i];
			when = $a(elem,"when");
			if(when != null && when != ""){
				value = Expression.process(when,data);
				if(value != null && (value=="true" || value == true)){
					template.push(templateBody.templates[i]);
				}
			}else{
				template.push(templateBody.templates[i]);
			}
		}

		return template;
	},
	/**
	 * 初始化循环体对象
	 */
	_initLoopInfo:function(dom){
		var template = dom.getAttribute("template");
		if(template !=null && template != ""){
			var template = document.getElementById(template).cloneNode(true);
			template.id = "";
			template.style.display = "";
			dom.appendChild(template);
			return {id:dom.id,loopBody:dom,ui:$a(dom,"ui")};
		}

		var thead,tbody,tfoot,loopBody;
		thead = $elt("THEAD",dom,null,true);
		tbody = $elt("TBODY",dom,null,true);
		tfoot = $elt("TFOOT",dom,null,true);
		
		if(tbody != null){
			loopBody = tbody;
		}else{
			if(thead != null || tfoot != null){
				loopBody = dom.cloneNode(true);
			}else{
				loopBody = dom;
			}
		}
		var children = loopBody.childNodes;
		var child;
		for(var i=0;i<children.length;i++){
			child = children[i];
			if(child.tagName == "THEAD" || child.tagName == "TFOOT" ||(child.nodeType == 3 || child.nodeType == 4 ||child.nodeType == 8)){
				loopBody.removeChild(child);
			}
		}
		
		return {id:dom.id,loopBody:loopBody,ui:$a(dom,"ui")};
	},
	/**
	 * 缺省的元素构造器,用来循环生成DOM元素
	 */
	_defaultBuildItems:function(templateBody,nodes,datas){
		if(!lang.isArray(datas))
			datas = [datas];

		var len = datas.length;
		var data;
		var templates;
		var row;
		for(var i=0;i<len;i++){
			if(templateBody.cancelCurrentOperation == true){
				templateBody.cancelCurrentOperation = false;
				break;
			}
			data = datas[i];
			data._MODIFIERS = customModifiers;
			data._self = data;
			data["userDataRowIndex"] = i;
			templates = this._getTemplate(templateBody,data);
			for(var t=0;t<templates.length;t++){
				nodes.push(templates[t].process(data));
			}
		}
		templateBody.itemCount = len;
	}
}