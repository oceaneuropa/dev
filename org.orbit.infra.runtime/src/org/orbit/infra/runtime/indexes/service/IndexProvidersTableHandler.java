package org.orbit.infra.runtime.indexes.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.orbit.infra.model.indexes.IndexProviderItem;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableProvider;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class IndexProvidersTableHandler implements DatabaseTableProvider {

	protected String database;

	/**
	 * 
	 * @param database
	 */
	public IndexProvidersTableHandler(String database) {
		this.database = database;
	}

	@Override
	public String getTableName() {
		return "IndexProviders";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableProvider.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id varchar(500) NOT NULL,");
			sb.append("    name varchar(500),");
			sb.append("    description varchar(2000),");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");

		} else if (DatabaseTableProvider.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    id varchar(500) NOT NULL,");
			sb.append("    name varchar(500),");
			sb.append("    description varchar(2000),");
			sb.append("    dateCreated bigint DEFAULT 0,");
			sb.append("    dateModified bigint DEFAULT 0,");
			sb.append("    PRIMARY KEY (id)");
			sb.append(");");
		}

		return sb.toString();
	}

	/**
	 * Create a IndexProviderItem from a ResultSet record.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected IndexProviderItem toModel(ResultSet rs) throws SQLException {
		String id = rs.getString("id");
		String name = rs.getString("name");
		String description = rs.getString("description");
		long dateCreated = rs.getLong("dateCreated");
		long dateModified = rs.getLong("dateModified");

		IndexProviderItem item = new IndexProviderItem(id);
		item.setName(name);
		item.setDescription(description);
		item.setDateCreated(dateCreated);
		item.setDateModified(dateModified);

		return item;
	}

	/**
	 * Get index providers list.
	 * 
	 * @param conn
	 * @param namespace
	 * @return
	 * @throws SQLException
	 */
	public List<IndexProviderItem> getIndexProviders(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY id ASC";
		ResultSetListHandler<IndexProviderItem> handler = new ResultSetListHandler<IndexProviderItem>() {
			@Override
			protected IndexProviderItem handleRow(ResultSet rs) throws SQLException {
				return toModel(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * Check if an index provider id exists.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean exists(Connection conn, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * Get an index providers.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public IndexProviderItem getIndexProvider(Connection conn, String id) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE id=?";
		ResultSetSingleHandler<IndexProviderItem> handler = new ResultSetSingleHandler<IndexProviderItem>() {
			@Override
			protected IndexProviderItem handleRow(ResultSet rs) throws SQLException {
				return toModel(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { id }, handler);
	}

	/**
	 * Insert a index provider.
	 * 
	 * @param conn
	 * @param id
	 * @param name
	 * @param description
	 * @param dateCreated
	 * @param dateModified
	 * @return
	 * @throws SQLException
	 */
	public IndexProviderItem insert(Connection conn, String id, String name, String description, long dateCreated, long dateModified) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (id, name, description, dateCreated, dateModified) VALUES (?, ?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { id, name, description, dateCreated, dateModified }, 1);
		if (succeed) {
			return getIndexProvider(conn, id);
		}
		return null;
	}

	/**
	 * Update name.
	 * 
	 * @param conn
	 * @param id
	 * @param appVersion
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String id, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=?, dateModified=? WHERE id=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, dateModified, id }, 1);
	}

	/**
	 * Update description.
	 * 
	 * @param conn
	 * @param id
	 * @param appVersion
	 * @param description
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDescription(Connection conn, String id, String description) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET description=?, dateModified=? WHERE id=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { description, dateModified, id }, 1);
	}

	/**
	 * Update modified date.
	 * 
	 * @param conn
	 * @param id
	 * @param appVersion
	 * @return
	 * @throws SQLException
	 */
	public boolean updateModifiedDate(Connection conn, String id) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET dateModified=? WHERE id=?";
		long dateModified = new Date().getTime();
		return DatabaseUtil.update(conn, updateSQL, new Object[] { dateModified, id }, 1);
	}

	/**
	 * Delete an index provider.
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String id) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE id=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { id }, 1);
	}

}
