package org.origin.common.command;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public abstract class AbstractCommand {

	/**
	 * 
	 * @param context
	 * @return
	 * @throws CommandException
	 */
	public abstract AbstractCommand execute(CommandContext context) throws CommandException;

}
