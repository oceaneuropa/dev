package org.origin.mgm.client.loadbalance;

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
		setId(indexService.getConfiguration().getUrl()); // use IndexService URL as its id
	}

}
