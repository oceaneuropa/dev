package osgi.node.client.example;

public class WSClientException extends Exception {

	private static final long serialVersionUID = -7777028999257432010L;

	protected int statusCode;

	/**
	 * 
	 * @param statusCode
	 * @param message
	 * @param cause
	 */
	public WSClientException(int statusCode, String message, Throwable cause) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}
