<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.origin.common.service.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.infra.api.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.component.api.tier3.domain.*"%>
<%@ page import="org.orbit.component.api.tier3.nodecontrol.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	MachineConfig machineConfig = (MachineConfig) request.getAttribute("machineConfig");
	PlatformConfig platformConfig = (PlatformConfig) request.getAttribute("platformConfig");

	NodeInfo[] nodeInfos = (NodeInfo[]) request.getAttribute("nodeInfos");
	Map<String, IndexItem> nodeIdToIndexItemMap = (Map<String, IndexItem>) request.getAttribute("nodeIdToIndexItemMap");

	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	String platformName = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String platformId = (platformConfig != null) ? platformConfig.getId() : "";

	if (nodeInfos == null) {
		nodeInfos = new NodeInfo[0];
	}
	if (nodeIdToIndexItemMap == null) {
		nodeIdToIndexItemMap = new HashMap<String, IndexItem>();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Nodes</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/nodes.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> > 
		<a href="<%=contextRoot%>/domain/platforms?machineId=<%=machineId%>">Platforms</a> (of <%=machineName%>)>
		<!-- <a href="<%=contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId%>"><%=platformName%></a> -->
		Platform [<%=platformName%>]
	</div>
	<div class="main_div01">
		<h2>Nodes</h2>
		<div class="top_tools_div01">
			<a id="action.addNode" class="button02">Create</a>
			<a id="action.deleteNodes" targetFormId="main_list" targetFormUrl="<%=contextRoot + "/domain/nodedelete"%>" class="button02">Delete</a>
			<a id="actionStartNodes" targetFormId="main_list" targetFormUrl="<%=contextRoot + "/domain/nodestart"%>" class="button02">Start</a> 
			<a id="action.stopNodes" targetFormId="main_list" targetFormUrl="<%=contextRoot + "/domain/nodestop"%>" class="button02">Stop</a> 
			<a id="actionInstallPrograms" class="button02">Batch Install</a> 
			<a id="actionUninstallPrograms" class="button02">Batch Uninstall</a>
			<a class="button02" href="<%=contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<tr>
					<th class="th1" width="20">
						<input type="checkbox" onClick="toggleSelection(this, 'id')" />
					</th>
					<th class="th1" width="100">JVM</th>
					<th class="th1" width="100">Id</th>
					<th class="th1" width="100">Name</th>
					<th class="th1" width="100">Status</th>
					<th class="th1" width="100">Metadata</th>
					<th class="th1" width="200">Actions</th>
				</tr>
				<%
					if (nodeInfos.length == 0) {
				%>
				<tr>
					<td colspan="7">(n/a)</td>
				</tr>
				<%
					} else {
						for (NodeInfo nodeInfo : nodeInfos) {
							String id = nodeInfo.getId();
							String name = nodeInfo.getName();
							URI uri = nodeInfo.getUri();
							String path = (uri != null) ? uri.getPath() : "(n/a)";
							String typeId = (String) nodeInfo.getAttributes().get("typeId");

							boolean isOnline = nodeInfo.getRuntimeStatus().isOnline();
							String runtimeState = nodeInfo.getRuntimeStatus().getRuntimeState();
							String statusStr1 = (isOnline)? "Online" : "Offline";
							String statusStr2 = runtimeState != null && !runtimeState.isEmpty() ? (" | " + runtimeState) : "";
							String statusColor = isOnline ? "#2eb82e" : "#cccccc";

							String metadataStr = "";
							
							String jvmName = null;
							String pid = null;
							if (nodeInfo.getRuntimeProperties().containsKey("jvm_name")) {
								jvmName = (String) nodeInfo.getRuntimeProperties().get("jvm_name");
								metadataStr += "jvm_name = " + jvmName;
							}
							if (nodeInfo.getRuntimeProperties().containsKey("pid")) {
								pid = (String) nodeInfo.getRuntimeProperties().get("pid");

								if (!metadataStr.isEmpty()) {
									metadataStr += "<br/>";
								}
								metadataStr += "pid = " + pid;
							}
							if (jvmName == null) {
								jvmName = "";
							}
							if (pid == null) {
								pid = "";
							}

							String hostURL = null;
							String currContextRoot = null;
							String nodePlatformId = null;
							String currUrl = null;
							String platformHome = null;
							IndexItem indexItem = nodeIdToIndexItemMap.get(id);

							if (indexItem != null) {
								hostURL = (String) indexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
								currContextRoot = (String) indexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);
								nodePlatformId = (String) indexItem.getProperties().get(PlatformConstants.IDX_PROP__PLATFORM_ID);
								currUrl = WebServiceAwareHelper.INSTANCE.getURL(hostURL, currContextRoot);
								platformHome = (String) indexItem.getProperties().get(PlatformConstants.IDX_PROP__PLATFORM_HOME);

								if (!metadataStr.isEmpty()) {
									metadataStr += "<br/>";
								}
								metadataStr += InfraConstants.SERVICE__HOST_URL + " = " + hostURL + "<br/>";
								metadataStr += InfraConstants.SERVICE__CONTEXT_ROOT + " = " + currContextRoot + "<br/>";
								metadataStr += PlatformConstants.IDX_PROP__PLATFORM_ID + " = " + nodePlatformId + "<br/>";
								if (currUrl != null) {
									metadataStr += "Platform URL = " + currUrl + "<br/>";	
								}
								if (platformHome != null) {
									metadataStr += PlatformConstants.IDX_PROP__PLATFORM_HOME + " = " + platformHome + "<br/>";	
								}
							}

							id = StringUtil.get(id);
							name = StringUtil.get(name);
							hostURL = StringUtil.get(hostURL, "");
							currContextRoot = StringUtil.get(currContextRoot, "");
							platformHome = StringUtil.get(platformHome, "");
				%>
				<tr>
					<td class="td1"><input type="checkbox" name="id" value="<%=id%>"></td>
					<td class="td1"><%=jvmName%></td>
					<td class="td1"><%=id%></td>
					<td class="td1"><%=name%></td>
					<td class="td1"><font color="<%=statusColor%>"><%=statusStr1%></font></td>
					<td class="td2">
						<div class="tooltip">metadatas...
  							<span class="tooltiptext"><%=metadataStr%></span>
						</div>
					</td>
					<td class="td1">
						<a class="action01" href="javascript:changeNode('<%=id%>', '<%=name%>', '<%=typeId%>')">Edit</a> |
						<a class="action01" href="javascript:startNode('<%=id%>')">Start</a> | 
						<a class="action01" href="javascript:stopNode('<%=id%>')">Stop</a> | 
						<a class="action01" target="_blank" href="<%=contextRoot%>/domain/nodeproperties?machineId=<%=machineId%>&platformId=<%=platformId%>&id=<%=id%>">Properties</a> |
						
						<% if (isOnline) { %>
							<a class="action01" target="_blank" href="<%=contextRoot%>/domain/nodeprograms?machineId=<%=machineId%>&platformId=<%=platformId%>&id=<%=id%>">Programs</a> | 
							<a class="action01" target="_blank" href="<%=contextRoot%>/domain/nodeservices?machineId=<%=machineId%>&platformId=<%=platformId%>&id=<%=id%>">Services</a>
						<% } else { %>
							<a class="action01b">Programs</a> | 
							<a class="action01b">Services</a>						
						<% } %>
						<!-- 
						 | <a class="action01" href="javascript:deleteNode('<%=contextRoot + "/domain/nodedelete"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>')">Delete</a> | 
						<a class="action01" href="javascript:startNode('<%=contextRoot + "/domain/nodestart"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>')">Start</a> | 
						<a class="action01" href="javascript:stopNode('<%=contextRoot + "/domain/nodestop"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>')">Stop</a></td>
						 -->
				</tr>
				<%
						}
					}
				%>
			</form>
		</table>
	</div>
	<br>

	<dialog id="newNodeDialog">
	<div class="dialog_title_div01">Create Node</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/domain/nodecreate"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" name="id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<td>Type:</td>
					<td><input type="text" name="typeId"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okAddNode" class="button02" href="javascript:document.getElementById('new_form').submit();">OK</a> 
			<a id="cancelAddNode" class="button02b" href="javascript:document.getElementById('new_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changeNodeDialog">
	<div class="dialog_title_div01">Change Node</div>
	<form id="update_form" method="post" action="<%=contextRoot + "/domain/nodeupdate"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>"> 
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" id="node_id" name="id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input id="node_name" type="text" name="name"></td>
				</tr>
				<tr>
					<td>Type:</td>
					<td><input id="node_typeId" type="text" name="typeId"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeNode" class="button02" href="javascript:document.getElementById('update_form').submit();">OK</a> 
			<a id="cancelChangeNode" class="button02b" href="javascript:document.getElementById('update_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="startNodesDialog">
	<div class="dialog_title_div01" id="startNodesDialogTitleDiv">Start Nodes</div>
	<div class="dialog_message_div01" id="startNodesDialogMessageDiv">Are you sure you want to start selected nodes?</div>
	<div class="dialog_main_div01">
		<input type="checkbox" id="startNodesDialog_option_clean" name="-clean" value="true"/> <label>-clean</label>
	</div>
	<div class="dialog_button_div01">
		<a id="okStartNodes" class="button02">OK</a> 
		<a id="cancelStartNodes" class="button02b">Cancel</a>
	</div>
	</dialog>

	<dialog id="startNodeDialog">
	<form id="start_node_form" method="post" action="<%=contextRoot + "/domain/nodestart"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>"> 
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" id="start_node_form_id" name="id">
		<div class="dialog_title_div01">Start Node</div>
		<div class="dialog_message_div01" id="startNodeDialogMessageDiv">Are you sure you want to start the node?</div>
		<div class="dialog_main_div01">
			<input type="checkbox" id="startNodesDialog_option_clean" name="-clean" value="true"/> <label>-clean</label>
		</div>
		<div class="dialog_button_div01">
			<a id="okStartNode" class="button02">OK</a> 
			<a id="cancelStartNode" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="stopNodeDialog">
	<form id="stop_node_form" method="post" action="<%=contextRoot + "/domain/nodestop"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>"> 
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" id="stop_node_form_id" name="id">
		<div class="dialog_title_div01">Stop Node</div>
		<div class="dialog_main_div01" id="stopNodeDialogMessageDiv">Are you sure you want to stop the node?</div>
		<div class="dialog_button_div01">
			<a id="okStopNode" class="button02">OK</a> 
			<a id="cancelStopNode" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteNodeDialog">
	<div class="dialog_title_div01">Delete Node</div>
	<div class="dialog_main_div01" id="deleteNodeDialogMessageDiv">Are you sure you want to delete the node?</div>
	<div class="dialog_button_div01">
		<a id="okDeleteNode" class="button02">OK</a> 
		<a id="cancelDeleteNode" class="button02b">Cancel</a>
	</div>
	</dialog>

	<dialog id="submitNodesDialog">
	<div class="dialog_title_div01" id="submitNodesDialogTitleDiv">Submit Nodes</div>
	<div class="dialog_main_div01" id="submitNodesDialogMessageDiv">Are you sure you want to submit selected nodes?</div>
	<div class="dialog_button_div01">
		<a id="okSubmitNodes" class="button02">OK</a> 
		<a id="cancelSubmitNodes" class="button02b">Cancel</a>
	</div>
	</dialog>

	<dialog id="programsSelectionDialog">
	<div class="dialog_title_div01">Programs</div>
	<form id="install_form" method="post" action="<%=contextRoot + "/domain/nodesprograminstall"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<div id="programsSelectionDiv" class="dialog_main_div02">
		</div>
		<div class="dialog_button_div01">
			<a id="okInstallPrograms" class="button02">OK</a> 
			<a id="cancelInstallPrograms" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="uninstallProgramsSelectionDialog">
	<div class="dialog_title_div01">Programs</div>
	<form id="uninstall_form" method="post" action="<%=contextRoot + "/domain/nodesprogramuninstall"%>" 
		data-programUninstallProviderUrl="<%=contextRoot + "/domain/nodesprogramuninstallprovider"%>" >
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<div id="uninstallProgramsSelectionDiv" class="dialog_main_div02">
		</div>
		<div class="dialog_button_div01">
			<a id="okUninstallPrograms" class="button02">OK</a> 
			<a id="cancelUninstallPrograms" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

</body>
</html>
