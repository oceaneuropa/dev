package org.orbit.infra.io.subscription;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ISubscribable {

	String getId();

	String getType();

	String getInstanceId();

	String getServerId();

	String getServerURL();

	Map<String, Object> getProperties();

	/**
	 * Get types subscribers that have subscribed me.
	 * 
	 * @return
	 */
	List<String> getSubscriberTypes();

	/**
	 * Get subscriptions with specific type of subscribers that subscribes me.
	 * 
	 * @param subscriberType
	 * @return
	 */
	List<ISubscription> getSubscriptions(String subscriberType);

	/**
	 * Get subscriptions with specific type of subscribers and subscriber instance id that subscribes me.
	 * 
	 * @return
	 */
	List<ISubscription> getSubscriptions(String subscriberType, String subscriberInstanceId);

}
