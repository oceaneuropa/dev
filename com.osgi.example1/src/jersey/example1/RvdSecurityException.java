package jersey.example1;

public class RvdSecurityException extends RvdException {

	private static final long serialVersionUID = 5093333252661623681L;

	public RvdSecurityException() {
	}

	public RvdSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public RvdSecurityException(String message) {
		super(message);
	}

}