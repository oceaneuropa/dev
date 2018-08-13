<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.PUBLIC_WEB_CONSOLE_CONTEXT_ROOT);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Identity</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div>
		<div class="form_title_div01">Sign Up</div>
		<div class="form_main_div01">
			<table class="form_table01">
				<form id="new_form" method="post" action="<%=contextRoot + "/signup_req"%>">
				<tr>
					<td width="25%">Username:</td>
					<td width="75%">
						<input type="text" name="username" class="input01" size="35">
					</td>
				</tr>
				<tr>
					<td>Email:</td>
					<td>
						<input type="text" name="email">
					</td>
				</tr>
				<tr>
					<td>Password:</td>
					<td>
						<input type="password" name="password">
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<a class="button02" href="javascript:document.getElementById('new_form').submit();">Create an account</a>
					</td>
				</tr>
				</form>
			</table>
		</div>
	</div>
</body>
</html>
