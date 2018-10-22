package org.orbit.infra.runtime.datacast.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.datatube.DataTubeClientResolver;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.datacast.service.DataTubeConfig;
import org.orbit.infra.runtime.util.DataTubeClientResolverImpl;
import org.orbit.platform.sdk.http.JWTTokenHandler;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.orbit.platform.sdk.util.ExtensionUtil;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.UUIDUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DataCastServiceImpl implements DataCastService, LifecycleAware {

	public static DataTubeConfig[] EMPTY_DATATUBES = new DataTubeConfig[0];
	public static ChannelMetadata[] EMPTY_CHANNELS = new ChannelMetadata[0];

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;

	/**
	 * 
	 * @param initProperties
	 */
	public DataCastServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new ServiceEditPoliciesImpl(DataCastService.class, this);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__ID);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__NAME);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.DATACAST__JDBC_PASSWORD);

		updateProperties(properties);

		initialize();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(DataCastService.class, this, props);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (this.serviceRegistry != null) {
			this.serviceRegistry.unregister();
			this.serviceRegistry = null;
		}
	}

	@Override
	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	/**
	 * 
	 * @param configProps
	 */
	public synchronized void updateProperties(Map<Object, Object> configProps) {
		if (configProps == null) {
			configProps = new HashMap<Object, Object>();
		}

		String indexServiceUrl = (String) configProps.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String globalHostURL = (String) configProps.get(InfraConstants.ORBIT_HOST_URL);
		String id = (String) configProps.get(InfraConstants.DATACAST__ID);
		String name = (String) configProps.get(InfraConstants.DATACAST__NAME);
		String hostURL = (String) configProps.get(InfraConstants.DATACAST__HOST_URL);
		String contextRoot = (String) configProps.get(InfraConstants.DATACAST__CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(InfraConstants.DATACAST__JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(InfraConstants.DATACAST__JDBC_URL);
		String jdbcUsername = (String) configProps.get(InfraConstants.DATACAST__JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(InfraConstants.DATACAST__JDBC_PASSWORD);

		boolean printProps = false;
		if (printProps) {
			System.out.println();
			System.out.println("Config properties:");
			System.out.println("-----------------------------------------------------");
			System.out.println(InfraConstants.ORBIT_INDEX_SERVICE_URL + " = " + indexServiceUrl);
			System.out.println(InfraConstants.ORBIT_HOST_URL + " = " + globalHostURL);
			System.out.println(InfraConstants.DATACAST__ID + " = " + id);
			System.out.println(InfraConstants.DATACAST__NAME + " = " + name);
			System.out.println(InfraConstants.DATACAST__HOST_URL + " = " + hostURL);
			System.out.println(InfraConstants.DATACAST__CONTEXT_ROOT + " = " + contextRoot);
			System.out.println(InfraConstants.DATACAST__JDBC_DRIVER + " = " + jdbcDriver);
			System.out.println(InfraConstants.DATACAST__JDBC_URL + " = " + jdbcURL);
			System.out.println(InfraConstants.DATACAST__JDBC_USERNAME + " = " + jdbcUsername);
			System.out.println(InfraConstants.DATACAST__JDBC_PASSWORD + " = " + jdbcPassword);
			System.out.println("-----------------------------------------------------");
			System.out.println();
		}

		this.properties = configProps;
		this.databaseProperties = getConnectionProperties(this.properties);
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected synchronized Properties getConnectionProperties(Map<Object, Object> props) {
		String driver = (String) this.properties.get(InfraConstants.DATACAST__JDBC_DRIVER);
		String url = (String) this.properties.get(InfraConstants.DATACAST__JDBC_URL);
		String username = (String) this.properties.get(InfraConstants.DATACAST__JDBC_USERNAME);
		String password = (String) this.properties.get(InfraConstants.DATACAST__JDBC_PASSWORD);
		return DatabaseUtil.getProperties(driver, url, username, password);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	/**
	 * Initialize database tables.
	 */
	public void initialize() {
		String database = null;
		try {
			database = DatabaseUtil.getDatabase(this.databaseProperties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assert (database != null) : "database name cannot be retrieved.";

		Connection conn = null;
		try {
			conn = DatabaseUtil.getConnection(this.databaseProperties);

			// if (this.categoryTableHandler != null) {
			// DatabaseUtil.initialize(conn, this.categoryTableHandler);
			// }
			// if (this.appTableHandler != null) {
			// DatabaseUtil.initialize(conn, this.appTableHandler);
			// }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	@Override
	public String getDataCastId() {
		String dataCastId = (String) this.properties.get(InfraConstants.DATACAST__ID);
		return dataCastId;
	}

	@Override
	public String getName() {
		return (String) this.properties.get(InfraConstants.DATACAST__NAME);
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(InfraConstants.DATACAST__HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(InfraConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		return (String) this.properties.get(InfraConstants.DATACAST__CONTEXT_ROOT);
	}

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected DataTubeConfigTableHandler getDataTubeConfigTableHandler(Connection conn) throws SQLException {
		DataTubeConfigTableHandler tableHandler = DataTubeConfigTableHandler.getInstance(conn, getDataCastId());
		return tableHandler;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected ChannelMetadataTableHandler getChannelMetadataTableHandler(Connection conn) throws SQLException {
		ChannelMetadataTableHandler tableHandler = ChannelMetadataTableHandler.getInstance(conn, getDataCastId());
		return tableHandler;
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleException(Exception e) throws ServerException {
		throw new ServerException("500", e);
	}

	@Override
	public DataTubeConfig[] getDataTubeConfigs() throws ServerException {
		DataTubeConfig[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			DataTubeConfigTableHandler tableHandler = getDataTubeConfigTableHandler(conn);

			List<DataTubeConfig> dataTubeConfigs = tableHandler.getList(conn);
			if (dataTubeConfigs != null && !dataTubeConfigs.isEmpty()) {
				result = dataTubeConfigs.toArray(new DataTubeConfig[dataTubeConfigs.size()]);
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_DATATUBES;
		}
		return result;
	}

	@Override
	public DataTubeConfig[] getDataTubeConfigs(boolean isEnabled) throws ServerException {
		DataTubeConfig[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			DataTubeConfigTableHandler tableHandler = getDataTubeConfigTableHandler(conn);

			List<DataTubeConfig> dataTubeConfigs = tableHandler.getList(conn, isEnabled);
			if (dataTubeConfigs != null && !dataTubeConfigs.isEmpty()) {
				result = dataTubeConfigs.toArray(new DataTubeConfig[dataTubeConfigs.size()]);
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_DATATUBES;
		}
		return result;
	}

	@Override
	public DataTubeConfig getDataTubeConfig(String configId) throws ServerException {
		DataTubeConfig result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			DataTubeConfigTableHandler tableHandler = getDataTubeConfigTableHandler(conn);

			result = tableHandler.getByConfigId(conn, configId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public DataTubeConfig createDataTubeConfig(String dataTubeId, String dataTubeName, boolean isEnabled, Map<String, Object> properties) throws ServerException {
		DataTubeConfig result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			DataTubeConfigTableHandler tableHandler = getDataTubeConfigTableHandler(conn);

			result = tableHandler.create(conn, dataTubeId, dataTubeName, isEnabled, properties);

		} catch (SQLException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public boolean updateDataTubeName(String configId, String dataTubeName) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();
			DataTubeConfigTableHandler tableHandler = getDataTubeConfigTableHandler(conn);

			isUpdated = tableHandler.updateName(conn, configId, dataTubeName);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateDataTubeEnabled(String configId, boolean isEnabled) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();
			DataTubeConfigTableHandler tableHandler = getDataTubeConfigTableHandler(conn);

			isUpdated = tableHandler.updateEnabled(conn, configId, isEnabled);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateDataTubeProperties(String configId, Map<String, Object> properties) throws ServerException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();
			DataTubeConfigTableHandler tableHandler = getDataTubeConfigTableHandler(conn);

			isUpdated = tableHandler.updateProperties(conn, configId, properties);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteDataTubeConfig(String configId) throws ServerException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();
			DataTubeConfigTableHandler tableHandler = getDataTubeConfigTableHandler(conn);

			isDeleted = tableHandler.deleteByConfigId(conn, configId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	@Override
	public ChannelMetadata[] getChannelMetadatas() throws ServerException {
		ChannelMetadata[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ChannelMetadataTableHandler tableHandler = getChannelMetadataTableHandler(conn);

			List<ChannelMetadata> channels = tableHandler.getList(conn);
			if (channels != null && !channels.isEmpty()) {
				result = channels.toArray(new ChannelMetadata[channels.size()]);
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_CHANNELS;
		}
		return result;
	}

	@Override
	public ChannelMetadata[] getChannelMetadatas(String dataTubeId) throws ServerException {
		ChannelMetadata[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ChannelMetadataTableHandler tableHandler = getChannelMetadataTableHandler(conn);

			List<ChannelMetadata> channels = tableHandler.getList(conn, dataTubeId);
			if (channels != null && !channels.isEmpty()) {
				result = channels.toArray(new ChannelMetadata[channels.size()]);
			}

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_CHANNELS;
		}
		return result;
	}

	@Override
	public boolean channelMetadataExistsById(String channelId) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ChannelMetadataTableHandler tableHandler = getChannelMetadataTableHandler(conn);

			exists = tableHandler.existsByChannelId(conn, channelId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public boolean channelMetadataExistsByName(String name) throws ServerException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ChannelMetadataTableHandler tableHandler = getChannelMetadataTableHandler(conn);

			exists = tableHandler.existsByChannelName(conn, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public ChannelMetadata getChannelMetadataById(String channelId) throws ServerException {
		ChannelMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ChannelMetadataTableHandler tableHandler = getChannelMetadataTableHandler(conn);

			result = tableHandler.getByChannelId(conn, channelId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public ChannelMetadata getChannelMetadataByName(String name) throws ServerException {
		ChannelMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ChannelMetadataTableHandler tableHandler = getChannelMetadataTableHandler(conn);

			result = tableHandler.getByChannelName(conn, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public ChannelMetadata createChannelMetadata(String dataTubeId, String name, String accessType, String accessCode, String ownerAccountId, List<String> accountIds, Map<String, Object> properties) throws ServerException {
		ChannelMetadata result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ChannelMetadataTableHandler tableHandler = getChannelMetadataTableHandler(conn);

			if (dataTubeId == null) {
				dataTubeId = allocateDataTube(conn, tableHandler);
			}
			if (dataTubeId == null) {
				throw new ServerException("500", "Data tube cannot be allocated.");
			}

			String channelId = generateChannelId();

			if (channelMetadataExistsByName(name)) {

			}

			if (properties == null) {
				properties = new HashMap<String, Object>();
			}
			result = tableHandler.create(conn, dataTubeId, channelId, name, accessType, accessCode, ownerAccountId, accountIds, properties);

		} catch (SQLException e) {
			handleException(e);
		} catch (IOException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	public String generateChannelId() {
		String channelId = UUIDUtil.generateBase64EncodedUUID();
		return channelId;
	}

	/**
	 * 
	 * @param conn
	 * @param tableHandler
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public String allocateDataTube(Connection conn, ChannelMetadataTableHandler tableHandler) throws IOException, SQLException {
		String dataTubeId = null;

		String dataCastServiceToken = getDataCastServiceToken();
		String dataCastId = getDataCastId();
		String indexServiceUrl = (String) getProperties().get(InfraConstants.ORBIT_INDEX_SERVICE_URL);

		long dataTubeWithLeastAccountsSize = Long.MAX_VALUE;

		List<ChannelMetadata> channels = tableHandler.getList(conn);
		for (ChannelMetadata currChannel : channels) {
			String currDataTubeId = currChannel.getDataTubeId();
			long currAccountSize = currChannel.getAccountIds().size();

		}

		DataTubeClientResolver dataTubeClientResolver = new DataTubeClientResolverImpl(indexServiceUrl);
		DataTubeClient[] dataTubeClients = dataTubeClientResolver.resolve(dataCastId, dataCastServiceToken, (Comparator) null);
		for (DataTubeClient currDataTubeClient : dataTubeClients) {
			try {
				if (currDataTubeClient.ping()) {

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return dataTubeId;
	}

	protected String getDataCastServiceToken() {
		String tokenValue = null;
		try {
			JWTTokenHandler tokenHandler = ExtensionUtil.JWT.getTokenHandler(InfraConstants.TOKEN_PROVIDER__ORBIT);
			if (tokenHandler != null) {
				String roles = OrbitRoles.DATACAST_ADMIN;
				int securityLevel = Secured.SecurityLevels.LEVEL_1;
				String classificationLevels = Secured.ClassificationLevels.TOP_SECRET + "," + Secured.ClassificationLevels.SECRET + "," + Secured.ClassificationLevels.CONFIDENTIAL;

				Map<String, String> payload = new LinkedHashMap<String, String>();
				payload.put(JWTTokenHandler.PAYLOAD__ROLES, roles);
				payload.put(JWTTokenHandler.PAYLOAD__SECURITY_LEVEL, String.valueOf(securityLevel));
				payload.put(JWTTokenHandler.PAYLOAD__CLASSIFICATION_LEVELS, classificationLevels);

				tokenValue = tokenHandler.createToken(payload);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tokenValue;
	}

	@Override
	public boolean deleteChannelMetadataById(String channelId) throws ServerException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ChannelMetadataTableHandler tableHandler = getChannelMetadataTableHandler(conn);

			isDeleted = tableHandler.deleteByChannelId(conn, channelId);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteChannelMetadataByName(String name) throws ServerException {

		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ChannelMetadataTableHandler tableHandler = getChannelMetadataTableHandler(conn);

			isDeleted = tableHandler.deleteByChannelName(conn, name);

		} catch (SQLException e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

}
