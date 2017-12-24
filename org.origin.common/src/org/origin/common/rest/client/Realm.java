package org.origin.common.rest.client;

import org.origin.common.adapter.IAdaptable;

interface Realm extends IAdaptable {

	String getName();

	CookieManager getCookieManager(String username);

	void setToken(String username, Token token);

	Token getToken(String username);

}
