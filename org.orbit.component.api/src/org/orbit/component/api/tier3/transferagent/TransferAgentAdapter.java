package org.orbit.component.api.tier3.transferagent;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier3.domain.TransferAgentConfig;
import org.origin.mgm.client.connector.ServiceConnectorFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

public class TransferAgentAdapter {

	protected ServiceTracker<ServiceConnectorFactory<TransferAgentConnector>, ServiceConnectorFactory<TransferAgentConnector>> connectorFactoryServiceTracker;
	protected Map<String, TransferAgentConnector> connectorMap = new HashMap<String, TransferAgentConnector>();

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		System.out.println("TransferAgentAdapter.start()");

		try {
			String FILTER_STRING = "(&(" + Constants.OBJECTCLASS + "=" + ServiceConnectorFactory.class.getName() + ")(type=" + TransferAgentConnector.class.getName() + "))";
			Filter filter = bundleContext.createFilter(FILTER_STRING);
			this.connectorFactoryServiceTracker = new ServiceTracker<ServiceConnectorFactory<TransferAgentConnector>, ServiceConnectorFactory<TransferAgentConnector>>(bundleContext, filter, null);
			this.connectorFactoryServiceTracker.open();

		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		System.out.println("TransferAgentAdapter.stop()");

		if (this.connectorFactoryServiceTracker != null) {
			this.connectorFactoryServiceTracker.close();
			this.connectorFactoryServiceTracker = null;
		}
	}

	/**
	 * 
	 * @param taConfig
	 * @return
	 */
	public TransferAgent getTransferAgent(TransferAgentConfig taConfig) {
		TransferAgent transferAgent = null;
		TransferAgentConnector connector = getTransferAgentConnector(taConfig);
		if (connector != null) {
			transferAgent = connector.getService();
		}
		return transferAgent;
	}

	/**
	 * 
	 * @param taConfig
	 * @return
	 */
	public TransferAgentConnector getTransferAgentConnector(TransferAgentConfig taConfig) {
		String machineId = taConfig.getMachineId();
		String transferAgentId = taConfig.getId();
		String key = machineId + "#" + transferAgentId;

		TransferAgentConnector connector = this.connectorMap.get(key);
		if (connector == null) {
			ServiceConnectorFactory<TransferAgentConnector> connectorFactory = this.connectorFactoryServiceTracker.getService();
			if (connectorFactory != null) {
				Map<Object, Object> properties = new HashMap<Object, Object>();
				properties.put(TransferAgentConfig.class.getName(), taConfig);
				connector = connectorFactory.create(properties);

				if (connector != null) {
					this.connectorMap.put(key, connector);
				}
			}
		}

		return connector;
	}

}

// try {
// String FILTER_STRING = "(&(" + Constants.OBJECTCLASS + "=" + ServiceConnectorFactory.class.getName() + ")(type=" + TransferAgentConnector.class.getName() +
// "))";
// Filter filter = this.bundleContext.createFilter(FILTER_STRING);
// this.connectorFactoryServiceTracker = new ServiceTracker<ServiceConnectorFactory<TransferAgentConnector>,
// ServiceConnectorFactory<TransferAgentConnector>>(this.bundleContext, filter, null);
// this.connectorFactoryServiceTracker.open();
//
// } catch (InvalidSyntaxException e) {
// e.printStackTrace();
// }

// if (this.connectorFactoryServiceTracker != null) {
// this.connectorFactoryServiceTracker.close();
// this.connectorFactoryServiceTracker = null;
// }