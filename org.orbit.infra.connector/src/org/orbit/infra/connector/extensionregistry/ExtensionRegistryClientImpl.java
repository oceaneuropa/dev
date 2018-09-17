package org.orbit.infra.connector.extensionregistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.infra.model.extensionregistry.ExtensionItemDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.StatusDTO;

public class ExtensionRegistryClientImpl extends ServiceClientImpl<ExtensionRegistryClient, ExtensionRegistryWSClient> implements ExtensionRegistryClient {

	/**
	 * 
	 * @param config
	 */
	public ExtensionRegistryClientImpl(ServiceConnector<ExtensionRegistryClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected ExtensionRegistryWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration clientConfig = WSClientConfiguration.create(this.properties);
		return new ExtensionRegistryWSClient(clientConfig);
	}

	@Override
	public List<ExtensionItem> getExtensionItems(String platformId) throws IOException {
		List<ExtensionItem> items = new ArrayList<ExtensionItem>();
		try {
			List<ExtensionItemDTO> DTOs = getWSClient().getExtensionItems(platformId, null);
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
			List<ExtensionItemDTO> DTOs = getWSClient().getExtensionItems(platformId, typeId);
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
			ExtensionItemDTO DTO = getWSClient().getExtensionItem(platformId, typeId, extensionId);
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
			ExtensionItemDTO newDTO = getWSClient().addExtensionItem(platformId, typeId, extensionId, name, description, properties);
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
			StatusDTO status = getWSClient().updateExtensionItem(platformId, typeId, extensionId, newTypeId, newExtensionId, newName, newDescription, properties);
			return succeed(status);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public boolean removeExtensionItems(String platformId) throws IOException {
		try {
			StatusDTO status = getWSClient().removeExtensionItems(platformId);
			return succeed(status);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public boolean removeExtensionItem(String platformId, String typeId, String extensionId) throws IOException {
		try {
			StatusDTO status = getWSClient().removeExtensionItem(platformId, typeId, extensionId);
			return succeed(status);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Map<String, Object> getExtensionProperties(String platformId, String typeId, String extensionId) throws IOException {
		try {
			return getWSClient().getExtensionProperties(platformId, typeId, extensionId);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public boolean setExtensionProperties(String platformId, String typeId, String extensionId, Map<String, Object> properties) throws IOException {
		try {
			StatusDTO status = getWSClient().setExtensionProperties(platformId, typeId, extensionId, properties);
			return succeed(status);

		} catch (ClientException e) {
			throw new IOException(e);
		}
	}

	@Override
	public boolean removeExtensionProperties(String platformId, String typeId, String extensionId, List<String> propertyNames) throws IOException {
		try {
			StatusDTO status = getWSClient().removeExtensionProperties(platformId, typeId, extensionId, propertyNames);
			return succeed(status);

		} catch (ClientException e) {
			throw new IOException(e);
		}
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

}

// protected static Logger LOG = LoggerFactory.getLogger(ExtensionRegistryClientImpl.class);
