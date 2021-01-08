package org.orbit.infra.connector.indexes;

import java.util.Date;

import org.orbit.infra.api.indexes.IndexProviderItem;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.model.TransientPropertySupport;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class IndexProviderItemImpl implements IndexProviderItem {

	protected String id;
	protected String name;
	protected String description;
	protected Date dateCreated;
	protected Date dateModified;
	protected TransientPropertySupport transientPropertySupport = new TransientPropertySupport();
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	public IndexProviderItemImpl() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	/** hashCode() equals() toString() */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexProviderItemImpl other = (IndexProviderItemImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IndexProviderItemImpl [id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", dateCreated=" + this.dateCreated + ", dateModified=" + this.dateModified + "]";
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

	@Override
	public <T> Class<T>[] getAdaptedClasses() {
		return this.adaptorSupport.getAdaptedClasses();
	}

}
