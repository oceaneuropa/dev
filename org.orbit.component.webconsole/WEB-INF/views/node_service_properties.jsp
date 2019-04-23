<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.infra.api.indexes.*, org.orbit.infra.api.extensionregistry.*"%>
<%@ page import="org.orbit.component.api.tier3.domain.*"%>
<%@ page import="org.orbit.component.api.tier3.nodecontrol.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	MachineConfig machineConfig = (MachineConfig) request.getAttribute("machineConfig");
	PlatformConfig platformConfig = (PlatformConfig) request.getAttribute("platformConfig");
	NodeInfo nodeInfo = (NodeInfo) request.getAttribute("nodeInfo");
	IndexItem nodeIndexItem = (IndexItem) request.getAttribute("nodeIndexItem");

	ServiceInfo serviceInfo = (ServiceInfo) request.getAttribute("serviceInfo");
	ServicePropertyInfo[] propertyInfos = (ServicePropertyInfo[]) request.getAttribute("propertyInfos");

	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";
	String platformName = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String platformId = (platformConfig != null) ? platformConfig.getId() : "";
	String id = (nodeInfo != null) ? nodeInfo.getId() : "";
	String name = (nodeInfo != null) ? nodeInfo.getName() : "";

	if (propertyInfos == null) {
		propertyInfos = new ServicePropertyInfo[0];	
	}

	int sid = -1;
	String serviceName = "";
	if (serviceInfo != null) {
		sid = serviceInfo.getSID();
		serviceName = serviceInfo.getName();
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Properties</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
<link rel="stylesheet" href="../views/css/treetable.css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/node_service_properties.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> > 
		<a href="<%=contextRoot%>/domain/platforms?machineId=<%=machineId%>">Platforms</a> (of <%=machineName%>)>
		<a href="<%=contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId%>">Nodes</a> (of <%=platformName%>) >
		<a href="<%=contextRoot + "/domain/nodeservices?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Services</a> (of <%=name%>) > 
		Service [<%=serviceName%>]
	</div>

	<div class="main_div01">
		<h2>Properties</h2>
		<div class="top_tools_div01">
			<a id="action_addServiceProperty" class="button02">Add</a> 
			<a id="action_removeServiceProperties" class="button02">Remove</a> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeserviceproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id + "&sid=" + sid%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="<%=contextRoot + "/domain/nodeservicepropertyremove"%>">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<input type="hidden" name="id" value="<%=id%>">
				<input type="hidden" name="sid" value="<%=sid%>">
				<tr>
					<th class="th1" width="12">
						<input type="checkbox" onClick="toggleSelection(this, 'name')" />
					</th>
					<th class="th1" width="200">Name</th>
					<th class="th1" width="200">Built-In Value</th>
					<th class="th1" width="200">Value</th>
					<th class="th1" width="200">Actions</th>
				</tr>
				<%
					if (propertyInfos.length == 0) {
				%>
				<tr>
					<td colspan="5">(n/a)</td>
				</tr>
				<%
					} else {
						for (ServicePropertyInfo propertyInfo : propertyInfos) {
							String currPropName = propertyInfo.getName();
							Object currPropBuiltInValue = propertyInfo.getBuiltInValue();
							Object currPropValue = propertyInfo.getValue();

							String currPropBuiltInValueStr = (currPropBuiltInValue != null) ? currPropBuiltInValue.toString() : "";
							String currPropValueStr = (currPropValue != null) ? currPropValue.toString() : "";

							String color1 = "#aaaaaa";
							String color2 = "#000000";

							if (currPropValueStr == null || currPropValueStr.isEmpty()) {
								if (currPropBuiltInValueStr != null && !currPropBuiltInValueStr.isEmpty()) {
									currPropValueStr = currPropBuiltInValueStr;
									color2 = "#aaaaaa";
								}
							}
				%>
				<tr>
					<td class="td1">
						<input type="checkbox" id="property__<%=currPropName%>" name="name" value="<%=currPropName%>">
					</td>
					<td class="td2">
						<%=currPropName%>
					</td>
					<td class="td2">
						<font color="<%=color1%>">
							<%=currPropBuiltInValueStr%>
						</font>
					</td>
					<td class="td2">
						<font color="<%=color2%>">
							<%=currPropValueStr%>
						</font>
					</td>
					<td class="td1">
						<a class="action01" href="javascript:changeServiceProperty('<%=currPropName%>', '<%=currPropValueStr%>')">Edit</a> | 
						<a class="action01" href="javascript:removeServiceProperty('<%=currPropName%>')">Remove</a> 
					</td>
				</tr>
				<%
						}
					}
				%>
			</form>
		</table>
	</div>
	<br/>

	<dialog id="newPropertyDialog">
	<div class="dialog_title_div01">Add Property</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/domain/nodeservicepropertyadd"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" name="sid" value="<%=sid%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Name:</td>
					<td width="75%">
						<input type="text" name="name" class="input01" size="20">
					</td>
				</tr>
				<tr>
					<td>Value:</td>
					<td>
						<textarea name="value" rows="10" cols="100"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okAddProperty" class="button02" href="javascript:document.getElementById('new_form').submit();">OK</a> 
			<a id="cancelAddProperty" class="button02b" href="javascript:document.getElementById('new_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changePropertyDialog">
	<div class="dialog_title_div01">Set Property</div>
	<form id="update_form" method="post" action="<%=contextRoot + "/domain/nodeservicepropertyupdate"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>"> 
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" name="sid" value="<%=sid%>">
		<input type="hidden" id="attribute_oldName" name="oldName">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Name:</td>
					<td width="75%">
						<input type="text" id="attribute_name" name="name" class="input01" size="20">
					</td>
				</tr>
				<tr>
					<td>Value:</td>
					<td>
						<textarea id="attribute_value" name="value" rows="10" cols="100"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeProperty" class="button02" href="javascript:document.getElementById('update_form').submit();">OK</a> 
			<a id="cancelChangeProperty" class="button02b" href="javascript:document.getElementById('update_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="removePropertyDialog">
	<form id="remove_form" method="post" action="<%=contextRoot + "/domain/nodeservicepropertyremove"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>"> 
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" name="sid" value="<%=sid%>">
		<input type="hidden" id="remove_form_property_name" name="name">

		<div class="dialog_title_div01">Remove Property</div>
		<div class="dialog_main_div01" id="removePropertyDialogMessageDiv">Are you sure you want to remove the property?</div>
		<div class="dialog_button_div01">
			<a id="okRemoveProperty" class="button02">OK</a> 
			<a id="cancelRemoveProperty" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="removePropertiesDialog">
	<div class="dialog_title_div01">Remove Properties</div>
	<div class="dialog_main_div01" id="removePropertiesDialogMessageDiv">Are you sure you want to remove selected properties?</div>
	<div class="dialog_button_div01">
		<a id="okRemoveProperties" class="button02">OK</a> 
		<a id="cancelRemoveProperties" class="button02b">Cancel</a>
	</div>
	</dialog>

</body>
</html>
