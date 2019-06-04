package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class SetConfigRegistryPropertyWSCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.SetConfigRegistryPropertyWSCommand";

	public SetConfigRegistryPropertyWSCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_REGISTRY__SET_CONFIG_REGISTRY_PROPERTY.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String id = request.getStringParameter("id");
		String oldName = request.getStringParameter("property_old_name");
		String name = request.getStringParameter("property_name");
		Object value = request.getParameter("property_value");

		if (id == null || id.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'id' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (name == null || name.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'property_name' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (value == null) {
			ErrorDTO error = new ErrorDTO("'property_value' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigRegistryService service = getService();

		ConfigRegistry configRegistry = service.getConfigRegistryById(id);
		if (configRegistry == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config registry is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Map<String, Object> existingProperties = configRegistry.getMetadata().getProperties();
		if (oldName != null && !oldName.equals(name)) {
			// property name is changed
			existingProperties.remove(oldName);
			existingProperties.put(name, value);
		} else {
			// property name is not changed
			existingProperties.put(name, value);
		}

		boolean succeed = service.updateConfigRegistryProperties(id, existingProperties);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
