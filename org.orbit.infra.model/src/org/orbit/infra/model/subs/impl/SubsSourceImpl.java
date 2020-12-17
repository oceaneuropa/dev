package org.orbit.infra.model.subs.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.orbit.infra.model.subs.SubsSource;
import org.origin.common.json.JSONUtil;
import org.origin.common.util.DateUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsSourceImpl implements SubsSource {

	protected Integer id;
	protected String type;
	protected String typeId;
	protected String name;
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();
	protected long dateCreated;
	protected long dateModified;

	public SubsSourceImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param type
	 * @param typeId
	 * @param name
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public SubsSourceImpl(Integer id, String type, String typeId, String name, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.id = id;
		this.type = type;
		this.typeId = typeId;
		this.name = name;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
		if (properties != null) {
			this.properties.putAll(properties);
		}
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
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
		String propertiesString = JSONUtil.toJsonString(this.properties);
		String dateCreatedStr = DateUtil.toString(this.dateCreated, DateUtil.getJdbcDateFormat());
		String dateModifiedStr = DateUtil.toString(this.dateModified, DateUtil.getJdbcDateFormat());

		return "SubsSourceImpl [id=" + id + ", type=" + type + ", typeId=" + typeId + ", name=" + name + ", properties=" + propertiesString + ", dateCreated=" + dateCreatedStr + ", dateModified=" + dateModifiedStr + "]";
	}

}
