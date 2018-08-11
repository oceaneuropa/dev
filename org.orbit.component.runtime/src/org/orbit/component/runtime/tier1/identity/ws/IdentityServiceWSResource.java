package org.orbit.component.runtime.tier1.identity.ws;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier1.identity.LoginRequestDTO;
import org.orbit.component.model.tier1.identity.LoginResponseDTO;
import org.orbit.component.model.tier1.identity.LogoutRequestDTO;
import org.orbit.component.model.tier1.identity.RegisterRequestDTO;
import org.orbit.component.runtime.model.identity.LoginRequest;
import org.orbit.component.runtime.model.identity.LoginResponse;
import org.orbit.component.runtime.model.identity.LogoutRequest;
import org.orbit.component.runtime.model.identity.LogoutResponse;
import org.orbit.component.runtime.model.identity.RegisterRequest;
import org.orbit.component.runtime.model.identity.RegisterResponse;
import org.orbit.component.runtime.tier1.identity.service.IdentityService;
import org.orbit.component.runtime.util.ModelConverter;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;

/*
 * Identity web service resource.
 * 
 * {contextRoot}: /orbit/v1/identity
 * 
 * Identity: 
 * URL (GET):  {scheme}://{host}:{port}/{contextRoot}/exists?type={elementType}&value={elementValue}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/register (Body parameter: RegisterRequestDTO)
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/login    (Body parameter: LoginRequestDTO)
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/logout   (Body parameter: LogoutRequestDTO)
 * 
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class IdentityServiceWSResource extends AbstractWSApplicationResource {

	@Inject
	public IdentityService service;

	public IdentityService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("IdentityService is not available.");
		}
		return this.service;
	}

	/**
	 * Check whether an element (specified by type) with a specified value exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/exists?type={elementType}&value={elementValue}
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	@GET
	@Path("exists")
	@Produces(MediaType.APPLICATION_JSON)
	public Response exists(@QueryParam("type") String type, @QueryParam("value") String value) {
		if (type == null || type.isEmpty()) {
			ErrorDTO nullError = new ErrorDTO("type is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullError).build();
		}
		if (value == null || value.isEmpty()) {
			ErrorDTO nullError = new ErrorDTO("value is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullError).build();
		}

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		try {
			boolean exists = false;
			IdentityService service = getService();

			if ("username".equalsIgnoreCase(type)) {
				exists = service.usernameExists(value);

			} else if ("email".equalsIgnoreCase(type)) {
				exists = service.emailExists(value);

			} else {
				ErrorDTO nullError = new ErrorDTO("type '" + type + "' is not supported. Supported types are: 'username' and 'email'.");
				return Response.status(Status.BAD_REQUEST).entity(nullError).build();
			}
			result.put("exists", exists);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(result).build();
	}

	/**
	 * Register
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/register (Body parameter: RegisterRequestDTO)
	 * 
	 * @param requestDTO
	 * @return
	 */
	@POST
	@Path("register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(RegisterRequestDTO requestDTO) {
		if (requestDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("requestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		try {
			IdentityService service = getService();
			RegisterRequest request = ModelConverter.Identity.toRequest(requestDTO);
			RegisterResponse response = service.register(request);

			String status = response.isSucceed() ? StatusDTO.SUCCESS : StatusDTO.FAILED;
			String message = response.getMessage();

			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, status, message);
			return Response.ok().entity(statusDTO).build();

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

	/**
	 * Login
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/login (Body parameter: LoginRequestDTO)
	 * 
	 * @param requestDTO
	 * @return
	 */
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(LoginRequestDTO requestDTO) {
		if (requestDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("requestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		try {
			IdentityService service = getService();
			LoginRequest request = ModelConverter.Identity.toRequest(requestDTO);
			LoginResponse response = service.login(request);
			LoginResponseDTO responseDTO = ModelConverter.Identity.toResponse(response);
			return Response.ok().entity(responseDTO).build();

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

	/**
	 * Logout
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/logout (Body parameter: LogoutRequestDTO)
	 * 
	 * @param requestDTO
	 * @return
	 */
	@POST
	@Path("logout")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logout(LogoutRequestDTO requestDTO) {
		if (requestDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("requestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		try {
			IdentityService service = getService();
			LogoutRequest request = ModelConverter.Identity.toRequest(requestDTO);
			LogoutResponse response = service.logout(request);

			String status = response.isSucceed() ? StatusDTO.SUCCESS : StatusDTO.FAILED;
			String message = response.getMessage();

			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, status, message);
			return Response.ok().entity(statusDTO).build();

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

}
