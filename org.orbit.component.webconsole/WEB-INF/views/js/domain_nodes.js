
function addNode() {
	document.getElementById('newNodeDialog').showModal();
}

function changeNode(id, name) {
	document.getElementById("node_id").setAttribute('value', id);
	document.getElementById("node_name").setAttribute('value', name);
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

function deleteNodes() {
	document.getElementById('deleteNodesDialogMessageDiv').innerHTML = "Are you sure you want to delete selected nodes?";

	document.getElementById('okDeleteNodes').addEventListener('click', function() {
		document.getElementById("main_list").submit();
	});

	document.getElementById('deleteNodesDialog').showModal();
}

(function() {
	// add action.addNode click listener
	document.getElementById('action.addNode').addEventListener('click', function() {
		addNode();
	});

	// add action.deleteNodes click listener
	document.getElementById('action.deleteNodes').addEventListener('click', function() {
		deleteNodes();
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

	document.getElementById('cancelDeleteNodes').addEventListener('click', function() {
		document.getElementById("main_list").reset();
		document.getElementById('deleteNodesDialog').close();
	});
})();
