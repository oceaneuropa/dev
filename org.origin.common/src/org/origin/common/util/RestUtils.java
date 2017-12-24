package org.origin.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang3.StringUtils;

/**
 * A few helper methods for handling REST requests and responses.
 * 
 * @see https://www.programcreek.com/java-api-examples/index.php?source_dir=para-master/para-server/src/main/java/com/erudika/para/rest/RestUtils.java
 * 
 * @see https://github.com/Erudika/para.git
 * 
 * @see https://mvnrepository.com/artifact/com.erudika
 * 
 */
public final class RestUtils {

	/**
	 * Extracts the access key from a request. It can be a header or a parameter.
	 * 
	 * @param request
	 *            a request
	 * @return the access key
	 */
	public static String extractAccessKey(HttpServletRequest request) {
		if (request == null) {
			return "";
		}
		String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.isBlank(auth)) {
			auth = request.getParameter("X-Amz-Credential");
			if (StringUtils.isBlank(auth)) {
				return "";
			} else {
				return StringUtils.substringBefore(auth, "/");
			}
		} else {
			String credential = StringUtils.substringBetween(auth, "Credential=", ",");
			return StringUtils.substringBefore(credential, "/");
		}
	}

	/**
	 * Extracts the date field from a request. It can be a header or a parameter.
	 * 
	 * @param request
	 *            a request
	 * @return the date
	 */
	public static String extractDate(HttpServletRequest request) {
		if (request == null) {
			return "";
		}
		String date = request.getHeader("X-Amz-Date");
		if (StringUtils.isBlank(date)) {
			return request.getParameter("X-Amz-Date");
		} else {
			return date;
		}
	}

	/**
	 * Extracts the resource name, for example '/v1/_some_resource_name' returns '_some_resource_name'.
	 * 
	 * @param request
	 *            a request
	 * @return the resource name
	 */
	public static String extractResourceName(HttpServletRequest request) {
		if (request == null || request.getRequestURI().length() <= 3) {
			return "";
		}
		String uri = request.getRequestURI().substring(1);
		int start = uri.indexOf("/");

		if (start >= 0 && start + 1 < uri.length()) {
			int end = uri.substring(start + 1).indexOf("/") + start + 1;
			if (end > start) {
				return uri.substring(start + 1, end);
			} else {
				return uri.substring(start + 1);
			}
		} else {
			return "";
		}
	}

}
