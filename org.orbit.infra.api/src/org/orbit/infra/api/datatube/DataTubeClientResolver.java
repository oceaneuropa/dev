package org.orbit.infra.api.datatube;

import java.io.IOException;
import java.util.Comparator;

public interface DataTubeClientResolver {

	/**
	 * 
	 * @param dataTubeServiceUrl
	 * @param accessToken
	 * @return
	 */
	DataTubeClient resolve(String dataTubeServiceUrl, String accessToken);

	/**
	 * 
	 * @param dataCastId
	 * @param dataTubeId
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	String getURL(String dataCastId, String dataTubeId, String accessToken) throws IOException;

	/**
	 * 
	 * @param dataCastId
	 * @param accessToken
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	DataTubeClient[] resolve(String dataCastId, String accessToken, Comparator<?> comparator) throws IOException;

	/**
	 * 
	 * @param dataCastId
	 * @param dataTubeId
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	DataTubeClient resolve(String dataCastId, String dataTubeId, String accessToken) throws IOException;

}
