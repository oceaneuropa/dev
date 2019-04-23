package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.HashMap;
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

public class SetConfigElementAttributeWSCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.SetConfigElementAttributeWSCommand";

	public SetConfigElementAttributeWSCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_ELEMENT__SET_CONFIG_ELEMENT_ATTRIBUTE.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String configRegistryId = request.getStringParameter("config_registry_id");
		String elementId = request.getStringParameter("element_id");
		String oldAttributeName = request.getStringParameter("old_attribute_name");
		String attributeName = request.getStringParameter("attribute_name");
		Object attributeValue = request.getParameter("attribute_value");

		if (configRegistryId == null || configRegistryId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'config_registry_id' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (elementId == null || elementId.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'element_id' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (attributeName == null || attributeName.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'attribute_name' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
		if (attributeValue == null) {
			ErrorDTO error = new ErrorDTO("'attribute_value' parameter is not set.");
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
		if (oldAttributeName != null && !oldAttributeName.equals(attributeName)) {
			// attribute name is changed
			existingAttributes.remove(oldAttributeName);
			existingAttributes.put(attributeName, attributeValue);
		} else {
			// attribute name is not changed
			existingAttributes.put(attributeName, attributeValue);
		}
		boolean succeed = configRegistry.updateAttributes(elementId, existingAttributes);

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("succeed", succeed);
		return Response.status(Status.OK).entity(result).build();
	}

}
