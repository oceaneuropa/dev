package org.orbit.platform.runtime.core.ws;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.platform.model.dto.ProcessDTO;
import org.orbit.platform.runtime.core.Platform;
import org.orbit.platform.runtime.processes.ProcessManager;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.IProcessFilter;
import org.orbit.platform.sdk.util.IProcessFilterByTypeId;
import org.orbit.platform.sdk.util.IProcessFilterByTypeIdAndId;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * Platform extension services resource.
 * 
 * {contextRoot} example:
 * /platform/v1/services
 * 
 * Extensions
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/processes?extensionTypeId={extensionTypeId}&extensionId={extensionId}
 * 
 * @see DomainServiceWSMachinesResource
 * @see TransferAgentServiceResource
 */
@Path("/processes")
@Produces(MediaType.APPLICATION_JSON)
public class PlatformWSProcessesResource extends AbstractWSApplicationResource {

	@Inject
	public Platform service;

	public Platform getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("Platform is not available.");
		}
		return this.service;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("extensionTypeId") String extensionTypeId, @QueryParam("extensionId") String extensionId) {
		List<ProcessDTO> processDTOs = new ArrayList<ProcessDTO>();

		Platform platform = getService();
		ProcessManager processManager = platform.getProcessManager();

		IProcess[] processes = null;
		if ((extensionTypeId == null || extensionTypeId.isEmpty()) && (extensionId == null || extensionId.isEmpty())) {
			processes = processManager.getProcesses();

		} else if (extensionId == null || extensionId.isEmpty()) {
			IProcessFilter filter = new IProcessFilterByTypeId(extensionTypeId);
			processes = processManager.getProcesses(filter);

		} else {
			IProcessFilter filter = new IProcessFilterByTypeIdAndId(extensionTypeId, extensionId);
			processes = processManager.getProcesses(filter);
		}

		if (processes != null) {
			for (IProcess process : processes) {
				ProcessDTO processDTO = ModelConverter.getInstance().toDTO(process);
				processDTOs.add(processDTO);
			}
		}
		return Response.ok().entity(processDTOs).build();
	}

}
