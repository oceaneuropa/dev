package org.nb.home.client.cli;

import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.nb.home.client.api.IHomeControl;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.osgi.framework.BundleContext;

public class HomeControlCommand implements Annotated {

	protected static String[] MACHINE_TITLES = new String[] { "ID", "Name", "IP Address", "Description" };

	protected BundleContext bundleContext;

	@Dependency
	protected IHomeControl homeControl;

	/**
	 * 
	 * @param bundleContext
	 */
	public HomeControlCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("HomeControlCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nb");
		props.put("osgi.command.function", new String[] { "pinghome" });
		OSGiServiceUtil.register(this.bundleContext, HomeControlCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("HomeControlCommand.stop()");

		OSGiServiceUtil.unregister(HomeControlCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void homeControlSet() {
	}

	@DependencyUnfullfilled
	public void homeControlUnset() {
	}

	/**
	 * Ping Home
	 * 
	 * Command: pinghome
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	@Descriptor("Ping Home")
	public void pinghome() {
		if (this.homeControl == null) {
			System.out.println("Please login first.");
			return;
		}

		ClientConfiguration clientConfig = this.homeControl.getAdapter(ClientConfiguration.class);
		String homeHost = clientConfig.getHost();
		int homePort = clientConfig.getPort();
		String homeLabel = homeHost + ":" + homePort;

		for (int i = 0; i < 10; i++) {
			try {
				int ping = this.homeControl.ping();
				String result = ping > 0 ? "running" : "down";
				System.out.println("Home (" + homeLabel + ") is " + result + ".");

			} catch (ClientException e) {
				// e.printStackTrace();
				String result = e.getMessage();
				System.out.println("Exception occurs: " + result);
				break;
			}
		}
	}

}
