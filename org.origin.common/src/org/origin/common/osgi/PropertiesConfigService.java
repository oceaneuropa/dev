package org.origin.common.osgi;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.xml.namespace.QName;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class PropertiesConfigService {

	protected BundleContext bundleContext;
	protected QName qname;
	protected ServiceRegistration<PropertiesConfigService> serviceReg;

	/**
	 * 
	 * @param bundleContext
	 * @param qname
	 */
	public PropertiesConfigService(BundleContext bundleContext, QName qname) {
		this.bundleContext = bundleContext;
		this.qname = qname;
	}

	public void start() {
		System.out.println("PropertiesConfigService.start()");

		Dictionary<String, String> configs = new Hashtable<String, String>();
		this.serviceReg = this.bundleContext.registerService(PropertiesConfigService.class, this, configs);
	}

	public void stop() {
		System.out.println("PropertiesConfigService.stop()");

		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

}
