package org.orbit.spirit.runtime.earth.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.infra.api.InfraConstants;
import org.orbit.platform.model.program.ProgramException;
import org.orbit.platform.model.program.ProgramManifest;
import org.orbit.platform.runtime.api.PlatformRuntimeAPIActivator;
import org.orbit.platform.runtime.api.platform.Platform;
import org.orbit.platform.runtime.api.programs.Program;
import org.orbit.platform.runtime.api.programs.ProgramHandler;
import org.orbit.platform.runtime.api.programs.ProgramsAndFeatures;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.spirit.model.userprograms.UserProgram;
import org.orbit.spirit.model.userprograms.UserPrograms;
import org.orbit.spirit.runtime.SpiritConstants;
import org.orbit.spirit.runtime.earth.service.EarthService;
import org.orbit.spirit.runtime.earth.service.World;
import org.orbit.spirit.runtime.util.ModelHelper;
import org.orbit.substance.api.SubstanceConstants;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.origin.common.rest.editpolicy.ServiceEditPoliciesImpl;
import org.origin.common.rest.server.ServerException;
import org.origin.common.rest.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.UUIDUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EarthServiceImpl implements EarthService, LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(EarthServiceImpl.class);

	protected Map<Object, Object> initProperties;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected Properties databaseProperties;
	protected ServiceRegistration<?> serviceRegistry;
	protected ServiceEditPolicies wsEditPolicies;

	protected List<World> worlds = new ArrayList<World>();

	/**
	 * 
	 * @param initProperties
	 */
	public EarthServiceImpl(Map<Object, Object> initProperties) {
		this.initProperties = initProperties;
		this.wsEditPolicies = new ServiceEditPoliciesImpl(EarthService.class, this);
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (this.initProperties != null) {
			properties.putAll(this.initProperties);
		}

		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SubstanceConstants.ORBIT_DFS_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__GAIA_ID);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__ID);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__NAME);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__JDBC_DRIVER);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__JDBC_URL);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__JDBC_USERNAME);
		PropertyUtil.loadProperty(bundleContext, properties, SpiritConstants.EARTH__JDBC_PASSWORD);

		updateProperties(properties);

		initialize();

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistry = bundleContext.registerService(EarthService.class, this, props);
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
		String dfsUrl = (String) configProps.get(SubstanceConstants.ORBIT_DFS_URL);
		String globalHostURL = (String) configProps.get(SpiritConstants.ORBIT_HOST_URL);
		String gaiaId = (String) configProps.get(SpiritConstants.EARTH__GAIA_ID);
		String id = (String) configProps.get(SpiritConstants.EARTH__ID);
		String name = (String) configProps.get(SpiritConstants.EARTH__NAME);
		String hostURL = (String) configProps.get(SpiritConstants.EARTH__HOST_URL);
		String contextRoot = (String) configProps.get(SpiritConstants.EARTH__CONTEXT_ROOT);
		String jdbcDriver = (String) configProps.get(SpiritConstants.EARTH__JDBC_DRIVER);
		String jdbcURL = (String) configProps.get(SpiritConstants.EARTH__JDBC_URL);
		String jdbcUsername = (String) configProps.get(SpiritConstants.EARTH__JDBC_USERNAME);
		String jdbcPassword = (String) configProps.get(SpiritConstants.EARTH__JDBC_PASSWORD);

		boolean printProps = false;
		if (printProps) {
			System.out.println();
			System.out.println("Config properties:");
			System.out.println("-----------------------------------------------------");
			System.out.println(InfraConstants.ORBIT_INDEX_SERVICE_URL + " = " + indexServiceUrl);
			System.out.println(SubstanceConstants.ORBIT_DFS_URL + " = " + dfsUrl);
			System.out.println(SpiritConstants.ORBIT_HOST_URL + " = " + globalHostURL);
			System.out.println(SpiritConstants.EARTH__GAIA_ID + " = " + gaiaId);
			System.out.println(SpiritConstants.EARTH__ID + " = " + id);
			System.out.println(SpiritConstants.EARTH__NAME + " = " + name);
			System.out.println(SpiritConstants.EARTH__HOST_URL + " = " + hostURL);
			System.out.println(SpiritConstants.EARTH__CONTEXT_ROOT + " = " + contextRoot);
			System.out.println(SpiritConstants.EARTH__JDBC_DRIVER + " = " + jdbcDriver);
			System.out.println(SpiritConstants.EARTH__JDBC_URL + " = " + jdbcURL);
			System.out.println(SpiritConstants.EARTH__JDBC_USERNAME + " = " + jdbcUsername);
			System.out.println(SpiritConstants.EARTH__JDBC_PASSWORD + " = " + jdbcPassword);
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
		String driver = (String) this.properties.get(SpiritConstants.EARTH__JDBC_DRIVER);
		String url = (String) this.properties.get(SpiritConstants.EARTH__JDBC_URL);
		String username = (String) this.properties.get(SpiritConstants.EARTH__JDBC_USERNAME);
		String password = (String) this.properties.get(SpiritConstants.EARTH__JDBC_PASSWORD);
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
	public String getGaiaId() {
		String gaiaId = (String) this.properties.get(SpiritConstants.EARTH__GAIA_ID);
		return gaiaId;
	}

	@Override
	public String getEarthId() {
		String earthId = (String) this.properties.get(SpiritConstants.EARTH__ID);
		return earthId;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(SpiritConstants.EARTH__NAME);
		return name;
	}

	@Override
	public String getHostURL() {
		String hostURL = (String) this.properties.get(SpiritConstants.EARTH__HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = (String) this.properties.get(SpiritConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.properties.get(SpiritConstants.EARTH__CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	protected void checkName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("name is null.");
		}
		if (name.isEmpty()) {
			throw new IllegalArgumentException("name is empty.");
		}
	}

	@Override
	public synchronized World[] getWorlds() throws ServerException {
		return this.worlds.toArray(new World[this.worlds.size()]);
	}

	@Override
	public synchronized boolean worldExists(String name) throws ServerException {
		checkName(name);
		for (Iterator<World> itor = this.worlds.iterator(); itor.hasNext();) {
			World currWorld = itor.next();
			if (name.equals(currWorld.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized World getWorld(String name) throws ServerException {
		checkName(name);
		World world = null;
		for (Iterator<World> itor = this.worlds.iterator(); itor.hasNext();) {
			World currWorld = itor.next();
			if (name.equals(currWorld.getName())) {
				world = currWorld;
				break;
			}
		}
		return world;
	}

	@Override
	public synchronized World createWorld(String name) throws ServerException {
		checkName(name);
		World world = new WorldImpl(this);
		String id = UUIDUtil.generateBase64EncodedUUID();
		world.setId(id);
		world.setName(name);
		return null;
	}

	@Override
	public synchronized boolean deleteWorld(String name) throws ServerException {
		checkName(name);

		for (Iterator<World> itor = this.worlds.iterator(); itor.hasNext();) {
			World currWorld = itor.next();
			if (name.equals(currWorld.getName())) {
				currWorld.dispose();
				itor.remove();
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized boolean join(World world, String accessToken) throws ServerException {
		LOG.debug("join()");

		String accountId = OrbitTokenUtil.INSTANCE.getAccountId(accessToken, PlatformConstants.TOKEN_PROVIDER__ORBIT);
		if (accountId == null) {
			throw new ServerException("500", "accountId is not retrieved from access token.");
		}
		if (world.getAccountIds().contains(accountId)) {
			return false;
		}

		ProgramsAndFeatures programsAndFeatures = null;
		Platform platform = PlatformRuntimeAPIActivator.getInstance().getPlatform();
		if (platform != null) {
			programsAndFeatures = platform.getProgramsAndFeatures();
		}
		if (programsAndFeatures == null) {
			throw new ServerException("500", "IProgramsAndFeatures is null.");
		}

		// 1. User programs provisioning
		// (1) Get the programs installed by the user.
		UserPrograms userPrograms = null;
		try {
			userPrograms = ModelHelper.INSTANCE.getUserPrograms(accessToken);
		} catch (IOException e) {
			throw new ServerException("500", e.getMessage(), e);
		}
		if (userPrograms == null) {
			throw new ServerException("500", "UserPrograms is null.");
		}

		// (2) If a program is already installed, make sure it is started.
		// If a program is not installed, collect it.
		List<UserProgram> userProgramsToInstall = new ArrayList<UserProgram>();
		for (UserProgram currUserProgram : userPrograms.getChildren()) {
			String programId = currUserProgram.getId();
			String programVersion = currUserProgram.getVersion();
			try {
				ProgramHandler programHandler = programsAndFeatures.getProgramHandler(programId, programVersion);
				if (programHandler != null) {
					if (!programHandler.getRuntimeState().isActivated()) {
						programHandler.activate();
					}
					Program program = programHandler.getProgram();
					if (!program.getRuntimeState().isStarted()) {
						programHandler.start();
					}
				}
			} catch (ProgramException e) {
				e.printStackTrace();
			}
			if (!programsAndFeatures.isInstalled(programId, programVersion)) {
				userProgramsToInstall.add(currUserProgram);
			}
		}

		// (3) Install and start the program, if it is not already installed in the platform.
		for (UserProgram currUserProgramToInstall : userProgramsToInstall) {
			String programId = currUserProgramToInstall.getId();
			String programVersion = currUserProgramToInstall.getVersion();
			try {
				ProgramManifest programManifest = programsAndFeatures.install(programId, programVersion, false);
				if (programManifest != null) {
					LOG.debug("Program ('" + programId + "'," + programVersion + ") is installed.");
					LOG.debug(programManifest.toString());
				} else {
					LOG.debug("Program ('" + programId + "'," + programVersion + ") is not installed.");
				}
			} catch (ProgramException e) {
				e.printStackTrace();
			}
		}

		// 2. Add accountId to the world
		world.getAccountIds().add(accountId);

		return true;
	}

	@Override
	public synchronized boolean leave(World world, String accessToken) throws ServerException {
		LOG.debug("leave()");

		return false;
	}

}
