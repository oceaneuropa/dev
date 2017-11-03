package org.orbit.component.cli;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.auth.AuthConnector;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier2.appstore.AppStoreConnector;
import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.orbit.component.cli.tier3.ResourcePropertyHelper;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.DateUtil;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;

public class SystemCommand implements Annotated {

	// Service type constants
	public static final String USER_REGISTRY = "userregistry";
	public static final String AUTH = "auth";
	public static final String CONFIGR_EGISTRY = "configregistry";
	public static final String APP_STORE = "appstore";
	public static final String DOMAIN = "domain";
	public static final String TRANSFER_AGENT = "transferagent";

	// Column names constants
	protected static String[] AUTH_SERVICES_TITLES = new String[] { "index_item_id", "auth.namespace", "auth.name", "auth.host.url", "auth.context_root", "last_heartbeat_time", "heartbeat_expire_time" };
	protected static String[] APPSTORE_SERVICES_TITLES = new String[] { "index_item_id", "appstore.namespace", "appstore.name", "appstore.host.url", "appstore.context_root", "last_heartbeat_time", "heartbeat_expire_time" };
	protected static String[] DOMAIN_SERVICES_TITLES = new String[] { "index_item_id", "domain_mgmt.namespace", "domain_mgmt.name", "domain_mgmt.host.url", "domain_mgmt.context_root", "last_heartbeat_time", "heartbeat_expire_time" };

	@Dependency
	protected AuthConnector authConnector;
	@Dependency
	protected AppStoreConnector appStoreConnector;
	@Dependency
	protected DomainManagementConnector domainMgmtConnector;

	protected BundleContext bundleContext;
	protected String scheme = "orbit";
	protected boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 */
	public SystemCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	protected String getScheme() {
		return this.scheme;
	}

	public void start() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".start()");
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						// show available services
						"lservices", //
		});

		OSGiServiceUtil.register(this.bundleContext, SystemCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stop()");
		}

		OSGiServiceUtil.unregister(SystemCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void connectorSet() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".connectorSet()");
			System.out.println("authConnector: " + authConnector);
			System.out.println("appStoreConnector: " + appStoreConnector);
			System.out.println("domainMgmtConnector: " + domainMgmtConnector);
		}
	}

	@DependencyUnfullfilled
	public void connectorUnset() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".connectorUnset()");
			System.out.println("authConnector: " + authConnector);
			System.out.println("appStoreConnector: " + appStoreConnector);
			System.out.println("domainMgmtConnector: " + domainMgmtConnector);
		}
	}

	// -----------------------------------------------------------------------------------------
	// Service
	// lservices
	// -----------------------------------------------------------------------------------------
	@Descriptor("List services")
	public void lservices(@Descriptor("The service to list") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) throws ClientException {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "lservices", new String[] { "-service", service });
		}

		if (USER_REGISTRY.equalsIgnoreCase(service)) {

		} else if (AUTH.equalsIgnoreCase(service)) {
			// } else if (OAUTH2.equalsIgnoreCase(service)) {
			listAuthServices();

		} else if (CONFIGR_EGISTRY.equalsIgnoreCase(service)) {

		} else if (APP_STORE.equalsIgnoreCase(service)) {
			listAppStoreServices();

		} else if (DOMAIN.equalsIgnoreCase(service)) {
			listDomainServices();

		} else if (TRANSFER_AGENT.equalsIgnoreCase(service)) {

		} else {
			// System.err.println("###### Unsupported service name: " + service);
			listAuthServices();

			listAppStoreServices();

			listDomainServices();
		}
	}

	protected void listAuthServices() throws ClientException {
		List<LoadBalanceResource<Auth>> resources = CommandHelper.INSTANCE.getAuthResources(this.authConnector);

		String[][] rows = new String[resources.size()][AUTH_SERVICES_TITLES.length];
		int rowIndex = 0;
		for (LoadBalanceResource<Auth> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, "auth.namespace");
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, "auth.name");
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, "auth.host.url");
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, "auth.context_root");
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);
			Date heartbeatExpireTime = ResourcePropertyHelper.INSTANCE.getHeartbeatExpireTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
			String heartbeatExpireTimeStr = (heartbeatExpireTime != null) ? DateUtil.toString(heartbeatExpireTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, heartbeatExpireTimeStr };
		}

		PrettyPrinter.prettyPrint(AUTH_SERVICES_TITLES, rows, resources.size());
		System.out.println();
	}

	protected void listAppStoreServices() throws ClientException {
		List<LoadBalanceResource<AppStore>> resources = CommandHelper.INSTANCE.getAppStoreResources(this.appStoreConnector);

		String[][] rows = new String[resources.size()][APPSTORE_SERVICES_TITLES.length];
		int rowIndex = 0;
		for (LoadBalanceResource<AppStore> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, "appstore.namespace");
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, "appstore.name");
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, "appstore.host.url");
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, "appstore.context_root");
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);
			Date heartbeatExpireTime = ResourcePropertyHelper.INSTANCE.getHeartbeatExpireTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
			String heartbeatExpireTimeStr = (heartbeatExpireTime != null) ? DateUtil.toString(heartbeatExpireTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, heartbeatExpireTimeStr };
		}

		PrettyPrinter.prettyPrint(APPSTORE_SERVICES_TITLES, rows, resources.size());
		System.out.println();
	}

	protected void listDomainServices() throws ClientException {
		List<LoadBalanceResource<DomainManagement>> resources = CommandHelper.INSTANCE.getDomainManagementResources(this.domainMgmtConnector);

		String[][] rows = new String[resources.size()][DOMAIN_SERVICES_TITLES.length];
		int rowIndex = 0;
		for (LoadBalanceResource<DomainManagement> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, "domain_mgmt.namespace");
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, "domain_mgmt.name");
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, "domain_mgmt.host.url");
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, "domain_mgmt.context_root");
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);
			Date heartbeatExpireTime = ResourcePropertyHelper.INSTANCE.getHeartbeatExpireTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
			String heartbeatExpireTimeStr = (heartbeatExpireTime != null) ? DateUtil.toString(heartbeatExpireTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, heartbeatExpireTimeStr };
		}

		PrettyPrinter.prettyPrint(DOMAIN_SERVICES_TITLES, rows, resources.size());
		System.out.println();
	}

}

// public static final String OAUTH2 = "oauth2";