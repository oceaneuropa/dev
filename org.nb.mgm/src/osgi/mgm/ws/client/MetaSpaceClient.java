package osgi.mgm.ws.client;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import osgi.mgm.common.util.ClientConfiguration;
import osgi.mgm.common.util.ClientException;
import osgi.mgm.ws.dto.MetaSpaceDTO;
import osgi.mgm.ws.dto.StatusDTO;

/**
 * MetaSpace web service client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces?filter={filter}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces (Body parameter: MetaSpaceDTO)
 * 
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces (Body parameter: MetaSpaceDTO)
 * 
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
 * 
 */
public class MetaSpaceClient extends AbstractMgmClient {

	/**
	 * 
	 * @param config
	 */
	public MetaSpaceClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Get all MetaSpaces in a MetaSector.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSpaceDTO> getMetaSpaces(String metaSectorId) throws ClientException {
		List<MetaSpaceDTO> metaSpaces = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("metaspaces").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			metaSpaces = response.readEntity(new GenericType<List<MetaSpaceDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		}
		if (metaSpaces == null) {
			metaSpaces = Collections.emptyList();
		}
		return metaSpaces;
	}

	/**
	 * Get MetaSpaces in a MetaSector by filter.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces?filter={filter}
	 * 
	 * @param metaSectorId
	 * @param filter
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSpaceDTO> getMetaSpaces(String metaSectorId, String filter) throws ClientException {
		List<MetaSpaceDTO> metaSpaces = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("metaspaces").queryParam("filter", filter).request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			metaSpaces = response.readEntity(new GenericType<List<MetaSpaceDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		}
		if (metaSpaces == null) {
			metaSpaces = Collections.emptyList();
		}
		return metaSpaces;
	}

	/**
	 * Get MetaSpace by metaSector Id and metaSpace Id.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public MetaSpaceDTO getMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException {
		MetaSpaceDTO metaSpace = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("metaspaces").path(metaSpaceId).request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			metaSpace = response.readEntity(MetaSpaceDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return metaSpace;
	}

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * Request URL (POST): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces
	 * 
	 * @param metaSectorId
	 * @param metaSpace
	 *            Body parameter for the new MetaSpace.
	 * 
	 * @return new MetaSpace
	 * @throws ClientException
	 */
	public MetaSpaceDTO addMetaSpace(String metaSectorId, MetaSpaceDTO metaSpace) throws ClientException {
		MetaSpaceDTO newMetaSpace = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("metaspaces").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.json(new GenericEntity<MetaSpaceDTO>(metaSpace) {
			}));
			checkResponse(response);

			newMetaSpace = response.readEntity(MetaSpaceDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return newMetaSpace;
	}

	/**
	 * Update MetaSpace information.
	 * 
	 * Request URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces
	 * 
	 * @param metaSectorId
	 * @param metaSpace
	 *            Body parameter for updating the MetaSpace.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateMetaSpace(String metaSectorId, MetaSpaceDTO metaSpace) throws ClientException {
		StatusDTO status = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("metaspaces").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).put(Entity.json(new GenericEntity<MetaSpaceDTO>(metaSpace) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return status;
	}

	/**
	 * Delete a MetaSpace from a MetaSector.
	 * 
	 * Request URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/metaspaces/{metaSpaceId}
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO deleteMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException {
		StatusDTO status = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("metaspaces").path(metaSpaceId).request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return status;
	}

}
