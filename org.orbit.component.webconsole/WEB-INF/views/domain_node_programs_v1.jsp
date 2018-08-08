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
	ProgramManifest[] programs = (ProgramManifest[]) request.getAttribute("programs");

	// ------------------------------------------------------------------------
	// Smooth data
	// ------------------------------------------------------------------------
	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	String platformName = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String platformId = (platformConfig != null) ? platformConfig.getId() : "";

	String id = (nodeInfo != null) ? nodeInfo.getId() : "";
	String name = (nodeInfo != null) ? nodeInfo.getName() : "";

	if (programs == null) {
		programs = new ProgramManifest[0];
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
<script type="text/javascript" src="<%=contextRoot + "/views/js/domain_node_programs_v2.js"%>"></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> > 
		<a href="<%=contextRoot%>/domain/platforms?machineId=<%=machineId%>"><%=machineName%></a> >
		<a href="<%=contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId%>"><%=platformName%></a> >
		<!-- <a href="<%=contextRoot + "/domain/nodeproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>"><%=name%></a> -->
		<%=name%>
	</div>

	<div class="main_div01">
		<h2>Programs</h2>
		<div class="top_tools_div01">
			<a id="actionInstallProgram" class="button02">Install</a> 
			<a id="actionUninstallPrograms" class="button02">Uninstall</a> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeprograms?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="<%=contextRoot + "/domain/nodeprogramuninstall"%>">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<input type="hidden" name="id" value="<%=id%>">
				<tr>
					<th class="th1" width="12"></th>
					<th class="th1" width="100">Type</th>
					<th class="th1" width="150">Id/Version</th>
					<th class="th1" width="150">Name</th>
					<th class="th1" width="100">State</th>
					<th class="th1" width="150">Actions</th>
				</tr>
				<%
					if (programs.length == 0) {
				%>
				<tr>
					<td colspan="6">(n/a)</td>
				</tr>
				<%
					} else {
						for (ProgramManifest program : programs) {
							String appType = program.getType();
							String appId = program.getId();
							String appVersion = program.getVersion();
							String appName = program.getName();
							String activationStateLabel = program.getActivationState().getLabel();
							String runtimeStateLabel = program.getRuntimeState().getLabel();

							String statusColor = "#cccccc";
							if (ProgramManifest.RUNTIME_STATE.STARTED.equals(program.getRuntimeState())) {
								statusColor = "#2eb82e";
							}
				%>
				<tr>
					<td class="td1">
						<input type="checkbox" name="id_version" value="<%=appId + "_" + appVersion%>">
					</td>
					<td class="td1"><%=appType%></td>
					<td class="td2"><%=appId + "_" + appVersion%></td>
					<td class="td2"><%=appName%></td>
					<td class="td1"><font color="<%=statusColor%>"><%=activationStateLabel + " | " + runtimeStateLabel%></font></td>
					<td class="td1">
						<a class="action01" href="javascript:programAction('<%=contextRoot + "/domain/nodeprogramaction"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>', '<%=appId%>', '<%=appVersion%>', 'activate')">Activate</a>
						<a class="action01" href="javascript:programAction('<%=contextRoot + "/domain/nodeprogramaction"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>', '<%=appId%>', '<%=appVersion%>', 'deactivate')">Deactivate</a>
						<a class="action01" href="javascript:programAction('<%=contextRoot + "/domain/nodeprogramaction"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>', '<%=appId%>', '<%=appVersion%>', 'start')">Start</a>
						<a class="action01" href="javascript:programAction('<%=contextRoot + "/domain/nodeprogramaction"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>', '<%=appId%>', '<%=appVersion%>', 'stop')">Stop</a>
						<a class="action01" href="javascript:programAction('<%=contextRoot + "/domain/nodeprogramaction"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>', '<%=appId%>', '<%=appVersion%>', 'uninstall')">Uninstall</a>  
					</td>
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

	<dialog id="programActionDialog">
	<div class="dialog_title_div01" id="programActionDialogTitleDiv" >{Action} Program</div>
	<div class="dialog_main_div01" id="programActionDialogMessageDiv">Are you sure you want to {action} the program?</div>
	<div class="dialog_button_div01">
		<a id="okProgramAction" class="button02">OK</a> 
		<a id="cancelProgramAction" class="button02b">Cancel</a>
	</div>
	</dialog>

	<dialog id="deleteAttributesDialog">
	<div class="dialog_title_div01">Delete Attributes</div>
	<div class="dialog_main_div01" id="deleteAttributesDialogMessageDiv">Are you sure you want to delete selected attributes?</div>
	<div class="dialog_button_div01">
		<a id="okDeleteAttributes" class="button02">OK</a> 
		<a id="cancelDeleteAttributes" class="button02b">Cancel</a>
	</div>
	</dialog>

</body>
</html>
