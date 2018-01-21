package org.orbit.component.model.tier4.mission.rto;

import java.text.DateFormat;
import java.util.Date;

import org.origin.common.util.DateUtil;

public class MissionVO {

	protected Integer id;
	protected String typeId;
	protected String name;
	protected Date createTime;
	protected Date lastUpdateTime;

	public MissionVO() {
	}

	public MissionVO(Integer id, String typeId, String name, Date createTime, Date lastUpdateTime) {
		this.id = id;
		this.typeId = typeId;
		this.name = name;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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
		sb.append("MissionVO(");
		sb.append("id=").append(this.id);
		sb.append(", typeId=").append(this.typeId);
		sb.append(", name=").append(this.name);
		sb.append(", createTime=").append(createTimeString);
		sb.append(", lastUpdateTime=").append(lastUpdateTimeString);
		sb.append(")");

		return sb.toString();
	}

}
