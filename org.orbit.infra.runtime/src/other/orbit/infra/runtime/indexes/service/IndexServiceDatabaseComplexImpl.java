package other.orbit.infra.runtime.indexes.service;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexItemRequestVO;
import org.orbit.infra.model.indexes.IndexItemRevisionVO;
import org.orbit.infra.model.indexes.IndexItemVO;
import org.orbit.infra.runtime.InfraConstants;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.indexes.service.IndexServiceDatabaseHelper;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.ICommand;
import org.origin.common.command.ICommandStack;
import org.origin.common.command.IEditingDomain;
import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.json.JSONUtil;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.ServerException;
import org.origin.common.util.CommandRequestHandler;
import org.origin.common.util.CompareUtil;
import org.origin.common.util.DateUtil;
import org.origin.common.util.IntegerComparator;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class IndexServiceDatabaseComplexImpl implements IndexService, IndexServiceUpdatable, ConnectionAware, CommandRequestHandler {

	// index item attribute names
	public static String INDEX_ITEM_ID_ATTR = "indexItemId";
	public static String INDEX_ITEM_PROVIDER_ID_ATTR = "indexProviderId";
	public static String INDEX_ITEM_TYPE_ATTR = "type";
	public static String INDEX_ITEM_NAME_ATTR = "name";
	public static String INDEX_ITEM_PROPERTIES_ATTR = "properties";
	public static String INDEX_ITEM_CREATE_TIME_ATTR = "createTime";
	public static String INDEX_ITEM_LAST_UPDATE_TIME_ATTR = "lastUpdateTime";

	// runtime error codes
	public static String ERROR_CODE_INDEX_ITEM_EXIST = "1001";
	public static String ERROR_CODE_INDEX_ITEM_NOT_FOUND = "2002";
	public static String ERROR_CODE_INDEX_ITEM_ILLEGAL_ARGUMENTS = "2003";

	// runtime constants
	public static String INDEX_SERVICE_EDITING_DOMAIN = "indexservice"; // editing domain name for editing index services

	protected BundleContext bundleContext;
	protected IndexServiceDatabaseConfigurationV1 indexServiceConfig;
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

	protected ICommandStack revisionCommandStack;
	protected boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 * @param indexServiceConfig
	 */
	public IndexServiceDatabaseComplexImpl(BundleContext bundleContext, IndexServiceDatabaseConfigurationV1 indexServiceConfig) {
		this.bundleContext = bundleContext;
		this.indexServiceConfig = indexServiceConfig;
	}

	protected String getClassName() {
		return this.getClass().getSimpleName();
	}

	public IndexServiceConfigurationV1 getConfiguration() {
		return this.indexServiceConfig;
	}

	protected IndexServiceDatabaseHelper getDatabaseHelper() {
		return IndexServiceDatabaseHelper.INSTANCE;
	}

	/**
	 * implement ConnectionAware interface
	 * 
	 * @throws SQLException
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return this.indexServiceConfig.getConnection();
	}

	// ------------------------------------------------------------------------------------------------------------
	// Methods for handle command requests
	// ------------------------------------------------------------------------------------------------------------
	/** implement CommandRequestHandler interface */
	@Override
	public boolean isCommandSupported(String command, Map<String, Object> parameters) {
		if ("sync".equalsIgnoreCase(command)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean performCommand(String command, Map<String, Object> parameters) {
		if ("sync".equalsIgnoreCase(command)) {
			try {
				synchronize();
			} catch (ServerException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------------------------
	// Methods accessing config properties
	// ------------------------------------------------------------------------------------------------------------
	public String getName() {
		return PropertyUtil.getString(this.indexServiceConfig.getProperties(), InfraConstants.COMPONENT_INDEX_SERVICE_NAME, null);
	}

	public String getHostURL() {
		return PropertyUtil.getString(this.indexServiceConfig.getProperties(), InfraConstants.COMPONENT_INDEX_SERVICE_HOST_URL, null);
	}

	public String getContextRoot() {
		return PropertyUtil.getString(this.indexServiceConfig.getProperties(), InfraConstants.COMPONENT_INDEX_SERVICE_CONTEXT_ROOT, null);
	}

	// ------------------------------------------------------------------------------------------------------------
	// Methods for lifecycle
	// ------------------------------------------------------------------------------------------------------------
	/** implement LifecycleAware interface */
	public synchronized void start(BundleContext bundleContext) {
		if (debug) {
			System.out.println(getClassName() + ".start()");
		}

		try {
			initializeTables();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Initialize the command stack
		this.revisionCommandStack = IEditingDomain.getEditingDomain(INDEX_SERVICE_EDITING_DOMAIN).getCommandStack(this);

		// Synchronize once when the index service is started, so that the cachedIndexItems and cachedRevisionId can be initialized.
		try {
			synchronize();
		} catch (ServerException e) {
			e.printStackTrace();
		}

		// Check/register the current index service as a service.
		registerAsService();

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

	/**
	 * Initialize database tables.
	 * 
	 * @throws SQLException
	 */
	public void initializeTables() throws SQLException {
		Connection conn = this.indexServiceConfig.getConnection();
		try {
			// DatabaseUtil.dropTable(conn, IndexItemRequestTableHandler.INSTANCE);
			// DatabaseUtil.dropTable(conn, IndexItemDataTableHandler.INSTANCE);
			// DatabaseUtil.dropTable(conn, IndexItemRevisionTableHandler.INSTANCE);

			DatabaseUtil.initialize(conn, IndexItemRequestTableHandler.INSTANCE);
			DatabaseUtil.initialize(conn, IndexItemDataTableHandler.INSTANCE);
			DatabaseUtil.initialize(conn, IndexItemRevisionTableHandler.INSTANCE);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
	}

	public synchronized void stop(BundleContext bundleContext) {
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
		IEditingDomain.getEditingDomain(INDEX_SERVICE_EDITING_DOMAIN).disposeCommandStack(this);
		this.revisionCommandStack = null;
	}

	/**
	 * Check/register the current index service as a service.
	 * 
	 */
	protected void registerAsService() {
		try {
			// Get configuration of the index service from the config.ini file
			String name = getName();
			String hostUrl = getHostURL();
			String contextRoot = getContextRoot();

			IndexItem matchedIndexItem = null;
			List<IndexItem> cachedIndexItems = getIndexItems();
			for (IndexItem cachedIndexItem : cachedIndexItems) {
				if (InfraConstants.INDEX_SERVICE_INDEXER_ID.equals(cachedIndexItem.getIndexProviderId()) && InfraConstants.INDEX_SERVICE_TYPE.equals(cachedIndexItem.getType())) {
					if (name != null && name.equals(cachedIndexItem.getName())) {
						matchedIndexItem = cachedIndexItem;
						break;
					}
				}
			}

			if (matchedIndexItem == null) {
				// Add an index item for the current index service and put the url, contextRoot, username, password for accessing the service as
				// properties of the index item.
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(org.orbit.infra.api.InfraConstants.SERVICE__HOST_URL, hostUrl);
				properties.put(org.orbit.infra.api.InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);

				addIndexItem(InfraConstants.INDEX_SERVICE_INDEXER_ID, InfraConstants.INDEX_SERVICE_TYPE, name, properties);
			}
		} catch (ServerException e) {
			e.printStackTrace();
		}
	}

	protected void scheduleBroadcaster() {
		// scheduled runner to broadcast to other index service nodes.
		Runnable broadcastRunner = new Runnable() {
			@Override
			public void run() {
				// try {
				// broadcast();
				// } catch (IndexServiceException e) {
				// e.printStackTrace();
				// }
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
				} catch (ServerException e) {
					e.printStackTrace();
				}
			}
		};
		// synchronize index items with database.
		this.synchronizeHandle = synchronizationScheduler.scheduleAtFixedRate(indexItemsSynchronizer, 0, 30, SECONDS);
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
				} catch (ServerException e) {
					e.printStackTrace();
				}
			}
		};
		// validate cached index items.
		this.validationHandle = validationScheduler.scheduleAtFixedRate(indexItemsValidator, 35, 30, SECONDS);
	}

	protected void unscheduleValidator() {
		if (this.validationHandle != null && !this.validationHandle.isCancelled()) {
			this.validationHandle.cancel(true);
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	// Methods for broadcasting to other index service nodes
	// ------------------------------------------------------------------------------------------------------------
	// public synchronized void broadcast() throws IndexServiceException {
	// try {
	// String serviceName = getName();
	//
	// List<IndexItem> indexItems = getIndexItems(null, OriginConstants.INDEX_SERVICE_TYPE);
	//
	// for (IndexItem indexItem : indexItems) {
	// String currName = indexItem.getName();
	// String url = (String) indexItem.getProperty(OriginConstants.INDEX_SERVICE_HOST_URL);
	// String contextRoot = (String) indexItem.getProperty(OriginConstants.INDEX_SERVICE_CONTEXT_ROOT);
	// Date lastHeartbeatTime = (Date) indexItem.getRuntimeProperty(OriginConstants.LAST_HEARTBEAT_TIME);
	//
	// // gets a calendar using the default time zone and locale.
	// Calendar calendar = Calendar.getInstance();
	// calendar.add(Calendar.SECOND, -getServiceHeartbeatExpireTime());
	// Date heartbeatTimeoutTime = calendar.getTime();
	//
	// if (serviceName != null && serviceName.equals(currName)) {
	// System.err.println("Ignore notifying current index service node '" + currName + "'.");
	// continue;
	// }
	//
	// // Last heartbeat happened 30 seconds ago. Index service node is considered as not running.
	// if (lastHeartbeatTime == null || lastHeartbeatTime.compareTo(heartbeatTimeoutTime) <= 0) {
	// System.err.println("Index service node '" + currName + "' is not running (last heartbeat time is " + lastHeartbeatTime + " ).");
	// continue;
	// }
	//
	// IndexServiceConfiguration config = contextRoot == null ? new IndexServiceConfiguration(url) : new IndexServiceConfiguration(url,
	// contextRoot);
	// IndexService indexService = IndexServiceFactory.getInstance().createIndexService(config);
	// try {
	// indexService.sendCommand("sync", null);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// } catch (IndexServiceException e) {
	// e.printStackTrace();
	// }
	// }

	// ------------------------------------------------------------------------------------------------------------
	// Methods for synchronization of the cached data
	// ------------------------------------------------------------------------------------------------------------
	public synchronized void synchronize() throws ServerException {
		if (debug) {
			// System.out.println(getClassName() + ".synchronize()");
			// System.out.println("\t\t\tcurrent cachedRevisionId is " + this.cachedRevisionId);
		}
		Integer oldCachedRevisionId = this.cachedRevisionId;

		CommandContext context = new CommandContext();
		context.adapt(IndexService.class, this);
		context.adapt(CommandRequestHandler.class, this);
		context.adapt(ConnectionAware.class, this);

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
					System.out.println(getClassName() + ".synchronize() cached data matches latestRevisionId (" + oldCachedRevisionId + "->" + cachedRevisionId + ").");
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
	 * @throws ServerException
	 */
	protected synchronized void reloadCache() throws ServerException {
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
			if (totalWaitingTime > 60 * 1000) {
				System.err.println(getClassName() + ".reloadCache() index items are being updated during loading of index items from database. After retried for 60 seconds, current reload is cancelled.");
				return;
			}

			// --------------------------------------------------------------------
			// wait for 1 second before retry again
			// --------------------------------------------------------------------
			try {
				if (debug) {
					System.out.println(getClassName() + ".reloadCache() index items are being updated from database. Wait for 10 seconds...");
				}
				Thread.sleep(10 * 1000);
				totalWaitingTime += (10 * 1000);
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
			throw new ServerException(StatusDTO.RESP_500, " Cannot load index items from database.");
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
	 * @throws ServerException
	 */
	protected synchronized void appendRevisions(CommandContext context, int startRevisionId, int endRevisionId) throws ServerException {
		if (debug) {
			// System.out.println(getClassName() + ".appendRevisions()");
			// System.out.println(getClassName() + ".appendRevisions() startRevisionId=" + startRevisionId + ", endRevisionId=" + endRevisionId);
		}

		List<IndexItemRevisionVO> revisionVOs = getDatabaseHelper().getRevisionsFromDatabase(this, startRevisionId, endRevisionId);

		indexItemsRWLock.writeLock().lock();
		try {
			for (IndexItemRevisionVO revisionVO : revisionVOs) {
				Integer revisionId = revisionVO.getRevisionId();
				String command = revisionVO.getCommand();
				String argumentsString = revisionVO.getArgumentsString();
				String undoCommand = revisionVO.getUndoCommand();
				String undoArgumentsString = revisionVO.getUndoArgumentsString();

				Map<String, Object> arguments = JSONUtil.toProperties(argumentsString, true);
				Map<String, Object> undoArguments = JSONUtil.toProperties(undoArgumentsString, true);

				RevisionCommand revisionCommand = new RevisionCommand(this, revisionId, command, arguments, undoCommand, undoArguments);
				try {
					// Create/Delete/Update index item with revision command
					this.revisionCommandStack.execute(context, revisionCommand);

					// Update the cached revision id
					this.cachedRevisionId = revisionId;

				} catch (CommandException e) {
					e.printStackTrace();
					throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when a applying a revision with a synchronization command. Message: " + e.getMessage());
				}
			}

			if (debug) {
				System.out.println(getClassName() + ".appendRevisions(" + startRevisionId + ", " + endRevisionId + ") cachedRevisionId is appended to " + this.cachedRevisionId);
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
	 * @throws ServerException
	 */
	protected synchronized void revertToRevision(CommandContext context, int toRevisionId) throws ServerException {
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

				ICommand command = this.revisionCommandStack.peekUndoCommand();

				if (!(command instanceof RevisionCommand)) {
					throw new ServerException(StatusDTO.RESP_500, "Cannot revert a non-revision command '" + command.getClass().getName() + "'. A non-revision command is not expected in the command stack.");
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
					throw new ServerException(StatusDTO.RESP_500, e.getClass().getName() + " occurs when reverting a revision with a synchronization command. Message: " + e.getMessage());
				}
			}

			if (debug) {
				System.out.println("\t\t\trevertToRevision(" + toRevisionId + ") cachedRevisionId is reverted to " + this.cachedRevisionId);
			}
		} finally {
			indexItemsRWLock.writeLock().unlock();
		}
	}

	public void validate() throws ServerException {
		if (debug) {
			// System.out.println(getClassName() + ".validate()");
		}

		long totalWaitingTime = 0;
		while (isValidating) {
			if (totalWaitingTime > 10 * 1000) {
				// it should not take long to validate all index items. A total waiting of 10 seconds should be long enough.
				System.err.println(getClassName() + ".validate() index items are being validated. After waiting for 10 seconds, current validation is cancelled.");
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

				if (totalRetryTime > 60 * 1000) {
					System.err.println("\t\t\tvalidate() index items are being updated during loading of index items from database. After retried for 60 seconds, current validation is cancelled.");
					return;
				}

				try {
					if (debug) {
						System.out.println("\t\t\tvalidate() index items are being updated from database. Wait for 10 seconds...");
					}
					Thread.sleep(10 * 1000);
					totalRetryTime += (10 * 1000);
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
						System.out.println(getClassName() + ".validate() cached data (cachedRevisionId=" + cachedRevisionIdThen + ") are in-sync with database (latestRevisionId=" + latestRevisionIdNow + ").");
					} else {
						System.out.println(getClassName() + ".validate() cached data (cachedRevisionId=" + cachedRevisionIdThen + ") are out-of-sync with database (latestRevisionId=" + latestRevisionIdNow + ").");
						System.out.println(getClassName() + ".validate() outSyncedIndexItemIds are: " + Arrays.toString(outSyncedIndexItemIds.toArray(new Integer[outSyncedIndexItemIds.size()])));
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

	protected List<IndexItem> getIndexItems() throws ServerException {
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
	public List<IndexItem> getIndexItems(String indexProviderId) throws ServerException {
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
	public List<IndexItem> getIndexItems(String indexProviderId, String type) throws ServerException {
		this.indexItemsRWLock.readLock().lock();
		try {
			List<IndexItem> indexItems = new ArrayList<IndexItem>();
			for (IndexItem indexItem : this.cachedIndexItems) {
				if (indexItem.getIndexProviderId().equals(indexProviderId) && indexItem.getType().equals(type)) {
					indexItems.add(indexItem);
				}
			}
			return indexItems;
		} finally {
			this.indexItemsRWLock.readLock().unlock();
		}
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, Integer indexItemId) throws ServerException {
		this.indexItemsRWLock.readLock().lock();
		try {
			IndexItem result = null;
			for (IndexItem indexItem : this.cachedIndexItems) {
				if (indexItem.getIndexItemId().equals(indexItemId)) {
					result = indexItem;
					break;
				}
			}
			return result;
		} finally {
			this.indexItemsRWLock.readLock().unlock();
		}
	}

	@Override
	public IndexItem getIndexItem(String indexProviderId, String type, String name) throws ServerException {
		this.indexItemsRWLock.readLock().lock();
		try {
			IndexItem result = null;
			for (IndexItem indexItem : this.cachedIndexItems) {
				if (indexItem.getIndexProviderId().equals(indexProviderId) && indexItem.getType().equals(type) && indexItem.getName().equals(name)) {
					result = indexItem;
					break;
				}
			}
			return result;
		} finally {
			this.indexItemsRWLock.readLock().unlock();
		}
	}

	/**
	 * Check whether a property of an index item exists.
	 * 
	 * @param indexItemId
	 * @param propName
	 * @return
	 * @throws ServerException
	 */
	@Override
	public boolean hasProperty(String indexProviderId, Integer indexItemId, String propName) throws ServerException {
		if (debug) {
			System.out.println(getClassName() + ".hasProperty(" + indexItemId + ", '" + propName + "')");
		}
		if (indexItemId == null) {
			throw new IllegalArgumentException("indexItemId is null.");
		}
		if (propName == null) {
			throw new IllegalArgumentException("propName is null.");
		}

		this.indexItemsRWLock.readLock().lock();
		try {
			IndexItem matchedIndexItem = null;
			for (IndexItem indexItem : this.cachedIndexItems) {
				if (indexItemId.equals(indexItem.getIndexItemId())) {
					matchedIndexItem = indexItem;
					break;
				}
			}
			if (matchedIndexItem != null) {
				if (matchedIndexItem.hasProperty(propName) || matchedIndexItem.hasRuntimeProperty(propName)) {
					return true;
				}
			}
		} finally {
			this.indexItemsRWLock.readLock().unlock();
		}

		return false;
	}

	/**
	 * Get the values of all properties of an index item.
	 * 
	 * @param indexItemId
	 * @return
	 * @throws ServerException
	 */
	@Override
	public Map<String, Object> getProperties(String indexProviderId, Integer indexItemId) throws ServerException {
		if (debug) {
			System.out.println(getClassName() + ".getProperties(indexItemId=" + indexItemId + ")");
		}
		if (indexItemId == null) {
			throw new IllegalArgumentException("indexItemId is null.");
		}

		Map<String, Object> result = new HashMap<String, Object>();

		this.indexItemsRWLock.readLock().lock();
		try {
			IndexItem matchedIndexItem = null;
			for (IndexItem indexItem : this.cachedIndexItems) {
				if (indexItemId.equals(indexItem.getIndexItemId())) {
					matchedIndexItem = indexItem;
					break;
				}
			}
			if (matchedIndexItem != null) {
				Map<String, Object> properties = matchedIndexItem.getProperties();
				if (properties != null) {
					result.putAll(properties);
				}
				// runtime properties take the privilege. runtime properties overwrite other properties
				Map<String, Object> runtimeProperties = matchedIndexItem.getRuntimeProperties();
				if (runtimeProperties != null) {
					result.putAll(runtimeProperties);
				}
			}
		} finally {
			this.indexItemsRWLock.readLock().unlock();
		}

		return result;
	}

	/**
	 * Get the property value of an index item.
	 * 
	 * @param indexItemId
	 * @param propName
	 * @return
	 * @throws ServerException
	 */
	@Override
	public Object getProperty(String indexProviderId, Integer indexItemId, String propName) throws ServerException {
		if (debug) {
			System.out.println(getClassName() + ".getProperty(" + indexItemId + ", '" + propName + "')");
		}
		if (indexItemId == null) {
			throw new IllegalArgumentException("indexItemId is null.");
		}
		if (propName == null) {
			throw new IllegalArgumentException("propName is null.");
		}

		this.indexItemsRWLock.readLock().lock();
		try {
			IndexItem matchedIndexItem = null;
			for (IndexItem indexItem : this.cachedIndexItems) {
				if (indexItemId.equals(indexItem.getIndexItemId())) {
					matchedIndexItem = indexItem;
					break;
				}
			}
			if (matchedIndexItem != null) {
				// runtime properties take the privilege
				Object propValue = matchedIndexItem.getRuntimeProperty(propName);
				if (propValue == null) {
					propValue = matchedIndexItem.getProperty(propName);
				}
				return propValue;
			}
		} finally {
			this.indexItemsRWLock.readLock().unlock();
		}

		return null;
	}

	/**
	 * 
	 * @param indexProviderId
	 * @param requestCommand
	 * @param requestArguments
	 * @return
	 * @throws ServerException
	 */
	protected Object[] createRequest(String indexProviderId, String requestCommand, Map<String, Object> requestArguments) throws ServerException {
		// -------------------------------------------------------------------------------------------------------
		// Step1. Create a new request in database.
		// -------------------------------------------------------------------------------------------------------
		IndexItemRequestVO newRequestVO = getDatabaseHelper().createNewRequestInDatabase(this, indexProviderId, requestCommand, JSONUtil.toJsonString(requestArguments));
		final Integer requestId = newRequestVO.getRequestId();

		// -------------------------------------------------------------------------------------------------------
		// Step2. Start updating the request's lastUpdateTime every second.
		// -------------------------------------------------------------------------------------------------------
		Runnable requestUpdater = new Runnable() {
			@Override
			public void run() {
				System.out.println("Update request (requestId=" + requestId + ") lastUpdateTime.");
				getDatabaseHelper().updateRequestLastUpdateTimeInDatabase(IndexServiceDatabaseComplexImpl.this, requestId, new Date());
			}
		};
		// update request's lastUpdateTime every second. initial delay 1 second
		ScheduledFuture<?> requestUpdaterHandle = requestUpdatorScheduler.scheduleAtFixedRate(requestUpdater, 1, 1, SECONDS);

		try {
			// -------------------------------------------------------------------------------------------------------
			// Step3. Wait for previous active pending requests.
			// -------------------------------------------------------------------------------------------------------
			long totalWaitingTime = 0;
			// Check previous pending requests that are still active (last update time within 10 seconds).
			// Wait (for a max of 10 seconds) until no previous active pending requests exist.
			while (getDatabaseHelper().hasActivePendingRequestsInDatabase(this, requestId)) {
				// Stops waiting if total waiting time exceed 10 seconds. 10 seconds should be long enough for any single command to complete.
				if (totalWaitingTime > 60 * 1000) {
					// throw new IndexServiceException(IndexServiceConstants.INTERNAL_ERROR, "There are still active pending predecessor requests
					// after waiting for 10 seconds. Current request for creating new index item is cancelled.");
					if (debug) {
						System.err.println(getClassName() + ".createIndexItem() there are previous requests that are still active and pending after waiting for 60 seconds. Current request for creating new index item continues.");
						break;
					}
				}
				// Wait for half second and then check again.
				try {
					if (debug) {
						System.out.println(getClassName() + ".createIndexItem() there are previous requests that are still active and pending. Wait for 10 seconds...");
					}
					Thread.sleep(10 * 1000);
					totalWaitingTime += (10 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (ServerException e) {
			// Mark the request as cancelled and stops the requestUpdater.
			getDatabaseHelper().cancelRequestInDatabase(this, requestId, requestUpdaterHandle);
			throw e;
		}
		return new Object[] { requestId, requestUpdaterHandle };
	}

	protected void notifyOtherIndexServicesToSync() {
		try {
			String serviceName = getName();

			List<IndexItem> indexItems = getIndexItems(null, InfraConstants.INDEX_SERVICE_TYPE);
			for (IndexItem indexItem : indexItems) {
				String currName = indexItem.getName();
				String hostURL = (String) indexItem.getProperty(org.orbit.infra.api.InfraConstants.SERVICE__HOST_URL);
				String contextRoot = (String) indexItem.getProperty(org.orbit.infra.api.InfraConstants.SERVICE__CONTEXT_ROOT);
				Date lastHeartbeatTime = (Date) indexItem.getRuntimeProperty(org.orbit.infra.api.InfraConstants.SERVICE__LAST_HEARTBEAT_TIME);

				// do not notify itself
				if (serviceName != null && serviceName.equals(currName)) {
					// System.err.println("Ignore notifying current index service node '" + currName + "'.");
					continue;
				}

				// IndexServiceConfiguration config = contextRoot == null ? new IndexServiceConfiguration(hostURL) : new IndexServiceConfiguration(hostURL,
				// contextRoot);
				// final IndexService indexService = IndexServiceFactory.getInstance().createIndexService(config);
				// try {
				// indexService.sendCommand("sync", null);
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
			}
		} catch (ServerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IndexItem addIndexItem(String indexProviderId, String type, String name, Map<String, Object> properties) throws ServerException {
		boolean succeed = false;

		// -------------------------------------------------------------------------------------------------------
		// Step1. Create a new request record in database.
		// -------------------------------------------------------------------------------------------------------
		String requestCommand = RevisionCommand.CMD_CREATE_INDEX_ITEM;
		Map<String, Object> requestArguments = new HashMap<String, Object>();
		requestArguments.put(INDEX_ITEM_PROVIDER_ID_ATTR, indexProviderId);
		requestArguments.put(INDEX_ITEM_TYPE_ATTR, type);
		requestArguments.put(INDEX_ITEM_NAME_ATTR, name);
		requestArguments.put(INDEX_ITEM_PROPERTIES_ATTR, properties);

		Object[] requestResult = createRequest(indexProviderId, requestCommand, requestArguments);
		Integer requestId = (Integer) requestResult[0];
		ScheduledFuture<?> requestUpdaterHandle = (ScheduledFuture<?>) requestResult[1];

		try {
			IndexItemVO newIndexItemVO = null;
			IndexItemRevisionVO revisionVO = null;

			// -------------------------------------------------------------------------------------------------------
			// Step 2. Create index item record in database.
			// -------------------------------------------------------------------------------------------------------
			newIndexItemVO = getDatabaseHelper().createIndexItemInDatabase(this, indexProviderId, type, name, JSONUtil.toJsonString(properties));

			if (newIndexItemVO != null) {
				Integer indexItemId = newIndexItemVO.getIndexItemId();
				String newPropertiesString = newIndexItemVO.getPropertiesString();
				Date createTime = newIndexItemVO.getCreateTime();
				Date lastUpdateTime = newIndexItemVO.getLastUpdateTime();
				Map<String, Object> newProperties = JSONUtil.toProperties(newPropertiesString);

				// -------------------------------------------------------------------------------------------------------
				// Step 3. Create revision record in database.
				// -------------------------------------------------------------------------------------------------------
				// command and arguments (for creating index item)
				String command = RevisionCommand.CMD_CREATE_INDEX_ITEM;
				Map<String, Object> commandArguments = new HashMap<String, Object>();
				commandArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);
				commandArguments.put(INDEX_ITEM_PROVIDER_ID_ATTR, indexProviderId);
				commandArguments.put(INDEX_ITEM_TYPE_ATTR, type);
				commandArguments.put(INDEX_ITEM_NAME_ATTR, name);
				commandArguments.put(INDEX_ITEM_PROPERTIES_ATTR, newProperties);
				commandArguments.put(INDEX_ITEM_CREATE_TIME_ATTR, createTime);
				commandArguments.put(INDEX_ITEM_LAST_UPDATE_TIME_ATTR, lastUpdateTime);

				// undo command and arguments (for deleting index item)
				String undoCommand = RevisionCommand.CMD_DELETE_INDEX_ITEM;
				Map<String, Object> undoCommandArguments = new HashMap<String, Object>();
				undoCommandArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);

				revisionVO = getDatabaseHelper().createRevisionInDatabase(this, indexProviderId, command, commandArguments, undoCommand, undoCommandArguments);
			}

			if (newIndexItemVO != null && revisionVO != null) {
				IndexItem indexItem = IndexServiceDatabaseHelper.INSTANCE.toIndexItem(newIndexItemVO);
				return indexItem;
			}

		} catch (ServerException e) {
			// -------------------------------------------------------------------------------------------------------
			// Step 5. Cancel the request
			// -------------------------------------------------------------------------------------------------------
			// Mark the request as cancelled and stops the requestUpdater.
			getDatabaseHelper().cancelRequestInDatabase(this, requestId, requestUpdaterHandle);
			throw e;

		} finally {
			if (succeed) {
				// -------------------------------------------------------------------------------------------------------
				// Step 4. Complete the request
				// -------------------------------------------------------------------------------------------------------
				// Mark the request as completed and stops the requestUpdater.
				getDatabaseHelper().completeRequestInDatabase(this, requestId, requestUpdaterHandle);

				// Synchronize with database again.
				// Reason to synchronize is that (1) the database may have been updated by other index services, while the current index service was
				// waiting for their requests to complete. (2) the current index service create record in index item table and revision table. the
				// cached index items and cached revisionId still need to be updated by doing the synchronization.
				// synchronize();

				// Notify other index services to synchronize
				notifyOtherIndexServicesToSync();

			} else {
				// -------------------------------------------------------------------------------------------------------
				// Step 5. Cancel the request
				// -------------------------------------------------------------------------------------------------------
				// Mark the request as cancelled and stops the requestUpdater.
				getDatabaseHelper().cancelRequestInDatabase(this, requestId, requestUpdaterHandle);
			}
		}

		return null;
	}

	/**
	 * Remove an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @throws ServerException
	 */
	@Override
	public boolean removeIndexItem(String indexProviderId, Integer indexItemId) throws ServerException {
		boolean succeed = false;

		// 1. Retrieve index item data from database.
		IndexItemVO indexItemVO = getDatabaseHelper().getIndexItemFromDatabase(this, indexItemId);
		if (indexItemVO == null) {
			if (debug) {
				System.out.println(getClassName() + ".removeIndexItem() IndexItem (indexItemId=" + indexItemId + ")  cannot be found in database.");
			}
			return false;
		}
		// String indexProviderId = indexItemVO.getIndexProviderId();
		String type = indexItemVO.getType();
		String name = indexItemVO.getName();
		String propertiesString = indexItemVO.getPropertiesString();
		Date createTime = indexItemVO.getCreateTime();
		Date lastUpdateTime = indexItemVO.getLastUpdateTime();
		Map<String, Object> properties = JSONUtil.toProperties(propertiesString);

		// 2. Create a new request record in database.
		String requestCommand = RevisionCommand.CMD_DELETE_INDEX_ITEM;
		Map<String, Object> requestArguments = new HashMap<String, Object>();
		requestArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);

		Object[] requestResult = createRequest(indexProviderId, requestCommand, requestArguments);
		Integer requestId = (Integer) requestResult[0];
		ScheduledFuture<?> requestUpdaterHandle = (ScheduledFuture<?>) requestResult[1];

		try {
			boolean deleted = false;
			IndexItemRevisionVO revisionVO = null;

			// 3. Delete index item record in database.
			deleted = getDatabaseHelper().deleteIndexItemInDatababse(this, indexItemId);

			// 4. Create revision record in database.
			if (deleted) {
				// command and arguments (for deleting index item)
				String command = RevisionCommand.CMD_DELETE_INDEX_ITEM;
				Map<String, Object> commandArguments = new HashMap<String, Object>();
				commandArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);

				// undo command and arguments (for creating index item)
				String undoCommand = RevisionCommand.CMD_CREATE_INDEX_ITEM;
				Map<String, Object> undoCommandArguments = new HashMap<String, Object>();
				undoCommandArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);
				undoCommandArguments.put(INDEX_ITEM_PROVIDER_ID_ATTR, indexProviderId);
				undoCommandArguments.put(INDEX_ITEM_TYPE_ATTR, type);
				undoCommandArguments.put(INDEX_ITEM_NAME_ATTR, name);
				undoCommandArguments.put(INDEX_ITEM_PROPERTIES_ATTR, properties);
				undoCommandArguments.put(INDEX_ITEM_CREATE_TIME_ATTR, createTime);
				undoCommandArguments.put(INDEX_ITEM_LAST_UPDATE_TIME_ATTR, lastUpdateTime);

				revisionVO = getDatabaseHelper().createRevisionInDatabase(this, indexProviderId, command, commandArguments, undoCommand, undoCommandArguments);
			}

			if (deleted && revisionVO != null) {
				succeed = true;
			}

		} catch (ServerException e) {
			// Mark the request as cancelled and stops the requestUpdater.
			getDatabaseHelper().cancelRequestInDatabase(this, requestId, requestUpdaterHandle);
			throw e;

		} finally {
			if (succeed) {
				// Mark the request as completed and stops the requestUpdater.
				getDatabaseHelper().completeRequestInDatabase(this, requestId, requestUpdaterHandle);

				// Synchronize with database again.
				// synchronize();

				// Notify other index services to synchronize
				notifyOtherIndexServicesToSync();

			} else {
				// Mark the request as cancelled and stops the requestUpdater.
				getDatabaseHelper().cancelRequestInDatabase(this, requestId, requestUpdaterHandle);
			}
		}

		return succeed;
	}

	/**
	 * Set the properties of an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param properties
	 * @throws ServerException
	 */
	@Override
	public boolean setProperties(String indexProviderId, Integer indexItemId, Map<String, Object> properties) throws ServerException {
		boolean succeed = false;

		// 1. Retrieve index item data from database.
		IndexItemVO indexItemVO = getDatabaseHelper().getIndexItemFromDatabase(this, indexItemId);
		if (indexItemVO == null) {
			if (debug) {
				System.out.println(getClassName() + ".setProperty() IndexItem (indexItemId=" + indexItemId + ")  cannot be found in database.");
			}
			return false;
		}
		if (properties == null || properties.isEmpty()) {
			if (debug) {
				System.out.println(getClassName() + ".setProperty() IndexItem (indexItemId=" + indexItemId + ")  properties are empty.");
			}
			return false;
		}
		// String indexProviderId = indexItemVO.getIndexProviderId();
		String propertiesString = indexItemVO.getPropertiesString();
		Date oldLastUpdateTime = indexItemVO.getLastUpdateTime();
		Map<String, Object> oldProperties = JSONUtil.toProperties(propertiesString);

		Map<String, Object> newProperties = new HashMap<String, Object>();
		newProperties.putAll(oldProperties);
		newProperties.putAll(properties);

		// 2. Create a new request record in database.
		String requestCommand = RevisionCommand.CMD_UPDATE_INDEX_ITEM;
		Map<String, Object> requestArguments = new HashMap<String, Object>();
		requestArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);
		requestArguments.put(INDEX_ITEM_PROPERTIES_ATTR, properties);

		Object[] requestResult = createRequest(indexProviderId, requestCommand, requestArguments);
		Integer requestId = (Integer) requestResult[0];
		ScheduledFuture<?> requestUpdaterHandle = (ScheduledFuture<?>) requestResult[1];

		try {
			Date newLastUpdateTime = new Date();
			IndexItemRevisionVO revisionVO = null;

			// 3. Update index item record in database.
			boolean updated = getDatabaseHelper().updateIndexItemPropertiesInDatabase(this, indexItemId, newProperties, newLastUpdateTime);

			// 4. Create revision record in database.
			if (updated) {
				// command and arguments (for updating index item properties)
				String command = RevisionCommand.CMD_UPDATE_INDEX_ITEM;
				Map<String, Object> commandArguments = new HashMap<String, Object>();
				commandArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);
				commandArguments.put(INDEX_ITEM_PROPERTIES_ATTR, oldProperties);
				commandArguments.put(INDEX_ITEM_LAST_UPDATE_TIME_ATTR, oldLastUpdateTime);

				// undo command and arguments (for reverting changes to the index item properties)
				String undoCommand = RevisionCommand.CMD_UPDATE_INDEX_ITEM;
				Map<String, Object> undoCommandArguments = new HashMap<String, Object>();
				undoCommandArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);
				commandArguments.put(INDEX_ITEM_PROPERTIES_ATTR, newProperties);
				commandArguments.put(INDEX_ITEM_LAST_UPDATE_TIME_ATTR, newLastUpdateTime);

				revisionVO = getDatabaseHelper().createRevisionInDatabase(this, indexProviderId, command, commandArguments, undoCommand, undoCommandArguments);
			}

			if (updated && revisionVO != null) {
				succeed = true;
			}

		} catch (ServerException e) {
			// Mark the request as cancelled and stops the requestUpdater.
			getDatabaseHelper().cancelRequestInDatabase(this, requestId, requestUpdaterHandle);
			throw e;

		} finally {
			if (succeed) {
				// Mark the request as completed and stops the requestUpdater.
				getDatabaseHelper().completeRequestInDatabase(this, requestId, requestUpdaterHandle);

				// Synchronize with database again.
				// synchronize();

				// Notify other index services to synchronize
				notifyOtherIndexServicesToSync();

			} else {
				// Mark the request as cancelled and stops the requestUpdater.
				getDatabaseHelper().cancelRequestInDatabase(this, requestId, requestUpdaterHandle);
			}
		}

		return succeed;
	}

	/**
	 * Remove the properties of an index item.
	 * 
	 * @param indexProviderId
	 * @param indexItemId
	 * @param propNames
	 * @return
	 * @throws ServerException
	 */
	@Override
	public boolean removeProperty(String indexProviderId, Integer indexItemId, List<String> propNames) throws ServerException {
		boolean succeed = false;

		// 1. Retrieve index item data from database.
		IndexItemVO indexItemVO = getDatabaseHelper().getIndexItemFromDatabase(this, indexItemId);
		if (indexItemVO == null) {
			if (debug) {
				System.out.println(getClassName() + ".removeProperty() IndexItem (indexItemId=" + indexItemId + ")  cannot be found in database.");
			}
			return false;
		}
		if (propNames == null || propNames.isEmpty()) {
			if (debug) {
				System.out.println(getClassName() + ".removeProperty() IndexItem's (indexItemId=" + indexItemId + ")  property names are empty.");
			}
			return false;
		}
		// String indexProviderId = indexItemVO.getIndexProviderId();
		String propertiesString = indexItemVO.getPropertiesString();
		Date oldLastUpdateTime = indexItemVO.getLastUpdateTime();
		Map<String, Object> oldAllProperties = JSONUtil.toProperties(propertiesString);

		Map<String, Object> newAllProperties = new HashMap<String, Object>();
		newAllProperties.putAll(oldAllProperties);
		for (String propName : propNames) {
			newAllProperties.remove(propName);
		}

		// 2. Create a new request record in database.
		Map<String, Object> requestArgsProperties = new HashMap<String, Object>();
		for (String propName : propNames) {
			Object propValue = oldAllProperties.get(propName);
			requestArgsProperties.put(propName, propValue);
		}

		String requestCommand = RevisionCommand.CMD_UPDATE_INDEX_ITEM;
		Map<String, Object> requestArguments = new HashMap<String, Object>();
		requestArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);
		requestArguments.put(INDEX_ITEM_PROPERTIES_ATTR, requestArgsProperties);

		Object[] requestResult = createRequest(indexProviderId, requestCommand, requestArguments);
		Integer requestId = (Integer) requestResult[0];
		ScheduledFuture<?> requestUpdaterHandle = (ScheduledFuture<?>) requestResult[1];

		try {
			boolean updated = false;
			Date newLastUpdateTime = new Date();
			IndexItemRevisionVO revisionVO = null;

			// 3. Update index item record in database.
			updated = getDatabaseHelper().updateIndexItemPropertiesInDatabase(this, indexItemId, newAllProperties, newLastUpdateTime);

			// 4. Create revision record in database.
			if (updated) {
				// command and arguments (for updating index item properties)
				String command = RevisionCommand.CMD_UPDATE_INDEX_ITEM;
				Map<String, Object> commandArguments = new HashMap<String, Object>();
				commandArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);
				commandArguments.put(INDEX_ITEM_PROPERTIES_ATTR, oldAllProperties);
				commandArguments.put(INDEX_ITEM_LAST_UPDATE_TIME_ATTR, oldLastUpdateTime);

				// undo command and arguments (for reverting changes to the index item properties)
				String undoCommand = RevisionCommand.CMD_UPDATE_INDEX_ITEM;
				Map<String, Object> undoCommandArguments = new HashMap<String, Object>();
				undoCommandArguments.put(INDEX_ITEM_ID_ATTR, indexItemId);
				commandArguments.put(INDEX_ITEM_PROPERTIES_ATTR, newAllProperties);
				commandArguments.put(INDEX_ITEM_LAST_UPDATE_TIME_ATTR, newLastUpdateTime);

				revisionVO = getDatabaseHelper().createRevisionInDatabase(this, indexProviderId, command, commandArguments, undoCommand, undoCommandArguments);
			}

			if (updated && revisionVO != null) {
				succeed = true;
			}

		} catch (ServerException e) {
			// Mark the request as cancelled and stops the requestUpdater.
			getDatabaseHelper().cancelRequestInDatabase(this, requestId, requestUpdaterHandle);
			throw e;

		} finally {
			if (succeed) {
				// Mark the request as completed and stops the requestUpdater.
				getDatabaseHelper().completeRequestInDatabase(this, requestId, requestUpdaterHandle);

				// Synchronize with database again.
				// synchronize();

				// Notify other index services to synchronize
				notifyOtherIndexServicesToSync();

			} else {
				// Mark the request as cancelled and stops the requestUpdater.
				getDatabaseHelper().cancelRequestInDatabase(this, requestId, requestUpdaterHandle);
			}
		}

		return succeed;
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
	public void addCachedIndexItem(IndexItem indexItem) throws ServerException {
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
				throw new ServerException(ERROR_CODE_INDEX_ITEM_EXIST, "Index item with id '" + indexItem.getIndexItemId() + "' already exists.");
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
	public void removeCachedIndexItem(Integer indexItemId) throws ServerException {
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
				throw new ServerException(ERROR_CODE_INDEX_ITEM_NOT_FOUND, "Index item with id '" + indexItemId + "' is not found.");
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
	public void updateCachedIndexItemProperties(Integer indexItemId, Map<String, Object> properties, Date lastUpdateTime) throws ServerException {
		if (debug) {
			// System.out.println(getClassName() + ".udpateCachedIndexItem()");
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
				throw new ServerException(ERROR_CODE_INDEX_ITEM_NOT_FOUND, "Index item with id '" + indexItemId + "' is not found.");
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

	// ------------------------------------------------------------------------------------------------------------
	// helper methods called by validate() method
	// ------------------------------------------------------------------------------------------------------------
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
		String type1 = indexItemSnapshot.getType();
		String name1 = indexItemSnapshot.getName();
		Map<String, Object> properties1 = indexItemSnapshot.getProperties();
		Date createTime1 = indexItemSnapshot.getCreateTime();
		Date lastUpdateTime1 = indexItemSnapshot.getLastUpdateTime();

		Integer indexItemId2 = indexItemFromDatabase.getIndexItemId();
		String indexProviderId2 = indexItemFromDatabase.getIndexProviderId();
		String type2 = indexItemFromDatabase.getType();
		String name2 = indexItemFromDatabase.getName();
		Map<String, Object> properties2 = indexItemFromDatabase.getProperties();
		Date createTime2 = indexItemFromDatabase.getCreateTime();
		Date lastUpdateTime2 = indexItemFromDatabase.getLastUpdateTime();

		boolean matchIndexItemId = CompareUtil.equals(indexItemId1, indexItemId2, false);
		boolean matchIndexProviderId = CompareUtil.equals(indexProviderId1, indexProviderId2, true);
		boolean matchType = CompareUtil.equals(type1, type2, true);
		boolean matchName = CompareUtil.equals(name1, name2, true);
		boolean matchProperties = CompareUtil.equals(properties1, properties2, true);
		boolean matchCreateTime = CompareUtil.equals(createTime1, createTime2, true);
		boolean matchLastUpdateTime = CompareUtil.equals(lastUpdateTime1, lastUpdateTime2, true);

		if (matchIndexItemId && matchIndexProviderId && matchType && matchName && matchProperties && matchCreateTime && matchLastUpdateTime) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param cachedIndexItem
	 * @param indexItemFromDatabase
	 * @return
	 * @throws ServerException
	 */
	private boolean update(IndexItem cachedIndexItem, IndexItem indexItemFromDatabase) throws ServerException {
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
		String type1 = cachedIndexItem.getType();
		String name1 = cachedIndexItem.getName();
		Map<String, Object> properties1 = cachedIndexItem.getProperties();
		Date createTime1 = cachedIndexItem.getCreateTime();
		Date lastUpdateTime1 = cachedIndexItem.getLastUpdateTime();

		Integer indexItemId2 = indexItemFromDatabase.getIndexItemId();
		String indexProviderId2 = indexItemFromDatabase.getIndexProviderId();
		String type2 = indexItemFromDatabase.getType();
		String name2 = indexItemFromDatabase.getName();
		Map<String, Object> properties2 = indexItemFromDatabase.getProperties();
		Date createTime2 = indexItemFromDatabase.getCreateTime();
		Date lastUpdateTime2 = indexItemFromDatabase.getLastUpdateTime();

		boolean matchIndexItemId = CompareUtil.equals(indexItemId1, indexItemId2, false);
		if (!matchIndexItemId) {
			throw new ServerException(StatusDTO.RESP_500, "Cannot update cached index item (indexItemId=" + indexItemId1 + ") with an index item from database with different index item id (indexItemId=" + indexItemId2 + ").");
		}

		boolean matchIndexProviderId = CompareUtil.equals(indexProviderId1, indexProviderId2, true);
		if (!matchIndexProviderId) {
			cachedIndexItem.setIndexProviderId(indexProviderId2);
			isUpdated = true;
		}

		boolean matchType = CompareUtil.equals(type1, type2, true);
		if (!matchType) {
			cachedIndexItem.setType(type2);
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
