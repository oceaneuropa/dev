package org.origin.mgm.model.vo;

import java.text.DateFormat;
import java.util.Date;

import org.origin.common.util.DateUtil;

public class IndexItemRevisionVO {

	protected Integer revisionId;
	protected String indexProviderId;
	protected String command;
	protected String arguments;
	protected String undoCommand;
	protected String undoArguments;
	protected Date updateTime;

	public IndexItemRevisionVO() {
	}

	/**
	 * 
	 * @param revisionId
	 * @param indexProviderId
	 * @param command
	 * @param arguments
	 * @param undoCommand
	 * @param undoArguments
	 * @param updateTime
	 */
	public IndexItemRevisionVO(Integer revisionId, String indexProviderId, String command, String arguments, String undoCommand, String undoArguments, Date updateTime) {
		this.revisionId = revisionId;
		this.indexProviderId = indexProviderId;
		this.command = command;
		this.arguments = arguments;
		this.undoCommand = undoCommand;
		this.undoArguments = undoArguments;
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
		sb.append(", arguments=").append(this.arguments);
		sb.append(", undoCommand=").append(this.undoCommand);
		sb.append(", undoArguments=").append(this.undoArguments);
		sb.append(", updateTime=").append(updateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
