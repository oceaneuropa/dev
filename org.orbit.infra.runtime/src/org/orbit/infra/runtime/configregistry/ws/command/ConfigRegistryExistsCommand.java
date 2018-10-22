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

public class ConfigRegistryExistsCommand extends AbstractDataCastCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.ConfigRegistryExistsCommand";

	public ConfigRegistryExistsCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_REGISTRY__CONFIG_REGISTRY_EXISTS.equalsIgnoreCase(requestName)) {
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

		boolean exists = false;
		if (hasId) {
			String id = request.getStringParameter("id");
			exists = service.configRegistryExistsById(id);

		} else if (hasName) {
			String name = request.getStringParameter("name");
			exists = service.configRegistryExistsByName(name);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("exists", exists);
		return Response.status(Status.OK).entity(result).build();
	}

}
