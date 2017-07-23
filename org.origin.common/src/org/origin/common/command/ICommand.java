package org.origin.common.command;

public interface ICommand {

	/**
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 
	 * @return
	 */
	public boolean isCompositeCommand();

	// ----------------------------------------------------------------------------------------
	// Execute
	// ----------------------------------------------------------------------------------------
	/**
	 * 
	 * @param context
	 * @return
	 * @throws CommandException
	 */
	public ICommandResult execute(CommandContext context) throws CommandException;

	/**
	 * 
	 * @param commandResult
	 */
	public void setCommandResult(ICommandResult commandResult);

	/**
	 * 
	 * @return
	 */
	public ICommandResult getCommandResult();

	// ----------------------------------------------------------------------------------------
	// Undo
	// ----------------------------------------------------------------------------------------
	/**
	 * 
	 * @param context
	 * @throws CommandException
	 */
	public ICommandResult undo(CommandContext context) throws CommandException;

	/**
	 * 
	 * @param undoResult
	 */
	public void setUndoResult(ICommandResult undoResult);

	/**
	 * 
	 * @return
	 */
	public ICommandResult getUndoResult();

}
