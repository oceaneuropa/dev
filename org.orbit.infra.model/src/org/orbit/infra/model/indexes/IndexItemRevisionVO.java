package org.orbit.infra.model.indexes;

import java.text.DateFormat;
import java.util.Date;

import org.origin.common.util.DateUtil;

public class IndexItemRevisionVO {

	protected Integer revisionId;
	protected String indexProviderId;
	protected String command;
	protected String argumentsString;
	protected String undoCommand;
	protected String undoArgumentsString;
	protected Date updateTime;

	public IndexItemRevisionVO() {
	}

	/**
	 * 
	 * @param revisionId
	 * @param indexProviderId
	 * @param command
	 * @param argumentsString
	 * @param undoCommand
	 * @param undoArgumentsString
	 * @param updateTime
	 */
	public IndexItemRevisionVO(Integer revisionId, String indexProviderId, String command, String argumentsString, String undoCommand, String undoArgumentsString, Date updateTime) {
		this.revisionId = revisionId;
		this.indexProviderId = indexProviderId;
		this.command = command;
		this.argumentsString = argumentsString;
		this.undoCommand = undoCommand;
		this.undoArgumentsString = undoArgumentsString;
		this.updateTime = updateTime;
	}

	public Integer getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(Integer revisionId) {
		this.revisionId = revisionId;
	}

	public String getIndexProviderId() {
		return indexProviderId;
	}

	public void setIndexProviderId(String indexProviderId) {
		this.indexProviderId = indexProviderId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getArgumentsString() {
		return argumentsString;
	}

	public void setArgumentsString(String argumentsString) {
		this.argumentsString = argumentsString;
	}

	public String getUndoCommand() {
		return undoCommand;
	}

	public void setUndoCommand(String undoCommand) {
		this.undoCommand = undoCommand;
	}

	public String getUndoArgumentsString() {
		return undoArgumentsString;
	}

	public void setUndoArgumentsString(String undoArgumentsString) {
		this.undoArgumentsString = undoArgumentsString;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	@Override
	public String toString() {
		String updateTimeString = updateTime != null ? DateUtil.toString(updateTime, getDateFormat()) : null;

		StringBuilder sb = new StringBuilder();
		sb.append("IndexItemRevisionVO(");
		sb.append("revisionId=").append(this.revisionId);
		sb.append(", indexProviderId=").append(this.indexProviderId);
		sb.append(", command=").append(this.command);
		sb.append(", arguments=").append(this.argumentsString);
		sb.append(", undoCommand=").append(this.undoCommand);
		sb.append(", undoArguments=").append(this.undoArgumentsString);
		sb.append(", updateTime=").append(updateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
