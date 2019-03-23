<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.component.api.tier3.domain.*"%>
<%@ page import="org.orbit.component.api.tier3.nodecontrol.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	// ------------------------------------------------------------------------
	// Get data
	// ------------------------------------------------------------------------
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	MachineConfig machineConfig = (MachineConfig) request.getAttribute("machineConfig");
	PlatformConfig platformConfig = (PlatformConfig) request.getAttribute("platformConfig");
	NodeInfo nodeInfo = (NodeInfo) request.getAttribute("nodeInfo");
	ExtensionInfo[] extensions = (ExtensionInfo[]) request.getAttribute("extensions");

	// ------------------------------------------------------------------------
	// Smooth data
	// ------------------------------------------------------------------------
	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	String platformName = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String platformId = (platformConfig != null) ? platformConfig.getId() : "";

	String id = (nodeInfo != null) ? nodeInfo.getId() : "";
	String name = (nodeInfo != null) ? nodeInfo.getName() : "";

	if (extensions == null) {
		extensions = new ExtensionInfo[0];
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Domain Management</title>
<link rel="stylesheet" href="../views/css/style.css">
<link rel="stylesheet" href="../views/css/treetable.css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> > 
		<a href="<%=contextRoot%>/domain/platforms?machineId=<%=machineId%>">Platforms</a> (of <%=machineName%>)>
		<a href="<%=contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId%>">Nodes</a> (of Platform [<%=platformName%>]) >
		<!-- <a href="<%=contextRoot + "/domain/nodeproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>"><%=name%></a> -->
		Node [<%=name%>]
	</div>

	<div class="main_div01">
		<h2>Extensions</h2>
		<table class="main_table01">
			<form id="main_list" method="post" action="">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<input type="hidden" name="id" value="<%=id%>">
				<input id ="main_list__action" type="hidden" name="action" value="">
				<tr>
					<th class="th1" width="150">Type</th>
					<th class="th1" width="150">Id</th>
					<th class="th1" width="150">Name</th>
					<th class="th1" width="150">Description</th>
				</tr>
				<%
					if (extensions.length == 0) {
				%>
				<tr>
					<td colspan="4">(n/a)</td>
				</tr>
				<%
					} else {
						for (ExtensionInfo extension : extensions) {
							String currTypeId = extension.getTypeId();
							String currId = extension.getId();
							String currName = extension.getName();
							String currDesc = extension.getDescription();
							
							if (currName == null) {
								currName = "";
							}
							if (currDesc == null) {
								currDesc = "";
							}
				%>
				<tr>
					<td class="td2"><%=currTypeId%></td>
					<td class="td2"><%=currId%></td>
					<td class="td2"><%=currName%></td>
					<td class="td2"><%=currDesc%></td>
				</tr>
				<%
						}
					}
				%>
			</form>
		</table>
	</div>
	<br/>

</body>
</html>
