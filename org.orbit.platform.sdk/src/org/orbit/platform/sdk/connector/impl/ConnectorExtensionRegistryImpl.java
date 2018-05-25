package org.orbit.platform.sdk.connector.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.orbit.platform.sdk.Activator;
import org.orbit.platform.sdk.connector.ConnectorDescriptor;
import org.orbit.platform.sdk.connector.ConnectorExtensionRegistry;
import org.orbit.platform.sdk.connector.ConnectorActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.util.ExtensionListener;
import org.origin.common.extensions.util.ExtensionTracker;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectorExtensionRegistryImpl implements ConnectorExtensionRegistry, ExtensionListener {

	protected static Logger LOG = LoggerFactory.getLogger(ConnectorExtensionRegistryImpl.class);

	public static ConnectorExtensionRegistryImpl INSTANCE = new ConnectorExtensionRegistryImpl();

	protected BundleContext bundleContext;
	protected ExtensionTracker extensionTracker;
	protected Map<String, ConnectorDescriptor> idToDescriptorMap = new HashMap<String, ConnectorDescriptor>();

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
	public ConnectorDescriptor[] getDescriptors() {
		return this.idToDescriptorMap.values().toArray(new ConnectorDescriptor[this.idToDescriptorMap.size()]);
	}

	public synchronized void dispose() {
		LOG.debug("dispose()");

		for (Iterator<String> itor = this.idToDescriptorMap.keySet().iterator(); itor.hasNext();) {
			String id = itor.next();
			ConnectorDescriptor descriptor = this.idToDescriptorMap.get(id);
			if (descriptor != null) {
				ConnectorActivator connectorActivator = descriptor.getConnector();
				if (connectorActivator != null) {
					BundleContext bundleContext = Activator.getInstance().getBundleContext(connectorActivator);
					connectorActivator.stop(bundleContext);
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
		if (ConnectorActivator.TYPE_ID.equals(typeId)) {
			ConnectorDescriptor descriptor = this.idToDescriptorMap.get(id);
			if (descriptor == null) {
				descriptor = new ConnectorDescriptorImpl(extension);
				this.idToDescriptorMap.put(id, descriptor);

				ConnectorActivator connectorActivator = descriptor.getConnector();
				if (connectorActivator != null) {
					BundleContext bundleContext = Activator.getInstance().getBundleContext(connectorActivator);
					connectorActivator.start(bundleContext);
				}
			}
		}
	}

	@Override
	public synchronized void extensionRemoved(IExtension extension) {
		LOG.debug("extensionRemoved()");

		String typeId = extension.getTypeId();
		String id = extension.getId();
		if (ConnectorActivator.TYPE_ID.equals(typeId)) {
			ConnectorDescriptor descriptor = this.idToDescriptorMap.remove(id);
			if (descriptor != null) {

				ConnectorActivator connectorActivator = descriptor.getConnector();
				if (connectorActivator != null) {
					BundleContext bundleContext = Activator.getInstance().getBundleContext(connectorActivator);
					connectorActivator.stop(bundleContext);
				}
			}
		}
	}

}
