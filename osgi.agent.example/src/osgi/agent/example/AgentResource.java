package osgi.agent.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.origin.common.util.Printer;

@Path("/")
public class AgentResource {

	protected boolean debug = true;

	@Context
	protected UriInfo uriInfo;

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		if (debug) {
			Printer.pl("/ping -> AgentResource.ping()");
		}

		return Response.ok("1").build();
	}

	@GET
	@Path("/version")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVersion() {
		if (debug) {
			Printer.pl("/version -> AgentResource.getVersion()");
		}

		return Response.ok("1.0.0").build();
	}

}
