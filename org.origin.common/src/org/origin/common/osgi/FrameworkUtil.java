package org.origin.common.osgi;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.osgi.framework.startlevel.FrameworkStartLevel;

public class FrameworkUtil {

	/**
	 * 
	 * @param configuration
	 * @return
	 */
	public static Framework createFramework(Map<String, String> configuration) {
		if (configuration == null) {
			configuration = new TreeMap<String, String>();
		}

		try {
			ServiceLoader<FrameworkFactory> loader = ServiceLoader.load(org.osgi.framework.launch.FrameworkFactory.class);
			FrameworkFactory factory = loader.iterator().next();

			// Create framework
			final Framework framework = factory.newFramework(configuration);
			framework.init();

			// pre start configuration
			setInitialBundleStartLevel(framework, configuration);
			addListenerToMonitorErrors(framework);
			Thread shutdownHook = addShutdownHook(framework);

			// start framework
			framework.start();

			// after start configuration
			closeClassLoader(framework);

			// wait for framework to stop, blocks current thread
			try {
				framework.waitForStop(0);
				// set state to stopping
				// setState(AppNodeRuntimeStates.Stopping, "Stopping AppNode OSGi Framework");
				System.out.println("Node is shutdown.");

			} catch (InterruptedException e) {
				e.printStackTrace();
				// framework = null;
				configuration.clear();
				// this.installedBundles.clear();
				System.out.println("Framework.waitForStop() failed, " + e.toString());
			}

			Runtime.getRuntime().removeShutdownHook(shutdownHook);

			// framework = null;
			configuration.clear();

			return framework;

		} catch (BundleException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Set initial start level for bundles
	 * 
	 * @param framework
	 * @param configuration
	 */
	protected static void setInitialBundleStartLevel(Framework framework, Map<String, String> configuration) {
		String defaultBundleStartlevel = configuration.get("osgi.bundles.defaultStartLevel");
		if (defaultBundleStartlevel == null) {
			defaultBundleStartlevel = "5";
		}
		FrameworkStartLevel fwstartlevelservice = framework.adapt(FrameworkStartLevel.class);
		fwstartlevelservice.setInitialBundleStartLevel(Integer.parseInt(defaultBundleStartlevel));
	}

	/**
	 * Add listener to monitor errors
	 * 
	 * @param framework
	 */
	protected static void addListenerToMonitorErrors(Framework framework) {
		framework.getBundleContext().addFrameworkListener(new FrameworkListener() {
			@Override
			public void frameworkEvent(final FrameworkEvent event) {
				System.out.println("Framework event: " + event.getType() + ", source " + event.getSource() + ", bundle " + event.getBundle() + ", throwable " + event.getThrowable());

				if (event.getType() == FrameworkEvent.ERROR) {
					Bundle b = event.getBundle();
					String location = b.getLocation();
					System.out.println(event.getThrowable().toString() + " (at " + location + ")");
				}
			}
		});
	}

	/**
	 * Add a shutdown hook to catch ctrl-c
	 * 
	 * @param framework
	 * @return
	 */
	protected static Thread addShutdownHook(final Framework framework) {
		Thread shutdownHook = new Thread("Node-ShutdownHook") {
			@Override
			public void run() {
				// TODO:
				// When OSGI container is stopped, stop apps gracefully.

				// shutdown framework
				try {
					System.out.println("shutting down node.");
					framework.stop();
				} catch (BundleException e) {
					e.printStackTrace();
				}
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownHook);
		return shutdownHook;
	}

	/**
	 * Performance : Optimization of the number of file handles opened by a single node.
	 * 
	 * @param framework
	 */
	protected static void closeClassLoader(Framework framework) {
		boolean closeClassLoader = Boolean.getBoolean("close.classloader");
		if (closeClassLoader) {
			ClassLoader classLoader = framework.getClass().getClassLoader();
			if (classLoader instanceof URLClassLoader) {
				try {
					((URLClassLoader) classLoader).close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
