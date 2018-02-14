package org.orbit.component.runtime.tier4.missioncontrol.service.persistence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.orbit.component.model.tier4.mission.rto.Mission;
import org.orbit.component.model.tier4.mission.rto.MissionConverter;
import org.orbit.component.model.tier4.mission.rto.MissionVO;
import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.jdbc.DatabaseUtil;

public class MissionPersistenceHandlerDatabaseImpl implements MissionPersistenceHandler {

	protected ConnectionAware connAware;

	public MissionPersistenceHandlerDatabaseImpl(ConnectionAware connAware) {
		checkConnectionAware(connAware);
		this.connAware = connAware;
	}

	public ConnectionAware getConnectionAware() {
		return this.connAware;
	}

	public void setConnectionAware(ConnectionAware connAware) {
		this.connAware = connAware;
	}

	protected Connection getConnection() {
		Connection conn = this.connAware.getConnection();
		if (conn == null) {
			throw new IllegalStateException("Connection is null.");
		}
		return conn;
	}

	protected void checkConnectionAware(ConnectionAware connAware) {
		if (connAware == null) {
			throw new IllegalArgumentException("Connection is null.");
		}
	}

	protected void handleSQLException(SQLException e) throws IOException {
		throw new IOException(e);
	}

	@Override
	public List<Mission> getMissions(String typeId) throws IOException {
		List<Mission> missions = new ArrayList<Mission>();
		Connection conn = getConnection();
		try {
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
		Connection conn = getConnection();
		try {
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
		Connection conn = getConnection();
		try {
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
		Connection conn = getConnection();
		try {
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
		Connection conn = getConnection();
		try {
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
		Connection conn = getConnection();
		try {
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
		Connection conn = getConnection();
		try {
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
		Connection conn = getConnection();
		try {
			succeed = MissionTableHandler.getInstance(conn, typeId).delete(conn, name);

		} catch (SQLException e) {
			handleSQLException(e);

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return succeed;
	}

}
