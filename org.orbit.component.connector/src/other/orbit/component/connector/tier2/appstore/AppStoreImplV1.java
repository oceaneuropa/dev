package other.orbit.component.connector.tier2.appstore;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppQuery;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.api.tier2.appstore.CreateAppRequest;
import org.orbit.component.api.tier2.appstore.UpdateAppRequest;
import org.orbit.component.connector.tier2.appstore.AppManifestImpl;
import org.orbit.component.connector.tier2.appstore.AppStoreWSClient;
import org.orbit.component.model.tier2.appstore.AppManifestDTO;
import org.orbit.component.model.tier2.appstore.AppQueryDTO;
import org.orbit.infra.api.InfraConstants;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.jdbc.SQLWhereOperator;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadata;
import org.origin.common.rest.model.StatusDTO;

/**
 * App store client connector implementation.
 * 
 */
public class AppStoreImplV1 implements AppStoreClient {

	protected Map<String, Object> properties;
	protected AppStoreWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public AppStoreImplV1(ServiceConnector<AppStoreClient> connector, Map<String, Object> properties) {
		if (connector != null) {
			adapt(ServiceConnector.class, connector);
		}
		this.properties = checkProperties(properties);
		initClient();
	}

	private Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public boolean close() throws ClientException {
		@SuppressWarnings("unchecked")
		ServiceConnector<AppStoreClient> connector = getAdapter(ServiceConnector.class);
		if (connector != null) {
			return connector.close(this);
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------------
	// Configuration methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public ServiceMetadata getMetadata() throws ClientException {
		return null;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(InfraConstants.SERVICE__NAME);
		return name;
	}

	@Override
	public String getURL() {
		String url = (String) properties.get(WSClientConstants.URL);
		return url;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void update(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	protected void initClient() {
		WSClientConfiguration config = WSClientConfiguration.create(this.properties);
		this.client = new AppStoreWSClient(config);
	}

	// ------------------------------------------------------------------------------------------------
	// Web service methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public AppManifest[] getApps(AppQuery query) throws ClientException {
		List<AppManifest> apps = new ArrayList<AppManifest>();
		try {
			AppQueryDTO queryDTO = toDTO(query);
			List<AppManifestDTO> appDTOs = this.client.getList(queryDTO);
			for (AppManifestDTO appDTO : appDTOs) {
				apps.add(toAppManifestImpl(appDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return apps.toArray(new AppManifest[apps.size()]);
	}

	@Override
	public AppManifest getApp(String appId, String appVersion) throws ClientException {
		AppManifest app = null;
		try {
			AppManifestDTO appDTO = this.client.get(appId, appVersion);
			if (appDTO != null) {
				app = toAppManifestImpl(appDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return app;
	}

	@Override
	public boolean exists(String appId, String appVersion) throws ClientException {
		try {
			return this.client.exists(appId, appVersion);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean create(CreateAppRequest createAppRequest) throws ClientException {
		String appId = createAppRequest.getAppId();
		String appVersion = createAppRequest.getAppVersion();
		if (this.client.exists(appId, appVersion)) {
			throw new ClientException(400, String.format("App with appId '%s' and appVersion '%s' already exists.", appId, appVersion));
		}

		AppManifestDTO newAppDTO = this.client.create(toDTO(createAppRequest));
		if (newAppDTO != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean create(CreateAppRequest createAppRequest, Path filePath) throws ClientException {
		String appId = createAppRequest.getAppId();
		String appVersion = createAppRequest.getAppVersion();
		if (this.client.exists(appId, appVersion)) {
			throw new ClientException(400, String.format("App with appId '%s' and appVersion '%s' already exists.", appId, appVersion));
		}

		if (createAppRequest.getFileName() == null && filePath != null) {
			createAppRequest.setFileName(filePath.getFileName().toString());
		}

		AppManifestDTO newAppDTO = this.client.create(toDTO(createAppRequest));

		if (newAppDTO != null && filePath != null) {
			return uploadAppArchive(-1, newAppDTO.getAppId(), newAppDTO.getAppVersion(), filePath);
		}
		return false;
	}

	@Override
	public boolean update(UpdateAppRequest updateAppRequest) throws ClientException {
		String appId = updateAppRequest.getAppId();
		String appVersion = updateAppRequest.getAppVersion();
		if (!this.client.exists(appId, appVersion)) {
			throw new ClientException(400, String.format("App with appId '%s' and version '%s' does not exists.", appId, appVersion));
		}

		StatusDTO status = this.client.update(toDTO(updateAppRequest));
		if (status != null && status.success()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(String appId, String appVersion) throws ClientException {
		try {
			StatusDTO status = this.client.delete(appId, appVersion);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean uploadAppArchive(int id, String appId, String appVersion, Path filePath) throws ClientException {
		StatusDTO status = this.client.upload(id, appId, appVersion, filePath);
		if (status != null && status.success()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean downloadAppArchive(String appId, String appVersion, OutputStream output) throws ClientException {
		boolean succeed = false;
		try {
			succeed = this.client.download(appId, appVersion, output);
		} catch (ClientException e) {
			throw e;
		}
		return succeed;
	}

	// ------------------------------------------------------------------------------------------------
	// Helper methods
	// ------------------------------------------------------------------------------------------------
	/**
	 * Get web service client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	protected WSClientConfiguration getClientConfiguration(Map<String, Object> properties) {
		String realm = (String) properties.get(WSClientConstants.REALM);
		String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
		String url = (String) properties.get(InfraConstants.SERVICE__HOST_URL);
		String contextRoot = (String) properties.get(InfraConstants.SERVICE__CONTEXT_ROOT);

		WSClientConfiguration config = WSClientConfiguration.create(realm, accessToken, url, contextRoot);
		return config;
	}

	/**
	 * Convert CreateAppRequest to DTO.
	 * 
	 * @param createAppRequest
	 * @return
	 */
	protected AppManifestDTO toDTO(CreateAppRequest createAppRequest) {
		AppManifestDTO DTO = new AppManifestDTO();
		DTO.setAppId(createAppRequest.getAppId());
		DTO.setAppVersion(createAppRequest.getAppVersion());
		DTO.setType(createAppRequest.getType());
		DTO.setName(createAppRequest.getName());
		DTO.setAppManifest(createAppRequest.getManifest());
		DTO.setAppFileName(createAppRequest.getFileName());
		DTO.setDescription(createAppRequest.getDescription());
		return DTO;
	}

	/**
	 * Convert UpdateAppRequest to DTO.
	 * 
	 * @param updateAppRequest
	 * @return
	 */
	protected AppManifestDTO toDTO(UpdateAppRequest updateAppRequest) {
		AppManifestDTO DTO = new AppManifestDTO();
		DTO.setAppId(updateAppRequest.getAppId());
		DTO.setAppVersion(updateAppRequest.getAppVersion());
		DTO.setType(updateAppRequest.getType());
		DTO.setName(updateAppRequest.getName());
		DTO.setAppManifest(updateAppRequest.getManifest());
		DTO.setAppFileName(updateAppRequest.getFileName());
		DTO.setDescription(updateAppRequest.getDescription());
		return DTO;
	}

	/**
	 * Convert AppQuery to DTO.
	 * 
	 * @param query
	 */
	protected AppQueryDTO toDTO(AppQuery query) {
		AppQueryDTO queryDTO = new AppQueryDTO();
		queryDTO.setAppId(query.getAppId());
		queryDTO.setName(query.getName());
		queryDTO.setAppVersion(query.getAppVersion());
		queryDTO.setType(query.getType());
		queryDTO.setDescription(query.getDescription());

		queryDTO.setAppId_oper(SQLWhereOperator.isEqual(query.getAppId_oper()) ? null : query.getAppId_oper());
		queryDTO.setName_oper(SQLWhereOperator.isEqual(query.getName_oper()) ? null : query.getName_oper());
		queryDTO.setAppVersion_oper(SQLWhereOperator.isEqual(query.getAppVersion_oper()) ? null : query.getAppVersion_oper());
		queryDTO.setType_oper(SQLWhereOperator.isEqual(query.getType_oper()) ? null : query.getType_oper());
		queryDTO.setDescription_oper(SQLWhereOperator.isEqual(query.getDescription_oper()) ? null : query.getDescription_oper());

		return queryDTO;
	}

	/**
	 * Convert DTO to AppManifestImpl.
	 * 
	 * @param dto
	 */
	public AppManifestImpl toAppManifestImpl(AppManifestDTO dto) {
		AppManifestImpl app = new AppManifestImpl();
		app.setAppId(dto.getAppId());
		app.setAppVersion(dto.getAppVersion());
		app.setType(dto.getType());
		app.setName(dto.getName());
		app.setManifest(dto.getAppManifest());
		app.setFileName(dto.getAppFileName());
		app.setDescription(dto.getDescription());
		app.setDateCreated(new Date(dto.getDateCreated()));
		app.setDateModified(new Date(dto.getDateModified()));
		return app;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

	@Override
	public String echo(String message) throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response sendRequest(Request request) throws ClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isProxy() {
		// TODO Auto-generated method stub
		return false;
	}

}

// protected String loadBalanceId;
// protected Properties loadBalanceProperties;
/// **
// * Get app store client configuration.
// *
// * @param properties
// * @return
// */
// public static ClientConfiguration getClientConfiguration(String url, String contextRoot, String username, String password) {
// return ClientConfiguration.get(url, contextRoot, username, password);
// }
/// **
// * Create app store connection properties.
// *
// * @param driver
// * @param url
// * @param username
// * @param password
// * @return
// */
// public static Properties getProperties(String url, String contextRoot, String username, String password) {
// Properties properties = new Properties();
// properties.setProperty(OrbitConstants.APPSTORE_URL, url);
// properties.setProperty(OrbitConstants.APPSTORE_CONTEXT_ROOT, contextRoot);
// properties.setProperty(OrbitConstants.APPSTORE_USERNAME, username);
// properties.setProperty(OrbitConstants.APPSTORE_PASSWORD, password);
// return properties;
// }
// System.out.println("AppStoreImpl.update(Map)");
// System.out.println("----------------------------------------");
// Printer.pl(properties);
// System.out.println("----------------------------------------");
// /**
// *
// * @param properties
// * @return
// */
// protected String getLoadBalanceId(Map<String, Object> properties) {
// String appStoreName = (String) properties.get(OrbitConstants.APPSTORE_NAME);
// String url = (String) properties.get(OrbitConstants.APPSTORE_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.APPSTORE_CONTEXT_ROOT);
// String key = url + "::" + contextRoot + "::" + appStoreName;
// return key;
// }
