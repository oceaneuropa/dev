/*
 * https://stackoverflow.com/questions/386281/how-to-implement-select-all-check-box-in-html
 */
function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}

function addNode() {
	document.getElementById('newNodeDialog').showModal();
}

function changeNode(id, name, typeId) {
	document.getElementById("node_id").setAttribute('value', id);
	document.getElementById("node_name").setAttribute('value', name);
	document.getElementById("node_typeId").setAttribute('value', typeId);
	document.getElementById('changeNodeDialog').showModal();
}

function startNode(actionURL, machineId, platformId, id) {
	document.getElementById('startNodeDialogMessageDiv').innerHTML = "Are you sure you want to start node '" + id + "'?";

	document.getElementById('okStartNode').addEventListener('click', function() {
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", actionURL);

		var machineIdField = document.createElement("input");
		machineIdField.setAttribute("type", "hidden");
		machineIdField.setAttribute("name", "machineId");
		machineIdField.setAttribute("value", machineId);

		var platformIdField = document.createElement("input");
		platformIdField.setAttribute("type", "hidden");
		platformIdField.setAttribute("name", "platformId");
		platformIdField.setAttribute("value", platformId);

		var idField = document.createElement("input");
		idField.setAttribute("type", "hidden");
		idField.setAttribute("name", "id");
		idField.setAttribute("value", id);

		form.appendChild(machineIdField);
		form.appendChild(platformIdField);
		form.appendChild(idField);

		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);
	});

	document.getElementById('startNodeDialog').showModal();
}

function stopNode(actionURL, machineId, platformId, id) {
	document.getElementById('stopNodeDialogMessageDiv').innerHTML = "Are you sure you want to stop node '" + id + "'?";

	document.getElementById('okStopNode').addEventListener('click', function() {
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", actionURL);

		var machineIdField = document.createElement("input");
		machineIdField.setAttribute("type", "hidden");
		machineIdField.setAttribute("name", "machineId");
		machineIdField.setAttribute("value", machineId);

		var platformIdField = document.createElement("input");
		platformIdField.setAttribute("type", "hidden");
		platformIdField.setAttribute("name", "platformId");
		platformIdField.setAttribute("value", platformId);

		var idField = document.createElement("input");
		idField.setAttribute("type", "hidden");
		idField.setAttribute("name", "id");
		idField.setAttribute("value", id);

		form.appendChild(machineIdField);
		form.appendChild(platformIdField);
		form.appendChild(idField);

		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);
	});

	document.getElementById('stopNodeDialog').showModal();
}

function deleteNode(actionURL, machineId, platformId, id) {
	document.getElementById('deleteNodeDialogMessageDiv').innerHTML = "Are you sure you want to delete node '" + id + "'?";

	document.getElementById('okDeleteNode').addEventListener('click', function() {
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", actionURL);

		var machineIdField = document.createElement("input");
		machineIdField.setAttribute("type", "hidden");
		machineIdField.setAttribute("name", "machineId");
		machineIdField.setAttribute("value", machineId);

		var platformIdField = document.createElement("input");
		platformIdField.setAttribute("type", "hidden");
		platformIdField.setAttribute("name", "platformId");
		platformIdField.setAttribute("value", platformId);

		var idField = document.createElement("input");
		idField.setAttribute("type", "hidden");
		idField.setAttribute("name", "id");
		idField.setAttribute("value", id);

		form.appendChild(machineIdField);
		form.appendChild(platformIdField);
		form.appendChild(idField);

		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);
	});

	document.getElementById('deleteNodeDialog').showModal();
}

function deleteNodes(formId, actionURL) {
	document.getElementById('submitNodesDialogTitleDiv').innerHTML = "Delete Nodes";
	document.getElementById('submitNodesDialogMessageDiv').innerHTML = "Are you sure you want to delete selected nodes?";

	document.getElementById('okSubmitNodes').addEventListener('click', function() {
		var mainForm = document.getElementById(formId);
		mainForm.setAttribute("action", actionURL);
		mainForm.submit();
	});

	document.getElementById('submitNodesDialog').showModal();
}

function startNodes(formId, actionURL) {
	document.getElementById('submitNodesDialogTitleDiv').innerHTML = "Start Nodes";
	document.getElementById('submitNodesDialogMessageDiv').innerHTML = "Are you sure you want to start selected nodes?";

	document.getElementById('okSubmitNodes').addEventListener('click', function() {
		var mainForm = document.getElementById(formId);
		mainForm.setAttribute("action", actionURL);
		mainForm.submit();
	});

	document.getElementById('submitNodesDialog').showModal();
}

function stopNodes(formId, actionURL) {
	document.getElementById('submitNodesDialogTitleDiv').innerHTML = "Stop Nodes";
	document.getElementById('submitNodesDialogMessageDiv').innerHTML = "Are you sure you want to stop selected nodes?";

	document.getElementById('okSubmitNodes').addEventListener('click', function() {
		var mainForm = document.getElementById(formId);
		mainForm.setAttribute("action", actionURL);
		mainForm.submit();
	});

	document.getElementById('submitNodesDialog').showModal();
}


$(document).on("click", "#actionStartNodes", function() {
	document.getElementById('startNodesDialog').showModal();
});
$(document).on("click", "#okStartNodes", function() {
	var actionURL = document.getElementById('actionStartNodes').getAttribute('targetFormUrl');
	var form = document.getElementById('main_list');
	form.setAttribute("action", actionURL);

	if (document.getElementById('startNodesDialog_option_clean').checked) {
		var cleanField = document.createElement("input");
		cleanField.setAttribute("type", "hidden");
		cleanField.setAttribute("name", "-clean");
		cleanField.setAttribute("value", "true");
		form.appendChild(cleanField);
	}

	form.submit();
});
$(document).on("click", "#cancelStartNodes", function() {
	// href="javascript:document.getElementById('upload_form').reset();"
	document.getElementById('main_list').reset();
	document.getElementById('startNodesDialog_option_clean').checked = false;
	document.getElementById('startNodesDialog').close();
});


//-----------------------------------------------------------------------
// Install programs
//-----------------------------------------------------------------------
$(document).on("click", "#actionInstallPrograms", function() {
	$.get("../appstore/programsprovider", function(responseXml) {
		var htmlContent = $(responseXml).find("data").html();
		$("#programsSelectionDiv").html(htmlContent);
	});

	var dialog = document.getElementById('programsSelectionDialog');
	dialog.showModal();
});

$(document).on("click", "#okInstallPrograms", function() {
	var form = document.getElementById("install_form");

	var checkboxes = document.getElementsByName("id");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].checked) {
			var nodeId = checkboxes[i].value;

			var idField = document.createElement("input");
			idField.setAttribute("type", "hidden");
			idField.setAttribute("name", "nodeId");
			idField.setAttribute("value", nodeId);
			form.appendChild(idField);
		}
	}

	// javascript:document.getElementById('install_form').submit();
	document.getElementById('install_form').submit();
	var dialog = document.getElementById('programsSelectionDialog');
	dialog.close();
});

$(document).on("click", "#cancelInstallPrograms", function() {
	// javascript:document.getElementById('install_form').reset();
	document.getElementById('install_form').reset();
	var dialog = document.getElementById('programsSelectionDialog');
	dialog.close();
});


//-----------------------------------------------------------------------
//Uninstall programs
//-----------------------------------------------------------------------
$(document).on("click", "#actionUninstallPrograms", function() {
	var uninstall_form = document.getElementById("uninstall_form");
	var url = uninstall_form.getAttribute("data-programUninstallProviderUrl");
	// alert("url = " + url);

	$.post(url, $("#main_list").serialize(), function(responseXml) {
		var htmlContent = $(responseXml).find("data").html();
		$("#uninstallProgramsSelectionDiv").html(htmlContent);
	});

//	var myform = document.getElementById("main_list");
//    var formData = new FormData(myform);
//    $.ajax({
//        url: "/domain/nodesprogramuninstallprovider",
//        data: formData,
//        cache: false,
//        processData: false,
//        contentType: false,
//        type: 'POST',
//        success: function (dataofconfirm) {
//            // do something with the result
//        }
//    });

	var dialog = document.getElementById('uninstallProgramsSelectionDialog');
	dialog.showModal();
});

$(document).on("click", "#okUninstallPrograms", function() {
	var form = document.getElementById("uninstall_form");

	var checkboxes = document.getElementsByName("id");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].checked) {
			var nodeId = checkboxes[i].value;

			var idField = document.createElement("input");
			idField.setAttribute("type", "hidden");
			idField.setAttribute("name", "nodeId");
			idField.setAttribute("value", nodeId);
			form.appendChild(idField);
		}
	}

	// javascript:document.getElementById('install_form').submit();
	document.getElementById('uninstall_form').submit();
	var dialog = document.getElementById('uninstallProgramsSelectionDialog');
	dialog.close();
});

$(document).on("click", "#cancelUninstallPrograms", function() {
	// javascript:document.getElementById('install_form').reset();
	document.getElementById('uninstall_form').reset();
	var dialog = document.getElementById('uninstallProgramsSelectionDialog');
	dialog.close();
});


//-----------------------------------------------------------------------
// Add listeners
//-----------------------------------------------------------------------
(function() {
	// add action.addNode click listener
	document.getElementById('action.addNode').addEventListener('click', function() {
		addNode();
	});

	// add action.deleteNodes click listener
	document.getElementById('action.deleteNodes').addEventListener('click', function() {
		var formId = document.getElementById('action.deleteNodes').getAttribute('targetFormId');
		var url = document.getElementById('action.deleteNodes').getAttribute('targetFormUrl');
		deleteNodes(formId, url);
	});

	// add action.startsNodes click listener
//	document.getElementById('action.startNodes').addEventListener('click', function() {
//		var formId = document.getElementById('action.startNodes').getAttribute('targetFormId');
//		var url = document.getElementById('action.startNodes').getAttribute('targetFormUrl');
//		startNodes(formId, url);
//	});

	// add action.stopNodes click listener
	document.getElementById('action.stopNodes').addEventListener('click', function() {
		var formId = document.getElementById('action.stopNodes').getAttribute('targetFormId');
		var url = document.getElementById('action.stopNodes').getAttribute('targetFormUrl');
		stopNodes(formId, url);
	});

	document.getElementById('cancelAddNode').addEventListener('click', function() {
		document.getElementById('newNodeDialog').close();
	});

	document.getElementById('cancelChangeNode').addEventListener('click', function() {
		document.getElementById('changeNodeDialog').close();
	});

	document.getElementById('cancelStartNode').addEventListener('click', function() {
		document.getElementById('startNodeDialog').close();
	});

	document.getElementById('cancelStopNode').addEventListener('click', function() {
		document.getElementById('stopNodeDialog').close();
	});

	document.getElementById('cancelDeleteNode').addEventListener('click', function() {
		document.getElementById('deleteNodeDialog').close();
	});

	document.getElementById('cancelSubmitNodes').addEventListener('click', function() {
		document.getElementById("main_list").reset();
		document.getElementById('submitNodesDialog').close();
	});
})();
