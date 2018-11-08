function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}

function setElementValue(elementId, elementValue) {
	document.getElementById(elementId).setAttribute('value', elementValue);
}

//-----------------------------------------------------------------------
// Create Channel
//-----------------------------------------------------------------------
$(document).on("click", "#actionCreateChannel", function() {
	$.get("../appstore/programsprovider", function(responseXml) {
		var htmlContent = $(responseXml).find("data_tube_id").html();
		$("#programsSelectionDiv").html(htmlContent);
	});

	document.getElementById('newChannelDialog').showModal();
});

$(document).on("click", "#okCreateChannel", function() {
	document.getElementById("new_form").submit();
});

$(document).on("click", "#cancelCreateChannel", function() {
	document.getElementById('new_form').reset();
	document.getElementById('newChannelDialog').close();
});

//-----------------------------------------------------------------------
// Change Channel
//-----------------------------------------------------------------------
function changeChannel(elementId, dataTubeId, name, enabled) {
	document.getElementById("update__elementId").setAttribute('value', elementId);
	document.getElementById("update__data_tube_id").setAttribute('value', dataTubeId);
	document.getElementById("update__name").setAttribute('value', name);

	if (enabled) {
		document.update_form_name.enabled.value='true';
	} else {
		document.update_form_name.enabled.value='false';
	}

	document.getElementById('changeChannelDialog').showModal();
}

$(document).on("click", "#okChangeChannel", function() {
	document.getElementById("update_form").submit();
});

$(document).on("click", "#cancelChangeChannel", function() {
	document.getElementById('update_form').reset();
	document.getElementById('changeChannelDialog').close();
});


//-----------------------------------------------------------------------
//Actions on channel
//-----------------------------------------------------------------------
function onChannelAction(action, formActionUrl) {
	document.getElementById("main_list__action").value = action;

	var form = document.getElementById("main_list");
	form.setAttribute("action", formActionUrl);

	var channelActionDialogTitleDiv = document.getElementById("channelActionDialogTitleDiv");
	var channelActionDialogMessageDiv = document.getElementById("channelActionDialogMessageDiv");

	var dialogTitle = null;
	var dialogMessage = null;

	if (action == "start") {
		dialogTitle = "Start Channels";
		dialogMessage = "Are you sure you want to start the channels?";

	} else if (action == "suspend") {
		dialogTitle = "Suspend Channels";
		dialogMessage = "Are you sure you want to suspend the channels?";

	} else if (action == "stop") {
		dialogTitle = "Stop Channels";
		dialogMessage = "Are you sure you want to stop the channels?";

	} else {
		dialogTitle = action + " Channels";
		dialogMessage = "Are you sure you want to '" + action + "' the channels?";
	}

	channelActionDialogTitleDiv.innerHTML = dialogTitle;
	channelActionDialogMessageDiv.innerHTML = dialogMessage;

	document.getElementById('channelActionDialog').showModal();
}

$(document).on("click", "#okChannelAction", function() {
	document.getElementById("main_list").submit();
});

$(document).on("click", "#cancelChannelAction", function() {
	document.getElementById('main_list').reset();
	document.getElementById('channelActionDialog').close();
});

//-----------------------------------------------------------------------
// Delete Channels
//-----------------------------------------------------------------------
$(document).on("click", "#actionDeleteChannels", function() {
	document.getElementById('deleteChannelsDialog').showModal();
});

$(document).on("click", "#okDeleteChannels", function() {
	var form = document.getElementById("delete_form");

	var checkboxes = document.getElementsByName("channelId");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].checked) {
			var channelId = checkboxes[i].value;

			var channelIdField = document.createElement("input");
			channelIdField.setAttribute("type", "hidden");
			channelIdField.setAttribute("name", "channelId");
			channelIdField.setAttribute("value", channelId);
			form.appendChild(channelIdField);
		}
	}

	form.submit();
	document.getElementById('deleteChannelsDialog').close();
});

$(document).on("click", "#cancelDeleteChannels", function() {
	document.getElementById('main_list').reset();
	document.getElementById('deleteChannelsDialog').close();
});
