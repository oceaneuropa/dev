package org.orbit.component.webconsole.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.tier1.identity.RefreshTokenResponse;
import org.orbit.component.api.util.ComponentsConfigPropertiesHandler;
import org.orbit.component.api.util.IdentityServiceUtil;
import org.orbit.component.webconsole.servlet.tier1.SignInServlet;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.http.PlatformHttpContext;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.osgi.service.http.HttpContext;

/**
 * 
 * @see SignInServlet
 */
public class OrbitHttpContext extends PlatformHttpContext {

	/**
	 * 
	 * @param httpContext
	 */
	public OrbitHttpContext(HttpContext httpContext) {
		super(httpContext);
	}

	@Override
	protected boolean validateToken(HttpServletRequest request, HttpServletResponse response) {
		boolean isTokenValid = false;
		HttpSession session = request.getSession(false);

		if (session != null) {
			String accessToken = (String) session.getAttribute(PlatformConstants.SESSION__ORBIT_ACCESS_TOKEN);
			isTokenValid = OrbitTokenUtil.INSTANCE.isTokenValid(PlatformConstants.TOKEN_PROVIDER__ORBIT, accessToken);

			if (!isTokenValid) {
				// refresh token
				String refreshToken = (String) session.getAttribute(PlatformConstants.SESSION__ORBIT_REFRESH_TOKEN);
				boolean isRefreshTokenValid = OrbitTokenUtil.INSTANCE.isTokenValid(PlatformConstants.TOKEN_PROVIDER__ORBIT, refreshToken);

				if (isRefreshTokenValid) {
					try {
						String identityServiceUrl = ComponentsConfigPropertiesHandler.getInstance().getIdentityServiceURL();
						RefreshTokenResponse refreshTokenResponse = IdentityServiceUtil.refreshToken(identityServiceUrl, refreshToken);

						if (refreshTokenResponse != null && refreshTokenResponse.isSucceed()) {
							OrbitTokenUtil.INSTANCE.updateSession(request, PlatformConstants.TOKEN_PROVIDER__ORBIT, refreshTokenResponse.getTokenType(), refreshTokenResponse.getAccessToken(), refreshTokenResponse.getRefreshToken());
							isTokenValid = true;
						}

					} catch (ClientException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return isTokenValid;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@Override
	protected boolean isPublic(HttpServletRequest request) {
		// Note:
		// 1. public servlets should have public url patterns. hard code for now
		// 2. @see https://stackoverflow.com/questions/36055411/the-localhost-page-isn-t-working-localhost-redirected-you-too-many-times
		String requestURL = request.getRequestURI();
		if (requestURL != null) {
			if (requestURL.contains("/orbit/webconsole/public/")) {
				return true;
			}
			if (requestURL.startsWith("/main")) {
				return true;
			}
			if (requestURL.endsWith(".css") || requestURL.endsWith(".js")) {
				return true;
			}
		}
		return false;
	}

}
