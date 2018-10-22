<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.service.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.infra.api.*"%>
<%@ page import="org.orbit.infra.api.datatube.*"%>
<%@ page import="org.orbit.infra.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

	String dataCastId = (String) request.getAttribute("dataCastId");

	String dataCastName = null;
	IndexItem dataCastIndexItem = (IndexItem) request.getAttribute("dataCastIndexItem");
	if (dataCastIndexItem != null) {
		dataCastName = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__NAME);
	}

	List<IndexItem> dataTubeIndexItems = (List<IndexItem>) request.getAttribute("dataTubeIndexItems");
	Map<String, DataTubeServiceMetadata> dataTubeIdToServiceMetadata = (Map<String, DataTubeServiceMetadata>) request.getAttribute("dataTubeIdToServiceMetadata");

	String dataCastLabel = (dataCastName != null) ? dataCastName : dataCastId;
	if (dataTubeIdToServiceMetadata == null) {
		dataTubeIdToServiceMetadata = new HashMap<String, DataTubeServiceMetadata>();
	}
	if (dataTubeIndexItems == null) {
		dataTubeIndexItems = new ArrayList<IndexItem>();
	}
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

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/admin/datacastlist">Data Cast Services</a> >
		<%=dataCastLabel%>
	</div>

	<div class="main_div01">
		<h2>Data Tubes</h2>
		<div class="top_tools_div01">
			<a class="button02" href="<%=contextRoot + "/admin/datatubelist?dataCastId=" + dataCastId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="100">JVM</th>
				<th class="th1" width="100">Data Tube Id</th>
				<th class="th1" width="100">Name</th>
				<th class="th1" width="200">URL</th>
				<th class="th1" width="100">Status</th>
				<th class="th1" width="250">Metadata</th>
			</tr>
			<%
				if (dataTubeIndexItems.isEmpty()) {
			%>
			<tr>
				<td colspan="6">(n/a)</td>
			</tr>
			<%
				} else {
					for (IndexItem dataTubeIndexItem : dataTubeIndexItems) {
						String theDataCastId = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__DATACAST_ID);
						String dataTubeId = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__ID);
						String name = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__NAME);
						// String hostUrl = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__HOST_URL);
						// String dataTubeContextRoot = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__CONTEXT_ROOT);
						String dataTubeBaseUrl = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__BASE_URL);

						boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dataTubeIndexItem);
						String statusText = isOnline ? "Online" : "Offline";
						String statusColor = isOnline ? "#2eb82e" : "#cccccc";

						String metadataStr = "";
						String propStr = "";
						String jvmName = null;

						DataTubeServiceMetadata serviceMetadata = dataTubeIdToServiceMetadata.get(dataTubeId);
						if (serviceMetadata != null) {
							jvmName = serviceMetadata.getJvmName();
							long currServerTime = serviceMetadata.getServerTime();
							String currServerTimeStr = DateUtil.toString(DateUtil.toDate(currServerTime), DateUtil.SIMPLE_DATE_FORMAT2);

							String currDataCastId = serviceMetadata.getDataCastId();
							String currDataTubeId = serviceMetadata.getDataTubeId();
							Map<String, Object> metadataProperties = serviceMetadata.getProperties();

							String currName = serviceMetadata.getName();
							String currHostUrl = serviceMetadata.getHostURL();
							String currContextRoot = serviceMetadata.getContextRoot();

							// metadataStr += "datacast_id = " + currDataCastId + "<br/>";
							// metadataStr += "datatube_id = " + currDataTubeId + "<br/>";
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

						if (jvmName == null) {
							jvmName = "";
						}
			%>
			<tr>
				<td class="td1"><%=jvmName%></td>
				<td class="td1"><%=dataTubeId%></td>
				<td class="td1"><%=name%></td>
				<td class="td1"><%=dataTubeBaseUrl%></td>
				<td class="td1" width="100"><font color="<%=statusColor%>"><%=statusText%></font></td>
				<td class="td2"><%=metadataStr%></td>
			</tr>
			<%
					} // dataTubeIndexItems loop
				}
			%>
		</table>
	</div>
	<br />
</body>
</html>
