<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.orbit.platform.webconsole.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<%@ page import="org.origin.common.util.*"%>
<%
	String contextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	Map<String, List<IndexItem>> indexerIdToIndexItems = (Map<String, List<IndexItem>>) request.getAttribute("indexerIdToIndexItems");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Index Services</title>
<%
	out.print("<link rel=\"stylesheet\" href=\"" + contextRoot + "/views/css/style.css\">");
%>
</head>
<body>
	<jsp:include page="<%=contextRoot + "/top_menu"%>" />
	<%
		for (Iterator<String> itor = indexerIdToIndexItems.keySet().iterator(); itor.hasNext();) {
			String indexerId = itor.next();
			List<IndexItem> indexItems = indexerIdToIndexItems.get(indexerId);
	%>
	<div class="div01">
		<h2>
			Indexer [<%=indexerId%>]
		</h2>
		<table id="table01">
			<tr>
				<th width="50">Id</th>
				<th width="200">Type</th>
				<th width="200">Name</th>
				<th width="650">Properties</th>
			</tr>
			<%
				if (indexItems.isEmpty()) {
			%>
			<tr>
				<td colspan="4">(n/a)</td>
			</tr>
			<%
				} else {
						for (IndexItem indexItem : indexItems) {
							int id = indexItem.getIndexItemId();
							String type = indexItem.getType();
							String name = indexItem.getName();
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
			%>
			<tr>
				<td id="td1"><%=id%></td>
				<td id="td2"><%=type%>s</td>
				<td id="td2"><%=name%></td>
				<td id="td2"><%=props%></td>
			</tr>
			<%
				}
					}
			%>
		</table>
	</div>
	<br>
	<%
		}
	%>
</body>
</html>
