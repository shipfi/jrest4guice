<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>distribute session test</title>
</head>
<body>
	<%
		session.setAttribute("userMsg","Welcom cnoss !!");
		System.out.println("设置userMsg为："+session.getAttribute("userMsg"));
	%>
	设置session值成功！！<br>
	<hr>
	<a href="readSession.jsp">读取</a>
</body>
</html>