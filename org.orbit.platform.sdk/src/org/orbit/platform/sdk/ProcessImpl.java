package org.orbit.platform.sdk;

import org.origin.common.adapter.AdaptorSupport;

public class ProcessImpl implements IProcess {

	protected int pid;
	protected String name;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	public ProcessImpl() {
	}

	/**
	 * 
	 * @param pid
	 * @param name
	 */
	public ProcessImpl(int pid, String name) {
		this.pid = pid;
		this.name = name;
	}

	@Override
	public int getPID() {
		return this.pid;
	}

	void setPID(int pid) {
		this.pid = pid;
	}

	@Override
	public String getName() {
		return this.name;
	}

	void setName(String name) {
		this.name = name;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessImpl other = (ProcessImpl) obj;
		if (pid != other.pid)
			return false;
		return true;
	}

}
