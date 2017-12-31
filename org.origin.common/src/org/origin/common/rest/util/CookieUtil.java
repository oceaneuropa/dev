package org.origin.common.rest.util;

import javax.servlet.http.Cookie;

public class CookieUtil {

	/**
	 * 
	 * @param name
	 * @param value
	 * @param secure
	 * @param expireIn
	 * @param path
	 * @return
	 */
	public static Cookie create(String name, String value, boolean secure, int expireIn, String path) {
		Cookie cookie = new Cookie(name, value);
		// determines whether the cookie should only be sent using a secure protocol, such as HTTPS or SSL
		cookie.setSecure(secure);

		// A negative value means that the cookie is not stored persistently and will be deleted when the Web browser exits. A zero value causes the
		// cookie to be deleted.
		cookie.setMaxAge(expireIn);

		// The cookie is visible to all the pages in the directory you specify, and all the pages in that directory's subdirectories
		cookie.setPath(path);

		return cookie;
	}

}
