package org.orbit.platform.model.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProcessDTO {

	@XmlElement
	protected int pid;

	@XmlElement
	protected String name;

	@XmlElement
	public int getPID() {
		return this.pid;
	}

	public void setPID(int pID) {
		this.pid = pID;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
