package org.orbit.component.model.tier4.mission.rto;

public class MissionException extends Exception {

	private static final long serialVersionUID = 4626668931566337172L;

	protected String code;

	public MissionException(String code, String message) {
		super(message);
		this.code = code;
	}

	public MissionException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public MissionException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

}
