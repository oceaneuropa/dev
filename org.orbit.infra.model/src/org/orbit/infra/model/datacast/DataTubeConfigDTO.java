package org.orbit.infra.model.datacast;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataTubeConfigDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String dataCastId;
	@XmlElement
	protected String dataTubeId;
	@XmlElement
	protected String name;
	@XmlElement
	protected boolean enabled;
	@XmlElement
	protected Map<String, Object> properties;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	public DataTubeConfigDTO() {
	}

	@XmlElement
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

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
