package org.orbit.infra.model.extensionregistry;

import java.text.DateFormat;
import java.util.Date;

import org.origin.common.util.DateUtil;

public class ExtensionItemVO {

	protected Integer id;
	protected String platformId;
	protected String typeId;
	protected String extensionId;
	protected String name;
	protected String description;
	protected String propertiesString;
	protected Date createTime;
	protected Date lastUpdateTime;

	public ExtensionItemVO() {
	}

	/**
	 * 
	 * @param id
	 * @param platformId
	 * @param typeId
	 * @param extensionId
	 * @param name
	 * @param description
	 * @param propertiesString
	 * @param createTime
	 * @param lastUpdateTime
	 */
	public ExtensionItemVO(Integer id, String platformId, String typeId, String extensionId, String name, String description, String propertiesString, Date createTime, Date lastUpdateTime) {
		this.id = id;
		this.platformId = platformId;
		this.typeId = typeId;
		this.extensionId = extensionId;
		this.name = name;
		this.description = description;
		this.propertiesString = propertiesString;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPlatformId() {
		return this.platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getExtensionId() {
		return this.extensionId;
	}

	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
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
		String createTimeString = this.createTime != null ? DateUtil.toString(this.createTime, getDateFormat()) : null;
		String lastUpdateTimeString = this.lastUpdateTime != null ? DateUtil.toString(this.lastUpdateTime, getDateFormat()) : null;

		StringBuilder sb = new StringBuilder();
		sb.append("ExtensionItemVO(");
		sb.append("id=").append(this.id);
		sb.append(", platformId=").append(this.platformId);
		sb.append(", typeId=").append(this.typeId);
		sb.append(", extensionId=").append(this.extensionId);
		sb.append(", name=").append(this.name);
		sb.append(", description=").append(this.description);
		sb.append(", propertiesString=").append(this.propertiesString);
		sb.append(", createTime=").append(createTimeString);
		sb.append(", lastUpdateTime=").append(lastUpdateTimeString);
		sb.append(")");

		return sb.toString();
	}

}
