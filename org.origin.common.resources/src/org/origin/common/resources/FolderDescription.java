package org.origin.common.resources;

import java.util.LinkedHashMap;
import java.util.Map;

public class FolderDescription {

	public static final String DEFAULT_VERSION = "1.0";

	protected String version = DEFAULT_VERSION;
	protected String id;
	protected Map<String, Object> attributes = new LinkedHashMap<String, Object>();

	public FolderDescription() {
	}

	/**
	 * 
	 * @param id
	 */
	public FolderDescription(String id) {
		this.id = id;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	public String[] getAttributeNames() {
		return this.attributes.keySet().toArray(new String[this.attributes.size()]);
	}

	/**
	 * 
	 * @param attrName
	 * @return
	 */
	public boolean hasAttribute(String attrName) {
		return (attrName != null && this.attributes.get(attrName) != null) ? true : false;
	}

	/**
	 * 
	 * @param attrName
	 * @return
	 */
	public String getStringAttribute(String attrName) {
		Object value = this.attributes.get(attrName);
		if (value != null) {
			return value.toString();
		}
		return null;
	}

	/**
	 * 
	 * @param attrName
	 * @return
	 */
	public Boolean getBooleanAttribute(String attrName) {
		Object value = this.attributes.get(attrName);
		if (value != null) {
			try {
				if (value instanceof Boolean) {
					return (Boolean) value;
				} else {
					return Boolean.parseBoolean(value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 
	 * @param attrName
	 * @return
	 */
	public Integer getIntAttribute(String attrName) {
		Object value = this.attributes.get(attrName);
		if (value != null) {
			try {
				if (value instanceof Integer) {
					return (Integer) value;
				} else {
					return Integer.parseInt(value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 
	 * @param attrName
	 * @return
	 */
	public Long getLongAttribute(String attrName) {
		Object value = this.attributes.get(attrName);
		if (value != null) {
			try {
				if (value instanceof Long) {
					return (Long) value;
				} else {
					return Long.parseLong(value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return (long) 0;
	}

	/**
	 * 
	 * @param attrName
	 * @param attrValue
	 */
	public void setAttirbute(String attrName, Object attrValue) {
		this.attributes.put(attrName, attrValue);
	}

	/**
	 * 
	 * @param attrName
	 */
	public void removeAttribute(String attrName) {
		this.attributes.remove(attrName);
	}

	@Override
	public String toString() {
		return "NodeDescription [version=" + this.version + ", id=" + this.id + "]";
	}

}

// protected String name;
// public String getName() {
// return this.name;
// }
// public void setName(String name) {
// this.name = name;
// }
// return "NodeDescription [version=" + this.version + ", id=" + this.id + ", name=" + this.name + "]";
