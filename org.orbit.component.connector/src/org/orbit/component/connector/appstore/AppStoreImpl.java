package org.orbit.component.connector.appstore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.orbit.component.api.appstore.AppManifest;
import org.orbit.component.api.appstore.AppQuery;
import org.orbit.component.api.appstore.AppStore;
import org.orbit.component.api.appstore.CreateAppRequest;
import org.orbit.component.connector.appstore.ws.AppStoreWSClient;
import org.orbit.component.model.appstore.dto.AppManifestDTO;
import org.orbit.component.model.appstore.dto.AppQueryDTO;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;

/**
 * App store client connector implementation.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 * 
 */
public class AppStoreImpl implements AppStore {

	protected static final AppManifest[] EMPTY_APPS = new AppManifest[0];

	protected Properties properties;
	protected AppStoreWSClient client;

	protected String loadBalanceId;
	protected Properties loadBalanceProperties;

	/**
	 * 
	 * @param properties
	 */
	public AppStoreImpl(Properties properties) {
		this.properties = properties;
		this.loadBalanceId = AppStoreUtil.getLoadBalanceId(this.properties);
		initClient();
	}

	protected void initClient() {
		ClientConfiguration clientConfig = AppStoreUtil.getClientConfiguration(this.properties);
		this.client = new AppStoreWSClient(clientConfig);
	}

	/**
	 * 
	 * @return
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Set properties to the app store impl triggers the re-initialization of the app store client.
	 * 
	 * @param properties
	 */
	public void update(Properties properties) {
		this.properties = properties;
		this.loadBalanceId = AppStoreUtil.getLoadBalanceId(this.properties);
		initClient();
	}

	// ------------------------------------------------------------------------------------------------
	// getApps(AppQuery query)
	// getApp(String appId)
	// exists(String appId)
	// create(CreateAppRequest createAppRequest)
	// create(CreateAppRequest createAppRequest, File file)
	// upload(String appId, File file)
	// download(String appId, File file)
	// download(String appId, OutputStream output)
	// delete(String appId)
	// ------------------------------------------------------------------------------------------------
	@Override
	public AppManifest[] getApps(AppQuery query) throws ClientException {
		List<AppManifest> apps = new ArrayList<AppManifest>();
		try {
			AppQueryDTO queryDTO = new AppQueryDTO(query);
			List<AppManifestDTO> appDTOs = this.client.getApps(queryDTO);
			for (AppManifestDTO appDTO : appDTOs) {
				apps.add(new AppManifestImpl(appDTO));
			}
		} catch (ClientException e) {
			throw e;
		}
		return apps.toArray(new AppManifest[apps.size()]);
	}

	@Override
	public AppManifest getApp(String appId) throws ClientException {
		AppManifest app = null;
		try {
			AppManifestDTO appDTO = this.client.getApp(appId);
			if (appDTO != null) {
				app = new AppManifestImpl(appDTO);
			}
		} catch (ClientException e) {
			throw e;
		}
		return app;
	}

	@Override
	public boolean exists(String appId) throws ClientException {
		try {
			return this.client.appExists(appId);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean create(CreateAppRequest createAppRequest) throws ClientException {
		String appId = createAppRequest.getAppId();
		if (this.client.appExists(appId)) {
			throw new ClientException(400, String.format("App '%s' already exists ", appId));
		}

		AppManifestDTO newAppDTO = this.client.addApp(new AppManifestDTO(createAppRequest));
		if (newAppDTO != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean create(CreateAppRequest createAppRequest, File file) throws ClientException {
		String appId = createAppRequest.getAppId();
		if (this.client.appExists(appId)) {
			throw new ClientException(400, String.format("App '%s' already exists ", appId));
		}

		if (createAppRequest.getFileName() == null && file != null) {
			createAppRequest.setFileName(file.getName());
		}

		AppManifestDTO newAppDTO = this.client.addApp(new AppManifestDTO(createAppRequest));
		if (newAppDTO != null && file != null) {
			return upload(newAppDTO.getAppId(), file);
		}
		return false;
	}

	@Override
	public boolean upload(String appId, File file) throws ClientException {
		StatusDTO status = this.client.uploadApp(appId, file);
		if (status != null && status.success()) {
			return true;
		}
		return false;
	}

	@Override
	public void download(String appId, File file) throws ClientException {
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			download(appId, output);
		} catch (ClientException e) {
			throw e;
		} catch (IOException e) {
			throw new ClientException(500, e.getMessage(), e);
		} finally {
			IOUtil.closeQuietly(output, true);
		}
	}

	@Override
	public void download(String appId, OutputStream output) throws ClientException {
		try {
			this.client.downloadApp(appId, output);
		} catch (ClientException e) {
			throw e;
		}
	}

	@Override
	public boolean delete(String appId) throws ClientException {
		try {
			StatusDTO status = this.client.deleteApp(appId);
			if (status != null && status.success()) {
				return true;
			}
		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

}
