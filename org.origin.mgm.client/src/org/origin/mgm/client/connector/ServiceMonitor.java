package org.origin.mgm.client.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.util.Timer;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexItemsMonitor;
import org.origin.mgm.client.api.IndexService;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;

public abstract class ServiceMonitor implements Annotated {

	/* update index items every 15 seconds */
	public static long INDEX_MONITOR_INTERVAL = 15 * Timer.SECOND;

	/* actively ping the service every 5 seconds */
	public static long ACTIVE_PING_INTERVAL = 5 * Timer.SECOND;

	protected IndexService indexService;
	protected IndexItemsMonitor indexItemsMonitor;
	protected List<IndexItem> indexItems = new ArrayList<IndexItem>();
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	@Dependency
	protected ConfigurationAdmin configAdmin;

	/**
	 * 
	 * @param indexService
	 */
	public ServiceMonitor(IndexService indexService) {
		this.indexService = indexService;
	}

	public IndexService getIndexService() {
		return indexService;
	}

	public synchronized void start(BundleContext bundleContext) {
		if (this.isStarted.get()) {
			return;
		}
		this.isStarted.set(true);

		// Start monitor to periodically get index items for a service
		this.indexItemsMonitor = new IndexItemsMonitor(getClass().getSimpleName() + " Monitor", this.indexService) {
			@Override
			protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
				return ServiceMonitor.this.getIndexItems(indexService);
			}

			@Override
			protected void indexItemsUpdated(List<IndexItem> indexItems) {
				ServiceMonitor.this.indexItemsUpdated(indexItems);
			}
		};
		this.indexItemsMonitor.setInterval(INDEX_MONITOR_INTERVAL);
		this.indexItemsMonitor.start();

		// Register Annotated service for dependency injection
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	public synchronized void stop(BundleContext bundleContext) {
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		// Unregister Annotated service
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);

		// Stop monitor
		if (this.indexItemsMonitor != null) {
			this.indexItemsMonitor.stop();
			this.indexItemsMonitor = null;
		}
	}

	/**
	 * 
	 * @param indexService
	 * @return
	 * @throws IOException
	 */
	protected abstract List<IndexItem> getIndexItems(IndexService indexService) throws IOException;

	protected synchronized void indexItemsUpdated(List<IndexItem> newIndexItems) {
		Map<Integer, IndexItem> oldIndexItemsMap = new HashMap<Integer, IndexItem>();
		for (IndexItem oldIndexItem : indexItems) {
			Integer indexItemId = oldIndexItem.getIndexItemId();
			oldIndexItemsMap.put(indexItemId, oldIndexItem);
		}

		Map<Integer, IndexItem> newIndexItemsMap = new HashMap<Integer, IndexItem>();
		for (IndexItem newIndexItem : newIndexItems) {
			Integer newIndexItemId = newIndexItem.getIndexItemId();
			newIndexItemsMap.put(newIndexItemId, newIndexItem);
		}

		List<IndexItem> removedIndexItems = new ArrayList<IndexItem>();
		List<IndexItem> updatedIndexItems = new ArrayList<IndexItem>();
		List<IndexItem> addedIndexItems = new ArrayList<IndexItem>();

		for (IndexItem oldIndexItem : indexItems) {
			Integer oldIndexItemId = oldIndexItem.getIndexItemId();
			IndexItem newIndexItem = newIndexItemsMap.get(oldIndexItemId);
			if (newIndexItem != null) {
				if (!equals(oldIndexItem, newIndexItem)) {
					updatedIndexItems.add(newIndexItem);
				}
			} else {
				removedIndexItems.add(oldIndexItem);
			}
		}

		for (IndexItem newIndexItem : newIndexItems) {
			Integer newIndexItemId = newIndexItem.getIndexItemId();
			IndexItem oldIndexItem = oldIndexItemsMap.get(newIndexItemId);
			if (oldIndexItem == null) {
				addedIndexItems.add(newIndexItem);
			}
		}

		this.indexItems.clear();
		this.indexItems.addAll(newIndexItems);

		for (IndexItem removedIndexItem : removedIndexItems) {
			indexItemRemoved(removedIndexItem);
		}

		for (IndexItem updatedIndexItem : updatedIndexItems) {
			indexItemUpdated(updatedIndexItem);
		}

		for (IndexItem addedIndexItem : addedIndexItems) {
			indexItemAdded(addedIndexItem);
		}
	}

	protected void indexItemRemoved(IndexItem indexItem) {

	}

	protected void indexItemUpdated(IndexItem indexItem) {

	}

	protected void indexItemAdded(IndexItem indexItem) {

	}

	/**
	 * 
	 * @param oldIndexItem
	 * @param newIndexItem
	 * @return
	 */
	private boolean equals(IndexItem oldIndexItem, IndexItem newIndexItem) {
		if (oldIndexItem != null && newIndexItem != null) {
			Integer oldIndexItemId = oldIndexItem.getIndexItemId();
			String oldProviderId = oldIndexItem.getIndexProviderId();
			String oldName = oldIndexItem.getName();
			String oldType = oldIndexItem.getType();
			Map<String, Object> oldProps = oldIndexItem.getProperties();
			if (oldProviderId == null) {
				oldProviderId = "";
			}
			if (oldName == null) {
				oldName = "";
			}
			if (oldType == null) {
				oldType = "";
			}
			if (oldProps == null) {
				oldProps = new HashMap<String, Object>();
			}

			Integer newIndexItemId = newIndexItem.getIndexItemId();
			String newProviderId = newIndexItem.getIndexProviderId();
			String newName = newIndexItem.getName();
			String newType = newIndexItem.getType();
			Map<String, Object> newProps = newIndexItem.getProperties();
			if (newProviderId == null) {
				newProviderId = "";
			}
			if (newName == null) {
				newName = "";
			}
			if (newType == null) {
				newType = "";
			}
			if (newProps == null) {
				newProps = new HashMap<String, Object>();
			}

			if (oldIndexItemId.equals(newIndexItemId)) {
				if (oldProviderId.equals(newProviderId) //
						&& oldName.equals(newName) //
						&& oldType.equals(newType) //
						&& oldProps.equals(newProps) //
				) {
					return true;
				}
			}
		}
		return false;
	}

}
