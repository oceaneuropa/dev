package org.orbit.component.connector.tier1.account;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.model.tier1.account.dto.UserAccountActionDTO;
import org.orbit.component.model.tier1.account.dto.UserAccountDTO;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.util.ResponseUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * User registry resource client.
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
 * 
 */
public class UserRegistryWSClient extends AbstractWSClient {

	public static String PATH_USERACCOUNTS = "useraccounts";
	public static String PATH_ACTION = "action";

	public static String PATH_EXISTS = "exists";
	public static String ATTR_EXISTS = "exists";
	public static String PATH_ACTIVATED = "activated";
	public static String ATTR_ACTIVATED = "activated";

	/**
	 * 
	 * @param config
	 */
	public UserRegistryWSClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Get user accounts.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<UserAccountDTO> getUserAccounts() throws ClientException {
		List<UserAccountDTO> userAccounts = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path(PATH_USERACCOUNTS);
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			userAccounts = response.readEntity(new GenericType<List<UserAccountDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (userAccounts == null) {
			userAccounts = Collections.emptyList();
		}
		return userAccounts;
	}

	/**
	 * Get a user account.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}
	 * 
	 * @param userId
	 * @return
	 * @throws ClientException
	 */
	public UserAccountDTO getUserAccount(String userId) throws ClientException {
		UserAccountDTO userAccount = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_USERACCOUNTS).path(userId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			userAccount = response.readEntity(UserAccountDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return userAccount;
	}

	/**
	 * Check whether a user account exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}/exists
	 * 
	 * @param userId
	 * @return
	 * @throws ClientException
	 */
	public boolean userAccountExists(String userId) throws ClientException {
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_USERACCOUNTS).path(userId).path(PATH_EXISTS).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			String responseString = response.readEntity(String.class);

			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey(ATTR_EXISTS)) {
				Object value = result.get(ATTR_EXISTS);
				if (value instanceof Boolean) {
					return (boolean) value;
				}
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return false;
	}

	/**
	 * Register a user account.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
	 * 
	 * @param newUserAccountRequestDTO
	 *            Body parameter for registering a user account.
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO registerUserAccount(UserAccountDTO newUserAccountRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_USERACCOUNTS).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<UserAccountDTO>(newUserAccountRequestDTO) {
			}));
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Update a user account.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/useraccounts (Body parameter: UserAccountDTO)
	 * 
	 * @param updateUserAccountRequestDTO
	 *            Body parameter for updating a user account.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO updateUserAccount(UserAccountDTO updateUserAccountRequestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Entity<?> bodyParameter = Entity.json(new GenericEntity<UserAccountDTO>(updateUserAccountRequestDTO) {
			});

			Builder builder = getRootPath().path(PATH_USERACCOUNTS).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(bodyParameter);
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * 
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws ClientException
	 */
	public boolean changePassword(String userId, String oldPassword, String newPassword) throws ClientException {
		Response response = null;
		try {
			UserAccountActionDTO actionDTO = new UserAccountActionDTO();
			actionDTO.setUserId(userId);
			actionDTO.setAction("change_password");
			Map<Object, Object> args = new HashMap<Object, Object>();
			args.put("oldpassword", oldPassword);
			args.put("newpassword", newPassword);
			actionDTO.setArgs(args);

			Entity<?> bodyParameter = Entity.json(new GenericEntity<UserAccountActionDTO>(actionDTO) {
			});

			Builder builder = getRootPath().path(PATH_USERACCOUNTS).path(PATH_ACTION).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(bodyParameter);
			checkResponse(response);

			String responseString = response.readEntity(String.class);

			boolean succeed = false;
			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey("succeed")) {
				Object value = result.get("succeed");
				if (value instanceof Boolean) {
					succeed = (boolean) value;
				}
			}

			if (succeed) {
				return true;
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return false;
	}

	/**
	 * Check whether a user account is activated.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}/activated
	 * 
	 * @param userId
	 * @return
	 * @throws ClientException
	 */
	public boolean isUserAccountActivated(String userId) throws ClientException {
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_USERACCOUNTS).path(userId).path(PATH_ACTIVATED).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			String responseString = response.readEntity(String.class);

			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey(ATTR_ACTIVATED)) {
				Object value = result.get(ATTR_ACTIVATED);
				if (value instanceof Boolean) {
					return (boolean) value;
				}
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return false;
	}

	/**
	 * Activate a user account.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/action (Body parameter: UserAccountActionDTO)
	 * 
	 * @param userId
	 * @return
	 * @throws ClientException
	 */
	public boolean activateUserAccount(String userId) throws ClientException {
		Response response = null;
		try {
			UserAccountActionDTO actionDTO = new UserAccountActionDTO();
			actionDTO.setUserId(userId);
			actionDTO.setAction("activate");
			Map<Object, Object> args = new HashMap<Object, Object>();
			args.put("key1", "v1");
			args.put("key2", true);
			args.put("key3", new Integer(12345));
			actionDTO.setArgs(args);

			Entity<?> bodyParameter = Entity.json(new GenericEntity<UserAccountActionDTO>(actionDTO) {
			});

			Builder builder = getRootPath().path(PATH_USERACCOUNTS).path(PATH_ACTION).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(bodyParameter);
			checkResponse(response);

			String responseString = response.readEntity(String.class);

			boolean isActivated = false;
			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey(ATTR_ACTIVATED)) {
				Object value = result.get(ATTR_ACTIVATED);
				if (value instanceof Boolean) {
					isActivated = (boolean) value;
				}
			}

			if (isActivated) {
				return true;
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return false;
	}

	/**
	 * Deactivate a user account.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/action (Body parameter: UserAccountActionDTO)
	 * 
	 * @param userId
	 * @return
	 * @throws ClientException
	 */
	public boolean deactivateUserAccount(String userId) throws ClientException {
		Response response = null;
		try {
			UserAccountActionDTO actionDTO = new UserAccountActionDTO();
			actionDTO.setUserId(userId);
			actionDTO.setAction("deactivate");
			Map<Object, Object> args = new HashMap<Object, Object>();
			args.put("key4", "v2");
			args.put("key5", false);
			args.put("key6", new Integer(23456));
			actionDTO.setArgs(args);

			Entity<?> bodyParameter = Entity.json(new GenericEntity<UserAccountActionDTO>(actionDTO) {
			});

			Builder builder = getRootPath().path(PATH_USERACCOUNTS).path(PATH_ACTION).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(bodyParameter);
			checkResponse(response);

			String responseString = response.readEntity(String.class);

			boolean isActivated = false;
			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey(ATTR_ACTIVATED)) {
				Object value = result.get(ATTR_ACTIVATED);
				if (value instanceof Boolean) {
					isActivated = (boolean) value;
				}
			}

			if (!isActivated) {
				return true;
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return false;
	}

	/**
	 * Delete a user account.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/useraccounts/{userId}
	 * 
	 * @param userId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO deleteUserAccount(String userId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Builder builder = getRootPath().path(PATH_USERACCOUNTS).path(userId).request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(response);

			status = response.readEntity(StatusDTO.class);
		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

}
