package org.orbit.component.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.IndexConstants;
import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.IndexServiceUtil;
import org.orbit.platform.sdk.command.CommandActivator;
import org.orbit.platform.sdk.http.AccessTokenHandler;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.service.AccessTokenProvider;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.DateUtil;
import org.origin.common.util.PrettyPrinter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import other.orbit.component.api.tier1.account.UserRegistryConnectorV1;
import other.orbit.component.api.tier1.auth.AuthConnectorV0;
import other.orbit.component.api.tier2.appstore.AppStoreConnectorV1;
import other.orbit.component.api.tier3.domainmanagement.DomainServiceConnectorV1;

public class ServicesClientCommand implements Annotated, CommandActivator, AccessTokenProvider {

	public static final String ID = "org.orbit.component.cli.ServicesClientCommand";

	protected static Logger LOG = LoggerFactory.getLogger(ServicesClientCommand.class);

	// Service type constants
	public static final String USER_REGISTRY = "userregistry";
	public static final String AUTH = "auth";
	public static final String CONFIGR_EGISTRY = "configregistry";
	public static final String APP_STORE = "appstore";
	public static final String DOMAIN = "domain";
	public static final String NODE_CONTROL = "nodecontrol";

	// Column names constants
	protected static String[] USERREGISTRY_SERVICES_COLUMNS = new String[] { "Index Item Id", "Namespace", "Name", "Host URL", "Context Root", "Heartbeat Time", "" };
	protected static String[] AUTH_SERVICES_COLUMNS = new String[] { "Index Item Id", "Namespace", "Name", "Host URL", "Context Root", "Heartbeat Time", "" };
	protected static String[] APPSTORE_SERVICES_COLUMNS = new String[] { "Index Item Id", "Namespace", "Name", "Host URL", "Context Root", "Heartbeat Time", "" };
	protected static String[] DOMAIN_SERVICES_COLUMNS = new String[] { "Index Item Id", "Namespace", "Name", "Host URL", "Context Root", "Heartbeat Time", "" };
	protected static String[] TRANSFERAGENT_SERVICES_TITLES = new String[] { "Index Item Id", "Name", "Host URL", "Context Root", "Heartbeat Time", "" };

	protected String scheme = "orbit";
	protected Map<Object, Object> properties;

	@Dependency
	protected UserRegistryConnectorV1 userRegistryConnector;
	@Dependency
	protected AuthConnectorV0 authConnector;
	@Dependency
	protected AppStoreConnectorV1 appStoreConnector;
	@Dependency
	protected DomainServiceConnectorV1 domainServiceConnector;

	protected AccessTokenHandler accessTokenHandler;

	public ServicesClientCommand() {
		this.accessTokenHandler = new AccessTokenHandler("orbit", OrbitRoles.DFS_ADMIN);
	}

	protected String getScheme() {
		return this.scheme;
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(COMMAND_SCOPE, getScheme());
		props.put(COMMAND_FUNCTION, new String[] { //
				// show available services
				"old_lservices", //
		});

		OSGiServiceUtil.register(bundleContext, ServicesClientCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);

		this.properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_USER_ACCOUNTS_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_AUTH_URL);
		// PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_REGISTRY_URL);
		// PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_APP_STORE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_NODE_CONTROL_URL);
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_MISSION_CONTROL_URL);
	}

	public void stop(BundleContext bundleContext) {
		OSGiServiceUtil.unregister(ServicesClientCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	// protected IndexServiceClient getIndexService() {
	// return InfraClients.getInstance().getIndexService(this.properties);
	// }
	/** AccessTokenProvider */
	@Override
	public String getAccessToken() {
		String tokenValue = this.accessTokenHandler.getAccessToken();
		return tokenValue;
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
	public void old_lservices(@Descriptor("The service to list") @Parameter(names = { "-s", "--service" }, absentValue = "") String service) throws ClientException {
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

		} else if (NODE_CONTROL.equalsIgnoreCase(service)) {
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
		List<LoadBalanceResource<UserAccountClient>> resources = ServicesCommandHelper.INSTANCE.getUserRegistryResources(this.userRegistryConnector);

		String[][] rows = new String[resources.size()][USERREGISTRY_SERVICES_COLUMNS.length];
		int rowIndex = 0;
		for (LoadBalanceResource<UserAccountClient> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__NAMESPACE);
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__NAME);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__HOST_URL);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__CONTEXT_ROOT);
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, "" };
		}

		PrettyPrinter.prettyPrint(USERREGISTRY_SERVICES_COLUMNS, rows, resources.size());
		System.out.println();
	}

	protected void listAuthServices() throws ClientException {
		// List<LoadBalanceResource<Auth>> resources = ServicesCommandHelper.INSTANCE.getAuthResources(this.authConnector);
		List<LoadBalanceResource<AuthClient>> resources = new ArrayList<LoadBalanceResource<AuthClient>>();

		String[][] rows = new String[resources.size()][AUTH_SERVICES_COLUMNS.length];
		int rowIndex = 0;
		for (LoadBalanceResource<AuthClient> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__NAMESPACE);
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__NAME);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__HOST_URL);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__CONTEXT_ROOT);
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, "" };
		}

		PrettyPrinter.prettyPrint(AUTH_SERVICES_COLUMNS, rows, resources.size());
		System.out.println();
	}

	protected void listAppStoreServices() throws ClientException {
		List<LoadBalanceResource<AppStoreClient>> resources = ServicesCommandHelper.INSTANCE.getAppStoreResources(this.appStoreConnector);

		String[][] rows = new String[resources.size()][APPSTORE_SERVICES_COLUMNS.length];
		int rowIndex = 0;
		for (LoadBalanceResource<AppStoreClient> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__NAMESPACE);
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__NAME);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__HOST_URL);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__CONTEXT_ROOT);
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, "" };
		}

		PrettyPrinter.prettyPrint(APPSTORE_SERVICES_COLUMNS, rows, resources.size());
		System.out.println();
	}

	protected void listDomainServices() throws ClientException {
		List<LoadBalanceResource<DomainManagementClient>> resources = ServicesCommandHelper.INSTANCE.getDomainServiceResources(this.domainServiceConnector);

		String[][] rows = new String[resources.size()][DOMAIN_SERVICES_COLUMNS.length];
		int rowIndex = 0;
		for (LoadBalanceResource<DomainManagementClient> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String namespace = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__NAMESPACE);
			String name = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__NAME);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__HOST_URL);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getProperty(resource, InfraConstants.SERVICE__CONTEXT_ROOT);
			Date lastHeartbeatTime = ResourcePropertyHelper.INSTANCE.getLastHeartbeatTime(resource);

			String lastHeartbeatTimeStr = (lastHeartbeatTime != null) ? DateUtil.toString(lastHeartbeatTime, DateUtil.SIMPLE_DATE_FORMAT2) : "null";

			rows[rowIndex++] = new String[] { indexItemId.toString(), namespace, name, hostUrl, contextRoot, lastHeartbeatTimeStr, "" };
		}

		PrettyPrinter.prettyPrint(DOMAIN_SERVICES_COLUMNS, rows, resources.size());
		System.out.println();
	}

	protected void listTransferAgents() throws ClientException {
		try {
			// IndexServiceClient indexService = getIndexService();
			String accessToken = getAccessToken();
			IndexServiceClient indexService = IndexServiceUtil.getClient(accessToken);

			List<IndexItem> indexItems = indexService.getIndexItems(IndexConstants.NODE_CONTROL_INDEXER_ID, IndexConstants.NODE_CONTROL_TYPE);

			String[][] rows = new String[indexItems.size()][TRANSFERAGENT_SERVICES_TITLES.length];
			int rowIndex = 0;
			for (IndexItem indexItem : indexItems) {
				Integer indexItemId = indexItem.getIndexItemId();
				Map<String, Object> props = indexItem.getProperties();

				// String namespace = (String) props.get(IndexConstants.NODE_SERVICE_NAMESPACE);
				String name = (String) props.get(InfraConstants.SERVICE__NAME);
				String hostUrl = (String) props.get(InfraConstants.SERVICE__HOST_URL);
				String contextRoot = (String) props.get(InfraConstants.SERVICE__CONTEXT_ROOT);
				Object lastHeartbeatTime = props.get(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME);
				// Object heartbeatExpireTime = props.get(IndexConstants.LAST_HEARTBEAT_TIME);

				String lastHeartbeatTimeStr = lastHeartbeatTime.toString();
				// String heartbeatExpireTimeStr = heartbeatExpireTime.toString();

				rows[rowIndex++] = new String[] { indexItemId.toString(), name, hostUrl, contextRoot, lastHeartbeatTimeStr, "" };
			}

			PrettyPrinter.prettyPrint(TRANSFERAGENT_SERVICES_TITLES, rows, indexItems.size());
			System.out.println();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
