<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String mainContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);
	
	String message = (String) request.getAttribute("message");
	String redirectURL = (String) request.getAttribute("redirectURL");
	if (message == null) {
		message = "";
	}
	if (redirectURL == null) {
		redirectURL = "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Origin</title>

<link rel="stylesheet" href="<%=mainContextRoot + "/views/css/style.css"%>">
<link rel="stylesheet" href="<%=mainContextRoot + "/views/css/main.css"%>">
<style type="text/css">
body {
	background-image: url("<%=mainContextRoot%>/views/images/1440x900/simple-09-1440x900.png");
}
</style>

<script type="text/javascript">
function goToRedirectURL() {
	window.location.href = "<%=redirectURL%>";
}
</script>

</head>
<body>
	<!-- jsp:include page="<%=platformContextRoot + "/top_message"%>" / -->
	<div>
		<div class="title1" style="width: 50%; margin: auto; padding: 50px; text-align: center; user-select: none;">
			Origin
		</div>
		<div class="form_main_div01" style="user-select: none;">
			<table class="form_table01" style="width: 300px; color: #efefef;">
				<tr>
					<td align="center" valign="top" height="40">
						<%=message%>
					</td>
				</tr>
				<tr>
					<td align="center">
						<input type="button" class="main_button_01" onclick="goToRedirectURL()" value="OK"> 
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
