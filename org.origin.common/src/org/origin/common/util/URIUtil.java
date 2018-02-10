package org.origin.common.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class URIUtil {

	/**
	 * Resolves a relative URI.
	 * 
	 * @param base
	 * @param uri
	 * @return
	 */
	public static URI resolve(final URI base, final URI uri) {
		if (base.equals(uri)) {
			return uri;
		} else {
			if (!uri.isAbsolute()) {
				return base.resolve(uri);
			} else {
				return uri;
			}
		}
	}

	/**
	 * Convert a ";" separated string to a list of host URI and append context root to each of the host URI in the list.
	 * 
	 * @param hostURLsString
	 * @param contextRoot
	 * @return
	 */
	public static List<URI> toList(String hostURLsString, String contextRoot) {
		if (contextRoot == null) {
			return toList(hostURLsString);
		}

		List<URI> uriList = new ArrayList<URI>();

		List<URI> hostURIs = toList(hostURLsString);
		for (URI hostURI : hostURIs) {
			String uriString = hostURI.toString();
			if (!uriString.endsWith("/") && !contextRoot.startsWith("/")) {
				uriString += "/";
			}
			uriString += contextRoot;

			try {
				uriList.add(new URI(uriString));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		return uriList;
	}

	/**
	 * Convert a ";" separated string to a list of URI.
	 * 
	 * If the string does not contain ";", the string is considered as a single URI.
	 * 
	 * @param string
	 * @return
	 */
	public static List<URI> toList(String string) {
		List<URI> uriList = null;
		if (string != null) {
			String[] segments = null;

			if (string.contains(";")) {
				// multiple URLs in the string, separated by ';'
				segments = string.split(";");

			} else if (string.contains(",")) {
				// multiple URLs in the string, separated by ','
				segments = string.split(",");

			} else {
				// single URL in the string
				segments = new String[] { string };
			}

			if (segments != null) {
				uriList = toList(segments);
			}
		}
		if (uriList == null) {
			uriList = new ArrayList<URI>();
		}
		return uriList;
	}

	/**
	 * Convert string array to a list of URI.
	 * 
	 * @param uriStrings
	 * @return
	 */
	public static List<URI> toList(String... uriStrings) {
		List<URI> uriList = new ArrayList<URI>();
		if (uriStrings != null) {
			for (String uriString : uriStrings) {
				try {
					uriList.add(new URI(uriString));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
		return uriList;
	}

}
