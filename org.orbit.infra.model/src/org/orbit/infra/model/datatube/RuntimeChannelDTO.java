package org.orbit.infra.model.datatube;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RuntimeChannelDTO {

	@XmlElement
	protected String dataCastId;
	@XmlElement
	protected String dataTubeId;
	@XmlElement
	protected String channelId;
	@XmlElement
	protected String name;
	// @XmlElement
	// protected String accessType;
	// @XmlElement
	// protected String accessCode;
	// @XmlElement
	// protected String ownerAccountId;
	// @XmlElement
	// protected List<String> accountIds;
	// @XmlElement
	// protected Map<String, Object> properties;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	@XmlElement
	public String getDataCastId() {
		return this.dataCastId;
	}

	public void setDataCastId(String dataCastId) {
		this.dataCastId = dataCastId;
	}

	@XmlElement
	public String getDataTubeId() {
		return this.dataTubeId;
	}

	public void setDataTubeId(String dataTubeId) {
		this.dataTubeId = dataTubeId;
	}

	@XmlElement
	public String getChannelId() {
		return this.channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// @XmlElement
	// public String getAccessType() {
	// return this.accessType;
	// }
	//
	// public void setAccessType(String accessType) {
	// this.accessType = accessType;
	// }
	//
	// @XmlElement
	// public String getAccessCode() {
	// return this.accessCode;
	// }
	//
	// public void setAccessCode(String accessCode) {
	// this.accessCode = accessCode;
	// }
	//
	// @XmlElement
	// public String getOwnerAccountId() {
	// return this.ownerAccountId;
	// }
	//
	// public void setOwnerAccountId(String ownerAccountId) {
	// this.ownerAccountId = ownerAccountId;
	// }
	//
	// @XmlElement
	// public List<String> getAccountIds() {
	// return this.accountIds;
	// }
	//
	// public void setAccountIds(List<String> accountIds) {
	// this.accountIds = accountIds;
	// }
	//
	// @XmlElement
	// public Map<String, Object> getProperties() {
	// return this.properties;
	// }
	//
	// public void setProperties(Map<String, Object> properties) {
	// this.properties = properties;
	// }

	@XmlElement
	public long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@XmlElement
	public long getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}
