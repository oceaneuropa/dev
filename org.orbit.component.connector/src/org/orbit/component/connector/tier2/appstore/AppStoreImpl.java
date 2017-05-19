package org.orbit.component.connector.tier2.appstore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier2.appstore.request.AppQuery;
import org.orbit.component.api.tier2.appstore.request.CreateAppRequest;
import org.orbit.component.api.tier2.appstore.request.UpdateAppRequest;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.model.tier2.appstore.AppManifestDTO;
import org.orbit.component.model.tier2.appstore.AppQueryDTO;
import org.origin.common.io.IOUtil;
import org.origin.common.jdbc.SQLWhereOperator;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.util.StringUtil;

/**
 * App store client connector implementation.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 * 
 */
public class AppStoreImpl implements AppStore {

	protected Map<String, Object> properties;
	protected AppStoreWSClient client;

	/**
	 * 
	 * @param properties
	 */
	public AppStoreImpl(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
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
		String hostURL = (String) this.properties.get(OrbitConstants.APPSTORE_HOST_URL);
		String contextRoot = (String) this.properties.get(OrbitConstants.APPSTORE_CONTEXT_ROOT);
		return hostURL + contextRoot;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	private Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	/**
	 * Update properties. Re-initiate web service client if host URL or context root is changed.
	 * 
	 * @param properties
	 */
	@Override
	public void update(Map<String, Object> properties) {
		String oldUrl = (String) this.properties.get(OrbitConstants.APPSTORE_HOST_URL);
		String oldContextRoot = (String) this.properties.get(OrbitConstants.APPSTORE_CONTEXT_ROOT);

		properties = checkProperties(properties);
		this.properties.putAll(properties);

		String newUrl = (String) properties.get(OrbitConstants.APPSTORE_HOST_URL);
		String newContextRoot = (String) properties.get(OrbitConstants.APPSTORE_CONTEXT_ROOT);

		boolean reinitClient = false;
		if (!StringUtil.equals(oldUrl, newUrl) || !StringUtil.equals(oldContextRoot, newContextRoot)) {
			reinitClient = true;
		}
		if (reinitClient) {
			initClient();
		}
	}

	protected void initClient() {
		ClientConfiguration clientConfig = getClientConfiguration(this.properties);
		this.client = new AppStoreWSClient(clientConfig);
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
			List<AppManifestDTO> appDTOs = this.client.getApps(queryDTO);
			for (AppManifestDTO appDTO : appDTOs) {
				apps.add(toAppManifestImpl(appDTO));
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
				app = toAppManifestImpl(appDTO);
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
			throw new ClientException(400, String.format("App with appId '%s' already exists.", appId));
		}

		AppManifestDTO newAppDTO = this.client.addApp(toDTO(createAppRequest));
		if (newAppDTO != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean create(CreateAppRequest createAppRequest, File file) throws ClientException {
		String appId = createAppRequest.getAppId();
		if (this.client.appExists(appId)) {
			throw new ClientException(400, String.format("App with appId '%s' already exists.", appId));
		}

		if (createAppRequest.getFileName() == null && file != null) {
			createAppRequest.setFileName(file.getName());
		}

		AppManifestDTO newAppDTO = this.client.addApp(toDTO(createAppRequest));

		if (newAppDTO != null && file != null) {
			return upload(newAppDTO.getAppId(), file);
		}
		return false;
	}

	@Override
	public boolean update(UpdateAppRequest updateAppRequest) throws ClientException {
		String appId = updateAppRequest.getAppId();
		if (!this.client.appExists(appId)) {
			throw new ClientException(400, String.format("App with appId '%s' does not exists.", appId));
		}

		StatusDTO status = this.client.updateApp(toDTO(updateAppRequest));
		if (status != null && status.success()) {
			return true;
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

	// ------------------------------------------------------------------------------------------------
	// Helper methods
	// ------------------------------------------------------------------------------------------------
	/**
	 * Get web service client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	protected ClientConfiguration getClientConfiguration(Map<String, Object> properties) {
		String url = (String) properties.get(OrbitConstants.APPSTORE_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.APPSTORE_CONTEXT_ROOT);
		return ClientConfiguration.get(url, contextRoot, null, null);
	}

	/**
	 * Convert CreateAppRequest to DTO.
	 * 
	 * @param createAppRequest
	 * @return
	 */
	protected AppManifestDTO toDTO(CreateAppRequest createAppRequest) {
		AppManifestDTO createAppRequestDTO = new AppManifestDTO();
		createAppRequestDTO.setAppId(createAppRequest.getAppId());
		createAppRequestDTO.setNamespace(createAppRequest.getNamespace());
		createAppRequestDTO.setCategoryId(createAppRequest.getCategoryId());
		createAppRequestDTO.setName(createAppRequest.getName());
		createAppRequestDTO.setVersion(createAppRequest.getVersion());
		createAppRequestDTO.setPriority(createAppRequest.getPriority());
		createAppRequestDTO.setAppManifest(createAppRequest.getManifest());
		createAppRequestDTO.setFileName(createAppRequest.getFileName());
		createAppRequestDTO.setDescription(createAppRequest.getDescription());
		return createAppRequestDTO;
	}

	/**
	 * Convert UpdateAppRequest to DTO.
	 * 
	 * @param updateAppRequest
	 * @return
	 */
	protected AppManifestDTO toDTO(UpdateAppRequest updateAppRequest) {
		AppManifestDTO updateAppRequestDTO = new AppManifestDTO();
		updateAppRequestDTO.setAppId(updateAppRequest.getAppId());
		updateAppRequestDTO.setNamespace(updateAppRequest.getNamespace());
		updateAppRequestDTO.setCategoryId(updateAppRequest.getCategoryId());
		updateAppRequestDTO.setName(updateAppRequest.getName());
		updateAppRequestDTO.setVersion(updateAppRequest.getVersion());
		updateAppRequestDTO.setPriority(updateAppRequest.getPriority());
		updateAppRequestDTO.setAppManifest(updateAppRequest.getManifest());
		updateAppRequestDTO.setFileName(updateAppRequest.getFileName());
		updateAppRequestDTO.setDescription(updateAppRequest.getDescription());

		return updateAppRequestDTO;
	}

	/**
	 * Convert AppQuery to DTO.
	 * 
	 * @param query
	 */
	protected AppQueryDTO toDTO(AppQuery query) {
		AppQueryDTO queryDTO = new AppQueryDTO();
		queryDTO.setAppId(query.getAppId());
		queryDTO.setNamespace(query.getNamespace());
		queryDTO.setCategoryId(query.getCategoryId());
		queryDTO.setName(query.getName());
		queryDTO.setVersion(query.getVersion());
		queryDTO.setDescription(query.getDescription());

		queryDTO.setAppId_oper(SQLWhereOperator.isEqual(query.getAppId_oper()) ? null : query.getAppId_oper());
		queryDTO.setNamespace_oper(SQLWhereOperator.isEqual(query.getNamespace_oper()) ? null : query.getNamespace_oper());
		queryDTO.setCategoryId_oper(SQLWhereOperator.isEqual(query.getCategoryId_oper()) ? null : query.getCategoryId_oper());
		queryDTO.setName_oper(SQLWhereOperator.isEqual(query.getName_oper()) ? null : query.getName_oper());
		queryDTO.setVersion_oper(SQLWhereOperator.isEqual(query.getVersion_oper()) ? null : query.getVersion_oper());
		queryDTO.setDescription_oper(SQLWhereOperator.isEqual(query.getDescription_oper()) ? null : query.getDescription_oper());

		return queryDTO;
	}

	/**
	 * Convert DTO to AppManifestImpl.
	 * 
	 * @param dto
	 */
	protected AppManifestImpl toAppManifestImpl(AppManifestDTO dto) {
		AppManifestImpl impl = new AppManifestImpl();
		impl.setAppId(dto.getAppId());
		impl.setNamespace(dto.getNamespace());
		impl.setCategoryId(dto.getCategoryId());
		impl.setName(dto.getName());
		impl.setVersion(dto.getVersion());
		impl.setPriority(dto.getPriority());
		impl.setManifestString(dto.getAppManifest());
		impl.setFileName(dto.getFileName());
		impl.setDescription(dto.getDescription());
		impl.setDateCreated(new Date(dto.getDateCreated()));
		impl.setDateModified(new Date(dto.getDateModified()));
		return impl;
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
