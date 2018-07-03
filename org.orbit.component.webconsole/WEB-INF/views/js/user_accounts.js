
	function addUser() {
		var addUserDialog = document.getElementById('newUserDialog');
		addUserDialog.showModal();
	}

	function changeUser(userId, password, firstName, lastName, email, phone) {
		document.getElementById("user_id").setAttribute('value',userId);
		document.getElementById("user_password").setAttribute('value',password);
		document.getElementById("user_firstName").setAttribute('value',firstName);
		document.getElementById("user_lastName").setAttribute('value',lastName);
		document.getElementById("user_email").setAttribute('value',email);
		document.getElementById("user_phone").setAttribute('value',phone);

		var changeUserDialog = document.getElementById('changeUserDialog');
		changeUserDialog.showModal();
	}

	function deleteUser(userId) {
		var actionURL = "<%=contextRoot + "/useraccountdelete"%>";
		var dialog = document.getElementById('deleteUserDialog');

		var messageDiv = document.getElementById('deleteUserDialogMessageDiv');
		messageDiv.innerHTML="Are you sure you want to delete user '"+userId+"'?";

		var okButton = document.getElementById('doDeleteUser');
		okButton.addEventListener('click', function() {
			var form = document.createElement("form");
			form.setAttribute("method", "post");
			form.setAttribute("action", actionURL);

			var idField = document.createElement("input");
			idField.setAttribute("type", "hidden");
			idField.setAttribute("name", "id");
			idField.setAttribute("value", userId);

			form.appendChild(idField);

			document.body.appendChild(form);
			form.submit();
			document.body.removeChild(form);
		});

		dialog.showModal();
	}

	function deleteUsers() {
		var actionURL = "<%=contextRoot + "/useraccountdelete"%>";
		var dialog = document.getElementById('deleteUsersDialog');

		var messageDiv = document.getElementById('deleteUsersDialogMessageDiv');
		messageDiv.innerHTML="Are you sure you want to delete selected users?";

		var okButton = document.getElementById('doDeleteUsers');
		okButton.addEventListener('click', function() {
			var form = document.createElement("form");
			form.setAttribute("method", "post");
			form.setAttribute("action", actionURL);

			var idField = document.createElement("input");
			idField.setAttribute("type", "hidden");
			idField.setAttribute("name", "id");
			idField.setAttribute("value", userId);

			form.appendChild(idField);

			document.body.appendChild(form);
			form.submit();
			document.body.removeChild(form);
		});

		dialog.showModal();
	}

	(function() {
		var newUserDialog = document.getElementById('newUserDialog');
		var cancelAddUserButton = document.getElementById('cancelAddUser');
		cancelAddUserButton.addEventListener('click', function() {
			newUserDialog.close();
		});

		var changeUserDialog = document.getElementById('changeUserDialog');
		var cancelChangeUserButton = document.getElementById('cancelChangeUser');
		cancelChangeUserButton.addEventListener('click', function() {
			changeUserDialog.close();
		});

		var deleteUserDialog = document.getElementById('deleteUserDialog');
		var cancelDeleteUserButton = document.getElementById('cancelDeleteUser');
		cancelDeleteUserButton.addEventListener('click', function() {
			deleteUserDialog.close();
		});

		var deleteUsersDialog = document.getElementById('deleteUsersDialog');
		var cancelDeleteUsersButton = document.getElementById('cancelDeleteUsers');
		cancelDeleteUsersButton.addEventListener('click', function() {
			deleteUsersDialog.close();
		});

	})();
