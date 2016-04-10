package osgi.mgm.ws.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for MetaSpace
 *
 */
@XmlRootElement
public class MetaSpaceDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String description;

	// Container MetaSector
	@XmlElement
	protected MetaSectorDTO metaSector;

	// List of Artifacts that are deployed to the MeteSpace.
	@XmlElement
	protected List<ArtifactDTO> deployedArtifacts = new ArrayList<ArtifactDTO>();

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
	public MetaSectorDTO getMetaSector() {
		return metaSector;
	}

	public void setMetaSector(MetaSectorDTO metaSector) {
		this.metaSector = metaSector;
	}

	@XmlElement
	public List<ArtifactDTO> getDeployedArtifacts() {
		return deployedArtifacts;
	}

	public void setDeployedArtifacts(List<ArtifactDTO> deployedArtifacts) {
		this.deployedArtifacts = deployedArtifacts;
	}

}
