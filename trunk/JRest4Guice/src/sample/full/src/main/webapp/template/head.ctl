<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
		function deleteContact(cName){
			if(!window.confirm("您确定要删除联系人\""+cName+"\" 吗?")){
				return false;
			}
			return true;
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
</body>
</html>