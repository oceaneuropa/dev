package org.orbit.infra.runtime.subscription.ws.command.target;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.subs.SubsTarget;
import org.orbit.infra.model.subs.dto.SubsTargetDTO;
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
public class ListSubsTargetsWSCommand extends AbstractInfraCommand<SubsServerService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.subsServer.ListSubsTargetsWSCommand";

	public ListSubsTargetsWSCommand() {
		super(SubsServerService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.SUBS_SERVER__LIST_TARGETS.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasTypeParam = request.hasParameter("type");

		SubsServerService service = getService();

		List<SubsTarget> targets = null;
		if (hasTypeParam) {
			String type = request.getStringParameter("type");
			targets = service.getTargets(type);
		} else {
			targets = service.getTargets();
		}

		List<SubsTargetDTO> targetDTOs = new ArrayList<SubsTargetDTO>();
		if (targets != null) {
			for (SubsTarget target : targets) {
				SubsTargetDTO targetDTO = RuntimeModelConverter.SUBS_SERVER.toDTO(target);
				if (targetDTO != null) {
					targetDTOs.add(targetDTO);
				}
			}
		}

		return Response.ok().entity(targetDTOs).build();
	}

}
