package org.nb.drive.rest.server.impl;

import java.io.IOException;
import java.util.Hashtable;

import org.nb.drive.rest.server.DriveService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriveServiceImpl implements DriveService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected BundleContext bundleContext;
	protected ServiceRegistration<?> serviceReg;

	/**
	 * 
	 * @param bundleContext
	 */
	public DriveServiceImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		// load properties
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		set(props, "rest.drive.home.dir");
	}

	/**
	 * 
	 * @param props
	 * @param key
	 */
	protected void set(Hashtable<String, Object> props, String key) {
		String value = this.bundleContext.getProperty(key);
		if (value == null) {
			value = System.getProperty(key);
			if (value == null) {
				return;
			}
		}
		props.put(key, value);
	}

	/**
	 * Start MGM service
	 * 
	 * @throws DriveException
	 */
	public void start() throws IOException {
		// 3. Register as a service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceReg = this.bundleContext.registerService(DriveService.class, this, props);
	}

	/**
	 * Stop MGM service
	 * 
	 * @throws DriveException
	 */
	public void stop() throws IOException {
		// Unregister the service
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

}
