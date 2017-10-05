package org.origin.common.cluster;

import java.io.Serializable;

public class Event implements Serializable {

	private static final long serialVersionUID = -4787882087046828224L;

	protected String namespace; // namespace of an event
	protected String localPart; // local name of an event
	protected Object source; // source of an event
	protected Object args; // must be serializable if used for inter-process communication

	public Event() {
	}

	/**
	 * 
	 * @param namespace
	 * @param localPart
	 * @param source
	 * @param args
	 */
	public Event(String namespace, String localPart, Object source, Object args) {
		this.namespace = checkNamespace(namespace);
		this.localPart = checkLocalPart(localPart);
		this.source = source;
		this.args = args;
	}

	protected String checkNamespace(String namespace) {
		if (namespace == null) {
			namespace = "";
		}
		return namespace;
	}

	protected String checkLocalPart(String localPart) {
		if (localPart == null) {
			localPart = "";
		}
		return localPart;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = checkNamespace(namespace);
	}

	public String getLocalPart() {
		return this.localPart;
	}

	public void setLocalPart(String localPart) {
		this.localPart = checkLocalPart(localPart);
	}

	public Object getSource() {
		return this.source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public Object getArgs() {
		return this.args;
	}

	public void setArgs(Object args) {
		this.args = args;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("namespace=").append(this.namespace);
		sb.append(", localPart=").append(this.localPart);
		sb.append(", source=").append(this.source);
		sb.append(", args=").append(args);
		return sb.toString();
	}

}
