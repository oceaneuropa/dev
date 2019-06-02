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


//-----------------------------------------------------------------------
// Add attribute
//-----------------------------------------------------------------------
$(document).on("click", "#actionAddAttribute", function() {
	document.getElementById('newAttributeDialog').showModal();
});

$(document).on("click", "#okAddAttribute", function() {
	var form = document.getElementById("new_form");
	form.submit();
});

$(document).on("click", "#cancelAddAttribute", function() {
	document.getElementById('new_form').reset();
	document.getElementById('newAttributeDialog').close();
});


//-----------------------------------------------------------------------
// Change attribute
//-----------------------------------------------------------------------
function changeAttribute(name, value, rowNum) {
	document.getElementById("attribute_oldName").setAttribute('value', name);
	document.getElementById("attribute_name").setAttribute('value', name);
	document.getElementById("attribute_value").value = value;
	document.getElementById("attribute_value").setAttribute('rows', rowNum);

	document.getElementById('changeAttributeDialog').showModal();
}

$(document).on("click", "#okChangeAttribute", function() {
	document.getElementById("update_form").submit();
});

$(document).on("click", "#cancelChangeAttribute", function() {
	document.getElementById('update_form').reset();
	document.getElementById('changeAttributeDialog').close();
});


//-----------------------------------------------------------------------
// Delete attributes
//-----------------------------------------------------------------------
$(document).on("click", "#actionDeleteAttributes", function() {
	document.getElementById('deleteAttributesDialog').showModal();
});

$(document).on("click", "#okDeleteAttributes", function() {
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
	document.getElementById('deleteAttributesDialog').close();
});

$(document).on("click", "#cancelDeleteAttributes", function() {
	document.getElementById('main_list').reset();
	document.getElementById('deleteAttributesDialog').close();
});


//-----------------------------------------------------------------------
// Delete attribute
//-----------------------------------------------------------------------
function deleteAttribute(name) {
	document.getElementById('delete_form2_name').value = name;
	document.getElementById('deleteAttributeDialog').showModal();
}

$(document).on("click", "#okDeleteAttribute", function() {
	document.getElementById('delete_form2').submit();
});

$(document).on("click", "#cancelDeleteAttribute", function() {
	document.getElementById('deleteAttributeDialog').close();
	document.getElementById('delete_form2_name').value = null;
});
