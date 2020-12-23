package org.orbit.infra.runtime.subs.ws.command.targettype;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsTargetType;
import org.orbit.infra.model.subs.dto.SubsTargetTypeDTO;
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
public class ListSubsTargetTypesWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.ListSubsTargetTypesWSCommand";

	public ListSubsTargetTypesWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__LIST_TARGET_TYPES.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		SubsServerService service = getService();

		List<SubsTargetType> types = service.getTargetTypes();

		List<SubsTargetTypeDTO> typeDTOs = new ArrayList<SubsTargetTypeDTO>();
		if (types != null) {
			for (SubsTargetType type : types) {
				SubsTargetTypeDTO typeDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(type);
				if (typeDTO != null) {
					typeDTOs.add(typeDTO);
				}
			}
		}

		return Response.ok().entity(typeDTOs).build();
	}

}
