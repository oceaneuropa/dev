package org.orbit.infra.runtime.subscription.ws.command.mapping;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.dto.SubsMappingDTO;
import org.orbit.infra.runtime.subscription.SubsServerService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ListMappingsWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.ListMappingsWSCommand";

	public ListMappingsWSCommand() {
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
		boolean hasSourceIdParam = request.hasParameter("sourceId");
		boolean hasTargetIdParam = request.hasParameter("targetId");
		boolean hasSourceTypeParam = request.hasParameter("sourceType");
		boolean hasSourceInstanceIdParam = request.hasParameter("sourceInstanceId");
		boolean hasTargetTypeParam = request.hasParameter("targetType");
		boolean hasTargetInstanceIdParam = request.hasParameter("targetInstanceId");

		Integer sourceId = request.getIntegerParameter("sourceId");
		Integer targetId = request.getIntegerParameter("targetId");
		String targetType = request.getStringParameter("targetType");
		String targetInstanceId = request.getStringParameter("targetInstanceId");
		String sourceType = request.getStringParameter("sourceType");
		String sourceInstanceId = request.getStringParameter("sourceInstanceId");

		SubsServerService service = getService();

		List<SubsMapping> mappings = null;
		if (hasSourceIdParam && hasTargetIdParam) {
			mappings = service.getMappings(sourceId, targetId);

		} else if (hasSourceIdParam) {
			if (hasTargetTypeParam && hasTargetInstanceIdParam) {
				mappings = service.getMappingsOfSource(sourceId, targetType, targetInstanceId);
			} else if (hasTargetTypeParam) {
				mappings = service.getMappingsOfSource(sourceId, targetType);
			} else {
				mappings = service.getMappingsOfSource(sourceId);
			}

		} else if (hasTargetIdParam) {
			if (hasSourceTypeParam && hasSourceInstanceIdParam) {
				mappings = service.getMappingsOfTarget(targetId, sourceType, sourceInstanceId);
			} else if (hasSourceTypeParam) {
				mappings = service.getMappingsOfTarget(targetId, sourceType);
			} else {
				mappings = service.getMappingsOfTarget(targetId);
			}

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
