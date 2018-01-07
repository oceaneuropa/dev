package org.orbit.infra.runtime.cli;

import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.channel.service.ChannelServiceImpl;
import org.orbit.infra.runtime.indexes.service.IndexServiceDatabaseImpl;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicesCommand {

	protected static Logger LOG = LoggerFactory.getLogger(ServicesCommand.class);

	// service types
	public static final String INDEX_SERVICE = "index_service";
	public static final String CHANNEL = "channel";

	protected BundleContext bundleContext;
	protected IndexServiceDatabaseImpl indexService;
	protected ChannelServiceImpl channelService;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");
		this.bundleContext = bundleContext;

		// Get the available components
		Map<Object, Object> configProperties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProperties, InfraConstants.COMPONENT_INDEX_SERVICE_AUTOSTART);
		PropertyUtil.loadProperty(bundleContext, configProperties, InfraConstants.COMPONENT_CHANNEL_AUTOSTART);

		boolean autoStartIndexService = configProperties.containsKey(InfraConstants.COMPONENT_INDEX_SERVICE_AUTOSTART) ? true : false;
		boolean autoStartChannelService = configProperties.containsKey(InfraConstants.COMPONENT_CHANNEL_AUTOSTART) ? true : false;
		LOG.info("autoStartIndexService = " + autoStartIndexService);
		LOG.info("autoStartChannelService = " + autoStartChannelService);

		if (autoStartIndexService) {
			startservice(ServicesCommand.INDEX_SERVICE);
		}
		if (autoStartChannelService) {
			startservice(ServicesCommand.CHANNEL);
		}

		Hashtable<String, Object> commandProps = new Hashtable<String, Object>();
		commandProps.put("osgi.command.scope", "orbitmgmt");
		commandProps.put("osgi.command.function", new String[] { "startservice", "stopservice" });
		OSGiServiceUtil.register(bundleContext, ServicesCommand.class.getName(), this, commandProps);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(ServicesCommand.class.getName(), this);

		stopservice(ServicesCommand.INDEX_SERVICE);
		stopservice(ServicesCommand.CHANNEL);

		this.bundleContext = null;
	}

	protected void checkBundleContext() {
		if (this.bundleContext == null) {
			throw new IllegalStateException("bundleContext is null.");
		}
	}

	/**
	 * Start a service
	 * 
	 * @param service
	 */
	@Descriptor("Start a service")
	public void startservice(@Descriptor("The service to start") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) {
		LOG.info("starting service: " + service);
		checkBundleContext();

		if (INDEX_SERVICE.equalsIgnoreCase(service)) {
			startIndexService(bundleContext);

		} else if (CHANNEL.equalsIgnoreCase(service)) {
			startChannelService(this.bundleContext);

		} else {
			System.err.println("###### Unsupported service name: " + service);
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
		checkBundleContext();

		if (INDEX_SERVICE.equalsIgnoreCase(service)) {
			stopIndexService(bundleContext);

		} else if (CHANNEL.equalsIgnoreCase(service)) {
			stopChannelService(this.bundleContext);

		} else {
			System.err.println("###### Unsupported service name: " + service);
		}
	}

	public void startIndexService(BundleContext bundleContext) {
		IndexServiceDatabaseImpl indexService = new IndexServiceDatabaseImpl();
		indexService.start(bundleContext);
		this.indexService = indexService;
	}

	public void stopIndexService(BundleContext bundleContext) {
		if (this.indexService != null) {
			this.indexService.stop(bundleContext);
			this.indexService = null;
		}
	}

	public void startChannelService(BundleContext bundleContext) {
		ChannelServiceImpl channelService = new ChannelServiceImpl();
		channelService.start(bundleContext);
		this.channelService = channelService;
	}

	public void stopChannelService(BundleContext bundleContext) {
		if (this.channelService != null) {
			this.channelService.stop(bundleContext);
			this.channelService = null;
		}
	}

}
