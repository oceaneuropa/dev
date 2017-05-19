package org.orbit.component.model.tier1.config;

public class RegistryPropertyVO {

	protected String userId;
	protected EPath path;
	protected String name;
	protected String value;

	public RegistryPropertyVO() {
	}

	/**
	 * 
	 * @param userId
	 * @param path
	 * @param name
	 * @param value
	 */
	public RegistryPropertyVO(String userId, EPath path, String name, String value) {
		this.userId = userId;
		this.path = path;
		this.name = name;
		this.value = value;
	}

	/**
	 * 
	 * @param userId
	 * @param path
	 * @param name
	 * @param value
	 */
	public RegistryPropertyVO(String userId, String path, String name, String value) {
		this.userId = userId;
		this.path = new EPath(path);
		this.name = name;
		this.value = value;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
