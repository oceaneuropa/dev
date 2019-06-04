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

	String configRegistryId = "";
	String configRegistryName = "";
	IConfigRegistry configReg = (IConfigRegistry) request.getAttribute("configReg");
	if (configReg != null) {
		configRegistryId = configReg.getId();
		configRegistryName = configReg.getName();
	}

	List<IConfigElement> parentConfigElements = (List<IConfigElement>) request.getAttribute("parentConfigElements");
	if (parentConfigElements == null) {
		parentConfigElements = new ArrayList<IConfigElement>();
	}

	String parentElementId = "";
	String elementId = "";
	String elementName = "";
	IConfigElement configElement = (IConfigElement) request.getAttribute("configElement");
	if (configElement != null) {
		parentElementId = configElement.getParentElementId();
		elementId = configElement.getElementId();
		elementName = configElement.getName();
	}

	Map<String, Object> attributes = (Map<String, Object>) request.getAttribute("attributes");
	if (attributes == null) {
		attributes = new HashMap<String, Object>();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Config Element</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/config_element_attributes.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot + "/admin/configregs"%>">Config Registry</a> / 
		<a href="<%=contextRoot + "/admin/configelements?configRegistryId=" + configRegistryId%>"><%=configRegistryName%></a> /
		<%
			for (IConfigElement currParentConfigElement : parentConfigElements) {
				String currParentElementId = currParentConfigElement.getElementId();
				String currParentElementName = currParentConfigElement.getName();
		%>
			<a href="<%=contextRoot + "/admin/configelements?configRegistryId=" + configRegistryId + "&parentElementId=" + currParentElementId%>"><%=currParentElementName%></a> /
		<%
			}
		%>
		<%=elementName%>
	</div>

	<div class="main_div01">
		<h2>Attributes</h2>
		<div class="top_tools_div01">
			<a id="actionAddAttribute" class="button02">Add</a> 
			<a id="actionDeleteAttributes" class="button02">Delete</a> 
			<a class="button02" href="<%=contextRoot + "/admin/configelementattributes?configRegistryId=" + configRegistryId + "&parentElementId=" + parentElementId + "&elementId=" + elementId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="">
				<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>"> 
				<input type="hidden" name="parentElementId" value="<%=parentElementId%>">
				<input type="hidden" name="elementId" value="<%=elementId%>">
				<tr>
					<th class="th1" width="10">
						<input type="checkbox" onClick="toggleSelection(this, 'name')" />
					</th>
					<th class="th1" width="200">Name</th>
					<th class="th1" width="400">Value</th>
					<th class="th1" width="150">Actions</th>
				</tr>
				<%
					if (attributes.isEmpty()) {
				%>
				<tr>
					<td colspan="4">(n/a)</td>
				</tr>
				<%
					} else {
						for (Iterator<String> itor = attributes.keySet().iterator(); itor.hasNext(); ) {
							String attrName = itor.next();
							Object attrValue = attributes.get(attrName);

							Object attrValueForEdit = attrValue;

							int rowNum = 1;
							if (attrValue instanceof String) {
								String valueString = (String) attrValue;
								if (valueString.contains("\r\n")) {
									String[] array = valueString.split("\r\n");
									rowNum = array.length;

									if (valueString.endsWith("\r\n")) {
										rowNum += 1;
									}
									rowNum += 1;

									attrValueForEdit = valueString.replace("\r\n", "\\r\\n");
								}
							}
				%>
				<tr>
					<td class="td1">
						<input type="checkbox" name="name" value="<%=attrName%>">
					</td>
					<td class="td1">
						<%=attrName%>
					</td>
					<td class="td2">
						<% if (rowNum > 1) { %>
							<textarea rows="<%=rowNum%>" cols="100" readonly><%=attrValue%></textarea>
						<% } else { %>
							<%=attrValue%>
						<% } %>
					</td>
					<td class="td1">
						<a class="action01" href="javascript:changeAttribute('<%=attrName%>', '<%=attrValueForEdit%>', '<%=rowNum%>')">Edit</a> | 
						<a class="action01" href="javascript:deleteAttribute('<%=attrName%>')">Delete</a> 
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

	<dialog id="newAttributeDialog">
	<div class="dialog_title_div01">Add Attribute</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/admin/configelementattributeadd"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<input type="hidden" name="parentElementId" value="<%=parentElementId%>">
		<input type="hidden" name="elementId" value="<%=elementId%>">
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
			<a id="okAddAttribute" class="button02">OK</a> 
			<a id="cancelAddAttribute" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changeAttributeDialog">
	<div class="dialog_title_div01">Set Attribute</div>
	<form id="update_form" method="post" action="<%=contextRoot + "/admin/configelementattributeupdate"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<input type="hidden" name="parentElementId" value="<%=parentElementId%>">
		<input type="hidden" name="elementId" value="<%=elementId%>">
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
			<a id="okChangeAttribute" class="button02">OK</a> 
			<a id="cancelChangeAttribute" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteAttributesDialog">
	<form id="delete_form" method="post" action="<%=contextRoot + "/admin/configelementattributedelete"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<input type="hidden" name="parentElementId" value="<%=parentElementId%>">
		<input type="hidden" name="elementId" value="<%=elementId%>">
		<div class="dialog_title_div01">Delete Attributes</div>
		<div class="dialog_main_div01">Are you sure you want to delete selected attributes?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteAttributes" class="button02">OK</a>
			<a id="cancelDeleteAttributes" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteAttributeDialog">
	<form id="delete_form2" method="post" action="<%=contextRoot + "/admin/configelementattributedelete"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<input type="hidden" name="parentElementId" value="<%=parentElementId%>">
		<input type="hidden" name="elementId" value="<%=elementId%>">
		<input type="hidden" id="delete_form2_name" name="name">
		<div class="dialog_title_div01">Delete Attribute</div>
		<div class="dialog_main_div01">Are you sure you want to delete the attribute?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteAttribute" class="button02">OK</a> 
			<a id="cancelDeleteAttribute" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

</body>
</html>
