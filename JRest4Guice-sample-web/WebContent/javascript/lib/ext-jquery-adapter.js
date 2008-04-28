/*
 * Ext JS Library 2.0
 * Copyright(c) 2006-2007, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext={version:"2.0"};window["undefined"]=window["undefined"];Ext.apply=function(C,D,B){if(B){Ext.apply(C,B)}if(C&&D&&typeof D=="object"){for(var A in D){C[A]=D[A]}}return C};(function(){var idSeed=0;var ua=navigator.userAgent.toLowerCase();var isStrict=document.compatMode=="CSS1Compat",isOpera=ua.indexOf("opera")>-1,isSafari=(/webkit|khtml/).test(ua),isIE=!isOpera&&ua.indexOf("msie")>-1,isIE7=!isOpera&&ua.indexOf("msie 7")>-1,isGecko=!isSafari&&ua.indexOf("gecko")>-1,isBorderBox=isIE&&!isStrict,isWindows=(ua.indexOf("windows")!=-1||ua.indexOf("win32")!=-1),isMac=(ua.indexOf("macintosh")!=-1||ua.indexOf("mac os x")!=-1),isLinux=(ua.indexOf("linux")!=-1),isSecure=window.location.href.toLowerCase().indexOf("https")===0;if(isIE&&!isIE7){try{document.execCommand("BackgroundImageCache",false,true)}catch(e){}}Ext.apply(Ext,{isStrict:isStrict,isSecure:isSecure,isReady:false,enableGarbageCollector:true,enableListenerCollection:false,SSL_SECURE_URL:"javascript:false",BLANK_IMAGE_URL:"http:/"+"/extjs.com/s.gif",emptyFn:function(){},applyIf:function(o,c){if(o&&c){for(var p in c){if(typeof o[p]=="undefined"){o[p]=c[p]}}}return o},addBehaviors:function(o){if(!Ext.isReady){Ext.onReady(function(){Ext.addBehaviors(o)});return }var cache={};for(var b in o){var parts=b.split("@");if(parts[1]){var s=parts[0];if(!cache[s]){cache[s]=Ext.select(s)}cache[s].on(parts[1],o[b])}}cache=null},id:function(el,prefix){prefix=prefix||"ext-gen";el=Ext.getDom(el);var id=prefix+(++idSeed);return el?(el.id?el.id:(el.id=id)):id},extend:function(){var io=function(o){for(var m in o){this[m]=o[m]}};return function(sb,sp,overrides){if(typeof sp=="object"){overrides=sp;sp=sb;sb=function(){sp.apply(this,arguments)}}var F=function(){},sbp,spp=sp.prototype;F.prototype=spp;sbp=sb.prototype=new F();sbp.constructor=sb;sb.superclass=spp;if(spp.constructor==Object.prototype.constructor){spp.constructor=sp}sb.override=function(o){Ext.override(sb,o)};sbp.override=io;Ext.override(sb,overrides);return sb}}(),override:function(origclass,overrides){if(overrides){var p=origclass.prototype;for(var method in overrides){p[method]=overrides[method]}}},namespace:function(){var a=arguments,o=null,i,j,d,rt;for(i=0;i<a.length;++i){d=a[i].split(".");rt=d[0];eval("if (typeof "+rt+" == \"undefined\"){"+rt+" = {};} o = "+rt+";");for(j=1;j<d.length;++j){o[d[j]]=o[d[j]]||{};o=o[d[j]]}}},urlEncode:function(o){if(!o){return""}var buf=[];for(var key in o){var ov=o[key],k=encodeURIComponent(key);var type=typeof ov;if(type=="undefined"){buf.push(k,"=&")}else{if(type!="function"&&type!="object"){buf.push(k,"=",encodeURIComponent(ov),"&")}else{if(ov instanceof Array){if(ov.length){for(var i=0,len=ov.length;i<len;i++){buf.push(k,"=",encodeURIComponent(ov[i]===undefined?"":ov[i]),"&")}}else{buf.push(k,"=&")}}}}}buf.pop();return buf.join("")},urlDecode:function(string,overwrite){if(!string||!string.length){return{}}var obj={};var pairs=string.split("&");var pair,name,value;for(var i=0,len=pairs.length;i<len;i++){pair=pairs[i].split("=");name=decodeURIComponent(pair[0]);value=decodeURIComponent(pair[1]);if(overwrite!==true){if(typeof obj[name]=="undefined"){obj[name]=value}else{if(typeof obj[name]=="string"){obj[name]=[obj[name]];obj[name].push(value)}else{obj[name].push(value)}}}else{obj[name]=value}}return obj},each:function(array,fn,scope){if(typeof array.length=="undefined"||typeof array=="string"){array=[array]}for(var i=0,len=array.length;i<len;i++){if(fn.call(scope||array[i],array[i],i,array)===false){return i}}},combine:function(){var as=arguments,l=as.length,r=[];for(var i=0;i<l;i++){var a=as[i];if(a instanceof Array){r=r.concat(a)}else{if(a.length!==undefined&&!a.substr){r=r.concat(Array.prototype.slice.call(a,0))}else{r.push(a)}}}return r},escapeRe:function(s){return s.replace(/([.*+?^${}()|[\]\/\\])/g,"\\$1")},callback:function(cb,scope,args,delay){if(typeof cb=="function"){if(delay){cb.defer(delay,scope,args||[])}else{cb.apply(scope,args||[])}}},getDom:function(el){if(!el||!document){return null}return el.dom?el.dom:(typeof el=="string"?document.getElementById(el):el)},getDoc:function(){return Ext.get(document)},getBody:function(){return Ext.get(document.body||document.documentElement)},getCmp:function(id){return Ext.ComponentMgr.get(id)},num:function(v,defaultValue){if(typeof v!="number"){return defaultValue}return v},destroy:function(){for(var i=0,a=arguments,len=a.length;i<len;i++){var as=a[i];if(as){if(as.dom){as.removeAllListeners();as.remove();continue}if(typeof as.destroy=="function"){as.destroy()}}}},removeNode:isIE?function(){var d;return function(n){if(n){d=d||document.createElement("div");d.appendChild(n);d.innerHTML=""}}}():function(n){if(n&&n.parentNode){n.parentNode.removeChild(n)}},type:function(o){if(o===undefined||o===null){return false}if(o.htmlElement){return"element"}var t=typeof o;if(t=="object"&&o.nodeName){switch(o.nodeType){case 1:return"element";case 3:return(/\S/).test(o.nodeValue)?"textnode":"whitespace"}}if(t=="object"||t=="function"){switch(o.constructor){case Array:return"array";case RegExp:return"regexp"}if(typeof o.length=="number"&&typeof o.item=="function"){return"nodelist"}}return t},isEmpty:function(v,allowBlank){return v===null||v===undefined||(!allowBlank?v==="":false)},value:function(v,defaultValue,allowBlank){return Ext.isEmpty(v,allowBlank)?defaultValue:v},isOpera:isOpera,isSafari:isSafari,isIE:isIE,isIE6:isIE&&!isIE7,isIE7:isIE7,isGecko:isGecko,isBorderBox:isBorderBox,isLinux:isLinux,isWindows:isWindows,isMac:isMac,isAir:!!window.htmlControl,useShims:((isIE&&!isIE7)||(isGecko&&isMac))});Ext.ns=Ext.namespace})();Ext.ns("Ext","Ext.util","Ext.grid","Ext.dd","Ext.tree","Ext.data","Ext.form","Ext.menu","Ext.state","Ext.lib","Ext.layout","Ext.app","Ext.ux");Ext.apply(Function.prototype,{createCallback:function(){var A=arguments;var B=this;return function(){return B.apply(window,A)}},createDelegate:function(C,B,A){var D=this;return function(){var F=B||arguments;if(A===true){F=Array.prototype.slice.call(arguments,0);F=F.concat(B)}else{if(typeof A=="number"){F=Array.prototype.slice.call(arguments,0);var E=[A,0].concat(B);Array.prototype.splice.apply(F,E)}}return D.apply(C||window,F)}},defer:function(C,E,B,A){var D=this.createDelegate(E,B,A);if(C){return setTimeout(D,C)}D();return 0},createSequence:function(B,A){if(typeof B!="function"){return this}var C=this;return function(){var D=C.apply(this||window,arguments);B.apply(A||this||window,arguments);return D}},createInterceptor:function(B,A){if(typeof B!="function"){return this}var C=this;return function(){B.target=this;B.method=C;if(B.apply(A||this||window,arguments)===false){return }return C.apply(this||window,arguments)}}});Ext.applyIf(String,{escape:function(A){return A.replace(/('|\\)/g,"\\$1")},leftPad:function(D,B,C){var A=new String(D);if(C===null||C===undefined||C===""){C=" "}while(A.length<B){A=C+A}return A},format:function(B){var A=Array.prototype.slice.call(arguments,1);return B.replace(/\{(\d+)\}/g,function(C,D){return A[D]})}});String.prototype.toggle=function(B,A){return this==B?A:B};String.prototype.trim=function(){var A=/^\s+|\s+$/g;return function(){return this.replace(A,"")}}();Ext.applyIf(Number.prototype,{constrain:function(B,A){return Math.min(Math.max(this,B),A)}});Ext.applyIf(Array.prototype,{indexOf:function(C){for(var B=0,A=this.length;B<A;B++){if(this[B]==C){return B}}return -1},remove:function(B){var A=this.indexOf(B);if(A!=-1){this.splice(A,1)}return this}});Date.prototype.getElapsed=function(A){return Math.abs((A||new Date()).getTime()-this.getTime())};
if(typeof jQuery=="undefined"){throw"Unable to load Ext, jQuery not found."}(function(){var B;Ext.lib.Dom={getViewWidth:function(D){return D?Math.max(jQuery(document).width(),jQuery(window).width()):jQuery(window).width()},getViewHeight:function(D){return D?Math.max(jQuery(document).height(),jQuery(window).height()):jQuery(window).height()},isAncestor:function(E,F){E=Ext.getDom(E);F=Ext.getDom(F);if(!E||!F){return false}if(E.contains&&!Ext.isSafari){return E.contains(F)}else{if(E.compareDocumentPosition){return !!(E.compareDocumentPosition(F)&16)}else{var D=F.parentNode;while(D){if(D==E){return true}else{if(!D.tagName||D.tagName.toUpperCase()=="HTML"){return false}}D=D.parentNode}return false}}},getRegion:function(D){return Ext.lib.Region.getRegion(D)},getY:function(D){return this.getXY(D)[1]},getX:function(D){return this.getXY(D)[0]},getXY:function(F){var E,J,L,M,I=(document.body||document.documentElement);F=Ext.getDom(F);if(F==I){return[0,0]}if(F.getBoundingClientRect){L=F.getBoundingClientRect();M=C(document).getScroll();return[L.left+M.left,L.top+M.top]}var N=0,K=0;E=F;var D=C(F).getStyle("position")=="absolute";while(E){N+=E.offsetLeft;K+=E.offsetTop;if(!D&&C(E).getStyle("position")=="absolute"){D=true}if(Ext.isGecko){J=C(E);var O=parseInt(J.getStyle("borderTopWidth"),10)||0;var G=parseInt(J.getStyle("borderLeftWidth"),10)||0;N+=G;K+=O;if(E!=F&&J.getStyle("overflow")!="visible"){N+=G;K+=O}}E=E.offsetParent}if(Ext.isSafari&&D){N-=I.offsetLeft;K-=I.offsetTop}if(Ext.isGecko&&!D){var H=C(I);N+=parseInt(H.getStyle("borderLeftWidth"),10)||0;K+=parseInt(H.getStyle("borderTopWidth"),10)||0}E=F.parentNode;while(E&&E!=I){if(!Ext.isOpera||(E.tagName!="TR"&&C(E).getStyle("display")!="inline")){N-=E.scrollLeft;K-=E.scrollTop}E=E.parentNode}return[N,K]},setXY:function(D,E){D=Ext.fly(D,"_setXY");D.position();var F=D.translatePoints(E);if(E[0]!==false){D.dom.style.left=F.left+"px"}if(E[1]!==false){D.dom.style.top=F.top+"px"}},setX:function(E,D){this.setXY(E,[D,false])},setY:function(D,E){this.setXY(D,[false,E])}};function C(D){if(!B){B=new Ext.Element.Flyweight()}B.dom=D;return B}Ext.lib.Event={getPageX:function(D){D=D.browserEvent||D;return D.pageX},getPageY:function(D){D=D.browserEvent||D;return D.pageY},getXY:function(D){D=D.browserEvent||D;return[D.pageX,D.pageY]},getTarget:function(D){return D.target},on:function(H,D,G,F,E){jQuery(H).bind(D,G)},un:function(F,D,E){jQuery(F).unbind(D,E)},purgeElement:function(D){jQuery(D).unbind()},preventDefault:function(D){D=D.browserEvent||D;if(D.preventDefault){D.preventDefault()}else{D.returnValue=false}},stopPropagation:function(D){D=D.browserEvent||D;if(D.stopPropagation){D.stopPropagation()}else{D.cancelBubble=true}},stopEvent:function(D){this.preventDefault(D);this.stopPropagation(D)},onAvailable:function(I,E,D){var H=new Date();var F=function(){if(H.getElapsed()>10000){clearInterval(G)}var J=document.getElementById(I);if(J){clearInterval(G);E.call(D||window,J)}};var G=setInterval(F,50)},resolveTextNode:function(D){if(D&&3==D.nodeType){return D.parentNode}else{return D}},getRelatedTarget:function(E){E=E.browserEvent||E;var D=E.relatedTarget;if(!D){if(E.type=="mouseout"){D=E.toElement}else{if(E.type=="mouseover"){D=E.fromElement}}}return this.resolveTextNode(D)}};Ext.lib.Ajax=function(){var D=function(E){return function(G,F){if((F=="error"||F=="timeout")&&E.failure){E.failure.call(E.scope||window,{responseText:G.responseText,responseXML:G.responseXML,argument:E.argument})}else{if(E.success){E.success.call(E.scope||window,{responseText:G.responseText,responseXML:G.responseXML,argument:E.argument})}}}};return{request:function(J,G,E,H,F){var I={type:J,url:G,data:H,timeout:E.timeout,complete:D(E)};if(F){if(F.xmlData){I.data=F.xmlData;I.processData=false;I.type="POST";I.contentType="text/xml"}else{if(F.jsonData){I.data=typeof F.jsonData=="object"?Ext.encode(F.jsonData):F.jsonData;I.processData=false;I.type="POST";I.contentType="text/javascript"}}if(F.headers){I.beforeSend=function(M){var K=F.headers;for(var L in K){if(K.hasOwnProperty(L)){M.setRequestHeader(L,K[L])}}}}}jQuery.ajax(I)},formRequest:function(I,H,F,J,E,G){jQuery.ajax({type:Ext.getDom(I).method||"POST",url:H,data:jQuery(I).formSerialize()+(J?"&"+J:""),timeout:F.timeout,complete:D(F)})},isCallInProgress:function(E){return false},abort:function(E){return false},serializeForm:function(E){return jQuery(E.dom||E).formSerialize()}}}();Ext.lib.Anim=function(){var D=function(E,F){var G=true;return{stop:function(H){},isAnimated:function(){return G},proxyCallback:function(){G=false;Ext.callback(E,F)}}};return{scroll:function(H,F,J,K,E,G){var I=D(E,G);H=Ext.getDom(H);if(typeof F.scroll.to[0]=="number"){H.scrollLeft=F.scroll.to[0]}if(typeof F.scroll.to[1]=="number"){H.scrollTop=F.scroll.to[1]}I.proxyCallback();return I},motion:function(H,F,I,J,E,G){return this.run(H,F,I,J,E,G)},color:function(H,F,J,K,E,G){var I=D(E,G);I.proxyCallback();return I},run:function(F,N,I,M,G,P,O){var J=D(G,P),K=Ext.fly(F,"_animrun");var E={};for(var H in N){if(N[H].from){if(H!="points"){K.setStyle(H,N[H].from)}}switch(H){case"points":var L,R;K.position();if(L=N.points.by){var Q=K.getXY();R=K.translatePoints([Q[0]+L[0],Q[1]+L[1]])}else{R=K.translatePoints(N.points.to)}E.left=R.left;E.top=R.top;if(!parseInt(K.getStyle("left"),10)){K.setLeft(0)}if(!parseInt(K.getStyle("top"),10)){K.setTop(0)}if(N.points.from){K.setXY(N.points.from)}break;case"width":E.width=N.width.to;break;case"height":E.height=N.height.to;break;case"opacity":E.opacity=N.opacity.to;break;case"left":E.left=N.left.to;break;case"top":E.top=N.top.to;break;default:E[H]=N[H].to;break}}jQuery(F).animate(E,I*1000,undefined,J.proxyCallback);return J}}}();Ext.lib.Region=function(F,G,D,E){this.top=F;this[1]=F;this.right=G;this.bottom=D;this.left=E;this[0]=E};Ext.lib.Region.prototype={contains:function(D){return(D.left>=this.left&&D.right<=this.right&&D.top>=this.top&&D.bottom<=this.bottom)},getArea:function(){return((this.bottom-this.top)*(this.right-this.left))},intersect:function(H){var F=Math.max(this.top,H.top);var G=Math.min(this.right,H.right);var D=Math.min(this.bottom,H.bottom);var E=Math.max(this.left,H.left);if(D>=F&&G>=E){return new Ext.lib.Region(F,G,D,E)}else{return null}},union:function(H){var F=Math.min(this.top,H.top);var G=Math.max(this.right,H.right);var D=Math.max(this.bottom,H.bottom);var E=Math.min(this.left,H.left);return new Ext.lib.Region(F,G,D,E)},constrainTo:function(D){this.top=this.top.constrain(D.top,D.bottom);this.bottom=this.bottom.constrain(D.top,D.bottom);this.left=this.left.constrain(D.left,D.right);this.right=this.right.constrain(D.left,D.right);return this},adjust:function(F,E,D,G){this.top+=F;this.left+=E;this.right+=G;this.bottom+=D;return this}};Ext.lib.Region.getRegion=function(G){var I=Ext.lib.Dom.getXY(G);var F=I[1];var H=I[0]+G.offsetWidth;var D=I[1]+G.offsetHeight;var E=I[0];return new Ext.lib.Region(F,H,D,E)};Ext.lib.Point=function(D,E){if(D instanceof Array){E=D[1];D=D[0]}this.x=this.right=this.left=this[0]=D;this.y=this.top=this.bottom=this[1]=E};Ext.lib.Point.prototype=new Ext.lib.Region();if(Ext.isIE){function A(){var D=Function.prototype;delete D.createSequence;delete D.defer;delete D.createDelegate;delete D.createCallback;delete D.createInterceptor;window.detachEvent("onunload",A)}window.attachEvent("onunload",A)}})();

/*
 * Ext - JS Library 1.0 Alpha 2
 * Copyright(c) 2006-2007, Jack Slocum.
 */

/*
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * $LastChangedDate$
 * $Rev$
 */

jQuery.fn._height = jQuery.fn.height;
jQuery.fn._width  = jQuery.fn.width;

/**
 * If used on document, returns the document's height (innerHeight)
 * If used on window, returns the viewport's (window) height
 * See core docs on height() to see what happens when used on an element.
 *
 * @example $("#testdiv").height()
 * @result 200
 *
 * @example $(document).height()
 * @result 800
 *
 * @example $(window).height()
 * @result 400
 *
 * @name height
 * @type Object
 * @cat Plugins/Dimensions
 */
jQuery.fn.height = function() {
	if ( this[0] == window )
		return self.innerHeight ||
			jQuery.boxModel && document.documentElement.clientHeight ||
			document.body.clientHeight;

	if ( this[0] == document )
		return Math.max( document.body.scrollHeight, document.body.offsetHeight );

	return this._height(arguments[0]);
};

/**
 * If used on document, returns the document's width (innerWidth)
 * If used on window, returns the viewport's (window) width
 * See core docs on height() to see what happens when used on an element.
 *
 * @example $("#testdiv").width()
 * @result 200
 *
 * @example $(document).width()
 * @result 800
 *
 * @example $(window).width()
 * @result 400
 *
 * @name width
 * @type Object
 * @cat Plugins/Dimensions
 */
jQuery.fn.width = function() {
	if ( this[0] == window )
		return self.innerWidth ||
			jQuery.boxModel && document.documentElement.clientWidth ||
			document.body.clientWidth;

	if ( this[0] == document )
		return Math.max( document.body.scrollWidth, document.body.offsetWidth );

	return this._width(arguments[0]);
};

/**
 * Returns the inner height value (without border) for the first matched element.
 * If used on document, returns the document's height (innerHeight)
 * If used on window, returns the viewport's (window) height
 *
 * @example $("#testdiv").innerHeight()
 * @result 800
 *
 * @name innerHeight
 * @type Number
 * @cat Plugins/Dimensions
 */
jQuery.fn.innerHeight = function() {
	return this[0] == window || this[0] == document ?
		this.height() :
		this.css('display') != 'none' ?
		 	this[0].offsetHeight - (parseInt(this.css("borderTopWidth")) || 0) - (parseInt(this.css("borderBottomWidth")) || 0) :
			this.height() + (parseInt(this.css("paddingTop")) || 0) + (parseInt(this.css("paddingBottom")) || 0);
};

/**
 * Returns the inner width value (without border) for the first matched element.
 * If used on document, returns the document's Width (innerWidth)
 * If used on window, returns the viewport's (window) width
 *
 * @example $("#testdiv").innerWidth()
 * @result 1000
 *
 * @name innerWidth
 * @type Number
 * @cat Plugins/Dimensions
 */
jQuery.fn.innerWidth = function() {
	return this[0] == window || this[0] == document ?
		this.width() :
		this.css('display') != 'none' ?
			this[0].offsetWidth - (parseInt(this.css("borderLeftWidth")) || 0) - (parseInt(this.css("borderRightWidth")) || 0) :
			this.height() + (parseInt(this.css("paddingLeft")) || 0) + (parseInt(this.css("paddingRight")) || 0);
};

/**
 * Returns the outer height value (including border) for the first matched element.
 * Cannot be used on document or window.
 *
 * @example $("#testdiv").outerHeight()
 * @result 1000
 *
 * @name outerHeight
 * @type Number
 * @cat Plugins/Dimensions
 */
jQuery.fn.outerHeight = function() {
	return this[0] == window || this[0] == document ?
		this.height() :
		this.css('display') != 'none' ?
			this[0].offsetHeight :
			this.height() + (parseInt(this.css("borderTopWidth")) || 0) + (parseInt(this.css("borderBottomWidth")) || 0)
				+ (parseInt(this.css("paddingTop")) || 0) + (parseInt(this.css("paddingBottom")) || 0);
};

/**
 * Returns the outer width value (including border) for the first matched element.
 * Cannot be used on document or window.
 *
 * @example $("#testdiv").outerWidth()
 * @result 1000
 *
 * @name outerWidth
 * @type Number
 * @cat Plugins/Dimensions
 */
jQuery.fn.outerWidth = function() {
	return this[0] == window || this[0] == document ?
		this.width() :
		this.css('display') != 'none' ?
			this[0].offsetWidth :
			this.height() + (parseInt(this.css("borderLeftWidth")) || 0) + (parseInt(this.css("borderRightWidth")) || 0)
				+ (parseInt(this.css("paddingLeft")) || 0) + (parseInt(this.css("paddingRight")) || 0);
};

/**
 * Returns how many pixels the user has scrolled to the right (scrollLeft).
 * Works on containers with overflow: auto and window/document.
 *
 * @example $("#testdiv").scrollLeft()
 * @result 100
 *
 * @name scrollLeft
 * @type Number
 * @cat Plugins/Dimensions
 */
jQuery.fn.scrollLeft = function() {
	if ( this[0] == window || this[0] == document )
		return self.pageXOffset ||
			jQuery.boxModel && document.documentElement.scrollLeft ||
			document.body.scrollLeft;

	return this[0].scrollLeft;
};

/**
 * Returns how many pixels the user has scrolled to the bottom (scrollTop).
 * Works on containers with overflow: auto and window/document.
 *
 * @example $("#testdiv").scrollTop()
 * @result 100
 *
 * @name scrollTop
 * @type Number
 * @cat Plugins/Dimensions
 */
jQuery.fn.scrollTop = function() {
	if ( this[0] == window || this[0] == document )
		return self.pageYOffset ||
			jQuery.boxModel && document.documentElement.scrollTop ||
			document.body.scrollTop;

	return this[0].scrollTop;
};

/**
 * Returns the location of the element in pixels from the top left corner of the viewport.
 *
 * For accurate readings make sure to use pixel values for margins, borders and padding.
 *
 * @example $("#testdiv").offset()
 * @result { top: 100, left: 100, scrollTop: 10, scrollLeft: 10 }
 *
 * @example $("#testdiv").offset({ scroll: false })
 * @result { top: 90, left: 90 }
 *
 * @example var offset = {}
 * $("#testdiv").offset({ scroll: false }, offset)
 * @result offset = { top: 90, left: 90 }
 *
 * @name offset
 * @param Object options A hash of options describing what should be included in the final calculations of the offset.
 *                       The options include:
 *                           margin: Should the margin of the element be included in the calculations? True by default.
 *                                   If set to false the margin of the element is subtracted from the total offset.
 *                           border: Should the border of the element be included in the calculations? True by default.
 *                                   If set to false the border of the element is subtracted from the total offset.
 *                           padding: Should the padding of the element be included in the calculations? False by default.
 *                                    If set to true the padding of the element is added to the total offset.
 *                           scroll: Should the scroll offsets of the parent elements be included in the calculations?
 *                                   True by default. When true, it adds the total scroll offsets of all parents to the
 *                                   total offset and also adds two properties to the returned object, scrollTop and
 *                                   scrollLeft. If set to false the scroll offsets of parent elements are ignored.
 *                                   If scroll offsets are not needed, set to false to get a performance boost.
 * @param Object returnObject An object to store the return value in, so as not to break the chain. If passed in the
 *                            chain will not be broken and the result will be assigned to this object.
 * @type Object
 * @cat Plugins/Dimensions
 * @author Brandon Aaron (brandon.aaron@gmail.com || http://brandonaaron.net)
 */
jQuery.fn.offset = function(options, returnObject) {
	var x = 0, y = 0, elem = this[0], parent = this[0], sl = 0, st = 0, options = jQuery.extend({ margin: true, border: true, padding: false, scroll: true }, options || {});
	do {
		x += parent.offsetLeft || 0;
		y += parent.offsetTop  || 0;

		// Mozilla and IE do not add the border
		if (jQuery.browser.mozilla || jQuery.browser.msie) {
			// get borders
			var bt = parseInt(jQuery.css(parent, 'borderTopWidth')) || 0;
			var bl = parseInt(jQuery.css(parent, 'borderLeftWidth')) || 0;

			// add borders to offset
			x += bl;
			y += bt;

			// Mozilla removes the border if the parent has overflow property other than visible
			if (jQuery.browser.mozilla && parent != elem && jQuery.css(parent, 'overflow') != 'visible') {
				x += bl;
				y += bt;
			}
		}

		var op = parent.offsetParent;
		if (op && (op.tagName == 'BODY' || op.tagName == 'HTML')) {
			// Safari doesn't add the body margin for elments positioned with static or relative
			if (jQuery.browser.safari && jQuery.css(parent, 'position') != 'absolute') {
				x += parseInt(jQuery.css(op, 'marginLeft')) || 0;
				y += parseInt(jQuery.css(op, 'marginTop'))  || 0;
			}

			// Exit the loop
			break;
		}

		if (options.scroll) {
			// Need to get scroll offsets in-between offsetParents
			do {
				sl += parent.scrollLeft || 0;
				st += parent.scrollTop  || 0;

				parent = parent.parentNode;

				// Mozilla removes the border if the parent has overflow property other than visible
				if (jQuery.browser.mozilla && parent != elem && parent != op && parent.style && jQuery.css(parent, 'overflow') != 'visible') {
					y += parseInt(jQuery.css(parent, 'borderTopWidth')) || 0;
					x += parseInt(jQuery.css(parent, 'borderLeftWidth')) || 0;
				}
			} while (parent != op);
		} else {
			parent = parent.offsetParent;
		}
	} while (parent);

	if ( !options.margin) {
		x -= parseInt(jQuery.css(elem, 'marginLeft')) || 0;
		y -= parseInt(jQuery.css(elem, 'marginTop'))  || 0;
	}

	// Safari and Opera do not add the border for the element
	if ( options.border && (jQuery.browser.safari || jQuery.browser.opera) ) {
		x += parseInt(jQuery.css(elem, 'borderLeftWidth')) || 0;
		y += parseInt(jQuery.css(elem, 'borderTopWidth'))  || 0;
	} else if ( !options.border && !(jQuery.browser.safari || jQuery.browser.opera) ) {
		x -= parseInt(jQuery.css(elem, 'borderLeftWidth')) || 0;
		y -= parseInt(jQuery.css(elem, 'borderTopWidth'))  || 0;
	}

	if ( options.padding ) {
		x += parseInt(jQuery.css(elem, 'paddingLeft')) || 0;
		y += parseInt(jQuery.css(elem, 'paddingTop'))  || 0;
	}

	// Opera thinks offset is scroll offset for display: inline elements
	if (options.scroll && jQuery.browser.opera && jQuery.css(elem, 'display') == 'inline') {
		sl -= elem.scrollLeft || 0;
		st -= elem.scrollTop  || 0;
	}

	var returnValue = options.scroll ? { top: y - st, left: x - sl, scrollTop:  st, scrollLeft: sl }
									: { top: y, left: x };

	if (returnObject) { jQuery.extend(returnObject, returnValue); return this; }
	else              { return returnValue; }
};



// FORM PLUGIN

/*
 * jQuery form plugin
 * @requires jQuery v1.0.3
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Revision: $Id$
 * Version: 0.9
 */

/**
 * ajaxSubmit() provides a mechanism for submitting an HTML form using AJAX.
 *
 * ajaxSubmit accepts a single argument which can be either a success callback function
 * or an options Object.  If a function is provided it will be invoked upon successful
 * completion of the submit and will be passed the response from the server.
 * If an options Object is provided, the following attributes are supported:
 *
 *  target:   Identifies the element(s) in the page to be updated with the server response.
 *            This value may be specified as a jQuery selection string, a jQuery object,
 *            or a DOM element.
 *            default value: null
 *
 *  url:      URL to which the form data will be submitted.
 *            default value: value of form's 'action' attribute
 *
 *  method:   @deprecated use 'type'
 *  type:     The method in which the form data should be submitted, 'GET' or 'POST'.
 *            default value: value of form's 'method' attribute (or 'GET' if none found)
 *
 *  before:   @deprecated use 'beforeSubmit'
 *  beforeSubmit:  Callback method to be invoked before the form is submitted.
 *            default value: null
 *
 *  after:    @deprecated use 'success'
 *  success:  Callback method to be invoked after the form has been successfully submitted
 *            and the response has been returned from the server
 *            default value: null
 *
 *  dataType: Expected dataType of the response.  One of: null, 'xml', 'script', or 'json'
 *            default value: null
 *
 *  semantic: Boolean flag indicating whether data must be submitted in semantic order (slower).
 *            default value: false
 *
 *  resetForm: Boolean flag indicating whether the form should be reset if the submit is successful
 *
 *  clearForm: Boolean flag indicating whether the form should be cleared if the submit is successful
 *
 *
 * The 'beforeSubmit' callback can be provided as a hook for running pre-submit logic or for
 * validating the form data.  If the 'beforeSubmit' callback returns false then the form will
 * not be submitted. The 'beforeSubmit' callback is invoked with three arguments: the form data
 * in array format, the jQuery object, and the options object passed into ajaxSubmit.
 * The form data array takes the following form:
 *
 *     [ { name: 'username', value: 'jresig' }, { name: 'password', value: 'secret' } ]
 *
 * If a 'success' callback method is provided it is invoked after the response has been returned
 * from the server.  It is passed the responseText or responseXML value (depending on dataType).
 * See jQuery.ajax for further details.
 *
 *
 * The dataType option provides a means for specifying how the server response should be handled.
 * This maps directly to the jQuery.httpData method.  The following values are supported:
 *
 *      'xml':    if dataType == 'xml' the server response is treated as XML and the 'after'
 *                   callback method, if specified, will be passed the responseXML value
 *      'json':   if dataType == 'json' the server response will be evaluted and passed to
 *                   the 'after' callback, if specified
 *      'script': if dataType == 'script' the server response is evaluated in the global context
 *
 *
 * Note that it does not make sense to use both the 'target' and 'dataType' options.  If both
 * are provided the target will be ignored.
 *
 * The semantic argument can be used to force form serialization in semantic order.
 * This is normally true anyway, unless the form contains input elements of type='image'.
 * If your form must be submitted with name/value pairs in semantic order and your form
 * contains an input of type='image" then pass true for this arg, otherwise pass false
 * (or nothing) to avoid the overhead for this logic.
 *
 *
 * When used on its own, ajaxSubmit() is typically bound to a form's submit event like this:
 *
 * $("#form-id").submit(function() {
 *     $(this).ajaxSubmit(options);
 *     return false; // cancel conventional submit
 * });
 *
 * When using ajaxForm(), however, this is done for you.
 *
 * @example
 * $('#myForm').ajaxSubmit(function(data) {
 *     alert('Form submit succeeded! Server returned: ' + data);
 * });
 * @desc Submit form and alert server response
 *
 *
 * @example
 * var options = {
 *     target: '#myTargetDiv'
 * };
 * $('#myForm').ajaxSubmit(options);
 * @desc Submit form and update page element with server response
 *
 *
 * @example
 * var options = {
 *     success: function(responseText) {
 *         alert(responseText);
 *     }
 * };
 * $('#myForm').ajaxSubmit(options);
 * @desc Submit form and alert the server response
 *
 *
 * @example
 * var options = {
 *     beforeSubmit: function(formArray, jqForm) {
 *         if (formArray.length == 0) {
 *             alert('Please enter data.');
 *             return false;
 *         }
 *     }
 * };
 * $('#myForm').ajaxSubmit(options);
 * @desc Pre-submit validation which aborts the submit operation if form data is empty
 *
 *
 * @example
 * var options = {
 *     url: myJsonUrl.php,
 *     dataType: 'json',
 *     success: function(data) {
 *        // 'data' is an object representing the the evaluated json data
 *     }
 * };
 * $('#myForm').ajaxSubmit(options);
 * @desc json data returned and evaluated
 *
 *
 * @example
 * var options = {
 *     url: myXmlUrl.php,
 *     dataType: 'xml',
 *     success: function(responseXML) {
 *        // responseXML is XML document object
 *        var data = $('myElement', responseXML).text();
 *     }
 * };
 * $('#myForm').ajaxSubmit(options);
 * @desc XML data returned from server
 *
 *
 * @example
 * var options = {
 *     resetForm: true
 * };
 * $('#myForm').ajaxSubmit(options);
 * @desc submit form and reset it if successful
 *
 * @example
 * $('#myForm).submit(function() {
 *    $(this).ajaxSubmit();
 *    return false;
 * });
 * @desc Bind form's submit event to use ajaxSubmit
 *
 *
 * @name ajaxSubmit
 * @type jQuery
 * @param options  object literal containing options which control the form submission process
 * @cat Plugins/Form
 * @return jQuery
 * @see formToArray
 * @see ajaxForm
 * @see $.ajax
 * @author jQuery Community
 */
jQuery.fn.ajaxSubmit = function(options) {
    if (typeof options == 'function')
        options = { success: options };

    options = jQuery.extend({
        url:    this.attr('action') || '',
        method: this.attr('method') || 'GET'
    }, options || {});

    // remap deprecated options (temporarily)
    options.success = options.success || options.after;
    options.beforeSubmit = options.beforeSubmit || options.before;
    options.type = options.type || options.method;

    var a = this.formToArray(options.semantic);

    // give pre-submit callback an opportunity to abort the submit
    if (options.beforeSubmit && options.beforeSubmit(a, this, options) === false) return this;

    var q = jQuery.param(a);

    if (options.type.toUpperCase() == 'GET') {
        // if url already has a '?' then append args after '&'
        options.url += (options.url.indexOf('?') >= 0 ? '&' : '?') + q;
        options.data = null;  // data is null for 'get'
    }
    else
        options.data = q; // data is the query string for 'post'

    var $form = this, callbacks = [];
    if (options.resetForm) callbacks.push(function() { $form.resetForm(); });
    if (options.clearForm) callbacks.push(function() { $form.clearForm(); });

    // perform a load on the target only if dataType is not provided
    if (!options.dataType && options.target) {
        var oldSuccess = options.success || function(){};
        callbacks.push(function(data, status) {
            jQuery(options.target).attr("innerHTML", data).evalScripts().each(oldSuccess, [data, status]);
        });
    }
    else if (options.success)
        callbacks.push(options.success);

    options.success = function(data, status) {
        for (var i=0, max=callbacks.length; i < max; i++)
            callbacks[i](data, status);
    };

    jQuery.ajax(options);
    return this;
};

/**
 * ajaxForm() provides a mechanism for fully automating form submission.
 *
 * The advantages of using this method instead of ajaxSubmit() are:
 *
 * 1: This method will include coordinates for <input type="image" /> elements (if the element
 *    is used to submit the form).
 * 2. This method will include the submit element's name/value data (for the element that was
 *    used to submit the form).
 * 3. This method binds the submit() method to the form for you.
 *
 * Note that for accurate x/y coordinates of image submit elements in all browsers
 * you need to also use the "dimensions" plugin (this method will auto-detect its presence).
 *
 * The options argument for ajaxForm works exactly as it does for ajaxSubmit.  ajaxForm merely
 * passes the options argument along after properly binding events for submit elements and
 * the form itself.  See ajaxSubmit for a full description of the options argument.
 *
 *
 * @example
 * var options = {
 *     target: '#myTargetDiv'
 * };
 * $('#myForm').ajaxSForm(options);
 * @desc Bind form's submit event so that 'myTargetDiv' is updated with the server response
 *       when the form is submitted.
 *
 *
 * @example
 * var options = {
 *     success: function(responseText) {
 *         alert(responseText);
 *     }
 * };
 * $('#myForm').ajaxSubmit(options);
 * @desc Bind form's submit event so that server response is alerted after the form is submitted.
 *
 *
 * @example
 * var options = {
 *     beforeSubmit: function(formArray, jqForm) {
 *         if (formArray.length == 0) {
 *             alert('Please enter data.');
 *             return false;
 *         }
 *     }
 * };
 * $('#myForm').ajaxSubmit(options);
 * @desc Bind form's submit event so that pre-submit callback is invoked before the form
 *       is submitted.
 *
 *
 * @name   ajaxForm
 * @param  options  object literal containing options which control the form submission process
 * @return jQuery
 * @cat    Plugins/Form
 * @type   jQuery
 * @see    ajaxSubmit
 * @author jQuery Community
 */
jQuery.fn.ajaxForm = function(options) {
    return this.each(function() {
        jQuery("input:submit,input:image,button:submit", this).click(function(ev) {
            var $form = this.form;
            $form.clk = this;
            if (this.type == 'image') {
                if (ev.offsetX != undefined) {
                    $form.clk_x = ev.offsetX;
                    $form.clk_y = ev.offsetY;
                } else if (typeof jQuery.fn.offset == 'function') { // try to use dimensions plugin
                    var offset = jQuery(this).offset();
                    $form.clk_x = ev.pageX - offset.left;
                    $form.clk_y = ev.pageY - offset.top;
                } else {
                    $form.clk_x = ev.pageX - this.offsetLeft;
                    $form.clk_y = ev.pageY - this.offsetTop;
                }
            }
            // clear form vars
            setTimeout(function() {
                $form.clk = $form.clk_x = $form.clk_y = null;
                }, 10);
        })
    }).submit(function(e) {
        jQuery(this).ajaxSubmit(options);
        return false;
    });
};


/**
 * formToArray() gathers form element data into an array of objects that can
 * be passed to any of the following ajax functions: $.get, $.post, or load.
 * Each object in the array has both a 'name' and 'value' property.  An example of
 * an array for a simple login form might be:
 *
 * [ { name: 'username', value: 'jresig' }, { name: 'password', value: 'secret' } ]
 *
 * It is this array that is passed to pre-submit callback functions provided to the
 * ajaxSubmit() and ajaxForm() methods.
 *
 * The semantic argument can be used to force form serialization in semantic order.
 * This is normally true anyway, unless the form contains input elements of type='image'.
 * If your form must be submitted with name/value pairs in semantic order and your form
 * contains an input of type='image" then pass true for this arg, otherwise pass false
 * (or nothing) to avoid the overhead for this logic.
 *
 * @example var data = $("#myForm").formToArray();
 * $.post( "myscript.cgi", data );
 * @desc Collect all the data from a form and submit it to the server.
 *
 * @name formToArray
 * @param semantic true if serialization must maintain strict semantic ordering of elements (slower)
 * @type Array<Object>
 * @cat Plugins/Form
 * @see ajaxForm
 * @see ajaxSubmit
 * @author jQuery Community
 */
jQuery.fn.formToArray = function(semantic) {
    var a = [];
    if (this.length == 0) return a;

    var form = this[0];
    var els = semantic ? form.getElementsByTagName('*') : form.elements;
    if (!els) return a;
    for(var i=0, max=els.length; i < max; i++) {
        var el = els[i];
        var n = el.name;
        if (!n) continue;

        if (semantic && form.clk && el.type == "image") {
            // handle image inputs on the fly when semantic == true
            if(!el.disabled && form.clk == el)
                a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
            continue;
        }
        var v = jQuery.fieldValue(el, true);
        if (v === null) continue;
        if (v.constructor == Array) {
            for(var j=0, jmax=v.length; j < jmax; j++)
                a.push({name: n, value: v[j]});
        }
        else
            a.push({name: n, value: v});
    }

    if (!semantic && form.clk) {
        // input type=='image' are not found in elements array! handle them here
        var inputs = form.getElementsByTagName("input");
        for(var i=0, max=inputs.length; i < max; i++) {
            var input = inputs[i];
            var n = input.name;
            if(n && !input.disabled && input.type == "image" && form.clk == input)
                a.push({name: n+'.x', value: form.clk_x}, {name: n+'.y', value: form.clk_y});
        }
    }
    return a;
};


/**
 * Serializes form data into a 'submittable' string. This method will return a string
 * in the format: name1=value1&amp;name2=value2
 *
 * The semantic argument can be used to force form serialization in semantic order.
 * If your form must be submitted with name/value pairs in semantic order then pass
 * true for this arg, otherwise pass false (or nothing) to avoid the overhead for
 * this logic (which can be significant for very large forms).
 *
 * @example var data = $("#myForm").formSerialize();
 * $.ajax('POST', "myscript.cgi", data);
 * @desc Collect all the data from a form into a single string
 *
 * @name formSerialize
 * @param semantic true if serialization must maintain strict semantic ordering of elements (slower)
 * @type String
 * @cat Plugins/Form
 * @see formToArray
 * @author jQuery Community
 */
jQuery.fn.formSerialize = function(semantic) {
    //hand off to jQuery.param for proper encoding
    return jQuery.param(this.formToArray(semantic));
};


/**
 * Serializes all field elements in the jQuery object into a query string.
 * This method will return a string in the format: name1=value1&amp;name2=value2
 *
 * The successful argument controls whether or not serialization is limited to
 * 'successful' controls (per http://www.w3.org/TR/html4/interact/forms.html#successful-controls).
 * The default value of the successful argument is true.
 *
 * @example var data = $("input").formSerialize();
 * @desc Collect the data from all successful input elements into a query string
 *
 * @example var data = $(":radio").formSerialize();
 * @desc Collect the data from all successful radio input elements into a query string
 *
 * @example var data = $("#myForm :checkbox").formSerialize();
 * @desc Collect the data from all successful checkbox input elements in myForm into a query string
 *
 * @example var data = $("#myForm :checkbox").formSerialize(false);
 * @desc Collect the data from all checkbox elements in myForm (even the unchecked ones) into a query string
 *
 * @example var data = $(":input").formSerialize();
 * @desc Collect the data from all successful input, select, textarea and button elements into a query string
 *
 * @name fieldSerialize
 * @param successful true if only successful controls should be serialized (default is true)
 * @type String
 * @cat Plugins/Form
 */
jQuery.fn.fieldSerialize = function(successful) {
    var a = [];
    this.each(function() {
        var n = this.name;
        if (!n) return;
        var v = jQuery.fieldValue(this, successful);
        if (v && v.constructor == Array) {
            for (var i=0,max=v.length; i < max; i++)
                a.push({name: n, value: v[i]});
        }
        else if (v !== null && typeof v != 'undefined')
            a.push({name: this.name, value: v});
    });
    //hand off to jQuery.param for proper encoding
    return jQuery.param(a);
};


/**
 * Returns the value of the field element in the jQuery object.  If there is more than one field element
 * in the jQuery object the value of the first successful one is returned.
 *
 * The successful argument controls whether or not the field element must be 'successful'
 * (per http://www.w3.org/TR/html4/interact/forms.html#successful-controls).
 * The default value of the successful argument is true.  If this value is false then
 * the value of the first field element in the jQuery object is returned.
 *
 * Note: If no valid value can be determined the return value will be undifined.
 *
 * Note: The fieldValue returned for a select-multiple element or for a checkbox input will
 *       always be an array if it is not undefined.
 *
 *
 * @example var data = $("#myPasswordElement").formValue();
 * @desc Gets the current value of the myPasswordElement element
 *
 * @example var data = $("#myForm :input").formValue();
 * @desc Get the value of the first successful control in the jQuery object.
 *
 * @example var data = $("#myForm :checkbox").formValue();
 * @desc Get the array of values for the first set of successful checkbox controls in the jQuery object.
 *
 * @example var data = $("#mySingleSelect").formValue();
 * @desc Get the value of the select control
 *
 * @example var data = $("#myMultiSelect").formValue();
 * @desc Get the array of selected values for the select-multiple control
 *
 * @name fieldValue
 * @param Boolean successful true if value returned must be for a successful controls (default is true)
 * @type String or Array<String>
 * @cat Plugins/Form
 */
jQuery.fn.fieldValue = function(successful) {
    var cbVal, cbName;

    // loop until we find a value
    for (var i=0, max=this.length; i < max; i++) {
        var el = this[i];
        var v = jQuery.fieldValue(el, successful);
        if (v === null || typeof v == 'undefined' || (v.constructor == Array && !v.length))
            continue;

        // for checkboxes, consider multiple elements, for everything else just return first valid value
        if (el.type != 'checkbox') return v;

        cbName = cbName || el.name;
        if (cbName != el.name) // return if we hit a checkbox with a different name
            return cbVal;
        cbVal = cbVal || [];
        cbVal.push(v);
    }
    return cbVal;
};

/**
 * Returns the value of the field element.
 *
 * The successful argument controls whether or not the field element must be 'successful'
 * (per http://www.w3.org/TR/html4/interact/forms.html#successful-controls).
 * The default value of the successful argument is true.  If the given element is not
 * successful and the successful arg is not false then the returned value will be null.
 *
 * Note: The fieldValue returned for a select-multiple element will always be an array.
 *
 * @example var data = jQuery.fieldValue($("#myPasswordElement")[0]);
 * @desc Gets the current value of the myPasswordElement element
 *
 * @name fieldValue
 * @param Element el The DOM element for which the value will be returned
 * @param Boolean successful true if value returned must be for a successful controls (default is true)
 * @type String or Array<String>
 * @cat Plugins/Form
 */
jQuery.fieldValue = function(el, successful) {
    var n = el.name, t = el.type, tag = el.tagName.toLowerCase();
    if (typeof successful == 'undefined') successful = true;

    if (successful && ( !n || el.disabled || t == 'reset' ||
        (t == 'checkbox' || t == 'radio') && !el.checked ||
        (t == 'submit' || t == 'image') && el.form && el.form.clk != el ||
        tag == 'select' && el.selectedIndex == -1))
            return null;

    if (tag == 'select') {
        var index = el.selectedIndex;
        if (index < 0) return null;
        var a = [], ops = el.options;
        var one = (t == 'select-one');
        var max = (one ? index+1 : ops.length);
        for(var i=(one ? index : 0); i < max; i++) {
            var op = ops[i];
            if (op.selected) {
                // extra pain for IE...
                var v = jQuery.browser.msie && !(op.attributes['value'].specified) ? op.text : op.value;
                if (one) return v;
                a.push(v);
            }
        }
        return a;
    }
    return el.value;
};


/**
 * Clears the form data.  Takes the following actions on the form's input fields:
 *  - input text fields will have their 'value' property set to the empty string
 *  - select elements will have their 'selectedIndex' property set to -1
 *  - checkbox and radio inputs will have their 'checked' property set to false
 *  - inputs of type submit, button, reset, and hidden will *not* be effected
 *  - button elements will *not* be effected
 *
 * @example $('form').clearForm();
 * @desc Clears all forms on the page.
 *
 * @name clearForm
 * @type jQuery
 * @cat Plugins/Form
 * @see resetForm
 */
jQuery.fn.clearForm = function() {
    return this.each(function() {
        jQuery('input,select,textarea', this).clearFields();
    });
};

/**
 * Clears the selected form elements.  Takes the following actions on the matched elements:
 *  - input text fields will have their 'value' property set to the empty string
 *  - select elements will have their 'selectedIndex' property set to -1
 *  - checkbox and radio inputs will have their 'checked' property set to false
 *  - inputs of type submit, button, reset, and hidden will *not* be effected
 *  - button elements will *not* be effected
 *
 * @example $('.myInputs').clearFields();
 * @desc Clears all inputs with class myInputs
 *
 * @name clearFields
 * @type jQuery
 * @cat Plugins/Form
 * @see clearForm
 */
jQuery.fn.clearFields = jQuery.fn.clearInputs = function() {
    return this.each(function() {
        var t = this.type, tag = this.tagName.toLowerCase();
        if (t == 'text' || t == 'password' || tag == 'textarea')
            this.value = '';
        else if (t == 'checkbox' || t == 'radio')
            this.checked = false;
        else if (tag == 'select')
            this.selectedIndex = -1;
    });
};


/**
 * Resets the form data.  Causes all form elements to be reset to their original value.
 *
 * @example $('form').resetForm();
 * @desc Resets all forms on the page.
 *
 * @name resetForm
 * @type jQuery
 * @cat Plugins/Form
 * @see clearForm
 */
jQuery.fn.resetForm = function() {
    return this.each(function() {
        // guard against an input with the name of 'reset'
        // note that IE reports the reset function as an 'object'
        if (typeof this.reset == 'function' || (typeof this.reset == 'object' && !this.reset.nodeType))
            this.reset();
    });
};
