package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.configregistry.ConfigElementDTO;
import org.orbit.infra.runtime.configregistry.service.ConfigElement;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.resource.Path;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class ListConfigElementsCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.ListConfigElementsCommand";

	public ListConfigElementsCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_ELEMENT__LIST_CONFIG_ELEMENTS.equalsIgnoreCase(requestName)) {
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

		ConfigRegistryService service = getService();

		ConfigRegistry configRegistry = service.getConfigRegistryById(configRegistryId);
		if (configRegistry == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config registry is not found.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		List<ConfigElementDTO> configElementDTOs = new ArrayList<ConfigElementDTO>();
		ConfigElement[] configElements = null;

		if (request.hasParameter("parent_element_id")) {
			String parentElementId = request.getStringParameter("parent_element_id");
			configElements = configRegistry.listElements(parentElementId);

		} else if (request.hasParameter("parent_element_path")) {
			String parentElementPath = request.getStringParameter("parent_element_path");
			Path parentPath = new Path(parentElementPath);
			configElements = configRegistry.listElements(parentPath);

		} else {
			configElements = configRegistry.listRoots();
		}

		if (configElements != null) {
			for (ConfigElement configElement : configElements) {
				ConfigElementDTO configElementDTO = ModelConverter.CONFIG_REGISTRY.toDTO(configElement);
				if (configElementDTO != null) {
					configElementDTOs.add(configElementDTO);
				}
			}
		}

		return Response.ok().entity(configElementDTOs).build();
	}

}
