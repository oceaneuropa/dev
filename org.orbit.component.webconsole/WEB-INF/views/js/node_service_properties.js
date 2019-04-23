function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}

$(document).on("click", "#action_addServiceProperty", function() {
	var dialog = document.getElementById('newPropertyDialog');
	dialog.showModal();
});

$(document).on("click", "#cancelAddProperty", function() {
	var dialog = document.getElementById('newPropertyDialog');
	dialog.close();
});

$(document).on("click", "#action_removeServiceProperties", function() {
	var dialog = document.getElementById('removePropertiesDialog');
	dialog.showModal();
});

$(document).on("click", "#okRemoveProperties", function() {
	var form = document.getElementById('main_list');
	form.submit();
});

$(document).on("click", "#cancelRemoveProperties", function() {
	var dialog = document.getElementById('removePropertiesDialog');
	dialog.close();
});

function changeServiceProperty(name, value, rowNum) {
	document.getElementById("attribute_oldName").setAttribute('value', name);
	document.getElementById("attribute_name").setAttribute('value', name);
	document.getElementById("attribute_value").value = value;
	document.getElementById("attribute_value").setAttribute('rows', rowNum);
	document.getElementById('changePropertyDialog').showModal();
}

$(document).on("click", "#cancelChangeProperty", function() {
	var dialog = document.getElementById('changePropertyDialog');
	dialog.close();
});

function removeServiceProperty(name) {
	// https://stackoverflow.com/questions/33828563/get-checkbox-name-from-javascript
	// var checkbox = $("#" + checkboxId);
	// checkbox.checked = true;
	// document.getElementById(checkboxId).checked = true;

	var input = document.getElementById('remove_form_property_name');
	input.value = name;

	var dialog = document.getElementById('removePropertyDialog');
	dialog.showModal();
}

$(document).on("click", "#okRemoveProperty", function() {
	var form = document.getElementById('remove_form');
	form.submit();
});

$(document).on("click", "#cancelRemoveProperty", function() {
	// alert('old value: ' + document.getElementById('remove_form_property_name').value);
	// alert('new value: ' + document.getElementById('remove_form_property_name').value);
	// document.getElementById('remove_form').reset();	
	var input = document.getElementById('remove_form_property_name');
	input.value = "";

	var dialog = document.getElementById('removePropertyDialog');
	dialog.close();
});
