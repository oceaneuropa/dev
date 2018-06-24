<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.component.api.tier1.account.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	// String message = (String) request.getAttribute("message");
	UserAccount[] userAccounts = (UserAccount[]) request.getAttribute("userAccounts");
	if (userAccounts == null) {
		userAccounts = new UserAccount[0];
	}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Registry</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="main_div01">
		<h2>User Accounts</h2>
		<div class="top_tools_div01">
			<button onclick="addUser()">Add</button>
			<button onclick="deleteUsers()">Delete</button>
		</div>
		<table class="main_table01">
			<form id="main_list">
			<tr>
				<th class="th1" width="20"></th>
				<th class="th1" width="150">Id</th>
				<th class="th1" width="150">Name</th>
				<th class="th1" width="150">Email</th>
				<th class="th1" width="100">Phone</th>
				<th class="th1" width="250">Details</th>
				<th class="th1" width="100">Actions</th>
			</tr>
			<%
				if (userAccounts.length == 0) {
			%>
			<tr>
				<td colspan="7">(n/a)</td>
			</tr>
			<%
				} else {
					for (UserAccount userAccount : userAccounts) {
						String userId = userAccount.getUserId();
						String password = userAccount.getPassword();
						String firstName = userAccount.getFirstName();
						String lastName = userAccount.getLastName();
						String email = userAccount.getEmail();
						String phone = userAccount.getPhone();
						Date creationTime = userAccount.getCreationTime();
						Date lastUpdateTime = userAccount.getLastUpdateTime();

						userId = StringUtil.get(userId);
						password = StringUtil.get(password);
						firstName = StringUtil.get(firstName);
						lastName = StringUtil.get(lastName);
						email = StringUtil.get(email);
						phone = StringUtil.get(phone);
						String creationTimeStr = (creationTime != null) ? DateUtil.toString(creationTime, DateUtil.SIMPLE_DATE_FORMAT2) : "(n/a)";
						String lastUpdateTimeStr = (lastUpdateTime != null) ? DateUtil.toString(lastUpdateTime, DateUtil.SIMPLE_DATE_FORMAT2) : "(n/a)";
			%>
			<tr>
				<td class="td1">
					<input type="checkbox" name="id" value="<%=userId%>">
				</td>
				<td class="td2">
					<%=userId%>
				</td>
				<td class="td2"><%=firstName%>&nbsp;&nbsp;<%=lastName%></td>
				<td class="td2"><%=email%></td>
				<td class="td1"><%=phone%></td>
				<td class="td1">
					<details>
  						<summary>More</summary>
  						<div style="text-align: left;">
  						<table class="detail_table01" style="font-size: 12px;">
  							<tr>
  								<td>Password: </td>
  								<td><%=password%></td>
  							</tr>
  							<tr>
  								<td>Creation Time: </td>
  								<td><%=creationTimeStr%></td>
  							</tr>
  							<tr>
  								<td>Last Update Time: </td>
  								<td><%=lastUpdateTimeStr%></td>
  							</tr>
  						</table>
  						</div>
					</details>
				</td>
				<td class="td1">
					<a href="javascript:changeUser('<%=userId%>', '<%=password%>', '<%=firstName%>', '<%=lastName%>', '<%=email%>', '<%=phone%>')">Change</a> | 
					<a href="javascript:deleteUser('<%=userId%>')">Delete</a>
				</td>
			</tr>
			<%
				}
				}
			%>
			</form>
		</table>
	</div>

	<dialog id="newUserDialog">
	<div class="dialog_title_div01">Add User</div>
		<form method="post" action="<%=contextRoot + "/userregistryadd"%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" name="id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type="password" name="password"></td>
				</tr>
				<tr>
					<td>First Name:</td>
					<td><input type="text" name="firstName"></td>
				</tr>
				<tr>
					<td>Last Name:</td>
					<td><input type="text" name="lastName"></td>
				</tr>
				<tr>
					<td>Email:</td>
					<td><input type="text" name="email"></td>
				</tr>
				<tr>
					<td>Phone:</td>
					<td><input type="text" name="phone"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<button type="submit">OK</button>
			<button id="cancelAddUser" type="reset">Cancel</button>
		</div>
		</form>
	</dialog>

	<dialog id="changeUserDialog">
	<div class="dialog_title_div01">Change User</div>
		<form method="post" action="<%=contextRoot + "/userregistryupdate"%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" id="user_id" name="id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input id="user_password" type="password" name="password"></td>
				</tr>
				<tr>
					<td>First Name:</td>
					<td><input id="user_firstName" type="text" name="firstName"></td>
				</tr>
				<tr>
					<td>Last Name:</td>
					<td><input id="user_lastName" type="text" name="lastName"></td>
				</tr>
				<tr>
					<td>Email:</td>
					<td><input id="user_email" type="text" name="email"></td>
				</tr>
				<tr>
					<td>Phone:</td>
					<td><input id="user_phone" type="text" name="phone"></td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<button type="submit">OK</button>
			<button id="cancelChangeUser" type="reset">Cancel</button>
		</div>
		</form>
	</dialog>

	<dialog id="deleteUserDialog">
		<div class="dialog_title_div01">Delete User</div>
		<div class="dialog_main_div01" id="deleteUserDialogMessageDiv">Are you sure you want to delete the user?</div>
		<div class="dialog_button_div01">
			<button id="doDeleteUser">OK</button>
			<button id="cancelDeleteUser">Cancel</button>
		</div>
	</dialog>

	<dialog id="deleteUsersDialog">
		<div class="dialog_title_div01">Delete User</div>
		<div class="dialog_main_div01" id="deleteUsersDialogMessageDiv">Are you sure you want to delete selected users?</div>
		<div class="dialog_button_div01">
			<button id="doDeleteUsers">OK</button>
			<button id="cancelDeleteUsers">Cancel</button>
		</div>
	</dialog>

<script>
	function addUser() {
		var addUserDialog = document.getElementById('newUserDialog');
		addUserDialog.showModal();
	}

	function changeUser(userId, password, firstName, lastName, email, phone) {
		document.getElementById("user_id").setAttribute('value',userId);
		document.getElementById("user_password").setAttribute('value',password);
		document.getElementById("user_firstName").setAttribute('value',firstName);
		document.getElementById("user_lastName").setAttribute('value',lastName);
		document.getElementById("user_email").setAttribute('value',email);
		document.getElementById("user_phone").setAttribute('value',phone);

		var changeUserDialog = document.getElementById('changeUserDialog');
		changeUserDialog.showModal();
	}

	function deleteUser(userId) {
		var actionURL = "<%=contextRoot + "/userregistrydelete"%>";
		var dialog = document.getElementById('deleteUserDialog');

		var messageDiv = document.getElementById('deleteUserDialogMessageDiv');
		messageDiv.innerHTML="Are you sure you want to delete user '"+userId+"'?";

		var okButton = document.getElementById('doDeleteUser');
		okButton.addEventListener('click', function() {
			var form = document.createElement("form");
			form.setAttribute("method", "post");
			form.setAttribute("action", actionURL);

			var idField = document.createElement("input");
			idField.setAttribute("type", "hidden");
			idField.setAttribute("name", "id");
			idField.setAttribute("value", userId);

			form.appendChild(idField);

			document.body.appendChild(form);
			form.submit();
			document.body.removeChild(form);
		});

		dialog.showModal();
	}

	function deleteUsers() {
		var actionURL = "<%=contextRoot + "/userregistrydelete"%>";
		var dialog = document.getElementById('deleteUsersDialog');

		var messageDiv = document.getElementById('deleteUsersDialogMessageDiv');
		messageDiv.innerHTML="Are you sure you want to delete selected users?";

		var okButton = document.getElementById('doDeleteUsers');
		okButton.addEventListener('click', function() {
			var form = document.getElementById("main_list");
			form.setAttribute("method", "post");
			form.setAttribute("action", actionURL);
			form.submit();
		});

		dialog.showModal();
	}

	(function() {
		var newUserDialog = document.getElementById('newUserDialog');
		var cancelAddUserButton = document.getElementById('cancelAddUser');
		cancelAddUserButton.addEventListener('click', function() {
			newUserDialog.close();
		});

		var changeUserDialog = document.getElementById('changeUserDialog');
		var cancelChangeUserButton = document.getElementById('cancelChangeUser');
		cancelChangeUserButton.addEventListener('click', function() {
			changeUserDialog.close();
		});

		var deleteUserDialog = document.getElementById('deleteUserDialog');
		var cancelDeleteUserButton = document.getElementById('cancelDeleteUser');
		cancelDeleteUserButton.addEventListener('click', function() {
			deleteUserDialog.close();
		});

		var deleteUsersDialog = document.getElementById('deleteUsersDialog');
		var cancelDeleteUsersButton = document.getElementById('cancelDeleteUsers');
		cancelDeleteUsersButton.addEventListener('click', function() {
			document.getElementById("main_list").reset();
			deleteUsersDialog.close();
		});
	})();
</script>
</body>
</html>