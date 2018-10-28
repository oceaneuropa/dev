<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.service.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.infra.api.*"%>
<%@ page import="org.orbit.infra.api.datacast.*"%>
<%@ page import="org.orbit.infra.io.*"%>
<%@ page import="org.orbit.infra.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

	IConfigElement[] configElements = (IConfigElement[]) request.getAttribute("configElements");
	if (configElements == null) {
		configElements = new IConfigElement[] {};
	}

	// List<IndexItem> dataCastIndexItems = (List<IndexItem>) request.getAttribute("dataCastIndexItems");
	// Map<String, DataCastServiceMetadata> dataCastIdToServiceMetadata = (Map<String, DataCastServiceMetadata>) request.getAttribute("dataCastIdToServiceMetadata");
	// if (dataCastIndexItems == null) {
	// 	dataCastIndexItems = new ArrayList<IndexItem>();
	// }
	// if (dataCastIdToServiceMetadata == null) {
	// 	dataCastIdToServiceMetadata = new HashMap<String, DataCastServiceMetadata>();
	// }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Data Cast</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/admin_datacast_list.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="main_div01">
		<h2>Data Cast Nodes</h2>
		<div class="top_tools_div01">
			<a id="actionAddNode" class="button02">Create</a>
			<a id="actionEnableNodes" class="button02" onClick="onNodeAction('enable', '<%=contextRoot + "/admin/datacastaction"%>')">Enable</a> 
			<a id="actionDisableNodes" class="button02" onClick="onNodeAction('disable', '<%=contextRoot + "/admin/datacastaction"%>')">Disable</a>
			<a id="actionDeleteNodes" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/admin/datacastlist"%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post">
			<input id ="main_list__action" type="hidden" name="action" value="">
			<tr>
				<th class="th1" width="20">
					<input type="checkbox" onClick="toggleSelection(this, 'elementId')" />
				</th>
				<th class="th1" width="120">JVM</th>
				<th class="th1" width="100">Name</th>
				<th class="th1" width="100">Data Cast Id</th>
				<th class="th1" width="50">Enabled</th>
				<th class="th1" width="180">URL</th>
				<th class="th1" width="50">Status</th>
				<th class="th1" width="180">Metadata</th>
				<th class="th1" width="100">Action</th>
			</tr>
			<%
				if (configElements.length == 0) {
			%>
			<tr>
				<td colspan="9">(n/a)</td>
			</tr>
			<%
				} else {
					for (IConfigElement configElement : configElements) {
						String elementId = configElement.getElementId();
						boolean enabled = configElement.getAttribute("enabled", Boolean.class);
						String dataCastId = configElement.getAttribute(InfraConstants.IDX_PROP__DATACAST__ID, String.class);

						String name = "";
						String dataCastServiceUrl = "";
						boolean isOnline = false;
						String metadataStr = "";
						String propStr = "";
						String jvmName = "";

						IndexItem dataCastIndexItem = configElement.getAdapter(IndexItem.class);
						if (dataCastIndexItem != null) {
							name = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__NAME);
							// String hostUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__HOST_URL);
							// String dataCastContextRoot = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__CONTEXT_ROOT);
							// String dataCastServiceUrl = WebServiceAwareHelper.INSTANCE.getURL(hostUrl, dataCastContextRoot);
							dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__BASE_URL);
							isOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);

							// config name overrides index setting
							if (configElement.getName() != null) {
								name = configElement.getName();
							}

						} else {
							name = configElement.getName();
						}

						DataCastServiceMetadata serviceMetadata = configElement.getAdapter(DataCastServiceMetadata.class);
						if (serviceMetadata != null) {
							long currServerTime = serviceMetadata.getServerTime();
							String currServerTimeStr = DateUtil.toString(DateUtil.toDate(currServerTime), DateUtil.SIMPLE_DATE_FORMAT2);
							jvmName = serviceMetadata.getJvmName();

							String currDataCastId = serviceMetadata.getDataCastId();
							Map<String, Object> metadataProperties = serviceMetadata.getProperties();

							String currName = serviceMetadata.getName();
							String currHostUrl = serviceMetadata.getHostURL();
							String currContextRoot = serviceMetadata.getContextRoot();

							// metadataStr += "datacast_id = " + currDataCastId + "<br/>";
							// metadataStr += "name = " + currName + "<br/>";
							// metadataStr += "host_url = " + currHostUrl + "<br/>";
							// metadataStr += "context_root = " + currContextRoot + "<br/>";
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
				<td class="td1"><%=jvmName%></td>
				<td class="td1"><%=name%></td>
				<td class="td1"><%=dataCastId%></td>
				<td class="td1"><%=enabledStr%></td>
				<td class="td2"><%=dataCastServiceUrl%></td>
				<td class="td1">
					<font color="<%=statusColor%>"><%=statusText%></font>
				</td>
				<td class="td2"><%=metadataStr%></td>
				<td class="td1">
					<a class="action01" href="javascript:changeDataCastNode('<%=elementId%>', '<%=dataCastId%>', '<%=name%>', <%=enabled%>)">Edit</a>
					<a class="action01" href="<%=contextRoot%>/admin/datatubelist?dataCastId=<%=dataCastId%>">Data Tubes</a>
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
	<form id="new_form" method="post" action="<%=contextRoot + "/admin/datacastadd"%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<td width="25%">Data Cast Id:</td>
					<td width="75%"><input type="text" name="data_cast_id" class="input01" size="35"></td>
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
	<form id="update_form" name="update_form_name" method="post" action="<%=contextRoot + "/admin/datacastupdate"%>">
		<input type="hidden" id="node__elementId" name="elementId" >
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input id="node__name" type="text" name="name"></td>
				</tr>
				<tr>
					<td width="25%">Data Cast Id:</td>
					<td width="75%"><input type="text" id="node__data_cast_id" name="data_cast_id" class="input01" size="35"></td>
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
	<form id="delete_form" method="post" action="<%=contextRoot + "/admin/datacastdelete"%>">
		<div class="dialog_title_div01">Delete Data Cast Nodes</div>
		<div class="dialog_main_div01" id="deleteNodesDialogMessageDiv">Are you sure you want to delete selected data cast nodes?</div>
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
