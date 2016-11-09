package org.origin.common.rest.server;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.ModelConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractApplicationResource implements IAdaptable {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

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

	/**
	 * Handle Exception and create ErrorDTO from it.
	 * 
	 * @param e
	 * @param errorCode
	 * @param printStackTrace
	 * @return
	 */
	protected ErrorDTO handleError(Exception e, String errorCode, boolean printStackTrace) {
		if (printStackTrace) {
			e.printStackTrace();
		}
		logger.error(e.getMessage());
		return ModelConverter.toDTO(e, errorCode);
	}

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}
