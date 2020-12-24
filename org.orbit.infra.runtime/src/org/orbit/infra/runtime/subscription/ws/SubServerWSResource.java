package org.orbit.infra.runtime.subscription.ws;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.infra.runtime.subscription.SubsServerService;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubServerWSResource extends AbstractWSApplicationResource {

	@Inject
	public SubsServerService service;

	public SubsServerService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("SubsServerService is not available.");
		}
		return this.service;
	}

	@POST
	@Path("request")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response request(@Context HttpHeaders httpHeaders, Request request) {
		return super.request(httpHeaders, request);
	}

}
