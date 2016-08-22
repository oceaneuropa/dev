package org.origin.common.command;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.command.impl.EditingDomain;

public abstract class IEditingDomain {

	protected static Map<String, IEditingDomain> editingDomainMap = new HashMap<String, IEditingDomain>();

	/**
	 * Get a CommandDomain by domain name.
	 * 
	 * @param domainName
	 * @return
	 */
	public synchronized static IEditingDomain getEditingDomain(String domainName) {
		IEditingDomain commandDomain = editingDomainMap.get(domainName);
		if (commandDomain == null) {
			commandDomain = new EditingDomain(domainName);
			editingDomainMap.put(domainName, commandDomain);
		}
		return commandDomain;
	}

	/**
	 * 
	 * @param domainName
	 * @return
	 */
	public synchronized static IEditingDomain disposeEditingDomain(String domainName) {
		IEditingDomain commandDomain = editingDomainMap.get(domainName);
		if (commandDomain != null) {
			editingDomainMap.remove(domainName);
			commandDomain.dispose();
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
	public abstract ICommandStack getCommandStack();

	/**
	 * Get a CommandStack by owner.
	 * 
	 * @param owner
	 * @return
	 */
	public abstract ICommandStack getCommandStack(Object owner);

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
