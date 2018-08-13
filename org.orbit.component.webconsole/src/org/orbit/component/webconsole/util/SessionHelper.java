package org.orbit.component.webconsole.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.http.JWTTokenHandler;
import org.orbit.platform.sdk.util.ExtensionHelper;
import org.origin.common.rest.client.Realms;
import org.origin.common.rest.client.Token;

public class SessionHelper {

	public static SessionHelper INSTANCE = new SessionHelper();

	/**
	 * 
	 * @param request
	 * @param loginResponse
	 * @throws Exception
	 */
	public void updateSession(HttpServletRequest request, LoginResponse loginResponse) throws Exception {
		String tokenType = loginResponse.getTokenType();
		String tokenValue = loginResponse.getTokenValue();
		Map<String, String> payload = ExtensionHelper.JWT.getTokenPayload(WebConstants.TOKEN_PROVIDER__ORBIT, tokenValue);
		if (payload != null) {
			String username = payload.get(JWTTokenHandler.PAYLOAD__USERNAME);
			String firstName = payload.get(JWTTokenHandler.PAYLOAD__FIRST_NAME);
			String lastName = payload.get(JWTTokenHandler.PAYLOAD__LAST_NAME);

			String fullName = null;
			if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) {
				fullName = firstName + " " + lastName;
			} else {
				fullName = username;
			}

			HttpSession session = request.getSession();
			session.setAttribute(WebConstants.SESSION__USERNAME, username);
			session.setAttribute(WebConstants.SESSION__FULLNAME, fullName);
			session.setAttribute(WebConstants.SESSION__TOKEN_TYPE, tokenType);
			session.setAttribute(WebConstants.SESSION__ACCESS_TOKEN, tokenValue);

			Token token = new Token(tokenType, tokenValue);
			Realms.getRealm(Realms.DEFAULT_REALM).setToken(username, token);
		}
	}

}
