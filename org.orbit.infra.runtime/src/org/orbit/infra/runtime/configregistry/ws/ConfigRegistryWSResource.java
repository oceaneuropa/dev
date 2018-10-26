package org.orbit.infra.runtime.configregistry.ws;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Config registry web service resource.
 * 
 * {contextRoot} example: /orbit/v1/config
 *
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.USER })
@javax.ws.rs.Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigRegistryWSResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(ConfigRegistryWSResource.class);

	@Inject
	public ConfigRegistryService service;

	public ConfigRegistryService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("ConfigRegistryService is not available.");
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
