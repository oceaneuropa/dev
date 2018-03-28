package org.orbit.component.runtime.tier1.auth.ws;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier1.auth.AuthorizationRequestDTO;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.TokenRequestDTO;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.runtime.tier1.auth.service.AuthModelConverter;
import org.orbit.component.runtime.tier1.auth.service.AuthService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.CookieUtil;
import org.origin.common.rest.util.ResponseUtil;

/**
 * @see https://www.oauth.com/oauth2-servers/definitions
 * 
 * @see https://www.oauth.com/oauth2-servers/definitions/#confidential-clients
 * 
 * @see https://www.oauth.com/oauth2-servers/access-tokens/refreshing-access-tokens/
 * 
 * 
 * @see ResourcesWebServiceResource for service dependency injection.
 * 
 * @see ProjectNodeResource for multiple POST web services and response with model.
 * 
 * @see https://stackoverflow.com/questions/8889679/how-to-create-a-cookie-and-add-to-http-response-from-inside-my-service-layer
 * 
 */
/*
 * Auth web service resource.
 * 
 * {contextRoot} example: /orbit/v1/auth
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/echo/{message}
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/authorize (Body parameter: AuthorizationRequestDTO)
 * 
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/token (Body parameter: TokenRequestDTO)
 * 
 * @see TransferAgentServiceResource
 */
@javax.ws.rs.Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AuthWSResource extends AbstractWSApplicationResource {

	// 1. https://www.oauth.com/oauth2-servers/definitions/
	// Roles:
	// --------------------------------------------------------
	// Resource owner (the user)
	// Resource server (the API)
	// Authorization server (can be the same server as the API)
	// Client (the third-party app)
	// --------------------------------------------------------

	// 2. https://www.oauth.com/oauth2-servers/definitions/#confidential-clients
	// Example URLs
	// --------------------------------------------------------
	// https://authorization-server.com/authorize
	// https://authorization-server.com/token
	// --------------------------------------------------------

	// 3. https://www.digitalocean.com/community/tutorials/an-introduction-to-oauth-2
	// (1) authorize
	// --------------------------------------------------------
	// https://cloud.digitalocean.com/v1/oauth/authorize?response_type=code&client_id=CLIENT_ID&redirect_uri=CALLBACK_URL&scope=read
	// https://cloud.digitalocean.com/v1/oauth/authorize: the API authorization endpoint
	// client_id=client_id: the application's client ID (how the API identifies the application)
	// redirect_uri=CALLBACK_URL: where the service redirects the user-agent after an authorization code is granted
	// response_type=code: specifies that your application is requesting an authorization code grant
	// scope=read: specifies the level of access that the application is requesting
	// --------------------------------------------------------
	// (2) token
	// --------------------------------------------------------
	// https://cloud.digitalocean.com/v1/oauth/token?client_id=CLIENT_ID&client_secret=CLIENT_SECRET&grant_type=authorization_code&code=AUTHORIZATION_CODE&redirect_uri=CALLBACK_URL
	// --------------------------------------------------------

	// 4. https://developers.hubspot.com/docs/methods/oauth2/get-access-and-refresh-tokens
	// (1) token request
	// --------------------------------------------------------
	// POST URL:
	// https://api.hubapi.com/oauth/v1/token
	// Headers:
	// Content-Type: application/x-www-form-urlencoded;charset=utf-8
	// Data:
	// grant_type=authorization_code&client_id=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx&client_secret=yyyyyyyy-yyyy-yyyy-yyyy-yyyyyyyyyyyy&redirect_uri=https://www.example.com/&code=zzzzzzzz-zzzz-zzzz-zzzz-zzzzzzzzzzzz
	// --------------------------------------------------------
	// (2) If successful, you will receive a JSON response with the tokens:
	// --------------------------------------------------------
	// {
	// "access_token": "xxxx",
	// "refresh_token": "yyyyyyyy-yyyy-yyyy-yyyy-yyyyyyyyyyyy",
	// "expires_in": 21600
	// }
	// --------------------------------------------------------
	//
	// (3) If there are any problems with the request, you'll receive a 400 response with an error message.
	// --------------------------------------------------------
	// {
	// "error": "error_code",
	// "error_description": "A human readable error message"
	// }
	// --------------------------------------------------------

	// 5. https://stackoverflow.com/questions/5925954/what-are-bearer-tokens-and-token-type-in-oauth-2
	// --------------------------------------------------------
	// Bearer can be simply understood as "give access to the bearer of this token." It's the equivalent of issuing a cheque "give money to the
	// bearer of the
	// cheque".
	// --------------------------------------------------------

	@Inject
	public AuthService service;

	protected AuthService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("AuthService is not available.");
		}
		return this.service;
	}

	@POST
	@Path("authorize")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authorize( //
			@Context HttpServletRequest servletRequest, //
			@Context HttpServletResponse servletResponse, //
			@Context ServletContext servletContext, //
			@Context HttpHeaders httpHeaders, //
			AuthorizationRequestDTO authorizationRequestDTO) {
		try {
			AuthorizationResponse response = getService().authorize(AuthModelConverter.getInstance().toRequest(authorizationRequestDTO));
			return Response.ok().entity(AuthModelConverter.getInstance().toResponseDTO(response)).build();

		} catch (ServerException e) {
			e.printStackTrace();
			// return Response.status(Status.BAD_REQUEST).entity(AuthConverter.getInstance().toResponseDTO(e)).build();
			ErrorDTO error = ResponseUtil.toDTO(e, e.getCode());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

	@POST
	@Path("token")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response token( //
			@Context HttpServletRequest servletRequest, //
			@Context HttpServletResponse servletResponse, //
			@Context ServletContext servletContext, //
			@Context HttpHeaders httpHeaders, //
			TokenRequestDTO tokenRequestDTO) {
		try {
			TokenResponse response = getService().getToken(AuthModelConverter.getInstance().toRequest(tokenRequestDTO));

			if (response != null && response.getAccessToken() != null) {
				String accessToken = response.getAccessToken();
				Cookie cookie = CookieUtil.create("OrbitSession", accessToken, false, 60 * 60 * 24, "/");
				servletResponse.addCookie(cookie);
			}

			// Cookie cookie2 = create("OrbitSession2", "Einstein@mtswz", false, 60 * 60 * 24, "/");
			// servletResponse.addCookie(cookie2);

			// test only
			if (response != null) {
				response.setState("from '" + getService().getName() + "'");
			}

			return Response.ok().entity(AuthModelConverter.getInstance().toResponseDTO(response)).build();

		} catch (ServerException e) {
			e.printStackTrace();
			// return Response.status(Status.BAD_REQUEST).entity(AuthConverter.getInstance().toResponseDTO(e)).build();
			ErrorDTO error = ResponseUtil.toDTO(e, e.getCode());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

}

// @GET
// @Path("ping")
// @Produces(MediaType.APPLICATION_JSON)
// public Response ping() {
// if (this.service != null) {
// return Response.ok(1).build();
// }
// return Response.ok(0).build();
// }
