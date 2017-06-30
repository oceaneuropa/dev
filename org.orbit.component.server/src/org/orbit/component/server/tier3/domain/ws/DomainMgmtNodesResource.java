package org.orbit.component.server.tier3.domain.ws;

import java.util.ArrayList;
import java.util.List;

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

import org.orbit.component.model.tier3.domain.DomainMgmtException;
import org.orbit.component.model.tier3.domain.ModelConverter;
import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * DomainManagement TransferAgents resource.
 * 
 * {contextRoot} example:
 * /orbit/v1/domain
 * 
 * Nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes/{nodeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes (Body parameter: NodeConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes (Body parameter: NodeConfigDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes/{nodeId}
 * 
 */
@Path("/machines/{machineId}/transferagents/{transferAgentId}/nodes")
@Produces(MediaType.APPLICATION_JSON)
public class DomainMgmtNodesResource extends AbstractApplicationResource {

	/**
	 * Get node configurations.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodes(@PathParam("machineId") String machineId, @PathParam("transferAgentId") String transferAgentId) {
		DomainManagementService domainMgmtService = getService(DomainManagementService.class);

		List<NodeConfigDTO> nodeConfigDTOs = new ArrayList<NodeConfigDTO>();
		try {
			List<NodeConfigRTO> nodeConfigs = domainMgmtService.getNodeConfigs(machineId, transferAgentId);
			if (nodeConfigs != null) {
				for (NodeConfigRTO nodeConfig : nodeConfigs) {
					NodeConfigDTO nodeConfigDTO = ModelConverter.getInstance().toDTO(nodeConfig);
					nodeConfigDTOs.add(nodeConfigDTO);
				}
			}
		} catch (DomainMgmtException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(nodeConfigDTOs).build();
	}

	/**
	 * Get a node configuration.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes/{nodeId}
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @return
	 */
	@GET
	@Path("{nodeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNode(@PathParam("machineId") String machineId, @PathParam("transferAgentId") String transferAgentId, @PathParam("nodeId") String nodeId) {
		NodeConfigDTO nodeConfigDTO = null;

		DomainManagementService domainMgmtService = getService(DomainManagementService.class);
		try {
			NodeConfigRTO nodeConfig = domainMgmtService.getNodeConfig(machineId, transferAgentId, nodeId);
			if (nodeConfig == null) {
				ErrorDTO notFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("Node with id '%s' cannot be found.", transferAgentId));
				return Response.status(Status.NOT_FOUND).entity(notFoundError).build();
			}
			nodeConfigDTO = ModelConverter.getInstance().toDTO(nodeConfig);

		} catch (DomainMgmtException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(nodeConfigDTO).build();
	}

	/**
	 * Add a node configuration.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes (Body parameter: NodeConfigDTO)
	 *
	 * @param machineId
	 * @param transferAgentId
	 * @param addTransferAgentRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNode(@PathParam("machineId") String machineId, @PathParam("transferAgentId") String transferAgentId, NodeConfigDTO addNodeRequestDTO) {
		if (addNodeRequestDTO == null) {
			ErrorDTO nullRequestDTOError = new ErrorDTO("addNodeRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullRequestDTOError).build();
		}

		boolean succeed = false;
		DomainManagementService domainMgmtService = getService(DomainManagementService.class);
		try {
			String nodeId = addNodeRequestDTO.getId();
			if (nodeId == null || nodeId.isEmpty()) {
				ErrorDTO emptyIdError = new ErrorDTO("nodeId Id is empty.");
				return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
			}
			if (domainMgmtService.nodeConfigExists(machineId, transferAgentId, nodeId)) {
				ErrorDTO alreadyExistsError = new ErrorDTO(String.format("Node with Id '%s' already exists.", nodeId));
				return Response.status(Status.BAD_REQUEST).entity(alreadyExistsError).build();
			}

			NodeConfigRTO addNodeRequest = ModelConverter.getInstance().toRTO(addNodeRequestDTO);
			succeed = domainMgmtService.addNodeConfig(machineId, transferAgentId, addNodeRequest);

		} catch (DomainMgmtException e) {
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
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes (Body parameter: NodeConfigDTO)
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param updateNodeRequestDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateNode(@PathParam("machineId") String machineId, @PathParam("transferAgentId") String transferAgentId, NodeConfigDTO updateNodeRequestDTO) {
		if (updateNodeRequestDTO == null) {
			ErrorDTO nullRequestDTOError = new ErrorDTO("updateNodeRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullRequestDTOError).build();
		}

		boolean succeed = false;
		DomainManagementService domainMgmtService = getService(DomainManagementService.class);
		try {
			NodeConfigRTO updateNodeRequest = ModelConverter.getInstance().toRTO(updateNodeRequestDTO);
			succeed = domainMgmtService.updateNodeConfig(machineId, transferAgentId, updateNodeRequest);

		} catch (DomainMgmtException e) {
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
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/machines/{machineId}/transferagents/{transferAgentId}/nodes/{nodeId}
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param nodeId
	 * @return
	 */
	@DELETE
	@Path("/{nodeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response removeNode(@PathParam("machineId") String machineId, @PathParam("transferAgentId") String transferAgentId, @PathParam("nodeId") String nodeId) {
		if (nodeId == null || nodeId.isEmpty()) {
			ErrorDTO emptyIdError = new ErrorDTO("nodeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(emptyIdError).build();
		}

		boolean succeed = false;
		DomainManagementService domainMgmtService = getService(DomainManagementService.class);
		try {
			succeed = domainMgmtService.deleteNodeConfig(machineId, transferAgentId, nodeId);

		} catch (DomainMgmtException e) {
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
