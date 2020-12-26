package org.orbit.infra.io.subscription.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.subscription.ISubsMapping;
import org.orbit.infra.api.subscription.ISubsSource;
import org.orbit.infra.api.subscription.ISubsTarget;
import org.orbit.infra.api.subscription.ISubsType;
import org.orbit.infra.api.subscription.SubsServerAPI;
import org.orbit.infra.api.util.SubsServerUtil;
import org.orbit.infra.io.subscription.ISubscribable;
import org.orbit.infra.io.subscription.ISubscriber;
import org.orbit.infra.io.subscription.ISubscription;
import org.orbit.infra.io.subscription.ISubscriptionService;
import org.origin.common.rest.client.ClientException;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubscriptionServiceImpl implements ISubscriptionService {

	protected String url;
	protected String accessToken;

	/**
	 * 
	 * @param url
	 * @param accessToken
	 */
	public SubscriptionServiceImpl(String url, String accessToken) {
		this.url = url;
		this.accessToken = accessToken;
	}

	// ------------------------------------------------
	// Config
	// ------------------------------------------------
	@Override
	public void setURL(String url) {
		this.url = url;
	}

	@Override
	public String getURL() {
		return this.url;
	}

	@Override
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String getAccessToken() {
		return this.accessToken;
	}

	public SubsServerAPI getAPI() {
		SubsServerAPI api = SubsServerUtil.getAPI(this.url, this.accessToken);
		api = checkAPI(api);
		return api;
	}

	/**
	 * 
	 * @param api
	 * @return
	 */
	protected SubsServerAPI checkAPI(SubsServerAPI api) {
		if (api == null || api.isProxy()) {
			SubsServerAPI resolvedAPI = SubsServerUtil.getAPI(this.url, this.accessToken);
			if (resolvedAPI != null && !resolvedAPI.isProxy()) {
				api = resolvedAPI;
			}
		}
		if (api == null) {
			throw new IllegalStateException("Subscription API is null.");
		}
		if (!SubsServerUtil.isOnline(api)) {
			throw new IllegalStateException("Subscription server is not online.");
		}
		return api;
	}

	/**
	 * 
	 * @param e
	 * @throws IOException
	 */
	protected void handleException(Exception e) throws IOException {
		throw new IOException(e.getMessage(), e);
	}

	// ------------------------------------------------
	// Admin
	// ------------------------------------------------
	/**
	 * Get subscribers types.
	 * 
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<String> getSubscriberTypes() throws IOException {
		List<String> types = new ArrayList<String>();
		try {
			SubsServerAPI api = getAPI();

			List<ISubsType> typeObjs = api.getSourceTypes();
			for (ISubsType typeObj : typeObjs) {
				String currType = typeObj.getType();
				types.add(currType);
			}

		} catch (ClientException e) {
			handleException(e);
		}
		return types;
	}

	/**
	 * Get subscribables types.
	 * 
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<String> getSubscribableTypes() throws IOException {
		List<String> types = new ArrayList<String>();
		try {
			SubsServerAPI api = getAPI();

			List<ISubsType> typeObjs = api.getTargetTypes();
			for (ISubsType typeObj : typeObjs) {
				String currType = typeObj.getType();
				types.add(currType);
			}

		} catch (ClientException e) {
			handleException(e);
		}
		return types;
	}

	/**
	 * Get subscribers by type.
	 * 
	 * @param type
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<ISubscriber> getSubscribers(String type) throws IOException {
		List<ISubscriber> subscribers = new ArrayList<ISubscriber>();
		try {
			SubsServerAPI api = getAPI();

			List<ISubsSource> sources = api.getSources(type);
			for (ISubsSource source : sources) {
				ISubscriber subscriber = new SubscriberImpl(this, source);
				subscribers.add(subscriber);
			}

		} catch (ClientException e) {
			handleException(e);
		}
		return subscribers;
	}

	/**
	 * Get subscribables by type.
	 * 
	 * @param type
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<ISubscribable> getSubscribables(String type) throws IOException {
		List<ISubscribable> subscribers = new ArrayList<ISubscribable>();
		try {
			SubsServerAPI api = getAPI();

			List<ISubsTarget> targets = api.getTargets(type);
			for (ISubsTarget target : targets) {
				ISubscribable subscribable = new SubscribableImpl(this, target);
				subscribers.add(subscribable);
			}

		} catch (ClientException e) {
			handleException(e);
		}
		return subscribers;
	}

	/**
	 * Get subscriptions that a subscriber subscribes.
	 * 
	 * @param subscriberId
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<ISubscription> getSubscriptionsBySubscriber(Integer subscriberId) throws IOException {
		List<ISubscription> subscriptions = new ArrayList<ISubscription>();
		try {
			SubsServerAPI api = getAPI();

			List<ISubsMapping> mappings = api.getMappingsOfSource(subscriberId);
			for (ISubsMapping mapping : mappings) {
				Integer sourceId = mapping.getSourceId();
				Integer targetId = mapping.getTargetId();
				if (!subscriberId.equals(sourceId)) {
					// log error
					System.err.println("subscriberId '" + subscriberId + "' and sourceId '" + sourceId + "' don't match");
					continue;
				}

				ISubsSource source = api.getSource(sourceId);
				ISubsTarget target = api.getTarget(targetId);
				if (source == null || target == null) {
					if (source == null) {
						System.err.println("Source '" + sourceId + "' is not found.");
					}
					if (target == null) {
						System.err.println("Target '" + targetId + "' is not found.");
					}
					continue;
				}

				ISubscriber subscriber = new SubscriberImpl(this, source);
				ISubscribable subscribable = new SubscribableImpl(this, target);
				ISubscription subscription = new SubscriptionImpl(mapping, subscriber, subscribable);
				subscriptions.add(subscription);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriptions;
	}

	/**
	 * Get subscriptions that a subscriber (with specified subscribable type) subscribes.
	 * 
	 * @param subscriberId
	 * @param subscribableType
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<ISubscription> getSubscriptionsBySubscriber(Integer subscriberId, String subscribableType) throws IOException {
		List<ISubscription> subscriptions = new ArrayList<ISubscription>();
		try {
			SubsServerAPI api = getAPI();

			List<ISubsMapping> mappings = api.getMappingsOfSource(subscriberId, subscribableType);
			for (ISubsMapping mapping : mappings) {
				Integer sourceId = mapping.getSourceId();
				Integer targetId = mapping.getTargetId();
				if (!subscriberId.equals(sourceId)) {
					// log error
					System.err.println("subscriberId '" + subscriberId + "' and sourceId '" + sourceId + "' don't match");
					continue;
				}

				ISubsSource source = api.getSource(sourceId);
				ISubsTarget target = api.getTarget(targetId);
				if (source == null || target == null) {
					if (source == null) {
						System.err.println("Source '" + sourceId + "' is not found.");
					}
					if (target == null) {
						System.err.println("Target '" + targetId + "' is not found.");
					}
					continue;
				}

				ISubscriber subscriber = new SubscriberImpl(this, source);
				ISubscribable subscribable = new SubscribableImpl(this, target);
				ISubscription subscription = new SubscriptionImpl(mapping, subscriber, subscribable);
				subscriptions.add(subscription);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriptions;
	}

	/**
	 * Get subscriptions that a subscriber (with specified subscribable type and instance id) subscribes.
	 * 
	 * @param subscriberId
	 * @param subscribableType
	 * @param subscribableInstanceId
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<ISubscription> getSubscriptionsBySubscriber(Integer subscriberId, String subscribableType, String subscribableInstanceId) throws IOException {
		List<ISubscription> subscriptions = new ArrayList<ISubscription>();
		try {
			SubsServerAPI api = getAPI();

			List<ISubsMapping> mappings = api.getMappingsOfSource(subscriberId, subscribableType, subscribableInstanceId);
			for (ISubsMapping mapping : mappings) {
				Integer sourceId = mapping.getSourceId();
				Integer targetId = mapping.getTargetId();
				if (!subscriberId.equals(sourceId)) {
					// log error
					System.err.println("subscriberId '" + subscriberId + "' and sourceId '" + sourceId + "' don't match");
					continue;
				}

				ISubsSource source = api.getSource(sourceId);
				ISubsTarget target = api.getTarget(targetId);
				if (source == null || target == null) {
					if (source == null) {
						System.err.println("Source '" + sourceId + "' is not found.");
					}
					if (target == null) {
						System.err.println("Target '" + targetId + "' is not found.");
					}
					continue;
				}

				ISubscriber subscriber = new SubscriberImpl(this, source);
				ISubscribable subscribable = new SubscribableImpl(this, target);
				ISubscription subscription = new SubscriptionImpl(mapping, subscriber, subscribable);
				subscriptions.add(subscription);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriptions;
	}

	/**
	 * Get subscriptions (of subscribers) that subscribe a subscribable.
	 * 
	 * @param subscribableId
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<ISubscription> getSubscriptionsBySubscribable(Integer subscribableId) throws IOException {
		List<ISubscription> subscriptions = new ArrayList<ISubscription>();
		try {
			SubsServerAPI api = getAPI();

			List<ISubsMapping> mappings = api.getMappingsOfTarget(subscribableId);
			for (ISubsMapping mapping : mappings) {
				Integer sourceId = mapping.getSourceId();
				Integer targetId = mapping.getTargetId();
				if (!subscribableId.equals(targetId)) {
					// log error
					System.err.println("subscribableId '" + subscribableId + "' and targetId '" + targetId + "' don't match");
					continue;
				}

				ISubsSource source = api.getSource(sourceId);
				ISubsTarget target = api.getTarget(targetId);
				if (source == null || target == null) {
					if (source == null) {
						System.err.println("Source '" + sourceId + "' is not found.");
					}
					if (target == null) {
						System.err.println("Target '" + targetId + "' is not found.");
					}
					continue;
				}

				ISubscriber subscriber = new SubscriberImpl(this, source);
				ISubscribable subscribable = new SubscribableImpl(this, target);
				ISubscription subscription = new SubscriptionImpl(mapping, subscriber, subscribable);
				subscriptions.add(subscription);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriptions;
	}

	/**
	 * Get subscriptions (with specified subscriber type) that subscribe a subscribable.
	 * 
	 * @param subscribableId
	 * @param subscriberType
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<ISubscription> getSubscriptionsBySubscribable(Integer subscribableId, String subscriberType) throws IOException {
		List<ISubscription> subscriptions = new ArrayList<ISubscription>();
		try {
			SubsServerAPI api = getAPI();

			List<ISubsMapping> mappings = api.getMappingsOfTarget(subscribableId, subscriberType);
			for (ISubsMapping mapping : mappings) {
				Integer sourceId = mapping.getSourceId();
				Integer targetId = mapping.getTargetId();
				if (!subscribableId.equals(targetId)) {
					// log error
					System.err.println("subscribableId '" + subscribableId + "' and targetId '" + targetId + "' don't match");
					continue;
				}

				ISubsSource source = api.getSource(sourceId);
				ISubsTarget target = api.getTarget(targetId);
				if (source == null || target == null) {
					if (source == null) {
						System.err.println("Source '" + sourceId + "' is not found.");
					}
					if (target == null) {
						System.err.println("Target '" + targetId + "' is not found.");
					}
					continue;
				}

				ISubscriber subscriber = new SubscriberImpl(this, source);
				ISubscribable subscribable = new SubscribableImpl(this, target);
				ISubscription subscription = new SubscriptionImpl(mapping, subscriber, subscribable);
				subscriptions.add(subscription);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriptions;
	}

	/**
	 * Get subscriptions (with specified subscriber type and instance id) that subscribe a subscribable.
	 * 
	 * @param subscribableId
	 * @param subscriberType
	 * @param subscriberInstanceId
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<ISubscription> getSubscriptionsBySubscribable(Integer subscribableId, String subscriberType, String subscriberInstanceId) throws IOException {
		List<ISubscription> subscriptions = new ArrayList<ISubscription>();
		try {
			SubsServerAPI api = getAPI();

			List<ISubsMapping> mappings = api.getMappingsOfTarget(subscribableId, subscriberType, subscriberInstanceId);
			for (ISubsMapping mapping : mappings) {
				Integer sourceId = mapping.getSourceId();
				Integer targetId = mapping.getTargetId();
				if (!subscribableId.equals(targetId)) {
					// log error
					System.err.println("subscribableId '" + subscribableId + "' and targetId '" + targetId + "' don't match");
					continue;
				}

				ISubsSource source = api.getSource(sourceId);
				ISubsTarget target = api.getTarget(targetId);
				if (source == null || target == null) {
					if (source == null) {
						System.err.println("Source '" + sourceId + "' is not found.");
					}
					if (target == null) {
						System.err.println("Target '" + targetId + "' is not found.");
					}
					continue;
				}

				ISubscriber subscriber = new SubscriberImpl(this, source);
				ISubscribable subscribable = new SubscribableImpl(this, target);
				ISubscription subscription = new SubscriptionImpl(mapping, subscriber, subscribable);
				subscriptions.add(subscription);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriptions;
	}

	// ------------------------------------------------
	// Subscriber
	// ------------------------------------------------
	/**
	 * Register a subscriber
	 * 
	 * @param type
	 * @param instanceId
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	@Override
	public ISubscriber registerSubscriber(String type, String instanceId, String name, Map<String, Object> properties) throws IOException {
		ISubscriber subscriber = null;
		try {
			SubsServerAPI api = getAPI();

			ISubsSource source = null;
			if (api.sourceExists(type, instanceId)) {
				source = api.getSource(type, instanceId);
				// log error
				System.err.println("Source with type and instanceId already exists.");

			} else {
				source = api.createSource(type, instanceId, name, properties);
				if (source == null) {
					System.err.println("Source is not created.");
				}
			}
			if (source != null) {
				subscriber = new SubscriberImpl(this, source);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriber;
	}

	/**
	 * Check whether a subscriber with given type and instance id already exists.
	 * 
	 * @param type
	 * @param instanceId
	 * @return
	 * @throws IOException
	 */
	@Override
	public boolean hasSubscriber(String type, String instanceId) throws IOException {
		boolean hasSubscriber = false;
		try {
			SubsServerAPI api = getAPI();
			hasSubscriber = api.sourceExists(type, instanceId);
		} catch (ClientException e) {
			handleException(e);
		}
		return hasSubscriber;
	}

	/**
	 * Get a subscriber by type and instance id.
	 * 
	 * @param type
	 * @param instanceId
	 * @return
	 * @throws IOException
	 */
	@Override
	public ISubscriber getSubscriber(String type, String instanceId) throws IOException {
		ISubscriber subscriber = null;
		try {
			SubsServerAPI api = getAPI();

			ISubsSource source = api.getSource(type, instanceId);
			if (source != null) {
				subscriber = new SubscriberImpl(this, source);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriber;
	}

	/**
	 * Unregister a subscriber
	 * 
	 * @param subscriberId
	 * @throws IOException
	 */
	@Override
	public boolean unregisterSubscriber(Integer subscriberId) throws IOException {
		boolean succeed = false;
		try {
			SubsServerAPI api = getAPI();
			succeed = api.deleteSource(subscriberId, true);
		} catch (ClientException e) {
			handleException(e);
		}
		return succeed;
	}

	/**
	 * Unregister a subscriber
	 * 
	 * @param type
	 * @param instanceId
	 * @throws IOException
	 */
	@Override
	public boolean unregisterSubscriber(String type, String instanceId) throws IOException {
		boolean succeed = false;
		try {
			SubsServerAPI api = getAPI();
			succeed = api.deleteSource(type, instanceId, true);
		} catch (ClientException e) {
			handleException(e);
		}
		return succeed;
	}

	// ------------------------------------------------
	// Subscribable
	// ------------------------------------------------
	/**
	 * Register a subscribable.
	 * 
	 * @param type
	 * @param instanceId
	 * @param name
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	@Override
	public ISubscribable registerSubscribable(String type, String instanceId, String name, String serverId, String serverURL, Map<String, Object> properties) throws IOException {
		ISubscribable subscribable = null;

		try {
			SubsServerAPI api = getAPI();

			ISubsTarget target = null;
			if (api.targetExists(type, instanceId)) {
				target = api.getTarget(type, instanceId);
				// log error
				System.err.println("Target with type and instanceId already exists.");

			} else {
				target = api.createTarget(type, instanceId, name, serverId, serverURL, properties);
				if (target == null) {
					System.err.println("Target is not created.");
				}
			}
			if (target != null) {
				subscribable = new SubscribableImpl(this, target);
			}
		} catch (ClientException e) {
			handleException(e);
		}

		return subscribable;
	}

	/**
	 * Check whether a subscribable with given type and instance id already exists.
	 * 
	 * @param type
	 * @param instanceId
	 * @return
	 * @throws IOException
	 */
	@Override
	public boolean hasSubscribable(String type, String instanceId) throws IOException {
		boolean hasSubscribable = false;
		try {
			SubsServerAPI api = getAPI();
			hasSubscribable = api.targetExists(type, instanceId);
		} catch (ClientException e) {
			handleException(e);
		}
		return hasSubscribable;
	}

	/**
	 * Get a subscribable by type and instance id.
	 * 
	 * @param type
	 * @param instanceId
	 * @return
	 * @throws IOException
	 */
	@Override
	public ISubscribable getSubscribable(String type, String instanceId) throws IOException {
		ISubscribable subscribable = null;
		try {
			SubsServerAPI api = getAPI();

			ISubsTarget target = api.getTarget(type, instanceId);
			if (target != null) {
				subscribable = new SubscribableImpl(this, target);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscribable;
	}

	/**
	 * Unregister a subscribable
	 * 
	 * @param subscribableId
	 * @throws IOException
	 */
	@Override
	public boolean unregisterSubscribable(Integer subscribableId) throws IOException {
		boolean succeed = false;
		try {
			SubsServerAPI api = getAPI();
			succeed = api.deleteTarget(subscribableId, true);
		} catch (ClientException e) {
			handleException(e);
		}
		return succeed;
	}

	/**
	 * Unregister a subscribable
	 * 
	 * @param type
	 * @param instanceId
	 * @throws IOException
	 */
	@Override
	public boolean unregisterSubscribable(String type, String instanceId) throws IOException {
		boolean succeed = false;
		try {
			SubsServerAPI api = getAPI();
			succeed = api.deleteTarget(type, instanceId, true);
		} catch (ClientException e) {
			handleException(e);
		}
		return succeed;
	}

}
