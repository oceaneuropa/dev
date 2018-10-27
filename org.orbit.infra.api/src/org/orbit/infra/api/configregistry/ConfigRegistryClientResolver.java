package org.orbit.infra.api.configregistry;

import java.io.IOException;

public interface ConfigRegistryClientResolver {

	/**
	 * 
	 * @param dfsServiceUrl
	 * @param accessToken
	 * @return
	 */
	ConfigRegistryClient resolve(String configRegistryServiceUrl, String accessToken);

	/**
	 * 
	 * @param configRegistryName
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	String getURL(String configRegistryName, String accessToken) throws IOException;

}