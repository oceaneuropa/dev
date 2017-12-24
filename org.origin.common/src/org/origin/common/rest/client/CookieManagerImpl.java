package org.origin.common.rest.client;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CookieManagerImpl implements CookieManager {

	public static Logger LOG = LoggerFactory.getLogger(CookieManagerImpl.class);

	private CookieStore cookieStore;

	public CookieManagerImpl() {
		this.cookieStore = new java.net.CookieManager().getCookieStore();
	}

	@Override
	public void setCookies(URI uri, String headerValue) {
		if (uri == null) {
			throw new IllegalArgumentException("URI is null");
		}
		if (headerValue == null) {
			LOG.info("headerValue is null");
			return;
		}

		try {
			List<HttpCookie> cookies = null;
			try {
				cookies = HttpCookie.parse(headerValue);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				LOG.info("Invalid cookie for " + uri + ": " + headerValue);
			}
			if (cookies == null) {
				cookies = Collections.emptyList();
			}

			for (HttpCookie cookie : cookies) {
				if (cookie.getPath() == null) {
					// If no path is specified, then by default
					// the path is the directory of the page/doc
					String path = uri.getPath();
					if (!path.endsWith("/")) {
						int i = path.lastIndexOf("/");
						if (i > 0) {
							path = path.substring(0, i + 1);
						} else {
							path = "/";
						}
					}
					cookie.setPath(path);
				}

				// As per RFC 2965, section 3.3.1:
				// Domain Defaults to the effective request-host. (Note that because
				// there is no dot at the beginning of effective request-host,
				// the default Domain can only domain-match itself.)
				if (cookie.getDomain() == null) {
					String host = uri.getHost();
					if (host != null && !host.contains(".")) {
						host += ".local";
					}
					cookie.setDomain(host);
				}

				String ports = cookie.getPortlist();
				if (ports != null) {
					int port = uri.getPort();
					if (port == -1) {
						port = "https".equals(uri.getScheme()) ? 443 : 80;
					}

					if (ports.isEmpty()) {
						// Empty port list means this should be restricted
						// to the incoming URI port
						cookie.setPortlist("" + port);
						if (shouldAcceptInternal(uri, cookie)) {
							this.cookieStore.add(uri, cookie);
						}

					} else {
						// Only store cookies with a port list
						// IF the URI port is in that list, as per
						// RFC 2965 section 3.3.2
						if (isInPortList(ports, port) && shouldAcceptInternal(uri, cookie)) {
							this.cookieStore.add(uri, cookie);
						}
					}

				} else {
					if (shouldAcceptInternal(uri, cookie)) {
						this.cookieStore.add(uri, cookie);
					}
				}
			}

		} catch (IllegalArgumentException e) {
			// invalid set-cookie header string
			e.printStackTrace();
		}
	}

	@Override
	public HttpCookie[] getCookies() {
		// The API returns an immutable list of http cookies; Return empty list if there's no http cookie in store.
		List<HttpCookie> notExpiredCookies = this.cookieStore.getCookies();
		if (notExpiredCookies == null) {
			return new HttpCookie[] {};
		}
		return notExpiredCookies.toArray(new HttpCookie[notExpiredCookies.size()]);
	}

	@Override
	public HttpCookie[] getCookies(URI uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI is null");
		}

		boolean secureLink = "https".equalsIgnoreCase(uri.getScheme());
		List<HttpCookie> cookies = new java.util.ArrayList<HttpCookie>();
		String path = uri.getPath();
		if (path == null || path.isEmpty()) {
			path = "/";
		}

		for (HttpCookie cookie : this.cookieStore.get(uri)) {
			// apply path-matches rule (RFC 2965 sec. 3.3.4)
			// and check for the possible "secure" tag (i.e. don't send
			// 'secure' cookies over unsecure links)
			if (pathMatches(path, cookie.getPath()) && (secureLink || !cookie.getSecure())) {
				// Enforce httponly attribute
				if (cookie.isHttpOnly()) {
					String s = uri.getScheme();
					if (!"http".equalsIgnoreCase(s) && !"https".equalsIgnoreCase(s)) {
						continue;
					}
				}
				// Let's check the authorize port list if it exists
				String ports = cookie.getPortlist();
				if (ports != null && !ports.isEmpty()) {
					int port = uri.getPort();
					if (port == -1) {
						port = "https".equals(uri.getScheme()) ? 443 : 80;
					}
					if (isInPortList(ports, port)) {
						cookies.add(cookie);
					}
				} else {
					cookies.add(cookie);
				}
			}
		}

		// apply sort rule (RFC 2965 sec. 3.3.4)
		Collections.sort(cookies, new CookiePathComparator());

		return cookies.toArray(new HttpCookie[cookies.size()]);
	}

	@Override
	public void removeAll() {
		this.cookieStore.removeAll();
	}

	/* ---------------- Private operations -------------- */

	// to determine whether or not accept this cookie
	private boolean shouldAcceptInternal(URI uri, HttpCookie cookie) {
		try {
			// return policyCallback.shouldAccept(uri, cookie);
			return true;
		} catch (Exception ignored) { // pretect against malicious callback
			return false;
		}
	}

	static private boolean isInPortList(String lst, int port) {
		int i = lst.indexOf(",");
		int val = -1;
		while (i > 0) {
			try {
				val = Integer.parseInt(lst.substring(0, i));
				if (val == port) {
					return true;
				}
			} catch (NumberFormatException numberFormatException) {
			}
			lst = lst.substring(i + 1);
			i = lst.indexOf(",");
		}
		if (!lst.isEmpty()) {
			try {
				val = Integer.parseInt(lst);
				if (val == port) {
					return true;
				}
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return false;
	}

	/*
	 * path-matches algorithm, as defined by RFC 2965
	 */
	private boolean pathMatches(String path, String pathToMatchWith) {
		if (path == pathToMatchWith)
			return true;
		if (path == null || pathToMatchWith == null)
			return false;
		if (path.startsWith(pathToMatchWith))
			return true;

		return false;
	}

	/*
	 * sort cookies with respect to their path: those with more specific Path attributes precede those with less specific, as defined in RFC 2965 sec. 3.3.4
	 */
	private List<String> sortByPath(List<HttpCookie> cookies) {
		Collections.sort(cookies, new CookiePathComparator());

		List<String> cookieHeader = new java.util.ArrayList<String>();
		for (HttpCookie cookie : cookies) {
			// Netscape cookie spec and RFC 2965 have different format of Cookie
			// header; RFC 2965 requires a leading $Version="1" string while Netscape
			// does not.
			// The workaround here is to add a $Version="1" string in advance
			if (cookies.indexOf(cookie) == 0 && cookie.getVersion() > 0) {
				cookieHeader.add("$Version=\"1\"");
			}

			cookieHeader.add(cookie.toString());
		}
		return cookieHeader;
	}

	static class CookiePathComparator implements Comparator<HttpCookie> {
		public int compare(HttpCookie c1, HttpCookie c2) {
			if (c1 == c2)
				return 0;
			if (c1 == null)
				return -1;
			if (c2 == null)
				return 1;

			// path rule only applies to the cookies with same name
			if (!c1.getName().equals(c2.getName()))
				return 0;

			// those with more specific Path attributes precede those with less specific
			if (c1.getPath().startsWith(c2.getPath()))
				return -1;
			else if (c2.getPath().startsWith(c1.getPath()))
				return 1;
			else
				return 0;
		}
	}

}
