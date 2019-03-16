package org.orbit.infra.connector.cli;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.annotation.Annotated;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicesClientCommand implements Annotated, CommandActivator {

	protected static Logger LOG = LoggerFactory.getLogger(ServicesClientCommand.class);

	// Service type constants
	public static final String CHANNEL = "channel";

	// Column names constants
	protected static String[] CHANNEL_SERVICES_COLUMNS = new String[] { "index_item_id", "channel.name", "channel.host.url", "channel.context_root", "Heartbeat Time", "" };

	protected String scheme = "infra";
	// protected String indexServiceUrl = null;

	protected String getScheme() {
		return this.scheme;
	}

	@Override
	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function", new String[] { //
				// show available services
				"lservices", //
		});

		OSGiServiceUtil.register(bundleContext, ServicesClientCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);

		// Map<Object, Object> properties = new Hashtable<Object, Object>();
		// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		// this.indexServiceUrl = (String) properties.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
	}

	@Override
	public void stop(BundleContext bundleContext) {
		OSGiServiceUtil.unregister(ServicesClientCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	protected IndexServiceClient getIndexService() {
		// if (this.indexServiceUrl == null) {
		// throw new IllegalStateException("indexServiceUrl is null.");
		// }
		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(null);
		return indexService;
	}

	// -----------------------------------------------------------------------------------------
	// lservices
	// -----------------------------------------------------------------------------------------
	@Descriptor("List services")
	public void lservices(@Descriptor("The service to list") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "lservices", new String[] { "-service", service });

		if (CHANNEL.equalsIgnoreCase(service)) {
			listChannelServices();

		} else {
			// System.err.println("###### Unsupported service name: " + service);
			listChannelServices();
		}
	}

	protected void listChannelServices() throws ClientException {
		try {
			IndexServiceClient indexService = getIndexService();

			List<IndexItem> indexItems = indexService.getIndexItems(InfraConstants.IDX__DATATUBE__INDEXER_ID, InfraConstants.IDX__DATATUBE__TYPE);

			String[][] rows = new String[indexItems.size()][CHANNEL_SERVICES_COLUMNS.length];
			int rowIndex = 0;
			for (IndexItem indexItem : indexItems) {
				Integer indexItemId = indexItem.getIndexItemId();
				Map<String, Object> props = indexItem.getProperties();

				String name = (String) props.get(InfraConstants.SERVICE__NAME);
				String hostUrl = (String) props.get(InfraConstants.SERVICE__HOST_URL);
				String contextRoot = (String) props.get(InfraConstants.SERVICE__CONTEXT_ROOT);
				Object lastHeartbeatTime = props.get(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME);

				String lastHeartbeatTimeStr = lastHeartbeatTime.toString();

				rows[rowIndex++] = new String[] { indexItemId.toString(), name, hostUrl, contextRoot, lastHeartbeatTimeStr, "" };
			}

			PrettyPrinter.prettyPrint(CHANNEL_SERVICES_COLUMNS, rows, indexItems.size());
			System.out.println();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
