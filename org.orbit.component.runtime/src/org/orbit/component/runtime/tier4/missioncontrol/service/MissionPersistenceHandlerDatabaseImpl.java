package org.orbit.component.runtime.tier4.missioncontrol.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.orbit.component.runtime.model.missioncontrol.Mission;
import org.orbit.component.runtime.model.missioncontrol.MissionVO;
import org.origin.common.jdbc.ConnectionProvider;
import org.origin.common.jdbc.DatabaseUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class MissionPersistenceHandlerDatabaseImpl implements MissionPersistenceHandler {

	protected ConnectionProvider connProvivder;

	public MissionPersistenceHandlerDatabaseImpl(ConnectionProvider connProvider) {
		checkConnectionProvider(connProvider);
		this.connProvivder = connProvider;
	}

	public ConnectionProvider getConnectionProvider() {
		return this.connProvivder;
	}

	public void setConnectionProvider(ConnectionProvider connProvider) {
		this.connProvivder = connProvider;
	}

	protected Connection getConnection() throws SQLException {
		Connection conn = this.connProvivder.getConnection();
		if (conn == null) {
			throw new IllegalStateException("Connection is null.");
		}
		return conn;
	}

	protected void checkConnectionProvider(ConnectionProvider connProvider) {
		if (connProvider == null) {
			throw new IllegalArgumentException("ConnectionProvider is null.");
		}
	}

	protected void handleSQLException(SQLException e) throws IOException {
		throw new IOException(e);
	}

	@Override
	public List<Mission> getMissions(String typeId) throws IOException {
		List<Mission> missions = new ArrayList<Mission>();
		Connection conn = null;
		try {
			conn = getConnection();
			List<MissionVO> missionVOs = MissionTableHandler.getInstance(conn, typeId).getMissions(conn);
			for (MissionVO missionVO : missionVOs) {
				Mission mission = MissionConverter.INSTANCE.toMission(missionVO);
				missions.add(mission);
			}

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return missions;
	}

	@Override
	public List<String> getMissionNames(String typeId) throws IOException {
		List<String> names = new ArrayList<String>();
		Connection conn = null;
		try {
			conn = getConnection();
			List<MissionVO> missionVOs = MissionTableHandler.getInstance(conn, typeId).getMissions(conn);
			for (MissionVO missionVO : missionVOs) {
				names.add(missionVO.getName());
			}

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return names;
	}

	@Override
	public Mission getMission(String typeId, Integer id) throws IOException {
		Mission mission = null;
		Connection conn = null;
		try {
			conn = getConnection();
			MissionVO missionVO = MissionTableHandler.getInstance(conn, typeId).getMission(conn, id);
			if (missionVO != null) {
				mission = MissionConverter.INSTANCE.toMission(missionVO);
			}

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mission;
	}

	@Override
	public Mission getMission(String typeId, String name) throws IOException {
		Mission mission = null;
		Connection conn = null;
		try {
			conn = getConnection();
			MissionVO missionVO = MissionTableHandler.getInstance(conn, typeId).getMission(conn, name);
			if (missionVO != null) {
				mission = MissionConverter.INSTANCE.toMission(missionVO);
			}

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mission;
	}

	@Override
	public boolean nameExists(String typeId, String name) throws IOException {
		boolean exists = false;
		Connection conn = null;
		try {
			conn = getConnection();
			exists = MissionTableHandler.getInstance(conn, typeId).delete(conn, name);

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return exists;
	}

	@Override
	public Mission insert(String typeId, String name) throws IOException {
		Mission mission = null;
		Connection conn = null;
		try {
			conn = getConnection();
			MissionVO missionVO = MissionTableHandler.getInstance(conn, typeId).insert(conn, name);
			if (missionVO != null) {
				mission = MissionConverter.INSTANCE.toMission(missionVO);
			}

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return mission;
	}

	@Override
	public boolean delete(String typeId, Integer id) throws IOException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = MissionTableHandler.getInstance(conn, typeId).delete(conn, id);

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

	@Override
	public boolean delete(String typeId, String name) throws IOException {
		boolean succeed = false;
		Connection conn = null;
		try {
			conn = getConnection();
			succeed = MissionTableHandler.getInstance(conn, typeId).delete(conn, name);

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

}
