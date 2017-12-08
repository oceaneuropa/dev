package org.orbit.component.server.tier3.domain.ws;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.orbit.component.server.tier3.domain.service.DomainManagementService;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DomainMgmtWSServiceResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(DomainMgmtWSServiceResource.class);

	@Inject
	public DomainManagementService service;

	protected DomainManagementService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DomainManagementService is not available.");
		}
		return this.service;
	}

}
