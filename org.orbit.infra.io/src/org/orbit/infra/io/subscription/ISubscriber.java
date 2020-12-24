package org.orbit.infra.io.subscription;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ISubscriber {

	String getId();

	String getType();

	String getInstanceId();

	Map<String, Object> getProperties();

	/**
	 * Get types subscribables that I have subscribed.
	 * 
	 * @return
	 */
	List<String> getSubscribableTypes();

	/**
	 * Get my subscriptions on specific type of subscribable.
	 * 
	 * @param subscribableType
	 * @return
	 */
	List<ISubscription> getSubscriptions(String subscribableType);

	/**
	 * Get my subscriptions on specific type of subscribable and subscribable instance id.
	 * 
	 * @param subscribableType
	 * @param subscribableInstanceId
	 * @return
	 */
	List<ISubscription> getSubscriptions(String subscribableType, String subscribableInstanceId);

}
