package org.orbit.infra.runtime.configregistry.ws.command;

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

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class GetConfigElementWSCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.GetConfigElementWSCommand";

	public GetConfigElementWSCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_ELEMENT__GET_CONFIG_ELEMENT.equalsIgnoreCase(requestName)) {
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
		boolean hasParentElementId = request.hasParameter("parent_element_id");
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

		ConfigElementDTO configElementDTO = null;
		ConfigElement configElement = null;

		if (hasElementId) {
			String elementId = request.getStringParameter("element_id");
			configElement = configRegistry.getElement(elementId);

		} else if (hasElementPath) {
			String pathString = request.getStringParameter("element_path");
			if (hasParentElementId) {
				String parentElementId = request.getStringParameter("parent_element_id");
				configElement = configRegistry.getElement(parentElementId, new Path(pathString));
			} else {
				configElement = configRegistry.getElement(new Path(pathString));
			}

		} else if (hasElementName) {
			String name = request.getStringParameter("element_name");
			String parentElementId = null;
			if (hasParentElementId) {
				parentElementId = request.getStringParameter("parent_element_id");
			} else {
				parentElementId = "-1";
			}
			configElement = configRegistry.getElement(parentElementId, name);
		}

		if (configElement != null) {
			configElementDTO = RuntimeModelConverter.CONFIG_REGISTRY.toDTO(configElement);
		}

		if (configElementDTO == null) {
			return Response.ok().build();
		}
		return Response.ok().entity(configElementDTO).build();
	}

}

// ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config element is not found.");
// return Response.status(Status.BAD_REQUEST).entity(error).build();
