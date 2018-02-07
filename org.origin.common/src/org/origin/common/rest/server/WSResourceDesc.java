package org.origin.common.rest.server;

import java.util.ArrayList;
import java.util.List;

public class WSResourceDesc {

	protected WSApplicationDesc parent;
	protected String path;
	protected List<WSMethodDesc> wsMethods = new ArrayList<WSMethodDesc>();

	/**
	 * 
	 * @param path
	 */
	public WSResourceDesc(String path) {
		this.path = path;
	}

	/**
	 * 
	 * @param parent
	 * @param path
	 */
	public WSResourceDesc(WSApplicationDesc parent, String path) {
		this.path = path;

		parent.addResource(this);
	}

	public WSApplicationDesc getParent() {
		return this.parent;
	}

	public void setParent(WSApplicationDesc parent) {
		this.parent = parent;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<WSMethodDesc> getMethods() {
		return this.wsMethods;
	}

	public void addMethods(List<WSMethodDesc> wsMethods) {
		if (wsMethods != null) {
			for (WSMethodDesc wsMethod : wsMethods) {
				addMethod(wsMethod);
			}
		}
	}

	public boolean addMethod(WSMethodDesc wsMethod) {
		if (wsMethod != null && !this.wsMethods.contains(wsMethod)) {
			boolean succeed = this.wsMethods.add(wsMethod);

			if (succeed) {
				WSResourceDesc oldParent = wsMethod.getParent();
				if (oldParent != null && oldParent != this) {
					oldParent.removeMethod(wsMethod);
				}
				wsMethod.setParent(this);
			}

			return succeed;
		}
		return false;
	}

	public boolean removeMethod(WSMethodDesc wsMethod) {
		if (wsMethod != null) {
			boolean succeed = this.wsMethods.remove(wsMethod);

			if (succeed) {
				WSResourceDesc oldParent = wsMethod.getParent();
				if (oldParent == this) {
					wsMethod.setParent(null);
				}
			}

			return succeed;
		}
		return false;
	}

}
