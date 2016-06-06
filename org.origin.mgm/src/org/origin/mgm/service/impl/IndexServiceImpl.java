package org.origin.mgm.service.impl;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.namespace.QName;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.CommandStack;
import org.origin.common.command.EditingDomain;
import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.json.JSONUtil;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.util.CompareUtil;
import org.origin.common.util.DateUtil;
import org.origin.common.util.IntegerComparator;
import org.origin.common.util.LifecycleAware;
import org.origin.common.util.PropertyUtil;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.model.vo.IndexItemDataVO;
import org.origin.mgm.model.vo.IndexItemRequestVO;
import org.origin.mgm.model.vo.IndexItemRevisionVO;
import org.origin.mgm.persistence.IndexItemDataTableHandler;
import org.origin.mgm.persistence.IndexItemRequestTableHandler;
import org.origin.mgm.persistence.IndexItemRevisionTableHandler;
import org.origin.mgm.service.IndexService;
import org.origin.mgm.service.IndexServiceActionAware;
import org.origin.mgm.service.IndexServiceConfiguration;
import org.origin.mgm.service.IndexServiceConstants;
import org.origin.mgm.service.IndexServiceListener;
import org.origin.mgm.service.IndexServiceListenerSupport;
import org.origin.mgm.service.IndexServiceUpdatable;
import org.origin.mgm.service.command.RevisionCommand;
import org.osgi.framework.BundleContext;

public class IndexServiceImpl implements IndexService, IndexServiceUpdatable, ConnectionAware, LifecycleAware, IndexServiceActionAware {

	protected BundleContext bundleContext;
	protected DatabaseIndexServiceConfiguration indexServiceConfig;
	protected IndexServiceListenerSupport listenerSupport = new IndexServiceListenerSupport();

	protected IndexItemRequestTableHandler requestTableHandler = IndexItemRequestTableHandler.INSTANCE;
	protected IndexItemDataTableHandler dataTableHandler = IndexItemDataTableHandler.INSTANCE;
	protected IndexItemRevisionTableHandler revisionTableHandler = IndexItemRevisionTableHandler.INSTANCE;

	protected ScheduledExecutorService broadcastScheduler = Executors.newScheduledThreadPool(1);
	protected ScheduledFuture<?> broadcastHandle;

	protected ScheduledExecutorService synchronizationScheduler = Executors.newScheduledThreadPool(1);
	protected ScheduledFuture<?> synchronizeHandle;

	protected ScheduledExecutorService validationScheduler = Executors.newScheduledThreadPool(1);
	protected ScheduledFuture<?> validationHandle;

	protected ScheduledExecutorService requestUpdatorScheduler = Executors.newScheduledThreadPool(1);

	protected IndexServicePrinter indexServicePrinter;

	protected ReadWriteLock indexItemsRWLock = new ReentrantReadWriteLock();
	protected volatile boolean isSynchronizing = false;
	protected volatile boolean isValidating = false;

	protected List<IndexItem> cachedIndexItems = new ArrayList<IndexItem>();
	protected Integer cachedRevisionId = 0;

	protected CommandStack revisionCommandStack;
	protected boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 * @param indexServiceConfig
	 */
	public IndexServiceImpl(BundleContext bundleContext, DatabaseIndexServiceConfiguration indexServiceConfig) {
		this.bundleContext = bundleContext;
		this.indexServiceConfig = indexServiceConfig;
	}

	protected String getClassName() {
		return this.getClass().getSimpleName();
	}

	public IndexServiceConfiguration getConfiguration() {
		return this.indexServiceConfig;
	}

	protected IndexServiceDatabaseHelper getDatabaseHelper() {
		return IndexServiceDatabaseHelper.INSTANCE;
	}

	/** implement ConnectionAware interface */
	@Override
	public Connection getConnection() {
		return this.indexServiceConfig.getConnection();
	}

	// ------------------------------------------------------------------------------------------------------------
	// Methods for actions
	// ------------------------------------------------------------------------------------------------------------
	/** implement IndexServiceActionAware interface */
	@Override
	public boolean isActionSupported(String action) {
		if ("sync".equalsIgnoreCase(action)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onAction(String action, Map<String, Object> parameters) {
		if ("sync".equalsIgnoreCase(action)) {
			try {
				synchronize();
			} catch (IndexServiceException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------------------------
	// Methods accessing config properties
	// ------------------------------------------------------------------------------------------------------------
	protected String getNodeName() {
		return PropertyUtil.getString(this.indexServiceConfig.getProperties(), IndexServiceConstants.CONFIG_PROP_NODE_NAME, null);
	}

	protected String getNodeUrl() {
		return PropertyUtil.getString(this.indexServiceConfig.getProperties(), IndexServiceConstants.CONFIG_PROP_NODE_URL, null);
	}

	protected String getNodeContextRoot() {
		return PropertyUtil.getString(this.indexServiceConfig.getProperties(), IndexServiceConstants.CONFIG_PROP_NODE_CONTEXT_ROOT, null);
	}

	protected String getNodeUsername() {
		return PropertyUtil.getString(this.indexServiceConfig.getProperties(), IndexServiceConstants.CONFIG_PROP_NODE_USERNAME, null);
	}

	protected String getNodePassword() {
		return PropertyUtil.getString(this.indexServiceConfig.getProperties(), IndexServiceConstants.CONFIG_PROP_NODE_PASSWORD, null);
	}

	protected Integer getHeartbeatExpireTime() {
		return PropertyUtil.getInt(this.indexServiceConfig.getProperties(), IndexServiceConstants.CONFIG_PROP_HEARTBEAT_EXPIRE_TIME, IndexServiceConstants.DEFAULT_HEARTBEAT_EXPIRE_TIME);
	}

	// ------------------------------------------------------------------------------------------------------------
	// Methods for lifecycle
	// ------------------------------------------------------------------------------------------------------------
	/** implement LifecycleAware interface */
	@Override
	public synchronized void start() {
		if (debug) {
			System.out.println(getClassName() + ".start()");
		}

		// Initialize the command stack
		this.revisionCommandStack = EditingDomain.getEditingDomain(IndexServiceConstants.EDITING_DOMAIN).getCommandStack(this);

		// Synchronize once when the index service is started, so that the cachedIndexItems and cachedRevisionId can be initialized.
		try {
			synchronize();
		} catch (IndexServiceException e) {
			e.printStackTrace();
		}

		// Check/register index item for the current index service node.
		try {
			// Get configuration of the index service node from the config.ini file
			String nodeName = getNodeName();
			String nodeUrl = getNodeUrl();
			String nodeContextRoot = getNodeContextRoot();
			String nodeUsername = getNodeUsername();
			String nodePassword = getNodePassword();

			IndexItem matchedIndexItem = null;
			List<IndexItem> cachedIndexItems = getIndexItems();
			for (IndexItem cachedIndexItem : cachedIndexItems) {
				if (IndexServiceConstants.INDEX_PROVIDER_ID.equals(cachedIndexItem.getIndexProviderId()) && IndexServiceConstants.NAMESPACE.equals(cachedIndexItem.getNamespace())) {
					if (nodeName != null && nodeName.equals(cachedIndexItem.getName())) {
						matchedIndexItem = cachedIndexItem;
						break;
					}
				}
			}

			if (matchedIndexItem == null) {
				// Add an index item for the current index service node and put the url, contextRoot, username, password for accessing the node as the
				// properties of the index item.
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(IndexServiceConstants.IDX_PROP_URL, nodeUrl);
				properties.put(IndexServiceConstants.IDX_PROP_CONTEXT_ROOT, nodeContextRoot);
				properties.put(IndexServiceConstants.IDX_PROP_USERNAME, nodeUsername);
				properties.put(IndexServiceConstants.IDX_PROP_PASSWORD, nodePassword);

				addIndexItem(IndexServiceConstants.INDEX_PROVIDER_ID, IndexServiceConstants.NAMESPACE, nodeName, properties);

			} else {
				// TODO:
				// Check properties of the index item and update the properties if changed.

			}
		} catch (IndexServiceException e) {
			e.printStackTrace();
		}

		scheduleBroadcaster();
		scheduleSynchronizer();
		scheduleValidator();

		if (this.indexServicePrinter != null) {
			this.indexServicePrinter.stop();
			this.indexServicePrinter = null;
		}
		this.indexServicePrinter = new IndexServicePrinter(this, this.indexServiceConfig.getProperties());
		this.indexServicePrinter.start();

		// Register IndexService to OSGi.Officially announce the service after everything is ready.
		OSGiServiceUtil.register(this.bundleContext, IndexService.class.getName(), this);
	}

	@Override
	public synchronized void stop() {
		if (debug) {
			System.out.println(getClassName() + ".stop()");
		}

		// Unregister IndexService from OSGi
		OSGiServiceUtil.unregister(IndexService.class.getName(), this);

		unscheduleBroadcaster();
		unscheduleSynchronizer();
		unscheduleValidator();

		if (this.indexServicePrinter != null) {
			this.indexServicePrinter.stop();
			this.indexServicePrinter = null;
		}

		// dispose the command stack
		EditingDomain.getEditingDomain(IndexServiceConstants.EDITING_DOMAIN).disposeCommandStack(this);
		this.revisionCommandStack = null;
	}

	protected void scheduleBroadcaster() {
		// scheduled runner to broadcast to other index service nodes.
		Runnable broadcastRunner = new Runnable() {
			@Override
			public void run() {
				try {
					broadcast();
				} catch (IndexServiceException e) {
					e.printStackTrace();
				}
			}
		};
		this.broadcastHandle = broadcastScheduler.scheduleAtFixedRate(broadcastRunner, 0, 10, SECONDS);
	}

	protected void unscheduleBroadcaster() {
		// Stop broadcasting to other index service nodes.
		if (this.broadcastHandle != null && !this.broadcastHandle.isCancelled()) {
			this.broadcastHandle.cancel(true);
		}
	}

	protected void scheduleSynchronizer() {
		// scheduled runner to synchronize cached data
		Runnable indexItemsSynchronizer = new Runnable() {
			@Override
			public void run() {
				try {
					synchronize();
				} catch (IndexServiceException e) {
					e.printStackTrace();
				}
			}
		};
		// synchronize index items with database.
		this.synchronizeHandle = synchronizationScheduler.scheduleAtFixedRate(indexItemsSynchronizer, 0, 5, SECONDS);
	}

	protected void unscheduleSynchronizer() {
		// Stop synchronizing index items
		if (this.synchronizeHandle != null && !this.synchronizeHandle.isCancelled()) {
			this.synchronizeHandle.cancel(true);
		}
	}

	protected void scheduleValidator() {
		// scheduled runner to validate cached data
		Runnable indexItemsValidator = new Runnable() {
			@Override
			public void run() {
				try {
					validate();
				} catch (IndexServiceException e) {
					e.printStackTrace();
				}
			}
		};
		// validate cached index items.
		this.validationHandle = validationScheduler.scheduleAtFixedRate(indexItemsValidator, 21, 21, SECONDS);
	}

	protected void unscheduleValidator() {
		if (this.validationHandle != null && !this.validationHandle.isCancelled()) {
			this.validationHandle.cancel(true);
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	// Methods for broadcasting to other index service nodes
	// ------------------------------------------------------------------------------------------------------------
	public synchronized void broadcast() throws IndexServiceException {
		try {
			String nodeName = getNodeName();

			List<IndexItem> indexItems = getIndexItemsByNamespace(IndexServiceConstants.NAMESPACE);

			for (IndexItem indexItem : indexItems) {
				String currNodeName = indexItem.getName();
				String url = (String) indexItem.getProperty(IndexServiceConstants.IDX_PROP_URL);
				String contextRoot = (String) indexItem.getProperty(IndexServiceConstants.IDX_PROP_CONTEXT_ROOT);
				String username = (String) indexItem.getProperty(IndexServiceConstants.IDX_PROP_USERNAME);
				String password = (String) indexItem.getProperty(IndexServiceConstants.IDX_PROP_PASSWORD);
				Date lastHeartbeatTime = (Date) indexItem.getRuntimeProperty(IndexServiceConstants.IDX_PROP_LAST_HEARTBEAT_TIME);

				// gets a calendar using the default time zone and locale.
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.SECOND, -getHeartbeatExpireTime());
				Date heartbeatTimeoutTime = calendar.getTime();

				if (nodeName != null && nodeName.equals(currNodeName)) {
					System.err.println("Ignore notifying current index service node '" + currNodeName + "'.");
					continue;
				}

				// Last heartbeat happened 30 seconds ago. Index service node is considered as not running.
				if (lastHeartbeatTime == null || lastHeartbeatTime.compareTo(heartbeatTimeoutTime) <= 0) {
					System.err.println("Index service node '" + currNodeName + "' is not running (last heartbeat time is " + lastHeartbeatTime + " ).");
					continue;
				}

				org.origin.mgm.client.api.IndexServiceConfiguration config = contextRoot == null ? new org.origin.mgm.client.api.IndexServiceConfiguration(url, username, password) : new org.origin.mgm.client.api.IndexServiceConfiguration(url, contextRoot, username, password);
				final org.origin.mgm.client.api.IndexService indexService = org.origin.mgm.client.api.IndexService.newInstance(config);
				try {
					indexService.action("sync", null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IndexServiceException e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	// Methods for synchronization of the cached data
	// ------------------------------------------------------------------------------------------------------------
	public synchronized void synchronize() throws IndexServiceException {
		if (debug) {
			System.out.println(getClassName() + ".synchronize()");
			System.out.println("\t\t\tsynchronize() current cachedRevisionId is " + this.cachedRevisionId);
		}

		CommandContext context = new CommandContext();
		context.adapt(ConnectionAware.class, this);
		context.adapt(IndexService.class, this);

		if (cachedRevisionId <= 0) {
			// data is not cached
			reloadCache();

		} else {
			int latestRevisionId = getDatabaseHelper().getLatestRevisionIdFromDatabase(this);
			if (debug) {
				// System.out.println(getClassName() + ".synchronize() latestRevisionId is " + latestRevisionId);
			}

			// Goal:
			// 1. Every revision contains a atomic action (with command name and arguments)
			// 2. Every revision contains a undo action (with undo command name and undo arguments) that can undo the changes made by the action.

			// Need to do:
			// --- the set of commands and arguments need to be defined.
			// --- a Command model (like a patch) for each action (create, update, delete index item) need to be defined.
			// --- a Command stack is needed for executing/undo/redo the commands.

			// data have already been cached
			if (cachedRevisionId == latestRevisionId) {
				// The cached data matches the latest revision. No need to append or revert revisions.
				if (debug) {
					System.out.println("\t\t\tsynchronize() cached data are synchronized with database (cachedRevisionId=" + cachedRevisionId + ").");
				}

			} else if (cachedRevisionId < latestRevisionId) {
				// The cached data is left behind the latest revision.
				// Append the revisions [cachedRevisionId + 1, maxRevisionId] to the cached data.
				appendRevisions(context, cachedRevisionId + 1, latestRevisionId);

			} else if (cachedRevisionId > latestRevisionId) {
				// The cached data is ahead of the latest revision. The revision have been reverted in the database.
				// Revert the cached data to the latest revision.
				revertToRevision(context, latestRevisionId);

				// if the cached data is still ahead of the latest revision and there is no more command for undo, reload all cached data.
				if (this.cachedRevisionId > latestRevisionId && this.revisionCommandStack.getUndoableSize() == 0) {
					reloadCache();
				}
			}
		}
	}

	/**
	 * 
	 * @throws IndexServiceException
	 */
	protected synchronized void reloadCache() throws IndexServiceException {
		if (debug) {
			System.out.println(getClassName() + ".reloadCache()");
		}

		// --------------------------------------------------------------------
		// load latest revision id and index items from database
		// --------------------------------------------------------------------
		int latestRevisionIdThen = getDatabaseHelper().getLatestRevisionIdFromDatabase(this);
		List<IndexItem> indexItems = getDatabaseHelper().getIndexItemsFromDatabase(this);
		int latestRevisionIdNow = getDatabaseHelper().getLatestRevisionIdFromDatabase(this);

		int totalWaitingTime = 0;
		// if new data comes in during the reloading, retry again to get all index items.
		while (latestRevisionIdNow != latestRevisionIdThen) {
			indexItems = null;

			// retry 10 times, if new data still comes in after each retry, do not retry again. Just use the data we got.
			if (totalWaitingTime > 10 * 1000) {
				System.err.println(getClassName() + ".reloadCache() index items are being updated during loading of index items from database. After retried for 10 seconds, current reload is cancelled.");
				return;
			}

			// --------------------------------------------------------------------
			// wait for 1 second before retry again
			// --------------------------------------------------------------------
			try {
				if (debug) {
					System.out.println(getClassName() + ".reloadCache() index items are being updated from database. Wait for 1 second...");
				}
				Thread.sleep(1000);
				totalWaitingTime += 1000;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// --------------------------------------------------------------------
			// load latest revision id and index items from database (repeat above)
			// --------------------------------------------------------------------
			latestRevisionIdThen = getDatabaseHelper().getLatestRevisionIdFromDatabase(this);
			indexItems = getDatabaseHelper().getIndexItemsFromDatabase(this);
			latestRevisionIdNow = getDatabaseHelper().getLatestRevisionIdFromDatabase(this);
		}

		if (indexItems == null) {
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, " Cannot load index items from database.");
		}

		indexItemsRWLock.writeLock().lock();
		try {
			// Update cached data with index items loaded from database
			this.cachedIndexItems.clear();
			this.cachedIndexItems.addAll(indexItems);

			// Update the cached revision id
			this.cachedRevisionId = latestRevisionIdNow;

			if (debug) {
				System.out.println(getClassName() + ".reloadCache() cachedRevisionId is reloaded as " + this.cachedRevisionId);
			}
		} finally {
			indexItemsRWLock.writeLock().unlock();
		}
	}

	/**
	 * Append the revisions from start revision (inclusive) to end revision (inclusive) to the cache.
	 * 
	 * @param context
	 *            context to execute revision command.
	 * @param startRevisionId
	 *            revision id to start with, inclusive
	 * @param endRevisionId
	 *            revision id to end with, inclusive
	 * @throws IndexServiceException
	 */
	protected synchronized void appendRevisions(CommandContext context, int startRevisionId, int endRevisionId) throws IndexServiceException {
		if (debug) {
			System.out.println(getClassName() + ".appendRevisions()");
			System.out.println("\t\t\tappendRevisions() startRevisionId=" + startRevisionId + ", endRevisionId=" + endRevisionId);
		}

		List<IndexItemRevisionVO> revisionVOs = getDatabaseHelper().getRevisionsFromDatabase(this, startRevisionId, endRevisionId);

		indexItemsRWLock.writeLock().lock();
		try {
			for (IndexItemRevisionVO revisionVO : revisionVOs) {
				Integer revisionId = revisionVO.getRevisionId();
				String command = revisionVO.getCommand();
				String argumentsString = revisionVO.getArguments();
				String undoCommand = revisionVO.getUndoCommand();
				String undoArgumentsString = revisionVO.getUndoArguments();

				RevisionCommand revisionCommand = new RevisionCommand(this, revisionId, command, argumentsString, undoCommand, undoArgumentsString);
				try {
					// Create/Delete/Update index item with revision command
					this.revisionCommandStack.execute(context, revisionCommand);

					// Update the cached revision id
					this.cachedRevisionId = revisionId;

				} catch (CommandException e) {
					e.printStackTrace();
					throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when a applying a revision with a synchronization command. Message: " + e.getMessage());
				}
			}

			if (debug) {
				System.out.println("\t\t\tappendRevisions(" + startRevisionId + ", " + endRevisionId + ") cachedRevisionId is appended to " + this.cachedRevisionId);
			}
		} finally {
			indexItemsRWLock.writeLock().unlock();
		}
	}

	/**
	 * Revert the cache to the specified revision id.
	 * 
	 * @param context
	 *            context to execute revision command.
	 * @param revisionId
	 *            revision id to revert to, inclusive
	 * @throws IndexServiceException
	 */
	protected synchronized void revertToRevision(CommandContext context, int toRevisionId) throws IndexServiceException {
		if (debug) {
			System.out.println(getClassName() + ".revertToRevision()");
			System.out.println("\t\t\trevertToRevision() toRevisionId=" + toRevisionId);
		}

		indexItemsRWLock.writeLock().lock();
		try {
			while (this.revisionCommandStack.canUndo()) {
				if (this.cachedRevisionId <= toRevisionId) {
					// the cached data is reverted to the toRevisionId.
					break;
				}

				AbstractCommand command = this.revisionCommandStack.peekUndoCommand();

				if (!(command instanceof RevisionCommand)) {
					throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, "Cannot revert a non-revision command '" + command.getClass().getName() + "'. A non-revision command is not expected in the command stack.");
				}

				int revisionId = ((RevisionCommand) command).getRevisionId();

				// do not undo a command if it is for a earlier revision than the toRevisionId.
				if (revisionId < toRevisionId) {
					break;
				}

				try {
					this.revisionCommandStack.undo(context);
					this.cachedRevisionId = revisionId;

				} catch (CommandException e) {
					e.printStackTrace();
					throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, e.getClass().getName() + " occurs when reverting a revision with a synchronization command. Message: " + e.getMessage());
				}
			}

			if (debug) {
				System.out.println("\t\t\trevertToRevision(" + toRevisionId + ") cachedRevisionId is reverted to " + this.cachedRevisionId);
			}
		} finally {
			indexItemsRWLock.writeLock().unlock();
		}
	}

	public void validate() throws IndexServiceException {
		if (debug) {
			System.out.println(getClassName() + ".validate()");
		}

		long totalWaitingTime = 0;
		while (isValidating) {
			if (totalWaitingTime > 10 * 1000) {
				// it should not take long to validate all index items. A total waiting of 10 seconds should be long enough.
				System.err.println("\t\t\tvalidate() index items are being validated. After waiting for 10 seconds, current validation is cancelled.");
				return;
			}
			try {
				Thread.sleep(1000);
				totalWaitingTime += 1000;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		isValidating = true;
		try {
			int cachedRevisionIdThen = -1;
			int latestRevisionIdThen = -1;
			List<IndexItem> indexItemsSnapshot = new ArrayList<IndexItem>();
			Map<IndexItem, IndexItem> snapshotToCachedIndexItemMap = new LinkedHashMap<IndexItem, IndexItem>();
			List<IndexItem> indexItemsFromDatabase = null;
			int latestRevisionIdNow = -1;

			// --------------------------------------------------------------------
			// load latest revision id and index items from database
			// clone cached index items
			// --------------------------------------------------------------------
			indexItemsRWLock.readLock().lock();
			try {
				cachedRevisionIdThen = this.cachedRevisionId;
				latestRevisionIdThen = getDatabaseHelper().getLatestRevisionIdFromDatabase(this);
				if (cachedRevisionIdThen != latestRevisionIdThen) {
					System.err.println("\t\t\tvalidate() cached data doesn't have the latest revision. Current validation is cancelled.");
					return;
				}
				for (IndexItem cachedIndexItem : this.cachedIndexItems) {
					IndexItem indexItemSnapshot = cachedIndexItem.clone();
					indexItemsSnapshot.add(indexItemSnapshot);
					snapshotToCachedIndexItemMap.put(indexItemSnapshot, cachedIndexItem);
				}
				indexItemsFromDatabase = getDatabaseHelper().getIndexItemsFromDatabase(this);
				latestRevisionIdNow = getDatabaseHelper().getLatestRevisionIdFromDatabase(this);

			} finally {
				indexItemsRWLock.readLock().unlock();
			}

			long totalRetryTime = 0;
			while (latestRevisionIdNow != latestRevisionIdThen) {
				cachedRevisionIdThen = -1;
				latestRevisionIdThen = -1;
				indexItemsSnapshot = new ArrayList<IndexItem>();
				snapshotToCachedIndexItemMap.clear();
				indexItemsFromDatabase = null;
				latestRevisionIdNow = -1;

				if (totalRetryTime > 10 * 1000) {
					System.err.println("\t\t\tvalidate() index items are being updated during loading of index items from database. After retried for 10 seconds, current validation is cancelled.");
					return;
				}

				try {
					if (debug) {
						System.out.println("\t\t\tvalidate() index items are being updated from database. Wait for 1 second...");
					}
					Thread.sleep(1000);
					totalRetryTime += 1000;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// --------------------------------------------------------------------
				// load latest revision id and index items from database
				// clone cached index items
				// --------------------------------------------------------------------
				indexItemsRWLock.readLock().lock();
				try {
					cachedRevisionIdThen = this.cachedRevisionId;
					latestRevisionIdThen = getDatabaseHelper().getLatestRevisionIdFromDatabase(this);
					if (cachedRevisionIdThen != latestRevisionIdThen) {
						System.err.println("\t\t\tvalidate() cached data doesn't have the latest revision. Current validation is cancelled.");
						return;
					}
					for (IndexItem cachedIndexItem : this.cachedIndexItems) {
						IndexItem indexItemSnapshot = cachedIndexItem.clone();
						indexItemsSnapshot.add(indexItemSnapshot);
						snapshotToCachedIndexItemMap.put(indexItemSnapshot, cachedIndexItem);
					}
					indexItemsFromDatabase = getDatabaseHelper().getIndexItemsFromDatabase(this);
					latestRevisionIdNow = getDatabaseHelper().getLatestRevisionIdFromDatabase(this);

				} finally {
					indexItemsRWLock.readLock().unlock();
				}
			}

			// validate cachedIndexItemsSnapshot against indexItemsFromDatabase.
			if (indexItemsSnapshot != null && indexItemsFromDatabase != null) {
				List<Integer> indexItemIds = new ArrayList<Integer>();
				for (IndexItem indexItemSnapshot : indexItemsSnapshot) {
					if (!indexItemIds.contains(indexItemSnapshot.getIndexItemId())) {
						indexItemIds.add(indexItemSnapshot.getIndexItemId());
					}
				}
				for (IndexItem indexItemFromDatabase : indexItemsFromDatabase) {
					if (!indexItemIds.contains(indexItemFromDatabase.getIndexItemId())) {
						indexItemIds.add(indexItemFromDatabase.getIndexItemId());
					}
				}
				IntegerComparator.sort(indexItemIds);

				Map<Integer, IndexItem> cachedIndexItemsMap = new HashMap<Integer, IndexItem>();
				Map<Integer, IndexItem> databaseIndexItemsMap = new HashMap<Integer, IndexItem>();

				for (IndexItem cachedIndexItemSnapshot : indexItemsSnapshot) {
					cachedIndexItemsMap.put(cachedIndexItemSnapshot.getIndexItemId(), cachedIndexItemSnapshot);
				}
				for (IndexItem indexItemFromDatabase : indexItemsFromDatabase) {
					databaseIndexItemsMap.put(indexItemFromDatabase.getIndexItemId(), indexItemFromDatabase);
				}

				List<Integer> outSyncedIndexItemIds = new ArrayList<Integer>();
				List<IndexItem> indexItemsToAdd = new ArrayList<IndexItem>();
				List<IndexItem> indexItemsToRemove = new ArrayList<IndexItem>();
				Map<IndexItem, IndexItem> indexItemsToUpdate = new LinkedHashMap<IndexItem, IndexItem>();

				for (Integer indexItemId : indexItemIds) {
					IndexItem indexItemSnapshot = cachedIndexItemsMap.get(indexItemId);
					IndexItem indexItemFromDatabase = databaseIndexItemsMap.get(indexItemId);

					if (indexItemSnapshot == null && indexItemFromDatabase != null) {
						// no cached index item with same indexItemid
						outSyncedIndexItemIds.add(indexItemFromDatabase.getIndexItemId());
						indexItemsToAdd.add(indexItemFromDatabase);

					} else if (indexItemSnapshot != null && indexItemFromDatabase == null) {
						// no index item from database with same indexItemId
						outSyncedIndexItemIds.add(indexItemSnapshot.getIndexItemId());
						indexItemsToRemove.add(indexItemSnapshot);

					} else if (indexItemSnapshot != null && indexItemFromDatabase != null && !equals(indexItemSnapshot, indexItemFromDatabase)) {
						// cached index item and index item from database with same indexItemId both exist but are out of sync.
						outSyncedIndexItemIds.add(indexItemSnapshot.getIndexItemId());
						indexItemsToUpdate.put(indexItemSnapshot, indexItemFromDatabase);
					}
				}

				if (debug) {
					if (outSyncedIndexItemIds.isEmpty()) {
						System.out.println("\t\t\tvalidate() cached data (cachedRevisionId=" + cachedRevisionIdThen + ") are in-sync with database (latestRevisionId=" + latestRevisionIdNow + ").");
					} else {
						System.out.println("\t\t\tvalidate() cached data (cachedRevisionId=" + cachedRevisionIdThen + ") are out-of-sync with database (latestRevisionId=" + latestRevisionIdNow + ").");
						System.out.println("\t\t\tvalidate() outSyncedIndexItemIds are: " + Arrays.toString(outSyncedIndexItemIds.toArray(new Integer[outSyncedIndexItemIds.size()])));
					}
				}

				if (!outSyncedIndexItemIds.isEmpty()) {
					// Check validation result to see if there are any index items are out of sync with the database and update them if any.
					indexItemsRWLock.writeLock().lock();
					try {
						for (IndexItem indexItemToAdd : indexItemsToAdd) {
							addCachedIndexItem(indexItemToAdd);
						}

						for (IndexItem indexItemToRemove : indexItemsToRemove) {
							removeCachedIndexItem(indexItemToRemove.getIndexItemId());
						}

						for (Entry<IndexItem, IndexItem> indexItemEntry : indexItemsToUpdate.entrySet()) {
							IndexItem indexItemSnapshot = indexItemEntry.getKey();
							IndexItem indexItemFromDatabase = indexItemEntry.getValue();
							IndexItem cachedIndexItem = snapshotToCachedIndexItemMap.get(indexItemSnapshot);

							Date lastUpdateTimeCached = cachedIndexItem.getLastUpdateTime();
							Date lastUpdateTimeSnapshot = indexItemSnapshot.getLastUpdateTime();

							boolean matchLastUpdateTime = CompareUtil.equals(lastUpdateTimeCached, lastUpdateTimeSnapshot, true);
							if (!matchLastUpdateTime) {
								String lastUpdateTimeCachedStr = lastUpdateTimeCached != null ? DateUtil.toString(lastUpdateTimeCached, DateUtil.getJdbcDateFormat()) : "null";
								String lastUpdateTimeSnapshotStr = lastUpdateTimeSnapshot != null ? DateUtil.toString(lastUpdateTimeSnapshot, DateUtil.getJdbcDateFormat()) : "null";

								System.err.println("\t\t\tvalidate() indexItemSnapshot (lastUpdateTime=" + lastUpdateTimeSnapshotStr + ") is out of sync with cachedIndexItem (lastUpdateTime=" + lastUpdateTimeCachedStr + "). The cachedIndexItem has been updated duirng validation. Update of the cachedIndexItem (indexItemId=" + cachedIndexItem.getIndexItemId() + ") is cancelled");
								continue;
							}

							update(cachedIndexItem, indexItemFromDatabase);
						}

					} finally {
						indexItemsRWLock.writeLock().unlock();
					}
				}
			}
		} finally {
			isValidating = false;
		}
	}

	@Override
	public List<IndexItem> getIndexItems() throws IndexServiceException {
		this.indexItemsRWLock.readLock().lock();
		try {
			List<IndexItem> indexItems = new ArrayList<IndexItem>();
			indexItems.addAll(this.cachedIndexItems);
			return indexItems;
		} finally {
			this.indexItemsRWLock.readLock().unlock();
		}
	}

	@Override
	public List<IndexItem> getIndexItemsByIndexProvider(String indexProviderId) throws IndexServiceException {
		this.indexItemsRWLock.readLock().lock();
		try {
			List<IndexItem> indexItems = new ArrayList<IndexItem>();
			for (IndexItem indexItem : this.cachedIndexItems) {
				if (indexProviderId != null && indexProviderId.equals(indexItem.getIndexItemId())) {
					indexItems.add(indexItem);
				}
			}
			return indexItems;
		} finally {
			this.indexItemsRWLock.readLock().unlock();
		}
	}

	@Override
	public List<IndexItem> getIndexItemsByNamespace(String namespace) throws IndexServiceException {
		this.indexItemsRWLock.readLock().lock();
		try {
			List<IndexItem> indexItems = new ArrayList<IndexItem>();
			for (IndexItem indexItem : this.cachedIndexItems) {
				if (namespace != null && namespace.equals(indexItem.getNamespace())) {
					indexItems.add(indexItem);
				}
			}
			return indexItems;
		} finally {
			this.indexItemsRWLock.readLock().unlock();
		}
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String namespace) throws IndexServiceException {
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		for (IndexItem indexItem : this.cachedIndexItems) {
			if (indexItem.getIndexProviderId().equals(indexProviderId) && indexItem.getNamespace().equals(namespace)) {
				indexItems.add(indexItem);
			}
		}
		return indexItems;
	}

	@Override
	public boolean addIndexItem(String indexProviderId, String namespace, String name, Map<String, Object> properties) throws IndexServiceException {
		boolean succeed = false;

		// -------------------------------------------------------------------------------------------------------
		// Step1. Create a new request
		// -------------------------------------------------------------------------------------------------------
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put(IndexServiceConstants.IDX_INDEX_PROVIDER_ID, indexProviderId);
		arguments.put(IndexServiceConstants.IDX_NAMESPACE, namespace);
		arguments.put(IndexServiceConstants.IDX_NAME, name);
		arguments.put(IndexServiceConstants.IDX_PROPERTIES, properties);

		IndexItemRequestVO newRequestVO = getDatabaseHelper().createNewRequestInDatabase(this, indexProviderId, IndexServiceConstants.CMD_CREATE_INDEX_ITEM, JSONUtil.toJsonString(arguments));
		final Integer requestId = newRequestVO.getRequestId();

		// -------------------------------------------------------------------------------------------------------
		// Step2. Start updating the request's lastUpdateTime every second.
		// -------------------------------------------------------------------------------------------------------
		Runnable requestUpdater = new Runnable() {
			@Override
			public void run() {
				System.out.println("Update request (requestId=" + requestId + ") lastUpdateTime.");
				getDatabaseHelper().updateRequestLastUpdateTimeInDatabase(IndexServiceImpl.this, requestId, new Date());
			}
		};
		// update request's lastUpdateTime every second. initial delay 1 second
		final ScheduledFuture<?>[] requestUpdaterHandle = new ScheduledFuture<?>[1];
		requestUpdaterHandle[0] = requestUpdatorScheduler.scheduleAtFixedRate(requestUpdater, 1, 1, SECONDS);

		// From now on, for each exception occurs, the requestUpdater need to be cancelled.
		try {
			// -------------------------------------------------------------------------------------------------------
			// Step3. Check if there are previous pending requests that are still active (last update time within 10 seconds).
			// Wait (for a max of 10 seconds) until no previous active pending requests exist.
			// -------------------------------------------------------------------------------------------------------
			long totalWaitingTime = 0;
			while (getDatabaseHelper().hasActivePendingRequestsInDatabase(this, requestId)) {
				// Stops waiting if total waiting time exceed 10 seconds. 10 seconds should be long enough for any single command to complete.
				if (totalWaitingTime > 10 * 1000) {
					// throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, "There are still active pending predecessor requests
					// after waiting for 10 seconds. Current request for creating new index item is cancelled.");
					if (debug) {
						System.err.println(getClassName() + ".createIndexItem() there are previous requests that are still active and pending after waiting for 10 seconds. Current request for creating new index item continues.");
						break;
					}
				}
				// Wait for half second and then check again.
				try {
					if (debug) {
						System.out.println(getClassName() + ".createIndexItem() there are previous requests that are still active and pending. Wait for 1 second...");
					}
					Thread.sleep(500);
					totalWaitingTime += 500;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// -------------------------------------------------------------------------------------------------------
			// Step 4. Create index item data and get the new indexItemId.
			// -------------------------------------------------------------------------------------------------------
			IndexItemDataVO newIndexItemVO = getDatabaseHelper().createIndexItemInDatabase(this, indexProviderId, namespace, name, JSONUtil.toJsonString(properties));

			Integer indexItemId = newIndexItemVO.getIndexItemId();
			String newPropertiesString = newIndexItemVO.getPropertiesString();
			Date createTime = newIndexItemVO.getCreateTime();
			Date lastUpdateTime = newIndexItemVO.getLastUpdateTime();
			Map<String, Object> newProperties = JSONUtil.toProperties(newPropertiesString);

			// -------------------------------------------------------------------------------------------------------
			// Step 5. Update revision table
			// -------------------------------------------------------------------------------------------------------
			// command and arguments
			String command = IndexServiceConstants.CMD_CREATE_INDEX_ITEM;
			Map<String, Object> commandArguments = new HashMap<String, Object>();
			commandArguments.put(IndexServiceConstants.IDX_INDEX_ITEM_ID, indexItemId);
			commandArguments.put(IndexServiceConstants.IDX_INDEX_PROVIDER_ID, indexProviderId);
			commandArguments.put(IndexServiceConstants.IDX_NAMESPACE, namespace);
			commandArguments.put(IndexServiceConstants.IDX_NAME, namespace);
			commandArguments.put(IndexServiceConstants.IDX_PROPERTIES, newProperties);
			commandArguments.put(IndexServiceConstants.IDX_CREATE_TIME, createTime);
			commandArguments.put(IndexServiceConstants.IDX_LAST_UPDATE_TIME, lastUpdateTime);

			// undo command and undo arguments
			String undoCommand = IndexServiceConstants.CMD_DELETE_INDEX_ITEM;
			Map<String, Object> undoCommandArguments = new HashMap<String, Object>();
			undoCommandArguments.put(IndexServiceConstants.IDX_INDEX_ITEM_ID, indexItemId);

			getDatabaseHelper().createRevisionInDatabase(this, indexProviderId, command, commandArguments, undoCommand, undoCommandArguments);

			// -------------------------------------------------------------------------------------------------------
			// Step 6. Mark the request as completed. Stops the requestUpdater.
			// -------------------------------------------------------------------------------------------------------
			getDatabaseHelper().completeRequestInDatabase(this, requestId, requestUpdaterHandle[0]);
			succeed = true;

		} finally {
			// -------------------------------------------------------------------------------------------------------
			// Step 7. Mark the request as cancelled. Stops the requestUpdater.
			// -------------------------------------------------------------------------------------------------------
			if (!succeed) {
				getDatabaseHelper().cancelRequestInDatabase(this, requestId, requestUpdaterHandle[0]);
			}
		}

		// -------------------------------------------------------------------------------------------------------
		// Step 8. Synchronize with database again.
		// -------------------------------------------------------------------------------------------------------
		synchronize();

		// -------------------------------------------------------------------------------------------------------
		// Step 9. Notify other index services to synchronize
		// -------------------------------------------------------------------------------------------------------
		notifyIndexServicesToSync();

		return succeed;
	}

	protected void notifyIndexServicesToSync() {
		try {
			String nodeName = getNodeName();

			List<IndexItem> indexItems = getIndexItemsByNamespace(IndexServiceConstants.NAMESPACE);

			for (IndexItem indexItem : indexItems) {
				String indexServiceNodeName = indexItem.getName();
				String url = (String) indexItem.getProperty(IndexServiceConstants.IDX_PROP_URL);
				String contextRoot = (String) indexItem.getProperty(IndexServiceConstants.IDX_PROP_CONTEXT_ROOT);
				String username = (String) indexItem.getProperty(IndexServiceConstants.IDX_PROP_USERNAME);
				String password = (String) indexItem.getProperty(IndexServiceConstants.IDX_PROP_PASSWORD);
				Date lastHeartbeatTime = (Date) indexItem.getRuntimeProperty(IndexServiceConstants.IDX_PROP_LAST_HEARTBEAT_TIME);

				// gets a calendar using the default time zone and locale.
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.SECOND, -getHeartbeatExpireTime());
				Date heartbeatTimeoutTime = calendar.getTime();

				if (nodeName != null && nodeName.equals(indexServiceNodeName)) {
					System.err.println("Ignore notifying current index service node '" + indexServiceNodeName + "'.");
					continue;
				}

				// Last heartbeat happened 30 seconds ago. Index service node is considered as not running.
				if (lastHeartbeatTime == null || lastHeartbeatTime.compareTo(heartbeatTimeoutTime) <= 0) {
					System.err.println("Index service node '" + indexServiceNodeName + "' is not running (last heartbeat time is " + lastHeartbeatTime + " ).");
					continue;
				}

				org.origin.mgm.client.api.IndexServiceConfiguration config = contextRoot == null ? new org.origin.mgm.client.api.IndexServiceConfiguration(url, username, password) : new org.origin.mgm.client.api.IndexServiceConfiguration(url, contextRoot, username, password);
				final org.origin.mgm.client.api.IndexService indexService = org.origin.mgm.client.api.IndexService.newInstance(config);
				try {
					indexService.action("sync", null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IndexServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove an index item.
	 * 
	 * @param indexItemId
	 * @throws IndexServiceException
	 */
	@Override
	public boolean removeIndexItem(Integer indexItemId) throws IndexServiceException {
		return false;
	}

	/**
	 * Get all properties names of a service.
	 * 
	 * @param namespace
	 * @param name
	 * @return
	 * @throws IndexServiceException
	 */
	public String[] getPropertyNames(String namespace, String name) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null && entry.getProperties() != null) {
		// return entry.getProperties().keySet().toArray(new String[entry.getProperties().size()]);
		// }
		return new String[] {};
	}

	/**
	 * Get the properties of a service.
	 * 
	 * @param namespace
	 * @param name
	 * @return
	 * @throws IndexServiceException
	 */
	public Map<String, Object> getProperties(String namespace, String name) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null) {
		// return entry.getProperties();
		// }
		return Collections.emptyMap();
	}

	/**
	 * Check whether a property of a service is available.
	 * 
	 * @param namespace
	 *            namespace of the service
	 * @param name
	 *            name of the service
	 * @param propName
	 *            property name
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean hasProperty(String namespace, String name, String propName) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null && entry.getProperties() != null && propName != null && entry.getProperties().containsKey(propName)) {
		// return true;
		// }
		return false;
	}

	/**
	 * Get the property value of a service.
	 * 
	 * @param namespace
	 * @param name
	 * @param propName
	 * @return
	 * @throws IndexServiceException
	 */
	public Object getProperty(String namespace, String name, String propName) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null && entry.getProperties() != null && propName != null) {
		// return entry.getProperties().get(propName);
		// }
		return null;
	}

	/**
	 * Set the property of a service.
	 * 
	 * @param namespace
	 *            namespace of the service
	 * @param name
	 *            name of the service
	 * @param propName
	 *            property name
	 * @param propValue
	 *            property value
	 * @throws IndexServiceException
	 */
	public void setProperty(String namespace, String name, String propName, Object propValue) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null && entry.getProperties() != null && propName != null) {
		//
		// if (entry.getProperties().containsKey(propName)) {
		// Object oldPropValue = entry.getProperties().get(propName);
		//
		// if ((oldPropValue == null && propValue != null) || (oldPropValue != null && !oldPropValue.equals(propValue))) {
		// entry.getProperties().put(propName, propValue);
		//
		// // Notify service property changed event
		// this.listenerSupport.notifyPropertyChanged(namespace, name, propName, oldPropValue, propValue);
		// }
		//
		// } else {
		// entry.getProperties().put(propName, propValue);
		//
		// // Notify service property added event
		// this.listenerSupport.notifyPropertyAdded(namespace, name, propName, propValue);
		// }
		// }
	}

	/**
	 * Remove the property of a service.
	 * 
	 * @param namespace
	 *            namespace of the service
	 * @param name
	 *            name of the service
	 * @param propName
	 *            property name
	 * @throws IndexServiceException
	 */
	public void removeProperty(String namespace, String name, String propName) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null && entry.getProperties() != null && propName != null) {
		// entry.getProperties().remove(propName);
		//
		// // Notify service property removed event
		// this.listenerSupport.notifyPropertyRemoved(namespace, name, propName);
		// }
	}

	/**
	 * Add a ServiceRegistryListener.
	 * 
	 * @param listener
	 */
	public void addListener(IndexServiceListener listener) {
		listenerSupport.addListener(listener);
	}

	/**
	 * Remove a ServiceRegistryListener.
	 * 
	 * @param listener
	 */
	public void removeListener(IndexServiceListener listener) {
		listenerSupport.removeListener(listener);
	}

	// ------------------------------------------------------------------------------------------------------------
	// Methods for RevisionCommand to update the cached data.
	// ------------------------------------------------------------------------------------------------------------
	/** implement IndexServiceUpdatable interface */
	@Override
	public void addCachedIndexItem(IndexItem indexItem) throws IndexServiceException {
		if (debug) {
			System.out.println(getClassName() + ".addCachedIndexItem()");
		}

		this.indexItemsRWLock.writeLock().lock();
		try {
			boolean indexItemIdExists = false;
			for (IndexItem cachedIndexItem : this.cachedIndexItems) {
				if (cachedIndexItem.getIndexItemId().equals(indexItem.getIndexItemId())) {
					indexItemIdExists = true;
					break;
				}
			}
			if (indexItemIdExists) {
				throw new IndexServiceException(IndexServiceConstants.ERROR_CODE_INDEX_ITEM_EXIST, "Index item with id '" + indexItem.getIndexItemId() + "' already exists.");
			}
			this.cachedIndexItems.add(indexItem);

			if (debug) {
				System.out.println(getClassName() + ".addCachedIndexItem() index item (indexItemId=" + indexItem.getIndexItemId() + ") is added to the cached data.");
			}
		} finally {
			this.indexItemsRWLock.writeLock().unlock();
		}
	}

	@Override
	public void removeCachedIndexItem(Integer indexItemId) throws IndexServiceException {
		if (debug) {
			System.out.println(getClassName() + ".removeCachedIndexItem()");
		}

		this.indexItemsRWLock.writeLock().lock();
		try {
			IndexItem indexItemToRemove = null;
			for (IndexItem cachedIndexItem : this.cachedIndexItems) {
				if (cachedIndexItem.getIndexItemId().equals(indexItemId)) {
					indexItemToRemove = cachedIndexItem;
					break;
				}
			}

			if (indexItemToRemove == null) {
				throw new IndexServiceException(IndexServiceConstants.ERROR_CODE_INDEX_ITEM_NOT_FOUND, "Index item with id '" + indexItemId + "' is not found.");
			}

			this.cachedIndexItems.remove(indexItemToRemove);

			if (debug) {
				System.out.println(getClassName() + ".removeCachedIndexItem() index item (indexItemId=" + indexItemId + ") is removed from the cached data.");
			}
		} finally {
			this.indexItemsRWLock.writeLock().unlock();
		}
	}

	@Override
	public void udpateCachedIndexItem(Integer indexItemId, Map<String, Object> properties, Date lastUpdateTime) throws IndexServiceException {
		if (debug) {
			System.out.println(getClassName() + ".udpateCachedIndexItem()");
		}

		this.indexItemsRWLock.writeLock().lock();
		try {
			IndexItem indexItemToUpdate = null;
			for (IndexItem cachedIndexItem : this.cachedIndexItems) {
				if (cachedIndexItem.getIndexItemId().equals(indexItemId)) {
					indexItemToUpdate = cachedIndexItem;
					break;
				}
			}

			if (indexItemToUpdate == null) {
				throw new IndexServiceException(IndexServiceConstants.ERROR_CODE_INDEX_ITEM_NOT_FOUND, "Index item with id '" + indexItemId + "' is not found.");
			}

			indexItemToUpdate.setProperties(properties);
			indexItemToUpdate.setLastUpdateTime(lastUpdateTime);

			if (debug) {
				System.out.println(getClassName() + ".udpateCachedIndexItem() index item (indexItemId=" + indexItemId + ") is updated in the cached data.");
			}
		} finally {
			this.indexItemsRWLock.writeLock().unlock();
		}
	}

	/**
	 * 
	 * @param indexItemSnapshot
	 * @param indexItemFromDatabase
	 * @return
	 */
	private boolean equals(IndexItem indexItemSnapshot, IndexItem indexItemFromDatabase) {
		if (indexItemSnapshot == null) {
			if (debug) {
				System.err.println(getClassName() + ".update() indexItemSnapshot is null.");
			}
			return false;
		}
		if (indexItemFromDatabase == null) {
			if (debug) {
				System.err.println(getClassName() + ".update() indexItemFromDatabase is null.");
			}
			return false;
		}

		Integer indexItemId1 = indexItemSnapshot.getIndexItemId();
		String indexProviderId1 = indexItemSnapshot.getIndexProviderId();
		String namespace1 = indexItemSnapshot.getNamespace();
		String name1 = indexItemSnapshot.getName();
		Map<String, Object> properties1 = indexItemSnapshot.getProperties();
		Date createTime1 = indexItemSnapshot.getCreateTime();
		Date lastUpdateTime1 = indexItemSnapshot.getLastUpdateTime();

		Integer indexItemId2 = indexItemFromDatabase.getIndexItemId();
		String indexProviderId2 = indexItemFromDatabase.getIndexProviderId();
		String namespace2 = indexItemFromDatabase.getNamespace();
		String name2 = indexItemFromDatabase.getName();
		Map<String, Object> properties2 = indexItemFromDatabase.getProperties();
		Date createTime2 = indexItemFromDatabase.getCreateTime();
		Date lastUpdateTime2 = indexItemFromDatabase.getLastUpdateTime();

		boolean matchIndexItemId = CompareUtil.equals(indexItemId1, indexItemId2, false);
		boolean matchIndexProviderId = CompareUtil.equals(indexProviderId1, indexProviderId2, true);
		boolean matchNamespace = CompareUtil.equals(namespace1, namespace2, true);
		boolean matchName = CompareUtil.equals(name1, name2, true);
		boolean matchProperties = CompareUtil.equals(properties1, properties2, true);
		boolean matchCreateTime = CompareUtil.equals(createTime1, createTime2, true);
		boolean matchLastUpdateTime = CompareUtil.equals(lastUpdateTime1, lastUpdateTime2, true);

		if (matchIndexItemId && matchIndexProviderId && matchNamespace && matchName && matchProperties && matchCreateTime && matchLastUpdateTime) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param cachedIndexItem
	 * @param indexItemFromDatabase
	 * @return
	 * @throws IndexServiceException
	 */
	private boolean update(IndexItem cachedIndexItem, IndexItem indexItemFromDatabase) throws IndexServiceException {
		if (cachedIndexItem == null) {
			if (debug) {
				System.err.println(getClassName() + ".update() cachedIndexItem is null.");
			}
			return false;
		}
		if (indexItemFromDatabase == null) {
			if (debug) {
				System.err.println(getClassName() + ".update() indexItemFromDatabase is null.");
			}
			return false;
		}

		boolean isUpdated = false;

		Integer indexItemId1 = cachedIndexItem.getIndexItemId();
		String indexProviderId1 = cachedIndexItem.getIndexProviderId();
		String namespace1 = cachedIndexItem.getNamespace();
		String name1 = cachedIndexItem.getName();
		Map<String, Object> properties1 = cachedIndexItem.getProperties();
		Date createTime1 = cachedIndexItem.getCreateTime();
		Date lastUpdateTime1 = cachedIndexItem.getLastUpdateTime();

		Integer indexItemId2 = indexItemFromDatabase.getIndexItemId();
		String indexProviderId2 = indexItemFromDatabase.getIndexProviderId();
		String namespace2 = indexItemFromDatabase.getNamespace();
		String name2 = indexItemFromDatabase.getName();
		Map<String, Object> properties2 = indexItemFromDatabase.getProperties();
		Date createTime2 = indexItemFromDatabase.getCreateTime();
		Date lastUpdateTime2 = indexItemFromDatabase.getLastUpdateTime();

		boolean matchIndexItemId = CompareUtil.equals(indexItemId1, indexItemId2, false);
		if (!matchIndexItemId) {
			throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, "Cannot update cached index item (indexItemId=" + indexItemId1 + ") with an index item from database with different index item id (indexItemId=" + indexItemId2 + ").");
		}

		boolean matchIndexProviderId = CompareUtil.equals(indexProviderId1, indexProviderId2, true);
		if (!matchIndexProviderId) {
			cachedIndexItem.setIndexProviderId(indexProviderId2);
			isUpdated = true;
		}

		boolean matchNamespace = CompareUtil.equals(namespace1, namespace2, true);
		if (!matchNamespace) {
			cachedIndexItem.setNamespace(namespace2);
			isUpdated = true;
		}

		boolean matchName = CompareUtil.equals(name1, name2, true);
		if (!matchName) {
			cachedIndexItem.setName(name2);
			isUpdated = true;
		}

		boolean matchProperties = CompareUtil.equals(properties1, properties2, true);
		if (!matchProperties) {
			cachedIndexItem.setProperties(properties2);
			isUpdated = true;
		}

		boolean matchCreateTime = CompareUtil.equals(createTime1, createTime2, true);
		if (!matchCreateTime) {
			cachedIndexItem.setCreateTime(createTime2);
			isUpdated = true;
		}

		boolean matchLastUpdateTime = CompareUtil.equals(lastUpdateTime1, lastUpdateTime2, true);
		if (!matchLastUpdateTime) {
			cachedIndexItem.setLastUpdateTime(lastUpdateTime2);
			isUpdated = true;
		}

		return isUpdated;
	}
}
