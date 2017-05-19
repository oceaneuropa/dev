package org.orbit.component.server.tier1.account.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.orbit.component.model.tier1.account.UserAccountRTO;
import org.orbit.component.model.tier1.account.UserRegistryException;
import org.orbit.component.server.tier1.account.service.UserRegistryService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

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
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}
 * 
 * User account activation
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}/activated
 */
@Path("/useraccounts")
@Produces(MediaType.APPLICATION_JSON)
public class UserRegistryUserAccountsResource extends AbstractApplicationResource {

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
		UserRegistryService userRegistryService = getService(UserRegistryService.class);

		List<UserAccountDTO> userAccountDTOs = new ArrayList<UserAccountDTO>();
		try {
			List<UserAccountRTO> userAccounts = userRegistryService.getUserAccounts();
			if (userAccounts != null) {
				for (UserAccountRTO userAccount : userAccounts) {
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

		UserRegistryService userRegistryService = getService(UserRegistryService.class);
		try {
			UserAccountRTO userAccount = userRegistryService.getUserAccount(userId);
			if (userAccount == null) {
				ErrorDTO userAccountNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), String.format("User account with userId '%s' cannot be found.", userId));
				return Response.status(Status.NOT_FOUND).entity(userAccountNotFoundError).build();
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
		UserRegistryService userRegistryService = getService(UserRegistryService.class);
		try {
			boolean exists = userRegistryService.userAccountExists(userId);
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
		UserRegistryService userRegistryService = getService(UserRegistryService.class);
		try {
			UserAccountRTO newUserAccountRequest = ModelConverter.getInstance().toRTO(newUserAccountRequestDTO);
			UserAccountRTO newUserAccount = userRegistryService.registerUserAccount(newUserAccountRequest);
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
		UserRegistryService userRegistryService = getService(UserRegistryService.class);
		try {
			UserAccountRTO updateUserAccountRequest = ModelConverter.getInstance().toRTO(updateUserAccountRequestDTO);
			succeed = userRegistryService.updateUserAccount(updateUserAccountRequest);

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
		UserRegistryService userRegistryService = getService(UserRegistryService.class);
		try {
			succeed = userRegistryService.deleteUserAccount(userId);

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
		UserRegistryService userRegistryService = getService(UserRegistryService.class);
		try {
			boolean activated = userRegistryService.isUserAccountActivated(userId);
			result.put("activated", activated);

		} catch (UserRegistryException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(result).build();
	}

}
