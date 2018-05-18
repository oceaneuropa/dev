package org.orbit.platform.sdk.spi;

import java.util.Map;

import org.osgi.framework.BundleContext;

public interface ServiceCommand {

	public static final String EXTENSION_TYPE_ID = "platform.service.command";

	boolean start(BundleContext bundleContext, Map<String, Object> parameters);

	boolean stop(BundleContext bundleContext);

}
