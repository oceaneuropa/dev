package org.origin.common.rest.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.model.Resource;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.switcher.Switcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 1. Why using ObjectNode to deserialize request body
 * @see https://stackoverflow.com/questions/19389723/can-not-deserialize-instance-of-java-lang-string-out-of-start-object-token
 * 
 * 2. How to handle query parameter with multiple values
 * @see https://stackoverflow.com/questions/24059773/correct-way-to-pass-multiple-values-for-same-parameter-name-in-get-request
 * 
 * 3. Do not set "Content-Length" header to avoid exception (org.apache.http.ProtocolException: Content-Length header already present)
 * @see https://stackoverflow.com/questions/3332370/content-length-header-already-present
 *
 * 4. Send javax ws-rs post request.
 * @see https://stackoverflow.com/questions/27211012/how-to-send-json-object-from-rest-client-using-javax-ws-rs-client-webtarget
 * 
 * 5. Inflector example
 * https://www.programcreek.com/java-api-examples/index.php?api=org.glassfish.jersey.process.Inflector
 * https://www.programcreek.com/java-api-examples/index.php?api=org.glassfish.jersey.server.model.Resource
 * 
 */
public class WSMethodInflector implements Inflector<ContainerRequestContext, Response> {

	protected static Logger LOG = LoggerFactory.getLogger(WSMethodInflector.class);

	protected String methodType;
	protected String methodPath;
	protected String produces;
	protected Client client;
	protected Switcher<URI> baseUriSwitcher;

	public WSMethodInflector() {
	}

	public WSMethodInflector(Resource.Builder wsResource, String methodPath, String methodType, String produces, Client client, Switcher<URI> baseUriSwitcher) {
		this.methodPath = methodPath;
		this.methodType = methodType;
		this.produces = produces;
		this.client = client;
		this.baseUriSwitcher = baseUriSwitcher;

		addToResource(wsResource);
	}

	public void addToResource(Resource.Builder wsResource) {
		wsResource.addChildResource(getMethodPath()).addMethod(getMethodType()).produces(getProduces()).handledBy(this);
	}

	public String getMethodType() {
		return this.methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public String getMethodPath() {
		return this.methodPath;
	}

	public void setMethodPath(String methodPath) {
		this.methodPath = methodPath;
	}

	public String getProduces() {
		return this.produces;
	}

	public void setProduces(String produces) {
		this.produces = produces;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Switcher<URI> getSwitcher() {
		return baseUriSwitcher;
	}

	public void setSwitcher(Switcher<URI> baseUriSwitcher) {
		this.baseUriSwitcher = baseUriSwitcher;
	}

	protected void checkClient() {
		if (this.client == null) {
			throw new IllegalStateException("Web service client is not set.");
		}
	}

	protected void checkSwitcher() {
		if (this.baseUriSwitcher == null) {
			throw new IllegalStateException("Base URI switcher is not set.");
		}
	}

	public void selfCheck() {
		checkClient();
		checkSwitcher();
	}

	@Override
	public Response apply(ContainerRequestContext requestContext) {
		LOG.debug("apply()");

		selfCheck();

		// Step1. load the bullet
		Object payload = getPayload(requestContext);

		// Step2. switch the barrel
		URI targetBaseURI = getTargetBaseURI(requestContext);
		if (targetBaseURI == null) {
			System.err.println("Target base URI is null.");
			return Response.serverError().entity(new ErrorDTO("500", "Target base URI is not available.", null)).build();
		}

		// Step3. aim the target
		// (1) append request path
		String newRequestUriStr = appendRequestPath(requestContext, targetBaseURI);

		// (2) append query parameters
		newRequestUriStr = appendQueryParameters(requestContext, newRequestUriStr);

		// (3) aim the target
		URI newRequestUri = null;
		try {
			newRequestUri = new URI(newRequestUriStr);
		} catch (Exception e) {
			System.err.println("New request URI is invalid. " + e.getMessage());
			return Response.serverError().entity(new ErrorDTO("500", "New request URI is invalid. ", e.getMessage())).build();
		}

		// System.out.println(getClass().getSimpleName() + ".apply() newRequestUri = " + newRequestUri);
		LOG.debug("newRequestUri = " + newRequestUri);

		// Invocation.Builder newWSResource = this.client.target(newRequestUri).request(MediaType.APPLICATION_JSON);
		Invocation.Builder newWSResource = this.client.target(newRequestUri).request();
		newWSResource.accept(new MediaType[] { MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE, MediaType.TEXT_PLAIN_TYPE });

		// (4) update request headers
		updateRequestHeaders(requestContext, newWSResource);

		// Step4. pull the trigger and fire!
		Response response = sendRequest(newWSResource, payload);

		// The "switcher.targetURI" response header is read by web service client
		// - see AbstractWSClient.handleResponseHeaders(WebTarget target, Response response) method.
		response.getHeaders().putSingle("switcher.targetURI", newRequestUri.toString());

		return response;
	}

	/**
	 * Step1. load the bullet
	 * 
	 * @param requestContext
	 * @return
	 */
	protected Object getPayload(ContainerRequestContext requestContext) {
		Object payload = null;
		InputStream input = null;
		try {
			input = requestContext.getEntityStream();
			if (input != null && input.available() > 0) {
				// Note:
				// - Read input stream into JsonNode causes issue for POST method, when the input stream is for the body parameter of the POST method.
				// - The JsonNode deserialized from the input stream contains fields like "nodeType" and "array". Then such JsonNode data gets forwarded to
				// target web service.
				// - When target web services get the data and tries to deserialize it into specific DTO (e.g. IndexItemSetPropertiesRequestDTO), the
				// deserialization of the data into the DTO fails.

				// Do not do this:
				// payload = JSONUtils.getJsonReader(JsonNode.class).readValue(input);

				// Forward the input stream directly as payload to the target web service and it works just fine!
				return input;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return payload;
	}

	/**
	 * Step2. switch the barrel
	 * 
	 * @param requestContext
	 * @return
	 */
	protected URI getTargetBaseURI(ContainerRequestContext requestContext) {
		URI targetBaseURI = this.baseUriSwitcher.getNext(requestContext, this.methodPath, 3, 1000);
		return targetBaseURI;
	}

	/**
	 * Step3. aim the target
	 * 
	 * (1) append request path
	 * 
	 * @param requestContext
	 * @param baseURI
	 * @return
	 */
	protected String appendRequestPath(ContainerRequestContext requestContext, URI baseURI) {
		String path = requestContext.getUriInfo().getPath();
		String newRequestURIstr = null;
		if (baseURI.toString().endsWith("/") || path.startsWith("/")) {
			newRequestURIstr = baseURI.toString() + path;
		} else {
			newRequestURIstr = baseURI.toString() + "/" + path;
		}
		return newRequestURIstr;
	}

	/**
	 * Step3. aim the target
	 * 
	 * (2) append query parameters
	 * 
	 * - UriInfo.getPath() does not include query parameters. Need to check UriInfo.getPath(boolean)
	 * 
	 * - when there is path parameters, the path parameters values are already in the path. So there is no need to configure path parameters in the path.
	 * 
	 * @param requestContext
	 * @param newRequestURIstr
	 * @return
	 */
	protected String appendQueryParameters(ContainerRequestContext requestContext, String newRequestURIstr) {
		String queryParamStr = "";
		MultivaluedMap<String, String> queryParams = requestContext.getUriInfo().getQueryParameters();
		if (queryParams != null) {
			for (Iterator<String> queryParamItor = queryParams.keySet().iterator(); queryParamItor.hasNext();) {
				String paramName = queryParamItor.next();
				List<String> paramValues = queryParams.get(paramName);
				if (paramValues != null) {
					for (String paramValue : paramValues) {
						if (!queryParamStr.isEmpty()) {
							queryParamStr += "&";
						}
						queryParamStr += paramName + "=" + paramValue;
					}
				}
			}
		}
		if (!queryParamStr.isEmpty()) {
			if (newRequestURIstr.contains("?")) {
				if (newRequestURIstr.endsWith("?")) {
					newRequestURIstr += queryParamStr;
				} else {
					newRequestURIstr += "&" + queryParamStr;
				}
			} else {
				newRequestURIstr += "?" + queryParamStr;
			}
		}
		return newRequestURIstr;
	}

	/**
	 * Step3. aim the target
	 * 
	 * (4) update request headers
	 * 
	 * @param requestContext
	 * @param wsResource
	 */
	protected void updateRequestHeaders(ContainerRequestContext requestContext, Invocation.Builder wsResource) {
		Map<String, String> newRequestHeaders = new HashMap<String, String>();

		MultivaluedMap<String, String> requestHeaders = requestContext.getHeaders();
		if (requestHeaders != null) {
			for (Iterator<String> headerItor = requestHeaders.keySet().iterator(); headerItor.hasNext();) {
				String headerName = headerItor.next();
				String headerValueString = requestContext.getHeaderString(headerName);
				newRequestHeaders.put(headerName, headerValueString);
			}
		}
		for (Iterator<String> newHeaderItor = newRequestHeaders.keySet().iterator(); newHeaderItor.hasNext();) {
			String headerName = newHeaderItor.next();
			String headerValue = newRequestHeaders.get(headerName);
			wsResource.header(headerName, headerValue);
		}
	}

	/**
	 * Step4. pull the trigger and fire!
	 * 
	 * @param wsResource
	 * @param payload
	 * @return
	 */
	protected Response sendRequest(Invocation.Builder wsResource, Object payload) {
		LOG.debug("sendRequest()");

		Response response = null;
		try {
			Entity<?> bodyParam = null;

			// Note:
			// - Javax ws-rs Entity is the body parameter for POST and PUT method.
			// - The Entity is created using com.fasterxml.jackson.databind.JsonNode deserialized from entity input stream from ContainerRequestContext (wrong)
			// - The Entity is created using entity input stream from ContainerRequestContext directly.
			if (payload != null) {
				bodyParam = Entity.entity(payload, MediaType.APPLICATION_JSON_TYPE);
			}

			if ("GET".equalsIgnoreCase(this.methodType)) {
				// e.g. http://127.0.0.1:8002/orbit/v1/indexservice/indexitems/platform.indexer?name=Sun&type=Platform
				response = wsResource.get(Response.class);

			} else if ("POST".equalsIgnoreCase(this.methodType)) {
				response = wsResource.post(bodyParam, Response.class);

			} else if ("PUT".equalsIgnoreCase(this.methodType)) {
				response = wsResource.put(bodyParam, Response.class);

			} else if ("DELETE".equalsIgnoreCase(this.methodType)) {
				response = wsResource.delete(Response.class);
			}

		} catch (Exception e) {
			LOG.debug("sendRequest() " + e.getClass().getName() + ": " + e.getMessage());
			// e.printStackTrace();
			System.err.println(e.toString());

			if (response != null) {
				return response;
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO("500", "Error occurs when forwarding request.", e.getMessage())).build();
		}

		return response;
	}

}

// map = JSONUtils.getJsonReader(Map.class).readValue(input);
// payload = JSONUtils.getJsonReader(String.class).readValue(input);

// URI reqUri = uriInfo.getRequestUri(); // e.g. http://127.0.0.1:13001/orbit/v1/ta/request
// URI absPath = uriInfo.getAbsolutePath(); // e.g. http://127.0.0.1:13001/orbit/v1/ta/request
// URI baseUri = uriInfo.getBaseUri(); // e.g. http://127.0.0.1:13001/orbit/v1/ta/
// String path = uriInfo.getPath(); // e.g. request
// String path1 = uriInfo.getPath(true); // e.g. request
// String path2 = uriInfo.getPath(false); // e.g. request
// int length = requestContext.getLength(); // e.g. 44
// String method = requestContext.getMethod(); // e.g. "POST"
// Request request = requestContext.getRequest(); // e.g. org.glassfish.jersey.server.ContainerRequest@58de0746
// MultivaluedMap<String, String> pathParams = uriInfo.getPathParameters();

// URI url = new URI(driverFactory.getClientConfiguration().getScheme() + "://" + host+ ":" + port + resourcePath);
// http://127.0.0.1:12001/orbit/v1/ta (real)
// http://127.0.0.1:13001/orbit/v1/ta (lbr)
// String newRequestPath = "http://127.0.0.1:12001/orbit/v1/ta/" + path;

// if ("Content-Length".equals(headerName)) {
// continue;
// }

// bodyParam = Entity.json(new GenericEntity<ObjectNode>((ObjectNode) payload) {
// });
// bodyParam = Entity.json(new GenericEntity<JsonToken>(((ObjectNode) payload).asToken()) {
// });
// bodyParam = Entity.json(new GenericEntity<Object>(payload) {
// });
// bodyParam = Entity.json(new GenericEntity<String>(((ObjectNode) payload).toString()) {
// });
// bodyParam = Entity.json(new StringEntity<(((ObjectNode) payload).toString()) {
// });

// bodyParam = Entity.json(payload);
// bodyParam = Entity.entity(stringPayload, MediaType.APPLICATION_JSON_TYPE);

// bodyParam = Entity.json(new GenericEntity<String>(stringPayload) {
// });
