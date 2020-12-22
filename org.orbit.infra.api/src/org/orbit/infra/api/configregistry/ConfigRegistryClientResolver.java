package org.orbit.infra.api.configregistry;

public interface ConfigRegistryClientResolver {

	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	ConfigRegistryClient resolve(String accessToken);

}

/*-
 * 
 * @param configRegistryName
 * @param accessToken
 * @return
 * @throws IOException
String getURL(String configRegistryName, String accessToken) throws IOException;
 */
