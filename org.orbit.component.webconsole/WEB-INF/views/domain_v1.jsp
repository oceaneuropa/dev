<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.component.api.tier3.domainmanagement.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
	MachineConfig[] machineConfigs = (MachineConfig[]) request.getAttribute("machineConfigs");
	if (machineConfigs == null) {
		machineConfigs = new MachineConfig[0];
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Domain Management</title>
<%
	out.print("<link rel=\"stylesheet\" href=\"" + contextRoot + "/views/css/style.css\">");
%>
</head>
<body>
	<div class="div01">
		<h2>Machines</h2>
		<table id="table01">
			<tr>
				<th width="200">Id</th>
				<th width="250">Name</th>
				<th width="250">IP Address</th>
				<th width="200">Actions</th>
			</tr>
			<%
				if (machineConfigs.length == 0) {
			%>
			<tr>
				<td colspan="8">(n/a)</td>
			</tr>
			<%
				} else {
					for (MachineConfig machineConfig : machineConfigs) {
						String id = machineConfig.getId();
						String name = machineConfig.getName();
						String ip = machineConfig.getIpAddress();

						id = StringUtil.get(id);
						name = StringUtil.get(name);
						ip = StringUtil.get(ip);
			%>
			<tr>
				<td id="td1"><%=id%></td>
				<td id="td2"><%=name%>s</td>
				<td id="td2"><%=ip%></td>
				<td id="td1"><a href="<%=contextRoot%>/domain/platforms?machineId=<%=id%>">View Platforms</a></td>
			</tr>
			<%
				}
				}
			%>
		</table>
	</div>
</body>
</html>
