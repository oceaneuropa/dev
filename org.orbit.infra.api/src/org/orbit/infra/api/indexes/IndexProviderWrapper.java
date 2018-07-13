package org.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class IndexProviderWrapper implements IndexProvider {

	protected IndexProvider indexProvider;

	public IndexProviderWrapper() {
	}

	public IndexProviderWrapper(IndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	public synchronized void set(IndexProvider indexProvider) {
		this.indexProvider = indexProvider;
	}

	public synchronized IndexProvider get() {
		return this.indexProvider;
	}

	@Override
	public String getName() {
		return (get() != null) ? get().getName() : null;
	}

	@Override
	public String getURL() {
		return (get() != null) ? get().getURL() : null;
	}

	@Override
	public Map<String, Object> getProperties() {
		return (get() != null) ? get().getProperties() : null;
	}

	@Override
	public boolean ping() {
		return (get() != null) ? get().ping() : null;
	}

	@Override
	public String echo(String message) throws IOException {
		return (get() != null) ? get().echo(message) : null;
	}

	@Override
	public boolean sendCommand(String action, Map<String, Object> params) throws IOException {
		return (get() != null) ? get().sendCommand(action, params) : null;
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId) throws IOException {
		return (get() != null) ? get().getIndexItems(indexProviderId) : null;
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws IOException {
		return (get() != null) ? get().getIndexItems(indexProviderId, type) : null;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws IOException {
		return (get() != null) ? get().getIndexItem(indexProviderId, type, name) : null;
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
		return (get() != null) ? get().getIndexItem(indexProviderId, indexItemId) : null;
	}

	@Override
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws IOException {
		return (get() != null) ? get().addIndexItem(indexProviderId, type, name, properties) : null;
	}

	@Override
	public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
		return (get() != null) ? get().removeIndexItem(indexProviderId, indexItemId) : null;
	}

	@Override
	public boolean setProperty(String indexProviderId, Integer indexItemId, String propName, Object propValue, String propType) throws IOException {
		return (get() != null) ? get().setProperty(indexProviderId, indexItemId, propName, propValue, propType) : null;
	}

	@Override
	public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws IOException {
		return (get() != null) ? get().setProperties(indexProviderId, indexItemId, properties) : null;
	}

	@Override
	public boolean removeProperties(String indexProviderId, Integer indexItemId, List<String> propertyNames) throws IOException {
		return (get() != null) ? get().removeProperties(indexProviderId, indexItemId, propertyNames) : null;
	}

	@Override
	public boolean isProxy() {
		return (get() != null) ? get().isProxy() : null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		if (get() != null) {
			get().adapt(clazz, object);
		}
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		if (get() != null) {
			get().adapt(classes, object);
		}
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return (get() != null) ? get().getAdapter(adapter) : null;
	}

}
