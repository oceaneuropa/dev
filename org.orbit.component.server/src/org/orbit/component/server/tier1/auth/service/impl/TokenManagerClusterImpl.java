package org.orbit.component.server.tier1.auth.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.component.server.tier1.auth.service.TokenManager;
import org.orbit.component.server.tier1.auth.service.UserToken;
import org.origin.common.cluster.EventBus;
import org.origin.common.cluster.EventBusUtil;

public class TokenManagerClusterImpl implements TokenManager {

	protected String clusterName;
	protected EventBus eventBus;
	protected AtomicBoolean activated = new AtomicBoolean(false);

	/**
	 * 
	 * @param clusterName
	 */
	public TokenManagerClusterImpl(String clusterName) {
		this.clusterName = clusterName;
	}

	public boolean isActivated() {
		return this.activated.get();
	}

	protected void checkActivated() {
		if (!isActivated()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not activated.");
		}
	}

	@Override
	public void activate() {
		System.out.println(getClass().getSimpleName() + ".activate()");
		if (!this.activated.compareAndSet(false, true)) {
			return;
		}

		this.eventBus = EventBusUtil.getEventBus();
	}

	@Override
	public void deactivate() {
		System.out.println(getClass().getSimpleName() + ".deactivate()");
		if (!this.activated.compareAndSet(true, false)) {
			return;
		}

		this.eventBus = null;
	}

	@Override
	public UserToken[] getUserTokens() {
		checkActivated();

		List<UserToken> userTokens = new ArrayList<UserToken>();

		Map<Object, Object> properties = this.eventBus.getLocalProperties(this.clusterName);
		if (properties != null) {
			for (Iterator<Object> keyItor = properties.keySet().iterator(); keyItor.hasNext();) {
				Object key = keyItor.next();
				Object value = properties.get(key);
				if (value instanceof UserToken) {
					userTokens.add((UserToken) value);
				}
			}
		}

		return userTokens.toArray(new UserToken[userTokens.size()]);
	}

	@Override
	public UserToken getUserToken(String username) {
		checkActivated();

		UserToken userToken = null;
		Map<Object, Object> properties = this.eventBus.getLocalProperties(this.clusterName);
		if (properties != null) {
			Object value = properties.get(username);
			if (value instanceof UserToken) {
				userToken = (UserToken) value;
			}
		}
		return userToken;
	}

	@Override
	public void setUserToken(String username, UserToken userToken) {
		checkActivated();

		try {
			this.eventBus.setProperty(this, username, userToken, this.clusterName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
