package org.orbit.infra.io.subscription;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ISubscription {

	String getId();

	ISubscriber getSubscriberId();

	ISubscribable getSubscribableId();

}
