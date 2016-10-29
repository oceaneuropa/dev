package org.origin.mgm.client.loadbalance;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.origin.common.loadbalance.LoadBalanceService;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.thread.ThreadPoolTimer;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexService;

public class IndexServiceLoadBalancer extends LoadBalancer<IndexService> {

	public static final String INDEX_SERVICE_INDEXER_ID = "component.indexservice.indexer";

	protected ThreadPoolTimer indexServicesMonitor;
	protected long monitoringInterval = -1;

	/**
	 * 
	 * @param indexServices
	 */
	public IndexServiceLoadBalancer(List<LoadBalanceService<IndexService>> indexServices) {
		super(indexServices);
	}

	public long getMonitoringInterval() {
		return this.monitoringInterval;
	}

	public void setMonitoringInterval(long monitorInterval) {
		this.monitoringInterval = monitorInterval;
	}

	@Override
	public void start() {
		super.start();

		// Start checking the index items for index services themselves using the IndexService API.
		// e.g. a remote IndexService is added to the indexing
		// e.g. a remote IndexService is removed from the indexing
		// For the refreshing of the properties of existing index items, the corresponding LoadBalanceService will do that by calling its referenced IndexService.
		if (this.indexServicesMonitor != null) {
			this.indexServicesMonitor.stop();
		}
		this.indexServicesMonitor = new ThreadPoolTimer("IndexServiceLoadBalancer(IndexService) Monitor", new Runnable() {
			@Override
			public void run() {
				monitor();
			}
		});
		if (this.monitoringInterval > 0) {
			this.indexServicesMonitor.setInterval(this.monitoringInterval);
		}
		this.indexServicesMonitor.start();
	}

	@Override
	public void stop() {
		if (this.indexServicesMonitor != null) {
			this.indexServicesMonitor.stop();
			this.indexServicesMonitor = null;
		}

		super.stop();
	}

	protected void monitor() {
		LoadBalanceService<IndexService> lbService = getFirstAvailableService();
		if (lbService == null) {
			return;
		}

		try {
			List<IndexItem> indexItems = lbService.getService().getIndexItems(INDEX_SERVICE_INDEXER_ID);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected LoadBalanceService<IndexService> getFirstAvailableService() {
		if (this.services.isEmpty()) {
			return null;
		} else if (this.services.size() == 1) {
			return this.services.get(0);
		}

		int latestPingIndex = -1;
		Date latestPingTime = null;
		for (int i = 0; i < this.services.size(); i++) {
			LoadBalanceService<IndexService> lbService = this.services.get(i);
			if (lbService.hasProperty("ping") && lbService.hasProperty("last_ping_time")) {
				int ping = (int) lbService.getProperty("ping");
				if (ping > 0) {
					Date currPingTime = (Date) lbService.getProperty("last_ping_time");
					if (latestPingTime == null || currPingTime.after(latestPingTime)) {
						latestPingTime = currPingTime;
						latestPingIndex = i;
					}
				}
			}
		}
		if (latestPingIndex >= 0) {
			return this.services.get(latestPingIndex);
		}

		// none of the services have valid ping result.
		return this.services.get(0);
	}

}
