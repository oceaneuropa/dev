package org.orbit.platform.runtime.core.command;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.platform.runtime.core.Platform;
import org.origin.common.extensions.ExtensionActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;

public class StopServiceCommand extends AbstractWSCommand {

	protected Platform platform;

	public StopServiceCommand(Platform platform) {
		this.platform = platform;
	}

	@Override
	public Response execute(Request request) throws Exception {
		String extensionTypeId = (request.getParameter("extensionTypeId") instanceof String) ? (String) request.getParameter("extensionTypeId") : null;
		if (extensionTypeId == null || extensionTypeId.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'extensionTypeId' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		String extensionId = (request.getParameter("extensionId") instanceof String) ? (String) request.getParameter("extensionId") : null;
		if (extensionId == null || extensionId.isEmpty()) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "'extensionId' parameter is not set.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		IExtensionService extensionService = ExtensionActivator.getDefault().getExtensionService();
		IExtension extension = extensionService.getExtension(extensionTypeId, extensionId);
		if (extension == null) {
			ErrorDTO error = new ErrorDTO(String.valueOf(Status.BAD_REQUEST.getStatusCode()), "Extension does not exist.", null);
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		// Map<String, Object> parameters = request.getParameters();

		return null;
	}

}