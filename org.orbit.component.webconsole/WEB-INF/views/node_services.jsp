<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*, org.origin.common.osgi.*"%>
<%@ page import="org.orbit.platform.api.*"%>
<%@ page import="org.orbit.platform.api.ServiceInfo.*"%>
<%@ page import="org.orbit.component.api.tier3.domain.*"%>
<%@ page import="org.orbit.component.api.tier3.nodecontrol.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	// ------------------------------------------------------------------------
	// Get data
	// ------------------------------------------------------------------------
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	MachineConfig machineConfig = (MachineConfig) request.getAttribute("machineConfig");
	PlatformConfig platformConfig = (PlatformConfig) request.getAttribute("platformConfig");
	NodeInfo nodeInfo = (NodeInfo) request.getAttribute("nodeInfo");
	
	// ServiceInfo[] services = (ServiceInfo[]) request.getAttribute("services");
	Map<BundleInfo, List<ServiceInfo>> servicesMap = (Map<BundleInfo, List<ServiceInfo>>) request.getAttribute("servicesMap");

	// ------------------------------------------------------------------------
	// Smooth data
	// ------------------------------------------------------------------------
	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	String platformName = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String platformId = (platformConfig != null) ? platformConfig.getId() : "";

	String id = (nodeInfo != null) ? nodeInfo.getId() : "";
	String name = (nodeInfo != null) ? nodeInfo.getName() : "";

	// if (services == null) {
	//	services = new ServiceInfo[0];
	// }
	if (servicesMap == null) {
		servicesMap = new HashMap<BundleInfo, List<ServiceInfo>>();
	}

	BundleInfo systemBundleInfo = new BundleInfoImpl("system", null);
	
	List<ServiceInfo> systemServices = servicesMap.get(systemBundleInfo);
	if (systemServices == null) {
		systemServices = new ArrayList<ServiceInfo>();
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Services</title>
<link rel="stylesheet" href="../views/css/style.css">
<link rel="stylesheet" href="../views/css/treetable.css">

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript" src="<%=contextRoot + "/views/js/node_services.js"%>"></script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_menu"%>" />
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div class="top_breadcrumbs_div01">
		<a href="<%=contextRoot%>/domain/machines">Machines</a> > 
		<a href="<%=contextRoot%>/domain/platforms?machineId=<%=machineId%>">Platforms</a> (of <%=machineName%>)>
		<a href="<%=contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId%>">Nodes</a> (of Platform [<%=platformName%>]) >
		<!-- <a href="<%=contextRoot + "/domain/nodeproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>"><%=name%></a> -->
		Node [<%=name%>]
	</div>

	<div class="main_div01">
		<h2>Services</h2>
		<div class="top_tools_div01">  
			<a id="actionStartServices" class="button02" onClick="onPlatformServiceAction('start', '<%=contextRoot + "/domain/nodeserviceaction"%>')">Start</a> 
			<a id="actionStopServices" class="button02" onClick="onPlatformServiceAction('stop', '<%=contextRoot + "/domain/nodeserviceaction"%>')">Stop</a> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeservices?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Refresh</a>
		</div>
		<br/>
		<br/>

		<h3>System</h3>
		<table class="main_table01">
			<form id="main_list" method="post" action="">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<input type="hidden" name="id" value="<%=id%>">
				<input id ="main_list__action" type="hidden" name="action" value="">
				<tr>
					<th class="th1" width="10">
						<input type="checkbox" onClick="toggleSelection(this, 'sid')" />
					</th>
					<th class="th1" width="400">Name</th>
					<th class="th1" width="150">Status</th>
					<th class="th1" width="150">Actions</th>
				</tr>
				<%
					// if (services.length == 0) {
					if (systemServices.isEmpty()) {
				%>
				<tr>
					<td colspan="4">(n/a)</td>
				</tr>
				<%
					} else {
						for (ServiceInfo service : systemServices) {
							String currProgramId = service.getProgramId();
							String currProgramVersion = service.getProgramVersion();

							int currSID = service.getSID();
							String currName = service.getName();
							String currDesc = service.getDescription();
							boolean currAutoStart = service.isAutoStart();
							RUNTIME_STATE currRuntimeState = service.getRuntimeState();
							String currRuntimeStatelabel = currRuntimeState.getLabel();

							if (currName == null) {
								currName = "";
							}
							if (currDesc == null) {
								currDesc = "";
							}
							
							boolean isStarted = (currRuntimeState != null && currRuntimeState.isStarted()) ? true : false;
							String statusColor = isStarted ? "#2eb82e" : "#cccccc";

							String descColor = "#aaaaaa";
				%>
				<tr>
					<td class="td1"><input type="checkbox" name="sid" value="<%=currSID%>"></td>
					<td class="td2">
						<%=currName%>
						<% if (!currDesc.isEmpty()) { %>
						<br/>
						<font color="<%=descColor%>"><%=currDesc%></font>
						<% } %>
					</td>
					<td class="td1">
						<font color="<%=statusColor%>">
							<%=currRuntimeStatelabel%>
						</font>
					</td>
					<td class="td1">
						<a class="action01" href="javascript:startService('<%=currSID%>')">Start</a> | 
						<a class="action01" href="javascript:stopService('<%=currSID%>')">Stop</a> | 
						<a class="action01" href="<%=contextRoot%>/domain/nodeserviceproperties?machineId=<%=machineId%>&platformId=<%=platformId%>&id=<%=id%>&sid=<%=currSID%>">Properties</a>
					</td>
				</tr>
				<%
						}
					}
				%>
			</form>
		</table>

		<%
		for(Iterator<BundleInfo> bundleInfoItor = servicesMap.keySet().iterator(); bundleInfoItor.hasNext(); ) {
			BundleInfo currProgram = bundleInfoItor.next();	
			if ("system".equals(currProgram.getSymbolicName())) {
				continue;
			}

			String currProgramId = currProgram.getSymbolicName();
			String currProgramVersion = currProgram.getVersion();
			String programLable = currProgramId;
			if (currProgramVersion != null && !currProgramVersion.isEmpty()) {
				programLable += " (" + currProgramVersion + ")";
			}

			List<ServiceInfo> currServices = servicesMap.get(currProgram);
			if (currServices == null) {
				currServices = new ArrayList<ServiceInfo>();
			}
		%>

		<h3><%=programLable%></h3>
		<table class="main_table01">
			<form id="main_list" method="post" action="">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<input type="hidden" name="id" value="<%=id%>">
				<input id ="main_list__action" type="hidden" name="action" value="">
				<tr>
					<th class="th1" width="10">
						<input type="checkbox" onClick="toggleSelection(this, 'sid', 'data-program', '<%=programLable%>')" />
					</th>
					<th class="th1" width="400">Name</th>
					<th class="th1" width="150">Status</th>
					<th class="th1" width="150">Actions</th>
				</tr>
				<%
					// if (services.length == 0) {
					if (systemServices.isEmpty()) {
				%>
				<tr>
					<td colspan="4">(n/a)</td>
				</tr>
				<%
					} else {
						for (ServiceInfo service : currServices) {
							int currSID = service.getSID();
							String currName = service.getName();
							String currDesc = service.getDescription();
							boolean currAutoStart = service.isAutoStart();
							RUNTIME_STATE currRuntimeState = service.getRuntimeState();
							String currRuntimeStatelabel = currRuntimeState.getLabel();

							if (currName == null) {
								currName = "";
							}
							if (currDesc == null) {
								currDesc = "";
							}
							
							boolean isStarted = (currRuntimeState != null && currRuntimeState.isStarted()) ? true : false;
							String statusColor = isStarted ? "#2eb82e" : "#cccccc";

							String descColor = "#aaaaaa";
				%>
				<tr>
					<td class="td1">
						<input type="checkbox" name="sid" data-program="<%=programLable%>" value="<%=currSID%>">
					</td>
					<td class="td2">
						<%=currName%>
						<% if (!currDesc.isEmpty()) { %>
						<br/>
						<font color="<%=descColor%>"><%=currDesc%></font>
						<% } %>
					</td>
					<td class="td1">
						<font color="<%=statusColor%>">
							<%=currRuntimeStatelabel%>
						</font>
					</td>
					<td class="td1">
						<a class="action01" href="javascript:startService('<%=currSID%>')">Start</a> | 
						<a class="action01" href="javascript:stopService('<%=currSID%>')">Stop</a> | 
						<a class="action01" target="_blank" href="<%=contextRoot%>/domain/nodeserviceproperties?machineId=<%=machineId%>&platformId=<%=platformId%>&id=<%=id%>&sid=<%=currSID%>">Properties</a>
					</td>
				</tr>
				<%
						}
					}
				%>
			</form>
		</table>
		<%
		}
		%>

	</div>
	<br/>

	<dialog id="serviceActionDialog">
	<div class="dialog_title_div01" id="serviceActionDialogTitleDiv" >{Action} Services</div>
	<div class="dialog_main_div01" id="serviceActionDialogMessageDiv">Are you sure you want to {action} the services?</div>
	<div class="dialog_button_div01">
		<a id="okServiceAction" class="button02">OK</a> 
		<a id="cancelServiceAction" class="button02b">Cancel</a>
	</div>
	</dialog>

	<dialog id="startServiceDialog">
	<form id="start_service_form" method="post" action="<%=contextRoot + "/domain/nodeserviceaction"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>"> 
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" name="action" value="start">
		<input type="hidden" id="start_service_form_sid" name="sid">
		<div class="dialog_title_div01">Start Service</div>
		<div class="dialog_main_div01">Are you sure you want to start the service?</div>
		<div class="dialog_button_div01">
			<a id="okStartService" class="button02">OK</a> 
			<a id="cancelStartService" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="stopServiceDialog">
	<form id="stop_service_form" method="post" action="<%=contextRoot + "/domain/nodeserviceaction"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>"> 
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" name="action" value="stop">
		<input type="hidden" id="stop_service_form_sid" name="sid">
		<div class="dialog_title_div01">Stop Service</div>
		<div class="dialog_main_div01">Are you sure you want to stop the service?</div>
		<div class="dialog_button_div01">
			<a id="okStopService" class="button02">OK</a> 
			<a id="cancelStopService" class="button02b">Cancel</a>
		</div>
	</form>
	</dialog>

</body>
</html>
