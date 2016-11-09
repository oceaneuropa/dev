package org.origin.mgm.client.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.origin.common.loadbalance.LoadBalanceService;
import org.origin.common.thread.ThreadPoolTimer;
import org.origin.mgm.client.loadbalance.IndexServiceLoadBalancer;

public abstract class IndexItemsMonitor extends ThreadPoolTimer {

	protected IndexServiceLoadBalancer indexServiceLoadBalancer;
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
	 * @param indexServiceLoadBalancer
	 */
	public IndexItemsMonitor(String name, IndexServiceLoadBalancer indexServiceLoadBalancer) {
		super(name);
		this.indexServiceLoadBalancer = indexServiceLoadBalancer;

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				syncIndexItems();
			}
		};
		setRunnable(runnable);
	}

	public IndexServiceLoadBalancer getIndexServiceLoadBalancer() {
		return indexServiceLoadBalancer;
	}

	public void setIndexServiceLoadBalancer(IndexServiceLoadBalancer indexServiceLoadBalancer) {
		this.indexServiceLoadBalancer = indexServiceLoadBalancer;
	}

	public List<IndexItem> getIndexItems() {
		return this.cachedIndexItems;
	}

	protected synchronized void syncIndexItems() {
		List<IndexItem> newIndexItems = null;
		boolean retrieved = false;

		LoadBalanceService<IndexService> lbServices = null;
		if (this.indexServiceLoadBalancer != null) {
			lbServices = this.indexServiceLoadBalancer.getNext();
			if (lbServices != null) {
				IndexService indexService = lbServices.getService();
				if (indexService != null) {
					try {
						newIndexItems = getIndexItems(indexService);
						retrieved = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		if (retrieved) {
			// update cached index items
			if (newIndexItems != null) {
				this.cachedIndexItems = newIndexItems;
			} else {
				this.cachedIndexItems.clear();
			}
		} else {
			// ignore updating the cached index items
		}
	}

	protected abstract List<IndexItem> getIndexItems(IndexService indexService) throws IOException;

}
