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
//Create config element
//-----------------------------------------------------------------------
$(document).on("click", "#actionAddConfigElement", function() {
	document.getElementById('newConfigElementDialog').showModal();
});

$(document).on("click", "#okAddConfigElement", function() {
	var form = document.getElementById("new_form");
	form.submit();
});

$(document).on("click", "#cancelAddConfigElement", function() {
	document.getElementById('new_form').reset();
	document.getElementById('newConfigElementDialog').close();
});


//-----------------------------------------------------------------------
//Change config element
//-----------------------------------------------------------------------
function changeConfigElement(elementId, name) {
	document.getElementById("config_element__elementId").setAttribute('value', elementId);
	document.getElementById("config_element__name").setAttribute('value', name);

	document.getElementById('changeConfigElementDialog').showModal();
}

$(document).on("click", "#okChangeConfigElement", function() {
	document.getElementById("update_form").submit();
});

$(document).on("click", "#cancelChangeConfigElement", function() {
	document.getElementById('update_form').reset();
	document.getElementById('changeConfigElementDialog').close();
});


//-----------------------------------------------------------------------
//Delete config elements
//-----------------------------------------------------------------------
$(document).on("click", "#actionDeleteConfigElements", function() {
	document.getElementById('deleteConfigElementsDialog').showModal();
});

$(document).on("click", "#okDeleteConfigElements", function() {
	var form = document.getElementById("delete_form");

	var checkboxes = document.getElementsByName("elementId");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].checked) {
			var elementId = checkboxes[i].value;
			if (elementId != null && elementId != "") {
				var elementIdField = document.createElement("input");
				elementIdField.setAttribute("type", "hidden");
				elementIdField.setAttribute("name", "elementId");
				elementIdField.setAttribute("value", elementId);		
				form.appendChild(elementIdField);
			}
		}
	}
	form.submit();
	document.getElementById('deleteConfigElementsDialog').close();
});

$(document).on("click", "#cancelDeleteConfigElements", function() {
	document.getElementById('main_list').reset();
	document.getElementById('deleteConfigElementsDialog').close();
});


//-----------------------------------------------------------------------
//Delete config element
//-----------------------------------------------------------------------
function deleteConfigElement(elementId) {
	document.getElementById('delete_form2_elementId').value = elementId;
	document.getElementById('deleteConfigElementDialog').showModal();
}

$(document).on("click", "#okDeleteConfigElement", function() {
	document.getElementById('delete_form2').submit();
});

$(document).on("click", "#cancelDeleteConfigElement", function() {
	document.getElementById('deleteConfigElementDialog').close();
	document.getElementById('delete_form2_elementId').value = null;
});
