package org.origin.common.rest.server;

import java.util.ArrayList;
import java.util.List;

public class WSApplicationDesc {

	protected String contextRoot;
	protected int feature = FeatureConstants.PING;
	protected List<WSResourceDesc> wsResources = new ArrayList<WSResourceDesc>();

	/**
	 * 
	 * @param contextRoot
	 */
	public WSApplicationDesc(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	public int getFeature() {
		return this.feature;
	}

	public void setFeature(int feature) {
		this.feature = feature;
	}

	public List<WSResourceDesc> getResources() {
		return this.wsResources;
	}

	public void addResources(List<WSResourceDesc> wsResources) {
		if (wsResources != null) {
			for (WSResourceDesc wsResource : wsResources) {
				addResource(wsResource);
			}
		}
	}

	public boolean addResource(WSResourceDesc wsResource) {
		if (wsResource != null && !this.wsResources.contains(wsResource)) {
			boolean succeed = this.wsResources.add(wsResource);

			if (succeed) {
				WSApplicationDesc oldParent = wsResource.getParent();
				if (oldParent != null && oldParent != this) {
					oldParent.removeResource(wsResource);
				}
				wsResource.setParent(this);
			}

			return succeed;
		}
		return false;
	}

	public boolean removeResource(WSResourceDesc wsResource) {
		if (wsResource != null) {
			boolean succeed = this.wsResources.remove(wsResource);

			if (succeed) {
				WSApplicationDesc oldParent = wsResource.getParent();
				if (oldParent == this) {
					wsResource.setParent(null);
				}
			}

			return succeed;
		}
		return false;
	}

}
