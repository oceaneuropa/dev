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
	ProgramInfo[] programs = (ProgramInfo[]) request.getAttribute("programs");

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
		programs = new ProgramInfo[0];
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
		<!-- <a href="<%=contextRoot + "/domain/nodeproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>"namename%></a> -->
		Node [<%=name%>]
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
					<th class="th1" width="150">Name</th>
					<th class="th1" width="150">Id and Version</th>
					<th class="th1" width="150">Type</th>
					<th class="th1" width="150">State</th>
					<th class="th1" width="250">Actions</th>
				</tr>
				<%
					if (programs.length == 0) {
				%>
				<tr>
					<td colspan="6">(n/a)</td>
				</tr>
				<%
					} else {
								for (ProgramInfo program : programs) {
									String appType = program.getType();
									String appId = program.getId();
									String appVersion = program.getVersion();
									String appName = program.getName();
									String activationStateLabel = program.getActivationState().getLabel();
									String runtimeStateLabel = program.getRuntimeState().getLabel();
									int numOfProblems = program.getNumOfProblems();

									boolean isActivated = (ProgramInfo.ACTIVATION_STATE.ACTIVATED.equals(program.getActivationState())) ? true : false;
									boolean isStarted = (ProgramInfo.RUNTIME_STATE.STARTED.equals(program.getRuntimeState())) ? true : false;

									String statusColor1 = isActivated ? "#2eb82e" : "#cccccc";
									String statusColor2 = (isActivated && isStarted) ? "#2eb82e" : "#cccccc";
									String statusColor3 = isStarted ? "#2eb82e" : "#cccccc";
				%>
				<tr>
					<td class="td1">
						<input type="checkbox" name="id_version" value="<%=appId + "_" + appVersion%>">
					</td>
					<td class="td2"><%=appName%></td>
					<td class="td2"><%=appId + " (" + appVersion + ")"%></td>
					<td class="td1"><%=appType%></td>
					<td class="td1">
						<font color="<%=statusColor1%>"><%=activationStateLabel%></font>
						<font color="<%=statusColor2%>"> | </font>
						<font color="<%=statusColor3%>"><%=runtimeStateLabel%></font>
					</td>
					<td class="td1">
						<a class="action01" href="javascript:activateProgram('<%=appId + "_" + appVersion%>')">Activate</a> | 
						<a class="action01" href="javascript:deactivateProgram('<%=appId + "_" + appVersion%>')">Deactivate</a> | 
						<a class="action01" href="javascript:startProgram('<%=appId + "_" + appVersion%>')">Start</a> | 
						<a class="action01" href="javascript:stopProgram('<%=appId + "_" + appVersion%>')">Stop</a> | 
						<a class="action01" target="_blank" href="<%=contextRoot%>/domain/nodeprogramproblems?machineId=<%=machineId%>&platformId=<%=platformId%>&id=<%=id%>&programId=<%=appId%>&programVersion=<%=appVersion%>">Problems (<%=numOfProblems%>)</a>
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

	<dialog id="startProgramDialog">
	<div class="dialog_title_div01" id="startProgramDialogTitleDiv">Start Program</div>
	<form id="start_program_form" method="post" action="<%=contextRoot + "/domain/nodeprogramaction"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" name="action" value="start">
		<input type="hidden" id="start_program__id_version" name="id_version" value="">
		<div class="dialog_main_div01" id="startProgramDialogMessageDiv">Are you sure you want to start the program?</div>
		<div class="dialog_button_div01">
			<a id="okStartProgram" class="button02">OK</a> 
			<a id="cancelStartProgram" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="stopProgramDialog">
	<div class="dialog_title_div01" id="stopProgramDialogTitleDiv">Stop Program</div>
	<form id="stop_program_form" method="post" action="<%=contextRoot + "/domain/nodeprogramaction"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" name="action" value="stop">
		<input type="hidden" id="stop_program__id_version" name="id_version" value="">
		<div class="dialog_main_div01" id="stopProgramDialogMessageDiv">Are you sure you want to stop the program?</div>
		<div class="dialog_button_div01">
			<a id="okStopProgram" class="button02">OK</a> 
			<a id="cancelStopProgram" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="activateProgramDialog">
	<div class="dialog_title_div01">Activate Program</div>
	<form id="activate_program_form" method="post" action="<%=contextRoot + "/domain/nodeprogramaction"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" name="action" value="activate">
		<input type="hidden" id="activate_program__id_version" name="id_version" value="">
		<div class="dialog_main_div01">Are you sure you want to activate the program?</div>
		<div class="dialog_button_div01">
			<a id="okActivateProgram" class="button02">OK</a> 
			<a id="cancelActivateProgram" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deactivateProgramDialog">
	<div class="dialog_title_div01">Deactivate Program</div>
	<form id="deactivate_program_form" method="post" action="<%=contextRoot + "/domain/nodeprogramaction"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" name="action" value="deactivate">
		<input type="hidden" id="deactivate_program__id_version" name="id_version" value="">
		<div class="dialog_main_div01">Are you sure you want to deactivate the program?</div>
		<div class="dialog_button_div01">
			<a id="okDeactivateProgram" class="button02">OK</a> 
			<a id="cancelDeactivateProgram" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

</body>
</html>
