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
	String id = (String) request.getAttribute("id");
	NodeInfo nodeInfo = (NodeInfo) request.getAttribute("nodeInfo");
	ProgramInfo program = (ProgramInfo) request.getAttribute("program");
	Problem[] problems = (Problem[]) request.getAttribute("problems");

	// ------------------------------------------------------------------------
	// Smooth data
	// ------------------------------------------------------------------------
	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	String platformName = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String platformId = (platformConfig != null) ? platformConfig.getId() : "";

	// String id = (nodeInfo != null) ? nodeInfo.getId() : "";
	String name = (nodeInfo != null) ? nodeInfo.getName() : "";

	String programId = "";
	String programVersion = "";
	String programSimpleName = "";
	if (program != null) {
		programId = program.getId();
		programVersion = program.getVersion();
		// programSimpleName = program.getSimpleName();
		programSimpleName = program.getName();
	}

	if (problems == null) {
		problems = new Problem[0];
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Programs</title>
<link rel="stylesheet" href="../views/css/style.css">
<link rel="stylesheet" href="../views/css/treetable.css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/node_programs.js"%>"></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> > 
		<a href="<%=contextRoot%>/domain/platforms?machineId=<%=machineId%>">Platforms</a> (of <%=machineName%>)>
		<a href="<%=contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId%>">Nodes</a> (of Platform [<%=platformName%>]) >
		<a href="<%=contextRoot + "/domain/nodeprograms?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Programs</a> (of Node [<%=name%>]) >
		Program [<%=programSimpleName%>] 
	</div>

	<div class="main_div01">
		<h2>Problems</h2>
		<div class="top_tools_div01">
			<a class="button02" href="<%=contextRoot + "/domain/nodeprogramproblemsaction?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id + "&programId=" + programId + "&programVersion=" + programVersion + "&action=clear"%>">Clear</a>
			<a class="button02" href="<%=contextRoot + "/domain/nodeprogramproblems?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id + "&programId=" + programId + "&programVersion=" + programVersion%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<input type="hidden" name="id" value="<%=id%>">
				<input type="hidden" name="programId" value="<%="programId"%>">
				<input type="hidden" name="programVersion" value="<%="programVersion"%>">
				<input id ="main_list__action" type="hidden" name="action" value="">
				<tr>
					<th class="th1" width="100">Time</th>
					<th class="th1" width="150">Message</th>
				</tr>
				<%
					if (problems.length == 0) {
				%>
				<tr>
					<td colspan="2">(empty)</td>
				</tr>
				<%
					} else {
						for (Problem problem : problems) {
							Date time = problem.getTime();
							String message = problem.getMessage();

							String timeStr = (time != null) ? time.toString() : "";
				%>
				<tr>
					<td class="td1"><%=timeStr%></td>
					<td class="td2"><%=message%></td>
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
