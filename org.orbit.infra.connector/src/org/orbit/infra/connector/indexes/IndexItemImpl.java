package org.orbit.infra.connector.indexes;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.model.TransientPropertySupport;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class IndexItemImpl implements IndexItem {

	protected Integer indexItemId;
	protected String indexProviderId;
	protected String type;
	protected String name;
	protected Map<String, Object> properties;
	protected Date createTime;
	protected Date updateTime;

	protected TransientPropertySupport transientPropertySupport = new TransientPropertySupport();
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param indexItemId
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 * @param createTime
	 * @param updateTime
	 */
	public IndexItemImpl(Integer indexItemId, String indexProviderId, String type, String name, Map<String, Object> properties, Date createTime, Date updateTime) {
		this.indexItemId = indexItemId;
		this.indexProviderId = indexProviderId;
		this.type = type;
		this.name = name;
		this.properties = properties;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	@Override
	public Integer getIndexItemId() {
		return this.indexItemId;
	}

	@Override
	public String getIndexProviderId() {
		return this.indexProviderId;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public synchronized Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new Hashtable<String, Object>();
		}
		return this.properties;
	}

	@Override
	public Date getDateCreated() {
		return this.createTime;
	}

	@Override
	public Date getDateModified() {
		return this.updateTime;
	}

	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result + ((indexItemId == null) ? 0 : indexItemId.hashCode());
	// return result;
	// }
	//
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj) {
	// return true;
	// }
	// if (obj == null) {
	// return false;
	// }
	// if (getClass() != obj.getClass()) {
	// return false;
	// }
	// IndexItemImpl other = (IndexItemImpl) obj;
	// if (indexItemId == null) {
	// if (other.indexItemId != null) {
	// return false;
	// }
	// } else if (!indexItemId.equals(other.indexItemId)) {
	// return false;
	// }
	// return true;
	// }

	@Override
	public String toString() {
		return "IndexItemImpl [indexItemId=" + this.indexItemId + ", indexProviderId=" + this.indexProviderId + ", type=" + this.type + ", name=" + this.name + ", createTime=" + this.createTime + ", updateTime=" + this.updateTime + "]";
	}

	/** TransientPropertyAware */
	@Override
	public <T> T getTransientProperty(String key) {
		return this.transientPropertySupport.getTransientProperty(key);
	}

	@Override
	public <T> T setTransientProperty(String key, T value) {
		return this.transientPropertySupport.setTransientProperty(key, value);
	}

	@Override
	public <T> T removeTransientProperty(String key) {
		return this.transientPropertySupport.removeTransientProperty(key);
	}

	/** IAdaptable */
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
