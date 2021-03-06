<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String mainContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create New Account</title>

<link rel="stylesheet" href="<%=mainContextRoot + "/views/css/style.css"%>">
<link rel="stylesheet" href="<%=mainContextRoot + "/views/css/main.css"%>">
<style type="text/css">
body {
	background-image: url("<%=mainContextRoot%>/views/images/1440x900/simple-09-1440x900.png");
}
</style>

<script type="text/javascript">
function loginPage() {
	window.location.href = "<%=mainContextRoot%>";
}

function createNewAccountPage() {
	window.location.href = "<%=mainContextRoot + WebConstants.ORIGIN_CREATE_NEW_ACCOUNT_PAGE_PATH%>";
}

function createNewAccount() {
	document.getElementById('new_form').action="<%=mainContextRoot + WebConstants.ORIGIN_CREATE_NEW_ACCOUNT_SERVLET_PATH%>";
	document.getElementById('new_form').submit();
}
</script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div>
		<div class="title1" style="width: 50%; margin: auto; padding: 50px; text-align: center;">
			Origin
		</div>
		<div class="form_main_div01" style="user-select: none;">
			<table class="form_table01" style="width: 350px; color: #efefef;">
				<tr>
					<td valign="top" height="330">
						<form id="new_form" method="post" action="<%=mainContextRoot + "/main_signin"%>">
							Account Name:<br />
							<input type="text" name="username" class="text_field_01" size="35"><br />
							Email:<br />
							<input type="text" name="email" class="text_field_01" size="35"><br />
							New Password:<br />
							<input type="password" name="password" class="text_field_01"><br />
							Confirm Password:<br />
							<input type="password" name="password" class="text_field_01">
						</form>
						<input type="button" class="main_button_01" style="float: right; margin-top: 15px;" onclick="createNewAccount()" value="Create New Account">
					</td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" class="main_button_01" onclick="loginPage()" value="< Login Page">
						<input type="button" class="main_button_01" onclick="createNewAccountPage()" value="Create Account Page">
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
