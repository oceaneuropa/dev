//-----------------------------------------------------------------------
// Selection
//-----------------------------------------------------------------------
function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}


//-----------------------------------------------------------------------
// Add property
//-----------------------------------------------------------------------
$(document).on("click", "#actionAddProperty", function() {
	document.getElementById('newPropertyDialog').showModal();
});

$(document).on("click", "#okAddProperty", function() {
	var form = document.getElementById("new_form");
	form.submit();
});

$(document).on("click", "#cancelAddProperty", function() {
	document.getElementById('new_form').reset();
	document.getElementById('newPropertyDialog').close();
});


//-----------------------------------------------------------------------
// Change property
//-----------------------------------------------------------------------
function changeProperty(name, value, rowNum) {
	document.getElementById("property_oldName").setAttribute('value', name);
	document.getElementById("property_name").setAttribute('value', name);
	document.getElementById("property_value").value = value;
	document.getElementById("property_value").setAttribute('rows', rowNum);

	document.getElementById('changePropertyDialog').showModal();
}

$(document).on("click", "#okChangeProperty", function() {
	document.getElementById("update_form").submit();
});

$(document).on("click", "#cancelChangeProperty", function() {
	document.getElementById('update_form').reset();
	document.getElementById('changePropertyDialog').close();
});


//-----------------------------------------------------------------------
// Delete properties
//-----------------------------------------------------------------------
$(document).on("click", "#actionDeleteProperties", function() {
	document.getElementById('deletePropertiesDialog').showModal();
});

$(document).on("click", "#okDeleteProperties", function() {
	var form = document.getElementById("delete_form");

	var checkboxes = document.getElementsByName("name");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].checked) {
			var name = checkboxes[i].value;

			var nameField = document.createElement("input");
			nameField.setAttribute("type", "hidden");
			nameField.setAttribute("name", "name");
			nameField.setAttribute("value", name);
			form.appendChild(nameField);
		}
	}

	form.submit();
	document.getElementById('deletePropertiesDialog').close();
});

$(document).on("click", "#cancelDeleteProperties", function() {
	document.getElementById('main_list').reset();
	document.getElementById('deletePropertiesDialog').close();
});


//-----------------------------------------------------------------------
// Delete property
//-----------------------------------------------------------------------
function deleteProperty(name) {
	document.getElementById('delete_form2_name').value = name;
	document.getElementById('deletePropertyDialog').showModal();
}

$(document).on("click", "#okDeleteProperty", function() {
	document.getElementById('delete_form2').submit();
});

$(document).on("click", "#cancelDeleteProperty", function() {
	document.getElementById('deletePropertyDialog').close();
	document.getElementById('delete_form2_name').value = null;
});
