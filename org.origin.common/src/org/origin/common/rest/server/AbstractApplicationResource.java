package org.origin.common.rest.server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractApplicationResource {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Context
	protected Providers providers;

	@Context
	protected UriInfo uriInfo;

	protected <T> T getService(Class<T> serviceClass) {
		T service = this.providers.getContextResolver(serviceClass, MediaType.APPLICATION_JSON_TYPE).getContext(serviceClass);
		if (service == null) {
			throw new WebApplicationException(Status.SERVICE_UNAVAILABLE);
		}
		return service;
	}

}
