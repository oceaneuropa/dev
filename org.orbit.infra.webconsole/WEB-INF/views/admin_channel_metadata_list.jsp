<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.service.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.origin.common.model.*"%>
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

	String dataCastName = null;
	IConfigElement dataCastConfigElement = (IConfigElement) request.getAttribute("dataCastConfigElement");
	if (dataCastConfigElement != null) {
		String name = (String) dataCastConfigElement.getName();
		if (name != null) {
			dataCastName = name;
		} else {
			dataCastName = dataCastId;
		}
	}

	ChannelMetadata[] channelMetadatas = (ChannelMetadata[]) request.getAttribute("channelMetadatas");
	if (channelMetadatas == null) {
		channelMetadatas = new ChannelMetadata[] {};
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Data Cast</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/admin_channel_metadata_list.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/admin/datacastlist">Data Cast Nodes</a> >
		<%=dataCastName%>
	</div>

	<div class="main_div01">
		<h2>Channel Metadatas</h2>
		<div class="top_tools_div01">
			<a id="actionCreateChannel" class="button02">Create</a>
			<a id="actionStartChannels" class="button02" onClick="onChannelAction('start', '<%=contextRoot + "/admin/channelmetadataaction?dataCastId=" + dataCastId%>')">Start</a>
			<a id="actionSuspendChannels" class="button02" onClick="onChannelAction('suspend', '<%=contextRoot + "/admin/channelmetadataaction?dataCastId=" + dataCastId%>')">Suspend</a> 
			<a id="actionStopChannels" class="button02" onClick="onChannelAction('stop', '<%=contextRoot + "/admin/channelmetadataaction?dataCastId=" + dataCastId%>')">Stop</a>
			<a id="actionDeleteChannels" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/admin/channelmetadatalist?dataCastId=" + dataCastId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post">
			<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
			<input id ="main_list__action" type="hidden" name="action" value="">
			<tr>
				<th class="th1" width="20">
					<input type="checkbox" onClick="toggleSelection(this, 'channelId')" />
				</th>
				<th class="th1" width="80">Name</th>
				<th class="th1" width="80">Data Tube Id</th>
				<th class="th1" width="60">Status</th>
				<th class="th1" width="60">Runtime Channel</th>
				<th class="th1" width="60">Access Type</th>
				<th class="th1" width="60">Access Code</th>
				<th class="th1" width="100">Owner AccountId</th>
				<th class="th1" width="150">Metadata</th>
				<th class="th1" width="60">Action</th>
			</tr>
			<%
				if (channelMetadatas.length == 0) {
			%>
			<tr>
				<td colspan="10">(n/a)</td>
			</tr>
			<%
				} else {
					for (ChannelMetadata channelMetadata : channelMetadatas) {
						String dataTubeId = channelMetadata.getDataTubeId();
						String channelId = channelMetadata.getChannelId();
						String name = channelMetadata.getName();
						String accessType = channelMetadata.getAccessType();
						String accessCode = channelMetadata.getAccessCode();
						String ownerAccountId = channelMetadata.getOwnerAccountId();
						List<AccountConfig> accountConfigs = channelMetadata.getAccountConfigs();
						ChannelStatus channelStatus = channelMetadata.getStatus();

						int channelStatusMode = channelStatus.getMode();
						String channelStatusStr = "";
						if (channelStatus.isStarted()) {
							channelStatusStr = "Started";
						} else if (channelStatus.isSuspended()) {
							channelStatusStr = "Suspended";
						} else if (channelStatus.isStopped()) {
							channelStatusStr = "Stopped";
						}

						String metadataStr = "";
						String propStr = "";
						// String jvmName = "";

						DataTubeServiceMetadata serviceMetadata = channelMetadata.getAdapter(DataTubeServiceMetadata.class);
						if (serviceMetadata != null) {
							long currServerTime = serviceMetadata.getServerTime();
							String currServerTimeStr = DateUtil.toString(DateUtil.toDate(currServerTime), DateUtil.SIMPLE_DATE_FORMAT2);
							// jvmName = serviceMetadata.getJvmName();

							String currDataCastId = serviceMetadata.getDataCastId();
							Map<String, Object> metadataProperties = serviceMetadata.getProperties();

							String currName = serviceMetadata.getName();
							String currHostUrl = serviceMetadata.getHostURL();
							String currContextRoot = serviceMetadata.getContextRoot();

							metadataStr += "server_time = " + currServerTimeStr + "<br/>";

							if (!metadataProperties.isEmpty()) {
								for (Iterator<String> propItor = metadataProperties.keySet().iterator(); propItor.hasNext();) {
									String propName = propItor.next();
									Object propValue = serviceMetadata.getProperty(propName);

									if (propValue != null) {
										if ("server_time".equals(propName)) {
											propValue = DateUtil.toString(DateUtil.toDate(Long.valueOf(propValue.toString())), DateUtil.SIMPLE_DATE_FORMAT2);
										}
										propStr += propName + " = " + propValue + "<br/>";
									}
								}
							}
						}

						RuntimeChannel runtimeChannel = channelMetadata.getAdapter(RuntimeChannel.class);
						if (runtimeChannel != null) {

						}

						String dataTubeStatusText = (serviceMetadata != null) ? "Online" : "Offline";
						String dataTubeStatusColor = (serviceMetadata != null) ? "#2eb82e" : "#cccccc";

						String runtimeChannelStatusText = (runtimeChannel != null) ? "exists" : "not exist";
						String runtimeChannelStatusColor = (runtimeChannel != null) ? "#2eb82e" : "#cccccc";
			%>
			<tr>
				<td class="td1">
					<input type="checkbox" name="channelId" value="<%=channelId%>">
				</td>
				<td class="td1"><%=name%></td>
				<td class="td1"><%=dataTubeId%> <font color="<%=dataTubeStatusColor%>">(<%=dataTubeStatusText%>)</font></td>
				<td class="td1"><%=channelStatusStr%></td>
				<td class="td1"><font color="<%=runtimeChannelStatusColor%>"><%=runtimeChannelStatusText%></td>
				<td class="td1"><%=accessType%></td>
				<td class="td1"><%=accessCode%></td>
				<td class="td1"><%=ownerAccountId%></td>
				<td class="td2"><%=metadataStr%></td>
				<td class="td1">
					<a class="action01" href="javascript:changeChannelMetadata('<%=channelId%>', <%=name%>', <%=channelStatusMode%>)">Edit</a>
				</td>
			</tr>
			<%
					} // loop
				}
			%>
			</form>
		</table>
	</div>
	<br/>

	<dialog id="newChannelDialog">
	<div class="dialog_title_div01">Create Channel Metadata</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/admin/channelmetadataadd"%>">
		<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<td width="25%">Data Tube Id:</td>
					<td width="75%"><input type="text" id="new__data_tube_id" name="data_tube_id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Access Type:</td>
					<td>
						<input name="access_type" type="radio" value="public" checked> <label>public</label> 
						<input name="access_type" type="radio" value="private" > <label>private</label>  
					</td>
				</tr>
				<tr>
					<td width="25%">Access Code:</td>
					<td width="75%"><input type="text" name="access_code" class="input01" size="35"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okCreateChannel" class="button02">OK</a> 
			<a id="cancelCreateChannel" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changeChannelDialog">
	<div class="dialog_title_div01">Change Channel Metadata</div>
	<form id="update_form" name="update_form_name" method="post" action="<%=contextRoot + "/admin/channelmetadataupdate"%>">
		<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input id="update__name" type="text" name="name"></td>
				</tr>
				<tr>
					<td width="25%">Data Tube Id:</td>
					<td width="75%"><input type="text" id="update__data_tube_id" name="data_tube_id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Enabled:</td>
					<td>
						<input name="enabled" type="radio" value="true"> <label>true</label> 
						<input name="enabled" type="radio" value="false"> <label>false</label> 
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeChannel" class="button02">OK</a> 
			<a id="cancelChangeChannel" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteChannelsDialog">
	<form id="delete_form" method="post" action="<%=contextRoot + "/admin/channelmetadatadelete"%>">
		<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
		<div class="dialog_title_div01">Delete Channel Metadatas</div>
		<div class="dialog_main_div01" id="deleteChannelsDialogMessageDiv">Are you sure you want to delete selected channel metadatas?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteChannels" class="button02">OK</a>
			<a id="cancelDeleteChannels" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="channelActionDialog">
		<div class="dialog_title_div01" id="channelActionDialogTitleDiv" >{Action} Channel Metadatas</div>
		<div class="dialog_main_div01" id="channelActionDialogMessageDiv">Are you sure you want to {action} the channel metadatas?</div>
		<div class="dialog_button_div01">
			<a id="okChannelAction" class="button02">OK</a> 
			<a id="cancelChannelAction" class="button02b">Cancel</a>
		</div>
	</dialog>

</body>
</html>
