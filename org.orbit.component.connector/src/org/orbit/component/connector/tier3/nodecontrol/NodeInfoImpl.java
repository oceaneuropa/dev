package org.orbit.component.connector.tier3.nodecontrol;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.RuntimeStatus;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.connector.util.RuntimeStatusImpl;

public class NodeInfoImpl implements NodeInfo {

	protected String id;
	protected String name;
	protected URI uri;
	protected Map<String, Object> attributes;

	protected RuntimeStatus runtimeStatus = new RuntimeStatusImpl();
	protected Map<String, Object> runtimeProperties = new HashMap<String, Object>();

	@Override
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public URI getUri() {
		return this.uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	@Override
	public synchronized Map<String, Object> getAttributes() {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, Object>();
		}
		return this.attributes;
	}

	public synchronized void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public RuntimeStatus getRuntimeStatus() {
		return this.runtimeStatus;
	}

	@Override
	public Map<String, Object> getRuntimeProperties() {
		return this.runtimeProperties;
	}

	@Override
	public String toString() {
		return "NodeInfoImpl [id=" + id + ", name=" + name + ", uri=" + uri + "]";
	}

}
