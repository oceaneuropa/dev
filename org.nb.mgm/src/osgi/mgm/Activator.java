package osgi.mgm;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import osgi.mgm.service.MgmService;
import osgi.mgm.service.impl.MgmServiceImpl;
import osgi.mgm.ws.MgmApplication;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static MgmServiceImpl mgm;

	public static BundleContext getContext() {
		return context;
	}

	public static MgmService getMgmService() {
		return Activator.mgm;
	}

	protected MgmApplication mgmApplication;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.logger.debug("Management Activator.start()");

		Activator.context = bundleContext;

		// 1. Start Mgm management service
		Activator.mgm = new MgmServiceImpl(bundleContext);
		Activator.mgm.start();

		// 2. Start MgmApplication web service
		this.mgmApplication = new MgmApplication(bundleContext, "/mgm");
		this.mgmApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		this.logger.debug("Management Activator.stop()");

		// 1. Stop MgmApplication service
		if (this.mgmApplication != null) {
			this.mgmApplication.stop();
			this.mgmApplication = null;
		}

		// 2. Stop Management service
		if (Activator.mgm != null) {
			Activator.mgm.stop();
			Activator.mgm = null;
		}

		Activator.context = null;
	}

}
