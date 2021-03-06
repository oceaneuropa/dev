package org.nb.mgm.client.cli;

import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.client.api.ManagementFactory;
import org.origin.common.osgi.OSGiServiceUtil;
import org.osgi.framework.BundleContext;

public class LoginCommand {

	protected BundleContext bundleContext;
	protected ManagementClient mgmClient;

	/**
	 * 
	 * @param bundleContext
	 */
	public LoginCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("LoginCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nb");
		props.put("osgi.command.function", new String[] { "login", "logout" });
		OSGiServiceUtil.register(this.bundleContext, LoginCommand.class.getName(), this, props);
	}

	public void stop() {
		System.out.println("LoginCommand.stop()");

		OSGiServiceUtil.unregister(LoginCommand.class.getName(), this);
	}

	/**
	 * Login
	 * 
	 * Command: login -url http://127.0.0.1:9090
	 * 
	 * Command: login -url http://127.0.0.1:9090 -u admin -p 123
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	@Descriptor("Login")
	public void login(@Descriptor("url") @Parameter(names = { "-url", "--url" }, absentValue = "") String url, //
			@Descriptor("username") @Parameter(names = { "-u", "--username" }, absentValue = "") String username, //
			@Descriptor("password") @Parameter(names = { "-p", "--password" }, absentValue = "") String password //
	) {
		if (this.mgmClient != null) {
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

		ManagementClient oldManagement = this.mgmClient;
		ManagementClient newManagement = ManagementFactory.createManagement(url, username, password);
		if (newManagement != null) {
			if (oldManagement != null) {
				OSGiServiceUtil.unregister(oldManagement);
			}
			OSGiServiceUtil.register(this.bundleContext, ManagementClient.class.getName(), newManagement);
			this.mgmClient = newManagement;

			System.out.println("Login successfully.");
		} else {
			System.out.println("Login failed.");
		}
	}

	/**
	 * Logout
	 * 
	 * Command: logout
	 * 
	 */
	@Descriptor("logout")
	public void logout() {
		if (this.mgmClient == null) {
			System.out.println("Logout already.");
			return;
		}

		OSGiServiceUtil.unregister(ManagementClient.class.getName(), this.mgmClient);
		this.mgmClient = null;
		System.out.println("Logout successfully.");
	}

}
