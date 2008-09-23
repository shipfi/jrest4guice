<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>联系人管理(CTL template)</title>
<link href="/full/css/default.css" rel="stylesheet" type="text/css" />
</head>
<body>

	$embed{"head.ctl"}

	<a href="/full/contact">添加新联系人</a>
	<br><br>
	<table id="contactTable" cellpadding="0" cellspacing="0" width="100%">
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
				<td ct:role="'admin','manager'">&nbsp;<a href="contacts/${contact.id}!delete" onclick="return deleteContact('${contact.name}');">删除</a></td>
			</tr>
		</tbody>
	</table>
	
	$include{"pageNavigation.ctl",(pageUrl:"/full/contacts?page=",ctx:ctx)}
	
	<div id="securityDiv" style="display: none;">
		<div style="width: 100%;height: 26px;background-image: url('images/head.jpg');"><img src="images/close.gif" style="cursor: pointer;margin-top: 4px;" onclick="closeLoginWindow();"></div>
		<iframe id="securityIframe" frameborder="0" width="280" height="200" scrolling="no"></iframe>
	</div>

	$embed{"foot.ctl"}
</body>
</html>