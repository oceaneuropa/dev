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
	String configRegistryName = "Unknown";

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
	String parentElementName = "Unknown";
	String grandParentElementId = "";
	if (parentConfigElements != null && !parentConfigElements.isEmpty()) {
		IConfigElement parentConfigElement = parentConfigElements.get(parentConfigElements.size() - 1);
		parentElementId = parentConfigElement.getElementId();
		parentElementName = parentConfigElement.getName();
		grandParentElementId = parentConfigElement.getParentElementId();
	}

	IConfigElement[] configElements = (IConfigElement[]) request.getAttribute("configElements");
	if (configElements == null) {
		configElements = new IConfigElement[0];
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Config Element</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/config_elements.js"%>" defer></script>

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
	</div>

	<div class="main_div01">
		<h2>Config Elements</h2>
		<div class="top_tools_div01">
			<a id="actionAddConfigElement" class="button02">Create</a>
			<a id="actionDeleteConfigElements" class="button02">Delete</a>
			<%
				if (!parentElementId.isEmpty()) {
			%>
				<a class="button02" href="<%=contextRoot + "/admin/configelements?configRegistryId=" + configRegistryId + "&parentElementId=" + grandParentElementId%>">Up</a>
				<a class="button02" href="<%=contextRoot + "/admin/configelements?configRegistryId=" + configRegistryId + "&parentElementId=" + parentElementId%>">Refresh</a>
			<%
				} else {
			%>
				<a class="button02" href="<%=contextRoot + "/admin/configregs"%>">Up</a>
				<a class="button02" href="<%=contextRoot + "/admin/configelements?configRegistryId=" + configRegistryId%>">Refresh</a>
			<%
				}
			%>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post">
			<input id ="main_list__action" type="hidden" name="action" value="">
			<tr>
				<th class="th1" width="10">
					<input type="checkbox" onClick="toggleSelection(this, 'elementId')" />
				</th>
				<th class="th1" width="100">Name</th>
				<th class="th1" width="150">Attributes</th>
				<th class="th1" width="100">Date Created</th>
				<th class="th1" width="100">Data Modified</th>
				<th class="th1" width="100">Actions</th>
			</tr>
			<%
				if (configElements.length == 0) {
			%>
			<tr>
				<td colspan="6">(n/a)</td>
			</tr>
			<%
				} else {
					for (IConfigElement configElement : configElements) {
						String currId = configElement.getElementId();
						String currName = configElement.getName();
						long currDateCreated = configElement.getDateCreated();
						long currDateModified = configElement.getDateModified();
						Map<String, Object> currAttrs = configElement.getAttributes();

						String currDateCreatedStr = DateUtil.toString(currDateCreated, DateUtil.SIMPLE_DATE_FORMAT2);
						String currDateModifiedStr = DateUtil.toString(currDateModified, DateUtil.SIMPLE_DATE_FORMAT2);

						String currAttrStr = "";
						if (currAttrs != null) {
							for(Iterator<String> itor = currAttrs.keySet().iterator(); itor.hasNext(); ) {
								String currAttrName = itor.next();
								Object currAttrValue = currAttrs.get(currAttrName);
								currAttrStr += currAttrName + "=" + currAttrValue + "<BR/>";
							}
						}
			%>
			<tr>
				<td class="td1"><input type="checkbox" name="elementId" value="<%=currId%>"></td>
				<td class="td2"><%=currName%></td>
				<td class="td2"><%=currAttrStr%></td>
				<td class="td1"><%=currDateCreatedStr%></td>
				<td class="td1"><%=currDateModifiedStr%></td>
				<td class="td1">
					<a class="action01" href="javascript:changeConfigElement('<%=currId%>', '<%=currName%>')">Edit</a> | 
					<a class="action01" href="javascript:deleteConfigElement('<%=currId%>')">Delete</a> | 
					<a class="action01" target="_blank" href="<%=contextRoot%>/admin/configelementattributes?configRegistryId=<%=configRegistryId%>&parentElementId=<%=parentElementId%>&elementId=<%=currId%>">Attributes</a> |
					<a class="action01" href="<%=contextRoot%>/admin/configelements?configRegistryId=<%=configRegistryId%>&parentElementId=<%=currId%>">Elements</a>
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

	<dialog id="newConfigElementDialog">
	<div class="dialog_title_div01">Create Config Element</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/admin/configelementadd"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<%
		if (!parentElementId.isEmpty()) {
		%>
		<input type="hidden" name="parentElementId" value="<%=parentElementId%>">
		<%
		}
		%>
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okAddConfigElement" class="button02">OK</a> 
			<a id="cancelAddConfigElement" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changeConfigElementDialog">
	<div class="dialog_title_div01">Change Config Element</div>
	<form id="update_form" name="update_form_name" method="post" action="<%=contextRoot + "/admin/configelementupdate"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<%
		if (!parentElementId.isEmpty()) {
		%>
		<input type="hidden" name="parentElementId" value="<%=parentElementId%>">
		<%
		}
		%>
		<input type="hidden" id="config_element__elementId" name="elementId" >
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Name:</td>
					<td><input id="config_element__name" type="text" name="name"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeConfigElement" class="button02">OK</a> 
			<a id="cancelChangeConfigElement" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteConfigElementsDialog">
	<form id="delete_form" method="post" action="<%=contextRoot + "/admin/configelementdelete"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<%
		if (!parentElementId.isEmpty()) {
		%>
		<input type="hidden" name="parentElementId" value="<%=parentElementId%>">
		<%
		}
		%>
		<div class="dialog_title_div01">Delete Config Elements</div>
		<div class="dialog_main_div01" id="deleteConfigElementsDialogMessageDiv">Are you sure you want to delete selected config elements?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteConfigElements" class="button02">OK</a>
			<a id="cancelDeleteConfigElements" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteConfigElementDialog">
	<form id="delete_form2" method="post" action="<%=contextRoot + "/admin/configelementdelete"%>">
		<input type="hidden" name="configRegistryId" value="<%=configRegistryId%>">
		<%
		if (!parentElementId.isEmpty()) {
		%>
		<input type="hidden" name="parentElementId" value="<%=parentElementId%>">
		<%
		}
		%>
		<input type="hidden" id="delete_form2_elementId" name="elementId">
		<div class="dialog_title_div01">Delete Config Element</div>
		<div class="dialog_main_div01">Are you sure you want to delete the config element?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteConfigElement" class="button02">OK</a> 
			<a id="cancelDeleteConfigElement" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

</body>
</html>
