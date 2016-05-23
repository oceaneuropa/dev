package org.origin.mgm.model.vo;

import java.text.DateFormat;
import java.util.Date;

import org.origin.common.util.DateUtil;

public class IndexItemRequestVO {

	protected Integer requestId;
	protected String indexProviderId;
	protected String command;
	protected String arguments;
	protected String status;
	protected Date requestTime;
	protected Date lastUpdateTime;

	public IndexItemRequestVO() {
	}

	/**
	 * 
	 * @param requestId
	 * @param indexProviderId
	 * @param command
	 * @param arguments
	 * @param status
	 * @param requestTime
	 * @param lastUpdateTime
	 */
	public IndexItemRequestVO(Integer requestId, String indexProviderId, String command, String arguments, String status, Date requestTime, Date lastUpdateTime) {
		this.requestId = requestId;
		this.indexProviderId = indexProviderId;
		this.command = command;
		this.arguments = arguments;
		this.status = status;
		this.requestTime = requestTime;
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(Integer requestId) {
		this.requestId = requestId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	@Override
	public String toString() {
		String requestTimeString = requestTime != null ? DateUtil.toString(requestTime, getDateFormat()) : null;
		String lastUpdateTimeString = lastUpdateTime != null ? DateUtil.toString(lastUpdateTime, getDateFormat()) : null;

		StringBuilder sb = new StringBuilder();
		sb.append("IndexItemRequestVO(");
		sb.append("requestId=").append(this.requestId);
		sb.append(", indexProviderId=").append(this.indexProviderId);
		sb.append(", command=").append(this.command);
		sb.append(", arguments=").append(this.arguments);
		sb.append(", status=").append(this.status);
		sb.append(", requestTime=").append(requestTimeString);
		sb.append(", lastUpdateTime=").append(lastUpdateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
