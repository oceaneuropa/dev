<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.service.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.infra.api.*"%>
<%@ page import="org.orbit.infra.api.datacast.*"%>
<%@ page import="org.orbit.infra.api.datatube.*"%>
<%@ page import="org.orbit.infra.io.*"%>
<%@ page import="org.orbit.infra.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

	String dataCastId = (String) request.getAttribute("dataCastId");
	String dataTubeId = (String) request.getAttribute("dataTubeId");
	DataCastServiceMetadata dataCastServiceMetadata = (DataCastServiceMetadata) request.getAttribute("dataCastServiceMetadata");
	DataTubeServiceMetadata dataTubeServiceMetadata = (DataTubeServiceMetadata) request.getAttribute("dataTubeServiceMetadata");
	IConfigElement dataCastConfigElement = (IConfigElement) request.getAttribute("dataCastConfigElement");
	IConfigElement dataTubeConfigElement = (IConfigElement) request.getAttribute("dataTubeConfigElement");
	RuntimeChannel[] runtimeChannels = (RuntimeChannel[]) request.getAttribute("runtimeChannels");

	String dataCastName = null;
	if (dataCastConfigElement != null) {
		String name = (String) dataCastConfigElement.getName();
		if (name != null) {
			dataCastName = name;
		}
	}
	if (dataCastName == null) {
		dataCastName = dataCastId;
	}

	String dataTubeName = null;
	if (dataTubeConfigElement != null) {
		String name = (String) dataTubeConfigElement.getName();
		if (name != null) {
			dataTubeName = name;
		}
	}
	if (dataTubeName == null) {
		dataTubeName = dataTubeId;
	}

	if (runtimeChannels == null) {
		runtimeChannels = new RuntimeChannel[] {};
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Data Cast</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/admin_runtime_channel_list.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/admin/datacastlist">Data Cast Nodes</a> >
		<a href="<%=contextRoot%>/admin/datatubelist?dataCastId=<%=dataCastId%>">Data Tube Nodes</a> (<%=dataCastName%>) >
		<%=dataTubeName%>
	</div>

	<div class="main_div01">
		<h2>Runtime Channels</h2>
		<div class="top_tools_div01">
			<a id="actionAddRuntimeChannel" class="button02">Create</a> 
			<a id="actionStartRuntimeChannels" class="button02" onClick="onRuntimeChannelAction('start', '<%=contextRoot + "/admin/runtimechannelaction?dataCastId=" + dataCastId + "&dataTubeId=" + dataTubeId%>')">Start</a> 
			<a id="actionSuspendRuntimeChannels" class="button02" onClick="onRuntimeChannelAction('suspend', '<%=contextRoot + "/admin/runtimechannelaction?dataCastId=" + dataCastId + "&dataTubeId=" + dataTubeId%>')">Suspend</a> 
			<a id="actionStopRuntimeChannels" class="button02" onClick="onRuntimeChannelAction('stop', '<%=contextRoot + "/admin/runtimechannel?dataCastId=" + dataCastId + "&dataTubeId=" + dataTubeId%>')">Stop</a> 
			<a id="actionDeleteRuntimeChannels" class="button02">Delete</a> 
			<a class="button02" href="<%=contextRoot + "/admin/runtimechannellist?dataCastId=" + dataCastId + "&dataTubeId=" + dataTubeId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post">
				<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
				<input type="hidden" name="dataTubeId" value="<%=dataTubeId%>">
				<input id="main_list__action" type="hidden" name="action" value="">
				<tr>
					<th class="th1" width="20"><input type="checkbox" onClick="toggleSelection(this, 'channelId')" /></th>
					<th class="th1" width="150">Channel</th>
					<th class="th1" width="100">Channel Status</th>
					<th class="th1" width="150">Channel Metadata</th>
					<th class="th1" width="150">Owner Account Id</th>
					<th class="th1" width="100">Action</th>
				</tr>
				<%
					if (runtimeChannels.length == 0) {
				%>
				<tr>
					<td colspan="6">(n/a)</td>
				</tr>
				<%
					} else {
						for (RuntimeChannel runtimeChannel : runtimeChannels) {
							String channelId = runtimeChannel.getChannelId();

							String name = runtimeChannel.getName();
							String nameColor = "";

							String channelStatusStr = "";
							String channelStatusColor = "";

							String channelMetadataStr = "";
							String ownerAccountId = "";

							ChannelMetadata channelMetadata = runtimeChannel.getAdapter(ChannelMetadata.class);
							if (channelMetadata != null) {
								String currDataCastId = channelMetadata.getDataCastId();
								String currDataTubeId = channelMetadata.getDataTubeId();
								String currChannelId = channelMetadata.getChannelId();
								String currAccessType = channelMetadata.getAccessType();
								String currAccessCode = channelMetadata.getAccessCode();
								ownerAccountId = channelMetadata.getOwnerAccountId();
								ChannelStatus channelStatus = channelMetadata.getStatus();

								nameColor = "#000000";

								if (currAccessType != null && !currAccessType.isEmpty()) {
									channelMetadataStr += currAccessType;
								}
								if (currAccessCode != null && !currAccessCode.isEmpty()) {
									if (!channelMetadataStr.isEmpty()) {
										channelMetadataStr += " | ";
									}
									channelMetadataStr += currAccessCode;
								}

								if (channelStatus != null) {
									if (channelStatus.contains(ChannelStatus.STARTED)) {
										channelStatusStr = "Started";
										channelStatusColor = "#2eb82e"; // green

									} else if (channelStatus.contains(ChannelStatus.SUSPENDED)) {
										channelStatusStr = "Suspend";
										channelStatusColor = "#ffcc00"; // yellow

									} else if (channelStatus.contains(ChannelStatus.STOPPED)) {
										channelStatusStr = "Stopped";
										channelStatusColor = "#cccccc"; // grey
									}
								}
							} else {
								nameColor = "#cccccc";
							}

							// String statusText = isOnline ? "Online" : "Offline";
							// String statusColor = isOnline ? "#2eb82e" : "#cccccc";
				%>
				<tr>
					<td class="td1"><input type="checkbox" name="channelId" value="<%=channelId%>"></td>
					<td class="td2"><%=channelId%> | <%=name%></td>
					<td class="td1"><font color="<%=channelStatusColor%>"><%=channelStatusStr%></font></td>
					<td class="td2"><%=channelMetadataStr%></td>
					<td class="td1"><%=ownerAccountId%></td>
					<td class="td1">
						<a class="action01" href="javascript:changeRuntimeChannel('<%=dataCastId%>', '<%=dataTubeId%>', '<%=channelId%>', '<%=name%>')">Edit</a>
					</td>
				</tr>
				<%
						} // for loop
					}
				%>
			</form>
		</table>
	</div>
	<br />

	<dialog id="newNodeDialog">
	<div class="dialog_title_div01">Create Node</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/admin/datatubeadd"%>">
		<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<td width="25%">Data Tube Id:</td>
					<td width="75%"><input type="text" name="data_tube_id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Enabled:</td>
					<td><input name="enabled" type="radio" value="true" checked> <label>true</label> <input name="enabled" type="radio" value="false"> <label>false</label></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okAddNode" class="button02">OK</a> <a id="cancelAddNode" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changeNodeDialog">
	<div class="dialog_title_div01">Change Node</div>
	<form id="update_form" name="update_form_name" method="post" action="<%=contextRoot + "/admin/datatubeupdate"%>">
		<input type="hidden" name="dataCastId" value="<%=dataCastId%>"> <input type="hidden" id="node__elementId" name="elementId">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input id="node__name" type="text" name="name"></td>
				</tr>
				<tr>
					<td width="25%">Data Tube Id:</td>
					<td width="75%"><input type="text" id="node__data_tube_id" name="data_tube_id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Enabled:</td>
					<td><input name="enabled" type="radio" value="true"> <label>true</label> <input name="enabled" type="radio" value="false"> <label>false</label></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeNode" class="button02">OK</a> <a id="cancelChangeNode" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteNodesDialog">
	<form id="delete_form" method="post" action="<%=contextRoot + "/admin/datatubedelete"%>">
		<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
		<div class="dialog_title_div01">Delete Data Tube Nodes</div>
		<div class="dialog_main_div01" id="deleteNodesDialogMessageDiv">Are you sure you want to delete selected data tube nodes?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteNodes" class="button02">OK</a> <a id="cancelDeleteNodes" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="nodeActionDialog">
	<div class="dialog_title_div01" id="nodeActionDialogTitleDiv">{Action} Nodes</div>
	<div class="dialog_main_div01" id="nodeActionDialogMessageDiv">Are you sure you want to {action} the nodes?</div>
	<div class="dialog_button_div01">
		<a id="okNodeAction" class="button02">OK</a> <a id="cancelNodeAction" class="button02b">Cancel</a>
	</div>
	</dialog>

</body>
</html>
