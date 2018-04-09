package com.chenxin.houseparent.domain;

import org.apache.shiro.authc.AuthenticationToken;

import java.io.Serializable;

public abstract class People implements AuthenticationToken,Serializable {

	@Override
	public String getPrincipal() {
		return this.username;
	}

	@Override
	public Object getCredentials() {
		return this.password;
	}

	private String username;
	private String password;

	public People() {
		// TODO Auto-generated constructor stub
	}

	public People(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "People [username=" + username + ", password=" + password + "]";
	}

}
