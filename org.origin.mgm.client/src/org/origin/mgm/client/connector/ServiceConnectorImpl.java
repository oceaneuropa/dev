package org.origin.mgm.client.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalanceResourceImpl;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.loadbalance.policy.LoadBalancePolicy;
import org.origin.common.loadbalance.policy.RoundRobinLoadBalancePolicy;
import org.origin.mgm.client.api.IndexItem;
import org.origin.mgm.client.api.IndexItemsMonitor;
import org.origin.mgm.client.api.IndexService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public abstract class ServiceConnectorImpl<S> implements ServiceConnector<S> {

	public static String INDEX_ITEM_ID = "index_item_id";

	protected IndexService indexService;
	protected Class<?> connectorInterface;
	protected LoadBalancer<S> loadBalancer;

	protected IndexItemsMonitor indexItemsMonitor;
	protected ServiceRegistration<?> serviceRegistration;

	protected ReadWriteLock rwLock = new ReentrantReadWriteLock();
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	/**
	 * 
	 * @param indexService
	 */
	public ServiceConnectorImpl(IndexService indexService) {
		this.indexService = indexService;
		this.loadBalancer = createLoadBalancer();
	}

	/**
	 * 
	 * @param indexService
	 * @param connectorInterface
	 */
	public ServiceConnectorImpl(IndexService indexService, Class<?> connectorInterface) {
		this.indexService = indexService;
		this.loadBalancer = createLoadBalancer();
		this.connectorInterface = connectorInterface;
	}

	public IndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	public void setConnectorInterface(Class<?> connectorInterface) {
		this.connectorInterface = connectorInterface;
	}

	protected Class<?> getConnectorInterface() {
		return this.connectorInterface;
	}

	protected LoadBalancer<S> createLoadBalancer() {
		LoadBalancer<S> loadBalancer = new LoadBalancer<S>();
		loadBalancer.setPolicy(getLoadBalancePolicy());
		return loadBalancer;
	}

	protected LoadBalancePolicy<S> getLoadBalancePolicy() {
		return new RoundRobinLoadBalancePolicy<S>();
	}

	public LoadBalancer<S> getLoadBalancer() {
		return this.loadBalancer;
	}

	/**
	 * Start the connector
	 * 
	 * 1. Start index items monitor.
	 * 
	 * 2. Register service.
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized void start(BundleContext bundleContext) {
		if (this.isStarted.get()) {
			return;
		}
		this.isStarted.set(true);

		// -----------------------------------------------------------------------------
		// Start Monitor
		// -----------------------------------------------------------------------------
		// Start monitor to periodically get index items for a service
		this.indexItemsMonitor = createIndexItemsMonitor();
		this.indexItemsMonitor.start();

		// -----------------------------------------------------------------------------
		// Register service
		// -----------------------------------------------------------------------------
		Class connectorInterface = getConnectorInterface();
		if (connectorInterface != null) {
			Hashtable<String, Object> props = new Hashtable<String, Object>();
			this.serviceRegistration = bundleContext.registerService(connectorInterface, this, props);
		}
	}

	public synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	public void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not started.");
		}
	}

	/**
	 * Stop the connector
	 * 
	 * 1. Unregister service.
	 * 
	 * 2. Stop index items monitor.
	 * 
	 */
	public synchronized void stop() {
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		// -----------------------------------------------------------------------------
		// Stop monitor
		// -----------------------------------------------------------------------------
		if (this.indexItemsMonitor != null) {
			this.indexItemsMonitor.stop();
			this.indexItemsMonitor = null;
		}

		// -----------------------------------------------------------------------------
		// Unregister service
		// -----------------------------------------------------------------------------
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	@Override
	public S getService() {
		checkStarted();

		if (this.loadBalancer.isEmpty()) {
			updateLoadBalancer(this.loadBalancer, this.indexItemsMonitor.getLoadedIndexItems());
		}

		S service = null;
		try {
			this.rwLock.readLock().lock();
			LoadBalanceResource<S> resource = this.loadBalancer.getNext();
			if (resource != null) {
				service = resource.getService();
			}
		} finally {
			this.rwLock.readLock().unlock();
		}
		return service;
	}

	protected IndexItemsMonitor createIndexItemsMonitor() {
		IndexItemsMonitor monitor = new IndexItemsMonitor(getConnectorInterface().getSimpleName() + " Monitor", this.indexService) {
			@Override
			protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
				return ServiceConnectorImpl.this.getIndexItems(indexService);
			}

			@Override
			protected void indexItemsUpdated(List<IndexItem> indexItems) {
				updateLoadBalancer(getLoadBalancer(), indexItems);
			}
		};
		monitor.setInterval(30 * 1000);
		return monitor;
	}

	/**
	 * 
	 * @param indexService
	 * @return
	 * @throws IOException
	 */
	protected abstract List<IndexItem> getIndexItems(IndexService indexService) throws IOException;

	/**
	 * 
	 * @param loadBalancer
	 * @param mostRecentIndexItems
	 */
	protected synchronized void updateLoadBalancer(LoadBalancer<S> loadBalancer, List<IndexItem> mostRecentIndexItems) {
		this.rwLock.writeLock().lock();

		try {
			List<LoadBalanceResource<S>> resources = loadBalancer.getResources();

			Map<Integer, LoadBalanceResource<S>> existingResourcesMap = new HashMap<Integer, LoadBalanceResource<S>>();
			for (LoadBalanceResource<S> resource : resources) {
				Integer currIndexItemId = (Integer) resource.getProperty(INDEX_ITEM_ID);
				if (currIndexItemId == null) {
					System.err.println(getConnectorInterface().getSimpleName() + " LoadBalanceResource's 'index_item_id' property is not available.");
					continue;
				}
				existingResourcesMap.put(currIndexItemId, resource);
			}

			Map<Integer, IndexItem> mostRecentIndexItemsMap = new HashMap<Integer, IndexItem>();
			for (IndexItem indexItem : mostRecentIndexItems) {
				Integer indexItemId = indexItem.getIndexItemId();
				mostRecentIndexItemsMap.put(indexItemId, indexItem);
			}

			List<LoadBalanceResource<S>> resourcesToRemove = new ArrayList<LoadBalanceResource<S>>();
			List<LoadBalanceResource<S>> newResources = new ArrayList<LoadBalanceResource<S>>();

			for (LoadBalanceResource<S> resource : resources) {
				Integer indexItemIdInResource = (Integer) resource.getProperty(INDEX_ITEM_ID);
				if (indexItemIdInResource == null) {
					System.err.println(getConnectorInterface().getSimpleName() + " LoadBalanceResource's 'index_item_id' property is not available.");
					continue;
				}

				if (mostRecentIndexItemsMap.containsKey(indexItemIdInResource)) {
					// Corresponding index item of the resource is found
					// (1)update the resource's properties using the index item's properties
					// (e.g. service name, service URL, service context root and generic last heart beat time).
					// (2) put the index item id to the resource's properties using "index_item_id" as prop name.
					IndexItem indexItem = mostRecentIndexItemsMap.get(indexItemIdInResource);

					Integer indexItemId = indexItem.getIndexItemId();
					Map<String, Object> properties = indexItem.getProperties();

					Map<String, Object> newProperties = new HashMap<String, Object>();
					newProperties.put(INDEX_ITEM_ID, indexItemId);
					newProperties.putAll(properties);

					resource.setProperties(newProperties);
					S service = resource.getService();
					updateService(service, newProperties);

				} else {
					// Corresponding index item of the resource is not found
					// (1) remove the resource.
					resourcesToRemove.add(resource);
				}
			}

			for (IndexItem indexItem : mostRecentIndexItems) {
				Integer indexItemId = indexItem.getIndexItemId();

				if (!existingResourcesMap.containsKey(indexItemId)) {
					Map<String, Object> properties = indexItem.getProperties();

					Map<String, Object> newProperties = new HashMap<String, Object>();
					newProperties.put(INDEX_ITEM_ID, indexItemId);
					newProperties.putAll(properties);

					S newService = createService(newProperties);
					if (newService != null) {
						LoadBalanceResource<S> newResource = new LoadBalanceResourceImpl<S>(newService, newProperties);
						newResources.add(newResource);
					}
				}
			}

			if (!resourcesToRemove.isEmpty()) {
				for (LoadBalanceResource<S> resourceToRemove : resourcesToRemove) {
					this.loadBalancer.removeResource(resourceToRemove);
				}
			}

			if (!newResources.isEmpty()) {
				for (LoadBalanceResource<S> newResource : newResources) {
					this.loadBalancer.addResource(newResource);
				}
			}
		} finally {
			this.rwLock.writeLock().unlock();
		}
	}

	/**
	 * Create a new service.
	 * 
	 * @param properties
	 * @return
	 */
	protected abstract S createService(Map<String, Object> properties);

	/**
	 * Update an existing service.
	 * 
	 * @param service
	 * @param properties
	 */
	protected abstract void updateService(S service, Map<String, Object> properties);

}
