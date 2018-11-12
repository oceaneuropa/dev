package com.osgi.example1.io;

import java.io.Serializable;

public class People implements Serializable {

	private static final long serialVersionUID = 8294180014912103005L;

	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private transient String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}