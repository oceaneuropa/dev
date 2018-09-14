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
<script type="text/javascript" src="<%=contextRoot + "/views/js/node_programs_list.js"%>"></script>

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
			<a id="actionUninstallPrograms" class="button02" onClick="onProgramAction('uninstall', '<%=contextRoot + "/domain/nodeprogramaction"%>')">Uninstall</a> 
			<a id="actionActivateProgram" class="button02" onClick="onProgramAction('activate', '<%=contextRoot + "/domain/nodeprogramaction"%>')">Activate</a> 
			<a id="actionDeactivatePrograms" class="button02" onClick="onProgramAction('deactivate', '<%=contextRoot + "/domain/nodeprogramaction"%>')">Deactivate</a> 
			<a id="actionStartProgram" class="button02" onClick="onProgramAction('start', '<%=contextRoot + "/domain/nodeprogramaction"%>')">Start</a> 
			<a id="actionStopPrograms" class="button02" onClick="onProgramAction('stop', '<%=contextRoot + "/domain/nodeprogramaction"%>')">Stop</a> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeprograms?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<input type="hidden" name="id" value="<%=id%>">
				<input id ="main_list__action" type="hidden" name="action" value="">
				<tr>
					<th class="th1" width="10">
						<input type="checkbox" onClick="toggleSelection(this, 'id_version')" />
					</th>
					<th class="th1" width="160">Type</th>
					<th class="th1" width="220">Id/Version</th>
					<th class="th1" width="220">Name</th>
					<th class="th1" width="150">State</th>
				</tr>
				<%
					if (programs.length == 0) {
				%>
				<tr>
					<td colspan="5">(n/a)</td>
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

							boolean isActivated = (ProgramManifest.ACTIVATION_STATE.ACTIVATED.equals(program.getActivationState())) ? true : false;
							boolean isStarted = (ProgramManifest.RUNTIME_STATE.STARTED.equals(program.getRuntimeState())) ? true : false;

							String statusColor1 = isActivated ? "#2eb82e" : "#cccccc";
							String statusColor2 = (isActivated && isStarted) ? "#2eb82e" : "#cccccc";
							String statusColor3 = isStarted ? "#2eb82e" : "#cccccc";
				%>
				<tr>
					<td class="td1">
						<input type="checkbox" name="id_version" value="<%=appId + "_" + appVersion%>">
					</td>
					<td class="td1"><%=appType%></td>
					<td class="td2"><%=appId + "_" + appVersion%></td>
					<td class="td2"><%=appName%></td>
					<td class="td1">
						<font color="<%=statusColor1%>"><%=activationStateLabel%></font>
						<font color="<%=statusColor2%>"> | </font>
						<font color="<%=statusColor3%>"><%=runtimeStateLabel%></font>
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
