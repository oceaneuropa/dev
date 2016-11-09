package org.origin.mgm.client.loadbalance;

import org.origin.common.loadbalance.AbstractLoadBalanceService;
import org.origin.mgm.client.api.IndexProvider;

public class IndexProviderLoadBalanceService extends AbstractLoadBalanceService<IndexProvider> {

	/**
	 * 
	 * @param indexProvider
	 */
	public IndexProviderLoadBalanceService(IndexProvider indexProvider) {
		super(indexProvider);
		setId(indexProvider.getConfiguration().getUrl()); // use IndexProvider URL as its id
	}

}
