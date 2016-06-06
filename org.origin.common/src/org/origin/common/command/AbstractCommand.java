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
	public abstract void execute(CommandContext context) throws CommandException;

	/**
	 * 
	 * @param context
	 * @throws CommandException
	 */
	public abstract void undo(CommandContext context) throws CommandException;

}
