package org.orbit.platform.cli;

import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.platform.api.PlatformClients;
import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.api.platform.PlatformClient;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ServiceClient;
import org.origin.common.rest.client.ServiceClientCommand;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlatformClientCommand extends ServiceClientCommand {

	protected static Logger LOG = LoggerFactory.getLogger(PlatformClientCommand.class);

	protected String scheme = "platform";
	protected Map<Object, Object> properties;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		LOG.info("start()");

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.ORBIT_GAIA_URL);
		this.properties = properties;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						"ping", "echo", "connect", //
						"start_web_service", "stop_web_service", //
						"start_web_service_relay", "stop_web_service_relay", //
						"execute", //
		});

		OSGiServiceUtil.register(bundleContext, PlatformClientCommand.class.getName(), this, props);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(PlatformClientCommand.class.getName(), this);
	}

	@Override
	public String getScheme() {
		return this.scheme;
	}

	@Override
	public ServiceClient getServiceClient() {
		return getPlatformClient();
	}

	protected PlatformClient getPlatformClient() {
		PlatformClient platformClient = PlatformClients.getInstance().getPlatform(this.properties);
		if (platformClient == null) {
			throw new IllegalStateException("Platform is null.");
		}
		return platformClient;
	}

	@Descriptor("Start service")
	public void start_service( //
			@Descriptor("Extension Type Id") @Parameter(names = { "-extensionTypeId", "--extensionTypeId" }, absentValue = Parameter.UNSPECIFIED) String extensionTypeId, //
			@Descriptor("Service Id") @Parameter(names = { "-serviceId", "--serviceId" }, absentValue = Parameter.UNSPECIFIED) String serviceId //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "start_service", new String[] { "extensionTypeId", extensionTypeId }, new String[] { "serviceId", serviceId });
	}

	@Descriptor("Stop service")
	public void stop_service( //
			@Descriptor("Extension Type Id") @Parameter(names = { "-extensionTypeId", "--extensionTypeId" }, absentValue = Parameter.UNSPECIFIED) String extensionTypeId, //
			@Descriptor("Service Id") @Parameter(names = { "-serviceId", "--serviceId" }, absentValue = Parameter.UNSPECIFIED) String serviceId //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "stop_service", new String[] { "extensionTypeId", extensionTypeId }, new String[] { "serviceId", serviceId });
	}

	@Descriptor("List services")
	public void list_services( //
			@Descriptor("Extension Type Id") @Parameter(names = { "-extensionTypeId", "--extensionTypeId" }, absentValue = Parameter.UNSPECIFIED) String extensionTypeId //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "list_services", new String[] { "extensionTypeId", extensionTypeId });

		PlatformClient platformClient = getPlatformClient();

	}

}
