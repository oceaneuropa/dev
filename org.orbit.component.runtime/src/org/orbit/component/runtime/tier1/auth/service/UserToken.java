package org.orbit.component.runtime.tier1.auth.service;

import java.io.Serializable;
import java.util.Date;

import org.origin.common.util.DateUtil;

public class UserToken implements Serializable {

	private static final long serialVersionUID = -4104051835363057083L;

	protected String username;
	protected String accessToken;
	protected String refreshToken;
	protected Date accessTokenExpireTime;
	protected Date refreshTokenExpireTime;

	public UserToken() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getAccessTokenExpireTime() {
		return this.accessTokenExpireTime;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void setAccessTokenExpireTime(Date accessTokenExpireTime) {
		this.accessTokenExpireTime = accessTokenExpireTime;
	}

	public Date getRefreshTokenExpireTime() {
		return this.refreshTokenExpireTime;
	}

	public void setRefreshTokenExpireTime(Date refreshTokenExpireTime) {
		this.refreshTokenExpireTime = refreshTokenExpireTime;
	}

	public long getAccessTokenExpiresInMinutes() {
		if (this.accessTokenExpireTime == null) {
			return -1;
		}
		return DateUtil.getMinutesBetween(new Date(), this.accessTokenExpireTime);
	}

	public long getRefreshTokenExpiresInMinutes() {
		if (this.refreshTokenExpireTime == null) {
			return -1;
		}
		return DateUtil.getMinutesBetween(this.refreshTokenExpireTime, new Date());
	}

}
