package org.orbit.component.cli;

import java.util.Hashtable;

import javax.ws.rs.core.Response;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.Requests;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.connector.tier3.nodecontrol.NodeControlModelConverter;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.model.Request;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeControlCommandGeneric {

	protected static Logger LOG = LoggerFactory.getLogger(NodeControlCommandGeneric.class);
	protected static String[] NODE_TITLES = new String[] { "Name" };

	protected String scheme = "generic.ta";

	public String getScheme() {
		return this.scheme;
	}

	public void start(final BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function", new String[] { "ping", "echo", "level", "list_nodes" });

		OSGiServiceUtil.register(bundleContext, NodeControlCommandGeneric.class.getName(), this, props);
	}

	public void stop(final BundleContext bundleContext) {
		OSGiServiceUtil.unregister(NodeControlCommandGeneric.class.getName(), this);
	}

	protected NodeControlClient getTransferAgent(String url) {
		NodeControlClient transferAgent = OrbitClients.getInstance().getNodeControl(url);
		if (transferAgent == null) {
			throw new IllegalStateException("TransferAgent is null.");
		}
		return transferAgent;
	}

	@Descriptor("ping")
	public void ping( //
			@Descriptor("TA server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "ping", new String[] { "url", url });
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'--url' parameter is not set.");
			return;
		}

		try {
			NodeControlClient transferAgent = getTransferAgent(url);

			boolean ping = transferAgent.ping();
			System.out.println("ping result = " + ping);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param url
	 * @param message
	 */
	@Descriptor("echo")
	public void echo( //
			@Descriptor("TA server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Message") @Parameter(names = { "-m", "--m" }, absentValue = Parameter.UNSPECIFIED) String message //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "echo", new String[] { "url", url }, new String[] { "m", message });
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'--url' parameter is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(message)) {
			LOG.error("'--m' parameter is not set.");
			return;
		}

		try {
			NodeControlClient transferAgent = getTransferAgent(url);

			String echoMessage = transferAgent.echo(message);
			System.out.println("echo result = " + echoMessage);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Descriptor("level")
	public void level( //
			@Descriptor("TA server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Level 1") @Parameter(names = { "-l1", "-l1" }, absentValue = Parameter.UNSPECIFIED) String level1, //
			@Descriptor("Level 2") @Parameter(names = { "-l2", "-l2" }, absentValue = Parameter.UNSPECIFIED) String level2, //
			@Descriptor("Message 1") @Parameter(names = { "-m1", "--m1" }, absentValue = Parameter.UNSPECIFIED) String message1, //
			@Descriptor("Message 2") @Parameter(names = { "-m2", "--m2" }, absentValue = Parameter.UNSPECIFIED) String message2 //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "level", new String[] { "url", url }, new String[] { "l1", level1 }, new String[] { "l2", level2 }, new String[] { "m1", message1 }, new String[] { "m2", message2 });
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'--url' parameter is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(level1)) {
			LOG.error("'--l1' parameter is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(level2)) {
			LOG.error("'--l2' parameter is not set.");
			return;
		}

		try {
			NodeControlClient transferAgent = getTransferAgent(url);

			String levelResult = transferAgent.level(level1, level2, message1, message2);
			System.out.println("level result = " + levelResult);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * 
	 * @param url
	 *            e.g. http://127.0.0.1:12001/orbit/v1/ta
	 * 
	 *            e.g. http://127.0.0.1:13001/orbit/v1/ta
	 */
	@Descriptor("List nodes")
	public void list_nodes(//
			@Descriptor("url") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "list_nodes", new String[] { "url", url });
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'-url' parameter is not set.");
			return;
		}

		try {
			NodeControlClient transferAgent = getTransferAgent(url);

			Request request = new Request(Requests.GET_NODES);
			Response response = transferAgent.sendRequest(request);

			NodeInfo[] nodeInfos = NodeControlModelConverter.INSTANCE.getNodes(response);
			String[][] rows = new String[nodeInfos.length][NODE_TITLES.length];
			int rowIndex = 0;
			for (NodeInfo nodeInfo : nodeInfos) {
				String name = nodeInfo.getName();
				rows[rowIndex++] = new String[] { name };
			}
			PrettyPrinter.prettyPrint(NODE_TITLES, rows, nodeInfos.length);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
