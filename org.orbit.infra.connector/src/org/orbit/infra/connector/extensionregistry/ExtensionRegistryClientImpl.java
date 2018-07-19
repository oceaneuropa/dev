package org.orbit.infra.connector.extensionregistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.infra.connector.InfraConstants;
import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.service.InternalProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtensionRegistryClientImpl implements ExtensionRegistryClient, InternalProxyService {

	protected static Logger LOG = LoggerFactory.getLogger(ExtensionRegistryClientImpl.class);

	protected Map<String, Object> properties;
	protected ExtensionRegistryWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();
	protected boolean isProxy = false;

	/**
	 * 
	 * @param config
	 */
	public ExtensionRegistryClientImpl(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	private Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	public void update(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	protected void initClient() {
		String realm = (String) this.properties.get(InfraConstants.REALM);
		String username = (String) this.properties.get(InfraConstants.USERNAME);
		String url = (String) this.properties.get(InfraConstants.URL);

		String orbitRealm = (String) this.properties.get(InfraConstants.ORBIT_REALM);
		if (realm == null && orbitRealm != null) {
			realm = orbitRealm;
		}

		String orbitExtensionRegistryServiceURL = (String) this.properties.get(InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);
		if (url == null && orbitExtensionRegistryServiceURL != null) {
			url = orbitExtensionRegistryServiceURL;
		}

		ClientConfiguration clientConfig = ClientConfiguration.create(realm, username, url);
		this.client = new ExtensionRegistryWSClient(clientConfig);
	}

	protected ExtensionRegistryWSClient getClient() {
		return this.client;
	}

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public String echo(String message) throws IOException {
		try {
			return this.client.echo(message);
		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(InfraConstants.EXTENSION_REGISTRY_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String hostURL = (String) this.properties.get(InfraConstants.EXTENSION_REGISTRY_HOST_URL);
		String contextRoot = (String) this.properties.get(InfraConstants.EXTENSION_REGISTRY_CONTEXT_ROOT);
		return hostURL + contextRoot;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	/**
	 * 
	 * @param DTO
	 * @return
	 */
	protected ExtensionItem toExtensionItem(ExtensionItemDTO DTO) {
		// Integer currId = DTO.getId();
		String currPlatformId = DTO.getPlatformId();
		String currTypeId = DTO.getTypeId();
		String currExtensionId = DTO.getExtensionId();
		String currName = DTO.getName();
		String currDesc = DTO.getDescription();
		Map<String, Object> currProperties = DTO.getProperties();

		// ExtensionItem item = new ExtensionItemImpl(currId, currPlatformId, currTypeId, currExtensionId, currName, currDesc, currProperties);
		ExtensionItem item = new ExtensionItemImpl(currPlatformId, currTypeId, currExtensionId, currName, currDesc, currProperties);
		return item;
	}

	protected boolean succeed(StatusDTO status) {
		if (status != null && status.success()) {
			return true;
		}
		return false;
	}

	@Override
	public List<ExtensionItem> getExtensionItems(String platformId) throws IOException {
		List<ExtensionItem> items = new ArrayList<ExtensionItem>();
		try {
			List<ExtensionItemDTO> DTOs = getClient().getExtensionItems(platformId, null);
			for (ExtensionItemDTO DTO : DTOs) {
				ExtensionItem item = toExtensionItem(DTO);
				items.add(item);
			}
		} catch (ClientException e) {
			throw new IOException(e);
		}
		return items;
	}

	@Override
	public List<ExtensionItem> getExtensionItems(String platformId, String typeId) throws IOException {
		List<ExtensionItem> items = new ArrayList<ExtensionItem>();
		try {
			List<ExtensionItemDTO> DTOs = getClient().getExtensionItems(platformId, typeId);
			for (ExtensionItemDTO DTO : DTOs) {
				ExtensionItem item = toExtensionItem(DTO);
				items.add(item);
			}
		} catch (ClientException e) {
			throw new IOException(e);
		}
		return items;
	}

	@Override
	public ExtensionItem getExtensionItem(String platformId, String typeId, String extensionId) throws IOException {
		ExtensionItem item = null;
		try {
			ExtensionItemDTO DTO = getClient().getExtensionItem(platformId, typeId, extensionId);
			if (DTO != null) {
				item = toExtensionItem(DTO);
			}
		} catch (ClientException e) {
			throw new IOException(e);
		}
		return item;
	}

	@Override
	public ExtensionItem addExtensionItem(String platformId, String typeId, String extensionId, String name, String description, Map<String, Object> properties) throws IOException {
		ExtensionItem newItem = null;
		try {
			ExtensionItemDTO newDTO = getClient().addExtensionItem(platformId, typeId, extensionId, name, description, properties);
			if (newDTO != null) {
				newItem = toExtensionItem(newDTO);
			}
		} catch (ClientException e) {
			throw new IOException(e);
		}
		return newItem;
	}

	@Override
	public boolean updateExtensionItem(String platformId, String typeId, String extensionId, String newTypeId, String newExtensionId, String newName, String newDescription, Map<String, Object> properties) throws IOException {
		try {
			StatusDTO status = getClient().updateExtensionItem(platformId, typeId, extensionId, newTypeId, newExtensionId, newName, newDescription, properties);
			return succeed(status);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public boolean removeExtensionItems(String platformId) throws IOException {
		try {
			StatusDTO status = getClient().removeExtensionItems(platformId);
			return succeed(status);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public boolean removeExtensionItem(String platformId, String typeId, String extensionId) throws IOException {
		try {
			StatusDTO status = getClient().removeExtensionItem(platformId, typeId, extensionId);
			return succeed(status);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Map<String, Object> getExtensionProperties(String platformId, String typeId, String extensionId) throws IOException {
		try {
			return getClient().getExtensionProperties(platformId, typeId, extensionId);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public boolean setExtensionProperties(String platformId, String typeId, String extensionId, Map<String, Object> properties) throws IOException {
		try {
			StatusDTO status = getClient().setExtensionProperties(platformId, typeId, extensionId, properties);
			return succeed(status);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public boolean removeExtensionProperties(String platformId, String typeId, String extensionId, List<String> propertyNames) throws IOException {
		try {
			StatusDTO status = getClient().removeExtensionProperties(platformId, typeId, extensionId, propertyNames);
			return succeed(status);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
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
	public boolean isProxy() {
		return this.isProxy;
	}

	@Override
	public void setProxy(boolean isProxy) {
		this.isProxy = isProxy;
	}

}
