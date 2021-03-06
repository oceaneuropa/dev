package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.configregistry.service.ConfigElement;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class RemoveConfigElementAttributesWSCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.RemoveConfigElementAttributesWSCommand";

	public RemoveConfigElementAttributesWSCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_ELEMENT__REMOVE_CONFIG_ELEMENT_ATTRIBUTES.equalsIgnoreCase(requestName)) {
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

		String elementId = request.getStringParameter("element_id");
		if (elementId == null || elementId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'element_id' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		List<String> attributeNames = null;
		if (request.hasParameter("attribute_names")) {
			attributeNames = (List<String>) request.getListParameter("attribute_names");
		}
		if (attributeNames == null || attributeNames.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'attribute_names' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigRegistryService service = getService();
		ConfigRegistry configRegistry = service.getConfigRegistryById(configRegistryId);
		if (configRegistry == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config registry is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigElement configElement = configRegistry.getElement(elementId);
		if (configElement == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config element is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Map<String, Object> existingAttributes = configElement.getAttributes();
		for (String attributeName : attributeNames) {
			existingAttributes.remove(attributeName);
		}

		boolean succeed = configRegistry.updateAttributes(elementId, existingAttributes);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
