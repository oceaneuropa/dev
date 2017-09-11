package org.origin.core.resources.impl.database;

public class WorkspaceVO {

	protected int workspaceId;
	protected String name;
	protected String password;
	protected String description;
	protected long lastModified;

	public WorkspaceVO() {
	}

	public WorkspaceVO(int workspaceId, String name, String password, String description, long lastModified) {
		this.workspaceId = workspaceId;
		this.name = name;
		this.password = password;
		this.description = description;
		this.lastModified = lastModified;
	}

	public int getWorkspaceId() {
		return this.workspaceId;
	}

	public void setWorkspaceId(int workspaceId) {
		this.workspaceId = workspaceId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("WorkspaceVO(");
		sb.append("workspaceId=").append(this.workspaceId);
		sb.append(", name=").append(this.name);
		sb.append(", password=").append(this.password);
		sb.append(", description=").append(this.description);
		sb.append(", lastModified=").append(this.lastModified);
		sb.append(")");
		return sb.toString();
	}

}
