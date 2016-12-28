package org.orbit.component.server.configregistry.service;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.server.Activator;
import org.orbit.component.server.OrbitConstants;
import org.origin.common.loadbalance.LoadBalanceService;
import org.origin.common.thread.ThreadPoolTimer;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexProvider;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;

public class ConfigRegistryServiceTimer extends ThreadPoolTimer {

	protected String hostURL;
	protected String contextRoot;
	protected String componentName;
	protected IndexProviderLoadBalancer indexProviderLoadBalancer;

	/**
	 * 
	 * @param hostURL
	 * @param contextRoot
	 * @param componentName
	 * @param indexProviderLoadBalancer
	 */
	public ConfigRegistryServiceTimer(String hostURL, String contextRoot, String componentName, IndexProviderLoadBalancer indexProviderLoadBalancer) {
		super("ConfigRegistry Service Timer");

		this.hostURL = hostURL;
		this.contextRoot = contextRoot;
		this.componentName = componentName;
		this.indexProviderLoadBalancer = indexProviderLoadBalancer;

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				updateIndexItem();
			}
		};
		setRunnable(runnable);
	}

	protected void updateIndexItem() {
		ConfigRegistryService configRegistryService = Activator.getConfigRegistryService();
		LoadBalanceService<IndexProvider> lbServices = this.indexProviderLoadBalancer.getNext();
		if (configRegistryService != null && lbServices != null) {
			IndexProvider indexProvider = lbServices.getService();
			if (indexProvider != null) {
				try {
					IndexItem indexItem = indexProvider.getIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE, this.componentName);
					if (indexItem == null) {
						// create new index item
						Map<String, Object> props = new Hashtable<String, Object>();
						props.put(OrbitConstants.INDEX_ITEM_URL_PROP, this.hostURL);
						props.put(OrbitConstants.INDEX_ITEM_CONTEXT_ROOT_PROP, this.contextRoot);
						props.put(OrbitConstants.INDEX_ITEM_LAST_HEARTBEAT_TIME_PROP, new Date());

						indexProvider.addIndexItem(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE, this.componentName, props);

					} else {
						// update existing index item
						Integer indexItemId = indexItem.getIndexItemId();
						Map<String, Object> props = new Hashtable<String, Object>();
						props.put(OrbitConstants.INDEX_ITEM_URL_PROP, this.hostURL);
						props.put(OrbitConstants.INDEX_ITEM_CONTEXT_ROOT_PROP, this.contextRoot);
						props.put(OrbitConstants.INDEX_ITEM_LAST_HEARTBEAT_TIME_PROP, new Date());

						indexProvider.setProperties(indexItemId, props);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
