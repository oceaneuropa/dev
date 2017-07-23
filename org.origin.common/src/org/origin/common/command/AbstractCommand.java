package org.origin.common.command;

/**
 *
 */
public abstract class AbstractCommand implements ICommand {

	protected String name;
	protected ICommandResult commandResult;
	protected ICommandResult undoResult;

	public AbstractCommand() {
		this.name = getClass().getSimpleName();
	}

	public AbstractCommand(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		if (this.name == null) {
			return this.getClass().getName();
		}
		return this.name;
	}

	@Override
	public boolean isCompositeCommand() {
		return false;
	}

	// ----------------------------------------------------------------------------------------
	// Execute
	// ----------------------------------------------------------------------------------------
	/**
	 * 
	 * @param context
	 * @return
	 * @throws CommandException
	 */
	public abstract ICommandResult execute(CommandContext context) throws CommandException;

	public void setCommandResult(ICommandResult commandResult) {
		this.commandResult = commandResult;
	}

	public ICommandResult getCommandResult() {
		return commandResult;
	}

	// ----------------------------------------------------------------------------------------
	// Undo
	// ----------------------------------------------------------------------------------------
	/**
	 * 
	 * @param context
	 * @throws CommandException
	 */
	public ICommandResult undo(CommandContext context) throws CommandException {
		return null;
	}

	public void setUndoResult(ICommandResult undoResult) {
		this.undoResult = undoResult;
	}

	public ICommandResult getUndoResult() {
		return undoResult;
	}

}
