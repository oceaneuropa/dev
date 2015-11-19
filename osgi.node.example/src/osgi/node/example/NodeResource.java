package osgi.node.example;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class NodeResource {

	protected boolean debug = true;

	@Context
	protected UriInfo uriInfo;

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		if (debug) {
			Printer.pl("/ping -> NodeResource.ping()");
		}

		return Response.ok("1").build();
	}

	@GET
	@Path("/version")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVersion() {
		if (debug) {
			Printer.pl("/version -> NodeResource.getVersion()");
		}

		return Response.ok("1.0.0").build();
	}

	@GET
	@Path("/shutdown")
	@Produces(MediaType.APPLICATION_JSON)
	public Response shutdown() {
		if (debug) {
			Printer.pl("/shutdown -> NodeResource.shutdown()");
		}

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Printer.pl("System.exit(1)");
				System.exit(1);
			}
		});
		thread.start();

		Printer.pl("this.uriInfo.getAbsolutePath() = " + this.uriInfo.getAbsolutePath());
		return Response.ok("shutdown " + this.uriInfo.getAbsolutePath()).build();
	}

}
