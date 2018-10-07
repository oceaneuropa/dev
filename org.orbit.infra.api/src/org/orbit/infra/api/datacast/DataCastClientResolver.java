package org.orbit.infra.api.datacast;

import java.io.IOException;

public interface DataCastClientResolver {

	/**
	 * 
	 * @param dataCastServiceUrl
	 * @param accessToken
	 * @return
	 */
	DataCastClient resolve(String dataCastServiceUrl, String accessToken);

	/**
	 * 
	 * @param dataCastId
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	String getURL(String dataCastId, String accessToken) throws IOException;

}
