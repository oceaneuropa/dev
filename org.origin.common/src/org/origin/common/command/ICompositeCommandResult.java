package org.origin.common.command;

public interface ICompositeCommandResult extends ICommandResult {

	/**
	 * 
	 * @param commandClass
	 * @return
	 */
	public Object getReturnValue(Class<?> commandClass);

	/**
	 * 
	 * @param commandClass
	 * @return
	 */
	public Object[] getReturnValues(Class<?> commandClass);

	/**
	 * 
	 * @param commandResult
	 */
	public void add(ICommandResult commandResult);

	/**
	 * 
	 * @param commandResult
	 */
	public void remove(ICommandResult commandResult);

	/**
	 * 
	 * @param includingNestedResults
	 * @return
	 */
	public ICommandResult[] getCommandResults(boolean includingNestedResults);

}
