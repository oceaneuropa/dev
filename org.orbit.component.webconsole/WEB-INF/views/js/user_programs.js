function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}

// -----------------------------------------------------------------------
// Add programs
// -----------------------------------------------------------------------
/**
 * Click Add button (id="actionAddProgram") to open programs selection dialog
 */
$(document).on("click", "#actionAddProgram", function() {
	// 1. Load programs info and populate the dialog
	// Execute Ajax GET request on URL of the servlet and execute the following
	// function with Ajax response XML
	$.get("./appstore/programsprovider", function(responseXml) {
		// Parse XML, find <data> element and append its HTML to HTML DOM
		// element with ID "programsSelectionDiv".
		var htmlContent = $(responseXml).find("data").html();
		$("#programsSelectionDiv").html(htmlContent);
	});

	// 2. Open programs selection dialog
	var dialog = document.getElementById('programsSelectionDialog');
	dialog.showModal();
});

/**
 * Click on Cancel button (id="cancelAddProgram") to close programs selection dialog
 */
$(document).on("click", "#cancelAddProgram", function() {
	// Close programs selection dialog
	var dialog = document.getElementById('programsSelectionDialog');
	dialog.close();
});

// -----------------------------------------------------------------------
// Remove programs
// -----------------------------------------------------------------------
$(document).on("click", "#actionRemovePrograms0", function() {
	document.getElementById('programActionDialog').showModal();
});

$(document).on("click", "#okRemovePrograms", function() {
	var form = document.getElementById("main_list");
	form.submit();
});

$(document).on("click", "#cancelRemovePrograms", function() {
	document.getElementById('main_list').reset();
	document.getElementById('programActionDialog').close();
});

// -----------------------------------------------------------------------
// Action on programs
// -----------------------------------------------------------------------
function onProgramAction(action, formActionUrl) {
	document.getElementById("main_list__action").value = action;

	var form = document.getElementById("main_list");
	form.setAttribute("action", formActionUrl);

	var programActionDialogTitleDiv = document.getElementById("programActionDialogTitleDiv");
	var programActionDialogMessageDiv = document.getElementById("programActionDialogMessageDiv");

	var dialogTitle = null;
	var dialogMessage = null;
	if (action == "remove") {
		dialogTitle = "Remove Programs";
		dialogMessage = "Are you sure you want to remove the programs?";

	} else {
		dialogTitle = action + " Programs";
		dialogMessage = "Are you sure you want to '" + action + "' the programs?";
	}

	programActionDialogTitleDiv.innerHTML = dialogTitle;
	programActionDialogMessageDiv.innerHTML = dialogMessage;

	var dialog = document.getElementById('programActionDialog');
	dialog.showModal();
}

$(document).on("click", "#okProgramAction", function() {
	var form = document.getElementById("main_list");
	form.submit();
});

$(document).on("click", "#cancelProgramAction", function() {
	document.getElementById('main_list').reset();
	document.getElementById('programActionDialog').close();
});
