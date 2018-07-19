<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.infra.api.indexes.*, org.orbit.infra.api.extensionregistry.*"%>
<%@ page import="org.orbit.component.api.tier3.domainmanagement.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	MachineConfig machineConfig = (MachineConfig) request.getAttribute("machineConfig");
	PlatformConfig platformConfig = (PlatformConfig) request.getAttribute("platformConfig");
	IndexItem platformIndexItem = (IndexItem) request.getAttribute("platformIndexItem");
	List<ExtensionItem> extensionItems = (List<ExtensionItem>) request.getAttribute("extensionItems");

	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	String name = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String id = (platformConfig != null) ? platformConfig.getId() : "";

	if (extensionItems == null) {
		extensionItems = new ArrayList<ExtensionItem>();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Domain Management</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
<link rel="stylesheet" href="https://cdn.metroui.org.ua/v4/css/metro-all.min.css">
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> > 
		<a href="<%=contextRoot%>/domain/platforms?machineId=<%=machineId%>"><%=machineName%></a> >
		<%=name%>
	</div>
	<div class="main_div01">
		<h2>Index Properties</h2>
		<div class="top_tools_div01"> 
			<a class="button02" href="<%=contextRoot + "/domain/platformproperties?machineId=" + machineId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="22.3%">Name</th>
				<th class="th1" width="76.7%">Value</th>
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
		<h2>Extensions</h2>
		<ul data-role="treeview">
    		<li data-caption="OneDrive">
        		<ul>
            		<li data-caption="Documents"></li>
            		<li data-caption="Projects">
                		<ul>
                   		 	<li data-caption="Web"></li>
                    		<li data-caption="Android"></li>
                    		<li data-caption="Windows"></li>
                    		<li data-caption="iOS"></li>
                		</ul>
            		</li>
        		</ul>
    		</li>

			<%
				for (ExtensionItem extensionItem : extensionItems) {
					String extensionTypeId = extensionItem.getTypeId();
					String extensionId = extensionItem.getExtensionId();
					String extensionName = extensionItem.getName();
					String extensionDesc = extensionItem.getDescription();

					extensionTypeId = StringUtil.get(extensionTypeId, "");
					extensionId = StringUtil.get(extensionId, "");
					extensionName = StringUtil.get(extensionName, "");
					extensionDesc = StringUtil.get(extensionDesc, "");
			%>
				<tr>
					<td class="td2">
						<%=extensionTypeId%>
					</td>
					<td class="td2">
						<%=extensionId%>
					</td>
					<td class="td2">
						<%=extensionName%>
					</td>
					<td class="td2">
						<%=extensionDesc%>
					</td>
				</tr>
			<%
				}
			%>
		</ul>
	</div>
	<br/>

	<div class="main_div01">
		<h2>Extensions</h2>
		<div class="top_tools_div01"> 
			<a class="button02" href="<%=contextRoot + "/domain/platformproperties?machineId=" + machineId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="25%">Type ID</th>
				<th class="th1" width="25%">ID</th>
				<th class="th1" width="25%">Name</th>
				<th class="th1" width="25%">Description</th>
			</tr>
			<%
				if (extensionItems.isEmpty()) {
			%>
			<tr>
				<td colspan="4">(n/a)</td>
			</tr>
			<%
				} else {
					for (ExtensionItem extensionItem : extensionItems) {
						String extensionTypeId = extensionItem.getTypeId();
						String extensionId = extensionItem.getExtensionId();
						String extensionName = extensionItem.getName();
						String extensionDesc = extensionItem.getDescription();

						extensionTypeId = StringUtil.get(extensionTypeId, "");
						extensionId = StringUtil.get(extensionId, "");
						extensionName = StringUtil.get(extensionName, "");
						extensionDesc = StringUtil.get(extensionDesc, "");
			%>
				<tr>
					<td class="td2">
						<%=extensionTypeId%>
					</td>
					<td class="td2">
						<%=extensionId%>
					</td>
					<td class="td2">
						<%=extensionName%>
					</td>
					<td class="td2">
						<%=extensionDesc%>
					</td>
				</tr>
			<%
					}
				}
			%>
		</table>
	</div>
	<br/>

    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script src="https://cdn.metroui.org.ua/v4/js/metro.min.js"></script>
</body>
</html>
