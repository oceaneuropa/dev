package org.orbit.infra.connector.cli;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.annotation.Annotated;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexServiceCommand implements Annotated, CommandActivator {

	public static final String ID = "org.orbit.infra.connector.IndexServiceCommand";

	protected static Logger LOG = LoggerFactory.getLogger(IndexServiceCommand.class);

	protected Map<Object, Object> properties;

	public IndexServiceCommand() {
		this.properties = new HashMap<Object, Object>();
	}

	protected String getScheme() {
		return "orbit";
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(BundleContext bundleContext) {
		Map<Object, Object> configProps = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.INDEX_SERVICE_NAME);
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.INDEX_SERVICE_HOST_URL);
		// PropertyUtil.loadProperty(bundleContext, configProps, InfraConstants.INDEX_SERVICE_CONTEXT_ROOT);
		update(configProps);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function",
				new String[] { //
						"index_service_ping", //
						"index_service_echo" //
				});
		OSGiServiceUtil.register(bundleContext, IndexServiceCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void stop(BundleContext bundleContext) {
		OSGiServiceUtil.unregister(IndexServiceCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	/**
	 * 
	 * @param properties
	 */
	public synchronized void update(Map<Object, Object> properties) {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;
	}

	protected IndexServiceClient getIndexService() throws ClientException {
		String url = getIndexServiceURL();
		LOG.debug("### ### ### ### url = " + url);

		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(url, null);
		if (indexService == null) {
			LOG.error("IndexService is not available.");
			throw new IllegalStateException("IndexService is not available. url = " + url);
		}
		return indexService;
	}

	protected String getIndexServiceURL() {
		String url = (String) this.properties.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		return url;
	}

	/**
	 * <pre>
	 * index_service_ping
	 * </pre>
	 * 
	 * @throws ClientException
	 */
	@Descriptor("index_service_ping")
	public void index_service_ping() throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "index_service_ping");
		try {
			IndexServiceClient indexService = getIndexService();

			boolean result = indexService.ping();
			LOG.info("### ### ### ### result = " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <pre>
	 * index_service_echo -message 'abc'
	 * </pre>
	 * 
	 * @param message
	 * @throws ClientException
	 */
	@Descriptor("index_service_echo")
	public void index_service_echo( //
			@Descriptor("Message") @Parameter(names = { "-message", "--message" }, absentValue = Parameter.UNSPECIFIED) String message //
	) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "index_service_send", new String[] { "message", message });
		try {
			IndexServiceClient indexService = getIndexService();

			String resultMessage = indexService.echo(message);
			LOG.info("### ### ### ### resultMessage = " + resultMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
