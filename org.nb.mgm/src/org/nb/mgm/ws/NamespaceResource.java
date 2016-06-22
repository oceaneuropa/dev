package org.nb.mgm.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.dto.DTOConverter;
import org.nb.mgm.model.dto.HomeDTO;
import org.nb.mgm.model.dto.MachineDTO;
import org.nb.mgm.model.dto.NamespaceDTO;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.model.runtime.Namespace;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * Namespace resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/namespaces 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/namespaces (Body parameter: NamespaceDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/namespaces (Body parameter: NamespaceDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/namespaces/{namespace}
 * 
 */
@Path("/namespaces")
@Produces(MediaType.APPLICATION_JSON)
public class NamespaceResource extends AbstractApplicationResource {

	protected void handleSave(ManagementService mgm) {
		if (!mgm.isAutoSave()) {
			mgm.save();
		}
	}

	/**
	 * Get Namespaces.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/namespaces
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNamespaces() {
		List<NamespaceDTO> namespaceDTOs = new ArrayList<NamespaceDTO>();

		ManagementService mgm = getService(ManagementService.class);
//		try {
			// Get Namespaces matched by query.
//			List<Namespace> namespaces = mgm.getNamespaces();

//			for (Namespace namespace : namespaces) {
//				NamespaceDTO namespaceDTO = DTOConverter.getInstance().toDTO(namespace);
//
//				List<HomeDTO> homeDTOs = new ArrayList<HomeDTO>();
//				for (Home home : mgm.getHomes(machine.getId())) {
//					HomeDTO homeDTO = DTOConverter.getInstance().toDTO(home);
//					// homeDTO.setMachine(machineDTO);
//					homeDTOs.add(homeDTO);
//				}
//				namespaceDTO.setHomes(homeDTOs);
//
//				namespaceDTOs.add(namespaceDTO);
//			}

//		} catch (MgmException e) {
//			ErrorDTO error = handleError(e, e.getCode(), true);
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
//		}

		return Response.ok().entity(namespaceDTOs).build();
	}

}
