if (!window.CMS) window.CMS = {};
if (!window.CMS.ctx) window.CMS.ctx = {};

CMS.ctx.initList = [];
CMS.ctx.base = document.location.href.replace(/^(.*?)\/pages\/\w+\/.+$/, '$1')
									 .replace(/^(.*?)\/[^\/]+\.html.*$/, '$1')
									 .replace(/^http:\/\/[^\/]+/, '');
CMS.ctx.msgtip = 'script';
CMS.ctx.rewrites = {
	'new' : 'editNew',
	'print' : 'show'
};
CMS.ctx.loginUser = {name:'administrator'};