package org.orbit.infra.io.subscription;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ISubscribable {

	Integer getId();

	String getType();

	String getInstanceId();

	String getName();

	String getServerId();

	String getServerURL();

	long getServerHeartbeatTime();

	Map<String, Object> getProperties();

	long getDateCreated();

	long getDateModified();

	/**
	 * Get types of subscribers that have subscribed me.
	 * 
	 * @return
	 * @throws IOException
	 */
	List<String> getSubscriptionTypes() throws IOException;

	/**
	 * Get subscriptions (with specified subscriber type) that subscribe me.
	 * 
	 * @param subscriberType
	 * @return
	 * @throws IOException
	 */
	List<ISubscription> getSubscriptions(String subscriberType) throws IOException;

	/**
	 * Get subscriptions (with specified subscriber type and instance id) that subscribe me.
	 * 
	 * @param subscriberType
	 * @param subscriberInstanceId
	 * @return
	 * @throws IOException
	 */
	List<ISubscription> getSubscriptions(String subscriberType, String subscriberInstanceId) throws IOException;

}
