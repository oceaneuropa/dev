package org.orbit.infra.io.subscription;

import org.orbit.infra.io.subscription.impl.SubsFactoryImpl;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public abstract class SubsFactory {

	protected static SubsFactory INSTANCE = init();

	protected static SubsFactory init() {
		SubsFactory factory = new SubsFactoryImpl();
		return factory;
	}

	public synchronized static void setInstance(SubsFactory factory) {
		INSTANCE = factory;
	}

	public synchronized static SubsFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = init();
		}
		return INSTANCE;
	}

	public abstract ISubscriptionService createSubscription(String accessToken);

}
