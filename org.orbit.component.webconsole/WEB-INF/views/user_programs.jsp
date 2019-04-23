<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.component.api.tier1.account.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%@ page import="org.orbit.spirit.model.userprograms.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	String accountId = (String) request.getAttribute("accountId");
	String username = (String) request.getAttribute("username");

	List<UserProgram> userPrograms = null;
	UserPrograms userProgramsObj = (UserPrograms) request.getAttribute("userPrograms");
	if (userProgramsObj != null) {
		userPrograms = userProgramsObj.getChildren();
	}
	if (userPrograms == null) {
		userPrograms = new ArrayList<UserProgram>();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Programs</title>

<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
<link rel="stylesheet" href="<%=contextRoot + "/views/css/treetable.css"%>">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/user_programs.js"%>"></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />

	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/useraccounts">User Accounts</a> >
		User [<%=username%>] (Account Id: '<%=accountId%>')
	</div>

	<div class="main_div01">
		<h2>User Programs</h2>
		<div class="top_tools_div01">
			<a id="actionAddProgram" class="button02">Add</a> 
			<a id="actionRemovePrograms" class="button02" onClick="onProgramAction('remove', '<%=contextRoot + "/userprogramaction"%>')">Remove</a>
			<a class="button02" href="<%=contextRoot + "/userprograms?accountId=" + accountId%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="">
			<input type="hidden" name="accountId" value="<%=accountId%>">
			<input id ="main_list__action" type="hidden" name="action" value="">
			<tr>
				<th class="th1" width="11">
					<input type="checkbox" onClick="toggleSelection(this, 'id_version')" />
				</th>
				<th class="th1" width="250">Id</th>
				<th class="th1" width="250">Version</th>
				<th class="th1" width="150">Actions</th>
			</tr>
			<%
				if (userPrograms.isEmpty()) {
			%>
			<tr>
				<td colspan="4">(n/a)</td>
			</tr>
			<%
				} else {
					for (UserProgram userProgram : userPrograms) {
						String programId = userProgram.getId();
						String programVersion = userProgram.getVersion();
			%>
			<tr>
				<td class="td1">
					<input type="checkbox" name="id_version" value="<%=programId + "_" + programVersion%>">
				</td>
				<td class="td2">
					<%=programId%>
				</td>
				<td class="td2">
					<%=programVersion%>
				</td>
				<td class="td1">
					<a class="action01" href="javascript:changeProgram('<%=programId%>', '<%=programVersion%>')">Edit</a> 
				</td>
			</tr>
			<%
				}
				}
			%>
			</form>
		</table>
	</div>

	<dialog id="programsSelectionDialog">
	<div class="dialog_title_div01">Programs</div>
	<form id="programs_add_form" method="post" action="<%=contextRoot + "/userprogramadd"%>">
		<input type="hidden" name="accountId" value="<%=accountId%>">
		<div id="programsSelectionDiv" class="dialog_main_div02">
		</div>
		<div class="dialog_button_div01">
			<a id="okAddProgram" class="button02" href="javascript:document.getElementById('programs_add_form').submit();">OK</a> 
			<a id="cancelAddProgram" class="button02b" href="javascript:document.getElementById('programs_add_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="programActionDialog">
	<div class="dialog_title_div01" id="programActionDialogTitleDiv" >{Action} Programs</div>
	<div class="dialog_main_div01" id="programActionDialogMessageDiv">Are you sure you want to {action} the programs?</div>
	<div class="dialog_button_div01">
		<a id="okProgramAction" class="button02">OK</a> 
		<a id="cancelProgramAction" class="button02b">Cancel</a>
	</div>
	</dialog>

</body>
</html>