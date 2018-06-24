<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.component.api.tier2.appstore.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
	AppManifest[] appManifests = (AppManifest[]) request.getAttribute("appManifests");
	if (appManifests == null) {
		appManifests = new AppManifest[0];
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>App Store</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<div class="div01">
		<h2>Apps</h2>
		<table id="table01">
			<tr>
				<th width="100">Id</th>
				<th width="100">Type</th>
				<th width="100">Priority</th>
				<th width="100">Name</th>
				<th width="100">Version</th>
				<th width="200">Description</th>
				<th width="100">File Name</th>
				<th width="160">Date Created</th>
				<th width="160">Date Modified</th>
				<th width="100">Actions</th>
			</tr>
			<%
				if (appManifests.length == 0) {
			%>
			<tr>
				<td colspan="10">(n/a)</td>
			</tr>
			<%
				} else {
					for (AppManifest appManifest : appManifests) {
						String id = appManifest.getAppId();
						String type = appManifest.getType();
						int priority = appManifest.getPriority();
						String name = appManifest.getName();
						String version = appManifest.getVersion();
						String desc = appManifest.getDescription();
						String fileName = appManifest.getFileName();
						Date dateCreated = appManifest.getDateCreated();
						Date dateModified = appManifest.getDateModified();

						id = StringUtil.get(id);
						type = StringUtil.get(type);
						name = StringUtil.get(name);
						version = StringUtil.get(version);
						desc = StringUtil.get(desc);
						fileName = StringUtil.get(fileName);
						String dateCreatedStr = (dateCreated != null) ? DateUtil.toString(dateCreated, DateUtil.SIMPLE_DATE_FORMAT2) : "(n/a)";
						String dateModifiedStr = (dateModified != null) ? DateUtil.toString(dateModified, DateUtil.SIMPLE_DATE_FORMAT2) : "(n/a)";
			%>
			<tr>
				<td id="td1"><%=id%></td>
				<td id="td2"><%=type%></td>
				<td id="td2"><%=priority%></td>
				<td id="td2"><%=name%></td>
				<td id="td2"><%=version%></td>
				<td id="td2"><%=desc%></td>
				<td id="td2"><%=fileName%></td>
				<td id="td2"><%=dateCreatedStr%></td>
				<td id="td2"><%=dateModifiedStr%></td>
				<td id="td1"></td>
			</tr>
			<%
				}
				}
			%>
		</table>
	</div>
</body>
</html>
