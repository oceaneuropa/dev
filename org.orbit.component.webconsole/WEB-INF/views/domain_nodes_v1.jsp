<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.component.api.tier3.domainmanagement.*"%>
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
<title>Domain Management</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
<script type="text/javascript" src="<%=contextRoot + "/views/js/domain_nodes.js"%>" defer></script>
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> > 
		<a href="<%=contextRoot%>/domain/platforms?machineId=<%=machineId%>"><%=machineName%></a> >
		<!-- <a href="<%=contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId%>"><%=platformName%></a> -->
		<%=platformName%>
	</div>
	<div class="main_div01">
		<h2>Nodes</h2>
		<div class="top_tools_div01">
			<a id="action.addNode" class="button02">Create</a>
			<a id="action.deleteNodes" targetFormId="main_list" targetFormUrl="<%=contextRoot + "/domain/nodedelete"%>" class="button02">Delete</a>
			<a id="action.startNodes" targetFormId="main_list" targetFormUrl="<%=contextRoot + "/domain/nodestart"%>" class="button02">Start</a> 
			<a id="action.stopNodes" targetFormId="main_list" targetFormUrl="<%=contextRoot + "/domain/nodestop"%>" class="button02">Stop</a> 
			<a class="button02" href="<%=contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<tr>
					<th class="th1" width="15">
						<input type="checkbox" onClick="toggleSelection(this, 'id')" />
					</th>
					<th class="th1" width="50">Id</th>
					<th class="th1" width="50">Name</th>
					<th class="th1" width="50">Host URL</th>
					<th class="th1" width="50">Context Root</th>
					<th class="th1" width="100">Platform Home</th>
					<th class="th1" width="50">Status</th>
					<th class="th1" width="120">Actions</th>
				</tr>
				<%
					if (nodeInfos.length == 0) {
				%>
				<tr>
					<td colspan="8">(n/a)</td>
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

							String hostURL = null;
							String currContextRoot = null;
							String platformHome = null;
							IndexItem indexItem = nodeIdToIndexItemMap.get(id);
							if (indexItem != null) {
								hostURL = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_HOST_URL);
								currContextRoot = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_CONTEXT_ROOT);
								platformHome = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_HOME);
							}

							id = StringUtil.get(id);
							name = StringUtil.get(name);
							hostURL = StringUtil.get(hostURL, "");
							currContextRoot = StringUtil.get(currContextRoot, "");
							platformHome = StringUtil.get(platformHome, "");
				%>
				<tr>
					<td class="td1"><input type="checkbox" name="id" value="<%=id%>"></td>
					<td class="td1"><%=id%></td>
					<td class="td2"><%=name%></td>
					<td class="td2"><%=hostURL%></td>
					<td class="td2"><%=currContextRoot%></td>
					<td class="td2"><%=platformHome%></td>
					<td class="td1"><font color="<%=statusColor%>"><%=statusStr1%></font></td>
					<td class="td1">
						<a class="action01" href="javascript:changeNode('<%=id%>', '<%=name%>', '<%=typeId%>')">Change</a> |
						<a class="action01" href="<%=contextRoot%>/domain/nodeproperties?machineId=<%=machineId%>&platformId=<%=platformId%>&id=<%=id%>">Properties</a>
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

	<dialog id="startNodeDialog">
	<div class="dialog_title_div01">Start Node</div>
	<div class="dialog_main_div01" id="startNodeDialogMessageDiv">Are you sure you want to start the node?</div>
	<div class="dialog_button_div01">
		<a id="okStartNode" class="button02">OK</a> 
		<a id="cancelStartNode" class="button02b">Cancel</a>
	</div>
	</dialog>

	<dialog id="stopNodeDialog">
	<div class="dialog_title_div01">Stop Node</div>
	<div class="dialog_main_div01" id="stopNodeDialogMessageDiv">Are you sure you want to stop the node?</div>
	<div class="dialog_button_div01">
		<a id="okStopNode" class="button02">OK</a> 
		<a id="cancelStopNode" class="button02b">Cancel</a>
	</div>
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

</body>
</html>
