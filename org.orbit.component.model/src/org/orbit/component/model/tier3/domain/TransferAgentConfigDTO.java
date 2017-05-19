package org.orbit.component.model.tier3.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Design time object for TransferAgent configuration.
 *
 */
@XmlRootElement
public class TransferAgentConfigDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String TAHome;
	@XmlElement
	protected String hostURL;
	@XmlElement
	protected String contextRoot;

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
	public String getTAHome() {
		return TAHome;
	}

	public void setTAHome(String TAHome) {
		this.TAHome = TAHome;
	}

	@XmlElement
	public String getHostURL() {
		return hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	@XmlElement
	public String getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

}
