package smu.sm.entity;

import java.io.Serializable;

public class Token implements Serializable {
	private static final long serialVersionUID = -5259354361302051742L;

	private String token;
	private int frequency;

	public Token(String token){}

	public Token(String token, int frequency) {
		super();
		this.token = token;
		this.frequency = frequency;
	}
	public String getToken() { return token; }
	public void setToken(String token) { this.token = token; }

	public int getFrequency() { return frequency; }
	public void setFrequency(int frequency) { this.frequency = frequency; }
	public void incrFrequency(){ this.frequency++; }
}
