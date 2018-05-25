package org.orbit.platform.sdk.command.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.orbit.platform.sdk.Activator;
import org.orbit.platform.sdk.command.CommandDescriptor;
import org.orbit.platform.sdk.command.CommandExtensionRegistry;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.util.ExtensionListener;
import org.origin.common.extensions.util.ExtensionTracker;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandExtensionRegistryImpl implements CommandExtensionRegistry, ExtensionListener {

	protected static Logger LOG = LoggerFactory.getLogger(CommandExtensionRegistryImpl.class);

	public static CommandExtensionRegistryImpl INSTANCE = new CommandExtensionRegistryImpl();

	protected BundleContext bundleContext;
	protected ExtensionTracker extensionTracker;
	protected Map<String, CommandDescriptor> idToDescriptorMap = new HashMap<String, CommandDescriptor>();

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void start(BundleContext bundleContext) throws Exception {
		LOG.debug("start()");

		this.bundleContext = bundleContext;

		// Start tracking extensions
		this.extensionTracker = new ExtensionTracker();
		this.extensionTracker.addListener(this);
		this.extensionTracker.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.debug("stop()");

		// Stop tracking extensions
		if (this.extensionTracker != null) {
			this.extensionTracker.stop(bundleContext);
			this.extensionTracker.removeListener(this);
			this.extensionTracker = null;
		}

		this.bundleContext = null;
	}

	@Override
	public CommandDescriptor[] getDescriptors() {
		return this.idToDescriptorMap.values().toArray(new CommandDescriptor[this.idToDescriptorMap.size()]);
	}

	public synchronized void dispose() {
		LOG.debug("dispose()");

		for (Iterator<String> itor = this.idToDescriptorMap.keySet().iterator(); itor.hasNext();) {
			String id = itor.next();
			CommandDescriptor descriptor = this.idToDescriptorMap.get(id);
			if (descriptor != null) {
				CommandActivator commandActivator = descriptor.getCommand();
				if (commandActivator != null) {
					BundleContext bundleContext = Activator.getInstance().getBundleContext(commandActivator);
					commandActivator.stop(bundleContext);
				}
			}
		}
		this.idToDescriptorMap.clear();
	}

	/**
	 * Implement ExtensionListener interface
	 */
	@Override
	public synchronized void extensionAdded(IExtension extension) {
		LOG.debug("extensionAdded()");

		String typeId = extension.getTypeId();
		String id = extension.getId();
		if (CommandActivator.TYPE_ID.equals(typeId)) {
			CommandDescriptor descriptor = this.idToDescriptorMap.get(id);
			if (descriptor == null) {
				descriptor = new CommandDescriptorImpl(extension);
				this.idToDescriptorMap.put(id, descriptor);

				CommandActivator commandActivator = descriptor.getCommand();
				if (commandActivator != null) {
					BundleContext bundleContext = Activator.getInstance().getBundleContext(commandActivator);
					commandActivator.start(bundleContext);
				}
			}
		}
	}

	@Override
	public synchronized void extensionRemoved(IExtension extension) {
		LOG.debug("extensionRemoved()");

		String typeId = extension.getTypeId();
		String id = extension.getId();
		if (CommandActivator.TYPE_ID.equals(typeId)) {
			CommandDescriptor descriptor = this.idToDescriptorMap.remove(id);
			if (descriptor != null) {
				CommandActivator commandActivator = descriptor.getCommand();
				if (commandActivator != null) {
					BundleContext bundleContext = Activator.getInstance().getBundleContext(commandActivator);
					commandActivator.stop(bundleContext);
				}
			}
		}
	}

}
