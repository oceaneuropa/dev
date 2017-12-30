package org.origin.common.rest.client;

import java.net.ConnectException;
import java.net.HttpCookie;
import java.net.URI;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.origin.common.rest.model.Request;
import org.origin.common.rest.util.ResponseUtil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Ping the service.
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
 *
 */
public abstract class AbstractWSClient implements Pingable {

	protected ClientConfiguration config;
	protected Client client;

	/**
	 * 
	 * @param realm
	 * @param config
	 */
	public AbstractWSClient(ClientConfiguration config) {
		this.config = config;
		this.client = config.createClient();
	}

	public ClientConfiguration getClientConfiguration() {
		return this.config;
	}

	public void close() {
		this.client.close();
	}

	/**
	 * Get WetTarget of {scheme}://{host}:{port}/{contextRoot}
	 * 
	 * @return
	 */
	public WebTarget getRootPath() {
		return this.client.target(this.config.getBaseUrl());
	}

	/**
	 * 
	 * @param failOnUnknownProperties
	 * @return
	 */
	protected ObjectMapper createObjectMapper(boolean failOnUnknownProperties) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
		return mapper;
	}

	/**
	 * 
	 * @return
	 */
	public boolean doPing() {
		try {
			int ping = ping();
			if (ping > 0) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Ping the service.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
	 * 
	 * @return
	 * @throws ClientException
	 */
	public int ping() throws ClientException {
		int result = 0;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("ping");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			result = response.readEntity(Integer.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return result;
	}

	/**
	 * Ping the service.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping?forward={forward}
	 * 
	 * @param forward
	 * @return
	 * @throws ClientException
	 */
	public int ping(boolean forward) throws ClientException {
		int result = 0;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("ping").queryParam("forward", forward);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			result = response.readEntity(Integer.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return result;
	}

	/**
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/echo?message={message}
	 * 
	 * @param message
	 * @return
	 * @throws ClientException
	 */
	public String echo(String message) throws ClientException {
		String result = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("echo").queryParam("message", message);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			result = response.readEntity(String.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return result;
	}

	/**
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/level/{level1}/{level2}?message1={message1}&message2={message2}
	 * 
	 * @param level1
	 * @param level2
	 * @param message1
	 * @param message2
	 * @return
	 * @throws ClientException
	 */
	public String level(String level1, String level2, String message1, String message2) throws ClientException {
		String result = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("level").path(level1).path(level2).queryParam("message1", message1).queryParam("message2", message2);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			result = response.readEntity(String.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return result;
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ClientException
	 */
	public Response sendRequest(Request request) throws ClientException {
		Response wsResponse = null;
		try {
			WebTarget target = fillRequestPath(getRootPath());
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			wsResponse = updateHeaders(builder).post(Entity.json(new GenericEntity<Request>(request) {
			}));
			checkResponse(target, wsResponse);

		} catch (Exception e) {
			handleException(e);
		}
		return wsResponse;
	}

	/**
	 * 
	 * @param webTarget
	 * @return
	 */
	public WebTarget fillRequestPath(WebTarget webTarget) {
		return webTarget.path("/request");
	}

	/**
	 * Update headers of the Builder
	 * 
	 * @param builder
	 * @return
	 */
	public Builder updateHeaders(Builder builder) {
		String realm = this.config.getRealm();
		String username = this.config.getUsername();

		// 1. Get tokenType (e.g. "Bearer") and accessToken (e.g. JWT) value stored in CookieManager and set it in "cookie" request header.
		Token token = Realms.getRealm(realm).getToken(username);
		if (token != null) {
			// Set "Authorization" request header
			String tokenType = token.getTokenType();
			String accessToken = token.getAccessToken();
			builder = builder.header(HttpHeaders.AUTHORIZATION, tokenType + " " + accessToken);

			// put tokenType and accessToken in the cookie string.
			// if (!cookieString.isEmpty()) {
			// cookieString += ";";
			// }
			// cookieString += ("tokenType=" + tokenType);
			// cookieString += (";accessToken=" + accessToken);
		}

		// 2. Get "OrbitSession" value from "Set-Cookies" response header (retrieved from response header and stored in CookieManager for each user)
		// and set the
		// value in "cookie" request header.
		// - In handleResponseHeaders(Response), "Set-Cookies" response header value is retrieved and stored in CookieManager as cookies.
		// - For each user in a realm, there is one CookieManager instance for that specific user in that specific realm.
		// - The cookies stored in CookieManager for a user is used in this method.
		String cookieString = "";
		CookieManager cookieManager = Realms.getRealm(realm).getCookieManager(username);
		if (cookieManager != null) {
			HttpCookie[] cookies = cookieManager.getCookies();
			for (HttpCookie cookie : cookies) {
				String cookieName = cookie.getName();
				String cookieValue = cookie.getValue();

				if (!cookieString.isEmpty()) {
					cookieString += ";";
				}
				cookieString += (cookieName + "=" + cookieValue);
			}
		}

		// Set "cookie" request header
		if (!cookieString.isEmpty()) {
			builder = builder.header(HttpHeaders.COOKIE, cookieString);
		}

		return builder;
	}

	/**
	 * 
	 * @param target
	 * @param response
	 * @throws ClientException
	 */
	protected void checkResponse(WebTarget target, Response response) throws ClientException {
		if (response != null && response.getStatusInfo() != null) {
			if (!Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
				throw new ClientException(response);
			}
		}

		handleResponseHeaders(target, response);
	}

	/**
	 * 
	 * @param response
	 */
	protected void handleResponseHeaders(WebTarget target, Response response) {
		try {
			if (target != null && response != null) {
				URI uri = target.getUri();
				String setCookieString = response.getHeaderString("Set-Cookie"); //$NON-NLS-1$

				if (setCookieString != null) {
					String realm = this.config.getRealm();
					String username = this.config.getUsername();

					CookieManager cookieManager = Realms.getRealm(realm).getCookieManager(username);
					cookieManager.setCookies(uri, setCookieString);
				}

				String targetBaseURI = response.getHeaderString("orbit.targetURI");
				if (targetBaseURI != null) {
					System.out.println("orbit.targetURI:");
					System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
					System.out.println(uri.toString() + " -> " + targetBaseURI);
					System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
					System.out.println();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void updateToken(String tokenType, String accessToken) {
		String realm = this.config.getRealm();
		String username = this.config.getUsername();

		Token token = new Token(tokenType, accessToken);
		Realms.getRealm(realm).setToken(username, token);
	}

	/**
	 * 
	 * @param e
	 * @throws ClientException
	 */
	protected void handleException(Exception e) throws ClientException {
		if (e instanceof ClientException) {
			ClientException ce = (ClientException) e;
			throw ce;

		} else if (e instanceof ProcessingException) {
			ProcessingException pe = (ProcessingException) e;
			if (pe.getCause() instanceof ConnectException) {
				throw new ClientException(503, pe.getMessage(), pe.getCause());
			}
			throw new ClientException(500, pe.getMessage(), pe);
		} else if (e != null) {
			throw new ClientException(500, e.getMessage(), e);
		}
	}

}

// protected Map<String, HttpCookie> setCookieMap = new HashMap<String, HttpCookie>();

// ErrorDTO error = null;
// try {
// // String responseString = response.readEntity(String.class);
// error = response.readEntity(ErrorDTO.class);
// } catch (Exception e) {
// // e.printStackTrace();
// // System.err.println(getClass().getSimpleName() + ".checkResponse(Response) Exception [" + e.getClass().getSimpleName() + "] " +
// e.getMessage());
// // throw new ClientException(500, e.getMessage(), null);
// }
// if (error != null) {
// throw new ClientException(response.getStatus(), error.getMessage(), null);
// } else {
// throw new ClientException(response);
// }

// Map<String, String> props = this.config.getProperties();
// if (props != null) {
// int i = 0;
// for (Iterator<String> itor = props.keySet().iterator(); itor.hasNext();) {
// if (i > 0) {
// cookies += ";";
// }
// String key = itor.next();
// String value = props.get(key);
// cookies += (key + "=" + value);
// i++;
// }
// }
