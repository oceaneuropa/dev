package org.orbit.infra.runtime.cli;

import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.infra.runtime.channel.service.ChannelServiceImpl;
import org.orbit.infra.runtime.indexes.service.IndexServiceDatabaseImpl;
import org.origin.common.osgi.OSGiServiceUtil;
import org.osgi.framework.BundleContext;

public class ServicesCommand {

	// service types
	public static final String INDEX_SERVICE = "indexservice";
	public static final String CHANNEL = "channel";

	protected BundleContext bundleContext;

	protected IndexServiceDatabaseImpl indexService;
	protected ChannelServiceImpl channelService;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".start()");
		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbitmgmt");
		props.put("osgi.command.function", new String[] { "startservice", "stopservice" });
		OSGiServiceUtil.register(bundleContext, ServicesCommand.class.getName(), this, props);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".stop()");

		OSGiServiceUtil.unregister(ServicesCommand.class.getName(), this);

		this.bundleContext = null;
	}

	/**
	 * Start a service
	 * 
	 * @param service
	 */
	@Descriptor("Start a service")
	public void startservice(@Descriptor("The service to start") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) {
		System.out.println("starting service: " + service);

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
		System.out.println("stopping service: " + service);

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
