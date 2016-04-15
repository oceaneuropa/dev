package osgi.mgm.ws.mgm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for Machine
 *
 */
@XmlRootElement
public class HomeDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String description;

	@XmlElement
	protected String url;

	// Container Machine
	@XmlElement
	protected MachineDTO machine;

	// List of MetaSectors that the Home joined
	@XmlElement
	protected List<MetaSectorDTO> joinedMetaSectors = new ArrayList<MetaSectorDTO>();

	// List of MetaSpaces that the Home joined
	@XmlElement
	protected List<MetaSpaceDTO> joinedMetaSpaces = new ArrayList<MetaSpaceDTO>();

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
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlElement
	public MachineDTO getMachine() {
		return machine;
	}

	public void setMachine(MachineDTO machine) {
		this.machine = machine;
	}

	@XmlElement
	public List<MetaSectorDTO> getJoinedMetaSectors() {
		return joinedMetaSectors;
	}

	public void setJoinedMetaSectors(List<MetaSectorDTO> joinedMetaSectors) {
		this.joinedMetaSectors = joinedMetaSectors;
	}

	@XmlElement
	public List<MetaSpaceDTO> getJoinedMetaSpaces() {
		return joinedMetaSpaces;
	}

	public void setJoinedMetaSpaces(List<MetaSpaceDTO> joinedMetaSpaces) {
		this.joinedMetaSpaces = joinedMetaSpaces;
	}

}
