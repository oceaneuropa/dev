package org.orbit.infra.connector.extensionregistry;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.extensionregistry.ExtensionItem;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ExtensionItemImpl implements ExtensionItem {

	// protected Integer id;
	protected String platformId;
	protected String typeId;
	protected String extensionId;
	protected String name;
	protected String description;
	protected Map<String, Object> properties;

	/**
	 * 
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param name
	 * @param description
	 * @param properties
	 */
	public ExtensionItemImpl(String platformId, String typeId, String extensionId, String name, String description, Map<String, Object> properties) {
		// this.id = id;
		this.platformId = platformId;
		this.typeId = typeId;
		this.extensionId = extensionId;
		this.name = name;
		this.description = description;
		this.properties = properties;
	}

	// @Override
	// public Integer getId() {
	// return this.id;
	// }

	@Override
	public String getPlatformId() {
		return this.platformId;
	}

	@Override
	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public String getExtensionId() {
		return this.extensionId;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public synchronized Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new Hashtable<String, Object>();
		}
		return this.properties;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((extensionId == null) ? 0 : extensionId.hashCode());
//		result = prime * result + ((platformId == null) ? 0 : platformId.hashCode());
//		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		ExtensionItemImpl other = (ExtensionItemImpl) obj;
//		if (extensionId == null) {
//			if (other.extensionId != null)
//				return false;
//		} else if (!extensionId.equals(other.extensionId))
//			return false;
//		if (platformId == null) {
//			if (other.platformId != null)
//				return false;
//		} else if (!platformId.equals(other.platformId))
//			return false;
//		if (typeId == null) {
//			if (other.typeId != null)
//				return false;
//		} else if (!typeId.equals(other.typeId))
//			return false;
//		return true;
//	}

	@Override
	public String toString() {
		return "ExtensionItemImpl [platformId=" + platformId + ", typeId=" + typeId + ", extensionId=" + extensionId + ", name=" + name + ", description=" + description + ", properties=" + properties + "]";
	}

}

// @Override
// public int hashCode() {
// final int prime = 31;
// int result = 1;
// result = prime * result + ((id == null) ? 0 : id.hashCode());
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
// ExtensionItemImpl other = (ExtensionItemImpl) obj;
// if (id == null) {
// if (other.id != null) {
// return false;
// }
// } else if (!id.equals(other.id)) {
// return false;
// }
// return true;
// }
//
// @Override
// public String toString() {
// return "ExtensionItemImpl [id=" + id + ", platformId=" + platformId + ", typeId=" + typeId + ", extensionId=" + extensionId + ", name=" + name + ",
// description=" + description + "]";
// }
