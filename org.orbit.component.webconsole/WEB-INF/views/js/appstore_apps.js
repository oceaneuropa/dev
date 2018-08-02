function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}

function addApp() {
	var addAppDialog = document.getElementById('newAppDialog');
	addAppDialog.showModal();
}

function changeApp(id, appId, appVersion, type, name, fileName, desc) {
	document.getElementById("record_id").setAttribute('value', id);
	document.getElementById("app_id").setAttribute('value', appId);
	document.getElementById("app_version").setAttribute('value', appVersion);
	document.getElementById("app_type").setAttribute('value', type);
	document.getElementById("app_name").setAttribute('value', name);
	document.getElementById("app_fileName").setAttribute('value', fileName);

	// document.getElementById("app_desc").setAttribute('value', desc);
	document.getElementById("app_desc").value = desc; // for setting value to textarea

	var changeAppDialog = document.getElementById('changeAppDialog');
	changeAppDialog.showModal();
}

function deleteApp(actionURL, id, version) {
	var dialog = document.getElementById('deleteAppDialog');

	var messageDiv = document.getElementById('deleteAppDialogMessageDiv');
	messageDiv.innerHTML = "Are you sure you want to delete app '" + id + " ( " +version + ")'?";

	var okButton = document.getElementById('okDeleteApp');
	okButton.addEventListener('click', function() {
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", actionURL);

		var idField = document.createElement("input");
		idField.setAttribute("type", "hidden");
		idField.setAttribute("name", "id");
		idField.setAttribute("value", id);
		
		var versionField = document.createElement("input");
		versionField.setAttribute("type", "hidden");
		versionField.setAttribute("name", "version");
		versionField.setAttribute("value", version);

		form.appendChild(idField);
		form.appendChild(versionField);

		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);
	});

	dialog.showModal();
}

function deleteApps() {
	var dialog = document.getElementById('deleteAppsDialog');

	var messageDiv = document.getElementById('deleteAppsDialogMessageDiv');
	messageDiv.innerHTML = "Are you sure you want to delete selected apps?";

	var okButton = document.getElementById('okDeleteApps');
	okButton.addEventListener('click', function() {
		var form = document.getElementById("main_list");
		form.submit();
	});

	dialog.showModal();
}

(function() {
	// add action.addApp click listener
	document.getElementById('action.addApp').addEventListener('click', function() {
		addApp();
	});

	// add action.deleteApps click listener
	document.getElementById('action.deleteApps').addEventListener('click', function() {
		deleteApps();
	});

	var addAppDialog = document.getElementById('newAppDialog');
	var cancelAddAppButton = document.getElementById('cancelAddApp');
	cancelAddAppButton.addEventListener('click', function() {
		addAppDialog.close();
	});

	var changeAppDialog = document.getElementById('changeAppDialog');
	var cancelChangeAppButton = document.getElementById('cancelChangeApp');
	cancelChangeAppButton.addEventListener('click', function() {
		changeAppDialog.close();
	});

	var deleteAppDialog = document.getElementById('deleteAppDialog');
	var cancelDeleteAppButton = document.getElementById('cancelDeleteApp');
	cancelDeleteAppButton.addEventListener('click', function() {
		deleteAppDialog.close();
	});

	var deleteAppsDialog = document.getElementById('deleteAppsDialog');
	var cancelDeleteAppsButton = document.getElementById('cancelDeleteApps');
	cancelDeleteAppsButton.addEventListener('click', function() {
		document.getElementById("main_list").reset();
		deleteAppsDialog.close();
	});

})();