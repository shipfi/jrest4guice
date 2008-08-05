$(document).ready (function () {
	if (window.CMS && window.CMS.ctx && window.CMS.ctx.async) {
		window.CMS.ctx.initList.push(init);
	} else {
		init();
	}
});

function init () {
	$('#bookmarklink').click(function () {		
		addBookmark(document.title, window.location.href);
		return false;
	});
	$('#homelink').click(function () {
		setHomePage(this, window.location.href);
		return false;
	});
}