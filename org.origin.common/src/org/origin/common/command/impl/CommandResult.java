package org.origin.common.command.impl;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.command.ICommand;
import org.origin.common.command.ICommandResult;
import org.origin.common.runtime.IStatus;

public class CommandResult implements ICommandResult {

	/**
	 * The command that returns the command result.
	 */
	protected ICommand command;

	/**
	 * The status of this command.
	 */
	protected IStatus status;

	/**
	 * The return value for this command, if applicable.
	 */
	protected Object returnValue;

	/**
	 * Constructs a new command result with the specified status and a default return value.
	 * 
	 * @param command
	 *            The command that returns the command result.
	 * @param status
	 *            The status for the command result.
	 */
	public CommandResult(ICommand command, IStatus status) {
		assert null != status : "null status"; //$NON-NLS-1$
		this.command = command;
		this.status = status;
	}

	/**
	 * Constructs a new command result with the specified status and return value.
	 * 
	 * @param command
	 *            The command that returns the command result.
	 * @param status
	 *            The status for the new command result.
	 * @param returnValue
	 *            The return value for the new command result.
	 */
	public CommandResult(ICommand command, IStatus status, Object returnValue) {
		assert null != status : "null status"; //$NON-NLS-1$
		this.command = command;
		this.status = status;
		this.returnValue = returnValue;
	}

	/**
	 * Get the command that returns the command result.
	 * 
	 * @return
	 */
	public ICommand getCommand() {
		return this.command;
	}

	/**
	 * Retrieves the status of the command that is executed, undone or redone.
	 * 
	 * @return The status.
	 */
	public IStatus getStatus() {
		return status;
	}

	/**
	 * The value returned by the execute, undo or redo of a GMF operation.
	 * 
	 * @return the return value; may be null
	 */
	public Object getReturnValue() {
		return returnValue;
	}

	@Override
	public boolean isCompositeResult() {
		return false;
	}

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}
