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
		String value = (String)session.getAttribute("userMsg");
		System.out.println("读取userMsg："+value);
	%>
	
	session值：<%=value%><br>
	<hr>
	<a href="writeSession.jsp">设置</a>
</body>
</html>