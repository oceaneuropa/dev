function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}

// -----------------------------------------------------------------------
// Install programs
// -----------------------------------------------------------------------
/**
 * Click Install button (id="actionInstallProgram") to open programs selection
 * dialog
 */
$(document).on("click", "#actionInstallProgram", function() {
	// 1. Load programs info and populate the dialog
	// Execute Ajax GET request on URL of the servlet and execute the following
	// function with Ajax response XML
	$.get("../appstore/programsprovider", function(responseXml) {
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
 * Click on Cancel button (id="cancelInstallProgram") to close programs
 * selection dialog
 */
$(document).on("click", "#cancelInstallProgram", function() {
	// Close programs selection dialog
	var dialog = document.getElementById('programsSelectionDialog');
	dialog.close();
});

// -----------------------------------------------------------------------
// Uninstall programs
// -----------------------------------------------------------------------
$(document).on("click", "#actionUninstallPrograms0", function() {
	document.getElementById('uninstallProgramsDialog').showModal();
});

$(document).on("click", "#okUninstallPrograms", function() {
	var form = document.getElementById("main_list");
	form.submit();
});

$(document).on("click", "#cancelUninstallPrograms", function() {
	document.getElementById('main_list').reset();
	document.getElementById('uninstallProgramsDialog').close();
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
	if (action == "uninstall") {
		dialogTitle = "Uninstall Programs";
		dialogMessage = "Are you sure you want to uninstall the programs?";

	} else if (action == "activate") {
		dialogTitle = "Activate Programs";
		dialogMessage = "Are you sure you want to activate the programs?";

	} else if (action == "deactivate") {
		dialogTitle = "Deactivate Programs";
		dialogMessage = "Are you sure you want to deactivate the programs?";

	} else if (action == "start") {
		dialogTitle = "Start Programs";
		dialogMessage = "Are you sure you want to start the programs?";

	} else if (action == "stop") {
		dialogTitle = "Stop Programs";
		dialogMessage = "Are you sure you want to stop the programs?";

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