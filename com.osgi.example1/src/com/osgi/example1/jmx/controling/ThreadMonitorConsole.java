package com.osgi.example1.jmx.controling;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.osgi.example1.jmx.controlled.ThreadMonitorMBean;

/**
 * Run it with the stop or start as the command line argument
 * 
 */
public class ThreadMonitorConsole {

	public static void main(String[] args) {
		try {
			// Connecting to JMX
			System.out.println("Connect to JMX service.");
			JMXServiceURL jmxURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
			JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxURL, null);
			MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();

			// Construct proxy for the the MBean object
			ObjectName mbeanName = new ObjectName(ThreadMonitorMBean.MBEAN_NAME);
			ThreadMonitorMBean threadMonitor = JMX.newMBeanProxy(connection, mbeanName, ThreadMonitorMBean.class, true);

			System.out.println("Connected to: " + threadMonitor.getName() + ", the app is " + (threadMonitor.isRunning() ? "" : "not ") + "running");

			// parse command line arguments
			if (args[0].equalsIgnoreCase("start")) {
				System.out.println("Invoke \"start\" method");
				threadMonitor.start();

			} else if (args[0].equalsIgnoreCase("stop")) {
				System.out.println("Invoke \"stop\" method");
				threadMonitor.stop();
			}

			// clean up and exit
			jmxConnector.close();
			System.out.println("Done.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
