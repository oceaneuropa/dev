package org.orbit.infra.io.subscription.impl;

import org.orbit.infra.io.subscription.ISubscriptionService;
import org.orbit.infra.io.subscription.SubscriptionFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubscriptionFactoryImpl extends SubscriptionFactory {

	@Override
	public ISubscriptionService createSubscription(String accessToken) {
		return new ISubscriptionServiceImpl(accessToken);
	}

}
