package org.orbit.component.server.tier3.domain.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.server.tier3.domain.service.DomainMgmtService;
import org.origin.common.rest.server.AbstractApplicationResource;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DomainMgmtServiceResource extends AbstractApplicationResource {

	@GET
	@Path("ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		DomainMgmtService service = getService(DomainMgmtService.class);
		if (service != null) {
			return Response.ok(1).build();
		}
		return Response.ok(0).build();
	}

}
