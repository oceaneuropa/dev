<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.platform.api.ServiceInfo.*"%>
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
	ServiceInfo[] services = (ServiceInfo[]) request.getAttribute("services");

	// ------------------------------------------------------------------------
	// Smooth data
	// ------------------------------------------------------------------------
	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	String platformName = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String platformId = (platformConfig != null) ? platformConfig.getId() : "";

	String id = (nodeInfo != null) ? nodeInfo.getId() : "";
	String name = (nodeInfo != null) ? nodeInfo.getName() : "";

	if (services == null) {
		services = new ServiceInfo[0];
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Services</title>
<link rel="stylesheet" href="../views/css/style.css">
<link rel="stylesheet" href="../views/css/treetable.css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/node_services_list.js"%>"></script>

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
		<h2>Services</h2>
		<div class="top_tools_div01">  
			<a id="actionStartProgram" class="button02" onClick="onProgramAction('start', '<%=contextRoot + "/domain/nodeprogramaction"%>')">Start</a> 
			<a id="actionStopPrograms" class="button02" onClick="onProgramAction('stop', '<%=contextRoot + "/domain/nodeprogramaction"%>')">Stop</a> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeservices?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<input type="hidden" name="id" value="<%=id%>">
				<input id ="main_list__action" type="hidden" name="action" value="">
				<tr>
					<th class="th1" width="15">
						<input type="checkbox" onClick="toggleSelection(this, 'sid')" />
					</th>
					<th class="th1" width="500">Name</th>
					<th class="th1" width="100">Auto Start</th>
					<th class="th1" width="100">State</th>
				</tr>
				<%
					if (services.length == 0) {
				%>
				<tr>
					<td colspan="4">(n/a)</td>
				</tr>
				<%
					} else {
						for (ServiceInfo service : services) {
							int currSID = service.getSID();
							String currName = service.getName();
							String currDesc = service.getDescription();
							boolean currAutoStart = service.isAutoStart();
							RUNTIME_STATE currRuntimeState = service.getRuntimeState();
							String currRuntimeStatelabel = currRuntimeState.getLabel();

							if (currName == null) {
								currName = "";
							}
							if (currDesc == null) {
								currDesc = "";
							}
							
							boolean isStarted = (currRuntimeState != null && currRuntimeState.isStarted()) ? true : false;
							String statusColor = isStarted ? "#2eb82e" : "#cccccc";
							
							String descColor = "#aaaaaa";
				%>
				<tr>
					<td class="td1"><input type="checkbox" name="sid" value="<%=currSID%>"></td>
					<td class="td2">
						<%=currName%>
						<% if (!currDesc.isEmpty()) { %>
						<br/>
						<font color="<%=descColor%>"><%=currDesc%></font>
						<% } %>
					</td>
					<td class="td1"><%=currAutoStart%></td>
					<td class="td1"><font color="<%=statusColor%>"><%=currRuntimeState%></font></td>
				</tr>
				<%
						}
					}
				%>
			</form>
		</table>
	</div>
	<br/>

	<dialog id="programsSelectionDialog">
	<div class="dialog_title_div01">Programs</div>
	<form id="install_form" method="post" action="<%=contextRoot + "/domain/nodeprograminstall"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<div id="programsSelectionDiv" class="dialog_main_div02">
		</div>
		<div class="dialog_button_div01">
			<a id="okInstallProgram" class="button02" href="javascript:document.getElementById('install_form').submit();">Install</a> 
			<a id="cancelInstallProgram" class="button02b" href="javascript:document.getElementById('install_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="uninstallProgramsDialog">
		<div class="dialog_title_div01">Uninstall Programs</div>
		<div class="dialog_main_div01" id="uninstallProgramsDialogMessageDiv">Are you sure you want to uninstall selected programs?</div>
		<div class="dialog_button_div01">
			<a id="okUninstallPrograms" class="button02">OK</a> 
			<a id="cancelUninstallPrograms" class="button02b">Cancel</a>
		</div>
	</dialog>

	<dialog id="programActionDialog">
	<div class="dialog_title_div01" id="programActionDialogTitleDiv" >{Action} Programs</div>
	<div class="dialog_main_div01" id="programActionDialogMessageDiv">Are you sure you want to {action} the programs?</div>
	<div class="dialog_button_div01">
		<a id="okProgramAction" class="button02">OK</a> 
		<a id="cancelProgramAction" class="button02b">Cancel</a>
	</div>
	</dialog>

</body>
</html>
