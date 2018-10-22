package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.common.ws.AbstractDataCastCommand;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class SetConfigRegistryPropertiesCommand extends AbstractDataCastCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.SetConfigRegistryPropertiesCommand";

	public SetConfigRegistryPropertiesCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_REGISTRY__SET_CONFIG_REGISTRY_PROPERTIES.equalsIgnoreCase(requestName)) {
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

		Map<String, Object> newProperties = null;
		if (request.hasParameter("properties")) {
			newProperties = (Map<String, Object>) request.getMapParameter("properties");
		}
		if (newProperties == null || newProperties.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'properties' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigRegistryService service = getService();

		boolean succeed = false;
		ConfigRegistry configRegistry = service.getById(id);
		if (configRegistry != null) {
			Map<String, Object> existingProperties = configRegistry.getMetadata().getProperties();
			existingProperties.putAll(newProperties);

			succeed = service.updateProperties(id, existingProperties);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
