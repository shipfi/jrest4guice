function doLogout(){
	if(!window.confirm("您确定要注销当前的登录状态吗?"))
		return;
	window.location.href = "logout.jsp"
}

function doLogin(){
	$.blockUI({message: $('#securityDiv'), css: { width: '280px',height:'226px'}}); 
	$("#securityIframe").attr("src","login.jsp");
}

function closeLoginWindow(){
	$.unblockUI();
}
