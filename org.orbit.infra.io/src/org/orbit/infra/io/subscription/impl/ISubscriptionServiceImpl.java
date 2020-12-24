package org.orbit.infra.io.subscription.impl;

import java.util.List;

import org.orbit.infra.io.subscription.ISubscriptionService;

public class ISubscriptionServiceImpl implements ISubscriptionService {

	protected String accessToken;

	public ISubscriptionServiceImpl(String accessToken) {
		this.accessToken = accessToken;
	}

	// ------------------------------------------------
	// Admin
	// ------------------------------------------------
	/**
	 * Get subscribers types.
	 * 
	 * @return
	 */
	public List<String> getSubscriberTypes() {

	}

}
