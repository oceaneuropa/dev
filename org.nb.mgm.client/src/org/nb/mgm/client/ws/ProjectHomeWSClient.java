package org.nb.mgm.client.ws;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nb.mgm.model.dto.Action;
import org.nb.mgm.model.dto.ProjectHomeDTO;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ClientUtil;
import org.origin.common.rest.model.StatusDTO;

/*
 * ProjectHome resource client.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes (Body parameter: ProjectHomeDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes (Body parameter: ProjectHomeDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/hasAttribute?attribute={attributeName}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/attribute?attribute={attributeName}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/action (Body parameter: Action)
 * 
 */
public class ProjectHomeWSClient extends AbstractWSClient {

	/**
	 * 
	 * @param config
	 */
	public ProjectHomeWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public int ping() throws ClientException {
		return 1;
	}

	/**
	 * Get ProjectHomes from a Project.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes
	 * 
	 * @param projectId
	 * @return
	 * @throws ClientException
	 */
	public List<ProjectHomeDTO> getProjectHomes(String projectId) throws ClientException {
		List<ProjectHomeDTO> projectHomeDTOs = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			projectHomeDTOs = response.readEntity(new GenericType<List<ProjectHomeDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		if (projectHomeDTOs == null) {
			projectHomeDTOs = Collections.emptyList();
		}
		return projectHomeDTOs;
	}

	/**
	 * Get ProjectHome.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public ProjectHomeDTO getProjectHome(String projectId, String projectHomeId) throws ClientException {
		ProjectHomeDTO projectHomeDTO = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			projectHomeDTO = response.readEntity(ProjectHomeDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return projectHomeDTO;
	}

	/**
	 * Add ProjectHome to a Project.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes (Body parameter: ProjectHomeDTO)
	 * 
	 * @param projectId
	 * @param newProjectHomeRequest
	 *            Body parameter for the new ProjectHome.
	 * 
	 * @return new ProjectHome
	 * @throws ClientException
	 */
	public ProjectHomeDTO addProjectHome(String projectId, ProjectHomeDTO newProjectHomeRequest) throws ClientException {
		ProjectHomeDTO newProjectHomeDTO = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<ProjectHomeDTO>(newProjectHomeRequest) {
			}));
			checkResponse(response);

			newProjectHomeDTO = response.readEntity(ProjectHomeDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return newProjectHomeDTO;
	}

	/**
	 * Update a ProjectHome.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes (Body parameter: ProjectHomeDTO)
	 * 
	 * @param projectId
	 * @param updateProjectHomeRequest
	 *            Body parameter for updating the ProjectHome.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateProjectHome(String projectId, ProjectHomeDTO updateProjectHomeRequest) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<ProjectHomeDTO>(updateProjectHomeRequest) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Delete a ProjectHome from a Project.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * 
	 * @return Delete status
	 * @throws ClientException
	 */
	public StatusDTO deleteProjectHome(String projectId, String projectHomeId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Get ProjectHome attribute
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/hasAttribute?attribute={attributeName}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param attribute
	 * @return
	 * @throws ClientException
	 */
	public boolean hasProjectHomeAttribute(String projectId, String projectHomeId, String attribute) throws ClientException {
		boolean hasAttribute = false;
		WebTarget target = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("hasAttribute").queryParam("attribute", attribute);
		try {
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			hasAttribute = response.readEntity(Boolean.class);

		} catch (ClientException e) {
			handleException(e);
		}
		return hasAttribute;
	}

	/**
	 * Get ProjectHome attribute
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/attribute?attribute={attributeName}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param attribute
	 * @param clazz
	 * @return
	 * @throws ClientException
	 */
	public <T> T getProjectHomeAttribute(String projectId, String projectHomeId, String attribute, Class<T> clazz) throws ClientException {
		T result = null;
		WebTarget target = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("attribute").queryParam("attribute", attribute);
		try {
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			result = response.readEntity(clazz);

		} catch (ClientException e) {
			handleException(e);
		}
		return result;
	}

	/**
	 * Send an Action to a ProjectHome.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/action (Body: Action)
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param action
	 * 
	 * @return new ProjectHome
	 * @throws ClientException
	 */
	public StatusDTO sendAction(String projectId, String projectHomeId, Action action) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("action").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<Action>(action) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return status;
	}

}
