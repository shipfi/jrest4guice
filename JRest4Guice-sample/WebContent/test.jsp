<%@page import="java.security.*"%>
<%@page import="javax.security.auth.*"%>
<%@page import="java.util.Enumeration"%>
<html>
<h1>Hello World!</h1>

<pre>
<% Subject subject = Subject.getSubject(AccessController.getContext()); %> 


<b>Subject</b> = <%= subject %>

-----------------------------------------------

<b>RemoteUser</b> = <%= request.getRemoteUser() %>

-----------------------------------------------

<%
	out.print("Is user in role \"admin\"?: ");
	if (request.isUserInRole("editor")) {
		out.println("yes");
	} else {
		out.println("no");
	}
%> 

-----------------------------------------------

<b>Session Contents</b>
<% 
	java.util.Enumeration atts = session.getAttributeNames();
	while (atts.hasMoreElements()) {
		String elem = (String)atts.nextElement();
		out.println(elem + " -> " + session.getAttribute(elem));
		out.println( );
	}
%>

</pre>

</html>

