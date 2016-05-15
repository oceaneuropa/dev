package org.origin.common.osgi;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.namespace.QName;

import org.origin.common.util.Printer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

public class PropertiesConfigServiceFactory implements ManagedServiceFactory {

	protected BundleContext bundleContext;
	protected ServiceRegistration<ManagedServiceFactory> serviceReg;
	protected ConcurrentMap<QName, PropertiesConfigService> qnameToQNameServiceMap = new ConcurrentHashMap<QName, PropertiesConfigService>();

	/**
	 * 
	 * @param bundleContext
	 */
	public PropertiesConfigServiceFactory(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("QNameServiceFactory.start()");

		Dictionary<String, String> configs = new Hashtable<String, String>();
		configs.put(Constants.SERVICE_PID, "qname.service.admin");
		this.serviceReg = this.bundleContext.registerService(ManagedServiceFactory.class, this, configs);
	}

	public void stop() {
		System.out.println("QNameServiceFactory.stop()");

		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	@Override
	public String getName() {
		return "QName Service Factory";
	}

	@Override
	public void updated(String service_pid, Dictionary<String, ?> configs) throws ConfigurationException {
		System.out.println("QNameServiceFactory.updated() serviceId='" + service_pid + "'");
		Printer.pl(configs);
		// ------------------------------------------------------------------------
		// p1 = v1
		// qname = {t1}n1
		// service.factoryPid = qname.service.admin
		// service.pid = qname.service.admin.ed642480-56d8-4f6b-afda-dcf61d5df2d4
		// ------------------------------------------------------------------------
	}

	@Override
	public void deleted(String pId) {
		System.out.println("QNameServiceFactory.deleted() serviceId='" + pId + "'");
	}

}
