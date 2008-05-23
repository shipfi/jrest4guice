<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String isRelogin = request.getParameter("isRelogin");
	if(isRelogin != null && isRelogin.equalsIgnoreCase("true")){
		session.invalidate();
	}
	if(request.getUserPrincipal()!=null)
		response.sendRedirect("main.html");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>JRest4Guice安全测试</title>
		<style type="text/css">
			body{
				margin: 0;
				width: 100%;
				height: 100%;
			}
			.hr{
				border:1px solid #abc;
				width: 130px;
			}
		</style>
	</head>
	<body>
		<form name="loginform" id="loginform" method="post" action="j_security_check">
			<table style="width: 100%;height: 100%;" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="middle" style="text-align: center;">
						<div style="height: 480px;font-size: 12px;text-align: center;">
							<div style="text-align: center;padding-top: 250px">
								<div style="width: 380px;cursor: pointer;margin: 0 auto;padding-left: 280px;">
									<div style="clear: both;height: 25px;"><div style="float: left;line-height: 25px">用户名：</div><div style="float: left;"><input name="j_username" type="text" class="hr" size="15" value="cnoss" /></div></div>
									<div style="clear: both;height: 25px;"><div style="float: left;line-height: 25px">密　码：</div><div style="float: left;"><input name="j_password" type="password" class="hr" size="15" value="123" /></div></div>
									<div style="clear: both;height: 25px;"><div style="float: left;line-height: 25px;margin-right: 45px;">&nbsp;</div><div style="float: left;"><input name="Submit" type="button" class="style7" value="登  录" onclick="loginform.submit();" style=" cursor:pointer" /></div></div>
								</div>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
