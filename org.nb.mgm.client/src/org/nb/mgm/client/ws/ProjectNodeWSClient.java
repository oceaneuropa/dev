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
import org.nb.mgm.model.dto.ProjectNodeDTO;
import org.nb.mgm.model.dto.SoftwareDTO;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ClientUtil;
import org.origin.common.rest.model.StatusDTO;

/*
 * ProjectNode resource client.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes (Body parameter: ProjectNodeDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes (Body parameter: ProjectNodeDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/hasAttribute?attribute={attributeName}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/attribute?attribute={attributeName}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/action (Body parameter: Action)
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/software
 * 
 */
public class ProjectNodeWSClient extends AbstractWSClient {

	/**
	 * 
	 * @param config
	 */
	public ProjectNodeWSClient(ClientConfiguration config) {
		super(config);
	}

	@Override
	public int ping() throws ClientException {
		return 1;
	}

	/**
	 * Get ProjectNodes from a ProjectHome.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @return a list of ProjectNodes
	 * @throws ClientException
	 */
	public List<ProjectNodeDTO> getProjectNodes(String projectId, String projectHomeId) throws ClientException {
		List<ProjectNodeDTO> projectNodeDTOs = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("nodes").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			projectNodeDTOs = response.readEntity(new GenericType<List<ProjectNodeDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		if (projectNodeDTOs == null) {
			projectNodeDTOs = Collections.emptyList();
		}
		return projectNodeDTOs;
	}

	/**
	 * Get ProjectNode.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * 
	 * @return ProjectNode
	 * @throws ClientException
	 */
	public ProjectNodeDTO getProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ClientException {
		ProjectNodeDTO projectNodeDTO = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("nodes").path(projectNodeId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			projectNodeDTO = response.readEntity(ProjectNodeDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return projectNodeDTO;
	}

	/**
	 * Add ProjectNode to a ProjectHome.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes (Body parameter: ProjectNodeDTO)
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param newProjectNodeRequest
	 *            Body parameter for the new ProjectNode.
	 * 
	 * @return new ProjectNode
	 * @throws ClientException
	 */
	public ProjectNodeDTO addProjectNode(String projectId, String projectHomeId, ProjectNodeDTO newProjectNodeRequest) throws ClientException {
		ProjectNodeDTO newProjectNodeDTO = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("nodes").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<ProjectNodeDTO>(newProjectNodeRequest) {
			}));
			checkResponse(response);

			newProjectNodeDTO = response.readEntity(ProjectNodeDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return newProjectNodeDTO;
	}

	/**
	 * Update a ProjectNode.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes (Body parameter: ProjectNodeDTO)
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param updateProjectNodeRequest
	 *            Body parameter for updating the ProjectNode.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateProjectNode(String projectId, String projectHomeId, ProjectNodeDTO updateProjectNodeRequest) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("nodes").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(Entity.json(new GenericEntity<ProjectNodeDTO>(updateProjectNodeRequest) {
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
	 * Delete a ProjectNode from a ProjectHome.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * 
	 * @return Delete status
	 * @throws ClientException
	 */
	public StatusDTO deleteProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("nodes").path(projectNodeId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
			response.close();

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Get ProjectNode attribute
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/hasAttribute?attribute={attributeName}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param attribute
	 * @return
	 * @throws ClientException
	 */
	public boolean hasProjectNodeAttribute(String projectId, String projectHomeId, String projectNodeId, String attribute) throws ClientException {
		boolean hasAttribute = false;
		WebTarget target = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("nodes").path(projectNodeId).path("hasAttribute").queryParam("attribute", attribute);
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
	 * Get ProjectNode attribute
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/attribute?attribute={attributeName}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param attribute
	 * @param clazz
	 * @return
	 * @throws ClientException
	 */
	public <T> T getProjectNodeAttribute(String projectId, String projectHomeId, String projectNodeId, String attribute, Class<T> clazz) throws ClientException {
		T result = null;
		WebTarget target = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("nodes").path(projectNodeId).path("attribute").queryParam("attribute", attribute);
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
	 * Send an Action to a ProjectNode.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/action (Body parameter: Action)
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param action
	 * 
	 * @return new ProjectHome
	 * @throws ClientException
	 */
	public StatusDTO sendAction(String projectId, String projectHomeId, String projectNodeId, Action action) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("nodes").path(projectNodeId).path("action").request(MediaType.APPLICATION_JSON);
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

	/**
	 * Get Software installed on a ProjectNode.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/software
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectId
	 * @return SoftwareDTO list.
	 * @throws ClientException
	 */
	public List<SoftwareDTO> getProjectNodeSoftware(String projectId, String projectHomeId, String projectNodeId) throws ClientException {
		List<SoftwareDTO> softwareDTOs = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path("projects").path(projectId).path("homes").path(projectHomeId).path("nodes").path(projectNodeId).path("software").request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			softwareDTOs = response.readEntity(new GenericType<List<SoftwareDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}
		if (softwareDTOs == null) {
			softwareDTOs = Collections.emptyList();
		}
		return softwareDTOs;
	}

}
