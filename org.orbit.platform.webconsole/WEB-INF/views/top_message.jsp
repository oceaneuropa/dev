<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.orbit.platform.webconsole.*"%>
<%
	String contextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String message = (String) request.getAttribute("message");
	if (message != null && !message.isEmpty()) {
%>
<div id="message_div" class="message_div01">
	<%=message%>
	<a href="javascript:showHide('message_div')" style="float: right">Dismiss</a>
</div>
<p></p>
<script>
	function showHide(elementId) {
		var x = document.getElementById(elementId);
		if (x.style.display === "none") {
			x.style.display = "block";
		} else {
			x.style.display = "none";
		}
	}
</script>
<%
	}
%>
