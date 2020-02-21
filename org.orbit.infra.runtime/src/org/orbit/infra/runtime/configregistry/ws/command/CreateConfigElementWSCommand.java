package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.configregistry.ConfigElementDTO;
import org.orbit.infra.runtime.configregistry.service.ConfigElement;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.resource.Path;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class CreateConfigElementWSCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.CreateConfigElementWSCommand";

	public CreateConfigElementWSCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_ELEMENT__CREATE_CONFIG_ELEMENT.equalsIgnoreCase(requestName)) {
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

		boolean hasParentElementId = request.hasParameter("parent_element_id");
		boolean hasElementPath = request.hasParameter("element_path");
		boolean hasElementName = request.hasParameter("element_name");

		Map<String, Object> attributes = null;
		if (request.hasParameter("attributes")) {
			attributes = (Map<String, Object>) request.getMapParameter("attributes");
		}

		boolean generateUniqueName = false;
		if (request.hasParameter("generate_unique_name")) {
			generateUniqueName = true;
		}

		if (!hasElementPath && !hasElementName) {
			ErrorDTO error = new ErrorDTO("'element_path' parameter or ''element_name' (and 'parent_element_id') parameters are not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigRegistryService service = getService();

		ConfigRegistry configRegistry = service.getConfigRegistryById(configRegistryId);
		if (configRegistry == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config registry is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigElement element = null;

		if (hasElementPath) {
			String elementPathString = request.getStringParameter("element_path");
			Path elementPath = new Path(elementPathString);

			if (hasParentElementId) {
				String parentElementId = request.getStringParameter("parent_element_id");
				element = configRegistry.createElement(parentElementId, elementPath, attributes, generateUniqueName);
			} else {
				element = configRegistry.createElement(elementPath, attributes, generateUniqueName);
			}

		} else if (hasElementName) {
			String parentElementId = null;
			if (hasParentElementId) {
				parentElementId = request.getStringParameter("parent_element_id");
			} else {
				parentElementId = "-1";
			}
			String name = request.getStringParameter("element_name");

			if (!generateUniqueName) {
				if (configRegistry.elementExists(parentElementId, name)) {
					ErrorDTO error = new ErrorDTO("Config registry with name '" + name + "' already exists.");
					return Response.status(Status.BAD_REQUEST).entity(error).build();
				}
			}

			element = configRegistry.createElement(parentElementId, name, attributes, generateUniqueName);
		}

		if (element == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Element cannot be created.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigElementDTO elementDTO = RuntimeModelConverter.CONFIG_REGISTRY.toDTO(element);
		return Response.ok().entity(elementDTO).build();
	}

}
