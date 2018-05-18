package other.orbit.commponent.model.tier1.auth;

public class AuthException extends Exception {

	private static final long serialVersionUID = 5905004230199778308L;

	protected String error;
	protected String error_description;
	protected String error_url;

	public AuthException(String error, String error_description) {
		this.error = error;
		this.error_description = error_description;
	}

	public AuthException(Throwable throwable, String error, String error_description) {
		super(throwable);
		this.error = error;
		this.error_description = error_description;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public String getError_url() {
		return error_url;
	}

	public void setError_url(String error_url) {
		this.error_url = error_url;
	}

}
