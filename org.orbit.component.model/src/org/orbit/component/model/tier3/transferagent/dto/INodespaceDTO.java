package org.orbit.component.model.tier3.transferagent.dto;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Design time object for NodespaceInfo on TA.
 *
 */
@XmlRootElement
public class INodespaceDTO {

	@XmlElement
	protected String name;
	@XmlElement
	protected URI uri;

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

}
