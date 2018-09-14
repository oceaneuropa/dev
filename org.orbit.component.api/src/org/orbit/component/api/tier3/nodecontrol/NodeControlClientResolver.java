package org.orbit.component.api.tier3.nodecontrol;

import java.io.IOException;

public interface NodeControlClientResolver {

	/**
	 * 
	 * @param accessToken
	 * @param platformId
	 * @return
	 * @throws IOException
	 */
	NodeControlClient resolve(String accessToken, String platformId) throws IOException;

}
