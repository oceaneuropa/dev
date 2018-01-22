package org.orbit.component.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.IndexConstants;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.account.other.UserRegistryConnector;
import org.orbit.component.api.tier1.auth.Auth;
import org.orbit.component.api.tier1.auth.other.AuthConnector;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.api.tier2.appstore.other.AppStoreConnector;
import org.orbit.component.api.tier3.domain.DomainService;
import org.orbit.component.api.tier3.domain.other.DomainServiceConnector;
import org.orbit.component.cli.util.ResourcePropertyHelper;
import org.orbit.component.cli.util.ServicesCommandHelper;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
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
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServicesCommand implements Annotated {

	protected static Logger LOG = LoggerFactory.getLogger(ServicesCommand.class);

	// Service type constants
	public static final String USER_REGISTRY = "userregistry";
	public static final String AUTH = "auth";
	public static final String CONFIGR_EGISTRY = "configregistry";
	public static final String APP_STORE = "appstore";
	public static final String DOMAIN = "domain";
	public static final String TRANSFER_AGENT = "transferagent";

	// Column names constants
	protected static String[] USERREGISTRY_SERVICES_COLUMNS = new String[] { "index_item_id", "userregistry.namespace", "userregistry.name", "userregistry.host.url", "userregistry.context_root", "last_heartbeat_time", "heartbeat_expire_time" };
	protected static String[] AUTH_SERVICES_COLUMNS = new String[] { "index_item_id", "auth.namespace", "auth.name", "auth.host.url", "auth.context_root", "last_heartbeat_time", "heartbeat_expire_time" };
	protected static String[] APPSTORE_SERVICES_COLUMNS = new String[] { "index_item_id", "appstore.namespace", "appstore.name", "appstore.host.url", "appstore.context_root", "last_heartbeat_time", "heartbeat_expire_time" };
	protected static String[] DOMAIN_SERVICES_COLUMNS = new String[] { "index_item_id", "domain_mgmt.namespace", "domain_mgmt.name", "domain_mgmt.host.url", "domain_mgmt.context_root", "last_heartbeat_time", "heartbeat_expire_time" };
	protected static String[] TRANSFERAGENT_SERVICES_TITLES = new String[] { "index_item_id", "transfer_agent.namespace", "transfer_agent.name", "transfer_agent.host.url", "transfer_agent.context_root", "last_heartbeat_time", "heartbeat_expire_time" };

	protected String scheme = "orbit";
	protected Map<Object, Object> properties;

	@Dependency
	protected UserRegistryConnector userRegistryConnector;
	@Dependency
	protected AuthConnector authConnector;
	@Dependency
	protected AppStoreConnector appStoreConnector;
	@Dependency
	protected DomainServiceConnector domainServiceConnector;

	protected String getScheme() {
		return this.scheme;
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						// show available services
						"lservices", //
		});

		OSGiServiceUtil.register(bundleContext, ServicesCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);

		this.properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_USER_REGISTRY_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_AUTH_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_CONFIG_REGISTRY_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_APP_STORE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_TRANSFER_AGENT_URL);
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_MISSION_CONTROL_URL);
	}

	public void stop(BundleContext bundleContext) {
		OSGiServiceUtil.unregister(ServicesCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	protected IndexService getIndexService() {
		return InfraClients.getInstance().getIndexServiceProxy(this.properties);
	}

	@DependencyFullfilled
	public void connectorSet() {
		LOG.debug("connectorSet()");
		LOG.debug("userRegistryConnector: " + userRegistryConnector);
		LOG.debug("authConnector: " + authConnector);
		LOG.debug("appStoreConnector: " + appStoreConnector);
		LOG.debug("domainMgmtConnector: " + domainServiceConnector);
		// LOG.debug("transferAgentConnector: " + transferAgentConnector);
	}

	@DependencyUnfullfilled
	public void connectorUnset() {
		LOG.debug("connectorUnset()");
		LOG.debug("userRegistryConnector: " + userRegistryConnector);
		LOG.debug("authConnector: " + authConnector);
		LOG.debug("appStoreConnector: " + appStoreConnector);
		LOG.debug("domainMgmtConnector: " + domainServiceConnector);
		// LOG.debug("transferAgentConnector: " + transferAgentConnector);
	}

	// -----------------------------------------------------------------------------------------
	// lservices
	// -----------------------------------------------------------------------------------------
	@Descriptor("List services")
	public void lservices(@Descriptor("The service to list") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) throws ClientException {
		CLIHelper.getInstance().printCommand(getScheme(), "lservices", new String[] { "-service", service });

		if (USER_REGISTRY.equalsIgnoreCase(service)) {
			listUserRegistryServices();

		} else if (AUTH.equalsIgnoreCase(service)) {
			listAuthServices();

		} else if (CONFIGR_EGISTRY.equalsIgnoreCase(service)) {

		} else if (APP_STORE.equalsIgnoreCase(service)) {
			listAppStoreServices();

		} else if (DOMAIN.equalsIgnoreCase(service)) {
			listDomainServices();

		} else if (TRANSFER_AGENT.equalsIgnoreCase(service)) {
			listTransferAgents();

		} else {
			// System.err.println("###### Unsupported service name: " + service);
			listUserRegistryServices();
			listAuthServices();
			listAppStoreServices();
			listDomainServices();
			listTransferAgents();
		}
	}

	protected void listUserRegistryServices() throws ClientException {
		List<LoadBalanceResource<UserRegistry>> resources = ServicesCommandHelper.INSTANCE.getUserRegistryResources(this.userRegistryConnector);

		String[][] rows = new String[resources.size()][USERREGISTRY_SERVICES_COLUMNS.length];
		int rowIndex = 0;
		for (LoadBalanceResource<UserRegistry> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.USER_REGISTRY_NAMESPACE);
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.USER_REGISTRY_NAME);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.USER_REGISTRY_HOST_URL);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.USER_REGISTRY_CONTEXT_ROOT);
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);
			Date heartbeatExpireTime = ResourcePropertyHelper.INSTANCE.getHeartbeatExpireTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
			String heartbeatExpireTimeStr = (heartbeatExpireTime != null) ? DateUtil.toString(heartbeatExpireTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, heartbeatExpireTimeStr };
		}

		PrettyPrinter.prettyPrint(USERREGISTRY_SERVICES_COLUMNS, rows, resources.size());
		System.out.println();
	}

	protected void listAuthServices() throws ClientException {
		// List<LoadBalanceResource<Auth>> resources = ServicesCommandHelper.INSTANCE.getAuthResources(this.authConnector);
		List<LoadBalanceResource<Auth>> resources = new ArrayList<LoadBalanceResource<Auth>>();

		String[][] rows = new String[resources.size()][AUTH_SERVICES_COLUMNS.length];
		int rowIndex = 0;
		for (LoadBalanceResource<Auth> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.AUTH_NAMESPACE);
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.AUTH_NAME);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.AUTH_HOST_URL);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.AUTH_CONTEXT_ROOT);
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);
			Date heartbeatExpireTime = ResourcePropertyHelper.INSTANCE.getHeartbeatExpireTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
			String heartbeatExpireTimeStr = (heartbeatExpireTime != null) ? DateUtil.toString(heartbeatExpireTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, heartbeatExpireTimeStr };
		}

		PrettyPrinter.prettyPrint(AUTH_SERVICES_COLUMNS, rows, resources.size());
		System.out.println();
	}

	protected void listAppStoreServices() throws ClientException {
		List<LoadBalanceResource<AppStore>> resources = ServicesCommandHelper.INSTANCE.getAppStoreResources(this.appStoreConnector);

		String[][] rows = new String[resources.size()][APPSTORE_SERVICES_COLUMNS.length];
		int rowIndex = 0;
		for (LoadBalanceResource<AppStore> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.APPSTORE_NAMESPACE);
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.APPSTORE_NAME);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.APPSTORE_HOST_URL);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.APPSTORE_CONTEXT_ROOT);
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);
			Date heartbeatExpireTime = ResourcePropertyHelper.INSTANCE.getHeartbeatExpireTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
			String heartbeatExpireTimeStr = (heartbeatExpireTime != null) ? DateUtil.toString(heartbeatExpireTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, heartbeatExpireTimeStr };
		}

		PrettyPrinter.prettyPrint(APPSTORE_SERVICES_COLUMNS, rows, resources.size());
		System.out.println();
	}

	protected void listDomainServices() throws ClientException {
		List<LoadBalanceResource<DomainService>> resources = ServicesCommandHelper.INSTANCE.getDomainServiceResources(this.domainServiceConnector);

		String[][] rows = new String[resources.size()][DOMAIN_SERVICES_COLUMNS.length];
		int rowIndex = 0;
		for (LoadBalanceResource<DomainService> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.DOMAIN_SERVICE_NAMESPACE);
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.DOMAIN_SERVICE_NAME);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.DOMAIN_MANAGEMENT_HOST_URL);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, IndexConstants.DOMAIN_MANAGEMENT_CONTEXT_ROOT);
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);
			Date heartbeatExpireTime = ResourcePropertyHelper.INSTANCE.getHeartbeatExpireTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
			String heartbeatExpireTimeStr = (heartbeatExpireTime != null) ? DateUtil.toString(heartbeatExpireTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, heartbeatExpireTimeStr };
		}

		PrettyPrinter.prettyPrint(DOMAIN_SERVICES_COLUMNS, rows, resources.size());
		System.out.println();
	}

	protected void listTransferAgents() throws ClientException {
		try {
			IndexService indexService = getIndexService();

			List<IndexItem> indexItems = indexService.getIndexItems(IndexConstants.TRANSFER_AGENT_INDEXER_ID, IndexConstants.TRANSFER_AGENT_TYPE);

			String[][] rows = new String[indexItems.size()][TRANSFERAGENT_SERVICES_TITLES.length];
			int rowIndex = 0;
			for (IndexItem indexItem : indexItems) {
				Integer indexItemId = indexItem.getIndexItemId();
				Map<String, Object> props = indexItem.getProperties();

				String namespace = (String) props.get(IndexConstants.TRANSFER_AGENT_NAMESPACE);
				String name = (String) props.get(IndexConstants.TRANSFER_AGENT_NAME);
				String hostUrl = (String) props.get(IndexConstants.TRANSFER_AGENT_HOST_URL);
				String contextRoot = (String) props.get(IndexConstants.TRANSFER_AGENT_CONTEXT_ROOT);
				Object lastHeartbeatTime = props.get(IndexConstants.HEARTBEAT_EXPIRE_TIME);
				Object heartbeatExpireTime = props.get(IndexConstants.LAST_HEARTBEAT_TIME);

				String lastHeartbeatTimeStr = lastHeartbeatTime.toString();
				String heartbeatExpireTimeStr = heartbeatExpireTime.toString();

				rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, heartbeatExpireTimeStr };
			}

			PrettyPrinter.prettyPrint(TRANSFERAGENT_SERVICES_TITLES, rows, indexItems.size());
			System.out.println();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

// public static final String OAUTH2 = "oauth2";
// protected void listTransferAgentServices() throws ClientException {
// List<LoadBalanceResource<TransferAgent>> resources = CommandHelper.INSTANCE.getTransferAgentResources(this.transferAgentConnector);
//
// String[][] rows = new String[resources.size()][TRANSFERAGENT_SERVICES_TITLES.length];
// int rowIndex = 0;
// for (LoadBalanceResource<TransferAgent> resource : resources) {
// Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
// String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, "transfer_agent.namespace");
// String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, "transfer_agent.name");
// String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, "transfer_agent.host.url");
// String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, "transfer_agent.context_root");
// Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);
// Date heartbeatExpireTime = ResourcePropertyHelper.INSTANCE.getHeartbeatExpireTime(resource);
//
// String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
// String heartbeatExpireTimeStr = (heartbeatExpireTime != null) ? DateUtil.toString(heartbeatExpireTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";
//
// rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, heartbeatExpireTimeStr };
// }
//
// PrettyPrinter.prettyPrint(TRANSFERAGENT_SERVICES_TITLES, rows, resources.size());
// System.out.println();
// }