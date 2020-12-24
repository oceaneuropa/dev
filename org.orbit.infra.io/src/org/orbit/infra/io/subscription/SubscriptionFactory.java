package org.orbit.infra.io.subscription;

import org.orbit.infra.io.subscription.impl.SubscriptionFactoryImpl;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public abstract class SubscriptionFactory {

	protected static SubscriptionFactory INSTANCE = init();

	protected static SubscriptionFactory init() {
		SubscriptionFactory factory = new SubscriptionFactoryImpl();
		return factory;
	}

	public synchronized static void setInstance(SubscriptionFactory factory) {
		INSTANCE = factory;
	}

	public synchronized static SubscriptionFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = init();
		}
		return INSTANCE;
	}

	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public abstract ISubscriptionService createSubscription(String accessToken);

}
