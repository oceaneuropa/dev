package org.orbit.infra.runtime.cli;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.runtime.InfraServices;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryServiceImpl;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.indexes.service.IndexServiceImpl;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.util.DateUtil;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfraRuntimeCommand implements CommandActivator {

	public static final String ID = "org.orbit.infra.runtime.InfraRuntimeCommand";

	protected static String[] INDEX_ITEM_COLUMNS = new String[] { "Id", "Type", "Name", "Last Upate Time", "Properties" };

	protected static Logger LOG = LoggerFactory.getLogger(InfraRuntimeCommand.class);

	// service types
	public static final String INDEX_SERVICE = "index_service";
	public static final String EXTENSION_REGISTRY = "extension_registry";
	public static final String CHANNEL = "channel";

	protected BundleContext bundleContext;
	protected IndexServiceImpl indexService;
	protected ExtensionRegistryServiceImpl extensionRegistryService;
	// protected DataTubeServiceImpl channelService;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		LOG.debug("start()");
		this.bundleContext = bundleContext;

		// Get the available components
		// Map<Object, Object> properties = new Hashtable<Object, Object>();
		// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_AUTOSTART);
		// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_AUTOSTART);
		//
		// boolean autoStartIndexService = false;
		// if ("true".equals(properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_AUTOSTART))) {
		// autoStartIndexService = true;
		// }
		// boolean autoStartChannelService = false;
		// if ("true".equals(properties.get(InfraConstants.COMPONENT_CHANNEL_AUTOSTART))) {
		// autoStartChannelService = true;
		// }
		//
		// LOG.info("autoStartIndexService = " + autoStartIndexService);
		// LOG.info("autoStartChannelService = " + autoStartChannelService);
		// if (autoStartIndexService) {
		// startservice(InfraCommand.INDEX_SERVICE);
		// }
		// if (autoStartChannelService) {
		// startservice(InfraCommand.CHANNEL);
		// }

		Hashtable<String, Object> commandProps = new Hashtable<String, Object>();
		commandProps.put(COMMAND_SCOPE, "infra");
		commandProps.put(COMMAND_FUNCTION, new String[] { //
				"lserver_indexitems", //
				"startservice", "stopservice" //
		});
		OSGiServiceUtil.register(bundleContext, InfraRuntimeCommand.class.getName(), this, commandProps);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.debug("stop()");

		OSGiServiceUtil.unregister(InfraRuntimeCommand.class.getName(), this);

		// stopservice(InfraCommand.INDEX_SERVICE);
		// stopservice(InfraCommand.CHANNEL);

		this.bundleContext = null;
	}

	protected void checkBundleContext() {
		if (this.bundleContext == null) {
			throw new IllegalStateException("bundleContext is null.");
		}
	}

	protected IndexService getIndexService() {
		IndexService indexService = InfraServices.getInstance().getIndexService();
		return indexService;
	}

	/**
	 * Start a service
	 * 
	 * @param service
	 */
	@Descriptor("Start a service")
	public void startservice(@Descriptor("The service to start") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) {
		LOG.info("starting service: " + service);
		try {
			checkBundleContext();

			if (INDEX_SERVICE.equalsIgnoreCase(service)) {
				startIndexService(bundleContext);

			} else if (CHANNEL.equalsIgnoreCase(service)) {
				// startChannelService(this.bundleContext);

			} else {
				System.err.println("###### Unsupported service name: " + service);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start a service
	 * 
	 * @param service
	 */
	@Descriptor("Stop a service")
	public void stopservice(@Descriptor("The service to stop") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) {
		LOG.info("stopping service: " + service);
		try {
			checkBundleContext();

			if (INDEX_SERVICE.equalsIgnoreCase(service)) {
				stopIndexService(bundleContext);

			} else if (CHANNEL.equalsIgnoreCase(service)) {
				// stopChannelService(this.bundleContext);

			} else {
				System.err.println("###### Unsupported service name: " + service);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startIndexService(BundleContext bundleContext) throws Exception {
		IndexServiceImpl indexService = new IndexServiceImpl(null);
		indexService.start(bundleContext);
		this.indexService = indexService;
	}

	public void stopIndexService(BundleContext bundleContext) throws Exception {
		if (this.indexService != null) {
			this.indexService.stop(bundleContext);
			this.indexService = null;
		}
	}

	// public void startChannelService(BundleContext bundleContext) {
	// DataTubeServiceImpl channelService = new DataTubeServiceImpl(null);
	// channelService.start(bundleContext);
	// this.channelService = channelService;
	// }
	//
	// public void stopChannelService(BundleContext bundleContext) {
	// if (this.channelService != null) {
	// this.channelService.stop(bundleContext);
	// this.channelService = null;
	// }
	// }

	/**
	 * <pre>
	 * e.g.
	 * lserver_indexitems -providerId component.index_service.indexer
	 * lserver_indexitems -providerId component.channel.indexer
	 * 
	 * lserver_indexitems -providerId component.user_registry.indexer
	 * lserver_indexitems -providerId component.oauth2.indexer
	 * lserver_indexitems -providerId component.auth.indexer
	 * lserver_indexitems -providerId component.config_registry.indexer
	 * lserver_indexitems -providerId component.app_store.indexer
	 * lserver_indexitems -providerId component.domain_management.indexer
	 * lserver_indexitems -providerId component.node_control.indexer
	 * lserver_indexitems -providerId component.mission_control.indexer
	 * 
	 * lserver_indexitems -providerId platform.indexer
	 * lserver_indexitems -providerId command_service.indexer
	 * 
	 * </pre>
	 * 
	 * @param indexProviderId
	 */
	@Descriptor("List index items")
	public void lserver_indexitems( //
			@Descriptor("Index provider id") @Parameter(names = { "-providerId", "--providerId" }, absentValue = "") String indexProviderId //
	) {
		LOG.info("lindexitems()");
		LOG.info("indexProviderId = " + indexProviderId);

		try {
			IndexService indexService = getIndexService();
			if (indexService == null) {
				LOG.debug("IndexService is null.");
				return;
			}
			if (indexProviderId == null || indexProviderId.isEmpty()) {
				return;
			}

			List<IndexItem> indexItems = indexService.getIndexItems(indexProviderId);

			int numRecords = 0;
			for (IndexItem indexItem : indexItems) {
				Map<String, Object> properties = indexItem.getProperties();
				if (properties.isEmpty()) {
					numRecords += 1;
				} else {
					numRecords += properties.size();
				}
			}

			String[][] records = new String[numRecords][INDEX_ITEM_COLUMNS.length];
			int index = 0;
			for (IndexItem indexItem : indexItems) {
				Integer id = indexItem.getIndexItemId();
				String type = indexItem.getType();
				String name = indexItem.getName();
				Date lastUpdateTime = indexItem.getLastUpdateTime();

				Map<String, Object> properties = indexItem.getProperties();
				String lastUpdateTimeStr = lastUpdateTime != null ? DateUtil.toString(lastUpdateTime, DateUtil.SIMPLE_DATE_FORMAT2) : "(n/a)";

				if (properties.isEmpty()) {
					records[index++] = new String[] { String.valueOf(id), type, name, lastUpdateTimeStr, null };
				} else {
					int i = 0;
					for (Iterator<String> itor = properties.keySet().iterator(); itor.hasNext();) {
						String key = itor.next();
						Object value = properties.get(key);
						// String label = key + " = " + value + " (" + value.getClass().getName() + ")";
						String label = key + " = " + value;

						if (value instanceof Long) {
							long longValue = ((Long) value).longValue();
							Date date = DateUtil.toDate(longValue);
							String dateStr = DateUtil.toString(date, DateUtil.SIMPLE_DATE_FORMAT2);
							if (dateStr != null) {
								label += " (" + dateStr + ")";
							}
						}

						if (i == 0) {
							records[index++] = new String[] { String.valueOf(id), type, name, lastUpdateTimeStr, label };
						} else {
							records[index++] = new String[] { "", "", "", "", label };
						}
						i++;
					}
				}
			}
			PrettyPrinter.prettyPrint(INDEX_ITEM_COLUMNS, records);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
