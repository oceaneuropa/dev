package org.orbit.infra.connector.cli;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.connector.OrbitConstants;
import org.origin.common.annotation.Annotated;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicesCommand implements Annotated {

	protected static Logger LOG = LoggerFactory.getLogger(ServicesCommand.class);

	// Service type constants
	public static final String CHANNEL = "channel";

	// Column names constants
	protected static String[] CHANNEL_SERVICES_COLUMNS = new String[] { "index_item_id", "channel.namespace", "channel.name", "channel.host.url", "channel.context_root", "last_heartbeat_time", "heartbeat_expire_time" };

	protected IndexService indexService;
	protected String scheme = "infra";

	/**
	 * 
	 * @param indexService
	 */
	public ServicesCommand(IndexService indexService) {
		this.indexService = indexService;
	}

	protected String getScheme() {
		return this.scheme;
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						// show available services
						"lservices", //
		});

		OSGiServiceUtil.register(bundleContext, ServicesCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	public void stop(BundleContext bundleContext) {
		OSGiServiceUtil.unregister(ServicesCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
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
			List<IndexItem> indexItems = this.indexService.getIndexItems(OrbitConstants.CHANNEL_INDEXER_ID, OrbitConstants.CHANNEL_TYPE);

			String[][] rows = new String[indexItems.size()][CHANNEL_SERVICES_COLUMNS.length];
			int rowIndex = 0;
			for (IndexItem indexItem : indexItems) {
				Integer indexItemId = indexItem.getIndexItemId();
				Map<String, Object> props = indexItem.getProperties();

				String namespace = (String) props.get(OrbitConstants.CHANNEL_NAMESPACE);
				String name = (String) props.get(OrbitConstants.CHANNEL_NAME);
				String hostUrl = (String) props.get(OrbitConstants.CHANNEL_HOST_URL);
				String contextRoot = (String) props.get(OrbitConstants.CHANNEL_CONTEXT_ROOT);
				Object lastHeartbeatTime = props.get(OrbitConstants.HEARTBEAT_EXPIRE_TIME);
				Object heartbeatExpireTime = props.get(OrbitConstants.LAST_HEARTBEAT_TIME);

				String lastHeartbeatTimeStr = lastHeartbeatTime.toString();
				String heartbeatExpireTimeStr = heartbeatExpireTime.toString();

				rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, heartbeatExpireTimeStr };
			}

			PrettyPrinter.prettyPrint(CHANNEL_SERVICES_COLUMNS, rows, indexItems.size());
			System.out.println();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
