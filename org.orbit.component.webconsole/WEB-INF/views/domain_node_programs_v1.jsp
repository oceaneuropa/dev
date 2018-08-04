<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.model.*"%>
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
					<th class="th1" width="200">Type</th>
					<th class="th1" width="400">Id/Version</th>
					<th class="th1" width="150">Name</th>
				</tr>
				<%
					if (programs.length == 0) {
				%>
				<tr>
					<td colspan="4">(n/a)</td>
				</tr>
				<%
					} else {
						for (ProgramManifest program : programs) {
							String appType = program.getType();
							String appId = program.getId();
							String appVersion = program.getVersion();
							String appName = program.getName();
				%>
				<tr>
					<td class="td1">
						<input type="checkbox" name="id_version" value="<%=appId + "_" + appVersion%>">
					</td>
					<td class="td1"><%=appType%></td>
					<td class="td2"><%=appId + "_" + appVersion%></td>
					<td class="td2"><%=appName%></td>
					<td class="td1">
						<a class="action01" href="javascript:deleteAttribute('<%=contextRoot + "/domain/nodeprogramuninstall"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>', '<%=appId%>', '<%=appVersion%>')">Uninstall</a>  
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

	<dialog id="changeAttributeDialog">
	<div class="dialog_title_div01">Set Attribute</div>
	<form id="update_form" method="post" action="<%=contextRoot + "/domain/nodeattributeupdate"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>"> 
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" id="attribute_oldName" name="oldName">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Name:</td>
					<td width="75%">
						<input type="text" id="attribute_name" name="name" class="input01" size="20">
					</td>
				</tr>
				<tr>
					<td>Value:</td>
					<td>
						<textarea id="attribute_value" name="value" rows="10" cols="100"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeAttribute" class="button02" href="javascript:document.getElementById('update_form').submit();">OK</a> 
			<a id="cancelChangeAttribute" class="button02b" href="javascript:document.getElementById('update_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteAttributeDialog">
	<div class="dialog_title_div01">Delete Attribute</div>
	<div class="dialog_main_div01" id="deleteAttributeDialogMessageDiv">Are you sure you want to delete the attribute?</div>
	<div class="dialog_button_div01">
		<a id="okDeleteAttribute" class="button02">OK</a> 
		<a id="cancelDeleteAttribute" class="button02b">Cancel</a>
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
