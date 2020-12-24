package org.orbit.infra.runtime.subscription.ws.command.mapping;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.SubsTarget;
import org.orbit.infra.model.subs.SubsType;
import org.orbit.infra.model.subs.dto.SubsTypeDTO;
import org.orbit.infra.runtime.subscription.SubsServerService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ListTargetTypesMappingOfSourceWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.ListTargetTypesMappingOfSourceWSCommand";

	public ListTargetTypesMappingOfSourceWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__LIST_TARGET_TYPES_MAPPING_OF_SOURCE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasSourceIdParam = request.hasParameter("sourceId");
		if (!hasSourceIdParam) {
			ErrorDTO error = new ErrorDTO("'sourceId' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Integer sourceId = request.getIntegerParameter("sourceId");

		SubsServerService service = getService();

		List<String> types = new ArrayList<String>();
		List<SubsMapping> mappings = service.getMappingsOfSource(sourceId);
		for (SubsMapping mapping : mappings) {
			Integer targetId = mapping.getTargetId();
			SubsTarget target = service.getTarget(targetId);
			if (target != null) {
				String type = target.getType();
				if (!types.contains(type)) {
					types.add(type);
				}
			}
		}

		List<SubsType> targetTypes = new ArrayList<SubsType>();
		for (String type : types) {
			SubsType taregtType = service.getTargetType(type);
			if (taregtType != null) {
				targetTypes.add(taregtType);
			}
		}

		List<SubsTypeDTO> typeDTOs = new ArrayList<SubsTypeDTO>();
		for (SubsType targetType : targetTypes) {
			SubsTypeDTO typeDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(targetType);
			if (typeDTO != null) {
				typeDTOs.add(typeDTO);
			}
		}
		return Response.ok().entity(typeDTOs).build();
	}

}
