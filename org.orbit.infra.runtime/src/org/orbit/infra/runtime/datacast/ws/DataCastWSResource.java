package org.orbit.infra.runtime.datacast.ws;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataCast web service resource.
 * 
 * {contextRoot} example: /orbit/v1/datacast
 *
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.USER })
@javax.ws.rs.Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DataCastWSResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(DataCastWSResource.class);

	@Inject
	public DataCastService service;

	public DataCastService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DataCastService is not available.");
		}
		return this.service;
	}

}
