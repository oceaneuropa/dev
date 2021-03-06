package org.orbit.infra.runtime.indexes.service;

import static other.orbit.infra.runtime.indexes.service.IndexItemRequestTableHandler.STATUS_PENDING;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexItemRequestVO;
import org.orbit.infra.model.indexes.IndexItemRevisionVO;
import org.orbit.infra.model.indexes.IndexItemVO;
import org.origin.common.jdbc.ConnectionProvider;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;

import other.orbit.infra.runtime.indexes.service.IndexItemDataTableHandler;
import other.orbit.infra.runtime.indexes.service.IndexItemRequestTableHandler;
import other.orbit.infra.runtime.indexes.service.IndexItemRevisionTableHandler;

/**
 * Helper class that provides methods for accessing request, index items and revision data from database.
 * 
 */
public class IndexServiceDatabaseHelper {

	public static IndexServiceDatabaseHelper INSTANCE = new IndexServiceDatabaseHelper();

	protected IndexItemRequestTableHandler requestTableHandler = IndexItemRequestTableHandler.INSTANCE;
	protected IndexItemDataTableHandler dataTableHandler = IndexItemDataTableHandler.INSTANCE;
	protected IndexItemRevisionTableHandler revisionTableHandler = IndexItemRevisionTableHandler.INSTANCE;

	protected boolean debug = true;

	/**
	 * Check whether there are predecessor requests that are active (lastUpdateTime within 10 seconds) and still pending.
	 * 
	 * @param connAware
	 * @param requestId
	 * @return
	 * @throws ServerException
	 */
	public boolean hasActivePendingRequestsInDatabase(ConnectionProvider connAware, Integer requestId) throws ServerException {
		boolean hasActivePendingRequests = false;
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			List<IndexItemRequestVO> activePendingRequests = requestTableHandler.getActivePendingRequests(conn, requestId, 10);
			if (!activePendingRequests.isEmpty()) {
				hasActivePendingRequests = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when getting active pending requests from database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return hasActivePendingRequests;
	}

	/**
	 * Create a pending request in database.
	 * 
	 * @param connAware
	 * @param indexProviderId
	 * @param command
	 * @param argumentsString
	 * @return
	 * @throws ServerException
	 */
	public IndexItemRequestVO createNewRequestInDatabase(ConnectionProvider connAware, String indexProviderId, String command, String argumentsString) throws ServerException {
		IndexItemRequestVO newRequestVO = null;
		Connection conn = null;
		try {
			conn = connAware.getConnection();
			newRequestVO = requestTableHandler.insert(conn, indexProviderId, command, argumentsString, STATUS_PENDING, new Date(), null);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when creating a request for creating an index item in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (newRequestVO == null) {
			throw new ServerException(StatusDTO.RESP_500, "Cannot create request for creating an index item in database.");
		}
		return newRequestVO;
	}

	/**
	 * 
	 * @param connAware
	 * @param requestId
	 * @param date
	 */
	public void updateRequestLastUpdateTimeInDatabase(ConnectionProvider connAware, Integer requestId, Date date) {
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			this.requestTableHandler.updateLastUpdateTime(conn, requestId, date);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	/**
	 * Cancel RequestUpdater and mark the request as completed in database.
	 * 
	 * @param connAware
	 * @param requestId
	 * @param requestUpdaterHandle
	 * @throws ServerException
	 */
	public void completeRequestInDatabase(ConnectionProvider connAware, Integer requestId, ScheduledFuture<?> requestUpdaterHandle) throws ServerException {
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			// cancel the updater
			if (requestUpdaterHandle != null) {
				requestUpdaterHandle.cancel(true);
			}

			// mark the request status as cancelled
			this.requestTableHandler.updateStatusAsCompleted(conn, requestId, new Date());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when updating a request as completed in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	/**
	 * Cancel RequestUpdater and mark the request as cancelled in database.
	 * 
	 * @param connAware
	 * @param requestId
	 * @param requestUpdaterHandle
	 * @throws ServerException
	 */
	public void cancelRequestInDatabase(ConnectionProvider connAware, Integer requestId, ScheduledFuture<?> requestUpdaterHandle) throws ServerException {
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			// cancel the updater
			if (requestUpdaterHandle != null) {
				requestUpdaterHandle.cancel(true);
			}

			// mark the request status as cancelled
			this.requestTableHandler.updateStatusAsCancelled(conn, requestId, new Date());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when updating a request as cancelled in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	/**
	 * Get all index items from database.
	 * 
	 * @param connAware
	 * @return
	 * @throws ServerException
	 */
	public List<IndexItem> getIndexItemsFromDatabase(ConnectionProvider connAware) throws ServerException {
		if (debug) {
			// System.out.println(getClassName() + ".getIndexItemsFromDatabase()");
		}

		List<IndexItem> indexItems = new ArrayList<IndexItem>();

		List<IndexItemVO> indexItemVOs = null;
		Connection conn = null;
		try {
			conn = connAware.getConnection();
			indexItemVOs = this.dataTableHandler.getIndexItems(conn);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when loading index items from database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		if (indexItemVOs != null) {
			for (IndexItemVO indexItemVO : indexItemVOs) {
				IndexItem indexItem = toIndexItem(indexItemVO);
				indexItems.add(indexItem);
			}
		}

		if (debug) {
			// System.out.println(getClassName() + ".getIndexItemsFromDatabase() indexItems.size() is " + indexItems.size());
		}
		return indexItems;
	}

	/**
	 * 
	 * @param connAware
	 * @param indexItemId
	 * @return
	 * @throws ServerException
	 */
	public IndexItemVO getIndexItemFromDatabase(ConnectionProvider connAware, Integer indexItemId) throws ServerException {
		IndexItemVO indexItemVO = null;
		Connection conn = null;
		try {
			conn = connAware.getConnection();
			indexItemVO = this.dataTableHandler.getIndexItem(conn, indexItemId);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when loading index item (indexItemid=" + indexItemId + ") from database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return indexItemVO;
	}

	/**
	 * 
	 * @param indexItemVO
	 * @return
	 */
	public IndexItem toIndexItem(IndexItemVO indexItemVO) {
		Integer indexItemId = indexItemVO.getIndexItemId();
		String indexProviderId = indexItemVO.getIndexProviderId();
		String type = indexItemVO.getType();
		String name = indexItemVO.getName();
		String propertiesString = indexItemVO.getPropertiesString();
		Date createTime = indexItemVO.getCreateTime();
		Date lastUpdateTime = indexItemVO.getLastUpdateTime();

		Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

		IndexItem indexItem = new IndexItem(indexItemId, indexProviderId, type, name, properties, createTime, lastUpdateTime);
		return indexItem;
	}

	/**
	 * Create an index item in database.
	 * 
	 * @param connAware
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param propertiesString
	 * @return
	 * @throws ServerException
	 */
	public IndexItemVO createIndexItemInDatabase(ConnectionProvider connAware, String indexProviderId, String type, String name, String propertiesString) throws ServerException {
		IndexItemVO newIndexItemVO = null;
		Connection conn = null;
		try {
			conn = connAware.getConnection();
			newIndexItemVO = this.dataTableHandler.insert(conn, indexProviderId, type, name, propertiesString, new Date(), null);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when creating an index item in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (newIndexItemVO == null) {
			throw new ServerException(StatusDTO.RESP_500, "Cannot create index item in database.");
		}
		return newIndexItemVO;
	}

	/**
	 * 
	 * @param connAware
	 * @param indexItemId
	 * @return
	 * @throws ServerException
	 */
	public boolean deleteIndexItemInDatababse(ConnectionProvider connAware, Integer indexItemId) throws ServerException {
		Connection conn = null;
		try {
			conn = connAware.getConnection();
			return this.dataTableHandler.delete(conn, indexItemId);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when deleting an index item (indexItemId=" + indexItemId + ") in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	/**
	 * 
	 * @param connAware
	 * @param indexItemId
	 * @param allProperties
	 * @param lastUpdateTime
	 * @return
	 * @throws ServerException
	 */
	public boolean updateIndexItemPropertiesInDatabase(ConnectionProvider connAware, Integer indexItemId, Map<String, Object> allProperties, Date lastUpdateTime) throws ServerException {
		Connection conn = null;
		try {
			String propertiesString = JSONUtil.toJsonString(allProperties);
			conn = connAware.getConnection();
			return this.dataTableHandler.updateProperties(conn, indexItemId, propertiesString, lastUpdateTime);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when updating the properties of an index item (indexItemId=" + indexItemId + ") in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	/**
	 * 
	 * @param connAware
	 * @return
	 * @throws ServerException
	 */
	public int getLatestRevisionIdFromDatabase(ConnectionProvider connAware) throws ServerException {
		if (debug) {
			// System.out.println(getClassName() + ".getLatestRevisionIdFromDatabase()");
		}

		int latestRevisionId = -1;
		Connection conn = null;
		try {
			conn = connAware.getConnection();
			latestRevisionId = this.revisionTableHandler.getMaxRevisionId(conn);
			if (debug) {
				// System.out.println(getClassName() + ".getLatestRevisionIdFromDatabase() latestRevisionId is " + latestRevisionId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when getting latest revision id from database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return latestRevisionId;
	}

	/**
	 * 
	 * @param connAware
	 * @param startRevisionId
	 * @param endRevisionId
	 * @return
	 * @throws ServerException
	 */
	public List<IndexItemRevisionVO> getRevisionsFromDatabase(ConnectionProvider connAware, int startRevisionId, int endRevisionId) throws ServerException {
		if (debug) {
			// System.out.println(getClassName() + ".getRevisionsFromDatabase()");
		}

		List<IndexItemRevisionVO> revisionVOs = null;
		Connection conn = null;
		try {
			conn = connAware.getConnection();
			revisionVOs = this.revisionTableHandler.getRevisions(conn, startRevisionId, endRevisionId);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when getting revisions from database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (revisionVOs == null) {
			revisionVOs = Collections.emptyList();
		}
		return revisionVOs;
	}

	/**
	 * Create a revision in database.
	 * 
	 * @param connAware
	 * @param indexProviderId
	 * @param command
	 * @param commandArguments
	 * @param undoCommand
	 * @param undoCommandArguments
	 * @return
	 * @throws ServerException
	 */
	public IndexItemRevisionVO createRevisionInDatabase(ConnectionProvider connAware, String indexProviderId, String command, Map<String, Object> commandArguments, String undoCommand, Map<String, Object> undoCommandArguments) throws ServerException {
		IndexItemRevisionVO newRevisionVO = null;
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			String commandArgumentsString = JSONUtil.toJsonString(commandArguments);
			String undoCommandArgumentsString = JSONUtil.toJsonString(undoCommandArguments);

			Date updateTime = new Date();
			newRevisionVO = this.revisionTableHandler.insert(conn, indexProviderId, command, commandArgumentsString, undoCommand, undoCommandArgumentsString, updateTime);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when creating a revision in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (newRevisionVO == null) {
			throw new ServerException(StatusDTO.RESP_500, "Cannot create revision in database.");
		}
		return newRevisionVO;
	}

}
