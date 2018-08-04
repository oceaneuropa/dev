
/**
 * Click Install button (id="actionInstallProgram") to open programs selection dialog
 */
$(document).on("click", "#actionInstallProgram", function() {
	// 1. Load programs info and populate the dialog
	// Execute Ajax GET request on URL of the servlet and execute the following function with Ajax response XML
    $.get("../appstore/programsprovider", function(responseXml) {
    	// Parse XML, find <data> element and append its HTML to HTML DOM element with ID "programsSelectionDiv".
    	var htmlContent = $(responseXml).find("data").html();
        $("#programsSelectionDiv").html(htmlContent);
    });

	// 2. Open programs selection dialog
	var dialog = document.getElementById('programsSelectionDialog');
	dialog.showModal();
});

/**
 * Click on Cancel button (id="cancelInstallProgram") to close programs selection dialog
 */
$(document).on("click", "#cancelInstallProgram", function() {
	// Close programs selection dialog
	var dialog = document.getElementById('programsSelectionDialog');
	dialog.close();
});
