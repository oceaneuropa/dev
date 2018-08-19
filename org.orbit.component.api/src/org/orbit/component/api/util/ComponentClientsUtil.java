package org.orbit.component.api.util;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.account.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.UpdateUserAccountRequest;
import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.component.api.tier1.identity.IdentityClient;
import org.orbit.component.api.tier1.identity.LoginRequest;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.tier1.identity.RegisterRequest;
import org.orbit.component.api.tier1.identity.RegisterResponse;
import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppQuery;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.api.tier2.appstore.CreateAppRequest;
import org.orbit.component.api.tier2.appstore.UpdateAppRequest;
import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.api.tier4.missioncontrol.MissionControlClient;
import org.orbit.component.model.tier3.domain.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.AddPlatformConfigRequest;
import org.orbit.component.model.tier3.domain.UpdateMachineConfigRequest;
import org.orbit.component.model.tier3.domain.UpdatePlatformConfigRequest;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientConstants;

public class ComponentClientsUtil {

	public static Identity Identity = new Identity();
	public static UserAccounts UserAccounts = new UserAccounts();
	public static Auth Auth = new Auth();
	public static AppStore AppStore = new AppStore();
	public static DomainControl DomainControl = new DomainControl();
	public static NodeControl NodeControl = new NodeControl();
	public static MissionControl MissionControl = new MissionControl();

	public static class Identity {
		/**
		 * 
		 * @param identityServiceUrl
		 * @param username
		 * @param email
		 * @param password
		 * @return
		 * @throws ClientException
		 */
		public RegisterResponse register(String identityServiceUrl, String username, String email, String password) throws ClientException {
			RegisterResponse response = null;
			IdentityClient identityClient = getIdentityClient(identityServiceUrl, username);
			if (identityClient != null) {
				RegisterRequest request = new RegisterRequest(username, email, password);
				response = identityClient.register(request);
			}
			return response;
		}

		/**
		 * 
		 * @param identityServiceUrl
		 * @param username
		 * @param email
		 * @param password
		 * @return
		 * @throws ClientException
		 */
		public LoginResponse login(String identityServiceUrl, String username, String email, String password) throws ClientException {
			LoginResponse response = null;
			IdentityClient identityClient = getIdentityClient(identityServiceUrl, username);
			if (identityClient != null) {
				LoginRequest request = new LoginRequest("orbit", "password", username, email, password);
				response = identityClient.login(request);
			}
			return response;
		}

		/**
		 * 
		 * @param identityServiceUrl
		 * @param accessToken
		 * @return
		 */
		public IdentityClient getIdentityClient(String identityServiceUrl, String accessToken) {
			IdentityClient identityClient = null;
			if (identityServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, identityServiceUrl);
				identityClient = ComponentClients.getInstance().getIdentityClient(properties);
			}
			return identityClient;
		}
	}

	public static class UserAccounts {
		public static final UserAccount[] EMPTY_USER_ACCOUNTS = new UserAccount[0];

		/**
		 * 
		 * @param userRegistryUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public UserAccount[] getUserAccounts(String userRegistryUrl, String accessToken) throws ClientException {
			UserAccount[] userAccounts = null;
			UserAccountClient userRegistry = getUserAccountsClient(userRegistryUrl, accessToken);
			if (userRegistry != null) {
				userAccounts = userRegistry.getUserAccounts();
			}
			if (userAccounts == null) {
				userAccounts = EMPTY_USER_ACCOUNTS;
			}
			return userAccounts;
		}

		/**
		 * 
		 * @param userRegistryUrl
		 * @param accessToken
		 * @param userId
		 * @return
		 * @throws ClientException
		 */
		public UserAccount getUserAccount(String userRegistryUrl, String accessToken, String userId) throws ClientException {
			UserAccount userAccount = null;
			UserAccountClient userRegistry = getUserAccountsClient(userRegistryUrl, accessToken);
			if (userRegistry != null) {
				userAccount = userRegistry.getUserAccount(userId);
			}
			return userAccount;
		}

		/**
		 * 
		 * @param userRegistryUrl
		 * @param accessToken
		 * @param username
		 * @return
		 * @throws ClientException
		 */
		public boolean usernameExists(String userRegistryUrl, String accessToken, String username) throws ClientException {
			boolean exists = false;
			UserAccountClient userRegistry = getUserAccountsClient(userRegistryUrl, accessToken);
			if (userRegistry != null) {
				exists = userRegistry.usernameExists(username);
			}
			return exists;
		}

		/**
		 * 
		 * @param userRegistryUrl
		 * @param accessToken
		 * @param email
		 * @return
		 * @throws ClientException
		 */
		public boolean emailExists(String userRegistryUrl, String accessToken, String email) throws ClientException {
			boolean exists = false;
			UserAccountClient userRegistry = getUserAccountsClient(userRegistryUrl, accessToken);
			if (userRegistry != null) {
				exists = userRegistry.emailExists(email);
			}
			return exists;
		}

		/**
		 * 
		 * @param userRegistryUrl
		 * @param accessToken
		 * @param userId
		 * @param email
		 * @param password
		 * @param firstName
		 * @param lastName
		 * @param phone
		 * @return
		 * @throws ClientException
		 */
		public boolean addUserAccount(String userRegistryUrl, String accessToken, String userId, String email, String password, String firstName, String lastName, String phone) throws ClientException {
			boolean succeed = false;
			UserAccountClient userRegistry = getUserAccountsClient(userRegistryUrl, accessToken);
			if (userRegistry != null) {
				CreateUserAccountRequest createUserAccountRequest = new CreateUserAccountRequest();
				createUserAccountRequest.setUserId(userId);
				createUserAccountRequest.setPassword(password);
				createUserAccountRequest.setFirstName(firstName);
				createUserAccountRequest.setLastName(lastName);
				createUserAccountRequest.setEmail(email);
				createUserAccountRequest.setPhone(phone);
				succeed = userRegistry.register(createUserAccountRequest);
			}
			return succeed;
		}

		/**
		 * 
		 * @param userRegistryUrl
		 * @param accessToken
		 * @param userId
		 * @param email
		 * @param password
		 * @param firstName
		 * @param lastName
		 * @param phone
		 * @return
		 * @throws ClientException
		 */
		public boolean updateUserAccount(String userRegistryUrl, String accessToken, String userId, String email, String password, String firstName, String lastName, String phone) throws ClientException {
			boolean succeed = false;
			UserAccountClient userRegistry = getUserAccountsClient(userRegistryUrl, accessToken);
			if (userRegistry != null) {
				UpdateUserAccountRequest updateUserAccountRequest = new UpdateUserAccountRequest();
				updateUserAccountRequest.setUserId(userId);
				updateUserAccountRequest.setPassword(password);
				updateUserAccountRequest.setEmail(email);
				updateUserAccountRequest.setFirstName(firstName);
				updateUserAccountRequest.setLastName(lastName);
				updateUserAccountRequest.setPhone(phone);

				succeed = userRegistry.update(updateUserAccountRequest);
			}
			return succeed;
		}

		/**
		 * 
		 * @param userRegistryUrl
		 * @param accessToken
		 * @param userId
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteUserAccount(String userRegistryUrl, String accessToken, String userId) throws ClientException {
			boolean exists = false;
			UserAccountClient userRegistry = getUserAccountsClient(userRegistryUrl, accessToken);
			if (userRegistry != null) {
				exists = userRegistry.delete(userId);
			}
			return exists;
		}

		/**
		 * 
		 * @param userRegistryUrl
		 * @param accessToken
		 * @return
		 */
		public UserAccountClient getUserAccountsClient(String userRegistryUrl, String accessToken) {
			UserAccountClient userRegistry = null;
			if (userRegistryUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, userRegistryUrl);
				userRegistry = ComponentClients.getInstance().getUserAccounts(properties);
			}
			return userRegistry;
		}
	}

	public static class Auth {
		/**
		 * 
		 * @param authServiceUrl
		 * @param accessToken
		 * @return
		 */
		public AuthClient getAuthClient(String authServiceUrl, String accessToken) {
			AuthClient auth = null;
			if (authServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, authServiceUrl);
				auth = ComponentClients.getInstance().getAuth(properties);
			}
			return auth;
		}
	}

	public static class AppStore {
		public static final AppManifest[] EMPTY_APPS = new AppManifest[0];

		/**
		 * 
		 * @param appStoreUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public AppManifest[] getApps(String appStoreUrl, String accessToken) throws ClientException {
			AppManifest[] appManifests = null;
			if (appStoreUrl != null) {
				AppStoreClient appStore = getAppStoreClient(appStoreUrl, accessToken);
				if (appStore != null) {
					AppQuery query = new AppQuery();
					appManifests = appStore.getApps(query);
				}
			}
			if (appManifests == null) {
				appManifests = EMPTY_APPS;
			}
			return appManifests;
		}

		/**
		 * 
		 * @param appStoreUrl
		 * @param accessToken
		 * @param appId
		 * @param appVersion
		 * @return
		 * @throws ClientException
		 */
		public AppManifest getApp(String appStoreUrl, String accessToken, String appId, String appVersion) throws ClientException {
			AppManifest appManifest = null;
			AppStoreClient appStore = getAppStoreClient(appStoreUrl, accessToken);
			if (appStore != null) {
				appManifest = appStore.getApp(appId, appVersion);
			}
			return appManifest;
		}

		/**
		 * 
		 * @param appStoreUrl
		 * @param accessToken
		 * @param id
		 * @param version
		 * @param type
		 * @param name
		 * @param desc
		 * @param fileName
		 * @return
		 * @throws ClientException
		 */
		public boolean addApp(String appStoreUrl, String accessToken, String id, String version, String type, String name, String desc, String fileName) throws ClientException {
			boolean succeed = false;
			AppStoreClient appStore = getAppStoreClient(appStoreUrl, accessToken);
			if (appStore != null) {
				CreateAppRequest createAppRequest = new CreateAppRequest();
				createAppRequest.setAppId(id);
				createAppRequest.setAppVersion(version);
				createAppRequest.setType(type);
				createAppRequest.setName(name);
				createAppRequest.setDescription(desc);
				createAppRequest.setFileName(fileName);

				succeed = appStore.create(createAppRequest);
			}
			return succeed;
		}

		/**
		 * 
		 * @param appStoreUrl
		 * @param accessToken
		 * @param id
		 * @param appId
		 * @param appVersion
		 * @param type
		 * @param name
		 * @param desc
		 * @param fileName
		 * @return
		 * @throws ClientException
		 */
		public boolean updateApp(String appStoreUrl, String accessToken, int id, String appId, String appVersion, String type, String name, String desc, String fileName) throws ClientException {
			boolean succeed = false;
			AppStoreClient appStore = getAppStoreClient(appStoreUrl, accessToken);
			if (appStore != null) {
				UpdateAppRequest updateAppRequest = new UpdateAppRequest();
				updateAppRequest.setId(id);
				updateAppRequest.setAppId(appId);
				updateAppRequest.setAppVersion(appVersion);
				updateAppRequest.setType(type);
				updateAppRequest.setName(name);
				updateAppRequest.setDescription(desc);
				updateAppRequest.setFileName(fileName);

				succeed = appStore.update(updateAppRequest);
			}
			return succeed;
		}

		/**
		 * 
		 * @param appStoreUrl
		 * @param accessToken
		 * @param id
		 * @param version
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteApp(String appStoreUrl, String accessToken, String id, String version) throws ClientException {
			boolean succeed = false;
			AppStoreClient appStore = getAppStoreClient(appStoreUrl, accessToken);
			if (appStore != null) {
				succeed = appStore.delete(id, version);
			}
			return succeed;
		}

		/**
		 * 
		 * @param appStoreUrl
		 * @param accessToken
		 * @param id
		 * @param appId
		 * @param appVersion
		 * @param files
		 * @return
		 * @throws ClientException
		 */
		public boolean uploadAppFile(String appStoreUrl, String accessToken, int id, String appId, String appVersion, List<File> files) throws ClientException {
			boolean succeed = false;
			AppStoreClient appStore = getAppStoreClient(appStoreUrl, accessToken);
			if (appStore != null) {
				File file = (files != null && !files.isEmpty()) ? files.get(0) : null;
				if (file != null && file.exists()) {
					succeed = appStore.uploadAppArchive(id, appId, appVersion, file.toPath());
				}
			}
			return succeed;
		}

		/**
		 * 
		 * @param appStoreUrl
		 * @param accessToken
		 * @param appId
		 * @param appVersion
		 * @param output
		 * @throws ClientException
		 */
		public void downloadAppFile(String appStoreUrl, String accessToken, String appId, String appVersion, OutputStream output) throws ClientException {
			AppStoreClient appStore = getAppStoreClient(appStoreUrl, accessToken);
			if (appStore != null) {
				appStore.downloadAppArchive(appId, appVersion, output);
			}
		}

		/**
		 * 
		 * @param appStoreUrl
		 * @return
		 */
		public AppStoreClient getAppStoreClient(String appStoreUrl, String accessToken) {
			AppStoreClient appStoreClient = null;
			if (appStoreUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, appStoreUrl);
				appStoreClient = ComponentClients.getInstance().getAppStore(properties);
			}
			return appStoreClient;
		}
	}

	public static class DomainControl {
		public static final MachineConfig[] EMPTY_MACHINE_CONFIGS = new MachineConfig[0];
		public static final PlatformConfig[] EMPTY_PLATFORM_CONFIGS = new PlatformConfig[0];

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @return
		 * @throws ClientException
		 */
		public MachineConfig[] getMachineConfigs(String domainServiceUrl, String accessToken) throws ClientException {
			MachineConfig[] machineConfigs = null;
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl, accessToken);
			if (domainClient != null) {
				machineConfigs = domainClient.getMachineConfigs();
			}
			if (machineConfigs == null) {
				machineConfigs = EMPTY_MACHINE_CONFIGS;
			}
			return machineConfigs;
		}

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @param machineId
		 * @return
		 * @throws ClientException
		 */
		public MachineConfig getMachineConfig(String domainServiceUrl, String accessToken, String machineId) throws ClientException {
			MachineConfig machineConfig = null;
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl, accessToken);
			if (domainClient != null) {
				machineConfig = domainClient.getMachineConfig(machineId);
			}
			return machineConfig;
		}

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @param id
		 * @param name
		 * @param ip
		 * @return
		 * @throws ClientException
		 */
		public boolean addMachineConfig(String domainServiceUrl, String accessToken, String id, String name, String ip) throws ClientException {
			boolean succeed = false;
			DomainManagementClient domainMgmt = getDomainClient(domainServiceUrl, accessToken);
			if (domainMgmt != null) {
				AddMachineConfigRequest addMachineRequest = new AddMachineConfigRequest();
				addMachineRequest.setMachineId(id);
				addMachineRequest.setName(name);
				addMachineRequest.setIpAddress(ip);
				succeed = domainMgmt.addMachineConfig(addMachineRequest);
			}
			return succeed;
		}

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @param id
		 * @param name
		 * @param ip
		 * @return
		 * @throws ClientException
		 */
		public boolean updateMachineConfig(String domainServiceUrl, String accessToken, String id, String name, String ip) throws ClientException {
			boolean succeed = false;
			DomainManagementClient domainMgmt = getDomainClient(domainServiceUrl, accessToken);
			if (domainMgmt != null) {
				UpdateMachineConfigRequest updateMachineRequest = new UpdateMachineConfigRequest();
				updateMachineRequest.setMachineId(id);
				updateMachineRequest.setName(name);
				updateMachineRequest.setIpAddress(ip);
				succeed = domainMgmt.updateMachineConfig(updateMachineRequest);
			}
			return succeed;
		}

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @param id
		 * @return
		 * @throws ClientException
		 */
		public boolean removeMachineConfig(String domainServiceUrl, String accessToken, String id) throws ClientException {
			boolean succeed = false;
			DomainManagementClient domainMgmt = getDomainClient(domainServiceUrl, accessToken);
			if (domainMgmt != null) {
				succeed = domainMgmt.removeMachineConfig(id);
			}
			return succeed;
		}

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @param machineId
		 * @return
		 * @throws ClientException
		 */
		public PlatformConfig[] getPlatformConfigs(String domainServiceUrl, String accessToken, String machineId) throws ClientException {
			PlatformConfig[] platformConfigs = null;
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl, accessToken);
			if (domainClient != null) {
				platformConfigs = domainClient.getPlatformConfigs(machineId);
			}
			if (platformConfigs == null) {
				platformConfigs = EMPTY_PLATFORM_CONFIGS;
			}
			return platformConfigs;
		}

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @param machineId
		 * @param platformId
		 * @return
		 * @throws ClientException
		 */
		public PlatformConfig getPlatformConfig(String domainServiceUrl, String accessToken, String machineId, String platformId) throws ClientException {
			PlatformConfig platformConfig = null;
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl, accessToken);
			if (domainClient != null) {
				platformConfig = domainClient.getPlatformConfig(machineId, platformId);
			}
			return platformConfig;
		}

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @param machineId
		 * @param platformId
		 * @param name
		 * @param hostUrl
		 * @param contextRoot
		 * @return
		 * @throws ClientException
		 */
		public boolean addPlatformConfig(String domainServiceUrl, String accessToken, String machineId, String platformId, String name, String hostUrl, String contextRoot) throws ClientException {
			boolean succeed = false;
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl, accessToken);
			if (domainClient != null) {
				AddPlatformConfigRequest addPlatformRequest = new AddPlatformConfigRequest();
				addPlatformRequest.setPlatformId(platformId);
				addPlatformRequest.setName(name);
				addPlatformRequest.setHostURL(hostUrl);
				addPlatformRequest.setContextRoot(contextRoot);
				succeed = domainClient.addPlatformConfig(machineId, addPlatformRequest);
			}
			return succeed;
		}

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @param machineId
		 * @param platformId
		 * @param name
		 * @param hostUrl
		 * @param contextRoot
		 * @return
		 * @throws ClientException
		 */
		public boolean updatePlatformConfig(String domainServiceUrl, String accessToken, String machineId, String platformId, String name, String hostUrl, String contextRoot) throws ClientException {
			boolean succeed = false;
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl, accessToken);
			if (domainClient != null) {
				UpdatePlatformConfigRequest updatePlatformRequest = new UpdatePlatformConfigRequest();
				updatePlatformRequest.setPlatformId(platformId);
				updatePlatformRequest.setName(name);
				updatePlatformRequest.setHostURL(hostUrl);
				updatePlatformRequest.setContextRoot(contextRoot);
				succeed = domainClient.updatPlatformConfig(machineId, updatePlatformRequest);
			}
			return succeed;
		}

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @param machineId
		 * @param platformId
		 * @return
		 * @throws ClientException
		 */
		public boolean removePlatformConfig(String domainServiceUrl, String accessToken, String machineId, String platformId) throws ClientException {
			boolean succeed = false;
			DomainManagementClient domainClient = getDomainClient(domainServiceUrl, accessToken);
			if (domainClient != null) {
				succeed = domainClient.removePlatformConfig(machineId, platformId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @return
		 */
		public DomainManagementClient getDomainClient(String domainServiceUrl, String accessToken) {
			DomainManagementClient domainClient = null;
			if (domainServiceUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, domainServiceUrl);
				domainClient = ComponentClients.getInstance().getDomainClient(properties);
			}
			return domainClient;
		}
	}

	public static class NodeControl {
		public static final NodeInfo[] EMPTY_NODE_INFOS = new NodeInfo[0];

		/**
		 * 
		 * @param nodeControlClient
		 * @return
		 * @throws ClientException
		 */
		public NodeInfo[] getNodes(NodeControlClient nodeControlClient) throws ClientException {
			NodeInfo[] nodeInfos = null;
			if (nodeControlClient != null) {
				nodeInfos = nodeControlClient.getNodes();
			}
			if (nodeInfos == null) {
				nodeInfos = EMPTY_NODE_INFOS;
			}
			return nodeInfos;
		}

		/**
		 * 
		 * @param nodeControlClient
		 * @param nodeId
		 * @return
		 * @throws ClientException
		 */
		public NodeInfo getNode(NodeControlClient nodeControlClient, String nodeId) throws ClientException {
			NodeInfo nodeInfo = null;
			if (nodeControlClient != null && nodeId != null) {
				nodeInfo = nodeControlClient.getNode(nodeId);
			}
			return nodeInfo;
		}

		/**
		 * 
		 * @param nodeControlClient
		 * @param id
		 * @param name
		 * @param typeId
		 * @return
		 * @throws ClientException
		 */
		public boolean createNode(NodeControlClient nodeControlClient, String id, String name, String typeId) throws ClientException {
			boolean succeed = false;
			if (nodeControlClient != null && id != null) {
				succeed = nodeControlClient.createNode(id, name, typeId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param nodeControlClient
		 * @param id
		 * @param name
		 * @param typeId
		 * @return
		 * @throws ClientException
		 */
		public boolean updateNode(NodeControlClient nodeControlClient, String id, String name, String typeId) throws ClientException {
			boolean succeed = false;
			if (nodeControlClient != null && id != null) {
				succeed = nodeControlClient.updateNode(id, name, typeId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param nodeControlClient
		 * @param nodeId
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteNode(NodeControlClient nodeControlClient, String nodeId) throws ClientException {
			boolean succeed = false;
			if (nodeControlClient != null && nodeId != null) {
				succeed = nodeControlClient.deleteNode(nodeId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param nodeControlClient
		 * @param nodeId
		 * @return
		 * @throws ClientException
		 */
		public boolean startNode(NodeControlClient nodeControlClient, String nodeId) throws ClientException {
			boolean succeed = false;
			if (nodeControlClient != null && nodeId != null) {
				succeed = nodeControlClient.startNode(nodeId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param nodeControlClient
		 * @param nodeId
		 * @return
		 * @throws ClientException
		 */
		public boolean stopNode(NodeControlClient nodeControlClient, String nodeId) throws ClientException {
			boolean succeed = false;
			if (nodeControlClient != null && nodeId != null) {
				succeed = nodeControlClient.stopNode(nodeId);
			}
			return succeed;
		}

		/**
		 * 
		 * @param nodeControlClient
		 * @param nodeId
		 * @param name
		 * @param value
		 * @return
		 * @throws ClientException
		 */
		public boolean addNodeAttribute(NodeControlClient nodeControlClient, String nodeId, String name, Object value) throws ClientException {
			boolean succeed = false;
			if (nodeControlClient != null && nodeId != null) {
				succeed = nodeControlClient.addNodeAttribute(nodeId, name, value);
			}
			return succeed;
		}

		/**
		 * 
		 * @param nodeControlClient
		 * @param nodeId
		 * @param oldName
		 * @param name
		 * @param value
		 * @return
		 * @throws ClientException
		 */
		public boolean updateNodeAttribute(NodeControlClient nodeControlClient, String nodeId, String oldName, String name, Object value) throws ClientException {
			boolean succeed = false;
			if (nodeControlClient != null && nodeId != null) {
				succeed = nodeControlClient.updateNodeAttribute(nodeId, oldName, name, value);
			}
			return succeed;
		}

		/**
		 * 
		 * @param nodeControlClient
		 * @param nodeId
		 * @param name
		 * @return
		 * @throws ClientException
		 */
		public boolean deleteNodeAttribute(NodeControlClient nodeControlClient, String nodeId, String name) throws ClientException {
			boolean succeed = false;
			if (nodeControlClient != null && nodeId != null) {
				succeed = nodeControlClient.deleteNodeAttribute(nodeId, name);
			}
			return succeed;
		}

		/**
		 * 
		 * @param nodeControlUrl
		 * @param accessToken
		 * @return
		 */
		public NodeControlClient getNodeControlClient(String nodeControlUrl, String accessToken) {
			NodeControlClient nodeControlClient = null;
			if (nodeControlUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, nodeControlUrl);

				nodeControlClient = ComponentClients.getInstance().getNodeControl(properties);
			}
			return nodeControlClient;
		}
	}

	public static class MissionControl {
		/**
		 * 
		 * @param domainServiceUrl
		 * @param accessToken
		 * @return
		 */
		public MissionControlClient getMissionControlClient(String missionControlUrl, String accessToken) {
			MissionControlClient missionControl = null;
			if (missionControlUrl != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(WSClientConstants.REALM, null);
				properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
				properties.put(WSClientConstants.URL, missionControlUrl);

				missionControl = ComponentClients.getInstance().getMissionControl(properties);
			}
			return missionControl;
		}
	}

}

// /**
// *
// * @param identityServiceUrl
// * @param username
// * @return
// * @throws ClientException
// */
// public boolean usernameExists(String identityServiceUrl, String username) throws ClientException {
// boolean exists = false;
// IdentityClient identityClient = getIdentityClient(identityServiceUrl, null);
// if (identityClient != null) {
// exists = identityClient.usernameExists(username);
// }
// return exists;
// }
//
// /**
// *
// * @param identityServiceUrl
// * @param username
// * @param email
// * @return
// * @throws ClientException
// */
// public boolean emailExists(String identityServiceUrl, String email) throws ClientException {
// boolean exists = false;
// IdentityClient identityClient = getIdentityClient(identityServiceUrl, null);
// if (identityClient != null) {
// exists = identityClient.emailExists(email);
// }
// return exists;
// }

// /**
// *
// * @param platformConfig
// * @return
// * @throws ClientException
// */
// protected NodeControlClient getNodeControlClient(PlatformConfig platformConfig) throws ClientException {
// NodeControlClient nodeControlClient = null;
// if (platformConfig != null) {
// String url = platformConfig.getHostURL() + platformConfig.getContextRoot();
// nodeControlClient = OrbitClients.getInstance().getNodeControl(url);
// }
// return nodeControlClient;
// }

// /**
// *
// * @param nodeIdToIndexItem
// * @param nodeId
// * @return
// */
// public PlatformClient getNodePlatformClient(Map<String, IndexItem> nodeIdToIndexItem, String nodeId) {
// PlatformClient nodePlatformClient = null;
// if (nodeIdToIndexItem != null && nodeId != null) {
// IndexItem nodeIndexItem = nodeIdToIndexItem.get(nodeId);
// if (nodeIndexItem != null) {
// nodePlatformClient = getNodePlatformClient(nodeIndexItem);
// }
// }
// return nodePlatformClient;
// }
//
// /**
// *
// * @param nodeIdToIndexItem
// * @param nodeId
// * @return
// */
// public PlatformClient getNodePlatformClient(IndexItem nodeIndexItem) {
// PlatformClient nodePlatformClient = null;
// if (nodeIndexItem != null) {
// String nodePlatformUrl = null;
// String platformHostUrl = (String) nodeIndexItem.getProperties().get(PlatformConstants.PLATFORM_HOST_URL);
// String platformContextRoot = (String) nodeIndexItem.getProperties().get(PlatformConstants.PLATFORM_CONTEXT_ROOT);
//
// if (platformHostUrl != null && platformContextRoot != null) {
// nodePlatformUrl = platformHostUrl;
// if (!nodePlatformUrl.endsWith("/") && !platformContextRoot.startsWith("/")) {
// nodePlatformUrl += "/";
// }
// nodePlatformUrl += platformContextRoot;
// }
//
// if (nodePlatformUrl != null) {
// nodePlatformClient = Clients.getInstance().getPlatformClient(nodePlatformUrl);
// }
// }
// return nodePlatformClient;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @return
// * @throws ClientException
// */
// public NodeControlClient getNodeControlClient(String domainServiceUrl, String machineId, String platformId) throws ClientException {
// NodeControlClient nodeControlClient = null;
// if (domainServiceUrl != null && machineId != null && platformId != null) {
// DomainManagementClient domainClient = getDomainClient(domainServiceUrl);
// if (domainClient != null) {
// PlatformConfig platformConfig = domainClient.getPlatformConfig(machineId, platformId);
// if (platformConfig != null) {
// String url = platformConfig.getHostURL() + platformConfig.getContextRoot();
// nodeControlClient = OrbitClients.getInstance().getNodeControl(url);
// }
// }
// }
// return nodeControlClient;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @return
// * @throws ClientException
// */
// public NodeInfo[] getNodes(String domainServiceUrl, String machineId, String platformId) throws ClientException {
// NodeInfo[] nodeInfos = null;
// if (domainServiceUrl != null && machineId != null && platformId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// nodeInfos = nodeControlClient.getNodes();
// }
// }
// return nodeInfos;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @return
// * @throws ClientException
// */
// public NodeInfo getNode(String domainServiceUrl, String machineId, String platformId, String nodeId) throws ClientException {
// NodeInfo nodeInfo = null;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// nodeInfo = nodeControlClient.getNode(nodeId);
// }
// }
// return nodeInfo;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param id
// * @param name
// * @param typeId
// * @return
// * @throws ClientException
// */
// public boolean createNode(String domainServiceUrl, String machineId, String platformId, String id, String name, String typeId) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && id != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.createNode(id, name, typeId);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param id
// * @param name
// * @param typeId
// * @return
// * @throws ClientException
// */
// public boolean updateNode(String domainServiceUrl, String machineId, String platformId, String id, String name, String typeId) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && id != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.updateNode(id, name, typeId);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @return
// * @throws ClientException
// */
// public boolean deleteNode(String domainServiceUrl, String machineId, String platformId, String nodeId) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.deleteNode(nodeId);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @return
// * @throws ClientException
// */
// public boolean startNode(String domainServiceUrl, String machineId, String platformId, String nodeId) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.startNode(nodeId);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @return
// * @throws ClientException
// */
// public boolean stopNode(String domainServiceUrl, String machineId, String platformId, String nodeId) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.stopNode(nodeId);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @param name
// * @param value
// * @return
// * @throws ClientException
// */
// public boolean addNodeAttribute(String domainServiceUrl, String machineId, String platformId, String nodeId, String name, Object value) throws
// ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.addNodeAttribute(nodeId, name, value);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @param name
// * @param value
// * @return
// * @throws ClientException
// */
// public boolean updateNodeAttribute(String domainServiceUrl, String machineId, String platformId, String nodeId, String oldName, String name, Object
// value) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.updateNodeAttribute(nodeId, oldName, name, value);
// }
// }
// return succeed;
// }

// /**
// *
// * @param domainServiceUrl
// * @param machineId
// * @param platformId
// * @param nodeId
// * @param name
// * @return
// * @throws ClientException
// */
// public boolean deleteNodeAttribute(String domainServiceUrl, String machineId, String platformId, String nodeId, String name) throws ClientException {
// boolean succeed = false;
// if (domainServiceUrl != null && machineId != null && platformId != null && nodeId != null) {
// NodeControlClient nodeControlClient = getNodeControlClient(domainServiceUrl, machineId, platformId);
// if (nodeControlClient != null) {
// succeed = nodeControlClient.deleteNodeAttribute(nodeId, name);
// }
// }
// return succeed;
// }
