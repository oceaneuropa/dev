<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	String username = (String) session.getAttribute(WebConstants.SESSION__USERNAME);
	String fullName = (String) session.getAttribute(WebConstants.SESSION__FULLNAME);
	String tokenType = (String) session.getAttribute(WebConstants.SESSION__TOKEN_TYPE);
	String accessToken = (String) session.getAttribute(WebConstants.SESSION__ACCESS_TOKEN);
	boolean isTokenValid = false;
	Object isTokenValidObj = request.getAttribute("isTokenValid");
	if (isTokenValidObj instanceof Boolean) {
		isTokenValid = (Boolean) isTokenValidObj;
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="main_div01">
		<h2>Home</h2>
		<div>
			Username: <%=username%> <br/>
			Full Name: <%=fullName%> <br/>
			Token Type: <%=tokenType%> <br/>
			Token Valid: <%=isTokenValid%>
		</div>
	</div>

</body>
</html>
