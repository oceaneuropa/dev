package org.orbit.infra.io.subs;

import org.orbit.infra.io.subs.impl.SubscriptionFactoryImpl;

public abstract class SubscriptionFactory {

	public static SubscriptionFactory INSTANCE = init();

	public static SubscriptionFactory init() {
		SubscriptionFactory factory = new SubscriptionFactoryImpl();
		return factory;
	}

	public synchronized static void setFactory(SubscriptionFactory factory) {
		INSTANCE = factory;
	}

	public synchronized static SubscriptionFactory getFactory() {
		if (INSTANCE == null) {
			INSTANCE = init();
		}
		return INSTANCE;
	}

	public abstract ISubscriptionService createSubscription(String accessToken);

}
