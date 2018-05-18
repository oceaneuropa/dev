package com.osgi.example1.jmx.controlled;

public interface ThreadMonitorMBean {

	String MBEAN_NAME = "org.orbit:type=ThreadMonitorPro";

	String getName();

	void start();

	void stop();

	boolean isRunning();

}
