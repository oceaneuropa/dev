package org.orbit.infra.io.subscription.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.subscription.ISubsMapping;
import org.orbit.infra.api.subscription.ISubsSource;
import org.orbit.infra.api.subscription.ISubsTarget;
import org.orbit.infra.api.subscription.ISubsType;
import org.orbit.infra.api.subscription.SubsServerAPI;
import org.orbit.infra.io.subscription.ISubscribable;
import org.orbit.infra.io.subscription.ISubscriber;
import org.orbit.infra.io.subscription.ISubscription;
import org.orbit.infra.io.subscription.ISubscriptionService;
import org.origin.common.rest.client.ClientException;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubscribableImpl implements ISubscribable {

	protected ISubscriptionService service;
	protected ISubsTarget target;

	/**
	 * 
	 * @param service
	 * @param target
	 */
	public SubscribableImpl(ISubscriptionService service, ISubsTarget target) {
		this.service = service;
		this.target = target;
	}

	protected SubsServerAPI getAPI() {
		SubsServerAPI api = ((SubscriptionServiceImpl) this.service).getAPI();
		return api;
	}

	protected void handleException(Exception e) throws IOException {
		throw new IOException(e.getMessage(), e);
	}

	@Override
	public Integer getId() {
		return this.target.getId();
	}

	@Override
	public String getType() {
		return this.target.getType();
	}

	@Override
	public String getInstanceId() {
		return this.target.getInstanceId();
	}

	@Override
	public String getName() {
		return this.target.getName();
	}

	@Override
	public String getServerId() {
		return this.target.getServerId();
	}

	@Override
	public String getServerURL() {
		return this.target.getServerURL();
	}

	@Override
	public long getServerHeartbeatTime() {
		return this.target.getServerHeartbeatTime();
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.target.getProperties();
	}

	@Override
	public long getDateCreated() {
		return this.target.getDateCreated();
	}

	@Override
	public long getDateModified() {
		return this.target.getDateModified();
	}

	@Override
	public List<String> getSubscriptionTypes() throws IOException {
		List<String> types = new ArrayList<String>();
		try {
			SubsServerAPI api = getAPI();

			Integer targetId = getId();
			List<ISubsType> sourceTypes = api.getMappingSourceTypesOfTarget(targetId);
			for (ISubsType typeObj : sourceTypes) {
				String type = typeObj.getType();
				if (type == null) {
					// log error
					System.err.println("Type '" + typeObj.getId() + "' - type value is null.");
					continue;
				}
				if (!types.contains(type)) {
					types.add(type);
				}
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return types;
	}

	@Override
	public List<ISubscription> getSubscriptions(String subscriberType) throws IOException {
		List<ISubscription> subscriptions = new ArrayList<ISubscription>();
		try {
			SubsServerAPI api = getAPI();

			Integer targetId = getId();
			List<ISubsMapping> mappings = api.getMappingsOfTarget(targetId, subscriberType);
			for (ISubsMapping mapping : mappings) {
				Integer sourceId = mapping.getSourceId();
				ISubsSource source = api.getSource(sourceId);
				if (source == null) {
					// log error
					System.err.println("Source '" + sourceId + "' is not found.");
					continue;
				}

				ISubscriber subscriber = new SubscriberImpl(this.service, source);
				ISubscription subscription = new SubscriptionImpl(mapping, subscriber, this);
				subscriptions.add(subscription);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriptions;
	}

	@Override
	public List<ISubscription> getSubscriptions(String subscriberType, String subscriberInstanceId) throws IOException {
		List<ISubscription> subscriptions = new ArrayList<ISubscription>();
		try {
			SubsServerAPI api = getAPI();

			Integer targetId = getId();
			List<ISubsMapping> mappings = api.getMappingsOfTarget(targetId, subscriberType, subscriberInstanceId);
			for (ISubsMapping mapping : mappings) {
				Integer sourceId = mapping.getSourceId();
				ISubsSource source = api.getSource(sourceId);
				if (source == null) {
					// log error
					System.err.println("Source '" + sourceId + "' is not found.");
					continue;
				}

				ISubscriber subscriber = new SubscriberImpl(this.service, source);
				ISubscription subscription = new SubscriptionImpl(mapping, subscriber, this);
				subscriptions.add(subscription);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriptions;
	}

}
