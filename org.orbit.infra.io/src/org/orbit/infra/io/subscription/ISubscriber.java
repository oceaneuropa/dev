package org.orbit.infra.io.subscription;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ISubscriber {

	Integer getId();

	String getType();

	String getInstanceId();

	String getName();

	Map<String, Object> getProperties();

	long getDateCreated();

	long getDateModified();

	/**
	 * Get types of subscribables that I have subscribed.
	 * 
	 * @return
	 * @throws IOException
	 */
	List<String> getSubscriptionTypes() throws IOException;

	/**
	 * Get subscriptions (with specified subscribable type) that I have subscribed.
	 * 
	 * @param subscribableType
	 * @return
	 * @throws IOException
	 */
	List<ISubscription> getSubscriptions(String subscribableType) throws IOException;

	/**
	 * Get subscriptions (with specified subscribable type and instance id) that I have subscribed.
	 * 
	 * @param subscribableType
	 * @param subscribableInstanceId
	 * @return
	 * @throws IOException
	 */
	List<ISubscription> getSubscriptions(String subscribableType, String subscribableInstanceId) throws IOException;

}
