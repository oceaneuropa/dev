package org.nb.mgm.client.ws;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nb.mgm.model.dto.ProjectHomeDTO;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
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
 */
public class ProjectHomeClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public ProjectHomeClient(ClientConfiguration config) {
		super(config);
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
			IOUtil.closeQuietly(response, true);
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
			IOUtil.closeQuietly(response, true);
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
			IOUtil.closeQuietly(response, true);
		}
		return newProjectHomeDTO;
	}

	/**
	 * Update ProjectHome.
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
			IOUtil.closeQuietly(response, true);
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
			response.close();

		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return status;
	}

}
