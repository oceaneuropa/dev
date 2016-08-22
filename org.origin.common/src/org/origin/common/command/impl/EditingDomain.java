package org.origin.common.command.impl;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.command.ICommandStack;
import org.origin.common.command.IEditingDomain;

public class EditingDomain extends IEditingDomain {

	protected String name;
	protected ICommandStack commandStack;
	protected Map<Object, ICommandStack> commandStackMap = new HashMap<Object, ICommandStack>();

	/**
	 * 
	 * @param name
	 */
	public EditingDomain(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public synchronized ICommandStack getCommandStack() {
		if (commandStack == null) {
			commandStack = new CommandStack();
		}
		return this.commandStack;
	}

	@Override
	public synchronized ICommandStack getCommandStack(Object owner) {
		ICommandStack commandStack = this.commandStackMap.get(owner);
		if (commandStack == null) {
			commandStack = new CommandStack();
			this.commandStackMap.put(owner, commandStack);
		}
		return commandStack;
	}

	@Override
	public synchronized void disposeCommandStack(Object owner) {
		this.commandStackMap.remove(owner);
	}

	@Override
	public synchronized void dispose() {
		this.commandStack = null;
		this.commandStackMap.clear();
	}

}
