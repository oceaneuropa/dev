package other.orbit.component.connector.tier1.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.api.tier1.registry.Registry;
import org.orbit.component.api.tier1.registry.EPath;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.tier1.config.ConfigRegistryWSClient;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.StringUtil;

public class ConfigRegistryImplV1 implements Registry {

	protected Map<String, Object> properties;
	protected ConfigRegistryWSClient client;

	protected String loadBalanceId;
	protected Properties loadBalanceProperties;

	/**
	 * 
	 * @param properties
	 */
	public ConfigRegistryImplV1(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		this.properties = properties;
		this.loadBalanceId = getLoadBalanceId(this.properties);
		initClient();
	}

	// ------------------------------------------------------------------------------------------------
	// Configuration methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.CONFIG_REGISTRY_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String hostURL = (String) this.properties.get(OrbitConstants.CONFIG_REGISTRY_HOST_URL);
		String contextRoot = (String) this.properties.get(OrbitConstants.CONFIG_REGISTRY_CONTEXT_ROOT);
		return hostURL + contextRoot;
	}

	protected void initClient() {
		// ClientConfiguration clientConfig = getClientConfiguration(this.properties);
		// this.client = new ConfigRegistryWSClient(clientConfig);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	/**
	 * Set properties to the app store impl triggers the re-initialization of the app store client.
	 * 
	 * @param properties
	 */
	@Override
	public void update(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}

		String oldUrl = (String) this.properties.get(OrbitConstants.APPSTORE_HOST_URL);
		String oldContextRoot = (String) this.properties.get(OrbitConstants.APPSTORE_CONTEXT_ROOT);

		this.properties = properties;
		this.loadBalanceId = getLoadBalanceId(this.properties);

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

	private String getLoadBalanceId(Map<String, Object> properties2) {
		return null;
	}

	// ------------------------------------------------------------------------------------------------
	// Web service client methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public Map<String, String> getProperties(String userId, EPath path) throws ClientException {
		return null;
	}

	@Override
	public String getProperty(String userId, EPath path, String key) throws ClientException {
		return null;
	}

	@Override
	public void setProperties(String userId, EPath path, Map<String, String> properties) throws ClientException {

	}

	@Override
	public void setProperty(String userId, EPath path, String key, String value) throws ClientException {

	}

	@Override
	public void removeProperty(String userId, EPath path, String key) throws ClientException {

	}

	@Override
	public void removeAll(String userId, EPath path) throws ClientException {

	}

	@Override
	public void removeAll(String userId) throws ClientException {

	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {

	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {

	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public boolean close() throws ClientException {
		return false;
	}

}
