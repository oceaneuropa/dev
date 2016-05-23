package org.origin.mgm.model.vo;

public class IndexItemRequestVO {

	protected Integer requestId;
	protected String indexProviderId;
	protected String command;
	protected String arguments;
	protected String status;
	protected String requestTimeString;
	protected String lastUpdateTimeString;

	public IndexItemRequestVO() {
	}

	/**
	 * 
	 * @param requestId
	 * @param indexProviderId
	 * @param command
	 * @param arguments
	 * @param status
	 * @param requestTimeString
	 * @param lastUpdateTimeString
	 */
	public IndexItemRequestVO(Integer requestId, String indexProviderId, String command, String arguments, String status, String requestTimeString, String lastUpdateTimeString) {
		this.requestId = requestId;
		this.indexProviderId = indexProviderId;
		this.command = command;
		this.arguments = arguments;
		this.status = status;
		this.requestTimeString = requestTimeString;
		this.lastUpdateTimeString = lastUpdateTimeString;
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

	public String getRequestTimeString() {
		return requestTimeString;
	}

	public void setRequestTimeString(String requestTimeString) {
		this.requestTimeString = requestTimeString;
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
		sb.append("IndexItemRequestVO(");
		sb.append("requestId=").append(this.requestId);
		sb.append("indexProviderId=").append(this.indexProviderId);
		sb.append(", command=").append(this.command);
		sb.append(", arguments=").append(this.arguments);
		sb.append(", status=").append(this.status);
		sb.append(", requestTimeString=").append(this.requestTimeString);
		sb.append(", lastUpdateTimeString=").append(this.lastUpdateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
