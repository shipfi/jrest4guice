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