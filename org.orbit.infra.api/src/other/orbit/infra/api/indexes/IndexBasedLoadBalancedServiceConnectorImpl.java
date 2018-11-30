package other.orbit.infra.api.indexes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.origin.common.annotation.Annotated;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalanceResourceImpl;
import org.origin.common.loadbalance.LoadBalancedServiceConnector;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.loadbalance.policy.LoadBalancePolicy;
import org.origin.common.loadbalance.policy.RoundRobinLoadBalancePolicy;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.Pingable;
import org.origin.common.thread.ThreadPoolTimer1;
import org.origin.common.util.DateUtil;
import org.origin.common.util.Printer;
import org.origin.common.util.Timer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public abstract class IndexBasedLoadBalancedServiceConnectorImpl<SERVICE_CLIENT> implements LoadBalancedServiceConnector<SERVICE_CLIENT>, Annotated {

	/* update index items every 15 seconds */
	public static long INDEX_MONITOR_INTERVAL = 15 * Timer.SECOND;

	/* actively ping the service every 5 seconds */
	public static long ACTIVE_PING_INTERVAL = 5 * Timer.SECOND;

	protected boolean debug = true;
	protected IndexServiceClient indexService;
	protected Class<?> connectorClass;
	protected LoadBalancer<SERVICE_CLIENT> loadBalancer;
	protected IndexItemsMonitor indexItemsMonitor;
	protected ServiceRegistration<?> serviceRegistration;
	protected ReadWriteLock rwLock = new ReentrantReadWriteLock();
	protected AtomicBoolean isStarted = new AtomicBoolean(false);
	protected Map<LoadBalanceResource<SERVICE_CLIENT>, PingMonitor> resourceToActivePingMonitorMap = new LinkedHashMap<LoadBalanceResource<SERVICE_CLIENT>, PingMonitor>();

	/**
	 * 
	 * @param indexService
	 */
	public IndexBasedLoadBalancedServiceConnectorImpl(IndexServiceClient indexService) {
		this.indexService = indexService;
		this.loadBalancer = createLoadBalancer();
	}

	/**
	 * 
	 * @param indexService
	 * @param connectorClass
	 */
	public IndexBasedLoadBalancedServiceConnectorImpl(IndexServiceClient indexService, Class<?> connectorClass) {
		this.indexService = indexService;
		this.loadBalancer = createLoadBalancer();
		this.connectorClass = connectorClass;
	}

	public boolean isDebug() {
		return this.debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public IndexServiceClient getIndexService() {
		return indexService;
	}

	public void setIndexService(IndexServiceClient indexService) {
		this.indexService = indexService;
	}

	public void setConnectorClass(Class<?> connectorClass) {
		this.connectorClass = connectorClass;
	}

	protected Class<?> getConnectorClass() {
		return this.connectorClass;
	}

	protected LoadBalancer<SERVICE_CLIENT> createLoadBalancer() {
		LoadBalancer<SERVICE_CLIENT> loadBalancer = new LoadBalancer<SERVICE_CLIENT>();
		loadBalancer.setPolicy(getLoadBalancePolicy());
		return loadBalancer;
	}

	protected LoadBalancePolicy<SERVICE_CLIENT> getLoadBalancePolicy() {
		return new RoundRobinLoadBalancePolicy<SERVICE_CLIENT>();
	}

	public LoadBalancer<SERVICE_CLIENT> getLoadBalancer() {
		return this.loadBalancer;
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
	 * Start the connector
	 * 
	 */
	@SuppressWarnings("unchecked")
	public synchronized void start(BundleContext bundleContext) {
		if (this.isStarted.get()) {
			return;
		}
		this.isStarted.set(true);

		// Start monitor to periodically get index items for a service
		this.indexItemsMonitor = new IndexItemsMonitor(getConnectorClass().getSimpleName() + " Monitor", this.indexService) {
			@Override
			protected List<IndexItem> getIndexItems(IndexServiceClient indexService) throws IOException {
				return IndexBasedLoadBalancedServiceConnectorImpl.this.getIndexItems(indexService);
			}

			@Override
			protected void indexItemsUpdated(List<IndexItem> indexItems) {
				updateLoadBalancer(getLoadBalancer(), indexItems);
			}
		};
		this.indexItemsMonitor.setInterval(INDEX_MONITOR_INTERVAL);
		this.indexItemsMonitor.start();

		// Register connector service
		@SuppressWarnings("rawtypes")
		Class connectorInterface = getConnectorClass();
		if (connectorInterface != null) {
			Hashtable<String, Object> props = new Hashtable<String, Object>();
			this.serviceRegistration = bundleContext.registerService(connectorInterface, this, props);
		}

		// Register Annotated service for dependency injection
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	/**
	 * Stop the connector
	 * 
	 */
	public synchronized void stop(BundleContext bundleContext) {
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		// Unregister Annotated service
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);

		// Unregister service
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		// Stop monitor
		if (this.indexItemsMonitor != null) {
			this.indexItemsMonitor.stop();
			this.indexItemsMonitor = null;
		}
	}

	@Override
	public SERVICE_CLIENT getService() {
		checkStarted();

		if (this.loadBalancer.isEmpty()) {
			updateLoadBalancer(this.loadBalancer, this.indexItemsMonitor.getLoadedIndexItems());
		}

		SERVICE_CLIENT service = null;
		try {
			this.rwLock.readLock().lock();

			LoadBalanceResource<SERVICE_CLIENT> resource = this.loadBalancer.getNext();
			if (resource != null) {
				service = resource.getService();

				if (isPingable(resource)) {
					try {
						((Pingable) service).ping();

					} catch (Exception e) {
						// ping failed
						IndexItem indexItem = resource.getAdapter(IndexItem.class);
						System.err.println(getClass().getSimpleName() + ".getService() ping [" + indexItem.getName() + "] failed: " + e.getMessage());
						if (!hasActivePingMonitor(resource)) {
							startActivePingMonitor(resource, ACTIVE_PING_INTERVAL);
						}

						// Current resource ping failed. Get next resource.
						resource = this.loadBalancer.getNext();
						if (resource != null) {
							service = resource.getService();
						}
					}
				}
			}

		} finally {
			this.rwLock.readLock().unlock();
		}
		return service;
	}

	/**
	 * 
	 * @param indexService
	 * @return
	 * @throws IOException
	 */
	protected abstract List<IndexItem> getIndexItems(IndexServiceClient indexService) throws IOException;

	/**
	 * 
	 * @param loadBalancer
	 * @param latestIndexItems
	 */
	protected synchronized void updateLoadBalancer(LoadBalancer<SERVICE_CLIENT> loadBalancer, List<IndexItem> latestIndexItems) {
		this.rwLock.writeLock().lock();

		try {
			List<LoadBalanceResource<SERVICE_CLIENT>> existingResources = loadBalancer.getResources();

			Map<Integer, LoadBalanceResource<SERVICE_CLIENT>> existingResourcesMap = new HashMap<Integer, LoadBalanceResource<SERVICE_CLIENT>>();
			for (LoadBalanceResource<SERVICE_CLIENT> resource : existingResources) {
				Integer currIndexItemId = (Integer) resource.getProperty(InfraConstants.INDEX_ITEM_ID);
				if (currIndexItemId == null) {
					System.err.println(getConnectorClass().getSimpleName() + " LoadBalanceResource's 'index_item_id' property is not available.");
					continue;
				}
				existingResourcesMap.put(currIndexItemId, resource);
			}

			Map<Integer, IndexItem> latestIndexItemsMap = new HashMap<Integer, IndexItem>();
			for (IndexItem indexItem : latestIndexItems) {
				Integer indexItemId = indexItem.getIndexItemId();
				latestIndexItemsMap.put(indexItemId, indexItem);
			}

			List<LoadBalanceResource<SERVICE_CLIENT>> resourcesToRemove = new ArrayList<LoadBalanceResource<SERVICE_CLIENT>>();
			List<LoadBalanceResource<SERVICE_CLIENT>> newResources = new ArrayList<LoadBalanceResource<SERVICE_CLIENT>>();

			for (LoadBalanceResource<SERVICE_CLIENT> existingResource : existingResources) {
				Integer resourceIndexItemId = (Integer) existingResource.getProperty(InfraConstants.INDEX_ITEM_ID);
				if (resourceIndexItemId == null) {
					System.err.println(getConnectorClass().getSimpleName() + " LoadBalanceResource's 'index_item_id' property is not available.");
					continue;
				}

				if (latestIndexItemsMap.containsKey(resourceIndexItemId)) {
					// Update existing resource using the latest index item.
					IndexItem latestIndexItem = latestIndexItemsMap.get(resourceIndexItemId);
					updateResource(existingResource, latestIndexItem);

				} else {
					resourcesToRemove.add(existingResource);
				}
			}

			for (IndexItem latestIndexItem : latestIndexItems) {
				Integer indexItemId = latestIndexItem.getIndexItemId();

				if (!existingResourcesMap.containsKey(indexItemId)) {
					LoadBalanceResource<SERVICE_CLIENT> newResource = createResource(latestIndexItem);
					if (newResource != null) {
						newResources.add(newResource);
					}
				}
			}

			for (LoadBalanceResource<SERVICE_CLIENT> resourceToRemove : resourcesToRemove) {
				removeResource(resourceToRemove);
			}

			for (LoadBalanceResource<SERVICE_CLIENT> newResource : newResources) {
				addResource(newResource);
			}

		} finally {
			this.rwLock.writeLock().unlock();
		}
	}

	/**
	 * 
	 * @param latestIndexItem
	 * @return
	 */
	protected LoadBalanceResource<SERVICE_CLIENT> createResource(IndexItem latestIndexItem) {
		LoadBalanceResource<SERVICE_CLIENT> newResource = null;

		Integer indexItemId = latestIndexItem.getIndexItemId();
		Map<String, Object> properties = latestIndexItem.getProperties();

		Map<String, Object> newProperties = new HashMap<String, Object>();
		newProperties.put(InfraConstants.INDEX_ITEM_ID, indexItemId);
		newProperties.putAll(properties);

		SERVICE_CLIENT newService = createService(newProperties);
		if (newService != null) {
			newResource = new LoadBalanceResourceImpl<SERVICE_CLIENT>(newService, newProperties);
			// keep a reference to the IndexItem
			newResource.adapt(IndexItem.class, latestIndexItem);
		}

		return newResource;
	}

	/**
	 * 
	 * @param resource
	 */
	protected void addResource(LoadBalanceResource<SERVICE_CLIENT> resource) {
		if (this.debug) {
			IndexItem indexItem = resource.getAdapter(IndexItem.class);
			Integer indexItemId = indexItem.getIndexItemId();
			String indexProviderId = indexItem.getIndexProviderId();
			String indexItemName = indexItem.getName();
			String indexItemType = indexItem.getType();
			System.out.println(getClass().getSimpleName() + ".addResource() IndexItem [" + indexItemId + " - " + indexItemName + " - " + indexItemType + " - " + indexProviderId + "]");
		}

		this.loadBalancer.addResource(resource);
	}

	/**
	 * 
	 * @param resource
	 * @param latestIndexItem
	 */
	protected void updateResource(LoadBalanceResource<SERVICE_CLIENT> resource, IndexItem latestIndexItem) {
		// 1. Get IndexItem attributes
		Integer indexItemId = latestIndexItem.getIndexItemId();
		String indexProviderId = latestIndexItem.getIndexProviderId();
		String indexItemName = latestIndexItem.getName();
		String indexItemType = latestIndexItem.getType();
		Map<String, Object> properties = latestIndexItem.getProperties();

		// 2. Set resource properties
		Map<String, Object> newProperties = new HashMap<String, Object>();
		// Put indexItemId as properties of the resource, so that when index items are updated, a resource can be associated with an updated index item.
		newProperties.put(InfraConstants.INDEX_ITEM_ID, indexItemId);
		newProperties.putAll(properties);

		// update the resource's properties with the index item's properties
		resource.setProperties(newProperties);
		// keep a reference to the IndexItem
		resource.adapt(IndexItem.class, latestIndexItem);

		Date lastHeartbeatTime = LoadBalanceResourceHelper.INSTANCE.getLastHeartBeatTime(resource);
		if (this.debug) {
			// CharSequence relativeTime = TimeUtil.getRelativeTime(lastHeartBeatTime.getTime());
			// System.out.println(getClass().getSimpleName() + ".updateResource() IndexItem [" + indexItemId + " - " + indexProviderId + " - " + indexItemType +
			// " - " + indexItemName + "] Last Heart Beat Time: " + DateUtil.toString(lastHeartBeatTime, DateUtil.SIMPLE_DATE_FORMAT2));
			// System.out.println("\tindexItemId=" + indexItemId);
			// System.out.println("\tindexProviderId=" + indexProviderId);
			// System.out.println("\tindexItemName=" + indexItemName);
			// System.out.println("\tindexItemType=" + indexItemType);
			// System.out.println("\tproperties:");
			// Printer.pl(properties);

			// Object value = properties.get(OriginConstants.LAST_HEARTBEAT_TIME);
			// String type = (value != null) ? value.getClass().getSimpleName() : null;
			// System.out.println("\t" + OriginConstants.LAST_HEARTBEAT_TIME + " = " + value + " (" + type + ")");
			// System.out.println();

			// System.out.println("\tLast Heart Beat Time: " + DateUtil.toString(lastHeartBeatTime, DateUtil.SIMPLE_DATE_FORMAT2));
			// System.out.println("\tCurrent Time: " + DateUtil.toString(new Date(), DateUtil.SIMPLE_DATE_FORMAT2));
			// System.out.println("\tService heart beats: " + relativeTime);
			// System.out.println();
		}

		// 3. Calculate heart beat expiration
		// Check whether the esource's service has expired by checking last heart beat time. The default expiration time is 30 seconds.
		boolean hasHeartBeatTime = LoadBalanceResourceHelper.INSTANCE.hasLastHeartBeatTime(resource);
		if (!hasHeartBeatTime) {
			System.err.println("### ### " + getClass().getSimpleName() + ".updateResource() IndexItem [" + indexItemName + " - " + indexItemType + " - " + indexProviderId + "] doesn't have 'last_heartbeat_time' property.");
			System.err.println("### ### properties:");
			Printer.err_pl(properties);
			System.err.println("### ###");
			System.err.println();
		}

		boolean isHeartbeatExpired = false;
		if (lastHeartbeatTime != null) {
			Date heartbeatExpireTime = DateUtil.addSeconds(lastHeartbeatTime, 30);
			if (heartbeatExpireTime.before(new Date())) {
				isHeartbeatExpired = true;
			}
		} else {
			isHeartbeatExpired = true;
		}

		// 4. Start active ping monitor if heart beat expired.
		// Stop active ping monitor if heart beat not expired.
		if (isPingable(resource)) {
			if (isHeartbeatExpired) {
				System.out.println(getClass().getSimpleName() + ".updateResource() IndexItem [" + indexItemName + "] last heart beat expired.");

				// (1) If expired, start an active ping monitor to actively ping the service with a even shorter period of time and to update the LAST_PING_TIME
				// Default active ping interval is 5 seconds.
				if (!hasActivePingMonitor(resource)) {
					startActivePingMonitor(resource, ACTIVE_PING_INTERVAL);
				}

			} else {
				// System.err.println(getClass().getSimpleName() + ".updateResource() IndexItem [" + indexItemName + "] last heart beat not expired.");

				// (2) If not expired, stop the active ping monitor (if available) to stop pinging the service.
				if (hasActivePingMonitor(resource)) {
					System.out.println(getClass().getSimpleName() + ".updateResource() IndexItem [" + indexItemName + "] last heart beat not expired.");
					stopActivePingMonitor(resource);
				}
			}
		}

		// 5. Update service properties
		SERVICE_CLIENT service = resource.getService();
		if (service != null) {
			updateService(service, newProperties);
		} else {
			System.err.println("### ### " + getClass().getSimpleName() + ".updateResource() resource [" + indexItemName + " - " + indexItemType + " - " + indexProviderId + "] returns null service.");
		}
	}

	/**
	 * 
	 * @param resource
	 */
	protected void removeResource(LoadBalanceResource<SERVICE_CLIENT> resource) {
		if (this.debug) {
			IndexItem indexItem = resource.getAdapter(IndexItem.class);
			Integer indexItemId = indexItem.getIndexItemId();
			String indexProviderId = indexItem.getIndexProviderId();
			String indexItemName = indexItem.getName();
			String indexItemType = indexItem.getType();
			System.out.println(getClass().getSimpleName() + ".removeResource() IndexItem [" + indexItemId + " - " + indexItemName + " - " + indexItemType + " - " + indexProviderId + "]");
		}

		this.loadBalancer.removeResource(resource);

		SERVICE_CLIENT service = resource.getService();
		if (service != null) {
			this.removeService(service);
		}
	}

	/**
	 * Whether a service can ping is not something dynamic. The service can either be available or not available on server side. So it is not the service side
	 * which updates the index item to say whether it is pingable. It is good enough for client to check whether the client of the service has a Pingable
	 * interface.
	 * 
	 * @param resource
	 * @return
	 */
	protected boolean isPingable(LoadBalanceResource<SERVICE_CLIENT> resource) {
		return (resource.getService() instanceof Pingable) ? true : false;
	}

	/**
	 * 
	 * @param resource
	 * @return
	 */
	protected boolean hasActivePingMonitor(LoadBalanceResource<SERVICE_CLIENT> resource) {
		synchronized (this.resourceToActivePingMonitorMap) {
			return (this.resourceToActivePingMonitorMap.containsKey(resource)) ? true : false;
		}
	}

	/**
	 * When the remote resource is pingable, create a thread to periodically ping the service actively with shorter period.
	 * 
	 * @param resource
	 * @param interval
	 */
	protected void startActivePingMonitor(LoadBalanceResource<SERVICE_CLIENT> resource, long interval) {
		IndexItem indexItem = resource.getAdapter(IndexItem.class);
		System.out.println(getClass().getSimpleName() + ".startActivePingMonitor() IndexItem [" + indexItem.getName() + "]");

		String monitorName = (indexItem != null) ? indexItem.getName() : "";
		monitorName = "Ping Monitor [" + monitorName + "]";
		PingMonitor pingMonitor = new PingMonitor(monitorName, resource, interval) {
			@Override
			void pinged(LoadBalanceResource<SERVICE_CLIENT> resource, boolean succeed) {
				updatePing(resource, succeed);
			}
		};
		pingMonitor.start();

		synchronized (this.resourceToActivePingMonitorMap) {
			this.resourceToActivePingMonitorMap.put(resource, pingMonitor);
		}
	}

	/**
	 * Stop the thread for actively ping the service.
	 * 
	 * @param resource
	 */
	protected void stopActivePingMonitor(LoadBalanceResource<SERVICE_CLIENT> resource) {
		IndexItem indexItem = resource.getAdapter(IndexItem.class);
		System.out.println(getClass().getSimpleName() + ".stopActivePingMonitor() IndexItem [" + indexItem.getName() + "]");

		PingMonitor pingMonitor = null;
		synchronized (this.resourceToActivePingMonitorMap) {
			pingMonitor = this.resourceToActivePingMonitorMap.remove(resource);
		}
		if (pingMonitor != null) {
			pingMonitor.stop();
		}
	}

	/**
	 * 
	 * @param resource
	 * @param succeed
	 */
	protected void updatePing(LoadBalanceResource<SERVICE_CLIENT> resource, boolean succeed) {
		Date nowTime = new Date();
		if (succeed) {
			// ping succeeded
			resource.setProperty(LoadBalanceResourceHelper.LAST_PING_TIME, nowTime);
			resource.setProperty(LoadBalanceResourceHelper.LAST_PING_SUCCEED, Boolean.TRUE);

		} else {
			// ping failed
			resource.setProperty(LoadBalanceResourceHelper.LAST_PING_TIME, nowTime);
			resource.setProperty(LoadBalanceResourceHelper.LAST_PING_SUCCEED, Boolean.FALSE);
		}
	}

	/**
	 * Create a new service.
	 * 
	 * @param properties
	 * @return
	 */
	protected abstract SERVICE_CLIENT createService(Map<String, Object> properties);

	/**
	 * Update an existing service.
	 * 
	 * @param service
	 * @param properties
	 */
	protected abstract void updateService(SERVICE_CLIENT service, Map<String, Object> properties);

	/**
	 * Remove an existing service.
	 * 
	 * @param service
	 */
	protected void removeService(SERVICE_CLIENT service) {

	}

	public abstract class PingMonitor extends ThreadPoolTimer1 {

		protected LoadBalanceResource<SERVICE_CLIENT> resource;

		/**
		 * 
		 * @param name
		 * @param resource
		 */
		public PingMonitor(String name, LoadBalanceResource<SERVICE_CLIENT> resource) {
			this(name, resource, 0);
		}

		/**
		 * 
		 * @param name
		 * @param resource
		 * @param interval
		 */
		public PingMonitor(String name, LoadBalanceResource<SERVICE_CLIENT> resource, long interval) {
			super(name);
			setDebug(true);

			this.resource = resource;

			if (interval > 0) {
				setInterval(interval);
			}

			setRunnable(new Runnable() {
				@Override
				public void run() {
					ping();
				}
			});
		}

		protected void ping() {
			SERVICE_CLIENT service = this.resource.getService();
			if (service instanceof Pingable) {
				boolean succeed = false;
				try {
					int ping = ((Pingable) service).ping();
					if (ping > 0) {
						succeed = true;
					}
				} catch (Exception e) {
					IndexItem indexItem = resource.getAdapter(IndexItem.class);
					System.err.println("PingMonitor.ping() ping [" + indexItem.getName() + "] failed. " + e.getMessage());
				}
				pinged(this.resource, succeed);
			}
		}

		/**
		 * 
		 * @param resource
		 * @param succeed
		 */
		abstract void pinged(LoadBalanceResource<SERVICE_CLIENT> resource, boolean succeed);
	}

}
