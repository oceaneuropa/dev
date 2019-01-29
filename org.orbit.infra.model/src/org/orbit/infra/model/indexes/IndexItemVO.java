package org.orbit.infra.model.indexes;

import java.text.DateFormat;
import java.util.Date;

import org.origin.common.util.DateUtil;

public class IndexItemVO {

	protected Integer indexItemId;
	protected String indexProviderId;
	protected String type;
	protected String name;
	protected String propertiesString;
	protected Date createTime;
	protected Date lastUpdateTime;

	public IndexItemVO() {
	}

	/**
	 * 
	 * @param indexItemId
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param propertiesString
	 * @param createTime
	 * @param lastUpdateTime
	 */
	public IndexItemVO(Integer indexItemId, String indexProviderId, String type, String name, String propertiesString, Date createTime, Date lastUpdateTime) {
		this.indexItemId = indexItemId;
		this.indexProviderId = indexProviderId;
		this.type = type;
		this.name = name;
		this.propertiesString = propertiesString;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getIndexItemId() {
		return this.indexItemId;
	}

	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}

	public String getIndexProviderId() {
		return this.indexProviderId;
	}

	public void setIndexProviderId(String indexProviderId) {
		this.indexProviderId = indexProviderId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPropertiesString() {
		return this.propertiesString;
	}

	public void setPropertiesString(String propertiesString) {
		this.propertiesString = propertiesString;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	@Override
	public String toString() {
		String createTimeString = createTime != null ? DateUtil.toString(createTime, getDateFormat()) : null;
		String lastUpdateTimeString = lastUpdateTime != null ? DateUtil.toString(lastUpdateTime, getDateFormat()) : null;

		StringBuilder sb = new StringBuilder();
		sb.append("IndexItemVO(");
		sb.append("indexItemId=").append(this.indexItemId);
		sb.append(", indexProviderId=").append(this.indexProviderId);
		sb.append(", type=").append(this.type);
		sb.append(", name=").append(this.name);
		sb.append(", propertiesString=").append(this.propertiesString);
		sb.append(", createTime=").append(createTimeString);
		sb.append(", lastUpdateTime=").append(lastUpdateTimeString);
		sb.append(")");

		return sb.toString();
	}

}
