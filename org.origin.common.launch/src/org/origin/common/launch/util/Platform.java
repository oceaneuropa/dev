package org.origin.common.launch.util;

import org.origin.common.launch.LaunchActivator;
import org.osgi.framework.BundleContext;

public class Platform {

	public static final String PROP_ARCH = "osgi.arch"; //$NON-NLS-1$
	public static final String PROP_OS = "osgi.os"; //$NON-NLS-1$

	public static BundleContext getBundleContext() {
		return LaunchActivator.getBundleContext();
	}

	public static String getOS() {
		return getBundleContext().getProperty(PROP_OS);
	}

	public static String getOSArch() {
		return getBundleContext().getProperty(PROP_ARCH);
	}

}
