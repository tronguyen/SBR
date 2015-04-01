package smu.sm.entity;

import java.io.Serializable;

public class Token implements Serializable {
	private static final long serialVersionUID = -5259354361302051742L;

	private String token;

	public Token(String token){
		this.token = token;
	}

	public String getToken() { return token; }
	public void setToken(String token) { this.token = token; }
}
