package org.nb.mgm.client.ws;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nb.mgm.model.dto.ArtifactDTO;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;

/*
 * Artifact resource client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts?filter={filter}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts/{artifactId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts (Body parameter: ArtifactDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts (Body parameter: ArtifactDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts/{artifactId}
 * 
 */
public class ArtifactWSClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public ArtifactWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public int ping() throws ClientException {
		return 1;
	}

	/**
	 * Get Artifacts in a MetaSector.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts
	 * 
	 * @param metaSectorId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<ArtifactDTO> getArtifacts(String metaSectorId) throws ClientException {
		List<ArtifactDTO> artifacts = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("artifacts").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			artifacts = response.readEntity(new GenericType<List<ArtifactDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		if (artifacts == null) {
			artifacts = Collections.emptyList();
		}
		return artifacts;
	}

	/**
	 * Get Artifacts in a MetaSector.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts?filter={filter}
	 * 
	 * @param metaSectorId
	 * @param filter
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<ArtifactDTO> getArtifacts(String metaSectorId, String filter) throws ClientException {
		List<ArtifactDTO> artifacts = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("artifacts").queryParam("filter", filter).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			artifacts = response.readEntity(new GenericType<List<ArtifactDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		if (artifacts == null) {
			artifacts = Collections.emptyList();
		}
		return artifacts;
	}

	/**
	 * Get an Artifact in a MetaSector.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts/{artifactId}
	 * 
	 * @param metaSectorId
	 * @param artifactId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public ArtifactDTO getArtifact(String metaSectorId, String artifactId) throws ClientException {
		ArtifactDTO artifact = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("artifacts").path(artifactId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			artifact = response.readEntity(ArtifactDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return artifact;
	}

	/**
	 * Add an Artifact to a MetaSector.
	 * 
	 * Request URL (POST): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts
	 * 
	 * @param metaSectorId
	 * @param artifact
	 *            Body parameter for the new Artifact.
	 * 
	 * @return new Artifact
	 * @throws ClientException
	 */
	public ArtifactDTO createArtifact(String metaSectorId, ArtifactDTO artifact) throws ClientException {
		ArtifactDTO newArtifact = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("artifacts").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<ArtifactDTO>(artifact) {
			}));
			checkResponse(response);

			newArtifact = response.readEntity(ArtifactDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return newArtifact;
	}

	/**
	 * Update an Artifact.
	 * 
	 * Request URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts
	 * 
	 * @param metaSectorId
	 * @param updateArtifactRequest
	 *            Body parameter for updating the Artifact.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateArtifact(String metaSectorId, ArtifactDTO updateArtifactRequest) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("artifacts").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<ArtifactDTO>(updateArtifactRequest) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Delete an Artifact from a MetaSector.
	 * 
	 * Request URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{metaSectorId}/artifacts/{artifactId}
	 * 
	 * @param metaSectorId
	 * @param artifactId
	 * 
	 * @return Delete status
	 * @throws ClientException
	 */
	public StatusDTO deleteArtifact(String metaSectorId, String artifactId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(metaSectorId).path("artifacts").path(artifactId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return status;
	}

}
