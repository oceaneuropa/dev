<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.component.api.tier3.domainmanagement.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	MachineConfig[] machineConfigs = (MachineConfig[]) request.getAttribute("machineConfigs");
	if (machineConfigs == null) {
		machineConfigs = new MachineConfig[0];
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Domain Management</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
<script type="text/javascript" src="<%=contextRoot + "/views/js/domain_machines.js"%>" defer></script>
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> 
	</div>
	<div class="main_div01">
		<h2>Machines</h2>
		<div class="top_tools_div01">
			<!-- 
			<button onclick="addMachine()">Add</button>
			<button onclick="deleteMachines()">Delete</button>
			<button onclick="location.href='<%=contextRoot + "/domain"%>'">Refresh</button>
			 -->
			<a id="action.addMachine" class="button02">Add</a>
			<a id="action.deleteMachines" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/domain/machines"%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="<%=contextRoot + "/domain/machinedelete"%>">
			<tr>
				<th class="th1" width="11"></th>
				<th class="th1" width="160">Id</th>
				<th class="th1" width="160">Name</th>
				<th class="th1" width="160">IP Address</th>
				<th class="th1" width="160">Actions</th>
			</tr>
			<%
				if (machineConfigs.length == 0) {
			%>
			<tr>
				<td colspan="5">(n/a)</td>
			</tr>
			<%
				} else {
					for (MachineConfig machineConfig : machineConfigs) {
						String id = machineConfig.getId();
						String name = machineConfig.getName();
						String ip = machineConfig.getIpAddress();

						id = StringUtil.get(id);
						name = StringUtil.get(name);
						ip = StringUtil.get(ip);
			%>
			<tr>
				<td class="td1">
					<input type="checkbox" name="id" value="<%=id%>">
				</td>
				<td class="td1"><%=id%></td>
				<td class="td2"><%=name%></td>
				<td class="td2"><%=ip%></td>
				<td class="td1">
					<a class="action01" href="javascript:changeMachine('<%=id%>', '<%=name%>', '<%=ip%>')">Change</a> |
					<a class="action01" href="<%=contextRoot%>/domain/platforms?machineId=<%=id%>">Platforms</a>
					<!-- | <a class="action01" href="javascript:deleteMachine('<%=contextRoot + "/domain/machinedelete"%>', '<%=id%>')">Delete</a> -->
				</td>
			</tr>
			<%
				}
				}
			%>
			</form>
		</table>
	</div>

	<dialog id="newMachineDialog">
	<div class="dialog_title_div01">Add Machine</div>
		<form id="new_form" method="post" action="<%=contextRoot + "/domain/machineadd"%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" name="id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<td>IP Address:</td>
					<td><input type="text" name="ip"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<!-- <button class="button02" type="submit">OK</button> -->
			<!-- <button class="button02" id="cancelAddMachine" type="reset">Cancel</button> -->
			<a class="button02" href="javascript:document.getElementById('new_form').submit();">OK</a>
			<a class="button02b" id="cancelAddMachine" href="javascript:document.getElementById('new_form').reset();">Cancel</a>
		</div>
		</form>
	</dialog>

	<dialog id="changeMachineDialog">
	<div class="dialog_title_div01">Change Machine</div>
		<form id="update_form" method="post" action="<%=contextRoot + "/domain/machineupdate"%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" id="machine_id" name="id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input id="machine_name" type="text" name="name"></td>
				</tr>
				<tr>
					<td>IP Address:</td>
					<td><input id="machine_ip" type="text" name="ip"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<!-- <button type="submit">OK</button> -->
			<!-- <button id="cancelChangeMachine" type="reset">Cancel</button> -->
			<a class="button02" href="javascript:document.getElementById('update_form').submit();">OK</a>
			<a id="cancelChangeMachine" class="button02b" href="javascript:document.getElementById('update_form').reset();">Cancel</a>
		</div>
		</form>
	</dialog>

	<dialog id="deleteMachineDialog">
		<div class="dialog_title_div01">Delete Machine</div>
		<div class="dialog_main_div01" id="deleteMachineDialogMessageDiv">Are you sure you want to delete the machine?</div>
		<div class="dialog_button_div01">
			<!-- <button id="doDeleteMachine">OK</button> -->
			<!-- <button id="cancelDeleteMachine">Cancel</button> -->
			<a id="okDeleteMachine" class="button02">OK</a>
			<a id="cancelDeleteMachine" class="button02b">Cancel</a>
		</div>
	</dialog>

	<dialog id="deleteMachinesDialog">
		<div class="dialog_title_div01">Delete Machine</div>
		<div class="dialog_main_div01" id="deleteMachinesDialogMessageDiv">Are you sure you want to delete selected machines?</div>
		<div class="dialog_button_div01">
			<!-- <button id="doDeleteMachines">OK</button> -->
			<!-- <button id="cancelDeleteMachines">Cancel</button> -->
			<a id="okDeleteMachines" class="button02">OK</a>
			<a id="cancelDeleteMachines" class="button02b">Cancel</a>
		</div>
	</dialog>

</body>
</html>
