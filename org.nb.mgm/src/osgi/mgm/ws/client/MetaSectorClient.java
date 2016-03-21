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
import osgi.mgm.ws.dto.MetaSectorDTO;
import osgi.mgm.ws.dto.StatusDTO;

/**
 * MetaSector web service client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/?filter={filter}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/metasectors/ (Body parameter: MetaSectorDTO)
 * 
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/metasectors/ (Body parameter: MetaSectorDTO)
 * 
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
 * 
 */
public class MetaSectorClient extends AbstractMgmClient {

	/**
	 * 
	 * @param config
	 */
	public MetaSectorClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Get all MetaSectors.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSectorDTO> getMetaSectors() throws ClientException {
		List<MetaSectorDTO> metaSectors = null;
		try {
			Builder builder = getRootPath().path("metasectors").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			metaSectors = response.readEntity(new GenericType<List<MetaSectorDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		}
		if (metaSectors == null) {
			metaSectors = Collections.emptyList();
		}
		return metaSectors;
	}

	/**
	 * Get MetaSectors by filter.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/?filter={filter}
	 * 
	 * @param filter
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSectorDTO> getMetaSectors(String filter) throws ClientException {
		List<MetaSectorDTO> metaSectors = null;
		try {
			Builder builder = getRootPath().path("metasectors").queryParam("filter", filter).request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			metaSectors = response.readEntity(new GenericType<List<MetaSectorDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		}
		if (metaSectors == null) {
			metaSectors = Collections.emptyList();
		}
		return metaSectors;
	}

	/**
	 * Get MetaSector by metaSector Id.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
	 * 
	 * @param metaSectorId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public MetaSectorDTO getMetaSector(String metaSectorId) throws ClientException {
		MetaSectorDTO metaSector = null;
		try {
			Builder builder = getRootPath().path("metasectors").path(metaSectorId).request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			metaSector = response.readEntity(MetaSectorDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return metaSector;
	}

	/**
	 * Add a MetaSector to the cluster.
	 * 
	 * Request URL (POST): {scheme}://{host}:{port}/{contextRoot}/metasectors
	 * 
	 * @param metaSector
	 *            Body parameter for the new MetaSector.
	 * 
	 * @return new MetaSector
	 * @throws ClientException
	 */
	public MetaSectorDTO createMetaSector(MetaSectorDTO metaSector) throws ClientException {
		MetaSectorDTO newMetaSector = null;
		try {
			Builder builder = getRootPath().path("metasectors").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.json(new GenericEntity<MetaSectorDTO>(metaSector) {
			}));
			checkResponse(response);

			newMetaSector = response.readEntity(MetaSectorDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return newMetaSector;
	}

	/**
	 * Update MetaSector information.
	 * 
	 * Request URL (PUT): {scheme}://{host}:{port}/{contextRoot}/metasectors (Body parameter: MetaSectorDTO)
	 * 
	 * @param metaSector
	 *            Body parameter for updating the MetaSector.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateMetaSector(MetaSectorDTO metaSector) throws ClientException {
		StatusDTO status = null;
		try {
			Builder builder = getRootPath().path("metasectors").request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).put(Entity.json(new GenericEntity<MetaSectorDTO>(metaSector) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return status;
	}

	/**
	 * Delete a MetaSector from the cluster.
	 * 
	 * Request URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/metasectors/{metaSectorId}
	 * 
	 * @param metaSectorId
	 * 
	 * @return Delete status
	 * @throws ClientException
	 */
	public StatusDTO deleteMetaSector(String metaSectorId) throws ClientException {
		StatusDTO status = null;
		try {
			Builder builder = getRootPath().path("metasectors").path(metaSectorId).request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		}
		return status;
	}

}
