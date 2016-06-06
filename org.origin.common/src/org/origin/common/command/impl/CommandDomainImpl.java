package org.origin.common.command.impl;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.command.CommandStack;
import org.origin.common.command.EditingDomain;

public class CommandDomainImpl extends EditingDomain {

	protected String name;
	protected CommandStack commandStack;
	protected Map<Object, CommandStack> commandStackMap = new HashMap<Object, CommandStack>();

	/**
	 * 
	 * @param name
	 */
	public CommandDomainImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public synchronized CommandStack getCommandStack() {
		if (commandStack == null) {
			commandStack = new CommandStackImpl();
		}
		return this.commandStack;
	}

	@Override
	public synchronized CommandStack getCommandStack(Object owner) {
		CommandStack commandStack = this.commandStackMap.get(owner);
		if (commandStack == null) {
			commandStack = new CommandStackImpl();
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
