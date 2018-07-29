<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.component.api.*"%>
<%@ page import="org.orbit.component.api.tier3.domainmanagement.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	MachineConfig machineConfig = (MachineConfig) request.getAttribute("machineConfig");
	PlatformConfig[] platformConfigs = (PlatformConfig[]) request.getAttribute("platformConfigs");
	Map<String, IndexItem> platformIdToIndexItemMap = (Map<String, IndexItem>) request.getAttribute("platformIdToIndexItemMap");
	Map<String, Map<String, List<IndexItem>>> platformIdToIndexerIdToIndexItemsMap = (Map<String, Map<String, List<IndexItem>>>) request.getAttribute("platformIdToIndexerIdToIndexItemsMap");

	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	if (platformConfigs == null) {
		platformConfigs = new PlatformConfig[0];
	}
	if (platformIdToIndexItemMap == null) {
		platformIdToIndexItemMap = new HashMap<String, IndexItem>();
	}
	if (platformIdToIndexerIdToIndexItemsMap == null) {
		platformIdToIndexerIdToIndexItemsMap = new HashMap<String, Map<String, List<IndexItem>>>(); 
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Domain Management</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
<script type="text/javascript" src="<%=contextRoot + "/views/js/domain_platforms.js"%>" defer></script>
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> >
		<!-- <a href="<%=contextRoot + "/domain/platforms?machineId=" + machineId%>"><%=machineName%></a> -->
		<%=machineName%>
	</div>
	<div class="main_div01">
		<h2>Platforms</h2>
		<div class="top_tools_div01">
			<!-- 
			<button onclick="addPlatform()">Add</button>
			<button onclick="deletePlatforms()">Delete</button>
			<button onclick="location.href='<%=contextRoot + "/domain/platforms?machineId=" + machineId%>'">Refresh</button>
			<a href="http://google.com" class="button6A">Go to Google</a>
			-->
			<a id="action.addPlatform" class="button02">Add</a> <a id="action.deletePlatforms" class="button02">Delete</a> <a class="button02" href="<%=contextRoot + "/domain/platforms?machineId=" + machineId%>">Refresh</a>
			<!-- <a class="button02" href="<%=contextRoot%>/domain/machines">View Machines</a> -->
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="<%=contextRoot + "/domain/platformdelete"%>">
				<input type="hidden" name="machineId" value="<%=machineId%>">
				<tr>
					<th class="th1" width="12"></th>
					<th class="th1" width="80">Id</th>
					<th class="th1" width="80">Name</th>
					<th class="th1" width="80">Host URL</th>
					<th class="th1" width="80">Context Root</th>
					<th class="th1" width="80">Platform Home</th>
					<th class="th1" width="50">Status</th>
					<th class="th1" width="180">Actions</th>
				</tr>
				<%
					if (platformConfigs.length == 0) {
				%>
				<tr>
					<td colspan="8">(n/a)</td>
				</tr>
				<%
					} else {
						for (PlatformConfig platformConfig : platformConfigs) {
							String id = platformConfig.getId();
							String name = platformConfig.getName();
							// String hostURL = platformConfig.getHostURL();
							// String currContextRoot = platformConfig.getContextRoot();

							String hostURL = null;
							String currContextRoot = null;
							String platformHome = null;
							IndexItem indexItem = platformIdToIndexItemMap.get(id);
							if (indexItem != null) {
								hostURL = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_HOST_URL);
								currContextRoot = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_CONTEXT_ROOT);
								platformHome = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_HOME);
							}

							id = StringUtil.get(id);
							name = StringUtil.get(name);
							hostURL = StringUtil.get(hostURL);
							currContextRoot = StringUtil.get(currContextRoot);
							platformHome = StringUtil.get(platformHome);

							boolean isOnline = platformConfig.getRuntimeStatus().isOnline();
							String runtimeState = platformConfig.getRuntimeStatus().getRuntimeState();
							String statusStr1 = (isOnline) ? "Online" : "Offline";
							String statusStr2 = runtimeState != null && !runtimeState.isEmpty() ? (" | " + runtimeState) : "";
							String statusColor = isOnline ? "#2eb82e" : "#cccccc";

							boolean hasNodeControl = false;
							Map<String, List<IndexItem>> indexerIdToIndexItemsMap = platformIdToIndexerIdToIndexItemsMap.get(id);
							if (indexerIdToIndexItemsMap != null) {
								List<IndexItem> nodeControlIndexItems = indexerIdToIndexItemsMap.get(IndexConstants.NODE_CONTROL_INDEXER_ID);
								if (nodeControlIndexItems != null && !nodeControlIndexItems.isEmpty()) {
									hasNodeControl = true;
								}
							}
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
						<a class="action01" href="javascript:changePlatform('<%=id%>', '<%=name%>', '<%=hostURL%>', '<%=currContextRoot%>')">Change</a> | 
						<a class="action01" href="<%=contextRoot%>/domain/platformproperties?machineId=<%=machineId%>&id=<%=id%>">Properties</a> | 
						<% if (hasNodeControl) { %>
							<a class="action01" href="<%=contextRoot%>/domain/nodes?machineId=<%=machineId%>&platformId=<%=id%>">Nodes</a> 
						<% } else {%>
							<a class="action01b">Nodes</a>
						<% }%>
						<!-- | <a class="action01" href="javascript:deletePlatform('<%=contextRoot + "/domain/platformdelete"%>', '<%=machineId%>', '<%=id%>')">Delete</a> -->
					</td>
				</tr>
				<%
						}
					}
				%>
			</form>
		</table>
	</div>

	<dialog id="newPlatformDialog">
	<div class="dialog_title_div01">Add Platform</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/domain/platformadd"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
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
					<td>Host URL:</td>
					<td><input type="text" name="hostUrl"></td>
				</tr>
				<tr>
					<td>Context Root:</td>
					<td><input type="text" name="contextRoot"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<!-- <button id="okAddPlatform" class="button02" type="submit">OK</button> -->
			<!-- <button id="cancelAddPlatform" class="button02" type="reset">Cancel</button> -->
			<a id="okAddPlatform" class="button02" href="javascript:document.getElementById('new_form').submit();">OK</a> <a id="cancelAddPlatform" class="button02b" href="javascript:document.getElementById('new_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changePlatformDialog">
	<div class="dialog_title_div01">Change Platform</div>
	<form id="update_form" method="post" action="<%=contextRoot + "/domain/platformupdate"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" id="platform_id" name="id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input id="platform_name" type="text" name="name"></td>
				</tr>
				<tr>
					<td>Host URL:</td>
					<td><input id="platform_hostUrl" type="text" name="hostUrl"></td>
				</tr>
				<tr>
					<td>Context Root:</td>
					<td><input id="platform_contextRoot" type="text" name="contextRoot"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<!-- <button id="okChangePlatform" type="submit">OK</button> -->
			<!-- <button id="cancelChangePlatform" type="reset">Cancel</button> -->
			<a id="okChangePlatform" class="button02" href="javascript:document.getElementById('update_form').submit();">OK</a> <a id="cancelChangePlatform" class="button02b" href="javascript:document.getElementById('update_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deletePlatformDialog">
	<div class="dialog_title_div01">Delete Platform</div>
	<div class="dialog_main_div01" id="deletePlatformDialogMessageDiv">Are you sure you want to delete the platform?</div>
	<div class="dialog_button_div01">
		<!-- <button id="okDeletePlatform">OK</button> -->
		<!-- <button id="cancelDeletePlatform">Cancel</button> -->
		<a id="okDeletePlatform" class="button02">OK</a> <a id="cancelDeletePlatform" class="button02b">Cancel</a>
	</div>
	</dialog>

	<dialog id="deletePlatformsDialog">
	<div class="dialog_title_div01">Delete Platform</div>
	<div class="dialog_main_div01" id="deletePlatformsDialogMessageDiv">Are you sure you want to delete selected platforms?</div>
	<div class="dialog_button_div01">
		<!-- <button id="okDeletePlatforms">OK</button> -->
		<!-- <button id="cancelDeletePlatforms">Cancel</button> -->
		<a id="okDeletePlatforms" class="button02">OK</a> <a id="cancelDeletePlatforms" class="button02b">Cancel</a>
	</div>
	</dialog>

</body>
</html>
