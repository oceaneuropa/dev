package org.origin.common.rest.client;

import java.io.IOException;

import javax.ws.rs.core.Response;

public class ClientUtil {

	/**
	 * Close the Closeable objects and <b>ignore</b> any {@link IOException} or null pointers. Must only be used for cleanup in exception handlers.
	 * 
	 * @param response
	 *            Close javax.ws.rs.core.Response.
	 * @param printStackTrace
	 */
	public static void closeQuietly(Response response, boolean printStackTrace) {
		try {
			if (response != null) {
				response.close();
			}
		} catch (Exception ioe) {
			if (printStackTrace) {
				ioe.printStackTrace();
			} else {
				// ignore
			}
		}
	}

}
