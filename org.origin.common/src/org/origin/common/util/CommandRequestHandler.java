package org.origin.common.util;

import java.util.Map;

public interface CommandRequestHandler {

	/**
	 * Check whether a command is supported.
	 * 
	 * @param command
	 *            a command to be checked.
	 * @param parameters
	 *            optional parameters of the command.
	 * @return
	 */
	public boolean isCommandSupported(String command, Map<String, Object> parameters);

	/**
	 * Perform a command.
	 * 
	 * @param command
	 *            a command to be performed.
	 * @param parameters
	 *            optional parameters of the command.
	 * @return
	 */
	public boolean performCommand(String command, Map<String, Object> parameters);

}
