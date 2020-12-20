package org.orbit.infra.runtime.subs.impl;

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
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.subs.SubsServerService;
import org.orbit.infra.runtime.util.SubsServiceConfigPropertiesHandler;
import org.orbit.platform.sdk.http.AccessTokenProvider;
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
	protected ServiceEditPolicies wsEditPolicies;
	protected AccessTokenProvider accessTokenProvider;

	protected SubsSourcesTableHandler sourcesTableHandler;
	protected SubsTargetsTableHandler targetsTableHandler;
	protected SubsMappingsTableHandler mappingsTableHandler;

	/**
	 * 
	 * @param initProperties
	 */
	public SubsServerServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new ServiceEditPoliciesImpl(SubsServerService.class, this);
		this.accessTokenProvider = new AccessTokenProvider(InfraConstants.TOKEN_PROVIDER__ORBIT, OrbitRoles.SUBS_SERVER_ADMIN);
	}

	/** AccessTokenAware */
	@Override
	public String getAccessToken() {
		String tokenValue = this.accessTokenProvider.getAccessToken();
		return tokenValue;
	}

	/** LifecycleAware */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		SubsServiceConfigPropertiesHandler.getInstance().addPropertyChangeListener(this);

		updateConnectionProperties();

		Connection conn = null;
		try {
			conn = getConnection();
			this.sourcesTableHandler = new SubsSourcesTableHandler();
			this.targetsTableHandler = new SubsTargetsTableHandler();
			this.mappingsTableHandler = new SubsMappingsTableHandler();

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

	/** ConnectionAware */
	@Override
	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/** WebServiceAware */
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

	/** EditPoliciesAwareService */
	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
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
	public SubsSource getSource(String type, String typeId) throws ServerException {
		SubsSource source = null;
		Connection conn = null;
		try {
			conn = getConnection();
			source = this.sourcesTableHandler.getSource(conn, type, typeId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return source;
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
	public boolean sourceExists(String type, String typeId) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.sourcesTableHandler.sourceExists(conn, type, typeId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public SubsSource createSource(String type, String typeId, String name, Map<String, Object> properties) throws ServerException {
		SubsSource source = null;
		Connection conn = null;
		try {
			conn = getConnection();
			source = this.sourcesTableHandler.createSource(conn, type, typeId, name, properties);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return source;
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
	public boolean updateSourceType(Integer sourceId, String type, String typeId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.sourcesTableHandler.updateType(conn, sourceId, type, typeId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateSourceProperties(Integer sourceId, Map<String, Object> properties) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.sourcesTableHandler.updateProperties(conn, sourceId, properties);

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
			succeed = this.sourcesTableHandler.deleteSource(conn, sourceId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteSource(Integer[] sourceIds) throws ServerException {
		if (sourceIds == null || sourceIds.length == 0) {
			return false;
		}

		int succeedCount = 0;
		Connection conn = null;
		try {
			conn = getConnection();

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
	public SubsTarget getTarget(String type, String typeId) throws ServerException {
		SubsTarget target = null;
		Connection conn = null;
		try {
			conn = getConnection();
			target = this.targetsTableHandler.getTarget(conn, type, typeId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return target;
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
	public boolean targetExists(String type, String typeId) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = this.targetsTableHandler.targetExists(conn, type, typeId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public SubsTarget createTarget(String type, String typeId, String name, String serverId, String serverURL, Map<String, Object> properties) throws ServerException {
		SubsTarget target = null;
		Connection conn = null;
		try {
			conn = getConnection();
			target = this.targetsTableHandler.createTarget(conn, type, typeId, name, serverId, serverURL, properties);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return target;
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
	public boolean updateTargetType(Integer targetId, String type, String typeId) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetsTableHandler.updateType(conn, targetId, type, typeId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateServerURL(Integer targetId, String serverId, String serverURL) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetsTableHandler.updateServerURL(conn, targetId, serverId, serverURL);

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
	public boolean updateTargetProperties(Integer targetId, Map<String, Object> properties) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.targetsTableHandler.updateProperties(conn, targetId, properties);

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
			succeed = this.targetsTableHandler.deleteTarget(conn, targetId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean deleteTarget(Integer[] targetIds) throws ServerException {
		if (targetIds == null || targetIds.length == 0) {
			return false;
		}

		int succeedCount = 0;
		Connection conn = null;
		try {
			conn = getConnection();

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
	public boolean updateClientURL(Integer mappingId, String clientId, String clientURL) throws ServerException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = this.mappingsTableHandler.updateClientURL(conn, mappingId, clientId, clientURL);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean updateClientHeartbeat(Integer mappingId) throws ServerException {
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
	public boolean deleteMapping(Integer[] mappingIds) throws ServerException {
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
