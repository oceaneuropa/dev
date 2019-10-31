package org.orbit.component.webconsole.util;

import org.orbit.platform.sdk.http.HttpContextProvider;
import org.osgi.service.http.HttpContext;

/**
 * 
 * @see org.orbit.platform.sdk.http.PlatformHttpContextProvider
 */
public class OrbitHttpContextProvider implements HttpContextProvider {

	public static String ID = "org.orbit.component.webconsole.util.OrbitHttpContextProvider";

	protected OrbitHttpContext httpContext;

	@Override
	public synchronized HttpContext getHttpContext() {
		if (this.httpContext == null) {
			this.httpContext = new OrbitHttpContext(null);
		}
		return this.httpContext;
	}

}
