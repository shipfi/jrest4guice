<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/full/css/default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="/full/javascript/lib/jquery-1.2.5.js"></script>
	<script type="text/javascript" src="/full/javascript/lib/jquery.blockUI.js"></script>
	<script type="text/javascript" src="/full/javascript/spry/SpryData.js"></script>
	<script type="text/javascript" src="/full/javascript/lib/user-ext.js"></script>
	<script type="text/javascript" src="/full/security.js"></script>
</head>
<body>
	<div style="background-image: url('images/body_head.gif');height: 84px;position: absolute;top:0px; width: 100%;left: 0px;">&nbsp;</div>
	<img src="images/mouse.png" class="png" style="position: absolute;top:-25px;left: 12px;width: 94px;height: 94px;">
	<div id="logoDiv" style="position: absolute;left:110px;top:28px;"><h3><span style="font-size:24px;color: green;font-weight: bold;">J</span>Rest4Guice</h3><h4 style="margin: -8px 0 0 48px;font-style: italic;">Simple demo</h4></div>
	<div style="margin: 30px 0 8px 1px;height: 55px;width:60%;"></div>
	<div id="welcomeDiv" style="position: absolute;top:42px;right: 8px;text-align: left;color: green;" ct:isLogin="true">
		<img class="png" src="images/people.png" style="margin-bottom: -3px;width: 24px;height: 24px;margin-right: 4px;">欢迎您回来，<a id="userInfoSpan" href="#" onclick="alert('查看个人资料！');void(0);return false;">cnoss</a><br/>
		<div style="margin-top: 8px;"><img src="images/warning.gif" style="margin-bottom: -3px;">你有&nbsp;<a id="todoSpan" href="#" onclick="alert('查看待办事项！');void(0);return false;">[4]</a>&nbsp;个待办事项要处理。</div>
		<div><img src="images/warning.gif" style="margin-bottom: -3px;">&nbsp;<a id="todoSpan" href="#" onclick="alert('查看待办事项！');void(0);return false;">[12]</a>&nbsp;个好友请求未处理。</div>
	</div>
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
</body>
</html>