package org.origin.common.rest.client;

import java.net.HttpCookie;
import java.net.URI;

interface CookieManager {

	void setCookies(URI uri, String headerValue);

	HttpCookie[] getCookies();

	HttpCookie[] getCookies(URI uri);

	void removeAll();

}
