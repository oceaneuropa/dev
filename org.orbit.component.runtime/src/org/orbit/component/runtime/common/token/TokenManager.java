package org.orbit.component.runtime.common.token;

public interface TokenManager {

	void activate();

	void deactivate();

	UserToken[] getUserTokens();

	UserToken getUserToken(String username);

	void setUserToken(String username, UserToken userToken);

}
