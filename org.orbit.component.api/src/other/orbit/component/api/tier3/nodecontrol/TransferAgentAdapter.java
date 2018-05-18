package other.orbit.component.api.tier3.nodecontrol;

public class TransferAgentAdapter {

	// protected ServiceTracker<ServiceConnectorFactory<TransferAgentConnector>, ServiceConnectorFactory<TransferAgentConnector>>
	// connectorFactoryServiceTracker;
	// protected Map<String, TransferAgentConnector> connectorMap = new HashMap<String, TransferAgentConnector>();
	// protected ServiceTracker<TransferAgentConnector, TransferAgentConnector> connectorTracker;

	// /**
	// *
	// * @param bundleContext
	// */
	// public void start(BundleContext bundleContext) {
	// System.out.println("TransferAgentAdapter.start()");
	//
	// try {
	// // String FILTER_STRING = "(&(" + Constants.OBJECTCLASS + "=" + ServiceConnectorFactory.class.getName() + ")(type=" +
	// // TransferAgentConnector.class.getName() + "))";
	// // Filter filter = bundleContext.createFilter(FILTER_STRING);
	// // this.connectorFactoryServiceTracker = new ServiceTracker<ServiceConnectorFactory<TransferAgentConnector>,
	// // ServiceConnectorFactory<TransferAgentConnector>>(bundleContext, filter, null);
	// // this.connectorFactoryServiceTracker.open();
	//
	// this.connectorTracker = new ServiceTracker<TransferAgentConnector, TransferAgentConnector>(bundleContext, (Filter) null, null);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// /**
	// *
	// * @param bundleContext
	// */
	// public void stop(BundleContext bundleContext) {
	// System.out.println("TransferAgentAdapter.stop()");
	//
	// if (this.connectorTracker != null) {
	// this.connectorTracker.close();
	// this.connectorTracker = null;
	// }
	// }

	// /**
	// *
	// * @param taConfig
	// * @return
	// */
	// public TransferAgentConnector getTransferAgentConnector(TransferAgentConfig taConfig) {
	// String machineId = taConfig.getMachineId();
	// String transferAgentId = taConfig.getId();
	// String key = machineId + "#" + transferAgentId;
	//
	// TransferAgentConnector connector = this.connectorMap.get(key);
	// if (connector == null) {
	// ServiceConnectorFactory<TransferAgentConnector> connectorFactory = this.connectorFactoryServiceTracker.getService();
	// if (connectorFactory != null) {
	// Map<Object, Object> properties = new HashMap<Object, Object>();
	// properties.put(TransferAgentConfig.class.getName(), taConfig);
	// connector = connectorFactory.create(properties);
	//
	// if (connector != null) {
	// this.connectorMap.put(key, connector);
	// }
	// }
	// }
	//
	// return connector;
	// }

	// /**
	// *
	// * @param taConfig
	// * @return
	// */
	// public TransferAgent getTransferAgent(TransferAgentConfig taConfig) {
	// TransferAgent transferAgent = null;
	// // TransferAgentConnector connector = getTransferAgentConnector(taConfig);
	// TransferAgentConnector connector = null;
	// if (connector != null) {
	// // transferAgent = connector.getService();
	// Map<Object, Object> properties = new HashMap<Object, Object>();
	// properties.put(OrbitConstants.TRANSFER_AGENT_MACHINE_ID, taConfig.getMachineId());
	// properties.put(OrbitConstants.TRANSFER_AGENT_TRANSFER_AGENT_ID, taConfig.getId());
	// properties.put(OrbitConstants.TRANSFER_AGENT_NAME, taConfig.getName());
	// properties.put(OrbitConstants.TRANSFER_AGENT_HOST_URL, taConfig.getHostURL());
	// properties.put(OrbitConstants.TRANSFER_AGENT_CONTEXT_ROOT, taConfig.getContextRoot());
	// properties.put(OrbitConstants.TRANSFER_AGENT_HOME, taConfig.getHome());
	//
	// transferAgent = connector.getService(properties);
	// }
	// return transferAgent;
	// }

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