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

import org.orbit.component.model.tier1.account.UserAccountActionDTO;
import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.util.ResponseUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * User registry web service client.
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
 * User account: 
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/useraccount?accountId={accountId}
 * URL (GET):    {scheme}://{host}:{port}/{contextRoot}/useraccount/activated?accountId={accountId}
 * URL (POST):   {scheme}://{host}:{port}/{contextRoot}/useraccount/action (Body parameter: UserAccountActionDTO)
 * 
 */
public class UserAccountWSClient extends WSClient {

	/**
	 * 
	 * @param config
	 */
	public UserAccountWSClient(WSClientConfiguration config) {
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
	public List<UserAccountDTO> getList() throws ClientException {
		List<UserAccountDTO> userAccounts = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("useraccounts");

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

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
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccount?accountId={accountId}
	 * 
	 * @param accountId
	 * @return
	 * @throws ClientException
	 */
	public UserAccountDTO get(String accountId) throws ClientException {
		UserAccountDTO userAccount = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("useraccount");
			target = target.queryParam("accountId", accountId);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			userAccount = response.readEntity(UserAccountDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return userAccount;
	}

	/**
	 * Check whether an element (specified by type) with a specified value exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccounts/exists?type={elementType}&value={elementValue}
	 * 
	 * @param elementType
	 * @param elementValue
	 * @return
	 * @throws ClientException
	 */
	public boolean exists(String elementType, String elementValue) throws ClientException {
		Response response = null;
		try {
			WebTarget target = getRootPath().path("useraccounts").path("exists");
			target = target.queryParam("type", elementType);
			target = target.queryParam("value", elementValue);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			String responseString = response.readEntity(String.class);

			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey("exists")) { //$NON-NLS-1$
				Object value = result.get("exists"); //$NON-NLS-1$
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
	 * @param requestDTO
	 *            Body parameter for registering a user account.
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO create(UserAccountDTO requestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("useraccounts");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<UserAccountDTO>(requestDTO) {
			}));
			checkResponse(target, response);

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
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/useraccount (Body parameter: UserAccountDTO)
	 * 
	 * @param requestDTO
	 *            Body parameter for updating a user account.
	 * 
	 * @return Update status
	 * @throws ClientException
	 */
	public StatusDTO update(UserAccountDTO requestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			Entity<?> bodyParameter = Entity.json(new GenericEntity<UserAccountDTO>(requestDTO) {
			});

			WebTarget target = getRootPath().path("useraccounts");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).put(bodyParameter);
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * Change password.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/useraccount/action (Body parameter: UserAccountActionDTO)
	 * 
	 * @param accountId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 * @throws ClientException
	 */
	public boolean changePassword(String accountId, String oldPassword, String newPassword) throws ClientException {
		Response response = null;
		try {
			UserAccountActionDTO actionDTO = new UserAccountActionDTO();
			actionDTO.setAccountId(accountId);
			actionDTO.setAction("change_password");
			Map<Object, Object> args = new HashMap<Object, Object>();
			args.put("oldpassword", oldPassword);
			args.put("newpassword", newPassword);
			actionDTO.setArgs(args);

			Entity<?> bodyParameter = Entity.json(new GenericEntity<UserAccountActionDTO>(actionDTO) {
			});

			WebTarget target = getRootPath().path("useraccount").path("action");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(bodyParameter);
			checkResponse(target, response);

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
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/useraccount/activated?accountId={accountId}
	 * 
	 * @param accountId
	 * @return
	 * @throws ClientException
	 */
	public boolean isActivated(String accountId) throws ClientException {
		Response response = null;
		try {
			WebTarget target = getRootPath().path("activated");
			target = target.queryParam("accountId", accountId);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			String responseString = response.readEntity(String.class);

			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey("activated")) {
				Object value = result.get("activated");
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
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/useraccount/action (Body parameter: UserAccountActionDTO)
	 * 
	 * @param accountId
	 * @return
	 * @throws ClientException
	 */
	public boolean activate(String accountId) throws ClientException {
		Response response = null;
		try {
			UserAccountActionDTO actionDTO = new UserAccountActionDTO();
			actionDTO.setAccountId(accountId);
			actionDTO.setAction("activate");
			Map<Object, Object> args = new HashMap<Object, Object>();
			args.put("key1", "v1");
			args.put("key2", true);
			args.put("key3", new Integer(12345));
			actionDTO.setArgs(args);

			Entity<?> bodyParameter = Entity.json(new GenericEntity<UserAccountActionDTO>(actionDTO) {
			});

			WebTarget target = getRootPath().path("useraccount").path("action");

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(bodyParameter);
			checkResponse(target, response);

			String responseString = response.readEntity(String.class);

			boolean isActivated = false;
			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey("activated")) {
				Object value = result.get("activated");
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
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/useraccount/action (Body parameter: UserAccountActionDTO)
	 * 
	 * @param accountId
	 * @return
	 * @throws ClientException
	 */
	public boolean deactivate(String accountId) throws ClientException {
		Response response = null;
		try {
			UserAccountActionDTO actionDTO = new UserAccountActionDTO();
			actionDTO.setAccountId(accountId);
			actionDTO.setAction("deactivate");
			Map<Object, Object> args = new HashMap<Object, Object>();
			args.put("key4", "v2");
			args.put("key5", false);
			args.put("key6", new Integer(23456));
			actionDTO.setArgs(args);

			Entity<?> bodyParameter = Entity.json(new GenericEntity<UserAccountActionDTO>(actionDTO) {
			});

			WebTarget target = getRootPath().path("useraccount").path("action");

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(bodyParameter);
			checkResponse(target, response);

			String responseString = response.readEntity(String.class);

			boolean isActivated = false;
			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey("activated")) {
				Object value = result.get("activated");
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
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/useraccounts?accountId={accountId}
	 * 
	 * @param accountId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO delete(String accountId) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("useraccounts");
			target = target.queryParam("accountId", accountId);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).delete();
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

}
