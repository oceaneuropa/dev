package org.orbit.component.api.cli.other;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.cli.util.ResourcePropertyHelper;
import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.domain.other.DomainServiceConnector;
import org.orbit.component.model.tier3.domain.dto.MachineConfig;
import org.orbit.component.model.tier3.domain.request.AddMachineConfigRequest;
import org.orbit.component.model.tier3.domain.request.UpdateMachineConfigRequest;
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

public class DomainServiceCommandV1 implements Annotated {

	// Service type constants
	public static final String USER_REGISTRY = "userregistry";
	public static final String OAUTH2 = "oauth2";
	public static final String CONFIGR_EGISTRY = "configregistry";
	public static final String APP_STORE = "appstore";
	public static final String DOMAIN = "domain";
	public static final String TRANSFER_AGENT = "transferagent";

	// Column names constants
	protected static String[] DOMAIN_SERVICES_TITLES = new String[] { "index_item_id", "domain_mgmt.host.url", "domain_mgmt.context_root", "domain_mgmt.name", "last_heartbeat_time", "heartbeat_expired" };
	protected static String[] MACHINE_CONFIG_TITLES = new String[] { "ID", "Name", "IP Address" };

	protected BundleContext bundleContext;

	@Dependency
	protected DomainServiceConnector domainServiceConnector;

	protected boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 */
	public DomainServiceCommandV1(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".start()");
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function",
				new String[] { //
						// show available services
						"lservices", //

						// machine configurations
						"_lmachines", "_addmachine", "_updatemachine", "_removemachine" //
		});

		OSGiServiceUtil.register(this.bundleContext, DomainServiceCommandV1.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stop()");
		}

		OSGiServiceUtil.unregister(DomainServiceCommandV1.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void connectorSet() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".connectorSet() Dependency on DomainMgmtConnector is set.");
		}
	}

	@DependencyUnfullfilled
	public void connectorUnset() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".connectorUnset() Dependency on DomainMgmtConnector is unset.");
		}
	}

	protected List<LoadBalanceResource<DomainManagementClient>> getServiceResources() throws ClientException {
		checkConnector();

		LoadBalancer<DomainManagementClient> lb = this.domainServiceConnector.getLoadBalancer();
		if (lb == null) {
			System.out.println("DomainService LoadBalancer is not available.");
			return null;
		}

		List<LoadBalanceResource<DomainManagementClient>> resources = lb.getResources();
		if (resources == null) {
			System.out.println("DomainService LoadBalancer's resource is null.");
			return null;
		}
		return resources;
	}

	protected DomainManagementClient getDomainService() throws ClientException {
		DomainManagementClient domainMgmt = this.domainServiceConnector.getService();
		checkDomainService(domainMgmt);
		print(domainMgmt);
		return domainMgmt;
	}

	protected void checkConnector() throws ClientException {
		if (this.domainServiceConnector == null) {
			System.out.println("DomainServiceConnector is not available.");
			throw new ClientException(500, "DomainServiceConnector is not available.");
		}
	}

	protected void checkDomainService(DomainManagementClient domainService) throws ClientException {
		if (domainService == null) {
			System.err.println(getClass().getSimpleName() + ".checkDomainService() domainMgmt is not available.");
			throw new ClientException(500, "DomainService is not available.");
		}
	}

	protected void print(DomainManagementClient domainService) {
		if (domainService == null) {
			System.out.println("DomainService is null.");
			return;
		} else {
			// System.out.println(domainService.getName() + " (" + domainService.getURL() + ")");
			System.out.println("(" + domainService.getURL() + ")");
		}
	}

	// -----------------------------------------------------------------------------------------
	// Service
	// lservices
	// -----------------------------------------------------------------------------------------
	@Descriptor("List services")
	public void lservices(@Descriptor("The service to list") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) throws ClientException {
		if (debug) {
			System.out.println("command:");
			System.out.println("\tlservices");
			System.out.println("parameters:");
			System.out.println("\t-service = " + service);
		}

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

	public void listDomainServices() throws ClientException {
		List<LoadBalanceResource<DomainManagementClient>> resources = getServiceResources();

		// System.out.println("Number of services: " + resources.size());
		String[][] rows = new String[resources.size()][DOMAIN_SERVICES_TITLES.length];
		int rowIndex = 0;
		for (LoadBalanceResource<DomainManagementClient> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, "domain_mgmt.name");
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, "domain_mgmt.host.url");
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, "domain_mgmt.context_root");
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);
			Date heartbeatExpireTime = ResourcePropertyHelper.INSTANCE.getHeartbeatExpireTime(resource);

			rows[rowIndex++] = new String[] { indexItemId.toString(), hostUrl, contextRoot, name, DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2), DateUtil.toString(heartbeatExpireTime, DateUtil.SIMPLE_DATE_FORMAT2) };
		}

		PrettyPrinter.prettyPrint(DOMAIN_SERVICES_TITLES, rows, resources.size());
	}

	// -----------------------------------------------------------------------------------------
	// Machine configs (old)
	// _lmachines
	// _addmachine
	// _updatemachine
	// _removemachine
	// -----------------------------------------------------------------------------------------
	@Descriptor("List machine configurations")
	public void _lmachines() {
		try {
			DomainManagementClient domainMgmt = getDomainService();

			MachineConfig[] machineConfigs = domainMgmt.getMachineConfigs();
			// System.out.println("Number of machines: " + machineConfigs.length);
			String[][] rows = new String[machineConfigs.length][MACHINE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (MachineConfig machineConfig : machineConfigs) {
				String machineId = machineConfig.getId();
				String machineName = machineConfig.getName();
				String ipAddress = machineConfig.getIpAddress();

				rows[rowIndex++] = new String[] { machineId, machineName, ipAddress };
			}
			PrettyPrinter.prettyPrint(MACHINE_CONFIG_TITLES, rows, machineConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param machineName
	 * @param ipAddress
	 */
	@Descriptor("Add machine configuration")
	public void _addmachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String machineId, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String machineName, //
			@Descriptor("Machine IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = "null") String ipAddress //
	) {
		try {
			DomainManagementClient domainMgmt = getDomainService();

			AddMachineConfigRequest addMachineRequest = new AddMachineConfigRequest();
			addMachineRequest.setMachineId(machineId);
			addMachineRequest.setName(machineName);
			addMachineRequest.setIpAddress(ipAddress);

			boolean succeed = domainMgmt.addMachineConfig(addMachineRequest);
			if (succeed) {
				System.out.println("Machine is added.");
			} else {
				System.out.println("Machine is not added.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param machineName
	 * @param ipAddress
	 */
	@Descriptor("Update machine configuration")
	public void _updatemachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String machineName, //
			@Descriptor("IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = Parameter.UNSPECIFIED) String ipAddress //
	) {
		try {
			DomainManagementClient domainMgmt = getDomainService();

			UpdateMachineConfigRequest updateMachineRequest = new UpdateMachineConfigRequest();
			updateMachineRequest.setMachineId(machineId);

			if (!Parameter.UNSPECIFIED.equals(machineName)) {
				if ("null".equals(machineName)) {
					machineName = null;
				}
				updateMachineRequest.setName(machineName);
			}

			if (!Parameter.UNSPECIFIED.equals(ipAddress)) {
				if ("null".equals(ipAddress)) {
					ipAddress = null;
				}
				updateMachineRequest.setIpAddress(ipAddress);
			}

			boolean succeed = domainMgmt.updateMachineConfig(updateMachineRequest);
			if (succeed) {
				System.out.println("Machine is updated.");
			} else {
				System.out.println("Machine is not updated.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 */
	@Descriptor("Remmove machine configuration")
	public void _removemachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String machineId //
	) {
		try {
			DomainManagementClient domainMgmt = getDomainService();

			boolean succeed = domainMgmt.removeMachineConfig(machineId);
			if (succeed) {
				System.out.println("Machine is removed.");
			} else {
				System.out.println("Machine is not removed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
