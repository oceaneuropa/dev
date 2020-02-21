package org.orbit.infra.runtime.configregistry.service.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.infra.runtime.configregistry.service.ConfigElement;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistry;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryMetadata;
import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.server.ServerException;
import org.origin.common.util.UUIDUtil;

public class ConfigRegistryImpl implements ConfigRegistry {

	public static ConfigElement[] EMPTY_ELEMENTS = new ConfigElement[0];

	protected ConfigRegistryService service;
	protected ConfigRegistryMetadata metadata;

	/**
	 * 
	 * @param service
	 * @param metadata
	 */
	public ConfigRegistryImpl(ConfigRegistryService service, ConfigRegistryMetadata metadata) {
		this.service = service;
		this.metadata = metadata;
	}

	@Override
	public ConfigRegistryService getService() {
		return this.service;
	}

	@Override
	public ConfigRegistryMetadata getMetadata() {
		return this.metadata;
	}

	@Override
	public void setMetadata(ConfigRegistryMetadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public String getId() {
		return this.metadata.getId();
	}

	@Override
	public String getType() {
		return this.metadata.getType();
	}

	@Override
	public String getName() {
		return this.metadata.getName();
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		Connection conn = this.service.getConnection();
		if (conn == null) {
			throw new SQLException("Cannot get JDBC Connection.");
		}
		return conn;
	}

	/**
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	protected ConfigRegistryElementsTableHandler getTableHandler(Connection conn) throws SQLException {
		return ConfigRegistryElementsTableHandler.getInstance(conn, getId());
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleSQLException(SQLException e) throws IOException {
		throw new IOException(e.getMessage(), e);
	}

	/**
	 * 
	 * @param e
	 * @throws ServerException
	 */
	protected void handleClientException(ClientException e) throws IOException {
		throw new IOException(e.getMessage(), e);
	}

	/**
	 * 
	 * @param configElements
	 * @throws IOException
	 */
	protected void updatePath(ConfigElement[] configElements) throws IOException {
		if (configElements != null) {
			for (ConfigElement configElement : configElements) {
				updatePath(configElement);
			}
		}
	}

	/**
	 * 
	 * @param configElement
	 * @throws IOException
	 */
	protected void updatePath(ConfigElement configElement) throws IOException {
		if (configElement != null) {
			String elementId = configElement.getElementId();
			Path path = getPath(elementId);
			if (path != null) {
				configElement.setPath(path);
			}
		}
	}

	@Override
	public ConfigElement[] listRoots() throws IOException {
		ConfigElement[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			List<ConfigElement> configElements = tableHandler.getList(conn, "-1");
			if (configElements != null) {
				result = configElements.toArray(new ConfigElement[configElements.size()]);
				updatePath(result);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_ELEMENTS;
		}
		return result;
	}

	@Override
	public ConfigElement[] listElements(String parentElementId) throws IOException {
		ConfigElement[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			List<ConfigElement> configElements = tableHandler.getList(conn, parentElementId);
			if (configElements != null) {
				result = configElements.toArray(new ConfigElement[configElements.size()]);
				updatePath(result);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_ELEMENTS;
		}
		return result;
	}

	@Override
	public ConfigElement[] listElements(Path parentPath) throws IOException {
		ConfigElement[] result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			ConfigElement parentConfigElement = getElement(parentPath);
			if (parentConfigElement != null) {
				String parentElementId = parentConfigElement.getElementId();

				List<ConfigElement> configElements = tableHandler.getList(conn, parentElementId);
				if (configElements != null) {
					result = configElements.toArray(new ConfigElement[configElements.size()]);
					updatePath(result);
				}
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (result == null) {
			result = EMPTY_ELEMENTS;
		}
		return result;
	}

	@Override
	public ConfigElement getElement(String elementId) throws IOException {
		ConfigElement result = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			result = tableHandler.getByElementId(conn, elementId);
			updatePath(result);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return result;
	}

	@Override
	public ConfigElement getElement(Path path) throws IOException {
		if (path == null || path.isEmpty()) {
			// Path is empty, which cannot be used to resolve a element. Element cannot be resolved.
			return null;
		}

		ConfigElement element = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			String currParentElementId = "-1";

			String[] pathSegments = path.getSegments();
			for (int i = 0; i < pathSegments.length; i++) {
				ConfigElement currConfigElement = tableHandler.getByName(conn, currParentElementId, pathSegments[i]);

				boolean isLastSegment = (i == (pathSegments.length - 1)) ? true : false;
				if (!isLastSegment) {
					// Current element is an intermediate segment.
					if (currConfigElement == null) {
						// Current intermediate config element cannot be found. Config element cannot be resolved. Config element doesn't exist.
						return null;
					}

					// Look for the next segment until reaching the last segment, which should be the config element name, if exists.
					currParentElementId = currConfigElement.getElementId();

				} else {
					// Current config element is the last segment, which should be the config element, if exists.
					if (currConfigElement != null) {
						element = currConfigElement;
					}
				}
			}

			if (element != null) {
				element.setPath(path);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return element;
	}

	@Override
	public ConfigElement getElement(String parentElementId, String name) throws IOException {
		ConfigElement element = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			element = tableHandler.getByName(conn, parentElementId, name);

			if (element != null) {
				updatePath(element);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return element;
	}

	@Override
	public ConfigElement getElement(String parentElementId, Path path) throws IOException {
		if (path == null || path.isEmpty()) {
			// Path is empty, which cannot be used to resolve a element. Element cannot be resolved.
			return null;
		}

		ConfigElement element = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			String currParentElementId = parentElementId;

			String[] pathSegments = path.getSegments();
			for (int i = 0; i < pathSegments.length; i++) {
				ConfigElement currElement = tableHandler.getByName(conn, currParentElementId, pathSegments[i]);

				boolean isLastSegment = (i == (pathSegments.length - 1)) ? true : false;
				if (!isLastSegment) {
					// Current element is an intermediate segment.
					if (currElement == null) {
						// Current intermediate element cannot be found. Element cannot be resolved. Element doesn't exist.
						return null;
					}

					// Look for the next segment until reaching the last segment, which should be the element name, if exists.
					currParentElementId = currElement.getElementId();

				} else {
					// Current element is the last segment, which should be the element, if exists.
					if (currElement != null) {
						element = currElement;
					}
				}
			}

			if (element != null) {
				updatePath(element);
			}

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return element;
	}

	@Override
	public Path getPath(String elementId) throws IOException {
		Path path = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			ConfigElement element = tableHandler.getByElementId(conn, elementId);
			if (element == null) {
				// Config element doesn't exists. Path cannot be constructed.
				return null;
			}

			Path currPath = new Path(element.getName());

			String currParentElementId = element.getParentElementId();
			while (currParentElementId != null && !currParentElementId.isEmpty() && !currParentElementId.equals("-1")) {
				ConfigElement parentConfigElement = tableHandler.getByElementId(conn, currParentElementId);
				if (parentConfigElement == null) {
					// Parent config element doesn't exists. Path cannot be constructed.
					return null;
				}

				String parentElementName = parentConfigElement.getName();
				currPath = new Path(parentElementName, currPath);

				currParentElementId = parentConfigElement.getParentElementId();
			}

			path = new Path(Path.ROOT, currPath);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return path;
	}

	@Override
	public boolean elementExists(String elementId) throws IOException {
		ConfigElement element = getElement(elementId);
		if (element != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean elementExists(Path path) throws IOException {
		ConfigElement element = getElement(path);
		if (element != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean elementExists(String parentElementId, String name) throws IOException {
		ConfigElement element = getElement(parentElementId, name);
		if (element != null) {
			return true;
		}
		return false;
	}

	@Override
	public ConfigElement createElement(Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		if (path == null || path.isEmpty()) {
			throw new IOException("Path is empty.");
		}

		ConfigElement element = null;
		if (!generateUniqueName) {
			element = getElement(path);
			if (element != null) {
				throw new IOException("Element with path '" + path.getPathString() + "' already exists.");
			}
		}

		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			String currParentElementId = "-1";

			String[] pathSegments = path.getSegments();
			for (int i = 0; i < pathSegments.length; i++) {
				String currPathSegment = pathSegments[i];
				ConfigElement currElement = tableHandler.getByName(conn, currParentElementId, currPathSegment);

				boolean isLastSegment = (i == (pathSegments.length - 1)) ? true : false;
				if (!isLastSegment) {
					// Current element is an intermediate segment of the path.
					// - Ensure the current element exist
					if (currElement == null) {
						currElement = createElement(currParentElementId, currPathSegment, null, false);
					}

					if (currElement == null) {
						// Current element doesn't exist and cannot be created.
						Path currPath = path.getPath(0, i + 1);
						throw new IOException("Config element with path '" + currPath.getPathString() + "' cannot be created.");
					}

					// Look for the next segment of the path.
					currParentElementId = currElement.getElementId();

				} else {
					// Current segment is the last segment of the path, which is for creating the new config element itself.
					element = createElement(currParentElementId, currPathSegment, attributes, generateUniqueName);
				}
			}

			if (element == null) {
				throw new RuntimeException("New element cannot be created.");
			}

			updatePath(element);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return element;
	}

	@Override
	public ConfigElement createElement(String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		ConfigElement element = null;
		Connection conn = null;
		try {
			if (parentElementId == null || parentElementId.isEmpty()) {
				parentElementId = "-1";
			}

			if (generateUniqueName) {
				int number = 1;
				boolean isLastSegmentNumber = false;
				int lastNumber = -1;
				int index = name.lastIndexOf("_");
				if (index > 0 && index < name.length() - 1) {
					String lastSegment = name.substring(index + 1);
					try {
						lastNumber = Integer.parseInt(lastSegment);
						number = lastNumber + 1;
						isLastSegmentNumber = true;
					} catch (Exception e) {
					}
				}

				String baseName = null;
				if (isLastSegmentNumber) {
					baseName = name.substring(0, index);
				} else {
					baseName = name;
				}
				while (elementExists(parentElementId, name)) {
					name = baseName + "_" + number;
					number++;
				}

			} else {
				element = getElement(parentElementId, name);
				if (element != null) {
					throw new IOException("Element with name '" + name + "' already exists.");
				}
			}

			// Check existence of parent element
			if (!"-1".equals(parentElementId)) {
				boolean parentExists = elementExists(parentElementId);
				if (!parentExists) {
					throw new RuntimeException("Parent config element with element id '" + parentElementId + "' does not exists.");
				}
			}

			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			String elementId = generateElementId();

			element = tableHandler.create(conn, elementId, parentElementId, name, attributes);
			if (element == null) {
				throw new RuntimeException("New config element cannot be created.");
			}

			updatePath(element);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return element;
	}

	@Override
	public ConfigElement createElement(String parentElementId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		if (parentElementId == null || parentElementId.isEmpty()) {
			parentElementId = "-1";
		}
		if (path == null || path.isEmpty()) {
			throw new IOException("Path is empty.");
		}

		ConfigElement element = null;
		if (!generateUniqueName) {
			element = getElement(parentElementId, path);
			if (element != null) {
				throw new IOException("Element with path '" + path.getPathString() + "' already exists.");
			}
		}

		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			String currParentElementId = parentElementId;

			String[] pathSegments = path.getSegments();
			for (int i = 0; i < pathSegments.length; i++) {
				String currPathSegment = pathSegments[i];

				ConfigElement currElement = tableHandler.getByName(conn, currParentElementId, currPathSegment);

				boolean isLastSegment = (i == (pathSegments.length - 1)) ? true : false;
				if (!isLastSegment) {
					// Current element is an intermediate segment of the path.
					// - Ensure the current config element exist
					if (currElement == null) {
						currElement = createElement(currParentElementId, currPathSegment, null, false);
					}

					if (currElement == null) {
						// Current element doesn't exist and cannot be created.
						Path currPath = path.getPath(0, i + 1);
						throw new IOException("Element with path '" + currPath.getPathString() + "' cannot be created.");
					}

					// Look for the next segment of the path.
					currParentElementId = currElement.getElementId();

				} else {
					// Current segment is the last segment of the path, which is for creating the new element itself.
					element = createElement(currParentElementId, currPathSegment, attributes, generateUniqueName);
				}
			}

			if (element == null) {
				throw new RuntimeException("New element cannot be created.");
			}

			updatePath(element);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return element;
	}

	public String generateElementId() {
		String uuid = UUIDUtil.generateBase64EncodedUUID();
		return uuid;
	}

	@Override
	public boolean updateName(String elementId, String name) throws IOException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			ConfigElement configElement = tableHandler.getByElementId(conn, elementId);
			if (configElement == null) {
				throw new IOException("Element doesn't exist.");
			}

			isUpdated = tableHandler.updateName(conn, elementId, name);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateAttributes(String elementId, Map<String, Object> attributes) throws IOException {
		boolean isUpdated = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			isUpdated = tableHandler.updateAttributes(conn, elementId, attributes);

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteElement(String elementId) throws IOException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			ConfigElement configElement = tableHandler.getByElementId(conn, elementId);
			if (configElement == null) {
				throw new IOException("Element doesn't exist.");
			}

			isDeleted = doDelete(conn, tableHandler, configElement, new ArrayList<ConfigElement>());

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteElement(Path path) throws IOException {
		boolean isDeleted = false;
		Connection conn = null;
		try {
			conn = getConnection();
			ConfigRegistryElementsTableHandler tableHandler = getTableHandler(conn);

			ConfigElement configElement = getElement(path);
			if (configElement == null) {
				throw new IOException("Element doesn't exist.");
			}

			doDelete(conn, tableHandler, configElement, new ArrayList<ConfigElement>());

		} catch (SQLException e) {
			handleSQLException(e);
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return isDeleted;
	}

	/**
	 * 
	 * @param conn
	 * @param tableHandler
	 * @param configElement
	 * @param encountered
	 * @return
	 * @throws SQLException
	 */
	protected boolean doDelete(Connection conn, ConfigRegistryElementsTableHandler tableHandler, ConfigElement configElement, List<ConfigElement> encountered) throws SQLException {
		if (configElement == null) {
			return false;
		}
		if (encountered.contains(configElement)) {
			return true;
		}
		encountered.add(configElement);

		String elementId = configElement.getElementId();

		List<ConfigElement> memberConfigElements = tableHandler.getList(conn, elementId);
		if (memberConfigElements != null) {
			for (ConfigElement memberConfigElement : memberConfigElements) {
				boolean currIsDeleted = doDelete(conn, tableHandler, memberConfigElement, encountered);
				if (!currIsDeleted) {
					return false;
				}
			}
		}

		boolean isDeleted = tableHandler.deleteByElementId(conn, elementId);
		return isDeleted;
	}

}
