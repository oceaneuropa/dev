package org.origin.core.resources.impl.database;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.core.resources.Constants;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.IWorkspaceService;
import org.origin.core.resources.WorkspaceDescription;
import org.origin.core.resources.impl.misc.WorkspaceNameComparator;
import org.osgi.framework.BundleContext;

public class WorkspaceServiceDatabaseImpl implements IWorkspaceService, ConnectionAware {

	protected List<IWorkspace> workspaces = new ArrayList<IWorkspace>();
	protected Map<Object, Object> properties;
	protected Properties jdbcProperties;

	/**
	 * 
	 * @param properties
	 */
	public WorkspaceServiceDatabaseImpl(Map<Object, Object> properties) {
		this.properties = properties;
	}

	@Override
	public void start(BundleContext context) throws IOException {
		update(this.properties);
	}

	@Override
	public void stop(BundleContext context) throws IOException {
	}

	@Override
	public Connection getConnection() {
		return DatabaseUtil.getConnection(this.jdbcProperties);
	}

	/**
	 * 
	 * @param properties
	 * @throws IOException
	 */
	public synchronized void update(Map<Object, Object> properties) throws IOException {
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		this.properties = properties;

		String id = (String) properties.get(Constants.WORKSPACES_ID);
		String jdbcDriver = (String) properties.get(Constants.WORKSPACES_JDBC_DRIVER);
		String jdbcURL = (String) properties.get(Constants.WORKSPACES_JDBC_URL);
		String jdbcUsername = (String) properties.get(Constants.WORKSPACES_JDBC_USERNAME);
		String jdbcPassword = (String) properties.get(Constants.WORKSPACES_JDBC_PASSWORD);
		this.jdbcProperties = DatabaseUtil.getProperties(jdbcDriver, jdbcURL, jdbcUsername, jdbcPassword);

		// this.workspacesTableHandler = new WorkspacesTableHandler(id);
		Connection conn = getConnection();
		try {
			// DatabaseUtil.initialize(conn, this.workspacesTableHandler);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		load();
	}

	protected String getId() {
		String id = (String) this.properties.get(Constants.WORKSPACES_ID);
		return id;
	}

	protected void load() throws IOException {
		synchronized (this.workspaces) {
			this.workspaces.clear();

			Connection conn = getConnection();
			try {
				// List<WorkspaceVO> workspaceVOs = this.workspacesTableHandler.getAll(conn);
				// for (WorkspaceVO workspaceVO : workspaceVOs) {
				// int workspaceId = workspaceVO.getWorkspaceId();
				// IWorkspace workspace = new WorkspaceDatabaseImpl(getId(), this, workspaceId);
				// this.workspaces.add(workspace);
				// }
			} catch (Exception e) {
				// DatabaseUtil.handleSQLExceptionWithIOException(e);
			} finally {
				DatabaseUtil.closeQuietly(conn, true);
			}
			Collections.sort(this.workspaces, WorkspaceNameComparator.ASC);
		}
	}

	/**
	 * 
	 * @param name
	 * @throws IOException
	 */
	protected void checkName(String name) throws IOException {
		if (name == null || name.isEmpty()) {
			throw new IOException("Workspace name is empyt.");
		}
	}

	@Override
	public List<String> getWorkspaces() throws IOException {
		List<String> names = new ArrayList<String>();
		synchronized (this.workspaces) {
			for (IWorkspace workspace : this.workspaces) {
				names.add(workspace.getName());
			}
		}
		return names;
	}

	@Override
	public IWorkspace open(String name, String password) throws IOException {
		checkName(name);

		IWorkspace matchedWorkspace = null;
		synchronized (this.workspaces) {
			for (IWorkspace workspace : this.workspaces) {
				if (name.equals(workspace.getName())) {
					matchedWorkspace = workspace;
					break;
				}
			}
		}

		if (matchedWorkspace != null) {
			boolean isPasswordRequired = false;
			boolean matchPassword = false;
			WorkspaceDescription desc = matchedWorkspace.getDescription();
			if (desc != null && desc.getPassword() != null && !desc.getPassword().isEmpty()) {
				isPasswordRequired = true;
				if (desc.getPassword().equals(password)) {
					matchPassword = true;
				}
			}
			if (isPasswordRequired) {
				if (matchPassword) {
					return matchedWorkspace;
				} else {
					throw new IOException("Wrong password");
				}
			} else {
				return matchedWorkspace;
			}
		} else {
			throw new IOException("Workspace does not exist.");
		}
	}

	@Override
	public IWorkspace create(String name, String password) throws IOException {
		return null;
	}

	@Override
	public boolean changePassword(String name, String oldPassword, String newPassword) throws IOException {
		return false;
	}

	@Override
	public boolean delete(String name, String password) throws IOException {
		return false;
	}

}

// WorkspaceDescription desc = new WorkspaceDescription();
// desc.setId(String.valueOf(workspaceId));
// desc.setName(name);
// desc.setPassword(password);
// desc.setDescription(description);
