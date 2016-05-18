package org.origin.common.osgi;

import java.util.Dictionary;
import java.util.Hashtable;

import org.origin.common.util.Printer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class PropertiesConfigService {

	protected BundleContext bundleContext;
	protected String pid;
	protected String configId;
	protected Dictionary<String, ?> configs;

	protected ServiceRegistration<PropertiesConfigService> serviceReg;

	/**
	 * 
	 * @param bundleContext
	 */
	public PropertiesConfigService(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public Dictionary<String, ?> getConfigs() {
		return configs;
	}

	public void setConfigs(Dictionary<String, ?> configs) {
		this.configs = configs;
	}

	public void updateConfigs(Dictionary<String, ?> configs) {
		System.out.println("PropertiesConfigService.updateConfigs()");

		this.configs = configs;

		System.out.println("Configs properties:");
		Printer.pl(configs);
		// Example:
		// ------------------------------------------------------------------------
		// service.factoryPid = properties.config.service.factory
		// service.pid = properties.config.service.factory.ed642480-56d8-4f6b-afda-dcf61d5df2d4
		// config.id = {t1}n1
		// p1 = v1
		// p2 = v2
		// ------------------------------------------------------------------------
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
