
function addAttribute() {
	document.getElementById('newAttributeDialog').showModal();
}

function changeAttribute(name, value, rowNum) {
	document.getElementById("attribute_oldName").setAttribute('value', name);
	document.getElementById("attribute_name").setAttribute('value', name);
	document.getElementById("attribute_value").value = value;
	document.getElementById("attribute_value").setAttribute('rows', rowNum);
	document.getElementById('changeAttributeDialog').showModal();
}

function deleteAttribute(actionURL, machineId, platformId, id, name) {
	document.getElementById('deleteAttributeDialogMessageDiv').innerHTML = "Are you sure you want to delete attribute '" + name + "'?";

	document.getElementById('okDeleteAttribute').addEventListener('click', function() {
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

		var nameField = document.createElement("input");
		nameField.setAttribute("type", "hidden");
		nameField.setAttribute("name", "name");
		nameField.setAttribute("value", name);

		form.appendChild(machineIdField);
		form.appendChild(platformIdField);
		form.appendChild(idField);
		form.appendChild(nameField);

		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);
	});

	document.getElementById('deleteAttributeDialog').showModal();
}

function deleteAttributes() {
	document.getElementById('deleteAttributesDialogMessageDiv').innerHTML = "Are you sure you want to delete selected attributes?";

	document.getElementById('okDeleteAttributes').addEventListener('click', function() {
		document.getElementById("main_list").submit();
	});

	document.getElementById('deleteAttributesDialog').showModal();
}

(function() {
	// add action.addAttribute click listener
	document.getElementById('action.addAttribute').addEventListener('click', function() {
		addAttribute();
	});

	// add action.deleteAttributes click listener
	document.getElementById('action.deleteAttributes').addEventListener('click', function() {
		deleteAttributes();
	});

	document.getElementById('cancelAddAttribute').addEventListener('click', function() {
		document.getElementById('newAttributeDialog').close();
	});

	document.getElementById('cancelChangeAttribute').addEventListener('click', function() {
		document.getElementById('changeAttributeDialog').close();
	});

	document.getElementById('cancelDeleteAttribute').addEventListener('click', function() {
		document.getElementById('deleteAttributeDialog').close();
	});

	document.getElementById('cancelDeleteAttributes').addEventListener('click', function() {
		document.getElementById("main_list").reset();
		document.getElementById('deleteAttributesDialog').close();
	});
})();
