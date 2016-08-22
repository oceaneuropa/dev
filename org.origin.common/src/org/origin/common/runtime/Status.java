package org.origin.common.runtime;

public class Status implements IStatus {

	/**
	 * Constant used to indicate an unknown plugin id.
	 */
	private static final String unknownId = "unknown"; //$NON-NLS-1$

	/**
	 * A standard OK status with an "ok" message.
	 */
	public static final IStatus OK_STATUS = new Status(OK, unknownId, OK, "ok", null); //$NON-NLS-1$

	/**
	 * A standard CANCEL status with no message.
	 */
	public static final IStatus CANCEL_STATUS = new Status(CANCEL, unknownId, 1, "", null); //$NON-NLS-1$

	/**
	 * Constant to avoid generating garbage.
	 */
	private static final IStatus[] theEmptyStatusArray = new IStatus[0];

	/**
	 * The severity. One of OK, ERROR, INFO, WARNING, or CANCEL.
	 */
	private int severity = OK;

	/**
	 * Unique identifier of plug-in.
	 */
	private String pluginId;

	/**
	 * Plug-in-specific status code.
	 */
	private int code;

	/**
	 * Message, localized to the current locale.
	 */
	private String message;

	/**
	 * Wrapped exception, or null if none.
	 */
	private Throwable exception;

	/**
	 * Simplified constructor of a new status object; assumes that code is <code>OK</code> and exception is <code>null</code>. The created status has
	 * no children.
	 *
	 * @param severity
	 *            one of OK, ERROR, INFO, WARNING, or CANCEL
	 * @param pluginId
	 *            the unique identifier of the relevant plug-in
	 * @param message
	 *            a human-readable message, localized to the current locale
	 */
	public Status(int severity, String pluginId, String message) {
		setSeverity(severity);
		setPlugin(pluginId);
		setMessage(message);
		setCode(OK);
		setException(null);
	}

	/**
	 * Simplified constructor of a new status object; assumes that code is <code>OK</code>. The created status has no children.
	 *
	 * @param severity
	 *            one of OK, ERROR, INFO, WARNING, or CANCEL
	 * @param pluginId
	 *            the unique identifier of the relevant plug-in
	 * @param message
	 *            a human-readable message, localized to the current locale
	 * @param exception
	 *            a low-level exception, or null if not applicable
	 */
	public Status(int severity, String pluginId, String message, Throwable exception) {
		setSeverity(severity);
		setPlugin(pluginId);
		setMessage(message);
		setException(exception);
		setCode(OK);
	}

	/**
	 * Creates a new status object. The created status has no children.
	 *
	 * @param severity
	 *            one of OK, ERROR, INFO, WARNING, or CANCEL
	 * @param pluginId
	 *            the unique identifier of the relevant plug-in
	 * @param code
	 *            the plug-in-specific status code, or <code>OK</code>
	 * @param message
	 *            a human-readable message, localized to the current locale
	 * @param exception
	 *            a low-level exception, or null if not applicable
	 */
	public Status(int severity, String pluginId, int code, String message, Throwable exception) {
		setSeverity(severity);
		setPlugin(pluginId);
		setCode(code);
		setMessage(message);
		setException(exception);
	}

	public IStatus[] getChildren() {
		return theEmptyStatusArray;
	}

	public int getCode() {
		return code;
	}

	public Throwable getException() {
		return exception;
	}

	public String getMessage() {
		return message;
	}

	public String getPlugin() {
		return pluginId;
	}

	public int getSeverity() {
		return severity;
	}

	public boolean isMultiStatus() {
		return false;
	}

	public boolean isOK() {
		return severity == OK;
	}

	public boolean matches(int severityMask) {
		return (severity & severityMask) != 0;
	}

	/**
	 * Sets the status code.
	 *
	 * @param code
	 *            the plug-in-specific status code, or OK
	 */
	protected void setCode(int code) {
		this.code = code;
	}

	/**
	 * Sets the exception.
	 *
	 * @param exception
	 *            a low-level exception, or null if not applicable
	 */
	protected void setException(Throwable exception) {
		this.exception = exception;
	}

	/**
	 * Sets the message. If null is passed, message is set to an empty string.
	 *
	 * @param message
	 *            a human-readable message, localized to the current locale
	 */
	protected void setMessage(String message) {
		if (message == null) {
			this.message = ""; //$NON-NLS-1$
		} else {
			this.message = message;
		}
	}

	/**
	 * Sets the plug-in id.
	 *
	 * @param pluginId
	 *            the unique identifier of the relevant plug-in
	 */
	protected void setPlugin(String pluginId) {
		this.pluginId = pluginId;
	}

	/**
	 * Sets the severity.
	 *
	 * @param severity
	 *            one of OK, ERROR, INFO, WARNING, or CANCEL
	 */
	protected void setSeverity(int severity) {
		assert (severity == OK || severity == ERROR || severity == WARNING || severity == INFO || severity == CANCEL) : "severity is invalid";
		this.severity = severity;
	}

	/**
	 * Returns a string representation of the status, suitable for debugging purposes only.
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Status "); //$NON-NLS-1$
		if (severity == OK) {
			buf.append("OK"); //$NON-NLS-1$
		} else if (severity == ERROR) {
			buf.append("ERROR"); //$NON-NLS-1$
		} else if (severity == WARNING) {
			buf.append("WARNING"); //$NON-NLS-1$
		} else if (severity == INFO) {
			buf.append("INFO"); //$NON-NLS-1$
		} else if (severity == CANCEL) {
			buf.append("CANCEL"); //$NON-NLS-1$
		} else {
			buf.append("severity="); //$NON-NLS-1$
			buf.append(severity);
		}
		buf.append(": "); //$NON-NLS-1$
		buf.append(pluginId);
		buf.append(" code="); //$NON-NLS-1$
		buf.append(code);
		buf.append(' ');
		buf.append(message);
		buf.append(' ');
		buf.append(exception);
		return buf.toString();
	}

}
