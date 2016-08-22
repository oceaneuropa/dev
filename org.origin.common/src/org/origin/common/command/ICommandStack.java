package org.origin.common.command;

public interface ICommandStack {

	/**
	 * Execute a command.
	 * 
	 * @param context
	 * @param command
	 * @throws CommandException
	 */
	public ICommandResult execute(CommandContext context, ICommand command) throws CommandException;

	/**
	 * Check whether there is command that can be undo.
	 * 
	 * @return
	 */
	public boolean canUndo();

	/**
	 * Get the size of undoable commands.
	 * 
	 * @return
	 */
	public int getUndoableSize();

	/**
	 * Peek the current available undo command. Do not execute the command directly.
	 * 
	 * @return
	 */
	public ICommand peekUndoCommand();

	/**
	 * Undo the last executed command.
	 * 
	 * @param context
	 * @throws CommandException
	 */
	public ICommandResult undo(CommandContext context) throws CommandException;

	/**
	 * Check whether there is command that can be redo.
	 * 
	 * @return
	 */
	public boolean canRedo();

	/**
	 * Get the size of redoable commands.
	 * 
	 * @return
	 */
	public int getRedoableSize();

	/**
	 * Peek the current available redo command. Do not execute the command directly.
	 * 
	 * @return
	 */
	public ICommand peekRedoCommand();

	/**
	 * Redo the last undo command.
	 * 
	 * @param context
	 * @throws CommandException
	 */
	public ICommandResult redo(CommandContext context) throws CommandException;

}
