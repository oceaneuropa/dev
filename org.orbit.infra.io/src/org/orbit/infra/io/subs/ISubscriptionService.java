package org.orbit.infra.io.subs;

import java.util.List;
import java.util.Map;

public interface ISubscriptionService {

	// ------------------------------------------------
	// Admin
	// ------------------------------------------------
	List<String> getSubscriberTypes();

	List<ISubscriber> getSubscribers(String type);

	List<String> getSubscribableTypes();

	List<ISubscribable> getSubscribables(String type);

	List<ISubscription> getSubscriptions(String subscriberId, String subscribableId);

	// ------------------------------------------------
	// Subscriber
	// ------------------------------------------------
	ISubscriber registerSubscriber(String type, String instanceId, Map<String, Object> properties);

	void unregisterSubscriber(String type, String instanceId);

	boolean hasSubscriber(String type, String instanceId);

	ISubscriber getSubscriber(String type, String instanceId);

	// ------------------------------------------------
	// Subscribable
	// ------------------------------------------------
	ISubscribable registerSubscribable(String type, String instanceId, Map<String, Object> properties);

	void unregisterSubscribable(String type, String instanceId);

	boolean hasSubscribable(String type, String instanceId);

	ISubscribable getSubscribable(String type, String instanceId);

}
