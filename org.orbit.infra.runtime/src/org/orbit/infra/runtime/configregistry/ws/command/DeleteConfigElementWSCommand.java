package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.resource.Path;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class DeleteConfigElementWSCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.DeleteConfigElementWSCommand";

	public DeleteConfigElementWSCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_ELEMENT__DELETE_CONFIG_ELEMENT.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String configRegistryId = request.getStringParameter("config_registry_id");
		if (configRegistryId == null || configRegistryId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'config_registry_id' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean hasElementId = request.hasParameter("element_id");
		boolean hasElementPath = request.hasParameter("element_path");

		if (!hasElementId && !hasElementPath) {
			ErrorDTO error = new ErrorDTO("'element_id' parameter or 'element_path' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigRegistryService service = getService();
		ConfigRegistry configRegistry = service.getConfigRegistryById(configRegistryId);
		if (configRegistry == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config registry is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean succeed = false;
		if (hasElementId) {
			String elementId = request.getStringParameter("element_id");
			succeed = configRegistry.delete(elementId);

		} else if (hasElementPath) {
			String elementPathString = request.getStringParameter("element_path");
			Path elementPath = new Path(elementPathString);
			succeed = configRegistry.delete(elementPath);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
