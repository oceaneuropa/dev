package org.origin.common.command.impl;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.command.ICommandResult;
import org.origin.common.command.ICompositeCommandResult;
import org.origin.common.runtime.IStatus;
import org.origin.common.runtime.Status;

public class CompositeCommandResult extends CommandResult implements ICompositeCommandResult {

	protected static final CommandResult[] EMPTY_RESULTS = new CommandResult[0];

	protected List<ICommandResult> commandResults;

	public CompositeCommandResult() {
		super(Status.OK_STATUS);
	}

	@Override
	public IStatus getResult() {
		ICommandResult[] commandResults = getCommandResults(false);
		for (ICommandResult commandResult : commandResults) {
			// IStatus currStatus = commandResult.getResult();
		}
		return null;
	}

	@Override
	public boolean isCompositeResult() {
		return true;
	}

	/**
	 * 
	 * @param commandResult
	 */
	@Override
	public void add(ICommandResult commandResult) {
		if (commandResult != null && !commandResults.contains(commandResult)) {
			this.commandResults.add(commandResult);
		}
	}

	/**
	 * 
	 * @param commandResult
	 */
	@Override
	public void remove(ICommandResult commandResult) {
		if (commandResult != null && commandResults.contains(commandResult)) {
			this.commandResults.remove(commandResult);
		}
	}

	/**
	 * 
	 * @param includingNestedResults
	 * @return
	 */
	@Override
	public ICommandResult[] getCommandResults(boolean includingNestedResults) {
		ICommandResult[] commandResults = null;
		if (includingNestedResults) {
			commandResults = getAllCommandResults();

		} else {
			if (!this.commandResults.isEmpty()) {
				commandResults = this.commandResults.toArray(new CommandResult[this.commandResults.size()]);
			}
		}
		if (commandResults == null) {
			commandResults = EMPTY_RESULTS;
		}
		return commandResults;
	}

	/**
	 * 
	 * @return
	 */
	protected ICommandResult[] getAllCommandResults() {
		List<ICommandResult> allResults = new ArrayList<ICommandResult>();
		List<ICommandResult> encounteredResults = new ArrayList<ICommandResult>();

		for (ICommandResult currResult : this.commandResults) {
			if (currResult instanceof ICompositeCommandResult) {
				getAllCommandResults((ICompositeCommandResult) currResult, allResults, encounteredResults);
			} else {
				allResults.add(currResult);
			}
		}

		return allResults.toArray(new CommandResult[allResults.size()]);
	}

	/**
	 * 
	 * @param compositeResult
	 * @param allResults
	 * @param encounteredResults
	 */
	protected void getAllCommandResults(ICompositeCommandResult compositeResult, List<ICommandResult> allResults, List<ICommandResult> encounteredResults) {
		if (compositeResult == null || encounteredResults.contains(compositeResult)) {
			return;
		}
		encounteredResults.add(compositeResult);

		for (ICommandResult currResult : compositeResult.getCommandResults(false)) {
			if (currResult instanceof ICompositeCommandResult) {
				getAllCommandResults((ICompositeCommandResult) currResult, allResults, encounteredResults);
			} else {
				allResults.add(currResult);
			}
		}
	}

}

// @Override
// public Object getReturnValue(Class<?> commandClass) {
// Object returnValue = null;
// Object[] returnValues = getReturnValues(commandClass);
// if (returnValues != null && returnValues.length > 0) {
// returnValue = returnValues[0];
// }
// return returnValue;
// }
//
// @Override
// public Object[] getReturnValues(Class<?> commandClass) {
// List<Object> list = new ArrayList<Object>();
// ICommandResult[] commandResults = getCommandResults(true);
// for (ICommandResult commandResult : commandResults) {
// ICommand command = commandResult.getCommand();
// Object returnValue = commandResult.getReturnValue();
// if (command != null && commandClass.isAssignableFrom(command.getClass()) && returnValue != null) {
// list.add(returnValue);
// }
// }
// return list.toArray(new Object[list.size()]);
// }
