package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class UpdateConfigRegistryCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.UpdateConfigRegistryCommand";

	public UpdateConfigRegistryCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_REGISTRY__UPDATE_CONFIG_REGISTRY.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = request.getStringParameter("id");
		if (id == null || id.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'id' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean hasSucceed = false;
		boolean hasFailed = false;

		ConfigRegistryService service = getService();

		if (request.hasParameter("type")) {
			String type = request.getStringParameter("type");
			boolean currSucceed = service.updateConfigRegistryType(id, type);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		if (request.hasParameter("name")) {
			String name = request.getStringParameter("name");
			boolean currSucceed = service.updateConfigRegistryName(id, name);
			if (currSucceed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		boolean succeed = false;
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
