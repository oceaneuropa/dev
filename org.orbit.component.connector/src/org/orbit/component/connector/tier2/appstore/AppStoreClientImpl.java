package org.orbit.component.connector.tier2.appstore;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppQuery;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.api.tier2.appstore.CreateAppRequest;
import org.orbit.component.api.tier2.appstore.UpdateAppRequest;
import org.orbit.component.connector.util.ClientModelConverter;
import org.orbit.component.model.tier2.appstore.AppManifestDTO;
import org.orbit.component.model.tier2.appstore.AppQueryDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.StatusDTO;

/**
 * App store client implementation.
 * 
 */
public class AppStoreClientImpl extends ServiceClientImpl<AppStoreClient, AppStoreWSClient> implements AppStoreClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public AppStoreClientImpl(ServiceConnector<AppStoreClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected AppStoreWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new AppStoreWSClient(config);
	}

	@Override
	public AppManifest[] getApps(AppQuery query) throws ClientException {
		List<AppManifest> apps = new ArrayList<AppManifest>();
		try {
			AppQueryDTO queryDTO = ClientModelConverter.AppStore.toDTO(query);
			List<AppManifestDTO> appDTOs = this.client.getList(queryDTO);
			for (AppManifestDTO appDTO : appDTOs) {
				apps.add(ClientModelConverter.AppStore.toApp(appDTO));
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
				app = ClientModelConverter.AppStore.toApp(appDTO);
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

		AppManifestDTO newAppDTO = this.client.create(ClientModelConverter.AppStore.toDTO(createAppRequest));
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

		AppManifestDTO newAppDTO = this.client.create(ClientModelConverter.AppStore.toDTO(createAppRequest));

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
		StatusDTO status = this.client.update(ClientModelConverter.AppStore.toDTO(updateAppRequest));
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
