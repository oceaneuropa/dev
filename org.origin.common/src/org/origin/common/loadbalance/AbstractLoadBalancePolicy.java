package org.origin.common.loadbalance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractLoadBalancePolicy<RES extends LoadBalanceableResource> implements LoadBalancePolicy<RES> {

	// protected List<RES> resources = new ArrayList<RES>();
	protected Map<String, RES> resourceMap = new LinkedHashMap<String, RES>();

	/**
	 * 
	 * @param resource
	 * @return
	 */
	public boolean contains(RES resource) {
		if (resource != null) {
			String resourceId = resource.getLoadBalanceId();
			if (resourceId != null) {
				for (RES res : this.resourceMap.values()) {
					if (resourceId.equals(res.getLoadBalanceId())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void setResources(List<RES> resources) {
		this.resourceMap.clear();
		if (resources != null) {
			for (RES res : resources) {
				this.resourceMap.put(res.getLoadBalanceId(), res);
			}
		}
	}

	@Override
	public List<RES> getResources() {
		List<RES> resources = new ArrayList<RES>();
		for (RES res : this.resourceMap.values()) {
			resources.add(res);
		}
		return resources;
	}

	/**
	 * 
	 * @param resource
	 */
	public void addResource(RES resource) {
		if (resource != null) {
			String resourceId = resource.getLoadBalanceId();
			if (resourceId != null) {
				this.resourceMap.put(resourceId, resource);
			}
		}
	}

	/**
	 * 
	 * @param resource
	 */
	public void removeResource(RES resource) {
		if (resource != null) {
			String resourceId = resource.getLoadBalanceId();
			if (resourceId != null) {
				this.resourceMap.remove(resourceId);
			}
		}
	}

	/**
	 * Get the next load balance resource.
	 * 
	 * @return
	 */
	@Override
	public abstract RES next();

}
