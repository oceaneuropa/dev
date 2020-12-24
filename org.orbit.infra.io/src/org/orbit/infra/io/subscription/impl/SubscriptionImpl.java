package org.orbit.infra.io.subscription.impl;

import java.util.Map;

import org.orbit.infra.api.subscription.ISubsMapping;
import org.orbit.infra.io.subscription.ISubscribable;
import org.orbit.infra.io.subscription.ISubscriber;
import org.orbit.infra.io.subscription.ISubscription;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubscriptionImpl implements ISubscription {

	protected ISubsMapping mapping;
	protected ISubscriber subscriber;
	protected ISubscribable subscribable;

	/**
	 * 
	 * @param mapping
	 * @param subscriber
	 * @param subscribable
	 */
	public SubscriptionImpl(ISubsMapping mapping, ISubscriber subscriber, ISubscribable subscribable) {
		this.mapping = mapping;
		this.subscriber = subscriber;
		this.subscribable = subscribable;
	}

	@Override
	public Integer getId() {
		return this.mapping.getId();
	}

	@Override
	public String getClientId() {
		return this.mapping.getClientId();
	}

	@Override
	public String getClientURL() {
		return this.mapping.getClientURL();
	}

	@Override
	public long getClientHeartbeatTime() {
		return this.mapping.getClientHeartbeatTime();
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.mapping.getProperties();
	}

	@Override
	public long getDateCreated() {
		return this.mapping.getDateCreated();
	}

	@Override
	public long getDateModified() {
		return this.mapping.getDateModified();
	}

	@Override
	public ISubscriber getSubscriber() {
		return this.subscriber;
	}

	@Override
	public ISubscribable getSubscribable() {
		return this.subscribable;
	}

}
