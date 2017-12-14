package org.orbit.component.runtime.tier3.transferagent.ws;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier3.transferagent.TransferAgentException;
import org.orbit.component.runtime.tier3.transferagent.service.TransferAgentService;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/**
 * https://docs.oracle.com/cd/E19798-01/821-1841/6nmq2cp1v/index.html
 * 
 * Transfer agent web service resource.
 * 
 * {contextRoot} example: /orbit/v1/ta
 *
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/request (body parameter: Request)
 *
 * @see HomeAgentResource
 * @see HomeWorkspacesResource
 * 
 * @see https://www.programcreek.com/java-api-examples/index.php?api=javax.ws.rs.core.Cookie
 * @see https://www.programcreek.com/java-api-examples/index.php?source_dir=opensoc-streaming-master/OpenSOC-DataServices/src/main/java/com/opensoc/dataservices
 *      /auth/RestSecurityInterceptor.java
 * 
 */
@javax.ws.rs.Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TransferAgentWSServiceResource extends AbstractWSApplicationResource {

	@Inject
	public TransferAgentService service;

	public TransferAgentService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("TransferAgentService is not available.");
		}
		return this.service;
	}

	@POST
	@Path("request")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response request(@Context HttpHeaders hh, Request request) {
		System.out.println(getClass().getSimpleName() + ".request()");

		MultivaluedMap<String, String> requestHeaders = hh.getRequestHeaders();
		Map<String, Cookie> cookies = hh.getCookies();

		System.out.println("Headers:");
		System.out.println("-----------------------------------------------------------------");
		for (Iterator<String> headerNameItor = requestHeaders.keySet().iterator(); headerNameItor.hasNext();) {
			String headerName = headerNameItor.next();
			String firstHeaderValue = requestHeaders.getFirst(headerName);
			List<String> headerValues = requestHeaders.get(headerName);
			String headerValuesString = Arrays.toString(headerValues.toArray(new String[headerValues.size()]));
			System.out.println(headerName + " = " + firstHeaderValue + ".. " + headerValuesString);
		}
		System.out.println("-----------------------------------------------------------------");

		System.out.println("Cookies:");
		System.out.println("-----------------------------------------------------------------");
		int i = 0;
		for (Iterator<String> cookieItor = cookies.keySet().iterator(); cookieItor.hasNext();) {
			String cookieName = cookieItor.next();
			Cookie cookie = cookies.get(cookieName);

			int version = cookie.getVersion();
			String domain = cookie.getDomain();
			String name = cookie.getName();
			String path = cookie.getPath();
			String value = cookie.getValue();

			System.out.println("Cookie[" + i + "] (version=" + version + ", domain=" + domain + ", name=" + name + ", path=" + path + ", value=" + value + ")");
			i++;
		}
		System.out.println("-----------------------------------------------------------------");

		TransferAgentService service = getService();

		WSCommand command = service.getEditPolicies().getCommand(request);
		if (command != null) {
			try {
				return command.execute(request);

			} catch (Exception e) {
				String statusCode = String.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode());
				if (e instanceof TransferAgentException) {
					statusCode = ((TransferAgentException) e).getCode();
				}
				ErrorDTO error = handleError(e, statusCode, true);
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
			}

		} else {
			// no command is found for the request
			// - return error response
			ErrorDTO error = new ErrorDTO("Request '" + request.getRequestName() + "' is not supported.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}
	}

}
