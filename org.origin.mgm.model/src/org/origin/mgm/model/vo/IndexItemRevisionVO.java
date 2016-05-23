package org.origin.mgm.model.vo;

public class IndexItemRevisionVO {

	protected Integer revision;
	protected String command;
	protected String arguments;
	protected String undoCommand;
	protected String undoArguments;
	protected String updateTimeString;

	public IndexItemRevisionVO() {
	}

	/**
	 * 
	 * @param revision
	 * @param command
	 * @param arguments
	 * @param undoCommand
	 * @param undoArguments
	 * @param updateTimeString
	 */
	public IndexItemRevisionVO(Integer revision, String command, String arguments, String undoCommand, String undoArguments, String updateTimeString) {
		this.revision = revision;
		this.command = command;
		this.arguments = arguments;
		this.undoCommand = undoCommand;
		this.undoArguments = undoArguments;
		this.updateTimeString = updateTimeString;
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

	public String getUndoCommand() {
		return undoCommand;
	}

	public void setUndoCommand(String undoCommand) {
		this.undoCommand = undoCommand;
	}

	public String getUndoArguments() {
		return undoArguments;
	}

	public void setUndoArguments(String undoArguments) {
		this.undoArguments = undoArguments;
	}

	public String getUpdateTimeString() {
		return updateTimeString;
	}

	public void setUpdateTimeString(String updateTimeString) {
		this.updateTimeString = updateTimeString;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("IndexItemRevisionVO(");
		sb.append("revision=").append(this.revision);
		sb.append(", command=").append(this.command);
		sb.append(", arguments=").append(this.arguments);
		sb.append(", undoCommand=").append(this.undoCommand);
		sb.append(", undoArguments=").append(this.undoArguments);
		sb.append(", updateTimeString=").append(this.updateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
