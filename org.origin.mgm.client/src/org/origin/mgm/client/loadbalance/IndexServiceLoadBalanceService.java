package org.origin.mgm.client.loadbalance;

import java.util.Date;

import org.origin.common.loadbalance.AbstractLoadBalanceService;
import org.origin.mgm.client.api.IndexService;

/**
 * @see org.apache.activemq.transport.AbstractInactivityMonitor
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class IndexServiceLoadBalanceService extends AbstractLoadBalanceService<IndexService> {

	/**
	 * 
	 * @param indexService
	 */
	public IndexServiceLoadBalanceService(IndexService indexService) {
		super(indexService);
		// use IndexService URL as its id
		setId(indexService.getConfiguration().getUrl());
	}

	/**
	 * Monitor the IndexService.
	 * 
	 * @param indexService
	 */
	@Override
	protected void monitor(IndexService indexService) {
		int result = indexService.ping();
		setProperty("ping", result);
		setProperty("last_ping_time", new Date());
	}

}
