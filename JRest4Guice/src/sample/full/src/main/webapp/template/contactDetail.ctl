<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>联系人详细信息(CTL template)</title>
<link href="@{ctxPath}/css/default.css" rel="stylesheet" type="text/css" />
</head>
<body>
	@set{contact=ctx.content}
	@set{action=ctxPath+"/contact"}
	@if{contact.id==null}
		<h4>请输入联系人的相关信息</h4>
	@else
		<h4>修改联系人<font color=green>"@{contact.name}"</font>的信息</h4>
		@set{action=ctxPath+"/contacts/"+contact.id+"!update"}
	@end
	
	<form action="@{action}" method="post">
		<input name="id" type="hidden" value="@{contact.id}" ct:if="contact.id!=null">
		<table>
			<tr>
				<td>姓    名</td><td><input name="name" type="text" value="@{contact.name}"><span class="error">@{ctx.invalidValueMap.name.message}</span></td>	
			</tr>
			<tr>
				<td>手    机</td><td><input name="mobilePhone" type="text" value="@{contact.mobilePhone}"><span class="error">@{ctx.invalidValueMap.mobilePhone.message}</span></td>	
			</tr>
			<tr>
				<td>电子邮箱</td><td><input name="email" type="text" value="@{contact.email}"><span class="error">@{ctx.invalidValueMap.email.message}</span></td>	
			</tr>
			<tr>
				<td>家庭住址</td><td><textarea name="address">@{contact.address}</textarea><span class="error">@{ctx.invalidValueMap.address.message}</span></td>
			</tr>
		</table>
		<br/>
		<input type="submit" value="确定">
		<input type="reset" value="取消">
	</form>
	
	<br>
	<img src="@{ctxPath}/images/go.gif" style="margin-bottom: -22px;"><a href="@{ctxPath}/contacts">返回联系人列表</a>
	</br>
</body>
</html>