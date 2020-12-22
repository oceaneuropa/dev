<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.service.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.infra.api.*"%>
<%@ page import="org.orbit.infra.api.datacast.*"%>
<%@ page import="org.orbit.infra.io.*"%>
<%@ page import="org.orbit.infra.io.configregistry.*"%>
<%@ page import="org.orbit.infra.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

	String configRegistryId = "";
	String configRegistryName = "";
	IConfigRegistry configReg = (IConfigRegistry) request.getAttribute("configReg");
	if (configReg != null) {
		configRegistryId = configReg.getId();
		configRegistryName = configReg.getName();
	}

	Map<String, Object> properties = (Map<String, Object>) request.getAttribute("properties");
	if (properties == null) {
		properties = new HashMap<String, Object>();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Config Registry</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/config_reg_properties.js"%>"></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot + "/admin/configregs"%>">Config Registry</a> / 
		<%=configRegistryName%>
	</div>

	<div class="main_div01">
		<h2>Properties</h2>
		<div class="top_tools_div01">
			<a id="actionAddProperty" class="button02">Add</a> 
			<a id="actionDeleteProperties" class="button02">Delete</a> 
			<a class="button02" href="<%=contextRoot + "/admin/configregproperties?configRegistryId=" + configRegistryId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="">
				<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>"> 
				<tr>
					<th class="th1" width="10">
						<input type="checkbox" onClick="toggleSelection(this, 'name')" />
					</th>
					<th class="th1" width="200">Name</th>
					<th class="th1" width="400">Value</th>
					<th class="th1" width="150">Actions</th>
				</tr>
				<%
					if (properties.isEmpty()) {
				%>
				<tr>
					<td colspan="4">(n/a)</td>
				</tr>
				<%
					} else {
						for (Iterator<String> itor = properties.keySet().iterator(); itor.hasNext(); ) {
							String propName = itor.next();
							Object propValue = properties.get(propName);

							Object valueForEdit = propValue;

							int rowNum = 1;
							if (propValue instanceof String) {
								String valueString = (String) propValue;
								if (valueString.contains("\r\n")) {
									String[] array = valueString.split("\r\n");
									rowNum = array.length;

									if (valueString.endsWith("\r\n")) {
										rowNum += 1;
									}
									rowNum += 1;

									valueForEdit = valueString.replace("\r\n", "\\r\\n");
								}
							}
				%>
				<tr>
					<td class="td1">
						<input type="checkbox" name="name" value="<%=propName%>">
					</td>
					<td class="td1">
						<%=propName%>
					</td>
					<td class="td2">
						<% if (rowNum > 1) { %>
							<textarea rows="<%=rowNum%>" cols="100" readonly><%=propValue%></textarea>
						<% } else { %>
							<%=propValue%>
						<% } %>
					</td>
					<td class="td1">
						<a class="action01" href="javascript:changeProperty('<%=propName%>', '<%=valueForEdit%>', '<%=rowNum%>')">Edit</a> | 
						<a class="action01" href="javascript:deleteProperty('<%=propName%>')">Delete</a> 
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
	<form id="new_form" method="post" action="<%=contextRoot + "/admin/configregpropertyadd"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
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
			<a id="okAddProperty" class="button02">OK</a> 
			<a id="cancelAddProperty" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changePropertyDialog">
	<div class="dialog_title_div01">Set Property</div>
	<form id="update_form" method="post" action="<%=contextRoot + "/admin/configregpropertyupdate"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<input type="hidden" id="property_oldName" name="oldName">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Name:</td>
					<td width="75%">
						<input type="text" id="property_name" name="name" class="input01" size="20">
					</td>
				</tr>
				<tr>
					<td>Value:</td>
					<td>
						<textarea id="property_value" name="value" rows="10" cols="100"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeProperty" class="button02">OK</a> 
			<a id="cancelChangeProperty" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deletePropertiesDialog">
	<form id="delete_form" method="post" action="<%=contextRoot + "/admin/configregpropertydelete"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<div class="dialog_title_div01">Delete Properties</div>
		<div class="dialog_main_div01">Are you sure you want to delete selected properties?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteProperties" class="button02">OK</a>
			<a id="cancelDeleteProperties" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deletePropertyDialog">
	<form id="delete_form2" method="post" action="<%=contextRoot + "/admin/configregpropertydelete"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<input type="hidden" id="delete_form2_name" name="name">
		<div class="dialog_title_div01">Delete Property</div>
		<div class="dialog_main_div01">Are you sure you want to delete the property?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteProperty" class="button02">OK</a> 
			<a id="cancelDeleteProperty" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

</body>
</html>
