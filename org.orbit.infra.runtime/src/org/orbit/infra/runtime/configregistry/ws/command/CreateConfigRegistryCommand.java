package org.orbit.infra.runtime.configregistry.ws.command;

import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.model.configregistry.ConfigRegistryDTO;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryMetadata;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.util.AbstractInfraCommand;
import org.orbit.infra.runtime.util.ModelConverter;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class CreateConfigRegistryCommand extends AbstractInfraCommand<ConfigRegistryService> implements WSCommand {

	public static String ID = "org.orbit.infra.runtime.configregistry.CreateConfigRegistryCommand";

	public CreateConfigRegistryCommand() {
		super(ConfigRegistryService.class);
	}

	@Override
	public boolean isSupported(Request request) {
		String requestName = request.getRequestName();
		if (RequestConstants.CONFIG_REGISTRY__CREATE_CONFIG_REGISTRY.equalsIgnoreCase(requestName)) {
			return true;
		}
		return false;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String type = request.getStringParameter("type");
		String name = request.getStringParameter("name");

		Map<String, Object> properties = null;
		if (request.hasParameter("properties")) {
			properties = (Map<String, Object>) request.getMapParameter("properties");
		}
		boolean generateUniqueName = false;
		if (request.hasParameter("generate_unique_name")) {
			generateUniqueName = true;
		}

		if (name == null || name.isEmpty()) {
			ErrorDTO error = new ErrorDTO("'name' parameter is empty.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigRegistryService service = getService();

		if (generateUniqueName) {
			String defaultName = name;
			boolean isLastSegmentNumber = false;
			int lastNumber = -1;
			int index = defaultName.lastIndexOf("_");
			if (index > 0 && index < defaultName.length() - 1) {
				String lastSegment = defaultName.substring(index + 1);
				try {
					lastNumber = Integer.parseInt(lastSegment);
					isLastSegmentNumber = true;
				} catch (Exception e) {
				}
			}

			int appendNumber = 1;
			if (isLastSegmentNumber) {
				appendNumber = lastNumber + 1;
			}
			while (service.configRegistryExistsByName(name)) {
				String nextName = null;
				if (isLastSegmentNumber) {
					nextName = defaultName.substring(0, index) + "_" + appendNumber;
				} else {
					nextName = defaultName + "_" + appendNumber;
				}
				name = nextName;
				appendNumber++;
			}

		} else {
			if (service.configRegistryExistsByName(name)) {
				ErrorDTO error = new ErrorDTO("Config registry with name '" + name + "' already exists.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}
		}

		ConfigRegistry configRegistry = service.createConfigRegistry(type, name, properties);
		if (configRegistry == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Config registry cannot be created.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		ConfigRegistryMetadata metadata = configRegistry.getMetadata();
		ConfigRegistryDTO metadataDTO = ModelConverter.CONFIG_REGISTRY.toDTO(metadata);
		return Response.ok().entity(metadataDTO).build();
	}

}
