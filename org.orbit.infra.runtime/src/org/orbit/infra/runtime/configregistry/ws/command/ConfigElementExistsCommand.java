package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractDataCastCommand;
import org.origin.common.resource.Path;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class ConfigElementExistsCommand extends AbstractDataCastCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.ConfigElementExistsCommand";

	public ConfigElementExistsCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_ELEMENT__CONFIG_ELEMENT_EXISTS.equalsIgnoreCase(requestName)) {
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
		boolean hasElementName = request.hasParameter("element_name");

		if (!hasElementId && !hasElementPath && !hasElementName) {
			ErrorDTO error = new ErrorDTO("'element_id' parameter or 'element_path' parameter or 'element_name' (and 'parent_element_id') parameters are not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigRegistryService service = getService();

		ConfigRegistry configRegistry = service.getConfigRegistryById(configRegistryId);
		if (configRegistry == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config registry is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		boolean exists = false;
		if (hasElementId) {
			String elementId = request.getStringParameter("element_id");
			exists = configRegistry.exists(elementId);

		} else if (hasElementPath) {
			String elementPathString = request.getStringParameter("element_path");
			Path elementPath = new Path(elementPathString);
			exists = configRegistry.exists(elementPath);

		} else if (hasElementName) {
			String parentElementId = null;
			boolean hasParentElementId = request.hasParameter("parent_element_id");
			if (hasParentElementId) {
				parentElementId = request.getStringParameter("parent_element_id");
			} else {
				parentElementId = "-1";
			}
			String name = request.getStringParameter("element_name");
			exists = configRegistry.exists(parentElementId, name);
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("exists", exists);
		return Response.status(Status.OK).entity(result).build();
	}

}
