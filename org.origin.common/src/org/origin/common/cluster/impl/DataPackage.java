package org.origin.common.cluster.impl;

import java.io.Serializable;

import org.origin.common.cluster.Event;

public class DataPackage implements Serializable {

	private static final long serialVersionUID = 2432013491814015400L;

	protected String clusterName; // the target cluster name that an event is sent to.
	protected Event event; // the event

	public DataPackage() {
	}

	/**
	 * 
	 * @param event
	 * @param clusterName
	 */
	public DataPackage(Event event, String clusterName) {
		this.event = event;
		this.clusterName = checkClusterName(clusterName);
	}

	protected String checkClusterName(String clusterName) {
		if (clusterName == null) {
			clusterName = "";
		}
		return clusterName;
	}

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = checkClusterName(clusterName);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		if (this.event != null) {
			sb.append(this.event.toString());
			sb.append(", ");
		}
		sb.append("clusterName=").append(this.clusterName);
		return sb.toString();
	}

}
