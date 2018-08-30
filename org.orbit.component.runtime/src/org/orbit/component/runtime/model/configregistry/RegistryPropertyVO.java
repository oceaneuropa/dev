package org.orbit.component.runtime.model.configregistry;

public class RegistryPropertyVO {

	protected String accountId;
	protected EPath path;
	protected String name;
	protected String value;

	public RegistryPropertyVO() {
	}

	/**
	 * 
	 * @param accountId
	 * @param path
	 * @param name
	 * @param value
	 */
	public RegistryPropertyVO(String accountId, EPath path, String name, String value) {
		this.accountId = accountId;
		this.path = path;
		this.name = name;
		this.value = value;
	}

	/**
	 * 
	 * @param accountId
	 * @param path
	 * @param name
	 * @param value
	 */
	public RegistryPropertyVO(String accountId, String path, String name, String value) {
		this.accountId = accountId;
		this.path = new EPath(path);
		this.name = name;
		this.value = value;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public EPath getPath() {
		return this.path;
	}

	public void setPath(EPath path) {
		this.path = path;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
