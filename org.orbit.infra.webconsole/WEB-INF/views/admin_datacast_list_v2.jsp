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
			<a class="button02" href="<%=contextRoot + "/admin/datacastlist"%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="100">JVM</th>
				<th class="th1" width="100">Id</th>
				<th class="th1" width="100">Name</th>
				<th class="th1" width="200">URL</th>
				<th class="th1" width="100">Status</th>
				<th class="th1" width="200">Metadata</th>
				<th class="th1" width="100">Action</th>
			</tr>
			<%
				// if (dataCastIndexItems.isEmpty()) {
				if (configElements.length == 0) {
			%>
			<tr>
				<td colspan="7">(n/a)</td>
			</tr>
			<%
				} else {
					// for (IndexItem dataCastIndexItem : dataCastIndexItems) {
					for (IConfigElement configElement : configElements) {
						IndexItem dataCastIndexItem = configElement.getAdapter(IndexItem.class);

						String dataCastId = "";
						String name = "";
						String dataCastServiceUrl = "";
						boolean isOnline = false;
						String metadataStr = "";
						String propStr = "";
						String jvmName = "";

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
						}

						// DataCastServiceMetadata serviceMetadata = dataCastIdToServiceMetadata.get(dataCastId);
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
				<td class="td1"><%=jvmName%></td>
				<td class="td1"><%=dataCastId%></td>
				<td class="td1"><%=name%></td>
				<td class="td1"><%=dataCastServiceUrl%></td>
				<td class="td1" width="100"><font color="<%=statusColor%>"><%=statusText%></font></td>
				<td class="td2"><%=metadataStr%></td>
				<td class="td1"><a class="action01" href="<%=contextRoot%>/admin/datatubelist?dataCastId=<%=dataCastId%>">Data Tubes</a></td>
			</tr>
			<%
					} // loop
				}
			%>
		</table>
	</div>
	<br />
</body>
</html>
