package org.orbit.platform.model.dto;

public class ProcessInfoImpl implements ProcessInfo {

	protected int pid;
	protected String name;

	public ProcessInfoImpl() {
	}

	/**
	 * 
	 * @param pid
	 * @param name
	 */
	public ProcessInfoImpl(int pid, String name) {
		this.pid = pid;
		this.name = name;
	}

	@Override
	public int getPID() {
		return this.pid;
	}

	public void setPID(int pID) {
		this.pid = pID;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
