package org.orbit.component.cli.tier3;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.orbit.component.api.tier3.domain.TransferAgentConfig;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier3.transferagent.TransferAgentConnector;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.mgm.client.connector.ServiceConnectorFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.util.tracker.ServiceTracker;

public class TransferAgentCLICommand implements Annotated {

	protected BundleContext bundleContext;
	protected ServiceTracker<ServiceConnectorFactory<TransferAgentConnector>, ServiceConnectorFactory<TransferAgentConnector>> connectorFactoryServiceTracker;
	protected Map<String, TransferAgentConnector> connectorMap = new HashMap<String, TransferAgentConnector>();

	@Dependency
	protected DomainManagementConnector domainMgmtConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	public TransferAgentCLICommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("TransferAgentCLICommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function", new String[] { //
				"lservices" //
		});

		OSGiServiceUtil.register(this.bundleContext, TransferAgentCLICommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);

		try {
			String FILTER_STRING = "(&(" + Constants.OBJECTCLASS + "=" + ServiceConnectorFactory.class.getName() + ")(type=" + TransferAgentConnector.class.getName() + "))";
			Filter filter = this.bundleContext.createFilter(FILTER_STRING);
			this.connectorFactoryServiceTracker = new ServiceTracker<ServiceConnectorFactory<TransferAgentConnector>, ServiceConnectorFactory<TransferAgentConnector>>(this.bundleContext, filter, null);
			this.connectorFactoryServiceTracker.open();

		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		System.out.println("TransferAgentCLICommand.stop()");

		if (this.connectorFactoryServiceTracker != null) {
			this.connectorFactoryServiceTracker.close();
			this.connectorFactoryServiceTracker = null;
		}

		OSGiServiceUtil.unregister(TransferAgentCLICommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @return
	 * @throws ClientException
	 */
	protected TransferAgent getTransferAgent(String machineId, String transferAgentId) throws ClientException {
		TransferAgent transferAgent = null;
		TransferAgentConnector connector = getTransferAgentConnector(machineId, transferAgentId);
		if (connector != null) {
			transferAgent = connector.getService();
		}
		return transferAgent;
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @return
	 * @throws ClientException
	 */
	protected TransferAgentConnector getTransferAgentConnector(String machineId, String transferAgentId) throws ClientException {
		String key = machineId + "#" + transferAgentId;

		TransferAgentConnector connector = this.connectorMap.get(key);
		if (connector == null) {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			TransferAgentConfig taConfig = domainMgmt.getTransferAgentConfig(machineId, transferAgentId);
			if (taConfig != null) {

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
		}

		return connector;
	}

	/**
	 * 
	 * @param domainMgmt
	 */
	protected void print(DomainManagement domainMgmt) {
		if (domainMgmt == null) {
			System.out.println("DomainManagement service is null.");
			return;
		} else {
			System.out.println(domainMgmt.getName() + " (" + domainMgmt.getURL() + ")");
		}
	}

}
