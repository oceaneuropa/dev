package org.orbit.infra.api.extensionregistry;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraClients;

public class ExtensionRegistryClientProxy implements ExtensionRegistryClient {

	private static ExtensionRegistryClientProxyImpl PROXY = new ExtensionRegistryClientProxyImpl();

	protected Map<?, ?> properties;
	protected String realm;
	protected String username;
	protected String url;

	protected ExtensionRegistryClient extensionRegistry = PROXY;

	public ExtensionRegistryClientProxy(Map<?, ?> properties) {
		this.properties = properties;
	}

	public ExtensionRegistryClientProxy(String url) {
		this(null, null, url);
	}

	public ExtensionRegistryClientProxy(String realm, String username, String url) {
		this.realm = realm;
		this.username = username;
		this.url = url;
	}

	protected synchronized ExtensionRegistryClient resolve() {
		if (this.extensionRegistry == null || this.extensionRegistry.isProxy()) {
			ExtensionRegistryClient resolvedExtensionRegistry = null;
			if (this.properties != null) {
				resolvedExtensionRegistry = InfraClients.getInstance().getExtensionRegistry(this.properties);
			}
			if (resolvedExtensionRegistry == null) {
				if (this.url != null) {
					resolvedExtensionRegistry = InfraClients.getInstance().getExtensionRegistry(this.realm, this.username, this.url);
				}
			}
			if (resolvedExtensionRegistry != null && !resolvedExtensionRegistry.isProxy()) {
				this.extensionRegistry = resolvedExtensionRegistry;
			}
		}
		if (this.extensionRegistry == null) {
			this.extensionRegistry = PROXY;
		}
		return this.extensionRegistry;
	}

	@Override
	public Map<String, Object> getProperties() {
		return resolve().getProperties();
	}

	@Override
	public String getName() {
		return resolve().getName();
	}

	@Override
	public String getURL() {
		return resolve().getURL();
	}

	@Override
	public boolean ping() {
		return resolve().ping();
	}

	@Override
	public String echo(String message) throws IOException {
		return resolve().echo(message);
	}

	@Override
	public List<ExtensionItem> getExtensionItems(String platformId) throws IOException {
		return resolve().getExtensionItems(platformId);
	}

	@Override
	public List<ExtensionItem> getExtensionItems(String platformId, String typeId) throws IOException {
		return resolve().getExtensionItems(platformId, typeId);
	}

	@Override
	public ExtensionItem getExtensionItem(String platformId, String typeId, String extensionId) throws IOException {
		return resolve().getExtensionItem(platformId, typeId, extensionId);
	}

	@Override
	public ExtensionItem addExtensionItem(String platformId, String typeId, String extensionId, String name, String description, Map<String, Object> properties) throws IOException {
		return resolve().addExtensionItem(platformId, typeId, extensionId, name, description, properties);
	}

	@Override
	public boolean updateExtensionItem(String platformId, String typeId, String extensionId, String newTypeId, String newExtensionId, String newName, String newDescription, Map<String, Object> properties) throws IOException {
		return resolve().updateExtensionItem(platformId, typeId, extensionId, newTypeId, newExtensionId, newName, newDescription, properties);
	}

	@Override
	public boolean removeExtensionItems(String platformId) throws IOException {
		return resolve().removeExtensionItems(platformId);
	}

	@Override
	public boolean removeExtensionItem(String platformId, String typeId, String extensionId) throws IOException {
		return resolve().removeExtensionItem(platformId, typeId, extensionId);
	}

	@Override
	public Map<String, Object> getExtensionProperties(String platformId, String typeId, String extensionId) throws IOException {
		return resolve().getExtensionProperties(platformId, typeId, extensionId);
	}

	@Override
	public boolean setExtensionProperties(String platformId, String typeId, String extensionId, Map<String, Object> properties) throws IOException {
		return resolve().setExtensionProperties(platformId, typeId, extensionId, properties);
	}

	@Override
	public boolean removeExtensionProperties(String platformId, String typeId, String extensionId, List<String> propertyNames) throws IOException {
		return resolve().removeExtensionProperties(platformId, typeId, extensionId, propertyNames);
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		resolve().adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		resolve().adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return resolve().getAdapter(adapter);
	}

	@Override
	public boolean isProxy() {
		return resolve().isProxy();
	}

	public static class ExtensionRegistryClientProxyImpl implements ExtensionRegistryClient {

		@Override
		public String getName() {
			return null;
		}

		@Override
		public String getURL() {
			return null;
		}

		@Override
		public Map<String, Object> getProperties() {
			return null;
		}

		@Override
		public boolean ping() {
			return false;
		}

		@Override
		public String echo(String message) throws IOException {
			return null;
		}

		@Override
		public List<ExtensionItem> getExtensionItems(String platformId) throws IOException {
			return null;
		}

		@Override
		public List<ExtensionItem> getExtensionItems(String platformId, String typeId) throws IOException {
			return null;
		}

		@Override
		public ExtensionItem getExtensionItem(String platformId, String typeId, String extensionId) throws IOException {
			return null;
		}

		@Override
		public ExtensionItem addExtensionItem(String platformId, String typeId, String extensionId, String name, String description, Map<String, Object> properties) throws IOException {
			return null;
		}

		@Override
		public boolean updateExtensionItem(String platformId, String typeId, String extensionId, String newTypeId, String newExtensionId, String newName, String newDescription, Map<String, Object> properties) throws IOException {
			return false;
		}

		@Override
		public boolean removeExtensionItems(String platformId) throws IOException {
			return false;
		}

		@Override
		public boolean removeExtensionItem(String platformId, String typeId, String extensionId) throws IOException {
			return false;
		}

		@Override
		public Map<String, Object> getExtensionProperties(String platformId, String typeId, String extensionId) throws IOException {
			return null;
		}

		@Override
		public boolean setExtensionProperties(String platformId, String typeId, String extensionId, Map<String, Object> properties) throws IOException {
			return false;
		}

		@Override
		public boolean removeExtensionProperties(String platformId, String typeId, String extensionId, List<String> propertyNames) throws IOException {
			return false;
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
		public boolean isProxy() {
			return true;
		}
	}

}
