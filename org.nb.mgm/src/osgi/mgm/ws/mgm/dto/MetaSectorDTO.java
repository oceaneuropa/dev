package osgi.mgm.ws.mgm.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for MetaSector
 *
 */
@XmlRootElement
public class MetaSectorDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String description;

	// a list of MetaSpace in the MetaSector
	@XmlElement
	protected List<MetaSpaceDTO> metaSpaces = new ArrayList<MetaSpaceDTO>();

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
	public List<MetaSpaceDTO> getMetaSpaces() {
		return metaSpaces;
	}

	public void setMetaSpaces(List<MetaSpaceDTO> metaSpaces) {
		this.metaSpaces = metaSpaces;
	}

}
