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

	IConfigRegistry[] configRegs = (IConfigRegistry[]) request.getAttribute("configRegs");
	if (configRegs == null) {
		configRegs = new IConfigRegistry[] {};
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Config Registry</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/config_regs.js"%>" defer></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot + "/admin/configregs"%>">Config Registry</a> / 
	</div>

	<div class="main_div01">
		<h2>Config Registry</h2>
		<div class="top_tools_div01">
			<a id="actionAddConfigReg" class="button02">Create</a>
			<a id="actionDeleteConfigRegs" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/admin/configregs"%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post">
			<input id ="main_list__action" type="hidden" name="action" value="">
			<tr>
				<th class="th1" width="10">
					<input type="checkbox" onClick="toggleSelection(this, 'configRegistryId')" />
				</th>
				<th class="th1" width="100">Name</th>
				<th class="th1" width="100">Type</th>
				<th class="th1" width="100">Properties</th>
				<th class="th1" width="100">Date Created</th>
				<th class="th1" width="100">Data Modified</th>
				<th class="th1" width="150">Actions</th>
			</tr>
			<%
				if (configRegs.length == 0) {
			%>
			<tr>
				<td colspan="7">(n/a)</td>
			</tr>
			<%
				} else {
					for (IConfigRegistry configReg : configRegs) {
						String currId = configReg.getId();
						String currName = configReg.getName();
						String currType = configReg.getType();
						long currDateCreated = configReg.getDateCreated();
						long currDateModified = configReg.getDateModified();
						Map<String, Object> currProps = configReg.getProperties();

						String currDateCreatedStr = DateUtil.toString(currDateCreated, DateUtil.SIMPLE_DATE_FORMAT2);
						String currDateModifiedStr = DateUtil.toString(currDateModified, DateUtil.SIMPLE_DATE_FORMAT2);

						String currPropStr = "";
						if (currProps != null) {
							for(Iterator<String> itor = currProps.keySet().iterator(); itor.hasNext(); ) {
								String currPropName = itor.next();
								Object currPropValue = currProps.get(currPropName);
								currPropStr += currPropName + "=" + currPropValue + "<BR/>";
							}
						}
			%>
			<tr>
				<td class="td1"><input type="checkbox" name="configRegistryId" value="<%=currId%>"></td>
				<td class="td2"><%=currName%></td>
				<td class="td1"><%=currType%></td>
				<td class="td1"><%=currPropStr%></td>
				<td class="td1"><%=currDateCreatedStr%></td>
				<td class="td1"><%=currDateModifiedStr%></td>
				<td class="td1">
					<a class="action01" href="javascript:changeConfigReg('<%=currId%>', '<%=currType%>', '<%=currName%>')">Edit</a> | 
					<a class="action01" href="javascript:deleteConfigReg('<%=currId%>')">Delete</a> | 
					<a class="action01" href="<%=contextRoot%>/admin/configelements?configRegistryId=<%=currId%>">Elements</a>
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


	<dialog id="newConfigRegDialog">
	<div class="dialog_title_div01">Create Config Registry</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/admin/configregadd"%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Type:</td>
					<td><input type="text" name="type"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okAddConfigReg" class="button02">OK</a> 
			<a id="cancelAddConfigReg" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changeConfigRegDialog">
	<div class="dialog_title_div01">Change Config Registry</div>
	<form id="update_form" name="update_form_name" method="post" action="<%=contextRoot + "/admin/configregupdate"%>">
		<input type="hidden" id="config_reg__configRegistryId" name="configRegistryId" >
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td>Type:</td>
					<td><input id="config_reg__type" type="text" name="type"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input id="config_reg__name" type="text" name="name"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeConfigReg" class="button02">OK</a> 
			<a id="cancelChangeConfigReg" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteConfigRegsDialog">
	<form id="delete_form" method="post" action="<%=contextRoot + "/admin/configregdelete"%>">
		<div class="dialog_title_div01">Delete Config Registries</div>
		<div class="dialog_main_div01">Are you sure you want to delete selected config registries?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteConfigRegs" class="button02">OK</a>
			<a id="cancelDeleteConfigRegs" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteConfigRegDialog">
	<form id="delete_form2" method="post" action="<%=contextRoot + "/admin/configregdelete"%>">
		<input type="hidden" id="delete_form2_configRegistryId" name="configRegistryId">
		<div class="dialog_title_div01">Delete Config Registry</div>
		<div class="dialog_main_div01">Are you sure you want to delete the config registry?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteConfigReg" class="button02">OK</a> 
			<a id="cancelDeleteConfigReg" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

</body>
</html>
