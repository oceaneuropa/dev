package org.orbit.component.cli.tier3;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.DateUtil;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;

public class DomainManagementCommand implements Annotated {

	// service types
	public static final String USER_REGISTRY = "userregistry";
	public static final String OAUTH2 = "oauth2";
	public static final String CONFIGR_EGISTRY = "configregistry";
	public static final String APP_STORE = "appstore";
	public static final String DOMAIN = "domain";
	public static final String TRANSFER_AGENT = "transferagent";

	protected BundleContext bundleContext;

	@Dependency
	protected DomainManagementConnector domainMgmtConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	public DomainManagementCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("DomainManagementCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function", new String[] { "lservices", "lmachines", "ltransferagents" });
		OSGiServiceUtil.register(this.bundleContext, DomainManagementCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("DomainMgmtCommand.stop()");

		OSGiServiceUtil.unregister(DomainManagementCommand.class.getName(), this);
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
	protected List<LoadBalanceResource<DomainManagement>> getServiceResources() throws ClientException {
		checkConnector();

		LoadBalancer<DomainManagement> lb = this.domainMgmtConnector.getLoadBalancer();
		if (lb == null) {
			System.out.println("DomainManagement LoadBalancer is not available.");
			return null;
		}

		List<LoadBalanceResource<DomainManagement>> resources = lb.getResources();
		if (resources == null) {
			System.out.println("DomainManagement LoadBalancer's resource is null.");
			return null;
		}

		return resources;
	}

	@Descriptor("List services")
	public void lservices(@Descriptor("The service to list") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) throws ClientException {
		// System.out.println("list services: " + service);

		if (USER_REGISTRY.equalsIgnoreCase(service)) {

		} else if (OAUTH2.equalsIgnoreCase(service)) {

		} else if (CONFIGR_EGISTRY.equalsIgnoreCase(service)) {

		} else if (APP_STORE.equalsIgnoreCase(service)) {

		} else if (DOMAIN.equalsIgnoreCase(service)) {
			listDomainServices();

		} else if (TRANSFER_AGENT.equalsIgnoreCase(service)) {

		} else {
			System.err.println("###### Unsupported service name: " + service);
		}
	}

	// protected static String[] DOMAIN_SERVICES_TITLES = new String[] { "IndexItem ID", "Host URL", "Context Root", "Name", "Heartbeat Time" };
	protected static String[] DOMAIN_SERVICES_TITLES = new String[] { "index_item_id", "domain_mgmt.host.url", "domain_mgmt.context_root", "domain_mgmt.name", "last_heartbeat_time", "heartbeat_expired" };

	public void listDomainServices() throws ClientException {
		List<LoadBalanceResource<DomainManagement>> resources = getServiceResources();

		// System.out.println("Number of services: " + resources.size());
		String[][] rows = new String[resources.size()][DOMAIN_SERVICES_TITLES.length];
		int rowIndex = 0;
		for (LoadBalanceResource<DomainManagement> resource : resources) {
			// String id = resource.getId();
			// DomainManagement domainMgmt = resource.getService();
			// String name = domainMgmt.getName();
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getHostUrl(resource);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getContextRoot(resource);
			String name = ResourcePropertyHelper.INSTANCE.getName(resource);
			Date heartBeatTime = ResourcePropertyHelper.INSTANCE.getHeartbeatTime(resource);
			boolean expired = ResourcePropertyHelper.INSTANCE.isHeartBeatExpired(resource);

			// String url = domainMgmt.getURL();
			// System.out.println(name + " (id = '" + id + "', url = '" + url + "')");
			// System.out.println(name + " (url = '" + url + "')");
			// System.out.println(name);
			// Map<?, ?> properties = resource.getProperties();
			// Printer.pl(properties);
			// System.out.println("ping: " + domainMgmt.ping());
			// System.out.println();

			// System.out.println(indexItemId + " - " + hostUrl + " - " + contextRoot + " - " + name + " - " + DateUtil.toString(heartBeatTime,
			// DateUtil.SIMPLE_DATE_FORMAT2));
			rows[rowIndex++] = new String[] { indexItemId.toString(), hostUrl, contextRoot, name, DateUtil.toString(heartBeatTime, DateUtil.SIMPLE_DATE_FORMAT2), expired ? "yes" : "no" };
		}

		PrettyPrinter.prettyPrint(DOMAIN_SERVICES_TITLES, rows, resources.size());
	}

	@Descriptor("List machine configurations")
	public void lmachines() throws ClientException {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			if (domainMgmt == null) {
				System.out.println("DomainManagement service is null.");
				return;
			} else {
				System.out.println(domainMgmt.getName() + " (" + domainMgmt.getURL() + ")");
			}

			MachineConfig[] machineConfigs = domainMgmt.getMachineConfigs();
			System.out.println("Number of machines: " + machineConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
