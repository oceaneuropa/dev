package org.nb.mgm.home.service.impl;

import java.io.File;
import java.util.Hashtable;

import org.nb.mgm.home.handler.HomeDirHandler;
import org.nb.mgm.home.service.HomeService;
import org.origin.common.util.SystemPropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class HomeServiceImpl implements HomeService {

	/** cluster home dir */
	public static final String CLUSTER_HOME_DIR = "cluster.home.dir";

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> serviceReg;

	protected File homeDir;

	protected HomeDirHandler homeDirHandler;

	public HomeServiceImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		// load properties
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		SystemPropertyUtil.loadProperty(this.bundleContext, props, CLUSTER_HOME_DIR);

		String homePath = (String) props.get(CLUSTER_HOME_DIR);
		this.homeDir = new File(homePath);

		this.homeDirHandler = new HomeDirHandler();
	}

	protected File getHomeDir() {
		return this.homeDir;
	}

	public void start() {
		// Register this HomeServiceImpl as a HomeService
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceReg = this.bundleContext.registerService(HomeService.class, this, props);
	}

	public void stop() {
		// Unregister this HomeServiceImpl as a HomeService
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	@Override
	public int ping() {
		return 1;
	}

	@Override
	public String getStatus() {
		return null;
	}

	@Override
	public void createSectorFolder(String sectorName) {
		this.homeDirHandler.createSectorFolder(this.homeDir, sectorName);
	}

}
