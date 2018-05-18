package other.orbit.infra.runtime.indexes.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.orbit.infra.model.indexes.IndexItemRequestVO;
import org.origin.common.jdbc.AbstractResultSetHandler;
import org.origin.common.jdbc.DatabaseTableAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.jdbc.ResultSetListHandler;
import org.origin.common.util.DateUtil;

/*
 * CRUD methods for the IndexItemRequest table.
 *
 */
public class IndexItemRequestTableHandler implements DatabaseTableAware {

	public static IndexItemRequestTableHandler INSTANCE = new IndexItemRequestTableHandler();

	public static final String STATUS_PENDING = "pending";
	public static final String STATUS_COMPLETED = "completed";
	public static final String STATUS_CANCELLED = "cancelled";

	protected ResultSetListHandler<IndexItemRequestVO> rsListHandler;
	protected AbstractResultSetHandler<IndexItemRequestVO> rsSingleHandler;

	public IndexItemRequestTableHandler() {
		this.rsListHandler = new ResultSetListHandler<IndexItemRequestVO>() {
			@Override
			protected IndexItemRequestVO handleRow(ResultSet rs) throws SQLException {
				return createVO(rs);
			}
		};

		this.rsSingleHandler = new AbstractResultSetHandler<IndexItemRequestVO>() {
			@Override
			public IndexItemRequestVO handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return createVO(rs);
				}
				return null;
			}
		};
	}

	/**
	 * Create a IndexItemRequestVO from a ResultSet.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected IndexItemRequestVO createVO(ResultSet rs) throws SQLException {
		Integer requestId = rs.getInt("requestId");
		String indexProviderId = rs.getString("indexProviderId");
		String command = rs.getString("command");
		String arguments = rs.getString("arguments");
		String status = rs.getString("status");
		String requestTimeString = rs.getString("requestTime");
		String lastUpdateTimeString = rs.getString("lastUpdateTime");

		Date requestTime = requestTimeString != null ? DateUtil.toDate(requestTimeString, DateUtil.getCommonDateFormats()) : null;
		Date lastUpdateTime = lastUpdateTimeString != null ? DateUtil.toDate(lastUpdateTimeString, DateUtil.getCommonDateFormats()) : null;

		return new IndexItemRequestVO(requestId, indexProviderId, command, arguments, status, requestTime, lastUpdateTime);
	}

	protected DateFormat getDateFormat() {
		return DateUtil.getJdbcDateFormat();
	}

	@Override
	public String getTableName() {
		return "IndexItemRequest";
	}

	protected String getPKName() {
		return "requestId";
	}

	@Override
	public String getCreateTableSQL(String database) {
		String sql = "";
		if (DatabaseTableAware.MYSQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS " + getTableName() + " (";
			sql += "	requestId int NOT NULL AUTO_INCREMENT,";
			sql += "	indexProviderId varchar(500) NOT NULL,";
			sql += "	command varchar(500) NOT NULL,";
			sql += "	arguments varchar(20000) NOT NULL,";
			sql += "	status varchar(50) DEFAULT NULL,";
			sql += "	requestTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (requestId)";
			sql += ");";

		} else if (DatabaseTableAware.POSTGRESQL.equalsIgnoreCase(database)) {
			sql += "CREATE TABLE IF NOT EXISTS " + getTableName() + " (";
			sql += "	requestId serial NOT NULL,";
			sql += "	indexProviderId varchar(500) NOT NULL,";
			sql += "	command varchar(500) NOT NULL,";
			sql += "	arguments varchar(20000) NOT NULL,";
			sql += "	status varchar(50) DEFAULT NULL,";
			sql += "	requestTime varchar(50) DEFAULT NULL,";
			sql += "	lastUpdateTime varchar(50) DEFAULT NULL,";
			sql += "	PRIMARY KEY (requestId)";
			sql += ");";
		}
		return sql;
	}

	/**
	 * 
	 * @param status
	 * @return
	 */
	protected String checkStatus(String status) {
		if (!STATUS_PENDING.equals(status) && !STATUS_CANCELLED.equals(status) && !STATUS_COMPLETED.equals(status)) {
			status = STATUS_PENDING;
		}
		return status;
	}

	/**
	 * Get a list of request items.
	 * 
	 * Later requests are shown first.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemRequestVO> getRequests(Connection conn) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " ORDER BY " + getPKName() + " DESC", null, this.rsListHandler);
	}

	/**
	 * Get a list of active pending requests (where the lastUpdateTime is updated every 1 second) that happened before the specified requestId. The
	 * request with the specified requestId need to wait until previous active requests are completed or cancelled (check every 1 second).
	 * 
	 * Pending requests with lastUpdateTime within 5 seconds of current time are considered as active requests and are included.
	 * 
	 * Pending requests with lastUpdateTime longer than 5 seconds of current time are considered as inactive or expired requests and are not included.
	 * 
	 * Later requests are shown first.
	 * 
	 * @param conn
	 * @param requestId
	 * @param withinSeconds
	 * @return
	 * @throws SQLException
	 */
	public List<IndexItemRequestVO> getActivePendingRequests(Connection conn, Integer requestId, int withinSeconds) throws SQLException {
		if (withinSeconds < 0) {
			withinSeconds = 0;
		}

		// gets a calendar using the default time zone and locale.
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -withinSeconds);
		Date expireDate = calendar.getTime();

		String expireDateString = DateUtil.toString(expireDate, getDateFormat());

		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE requestId<? AND status=? AND lastUpdateTime>=? ORDER BY " + getPKName() + " DESC", new Object[] { requestId, STATUS_PENDING, expireDateString }, this.rsListHandler);
	}

	/**
	 * Get a request by requestId.
	 * 
	 * @param conn
	 * @param requestId
	 * @return
	 * @throws SQLException
	 */
	public IndexItemRequestVO getRequest(Connection conn, Integer requestId) throws SQLException {
		return DatabaseUtil.query(conn, "SELECT * FROM " + getTableName() + " WHERE " + getPKName() + "=?", new Object[] { requestId }, this.rsSingleHandler);
	}

	/**
	 * Get the max requestId.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Integer getMaxRequestId(Connection conn) throws SQLException {
		AbstractResultSetHandler<Integer> handler = new AbstractResultSetHandler<Integer>() {
			@Override
			public Integer handle(ResultSet rs) throws SQLException {
				if (rs.next()) {
					return rs.getInt(1);
				}
				return 0;
			}
		};
		return DatabaseUtil.query(conn, "SELECT MAX(" + getPKName() + ") FROM " + getTableName() + "", null, handler);
	}

	/**
	 * Insert a request item.
	 * 
	 * @param conn
	 * @param indexProviderId
	 * @param command
	 * @param arguments
	 * @param status
	 * @param requestTime
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public IndexItemRequestVO insert(Connection conn, String indexProviderId, String command, String arguments, String status, Date requestTime, Date lastUpdateTime) throws SQLException {
		IndexItemRequestVO newRequestVO = null;

		status = checkStatus(status);

		if (lastUpdateTime == null) {
			lastUpdateTime = requestTime;
		}
		String requestTimeString = DateUtil.toString(requestTime, getDateFormat());
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());

		Integer requestId = DatabaseUtil.insert(conn, "INSERT INTO " + getTableName() + " (indexProviderId, command, arguments, status, requestTime, lastUpdateTime) VALUES (?, ?, ?, ?, ?, ?)", new Object[] { indexProviderId, command, arguments, status, requestTimeString, lastUpdateTimeString });
		if (requestId > 0) {
			newRequestVO = new IndexItemRequestVO(requestId, indexProviderId, command, arguments, status, requestTime, lastUpdateTime);
		}
		return newRequestVO;
	}

	/**
	 * Update request last update time.
	 * 
	 * @param conn
	 * @param requestId
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateLastUpdateTime(Connection conn, Integer requestId, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET lastUpdateTime=? WHERE requestId=?", new Object[] { lastUpdateTimeString, requestId }, 1);
	}

	/**
	 * Update request status as completed.
	 * 
	 * @param conn
	 * @param requestId
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateStatusAsCompleted(Connection conn, Integer requestId, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET status=?, lastUpdateTime=? WHERE requestId=?", new Object[] { STATUS_COMPLETED, lastUpdateTimeString, requestId }, 1);
	}

	/**
	 * Update request status as cancelled.
	 * 
	 * @param conn
	 * @param requestId
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 */
	public boolean updateStatusAsCancelled(Connection conn, Integer requestId, Date lastUpdateTime) throws SQLException {
		String lastUpdateTimeString = DateUtil.toString(lastUpdateTime, getDateFormat());
		return DatabaseUtil.update(conn, "UPDATE " + getTableName() + " SET status=?, lastUpdateTime=? WHERE requestId=?", new Object[] { STATUS_CANCELLED, lastUpdateTimeString, requestId }, 1);
	}

	/**
	 * Delete a request item.
	 * 
	 * @param conn
	 * @param requestId
	 * @return
	 * @throws SQLException
	 */
	public boolean delete(Connection conn, Integer requestId) throws SQLException {
		return DatabaseUtil.update(conn, "DELETE FROM " + getTableName() + " WHERE requestId=?", new Object[] { requestId }, 1);
	}

}
