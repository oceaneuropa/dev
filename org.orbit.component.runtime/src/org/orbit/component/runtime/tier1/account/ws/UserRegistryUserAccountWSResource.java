package org.orbit.component.runtime.tier1.account.ws;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier1.account.UserAccountActionDTO;
import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.orbit.component.runtime.model.account.UserAccount;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.component.runtime.util.ModelConverter;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;

/*
 * User account web service resource.
 * 
 * {contextRoot}: /orbit/v1/userregistry
 * 
 * User account: 
 * URL (GET):  {scheme}://{host}:{port}/{contextRoot}/useraccount?username={username}
 * URL (GET):  {scheme}://{host}:{port}/{contextRoot}/useraccount/activated?username={username}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/useraccount/action (Body parameter: UserAccountActionDTO)
 * 
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN, OrbitRoles.USER_ACCOUNTS_ADMIN })
@Path("/useraccount")
@Produces(MediaType.APPLICATION_JSON)
public class UserRegistryUserAccountWSResource extends AbstractWSApplicationResource {

	@Inject
	public UserRegistryService service;

	protected UserRegistryService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("UserRegistryService is not available.");
		}
		return this.service;
	}

	/**
	 * Get a user account.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccount
	 * 
	 * @param username
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("username") String username) {
		UserAccountDTO userAccountDTO = null;
		try {
			UserRegistryService service = getService();
			UserAccount userAccount = service.getUserAccount(username);
			if (userAccount == null) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("User account with userId '%s' cannot be found.", username));
				return Response.status(Status.NOT_FOUND).entity(error).build();
			}
			userAccountDTO = ModelConverter.Account.toUserAccountDTO(userAccount);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(userAccountDTO).build();
	}

	/**
	 * Check whether a user account is activated.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccount/activated?username={username}
	 * 
	 * @param username
	 * @return
	 */
	@GET
	@Path("/activated")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isActivated(@QueryParam("username") String username) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		UserRegistryService service = getService();
		try {
			boolean activated = service.isUserAccountActivated(username);
			result.put("activated", activated);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(result).build();
	}

	/**
	 * User account action.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/useraccount/action (Body parameter: UserAccountActionDTO)
	 * 
	 * @param username
	 * @return
	 */
	@PUT
	@Path("action")
	@Produces(MediaType.APPLICATION_JSON)
	public Response onAction(UserAccountActionDTO actionDTO) {
		if (actionDTO == null) {
			ErrorDTO error = new ErrorDTO("UserAccountActionDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(error).build();
		}

		Map<String, Object> result = new HashMap<String, Object>();
		UserRegistryService service = getService();
		try {
			String userId = actionDTO.getUserId();
			String action = actionDTO.getAction();
			Map<Object, Object> args = actionDTO.getArgs();

			if (userId == null || userId.isEmpty()) {
				ErrorDTO error = new ErrorDTO("userId is empty.");
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

			UserAccount userAccount = service.getUserAccount(userId);
			if (userAccount == null) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("User account with userId '%s' cannot be found.", userId));
				return Response.status(Status.NOT_FOUND).entity(error).build();
			}

			if ("activate".equalsIgnoreCase(action)) {
				boolean activated = false;
				boolean succeed = service.activateUserAccount(userId);
				if (succeed) {
					activated = service.isUserAccountActivated(userId);
				}
				result.put("activated", activated);

			} else if ("deactivate".equalsIgnoreCase(action)) {
				boolean activated = false;
				boolean succeed = service.deactivateUserAccount(userId);
				if (succeed) {
					activated = service.isUserAccountActivated(userId);
				}
				result.put("activated", activated);

			} else if ("change_password".equalsIgnoreCase(action)) {
				String oldPassword = null;
				String newPassword = null;

				if (args.get("oldpassword") instanceof String) {
					oldPassword = (String) args.get("oldpassword");
				}
				if (args.get("newpassword") instanceof String) {
					newPassword = (String) args.get("newpassword");
				}

				if (newPassword == null || newPassword.isEmpty()) {
					ErrorDTO error = new ErrorDTO("New password is empty.");
					return Response.status(Status.BAD_REQUEST).entity(error).build();
				}

				boolean succeed = service.changePassword(userId, oldPassword, newPassword);
				result.put("succeed", succeed);

			} else {
				ErrorDTO error = new ErrorDTO("Unsupported action: " + action);
				return Response.status(Status.BAD_REQUEST).entity(error).build();
			}

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(result).build();
	}

}
