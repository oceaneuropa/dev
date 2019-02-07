<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.infra.api.indexes.*, org.orbit.infra.api.extensionregistry.*"%>
<%@ page import="org.orbit.component.api.tier3.domain.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	MachineConfig machineConfig = (MachineConfig) request.getAttribute("machineConfig");
	PlatformConfig platformConfig = (PlatformConfig) request.getAttribute("platformConfig");
	IndexItem platformIndexItem = (IndexItem) request.getAttribute("platformIndexItem");
	List<ExtensionItem> extensionItems = (List<ExtensionItem>) request.getAttribute("extensionItems");
	Map<String, List<ExtensionItem>> extensionItemMap = (Map<String, List<ExtensionItem>>) request.getAttribute("extensionItemMap");
	List<IndexItem> indexItems = (List<IndexItem>) request.getAttribute("indexItems");

	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	String name = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String id = (platformConfig != null) ? platformConfig.getId() : "";

	if (extensionItems == null) {
		extensionItems = new ArrayList<ExtensionItem>();
	}
	if (extensionItemMap == null) {
		extensionItemMap = new TreeMap<String, List<ExtensionItem>>();	
	}
	if (indexItems == null) {
		indexItems = new ArrayList<IndexItem>();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Domain Management</title>
<!-- 
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
<link rel="stylesheet" href="<%=contextRoot + "/views/css/treetable.css"%>">
 -->
<link rel="stylesheet" href="../views/css/style.css">
<link rel="stylesheet" href="../views/css/treetable.css">
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> > 
		<a href="<%=contextRoot%>/domain/platforms?machineId=<%=machineId%>">Platforms</a> (of <%=machineName%>) >
		Platform [<%=name%>]
	</div>
	<div class="main_div01">
		<h2>Platform Properties</h2>
		<div class="top_tools_div01"> 
			<a class="button02" href="<%=contextRoot + "/domain/platformproperties?machineId=" + machineId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="25.8%">Name</th>
				<th class="th1" width="74.2%">Value</th>
			</tr>
			<%
				if (platformIndexItem == null || platformIndexItem.getProperties().isEmpty()) {
			%>
			<tr>
				<td colspan="2">(n/a)</td>
			</tr>
			<%
				} else {
					Map<String, Object> indexItemProperties = platformIndexItem.getProperties();
					for (Iterator<String> itor = indexItemProperties.keySet().iterator(); itor.hasNext(); ) {
						String propName = itor.next();
						Object propValue = indexItemProperties.get(propName);
			%>
				<tr>
					<td class="td2">
						<%=propName%>
					</td>
					<td class="td2">
						<%=propValue%>
					</td>
				</tr>
			<%
					}
				}
			%>
		</table>
	</div>
	<br/>

	<div class="main_div01">
		<h2>Services</h2>
		<div class="top_tools_div01"> 
			<a class="button02" href="<%=contextRoot + "/domain/platformproperties?machineId=" + machineId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="200">Indexer</th>
				<th class="th1" width="150">Type</th>
				<th class="th1" width="150">Name</th>
				<th class="th1" width="100">Status</th>
				<th class="th1" width="400">Properties</th>
			</tr>
			<%
				if (indexItems.isEmpty()) {
			%>
			<tr>
				<td colspan="5">(n/a)</td>
			</tr>
			<%
				} else {
						for (IndexItem indexItem : indexItems) {
							String indexProviderId = indexItem.getIndexProviderId();
							int indexItemId = indexItem.getIndexItemId();
							String indexItemType = indexItem.getType();
							String indexItemName = indexItem.getName();
							Map<String, Object> properties = indexItem.getProperties();

							String props = "";
							if (properties != null) {
								int pIndex = 0;
								for (Iterator<String> propItor = properties.keySet().iterator(); propItor.hasNext();) {
									String propName = propItor.next();
									Object propValue = properties.get(propName);

									if (propName.endsWith("_time") && propValue instanceof Long) {
										Date date = DateUtil.toDate((Long) propValue);
										propValue = DateUtil.toString(date, DateUtil.SIMPLE_DATE_FORMAT2);
									}

									if (pIndex > 0) {
										props += ", <br/>";
									}
									props += propName + " = " + propValue.toString();
									pIndex++;
								}
							}

							boolean isOnline = IndexItemHelper.INSTANCE.isOnline(indexItem);
							String statusText = isOnline ? "Online" : "Offline";
							String statusColor = isOnline ? "#2eb82e" : "#cccccc";
			%>
			<tr>
				<td class="td2"><%=indexProviderId%></td>
				<td class="td2"><%=indexItemType%></td>
				<td class="td2"><%=indexItemName%></td>
				<td class="td1"><font color="<%=statusColor%>"><%=statusText%></font></td>
				<td class="td2"><%=props%></td>
			</tr>
			<%
					}
				}
			%>
		</table>
	</div>
	<br>

	<div class="main_div01">
		<h2>Extensions (<%=extensionItems.size()%>)</h2>
		<div class="top_tools_div01"> 
			<a class="button02" href="<%=contextRoot + "/domain/platformproperties?machineId=" + machineId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="tree_table01">
			<thead>
				<tr>
					<th width="300" >Extension</th>
					<th width="250" >Name</th>
					<th width="250" >Description</th>
					<th width="250" >Properties</th>
				</tr>
			</thead>
			<tbody>
			<%
				for (Iterator<String> typeIdItor = extensionItemMap.keySet().iterator(); typeIdItor.hasNext(); ) {
					String currExtensionTypeId = typeIdItor.next();
					List<ExtensionItem> currExtensionItems = extensionItemMap.get(currExtensionTypeId);
			%>
				<tr>
					<th colspan="3" style="font-weight: bold;"><%=currExtensionTypeId%> (<%=currExtensionItems.size()%>)</th>
				</tr>
			<%
					for (ExtensionItem extensionItem : currExtensionItems) {
						String extensionId = extensionItem.getExtensionId();
						String extensionName = extensionItem.getName();
						String extensionDesc = extensionItem.getDescription();
						Map<String, Object> extensionProps = extensionItem.getProperties();

						extensionId = StringUtil.get(extensionId, "");
						extensionName = StringUtil.get(extensionName, "");
						extensionDesc = StringUtil.get(extensionDesc, "");
						
						String extensionPropsString = "";
						if (extensionProps != null) {
							int index = 0;
							for (Iterator<String> propNameItor = extensionProps.keySet().iterator(); propNameItor.hasNext(); ) {
								String propName = propNameItor.next();
								Object propValue = extensionProps.get(propName);
								String propValueString = (propValue != null) ? propValue.toString() : "";
								if (index > 0) {
									extensionPropsString += "<br/>";
								}
								extensionPropsString += propName + "=" + propValueString;
								index++;
							}
						}
						
			%>
				<tr>
					<th class="start"><%=extensionId%></th>
					<td><%=extensionName%></td>
					<td><%=extensionDesc%></td>
					<td><%=extensionPropsString%></td>
				</tr>
			<%
					}
				}
			%>
			</tbody>
		</table>
	</div>
	<br/>
</body>
</html>
