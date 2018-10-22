package org.orbit.infra.runtime.configregistry.ws;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
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

}
