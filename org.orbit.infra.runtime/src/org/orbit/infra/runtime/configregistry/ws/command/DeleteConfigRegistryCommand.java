package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.common.ws.AbstractDataCastCommand;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class DeleteConfigRegistryCommand extends AbstractDataCastCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.CreateConfigRegistryCommand";

	public DeleteConfigRegistryCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_REGISTRY__DELETE_CONFIG_REGISTRY.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasId = request.hasParameter("id");
		boolean hasName = request.hasParameter("name");

		if (!hasId && !hasName) {
			ErrorDTO error = new ErrorDTO("'id' parameter or 'name' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigRegistryService service = getService();

		boolean succeed = false;
		if (hasId) {
			String id = request.getStringParameter("id");
			succeed = service.deleteById(id);

		} else if (hasName) {
			String name = request.getStringParameter("name");
			succeed = service.deleteByName(name);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
