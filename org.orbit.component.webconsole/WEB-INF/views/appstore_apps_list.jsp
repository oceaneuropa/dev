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
<script type="text/javascript" src="<%=contextRoot + "/views/js/appstore_apps.js"%>" defer></script>
</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="main_div01">
		<h2>Apps</h2>
		<div class="top_tools_div01">
			<a id="action.addApp" class="button02">Add</a>
			<a id="action.deleteApps" class="button02">Delete</a>
			<a class="button02" href="<%=contextRoot + "/appstore/apps"%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="<%=contextRoot + "/appstore/appdelete"%>">
			<tr>
				<th class="th1" width="12">
					<input type="checkbox" onClick="toggleSelection(this, 'appId_appVersion')" />
				</th>
				<th class="th1" width="100">Type</th>
				<th class="th1" width="150">Id/Version</th>
				<th class="th1" width="150">Name</th>
				<th class="th1" width="200">File Name</th>
				<th class="th1" width="100">Actions</th>
			</tr>
			<%
				if (appManifests.length == 0) {
			%>
			<tr>
				<td colspan="6">(n/a)</td>
			</tr>
			<%
				} else {
					for (AppManifest appManifest : appManifests) {
						int id = appManifest.getId();
						String appId = appManifest.getAppId();
						String appVersion = appManifest.getAppVersion();
						String type = appManifest.getType();
						String name = appManifest.getName();
						String fileName = appManifest.getFileName();
						long fileLength = appManifest.getFileLength();
						String desc = appManifest.getDescription();
						// Date dateCreated = appManifest.getDateCreated();
						// Date dateModified = appManifest.getDateModified();

						appId = StringUtil.get(appId);
						appVersion = StringUtil.get(appVersion);
						type = StringUtil.get(type);
						name = StringUtil.get(name);
						fileName = StringUtil.get(fileName);
						desc = StringUtil.get(desc);

						// String dateCreatedStr = (dateCreated != null) ? DateUtil.toString(dateCreated, DateUtil.SIMPLE_DATE_FORMAT2) : "(n/a)";
						// String dateModifiedStr = (dateModified != null) ? DateUtil.toString(dateModified, DateUtil.SIMPLE_DATE_FORMAT2) : "(n/a)";
			%>
			<tr>
				<td class="td1">
					<input type="checkbox" name="appId_appVersion" value="<%=appId + "|" + appVersion%>">
				</td>
				<td class="td1"><%=type%></td>
				<td class="td2"><%=appId%>_<%=appVersion%></td>
				<td class="td2"><%=name%></td>
				<td class="td2">
					<% if (fileLength > 0) { %>
						<a class="action01" href="<%=contextRoot%>/appstore/appdownload?appId=<%=appId%>&appVersion=<%=appVersion%>" target="_blank"><%=fileName%></a>
					<% } else { %>
						<%=fileName%>
					<% } %>
				</td>
				<td class="td1">
					<a class="action01" href="javascript:changeApp('<%=id%>', '<%=appId%>', '<%=appVersion%>', '<%=type%>', '<%=name%>', '<%=fileName%>', '<%=desc%>')">Edit</a> 
					<a class="action01" href="<%=contextRoot%>/appstore/appproperties?appId=<%=appId%>&appVersion=<%=appVersion%>">Properties</a>
					<a class="action01" href="javascript:uploadApp('<%=id%>', '<%=appId%>', '<%=appVersion%>')">Upload</a>
					<!-- 
					<a class="action01" href="<%=contextRoot%>/appstore/appdownload?appId=<%=appId%>&appVersion=<%=appVersion%>" target="_blank">Download</a>
					 -->
				</td>
			</tr>
			<%
					}
				}
			%>
			</form>
		</table>
	</div>

	<dialog id="newAppDialog">
	<div class="dialog_title_div01">Add App</div>
		<form id="new_form" method="post" action="<%=contextRoot + "/appstore/appadd"%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" name="id" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Version:</td>
					<td><input type="text" name="version" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Type:</td>
					<td><input type="text" name="type"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<td>File Name:</td>
					<td><input type="text" name="fileName"></td>
				</tr>
				<tr>
					<td>Description:</td>
					<td>
						<textarea name="desc" rows="10" cols="100"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a class="button02" href="javascript:document.getElementById('new_form').submit();">OK</a>
			<a class="button02b" id="cancelAddApp" href="javascript:document.getElementById('new_form').reset();">Cancel</a>
		</div>
		</form>
	</dialog>

	<dialog id="changeAppDialog">
	<div class="dialog_title_div01">Change App</div>
		<form id="update_form" method="post" action="<%=contextRoot + "/appstore/appupdate"%>">
			<input id="record_id" type="hidden" name="id">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Id:</td>
					<td width="75%"><input type="text" id="app_id" name="appId" class="input01" size="35"></td>
				</tr>
				<tr>
					<td>Version:</td>
					<td><input id="app_version" type="text" name="appVersion"></td>
				</tr>
				<tr>
					<td>Type:</td>
					<td><input id="app_type" type="text" name="type"></td>
				</tr>
				<tr>
					<td>Name:</td>
					<td><input id="app_name" type="text" name="name"></td>
				</tr>
				<tr>
					<td>File Name:</td>
					<td><input id="app_fileName" type="text" name="fileName"></td>
				</tr>
				<tr>
					<td>Description:</td>
					<td>
						<textarea id="app_desc" name="desc" rows="10" cols="100"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a class="button02" href="javascript:document.getElementById('update_form').submit();">OK</a>
			<a id="cancelChangeApp" class="button02b" href="javascript:document.getElementById('update_form').reset();">Cancel</a>
		</div>
		</form>
	</dialog>

	<dialog id="uploadAppDialog">
	<div class="dialog_title_div01">Upload App File</div>
		<form id="upload_form" method="post" action="<%=contextRoot + "/appstore/appupload"%>" enctype="multipart/form-data">
		<input id="upload_id" type="hidden" name="id">
		<input id="upload_app_id" type="hidden" name="appId">
		<input id="upload_app_version" type="hidden" name="appVersion">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<input type="file" name="uploadFile" />
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a class="button02" href="javascript:document.getElementById('upload_form').submit();">OK</a>
			<a id="cancelUploadApp" class="button02b" href="javascript:document.getElementById('upload_form').reset();">Cancel</a>
		</div>
		</form>
	</dialog>

	<dialog id="deleteAppDialog">
		<div class="dialog_title_div01">Delete App</div>
		<div class="dialog_main_div01" id="deleteAppDialogMessageDiv">Are you sure you want to delete the app?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteApp" class="button02">OK</a>
			<a id="cancelDeleteApp" class="button02b">Cancel</a>
		</div>
	</dialog>

	<dialog id="deleteAppsDialog">
		<div class="dialog_title_div01">Delete Apps</div>
		<div class="dialog_main_div01" id="deleteAppsDialogMessageDiv">Are you sure you want to delete selected apps?</div>
		<div class="dialog_button_div01">
			<a id="okDeleteApps" class="button02">OK</a>
			<a id="cancelDeleteApps" class="button02b">Cancel</a>
		</div>
	</dialog>

</body>
</html>
