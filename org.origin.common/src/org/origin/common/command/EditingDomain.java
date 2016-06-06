package org.origin.common.command;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.command.impl.CommandDomainImpl;

public abstract class EditingDomain {

	protected static Map<String, EditingDomain> commandNameToCommandDomainMap = new HashMap<String, EditingDomain>();

	/**
	 * Get a CommandDomain by domain name.
	 * 
	 * @param domainName
	 * @return
	 */
	public synchronized static EditingDomain getEditingDomain(String domainName) {
		EditingDomain commandDomain = commandNameToCommandDomainMap.get(domainName);
		if (commandDomain == null) {
			commandDomain = new CommandDomainImpl(domainName);
			commandNameToCommandDomainMap.put(domainName, commandDomain);
		}
		return commandDomain;
	}

	/**
	 * Get the name of the editing domain.
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * Get a default CommandStack of the editing domain.
	 * 
	 * @return
	 */
	public abstract CommandStack getCommandStack();

	/**
	 * Get a CommandStack by owner.
	 * 
	 * @param owner
	 * @return
	 */
	public abstract CommandStack getCommandStack(Object owner);

	/**
	 * Dispose a CommandStack by owner.
	 * 
	 * @param owner
	 */
	public abstract void disposeCommandStack(Object owner);

	/**
	 * Dispose the editing domain. This will dispose all CommandStacks in the editing domain, including the default CommandStack and the CommandStacks
	 * created by owners.
	 */
	public abstract void dispose();

}
