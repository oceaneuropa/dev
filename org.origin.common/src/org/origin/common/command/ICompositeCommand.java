package org.origin.common.command;

public interface ICompositeCommand extends ICommand {

	/**
	 * 
	 * @param command
	 */
	public abstract void add(ICommand command);

	/**
	 * 
	 * @param command
	 */
	public abstract void remove(ICommand command);

	/**
	 * 
	 * @return
	 */
	public abstract boolean isEmpty();

	/**
	 * 
	 * @return
	 */
	public abstract int size();

	/**
	 * 
	 * @return
	 */
	public ICommand[] getChildren();

}
