<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>联系人管理(CTL template)</title>
<link href="/full/css/default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="/full/javascript/lib/jquery-1.2.5.js"></script>
	<script type="text/javascript" src="/full/javascript/lib/jquery-ui-1.5.js"></script>
	<script type="text/javascript" src="/full/javascript/lib/jquery.blockUI.js"></script>
	<script type="text/javascript" src="/full/javascript/lib/user-ext.js"></script>
	<script type="text/javascript" src="/full/security.js"></script>
	<script>
		window.onload = function(){
			IFrameUtil.subscribeEvent("onLogin",window,function(param){
				window.location = window.location; 
			});
		}
	</script>


</head>
<body>
	<br>
	<div style="position: absolute;top:4px;right: 8px;border-bottom: solid 1px silver;">
		<div style="margin-bottom: 4px;">
		<a id="logoutButton" href="#" onclick="doLogout();void(0);return false;" ct:isLogin="true">注销</a>
		<a id="registButton" href="#" onclick="alert('很抱歉，此功能暂未实现！');void(0);return false;" ct:isLogin="false">闪电注册</a>
		<a id="loginButton" href="#" onclick="doLogin();void(0);return false;" ct:isLogin="false">登录</a>
		<input type="text" style="width: 160px;color: silver;margin-left: 20px;" value="搜索－此功能暂未实现！">
		<br/>
		</div>
	</div>
	<br>
	<a href="/full/contact">添加新联系人</a>
	<br><br>
	<table id="contactTable" cellpadding="0" background="#ffffff" cellspacing="0" width="100%">
		<thead style="font-weight: bold;">
			<tr style="height: 26px;background-image: url('/full/images/head.jpg');color: white;">
				<td style="width: 110px;">&nbsp;姓 名</td>
				<td style="width: 110px;">&nbsp;移动电话</td>
				<td style="width: 130px;">&nbsp;电子邮件</td>
				<td>&nbsp;住址</td>
				<td style="width: 130px;" ct:role="'admin','manager'">&nbsp;操作</td>
			</tr>
		</thead>
		<tbody>
			<tr style="cursor: pointer;height: 22px;" ct:for="contact : ctx.content">
				<td>&nbsp;<a href="contacts/${contact.id}">${contact.name}</a></td>
				<td>&nbsp;${contact.mobilePhone}</td>
				<td>&nbsp;${contact.email}</td>
				<td>&nbsp;${contact.address}</td>
				<td ct:role="'admin','manager'">&nbsp;<a href="contacts/${contact.id}!delete">删除</a></td>
			</tr>
		</tbody>
	</table>
	<br/>
	<div><img src="/full/images/go.gif" style="margin-bottom: -22px;margin-left: 0px;"><a href="/full/contactAjaxDemo.html" style="font-weight: bold;font-size: 20px;">返回体验AJAX版</a><a href="/full" style="font-weight: bold;font-size: 12px;margin-left:40px;">首  页</a></div>

	<div id="securityDiv" style="display: none;">
		<div style="width: 100%;height: 26px;background-image: url('images/head.jpg');"><img src="images/close.gif" style="cursor: pointer;margin-top: 4px;" onclick="closeLoginWindow();"></div>
		<iframe id="securityIframe" frameborder="0" width="280" height="200" scrolling="no"></iframe>
	</div>
</body>
</html>