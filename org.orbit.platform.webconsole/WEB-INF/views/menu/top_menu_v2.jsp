<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.orbit.platform.webconsole.*"%>
<%
	String contextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	Map<String, String> servicesURLMap = (Map<String, String>) request.getAttribute("servicesURLMap");
%>
<head>
<!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"> -->
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style_nav.css"%>">
</head>
<div class="navbar">
	<a href="#home">Home</a>
	<div class="dropdown">
		<button class="dropbtn">Services<i class="fa fa-caret-down"></i></button>
		<div class="dropdown-content">
			<%
				for (Iterator<String> itor1 = servicesURLMap.keySet().iterator(); itor1.hasNext();) {
					String currServiceName = itor1.next();
					String currServiceURL = servicesURLMap.get(currServiceName);
			%>
			<a href="<%=currServiceURL%>"><%=currServiceName%></a>
			<%
				}
			%>
		</div>
	</div>
</div>
<p></p>
