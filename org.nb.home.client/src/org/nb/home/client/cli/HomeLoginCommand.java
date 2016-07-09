package org.nb.home.client.cli;

import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.home.client.api.HomeFactory;
import org.nb.home.client.api.IHomeControl;
import org.origin.common.osgi.OSGiServiceUtil;
import org.osgi.framework.BundleContext;

public class HomeLoginCommand {

	protected BundleContext bundleContext;
	protected IHomeControl homeControl;

	/**
	 * 
	 * @param bundleContext
	 */
	public HomeLoginCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("HomeLoginCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nb");
		props.put("osgi.command.function", new String[] { "loginhome", "logouthome" });
		OSGiServiceUtil.register(this.bundleContext, HomeLoginCommand.class.getName(), this, props);
	}

	public void stop() {
		System.out.println("HomeLoginCommand.stop()");

		OSGiServiceUtil.unregister(HomeLoginCommand.class.getName(), this);
	}

	/**
	 * Login Home
	 * 
	 * Command: loginhome -url http://127.0.0.1:9090
	 * 
	 * Command: loginhome -url http://127.0.0.1:9090 -u admin -p 123
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	@Descriptor("Login Home")
	public void loginhome(@Descriptor("url") @Parameter(names = { "-url", "--url" }, absentValue = "") String url, //
			@Descriptor("username") @Parameter(names = { "-u", "--username" }, absentValue = "") String username, //
			@Descriptor("password") @Parameter(names = { "-p", "--password" }, absentValue = "") String password //
	) {
		if (this.homeControl != null) {
			System.out.println("Login already.");
			return;
		}

		if (url.isEmpty()) {
			System.out.println("--url parameter is required.");
			return;
		}
		if (username.isEmpty()) {
			System.out.println("Warning: --username parameter is not specified.");
			// return;
		}
		if (password.isEmpty()) {
			System.out.println("Warning: -password parameter is not specified.");
			// return;
		}

		IHomeControl oldHomeControl = this.homeControl;
		IHomeControl newHomeControl = HomeFactory.createHomeControl(url, username, password);

		int ping = 0;
		if (newHomeControl != null) {
			try {
				ping = newHomeControl.ping();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (ping > 0) {
			if (oldHomeControl != null) {
				OSGiServiceUtil.unregister(oldHomeControl);
			}
			OSGiServiceUtil.register(this.bundleContext, IHomeControl.class.getName(), newHomeControl);
			this.homeControl = newHomeControl;

			System.out.println("Login successfully.");
		} else {
			System.out.println("Login failed.");
		}
	}

	/**
	 * Logout
	 * 
	 * Command: logouthome
	 * 
	 */
	@Descriptor("Logout Home")
	public void logouthome() {
		if (this.homeControl == null) {
			System.out.println("Logout already.");
			return;
		}

		OSGiServiceUtil.unregister(IHomeControl.class.getName(), this.homeControl);
		this.homeControl = null;
		System.out.println("Logout successfully.");
	}

}
