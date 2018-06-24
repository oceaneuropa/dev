<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.component.api.tier3.domainmanagement.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
	MachineConfig machineConfig = (MachineConfig) request.getAttribute("machineConfig");

	String message = (String) request.getAttribute("message");
	PlatformConfig[] platformConfigs = (PlatformConfig[]) request.getAttribute("platformConfigs");
	if (platformConfigs == null) {
		platformConfigs = new PlatformConfig[0];
	}

	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Domain Management (Platforms)</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
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
</head>
<body>
	<div class="div01">
		<h2>Platforms</h2>
		<a href="<%=contextRoot%>/domain">Domain Management</a> > <%=machineName%>
		<table id="table01">
			<tr>
				<th width="50">Id</th>
				<th width="200">Name</th>
				<th width="200">Context Root</th>
				<th width="250">Host URL</th>
				<th width="250">Home</th>
			</tr>
			<%
				if (platformConfigs.length == 0) {
			%>
			<tr>
				<td colspan="8">(n/a)</td>
			</tr>
			<%
				} else {
					for (PlatformConfig platformConfig : platformConfigs) {
						String id = platformConfig.getId();
						String name = platformConfig.getName();
						String currContextRoot = platformConfig.getContextRoot();
						String hostURL = platformConfig.getHostURL();
						String home = platformConfig.getHome();

						id = StringUtil.get(id);
						name = StringUtil.get(name);
						currContextRoot = StringUtil.get(currContextRoot);
						hostURL = StringUtil.get(hostURL);
						home = StringUtil.get(home);
			%>
			<tr>
				<td id="td1"><%=id%></td>
				<td id="td2"><%=name%>s</td>
				<td id="td2"><%=currContextRoot%></td>
				<td id="td2"><%=hostURL%>s</td>
				<td id="td2"><%=home%>s</td>
			</tr>
			<%
				}
				}
			%>
		</table>
	</div>
</body>
</html>
