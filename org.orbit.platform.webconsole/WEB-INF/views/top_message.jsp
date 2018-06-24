<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.orbit.platform.webconsole.*"%>
<%
	String contextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String message = (String) request.getAttribute("message");
	if (message != null && !message.isEmpty()) {
%>
<script type="text/javascript" src="<%=contextRoot + "/views/js/top_message.js"%>"></script>
<div id="message_div" class="message_div01">
	<%=message%>
	<a href="javascript:showHide('message_div')" style="float: right">Dismiss</a>
</div>
<p></p>
<%
	}
%>
