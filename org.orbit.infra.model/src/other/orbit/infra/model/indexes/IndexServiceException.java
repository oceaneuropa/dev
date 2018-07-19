package other.orbit.infra.model.indexes;

public class IndexServiceException extends Exception {

	private static final long serialVersionUID = 365480828042389121L;

	protected String code;

	public IndexServiceException(String code, String message) {
		super(message);
		this.code = code;
	}

	public IndexServiceException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public IndexServiceException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
