package org.orbit.infra.io.subscription;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ISubscriptionService {

	// ------------------------------------------------
	// Config
	// ------------------------------------------------
	void setURL(String url);

	String getURL();

	void setAccessToken(String accessToken);

	String getAccessToken();

	// ------------------------------------------------
	// Admin
	// ------------------------------------------------
	/**
	 * Get subscribers types.
	 * 
	 * @return
	 * @throws IOException
	 */
	List<String> getSubscriberTypes() throws IOException;

	/**
	 * Get subscribables types.
	 * 
	 * @return
	 * @throws IOException
	 */
	List<String> getSubscribableTypes() throws IOException;

	/**
	 * Get subscribers by type.
	 * 
	 * @param type
	 * @return
	 * @throws IOException
	 */
	List<ISubscriber> getSubscribers(String type) throws IOException;

	/**
	 * Get subscribables by type.
	 * 
	 * @param type
	 * @return
	 * @throws IOException
	 */
	List<ISubscribable> getSubscribables(String type) throws IOException;

	/**
	 * Get subscriptions that a subscriber subscribes.
	 * 
	 * @param subscriberId
	 * @return
	 * @throws IOException
	 */
	List<ISubscription> getSubscriptionsBySubscriber(Integer subscriberId) throws IOException;

	/**
	 * Get subscriptions that a subscriber (with specified subscribable type) subscribes.
	 * 
	 * @param subscriberId
	 * @param subscribableType
	 * @return
	 * @throws IOException
	 */
	List<ISubscription> getSubscriptionsBySubscriber(Integer subscriberId, String subscribableType) throws IOException;

	/**
	 * Get subscriptions that a subscriber (with specified subscribable type and instance id) subscribes.
	 * 
	 * @param subscriberId
	 * @param subscribableType
	 * @param subscribableInstanceId
	 * @return
	 * @throws IOException
	 */
	List<ISubscription> getSubscriptionsBySubscriber(Integer subscriberId, String subscribableType, String subscribableInstanceId) throws IOException;

	/**
	 * Get subscriptions (of subscribers) that subscribe a subscribable.
	 * 
	 * @param subscribableId
	 * @return
	 * @throws IOException
	 */
	List<ISubscription> getSubscriptionsBySubscribable(Integer subscribableId) throws IOException;

	/**
	 * Get subscriptions (with specified subscriber type) that subscribe a subscribable.
	 * 
	 * @param subscribableId
	 * @param subscriberType
	 * @return
	 * @throws IOException
	 */
	List<ISubscription> getSubscriptionsBySubscribable(Integer subscribableId, String subscriberType) throws IOException;

	/**
	 * Get subscriptions (with specified subscriber type and instance id) that subscribe a subscribable.
	 * 
	 * @param subscribableId
	 * @param subscriberType
	 * @param subscriberInstanceId
	 * @return
	 * @throws IOException
	 */
	List<ISubscription> getSubscriptionsBySubscribable(Integer subscribableId, String subscriberType, String subscriberInstanceId) throws IOException;

	// ------------------------------------------------
	// Subscriber
	// ------------------------------------------------
	/**
	 * Register a subscriber
	 * 
	 * @param type
	 * @param instanceId
	 * @param name
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	ISubscriber registerSubscriber(String type, String instanceId, String name, Map<String, Object> properties) throws IOException;

	/**
	 * Check whether a subscriber with given type and instance id already exists.
	 * 
	 * @param type
	 * @param instanceId
	 * @return
	 * @throws IOException
	 */
	boolean hasSubscriber(String type, String instanceId) throws IOException;

	/**
	 * Get a subscriber by type and instance id.
	 * 
	 * @param type
	 * @param instanceId
	 * @return
	 * @throws IOException
	 */
	ISubscriber getSubscriber(String type, String instanceId) throws IOException;

	/**
	 * Unregister a subscriber
	 * 
	 * @param subscriberId
	 * @throws IOException
	 */
	boolean unregisterSubscriber(Integer subscriberId) throws IOException;

	/**
	 * Unregister a subscriber
	 * 
	 * @param type
	 * @param instanceId
	 * @throws IOException
	 */
	boolean unregisterSubscriber(String type, String instanceId) throws IOException;

	// ------------------------------------------------
	// Subscribable
	// ------------------------------------------------
	/**
	 * Register a subscribable.
	 * 
	 * @param type
	 * @param instanceId
	 * @param name
	 * @param serverId
	 * @param serverURL
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	ISubscribable registerSubscribable(String type, String instanceId, String name, String serverId, String serverURL, Map<String, Object> properties) throws IOException;

	/**
	 * Check whether a subscribable with given type and instance id already exists.
	 * 
	 * @param type
	 * @param instanceId
	 * @return
	 * @throws IOException
	 */
	boolean hasSubscribable(String type, String instanceId) throws IOException;

	/**
	 * Get a subscribable by type and instance id.
	 * 
	 * @param type
	 * @param instanceId
	 * @return
	 * @throws IOException
	 */
	ISubscribable getSubscribable(String type, String instanceId) throws IOException;

	/**
	 * Unregister a subscribable
	 * 
	 * @param subscribableId
	 * @throws IOException
	 */
	boolean unregisterSubscribable(Integer subscribableId) throws IOException;

	/**
	 * Unregister a subscribable.
	 * 
	 * @param type
	 * @param instanceId
	 * @throws IOException
	 */
	boolean unregisterSubscribable(String type, String instanceId) throws IOException;

}
