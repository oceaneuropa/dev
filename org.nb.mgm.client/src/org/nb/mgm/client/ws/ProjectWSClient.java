package org.nb.mgm.client.ws;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nb.mgm.model.dto.ProjectDTO;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;

/*
 * Project resource client
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects (Body parameter: ProjectDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects (Body parameter: ProjectDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}
 * 
 */
public class ProjectWSClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public ProjectWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public int ping() throws ClientException {
		return 1;
	}

	/**
	 * Get Projects.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<ProjectDTO> getProjects() throws ClientException {
		List<ProjectDTO> projects = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			projects = response.readEntity(new GenericType<List<ProjectDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		if (projects == null) {
			projects = Collections.emptyList();
		}
		return projects;
	}

	/**
	 * Get Project by Id.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}
	 * 
	 * @param projectId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public ProjectDTO getProject(String projectId) throws ClientException {
		ProjectDTO project = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			project = response.readEntity(ProjectDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return project;
	}

	/**
	 * Add a Project.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects (Body parameter: ProjectDTO)
	 * 
	 * @param newProjectRequest
	 *            Body parameter for the new Project.
	 * 
	 * @return new Project
	 * @throws ClientException
	 */
	public ProjectDTO addProject(ProjectDTO newProjectRequest) throws ClientException {
		ProjectDTO newProject = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<ProjectDTO>(newProjectRequest) {
			}));
			checkResponse(response);

			newProject = response.readEntity(ProjectDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(response, true);
		}
		return newProject;
	}

	/**
	 * Update a Project.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects (Body parameter: ProjectDTO)
	 * 
	 * @param updateProjectRequest
	 *            Body parameter for updating the Project.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateProject(ProjectDTO updateProjectRequest) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<ProjectDTO>(updateProjectRequest) {
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
	 * Delete a Project.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}
	 * 
	 * @param projectId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO deleteProject(String projectId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).request(MediaType.APPLICATION_JSON);
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
