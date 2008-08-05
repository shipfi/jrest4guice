jQuery.extend(String.prototype, {
	capitalize : function () {
		return this.charAt(0).toUpperCase() + this.substring(1);
	},
	trim : function () {
		return this.replace(/^\s+|\s+$/g, '');
	},
	isInt : function () {
		return /^[\-+]?(0|([1-9]\d*))$/.test(this);
	},
	isFloat : function () {
		return /^[\-+]?(0|([1-9]\d*))(\.\d*(\dE[\-+]?\d+)?)?$/.test(this);
	},
	isNumber : function () {
		return /^\d+$/.test(this);
	},
	isEmail : function () {
		return (/\b(^[_A-Za-z0-9-]+(\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\.[A-Za-z0-9-]+)*((\.[A-Za-z0-9]{2,})|(\.[A-Za-z0-9]{2,}\.[A-Za-z0-9]{2,}))$)\b/gi).test(this);
	},
	isUrl : function () {
		return (/(^(ftp|http|https):\/\/(\.[_A-Za-z0-9-]+)*(@?([A-Za-z0-9-])+)?(\.[A-Za-z0-9-]+)*((\.[A-Za-z0-9]{2,})|(\.[A-Za-z0-9]{2,}\.[A-Za-z0-9]{2,}))(:[0-9]+)?([\/A-Za-z0-9?#_-]*)?$)/gi).test(this);
	}
});

jQuery.extend(Array.prototype, {
	indexOf : function(item, from) {
		var len = this.length;
		for (var i = (from < 0) ? Math.max(0, len + from) : from || 0; i < len; i++){
			if (this[i] === item) return i;
		}
		return -1;
	},
	every : function(fn, bind){
		for (var i = 0, j = this.length; i < j; i++){
			if (!fn.call(bind, this[i], i, this)) return false;
		}
		return true;
	},
	some : function(fn, bind){
		for (var i = 0, j = this.length; i < j; i++){
			if (fn.call(bind, this[i], i, this)) return true;
		}
		return false;
	}
});
	
function setupUploader (o) {
	var options = jQuery.extend({
	    button : 'upload',
	    file : 'file',
		onUploadStart : function () {},
		onUploadCompleted : function (data) {}
	}, o);
	
	$($ID(options.button)).click(function () {
		var file = $($ID(options.file))[0];
		if (file && file.value) {
			var iframeID = 'secretIframe';
			var iframe = $($ID(iframeID));
			if (iframe.length == 0) {
				$('body').append('<iframe src="javascript:void(0)" frameborder="0" width="0" height="0" id="' + iframeID + '" name="' + iframeID + '"></iframe>');
				iframe = $($ID(iframeID));
				iframe.css({'width':0,'height':0,'border':0});
			}
			iframe.load(function () {
				if ($('body',$(this).contents()).html().indexOf('j_acegi_security_check') != -1) {
					alert(CMS.ctx.lang['tip.timeout']);
					window.top.location = CMS.ctx.base + '/j_acegi_logout';
				}
				$(this).unbind('load');
			});
			iframe[0].onUploadCompleted = options.onUploadCompleted;
			options.onUploadStart();
			file.form.target = iframeID;
			file.form.submit();
		}
	});
}

function AjaxError (max) {
	max = max || 1;
	this.size = 0;
	this.clear = function () {
		this.size = 0;
	};
	
	this.getHandler = function (fn) {
		var err = this;
		return function (xml, status, e) {
			if (err.size < max) {
				fn(xml, status, e);
			}
			err.size++;
		}
	};
}

var allScripts = {};

(function () {
	jQuery('script').each(function (i, script) {
		var src = script.getAttribute("src");
		if (src) {
			allScripts[src.substr(src.lastIndexOf('/') + 1)] = true;
		}		
	});
})();

function addBookmark (title, url) {
	try {
		if (window.sidebar) { 
			window.sidebar.addPanel(title, url, ""); 
		} else if (document.all) {
			window.external.AddFavorite(url, title);
		}
	} catch(e) {
		alert(e.message);
	}
}

function afterClick (el, args) {
	var after = fnByName(el.name, 'after');
	if ($.isFunction(after)) {
		after.apply(el, args);
		return true;
	}
	return false;
}

function beforeClick (el) {
	var validate = fnByName(el.form.name, 'validate');
	if (!$.isFunction(validate) || validate.apply(el.form)) {
		var before = fnByName(el.name, 'before');
		return !$.isFunction(before) || before.apply(el);
	}
	return false;
}
 
function fnByName (name, prefix) {
	if (!name) {
		name = 'submit';
	} else if (name.indexOf('method:') == 0) {
		name = name.substr(7);
	}
	return window[prefix + name.capitalize()];
}

function getJSON (url) {
	var result = {};
	$.ajax({
		type : 'get',
		url : url,
		async: false,
		success : function (data, status) {result = data;},
		dataType : 'json'
	});
	return result;
}
	
function getMethod (url) {
	url = '/' + url;
	var method = '';
	var re = /^.*?\/\w+-(\w+)-\w+(\/\d+)?\.(json|html).*$/;
	if (re.test(url)) {
		method = url.replace(re, '$1');
	} else {
		re = /^.*?\/(\w+)(-\w+)?(\/\d+)?\.(json|html).*$/;
		if (re.test(url)) {
			method = url.replace(re, '$1');
		}			
	}
	
	if (CMS.ctx.rewrites[method]) {
		return CMS.ctx.rewrites[method];
	}
	
	var relativeURL = url.replace(/^.*?\/pages\/(\w+\/[^\/]+)(\/\d+)?\.(json|html).*$/, '$1');
	if (CMS.ctx.rewrites[relativeURL]) {
		return CMS.ctx.rewrites[relativeURL];
	}
	
	return method;
}
		
function getPager (pager) {
	var totalRows = parseInt((pager && pager.totalRows) || jQuery('#totalRows').val() || 0);
	var limit = parseInt((pager && pager.limit) || jQuery('#limit').val() || 10);
	var offset = parseInt((pager && pager.offset) || jQuery('#offset').val() || 1);
	var totalPage = Math.floor(totalRows / limit) + 1;
	var currentPage = (totalRows == 0) ? 0 : Math.floor(offset / limit) + 1;
	return {
		totalRows : totalRows,
		limit : limit,
		offset : offset,
		totalPage : totalPage,
		currentPage : currentPage,
		hasPrev : (totalRows > 0 && offset > 1),
		hasNext : (totalRows > 0 && offset + limit - 1 < totalRows),
		isLast : (currentPage == totalPage)
	}
}

function getPagerHTML (pager) {
	var html = '<ul>';
	pager = getPager(pager);
	if (pager.hasPrev) {
		html += '<li><a href="javascript:goFirstPage();">' + CMS.ctx.lang['pager.first'] + '</a></li>';
		html += '<li><a href="javascript:goPrevPage();">' + CMS.ctx.lang['pager.prev'] + '</a></li>';
	}
	if (pager.hasNext) {
		html += '<li><a href="javascript:goNextPage();">' + CMS.ctx.lang['pager.next'] + '</a></li>';
		html += '<li><a href="javascript:goLastPage();">' + CMS.ctx.lang['pager.last'] + '</a></li>';
	}
	html += '</ul>';
	return html;
}

function getServerUrl (url, ext) {
	var serverUrl;
	var re = /\/(pages\/\w+\/[^\/]+(\/\d+)?)\.html/;
	if (re.test(url)) {
		serverUrl = url.replace(re, '/server/$1.' + $V(ext, 'json'));
	} else {
		serverUrl = url;
	}

	return serverUrl;
}

function goFirstPage () {
	jQuery('#offset').val(1);
	jQuery('.cms-list-form').submit();
}

function goLastPage () {
	var pager = getPager();
	var r = pager.totalRows % pager.limit;
	if (r == 0) {
		r = pager.limit;
	}
	jQuery('#offset').val(pager.totalRows - r + 1);
	jQuery('.cms-list-form').submit();
}

function goNextPage () {
	var pager = getPager();
	jQuery('#offset').val(pager.offset + pager.limit);
	jQuery('.cms-list-form').submit();
}

function goPage (pageNo) {
	var pager = getPager();
	jQuery('#offset').val((pageNo - 1) * pager.limit + 1);
	jQuery('.cms-list-form').submit();
}

function goPrevPage () {
	var pager = getPager();
	jQuery('#offset').val(pager.offset - pager.limit);
	jQuery('.cms-list-form').submit();
}

function handleResult (result, status, message) {
	if (CMS.ctx.msgtip == 'script') {
		var errors = (result.fieldErrors && jQuery.makeArray(result.fieldErrors).join('\n')) ||
					 (result.actionErrors && jQuery.makeArray(result.actionErrors).join('\n')) || '';
		if (errors != '') {
			alert(errors);
			return false;
		} else {
			var messages = message || (result.actionMessages && jQuery.makeArray(result.actionMessages).join('\n')) || '';
			if (messages != '') {
				alert(messages);
			}
			return true;
		}						
	}
}

function loadCalendar () {
	var scripts = [];
	if (!allScripts['calendar.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jscalendar/calendar.js');
	}
	if (!allScripts['calendar-cn-utf8.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jscalendar/lang/calendar-cn-utf8.js');
	}
	if (!allScripts['calendar-setup.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jscalendar/calendar-setup.js');
	}
	loadScripts2(scripts);
}

function loadContext (root) {
	loadScripts2([(root?'':'../../') + 'server/scripts/context.js']);
	loadScripts2([
		CMS.ctx.base + '/server/scripts/lang.js',
		CMS.ctx.base + '/server/scripts/dictionary.js',
		CMS.ctx.base + '/server/scripts/constants.js',
		CMS.ctx.base + '/scripts/template.js?v=1.0.38'
	]);
}

function loadConstants () {
	var constants = {};
	
	var data = getJSON(CMS.ctx.base + '/server/public/allconst.json');
	jQuery.each(data.allconst, function (i, constant) {
		constants[constant.name] = (constant.value || 0);
	});
	
	return constants;
}

function loadDialog () {
	var scripts = [];
	if (!allScripts['jquery.dimensions.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jquery.ui/jquery.dimensions.js');
	}
	if (!allScripts['ui.dialog.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jquery.ui/ui.dialog.js');
	}
	if (!allScripts['ui.mouse.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jquery.ui/ui.mouse.js');
	}
	if (!allScripts['ui.resizable.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jquery.ui/ui.resizable.js');
	}
	if (!allScripts['ui.draggable.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jquery.ui/ui.draggable.js');
	}
	loadScripts2(scripts);
}

function loadDicts () {
	var dicts = {};
	
	var data = getJSON(CMS.ctx.base + '/server/public/alldict.json');
	jQuery.each(data.alldict, function (i, dict){
		if (dict.name) {
			if (dict.subdicts && dict.subdicts.length > 0) {
				dicts[dict.name] = dict.subdicts.slice(0);
			} else {
				dicts[dict.name] = [];
			}
		}
	});
	jQuery.each(dicts, function (name, dict) {
		if (dict.length > 0) {
			jQuery.each(dict, function (i, subdict){
				subdict.asscdicts = [];
				if (subdict.asscNames) {
					jQuery.each(subdict.asscNames.split(','), function (j, asscName) {
						subdict.asscdicts.push(dicts[asscName] || {});
					});
				}
			});
		}
	});
	
	return dicts;
}

function loadNav (hasTree) {
	var scripts = [];
	if (!allScripts['jquery.dimensions.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jquery.ui/jquery.dimensions.js');
	}
	if (!allScripts['ui.accordion.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jquery.ui/ui.accordion.js');
	}	
	if (hasTree && !allScripts['tree.js']) {
		scripts.push(CMS.ctx.base + '/scripts/tree.js');
	}
	loadScripts2(scripts);
	
	var data = getJSON(CMS.ctx.base + '/server/public/navMenus.json');
	sortByOrder(data.navMenus);
	return data;
}

function loadTabs () {
	var scripts = [];
	if (!allScripts['ui.core.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jquery.ui/ui.core.js');
	}
	if (!allScripts['ui.tabs.js']) {
		scripts.push(CMS.ctx.base + '/scripts/jquery.ui/ui.tabs.js');
	}
	loadScripts2(scripts);
}

function loadScripts (scripts, callback) {
	var n = 0;
	jQuery(scripts).each(function (i, script) {
		jQuery.getScript(script, function () {
			n++;
			allScripts[script.substr(script.lastIndexOf('/') + 1)] = true;
			if (n == scripts.length) {
				if (jQuery.isFunction(callback)) {
					callback();
				}
			}
		});
	});
}

function loadScripts2 (scripts) {
	jQuery(scripts).each(function (i, script) {
		jQuery.ajax({ url: script, async: false, dataType: "script" });
		allScripts[script.substr(script.lastIndexOf('/') + 1)] = true;
	});
}

function mergeData (data) {
	data.params = data.params || {};
	if (document.location && document.location.search) {
		jQuery.each(document.location.search.substr(1).split('&'), function (i, param) {
			var pair = param.split('=');
			var dotPos = pair[0].indexOf('.');
			if (dotPos != -1) {
				var prefix = pair[0].substr(0, dotPos);
				if (!data[prefix]) {
					data[prefix] = {};
				}
				data[prefix][decodeURIComponent(pair[0].substr(dotPos + 1))] = decodeURIComponent(pair[1]);
			} else {
				data.params[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1]);
			}
		});
	}
}

function parseJST (data) {
	mergeData(data);
	if (jQuery('#main_jst')) {
		if (jQuery.isFunction(window.beforeParseJST)) {
			window.beforeParseJST(data);
		}
		jQuery('#main').html(TrimPath.processDOMTemplate('main_jst',data).toString());
		if (jQuery.isFunction(window.afterParseJST)) {
			window.afterParseJST(data);
		}
	}	
	if (jQuery.isFunction(window.initialize)) {
		window.initialize(data);
	}
}

function setHomePage(link, url) {
	try {
		link.style.behavior = 'url(#default#homepage)';
		link.setHomePage(url);
	} catch(e) {
		alert(e.message);
	}
}

function setList (select, options, selected) {
	var s = '';
	jQuery.each(options, function (i, option) {
		s += '<option value="' + $V(option.value, '') + '"' + ((selected == i)?' selected="selected"':'') + '>' + $V(option.text, '') + '</option>';
	});
	jQuery(select).html(s);
}

function setupCalendar (o) {
	var options = jQuery.extend({
	    ifFormat : '%Y-%m-%d',
	    showsTime : false,
	    timeFormat : "24",
	    align : "Bl",
	    tip : CMS.ctx.lang['tip.date.selector']
	}, o);
	if (!options.button) {
		options.button = options.inputField + '_trigger';
	}
	if (jQuery($ID(options.button)).length == 0) {
		jQuery($ID(options.inputField)).after('<img src="' + CMS.ctx.base + '/styles/default/images/calendar.gif"' +
			' id="' + options.button + '"' +
			' title="' + options.tip + '"' +
			' alt="' + options.tip + '"' +
			' style="margin-left:5px;vertical-align:absbottom;cursor:pointer;" />');	
	}
	loadCalendar();
	Calendar.setup(options);
}

function sortByOrder (data, parentProperty, orderProperty) {
	parentProperty = parentProperty || 'parentId';
	orderProperty = orderProperty || 'displayOrder';
	if (!data || !data.length || data.length <= 0 || !data[0].id || !data[0][orderProperty]) {
		return;
	}
	var map = {};
	for (var i = 0; i < data.length; i++) {
		var id = data[i].id;
		map['_' + id] = i;
	}
	for (var i = 0; i < data.length; i++) {
		var parent = data[i][parentProperty];
		if (parent) {
			data[i]['_order'] = data[map['_' + parent]]['_order'] + '_' + data[i][orderProperty];
		} else {
			data[i]['_order'] = data[i][orderProperty] + '';
		}
	}
	data.sort(function (datai, dataj) {
		if (datai['_order'] < dataj['_order']) {
			return -1;
		}
		if (datai['_order'] > dataj['_order']) {
			return 1;
		}
		return 0;
	});
}

function $chk(obj){
	return !!(obj || obj === 0);
};

function $ID (id) {
	if (id.charAt(0) == '#') {
		return id;
	}
	return '#' + id;
}

function $R (start, end, step) {
	var r = [];
	if (!end) {
		end = start;
		start = 0;		
	}
	step = step || 1;
	if (start < end) {
		for (var i = start; i < end; i += step) {
			r.push(i);
		}
	} else if (start > end) {
		for (var i = start; i > end; i -= step) {
			r.push(i);
		}
	}
	return r;
}

function $V (v1, v2) {
	return $chk(v1) ? v1 : v2;
}