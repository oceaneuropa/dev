package org.orbit.component.model.tier1.config;

public class RegistryPathVO {

	protected String userId;
	protected EPath path;
	protected String description;

	public RegistryPathVO() {
	}

	/**
	 * 
	 * @param userId
	 * @param path
	 * @param description
	 */
	public RegistryPathVO(String userId, EPath path, String description) {
		this.userId = userId;
		this.path = path;
		this.description = description;
	}

	/**
	 * 
	 * @param userId
	 * @param path
	 * @param description
	 */
	public RegistryPathVO(String userId, String path, String description) {
		this.userId = userId;
		this.path = new EPath(path);
		this.description = description;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public EPath getPath() {
		return path;
	}

	public void setPath(EPath path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
