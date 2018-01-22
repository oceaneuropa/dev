package org.orbit.os.cli;

import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.felix.service.command.Descriptor;
import org.orbit.os.api.OSClients;
import org.orbit.os.api.OSConstants;
import org.orbit.os.api.Requests;
import org.orbit.os.api.gaia.GAIA;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ServiceClient;
import org.origin.common.rest.client.ServiceClientCommand;
import org.origin.common.rest.model.Request;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GAIACommand extends ServiceClientCommand {

	protected static Logger LOG = LoggerFactory.getLogger(GAIACommand.class);

	protected static String[] WORLD_TITLES = new String[] { "Name" };

	protected String scheme = "gaia";
	protected Map<Object, Object> properties;

	@Override
	public String getScheme() {
		return this.scheme;
	}

	public void start(final BundleContext bundleContext) {
		LOG.info("start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						"ping", "echo", //
						"list_worlds", "get_world", "world_exist", "create_world", "delete_world", "world_status"//
		});

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, OSConstants.ORBIT_GAIA_URL);
		this.properties = properties;

		OSGiServiceUtil.register(bundleContext, GAIACommand.class.getName(), this, props);
	}

	public void stop(final BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(GAIACommand.class.getName(), this);
	}

	@Override
	public ServiceClient getServiceClient() {
		return getGAIA();
	}

	protected GAIA getGAIA() {
		GAIA gaia = OSClients.getInstance().getGAIA(this.properties);
		if (gaia == null) {
			throw new IllegalStateException("GAIA is null.");
		}
		return gaia;
	}

	// -----------------------------------------------------------------------------------------
	// Missions
	// list_worlds
	// get_world
	// world_exist
	// create_world
	// delete_world
	// world_status
	// -----------------------------------------------------------------------------------------
	@Descriptor("List worlds")
	public void list_worlds() {
		CLIHelper.getInstance().printCommand(getScheme(), "list_worlds");

		try {
			GAIA gaia = getGAIA();

			Request request = new Request(Requests.LIST_WORLDS);
			Response response = gaia.sendRequest(request);

			// NodeInfo[] nodeInfos = TransferAgentConverter.INSTANCE.getNodes(response);
			// String[][] rows = new String[nodeInfos.length][NODE_TITLES.length];
			// int rowIndex = 0;
			// for (NodeInfo nodeInfo : nodeInfos) {
			// String name = nodeInfo.getName();
			// rows[rowIndex++] = new String[] { name };
			// }
			// PrettyPrinter.prettyPrint(NODE_TITLES, rows, nodeInfos.length);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
