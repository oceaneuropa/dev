package org.orbit.component.runtime.switcher.cli;

import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.origin.common.osgi.OSGiServiceUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelaysCommand {

	protected static Logger LOG = LoggerFactory.getLogger(RelaysCommand.class);

	// switcher types
	public static final String USER_REGISTRY = "user_registry";
	public static final String AUTH = "auth";
	public static final String CONFIGR_EGISTRY = "config_registry";
	public static final String APP_STORE = "app_store";
	public static final String DOMAIN_MANAGEMENT = "domain_management";
	public static final String NODE_MANAGEMENT = "node_management";
	public static final String MISSION_CONTROL = "mission_control";

	protected BundleContext bundleContext;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit_switcher");
		props.put("osgi.command.function", new String[] { "startswitcher", "stopswitcher" });
		OSGiServiceUtil.register(bundleContext, RelaysCommand.class.getName(), this, props);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(RelaysCommand.class.getName(), this);
	}

	protected void checkBundleContext() {
		if (this.bundleContext == null) {
			throw new IllegalStateException("bundleContext is null.");
		}
	}

	/**
	 * Start a switcher
	 * 
	 * @param service
	 */
	@Descriptor("Start a switcher")
	public void startswitcher(@Descriptor("The switcher to start") @Parameter(names = { "-s", "--switcher" }, absentValue = "null") String switcher) {
		LOG.info("starting switcher: " + switcher);
		checkBundleContext();

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

	}

}
