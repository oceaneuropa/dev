<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.component.api.tier1.account.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
	UserAccount[] userAccounts = (UserAccount[]) request.getAttribute("userAccounts");
	if (userAccounts == null) {
		userAccounts = new UserAccount[0];
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Registry</title>
<%
	out.print("<link rel=\"stylesheet\" href=\"" + contextRoot + "/views/css/style.css\">");
%>
</head>
<body>
	<div class="div01">
		<h2>User Accounts</h2>
		<table id="table01">
			<tr>
				<th width="100">Id</th>
				<th width="100">Password</th>
				<th width="100">First Name</th>
				<th width="100">Last Name</th>
				<th width="100">Email</th>
				<th width="200">Phone</th>
				<th width="200">Creation Time</th>
				<th width="200">Last Update Time</th>
			</tr>
			<%
				if (userAccounts.length == 0) {
			%>
			<tr>
				<td colspan="8">(n/a)</td>
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
				<td id="td1"><%=userId%></td>
				<td id="td2"><%=password%>s</td>
				<td id="td2"><%=firstName%></td>
				<td id="td2"><%=lastName%></td>
				<td id="td2"><%=email%></td>
				<td id="td2"><%=phone%>s</td>
				<td id="td2"><%=creationTimeStr%></td>
				<td id="td2"><%=lastUpdateTimeStr%></td>
			</tr>
			<%
				}
				}
			%>
		</table>
	</div>
</body>
</html>
