<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="application/xml" pageEncoding="UTF-8"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.component.api.tier2.appstore.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	AppManifest[] appManifests = (AppManifest[]) request.getAttribute("appManifests");
	if (appManifests == null) {
		appManifests = new AppManifest[0];
	}
%>
<data>
    <table width="100%">
		<tr>
			<th width="12">
			</th>
			<th width="150">Name</th>
			<th width="150">Type</th>
			<th width="150">Id/Version</th>
		</tr>
    	<%
			if (appManifests.length == 0) {
		%>
		<tr>
			<td colspan="4">(n/a)</td>
		</tr>
		<%
			} else {
				for (AppManifest appManifest : appManifests) {
					String appId = appManifest.getAppId();
					String appVersion = appManifest.getAppVersion();
					String type = appManifest.getType();
					String name = appManifest.getName();
		%>
			<tr>
				<td>
					<input type="checkbox" name="appId_appVersion" value="<%=appId + "|" + appVersion%>" />
				</td>
				<td><%=name%></td>
				<td><%=type%></td>
				<td><%=appId + "_" + appVersion%></td>
			</tr>
		<%
				}
			}
		%>
    </table>
</data>
