//-----------------------------------------------------------------------
// Selection
//-----------------------------------------------------------------------
function toggleSelection(source, checkboxesName) {
	var checkboxes = document.getElementsByName(checkboxesName);
	var n = checkboxes.length;
	for (var i = 0; i < n; i++) {
		checkboxes[i].checked = source.checked;
	}
}


//-----------------------------------------------------------------------
// Create config reg
//-----------------------------------------------------------------------
$(document).on("click", "#actionAddConfigReg", function() {
	document.getElementById('newConfigRegDialog').showModal();
});

$(document).on("click", "#okAddConfigReg", function() {
	var form = document.getElementById("new_form");
	form.submit();
});

$(document).on("click", "#cancelAddConfigReg", function() {
	document.getElementById('new_form').reset();
	document.getElementById('newConfigRegDialog').close();
});


//-----------------------------------------------------------------------
// Change config reg
//-----------------------------------------------------------------------
function changeConfigReg(configRegistryId, type, name) {
	document.getElementById("config_reg__configRegistryId").setAttribute('value', configRegistryId);
	document.getElementById("config_reg__type").setAttribute('value', type);
	document.getElementById("config_reg__name").setAttribute('value', name);

	document.getElementById('changeConfigRegDialog').showModal();
}

$(document).on("click", "#okChangeConfigReg", function() {
	document.getElementById("update_form").submit();
});

$(document).on("click", "#cancelChangeConfigReg", function() {
	document.getElementById('update_form').reset();
	document.getElementById('changeConfigRegDialog').close();
});


//-----------------------------------------------------------------------
// Delete config regs
//-----------------------------------------------------------------------
$(document).on("click", "#actionDeleteConfigRegs", function() {
	document.getElementById('deleteConfigRegsDialog').showModal();
});

$(document).on("click", "#okDeleteConfigRegs", function() {
	var form = document.getElementById("delete_form");

	var checkboxes = document.getElementsByName("configRegistryId");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].checked) {
			var configRegistryId = checkboxes[i].value;

			if (configRegistryId != null && configRegistryId != "") {
				var configRegistryIdField = document.createElement("input");
				configRegistryIdField.setAttribute("type", "hidden");
				configRegistryIdField.setAttribute("name", "configRegistryId");
				configRegistryIdField.setAttribute("value", configRegistryId);
				form.appendChild(configRegistryIdField);
			}
		}
	}

	form.submit();
	document.getElementById('deleteConfigRegsDialog').close();
});

$(document).on("click", "#cancelDeleteConfigRegs", function() {
	document.getElementById('main_list').reset();
	document.getElementById('deleteConfigRegsDialog').close();
});


//-----------------------------------------------------------------------
// Delete config reg
//-----------------------------------------------------------------------
function deleteConfigReg(configRegistryId) {
	document.getElementById('delete_form2_configRegistryId').value = configRegistryId;
	document.getElementById('deleteConfigRegDialog').showModal();
}

$(document).on("click", "#okDeleteConfigReg", function() {
	document.getElementById('delete_form2').submit();
});

$(document).on("click", "#cancelDeleteConfigReg", function() {
	document.getElementById('deleteConfigRegDialog').close();
	document.getElementById('delete_form2_configRegistryId').value = null;
});
