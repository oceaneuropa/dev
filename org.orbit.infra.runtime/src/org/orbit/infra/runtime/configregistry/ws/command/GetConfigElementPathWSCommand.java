package org.orbit.infra.runtime.configregistry.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.runtime.configregistry.service.ConfigElement;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.resource.Path;
import org.origin.common.resource.PathDTO;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class GetConfigElementPathWSCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.GetConfigElementPathWSCommand";

	public GetConfigElementPathWSCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_ELEMENT__GET_CONFIG_ELEMENT_PATH.equalsIgnoreCase(requestName)) {
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

		if (!hasElementId) {
			ErrorDTO error = new ErrorDTO("'element_id' parameter is not set.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigRegistryService service = getService();

		ConfigRegistry configRegistry = service.getConfigRegistryById(configRegistryId);
		if (configRegistry == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config registry is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		PathDTO pathDTO = null;

		if (hasElementId) {
			String elementId = request.getStringParameter("element_id");
			ConfigElement configElement = configRegistry.getElement(elementId);

			if (configElement != null) {
				Path path = configElement.getPath();
				if (path != null) {
					pathDTO = RuntimeModelConverter.COMMON.toDTO(path);
				}
			}
		}

		if (pathDTO == null) {
			return Response.ok().build();
		}
		return Response.ok().entity(pathDTO).build();
	}

}
