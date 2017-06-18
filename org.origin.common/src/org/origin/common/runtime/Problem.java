package org.origin.common.runtime;

import java.util.Date;

public class Problem {

	protected Date time;
	protected Exception exception;

	public Problem(Exception exception) {
		this(new Date(), exception);
	}

	public Problem(Date time, Exception exception) {
		this.exception = exception;
	}

	public Date getTime() {
		return time;
	}

	public Exception getException() {
		return exception;
	}

}
