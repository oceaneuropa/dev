package org.origin.common.rest.client;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.adapter.AdaptorSupport;

class RealmImpl implements Realm {

	private String name;
	private Map<String, CookieManager> cookieManagers = new HashMap<String, CookieManager>();
	private Map<String, Token> tokens = new HashMap<String, Token>();
	private AdaptorSupport adaptorSupport = new AdaptorSupport();

	public RealmImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public synchronized CookieManager getCookieManager(String username) {
		CookieManager cookieManager = this.cookieManagers.get(username);
		if (cookieManager == null) {
			cookieManager = new CookieManagerImpl();
			this.cookieManagers.put(username, cookieManager);
		}
		return cookieManager;
	}

	@Override
	public synchronized void setToken(String username, Token token) {
		this.tokens.put(username, token);
	}

	@Override
	public synchronized Token getToken(String username) {
		return this.tokens.get(username);
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}
