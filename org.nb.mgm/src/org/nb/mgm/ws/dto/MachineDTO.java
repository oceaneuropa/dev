package org.nb.mgm.ws.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for Machine
 *
 */
@XmlRootElement
public class MachineDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String description;

	@XmlElement
	protected String ipAddress;

	// a list of Homes in the Machine
	@XmlElement
	protected List<HomeDTO> homes = new ArrayList<HomeDTO>();

	@XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@XmlElement
	public List<HomeDTO> getHomes() {
		return homes;
	}

	public void setHomes(List<HomeDTO> homes) {
		this.homes = homes;
	}

}
