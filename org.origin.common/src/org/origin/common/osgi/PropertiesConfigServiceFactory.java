package org.origin.common.osgi;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

public class PropertiesConfigServiceFactory implements ManagedServiceFactory, Annotated {

	/** managed service factory ID */
	public static String SERVICE_PID = "properties.config.service.factory";
	/** property name for the unique id of a set of properties related to the id */
	public static final String CONFIG_ID = "config.id";

	protected BundleContext bundleContext;
	protected ServiceRegistration<ManagedServiceFactory> serviceReg;
	protected ConcurrentMap<String, PropertiesConfigService> pidToPropConfigServiceMap = new ConcurrentHashMap<String, PropertiesConfigService>();

	@Dependency
	protected ConfigurationAdmin configAdmin;

	/**
	 * 
	 * @param bundleContext
	 */
	public PropertiesConfigServiceFactory(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@DependencyFullfilled
	public void start() {
		System.out.println("PropertiesConfigServiceFactory.start()");

		Dictionary<String, String> configs = new Hashtable<String, String>();
		configs.put(Constants.SERVICE_PID, SERVICE_PID);
		this.serviceReg = this.bundleContext.registerService(ManagedServiceFactory.class, this, configs);
	}

	@DependencyUnfullfilled
	public void stop() {
		System.out.println("PropertiesConfigServiceFactory.stop()");

		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	@Override
	public String getName() {
		return "Properties Config Service Factory";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> configs) throws ConfigurationException {
		System.out.println("PropertiesConfigServiceFactory.updated() pid='" + pid + "'");

		PropertiesConfigService propConfigService = pidToPropConfigServiceMap.get(pid);
		if (propConfigService == null) {
			propConfigService = new PropertiesConfigService(this.bundleContext);
			propConfigService.setPid(pid);

			Object configId = configs.get(CONFIG_ID);
			if (configId != null) {
				propConfigService.setConfigId(configId.toString());
			}

			propConfigService.setConfigs(configs);
			propConfigService.start();
			pidToPropConfigServiceMap.put(pid, propConfigService);

		} else {
			propConfigService.updateConfigs(configs);
		}
	}

	@Override
	public void deleted(String pid) {
		System.out.println("PropertiesConfigServiceFactory.deleted() pid='" + pid + "'");

		if (pidToPropConfigServiceMap.containsKey(pid)) {
			PropertiesConfigService propConfigService = pidToPropConfigServiceMap.remove(pid);
			if (propConfigService != null) {
				propConfigService.stop();
			}
		}
	}

}
