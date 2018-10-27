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
		<h2>Data Cast Services</h2>
		<div class="top_tools_div01">
			<a id="actionAddNode" class="button02">Create</a>
			<a id="actionDeleteNodes" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/admin/datacastlist"%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post">
			<tr>
				<th class="th1" width="20">
					<input type="checkbox" onClick="toggleSelection(this, 'elementId')" />
				</th>
				<th class="th1" width="100">JVM</th>
				<th class="th1" width="100">Data Cast Id</th>
				<th class="th1" width="100">Name</th>
				<th class="th1" width="200">URL</th>
				<th class="th1" width="100">Enabled</th>
				<th class="th1" width="100">Status</th>
				<th class="th1" width="200">Metadata</th>
				<th class="th1" width="150">Action</th>
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

						IndexItem dataCastIndexItem = configElement.getAdapter(IndexItem.class);

						String dataCastId = "";
						String name = "";
						String dataCastServiceUrl = "";
						boolean isOnline = false;
						String metadataStr = "";
						String propStr = "";
						String jvmName = "";
						boolean enabled = false;

						if (dataCastIndexItem != null) {
							dataCastId = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__ID);
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
							dataCastId = configElement.getAttribute(InfraConstants.IDX_PROP__DATACAST__ID, String.class);
							name = configElement.getName();
							enabled = configElement.getAttribute("enabled", Boolean.class);
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

							if (false && !metadataProperties.isEmpty()) {
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
			%>
			<tr>
				<td class="td1"><input type="checkbox" name="elementId" value="<%=elementId%>"></td>
				<td class="td1"><%=jvmName%></td>
				<td class="td1"><%=dataCastId%></td>
				<td class="td1"><%=name%></td>
				<td class="td1"><%=dataCastServiceUrl%></td>
				<td class="td1"><%=enabled%></td>
				<td class="td1" width="100"><font color="<%=statusColor%>"><%=statusText%></font></td>
				<td class="td2"><%=metadataStr%></td>
				<td class="td1">
					<a class="action01" href="javascript:changeDataCastNode('<%=elementId%>', '<%=dataCastId%>', '<%=name%>', '<%=enabled%>')">Edit</a>
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
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" name="data_cast_id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
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
	<div class="dialog_title_div01">Change Data Cast Node</div>
	<form id="update_form" method="post" action="<%=contextRoot + "/admin/datacastupdate"%>">
		<input type="hidden" id="node__elementId" name="elementId" >
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Data Cast Id:</td>
					<td width="75%"><input type="text" id="node__data_cast_id" name="data_cast_id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input id="node__name" type="text" name="name"></td>
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

</body>
</html>
