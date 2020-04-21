package org.orbit.component.webconsole.servlet.tier1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.util.IdentityServiceUtil;
import org.orbit.component.api.util.UserAccountUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.spirit.model.userprograms.UserProgram;
import org.orbit.spirit.model.userprograms.UserPrograms;
import org.orbit.spirit.resource.util.UserProgramsHelper;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class UserProgramActionServlet extends HttpServlet {

	private static final long serialVersionUID = 1007647984613608485L;

	public static String ACTION__REMOVE = "remove";

	public static List<String> ACTIONS = new ArrayList<String>();

	static {
		ACTIONS.add(ACTION__REMOVE);
	}

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String identityServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_IDENTITY_SERVICE_URL);
		String userRegistryUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_USER_ACCOUNTS_URL);

		String accountId = ServletUtil.getParameter(request, "accountId", "");
		String[] idVersions = ServletUtil.getParameterValues(request, "id_version", EMPTY_IDS);
		String action = ServletUtil.getParameter(request, "action", "");

		String message = "";
		if (accountId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (idVersions.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "Programs are not selected.");
		}
		if (action.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'action' parameter is not set.");
		}
		if (!ACTIONS.contains(action)) {
			message = MessageHelper.INSTANCE.add(message, "Action '" + action + "' is not supported. Supported actions: " + Arrays.toString(ACTIONS.toArray(new String[ACTIONS.size()])) + ".");
		}

		boolean succeed = false;

		if (!accountId.isEmpty() && idVersions.length > 0) {
			try {
				// Note:
				// Use admin user's access token to get the account information of other user with given accountId.
				// This is for testing only. In real world, admin user should never be allowed to access other people's data(programs, files, etc).
				String adminUserAccessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);
				UserAccount userAccount = UserAccountUtil.getUserAccount(userRegistryUrl, adminUserAccessToken, accountId);

				if (userAccount != null) {
					String username = userAccount.getUsername();
					String email = userAccount.getEmail();
					String password = userAccount.getPassword();
					LoginResponse loginResponse = IdentityServiceUtil.login(identityServiceUrl, username, email, password);

					if (loginResponse != null) {
						// @see org.orbit.component.webconsole.servlet.tier1.identity.SignInServlet
						boolean signInSucceed = loginResponse.isSucceed();
						if (signInSucceed) {
							String accessToken = loginResponse.getAccessToken();

							UserPrograms userPrograms = null;
							try {
								userPrograms = UserProgramsHelper.INSTANCE.getUserPrograms(accessToken);

							} catch (Exception e) {
								message = MessageHelper.INSTANCE.add(message, "Cannot getUserPrograms: '" + e.getMessage() + "'.");
							}
							if (userPrograms == null) {
								userPrograms = UserProgramsHelper.INSTANCE.createUserPrograms(accessToken);
							}
							if (userPrograms == null) {
								message = MessageHelper.INSTANCE.add(message, "UserPrograms is not found.");
							}

							List<UserProgram> programsToRemove = new ArrayList<UserProgram>();
							for (UserProgram userProgram : userPrograms.getChildren()) {
								boolean doRemove = false;

								String programId = userProgram.getId();
								String programVersion = userProgram.getVersion();
								if (programId == null) {
									programId = "";
								}
								if (programVersion == null) {
									programVersion = "";
								}

								for (int i = 0; i < idVersions.length; i++) {
									String currIdVersion = idVersions[i];
									int index = currIdVersion.lastIndexOf("_");
									String currAppId = currIdVersion.substring(0, index);
									String currAppVersion = currIdVersion.substring(index + 1);

									if (programId.equals(currAppId) && programVersion.equals(currAppVersion)) {
										doRemove = true;
										break;
									}
								}

								if (doRemove) {
									programsToRemove.add(userProgram);
								}
							}

							boolean doSave = false;
							if (!programsToRemove.isEmpty()) {
								userPrograms.getChildren().removeAll(programsToRemove);
								doSave = true;
							}
							if (doSave) {
								UserProgramsHelper.INSTANCE.saveUserPrograms(accessToken, userPrograms);
								succeed = true;
							}
						}
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (ACTION__REMOVE.equals(action)) {
			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, (idVersions.length > 1) ? "Programs are removed." : "Program is removed.");
			} else {
				message = MessageHelper.INSTANCE.add(message, (idVersions.length > 1) ? "Programs are not removed." : "Program is not removed.");
			}
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/userprograms?accountId=" + accountId);
	}

}
