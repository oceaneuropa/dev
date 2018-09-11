package org.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadata;

public class IndexServiceClientWrapper implements IndexServiceClient {

	protected IndexServiceClient indexProvider;

	public IndexServiceClientWrapper() {
	}

	public IndexServiceClientWrapper(IndexServiceClient indexProvider) {
		this.indexProvider = indexProvider;
	}

	public synchronized void set(IndexServiceClient indexProvider) {
		this.indexProvider = indexProvider;
	}

	public synchronized IndexServiceClient get() {
		return this.indexProvider;
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
	public void update(Map<String, Object> properties) {
		if (get() != null) {
			get().update(properties);
		}
	}

	@Override
	public boolean close() throws ClientException {
		return (get() != null) ? get().close() : false;
	}

	@Override
	public ServiceMetadata getMetadata() throws ClientException {
		return (get() != null) ? get().getMetadata() : null;
	}

	@Override
	public Response sendRequest(Request request) throws ClientException {
		return (get() != null) ? get().sendRequest(request) : null;
	}

	@Override
	public String getName() throws ClientException {
		return (get() != null) ? get().getName() : null;
	}

	@Override
	public boolean ping() {
		return (get() != null) ? get().ping() : null;
	}

	@Override
	public String echo(String message) throws ClientException {
		return (get() != null) ? get().echo(message) : null;
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
	public boolean deleteIndexItem(String indexProviderId, Integer indexItemId) throws IOException {
		return (get() != null) ? get().deleteIndexItem(indexProviderId, indexItemId) : null;
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

	@Override
	public boolean isProxy() {
		return (get() != null) ? get().isProxy() : null;
	}

}
