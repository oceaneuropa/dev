package org.orbit.component.runtime.tier3.domain.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.runtime.model.domain.NodeConfig;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;
import org.orbit.component.runtime.util.RuntimeModelConverter;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * DomainService Nodes resource.
 * 
 * {contextRoot} example:
 * /orbit/v1/domain
 * 
 * Nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes/{nodeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes (Body parameter: NodeConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes (Body parameter: NodeConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes/{nodeId}
 * 
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN, OrbitRoles.DOMAIN_MANAGEMENT_ADMIN })
@Path("/machines/{machineId}/platforms/{platformId}/nodes")
@Produces(MediaType.APPLICATION_JSON)
public class DomainServiceWSNodesResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(DomainServiceWSNodesResource.class);

	@Inject
	public DomainManagementService service;

	public DomainManagementService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DomainService is not available.");
		}
		return this.service;
	}

	/**
	 * Get node configurations.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes
	 * 
	 * @param machineId
	 * @param platformId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodes(@PathParam("machineId") String machineId, @PathParam("platformId") String platformId) {
		DomainManagementService service = getService();

		List<NodeConfigDTO> nodeConfigDTOs = new ArrayList<NodeConfigDTO>();
		try {
			List<NodeConfig> nodeConfigs = service.getNodeConfigs(machineId, platformId);
			if (nodeConfigs != null) {
				for (NodeConfig nodeConfig : nodeConfigs) {
					NodeConfigDTO nodeConfigDTO = RuntimeModelConverter.Domain.toNodeConfigDTO(nodeConfig);
					nodeConfigDTOs.add(nodeConfigDTO);
				}
			}

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(nodeConfigDTOs).build();
	}

	/**
	 * Get a node configuration.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes/{nodeId}
	 * 
	 * @param machineId
	 * @param platformId
	 * @return
	 */
	@GET
	@Path("{nodeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNode(@PathParam("machineId") String machineId, @PathParam("platformId") String platformId, @PathParam("nodeId") String nodeId) {
		NodeConfigDTO nodeConfigDTO = null;

		DomainManagementService service = getService();
		try {
			NodeConfig nodeConfig = service.getNodeConfig(machineId, platformId, nodeId);
			if (nodeConfig == null) {
				ErrorDTO notFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("Node with id '%s' cannot be found.", platformId));
				return Response.status(Status.NOT_FOUND).entity(notFoundError).build();
			}

			nodeConfigDTO = RuntimeModelConverter.Domain.toNodeConfigDTO(nodeConfig);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(nodeConfigDTO).build();
	}

	/**
	 * Add a node configuration.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes (Body parameter: NodeConfigDTO)
	 *
	 * @param machineId
	 * @param platformId
	 * @param addNodeRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNode(@PathParam("machineId") String machineId, @PathParam("platformId") String platformId, NodeConfigDTO addNodeRequestDTO) {
		if (addNodeRequestDTO == null) {
			ErrorDTO nullRequestDTOError = new ErrorDTO("addNodeRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullRequestDTOError).build();
		}

		boolean succeed = false;
		DomainManagementService service = getService();
		try {
			String nodeId = addNodeRequestDTO.getId();
			if (nodeId == null || nodeId.isEmpty()) {
				ErrorDTO emptyIdError = new ErrorDTO("nodeId Id is empty.");
				return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
			}
			if (service.nodeConfigExists(machineId, platformId, nodeId)) {
				ErrorDTO alreadyExistsError = new ErrorDTO(String.format("Node with Id '%s' already exists.", nodeId));
				return Response.status(Status.BAD_REQUEST).entity(alreadyExistsError).build();
			}

			NodeConfig addNodeRequest = RuntimeModelConverter.Domain.toNodeConfig(addNodeRequestDTO);

			succeed = service.addNodeConfig(machineId, platformId, addNodeRequest);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Node is added.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Node is not added.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Update a node configuration.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes (Body parameter: NodeConfigDTO)
	 * 
	 * @param machineId
	 * @param platformId
	 * @param updateNodeRequestDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNode(@PathParam("machineId") String machineId, @PathParam("platformId") String platformId, NodeConfigDTO updateNodeRequestDTO) {
		if (updateNodeRequestDTO == null) {
			ErrorDTO nullRequestDTOError = new ErrorDTO("updateNodeRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullRequestDTOError).build();
		}

		boolean succeed = false;
		DomainManagementService service = getService();
		try {
			NodeConfig updateNodeRequest = RuntimeModelConverter.Domain.toNodeConfig(updateNodeRequestDTO);
			List<String> fieldsToUpdate = updateNodeRequestDTO.getFieldsToUpdate();

			succeed = service.updateNodeConfig(machineId, platformId, updateNodeRequest, fieldsToUpdate);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Node is updated successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Node is not updated.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Remove a node configuration.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/platforms/{platformId}/nodes/{nodeId}
	 * 
	 * @param machineId
	 * @param platformId
	 * @param nodeId
	 * @return
	 */
	@DELETE
	@Path("/{nodeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeNode(@PathParam("machineId") String machineId, @PathParam("platformId") String platformId, @PathParam("nodeId") String nodeId) {
		if (nodeId == null || nodeId.isEmpty()) {
			ErrorDTO emptyIdError = new ErrorDTO("nodeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
		}

		boolean succeed = false;
		DomainManagementService service = getService();
		try {
			succeed = service.deleteNodeConfig(machineId, platformId, nodeId);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Node is removed successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Node is not removed.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

}
