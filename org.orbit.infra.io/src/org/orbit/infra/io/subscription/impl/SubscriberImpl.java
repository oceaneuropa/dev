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
public class SubscriberImpl implements ISubscriber {

	protected ISubscriptionService service;
	protected ISubsSource source;

	/**
	 * 
	 * @param service
	 * @param source
	 */
	public SubscriberImpl(ISubscriptionService service, ISubsSource source) {
		this.service = service;
		this.source = source;
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
		return this.source.getId();
	}

	@Override
	public String getType() {
		return this.source.getType();
	}

	@Override
	public String getInstanceId() {
		return this.source.getInstanceId();
	}

	@Override
	public String getName() {
		return this.source.getName();
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.source.getProperties();
	}

	@Override
	public long getDateCreated() {
		return this.source.getDateCreated();
	}

	@Override
	public long getDateModified() {
		return this.source.getDateModified();
	}

	@Override
	public List<String> getSubscriptionTypes() throws IOException {
		List<String> types = new ArrayList<String>();
		try {
			SubsServerAPI api = getAPI();

			Integer sourceId = getId();
			List<ISubsType> targetTypes = api.getMappingTargetTypesOfSource(sourceId);
			for (ISubsType typeObj : targetTypes) {
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
	public List<ISubscription> getSubscriptions(String subscribableType) throws IOException {
		List<ISubscription> subscriptions = new ArrayList<ISubscription>();
		try {
			SubsServerAPI api = getAPI();

			Integer sourceId = getId();
			List<ISubsMapping> mappings = api.getMappingsOfSource(sourceId, subscribableType);
			for (ISubsMapping mapping : mappings) {
				Integer targetId = mapping.getTargetId();
				ISubsTarget target = api.getTarget(targetId);
				if (target == null) {
					// log error
					System.err.println("Target '" + targetId + "' is not found.");
					continue;
				}

				ISubscribable subscribable = new SubscribableImpl(this.service, target);
				ISubscription subscription = new SubscriptionImpl(mapping, this, subscribable);
				subscriptions.add(subscription);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriptions;
	}

	@Override
	public List<ISubscription> getSubscriptions(String subscribableType, String subscribableInstanceId) throws IOException {
		List<ISubscription> subscriptions = new ArrayList<ISubscription>();
		try {
			SubsServerAPI api = getAPI();

			Integer sourceId = getId();
			List<ISubsMapping> mappings = api.getMappingsOfSource(sourceId, subscribableType, subscribableInstanceId);
			for (ISubsMapping mapping : mappings) {
				Integer targetId = mapping.getTargetId();
				ISubsTarget target = api.getTarget(targetId);
				if (target == null) {
					// log error
					System.err.println("Target '" + targetId + "' is not found.");
					continue;
				}

				ISubscribable subscribable = new SubscribableImpl(this.service, target);
				ISubscription subscription = new SubscriptionImpl(mapping, this, subscribable);
				subscriptions.add(subscription);
			}
		} catch (ClientException e) {
			handleException(e);
		}
		return subscriptions;
	}

}
