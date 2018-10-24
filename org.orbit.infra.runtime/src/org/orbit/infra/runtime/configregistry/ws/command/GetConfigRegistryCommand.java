package org.orbit.infra.runtime.configregistry.ws.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.configregistry.ConfigRegistryDTO;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryMetadata;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractDataCastCommand;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class GetConfigRegistryCommand extends AbstractDataCastCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.GetConfigRegistryCommand";

	public GetConfigRegistryCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_REGISTRY__GET_CONFIG_REGISTRY.equalsIgnoreCase(requestName)) {
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

		ConfigRegistry configRegistry = null;
		if (hasId) {
			String id = request.getStringParameter("id");
			configRegistry = service.getConfigRegistryById(id);

		} else if (hasName) {
			String name = request.getStringParameter("name");
			configRegistry = service.getConfigRegistryByName(name);
		}

		if (configRegistry == null) {
			// ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config registry is not found.");
			// return Response.status(Status.BAD_REQUEST).entity(error).build();
			return Response.ok().build();
		}

		ConfigRegistryMetadata metadata = configRegistry.getMetadata();
		ConfigRegistryDTO metadataDTO = ModelConverter.CONFIG_REGISTRY.toDTO(metadata);
		return Response.ok().entity(metadataDTO).build();
	}

}
