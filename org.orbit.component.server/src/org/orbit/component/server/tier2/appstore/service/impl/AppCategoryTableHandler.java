package org.orbit.component.server.tier2.appstore.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.orbit.component.model.tier2.appstore.AppCategoryRTO;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.jdbc.ResultSetSingleHandler;

public class AppCategoryTableHandler implements DatabaseTableAware {

	public static AppCategoryTableHandler INSTANCE = new AppCategoryTableHandler();

	@Override
	public String getTableName() {
		return "AppCategory";
	}

	@Override
	public String getCreateTableSQL(String database) {
		StringBuilder sb = new StringBuilder();

		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    categoryId varchar(500) NOT NULL,");
			sb.append("    namespace varchar(500) NOT NULL,");
			sb.append("    parentId varchar(500) NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    description varchar(2000),");
			sb.append("    PRIMARY KEY (categoryId)");
			sb.append(");");

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sb.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
			sb.append("    categoryId varchar(500) NOT NULL,");
			sb.append("    namespace varchar(500) NOT NULL,");
			sb.append("    parentId varchar(500) NOT NULL,");
			sb.append("    name varchar(500) NOT NULL,");
			sb.append("    description varchar(2000),");
			sb.append("    PRIMARY KEY (categoryId)");
			sb.append(");");
		}

		return sb.toString();
	}

	/**
	 * Create a AppCategoryRTO from a ResultSet record.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected AppCategoryRTO toRTO(ResultSet rs) throws SQLException {
		String categoryId = rs.getString("categoryId");
		String parentId = rs.getString("parentId");
		String name = rs.getString("name");
		String description = rs.getString("description");

		return new AppCategoryRTO(categoryId, parentId, name, description);
	}

	/**
	 * Get root categories.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<AppCategoryRTO> getRootCategories(Connection conn) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY categoryId ASC WHERE parentId='-1'";
		ResultSetListHandler<AppCategoryRTO> handler = new ResultSetListHandler<AppCategoryRTO>() {
			@Override
			protected AppCategoryRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, null, handler);
	}

	/**
	 * Get categories.
	 * 
	 * @param conn
	 * @param parentId
	 * @return
	 * @throws SQLException
	 */
	public List<AppCategoryRTO> getCategories(Connection conn, String parentId) throws SQLException {
		if (parentId == null || parentId.isEmpty() || parentId.equals("-1")) {
			return getRootCategories(conn);
		}
		String querySQL = "SELECT * FROM " + getTableName() + " ORDER BY categoryId ASC WHERE parentId=?";
		ResultSetListHandler<AppCategoryRTO> handler = new ResultSetListHandler<AppCategoryRTO>() {
			@Override
			protected AppCategoryRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentId }, handler);
	}

	/**
	 * Get a category.
	 * 
	 * @param conn
	 * @param categoryId
	 * @return
	 * @throws SQLException
	 */
	public AppCategoryRTO getCategory(Connection conn, String categoryId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE categoryId=?";
		ResultSetSingleHandler<AppCategoryRTO> handler = new ResultSetSingleHandler<AppCategoryRTO>() {
			@Override
			protected AppCategoryRTO handleRow(ResultSet rs) throws SQLException {
				return toRTO(rs);
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { categoryId }, handler);
	}

	/**
	 * Check if a category exists.
	 * 
	 * @param conn
	 * @param categoryId
	 * @return
	 * @throws SQLException
	 */
	public boolean hasCategory(Connection conn, String categoryId) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE categoryId=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { categoryId }, handler);
	}

	/**
	 * Check if a category exists.
	 * 
	 * @param conn
	 * @param parentId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean hasCategory(Connection conn, String parentId, String name) throws SQLException {
		String querySQL = "SELECT * FROM " + getTableName() + " WHERE parentId=? AND name=?";
		AbstractResultSetHandler<Boolean> handler = new AbstractResultSetHandler<Boolean>() {
			@Override
			public Boolean handle(ResultSet rs) throws SQLException {
				return rs.next() ? true : false;
			}
		};
		return DatabaseUtil.query(conn, querySQL, new Object[] { parentId, name }, handler);
	}

	/**
	 * Insert a category.
	 * 
	 * @param conn
	 * @param parentId
	 * @return
	 * @throws SQLException
	 */
	public AppCategoryRTO insert(Connection conn, String categoryId, String parentId, String name, String description) throws SQLException {
		String insertSQL = "INSERT INTO " + getTableName() + " (categoryId, parentId, name, description) VALUES (?, ?, ?, ?)";
		boolean succeed = DatabaseUtil.update(conn, insertSQL, new Object[] { categoryId, parentId, name, description }, 1);
		if (succeed) {
			return getCategory(conn, categoryId);
		}
		return null;
	}

	/**
	 * 
	 * @param conn
	 * @param categoryId
	 * @param columnToValueMap
	 * @return
	 * @throws SQLException
	 */
	public boolean update(Connection conn, String categoryId, Map<String, Object> columnToValueMap) throws SQLException {
		return DatabaseUtil.update(conn, getTableName(), "categoryId", categoryId, columnToValueMap);
	}

	/**
	 * Update parent id.
	 * 
	 * @param conn
	 * @param categoryId
	 * @param parentId
	 * @return
	 * @throws SQLException
	 */
	public boolean updateParentId(Connection conn, String categoryId, String parentId) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET parentId=? WHERE categoryId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { parentId, categoryId }, 1);
	}

	/**
	 * Update name.
	 * 
	 * @param conn
	 * @param categoryId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean updateName(Connection conn, String categoryId, String name) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET name=? WHERE categoryId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { name, categoryId }, 1);
	}

	/**
	 * Update description.
	 * 
	 * @param conn
	 * @param categoryId
	 * @param description
	 * @return
	 * @throws SQLException
	 */
	public boolean updateDescription(Connection conn, String categoryId, String description) throws SQLException {
		String updateSQL = "UPDATE " + getTableName() + " SET description=? WHERE categoryId=?";
		return DatabaseUtil.update(conn, updateSQL, new Object[] { description, categoryId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param categoryId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String categoryId) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE categoryId=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { categoryId }, 1);
	}

	/**
	 * 
	 * @param conn
	 * @param parentId
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, String parentId, String name) throws SQLException {
		String deleteSQL = "DELETE FROM " + getTableName() + " WHERE parentId=? AND name=?";
		return DatabaseUtil.update(conn, deleteSQL, new Object[] { parentId, name }, 1);
	}

}
