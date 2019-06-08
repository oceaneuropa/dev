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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.orbit.component.runtime.model.account.UserAccount;
import org.orbit.component.runtime.tier1.account.service.UserRegistryService;
import org.orbit.component.runtime.util.RuntimeModelConverter;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;

/*
 * User accounts web service resource.
 * 
 * {contextRoot}: /orbit/v1/userregistry
 * 
 * User accounts: 
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/useraccounts 
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/useraccounts/exists?type={elementType}&value={elementValue}
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
 * URL (PUT):    {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/useraccounts?accountId={accountId}
 * 
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN, OrbitRoles.USER_ACCOUNTS_ADMIN })
@Path("/useraccounts")
@Produces(MediaType.APPLICATION_JSON)
public class UserRegistryUserAccountsWSResource extends AbstractWSApplicationResource {

	@Inject
	public UserRegistryService service;

	public UserRegistryService getService() throws RuntimeException {
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
	public Response getList() {
		List<UserAccountDTO> userAccountDTOs = new ArrayList<UserAccountDTO>();
		try {
			UserRegistryService service = getService();
			List<UserAccount> userAccounts = service.getUserAccounts();
			if (userAccounts != null) {
				for (UserAccount userAccount : userAccounts) {
					UserAccountDTO userAccountDTO = RuntimeModelConverter.Account.toUserAccountDTO(userAccount);
					userAccountDTOs.add(userAccountDTO);
				}
			}
		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(userAccountDTOs).build();
	}

	/**
	 * Check whether an element (specified by type) with a specified value exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/exists?type={elementType}&value={elementValue}
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
			UserRegistryService service = getService();

			if ("accountId".equalsIgnoreCase(type)) {
				exists = service.accountIdExists(value);

			} else if ("username".equalsIgnoreCase(type)) {
				exists = service.usernameExists(value);

			} else if ("email".equalsIgnoreCase(type)) {
				exists = service.emailExists(value);

			} else {
				ErrorDTO nullError = new ErrorDTO("type '" + type + "' is not supported. Supported types are: 'accountId', 'username' and 'email'.");
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
	 * Create a user account
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
	 * 
	 * @param requestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(UserAccountDTO requestDTO) {
		if (requestDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("requestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		boolean succeed = false;
		try {
			UserRegistryService service = getService();
			UserAccount newUserAccountRequest = RuntimeModelConverter.Account.toUserAccount(requestDTO);
			UserAccount newUserAccount = service.registerUserAccount(newUserAccountRequest);
			if (newUserAccount != null) {
				succeed = true;
			}

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "User account is registered.");
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
	 * @param requestDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(UserAccountDTO requestDTO) {
		if (requestDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("requestDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		boolean succeed = false;
		UserRegistryService service = getService();
		try {
			UserAccount updateUserAccountRequest = RuntimeModelConverter.Account.toUserAccount(requestDTO);
			succeed = service.updateUserAccount(updateUserAccountRequest);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "User account is updated.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "User account is not updated.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

	/**
	 * Delete a user account
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/useraccounts?accountId={accountId}
	 * 
	 * @param accountId
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@QueryParam("accountId") String accountId) {
		boolean isAccountIdEmpty = (accountId == null || accountId.isEmpty()) ? true : false;
		if (isAccountIdEmpty) {
			ErrorDTO nullAppIdError = new ErrorDTO("accountId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullAppIdError).build();
		}

		boolean succeed = false;
		UserRegistryService service = getService();
		try {
			succeed = service.deleteUserAccount(accountId);

		} catch (ServerException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "User account is deleted.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "User account is not deleted.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

}
