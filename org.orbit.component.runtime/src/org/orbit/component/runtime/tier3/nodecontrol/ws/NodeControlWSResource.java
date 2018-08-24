package org.orbit.component.runtime.tier3.nodecontrol.ws;

import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import org.orbit.component.runtime.tier3.nodecontrol.service.NodeControlService;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.editpolicy.WSCommand;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;

/*
 * https://docs.oracle.com/cd/E19798-01/821-1841/6nmq2cp1v/index.html
 * 
 * Transfer agent web service resource.
 * 
 * {contextRoot} example: /orbit/v1/nodecontrol
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
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN, OrbitRoles.DOMAIN_MANAGEMENT_ADMIN })
@javax.ws.rs.Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class NodeControlWSResource extends AbstractWSApplicationResource {

	@Inject
	public NodeControlService service;

	public NodeControlService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("NodeControl is not available.");
		}
		return this.service;
	}

	@POST
	@Path("request")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response request( //
			@Context HttpServletRequest servletRequest, //
			@Context HttpServletResponse servletResponse, //
			@Context ServletContext servletContext, //
			@Context HttpHeaders httpHeaders, //
			Request request) {
		System.out.println(getClass().getSimpleName() + ".request()");

		HttpSession session = servletRequest.getSession();
		System.out.println("HttpSession:");
		System.out.println("-----------------------------------------------------------------");
		if (session == null) {
			System.out.println("null");
		} else {
			String id = session.getId();
			long creationTIme = session.getCreationTime();
			long lastAccessTime = session.getLastAccessedTime();
			boolean isNew = session.isNew();
			int interval = session.getMaxInactiveInterval();
			System.out.println("id = " + id);
			System.out.println("creationTIme = " + new Date(creationTIme));
			System.out.println("lastAccessTime = " + new Date(lastAccessTime));
			System.out.println("isNew = " + isNew);
			System.out.println("interval = " + interval);
			System.out.println("Attributes:");
			for (Enumeration<String> enumr = session.getAttributeNames(); enumr.hasMoreElements();) {
				String attrName = enumr.nextElement();
				Object attrValue = session.getAttribute(attrName);
				String attrValueStr = attrValue != null ? attrValue.toString() : null;
				System.out.println("\t" + attrName + " = " + attrValueStr);
			}
			session.setAttribute("accessToken", "Einstein@mtswz");
		}
		System.out.println("-----------------------------------------------------------------");

		MultivaluedMap<String, String> requestHeaders = httpHeaders.getRequestHeaders();
		Map<String, Cookie> cookies = httpHeaders.getCookies();

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

		NodeControlService service = getService();

		WSCommand command = service.getEditPolicies().getCommand(httpHeaders, request);
		if (command != null) {
			try {
				return command.execute(request);

			} catch (Exception e) {
				String statusCode = String.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode());
				if (e instanceof ServerException) {
					statusCode = ((ServerException) e).getCode();
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
