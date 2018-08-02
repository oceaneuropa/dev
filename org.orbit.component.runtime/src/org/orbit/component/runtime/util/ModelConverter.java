package org.orbit.component.runtime.util;

import org.orbit.component.model.tier1.account.UserAccountDTO;
import org.orbit.component.model.tier1.auth.AuthorizationRequest;
import org.orbit.component.model.tier1.auth.AuthorizationRequestDTO;
import org.orbit.component.model.tier1.auth.AuthorizationResponse;
import org.orbit.component.model.tier1.auth.AuthorizationResponseDTO;
import org.orbit.component.model.tier1.auth.TokenRequest;
import org.orbit.component.model.tier1.auth.TokenRequestDTO;
import org.orbit.component.model.tier1.auth.TokenResponse;
import org.orbit.component.model.tier1.auth.TokenResponseDTO;
import org.orbit.component.model.tier2.appstore.AppManifestDTO;
import org.orbit.component.model.tier2.appstore.AppQueryDTO;
import org.orbit.component.model.tier3.domain.MachineConfigDTO;
import org.orbit.component.model.tier3.domain.NodeConfigDTO;
import org.orbit.component.model.tier3.domain.PlatformConfigDTO;
import org.orbit.component.runtime.model.account.UserAccount;
import org.orbit.component.runtime.model.appstore.AppManifest;
import org.orbit.component.runtime.model.appstore.AppQuery;
import org.orbit.component.runtime.model.domain.MachineConfig;
import org.orbit.component.runtime.model.domain.NodeConfig;
import org.orbit.component.runtime.model.domain.PlatformConfig;

public class ModelConverter {

	public static Account Account = new Account();
	public static AppStore AppStore = new AppStore();
	public static Auth Auth = new Auth();
	public static Domain Domain = new Domain();

	public static class Account {
		// ------------------------------------------------------------------------------------------
		// RTO -> DTO
		// ------------------------------------------------------------------------------------------
		/**
		 * Convert UserAccountRTO to UserAccountDTO.
		 * 
		 * @param userAccount
		 * @return
		 */
		public UserAccountDTO toDTO(UserAccount userAccount) {
			if (userAccount == null) {
				return null;
			}
			UserAccountDTO dto = new UserAccountDTO();

			dto.setUserId(userAccount.getUserId());
			dto.setPassword(userAccount.getPassword());
			dto.setEmail(userAccount.getEmail());
			dto.setFirstName(userAccount.getFirstName());
			dto.setLastName(userAccount.getLastName());
			dto.setPhone(userAccount.getPhone());
			dto.setCreationTime(userAccount.getCreationTime());
			dto.setLastUpdateTime(userAccount.getLastUpdateTime());
			dto.setActivated(userAccount.isActivated());

			return dto;
		}

		// ------------------------------------------------------------------------------------------
		// DTO -> RTO
		// ------------------------------------------------------------------------------------------
		/**
		 * Convert UserAccountDTO to UserAccountRTO.
		 * 
		 * @param userAccountDTO
		 * @return
		 */
		public UserAccount toRTO(UserAccountDTO userAccountDTO) {
			if (userAccountDTO == null) {
				return null;
			}
			UserAccount userAccount = new UserAccount();

			userAccount.setUserId(userAccountDTO.getUserId());
			userAccount.setPassword(userAccountDTO.getPassword());
			userAccount.setEmail(userAccountDTO.getEmail());
			userAccount.setFirstName(userAccountDTO.getFirstName());
			userAccount.setLastName(userAccountDTO.getLastName());
			userAccount.setPhone(userAccountDTO.getPhone());
			userAccount.setCreationTime(userAccountDTO.getCreationTime());
			userAccount.setLastUpdateTime(userAccountDTO.getLastUpdateTime());
			userAccount.setActivated(userAccountDTO.isActivated());

			return userAccount;
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
		// ------------------------------------------------------------------------------------------
		// RTO to DTO
		// ------------------------------------------------------------------------------------------
		/**
		 * Convert AppManifestRTO to AppManifestDTO.
		 * 
		 * @param app
		 * @return
		 */
		public AppManifestDTO toDTO(AppManifest app) {
			if (app == null) {
				return null;
			}
			AppManifestDTO dto = new AppManifestDTO();

			dto.setId(app.getId());
			dto.setAppId(app.getAppId());
			dto.setAppVersion(app.getAppVersion());
			dto.setType(app.getType());
			dto.setName(app.getName());
			dto.setAppManifest(app.getAppManifest());
			dto.setAppFileName(app.getAppFileName());
			dto.setDescription(app.getDescription());
			dto.setDateCreated(app.getDateCreated());
			dto.setDateModified(app.getDateModified());

			return dto;
		}

		// ------------------------------------------------------------------------------------------
		// DTO to RTO
		// ------------------------------------------------------------------------------------------
		/**
		 * Convert AppManifestDTO to AppManifestRTO.
		 * 
		 * @param app
		 * @return
		 */
		public AppManifest toRTO(AppManifestDTO appDTO) {
			if (appDTO == null) {
				return null;
			}
			AppManifest app = new AppManifest();

			app.setId(appDTO.getId());
			app.setAppId(appDTO.getAppId());
			app.setAppVersion(appDTO.getAppVersion());
			app.setType(appDTO.getType());
			app.setName(appDTO.getName());
			app.setAppManifest(appDTO.getAppManifest());
			app.setAppFileName(appDTO.getAppFileName());
			app.setDescription(appDTO.getDescription());
			app.setDateCreated(appDTO.getDateCreated());
			app.setDateModified(appDTO.getDateModified());

			return app;
		}

		/**
		 * Convert AppQueryDTO to AppQueryRTO.
		 * 
		 * @param app
		 * @return
		 */
		public AppQuery toRTO(AppQueryDTO queryDTO) {
			if (queryDTO == null) {
				return null;
			}
			AppQuery query = new AppQuery();

			// Set/Get
			query.setAppId(queryDTO.getAppId());
			query.setAppVersion(queryDTO.getAppVersion());
			query.setType(queryDTO.getType());
			query.setName(queryDTO.getName());
			query.setDescription(queryDTO.getDescription());

			// Where operator
			query.setAppId_oper(queryDTO.getAppId_oper());
			query.setAppVersion_oper(queryDTO.getAppVersion_oper());
			query.setType_oper(queryDTO.getType_oper());
			query.setName_oper(queryDTO.getName_oper());
			query.setDescription_oper(queryDTO.getDescription_oper());

			return query;
		}
	}

	public static class Domain {
		// ------------------------------------------------------------------------------------------
		// RTO to DTO
		// ------------------------------------------------------------------------------------------
		/**
		 * Convert MachineConfigRTO to MachineConfigDTO.
		 * 
		 * @param machineConfig
		 * @return
		 */
		public MachineConfigDTO toDTO(MachineConfig machineConfig) {
			if (machineConfig == null) {
				return null;
			}
			MachineConfigDTO dto = new MachineConfigDTO();

			dto.setId(machineConfig.getId());
			dto.setName(machineConfig.getName());
			dto.setIpAddress(machineConfig.getIpAddress());

			return dto;
		}

		/**
		 * Convert TransferAgentConfigRTO to TransferAgentConfigDTO.
		 * 
		 * @param transferAgentConfig
		 * @return
		 */
		public PlatformConfigDTO toDTO(PlatformConfig transferAgentConfig) {
			if (transferAgentConfig == null) {
				return null;
			}
			PlatformConfigDTO dto = new PlatformConfigDTO();

			dto.setId(transferAgentConfig.getId());
			dto.setName(transferAgentConfig.getName());
			dto.setHome(transferAgentConfig.getHome());
			dto.setHostURL(transferAgentConfig.getHostURL());
			dto.setContextRoot(transferAgentConfig.getContextRoot());

			return dto;
		}

		/**
		 * Convert NodeConfigRTO to NodeConfigDTO.
		 * 
		 * @param nodeConfig
		 * @return
		 */
		public NodeConfigDTO toDTO(NodeConfig nodeConfig) {
			if (nodeConfig == null) {
				return null;
			}
			NodeConfigDTO dto = new NodeConfigDTO();

			dto.setId(nodeConfig.getId());
			dto.setMachineId(nodeConfig.getMachineId());
			dto.setPlatformId(nodeConfig.getPlatformId());
			dto.setName(nodeConfig.getName());
			dto.setHome(nodeConfig.getHome());
			dto.setHostURL(nodeConfig.getHostURL());
			dto.setContextRoot(nodeConfig.getContextRoot());

			return dto;
		}

		// ------------------------------------------------------------------------------------------
		// DTO to RTO
		// ------------------------------------------------------------------------------------------
		/**
		 * Convert MachineConfigDTO to MachineConfigRTO.
		 * 
		 * @param machineConfigDTO
		 * @return
		 */
		public MachineConfig toRTO(MachineConfigDTO machineConfigDTO) {
			if (machineConfigDTO == null) {
				return null;
			}
			MachineConfig machineConfig = new MachineConfig();

			machineConfig.setId(machineConfigDTO.getId());
			machineConfig.setName(machineConfigDTO.getName());
			machineConfig.setIpAddress(machineConfigDTO.getIpAddress());

			return machineConfig;
		}

		/**
		 * Convert TransferAgentConfigDTO to TransferAgentConfigRTO.
		 * 
		 * @param transferAgentConfigDTO
		 * @return
		 */
		public PlatformConfig toRTO(PlatformConfigDTO transferAgentConfigDTO) {
			if (transferAgentConfigDTO == null) {
				return null;
			}
			PlatformConfig transferAgentConfig = new PlatformConfig();

			transferAgentConfig.setId(transferAgentConfigDTO.getId());
			transferAgentConfig.setName(transferAgentConfigDTO.getName());
			transferAgentConfig.setHome(transferAgentConfigDTO.getHome());
			transferAgentConfig.setHostURL(transferAgentConfigDTO.getHostURL());
			transferAgentConfig.setContextRoot(transferAgentConfigDTO.getContextRoot());

			return transferAgentConfig;
		}

		/**
		 * Convert NodeConfigDTO to NodeConfigRTO.
		 * 
		 * @param nodeConfigDTO
		 * @return
		 */
		public NodeConfig toRTO(NodeConfigDTO nodeConfigDTO) {
			if (nodeConfigDTO == null) {
				return null;
			}
			NodeConfig nodeConfig = new NodeConfig();

			nodeConfig.setId(nodeConfigDTO.getId());
			nodeConfig.setMachineId(nodeConfigDTO.getMachineId());
			nodeConfig.setPlatformId(nodeConfigDTO.getPlatformId());
			nodeConfig.setName(nodeConfigDTO.getName());
			nodeConfig.setHome(nodeConfigDTO.getHome());
			nodeConfig.setHostURL(nodeConfigDTO.getHostURL());
			nodeConfig.setContextRoot(nodeConfigDTO.getContextRoot());

			return nodeConfig;
		}
	}

}

/// **
// * Convert UserRegistryException object to Error DTO.
// *
// * @param e
// * @return
// */
// public ErrorDTO toDTO(ServerException e) {
// if (e == null) {
// return null;
// }
//
// ErrorDTO dto = new ErrorDTO();
//
// dto.setCode(e.getCode());
// dto.setMessage(e.getMessage());
//
// if (e.getCause() != null) {
// String causeName = e.getCause().getClass().getName();
// String causeMessage = e.getCause().getMessage();
// dto.setDetail(causeName + " " + causeMessage);
//
// } else {
// String causeName = e.getClass().getName();
// dto.setDetail(causeName);
// }
// return dto;
// }
