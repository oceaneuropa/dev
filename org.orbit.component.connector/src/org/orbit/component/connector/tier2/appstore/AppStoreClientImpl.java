package org.orbit.component.connector.tier2.appstore;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppQuery;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.api.tier2.appstore.CreateAppRequest;
import org.orbit.component.api.tier2.appstore.UpdateAppRequest;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.util.ModelConverter;
import org.orbit.component.model.tier2.appstore.AppManifestDTO;
import org.orbit.component.model.tier2.appstore.AppQueryDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.model.StatusDTO;

/**
 * App store client connector implementation.
 * 
 */
public class AppStoreClientImpl implements AppStoreClient {

	protected Map<String, Object> properties;
	protected AppStoreWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public AppStoreClientImpl(ServiceConnector<AppStoreClient> connector, Map<String, Object> properties) {
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
		String realm = (String) this.properties.get(OrbitConstants.REALM);
		String username = (String) this.properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) this.properties.get(OrbitConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		this.client = new AppStoreWSClient(config);
	}

	// ------------------------------------------------------------------------------------------------
	// Configuration methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.APPSTORE_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String fullUrl = (String) properties.get(OrbitConstants.URL);
		return fullUrl;
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
			AppQueryDTO queryDTO = ModelConverter.AppStore.toDTO(query);
			List<AppManifestDTO> appDTOs = this.client.getApps(queryDTO);
			for (AppManifestDTO appDTO : appDTOs) {
				apps.add(ModelConverter.AppStore.toApp(appDTO));
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
			AppManifestDTO appDTO = this.client.getApp(appId, appVersion);
			if (appDTO != null) {
				app = ModelConverter.AppStore.toApp(appDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return app;
	}

	@Override
	public boolean exists(String appId, String appVersion) throws ClientException {
		try {
			return this.client.appExists(appId, appVersion);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean create(CreateAppRequest createAppRequest) throws ClientException {
		String appId = createAppRequest.getAppId();
		String appVersion = createAppRequest.getAppVersion();
		if (this.client.appExists(appId, appVersion)) {
			throw new ClientException(400, String.format("App with appId '%s' and appVersion '%s' already exists.", appId, appVersion));
		}

		AppManifestDTO newAppDTO = this.client.addApp(ModelConverter.AppStore.toDTO(createAppRequest));
		if (newAppDTO != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean create(CreateAppRequest createAppRequest, Path filePath) throws ClientException {
		String appId = createAppRequest.getAppId();
		String appVersion = createAppRequest.getAppVersion();
		if (this.client.appExists(appId, appVersion)) {
			throw new ClientException(400, String.format("App with appId '%s' and appVersion '%s' already exists.", appId, appVersion));
		}

		if (createAppRequest.getFileName() == null && filePath != null) {
			createAppRequest.setFileName(filePath.getFileName().toString());
		}

		AppManifestDTO newAppDTO = this.client.addApp(ModelConverter.AppStore.toDTO(createAppRequest));

		if (newAppDTO != null && filePath != null) {
			return uploadAppArchive(-1, newAppDTO.getAppId(), newAppDTO.getAppVersion(), filePath);
		}
		return false;
	}

	@Override
	public boolean update(UpdateAppRequest updateAppRequest) throws ClientException {
		// String appId = updateAppRequest.getAppId();
		// String appVersion = updateAppRequest.getAppVersion();
		// if (!this.client.appExists(appId, appVersion)) {
		// throw new ClientException(400, String.format("App with appId '%s' and appVersion '%s' does not exists.", appId, appVersion));
		// }
		StatusDTO status = this.client.updateApp(ModelConverter.AppStore.toDTO(updateAppRequest));
		if (status != null && status.success()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean delete(String appId, String appVersion) throws ClientException {
		try {
			StatusDTO status = this.client.deleteApp(appId, appVersion);
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
		StatusDTO status = this.client.uploadAppArchive(id, appId, appVersion, filePath);
		if (status != null && status.success()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean downloadAppArchive(String appId, String appVersion, OutputStream output) throws ClientException {
		boolean succeed = false;
		try {
			succeed = this.client.downloadAppArchive(appId, appVersion, output);
		} catch (ClientException e) {
			throw e;
		}
		return succeed;
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

}

// /**
// * Get web service client configuration.
// *
// * @param properties
// * @return
// */
// protected ClientConfiguration getClientConfiguration(Map<String, Object> properties) {
// String realm = (String) properties.get(OrbitConstants.REALM);
// String username = (String) properties.get(OrbitConstants.USERNAME);
// String url = (String) properties.get(OrbitConstants.APPSTORE_HOST_URL);
// String contextRoot = (String) properties.get(OrbitConstants.APPSTORE_CONTEXT_ROOT);
//
// return ClientConfiguration.create(realm, username, url, contextRoot);
// }
