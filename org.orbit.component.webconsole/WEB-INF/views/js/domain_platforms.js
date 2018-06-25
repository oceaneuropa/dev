
function addPlatform() {
	document.getElementById('newPlatformDialog').showModal();
}

function changePlatform(id, name, hostURL, contextRoot) {
	document.getElementById("platform_id").setAttribute('value', id);
	document.getElementById("platform_name").setAttribute('value', name);
	document.getElementById("platform_hostUrl").setAttribute('value', hostURL);
	document.getElementById("platform_contextRoot").setAttribute('value', contextRoot);
	document.getElementById('changePlatformDialog').showModal();
}

function deletePlatform(actionURL, machineId, id) {
	document.getElementById('deletePlatformDialogMessageDiv').innerHTML = "Are you sure you want to delete platform '" + id + "'?";

	document.getElementById('okDeletePlatform').addEventListener('click', function() {
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", actionURL);

		var platformIdField = document.createElement("input");
		platformIdField.setAttribute("type", "hidden");
		platformIdField.setAttribute("name", "machineId");
		platformIdField.setAttribute("value", machineId);

		var idField = document.createElement("input");
		idField.setAttribute("type", "hidden");
		idField.setAttribute("name", "id");
		idField.setAttribute("value", id);

		form.appendChild(platformIdField);
		form.appendChild(idField);

		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);
	});

	document.getElementById('deletePlatformDialog').showModal();
}

function deletePlatforms() {
	document.getElementById('deletePlatformsDialogMessageDiv').innerHTML = "Are you sure you want to delete selected platforms?";

	document.getElementById('okDeletePlatforms').addEventListener('click', function() {
		document.getElementById("main_list").submit();
	});

	document.getElementById('deletePlatformsDialog').showModal();
}

(function() {
	document.getElementById('cancelAddPlatform').addEventListener('click', function() {
		document.getElementById('newPlatformDialog').close();
	});

	document.getElementById('cancelChangePlatform').addEventListener('click', function() {
		document.getElementById('changePlatformDialog').close();
	});

	document.getElementById('cancelDeletePlatform').addEventListener('click', function() {
		document.getElementById('deletePlatformDialog').close();
	});

	document.getElementById('cancelDeletePlatforms').addEventListener('click', function() {
		document.getElementById("main_list").reset();
		document.getElementById('deletePlatformsDialog').close();
	});
})();
