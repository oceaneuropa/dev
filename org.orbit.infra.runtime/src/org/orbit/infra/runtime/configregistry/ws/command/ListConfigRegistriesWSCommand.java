package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.configregistry.ConfigRegistryDTO;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryMetadata;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.RuntimeModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.Request;

public class ListConfigRegistriesWSCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.ListConfigRegistriesWSCommand";

	public ListConfigRegistriesWSCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_REGISTRY__LIST_CONFIG_REGISTRIES.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean hasType = request.hasParameter("type");

		ConfigRegistryService service = getService();

		ConfigRegistry[] configRegistries = null;
		if (hasType) {
			String type = request.getStringParameter("type");
			configRegistries = service.getConfigRegistries(type);
		} else {
			configRegistries = service.getConfigRegistries();
		}

		List<ConfigRegistryDTO> configRegistryDTOs = new ArrayList<ConfigRegistryDTO>();
		if (configRegistries != null) {
			for (ConfigRegistry configRegistry : configRegistries) {
				ConfigRegistryMetadata metadata = configRegistry.getMetadata();

				ConfigRegistryDTO configRegistryDTO = RuntimeModelConverter.CONFIG_REGISTRY.toDTO(metadata);
				if (configRegistryDTO != null) {
					configRegistryDTOs.add(configRegistryDTO);
				}
			}
		}

		return Response.ok().entity(configRegistryDTOs).build();
	}

}
