package other.orbit.component.runtime.extensions;

import java.util.Map;

import org.orbit.platform.sdk.spi.ServiceCommand;
import org.osgi.framework.BundleContext;

public class AppStoreServiceCommand implements ServiceCommand {

	@Override
	public boolean start(BundleContext bundleContext, Map<String, Object> parameters) {
		return false;
	}

	@Override
	public boolean stop(BundleContext bundleContext) {
		return false;
	}

}
