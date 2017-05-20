package org.origin.mgm.client.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.origin.common.thread.ThreadPoolTimer;

public abstract class IndexItemsMonitor extends ThreadPoolTimer {

	protected IndexService indexService;
	protected List<IndexItem> cachedIndexItems = new ArrayList<IndexItem>();

	/**
	 * 
	 * @param name
	 */
	public IndexItemsMonitor(String name) {
		this(name, null);
	}

	/**
	 * 
	 * @param name
	 * @param indexService
	 */
	public IndexItemsMonitor(String name, IndexService indexService) {
		super(name);
		this.indexService = indexService;

		setRunnable(new Runnable() {
			@Override
			public void run() {
				boolean performed = syncIndexItems();
				if (performed) {
					indexItemsUpdated(IndexItemsMonitor.this.cachedIndexItems);
				}
			}
		});
	}

	public List<IndexItem> getLoadedIndexItems() {
		return this.cachedIndexItems;
	}

	protected synchronized boolean syncIndexItems() {
		boolean performed = false;

		List<IndexItem> newIndexItems = null;
		if (indexService != null) {
			try {
				newIndexItems = getIndexItems(indexService);
				performed = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (performed) {
			// update cached index items
			if (newIndexItems != null) {
				this.cachedIndexItems = newIndexItems;
			} else {
				this.cachedIndexItems.clear();
			}
		} else {
			// ignore updating the cached index items when the action of getting index items doesn't happen.
			// e.g. indexing service is down due to network changes or or connection failure or server hardware failure.
			// when that happens, the cachedIndexItems remains the value it has
		}

		return performed;
	}

	/**
	 * Called when trying to update index items.
	 * 
	 * @param indexService
	 * @return
	 * @throws IOException
	 */
	protected abstract List<IndexItem> getIndexItems(IndexService indexService) throws IOException;

	/**
	 * Called when index items are updated.
	 * 
	 * @param indexItems
	 */
	protected abstract void indexItemsUpdated(List<IndexItem> indexItems);

}