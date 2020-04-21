<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="org.orbit.platform.sdk.*"%>
<%@ page import="org.orbit.component.webconsole.*"%>
<%@ page import="org.orbit.glasscube.api.*"%>
<%
	String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
	String mainContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);

	String username = (String) session.getAttribute(PlatformConstants.SESSION__ORBIT_USERNAME);

	List<GlassCube> cubes = (List) request.getAttribute("cubes");
	if (cubes == null) {
		cubes = new ArrayList<GlassCube>();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Origin</title>

<link rel="stylesheet" href="<%=mainContextRoot + "/views/css/style.css"%>">
<link rel="stylesheet" href="<%=mainContextRoot + "/views/css/main.css"%>">
<style type="text/css">
body {
	background-image: url("<%=mainContextRoot%>/views/images/1440x900/simple-09-1440x900.png");
}

.select01 {
  width: 100%;
  height: 250px;
  padding: 2px 2px;
  margin: 8px 0;
  display: inline-block;
  border: 2px solid #bfbfbf;
  border-radius: 5px;
  box-sizing: border-box;
}

.select01 option{
 	padding: 4px 4px;
 	// background: #cccccc;
    // color: #ffffff;
    // text-shadow: 0 1px 0 rgba(0, 0, 0, 0.4);
    cursor: pointer;
}

</style>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.5.0.min.js"></script>
<script type="text/javascript">
function logout() {
	window.location.href = "<%=mainContextRoot + "/logoutHandler"%>";
}

function instanceOnSelection(selectionElement) {
	// var cubeManagerId = $("option:selected", "#instances").attr("data-cubeManagerId");
	// var cubeId = document.getElementById("instances").value;
	// var cubeId = selectionElement.options[selectionElement.selectedIndex].value;
	var cubeName = selectionElement.options[selectionElement.selectedIndex].text;

	// console.log("cubeManagerId = " + cubeManagerId);
	// console.log("cubeId = " + cubeId);
	// console.log("cubeName = " + cubeName);

	// document.getElementById("join_cubeManagerId").value = cubeManagerId;
	// document.getElementById("join_cubeId").value = cubeId;
	document.getElementById("join_cubeName").value = cubeName;
}

function join() {
	var cubeName = document.getElementById("join_cubeName").value;
	var password = document.getElementById("join_password").value;

	// window.location.href = "<%=mainContextRoot%>/worldPage?cubeName=" + cubeName + "&password=" + password;
	window.location.href = "<%=mainContextRoot%>/OS/" + cubeName;
}

</script>

</head>
<body>
	<jsp:include page="<%=platformContextRoot + "/top_message"%>" />
	<div>
		<div class="title1" style="width: 50%; margin: auto; padding: 50px; text-align: center; user-select: none;">
			Origin
		</div>
		<div class="form_main_div01" style="user-select: none;">
			<table class="form_table01" style="width: 600px; color: #efefef;">
					<tr>
						<td width="200" valign="top" style="padding: 10px 10px;">
							OS Instances: <br/>
							<select class="select01" id="instances" name="rooms" size="12" onchange="instanceOnSelection(this);" >
								<%
								for (int i = 0; i < cubes.size(); i++) {
									GlassCube cube = cubes.get(i);
									String cubeId = cube.getId();
									String cubeName = cube.getName();
									String cubeManagerId = (String) cube.getProperty("cube_manager.id");
								%>
				  				<option value="<%=cubeId%>" data-cubeManagerId="<%=cubeManagerId%>" ><%=cubeName%></option>
								<%
								}
								%>
							</select>
						</td>
						<td width="200" valign="top" style="padding: 10px 10px;">
							<form id="form_join" method="post" action="<%=mainContextRoot + "/join"%>">
								Name:<br />
								<input id="join_cubeName" type="text" name="cubeName" class="text_field_01" size="35"><br />
								Password:<br />
								<input id="join_password" type="password" name="password" class="text_field_01">
							</form>
						</td>
					</tr>
					<tr>
						<td width="200" align="center" colspan="2" style="padding: 10px 10px;">
							<input type="button" class="main_button_01" onclick="create()" value="Create">
							<input type="button" class="main_button_01" onclick="join()" value="Join">
							<input type="button" class="main_button_01" onclick="delete()" value="Delete">
							<input type="button" class="main_button_01" onclick="logout()" value="Exit">
						</td>
					</tr>	
			</table>
		</div>
	</div>
</body>
</html>
