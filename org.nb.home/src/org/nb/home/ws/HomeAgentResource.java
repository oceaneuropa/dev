package org.nb.home.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.nb.home.model.exception.HomeException;
import org.nb.home.service.HomeAgentService;
import org.origin.common.command.IEditingDomain;
import org.origin.common.rest.agent.AbstractAgentResource;

/*
 * Home Agent resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
 * 
 * Note: 
 * A new instance of HomeAgentResource is created for every web service request.
 * 
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class HomeAgentResource extends AbstractAgentResource {

	@Override
	protected IEditingDomain getEditingDomain() {
		return getService(HomeAgentService.class).getEditingDomain();
	}

	@GET
	@Path("/ping")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		int pingResult = -1;
		try {
			pingResult = getService(HomeAgentService.class).ping();
		} catch (HomeException e) {
			e.printStackTrace();
		}
		System.out.println("HomeAgentResource.ping() pingResult=" + pingResult);
		return Response.ok(String.valueOf(pingResult)).build();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (HomeAgentService.class.isAssignableFrom(adapter)) {
			return (T) getService(HomeAgentService.class);
		}
		return super.getAdapter(adapter);
	}

}
