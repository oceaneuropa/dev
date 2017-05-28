package org.orbit.component.cli.tier3;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.orbit.component.api.tier3.domain.DomainMgmt;
import org.orbit.component.api.tier3.domain.DomainMgmtConnector;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.Printer;
import org.osgi.framework.BundleContext;

public class DomainMgmtCommand implements Annotated {

	protected BundleContext bundleContext;

	@Dependency
	protected DomainMgmtConnector domainMgmtConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	public DomainMgmtCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("DomainMgmtCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function", new String[] { "ldomainmgmtservices", "lmachines", "ltransferagents" });
		OSGiServiceUtil.register(this.bundleContext, DomainMgmtCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("DomainMgmtCommand.stop()");

		OSGiServiceUtil.unregister(DomainMgmtCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void domainMgmtConnectorSet() {
		System.out.println("DomainMgmtConnector is set.");
	}

	@DependencyUnfullfilled
	public void domainMgmtConnectorUnset() {
		System.out.println("DomainMgmtConnector is unset.");
	}

	protected void checkConnector() throws ClientException {
		if (this.domainMgmtConnector == null) {
			System.out.println("DomainMgmtConnector is not available.");
			throw new ClientException(500, "DomainMgmtConnector is not available.");
		}
	}

	/**
	 * 
	 * @return
	 * @throws ClientException
	 */
	protected List<LoadBalanceResource<DomainMgmt>> getServiceResources() throws ClientException {
		checkConnector();

		LoadBalancer<DomainMgmt> lb = this.domainMgmtConnector.getLoadBalancer();
		if (lb == null) {
			System.out.println("DomainMgmt LoadBalancer is not available.");
			return null;
		}

		List<LoadBalanceResource<DomainMgmt>> resources = lb.getResources();
		if (resources == null) {
			System.out.println("DomainMgmt LoadBalancer's resource is null.");
			return null;
		}

		return resources;
	}

	@Descriptor("List Domain management services")
	public void ldomainmgmtservices() throws ClientException {
		List<LoadBalanceResource<DomainMgmt>> resources = getServiceResources();

		System.out.println("Number of DomainManagement services: " + resources.size());
		for (LoadBalanceResource<DomainMgmt> resource : resources) {
			// String id = resource.getId();
			DomainMgmt domainMgmt = resource.getService();
			String name = domainMgmt.getName();
			String url = domainMgmt.getURL();
			// System.out.println(name + " (id = '" + id + "', url = '" + url + "')");
			System.out.println(name + " (url = '" + url + "')");
			Map<?, ?> properties = resource.getProperties();
			Printer.pl(properties);
			System.out.println("ping: " + domainMgmt.ping());
			System.out.println();
		}
	}

	public void lmachines() throws ClientException {
		DomainMgmt domainMgmt = this.domainMgmtConnector.getService();
		if (domainMgmt == null) {
			System.out.println("DomainMgmt service is null.");
			return;
		}
		
		domainMgmt.getMachineConfigs();
	}

}
