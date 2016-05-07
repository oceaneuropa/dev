package org.origin.mgm.model.vo;

public class IndexItemLogVO {

	protected Integer revision;
	protected String command;
	protected String arguments;
	protected String lastUpdateTimeString;

	public IndexItemLogVO() {
	}

	/**
	 * 
	 * @param revision
	 * @param command
	 * @param arguments
	 * @param lastUpdateTimeString
	 */
	public IndexItemLogVO(Integer revision, String command, String arguments, String lastUpdateTimeString) {
		this.revision = revision;
		this.command = command;
		this.arguments = arguments;
		this.lastUpdateTimeString = lastUpdateTimeString;
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getArguments() {
		return arguments;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	public String getLastUpdateTimeString() {
		return lastUpdateTimeString;
	}

	public void setLastUpdateTimeString(String lastUpdateTimeString) {
		this.lastUpdateTimeString = lastUpdateTimeString;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("IndexItemLogVO(");
		sb.append("revision=").append(this.revision);
		sb.append(", command=").append(this.command);
		sb.append(", arguments=").append(this.arguments);
		sb.append(", lastUpdateTimeString=").append(this.lastUpdateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
