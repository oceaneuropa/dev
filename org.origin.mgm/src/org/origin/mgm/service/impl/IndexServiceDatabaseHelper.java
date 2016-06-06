package org.origin.mgm.service.impl;

import static org.origin.mgm.persistence.IndexItemRequestTableHandler.STATUS_PENDING;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.json.JSONUtil;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.model.vo.IndexItemDataVO;
import org.origin.mgm.model.vo.IndexItemRequestVO;
import org.origin.mgm.model.vo.IndexItemRevisionVO;
import org.origin.mgm.persistence.IndexItemDataTableHandler;
import org.origin.mgm.persistence.IndexItemRequestTableHandler;
import org.origin.mgm.persistence.IndexItemRevisionTableHandler;
import org.origin.mgm.service.IndexServiceConstants;

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
	 * @throws IndexServiceException
	 */
	public boolean hasActivePendingRequestsInDatabase(ConnectionAware connAware, Integer requestId) throws IndexServiceException {
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
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when getting active pending requests from database. Message: " + e.getMessage());
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
	 * @throws IndexServiceException
	 */
	public IndexItemRequestVO createNewRequestInDatabase(ConnectionAware connAware, String indexProviderId, String command, String argumentsString) throws IndexServiceException {
		IndexItemRequestVO newRequestVO = null;
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			newRequestVO = requestTableHandler.insert(conn, indexProviderId, command, argumentsString, STATUS_PENDING, new Date(), null);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when creating a request for creating an index item in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (newRequestVO == null) {
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, "Cannot create request for creating an index item in database.");
		}
		return newRequestVO;
	}

	/**
	 * 
	 * @param connAware
	 * @param requestId
	 * @param date
	 */
	public void updateRequestLastUpdateTimeInDatabase(ConnectionAware connAware, Integer requestId, Date date) {
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			requestTableHandler.updateLastUpdateTime(conn, requestId, date);
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
	 * @throws IndexServiceException
	 */
	public void completeRequestInDatabase(ConnectionAware connAware, Integer requestId, ScheduledFuture<?> requestUpdaterHandle) throws IndexServiceException {
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			// cancel the updater
			if (requestUpdaterHandle != null) {
				requestUpdaterHandle.cancel(true);
			}

			// mark the request status as cancelled
			requestTableHandler.updateStatusAsCompleted(conn, requestId, new Date());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when updating a request as completed in database. Message: " + e.getMessage());
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
	 * @throws IndexServiceException
	 */
	public void cancelRequestInDatabase(ConnectionAware connAware, Integer requestId, ScheduledFuture<?> requestUpdaterHandle) throws IndexServiceException {
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			// cancel the updater
			if (requestUpdaterHandle != null) {
				requestUpdaterHandle.cancel(true);
			}

			// mark the request status as cancelled
			requestTableHandler.updateStatusAsCancelled(conn, requestId, new Date());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when updating a request as cancelled in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	/**
	 * Get all index items from database.
	 * 
	 * @param connAware
	 * @return
	 * @throws IndexServiceException
	 */
	public List<IndexItem> getIndexItemsFromDatabase(ConnectionAware connAware) throws IndexServiceException {
		if (debug) {
			// System.out.println(getClassName() + ".getIndexItemsFromDatabase()");
		}

		List<IndexItem> indexItems = new ArrayList<IndexItem>();

		List<IndexItemDataVO> indexItemVOs = null;

		Connection conn = null;
		try {
			conn = connAware.getConnection();

			indexItemVOs = dataTableHandler.getIndexItems(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when loading index items from database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		if (indexItemVOs != null) {
			for (IndexItemDataVO indexItemVO : indexItemVOs) {
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
	 * @param indexItemVO
	 * @return
	 */
	private IndexItem toIndexItem(IndexItemDataVO indexItemVO) {
		Integer indexItemId = indexItemVO.getIndexItemId();
		String indexProviderId = indexItemVO.getIndexProviderId();
		String namespace = indexItemVO.getNamespace();
		String name = indexItemVO.getName();
		String propertiesString = indexItemVO.getPropertiesString();
		Date createTime = indexItemVO.getCreateTime();
		Date lastUpdateTime = indexItemVO.getLastUpdateTime();

		Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

		IndexItem indexItem = new IndexItem(indexItemId, indexProviderId, namespace, name, properties, createTime, lastUpdateTime);
		return indexItem;
	}

	/**
	 * Create an index item in database.
	 * 
	 * @param connAware
	 * @param indexProviderId
	 * @param namespace
	 * @param name
	 * @param propertiesString
	 * @return
	 * @throws IndexServiceException
	 */
	public IndexItemDataVO createIndexItemInDatabase(ConnectionAware connAware, String indexProviderId, String namespace, String name, String propertiesString) throws IndexServiceException {
		IndexItemDataVO newIndexItemVO = null;
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			newIndexItemVO = dataTableHandler.insert(conn, indexProviderId, namespace, name, propertiesString, new Date(), null);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when creating an index item in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (newIndexItemVO == null) {
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, "Cannot create index item in database.");
		}
		return newIndexItemVO;
	}

	/**
	 * 
	 * @param connAware
	 * @return
	 * @throws IndexServiceException
	 */
	public int getLatestRevisionIdFromDatabase(ConnectionAware connAware) throws IndexServiceException {
		if (debug) {
			// System.out.println(getClassName() + ".getLatestRevisionIdFromDatabase()");
		}

		int latestRevisionId = -1;
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			latestRevisionId = revisionTableHandler.getMaxRevisionId(conn);
			if (debug) {
				// System.out.println(getClassName() + ".getLatestRevisionIdFromDatabase() latestRevisionId is " + latestRevisionId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when getting latest revision id from database. Message: " + e.getMessage());
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
	 * @throws IndexServiceException
	 */
	public List<IndexItemRevisionVO> getRevisionsFromDatabase(ConnectionAware connAware, int startRevisionId, int endRevisionId) throws IndexServiceException {
		if (debug) {
			// System.out.println(getClassName() + ".getRevisionsFromDatabase()");
		}

		List<IndexItemRevisionVO> revisionVOs = null;
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			revisionVOs = revisionTableHandler.getRevisions(conn, startRevisionId, endRevisionId);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when getting revisions from database. Message: " + e.getMessage());
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
	 * @throws IndexServiceException
	 */
	public IndexItemRevisionVO createRevisionInDatabase(ConnectionAware connAware, String indexProviderId, String command, Map<String, Object> commandArguments, String undoCommand, Map<String, Object> undoCommandArguments) throws IndexServiceException {
		IndexItemRevisionVO newRevisionVO = null;
		Connection conn = null;
		try {
			conn = connAware.getConnection();

			String commandArgumentsString = JSONUtil.toJsonString(commandArguments);
			String undoCommandArgumentsString = JSONUtil.toJsonString(undoCommandArguments);

			Date updateTime = new Date();
			newRevisionVO = revisionTableHandler.insert(conn, indexProviderId, "create_index_item", commandArgumentsString, "delete_index_item", undoCommandArgumentsString, updateTime);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when creating a revision in database. Message: " + e.getMessage());
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		if (newRevisionVO == null) {
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, "Cannot create revision in database.");
		}
		return newRevisionVO;
	}

}
