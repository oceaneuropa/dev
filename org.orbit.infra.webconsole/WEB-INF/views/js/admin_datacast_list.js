function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}

function setElementValue(elementId, elementValue) {
	document.getElementById(elementId).setAttribute('value', elementValue);
}

//-----------------------------------------------------------------------
// Create nodes
//-----------------------------------------------------------------------
$(document).on("click", "#actionAddNode", function() {
	document.getElementById('newNodeDialog').showModal();
});

$(document).on("click", "#okAddNode", function() {
	var form = document.getElementById("new_form");
	form.submit();
});

$(document).on("click", "#cancelAddNode", function() {
	document.getElementById('new_form').reset();
	document.getElementById('newNodeDialog').close();
});

//-----------------------------------------------------------------------
// Change node
//-----------------------------------------------------------------------
function changeDataCastNode(elementId, dataCastId, name, enabled) {
	document.getElementById("node__elementId").setAttribute('value', elementId);
	document.getElementById("node__data_cast_id").setAttribute('value', dataCastId);
	document.getElementById("node__name").setAttribute('value', name);
	document.getElementById("node__enabled").setAttribute('value', enabled);	

	// alert("enabled=" + enabled);
	// var radio = document.getElementsByName('node__enabled_radio');
	if (enabled == 'true') {
		document.update_form_name.node__enabled_radio.value='true';
	} else {
		document.update_form_name.node__enabled_radio.value='false';
	}

	document.getElementById('changeNodeDialog').showModal();
}

$(document).on("click", "#okChangeNode", function() {
	document.getElementById("update_form").submit();
});

$(document).on("click", "#cancelChangeNode", function() {
	document.getElementById('update_form').reset();
	document.getElementById('changeNodeDialog').close();
});

//-----------------------------------------------------------------------
// Delete nodes
//-----------------------------------------------------------------------
$(document).on("click", "#actionDeleteNodes", function() {
	document.getElementById('deleteNodesDialog').showModal();
});

$(document).on("click", "#okDeleteNodes", function() {
	var form = document.getElementById("delete_form");

	var checkboxes = document.getElementsByName("elementId");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].checked) {
			var elementId = checkboxes[i].value;

			var elementIdField = document.createElement("input");
			elementIdField.setAttribute("type", "hidden");
			elementIdField.setAttribute("name", "elementId");
			elementIdField.setAttribute("value", elementId);
			form.appendChild(elementIdField);
		}
	}

	// javascript:document.getElementById('install_form').submit();
	// document.getElementById('install_form').submit();
	form.submit();
	document.getElementById('deleteNodesDialog').close();

	// var form = document.getElementById("main_list");
	// form.submit();
});

$(document).on("click", "#cancelDeleteNodes", function() {
	document.getElementById('main_list').reset();
	document.getElementById('deleteNodesDialog').close();
});
