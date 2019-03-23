package org.orbit.infra.connector.configregistry;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigElement;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.connector.util.ModelConverter;
import org.origin.common.resource.Path;

public class ConfigElementImpl implements ConfigElement {

	protected ConfigRegistryClient configRegistryClient;

	protected String configRegistryId;
	protected int id;
	protected String elementId;
	protected String parentElementId;
	protected Path path;
	protected String name;
	protected Map<String, Object> attributes;
	protected long dateCreated;
	protected long dateModified;

	/**
	 * 
	 * @param configRegistryClient
	 */
	public ConfigElementImpl(ConfigRegistryClient configRegistryClient) {
		this.configRegistryClient = configRegistryClient;
	}

	/**
	 * 
	 * @param configRegistryClient
	 * @param configRegistryId
	 * @param id
	 * @param elementId
	 * @param parentElementId
	 * @param path
	 * @param name
	 * @param attributes
	 * @param dateCreated
	 * @param dateModified
	 */
	public ConfigElementImpl(ConfigRegistryClient configRegistryClient, String configRegistryId, int id, String elementId, String parentElementId, Path path, String name, Map<String, Object> attributes, long dateCreated, long dateModified) {
		this.configRegistryClient = configRegistryClient;

		this.configRegistryId = configRegistryId;
		this.id = id;
		this.elementId = elementId;
		this.parentElementId = parentElementId;
		this.path = path;
		this.name = name;
		this.attributes = attributes;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	@Override
	public ConfigRegistryClient getConfigRegistryClient() {
		return this.configRegistryClient;
	}

	@Override
	public String getConfigRegistryId() {
		return this.configRegistryId;
	}

	public void setConfigRegistryId(String configRegistryId) {
		this.configRegistryId = configRegistryId;
	}

	@Override
	public String getElementId() {
		return this.elementId;
	}

	@Override
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	@Override
	public String getParentElementId() {
		return this.parentElementId;
	}

	@Override
	public void setParentElementId(String parentElementId) {
		this.parentElementId = parentElementId;
	}

	@Override
	public Path getPath() {
		return this.path;
	}

	@Override
	public void setPath(Path path) {
		this.path = path;

		// Update local name at the same time.
		if (this.path != null) {
			String lastSegment = this.path.getLastSegment();

			if ((this.name != null && !this.name.equals(lastSegment)) || (this.name == null && lastSegment != null)) {
				this.name = lastSegment;
			}
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		if (this.path != null && this.path.isRoot()) {
			throw new UnsupportedOperationException("Cannot change root name.");
		}

		this.name = name;

		// Update path at the same time, if path has been set.
		if (this.path != null) {
			String lastSegment = this.path.getLastSegment();
			if ((this.name != null && !this.name.equals(lastSegment)) || (this.name == null && lastSegment != null)) {
				Path parentPath = this.path.getParent();
				if (parentPath == null) {
					this.path = new Path(name);
				} else {
					this.path = new Path(parentPath, name);
				}
			}
		}
	}

	@Override
	public String[] getAttributeNames() {
		String[] attrNames = null;
		if (this.attributes != null && !this.attributes.isEmpty()) {
			attrNames = this.attributes.keySet().toArray(new String[this.attributes.size()]);
		}
		if (attrNames == null) {
			attrNames = new String[0];
		}
		return attrNames;
	}

	@Override
	public Map<String, Object> getAttributes() {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, Object>();
		}
		return this.attributes;
	}

	@Override
	public Object getAttribute(String attrName) {
		Object attrValue = null;
		if (attrName != null && this.attributes != null) {
			attrValue = this.attributes.get(attrName);
		}
		return attrValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttribute(String attrName, Class<T> attrValueClass) {
		T attrValue = null;
		if (attrName != null && attrValueClass != null && this.attributes != null) {
			Object attrValueObject = this.attributes.get(attrName);
			if (attrValueObject != null && attrValueClass.isAssignableFrom(attrValueObject.getClass())) {
				attrValue = (T) attrValueObject;
			}
		}
		return attrValue;
	}

	@Override
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public long getDateCreated() {
		return this.dateCreated;
	}

	@Override
	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public long getDateModified() {
		return this.dateModified;
	}

	@Override
	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public String toString() {
		String attributesString = ModelConverter.COMMON.toMapString(this.attributes);

		StringBuilder sb = new StringBuilder();
		sb.append("ConfigElementImpl (");
		sb.append("configRegistryId='").append(this.configRegistryId).append("'");
		sb.append("id='").append(this.id).append("'");
		sb.append(", elementId='").append(this.elementId).append("'");
		sb.append(", parentElementId='").append(this.parentElementId).append("'");
		if (this.path == null) {
			sb.append(", path=null");
		} else {
			sb.append(", path='").append(this.path.getPathString()).append("'");
		}
		sb.append(", name='").append(this.name).append("'");
		sb.append(", attributes=").append(attributesString);
		sb.append(", dateCreated=").append(this.dateCreated);
		sb.append(", dateModified=").append(this.dateModified);
		sb.append(")");

		return sb.toString();
	}

}
