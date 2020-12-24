package org.orbit.infra.runtime.subscription.ws.command.mapping;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.SubsSource;
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
public class ListSourceTypesMappingOfTargetWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.ListSourceTypesMappingOfTargetWSCommand";

	public ListSourceTypesMappingOfTargetWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__LIST_SOURCE_TYPES_MAPPING_OF_TARGET.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasTargetIdParam = request.hasParameter("targetId");
		if (!hasTargetIdParam) {
			ErrorDTO error = new ErrorDTO("'targetId' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Integer targetId = request.getIntegerParameter("targetId");

		SubsServerService service = getService();

		List<String> types = new ArrayList<String>();
		List<SubsMapping> mappings = service.getMappingsOfTarget(targetId);
		for (SubsMapping mapping : mappings) {
			Integer sourceId = mapping.getSourceId();
			SubsSource source = service.getSource(sourceId);
			if (source != null) {
				String type = source.getType();
				if (!types.contains(type)) {
					types.add(type);
				}
			}
		}

		List<SubsType> sourceTypes = new ArrayList<SubsType>();
		for (String type : types) {
			SubsType sourceType = service.getSourceType(type);
			if (sourceType != null) {
				sourceTypes.add(sourceType);
			}
		}

		List<SubsTypeDTO> typeDTOs = new ArrayList<SubsTypeDTO>();
		for (SubsType sourceType : sourceTypes) {
			SubsTypeDTO typeDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(sourceType);
			if (typeDTO != null) {
				typeDTOs.add(typeDTO);
			}
		}
		return Response.ok().entity(typeDTOs).build();
	}

}
