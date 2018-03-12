package org.origin.common.command.impl;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.command.ICommandResult;

public class CommandResult implements ICommandResult {

	/**
	 * The status of this command.
	 */
	protected Object result;

	/**
	 * The return value for this command, if applicable.
	 */
	protected Object returnValue;

	/**
	 * Constructs a new command result with the specified status and a default return value.
	 * 
	 * @param command
	 *            The command that returns the command result.
	 * @param result
	 *            The status for the command result.
	 */
	public CommandResult(Object result) {
		assert null != result : "null status"; //$NON-NLS-1$
		this.result = result;
	}

	/**
	 * Constructs a new command result with the specified status and return value.
	 * 
	 * @param command
	 *            The command that returns the command result.
	 * @param result
	 *            The status for the new command result.
	 * @param returnValue
	 *            The return value for the new command result.
	 */
	public CommandResult(Object result, Object returnValue) {
		assert null != result : "null status"; //$NON-NLS-1$
		this.result = result;
		this.returnValue = returnValue;
	}

	/**
	 * Retrieves the status of the command that is executed, undone or redone.
	 * 
	 * @return The status.
	 */
	public Object getResult() {
		return this.result;
	}

	/**
	 * The value returned by the execute, undo or redo of a GMF operation.
	 * 
	 * @return the return value; may be null
	 */
	public Object getReturnValue() {
		return this.returnValue;
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
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}
