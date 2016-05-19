package org.origin.mgm.model.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

public class IndexItem {

	public static final String DEFAULT_TYPE = XMLConstants.DEFAULT_NS_PREFIX;

	/**
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public static QName getQName(String type, String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name is null.");
		}
		type = type != null ? type : DEFAULT_TYPE;
		return new QName(type, name);
	}

	/**
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public static String getFullName(String type, String name) {
		return getQName(type, name).toString();
	}

	protected Integer id;
	protected String namespace;
	protected String name;
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();

	/**
	 * 
	 * @param id
	 * @param type
	 * @param name
	 * @param props
	 */
	public IndexItem(Integer id, String type, String name, Map<String, Object> props) {
		if (id == null) {
			throw new IllegalArgumentException("id is null.");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name is null.");
		}
		this.id = id;
		this.namespace = type != null ? type : DEFAULT_TYPE;
		this.name = name;
		if (props != null) {
			this.properties = props;
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("name is null.");
		}
		this.name = name;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id:");
		sb.append(this.id);
		sb.append(getFullName(this.namespace, this.name));
		sb.append("properties:");
		sb.append(this.properties.toString());
		return sb.toString();
	}

	@Override
	public int hashCode() {
		return ("{" + this.namespace + "}" + this.name).hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof IndexItem)) {
			return false;
		}
		IndexItem other = (IndexItem) object;
		if (this.namespace.equals(other.getNamespace()) && this.name.equals(other.getName())) {
			return true;
		}
		return false;
	}

}
