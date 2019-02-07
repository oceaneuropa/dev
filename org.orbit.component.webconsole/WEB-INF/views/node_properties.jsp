<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, java.net.*, javax.servlet.*"%>
<%@ page import="org.origin.common.util.*"%>
<%@ page import="org.orbit.infra.api.indexes.*, org.orbit.infra.api.extensionregistry.*"%>
<%@ page import="org.orbit.component.api.tier3.domain.*"%>
<%@ page import="org.orbit.component.api.tier3.nodecontrol.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

	MachineConfig machineConfig = (MachineConfig) request.getAttribute("machineConfig");
	PlatformConfig platformConfig = (PlatformConfig) request.getAttribute("platformConfig");
	NodeInfo nodeInfo = (NodeInfo) request.getAttribute("nodeInfo");
	IndexItem nodeIndexItem = (IndexItem) request.getAttribute("nodeIndexItem");
	List<IndexItem> indexItems = (List<IndexItem>) request.getAttribute("indexItems");
	List<ExtensionItem> extensionItems = (List<ExtensionItem>) request.getAttribute("extensionItems");
	Map<String, List<ExtensionItem>> extensionItemMap = (Map<String, List<ExtensionItem>>) request.getAttribute("extensionItemMap");

	String machineName = (machineConfig != null) ? machineConfig.getName() : "n/a";
	String machineId = (machineConfig != null) ? machineConfig.getId() : "";

	String platformName = (platformConfig != null) ? platformConfig.getName() : "n/a";
	String platformId = (platformConfig != null) ? platformConfig.getId() : "";

	String id = (nodeInfo != null) ? nodeInfo.getId() : "";
	String name = (nodeInfo != null) ? nodeInfo.getName() : "";
	Map<String, Object> attributes = (nodeInfo != null) ? nodeInfo.getAttributes() : null;
	if (attributes == null) {
		attributes = new HashMap<String, Object>();
	}
	if (indexItems == null) {
		indexItems = new ArrayList<IndexItem>();
	}
	if (extensionItems == null) {
		extensionItems = new ArrayList<ExtensionItem>();
	}
	if (extensionItemMap == null) {
		extensionItemMap = new TreeMap<String, List<ExtensionItem>>();	
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Domain Management</title>
<link rel="stylesheet" href="<%=contextRoot + "/views/css/style.css"%>">
<link rel="stylesheet" href="../views/css/treetable.css">

<script type="text/javascript" src="<%=contextRoot + "/views/js/node_properties.js"%>" defer></script>
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
		<h2>Configurations</h2>
		<div class="top_tools_div01">
			<a id="action.addAttribute" class="button02">Add</a> 
			<a id="action.deleteAttributes" class="button02">Delete</a> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<form id="main_list" method="post" action="<%=contextRoot + "/domain/nodeattributedelete"%>">
				<input type="hidden" name="machineId" value="<%=machineId%>"> 
				<input type="hidden" name="platformId" value="<%=platformId%>">
				<input type="hidden" name="id" value="<%=id%>">
				<tr>
					<th class="th1" width="12"></th>
					<th class="th1" width="200">Name</th>
					<th class="th1" width="400">Value</th>
					<th class="th1" width="150">Actions</th>
				</tr>
				<%
					if (attributes.isEmpty()) {
				%>
				<tr>
					<td colspan="4">(n/a)</td>
				</tr>
				<%
					} else {
						for (Iterator<String> itor = attributes.keySet().iterator(); itor.hasNext(); ) {
							String attrName = itor.next();
							Object attrValue = attributes.get(attrName);

							Object attrValueForEdit = attrValue;

							int rowNum = 1;
							if (attrValue instanceof String) {
								String valueString = (String) attrValue;
								if (valueString.contains("\r\n")) {
									String[] array = valueString.split("\r\n");
									rowNum = array.length;

									if (valueString.endsWith("\r\n")) {
										rowNum += 1;
									}
									rowNum += 1;

									attrValueForEdit = valueString.replace("\r\n", "\\r\\n");
								}
							}
				%>
				<tr>
					<td class="td1">
						<input type="checkbox" name="name" value="<%=attrName%>">
					</td>
					<td class="td1">
						<%=attrName%>
					</td>
					<td class="td2">
						<% if (rowNum > 1) { %>
							<textarea rows="<%=rowNum%>" cols="100" readonly><%=attrValue%></textarea>
						<% } else { %>
							<%=attrValue%>
						<% } %>
					</td>
					<td class="td1">
						<a class="action01" href="javascript:changeAttribute('<%=attrName%>', '<%=attrValueForEdit%>', '<%=rowNum%>')">Edit</a> | 
						<a class="action01" href="javascript:deleteAttribute('<%=contextRoot + "/domain/nodeattributedelete"%>', '<%=machineId%>', '<%=platformId%>', '<%=id%>', '<%=attrName%>')">Delete</a> 
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

	<div class="main_div01">
		<h2>Properties</h2>
		<div class="top_tools_div01"> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="22.3%">Name</th>
				<th class="th1" width="76.7%">Value</th>
			</tr>
			<%
				if (nodeIndexItem == null || nodeIndexItem.getProperties().isEmpty()) {
			%>
			<tr>
				<td colspan="2">(n/a)</td>
			</tr>
			<%
				} else {
					Map<String, Object> indexItemProperties = nodeIndexItem.getProperties();
					for (Iterator<String> itor = indexItemProperties.keySet().iterator(); itor.hasNext(); ) {
						String propName = itor.next();
						Object propValue = indexItemProperties.get(propName);
			%>
				<tr>
					<td class="td2">
						<%=propName%>
					</td>
					<td class="td2">
						<%=propValue%>
					</td>
				</tr>
			<%
					}
				}
			%>
		</table>
	</div>
	<br/>

	<div class="main_div01">
		<h2>Services</h2>
		<div class="top_tools_div01"> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="main_table01">
			<tr>
				<th class="th1" width="200">Indexer</th>
				<th class="th1" width="150">Type</th>
				<th class="th1" width="150">Name</th>
				<th class="th1" width="100">Status</th>
				<th class="th1" width="400">Properties</th>
			</tr>
			<%
				if (indexItems.isEmpty()) {
			%>
			<tr>
				<td colspan="5">(n/a)</td>
			</tr>
			<%
				} else {
						for (IndexItem indexItem : indexItems) {
							String indexProviderId = indexItem.getIndexProviderId();
							int indexItemId = indexItem.getIndexItemId();
							String indexItemType = indexItem.getType();
							String indexItemName = indexItem.getName();
							Map<String, Object> properties = indexItem.getProperties();

							String props = "";
							if (properties != null) {
								int pIndex = 0;
								for (Iterator<String> propItor = properties.keySet().iterator(); propItor.hasNext();) {
									String propName = propItor.next();
									Object propValue = properties.get(propName);

									if (propName.endsWith("_time") && propValue instanceof Long) {
										Date date = DateUtil.toDate((Long) propValue);
										propValue = DateUtil.toString(date, DateUtil.SIMPLE_DATE_FORMAT2);
									}

									if (pIndex > 0) {
										props += ", <br/>";
									}
									props += propName + " = " + propValue.toString();
									pIndex++;
								}
							}

							boolean isOnline = IndexItemHelper.INSTANCE.isOnline(indexItem);
							String statusText = isOnline ? "Online" : "Offline";
							String statusColor = isOnline ? "#2eb82e" : "#cccccc";
			%>
			<tr>
				<td class="td2"><%=indexProviderId%></td>
				<td class="td2"><%=indexItemType%></td>
				<td class="td2"><%=indexItemName%></td>
				<td class="td1"><font color="<%=statusColor%>"><%=statusText%></font></td>
				<td class="td2"><%=props%></td>
			</tr>
			<%
					}
				}
			%>
		</table>
	</div>
	<br>

	<div class="main_div01">
		<h2>Extensions (<%=extensionItems.size()%>)</h2>
		<div class="top_tools_div01"> 
			<a class="button02" href="<%=contextRoot + "/domain/nodeproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id%>">Refresh</a>
		</div>
		<table class="tree_table01">
			<thead>
				<tr>
					<th width="300" >Extension</th>
					<th width="250" >Name</th>
					<th width="250" >Description</th>
					<th width="250" >Properties</th>
				</tr>
			</thead>
			<tbody>
			<%
				for (Iterator<String> typeIdItor = extensionItemMap.keySet().iterator(); typeIdItor.hasNext(); ) {
					String currExtensionTypeId = typeIdItor.next();
					List<ExtensionItem> currExtensionItems = extensionItemMap.get(currExtensionTypeId);
			%>
				<tr>
					<th colspan="3" style="font-weight: bold;"><%=currExtensionTypeId%> (<%=currExtensionItems.size()%>)</th>
				</tr>
			<%
					for (ExtensionItem extensionItem : currExtensionItems) {
						String extensionId = extensionItem.getExtensionId();
						String extensionName = extensionItem.getName();
						String extensionDesc = extensionItem.getDescription();
						Map<String, Object> extensionProps = extensionItem.getProperties();

						extensionId = StringUtil.get(extensionId, "");
						extensionName = StringUtil.get(extensionName, "");
						extensionDesc = StringUtil.get(extensionDesc, "");
						
						String extensionPropsString = "";
						if (extensionProps != null) {
							int index = 0;
							for (Iterator<String> propNameItor = extensionProps.keySet().iterator(); propNameItor.hasNext(); ) {
								String propName = propNameItor.next();
								Object propValue = extensionProps.get(propName);
								String propValueString = (propValue != null) ? propValue.toString() : "";
								if (index > 0) {
									extensionPropsString += "<br/>";
								}
								extensionPropsString += propName + "=" + propValueString;
								index++;
							}
						}
						
			%>
				<tr>
					<th class="start"><%=extensionId%></th>
					<td><%=extensionName%></td>
					<td><%=extensionDesc%></td>
					<td><%=extensionPropsString%></td>
				</tr>
			<%
					}
				}
			%>
			</tbody>
		</table>
	</div>
	<br/>

	<dialog id="newAttributeDialog">
	<div class="dialog_title_div01">Add Attribute</div>
	<form id="new_form" method="post" action="<%=contextRoot + "/domain/nodeattributeadd"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>">
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Name:</td>
					<td width="75%">
						<input type="text" name="name" class="input01" size="20">
					</td>
				</tr>
				<tr>
					<td>Value:</td>
					<td>
						<textarea name="value" rows="10" cols="100"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okAddAttribute" class="button02" href="javascript:document.getElementById('new_form').submit();">OK</a> 
			<a id="cancelAddAttribute" class="button02b" href="javascript:document.getElementById('new_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="changeAttributeDialog">
	<div class="dialog_title_div01">Set Attribute</div>
	<form id="update_form" method="post" action="<%=contextRoot + "/domain/nodeattributeupdate"%>">
		<input type="hidden" name="machineId" value="<%=machineId%>"> 
		<input type="hidden" name="platformId" value="<%=platformId%>">
		<input type="hidden" name="id" value="<%=id%>">
		<input type="hidden" id="attribute_oldName" name="oldName">
		<div class="dialog_main_div01">
			<table class="dialog_table01">
				<tr>
					<td width="25%">Name:</td>
					<td width="75%">
						<input type="text" id="attribute_name" name="name" class="input01" size="20">
					</td>
				</tr>
				<tr>
					<td>Value:</td>
					<td>
						<textarea id="attribute_value" name="value" rows="10" cols="100"></textarea>
					</td>
				</tr>
			</table>
		</div>
		<div class="dialog_button_div01">
			<a id="okChangeAttribute" class="button02" href="javascript:document.getElementById('update_form').submit();">OK</a> 
			<a id="cancelChangeAttribute" class="button02b" href="javascript:document.getElementById('update_form').reset();">Cancel</a>
		</div>
	</form>
	</dialog>

	<dialog id="deleteAttributeDialog">
	<div class="dialog_title_div01">Delete Attribute</div>
	<div class="dialog_main_div01" id="deleteAttributeDialogMessageDiv">Are you sure you want to delete the attribute?</div>
	<div class="dialog_button_div01">
		<a id="okDeleteAttribute" class="button02">OK</a> 
		<a id="cancelDeleteAttribute" class="button02b">Cancel</a>
	</div>
	</dialog>

	<dialog id="deleteAttributesDialog">
	<div class="dialog_title_div01">Delete Attributes</div>
	<div class="dialog_main_div01" id="deleteAttributesDialogMessageDiv">Are you sure you want to delete selected attributes?</div>
	<div class="dialog_button_div01">
		<a id="okDeleteAttributes" class="button02">OK</a> 
		<a id="cancelDeleteAttributes" class="button02b">Cancel</a>
	</div>
	</dialog>

</body>
</html>
