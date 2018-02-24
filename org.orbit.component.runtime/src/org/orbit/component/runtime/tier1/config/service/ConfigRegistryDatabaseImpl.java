package org.orbit.component.runtime.tier1.config.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.orbit.component.model.tier1.config.EPath;
import org.orbit.component.model.tier1.config.RegistryPathVO;
import org.orbit.component.model.tier1.config.RegistryPropertyVO;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;

/**
 * Database implementation of a ConfigRegistry.
 *
 */
public class ConfigRegistryDatabaseImpl implements ConfigRegistry {

	protected String userId;
	protected Properties databaseProperties;

	protected ConfigRegistryPathTableHandler pathTableHandler = ConfigRegistryPathTableHandler.INSTANCE;
	protected ConfigRegistryPropertyTableHandler propertyTableHandler = ConfigRegistryPropertyTableHandler.INSTANCE;

	/**
	 * 
	 * @param userId
	 * @param databaseProperties
	 */
	public ConfigRegistryDatabaseImpl(String userId, Properties databaseProperties) {
		this.userId = userId;
		this.databaseProperties = databaseProperties;
	}

	public String getUserId() {
		return this.userId;
	}

	/**
	 * Update database connection properties.
	 * 
	 * @param databaseProperties
	 */
	public synchronized void update(Properties databaseProperties) {
		this.databaseProperties = databaseProperties;
	}

	protected Connection getConnection() {
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
		Connection conn = getConnection();
		try {
			List<RegistryPathVO> pathVOs = this.pathTableHandler.getPaths(conn, this.userId);
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
		Connection conn = getConnection();
		try {
			hasPath = this.pathTableHandler.hasPath(conn, this.userId, path.getPathString());
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
		Connection conn = getConnection();
		try {
			RegistryPathVO newPathVO = this.pathTableHandler.insertPath(conn, this.userId, path.getPathString(), description);
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
		Connection conn = getConnection();
		try {
			return this.pathTableHandler.updatePath(conn, this.userId, path.getPathString(), newPath.getPathString());

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
		Connection conn = getConnection();
		try {
			return this.pathTableHandler.updateDescription(conn, this.userId, path.getPathString(), description);

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
		Connection conn = getConnection();
		try {
			// delete all properties in the path. then delete the path
			return this.pathTableHandler.deletePath(conn, this.userId, path.getPathString());

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean removeAllPaths() throws ServerException {
		Connection conn = getConnection();
		try {
			// delete all paths and all properties in each path.
			return this.pathTableHandler.deleteAllPaths(conn, userId);

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
		Connection conn = getConnection();
		try {
			List<RegistryPropertyVO> propertyVOs = this.propertyTableHandler.getProperties(conn, this.userId, path.getPathString());
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
		Connection conn = getConnection();
		try {
			RegistryPropertyVO propertyVO = this.propertyTableHandler.getProperty(conn, this.userId, path.getPathString(), name);
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
		Connection conn = getConnection();
		try {
			return this.propertyTableHandler.setProperty(conn, this.userId, path.getPathString(), name, value);

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean removeProperty(EPath path, String name) throws ServerException {
		Connection conn = getConnection();
		try {
			return this.propertyTableHandler.deleteProperty(conn, this.userId, path.getPathString(), name);

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

	@Override
	public boolean removeProperties(EPath path) throws ServerException {
		Connection conn = getConnection();
		try {
			return this.propertyTableHandler.deleteProperties(conn, this.userId, path.getPathString());

		} catch (Exception e) {
			handleException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return false;
	}

}
