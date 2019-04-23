function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}

//-----------------------------------------------------------------------
//Action on services
//-----------------------------------------------------------------------
function onPlatformServiceAction(action, formActionUrl) {
	document.getElementById("main_list__action").value = action;

	var form = document.getElementById("main_list");
	form.setAttribute("action", formActionUrl);

	var serviceActionDialogTitleDiv = document.getElementById("serviceActionDialogTitleDiv");
	var serviceActionDialogMessageDiv = document.getElementById("serviceActionDialogMessageDiv");

	var dialogTitle = null;
	var dialogMessage = null;
	if (action == "start") {
		dialogTitle = "Start Services";
		dialogMessage = "Are you sure you want to start the services?";

	} else if (action == "stop") {
		dialogTitle = "Stop Services";
		dialogMessage = "Are you sure you want to stop the services?";

	} else {
		dialogTitle = action + " Services";
		dialogMessage = "Are you sure you want to '" + action + "' the services?";
	}

	serviceActionDialogTitleDiv.innerHTML = dialogTitle;
	serviceActionDialogMessageDiv.innerHTML = dialogMessage;

	var dialog = document.getElementById('serviceActionDialog');
	dialog.showModal();
}

$(document).on("click", "#okServiceAction", function() {
	var form = document.getElementById("main_list");
	form.submit();
});

$(document).on("click", "#cancelServiceAction", function() {
	document.getElementById('main_list').reset();
	document.getElementById('serviceActionDialog').close();
});





function startService(sid) {
	var input = document.getElementById('start_service_form_sid');
	input.value = sid;

	var dialog = document.getElementById('startServiceDialog');
	dialog.showModal();
}

$(document).on("click", "#okStartService", function() {
	var form = document.getElementById('start_service_form');
	form.submit();
});

$(document).on("click", "#cancelStartService", function() {
	var input = document.getElementById('start_service_form_sid');
	input.value = "";

	var dialog = document.getElementById('startServiceDialog');
	dialog.close();
});


function stopService(sid) {
	var input = document.getElementById('stop_service_form_sid');
	input.value = sid;

	var dialog = document.getElementById('stopServiceDialog');
	dialog.showModal();
}

$(document).on("click", "#okStopService", function() {
	var form = document.getElementById('stop_service_form');
	form.submit();
});

$(document).on("click", "#cancelStopService", function() {
	var input = document.getElementById('stop_service_form_sid');
	input.value = "";

	var dialog = document.getElementById('stopServiceDialog');
	dialog.close();
});

