<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统错误信息(CTL template)</title>
<link href="/full/css/default.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<center>
	<br>
	@set{msg = ctx.errorMessage}
	<img src="/full/images/error.png" style="margin-top:40px;">
	<h2>操作错误：@{msg}</h2>
	<div ct:if="ctx.invalidValues!=null">
		<hr/>
		<h4>错误明细</h4>
		<div style="text-align: left;width:33.3%;margin-top: -18px;">
			<hr/>
			<div ct:if="value : ctx.invalidValues">
				<b><font color=red>@{value.propertyName}：&nbsp;&nbsp;</font></b><font color=green>@{value.message}</font>
			</div>
			<hr/>
		</div>
	</div>
	<br><br>
	<img src="/full/images/go.gif" style="margin-bottom: -22px;"><a href="javascript:history.go(-1);" style="font-weight: bold;font-size: 18px;">返回</a>
	</br>
	</center>
</body>
</html>