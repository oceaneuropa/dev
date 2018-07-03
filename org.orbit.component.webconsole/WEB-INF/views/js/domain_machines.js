
function addMachine() {
	var addMachineDialog = document.getElementById('newMachineDialog');
	addMachineDialog.showModal();
}

function changeMachine(id, name, ip) {
	document.getElementById("machine_id").setAttribute('value', id);
	document.getElementById("machine_name").setAttribute('value', name);
	document.getElementById("machine_ip").setAttribute('value', ip);

	var changeMachineDialog = document.getElementById('changeMachineDialog');
	changeMachineDialog.showModal();
}

function deleteMachine(actionURL, machineId) {
	var dialog = document.getElementById('deleteMachineDialog');

	var messageDiv = document.getElementById('deleteMachineDialogMessageDiv');
	messageDiv.innerHTML = "Are you sure you want to delete machine '" + machineId + "'?";

	var okButton = document.getElementById('okDeleteMachine');
	okButton.addEventListener('click', function() {
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", actionURL);

		var idField = document.createElement("input");
		idField.setAttribute("type", "hidden");
		idField.setAttribute("name", "id");
		idField.setAttribute("value", machineId);

		form.appendChild(idField);

		document.body.appendChild(form);
		form.submit();
		document.body.removeChild(form);
	});

	dialog.showModal();
}

function deleteMachines() {
	var dialog = document.getElementById('deleteMachinesDialog');

	var messageDiv = document.getElementById('deleteMachinesDialogMessageDiv');
	messageDiv.innerHTML = "Are you sure you want to delete selected machines?";

	var okButton = document.getElementById('okDeleteMachines');
	okButton.addEventListener('click', function() {
		var form = document.getElementById("main_list");
		form.submit();
	});

	dialog.showModal();
}

(function() {
	// add action.addMachine click listener
	document.getElementById('action.addMachine').addEventListener('click', function() {
		addMachine();
	});

	// add action.deleteMachines click listener
	document.getElementById('action.deleteMachines').addEventListener('click', function() {
		deleteMachines();
	});

	var addMachineDialog = document.getElementById('newMachineDialog');
	var cancelAddMachineButton = document.getElementById('cancelAddMachine');
	cancelAddMachineButton.addEventListener('click', function() {
		addMachineDialog.close();
	});

	var changeMachineDialog = document.getElementById('changeMachineDialog');
	var cancelChangeMachineButton = document.getElementById('cancelChangeMachine');
	cancelChangeMachineButton.addEventListener('click', function() {
		changeMachineDialog.close();
	});
	
	var deleteMachineDialog = document.getElementById('deleteMachineDialog');
	var cancelDeleteMachineButton = document.getElementById('cancelDeleteMachine');
	cancelDeleteMachineButton.addEventListener('click', function() {
		deleteMachineDialog.close();
	});

	var deleteMachinesDialog = document.getElementById('deleteMachinesDialog');
	var cancelDeleteMachinesButton = document.getElementById('cancelDeleteMachines');
	cancelDeleteMachinesButton.addEventListener('click', function() {
		document.getElementById("main_list").reset();
		deleteMachinesDialog.close();
	});
})();
