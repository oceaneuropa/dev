package org.orbit.component.connector.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.orbit.component.api.tier1.account.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.UpdateUserAccountRequest;
import org.orbit.component.api.tier1.identity.LoginRequest;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.tier1.identity.LogoutRequest;
import org.orbit.component.api.tier1.identity.RegisterRequest;
import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppQuery;
import org.orbit.component.api.tier2.appstore.CreateAppRequest;
import org.orbit.component.api.tier2.appstore.UpdateAppRequest;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.NodeConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.api.tier4.missioncontrol.Mission;
import org.orbit.component.connector.tier1.account.UserAccountImpl;
import org.orbit.component.connector.tier2.appstore.AppManifestImpl;
import org.orbit.component.connector.tier3.domain.MachineConfigImpl;
import org.orbit.component.connector.tier3.domain.NodeConfigImpl;
import org.orbit.component.connector.tier3.domain.PlatformConfigImpl;
import org.orbit.component.connector.tier3.nodecontrol.NodeInfoImpl;
import org.orbit.component.connector.tier4.missioncontrol.MissionImpl;
import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationRequestDTO;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.AuthorizationResponseDTO;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenRequestDTO;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.model.tier1.auth.TokenResponseDTO;
import org.orbit.component.model.tier1.identity.LoginRequestDTO;
import org.orbit.component.model.tier1.identity.LoginResponseDTO;
import org.orbit.component.model.tier1.identity.LogoutRequestDTO;
import org.orbit.component.model.tier1.identity.RegisterRequestDTO;
import org.orbit.component.model.tier2.appstore.AppManifestDTO;
import org.orbit.component.model.tier2.appstore.AppQueryDTO;
import org.orbit.component.model.tier3.domain.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.AddNodeConfigRequest;
import org.orbit.component.model.tier3.domain.AddPlatformConfigRequest;
import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.model.tier3.domain.PlatformConfigDTO;
import org.orbit.component.model.tier3.domain.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.UpdateNodeConfigRequest;
import org.orbit.component.model.tier3.domain.UpdatePlatformConfigRequest;
import org.orbit.component.model.tier3.nodecontrol.NodeDTO;
import org.orbit.component.model.tier4.missioncontrol.MissionDTO;
import org.origin.common.jdbc.SQLWhereOperator;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

public class ModelConverter {

	public static Account Account = new Account();
	public static Identity Identity = new Identity();
	public static Auth Auth = new Auth();
	public static AppStore AppStore = new AppStore();
	public static Domain Domain = new Domain();
	public static NodeControl NodeControl = new NodeControl();
	public static MissionControl MissionControl = new MissionControl();

	public static class Account {
		/**
		 * Convert CreateUserAccountRequest to DTO.
		 * 
		 * @param createUserAccountRequest
		 * @return
		 */
		public UserAccountDTO toDTO(CreateUserAccountRequest createUserAccountRequest) {
			UserAccountDTO requestDTO = new UserAccountDTO();

			requestDTO.setUsername(createUserAccountRequest.getUsername());
			requestDTO.setPassword(createUserAccountRequest.getPassword());
			requestDTO.setEmail(createUserAccountRequest.getEmail());
			requestDTO.setFirstName(createUserAccountRequest.getFirstName());
			requestDTO.setLastName(createUserAccountRequest.getLastName());
			requestDTO.setPhone(createUserAccountRequest.getPhone());

			return requestDTO;
		}

		/**
		 * Convert UpdateUserAccountRequest to DTO.
		 * 
		 * @param updateUserAccountRequest
		 * @return
		 */
		public UserAccountDTO toDTO(UpdateUserAccountRequest updateUserAccountRequest) {
			UserAccountDTO requestDTO = new UserAccountDTO();

			requestDTO.setAccountId(updateUserAccountRequest.getAccountId());
			requestDTO.setUsername(updateUserAccountRequest.getUsername());
			requestDTO.setEmail(updateUserAccountRequest.getEmail());
			requestDTO.setPassword(updateUserAccountRequest.getPassword());
			requestDTO.setFirstName(updateUserAccountRequest.getFirstName());
			requestDTO.setLastName(updateUserAccountRequest.getLastName());
			requestDTO.setPhone(updateUserAccountRequest.getPhone());

			return requestDTO;
		}

		/**
		 * Convert DTO to UserAccountImpl.
		 * 
		 * @param userAccountDTO
		 * @return
		 */
		public UserAccountImpl toUserAccountImpl(UserAccountDTO userAccountDTO) {
			UserAccountImpl userAccount = new UserAccountImpl();

			userAccount.setAccountId(userAccountDTO.getAccountId());
			userAccount.setUsername(userAccountDTO.getUsername());
			userAccount.setEmail(userAccountDTO.getEmail());
			userAccount.setPassword(userAccountDTO.getPassword());
			userAccount.setFirstName(userAccountDTO.getFirstName());
			userAccount.setLastName(userAccountDTO.getLastName());
			userAccount.setPhone(userAccountDTO.getPhone());
			userAccount.setCreationTime(userAccountDTO.getCreationTime());
			userAccount.setLastUpdateTime(userAccountDTO.getLastUpdateTime());
			userAccount.setActivated(userAccountDTO.isActivated());

			return userAccount;
		}
	}

	public static class Identity {

		public RegisterRequestDTO toRequestDTO(RegisterRequest request) {
			if (request == null) {
				return null;
			}

			String username = request.getUsername();
			String email = request.getEmail();
			String password = request.getPassword();

			RegisterRequestDTO requestDTO = new RegisterRequestDTO();
			requestDTO.setUsername(username);
			requestDTO.setEmail(email);
			requestDTO.setPassword(password);

			return requestDTO;
		}

		public LoginRequestDTO toRequestDTO(LoginRequest request) {
			if (request == null) {
				return null;
			}

			String clientId = request.getClientId();
			String grantType = request.getGrantType();
			String username = request.getUsername();
			String email = request.getEmail();
			String password = request.getPassword();

			LoginRequestDTO requestDTO = new LoginRequestDTO();
			requestDTO.setClientId(clientId);
			requestDTO.setGrantType(grantType);
			requestDTO.setUsername(username);
			requestDTO.setEmail(email);
			requestDTO.setPassword(password);

			return requestDTO;
		}

		public LogoutRequestDTO toRequestDTO(LogoutRequest request) {
			if (request == null) {
				return null;
			}

			String tokenType = request.getTokenType();
			String tokenValue = request.getTokenValue();

			LogoutRequestDTO requestDTO = new LogoutRequestDTO();
			requestDTO.setTokenType(tokenType);
			requestDTO.setTokenValue(tokenValue);

			return requestDTO;
		}

		public LoginResponse toResponse(LoginResponseDTO responseDTO) {
			if (responseDTO == null) {
				return null;
			}

			boolean succeed = responseDTO.isSucceed();
			String message = responseDTO.getMessage();
			String tokenType = responseDTO.getTokenType();
			String tokenValue = responseDTO.getTokenValue();

			LoginResponse response = new LoginResponse();
			response.setSucceed(succeed);
			response.setMessage(message);
			response.setTokenType(tokenType);
			response.setTokenValue(tokenValue);

			return response;
		}
	}

	public static class Auth {

		// ----------------------------------------------------------------------
		// Use on client side
		// ----------------------------------------------------------------------
		/**
		 * Convert AuthorizationRequest object to AuthorizationRequestDTO object.
		 * 
		 * @param authorizationRequest
		 * @return
		 */
		public AuthorizationRequestDTO toRequestDTO(AuthorizationRequest authorizationRequest) {
			String client_id = authorizationRequest.getClient_id();
			String response_type = authorizationRequest.getResponse_type();
			String redirect_url = authorizationRequest.getRedirect_url();
			String scope = authorizationRequest.getScope();
			String state = authorizationRequest.getState();

			AuthorizationRequestDTO requestDTO = new AuthorizationRequestDTO();
			requestDTO.setClient_id(client_id);
			requestDTO.setResponse_type(response_type);
			requestDTO.setRedirect_url(redirect_url);
			requestDTO.setScope(scope);
			requestDTO.setState(state);

			return requestDTO;
		}

		/**
		 * Convert AuthorizationResponseDTO object to AuthorizationResponse object.
		 * 
		 * @param authorizationResponseDTO
		 * @return
		 */
		public AuthorizationResponse toResponseDTO(AuthorizationResponseDTO authorizationResponseDTO) {
			String client_id = authorizationResponseDTO.getClient_id();
			String response_type = authorizationResponseDTO.getResponse_type();
			String redirect_url = authorizationResponseDTO.getRedirect_url();
			String scope = authorizationResponseDTO.getScope();
			String state = authorizationResponseDTO.getState();

			AuthorizationResponse response = new AuthorizationResponse();
			response.setClient_id(client_id);
			response.setResponse_type(response_type);
			response.setRedirect_url(redirect_url);
			response.setScope(scope);
			response.setState(state);

			return response;
		}

		/**
		 * Convert TokenRequest object to TokenRequestDTO object.
		 * 
		 * @param tokenRequest
		 * @return
		 */
		public TokenRequestDTO toRequestDTO(TokenRequest tokenRequest) {
			String grant_type = tokenRequest.getGrantType();
			String client_id = tokenRequest.getClientId();
			String client_secret = tokenRequest.getClient_secret();
			String username = tokenRequest.getUsername();
			String password = tokenRequest.getPassword();
			String refresh_token = tokenRequest.getRefreshToken();
			String scope = tokenRequest.getScope();
			String state = tokenRequest.getState();

			TokenRequestDTO requestDTO = new TokenRequestDTO();
			requestDTO.setGrant_type(grant_type);
			requestDTO.setClient_id(client_id);
			requestDTO.setClient_secret(client_secret);
			requestDTO.setUsername(username);
			requestDTO.setPassword(password);
			requestDTO.setRefresh_token(refresh_token);
			requestDTO.setScope(scope);
			requestDTO.setState(state);

			return requestDTO;
		}

		/**
		 * Convert TokenResponseDTO object to TokenResponse object.
		 * 
		 * @param tokenResponseDTO
		 * @return
		 */
		public TokenResponse toResponseDTO(TokenResponseDTO tokenResponseDTO) {
			String token_type = tokenResponseDTO.getToken_type();
			String access_token = tokenResponseDTO.getAccess_token();
			long expires_in = tokenResponseDTO.getExpires_in();
			String refresh_token = tokenResponseDTO.getRefresh_token();
			String scope = tokenResponseDTO.getScope();
			String state = tokenResponseDTO.getState();

			TokenResponse response = new TokenResponse();
			response.setTokenType(token_type);
			response.setAccessToken(access_token);
			response.setExpiresIn(expires_in);
			response.setRefreshToken(refresh_token);
			response.setScope(scope);
			response.setState(state);

			return response;
		}

		// ----------------------------------------------------------------------
		// Use on server side
		// ----------------------------------------------------------------------
		// /**
		// * Convert ServerException object to ErrorResponseDTO object.
		// *
		// * @param e
		// * @return
		// */
		// public ErrorResponseDTO toResponseDTO(ServerException e) {
		// String error = e.getError();
		// String error_description = e.getError_description();
		// String error_url = e.getError_url();
		//
		// ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
		// errorResponseDTO.setError(error);
		// errorResponseDTO.setError_description(error_description);
		// errorResponseDTO.setError_uri(error_url);
		//
		// return errorResponseDTO;
		// }

		/**
		 * Convert AuthorizationRequestDTO object to AuthorizationRequest object.
		 * 
		 * @param authorizationRequestDTO
		 * @return
		 */
		public AuthorizationRequest toRequest(AuthorizationRequestDTO authorizationRequestDTO) {
			String client_id = authorizationRequestDTO.getClient_id();
			String response_type = authorizationRequestDTO.getResponse_type();
			String redirect_url = authorizationRequestDTO.getRedirect_url();
			String scope = authorizationRequestDTO.getScope();
			String state = authorizationRequestDTO.getState();

			AuthorizationRequest request = new AuthorizationRequest();
			request.setClient_id(client_id);
			request.setResponse_type(response_type);
			request.setRedirect_url(redirect_url);
			request.setScope(scope);
			request.setState(state);

			return request;
		}

		/**
		 * Convert AuthorizationResponse object to AuthorizationResponseDTO object.
		 * 
		 * @param authorizationResponse
		 * @return
		 */
		public AuthorizationResponseDTO toResponseDTO(AuthorizationResponse authorizationResponse) {
			String client_id = authorizationResponse.getClient_id();
			String response_type = authorizationResponse.getResponse_type();
			String redirect_url = authorizationResponse.getRedirect_url();
			String scope = authorizationResponse.getScope();
			String state = authorizationResponse.getState();

			AuthorizationResponseDTO responseDTO = new AuthorizationResponseDTO();
			responseDTO.setClient_id(client_id);
			responseDTO.setResponse_type(response_type);
			responseDTO.setRedirect_url(redirect_url);
			responseDTO.setScope(scope);
			responseDTO.setState(state);

			return responseDTO;
		}

		/**
		 * Convert TokenRequestDTO object to TokenRequest object.
		 * 
		 * @param tokenRequestDTO
		 * @return
		 */
		public TokenRequest toRequest(TokenRequestDTO tokenRequestDTO) {
			String grant_type = tokenRequestDTO.getGrant_type();
			String client_id = tokenRequestDTO.getClient_id();
			String client_secret = tokenRequestDTO.getClient_secret();
			String username = tokenRequestDTO.getUsername();
			String password = tokenRequestDTO.getPassword();
			String refresh_token = tokenRequestDTO.getRefresh_token();
			String scope = tokenRequestDTO.getScope();
			String state = tokenRequestDTO.getState();

			TokenRequest request = new TokenRequest();
			request.setGrantType(grant_type);
			request.setClientId(client_id);
			request.setClientSecret(client_secret);
			request.setUsername(username);
			request.setPassword(password);
			request.setRefreshToken(refresh_token);
			request.setScope(scope);
			request.setState(state);

			return request;
		}

		/**
		 * Convert TokenResponse object to TokenResponseDTO object.
		 * 
		 * @param tokenResponse
		 * @return
		 */
		public TokenResponseDTO toResponseDTO(TokenResponse tokenResponse) {
			String token_type = tokenResponse.getTokenType();
			String access_token = tokenResponse.getAccessToken();
			long expires_in = tokenResponse.getExpiresIn();
			String refresh_token = tokenResponse.getRefreshToken();
			String scope = tokenResponse.getScope();
			String state = tokenResponse.getState();

			TokenResponseDTO responseDTO = new TokenResponseDTO();
			responseDTO.setToken_type(token_type);
			responseDTO.setAccess_token(access_token);
			responseDTO.setExpires_in(expires_in);
			responseDTO.setRefresh_token(refresh_token);
			responseDTO.setScope(scope);
			responseDTO.setState(state);

			return responseDTO;
		}
	}

	public static class AppStore {
		/**
		 * Convert DTO to AppManifestImpl.
		 * 
		 * @param appDTO
		 */
		public AppManifest toApp(AppManifestDTO appDTO) {
			AppManifestImpl app = new AppManifestImpl();
			app.setId(appDTO.getId());
			app.setAppId(appDTO.getAppId());
			app.setAppVersion(appDTO.getAppVersion());
			app.setType(appDTO.getType());
			app.setName(appDTO.getName());
			app.setManifest(appDTO.getAppManifest());
			app.setFileName(appDTO.getAppFileName());
			app.setFileLength(appDTO.getAppFileLength());
			app.setDescription(appDTO.getDescription());
			app.setDateCreated(new Date(appDTO.getDateCreated()));
			app.setDateModified(new Date(appDTO.getDateModified()));
			return app;
		}

		/**
		 * Convert CreateAppRequest to DTO.
		 * 
		 * @param request
		 * @return
		 */
		public AppManifestDTO toDTO(CreateAppRequest request) {
			AppManifestDTO requestDTO = new AppManifestDTO();
			requestDTO.setAppId(request.getAppId());
			requestDTO.setAppVersion(request.getAppVersion());
			requestDTO.setType(request.getType());
			requestDTO.setName(request.getName());
			requestDTO.setAppManifest(request.getManifest());
			requestDTO.setAppFileName(request.getFileName());
			requestDTO.setDescription(request.getDescription());

			return requestDTO;
		}

		/**
		 * Convert UpdateAppRequest to DTO.
		 * 
		 * @param request
		 * @return
		 */
		public AppManifestDTO toDTO(UpdateAppRequest request) {
			AppManifestDTO requestDTO = new AppManifestDTO();
			requestDTO.setId(request.getId());
			requestDTO.setAppId(request.getAppId());
			requestDTO.setAppVersion(request.getAppVersion());
			requestDTO.setType(request.getType());
			requestDTO.setName(request.getName());
			requestDTO.setAppManifest(request.getManifest());
			requestDTO.setAppFileName(request.getFileName());
			requestDTO.setDescription(request.getDescription());

			return requestDTO;
		}

		/**
		 * Convert AppQuery to DTO.
		 * 
		 * @param query
		 */
		public AppQueryDTO toDTO(AppQuery query) {
			AppQueryDTO queryDTO = new AppQueryDTO();
			queryDTO.setAppId(query.getAppId());
			queryDTO.setAppVersion(query.getAppVersion());
			queryDTO.setType(query.getType());
			queryDTO.setName(query.getName());
			queryDTO.setDescription(query.getDescription());

			queryDTO.setAppId_oper(SQLWhereOperator.isEqual(query.getAppId_oper()) ? null : query.getAppId_oper());
			queryDTO.setAppVersion_oper(SQLWhereOperator.isEqual(query.getAppVersion_oper()) ? null : query.getAppVersion_oper());
			queryDTO.setName_oper(SQLWhereOperator.isEqual(query.getName_oper()) ? null : query.getName_oper());
			queryDTO.setType_oper(SQLWhereOperator.isEqual(query.getType_oper()) ? null : query.getType_oper());
			queryDTO.setDescription_oper(SQLWhereOperator.isEqual(query.getDescription_oper()) ? null : query.getDescription_oper());

			return queryDTO;
		}
	}

	public static class Domain {

		public MachineConfig[] convertToMachineConfigs(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			// Responses responses = ResponseUtil.parseResponses(response);

			List<MachineConfig> machineConfigs = new ArrayList<MachineConfig>();
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// Object bodyObj = response.getBody();
			// if (bodyObj instanceof List) {
			// List<?> elements = (List<?>) bodyObj;
			// for (Object element : elements) {
			// if (element instanceof Map<?, ?>) {
			// Map<?, ?> map = (Map<?, ?>) element;
			// MachineConfig machine = DomainManagementConverter.INSTANCE.toMachineConfig(map);
			// if (machine != null) {
			// machineConfigs.add(machine);
			// }
			// }
			// }
			// }
			// }
			return machineConfigs.toArray(new MachineConfig[machineConfigs.size()]);
		}

		public MachineConfig convertToMachineConfig(Response response) {
			MachineConfig machineConfig = null;
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// Object bodyObj = response.getBody();
			// if (bodyObj instanceof Map<?, ?>) {
			// Map<?, ?> map = (Map<?, ?>) bodyObj;
			// machineConfig = DomainManagementConverter.INSTANCE.toMachineConfig(map);
			// }
			// }
			return machineConfig;
		}

		public PlatformConfig[] convertToPlatformConfigs(Response response) {
			List<PlatformConfig> platformConfigs = new ArrayList<PlatformConfig>();
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// Object bodyObj = response.getBody();
			// if (bodyObj instanceof List) {
			// List<?> elements = (List<?>) bodyObj;
			// for (Object element : elements) {
			// if (element instanceof Map<?, ?>) {
			// Map<?, ?> map = (Map<?, ?>) element;
			// TransferAgentConfig ta = DomainManagementConverter.INSTANCE.toTransferAgentConfig(map);
			// if (ta != null) {
			// taConfigs.add(ta);
			// }
			// }
			// }
			// }
			// }
			return platformConfigs.toArray(new PlatformConfig[platformConfigs.size()]);
		}

		public PlatformConfig convertToPlatformConfig(Response response) {
			PlatformConfig taConfig = null;
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// Object bodyObj = response.getBody();
			// if (bodyObj instanceof Map<?, ?>) {
			// Map<?, ?> map = (Map<?, ?>) bodyObj;
			// taConfig = DomainManagementConverter.INSTANCE.toTransferAgentConfig(map);
			// }
			// }
			return taConfig;
		}

		public NodeConfig[] convertToNodeConfigs(Response response) {
			List<NodeConfig> nodeConfigs = new ArrayList<NodeConfig>();
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// Object bodyObj = response.getBody();
			// if (bodyObj instanceof List) {
			// List<?> elements = (List<?>) bodyObj;
			// for (Object element : elements) {
			// if (element instanceof Map<?, ?>) {
			// Map<?, ?> map = (Map<?, ?>) element;
			// NodeConfig nodeConfig = DomainManagementConverter.INSTANCE.toNodeConfig(map);
			// if (nodeConfig != null) {
			// nodeConfigs.add(nodeConfig);
			// }
			// }
			// }
			// }
			// }
			return nodeConfigs.toArray(new NodeConfig[nodeConfigs.size()]);
		}

		public NodeConfig convertToNodeConfig(Response response) {
			NodeConfig nodeConfig = null;
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// Object bodyObj = response.getBody();
			// if (bodyObj instanceof Map<?, ?>) {
			// Map<?, ?> map = (Map<?, ?>) bodyObj;
			// nodeConfig = DomainManagementConverter.INSTANCE.toNodeConfig(map);
			// }
			// }
			return nodeConfig;
		}

		/**
		 * 
		 * @param machineDTO
		 * @return
		 */
		public MachineConfig toMachineConfig(MachineConfigDTO machineDTO) {
			MachineConfigImpl impl = new MachineConfigImpl();
			impl.setId(machineDTO.getId());
			impl.setName(machineDTO.getName());
			impl.setIpAddress(machineDTO.getIpAddress());
			return impl;
		}

		/**
		 * 
		 * @param map
		 * @return
		 */
		public MachineConfig toMachineConfig(Map<?, ?> map) {
			String machineId = (String) map.get("id");
			String machineName = (String) map.get("name");
			String ipAddress = (String) map.get("ipAddress");

			MachineConfigImpl impl = new MachineConfigImpl();
			impl.setId(machineId);
			impl.setName(machineName);
			impl.setIpAddress(ipAddress);
			return impl;
		}

		/**
		 * 
		 * @param machineId
		 * @param platformConfigDTO
		 * @return
		 */
		public PlatformConfig toPlatformConfig(String machineId, PlatformConfigDTO platformConfigDTO) {
			PlatformConfigImpl impl = new PlatformConfigImpl();
			impl.setMachineId(machineId);
			impl.setId(platformConfigDTO.getId());
			impl.setName(platformConfigDTO.getName());
			impl.setHome(platformConfigDTO.getHome());
			impl.setHostURL(platformConfigDTO.getHostURL());
			impl.setContextRoot(platformConfigDTO.getContextRoot());
			return impl;
		}

		/**
		 * 
		 * @param map
		 * @return
		 */
		public PlatformConfig toPlatformConfig(Map<?, ?> map) {
			String machineId = (String) map.get("machineId");
			String id = (String) map.get("id");
			String name = (String) map.get("name");
			String home = (String) map.get("home");
			String hostURL = (String) map.get("hostURL");
			String contextRoot = (String) map.get("contextRoot");

			PlatformConfigImpl impl = new PlatformConfigImpl();
			impl.setMachineId(machineId);
			impl.setId(id);
			impl.setName(name);
			impl.setHome(home);
			impl.setHostURL(hostURL);
			impl.setContextRoot(contextRoot);

			return impl;
		}

		/**
		 * 
		 * @param machineId
		 * @param platformId
		 * @param nodeConfigDTO
		 * @return
		 */
		public NodeConfig toNodeConfig(String machineId, String platformId, NodeConfigDTO nodeConfigDTO) {
			NodeConfigImpl impl = new NodeConfigImpl();
			impl.setMachineId(machineId);
			impl.setPlatformId(platformId);
			impl.setId(nodeConfigDTO.getId());
			impl.setName(nodeConfigDTO.getName());
			impl.setHome(nodeConfigDTO.getHome());
			impl.setHostURL(nodeConfigDTO.getHostURL());
			impl.setContextRoot(nodeConfigDTO.getContextRoot());
			return impl;
		}

		/**
		 * 
		 * @param map
		 * @return
		 */
		public NodeConfig toNodeConfig(Map<?, ?> map) {
			String machineId = (String) map.get("machineId");
			String platformId = (String) map.get("platformId");
			String id = (String) map.get("id");
			String name = (String) map.get("name");
			String home = (String) map.get("home");
			String hostURL = (String) map.get("hostURL");
			String contextRoot = (String) map.get("contextRoot");

			NodeConfigImpl impl = new NodeConfigImpl();
			impl.setMachineId(machineId);
			impl.setPlatformId(platformId);
			impl.setId(id);
			impl.setName(name);
			impl.setHome(home);
			impl.setHostURL(hostURL);
			impl.setContextRoot(contextRoot);

			return impl;
		}

		/**
		 * 
		 * @param addMachineRequest
		 * @return
		 */
		public MachineConfigDTO toDTO(AddMachineConfigRequest addMachineRequest) {
			MachineConfigDTO addMachineRequestDTO = new MachineConfigDTO();
			addMachineRequestDTO.setId(addMachineRequest.getMachineId());
			addMachineRequestDTO.setName(addMachineRequest.getName());
			addMachineRequestDTO.setIpAddress(addMachineRequest.getIpAddress());
			return addMachineRequestDTO;
		}

		/**
		 * 
		 * @param updateMachineRequest
		 * @return
		 */
		public MachineConfigDTO toDTO(UpdateMachineConfigRequest updateMachineRequest) {
			MachineConfigDTO updateMachineRequestDTO = new MachineConfigDTO();
			updateMachineRequestDTO.setId(updateMachineRequest.getMachineId());
			updateMachineRequestDTO.setName(updateMachineRequest.getName());
			updateMachineRequestDTO.setIpAddress(updateMachineRequest.getIpAddress());
			updateMachineRequestDTO.setFieldsToUpdate(updateMachineRequest.getFieldsToUpdate());
			return updateMachineRequestDTO;
		}

		/**
		 * 
		 * @param addPlatformConfig
		 * @return
		 */
		public PlatformConfigDTO toDTO(AddPlatformConfigRequest addPlatformConfig) {
			PlatformConfigDTO addPlatformConfigDTO = new PlatformConfigDTO();
			addPlatformConfigDTO.setId(addPlatformConfig.getPlatformId());
			addPlatformConfigDTO.setName(addPlatformConfig.getName());
			addPlatformConfigDTO.setHome(addPlatformConfig.getHome());
			addPlatformConfigDTO.setHostURL(addPlatformConfig.getHostURL());
			addPlatformConfigDTO.setContextRoot(addPlatformConfig.getContextRoot());
			return addPlatformConfigDTO;
		}

		/**
		 * 
		 * @param updatePlatformConfig
		 * @return
		 */
		public PlatformConfigDTO toDTO(UpdatePlatformConfigRequest updatePlatformConfig) {
			PlatformConfigDTO updatePlatformConfigDTO = new PlatformConfigDTO();
			updatePlatformConfigDTO.setId(updatePlatformConfig.getPlatformId());
			updatePlatformConfigDTO.setName(updatePlatformConfig.getName());
			updatePlatformConfigDTO.setHome(updatePlatformConfig.getHome());
			updatePlatformConfigDTO.setHostURL(updatePlatformConfig.getHostURL());
			updatePlatformConfigDTO.setContextRoot(updatePlatformConfig.getContextRoot());
			updatePlatformConfigDTO.setFieldsToUpdate(updatePlatformConfig.getFieldsToUpdate());
			return updatePlatformConfigDTO;
		}

		/**
		 * 
		 * @param addNodeRequest
		 * @return
		 */
		public NodeConfigDTO toDTO(AddNodeConfigRequest addNodeRequest) {
			NodeConfigDTO addNodeRequestDTO = new NodeConfigDTO();
			addNodeRequestDTO.setId(addNodeRequest.getNodeId());
			addNodeRequestDTO.setName(addNodeRequest.getName());
			addNodeRequestDTO.setHome(addNodeRequest.getHome());
			addNodeRequestDTO.setHostURL(addNodeRequest.getHostURL());
			addNodeRequestDTO.setContextRoot(addNodeRequest.getContextRoot());
			return addNodeRequestDTO;
		}

		/**
		 * 
		 * @param updateNodeRequest
		 * @return
		 */
		public NodeConfigDTO toDTO(UpdateNodeConfigRequest updateNodeRequest) {
			NodeConfigDTO updateNodeRequestDTO = new NodeConfigDTO();
			updateNodeRequestDTO.setId(updateNodeRequest.getNodeId());
			updateNodeRequestDTO.setName(updateNodeRequest.getName());
			updateNodeRequestDTO.setHome(updateNodeRequest.getHome());
			updateNodeRequestDTO.setHostURL(updateNodeRequest.getHostURL());
			updateNodeRequestDTO.setContextRoot(updateNodeRequest.getContextRoot());
			return updateNodeRequestDTO;
		}
	}

	public static class NodeControl {

		public NodeInfo toNode(NodeDTO nodeDTO) {
			NodeInfoImpl nodeInfo = new NodeInfoImpl();
			nodeInfo.setId(nodeDTO.getId());
			nodeInfo.setName(nodeDTO.getName());
			nodeInfo.setUri(nodeDTO.getUri());
			nodeInfo.setAttributes(nodeDTO.getAttributes());
			return nodeInfo;
		}

		public NodeInfo[] getNodes(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			List<NodeInfo> nodes = new ArrayList<NodeInfo>();
			List<NodeDTO> nodeDTOs = response.readEntity(new GenericType<List<NodeDTO>>() {
			});
			for (NodeDTO nodeDTO : nodeDTOs) {
				NodeInfo node = toNode(nodeDTO);
				nodes.add(node);
			}
			return nodes.toArray(new NodeInfo[nodes.size()]);
		}

		public NodeInfo getNode(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			NodeInfo node = null;
			NodeDTO nodeDTO = response.readEntity(NodeDTO.class);
			if (nodeDTO != null) {
				node = toNode(nodeDTO);
			}
			return node;
		}

		public boolean exists(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean exists = false;
			try {
				exists = ResponseUtil.getSimpleValue(response, "exists", Boolean.class);
			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return exists;
		}

		public boolean isCreated(Response response) throws ClientException {
			return isSucceed(response);
		}

		public boolean isUpdated(Response response) throws ClientException {
			return isSucceed(response);
		}

		public boolean isDeleted(Response response) throws ClientException {
			return isSucceed(response);
		}

		public boolean isStarted(Response response) throws ClientException {
			return isSucceed(response);
		}

		public boolean isStopped(Response response) throws ClientException {
			return isSucceed(response);
		}

		public boolean isSucceed(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean succeed = false;
			try {
				succeed = ResponseUtil.getSimpleValue(response, "succeed", Boolean.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return succeed;
		}

		public String getStatus(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			String status = null;
			try {
				status = ResponseUtil.getSimpleValue(response, "status", String.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return status;
		}
	}

	public static class MissionControl {

		public Mission toMission(MissionDTO missionDTO) {
			Mission mission = new MissionImpl();
			mission.setTypeId(missionDTO.getTypeId());
			mission.setName(missionDTO.getName());
			return mission;
		}

		public Mission[] getMissions(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			List<Mission> missions = new ArrayList<Mission>();
			List<MissionDTO> missionDTOs = response.readEntity(new GenericType<List<MissionDTO>>() {
			});
			for (MissionDTO missionDTO : missionDTOs) {
				Mission mission = toMission(missionDTO);
				missions.add(mission);
			}
			return missions.toArray(new Mission[missions.size()]);
		}

		public Mission getMission(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}

			Mission mission = null;
			MissionDTO missionDTO = response.readEntity(MissionDTO.class);
			if (missionDTO != null) {
				mission = toMission(missionDTO);
			}
			return mission;
		}

		public boolean exists(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean exists = false;
			try {
				exists = ResponseUtil.getSimpleValue(response, "exists", Boolean.class);
			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return exists;
		}

		public boolean isCreated(Response response) throws ClientException {
			return isSucceed(response);
		}

		public boolean isDeleted(Response response) throws ClientException {
			return isSucceed(response);
		}

		public boolean isSucceed(Response response) throws ClientException {
			if (!ResponseUtil.isSuccessful(response)) {
				throw new ClientException(response);
			}
			boolean succeed = false;
			try {
				succeed = ResponseUtil.getSimpleValue(response, "succeed", Boolean.class);

			} catch (Exception e) {
				throw new ClientException(500, e.getMessage(), e);
			}
			return succeed;
		}
	}

}

// public NodespaceInfo[] convertToNodespaceInfos(Responses responses) {
// List<NodespaceInfo> nodespaceInfos = new ArrayList<NodespaceInfo>();
// Response response = responses.getResponse(Response.class);
// if (response != null) {
// Object bodyObj = response.getBody();
// if (bodyObj instanceof List) {
// List<?> elements = (List<?>) bodyObj;
// for (Object element : elements) {
// if (element instanceof Map<?, ?>) {
// Map<?, ?> map = (Map<?, ?>) element;
// NodespaceInfo nodespaceInfo = TransferAgentConverter.INSTANCE.toNodespaceInfo(map);
// if (nodespaceInfo != null) {
// nodespaceInfos.add(nodespaceInfo);
// }
// }
// }
// }
// }
// return nodespaceInfos.toArray(new NodespaceInfo[nodespaceInfos.size()]);
// }

// public NodespaceInfo convertToNodespaceInfo(Responses responses) {
// NodespaceInfo nodespaceInfo = null;
// Response response = responses.getResponse(Response.class);
// if (response != null) {
// Object bodyObj = response.getBody();
// if (bodyObj instanceof Map<?, ?>) {
// Map<?, ?> map = (Map<?, ?>) bodyObj;
// nodespaceInfo = TransferAgentConverter.INSTANCE.toNodespaceInfo(map);
// }
// }
// return nodespaceInfo;
// }

// public NodeInfo[] convertToNodeInfos(Responses responses) {
// List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
// Response response = responses.getResponse(Response.class);
// if (response != null) {
// Object bodyObj = response.getBody();
// if (bodyObj instanceof List) {
// List<?> elements = (List<?>) bodyObj;
// for (Object element : elements) {
// if (element instanceof Map<?, ?>) {
// Map<?, ?> map = (Map<?, ?>) element;
// NodeInfo nodeInfo = TransferAgentConverter.INSTANCE.toNodeInfo(map);
// if (nodeInfo != null) {
// nodeInfos.add(nodeInfo);
// }
// }
// }
// }
// }
// return nodeInfos.toArray(new NodeInfo[nodeInfos.size()]);
// }

// public NodeInfo convertToNodeInfo(Responses responses) {
// NodeInfo nodeInfo = null;
// Response response = responses.getResponse(Response.class);
// if (response != null) {
// Object bodyObj = response.getBody();
// if (bodyObj instanceof Map<?, ?>) {
// Map<?, ?> map = (Map<?, ?>) bodyObj;
// nodeInfo = TransferAgentConverter.INSTANCE.toNodeInfo(map);
// }
// }
// return nodeInfo;
// }

/// **
// *
// * @param map
// * @return
// */
// public NodespaceInfo toNodespaceInfo(Map<?, ?> map) {
// String name = (String) map.get("name");
//
// NodespaceInfoImpl impl = new NodespaceInfoImpl();
// impl.setName(name);
// return impl;
// }

/// **
// *
// * @param map
// * @return
// */
// public NodeInfo toNodeInfo(Map<?, ?> map) {
// String id = (String) map.get("id");
// String name = (String) map.get("name");
//
// NodeInfoImpl impl = new NodeInfoImpl();
// impl.setId(id);
// impl.setName(name);
// return impl;
// }
