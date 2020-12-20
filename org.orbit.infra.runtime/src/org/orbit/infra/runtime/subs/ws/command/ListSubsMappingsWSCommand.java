package org.orbit.infra.runtime.subs.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.dto.SubsMappingDTO;
import org.orbit.infra.runtime.subs.SubsServerService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ListSubsMappingsWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.ListSubsMappingsWSCommand";

	public ListSubsMappingsWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__LIST_MAPPINGS.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		Integer sourceId = request.getIntegerParameter("sourceId");
		Integer targetId = request.getIntegerParameter("targetId");

		SubsServerService service = getService();

		List<SubsMapping> mappings = null;
		if (sourceId != null && targetId != null) {
			mappings = service.getMappings(sourceId, targetId);

		} else if (sourceId != null) {
			mappings = service.getMappingsOfSource(sourceId);

		} else if (targetId != null) {
			mappings = service.getMappingsOfTarget(targetId);

		} else {
			mappings = service.getMappings();
		}

		List<SubsMappingDTO> mappingDTOs = new ArrayList<SubsMappingDTO>();
		if (mappings != null) {
			for (SubsMapping mapping : mappings) {
				SubsMappingDTO mappingDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(mapping);
				if (mappingDTO != null) {
					mappingDTOs.add(mappingDTO);
				}
			}
		}

		return Response.ok().entity(mappingDTOs).build();
	}

}
