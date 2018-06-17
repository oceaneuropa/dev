<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.orbit.infra.api.indexes.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JSP - Hello World Tutorial - Programmer Gate</title>
</head>
<body>
	<h3>My name is ${name}</h3>
	<!-- OR -->
	<h3>
		My name is
		<%=request.getAttribute("name")%></h3>
	<%="Hello World!"%>
	<%
		Date date = new Date();
		out.print("<h2 align=\"center\">" + date.toString() + "</h2>");
	%>

	<%
		Map<String, List<IndexItem>> indexerIdToIndexItems = (Map<String, List<IndexItem>>) request.getAttribute("indexerIdToIndexItems");
		for (Iterator<String> itor = indexerIdToIndexItems.keySet().iterator(); itor.hasNext();) {
			String indexerId = itor.next();
			List<IndexItem> indexItems = indexerIdToIndexItems.get(indexerId);
	%>
	<table>
		<tr>
			<td>Id</td>
			<td>Type</td>
			<td>Name</td>
		</tr>
		<%
			for (IndexItem indexItem : indexItems) {
					int id = indexItem.getIndexItemId();
					String type = indexItem.getType();
					String name = indexItem.getName();
		%>
		<tr>
			<td><%=id%></td>
			<td><%=type%></td>
			<td><%=name%></td>
		</tr>
		<%
			}
		%>
	</table>
	<br>
	<%
		}
	%>
</body>
</html>
