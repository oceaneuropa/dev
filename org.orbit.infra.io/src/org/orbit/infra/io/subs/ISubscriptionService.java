package org.orbit.infra.io.subs;

import java.util.List;

public interface ISubscriptionService {

	List<ISubscription> getSubscriptions();

	List<ISubscribable> getSubscribables();

}
