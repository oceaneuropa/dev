package org.orbit.infra.io.subscription;

import java.util.Map;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ISubscription {

	Integer getId();

	String getClientId();

	String getClientURL();

	long getClientHeartbeatTime();

	Map<String, Object> getProperties();

	long getDateCreated();

	long getDateModified();

	ISubscriber getSubscriber();

	ISubscribable getSubscribable();

}
