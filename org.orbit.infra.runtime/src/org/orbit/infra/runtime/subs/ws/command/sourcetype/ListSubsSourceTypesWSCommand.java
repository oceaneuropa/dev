package org.orbit.infra.runtime.subs.ws.command.sourcetype;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsSourceType;
import org.orbit.infra.model.subs.dto.SubsSourceTypeDTO;
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
public class ListSubsSourceTypesWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.ListSubsSourceTypesWSCommand";

	public ListSubsSourceTypesWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__LIST_SOURCE_TYPES.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		SubsServerService service = getService();

		List<SubsSourceType> types = service.getSourceTypes();

		List<SubsSourceTypeDTO> typeDTOs = new ArrayList<SubsSourceTypeDTO>();
		if (types != null) {
			for (SubsSourceType type : types) {
				SubsSourceTypeDTO typeDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(type);
				if (typeDTO != null) {
					typeDTOs.add(typeDTO);
				}
			}
		}

		return Response.ok().entity(typeDTOs).build();
	}

}
