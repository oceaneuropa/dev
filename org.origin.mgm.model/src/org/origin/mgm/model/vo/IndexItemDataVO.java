package org.origin.mgm.model.vo;

import java.text.DateFormat;
import java.util.Date;

import org.origin.common.util.DateUtil;

public class IndexItemDataVO {

	protected Integer indexItemId;
	protected String indexProviderId;
	protected String namespace;
	protected String name;
	protected String propertiesString;
	protected Date createTime;
	protected Date lastUpdateTime;

	public IndexItemDataVO() {
	}

	/**
	 * 
	 * @param indexItemId
	 * @param indexProviderId
	 * @param namespace
	 * @param name
	 * @param propertiesString
	 * @param createTime
	 * @param lastUpdateTime
	 */
	public IndexItemDataVO(Integer indexItemId, String indexProviderId, String namespace, String name, String propertiesString, Date createTime, Date lastUpdateTime) {
		this.indexItemId = indexItemId;
		this.indexProviderId = indexProviderId;
		this.namespace = namespace;
		this.name = name;
		this.propertiesString = propertiesString;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getIndexItemId() {
		return indexItemId;
	}

	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}

	public String getIndexProviderId() {
		return indexProviderId;
	}

	public void setIndexProviderId(String indexProviderId) {
		this.indexProviderId = indexProviderId;
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
		this.name = name;
	}

	public String getPropertiesString() {
		return propertiesString;
	}

	public void setPropertiesString(String propertiesString) {
		this.propertiesString = propertiesString;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
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
		sb.append("IndexItemDataVO(");
		sb.append("indexItemId=").append(this.indexItemId);
		sb.append(", indexProviderId=").append(this.indexProviderId);
		sb.append(", namespace=").append(this.namespace);
		sb.append(", name=").append(this.name);
		sb.append(", propertiesString=").append(this.propertiesString);
		sb.append(", createTime=").append(createTimeString);
		sb.append(", lastUpdateTime=").append(lastUpdateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
