package org.orbit.component.runtime.model.configregistry;

public class RegistryPathVO {

	protected String accountId;
	protected EPath path;
	protected String description;

	public RegistryPathVO() {
	}

	/**
	 * 
	 * @param accountId
	 * @param path
	 * @param description
	 */
	public RegistryPathVO(String accountId, EPath path, String description) {
		this.accountId = accountId;
		this.path = path;
		this.description = description;
	}

	/**
	 * 
	 * @param accountId
	 * @param path
	 * @param description
	 */
	public RegistryPathVO(String accountId, String path, String description) {
		this.accountId = accountId;
		this.path = new EPath(path);
		this.description = description;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
