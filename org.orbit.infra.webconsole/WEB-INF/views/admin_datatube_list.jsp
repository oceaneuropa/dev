<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.service.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.infra.api.*"%>
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

	IConfigElement[] configElements = (IConfigElement[]) request.getAttribute("configElements");
	if (configElements == null) {
		configElements = new IConfigElement[] {};
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Data Cast</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/admin_datatube_list.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/admin/datacastlist">Data Cast Nodes</a> >
		<%=dataCastName%>
	</div>

	<div class="main_div01">
		<h2>Data Tube Nodes</h2>
		<div class="top_tools_div01">
			<a id="actionAddNode" class="button02">Create</a>
			<a id="actionEnableNodes" class="button02" onClick="onNodeAction('enable', '<%=contextRoot + "/admin/datatubeaction?dataCastId=" + dataCastId%>')">Enable</a> 
			<a id="actionDisableNodes" class="button02" onClick="onNodeAction('disable', '<%=contextRoot + "/admin/datatubeaction?dataCastId=" + dataCastId%>')">Disable</a>
			<a id="actionDeleteNodes" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/admin/datatubelist?dataCastId=" + dataCastId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post">
			<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
			<input id ="main_list__action" type="hidden" name="action" value="">
			<tr>
				<th class="th1" width="20">
					<input type="checkbox" onClick="toggleSelection(this, 'elementId')" />
				</th>
				<th class="th1" width="100">Name</th>
				<th class="th1" width="100">Data Tube Id</th>
				<th class="th1" width="60">Enabled</th>
				<th class="th1" width="60">Status</th>
				<th class="th1" width="120">Metadata</th>
				<th class="th1" width="120">Action</th>
			</tr>
			<%
				if (configElements.length == 0) {
			%>
			<tr>
				<td colspan="7">(n/a)</td>
			</tr>
			<%
				} else {
					for (IConfigElement configElement : configElements) {
						String elementId = configElement.getElementId();
						boolean enabled = configElement.getAttribute("enabled", Boolean.class);
						String dataTubeId = configElement.getAttribute(InfraConstants.IDX_PROP__DATATUBE__ID, String.class);

						String name = "";
						String dataTubeServiceUrl = "";
						boolean isOnline = false;
						String metadataStr = "";
						String propStr = "";
						String jvmName = "";

						IndexItem dataTubeIndexItem = configElement.getAdapter(IndexItem.class);
						if (dataTubeIndexItem != null) {
							name = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__NAME);
							dataTubeServiceUrl = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__BASE_URL);
							isOnline = IndexItemHelper.INSTANCE.isOnline(dataTubeIndexItem);

							metadataStr += "base_url = " + dataTubeServiceUrl + "<br/>";

							// config name overrides index setting
							if (configElement.getName() != null) {
								name = configElement.getName();
							}
						} else {
							name = configElement.getName();
						}

						DataTubeServiceMetadata serviceMetadata = configElement.getAdapter(DataTubeServiceMetadata.class);
						if (serviceMetadata != null) {
							long currServerTime = serviceMetadata.getServerTime();
							String currServerTimeStr = DateUtil.toString(DateUtil.toDate(currServerTime), DateUtil.SIMPLE_DATE_FORMAT2);
							jvmName = serviceMetadata.getJvmName();

							String currDataCastId = serviceMetadata.getDataCastId();
							Map<String, Object> metadataProperties = serviceMetadata.getProperties();

							String currName = serviceMetadata.getName();
							String currHostUrl = serviceMetadata.getHostURL();
							String currContextRoot = serviceMetadata.getContextRoot();

							metadataStr += "JVM = " + jvmName + "<br/>";
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

						String statusText = isOnline ? "Online" : "Offline";
						String statusColor = isOnline ? "#2eb82e" : "#cccccc";
						String enabledStr = enabled ? "true" : "false";
			%>
			<tr>
				<td class="td1">
					<input type="checkbox" name="elementId" value="<%=elementId%>">
				</td>
				<td class="td1"><%=name%></td>
				<td class="td1"><%=dataTubeId%></td>
				<td class="td1"><%=enabledStr%></td>
				<td class="td1">
					<font color="<%=statusColor%>"><%=statusText%></font>
				</td>
				<td class="td2"><%=metadataStr%></td>
				<td class="td1">
					<a class="action01" href="javascript:changeDataTubeNode('<%=elementId%>', '<%=dataTubeId%>', '<%=name%>', <%=enabled%>)">Edit</a>
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
					<td>
						<input name="enabled" type="radio" value="true" checked> <label>true</label> 
						<input name="enabled" type="radio" value="false" > <label>false</label> 
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okAddNode" class="button02">OK</a> 
			<a id="cancelAddNode" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changeNodeDialog">
	<div class="dialog_title_div01">Change Node</div>
	<form id="update_form" name="update_form_name" method="post" action="<%=contextRoot + "/admin/datatubeupdate"%>">
		<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
		<input type="hidden" id="node__elementId" name="elementId" >
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
					<td>
						<input name="enabled" type="radio" value="true"> <label>true</label> 
						<input name="enabled" type="radio" value="false"> <label>false</label> 
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeNode" class="button02">OK</a> 
			<a id="cancelChangeNode" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteNodesDialog">
	<form id="delete_form" method="post" action="<%=contextRoot + "/admin/datatubedelete"%>">
		<input type="hidden" name="dataCastId" value="<%=dataCastId%>">
		<div class="dialog_title_div01">Delete Data Tube Nodes</div>
		<div class="dialog_main_div01" id="deleteNodesDialogMessageDiv">Are you sure you want to delete selected data tube nodes?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteNodes" class="button02">OK</a>
			<a id="cancelDeleteNodes" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="nodeActionDialog">
		<div class="dialog_title_div01" id="nodeActionDialogTitleDiv" >{Action} Nodes</div>
		<div class="dialog_main_div01" id="nodeActionDialogMessageDiv">Are you sure you want to {action} the nodes?</div>
		<div class="dialog_button_div01">
			<a id="okNodeAction" class="button02">OK</a> 
			<a id="cancelNodeAction" class="button02b">Cancel</a>
		</div>
	</dialog>

</body>
</html>
