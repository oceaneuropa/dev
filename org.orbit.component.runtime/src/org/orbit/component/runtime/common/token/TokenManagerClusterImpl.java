package org.orbit.component.runtime.common.token;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.origin.common.cluster.EventBus;
import org.origin.common.cluster.EventBusUtil;

public class TokenManagerClusterImpl implements TokenManager {

	protected String serviceFullName;
	protected String clusterName;
	protected EventBus eventBus;
	protected AtomicBoolean activated = new AtomicBoolean(false);

	/**
	 * 
	 * @param serviceFullName
	 * @param clusterName
	 */
	public TokenManagerClusterImpl(String serviceFullName, String clusterName) {
		this.serviceFullName = serviceFullName;
		this.clusterName = clusterName;
	}

	@Override
	public void activate() {
		// System.out.println(getClass().getSimpleName() + ".activate()");
		if (!this.activated.compareAndSet(false, true)) {
			return;
		}

		this.eventBus = EventBusUtil.getEventBus();
		try {
			this.eventBus.joinCluster(this.clusterName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deactivate() {
		System.out.println(getClass().getSimpleName() + ".deactivate()");
		if (!this.activated.compareAndSet(true, false)) {
			return;
		}

		if (this.eventBus != null) {
			try {
				this.eventBus.leaveCluster(this.clusterName);
				this.eventBus.closeCluster(this.clusterName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.eventBus = null;
		}
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
			this.eventBus.setProperty(this.serviceFullName, username, userToken, this.clusterName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
