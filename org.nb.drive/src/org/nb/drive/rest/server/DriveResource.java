package org.nb.drive.rest.server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.nb.drive.api.DriveException;
import org.nb.drive.rest.dto.DTOConverter;
import org.origin.common.rest.dto.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriveResource {

	protected static Logger logger = LoggerFactory.getLogger(DriveResource.class);

	@Context
	protected Providers providers;

	@Context
	protected UriInfo uriInfo;

	protected DriveService getDriveService() {
		DriveService driveService = this.providers.getContextResolver(DriveService.class, MediaType.APPLICATION_JSON_TYPE).getContext(DriveService.class);
		if (driveService == null) {
			throw new WebApplicationException(Status.SERVICE_UNAVAILABLE);
		}
		return driveService;
	}

	/**
	 * Handle DriveService and create ErrorDTO from it.
	 * 
	 * @param e
	 * @return
	 */
	protected ErrorDTO handleError(DriveException e) {
		e.printStackTrace();
		logger.error(e.getMessage());
		return DTOConverter.getInstance().toDTO(e);
	}

}
