package org.orbit.component.runtime.tier1.config.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.runtime.model.configregistry.EPath;
import org.orbit.component.runtime.model.configregistry.RegistryPathVO;
import org.orbit.component.runtime.model.configregistry.RegistryPropertyVO;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;

/**
 * Database implementation of a ConfigRegistry.
 *
 */
public class ConfigRegistryDatabaseImpl implements ConfigRegistry {

	protected String accountId;
	protected Properties databaseProperties;

	protected ConfigRegistryPathTableHandler pathTableHandler = ConfigRegistryPathTableHandler.INSTANCE;
	protected ConfigRegistryPropertyTableHandler propertyTableHandler = ConfigRegistryPropertyTableHandler.INSTANCE;

	/**
	 * 
	 * @param accountId
	 * @param databaseProperties
	 */
	public ConfigRegistryDatabaseImpl(String accountId, Properties databaseProperties) {
		this.accountId = accountId;
		this.databaseProperties = databaseProperties;
	}

	public String getAccountId() {
		return this.accountId;
	}

	/**
	 * Update database connection properties.
	 * 
	 * @param databaseProperties
	 */
	public synchronized void update(Properties databaseProperties) {
		this.databaseProperties = databaseProperties;
	}

	protected Connection getConnection() throws SQLException {
		return DatabaseUtil.getConnection(this.databaseProperties);
	}

	protected void handleException(Exception e) throws ServerException {
		throw new ServerException(StatusDTO.RESP_500, e.getMessage(), e);
	}

	// ---------------------------------------------------------------------------------------------------------
	// Path
	// ---------------------------------------------------------------------------------------------------------
	@Override
	public List<EPath> getPaths() throws ServerException {
		List<EPath> paths = new ArrayList<EPath>();
		Connection conn = null;
		try {
			conn = getConnection();
			List<RegistryPathVO> pathVOs = this.pathTableHandler.getPaths(conn, this.accountId);
			if (pathVOs != null) {
				for (RegistryPathVO pathVO : pathVOs) {
					EPath path = pathVO.getPath();
					if (path != null) {
						paths.add(path);
					}
				}
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return paths;
	}

	@Override
	public boolean hasPath(EPath path) throws ServerException {
		boolean hasPath = false;
		Connection conn = null;
		try {
			conn = getConnection();
			hasPath = this.pathTableHandler.hasPath(conn, this.accountId, path.getPathString());
		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return hasPath;
	}

	@Override
	public boolean addPath(EPath path, String description) throws ServerException {
		if (hasPath(path)) {
			return false;
		}
		Connection conn = null;
		try {
			conn = getConnection();
			RegistryPathVO newPathVO = this.pathTableHandler.insertPath(conn, this.accountId, path.getPathString(), description);
			if (newPathVO != null) {
				return true;
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean updatePath(EPath path, EPath newPath) throws ServerException {
		if (hasPath(path)) {
			return false;
		}
		Connection conn = null;
		try {
			conn = getConnection();
			return this.pathTableHandler.updatePath(conn, this.accountId, path.getPathString(), newPath.getPathString());

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean updatePathDescription(EPath path, String description) throws ServerException {
		if (hasPath(path)) {
			return false;
		}
		Connection conn = null;
		try {
			conn = getConnection();
			return this.pathTableHandler.updateDescription(conn, this.accountId, path.getPathString(), description);

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean removePath(EPath path) throws ServerException {
		if (hasPath(path)) {
			return false;
		}
		Connection conn = null;
		try {
			conn = getConnection();
			// delete all properties in the path. then delete the path
			return this.pathTableHandler.deletePath(conn, this.accountId, path.getPathString());

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean removeAllPaths() throws ServerException {
		Connection conn = null;
		try {
			conn = getConnection();
			// delete all paths and all properties in each path.
			return this.pathTableHandler.deleteAllPaths(conn, accountId);

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	// ---------------------------------------------------------------------------------------------------------
	// Path properties
	// ---------------------------------------------------------------------------------------------------------
	@Override
	public Map<String, String> getProperties(EPath path) throws ServerException {
		Map<String, String> properties = new LinkedHashMap<String, String>();
		Connection conn = null;
		try {
			conn = getConnection();
			List<RegistryPropertyVO> propertyVOs = this.propertyTableHandler.getProperties(conn, this.accountId, path.getPathString());
			if (propertyVOs != null) {
				for (RegistryPropertyVO propertyVO : propertyVOs) {
					String name = propertyVO.getName();
					String value = propertyVO.getValue();
					properties.put(name, value);
				}
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return properties;
	}

	@Override
	public String getProperty(EPath path, String name) throws ServerException {
		String value = null;
		Connection conn = null;
		try {
			conn = getConnection();
			RegistryPropertyVO propertyVO = this.propertyTableHandler.getProperty(conn, this.accountId, path.getPathString(), name);
			if (propertyVO != null) {
				value = propertyVO.getValue();
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return value;
	}

	@Override
	public boolean setProperty(EPath path, String name, String value) throws ServerException {
		Connection conn = null;
		try {
			conn = getConnection();
			return this.propertyTableHandler.setProperty(conn, this.accountId, path.getPathString(), name, value);

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean removeProperty(EPath path, String name) throws ServerException {
		Connection conn = null;
		try {
			conn = getConnection();
			return this.propertyTableHandler.deleteProperty(conn, this.accountId, path.getPathString(), name);

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean removeProperties(EPath path) throws ServerException {
		Connection conn = null;
		try {
			conn = getConnection();
			return this.propertyTableHandler.deleteProperties(conn, this.accountId, path.getPathString());

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

}
