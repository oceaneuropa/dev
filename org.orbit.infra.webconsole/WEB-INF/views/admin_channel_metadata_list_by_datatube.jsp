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

	String groupBy = (String) request.getAttribute("groupBy");
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

	// ChannelMetadata[] channelMetadatas = (ChannelMetadata[]) request.getAttribute("channelMetadatas");
	// if (channelMetadatas == null) {
	//	channelMetadatas = new ChannelMetadata[] {};
	// }
	Map<String, List<ChannelMetadata>> channelMetadatasMap = (Map<String, List<ChannelMetadata>>) request.getAttribute("channelMetadatasMap");
	if (channelMetadatasMap == null) {
		channelMetadatasMap = new LinkedHashMap<String, List<ChannelMetadata>>();		
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
			<a id="actionSyncChannels" class="button02" onClick="onChannelAction('sync', '<%=contextRoot + "/admin/channelmetadataaction?dataCastId=" + dataCastId%>')">Synchronize Runtime Channels</a>
			<a id="actionDeleteChannels" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/admin/channelmetadatalist?dataCastId=" + dataCastId + "&groupBy=" + groupBy%>">Refresh</a>
		</div>
		<div style="float: right;">
			(
			group by: 
			<% if ("".equals(groupBy)) { %>
				none
			<% } else { %>
				<a href="<%=contextRoot + "/admin/channelmetadatalist?dataCastId=" + dataCastId%>">none</a>
			<% } %>
			|
			<% if ("datatube".equals(groupBy)) { %>
				DataTube
			<% } else { %>
				<a href="<%=contextRoot + "/admin/channelmetadatalist?dataCastId=" + dataCastId%>&groupBy=datatube">DataTube</a>
			<% } %>
			)
		</div>
		<form id="main_list" method="post">
		<%
			for(Iterator<String> itor = channelMetadatasMap.keySet().iterator(); itor.hasNext(); ) {
				String dataTubeId = itor.next();	
				List<ChannelMetadata> channelMetadatas = channelMetadatasMap.get(dataTubeId);
				if (channelMetadatas == null) {
					channelMetadatas = new ArrayList<ChannelMetadata>();
				}
		%>
		<table class="main_table01">
			<input type="hidden" name="groupBy" value="<%=groupBy%>">
			<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
			<input id ="main_list__action" type="hidden" name="action" value="">
			<tr>
				<th class="th1" width="12">
					<input type="checkbox" onClick="toggleSelection(this, 'channelId', '<%=dataTubeId%>')" />
				</th>
				<th class="th1" width="120">Data Tube Id</th>
				<th class="th1" width="180">Channel</th>
				<th class="th1" width="150">Runtime Status</th>
				<th class="th1" width="150">Access</th>
				<th class="th1" width="120">Owner Account Id</th>
				<th class="th1" width="100">Action</th>
			</tr>
			<%
				if (channelMetadatas.isEmpty()) {
			%>
			<tr>
				<td colspan="7">(n/a)</td>
			</tr>
			<%
				} else {
					for (ChannelMetadata channelMetadata : channelMetadatas) {
						// String dataTubeId = channelMetadata.getDataTubeId();
						String channelId = channelMetadata.getChannelId();
						String name = channelMetadata.getName();
						String accessType = channelMetadata.getAccessType();
						String accessCode = channelMetadata.getAccessCode();
						String ownerAccountId = channelMetadata.getOwnerAccountId();
						List<AccountConfig> accountConfigs = channelMetadata.getAccountConfigs();
						ChannelStatus channelStatus = channelMetadata.getStatus();

						int channelStatusMode = channelStatus.getMode();

						String channelStatusStr = "";
						String channelStatusColor = "#000000";

						if (channelStatus.isStarted()) {
							channelStatusStr = "Started";
							channelStatusColor = "#2eb82e";

						} else if (channelStatus.isSuspended()) {
							channelStatusStr = "Suspended";
							channelStatusColor = "#ffbb33"; //#ffa64d //#ffcc66 //#ffd480 //#ffa31a //#ffbb33

						} else if (channelStatus.isStopped()) {
							channelStatusStr = "Stopped";
							channelStatusColor = "#cccccc";
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

						String dataTubeStatusText = (serviceMetadata != null) ? "Online" : "Offline";
						String dataTubeStatusColor = (serviceMetadata != null) ? "#2eb82e" : "#cccccc";

						String runtimeInstanceStr = "Runtime Instance";
						String runtimeInstanceColor = (runtimeChannel != null) ? "#2eb82e" : "#cccccc";
			%>
			<tr>
				<td class="td1">
					<input type="checkbox" name="channelId" data-dataTubeId="<%=dataTubeId%>" value="<%=channelId%>">
				</td>
				<td class="td1">
					<font color="<%=dataTubeStatusColor%>"><%=dataTubeId%></font>
				</td>
				<td class="td2">
					<%=channelId%> | <%=name%>
				</td>
				<td class="td1">
					<font color="<%=runtimeInstanceColor%>"><%=runtimeInstanceStr%></font> | 
					<font color="<%=channelStatusColor%>"><%=channelStatusStr%></font>
				</td>
				<td class="td1">
					<%=accessType%> | <%=accessCode%>
				</td>
				<td class="td1">
					<%=ownerAccountId%>
				</td>
				<td class="td1">
					<a class="action01" href="javascript:changeChannelMetadata('<%=channelId%>', '<%=dataTubeId%>', '<%=name%>', <%=channelStatusMode%>, '<%=accessType%>', '<%=accessCode%>', '<%=ownerAccountId%>')">Edit</a>
				</td>
			</tr>
			<%
					} // loop
				}
			%>
		</table>
			<% if (itor.hasNext()) { %>
				</br>
			<% } %>
		<%
			}
		%>
		</form>
	</div>
	<br/>

	<dialog id="newChannelDialog">
	<div class="dialog_title_div01">Create Channel Metadata</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/admin/channelmetadataadd"%>">
		<input type="hidden" name="groupBy" value="<%=groupBy%>">
		<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="30%">Data Tube Id:</td>
					<td width="70%">
						<input type="text" id="new__data_tube_id" name="data_tube_id" class="input01" size="35">
					</td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<td>Access Type:</td>
					<td>
						<input name="access_type" type="radio" value="public" checked> <label>public</label> 
						<input name="access_type" type="radio" value="private" > <label>private</label>  
					</td>
				</tr>
				<tr>
					<td>Access Code:</td>
					<td>
						<input type="text" name="access_code" class="input01" size="35">
					</td>
				</tr>
				<tr>
					<td>Owner Account Id:</td>
					<td>
						<input type="text" name="owner_account_id" class="input01" size="35">
					</td>
				</tr>
				<tr>
					<td></td>
					<td colspan="2"><input type="checkbox" name="-start" value="true"/> <label>-start</label></td>
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
		<input type="hidden" name="groupBy" value="<%=groupBy%>">
		<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
		<input type="hidden" id="update__channel_id" name="channel_id" value="">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="30%">Data Tube Id:</td>
					<td width="70%">
						<input type="text" id="update__data_tube_id" name="data_tube_id" class="input01" size="35">
					</td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input id="update__name" type="text" name="name"></td>
				</tr>
				<tr>
					<td>Access Type:</td>
					<td>
						<input name="access_type" type="radio" value="public"> <label>public</label> 
						<input name="access_type" type="radio" value="private" > <label>private</label>  
					</td>
				</tr>
				<tr>
					<td>Access Code:</td>
					<td>
						<input type="text" id="update__access_code" name="access_code" class="input01" size="35">
					</td>
				</tr>
				<tr>
					<td>Owner Account Id:</td>
					<td>
						<input type="text" id="update__owner_account_id" name="owner_account_id" class="input01" size="35">
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
		<input type="hidden" name="groupBy" value="<%=groupBy%>">
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
