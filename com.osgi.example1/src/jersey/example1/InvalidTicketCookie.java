package jersey.example1;

public class InvalidTicketCookie extends RvdSecurityException {

	private static final long serialVersionUID = 4027698240325251226L;

	public InvalidTicketCookie() {
	}

	public InvalidTicketCookie(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidTicketCookie(String message) {
		super(message);
	}

}
