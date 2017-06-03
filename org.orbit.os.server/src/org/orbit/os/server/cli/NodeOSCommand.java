package org.orbit.os.server.cli;

import java.util.Hashtable;
import java.util.Properties;

import org.apache.felix.service.command.Descriptor;
import org.orbit.os.server.Activator;
import org.orbit.os.server.service.impl.NodeOSImpl;
import org.origin.common.osgi.OSGiServiceUtil;
import org.osgi.framework.BundleContext;

public class NodeOSCommand {

	protected BundleContext bundleContext;
	protected NodeOSImpl nodeOSImpl;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".start()");
		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nodeos");
		props.put("osgi.command.function", new String[] { "startnodeos", "stopnodeos" });
		OSGiServiceUtil.register(bundleContext, NodeOSCommand.class.getName(), this, props);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		System.out.println(getClass().getSimpleName() + ".stop()");

		OSGiServiceUtil.unregister(NodeOSCommand.class.getName(), this);

		this.bundleContext = null;
	}

	/**
	 * Start node OS
	 * 
	 */
	@Descriptor("Start node OS")
	public void startnodeos() {
		System.out.println(getClass().getSimpleName() + ".startnodeos()");

		startNodeOS(this.bundleContext);
	}

	/**
	 * Stop node OS
	 * 
	 */
	@Descriptor("Stop node OS")
	public void stopnodeos() {
		System.out.println(getClass().getSimpleName() + ".stopnodeos()");

		stopNodeOS();
	}

	/**
	 * Start Node OS
	 * 
	 * @param bundleContext
	 */
	protected void startNodeOS(BundleContext bundleContext) {
		if (this.nodeOSImpl == null) {
			Properties configIniProps = Activator.getNodeHomeConfigIniProperties();
			this.nodeOSImpl = new NodeOSImpl(bundleContext, configIniProps);
		}
		this.nodeOSImpl.start();
	}

	/**
	 * Stop Node OS
	 * 
	 */
	protected void stopNodeOS() {
		if (nodeOSImpl != null) {
			nodeOSImpl.stop();
			nodeOSImpl = null;
		}
	}

}
