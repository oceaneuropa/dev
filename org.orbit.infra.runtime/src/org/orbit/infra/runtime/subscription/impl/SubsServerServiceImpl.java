package org.orbit.infra.runtime.subscription.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.infra.model.subs.SubsMapping;
import org.orbit.infra.model.subs.SubsSource;
import org.orbit.infra.model.subs.SubsTarget;
import org.orbit.infra.model.subs.SubsType;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.subscription.SubsServerService;
import org.orbit.infra.runtime.subscription.util.SubsMappingsTableHandler;
import org.orbit.infra.runtime.subscription.util.SubsSourceTypesTableHandler;
import org.orbit.infra.runtime.subscription.util.SubsSourcesTableHandler;
import org.orbit.infra.runtime.subscription.util.SubsTargetTypesTableHandler;
import org.orbit.infra.runtime.subscription.util.SubsTargetsTableHandler;
import org.orbit.infra.runtime.util.SubsServiceConfigPropertiesHandler;
import org.orbit.platform.sdk.http.AccessTokenHandler;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.event.PropertyChangeEvent;
import org.origin.common.event.PropertyChangeListener;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsServerServiceImpl implements SubsServerService, PropertyChangeListener {

	protected Map<Object, Object> initProperties;
	protected Properties databaseProperties;

	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies editPolicies;
	protected AccessTokenHandler accessTokenHandler;

	protected SubsSourceTypesTableHandler sourceTypesTableHandler;
	protected SubsTargetTypesTableHandler targetTypesTableHandler;
	protected SubsSourcesTableHandler sourcesTableHandler;
	protected SubsTargetsTableHandler targetsTableHandler;
	protected SubsMappingsTableHandler mappingsTableHandler;

	/**
	 * 
	 * @param initProperties
	 */
	public SubsServerServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.editPolicies = new ServiceEditPoliciesImpl(SubsServerService.class, this);
		this.accessTokenHandler = new AccessTokenHandler(InfraConstants.TOKEN_PROVIDER__ORBIT, OrbitRoles.SUBS_SERVER_ADMIN);
	}

	/** AccessTokenProvider */
	@Override
	public String getAccessToken() {
		String tokenValue = this.accessTokenHandler.getAccessToken();
		return tokenValue;
	}

	/** ILifecycle */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		SubsServiceConfigPropertiesHandler.getInstance().addPropertyChangeListener(this);

		updateConnectionProperties();

		Connection conn = null;
		try {
			conn = getConnection();

			this.sourceTypesTableHandler = new SubsSourceTypesTableHandler();
			this.targetTypesTableHandler = new SubsTargetTypesTableHandler();
			this.sourcesTableHandler = new SubsSourcesTableHandler();
			this.targetsTableHandler = new SubsTargetsTableHandler();
			this.mappingsTableHandler = new SubsMappingsTableHandler();

			DatabaseUtil.initialize(conn, this.sourceTypesTableHandler);
			DatabaseUtil.initialize(conn, this.targetTypesTableHandler);
			DatabaseUtil.initialize(conn, this.sourcesTableHandler);
			DatabaseUtil.initialize(conn, this.targetsTableHandler);
			DatabaseUtil.initialize(conn, this.mappingsTableHandler);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(SubsServerService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}

		SubsServiceConfigPropertiesHandler.getInstance().removePropertyChangeListener(this);
	}

	/** PropertyChangeListener */
	@Override
	public void notifyEvent(PropertyChangeEvent event) {
		String eventName = event.getName();
		if (InfraConstants.SUBS_SERVER__JDBC_DRIVER.equals(eventName) //
				|| InfraConstants.SUBS_SERVER__JDBC_URL.equals(eventName) //
				|| InfraConstants.SUBS_SERVER__JDBC_USERNAME.equals(eventName) //
				|| InfraConstants.SUBS_SERVER__JDBC_PASSWORD.equals(eventName)) {
			updateConnectionProperties();
		}
	}

	protected synchronized void updateConnectionProperties() {
		SubsServiceConfigPropertiesHandler configPropertiesHandler = SubsServiceConfigPropertiesHandler.getInstance();
		String driver = configPropertiesHandler.getProperty(InfraConstants.SUBS_SERVER__JDBC_DRIVER, this.initProperties);
		String url = configPropertiesHandler.getProperty(InfraConstants.SUBS_SERVER__JDBC_URL, this.initProperties);
		String username = configPropertiesHandler.getProperty(InfraConstants.SUBS_SERVER__JDBC_USERNAME, this.initProperties);
		String password = configPropertiesHandler.getProperty(InfraConstants.SUBS_SERVER__JDBC_PASSWORD, this.initProperties);

		this.databaseProperties = DatabaseUtil.getProperties(driver, url, username, password);
	}

	/** ConnectionProvider */
	@Override
	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/** IWebService */
	@Override
	public String getName() {
		return SubsServiceConfigPropertiesHandler.getInstance().getProperty(InfraConstants.SUBS_SERVER__NAME, this.initProperties);
	}

	@Override
	public String getHostURL() {
		String hostURL = SubsServiceConfigPropertiesHandler.getInstance().getProperty(InfraConstants.SUBS_SERVER__HOST_URL, this.initProperties);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = SubsServiceConfigPropertiesHandler.getInstance().getProperty(InfraConstants.ORBIT_HOST_URL, this.initProperties);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		return SubsServiceConfigPropertiesHandler.getInstance().getProperty(InfraConstants.SUBS_SERVER__CONTEXT_ROOT, this.initProperties);
	}

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.editPolicies;
	}

	/** SubsServerService */
	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleException(Exception e) throws ServerException {
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	// ------------------------------------------------------
	// Source Types
	// ------------------------------------------------------
	@Override
	public List<SubsType> getSourceTypes() throws ServerException {
		List<SubsType> types = null;
		Connection conn = null;
		try {
			conn = getConnection();
			types = this.sourceTypesTableHandler.getTypes(conn);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (types == null) {
			types = new ArrayList<SubsType>();
		}
		return types;
	}

	@Override
	public SubsType getSourceType(Integer id) throws ServerException {
		SubsType typeObj = null;
		Connection conn = null;
		try {
			conn = getConnection();
			typeObj = this.sourceTypesTableHandler.getType(conn, id);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return typeObj;
	}

	@Override
	public SubsType getSourceType(String type) throws ServerException {
		SubsType typeObj = null;
		Connection conn = null;
		try {
			conn = getConnection();
			typeObj = this.sourceTypesTableHandler.getType(conn, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return typeObj;
	}

	@Override
	public boolean sourceTypeExists(String type) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.sourceTypesTableHandler.typeExists(conn, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public SubsType createSourceType(String type, String name) throws ServerException {
		SubsType typeObj = null;
		Connection conn = null;
		try {
			conn = getConnection();
			typeObj = this.sourceTypesTableHandler.createType(conn, type, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return typeObj;
	}

	@Override
	public boolean updateSourceTypeType(Integer id, String type) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.sourceTypesTableHandler.updateType(conn, id, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateSourceTypeName(Integer id, String name) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.sourceTypesTableHandler.updateName(conn, id, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateSourceTypeName(String type, String name) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.sourceTypesTableHandler.updateName(conn, type, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteSourceType(Integer id) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.sourceTypesTableHandler.deleteType(conn, id);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteSourceType(String type) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.sourceTypesTableHandler.deleteType(conn, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	// ------------------------------------------------------
	// Target Types
	// ------------------------------------------------------
	@Override
	public List<SubsType> getTargetTypes() throws ServerException {
		List<SubsType> types = null;
		Connection conn = null;
		try {
			conn = getConnection();
			types = this.targetTypesTableHandler.getTypes(conn);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (types == null) {
			types = new ArrayList<SubsType>();
		}
		return types;
	}

	@Override
	public SubsType getTargetType(Integer id) throws ServerException {
		SubsType typeObj = null;
		Connection conn = null;
		try {
			conn = getConnection();
			typeObj = this.targetTypesTableHandler.getType(conn, id);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return typeObj;
	}

	@Override
	public SubsType getTargetType(String type) throws ServerException {
		SubsType typeObj = null;
		Connection conn = null;
		try {
			conn = getConnection();
			typeObj = this.targetTypesTableHandler.getType(conn, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return typeObj;
	}

	@Override
	public boolean targetTypeExists(String type) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.targetTypesTableHandler.typeExists(conn, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public SubsType createTargetType(String type, String name) throws ServerException {
		SubsType typeObj = null;
		Connection conn = null;
		try {
			conn = getConnection();
			typeObj = this.targetTypesTableHandler.createType(conn, type, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return typeObj;
	}

	@Override
	public boolean updateTargetTypeType(Integer id, String type) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetTypesTableHandler.updateType(conn, id, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateTargetTypeName(Integer id, String name) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetTypesTableHandler.updateName(conn, id, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateTargetTypeName(String type, String name) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetTypesTableHandler.updateName(conn, type, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteTargetType(Integer id) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetTypesTableHandler.deleteType(conn, id);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteTargetType(String type) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetTypesTableHandler.deleteType(conn, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	// ------------------------------------------------------
	// Source
	// ------------------------------------------------------
	@Override
	public List<SubsSource> getSources() throws ServerException {
		List<SubsSource> sources = null;
		Connection conn = null;
		try {
			conn = getConnection();
			sources = this.sourcesTableHandler.getSources(conn);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (sources == null) {
			sources = new ArrayList<SubsSource>();
		}
		return sources;
	}

	@Override
	public List<SubsSource> getSources(String type) throws ServerException {
		List<SubsSource> sources = null;
		Connection conn = null;
		try {
			conn = getConnection();
			sources = this.sourcesTableHandler.getSources(conn, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (sources == null) {
			sources = new ArrayList<SubsSource>();
		}
		return sources;
	}

	@Override
	public SubsSource getSource(Integer sourceId) throws ServerException {
		SubsSource source = null;
		Connection conn = null;
		try {
			conn = getConnection();
			source = this.sourcesTableHandler.getSource(conn, sourceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return source;
	}

	@Override
	public SubsSource getSource(String type, String instanceId) throws ServerException {
		SubsSource source = null;
		Connection conn = null;
		try {
			conn = getConnection();
			source = this.sourcesTableHandler.getSource(conn, type, instanceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return source;
	}

	@Override
	public boolean sourceExists(String type) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.sourcesTableHandler.sourceExists(conn, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean sourceExists(Integer sourceId) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.sourcesTableHandler.sourceExists(conn, sourceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean sourceExists(String type, String instanceId) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.sourcesTableHandler.sourceExists(conn, type, instanceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public SubsSource createSource(String type, String instanceId, String name, Map<String, Object> properties, boolean createType) throws ServerException {
		SubsSource source = null;
		Connection conn = null;
		try {
			if (createType) {
				if (!sourceTypeExists(type)) {
					createSourceType(type, null);
				}
			}

			conn = getConnection();
			source = this.sourcesTableHandler.createSource(conn, type, instanceId, name, properties);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return source;
	}

	@Override
	public boolean updateSourceType(Integer sourceId, String type, boolean createType) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			if (createType) {
				if (!sourceTypeExists(type)) {
					createSourceType(type, null);
				}
			}

			conn = getConnection();
			succeed = this.sourcesTableHandler.updateType(conn, sourceId, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateSourceInstanceId(Integer sourceId, String instanceId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.sourcesTableHandler.updateInstanceId(conn, sourceId, instanceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateSourceName(Integer sourceId, String name) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.sourcesTableHandler.updateName(conn, sourceId, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateSourceProperties(Integer sourceId, Map<String, Object> properties, boolean clearProperties) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();

			if (clearProperties) {
				succeed = this.sourcesTableHandler.updateProperties(conn, sourceId, properties);

			} else {
				if (properties != null) {
					SubsSource source = this.sourcesTableHandler.getSource(conn, sourceId);
					if (source != null) {
						Map<String, Object> allProperties = source.getProperties();
						allProperties.putAll(properties);
						succeed = this.sourcesTableHandler.updateProperties(conn, sourceId, allProperties);
					}
				}
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteSource(Integer sourceId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();

			// Delete the source's mappings, if any.
			List<Integer> mappingIds = new ArrayList<Integer>();
			List<SubsMapping> mappings = getMappingsOfSource(sourceId);
			for (SubsMapping mapping : mappings) {
				mappingIds.add(mapping.getId());
			}
			if (!mappingIds.isEmpty()) {
				Integer[] mappingIdsArray = mappingIds.toArray(new Integer[mappingIds.size()]);
				deleteMappings(mappingIdsArray);
			}

			// Delete the source
			succeed = this.sourcesTableHandler.deleteSource(conn, sourceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteSources(Integer[] sourceIds) throws ServerException {
		if (sourceIds == null || sourceIds.length == 0) {
			return false;
		}

		int succeedCount = 0;
		Connection conn = null;
		try {
			conn = getConnection();

			// Delete the sources' mappings, if any.
			List<Integer> mappingIds = new ArrayList<Integer>();
			for (Integer currSourceId : sourceIds) {
				List<SubsMapping> mappings = getMappingsOfSource(currSourceId);
				for (SubsMapping mapping : mappings) {
					mappingIds.add(mapping.getId());
				}
			}
			if (!mappingIds.isEmpty()) {
				Integer[] mappingIdsArray = mappingIds.toArray(new Integer[mappingIds.size()]);
				deleteMappings(mappingIdsArray);
			}

			// Delete the sources
			for (Integer currSourceId : sourceIds) {
				boolean currSucceed = this.sourcesTableHandler.deleteSource(conn, currSourceId);
				if (currSucceed) {
					succeedCount++;
				}
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		boolean succeed = false;
		if (succeedCount == sourceIds.length) {
			succeed = true;
		}
		return succeed;
	}

	// ------------------------------------------------------
	// Target
	// ------------------------------------------------------
	@Override
	public List<SubsTarget> getTargets() throws ServerException {
		List<SubsTarget> targets = null;
		Connection conn = null;
		try {
			conn = getConnection();
			targets = this.targetsTableHandler.getTargets(conn);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (targets == null) {
			targets = new ArrayList<SubsTarget>();
		}
		return targets;
	}

	@Override
	public List<SubsTarget> getTargets(String type) throws ServerException {
		List<SubsTarget> targets = null;
		Connection conn = null;
		try {
			conn = getConnection();
			targets = this.targetsTableHandler.getTargets(conn, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (targets == null) {
			targets = new ArrayList<SubsTarget>();
		}
		return targets;
	}

	@Override
	public SubsTarget getTarget(Integer targetId) throws ServerException {
		SubsTarget target = null;
		Connection conn = null;
		try {
			conn = getConnection();
			target = this.targetsTableHandler.getTarget(conn, targetId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return target;
	}

	@Override
	public SubsTarget getTarget(String type, String instanceId) throws ServerException {
		SubsTarget target = null;
		Connection conn = null;
		try {
			conn = getConnection();
			target = this.targetsTableHandler.getTarget(conn, type, instanceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return target;
	}

	@Override
	public boolean targetExists(String type) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.targetsTableHandler.targetExists(conn, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean targetExists(Integer targetId) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.targetsTableHandler.targetExists(conn, targetId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean targetExists(String type, String instanceId) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.targetsTableHandler.targetExists(conn, type, instanceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public SubsTarget createTarget(String type, String instanceId, String name, String serverId, String serverURL, Map<String, Object> properties, boolean createType) throws ServerException {
		SubsTarget target = null;
		Connection conn = null;
		try {
			if (createType) {
				if (!targetTypeExists(type)) {
					createTargetType(type, null);
				}
			}

			conn = getConnection();
			target = this.targetsTableHandler.createTarget(conn, type, instanceId, name, serverId, serverURL, properties);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return target;
	}

	@Override
	public boolean updateTargetType(Integer targetId, String type, boolean createType) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			if (createType) {
				if (!targetTypeExists(type)) {
					createTargetType(type, null);
				}
			}

			conn = getConnection();
			succeed = this.targetsTableHandler.updateType(conn, targetId, type);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateTargetInstanceId(Integer targetId, String instanceId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetsTableHandler.updateInstanceId(conn, targetId, instanceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateTargetName(Integer targetId, String name) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetsTableHandler.updateName(conn, targetId, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateTargetProperties(Integer targetId, Map<String, Object> properties, boolean clearProperties) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();

			if (clearProperties) {
				succeed = this.targetsTableHandler.updateProperties(conn, targetId, properties);

			} else {
				if (properties != null) {
					SubsTarget target = this.targetsTableHandler.getTarget(conn, targetId);
					if (target != null) {
						Map<String, Object> allProperties = target.getProperties();
						allProperties.putAll(properties);
						succeed = this.targetsTableHandler.updateProperties(conn, targetId, allProperties);
					}
				}
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateServerId(Integer targetId, String serverId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetsTableHandler.updateServerId(conn, targetId, serverId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateServerURL(Integer targetId, String serverURL) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetsTableHandler.updateServerURL(conn, targetId, serverURL);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateServerHeartbeat(Integer targetId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetsTableHandler.updateServerHeartbeatTime(conn, targetId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteTarget(Integer targetId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();

			// Delete the target's mappings, if any.
			List<Integer> mappingIds = new ArrayList<Integer>();
			List<SubsMapping> mappings = getMappingsOfTarget(targetId);
			for (SubsMapping mapping : mappings) {
				mappingIds.add(mapping.getId());
			}
			if (!mappingIds.isEmpty()) {
				Integer[] mappingIdsArray = mappingIds.toArray(new Integer[mappingIds.size()]);
				deleteMappings(mappingIdsArray);
			}

			// Delete the target
			succeed = this.targetsTableHandler.deleteTarget(conn, targetId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteTargets(Integer[] targetIds) throws ServerException {
		if (targetIds == null || targetIds.length == 0) {
			return false;
		}

		int succeedCount = 0;
		Connection conn = null;
		try {
			conn = getConnection();

			// Delete the targets' mappings, if any.
			List<Integer> mappingIds = new ArrayList<Integer>();
			for (Integer currTargetId : targetIds) {
				List<SubsMapping> mappings = getMappingsOfTarget(currTargetId);
				for (SubsMapping mapping : mappings) {
					mappingIds.add(mapping.getId());
				}
			}
			if (!mappingIds.isEmpty()) {
				Integer[] mappingIdsArray = mappingIds.toArray(new Integer[mappingIds.size()]);
				deleteMappings(mappingIdsArray);
			}

			// Delete the targets
			for (Integer currTargetId : targetIds) {
				boolean currSucceed = this.targetsTableHandler.deleteTarget(conn, currTargetId);
				if (currSucceed) {
					succeedCount++;
				}
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		boolean succeed = false;
		if (succeedCount == targetIds.length) {
			succeed = true;
		}
		return succeed;
	}

	// ------------------------------------------------------
	// Mapping
	// ------------------------------------------------------
	@Override
	public List<SubsMapping> getMappings() throws ServerException {
		List<SubsMapping> mappings = null;
		Connection conn = null;
		try {
			conn = getConnection();
			mappings = this.mappingsTableHandler.getMappings(conn);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (mappings == null) {
			mappings = new ArrayList<SubsMapping>();
		}
		return mappings;
	}

	@Override
	public List<SubsMapping> getMappingsOfSource(Integer sourceId) throws ServerException {
		List<SubsMapping> mappings = null;
		Connection conn = null;
		try {
			conn = getConnection();
			mappings = this.mappingsTableHandler.getMappingsOfSource(conn, sourceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (mappings == null) {
			mappings = new ArrayList<SubsMapping>();
		}
		return mappings;
	}

	@Override
	public List<SubsMapping> getMappingsOfSource(Integer sourceId, String targetType) throws ServerException {
		List<SubsMapping> mappings = new ArrayList<SubsMapping>();
		Connection conn = null;
		try {
			conn = getConnection();
			List<SubsMapping> mappingsOfSource = this.mappingsTableHandler.getMappingsOfSource(conn, sourceId);
			for (SubsMapping mapping : mappingsOfSource) {
				Integer targetId = mapping.getTargetId();

				boolean matchTargetType = false;
				SubsTarget target = this.targetsTableHandler.getTarget(conn, targetId);
				if (target != null) {
					String currTargetType = target.getType();
					if (currTargetType != null && currTargetType.equals(targetType)) {
						matchTargetType = true;
					}
				}
				if (matchTargetType) {
					mappings.add(mapping);
				}
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mappings;
	}

	@Override
	public List<SubsMapping> getMappingsOfSource(Integer sourceId, String targetType, String targetInstanceId) throws ServerException {
		List<SubsMapping> mappings = new ArrayList<SubsMapping>();
		Connection conn = null;
		try {
			conn = getConnection();
			List<SubsMapping> mappingsOfSource = this.mappingsTableHandler.getMappingsOfSource(conn, sourceId);
			for (SubsMapping mapping : mappingsOfSource) {
				Integer targetId = mapping.getTargetId();

				boolean matchTargetType = false;
				boolean matchTargetInstanceId = false;
				SubsTarget target = this.targetsTableHandler.getTarget(conn, targetId);
				if (target != null) {
					String currTargetType = target.getType();
					String currTargetInstanceId = target.getInstanceId();
					if (currTargetType != null && currTargetType.equals(targetType)) {
						matchTargetType = true;
					}
					if (currTargetInstanceId != null && currTargetInstanceId.equals(targetInstanceId)) {
						matchTargetInstanceId = true;
					}
				}
				if (matchTargetType && matchTargetInstanceId) {
					mappings.add(mapping);
				}
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mappings;
	}

	@Override
	public List<SubsMapping> getMappingsOfTarget(Integer targetId) throws ServerException {
		List<SubsMapping> mappings = null;
		Connection conn = null;
		try {
			conn = getConnection();
			mappings = this.mappingsTableHandler.getMappingsOfTarget(conn, targetId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (mappings == null) {
			mappings = new ArrayList<SubsMapping>();
		}
		return mappings;
	}

	@Override
	public List<SubsMapping> getMappingsOfTarget(Integer targetId, String sourceType) throws ServerException {
		List<SubsMapping> mappings = new ArrayList<SubsMapping>();
		Connection conn = null;
		try {
			conn = getConnection();
			List<SubsMapping> mappingsOfTarget = this.mappingsTableHandler.getMappingsOfTarget(conn, targetId);
			for (SubsMapping mapping : mappingsOfTarget) {
				Integer sourceId = mapping.getSourceId();

				boolean matchSourceType = false;
				SubsSource source = this.sourcesTableHandler.getSource(conn, sourceId);
				if (source != null) {
					String currSourceType = source.getType();
					if (currSourceType != null && currSourceType.equals(sourceType)) {
						matchSourceType = true;
					}
				}
				if (matchSourceType) {
					mappings.add(mapping);
				}
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mappings;
	}

	@Override
	public List<SubsMapping> getMappingsOfTarget(Integer targetId, String sourceType, String sourceInstanceId) throws ServerException {
		List<SubsMapping> mappings = new ArrayList<SubsMapping>();
		Connection conn = null;
		try {
			conn = getConnection();
			List<SubsMapping> mappingsOfTarget = this.mappingsTableHandler.getMappingsOfTarget(conn, targetId);
			for (SubsMapping mapping : mappingsOfTarget) {
				Integer sourceId = mapping.getSourceId();

				boolean matchSourceType = false;
				boolean matchSourceInstanceId = false;
				SubsSource source = this.sourcesTableHandler.getSource(conn, sourceId);
				if (source != null) {
					String currSourceType = source.getType();
					String currSourceInstanceId = source.getInstanceId();
					if (currSourceType != null && currSourceType.equals(sourceType)) {
						matchSourceType = true;
					}
					if (currSourceInstanceId != null && currSourceInstanceId.equals(sourceInstanceId)) {
						matchSourceInstanceId = true;
					}
				}
				if (matchSourceType && matchSourceInstanceId) {
					mappings.add(mapping);
				}
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mappings;
	}

	@Override
	public List<SubsMapping> getMappings(Integer sourceId, Integer targetId) throws ServerException {
		List<SubsMapping> mappings = null;
		Connection conn = null;
		try {
			conn = getConnection();
			mappings = this.mappingsTableHandler.getMappings(conn, sourceId, targetId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (mappings == null) {
			mappings = new ArrayList<SubsMapping>();
		}
		return mappings;
	}

	@Override
	public SubsMapping getMapping(Integer mappingId) throws ServerException {
		SubsMapping mapping = null;
		Connection conn = null;
		try {
			conn = getConnection();
			mapping = this.mappingsTableHandler.getMapping(conn, mappingId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mapping;
	}

	@Override
	public SubsMapping getMappingByClientId(Integer sourceId, Integer targetId, String clientId) throws ServerException {
		SubsMapping mapping = null;
		Connection conn = null;
		try {
			conn = getConnection();
			mapping = this.mappingsTableHandler.getMappingByClientId(conn, sourceId, targetId, clientId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mapping;
	}

	@Override
	public SubsMapping getMappingByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ServerException {
		SubsMapping mapping = null;
		Connection conn = null;
		try {
			conn = getConnection();
			mapping = this.mappingsTableHandler.getMappingByClientURL(conn, sourceId, targetId, clientURL);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mapping;
	}

	@Override
	public boolean mappingExists(Integer mappingId) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.mappingsTableHandler.mappingExists(conn, mappingId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean mappingExistsByClientId(Integer sourceId, Integer targetId, String clientId) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.mappingsTableHandler.mappingExistsByClientId(conn, sourceId, targetId, clientId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean mappingExistsByClientURL(Integer sourceId, Integer targetId, String clientURL) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.mappingsTableHandler.mappingExistsByClientURL(conn, sourceId, targetId, clientURL);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public SubsMapping createMapping(Integer sourceId, Integer targetId, String clientId, String clientURL, Map<String, Object> properties) throws ServerException {
		SubsMapping mapping = null;
		Connection conn = null;
		try {
			conn = getConnection();
			mapping = this.mappingsTableHandler.createMapping(conn, sourceId, targetId, clientId, clientURL, properties);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mapping;
	}

	@Override
	public boolean updateMappingProperties(Integer mappingId, Map<String, Object> properties, boolean clearProperties) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();

			if (clearProperties) {
				succeed = this.mappingsTableHandler.updateProperties(conn, mappingId, properties);

			} else {
				if (properties != null) {
					SubsMapping mapping = this.mappingsTableHandler.getMapping(conn, mappingId);
					if (mapping != null) {
						Map<String, Object> allProperties = mapping.getProperties();
						allProperties.putAll(properties);
						succeed = this.mappingsTableHandler.updateProperties(conn, mappingId, allProperties);
					}
				}
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateMappingClientId(Integer mappingId, String clientId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.mappingsTableHandler.updateClientId(conn, mappingId, clientId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateMappingClientURL(Integer mappingId, String clientURL) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.mappingsTableHandler.updateClientURL(conn, mappingId, clientURL);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateMappingClientHeartbeat(Integer mappingId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.mappingsTableHandler.updateClientHeartbeatTime(conn, mappingId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteMapping(Integer mappingId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.mappingsTableHandler.deleteMapping(conn, mappingId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteMappings(Integer[] mappingIds) throws ServerException {
		if (mappingIds == null || mappingIds.length == 0) {
			return false;
		}

		int succeedCount = 0;
		Connection conn = null;
		try {
			conn = getConnection();

			for (Integer currMappingId : mappingIds) {
				boolean currSucceed = this.mappingsTableHandler.deleteMapping(conn, currMappingId);
				if (currSucceed) {
					succeedCount++;
				}
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		boolean succeed = false;
		if (succeedCount == mappingIds.length) {
			succeed = true;
		}
		return succeed;
	}

}
