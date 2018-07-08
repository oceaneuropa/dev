<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.orbit.component.api.tier3.domainmanagement.*"%>
<%@ page import="org.orbit.component.api.tier3.nodecontrol.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	MachineConfig machineConfig = (MachineConfig) request.getAttribute("machineConfig");
	PlatformConfig platformConfig = (PlatformConfig) request.getAttribute("platformConfig");
	NodeInfo nodeInfo = (NodeInfo) request.getAttribute("nodeInfo");
	IndexItem nodeIndexItem = (IndexItem) request.getAttribute("nodeIndexItem");

	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	String platformName = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String platformId = (platformConfig != null) ? platformConfig.getId() : "";

	String id = (nodeInfo != null) ? nodeInfo.getId() : "";
	String name = (nodeInfo != null) ? nodeInfo.getName() : "";
	Map<String, Object> attributes = (nodeInfo != null) ? nodeInfo.getAttributes() : null;
	if (attributes == null) {
		attributes = new HashMap<String, Object>();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Domain Management (Node Attributes)</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
<script type="text/javascript" src="<%=contextRoot + "/views/js/domain_node_attributes.js"%>" defer></script>
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> > 
		<a href="<%=contextRoot%>/domain/platforms?machineId=<%=machineId%>"><%=machineName%></a> >
		<a href="<%=contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId%>"><%=platformName%></a> >
		<!-- <a href="<%=contextRoot + "/domain/nodeattributes?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>"><%=name%></a> -->
		<%=name%>
	</div>
	<div class="main_div01">
		<h2>Index Properties</h2>
		<div class="top_tools_div01"> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeattributes?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="22.3%">Name</th>
				<th class="th1" width="76.7%">Value</th>
			</tr>
			<%
				if (nodeIndexItem == null || nodeIndexItem.getProperties().isEmpty()) {
			%>
			<tr>
				<td colspan="2">(n/a)</td>
			</tr>
			<%
				} else {
					Map<String, Object> indexItemProperties = nodeIndexItem.getProperties();
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
		<h2>Node Attributes</h2>
		<div class="top_tools_div01">
			<a id="action.addAttribute" class="button02">Add</a> 
			<a id="action.deleteAttributes" class="button02">Delete</a> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeattributes?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="<%=contextRoot + "/domain/nodeattributedelete"%>">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<input type="hidden" name="id" value="<%=id%>">
				<tr>
					<th class="th1" width="12"></th>
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

									if (valueString.startsWith("\r\n")) {
										rowNum += 1;
									}
									if (valueString.endsWith("\r\n")) {
										rowNum += 1;
									}

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
						<a class="action01" href="javascript:changeAttribute('<%=attrName%>', '<%=attrValueForEdit%>', '<%=rowNum%>')">Change</a> | 
						<a class="action01" href="javascript:deleteAttribute('<%=contextRoot + "/domain/nodeattributedelete"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>', '<%=attrName%>')">Delete</a> 
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
	<form id="new_form" method="post" action="<%=contextRoot + "/domain/nodeattributeadd"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
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
			<a id="okAddAttribute" class="button02" href="javascript:document.getElementById('new_form').submit();">OK</a> 
			<a id="cancelAddAttribute" class="button02b" href="javascript:document.getElementById('new_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changeAttributeDialog">
	<div class="dialog_title_div01">Set Attribute</div>
	<form id="update_form" method="post" action="<%=contextRoot + "/domain/nodeattributeupdate"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>"> 
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
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
			<a id="okChangeAttribute" class="button02" href="javascript:document.getElementById('update_form').submit();">OK</a> 
			<a id="cancelChangeAttribute" class="button02b" href="javascript:document.getElementById('update_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteAttributeDialog">
	<div class="dialog_title_div01">Delete Attribute</div>
	<div class="dialog_main_div01" id="deleteAttributeDialogMessageDiv">Are you sure you want to delete the attribute?</div>
	<div class="dialog_button_div01">
		<a id="okDeleteAttribute" class="button02">OK</a> 
		<a id="cancelDeleteAttribute" class="button02b">Cancel</a>
	</div>
	</dialog>

	<dialog id="deleteAttributesDialog">
	<div class="dialog_title_div01">Delete Attributes</div>
	<div class="dialog_main_div01" id="deleteAttributesDialogMessageDiv">Are you sure you want to delete selected attributes?</div>
	<div class="dialog_button_div01">
		<a id="okDeleteAttributes" class="button02">OK</a> 
		<a id="cancelDeleteAttributes" class="button02b">Cancel</a>
	</div>
	</dialog>

</body>
</html>
