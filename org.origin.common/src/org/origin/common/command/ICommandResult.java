package org.origin.common.command;

import org.origin.common.adapter.IAdaptable;

public interface ICommandResult extends IAdaptable {

	/**
	 * Retrieves the status of the command that is executed, undone or redone.
	 * 
	 * @return The status.
	 */
	public Object getResult();

	/**
	 * The value returned by the execute, undo or redo of a GMF operation.
	 * 
	 * @return the return value; may be null
	 */
	public Object getReturnValue();

	/**
	 * Check whether the command result is a composite result which contains a list of children results.
	 * 
	 * @return
	 */
	public boolean isCompositeResult();

}

/// **
// * Get the command that returns the command result.
// *
// * @return
// */
// public ICommand getCommand();
