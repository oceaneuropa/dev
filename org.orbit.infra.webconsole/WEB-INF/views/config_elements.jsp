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

	IConfigElement[] configElements = (IConfigElement[]) request.getAttribute("configElements");
	if (configElements == null) {
		configElements = new IConfigElement[0];
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Config Registry</title>
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
			<%
				if (!parentConfigElements.isEmpty()) {
					String parentElementId = "";
					String parentElementName = "Unknown";
					String grandParentElementId = "";
					IConfigElement parentConfigElement = parentConfigElements.get(parentConfigElements.size() - 1);
					if (parentConfigElement != null) {
						parentElementId = parentConfigElement.getElementId();
						parentElementName = parentConfigElement.getName();
						grandParentElementId = parentConfigElement.getParentElementId();
					}
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
				<th class="th1" width="150">Name</th>
				<th class="th1" width="150">Attributes</th>
				<th class="th1" width="100">Date Created</th>
				<th class="th1" width="100">Data Modified</th>
				<th class="th1" width="100">Actions</th>
			</tr>
			<%
				if (configElements.length == 0) {
			%>
			<tr>
				<td colspan="5">(n/a)</td>
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
				<td class="td2"><%=currName%></td>
				<td class="td2"><%=currAttrStr%></td>
				<td class="td1"><%=currDateCreatedStr%></td>
				<td class="td1"><%=currDateModifiedStr%></td>
				<td class="td1">
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

</body>
</html>
