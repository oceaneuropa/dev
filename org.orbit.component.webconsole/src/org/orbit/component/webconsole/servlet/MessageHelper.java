package org.orbit.component.webconsole.servlet;

public class MessageHelper {

	public static MessageHelper INSTANCE = new MessageHelper();

	/**
	 * 
	 * @param message
	 * @return
	 */
	protected String check(String message) {
		if (message == null) {
			message = "";
		}
		if (!message.isEmpty()) {
			message += " ";
		}
		return message;
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	public String add(String message, String newMessage) {
		if (message == null) {
			message = "";
		}

		if (newMessage == null || newMessage.isEmpty()) {
			return message;
		}

		if (!message.isEmpty()) {
			message += " ";
		}
		message += newMessage;

		return message;
	}

}
