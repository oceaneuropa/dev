package org.orbit.component.runtime.tier1.account.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier1.account.ModelConverter;
import org.orbit.component.model.tier1.account.UserAccount;
import org.orbit.component.model.tier1.account.UserRegistryException;
import org.orbit.component.model.tier1.account.dto.UserAccountActionDTO;
import org.orbit.component.model.tier1.account.dto.UserAccountDTO;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * User registry resource.
 * 
 * {contextRoot} example: 
 * /orbit/v1/userregistry
 * 
 * User accounts: 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}/exists
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/useraccounts/action/ (Body parameter: UserAccountActionDTO)
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}
 * 
 * User account activation
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}/activated
 */
@Path("/useraccounts")
@Produces(MediaType.APPLICATION_JSON)
public class UserRegistryUserAccountsWSResource extends AbstractWSApplicationResource {

	@Inject
	public UserRegistryService service;

	protected UserRegistryService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("UserRegistryService is not available.");
		}
		return this.service;
	}

	/**
	 * Get user accounts.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserAccounts() {
		// UserRegistryService service = getService(UserRegistryService.class);
		UserRegistryService service = getService();

		List<UserAccountDTO> userAccountDTOs = new ArrayList<UserAccountDTO>();
		try {
			List<UserAccount> userAccounts = service.getUserAccounts();
			if (userAccounts != null) {
				for (UserAccount userAccount : userAccounts) {
					UserAccountDTO userAccountDTO = ModelConverter.getInstance().toDTO(userAccount);
					userAccountDTOs.add(userAccountDTO);
				}
			}
		} catch (UserRegistryException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(userAccountDTOs).build();
	}

	/**
	 * Get a user account.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}
	 * 
	 * @param userId
	 * @return
	 */
	@GET
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserAccount(@PathParam("userId") String userId) {
		UserAccountDTO userAccountDTO = null;

		UserRegistryService service = getService();
		try {
			UserAccount userAccount = service.getUserAccount(userId);
			if (userAccount == null) {
				ErrorDTO error = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("User account with userId '%s' cannot be found.", userId));
				return Response.status(Status.NOT_FOUND).entity(error).build();
			}
			userAccountDTO = ModelConverter.getInstance().toDTO(userAccount);

		} catch (UserRegistryException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(userAccountDTO).build();
	}

	/**
	 * Check whether a user account exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}/exists
	 * 
	 * @param userId
	 * @return
	 */
	@GET
	@Path("{userId}/exists")
	@Produces(MediaType.APPLICATION_JSON)
	public Response userAccountExists(@PathParam("userId") String userId) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();

		UserRegistryService service = getService();
		try {
			boolean exists = service.userAccountExists(userId);
			result.put("exists", exists);

		} catch (UserRegistryException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(result).build();
	}

	/**
	 * Register a user account
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
	 * 
	 * @param newUserAccountRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response registerUserAccount(UserAccountDTO newUserAccountRequestDTO) {
		if (newUserAccountRequestDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("registerUserAccountRequestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		boolean succeed = false;
		UserRegistryService service = getService();
		try {
			UserAccount newUserAccountRequest = ModelConverter.getInstance().toRTO(newUserAccountRequestDTO);
			UserAccount newUserAccount = service.registerUserAccount(newUserAccountRequest);
			if (newUserAccount != null) {
				succeed = true;
			}

		} catch (UserRegistryException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "User account is registered successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "User account is not registered.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Update a user account
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
	 * 
	 * @param updateUserAccountRequestDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUserAccount(UserAccountDTO updateUserAccountRequestDTO) {
		if (updateUserAccountRequestDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("updateUserAccountRequest is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		boolean succeed = false;
		UserRegistryService service = getService();
		try {
			UserAccount updateUserAccountRequest = ModelConverter.getInstance().toRTO(updateUserAccountRequestDTO);
			succeed = service.updateUserAccount(updateUserAccountRequest);

		} catch (UserRegistryException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "User account is updated successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "User account is not updated.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Check whether a user account is activated.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}/activated
	 * 
	 * @param userId
	 * @return
	 */
	@GET
	@Path("{userId}/activated")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isUserAccountActivated(@PathParam("userId") String userId) {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		UserRegistryService service = getService();
		try {
			boolean activated = service.isUserAccountActivated(userId);
			result.put("activated", activated);

		} catch (UserRegistryException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(result).build();
	}

	/**
	 * Handle user account action.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/useraccounts/action/ (Body parameter: UserAccountActionDTO)
	 * 
	 * @param userId
	 * @return
	 */
	@PUT
	@Path("action")
	@Produces(MediaType.APPLICATION_JSON)
	public Response onUserAccountAction(UserAccountActionDTO actionDTO) {
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

		} catch (UserRegistryException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(result).build();
	}

	/**
	 * Delete a user account
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}
	 * 
	 * @param userId
	 * @return
	 */
	@DELETE
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUserAccount(@PathParam("userId") String userId) {
		if (userId == null || userId.isEmpty()) {
			ErrorDTO nullAppIdError = new ErrorDTO("userId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppIdError).build();
		}

		boolean succeed = false;
		// UserRegistryService service = getService(UserRegistryService.class);
		UserRegistryService service = getService();
		try {
			succeed = service.deleteUserAccount(userId);

		} catch (UserRegistryException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "User account is deleted successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "User account is not deleted.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

}
