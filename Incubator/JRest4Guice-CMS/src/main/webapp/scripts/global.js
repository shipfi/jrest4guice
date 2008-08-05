(function ($) {

	$(document).ready (function () {
		
		loadContext();
		
		if ($.isFunction(window.prepare)) {
			var data = {};
			if (!window.prepare(data)) {
				showData(data);
				return;
			}
		}
		
		CMS.ctx.async = true;
		
		var serverUrl = getServerUrl(window.location.href);
		$.ajax({
			type : 'get',
			url : serverUrl,
			success : function (data, status) {
				showData(data || {});
			},
			error : (new AjaxError()).getHandler(function (xml, status, e) {
				showData({});
			}),
			data : 'method:' + getMethod(serverUrl) + '=',
			dataType : 'json'
		});
	});

	function showData (data) {
	
		parseJST(data);
		
		if (!CMS.ctx.hasContainer) {
			$('form').each(function () {
				this.action = rewriteURL(this.action);					
			});
		}
		
		$('.cms-list-form').each(function () {
			this.method = 'get';					
		});
		
		$('.cms-list-table tbody tr:odd').addClass('odd');
		$('.cms-list-table tbody tr').mouseover(function () {
			$(this).addClass('over');
		}).mouseout(function () {
			$(this).removeClass("over");
		});
	
		$('#chkAll').click(function () {
			$('input[@name=model.chkIDs]').attr('checked', this.checked);
		});
		
		$('a.cms-ajax').each(function () {
			if (/delete\.html/.test(this.href)) {
				$(this).click(function () {
					try {
						if (confirm(CMS.ctx.lang['tip.del'])) {
							if(!$('input[@name=model.chkIDs]').is(':checked')) {
								alert(CMS.ctx.lang['tip.chk']);
							} else {
								if ($('input[@name=model.chkIDs]').length == $('input[@name=model.chkIDs]:checked').length) {
									var pager = getPager();
									if (pager.isLast) {
										$('#offset').val(Math.max(1, pager.offset - pager.limit));
									}
								}
								remove(this.href);
							}
						}
					} catch (e) {
						alert(e.message);
					}
					return false;
				});
			}
			if (/destroy\/\d+\.html/.test(this.href)) {
				$(this).click(function () {
					try {
						if (confirm(CMS.ctx.lang['tip.del'])) {
							if ($('input[@name=model.chkIDs]').length == 1) {
								var pager = getPager();
								if (pager.isLast) {
									$('#offset').val(Math.max(1, pager.offset - pager.limit));
								}
							}
							remove(this.href);
						}
					} catch (e) {
						alert(e.message);
					}
					return false;
				});
			}
			if (!CMS.ctx.hasContainer) {
				this.href = rewriteURL(this.href);
			}
		});
		
		$('input[@type=submit].cms-ajax').click(function () {
			try {
				if (beforeClick(this)) {
					submit(this);
				}
			} catch (e) {
				alert(e.message);
			}
			return false;
		});
		
		$('input[@type=button].cms-back').click(function () {
			history.back();
		});
		
		if (CMS.ctx.initList) {
			$.each(CMS.ctx.initList, function(){
				this();
			});
			CMS.ctx.initList = null;
		}
	}

	function remove (url) {
		var serverUrl = getServerUrl(url);
		request(
			serverUrl,
			'method:' + getMethod(serverUrl),
			$('form.cms-list-form').serialize(),
			'',
			function () {
				$('form.cms-list-form').submit();
			}
		);
	}
	
	function submit (el) {	
		var serverUrl = getServerUrl(el.form.action);
		var method;
		if (el.name.indexOf('method:') == 0) {
			method = el.name.substr(7);
		} else {
			method = getMethod(serverUrl);
		}
		request(
			serverUrl,
			'method:' + method,
			$(el.form).serialize(),
			$(el).attr('message'),
			{
				create : function (result) {				
					if(!afterClick(el, [result])) {
						el.form.reset();
					}
				},
				update : function (result) {
					afterClick(el, [result]);
				}
			}[method]
		);
	}
	
	function request (serverUrl, method, data, message, afterCallback) {
		$.ajax({
			type: 'post',
			url: serverUrl,
			success: function (result, status) {
				if (handleResult(result, status, message) && afterCallback) {
					afterCallback(result);
				}
			},
			error: (new AjaxError()).getHandler(function (xml, status, e) {
				if (xml && xml.responseText.indexOf('j_acegi_security_check') != -1) {
					alert(CMS.ctx.lang['tip.timeout']);
					window.top.location = CMS.ctx.base + '/j_acegi_logout';
				} else {
					alert(CMS.ctx.lang['tip.ajax.fail']);
				}
			}),
			data: method + '=&' + data,
			dataType: 'json'
		});
	}

	function rewriteURL (url) {
		return url.replace(/(\/(\d+)\.html)/, '.html?id=$2');
	}

})(jQuery);